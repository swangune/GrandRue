# IMP-05 — Ordinary First-Activation Exact Approval Admission

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** D5b — Ordinary first-activation exact admission integration
**Date:** 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements the ordinary first-activation approval predicate required by:

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`;
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`;
- `designs/MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment.md`;
- `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`;
- `designs/MS-PROT-069 v1.1 — Idempotency, Retry & Concurrency Safety Amendment.md`;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

The accepted rule is that an ordinary first activation must consume a then-current applicable D3 approval for the exact immutable revision, semantic release, successful validation/package evidence and business-facing impact-review evidence. The earlier general approval abstraction is insufficient for this path.

## 2. Implemented production boundary

`JooqConfigurationReleaseActivation` now:

- distinguishes ordinary first activation from replacement activation after locking the merchant's current activation pointer;
- resolves first-activation approval through the narrow `InitialConfigurationRevisionApprovalApplicabilityAuthority` view implemented by the durable D3 authority;
- rejects first activation when only the earlier weak general approval exists;
- validates merchant, configuration-revision and semantic-release affinity against the exact release candidate;
- consumes D3's current-applicability result, which is already bound by durable foreign keys and transaction checks to the exact validation, package and impact-review evidence;
- shares the Merchant Controller transfer advisory fence while resolving applicability, so Controller transfer cannot create a stale-authority commit interval; and
- preserves the existing general approval behavior for later replacement activations.

Backward-compatible constructors fail closed for ordinary first activation when no exact-applicability authority is supplied.

## 3. RED → GREEN evidence

RED added a PostgreSQL contract proving that a weak `ConfigurationRevisionApprovalAuthority` result alone must not admit an ordinary first activation. It failed with `expected APPROVAL_REQUIRED but was SUCCESS`.

GREEN introduced the narrow applicability read port, made the durable D3 authority implement it, integrated it inside the activation transaction and retained the Controller-transfer fence until activation commits.

Focused PostgreSQL verification covered 27 D3/D5 contracts and passed, including:

- weak general approval rejection for first activation;
- successful activation using a persisted exact D3 approval;
- exact semantic-release mismatch rejection;
- Controller-transfer invalidation followed by successful approval from the new current Controller;
- concurrent first-activation serialization;
- later-release compatibility admission; and
- activation outbox atomicity and recovery behavior.

## 4. Canonical verification

The isolated canonical gate ran only after confirming no other MainStreet Maven/Java process was using the shared target directory or PostgreSQL schema:

```text
mvn --batch-mode -Ppostgres-it clean verify

Java release target:                   25
Flyway migrations validated:           40
production Java sources compiled:      660
test Java sources compiled:            234
unit / conformance tests:              662 PASS
PostgreSQL integration tests:          241 PASS
total Maven tests:                     903 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

## 5. Explicit non-claims and next gate

D5b does not claim implementation of the complete serving-deployment admission invariant in MS-PROT-040 v1.2. The next critical activation child requires a durable Serving Deployment Admission Snapshot, exact executable-support evidence, a canonical Configuration New-Activity Execution Requirement Set identity and a cross-boundary activation/deployment generation fence.

Those choices are still `DEFERRED — INACTIVE` in the canonical DDR under `MS-PROT-040-V12-DQ-002` through `MS-PROT-040-V12-DQ-005`. Their revisit conditions are now met. Production work on that child must pause for governed resolution and manual approval; no storage, digest or cross-control-plane fencing design has been invented here.
