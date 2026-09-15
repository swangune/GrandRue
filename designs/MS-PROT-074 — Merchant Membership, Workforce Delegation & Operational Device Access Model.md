# MS-PROT-074 — Merchant Membership, Workforce Delegation & Operational Device Access Model

**Document ID:** MS-PROT-074  
**Version:** 1.0  
**Status:** ACCEPTED  
**Depends on:** MS-PROT-027, MS-PROT-028, MS-PROT-031, MS-PROT-062, MS-PROT-063, MS-PROT-064, MS-PROT-067, MS-PROT-071, MS-PROT-073  
**Closes:** Promoted design-review item — Staff / Team identity, merchant relationship, delegated authority and lifecycle  
**Purpose:** Define merchant workforce membership, groups, scoped role assignment, delegated authority and staff operational-device access without creating a parallel identity system, global role model, business-type permission model or uncontrolled staff access from private devices.

---

## 1. Governing Principle

> **Main Street workforce access is an enterprise-style tenant-membership and scoped-role model. A human Identity may participate in multiple Merchant Accounts, but every Merchant Membership, Group Membership, Role Assignment and staff operational-device authorisation is independently merchant-scoped. Staff may administer their own identity and credentials from personal devices, but merchant operational information and operations are available to staff only through an explicitly authorised Merchant Operational Device Context.**

Canonical separation:

```text
Identity
    ≠ Merchant Membership
    ≠ Group Membership
    ≠ Role Assignment
    ≠ Merchant Controller
    ≠ Merchant Operational Device Authorisation

Authentication
    ≠ Actor Authorisation

Role name / job title
    ≠ authority

Staff credential
    ≠ permission to access merchant data from any device
```

---

## 2. Architectural Placement and Ownership

The workforce/delegation authority owns:

```text
MerchantMembership
MerchantAccessGroup
MerchantGroupMembership
MerchantRoleDefinition
MerchantRoleAssignment
staff-access invitation / membership establishment semantics
staff membership lifecycle
merchant-scoped staff reference semantics
```

It does NOT own:

```text
Identity or credential material
authentication provider technology
Session authority
Merchant Account existence
Merchant Controller relationship
Commercial Entitlement
Merchant Configuration
capability business state
AuditRecord
staff scheduling / rota / bookable-resource state
payroll, employment-law or HR truth
```

MS-PROT-063 owns authentication, session continuity and Trusted Execution Context. MS-PROT-062 owns runtime composition. MS-PROT-064 owns audit evidence. MS-PROT-067 owns credential/security-bound material. MS-PROT-071 and MS-PROT-028 continue to own Merchant Account / Merchant Controller boundaries.

---

## 3. Merchant Membership

A **Merchant Membership** is the authoritative merchant-scoped relationship that permits an authenticated Identity to receive workforce access assignments in one Merchant Scope.

Conceptually:

```text
MerchantMembership
{
    membershipIdentity
    merchantIdentifier
    identityReference
    lifecycle
    establishedAt
    endedAt?
}
```

The representation is conceptual and does not mandate one Java type or table.

A Merchant Membership means only that the Identity has a current or historical workforce relationship with the Merchant Account. It does not assert:

```text
legal employment
payroll status
company directorship
legal ownership
professional qualification
Merchant Controller authority
```

`Staff` is a business-facing/member classification and projection over this relationship; it is not the foundational global identity object.

---

## 4. One Identity, Multiple Merchant Relationships

One Main Street Identity MAY participate independently in multiple Merchant Accounts.

Example:

```text
Identity P
   ├── Staff membership → Merchant A
   ├── Staff membership → Merchant B
   └── Controller relationship → Merchant C
```

Each relationship is independently scoped and independently revocable.

Ending or suspending Merchant A membership MUST NOT alter Merchant B membership or Merchant C controller authority.

Authority from one Merchant Scope MUST NOT leak into another.

---

## 5. Staff Identity and Credentials

Every acting staff member MUST be individually attributable through a trusted authenticated principal.

Main Street does not require a separate global `StaffAccount` to become business authority.

A person invited by a merchant MAY:

```text
accept invitation on a personal device
create a new Main Street Identity where required
reuse an existing Main Street Identity
manage personal authentication credentials
set/reset the merchant-scoped staff PIN where supported
```

The merchant MUST NOT need to know or create the person's personal password, passkey or equivalent Main Street Identity credential.

