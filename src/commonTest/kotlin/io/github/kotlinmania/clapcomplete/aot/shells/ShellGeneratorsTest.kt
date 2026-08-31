// port-lint: tests aot/shells/bash.rs
package io.github.kotlinmania.clapcomplete.aot.shells

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.ArgAction
import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clap.ValueHint
import io.github.kotlinmania.clapcomplete.ClapComplete
import kotlin.test.Test
import kotlin.test.assertTrue

class ShellGeneratorsTest {
    private fun testCommand(): Command =
        Command
            .new("mycli")
            .about("Test CLI application")
            .arg(
                Arg
                    .new("config")
                    .short('c')
                    .long("config")
                    .help("Path to config file")
                    .valueHint(ValueHint.FilePath),
            ).arg(
                Arg
                    .new("verbose")
                    .short('v')
                    .long("verbose")
                    .action(ArgAction.SetTrue)
                    .help("Verbose output"),
            ).subcommand(
                Command
                    .new("build")
                    .about("Build the project")
                    .arg(
                        Arg
                            .new("release")
                            .long("release")
                            .action(ArgAction.SetTrue)
                            .help("Build in release mode"),
                    ),
            )

    @Test
    fun testBashGeneration() {
        val script = ClapComplete.generate(Bash, testCommand())
        assertTrue(script.contains("_mycli()"))
        assertTrue(script.contains("complete -F _mycli"))
        assertTrue(script.contains("--config"))
        assertTrue(script.contains("--verbose"))
        assertTrue(script.contains("build"))
    }

    @Test
    fun testElvishGeneration() {
        val script = ClapComplete.generate(Elvish, testCommand())
        assertTrue(script.contains("edit:completion:arg-completer[mycli]"))
        assertTrue(script.contains("--config"))
        assertTrue(script.contains("build"))
    }

    @Test
    fun testFishGeneration() {
        val script = ClapComplete.generate(Fish, testCommand())
        assertTrue(script.contains("complete -c mycli"))
        assertTrue(script.contains("-l config"))
        assertTrue(script.contains("-l verbose"))
        assertTrue(script.contains("build"))
    }

    @Test
    fun testPowerShellGeneration() {
        val script = ClapComplete.generate(PowerShell, testCommand())
        assertTrue(script.contains("Register-ArgumentCompleter -Native -CommandName 'mycli'"))
        assertTrue(script.contains("'--config'"))
        assertTrue(script.contains("'--verbose'"))
        assertTrue(script.contains("'build'"))
    }

    @Test
    fun testZshGeneration() {
        val script = ClapComplete.generate(Zsh, testCommand())
        assertTrue(script.contains("#compdef mycli"))
        assertTrue(script.contains("_mycli()"))
        assertTrue(script.contains("--config"))
        assertTrue(script.contains("--verbose"))
    }

    @Test
    fun testShellEnumDelegation() {
        for (shell in Shell.valueVariants()) {
            val script = ClapComplete.generate(shell, testCommand())
            assertTrue(script.isNotEmpty())
            assertTrue(shell.fileName("mycli").isNotEmpty())
        }
    }
}
