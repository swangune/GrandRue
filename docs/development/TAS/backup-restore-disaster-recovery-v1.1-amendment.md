# MS-TAS-RECOVERY-001 v1.1 — Exact Release, Durable Reaction & Post-Restore Authority Reconciliation Amendment

**Document ID:** MS-TAS-RECOVERY-001  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** Manual approval on 28 August 2026 after in-chat accepted-authority trace, implementation-evidence review, recovery-gap classification, falsification, ambiguity review and recommendation  
**Authority class:** Production Architecture / TAS amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-TAS-RECOVERY-001 v1.0 only within exact semantic-release recovery, durable event/reaction/background responsibility recovery, post-target authoritative restriction reconciliation, recovery-candidate completeness and recovery testing  
**Preserves:** active-primary/pilot-light regional DR; PostgreSQL cluster-level PITR; provider-neutral implementation; Recovery Candidate lifecycle; canonical-media recovery; secrets outside generic database backup; provider reconciliation; fencing; session invalidation; recovery validation; existing RPO/RTO objectives  
**Depends on:** MS-TAS-RECOVERY-001 v1.0; ADR-010; ADR-011; ADR-012; ADR-013; ADR-014; MS-PROT-025; composite MS-PROT-026; composite MS-PROT-027; composite MS-PROT-040; composite MS-PROT-053; MS-PROT-054; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-065; MS-PROT-067; composite MS-PROT-068; composite MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-076; MS-PROT-079  
**Closes:** MS-PROT-079 Target 21 — Backup / restore / disaster recovery  
**Purpose:** Bring Main Street’s accepted recovery architecture into exact conformance with the production semantic-release, durable Event Reaction/background-work and terminal-authority models accepted after MS-TAS-RECOVERY-001 v1.0, without introducing a second source of business authority or changing the initial regional recovery topology.

---

## 1. Governing Decision

MS-TAS-RECOVERY-001 v1.0 remains authoritative except where this amendment is more specific.

The recovery rule becomes:

```text
recover coherent authoritative state
        +
recover exact semantic meaning required by that state
        +
recover durable unfinished responsibilities
        +
reconcile authoritative restrictions/effects
that must survive the selected restore point
        ↓
validate
        ↓
fence competing authority
        ↓
promote
        ↓
rebuild derivatives
        ↓
resume bounded execution
```

A recovery candidate that contains database rows but cannot establish the exact semantic meaning or unresolved durable responsibilities required to interpret them is **not production-ready authority**.

---

## 2. Recovery Authority Remains Singular

This amendment does not create a Recovery domain that owns business state.

```text
Recovery metadata
    ≠ business truth

Recovery Manifest
    ≠ semantic authority

Recovery Release Bundle
    ≠ Semantic Registry Release

backup copy
    ≠ Merchant Configuration

recovered Event Reaction
    ≠ authority to perform arbitrary mutation

operator recovery decision
    ≠ authority to rewrite business history
```

Recovery infrastructure restores and validates accepted authorities.

It does not replace them.

---

## 3. Exact Semantic-Release Recovery

MS-TAS-RECOVERY-001 v1.0 requires a compatible semantic registry release.

Target 21 makes this exact.

For every restored context requiring source-release resolution, recovery SHALL be able to establish:

```text
exact Semantic Registry Release identity
        +
exact immutable Published Semantic Definition Set evidence
        +
integrity/provenance
        +
a conforming materialisation path
```

according to ADR-011 and ADR-013.

Recovery MUST NOT use:

```text
latest release
nearest compatible release
current registration code relabelled as old release
default release
```

as a substitute.

---

## 4. Recovery Release Bundle Amendment

The conceptual `RecoveryReleaseBundle` is refined to include or resolve:

```text
RecoveryReleaseBundle
{
    application artefact / image

    Flyway migration chain

    exact Semantic Registry Release identities

    exact Published Semantic Definition Set evidence
        or durable reference to that immutable evidence

    Packaged Semantic Definition Bundles
        required by the recovered serving path

    integrity / provenance metadata

    executable-contract support evidence
        required for recovered execution paths

    deployment / infrastructure specification
}
```

A release bundle MAY physically reference separately retained immutable artefacts.

It does not have to copy all evidence into one archive.

---

## 5. Source-Release Resolution Is Conditional

Not every historical object requires full historical source-definition materialisation.

ADR-011 survives:

```text
exact retained RCP
+
other accepted immutable execution evidence
```

