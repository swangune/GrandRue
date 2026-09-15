# IAC-01 — Merchant–Staff Identity & Access Control

## 1. Purpose

Main Street shall allow merchants to create and control staff access to their business without requiring staff members to maintain independent Main Street accounts.

A staff member exists within a **merchant employment relationship** and may only access the business functions explicitly authorised by that merchant.

The system shall support delegated administration while preserving the merchant owner's ultimate authority.

---

## 2. Identity Model

Main Street has two fundamentally different identity contexts:

### Merchant Identity

A merchant can directly sign in to Main Street.

The merchant account controls the business.

### Staff Identity

A staff member does not independently sign in as a general Main Street user.

The staff member is associated with a specific merchant through a merchant-controlled staff record.

```text
Merchant
   │
   ├── Owner
   │
   └── Staff
        ├── Administrator
        ├── Manager
        └── Staff
```

A staff member may have relationships with different merchants at different times, but each employment relationship is separately authorised.

---

# 3. Staff Registration

The merchant initiates staff registration.

```text
Merchant
   ↓
Invite Staff
   ↓
Staff completes registration
   ↓
Main Street creates merchant staff record
   ↓
Staff Number generated
   ↓
Merchant assigns role
   ↓
Merchant grants permissions
   ↓
Staff access becomes active
```

The merchant does not need to manually enter all staff information where the registration process can safely collect it directly from the staff member.

---

# 4. Staff Number

Main Street automatically generates a unique staff number for the merchant's staff member.

Example:

```text
ST-00042
```

The staff number identifies the staff member **within the merchant relationship**.

It is not an independent Main Street customer identifier.

---

# 5. Roles

Main Street shall support role-based access while allowing merchants to customise permissions.

Initial roles may include:

* Owner
* Administrator
* Manager
* Staff

Roles provide sensible permission defaults.

The actual authority of a staff member is determined by the permissions granted to that staff member.

---

# 6. Owner

The Owner has ultimate authority over the merchant account.

The Owner may:

* Manage the business
* Manage staff
* Grant administrative privileges
* Revoke access
* Configure permissions
* Manage catalogue
* Manage bookings
* Manage customers
* Manage POS
* Manage website
* Manage integrations
* Manage payment configuration
* Review staff activity
* Transfer ownership where supported

Certain owner-level operations must remain non-delegable.

These include, at minimum:

* Transferring ownership
* Closing the merchant account
* Irreversibly deleting the merchant
* Other actions designated as owner-exclusive

---

# 7. Administrator

An Administrator is a staff member who has received delegated administrative authority from the merchant.

Administrator status does not transfer ownership.

The merchant may grant an administrator authority over:

* Staff
* Catalogue
* Bookings
* Customers
* POS
* Website
* Payments
* Other business capabilities

Administrative permissions remain subordinate to the Owner.

---

# 8. Delegated Administration

An administrator may manage other staff only within the authority granted to them.

An administrator must not be able to grant permissions exceeding their own authority.

Example:

```text
Owner
 │
 └── Administrator
       │
       ├── Catalogue ✓
       ├── Bookings ✓
       ├── POS ✓
       ├── Customers ✓
       └── Ownership ✗
```

The administrator can delegate the permissions they are authorised to delegate, but cannot create owner-level authority.

---

# 9. Permission Model

Permissions shall be capability-oriented rather than industry-specific.

Examples:

```text
Catalogue
├── View
├── Create
├── Edit
└── Archive

Bookings
├── View
├── Create
├── Modify
├── Cancel
└── Check-in / Check-out

Customers
├── View
├── Create
└── Edit

Products
├── View
├── Sell
└── Manage

Services
├── View
├── Book
└── Manage

Resources
├── View
├── Allocate
└── Manage

Payments
├── View
├── Process
└── Refund

Staff
├── View
├── Invite
├── Assign
├── Modify
├── Suspend
└── Manage access

Website
├── View
├── Edit
└── Publish
```

The merchant may assign these permissions individually or through predefined roles.

---

# 10. Permission Ceiling

Every staff member has an effective permission ceiling.

A delegated administrator cannot grant another staff member a permission that exceeds the administrator's own authority.

```text
Owner authority
      ↓
Administrator authority
      ↓
Manager authority
      ↓
Staff authority
```

The hierarchy is not necessarily a strict organisational hierarchy; it represents the maximum authority that may be delegated.

---

# 11. Resource Scope

Permissions may optionally be restricted to particular business resources.

For example:

```text
Front Desk
├── View bookings
├── Check-in/out
└── View rooms

Housekeeping
├── View assigned rooms
└── Update room status

Restaurant Staff
├── View food products
├── Create orders
└── Process permitted payments
```

This prevents staff from receiving unnecessary access to unrelated business operations.

