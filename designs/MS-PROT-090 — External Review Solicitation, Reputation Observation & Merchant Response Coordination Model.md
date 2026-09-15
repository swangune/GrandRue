# MS-PROT-090 — External Review Solicitation, Reputation Observation & Merchant Response Coordination Model

**Document ID:** MS-PROT-090  
**Version:** 1.0  
**Status:** **ACCEPTED by manual approval on 9 September 2026**  
**Approved:** Manual approval of the complete proposed authority on 9 September 2026  
**Authority type:** Review/Reputation coordination semantic/design authority  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Depends on:** composite MS-PROT-042; composite MS-PROT-043; composite MS-PROT-048; composite MS-PROT-051; composite MS-PROT-053; composite MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-065; MS-PROT-067; composite MS-PROT-068; composite MS-PROT-069; MS-PROT-070; MS-PROT-072; composite MS-PROT-075; MS-PROT-082; composite MS-PROT-085  
**Design node:** Priority-B Review / Reputation Operations  
**Implementation activation:** NONE

---

# 0. Governing Decision

Main Street SHALL support a bounded external-review coordination capability that can:

```text
recognise a genuine customer experience
        ↓
deterministically determine review-solicitation eligibility
        ↓
apply merchant policy
+
customer/contact permission
+
provider destination
        ↓
issue one neutral external-review request
through Notification
```

and separately:

```text
external review provider
        ↓
provider-qualified review evidence
        ↓
merchant observation / Merchant Attention
        ↓
optional merchant response
        ↓
explicit approval of exact response
        ↓
provider execution
```

Main Street SHALL NOT become the authoritative owner of:

```text
customer review content
customer rating
reviewer identity
provider review lifecycle
provider aggregate rating
customer satisfaction
business reputation score
```

The external review provider remains authoritative for the review itself.

Main Street owns only the minimum coordination facts necessary to safely solicit, observe and respond.

Canonical:

```text
customer experience truth
    → source capability

review solicitation coordination
    → MS-PROT-090

review content/rating
    → external review provider

communication delivery
    → Notification

contact legality / permission
    → Data Protection / Regulatory authority

merchant handling
    → Merchant Attention

provider execution
    → Provider / Integration

response wording approval
    → merchant authority
```

---

# 1. Feature Admission

## 1.1 Representation Test — NOT THE PRIMARY JUSTIFICATION

Main Street can represent Orders, Appointments, Bookings and other business operations without owning review/reputation functionality.

Review/Reputation is therefore not admitted merely because businesses use reviews.

## 1.2 Coordination Test — PASS

Main Street already owns or coordinates authoritative facts establishing:

```text
customer relationship
Appointment occurrence
Booking utilisation
merchant/location identity
Notification delivery
provider connections
regulatory permission
Merchant Attention
```

Without a governed review boundary, implementations would be tempted to infer customer satisfaction, selectively choose customers, duplicate provider review truth or create ad-hoc automated responses.

## 1.3 Administrative-Compression Test — PASS

Without coordination, a small merchant commonly must:

```text
remember who to ask
decide when to ask
find the review link
avoid asking repeatedly
monitor an external review platform
notice new reviews
draft replies
switch into the provider application
```

Main Street can safely absorb most of this recurring software administration.

## 1.4 ERP / Suite Drift — PASS

This authority does not create:

```text
CRM
survey platform
NPS system
testimonial manager
social-media management suite
SEO suite
competitor-monitoring system
review marketplace
reputation-scoring engine
customer-success platform
```

---

# 2. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The Fundamental Vision requires Main Street to absorb digital-infrastructure complexity rather than turning the merchant into a software administrator.

This capability satisfies that objective when the merchant experience is approximately:

```text
Ask customers for honest reviews
after eligible experiences?

[On]
```

rather than:

```text
configure review workflows
configure sentiment filters
configure trigger rules
configure provider polling
configure reminder sequences
configure score thresholds
configure reputation automations
```

The internal complexity involving customer-experience evidence, provider identity, contact permission, idempotency, review-platform policy and response approval is justified because it protects truthful customer participation and public merchant speech.

That complexity SHALL remain inside Main Street.

---

# 3. Scope

The initial authority covers exactly three responsibilities:

1. **External Review Solicitation**
2. **External Review Observation**
3. **Merchant Response Coordination**

These responsibilities are independent.

A merchant may use one without requiring all three where applicable infrastructure permits.

---

