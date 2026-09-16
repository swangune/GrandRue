# MS-PROT-083 v1.2 — Customer Return Behaviour, Retention Boundary & Re-engagement Analytical Handoff Amendment

**Document ID:** MS-PROT-083  
**Version:** 1.2  
**Status:** ACCEPTED  
**Approved:** 9 September 2026 by explicit manual approval  
**Grouped work package:** `RET-GRP-01 — Customer Return Measurement & Re-engagement Boundary`  
**Authority type:** Customer-return analytical-measurement and cross-capability handoff amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-083 through v1.1 only within the customer-return analytical measure portfolio  
**Composes with:** composite MS-PROT-043; composite MS-PROT-060; composite MS-PROT-042; composite MS-PROT-087 through v1.3  
**Partially resolves:** `MS-PROT-083-DQ-001 — Initial Analytical Measure Portfolio`, customer-return slice only  
**Does not resolve:** MS-PROT-083-DQ-002 through DQ-015 except where an existing accepted boundary is restated  
**Implementation activation:** NONE

---

# 0. Governing Disposition

The Priority-B roadmap node:

```text
customer retention / re-engagement
```

SHALL NOT become a standalone Main Street capability.

The accepted ownership composition is:

```text
CustomerContext
    owns merchant-customer relationship truth

Order / Order Fulfilment
Appointment
Booking
    own customer-business activity truth

Business Intelligence
    owns defined customer-return
    analytical meaning

Marketing
    owns re-engagement Campaign purpose,
    audience evaluation and outreach

Notification
    owns outbound delivery

Merchant
    owns the decision to initiate
    discretionary re-engagement
```

Canonical:

```text
source-owned customer activity
        ↓
customer-return analytical bindings
        ↓
defined customer-return measures
        ↓
merchant understanding
        ↓
optional merchant decision
        ↓
existing Marketing re-engagement flow
```

Not:

```text
customer activity
        ↓
CRM lifecycle state
        ↓
AI churn score
        ↓
automatic campaign
```

---

# 1. Feature Admission

## 1.1 Standalone Retention Capability — REJECTED

A standalone Retention capability fails the ownership/proportionality test.

Main Street already possesses the required authoritative domains:

```text
CustomerContext
Orders
Order Fulfilment
Appointments
Bookings
Marketing
Notification
Business Intelligence
```

Creating another capability to own:

```text
active customer
inactive customer
retained customer
lapsed customer
loyal customer
```

would duplicate existing truth and create CRM semantic gravity.

Therefore:

```text
Retention module
    → REJECTED
```

## 1.2 Representation Test — PASS FOR ANALYTICAL SLICE

Micro and small merchants legitimately need to distinguish:

```text
customers who dealt with the business

customers who had dealt with
the business previously

customers returning after
a meaningful gap
```

These are materially different analytical observations.

## 1.3 Coordination Test — PASS

Customer-return analysis requires coordinated interpretation of:

```text
CustomerContext
+
Order Fulfilment
+
Appointment occurrence
+
Booking utilisation
+
time semantics
+
evidence coverage
+
Business Intelligence
+
optional Marketing handoff
```

without transferring source ownership.

## 1.4 Administrative-Compression Test — PASS

Without this analytical slice, merchants commonly need to:

```text
export customer lists
export order history
export booking history
export appointments
merge spreadsheets
deduplicate customers
guess who is returning
calculate repeat-customer percentages
maintain "lapsed customer" lists
```

Main Street can safely absorb that administration.

## 1.5 ERP / CRM Drift Test — PASS AFTER RECLASSIFICATION

The admitted design explicitly rejects:

```text
CRM pipeline
customer lifecycle engine
loyalty engine
customer scoring
churn scoring
VIP tiers
lead scoring
customer-success platform
arbitrary segmentation
```

The roadmap node is therefore admitted only as:

```text
bounded analytical semantics
+
existing Marketing handoff
```

---

# 2. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING**

A target merchant should be able to ask:

> Are customers coming back?

and receive a bounded answer derived from the business records Main Street already coordinates.

The merchant SHALL NOT need to understand:

```text
cohort SQL
customer joins
identity deduplication
source-event schemas
historical coverage
analytical grain
time-series modelling
CRM segmentation
```

The design increases internal precision while reducing merchant administration.

---

# 3. Governing Ownership Boundary

Hard ownership remains:

```text
CustomerContext
    customer relationship identity/context

Ordering
    Order commitment

Order Fulfilment
    authoritative satisfaction evidence

Appointment
    scheduled-interaction occurrence

Booking
    reservation/utilisation truth

Business Intelligence
    customer-return analytical interpretation

Marketing
    Campaign purpose
    Audience Definition
    recipient eligibility
    re-engagement outreach

Notification
    outbound delivery
```

Business Intelligence SHALL NOT manufacture source facts.

Marketing SHALL NOT manufacture analytical history.

CustomerContext SHALL NOT acquire analytical lifecycle states.

---

