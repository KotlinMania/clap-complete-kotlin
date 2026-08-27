// port-lint: source clap_complete/src/aot/generator/utils.rs
package io.github.kotlinmania.clapcomplete.aot.generator

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clap.builder.PossibleValue

/**
 * Utility functions for generating shell completion scripts.
 */
object Utils {
    /**
     * Gets all subcommands including child subcommands in the form of `(name, binName)`.
     */
    fun allSubcommands(cmd: Command, parentBinName: String = cmd.getName()): List<Pair<String, String>> {
        val subcmds = mutableListOf<Pair<String, String>>()
        for (sc in cmd.getSubcommands()) {
            val scBinName = "$parentBinName ${sc.getName()}"
            subcmds.add(sc.getName() to scBinName)
            subcmds.addAll(allSubcommands(sc, scBinName))
        }
        return subcmds
    }

    /**
     * Finds a subcommand following the specified path segments.
     */
    fun findSubcommandWithPath(cmd: Command, path: List<String>): Command {
        var current = cmd
        for (segment in path) {
            current = current.getSubcommands().firstOrNull { sc ->
                sc.getName() == segment || sc.getVisibleAliases().contains(segment)
            } ?: error("subcommand should exist: $segment")
        }
        return current
    }

    /**
     * Gets the immediate subcommands of a command in the form of `(name, binName)`.
     */
    fun subcommands(cmd: Command, parentBinName: String = cmd.getName()): List<Pair<String, String>> {
        val subcmds = mutableListOf<Pair<String, String>>()
        for (sc in cmd.getSubcommands()) {
            val scBinName = "$parentBinName ${sc.getName()}"
            subcmds.add(sc.getName() to scBinName)
        }
        return subcmds
    }

    /**
     * Gets all short options and visible short aliases for a command.
     */
    fun shortsAndVisibleAliases(cmd: Command): List<Char> {
        val shorts = mutableListOf<Char>()
        for (opt in cmd.getArguments()) {
            opt.getShort()?.let { shorts.add(it) }
        }
        return shorts
    }

    /**
     * Gets all long options and visible long aliases for a command.
     */
    fun longsAndVisibleAliases(cmd: Command): List<String> {
        val longs = mutableListOf<String>()
        for (opt in cmd.getArguments()) {
            opt.getLong()?.let { longs.add(it) }
        }
        return longs
    }

    /**
     * Gets all flag arguments (non-positional arguments that do not take values).
     */
    fun flags(cmd: Command): List<Arg> =
        cmd.getArguments().filter { arg ->
            (arg.getShort() != null || arg.getLong() != null) && !arg.getAction().takesValues()
        }

    /**
     * Gets all option arguments (non-positional arguments that take values).
     */
    fun opts(cmd: Command): List<Arg> =
        cmd.getArguments().filter { arg ->
            (arg.getShort() != null || arg.getLong() != null) && arg.getAction().takesValues()
        }

    /**
     * Gets all positional arguments for a command.
     */
    fun positionals(cmd: Command): List<Arg> =
        cmd.getArguments().filter { arg ->
            arg.getShort() == null && arg.getLong() == null
        }

    /**
     * Returns the possible values for an argument, or null if empty.
     */
    fun possibleValues(arg: Arg): List<PossibleValue>? {
        val pv = arg.getPossibleValues()
        return if (pv.isEmpty()) null else pv
    }
}