# 4. Explicit Non-Goals

MS-PROT-090 does not establish:

```text
native Main Street reviews
native star ratings
Main Street review hosting
testimonials
NPS
customer satisfaction scoring
review sentiment scoring as authority
review-to-revenue attribution
review-based customer segmentation
review-based staff performance
review-based incentives
review-removal services
competitor review monitoring
cross-provider reputation scores
SEO ranking optimisation
automatic public review responses
review-generation AI
```

A later proposal seeking any of these requires fresh Feature Admission.

---

# 5. Review Provider Owns Review Truth

An external review is provider-owned content.

Examples include external platforms capable of publishing customer reviews for a merchant.

Main Street SHALL NOT transform:

```text
provider review
```

into:

```text
MainStreetReview
```

merely because Main Street can retrieve it.

Provider-owned facts may include:

```text
provider review identity
reviewer representation
rating
comment
media
creation time
provider reply state
provider moderation state
```

Those remain provider facts.

---

# 6. Review Destination Binding

A **Review Destination Binding** establishes the exact external place to which an eligible customer may be directed to leave a review.

Conceptually:

```text
ReviewDestinationBinding
{
    MerchantScope
    destinationIdentity
    provider / fulfilment affinity
    provider business/listing/location identity
    applicable MerchantLocation affinity
        where required
    currentness
    activation provenance
}
```

A Review Destination Binding is not:

```text
Merchant identity
MerchantLocation identity
ProviderConnection
review content
```

Those remain separate authorities.

---

# 7. Initial Destination Cardinality

For one exact destination scope, the initial portfolio permits:

```text
zero or one current
Review Destination Binding
for targeted Review Solicitation
```

A merchant SHALL NOT configure:

```text
Google + Provider B + Provider C
```

and make one customer choose among multiple review sites inside the initial targeted-solicitation workflow.

Future multi-destination policy requires fresh Feature Admission.

---

# 8. Destination Integrity

A targeted review request SHALL use only a registered, validated Review Destination Binding.

Rejected:

```text
merchant pastes arbitrary URL
        ↓
Main Street automatically emails customers
```

without provider/destination validation.

Main Street MUST establish that the destination belongs to or is legitimately managed for the applicable merchant review target.

---

# 9. Source Capability Owns Customer-Experience Truth

Review/Reputation SHALL NOT decide whether a customer had a genuine experience from generic activity signals.

Rejected:

```text
Payment succeeded
    → genuine experience

Appointment end time passed
    → genuine experience

Conversation exists
    → genuine experience

CustomerContext exists
    → genuine experience

customer sentiment positive
    → genuine experience
```

Instead:

```text
source capability
    owns exact business outcome
        ↓
registered Review Experience Contract
        ↓
review-solicitation eligibility
```

---

# 10. Review Experience Contract

A **Review Experience Contract** is an immutable, versioned, source-owner-qualified semantic definition establishing whether an exact source-owned outcome is sufficient to represent an appropriate review-solicitation point.

Conceptually:

```text
ReviewExperienceContract
{
    contractFamily
    version
    sourceOwner
    sourceSubjectType
    applicabilityRule
    qualifyingExperienceEvidence
    nonqualifyingEvidence
    customerRelationshipRequirement
    destinationAffinityRule
    currentnessRule
}
```

It does not own the source outcome.

It interprets accepted source truth only for the narrow purpose:

```text
may this genuine customer experience
enter review solicitation?
```

---

# 11. Appointment Review Experience

Composite MS-PROT-042 remains authoritative for Appointment Occurrence Outcome.

An Appointment SHALL participate in Review Solicitation only where the exact registered Appointment semantics establish that the Appointment interaction is itself an appropriate customer-experience review point.

This distinction is mandatory.

Example:

```text
haircut Appointment
    OCCURRED
```

may represent the actual customer service experience.

But:

```text
mechanic vehicle-drop-off Appointment
    OCCURRED
```

does not prove the vehicle repair experience has reached an appropriate review point.

Therefore:

> **Appointment `OCCURRED` is not universally review-solicitation eligibility.**

For an Appointment family with an applicable Review Experience Contract, the initial qualifying outcomes are:

```text
OCCURRED
PARTIAL_OCCURRENCE
MERCHANT_SIDE_NON_OCCURRENCE
```

The initial nonqualifying outcome is:

```text
CUSTOMER_NO_SHOW
```

because the customer did not materially participate in the Appointment experience.

