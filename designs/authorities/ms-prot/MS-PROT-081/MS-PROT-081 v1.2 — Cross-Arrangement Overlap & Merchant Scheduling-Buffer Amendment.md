# MS-PROT-081 v1.2 — Cross-Arrangement Overlap & Merchant Scheduling-Buffer Amendment

**Document ID:** MS-PROT-081  
**Version:** 1.2  
**Status:** **ACCEPTED by manual approval on 9 September 2026**  
**Approved:** Manual approval of the complete revised authority presented in ChatGPT on 9 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Depends on:** composite MS-PROT-074; composite MS-PROT-081 through v1.1; MS-PROT-091  
**Amends:** MS-PROT-081 workforce scheduling constraints; MS-PROT-081 v1.1 §16 and §25  
**Closes:** `MS-PROT-081-DQ-021 — Exact cross-Arrangement scheduling-overlap policy`  
**Implementation activation:** NONE  
**Purpose:** Establish exact cross-Arrangement overlap semantics and a separate merchant-owned minimum buffer between sequential `ScheduledWorkCommitment`s without confusing rota `Shift` requirements with worker scheduling commitments.

---

# 1. Governing Decision

Main Street SHALL distinguish:

```text
Shift
    =
work requirement owned/refined by MS-PROT-091

ScheduledWorkCommitment
    =
authoritative fact owned by MS-PROT-081
that one exact workforce participant
is scheduled to perform work
```

Therefore:

> **Scheduling constraints concerning an individual govern ScheduledWorkCommitments, not the mere existence, timing or publication of unassigned rota Shifts.**

Two independent scheduling predicates are established:

```text
A. overlapping commitments
        ↓
cross-Arrangement overlap policy

B. sequential non-overlapping commitments
        ↓
merchant-owned MinimumInterCommitmentBuffer
```

Neither predicate substitutes for the other.

---

# 2. Cross-Arrangement Governing Rule

Two temporally overlapping `ScheduledWorkCommitment`s belonging to different `WorkforceSchedulingArrangement`s of the same `MerchantMembership` MUST NOT coexist merely because the Arrangements differ.

Their coexistence requires:

```text
current ArrangementOverlapPolicy = OVERLAP_ALLOWED

or

exact authorised CrossArrangementOverlapOverride
```

and every independently applicable scheduling constraint must also pass.

Conversely:

> **Main Street MUST NOT infer that different WorkforceSchedulingArrangements belonging to the same MerchantMembership are necessarily mutually exclusive.**

---

# 3. Problem

Composite MS-PROT-081 permits one `MerchantMembership` to hold multiple `WorkforceSchedulingArrangement`s.

Examples may include:

```text
Barber
+
Reception duties
```

or:

```text
Security work
+
Delivery duties
```

Neither worker identity nor Arrangement labels establish whether simultaneous performance is legitimate.

Therefore Main Street requires an explicit merchant scheduling decision when different Arrangements overlap.

Separately, a merchant may require a minimum rest, transition or operational gap between sequential allocations of work to the same individual.

That business rule is also not inferable by Main Street.

---

# 4. Explicit Non-Goals

This amendment does NOT establish:

- employment status;
- universal working-time law;
- statutory rest-period calculation;
- Payroll consequences;
- compensation;
- universal worker multitasking capability;
- role-name compatibility;
- AI scheduling authority;
- cross-merchant worker coordination;
- travel-time optimisation;
- routing/navigation;
- automatic WorkSite incompatibility;
- universal minimum time between commitments;
- a second worker-scheduling aggregate;
- spacing rules between unassigned rota Shifts.

The canonical worker-scheduled fact remains:

```text
ScheduledWorkCommitment
```

---

# 5. Temporal Interval Rule

For scheduling-conflict purposes, a `ScheduledWorkCommitment` occupies:

```text
[start, end)
```

Two commitments overlap when:

```text
A.start < B.end
AND
B.start < A.end
```

Therefore:

```text
A.end == B.start
```

is sequential, not overlapping.

A Scheduled Break does not automatically remove part of a ScheduledWorkCommitment from these predicates.

---

# 6. Arrangement Pair Identity

An overlap policy applies to exactly two distinct `WorkforceSchedulingArrangement`s satisfying:

