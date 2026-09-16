# MS-PROT-087 v1.3 — Direct Outreach Safety & Automated Campaign Execution Amendment

**Document ID:** MS-PROT-087  
**Version:** 1.3  
**Status:** **ACCEPTED by explicit manual approval on 9 September 2026**  
**Approved:** Explicit manual approval on 9 September 2026 after grouped MKT-GRP-01 redesign, Fundamental Vision Conformance, ownership review, jurisdiction-difference review, automation-authority review, temporal/recurrence review, Notification boundary review, provider-uncertainty review, identity/deduplication review, anti-ERP review and combined falsification  
**Grouped work package:** `MKT-GRP-01 — Direct Outreach Safety & Automation`  
**Authority type:** Marketing Campaign permission, suppression, contact-policy and bounded-automation semantic amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-087 through v1.2  
**Replaces:** The earlier unapproved chat-only MS-PROT-087 v1.3 permission/contact-policy proposal; that proposal never became repository authority  
**Depends on:** composite MS-PROT-026; composite MS-PROT-027; composite MS-PROT-040; composite MS-PROT-043; composite MS-PROT-046; composite MS-PROT-048; composite MS-PROT-053; MS-PROT-056; composite MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-065; MS-PROT-067; composite MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-073; composite MS-PROT-075 through v1.2; MS-PROT-082; MS-PROT-083; composite MS-PROT-085; composite MS-PROT-086; MS-PROT-088  
**Resolves:** `MS-PROT-087-DQ-003 — Marketing Permission, Suppression & Contact Policy` and `MS-PROT-087-DQ-004 — Automated Campaign Trigger & Recurrence Portfolio`  
**Implementation activation:** NONE

---

# 1. Governing Decision

Main Street SHALL support direct Marketing execution only where:

```text
merchant Marketing intent
        +
approved exact Campaign meaning
        +
registered Audience Definition
        +
current candidate relationship
        +
current jurisdiction-qualified
contact permission
        +
current suppression state
        +
current endpoint/serviceability
        +
current contact-pressure eligibility
        +
current campaign/source validity
        ↓
ELIGIBLE
```

Automated Marketing adds a second independent authority:

```text
current Automated Campaign Contract
        +
eligible exact trigger/recurrence
        ↓
candidate Campaign Occurrence
```

These authorities SHALL remain separate.

Canonical:

```text
standing automation authority
        ≠
standing permission to contact
```

Therefore every automated occurrence and every recipient externalisation must re-establish current contact authority.

---

# 2. Core MKT-GRP-01 Composition

The combined authority consists of six semantic layers:

```text
A. Direct Marketing Permission

B. Durable Suppression

C. Contact Policy

D. Standing Automation Authority

E. Trigger / Recurrence / Occurrence

F. Per-Occurrence and
   Pre-Externalisation Revalidation
```

No layer may substitute for another.

---

# 3. Feature Admission

## 3.1 Representation Test — PASS

Main Street must represent differences such as:

```text
customer may receive email now

customer exists but may not
receive Marketing now

customer opted out

applicable jurisdiction does not
require prior permission but
requires other duties

merchant approved one future send

merchant authorised bounded
recurring execution

automation remains active
but one recipient is no longer
contactable
```

These are materially different business facts.

## 3.2 Coordination Test — PASS

The design coordinates:

```text
Marketing
CustomerContext
Data Protection
Regulatory Administration
Notification
Merchant Brand Infrastructure
Campaign approval
Source capabilities
Background Work
Provider fulfilment
Reconciliation
Audit
```

without transferring ownership.

## 3.3 Administrative-Compression Test — PASS

The merchant should not configure:

```text
legal basis
soft opt-in
subscriber type
jurisdiction codes
regulatory rules
provider status maps
queue workers
cron expressions
suppression joins
retry graphs
```

Main Street absorbs these concerns.

## 3.4 ERP-Drift — PASS

The amendment does not create:

```text
CRM journeys
marketing funnels
lead pipelines
customer scoring
generic workflow automation
multi-channel campaign builders
advertising management
```

---

# 4. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

Merchant-facing operation remains:

```text
What do you want to say?

Who is it relevant to?

Send once or keep this
bounded campaign active?

Review and approve.
```

Main Street internally resolves:

```text
permission
jurisdiction
suppression
endpoint
frequency
current audience
schedule
recurrence
duplicates
late execution
provider uncertainty
```

The complexity is justified because Marketing communication is externally irreversible and trust-sensitive.

---

# 5. Initial Outreach Scope

The permission/contact-policy authority established here initially applies only to:

```text
DIRECT_EMAIL_MARKETING_V1
```

The automation authority applies only to the exact initial Campaign/outreach combinations explicitly admitted below.

This amendment does not activate:

```text
SMS marketing
PUSH marketing
WhatsApp marketing
social direct messages
cold prospecting
paid advertising
```

---

# PART I — DIRECT MARKETING PERMISSION

# 6. Ownership

Canonical ownership:

```text
Marketing
    owns:
        Campaign purpose
        Campaign Revision
        Audience Definition
        campaign-specific
        recipient eligibility
        Campaign Occurrence
        campaign-side suppression

Data Protection /
Regulatory Administration
    own:
        jurisdiction-qualified
        communication rules

        protected-data use

        permission evidence

        durable contact suppression

        permitted/prohibited/
        unresolved determination

CustomerContext
    owns:
        merchant/customer relationship

Notification
    owns:
        endpoint/channel resolution
        Notification Preference
        rendering
        Dispatch
        Delivery Evidence

Merchant Brand Infrastructure
    owns:
        authorised sender identity
```

Marketing SHALL NOT create a competing legal, customer, endpoint or provider authority.

---

# 7. Direct Marketing Contact Policy Determination

The Data Protection / Regulatory composition SHALL provide an authoritative:

**DirectMarketingContactPolicyDetermination**

Conceptually:

```text
DirectMarketingContactPolicyDetermination
{
    MerchantScope

    candidate / contact basis

    outreachFamily

    campaignPurpose

    material content classification

    JurisdictionContext

    RegulatoryRuleSetRelease

    applicable recipient /
    subscriber classification

    permissionBasis

    permissionEvidence

    applicable durable suppression

    requiredContentElements

    timingRestrictions

    additionalRestrictions

    outcome
        PERMITTED
        PROHIBITED
        UNRESOLVED

    evaluatedAt

    provenance
}
```

Marketing consumes this determination.

---

# 8. Determination Outcomes

Exactly:

```text
PERMITTED

PROHIBITED

UNRESOLVED
```

Only:

```text
PERMITTED
```

may proceed.

`UNRESOLVED` means evidence required for a material decision cannot be established.

Canonical:

```text
UNRESOLVED
    → fail closed
```

No merchant “send anyway” operation exists.

