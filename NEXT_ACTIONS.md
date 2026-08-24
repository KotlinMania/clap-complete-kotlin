# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 3/18 (16.7%)
- **Function parity:** 17/159 matched (target 20) — 10.7%
- **Class/type parity:** 2/23 matched (target 4) — 8.7%
- **Combined symbol parity:** 19/182 matched (target 24) — 10.4%
- **Average inline-code cosine:** 0.59 (function body across 3 matched files)
- **Average documentation cosine:** 0.63 (doc text across 3 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 1 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. shells.shell

- **Target:** `shells.Shell`
- **Similarity:** 0.13
- **Dependents:** 2
- **Priority Score:** 2081208.6
- **Functions:** 3/10 matched (target 5)
- **Missing functions:** `fmt`, `value_variants`, `file_name`, `generate`, `try_generate`, `from_env`, `parse_shell_from_path`
- **Types:** 1/2 matched
- **Missing types:** `Err`

### 2. lib

- **Target:** `clapcomplete.Lib`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 10100.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/1 matched
- **Missing types:** `ReadmeDoctests`

### 3. engine.candidate

- **Target:** `engine.Candidate`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 1503.6
- **Functions:** 14/14 matched (target 15)
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

