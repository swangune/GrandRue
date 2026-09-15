# MS-PROT-043 — Context-Aware Enquiry, Customer & Merchant Relationship Model

**Document ID:** MS-PROT-043  
**Version:** 1.2  
**Status:** **ACCEPTED after semantic revision, falsification and validation**  
**Supersedes:** MS-PROT-043 v1.1  
**Depends on:** MS-PROT-005, MS-PROT-007, MS-PROT-020 v1.4 semantic amendment, MS-PROT-021, MS-PROT-022, MS-PROT-023, MS-PROT-026, MS-PROT-027, MS-PROT-028, MS-PROT-029, MS-PROT-031, MS-PROT-036, MS-PROT-037, MS-PROT-038, MS-PROT-039, MS-PROT-041, MS-PROT-042 v1.1  
**Purpose:** Define how Main Street represents context-aware enquiries, merchant-scoped customer relationships, customer contact data, optional customer accounts, conversation relationships and repeated interactions while minimising customer effort, maximising merchant operational context, preserving capability ownership and preventing enquiry handling from becoming an arbitrary workflow or cross-merchant CRM.

---

# 1. Governing principle

> **Main Street shall minimise what customers must manually provide while preserving and presenting all relevant business context already known from the interaction.**

The target experience is:

```text
Business context already known
        +
Applicable Enquiry requirements
        +
Minimum unresolved customer input
        ↓
Complete actionable Enquiry
        ↓
Merchant receives sufficient context
to understand and act immediately
```

This is a direct consequence of Main Street's operational objective:

> **The platform should remove avoidable work for both merchant and customer rather than merely digitising existing manual steps.**

---

# 2. Semantic classification

The following classifications are normative.

```text
Visitor
    = transient interaction context

Enquiry
    = merchant-scoped Operational Object

CustomerContext
    = merchant-scoped Operational Object

Conversation
    = Operational Object where durable messaging applies

CustomerAccount
    = identity/access construct

SUBJECT
    = typed relationship role

EnquiryContext
    = resolved interaction/operational context,
      not automatically an Operational Object
```

The following are explicitly not introduced:

```text
EnquirySubject primitive
Global Customer primitive
Generic Enquiry workflow engine
Generic merchant form language
```

---

# 3. Context-Carry-Forward invariant

The following is a hard platform invariant:

> **Main Street shall not require a customer to restate authoritative contextual information that Main Street already possesses from the interaction.**

Example:

```text
Customer views Listing L123
        ↓
Customer clicks Enquire
        ↓
Main Street knows:
    merchant
    Listing L123
    listing type
    current subject context
```

The customer shall not then be asked:

```text
Which listing?
What is the address?
What is the postcode?
What property are you asking about?
```

unless the requested information has a materially different semantic meaning.

---

# 4. Minimal customer input versus complete merchant context

These are not contradictory goals.

Main Street should optimise simultaneously for:

```text
MINIMUM CUSTOMER INPUT
+
MAXIMUM RELEVANT MERCHANT CONTEXT
```

Therefore:

> **The customer supplies only information that is both materially required and not already reliably known.**

While:

> **The merchant receives sufficient context to identify the subject, understand the request and perform the relevant next action without reconstructing information manually.**

---

# 5. Visitor

A **Visitor** is a participant interacting with a merchant's public surface where no durable customer-specific operational object is yet required.

Examples:

```text
browse property listings
read scholarship information
view products
inspect services
view Listing photos
use map presentation
inspect Booking availability
inspect Appointment availability
read merchant announcements
```

A Visitor is not automatically persisted as:

```text
CustomerContext
Enquiry
CustomerAccount
```

Hard invariant:

> **Browsing alone shall not establish a durable merchant-customer relationship.**

---

# 6. Enquiry

An **Enquiry** is:

> **A merchant-scoped Operational Object representing a customer-side request for information, clarification, contact, consideration or further action which does not itself establish or modify another business commitment.**

An Enquiry may concern:

```text
the merchant generally
a specific business subject
an existing customer commitment
an offering
published information
```

Examples:

```text
"Are pets allowed at this property?"

"Can international students apply?"

"Do you repair electric vehicles?"

"Can someone call me about this service?"

"Can I add another guest to this Booking?"
```

---

# 7. Enquiry is an Operational Object

An Enquiry has independent business identity.

Conceptually:

```text
Enquiry
{
    enquiry_identity
    merchant_scope
    submitted customer data
    applicable typed relationships
    provenance
    lifecycle where justified
}
```

It is not merely:

```text
a message string
an email
a website form submission
```

because Main Street may need to:

```text
display it
assign it
respond to it
link it to a CustomerContext
retain provenance
relate it to a subject
close it
derive operational actions from it
```

---

# 8. Enquiry is not a commitment

An Enquiry does not itself create:

```text
Booking
Appointment
Order
Allocation
Payment
```

Example:

> "Can I view this property on Saturday?"

is an Enquiry until the appropriate Appointment semantics create a viewing Appointment.

Likewise:

> "Please reserve this room."

does not itself create a Booking.

---

# 9. SUBJECT is a typed relationship role

