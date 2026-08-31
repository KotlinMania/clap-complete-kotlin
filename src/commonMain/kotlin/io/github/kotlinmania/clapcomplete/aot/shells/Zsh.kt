// port-lint: source clap_complete/src/aot/shells/zsh.rs
package io.github.kotlinmania.clapcomplete.aot.shells

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.ArgAction
import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clap.ValueHint
import io.github.kotlinmania.clapcomplete.aot.generator.Generator
import io.github.kotlinmania.clapcomplete.aot.generator.Utils

/**
 * Generate zsh completion file.
 */
object Zsh : Generator {
    override fun fileName(name: String): String = "_$name"

    override fun generate(cmd: Command, buf: Appendable) {
        tryGenerate(cmd, buf)
    }

    override fun tryGenerate(cmd: Command, buf: Appendable) {
        val binName = cmd.getName()

        val initialArgs = getArgsOf(cmd, null)
        val subcommands = getSubcommandsOf(cmd)
        val subcommandDets = subcommandDetails(cmd)

        buf.append(
            """
            |#compdef $binName
            |
            |autoload -U is-at-least
            |
            |_$binName() {
            |    typeset -A opt_args
            |    typeset -a _arguments_options
            |    local ret=1
            |
            |    if is-at-least 5.2; then
            |        _arguments_options=(-s -S -C)
            |    else
            |        _arguments_options=(-s -C)
            |    fi
            |
            |    local context curcontext="${'$'}curcontext" state line
            |    $initialArgs$subcommands
            |}
            |
            |$subcommandDets
            |
            |if [ "${'$'}funcstack[1]" = "_$binName" ]; then
            |    _$binName "${'$'}@"
            |else
            |    compdef _$binName $binName
            |fi
            |
            """.trimMargin(),
        )
    }

    fun escapeHelp(string: String): String =
        string
            .replace("\\", "\\\\")
            .replace("'", "'\\''")
            .replace("[", "\\[")
            .replace("]", "\\]")
            .replace(":", "\\:")
            .replace("$", "\\$")
            .replace("`", "\\`")
            .replace('\n', ' ')

    fun escapeValue(string: String): String =
        string
            .replace("\\", "\\\\")
            .replace("'", "'\\''")
            .replace("[", "\\[")
            .replace("]", "\\]")
            .replace(":", "\\:")
            .replace("$", "\\$")
            .replace("`", "\\`")
            .replace("(", "\\(")
            .replace(")", "\\)")
            .replace(" ", "\\ ")

    private fun subcommandDetails(p: Command): String {
        val binName = p.getName()
        val ret = mutableListOf<String>()

        val binNameUnderscore = binName.replace(' ', '_')
        val parentText =
            """
            |(( ${'$'}+functions[_${binNameUnderscore}_commands] )) ||
            |_${binNameUnderscore}_commands() {
            |    local commands; commands=(${subcommandsOf(p)})
            |    _describe -t commands '$binName commands' commands "${'$'}@"
            |}
            """.trimMargin()
        ret.add(parentText)

        val allSubcommandBins =
            Utils
                .allSubcommands(p)
                .map { it.second }
                .distinct()
                .sorted()

        for (scBinName in allSubcommandBins) {
            val scBinNameUnderscore = scBinName.replace(' ', '_')
            val parser = parserOf(p, scBinName) ?: continue
            ret.add(
                """
                |(( ${'$'}+functions[_${scBinNameUnderscore}_commands] )) ||
                |_${scBinNameUnderscore}_commands() {
                |    local commands; commands=(${subcommandsOf(parser)})
                |    _describe -t commands '$scBinName commands' commands "${'$'}@"
                |}
                """.trimMargin(),
            )
        }

        return ret.joinToString("\n")
    }

