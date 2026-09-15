# MS-PROT-089 — Capacity Waitlist & Availability Opportunity Coordination Model

**Document ID:** MS-PROT-089  
**Version:** 1.0  
**Status:** ACCEPTED  
**Approved:** 9 September 2026 by explicit manual approval  
**Authority type:** Cross-capability capacity-demand coordination semantic/design authority  
**Promoted by:** MS-PROT-042 v1.15  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Depends on:** composite MS-PROT-020; MS-PROT-025; composite MS-PROT-042 through v1.15; composite MS-PROT-043 through v1.4; composite MS-PROT-053; MS-PROT-054; composite MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-065; composite MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-073; composite MS-PROT-075; composite MS-PROT-085; composite MS-PROT-086  
**Resolves:** promoted Waitlist design node created by MS-PROT-042 v1.15  
**Implementation activation:** NONE

---

# 1. Governing Decision

Main Street SHALL represent capacity-related unmet customer demand through a bounded native **Capacity Waitlist** capability.

The governing distinction is:

```text
customer wants an unavailable opportunity
        ↓
Waitlist Entry

capacity may later become available
        ↓
source owner establishes
current opportunity evidence

Waitlist selects
the correct queued demand
        ↓
Waitlist Promotion

customer accepts
        ↓
source-owner operation

        ↓

Booking
Appointment
or Appointment Participation
```

Hard boundary:

```text
Waitlist
    owns queued unmet demand
    and promotion coordination

Booking / Appointment
    own eventual customer commitment

Resource / Allocation
    own capacity

Scheduling
    owns Appointment schedulability

Notification
    owns communication delivery

CustomerContext
    owns merchant/customer relationship
```

Waitlist SHALL NOT become a reservation engine, CRM queue, workflow platform or second source of capacity truth.

---

# 2. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING**

A target merchant should be able to express:

```text
"If 2pm becomes free,
offer it to the next person."

"Put Sarah on the cancellation list."

"James doesn't want the space anymore."
```

without manually operating:

```text
spreadsheets
paper callback lists
priority calculations
notification workflows
capacity polling
retry queues
customer follow-up logs
```

Main Street absorbs those mechanics.

Ordinary staff need only understand:

```text
waiting
space available
offered
accepted
declined
```

not Main Street's coordination architecture.

---

# 3. Feature Admission

MS-PROT-042 v1.15 already established:

```text
Representation Test
    PASS

Coordination Test
    PASS

Administrative-Compression Test
    PASS
```

MS-PROT-089 does not reopen that decision.

Its responsibility is to define the smallest safe admitted capability.

---

# 4. Explicit Non-Goals

MS-PROT-089 does not create:

```text
generic queue management

CRM lead pipelines

sales stages

support-ticket queues

restaurant host queues

warehouse queues

employee queues

workflow designers

customer scoring

VIP segmentation

marketing lists

product back-ordering

generic event ticketing

membership management

priority optimisation

overbooking

capacity inflation

generic reservation Holds

generic callback management
```

Overbooking remains rejected for the initial portfolio under MS-PROT-042 v1.15.

---

# 5. Canonical Concepts

MS-PROT-089 establishes:

```text
Waitlist Opportunity Contract

Waitlist Target

Waitlist Entry

Queue Precedence

Waitlist Admission Assessment

Source Opportunity Assessment

Promotion Candidate Assessment

Waitlist Promotion

Customer Promotion Response

Waitlist Entry Resolution
```

These concepts are bounded to capacity-related customer demand.

---

# 6. Waitlist Opportunity Contract

A **Waitlist Opportunity Contract** is an immutable, versioned, source-owner-qualified semantic contract defining how Waitlist may coordinate one exact family of source-owned capacity opportunity.

Conceptually:

```text
WaitlistOpportunityContract
{
    contractFamily
    contractVersion

    sourceOwner

    targetDefinition
    targetCanonicalisationRule

    admissionAssessmentRule
    targetCurrentnessRule

    sourceOpportunityAssessmentRule
    candidateEligibilityRule

    promotionProtectionMode

    promotionResponseProfile

    sourceCommitmentHandoff

    sourceCommitmentSuccessEvidence

    protectionAndExposureRule

    notificationContract

    failureClassification
}
```

It is not a merchant-authored rules DSL.

---

# 7. Source Owner Retains Semantic Authority

The source capability defines:

```text
what the customer ultimately wants

whether that target is materially valid

whether capacity currently exists

whether a customer may currently commit

what source operation creates commitment

what source evidence proves commitment
```

Waitlist consumes those determinations.

Waitlist SHALL NOT reproduce them.

---

# 8. Initial Waitlist Opportunity Contract Portfolio

The initial portfolio contains exactly three source-qualified families:

```text
appointment /
capacity-waitlist-exact-interval@1

appointment /
capacity-waitlist-shared-participation@1

booking /
capacity-waitlist-exact-reservation@1
```

No generic arbitrary-target contract exists.

---

# 9. Exact-Interval Appointment Waitlist

`appointment / capacity-waitlist-exact-interval@1` represents unmet demand for one exact candidate Appointment opportunity.

Material target meaning includes the source-required dimensions necessary to distinguish the requested commitment, including as applicable:

```text
Merchant Scope

Offering / scheduled-operation context

exact interval

applicable Appointment
Support Requirement semantics

other source-required commitment affinity
```

The source Appointment/Scheduling authority defines those dimensions.

---

# 10. Shared Appointment Participation Waitlist

`appointment / capacity-waitlist-shared-participation@1` represents demand for participant capacity in one exact existing shared Appointment.

Example:

```text
Friday 18:00 Yoga

capacity = full

Sarah
    ↓
waitlist
```

The eventual source-owned commitment is:

```text
Appointment Participation
```

not another duplicate Appointment.

---

# 11. Exact Booking Reservation Waitlist

`booking / capacity-waitlist-exact-reservation@1` represents unmet demand for one exact Booking reservation meaning.

Material dimensions include as applicable:

```text
Booked Subject semantics

Reservation Scope

quantity

other source-required
Booking commitment material
```

Waitlist does not invent generic booked-subject equivalence.

---

# 12. Initial Portfolio Is Exact-Target Only

The following are not included initially:

```text
"any Saturday this month"

"next available appointment"

"anything after 5pm"

"any room under £100"

series-wide recurring waitlists

product restock waitlists

inventory backorders

multiple interchangeable preference sets
```

Those require later explicit target-contract admission.

This is not a retained MS-PROT-089 DQ.

---

# 13. Waitlist Target

A **Waitlist Target** is the Waitlist-owned grouping identity for one exact source-qualified unmet-demand meaning under one Waitlist Opportunity Contract.

Conceptually:

```text
WaitlistTarget
{
    MerchantScope

    targetIdentity

    contractFamily
    contractVersion

    sourceOwner

    canonical source-target affinity

    material target provenance
}
```