MS-PROT-043 shall not create an `EnquirySubject` primitive.

Instead:

```text
Enquiry
    ──SUBJECT──► target Operational Object
```

The target type is defined by a platform-owned registered RelationshipDefinition.

Examples:

```text
Enquiry
    ──SUBJECT──► Listing

Enquiry
    ──SUBJECT──► Product

Enquiry
    ──SUBJECT──► Opportunity

Enquiry
    ──SUBJECT──► Appointment

Enquiry
    ──SUBJECT──► Booking
```

---

# 10. SUBJECT relationship ownership

The Enquiry capability owns the meaning:

> This Enquiry concerns this subject.

The target capability continues to own the target object.

Therefore:

```text
Enquiry → Listing
```

does not give Enquiry capability authority to:

```text
change Listing price
withdraw Listing
modify Listing address
change Listing state
```

Hard invariant:

> **Reference authority is not mutation authority.**

---

# 11. Subject is optional

Not every Enquiry has a specific subject.

Example:

> "Do you have disabled parking?"

may legitimately be:

```text
Enquiry
    merchant = M100
    SUBJECT = none
```

Therefore Main Street shall not force every Enquiry into a subject-specific flow.

---

# 12. Known subject must be carried automatically

Where the interaction occurs from a known subject:

```text
Listing L123
        ↓
click Enquire
```

Main Street shall establish:

```text
Enquiry
    SUBJECT → L123
```

without asking the customer to identify L123 again.

This requirement applies equally to:

```text
Product
Opportunity
Offering
Booking
Appointment
other registered supported target
```

---

# 13. SUBJECT must be structurally established

Where Main Street already knows the current subject from UI/navigation context:

```text
SUBJECT
```

must be established structurally.

It shall not rely on AI or free-text inference.

Rejected:

```text
Customer:
"the flat on High Street"

AI:
maybe Listing L123
```

when the customer already initiated Enquiry directly from L123.

Correct:

```text
interaction context
    current subject = L123
        ↓
typed SUBJECT relationship
```

---

# 14. Structured context outranks textual inference

Where structured interaction context exists:

```text
structured subject identity
    >
customer prose inference
```

AI may interpret what the customer is asking.

AI shall not replace a known structured subject with another target inferred from ambiguous text.

---

# 15. Realtor reference scenario

Consider a realtor offering properties:

```text
FOR SALE
TO RENT
```

Listing L123 contains:

```text
transaction mode
property type
structured address
city
postcode
street
building number
price
bedrooms
bathrooms
description
photos
location
map presentation
listing status
```

Customer:

```text
opens L123
views photos
reads description
checks map/location
scrolls to Enquiry
```

Main Street already knows:

```text
Merchant
Listing L123
Listing metadata
Current presentation context
```

---

# 16. Realtor customer experience

The customer may see:

```text
Enquire about this property

24 High Street
Swansea
SA1 1AA

Name
[________________]

Email / phone
[________________]

Your question
[___________________________]

[Send enquiry]
```

The customer should not enter:

```text
property address
property reference
postcode
property type
rent/sale status
```

unless a genuinely different field is being requested.

---

# 17. Realtor merchant experience

The merchant-facing projection may show:

```text
PROPERTY ENQUIRY

Listing
24 High Street
Swansea
SA1 1AA

Reference
L123

Transaction
To Rent

Property type
2-bedroom flat

Price
£1,200 pcm

Customer
Sarah Jones

Contact
sarah@example.com

Question
"Is this available from September and are pets allowed?"
```

The merchant should not need to ask:

> "Which property?"

when Main Street already possessed that context.

---

# 18. Merchant-facing subject projection

The Enquiry does not own Listing metadata.

Instead:

```text
Enquiry E1
    SUBJECT → Listing L123
```

The Enquiry read model can project selected current Listing information.

Conceptually:

```text
Enquiry
    ↓
resolve SUBJECT
    ↓
retrieve permitted Listing projection
    ↓
display relevant merchant context
```

---

# 19. Subject data remains capability-owned

For the realtor case:

```text
Listing capability owns:
    address
    price
    photos
    description
    map location
    bedrooms
    bathrooms
    status
```

Enquiry owns:

```text
customer question
submitted contact data
SUBJECT relationship
provenance
Enquiry state
```

No authority duplication occurs.

---

# 20. EnquiryContext

`EnquiryContext` shall mean:

> **The resolved set of authoritative and interaction-derived information available to determine what the customer still needs to supply and what the merchant should see.**

It is not automatically a persisted Operational Object.

Conceptually:

```text
EnquiryContext =
    Merchant scope
    + SUBJECT relationship
    + subject projection
    + known CustomerContext where applicable
    + interaction provenance
    + applicable Enquiry requirements
```

---

# 21. EnquiryContext is derived

EnquiryContext may be reconstructed from authoritative sources.

Therefore:

```text
EnquiryContext
≠
second copy of Listing
≠
second copy of CustomerContext
≠
universal context database row
```

If a particular contextual fact requires historical preservation, that becomes provenance/evidence.

---

# 22. Submission-time provenance

