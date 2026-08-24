// port-lint: source aot/shells/fish.rs
package io.github.kotlinmania.clapcomplete.aot.shells

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clap.ValueHint
import io.github.kotlinmania.clapcomplete.aot.generator.Generator
import io.github.kotlinmania.clapcomplete.aot.generator.Utils

/**
 * Generate fish completion file.
 */
object Fish : Generator {
    override fun fileName(name: String): String = "$name.fish"

    override fun generate(cmd: Command, buf: Appendable) {
        tryGenerate(cmd, buf)
    }

    override fun tryGenerate(cmd: Command, buf: Appendable) {
        val binName = cmd.getName()
        val name = escapeName(binName)
        var needsFnName = "__fish_${name}_needs_command"
        var usingFnName = "__fish_${name}_using_subcommand"

        if (cmd.getSubcommands().isNotEmpty()) {
            genSubcommandHelpers(name, cmd, buf, needsFnName, usingFnName)
        } else {
            needsFnName = "__fish_use_subcommand"
            usingFnName = "__fish_seen_subcommand_from"
        }

        val buffer = StringBuilder()
        genFishInner(
            binName,
            emptyList(),
            cmd,
            buffer,
            needsFnName,
            usingFnName,
        )
        buf.append(buffer.toString())
    }

    private fun escapeString(string: String, escapeComma: Boolean): String {
        val str = string.replace("\\", "\\\\").replace("'", "\\'")
        return if (escapeComma) str.replace(",", "\\,") else str
    }

    private fun escapeHelp(help: String?): String =
        if (help != null) escapeString(help.replace('\n', ' '), false) else ""

    private fun escapeName(name: String): String = name.replace('-', '_')

    private fun genFishInner(
        rootCommand: String,
        parentCommands: List<String>,
        cmd: Command,
        buffer: StringBuilder,
        needsFnName: String,
        usingFnName: String,
    ) {
        val basicTemplate = StringBuilder("complete -c $rootCommand")

        if (parentCommands.isEmpty()) {
            if (cmd.getSubcommands().isNotEmpty()) {
                basicTemplate.append(" -n \"$needsFnName\"")
            }
        } else {
            val out = StringBuilder(usingFnName)
            when (parentCommands.size) {
                1 -> {
                    out.append(" ${parentCommands[0]}")
                    if (cmd.getSubcommands().isNotEmpty()) {
                        out.append("; and not __fish_seen_subcommand_from")
                    }
                    val subcommands = cmd.getSubcommands().flatMap { listOf(it.getName()) + it.getVisibleAliases() }
                    for (name in subcommands) {
                        out.append(" $name")
                    }
                }
                2 -> {
                    out.append(" ${parentCommands[0]}; and __fish_seen_subcommand_from ${parentCommands[1]}")
                }
                else -> return
            }
            basicTemplate.append(" -n \"$out\"")
        }

        for (option in Utils.opts(cmd)) {
            val template = StringBuilder(basicTemplate.toString())

            option.getShort()?.let { short ->
                template.append(" -s $short")
            }
            option.getLong()?.let { long ->
                template.append(" -l ${escapeString(long, false)}")
            }
            option.getHelp()?.let { data ->
                template.append(" -d '${escapeHelp(data)}'")
            }
            template.append(valueCompletion(option))

            buffer.append(template.toString())
            buffer.append('\n')
        }

        for (flag in Utils.flags(cmd)) {
            val template = StringBuilder(basicTemplate.toString())

            flag.getShort()?.let { short ->
                template.append(" -s $short")
            }
            flag.getLong()?.let { long ->
                template.append(" -l ${escapeString(long, false)}")
            }
            flag.getHelp()?.let { data ->
                template.append(" -d '${escapeHelp(data)}'")
            }

            buffer.append(template.toString())
            buffer.append('\n')
        }

        val hasPositionals = Utils.positionals(cmd).isNotEmpty()
        if (!hasPositionals) {
            basicTemplate.append(" -f")
        }
        for (subcommand in cmd.getSubcommands()) {
            val names = listOf(subcommand.getName()) + subcommand.getVisibleAliases()
            for (subcommandName in names) {
                val template = StringBuilder(basicTemplate.toString())
                template.append(" -a \"$subcommandName\"")
                subcommand.getAbout()?.let { data ->
                    template.append(" -d '${escapeHelp(data)}'")
                }
                buffer.append(template.toString())
                buffer.append('\n')
            }
        }

        for (subcommand in cmd.getSubcommands()) {
            val names = listOf(subcommand.getName()) + subcommand.getVisibleAliases()
            for (subcommandName in names) {
                val nextParents = parentCommands + subcommandName
                genFishInner(
                    rootCommand,
                    nextParents,
                    subcommand,
                    buffer,
                    needsFnName,
                    usingFnName,
                )
            }
        }
    }

    private fun genSubcommandHelpers(
        binName: String,
        cmd: Command,
        buf: Appendable,
        needsFnName: String,
        usingFnName: String,
    ) {
        val optspecs = StringBuilder()
        val cmdOpts = cmd.getArguments().filter { it.getShort() != null || it.getLong() != null }
        for (option in cmdOpts) {
            optspecs.append(' ')
            var hasShort = false
            option.getShort()?.let { short ->
                hasShort = true
                optspecs.append(short)
            }
            option.getLong()?.let { long ->
                if (hasShort) optspecs.append('/')
                optspecs.append(escapeString(long, false))
            }
            if (option.getAction().takesValues()) {
                optspecs.append('=')
            }
        }

        val optspecsFnName = "__fish_${binName}_global_optspecs"
        buf.append(
            """
            |# Print an optspec for argparse to handle cmd's options that are independent of any subcommand.
            |function $optspecsFnName
            |	string join \n$optspecs
            |end
            |
            |function $needsFnName
            |	# Figure out if the current invocation already has a command.
            |	set -l cmd (commandline -opc)
            |	set -e cmd[1]
            |	argparse -s ($optspecsFnName) -- ${'$'}cmd 2>/dev/null
            |	or return
            |	if set -q argv[1]
            |		# Also print the command, so this can be used to figure out what it is.
            |		echo ${'$'}argv[1]
            |		return 1
            |	end
            |	return 0
            |end
            |
            |function $usingFnName
            |	set -l cmd ($needsFnName)
            |	test -z "${'$'}cmd"
            |	and return 1
            |	contains -- ${'$'}cmd[1] ${'$'}argv
            |end
            |
            |
            """.trimMargin(),
        )
    }

    private fun valueCompletion(option: Arg): String {
        if (!option.getAction().takesValues()) {
            return ""
        }

        val data = Utils.possibleValues(option)
        return if (data != null) {
            val formatted =
                data
                    .filter { !it.isHideSet() }
                    .joinToString("\n") { value ->
                        val name = escapeString(value.getName(), true)
                        val help = escapeHelp(value.getHelp()?.toString())
                        "$name\\t'$help'"
                    }
            " -r -f -a \"$formatted\""
        } else {
            when (option.getValueHint()) {
                ValueHint.Unknown -> " -r"
                ValueHint.AnyPath, ValueHint.FilePath, ValueHint.ExecutablePath -> " -r -F"
                ValueHint.DirPath -> " -r -f -a \"(__fish_complete_directories)\""
                ValueHint.CommandString, ValueHint.CommandName -> " -r -f -a \"(__fish_complete_command)\""
                ValueHint.Username -> " -r -f -a \"(__fish_complete_users)\""
                ValueHint.Hostname -> " -r -f -a \"(__fish_print_hostnames)\""
                else -> " -r -f"
            }
        }
    }
}
