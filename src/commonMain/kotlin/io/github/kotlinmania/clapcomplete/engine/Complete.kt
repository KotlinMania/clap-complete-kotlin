// port-lint: source engine/complete.rs
package io.github.kotlinmania.clapcomplete.engine

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clap.ValueHint
import io.github.kotlinmania.clap.builder.PossibleValue
import io.github.kotlinmania.clapcomplete.aot.generator.Utils

internal sealed interface ParseState {
    data object ValueDone : ParseState

    data class Opt(
        val opt: Arg,
        val count: Int = 1,
    ) : ParseState

    data class Pos(
        val posIndex: Int,
        val numArg: Int = 1,
    ) : ParseState
}

/**
 * Complete the given command, shell-agnostic.
 */
fun complete(
    cmd: Command,
    args: List<String>,
    argIndex: Int,
    currentDir: String? = null,
): List<CompletionCandidate> {
    var currentCmd = cmd
    var posIndex = 1
    var isEscaped = false
    var nextState: ParseState = ParseState.ValueDone

    val startIndex = if (args.isNotEmpty() && args[0] == cmd.getName()) 1 else 0
    var i = startIndex

    while (i < argIndex && i < args.size) {
        val arg = args[i]
        val currentState = nextState
        nextState = ParseState.ValueDone

        if (arg == "--") {
            isEscaped = true
        } else if (optAllowsHyphen(currentState, arg)) {
            if (currentState is ParseState.Opt) {
                nextState = parseOptValue(currentState.opt, currentState.count)
            }
        } else if (arg.startsWith("--")) {
            val flagWithVal = arg.removePrefix("--")
            val eqIdx = flagWithVal.indexOf('=')
            val flag = if (eqIdx >= 0) flagWithVal.substring(0, eqIdx) else flagWithVal
            val value = if (eqIdx >= 0) flagWithVal.substring(eqIdx + 1) else null

            val opt =
                currentCmd.getArguments().firstOrNull { a ->
                    a.getLong() == flag
                }
            if (opt != null) {
                if (opt.getAction().takesValues() && value == null) {
                    nextState = ParseState.Opt(opt, 1)
                }
            } else if (posAllowsHyphen(currentCmd, posIndex)) {
                val (ns, pi) = parsePositional(currentCmd, posIndex, isEscaped, currentState)
                nextState = ns
                posIndex = pi
            }
        } else if (arg.startsWith("-") && arg.length > 1) {
            val (_, takesValueOpt, _) = parseShortflags(currentCmd, arg.removePrefix("-"))
            if (takesValueOpt != null) {
                nextState = ParseState.Opt(takesValueOpt, 1)
            }
        } else {
            val sc =
                currentCmd.getSubcommands().firstOrNull {
                    it.getName() == arg || it.getVisibleAliases().contains(arg)
                }
            if (sc != null) {
                currentCmd = sc
                posIndex = 1
            } else {
                val (ns, pi) = parsePositional(currentCmd, posIndex, isEscaped, currentState)
                nextState = ns
                posIndex = pi
            }
        }
        i++
    }

    val currentArg = if (argIndex < args.size) args[argIndex] else ""
    return completeArg(currentArg, currentCmd, currentDir, posIndex, isEscaped, nextState)
}

private fun completeArg(
    arg: String,
    cmd: Command,
    currentDir: String?,
    posIndex: Int,
    isEscaped: Boolean,
    state: ParseState,
): List<CompletionCandidate> {
    val completions = mutableListOf<CompletionCandidate>()

    when (state) {
        is ParseState.ValueDone -> {
            completions.addAll(completeSubcommand(arg, cmd))
            val positionals = Utils.positionals(cmd)
            if (posIndex - 1 < positionals.size && posIndex > 0) {
                completions.addAll(completeArgValue(arg, positionals[posIndex - 1], currentDir))
            }
            if (!isEscaped) {
                completions.addAll(completeOption(arg, cmd, currentDir))
            }
        }
        is ParseState.Pos -> {
            val positionals = Utils.positionals(cmd)
            if (posIndex - 1 < positionals.size && posIndex > 0) {
                val positional = positionals[posIndex - 1]
                completions.addAll(completeArgValue(arg, positional, currentDir))
                completions.addAll(completeOption(arg, cmd, currentDir))
            }
        }
        is ParseState.Opt -> {
            completions.addAll(completeArgValue(arg, state.opt, currentDir))
            if (state.count > 1) {
                completions.addAll(completeArg(arg, cmd, currentDir, posIndex, isEscaped, ParseState.ValueDone))
            }
        }
    }

    val visible = completions.filter { !it.isHideSet() }
    val finalCompletions = if (visible.isNotEmpty()) visible else completions

    val seen = mutableSetOf<String>()
    val deduped = mutableListOf<CompletionCandidate>()
    for (c in finalCompletions) {
        val key = c.getId() ?: c.getValue()
        if (seen.add(key)) {
            deduped.add(c)
        }
    }

    return deduped.sortedWith(
        compareBy<CompletionCandidate> { it.getTag()?.toString() ?: "" }
            .thenBy { it.getDisplayOrder() ?: 0 }
            .thenBy { it.getValue() },
    )
}