# 4. No Canonical Customer Lifecycle State

The following SHALL NOT become authoritative CustomerContext state:

```text
NEW
ACTIVE
RETURNING
LOYAL
DORMANT
LAPSED
CHURNED
AT_RISK
VIP
HIGH_VALUE
```

A customer may satisfy an analytical predicate at one evaluation time without acquiring a durable lifecycle label.

Canonical:

```text
RETURNING in analytical evaluation
    ≠
CustomerContext.status = RETURNING
```

---

# 5. Retention Remains a High-Risk Term

MS-PROT-083 v1.0 remains authoritative that familiar terms such as:

```text
retention
```

are not metrics until precisely defined.

MS-PROT-083 v1.2 SHALL NOT introduce a universal:

```text
Customer Retention Rate
```

because materially different businesses have materially different repeat cycles.

Examples:

```text
barber
    repeat interaction commonly frequent

motel
    repeat interaction may be seasonal

mechanic
    repeat requirement may be occasional

solicitor
    legitimate customer relationship
    may involve no expected return
    for years
```

Therefore:

```text
low repeat activity
    ≠ poor retention
    ≠ business problem
```

without additional accepted semantics.

---

# 6. Initial Analytical Concept — Customer Return Activity Contract

A **Customer Return Activity Contract** defines how exact source-owned evidence may participate in customer-return analysis.

Conceptually:

```text
CustomerReturnActivityContract
{
    contractIdentity
    contractVersion

    sourceSemanticOwner
    sourceSubjectType

    customerRelationshipRequirement

    qualifyingSourceEvidence

    sourceSubjectDeduplicationIdentity

    activityBusinessTime

    correctionSemantics

    requiredCoverage

    dataUsePurpose
}
```

It is an analytical interpretation contract.

It is not source business truth.

---

# 7. Customer Return Activity Is Purpose-Bound

A Customer Return Activity means only:

> An exact source-owned business interaction has reached the minimum accepted point at which it may contribute to customer-return analysis.

It does NOT mean:

```text
customer was satisfied
transaction was profitable
merchant performed perfectly
review should be positive
customer was retained
relationship is healthy
```

---

# 8. Initial Customer Return Activity Portfolio

The initial portfolio contains exactly three contract families:

```text
customer-return /
order-fulfilled-activity@1

customer-return /
appointment-occurred-activity@1

customer-return /
booking-utilised-activity@1
```

No other capability contributes Customer Return Activity under v1.2.

---

# 9. `customer-return / order-fulfilled-activity@1`

One exact Order contributes at most one Customer Return Activity where:

```text
Order
    is authoritatively related
    to exact CustomerContext

AND

at least one authoritative
positive-quantity
Order Fulfilment Satisfaction Portion
exists for that Order
```

The Customer Return Activity identity is based on the exact Order.

Multiple:

```text
Fulfilment portions
Shipments
provider events
delivery attempts
```

for the same Order SHALL NOT manufacture multiple customer-return activities for that Order.

---

# 10. Order Activity Time

The business-time anchor SHALL be the earliest applicable authoritative Satisfaction Anchor time establishing qualifying Order Fulfilment for the exact Order.

It SHALL NOT use:

```text
analytics processing time
provider callback arrival time
email delivery time
Payment time
```

as a substitute.

---

# 11. Returns and Refunds Do Not Erase Historical Activity

A later:

```text
Return
Refund
replacement
Payment reversal
complaint
```

does not automatically erase the fact that qualifying Order Fulfilment occurred.

However:

```text
Order activity
    ≠ satisfaction

Order activity
    ≠ successful commercial outcome
```

Those later facts may participate in other analytics only through separately accepted definitions.

---

# 12. `customer-return / appointment-occurred-activity@1`

One exact Appointment contributes at most one Customer Return Activity where the current authoritative Appointment Occurrence Outcome is:

```text
OCCURRED
PARTIAL_OCCURRENCE
```

The following do not qualify:

```text
CUSTOMER_NO_SHOW
MERCHANT_SIDE_NON_OCCURRENCE
no authoritative outcome
```

---

# 13. Appointment Return Semantics Differ From Review Semantics

MS-PROT-090 may legitimately regard:

```text
MERCHANT_SIDE_NON_OCCURRENCE
```

as a genuine experience capable of supporting neutral review solicitation.

Customer-return analysis answers a different question.

For customer-return analysis:

```text
MERCHANT_SIDE_NON_OCCURRENCE
```

does not establish that the customer returned for an actually occurring/partially occurring customer interaction.

Therefore:

```text
Review Experience qualification
    ≠ Customer Return Activity qualification
```

Shared source evidence does not require identical downstream interpretation.

---

# 14. Appointment Activity Time

The activity SHALL use the exact Appointment business-time semantics associated with the occurrence-bearing Appointment revision.

It SHALL NOT use:

```text
outcome-recorded timestamp
background-job time
analytics evaluation time
```

as the customer activity time.

---

# 15. `customer-return / booking-utilised-activity@1`