A subject can change after Enquiry submission.

Example:

```text
At submission:
Listing L123 rent = £1,200

Later:
Listing L123 rent = £1,300
```

The Enquiry must retain enough provenance to interpret the submission accurately where the difference is materially significant.

Permitted implementation strategies may include:

```text
subject revision reference
selected snapshot values
projection revision
historical read model
event provenance
```

The exact implementation is deferred.

---

# 23. No indiscriminate subject snapshot

Main Street shall not copy an entire Listing, Product or Appointment into every Enquiry.

That would create duplicate authority.

Only context required for:

```text
historical interpretation
audit
legal evidence
```

should be preserved independently.

---

# 24. Enquiry requirements are contextual

Enquiry data requirements depend on the applicable business need.

Example:

### Property Enquiry

Known:

```text
subject Listing
property address
price
property type
merchant
```

Customer may need to provide only:

```text
name
reply contact
question
```

---

# 25. Construction enquiry

Customer views:

```text
Offering
"Home Extension"
```

Main Street knows the service.

But it may not know:

```text
project location
project scale
```

The applicable Enquiry requirements may therefore request:

```text
project postcode
brief project description
```

This does not violate Context-Carry-Forward because those facts are genuinely unresolved.

---

# 26. Scholarship enquiry

Customer views:

```text
Opportunity O100
```

Main Street knows:

```text
scholarship
provider
deadline
level
other stored metadata
```

Customer may only need:

```text
reply contact
question
```

They shall not re-enter the scholarship name.

---

# 27. Required information resolution

Before rendering an Enquiry surface, Main Street should conceptually evaluate:

```text
Applicable requirements
        ↓
Known interaction context
        ↓
Known subject data
        ↓
Known legitimate customer data
        ↓
Determine unresolved requirements
        ↓
Ask only unresolved information
```

Canonical rule:

```text
KNOWN
    → carry forward

REQUIRED + UNKNOWN
    → ask

OPTIONAL + useful + supported
    → may ask

IRRELEVANT
    → omit
```

---

# 28. Customer details can also carry forward

Where a reliable CustomerContext already exists:

```text
CustomerContext C1
    name
    email
    phone
```

an Enquiry form may reuse applicable information.

The customer should not repeatedly type:

```text
name
email
phone
```

without a meaningful reason.

---

# 29. Reuse does not imply immutable identity

Contact information such as:

```text
email
phone
address
```

remains mutable business/contact data.

It must not silently become the customer's identity key.

---

# 30. Contact information is not authenticated identity

A customer providing:

```text
sarah@example.com
```

means:

> this Enquiry supplied this contact value.

It does not automatically prove:

> the sender owns this email address.

Likewise:

```text
telephone
name
postcode
```

are not authentication.

---

# 31. CustomerContext

A **CustomerContext** is:

> **A stable merchant-scoped Operational Object representing a durable customer-specific business relationship with that merchant.**

Conceptually:

```text
Merchant M1
    ↓
CustomerContext C1
```

It is not:

```text
global Main Street person identity
CustomerAccount
Enquiry
```

---

# 32. Enquiry does not automatically create CustomerContext

An Enquiry can exist as:

```text
Enquiry E1
    merchant
    customer-supplied contact
    SUBJECT → Listing L123
```

without:

```text
CustomerContext
```

if no durable customer relationship is yet required.

This prevents one-off enquiries from polluting durable customer records.

---

# 33. CustomerContext becomes necessary when operations require it

At minimum:

```text
Booking
Appointment
```

require CustomerContext under MS-PROT-042.

Other capabilities may require CustomerContext where their operational semantics genuinely need a durable customer relationship.

---

# 34. Enquiry may later link to CustomerContext

Example:

```text
Property Enquiry E1
        ↓
Viewing arranged
        ↓
CustomerContext C1 created
        ↓
Appointment A1 created
```

Main Street may then establish:

```text
E1
    CUSTOMER → C1

A1
    CUSTOMER → C1
```

where the applicable typed relationships exist.

Enquiry E1 remains an Enquiry.

---

# 35. Enquiry conversion is not object transformation

Rejected:

```text
Enquiry E1
    becomes
Appointment A1
```

Correct:

```text
Enquiry E1
    ──ORIGINATES / precedes──► Appointment A1
```

or equivalent provenance relationship.

Each maintains independent identity.

---

# 36. CustomerContext is merchant-scoped

Same human:

```text
Merchant A
    CustomerContext CA

Merchant B
    CustomerContext CB
```

These are separate merchant relationships.

Main Street shall not expose:

```text
Merchant B history
```

to Merchant A.

---

# 37. CustomerAccount

A **CustomerAccount** is an identity/access construct.

It may authenticate a person who has relationships with multiple merchants.

Conceptually:

```text
CustomerAccount X
    │
    ├── linked to Merchant A CustomerContext
    └── linked to Merchant B CustomerContext
```

Those CustomerContexts remain separate.

---

# 38. CustomerAccount is optional

Guest interactions remain valid.

Example:

```text
Guest submits Enquiry
Guest creates Appointment
Guest creates Booking
```