```text
same workspace
same MerchantMembership
Arrangement A != Arrangement B
```

The pair is symmetric:

```text
Pair(A, B) = Pair(B, A)
```

Reversing the order MUST NOT create another policy.

---

# 7. ArrangementOverlapPolicy

`ArrangementOverlapPolicy` is the merchant-owned reusable scheduling rule governing whether the existence of commitments under one exact Arrangement pair may ordinarily overlap.

Allowed values:

```text
OVERLAP_ALLOWED
OVERLAP_PROHIBITED
```

There is no persisted:

```text
UNKNOWN
UNRESOLVED
DEFAULT
```

value.

Absence means:

> **No reusable merchant decision has been established for this Arrangement pair.**

Absence is neither permission nor prohibition.

---

# 8. OVERLAP_ALLOWED Meaning

`OVERLAP_ALLOWED` means only:

> **Temporal overlap between commitments under this exact Arrangement pair is not prohibited merely because both commitments exist at the same time.**

It does not establish:

- physical feasibility;
- unlimited simultaneous workload;
- customer availability;
- Appointment capacity;
- compliance;
- WorkSite feasibility;
- leave eligibility;
- compensation consequence.

All other scheduling constraints continue to apply.

---

# 9. OVERLAP_PROHIBITED Meaning

`OVERLAP_PROHIBITED` means:

> **Ordinary creation of temporally overlapping ScheduledWorkCommitments under this exact Arrangement pair is prohibited.**

It does not declare that overlap is universally impossible or unlawful.

An exact authorised one-off override may permit an exception.

---

# 10. No Silent Policy Inference

Main Street MUST NOT establish Arrangement overlap policy from:

- Arrangement names;
- job titles;
- role labels;
- Payroll status;
- Compensation Relationship;
- previous successful overlaps;
- previous rejected overlaps;
- business category;
- employment classification;
- AI inference;
- WorkSite equality or inequality.

Reusable policy requires merchant authority.

---

# 11. Unresolved Overlap

Where:

```text
cross-Arrangement temporal overlap exists
+
no current pair policy exists
+
no exact authorised override exists
```

the scheduling operation SHALL produce:

```text
CROSS_ARRANGEMENT_REVIEW_REQUIRED
```

No new conflicting `ScheduledWorkCommitment` may be committed.

---

# 12. First-Conflict Merchant Decisions

An authorised scheduler encountering an unresolved cross-Arrangement conflict may:

```text
A. reject this scheduling attempt only

B. allow this exact occurrence only

C. establish OVERLAP_ALLOWED for the Arrangement pair

D. establish OVERLAP_PROHIBITED for the Arrangement pair
```

Rejecting one proposed commitment MUST NOT silently establish permanent `OVERLAP_PROHIBITED`.

Likewise, permitting one occurrence MUST NOT silently establish `OVERLAP_ALLOWED`.

---

# 13. CrossArrangementOverlapOverride

A `CrossArrangementOverlapOverride` permits one exact otherwise unresolved or prohibited overlap.

It MUST be bound to the exact scheduling intent and preserve at least:

```text
workspace
MerchantMembership
candidate Arrangement
candidate WorkforceTimeTermsRevision
candidate Shift identity/revision where applicable
candidate interval
conflicting ScheduledWorkCommitment identities
conflicting commitment revisions
applicable policy revision or explicit policy absence
authorising actor
timestamp
reason
logical override identity
resulting ScheduledWorkCommitment identity if consumed
```

An override MUST NOT become reusable pair policy.

---

# 14. Override Consumption

An override may be:

```text
AVAILABLE
    ↓
CONSUMED
```

or:

```text
AVAILABLE
    ↓
REVOKED
```

`CONSUMED` and `REVOKED` are terminal.

One override MUST NOT authorise multiple materially distinct commitments.

---

# 15. Multiple Arrangement Conflicts

If one candidate commitment overlaps commitments under Arrangements A and B:

```text
candidate Arrangement C
```

then both:

```text
Pair(C, A)
Pair(C, B)
```

must independently pass.

Pair policy is not transitive.

Therefore:

```text
ALLOW(A, B)
+
ALLOW(B, C)
```

does not imply:

```text
ALLOW(A, C)
```

