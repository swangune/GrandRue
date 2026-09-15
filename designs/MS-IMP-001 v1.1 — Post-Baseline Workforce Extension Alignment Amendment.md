# MS-IMP-001 v1.1 — Post-Baseline Workforce Extension Alignment Amendment

**Document ID:** MS-IMP-001  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** Manual approval on 5 September 2026 after comparison against actual implementation progress  
**Authority type:** Implementation-programme governance amendment  
**Governs:** Production implementation sequencing and implementation-node eligibility within this amendment scope  
**Subordinate to:** Accepted Main Street semantic/design/TAS/ADR authority  
**Executed through:** `designs/IMPLEMENTATION-RULES.md`  
**Amends:** MS-IMP-001 v1.0 only within IMP-08B scope qualification, IMP-11 scope qualification, post-baseline Workforce Scheduling implementation sequencing, Workforce Compensation/Payroll activation boundaries and downstream hardening consequences  
**Preserves:** all MS-IMP-001 v1.0 macro targets, dependencies, programme gates, completion evidence and rules outside this amendment scope; the current IMP-07 fine-grained graph; historical implementation evidence  
**Depends on:** MS-IMP-001 v1.0; MS-PROT-079 v1.0 + v1.1; composite MS-PROT-074 through v1.2; composite MS-PROT-080 through v1.3; composite MS-PROT-081 through v1.1; composite MS-PROT-042 through v1.8; current `AUTHORITY-INDEX.md`; current `IMPLEMENTATION-RULES.md`  
**Purpose:** Add the later accepted Workforce Scheduling and Workforce Compensation scope to implementation governance without redefining already implemented workforce-access or customer-appointment responsibilities and without interrupting the active Publication → Enquiry programme frontier.

---

## 1. Current Programme Frontier Is Preserved

At acceptance of this amendment, the active implementation programme is IMP-07 Publication → Enquiry.

The current fine-grained IMP-07 graph remains authoritative implementation evidence/navigation for that macro target.

This amendment does **not**:

```text
reopen IMP-01..IMP-06
reopen completed IMP-07 children
change Publication P1–P3 completion
make Workforce scope a prerequisite of Publication P4/P5
move Enquiry dependencies
promote a later workforce node ahead of the current READY Publication work
```

The existing implementation frontier SHALL continue according to its current graph unless an independent implementation/design escalation changes it.

---

## 2. IMP-08B Scope Qualification

MS-IMP-001 v1.0 section 15 SHALL be read as:

> **IMP-08B — Workforce Access & Delegation Foundation**

Its scope remains the already-defined access/delegation responsibility:

```text
Merchant Membership
invitation
roles / groups
delegation
Merchant Operational Device Authorisation / context
staff principal establishment
staff attribution
Audit integration
personal workforce self-service security foundations where accepted
```

IMP-08B does **not** include:

```text
Workforce Scheduling Arrangement
Shift Offer
Scheduled Work Commitment
clock-in / clock-out
Timekeeping evidence
Worked Time
Leave Request / Leave Decision
Compensation Relationship
Workforce Compensation
Payroll
```

This is a qualification of existing scope, not an expansion of its completion criteria.

Historical evidence referring to `IMP-08B — Workforce Foundation` remains valid evidence for the narrower access/delegation scope that governed when it was produced.

---

## 3. IMP-11 Scope Qualification

MS-IMP-001 v1.0 section 19 SHALL be read as:

> **IMP-11 — Customer Booking / Appointment Scheduling Vertical Slice**

Its scope remains:

```text
Business Hours
Calendar
customer/merchant Appointment Scheduling evaluation
Booking
Appointment
customer-facing availability
concurrent appointment/booking commitment protection
rescheduling / cancellation where accepted
projections
Exposure
customer / merchant APIs
```

`Scheduling` in IMP-11 means the Booking/Appointment scheduling responsibility governed by the accepted Booking/Appointment/Scheduling authority.

IMP-11 does **not** own:

```text
workforce shifts
Workforce Scheduling Arrangement
workforce scheduling offers/acceptance
workforce attendance/timekeeping
worked-time evidence
leave
Workforce Compensation
Payroll
```

Workforce evidence may constrain Appointment Scheduling only through the explicit accepted interoperability contract. Such interoperability does not merge the macro targets.

---

## 4. IMP-WF-01 — Workforce Scheduling / Timekeeping / Leave Extension

The accepted WF-EXT design scope is represented in implementation governance by a new macro extension:

> **IMP-WF-01 — Workforce Scheduling, Timekeeping, Break & Leave Vertical Slice**

### 4.1 Core dependency

```text
IMP-08B ──HARD──> IMP-WF-01
```

IMP-WF-01 cannot be implemented correctly until the relevant Merchant Membership/workforce-access foundation exists.

No duplicate direct HARD dependency on IMP-04, IMP-06 or IMP-07 is required because those requirements are already satisfied transitively through the accepted macro graph before IMP-08B can complete.

