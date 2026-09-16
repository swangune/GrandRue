Good. We now move to MS-PROT-005 — Requirement & Policy Framework.

This layer is particularly important because it converts Main Street's understanding of a merchant into controlled operational behaviour without exposing the underlying complexity to the merchant.

MS-PROT-005 — Requirement & Policy Framework

Status: Proposed
Purpose: Define what a business operation requires, what the merchant is allowed to control, and how those decisions affect runtime behaviour.

The governing principle is:

> Main Street determines what is relevant; the merchant determines the permitted business policy within that relevant scope.




---

1. Three different things must remain separate

REQUIREMENT
"What must be known or satisfied?"

POLICY
"How may this business operate?"

CONFIGURATION
"What has this merchant chosen?"

Example:

Delivery
    ↓
Requirement:
    delivery address is required

Policy:
    delivery enabled/disabled

Configuration:
    delivery = enabled

They are related, but they are not the same object.


---

2. Requirements are contextual

A customer should never be presented with a giant universal Main Street form.

Instead:

Operation
    ↓
Applicable capability
    ↓
Requirements
    ↓
Relevant customer information

For example:

Grocery collection

Customer
    name
    contact
    collection information

Grocery delivery

Customer
    name
    contact
    delivery address

Mechanic

Customer
    name
    contact
    vehicle registration

Mechanic with mobile service

Customer
    name
    contact
    vehicle registration
    service location

Realtor viewing

Customer
    name
    contact

Potentially additional information if the merchant's configured process requires it.


---

3. Requirement structure

A requirement should conceptually contain:

Requirement
{
    id
    subject
    context
    data_type
    required_when
    validation
    sensitivity
    persistence
    purpose
}

For example:

vehicle_registration
    subject = customer
    context = vehicle_service
    data_type = vehicle_identifier
    required_when = service.requires_vehicle
    validation = vehicle_registration_format
    purpose = service_identification

The requirement itself does not determine how the UI looks.


---

4. Conditional requirements

This is one of the most important mechanisms.

Example:

IF delivery = enabled
THEN delivery_address = required

Another:

IF mobile_service = enabled
THEN service_location = required

Another:

IF payment_confirmation = required
THEN payment_method = required_before_confirmation

This creates a dependency graph:

Policy
   ↓
Capability condition
   ↓
Requirement activation


---

5. Requirements should be attached to operations, not merely merchants

This is subtle but important.

Don't say:

Merchant requires:
    address

Say:

Delivery operation requires:
    address

The same customer can therefore interact with the same merchant through multiple contexts without repeatedly providing irrelevant information.


---

6. Reusable customer information

Main Street should maintain reusable customer information where appropriate.

For example:

Customer Profile
    contact information
    saved addresses
    vehicles

But the operation determines whether a particular piece is relevant.

Example:

Mechanic booking
    ↓
vehicle required
    ↓
customer already has vehicle registered
    ↓
select existing vehicle

Main Street doesn't need to ask again.


---

7. Merchant policies

Now the second half.

A policy is a controlled choice exposed to the merchant.

For example:

Booking
├── customer cancellation
├── payment requirement
├── customer selection mode
└── allocation strategy

But the merchant must not see every possible policy.

This is crucial.


---

8. Policy eligibility

The capability configuration determines which policies are relevant.

For example:

Payment capability = disabled

Then:

Payment confirmation policy

should not appear in the merchant dashboard.

Similarly:

Delivery capability = disabled

means:

Delivery fee
Delivery address policy

are irrelevant and should not appear.

This gives us the simple dashboard we want.


---

9. Policy hierarchy

Policies can have dependencies.

Example:

Delivery
    enabled
       │
       ├── delivery method
       ├── delivery area
       ├── delivery fee
       └── address requirement

If:

Delivery = OFF

the entire dependent policy branch disappears.

The merchant isn't presented with configuration that has no meaning.


---

10. Policy types

We should classify policies.

Boolean

delivery_enabled = true

Enumeration

allocation = automatic | merchant

Quantity

maximum_guests = 4

Duration

service_buffer = 15 minutes

Conditional

payment_required_if = online_booking

Structured

opening_hours
delivery_area
cancellation_rules

But we should keep the permitted types small.


---

11. Policy constraints

A policy may have:

allowed_values
default_value
dependencies
conflicts
validation
merchant_visibility

For example:

allocation_strategy

allowed:
    automatic
    merchant

available_when:
    allocation capability enabled

The AI cannot choose:

allocation_strategy = quantum

because the schema doesn't permit it.


---

12. Immutable rules vs configurable policies

This distinction must be absolute.

Invariant

Merchant cannot change it.

A resource cannot be allocated twice incompatibly.

Policy

Merchant can choose.

Allocation strategy:
    automatic
    merchant

System configuration

Main Street may control it internally.

transaction isolation
event persistence
idempotency handling

The merchant should never see those.


---

13. Payment policy

Based on our latest decision, payment should be represented carefully.

We should not have:

payment_required = temporary_hold

Instead:

Payment
├── enabled
├── required_for_confirmation
└── optional

And separately:

Reservation/Hold
└── enabled / disabled

Thus:

Hold = OFF

is completely valid.

That is the normal fluid car-wash model.


---

14. Example: car wash

Capabilities:
    Service
    Booking
    Scheduling
    Payment

Policies:

Payment required = YES
Hold = OFF
Customer cancellation = YES

Customer:

select service
select time
review
pay
commit

If somebody else commits the slot first:

commit → conflict

The customer is returned to availability resolution.

No artificial 10-minute Main Street timer.


---

15. Example: motel

Capabilities:

Accommodation
Catalogue
Booking
Availability
Allocation
Payment

Policies:

customer_selection = category
allocation = automatic
payment_required = true
hold = false
availability_release = staff

Again:

payment does not automatically imply a hold.

If the motel genuinely requires guaranteed reservation during payment, then:

hold = true

becomes an explicit merchant policy/capability.


---

16. Policy changes must be versioned

A merchant changing:

payment_required

from:

false → true

can affect active operations.

Therefore configuration cannot simply overwrite the previous value without context.

We need:

Configuration
    version
    effective_from
    created_by
    approved_by
    status

Potential states:

DRAFT
APPROVED
ACTIVE
SUPERSEDED

This is particularly important because Main Street is generating configuration from inference.


---

17. Merchant approval becomes a formal transition

INFERRED
    ↓
DRAFT
    ↓
MERCHANT_APPROVED
    ↓
ACTIVE

The AI can generate:

DRAFT

but cannot silently activate merchant-impacting configuration.


---

18. Policy dependencies must be validated

Suppose AI infers:

delivery = enabled
delivery_address = required

Valid.

But:

delivery = disabled
delivery_address = required

should fail validation unless another capability independently requires the address.

The validator therefore checks:

Capability dependency
+
Policy dependency
+
Requirement dependency


---

19. Configuration compiler

Our Configuration Compiler now has a much clearer job:

Candidate configuration
       ↓
Schema validation
       ↓
Capability validation
       ↓
Policy validation
       ↓
Requirement activation
       ↓
Dependency resolution
       ↓
Conflict detection
       ↓
Invariant validation
       ↓
Configuration package

Only then can it become active.


---

20. The merchant dashboard is consequently generated from configuration

This is a major architectural consequence.

We don't manually design:

Mechanic dashboard
Motel dashboard
Grocery dashboard
Realtor dashboard

Instead:

Merchant configuration
       ↓
Eligible controls
       ↓
Merchant Dashboard Presentation Contract

A motel gets motel-relevant controls.

A mechanic gets mechanic-relevant controls.

But the underlying operational engine remains shared.


---

21. Customer UI follows the same principle

Operational configuration produces semantic requirements and available operations.

Then a separate presentation system decides:

what should be displayed
how it should be displayed
what interaction pattern is appropriate

This preserves our recent decision:

> Operational capabilities do not configure UI.



They provide the semantic/operational contract that another system can use to construct the UI.


---

22. The architecture is becoming very coherent

BUSINESS UNDERSTANDING
                       │
                       ▼
                  AI INFERENCE
                       │
                       ▼
              CANDIDATE CONFIG
                       │
                       ▼
              CONFIGURATION COMPILER
                       │
       ┌───────────────┼────────────────┐
       ▼               ▼                ▼
   Capability       Policy          Requirement
   validation       validation      validation
       │               │                │
       └───────────────┼────────────────┘
                       ▼
                 ACTIVE CONFIG
                       │
              ┌────────┴────────┐
              ▼                 ▼
        Operational Runtime   Presentation
                                  │
                          ┌───────┴───────┐
                          ▼               ▼
                     Customer UI     Merchant UI

This is the architecture I would continue with.


---

23. MS-PROT-005 decisions

Decision	Status

Requirements are contextual	Accepted
Requirements attach to operations/capabilities	Accepted
Customer data is requested only when relevant	Accepted
Policies are separate from requirements	Accepted
Policies are separate from invariants	Accepted
Merchants only see relevant policies	Accepted
AI cannot invent policy values	Accepted
Payment ≠ reservation/hold	Accepted
Payment expiry belongs to payment infrastructure	Accepted
Hold is optional	Accepted
Configuration is versioned	Accepted
Merchant approval precedes activation	Accepted
Operational configuration does not define UI	Accepted



---

Next: MS-PROT-006 — Availability, Capacity & Allocation

Now we can tackle the hardest operational subsystem with the foundations in place.

It needs to answer, precisely:

What is available?
How much is available?
When is it available?
What makes it unavailable?
Who/what can consume it?
When does consumption become authoritative?
How do we allocate it?
How do we prevent double allocation?
How do we handle concurrent customers?
How do motel, service, retail and real-estate scenarios use the same mechanism?

And importantly, we will not start from "hotel availability" or "appointment availability." We will derive both from the common semantic model we have now established.