---

# 16. Merchant-Owned Sequential Buffer

Main Street SHALL support an optional merchant-owned:

```text
MinimumInterCommitmentBuffer
```

for one exact `MerchantMembership`.

It represents:

> **The minimum merchant-required temporal separation between sequential, non-overlapping ScheduledWorkCommitments allocated to that individual within the merchant workspace.**

It is a scheduling policy.

It is not:

- a Shift property;
- a rota-period property;
- an employment-law classification;
- a statutory rest-period determination;
- an Arrangement-compatibility rule.

---

# 17. Buffer Scope

`MinimumInterCommitmentBuffer` applies:

```text
workspace
+
MerchantMembership
+
all applicable ScheduledWorkCommitments
```

regardless of whether sequential commitments belong to:

```text
same WorkforceSchedulingArrangement
```

or:

```text
different WorkforceSchedulingArrangements
```

This is intentionally Membership-wide because the policy concerns allocation of sequential work to the same individual.

---

# 18. No Main Street Buffer Default

Main Street MUST NOT impose a universal buffer duration.

There is no platform rule:

```text
everyone requires two hours
```

or:

```text
everyone requires one hour
```

or any other fixed duration.

Absence of merchant policy means:

```text
no additional merchant-defined
MinimumInterCommitmentBuffer
```

Other independently authoritative constraints may still prohibit the scheduling operation.

---

# 19. Merchant Policy Value

Where established, `MinimumInterCommitmentBuffer` SHALL be a non-negative deterministic duration.

Conceptually:

```text
MinimumInterCommitmentBuffer
{
    workspace_id
    workforce_membership_id
    duration
    revision
    established_by
    established_at
    reason?
}
```

Examples:

```text
PT30M
PT1H
PT2H
```

are valid representations where supported by the implementation contract.

A merchant wishing to require two hours may establish:

```text
PT2H
```

Main Street does not choose that value.

---

# 20. Sequential Buffer Predicate

For two non-overlapping commitments where:

```text
Earlier.end <= Later.start
```

the sequential gap is:

```text
Later.start - Earlier.end
```

Where a current `MinimumInterCommitmentBuffer` exists:

```text
Later.start - Earlier.end
    >=
MinimumInterCommitmentBuffer
```

is required.

Example:

```text
earlier commitment
09:00–13:00

merchant buffer
PT2H
```

then:

```text
next 13:00    → reject
next 14:30    → reject
next 15:00    → buffer satisfied
next 16:00    → buffer satisfied
```

subject to all other constraints.

---

# 21. Shift Does Not Participate Until Allocation

MS-PROT-091 may contain rota Shifts such as:

```text
Shift A
09:00–13:00

Shift B
13:30–17:30
```

The existence of those two work requirements does NOT itself violate `MinimumInterCommitmentBuffer`.

The buffer becomes relevant only when one exact `MerchantMembership` would acquire corresponding `ScheduledWorkCommitment`s.

Therefore:

```text
Shift spacing
    ≠
worker scheduling-buffer validation
```

---

# 22. Allocation-Mechanism Neutrality

The overlap and sequential-buffer rules apply to creation of a `ScheduledWorkCommitment` regardless of whether it arises through:

```text
DIRECT_ASSIGNMENT
ACCEPTED_TARGETED_SHIFT_OFFER
ACCEPTED_OPEN_SHIFT_CLAIM
```

A ShiftClaim or ShiftOffer does not itself become a ScheduledWorkCommitment merely because its eventual scheduling would satisfy the buffer.

The governing validation occurs when the authoritative commitment would be created.

---

# 23. Overlap and Buffer Are Mutually Exclusive Predicates

For a particular pair of commitments:

```text
if intervals overlap
    → overlap policy applies

if intervals do not overlap
    → sequential-buffer policy may apply
```

The same commitment pair is not simultaneously evaluated as both overlapping and sequential.

Therefore an overlap permitted by `ArrangementOverlapPolicy` is not rejected merely because there is no sequential buffer between the two overlapping commitments.

---

# 24. No Compatibility Inference From Buffer

A buffer says nothing about cross-Arrangement compatibility.

Therefore:

```text
MinimumInterCommitmentBuffer = PT2H
```