may be sufficient for a path that does not require Source-Release Resolution.

Therefore recovery SHALL determine:

```text
does this recovered execution/interpretation path
require source-release resolution?
```

If **yes**, exact release definition evidence is mandatory.

If **no**, the accepted retained evidence may suffice.

Recovery MUST NOT impose universal eager loading of every historical Main Street release.

---

## 6. Recovery Semantic Coverage Gate

Before promotion, the candidate SHALL prove semantic coverage for every exact release required by:

```text
current active Merchant Configurations

recovered residual-authority execution

historical interpretation paths
that genuinely require source definitions

registered durable work/event/reaction responsibilities
whose contract meaning must still be interpreted
```

The gate is:

```text
REQUIRED RELEASE
    materialisable exactly
        OR
    path proven not to require source-release resolution
        ↓
PASS
```

Otherwise:

```text
FAIL RECOVERY VALIDATION
```

---

## 7. Materialisation Is Not Executability

Recovery SHALL preserve:

```text
semantic definition materialisable
    ≠ executable contract supported
    ≠ operation authorised
    ≠ operation operationally eligible
```

A restored release may be perfectly materialisable while its required execution implementation is unavailable.

Such a candidate SHALL NOT silently reinterpret the contract through another implementation.

ADR-012 remains authoritative for executable support.

---

## 8. Durable Event Publication Recovery

A restored database SHALL preserve sufficient durable state to recover required event publication responsibility.

Where a Domain Event occurrence committed before the restore point:

```text
authoritative source fact
+
Domain Event occurrence/publication responsibility
```

must remain mutually consistent according to the accepted transaction boundary.

Recovery MUST NOT infer:

```text
event row absent
    → source fact did not happen
```

where another accepted durable representation establishes the event/publication responsibility.

---

## 9. Event Reaction Recovery

A production Event Reaction is now a distinct durable recovery concern.

Recovery SHALL preserve or reconstruct sufficient evidence for:

```text
source Event occurrence

exact EventReactionContract identity

reaction owner

logical Event Reaction identity

reaction acknowledgement/progression

ordering/supersession affinity where required

downstream logical-intent affinity

unresolved failure/uncertainty evidence
```

One Event Reaction is logically identified by the accepted relationship:

```text
source Event occurrence
+
EventReactionContract
```

or its equivalent stable representation.

---

## 10. No Global Event Acknowledgement During Recovery

Recovery MUST NOT collapse:

```text
Event published
```

into:

```text
all reactions completed
```

For one event:

```text
Reaction A completed
Reaction B pending
Reaction C failed/retrying
```

remains a valid recovered state.

Each reaction resumes independently.

---

## 11. Physical Broker State Is Still Not Authority

MS-TAS-RECOVERY-001 v1.0 survives and becomes more explicit:

```text
queue message
broker offset
scheduler entry
worker memory
```

are not the canonical recovery source for Event Reaction or Durable Work responsibility.

The canonical source is the accepted durable responsibility/progression evidence.

Physical transport infrastructure MAY be reconstructed from it.

---

## 12. Duplicate Reaction Recovery

Following restore, repeated delivery of an already-known:

```text
Event E
+
Reaction Contract R
```

MUST converge on the same logical Event Reaction.

Recovery SHALL NOT manufacture another logical consequence merely because:

```text
broker state was lost
worker acknowledgement was lost
physical delivery is repeated
```

---

## 13. Durable Work Recovery

The following identities remain distinct across recovery:

```text
DurableWorkInstruction
    ≠ WorkAttempt
    ≠ downstream Command
    ≠ provider effect
    ≠ Event Reaction
```

A retry/restart after recovery SHALL resolve the previously established downstream logical intent where required.

It MUST NOT create a new effect merely because the previous WorkAttempt record was interrupted.

---

## 14. Work Attempt Uncertainty

If a recovered WorkAttempt may have crossed a consequential execution boundary but the outcome cannot be established:

```text
OUTCOME UNCERTAIN
```

shall remain explicit.

Recovery MUST NOT reset it to:

```text
NOT ATTEMPTED
```

simply because the application crashed before acknowledging completion.

The applicable MS-PROT-069 reconciliation contract governs resolution.

---

## 15. Post-Restore Authority Survival

MS-TAS-RECOVERY-001 v1.0 already protects post-target privacy/security restrictions.

Target 21 generalises the rule narrowly.