---

# 9. Jurisdiction-Specific Rule Composition

Main Street SHALL NOT implement one universal Marketing consent rule.

A determination may depend on:

```text
merchant jurisdiction

recipient jurisdiction

recipient/subscriber classification

business/legal structure

channel

campaign purpose

content classification

contact provenance

relationship history

effective rule date
```

MS-PROT-082 remains authoritative for jurisdiction resolution and versioned regulatory knowledge.

---

# 10. Merchant Does Not Choose Legal Basis

Rejected merchant interaction:

```text
Select legal basis:

[Consent]
[Soft opt-in]
[Legitimate interests]
[CAN-SPAM]
```

Expected:

```text
Audience:
Previous customers

        ↓

Main Street determines
which recipients can
actually be contacted
```

---

# 11. Initial Permission-Basis Vocabulary

The initial closed semantic vocabulary is:

```text
AFFIRMATIVE_MARKETING_PERMISSION

QUALIFYING_EXISTING_CUSTOMER_EXCEPTION

PRIOR_PERMISSION_NOT_REQUIRED_BY_APPLICABLE_RULE
```

These are Main Street classifications.

They do not pretend different jurisdictions use identical legal terminology.

---

# 12. Affirmative Marketing Permission

`AFFIRMATIVE_MARKETING_PERMISSION` means sufficient current evidence establishes an affirmative recipient choice permitting the intended Marketing contact under the applicable rules.

Evidence may require:

```text
MerchantScope

recipient/contact basis

EMAIL channel

scope

choice wording

capture mechanism

customer action

effective time

provenance

rule/release affinity
```

---

# 13. Permission Cannot Be Fabricated

Insufficient by themselves:

```text
pre-ticked box

silence

purchase

Booking

Appointment

CustomerContext

CustomerAccount

known email

previous operational contact

absence of unsubscribe

merchant assertion

AI confidence
```

---

# 14. Permission Evidence Sources

Initial possible evidence-source classes:

```text
DIRECT_CUSTOMER_ACTION

AUTHORISED_MERCHANT_ATTESTATION
OF EXPLICIT CUSTOMER CHOICE

ACCEPTED_HISTORICAL_PERMISSION_EVIDENCE
```

Evidence source does not itself establish sufficiency.

The current Regulatory Determination does.

---

# 15. Merchant Attestation

An authorised merchant actor MAY record explicit customer Marketing permission received:

```text
in person

by telephone

through another legitimate
off-platform interaction
```

The record SHALL retain:

```text
actor

CustomerContext/contact basis

permission scope

observedAt

recordedAt

provenance
```

A mutable:

```text
marketingConsent = true
```

flag without evidence is prohibited.

---

# 16. Imported Historical Permission

Historical permission may be consumed only if sufficient evidence establishes:

```text
who

which merchant

which endpoint/channel

which Marketing scope

when

how

what choice was presented

whether it was later withdrawn
```

A spreadsheet field such as:

```text
subscribed = yes
```

is insufficient by itself.

Insufficient evidence yields:

```text
UNRESOLVED
```

---

# 17. Qualifying Existing-Customer Exception

`QUALIFYING_EXISTING_CUSTOMER_EXCEPTION` means the applicable current rule permits Marketing without a separate affirmative permission because an exact existing-customer exception applies.

The regulatory evaluator may require evidence concerning matters such as:

```text
direct collection of contact details

sale / negotiation relationship

merchant identity

similar product/service scope

opportunity to object at collection

opportunity to object
in every communication

absence of later objection
```

Marketing SHALL NOT implement these legal rules independently.

---

# 18. Customer Relationship Alone Is Insufficient

Rejected:

```text
CustomerContext exists
        ↓
existing-customer exception valid
```

and:

```text
Order exists
        ↓
existing-customer exception valid
```

Every material condition of the applicable rule must independently be established.

---

# 19. Missing Historical Evidence

If an exception requires historical evidence that Main Street cannot establish:

```text
UNRESOLVED
```

applies.

Main Street SHALL NOT assume:

```text
"the merchant probably
showed an opt-out"
```

---

# 20. Prior Permission Not Required

`PRIOR_PERMISSION_NOT_REQUIRED_BY_APPLICABLE_RULE` means the current applicable rule permits this exact direct Marketing email without prior recipient permission.

It does not mean:

```text
no suppression

no opt-out

no sender obligations

no identity requirements

no required disclosure

no data-protection duties

unlimited contact
```

---

# 21. Jurisdiction / Classification Uncertainty

If a material:

```text
jurisdiction

subscriber classification

recipient classification

rule release

permission requirement
```

cannot be authoritatively resolved:

```text
outcome = UNRESOLVED
```

No direct email Marketing occurs.

---

# PART II — DURABLE MARKETING SUPPRESSION

# 22. Marketing Contact Suppression

A **Marketing Contact Suppression** is authoritative durable evidence that an otherwise potentially eligible direct Marketing contact SHALL NOT occur within an exact scope.

Initial suppression scopes are:

```text
MERCHANT_EMAIL_ENDPOINT

MERCHANT_CUSTOMER_CONTEXT
```

---

# 23. Endpoint Suppression

`MERCHANT_EMAIL_ENDPOINT` means:

```text
MerchantScope
+
normalised EMAIL endpoint
+
all MS-PROT-087
direct-email Marketing purposes
```

are suppressed.

This protects the actual destination.

---

# 24. CustomerContext Suppression

`MERCHANT_CUSTOMER_CONTEXT` means:

```text
MerchantScope
+
CustomerContext
+
all MS-PROT-087
direct-email Marketing purposes
```

are suppressed.

This represents a customer-level objection/request.

All current and later valid email endpoints used for that CustomerContext are affected until the suppression is validly superseded.

---

# 25. Contact Equality Does Not Merge Customers

If two CustomerContexts share:

```text
family@example.com
```

endpoint suppression may prevent sends to that endpoint.

It does not merge the CustomerContexts.

Canonical:

```text
endpoint suppression
    ≠ customer identity merge
```

---

# 26. Suppression Sources

Initial suppression may arise from:

```text
customer unsubscribe

customer direct objection

authorised merchant recording
of customer withdrawal

provider complaint evidence
after accepted interpretation

applicable regulatory prohibition

other accepted contact-policy rule
```

---

# 27. Suppression Dominates Permission

Canonical:

```text
valid permission basis
        +
applicable current suppression
        ↓
PROHIBITED
```

No permission basis overrides current suppression.

---

# 28. New Business Activity Does Not Clear Suppression

The following do not reactivate Marketing:

```text
new Order

new Booking

new Appointment

new Conversation

new Payment

new CustomerContext activity
```

---

# 29. Existing-Customer Exception Cannot Override Opt-Out

A customer who opted out does not become marketable merely because:

```text
they purchase again
```

or again satisfy an existing-customer exception.

---

# 30. Repermission

A later independently sufficient:

```text
AFFIRMATIVE_MARKETING_PERMISSION
```

MAY supersede a customer-originated suppression where current regulatory authority permits it.

The new choice must be:

```text
explicit

later

scope-compatible

authoritative
```

Historical suppression remains auditable.

---

# 31. Shared-Endpoint Repermission Safety

One CustomerContext's repermission SHALL NOT automatically clear an endpoint suppression where:

```text
the endpoint is shared

or

another unresolved/current
suppression basis still applies
```

The external endpoint remains suppressed until sufficient authority establishes that clearing it is safe.

---

# 32. Unsubscribe Mechanism

Every direct Marketing email SHALL provide a simple customer mechanism to stop future direct Marketing.

Initial product requirements:

```text
no login

no CustomerAccount

no fee

no unrelated personal information

no preference maze

no more than one confirmation/result page
```

---

# 33. Immediate Main Street Effect

Once a valid unsubscribe is accepted:

```text
Marketing Contact Suppression
becomes effective immediately
```

for subsequent Main Street eligibility.

Main Street SHALL NOT intentionally continue sending merely because a jurisdiction permits a longer statutory processing window.

---

# 34. Marketing Opt-Out Does Not Block Operational Communication

Canonical:

```text
Marketing unsubscribe
    ≠ security suppression

    ≠ Order update suppression

    ≠ Appointment update suppression

    ≠ Booking update suppression

    ≠ Payment notice suppression
```

---

# PART III — CONTACT POLICY

# 35. Notification Preference

MS-PROT-075 remains authoritative for:

```text
UNSPECIFIED
PREFERRED
DISABLED
```

For direct Marketing:

```text
DISABLED
    → prevents applicable
      controllable Marketing
      email delivery

PREFERRED
    → cannot create permission

UNSPECIFIED
    → cannot create permission
```

---

# 36. Exact Endpoint Required

A current serviceable email endpoint must be independently established.

The following are insufficient:

```text
historical email

email from old Order

merchant typed email

same email elsewhere

CustomerContext alone
```

---

# 37. Campaign-Occurrence Deduplication

For one Campaign Occurrence:

> One normalised email endpoint may receive at most one logical `DIRECT_EMAIL_MARKETING_V1` communication responsibility.

Multiple CustomerContexts or source relationships SHALL NOT manufacture multiple copies.

---

# 38. Shared Endpoint and Personalisation

Where person-specific recipient identity cannot safely be resolved at an endpoint:

```text
person-specific disclosure
    → prohibited / unresolved
```

The email may proceed only if:

```text
contact authority is sufficient
+
approved content is safe for
the shared/unresolved context
```

---

# 39. Initial Contact-Pressure Portfolio

Initial platform protection limits per:

```text
MerchantScope
×
normalised EMAIL endpoint
```

across all MS-PROT-087 direct Marketing are:

```text
maximum 1
per rolling 24 hours

maximum 2
per rolling 7 days

maximum 6
per rolling 30 days
```

These are Main Street product-protection limits.

They are not representations of statutory limits.

---

# 40. Contact Pressure Is Cross-Campaign

The limits cannot be bypassed by creating several Campaigns.

Canonical:

```text
Campaign A
Campaign B
Campaign C

        ↓

one shared
merchant × endpoint
contact-pressure history
```

---

# 41. External-Effect Counting

A logical Marketing responsibility counts against contact pressure when prior physical effect is sufficiently possible.

Initial qualifying evidence includes:

```text
PROVIDER_ACCEPTED

PROVIDER_REPORTED_DELIVERED

EXECUTION_UNCERTAIN
```

or semantically equivalent accepted evidence.

A definitive:

```text
KNOWN_FAILED_BEFORE_PROVIDER_EFFECT
```

does not consume a contact-pressure occurrence.

---

# 42. No Merchant Frequency Override

Initial product provides no:

```text
ignore limit

VIP exception

campaign-specific bypass

send anyway
```

operation.

---

# 43. Email Quiet-Hours Boundary

The initial direct-email contact policy introduces no universal platform EMAIL quiet-hours prohibition.

A specific jurisdiction may impose one.

A Campaign's own approved schedule remains authoritative.

---

# 44. Required Compliance Content

Immediately before externalisation Main Street SHALL establish all current jurisdiction-required Marketing email content.

This may include, depending on governing rules:

```text
truthful sender identity

merchant identity

non-deceptive subject

advertising identification

physical/postal address

valid opt-out mechanism

other required disclosures
```

---

# 45. Regulatory Content Is Administrative Compression

Where current regulation requires deterministic standard content:

```text
Main Street
    adds/validates it
```

The merchant SHALL NOT repeatedly type compliance boilerplate.

---

# 46. Merchant Brand Sender Identity

Where an authorised Business Email Identity exists:

```text
DIRECT_EMAIL_MARKETING_V1
```

SHOULD use the applicable merchant-branded sender identity.

Sender identity does not create contact permission.

No serviceable authorised sender path:

```text
→ direct-email path
  not serviceable
```

---

# 47. Transactional-Label Bypass

Promotional content cannot evade Marketing rules through labels such as:

```text
receipt

service notice

appointment update

order update

customer service

security notice
```

Classification follows material purpose/content.

---

# 48. Mixed Communication

The initial portfolio SHALL NOT combine direct Marketing content with:

```text
security-critical

transactional

Payment-required

Booking-required

Appointment-required

Order-required
```

communication in one logical message.

They SHALL be separated.

---

# PART IV — STANDING AUTOMATION AUTHORITY

# 49. Automated Campaign Contract

An **Automated Campaign Contract** is merchant-authorised standing authority permitting Main Street to evaluate and, where all current conditions remain valid, establish bounded future Campaign Occurrences without requiring a new manual activation for every occurrence.

Conceptually:

```text
AutomatedCampaignContract
{
    automationContractIdentity

    MerchantScope

    campaignIdentity

    exact Campaign Revision

    Campaign purpose

    Audience Definition version

    permitted outreach families

    automationTriggerFamily

    recurrence semantics
        where applicable

    effectiveFrom

    authorityEndsAt

    recipientRepeatPolicy

    executionWindowProfile

    maximumOccurrenceCount?

    approval identity

    approving actor

    approvedAt

    pause / termination history

    provenance
}
```

---

# 50. Automation Is Not AI Autonomy

Canonical:

```text
merchant approves
bounded deterministic rule
        ↓
Main Street evaluates it
```

not:

```text
AI decides when,
what, who and how
to market
```

---

# 51. Automation Does Not Create Contact Permission

Hard invariant:

```text
Automated Campaign Contract
    ≠ permission

    ≠ endpoint authority

    ≠ suppression override

    ≠ future recipient eligibility
```

