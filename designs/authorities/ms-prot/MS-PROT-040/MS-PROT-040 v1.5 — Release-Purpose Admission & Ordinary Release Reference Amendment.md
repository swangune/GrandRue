# MS-PROT-040 v1.5 — Release-Purpose Admission & Ordinary Release Reference Amendment

**Document ID:** MS-PROT-040  
**Version:** 1.5  
**Status:** **ACCEPTED by manual approval on 29 August 2026**  
**Approved:** Manual approval of Option 1 on 29 August 2026  
**Authority type:** Semantic-release administration / Merchant Configuration activation-admission architecture amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-040 v1.0 through v1.4 within exact release-purpose admission, the Ordinary New-Configuration Semantic Release Reference and activation-admission provenance  
**Supersedes:** No accepted authority outside the amended scope  
**Depends on:** MS-PROT-040 v1.0 + v1.1 + v1.2 + v1.3 + v1.4; MS-PROT-054; ADR-013; MS-IMPLEMENTATION-RULES-001  
**Closes:** `MS-PROT-040-V12-DQ-001`  
**Purpose:** Define the initial durable exact release-purpose admission authority, the separately versioned ordinary new-Configuration semantic-release reference, and the evidence which Configuration activation must consume and retain.

---

## 1. Governing Decision

The initial release-purpose control plane SHALL use:

```text
append-only exact release-purpose admission decisions
        +
one current decision pointer per exact release and purpose
        +
immutable ordinary-reference revisions
        +
one versioned singleton Ordinary New-Configuration reference pointer
```

The two governed purposes remain distinct:

```text
NEW_CONFIGURATION_VALIDATION
NEW_BUSINESS_ACTIVITY
```

Advancing the ordinary reference SHALL atomically prove that the selected immutable Semantic Registry Release is currently admitted for both purposes and SHALL retain the exact decision identities consumed by that advance.

Activation SHALL evaluate the release pinned by the Configuration Revision. It SHALL NOT require that release to equal the then-current ordinary reference. Activation SHALL retain the exact current admission decision identities it consumed.

Caller booleans, mutable aliases, deployment snapshots and unversioned “latest” references are not release-purpose admission authority.

---

## 2. Scope and Problem

MS-PROT-040 v1.2 established one exact ordinary new-Configuration release reference and separate purpose admission. MS-PROT-040 v1.4 established immutable serving evidence and the shared/exclusive generation fence. Neither authority selected the exact durable representation of current release-purpose admission.

Without that representation, activation could receive a caller assertion which cannot prove which decision was current, whether the two purposes were independently admitted, whether a later withdrawal crossed the activation transaction, or which historical authority an activation consumed.

This amendment closes that gap for the initial PostgreSQL modular-monolith topology.

---

## 3. Non-Goals

This amendment does not define the operator UI, transport or automation workflow for advancing the ordinary reference; staged rollout or multiple serving cohorts; merchant-selectable semantic-release channels; a universal retention duration; or a replacement for ADR-013 release materialisation or MS-PROT-040 v1.4 serving evidence.

`MS-PROT-040-V12-DQ-006` therefore remains deferred.

---

## 4. Canonical Concepts

### 4.1 Semantic Release-Purpose Admission Decision

An immutable platform-administration fact for one exact immutable Semantic Registry Release and exactly one governed purpose. Its disposition is `ADMITTED` or `WITHDRAWN`.

### 4.2 Current Release-Purpose Admission Pointer

Mutable currentness for one exact `(Semantic Registry Release, purpose)` pair. It points to one immutable decision with the same release and purpose. It does not itself assert admission.

### 4.3 Ordinary New-Configuration Semantic Release Reference Revision

An immutable platform-administration fact selecting one exact Semantic Registry Release for ordinary creation/validation of new Configuration Revisions. It retains the exact currently admitted decision identity for each governed purpose which justified the selection.

### 4.4 Ordinary New-Configuration Semantic Release Pointer

The singleton mutable pointer to the current immutable reference revision. Its monotonically advancing epoch establishes currentness and concurrency order; it is not admission authority.

---

## 5. Ownership and Negative Authority

Release-purpose admission decisions and ordinary-reference revisions are platform semantic-release administration facts. Merchant Configuration consumes them but does not own or manufacture them.

The following SHALL NOT establish release-purpose admission:

- a request boolean or enum asserting “admitted”;
- a Merchant Controller approval;
- a Serving Deployment Admission Snapshot;
- presence of compiled output or a packaged bundle alone;
- equality with the current ordinary reference;
- an environment variable, deployment label or mutable release alias; or
- a pre-transaction lookup not protected by the required database lock.

---

## 6. Required Durable Identity and Affinity

Each immutable admission decision SHALL retain at minimum:

```text
admission decision identifier
exact Semantic Registry Release identifier
exact governed purpose
ADMITTED | WITHDRAWN disposition
decision time
deciding platform principal identifier
decision provenance reference
```

