# MS-PROT-081 v1.1 — Workforce Scheduling Arrangement & Evidence Affinity Amendment

**Document ID:** MS-PROT-081  
**Version:** 1.1  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact final Workforce Corpus Conflict-Correction Package presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Amends:** MS-PROT-081 v1.0 within workforce scheduling/time/leave affinity, arrangement identity/lifecycle, break-compensation boundary and related deferred scope  
**Purpose:** Introduce a narrow Workforce Scheduling Arrangement so one Merchant Membership can participate in multiple distinct scheduling/time/leave rule sets and, where applicable, exact Compensation Relationships without ambiguous later attribution.

---

# 1. Workforce Scheduling Arrangement

MS-PROT-081 SHALL introduce a narrow **Workforce Scheduling Arrangement**.

A Workforce Scheduling Arrangement is a durable merchant-scoped scheduling relationship connecting one Merchant Membership to one coherent set of Workforce Time Terms and, where applicable, one exact Compensation Relationship.

Conceptually:

```text
WorkforceSchedulingArrangement
{
    arrangementIdentity
    MerchantScope
    MerchantMembership
    CompensationRelationship?
    lifecycle
    provenance
}
```

It is NOT:

```text
employment status
employment contract
employee
worker
contractor
self-employed status
Payroll Participant
Compensation Relationship
general HR relationship
```

Its sole purpose is scheduling/time/leave affinity.

---

# 2. Merchant Membership Alone Is Insufficient

One Merchant Membership MAY have multiple Workforce Scheduling Arrangements.

Example:

```text
Alex
Merchant Membership → Builder Ltd

Arrangement A
    site engineer
    direct shifts
    hourly
    Compensation Relationship CR-1
    PAYROLL

Arrangement B
    specialist surveying
    offered assignments
    day rate
    Compensation Relationship CR-2
    NON-PAYROLL
```

Main Street MUST NOT create two Identities or two Memberships merely to distinguish these relationships.

---

# 3. Arrangement Lifecycle

Initial lifecycle:

```text
ACTIVE
   ↓ end
ENDED
```

`ENDED` is terminal for that arrangement identity.

An ACTIVE Merchant Membership does not imply every Scheduling Arrangement is active.

An ENDED Arrangement MUST NOT accept:

```text
new Shift Offers
new Scheduled Work Commitments
new Time Capture Events
new Leave Requests
```

Historical evidence survives.

Where materially different scheduling affinity later begins, a new Arrangement is established.

---

# 4. Compensation Relationship Affinity

A Workforce Scheduling Arrangement MAY reference zero or one Compensation Relationship.

The affinity SHALL remain stable for that Arrangement identity.

Changing the applicable Compensation Relationship SHALL require a new Scheduling Arrangement rather than silently rewriting historical affinity.

Multiple Scheduling Arrangements MAY reference the same Compensation Relationship.

This supports cases such as:

```text
Agency Ltd = Payee

Alice Arrangement → Agency Compensation Relationship
David Arrangement → same Agency Compensation Relationship
```

while Alice and David remain separately attributable workers.

---

# 5. Scheduling Participant and Payee May Differ

Hard distinction:

```text
scheduled person
    ≠ necessarily Payee
```

Example:

```text
Alice
    Merchant Membership
    scheduled and clocked by Merchant

Agency Ltd
    Payee
    Compensation Relationship
```

Main Street MAY preserve:

```text
Alice Arrangement
        ↓
Agency Ltd Compensation Relationship
```

without asserting that Alice personally receives the merchant's compensation.

This supports agency and subcontracting models without manufacturing Payroll relationships.

---

# 6. Workforce Time Terms Scope

`WorkforceTimeTermsRevision` SHALL belong to one exact Workforce Scheduling Arrangement.

Therefore:

```text
Merchant Membership
       ↓
Scheduling Arrangement
       ↓
Workforce Time Terms Revision
```

Different Arrangements for the same person may legitimately have different:

```text
direct-assignment rules
offer/accept rules
normal working patterns
break structures
leave participation rules
scheduling constraints
```

---

# 7. Shift Offer Scope