where applicable.

Main Street shall not require registration solely because it is technically capable of supporting accounts.

---

# 39. CustomerAccount does not own merchant customer data

Merchant-owned relationship data remains:

```text
Merchant
    ↓
CustomerContext
```

not:

```text
CustomerAccount
    ↓
global merchant history
```

The account may authenticate access to merchant-scoped relationships.

It does not merge them.

---

# 40. Same email does not justify automatic merge

Suppose:

```text
Enquiry E1
email = john@example.com

CustomerContext C9
email = john@example.com
```

This alone does not establish sufficient identity evidence to merge automatically.

Possible reasons:

```text
mistyped email
shared inbox
recycled contact value
impersonation
household address
```

Hard invariant:

> **Mutable contact values shall not silently define customer identity.**

---

# 41. Reliable association

Automatic association may use stronger mechanisms such as:

```text
authenticated CustomerAccount
secure customer access token
existing authorised session
other registered trustworthy correlation
```

The exact correlation policy remains deferred.

---

# 42. Weak matches

Weak matches may produce merchant-facing assistance such as:

```text
Possible existing customer
```

where appropriate.

They must not silently alter ownership or merge history.

---

# 43. Conversation

Where Main Street messaging is durable, **Conversation** is an Operational Object.

It represents:

> A merchant-scoped communication context containing messages between permitted participants.

A Conversation may relate to:

```text
Enquiry
CustomerContext
Booking
Appointment
```

where registered.

---

# 44. Enquiry and Conversation are distinct

Example:

```text
Enquiry E1
    "Is this property still available?"
        ↓
Conversation V1
    merchant replies
    customer follows up
```

Enquiry represents the request.

Conversation represents continuing communication.

They may share lifecycle timing but remain distinct semantics.

---

# 45. Not every Enquiry requires Conversation

A simple enquiry may be handled through:

```text
Enquiry
    ↓
single merchant reply
    ↓
closed
```

without requiring a durable multi-message Conversation object.

Conversation should exist only where the communication model justifies it.

---

# 46. Messages do not mutate business commitments

Customer says:

> "Please cancel my Appointment."

The message is communication.

It does not directly establish:

```text
Appointment = CANCELLED
```

The appropriate authoritative operation must execute.

Correct:

```text
Message
    ↓
intent recognised
    ↓
CancelAppointment command
    ↓
authorisation
    ↓
validation
    ↓
Appointment cancellation
```

---

# 47. AI boundary

AI may:

```text
classify Enquiry intent
summarise messages
suggest merchant responses
suggest likely operational actions
extract non-authoritative information
```

AI may not autonomously:

```text
create Booking
create Appointment
cancel Appointment
modify Booking
merge CustomerContexts
change Listing
```

without an authorised operation boundary.

---

# 48. Enquiry-derived merchant actions

Merchant-facing Enquiry UI may surface contextually applicable actions.

Example realtor Enquiry:

```text
View Listing
Reply
Arrange Viewing
Close Enquiry
```

Consultant Enquiry:

```text
Reply
Arrange Appointment
View Offering
Close
```

These actions derive from:

```text
active capabilities
typed subject
merchant configuration
actor authority
current subject state
```

not business-category runtime branches.

---

# 49. Viewing a property

A property viewing is naturally represented as:

```text
Appointment
```

if it establishes an agreed service/interpersonal time.

Therefore:

```text
Property Enquiry
        ↓
Arrange Viewing
        ↓
Main Street calculates Appointment availability
        ↓
valid time selected
        ↓
Appointment created
```

The Listing remains the subject/property context.

---

# 50. Enquiry is not a workflow engine

Rejected:

```text
IF enquiry answer = X
THEN create Booking

IF enquiry answer = Y
THEN charge customer

IF enquiry contains "cancel"
THEN cancel Appointment
```

Enquiry may initiate or suggest independent operations.

It does not embed arbitrary executable workflow.

---

# 51. Structured questions

Main Street may support registered structured Enquiry questions.

Examples:

```text
Preferred contact method

Interested in:
    Viewing
    More information

Project postcode

Move-in timeframe
```

where appropriate.

The structure must remain:

```text
platform-supported
bounded
typed
configuration-controlled
```

not arbitrary executable form logic.

---

# 52. Free-text remains supported

Many enquiries require open-ended questions.

Therefore Main Street may combine:

```text
structured context
structured customer details
structured choices
free-text question
```

without forcing every request into preconfigured categories.

---

# 53. Business-specific Enquiry needs do not imply business-specific engine

Examples:

```text
Realtor
    Listing SUBJECT
    property metadata projection

Publisher
    Opportunity SUBJECT

Retailer
    Product SUBJECT

Consultant
    Offering SUBJECT
```

The generic mechanism remains:

```text
Enquiry
    +
typed relationships
    +
contextual requirements
    +
capability-owned projections
```

No:

```text
RealtorEnquiryEngine
ScholarshipEnquiryEngine
ProductEnquiryEngine
```

is required.

---

# 54. Offering and Listing remain distinct unresolved semantics

This document does not determine whether:

```text
Listing
```

is:

```text
a special Offering
a PublishedOffering
a separate semantic concept
```

That remains separate semantic design work.

MS-PROT-043 requires only that a valid target type may participate in the registered Enquiry `SUBJECT` relationship.

---

# 55. Location metadata

For property Listings, structured location may include:

```text
building number
street
city
postcode
coordinates
```

These are data concepts/values owned by the relevant Listing/property capability.

They are not Enquiry primitives.

---

# 56. Map integration

Map integration belongs to the Listing/presentation context.

Conceptually:

```text
Listing
    ↓
structured location
    ↓
map integration
```

The Enquiry accesses relevant Listing context through its typed SUBJECT relationship.

If map rendering fails, Enquiry remains valid provided actual Enquiry requirements can still be satisfied.

---

# 57. Customer history

Merchant-facing customer history may project:

```text
Enquiries
Appointments
Bookings
Orders
Conversations
```

where available.

But CustomerContext shall not duplicate authoritative copies of those objects.

Conceptually:

```text
CustomerContext
        │
        ├── Enquiry E1
        ├── Appointment A1
        ├── Booking B1
        └── Conversation V1
                ↓
          Customer history projection
```

Each owning capability remains authoritative.

---

# 58. Subscriber is not Customer

A person may subscribe to:

```text
property alerts
scholarship updates
merchant announcements
```

without having:

```text
Booking
Appointment
CustomerContext
```

solely because they subscribed.

Subscription semantics remain independent.

---

# 59. Enquiry does not imply Subscription

Providing:

```text
email
phone
```

for an Enquiry does not subscribe the person to marketing communications.

---

# 60. Operational communication versus marketing

The merchant may use supplied contact information to respond to the Enquiry according to applicable communication semantics.

That does not automatically create consent for:

```text
promotional email
marketing campaign
bulk SMS
newsletter
```

Hard invariant:

> **Operational contactability does not imply marketing consent.**

---

# 61. Tenant isolation

Every Enquiry belongs to a merchant scope.

Every merchant-owned SUBJECT relationship must satisfy the applicable scope rule.

Current default:

```text
SAME_MERCHANT
```

Example invalid relationship:

```text
Merchant A Enquiry
    SUBJECT → Merchant B Listing
```

unless a future explicit platform-defined cross-tenant semantic exists.

---

# 62. Cross-merchant customer visibility

Merchant A may access:

```text
Merchant A CustomerContext
Merchant A Enquiries
Merchant A Appointments
Merchant A Bookings
```

It may not query:

```text
all activity this person has across Main Street
```

and receive Merchant B's records.

---

# 63. Customer-side cross-merchant access

A future authenticated CustomerAccount may potentially provide:

```text
My Appointments
My Bookings
```

across merchants.

That is customer-side access.

It does not make merchants share customer information with one another.

---

# 64. Enquiry lifecycle

A minimal lifecycle may need concepts such as:

```text
OPEN
CLOSED
```

Potentially:

```text
RESPONDED
```

if it represents a material business distinction.

However the state machine should remain minimal.

Main Street shall not impose:

```text
LEAD
QUALIFIED
HOT
CONVERTED
LOST
```

as universal Enquiry states.

---

# 65. Response is not necessarily lifecycle state

A merchant sending one message may not require a state transition if:

```text
message history
```

already supplies the necessary fact.

State exists only when operational behaviour genuinely depends on the distinction.

This will be finalised in the Conversation/Enquiry operation specification.

---

# 66. Closing an Enquiry

Closing an Enquiry means:

> the merchant no longer treats that Enquiry as requiring active handling.

It does not:

```text
delete the subject
delete CustomerContext
cancel Appointment
cancel Booking
terminate Conversation automatically
```

unless separately defined.

---

# 67. Enquiry assignment

Some merchants may need:

```text
assigned staff
responsible team
```

for Enquiry management.

This is not universally required.

Assignment should be introduced only if merchant operational evidence justifies it.

---

# 68. Subject lifecycle

If:

```text
Listing L123
ACTIVE → WITHDRAWN
```

existing:

```text
Enquiry E1 SUBJECT → L123
```

remains valid historical context.

Target lifecycle change shall not automatically erase the relationship.

---

# 69. New Enquiry availability may depend on subject state

If a Listing becomes:

```text
WITHDRAWN
```

the storefront may stop exposing:

```text
Enquire
```

where configured.

Existing Enquiries remain.

Thus:

```text
new interaction availability
≠
historical relationship validity
```

---

# 70. Direct navigation

Merchant Enquiry UI should allow direct navigation to related subject where useful.

Example:

```text
Enquiry E1
    ↓
View Listing
```

or:

```text
Enquiry E2
    ↓
View Appointment
```

This is a read-model/navigation consequence of the typed relationship.

---

# 71. No manual reference reconstruction

Rejected merchant experience:

```text
Customer:
"Is this still available?"

Merchant:
"Which property?"
```

when Main Street knew:

```text
SUBJECT → Listing L123
```

at submission.

Discarding known context is a platform failure.

---

# 72. Enquiry completeness

