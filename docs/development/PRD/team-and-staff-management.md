# **28. Team, Staff & Payroll Support**

**Revision:** 4 September 2026  
**Status:** Current product intent

## **28.1 Purpose**

The Team, Staff & Payroll area enables businesses with multiple people to manage workforce access, responsibilities, schedules, operational accountability and—where enabled—payroll from one Main Street operating environment.

Main Street must remain simple for sole traders while scaling naturally to businesses with employees, workers, contractors and multiple operational roles.

Payroll is an optional business capability. It does not change the fundamental workforce access model and is intentionally separated from legal-employment classification, customer Payment and merchant subscription billing.

---

## **28.2 Design Philosophy**

The workforce area is founded on two principles:

> **Every person should have only the authority required for their work.**

> **Payroll should reduce workforce administration without turning Main Street into a jurisdiction-specific tax authority.**

Business owners remain in control of staffing, compensation decisions and payroll approval while Main Street provides governed workflows and integrations.

---

## **28.3 Core Separation**

Main Street must preserve the following distinctions:

```text
Main Street Identity
    ≠ Merchant Membership
    ≠ staff role / authority
    ≠ legal employment status
    ≠ Payroll Participant
    ≠ compensation arrangement
    ≠ Pay Run
    ≠ payroll-provider result
    ≠ payout / bank settlement
```

A person may work for a merchant without being paid through Main Street.

A payroll participant may require compensation administration without having normal Main Street staff-operational access.

A Merchant Membership therefore MUST NOT automatically create payroll participation, compensation terms or legal-employment claims.

---

## **28.4 Objectives**

The Team, Staff & Payroll area shall:

- support multiple workforce members;
- provide merchant-scoped role and privilege management;
- enable appointment/resource allocation where applicable;
- maintain individual accountability and auditability;
- protect customer, merchant and payroll information;
- scale from sole traders to growing teams;
- integrate with scheduling, orders, inventory and analytics where relevant;
- support optional provider-backed payroll administration; and
- give an individual secure access to their own payroll documents without exposing merchant-wide payroll information.

---

## **28.5 Workforce Roles and Authority**

Main Street may provide merchant-facing roles such as Business Owner/Controller, Manager and Staff Member, but role labels are not authority by themselves.

Authority shall resolve to explicit merchant-scoped privileges governed by the accepted workforce/access model.

Examples may include:

- manage bookings;
- manage orders;
- manage products/services;
- manage inventory;
- respond to enquiries;
- manage staff memberships;
- manage workforce schedules;
- view operational reports;
- manage payroll configuration;
- prepare payroll;
- approve payroll; and
- view payroll reports.

Payroll privileges must be narrower than ordinary staff-management authority where appropriate.

A manager who may schedule staff MUST NOT automatically gain access to compensation or payroll information.

---

## **28.6 Staff Profiles**

A staff/member-facing profile may contain operational information such as:

- name;
- job title;
- profile photograph;
- business contact details;
- biography;
- skills or specialisations;
- working availability; and
- membership/workforce state.

Public staff profiles may be exposed for businesses where customers choose a particular practitioner or staff member.

Payroll data, private compensation terms, tax identifiers and bank information MUST NOT be exposed through public staff profiles.

---

## **28.7 Staff Availability and Scheduling**

Where applicable, a staff member may have business-operational availability including:

- working days;
- working hours;
- break periods;
- holiday/leave-related unavailability where supported; and
- temporary operational unavailability.

Booking and scheduling capabilities may consume appropriate staff/resource availability.

However:

> **Scheduled time is not automatically payroll time.**

A calendar entry, appointment allocation or booking duration MUST NOT be treated as hours worked or payable time unless a separately governed timekeeping/payroll input process establishes that meaning.

---

## **28.8 Appointment and Work Allocation**

Appointments or work may be allocated through supported methods such as:

### Customer Choice

The customer selects an eligible staff member.

### Automatic Assignment

