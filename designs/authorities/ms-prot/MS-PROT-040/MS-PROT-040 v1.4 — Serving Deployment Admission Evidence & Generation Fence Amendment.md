# MS-PROT-040 v1.4 — Serving Deployment Admission Evidence & Generation Fence Amendment

**Document ID:** MS-PROT-040
**Version:** 1.4
**Status:** **ACCEPTED by manual approval on 29 August 2026**
**Approved:** Manual approval on 29 August 2026
**Authority type:** Merchant Configuration activation / serving-deployment admission architecture amendment
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`
**Amends:** MS-PROT-040 v1.0 through v1.3 within serving-deployment admission evidence, Configuration New-Activity Execution Requirement Set identity and activation/deployment generation fencing scope
**Supersedes:** No accepted authority outside the amended scope
**Depends on:** MS-PROT-040 v1.0 + v1.1 + v1.2 + v1.3; MS-PROT-054; MS-PROT-069 v1.1; ADR-012; ADR-013; MS-IMPLEMENTATION-RULES-001
**Closes:** `MS-PROT-040-V12-DQ-002`, `MS-PROT-040-V12-DQ-003`, `MS-PROT-040-V12-DQ-004`, `MS-PROT-040-V12-DQ-005`
**Purpose:** Define the initial PostgreSQL representation of immutable ordinary Serving Deployment Admission Snapshots, exact contract-scoped executable-support evidence, deterministic requirement-set identity and the durable bidirectional generation fence required by Configuration activation and serving-generation promotion.

---

## 1. Governing Decision

The initial ordinary serving topology SHALL use:

```text
immutable PostgreSQL Serving Deployment Admission Snapshots
        +
normalized exact materialisation and executable-support evidence
        +
deterministic versioned Configuration requirement-set identity
        +
one durable ordinary-cohort admission-control record
        +
two-phase fail-closed serving-generation promotion
```

Configuration activation and serving-generation promotion SHALL coordinate through the same durable ordinary-cohort control record. A mutable boolean, an opaque caller assertion, a release-wide `supported` flag, an advisory lock without durable state or an unobservable control-plane switch SHALL NOT establish serving-deployment admission.

The initial implementation remains a modular monolith with one homogeneous ordinary serving cohort. This amendment does not authorize distributed services, multi-cohort routing or provider-health admission.

---

## 2. Problem and Governed Scope

MS-PROT-040 v1.2 requires activation to prove exact semantic materialisation, exact contract-scoped executable support and a current serving-generation fence. It also requires serving-generation promotion to prove that no currently active Configuration would be stranded.

A precheck alone permits this invalid interval:

```text
activation validates generation G1
        ↓
serving control plane changes to unsupported G2
        ↓