One exact Booking contributes at most one Customer Return Activity where:

```text
Booking reservation commitment
    = RELEASED

AND

current Booking Utilisation Outcome
    =
UTILISED
or
PARTIALLY_UTILISED
```

The following do not qualify:

```text
CUSTOMER_NON_UTILISATION
MERCHANT_SIDE_NON_HONOUR
absence of utilisation outcome
```

---

# 16. Booking Activity Time

The applicable business-time anchor SHALL come from the exact Booking utilisation/reservation scope governed by Booking authority.

Analytics SHALL NOT substitute:

```text
Booking creation time
Payment time
provider settlement time
outcome-recording time
```

for the defined customer-use activity time.

---

# 17. Customer Relationship Requirement

Every Customer Return Activity requires an authoritative relationship to one exact:

```text
CustomerContext
```

within the same MerchantScope.

The following are insufficient:

```text
same email
same telephone
same name
same payment card name
same provider customer ID
same postcode
AI similarity
```

Customer-return analytics SHALL NOT create its own identity reconciliation system.

---

# 18. CustomerAccount Is Not Required

A customer does not need a CustomerAccount to participate.

Canonical:

```text
CustomerContext
    required

CustomerAccount
    not universally required
```

Guest commerce may therefore contribute where the source capability already establishes the accepted CustomerContext relationship.

---

# 19. Cross-Capability Customer Return

For a hybrid merchant, prior qualifying activity may come from a different accepted source family.

Example:

```text
CustomerContext C

January:
Appointment OCCURRED

March:
Order fulfilled
```

For merchant-level customer-return analysis:

```text
March customer activity
    may establish C as returning
```

The customer relationship is with the merchant, not with an artificial capability silo.

---

# 20. No Cross-Merchant Return Identity

Customer-return analysis is strictly MerchantScope-bound.

The same person interacting with two different merchants does not create:

```text
cross-merchant customer history
```

or a platform-wide customer-return profile.

---

# 21. Current Customer Return Classification

For one CustomerContext with at least one qualifying Customer Return Activity inside an observation window, BI may determine exactly one current classification for that evaluation:

```text
RETURNING

NO_PRIOR_QUALIFYING_ACTIVITY_IN_DECLARED_COVERAGE

PRIOR_HISTORY_UNRESOLVED
```

These are analytical evaluation outcomes.

They are not CustomerContext lifecycle states.

---

# 22. `RETURNING`

`RETURNING` means:

```text
qualifying Customer Return Activity
exists inside current observation window

AND

at least one earlier
qualifying Customer Return Activity
is authoritatively established
before the observation window
```

The earlier activity may belong to any admitted v1.2 source family.

---

# 23. `NO_PRIOR_QUALIFYING_ACTIVITY_IN_DECLARED_COVERAGE`

This outcome requires:

```text
current qualifying activity

+

sufficient authoritative coverage
for the declared historical scope

+

no qualifying prior activity
inside that declared coverage
```

It SHALL NOT automatically be presented as:

```text
new customer
first-ever customer
first visit ever
```

unless separate evidence establishes whole-business historical coverage.

---

# 24. `PRIOR_HISTORY_UNRESOLVED`

This outcome applies where current qualifying activity is established but required prior-history coverage cannot establish whether earlier qualifying customer activity exists.

Examples:

```text
Main Street history begins recently

older records were legitimately deleted

required source unavailable

legacy activity exists outside
represented Main Street systems

customer reconciliation unresolved
```

Missing history SHALL NOT become:

```text
no prior activity
```

---

# 25. Initial Customer-Return Analytical Input Bindings

The initial binding portfolio contains exactly:

```text
customer-return-analytics /
customer-context@1

customer-return-analytics /
order-fulfilled-activity@1

customer-return-analytics /
appointment-occurred-activity@1

customer-return-analytics /
booking-utilised-activity@1
```

No generic:

```text
customer.lastActivity
all-customer-events
CRM-event-stream
```

binding is authorised.

---

# 26. Initial Customer-Return Measure Portfolio

The initial portfolio contains exactly four Measure Definition families:

```text
customer-return /
qualified-activity-count@1

customer-return /
activity-customer-classification-count@1

customer-return /
returning-customer-share@1

customer-return /
returned-after-quiet-period-count@1
```

These partially resolve `MS-PROT-083-DQ-001`.

No other customer metric becomes authoritative merely because it is familiar.

---

# 27. `customer-return / qualified-activity-count@1`

This measure counts unique Customer Return Activities within an exact observation window.

Grain:

```text
MerchantScope
+
observation window
+
optional source-family dimension
```

Each source subject contributes at most one qualifying activity.

Examples:

```text
one Order
    → maximum one activity

one Appointment
    → maximum one activity

one Booking
    → maximum one activity
```

---

# 28. Activity Counts Are Not Customer Counts

A single CustomerContext may contribute multiple qualifying activities.

Therefore:

```text
qualified activity count
    ≠ distinct customer count
```

