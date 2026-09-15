# MS-PROT-040 v1.8 — Configuration Reinstatement Decision Affinity Amendment

**Document ID:** MS-PROT-040  
**Version:** 1.8  
**Status:** ACCEPTED  
**Approved:** 12 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` and `designs/DOCUMENT-GOVERNANCE.md`  
**Amends:** Composite MS-PROT-040 through v1.7  
**Implementation gate:** `IMP-05-R3B — Reinstatement composition`  
**Design-gap classification:** `UNDERSPECIFIED_SEMANTICS / MISSING_CONSISTENCY_RULE`  
**Purpose:** Define the exact causal affinity that makes a Configuration reinstatement a new current decision rather than reuse of historical approval, historical evidence or historical activation.

---

## 1. Problem

Accepted MS-PROT-040 v1.6 and v1.7 establish that reinstatement is not rollback.

A superseded immutable Configuration Revision may become active again only through:

```text
superseded immutable Configuration Revision
        ↓
current validation / package evidence
        ↓
current impact-review evidence
        ↓
new current Controller approval
        ↓
trusted current Controller activation
        ↓
current compatibility
        ↓
current serving admission
        ↓
current concurrency
        ↓
new Configuration Activation
```

Historical approval is insufficient.

Historical activation replay is insufficient.

The accepted corpus did not previously define the exact causal identifier that distinguishes an old approval for Revision A from a new approval to reinstate Revision A against the merchant's current Configuration state when the same Identity and same Controller Relationship remain current.

Controller Relationship affinity alone therefore cannot establish that an approval is a new reinstatement decision.

A temporal convention, request-name convention, implementation-specific query ordering or "latest row" heuristic MUST NOT supply this semantic rule.

---

## 2. Fundamental Vision Conformance

**Result: VISION-CONFORMING.**

The proposal preserves Main Street's business-language and administrative-compression principles.

The merchant is not asked to understand Reinstatement Basis Activation, activation generations, approval types or revision graph mechanics.

The intended merchant interaction remains conceptually:

```text
Use this previous configuration again
        ↓
Main Street re-checks it against the business now
        ↓
Main Street shows material consequences
        ↓
merchant approves
        ↓
Main Street activates it
```

The additional precision is internal infrastructure required to prevent stale authority from being reused.

It therefore increases internal correctness without proportional merchant-visible complexity.

---

## 3. Configuration Reinstatement

A **Configuration Reinstatement** is a new activation of an immutable Configuration Revision that:

```text
belongs to the same Merchant Scope
AND
is not the merchant's current active Configuration Revision
AND
has at least one prior committed Configuration Activation
for that Merchant Scope
```

Therefore:

```text
previously active + now superseded
        = eligible reinstatement target
```

subject to every current validation, impact, approval, compatibility, admission and concurrency predicate.

A revision that has never previously been active is not a reinstatement target.

A revision that is already current is not a reinstatement target.

An exact lost-acknowledgement replay of the current activation is not reinstatement.

---

## 4. Reinstatement Basis Activation

Every Configuration Reinstatement decision SHALL be evaluated against one exact **Reinstatement Basis Activation**.

The Reinstatement Basis Activation is:

```text
the exact committed Configuration Activation
that owns the merchant's current Configuration pointer
when the reinstatement decision is established
```

Its canonical identity is the exact `activation_request_identifier` of that committed activation.

Revision identity alone is insufficient.

Canonical distinction:

```text
current Configuration Revision = B
        ≠
exact current Configuration Activation = B₁
```

because a later lifecycle may contain:

```text
A
→ B₁
→ C
→ B₂
```

where both B₁ and B₂ activate Revision B but represent materially different current-decision contexts.

---

## 5. Reinstatement Decision Affinity

All authoritative evidence used to establish one reinstatement decision SHALL carry exact affinity to the same Reinstatement Basis Activation.

For reinstatement, the authoritative chain becomes:

```text
Reinstatement Basis Activation
        │
        ├── exact target Configuration Revision
        │
        ├── exact reinstatement validation evidence
        │       ↓
        │   exact Resolved Configuration Package evidence
        │
        ├── exact reinstatement impact-review evidence
        │
        └── exact reinstatement approval
```

The following facts SHALL therefore carry the exact Reinstatement Basis Activation identifier when produced for reinstatement:

```text
Configuration Validation Evidence
Configuration Impact Review Evidence
Configuration Revision Approval
```

Absence of Reinstatement Basis Activation affinity means that the fact was not established as part of a reinstatement decision.

Existing historical evidence SHALL NOT be backfilled with invented reinstatement affinity.

---

## 6. Fresh Evidence Rule

For reinstatement:

```text
historical validation evidence
        ≠ current reinstatement validation evidence

