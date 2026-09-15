# MS-PROT-042 v1.7 — Workforce Availability Constraint Interoperability Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.7  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact final Workforce Scheduling, Timekeeping, Leave & Compensation authority package presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Amends:** Composite MS-PROT-042 only within workforce-availability evidence interoperability for explicitly linked schedulable Resources  
**Purpose:** Permit Appointment Scheduling to consume owner-qualified workforce schedule/leave evidence for explicitly linked schedulable Resources without merging Workforce Scheduling with Appointment Scheduling or allowing workforce changes to silently rewrite customer commitments.

---

# 1. Amendment Scope

MS-PROT-042 remains the authority for Appointment Scheduling.

MS-PROT-081 remains the authority for workforce scheduling.

No universal Schedule aggregate is introduced.

---

# 2. Explicit Resource Link Required

Workforce availability evidence MAY constrain Appointment Scheduling only where there is an explicit accepted linkage between:

```text
Merchant Membership
and
capability-owned schedulable Resource
```

Identity similarity/name matching MUST NOT create that linkage.

---

# 3. Workforce Evidence as Appointment Constraint Evidence

Where explicitly linked and semantically applicable, Appointment Scheduling MAY consume current owner-qualified evidence such as:

```text
Scheduled Work Commitment
Approved Leave
other accepted Workforce availability evidence
```

as Scheduling Constraint Evidence.

MS-PROT-042 continues to own the resulting Appointment Scheduling Evaluation.

---

# 4. Example

```text
Sarah
Merchant Membership
    ↓
Workforce shift:
09:00–17:00

Explicit linked Resource:
Stylist Sarah
    ↓
Appointment Scheduling
may offer appointments within
the applicable work availability
```

If:

```text
Approved Leave:
Tuesday
```

then Tuesday workforce unavailability may constrain new Appointment commitments.

---

# 5. Existing Appointment Is Not Silently Cancelled

A later:

```text
shift cancellation
leave approval
workforce schedule change
```

MUST NOT silently cancel or rewrite an existing Appointment commitment.

If new workforce evidence conflicts with an existing Appointment:

```text
conflict/remediation is required
```

according to the applicable owning authorities.

Existing customer commitment truth remains explicit.