Waitlist owns the grouping identity.

The source capability owns the business meaning referenced by it.

---

# 14. Target Canonicalisation

Two customers SHALL share one Waitlist Target only when the source-qualified contract deterministically establishes that their requested capacity opportunity has equivalent material meaning.

Rejected:

```text
same display text
same price
same date label
AI says "basically the same"
```

as target-equivalence authority.

---

# 15. Target Meaning Is Immutable

A materially changed source target SHALL NOT silently mutate an existing Waitlist Target.

Example:

```text
Shared Appointment
Friday 18:00
        ↓
rescheduled
Saturday 09:00
```

Existing Friday waitlist entries do not silently become Saturday demand.

The old target becomes non-applicable according to source authority.

A new target/customer decision is required where appropriate.

---

# 16. Waitlist Entry

A **Waitlist Entry** is:

> A durable merchant-scoped fact that one exact CustomerContext has explicitly requested to wait for one exact Waitlist Target.

Conceptually:

```text
WaitlistEntry
{
    entryIdentity

    MerchantScope

    targetIdentity

    CustomerContext identity

    admittedAt

    precedenceKey

    customer-request provenance

    governing contract affinity

    current resolution?
}
```

It is not:

```text
Booking
Appointment
Appointment Participation
Hold
Allocation
Enquiry
Conversation
Attention Occurrence
```

---

# 17. Customer Intent Is Required

Waitlist Entry creation requires authoritative evidence of customer intent.

Accepted initial authority classes:

```text
DIRECT_CUSTOMER_ACTION

AUTHORISED_MERCHANT_ATTESTATION
OF_CUSTOMER_REQUEST
```

Examples:

```text
customer presses:
[Join waiting list]

customer phones merchant:
"put me on the cancellation list"

customer asks in person
```

---

# 18. Browsing Does Not Create Waitlist Membership

Rejected:

```text
customer viewed fully booked slot
        ↓
automatically add to waitlist
```

Likewise:

```text
abandoned booking attempt

AI predicts interest

Enquiry sentiment

previous purchase

Conversation existence
```

does not create membership.

---

# 19. Conversation Message Is Not Waitlist Instruction

A Message such as:

```text
"let me know if someone cancels"
```

is communication evidence.

It SHALL NOT automatically create a Waitlist Entry.

AI may identify:

```text
possible waitlist intent
```

and prepare an authorised action.

A direct customer action or authorised merchant attestation is still required.

This preserves Customer Messaging's non-command boundary.

---

# 20. CustomerContext Requirement

Every current Waitlist Entry SHALL relate to one exact merchant-scoped CustomerContext.

Waitlist SHALL NOT create a competing customer identity.

Where a customer does not yet have the required CustomerContext:

```text
customer waitlist request
        ↓
existing CustomerContext
establishment/linking authority
        ↓
Waitlist Entry
```

CustomerAccount creation is not required.

---

# 21. Contact Equality Is Not Customer Identity

The following SHALL NOT merge Waitlist Entries or CustomerContexts:

```text
same email

same phone number

same name

same address
```

Any CustomerContext reconciliation remains governed by its accepted authority.

---

# 22. CustomerAccount Is Optional

A customer may participate through:

```text
registered customer context
```

or, where offered:

```text
trusted purpose-bound
guest contextual access
```

under MS-PROT-063.

Waitlist SHALL NOT require a global customer login merely to join, withdraw or respond.

---

# 23. Guest Contextual Access

Where Main Street provides a secure guest action:

```text
View waitlist entry

Withdraw

Accept opportunity

Decline opportunity
```

the contextual proof SHALL be bound to:

```text
Merchant Scope

exact Waitlist Entry

exact permitted action class

applicable validity/currentness
```

Possession of:

```text
email address
phone number
target identity
entry number
```

is insufficient.

---

# 24. Duplicate Membership

Initial invariant:

> One CustomerContext may have at most one current Waitlist Entry for one exact Waitlist Target.

Therefore:

```text
same customer
+
same target
+
repeated join
```

converges to the existing current entry.

No second queue position is created.

---

# 25. Different Targets Remain Independent

The same CustomerContext MAY legitimately wait for:

```text
Target A
and
Target B
```

where the targets are materially distinct.

No global customer waitlist status exists.

---

# 26. Waitlist Admission Assessment

Before entry creation, the applicable source contract SHALL produce a current:

**Waitlist Admission Assessment**

with exactly one of:

```text
WAITLISTABLE

DIRECT_COMMITMENT_AVAILABLE

NOT_WAITLISTABLE

UNRESOLVED
```

---

# 27. `WAITLISTABLE`

`WAITLISTABLE` means:

> The exact target is valid for this waitlist contract, no independent authoritative rejection already prevents the intended source commitment, and current source evidence establishes qualifying capacity-related unmet demand.

It does not guarantee future availability.

---

# 28. `DIRECT_COMMITMENT_AVAILABLE`

`DIRECT_COMMITMENT_AVAILABLE` means:

> Current source evidence establishes that the customer may proceed toward ordinary source commitment rather than join the waitlist.

Main Street SHOULD route toward:

```text
Booking
Appointment
or
Appointment Participation
```

instead of creating avoidable queue state.

Final source commitment still performs ordinary revalidation.

---

# 29. `NOT_WAITLISTABLE`

Examples may include:

```text
target is no longer valid

time is outside supported
Appointment semantics

Booking reservation scope
is invalid

capacity scarcity is not
the material blocking reason

target family is unsupported
```

No Waitlist Entry is created.

---

# 30. `UNRESOLVED`

Where sufficient current source evidence cannot establish admission:

```text
UNRESOLVED
```

Main Street SHALL fail closed.

It SHALL NOT add the customer:

```text
"just in case"
```

---

# 31. Availability Projection Is Insufficient

A UI showing:

```text
FULL
```

does not by itself authorise:

```text
WAITLISTABLE
```

The applicable source contract must perform current owner-qualified assessment.

Projections guide interaction.

They do not own admission.

---

# 32. Queue Precedence

Each admitted Waitlist Entry receives an immutable **Queue Precedence**.

Initial policy:

> **FIFO by authoritative admission order within one exact Waitlist Target.**

Queue precedence is Waitlist-owned.

It is not merchant-authored priority.

---

# 33. Authoritative Admission Order

Precedence SHALL use Main Street authoritative admission ordering.

It SHALL NOT be based on:

```text
customer device time

merchant-entered historical timestamp

email timestamp

claimed phone-call time

AI interpretation
```

A concurrent tie SHALL receive a deterministic total-order tie-break established by Main Street.

The exact database mechanism is implementation scope.

---

# 34. No Backdating

A merchant cannot enter:

```text
"Sarah actually asked yesterday"
```

to obtain an earlier queue position today.

The real historical interaction may be retained as provenance.