A submitted Enquiry is valid when:

```text
merchant scope is valid
subject relationship is valid where required/present
required unresolved customer data is supplied
customer data is structurally valid
applicable authority/input rules are satisfied
```

It does not require:

```text
CustomerAccount
CustomerContext
Booking
Appointment
```

unless independently required.

---

# 73. Enquiry creation effects

Conceptually, `SubmitEnquiry` may produce:

```text
CREATE_OBJECT Enquiry
+
ESTABLISH_RELATIONSHIP SUBJECT
```

where subject exists.

It may also establish:

```text
CUSTOMER relationship
```

if a reliable CustomerContext is already known.

These effects form one coherent Enquiry creation outcome where applicable.

---

# 74. Subject relationship failure

If:

```text
customer initiates from Listing L123
```

but L123 no longer resolves as a valid target before submission, Main Street must not silently create:

```text
subjectless Enquiry
```

unless the interaction is deliberately changed to a valid general-Enquiry flow.

Known structured context shall not disappear accidentally.

---

# 75. Context carry-forward and security

Main Street shall carry forward information only when the requesting participant is entitled to use that context.

For example:

```text
public Listing metadata
```

may be included freely in its public Enquiry flow.

But:

```text
Booking B500
```

requires appropriate customer authority before an Enquiry concerning that Booking can expose sensitive Booking context.

Context carry-forward does not override access control.

---

# 76. Existing Appointment Enquiry

Customer accesses:

```text
Appointment A100
```

through an authorised mechanism.

Customer chooses:

```text
Ask a question
```

Main Street establishes:

```text
Enquiry E100
    SUBJECT → Appointment A100
```

The customer does not re-enter:

```text
appointment reference
appointment time
service
merchant
```

where already known and legitimately reusable.

---

# 77. Existing Booking Enquiry

Likewise:

```text
Enquiry E200
    SUBJECT → Booking B200
```

may concern:

> "Can another guest be added?"

The Enquiry does not modify B200.

Any Booking change uses the Booking capability.

---

# 78. Repeated guest Enquiry

A guest sends two Enquiries using the same unverified email.

Main Street must not automatically conclude:

```text
same CustomerContext
```

solely from email equality.

The Enquiries may remain separate until stronger correlation exists.

---

# 79. Merchant-created customer relationship

A merchant may establish CustomerContext for legitimate operations such as:

```text
telephone Booking
telephone Appointment
ongoing service relationship
```

This does not require prior Enquiry.

Enquiry is not the mandatory entrance to CustomerContext.

---

# 80. No universal sales funnel

Main Street shall not force:

```text
Visitor
    ↓
Lead
    ↓
Prospect
    ↓
Customer
    ↓
Converted
```

Different businesses operate differently.

CustomerContext exists because a durable operational relationship requires it, not because Main Street is implementing a universal CRM funnel.

---

# 81. Falsification — realtor Listing Enquiry

Customer visits Listing L123 and clicks Enquire.

Expected:

```text
Enquiry created
SUBJECT → L123
property details not re-requested
customer supplies unresolved contact/question data
merchant sees relevant Listing context
```

**PASS**

---

# 82. Falsification — general realtor Enquiry

Customer asks:

> "Do you manage properties in Cardiff?"

No specific Listing.

Expected:

```text
Enquiry
SUBJECT = none
```

**PASS**

The model does not manufacture a Listing.

---

# 83. Falsification — property changes after Enquiry

Price changes after submission.

Expected:

```text
SUBJECT still references same Listing
current Listing remains authoritative
material submission-time context remains reconstructible
```

**PASS**

---

# 84. Falsification — withdrawn property

Listing withdrawn after Enquiry.

Expected:

```text
existing Enquiry retained
historical SUBJECT retained
new Enquiry exposure may stop
```

**PASS**

---

# 85. Falsification — scholarship opportunity

Customer views Opportunity O1.

Asks:

> "Can international MSc students apply?"

Expected:

```text
SUBJECT → O1
customer does not type scholarship title
publisher receives opportunity context
```

**PASS**

---

# 86. Falsification — generic construction service

Customer views Home Extension Offering.

Main Street knows Offering but not project location.

Expected:

```text
Offering context carried
project postcode requested
```

because it is unresolved and materially required.

**PASS**

---

# 87. Falsification — self-scheduling consultant

Customer sees available Appointment times and creates an Appointment directly.

No Enquiry is required.

**PASS**

Enquiry does not become a mandatory precursor to Appointment.

---

# 88. Falsification — merchant-confirmed consultant

Customer first asks for consultation.

Enquiry may lead to:

```text
SchedulingRequest
CustomerContext
Appointment
```

where applicable.

Each remains independent.

**PASS**

---

# 89. Falsification — mechanic service Enquiry

Customer views:

```text
Timing Belt Replacement
```

and asks:

> "Do you service my vehicle model?"

Main Street carries Offering context.

If vehicle registration/model is required and unknown, Main Street requests it.

**PASS**

---

# 90. Falsification — guest Appointment

Customer has no account.

Main Street creates:

```text
CustomerContext
Appointment
```

