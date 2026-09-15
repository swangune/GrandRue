# MS-PROT-079 v1.1 — Post-Baseline Design Extension & Scheduling Scope Qualification Amendment

**Document ID:** MS-PROT-079  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** Manual approval on 5 September 2026 after implementation-progress comparison and conflict review  
**Authority type:** Product/design sequencing amendment  
**Governed by:** `DESIGN-RULES.md` v2.1 and `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-079 v1.0 only within Target-11 scope qualification, post-baseline design-extension handling and implementation-bridge consequences  
**Preserves:** the original Targets 1–21, their historical closure order, their accepted substantive design packages and all surviving v1.0 rules outside this amendment scope  
**Depends on:** MS-PROT-079 v1.0; composite MS-PROT-074 through v1.2; composite MS-PROT-080 through v1.3; composite MS-PROT-081 through v1.1; composite MS-PROT-042 through v1.8; current `AUTHORITY-INDEX.md`  
**Purpose:** Preserve the completed original backend-design programme while making later accepted Workforce Scheduling and Workforce Compensation scope explicit without retroactively redefining Target 11 or invalidating implementation evidence produced against the original programme.

---

## 1. Governing Decision

The original MS-PROT-079 Targets 1–21 remain the authoritative historical backend-design completion baseline.

Later accepted design scope SHALL be represented as a **Post-Baseline Design Extension** where it adds a materially new capability responsibility that was not part of the exact original target meaning.

A Post-Baseline Design Extension MUST NOT silently:

```text
rewrite a closed original target
change the historical meaning of completed design evidence
invalidate conforming implementation merely because later scope exists
move an earlier completion boundary
collapse a later capability into a similarly named earlier target
```

The governing relationship is:

```text
MS-PROT-079 v1.0 Targets 1–21
        = original backend-design completion baseline

later accepted capability authority
        +
explicit extension qualification
        =
Post-Baseline Design Extension
```

---

## 2. Target 11 Is Customer Booking / Appointment Scheduling

MS-PROT-079 v1.0 Target 11 SHALL be read as:

> **Target 11 — Customer Booking / Appointment Scheduling**

Its original references to `Scheduling` mean the customer/merchant appointment-scheduling responsibility composed with Booking, Appointment, Business Hours, Calendar and applicable Resource availability/allocation evidence.

Target 11 does **not** own or absorb:

```text
Workforce Scheduling Arrangement
Shift Offer
Scheduled Work Commitment
workforce shift acceptance/decline
Workforce Time Terms
Timekeeping evidence
clock-in / clock-out evidence
worked-time evidence
workforce break evidence
Leave Request / Leave Decision
Workforce Compensation
Payroll
```

Those responsibilities are governed by their later accepted owners.

This qualification preserves the accepted Booking/Appointment ownership distinctions and the existing Target-11 design package. It does not reopen Target 11.

---

## 3. Workforce Access Is Not Workforce Scheduling

Merchant Membership, merchant Role/Group delegation, workforce self-service authorisation and Merchant Operational Device Authorisation are workforce-access responsibilities governed principally by composite MS-PROT-074 and related Identity/Security authority.

They are distinct from Workforce Scheduling.

Canonical:

```text
Merchant Membership / workforce access
        ≠
Workforce Scheduling Arrangement
        ≠
Compensation Relationship
```

The existence of workforce-access foundations therefore does not establish that Workforce Scheduling, Timekeeping, Leave or Compensation was already part of the original MS-PROT-079 workforce-access design responsibility.

---

## 4. WF-EXT — Workforce Scheduling / Timekeeping / Leave

Composite MS-PROT-081 through v1.1 is registered under this sequencing authority as:

> **WF-EXT — Workforce Scheduling / Timekeeping / Break & Leave Design Extension**

WF-EXT is a Post-Baseline Design Extension.

It includes, within MS-PROT-081 authority:

```text
Workforce Scheduling Arrangement
Workforce Time Terms
Shift Offers
Scheduled Work Commitments
scheduled breaks
Timekeeping evidence
worked-time derivation
leave requests / decisions / evidence
arrangement-qualified interoperability
```

WF-EXT is not Target 11 and MUST NOT be treated as a Target-11 reopening.

Where WF-EXT supplies accepted owner-qualified evidence to Appointment Scheduling, the integration is exactly the interoperability permitted by composite MS-PROT-042 through v1.8 and does not transfer Workforce or Appointment ownership.

---

## 5. WC-EXT — Workforce Compensation / Payroll

Composite MS-PROT-080 through v1.3 is registered under this sequencing authority as:

> **WC-EXT — Workforce Compensation, Jurisdiction Pay Treatment & Payroll Design Extension**

WC-EXT is a Post-Baseline Design Extension.

It includes the accepted generic Workforce Compensation boundary and its Payroll / Non-Payroll routes, Compensation Relationships, Compensation Terms, Compensation Amounts, jurisdiction-qualified pay treatment, Compensation Documents and related execution semantics.

WC-EXT MUST NOT be collapsed into WF-EXT merely because some compensation calculations may consume worked-time or leave evidence.

Canonical:

```text
Workforce Scheduling / Timekeeping / Leave
        ≠