Queue admission starts when Main Street authoritatively accepts the Waitlist Entry.

---

# 35. No Priority Scores

The initial Waitlist model contains no:

```text
VIP priority

high-value customer priority

spend score

loyalty score

AI priority

urgency score

manual numeric priority

demographic priority

staff preference score
```

There is no merchant priority DSL.

---

# 36. No Manual Reordering

The initial operation portfolio SHALL NOT include:

```text
move to top

move down

set position

promote specific customer
```

as Waitlist-owned operations.

A merchant may explicitly resolve/remove an entry where authorised.

That is not hidden reordering.

---

# 37. Waitlist Does Not Become a Global Gatekeeper

Queue precedence governs:

```text
Waitlist-owned promotion
```

only.

It does not prohibit a source capability from accepting another independently legitimate direct Booking or Appointment under its own authority.

Therefore Waitlist membership does not guarantee:

```text
exclusive priority
over all future merchant demand
```

unless the source capability has separately established protected capacity for the promoted customer.

---

# 38. Customer-Facing Position

The initial customer portfolio SHALL NOT expose an exact numeric queue position.

A customer may see meaning equivalent to:

```text
You're on the waiting list.
We'll contact you if this opportunity becomes available.
```

Reason:

current promotion eligibility and source conditions may mean raw ordinal position is not equivalent to guaranteed next commitment.

Merchant projections may show deterministic order where operationally useful.

---

# 39. Source Opportunity Assessment

A source wake-up or capacity change does not directly promote a customer.

The source contract SHALL first establish a current:

**Source Opportunity Assessment**

with exactly:

```text
PROMOTABLE

NO_CURRENT_OPPORTUNITY

TARGET_CLOSED

UNRESOLVED
```

---

# 40. `PROMOTABLE`

`PROMOTABLE` means:

> Current authoritative source evidence establishes enough opportunity to consider exactly one Waitlist promotion under the initial portfolio.

It does not mean:

```text
capacity reserved

customer guaranteed commitment

Booking exists

Appointment exists
```

---

# 41. Sequential Promotion

Even where current source capacity may support several customers:

> **The initial Waitlist portfolio promotes one customer at a time per exact Waitlist Target.**

After each promotion reaches an outcome:

```text
source re-evaluates
        ↓
if PROMOTABLE remains
        ↓
consider next customer
```

No batch promotion exists initially.

This deliberately favours correctness and low operational complexity.

---

# 42. Capacity-Change Signals Are Wake Signals

A source event such as:

```text
Booking cancelled

Appointment cancelled

participant withdrew

Allocation released
```

may wake Waitlist coordination.

It does not prove:

```text
capacity now exists
```

Waitlist SHALL re-inspect source authority.

---

# 43. Clock Passage Is Not Opportunity Authority

A timer may wake evaluation.

It cannot establish:

```text
PROMOTABLE
```

merely because:

```text
some timestamp passed
```

MS-PROT-065 owns durable wake/scheduling mechanics.

Source capability owns current opportunity meaning.

---

# 44. Promotion Candidate Assessment

When source opportunity is `PROMOTABLE`, entries SHALL be considered in Queue Precedence order.

Each current entry receives:

**Promotion Candidate Assessment**

with exactly:

```text
ELIGIBLE_NOW

NOT_ELIGIBLE_NOW

NO_LONGER_APPLICABLE

UNRESOLVED
```

The source-qualified contract supplies the business evaluation.

---

# 45. `ELIGIBLE_NOW`

The first entry in Queue Precedence whose assessment is:

```text
ELIGIBLE_NOW
```

is the candidate for the next Waitlist Promotion.

All applicable current execution/security/contact requirements must still succeed.

---

# 46. `NOT_ELIGIBLE_NOW`

An earlier entry that is authoritatively:

```text
NOT_ELIGIBLE_NOW
```

may be bypassed for that exact opportunity.

Its queue precedence is retained.

It MAY become eligible for a later opportunity.

This prevents one temporarily ineligible customer from wasting legitimate capacity without stripping their original place.

---

# 47. `NO_LONGER_APPLICABLE`

Where source authority definitively establishes that an entry can no longer concern the target:

```text
Entry
    → NO_LONGER_APPLICABLE resolution
```

and evaluation may continue to the next current entry.

---

# 48. `UNRESOLVED` Blocks Automatic Bypass

If an earlier-precedence entry is:

```text
UNRESOLVED
```

Main Street SHALL NOT silently skip that entry and favour a later customer.

Automatic promotion stops until:

```text
evidence resolves

or

an accepted handling path
legitimately resolves the entry
```

This is a deliberate fail-closed fairness boundary.

---

# 49. Waitlist Promotion

A **Waitlist Promotion** is:

> A durable Waitlist-owned coordination fact that one exact current Waitlist Entry has been selected for one current source-qualified opportunity.

Conceptually:

```text
WaitlistPromotion
{
    promotionIdentity

    MerchantScope

    targetIdentity

    entryIdentity

    source opportunity provenance

    governing contract affinity

    protection mode

    source protection reference?

    preparedAt

    activatedAt?

    respondBy?

    customer response?

    source handoff provenance?
}
```

---

# 50. At Most One Active Promotion Per Target

Initial invariant:

```text
one Waitlist Target
    ≤
one current active promotion
```

No simultaneous:

```text
"first to click wins"
```

blast to multiple waitlisted customers is permitted.

That would undermine queue precedence and move toward intentional oversubscription.

---

# 51. Promotion Protection Modes

Initial closed vocabulary:

```text
UNPROTECTED

SOURCE_PROTECTED
```

These describe whether the source owner has protected capacity for this promotion.

They do not create Waitlist capacity authority.

---

# 52. `UNPROTECTED`

An unprotected promotion means:

```text
current source opportunity exists

customer is invited to attempt commitment

capacity remains generally contestable

final source operation revalidates
```

Customer communication SHALL NOT claim:

```text
reserved
held
guaranteed
```

---

# 53. `SOURCE_PROTECTED`

`SOURCE_PROTECTED` means:

> The source capability has already established exact accepted source-owned protection for the promoted opportunity.

Waitlist SHALL retain only the exact source protection reference/provenance needed for coordination.

It SHALL NOT own the Hold/Allocation.

---

# 54. Existing Appointment Hold Authority May Be Reused

Where exact Appointment semantics permit, Appointment may use its accepted:

```text
TimeProposal
+
Appointment Proposal Hold
```

authority when promoting a waitlisted exact interval.

That remains Appointment-owned.

MS-PROT-089 does not create a generic Waitlist Hold.

---

# 55. No Silent Protection Fallback

If a Waitlist Opportunity Contract requires:

```text
SOURCE_PROTECTED
```

but source protection cannot be established:

```text
promotion does not silently
become UNPROTECTED
```

The coordination path fails/re-evaluates.

---