internal fun completeOption(
    arg: String,
    cmd: Command,
    currentDir: String?,
): List<CompletionCandidate> {
    val completions = mutableListOf<CompletionCandidate>()
    if (arg.isEmpty()) {
        completions.addAll(longsAndVisibleAliases(cmd))
        completions.addAll(hiddenLongsAliases(cmd))
        val dashOrArg = "-"
        completions.addAll(shortsAndVisibleAliases(cmd).map { it.addPrefix(dashOrArg) })
    } else if (arg == "-") {
        completions.addAll(shortsAndVisibleAliases(cmd).map { it.addPrefix("-") })
        completions.addAll(longsAndVisibleAliases(cmd))
        completions.addAll(hiddenLongsAliases(cmd))
    } else if (arg.startsWith("--")) {
        val flagWithVal = arg.removePrefix("--")
        val eqIdx = flagWithVal.indexOf('=')
        if (eqIdx >= 0) {
            val flag = flagWithVal.substring(0, eqIdx)
            val value = flagWithVal.substring(eqIdx + 1)
            val opt = cmd.getArguments().firstOrNull { it.getLong() == flag }
            if (opt != null) {
                completions.addAll(
                    completeArgValue(value, opt, currentDir).map { it.addPrefix("--$flag=") },
                )
            }
        } else {
            completions.addAll(
                longsAndVisibleAliases(cmd).filter { it.getValue().startsWith(arg) },
            )
            completions.addAll(
                hiddenLongsAliases(cmd).filter { it.getValue().startsWith(arg) },
            )
        }
    } else if (arg.startsWith("-")) {
        val shortContent = arg.removePrefix("-")
        val (leadingFlags, takesValueOpt, _) = parseShortflags(cmd, shortContent)
        if (takesValueOpt != null) {
            val eqIdx = shortContent.indexOf('=')
            val value = if (eqIdx >= 0) shortContent.substring(eqIdx + 1) else ""
            val sep = if (eqIdx >= 0) "=" else ""
            completions.addAll(
                completeArgValue(value, takesValueOpt, currentDir).map { it.addPrefix("-$leadingFlags$sep") },
            )
        } else {
            completions.addAll(
                shortsAndVisibleAliases(cmd).map { it.addPrefix("-$leadingFlags") },
            )
        }
    }
    return completions
}

internal fun completeArgValue(
    value: String,
    arg: Arg,
    currentDir: String?,
): List<CompletionCandidate> {
    var values = mutableListOf<CompletionCandidate>()
    val (prefix, actualValue) = rsplitDelimiter(value, null) ?: (null to value)

    val pvList = possibleValues(arg)
    if (pvList != null) {
        for (p in pvList) {
            val name = p.getName()
            if (name.startsWith(actualValue)) {
                values.add(
                    CompletionCandidate
                        .new(name)
                        .help(p.getHelp()?.toString())
                        .hide(p.isHideSet()),
                )
            }
        }
    } else {
        when (arg.getValueHint()) {
            ValueHint.Unknown, ValueHint.Other -> {}
            ValueHint.AnyPath, ValueHint.FilePath, ValueHint.DirPath, ValueHint.ExecutablePath -> {
                values.addAll(completePath(actualValue, currentDir) { true })
            }
            else -> {}
        }
        values.sort()
    }

    if (prefix != null) {
        values = values.map { it.addPrefix(prefix) }.toMutableList()
    }

    values =
        values
            .map { comp ->
                if (comp.getTag() != null) {
                    comp
                } else {
                    comp.tag(arg.getLong() ?: arg.getShort()?.toString() ?: "")
                }
            }.toMutableList()

    return values
}