activation commits against stale G1 evidence
```

Likewise, mutable or release-wide support assertions cannot establish which exact contracts, participant effects or semantic bundles one generation can execute.

This amendment governs the missing durable technical evidence and concurrency mechanism. It does not change Merchant Configuration ownership, Semantic Registry meaning, deployment ownership or the activation predicates already accepted by MS-PROT-040 v1.2.

---

## 3. Explicit Non-Goals

This amendment does not define:

```text
public or operator API representation
deployment provider or orchestration vendor
routine release-promotion operator workflow
multi-region or multi-cohort routing
compatibility-aware cohort partitioning
provider health/readiness
Commercial Entitlement
actor authorisation or strong-authentication policy
retention duration
observability dashboard design
remote semantic-registry service topology
```

`MS-PROT-040-V12-DQ-001` remains unresolved for the exact durable Ordinary New-Configuration Semantic Release Reference and its production release-administration representation. Its revisit condition is now active because activation must consume current release-purpose admission, but this amendment does not invent that separate release-administration contract.

`MS-PROT-040-V12-DQ-006`, `DQ-007`, `DQ-009`, `DQ-011` and `DQ-012` remain deferred under their existing revisit conditions.

---

## 4. Canonical Terminology

### 4.1 Ordinary Serving Cohort

The single homogeneous deployment cohort serving ordinary merchant traffic in the initial production topology.

### 4.2 Serving Deployment Admission Snapshot

An immutable technical evidence object for one exact ordinary serving generation. It identifies the exact packaged semantic releases materialised by that generation and the exact semantic execution contracts supported by each implementation path.

### 4.3 Ordinary Serving Admission Control Record

The single durable mutable coordination record that identifies the stable ordinary generation or an in-progress transition between two generations. It is concurrency authority for activation/promotion coordination only; it is not semantic or Configuration authority.

### 4.4 Configuration New-Activity Execution Requirement Set

The deterministic set of exact ADR-012 execution requirements derived from one exact RCP for new activity. Each requirement identifies one affected execution contract and zero or more required participant/effect contracts.

### 4.5 Promotion Transition

The durable two-phase change from one stable ordinary serving generation to another. A transition is either in progress, reconciled to the target, or safely aborted while the prior generation remains authoritative.

---

## 5. Ownership

The deployment/admission subsystem owns:

```text
Serving Deployment Admission Snapshot evidence
ordinary serving-generation control state
promotion-transition identity and technical reconciliation
```

Merchant Configuration owns:

```text
Configuration Revision and activation history
RCP-affined requirement-set evidence
the decision whether activation predicates commit
```

The Semantic Registry remains authoritative for release and execution-contract meaning. ADR-012 remains authoritative for executable-support meaning. ADR-013 remains authoritative for packaged semantic materialisation. The deployment subsystem records technical evidence about those authorities; it does not redefine them.

---

## 6. Snapshot Identity, Cardinality and Immutability

Each snapshot SHALL have exactly one stable non-blank `generationIdentifier` and exactly one `ORDINARY` cohort identity in the initial topology.

One generation has:

```text
one immutable snapshot header
one or more exact materialised-release evidence rows
zero or more implementation-path support manifests
zero or more exact supported-contract rows per manifest
```

The snapshot header and child evidence are append-only. Update or deletion is prohibited through the owning authority. Corrections or support changes require a new generation identity and a new immutable snapshot.

The current ordinary generation is not encoded by mutating a snapshot. Currentness belongs only to the separate admission-control record.

---

## 7. PostgreSQL Snapshot Representation

The initial durable representation SHALL use normalized PostgreSQL relations with foreign-key and uniqueness constraints equivalent to:

```text
serving_deployment_admission_snapshot
    generation_identifier PRIMARY KEY
    cohort_identifier = ORDINARY
    evidence_recorded_at

serving_deployment_materialised_release
    generation_identifier FOREIGN KEY
    semantic_registry_release_identifier
    packaged_bundle_content_digest
    PRIMARY KEY (generation_identifier,
                 semantic_registry_release_identifier)

serving_deployment_executable_support_manifest
    generation_identifier FOREIGN KEY
    implementation_path_identifier
    PRIMARY KEY (generation_identifier,
                 implementation_path_identifier)

serving_deployment_executable_support_contract
    generation_identifier
    implementation_path_identifier
    semantic_registry_release_identifier
    contract_identifier
    FOREIGN KEY to exact manifest
    PRIMARY KEY (generation_identifier,
                 implementation_path_identifier,
                 semantic_registry_release_identifier,
                 contract_identifier)
```

Names may be adjusted mechanically to repository naming rules, but the identities, normalization, immutability and exact affinities SHALL remain.

The materialised-release digest is the exact ADR-013 packaged-bundle content digest. A release identifier without its bundle digest is insufficient materialisation evidence.

---

## 8. Executable-Support Evidence Encoding

Executable support SHALL be encoded as exact normalized tuples:

```text
(generation identifier,
 implementation-path identifier,
 semantic-release identifier,
 execution-contract identifier)
```

The tuple set is the durable encoding of the existing `ExecutableSupportManifest` meaning. Coverage succeeds only where at least one exact implementation path contains every contract required by one exact `ExecutableSupportRequirement`.

The following are prohibited as sufficient evidence:

```text
supported = true
release supported = true
operation handler exists
opaque caller-supplied JSON
latest manifest
current runtime registration without snapshot affinity
```

Any change to materialised releases, bundle digests, implementation paths or supported contracts produces a different generation snapshot.

---

## 9. Deterministic Requirement-Set Derivation

The Configuration owner SHALL derive the Configuration New-Activity Execution Requirement Set from:

```text
exact immutable RCP
        +
exact RCP semantic-release affinity
        +
