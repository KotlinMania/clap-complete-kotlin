import Testing
import ClapComplete

// Smoke test for the Kotlin → Swift Export → SPM → swift test pipeline.
//
// The file's mere existence and successful compilation prove three layers
// of the pipeline:
//
//   1. `embedSwiftExportForXcode` produced `ClapComplete.swiftmodule/`
//      and the supporting KotlinRuntimeSupport / ExportedKotlinPackages /
//      KotlinRuntime swiftmodule bundles. If any of them were missing,
//      `import ClapComplete` above would fail at compile time.
//
//   2. The static archive `libClapComplete.a` (produced by the
//      `linkSwiftExportBinaryDebugStaticMacosArm64` and
//      `mergeMacosDebugSwiftExportLibraries` tasks) supplied every
//      `__root____*` and `KotlinError`-related symbol the Swift modules
//      reference. If the archive were missing or empty, this test
//      executable would fail to link with "undefined symbols for
//      architecture arm64".
//
//   3. The Kotlin `swiftExport { moduleName = "ClapComplete" }` and
//      `flattenPackage = "io.github.kotlinmania.clapcomplete"` configuration in
//      build.gradle.kts produced a module name that's both syntactically
//      valid as a Swift identifier and reachable from this Package.swift
//      via the `ClapCompleteLibrary` product.
//
// Add more meaningful per-API tests below as the Swift Export surface
// grows. For now the import + a single passing assertion is the
// canary that the pipeline is green for this repo.
@Suite("ClapComplete Swift Export Smoke Tests")
struct ClapCompleteExportTests {
    private typealias Shell = ExportedKotlinPackages.io.github.kotlinmania.clapcomplete.aot.shells.Shell
    private typealias ZshGenerator = ExportedKotlinPackages.io.github.kotlinmania.clapcomplete.aot.shells.Zsh
    private typealias BashGenerator = ExportedKotlinPackages.io.github.kotlinmania.clapcomplete.aot.shells.Bash
    private typealias FishGenerator = ExportedKotlinPackages.io.github.kotlinmania.clapcomplete.aot.shells.Fish

    @Test
    func shellParsingAndFileNamesWork() {
        #expect(Shell.Companion.shared.valueVariants().count == 5)
        #expect(Shell.Companion.shared.fromStr(s: "zsh") == .Zsh)
        #expect(Shell.Companion.shared.fromShellPath(path: "/bin/fish") == .Fish)
        #expect(Shell.Companion.shared.fromShellPath(path: "/usr/local/bin/pwsh") == .PowerShell)
        #expect(Shell.Companion.shared.fromStr(s: "unknown") == nil)

        #expect(Shell.Bash.fileName(name: "mycli") == "mycli.bash")
        #expect(Shell.Zsh.fileName(name: "mycli") == "_mycli")
        #expect(Shell.Fish.fileName(name: "mycli") == "mycli.fish")
        #expect(BashGenerator.shared.fileName(name: "demo") == "demo.bash")
        #expect(FishGenerator.shared.fileName(name: "demo") == "demo.fish")
    }

    @Test
    func zshEscapingAndEngineEnvTypesWork() {
        let escaped = ZshGenerator.shared.escapeValue(string: "foo bar:baz")
        #expect(escaped == "foo\\ bar\\:baz")
        let escapedHelp = ZshGenerator.shared.escapeHelp(string: "a[b]:c\nd")
        #expect(escapedHelp == "a\\[b\\]\\:c d")

        let shells = env.Shells.Companion.shared.builtins()
        let names = shells.names()
        #expect(names.contains("bash"))
        #expect(names.contains("zsh"))
        #expect(names.contains("fish"))
        #expect(names.contains("elvish"))
        #expect(names.contains("powershell"))

        let candidate = engine.CompletionCandidate.Companion.shared.new(value: "status")
            .help(help: "Show status")
            .id(id: "cmd::status")
            .hide(hidden: false)
        #expect(candidate.getValue() == "status")
        #expect(candidate.getId() == "cmd::status")
        #expect(!candidate.isHideSet())
    }
}
