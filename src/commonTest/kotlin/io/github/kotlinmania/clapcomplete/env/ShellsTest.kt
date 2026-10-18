// port-lint: tests env/shells.rs
package io.github.kotlinmania.clapcomplete.env

import kotlin.test.Test
import kotlin.test.assertTrue

class ShellsTest {
    @Test
    fun fishEnvCompleterPathQuotingWorks() {
        fun getFishRegistration(completerBin: String): String {
            val buf = StringBuilder()
            Fish.writeRegistration(
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
}
