// port-lint: tests engine/complete.rs
package io.github.kotlinmania.clapcomplete.engine

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.ArgAction
import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clapcomplete.env.CompleteEnv
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompleteEngineTest {
    private fun testCommand(): Command =
        Command
            .new("myapp")
            .arg(
                Arg
                    .new("format")
                    .long("format")
                    .valueParserStrings(listOf("json", "yaml", "toml")),
            ).arg(
                Arg
                    .new("verbose")
                    .short('v')
                    .long("verbose")
                    .action(ArgAction.SetTrue),
            ).subcommand(
                Command
                    .new("test")
                    .about("Run tests"),
            )

    @Test
    fun testCompleteSubcommandsAndOptions() {
        val cmd = testCommand()
        val candidates = complete(cmd, listOf("myapp", ""), 1)
        val values = candidates.map { it.getValue() }
        assertTrue(values.contains("test"))
        assertTrue(values.contains("--format"))
        assertTrue(values.contains("--verbose"))
        assertTrue(values.contains("-v"))
    }

    @Test
    fun testCompletePossibleValues() {
        val cmd = testCommand()
        val candidates = complete(cmd, listOf("myapp", "--format", "j"), 2)
        assertEquals(listOf("json"), candidates.map { it.getValue() })
    }

    @Test
    fun testCompleteEnvRegistration() {
        val env = CompleteEnv.withFactory { testCommand() }
        val output = StringBuilder()
        val completed =
            env.tryComplete(
                args = emptyList(),
                envLookup = { if (it == "COMPLETE") "bash" else null },
                output = output,
            )
        assertTrue(completed)
        assertTrue(output.contains("_clap_complete_myapp()"))
    }
}