A merchant with:

```text
50 activities
```

does not automatically have:

```text
50 customers
```

---

# 29. `customer-return / activity-customer-classification-count@1`

This measure counts distinct CustomerContexts having at least one qualifying activity within the observation window.

Each CustomerContext is counted exactly once under one dimension:

```text
RETURNING

NO_PRIOR_QUALIFYING_ACTIVITY_IN_DECLARED_COVERAGE

PRIOR_HISTORY_UNRESOLVED
```

Grain:

```text
MerchantScope
+
observation window
+
classification outcome
```

Multiple qualifying activities by the same customer inside the period do not multiply the customer count.

---

# 30. Returning Customer Count

The:

```text
RETURNING
```

dimension of:

```text
customer-return /
activity-customer-classification-count@1
```

is the authoritative initial returning-customer count.

No separate database flag or CRM segment is required.

---

# 31. `customer-return / returning-customer-share@1`

This measure answers the bounded question:

> Of customers with qualifying represented activity in this observation window, what proportion had qualifying prior activity?

Conceptually:

```text
RETURNING customers
-----------------------------
all customers with qualifying
activity in observation window
```

But the ratio is authoritative only where historical classification coverage is sufficient for the entire denominator.

---

# 32. Returning Share Fails Closed on Unresolved History

If any customer in the intended denominator is:

```text
PRIOR_HISTORY_UNRESOLVED
```

then:

```text
returning-customer-share
    = UNRESOLVED
```

for the declared scope.

Main Street SHALL NOT calculate the percentage using only customers whose history happened to be resolvable.

This prevents hidden selection bias.

---

# 33. Returning Share Is Not Universal Retention Rate

Merchant-facing language SHALL use meaning such as:

```text
Returning customers

Share of customers who had
dealt with you before
```

It SHALL NOT automatically label this metric:

```text
retention rate
customer loyalty
churn rate
```

Canonical:

```text
returning-customer-share
    ≠ universal retention rate
```

---

# 34. `customer-return / returned-after-quiet-period-count@1`

This measure counts distinct CustomerContexts for which:

```text
qualifying activity occurs
within observation window

AND

at least one older qualifying
activity exists

AND

no qualifying activity exists
during the exact quietPeriod
immediately preceding the
current return activity
```

with sufficient required historical coverage.

Each CustomerContext is counted at most once in the observation window.

---

# 35. Quiet Period Is Explicit

`quietPeriod` SHALL be:

```text
explicit
bounded
version-affined
business-readable
```

There is no universal Main Street definition of:

```text
lapsed after 90 days
```

or any other single duration.

Different analytical definitions may use different exact quiet periods.

---

# 36. Quiet Period Is Not Customer State

A customer satisfying:

```text
returned-after-quiet-period
```

does not acquire:

```text
LAPSED
REACTIVATED
WIN_BACK
```

CustomerContext state.

The result exists only inside the analytical evaluation.

---

# 37. Marketing Re-engagement Remains Separately Governed

Composite MS-PROT-087 remains authoritative for:

```text
marketing/customer-reengagement@1

marketing-audience/
previous-customer-reengagement@1
```

MS-PROT-083 v1.2 does not redefine those contracts.

The two domains answer different questions:

```text
Business Intelligence
    → what customer-return behaviour
      is represented?

Marketing
    → who may be a candidate
      for this Campaign purpose?
```

---

# 38. Analytical Quiet Period Does Not Automatically Equal Marketing Quiet Period

Even where the concepts appear similar:

```text
BI quietPeriod
    ≠ automatically
Marketing Audience quietPeriod
```

Exact affinity must be established deliberately.

A UI or application layer SHALL NOT assume identical semantics merely because both concern elapsed time since customer activity.

---

# 39. Merchant-Initiated Re-engagement Handoff

Where applicable, Main Street MAY provide a contextual merchant action equivalent to:

```text
Invite previous customers back
```

The action SHALL enter the existing Marketing workflow.

It SHALL NOT directly send communication.

Canonical:

```text
customer-return observation
        ↓
merchant chooses
"Invite previous customers back"
        ↓
Marketing Campaign preparation
        ↓
Marketing audience evaluation
        ↓
permission / suppression /
contact-pressure / provider checks
        ↓
merchant-approved / governed
Campaign execution
```

---

# 40. Contextual Handoff Is Not Business Recommendation

MS-PROT-083 v1.2 does not establish a new proactive:

```text
Business Recommendation Definition
```

for customer re-engagement.

Therefore:

```text
"Invite previous customers back"
```

may be exposed as a merchant-initiated contextual capability handoff.

It SHALL NOT be automatically surfaced as:

```text
You should launch a campaign
```

based solely on a customer-return metric.

`MS-PROT-083-DQ-006 — Recommendation prioritisation` remains unresolved.

---

# 41. No Automatic Re-engagement Trigger

Customer activity or lack of activity SHALL NOT itself automatically create or execute a Campaign under v1.2.

