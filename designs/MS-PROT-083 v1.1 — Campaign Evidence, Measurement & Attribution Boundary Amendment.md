# MS-PROT-083 v1.1 — Campaign Evidence, Measurement & Attribution Boundary Amendment

**Document ID:** MS-PROT-083  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 9 September 2026 by explicit manual approval  
**Grouped work package:** `MKT-GRP-02 — Campaign Evidence & Performance Interpretation`  
**Authority type:** Campaign-specific analytical measurement and attribution-boundary amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** MS-PROT-083 v1.0 only within the initial campaign-specific Analytical Measure portfolio  
**Composes with:** Composite MS-PROT-087 through v1.3  
**Does not amend:** Campaign execution, permission, Notification, Publication, CustomerContext or source-business truth  
**Depends on:** composite MS-PROT-026; composite MS-PROT-027; composite MS-PROT-043; composite MS-PROT-046; composite MS-PROT-053; MS-PROT-054; composite MS-PROT-057; MS-PROT-059; MS-PROT-064; composite MS-PROT-068; composite MS-PROT-069; MS-PROT-072; composite MS-PROT-075 through v1.2; MS-PROT-082; MS-PROT-083 v1.0; composite MS-PROT-087 through v1.3; MS-PROT-088 and applicable source-capability authorities  
**Resolves:** `MS-PROT-087-DQ-005 — Campaign Measure and Attribution Portfolio`  
**Partially resolves:** `MS-PROT-083-DQ-001 — Initial Analytical Measure Portfolio`, campaign-specific slice only  
**Implementation activation:** NONE

---

# 1. Governing Decision

Main Street SHALL initially measure Campaign activity using only evidence whose relationship to the Campaign is deterministically established through accepted Campaign, Publication, Notification, suppression or directly correlated execution semantics.

Canonical:

```text
Campaign activity
        ↓
owner-qualified evidence
        ↓
registered Analytical Input Bindings
        ↓
campaign-specific
Analytical Measure Definitions
        ↓
Analytical Observations
```

The initial portfolio SHALL NOT claim that Marketing caused:

```text
Order

Booking

Appointment

Payment

revenue

customer retention

repeat business

business growth
```

merely because those outcomes occurred after Marketing activity.

Core distinction:

```text
Campaign execution evidence
    ≠ customer engagement

customer engagement
    ≠ commercial conversion

commercial outcome after Campaign
    ≠ Campaign-caused outcome
```

---

# 2. MKT-GRP-02 Scope

This group answers:

```text
What happened
during Campaign execution?

What delivery/contact evidence
can Main Street substantiate?

What may Main Street
truthfully measure?

What may Main Street
truthfully compare?

What may Main Street
attribute to the Campaign?

What claims remain prohibited?
```

It does not answer:

```text
how to spend advertising money

how to optimise external ads

how to calculate Campaign ROI

how to perform causal experiments

how to track user browsing behaviour

how to build an analytics warehouse
```

---

# 3. Feature Admission

## 3.1 Representation Test — PASS

Merchants legitimately need answers such as:

```text
How many customers
could this Campaign contact?

How many emails reached
the provider?

How many have provider-reported
delivery evidence?

How many unsubscribed
through this Campaign?

Did the website announcement publish?
```

Those are materially different facts.

## 3.2 Coordination Test — PASS

Campaign measurement coordinates:

```text
Marketing
+
Notification
+
Publication
+
Data Protection
+
Analytics
```

without transferring ownership.

## 3.3 Administrative-Compression Test — PASS

The merchant SHALL NOT need to reconcile:

```text
Campaign recipient lists

email-provider dashboards

unsubscribe records

website publication history

spreadsheet calculations
```

merely to understand Campaign execution.

## 3.4 ERP-Drift — PASS

The amendment does not introduce:

```text
enterprise marketing analytics

customer data platform

multi-touch attribution suite

marketing data warehouse

funnel designer

customer journey analytics

web analytics platform
```

---

# 4. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING**

Main Street may present concise evidence such as:

```text
Audience matched       427
Eligible to contact    312
Provider accepted      305
Reported delivered     298
Campaign unsubscribes    4
```

while internally preserving:

```text
owner-qualified evidence

coverage

provider qualification

deduplication

time affinity

method version

uncertainty
```

The merchant sees useful business evidence without becoming a marketing analyst.

---

# 5. Ownership Boundary

Ownership remains:

```text
Marketing
    Campaign
    Campaign Revision
    Campaign Occurrence
    recipient eligibility
    Campaign execution relationship

Notification
    Notification Intent
    Dispatch
    Delivery Attempt
    Delivery Evidence

Publication
    Announcement
    lifecycle
    publication revision

Data Protection /
Regulatory authority
    Marketing Contact Suppression
    protected-data use

source capability
    Order / Booking /
    Appointment / Payment etc.

Business Intelligence /
MS-PROT-083
    Analytical Input Bindings
    Measure Definitions
    Analytical Observations
    analytical claims
```

Analytics SHALL NOT copy source semantics merely to simplify reporting.

---

# 6. Campaign Outcome Observation Remains Evidence

MS-PROT-087's:

```text
CampaignOutcomeObservation
```

remains Campaign-related evidence.