# 56. Source Protection Cannot Outlive Its Authority

For a protected promotion:

```text
respondBy
    <=
source protection expiry
```

A Waitlist response deadline cannot extend source-owned capacity protection.

If protection expires first, the source no longer supports a protected promotion.

---

# 57. Promotion Response Profile

Every automatically active Waitlist Opportunity Contract SHALL reference an immutable, versioned:

**Promotion Response Profile**

that deterministically establishes:

```text
response-window rule

promotion activation basis

source/target maximum boundary

no-response consequence

permitted communication contract
```

There is no universal platform:

```text
10 minutes

30 minutes

2 hours

24 hours
```

response period.

---

# 58. No Hidden Deferred Timeout

The exact response rule is mandatory contract/release data.

Therefore:

```text
no Promotion Response Profile
    ↓
automatic customer promotion
is not serviceable
```

This is not a retained semantic DQ.

It is a mandatory operational contract requirement.

---

# 59. Promotion Is Durable Before External Effect

A Promotion SHALL exist durably before Main Street causes the customer-facing external communication effect.

Rejected:

```text
send email
        ↓
then create promotion record
```

This preserves retry and uncertainty safety.

---

# 60. Notification Owns Delivery

Waitlist determines:

```text
why customer must be contacted

which exact Promotion is being offered

what source-qualified facts may be shown
```

Notification owns:

```text
recipient resolution

channel

provider/internal fulfiller

dispatch

retry

delivery evidence
```

No provider status becomes Waitlist business truth directly.

---

# 61. Initial Notification Contract

MS-PROT-089 registers:

```text
capacity-waitlist /
opportunity-offer@1
```

as an operational Notification responsibility.

It is not Marketing.

Waitlist membership does not create marketing permission.

---

# 62. Promotion Activation

A prepared Promotion becomes customer-response-active only after Notification establishes the exact accepted externalisation evidence required by its Promotion Response Profile.

For external provider delivery, that profile MAY use accepted Notification evidence equivalent to:

```text
PROVIDER_ACCEPTED
```

where appropriate.

For an internal delivery path, equivalent accepted internal fulfilment evidence may apply.

Hard distinction remains:

```text
promotion activated
    ≠ customer received
    ≠ customer read
    ≠ customer accepted
```

---

# 63. Response Deadline

Every activated Promotion SHALL have one exact:

```text
respondBy
```

derived deterministically from:

```text
activatedAt

Promotion Response Profile

source target validity boundary

source protection deadline
where applicable
```

The resulting deadline is immutable for that Promotion.

---

# 64. Authoritative Time

Response validity uses Main Street authoritative time.

Rejected:

```text
customer device clock

email client timestamp

merchant browser clock
```

At:

```text
authoritative time >= respondBy
```

a new customer response is too late for that Promotion.

---

# 65. Communication Failure Before Activation

If no accepted externalisation evidence is established:

```text
Promotion does not become active
```

and no response deadline starts.

Waitlist SHALL NOT treat the customer's silence as expiry where the offer never achieved the contract's minimum externalisation threshold.

---

# 66. Known Delivery Failure

Where a Promotion became active but accepted Notification evidence later definitively establishes a failed communication route before a customer response:

Main Street MAY attempt another independently permitted Notification path under MS-PROT-075 where still legitimate.

If no permitted automated route can safely continue:

```text
entry retains its queue position

source protection is safely
released/reconciled as applicable

merchant intervention may be surfaced
```

The customer SHALL NOT be silently removed merely because Main Street's communication infrastructure failed.

---

# 67. Customer Promotion Response

A customer response concerns exactly one Waitlist Promotion.

Initial response vocabulary:

```text
ACCEPT

DECLINE
```

Silence is neither.

---

# 68. Direct Customer Response Authority

A direct customer response requires:

```text
trusted Merchant Scope

exact Waitlist Promotion

exact related Waitlist Entry

exact CustomerContext or
accepted purpose-bound contextual proof

current Promotion

current response deadline
```

A URL parameter or email address alone is insufficient.

---

# 69. Merchant Attestation of Customer Response

An authorised merchant-side actor MAY record an explicit customer response received:

```text
by telephone

in person

through another legitimate
off-platform interaction
```

The operation SHALL preserve:

```text
actor

response

Promotion identity

observed customer-decision time

recorded time

provenance
```

No CustomerAccount is required.

---

# 70. Conversation Reply Is Not Automatic Response Authority

A customer Message:

```text
"Yes, I'll take it"
```

does not automatically execute:

```text
waitlist ACCEPT

Booking

Appointment

Appointment Participation
```

Customer Messaging remains communication truth.

An authorised merchant may record the response, or a future exact accepted messaging-action contract may supply authority.

AI SHALL NOT convert message sentiment into commitment.

---

# 71. Customer Acceptance Does Not Create Commitment

Hard distinction:

```text
Promotion ACCEPTED
    ≠ Booking

Promotion ACCEPTED
    ≠ Appointment

Promotion ACCEPTED
    ≠ Appointment Participation
```

Acceptance means:

> **The customer authoritatively instructed Main Street to attempt the exact offered source commitment.**

---

# 72. Source Commitment Handoff

After valid customer acceptance:

```text
Waitlist customer instruction
        ↓
MS-PROT-072 orchestration
        ↓
exact source-owner operation
        ↓
source performs current
authorisation / eligibility /
capacity / concurrency validation
```

There is no generic:

```text
waitlist.commit()
```

that owns the source business object.

---

# 73. Successful Source Commitment

A Waitlist Entry may be resolved:

```text
COMMITMENT_ESTABLISHED
```

only after exact accepted source evidence proves establishment of:

```text
Booking

Appointment

or
Appointment Participation
```

as applicable.

Provider/Notification/customer-response evidence cannot substitute for this.

---

# 74. Accepted Promotion but Lost Capacity

Scenario:

```text
UNPROTECTED promotion

customer accepts

another legitimate source
commitment consumed capacity first
```

Expected:

```text
Promotion remains historically ACCEPTED

source commitment fails

Waitlist Entry remains active
with original Queue Precedence

no false Booking/Appointment
```

The customer is not moved to the end merely because the unprotected opportunity was lost.

---

# 75. Source Commitment Uncertainty

If source commitment execution becomes uncertain:

```text
do not issue next promotion

do not create another source commitment

do not assume failure

do not assume success
```

MS-PROT-069 reconciliation governs.

The Waitlist target remains blocked from further promotion where advancing could create conflicting commitment.

---

# 76. Definitive Source Rejection

If source authority definitively establishes that the entry is:

```text
NO_LONGER_APPLICABLE
```

the entry may be terminally resolved.

If source evidence merely establishes:

```text
NOT_ELIGIBLE_NOW
```

the entry remains current with original precedence.

Waitlist SHALL NOT invent the difference.

---

# 77. Waitlist Entry Resolution