does NOT imply:

```text
OVERLAP_PROHIBITED
```

and:

```text
OVERLAP_ALLOWED
```

does NOT imply:

```text
no sequential buffer requirement
```

They are independent merchant scheduling decisions.

---

# 25. Independent Constraints

Neither overlap permission nor sequential-buffer satisfaction overrides independently authoritative constraints including:

```text
ApprovedLeaveEvidence
ScheduleExclusion
Arrangement lifecycle
Membership lifecycle
WorkforceTimeTermsRevision
same-Arrangement scheduling constraints
Shift state
headcount
classification requirements
actor authorisation
accepted regulatory constraints
accepted physical-presence constraints
other scheduling invariants
```

---

# 26. WorkSite Boundary

Different WorkSite identifiers do not automatically establish scheduling impossibility.

Main Street MUST NOT infer:

```text
WorkSite A != WorkSite B
    therefore
scheduling impossible
```

solely from identifier or location inequality.

Where another accepted authority deterministically establishes a physical-presence or location constraint, that independently authoritative constraint continues to govern.

Neither:

```text
OVERLAP_ALLOWED
```

nor:

```text
MinimumInterCommitmentBuffer satisfied
```

may override an independently established impossibility.

---

# 27. Merchant Policy Authority

Only an actor authorised to establish the applicable workforce scheduling policy may establish or revise:

```text
ArrangementOverlapPolicy
MinimumInterCommitmentBuffer
```

Worker self-service access, ShiftClaim eligibility or ShiftOffer acceptance authority does not automatically grant policy-setting authority.

---

# 28. Exception-Driven Merchant Experience

Main Street SHOULD NOT require merchants to configure overlap relationships or buffer values during onboarding merely because workforce scheduling exists.

Configuration SHOULD arise only where the merchant's operating reality requires it.

Examples:

```text
first cross-Arrangement conflict
        ↓
ask merchant for explicit decision
```

or:

```text
merchant expresses:
"Give Jane at least two hours
between separate allocations of work."
        ↓
candidate MinimumInterCommitmentBuffer = PT2H
        ↓
merchant approval
```

The merchant need not understand internal concepts such as:

```text
WorkforceSchedulingArrangement
ArrangementOverlapPolicy
MinimumInterCommitmentBuffer
```

---

# 29. Business-Language Projection

Merchant-facing conflict presentation SHOULD use business language.

Example:

```text
Jane is already scheduled:

Barber
09:00–17:00

You're scheduling:

Reception
12:00–13:00

These duties overlap.
```

For sequential buffer:

```text
Jane finishes at 13:00.

You require at least 2 hours
between separate work allocations.

The earliest permitted start is 15:00.
```

These are presentation examples only.

Exact UI remains downstream.

---

# 30. AI Boundary

AI MAY:

- interpret a merchant's natural-language scheduling preference;
- propose a registered buffer duration;
- explain an overlap conflict;
- present existing authoritative evidence.

AI MUST NOT:

- infer Arrangement compatibility as authority;
- establish reusable policy autonomously;
- invent a buffer requirement;
- infer statutory rest rules;
- create an override without authorised mutation;
- bypass deterministic validation.

---

# 31. Historical Policy Affinity

A `ScheduledWorkCommitment` created because a cross-Arrangement overlap policy permitted it MUST retain sufficient affinity to the exact policy revision used.

A commitment created through an override MUST retain exact override provenance.

Where a merchant buffer materially participated in scheduling validation, sufficient evidence MUST exist to identify the applicable buffer revision used for that scheduling decision.

Later policy changes MUST NOT rewrite historical commitments.

---

# 32. Policy Revision

Material changes to:

```text
ArrangementOverlapPolicy
```

or:

```text
MinimumInterCommitmentBuffer
```

require a new revision.

Current policy controls new or materially revised scheduling operations.

Historical commitments retain historical decision provenance.

---

# 33. Policy Change Does Not Retroactively Reschedule

Changing:

```text
buffer PT2H
    →
buffer PT30M
```

or:

```text
OVERLAP_ALLOWED
    →
OVERLAP_PROHIBITED
```

MUST NOT automatically:

- move existing commitments;
- delete commitments;
- cancel commitments;
- declare historical commitments invalid.

