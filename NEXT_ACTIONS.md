# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 17/22 (77.3%)
- **Function parity:** 111/161 matched (target 158) — 68.9%
- **Class/type parity:** 15/30 matched (target 36) — 50.0%
- **Combined symbol parity:** 126/191 matched (target 194) — 66.0%
- **Average inline-code cosine:** 0.53 (function body across 12 matched files)
- **Average documentation cosine:** 0.62 (doc text across 12 matched files)
- **Cheat-zeroed Files:** 6
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
- **Similarity:** 0.47
- **Dependents:** 2
- **Priority Score:** 2031205.2
- **Functions:** 8/10 matched (target 11)
- **Missing functions:** `fmt`, `from_env`
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
- **Similarity:** 0.73
- **Dependents:** 2
- **Priority Score:** 2012002.8
- **Functions:** 18/19 matched (target 18)
- **Missing functions:** `push_conflicts`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/shells/zsh.rs` vs expected `aot/shells/zsh.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:aot/shells/zsh.rs` vs expected `aot/shells/zsh.rs`
- **Proposed provenance header:** `// port-lint: source aot/shells/zsh.rs` (current: `// port-lint: source aot/shells/zsh.rs`)
- **Proposed provenance header:** `// port-lint: tests aot/shells/zsh.rs` (current: `// port-lint: tests aot/shells/zsh.rs`)
- **Lint issues:** 2

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

### 7. engine.complete

- **Target:** `engine.Complete [PROVENANCE-FALLBACK]`
- **Similarity:** 0.10
- **Dependents:** 1
- **Priority Score:** 1172109.0
- **Functions:** 3/20 matched (target 9)
- **Missing functions:** `complete_option`, `rsplit_delimiter`, `complete_custom_arg_value`, `complete_subcommand`, `complete_external_subcommand`, `longs_and_visible_aliases`, `hidden_longs_aliases`, `shorts_and_visible_aliases`, `populate_arg_candidate`, `possible_values`, `subcommands`, `populate_command_candidate`, `parse_shortflags`, `parse_positional`, `parse_opt_value`, `pos_allows_hyphen`, `opt_allows_hyphen`
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `engine/complete.rs` vs expected `engine/complete.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:engine/complete.rs` vs expected `engine/complete.rs`
- **Proposed provenance header:** `// port-lint: source engine/complete.rs` (current: `// port-lint: source engine/complete.rs`)
- **Proposed provenance header:** `// port-lint: tests engine/complete.rs` (current: `// port-lint: tests engine/complete.rs`)
- **Lint issues:** 3

### 8. env.shells

- **Target:** `env.Shells [PROVENANCE-FALLBACK]`
- **Similarity:** 0.51
- **Dependents:** 1
- **Priority Score:** 1111604.9
- **Functions:** 5/9 matched (target 22)
- **Missing functions:** `is`, `from_str`, `default`, `fish_env_completer_path_quoting_works`
- **Types:** 0/7 matched (target 6)
- **Missing types:** `Bash`, `CompType`, `Err`, `Elvish`, `Fish`, `Powershell`, `Zsh`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `env/shells.rs` vs expected `env/shells.rs`
- **Proposed provenance header:** `// port-lint: source env/shells.rs` (current: `// port-lint: source env/shells.rs`)
- **Lint issues:** 4

### 9. generator.utils

- **Target:** `generator.Utils [PROVENANCE-FALLBACK]`
- **Similarity:** 0.45
- **Dependents:** 1
- **Priority Score:** 1041705.4
- **Functions:** 13/17 matched (target 16)
- **Missing functions:** `common_app`, `built`, `built_with_version`, `test_flag_subcommand`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 6/10 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `aot/generator/utils.rs` vs expected `aot/generator/utils.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:aot/generator/utils.rs` vs expected `aot/generator/utils.rs`
- **Proposed provenance header:** `// port-lint: source aot/generator/utils.rs` (current: `// port-lint: source aot/generator/utils.rs`)
- **Proposed provenance header:** `// port-lint: tests aot/generator/utils.rs` (current: `// port-lint: tests aot/generator/utils.rs`)
- **Lint issues:** 2

### 10. engine.custom

- **Target:** `engine.Custom [PROVENANCE-FALLBACK]`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 82106.8
- **Functions:** 9/15 matched (target 13)
- **Missing functions:** `fmt`, `default`, `complete_path`, `is_hidden`, `split_file_name`, `path_has_name`
- **Types:** 4/6 matched (target 4)
- **Missing types:** `ValueCompleter`, `ValueCandidates`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `engine/custom.rs` vs expected `engine/custom.rs`
- **Proposed provenance header:** `// port-lint: source engine/custom.rs` (current: `// port-lint: source engine/custom.rs`)
- **Lint issues:** 1

### 11. env.mod

- **Target:** `env.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 51510.0
- **Functions:** 8/12 matched (target 10)
- **Missing functions:** `var`, `complete`, `shell`, `write_registration`
- **Types:** 2/3 matched
- **Missing types:** `EnvCompleter`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `env/mod.rs` vs expected `env/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `env/mod.rs` vs expected `env/mod.rs`
- **Proposed provenance header:** `// port-lint: source env/mod.rs` (current: `// port-lint: source env/mod.rs`)
- **Proposed provenance header:** `// port-lint: source env/mod.rs` (current: `// port-lint: source env/mod.rs`)
- **Lint issues:** 2

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
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 0/1 matched
- **Missing types:** `ReadmeDoctests`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Lint issues:** 1

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

### 15. aot.mod

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

### 16. engine.mod

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

### 17. shells.mod

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

