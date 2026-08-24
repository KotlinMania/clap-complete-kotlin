// port-lint: source lib.rs
package io.github.kotlinmania.clapcomplete

import io.github.kotlinmania.clap.Command
import io.github.kotlinmania.clapcomplete.aot.generator.Generator
import io.github.kotlinmania.clapcomplete.aot.generator.generateToString

/**
 * Command line completion support for clap-kotlin.
 */
object ClapComplete {
    const val VERSION: String = "0.1.1"

    /**
     * Generate completion script for the given generator and command.
     */
    fun generate(generator: Generator, cmd: Command, binName: String = cmd.getName()): String =
        generateToString(generator, cmd, binName)
}
