# MS-PROT-043 — Context-Aware Enquiry, Customer & Merchant Relationship Model

**Document ID:** MS-PROT-043  
**Version:** 1.1  
**Status:** **PROPOSED — Pending Semantic Review**  
**Supersedes:** MS-PROT-043 v1.0  
**Depends on:** MS-PROT-005, MS-PROT-007, MS-PROT-020, MS-PROT-021, MS-PROT-022, MS-PROT-023, MS-PROT-026, MS-PROT-027, MS-PROT-028, MS-PROT-029, MS-PROT-031, MS-PROT-036, MS-PROT-037, MS-PROT-038, MS-PROT-039, MS-PROT-041, MS-PROT-042  
**Purpose:** Define how Main Street represents enquiries, their business context, customer-supplied information, merchant-facing enquiry information, merchant-scoped customer relationships, optional customer accounts, conversations, and repeated interactions while minimising customer effort and maximising actionable merchant context.

---

# 1. Governing principle

> **Main Street shall minimise what customers must provide while automatically carrying forward all relevant information already known from the business context in which an interaction occurs.**

An Enquiry is therefore not merely:

```text
name
email
message
```

An Enquiry is a **context-aware business interaction**.

Canonical form:

```text
Known business context
        +
Applicable enquiry requirements
        +
Minimum required customer input
        ↓
Complete merchant-facing enquiry
```

This supports Main Street's broader objective:

> **Streamline business operation and management for both merchants and customers by removing unnecessary repetition, manual interpretation and context reconstruction.**

---

# 2. Context-Carry-Forward invariant

The following is a hard invariant:

> **Main Street shall not require a customer to manually restate contextual information that Main Street already knows authoritatively from the interaction.**

If a customer initiates an Enquiry from:

```text
Property Listing L123
```

the customer shall not be asked:

```text
Which property?
Property address?
Postcode?
Listing reference?
```

Main Street already knows the subject.

Instead:

```text
Customer views Listing L123
        ↓
Customer clicks Enquire
        ↓
Enquiry.subject = L123
```

The relationship is automatic.

---

# 3. Minimal customer input does not mean minimal merchant information

This distinction is fundamental.

Main Street optimises for:

```text
MINIMUM CUSTOMER REPETITION
        +
MAXIMUM RELEVANT MERCHANT CONTEXT
```

Therefore:

> **Customer input should contain only information Main Street does not already know and which the merchant genuinely requires for the applicable Enquiry.**

At the same time:

> **The merchant-facing Enquiry must provide the full relevant operational context necessary to understand and act on the request without reconstructing that context manually.**

---

# 4. Scope

MS-PROT-043 governs:

```text
Visitor
Enquiry
EnquirySubject
EnquiryContext
Enquiry requirements
Customer-supplied enquiry data
Subject-derived context
Merchant-facing enquiry projection
CustomerContext
CustomerAccount
Contact information
Conversation relationship
Repeated interaction association
Customer relationship establishment
Subscription distinction
Operational provenance
Cross-merchant isolation
```

It does not define:

```text
specific realtor domain schema
specific listing implementation
map provider
Google Maps API
customer authentication technology
messaging transport
CRM analytics
privacy retention periods
AI provider
database schema
frontend component implementation
```

---

# 5. Core semantic concepts

The following shall remain distinct:

```text
Visitor

Enquiry

EnquirySubject

EnquiryContext

CustomerContext

CustomerAccount

Conversation

Subscription

Booking

Appointment
```

None exists merely because another exists.

---

# 6. Visitor

A **Visitor** is a person interacting with a merchant's public surface where no durable customer-specific relationship is yet required.

Examples:

```text
browse properties
view products
read opportunity listings
inspect a service
view photos
view map
read merchant information
inspect Booking availability
inspect Appointment availability
```

Browsing shall not create a CustomerContext.

---

# 7. Enquiry definition

An **Enquiry** is:

> **A merchant-scoped, context-aware request for information, clarification, contact, consideration or further action that does not itself establish or modify a business commitment.**

An Enquiry may concern:

```text
a specific subject
a merchant generally
an existing customer operation
```

Examples:

```text
"Are pets allowed at this property?"

"Can international students apply?"

"Do you repair electric vehicles?"

"Can you call me about this service?"

"Can I add another guest to this Booking?"
```

---

# 8. Enquiry is first-class

Enquiry shall not be represented as a fake:

```text
Appointment
Booking
Order
Customer
```

merely because it may eventually lead to one.

Valid:

```text
Enquiry
    ↓
ends
```

Valid:

```text
Enquiry
    ↓
Conversation
    ↓
Appointment
```

Valid:

```text
Enquiry
    ↓
Booking
```

The resulting operation remains semantically independent.

---

# 9. EnquirySubject

An Enquiry may reference the business subject about which the customer is enquiring.

Conceptually:

```text
Enquiry
    └── subject → EnquirySubject
```

`EnquirySubject` is a semantic relationship, not necessarily a universal concrete Java class.