release-affined Semantic Registry execution-contract mappings
```

The resolver SHALL include every statically applicable new-activity operation and every required participant, operation-effect, relationship/effect or other ADR-012 execution contract required by that operation.

It SHALL exclude live actor, entitlement, provider, inventory, booking, session, projection, Exposure and other contextual facts listed by MS-PROT-040 v1.2.

Callers SHALL NOT supply the resolved set or claim that it is complete.

---

## 10. Requirement-Set Durable Representation

One exact requirement-set evidence object SHALL retain:

```text
merchant identifier
Configuration Revision identifier
Semantic Registry Release identifier
resolved-package evidence identifier
canonicalization version
requirement-set digest
ordered/reconstructable exact requirements
```

Each exact requirement SHALL retain one affected `SemanticExecutionContractReference` and the complete set of required participant/effect contract references. Storage SHALL preserve grouping; flattening all contracts into one unstructured release-wide set is insufficient.

The evidence is immutable and exact-affined to the D2b resolved-package evidence. A different RCP, semantic release, resolved mapping or requirement content requires a different evidence identity.

---

## 11. Canonical Requirement-Set Identity

The canonical identity SHALL be:

```text
ms-reqset-v1:sha256:<lowercase hexadecimal SHA-256 digest>
```

The SHA-256 input SHALL be a versioned, length-prefixed UTF-8 encoding with these rules:

1. the domain/version string `mainstreet.configuration-new-activity-requirement-set/v1` is encoded first;
2. requirements are sorted by affected contract `(semanticReleaseIdentifier, contractIdentifier)` using unsigned UTF-8 byte order;
3. within each requirement, participant/effect contracts are deduplicated and sorted by the same tuple order;
4. every string is encoded as a four-byte unsigned big-endian byte length followed by its exact UTF-8 bytes;
5. each requirement encodes the affected release and contract, the four-byte count of participant/effect contracts, then each participant/effect release and contract;
6. no whitespace, locale, platform line ending, map iteration order, JSON serializer or database row order contributes to the digest.

The canonicalization version SHALL be stored separately as `1` as well as being present in the identity prefix. Unknown versions fail closed.

The digest is deterministic content identity, not authenticity or semantic authority.

---

## 12. Ordinary Admission-Control Record

The initial topology SHALL have exactly one PostgreSQL admission-control record for the `ORDINARY` cohort with:

```text
cohort identifier PRIMARY KEY
control lifecycle: STABLE | PROMOTING
monotonic epoch
current stable generation identifier
target generation identifier when PROMOTING
logical promotion-transition identifier when PROMOTING
last transition instant
```

`STABLE` requires one current generation and no target/transition identity. `PROMOTING` requires both the prior current generation and one target generation plus one logical transition identity.

The control record is mutable coordination state. The referenced snapshots remain immutable.

---

## 13. Activation-Side Fence

Before evaluating serving-deployment admission, Configuration activation SHALL acquire a PostgreSQL shared row lock on the ordinary admission-control record and hold it until the activation transaction commits or rolls back.

Activation proceeds only if:

```text
control lifecycle = STABLE
        +
current generation snapshot exists
        +
candidate semantic release and bundle digest are materialised
        +
snapshot support covers every exact requirement
        +