Future and materially revised scheduling operations use the then-current policy.

---

# 34. Concurrency

Scheduling operations for the same `MerchantMembership` MUST provide sufficient atomicity or serialization to protect:

```text
cross-Arrangement explicitness
+
MinimumInterCommitmentBuffer
```

Two concurrent operations MUST NOT each observe an apparently valid schedule and then jointly commit a state that violates either invariant.

Exact locking technology remains downstream.

---

# 35. Currentness

Scheduling validation MUST use current relevant:

```text
ArrangementOverlapPolicy revision
MinimumInterCommitmentBuffer revision
conflicting commitment revisions
candidate Shift revision
WorkforceTimeTermsRevision
```

where applicable.

Materially stale validation requires re-evaluation.

Unsafe last-write-wins behaviour is prohibited.

---

# 36. Idempotency

Logical retries MUST NOT create:

- duplicate policy revisions;
- duplicate overrides;
- duplicate override consumption;
- duplicate `ScheduledWorkCommitment`s.

Lost acknowledgement after commit MUST be recoverable without repeating semantic effect.

---

# 37. Failure and Rejection Semantics

Main Street SHALL distinguish at least:

```text
CROSS_ARRANGEMENT_REVIEW_REQUIRED

CROSS_ARRANGEMENT_OVERLAP_PROHIBITED

INTER_COMMITMENT_BUFFER_NOT_SATISFIED

CROSS_ARRANGEMENT_OVERRIDE_UNAUTHORISED

CROSS_ARRANGEMENT_OVERRIDE_STALE

SCHEDULING_POLICY_STALE

INDEPENDENT_SCHEDULING_CONFLICT

AUTHORISATION_REJECTION

VALIDATION_REJECTION

CONFLICT

OPERATIONAL_FAILURE
```

One generic `FAILED` result MUST NOT erase materially different recovery paths.

---

# 38. Core Invariants

## INV-081-X01 — Shift Is Not Worker Commitment

A rota `Shift` MUST NOT be treated as authoritative evidence that any particular worker is scheduled.

## INV-081-X02 — Cross-Arrangement Explicitness

Different Arrangements MUST NOT permit silent overlapping commitments.

## INV-081-X03 — No Universal Cross-Arrangement Prohibition

Different Arrangements MUST NOT automatically prohibit overlap.

## INV-081-X04 — No Implicit Unknown Policy

Absence of pair policy is unresolved rather than permission or prohibition.

## INV-081-X05 — Pair Symmetry

Overlap policy is symmetric for the exact Arrangement pair.

## INV-081-X06 — No Transitive Compatibility

Arrangement-pair overlap permission is not transitive.

## INV-081-X07 — Override Specificity

A one-off override authorises only its exact scheduling intent.

## INV-081-X08 — Independent Constraints Survive

Overlap permission and buffer satisfaction do not suppress other authorities.

## INV-081-X09 — Minimum Sequential Commitment Buffer

Where a current merchant-owned `MinimumInterCommitmentBuffer` exists for a `MerchantMembership`, sequential non-overlapping `ScheduledWorkCommitment`s MUST satisfy that minimum duration.

## INV-081-X10 — Buffer Is Not Shift Spacing

`MinimumInterCommitmentBuffer` MUST NOT constrain unassigned MS-PROT-091 Shift creation merely because two Shift intervals are close together.

## INV-081-X11 — Buffer Is Not Compatibility

Minimum sequential separation MUST NOT be interpreted as Arrangement overlap policy.

## INV-081-X12 — No Platform Buffer

Main Street MUST NOT impose a universal minimum buffer duration.

## INV-081-X13 — Merchant Authority

The merchant determines supported operational overlap and buffer policy within accepted platform/legal constraints.

## INV-081-X14 — Historical Affinity

Current policy changes MUST NOT rewrite the scheduling basis of historical commitments.

---

# 39. Falsification

## 39.1 Two Unassigned Adjacent Shifts

```text
Shift A 09:00–13:00
Shift B 13:30–17:00
```

No worker has been allocated.

Merchant buffer for Jane:

```text
PT2H
```

Result:

```text
both Shifts may exist
```

PASS.

The buffer constrains Jane's eventual commitments, not rota work requirements.