Every Shift Offer SHALL reference one exact Workforce Scheduling Arrangement and exact Workforce Time Terms Revision.

Acceptance or rejection applies only to that offer and arrangement.

---

# 8. Scheduled Work Commitment Scope

Every Scheduled Work Commitment SHALL reference:

```text
Merchant Scope
Merchant Membership
Workforce Scheduling Arrangement
exact Workforce Time Terms Revision
shift revision
scheduled interval
Scheduled Breaks
provenance
```

A commitment cannot be resolved merely from:

```text
merchant + Identity
```

or:

```text
Merchant Membership
```

where more than one Arrangement exists.

---

# 9. Time Capture Event Scope

Every Time Capture Event SHALL retain one exact Workforce Scheduling Arrangement.

Where a Scheduled Work Commitment exists, the event SHOULD also retain that exact commitment affinity.

Therefore:

```text
CLOCK_IN
BREAK_START
BREAK_END
CLOCK_OUT
```

cannot float ambiguously across multiple arrangements.

---

# 10. Unmatched Time

If Time Capture occurs without an applicable Scheduled Work Commitment:

```text
Arrangement identity
```

must still be determinable.

Where multiple Arrangements are possible and Main Street cannot establish the correct one:

```text
UNRESOLVED
```

The system MUST NOT guess from:

```text
current pay rate
latest Compensation Relationship
job title
most recent shift
AI inference
```

---

# 11. Worked-Time Evidence Scope

Approved Worked-Time Evidence MUST retain exact affinity to:

```text
Merchant Membership
Workforce Scheduling Arrangement
source Time Capture evidence
```

Where the Arrangement has a Compensation Relationship, the resulting Compensation handoff retains that exact relationship affinity.

Rejected:

```text
Alex worked 8h
    ↓
search Alex's current pay relationship
```

Accepted:

```text
Approved Worked-Time Evidence W17
    ↓
Arrangement A
    ↓
Compensation Relationship CR-1
```

---

# 12. No Cross-Arrangement Evidence Reuse

The same Time Capture Event or Approved Worked-Time Evidence MUST NOT silently contribute to multiple Scheduling Arrangements or multiple Compensation Relationships.

If real work must be apportioned:

```text
explicit attributable evidence
```

is required.

No automatic:

```text
50% CR-1
50% CR-2
```

allocation is authorised.

Exact multi-relationship time allocation remains downstream unless demonstrated necessary.

---

# 13. Leave Is Arrangement-Scoped

A Leave Request SHALL reference one exact Workforce Scheduling Arrangement.

Therefore:

```text
Leave under Arrangement A
    ≠ automatically Leave under Arrangement B
```

This matters when one person simultaneously has:

```text
employee Arrangement
+
independent-contractor Arrangement
```

with the same merchant.

---

# 14. Multi-Arrangement Leave UX

Main Street MAY provide one merchant-facing action such as:

```text
Book 14–18 September off
```

that coordinates leave requests over multiple applicable Arrangements.

But each resulting Leave Request/Decision remains independently owner-qualified.

UI convenience MUST NOT create one universal person-wide Leave authority.

---

# 15. Approved Leave Evidence

Approved Leave Evidence MUST retain the exact Workforce Scheduling Arrangement.

Where compensation applies:

```text
Approved Leave
      ↓
Arrangement
      ↓
Compensation Relationship
      ↓
Workforce Compensation
```

This prevents paid leave from being applied to the wrong Compensation Relationship.

---

# 16. Cross-Arrangement Scheduling Conflicts

Main Street SHALL NOT establish a universal rule that every overlapping Arrangement is invalid.

Examples such as:

```text
on-call responsibility
supervisory duty
parallel non-exclusive work
```

may justify overlap.

However, Workforce Scheduling MUST preserve enough identity evidence to detect cross-Arrangement overlap.

The exact overlap/rejection policy remains deferred.

Main Street MUST NOT silently assume that overlapping schedules are safe.

---

# 17. Break Ownership

Canonical ownership remains:

```text
Workforce Scheduling
    → Scheduled Break

Timekeeping
    → Actual Break evidence

Workforce Compensation
    → monetary consequence

Payroll
    → applicable Payroll execution
```