internal fun rsplitDelimiter(
    value: String,
    delimiter: Char?,
): Pair<String?, String>? {
    if (delimiter == null) return null
    val pos = value.lastIndexOf(delimiter)
    if (pos < 0) return null
    val prefix = value.substring(0, pos + 1)
    val rem = value.substring(pos + 1)
    return prefix to rem
}

internal fun completeCustomArgValue(
    value: String,
    completer: ArgValueCandidates,
): List<CompletionCandidate> {
    val values = completer.candidates().toMutableList()
    values.retainAll { it.getValue().startsWith(value) }
    return values
}

internal fun completeSubcommand(
    value: String,
    cmd: Command,
): List<CompletionCandidate> {
    val scs = subcommands(cmd).filter { it.getValue().startsWith(value) }.toMutableList()
    return scs.distinct().sorted()
}

internal fun completeExternalSubcommand(
    value: String,
    completer: SubcommandCandidates,
): List<CompletionCandidate> {
    val values = completer.candidates().toMutableList()
    values.retainAll { it.getValue().startsWith(value) }
    return values
}

internal fun longsAndVisibleAliases(p: Command): List<CompletionCandidate> {
    val result = mutableListOf<CompletionCandidate>()
    for (a in p.getArguments()) {
        a.getLong()?.let { l ->
            result.add(populateArgCandidate(CompletionCandidate.new("--$l"), a))
        }
    }
    return result
}

internal fun hiddenLongsAliases(p: Command): List<CompletionCandidate> {
    val result = mutableListOf<CompletionCandidate>()
    for (a in p.getArguments()) {
        // hidden aliases
    }
    return result
}

internal fun shortsAndVisibleAliases(p: Command): List<CompletionCandidate> {
    val result = mutableListOf<CompletionCandidate>()
    for (a in p.getArguments()) {
        a.getShort()?.let { s ->
            val help = a.getHelp() ?: a.getLong()?.let { "--$it" }
            result.add(populateArgCandidate(CompletionCandidate.new(s.toString()), a).help(help))
        }
    }
    return result
}

internal fun populateArgCandidate(
    candidate: CompletionCandidate,
    arg: Arg,
): CompletionCandidate =
    candidate
        .help(arg.getHelp())
        .id("arg::${candidate.getValue()}")
        .tag("Options")

internal fun possibleValues(a: Arg): List<PossibleValue>? = Utils.possibleValues(a)

internal fun subcommands(p: Command): List<CompletionCandidate> {
    val result = mutableListOf<CompletionCandidate>()
    for (sc in p.getSubcommands()) {
        result.add(populateCommandCandidate(CompletionCandidate.new(sc.getName()), p, sc))
        for (alias in sc.getVisibleAliases()) {
            result.add(populateCommandCandidate(CompletionCandidate.new(alias), p, sc))
        }
    }
    return result
}

internal fun populateCommandCandidate(
    candidate: CompletionCandidate,
    cmd: Command,
    subcommand: Command,
): CompletionCandidate =
    candidate
        .help(subcommand.getAbout())
        .id("command::${subcommand.getName()}")
        .tag("Commands")

internal fun parseShortflags(
    cmd: Command,
    short: String,
): Triple<String, Arg?, String> {
    var takesValueOpt: Arg? = null
    val leadingFlags = StringBuilder()
    var remaining = short

    for (c in short) {
        leadingFlags.append(c)
        val opt = cmd.getArguments().firstOrNull { it.getShort() == c }
        if (opt != null && opt.getAction().takesValues()) {
            takesValueOpt = opt
            remaining = short.substringAfter(c)
            break
        }
    }

    return Triple(leadingFlags.toString(), takesValueOpt, remaining)
}

internal fun parsePositional(
    cmd: Command,
    posIndex: Int,
    isEscaped: Boolean,
    state: ParseState,
): Pair<ParseState, Int> =
    if (isEscaped) {
        ParseState.Pos(posIndex, 1) to (posIndex + 1)
    } else {
        ParseState.ValueDone to (posIndex + 1)
    }

internal fun parseOptValue(
    opt: Arg,
    count: Int,
): ParseState =
    if (count < 1) {
        ParseState.Opt(opt, count + 1)
    } else {
        ParseState.ValueDone
    }

internal fun posAllowsHyphen(
    cmd: Command,
    posIndex: Int,
): Boolean = false

internal fun optAllowsHyphen(
    state: ParseState,
    arg: String,
): Boolean = false
