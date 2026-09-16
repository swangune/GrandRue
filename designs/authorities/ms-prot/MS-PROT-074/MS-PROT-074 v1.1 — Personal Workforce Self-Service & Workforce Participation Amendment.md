# MS-PROT-074 v1.1 — Personal Workforce Self-Service & Workforce Participation Amendment

**Document ID:** MS-PROT-074  
**Version:** 1.1  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact final Workforce Scheduling, Timekeeping, Leave & Compensation authority package presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Amends:** MS-PROT-074 v1.0 within personal-device workforce participation and personal workforce self-service scope  
**Purpose:** Expand the permitted personal-device staff surface to bounded personal workforce self-service without converting personal devices into general merchant-operational contexts or changing Merchant Membership into legal employment, Payroll or compensation authority.

---

# 1. Amendment Scope

MS-PROT-074 v1.0 survives except where this amendment expands the permitted personal-device workforce surface.

The existing distinction:

```text
Membership Security Projection
        ≠
Merchant Operational Projection
```

shall become:

```text
Membership Security Projection

Personal Workforce Self-Service Projection

Merchant Operational Projection
```

These remain distinct.

---

# 2. Personal Workforce Self-Service Is Not Merchant Operational Access

A staff member MAY use a personal/non-operational device for their own workforce participation.

Permitted scope MAY include:

```text
view own schedule
view own shift offers
accept/reject own shift offers
clock own time where merchant policy permits
record own break events
view own time record
submit own time correction
request own leave
view own leave decisions
view permitted own pay/compensation documents
```

This SHALL NOT establish general Merchant Operational Device Authorisation.

---

# 3. Personal Self-Service Cannot Manage Other Staff

A personal/non-operational staff context MUST NOT permit:

```text
schedule another worker
see another worker's schedule
approve another worker's time
approve/reject another worker's leave
view merchant-wide rota
view merchant customers
view orders
view appointments
view inventory
view financial reports
manage merchant configuration
```

unless the device separately has the required Merchant Operational Device Context and the actor has applicable authority.

---

# 4. Merchant Membership Remains Non-Legal

Merchant Membership continues to mean workforce-access/participation relationship only.

It SHALL NOT assert:

```text
employee
self-employed
contractor
Payroll Participant
leave eligibility
pay entitlement
```

---

# 5. Staff Resource Separation Survives

The existing invariant remains:

```text
Merchant Membership
    ≠
schedulable customer-facing Resource
```

A separately accepted explicit linkage MAY allow Appointment Scheduling to consume Workforce availability evidence without merging those identities.

---

# 6. Notification Amendment to MS-PROT-074 Boundary

The prior restriction preventing operational payloads on private devices is narrowed:

```text
merchant operational payload
    remains prohibited

own Personal Workforce Self-Service payload
    may be delivered
```

Examples permitted:

```text
"You have a shift offer for Monday."
"Your Tuesday shift changed."
"Your leave request was approved."
"Your timecard needs attention."
```

Payload MUST remain purpose-bound and must not expose unrelated merchant information.

MS-PROT-075 remains the cross-cutting notification authority.

---

# 7. Cross-Authority Boundary

This amendment does not transfer ownership of Workforce Scheduling, Timekeeping, Leave or Compensation into MS-PROT-074.

Canonical separation remains:

```text
Merchant Membership
    ≠ Workforce Time Terms
    ≠ Compensation Relationship
    ≠ Workforce Scheduling
    ≠ Timekeeping
    ≠ Leave
    ≠ Payroll
```

MS-PROT-081 owns Workforce Scheduling, Timekeeping, Break and Leave semantics. MS-PROT-080 owns Workforce Compensation and Payroll consequences. Composite MS-PROT-063 owns trusted context establishment.

---

# 8. Acceptance Statement

> **A staff member may use a personal device for purpose-bound participation in their own schedule, time, leave and permitted compensation-document workflows without that device becoming a general merchant-operational device. Merchant-wide workforce administration and unrelated merchant operations remain subject to the existing Staff Operational Context and Merchant Operational Device Authorisation boundary.**