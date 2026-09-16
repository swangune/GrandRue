# MS-PROT-020 v1.4 — Semantic Amendment: Operational Objects, Resources, Effects & Typed Relationships

**Status:** **ACCEPTED after falsification and validation**  
**Amends:** MS-PROT-020 v1.3  
**Purpose:** Resolve the three semantic ambiguities identified during review of MS-PROT-043:

1. Operational Object versus Resource.
2. Authoritative operation-effect algebra.
3. Typed relationships between capability-owned business objects.

This amendment does **not** yet change repository code. It establishes the semantics that the implementation must subsequently conform to.

---

# 1. Governing correction

The existing semantic model uses `Resource` too broadly.

Current registry code defines `OwnedResourceDefinition` as an owner-scoped lifecycle-bearing object containing states and an initial state.  Operations then target those resources through creation or transition effects. 

That makes the implementation capable of treating:

```text
Appointment
Booking
Enquiry
CustomerContext
Listing
```

as "resources".

Semantically, that is incorrect.

The corrected model is:

```text
Operational Object
        │
        ├── may have lifecycle
        ├── may own authoritative data
        ├── may participate in relationships
        │
        └── may play Resource role
                     │
                     ▼
              Capacity / Allocation
```

Therefore:

> **Operational Object and Resource are distinct concepts. Resource is a role that some operational or configured subjects can play; it is not the universal container for business state.**

---

# PART I — OPERATIONAL OBJECT VERSUS RESOURCE

# 2. Operational Object

An **Operational Object** is:

> **A capability-owned, identity-bearing business object whose authoritative data and, where applicable, lifecycle are managed by Main Street runtime.**

Examples:

```text
Appointment
Booking
Enquiry
CustomerContext
Listing
Order
Conversation
Allocation
MerchantScheduleIntent
```

An Operational Object may contain:

```text
identity
merchant scope
typed authoritative data
lifecycle state where applicable
typed relationships
provenance
```

It does not need to possess all of these.

---

# 3. Operational Object is not a universal business superclass

This concept establishes semantic classification.

It does **not** imply that Java must eventually contain:

```java
class Appointment extends OperationalObject
```

or a huge shared runtime base class.

The architectural meaning is:

```text
Appointment
Booking
Enquiry
...
```

share the property that they are durable, addressable business objects.

Their actual semantics remain capability-owned.

---

# 4. Lifecycle is optional

An Operational Object does not require an elaborate state machine merely to qualify.

For example:

```text
Appointment
    CONFIRMED
    CANCELLED
    ...

Enquiry
    OPEN
    CLOSED
    ...
```

may genuinely require lifecycle states.

A simple relationship object might not.

Therefore:

> **Identity and authoritative business existence define an Operational Object; a state machine is added only when materially required.**

This corrects the current assumption that every `OwnedResourceDefinition` requires at least one state and an initial state. 

---

# 5. Resource

A **Resource** is:

> **A semantic role played by a subject whose usable capacity can constrain an operation and against which Main Street may evaluate availability or establish allocation.**

Examples:

```text
Room 104
Mechanic Alice
Service Bay 2
Consultant David
Excavator 7
Executive Room Pool
Delivery Capacity
```

The defining question is not:

> Does this thing have state?

The defining question is:

> Can its usable capacity constrain an operation?

---

# 6. Resource is a role

This is the important correction.

Consider:

```text
StaffMember Alice
```

Alice may be an Operational Object because Main Street manages staff identity, employment relationship and state.

Alice may additionally play:

```text
RESOURCE
```

for:

```text
Hair Appointment
Mechanic Appointment
Consultation
```

Therefore:

```text
Operational Object
    StaffMember Alice

Resource role
    Alice contributes schedulable capacity
```

Similarly:

```text
Room 104
```

can be both:

```text
Operational Object
+
Resource
```

But:

```text
Appointment A100
```

is an Operational Object and normally **not** a Resource.

---

# 7. Resource does not imply individual physical object

A Resource may represent pooled capacity.

Example:

```text
Executive Room Capacity
capacity = 5
```

The customer may reserve:

```text
1 Executive Room
```

without selecting:

```text
Room 104
```

Therefore Resource semantics must support both:

```text
INDIVIDUAL RESOURCE

Room 104
Staff Alice
Service Bay 2
```

and:

```text
POOLED RESOURCE / CAPACITY SUBJECT

Executive Rooms
Delivery capacity
Concurrent consultation capacity
```

The exact implementation of resource pools remains downstream.

---

# 8. Capacity

Capacity is:

> **The bounded usable amount of a Resource within the applicable context.**

It is not another Operational Object merely because Main Street calculates it.

Examples:

```text
5 rooms

1 staff member at one time

3 concurrent bays

10 units
```

Capacity can vary by:

```text
time
resource state
configuration
existing allocations
policy
```

---

# 9. Allocation

Allocation remains:

> **An authoritative scoped claim against Resource capacity for a particular use.**

This remains one of the genuinely reusable low-level semantics.

Current code already separates allocation scope into quantity and time-window forms.   

That abstraction survives.

---

# 10. Classification examples

| Subject | Operational Object | Resource role |
|---|---:|---:|
| Booking | Yes | No |
| Appointment | Yes | No |
| Enquiry | Yes | No |
| CustomerContext | Yes | No |
| Property Listing | Yes | Normally no |
| Staff member | Yes | Often yes |
| Hotel room | Yes | Yes |
| Equipment | Yes | Yes |
| Service bay | Yes | Yes |
| Executive-room pool | May be configured rather than runtime object | Yes |
| Merchant UNAVAILABLE intent | Yes | No |
| Working hours | No — configuration | No |
| Recurring break | No — configuration | No |

This distinction survives the merchant scenarios we have developed.

---

# 11. Property Listing is not a Resource merely because it concerns property

This deserves explicit protection.

A realtor's:

```text
Listing L123
24 High Street
```

is an Operational Object representing published business information.

The underlying property might eventually participate in some Resource capability.

But:

```text
Listing
≠
Resource
```

A listing can be sold, rented, withdrawn or enquired about without becoming an allocation primitive.

---

# 12. Merchant UNAVAILABLE intent

A merchant-created entry:

```text
UNAVAILABLE
14:00–15:00
"Dentist appointment"
```

is an Operational Object representing merchant scheduling intent.

It is not:

```text
Appointment
Booking
Resource
WorkingHours
Break
```

It contributes a scheduling constraint from which Appointment availability is calculated.

---

# 13. Working hours and breaks

These remain configuration.

```text
Working hours
Recurring breaks
```

do not need their own runtime Operational Objects merely because they influence availability.

This retains the earlier scheduling distinction:

```text
CONFIGURATION
    working hours
    recurring breaks

OPERATIONAL OBJECT
    merchant UNAVAILABLE intent
    Appointment

DERIVED
    Appointment availability
```

---

# 14. Operational Object verdict

**ACCEPTED**

The previous broad Resource model is superseded semantically.

Canonical rule:

> **Every lifecycle-bearing business object is not a Resource. Resource is a capacity-constraining role. Capability-owned persistent business state belongs to Operational Objects.**

---

# PART II — AUTHORITATIVE EFFECT ALGEBRA

# 15. Why operation effects require revision

The old prototype assumes:

```text
Operation
    =
one Resource
+
one Transition
+
one Privilege
```



The newer registry correctly moved toward:

```text
Operation
    ↓
bounded collection of effects
```



However, the current effect model still permits only:

```text
CREATE RESOURCE
TRANSITION RESOURCE STATE
```



That is insufficient.

---

# 16. Counterexample — Appointment rescheduling

Suppose:

```text
Appointment A100

scheduledInterval =
10:00–11:00
```

is moved to:

```text
15:00–16:00
```

The Appointment may remain:

```text
CONFIRMED
```

throughout.

Nothing necessarily requires a lifecycle transition.

Yet authoritative data changed.

Therefore:

```text
CREATE
+
TRANSITION
```

cannot express this operation correctly.

We need authoritative **data mutation**.

---

# 17. Counterexample — Listing price change

```text
Listing L123

price:
£1,200
    ↓
£1,300
```

The Listing remains the same object.

Its lifecycle may remain:

```text
ACTIVE
```

Again:

```text
no creation
no state transition
```

but authoritative business state changed.

---

# 18. Counterexample — Customer contact change

```text
CustomerContext C1

email A
    ↓
email B
```

The CustomerContext remains C1.

No lifecycle transition is implied.

Again, data mutation is required.

---

# 19. Canonical authoritative effects

The minimum effect algebra is therefore:

```text
CREATE_OBJECT

TRANSITION_STATE

MUTATE_DATA

ESTABLISH_RELATIONSHIP

REMOVE_RELATIONSHIP

CLAIM_ALLOCATION

RELEASE_ALLOCATION
```

These are semantic effect categories.

They are not merchant-configurable instructions.

---

# 20. CREATE_OBJECT

Definition:

> **Establish a new capability-owned Operational Object with new stable identity and its valid initial authoritative condition.**

Examples:

```text
create Enquiry
create Appointment
create Booking
create CustomerContext
create merchant UNAVAILABLE intent
```

Creation does not automatically imply:

```text
customer relationship
allocation
event
notification
```

Those are separate applicable effects/outcomes.

---

# 21. TRANSITION_STATE

Definition:

> **Move an existing Operational Object from one registered lifecycle state to another valid registered lifecycle state.**

Examples:

```text
Appointment
CONFIRMED → CANCELLED

Listing
ACTIVE → WITHDRAWN

Enquiry
OPEN → CLOSED
```

State remains owner-scoped.

Generic data mutation cannot bypass lifecycle transition rules.

---

# 22. MUTATE_DATA

Definition:

> **Change authoritative typed data owned by an existing Operational Object without changing its identity.**

Examples:

```text
Appointment.scheduledInterval

Listing.price

CustomerContext.email

Booking.reservationScope

MerchantScheduleIntent.interval
```

However, this cannot become:

```text
arbitrary JSON patch
arbitrary property path
merchant-authored script
```

The operation definition must identify a registered, capability-owned data binding.

---

# 23. Data mutation must be typed

Conceptually:

```text
MUTATE_DATA
{
    target_object_type
    data_concept
    permitted mutation semantics
}
```

Examples:

```text
Appointment
    SCHEDULED_INTERVAL

Listing
    ASKING_PRICE

CustomerContext
    EMAIL_CONTACT
```

The capability declares which Operation can change which data.

Merchant configuration cannot create new mutations.

---

# 24. ESTABLISH_RELATIONSHIP

Definition:

> **Create a registered typed relationship from one addressable business object to another valid target.**

Examples:

```text
Appointment → CustomerContext

Booking → CustomerContext

Enquiry → Listing

Enquiry → Appointment

Enquiry → Booking
```

This does not mutate the target object's owned business state.

---

# 25. REMOVE_RELATIONSHIP

Definition:

> **Remove an existing relationship where the owning semantic model permits the relationship to be removed.**

It does not delete either endpoint.

Therefore:

```text
REMOVE_RELATIONSHIP
≠
DELETE_OBJECT
```

---

# 26. CLAIM_ALLOCATION

Definition:

> **Establish an authoritative capacity claim against an applicable Resource for a registered business use.**

Examples:

```text
Appointment → Staff time

Booking → room-category capacity

Order → inventory capacity
```

This effect must operate at the authoritative consistency boundary that prevents invalid over-allocation.

---

# 27. RELEASE_ALLOCATION

Definition:

> **Terminate an existing capacity claim such that the claimed Resource capacity may participate in fresh availability calculations.**

Examples:

```text
Appointment cancelled
    ↓
release Appointment-owned allocation

Booking cancelled
    ↓
release Booking-owned capacity claim
```

Release does not directly mean:

```text
available = true
```

Availability must be recalculated.

---

# 28. No generic DELETE effect is accepted

At present:

```text
DELETE_OBJECT
```

is **not** accepted into the core effect algebra.

Business history such as:

```text
Booking
Appointment
Enquiry
```

normally changes lifecycle rather than disappearing.

Privacy erasure, hard deletion, retention expiry and archival are separate data-governance concerns.

A future use case may justify deletion explicitly.

---

# 29. Domain Event is not an authoritative mutation effect

The current architecture correctly distinguishes operational effects from events.

A Domain Event represents a committed fact, not the action that makes the fact true.

Therefore:

```text
TRANSITION Appointment → CANCELLED
RELEASE Allocation
        ↓
commit succeeds
        ↓
AppointmentCancelled event
```

not:

```text
AppointmentCancelled event
        ↓
event makes Appointment cancelled
```

MS-PROT-020 already establishes this distinction. 

---

# 30. Effects are not operation chaining

An operation may have:

```text
TRANSITION_STATE
+
RELEASE_ALLOCATION
+
MUTATE_DATA
```

without meaning:

```text
effect 1 triggers operation 2
operation 2 triggers operation 3
```

The effects jointly describe one coherent authoritative outcome.

This preserves the explicit rejection of generic trigger→consequence composition in MS-PROT-020. 

---

# 31. Effect-set atomicity

Where an invariant requires the effects to become true together:

> **The complete applicable effect set shall commit at the same authoritative consistency boundary.**

Example:

```text
Reschedule Appointment

release old capacity
+
claim new capacity
+
change scheduled interval
```

must not produce a durable state such as:

```text
old allocation released
but
Appointment still says old time
```

or:

```text
Appointment changed
but
new capacity was never acquired
```

where those facts participate in the same local invariant.

---

# 32. Effect order is not business workflow

The semantic specification should describe:

```text
required resulting authoritative state
```

rather than inventing a workflow from implementation ordering.

An implementation may internally:

```text
lock
validate
write
flush
```

in some order.

That does not give those technical steps independent business meaning.

---

# 33. Cross-capability authority remains protected

An Appointment operation may establish:

```text
Appointment.customer → CustomerContext C100
```

but it cannot arbitrarily change:

```text
CustomerContext
```

because Customer capability retains authority over CustomerContext.

Likewise:

```text
Enquiry → Listing
```

does not grant Enquiry capability authority to change:

```text
Listing.price
Listing.status
```

Relationship does not transfer ownership.

---

# 34. Synchronous cross-capability coordination

Where one user command genuinely requires authoritative effects owned by different capabilities, Main Street may coordinate independent capability-owned semantics within an applicable consistency boundary.

Example:

```text
Guest creates first Appointment
```

may require:

```text
Customer capability
    create CustomerContext

Appointment capability
    create Appointment
    establish customer relationship
```

This is **synchronous consistency coordination**, not ownership collapse.

MS-PROT-020 already distinguishes such coordination from static relationships and event reaction. 

---

# 35. Effect algebra verdict

**ACCEPTED**

Canonical authoritative effect set:

```text
CREATE_OBJECT
TRANSITION_STATE
MUTATE_DATA
ESTABLISH_RELATIONSHIP
REMOVE_RELATIONSHIP
CLAIM_ALLOCATION
RELEASE_ALLOCATION
```

Future effect categories require demonstrated counterexamples that cannot be represented safely through this set.

---

# PART III — TYPED RELATIONSHIP MODEL

# 36. Why typed relationships are required

Our newer domains now require explicit relationships:

```text
Enquiry → Listing

Appointment → CustomerContext

Booking → CustomerContext

Appointment → Offering

Enquiry → Appointment

Enquiry → Booking
```

Representing these only as:

```text
String subjectIdentifier
```

would lose:

```text
target type
owner
tenant scope
cardinality
relationship meaning
referential validity
```

A typed relationship model is therefore justified.

---

# 37. Relationship definition

A **RelationshipDefinition** is:

> **A platform-owned, source-owned contract defining the permitted semantic relationship between one business object type and another.**

Conceptually:

```text
RelationshipDefinition
{
    identifier
    role
    source_type
    target_type
    cardinality
    scope_constraint
}
```

It defines valid structure.

The actual runtime association is a relationship instance/reference.

---

# 38. Relationship ownership

The **source semantic owner owns the meaning of the relationship**.

Example:

```text
Appointment
    customer → CustomerContext
```

Appointment capability owns the fact that:

> an Appointment must relate to a CustomerContext.

Customer capability still owns:

```text
CustomerContext
```

itself.

This is crucial.

---

# 39. Relationships do not transfer authority

If:

```text
Enquiry E1
    subject → Listing L1
```

then Enquiry capability may:

```text
reference L1
display appropriate projection
retain provenance
```

but may not:

```text
change L1 price
withdraw L1
edit L1 address
```

without executing an authorised Listing operation.

Canonical invariant:

> **Reference authority is not mutation authority.**

---

# 40. Exact typing

Relationship definitions must specify valid target semantics.

Incorrect:

```text
subjectIdentifier = "123"
```

with no semantic type.

Correct conceptually:

```text
Enquiry.subject
    target type = Listing
```

The registry/compiler can then reject:

```text
Enquiry.subject
    → PaymentReceipt
```

unless that relationship is registered.

---

# 41. Relationship role

Different typed relationships may share a semantic role.

Examples:

```text
Enquiry → Listing
role = SUBJECT

Enquiry → Product
role = SUBJECT

Enquiry → Opportunity
role = SUBJECT
```

This allows a generic Enquiry experience without inventing:

```text
PropertyEnquiry
ProductEnquiry
ScholarshipEnquiry
```

as core runtime classes.

The exact source-target combination remains registered.

---

# 42. `EnquirySubject` is therefore not a primitive

This resolves one of MS-PROT-043's open questions.

`EnquirySubject` should **not** become:

```text
global primitive
universal base class
generic database object
```

Instead:

> **SUBJECT is a relationship role used by registered Enquiry-to-subject relationship definitions.**

Thus:

```text
Enquiry
    └── SUBJECT → Listing
```

or:

```text
Enquiry
    └── SUBJECT → Opportunity
```

---

# 43. Cardinality is registered, not assumed globally

Relationships require cardinality.

Candidate contracts include:

```text
OPTIONAL_ONE      0..1
REQUIRED_ONE      1
ZERO_OR_MORE      0..*
ONE_OR_MORE       1..*
```

Examples:

```text
Appointment.customer
    REQUIRED_ONE

Booking.customer
    REQUIRED_ONE

Enquiry.customerContext
    OPTIONAL_ONE
```

The cardinality of `Enquiry.subject` shall be determined by its registered semantic relationship rather than presumed universally by the core type system.

---

# 44. Why Enquiry subject cardinality should not be hard-coded yet

The common storefront path is:

```text
Listing L123
    ↓
Enquire
    ↓
subject = L123
```

One subject is sufficient.

But future valid cases may include enquiries relating to multiple objects.

We do not need to solve that now.

Therefore:

> **MS-PROT-043 may use the single-subject flow as its normal UX case, but the primitive relationship mechanism shall not hard-code "all enquiries can have exactly one subject".**

---

# 45. Merchant scope

Merchant-owned operational relationships default to:

```text
SAME_MERCHANT
```

Therefore:

```text
Merchant A Enquiry
    → Merchant B Listing
```

is invalid unless a future explicit platform semantic authorises such a relationship.

For current Main Street semantics:

> **Cross-merchant operational references are prohibited.**

This preserves tenancy.

---

# 46. Target existence

A relationship can be committed only when its target can be resolved as a valid target under the applicable relationship definition.

Therefore:

```text
Enquiry.subject = L123
```

requires:

```text
L123 exists
L123 has permitted type
L123 belongs to permitted scope
```

at relationship establishment.

---

# 47. Target lifecycle does not automatically destroy the relationship

Suppose:

```text
Enquiry E1
    → Listing L123
```

and later:

```text
Listing L123
ACTIVE → WITHDRAWN
```

The Enquiry does not become invalid historically.

Its reference remains meaningful:

```text
E1 was about L123
```

Therefore:

> **Relationship lifecycle does not automatically follow target lifecycle.**

---

# 48. No implicit cascade deletion

Relationship semantics shall not imply:

```text
delete Listing
    ↓
delete Enquiries
```

or:

```text
delete CustomerContext
    ↓
delete Appointments
```

Business lifecycle and retention policies remain capability/governance concerns.

---

# 49. Historical targets

If a related object becomes inactive, archived or otherwise non-public:

```text
Enquiry → Listing
```

must remain capable of historical interpretation.

This supports MS-PROT-043's requirement that later changes not destroy relevant enquiry context.

---

# 50. Relationships should not carry rich business state

A useful boundary:

> **If the relationship itself requires identity, lifecycle, substantial data or independent operations, it should normally become an Operational Object rather than an increasingly complex relationship edge.**

For example:

```text
Subscription
Employment
Membership
Allocation
```

may deserve first-class object semantics where their relationship itself has meaningful state.

This prevents a generic graph engine from swallowing the domain.

---

# 51. Relationship versus Allocation

An Allocation may contain references to:

```text
Resource
use
```

but its authoritative conflict/capacity semantics are stronger than a generic relationship.

Therefore:

```text
Allocation
≠ generic edge
```

Allocation remains its own semantic construct.

---

# 52. Relationship versus DataConcept

A structured value such as:

```text
postcode
price
scheduled interval
```

is Data.

It is not a relationship merely because another system can query it.

Use relationship semantics where one addressable business object refers to another.

---

# 53. Relationship versus provenance

Provenance may use typed relationships where appropriate.

Example:

```text
Appointment A1
    ORIGINATED_FROM → Enquiry E1
```

provided the relationship is registered.

Again:

```text
ORIGINATED_FROM
```

does not make Enquiry part of the Appointment lifecycle.

---

# 54. Relationship versus capability dependency

Do not confuse:

```text
Appointment.customer → CustomerContext
```

with:

```text
Appointment capability REQUIRES Customer capability
```

These occur at different levels.

### Capability relationship

```text
CAPABILITY → CAPABILITY
```

describes semantic/configuration dependency.

### Operational relationship

```text
OBJECT INSTANCE → OBJECT INSTANCE
```

describes concrete business association.

Both are necessary.

---

# 55. Relationship definitions must not become merchant-defined graph schemas

Merchants may supply:

```text
which Listing
which CustomerContext
which Offering
```