Initial closed resolution vocabulary:

```text
CUSTOMER_WITHDREW

MERCHANT_REMOVED

CUSTOMER_DECLINED_OPPORTUNITY

OPPORTUNITY_RESPONSE_EXPIRED

COMMITMENT_ESTABLISHED

TARGET_CLOSED

NO_LONGER_APPLICABLE

VOIDED_AS_ERROR
```

Resolution facts are immutable.

The Entry itself is not deleted merely because it stops waiting.

---

# 78. `CUSTOMER_WITHDREW`

The customer explicitly no longer wishes to wait.

A merchant MAY attest this decision where received by phone/in person.

No source commitment is affected merely because Waitlist membership ends.

---

# 79. `MERCHANT_REMOVED`

An authorised merchant actor may explicitly end one current Waitlist Entry.

The removal SHALL be auditable.

It does not rewrite queue history.

Where the customer must be informed under applicable accepted policy, Waitlist establishes the corresponding Notification responsibility.

---

# 80. Merchant Removal Is Not Reordering

A merchant may not silently:

```text
change position 5 to position 1
```

But explicit removal of one entry is a real business operation.

If later re-added:

```text
new Entry
+
new Queue Precedence
```

applies.

---

# 81. `CUSTOMER_DECLINED_OPPORTUNITY`

Where a customer explicitly declines an active Promotion:

```text
Entry resolves
CUSTOMER_DECLINED_OPPORTUNITY
```

The next candidate may then be evaluated against fresh source opportunity.

The customer may later join again as a new Entry where still waitlistable.

---

# 82. `OPPORTUNITY_RESPONSE_EXPIRED`

Where:

```text
Promotion was validly activated

no valid response exists

authoritative time >= respondBy

no known communication failure
requires preservation of the entry
```

the Entry resolves:

```text
OPPORTUNITY_RESPONSE_EXPIRED
```

This does not claim:

```text
customer rejected merchant

customer read message

customer intentionally declined
```

It means only that the bounded offer expired without an accepted response.

---

# 83. `TARGET_CLOSED`

Where source authority establishes that the exact Waitlist Target can no longer produce its intended source commitment:

```text
TARGET_CLOSED
```

resolves all remaining current entries for that target.

Examples:

```text
Appointment opportunity is no longer
temporally valid

shared Appointment was cancelled

Booking reservation opportunity
is no longer applicable
```

Clock passage may wake this evaluation.

Source authority establishes closure.

---

# 84. `VOIDED_AS_ERROR`

An authorised correction may establish that an Entry was created erroneously.

Historical evidence remains.

Voiding does not rewrite the admission event as though it never existed.

---

# 85. Customer Does Not Lose Position After Infrastructure Failure

A current Entry SHALL retain Queue Precedence after:

```text
known Notification failure

unprotected opportunity loss

source technical failure

retriable coordination failure
```

unless another accepted terminal resolution applies.

Main Street infrastructure failure is not customer abandonment.

---

# 86. One Promotion at a Time Prevents Soft Overbooking

Rejected:

```text
one slot opens

send offer to 5 waitlisted customers

take whoever responds first
```

The initial model uses:

```text
one current opportunity
        ↓
one current Promotion
```

This preserves the v1.15 rejection of overbooking and avoids creating a race disguised as marketing outreach.

---

# 87. No Capacity Claim From Queue Position

Even the first Entry owns:

```text
no Resource
no Allocation
no Hold
```

Queue precedence controls only Waitlist promotion selection.

---

# 88. Protected Promotion Preserves Source Ownership

Where a source owner creates a protection object:

```text
Waitlist Promotion
    references protection

source owner
    owns protection semantics

Waitlist
    owns coordination
```

Releasing/expiring that protection remains source-owned.

---

# 89. Target Becomes Available Again

After:

```text
decline

response expiry

failed source commitment

successful commitment with
remaining capacity
```

Main Street SHALL:

```text
re-evaluate source opportunity
```

before considering another customer.

It SHALL NOT reuse the previous capacity assessment.

---

# 90. Customer Already Obtains Commitment Elsewhere

If a waitlisted customer independently receives the equivalent source commitment through another legitimate path:

source authority may determine the Entry:

```text
NO_LONGER_APPLICABLE
```

where its exact contract establishes that result.

Waitlist SHALL NOT infer equivalence from customer identity alone.

---

# 91. Data Protection and Minimisation

Waitlist SHALL retain only what is necessary for:

```text
queued-demand truth

queue precedence

target interpretation

customer relationship

promotion history

response history

source handoff

audit/reconciliation
```

It SHALL NOT duplicate:

```text
whole CustomerContext

whole Conversation

whole Booking/Appointment

marketing profile

customer scoring
```

Retention/disposition remains governed by MS-PROT-053.

---

# 92. Waitlist Is Not Marketing Permission

Hard distinction:

```text
customer joined waitlist
    ≠ marketing consent

waitlist email
    ≠ Campaign

CustomerContext
    ≠ marketing permission
```

Waitlist operational communication SHALL NOT be reused as a pretext for promotional outreach.

---

# 93. Customer Privacy

A customer may observe only:

```text
their own Waitlist Entry

their own active Promotion

source information legitimately
exposable to them
```

Waitlist SHALL NOT expose:

```text
other customers

other CustomerContexts

other email addresses

other customer outcomes

raw queue identities
```

---

# 94. Merchant Exposure

Merchant workers may observe Waitlist information only where their current role/access authority permits the relevant merchant operational context.

Queue assignment does not grant staff authority.

Waitlist does not create a new employee role system.

---

# 95. No Public Exact Queue Size Requirement

Main Street may expose business-safe information such as:

```text
Join waiting list
```

without exposing:

```text
17 people ahead of you

names of waiting customers

exact internal queue ledger
```

Public queue-size exposure is not part of the initial contract.

---

# 96. Merchant Attention Contract

MS-PROT-089 registers one initial Attention source family:

```text
capacity-waitlist /
coordination-intervention-required@1
```

Source owner:

```text
Capacity Waitlist
```

It applies only when a human merchant-side decision/action can legitimately unblock a Waitlist coordination episode.

---

# 97. Attention Applicability

Examples include:

```text
customer cannot be reached
through any serviceable automated
permitted route but merchant may
contact them directly

earlier-precedence entry requires
human-resolvable evidence before
automation can continue

customer response was handled
off-platform and must be recorded
```

It SHALL NOT be created merely for:

```text
provider outage

database outage

generic system error

reconciliation that merchant
cannot legitimately resolve
```

Those remain with Operational Health/Reconciliation.

---

# 98. Attention Handling Satisfaction

The initial satisfaction paths are:

```text
COORDINATION_RESUMED

ENTRY_RESOLVED

HANDLED_OUTSIDE_MAIN_STREET_RECORDED
```

Acknowledgement alone does not satisfy the contract.