Each current admission pointer SHALL be uniquely identified by exact release plus purpose and SHALL reference a decision having the same exact release and purpose.

Each immutable ordinary-reference revision SHALL retain at minimum:

```text
ordinary-reference revision identifier
exact selected Semantic Registry Release identifier
exact NEW_CONFIGURATION_VALIDATION admission decision identifier
exact NEW_BUSINESS_ACTIVITY admission decision identifier
selection time
selecting platform principal identifier
selection provenance reference
```

The singleton ordinary pointer SHALL retain its exact current revision identity and a monotonically increasing reference epoch. Identifiers SHALL be opaque stable identities. Mutable aliases and display names SHALL NOT substitute for them.

---

## 7. Admission-Decision Mutation Contract

Recording a release-purpose decision SHALL:

1. lock the current pointer for the exact release-purpose pair exclusively, creating its serialized currentness position when absent;
2. insert one immutable decision;
3. atomically move the current pointer to that exact decision; and
4. commit both changes together.

Reusing a decision identifier with the exact same immutable intent SHALL return the already-recorded fact. Reusing it with different release, purpose, disposition, principal or provenance SHALL fail as an identity conflict.

A later decision SHALL NOT mutate or delete an earlier decision. Withdrawal is a new immutable `WITHDRAWN` decision made current for that pair.

---

## 8. Ordinary-Reference Advancement Contract

Advancing the ordinary reference SHALL occur in one PostgreSQL transaction which:

1. takes the required exclusive lock on the singleton ordinary pointer;
2. locks the current admission pointers for the selected release and both governed purposes in deterministic purpose order;
3. resolves each pointer to its exact immutable current decision;
4. proves both decisions have disposition `ADMITTED` and exact release/purpose affinity;
5. inserts one immutable ordinary-reference revision retaining both exact decision identities; and
6. advances the singleton pointer and reference epoch atomically.

The same reference-revision identity and exact immutable intent is an idempotent retry. Changed intent under the same identity SHALL conflict. A stale expected reference epoch SHALL conflict.

No ordinary pointer mutation may commit when either purpose is absent, withdrawn, ambiguous or changes across the transaction.

---

## 9. Configuration-Revision Creation and Validation

Ordinary new-Configuration creation/validation SHALL resolve the singleton ordinary pointer to one immutable reference revision and consume its exact selected release.

The immutable reference revision is evidence of the decisions consumed when the ordinary pointer advanced; it does not freeze purpose admission forever. Any later validation or activation gate SHALL evaluate the then-current decision for the relevant exact release and purpose as required by its governing protocol.

An already-created Configuration Revision remains pinned to its exact Semantic Registry Release when the ordinary pointer later advances.

---

## 10. D5c4 Activation Admission Contract

Within the existing atomic Configuration activation transaction, activation SHALL:

1. use the Configuration Revision's pinned exact Semantic Registry Release;
2. acquire the shared PostgreSQL lock on the ordinary-cohort admission-control record defined by MS-PROT-040 v1.4;
3. require lifecycle `STABLE` and retain the exact stable generation identifier and generation epoch;
4. acquire shared locks on the pinned release's two current release-purpose pointers in deterministic purpose order;
5. require both exact current decisions to be present, affined to the pinned release and purpose, and `ADMITTED`;
6. resolve the exact immutable validation/package evidence and deterministic Configuration New-Activity Execution Requirement Set for the Configuration Revision and pinned release;
7. prove that the stable Serving Deployment Admission Snapshot materialises the pinned release at one exact bundle digest and covers every exact required path/release/contract tuple;
8. retain the exact admission decision identifiers, resolved-package evidence identity, requirement-set identity, serving snapshot/generation identity, generation epoch and materialised bundle digest; and
9. atomically commit that provenance with the activation evidence, Merchant current-Configuration pointer mutation and publication intent/outbox work already required by composite MS-PROT-040.

The shared locks SHALL remain held until the activation transaction commits or rolls back. Release-purpose decision currentness uses conflicting exclusive locks. Serving promotion uses the conflicting exclusive ordinary-cohort control-row lock. Therefore neither current admission nor serving generation may cross a successful activation commit.

The activation transaction SHALL NOT make an external serving switch and SHALL NOT wait across one.

---

## 11. Pinned-Release Rule

Activation SHALL NOT compare the revision's pinned release with the current Ordinary New-Configuration Semantic Release Pointer.

Consequently:

```text
ordinary pointer advances from release A to release B
        +
existing revision remains pinned to release A
        +
release A remains currently admitted for both purposes
        +
stable serving evidence covers release A and its exact requirements
        →
existing revision may activate
```

If release A is later withdrawn for either purpose, that activation SHALL fail closed even if its earlier ordinary-reference revision recorded admitted decisions.

---

## 12. Retry and Historical Evidence

An exact retry of a committed activation SHALL return the committed activation evidence and its retained admission/serving provenance. It SHALL NOT re-evaluate newer admission decisions, a newer ordinary reference or a newer serving generation.