Authentication technology remains governed by MS-PROT-063.

---

## 6. Staff Invitation and Membership Establishment

A merchant-authorised invitation is a bounded establishment mechanism, not staff authority by itself.

Canonical flow:

```text
Merchant Controller / authorised membership administrator
        ↓
issue staff invitation
        ↓
recipient authenticates or creates Main Street Identity
        ↓
recipient accepts invitation
        ↓
Merchant Membership established
        ↓
role/group assignments may be established separately
```

Hard distinctions:

```text
Invitation ≠ Merchant Membership
Invitation ≠ authentication
Invitation ≠ Role Assignment
Invitation ≠ operational access
```

An invitation MUST be merchant-scoped, revocable before acceptance, bounded by expiry, attributable to its issuer and safe under duplicate acceptance/retry.

One logical invitation MUST NOT create multiple active memberships through replay.

---

## 7. Merchant-Scoped Staff Reference and PIN

Main Street MAY issue a human-friendly merchant-scoped staff reference such as:

```text
ST-0042
```

The staff reference identifies a Merchant Membership for operational convenience. It is not the global Identity and it is not authority.

A personal merchant-scoped PIN MAY be used as a lightweight staff authentication factor on an authorised operational device.

Canonical distinction:

```text
Main Street Identity credential
    authenticates the person / supports identity administration

Merchant-scoped Staff Reference + PIN
    supports fast workforce authentication for that merchant

Neither grants business authority by itself.
```

A staff member MAY reset their own merchant PIN from a personal device after authenticating their Main Street Identity through the accepted personal-security path.

A PIN reset MUST NOT expose merchant operational data and MUST make the superseded PIN unusable. Operational sessions whose continued validity depends on the superseded PIN SHOULD be invalidated according to MS-PROT-063 security/session rules.

---

## 8. Merchant Access Groups

A **Merchant Access Group** is a merchant-scoped grouping of active Merchant Memberships for access administration.

Examples may include:

```text
Front Desk
Managers
Housekeeping
Workshop
Sales
```

Group names are merchant-facing organisational labels. They are not global role semantics.

A group:

```text
cannot authenticate
cannot become an Execution Principal
cannot cross Merchant Scope
cannot itself perform a business operation
```

Nested groups are not accepted in v1.0.

---

## 9. Group Membership

A **Merchant Group Membership** associates one Merchant Membership with one Merchant Access Group in the same Merchant Scope.

Conceptually:

```text
MerchantGroupMembership
{
    group
    membership
    lifecycle
}
```

Initial lifecycle:

```text
ACTIVE
ENDED
```

A Merchant Membership MAY belong to multiple groups.

---

## 10. Merchant Role Definition

A **Merchant Role Definition** is a reusable merchant-scoped access profile composed only from registered Main Street privileges/authority targets.

Examples of merchant-facing names may include:

```text
Cashier
Booking Manager
Inventory Manager
Administrator
```

The name is not runtime authority.

The role resolves to explicit registered privileges, for example:

```text
Booking Manager
    appointment.view
    appointment.create
    appointment.modify
    appointment.cancel
```

A role MUST NOT invent an unregistered capability Operation or semantic privilege.

Job titles such as `Stylist`, `Mechanic` or `Receptionist` MAY be merchant organisational labels but MUST NOT automatically imply authority.

---

## 11. Merchant Role Assignment

A **Merchant Role Assignment** is the authoritative merchant-scoped fact that assigns a Merchant Role Definition to a principal subject and scope.

Initial assignment subjects MAY be:

```text
Merchant Membership
Merchant Access Group
```

Conceptually:

```text
MerchantRoleAssignment
{
    assignmentIdentity
    merchantIdentifier
    subject
    roleDefinition
    scope
    delegationAuthority
    provenance
    effectiveFrom
    effectiveUntil?
    lifecycle
}
```

This is conceptual rather than a mandated persistence shape.

Role scope may be merchant-wide or a narrower registered capability/resource scope where accepted semantics support that scope.

---

## 12. Direct and Group-Derived Authority

For one Staff Principal, applicable workforce authority is derived from:

```text
active direct Role Assignments
        +
active Role Assignments on active Groups
where active Group Membership exists
```

The result is supplied to the existing Actor Authorisation dimension; it does not become a second runtime permission engine.