Main Street resolves assignment from authoritative availability/capacity and merchant rules.

### Merchant Assignment

An authorised merchant actor assigns the work directly.

Allocation affects operations. It does not by itself create compensation or payable-time truth.

---

## **28.9 Internal Notes and Operational Evidence**

Staff may record authorised internal operational notes where the owning capability permits them.

Significant workforce actions should remain attributable and auditable, including examples such as:

- Booking created or modified;
- Order fulfilled;
- Inventory changed;
- Refund action initiated;
- staff membership/role changed;
- payroll input changed;
- Pay Run prepared;
- Pay Run approved/rejected; and
- provider payroll execution/reconciliation action performed.

Audit evidence must not become a substitute for the authoritative owner of the business fact.

---

## **28.10 Notifications**

Staff should receive notifications relevant to their responsibilities and permitted surfaces.

Examples may include:

- appointment assignment/cancellation;
- customer arrival;
- new enquiry;
- shift/work reminder where supported;
- low inventory alert; and
- payroll document available.

Payroll notification payloads should minimise sensitive financial information. A notification may say that a payslip is available rather than exposing pay amounts on a lock screen.

---

## **28.11 Staff Performance**

The platform may provide appropriately governed operational metrics such as:

- appointments completed;
- orders fulfilled;
- revenue associated with work;
- customer retention; and
- operational throughput.

Performance analytics should support business improvement rather than uncontrolled employee surveillance.

Payroll or compensation data must not be mixed into performance ranking merely because the information is technically available.

---

# **28.12 Payroll Support**

Main Street shall support payroll as an **optional post-MVP workforce capability**.

The product objective is to let a merchant administer payroll from the same operating environment used for staff and business operations while preserving clear authority boundaries.

Main Street should support a merchant-facing workflow conceptually covering:

```text
Payroll Participant
        ↓
compensation/pay arrangement
        ↓
Pay Period / Pay Run preparation
        ↓
approved payroll inputs
        ↓
payroll calculation fulfilment
        ↓
merchant review / approval
        ↓
statutory filing / payout fulfilment where supported
        ↓
reconciliation / durable evidence
        ↓
individual payroll documents
```

Exact production semantics remain subject to dedicated payroll design authority before implementation.

---

## **28.13 Payroll Provider Strategy**

Main Street should be provider-neutral.

Initial payroll support should prefer integration with qualified payroll providers for jurisdiction-specific responsibilities such as:

- tax and statutory deduction calculations;
- employer contribution calculations;
- statutory reporting/filing;
- jurisdiction-specific payroll forms/documents;
- payroll payment instructions or funds movement where supported; and
- provider compliance updates.

Main Street should own the merchant-facing orchestration, authority boundaries, approved inputs, provider binding, evidence/reconciliation and presentation required by its payroll capability.

The external provider does not become Main Street's business model.

Provider selection may change **how** payroll responsibilities are fulfilled; it must not silently redefine the meaning of Main Street's payroll workflow.

---

## **28.14 Payroll Participation Is Separate from Staff Access**

Main Street must not assume:

```text
ACTIVE Merchant Membership
    → automatically on payroll
```

or:

```text
Payroll Participant
    → automatically authorised for merchant operations
```

Examples that break this assumption include:

- owners who do not receive payroll through Main Street;
- temporary/contract workers;
- payroll participants who never use Main Street operational tools;
- staff who are paid through an external arrangement; and
- former staff whose historical payroll evidence must remain accessible/retained according to policy.

The exact Payroll Participant representation and its relationship to legal employment remain dedicated payroll-design questions.

---

## **28.15 Compensation and Payroll Inputs**

Compensation terms and payroll inputs are sensitive business facts.

Main Street may support structured inputs such as salary/hourly/fixed compensation, variable earnings, approved adjustments or time-derived inputs only where the applicable payroll design and provider contract define them.

Hard rule:

> **Main Street must never infer payable hours merely from bookings, appointments, schedules, device presence or customer activity.**

