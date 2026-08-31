// port-lint: source clap_complete/src/aot/shells/elvish.rs
package io.github.kotlinmania.clapcomplete.aot.shells

import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clapcomplete.aot.generator.Generator
import io.github.kotlinmania.clapcomplete.aot.generator.Utils

/**
 * Generate elvish completion file.
 */
object Elvish : Generator {
    override fun fileName(name: String): String = "$name.elv"

    override fun generate(cmd: Command, buf: Appendable) {
        tryGenerate(cmd, buf)
    }

    override fun tryGenerate(cmd: Command, buf: Appendable) {
        val binName = cmd.getName()
        val subcommandsCases = generateInner(cmd, "")

        buf.append(
            """
            |
            |use builtin;
            |use str;
            |
            |set edit:completion:arg-completer[$binName] = {|@words|
            |    fn spaces {|n|
            |        builtin:repeat ${'$'}n ' ' | str:join ''
            |    }
            |    fn cand {|text desc|
            |        edit:complex-candidate ${'$'}text &display=${'$'}text' '(spaces (- 14 (wcswidth ${'$'}text)))${'$'}desc
            |    }
            |    var command = '$binName'
            |    for word ${'$'}words[1..-1] {
            |        if (str:has-prefix ${'$'}word '-') {
            |            break
            |        }
            |        set command = ${'$'}command';'${'$'}word
            |    }
            |    var completions = [$subcommandsCases
            |    ]
            |    ${'$'}completions[${'$'}command]
            |}
            |
            """.trimMargin(),
        )
    }

    private fun escapeString(string: String): String = string.replace("'", "''")

    private fun escapeHelp(help: String?, data: String): String =
        if (help != null) escapeString(help.replace('\n', ' ')) else data

    private fun generateInner(p: Command, previousCommandName: String): String {
        val commandNames =
            if (previousCommandName.isEmpty()) {
                listOf(p.getName())
            } else {
                (listOf(p.getName()) + p.getVisibleAliases()).map { name -> "$previousCommandName;$name" }
            }

        val completions = StringBuilder()
        val preamble = "\n            cand "

        for (option in Utils.opts(p)) {
            option.getShort()?.let { short ->
                val tooltip = escapeHelp(option.getHelp(), short.toString())
                completions.append(preamble)
                completions.append("-$short '$tooltip'")
            }
            option.getLong()?.let { long ->
                val tooltip = escapeHelp(option.getHelp(), long)
                completions.append(preamble)
                completions.append("--$long '$tooltip'")
            }
        }

        for (flag in Utils.flags(p)) {
            flag.getShort()?.let { short ->
                val tooltip = escapeHelp(flag.getHelp(), short.toString())
                completions.append(preamble)
                completions.append("-$short '$tooltip'")
            }
            flag.getLong()?.let { long ->
                val tooltip = escapeHelp(flag.getHelp(), long)
                completions.append(preamble)
                completions.append("--$long '$tooltip'")
            }
        }

        for (subcommand in p.getSubcommands()) {
            val names = listOf(subcommand.getName()) + subcommand.getVisibleAliases()
            for (name in names) {
                val tooltip = escapeHelp(subcommand.getAbout(), name)
                completions.append(preamble)
                completions.append("$name '$tooltip'")
            }
        }

        val subcommandsCases = StringBuilder()
        for (commandName in commandNames) {
            subcommandsCases.append(
                """
                |
                |        &'$commandName'= {$completions
                |        }
                """.trimMargin(),
            )
        }

        for (subcommand in p.getSubcommands()) {
            for (commandName in commandNames) {
                val subcommandSubcommandsCases = generateInner(subcommand, commandName)
                subcommandsCases.append(subcommandSubcommandsCases)
            }
        }

        return subcommandsCases.toString()
    }
}