Explicit `DENY` precedence is not accepted in v1.0. Where merchants require narrower authority, they adjust Role Assignments, assignment scope or Group Membership.

---

## 13. Exercise Authority and Delegation Authority

The authority to exercise a privilege and the authority to delegate it are distinct.

```text
may exercise X
    ≠
may assign/delegate X
```

A Role Assignment MAY therefore permit operation exercise without permitting onward assignment.

A grantor MUST NOT create a Role Assignment whose privilege set, assignment scope or delegation capability exceeds the grantor's current authority to delegate.

This is the canonical **delegation ceiling**.

A staff principal MUST NOT self-escalate beyond their existing delegation envelope.

---

## 14. Merchant Controller Boundary

Merchant Controller authority is not an ordinary workforce role.

Hard invariant:

```text
all staff/workforce privileges combined
    ≠
Merchant Controller relationship
```

No combination of Merchant Role Assignments or Group Memberships may manufacture Merchant Controller authority, ownership transfer or Merchant Account closure authority.

Merchant-control transfer, suspension, closure and terminal account lifecycle remain separately governed.

---

## 15. Merchant Membership Lifecycle

Initial workforce membership lifecycle:

```text
ACTIVE
   ↓ suspend
SUSPENDED
   ↓ resume
ACTIVE

ACTIVE / SUSPENDED
   ↓ end
ENDED
```

`ENDED` is terminal for that membership identity.

### ACTIVE

The relationship may participate in staff-principal establishment and Actor Authorisation. It grants no capability privilege by itself.

### SUSPENDED

The relationship remains historically present but contributes no current workforce authority.

### ENDED

The relationship is over and can never become effective again under the same membership identity.

If the person later rejoins the merchant, a new Merchant Membership MUST be established. Old Role Assignments MUST NOT silently revive.

Historical attribution MUST survive suspension/end.

---

## 16. Role Assignment Lifecycle

Initial Role Assignment lifecycle:

```text
ACTIVE
REVOKED
EXPIRED
```

A time-bounded assignment uses a half-open effective interval:

```text
effectiveFrom <= now < effectiveUntil
```

Revocation is permanent for the assignment identity. Restoring the same access creates a new assignment rather than rewriting the historical record.

A grantor's later loss of authority does not automatically revoke every previously valid assignment they created. Valid committed assignments are independent authoritative facts until explicitly revoked/expired or made ineffective by membership/group state.

---

## 17. Merchant Operational Device Authorisation

A **Merchant Operational Device Authorisation** means:

> Main Street has independently established that a specific device/application context has been explicitly authorised by a current Merchant Controller to establish staff operational contexts for one Merchant Scope.

It does not assert legal ownership of the physical hardware or that the hardware is physically located at the merchant premises.

Hard distinction:

```text
Merchant Operational Device Authorisation
    ≠ Merchant Membership
    ≠ Role Assignment
    ≠ staff authentication
    ≠ Merchant Controller Session
```

Device authorisation is a staff-access trust prerequisite, not business authority.

---

## 18. Explicit Controller Enrolment

Ordinary Merchant Controller sign-in MUST NOT automatically authorise the current device for persistent staff operational access.

Canonical enrolment:

```text
Merchant Controller authenticates
        ↓
current Controller relationship verified
        ↓
explicit "authorise this device for staff access" intent
        ↓
operation-specific authentication assurance / reauthentication where required
        ↓
Merchant Operational Device Authorisation = ACTIVE
```

Staff credentials MUST NOT establish Merchant Operational Device Authorisation.

Initial v1.0 authority to enrol a staff-operational device belongs to a current Merchant Controller. Delegation of device enrolment to staff administrators is not accepted in v1.0.

The Controller may end their own session after enrolment while the Device Authorisation remains active.

---

## 19. Device/Application Context, Not Client Flag

Device authorisation MUST be bound to independently verifiable device/application-instance evidence.

The following are insufficient by themselves:

```text
client-supplied deviceId
merchantId cookie
localStorage flag
user-agent
IP address
Staff Reference
staff PIN
ordinary copied browser state
```

The exact mechanism is downstream security implementation. It MAY use device/application key material, secure platform storage, hardware-backed keys where available or equivalent server-validated registration.

MS-PROT-067 governs protected credential/binding material.

A browser-data reset, app reinstall or factory reset MUST NOT automatically recover old device authority solely from a mutable client identifier.

---

## 20. Private Device Personal-Security Surface

