// port-lint: source env/shells.rs
package io.github.kotlinmania.clapcomplete.env

import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clapcomplete.engine.complete

/**
 * Shell-integration for dynamic completions.
 */
interface EnvCompleter {
    /** Canonical name for this shell */
    fun name(): String

    /** Whether the name matches this shell */
    fun isMatch(name: String): Boolean

    /** Register for completions */
    fun writeRegistration(
        varName: String,
        name: String,
        bin: String,
        completer: String,
        buf: Appendable,
    )

    /** Complete the given command */
    fun writeComplete(
        cmd: Command,
        args: List<String>,
        currentDir: String?,
        buf: Appendable,
    )
}

/** Bash dynamic completion adapter */
object BashEnv : EnvCompleter {
    override fun name(): String = "bash"

    override fun isMatch(name: String): Boolean = name == "bash"

    override fun writeRegistration(
        varName: String,
        name: String,
        bin: String,
        completer: String,
        buf: Appendable,
    ) {
        val escapedName = name.replace('-', '_')
        buf.append(
            """
            |_clap_complete_$escapedName() {
            |    local IFS=${'$'}'\013'
            |    local _CLAP_COMPLETE_INDEX=${'$'}{COMP_CWORD}
            |    local _CLAP_COMPLETE_COMP_TYPE=${'$'}{COMP_TYPE}
            |    if compopt +o nospace 2> /dev/null; then
            |        local _CLAP_COMPLETE_SPACE=false
            |    else
            |        local _CLAP_COMPLETE_SPACE=true
            |    fi
            |    local words=("${'$'}{COMP_WORDS[@]}")
            |    if [[ "${'$'}{BASH_VERSINFO[0]}" -ge 4 ]]; then
            |        words[COMP_CWORD]="${'$'}2"
            |    fi
            |    COMPREPLY=( ${'$'}( \
            |        _CLAP_IFS="${'$'}IFS" \
            |        _CLAP_COMPLETE_INDEX="${'$'}_CLAP_COMPLETE_INDEX" \
            |        _CLAP_COMPLETE_COMP_TYPE="${'$'}_CLAP_COMPLETE_COMP_TYPE" \
            |        _CLAP_COMPLETE_SPACE="${'$'}_CLAP_COMPLETE_SPACE" \
            |        $varName="bash" \
            |        "$completer" -- "${'$'}{words[@]}" \
            |    ) )
            |    if [[ ${'$'}? != 0 ]]; then
            |        unset COMPREPLY
            |    elif [[ ${'$'}_CLAP_COMPLETE_SPACE == false ]] && [[ "${'$'}{COMPREPLY-}" =~ [=/:]${'$'} ]]; then
            |        compopt -o nospace
            |    fi
            |}
            |if [[ "${'$'}{BASH_VERSINFO[0]}" -eq 4 && "${'$'}{BASH_VERSINFO[1]}" -ge 4 || "${'$'}{BASH_VERSINFO[0]}" -gt 4 ]]; then
            |    complete -o nospace -o bashdefault -o nosort -F _clap_complete_$escapedName $bin
            |else
            |    complete -o nospace -o bashdefault -F _clap_complete_$escapedName $bin
            |fi
            |
            """.trimMargin(),
        )
    }

    override fun writeComplete(
        cmd: Command,
        args: List<String>,
        currentDir: String?,
        buf: Appendable,
    ) {
        val index = (args.size - 1).coerceAtLeast(0)
        val completions = complete(cmd, args, index, currentDir)
        for ((i, candidate) in completions.withIndex()) {
            if (i != 0) {
                buf.append("\n")
            }
            buf.append(candidate.getValue())
        }
    }
}

/** Elvish dynamic completion adapter */
object ElvishEnv : EnvCompleter {
    override fun name(): String = "elvish"

    override fun isMatch(name: String): Boolean = name == "elvish"

    override fun writeRegistration(
        varName: String,
        name: String,
        bin: String,
        completer: String,
        buf: Appendable,
    ) {
        buf.append(
            """
            |set edit:completion:arg-completer[$bin] = { |@words|
            |    var index = (count ${'$'}words)
            |    set index = (- ${'$'}index 1)
            |
            |    put (env _CLAP_IFS="\n" _CLAP_COMPLETE_INDEX=(to-string ${'$'}index) $varName="elvish" $completer -- ${'$'}@words) | to-lines
            |}
            |
            """.trimMargin(),
        )
    }

    override fun writeComplete(
        cmd: Command,
        args: List<String>,
        currentDir: String?,
        buf: Appendable,
    ) {
        val index = (args.size - 1).coerceAtLeast(0)
        val completions = complete(cmd, args, index, currentDir)
        for ((i, candidate) in completions.withIndex()) {
            if (i != 0) {
                buf.append("\n")
            }
            buf.append(candidate.getValue())
        }
    }
}

/** Fish dynamic completion adapter */
object FishEnv : EnvCompleter {
    override fun name(): String = "fish"

    override fun isMatch(name: String): Boolean = name == "fish"

    override fun writeRegistration(
        varName: String,
        name: String,
        bin: String,
        completer: String,
        buf: Appendable,
    ) {
        buf.append(
            "complete --keep-order --exclusive --command $bin --arguments \"($varName=fish $completer -- (commandline --current-process --tokenize --cut-at-cursor) (commandline --current-token))\"\n",
        )
    }

    override fun writeComplete(
        cmd: Command,
        args: List<String>,
        currentDir: String?,
        buf: Appendable,
    ) {
        val index = (args.size - 1).coerceAtLeast(0)
        val completions = complete(cmd, args, index, currentDir)
        for (candidate in completions) {
            buf.append(candidate.getValue())
            candidate.getHelp()?.let { help ->
                buf.append("\t").append(help.toString().lines().firstOrNull() ?: "")
            }
            buf.append("\n")
        }
    }
}