No authoritative outcome:

```text
→ UNRESOLVED
→ no solicitation
```

---

# 12. Merchant-Side Failure Is Not Filtered Out

For an otherwise applicable Review Experience Contract:

```text
MERCHANT_SIDE_NON_OCCURRENCE
```

SHALL NOT be excluded merely because the resulting review might be negative.

Likewise:

```text
PARTIAL_OCCURRENCE
```

SHALL NOT be excluded because satisfaction is uncertain.

The purpose of source qualification is:

```text
genuine experience
```

not:

```text
likely positive review
```

---

# 13. Booking Review Experience

Composite MS-PROT-042 remains authoritative for Booking commitment, discharge and Booking Utilisation Outcome.

An initial Booking review experience requires:

```text
Booking reservation commitment = RELEASED

+

registered Review Experience Contract applies

+

current Booking Utilisation Outcome
```

The initial qualifying outcomes are:

```text
UTILISED
PARTIALLY_UTILISED
MERCHANT_SIDE_NON_HONOUR
```

The initial nonqualifying outcome is:

```text
CUSTOMER_NON_UTILISATION
```

No utilisation outcome:

```text
→ UNRESOLVED
→ no solicitation
```

Requiring Booking discharge prevents Main Street from requesting a review while the reserved experience is still materially underway.

---

# 14. Ordering Is Not in the Initial Source Portfolio

Order, Order Fulfilment and Shipment currently provide authoritative commitment, satisfaction and movement facts.

They do not yet provide one universally valid:

```text
customer experience ready for review
```

fact covering:

```text
collection
delivery
replacement
failed delivery
partial fulfilment
post-delivery remediation
```

MS-PROT-090 therefore SHALL NOT manufacture one.

Initial targeted review solicitation source families are limited to the exact Appointment and Booking contracts admitted above.

Ordering/Product review solicitation requires a later owner-qualified Review Experience Contract through normal Feature Admission.

---

# 15. Customer Relationship Requirement

A targeted Review Solicitation requires an exact trusted customer relationship to the source experience.

The initial source families therefore consume the accepted related-customer relationships owned by Appointment/Booking and CustomerContext authority.

Rejected eligibility:

```text
same email
same name
same telephone
similar name
reviewer name similarity
payment-card name
```

Attribute similarity does not establish customer relationship.

---

# 16. Review Solicitation Policy

The initial merchant-facing policy has exactly two modes:

```text
DISABLED

ALL_ELIGIBLE_ONCE
```

No per-customer manual-selection mode exists initially.

---

# 17. Meaning of `ALL_ELIGIBLE_ONCE`

`ALL_ELIGIBLE_ONCE` means:

> Main Street may issue one neutral targeted review request to every independently eligible CustomerContext for the applicable Review Destination, subject to current relationship, contact, regulatory, preference and execution requirements.

It SHALL NOT mean:

```text
ask only good customers
ask customers selected by staff
ask customers selected by rating prediction
ask high spenders only
ask customers without complaints only
```

---

# 18. No Customer-Level Cherry-Picking Workflow

Initial Main Street Review Solicitation SHALL NOT expose:

```text
customer list
    [Send review request]
    [Skip]
```

as an ordinary targeted solicitation workflow.

That mechanism would make selective solicitation operationally easy.

The merchant controls:

```text
solicitation disabled
        or
all eligible customers once
```

Main Street controls deterministic application of the accepted policy.

---

# 19. One Targeted Solicitation Per Customer and Destination

The initial contact-pressure rule is:

```text
maximum one logical Main Street
targeted Review Solicitation

per:

CustomerContext
×
Review Destination
```

There is no rolling numeric reset period.

There are no repeated review reminders.

There is no:

```text
ask again after 30 days
ask again after every appointment
ask again until review received
```

in the initial portfolio.

A materially different repeat-solicitation policy requires fresh Feature Admission.

---

# 20. No Review-Completion Attribution

Main Street SHALL NOT infer:

```text
Review Solicitation R1
        ↓
External Review V1
```

merely because V1 appeared later.

The initial portfolio therefore has:

```text
no review-conversion attribution
no review-request conversion rate
no review-request ROI
```

A provider review may be observed independently.

It is not automatically attributed to an earlier solicitation.

---

# 21. No Historical Backfill

When Review Solicitation becomes enabled:

```text
historical eligible Appointments / Bookings
```

SHALL NOT automatically generate review requests.