A staff member MAY use a non-operational personal device for bounded personal-security and membership administration, including as applicable:

```text
accept merchant invitation
create/reuse Main Street Identity
manage personal authentication factors
recover personal credentials
set/reset merchant-scoped PIN
view minimal membership-security information necessary to perform those actions
```

This surface MUST NOT expose merchant operational information.

---

## 21. Membership Security Projection vs Merchant Operational Projection

Main Street SHALL distinguish:

### Membership Security Projection

A deliberately minimal projection that may be exposed on a personal device for identity/security administration.

It MAY include only bounded information necessary for that purpose, such as:

```text
merchant display name
membership state
staff reference
invitation state
PIN/security state
available security/recovery actions
```

### Merchant Operational Projection

Merchant-owned operational/business information, including for example:

```text
customers
appointments
orders
inventory
messages
sales/financial figures
reports
merchant documents
operational calendars
customer contact information
business-operation queues
```

A staff principal MUST NOT receive Merchant Operational Projections unless the current device/application context has an ACTIVE Merchant Operational Device Authorisation for that Merchant Scope.

This rule applies to reads as well as writes.

---

## 22. Operational Notifications Must Not Bypass Device Restriction

A private/non-operational staff device MUST NOT receive merchant operational information indirectly through notifications merely because the Identity has an active Merchant Membership.

Security/account notifications MAY be delivered, for example:

```text
Your PIN was changed.
You were invited to Merchant A.
Your membership was suspended.
```

Operational notification payloads on non-authorised devices must either be withheld or reduced to a non-sensitive signal that does not disclose merchant operational information.

Detailed notification semantics remain separately governed by the Notification design authority.

---

## 23. Trusted Staff Operational Context

A trusted staff operational context requires, as applicable:

```text
authenticated Main Street Identity / staff proof
+
ACTIVE Merchant Membership
+
resolved Merchant Scope
+
ACTIVE Merchant Operational Device Authorisation
+
current Role/Group authority
+
normal MS-PROT-062 runtime predicates
```

A valid staff credential on a private/non-authorised device MUST NOT establish a normal merchant operational context.

Unknown or unprovable device-authorisation state MUST fail closed for staff operational access.

---

## 24. Device Authorisation Lifecycle and Revocation

Initial lifecycle:

```text
ACTIVE
   ↓ revoke
REVOKED
```

`REVOKED` is terminal for that Device Authorisation identity.

Re-authorising the same physical device/application later creates a new Device Authorisation rather than rewriting history.

When a Device Authorisation is revoked:

```text
new staff operational authentication on that context is denied
merchant operational projections are denied
relevant staff sessions on that device/context are invalidated or made unusable
historical business/audit evidence remains unchanged
```

Stale sessions, cached role claims or remembered Staff References MUST NOT override current Device Authorisation state.

---

## 25. Staff Relationship / Role Revocation Concurrency

After a Merchant Membership suspension/end or applicable Role Assignment revocation commits, an authoritative operation that has not yet crossed its required current-authority revalidation boundary MUST NOT subsequently commit using the revoked authority.

Similarly, after Device Authorisation revocation commits, new staff operational reads/writes requiring that context MUST be rejected.

Previously committed business facts are not retroactively erased.

---

## 26. Shared Devices and Attribution

Main Street supports shared merchant operational devices such as POS terminals, reception tablets and office workstations.

Example:

```text
POS-1 authorised for Merchant A

Alice authenticates
    ↓ Alice staff session
    ↓ Sale 101 attributed to Alice

Alice signs out / device locks

David authenticates
    ↓ David staff session
    ↓ Sale 102 attributed to David
```

Shared hardware is permitted; shared execution identity is not accepted for attributable authoritative actions.

Audit/operational evidence MAY additionally record the operational device context.

---

## 27. One Device, Multiple Merchants

One physical/application context MAY hold independent Device Authorisations for more than one Merchant Scope where each current Merchant Controller separately authorises it.

Each authorisation is independent.

A staff context for Merchant A MUST NOT reveal Merchant B merely because the same device also has a Merchant B authorisation.

---

## 28. Merchant Controller Access Is Separate

The staff device prerequisite applies to staff operational access.

A Merchant Controller may authenticate from a new/private device under the accepted Controller authentication/trust policy so that they can manage the merchant and enrol operational devices.

Where one Identity is both Controller and Staff, the system MUST preserve which relationship/context is being exercised.