historical impact review
        ≠ current reinstatement impact review

historical approval
        ≠ current reinstatement approval
```

Freshness SHALL be established by creation of new immutable evidence facts carrying the exact current Reinstatement Basis Activation.

Freshness SHALL NOT be inferred solely from timestamp comparison, row ordering, a "latest" query result, the same Controller Relationship, the same approving Identity, the same target revision, the same semantic release or the same package content.

This avoids using clocks or persistence ordering as business authority.

A Resolved Configuration Package used for reinstatement SHALL be the exact package evidence referenced by the basis-affined reinstatement validation evidence.

---

## 7. Reinstatement Approval

A **Configuration Reinstatement Approval** is a new immutable Configuration-owned approval decision with exact affinity to:

```text
Merchant Scope
target Configuration Revision
Semantic Registry Release
Reinstatement Basis Activation
successful basis-affined validation evidence
exact Resolved Configuration Package evidence
completed basis-affined impact-review evidence
approving principal
current Controller Relationship
approval time
```

The approving principal MUST be the current ACTIVE Merchant Controller through the exact recorded Controller Relationship and trusted authenticated context required by composite MS-PROT-040.

The approval MAY target either the original initial Configuration Revision or a later previously active Configuration Revision, provided the target is currently superseded.

The historical base of the target revision does not have to equal the merchant's current revision.

Reinstatement means re-activating an immutable historical decision after fresh current evaluation; it does not rewrite the target revision's historical base.

---

## 8. Superseded Initial Revision

The initial Configuration Revision is not permanently excluded from reinstatement merely because:

```text
configuration_version = 1
base_configuration_revision_identifier = null
```

Those properties describe how that immutable revision was originally created.

They do not mean that the revision may never become active again.

Therefore a superseded initial Configuration Revision MAY be reinstated through the ordinary reinstatement decision path.

Its original first-configuration approval SHALL NOT authorise that reinstatement.

Its original onboarding approval semantics SHALL NOT be generalised or reused as reinstatement semantics.

---

## 9. Approval Authority Separation

The existing `InitialConfigurationRevisionApprovalAuthority` continues to govern ordinary first activation.

The existing `NonInitialConfigurationRevisionApprovalAuthority` continues to govern ordinary forward non-initial replacement approval within its accepted scope.

Neither authority SHALL be broadened merely for implementation convenience.

Reinstatement SHALL use a bounded reinstatement approval authority capable of accepting any genuinely superseded immutable Configuration Revision, including the initial revision.

Canonical separation:

```text
first activation approval
        ≠
forward replacement approval
        ≠
reinstatement approval
```

These may share common exact-evidence infrastructure without sharing applicability semantics.

---

## 10. Reinstatement Classification During Activation

After exact committed-request replay has been resolved, activation SHALL classify the target as follows:

```text
no current Configuration
        → ordinary first activation

current Configuration exists
AND target is not current
AND target has a prior committed activation
        → reinstatement

current Configuration exists
AND target has no prior committed activation
AND target's immutable base equals current revision
        → ordinary forward replacement

otherwise
        → activation conflict
```

Historical activation existence MUST therefore be considered before treating a target merely as another forward replacement.

This prevents a previously active revision from accidentally returning through replacement semantics when its historical base happens to become current again.

---

## 11. Current Applicability of Reinstatement Approval

A reinstatement approval is currently applicable only while all accepted current-authority predicates remain satisfied and:

```text
approval.reinstatementBasisActivation
        =
merchantCurrentConfigurationActivation.activationRequestIdentifier
```

If the current activation pointer moves after approval, that approval immediately ceases to be applicable for reinstatement.

This remains true even if the merchant later returns to the same Configuration Revision.

Example:

```text
A
→ B₁

approve reinstatement of A
basis = B₁

B₁
→ C
→ B₂
```

The approval against B₁ SHALL NOT become applicable again merely because Revision B is current again.

A new reinstatement decision against B₂ is required.

---

## 12. Activation

A valid reinstatement activation requires:

```text
trusted authenticated execution context
OPEN Merchant Account
no effective account-wide Suspension
current ACTIVE Merchant Controller
target is a genuine superseded revision
exact current Reinstatement Basis Activation
exact currently applicable reinstatement approval
exact basis-affined validation/package evidence
exact basis-affined impact review
current compatibility
current serving admission
current concurrency
new activation request identity
```

Successful reinstatement creates a new immutable Configuration Activation fact.

It SHALL update the current Configuration pointer atomically under the existing activation consistency boundary.

It SHALL NOT modify the historical target Configuration Revision.

---

## 13. Historical Approval

Every committed approval remains immutable historical evidence.

For reinstatement:

```text
historical approval existence
        ≠
