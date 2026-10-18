package io.github.kotlinmania.clapcomplete.env

import io.github.kotlinmania.clap.Command
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CompleteEnvTest {
    @Test
    fun testShellsBuiltins() {
        val shells = Shells.builtins()
        val names = shells.names()
        assertTrue(names.contains("bash"))
        assertTrue(names.contains("zsh"))
        assertTrue(names.contains("fish"))
        assertTrue(names.contains("elvish"))
        assertTrue(names.contains("powershell"))
    }

    @Test
    fun testCompleteEnvTryCompleteDisabled() {
        val completeEnv = CompleteEnv.withFactory { Command.new("testcli") }
        val output = StringBuilder()
        val completed =
            completeEnv.tryComplete(
                args = emptyList(),
                envLookup = { null },
                output = output,
            )
        assertFalse(completed)
        assertEquals("", output.toString())
    }

    @Test
    fun testCompleteEnvTryCompleteRegistration() {
        val completeEnv =
            CompleteEnv
                .withFactory { Command.new("testcli") }
                .`var`("_TESTCLI_COMPLETE")
                .bin("testcli")
        val output = StringBuilder()
        val completed =
            completeEnv.tryComplete(
                args = emptyList(),
                envLookup = { key -> if (key == "_TESTCLI_COMPLETE") "bash" else null },
                output = output,
            )
        assertTrue(completed)
        assertTrue(output.isNotEmpty())
    }

    @Test
    fun verifyCli() {
        val command =
            Command
                .new("dynamic")
                .arg(
                    io.github.kotlinmania.clap.Arg
                        .new("input")
                        .long("input")
                        .short('i'),
                ).arg(
                    io.github.kotlinmania.clap.Arg
                        .new("format")
                        .long("format")
                        .short('F'),
                )
        assertEquals("dynamic", command.getName())
    }
}