No assignment or snooze is required for the initial portfolio.

No universal SLA is created.

---

# 99. Attention Candidate Actions

Where independently authorised, the Attention occurrence MAY expose:

```text
Open Waitlist Entry

Record Customer Response

Withdraw / Remove Entry
```

These remain references to Waitlist-owned operations.

They are not commands or grants of authority by themselves.

---

# 100. No Duplicate Attention Authority

Customer Communication continues to own:

```text
human-response-required
```

for inbound customer Messages.

If a Message says:

```text
"yes, I'll take the slot"
```

Customer Communication may require human handling.

Waitlist Attention SHALL NOT duplicate the Message-handling occurrence merely because the message concerns a Promotion.

After the human records the explicit response through Waitlist:

```text
Customer Communication
    retains Message truth

Waitlist
    owns Promotion response truth
```

---

# 101. AI Boundary

AI MAY:

```text
interpret:
"put me on the cancellation list"

suggest a Waitlist join action

explain target details

summarise an exception

identify likely explicit
accept/decline wording
for human review
```

AI SHALL NOT:

```text
join customer automatically

invent CustomerContext identity

change queue precedence

create VIP priority

skip an unresolved earlier customer

declare capacity available

create a Hold

accept Promotion for customer

infer decline from silence

infer acceptance from sentiment

create source Booking/Appointment

relax capacity because of
predicted no-show
```

---

# 102. Capability Deactivation

Deactivating Capacity Waitlist SHALL prevent:

```text
new ordinary Waitlist Entries
```

according to configuration authority.

It SHALL NOT silently:

```text
delete existing Entries

erase active Promotions

erase customer responses

discard historical precedence

manufacture TARGET_CLOSED
```

Existing active entries require residual management.

---

# 103. Residual Management

Where Waitlist is no longer enabled for new use, Main Street SHALL preserve sufficient residual operation to:

```text
observe existing Entries

process current Promotion response

withdraw/remove Entries

complete already-authorised
source handoff

close remaining target/entries
through legitimate authority
```

until the residual obligations are resolved.

---

# 104. Commercial Downgrade

Loss of Waitlist commercial entitlement SHALL NOT erase existing business truth.

Entitlement may prevent new ordinary joins.

It SHALL NOT turn active Entries into inaccessible orphaned state.

MS-PROT-056 and residual-access authorities remain governing.

---

# 105. Semantic Release Change

Existing Entries preserve:

```text
exact Waitlist Opportunity Contract
family/version affinity
```

under which they were created.

A new release SHALL NOT silently split one live queue into materially different target identities.

Compatibility/migration remains governed by MS-PROT-054.

---

# 106. Idempotency

All authoritative Waitlist operations SHALL comply with MS-PROT-059.

Examples:

```text
same join request retry
    → same Entry

same withdrawal retry
    → same resolution

same customer response retry
    → same response result

same Promotion work retry
    → no duplicate active Promotion
```

Reusing one logical command identity with materially different intent fails.

---

# 107. Concurrency — Duplicate Join

Two concurrent joins from the same:

```text
Merchant Scope
+
CustomerContext
+
Waitlist Target
```

shall establish at most one current Entry.

The other request converges or receives deterministic existing-membership outcome.

---

# 108. Concurrency — Queue Order

Concurrent distinct customer admissions SHALL receive one authoritative total ordering.

No two current entries may be semantically:

```text
equally first
```

for one target.

The implementation mechanism remains downstream.

---

# 109. Concurrency — Promotion

Concurrent wake-ups SHALL NOT create:

```text
two active Promotions
```

for one target.

Active-promotion uniqueness is an authoritative invariant.

---

# 110. Protected Promotion Atomicity

Where Promotion requires source protection:

```text
source protection
+
Promotion preparation
```

must satisfy the required narrow cross-capability consistency/reconciliation invariant.

Rejected outcome:

```text
capacity held indefinitely
but no durable Promotion exists
```

If atomic establishment is not feasible under the accepted source model, durable reconciliation must guarantee convergence.

---

# 111. External Communication Is Not in Database Transaction

Notification provider calls SHALL NOT occur inside the authoritative database transaction merely to make Promotion appear atomic.

Canonical:

```text
durable Promotion
+
durable Notification responsibility
        ↓
commit
        ↓
provider/internal fulfilment
```

---

# 112. Source Commitment Is Independently Authoritative

A successful Waitlist orchestration cannot compensate for a failed source commitment by locally marking:

```text
COMMITMENT_ESTABLISHED
```

Waitlist must consume exact source success evidence.

---

# 113. Falsification — Full Salon Slot

Scenario:

```text
2pm haircut full

Sarah:
"put me on cancellation list"
```

Expected:

```text
source:
WAITLISTABLE

CustomerContext established/resolved

Waitlist Entry created

no Appointment
no Hold
```

**PASS**

---

# 114. Falsification — Capacity Is Actually Available

Scenario:

2pm has valid current capacity.

Customer attempts to join waitlist.

Expected:

```text
DIRECT_COMMITMENT_AVAILABLE

no unnecessary Waitlist Entry
```

**PASS**

---

# 115. Falsification — Invalid Appointment Time

Scenario:

Customer requests a time the merchant does not operate and which is not a qualifying capacity-constrained target.

Expected:

```text
NOT_WAITLISTABLE
```

Waitlist is not used to hide invalid Scheduling semantics.

**PASS**

---

# 116. Falsification — Duplicate Join

Customer taps:

```text
Join waiting list
```

twice due network retry.

Expected:

```text
one current Entry
one queue position
```

**PASS**

---

# 117. Falsification — Same Email, Different CustomerContexts

Two independent CustomerContexts share one email address.

Expected:

```text
no identity merge

no entry merge
```

**PASS**

---

# 118. Falsification — Two Concurrent Customers

Sarah and James join simultaneously.

Expected:

```text
both valid Entries

one deterministic total precedence
```

No ambiguous equal-first position.

**PASS**

---

# 119. Falsification — One Slot Opens

Ten customers are waiting.

Expected:

```text
source PROMOTABLE

precedence assessment

one current Promotion only
```

No batch blast.

**PASS**

---

# 120. Falsification — First Customer Declines

Expected:

```text
first Entry resolves
CUSTOMER_DECLINED_OPPORTUNITY

fresh source re-evaluation

next eligible customer
may be promoted
```

**PASS**

---

# 121. Falsification — First Customer Does Not Respond

Promotion was validly activated.

Deadline passes.

Expected:

```text
OPPORTUNITY_RESPONSE_EXPIRED

fresh source re-evaluation

next candidate
```

No claim that customer read or rejected the offer.

**PASS**

---

# 122. Falsification — Earlier Customer Temporarily Ineligible

Entry 1:

```text
NOT_ELIGIBLE_NOW
```

Entry 2:

```text
ELIGIBLE_NOW
```

