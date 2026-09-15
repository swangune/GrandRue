# BO-11 — Merchant Staff Administration

> **Workflow ID:** BO-11
> **Module:** Business Operations
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow manages staff participation within an individual merchant's Main Street environment.

Staff are associated with merchants, not with Main Street as a platform-wide membership.

A staff member does not create or maintain an independent Main Street account.

Instead, the merchant invites the staff member to complete a merchant-specific onboarding process. Following successful onboarding, Main Street creates a **Merchant Staff Record** and automatically assigns a unique **Staff Number**.

The merchant remains responsible for determining the staff member's role, responsibilities, access and operational participation.

---

# 2. Objective

The workflow shall enable merchants to:

* Invite staff members
* Collect required staff information with minimal administration
* Create merchant-specific staff records
* Automatically assign Staff Numbers
* Assign roles
* Assign responsibilities
* Assign access permissions
* Associate staff with relevant business functions
* Activate or deactivate staff participation
* Support staff-dependent business workflows

The workflow shall minimise the information and administrative work required from merchants while preserving merchant control.

---

# 3. Core Principles

## 3.1 Merchant Ownership

A staff relationship belongs to the individual merchant.

Main Street does not maintain a platform-wide employment relationship with the staff member.

---

## 3.2 Merchant Control

The merchant controls:

* Staff membership
* Roles
* Responsibilities
* Access
* Operational participation
* Activation
* Deactivation

---

## 3.3 No Platform-Wide Staff Account

Staff onboarding does not create a general Main Street account that can independently access the platform.

Staff access exists only within the context of an authorised merchant relationship.

---

## 3.4 Self-Supplied Information

The staff member provides their own required onboarding information.

The merchant should not be required to manually enter information that the staff member can reasonably provide themselves.

---

## 3.5 Minimum Merchant Administration

The merchant's primary workflow should be:

```text
Invite
  ↓
Assign
  ↓
Authorise
```

Main Street handles the underlying administration.

---

# 4. Actors

## Primary Actors

* Merchant
* Staff Member

## Supporting Capabilities

* Identity & Access Control
* Booking Management
* Business Order Management
* Service Catalogue Management
* Customer Relationship Management
* Reporting & Business Insights
* AI Services

---

# 5. Preconditions

* The merchant has completed business onboarding.
* The merchant has authority to administer staff.
* Staff Administration is enabled or required by the business.
* The merchant has initiated a staff invitation.

A business without staff may operate Main Street without using Staff Administration.

---

# 6. Staff Onboarding Model

Staff onboarding is a **merchant-specific onboarding process**.

It is intentionally lightweight and form-based.

The staff member completes the information requested by the merchant's onboarding configuration.

The process resembles a structured questionnaire in its simplicity, but it is not a general survey and does not establish a platform-wide Main Street account.

The completed information becomes part of the merchant-specific Staff Record.

---

# 7. Workflow

## Step 1 — Merchant Invites Staff

The merchant initiates a staff invitation from the merchant dashboard.

The merchant provides only the information necessary to initiate the invitation.

The merchant does not create the staff member's complete record.

---

## Step 2 — Main Street Creates Pending Staff Record

Main Street creates a pending Merchant Staff Record associated exclusively with the merchant.

The record remains inactive until the staff onboarding process is completed.

---

## Step 3 — Staff Receives Invitation

The invited staff member receives instructions to complete the merchant's staff onboarding process.

The invitation is associated with the specific merchant and pending staff record.

---

## Step 4 — Staff Completes Onboarding

The staff member provides the information required by the merchant and Main Street.

The information may include:

* Name
* Contact information
* Required business information
* Other information configured as necessary for the business

The staff member submits the information for processing.

---

## Step 5 — Main Street Validates the Submission

Main Street validates the submitted information according to applicable platform and merchant rules.

If information is incomplete or invalid, the staff member is asked to correct it.

The merchant does not need to recreate the staff record.

---

## Step 6 — Staff Record Becomes Active

Following successful onboarding, Main Street activates the Merchant Staff Record.

The record remains associated exclusively with the merchant that initiated the invitation.

---

## Step 7 — Main Street Assigns Staff Number

Main Street automatically generates a unique Staff Number for the staff member's relationship with the merchant.

The merchant does not choose the Staff Number.

The Staff Number becomes the merchant-specific operational identifier for that staff record.

---

## Step 8 — Merchant Assigns Role

The merchant assigns the staff member's role.

Examples include:

* Manager
* Sales Assistant
* Receptionist
* Mechanic
* Hair Stylist
* Consultant