---

# 12. Staff Authentication

Staff access shall use merchant-scoped staff credentials.

For POS and other shared operational devices, the initial beta authentication model shall support:

```text
Staff Number
+
Personal PIN
```

The PIN is personal to the staff member and must not be treated as a shared business credential.

---

# 13. Credential and Session Management

The staff credential and operational session shall have separate lifetimes.

### Credential

The merchant may establish a credential-expiration policy.

The merchant may:

* Reset PIN
* Force PIN change
* Disable credential
* Suspend staff
* Revoke staff access

### Session

Operational sessions should be short-lived.

For example, a POS may automatically lock following a period of inactivity.

The staff member then re-authenticates.

This reduces the risk of one staff member leaving an authenticated POS unattended.

---

# 14. POS Attribution

Every authenticated POS session shall identify:

* Merchant
* Staff member
* Device
* Session
* Start time
* End time

Transactions performed during the session are attributed to the authenticated staff member.

Example:

```text
Transaction: TX-10482
Merchant: Merchant A
Staff: ST-00042
Device: POS-03
Action: Product Sale
Time: 14:08
Amount: £12.00
```

---

# 15. Customer-Service Attribution

Main Street shall distinguish between:

### Action performed by

The staff member who performed the system operation.

### Attended by

The staff member who actually attended to the customer where the business requires this information.

These may be different people.

Example:

```text
Customer: John Smith

Attended by: ST-00021
Payment processed by: ST-00042
Booking modified by: ST-00021
```

This provides meaningful operational traceability.

---

# 16. Audit Trail

Critical staff actions shall generate immutable audit events.

Examples:

* Staff login
* POS session start
* POS session termination
* Product sale
* Booking creation
* Booking modification
* Booking cancellation
* Customer record modification
* Payment operation
* Refund
* Catalogue modification
* Permission change
* Staff suspension
* PIN reset
* Staff access revocation

Each event should record the relevant:

```text
Merchant
Staff
Action
Business object
Timestamp
Device/session
Result
```

---

# 17. Access Revocation

The merchant must be able to immediately revoke staff access.

```text
Merchant
   ↓
Staff
   ↓
Revoke Access
   ↓
Active sessions terminated
   ↓
Credentials disabled
   ↓
Future access denied
   ↓
Historical activity retained
```

Revocation must not delete historical activity.

The staff member's previous transactions and actions remain attributable to that staff identity.

---

# 18. Staff Leaving a Business

When a staff member leaves:

```text
Employment Active
       ↓
Merchant ends employment
       ↓
Access revoked
       ↓
Sessions terminated
       ↓
Merchant data remains protected
       ↓
Historical activity preserved
```

If the individual later joins another merchant, a **new merchant employment relationship** is established.

Appropriate staff information may be reused during registration, but the new merchant receives a separate authority context.

The individual does not carry permissions from the previous merchant.

---

# 19. Security Decision

Every protected operation shall evaluate at least:

```text
Identity
   ↓
Merchant relationship
   ↓
Employment status
   ↓
Role
   ↓
Permission
   ↓
Resource scope
   ↓
Session validity
   ↓
Business rules
   ↓
ALLOW / DENY
```

A valid Staff Number or PIN alone is never sufficient to authorise an operation.

---

# 20. Merchant Control Principle

Main Street shall provide the infrastructure for access control, but the merchant remains responsible for determining who may operate their business and what those people may do.

Therefore:

> **Main Street provides the control mechanism; the merchant exercises the control.**

Main Street may provide recommended roles and permissions, but it must not unnecessarily override merchant-authorised configuration.

---

# 21. Beta Scope

The beta shall implement:

* Merchant-only Main Street authentication
* Merchant-controlled staff invitation
* Staff registration
* Automatic Staff Number generation
* Merchant-scoped staff identity
* Owner role
* Administrator role
* Manager role
* Staff role
* Granular permissions
* Delegated administration
* Permission ceiling
* Staff PIN
* POS session management
* Staff transaction attribution
* Staff activity audit trail
* Immediate access revocation
* Historical activity preservation

Advanced authentication methods such as passkeys, NFC credentials, biometric authentication and hardware security keys may be introduced later without changing the underlying merchant-staff model.

---

# 22. Architectural Statement

Main Street shall treat staff access as a **merchant-controlled delegated authority system**.

Staff members do not become independent Main Street users. Their access exists only within an authorised relationship with a merchant.

The system shall provide sufficient flexibility for a merchant to delegate substantial administrative responsibility to trusted staff while retaining ultimate ownership and control.

Every significant operational action must remain attributable to the staff member who performed it.

The result is a system where:

**The merchant controls the business.**

**Staff operate the business within delegated authority.**

**Main Street enforces the boundary and records what happened.**