as applicable business data.

They may not create arbitrary new semantic relationships such as:

```text
Appointment
    secretly_controls → Payment
```

Relationship definitions remain platform-owned registered semantics.

---

# 56. Typed relationship verdict

**ACCEPTED**

Canonical rule:

> **Main Street shall represent meaningful cross-object associations through platform-owned typed relationship definitions. The source semantic owner defines the relationship role, target type, cardinality and scope. A relationship permits reference but does not transfer mutation authority, lifecycle ownership or tenant scope.**

---

# PART IV — FALSIFICATION

# 57. Appointment

```text
Appointment A1
Customer C1
Staff S1
14:00–15:00
```

Classification:

```text
Appointment
    Operational Object

CustomerContext
    Operational Object

Staff
    Operational Object
    +
    Resource role

Appointment.customer
    typed REQUIRED_ONE relationship

Appointment.staff
    typed relationship where applicable

Appointment scheduling occupancy
    Allocation
```

Rescheduling:

```text
MUTATE_DATA scheduledInterval
RELEASE_ALLOCATION old interval
CLAIM_ALLOCATION new interval
```

No fake state transition required.

**PASS**

---

# 58. Appointment cancellation

```text
Appointment A1
CONFIRMED
```

Cancellation produces:

```text
TRANSITION_STATE
    CONFIRMED → CANCELLED

RELEASE_ALLOCATION
```

then, after successful commitment:

```text
AppointmentCancelled
```

Availability recalculates.

**PASS**

---

# 59. Automatic hotel Booking

```text
Booking B1
Customer C1
Executive Room
Friday → Sunday
```

Classification:

```text
Booking
    Operational Object

CustomerContext
    Operational Object

Executive Room Pool
    Resource role

Booking.customer
    typed relationship

reservation
    Allocation
```

Creation may produce:

```text
CREATE_OBJECT Booking
ESTABLISH_RELATIONSHIP customer
CLAIM_ALLOCATION
```

as one coherent authoritative outcome.

**PASS**

---

# 60. Booking cancellation

```text
TRANSITION_STATE Booking → CANCELLED
RELEASE_ALLOCATION
```

Booking is still not a Resource.

**PASS**

---

# 61. Property Listing

```text
Listing L123
24 High Street
£1,200 pcm
```

Classification:

```text
Listing
    Operational Object

address/price/etc.
    typed DataConcept values

Listing
    not automatically a Resource
```

Price update:

```text
MUTATE_DATA askingPrice
```

**PASS**

---

# 62. Property Enquiry

```text
Enquiry E1
    SUBJECT → Listing L123
```

Classification:

```text
Enquiry
    Operational Object

Listing
    Operational Object

SUBJECT
    typed relationship role
```

The customer does not manually enter:

```text
24 High Street
L123
```

because structured context established the relationship.

**PASS**

---

# 63. Listing changes after Enquiry

At submission:

```text
Listing price = £1,200
```

Later:

```text
Listing price = £1,300
```

Relationship remains:

```text
E1 → L123
```

while submission provenance can preserve materially relevant historical information.

No duplicated Listing authority is required in Enquiry.

**PASS**

---

# 64. CustomerContext

```text
CustomerContext C1
```

is:

```text
Operational Object
```

but:

```text
not Resource
```

Contact update uses:

```text
MUTATE_DATA
```

Appointment relationship uses:

```text
Appointment.customer → C1
```

**PASS**

---

# 65. Merchant personal unavailability

```text
MerchantScheduleIntent U1
14:00–15:00
"Dentist appointment"
```

is:

```text
Operational Object
```

but:

```text
not Appointment
not CustomerContext
not Resource
```

Its presence contributes to scheduling calculation.

**PASS**

---

# 66. Working hours

```text
09:00–17:00
```

remain:

```text
configuration
```

not Operational Object and not Resource.

They constrain Appointment availability.

**PASS**

---

# 67. Staff resource

```text
StaffMember S1
```

is simultaneously:

```text
Operational Object
+
Resource role
```

This proves why Resource cannot remain the universal object superclass.

**PASS**

---

# 68. Enquiry about existing Appointment

```text
Enquiry E2
    SUBJECT → Appointment A1
```

The Enquiry can ask:

> "Can I move this to Friday?"

but cannot mutate A1.

The appropriate Appointment operation must execute.

**PASS**

---

# 69. Cross-merchant reference

```text
Merchant A Enquiry
    → Merchant B Listing
```

Rejected by same-merchant scope constraint.

**PASS**

---