the locked generation/epoch remains the committed control state
```

If the control lifecycle is `PROMOTING`, activation rejects with `DEPLOYMENT_ADMISSION_CONFLICT`. It does not wait for external deployment work while holding an application transaction open.

The activation SHALL persist the admitted generation identifier, epoch, snapshot reference, semantic-release identity and exact requirement-set identity with activation evidence.

---

## 14. Promotion Prepare Operation

Promotion preparation SHALL:

1. identify one logical transition request and one immutable target snapshot;
2. acquire an exclusive PostgreSQL row lock on the ordinary admission-control record;
3. require the record to be `STABLE` unless resolving an exact retry;
4. prove that the target snapshot materialises and supports every currently active Configuration whose semantic context remains required for ordinary new activity;
5. increment the monotonic epoch;
6. commit `PROMOTING` with prior current generation, target generation and transition identity; and
7. perform no external serving switch inside that database transaction.

The exclusive lock conflicts with activation's shared lock. Therefore every activation either commits before the promotion scan and is included, or observes `PROMOTING` and cannot commit.

---

## 15. External Switch and Reconciliation

After preparation commits, the deployment adapter MAY switch the homogeneous ordinary serving cohort from the prior generation to the target generation.

During `PROMOTING`:

```text
new Configuration activation = fail closed
existing activity = remains supported by both admitted endpoints
```

The prior generation already supports all active Configurations. Promotion preparation proves the target generation also supports them. A rolling interval containing both generations therefore does not strand existing active Configuration.

After the external operation, reconciliation SHALL obtain trusted observed serving-generation evidence.

If observation proves the target generation is serving, finalization acquires the exclusive control-row lock and commits `STABLE` with the target as current.

If observation proves the prior generation remains serving, an exact safe abort MAY restore `STABLE` with the prior generation.

If the external outcome is unknown, mixed beyond the governed homogeneous transition, or contradicts both permitted outcomes, the record remains `PROMOTING`; activation remains fail closed until reconciliation establishes a safe state.

---

## 16. Promotion Retry and Identity

The same logical promotion-transition identifier with identical prior generation, target generation and intent SHALL return or continue the existing transition outcome.

Reuse of one transition identifier for different intent is a conflict.

A lost acknowledgement SHALL NOT create a second promotion transition. Recovery SHALL reconcile the existing durable transition with trusted observed control-plane state before any new transition begins.

---

## 17. No Long Cross-Control-Plane Database Transaction

Main Street SHALL NOT keep one database transaction open while waiting for an external deployment or routing operation.

The durable `PROMOTING` phase is the fence across that boundary. This accepts a temporary fail-closed activation interval in exchange for recoverable state, bounded database locks and no unsupported committed Configuration interval.

---

## 18. Activation and Promotion Atomicity

Activation's configuration evidence, current pointer, publication intent and serving-admission provenance SHALL commit atomically in the existing activation transaction.

Promotion preparation's target coverage proof and transition to `PROMOTING` SHALL commit atomically under the exclusive control-row lock.

Promotion finalization or safe abort SHALL commit atomically under that same lock after trusted external-state reconciliation.

No event, cache, in-memory mutex or asynchronous observer may replace these atomic boundaries.

---

## 19. Release-Purpose Admission Boundary

Snapshot materialisation and executable support do not prove that a Semantic Registry Release remains permitted for:

```text
NEW_CONFIGURATION_VALIDATION
NEW_BUSINESS_ACTIVITY
```

Activation SHALL consume current release-purpose admission authority separately. D5 serving-deployment work may build snapshot, support and fencing foundations before the durable release-administration representation is resolved, but final activation integration remains blocked by `MS-PROT-040-V12-DQ-001`.

---

## 20. Failure and Rejection Semantics

At minimum, callers or application orchestration SHALL be able to distinguish:

| Condition | Classification |
|---|---|
| exact release no longer permitted for required purpose | semantic-release admission rejection |
| stable serving snapshot absent | deployment admission rejection |
| exact release/digest not materialised | semantic materialisation rejection |
| one or more exact requirements uncovered | executable-support rejection |
| control lifecycle is `PROMOTING` or epoch changed | deployment admission conflict |
| promotion request identity reused for different intent | request-identity conflict |
| external serving state uncertain | technical failure requiring reconciliation |

Transport status codes and public error payloads remain governed separately.

---

## 21. Historical Evidence

Successful activation evidence SHALL retain enough exact references to reconstruct:

```text
which Configuration Revision and release activated
which exact RCP/package and requirement set were admitted
which immutable serving-generation snapshot was evaluated
which generation epoch was fenced
which principal initiated activation
when activation committed
```

Later snapshot, manifest, release-admission or control-state changes SHALL NOT rewrite that evidence.

---

## 22. Security and Negative Authority

Snapshot registration and promotion are platform deployment-administration operations. Merchant, onboarding, AI, Configuration callers and external transport SHALL NOT assert snapshot content, current generation, observed serving state or promotion completion.

Trusted adapters may supply technical evidence only through the owning deployment authority. Authentication and authorisation for operator-facing transport remain separately governed and MUST be enforced before production exposure.

---

## 23. Falsification Evidence

| Scenario | Required result |
|---|---|
| Release bundle is present but one participant contract is absent | Activation rejects executable support. |
| Caller supplies `supported=true` | Assertion has no authority. |
| Manifest changes under the same generation identity | Mutation is rejected; create a new generation. |
| Activation holds the shared fence when promotion prepares | Promotion waits, then includes the committed activation in its coverage scan. |
| Promotion prepares first | Activation observes `PROMOTING` and rejects without committing. |
| External switch succeeds but acknowledgement is lost | Transition remains durable; reconciliation finalizes the same transition. |
| External outcome is unknown | Activation remains fail closed; no guessed stable generation. |
| Promotion crashes before external switch | Prior generation continues serving; safe abort requires observed evidence. |
| Promotion crashes during rolling switch | Both generations have proved support for all pre-transition active Configurations; new activation remains blocked. |
| Requirement rows arrive in different order | Canonical identity is unchanged. |
| One participant/effect contract changes | Canonical identity changes. |
| Current release-purpose admission is absent | Snapshot evidence cannot substitute; final activation rejects. |

---

## 24. Alternatives Rejected

### 24.1 Advisory lock only

Rejected because it leaves no durable transition state for crash recovery or uncertain external outcome.

### 24.2 One database transaction held across deployment

Rejected because external deployment duration and failure are unbounded relative to a safe database transaction.

### 24.3 Mutable current snapshot

Rejected because historical activation evidence could silently resolve to changed materialisation or support content.

### 24.4 Opaque JSON evidence

Rejected as the authoritative representation because exact tuple affinity, uniqueness and coverage would depend on serializer and caller behavior rather than relational constraints.

### 24.5 Release-wide support flag

Rejected by MS-PROT-040 v1.2 and ADR-012 because one unsupported participant/effect contract can invalidate the candidate.

### 24.6 Distributed deployment-admission service

Deferred because the initial modular monolith and homogeneous cohort do not justify distributed-system complexity.

---

## 25. Trade-Offs

The accepted design adds normalized evidence rows, a global ordinary-cohort coordination record and a temporary fail-closed activation interval during deployment promotion.

In return, it provides exact auditability, deterministic coverage, crash-recoverable promotion state and a proof that activation and promotion cannot cross into an unsupported committed interval.

The single ordinary-cohort control record may become a throughput boundary. That is accepted for the initial homogeneous topology. Evidence of material contention or a need for independent serving cohorts is the condition for revisiting partitioning under `MS-PROT-040-V12-DQ-007` and ADR-013 cohort authority.

---

## 26. Implementation Sequence

Implementation SHALL proceed through independently testable children:

```text
D5c1 deterministic RCP-affined requirement-set evidence
        ↓
