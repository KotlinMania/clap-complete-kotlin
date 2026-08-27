// port-lint: tests env/mod.rs
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
    fun fishEnvCompleterPathQuotingWorks() {
        fun getFishRegistration(completerBin: String): String {
            val buf = StringBuilder()
            FishEnv.writeRegistration(
                varName = "IGNORED_VAR",
                name = "ignored-name",
                bin = "/ignored/bin",
                completer = completerBin,
                buf = buf,
            )
            return buf.toString()
        }

        val script1 = getFishRegistration("completer")
        assertTrue(script1.contains("completer --"))

        val script2 = getFishRegistration("/path/completer")
        assertTrue(script2.contains("/path/completer --"))

        val script3 = getFishRegistration("/path with a space/completer")
        assertTrue(script3.contains("'/path with a space/completer' --"))
    }

    @Test
    fun verifyCli() {
        val command =
            Command.new("dynamic")
                .arg(
                    io.github.kotlinmania.clap.Arg.new("input")
                        .long("input")
                        .short('i'),
                )
                .arg(
                    io.github.kotlinmania.clap.Arg.new("format")
                        .long("format")
                        .short('F'),
                )
        assertEquals("dynamic", command.getName())
    }
}