Roles are determined by the merchant's business model.

---

## Step 9 — Merchant Assigns Responsibilities

The merchant determines what the staff member is responsible for.

Examples include:

* Managing bookings
* Serving customers
* Preparing orders
* Managing inventory
* Providing services
* Handling customer communications

Responsibilities may vary independently of the staff member's role.

---

## Step 10 — Merchant Assigns Access

The merchant determines what the staff member is permitted to access and perform within the merchant's Main Street environment.

Access may include:

* Viewing bookings
* Managing bookings
* Creating orders
* Viewing customers
* Managing assigned services
* Viewing inventory
* Other authorised business functions

Access shall follow the platform's permission model.

---

## Step 11 — Staff Accesses Merchant Environment

The staff member accesses Main Street through the merchant's authorised environment.

Staff access is associated with:

* The merchant
* The Merchant Staff Record
* The Staff Number
* The permissions assigned by the merchant

The staff member does not independently access a generic Main Street staff environment.

---

# 8. Staff Number

The Staff Number is a **merchant-specific identifier** generated by Main Street.

For example:

```text
Merchant A
Staff Number: ST-0042

Merchant B
Staff Number: ST-0017
```

The same individual may therefore have different Staff Numbers for different merchants.

The Staff Number shall:

* Be automatically generated
* Be unique within the applicable merchant context
* Remain associated with the Merchant Staff Record
* Be used for authorised operational identification
* Support reporting and audit trails
* Not contain sensitive personal information
* Not function as a Main Street-wide identity
* Not be used as the sole authentication factor

---

# 9. Merchant Staff Record

The Merchant Staff Record represents the staff member's relationship with one merchant.

It may contain:

* Staff Number
* Staff-provided information
* Merchant-assigned role
* Merchant-assigned responsibilities
* Access permissions
* Operational associations
* Staff status
* Relevant historical information

The Merchant Staff Record belongs to the merchant context.

---

# 10. Staff and Multiple Merchants

A person may work for more than one merchant.

Each merchant establishes an independent staff relationship.

For example:

```text
Person
│
├── Merchant A
│     └── Staff Record
│          └── Staff Number: A-0042
│
├── Merchant B
│     └── Staff Record
│          └── Staff Number: B-0017
│
└── Merchant C
      └── Staff Record
           └── Staff Number: C-0088
```

Merchant A cannot access Merchant B's staff relationship.

Merchant B cannot access Merchant A's staff relationship.

Roles, responsibilities, access and operational history remain merchant-specific.

---

# 11. Staff Joining Another Merchant

When a staff member leaves one merchant and joins another merchant, the new merchant must establish a new staff relationship.

The new merchant:

1. Invites the staff member.
2. Staff member completes the new merchant's onboarding process.
3. Main Street creates a new Merchant Staff Record.
4. Main Street assigns a new Staff Number.
5. The new merchant assigns roles.
6. The new merchant assigns responsibilities.
7. The new merchant assigns access.

The previous merchant's Staff Number, roles, responsibilities, permissions and operational history are not transferred to the new merchant.

---

# 12. Information Reuse

Where Main Street can safely reduce repetitive data entry, previously supplied staff information may be made available to the staff member during a new merchant onboarding process, subject to appropriate consent and privacy controls.

Information belonging to the previous merchant shall not be disclosed to the new merchant.

The new Merchant Staff Record must be independently established.

Information reuse exists to reduce staff administration, not to transfer employment records between merchants.

---

# 13. Staff Access Model

Staff access is always contextual.

```text
Merchant
   ↓
Merchant Environment
   ↓
Merchant Staff Record
   ↓
Staff Number
   ↓
Assigned Permissions
   ↓
Authorised Operations
```

The staff member cannot bypass the merchant relationship to obtain platform-wide access.

Merchant administrators retain control over staff access.

---

# 14. Staff Lifecycle

A Merchant Staff Record follows the lifecycle:

```text
Invitation
   ↓
Pending
   ↓
Onboarded
   ↓
Configured
   ↓
Active
   ↓
Inactive
```

A record may become inactive when:

* The staff member leaves the business
* The merchant removes access
* The merchant suspends the staff relationship
* Other authorised business conditions require deactivation

Historical records remain intact.

---

# 15. Staff Availability

Staff membership does not imply staff scheduling.

A staff member may be active without having:

* A recurring schedule
* Defined working hours
* Booking responsibilities
* Appointment allocation

Where a business requires staff availability, the relevant operational capability shall determine how availability is established.

