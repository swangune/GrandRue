# MS-PROT-063 v1.1 — Staff Operational Device Context Amendment

**Document ID:** MS-PROT-063  
**Version:** 1.1  
**Status:** ACCEPTED  
**Amends:** MS-PROT-063 v1.0 — Authentication, Session & Trusted Execution Principal Establishment Model  
**Depends on:** MS-PROT-027, MS-PROT-028 v1.3, MS-PROT-031, MS-PROT-063 v1.0, MS-PROT-067, MS-PROT-074  
**Purpose:** Add Merchant Operational Device Context as a required trusted-context input for staff merchant-operational access while preserving personal-device identity/security administration and keeping device authorisation distinct from Identity, Session, Merchant Membership and Actor Authorisation.

---

## 1. Scope

This amendment is intentionally narrow.

MS-PROT-063 v1.0 remains accepted except where this amendment adds staff operational-device requirements to staff principal establishment and trusted-context propagation.

This amendment does not redefine:

```text
Identity
Session
Merchant Scope
Merchant Membership
Role Assignment
Actor Authorisation
Commercial Entitlement
capability-owned business eligibility
```

MS-PROT-074 owns workforce membership/delegation and Merchant Operational Device Authorisation semantics.

---

## 2. New Canonical Concept — Merchant Operational Device Context

A **Merchant Operational Device Context** is trusted security context proving that the current device/application instance has an ACTIVE Merchant Operational Device Authorisation for the resolved Merchant Scope.

It answers:

> **May this device/application context establish a staff merchant-operational execution context for this Merchant Scope?**

It does not answer:

```text
Who is the staff member?
Is the staff member an active Merchant Member?
Which roles does the staff member hold?
May the staff member perform the requested operation?
Is the merchant commercially entitled?
Is the operation semantically applicable?
```

---

## 3. Trusted Staff Operational Context

For staff merchant-operational access, MS-PROT-063 SHALL establish or carry trusted context sufficient to resolve, as applicable:

```text
Merchant Scope
Staff Execution Principal
staff authentication evidence
Merchant Membership context
Merchant Operational Device Context
Session/security context
```

Conceptually:

```text
staff authentication
        +
Merchant Scope resolution
        +
current Merchant Membership
        +
ACTIVE Merchant Operational Device Context
        ↓
Trusted Staff Operational Context
        ↓
MS-PROT-062 Actor Authorisation and other runtime dimensions
```

This is a semantic contract, not a mandated Java record shape.

---

## 4. Personal Security Context Is Not Staff Operational Context

A staff member MAY authenticate their Main Street Identity from a personal/non-operational device for bounded identity/security administration permitted by MS-PROT-074.

Such authentication MAY establish a **Personal Security Context** sufficient for actions such as:

```text
accept invitation
create/reuse Main Street Identity
manage personal authentication factors
recover personal credentials
set/reset merchant-scoped PIN
view minimal Membership Security Projection
```

It MUST NOT by itself establish a Staff Operational Context.

Canonical distinction:

```text
Personal Security Context
    ≠
Staff Operational Context
```

---

## 5. Device Authorisation Is Required for Operational Reads and Writes

A staff execution context MUST NOT receive Merchant Operational Projections or perform merchant operational mutations unless an ACTIVE Merchant Operational Device Context has been established for the applicable Merchant Scope.

This requirement applies to both:

```text
merchant operational reads
merchant operational writes
```

A valid staff credential or active Merchant Membership on a non-authorised device is insufficient.

---

## 6. Device Context Must Be Trusted, Not Client-Declared

Merchant Operational Device Context MUST be established from independently validated device/application-instance evidence linked to server-side or otherwise trusted Merchant Operational Device Authorisation state.

The following MUST NOT establish trusted device context merely because the client supplies them:

```text
deviceId
merchantId
trusted=true
cookie/localStorage flag
IP address
user-agent
Staff Reference
staff PIN
```

Transport/application adapters may consume cryptographic or equivalent security evidence, but downstream capability code MUST NOT inspect raw device secrets or browser security material directly.

MS-PROT-067 governs protected credential/binding material.

---

## 7. Merchant Controller Enrolment Boundary

A Merchant Controller Session and a Merchant Operational Device Authorisation remain distinct.