The initial portfolio evaluates new qualifying experience points only.

This prevents a newly activated capability from unexpectedly contacting an old customer population.

---

# 22. Contact-Permission Boundary

Review/Reputation does not own legal contact authority.

A targeted email request requires a current owner-qualified contact determination equivalent to:

```text
PERMITTED
NOT_PERMITTED
UNRESOLVED
```

from applicable Data Protection / Regulatory authority for the exact:

```text
MerchantScope
CustomerContext
endpoint
jurisdiction
review-solicitation purpose
governing time
```

Only:

```text
PERMITTED
```

may support externalisation.

```text
NOT_PERMITTED
UNRESOLVED
```

both fail closed.

The exact jurisdiction rule source remains owned by MS-PROT-082 / applicable Data Protection authority.

---

# 23. Initial Review-Request Channel

The initial targeted Review Solicitation channel is exactly:

```text
EMAIL
```

delivered through composite MS-PROT-075.

The initial authority does not activate:

```text
SMS
WhatsApp
social DM
push
automated voice
```

review solicitation.

Those require later channel-specific admission if materially required.

---

# 24. Notification Owns Delivery

Canonical:

```text
Review/Reputation
    determines why the request exists
    and whether it remains applicable

        ↓

Notification Intent

        ↓

Notification
    resolves permitted endpoint/channel
    rendering
    dispatch
    delivery attempt
    delivery evidence
```

Review/Reputation SHALL NOT:

```text
send SMTP directly
select provider directly
implement retries directly
interpret provider delivery status directly
```

---

# 25. Review-Request Content Must Be Neutral

Review-request content SHALL invite an honest review.

It SHALL NOT:

```text
request a five-star review
ask for a positive review
suggest required sentiment
offer compensation
offer discount
offer free goods/services
condition a benefit on reviewing
ask customers to change/remove negative reviews
pressure the customer to review
```

Permitted business meaning is equivalent to:

```text
If you'd like, you can leave an honest review
of your experience with {merchant}.
```

Exact copy remains Notification rendering/presentation scope.

---

# 26. Complaint, Refund and Sentiment Cannot Suppress Solicitation

Where the exact Review Experience Contract establishes eligibility, Main Street SHALL NOT suppress solicitation because:

```text
customer complained
refund exists
refund was requested
Conversation sentiment is negative
merchant thinks customer is unhappy
AI predicts a poor rating
customer spent little
customer is not a repeat customer
```

Those facts do not redefine whether the customer had the qualifying experience.

---

# 27. Recipient Preference

Review Solicitation SHALL use an owner-qualified Notification Contract, initially equivalent to:

```text
reputation / review-request-email@1
```

A recipient may disable that optional Notification Contract/channel under accepted Notification Preference semantics.

```text
DISABLED
    → no review-request email
```

Preference does not create permission.

---

# 28. Review Solicitation Identity

One logical Review Solicitation SHALL have durable identity based on:

```text
MerchantScope
+
CustomerContext
+
Review Destination
```

and SHALL retain provenance including:

```text
source experience
Review Experience Contract
source outcome/revision
merchant policy revision
contact-policy evidence
destination binding
Notification Intent
creation time
```

Contract/policy revisions are provenance.

They do not create another solicitation identity for the same CustomerContext × destination.

---

# 29. Currentness Before External Effect

Immediately before a new review-request external effect, Main Street SHALL revalidate:

```text
Review Solicitation Policy still enabled

Review Destination Binding still current

source experience remains authoritatively qualifying

CustomerContext relationship remains valid

endpoint remains applicable

contact permission = PERMITTED

recipient preference permits channel

Notification fulfilment path remains admissible
```

A stale eligibility decision SHALL NOT authorize a new external effect.

---

# 30. Outcome Correction Before Send

Example:

```text
Appointment outcome initially OCCURRED

review request prepared but not externalised

outcome corrected to CUSTOMER_NO_SHOW
```

Expected:

```text
request externalisation blocked
```

The current source truth governs before effect.

---

# 31. Outcome Correction After Send

Different case:

```text
review request external effect already occurred

later source outcome corrected
```

Main Street SHALL NOT attempt to:

```text
unsend email
rewrite historical eligibility
erase the solicitation
```

The historical solicitation retains the exact source evidence under which it was legitimately issued.

No second solicitation is created.

---

# 32. External Review Observation