Where time-based payroll is supported, payable-time evidence must originate from an explicit timekeeping source or merchant-approved payroll input process.

---

## **28.16 Payroll Approval and Execution**

Payroll preparation, payroll approval and funds movement are distinct responsibilities.

Conceptually:

```text
prepare Pay Run
    ≠
approve Pay Run
    ≠
provider calculation complete
    ≠
statutory filing accepted
    ≠
payout instruction accepted
    ≠
funds settled
```

Main Street must not display a payroll run as paid merely because a calculation succeeded or an execution request was submitted.

Where provider uncertainty exists, Main Street should reconcile rather than repeat an action blindly.

For businesses requiring stronger internal controls, future payroll authority should permit separation between preparer and approver without making that separation mandatory for every microbusiness.

---

## **28.17 Individual Payroll Self-Service**

A workforce member should be able to access **their own** payroll documents securely from a personal device where Main Street payroll is enabled.

Examples may include:

- payslips;
- pay-run summaries relevant only to that individual;
- historical payroll documents; and
- provider/statutory documents where Main Street is permitted to expose them.

This requires a deliberately bounded **personal workforce self-service surface**.

It must not grant access to:

- other workers' pay;
- merchant-wide payroll reports;
- customer information;
- orders/bookings/inventory; or
- general merchant operational projections.

This surface is distinct from both the minimal identity/security surface and the authorised Merchant Operational Device Context defined by the accepted workforce architecture.

Exact authentication assurance, Exposure and document-delivery semantics require dedicated payroll/workforce design before implementation.

---

## **28.18 Payroll Privacy and Security**

Payroll data is highly sensitive personal and financial information.

Main Street payroll support must apply strict purpose limitation, access control, audit evidence, provider credential protection, data minimisation and retention/disposition rules.

Examples of sensitive material include:

- compensation amounts;
- tax identifiers;
- bank/payment details;
- statutory deductions;
- payroll documents; and
- provider credentials/references.

AI must not be given unrestricted payroll access merely because it assists the merchant elsewhere in Main Street.

Any AI-assisted payroll interaction must use purpose-built context and the same deterministic authorisation/approval contracts as direct interaction.

---

## **28.19 Team Communication**

Future versions may include lightweight internal communication such as shift notes, operational announcements, reminders and task assignments.

These features are intended for business coordination and are not intended to replace dedicated messaging platforms.

---

## **28.20 Multi-Location and International Expansion**

The architecture should support future businesses with multiple locations while preserving merchant scope and payroll authority.

Payroll introduces additional international concerns including:

- tax jurisdictions;
- payroll calendars;
- currencies;
- statutory reporting;
- provider availability; and
- worker-location/employment rules.

Main Street must not encode one country's payroll law as universal platform semantics.

Country-specific payroll support should be introduced through explicit jurisdiction/provider capability rather than hidden category branching.

---

## **28.21 Staff Lifecycle**

The workforce relationship shall support governed invitation, activation, suspension and ending.

Historical business and payroll evidence must not be destroyed merely because a person leaves the merchant.

Ending workforce access must remove current operational authority according to the workforce model, while payroll/document retention and post-employment self-service follow their own applicable payroll/privacy rules.

---

## **28.22 MVP Boundary**

Payroll is a product requirement but is **not part of the initial MVP**.

The MVP should preserve architectural extension points and avoid assumptions that would make payroll difficult later, but implementation of payroll calculation, statutory filing, payslips and payroll payout is deferred until the dedicated payroll design is accepted and deliberately sequenced.

---

## **28.23 Team, Staff & Payroll Statement**

Main Street should allow local businesses to grow from a sole trader into an employer without abandoning the platform.

Workforce access, scheduling and payroll should feel coherent to the merchant while remaining semantically separate underneath.

> **Main Street manages supported workforce operations as one experience, but it does not collapse identity, authority, employment, payroll calculation and money movement into one object or workflow.**

---

### End of Section 28 – Team, Staff & Payroll Support