No Enquiry required.

**PASS**

---

# 91. Falsification — existing Appointment question

Authorised customer asks about Appointment A10.

Expected:

```text
Enquiry SUBJECT → A10
```

Message itself does not reschedule/cancel.

**PASS**

---

# 92. Falsification — customer asks cancellation in Conversation

Customer writes:

> "Cancel it."

Expected:

```text
communication records intent
Appointment remains unchanged
until CancelAppointment executes
```

**PASS**

---

# 93. Falsification — same email, different people

Two Enquiries share email.

No strong correlation.

Expected:

```text
no silent CustomerContext merge
```

**PASS**

---

# 94. Falsification — same authenticated customer

Authenticated customer already linked to Merchant A CustomerContext C1.

New Enquiry may safely relate to C1 under applicable authority.

**PASS**

---

# 95. Falsification — same customer across two merchants

CustomerAccount relates to:

```text
Merchant A C1
Merchant B C7
```

Expected:

```text
separate CustomerContexts
separate Enquiries
merchant isolation preserved
```

**PASS**

---

# 96. Falsification — newsletter subscriber

Person subscribes to property alerts.

No durable customer transaction exists.

Expected:

```text
Subscription
not automatically CustomerContext
not Enquiry
```

**PASS**

---

# 97. Falsification — marketing consent

Customer provides email for property Enquiry.

Expected:

```text
merchant may respond operationally
marketing subscription not implied
```

**PASS**

---

# 98. Falsification — arbitrary form workflow

Merchant attempts:

```text
if customer selects YES
    automatically charge deposit

if customer selects NO
    create Appointment
```

within Enquiry configuration.

**FAIL BY DESIGN**

Such behaviour is outside permitted Enquiry configuration.

Correctly rejected.

---

# 99. Falsification — cross-merchant subject

Merchant A Enquiry references Merchant B Listing.

**FAIL BY DESIGN**

Rejected by tenant scope.

---

# 100. Falsification — AI changes structured subject

Enquiry launched from L123.

AI infers customer actually meant L124 based on prose.

**FAIL BY DESIGN**

Structured subject remains authoritative unless participant explicitly changes context through a supported operation.

---

# 101. Falsification — map outage

Map provider unavailable.

Listing information and subject reference remain available.

Enquiry can still proceed if actual requirements are satisfied.

**PASS**

Map integration does not own Enquiry semantics.

---

# 102. Falsification — information-only merchant

Scholarship publisher uses:

```text
Publication
Opportunity
Enquiry
Notification
```

with no Booking, Appointment or CustomerContext requirement for ordinary one-off Enquiry.

**PASS**

---

# 103. Falsification — hybrid merchant

Hotel offers:

```text
Booking
Spa Appointment
General Enquiry
Room-specific Enquiry
```

Customer can:

```text
Enquire about room
Book room
Schedule spa Appointment
```

without collapsing the three interactions.

**PASS**

---

# 104. Rejected models

The following are rejected:

1. Universal `name + email + message` Enquiry schema.
2. Every Enquiry creates CustomerContext.
3. Every Enquiry requires CustomerAccount.
4. `EnquirySubject` as universal primitive.
5. `EnquiryContext` as mandatory persisted object.
6. Customer manually re-entering known subject information.
7. Subject identity inferred from text where interaction already established it.
8. Enquiry owning duplicated subject data.
9. Merchant reconstructing known subject context manually.
10. Arbitrary merchant-defined Enquiry workflow.
11. Enquiry message directly mutating Booking or Appointment.
12. Contact value functioning as customer identity.
13. Same email automatically merging CustomerContexts.
14. Cross-merchant CustomerContext sharing.
15. Merchant-visible global customer profile.
16. Subscription automatically establishing CustomerContext.
17. Enquiry contact implying marketing consent.
18. Generic CRM lead funnel as Main Street customer semantics.
19. Property-specific Enquiry runtime.
20. Map integration becoming semantic authority for property Enquiry.

---

# 105. Accepted invariants

