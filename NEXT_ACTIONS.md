# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 11/18 (61.1%)
- **Function parity:** 131/135 matched (target 185) — 97.0%
- **Class/type parity:** 22/22 matched (target 33) — 100.0%
- **Combined symbol parity:** 153/157 matched (target 218) — 97.5%
- **Average inline-code cosine:** 0.68 (function body across 11 matched files)
- **Average documentation cosine:** 0.70 (doc text across 11 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 4 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. shells.zsh

- **Target:** `shells.Zsh`
- **Similarity:** 0.79
- **Dependents:** 2
- **Priority Score:** 2002002.0
- **Functions:** 19/19 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 2. shells.shell

- **Target:** `shells.Shell`
- **Similarity:** 0.54
- **Dependents:** 2
- **Priority Score:** 2001204.6
- **Functions:** 10/10 matched (target 14)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

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

### 7. generator.utils

- **Target:** `generator.Utils`
- **Similarity:** 0.56
- **Dependents:** 1
- **Priority Score:** 1031704.4
- **Functions:** 14/17 matched (target 22)
- **Missing functions:** `common_app`, `built`, `built_with_version`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 7/10 matched

### 8. env.shells

- **Target:** `env.Shells`
- **Similarity:** 0.57
- **Dependents:** 1
- **Priority Score:** 1011604.3
- **Functions:** 8/9 matched (target 27)
- **Missing functions:** `is`
- **Types:** 7/7 matched (target 9)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 9. engine.complete

- **Target:** `engine.Complete`
- **Similarity:** 0.70
- **Dependents:** 1
- **Priority Score:** 1002103.0
- **Functions:** 20/20 matched (target 24)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 10. engine.custom

- **Target:** `engine.Custom`
- **Similarity:** 0.49
- **Dependents:** 0
- **Priority Score:** 2105.1
- **Functions:** 15/15 matched (target 24)
- **Missing functions:** _none_
- **Types:** 6/6 matched
- **Missing types:** _none_

### 11. engine.candidate

- **Target:** `engine.Candidate`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 1503.6
- **Functions:** 14/14 matched (target 17)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `generator.mod` | `aot.generator.Mod` | 0 | `aot/generator/mod.rs` | `aot/generator/Mod.kt` |
| `aot.mod` | `aot.Mod` | 0 | `aot/mod.rs` | `aot/Mod.kt` |
| `shells.mod` | `aot.shells.Mod` | 0 | `aot/shells/mod.rs` | `aot/shells/Mod.kt` |
| `engine.mod` | `engine.Mod` | 0 | `engine/mod.rs` | `engine/Mod.kt` |
| `env.mod` | `env.Mod` | 0 | `env/mod.rs` | `env/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `macros` | `Macros` | 0 | `macros.rs` | `Macros.kt` |