---

## 39.2 Same Worker Allocated Both

Jane receives a commitment against:

```text
Shift A 09:00–13:00
```

Attempted allocation against:

```text
Shift B 13:30–17:00
```

Merchant policy:

```text
PT2H
```

Result:

```text
INTER_COMMITMENT_BUFFER_NOT_SATISFIED
```

PASS.

---

## 39.3 Exact Two-Hour Boundary

```text
commitment A ends 13:00
commitment B starts 15:00
buffer PT2H
```

Result:

```text
buffer satisfied
```

PASS.

---

## 39.4 Merchant Requires No Additional Gap

No `MinimumInterCommitmentBuffer` exists.

```text
commitment A ends 13:00
commitment B starts 13:00
```

The merchant-defined buffer predicate does not reject the operation.

Other constraints remain authoritative.

PASS.

---

## 39.5 Same Arrangement

```text
Arrangement A commitment
09:00–13:00

Arrangement A commitment
14:00–18:00

buffer PT2H
```

Result:

```text
reject buffer
```

PASS.

The buffer is Membership-wide.

---

## 39.6 Different Arrangements Sequentially

```text
Arrangement A
09:00–13:00

Arrangement B
14:00–18:00

buffer PT2H
```

Result:

```text
reject buffer
```

PASS.

Arrangement difference does not bypass person-wide buffer policy.

---

## 39.7 Different Arrangements Overlap

```text
Arrangement A
09:00–17:00

Arrangement B
12:00–13:00

Pair policy
OVERLAP_ALLOWED

Membership buffer
PT2H
```

Result:

```text
cross-Arrangement overlap passes pair policy
buffer predicate is not applicable to this overlapping pair
continue remaining validation
```

PASS.

---

## 39.8 First Cross-Arrangement Conflict

No pair policy exists.

Result:

```text
CROSS_ARRANGEMENT_REVIEW_REQUIRED
```

PASS.

---

## 39.9 Merchant Rejects Current Attempt Only

Merchant does not proceed.

Result:

```text
no commitment
no reusable policy
```

PASS.

---

## 39.10 One-Off Overlap

Pair:

```text
OVERLAP_PROHIBITED
```

Authorised exact override exists.

Other constraints pass.

Result:

```text
one commitment created
override consumed
```

PASS.

---

## 39.11 Three Arrangements

```text
ALLOW(A,B)
ALLOW(B,C)
A,C absent
```

Candidate overlap requires A-C.

Result:

```text
CROSS_ARRANGEMENT_REVIEW_REQUIRED
```

PASS.

No transitive inference.

---

## 39.12 Approved Leave

Overlap allowed and buffer satisfied.

Applicable Approved Leave independently prohibits commitment.

Result:

```text
reject
```

PASS.

---

## 39.13 Different WorkSites

Two WorkSite identities differ.

No accepted deterministic location constraint establishes impossibility.

Result:

```text
WorkSite inequality alone does not reject
```

PASS.

---

## 39.14 Low-Software-Capacity Merchant

Merchant has one Arrangement per person and no desired minimum gap.

The merchant never encounters either configuration concept.

PASS.

---

## 39.15 Merchant Natural-Language Policy

Merchant says:

```text
"Give Jane two hours between
separate allocations of work."
```

Main Street may propose:

```text
MinimumInterCommitmentBuffer = PT2H
```

Merchant approves.

Deterministic scheduling then enforces it.

PASS.

---

## 39.16 AI Guesses Two Hours

No merchant policy exists.

AI predicts two hours would be sensible.

Result:

```text
no authoritative buffer is created
```

PASS.

---

# 40. Rejected Alternatives

Rejected:

```text
Different Arrangement → always allow overlap
```

Rejected:

```text
Same person → all overlap prohibited
```

Rejected:

```text
worker.can_multitask
```

Rejected:

```text
role-name compatibility
```

Rejected:

```text
historical overlap creates policy
```

Rejected:

```text
Main Street universal two-hour rule
```

Rejected:

```text
buffer attached to every rota Shift
```

Rejected:

```text
two nearby unassigned Shifts are invalid
```

Rejected:

```text
different WorkSite means impossible
```

Rejected:

```text
AI chooses scheduling policy
```