Controller access MUST NOT silently convert every staff context into Controller authority.

---

## 29. Staff as Business Resource Is Separate

A human staff member MAY also be represented by a capability-owned business Resource, such as a bookable stylist, mechanic or consultant.

Hard distinction:

```text
Merchant Membership / workforce access
    ≠
Schedulable or allocatable business Resource
```

The two lifecycles and authorities remain independently owned.

---

## 30. Performed By vs Attended By

Main Street SHALL distinguish:

```text
Performed by
    the Execution Principal who performed the Main Street operation

Attended by
    the capability/business participant or Resource that actually served the customer where relevant
```

These may refer to different people.

Audit attribution derives from the Execution Principal; capability-specific `attendedBy` semantics remain owned by the applicable business capability.

---

## 31. Audit Requirements

Material workforce-security changes require durable attributable evidence under MS-PROT-064.

At minimum, the design shall support audit evidence for:

```text
Membership established
Membership suspended/resumed/ended
Role Assignment established/revoked
Delegation authority change
Group Membership change where authority is affected
Device Authorisation established/revoked
material PIN/security reset where required by security policy
```

Where the governing action requires Atomic Audit Evidence, success MUST NOT be reported without the required durable evidence.

Ordinary reads do not automatically require durable audit evidence.

---

## 32. Commercial and Semantic Independence

Workforce authority does not create Merchant Configuration or Commercial Entitlement.

Examples:

```text
Role Assignment includes inventory.manage
+
Inventory not semantically applicable
    → operation not applicable

Role Assignment includes protected paid functionality
+
merchant not commercially entitled
    → NOT_ENTITLED
```

Role/Group state MUST NOT be rewritten merely because configuration or commercial state changes.

MS-PROT-062 composes these predicates independently.

---

## 33. Authentication Assurance and Identity Proofing

Staff do not inherit a universal real-world identity-proofing requirement merely because they hold a Merchant Membership.

Ordinary workforce access requires accepted authentication assurance, Merchant Membership and applicable device/role context.

Stronger or recent authentication / additional Trust Claims MAY be required only where an accepted operation-specific security, legal or regulatory obligation justifies them.

Device enrolment is a high-impact security action and may require stronger/recent Controller authentication assurance.

---

## 34. Resource Protection for Staff Authentication

Failed staff authentication/PIN attempts MAY be constrained by MS-PROT-073 Resource Protection.

Rate limiting or Temporary Protective Restriction MUST NOT by itself suspend/end Merchant Membership, revoke roles or declare fraud.

---

## 35. Offline Operational Data

Initial staff operational access MUST NOT assume durable offline availability of merchant operational data.

If future product requirements demand offline staff operation, a separate security design must govern encrypted local data, authority expiry, revocation reconciliation and offline operation constraints.

This v1.0 authority does not promise immediate remote invalidation of already-downloaded offline data.

---

## 36. Security Claim Boundary

The accepted guarantee is:

> **Main Street prevents staff from independently obtaining electronic merchant operational access through a device/application context that the applicable Merchant Controller has not authorised.**

This is not a claim that Main Street can prevent an authorised staff member from manually copying, photographing or verbally disclosing information legitimately visible to them. Broader data-loss prevention is outside this authority.

---

## 37. Falsification Findings

The model survives the following cases:

1. **Staff works for two merchants** — one Identity; independent memberships/roles/device contexts; no leakage. PASS.
2. **Staff accepts invitation from private phone** — membership/security flow works; no operational data exposed. PASS.
3. **Staff resets PIN from private phone** — PIN changes; operational dashboard remains unavailable. PASS.
4. **Staff knows valid Staff Reference + PIN on private device** — operational access denied because Device Authorisation is absent. PASS.
5. **Shared POS** — each staff member authenticates individually; actions remain attributable. PASS.
6. **Controller signs in on borrowed device** — sign-in alone does not authorise staff mode; explicit enrolment is required. PASS.
7. **Stolen Controller session** — device enrolment may require current/recent authentication assurance; stale session alone need not be sufficient. PASS.
8. **Lost operational tablet** — Device Authorisation revocation blocks future staff reads/writes; history preserved. PASS.
9. **Manager can refund but cannot delegate refund** — exercise and delegation authority remain separate. PASS.
10. **Administrator attempts self-escalation** — delegation ceiling rejects the assignment. PASS.
11. **Staff accumulates every workforce role** — Merchant Controller authority is not manufactured. PASS.
12. **Role remains while capability removed or entitlement lost** — independent runtime predicates reject execution without rewriting role state. PASS.
13. **Staff member leaves and later returns** — new Membership identity; old roles do not revive. PASS.
14. **Same device authorised for two merchants** — each Merchant Scope remains independently resolved. PASS.
15. **Operational notification to private phone** — payload may not leak merchant operational data. PASS.
16. **Stolen authorised device + stolen PIN** — credential protection, session controls and MS-PROT-073 rate protection remain necessary; device authorisation alone is not sufficient authentication. PASS with normal credential-security controls.
17. **Offline device revoked remotely** — immediate revocation of already-downloaded data cannot be guaranteed without a separate offline-security design; therefore durable offline operational data is excluded from v1.0. PASS after scope restriction.

