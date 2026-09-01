# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 18/22 (81.8%)
- **Function parity:** 135/161 matched (target 193) — 83.9%
- **Class/type parity:** 15/30 matched (target 38) — 50.0%
- **Combined symbol parity:** 150/191 matched (target 231) — 78.5%
- **Average inline-code cosine:** 0.56 (function body across 13 matched files)
- **Average documentation cosine:** 0.57 (doc text across 13 matched files)
- **Cheat-zeroed Files:** 7
- **Critical Issues:** 11 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. shells.shell

- **Target:** `shells.Shell [PROVENANCE-FALLBACK]`
- **Similarity:** 0.53
- **Dependents:** 2
- **Priority Score:** 2021204.8
- **Functions:** 9/10 matched (target 13)
- **Missing functions:** `fmt`
- **Types:** 1/2 matched
- **Missing types:** `Err`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/shells/shell.rs` vs expected `aot/shells/shell.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/shells/shell.rs` vs expected `aot/shells/shell.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:aot/shells/shell.rs` vs expected `aot/shells/shell.rs`
- **Proposed provenance header:** `// port-lint: source aot/shells/shell.rs` (current: `// port-lint: source aot/shells/shell.rs`)
- **Proposed provenance header:** `// port-lint: source aot/shells/shell.rs` (current: `// port-lint: source aot/shells/shell.rs`)
- **Proposed provenance header:** `// port-lint: tests aot/shells/shell.rs` (current: `// port-lint: tests aot/shells/shell.rs`)
- **Lint issues:** 3

### 2. shells.zsh

- **Target:** `shells.Zsh [PROVENANCE-FALLBACK]`
- **Similarity:** 0.79
- **Dependents:** 2
- **Priority Score:** 2002002.1
- **Functions:** 19/19 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/shells/zsh.rs` vs expected `aot/shells/zsh.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:aot/shells/zsh.rs` vs expected `aot/shells/zsh.rs`
- **Proposed provenance header:** `// port-lint: source aot/shells/zsh.rs` (current: `// port-lint: source aot/shells/zsh.rs`)
- **Proposed provenance header:** `// port-lint: tests aot/shells/zsh.rs` (current: `// port-lint: tests aot/shells/zsh.rs`)
- **Lint issues:** 5

### 3. shells.bash

- **Target:** `shells.Bash [PROVENANCE-FALLBACK]`
- **Similarity:** 0.71
- **Dependents:** 2
- **Priority Score:** 2001002.9
- **Functions:** 9/9 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/shells/bash.rs` vs expected `aot/shells/bash.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:aot/shells/bash.rs` vs expected `aot/shells/bash.rs`
- **Proposed provenance header:** `// port-lint: source aot/shells/bash.rs` (current: `// port-lint: source aot/shells/bash.rs`)
- **Proposed provenance header:** `// port-lint: tests aot/shells/bash.rs` (current: `// port-lint: tests aot/shells/bash.rs`)
- **Lint issues:** 2

### 4. shells.fish

- **Target:** `shells.Fish [PROVENANCE-FALLBACK]`
- **Similarity:** 0.82
- **Dependents:** 2
- **Priority Score:** 2001001.8
- **Functions:** 9/9 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/shells/fish.rs` vs expected `aot/shells/fish.rs`
- **Proposed provenance header:** `// port-lint: source aot/shells/fish.rs` (current: `// port-lint: source aot/shells/fish.rs`)
- **Lint issues:** 1

### 5. shells.powershell

- **Target:** `shells.PowerShell [PROVENANCE-FALLBACK]`
- **Similarity:** 0.81
- **Dependents:** 2
- **Priority Score:** 2000802.0
- **Functions:** 7/7 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/shells/powershell.rs` vs expected `aot/shells/powershell.rs`
- **Proposed provenance header:** `// port-lint: source aot/shells/powershell.rs` (current: `// port-lint: source aot/shells/powershell.rs`)
- **Lint issues:** 1

### 6. shells.elvish

- **Target:** `shells.Elvish [PROVENANCE-FALLBACK]`
- **Similarity:** 0.82
- **Dependents:** 2
- **Priority Score:** 2000701.8
- **Functions:** 6/6 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/shells/elvish.rs` vs expected `aot/shells/elvish.rs`
- **Proposed provenance header:** `// port-lint: source aot/shells/elvish.rs` (current: `// port-lint: source aot/shells/elvish.rs`)
- **Lint issues:** 1

### 7. env.shells

- **Target:** `env.Shells [PROVENANCE-FALLBACK]`
- **Similarity:** 0.50
- **Dependents:** 1
- **Priority Score:** 1111605.0
- **Functions:** 5/9 matched (target 22)
- **Missing functions:** `is`, `from_str`, `default`, `fish_env_completer_path_quoting_works`
- **Types:** 0/7 matched (target 6)
- **Missing types:** `Bash`, `CompType`, `Err`, `Elvish`, `Fish`, `Powershell`, `Zsh`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `env/shells.rs` vs expected `env/shells.rs`
- **Proposed provenance header:** `// port-lint: source env/shells.rs` (current: `// port-lint: source env/shells.rs`)
- **Lint issues:** 4

