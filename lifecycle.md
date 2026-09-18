# Main Street Design-to-Implementation Lifecycle

> **Status:** Non-authoritative navigation summary.  
> **Governing documents:** `designs/DOCUMENT-GOVERNANCE.md`, `designs/DESIGN-RULES.md`, `designs/IMPLEMENTATION-RULES.md`, `designs/AUTHORITY-INDEX.md`.

The earlier linear lifecycle sketch is superseded by the governed evidence loop below:

```text
product/domain need
    ↓
existing accepted authority?
    ├── yes → implementation evidence path
    │          ↓
    │      falsification test
    │          ↓
    │      smallest conformant implementation
    │          ↓
    │      proportional slice verification
          ↓
      full gate before node completion
    │          ↓
    │      IMPLEMENTATION.md / graph-evidence update
    │
    └── no consequential answer
               ↓
          governed design work
               ↓
          validation/falsification
               ↓
          accepted authority
               ↓
          implementation evidence path
```

Discussion, a workflow-tree entry, a diagram, implementation convenience or existing code does not by itself create accepted semantic/design authority.

Current implementation execution state is exposed through `/IMPLEMENTATION.md`; dependency/readiness truth is in `docs/development/implementation-programme-state.json`. Historical `implementation-status.md` and `docs/development/design-implementation-conformance.md` remain evidence only and must not determine current READY nodes.