Possible subjects include:

```text
Listing
Offering
Product
Publication
Opportunity
Booking
Appointment
Order
other registered business object
```

The relevant capability owns the subject.

Enquiry owns only the relationship to it.

---

# 10. Subject is optional

Not every Enquiry has a specific subject.

Example:

> "Do you have wheelchair access?"

may concern the merchant generally.

Therefore:

```text
Enquiry.subject = OPTIONAL
```

But:

> **Where an Enquiry is initiated from a known subject, Main Street shall preserve that subject automatically.**

---

# 11. Subject identity is not customer-entered text

Incorrect:

```text
Property:
"24 High Street"
```

entered manually by the customer.

Correct:

```text
subject_reference = Listing L123
```

This provides:

```text
stable relationship
correct merchant scope
structured retrieval
reliable merchant context
```

and removes customer transcription errors.

---

# 12. Realtor reference scenario

A realtor offers:

```text
SALE
RENT
```

properties.

A Listing may expose structured information such as:

```text
Listing L123

Transaction:
RENT

Property type:
Flat

Address:
24 High Street
Swansea
SA1 1AA

Bedrooms:
2

Bathrooms:
1

Price:
£1,200 pcm

Description:
...

Photos:
...

Location:
coordinates / map reference
```

The customer opens L123.

They inspect:

```text
description
photos
price
location
map
property characteristics
```

Then they want more information.

---

# 13. Realtor Enquiry initiation

The listing may provide:

```text
Enquire about this property
```

or an Enquiry surface positioned directly beneath the listing.

When initiated:

```text
Current page context
    = Listing L123

Customer clicks Enquire
        ↓
Main Street automatically establishes:

Enquiry.subject = L123
```

No separate property-identification step occurs.

---

# 14. Realtor customer experience

The customer might see:

```text
Enquire about this property

24 High Street
Swansea
SA1 1AA

Name
[________________]

Email / telephone
[________________]

Your question
[____________________________]
[____________________________]

[Send enquiry]
```

They shall not ordinarily be asked:

```text
Property address
Street
City
Postcode
Listing number
Property type
Rent or sale
```

because Main Street already knows those facts.

---

# 15. Realtor merchant experience

The merchant receives an actionable enquiry projection.

For example:

```text
PROPERTY ENQUIRY

Subject
24 High Street
Swansea
SA1 1AA

Listing
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

> "Which property are you referring to?"

Main Street has already preserved the relationship.

---

# 16. Merchant-facing subject navigation

Where appropriate, the merchant enquiry surface should permit direct navigation to the related subject.

Example:

```text
Enquiry E500
    ↓
View Listing L123
```

The merchant can inspect the authoritative current listing without manually searching.

This is a projection/navigation concern, not duplicated Enquiry ownership of the Listing.

---

# 17. Listing metadata belongs to Listing

Property-specific information such as:

```text
address
coordinates
price
bedrooms
bathrooms
photos
description
property type
transaction mode
```

belongs to the Listing-owning capability.

It shall not become authoritative Enquiry data merely because it is displayed with an Enquiry.

Canonical relationship:

```text
Listing L123
    owns property information

Enquiry E500
    references L123
```

---

# 18. Map belongs to subject presentation

An integrated map associated with a property Listing belongs to the Listing/presentation capability.

Conceptually:

```text
Listing
    ├── structured address
    ├── location
    ├── photos
    ├── description
    └── map presentation
```

The Enquiry does not separately own:

```text
map
coordinates
property address
```

It inherits access to the relevant subject context through its relationship.

---

# 19. No `RealtorEnquiry` core primitive

The realtor example does not justify:

```text
RealtorEnquiry
PropertyEnquiryEngine
RealtorRuntime
```

in the Main Street semantic core.

The generic pattern is:

```text
Business Subject
        ↓
Context-aware Enquiry
```

Real estate validates that pattern.

It does not become the architecture itself.

---

# 20. Business-need-driven enquiry configuration

Enquiry structure shall be determined by applicable business needs.

Main Street must support different Enquiry requirements for different subjects or operational contexts.

For example:

### Property Listing

```text
name
reply contact
question
```

because property context is already known.

### Construction service

Main Street may additionally need:

```text
project postcode
project description
```

because the enquiry subject alone may not establish the job location.

### Scholarship opportunity

It may need:

```text
reply email
question
```

without requiring address or telephone.

---

# 21. EnquiryConfiguration

Conceptually, active merchant configuration may determine an applicable Enquiry configuration.

It may describe:

```text
which subjects support Enquiry
which customer fields are required
which customer fields are optional
which contextual relationships are attached
which permitted structured questions apply
```

This is controlled Main Street configuration.

It shall not become an arbitrary merchant-defined workflow language.

---

# 22. Enquiry requirements are contextual

A field shall be requested only when it is materially required in that context.

Therefore:

```text
required field for one Enquiry
    ≠