Rejected:

```text
customer reaches 90 days inactive
        ↓
automatically email customer
```

Composite MS-PROT-087 remains authoritative for Campaign automation.

This amendment does not expand its accepted trigger portfolio.

---

# 42. Re-engagement Outcome Is Not Campaign Attribution

Suppose:

```text
Campaign sent

then

customer later returns
```

MS-PROT-083 v1.2 MAY establish independently:

```text
customer returned
```

where source evidence supports that claim.

It SHALL NOT establish:

```text
Campaign caused return

Campaign retained customer

Campaign won customer back
```

without separately accepted causal/attribution authority.

MS-PROT-083 v1.1 remains authoritative for the Campaign attribution boundary.

---

# 43. No Post-Campaign Return Conversion Metric

This amendment does not introduce:

```text
re-engagement conversion rate

customers returned after email

revenue from re-engagement

retention uplift from Campaign

Campaign win-back rate
```

merely through temporal correlation.

---

# 44. Whole-Business Coverage Boundary

Main Street SHALL distinguish:

```text
customer activity represented
within declared Main Street scope
```

from:

```text
all customer activity
in the merchant's real-world business
```

If the merchant also operates:

```text
unrepresented cash sales
external booking systems
external marketplace transactions
legacy customer records
```

then a whole-business customer-return claim requires accepted coverage evidence.

---

# 45. Merchant-Facing Coverage Honesty

Where whole-business coverage is not established, presentation SHALL use qualified meaning such as:

```text
Based on customer activity
recorded in Main Street
```

or another truthful scope representation.

It SHALL NOT silently say:

```text
44% of all your customers returned
```

without sufficient coverage authority.

---

# 46. Historical Coverage Start

The presence of an old database record does not by itself establish complete historical coverage.

Where the exact business has only been represented from time `T0`:

```text
history before T0
    may remain unknown
```

unless accepted evidence establishes otherwise.

This matters particularly when determining:

```text
NO_PRIOR_QUALIFYING_ACTIVITY_IN_DECLARED_COVERAGE
```

and returning-customer share.

---

# 47. Customer Identity Correction

CustomerContext reconciliation/correction remains owned by its accepted authority.

If two previously distinct contexts are authoritatively reconciled:

```text
future analytical evaluation
    uses the current authoritative
    customer relationship structure
```

Historical Analytical Observations retain the exact source/evaluation provenance under which they were produced.

They are not silently rewritten.

---

# 48. Source Outcome Correction

Example:

```text
Appointment outcome
initially OCCURRED

later authoritatively corrected
to CUSTOMER_NO_SHOW
```

For a new analytical evaluation:

```text
Appointment no longer contributes
customer-return activity
```

A previously retained Analytical Observation remains historical evidence bound to its original Evaluation Context.

It SHALL NOT rewrite Appointment truth.

---

# 49. Correction Does Not Create Analytical Contradiction

It is legitimate for:

```text
historical Observation O1
    based on evidence revision R1

new Observation O2
    based on corrected revision R2
```

to differ.

The system SHALL preserve exact evidence affinity rather than pretending the analytical past was always known correctly.

---

# 50. Data Protection and Minimisation

Customer-return analytics SHALL operate under accepted protected-data use authority.

This amendment does not justify retaining additional personal data merely to improve analytics.

Where data is legitimately removed under lifecycle authority:

```text
analytical coverage may become
PARTIAL_KNOWN
or
COVERAGE_UNKNOWN
```

as appropriate.

Main Street SHALL prefer honest loss of analytical certainty over unlawful or unnecessary retention.

---

# 51. No Customer-Scoring Store

MS-PROT-083 v1.2 SHALL NOT create durable per-customer fields such as:

```text
returnScore
churnScore
loyaltyScore
valueScore
engagementScore
riskScore
```

Analytical evaluation is not an excuse to turn CustomerContext into a scoring database.

---

# 52. AI Boundary

AI MAY:

```text
explain a returning-customer measure

summarise a historical comparison

explain why the measure is unresolved

help the merchant understand
what "returning" means

help prepare ordinary-language
Marketing content after the merchant
enters the governed Campaign flow
```

AI SHALL NOT:

```text
decide authoritative customer-return classification

infer missing customer history

invent a retention rate

create churn scores

select "likely to leave" customers

silently create Marketing audiences

initiate re-engagement outreach

attribute customer return to Campaign activity
```

Deterministic analytical contracts remain authoritative.

---

# 53. No Churn Prediction in Initial Portfolio

The initial portfolio contains no:

```text
customer churn prediction
return propensity model
purchase probability
lifetime value
next purchase prediction
```

These would require separate Analytical Method Contracts, qualification, privacy analysis and Feature Admission.

They are not necessary to answer the initial merchant question:

> Are customers coming back?

---

# 54. No Customer-Value Segmentation

Customer-return analysis SHALL NOT introduce targeting based on:

```text
spend
revenue
margin
order value
booking value
frequency score
lifetime value
```