Staff Administration shall not impose scheduling on businesses that do not require it.

---

# 16. Booking Integration

Where a service requires staff participation, Booking Management may use authorised staff information.

The booking workflow may consider:

* Staff capability
* Assigned responsibility
* Staff availability
* Existing commitments
* Business rules
* Other operational constraints

A staff schedule is not automatically treated as the complete representation of availability.

Booking Management remains responsible for determining whether the booking can be accepted.

---

# 17. Business Order Integration

Where staff participation is required for an order, authorised staff may be associated with the relevant operational activity.

Examples include:

* Order preparation
* Customer service
* Collection handling
* Service fulfilment

Staff participation shall not be required where the business does not use staff for the relevant operation.

---

# 18. Business Rules

* Staff relationships belong to individual merchants.
* Staff do not have platform-wide Main Street staff accounts.
* Staff onboarding is merchant-specific.
* Staff members provide their own onboarding information.
* Main Street automatically creates the Merchant Staff Record.
* Main Street automatically assigns the Staff Number.
* Staff Numbers are merchant-specific.
* Staff Numbers shall not be reused within the applicable merchant context.
* Merchants control roles.
* Merchants control responsibilities.
* Merchants control access.
* Staff access shall be limited to the merchant environment for which it was granted.
* Staff joining another merchant must establish a new staff relationship.
* Previous merchant data shall not be transferred to another merchant.
* Permitted staff-provided information may be reused to reduce repetitive onboarding.
* Staff onboarding does not imply staff scheduling.
* Staff deactivation shall not delete historical business records.

---

# 19. Platform Responsibilities

Main Street shall:

* Facilitate staff invitations.
* Maintain pending staff onboarding records.
* Provide the staff onboarding process.
* Validate submitted information.
* Create Merchant Staff Records.
* Automatically generate Staff Numbers.
* Maintain staff lifecycle state.
* Enforce merchant-specific access boundaries.
* Support merchant-defined roles.
* Support merchant-defined responsibilities.
* Support merchant-defined permissions.
* Provide authorised staff information to relevant business capabilities.
* Preserve historical staff associations.
* Minimise repetitive administration.

Main Street shall not:

* Treat staff as platform-wide employees.
* Give staff independent access to Main Street outside a merchant relationship.
* Allow one merchant to access another merchant's staff records.
* Transfer one merchant's staff permissions to another merchant.
* Automatically transfer roles or responsibilities between merchants.
* Force businesses to configure staff schedules.
* Require merchants to manually reproduce staff information unnecessarily.

---

# 20. Success Criteria

The workflow is successful when:

* A merchant can invite a staff member with minimal effort.
* The staff member can provide their own required information.
* A merchant-specific Staff Record is created.
* A unique Staff Number is automatically assigned.
* The merchant can assign the appropriate role.
* The merchant can assign responsibilities.
* The merchant can control access.
* Staff can operate within the authorised merchant environment.
* Staff cannot access another merchant's environment through the relationship.
* Staff joining another merchant can be onboarded without unnecessary repetition.
* Previous merchant information remains protected.
* Businesses without staff remain unaffected by the capability.

---

# 21. Postconditions

Following successful onboarding:

* A Merchant Staff Record exists.
* The record belongs exclusively to the relevant merchant.
* A unique Staff Number has been assigned.
* The staff member's submitted information is recorded.
* Merchant-defined roles are recorded.
* Merchant-defined responsibilities are recorded.
* Merchant-defined access permissions are active.
* Authorised business capabilities can use the staff information.

---

# 22. Related Workflows

Previous:

* BO-10 — Payment Orchestration

Related:

* BO-05 — Booking Management
* BO-08 — Service Catalogue Management
* Business Order Management
* Customer Relationship Management

Next:

* BO-12 — Reporting & Business Insights

Platform Services:

* Identity & Access Control
* AI Services

---

# 23. Design Statement

Main Street treats staff administration as a merchant-owned business capability rather than a platform-wide identity system.

The merchant remains in control of the business relationship:

> **Invite → Assign → Authorise**

The staff member supplies their own required onboarding information:

> **Complete Onboarding → Confirm Information**

Main Street performs the administrative coordination:

> **Validate → Create Merchant Staff Record → Assign Staff Number → Enforce Access → Enable Operations**

This approach minimises merchant administration while maintaining clear ownership, strong tenant isolation and merchant control.

A staff member may participate in multiple businesses, but each business relationship remains independent.

Main Street therefore facilitates staff administration without becoming the owner of employment relationships.
