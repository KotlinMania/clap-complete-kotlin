// port-lint: tests clap_complete/src/lib.rs
package io.github.kotlinmania.clapcomplete

import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clapcomplete.shells.Shell
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LibTest {
    @Test
    fun testVersion() {
        assertEquals("0.1.1", ClapComplete.VERSION)
    }

    @Test
    fun testGenerateBashScript() {
        val cmd = Command.new("myapp").about("A test CLI application")
        val script = ClapComplete.generate(Shell.Bash, cmd, "myapp")
        assertTrue(script.contains("myapp"))
        assertTrue(script.contains("complete -F"))
    }

    @Test
    fun testGenerateZshScript() {
        val cmd = Command.new("myapp").about("A test CLI application")
        val script = ClapComplete.generate(Shell.Zsh, cmd, "myapp")
        assertTrue(script.contains("compdef _myapp myapp") || script.contains("#compdef myapp"))
    }
}