universal Customer field
```

Example:

```text
Project postcode required
for a construction quote enquiry
```

does not imply:

```text
postcode required
for every Main Street customer
```

This remains consistent with MS-PROT-005.

---

# 23. Required-data derivation

For each Enquiry, Main Street should conceptually evaluate:

```text
Applicable Enquiry requirements
        ↓
What context is already known?
        ↓
What customer information is already available
and legitimately reusable?
        ↓
What required information remains unresolved?
        ↓
Ask only for unresolved required information
```

Canonical principle:

```text
KNOWN
    → carry forward

REQUIRED + UNKNOWN
    → ask

OPTIONAL + USEFUL
    → may ask where configured

IRRELEVANT
    → do not ask
```

---

# 24. Context must not be requested twice

Suppose Main Street already knows:

```text
subject = Property L123
location = Swansea
```

The Enquiry form shall not ask:

```text
Which property?
Which city?
```

unless the actual Enquiry requires some *different* location context.

For example:

```text
"Where do you currently live?"
```

is semantically different from:

```text
"Where is the property?"
```

The system must distinguish them rather than suppress questions based on superficial field-name similarity.

---

# 25. Customer details may also be carried forward

Where Main Street reliably knows the customer's merchant-scoped context and applicable information may legitimately be reused:

```text
CustomerContext
    ↓
known contact information
```

Main Street may prepopulate appropriate Enquiry data.

The customer should not repeatedly enter the same information without reason.

However:

> **Prepopulation does not remove the requirement to distinguish authoritative identity from mutable contact information.**

---

# 26. Contact information is not identity

A submitted:

```text
email
telephone
name
```

remains contact/business data.

It is not automatically authenticated identity.

This distinction remains unchanged from MS-PROT-043 v1.0.

---

# 27. Enquiry does not automatically create CustomerContext

Even a richly contextual Enquiry such as:

```text
Enquiry E500
    subject = Property L123
    sender = Sarah
    email = ...
```

does not necessarily require a durable CustomerContext.

Therefore:

```text
Enquiry
    ↛ automatically CustomerContext
```

An Enquiry may remain an independent interaction.

---

# 28. When CustomerContext becomes necessary

A CustomerContext shall be created or linked when a durable customer-specific operational relationship requires it.

At minimum:

```text
Booking
Appointment
```

require it under MS-PROT-042.

Other operations may require it according to their registered semantics.

---

# 29. Enquiry conversion does not mutate Enquiry into Customer

Suppose:

```text
Property Enquiry E500
        ↓
merchant arranges viewing
        ↓
Appointment A600
```

Main Street may establish:

```text
CustomerContext C700
```

and associate:

```text
E500 → C700
A600 → C700
```

But:

```text
Enquiry E500
```

remains an Enquiry.

It does not become the Appointment or CustomerContext.

---

# 30. CustomerContext definition

A **CustomerContext** is:

> **The stable merchant-scoped operational representation of a person participating in durable customer-specific business activity with that merchant.**

Conceptually:

```text
Merchant M1
    ↓
CustomerContext C1
```

It is not a universal platform customer profile.

---

# 31. CustomerContext remains merchant-scoped

Same person:

```text
Merchant A → CustomerContext CA

Merchant B → CustomerContext CB
```

Those remain separate relationships.

Main Street shall not expose Merchant B's customer relationship to Merchant A.

---

# 32. CustomerAccount remains optional

A **CustomerAccount** is an authentication/access construct.

It is not the CustomerContext.

Therefore:

```text
CustomerContext
    can exist without CustomerAccount
```

and:

```text
CustomerAccount
    may link to multiple merchant-scoped CustomerContexts
```

without merging those merchant relationships.

---

# 33. Guest Enquiry

A person may submit an Enquiry without:

```text
account registration
login
pre-existing CustomerContext
```

provided the applicable interaction allows it.

This is expected Main Street behaviour, not an exception.

---

# 34. Guest Appointment/Booking transition

An Enquiry may later result in:

```text
Appointment
```

or:

```text
Booking
```

without requiring account creation.

Main Street creates or links the necessary CustomerContext as part of the business operation.

---

# 35. Enquiry and Conversation are distinct

An Enquiry represents:

> the customer's request or question.

A Conversation represents:

> an ongoing communication context between permitted participants.

Example:

```text
Enquiry E100
        ↓
Conversation V100
        ↓
merchant response
        ↓
