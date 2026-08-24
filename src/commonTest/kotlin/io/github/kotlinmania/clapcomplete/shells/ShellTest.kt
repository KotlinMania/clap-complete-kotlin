// port-lint: tests aot/shells/shell.rs
package io.github.kotlinmania.clapcomplete.shells

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ShellTest {
    @Test
    fun testShellFromStr() {
        assertEquals(Shell.Bash, Shell.fromStr("bash"))
        assertEquals(Shell.Zsh, Shell.fromStr("zsh"))
        assertEquals(Shell.Fish, Shell.fromStr("fish"))
        assertEquals(Shell.Elvish, Shell.fromStr("elvish"))
        assertEquals(Shell.PowerShell, Shell.fromStr("powershell"))
        assertNull(Shell.fromStr("unknown"))
    }

    @Test
    fun testShellFromPath() {
        assertEquals(Shell.Bash, Shell.fromShellPath("/bin/bash"))
        assertEquals(Shell.Zsh, Shell.fromShellPath("/usr/bin/zsh"))
        assertEquals(Shell.Fish, Shell.fromShellPath("/usr/local/bin/fish"))
        assertEquals(Shell.Elvish, Shell.fromShellPath("/usr/bin/elvish"))
        assertEquals(Shell.PowerShell, Shell.fromShellPath("C:\\Windows\\System32\\powershell.exe"))
        assertNull(Shell.fromShellPath("/opt/custom_shell"))
    }
}
