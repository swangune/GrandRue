# MS-PROT-042 v1.8 — Arrangement-Qualified Workforce Availability Interoperability Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.8  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact final Workforce Corpus Conflict-Correction Package presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Amends:** Composite MS-PROT-042 through v1.7 within Workforce availability evidence affinity to customer-facing schedulable Resources  
**Purpose:** Refine Workforce-to-Appointment Scheduling interoperability so workforce availability constrains a customer-facing schedulable Resource only through an explicit Workforce Scheduling Arrangement link rather than Merchant Membership or Identity similarity alone.

---

# 1. Arrangement-Qualified Resource Link

The existing workforce/Resource link is refined.

A bare:

```text
Merchant Membership
    ↔
schedulable Resource
```

is insufficient where the Membership has multiple Scheduling Arrangements.

The effective link SHALL identify:

```text
Workforce Scheduling Arrangement
    ↔
capability-owned schedulable Resource
```

while retaining Membership identity as workforce provenance.

---

# 2. Appointment Constraint Evidence

Appointment Scheduling MAY consume only workforce evidence belonging to an explicitly linked Workforce Scheduling Arrangement.

Example:

```text
Alex Membership

Arrangement A
    stylist
    Resource = Stylist Alex

Arrangement B
    training consultant
    no Stylist Resource affinity
```

Only Arrangement A may constrain the Stylist Resource's Appointment Scheduling Evaluation.

Arrangement B's leave or shift state MUST NOT silently block stylist appointments.

---

# 3. Multiple Explicit Arrangement Links

A schedulable Resource MAY consume more than one Scheduling Arrangement only where each linkage is explicit and semantically valid.

No:

```text
same Identity
same display name
same Membership
```

shortcut may create additional linkage.

---

# 4. Existing Appointment Commitments Remain Protected

MS-PROT-042 v1.7's existing rule survives:

```text
later Workforce change
    ≠ silent Appointment cancellation
```

This amendment changes only the precision of workforce-evidence affinity.

---

# 5. Acceptance Statement

> **Appointment Scheduling may consume workforce availability only from explicitly linked Workforce Scheduling Arrangements, not merely from a shared Merchant Membership or Identity. Later workforce changes remain incapable of silently rewriting existing Appointment commitments.**