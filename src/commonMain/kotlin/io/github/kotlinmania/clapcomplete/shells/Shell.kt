// port-lint: source aot/shells/shell.rs
package io.github.kotlinmania.clapcomplete.shells

import io.github.kotlinmania.clap.builder.PossibleValue

/**
 * Shell with auto-generated completion script available.
 */
public enum class Shell(
    public val value: String,
) {
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

    public fun toPossibleValue(): PossibleValue = PossibleValue.new(value)

    public companion object {
        public fun fromStr(s: String): Shell? = entries.firstOrNull { it.value.equals(s, ignoreCase = true) }

        public fun fromShellPath(path: String): Shell? {
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