Controller authentication MUST NOT automatically convert the current device into a staff-operational device.

The device becomes staff-operational only after a separately authorised enrolment action defined by MS-PROT-074.

The Controller MAY end their session after successful device enrolment while the resulting Device Authorisation remains independently active.

---

## 8. Staff Credentials Cannot Bootstrap Device Trust

Hard invariant:

> **A staff credential MUST NOT establish Merchant Operational Device Authorisation.**

Therefore a Staff Reference, PIN, password, passkey or other staff authentication proof on a private/non-authorised device cannot bootstrap staff operational access.

---

## 9. Current-State Revalidation

Session continuity MUST NOT freeze Merchant Operational Device Authorisation state.

Where staff operational access is attempted, current security context MUST be able to determine whether the Device Authorisation remains valid.

After Device Authorisation revocation commits:

```text
stale session
cached role claim
previous successful login
remembered Staff Reference/PIN
```

MUST NOT independently establish valid future Staff Operational Context.

This follows MS-PROT-063 v1.0's existing rule that mutable relationship/access state can invalidate future principal establishment or authorisation without deleting Identity/history.

---

## 10. Unknown Device State

Where Main Street cannot establish that the current device/application context has an ACTIVE Merchant Operational Device Authorisation for the applicable Merchant Scope:

```text
UNKNOWN
    ≠
ACTIVE
```

Staff operational access MUST fail closed.

Personal identity/security administration may continue where independently authorised.

---

## 11. Multi-Merchant Device Context

A physical/application context MAY hold independently established Merchant Operational Device Authorisations for multiple Merchant Scopes.

Trusted context MUST select and validate the specific Merchant Scope being exercised.

Authorisation for Merchant A MUST NOT imply visibility or execution authority for Merchant B.

---

## 12. Controller and Staff Contexts for the Same Identity

The same Identity MAY be a Merchant Controller and a staff member for the same or different Merchants.

MS-PROT-063 MUST preserve the relationship/context actually being exercised.

A valid Controller context does not silently upgrade every Staff Operational Context to Controller authority, and a staff device restriction does not prevent the same Identity from using a separately authorised Controller path.

---

## 13. PIN Reset and Session Consequence

A staff PIN reset MAY be initiated from Personal Security Context under MS-PROT-074.

The reset must not automatically establish Staff Operational Context.

When the superseded PIN would otherwise continue to support existing staff authentication continuity, the authentication/session boundary MUST make that superseded proof unusable and invalidate or re-establish affected operational sessions according to accepted security policy.

---

## 14. Operational Notification Context

Authentication/session infrastructure MUST NOT treat possession of a valid staff Identity/session on a private device as sufficient reason to deliver merchant operational payloads.

Where notification delivery targets a staff personal device, the Notification authority must respect the projection/device restrictions established by MS-PROT-074.

This amendment does not define notification semantics.

---

## 15. Offline Limitation

This amendment does not establish a general offline Staff Operational Context capable of retaining durable merchant operational data after server-side revocation.

Offline staff operation requires a separate accepted security contract governing local encryption, authority expiry, revocation reconciliation and offline mutation/read constraints.

---

## 16. Hard Invariants

1. Staff operational context and personal security context remain distinct.
2. Staff operational reads and writes require ACTIVE Merchant Operational Device Context.
3. Staff authentication alone cannot establish operational device trust.
4. Device trust cannot be established from client-declared identifiers/flags alone.
5. Merchant Controller sign-in alone does not enrol a device.
6. Device Authorisation remains Merchant-scoped.
7. Session continuity cannot override Device Authorisation revocation.
8. Unknown device-authorisation state fails closed for staff operational access.
9. One device may hold multiple independent Merchant Authorisations without cross-scope leakage.
10. Personal-device PIN reset/invitation/identity administration does not expose merchant operational data.
11. Capability code remains insulated from raw device credential/security material.

---

## 17. Acceptance Statement

MS-PROT-063 now distinguishes staff personal identity/security authentication from staff merchant-operational principal establishment.

> **A staff Identity may authenticate from a personal device for security administration, but Main Street establishes a Staff Operational Context only when the applicable Merchant Scope, current Membership and Merchant Operational Device Context have independently been trusted.**
