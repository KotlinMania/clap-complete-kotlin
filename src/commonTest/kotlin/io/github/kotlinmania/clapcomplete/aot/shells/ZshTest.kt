// port-lint: tests aot/shells/zsh.rs
package io.github.kotlinmania.clapcomplete.aot.shells

import kotlin.test.Test
import kotlin.test.assertEquals

class ZshTest {
    @Test
    fun testEscapeValue() {
        val rawString = "\\ [foo]() `bar https://\$PATH"
        assertEquals(
            "\\\\\\ \\[foo\\]\\(\\)\\ \\`bar\\ https\\://\\\$PATH",
            Zsh.escapeValue(rawString),
        )
    }

    @Test
    fun testEscapeHelp() {
        val rawString = "\\ [foo]() `bar https://\$PATH"
        assertEquals(
            "\\\\ \\[foo\\]() \\`bar https\\://\\\$PATH",
            Zsh.escapeHelp(rawString),
        )
    }
}