It is not automatically an Analytical Observation.

Canonical:

```text
Campaign Outcome Observation
    evidence/correlation artifact

Analytical Observation
    derived result under an exact
    Analytical Measure Definition
```

No semantic ownership is transferred.

---

# 7. Initial Attribution Rule

The initial Campaign attribution rule is:

```text
DIRECT_EXECUTION_TRACE_V1
```

It is deterministic.

It is not a statistical/probabilistic Analytical Method Contract.

---

# 8. Meaning of `DIRECT_EXECUTION_TRACE_V1`

A Campaign-related effect may be attributed to an exact Campaign Occurrence only where accepted semantic provenance directly and deterministically establishes the relationship.

Initial accepted examples:

```text
Campaign Occurrence
    → exact direct-email
      Notification responsibility

Campaign Occurrence
    → exact Notification Dispatch /
      Delivery Evidence

Campaign Occurrence
    → exact Campaign-linked
      Announcement publication

Campaign email
    → exact campaign-bound
      unsubscribe action

Campaign Notification
    → exact correlated provider
      complaint evidence
```

---

# 9. Direct Trace Is Not Commercial Causation

`DIRECT_EXECUTION_TRACE_V1` SHALL NOT establish that a Campaign caused:

```text
Order

Booking

Appointment

Payment

repeat visit

customer retention

revenue

profit

business growth
```

No temporal proximity changes this rule.

---

# 10. No Post-Campaign Commercial Correlation Portfolio Initially

The initial portfolio SHALL NOT calculate:

```text
customers who purchased
within 7 days

customers who booked
within 30 days

revenue from contacted customers

returning customers
after Campaign
```

merely by joining Campaign recipients to later CustomerContext activity.

Reason:

Such measures introduce:

```text
arbitrary outcome windows

behavioural profiling

ambiguous causation

campaign/source correlation semantics

potentially misleading "conversion"
interpretation
```

without sufficient initial product value to justify the complexity.

---

# 11. No Behavioral Tracking Expansion

MS-PROT-087 v1.3 remains authoritative.

This amendment does not introduce:

```text
tracking pixels

email open tracking

generic click tracking

link rewriting for analytics

web-session Campaign tracking

cross-page behavioural tracking

device fingerprinting
```

DQ-005 SHALL NOT be used to bypass the accepted direct-Marketing tracking boundary.

---

# 12. Unsubscribe Correlation Is Not Generic Click Tracking

A purpose-bound unsubscribe control may carry sufficient exact operation affinity to establish:

```text
this suppression operation
originated from this exact
Campaign communication
```

because the link exists to execute the customer's unsubscribe instruction.

This does not authorise collection of unrelated:

```text
page views

clickstream

navigation history

engagement behaviour
```

---

# 13. Campaign Analytical Input Binding Portfolio

The initial campaign-specific Analytical Input Binding portfolio contains exactly:

```text
campaign-analytics /
campaign-occurrence@1

campaign-analytics /
recipient-eligibility@1

campaign-analytics /
direct-email-responsibility@1

campaign-analytics /
notification-delivery-evidence@1

campaign-analytics /
campaign-contact-suppression@1

campaign-analytics /
website-announcement-publication@1
```

No generic:

```text
all-customer-activity
```

analytical binding exists.

---

# 14. Campaign Occurrence Binding

`campaign-analytics / campaign-occurrence@1`

consumes:

```text
Campaign identity

Campaign Revision

Campaign Occurrence

outreach-family affinity

occurrence time/provenance
```

from Marketing authority.

It does not establish external effect.

---

# 15. Recipient Eligibility Binding

`campaign-analytics / recipient-eligibility@1`

consumes exact:

```text
MarketingRecipientEligibilityAssessment
```

evidence including:

```text
Campaign Occurrence

candidate identity basis

outreach family

ELIGIBLE /
INELIGIBLE /
UNRESOLVED

evaluation time

provenance
```

It SHALL NOT reproduce permission-law evaluation itself.

---

# 16. Direct Email Responsibility Binding

`campaign-analytics / direct-email-responsibility@1`

consumes the exact Campaign-to-Notification relationship establishing one logical:

```text
DIRECT_EMAIL_MARKETING_V1
```

communication responsibility.

It does not establish provider acceptance or delivery.

---

# 17. Notification Delivery Evidence Binding

`campaign-analytics / notification-delivery-evidence@1`

consumes only accepted canonical Notification evidence.

Initial relevant evidence classes include:

```text
KNOWN_FAILED_BEFORE_PROVIDER_EFFECT

PROVIDER_ACCEPTED

PROVIDER_REJECTED

PROVIDER_REPORTED_DELIVERED

PROVIDER_REPORTED_BOUNCED

PROVIDER_REPORTED_COMPLAINT

EXECUTION_UNCERTAIN

UNINTERPRETED_PROVIDER_EVIDENCE
```

Provider-native strings are not analytical inputs until Notification authority interprets them.

---

# 18. Marketing Contact Suppression Binding

`campaign-analytics / campaign-contact-suppression@1`

consumes a Marketing Contact Suppression only where exact provenance establishes a relationship to the Campaign communication being measured.

Example:

```text
campaign-bound unsubscribe
        ↓
Marketing Contact Suppression
        ↓
campaign-specific
analytical input
```

A suppression created independently months later cannot be assigned retroactively to the Campaign.

---

# 19. Website Announcement Publication Binding

`campaign-analytics / website-announcement-publication@1`

consumes exact Campaign-to-Publication coordination evidence plus Publication-owned authoritative lifecycle/revision evidence.

It may establish:

```text
campaign-linked Announcement
reached PUBLISHED
```

It does not establish:

```text
customer viewed Announcement

customer read Announcement

customer acted because
of Announcement
```

`PUBLISHED` also remains distinct from current Exposure.

---

# 20. Initial Measure Portfolio

The initial campaign Analytical Measure portfolio contains exactly five Measure Definition families:

```text
campaign /
recipient-eligibility-count@1

campaign /
direct-email-responsibility-count@1

campaign /
direct-email-delivery-evidence-count@1

campaign /
campaign-linked-suppression-count@1

campaign /
website-announcement-publication-count@1
```

No other Campaign metric name is authoritative merely because it is familiar.

---

# 21. `campaign / recipient-eligibility-count@1`

This measure counts unique recipient eligibility assessments at the grain:

```text
MerchantScope
+
Campaign Occurrence
+
outreach family
+
eligibility outcome
```

Outcome dimension is exactly:

```text
ELIGIBLE

INELIGIBLE

UNRESOLVED
```

A candidate is counted at most once per exact:

```text
Campaign Occurrence
×
outreach family
×
canonical recipient/deduplication basis
```

under the accepted Campaign semantics.

---

# 22. Candidate Total

The total candidate-assessment population may be derived as:

```text
ELIGIBLE
+
INELIGIBLE
+
UNRESOLVED
```

only where those outcome sets form complete, mutually exclusive coverage for the declared Campaign/outreach evaluation.

Otherwise:

```text
candidate total
coverage = PARTIAL / UNKNOWN
```

as applicable.

---

# 23. `campaign / direct-email-responsibility-count@1`

This measure counts unique logical direct-email Marketing communication responsibilities created for one exact Campaign Occurrence.

Grain:

```text
MerchantScope
+
Campaign Occurrence
+
DIRECT_EMAIL_MARKETING_V1
```

Transport retries do not increase this count.

Multiple physical Delivery Attempts for one logical responsibility do not increase this count.

---

# 24. Direct Email Responsibility Count Is Not Send Count

Merchant-facing terminology SHALL prefer meaning such as:

```text
Emails prepared for delivery

Direct-email responsibilities
```

where transport certainty matters.

The count SHALL NOT automatically be labelled:

```text
emails delivered
customers reached
```

---

# 25. `campaign / direct-email-delivery-evidence-count@1`

This measure counts unique logical direct-email responsibilities for which one specified canonical Notification evidence class has been established.

Dimensions include:

```text
Campaign Occurrence

Notification evidence class
```

One logical responsibility may legitimately appear in multiple evidence-class observations over time.

Therefore evidence-class counts SHALL NOT be assumed to partition the responsibility population.

---

# 26. Provider-Accepted Count

For:

```text
PROVIDER_ACCEPTED
```

the permissible statement is:

> The provider accepted this many Campaign email responsibilities.

It SHALL NOT be labelled:

```text
delivered
received
read
```

---

# 27. Provider-Reported-Delivered Count

For:

```text
PROVIDER_REPORTED_DELIVERED
```

permissible wording is:

> The email provider reported delivery evidence for this many Campaign email responsibilities.

It SHALL NOT be shortened in high-risk analytical prose to:

```text
customers reached
customers read the email
```

---

# 28. Bounce Count

For:

```text
PROVIDER_REPORTED_BOUNCED
```

Main Street may report provider-qualified bounce evidence.

A bounce does not itself establish:

```text
CustomerContext invalid

customer no longer exists

every future endpoint is invalid

Marketing permission withdrawn
```

---

# 29. Complaint Count

For:

```text
PROVIDER_REPORTED_COMPLAINT
```

Main Street may report exact authenticated/correlated provider complaint evidence.

Any durable suppression consequence remains governed by MS-PROT-087 v1.3 and applicable data-protection/regulatory authority.

---

# 30. Execution-Uncertain Count

For:

```text
EXECUTION_UNCERTAIN
```

Main Street SHALL report uncertainty explicitly.

It SHALL NOT classify the same responsibility as:

```text
not sent
failed
delivered
```

without stronger evidence.

---

# 31. Uninterpreted Provider Evidence

`UNINTERPRETED_PROVIDER_EVIDENCE` may be counted for diagnostic analytical completeness.

It SHALL NOT enter a stronger delivery outcome category.

---

# 32. `campaign / campaign-linked-suppression-count@1`

This measure counts unique Marketing Contact Suppression facts whose exact provenance binds them to one Campaign communication.

Initial analytical source classes are:

```text
CUSTOMER_UNSUBSCRIBE

PROVIDER_COMPLAINT
```

only where exact accepted authority establishes those origins.

---

# 33. Campaign-Linked Unsubscribe Count

Permissible wording:

> Four unsubscribe requests were recorded through this Campaign's direct-email unsubscribe path.

Prohibited wording:

```text
four customers disliked
the Campaign

the Campaign caused
four customers to leave
```

The analytical evidence establishes operation provenance, not customer motivation.

---

# 34. Campaign-Linked Provider Complaint Count

Permissible wording:

> The provider reported complaint evidence associated with two Campaign email responsibilities.

It does not establish:

```text
why the recipient complained

that the Campaign content
was unlawful

that the merchant acted improperly
```

unless another authority independently establishes that proposition.

---

# 35. `campaign / website-announcement-publication-count@1`

This measure counts unique Campaign-linked Announcement publication objects/revisions for which authoritative Publication evidence establishes the relevant publish transition.

Initial grain:

```text
MerchantScope
+
Campaign Occurrence
+
Announcement identity
```

It is normally expected to be small.

---

# 36. Publication Count Is Not Audience Reach

Canonical:

```text
Announcement published
    ≠ Announcement exposed
      to every visitor

Announcement exposed
    ≠ viewed

viewed
    ≠ read

read
    ≠ commercial action
```

No impression/view count is established.

---

# 37. No Generic Delivery Rate Initially

The initial portfolio SHALL NOT introduce a universal:

```text
delivery rate
```

because provider evidence completeness and status semantics may differ materially between fulfilment paths.

Main Street may display counts with explicit evidence coverage.

A later rate may be admitted only through an exact Measure Definition specifying a truthful denominator and provider-evidence coverage requirement.

---

# 38. No Open Rate

No:

```text
open count

open rate
```

is part of the initial portfolio.

Provider-reported open evidence listed historically in MS-PROT-087 v1.0 is narrowed by the accepted v1.3 tracking boundary and is not selected as a production Campaign measure.

---

# 39. No Click-Through Rate

No:

```text
click count

click-through rate
```

is part of the initial portfolio.

The Campaign CTA may direct a customer to a legitimate Main Street surface.

That does not create behavioural tracking authority.

---

# 40. No Conversion Rate

The initial portfolio contains no:

```text
conversion count

conversion rate

booking conversion

order conversion
```

because no accepted Campaign-to-commercial-outcome attribution contract currently establishes that meaning.

---

# 41. No Campaign Revenue Measure

The initial portfolio contains no:

```text
Campaign revenue

revenue generated

Campaign sales

revenue per email

Campaign ROI

ROAS
```

MS-PROT-087-DQ-006 remains separately deferred for paid advertising/spend, and Financial Operations authority remains separately governed.

---

# 42. No Retention-Uplift Measure

The initial portfolio SHALL NOT claim:

```text
retention improved

re-engagement increased retention

Campaign prevented churn
```

No causal retention method is established.

---

# 43. No “Best-Performing Campaign”

The expression:

```text
best-performing Campaign
```

is prohibited in authoritative analytical output unless an exact future performance objective and comparable measure set establish what “best” means.

Main Street may instead say something precise, such as:

> Campaign A had more provider-reported delivered evidence than Campaign B over these comparable Campaign Occurrences.

where all comparison requirements are satisfied.

---

# 44. Initial Attribution Portfolio

The entire initial attribution portfolio is:

```text
DIRECT_EXECUTION_TRACE_V1
```

No:

```text
first-touch attribution

last-touch attribution

multi-touch attribution

view-through attribution

time-decay attribution

incrementality model

marketing-mix model

AI attribution model
```

is authorised.

---

# 45. Why DQ-005 Can Close Without Commercial Attribution

DQ-005 requires Main Street to define:

```text
what is measured

which evidence is sufficient

what attribution method applies

what uncertainty exists

what causal language is permitted
```

A valid initial answer may be:

```text
measure direct execution evidence

attribute only mechanically
traceable direct effects

do not attribute downstream
commercial outcomes
```

The absence of speculative attribution is a deliberate portfolio decision, not unfinished semantics.

---

# 46. Statistical Analytical Method Portfolio Not Required

`DIRECT_EXECUTION_TRACE_V1` is deterministic provenance resolution.

Therefore this amendment does not activate:

```text
MS-PROT-083-DQ-004
Initial Analytical Method Portfolio

MS-PROT-083-DQ-005
Method Qualification Operational Process
```

Those remain deferred.

---

# 47. Commercial Attribution Requires Fresh Feature Admission

A future proposal to claim:

```text
this Campaign generated
12 bookings

this email generated
£800 revenue

this Campaign improved
retention by 8%
```

must undergo fresh Feature Admission.

It would likely require some combination of:

```text
new Campaign interaction evidence

additional protected-data-use authority

source-operation correlation

experimental/statistical design

Analytical Method Contract

Method Qualification

financial semantics

currency semantics

coverage qualification
```

It SHALL NOT automatically reopen DQ-005.

---

# 48. Analytical Grain

Initial campaign measures support exact analytical grain:

```text
one Campaign Occurrence
+
one applicable outreach family
```

where relevant.

They MAY also aggregate across multiple Campaign Occurrences only under:

```text
same Measure Definition version

explicit occurrence/time scope

compatible evidence semantics
```

---

# 49. Periodic Campaign Aggregation

For an Automated Campaign Contract, Main Street MAY aggregate campaign measures across its Campaign Occurrences.

The result SHALL preserve:

```text
Automation Contract affinity

included Campaign Occurrences

evaluation interval

measure version

evidence coverage
```

It does not become a new automation truth.

---

# 50. No Hidden “Campaign Lifetime” Window

A query such as:

```text
How has this Campaign done?
```

must resolve to an explicit analytical scope.

Possible truthful scope:

```text
all Campaign Occurrences
from first committed occurrence
through current evaluation time
```

where coverage is established.

A display SHALL NOT use an undocumented proprietary lookback.

---

# 51. As-Of Analytical Time

Delivery and suppression evidence may continue arriving after a Campaign Occurrence.

Therefore measures are evaluated:

```text
as of exact analytical evaluation time
```

unless another explicit observation interval is supplied.

Example:

```text
12:00
provider-reported delivered = 290

15:00
additional provider evidence arrives

new evaluation
provider-reported delivered = 298
```

The earlier Analytical Observation is not rewritten.

---

# 52. No Universal Campaign Finalisation Deadline

This amendment introduces no:

```text
24-hour reporting cutoff

7-day finalisation period

30-day Campaign close
```

for delivery evidence.

Provider/source evidence may arrive according to its owning contract.

Analytical output remains explicitly time-qualified.

---

# 53. Evidence Coverage

Every material Campaign Analytical Observation SHALL carry one of the existing MS-PROT-083 coverage classes:

```text
COMPLETE_FOR_DECLARED_SCOPE

PARTIAL_KNOWN

COVERAGE_UNKNOWN
```

---

# 54. Provider Delivery Coverage

If provider delivery evidence is incomplete:

Main Street may truthfully say:

> Provider-reported delivery evidence has been observed for 298 messages.

It SHALL NOT infer:

```text
remaining messages
were not delivered
```

Missing evidence is not failure.

---

# 55. Eligibility Coverage

If all Candidate Recipients for one exact Audience Definition/Campaign Occurrence were successfully assessed:

```text
COMPLETE_FOR_DECLARED_SCOPE
```

may apply for that declared recipient assessment scope.

This does not establish:

```text
all real-world merchant customers
were represented
```

---

# 56. Whole-Business Claims Remain Prohibited

Campaign analytics SHALL NOT infer:

```text
all customers

all merchant demand

whole-business retention

whole-business revenue effect
```

from Main Street Campaign evidence alone.

---

# 57. Comparison Semantics

Campaign measures may be compared only where:

```text
same Measure Definition version

compatible Campaign/outreach grain

compatible source-evidence semantics

sufficient coverage

explicit comparison scope
```

are established.

---

# 58. Provider Differences

A Campaign delivered through Provider A and another through Provider B SHALL NOT automatically be compared using provider-reported delivery evidence if the underlying evidence profiles have materially different coverage/meaning.

Provider-neutral canonical interpretation is necessary but not always sufficient for comparability.

The applicable Measure Definition must establish sufficient evidence compatibility.

---

# 59. No Cross-Channel Performance Collapse

`WEBSITE_ANNOUNCEMENT_V1` and `DIRECT_EMAIL_MARKETING_V1` SHALL NOT be collapsed into one generic:

```text
reach

engagement

performance
```

measure.

They expose different evidence.

---

# 60. Data-Use Authority

Campaign analytics does not inherit unrestricted customer-data use merely because Marketing lawfully contacted the customer.

Canonical:

```text
Marketing communication permitted
    ≠ unrestricted analytical use
```

Every Analytical Input Binding remains subject to current MS-PROT-053 data-use authority where required.

---

# 61. Data Minimisation

The initial portfolio requires no complete customer activity dossier.

Campaign measure evaluation should retain or query only what is required for:

```text
Campaign affinity

recipient assessment grain

Notification evidence

suppression provenance

Publication evidence
```

---

# 62. No Customer-Level Performance Ranking

The initial portfolio SHALL NOT rank:

```text
most responsive customer

least responsive customer

high-converting customer

likely buyer

valuable recipient
```

No customer scoring or CRM profiling is created.

---

# 63. No Recipient Engagement Lifecycle

Marketing/BI SHALL NOT persist customer states such as:

```text
ENGAGED

UNENGAGED

RESPONSIVE

UNRESPONSIVE

CAMPAIGN-CONVERTED
```

into CustomerContext.

---

# 64. Campaign Suppression Is Not Performance Failure

A recipient being:

```text
INELIGIBLE

UNRESOLVED

suppressed

frequency-limited
```

does not mean the Campaign:

```text
failed
```

These are governance/contact-policy outcomes.

---

# 65. Campaign Completion Is Not Campaign Success

Existing Marketing semantics remain:

```text
Campaign completion
    = no pending Campaign-owned work
```

not:

```text
business goal achieved
```

Analytics SHALL preserve that distinction.

---

# 66. Merchant-Facing Presentation

A permitted initial presentation may be equivalent to:

```text
Campaign activity

Audience assessed            427
Eligible                     312
Not eligible                  102
Unresolved                     13

Email responsibilities       305
Provider accepted            305
Provider-reported delivered  298
Provider-reported bounced      3
Execution uncertain            1

Unsubscribed through
this Campaign                  4
```

Where a website announcement applies:

```text
Website announcement:
Published
```