Expected:

```text
Entry 1 retains precedence

Entry 2 may receive
this current opportunity
```

**PASS**

---

# 123. Falsification — Earlier Customer Unresolved

Entry 1:

```text
UNRESOLVED
```

Entry 2:

```text
ELIGIBLE_NOW
```

Expected:

```text
automatic promotion stops

Entry 2 is not silently preferred
```

**PASS**

---

# 124. Falsification — Unprotected Offer Loses Capacity

Sarah is promoted without source protection.

Before Sarah accepts, another legitimate direct Booking consumes capacity.

Sarah accepts.

Expected:

```text
source revalidation fails

no false commitment

Sarah retains original
Waitlist precedence
```

**PASS**

---

# 125. Falsification — Protected Appointment Opportunity

Source Appointment authority establishes a valid proposal-specific Hold.

Expected:

```text
Waitlist references Hold

Waitlist does not own Hold

customer may only be told
"held/reserved"
within source evidence
```

**PASS**

---

# 126. Falsification — Notification Known Failure

Promotion email/SMS route fails under accepted Notification evidence.

No other permitted automated route can proceed.

Expected:

```text
customer not removed

queue precedence retained

protection safely handled

merchant intervention may surface
```

**PASS**

---

# 127. Falsification — Customer Says “Yes” in Chat

Expected:

```text
Conversation Message exists

no automatic Promotion ACCEPT

no Booking / Appointment
```

Human may record the response or customer may use an accepted direct action.

**PASS**

---

# 128. Falsification — Secure Guest Acceptance

Customer has no CustomerAccount but uses an exact purpose-bound contextual proof for the active Promotion.

Expected:

```text
trusted scoped action
may establish ACCEPT
```

No global account required.

**PASS**

---

# 129. Falsification — Telephone Acceptance

Merchant calls Sarah.

Sarah explicitly accepts.

Expected:

```text
authorised merchant actor
records customer response
with provenance

source commitment attempted
```

**PASS**

---

# 130. Falsification — Commitment Outcome Uncertain

Customer accepts.

Source operation times out after possible commit.

Expected:

```text
no next Promotion

no duplicate source operation

MS-PROT-069 reconciliation
```

**PASS**

---

# 131. Falsification — Direct Customer Books Outside Waitlist

Merchant/source legitimately accepts another direct customer while an unprotected Waitlist exists.

Expected:

```text
source commitment remains valid

Waitlist does not veto it
```

Waitlist is not a global commitment gatekeeper.

**PASS**

---

# 132. Falsification — Shared Appointment Rescheduled

Customers are waiting for:

```text
Friday 18:00 class
```

Merchant validly reschedules it to:

```text
Saturday 09:00
```

Expected:

```text
Friday target does not silently
become Saturday target

old target becomes non-applicable
according to source authority
```

**PASS**

---

# 133. Falsification — Target Closes

Appointment is cancelled entirely.

Expected:

```text
source TARGET_CLOSED

all current Entries resolve

no Promotion manufactured
```

**PASS**

---

# 134. Falsification — Customer Withdraws

Sarah says:

```text
"take me off the list"
```

Expected:

```text
CUSTOMER_WITHDREW

queue order of remaining
entries closes naturally
```

**PASS**

---

# 135. Falsification — Merchant Wants VIP Priority

Merchant says:

```text
"Put my best customers first."
```

Expected:

```text
not supported
```

No spend/VIP priority field is created.

**PASS**

---

# 136. Falsification — AI Predicts No-Show

AI estimates existing booked customer has 80% chance of no-show.

Expected:

```text
no new source opportunity

no Waitlist Promotion

no capacity relaxation
```

**PASS**

---

# 137. Falsification — Product Restock Request

Customer wants:

```text
"tell me when this product
comes back in stock"
```

Expected:

```text
outside initial
MS-PROT-089 target portfolio
```

It may justify a future Inventory-specific admission.

Waitlist does not silently expand.

**PASS**

---

# 138. Falsification — Low-Software-Capacity Merchant

Merchant says:

```text
"If anyone cancels,
offer it to the next person."
```

Expected normal operation:

```text
customer joins

Main Street remembers order

capacity opens

Main Street safely contacts
the next eligible customer

merchant sees only exceptions
```

Merchant does not configure:

```text
queue algorithms

capacity listeners

event subscriptions

retry policies

provider mappings

idempotency keys
```

**PASS**

---

# 139. Falsification — ERP Drift

Implementation proposes:

```text
custom queue types

drag-and-drop ordering

priority scoring

workflow stages

lead nurturing

campaign automation

multi-step task routing
```

Expected:

```text
outside MS-PROT-089 authority
```

**PASS**

---

# 140. Ownership Review

| Truth | Owner |
|---|---|
| Waitlist Target grouping | Capacity Waitlist |
| Waitlist Entry | Capacity Waitlist |
| Queue Precedence | Capacity Waitlist |
| Promotion coordination | Capacity Waitlist |
| Customer Promotion Response | Capacity Waitlist |
| Booking | Booking |
| Appointment | Appointment |
| Appointment Participation | Appointment |
| Appointment schedulability | Scheduling |
| Capacity / Allocation | Resource / Allocation |
| CustomerContext | Customer relationship authority |
| Trusted execution context | Authentication / trusted-context authority |
| Notification delivery | Notification |
| Conversation / Message | Customer Communication |
| Merchant handling | Merchant Attention |
| Payment / refund | Payment |
| Operational failure | applicable Operational Health owner |
| Execution uncertainty | Reconciliation |
| Overbooking | **None — rejected initial capability** |

**PASS**

---

# 141. Anti-ERP Review

The design deliberately has:

```text
one bounded queue purpose

three exact source target families

one FIFO precedence policy

one active Promotion per target

no merchant workflow configuration

no priority engine

no generic queue abstraction
exposed as a product module
```

This is the minimum sufficient capability.

**PASS**

---

# 142. Hard Invariants