reinstatement applicability
```

An original first-activation approval has no Reinstatement Basis Activation affinity and therefore cannot authorise reinstatement.

A previous reinstatement approval remains historical after use, but its basis refers to the prior current activation and therefore cannot authorise a later reinstatement cycle.

The same current Controller Relationship does not revive it.

---

## 14. Retry and Idempotency

Approval request identity remains append-only and exact.

An exact retry of the same reinstatement approval request MAY return the committed approval only when the request carries the same complete intent, including the same target revision, Reinstatement Basis Activation, validation evidence, impact-review evidence, package evidence, approving principal and approval time.

Reusing the request identity with a different basis or evidence is an approval-request identity conflict.

Configuration activation retry retains the already established R3A/R3B rule:

```text
committed activation request
+ exact original intent
+ exact activation still owns current pointer
        → lost-acknowledgement replay

historical activation request
after current pointer moved
        → not replay
```

Historical activation replay SHALL NOT create reinstatement.

---

## 15. Concurrency

The reinstatement approval writer SHALL establish its basis while the merchant's exact current Configuration Activation cannot move underneath the decision.

The implementation consistency order SHALL preserve the existing Configuration activation lock ordering.

A conforming relational implementation MAY use:

```text
approval logical-request fence
        ↓
merchant Configuration-activation fence
        ↓
merchant current-authority fence
        ↓
resolve exact current activation
        ↓
validate superseded target + evidence + Controller
        ↓
commit approval
```

The current-authority fence remains shared with Controller transfer, Merchant Account closure and Suspension transitions as already required.

Activation SHALL re-check that the exact basis activation still owns the current pointer.

Approval-time locking does not replace activation-time revalidation.

---

## 16. Persistence Semantics

The minimum durable representation SHALL make reinstatement affinity explicit rather than inferred.

For facts participating in reinstatement, persistence SHALL support an optional exact:

```text
reinstatement_basis_activation_request_identifier
```

on:

```text
Configuration Validation Evidence
Configuration Impact Review Evidence
Configuration Revision Approval
```

For ordinary non-reinstatement facts this affinity is absent.

For reinstatement facts it is mandatory.

Where a relational implementation is used, the identifier SHALL reference an immutable committed Configuration Activation belonging to the same Merchant Scope.

Existing rows SHALL remain unchanged with absent reinstatement affinity.

No historical backfill SHALL fabricate an affinity that did not exist when the evidence or approval was created.

Uniqueness and retry constraints MUST NOT collapse facts belonging to different Reinstatement Basis Activations.

---

## 17. Failure Semantics

Missing reinstatement approval or evidence fails closed.

A reinstatement approval whose basis no longer owns the current pointer is not currently applicable and SHALL require a new current decision.

A stale `expectedCurrentConfigurationIdentifier` retains the existing activation-conflict behaviour.

A revision that was never previously activated SHALL NOT be accepted through reinstatement semantics.

A currently active target SHALL NOT generate a new reinstatement activation; only a valid exact lost-acknowledgement replay may return an existing activation result.

Existing compatibility and serving-admission rejection semantics remain unchanged.

No new merchant-visible failure taxonomy is required by this amendment.

---

## 18. Non-Claims

This amendment does not create rollback.

It does not mutate historical Configuration Revisions.

It does not make onboarding approval reusable.

It does not broaden Workforce approval or activation authority.

It does not create system, AI or scheduled ordinary activation authority.

It does not relax OPEN-account or Suspension checks.

It does not relax Controller Relationship currentness.

It does not allow an old package or old impact review to be relabelled as fresh.

It does not change semantic compatibility ownership.

It does not change serving-admission ownership.

It does not unlock IMP-06 or another downstream macro by itself.

---

## 19. Architecture Review

**PASS.**

The amendment keeps Configuration as the owner of Configuration decision/activation semantics.

It does not duplicate Merchant Account or Controller truth.

It reuses immutable Configuration Activation identity rather than introducing a separate arbitrary generation counter.

It preserves first-activation and ordinary forward-replacement authorities instead of widening them.

It adds one narrow causal affinity required to make the already-accepted "fresh current reinstatement decision" mechanically enforceable.

No merchant-facing software abstraction is introduced.

---

## 20. Falsification

### F1 — Historical first approval, same Controller

A initially approved; A activated; A → B; Controller Relationship unchanged; attempt to reinstate A using original approval.

**Required result:** REJECT.

The original approval has no Reinstatement Basis Activation affinity.

**PASS.**

### F2 — Fresh approval of superseded initial revision

A initial revision; A → B; fresh B-basis validation/package for A; fresh B-basis impact review for A; new Controller approval for A with basis B activation; activate A.

**Required result:** SUCCESS with a new activation fact replacing B.

**PASS.**

### F3 — Same-revision cycle alias

A → B₁; approve A reinstatement against B₁; B₁ → C → B₂; attempt to use the B₁ approval while Revision B is current again.

**Required result:** REJECT.

Exact current activation differs.

**PASS.**

### F4 — Historical later revision

A → B → C; attempt to reinstate B using its old forward-replacement approval.

**Required result:** REJECT.

Old replacement approval is not a reinstatement approval.

**PASS.**

### F5 — Controller transfer

Approve reinstatement under Controller Relationship R1; R1 ends; R2 becomes current; activate.

**Required result:** REJECT.

Existing Controller Relationship currentness remains mandatory.

**PASS.**

### F6 — Basis moves during approval

Concurrent activation attempts to move the current pointer while reinstatement approval is being established.

**Required result:** the basis is either established against one serialized current activation or approval fails/retries; no mixed-context approval may commit.

**PASS with required shared activation fence.**

### F7 — Basis moves after approval

Current pointer changes after approval but before activation.

**Required result:** activation revalidation rejects the approval as no longer current.

**PASS.**

### F8 — Evidence-basis mismatch

Validation is bound to B₁; impact review is bound to B₂.

**Required result:** REJECT before approval commits.

**PASS.**

### F9 — Target never previously active

A candidate revision has never had a committed activation.

**Required result:** it is not a reinstatement target.

**PASS.**

### F10 — Low-software-capacity merchant

Merchant selects a previous configuration and is asked only to review the current consequences and approve.

**Required result:** no activation-generation or reinstatement-basis terminology is exposed.

**PASS — vision-conforming.**

### F11 — Historical activation replay

Old activation request identity is reused after another activation owns the pointer.

**Required result:** it cannot cause reinstatement or report current success.

**PASS — consistent with the R3B replay-lineage correction.**

### F12 — Unsupported serving generation

Fresh reinstatement approval exists but exact current serving admission cannot support the approved package/release.

**Required result:** REJECT without pointer movement.

**PASS under existing serving-admission authority.**

No unresolved falsification counterexample remains within this amendment's scope.

---

## 21. Ambiguity Review

**PASS after amendment.**

The following interpretations are explicitly closed:

```text
same revision
        ≠ same current activation

