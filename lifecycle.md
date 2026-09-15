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
    │      full verification
    │          ↓
    │      implementation-status / conformance update
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

Current implementation state and accepted-but-partial scope are recorded in `docs/development/implementation-status.md` and the current macro-specific conformance record it references. `docs/development/design-implementation-conformance.md` is a dated historical snapshot and must not be used to determine current READY nodes.