MS-PROT-087 v1.2's prohibited targeting boundary remains intact.

---

# 55. No Loyalty Capability

This amendment does not create:

```text
points
rewards
memberships
stamps
tiers
coupons for loyalty
cashback
referral programmes
```

A future loyalty capability requires independent Feature Admission.

---

# 56. No Customer Satisfaction Inference

Returning behaviour SHALL NOT be interpreted as:

```text
customer satisfaction
positive review
service quality
brand advocacy
```

Likewise, failure to return SHALL NOT automatically mean:

```text
dissatisfaction
churn
merchant failure
```

---

# 57. No Staff Performance Inference

Customer-return measures SHALL NOT become direct worker-performance measures.

Rejected:

```text
customers returned after seeing worker X
        ↓
worker X retention score
```

without separate workforce/performance authority and falsification.

No staff leaderboard is introduced.

---

# 58. No Business Health Indicator Yet

MS-PROT-083 v1.2 does not resolve:

```text
MS-PROT-083-DQ-002
Initial Business Health Indicator portfolio
```

Reason:

```text
customer return behaviour
    is observable

but

what level/trend constitutes
MATERIAL_CONCERN
```

varies materially by business model.

The amendment therefore permits truthful observation without inventing universal business-health thresholds.

---

# 59. No Generic Retention Alert

Main Street SHALL NOT initially surface:

```text
Your retention is bad
```

or:

```text
Customer churn warning
```

from these measures alone.

A later Business Health Indicator requires a separate accepted definition establishing:

```text
applicability
comparison semantics
evidence requirements
materiality
business-model dependence
```

---

# 60. Comparisons Use Existing MS-PROT-083 Authority

Customer-return measures MAY participate in comparisons such as:

```text
this month vs previous month

this quarter vs prior comparable quarter
```

only where existing MS-PROT-083 comparison and compatibility requirements are satisfied.

A change in returning-customer share does not automatically establish a causal explanation.

---

# 61. Hybrid Merchant Falsification

Merchant:

```text
salon
+
product retail
```

Customer:

```text
January haircut OCCURRED
March product Order fulfilled
```

Expected:

```text
March customer classified RETURNING
at merchant-level scope
```

without requiring a CRM record.

**PASS**

---

# 62. Barber Falsification

A barber has 80 customers with qualifying activity this month.

40 have qualifying prior activity.

40 have no prior qualifying activity within complete represented history.

Expected:

```text
RETURNING = 40

NO_PRIOR... = 40

PRIOR_HISTORY_UNRESOLVED = 0

returning-customer-share = 50%
```

**PASS**

---

# 63. Incomplete-History Falsification

Same barber has only two months of Main Street history and prior records are unavailable.

Twenty customer histories cannot be classified.

Expected:

```text
RETURNING count
may still be established
where prior activity exists

PRIOR_HISTORY_UNRESOLVED = 20

returning-customer-share
= UNRESOLVED
```

Main Street SHALL NOT silently present a percentage from the resolvable subset.

**PASS**

---

# 64. Solicitor Falsification

A solicitor serves customers who may legitimately require assistance only once every several years.

Observed returning-customer share is low.

Expected:

```text
measure may exist

no MATERIAL_CONCERN inferred

no churn label

no automatic re-engagement Campaign
```

**PASS**

---

# 65. Appointment No-Show Falsification

Customer books an Appointment.

Outcome:

```text
CUSTOMER_NO_SHOW
```

Expected:

```text
no Customer Return Activity
```

even though a commitment existed.

**PASS**

---

# 66. Merchant-Side Appointment Failure Falsification

Outcome:

```text
MERCHANT_SIDE_NON_OCCURRENCE
```

Expected:

```text
may qualify for neutral review solicitation
under MS-PROT-090 where applicable

but

does not qualify for
customer-return activity
```

**PASS**

---

# 67. Booking Non-Honour Falsification

Hotel Booking reaches:

```text
MERCHANT_SIDE_NON_HONOUR
```

Expected:

```text
no customer-return activity
```

The customer did not receive the booked utilisation.

**PASS**

---

# 68. Fulfilled-Then-Refunded Order Falsification

Order:

```text
fulfilled

then

customer requests refund
```

Expected:

```text
qualifying historical customer activity remains

but

no satisfaction/profit/retention
claim is created
```

**PASS**

---

# 69. Multiple Fulfilments Falsification

One Order is fulfilled through three shipments.

Expected:

```text
one Order-based
Customer Return Activity

not three
```

**PASS**

---

# 70. Quiet-Period Return Falsification

Customer has:

```text
qualifying activity:
1 January

no qualifying activity:
2 January–30 April

qualifying activity:
1 May
```

For an exact 90-day quietPeriod with sufficient historical coverage:

```text
customer may contribute
returned-after-quiet-period
```

For an exact 180-day quietPeriod:

```text
customer does not qualify
```

No durable lifecycle state is created.

**PASS**

---

# 71. Campaign Correlation Falsification

