// port-lint: source clap_complete/src/engine/custom.rs
package io.github.kotlinmania.clapcomplete.engine

/**
 * User-provided completion candidates for an Arg.
 */
fun interface ValueCompleter {
    /**
     * All potential candidates for an argument matching `current`.
     */
    fun complete(current: String): List<CompletionCandidate>
}

/**
 * Extend Arg with a completer.
 */
class ArgValueCompleter(
    private val completer: ValueCompleter,
) {
    fun complete(current: String): List<CompletionCandidate> = completer.complete(current)

    companion object {
        fun new(completer: ValueCompleter): ArgValueCompleter = ArgValueCompleter(completer)
    }
}

/**
 * User-provided completion candidates for an Arg or Subcommand.
 */
fun interface ValueCandidates {
    /**
     * All potential candidates for an argument or subcommand.
     */
    fun candidates(): List<CompletionCandidate>
}

/**
 * Extend Arg with ValueCandidates.
 */
class ArgValueCandidates(
    private val candidatesProvider: ValueCandidates,
) {
    fun candidates(): List<CompletionCandidate> = candidatesProvider.candidates()

    companion object {
        fun new(candidatesProvider: ValueCandidates): ArgValueCandidates = ArgValueCandidates(candidatesProvider)
    }
}

/**
 * Extend Command with SubcommandCandidates.
 */
class SubcommandCandidates(
    private val candidatesProvider: ValueCandidates,
) {
    fun candidates(): List<CompletionCandidate> = candidatesProvider.candidates()

    companion object {
        fun new(candidatesProvider: ValueCandidates): SubcommandCandidates = SubcommandCandidates(candidatesProvider)
    }
}

/**
 * Complete a value as a path.
 */
class PathCompleter(
    private var filter: ((String) -> Boolean)? = null,
    private var currentDir: String? = null,
    private var stdio: Boolean = false,
) : ValueCompleter {
    fun stdio(): PathCompleter {
        this.stdio = true
        return this
    }

    fun filter(filter: (String) -> Boolean): PathCompleter {
        this.filter = filter
        return this
    }

    fun currentDir(path: String): PathCompleter {
        this.currentDir = path
        return this
    }

    override fun complete(current: String): List<CompletionCandidate> {
        val candidates = mutableListOf<CompletionCandidate>()
        if (stdio && current.isEmpty()) {
            candidates.add(CompletionCandidate.new("-").help("stdio"))
        }
        return candidates
    }

    companion object {
        fun any(): PathCompleter = PathCompleter()

        fun file(): PathCompleter = PathCompleter()

        fun dir(): PathCompleter = PathCompleter()
    }
}
