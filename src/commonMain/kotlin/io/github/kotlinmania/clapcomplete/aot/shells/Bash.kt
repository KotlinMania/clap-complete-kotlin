// port-lint: source clap_complete/src/aot/shells/bash.rs
package io.github.kotlinmania.clapcomplete.aot.shells

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clap.ValueHint
import io.github.kotlinmania.clapcomplete.aot.generator.Generator
import io.github.kotlinmania.clapcomplete.aot.generator.Utils

/**
 * Generate bash completion file.
 */
object Bash : Generator {
    override fun fileName(name: String): String = "$name.bash"

    override fun generate(cmd: Command, buf: Appendable) {
        tryGenerate(cmd, buf)
    }

    override fun tryGenerate(cmd: Command, buf: Appendable) {
        val binName = cmd.getName()
        val fnName = binName.replace('-', '_')

        val nameOpts = allOptionsForPath(cmd, binName)
        val nameOptsDetails = optionDetailsForPath(cmd, binName)
        val subcmds = allSubcommands(cmd, fnName)
        val subcmdDetails = subcommandDetails(cmd)

        buf.append(
            """
            |_$binName() {
            |    local i cur prev opts cmd
            |    COMPREPLY=()
            |    if [[ "${'$'}{BASH_VERSINFO[0]}" -ge 4 ]]; then
            |        cur="${'$'}2"
            |    else
            |        cur="${'$'}{COMP_WORDS[COMP_CWORD]}"
            |    fi
            |    prev="${'$'}3"
            |    cmd=""
            |    opts=""
            |
            |    for i in "${'$'}{COMP_WORDS[@]:0:COMP_CWORD}"
            |    do
            |        case "${'$'}{cmd},${'$'}{i}" in
            |            ",$1")
            |                cmd="$fnName"
            |                ;;$subcmds
            |            *)
            |                ;;
            |        esac
            |    done
            |
            |    case "${'$'}{cmd}" in
            |        $fnName)
            |            opts="$nameOpts"
            |            if [[ ${'$'}{cur} == -* || ${'$'}{COMP_CWORD} -eq 1 ]] ; then
            |                COMPREPLY=( $(compgen -W "${'$'}{opts}" -- "${'$'}{cur}") )
            |                return 0
            |            fi
            |            case "${'$'}{prev}" in$nameOptsDetails
            |                *)
            |                    COMPREPLY=()
            |                    ;;
            |            esac
            |            COMPREPLY=( $(compgen -W "${'$'}{opts}" -- "${'$'}{cur}") )
            |            return 0
            |            ;;$subcmdDetails
            |    esac
            |}
            |
            |if [[ "${'$'}{BASH_VERSINFO[0]}" -eq 4 && "${'$'}{BASH_VERSINFO[1]}" -ge 4 || "${'$'}{BASH_VERSINFO[0]}" -gt 4 ]]; then
            |    complete -F _$binName -o nosort -o bashdefault -o default $binName
            |else
            |    complete -F _$binName -o bashdefault -o default $binName
            |fi
            |
            """.trimMargin(),
        )
    }

    private fun allSubcommands(cmd: Command, parentFnName: String): String {
        val subcmds = mutableListOf<Triple<String, String, String>>()

        fun addCommand(pFnName: String, command: Command) {
            val fName = "${pFnName}__${command.getName().replace('-', '_')}"
            subcmds.add(Triple(pFnName, command.getName(), fName))
            for (alias in command.getVisibleAliases()) {
                subcmds.add(Triple(pFnName, alias, fName))
            }
            for (subcmd in command.getSubcommands()) {
                addCommand(fName, subcmd)
            }
        }

        for (subcmd in cmd.getSubcommands()) {
            addCommand(parentFnName, subcmd)
        }
        subcmds.sortBy { "${it.first},${it.second}" }

        val cases = mutableListOf<String>()
        for ((pFnName, name, fName) in subcmds) {
            cases.add(
                """
                |            $pFnName,$name)
                |                cmd="$fName"
                |                ;;
                """.trimMargin(),
            )
        }

        return if (cases.isEmpty()) "" else "\n" + cases.joinToString("\n")
    }

    private fun subcommandDetails(cmd: Command): String {
        val scs =
            Utils
                .allSubcommands(cmd)
                .map { it.second.replace(' ', '_') }
                .distinct()
                .sorted()

        val subcmdDets = mutableListOf<String>()
        for (sc in scs) {
            val scSubcmd = sc.replace('-', '_')
            val scOpts = allOptionsForPath(cmd, sc)
            val level = sc.split("__").size
            val optsDetails = optionDetailsForPath(cmd, sc)

            subcmdDets.add(
                """
                |        $scSubcmd)
                |            opts="$scOpts"
                |            if [[ ${'$'}{cur} == -* || ${'$'}{COMP_CWORD} -eq $level ]] ; then
                |                COMPREPLY=( $(compgen -W "${'$'}{opts}" -- "${'$'}{cur}") )
                |                return 0
                |            fi
                |            case "${'$'}{prev}" in$optsDetails
                |                *)
                |                    COMPREPLY=()
                |                    ;;
                |            esac
                |            COMPREPLY=( $(compgen -W "${'$'}{opts}" -- "${'$'}{cur}") )
                |            return 0
                |            ;;
                """.trimMargin(),
            )
        }

        return if (subcmdDets.isEmpty()) "" else "\n" + subcmdDets.joinToString("\n")
    }