customer response
```

An Enquiry may be handled without a durable Conversation where the interaction model does not require one.

---

# 36. Conversation does not own subject semantics

Where a Conversation originates from:

```text
Enquiry.subject = Listing L123
```

the Conversation may retain access to that relationship.

But the Listing remains owned by the Listing capability.

The Conversation does not duplicate property semantics.

---

# 37. Enquiry does not mutate business state

A customer may write:

> "Please cancel my viewing."

The message does not itself make:

```text
Appointment = CANCELLED
```

The appropriate authorised Appointment cancellation operation must execute.

Similarly:

> "I want to reserve this room."

does not automatically establish a Booking.

---

# 38. Context can identify the intended operation

Although an Enquiry cannot mutate business state, context may make the merchant's next operation obvious.

For example:

```text
Enquiry
subject = Property L123
customer asks for viewing
```

Merchant-facing actions may include:

```text
Reply
Arrange Appointment
View Listing
Close Enquiry
```

where those operations are supported.

This reduces merchant navigation without turning Enquiry into a workflow engine.

---

# 39. Main Street may suggest actions

Main Street or inference may identify likely follow-up actions from an Enquiry.

Example:

```text
"Can I view the property Saturday?"
```

Possible suggestion:

```text
Arrange viewing Appointment
```

But AI/inference shall not autonomously commit the Appointment.

The relevant operation remains authoritative.

---

# 40. Contextual merchant actions

Merchant-facing action availability should derive from:

```text
Enquiry subject
active capabilities
merchant configuration
actor authority
current business state
```

not from business-category `if/else` branches.

---

# 41. Subject-aware Enquiry across domains

The generic model shall support:

### Real estate

```text
Listing
    ↓
Enquiry
```

### Information publisher

```text
Opportunity
    ↓
Enquiry
```

### Retail

```text
Product
    ↓
Enquiry
```

### Consultant

```text
Service Offering
    ↓
Enquiry
```

### Existing customer Appointment

```text
Appointment
    ↓
Enquiry
```

### Existing Booking

```text
Booking
    ↓
Enquiry
```

---

# 42. Information publisher example

Customer views:

```text
Opportunity O100
Commonwealth Scholarship
```

and selects:

```text
Ask a question
```

Main Street automatically attaches:

```text
subject = O100
```

The customer writes:

> "Are international postgraduate students eligible?"

The publisher receives:

```text
Opportunity
Commonwealth Scholarship

Opportunity reference
O100

Question
Are international postgraduate students eligible?

Reply contact
...
```

The customer does not need to type the scholarship name.

---

# 43. Product example

Customer views:

```text
Product P200
```

and asks:

> "Does this come in a larger size?"

Main Street attaches:

```text
subject = P200
```

The merchant does not need to determine which product the customer meant.

---

# 44. Service example

Customer views:

```text
Offering O300
"Structural Survey"
```

and asks:

> "Do you cover Swansea?"

The Offering context is preserved automatically.

If the operation additionally requires:

```text
property postcode
```

Main Street may ask for it because that information is not already known from the service itself.

---

# 45. Existing Appointment Enquiry

An authenticated or securely authorised customer may open:

```text
Appointment A400
```

and select:

```text
Ask a question
```

Main Street attaches:

```text
subject = Appointment A400
```

The merchant receives the Appointment context automatically.

The customer does not need to type:

```text
appointment number
date
service
```

where Main Street already knows them.

---

# 46. Existing Booking Enquiry

Likewise:

```text
Booking B500
```

may be the subject of:

> "Can I add another guest?"

The merchant receives the Booking relationship automatically.

The Enquiry itself does not modify the Booking.

---

# 47. Subject metadata projection

The merchant-facing Enquiry may project selected metadata from its subject.

For a property:

```text
address
price
listing status
transaction mode
primary image
```

For a product:

```text
name
SKU/reference
price
image
```

For an Appointment:

```text
service
time
customer
current state
```

The precise projection belongs to the relevant presentation/read-model design.

---

# 48. Full merchant context does not mean unrestricted data duplication

"Full information" shall mean:

> **All relevant information required to identify, understand and act on the Enquiry.**

It does not mean duplicating every available field from the subject.

For example, an Enquiry about a property need not duplicate:

```text
internal audit metadata
database timestamps
internal compiler configuration
unrelated staff data
```

The projection should remain operationally relevant.

---

# 49. Current subject data versus submission-time context

A subject may change after an Enquiry is submitted.

Example:

```text
Customer enquires when rent = £1,200
Merchant later changes rent = £1,300
```

Main Street must not lose the ability to determine what context applied when the Enquiry was submitted where that difference is materially relevant.

Therefore the model shall distinguish:

```text
SubjectReference
```

from:

```text
SubmissionContext / provenance evidence
```

---

# 50. SubjectReference remains live relationship

The Enquiry shall retain its stable relationship to:

```text
Listing L123
```

so the merchant can access the current authoritative Listing.

---

# 51. Submission-time provenance

Where mutable subject data is materially relevant to interpreting the Enquiry, Main Street must preserve sufficient provenance to establish the relevant context at submission time.

This may be implemented through:

```text
subject revision reference
projection revision
selected snapshot
event/provenance data
```

The exact representation is deferred.

Hard requirement:

> **Later subject changes must not make historically material Enquiry context impossible to reconstruct.**

---

# 52. Do not indiscriminately snapshot entire subjects

The preceding requirement does not justify copying every subject into every Enquiry.

Snapshots/provenance should be bounded to information whose historical interpretation materially matters.

The precise strategy requires later persistence design.

---

# 53. Enquiry source

Main Street should preserve how an Enquiry originated where useful.

Examples:

```text
Listing page
Product page
Offering page
Booking details
Appointment details
General contact surface
```

This is provenance.

It must not be confused with semantic subject identity.

---

# 54. General merchant Enquiry

Main Street shall still support enquiries without a specific subject.

Example:

```text
"Do you have parking?"
```

Conceptually:

```text
Enquiry
    merchant = M100
    subject = NONE