same Controller Relationship
        ≠ new reinstatement approval

historical approval
        ≠ reinstatement approval

latest database row
        ≠ current authority by itself

timestamp recency
        ≠ reinstatement causal affinity

historical base revision
        ≠ required source of reinstatement

initial revision
        ≠ permanently non-reinstatable

activation replay
        ≠ reinstatement

previously inactive candidate
        ≠ reinstatement target
```

Two conforming implementers should therefore derive the same material behaviour.

---

## 22. Alternatives and Trade-offs

| Alternative | Result | Reason |
|---|---|---|
| Broaden `NonInitialConfigurationRevisionApprovalAuthority` to version 1 | REJECT | Collapses first-configuration and reinstatement semantics |
| Reuse the original first approval | REJECT | Directly violates fresh-current-decision authority |
| Use approval timestamp only | REJECT | Clock ordering is not stable causal authority |
| Bind only to current revision identifier | REJECT | Fails B₁ → C → B₂ cycle alias |
| Add approval-purpose enum only | REJECT | Distinguishes intent but does not bind the exact current decision context |
| Create an independent mutable reinstatement state | REJECT | Duplicates authority and adds unnecessary lifecycle state |
| Exact Reinstatement Basis Activation affinity | ACCEPT | Minimal deterministic causal identity using an already authoritative immutable activation fact |

The accepted trade-off is modest additional internal persistence/contract complexity in exchange for deterministic authority, retry safety and historical correctness.

---

## 23. Accepted Result

**STATUS: ACCEPTED.**

The amendment closes the implementation-discovered semantic gap that prevented IMP-05-R3B from being implemented without inference.

It is vision-conforming, preserves existing authority boundaries, survives the recorded falsification cases and resolves the superseded-initial-revision problem without broadening onboarding or ordinary replacement authority.

---

## 24. Implementation Consequence

After repository formalisation and corpus conformance, `IMP-05-R3B` MAY resume tests-first implementation.

This acceptance does not approve implementation code in advance.

It does not make `IMP-05-R3B`, `IMP-05-R3` or `IMP-05` conforming complete.

It does not unlock `IMP-06`.

---

## 25. Approval Scope

The explicit manual approval granted on 12 September 2026 applies to the complete normative authority in this document:

```text
MS-PROT-040 v1.8
Configuration Reinstatement Decision Affinity Amendment
```

Repository formalisation is permitted for this authority and the governance/navigation updates required by existing design governance.