Every recipient must still pass current DQ-003 semantics.

---

# 52. Exact Campaign Revision Affinity

Initial automation SHALL bind one exact immutable Campaign Revision.

It SHALL NOT authorise arbitrary future:

```text
AI-generated copy

merchant-edited copy

new offer

new CTA

new audience

new personalisation

new outreach family
```

A materially changed Campaign Revision requires new approval and, where automation is desired, a new Automated Campaign Contract.

---

# 53. No Dynamic Automation Template in Initial Portfolio

Although the base Marketing authority permits future template concepts, the initial automated portfolio deliberately excludes:

```text
dynamic content template
that changes materially
without new merchant approval
```

This keeps standing authority narrow.

Future dynamic-template automation requires normal Feature Admission.

---

# 54. Standing Authority Is Time-Bounded

No initial Automated Campaign Contract may be permanently open-ended.

Every contract SHALL have:

```text
authorityEndsAt
```

The initial maximum standing authority is:

```text
12 calendar months
from approval
```

A merchant may choose an earlier end.

Continued automation after expiry requires fresh approval.

---

# 55. Why Standing Authority Is Bounded

The 12-month ceiling is a Main Street product-protection boundary supporting:

```text
merchant intent freshness

content review

regulatory evolution

business-change review

commercial entitlement review
```

It is not a statutory claim.

---

# 56. Initial Automation Trigger Portfolio

The initial portfolio contains exactly:

```text
SCHEDULED_SINGLE_OCCURRENCE_V1

PERIODIC_AUDIENCE_REEVALUATION_V1
```

No generic source-event trigger is admitted initially.

---

# 57. `SCHEDULED_SINGLE_OCCURRENCE_V1`

This permits one exact approved Campaign Revision to be automatically activated once at a future approved due time.

Applicable initial Campaign purposes:

```text
marketing/merchant-news-awareness@1

marketing/offering-awareness@1

marketing/customer-appreciation@1

marketing/customer-reengagement@1
```

Applicable outreach combinations remain those already permitted by v1.1.

---

# 58. Scheduled Single Occurrence Is Still Automation

The merchant approves:

```text
exact Campaign Revision
+
exact future due time
```

Main Street later executes without another manual click.

Therefore it belongs to the automated portfolio even though it is not recurring.

---

# 59. `PERIODIC_AUDIENCE_REEVALUATION_V1`

This trigger periodically re-evaluates a governed Audience Definition and may establish Campaign Occurrences using one exact approved direct-email Campaign Revision.

Initial permitted purposes are exactly:

```text
marketing/customer-appreciation@1

marketing/customer-reengagement@1
```

Initial outreach family is exactly:

```text
DIRECT_EMAIL_MARKETING_V1
```

---

# 60. Why Periodic Automation Is Narrow

The initial periodic model is intended to express business intent such as:

```text
"Each month,
invite newly eligible
previous customers back."
```

not:

```text
"Keep blasting everyone
indefinitely."
```

---

# 61. Initial Periodic Recurrence Families

Exactly:

```text
WEEKLY_LOCAL@1

MONTHLY_DAY_OF_MONTH_LOCAL@1
```

No:

```text
DAILY recurrence

hourly recurrence

cron expression

arbitrary RRULE

AI-selected timing
```

exists initially.

---

# 62. Weekly Local Recurrence

`WEEKLY_LOCAL@1` requires:

```text
IANA timezone

one weekday

local due time

effective start

authority end
```

The same local business time remains intended across ordinary DST offset changes.

---

# 63. Monthly Local Recurrence

`MONTHLY_DAY_OF_MONTH_LOCAL@1` requires:

```text
IANA timezone

day of month 1..31

local due time

effective start

authority end
```

If the requested day does not exist in a month:

```text
that month has
no occurrence
```

Main Street SHALL NOT silently move it to the month's final day.

---

# 64. No Daily Automated Direct Marketing

The initial portfolio deliberately excludes DAILY recurrence.

This reduces:

```text
contact-pressure risk

merchant-intent drift

spam-like use

accidental over-automation
```

A future daily Marketing automation capability requires Feature Admission.

---

# 65. No Source-Event Trigger Initially

Initial portfolio does not admit:

```text
new Product event
        → send Marketing

Order completed
        → send Marketing

Appointment occurred
        → send Marketing

Inventory changed
        → send Marketing

customer inactive event
        → send Marketing
```

Those are new source-trigger families requiring explicit contract admission.

This also keeps Review solicitation outside MS-PROT-087.

---

# 66. Recipient Repeat Policy

The initial periodic automation portfolio uses exactly:

```text
ONCE_PER_AUTOMATION_CONTRACT
```

per:

```text
CustomerContext
+
normalised email endpoint
```

A recipient successfully externalised through one periodic Automated Campaign Contract SHALL NOT receive the same automated Campaign again from that contract.

---

# 67. Why Recurrence Still Matters

Periodic recurrence does not mean repeated emailing of the same recipient.

It means:

```text
weekly/monthly:
    re-evaluate audience

newly eligible customer
    → may receive once

already externally marketed
through this contract
    → suppressed for this contract
```

This is administrative compression without creating a nurture journey.

---

# 68. Manual Campaigns Remain Independent

`ONCE_PER_AUTOMATION_CONTRACT` does not permanently suppress the customer from all future merchant Marketing.

A separately approved future Campaign may independently evaluate eligibility, subject to:

```text
permission

suppression

contact-pressure limits
```

---

# 69. No Automation Contract Cloning Bypass

A merchant SHALL NOT use trivial duplicate Automated Campaign Contracts solely to defeat the once-per-contract protection.

Materially equivalent concurrent automated contracts targeting the same purpose/audience/content may be rejected or classified as conflicting under Marketing policy.

The exact semantic equivalence must be deterministic.

AI similarity is insufficient.

---

# PART V — TRIGGER, OCCURRENCE & TIME SEMANTICS

# 70. Automation Schedule Identity

Each logical automation due point SHALL have stable:

**Automation Schedule Occurrence Identity**

derived from:

```text
AutomatedCampaignContract
+
logical recurrence position
```

One logical due point may establish at most one Campaign Occurrence.

---

# 71. Time Does Not Create Campaign Authority

Clock passage may make a contract due.

It does not itself create external communication authority.

Canonical:

```text
due schedule
        ↓
Automation Due Assessment
        ↓
if executable
        ↓
Campaign Occurrence
        ↓
current recipient evaluation
```

---

# 72. Automation Due Assessment

At each due point Main Street SHALL determine:

```text
EXECUTABLE

NOT_EXECUTABLE

UNRESOLVED

MISSED_EXECUTION_WINDOW
```

before establishing or executing the Campaign Occurrence.

---