A fact established after restore point `T` SHALL be preserved/reconciled when all of the following are true:

```text
1. allowing T-state to resume would revive
   authority that later became invalid;

2. the later fact is security-, privacy-,
   terminal-control- or similarly
   non-resurrectable authority;

3. the applicable owning authority defines
   that later state as still effective/current;

4. recovery can establish sufficient
   trusted evidence of that later fact.
```

This is **not** a general replay of all post-T business transactions.

---

## 16. Initial Non-Resurrectable Authority Classes

The initial portfolio SHALL include at least, where applicable:

```text
credential revocation

security-key / credential-generation invalidation

PersonalDataUseBasis withdrawal

privacy/data-use restriction or disposition

public Exposure withdrawal required
by current security/privacy authority

effective Merchant Account Suspension

Merchant Account terminal closure

ended Merchant Controller relationship
caused by authoritative transfer/recovery/closure

other explicitly registered
security/control revocation
```

These remain owned by their respective authorities.

Recovery only prevents the selected historical database point from defeating them.

---

## 17. Merchant Account Closure

MS-PROT-076 defines:

```text
OPEN → CLOSING → CLOSED
```

and:

```text
CLOSED = terminal
```

Therefore, if:

```text
T1 Merchant Account OPEN

T2 Merchant Account CLOSED

T3 disaster restore selects T1
```

recovery MUST NOT make the merchant operationally `OPEN` again merely because T1 predates the terminal closure.

The recovery process SHALL reconcile the terminal lifecycle fact and corresponding Controller relationship consequence before ordinary merchant operation resumes.

---

## 18. Controller Relationship Recovery

If a later accepted Controller transfer or terminal closure ended an earlier Controller relationship, restoring a pre-transition database point SHALL NOT reinstate the old Controller as current authority.

Required:

```text
restored historical relationship
+
trusted post-target control evidence
        ↓
reconcile through Merchant Account /
Controller authority
        ↓
current valid relationship state
```

Session invalidation alone is insufficient because the relationship itself is authoritative.

---

## 19. Ordinary Post-Target Business Mutation Is Different

The amendment intentionally does **not** require reconstruction of all mutations after `T`.

For example, within the accepted regional RPO:

```text
new Order
Inventory adjustment
ordinary Profile edit
ordinary Publication revision
```

may be lost according to the accepted recovery objective if no stronger authority requires survival.

Recovery is not a hidden continuous business-event replication system.

---

## 20. External Provider Effects

MS-TAS-RECOVERY-001 v1.0 survives.

For provider effects potentially occurring after the restored local point:

```text
Payment
Refund
Shipment preparation
Return label
Notification delivery
AI/provider operation
other provider-side effect
```

recovery SHALL preserve exact historical execution affinity where available and reconcile against trusted provider evidence.

It SHALL NOT blindly resubmit the effect.

---

## 21. Historical Provider Affinity

Reconciliation after recovery SHALL retain the exact historical execution path where semantics require it, including applicable:

```text
ProviderConnection

Fulfilment Binding

platform/merchant routing affinity

provider request identity

logical effect identity
```

Current provider configuration MUST NOT silently replace the historical path merely because the old provider is no longer the ordinary current provider.

---

## 22. Derived-State Recovery

Derived state remains reconstructable.

Recovery SHALL not back up or restore a projection merely to avoid performing the owning Projection Contract.

After recovery:

```text
authoritative state
        ↓
Projection Contract
        ↓
recompute/rebuild
```

where applicable.

Any stale recovered derivative has no mutation authority.

---

## 23. Exposure After Recovery

A restored projection or old stored public representation SHALL NOT automatically resume Exposure.

Before protected/customer/public surfaces reopen, Main Street SHALL re-establish:

```text
Projection Serviceability
+
current Exposure
+
current security/privacy restrictions
+
relationship/context requirement
where applicable
```

A cached or restored previously exposed value cannot override a later applicable revocation.

---

## 24. Data Lifecycle and Backups

Backup retention remains distinct from business retention.

When data previously subject to disposition reappears inside a restored historical candidate:

```text
existence in backup
    ≠ renewed use authority
```

The recovery path SHALL re-evaluate applicable DataLifecycleContracts before such data can return to ordinary operational use, AI use, provider transmission or public/customer Exposure.

This composes with the accepted `RETAIN | DISPOSITION_DUE | UNRESOLVED` model.

---

## 25. Recovery Candidate Validation Additions

