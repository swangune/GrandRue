# MS-PROT-080 v1.3 — Workforce Compensation Affinity, Break Consequence & Self-Service Amendment

**Document ID:** MS-PROT-080  
**Version:** 1.3  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact final Workforce Corpus Conflict-Correction Package presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Amends:** Composite MS-PROT-080 v1.0–v1.2 within workforce-evidence affinity, break-related compensation consequences, compensation self-service relationship authority and stale Timekeeping/deferred wording  
**Purpose:** Ensure Workforce Compensation consumes exact arrangement/relationship-affined evidence, independently authorises compensation self-service, removes ambiguous Paid Break Entitlement terminology, and reconciles stale Timekeeping/deferred statements with accepted Workforce Scheduling authority.

---

# 1. Workforce Evidence Compensation Affinity

Where Approved Worked-Time Evidence or Approved Leave Evidence contributes to compensation, Workforce Compensation MUST preserve the exact Workforce Scheduling Arrangement affinity and the exact Compensation Relationship referenced by that Arrangement.

Main Street MUST NOT determine the applicable Compensation Relationship later by searching current person/Membership state.

Rejected:

```text
Alex worked 8h
    ↓
find Alex's current Compensation Relationship
```

Accepted:

```text
Approved Worked-Time Evidence W17
    ↓
Workforce Scheduling Arrangement A
    ↓
Compensation Relationship CR-1
    ↓
exact Compensation Terms Revision
```

---

# 2. No Silent Later Binding

If historical Workforce evidence had no Compensation Relationship affinity when it was established, it MUST NOT later become compensation evidence merely because someone subsequently attaches or creates a Compensation Relationship.

Any retrospective compensation attribution requires an explicit governed correction/establishment operation with provenance.

Historical meaning is not rewritten.

---

# 3. Compensation Access Is Not Membership Authority

Hard invariant:

```text
Merchant Membership
+
Personal Workforce Self-Service Context
    ≠ automatic access to Compensation
```

Compensation Relationship authority remains Workforce Compensation-owned.

---

# 4. Related-Payee Compensation Requirement

MS-PROT-080 SHALL define an owner-qualified requirement conceptually identified as:

```text
workforce-compensation / related-payee-compensation
```

It asks:

> Is the current trusted principal authoritatively related to the exact Payee and exact Compensation Relationship for the requested Compensation self-service purpose?

This is a relationship requirement.

It is NOT:

```text
blanket Exposure
Actor Authorisation by itself
Merchant Membership
Compensation Relationship itself
```

---

# 5. Natural-Person Payee

Where the Payee is the authenticated natural person, the requirement may resolve from accepted authoritative identity/Payee relationship evidence.

Exact persistence representation remains downstream.

Client matching of:

```text
name
email
phone
bank account
```

MUST NOT independently establish the relationship.

---

# 6. Current Staff Compensation Access

A current staff member MAY use the personal workforce application/session to request permitted own Compensation information only where all applicable requirements are separately satisfied:

```text
Personal Workforce Self-Service Context
+
exact related-payee Compensation requirement
+
applicable operation authority
+
privacy
+
Exposure
```

Membership contributes trusted staff context.

It is not Compensation authority.

---

# 7. Former Staff

Ending Merchant Membership MUST NOT itself destroy historical Compensation-document eligibility.

Historical Compensation access MAY remain semantically eligible where:

```text
exact related-payee Compensation requirement
+
retained Compensation history/document
+
applicable historical-read policy
+
privacy
+
Exposure
```

are satisfied.

However, the exact authentication/API/Surface/Audience mechanism for a former Payee remains deferred.

Former Compensation access MUST NOT restore:

```text
shift acceptance
clocking
leave requests
staff dashboard
workforce authority
```

---

# 8. Payee Without Merchant Membership

A Payee MAY have:

```text
Compensation Relationship
+
no Merchant Membership
```

Examples include:

```text
project contractor
consultancy company
supplier-like independent payee
```

Personal Workforce Self-Service Context MUST NOT be manufactured for such a Payee.

Generic Payee self-service remains separately governed.

---

# 9. Legal-Entity Payee

Where:

```text
Payee = legal entity
```

an authenticated human MUST NOT automatically satisfy:

```text
related-payee-compensation
```

A separately accepted representative-authority relationship is required.

Exact representative semantics remain deferred.

---

# 10. Break-Related Compensation Ownership

`Paid Break Entitlement` SHALL NOT remain a canonical generic concept.

