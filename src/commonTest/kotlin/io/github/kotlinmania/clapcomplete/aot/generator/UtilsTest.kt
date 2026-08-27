// port-lint: tests clap_complete/src/aot/generator/utils.rs
package io.github.kotlinmania.clapcomplete.aot.generator

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.ArgAction
import io.github.kotlinmania.clap.Command
import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {
    private fun commonApp(): Command =
        Command
            .new("myapp")
            .subcommand(
                Command
                    .new("test")
                    .subcommand(Command.new("config"))
                    .arg(
                        Arg
                            .new("file")
                            .short('f')
                            .visibleShortAlias('p')
                            .long("file")
                            .action(ArgAction.SetTrue)
                            .visibleAlias("path"),
                    ),
            ).subcommand(Command.new("hello"))

    private fun built(): Command = commonApp()

    private fun builtWithVersion(): Command = commonApp().version("3.0")

    @Test
    fun testSubcommands() {
        val cmd = builtWithVersion()
        assertEquals(
            listOf(
                "test" to "myapp test",
                "hello" to "myapp hello",
            ),
            Utils.subcommands(cmd),
        )
    }

    @Test
    fun testAllSubcommands() {
        val cmd = builtWithVersion()
        assertEquals(
            listOf(
                "test" to "myapp test",
                "config" to "myapp test config",
                "hello" to "myapp hello",
            ),
            Utils.allSubcommands(cmd),
        )
    }

    @Test
    fun testFindSubcommandWithPath() {
        val cmd = builtWithVersion()
        val scApp = Utils.findSubcommandWithPath(cmd, listOf("test", "config"))
        assertEquals("config", scApp.getName())
    }

    @Test
    fun testFlags() {
        val cmd = builtWithVersion()
        val scFlags = Utils.flags(Utils.findSubcommandWithPath(cmd, listOf("test")))
        assertEquals(1, scFlags.size)
        assertEquals("file", scFlags[0].getLong())
    }

    @Test
    fun testFlagSubcommand() {
        val cmd = built()
        val scFlags = Utils.flags(Utils.findSubcommandWithPath(cmd, listOf("test")))
        assertEquals(1, scFlags.size)
        assertEquals("file", scFlags[0].getLong())
    }

    @Test
    fun testShorts() {
        val cmd = builtWithVersion()
        val scShorts = Utils.shortsAndVisibleAliases(Utils.findSubcommandWithPath(cmd, listOf("test")))
        assertEquals(listOf('f'), scShorts)
    }

    @Test
    fun testLongs() {
        val cmd = builtWithVersion()
        val scLongs = Utils.longsAndVisibleAliases(Utils.findSubcommandWithPath(cmd, listOf("test")))
        assertEquals(listOf("file"), scLongs)
    }
}