MS-TAS-RECOVERY-001 v1.0 Section 18 is amended to require validation of, where applicable:

```text
exact semantic-release definition evidence

Packaged Semantic Definition Bundle integrity

Deployment Semantic Materialisation coverage

executable-contract support for required paths

Domain Event publication responsibility consistency

Event Reaction identity/progression consistency

DurableWorkInstruction / WorkAttempt consistency

downstream logical-intent affinity

unresolved provider-effect/reconciliation identity

post-target terminal/control restriction reconciliation

Merchant Account lifecycle/controller cardinality

current data-lifecycle use restrictions
```

Failure of any required gate means:

```text
DO NOT PROMOTE
```

---

## 26. Recovery Business-Invariant Test Additions

The recovery test catalogue SHALL additionally exercise:

```text
old semantic release required
    → exact bundle materialises

required old release missing
    → candidate fails closed

old event occurrence
with one completed and one pending reaction
    → only pending reaction resumes

worker crash after downstream effect
before acknowledgement
    → no duplicate logical effect

restore before Merchant Account closure
    → CLOSED account does not reopen

restore before Controller transfer
    → former Controller does not regain authority

restore before data disposition
    → old retained bytes do not regain
      ordinary use authority

provider effect after local restore point
    → reconcile, do not blindly repeat
```

---

## 27. Recovery Exercise Evidence

Each applicable restore/DR exercise SHALL record whether:

```text
exact semantic materialisation succeeded

required execution contracts were supported

Event Reaction progression was recovered correctly

durable work resumed without duplicate intent

post-target non-resurrectable authority was preserved

provider uncertainty was reconciled safely
```

A restore drill that verifies only database readability remains insufficient.

---

## 28. Recovery Manifest Refinement

`RecoveryManifest` SHOULD be sufficient to correlate the recovered store with:

```text
exact database recovery point

schema/Flyway state

application release

required Semantic Registry Releases

corresponding immutable definition evidence

Recovery Release Bundle identity

canonical-media recovery set

security/KMS generation references

verification result
```

It remains recovery metadata, not business authority.

---

## 29. Fencing Survives Unchanged

Before recovered authority accepts writes:

```text
previous writer
    MUST be fenced
```

No semantic-release or event-recovery mechanism introduced here permits multiple active authoritative writers.

Initial regional architecture remains pilot-light.

---

## 30. No Active-Active Expansion

Target 21 does not authorise:

```text
multi-master PostgreSQL
active-active Inventory
active-active Appointment allocation
cross-region conflict merging
multi-region Merchant Account authority
automatic business-state CRDT resolution
```

Those require separately governed evidence.

---

## 31. Recovery Failure Classes

Recovery operations SHALL distinguish at least:

```text
BACKUP_INTEGRITY_FAILURE

DATABASE_INTEGRITY_FAILURE

SCHEMA_COMPATIBILITY_FAILURE

SEMANTIC_RELEASE_UNAVAILABLE

SEMANTIC_RELEASE_INTEGRITY_FAILURE

SEMANTIC_MATERIALISATION_FAILURE

EXECUTABLE_SUPPORT_INSUFFICIENT

EVENT_REACTION_INCONSISTENCY

DURABLE_WORK_INCONSISTENCY

POST_TARGET_AUTHORITY_UNRESOLVED

PROVIDER_RECONCILIATION_UNRESOLVED

SECURITY_RECONCILIATION_UNRESOLVED

DATA_LIFECYCLE_RECONCILIATION_UNRESOLVED

PRIOR_WRITER_NOT_FENCED

RECOVERY_INVARIANT_FAILURE
```

Exact Java exception types, runbook codes and alert identifiers remain implementation detail.

---

## 32. Operational Approval

Regional promotion remains an explicitly authorised operational action.

An operator may decide:

```text
promote validated candidate
```

but cannot use promotion to waive unresolved semantic or security gates.

Therefore:

```text
operator approval
    ≠ override failed recovery validation
```

Where extraordinary intervention is necessary, it must use an accepted audited intervention path.

---

## 33. Alternatives Considered

### A. Accept MS-TAS-RECOVERY-001 v1.0 unchanged

Rejected.

Its overall architecture is compatible, but it predates exact packaged semantic materialisation and independent Event Reactions and does not clearly prevent resurrection of later terminal Merchant Account/control facts.

### B. Rewrite recovery architecture completely

Rejected.

