// port-lint: source engine/complete.rs
package io.github.kotlinmania.clapcomplete.engine

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clap.ValueHint
import io.github.kotlinmania.clapcomplete.aot.generator.Utils

private sealed interface ParseState {
    data object ValueDone : ParseState

    data class Opt(
        val arg: Arg,
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
    var state: ParseState = ParseState.ValueDone
    var posIndex = 0

    val startIndex = if (args.isNotEmpty() && args[0] == cmd.getName()) 1 else 0
    var i = startIndex

    while (i < argIndex && i < args.size) {
        val arg = args[i]
        when (val s = state) {
            is ParseState.Opt -> {
                state = ParseState.ValueDone
            }
            else -> {
                val sc =
                    currentCmd.getSubcommands().firstOrNull {
                        it.getName() == arg || it.getVisibleAliases().contains(arg)
                    }
                if (sc != null) {
                    currentCmd = sc
                    posIndex = 0
                    state = ParseState.ValueDone
                } else if (arg.startsWith("--")) {
                    val flag = arg.removePrefix("--")
                    val opt = currentCmd.getArguments().firstOrNull { it.getLong() == flag }
                    if (opt != null && opt.getAction().takesValues()) {
                        state = ParseState.Opt(opt)
                    }
                } else if (arg.startsWith("-") && arg.length > 1) {
                    val flag = arg.removePrefix("-")
                    val opt = currentCmd.getArguments().firstOrNull { it.getShort()?.toString() == flag }
                    if (opt != null && opt.getAction().takesValues()) {
                        state = ParseState.Opt(opt)
                    }
                } else {
                    posIndex++
                    state = ParseState.Pos(posIndex)
                }
            }
        }
        i++
    }

    val currentArg = if (argIndex < args.size) args[argIndex] else ""
    return when (val s = state) {
        is ParseState.Opt -> completeArgValue(currentArg, s.arg, currentDir)
        else -> completeArg(currentArg, currentCmd, currentDir, posIndex)
    }
}

private fun completeArg(
    arg: String,
    cmd: Command,
    currentDir: String?,
    posIndex: Int,
): List<CompletionCandidate> {
    val completions = mutableListOf<CompletionCandidate>()

    if (arg.isEmpty() || !arg.startsWith("-")) {
        completions.addAll(completeSubcommands(arg, cmd))
        val positionals = Utils.positionals(cmd)
        if (posIndex < positionals.size) {
            completions.addAll(completeArgValue(arg, positionals[posIndex], currentDir))
        }
    }

    if (arg.isEmpty() || arg.startsWith("-")) {
        completions.addAll(completeOptions(arg, cmd, currentDir))
    }

    val visible = completions.filter { !it.isHideSet() }
    val finalCompletions = if (visible.isNotEmpty()) visible else completions

    val seen = mutableSetOf<String>()
    val deduped = mutableListOf<CompletionCandidate>()
    for (c in finalCompletions) {
        val key = c.getValue()
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

private fun completeSubcommands(value: String, cmd: Command): List<CompletionCandidate> {
    val candidates = mutableListOf<CompletionCandidate>()
    for (sc in cmd.getSubcommands()) {
        val names = listOf(sc.getName()) + sc.getVisibleAliases()
        for (name in names) {
            if (name.startsWith(value)) {
                candidates.add(
                    CompletionCandidate
                        .new(name)
                        .help(sc.getAbout())
                        .tag(cmd.getName())
                        .id("command::$name"),
                )
            }
        }
    }
    return candidates
}

private fun completeOptions(
    arg: String,
    cmd: Command,
    currentDir: String?,
): List<CompletionCandidate> {
    val completions = mutableListOf<CompletionCandidate>()

    for (opt in cmd.getArguments()) {
        opt.getLong()?.let { long ->
            val full = "--$long"
            if (full.startsWith(arg)) {
                completions.add(
                    CompletionCandidate
                        .new(full)
                        .help(opt.getHelp())
                        .tag(cmd.getName())
                        .id("arg::$long"),
                )
            }
            if (arg.startsWith("$full=") && opt.getAction().takesValues()) {
                val valuePart = arg.removePrefix("$full=")
                val values = completeArgValue(valuePart, opt, currentDir)
                completions.addAll(values.map { it.addPrefix("$full=") })
            }
        }

        opt.getShort()?.let { short ->
            val full = "-$short"
            if (full.startsWith(arg)) {
                completions.add(
                    CompletionCandidate
                        .new(full)
                        .help(opt.getHelp())
                        .tag(cmd.getName())
                        .id("arg::$short"),
                )
            }
        }
    }

    return completions
}

private fun completeArgValue(
    value: String,
    arg: Arg,
    currentDir: String?,
): List<CompletionCandidate> {
    val values = mutableListOf<CompletionCandidate>()
    val pvList = Utils.possibleValues(arg)
    if (pvList != null) {
        for (pv in pvList) {
            if (pv.getName().startsWith(value)) {
                values.add(
                    CompletionCandidate
                        .new(pv.getName())
                        .help(pv.getHelp()?.toString())
                        .hide(pv.isHideSet()),
                )
            }
        }
    } else {
        when (arg.getValueHint()) {
            ValueHint.Unknown, ValueHint.Other -> {}
            ValueHint.AnyPath, ValueHint.FilePath, ValueHint.DirPath -> {
                // Path hints
            }
            else -> {}
        }
    }
    return values
}
