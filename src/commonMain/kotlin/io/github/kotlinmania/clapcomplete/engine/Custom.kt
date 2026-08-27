// port-lint: source engine/custom.rs
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
        val filterFunc = filter ?: { true }
        val candidates = completePath(current, currentDir, filterFunc).toMutableList()
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

internal fun completePath(
    value: String,
    currentDir: String?,
    isWanted: (String) -> Boolean,
): List<CompletionCandidate> {
    val (prefix, current) = splitFileName(value)
    val completions = mutableListOf<CompletionCandidate>()
    if (value.isEmpty() && isWanted(if (prefix.isEmpty()) "." else prefix)) {
        completions.add(CompletionCandidate.new("."))
    }
    return completions
}

internal fun isHidden(fileName: String): Boolean = fileName.startsWith(".")

internal fun splitFileName(path: String): Pair<String, String> {
    return if (pathHasName(path)) {
        val lastSlash = path.lastIndexOfAny(charArrayOf('/', '\\'))
        if (lastSlash >= 0) {
            path.substring(0, lastSlash) to path.substring(lastSlash + 1)
        } else {
            "" to path
        }
    } else {
        path to ""
    }
}

internal fun pathHasName(path: String): Boolean {
    if (path.isEmpty()) return false
    val trailing = path.last()
    return trailing != '/' && trailing != '\\' && !path.endsWith("..")
}