# 73. Due-Point Revalidation

At minimum:

```text
contract currently active

contract within effective interval

merchant entitlement

Campaign exists

exact Campaign Revision current
for this automation

Campaign not cancelled

approval still valid

Audience Definition available

outreach family still supported

semantic release compatibility

required provider/serviceability
```

must be evaluated.

---

# 74. Execution Window Profile

Every Automated Campaign Contract SHALL bind an exact immutable:

```text
AutomationExecutionWindowProfile
```

which defines how late after the nominal due time execution may begin.

There is no hidden universal execution window.

---

# 75. Execution Window Is Mandatory Release Data

If no valid Automation Execution Window Profile is available:

```text
automatic execution
    = DISABLED
```

This is a closed semantic requirement, not an unresolved DQ.

Concrete release/profile values remain configuration/product authority.

---

# 76. No Silent Catch-Up

If the execution window has closed:

```text
MISSED_EXECUTION_WINDOW
```

applies.

Main Street SHALL NOT automatically send the Campaign late.

---

# 77. No Backfill Blast

Scenario:

```text
weekly automation

system unavailable
for three weeks

system recovers
```

Rejected:

```text
send three missed Campaigns
```

Default:

```text
missed due points remain missed

evaluate next valid
future recurrence
```

---

# 78. Scheduler Delay Inside Window

If Main Street wakes late but still inside the exact execution window:

```text
the same logical occurrence
may execute once
```

Retries converge on its existing occurrence identity.

---

# 79. DST Semantics

Local recurrence SHALL preserve local civil-time meaning across ordinary offset changes.

If a configured local time is:

```text
nonexistent
or
ambiguous
```

under governing timezone rules and no deterministic resolution profile applies:

```text
UNRESOLVED
```

No guessed time is used.

---

# 80. Campaign Occurrence Creation

A Campaign Occurrence SHALL record:

```text
Campaign

Campaign Revision

Automation Contract
where automated

logical schedule occurrence

Audience Definition version

intended outreach

trigger/due evidence

merchant authority provenance
```

A Campaign Occurrence does not itself prove any recipient was contacted.

---

# 81. Duplicate Trigger Convergence

Repeated worker wakes, queue retries or duplicate schedule signals for one logical due point SHALL converge on:

```text
one Campaign Occurrence
```

No duplicate Campaign Occurrence is created.

---

# 82. Partial Recipient Externalisation

One Campaign Occurrence may legitimately result in:

```text
some recipients externalised

some INELIGIBLE

some UNRESOLVED

some suppressed

some endpoint failures
```

The occurrence is not transactionally all-or-nothing across recipients.

Already executed external effects cannot be rolled back.

---

# PART VI — PAUSE, RESUME, TERMINATION & REVISION

# 83. Automation Contract Operational State

The current operational state vocabulary is:

```text
ACTIVE

PAUSED

TERMINATED

EXPIRED
```

This is Marketing automation state.

It is not Campaign lifecycle truth generally.

---

# 84. `ACTIVE`

Future eligible due points may be evaluated.

It does not mean they will execute.

---

# 85. `PAUSED`

No new automated Campaign Occurrence may be established while paused.

Existing externally executed communication remains historical truth.

---

# 86. Pause Is Prospective

If a trigger/due point occurs while paused:

```text
no Campaign Occurrence
```

is created from that paused due point.

---

# 87. Resume Has No Replay

Resuming:

```text
PAUSED → ACTIVE
```

permits only future eligible due points.

Paused due points are not replayed.

---

# 88. `TERMINATED`

Termination permanently ends standing automation authority.

No future occurrence may be created.

A terminated contract cannot be resumed.

A new merchant approval/contract is required.

---

# 89. `EXPIRED`

At:

```text
authoritative time
>= authorityEndsAt
```

the contract is expired.

No future occurrence may be created.

---

# 90. Campaign Cancellation

If the underlying Campaign is validly cancelled:

```text
Automated Campaign Contract
cannot create future occurrences
```

Historical occurrence evidence remains.

---

# 91. Material Campaign Revision

Changing externally material Campaign meaning does not mutate the automated contract.

Canonical:

```text
Campaign Revision 4
bound to Automation A

merchant creates Revision 5
        ↓
Automation A
remains bound to Revision 4

Revision 5
requires new approval /
automation authority
```

---

# 92. Unsafe Old Revision

If Revision 4 becomes invalid because an authoritative source fact it materially communicates is no longer current:

```text
future execution fails closed
```

Main Street SHALL NOT silently rewrite it into Revision 5.

---

# 93. Commercial Entitlement Loss

If the merchant loses entitlement to automated Marketing:

```text
no new automated occurrence
```

Historical Campaign and contract evidence remain.

Residual merchant access follows existing commercial/residual authorities.

---

# 94. Capability Deactivation

Disabling Marketing or the applicable automation capability:

```text
prevents new automated occurrence
```

without deleting:

```text
Campaigns

Approvals

Contracts

Occurrences

Delivery evidence

Suppression
```

---

# PART VII — PER-OCCURRENCE & PRE-EXTERNALISATION REVALIDATION

# 95. Audience Is Re-Evaluated Every Occurrence

Standing automation SHALL NOT snapshot recipient membership permanently.

At each occurrence:

```text
current Audience Definition
evaluation
```

is required.

---

# 96. Candidate Membership Does Not Reserve Future Contact

Example:

```text
January:
Sarah qualifies

February:
Sarah no longer qualifies
```

A February occurrence cannot use January's candidate result.

---

# 97. Permission Revalidation

Each candidate requires a current:

```text
DirectMarketingContactPolicyDetermination
```

for the exact occurrence/outreach.

Historical permission assessment is not sufficient.

---

# 98. Suppression Revalidation

Current durable suppression SHALL be evaluated for every externalisation attempt.

---

# 99. Contact-Pressure Revalidation

Current rolling contact-pressure limits SHALL be evaluated for every candidate.

If blocked:

```text
recipient is INELIGIBLE
for this occurrence
```

The blocked automated message SHALL NOT be queued for future catch-up.

---

# 100. Endpoint Revalidation

Current email endpoint serviceability and current Notification Preference SHALL be re-established.

---

# 101. Source-Business Currentness

Any material source claim in the exact Campaign Revision must remain authoritatively valid.

Example:

```text
Campaign says:
"Our winter service
is now £50"
```

If authoritative price becomes £60:

```text
old approved Campaign
must not silently send
```

---

# 102. Provider Readiness

Provider/fulfilment readiness SHALL be current.

Provider readiness alone does not make the recipient eligible.

---

# 103. Externalisation Admission Boundary

Current Marketing eligibility must still govern at the Notification external-effect admission boundary.

A previously-created Notification Intent SHALL NOT be externally dispatched if Marketing applicability has since become invalid.

