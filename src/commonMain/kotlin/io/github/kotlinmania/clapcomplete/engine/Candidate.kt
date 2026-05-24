// port-lint: source engine/candidate.rs
package io.github.kotlinmania.clapcomplete.engine

import io.github.kotlinmania.clap.builder.StyledStr

/**
 * A shell-agnostic completion candidate.
 */
data class CompletionCandidate(
    private val value: String = "",
    private val helpValue: StyledStr? = null,
    private val idValue: String? = null,
    private val tagValue: StyledStr? = null,
    private val displayOrderValue: Int? = null,
    private val hidden: Boolean = false,
) : Comparable<CompletionCandidate> {
    /**
     * Set the help message of the completion candidate.
     */
    fun help(help: StyledStr?): CompletionCandidate = copy(helpValue = help)

    /**
     * Only first for a given Id is shown.
     *
     * To reduce the risk of conflicts, this should likely contain a namespace.
     */
    fun id(id: String?): CompletionCandidate = copy(idValue = id)

    /**
     * Group candidates by tag.
     *
     * Future: these may become user-visible.
     */
    fun tag(tag: StyledStr?): CompletionCandidate = copy(tagValue = tag)

    /**
     * Sort weight within a [tag].
     */
    fun displayOrder(order: Int?): CompletionCandidate = copy(displayOrderValue = order)

    /**
     * Set the visibility of the completion candidate.
     *
     * Only shown when there is no visible candidate for completing the current argument.
     */
    fun hide(hidden: Boolean): CompletionCandidate = copy(hidden = hidden)

    /**
     * Add a prefix to the value of completion candidate.
     *
     * This is generally used for post-process by [complete] for things like
     * pre-pending flags, merging delimiter-separated values, etc.
     */
    fun addPrefix(prefix: String): CompletionCandidate = copy(value = prefix + value)

    /**
     * Get the literal value being proposed for completion.
     */
    fun getValue(): String = value

    /**
     * Get the help message of the completion candidate.
     */
    fun getHelp(): StyledStr? = helpValue

    /**
     * Get the id used for de-duplicating.
     */
    fun getId(): String? = idValue

    /**
     * Get the grouping tag.
     */
    fun getTag(): StyledStr? = tagValue

    /**
     * Get the grouping tag.
     */
    fun getDisplayOrder(): Int? = displayOrderValue

    /**
     * Get the visibility of the completion candidate.
     */
    fun isHideSet(): Boolean = hidden

    override fun compareTo(other: CompletionCandidate): Int =
        compareBy<CompletionCandidate>(
            { it.value },
            { it.helpValue?.toString() },
            { it.idValue },
            { it.tagValue?.toString() },
            { it.displayOrderValue },
            { it.hidden },
        ).compare(this, other)

    companion object {
        /**
         * Create a new completion candidate.
         */
        fun new(value: String): CompletionCandidate = CompletionCandidate(value)

        fun from(value: String): CompletionCandidate = CompletionCandidate(value)
    }
}
