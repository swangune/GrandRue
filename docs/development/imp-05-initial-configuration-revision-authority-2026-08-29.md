# IMP-05 — Initial Configuration Revision Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** D1 — Configuration Revision materialisation/persistence
**Date:** 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements the initial Merchant Configuration bootstrap boundary required by:

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`;
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`;
- `designs/MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment.md`;
- the exact configuration-handoff requirements of `designs/MS-PROT-052 v1.2 — Onboarding Case Lifecycle, Concurrency & Initial Configuration Intent Handoff Amendment.md`;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

D1 owns acceptance of the immutable Initial Configuration Intent. Onboarding remains the source-evidence authority and does not choose a semantic release or create executable configuration.

## 2. Implemented production boundary

The implementation provides:

- a Configuration-owned materialisation authority and exact merchant-scoped revision lookup;
- immutable version-1 `MerchantConfigurationRevision` content with no base revision or Fulfilment Binding Set;
- exact source Initial Configuration Intent, Onboarding Case and reviewed revision provenance;
- a platform-owned ordinary new-configuration semantic-release selector that callers, merchants and AI cannot override;
- normalized immutable capability and policy-selection persistence;
- one revision per source intent and one version-1 revision per Merchant Scope;
- deterministic retry that returns the original release-pinned revision after the ordinary release advances;
- per-intent, per-merchant and per-revision concurrency serialization;
- fail-closed rejection of unsupported seed namespaces and unavailable ordinary semantic release;
- an exact append-only `COMPLETE` Onboarding Case revision when Configuration accepts responsibility for the intent; and
- one transaction for revision persistence and source-case completion, so either both commit or neither does.

Materialisation establishes immutable configuration evidence only. It does not validate, compile, approve, publish or activate the revision.

## 3. Durable representation

Flyway migration `V36__configuration__create_revision_authority.sql`:

- binds a Configuration Revision to the exact immutable source intent and reviewed Onboarding Case revision;
- enforces merchant-scoped revision identity and version uniqueness;
- enforces the initial-revision shape (`version = 1`, no base, no binding);
- stores exact semantic-release affinity and materialisation provenance; and
- normalizes immutable capability and policy selections.

Flyway migration `V37__onboarding__acknowledge_configuration_handoff.sql`:

- admits the append-only `COMPLETE` mutation kind; and
- requires every initial Configuration Revision to identify its exact Onboarding Case completion revision.

## 4. Conformance evidence

| Required behavior | Executable evidence | Result |
|---|---|---|
| Exact intent materialises one immutable first revision | `JooqConfigurationRevisionAuthorityIT` | PASS |
| Revision stores exact merchant, source-case, source-intent and semantic-release affinity | `JooqConfigurationRevisionAuthorityIT` | PASS |
| Configuration acceptance atomically completes the source Onboarding Case | `JooqConfigurationRevisionAuthorityIT` | PASS |
| Retry after ordinary-release advancement returns the original pinned revision and one completion | `JooqConfigurationRevisionAuthorityIT` | PASS |
| One intent cannot be reused with a different revision identity | `JooqConfigurationRevisionAuthorityIT` | PASS |
| Concurrent handoff delivery commits at most one revision | `JooqConfigurationRevisionAuthorityIT` | PASS |
| Unsupported semantic seed namespaces fail closed | `JooqConfigurationRevisionAuthorityIT` | PASS |
| Unavailable ordinary release leaves revision absent and case submitted | `JooqConfigurationRevisionAuthorityIT` | PASS |

## 5. IMPLEMENTATION-RULES trace

The RED step added the PostgreSQL integration contract before the D1 production contracts existed. Focused test compilation failed on the missing Configuration Revision authority, command, revision, release-selector and failure symbols. The GREEN step added the minimum domain ports, immutable revision record, jOOQ transaction owner and schema migrations.

Focused verification then exposed two test-environment issues: a timestamp fixture needed an explicit PostgreSQL `timestamptz` cast, and an already-applied unshipped draft of V36 had left one row before V37 introduced the completion-revision requirement. The disposable integration schema was reset, all 37 migrations were reapplied from empty, and test cleanup was strengthened to prevent cross-class revision leakage. The first full run also collided with VS Code's Java language server writing to Maven's `target/classes`; workspace auto-build was temporarily disabled, the canonical gate passed in isolation, and the workspace setting was restored.

The final canonical gate ran against PostgreSQL 18.6:

```text
mvn --batch-mode clean verify -Ppostgres-it

Java release target:                   25
Flyway migrations validated:           37
production Java sources compiled:      637
test Java sources compiled:            230
unit / conformance tests:              662 PASS
PostgreSQL integration tests:          217 PASS
total Maven tests:                     879 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

## 6. Explicit non-claims

D1 does not claim completion of:

- later Configuration Revisions, change intents, rebasing or migration;
- a durable administration mechanism for advancing the ordinary semantic-release reference;
- deterministic validation, compilation or Resolved Configuration Package orchestration for this persisted revision;
- Configuration Revision approval authority/persistence;
- publication, activation or serving-deployment admission;
- profile, location or Business Hours fact adoption;
- HTTP/UI transport; or
- end-to-end IMP-05 completion.

## 7. Graph consequence

D1 initial Configuration Revision materialisation/persistence is **CONFORMING_COMPLETE**. D2a deterministic compilation/RCP remains **CONFORMING_COMPLETE** from IMP-03, and D2b durable exact validation/package evidence is now **CONFORMING_COMPLETE**. D2c immutable business-facing impact-review evidence is the selected critical **READY** node; D3 current-Controller approval persistence remains blocked on D2c. B2 durable Business Hours revision authority remains independently READY. IMP-05 remains **PARTIALLY_CONFORMING** until all required children and the establishment-to-active completion proof conform.