Canonical opt-out race:

```text
NotificationIntent created

customer unsubscribes

provider call not yet made
        ↓

current Marketing applicability
fails
        ↓

no provider effect
```

---

# 104. Notification Owns Dispatch

Marketing does not execute provider email.

It supplies current:

```text
why
recipient eligibility
source content
applicability
```

Notification retains:

```text
Dispatch
Attempt
Retry
Delivery Evidence
```

---

# 105. Unsubscribe During Campaign Processing

A recipient who unsubscribes while a large Campaign Occurrence is still processing SHALL be suppressed for any not-yet-externalised Marketing effect.

Previously externalised email remains historical.

---

# 106. No Retry Around New Suppression

A Notification retry remains permissible only where current Marketing eligibility remains valid.

Retry authority cannot bypass a newly created suppression.

---

# PART VIII — FAILURE, UNCERTAINTY & IDEMPOTENCY

# 107. Idempotency

Automated Marketing operations SHALL comply with MS-PROT-059.

Same logical recurrence retry:

```text
→ same occurrence
```

Same recipient responsibility retry:

```text
→ same logical Notification responsibility
```

---

# 108. Provider Uncertainty

If provider effect may have occurred:

```text
EXECUTION_UNCERTAIN
```

applies under Notification/Reconciliation authority.

Marketing SHALL NOT generate another physical send merely because:

```text
worker retried

next automation tick occurred

merchant refreshed page
```

---

# 109. Uncertain Effect Counts Toward Contact Pressure

An uncertain prior Marketing effect counts toward contact-pressure policy.

This prevents uncertainty becoming a resend loophole.

---

# 110. Known Pre-Effect Failure

Where provider execution definitively failed before any external effect:

```text
contact-pressure count
is not consumed
```

Retry remains subject to current:

```text
permission

suppression

endpoint

campaign validity

execution window

Notification retry profile
```

---

# 111. Occurrence Failure Is Not Standing Contract Failure

One failed Campaign Occurrence does not automatically terminate the Automated Campaign Contract.

The next future due point may be considered if the contract remains valid.

---

# 112. Structural Failure

A condition such as:

```text
Campaign Revision permanently invalid

Audience Definition unavailable
under current semantic release

contract expired

Campaign terminated
```

may prevent future occurrences until a new valid authority exists.

No repeated futile sends are attempted.

---

# 113. Merchant Attention

Automation failure MAY create Merchant Attention only where:

```text
a real merchant decision
can resolve the condition
```

Generic provider outage or infrastructure failure remains with operational health/reconciliation.

---

# PART IX — AI BOUNDARY

# 114. AI May Assist

AI MAY:

```text
draft Campaign content

suggest registered
Audience Definitions

explain recipient exclusions

explain permission outcomes

suggest one of the admitted
automation families

summarise automation results

highlight missing evidence
```

---

# 115. AI Cannot Create Authority

AI SHALL NOT:

```text
invent permission

clear suppression

choose legal basis

override regulatory result

override contact limits

create a new Audience Definition

approve a Campaign

create standing automation authority

change recurrence

resume terminated automation

create dynamic Campaign content
at send time

select unregistered triggers

send email
```

---

# 116. AI Cannot Expand Standing Scope

Example:

```text
merchant approved:
monthly re-engagement

AI decides:
weekly would perform better
```

Expected:

```text
no change
```

A material automation change requires merchant authority.

---

# PART X — MERCHANT EXPERIENCE

# 117. Manual Future Send

Merchant experience:

```text
Campaign

Send:
Friday at 10:00

Audience:
Previous customers

        ↓

Main Street:
312 customers can currently
be considered

Final eligibility will be
checked when sending.

[Schedule]
```

The merchant does not configure jobs or legal rules.

---

# 118. Recurring Re-Engagement

Merchant interaction may be:

```text
Keep this campaign active?

Every:
[Week]
[Month]

Until:
[date]

Each customer receives
this campaign at most once.

[Review & approve]
```

Technical concepts remain hidden.

---

# 119. Recipient Explanation

Merchant may see aggregated outcomes such as:

```text
427 matched audience

312 eligible

73 unsubscribed /
contact restricted

29 frequency-limited

13 unresolved
```

without exposing unnecessary legal or identity internals.

---

# 120. No Consent Dashboard Requirement

The ordinary merchant experience SHALL NOT require an enterprise-style:

```text
Consent Management
dashboard

Legal Basis Matrix

Subscriber Type editor

Suppression SQL view
```

Main Street exposes only actionable exceptions where needed.

---

# PART XI — FALSIFICATION

# 121. Existing Customer but No Sufficient Permission Evidence

Expected:

```text
UNRESOLVED / PROHIBITED
no send
```

**PASS**

---

# 122. Qualifying Existing-Customer Exception

Every applicable condition established.

Expected:

```text
PERMITTED
subject to remaining checks
```

**PASS**

---

# 123. Prior Permission Not Required

Jurisdiction permits contact without prior permission.

Recipient not suppressed; required duties satisfied.

Expected:

```text
PERMITTED
```

**PASS**

---

# 124. Prior Permission Not Required but Recipient Unsubscribed

Expected:

```text
PROHIBITED
```

**PASS**

---

# 125. Merchant Says “They Bought Before”

Required historical opt-out/collection evidence absent.

Expected:

```text
UNRESOLVED
```

**PASS**

---

# 126. Customer Unsubscribes Before Scheduled Send

Campaign scheduled earlier.

Before provider effect:

```text
suppression becomes current
```

Expected:

```text
no Marketing email
```

**PASS**

---

# 127. Customer Purchases Again After Unsubscribe

Expected:

```text
suppression remains
```

**PASS**

---

# 128. Valid Explicit Repermission

Expected:

```text
suppression may be superseded
only under current
regulatory authority
```

**PASS**

---

# 129. Shared Endpoint

Two CustomerContexts → one email.

Expected:

```text
at most one logical email
per occurrence

no identity merge
```

**PASS**

---

# 130. Contact-Pressure Bypass Attempt

Merchant creates several Campaigns.

Expected:

```text
merchant × endpoint
limits still apply
```

**PASS**

---

# 131. Single Scheduled Campaign

Merchant approves campaign for Friday 10:00.

Main Street wakes 10:03 within accepted execution window.

Expected:

```text
one occurrence
```

**PASS**

---

# 132. Duplicate Scheduler Wake

Same due point processed twice.

Expected:

```text
one Campaign Occurrence
```

**PASS**

---

# 133. Scheduler Wakes After Window

Expected:

```text
MISSED_EXECUTION_WINDOW

no late Marketing send
```

**PASS**

---

# 134. Three Missed Weekly Occurrences

System later recovers.

Expected:

```text
no three-email catch-up
```

**PASS**

