# MS-PROT-063 v1.2 — Personal Workforce Self-Service Trusted Context Amendment

**Document ID:** MS-PROT-063  
**Version:** 1.2  
**Status:** **ACCEPTED by manual approval on 5 September 2026**  
**Approved:** Manual approval of the exact final Workforce Scheduling, Timekeeping, Leave & Compensation authority package presented in ChatGPT on 5 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** MS-DESIGN-RULES-001, MS-AVS-001  
**Amends:** Composite MS-PROT-063 v1.0–v1.1 within trusted-context establishment for bounded personal workforce self-service  
**Purpose:** Add a purpose-bound Personal Workforce Self-Service Context that permits a staff member to operate on their own schedule/time/leave/compensation self-service data from a personal device without weakening the existing Merchant Operational Device Context requirement for normal merchant-operational access.

---

# 1. Three Trusted Context Classes

MS-PROT-063 SHALL distinguish:

```text
Personal Security Context

Personal Workforce Self-Service Context

Staff Operational Context
```

They SHALL NOT imply one another.

---

# 2. Personal Workforce Self-Service Context

A **Personal Workforce Self-Service Context** is trusted context establishing that:

```text
authenticated Identity
+
current Merchant Membership
+
resolved Merchant Scope
+
purpose-bound self-service operation
```

apply to the current request.

It does NOT require Merchant Operational Device Authorisation merely because the device is personal.

---

# 3. Scope Restriction

The context may authorise only operations where the subject is the authenticated person's own Merchant Membership and where the owning capability explicitly permits personal self-service.

Conceptually:

```text
authenticated Identity P
+
Merchant Membership M belonging to P
+
operation:
    own_shift_offer.accept
        ↓
eligible self-service context
```

Rejected:

```text
P
+
manager role
+
personal phone
+
approve another staff member's holiday
```

That remains merchant operational activity.

---

# 4. No Privilege Escalation

Personal Workforce Self-Service Context MUST NOT bypass:

```text
Actor Authorisation
Merchant Scope isolation
membership lifecycle
operation applicability
privacy
resource protection
```

It merely removes the general operational-device prerequisite for the narrowly defined self-service operation class.

---

# 5. Staff Operational Device Boundary Survives

MS-PROT-063 v1.1 remains authoritative for normal merchant-operational reads and writes.

Therefore:

```text
personal workforce self-service
        ≠
merchant dashboard access
```

A personal workforce self-service session MUST NOT be promoted into Staff Operational Context solely because the same Identity also holds a merchant role.

---

# 6. Self-Service Subject Binding

Main Street MUST establish that the requested workforce subject is the current authenticated Identity's own Merchant Membership.

Client-supplied membership identifiers MAY be locators but MUST NOT independently establish self-service subject authority.

Unknown or mismatched subject binding MUST fail closed.

---

# 7. Current Membership Revalidation

Session continuity MUST NOT freeze Merchant Membership state.

After the applicable Merchant Membership becomes SUSPENDED or ENDED, stale self-service sessions MUST NOT continue to establish future personal workforce operations for that membership.

Historical read access, if any, remains separately purpose-bound and must follow the applicable owning authority and privacy rules.

---

# 8. Personal Workforce Notification Context

Possession of a valid Personal Workforce Self-Service Context MAY support delivery or retrieval of purpose-bound self-service notifications permitted by MS-PROT-074 v1.1 and MS-PROT-075.

It MUST NOT be treated as sufficient authority to deliver unrelated merchant-operational payloads.

---

# 9. Hard Invariants

1. Personal Security Context, Personal Workforce Self-Service Context and Staff Operational Context remain distinct.
2. Personal Workforce Self-Service Context applies only to the authenticated person's own Merchant Membership and explicitly permitted self-service operations.
3. It does not grant merchant-wide workforce administration.
4. It does not grant unrelated merchant-operational access.
5. Merchant Operational Device Authorisation remains required for normal staff merchant-operational access.
6. Current Merchant Membership state remains revalidated and cannot be frozen by a stale session.
7. Client-declared membership/subject values cannot independently establish self-service authority.
8. Existing Actor Authorisation, Merchant Scope, privacy and resource-protection rules continue to apply.

---

# 10. Acceptance Statement

> **Main Street now recognises a purpose-bound Personal Workforce Self-Service Context for a staff member's own schedule, time, leave and permitted compensation self-service. This context is narrower than Staff Operational Context and does not convert a personal device into a merchant-operational device.**