The existing topology and core invariants remain appropriate. A rewrite would create unnecessary churn.

### C. Introduce a separate Recovery semantic subsystem

Rejected.

Recovery would become a second authority over business facts.

### D. Narrow v1.1 amendment

**Accepted.**

It preserves v1.0 while adding only the production constraints introduced by later accepted authorities.

---

## 34. Falsification

### Scenario 1 — Restore old DB with only latest semantic code

```text
DB requires R7
deployment contains only current R12 definitions
```

Result:

```text
FAIL
```

R12 cannot be relabelled as R7.

### Scenario 2 — Exact R7 bundle exists but runtime no longer supports required contract

Result:

```text
semantic materialisation = PASS
execution support = FAIL
candidate not serviceable for that required path
```

### Scenario 3 — Event E1 had reactions N1 and P1

Before disaster:

```text
N1 completed
P1 pending
```

Recovery must preserve that distinction.

It must not re-run N1 merely because the broker was rebuilt.

### Scenario 4 — WorkAttempt called payment provider, response lost

Recovery cannot conclude:

```text
not attempted
```

It must enter reconciliation.

### Scenario 5 — Merchant Account closed after selected restore point

Recovery must not reopen it.

### Scenario 6 — Controller A transferred control to B after restore point

Recovery must not reinstate A merely because old DB state names A.

### Scenario 7 — customer data deleted/restricted after restore point

Bytes may reappear in the isolated candidate, but they cannot regain ordinary use or Exposure authority.

### Scenario 8 — ordinary Order created inside accepted regional RPO and not present in recovery point

This amendment does not invent the Order from nowhere.

The accepted RPO still applies.

### Scenario 9 — old provider binding produced a side effect

Current provider configuration differs.

Recovery reconciles the historical path rather than silently attributing the effect to the current provider.

### Scenario 10 — previous primary returns

It remains fenced.

No split-brain authority is permitted.

All scenarios are satisfiable without changing the active-primary/pilot-light architecture.

---

## 35. Deferred Implementation Scope

The following remain downstream implementation/operations choices:

```text
cloud provider

managed PostgreSQL product

WAL/archive product

object-storage product

KMS/secret product

exact backup encryption mechanism

exact RecoveryManifest encoding

exact RecoveryReleaseBundle archive format

exact semantic bundle archive format

exact digest/signature algorithm

exact infrastructure-as-code tool

exact DR orchestration scripts

exact restore validation implementation

exact SQL integrity queries

exact Event Reaction persistence schema

exact DurableWork persistence schema

exact provider reconciliation clients

exact recovery operator UI/API

exact health/dashboard/alert implementation

exact immutable recovery-log technology,
if one is required to preserve post-target
non-resurrectable evidence across PITR

exact runbook wording
```

The final item is especially important: this amendment requires the **behaviour** that later non-resurrectable facts survive recovery; it does not prematurely select the physical mechanism that stores those facts outside the rollback boundary.

---

## 36. Conformance Criteria

Target 21 is Design-Closed only if an implementation can demonstrate:

1. one coherent PostgreSQL authoritative recovery point;
2. exact source-release resolution where required;
3. no semantic `latest` fallback;
4. materialisation and executable support remain distinct;
5. durable Domain Event publication responsibility survives;
6. Event Reactions recover independently;
7. completed reactions are not duplicated merely because transport state was rebuilt;
8. Durable Work preserves downstream logical-intent affinity;
9. uncertain external effects enter reconciliation;
10. terminal/control/privacy/security authority is not resurrected by PITR;
11. Merchant Account `CLOSED` cannot silently become `OPEN`;
12. ended Controller authority cannot silently reactivate;
13. restored personal/restricted data does not regain use authority solely because bytes reappeared;
14. historical provider affinity is preserved for reconciliation;
15. derived projections remain rebuildable rather than authoritative backup truth;
16. prior writer is fenced before promotion;
17. candidate validation fails closed where required recovery evidence is unresolved;
18. recovery exercises test these invariants.

---

## 37. Acceptance Statement

> **Main Street recovery restores a coherent authoritative past without allowing that past to overwrite semantic identity, unfinished durable responsibilities, later non-resurrectable authority or external reality. Exact releases are reconstructed exactly; Event Reactions and Durable Work resume from their own durable identities; terminal, security and privacy restrictions cannot be undone merely by PITR; provider effects are reconciled rather than repeated; and no recovered candidate becomes authoritative until these conditions are proven.**