---

# 135. Weekly Re-Engagement

Sarah is newly eligible in week 2.

Expected:

```text
Sarah may receive once
```

Sarah remains eligible week 3.

Expected:

```text
ONCE_PER_AUTOMATION_CONTRACT
prevents repeat
```

**PASS**

---

# 136. New Customer Becomes Eligible Later

James was not eligible when automation was approved.

He later qualifies.

Expected:

```text
current audience evaluation
may admit James

current permission still required
```

**PASS**

---

# 137. Customer Loses Permission While Automation Active

Expected:

```text
automation remains active

customer no longer externalised
```

**PASS**

---

# 138. Campaign Content Becomes Stale

Campaign says £50.

Authoritative price now £60.

Expected:

```text
no silent rewrite
no send of stale claim
```

**PASS**

---

# 139. Merchant Edits Campaign

Revision 5 created.

Automation bound to Revision 4.

Expected:

```text
automation does not silently
switch to Revision 5
```

**PASS**

---

# 140. Paused Campaign

Trigger becomes due while paused.

Expected:

```text
no occurrence
```

**PASS**

---

# 141. Resume

Expected:

```text
future due points only
no replay
```

**PASS**

---

# 142. Terminated Automation

Merchant later clicks resume.

Expected:

```text
rejected

new approval required
```

**PASS**

---

# 143. 12-Month Authority Expiry

Expected:

```text
no future occurrences
without reapproval
```

**PASS**

---

# 144. AI Changes Monthly to Weekly

Expected:

```text
no authority
```

**PASS**

---

# 145. AI Generates New Offer at Send Time

Expected:

```text
no externalisation
```

**PASS**

---

# 146. Customer Opts Out After NotificationIntent Creation

Provider effect has not occurred.

Expected:

```text
dispatch blocked by
current Marketing applicability
```

**PASS**

---

# 147. Provider Execution Uncertain

Expected:

```text
no blind duplicate

counts toward
contact-pressure policy

reconciliation applies
```

**PASS**

---

# 148. Known Pre-Effect Provider Failure

Expected:

```text
no contact-pressure consumption

retry only if every
current condition remains valid
```

**PASS**

---

# 149. Merchant Tries Daily Automation

Expected:

```text
unsupported initial portfolio
```

**PASS**

---

# 150. Merchant Tries Arbitrary Event Trigger

Example:

```text
"Whenever someone
buys anything,
email them a promotion."
```

Expected:

```text
unsupported
```

No generic trigger DSL.

**PASS**

---

# 151. Marketing Disguised as Operational Message

Expected:

```text
Marketing authority still applies
```

**PASS**

---

# 152. Tracking Pixel Proposed

Expected:

```text
outside current portfolio
```

**PASS**

---

# 153. Low-Software-Capacity Merchant

Merchant says:

```text
"Every month,
invite old customers back,
but don't keep emailing
the same people."
```

Main Street translates to:

```text
customer-reengagement

PERIODIC_AUDIENCE_REEVALUATION_V1

MONTHLY_DAY_OF_MONTH_LOCAL@1

ONCE_PER_AUTOMATION_CONTRACT

current permission +
suppression checks
```

without exposing those terms.

**PASS**

---

# PART XII — HARD INVARIANTS

## Permission / suppression

```text
INV-087-V13-PERM-001
Audience membership never establishes Marketing permission.

INV-087-V13-PERM-002
Marketing does not own statutory or jurisdiction-specific permission truth.

INV-087-V13-PERM-003
Every direct-email candidate requires a current DirectMarketingContactPolicyDetermination.

INV-087-V13-PERM-004
PERMITTED, PROHIBITED and UNRESOLVED are the only determination outcomes.

INV-087-V13-PERM-005
UNRESOLVED cannot externalise.

INV-087-V13-PERM-006
Initial permission bases are exactly AFFIRMATIVE_MARKETING_PERMISSION, QUALIFYING_EXISTING_CUSTOMER_EXCEPTION and PRIOR_PERMISSION_NOT_REQUIRED_BY_APPLICABLE_RULE.

INV-087-V13-PERM-007
Customer relationship, transaction, account and endpoint do not independently create permission.

INV-087-V13-PERM-008
Merchant does not choose the regulatory basis.

INV-087-V13-PERM-009
Historical permission requires sufficient provenance.

INV-087-V13-PERM-010
AI cannot create permission.
```

## Suppression

```text
INV-087-V13-SUPP-001
Current suppression overrides every permission basis.

INV-087-V13-SUPP-002
New business activity does not clear suppression.

INV-087-V13-SUPP-003
Existing-customer exception cannot override opt-out.

INV-087-V13-SUPP-004
Unsubscribe requires no login or CustomerAccount.

INV-087-V13-SUPP-005
Accepted unsubscribe is immediately effective for subsequent Main Street Marketing decisions.

INV-087-V13-SUPP-006
Marketing suppression does not suppress independently governed operational/security communication.

INV-087-V13-SUPP-007
Endpoint suppression does not merge CustomerContexts.

INV-087-V13-SUPP-008
Shared-endpoint suppression cannot be casually cleared by one unrelated CustomerContext.

INV-087-V13-SUPP-009
Suppression history is immutable/auditable.

INV-087-V13-SUPP-010
Provider complaint evidence requires accepted interpretation before becoming canonical suppression.
```

## Contact policy

```text
INV-087-V13-CONT-001
Notification Preference does not create Marketing permission.

INV-087-V13-CONT-002
Known endpoint does not create Marketing authority.

INV-087-V13-CONT-003
One normalised endpoint receives at most one logical direct Marketing responsibility per Campaign Occurrence.

INV-087-V13-CONT-004
Contact equality does not establish customer identity.

INV-087-V13-CONT-005
Contact-pressure policy is MerchantScope × endpoint scoped across Campaigns.

INV-087-V13-CONT-006
Initial limits are 1/24h, 2/7d and 6/30d.

INV-087-V13-CONT-007
Merchant cannot override those limits.

INV-087-V13-CONT-008
Possible prior external effect consumes contact pressure.

INV-087-V13-CONT-009
Known pre-effect failure does not consume contact pressure.

INV-087-V13-CONT-010
No universal EMAIL quiet-hours prohibition exists absent a specific governing rule.

INV-087-V13-CONT-011
Current jurisdiction-required content must be present before externalisation.

INV-087-V13-CONT-012
Marketing cannot bypass policy by transactional relabelling.

INV-087-V13-CONT-013
Current portfolio prohibits mixed operational + promotional logical communication.

INV-087-V13-CONT-014
Current portfolio does not introduce open/click behavioural tracking.
```

## Automation