D5c2 immutable serving-generation snapshot/support evidence
        ↓
D5c3 bidirectional promotion state machine and generation fence
        ↓
D5c4 activation-side serving admission integration
```

D5c4 remains blocked until the current release-purpose admission authority required by Section 19 is implementation-ready.

---

## 27. Conformance Criteria

Implementation conforms only if tests establish at least:

```text
[ ] snapshot and all child evidence are immutable
[ ] exact bundle digest is retained per materialised release
[ ] support is exact path/release/contract evidence rather than a boolean
[ ] requirement derivation consumes the exact RCP and release-affined mappings
[ ] canonical requirement identity is stable across input ordering
[ ] any material requirement change changes the identity
[ ] activation and promotion use conflicting locks on one durable control row
[ ] activation cannot commit while PROMOTING
[ ] promotion target covers every active Configuration before preparation commits
[ ] activation committed before preparation is included in promotion coverage
[ ] external uncertainty leaves the system fail closed and recoverable
[ ] exact retry continues one transition; changed intent conflicts
[ ] activation retains exact snapshot/generation/epoch/requirement provenance
[ ] release-purpose admission remains separate and mandatory
[ ] canonical PostgreSQL-backed verification proves the durable constraints
```

---

## 28. Amendment Effect

This amendment resolves the exact implementation-architecture choices retained by `MS-PROT-040-V12-DQ-002` through `DQ-005`.

MS-PROT-040 v1.0 remains authoritative for the general Configuration lifecycle. v1.1 remains authoritative for immutable revision/RCP affinity and atomic activation. v1.2 remains authoritative for complete ordinary first-activation predicates and the bidirectional serving-support invariant. v1.3 remains authoritative for durable exact validation, package, impact-review and approval evidence.

This amendment supplies the durable evidence representation and cross-boundary fencing mechanism needed to implement those existing rules. It does not weaken or replace any prior predicate.

---

## 29. Acceptance Statement

MS-PROT-040 v1.4 is accepted when Main Street represents serving-generation admission as immutable normalized PostgreSQL evidence, derives one versioned deterministic requirement-set identity from the exact RCP, and coordinates activation and deployment promotion through a durable two-phase ordinary-cohort fence that fails closed under concurrency, crash and uncertain external outcome.

> **No Configuration becomes active from a boolean or stale deployment precheck, and no serving generation becomes ordinary-current by stranding an active Configuration. Exact immutable evidence and one durable recoverable fence protect both directions.**

---

## Governance Assessment

```text
DESIGN / PROPOSE: COMPLETE
AUTHORITY TRACE: COMPLETE
OWNERSHIP REVIEW: PASS
EVIDENCE-IDENTITY REVIEW: PASS
RETRY / CRASH / CONCURRENCY REVIEW: PASS
CONTROL-PLANE UNCERTAINTY FALSIFICATION: PASS
AMBIGUITY REVIEW: PASS within the approved amendment scope
IMPLEMENTATION READINESS: READY for D5c1 through D5c3; D5c4 remains gated by DQ-001
MANUAL APPROVAL: GRANTED on 29 August 2026
STATUS: ACCEPTED
```