Historical admission decisions, ordinary-reference revisions and activation provenance SHALL remain immutable. Current pointers may advance without rewriting historical facts.

Pre-v1.5 activation rows may remain identifiable as legacy evidence without fabricated admission provenance. Every activation committed through the v1.5 contract SHALL retain the complete v1.5 evidence set.

---

## 13. Failure Semantics

Activation SHALL fail closed with the accepted MS-PROT-040 v1.2 rejection distinctions where applicable:

- `SEMANTIC_RELEASE_NOT_ADMITTED_FOR_NEW_CONFIGURATION` when the pinned release has no current admitted validation-purpose decision;
- `SEMANTIC_RELEASE_NOT_ADMITTED_FOR_NEW_BUSINESS_ACTIVITY` when it has no current admitted new-business-activity decision;
- `SEMANTIC_RELEASE_NOT_MATERIALISED_BY_SERVING_DEPLOYMENT` when the stable snapshot does not materialise the pinned release exactly;
- `EXECUTABLE_SUPPORT_INCOMPLETE` when any exact required tuple is uncovered;
- `DEPLOYMENT_ADMISSION_CONFLICT` when the ordinary cohort is `PROMOTING`, missing, epoch-inconsistent or otherwise uncertain; and
- `PACKAGE_MISMATCH` or the existing exact validation/approval rejection when required revision/release/package evidence is missing, ambiguous or inconsistent.

Absence, ambiguity, malformed affinity and uncertain state are rejection, never implicit admission.

---

## 14. Security and Audit Invariants

The platform principal which records an admission decision or advances the ordinary reference SHALL be authenticated and authorized by the applicable platform-administration policy. Merchant roles do not gain this authority through Configuration permissions.

Audit reconstruction SHALL be able to answer which exact decisions were current for each purpose, which decisions justified an ordinary-reference revision, which exact decisions and serving generation an activation consumed, which principal established each fact, and whether a retry returned an existing fact rather than manufacturing a new one.

---

## 15. Falsification Cases

Conformance evidence SHALL prove at least:

1. separately admitted purposes are both required;
2. a current `WITHDRAWN` decision rejects even when an older admitted decision exists;
3. a pointer cannot reference a decision for another release or purpose;
4. ordinary-reference advancement is atomic and fails when either purpose is not currently admitted;
5. a stale reference epoch and changed-intent identity reuse conflict;
6. activation uses its pinned release after the ordinary pointer advances;
7. activation retains both exact current decision identities;
8. activation cannot commit while the serving control row is `PROMOTING`;
9. admission-currentness mutation and serving promotion cannot cross the activation transaction;
10. missing materialisation, digest or requirement coverage fails with the correct distinction;
11. exact activation retry returns historical committed provenance without re-evaluation; and
12. caller booleans, snapshots or mutable aliases alone cannot establish admission.

---

## 16. Rejected Alternatives

### 16.1 Singleton release row with mutable purpose booleans

Rejected because it collapses two independent authorities, destroys decision history and cannot identify the exact decision an activation consumed.

### 16.2 Treat the serving snapshot as release admission

Rejected because technical materialisation/support evidence does not own semantic-release purpose admission.

### 16.3 Require activation release equality with the ordinary pointer

Rejected because it would strand valid already-created revisions when the creation/validation default advances, contrary to MS-PROT-040 v1.2.

### 16.4 Pass admission from the caller

Rejected because a caller assertion cannot establish durable currentness, lock affinity, retry reconstruction or auditable identity.

---

## 17. Trade-Offs

The accepted model introduces more rows and lock operations than a mutable singleton. In return it preserves independent purpose authority, exact historical reconstruction, deterministic retries and transactionally enforceable concurrency.

The ordinary reference retains the decisions consumed at advancement while activation intentionally re-evaluates current admission for its pinned release. This duplicates identifiers by design: one set explains default-release selection, the other explains the later activation decision.

---

## 18. Readiness and Amendment Effect

This amendment resolves `MS-PROT-040-V12-DQ-001` and makes D5c4 implementation READY under `MS-IMP-001.md`.

D5c4 completion requires PostgreSQL-backed RED-to-GREEN evidence for the contracts above, implementation-evidence and dependency-graph updates, and canonical verification under `IMPLEMENTATION-RULES.md`.

`MS-PROT-040-V12-DQ-006` remains deferred until an operator/automation workflow for routine production advancement is required.

---

## 19. Acceptance Statement

Manual approval of Option 1 on 29 August 2026 accepted this exact model: append-only release-purpose decisions, current per-release/purpose pointers, immutable ordinary-reference revisions, a separate singleton versioned ordinary pointer, atomic dual-purpose proof on pointer advancement, pinned-release activation evaluation and retention of exact current decision identities.

This document records that approved decision through the governed design-document process. It does not infer authority from implementation convenience.