Rejected:

```text
second worker-scheduling aggregate
```

---

# 41. Trade-Off Decision

## Cross-Arrangement overlap

**Rejected:** universal permission.  
Too permissive.

**Rejected:** universal prohibition.  
Cannot represent legitimate concurrent duties.

**Chosen:** explicit Arrangement-pair merchant policy plus narrow one-off override.

## Sequential work separation

**Rejected:** universal Main Street duration.  
Would make Main Street the merchant's operating-policy adjudicator.

**Rejected:** no ability to express a merchant gap rule.  
Would prevent accurate representation of merchants who deliberately require transition/rest time.

**Chosen:** optional merchant-owned Membership-wide `MinimumInterCommitmentBuffer`.

## Shift versus worker scheduling

**Rejected:** enforce the buffer between rota Shift definitions.  
A Shift is work that needs somebody, not proof that one person has been scheduled.

**Chosen:** enforce the buffer only against authoritative `ScheduledWorkCommitment`s.

---

# 42. Fundamental Vision Conformance

**Result:** `VISION-CONFORMING`

The amendment:

- represents real merchant workforce scheduling decisions;
- keeps business decisions with merchants;
- introduces no universal HR policy;
- keeps ordinary staff interaction role-native;
- does not require universal configuration;
- surfaces complexity only when relevant;
- keeps AI inferential;
- preserves single scheduling ownership;
- distinguishes rota work requirements from worker commitments;
- allows business-language configuration instead of exposing internal semantic vocabulary.

Feature Admission:

```text
Representation Test         PASS
Coordination Test           PASS
Administrative Compression PASS
```

---

# 43. Amendment Effect

Upon explicit manual approval and conforming repository formalisation:

1. `MS-PROT-081-DQ-021` becomes **RESOLVED**.
2. Composite MS-PROT-081 gains the exact cross-Arrangement overlap policy.
3. Composite MS-PROT-081 gains the optional merchant-owned `MinimumInterCommitmentBuffer`.
4. MS-PROT-091 remains owner of rota Shift requirements.
5. MS-PROT-081 remains sole owner of `ScheduledWorkCommitment`.
6. MS-PROT-091 statements saying DQ-021 is unresolved become stale current-status assertions and must be conformingly updated without changing surviving semantic meaning.
7. The DDR must record DQ-021 as resolved.
8. Applicable Authority Index/Lexicon navigation must be updated where required.
9. No other MS-PROT-081 deferred question is resolved.
10. No implementation activation is granted.

---

# 44. Implementation Boundary

Acceptance establishes semantic authority only.

It does not independently determine:

- Java representation;
- persistence schema;
- endpoint design;
- locking mechanism;
- scheduler interface;
- authorisation privilege identifiers;
- mobile UI;
- database indexes;
- transport representation.

Implementation must proceed through normal tests-first implementation governance when separately activated.

---

# 45. Final Decision

> **Main Street distinguishes rota work requirements from worker scheduling commitments. A Shift may exist without scheduling any individual; worker-specific scheduling constraints govern the MS-PROT-081 ScheduledWorkCommitment created when an individual is actually allocated to work.**

> **Cross-Arrangement temporal overlap for the same MerchantMembership is neither universally permitted nor universally prohibited. It requires an explicit reusable ArrangementOverlapPolicy or an exact authorised one-off override, while every other authoritative scheduling constraint remains in force.**

> **Main Street may additionally enforce a merchant-owned MinimumInterCommitmentBuffer between sequential non-overlapping ScheduledWorkCommitments for one MerchantMembership. Main Street defines no universal duration. A merchant may choose two hours, one hour, another supported duration or no additional merchant-defined buffer according to the merchant's business operation.**

> **The sequential buffer does not constrain the creation of unassigned rota Shifts, does not define Arrangement compatibility, does not replace statutory or regulatory constraints and does not create a second scheduling authority. Merchant-visible operation remains exception-driven and expressed in business language.**

---

## Recommendation / Approval Record

**VISION CONFORMANCE:** `VISION-CONFORMING`

**RECOMMENDATION:** **ACCEPT**

**Manual approval of this complete revised authority:** GRANTED on 9 September 2026.

### End of accepted MS-PROT-081 v1.2