```

The same capability supports both general and subject-aware enquiries.

---

# 55. Enquiry data ownership

The Enquiry owns information specific to the Enquiry itself.

Examples:

```text
enquiry identity
merchant scope
subject relationship
customer-supplied enquiry data
submission time/provenance
applicable state
```

It does not own copies of the subject's operational model.

---

# 56. Customer-supplied information

Customer-supplied enquiry data may include:

```text
name
reply contact
message
structured answers
```

depending on applicable Enquiry requirements.

No universal complete customer profile is required.

---

# 57. Structured Enquiry answers

Some businesses may benefit from structured questions.

Example:

```text
Preferred contact method:
Email / Phone

Interested in:
Viewing / More information
```

These may be supported where materially useful.

However:

> **Main Street shall not turn Enquiry into an arbitrary merchant-defined form/workflow engine.**

Structured choices must remain within registered supported configuration.

---

# 58. Free-text remains valid

Not every customer question can or should be prestructured.

A free-text question field remains appropriate for open-ended Enquiry.

Main Street may combine:

```text
structured context
+
structured customer fields
+
free-text question
```

without reducing everything to free text.

---

# 59. Enquiry fields should be business-facing

Customer interfaces should use ordinary business language.

Do not expose:

```text
EnquirySubjectReference
CustomerContextId
RequirementNode
SemanticOwner
```

to customers.

The UI should say:

```text
Your name
Your email
Your question
Preferred viewing time
```

where applicable.

---

# 60. Customer effort invariant

A well-designed Main Street Enquiry should avoid asking customers to perform work the platform can perform itself.

Rejected customer tasks include:

```text
retype listing title
retype property address
copy product code
describe which service page they came from
enter Booking reference when launched from Booking
enter Appointment date when launched from Appointment
```

when Main Street already knows that information.

---

# 61. Merchant reconstruction invariant

A merchant should not routinely need to infer subject context from ambiguous free text.

Rejected outcome:

```text
Customer:
"Is this still available?"

Merchant:
"Which one?"
```

when the Enquiry was launched directly from a known Listing.

That represents loss of platform-known context and violates this specification.

---

# 62. CustomerContext association after Enquiry

An initially unlinked Enquiry may later become associated with a CustomerContext.

Example:

```text
E100 submitted
        ↓
merchant arranges Appointment
        ↓
C100 established
        ↓
E100 associated with C100
```

This association preserves continuity.

It does not retrospectively change what E100 was.

---

# 63. Existing CustomerContext Enquiry

Where a reliable existing customer relationship is known:

```text
CustomerContext C100
        ↓
Enquiry E101
```

the Enquiry may reference that CustomerContext immediately.

Customer data need not be duplicated unnecessarily.

---

# 64. Weak matching remains insufficient

Same:

```text
email
telephone
name
```

does not automatically permit customer merging.

The anti-false-merge rules of v1.0 remain in force.

Context-aware Enquiry does not weaken identity integrity.

---

# 65. Enquiry is merchant-scoped

Every Enquiry belongs to exactly the applicable merchant scope.

An Enquiry to:

```text
Merchant A
```

must not be accessible to:

```text
Merchant B
```

merely because the same customer/contact information occurs elsewhere.

---

# 66. Subject must belong to valid scope

An Enquiry claiming:

```text
merchant = Merchant A
subject = Listing owned by Merchant B
```

is structurally invalid unless an explicitly registered cross-scope relationship exists in the future.

Compiler/application validation shall protect scope integrity.

---

# 67. Customer Account does not widen merchant access

If one CustomerAccount interacts with multiple merchants:

```text
Account X
    ├── Merchant A relationship
    └── Merchant B relationship