### 4.2 Scope

IMP-WF-01 includes, as applicable under composite MS-PROT-081:

```text
Workforce Scheduling Arrangement persistence and lifecycle
Workforce Time Terms
Shift Offer
worker decision where applicable
Scheduled Work Commitment
Scheduled Break representation
Timekeeping evidence
clock-in / clock-out or equivalent accepted evidence capture
Actual Break evidence
Worked Time derivation
Leave Request / Leave Decision / leave evidence
own-subject worker projections and operations
merchant operational administration
concurrency / revision / idempotency rules
historical arrangement affinity
```

The target MUST preserve:

```text
Merchant Membership
    ≠ Workforce Scheduling Arrangement

Scheduled Work Commitment
    ≠ Attendance
    ≠ Worked Time

Appointment Scheduling
    ≠ Workforce Scheduling
```

---

## 5. IMP-WF-01 Child-Specific Conditional Dependencies

The macro target MUST NOT acquire broad upstream dependencies merely because optional child paths can use them.

The fine-grained graph SHALL derive dependencies per child node.

The following are conditional examples:

```text
IMP-08C ──CONDITIONAL──> IMP-WF-01 child
    where durable reactions, timers, reminders, retries or background work are required

IMP-09 ──CONDITIONAL──> IMP-WF-01 child
    where an external workforce/time/attendance provider is used

IMP-10 ──CONDITIONAL──> IMP-WF-01 child
    where Notification provider execution is required

IMP-11 ──CONDITIONAL──> interoperability child
    where Workforce availability evidence constrains Appointment Scheduling
```

The absence of one optional provider/notification/interoperability path MUST NOT block a conforming internal Workforce Scheduling path whose accepted semantics do not require it.

---

## 6. Workforce Compensation / Payroll Implementation Extension

Composite MS-PROT-080 establishes accepted semantic scope, but this amendment does **not** collapse all of that scope into one immediately executable macro target.

The implementation family is reserved as:

> **IMP-WC — Workforce Compensation / Jurisdiction Pay Treatment / Payroll Extension Family**

Current state:

```text
accepted semantic scope       YES
implementation family known   YES
single executable macro       NOT FROZEN
current MVP readiness         NO
Payroll sequencing            POST-MVP unless separately reprioritised
```

Before IMP-WC becomes READY, implementation governance MUST derive and approve a decomposition that does not accidentally make simple compensation tracking depend on jurisdictional Payroll infrastructure.

At minimum the decomposition review MUST distinguish:

```text
Compensation core
    Compensation Relationship
    Compensation Terms
    Compensation Amount
    approval
    ledger / evidence / documents where applicable

from

jurisdictional administration
    pay-treatment resolution
    Payroll calculation
    Non-Payroll payer obligations
    statutory filing / reporting
    remittance
    jurisdiction-specific documents
    regulatory reconciliation
```

This section reserves the implementation family; it does not authorise automatic production implementation of unresolved IMP-WC children.

---

## 7. No Universal Workforce-Scheduling Dependency for Compensation

IMP-WC MUST NOT receive a universal HARD dependency on IMP-WF-01.

Canonical:

```text
project milestone compensation
fixed project compensation
negotiated progress compensation
commission / other supported non-time basis
        may be implementable without Workforce Scheduling
```

A particular compensation child SHALL depend on IMP-WF-01 only where it consumes authoritative Workforce Scheduling/Timekeeping/Leave evidence, for example:

```text
Approved Payable Time
Worked Time
leave evidence
break-related evidence where the jurisdiction/terms require it
```

The edge in that case is child-specific and semantic-purpose-specific.

---

## 8. Downstream Hardening / Delivery Consequences

Because IMP-WF-01 is accepted before the current programme has reached IMP-16, it SHALL participate naturally in the later cross-system completion gates rather than requiring those gates to be reopened after completion.

Accordingly, MS-IMP-001 v1.0 section 24 is amended within this scope so that IMP-16 cannot be considered complete while an in-scope IMP-WF-01 remains incomplete.

The effective IMP-16 prerequisite set therefore includes:

```text
IMP-08A COMPLETE
IMP-08B COMPLETE
IMP-08C COMPLETE
IMP-10 COMPLETE
IMP-11 COMPLETE
IMP-12 COMPLETE
IMP-13 COMPLETE
IMP-14 COMPLETE
IMP-15 COMPLETE
IMP-WF-01 COMPLETE
```

IMP-WF-01 is then subject, as applicable, to the same later programme responsibilities for:

```text
IMP-16 cross-system lifecycle/historical hardening
IMP-17 production API completion
IMP-18 production operability
IMP-19 recovery
IMP-20 controlled live-readiness
```

IMP-WC is different because it remains post-MVP/not activated. It is therefore **not** silently added to the current IMP-16..IMP-20 completion prerequisites.