Main Street MAY retrieve reviews from a registered external Review Destination where the applicable provider connection, authorisation, provider policy and data-protection conditions permit.

The result is:

```text
provider-qualified external review material
```

not native Main Street review truth.

---

# 33. External Review Reference

Main Street MAY retain the minimum provider-qualified locator/provenance necessary to coordinate handling:

```text
ExternalReviewReference
{
    MerchantScope
    ReviewDestinationBinding
    provider review reference
    first observed time
    minimum permitted provider provenance
}
```

An External Review Reference is not the review.

---

# 34. Provider Review Material Is Request-Scoped by Default

The initial design assumes provider review content is:

```text
request-scoped / transient
```

unless the applicable provider/data-lifecycle contract explicitly permits bounded caching.

Main Street SHALL NOT require a permanent internal copy of:

```text
review text
rating
reviewer profile
review media
provider aggregate score
```

to operate Review/Reputation.

Provider-specific limits remain binding.

---

# 35. No Internal Reputation Warehouse

Initial Review/Reputation SHALL NOT create:

```text
review-content warehouse
rating history warehouse
cross-provider review index
sentiment archive
merchant review data lake
```

nor a materialised:

```text
Main Street Reputation Score
```

---

# 36. No Reviewer-to-Customer Matching

An external provider reviewer SHALL NOT be reconciled to a Main Street CustomerContext using:

```text
display name
avatar
review text
rating
location
AI inference
similar contact attributes
```

A provider reviewer and a Main Street customer remain independent identities absent a separately authoritative relationship.

---

# 37. Merchant Attention Integration

The initial Review/Reputation portfolio SHALL register one Attention Contract:

```text
reputation / external-review-observed@1
```

Its source episode is:

```text
one newly observed
provider-qualified external review reference
```

Its activation identity is based on the exact external review reference.

Duplicate provider retrieval does not create duplicate Attention occurrences.

---

# 38. Review Attention Is Sentiment-Neutral

The Attention Contract applies regardless of:

```text
1-star
2-star
3-star
4-star
5-star
rating-only
written review
positive text
negative text
```

Main Street SHALL NOT initially create:

```text
negative reviews = attention
positive reviews = hidden
```

or vice versa.

No universal rating-based priority is introduced.

---

# 39. Initial Review Handling

The merchant-facing review observation supports exactly these ordinary handling outcomes:

```text
REVIEWED_NO_REPLY_RECORDED

REPLY_APPROVED_FOR_EXECUTION
```

These are Attention/review-coordination handling facts.

They do not modify the provider review.

---

# 40. Review Reply Is Public Merchant Speech

A merchant response to an external review is:

```text
merchant-authorised
provider-bound public response intent
```

It is not:

```text
Customer Messaging Conversation
Publication
review truth
customer-service resolution
```

The response is bound to one exact external provider review.

---

# 41. Review Response Instruction

Before an external reply effect, Main Street SHALL durably establish an exact merchant-approved response instruction equivalent to:

```text
MerchantReviewResponseInstruction
{
    MerchantScope
    logical instruction identity
    ReviewDestinationBinding
    exact ExternalReviewReference
    exact reviewed-material affinity
    exact approved response text
    approving actor
    approval time
    provider execution affinity
}
```

The exact physical representation is implementation scope.

---

# 42. Every External Reply Requires Specific Approval

The initial portfolio SHALL NOT support:

```text
automatic review replies
standing blanket reply authority
"auto-reply to 5-star reviews"
"auto-apologise to 1-star reviews"
AI send without confirmation
```

Every external review reply requires an authorised merchant actor to approve:

```text
this exact response
to
this exact review
```

before provider execution.

---

# 43. Review Must Be Revalidated Before Reply

The provider review may change after the merchant first observes it.

Before externalisation Main Street SHALL re-establish the current review material or sufficient currentness evidence.

If materially relevant review content changed after the merchant approved a response:

```text
previous approval = stale
```

and the response SHALL NOT be sent until the merchant reviews and approves again.

---

# 44. Existing Provider Reply Is Authoritative Provider State

If a merchant responds directly through the external provider:

```text
provider has current reply
```

Main Street SHALL observe that provider state where permitted.

It SHALL NOT automatically overwrite or revert the external response merely because Main Street contains older response material.

---

# 45. Response Replacement and Withdrawal

The initial operation families are:

```text
reputation.reply-to-external-review

reputation.withdraw-external-review-reply
```

Both require:

```text
exact target review
current provider evidence
specific merchant instruction
current actor authority
current provider readiness
current provider/content policy
```

Replacing an existing provider reply requires approval of the exact replacement text.

Withdrawal requires explicit merchant instruction.

---

# 46. Provider Effect Safety

Every consequential external response effect SHALL begin from durable exact intent before provider invocation.

Provider timeout or uncertain acknowledgement SHALL NOT trigger blind duplicate execution.

Canonical:

```text
durable response instruction
        ↓
provider attempt
        ↓
definitive evidence
    or
execution uncertainty
        ↓
reconciliation
```

Composite MS-PROT-069 remains authoritative for uncertainty.

---

# 47. Provider Moderation Does Not Become Main Street Review Truth

A provider may:

```text
accept reply
moderate reply
reject reply
flag policy violation
delay publication
remove content
```

These remain provider evidence.

Main Street may represent their operational consequence without inventing:

```text
review accepted by Main Street
review rejected by Main Street
merchant reputation changed
```

---

# 48. AI Boundary

AI MAY, where the applicable provider/data-use contract permits the review material to be processed:

```text
help draft a merchant reply
suggest clearer wording
identify potentially sensitive wording
summarise provider material for the merchant
```

AI SHALL NOT:

```text
determine Review Solicitation eligibility
predict which customers will leave positive reviews
suppress customers based on predicted sentiment
create fake reviews
generate customer reviews
publish a review response
withdraw a response
reconcile reviewer identity to CustomerContext
create authoritative satisfaction
create a Reputation Score
```

If provider/content-use rules do not permit AI processing of review content:

```text
AI assistance = unavailable
```

without blocking ordinary merchant review handling.

---

# 49. No Staff Review Quotas

Main Street SHALL NOT establish:

```text
staff member must obtain 5 reviews
review count per employee
review rating per worker
review solicitation leaderboard
bonus linked to reviews
```

as part of the initial Review/Reputation capability.

This prevents review manipulation and employee-performance-system drift.

---

# 50. Merchant UX

A simple merchant should experience review setup approximately as:

```text
Customer reviews

Ask customers for an honest review
after eligible visits or stays?

[On]

Review destination:
Google Business Profile
[Connected]
```

The merchant SHALL NOT configure:

```text
source outcome rules
rating thresholds
sentiment rules
contact algorithms
retry schedules
provider polling cadence
review identity matching
Attention activation IDs
```

Main Street absorbs those mechanics.

A new review may appear approximately as:

```text
New customer review

[provider-supplied review material]

[Reply]
[No reply]
```

where provider terms permit the material to be displayed.

---

# 51. Low-Software-Capacity Merchant Falsification

Scenario:

A barber runs one shop and understands neither review APIs nor marketing automation.

The merchant turns on:

```text
Ask customers for reviews
```

A haircut Appointment reaches:

```text
OCCURRED
```

under an applicable Review Experience Contract.

Main Street:

```text
recognises genuine experience
checks customer relationship
checks one-request rule
checks contact permission
checks preference
resolves review destination
sends neutral request through Notification
```

The barber configures none of those mechanisms.

**PASS**

---

# 52. Falsification — Mechanic Drop-Off

```text
Appointment:
vehicle drop-off

Outcome:
OCCURRED
```

But the Appointment semantics do not establish that the repair/service experience itself has reached its Review Experience Contract point.

Expected:

```text
no review solicitation
```

**PASS**

---

# 53. Falsification — Merchant Failure

```text
qualifying haircut Appointment

Outcome:
MERCHANT_SIDE_NON_OCCURRENCE
```

The customer had a genuine merchant-side failed experience.

Expected:

```text
eligible under the same neutral experience contract
```

not:

```text
suppress because likely negative
```

**PASS**

---

# 54. Falsification — Customer No-Show

```text
Appointment:
CUSTOMER_NO_SHOW
```

Expected:

```text
no targeted review request
```

because the customer did not materially experience the qualifying interaction.

**PASS**

---

# 55. Falsification — Booking

```text
hotel Booking
reservation RELEASED
Utilisation Outcome = UTILISED
```

Expected:

```text
eligible
```

Different case:

```text
CUSTOMER_NON_UTILISATION
```

Expected:

```text
not eligible
```

**PASS**

---

# 56. Falsification — Negative Complaint

Customer has:

```text
qualifying experience
+
complaint
+
refund request
```

Expected:

```text
complaint/refund does not selectively suppress
otherwise valid review solicitation
```

**PASS**

---

# 57. Falsification — Frequent Customer

Customer receives haircut every two weeks.

Expected:

```text
first independently eligible experience
may create one solicitation

later visits
do not create another targeted request
for same Review Destination
```

No arbitrary frequency configuration is required.

**PASS**

---

# 58. Falsification — Merchant Requests Happy Customers Only

Merchant asks Main Street:

```text
Only send review requests
to customers who seemed happy.
```

Expected:

```text
unsupported
```

Main Street does not create a sentiment-gated solicitation policy.

**PASS**

---

# 59. Falsification — Contact Permission Unresolved

Customer experience qualifies but jurisdiction/contact authority returns:

```text
UNRESOLVED
```

Expected:

```text
no external review-request effect
```

**PASS**

---

# 60. Falsification — Review Changes Before Reply

Merchant approves reply against review material R1.

Customer edits the external review to materially different R2 before provider effect.

Expected:

```text
old response approval fails currentness
merchant reviews R2
new approval required
```

**PASS**

---

# 61. Falsification — AI Draft

AI drafts:

```text
"Thank you for your feedback..."
```

Expected:

```text
draft only
```

No provider effect occurs until exact merchant approval.

**PASS**

---

# 62. Falsification — Provider Timeout

Merchant approves reply.

Provider execution becomes uncertain.

Expected:

```text
no blind duplicate reply
reconciliation required
```

**PASS**

---

# 63. Falsification — External Merchant Edit

Merchant changes the reply directly in the provider application.

Expected:

```text
provider current state remains authoritative

Main Street does not automatically
restore its previous reply
```

**PASS**

---

# 64. Falsification — Review Data Warehouse

Implementation proposes storing all provider review bodies and ratings forever for future analytics.

Expected:

```text
rejected by MS-PROT-090
unless a later authority plus provider/data-use contract
explicitly admits that capability
```

**PASS**

---

# 65. Architectural Review

## Ownership

**PASS**

Source capabilities own customer-experience truth.  
External providers own review truth.  
Review/Reputation owns only solicitation and response coordination.  
Notification owns delivery.  
Merchant Attention owns handling.  
Data Protection/Regulatory owns contact authority.

## Provider Replaceability

**PASS**

Review Destination Binding is provider-qualified without making provider identity canonical business semantics.

## AI Non-Authority

**PASS**

AI may assist wording only where permitted.

## Administrative Compression

**PASS**

Merchant configuration is reduced to a small business-facing activation choice rather than review-workflow administration.

## Anti-ERP / Anti-Suite

**PASS**

No native review marketplace, CRM, NPS, social-management suite or SEO platform is created.

---

# 66. Hard Invariants

```text
INV-090-001
External review providers own external review truth.

INV-090-002
Main Street SHALL NOT create a native review/rating authority merely by retrieving provider content.

INV-090-003
Review Solicitation eligibility requires an exact source-owner-qualified Review Experience Contract.

INV-090-004
Appointment OCCURRED is not universally review-solicitation eligibility.

INV-090-005
Initial qualifying Appointment outcomes may include merchant-side failure and partial occurrence; likely negative sentiment cannot be used to exclude them.

INV-090-006
CUSTOMER_NO_SHOW is not an initial qualifying Appointment experience.

INV-090-007
Initial Booking solicitation requires RELEASED reservation commitment plus qualifying Booking Utilisation Outcome.

INV-090-008
Ordering/Product review solicitation is not admitted by v1.0.

INV-090-009
Initial merchant Review Solicitation policy is exactly DISABLED or ALL_ELIGIBLE_ONCE.

INV-090-010
No per-customer selective targeted-solicitation workflow exists initially.

INV-090-011
At most one logical Main Street targeted review solicitation exists per CustomerContext × Review Destination.

INV-090-012
No repeated targeted-review reminder exists initially.

INV-090-013
Customer sentiment, complaint, refund, spend, loyalty or AI prediction cannot determine solicitation eligibility.

INV-090-014
Targeted externalisation requires current independently authoritative contact permission.

INV-090-015
Initial targeted Review Solicitation channel is EMAIL through Notification.

INV-090-016
Review-request content must remain neutral and SHALL NOT request positive sentiment, rating level or incentive-conditioned review behaviour.

INV-090-017
Main Street does not attribute an external review to a previous solicitation without separately accepted authority.

INV-090-018
No historical review-solicitation backfill occurs when the capability is activated.

INV-090-019
Provider review content is request-scoped/transient by default and subject to provider-specific data-use restrictions.

INV-090-020
No native cross-provider Reputation Score, rating warehouse or review-content warehouse exists initially.

INV-090-021
External reviewer identity SHALL NOT be inferred into CustomerContext from attribute similarity or AI.

INV-090-022
The initial external-review Attention family is sentiment-neutral.

INV-090-023
Every external review reply requires specific merchant approval of exact text for the exact review.

INV-090-024
A materially changed review invalidates stale response approval.

INV-090-025
AI cannot publish, withdraw or automatically trigger review responses.

INV-090-026
A durable merchant response instruction must precede consequential provider effect.

INV-090-027
Execution uncertainty blocks blind duplicate provider response effects.

INV-090-028
Main Street SHALL NOT automatically revert merchant changes made directly through the external provider.

INV-090-029
No staff review quotas, review-rating performance measures or review-solicitation leaderboards exist initially.

INV-090-030
Implementation activation remains NONE.
```