```

Merchant A sees only Merchant A information.

The Enquiry model shall preserve this boundary.

---

# 68. Subscription remains distinct

A customer may:

```text
subscribe to property alerts
subscribe to scholarship updates
```

without creating CustomerContext solely because of the subscription.

Likewise an Enquiry does not automatically subscribe the sender.

---

# 69. Enquiry contact is not marketing consent

Providing:

```text
email
phone
```

for an Enquiry permits applicable Enquiry response.

It does not automatically authorise:

```text
promotional email
marketing campaign
bulk SMS
```

Marketing/subscription semantics remain separate.

---

# 70. Merchant response

An authorised merchant actor may respond to an Enquiry through the applicable communication mechanism.

Response may occur via:

```text
Main Street Conversation
email integration
other supported communication channel
```

The transport does not redefine the Enquiry semantics.

---

# 71. Enquiry assignment

Some merchants may require Enquiries to be handled by staff.

Where supported, Main Street may relate an Enquiry to:

```text
assigned staff
team
responsible actor
```

This is operational management metadata.

It shall not be universal where unnecessary.

Detailed assignment semantics are deferred.

---

# 72. Enquiry state

Enquiry requires enough lifecycle semantics to distinguish actionable work.

Candidate states might include:

```text
OPEN
RESPONDED
CLOSED
```

but the exact vocabulary is not yet accepted.

State design will be reviewed separately against:

```text
one-way contact forms
multi-message conversations
staff assignment
automated replies
customer follow-up
```

Therefore this document does not finalise the Enquiry state machine.

---

# 73. Closing Enquiry does not modify subject

Closing:

```text
Enquiry E100
```

does not:

```text
remove Listing
cancel Appointment
cancel Booking
delete CustomerContext
```

Enquiry lifecycle remains independent.

---

# 74. Enquiry-derived operations

An Enquiry may result in an independent operation.

Examples:

```text
Property Enquiry
    → Viewing Appointment

Service Enquiry
    → Appointment

Accommodation Enquiry
    → Booking

Product Enquiry
    → Order
```

The resulting operation gets its own identity and authoritative lifecycle.

---

# 75. Provenance may link resulting operation

Main Street may preserve:

```text
Appointment A100
    originated_from Enquiry E100
```

or equivalent provenance.

This permits operational traceability without combining lifecycles.

---

# 76. AI boundary

AI may assist by:

```text
classifying Enquiry
summarising question
identifying likely requested action
suggesting response
extracting non-authoritative information
```

AI shall not:

```text
invent subject relationships
change authoritative subject
create Booking without operation
create Appointment without operation
merge CustomerContexts autonomously
```

Subject relationships originating from the interface should be structurally established rather than inferred from customer prose.

---

# 77. Structured context should outrank textual inference

If:

```text
Enquiry.subject = Listing L123
```

and customer text ambiguously mentions:

> "the flat on High Street"

Main Street already has the authoritative subject relationship.

AI does not need to guess which Listing the customer meant.

Canonical precedence:

```text
Structured interaction context
        >
textual inference
```

for subject identity.

---

# 78. Storefront composition

A capability supporting Enquiry may expose Enquiry actions on relevant public surfaces.

Examples:

```text
Listing
    → Enquire

Offering
    → Ask a question

Publication
    → Enquire

Booking details
    → Contact merchant

Appointment details
    → Ask merchant
```

Whether those actions appear derives from active configuration and applicable semantics.

---

# 79. Enquiry surface should be colocated where useful

Main Street may present Enquiry:

```text
below subject content
```

and/or through:

```text
Enquire button
```

provided both preserve the same subject context.

The UX shall not require navigating to a generic contact page that discards the known subject unnecessarily.

---

# 80. Mobile-first requirement

On mobile:

```text
view subject
    ↓
scroll
    ↓