If IMP-WC is later activated after those original gates have completed, its approved implementation decomposition MUST include equivalent lifecycle/API/operability/recovery/live-readiness proof appropriate to its scope before that extension is considered production-ready.

---

## 9. Effective Macro Graph Addition

The existing MS-IMP-001 v1.0 graph remains in force, with this additional branch:

```text
                    IMP-07
                      │
                      ├──────── existing branches ────────►
                      │
                      ▼
                   IMP-08B
                      │
                      │ HARD
                      ▼
                 IMP-WF-01
                      │
                      └──────────────► participates in IMP-16..IMP-20

IMP-08C / IMP-09 / IMP-10 / IMP-11
    └── conditional edges only to the exact IMP-WF-01 child that uses them

IMP-WC
    accepted future extension family
    POST-MVP / NOT_READY until approved decomposition
```

The existing IMP-07 → IMP-08B programme gate remains unchanged.

---

## 10. Historical Evidence Preservation

Implementation evidence is historical evidence, not mutable programme prose.

Therefore existing files that record:

```text
IMP-08B — Workforce Foundation
IMP-11 — Scheduling Vertical Slice
```

MUST NOT be rewritten merely to use the new qualified names.

Their meaning is interpreted through MS-IMP-001 v1.0 composed with this v1.1 amendment.

A historical evidence record is changed only if that record itself contains a factual defect that requires a separately justified correction.

---

## 11. Implementation-Rules Interaction

`IMPLEMENTATION-RULES.md` retains its existing role:

```text
MS-IMP-001 macro eligibility
        ↓
dynamic fine-grained graph
        ↓
smallest READY child
```

It MAY split IMP-WF-01 into smaller implementation nodes and derive child-specific dependency edges under its existing automatic graph-refinement authority.

It MUST NOT:

```text
merge IMP-WF-01 into IMP-08B
merge IMP-WF-01 into IMP-11
activate IMP-WC automatically
invent an IMP-WC macro decomposition that changes programme gates
make every compensation child depend on IMP-WF-01
```

Any such material macro change requires the applicable approval lifecycle.

No change to `IMPLEMENTATION-RULES.md` itself is required by this amendment.

---

## 12. Falsification Results

| Candidate implementation treatment | Result | Reason |
|---|---|---|
| Expand IMP-08B to include rota/timekeeping/leave | REJECTED | Moves an already implemented/partially evidenced completion boundary. |
| Treat IMP-11 as generic scheduling including staff shifts | REJECTED | Existing design/implementation is Booking/Appointment-oriented. |
| Give IMP-WF-01 HARD dependencies on every earlier foundation | REJECTED | Redundant transitive edges over-constrain the macro graph. |
| Make Notification/provider interoperability mandatory for all workforce scheduling | REJECTED | Those are path-specific concerns. |
| Make all compensation depend on IMP-WF-01 | REJECTED | Non-time compensation mechanisms do not require scheduling/time evidence. |
| Put Payroll automatically into the current MVP graph | REJECTED | Accepted sequencing keeps Payroll post-MVP unless separately promoted. |
| Ignore IMP-WF-01 until after IMP-16..IMP-20 and reopen those gates later | REJECTED | The current programme has not reached those gates; the extension can participate naturally when they are executed. |
| Rewrite historical implementation evidence to new names | REJECTED | Corrupts implementation-history provenance. |

---

## 13. Conformance Conditions

The implementation programme is conforming within this amendment scope only if all of the following remain true:

```text
current IMP-07 frontier unchanged by this amendment

IMP-08B = Workforce Access & Delegation Foundation
IMP-08B ≠ Workforce Scheduling / Timekeeping / Leave

IMP-11 = Customer Booking / Appointment Scheduling
IMP-11 ≠ Workforce Scheduling

IMP-WF-01 is a separate macro extension
IMP-WF-01 HARD-depends on IMP-08B
optional provider/notification/appointment interoperability is child-specific

IMP-WC is not automatically READY
Payroll remains post-MVP unless separately reprioritised
Workforce Compensation does not universally depend on Workforce Scheduling

historical evidence remains historical evidence
later extension scope does not retroactively invalidate prior conforming work
```

---

## 14. Final Accepted Decision

> **Main Street shall preserve the active IMP-07 implementation frontier and the historical meaning of its original implementation graph. IMP-08B is qualified as Workforce Access & Delegation; IMP-11 is qualified as Customer Booking / Appointment Scheduling. Workforce Scheduling, Timekeeping, Break and Leave are implemented through a separate IMP-WF-01 extension that depends on IMP-08B and uses provider/notification/Appointment dependencies only where an exact child path requires them. Workforce Compensation/Payroll remains a separate post-baseline implementation family; it must not universally depend on Workforce Scheduling, and its compensation-core versus jurisdictional-Payroll decomposition must be approved before automatic implementation.**
