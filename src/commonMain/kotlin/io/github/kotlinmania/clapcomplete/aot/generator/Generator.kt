// port-lint: source clap_complete/src/aot/generator/mod.rs
package io.github.kotlinmania.clapcomplete.aot.generator

import io.github.kotlinmania.clap.Command

/**
 * Generator trait which can be used to write generators for shell completions.
 */
interface Generator {
    /**
     * Returns the file name that is created when this generator is called.
     */
    fun fileName(name: String): String

    /**
     * Generates completion script output from a [Command].
     */
    fun generate(cmd: Command, buf: Appendable)

    /**
     * Fallible version to generate completion script output from a [Command].
     */
    fun tryGenerate(cmd: Command, buf: Appendable) {
        generate(cmd, buf)
    }
}

/**
 * Generate a completions file for a specified shell at runtime.
 */
fun generate(generator: Generator, cmd: Command, binName: String, buf: Appendable) {
    generator.generate(cmd, buf)
}

/**
 * Generate completion string for a specified shell.
 */
fun generateToString(generator: Generator, cmd: Command, binName: String = cmd.getName()): String {
    val sb = StringBuilder()
    generate(generator, cmd, binName, sb)
    return sb.toString()
}