# 70. Relationship with business lifecycle

Suppose a future:

```text
Membership between Customer and Merchant
```

requires:

```text
identity
start date
state
renewal
cancellation
```

This should become:

```text
Membership Operational Object
```

rather than stuffing those behaviours into a generic edge.

**PASS**

---

# PART V — CONSEQUENCES FOR MS-PROT-043

MS-PROT-043 v1.1 can now be reviewed against a much cleaner semantic foundation.

Several terms need tightening.

## 71. `Enquiry`

Retain.

Classification:

```text
Operational Object
```

It owns:

```text
identity
merchant scope
customer-supplied enquiry data
lifecycle where justified
relationships
provenance
```

---

# 72. `EnquirySubject`

Do **not** introduce as a primitive or universal object.

Replace conceptually with:

```text
Enquiry
    SUBJECT → typed target
```

The target might be:

```text
Listing
Offering
Product
Opportunity
Booking
Appointment
```

only where the applicable relationship definition exists.

---

# 73. `EnquiryContext`

Do not treat it automatically as a persistent first-class object.

Most context is derived from:

```text
typed subject relationships
merchant scope
CustomerContext where known
interaction provenance
applicable requirements
```

Therefore:

> **EnquiryContext should primarily mean the resolved context used to construct and project an Enquiry, not a universal semantic object.**

If some context later requires durable independent identity/lifecycle, that specific concept must justify becoming an Operational Object.

---

# 74. Context-Carry-Forward

Retain as an invariant.

It is **not a primitive**.

It is a deterministic application/presentation rule:

```text
Known authoritative context
        ↓
resolve applicable requirements
        ↓
reuse already-known values
        ↓
ask only unresolved required information
```

---

# 75. `CustomerContext`

Retain.

Classification:

```text
merchant-scoped Operational Object
```

Not Resource.

Not CustomerAccount.

---

# 76. `CustomerAccount`

Retain outside the merchant operational-object hierarchy as an identity/access construct.

The relationship:

```text
CustomerAccount
    ↔
Merchant CustomerContext
```

is governed by identity/access semantics, not by arbitrary merchant relationship configuration.

---

# 77. `Listing`

MS-PROT-043 may continue to use Listing as the realtor example.

However, the exact semantic relationship between:

```text
Listing
Offering
Published content
```

still requires separate review.

That question no longer blocks the Enquiry relationship mechanism.

Whatever Listing eventually becomes, it can be a registered target type of the Enquiry `SUBJECT` relationship.

---

# 78. `Visitor`

Visitor should remain transient interaction/actor context.

It is not an Operational Object merely because someone loaded a page.

This preserves:

```text
browse
    ≠
Customer
    ≠
Enquiry
```

---

# 79. `Conversation`

If durable Main Street messaging is enabled, Conversation is appropriately:

```text
Operational Object
```

It may relate to:

```text
Enquiry
CustomerContext
Booking
Appointment
```

but it does not own those objects' state.

---

# 80. `Subscription`

Where durable subscription lifecycle exists, Subscription should likely be:

```text
Operational Object
```

rather than a Boolean on CustomerContext.

Its detailed semantics remain deferred.

---

# PART VI — REPOSITORY CONSEQUENCES

The current implementation should **not** be modified piecemeal until these accepted semantic changes are translated into one coherent migration.

The required direction is now clear.

## 81. Rename the broad resource object model

Current:

```text
OwnedResourceDefinition
```



Target semantic name:

```text
OwnedOperationalObjectDefinition
```

Likewise:

```text
ExecutableResourceDefinition
    →
ExecutableOperationalObjectDefinition
```

where those classes are representing lifecycle-bearing business objects rather than actual capacity Resources.

---

# 82. Rename creation effects

Current:

```text
OwnedResourceCreationEffect
```



should become semantically:

```text
OwnedObjectCreationEffect
```

with:

```text
targetObjectIdentifier
```

rather than:

```text
targetResourceIdentifier
```

---

# 83. State-transition effect target

Current:

```text
OwnedStateTransitionEffect.targetResourceIdentifier
```



should eventually become:

```text
targetObjectIdentifier
```

because a Booking or Enquiry state transition does not make it a Resource.

---

# 84. Add data mutation effect

The effect algebra requires a new concept equivalent to:

```text
OwnedDataMutationEffect
```

with strict registration against capability-owned typed data.

No free-form property mutation.

---

# 85. Add relationship definitions

The registry requires an owner-scoped concept equivalent to:

```text
OwnedRelationshipDefinition
```

describing:

```text
identifier
role
source object
target semantic type
cardinality
scope constraint
```

Exact Java shape should follow tests, not this document's illustrative structure.

---

# 86. Add relationship effects

Operations require bounded effects equivalent to:

```text
OwnedRelationshipEstablishmentEffect

OwnedRelationshipRemovalEffect
```

or a carefully typed unified relationship-mutation effect.

The naming should follow whichever formulation produces the smallest unambiguous implementation.

---

# 87. Integrate allocation carefully

Current `AllocationClaim` semantics remain useful. 

But allocation should ultimately reference typed Resource subjects and typed uses rather than rely indefinitely on unconstrained String identifiers.

That refinement can occur after the object/relationship migration.

---

# 88. Remove the superseded semantic path

The old `CapabilityCompiler` still compiles one capability into one `Resource` and one-transition Operations, and still supports `OperationComposition`. 

`OperationComposition` itself still models trigger→consequence. 

Tests still exist for that mechanism. 

Those artifacts now conflict with accepted canonical semantics.

They should eventually be:

```text
removed
```

rather than adapted into the new model.

We should preserve design/history in Git rather than keeping two executable semantic systems indefinitely.

---

# PART VII — GOVERNANCE

## PROPOSE

Three corrections were proposed:

```text
OperationalObject ≠ Resource

Expanded authoritative effect algebra

Platform-owned typed relationships
```

**PASS**

---

## REVIEW / FALSIFICATION

They were challenged against:

- automatic hotel Booking;
- Booking cancellation;
- customer-selected Appointment;
- Appointment rescheduling;
- Appointment cancellation;
- staff scheduling;
- resource pools;
- property Listings;
- Listing price changes;
- property Enquiries;
- Enquiry about existing Appointment;
- CustomerContext;
- merchant personal UNAVAILABLE intent;
- working hours/breaks;
- historical subject relationships;
- cross-merchant references;
- relationship lifecycle;
- cross-capability authority.

No counterexample requires collapsing Operational Object and Resource.

No tested business mutation requires an effect outside the proposed minimum algebra.

No tested relationship requires a generic untyped graph model.

**REVIEW: PASS**

---

## VALIDATE

The resulting model supports:

```text
publisher
realtor
consultant
solicitor
mechanic
salon
hotel/motel
retail
hybrid merchant
```

without category-specific runtime branching.

It remains compatible with the accepted Main Street authority hierarchy:

```text
Platform semantics
        ↓
Capability ownership
        ↓
Merchant configuration
        ↓
Resolution
        ↓
Generic runtime
```

**VALIDATE: PASS**

---

## ACCEPT

**ACCEPTED**

---

# Canonical semantic model after this review

```text
CAPABILITY
    │
    ├── owns Operational Object Definitions
    │       │
    │       ├── authoritative typed data
    │       ├── lifecycle where required
    │       └── typed relationships
    │
    ├── owns Operations
    │       │
    │       └── bounded authoritative effects
    │               ├── CREATE_OBJECT
    │               ├── TRANSITION_STATE
    │               ├── MUTATE_DATA
    │               ├── ESTABLISH_RELATIONSHIP
    │               ├── REMOVE_RELATIONSHIP
    │               ├── CLAIM_ALLOCATION
    │               └── RELEASE_ALLOCATION
    │
    ├── owns Requirements
    ├── owns Policies
    └── produces Domain Events
```

Separately:

```text
Operational Object
        │
        │ may play
        ▼
RESOURCE ROLE
        │
        ▼
CAPACITY
        │
        ▼
AVAILABILITY DECISION
        │
        ▼
ALLOCATION
```

And cross-object context becomes:

```text
Enquiry
    ──SUBJECT────────► Listing

Appointment
    ──CUSTOMER───────► CustomerContext

Booking
    ──CUSTOMER───────► CustomerContext

Appointment
    ──OFFERING───────► Offering

Appointment
    ──ORIGINATED_FROM► Enquiry
```

with every relationship typed, scoped and platform-defined.

## Result for MS-PROT-043

The semantic review has succeeded, but **MS-PROT-043 v1.1 is not yet accepted** because its text still needs to incorporate these decisions—particularly replacing `EnquirySubject` as a quasi-concept with a typed `SUBJECT` relationship and tightening `EnquiryContext`.

The next correct action is therefore to produce **MS-PROT-043 v1.2**, incorporating this accepted semantic amendment, and then falsify the complete Enquiry/Customer model against realtor, publisher, consultant, mechanic, guest customer, existing customer and cross-merchant scenarios.
