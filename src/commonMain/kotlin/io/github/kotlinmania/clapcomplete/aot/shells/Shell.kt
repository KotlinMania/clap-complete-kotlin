// port-lint: source aot/shells/shell.rs
package io.github.kotlinmania.clapcomplete.aot.shells

import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clap.builder.PossibleValue
import io.github.kotlinmania.clapcomplete.aot.generator.Generator

/**
 * Shell with auto-generated completion script available.
 */
enum class Shell(
    val value: String,
) : Generator {
    /** Bourne Again SHell (bash) */
    Bash("bash"),

    /** Elvish shell */
    Elvish("elvish"),

    /** Friendly Interactive SHell (fish) */
    Fish("fish"),

    /** PowerShell */
    PowerShell("powershell"),

    /** Z SHell (zsh) */
    Zsh("zsh"),
    ;

    fun toPossibleValue(): PossibleValue = PossibleValue.new(value)

    override fun fileName(name: String): String =
        when (this) {
            Bash ->
                io.github.kotlinmania.clapcomplete.aot.shells.Bash
                    .fileName(name)
            Elvish ->
                io.github.kotlinmania.clapcomplete.aot.shells.Elvish
                    .fileName(name)
            Fish ->
                io.github.kotlinmania.clapcomplete.aot.shells.Fish
                    .fileName(name)
            PowerShell ->
                io.github.kotlinmania.clapcomplete.aot.shells.PowerShell
                    .fileName(name)
            Zsh ->
                io.github.kotlinmania.clapcomplete.aot.shells.Zsh
                    .fileName(name)
        }

    override fun generate(cmd: Command, buf: Appendable) {
        tryGenerate(cmd, buf)
    }

    override fun tryGenerate(cmd: Command, buf: Appendable) {
        when (this) {
            Bash ->
                io.github.kotlinmania.clapcomplete.aot.shells.Bash
                    .tryGenerate(cmd, buf)
            Elvish ->
                io.github.kotlinmania.clapcomplete.aot.shells.Elvish
                    .tryGenerate(cmd, buf)
            Fish ->
                io.github.kotlinmania.clapcomplete.aot.shells.Fish
                    .tryGenerate(cmd, buf)
            PowerShell ->
                io.github.kotlinmania.clapcomplete.aot.shells.PowerShell
                    .tryGenerate(cmd, buf)
            Zsh ->
                io.github.kotlinmania.clapcomplete.aot.shells.Zsh
                    .tryGenerate(cmd, buf)
        }
    }

    override fun toString(): String = value

    companion object {
        fun valueVariants(): List<Shell> = entries

        fun fromStr(s: String): Shell? = entries.firstOrNull { it.value.equals(s, ignoreCase = true) }

        fun fromShellPath(path: String): Shell? = parseShellFromPath(path)

        fun fromEnv(getEnv: (String) -> String? = { null }): Shell? {
            val envShell = getEnv("SHELL")
            return if (envShell != null) {
                fromShellPath(envShell)
            } else {
                null
            }
        }

        fun parseShellFromPath(path: String): Shell? {
            val name = path.substringAfterLast('/').substringAfterLast('\\').substringBeforeLast('.')
            return when (name.lowercase()) {
                "bash" -> Bash
                "zsh" -> Zsh
                "fish" -> Fish
                "elvish" -> Elvish
                "powershell", "powershell_ise", "pwsh" -> PowerShell
                else -> null
            }
        }
    }
}