    private fun subcommandsOf(p: Command): String {
        val segments = mutableListOf<String>()

        fun addSubcommands(subcommand: Command, name: String) {
            val help = escapeHelp(subcommand.getAbout() ?: "")
            segments.add("'$name:$help' \\")
        }

        for (command in p.getSubcommands()) {
            addSubcommands(command, command.getName())
            for (alias in command.getVisibleAliases()) {
                addSubcommands(command, alias)
            }
        }

        return if (segments.isEmpty()) {
            ""
        } else {
            "\n" + segments.joinToString("\n") + "\n    "
        }
    }

    private fun getSubcommandsOf(parent: Command): String {
        if (parent.getSubcommands().isEmpty()) {
            return ""
        }

        val subcommandNames = Utils.subcommands(parent)
        val allSubcommands = mutableListOf<String>()

        for ((name, binName) in subcommandNames) {
            val segments = mutableListOf("($name)")
            val parser = parserOf(parent, binName) ?: parent
            val subcommandArgs = getArgsOf(parser, parent)

            if (subcommandArgs.isNotEmpty()) {
                segments.add(subcommandArgs)
            }

            val children = getSubcommandsOf(parser)
            if (children.isNotEmpty()) {
                segments.add(children)
            }

            segments.add(";;")
            allSubcommands.add(segments.joinToString("\n"))
        }

        val parentBinName = parent.getName()
        val nameHyphen = parentBinName.replace(' ', '-')
        val pos = Utils.positionals(parent).size + 1

        return """
            |
            |    case ${'$'}state in
            |    (${parent.getName()})
            |        words=(${'$'}line[$pos] "${'$'}{words[@]}")
            |        (( CURRENT += 1 ))
            |        curcontext="${'$'}{curcontext%:*:*}:$nameHyphen-command-${'$'}line[$pos]:"
            |        case ${'$'}line[$pos] in
            |            ${allSubcommands.joinToString("\n")}
            |        esac
            |    ;;
            |esac
            """.trimMargin()
    }

    private fun parserOf(parent: Command, binName: String): Command? {
        if (binName == parent.getName()) {
            return parent
        }

        for (subcommand in parent.getSubcommands()) {
            val scBinName = "${parent.getName()} ${subcommand.getName()}"
            if (binName == scBinName || binName == subcommand.getName()) {
                return subcommand
            }
            val ret = parserOf(subcommand, binName)
            if (ret != null) {
                return ret
            }
        }

        return null
    }

    private fun getArgsOf(parent: Command, pGlobal: Command?): String {
        val segments = mutableListOf("_arguments \"\${_arguments_options[@]}\" : \\")
        val opts = writeOptsOf(parent, pGlobal)
        val flags = writeFlagsOf(parent, pGlobal)
        val positionals = writePositionalsOf(parent)

        if (opts.isNotEmpty()) {
            segments.add(opts)
        }
        if (flags.isNotEmpty()) {
            segments.add(flags)
        }
        if (positionals.isNotEmpty()) {
            segments.add(positionals)
        }

        if (parent.getSubcommands().isNotEmpty()) {
            val parentBinName = parent.getName()
            val scBinName = parentBinName.replace(' ', '_')
            segments.add("\":: :_${scBinName}_commands\" \\")
            segments.add("\"*::: :->${parent.getName()}\" \\")
        } else if (parent.isAllowExternalSubcommandsSet()) {
            segments.add("\"*::external_command:_default\" \\")
        }

        segments.add("&& ret=0")
        return segments.joinToString("\n")
    }