Customer receives a re-engagement Campaign on 20 April and returns 1 May.

Expected:

```text
BI may establish:
customer returned after quiet period

Marketing analytics may establish:
Campaign communication evidence

Main Street may NOT establish:
Campaign caused the return
```

**PASS**

---

# 72. Contact Opt-Out Falsification

A returning-after-quiet-period observation exists.

Customer has Marketing suppression.

Merchant chooses:

```text
Invite previous customers back
```

Expected:

```text
Marketing re-evaluates
audience + permission + suppression

suppressed customer
does not externalise
```

BI observation does not override contact authority.

**PASS**

---

# 73. AI Falsification

AI states:

> These 37 customers are at risk of churn.

No accepted churn method exists.

Expected:

```text
statement prohibited
```

AI may instead say:

> Main Street cannot currently determine which individual customers are likely to stop returning.

**PASS**

---

# 74. Low-Software-Capacity Merchant Falsification

Merchant opens Main Street and sees:

```text
Customers this month        84

Returning customers         37

Returning customer share    44%
```

where coverage supports the figures.

The merchant does not configure:

```text
joins
segments
activity schemas
deduplication
database queries
lifecycle stages
```

If the merchant wants outreach:

```text
[Invite previous customers back]
```

opens the governed Marketing flow.

**PASS**

---

# 75. Architectural Review

## Source Ownership — PASS

No commerce/customer source truth moves into BI.

## Cross-Capability Value — PASS

Order, Appointment and Booking evidence can contribute to one merchant-level customer-return question.

## Administrative Compression — PASS

Manual spreadsheet reconciliation is removed.

## Anti-CRM — PASS

No lifecycle, pipeline, scoring or segment store is created.

## Anti-Marketing-Suite — PASS

Outreach remains existing Marketing authority.

## AI Non-Authority — PASS

No churn/propensity inference is introduced.

## Data Protection — PASS

No additional durable customer scoring is required.

## Provider Neutrality — PASS

No provider defines customer-return semantics.

---

# 76. Ambiguity Review

**Outcome: PASS**

The following distinctions are explicit:

```text
CustomerContext
    ≠ Customer Return Activity

Customer Return Activity
    ≠ customer satisfaction

Customer Return Activity
    ≠ Review Experience qualification

RETURNING analytical classification
    ≠ CustomerContext lifecycle state

returning-customer-share
    ≠ universal retention rate

returned-after-quiet-period
    ≠ lapsed-customer state

BI quietPeriod
    ≠ Marketing quietPeriod

customer returned after Campaign
    ≠ Campaign caused return

contextual Marketing handoff
    ≠ Business Recommendation

customer-return analytics
    ≠ CRM
```

No unresolved semantic collision remains within the initial portfolio.

---

# 77. Hard Invariants

```text
INV-083-V12-001
Customer retention/re-engagement SHALL NOT become a standalone Main Street capability under this amendment.

INV-083-V12-002
CustomerContext retains merchant-customer relationship authority.

INV-083-V12-003
Source capabilities retain authoritative customer-business activity truth.

INV-083-V12-004
Business Intelligence owns only accepted customer-return analytical meaning.

INV-083-V12-005
Marketing retains re-engagement Campaign and Audience authority.

INV-083-V12-006
No durable NEW/ACTIVE/RETURNING/LOYAL/DORMANT/LAPSED/CHURNED/AT_RISK customer lifecycle state is created.

INV-083-V12-007
No universal Customer Retention Rate is authorised.

INV-083-V12-008
Initial Customer Return Activity contracts are exactly fulfilled Order, occurred/partially occurred Appointment and utilised/partially utilised Booking families.

INV-083-V12-009
One exact source subject contributes at most one Customer Return Activity.

INV-083-V12-010
Customer Return Activity requires authoritative affinity to one CustomerContext in the same MerchantScope.

INV-083-V12-011
Contact-value similarity or AI inference cannot establish CustomerContext relationship.

INV-083-V12-012
CustomerAccount is not universally required for customer-return analysis.

INV-083-V12-013
Appointment CUSTOMER_NO_SHOW and MERCHANT_SIDE_NON_OCCURRENCE do not qualify as customer-return activity.

INV-083-V12-014
Booking CUSTOMER_NON_UTILISATION and MERCHANT_SIDE_NON_HONOUR do not qualify as customer-return activity.

INV-083-V12-015
Review Experience qualification and Customer Return Activity qualification are non-interchangeable.

INV-083-V12-016
Missing prior history cannot be interpreted as no prior activity.

INV-083-V12-017
Returning-customer share is UNRESOLVED when intended denominator history classification is unresolved.

INV-083-V12-018
RETURNING is an analytical evaluation outcome, not CustomerContext state.

INV-083-V12-019
Quiet-period return is purpose-bound analytical meaning, not a lapsed/reactivated lifecycle transition.

INV-083-V12-020
BI quietPeriod and Marketing re-engagement quietPeriod are not implicitly equivalent.

INV-083-V12-021
Customer return after a Campaign does not establish Campaign causation or attribution.

INV-083-V12-022
No post-Campaign return conversion, retention uplift or win-back metric is authorised.

INV-083-V12-023
No customer churn, propensity, lifetime-value or loyalty scoring is authorised.

INV-083-V12-024
Customer-return measures cannot directly create worker-performance measures.

INV-083-V12-025
No Business Health concern is inferred solely from low customer-return behaviour.

INV-083-V12-026
No automatic source-event-driven re-engagement Campaign is authorised.

INV-083-V12-027
A merchant-initiated re-engagement handoff enters existing Marketing authority and revalidates all Marketing constraints.

INV-083-V12-028
Whole-business customer-return claims require sufficient whole-business coverage evidence.

INV-083-V12-029
Historical Analytical Observations retain exact evidence/revision affinity after later source or CustomerContext correction.

INV-083-V12-030
Data lifecycle may reduce analytical coverage; analytics does not justify unnecessary personal-data retention.

INV-083-V12-031
AI cannot create authoritative return classification or infer missing history.

INV-083-V12-032
No new Recommendation Definition is introduced by v1.2.

INV-083-V12-033
MS-PROT-083-DQ-002 Business Health Indicator portfolio remains unresolved.

INV-083-V12-034
MS-PROT-083-DQ-001 is only partially resolved further, for the exact v1.2 customer-return measure slice.

INV-083-V12-035
Implementation activation remains NONE.
```

