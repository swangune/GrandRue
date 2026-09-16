# MS-PROT-074 v1.2 — Membership-Relative Personal Workforce Self-Service Authorisation Amendment

**Document ID:** MS-PROT-074  
**Version:** 1.2  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact final Workforce Corpus Conflict-Correction Package presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Amends:** Composite MS-PROT-074 v1.0–v1.1 within membership-relative personal workforce self-service authorisation and historical downstream-scope qualification  
**Purpose:** Define explicit own-subject Actor Authorisation for bounded personal workforce self-service without creating an implicit merchant role or weakening merchant-operational device/role boundaries.

---

# 1. Membership-Relative Self-Service Actor Authorisation

For an operation explicitly registered as own-subject Workforce Self-Service:

```text
authenticated Identity
+
ACTIVE Merchant Membership belonging to that Identity
+
ACTIVE Workforce Scheduling Arrangement
    where the operation is Arrangement-specific
+
resolved Merchant Scope
+
Personal Workforce Self-Service Context
+
exact own-subject binding
+
capability-owned operational eligibility
```

MAY establish Actor Authorisation.

A Merchant Role Assignment is not required merely for these own-subject operations.

---

# 2. This Is Not an Implicit Role

Membership-relative self-service authority:

```text
is not Merchant Role Assignment
is not a default staff role
is not delegable
is not merchant-wide authority
```

It exists only because the requested operation concerns the exact authenticated person's own bounded workforce relationship.

---

# 3. Eligible Self-Service Families

Subject to owning capability policy, the initial family MAY include:

```text
view own schedules
view own Shift Offers
accept/reject own Shift Offer
clock own time
record own break
view own time
submit own time correction
request own leave
withdraw own pending leave where permitted
view own leave decisions
```

Each Arrangement-specific operation must resolve the exact Arrangement.

---

# 4. Cross-Subject Operations

The following remain merchant-operational:

```text
schedule someone else
view team rota
approve another person's time
correct another person's time
approve another person's leave
manage workforce policy
```

They continue to require applicable:

```text
Actor Authorisation
Merchant Role Assignment
Staff Operational Context
Merchant Operational Device Context
```

---

# 5. Suspension and Termination

`SUSPENDED` or `ENDED` Merchant Membership MUST NOT authorise new current Workforce Self-Service mutations.

An ENDED Scheduling Arrangement likewise cannot authorise new Arrangement-specific activity.

Session continuity MUST NOT freeze Membership or Arrangement state.

---

# 6. Compensation Access Is Separately Authorised

Hard invariant:

```text
Merchant Membership
+
Personal Workforce Self-Service Context
    ≠ automatic access to Compensation
```

Compensation Relationship authority remains MS-PROT-080-owned.

---

# 7. Historical Downstream Scope Qualification

The historical MS-PROT-074 v1.0 phrase:

```text
staff scheduling / rota / payroll / HR semantics
```

is now qualified as:

```text
Workforce Scheduling / rota
    resolved by MS-PROT-081

Workforce Compensation / Payroll
    resolved by MS-PROT-080

general HR semantics
    remain outside accepted authority
```

The historical base document need not be destructively rewritten.

---

# 8. Acceptance Statement

> **Personal Workforce Self-Service authority is relationship-derived and own-subject only; it is not a default merchant role. Merchant-wide workforce administration remains governed by normal Role Assignment and Merchant Operational Device Context. Merchant Membership and Personal Workforce Self-Service Context do not independently authorise Compensation access.**