---

# 67. Required Qualifying Language

Where evidence class matters, merchant-facing labels SHOULD preserve qualification.

Prefer:

```text
Provider-reported delivered
```

over:

```text
Reached
```

Prefer:

```text
Unsubscribed through
this Campaign
```

over:

```text
Campaign lost customers
```

---

# 68. AI Analytical Explanation

AI MAY explain registered Campaign measures.

Example:

> The provider reported delivery evidence for 298 of the Campaign email responsibilities for which we currently have delivery evidence.

AI SHALL NOT transform this into:

> 298 customers read the Campaign.

---

# 69. AI Cannot Invent Commercial Attribution

AI SHALL NOT infer:

```text
bookings generated

sales generated

revenue generated

retention uplift

Campaign ROI
```

from temporal proximity, narrative plausibility or model reasoning.

---

# 70. Business Recommendation Boundary

MS-PROT-083 may use Campaign execution evidence as one premise in a governed Business Recommendation only where an exact Recommendation Definition permits it.

Example:

```text
high bounce evidence
        ↓
possible recommendation:
review contact-data quality
```

The Recommendation itself remains non-authoritative.

---

# 71. Delivery Evidence Does Not Prove Content Quality

A high provider-reported delivery count does not establish:

```text
good copy

good offer

good audience

customer satisfaction

commercial effectiveness
```

Likewise, lower delivery evidence does not necessarily establish poor Campaign content.

---

# 72. Falsification — Provider Accepted

Scenario:

```text
305 Campaign email
responsibilities

provider accepted 305
```

Expected permissible claim:

```text
Provider accepted: 305
```

Rejected:

```text
305 customers received it
```

**PASS**

---

# 73. Falsification — Delivery Evidence Missing

305 provider accepted.

298 have provider-reported delivered evidence.

Expected:

```text
Provider-reported delivered: 298
```

Rejected:

```text
7 were not delivered
```

unless independent authoritative evidence establishes that.

**PASS**

---

# 74. Falsification — Duplicate Provider Callbacks

Same provider delivery callback arrives three times.

Expected:

```text
one logical responsibility
counts once for that evidence class
```

**PASS**

---

# 75. Falsification — Retry

One logical direct-email responsibility has two legitimate physical attempts.

Expected:

```text
direct-email-responsibility-count
remains 1
```

Delivery evidence remains bound to its exact attempts/responsibility.

**PASS**

---

# 76. Falsification — Uncertain Send

Provider execution uncertain.

Expected:

```text
execution-uncertain evidence count
may increase

not-delivered count
does not appear
```

**PASS**

---

# 77. Falsification — Campaign Unsubscribe

Customer uses exact unsubscribe link in Campaign email.

Expected:

```text
Marketing Contact Suppression

+

campaign-linked suppression measure
```

Permissible:

> One unsubscribe was recorded through this Campaign.

Not permissible:

> The Campaign caused the customer to leave.

**PASS**

---

# 78. Falsification — Independent Later Opt-Out

Customer received Campaign A.

Two months later customer independently changes Marketing preference without Campaign affinity.

Expected:

```text
suppression remains authoritative

not counted as
Campaign A-linked unsubscribe
```

**PASS**

---

# 79. Falsification — Website Announcement Published

Campaign-linked Announcement reaches authoritative `PUBLISHED`.

Expected:

```text
publication count may record it
```

No:

```text
impression

view

engagement
```

is manufactured.

**PASS**

---

# 80. Falsification — Customer Orders Later

Customer receives Campaign email.

Five days later same CustomerContext places an Order.

Expected:

```text
Order remains authoritative

no Campaign conversion

no attributed sale

no Campaign revenue
```

**PASS**

---

# 81. Falsification — Customer Books Through Ordinary Website

Customer receives Campaign then independently visits merchant site and books.

No accepted Campaign-specific behavioural correlation exists.

Expected:

```text
Booking exists

Campaign-to-Booking
attribution = not established
```

**PASS**

---

# 82. Falsification — Merchant Asks “How Much Did It Make?”

Expected Main Street answer meaning:

```text
Current Campaign evidence
does not establish
Campaign-generated revenue.
```

Main Street may separately show accepted Campaign execution measures.

**PASS**

---

# 83. Falsification — Compare Two Campaigns

Campaign A:

```text
provider-reported delivered = 300
```

Campaign B:

```text
provider-reported delivered = 280
```

Same Measure Definition and sufficiently compatible coverage.

Permitted:

> Campaign A had 20 more provider-reported delivered email responsibilities over these declared scopes.

Rejected:

> Campaign A performed better.

unless a later exact performance definition establishes that meaning.

**PASS**

---

# 84. Falsification — Different Providers

A and B use evidence profiles with materially different delivery reporting coverage.

Expected:

```text
comparison unresolved /
qualified

not mechanically ranked
```

**PASS**

---

# 85. Falsification — AI Says Campaign Drove Sales

No qualified causal method exists.

Expected:

```text
claim rejected
```

**PASS**

---

# 86. Falsification — Low-Software-Capacity Merchant

Merchant opens Campaign summary.

Expected:

```text
simple evidence counts
+
plain-language qualification
```

not:

```text
attribution model settings

UTM administration

provider-event mapping

SQL queries

confidence-model configuration
```

**PASS**

---

# 87. Anti-ERP Review

Explicitly excluded:

```text
marketing data warehouse

customer journey analytics

CRM scoring

web analytics suite

multi-touch attribution

enterprise Campaign dashboard builder

marketing mix modelling

generic report designer
```

**PASS**

---

# 88. Hard Invariants — Evidence

```text
INV-083-V11-CAMP-EVID-001
Campaign execution evidence remains distinct from analytical interpretation.

INV-083-V11-CAMP-EVID-002
Notification owns delivery evidence.

INV-083-V11-CAMP-EVID-003
Publication owns Announcement publication truth.

INV-083-V11-CAMP-EVID-004
Source capabilities retain all downstream commercial business truth.

INV-083-V11-CAMP-EVID-005
Provider-native evidence cannot enter Campaign analytics before accepted canonical interpretation.

INV-083-V11-CAMP-EVID-006
Duplicate provider evidence cannot multiply one logical analytical count.

INV-083-V11-CAMP-EVID-007
Missing delivery evidence is not delivery failure.

INV-083-V11-CAMP-EVID-008
Campaign-linked suppression requires exact provenance to the measured Campaign communication.
```

---

# 89. Hard Invariants — Measures

```text
INV-083-V11-CAMP-MEAS-001
Every initial Campaign metric is one of the five registered Measure Definition families.

INV-083-V11-CAMP-MEAS-002
Charts, UI and AI cannot invent alternate Campaign formulas.

INV-083-V11-CAMP-MEAS-003
Direct-email responsibility count uses logical responsibilities, not physical provider attempts.

INV-083-V11-CAMP-MEAS-004
Delivery-evidence count is canonical-evidence-class qualified.

INV-083-V11-CAMP-MEAS-005
One logical responsibility may legitimately contribute to more than one delivery-evidence class over time.

INV-083-V11-CAMP-MEAS-006
Initial portfolio contains no generic delivery rate.

INV-083-V11-CAMP-MEAS-007
Initial portfolio contains no open rate.

INV-083-V11-CAMP-MEAS-008
Initial portfolio contains no click-through rate.

INV-083-V11-CAMP-MEAS-009
Initial portfolio contains no conversion rate.

INV-083-V11-CAMP-MEAS-010
Initial portfolio contains no Campaign revenue, ROI or ROAS measure.

INV-083-V11-CAMP-MEAS-011
Initial portfolio contains no retention-uplift measure.

INV-083-V11-CAMP-MEAS-012
No universal Campaign success score or best-performing-Campaign metric is authorised.
```

---

# 90. Hard Invariants — Attribution

```text
INV-083-V11-CAMP-ATTR-001
Initial Campaign attribution uses exactly DIRECT_EXECUTION_TRACE_V1.

INV-083-V11-CAMP-ATTR-002
DIRECT_EXECUTION_TRACE_V1 requires deterministic accepted provenance.

INV-083-V11-CAMP-ATTR-003
DIRECT_EXECUTION_TRACE_V1 does not attribute downstream Orders, Bookings, Appointments, Payments or revenue.

INV-083-V11-CAMP-ATTR-004
Temporal proximity does not establish Campaign causation.

INV-083-V11-CAMP-ATTR-005
CustomerContext identity does not establish Campaign causation.

INV-083-V11-CAMP-ATTR-006
No post-Campaign commercial-outcome window is introduced initially.

INV-083-V11-CAMP-ATTR-007
No behavioural tracking is introduced to manufacture attribution evidence.

INV-083-V11-CAMP-ATTR-008
Commercial causal attribution requires fresh Feature Admission and applicable MS-PROT-083 analytical-method authority.

INV-083-V11-CAMP-ATTR-009
AI confidence or explanation cannot create Campaign causation.

INV-083-V11-CAMP-ATTR-010
A directly campaign-bound unsubscribe may be traced to that Campaign operation without becoming generic behavioural tracking.
```

---

# 91. Hard Invariants — Coverage and Comparison

```text
INV-083-V11-CAMP-COV-001
Every material Campaign observation carries explicit evidence coverage.

INV-083-V11-CAMP-COV-002
Campaign evidence does not establish whole-business coverage.

INV-083-V11-CAMP-COV-003
Cross-Campaign comparison requires compatible Measure Definition versions and evidence semantics.

INV-083-V11-CAMP-COV-004
Provider differences may prevent comparison even after canonical status interpretation.

INV-083-V11-CAMP-COV-005
Public and direct outreach cannot be collapsed into one generic reach/performance measure.

INV-083-V11-CAMP-COV-006
Campaign measures are as-of exact evaluation time unless another explicit observation interval applies.

INV-083-V11-CAMP-COV-007
Late-arriving evidence creates a new analytical evaluation; it does not rewrite prior Analytical Observation meaning.
```

---

# 92. Hard Invariants — Data / AI