---

# 78. Rejected Alternatives

Rejected:

```text
create MS-PROT-091 Retention capability

build a CRM module

persist ACTIVE / LAPSED / CHURNED states

use Order commitment alone as return activity

use Appointment booking alone as return activity

count customer no-show as return activity

count merchant-side non-occurrence as return activity

count every Shipment as another customer visit

infer first-ever customer from missing history

universal 90-day lapsed rule

universal retention percentage

customer churn score

AI-selected at-risk customers

customer lifetime value in initial portfolio

VIP / loyalty tiers

automatic "win-back" Campaign

source-event-driven re-engagement email

Campaign-return conversion rate

Campaign-caused retention claim

negative-review/customer-sentiment retention filtering

staff retention-performance leaderboard

whole-business retention claim from partial Main Street data
```

---

# 79. Deferred-Scope Outcome

The defined `RET-GRP-01` initial portfolio has **no retained semantic DQ of its own**.

The following remain outside this initial portfolio:

```text
universal or vertical-specific
Customer Retention Rate

customer churn prediction

customer lifetime value

loyalty/rewards

product-specific repeat purchasing

service-specific affinity segmentation

customer frequency scoring

cohort survival curves

probabilistic return prediction

automatic re-engagement triggers

Campaign-caused return attribution

customer return Business Health thresholds

proactive customer-return Recommendation Definitions

cross-merchant customer benchmarking
```

If later promoted, each requires the applicable existing DQ or fresh Feature Admission.

Existing DDR effects remain:

```text
MS-PROT-083-DQ-001
    PARTIALLY RESOLVED further
    for customer-return slice

MS-PROT-083-DQ-002
    remains DEFERRED

MS-PROT-083-DQ-004
    remains DEFERRED

MS-PROT-083-DQ-006
    remains DEFERRED

MS-PROT-083-DQ-012
    remains DEFERRED

MS-PROT-083-DQ-013
    remains DEFERRED
```

---

# 80. Priority-B Roadmap Disposition

Upon acceptance, the roadmap node:

```text
customer retention / re-engagement
```

SHALL be considered:

```text
DESIGN-CLOSED — INITIAL PORTFOLIO
```

through composition of:

```text
MS-PROT-043
    CustomerContext

source commerce authorities
    customer activity truth

MS-PROT-083 v1.2
    customer-return analytics

MS-PROT-087
    re-engagement Campaign semantics

MS-PROT-075
    Notification delivery
```

No standalone Retention capability remains pending.

---

# 81. Fundamental Vision Result

```text
VISION-CONFORMING
```

The design answers a real merchant question while deliberately refusing to create the CRM machinery normally associated with “retention software.”

Main Street absorbs:

```text
cross-capability activity interpretation
customer deduplication
historical evidence qualification
return classification
coverage honesty
```

and leaves the merchant with ordinary business language.

---

# 82. Recommendation

```text
RECOMMENDATION: ACCEPT
```

The proposal is the minimum sufficiently expressive treatment of the Priority-B customer retention/re-engagement node.

It gives Main Street useful customer-return intelligence while preserving the critical boundary:

> **Main Street may measure whether customers return and help a merchant enter an existing re-engagement workflow; it does not turn customers into CRM lifecycle objects or claim that Marketing caused their behaviour.**

---

# 83. Approval Boundary

This authority was approved by explicit manual approval on 9 September 2026 and is authoritative within its stated scope.

Repository formalisation and governance navigation updates do not activate implementation.

```text
implementation activation = NONE
```
