# IMP-05 — Initial Configuration Revision Approval Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** D3 — Current-Controller exact approval authority / persistence
**Date:** 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements the ordinary first-configuration approval boundary required by:

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`;
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`;
- `designs/MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment.md`;
- `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`;
- `designs/MS-PROT-069 v1.1 — Idempotency, Retry & Concurrency Safety Amendment.md`;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

D3 records approval only after resolving the exact immutable first Configuration Revision, its exact successful validation/package evidence, its exact business-facing impact-review evidence, the absence of a blocking impact and the current Merchant Controller inside one transaction. Caller booleans and pre-revision onboarding review cannot establish this authority.

## 2. Implemented production boundary

The implementation provides:

- a Configuration-owned command and authority for ordinary first-revision approval;
- a required authenticated `TrustedExecutionContext` whose merchant and principal match the command;
- exact merchant, revision, semantic-release, validation, package and impact-review affinity;
- observable rejection categories for absent or mismatched revision, validation, package and impact evidence, blocking findings, missing current Controller, wrong principal, request-identity conflict and concurrency conflict;
- current-Controller resolution and row protection inside the approval transaction;
- reuse of the Merchant Account Controller-transfer advisory lock, preventing approval from committing across a concurrent transfer boundary;
- append-only approval history retaining exact evidence identities, approving principal, Controller relationship and approval instant;
- exact logical-request retry and at-most-one duplicate commit under concurrency;
- current-applicability resolution that requires the recorded Controller relationship and principal still to be active; and
- no activation, publication or serving-deployment side effect.

The authority reads the separate D1, D2b, D2c and Merchant Controller relations inside the owning database transaction. This permitted physical co-location preserves their distinct identities, predicates and ownership while avoiding a non-atomic caller assertion between authorities.

## 3. Durable representation

Flyway migration `V40__configuration__create_initial_revision_approval.sql`:

- adds a composite impact-evidence affinity key across merchant, impact identity, revision, release, validation and package;
- creates append-only `configuration_revision_approval` facts keyed by logical approval request identity;
- binds approval by composite foreign keys to the exact revision, validation/package tuple and impact-review tuple;
- binds approval to the exact Merchant Controller relationship that was current at commit;
- retains the approving principal and approval instant; and
- constrains one logical fact for the exact merchant/revision/validation/impact/principal combination while allowing a later current Controller to approve the same unchanged evidence.

## 4. Conformance evidence

| Required behavior | Executable evidence | Result |
|---|---|---|
| Current authenticated Controller approves the exact revision and evidence | `JooqInitialConfigurationRevisionApprovalAuthorityIT` | PASS |
| Missing validation or impact evidence is rejected observably | `JooqInitialConfigurationRevisionApprovalAuthorityIT` | PASS |
| Unauthenticated and non-Controller principals are rejected | `JooqInitialConfigurationRevisionApprovalAuthorityIT` | PASS |
| A blocking impact prevents approval and creates no fact | `JooqInitialConfigurationRevisionApprovalAuthorityIT` | PASS |
| Exact retry returns the committed fact; changed intent conflicts | `JooqInitialConfigurationRevisionApprovalAuthorityIT` | PASS |
| Controller transfer preserves history, removes old applicability and permits current-Controller approval | `JooqInitialConfigurationRevisionApprovalAuthorityIT` | PASS |
| Concurrent duplicate delivery commits one logical fact | `JooqInitialConfigurationRevisionApprovalAuthorityIT` | PASS |
| Fresh database migration chain applies and validates V1 through V40 | `FlywayMigrationChainIT` | PASS |

The package-affinity rejection category is defensive at the approval boundary. Valid D2b/D2c storage makes malformed package affinity unrepresentable through composite foreign keys; D2b and D2c integration contracts separately prove that mismatched package evidence cannot be recorded.

## 5. IMPLEMENTATION-RULES trace

The RED step added the PostgreSQL D3 contract before production symbols existed. Focused compilation failed on 17 missing approval command, fact, authority and categorized-persistence symbols. The GREEN step added only those semantic contracts, the jOOQ transaction authority and V40.

The seven focused D3 contracts passed against PostgreSQL 18.6. The affected D1, D2b, D2c and onboarding suites then passed together (39 PASS). The first broader run correctly exposed four older cleanup fixtures that did not yet truncate the new dependent approval table; after those fixtures were updated, all affected tests passed.

Two later canonical attempts were invalidated by a separate overlapping canonical process using the same `target` directory and PostgreSQL schema. The competing implementation heartbeat was paused, the already-running process was allowed to exit, and the canonical gate was repeated in isolation. The isolated final result was:

```text
mvn --batch-mode -Ppostgres-it clean verify

Java release target:                   25
Flyway migrations validated:           40
production Java sources compiled:      659
test Java sources compiled:            233
unit / conformance tests:              662 PASS
PostgreSQL integration tests:          237 PASS
total Maven tests:                     899 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

## 6. Explicit non-claims

D3 does not claim completion of:

- transport, HTTP or UI approval surfaces;
- approval by non-Controller roles or later change-set policy;
- integration of this exact applicable approval into ordinary first-activation admission;
- activation, publication or serving-deployment state changes;
- later Configuration Revision/change-set mutation; or
- end-to-end IMP-05 completion.

The existing general activation foundation still consumes the earlier, weaker `ConfigurationRevisionApprovalAuthority`. D3 deliberately does not promote the revision or alter that path. Replacing that admission dependency with the exact current-applicable D3 authority is the next implementation node.

## 7. Graph consequence

D1, D2a, D2b, D2c and D3 are **CONFORMING_COMPLETE**. Existing publication and activation persistence remain conforming foundations. D5 is refined into D5a activation persistence foundation (**COMPLETE**) and D5b ordinary first-activation exact admission integration (**READY**). IMP-05 remains **PARTIALLY_CONFORMING** until the exact approval is consumed at first activation and the required establishment-to-active proof conforms.