Enquiry surface
```

or:

```text
tap Enquire
```

should preserve context without requiring copying, pasting, memorising or re-entering subject information.

This follows Main Street's mobile-first operational invariant.

---

# 81. Map integration does not become Enquiry dependency

A real-estate Listing may expose an integrated map.

If the map provider is unavailable:

```text
Listing Enquiry
```

should not necessarily become impossible if the subject identity and required listing information remain available.

Map presentation and Enquiry semantics are therefore separate capabilities/dependencies.

---

# 82. Enquiry completeness

A submitted Enquiry is complete for acceptance when:

```text
valid merchant scope exists
subject relationship is valid where present
all applicable required customer fields are satisfied
submitted data is structurally valid
```

It does not require:

```text
CustomerContext
CustomerAccount
Booking
Appointment
```

unless separately applicable.

---

# 83. Customer-visible confirmation

After successful Enquiry submission, Main Street should provide clear acknowledgement.

Conceptually:

```text
Enquiry received
```

This acknowledgement is not equivalent to:

```text
merchant has responded
Appointment confirmed
Booking confirmed
```

---

# 84. Merchant receives context atomically with Enquiry

Main Street shall not produce an Enquiry where:

```text
message saved
```

but the known subject relationship is accidentally discarded.

For subject-aware Enquiries:

```text
Enquiry + valid subject relationship
```

form part of the accepted Enquiry semantics.

---

# 85. Subject deletion/retirement

If a subject is later removed from active public presentation:

```text
Listing L123 no longer active
```

existing Enquiries referencing L123 shall not lose their historical identity.

The relationship/provenance must remain interpretable according to retention policy.

---

# 86. Subject state can affect new Enquiry availability

If:

```text
Listing = withdrawn
```

Main Street may stop exposing:

```text
new enquiry action
```

depending on registered policy.

Existing Enquiries remain historical interactions.

This is a subject-state/presentation decision, not automatic Enquiry deletion.

---

# 87. Business-specific metadata schemas remain bounded

The realtor example demonstrates that some subjects require structured metadata.

Main Street shall not solve this by permitting arbitrary executable merchant schemas.

Future subject metadata design must preserve:

```text
platform-supported definitions
bounded configuration
merchant-owned values
compiler-valid structure
```

The exact Listing/Offering metadata architecture is deferred for semantic review.

---

# 88. Realtor-specific metadata candidate

Real-estate Listing requirements may eventually include registered concepts such as:

```text
transaction mode
property type
structured address
postcode
city
street
building number
geographic location
price
bedrooms
bathrooms
media
description
listing status
```

These are **not accepted as semantic-core primitives by this document**.

They are evidence that Main Street must support capability-owned structured metadata without business-type runtime branching.

---

# 89. Falsification — property address repetition

Customer views L123 and clicks Enquire.

System asks:

```text
"Enter property address"
```

Result:

**FAIL**

Reason:

Main Street already knows the subject.

This violates Context-Carry-Forward.

---

# 90. Falsification — ambiguous realtor email

Customer views L123 and submits:

> "Is this still available?"

Merchant receives only:

```text
customer email
message
```

Result:

**FAIL**

because the known Listing context was discarded.

---

# 91. Falsification — correct property Enquiry

Customer views L123.

Main Street sends:

```text
subject = L123
customer contact
customer question
```

Merchant projection displays relevant property information.

Result:

**PASS**

---

# 92. Falsification — general realtor Enquiry

Customer asks:

> "Do you manage properties in Cardiff?"

No Listing is involved.

Required:

```text
subject = NONE
```

Result:

**PASS**

The model does not force fake Listing relationships.

---

# 93. Falsification — property details change later

Customer enquires about L123 at £1,200 pcm.

Price later changes.

Main Street retains stable Listing relationship and enough provenance to interpret the original Enquiry context where materially required.

**PASS**, subject to later persistence design.

---

# 94. Falsification — scholarship Enquiry

Customer views Opportunity O10 and asks eligibility question.

Main Street attaches O10 automatically.

No need to type opportunity title.

**PASS**

---

# 95. Falsification — construction Enquiry needs unknown location

Customer views generic construction service.

Main Street knows service but not project location.

Applicable requirement asks customer for:

```text
project postcode
```

**PASS**

Context carry-forward does not prevent asking genuinely unknown necessary information.

---

# 96. Falsification — one-off Enquiry

Customer submits product question.

Merchant answers.

No subsequent operation.

No CustomerContext required.

**PASS**

---

# 97. Falsification — Enquiry becomes viewing Appointment

Property Enquiry leads to viewing.

Main Street creates/links CustomerContext and creates Appointment.

Listing, Enquiry, CustomerContext and Appointment remain distinct.

**PASS**

---

# 98. Falsification — customer says "cancel" in Enquiry

Existing Appointment is subject.

Customer sends:

> "Please cancel this."

Appointment remains unchanged until authorised cancellation executes.

**PASS**

---

# 99. Falsification — same contact at two merchants

Same email submits subject-aware Enquiries to two merchants.

Enquiries and any resulting CustomerContexts remain separately tenant-scoped.

**PASS**

---

# 100. Falsification — arbitrary merchant form builder

Merchant attempts to define executable logic:

```text
if answer X then create Booking
if answer Y then charge customer
```

inside Enquiry configuration.

**FAIL**

Enquiry configuration cannot become arbitrary workflow semantics.

---

# 101. Falsification — AI guesses subject despite known context

Enquiry originates from L123 but AI parses text and assigns L124.

**FAIL**

Structured interaction context is authoritative.

---

# 102. Falsification — map unavailable

Property map fails to render.

Listing identity and Enquiry context remain valid.

Enquiry may still be submitted if all actual requirements are satisfied.

**PASS**

---

# 103. Rejected models

The following are explicitly rejected.

### 103.1 Universal name-email-message Enquiry

Rejected as insufficient for context-rich business operations.

### 103.2 Customers manually identify known subjects

Rejected.

### 103.3 Subject information exists only inside free-text message

Rejected where structured context exists.

### 103.4 Every Enquiry creates CustomerContext

Rejected.

### 103.5 Every Enquiry requires CustomerAccount

Rejected.

### 103.6 Enquiry duplicates full subject data as its authority

Rejected.

### 103.7 Merchant reconstructs subject from customer's prose

Rejected where Main Street already knows the subject.

### 103.8 Context-aware Enquiry requires niche-specific Enquiry engine

Rejected.

### 103.9 Property map belongs to Enquiry

Rejected.

### 103.10 Listing metadata becomes universal Customer data

Rejected.

### 103.11 Arbitrary merchant form/workflow builder

Rejected.

### 103.12 AI determines subject when structured subject context exists

Rejected.

### 103.13 Contact data implies customer identity

Rejected.

### 103.14 Enquiry text directly changes business commitments

Rejected.

### 103.15 Minimal customer input means minimal merchant context

Rejected.

---

# 104. Candidate invariants for semantic review

1. Enquiry is a first-class merchant-scoped business interaction.
2. Enquiry does not itself establish a Booking, Appointment, Order or other commitment.
3. Enquiry may have a specific subject.
4. Enquiry may legitimately have no subject.
5. When initiated from a known subject, the subject relationship is automatically preserved.
6. Customers shall not manually restate known subject information.
7. Main Street shall minimise customer input.
8. Main Street shall maximise relevant merchant context through context carry-forward.
9. Only required information that remains unknown should normally be requested.
10. Subject identity shall be structural rather than inferred from free text where known.
11. The subject-owning capability remains authoritative for subject data.
12. Enquiry does not become authoritative owner of Listing/Product/Offering metadata.
13. Merchant-facing Enquiry projections may expose relevant subject metadata.
14. Enquiry may preserve submission-time provenance where mutable subject information materially affects interpretation.
15. Historical context shall not become unrecoverable solely because the subject later changes.
16. Context snapshots shall not indiscriminately duplicate complete subjects.
17. Enquiry requirements are contextual rather than universal.
18. Enquiry fields may vary by supported capability/context.
19. CustomerContext is not automatically created for every Enquiry.
20. Booking and Appointment still require CustomerContext.
21. CustomerAccount remains optional.
22. CustomerContext remains merchant-scoped.
23. Contact information is not authenticated identity.
24. Enquiry and Conversation remain distinct.
25. Conversation does not own business state.
26. Enquiry text cannot directly mutate business commitments.
27. AI may interpret Enquiry but cannot bypass operation authority.
28. Structured interaction context outranks textual subject inference.
29. Enquiry may lead to an independent Appointment, Booking, Order or other operation.
30. Resulting operations retain their own identities and lifecycles.
31. Provenance may link an operation to its originating Enquiry.
32. Subscription remains separate from Enquiry and Customer.
33. Enquiry contact details do not imply marketing consent.
34. Enquiry remains tenant-isolated.
35. Subject references must respect merchant scope.
36. Customer effort should not increase merely to make merchant processing easier when Main Street can supply the missing context automatically.
37. Merchant processing should not require reconstructing information Main Street already possessed.
38. Enquiry presentation is mobile-first and preserves subject context.
39. Rich subject metadata shall be capability-owned rather than business-category branching in the runtime.
40. Enquiry configuration shall remain bounded and shall not become an arbitrary workflow DSL.

---

# 105. Deferred questions for semantic review

The following require explicit review rather than silent acceptance:

```text
What exactly qualifies as an EnquirySubject?