Workforce Compensation
        ≠
Payroll / jurisdiction execution
```

A Compensation path that does not consume workforce scheduling/time evidence MUST NOT acquire a Workforce Scheduling dependency merely for sequencing convenience.

Payroll remains post-MVP unless a separately accepted product/programme decision reprioritises it.

---

## 6. Extension Dependency Rule

A Post-Baseline Design Extension SHALL depend only on the original baseline and other extensions actually required by its accepted semantics or implementation architecture.

Similarity of terminology is not a dependency.

Therefore:

```text
WF-EXT
    may depend on workforce-access / identity foundations
    may interoperate conditionally with Appointment Scheduling
    does not become part of Target 11

WC-EXT
    may consume WF-EXT evidence for applicable time/leave-based compensation
    does not universally depend on WF-EXT
```

Provider, Notification, background-work, Exposure, API and operational dependencies SHALL remain conditional unless the exact extension path requires them.

---

## 7. Historical Completion Preservation

Acceptance of WF-EXT or WC-EXT does not change the truth of earlier completion evidence.

In particular:

```text
closed MS-PROT-079 Targets 1–21
        remain closed

existing implementation evidence
        remains evidence against the scope that governed when produced

later extension authority
        creates future work
        not retroactive failure
```

A later extension may reveal a genuine contradiction in earlier authority or implementation. Where that occurs, the specific contradiction MUST enter the governed design/implementation escalation lifecycle.

Mere existence of later scope is not such a contradiction.

---

## 8. Implementation Bridge

MS-IMP-001 SHALL map these extensions into implementation sequencing without redefining the original implementation evidence.

The implementation programme MUST:

1. preserve the current active implementation frontier;
2. qualify existing macro targets where their names are broader than their actual implemented/accepted scope;
3. create a separate Workforce Scheduling implementation extension rather than expanding the existing Workforce Access foundation;
4. keep Customer Appointment Scheduling separate from Workforce Scheduling;
5. avoid a universal Workforce Scheduling dependency for Workforce Compensation;
6. keep Payroll post-MVP unless separately promoted; and
7. derive child-specific provider, notification, background, interoperability and transport dependencies only where the child path actually uses them.

---

## 9. Current-Frontier Non-Interference

At acceptance of this amendment, the active production implementation programme is in IMP-07 Publication → Enquiry.

This amendment does not reopen or resequence the active IMP-07 fine-grained graph.

In particular, it does not alter Publication P1–P3 completion or make Workforce scope a prerequisite of Publication public Exposure, Enquiry or their current descendants.

---

## 10. Falsification Results

The following candidate interpretations are rejected:

| Candidate | Result | Reason |
|---|---|---|
| Treat Workforce Scheduling as part of original Target 11 | REJECTED | Target-11 authority and implementation are Booking/Appointment-oriented; later MS-PROT-081 owns workforce scheduling facts. |
| Expand the existing Workforce Access foundation to include shifts/time/leave | REJECTED | Merchant Membership/roles/device access are already a distinct implemented responsibility and must not have their completion boundary moved retroactively. |
| Make all Workforce Compensation depend on Workforce Scheduling | REJECTED | Project/milestone/negotiated/fixed compensation can exist without workforce time evidence. |
| Treat later extensions as reopening Targets 1–21 | REJECTED | Later accepted scope does not invalidate historical closure by itself. |
| Ignore later accepted scope because the original backend programme closed | REJECTED | Accepted semantic authority still requires eventual implementation when product sequencing makes it eligible. |
| Place Payroll automatically into the current MVP graph | REJECTED | Current accepted product sequencing keeps Payroll post-MVP unless deliberately reprioritised. |

---

## 11. Conformance Conditions

Conforming downstream governance and implementation evidence MUST preserve all of the following:

```text
Target 11 = Customer Booking / Appointment Scheduling
Target 11 ≠ Workforce Scheduling

Workforce Access / Delegation ≠ Workforce Scheduling
Workforce Scheduling ≠ Workforce Compensation
Workforce Compensation ≠ Payroll-only

WF-EXT is post-baseline design scope
WC-EXT is post-baseline design scope

later extension ≠ retroactive invalidation
later extension ≠ automatic current readiness
```

Any document or implementation graph that violates these distinctions is non-conforming within this amendment scope.

---

## 12. Final Accepted Decision

> **MS-PROT-079 v1.0 remains the historical 21-target backend-design completion baseline. Target 11 is explicitly qualified as Customer Booking / Appointment Scheduling. Later accepted Workforce Scheduling and Workforce Compensation authority are Post-Baseline Design Extensions rather than retroactive expansions of closed targets. Their implementation dependencies must be derived from actual semantic and implementation needs, preserving historical evidence and the current implementation frontier.**