---

## 38. Rejected Alternatives

The following are rejected:

```text
global User.role authority
fixed OWNER > ADMIN > MANAGER > STAFF hierarchy as semantic authority
job title as permission
Staff Reference/PIN as authority
staff credential that works operationally from any device
merchant login automatically trusting the device
client-controlled trusted-device flags
shared staff execution accounts
Team/Group as Execution Principal
staff privileges manufacturing Merchant Controller authority
Merchant Membership as legal employment evidence
destructive deletion to represent workforce departure
unqualified private-device merchant dashboards
operational push notifications that bypass device restrictions
unbounded nested groups in v1.0
explicit DENY precedence in v1.0
durable offline merchant-data access without separate security design
```

---

## 39. Hard Invariants

1. Workforce authority is Merchant-scoped.
2. Identity, Merchant Membership, Group Membership, Role Assignment, Merchant Controller and Device Authorisation remain distinct.
3. Every acting staff principal is individually attributable.
4. Merchant Membership grants no capability privilege by itself.
5. Runtime authority derives from applicable current Role Assignments over registered privileges.
6. Role/job-title names are not authority.
7. Direct and Group-derived role assignments compose without creating a separate permission engine.
8. Exercise authority and delegation authority remain distinct.
9. A delegate cannot assign beyond the current delegation ceiling or self-escalate.
10. Workforce authority cannot manufacture Merchant Controller authority.
11. Suspended/Ended Memberships contribute no workforce authority; Ended is terminal.
12. Historical attribution survives workforce access termination.
13. Staff may accept invitations, register identity and reset credentials/PIN from personal devices.
14. Personal-device identity/security administration MUST NOT expose Merchant Operational Projections.
15. Staff operational reads and writes require an ACTIVE Merchant Operational Device Authorisation for the Merchant Scope.
16. Merchant Controller sign-in alone does not authorise a device; device enrolment requires explicit intent.
17. Staff credentials cannot authorise operational devices.
18. Device authorisation MUST be bound to independently verifiable device/application-instance evidence.
19. Unknown/revoked Device Authorisation fails closed for staff operational access.
20. Stale sessions cannot override current Membership, Role Assignment or Device Authorisation state.
21. Merchant operational notifications cannot leak business information to non-authorised staff device contexts.
22. One Identity and one physical device may participate in multiple Merchants only through independently scoped relationships/authorisations.
23. Workforce access identity remains separate from schedulable/business Resource identity.
24. Initial staff operation does not promise durable offline merchant-data access.

---

## 40. Deferred / Downstream Scope

This authority deliberately does not select:

```text
password/passkey/identity-provider technology
exact PIN policy and storage implementation
exact device-bound cryptographic mechanism
hardware-backed key requirements
role-management UI
default merchant role templates
nested groups
explicit DENY precedence
delegated device enrolment by non-controllers
offline operational-data architecture
staff scheduling/rota/payroll/HR semantics
cross-merchant workforce administration
Merchant Controller ownership transfer
Merchant Account suspension/closure
```

---

## 41. Acceptance Statement

Main Street adopts an enterprise-style merchant workforce-access model in which one reusable human Identity may participate in multiple Merchant Accounts through independent Merchant Memberships, Groups and scoped Role Assignments, while staff merchant-operational access is additionally constrained to Merchant Controller-authorised device/application contexts.

> **The credential belongs to the person; workforce authority belongs to the merchant; operational device access is explicitly merchant-authorised; business execution remains capability-owned.**