---

# 67. Rejected Alternatives

Rejected:

```text
Main Street hosts its own review marketplace

one universal reputation score

OCCURRED → review request for every Appointment

only solicit customers predicted to be happy

private complaint first, public review only if positive

manual merchant customer-by-customer review-request list

repeat requests after every transaction

review-request reminder campaign

review incentives

"leave us five stars"

SMS/WhatsApp/social solicitation in the initial portfolio

Payment as customer-experience proof

Conversation sentiment as review eligibility

reviewer-name matching to CustomerContext

cross-provider permanent review warehouse

automatic AI review replies

automatic negative-review apologies

automatic positive-review thank-you replies

automatic provider-reply overwrite/reversion

review quotas for staff

review-based employee performance scoring
```

---

# 68. Current External-Policy Falsification

The model is compatible with the current major-platform pattern in which:

- merchants may neutrally ask customers for reviews;
- incentivised and selectively positive solicitation is prohibited;
- external review platforms remain the source of review content;
- merchant responses require merchant authorisation; and
- provider API content may carry strict storage/use restrictions.

Those provider policies are external constraints, not MS-PROT-090 semantic ownership.

If a provider later changes its rules:

```text
provider contract / integration changes
```

rather than:

```text
Main Street review semantics silently change
```

unless the change reveals that the accepted semantic model itself is no longer safe.

---

# 69. Deferred-Scope Outcome

MS-PROT-090 v1.0 SHALL have **no retained semantic DQ for its defined initial portfolio**.

The following are deliberately outside the initial portfolio and do not remain open questions requiring immediate resolution:

```text
Order/Product review-solicitation source contracts
repeat/recurrent review solicitation
review-request reminders
SMS/WhatsApp/social solicitation
multiple simultaneous review destinations
native review hosting
testimonials
NPS/surveys
cross-provider reputation analytics
rating trends
review-to-revenue attribution
automatic public replies
review dispute/reporting automation
competitor monitoring
SEO/reputation optimisation
```

Any of these requires fresh Feature Admission if later promoted.

Exact provider vendor, SDK, polling/webhook mechanism, provider-specific content cache duration within allowed limits, API DTOs, persistence technology and UI layout remain implementation/provider-policy matters under existing authorities.

---

# 70. Fundamental Vision Result

```text
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
```

The proposal reduces recurring merchant administration while refusing the easiest manipulative shortcuts.

The necessary internal complexity exists to preserve:

```text
genuine experience
neutral solicitation
merchant public-speech authority
provider truth
privacy
contact permission
idempotency
provider uncertainty
```

The merchant does not administer that machinery.

---

# 71. Recommendation

```text
RECOMMENDATION: ACCEPT
```

MS-PROT-090 is the minimum sufficiently expressive Review/Reputation capability for Main Street's current target.

It adds genuine cross-capability value without becoming:

```text
review platform
CRM
marketing suite
social-management tool
SEO product
customer-survey product
```

It preserves the central architectural rule:

> **Main Street coordinates reputation-related operations around authoritative business experience and external-provider truth; it does not manufacture customer opinion or own the review ecosystem.**

---

# 72. Approval Boundary

This authority received explicit manual approval on 9 September 2026 and is authoritative within its accepted scope.

Formalisation SHALL preserve the approved normative meaning.

**Implementation activation: NONE.**