    private fun optionDetailsForPath(cmd: Command, path: String): String {
        val segments = path.split("__").drop(1).filter { it.isNotEmpty() }
        val p = if (segments.isEmpty()) cmd else Utils.findSubcommandWithPath(cmd, segments)
        val opts = mutableListOf<String>()

        for (o in Utils.opts(p)) {
            val compopt =
                when (o.getValueHint()) {
                    ValueHint.FilePath -> "compopt -o filenames"
                    ValueHint.DirPath -> "compopt -o plusdirs"
                    ValueHint.Other -> "compopt -o nospace"
                    else -> null
                }

            o.getLong()?.let { long ->
                val v = mutableListOf<String>()
                v.add("--$long)")
                if (o.getValueHint() == ValueHint.FilePath) {
                    v.add("local oldifs")
                    v.add("if [ -n \"\${IFS+x}\" ]; then")
                    v.add("    oldifs=\"\$IFS\"")
                    v.add("fi")
                    v.add("IFS=$'\\n'")
                    v.add("COMPREPLY=(${valsFor(o)})")
                    v.add("if [ -n \"\${oldifs+x}\" ]; then")
                    v.add("    IFS=\"\$oldifs\"")
                    v.add("fi")
                } else {
                    v.add("COMPREPLY=(${valsFor(o)})")
                }
                if (compopt != null) {
                    v.add("if [[ \"\${BASH_VERSINFO[0]}\" -ge 4 ]]; then")
                    v.add("    $compopt")
                    v.add("fi")
                }
                v.add("return 0")
                v.add(";;")
                opts.add(v.joinToString("\n                    "))
            }

            o.getShort()?.let { short ->
                val v = mutableListOf<String>()
                v.add("-$short)")
                if (o.getValueHint() == ValueHint.FilePath) {
                    v.add("local oldifs")
                    v.add("if [ -n \"\${IFS+x}\" ]; then")
                    v.add("    oldifs=\"\$IFS\"")
                    v.add("fi")
                    v.add("IFS=$'\\n'")
                    v.add("COMPREPLY=(${valsFor(o)})")
                    v.add("if [ -n \"\${oldifs+x}\" ]; then")
                    v.add("    IFS=\"\$oldifs\"")
                    v.add("fi")
                } else {
                    v.add("COMPREPLY=(${valsFor(o)})")
                }
                if (compopt != null) {
                    v.add("if [[ \"\${BASH_VERSINFO[0]}\" -ge 4 ]]; then")
                    v.add("    $compopt")
                    v.add("fi")
                }
                v.add("return 0")
                v.add(";;")
                opts.add(v.joinToString("\n                    "))
            }
        }

        return if (opts.isEmpty()) "" else "\n                " + opts.joinToString("\n                ")
    }

    private fun valsFor(o: Arg): String {
        val vals = Utils.possibleValues(o)
        return when {
            vals != null -> {
                val names = vals.filter { !it.isHideSet() }.joinToString(" ") { it.getName() }
                "$(compgen -W \"$names\" -- \"\${cur}\")"
            }
            o.getValueHint() == ValueHint.DirPath -> ""
            o.getValueHint() == ValueHint.Other -> "\"\${cur}\""
            else -> "$(compgen -f \"\${cur}\")"
        }
    }

    private fun allOptionsForPath(cmd: Command, path: String): String {
        val segments = path.split("__").drop(1).filter { it.isNotEmpty() }
        val p = if (segments.isEmpty()) cmd else Utils.findSubcommandWithPath(cmd, segments)

        val opts = StringBuilder()
        for (short in Utils.shortsAndVisibleAliases(p)) {
            opts.append("-$short ")
        }
        for (long in Utils.longsAndVisibleAliases(p)) {
            opts.append("--$long ")
        }
        for (pos in Utils.positionals(p)) {
            val vals = Utils.possibleValues(pos)
            if (vals != null) {
                for (value in vals) {
                    opts.append("${value.getName()} ")
                }
            } else {
                opts.append("${pos.getId()} ")
            }
        }
        for ((sc, _) in Utils.subcommands(p)) {
            opts.append("$sc ")
        }
        return opts.toString().trimEnd()
    }
}
