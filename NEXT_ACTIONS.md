# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 11/18 (61.1%)
- **Function parity:** 149/159 matched (target 185) — 93.7%
- **Class/type parity:** 22/22 matched (target 33) — 100.0%
- **Combined symbol parity:** 171/181 matched (target 218) — 94.5%
- **Average inline-code cosine:** 0.21 (function body across 11 matched files)
- **Average documentation cosine:** 0.70 (doc text across 11 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 11 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 0 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. shells.zsh

- **Target:** `shells.Zsh`
- **Similarity:** 0.27
- **Dependents:** 2
- **Priority Score:** 2002007.2
- **Functions:** 19/19 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 2. shells.shell

- **Target:** `shells.Shell`
- **Similarity:** 0.15
- **Dependents:** 2
- **Priority Score:** 2001208.5
- **Functions:** 10/10 matched (target 14)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 3. shells.bash

- **Target:** `shells.Bash`
- **Similarity:** 0.19
- **Dependents:** 2
- **Priority Score:** 2001008.1
- **Functions:** 9/9 matched (target 16)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 4. shells.fish

- **Target:** `shells.Fish`
- **Similarity:** 0.33
- **Dependents:** 2
- **Priority Score:** 2001006.8
- **Functions:** 9/9 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 5. shells.powershell

- **Target:** `shells.PowerShell`
- **Similarity:** 0.32
- **Dependents:** 2
- **Priority Score:** 2000806.8
- **Functions:** 7/7 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 6. shells.elvish

- **Target:** `shells.Elvish`
- **Similarity:** 0.33
- **Dependents:** 2
- **Priority Score:** 2000706.8
- **Functions:** 6/6 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 7. env.shells

- **Target:** `env.Shells`
- **Similarity:** 0.15
- **Dependents:** 1
- **Priority Score:** 1053208.5
- **Functions:** 20/25 matched (target 27)
- **Missing functions:** `is`, `is`, `is`, `is`, `is`
- **Types:** 7/7 matched (target 9)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 8. generator.utils

- **Target:** `generator.Utils`
- **Similarity:** 0.15
- **Dependents:** 1
- **Priority Score:** 1031708.5
- **Functions:** 14/17 matched (target 22)
- **Missing functions:** `common_app`, `built`, `built_with_version`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 7/10 matched

### 9. engine.complete

- **Target:** `engine.Complete`
- **Similarity:** 0.18
- **Dependents:** 1
- **Priority Score:** 1002108.2
- **Functions:** 20/20 matched (target 24)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 10. engine.custom

- **Target:** `engine.Custom`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 22908.8
- **Functions:** 21/23 matched (target 24)
- **Missing functions:** `candidates`, `complete`
- **Types:** 6/6 matched
- **Missing types:** _none_

### 11. engine.candidate

- **Target:** `engine.Candidate`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 1508.9
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

