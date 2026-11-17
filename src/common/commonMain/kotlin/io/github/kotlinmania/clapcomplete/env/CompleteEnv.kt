package io.github.kotlinmania.clapcomplete.env

import io.github.kotlinmania.clap.Command

/**
 * Collection of shell-specific dynamic completers.
 */
class Shells(
    private val completers: List<EnvCompleter>,
) {
    fun completer(name: String): EnvCompleter? = completers.firstOrNull { it.isMatch(name) }

    fun names(): List<String> = completers.map { it.name() }

    fun iter(): List<EnvCompleter> = completers

    companion object {
        fun builtins(): Shells =
            Shells(listOf(Bash, Elvish, Fish, Powershell, Zsh))
    }
}

/**
 * Environment-activated completions for your CLI.
 */
class CompleteEnv(
    private val factory: () -> Command,
) {
    private var varName: String = "COMPLETE"
    private var binName: String? = null
    private var completerName: String? = null
    private var shellRegistry: Shells = Shells.builtins()

    fun `var`(varName: String): CompleteEnv =
        apply {
            this.varName = varName
        }

    fun bin(bin: String): CompleteEnv =
        apply {
            this.binName = bin
        }

    fun completer(completer: String): CompleteEnv =
        apply {
            this.completerName = completer
        }

    fun shells(shells: Shells): CompleteEnv =
        apply {
            this.shellRegistry = shells
        }

    fun tryComplete(
        args: List<String>,
        currentDir: String? = null,
        envLookup: (String) -> String? = { null },
        output: Appendable = StringBuilder(),
    ): Boolean {
        val shellEnv = envLookup(varName) ?: return false
        if (shellEnv.isEmpty() || shellEnv == "0") {
            return false
        }

        val shellName =
            shellEnv
                .substringAfterLast('/')
                .substringAfterLast('\\')
                .substringBeforeLast('.')
        val shell = shellRegistry.completer(shellName) ?: return false
        val cmd = factory()

        val completionArgs =
            if (args.isEmpty()) {
                emptyList()
            } else {
                val tail = args.drop(1)
                val escapeIdx = tail.indexOf("--")
                if (escapeIdx >= 0) {
                    tail.drop(escapeIdx + 1)
                } else {
                    emptyList()
                }
            }

        if (completionArgs.isEmpty()) {
            val bin = binName ?: cmd.getName()
            val completer = completerName ?: bin
            shell.writeRegistration(varName, cmd.getName(), bin, completer, output)
        } else {
            shell.writeComplete(cmd, completionArgs, currentDir, output)
        }

        return true
    }

    companion object {
        fun withFactory(factory: () -> Command): CompleteEnv = CompleteEnv(factory)
    }
}