1. Enquiry is a merchant-scoped Operational Object.
2. Enquiry is not a Booking, Appointment, Order or other commitment.
3. Visitor is transient interaction context.
4. Browsing does not create CustomerContext.
5. SUBJECT is a typed relationship role, not a primitive.
6. Enquiry may have no SUBJECT.
7. Known subjects are carried automatically.
8. Subject identity is structural where known.
9. Structured context outranks textual inference.
10. Subject capability remains authoritative for subject data.
11. Enquiry does not duplicate target authority.
12. EnquiryContext is primarily resolved context, not automatically an Operational Object.
13. Customer input shall be minimised.
14. Relevant merchant context shall be maximised.
15. Known contextual information shall not be manually requested again without semantic reason.
16. Enquiry requirements are contextual.
17. Required-but-unknown information is requested.
18. Irrelevant information is omitted.
19. Existing customer information may be reused where legitimate.
20. Contact information is not authenticated identity.
21. Enquiry does not automatically create CustomerContext.
22. CustomerContext is merchant-scoped.
23. Booking and Appointment require CustomerContext.
24. CustomerAccount remains optional.
25. CustomerAccount is distinct from CustomerContext.
26. Guest operations remain supported.
27. Weak contact matching shall not silently merge customers.
28. Cross-merchant customer relationships remain isolated.
29. Conversation is distinct from Enquiry.
30. Messages do not mutate business commitments.
31. AI cannot bypass authoritative operations.
32. Enquiry may result in independent operations.
33. Resulting operations retain independent identities and lifecycles.
34. Provenance may link resulting operations to Enquiry.
35. Enquiry UI may expose contextual next actions.
36. Contextual actions derive from active semantics and authority, not merchant category.
37. Structured Enquiry questions must remain bounded.
38. Free-text Enquiry remains supported.
39. Enquiry shall not become arbitrary workflow scripting.
40. Historical subject relationships survive subject lifecycle changes.
41. Subject deletion/inactivation does not automatically delete Enquiry.
42. Relevant submission-time context must remain reconstructible where materially required.
43. Whole-subject duplication is not required.
44. Customer-side known context may be reused only where authorised.
45. Existing Booking/Appointment context requires customer authority before exposure.
46. Subscription remains separate from CustomerContext.
47. Enquiry contactability does not imply marketing consent.
48. Main Street does not impose a universal sales funnel.
49. Realtor, publisher, consultant, mechanic, hotel and hybrid merchants use the same Enquiry mechanism.
50. Business-specific context belongs to capability-owned subject data, not niche-specific Enquiry engines.

---

# 106. Deferred decisions

The following remain open:

```text
exact Enquiry lifecycle states

minimum Conversation lifecycle

staff/team Enquiry assignment

Enquiry SLA/reminder semantics

spam and abuse protection

public anonymous rate limits

customer matching confidence policy

customer merge operation

customer split operation

organisation/business customers

multi-contact customers

exact CustomerAccount technology

guest access tokens

contact verification

Enquiry read-model design

historical subject snapshot mechanism

relationship cardinality for all SUBJECT targets

whether multi-subject Enquiries are required

Listing semantic model

Offering vs Listing relationship

capability-owned metadata schemas

structured Address DataConcept

map/location integration

property-specific metadata catalogue

customer data retention

privacy deletion/anonymisation

marketing consent implementation
```

---

# 107. Governance review

## PROPOSE

MS-PROT-043 was revised to incorporate the accepted semantic model:

```text
Operational Object
typed relationships
context carry-forward
resolved EnquiryContext
merchant-scoped CustomerContext
optional CustomerAccount
```

**PASS**

---

## REVIEW / FALSIFICATION

The model was challenged against:

```text
realtor specific Listing Enquiry
general realtor Enquiry
mutable Listing metadata
withdrawn Listing
scholarship Opportunity
construction service
self-scheduling consultant
merchant-controlled consultant
mechanic
guest Appointment
existing Appointment enquiry
cancellation request in messaging
same email duplicate
authenticated returning customer
cross-merchant customer
subscription
marketing consent
hybrid hotel
map outage
arbitrary workflow attempt
AI subject misclassification
cross-merchant subject reference
```

No valid case requires a global Enquiry subject primitive, universal Customer identity, merchant-specific Enquiry engine or arbitrary workflow mechanism.

**PASS**

---

## VALIDATE

The revised model supports materially different businesses through:

```text
typed subject relationships
contextual requirements
capability-owned data
merchant-scoped customer relationships
generic Enquiry operations
```

without business-category runtime branching.

It is consistent with the accepted Main Street construction hierarchy and the new OperationalObject/Resource boundary.

**PASS**

---

## ACCEPT

**MS-PROT-043 v1.2 is ACCEPTED.**

---

# 108. Canonical decision

> **Main Street Enquiry is a merchant-scoped Operational Object representing a context-aware request for information, clarification, contact or further action. An Enquiry does not itself establish or modify a Booking, Appointment or other commitment. When the customer initiates an Enquiry from a known business object, Main Street establishes a platform-defined typed `SUBJECT` relationship to that object and automatically carries forward relevant authoritative context. `EnquirySubject` is therefore not a primitive, and `EnquiryContext` is primarily resolved context derived from merchant scope, typed relationships, subject projections, known customer information, provenance and applicable requirements. The customer supplies only materially required information that Main Street cannot already determine, while the merchant receives enough context to understand and act on the request without reconstructing known information manually. CustomerContext remains a separate merchant-scoped Operational Object created only when a durable customer-specific relationship requires it; CustomerAccount remains an optional identity/access construct. Contact information is not customer identity, weak matching cannot silently merge relationships, Conversation remains distinct from Enquiry, and communication cannot directly mutate authoritative business state. This enables rich use cases such as property-specific, product-specific, opportunity-specific and existing-commitment enquiries without niche-specific Enquiry engines or a generic workflow language.**

The next semantic boundary should now be **Listing / Offering / Published Subject semantics**, because the realtor case has exposed a real unresolved distinction: whether a property Listing is merely an Offering, a published representation of something else, or a separate reusable semantic construct. That decision will also affect products, scholarship opportunities, services and other subject-aware storefront content.
