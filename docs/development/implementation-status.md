# Historical Implementation Status Compatibility Pointer

> **Status:** `HISTORICAL_COMPATIBILITY`  
> **Live implementation controller:** `/IMPLEMENTATION.md`  
> **Canonical fine-grained graph:** `docs/development/implementation-programme-state.json`  
> **Historical verbose archive:** `docs/development/implementation-status-history-2026-09-14-pre-c2b-closure.md`

Under `MS-IMPLEMENTATION-RULES-001 v2.0`, this path no longer determines the live implementation frontier and is no longer synchronised at the end of implementation cycles.

The prior contents of this file remain historical implementation evidence in Git history and in the bounded evidence documents they referenced. Earlier evidence that required `implementation-status.md` under an earlier accepted IMPLEMENTATION-RULES version remains valid within its original scope and MUST NOT be mass-rewritten.

For current work use:

```text
/IMPLEMENTATION.md
    → current operational execution pointer

docs/development/implementation-programme-state.json
    → canonical fine-grained dependency/readiness graph

designs/IMPLEMENTATION-RULES.md
    → mandatory implementation execution rules
```

If current controller, graph and fresh evidence disagree, current graph/evidence governs and `/IMPLEMENTATION.md` must be corrected before implementation continues.