### 8. generator.utils

- **Target:** `generator.Utils [PROVENANCE-FALLBACK]`
- **Similarity:** 0.56
- **Dependents:** 1
- **Priority Score:** 1031704.4
- **Functions:** 14/17 matched (target 19)
- **Missing functions:** `common_app`, `built`, `built_with_version`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 7/10 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/generator/utils.rs` vs expected `aot/generator/utils.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:aot/generator/utils.rs` vs expected `aot/generator/utils.rs`
- **Proposed provenance header:** `// port-lint: source aot/generator/utils.rs` (current: `// port-lint: source aot/generator/utils.rs`)
- **Proposed provenance header:** `// port-lint: tests aot/generator/utils.rs` (current: `// port-lint: tests aot/generator/utils.rs`)
- **Lint issues:** 2

### 9. engine.complete

- **Target:** `engine.Complete [PROVENANCE-FALLBACK]`
- **Similarity:** 0.70
- **Dependents:** 1
- **Priority Score:** 1002103.0
- **Functions:** 20/20 matched (target 24)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `engine/complete.rs` vs expected `engine/complete.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:engine/complete.rs` vs expected `engine/complete.rs`
- **Proposed provenance header:** `// port-lint: source engine/complete.rs` (current: `// port-lint: source engine/complete.rs`)
- **Proposed provenance header:** `// port-lint: tests engine/complete.rs` (current: `// port-lint: tests engine/complete.rs`)
- **Lint issues:** 2

### 10. env.mod

- **Target:** `env.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 51510.0
- **Functions:** 8/12 matched (target 16)
- **Missing functions:** `var`, `complete`, `shell`, `write_registration`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `EnvCompleter`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `env/mod.rs` vs expected `env/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `env/mod.rs` vs expected `env/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:env/mod.rs` vs expected `env/mod.rs`
- **Proposed provenance header:** `// port-lint: source env/mod.rs` (current: `// port-lint: source env/mod.rs`)
- **Proposed provenance header:** `// port-lint: source env/mod.rs` (current: `// port-lint: source env/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests env/mod.rs` (current: `// port-lint: tests env/mod.rs`)
- **Lint issues:** 3

### 11. engine.custom

- **Target:** `engine.Custom [PROVENANCE-FALLBACK]`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 42105.4
- **Functions:** 13/15 matched (target 17)
- **Missing functions:** `fmt`, `default`
- **Types:** 4/6 matched (target 4)
- **Missing types:** `ValueCompleter`, `ValueCandidates`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `engine/custom.rs` vs expected `engine/custom.rs`
- **Proposed provenance header:** `// port-lint: source engine/custom.rs` (current: `// port-lint: source engine/custom.rs`)
- **Lint issues:** 1

### 12. generator.mod

- **Target:** `generator.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10410.0
- **Functions:** 2/3 matched
- **Missing functions:** `generate_to`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/generator/mod.rs` vs expected `aot/generator/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/generator/mod.rs` vs expected `aot/generator/mod.rs`
- **Proposed provenance header:** `// port-lint: source aot/generator/mod.rs` (current: `// port-lint: source aot/generator/mod.rs`)
- **Proposed provenance header:** `// port-lint: source aot/generator/mod.rs` (current: `// port-lint: source aot/generator/mod.rs`)
- **Lint issues:** 2

### 13. lib

- **Target:** `clapcomplete.Lib [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/0 matched (target 4)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 2)
- **Missing types:** `ReadmeDoctests`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests lib.rs`)
- **Lint issues:** 2

### 14. engine.candidate

- **Target:** `engine.Candidate [PROVENANCE-FALLBACK]`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 1503.6
- **Functions:** 14/14 matched (target 17)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `engine/candidate.rs` vs expected `engine/candidate.rs`
- **Proposed provenance header:** `// port-lint: source engine/candidate.rs` (current: `// port-lint: source engine/candidate.rs`)
- **Lint issues:** 1

### 15. macros

- **Target:** `clapcomplete.Macros [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `macros.rs` vs expected `macros.rs`
- **Proposed provenance header:** `// port-lint: source macros.rs` (current: `// port-lint: source macros.rs`)
- **Lint issues:** 1

### 16. aot.mod

- **Target:** `aot.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/mod.rs` vs expected `aot/mod.rs`
- **Proposed provenance header:** `// port-lint: source aot/mod.rs` (current: `// port-lint: source aot/mod.rs`)
- **Lint issues:** 1

### 17. engine.mod

- **Target:** `engine.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `engine/mod.rs` vs expected `engine/mod.rs`
- **Proposed provenance header:** `// port-lint: source engine/mod.rs` (current: `// port-lint: source engine/mod.rs`)
- **Lint issues:** 1

### 18. shells.mod

- **Target:** `shells.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/shells/mod.rs` vs expected `aot/shells/mod.rs`
- **Proposed provenance header:** `// port-lint: source aot/shells/mod.rs` (current: `// port-lint: source aot/shells/mod.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