```text
INV-083-V11-CAMP-DATA-001
Marketing contact authority does not create unrestricted analytical-use authority.

INV-083-V11-CAMP-DATA-002
Campaign analytics does not create customer engagement lifecycle states.

INV-083-V11-CAMP-DATA-003
Campaign analytics does not rank customers by responsiveness or commercial value.

INV-083-V11-CAMP-DATA-004
AI may explain only registered Campaign measures and governed analytical claims.

INV-083-V11-CAMP-DATA-005
AI cannot invent conversion, revenue, retention or causal Campaign claims.

INV-083-V11-CAMP-DATA-006
Implementation activation remains NONE.
```

---

# 93. Rejected Alternatives

Rejected:

```text
Campaign success boolean

generic engagement score

open rate

click-through rate

UTM tracking by default

web-session attribution

7-day conversion window

30-day conversion window

last-touch attribution

first-touch attribution

multi-touch attribution

customer purchase after email
equals Campaign conversion

Campaign-generated revenue
without causal authority

Campaign ROI without
spend/financial authority

best-performing Campaign
without exact objective

provider dashboard metrics
copied as Main Street truth

AI attribution
```

---

# 94. Deferred-Decision Outcome

This accepted authority resolves:

```text
MS-PROT-087-DQ-005
    → RESOLVED
```

with the initial Campaign Measure and Attribution portfolio defined by this authority.

`MS-PROT-083-DQ-001` becomes:

```text
PARTIALLY RESOLVED

campaign-specific
initial Analytical Measure slice
resolved by MS-PROT-083 v1.1

general Business Intelligence
measure portfolio remains deferred
```

---

# 95. MS-PROT-083 Decisions Not Resolved

This amendment does NOT resolve:

```text
MS-PROT-083-DQ-002
Business Health Indicators

MS-PROT-083-DQ-003
analytical persistence /
time-series architecture

MS-PROT-083-DQ-004
statistical Analytical Method portfolio

MS-PROT-083-DQ-005
Method Qualification process

MS-PROT-083-DQ-006
recommendation prioritisation

MS-PROT-083-DQ-007
external context

MS-PROT-083-DQ-009
analytical surface

MS-PROT-083-DQ-010
reports/exports

MS-PROT-083-DQ-011
analytical retention

MS-PROT-083-DQ-012
adaptive learning

MS-PROT-083-DQ-013
cross-merchant benchmarking

MS-PROT-083-DQ-014
currency normalisation

MS-PROT-083-DQ-015
Financial Health composition
```

Request-scoped analytical evaluation remains sufficient semantic authority for this design; implementation remains separately governed.

---

# 96. Marketing Decisions Remaining

After this acceptance:

```text
MKT-GRP-01
    COMPLETE

MKT-GRP-02
    COMPLETE
```

The only retained MS-PROT-087 deferred decision is:

```text
MS-PROT-087-DQ-006

Paid Advertising,
Spend & External Optimisation
Authority
```

---

# 97. DQ-006 Remains Deferred

DQ-006 SHALL NOT automatically become next simply because it is the remaining Marketing DQ.

It depends materially on:

```text
Financial Operations

outgoing merchant spend authority

provider/ad-account semantics

budget commitment

financial reconciliation
```

whose readiness must be re-established through `SEQUENCE.md`.

---

# 98. Review/Reputation Remains Separate

Nothing in this amendment creates:

```text
review solicitation

review-provider evidence

reputation score

sentiment analysis

review response authority
```

Review/Reputation remains its own design node.

---

# 99. Implementation-Rules Impact

**Implementation activation: NONE**

A future conforming implementation must preserve:

```text
exact Analytical Measure
Definition affinity

exact Analytical Input Bindings

Campaign Occurrence affinity

logical Notification
responsibility identity

provider evidence qualification

Campaign-linked suppression provenance

Publication authority

evidence coverage

as-of evaluation time

deduplication

no commercial attribution

data-use authority

AI claim grounding
```

Exact:

```text
database schema

aggregation SQL

analytics cache

dashboard

charting library

API routes

materialisation strategy

provider dashboard ingestion
```

remain downstream.

---

# 100. Sequencing Consequence

With this authority accepted and formalised:

```text
MKT-GRP-02
    → COMPLETE
```

The grouped Marketing cleanup ends for all currently dependency-ready work.

Execution SHALL then return to the current global `SEQUENCE.md` frontier under `DESIGN-RULES.md`.

`MKT-GRP-03 / DQ-006` remains deferred until its financial/spend prerequisites are demonstrably ready.

---

# 101. Recommendation

**RECOMMENDATION: ACCEPT — MANUALLY APPROVED**

This authority gives Main Street useful Campaign measurement without manufacturing the false precision typical of marketing software.

The model truthfully answers:

```text
Who was assessed?

Who was eligible?

What direct-email responsibilities
were established?

What does the provider
actually report?

Who unsubscribed through
this Campaign?

Was the Campaign-linked
website Announcement published?
```

while deliberately declining to claim:

```text
who read it

who bought because of it

how much revenue it generated

whether retention improved

which Campaign was “best”
```

until stronger evidence and analytical authority exist.

This is consistent with the core Main Street design principle:

> absorb technical complexity internally, but do not manufacture business truth merely because software can calculate a number.

It passed:

```text
Feature Admission

Fundamental Vision Conformance

ownership review

analytical-boundary review

provider-evidence review

data-use review

anti-tracking review

causal-claim review

anti-ERP review

falsification
```

**Implementation activation: NONE.**