Canonical ownership is:

```text
Workforce Scheduling
    → Scheduled Break

Timekeeping
    → Actual Break evidence

Workforce Compensation
    → break-related monetary consequence/input

Payroll
    → applicable Payroll execution
```

A Scheduled Break does not own monetary paid/unpaid status.

If UI presents:

```text
Paid break
Unpaid break
```

that representation MUST derive from applicable:

```text
Compensation Terms Revision
+
jurisdiction rules
```

and retain its source affinity.

---

# 11. Break-Related Compensation Calculation

Break-related Compensation consequence MAY derive from:

```text
Scheduled Break evidence
+
Actual Break evidence where applicable
+
Compensation Terms Revision
+
jurisdiction-qualified rules
```

The calculation MUST NOT universally assume:

```text
scheduled break duration
=
paid duration
```

---

# 12. Break Overrun

Example:

```text
Scheduled Break       30m
Actual Break          45m
```

Timekeeping determines:

```text
Actual Break = 45m
Excess Break = 15m
```

Worked Time excludes the actual break interval.

Compensation may determine a separate break-related compensation input corresponding to the applicable 30-minute scheduled break where terms/rules permit.

The extra 15m:

```text
≠ Worked Time
≠ additional break compensation
```

merely because it occurred.

---

# 13. Fixed Salary

Where fixed salary already incorporates the applicable break treatment, break evidence MUST NOT create a duplicate additional payment.

---

# 14. Payroll Input Snapshot Affinity

Where material, PayrollInputSnapshot MUST reference the exact:

```text
Approved Worked-Time Evidence
Workforce Scheduling Arrangement affinity
Compensation Relationship
Approved Leave Evidence
break-related Compensation input/consequence where applicable
other accepted Compensation inputs
```

used in calculation.

---

# 15. Timekeeping Cleanup

MS-PROT-080 v1.3 supersedes remaining stale phrases such as:

```text
future Timekeeping
Approved Payable Time
```

where current meaning is now governed by MS-PROT-081/MS-PROT-080 v1.2+.

Preferred distinction:

```text
Approved Worked-Time Evidence
+
non-work Compensation inputs where applicable
        ↓
Compensation Amount
```

---

# 16. MS-PROT-080-V11-DQ-005

The authority now records:

```text
MS-PROT-080-V11-DQ-005
RESOLVED
```

by composite:

```text
MS-PROT-081
+
MS-PROT-080 v1.2/v1.3
```

The source authority and DDR MUST agree.

---

# 17. MS-PROT-080-V11-DQ-013

DQ-013 SHALL become:

> Exact generic Payee Compensation Self-Service authentication, API/Surface/Audience representation and legal-entity representative-access mechanism.

Current Membership-bound staff self-service context is already governed by composite MS-PROT-063/MS-PROT-074.

Compensation relationship eligibility is governed by MS-PROT-080.

The unresolved part concerns:

```text
Payees without Merchant Membership
former Payees without current Membership
legal-entity representatives
exact API surface
exact Exposure audience/context if existing audiences are insufficient
```

---

# 18. Exposure Audience Boundary

No new Exposure audience is accepted in this amendment.

Current staff remain capable of participating as authorised actors within the accepted `MERCHANT` audience model, subject to purpose-bound context and Exposure contracts.

However:

```text
former staff
non-member contractor
legal-entity Payee representative
```

MUST NOT be forced into `MERCHANT`, `CUSTOMER` or `PUBLIC` merely for implementation convenience.

If the existing audience vocabulary cannot faithfully represent generic Payee self-service, MS-PROT-027/MS-PROT-049 MUST be amended before that functionality is implemented.

This remains part of DQ-013.

---

# 19. Generic Compensation/Payee API Surface

Exact generic Compensation/Payee self-service transport remains:

```text
MS-PROT-080-V11-DQ-013
```

No implementation may invent the unresolved authentication/API/Surface/Audience or legal-entity representative mechanism.

---

# 20. Acceptance Statement

> **Compensation access is independently relationship-qualified. Merchant Membership does not grant pay-document access, and an ended Membership does not automatically destroy historical Payee eligibility. Worked-time and leave compensation consume exact Workforce Scheduling Arrangement and Compensation Relationship affinity. Scheduled Break and Actual Break remain Workforce/Timekeeping facts; their monetary consequence belongs exclusively to Workforce Compensation, and no generic Paid Break Entitlement authority is introduced.**