```text
INV-089-001
Waitlist owns queued unmet demand, not eventual source commitment.

INV-089-002
Waitlist Entry is not Booking, Appointment, Appointment Participation, Hold or Allocation.

INV-089-003
Every current Waitlist Entry relates to one exact CustomerContext and one exact Waitlist Target.

INV-089-004
Browsing, AI inference, Conversation existence or contact similarity cannot create Waitlist membership.

INV-089-005
Initial membership requires direct customer action or authorised merchant attestation of customer request.

INV-089-006
A CustomerAccount is not required.

INV-089-007
Same contact information does not merge CustomerContexts or Entries.

INV-089-008
At most one current Entry exists per CustomerContext × Waitlist Target.

INV-089-009
The initial target portfolio contains only exact Appointment interval, exact shared-Appointment participation and exact Booking reservation contracts.

INV-089-010
Join admission requires current source-owner-qualified Waitlist Admission Assessment.

INV-089-011
Projection/display availability cannot authorise Waitlist admission.

INV-089-012
Queue Precedence is FIFO by authoritative admission order.

INV-089-013
Client timestamps and merchant backdating cannot create earlier precedence.

INV-089-014
No VIP, spend, AI, manual numeric or demographic priority exists.

INV-089-015
No manual reorder operation exists.

INV-089-016
Queue precedence governs Waitlist promotion, not all source commitments globally.

INV-089-017
A source wake signal cannot manufacture opportunity.

INV-089-018
Source opportunity must be re-established from current owner-qualified evidence.

INV-089-019
Only one current Promotion exists per exact Waitlist Target in the initial portfolio.

INV-089-020
An earlier NOT_ELIGIBLE_NOW Entry may be bypassed for that opportunity without losing precedence.

INV-089-021
An earlier UNRESOLVED Entry cannot be silently bypassed by automation.

INV-089-022
Waitlist membership never reserves capacity.

INV-089-023
Waitlist protection mode is either UNPROTECTED or SOURCE_PROTECTED.

INV-089-024
SOURCE_PROTECTED requires exact source-owned protection evidence.

INV-089-025
Waitlist cannot manufacture, extend or release source protection authority.

INV-089-026
No silent SOURCE_PROTECTED → UNPROTECTED downgrade is permitted.

INV-089-027
Every automated Promotion has an exact version-affined Promotion Response Profile.

INV-089-028
No universal numeric Promotion response timeout is introduced.

INV-089-029
Promotion exists durably before external communication effect.

INV-089-030
Notification owns delivery evidence.

INV-089-031
Promotion externalisation does not prove recipient receipt, read or acceptance.

INV-089-032
Customer response at or after respondBy is invalid for that Promotion.

INV-089-033
A Conversation Message does not automatically create a Promotion response or source commitment.

INV-089-034
Promotion ACCEPT means instruction to attempt source commitment; it does not itself establish commitment.

INV-089-035
Only source-owned success evidence may establish COMMITMENT_ESTABLISHED.

INV-089-036
Lost unprotected capacity after customer acceptance does not fabricate commitment or strip the Entry's original precedence.

INV-089-037
Source execution uncertainty blocks conflicting further promotion until reconciled.

INV-089-038
Known Main Street communication failure does not automatically remove the customer from the queue.

INV-089-039
Customer decline and response expiry are semantically distinct.

INV-089-040
TARGET_CLOSED requires source authority.

INV-089-041
Entry resolution preserves historical admission/precedence evidence.

INV-089-042
Waitlist membership does not create marketing permission.

INV-089-043
Customers cannot observe other customers' Waitlist identities or outcomes.

INV-089-044
AI cannot create membership, priority, availability, response, Hold or source commitment authority.

INV-089-045
Capability deactivation/downgrade cannot erase active residual Waitlist truth.

INV-089-046
Retries are idempotent and cannot duplicate Entries, Promotions or source commitments.

INV-089-047
Concurrent promotion cannot produce more than one current active Promotion per target.

INV-089-048
Waitlist does not permit or disguise overbooking.

INV-089-049
Product/inventory restock, broad-preference and generic queue semantics are outside the initial target portfolio.

INV-089-050
No implementation activation is granted by this authority.
```

---

# 143. Rejected Alternatives

Rejected:

```text
Waitlist owned by Booking

Waitlist owned by Appointment

Waitlist represented as Enquiry

Waitlist represented as Merchant Attention

Waitlist represented as Conversation

one generic Queue primitive
used across Main Street

customer joins from browsing automatically

AI-created queue membership

same email deduplication

priority score

VIP queue

merchant drag-and-drop ordering

first-to-click batch offers

waitlist position reserves capacity

waitlist creates Hold

waitlist trusts stale availability

capacity release event
automatically means capacity available

universal 30-minute timeout

message saying "yes"
automatically creates Booking

waitlist acceptance
equals Appointment

notification delivery
equals customer acceptance

move customer to back
after Main Street infrastructure failure

waitlist as marketing audience

product restock silently added

overbooking through waitlist

capacity inflation
as hidden overbooking
```

---

# 144. No Retained Semantic DQ

The initial MS-PROT-089 portfolio resolves:

```text
Waitlist ownership

exact initial source scope

entry identity

CustomerContext relationship

guest/customer access boundary

admission

deduplication

precedence

fairness/bypass semantics

opportunity evaluation

promotion concurrency

capacity protection boundary

response deadline authority

customer response authority

Notification boundary

Conversation boundary

source commitment handoff

failure/uncertainty behaviour

entry resolution

Merchant Attention integration

AI boundary

privacy

residual lifecycle

overbooking boundary
```

No retained semantic DQ remains for the defined initial portfolio.

Future additions such as:

```text
broad time-window preferences

product-restock waitlists

series-wide waitlists

priority classes

batch offers

inventory backorders
```

are new Feature Admission work, not deferred incompleteness in MS-PROT-089 v1.0.

---

# 145. Implementation-Rules Impact

**Implementation activation: NONE**

Acceptance establishes semantic authority only.

A future conforming implementation must preserve:

```text
target/contract version affinity

CustomerContext ownership

authoritative precedence

duplicate prevention

current source evaluation

one-active-Promotion invariant

source-owned capacity protection

durable-before-external-effect ordering

Notification separation

purpose-bound customer execution

idempotency

source commitment revalidation

uncertainty reconciliation

residual operation

tenant isolation

auditability
```

Exact:

```text
database tables

locks

queue indexes

background worker technology

API routes

secure-link encoding

provider choice

UI components
```

remain downstream implementation work.

---

# 146. Sequencing Consequence

With this accepted authority formalised:

```text
MS-PROT-089
    → DESIGN-CLOSED
```

for its initial semantic portfolio.

The special MS-PROT-042 grouped-cleanup sequence ends completely.

Next design work SHALL be selected afresh from the current global:

```text
SEQUENCE.md
```

dependency frontier under:

```text
designs/DESIGN-RULES.md
```

No next protocol number is inferred mechanically.

---

# 147. Recommendation

**RECOMMENDATION: ACCEPT**

MS-PROT-089 is the minimum sufficiently expressive model for the admitted capacity-waitlist need.

It achieves:

```text
real unmet-demand representation

FIFO operational fairness

automatic administrative compression

safe source revalidation

customer-account optionality

source-owned capacity and commitment

one controlled promotion at a time

failure honesty

exception-driven merchant handling
```

without introducing:

```text
CRM

generic queues

workflow software

priority optimisation

marketing automation

or overbooking
```

It passes:

```text
Feature Admission

Fundamental Vision Conformance

ownership review

customer-access review

capacity/concurrency review

Notification/Communication review

Merchant Attention review

AI-boundary review

data-minimisation review

anti-ERP review

falsification
```

**Implementation activation: NONE.**