Should EnquirySubject be a formal semantic abstraction
or simply a registered typed relationship?

What information constitutes EnquiryContext?

Which submission-time subject facts must be preserved?

Should subject revision identity be mandatory?

Where does a generic Listing abstraction belong?

Is Listing distinct from Offering?

How should capability-owned metadata schemas work?

How much freedom may merchants have to configure Enquiry fields?

When should an Enquiry create CustomerContext?

When should an Enquiry become a Conversation?

Does Enquiry need assignment semantics?

What is the minimum Enquiry lifecycle?

How should enquiries about existing Booking/Appointment
relationships be authorised?

How should public Enquiry spam/abuse controls work?

How should map/location semantics relate to Listing?

Should structured address be a general DataConcept?

How should organisations/business customers be represented?
```

---

# 106. Governance state

## PROPOSE

MS-PROT-043 v1.1 has been materially rewritten around:

```text
business-need-driven Enquiry
subject-aware interaction
context carry-forward
minimal customer effort
rich merchant context
merchant-scoped customer relationships
```

**PROPOSE: COMPLETE**

## REVIEW / FALSIFICATION

Initial counterexamples have been included, but because this revision introduces potentially consequential semantics around:

```text
EnquirySubject
Listing
subject metadata
submission-time context
CustomerContext creation
```

the document shall **not** be marked Accepted yet.

**REVIEW: REQUIRED**

## VALIDATE

Formal validation shall follow semantic review and any resulting revision.

**VALIDATE: NOT YET PERFORMED**

## ACCEPT

**NOT YET ACCEPTED**

---

# 107. Proposed canonical decision

> **Main Street Enquiry is a business-need-driven, context-aware interaction rather than a universal contact form. When a customer initiates an Enquiry from a known business subject, Main Street automatically preserves the relationship to that subject and carries forward relevant authoritative context. The customer is asked only for information that Main Street does not already know and that the applicable Enquiry genuinely requires. The merchant receives a complete actionable projection containing the customer's question, required contact information and relevant subject context without having to determine manually which Listing, Offering, Product, Publication, Booking, Appointment or other supported subject the customer means. The subject-owning capability remains authoritative for its own data; the Enquiry references that subject rather than replacing or duplicating it. An Enquiry does not automatically create a CustomerContext, CustomerAccount, Booking or Appointment. Durable customer relationships are established only when an applicable customer-specific operation requires them. This model allows Main Street to reduce effort simultaneously for customers and merchants while preserving semantic ownership, tenant isolation and deterministic business behaviour.**

**MS-PROT-043 v1.1 is now ready for semantic review, not acceptance.**
