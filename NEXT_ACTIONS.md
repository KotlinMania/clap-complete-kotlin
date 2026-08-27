# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 18/18 (100.0%)
- **Function parity:** 135/150 matched (target 193) — 90.0%
- **Class/type parity:** 15/27 matched (target 38) — 55.6%
- **Combined symbol parity:** 150/177 matched (target 231) — 84.7%
- **Average inline-code cosine:** 0.57 (function body across 13 matched files)
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
- **Similarity:** 0.79
- **Dependents:** 2
- **Priority Score:** 2002002.1
- **Functions:** 19/19 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/2 matched
- **Lint issues:** 3

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

### 7. env.shells

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

### 8. generator.utils

- **Target:** `generator.Utils`
- **Similarity:** 0.56
- **Dependents:** 1
- **Priority Score:** 1031704.4
- **Functions:** 14/17 matched (target 19)
- **Missing functions:** `common_app`, `built`, `built_with_version`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 7/10 matched

### 9. engine.complete

- **Target:** `engine.Complete`
- **Similarity:** 0.70
- **Dependents:** 1
- **Priority Score:** 1002102.9
- **Functions:** 20/20 matched (target 24)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 10. env.mod

- **Target:** `env.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 51510.0
- **Functions:** 8/12 matched (target 16)
- **Missing functions:** `var`, `complete`, `shell`, `write_registration`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `EnvCompleter`

### 11. engine.custom

- **Target:** `engine.Custom`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 42105.4
- **Functions:** 13/15 matched (target 17)
- **Missing functions:** `fmt`, `default`
- **Types:** 4/6 matched (target 4)
- **Missing types:** `ValueCompleter`, `ValueCandidates`

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

### 15. macros

- **Target:** `clapcomplete.Macros [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 16. aot.mod

- **Target:** `aot.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 17. engine.mod

- **Target:** `engine.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 18. shells.mod

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