    private fun valueCompletion(arg: Arg): String? {
        val values = Utils.possibleValues(arg)
        return if (values != null) {
            if (values.any { !it.isHideSet() && it.getHelp() != null }) {
                val formatted =
                    values
                        .filter { !it.isHideSet() }
                        .joinToString("\n") { value ->
                            val name = escapeValue(value.getName())
                            val tooltip = escapeHelp(value.getHelp()?.toString() ?: "")
                            "$name\\:\"$tooltip\""
                        }
                "(($formatted))"
            } else {
                val names = values.filter { !it.isHideSet() }.joinToString(" ") { it.getName() }
                "($names)"
            }
        } else {
            when (arg.getValueHint()) {
                ValueHint.Unknown -> "_default"
                ValueHint.Other -> ""
                ValueHint.AnyPath, ValueHint.FilePath -> "_files"
                ValueHint.DirPath -> "_files -/"
                ValueHint.ExecutablePath -> "_absolute_command_paths"
                ValueHint.CommandName -> "_command_names -e"
                ValueHint.CommandString -> "_cmdstring"
                ValueHint.CommandWithArguments -> "_cmdambivalent"
                ValueHint.Username -> "_users"
                ValueHint.Hostname -> "_hosts"
                ValueHint.Url -> "_urls"
                ValueHint.EmailAddress -> "_email_addresses"
            }
        }
    }

    private fun writeOptsOf(p: Command, pGlobal: Command?): String {
        val ret = mutableListOf<String>()

        for (o in Utils.opts(p)) {
            val help = escapeHelp(o.getHelp() ?: "")
            val conflicts = argConflicts(p, o, pGlobal)
            val multiple = if (o.getAction() == ArgAction.Count || o.getAction() == ArgAction.Append) "*" else ""
            val vn = o.getValueName() ?: " "
            val vc = valueCompletion(o)?.let { ":$vn:$it" } ?: ":$vn: "

            o.getShort()?.let { short ->
                ret.add("'$conflicts$multiple-$short+[$help]$vc' \\")
            }
            o.getLong()?.let { long ->
                ret.add("'$conflicts$multiple--$long=[$help]$vc' \\")
            }
        }

        return ret.joinToString("\n")
    }

    private fun pushConflicts(conflicts: List<Arg>, res: MutableList<String>) {
        for (conflict in conflicts) {
            conflict.getShort()?.let { s ->
                res.add("-$s")
            }
            conflict.getLong()?.let { l ->
                res.add("--$l")
            }
        }
    }

    private fun argConflicts(cmd: Command, arg: Arg, appGlobal: Command?): String {
        val res = mutableListOf<String>()
        return if (res.isEmpty()) {
            ""
        } else {
            "(${res.joinToString(" ")})"
        }
    }

    private fun writeFlagsOf(p: Command, pGlobal: Command?): String {
        val ret = mutableListOf<String>()

        for (f in Utils.flags(p)) {
            val help = escapeHelp(f.getHelp() ?: "")
            val conflicts = argConflicts(p, f, pGlobal)
            val multiple = if (f.getAction() == ArgAction.Count || f.getAction() == ArgAction.Append) "*" else ""

            f.getShort()?.let { short ->
                ret.add("'$conflicts$multiple-$short[$help]' \\")
            }
            f.getLong()?.let { long ->
                ret.add("'$conflicts$multiple--$long[$help]' \\")
            }
        }

        return ret.joinToString("\n")
    }

    private fun writePositionalsOf(p: Command): String {
        val ret = mutableListOf<String>()
        var catchAllEmitted = false

        for (arg in Utils.positionals(p)) {
            val isMultiValued = arg.getAction() == ArgAction.Append
            if (catchAllEmitted && isMultiValued) {
                continue
            }

            val cardinality =
                if (isMultiValued && p.getSubcommands().isEmpty()) {
                    catchAllEmitted = true
                    "*:"
                } else {
                    ":"
                }

            val help = arg.getHelp()?.let { " -- $it" } ?: ""
            val escapedHelp =
                help
                    .replace("[", "\\[")
                    .replace("]", "\\]")
                    .replace("'", "'\\''")
                    .replace(":", "\\:")
            val vc = valueCompletion(arg) ?: ""

            ret.add("'$cardinality:${arg.getId()}$escapedHelp:$vc' \\")
        }

        return ret.joinToString("\n")
    }
}