---

# 18. Remove `Paid Break Entitlement`

`Paid Break Entitlement` SHALL NOT remain a canonical generic concept.

Instead:

```text
Scheduled Break evidence
+
Actual Break evidence where applicable
+
Compensation Terms Revision
+
jurisdiction rules
        ↓
break-related Compensation consequence/input
```

Workforce Compensation owns the monetary consequence.

---

# 19. Break Pay Invariants

```text
Scheduled Break
    ≠ Actual Break
    ≠ Worked Time
    ≠ break-related Compensation consequence
```

For:

```text
Scheduled Break = 30m
Actual Break    = 45m
```

Timekeeping establishes:

```text
Actual Break = 45m
Excess Break = 15m
```

Worked Time excludes the actual 45 minutes.

If applicable Compensation Terms pay the scheduled 30-minute break:

```text
30m
```

may contribute separately to Compensation.

The additional 15 minutes does not become:

```text
Worked Time
or
additional break compensation
```

merely because it occurred.

---

# 20. Salary Boundary

Where fixed/salaried compensation already incorporates the applicable break treatment:

```text
break evidence
```

MUST NOT cause duplicate additional compensation.

Excess break remains attendance evidence unless applicable Compensation Terms/jurisdiction establish another lawful consequence.

---

# 21. Agreement Evidence and Cross-Capability Atomicity

An agreement MAY contain:

```text
Workforce Time Terms
+
Compensation Terms
```

and both owner authorities MAY reference the same accepted Agreement evidence.

But Main Street MUST NOT assume:

```text
agreement accepted
    → scheduling + compensation mutations
      must always commit atomically
```

MS-PROT-072 governs cross-capability consistency.

Atomicity is permitted only where a separately accepted business invariant requires all participating effects to succeed/fail together.

Otherwise:

```text
Agreement evidence
    ↓
owner-qualified establishment/progression
```

may proceed through normal bounded orchestration/retry semantics.

---

# 22. No Universal Agreement Aggregate

Agreement generation/acceptance remains evidence and governed input.

It MUST NOT become a hidden aggregate owning:

```text
Workforce Scheduling Arrangement
Compensation Relationship
Workforce Time Terms
Compensation Terms
```

---

# 23. Notification Authority

No MS-PROT-075 amendment is required by this amendment.

Its existing rule remains valid:

> Notification may deliver only content the recipient/context could legitimately receive under the applicable authority.

MS-PROT-074 v1.1 permits purpose-bound own-workforce notification payloads.

Exact:

```text
shift
time
leave
```

Notification Contracts remain deferred under:

```text
MS-PROT-081-DQ-015
```

Implementation MUST NOT invent them before that deferred item is promoted.

---

# 24. API Surface

No MS-PROT-035 amendment is accepted by this amendment.

Exact Personal Workforce Self-Service API/UI remains:

```text
MS-PROT-081-DQ-013
```

An implementation MUST NOT map personal self-service onto `MERCHANT_OPERATIONAL` merely to reuse an existing API surface if doing so would incorrectly require or bypass Merchant Operational Device Context.

---

# 25. Additional Deferred Questions

```text
MS-PROT-081-DQ-021
Exact cross-Arrangement scheduling-overlap policy.

MS-PROT-081-DQ-022
Exact retrospective time/compensation attribution mechanism
when historical workforce evidence had no Compensation
Relationship affinity.
```

Neither question permits silent guessing.

---

# 26. Acceptance Statement

> **Merchant Membership identifies a person's workforce relationship with a merchant, but it is not sufficiently precise to identify every distinct way that person may be scheduled or compensated. Main Street therefore uses a narrow Workforce Scheduling Arrangement to bind one coherent scheduling/time/leave rule set to a Merchant Membership and, where applicable, one exact Compensation Relationship. Shift Offers, Scheduled Work Commitments, Time Capture Events, Approved Worked-Time Evidence and Leave retain exact Workforce Scheduling Arrangement affinity. Main Street does not discover the applicable compensation relationship later by searching the person's current state.**