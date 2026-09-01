// port-lint: source aot/shells/powershell.rs
package io.github.kotlinmania.clapcomplete.aot.shells

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clapcomplete.aot.generator.Generator
import io.github.kotlinmania.clapcomplete.aot.generator.Utils

/**
 * Generate powershell completion file.
 */
object PowerShell : Generator {
    override fun fileName(name: String): String = "_$name.ps1"

    override fun generate(cmd: Command, buf: Appendable) {
        tryGenerate(cmd, buf)
    }

    override fun tryGenerate(cmd: Command, buf: Appendable) {
        val binName = cmd.getName()
        val subcommandsCases = generateInner(cmd, "")

        buf.append(
            """
            |
            |using namespace System.Management.Automation
            |using namespace System.Management.Automation.Language
            |
            |Register-ArgumentCompleter -Native -CommandName '$binName' -ScriptBlock {
            |    param(${'$'}wordToComplete, ${'$'}commandAst, ${'$'}cursorPosition)
            |
            |    ${'$'}commandElements = ${'$'}commandAst.CommandElements
            |    ${'$'}command = @(
            |        '$binName'
            |        for (${'$'}i = 1; ${'$'}i -lt ${'$'}commandElements.Count; ${'$'}i++) {
            |            ${'$'}element = ${'$'}commandElements[${'$'}i]
            |            if (${'$'}element -isnot [StringConstantExpressionAst] -or
            |                ${'$'}element.StringConstantType -ne [StringConstantType]::BareWord -or
            |                ${'$'}element.Value.StartsWith('-') -or
            |                ${'$'}element.Value -eq ${'$'}wordToComplete) {
            |                break
            |        }
            |        ${'$'}element.Value
            |    }) -join ';'
            |
            |    ${'$'}completions = @(switch (${'$'}command) {$subcommandsCases
            |    })
            |
            |    ${'$'}completions.Where{ ${'$'}_.CompletionText -like "${'$'}wordToComplete*" } |
            |        Sort-Object -Property ListItemText
            |}
            |
            """.trimMargin(),
        )
    }

    private fun escapeString(string: String): String =
        string.replace("'", "''").replace("’", "'’")

    private fun escapeHelp(help: String?, data: String): String {
        if (help != null && help.isNotEmpty()) {
            return escapeString(help.replace('\n', ' '))
        }
        return data
    }

    private fun generateInner(p: Command, previousCommandName: String): String {
        val commandNames =
            if (previousCommandName.isEmpty()) {
                listOf(p.getName())
            } else {
                (listOf(p.getName()) + p.getVisibleAliases()).map { name -> "$previousCommandName;$name" }
            }

        val completions = StringBuilder()
        val preamble = "\n            [CompletionResult]::new("

        for (option in Utils.opts(p)) {
            generateAliases(completions, preamble, option)
        }

        for (flag in Utils.flags(p)) {
            generateAliases(completions, preamble, flag)
        }

        for (subcommand in p.getSubcommands()) {
            val names = listOf(subcommand.getName()) + subcommand.getVisibleAliases()
            for (name in names) {
                val tooltip = escapeHelp(subcommand.getAbout(), name)
                completions.append(preamble)
                completions.append("'$name', '$name', [CompletionResultType]::ParameterValue, '$tooltip')")
            }
        }

        val subcommandsCases = StringBuilder()
        for (commandName in commandNames) {
            subcommandsCases.append(
                """
                |
                |        '$commandName' {$completions
                |            break
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

    private fun generateAliases(completions: StringBuilder, preamble: String, arg: Arg) {
        arg.getShort()?.let { short ->
            val tooltip = escapeHelp(arg.getHelp(), short.toString())
            val space = if (short.isUpperCase()) " " else ""
            completions.append("$preamble'-$short', '-$short$space', [CompletionResultType]::ParameterName, '$tooltip')")
        }
        arg.getLong()?.let { long ->
            val tooltip = escapeHelp(arg.getHelp(), long)
            completions.append("$preamble'--$long', '--$long', [CompletionResultType]::ParameterName, '$tooltip')")
        }
    }
}