```text
INV-087-V13-AUTO-001
Standing automation authority is not standing contact permission.

INV-087-V13-AUTO-002
Every Automated Campaign Contract binds one exact Campaign Revision.

INV-087-V13-AUTO-003
Initial automation cannot dynamically generate materially new Campaign content.

INV-087-V13-AUTO-004
Every Automated Campaign Contract has a finite authorityEndsAt.

INV-087-V13-AUTO-005
Initial standing authority cannot exceed 12 calendar months.

INV-087-V13-AUTO-006
Initial trigger families are exactly SCHEDULED_SINGLE_OCCURRENCE_V1 and PERIODIC_AUDIENCE_REEVALUATION_V1.

INV-087-V13-AUTO-007
Initial periodic recurrence families are exactly WEEKLY_LOCAL@1 and MONTHLY_DAY_OF_MONTH_LOCAL@1.

INV-087-V13-AUTO-008
No DAILY or arbitrary cron/RRULE recurrence exists initially.

INV-087-V13-AUTO-009
No source-event Marketing trigger exists initially.

INV-087-V13-AUTO-010
Periodic automation is limited initially to customer appreciation and customer re-engagement direct email.

INV-087-V13-AUTO-011
Initial repeat policy is ONCE_PER_AUTOMATION_CONTRACT.

INV-087-V13-AUTO-012
AI cannot expand automation scope.
```

## Trigger / occurrence

```text
INV-087-V13-TRIG-001
One logical schedule occurrence may establish at most one Campaign Occurrence.

INV-087-V13-TRIG-002
Clock passage alone does not create externalisation authority.

INV-087-V13-TRIG-003
Every automated due point requires current Due Assessment.

INV-087-V13-TRIG-004
Every automation binds an exact Execution Window Profile.

INV-087-V13-TRIG-005
Missed execution windows do not silently catch up.

INV-087-V13-TRIG-006
Multiple missed recurrences do not create a recovery blast.

INV-087-V13-TRIG-007
Local recurrence preserves local civil-time meaning.

INV-087-V13-TRIG-008
Unresolved temporal ambiguity fails closed.

INV-087-V13-TRIG-009
Duplicate worker/schedule signals converge.

INV-087-V13-TRIG-010
Campaign Occurrence does not prove recipient externalisation.
```

## Lifecycle

```text
INV-087-V13-LIFE-001
PAUSED contracts create no new automated occurrences.

INV-087-V13-LIFE-002
Resume does not replay paused occurrences.

INV-087-V13-LIFE-003
TERMINATED contracts cannot resume.

INV-087-V13-LIFE-004
EXPIRED contracts create no future occurrences.

INV-087-V13-LIFE-005
Material Campaign Revision does not silently rebind standing automation.

INV-087-V13-LIFE-006
Entitlement/deactivation prevents future automation without deleting history.
```

## Execution

```text
INV-087-V13-EXEC-001
Audience is re-evaluated per occurrence.

INV-087-V13-EXEC-002
Permission is re-evaluated per recipient.

INV-087-V13-EXEC-003
Suppression is re-evaluated per recipient.

INV-087-V13-EXEC-004
Contact pressure is re-evaluated per recipient.

INV-087-V13-EXEC-005
Endpoint and Notification Preference are re-evaluated.

INV-087-V13-EXEC-006
Material source facts must remain current.

INV-087-V13-EXEC-007
Marketing eligibility must remain valid at Notification external-effect admission.

INV-087-V13-EXEC-008
Retry cannot bypass newly-created suppression.

INV-087-V13-EXEC-009
Provider uncertainty cannot manufacture duplicate Marketing sends.

INV-087-V13-EXEC-010
No implementation activation is granted.
```

---

# 154. Rejected Alternatives

Rejected:

```text
one marketingConsent boolean

CustomerContext means permission

purchase means permission

email means permission

merchant-selected legal basis

one global contact rule

Notification Preference means consent

login-required unsubscribe

purchase clears unsubscribe

merchant override suppression

campaign-specific frequency bypass

shared email merges CustomerContexts

tracking-pixel default

AI chooses permission

standing automation means
standing recipient eligibility

automation with no end date

arbitrary cron

daily direct-email automation

generic source-event triggers

automatic catch-up

replaying paused occurrences

arbitrary dynamic AI content

automatic Campaign Revision rebinding

provider uncertainty causes resend

marketing disguised as transactional
```

---

# 155. Deferred-Decision Outcome

With acceptance:

```text
MKT-GRP-01
    → RESOLVED
```

and:

```text
MS-PROT-087-DQ-003
    → RESOLVED

MS-PROT-087-DQ-004
    → RESOLVED
```

The remaining Marketing DQs are:

```text
MS-PROT-087-DQ-005
Campaign Measure
& Attribution Portfolio

MS-PROT-087-DQ-006
Paid Advertising,
Spend & External
Optimisation Authority
```

---

# 156. Sequencing Consequence

After formalisation of this combined authority:

```text
MKT-GRP-01
    complete
```

The next Marketing-related grouped work is:

```text
MKT-GRP-02
Campaign Evidence &
Performance Interpretation
```

centred on:

```text
MS-PROT-087-DQ-005
```

and only the minimum campaign-specific MS-PROT-083 analytical authority proven necessary by dependency analysis.

`MKT-GRP-03 / DQ-006` remains deferred until its Financial Operations/spend/provider dependencies are sufficiently authoritative.

---

# 157. Implementation-Rules Impact

**Implementation activation: NONE**

Future implementation must preserve:

```text
jurisdiction-rule affinity

permission provenance

suppression history

CustomerContext separation

endpoint suppression

contact-pressure concurrency

Campaign Revision affinity

Automation Contract affinity

schedule occurrence identity

duplicate convergence

no catch-up

pause/resume/termination semantics

per-occurrence audience evaluation

pre-externalisation permission revalidation

Notification separation

provider uncertainty

auditability

idempotency
```

Exact:

```text
database schema

unsubscribe token format

worker technology

queue/broker

API routes

UI components

email provider

rule evaluator implementation

locking mechanism
```

remain downstream.

---

# 158. Recommendation

**RECOMMENDATION: ACCEPT**

Grouping DQ-003 and DQ-004 strengthens rather than weakens the architecture because the full safety invariant can now be stated in one place:

```text
merchant standing automation authority
        ↓
makes Campaign evaluation possible

but

every actual externalisation
        ↓
still requires current
recipient-specific permission,
suppression, contact policy,
endpoint and campaign validity
```

The combined authority closes the direct Marketing safety chain without exposing compliance machinery or automation administration to micro and small merchants.

It passes:

```text
Feature Admission

Fundamental Vision Conformance

ownership review

jurisdiction-difference review

customer-trust review

automation-authority review

temporal/recurrence review

Notification boundary review

provider-uncertainty review

identity/deduplication review

anti-ERP review

combined falsification
```

**Implementation activation: NONE.**