/** PowerShell dynamic completion adapter */
object PowerShellEnv : EnvCompleter {
    override fun name(): String = "powershell"

    override fun isMatch(name: String): Boolean = name == "powershell" || name == "powershell_ise" || name == "pwsh"

    override fun writeRegistration(
        varName: String,
        name: String,
        bin: String,
        completer: String,
        buf: Appendable,
    ) {
        buf.append(
            """
            |Register-ArgumentCompleter -Native -CommandName $bin -ScriptBlock {
            |    param(${'$'}wordToComplete, ${'$'}commandAst, ${'$'}cursorPosition)
            |
            |    ${'$'}prev = ${'$'}env:$varName;
            |    ${'$'}env:$varName = "powershell";
            |
            |    ${'$'}args = ${'$'}commandAst.Extent.Text
            |    ${'$'}args = ${'$'}args.Substring(0, [math]::Min(${'$'}cursorPosition, ${'$'}args.Length));
            |    if (${'$'}wordToComplete -eq "") {
            |        ${'$'}args += " ''";
            |    }
            |
            |    ${'$'}results = Invoke-Expression @"
            |& $completer -- ${'$'}args
            |"@;
            |    if (${'$'}null -eq ${'$'}prev) {
            |        Remove-Item Env:\$varName;
            |    } else {
            |        ${'$'}env:$varName = ${'$'}prev;
            |    }
            |    ${'$'}results | ForEach-Object {
            |        ${'$'}split = ${'$'}_.Split("`t");
            |        ${'$'}cmd = ${'$'}split[0];
            |
            |        if (${'$'}split.Length -eq 2) {
            |            ${'$'}help = ${'$'}split[1];
            |        }
            |        else {
            |            ${'$'}help = ${'$'}split[0];
            |        }
            |
            |        [System.Management.Automation.CompletionResult]::new(${'$'}cmd, ${'$'}cmd, 'ParameterValue', ${'$'}help)
            |    }
            |};
            |
            """.trimMargin(),
        )
    }

    override fun writeComplete(
        cmd: Command,
        args: List<String>,
        currentDir: String?,
        buf: Appendable,
    ) {
        val index = (args.size - 1).coerceAtLeast(0)
        val completions = complete(cmd, args, index, currentDir)
        for (candidate in completions) {
            buf.append(candidate.getValue())
            candidate.getHelp()?.let { help ->
                buf.append("\t").append(help.toString().lines().firstOrNull() ?: "")
            }
            buf.append("\n")
        }
    }
}

/** Zsh dynamic completion adapter */
object ZshEnv : EnvCompleter {
    override fun name(): String = "zsh"

    override fun isMatch(name: String): Boolean = name == "zsh"

    override fun writeRegistration(
        varName: String,
        name: String,
        bin: String,
        completer: String,
        buf: Appendable,
    ) {
        val escapedName = name.replace('-', '_')
        buf.append(
            """
            |#compdef $bin
            |function _clap_dynamic_completer_$escapedName() {
            |    local _CLAP_COMPLETE_INDEX=${'$'}(expr ${'$'}CURRENT - 1)
            |    local _CLAP_IFS=${'$'}'\n'
            |
            |    local completions=("${'$'}{(@f)${'$'}( \
            |        _CLAP_IFS="${'$'}_CLAP_IFS" \
            |        _CLAP_COMPLETE_INDEX="${'$'}_CLAP_COMPLETE_INDEX" \
            |        $varName="zsh" \
            |        $completer -- "${'$'}{words[@]}" 2>/dev/null \
            |    )}")
            |
            |    if [[ -n ${'$'}completions ]]; then
            |        local -a dirs=()
            |        local -a other=()
            |        local completion
            |        for completion in ${'$'}completions; do
            |            local value="${'$'}{completion%%:*}"
            |            if [[ "${'$'}value" == */ ]]; then
            |                local dir_no_slash="${'$'}{value%/}"
            |                if [[ "${'$'}completion" == *:* ]]; then
            |                    local desc="${'$'}{completion#*:}"
            |                    dirs+=("${'$'}dir_no_slash:${'$'}desc")
            |                else
            |                    dirs+=("${'$'}dir_no_slash")
            |                fi
            |            else
            |                other+=("${'$'}completion")
            |            fi
            |        done
            |        [[ -n ${'$'}dirs ]] && _describe 'values' dirs -S '/' -r '/'
            |        [[ -n ${'$'}other ]] && _describe 'values' other
            |    fi
            |}
            |
            |compdef _clap_dynamic_completer_$escapedName $bin
            |
            """.trimMargin(),
        )
    }

    override fun writeComplete(
        cmd: Command,
        args: List<String>,
        currentDir: String?,
        buf: Appendable,
    ) {
        val index = (args.size - 1).coerceAtLeast(0)
        val completions = complete(cmd, args, index, currentDir)
        for ((i, candidate) in completions.withIndex()) {
            if (i != 0) {
                buf.append("\n")
            }
            buf.append(escapeValue(candidate.getValue()))
            candidate.getHelp()?.let { help ->
                buf.append(":").append(escapeHelp(help.toString().lines().firstOrNull() ?: ""))
            }
        }
    }

    private fun escapeValue(string: String): String =
        string.replace("\\", "\\\\").replace(":", "\\:")

    private fun escapeHelp(string: String): String =
        string.replace("\\", "\\\\")
}
