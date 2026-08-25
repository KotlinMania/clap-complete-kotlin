# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 17/18 (94.4%)
- **Function parity:** 113/150 matched (target 172) — 75.3%
- **Class/type parity:** 15/27 matched (target 38) — 55.6%
- **Combined symbol parity:** 128/177 matched (target 210) — 72.3%
- **Average inline-code cosine:** 0.55 (function body across 12 matched files)
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

- **Target:** `shells.Shell`
- **Similarity:** 0.53
- **Dependents:** 2
- **Priority Score:** 2021204.8
- **Functions:** 9/10 matched (target 13)
- **Missing functions:** `fmt`
- **Types:** 1/2 matched
- **Missing types:** `Err`

### 2. shells.zsh

- **Target:** `shells.Zsh`
- **Similarity:** 0.73
- **Dependents:** 2
- **Priority Score:** 2012002.8
- **Functions:** 18/19 matched (target 18)
- **Missing functions:** `push_conflicts`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 3. shells.bash

- **Target:** `shells.Bash`
- **Similarity:** 0.71
- **Dependents:** 2
- **Priority Score:** 2001002.9
- **Functions:** 9/9 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 4. shells.fish

- **Target:** `shells.Fish`
- **Similarity:** 0.82
- **Dependents:** 2
- **Priority Score:** 2001001.8
- **Functions:** 9/9 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 5. shells.powershell

- **Target:** `shells.PowerShell`
- **Similarity:** 0.81
- **Dependents:** 2
- **Priority Score:** 2000802.0
- **Functions:** 7/7 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 6. shells.elvish

- **Target:** `shells.Elvish`
- **Similarity:** 0.82
- **Dependents:** 2
- **Priority Score:** 2000701.8
- **Functions:** 6/6 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 7. engine.complete

- **Target:** `engine.Complete`
- **Similarity:** 0.10
- **Dependents:** 1
- **Priority Score:** 1172109.0
- **Functions:** 3/20 matched (target 9)
- **Missing functions:** `complete_option`, `rsplit_delimiter`, `complete_custom_arg_value`, `complete_subcommand`, `complete_external_subcommand`, `longs_and_visible_aliases`, `hidden_longs_aliases`, `shorts_and_visible_aliases`, `populate_arg_candidate`, `possible_values`, `subcommands`, `populate_command_candidate`, `parse_shortflags`, `parse_positional`, `parse_opt_value`, `pos_allows_hyphen`, `opt_allows_hyphen`
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_
- **Lint issues:** 1

### 8. env.shells

- **Target:** `env.Shells`
- **Similarity:** 0.50
- **Dependents:** 1
- **Priority Score:** 1111605.0
- **Functions:** 5/9 matched (target 22)
- **Missing functions:** `is`, `from_str`, `default`, `fish_env_completer_path_quoting_works`
- **Types:** 0/7 matched (target 6)
- **Missing types:** `Bash`, `CompType`, `Err`, `Elvish`, `Fish`, `Powershell`, `Zsh`
- **Tests:** 0/1 matched
- **Lint issues:** 3

### 9. generator.utils

- **Target:** `generator.Utils`
- **Similarity:** 0.56
- **Dependents:** 1
- **Priority Score:** 1031704.4
- **Functions:** 14/17 matched (target 19)
- **Missing functions:** `common_app`, `built`, `built_with_version`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 7/10 matched

### 10. engine.custom

- **Target:** `engine.Custom`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 82106.8
- **Functions:** 9/15 matched (target 13)
- **Missing functions:** `fmt`, `default`, `complete_path`, `is_hidden`, `split_file_name`, `path_has_name`
- **Types:** 4/6 matched (target 4)
- **Missing types:** `ValueCompleter`, `ValueCandidates`

### 11. env.mod

- **Target:** `env.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 51510.0
- **Functions:** 8/12 matched (target 16)
- **Missing functions:** `var`, `complete`, `shell`, `write_registration`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `EnvCompleter`

### 12. generator.mod

- **Target:** `generator.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10410.0
- **Functions:** 2/3 matched
- **Missing functions:** `generate_to`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 13. lib

- **Target:** `clapcomplete.Lib [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/0 matched (target 4)
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 2)
- **Missing types:** `ReadmeDoctests`

### 14. engine.candidate

- **Target:** `engine.Candidate`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 1503.6
- **Functions:** 14/14 matched (target 17)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 15. aot.mod

- **Target:** `aot.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 16. engine.mod

- **Target:** `engine.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 17. shells.mod

- **Target:** `shells.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

