# MS-PROT-083 v1.3 — Initial General Business Intelligence Measure Portfolio Amendment

**Document ID:** MS-PROT-083  
**Version:** 1.3  
**Status:** ACCEPTED — explicit manual approval granted 10 September 2026  
**Authority type:** Business Intelligence semantic amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2 and `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-083 through v1.2  
**Depends on:** Composite MS-PROT-042, MS-PROT-053, MS-PROT-055, MS-PROT-058, MS-PROT-059, MS-PROT-077 and MS-PROT-081 within their applicable scopes  
**Design node:** `MS-PROT-083-DQ-001 — Initial Analytical Measure portfolio`  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING

---

# 1. Governing Decision

Main Street SHALL establish a deliberately small initial **general Business Intelligence Measure portfolio** over already-authoritative operational facts.

The governing architecture is:

```text
source capability
owns business truth
        ↓
source-owned business fact
+ exact interaction-origin provenance where applicable
        ↓
owner-qualified Analytical Input Binding
        ↓
exact Measure Definition
        ↓
Analytical Observation
        ↓
business-language interpretation
```

Business Intelligence owns:

```text
Analytical Input Bindings
Measure Definitions
Analytical Observations
analytical classifications
analytical provenance
coverage qualification
```

Business Intelligence does NOT take ownership of:

```text
Orders
Appointments
Bookings
Payment Obligations
Payment Applications
Inventory Positions
Inventory Claims
Scheduled Work Commitments
Leave
customer identity
interaction-channel source provenance
financial accounting truth
```

The initial general portfolio SHALL contain exactly seven Measure Definition families:

```text
business-intelligence /
order-commitment-count@1

business-intelligence /
appointment-occurrence-outcome-count@1

business-intelligence /
booking-utilisation-outcome-count@1

business-intelligence /
outstanding-payment-obligation-count@1

business-intelligence /
outstanding-payment-obligation-value@1

business-intelligence /
inventory-position-constraint-count@1

business-intelligence /
scheduled-commitment-person-duration@1
```

The first three commerce measures MAY additionally be partitioned by the exact **Commitment Interaction Origin** analytical dimension defined by this amendment.

No additional Measure Definition is created merely to count interaction origins.

No other familiar KPI becomes authoritative merely because it is commonly used in business software.

---

# 2. Feature Admission

## 2.1 Representation Test

**PASS**

Main Street already owns or coordinates the source operational facts represented by this portfolio.

Without exact Measure Definitions, the platform cannot safely answer ordinary merchant questions such as:

```text
How many orders did we take?

How did customers place those orders?

What happened with our appointments?

Were those appointments booked online,
in person or by telephone?

Were our bookings actually used?

How much represented customer payment obligation
is still outstanding?

How many stock positions are constrained?

How much staff time is currently scheduled?
```

without allowing dashboard code, AI or ad-hoc queries to invent semantic meaning.

## 2.2 Coordination Test

**PASS**

The measures provide a governed interpretation layer over independently authoritative capabilities while preserving ownership.

## 2.3 Administrative-Compression Test

**PASS**

The merchant SHOULD NOT need to export:

```text
orders
appointments
bookings
payment records
inventory
staff schedules
channel-origin records
```

into spreadsheets merely to understand basic business operation and how customers transact with the business.

## 2.4 ERP-Drift Test

**PASS**

This amendment deliberately rejects:

```text
general ledger
financial statements
data warehouse
OLAP cube
arbitrary report builder
custom formula engine
enterprise KPI designer
workforce performance management
customer scoring
multi-channel attribution suite
customer journey analytics
```

The portfolio is intentionally smaller than what the architecture could technically support.

---

# 3. Scope

This amendment defines:

1. the initial general BI Analytical Input Binding portfolio;
2. seven exact Measure Definition families;
3. one bounded Commitment Interaction Origin analytical classification;
4. interaction-origin partitioning for Order, Appointment and Booking measures;
5. temporal and currentness rules;
6. evidence-coverage requirements;
7. zero-versus-unknown semantics;
8. currency separation;
9. historical correction semantics;
10. merchant-facing interpretation boundaries;
11. privacy, AI and source-authority boundaries.

It completes the **initial general Measure portfolio** required by `MS-PROT-083-DQ-001`.

---

# 4. Non-Goals

This amendment does NOT establish:

```text
revenue
profit
gross margin
net margin
cash flow
cash position
accounts receivable
customer lifetime value
average order value
conversion rate
utilisation rate
productivity
labour efficiency
employee performance
inventory turnover
stockout rate
growth rate
churn
new generic retention rate
forecasting
benchmarks
business-health thresholds
red/amber/green status
recommendation ranking
causal attribution
marketing attribution
financial-health measures
cross-currency totals
```

It also does NOT establish:

```text
OnlineOrder
WalkInOrder
TelephoneOrder

OnlineAppointment
WalkInAppointment
TelephoneAppointment

OnlineBooking
WalkInBooking
TelephoneBooking
```

Channel/origin remains provenance around one shared capability-owned commitment model.

---

# 5. Ownership Boundary

Source ownership remains:

```text
Ordering
    Order commitment truth

Appointment
    Appointment commitment
    and occurrence outcome truth

Booking
    Booking commitment
    and utilisation outcome truth

Payment
    Payment Obligation
    Payment Obligation Adjustment
    Payment Application
    Due Evaluation
    outstanding-payment truth

Inventory
    Inventory Position
    stock-on-hand
    Inventory Claim
    claim-resolution truth

Workforce Scheduling
    ScheduledWorkCommitment
    Workforce Scheduling Arrangement
    scheduling interval truth

MS-PROT-059 / applicable operation owner
    interaction-channel/origin provenance
    surrounding authoritative operation
```

Business Intelligence owns only the exact analytical interpretation defined here.

Canonical:

```text
analytical measure
    ≠ source business fact
```

and:

```text
analytical origin classification
    ≠ source interaction provenance
```

and:

```text
ability to calculate a measure
    ≠ authority to mutate its source
```

---

# 6. Unified Interaction Channel Boundary

MS-PROT-059 remains authoritative for the principle that multiple channels converge on the same business operations.

Canonical:

```text
Online Order
Phone Order
Walk-In Order
```

are rejected as separate business-object types.

Preferred:

```text
Order
    +
origin/provenance
```

Likewise:

```text
Appointment
    +
origin/provenance
```

and, where applicable:

```text
Booking
    +
origin/provenance
```

The analytical model SHALL consume such provenance rather than manufacture a separate BI-owned `salesChannel` source field.

---

# 7. Mode of Sale Is Not Technical Entry Mechanism

Main Street SHALL distinguish:

```text
Commitment Interaction Origin
```

from:

```text
technical entry surface
record-entry mechanism
Payment method
Payment channel
Fulfilment method
collection/delivery method
Marketing attribution
customer acquisition source
```

Example:

```text
customer telephones merchant

staff enters Order using POS
```

means:

```text
Commitment Interaction Origin
    = TELEPHONE

Technical Entry Surface
    = POS
```

It MUST NOT become:

```text
Commitment Interaction Origin
    = WALK_IN
```

merely because POS software was used.

---

# 8. Commitment Interaction Origin

A **Commitment Interaction Origin** is the analytical classification of the customer/business interaction through which the exact commercial commitment was authoritatively established.

Initial classification vocabulary:

```text
ONLINE

WALK_IN

TELEPHONE

OTHER_REPRESENTED

UNRESOLVED
```

These are analytical classes.

They do not alter source business identity.

---

# 9. `ONLINE`

`ONLINE` applies only where authoritative operation provenance establishes that the commitment-establishing interaction occurred through an applicable customer-facing remote digital self-service path.

Examples may include:

```text
merchant website Order
customer self-booked Appointment
customer self-service Booking
```

where accepted source provenance establishes that origin.

`ONLINE` MUST NOT be inferred merely because:

```text
Payment happened online
staff used a web dashboard
merchant used Main Street
confirmation email was sent
customer later visited a website
```

---

# 10. `WALK_IN`

`WALK_IN` applies only where authoritative operation provenance establishes that the commitment-establishing customer interaction occurred in person at the merchant's physical operational context.

Examples:

```text
customer walks into shop
and buys product

customer arrives at salon
and staff creates same-day Appointment

customer arrives at motel reception
and staff creates Booking
```

Technical entry may occur through:

```text
POS
merchant dashboard
staff device
```

without changing the analytical origin from `WALK_IN`.

---

# 11. `TELEPHONE`

`TELEPHONE` applies only where authoritative operation provenance establishes that the commitment-establishing customer interaction occurred through telephone-assisted communication.

Example:

```text
customer calls merchant
        ↓
staff creates Appointment
```

The Appointment may have been entered through the merchant dashboard, but:

```text
Commitment Interaction Origin
    = TELEPHONE
```

because the authoritative interaction provenance establishes telephone assistance.

---

# 12. `OTHER_REPRESENTED`

`OTHER_REPRESENTED` applies where:

```text
authoritative source provenance exists
```

but the exact commitment-establishing interaction does not conform to the initial:

```text
ONLINE
WALK_IN
TELEPHONE
```

classification.

Examples may eventually include an explicitly represented:

```text
external integration
other accepted assisted channel
future deliberately admitted channel
```

This class prevents the initial three-category business presentation from forcing materially different origins into an incorrect category.

The underlying exact source provenance MUST remain available.

---

# 13. `UNRESOLVED`

`UNRESOLVED` applies where the commercial commitment is authoritative but sufficient authoritative provenance does not establish the applicable Commitment Interaction Origin.

Canonical:

```text
origin missing
    ≠ WALK_IN
```

and:

```text
staff entered record
    ≠ sufficient evidence
      of interaction origin
```

Main Street MUST NOT infer origin using:

```text
customer address
Payment method
employee identity
time of day
device
IP address
merchant category
AI probability
```

---

# 14. Commitment-Establishing Interaction

Where a customer journey uses several channels, classification follows the interaction through which the exact commitment was established.

Example:

```text
customer sees merchant website
        ↓
customer later telephones
        ↓
staff establishes Booking
```

Result:

```text
Commitment Interaction Origin
    = TELEPHONE
```

The earlier website visit does not make the Booking `ONLINE`.

Similarly:

```text
customer makes Order online
        ↓
pays at collection
        ↓
collects in store
```

remains:

```text
Commitment Interaction Origin
    = ONLINE
```

because Payment and Fulfilment are separate dimensions.

---

# 15. Interaction Origin Is Not Marketing Attribution

The classification answers:

> Through what interaction mode was this exact commitment established?

It does NOT answer:

```text
How did the customer discover the merchant?

Which Campaign caused the transaction?

Which advertisement converted?

What was the customer's first touch?

What influenced the purchase?
```

Therefore:

```text
ONLINE Order
```

does not mean:

```text
online marketing caused Order
```

and:

```text
TELEPHONE Booking
```

does not mean:

```text
telephone marketing caused Booking
```

---

# 16. Interaction Origin Is Not Payment Method

The following is valid:

```text
Order origin
    = TELEPHONE

Payment
    = remote online provider execution

Fulfilment
    = collection
```

Likewise:

```text
Booking origin
    = ONLINE

Payment
    = pay in person
```

No one fact overwrites another.

---

# 17. No General-Purpose Metric Engine

This amendment does NOT establish:

```text
metric expression language
user-authored formulas
SQL metric definitions
generic aggregation DSL
arbitrary join builder
merchant KPI editor
AI-generated formulas
```

Every production Measure Definition remains an accepted registered semantic contract.

Future measures require normal Feature Admission.

---

# 18. Initial Analytical Input Binding Portfolio

The general portfolio contains exactly seven new Analytical Input Binding families:

```text
business-intelligence-analytics /
order-commitment@1

business-intelligence-analytics /
appointment-occurrence-outcome@1

business-intelligence-analytics /
booking-utilisation-outcome@1

business-intelligence-analytics /
payment-obligation-state@1

business-intelligence-analytics /
inventory-position-constraint@1

business-intelligence-analytics /
scheduled-work-commitment@1

business-intelligence-analytics /
commitment-interaction-origin@1
```

The final binding is a source-provenance interpretation binding.

It is not an independently mutable business record.

---

# 19. `commitment-interaction-origin@1`

This binding consumes only authoritative provenance associated with the exact commitment-establishing operation.

It SHALL include sufficient affinity to establish:

```text
MerchantScope
source capability
source commitment identity
source operation / establishment identity
source-origin provenance
source-origin revision/evidence
```

where available.

Its only initial analytical output is:

```text
ONLINE
WALK_IN
TELEPHONE
OTHER_REPRESENTED
UNRESOLVED
```

It MUST NOT overwrite or reduce the underlying exact source provenance.

---

# 20. Common Binding Requirements

Every binding SHALL identify, where applicable:

```text
MerchantScope
source semantic owner
source object identity
source revision / authoritative state
business-time affinity
evaluation time
required source coverage
correction provenance
unit / Currency where applicable
interaction-origin provenance where applicable
```

Bindings MUST NOT convert missing source evidence into analytical facts.

A binding SHALL NOT acquire authority merely by copying a value into an analytics structure.

---

# 21. Temporal Classes

The initial portfolio uses three explicit temporal forms.

## 21.1 Interval Activity

An interval measure operates over:

```text
[start, end)
```

where:

```text
start < end
```

The boundary MUST resolve deterministically under applicable source business-time semantics.

Interval measures are:

```text
order-commitment-count@1
appointment-occurrence-outcome-count@1
booking-utilisation-outcome-count@1
```

## 21.2 As-Of State

An as-of measure describes authoritative state observed at one exact:

```text
evaluatedAt
```

Payment and Inventory measures use this class.

## 21.3 As-Of Planning Window

The Workforce measure uses:

```text
evaluatedAt
+
planning interval [start, end)
```

It answers what authoritative Scheduled Work is currently committed inside the specified planning interval.

---

# 22. Coverage Rule

Every Measure Definition MUST declare the source coverage required to make its result truthful.

Canonical:

```text
missing evidence
    ≠ zero
```

A numeric zero may be produced only when sufficient authoritative coverage establishes:

```text
complete declared source scope
+
no qualifying fact
```

Origin-specific zeroes additionally require sufficient origin-provenance coverage.

Example:

```text
ONLINE Orders = 0
```

is truthful only when Main Street can establish complete Order coverage for the period and sufficient provenance to classify relevant commitments.

If several Orders have unresolved origin, Main Street MUST expose that uncertainty rather than allocate them arbitrarily among the known modes.

---

# 23. Capability Applicability

If a merchant does not use an applicable capability:

```text
measure not applicable
    ≠ measure value 0
```

Example:

A merchant with no Inventory capability SHALL NOT be presented as having:

```text
0 stock constraints
```

as though Main Street had assessed inventory.

---

# 24. `business-intelligence / order-commitment-count@1`

This measure counts distinct authoritative Order identities whose original Order commitment becomes authoritative within the observation window.

Conceptually:

```text
COUNT DISTINCT OrderIdentity
WHERE
    OrderEstablishmentBusinessTime
        ∈ [start, end)
```

Each Order contributes at most one count.

The following do not multiply it:

```text
Order amendment
partial release
Inventory Claim
Payment attempt
Payment Application
Shipment
Fulfilment portion
provider retry
```

A later authorised release/cancellation does not rewrite the historical fact that the original Order commitment was established.

This measure means:

> Orders committed during the stated period.

It MUST NOT be labelled:

```text
sales revenue
paid orders
fulfilled orders
profitable orders
successful orders
```

---

# 25. Order Interaction-Origin Dimension

Where sufficient exact provenance exists:

```text
business-intelligence /
order-commitment-count@1
```

MAY be partitioned by:

```text
ONLINE
WALK_IN
TELEPHONE
OTHER_REPRESENTED
UNRESOLVED
```

Example:

```text
Orders this month

Online        124
Walk-in        83
Telephone      21
Other           2
Unresolved      4
```

The partition MUST preserve the same total Order population.

If coverage is complete:

```text
total Orders
=
ONLINE
+
WALK_IN
+
TELEPHONE
+
OTHER_REPRESENTED
+
UNRESOLVED
```

No Order may appear in two origin classes for one commitment-establishment evaluation.

---

# 26. `business-intelligence / appointment-occurrence-outcome-count@1`

This measure counts distinct Appointment identities having an authoritative Appointment Occurrence Outcome whose applicable Appointment business-time anchor falls within the observation window.

Each qualifying Appointment contributes exactly once under its current authoritative occurrence-outcome dimension.

Applicable outcomes include:

```text
OCCURRED
PARTIAL_OCCURRENCE
CUSTOMER_NO_SHOW
MERCHANT_SIDE_NON_OCCURRENCE
```

Analytics SHALL NOT infer an occurrence outcome from:

```text
clock-in
Payment
calendar status
Notification delivery
customer message
provider event
AI inference
```

An Appointment with no authoritative occurrence outcome is not silently classified.

---

# 27. Appointment Interaction-Origin Dimension

The Appointment outcome measure MAY additionally be partitioned by Commitment Interaction Origin where sufficient source provenance exists.

Example:

```text
Appointments

                 Occurred  Partial  No-show
Online                82        2        6
Walk-in               19        1        0
Telephone             31        0        3
```

Interaction Origin describes how the Appointment commitment was established.

It does NOT change the later Appointment Occurrence Outcome.

Therefore:

```text
TELEPHONE
+
CUSTOMER_NO_SHOW
```

is a valid combined analytical dimension.

---

# 28. Appointment Corrections

Where Appointment authority corrects an occurrence outcome:

```text
old Analytical Observation
    remains historically true
    as the observation produced then

new evaluation
    uses the new authoritative
    occurrence outcome
```

The same principle applies if source-owned origin provenance is authoritatively corrected.

Analytics MUST NOT rewrite previous Analytical Observation provenance to pretend the corrected source state always existed.

---

# 29. `business-intelligence / booking-utilisation-outcome-count@1`

This measure counts distinct Booking identities having an authoritative Booking Utilisation Outcome whose applicable Booking utilisation/reservation business-time anchor falls within the observation window.

Each qualifying Booking contributes once under its current authoritative utilisation-outcome dimension.

Applicable dimensions include:

```text
UTILISED
PARTIALLY_UTILISED
CUSTOMER_NON_UTILISATION
MERCHANT_SIDE_NON_HONOUR
```

A Booking Natural Discharge does not itself manufacture utilisation.

This measure MUST NOT be called:

```text
utilisation rate
occupancy rate
capacity efficiency
```

because those denominators are not established here.

---

# 30. Booking Interaction-Origin Dimension

The Booking utilisation measure MAY additionally be partitioned by Commitment Interaction Origin where sufficient authoritative source provenance exists.

Example:

```text
Bookings utilised this month

Online         42
Walk-in         8
Telephone      17
Other           1
Unresolved      2
```

A Booking established online but paid in person remains:

```text
ONLINE
```

A Booking created by reception after a telephone call remains:

```text
TELEPHONE
```

A walk-in guest entered through the same reception UI remains:

```text
WALK_IN
```

---

# 31. Booking Corrections

Booking Utilisation Outcome correction follows the same historical-observation rule as Appointment correction.

Authoritative origin-provenance correction follows the same rule.

Later accepted source correction may change a newly evaluated measure.

It does not mutate old Analytical Observation provenance.

---

# 32. Payment Analytical Binding

`business-intelligence-analytics / payment-obligation-state@1` consumes only Payment-owned authoritative evidence required to determine:

```text
effective obligation amount
applied amount
outstanding obligation amount
current Due Evaluation
Currency
```

For one obligation:

```text
applied amount
=
sum(authoritative PaymentApplications)

outstanding amount
=
max(
    0,
    effective obligation amount
    -
    applied amount
)
```

Due Evaluation remains exactly:

```text
DUE
NOT_DUE
UNRESOLVED
```

Analytics SHALL NOT reinterpret provider status directly.

---

# 33. Payment Measures Are Not Partitioned by Mode of Sale Initially

The initial:

```text
outstanding-payment-obligation-count@1

outstanding-payment-obligation-value@1
```

SHALL NOT automatically inherit Commitment Interaction Origin.

Reason:

Payment is a separate capability and may relate to multiple commercial sources or later amendments.

Attaching Order/Appointment/Booking origin to Payment analytics would require an additional explicit cross-capability analytical relationship.

That relationship is not necessary to answer the initial merchant question:

> How do customers place Orders, Appointments and Bookings?

Therefore it is not admitted here.

---

# 34. `business-intelligence / outstanding-payment-obligation-count@1`

This measure counts exact Payment Obligations having:

```text
outstanding obligation amount > 0
```

at `evaluatedAt`.

The measure is dimensioned by:

```text
Currency
+
current Due Evaluation
```

An obligation with zero outstanding amount is not counted.

---

# 35. `business-intelligence / outstanding-payment-obligation-value@1`

This measure sums authoritative outstanding Payment Obligation value at `evaluatedAt`.

For each exact:

```text
Currency
+
Due Evaluation
```

the result is:

```text
SUM(outstanding obligation amount)
```

for qualifying obligations.

The measure MUST preserve `UNRESOLVED`.

---

# 36. Payment Meaning Boundary

Payment measures mean only:

> Represented Payment Obligations that remain unapplied in whole or part under accepted Payment semantics.

They MUST NOT automatically be described as:

```text
accounts receivable
revenue outstanding
cash expected
cash flow
sales
merchant income
bank balance
profit
```

---

# 37. Currency Rule

No value measure performs cross-currency aggregation.

Canonical:

```text
GBP 100
+
USD 200

≠

300 money
```

without separately accepted Currency Normalisation.

`MS-PROT-083-DQ-014` remains unresolved.

---

# 38. Inventory Analytical Binding

`business-intelligence-analytics / inventory-position-constraint@1` consumes exact current Inventory-owned evidence:

```text
established Inventory Position
stock on hand
remaining active Inventory Claims
quantity/unit compatibility
source scope
source revision/currentness
```

For one compatible Inventory Position:

```text
uncommitted quantity
=
stock on hand
-
sum(remaining active claim quantity)
```

This is analytical classification only.

---

# 39. Inventory Constraint Classification

Each established Inventory Position is classified into exactly one:

```text
UNCOMMITTED_QUANTITY_POSITIVE

FULLY_CLAIMED

KNOWN_ZERO_STOCK

ACTIVE_CLAIM_SHORTFALL
```

Predicates:

```text
UNCOMMITTED_QUANTITY_POSITIVE

stock on hand
>
remaining active claims
```

```text
FULLY_CLAIMED

stock on hand > 0
AND
stock on hand = remaining active claims
```

```text
KNOWN_ZERO_STOCK

stock on hand = 0
AND
remaining active claims = 0
```

```text
ACTIVE_CLAIM_SHORTFALL

stock on hand
<
remaining active claims
```

---

# 40. `business-intelligence / inventory-position-constraint-count@1`

This measure counts distinct established Inventory Positions by exact constraint state.

An Inventory Position never established is not:

```text
KNOWN_ZERO_STOCK
```

because:

```text
unestablished
    ≠ zero
```

This measure MUST NOT establish:

```text
public product availability
reorder need
stock health
customer reservation eligibility
future stock prediction
```

---

# 41. Workforce Analytical Binding

`business-intelligence-analytics / scheduled-work-commitment@1` consumes exact current Workforce Scheduling evidence:

```text
Merchant Membership
Workforce Scheduling Arrangement
ScheduledWorkCommitment identity
current authoritative commitment revision
commitment interval
MerchantScope
```

Only `ScheduledWorkCommitment` contributes.

---

# 42. `business-intelligence / scheduled-commitment-person-duration@1`

This measure answers:

> How much person-duration is currently represented by authoritative Scheduled Work Commitments inside this planning interval?

For:

```text
evaluatedAt
+
planning window [start, end)
```

the evaluator SHALL:

```text
1. obtain current authoritative
   ScheduledWorkCommitments;

2. intersect every commitment
   with [start, end);

3. group by exact Merchant Membership;

4. union overlapping intervals
   for the same Membership;

5. measure each Membership's
   resulting union duration;

6. sum durations across Memberships.
```

Conceptually:

```text
ScheduledPersonDuration
=
Σ Membership
    duration(
        UNION(
            commitment intervals
            ∩ planning window
        )
    )
```

---

# 43. Cross-Arrangement Overlap

Valid overlapping commitments for one Membership MUST NOT double-count person time.

Example:

```text
Arrangement A
09:00–13:00

Arrangement B
11:00–15:00
```

Result:

```text
6 hours
```

not:

```text
8 hours
```

---

# 44. Scheduled Break Boundary

The measure uses gross Scheduled Work Commitment interval.

Scheduled Break is not subtracted.

Therefore:

```text
scheduled person-duration
    ≠ approved worked time
    ≠ payable time
    ≠ productive time
    ≠ customer capacity
```

---

# 45. Leave Boundary

Approved Leave does not directly subtract from the measure.

If authoritative Scheduled Work still exists pending remediation, BI reports the authoritative current commitment and does not silently repair the conflict.

---

# 46. Workforce Individual-Scoring Prohibition

The portfolio does not establish:

```text
worker productivity
attendance scoring
performance ranking
labour efficiency
worker utilisation score
```

Scheduled person-duration is merchant-scope planning evidence.

---

# 47. No Cross-Measure Arithmetic by Default

The existence of two accepted measures does not authorise arithmetic between them.

Prohibited without another Measure Definition:

```text
Payment value
/
Order count
=
average order value
```

```text
Appointment OCCURRED
/
all Appointment outcomes
=
utilisation rate
```

```text
Orders
/
scheduled person-duration
=
worker productivity
```

---

# 48. No Universal Business Activity Total

Main Street SHALL NOT calculate:

```text
Orders
+
Appointments
+
Bookings
=
transactions
```

as a generic whole-business total.

A hybrid merchant may see each applicable measure together without pretending they are the same unit.

---

# 49. No Universal “Offline” Category

Main Street SHALL NOT collapse:

```text
WALK_IN
+
TELEPHONE
```

into a canonical generic:

```text
OFFLINE
```

within the source analytical model.

A presentation may visually group categories if it preserves the underlying exact values and does not alter semantic meaning.

Reason:

Telephone-assisted and in-person operations are materially different merchant operating patterns.

---

# 50. Currentness and Corrections

Each evaluation consumes source truth current under the applicable authority.

For correctable source meaning:

```text
new source correction
        ↓
future evaluation may differ
```

but:

```text
previous Analytical Observation
        remains immutable evidence
        of the previous evaluation
```

This applies equally to:

```text
occurrence outcome
utilisation outcome
interaction-origin provenance
```

where the source authority permits correction.

---

# 51. Source-Consistent Reads

Every general measure is deliberately defined primarily over one authoritative source capability.

Interaction Origin is carried as provenance of that same commitment-establishing operation rather than acquired through a separate retrospective analytics join.

Therefore adding the origin dimension does NOT introduce a generic cross-capability analytical transaction.

If sufficient source consistency cannot be achieved:

```text
result is unresolved
```

rather than fabricated.

---

# 52. Evaluation Retry and Idempotency

Repeated evaluation with the same exact:

```text
Measure Definition
MerchantScope
source scope
temporal boundary
evaluation affinity
source evidence/revisions
origin provenance
```

MUST NOT manufacture different semantic meaning merely because processing retried.

---

# 53. Data Lifecycle

The existence of a Measure Definition or origin dimension does not extend source-data retention or use authority.

Composite MS-PROT-053 remains authoritative.

Analytics MUST NOT retain unnecessary:

```text
telephone numbers
IP addresses
device fingerprints
message bodies
location traces
```

merely to support an interaction-origin classification once sufficient lawful source provenance exists.

---

# 54. Exposure and Access

Presentation of Analytical Observations remains subject to applicable:

```text
Actor Authorisation
Audience
Exposure
Data Protection
source relationship
```

authority.

This amendment creates no new BI audience or API surface.

---

# 55. Merchant-Facing Language

Merchant-facing presentation SHOULD use ordinary business terms.

Examples:

```text
Orders this month

Online        124
Walk-in        83
Telephone      21
Other           2
Unresolved      4
```

or:

```text
How customers booked

Online         61%
Telephone      27%
Walk-in        10%
Other           2%
```

A percentage presentation is permitted only as a presentation of the exact categorical partition where denominator coverage is complete.

It MUST NOT silently exclude `UNRESOLVED`.

If unresolved provenance exists, presentation must remain coverage-honest.

---

# 56. No Merchant KPI Configuration Console

A merchant SHALL NOT configure:

```text
formulas
joins
aggregation expressions
metric schemas
SQL
channel classification logic
```

for these standard measures.

Where merchant/staff assisted entry occurs, Main Street may require simple role-native origin capture only where the operation context does not already establish it.

Example:

```text
How did the customer contact you?

Online
Walk-in
Telephone
Other
```

should appear only where necessary.

Main Street SHOULD infer deterministic context where safe—for example, public customer self-service can already establish ONLINE—without asking redundant questions.

---

# 57. AI Boundary

AI MAY:

```text
explain a measure
summarise observations
translate analytical language
highlight an established change
```

AI MUST NOT:

```text
invent a Measure Definition
guess interaction origin
alter a formula
turn unknown into zero
combine currencies
infer revenue
infer productivity
infer business health
infer marketing causation
```

If authoritative provenance says only:

```text
staff entered Order
```

AI cannot decide:

```text
probably WALK_IN
```

and make that authoritative.

---

# 58. Business Health Boundary

This amendment does not decide whether any value is:

```text
good
bad
healthy
unhealthy
normal
abnormal
concerning
improving
deteriorating
```

Those semantics remain under:

```text
MS-PROT-083-DQ-002
```

Therefore:

```text
Measure
    ≠ Health Indicator
```

Likewise:

```text
70% of Orders are WALK_IN
```

does not mean:

```text
online presence is poor
```

without separately accepted Business Health reasoning.

---

# 59. Merchant Attention Boundary

A Measure or origin distribution does not automatically create Merchant Attention.

Example:

```text
TELEPHONE = 80%
```

does not itself authorise:

```text
Main Street should alert
the merchant to move online
```

That requires separately accepted analytical/health/recommendation authority.

---

# 60. Financial Operations Boundary

Payment measures do not reconstruct blocked MS-PROT-084 Financial Operations authority.

This amendment does not establish:

```text
Operating Cost
expense
income
revenue
profit
cash flow
financial statement
financial health
```

---

# 61. External Context Boundary

No initial general measure consumes:

```text
weather
local events
economic indicators
competitor data
industry benchmark
market trend
```

`MS-PROT-083-DQ-007` remains deferred.

---

# 62. Core Invariants

**BI-G01 — Source Truth Preservation**  
No Measure Definition becomes authoritative source business truth.

**BI-G02 — Exact Initial Portfolio**  
The v1.3 general portfolio contains exactly seven Measure Definition families.

**BI-G03 — No Familiar-Name Authority**  
A familiar KPI name creates no semantic authority.

**BI-G04 — Missing Is Not Zero**  
Incomplete required evidence MUST NOT be represented as numeric zero.

**BI-G05 — Non-Applicable Is Not Zero**  
Absence of a capability MUST NOT be represented as zero activity.

**BI-G06 — Exact Temporal Boundary**  
Every interval measure uses an explicit deterministic half-open observation window.

**BI-G07 — Currentness Provenance**  
Every observation preserves exact source affinity.

**BI-G08 — Correction Preservation**  
Later correction does not rewrite earlier Analytical Observation provenance.

**BI-G09 — Currency Separation**  
No cross-currency value aggregation exists in v1.3.

**BI-G10 — Payment Is Not Revenue**  
Payment measures do not create revenue, cash, profit or accounting truth.

**BI-G11 — Inventory Position Absence Is Not Zero**  
Unestablished Inventory is never known-zero stock.

**BI-G12 — Inventory Analytics Is Not Reservation**  
Inventory classification creates no Inventory Claim or customer availability authority.

**BI-G13 — Shift Is Not Worker Commitment**  
Only ScheduledWorkCommitment contributes to scheduled person-duration.

**BI-G14 — No Double-Counted Person Time**  
Overlapping commitments for one Membership are unioned.

**BI-G15 — Scheduled Duration Is Not Worked Time**  
Scheduled duration creates no attendance/pay/productivity truth.

**BI-G16 — No Cross-Measure Formula Inference**  
Arithmetic between accepted measures requires separately accepted semantics.

**BI-G17 — No Universal Transaction Count**  
Orders, Appointments and Bookings are not one generic transaction metric.

**BI-G18 — No Health Inference**  
A Measure alone creates no Business Health classification.

**BI-G19 — No Automatic Attention**  
An Analytical Observation alone creates no Merchant Attention obligation.

**BI-G20 — AI Non-Authority**  
AI cannot establish formulas, missing facts, origin, causation or thresholds.

**BI-G21 — Data-Lifecycle Preservation**  
Analytics does not extend source-data authority or retention.

**BI-G22 — Source-Provenance Origin**  
Commitment Interaction Origin derives only from authoritative commitment-establishment provenance governed by MS-PROT-059/applicable source authority.

**BI-G23 — Origin Is Not Identity**  
Interaction Origin never creates channel-specific Order, Appointment or Booking identities.

**BI-G24 — Origin Is Not Entry Surface**  
POS/dashboard/device choice does not establish WALK_IN, TELEPHONE or ONLINE origin by itself.

**BI-G25 — Origin Is Not Payment Method**  
Payment execution does not determine commitment origin.

**BI-G26 — Origin Is Not Fulfilment Method**  
Collection/delivery/physical use does not determine commitment origin.

**BI-G27 — Origin Is Not Marketing Attribution**  
Interaction Origin creates no acquisition or causal Marketing claim.

**BI-G28 — One Origin Per Commitment Establishment**  
One qualifying commitment belongs to exactly one initial origin class for one exact classification evaluation.

**BI-G29 — Unresolved Origin Is Explicit**  
Insufficient origin evidence becomes `UNRESOLVED`, never a guessed channel.

**BI-G30 — Other Is Explicitly Represented**  
A materially represented source channel outside the initial three may be `OTHER_REPRESENTED`; it MUST NOT be forced into an incorrect class.

**BI-G31 — No Universal Offline Collapse**  
WALK_IN and TELEPHONE remain distinct canonical classes.

**BI-G32 — No Payment-Origin Join Initially**  
Payment measures do not automatically inherit commerce interaction origin.

**BI-G33 — No Persistence Mandate**  
v1.3 does not decide analytical warehouse/time-series persistence.

**BI-G34 — No Financial-Operations Reconstruction**  
Payment analytics cannot fabricate missing MS-PROT-084 semantics.

---

# 63. Falsification

## F-01 — Order amendment

One Order is amended three times.

Expected:

```text
order-commitment-count = 1
```

**PASS**

## F-02 — Later Order release

Order established Monday and released Wednesday.

Historical establishment remains counted.

**PASS**

## F-03 — Online Order, in-store collection

Customer creates Order through public merchant website and later collects physically.

Expected:

```text
Origin = ONLINE
```

not WALK_IN.

**PASS**

## F-04 — Telephone Order entered through POS

Customer telephones merchant. Staff enters the Order through POS.

Expected:

```text
Origin = TELEPHONE
```

**PASS**

## F-05 — Walk-in Order entered through web dashboard

Customer is physically present. Staff records Order through browser dashboard.

Expected:

```text
Origin = WALK_IN
```

**PASS**

## F-06 — Staff-created Order with no origin evidence

Only evidence is:

```text
staff entered Order
```

Expected:

```text
Origin = UNRESOLVED
```

**PASS**

## F-07 — AI guesses walk-in

AI infers WALK_IN because merchant usually receives walk-ins.

Expected: rejected.

**PASS**

## F-08 — Website discovery then telephone Booking

Customer sees merchant website but telephones to make Booking.

Expected:

```text
Booking Origin = TELEPHONE
```

**PASS**

## F-09 — Online Booking, pay at property

Booking commitment established online, payment later occurs in person.

Expected:

```text
Origin = ONLINE
```

**PASS**

## F-10 — Walk-in Appointment

Customer enters salon without prior booking; staff establishes Appointment.

Expected:

```text
Origin = WALK_IN
```

**PASS**

## F-11 — Telephone Appointment no-show

Appointment created by telephone and later records `CUSTOMER_NO_SHOW`.

Expected:

```text
Origin = TELEPHONE
Outcome = CUSTOMER_NO_SHOW
```

Both dimensions remain independently truthful.

**PASS**

## F-12 — Different represented channel

Source provenance establishes a valid future/integration channel outside the initial three.

Expected:

```text
OTHER_REPRESENTED
```

rather than forced ONLINE.

**PASS**

## F-13 — Missing origin mixed with known origins

100 Orders exist:

```text
50 ONLINE
30 WALK_IN
10 TELEPHONE
10 UNRESOLVED
```

Expected:

No presentation may claim:

```text
50% online
37.5% walk-in
12.5% telephone
```

after silently dropping unresolved Orders.

**PASS**

## F-14 — Payment method confused with origin

Telephone Order paid through online payment link.

Expected:

```text
Order Origin = TELEPHONE
```

**PASS**

## F-15 — Delivery method confused with origin

Online Order delivered by merchant.

Expected:

```text
Origin = ONLINE
```

**PASS**

## F-16 — Campaign click before walk-in purchase

Customer clicks Campaign link but later walks into shop and establishes Order there.

Expected:

```text
Commitment Origin = WALK_IN
```

No Campaign conversion is inferred.

**PASS**

## F-17 — Payment provider says succeeded

No authoritative Payment Application.

Outstanding Payment is not silently reduced.

**PASS**

## F-18 — Partial Payment Application

£100 obligation receives £40 authoritative application.

Expected:

```text
outstanding = £60
```

**PASS**

## F-19 — Excess applied

£70 effective obligation has £100 applied.

Expected:

```text
outstanding = £0
```

No automatic credit interpretation.

**PASS**

## F-20 — Due status unresolved

Outstanding £200, Due Evaluation `UNRESOLVED`.

Expected: remains explicitly unresolved.

**PASS**

## F-21 — Multiple currencies

GBP and USD obligations.

Expected: separate observations.

**PASS**

## F-22 — Appointment clock evidence only

No authoritative Appointment Occurrence Outcome.

Expected: no occurred inference.

**PASS**

## F-23 — Appointment correction

No-show corrected to occurred.

Expected: future observation reflects correction; historical analytical evidence survives.

**PASS**

## F-24 — Booking discharged without utilisation outcome

Expected: no utilised inference.

**PASS**

## F-25 — Unestablished Inventory Position

Expected: not `KNOWN_ZERO_STOCK`.

**PASS**

## F-26 — Stock exceeds claims

```text
stock = 10
claims = 4
```

Expected:

`UNCOMMITTED_QUANTITY_POSITIVE`.

**PASS**

## F-27 — Stock exactly claimed

```text
stock = 10
claims = 10
```

Expected:

`FULLY_CLAIMED`.

**PASS**

## F-28 — Physical shortfall

```text
stock = 6
claims = 8
```

Expected:

`ACTIVE_CLAIM_SHORTFALL`.

No claim mutation.

**PASS**

## F-29 — One worker with overlapping Arrangements

```text
09:00–13:00
11:00–15:00
```

Expected:

```text
6 person-hours
```

not 8.

**PASS**

## F-30 — Scheduled break

Expected: gross commitment duration unchanged.

**PASS**

## F-31 — Approved Leave conflicts with existing commitment

Expected: BI does not silently repair source truth.

**PASS**

## F-32 — Merchant lacks Booking capability

Expected: Booking measure not applicable, not zero.

**PASS**

## F-33 — Incomplete historical coverage

Expected: unresolved/qualified, not fabricated zero.

**PASS**

## F-34 — Dashboard derives average order value

No accepted separate Measure Definition.

Expected: prohibited.

**PASS**

## F-35 — Dashboard sums Orders and Appointments

Expected: no generic transaction total.

**PASS**

## F-36 — BI labels Payment value revenue

Expected: prohibited.

**PASS**

## F-37 — BI says 80% telephone is unhealthy

Expected: prohibited pending Business Health authority.

**PASS**

## F-38 — BI recommends “move customers online”

Origin distribution exists but recommendation semantics have not established this action.

Expected: no automatic recommendation.

**PASS**

---

# 64. Ambiguity Review

**A01 — Does BI now own Orders?**  
No.

**A02 — Does Order count mean revenue?**  
No.

**A03 — Does BI own interaction source provenance?**  
No.

**A04 — Is `ONLINE` determined by online Payment?**  
No.

**A05 — Is a POS transaction necessarily WALK_IN?**  
No.

**A06 — Is a dashboard-created Appointment necessarily WALK_IN?**  
No.

**A07 — Does a telephone booking entered through dashboard remain TELEPHONE?**  
Yes.

**A08 — Does in-store collection change an ONLINE Order to WALK_IN?**  
No.

**A09 — Does website discovery make a later telephone commitment ONLINE?**  
No.

**A10 — Does mode of sale equal Marketing attribution?**  
No.

**A11 — Can AI infer missing interaction origin?**  
No.

**A12 — What happens when provenance exists but is outside the initial three?**  
`OTHER_REPRESENTED`.

**A13 — What happens when provenance is insufficient?**  
`UNRESOLVED`.

**A14 — Is `OTHER_REPRESENTED` the same as unknown?**  
No.

**A15 — Can WALK_IN and TELEPHONE be merged into OFFLINE canonically?**  
No.

**A16 — Are Payment measures partitioned by commitment origin?**  
Not in the initial portfolio.

**A17 — Does provider payment success alter Payment measures directly?**  
No.

**A18 — Is outstanding Payment value accounts receivable?**  
No.

**A19 — Can currencies be added?**  
No.

**A20 — Does missing Inventory mean zero stock?**  
No.

**A21 — Does Scheduled Work duration equal worked time?**  
No.

**A22 — Does a Measure create Business Health meaning?**  
No.

**A23 — Does a measure automatically enter Merchant Attention?**  
No.

**A24 — Does this create an analytics warehouse?**  
No.

**A25 — Does this resolve Financial Health?**  
No.

**A26 — Can AI define another KPI?**  
No.

**Ambiguity result:** PASS

---

# 65. Architecture Review

## 65.1 Semantic ownership

**PASS**

All business truth and interaction provenance remain at accepted source authorities.

## 65.2 Unified channel architecture

**PASS**

The amendment follows MS-PROT-059 rather than creating channel-specific business objects.

## 65.3 Analytical normalisation

**PASS**

A bounded five-value analytical classification permits useful comparison while retaining exact source provenance.

## 65.4 Cross-capability coupling

**PASS**

Origin is carried with commitment-establishment provenance and does not require retrospective joins across unrelated capabilities.

## 65.5 Administrative burden

**PASS**

Deterministically known channel context should be captured automatically. Merchant/staff input is required only where origin is otherwise unresolved.

## 65.6 Persistence

**PASS**

No analytics warehouse is introduced.

## 65.7 Privacy

**PASS**

Origin classification does not require retaining raw communication or behavioural tracking merely for analytics.

## 65.8 Financial semantic gravity

**PASS**

Mode of sale remains separate from Payment and accounting.

## 65.9 ERP drift

**PASS**

No generic channel analytics suite or customer journey system is created.

---

# 66. Fundamental Vision Conformance

## Business-to-Software Translation

**PASS**

A merchant can understand:

```text
Online
Walk-in
Telephone
```

without learning source-provenance semantics.

## Administrative Compression

**PASS**

Main Street automatically preserves known interaction context and eliminates spreadsheet reconciliation by sale/booking mode.

## Ordinary-Staff Training

**PASS**

Where staff input is required, the question can simply be:

```text
How did the customer contact you?
```

rather than exposing internal channel semantics.

## Role-Native Operation

**PASS**

Staff interact with business-language modes familiar from their work.

## Target-Market Proportionality

**PASS**

Three primary modes plus explicit Other/Unresolved cover the initial requirement without building omnichannel analytics.

## Capability Depth

**PASS**

The dimension is sufficiently precise to be useful across Orders, Appointments and Bookings.

## Ownership Versus Integration

**PASS**

MS-PROT-059/source capability owns provenance; BI owns only normalised analytical interpretation.

## Cross-Capability Value

**PASS**

The same bounded analytical dimension works across materially different commerce capabilities without merging their business commitments.

## Exception-Driven Operation

**PASS**

Origin is only manually requested where deterministic context does not already establish it.

## Business Evolution

**PASS**

Future interaction modes can remain `OTHER_REPRESENTED` until evidence justifies first-class analytical admission.

### Result

```text
VISION-CONFORMING
```

---

# 67. Trade-Offs

The design chooses:

```text
source provenance
+
small normalised analytical dimension
```

instead of:

```text
BI-owned salesChannel field
```

This introduces slightly more internal provenance discipline but prevents technical UI choice, Payment method, Fulfilment mode and Marketing attribution from becoming conflated.

It also chooses:

```text
ONLINE
WALK_IN
TELEPHONE
OTHER_REPRESENTED
UNRESOLVED
```

rather than exactly three exhaustive values.

That additional distinction is necessary because forcing every future or poorly evidenced interaction into the initial three would create false intelligence.

---

# 68. Future Scope

Later evidence may justify additional first-class origin classes such as a specific marketplace, messaging-commerce or integration-mediated mode.

Such additions require normal Feature Admission.

This section does not pre-approve them.

Likewise, future analysis might ask:

```text
Which mode has the highest average order value?

Which booking mode has the most no-shows?

Which mode produces more returning customers?
```

Those are **new cross-measure analytical propositions** and require separate Measure Definitions with exact denominators, source relationships and coverage.

They are not authorised merely because the underlying dimensions exist.

---

# 69. Effect on `MS-PROT-083-DQ-001`

Composite MS-PROT-083 then contains:

```text
v1.1
    Campaign Measure portfolio

v1.2
    Customer Return Measure portfolio

v1.3
    Initial General Business Intelligence
    Measure portfolio
    +
    bounded Commitment Interaction Origin
    analytical dimension
```

Together these provide the required initial Analytical Measure portfolio.

Therefore upon manual approval and formalisation:

```text
MS-PROT-083-DQ-001
    → RESOLVED
```

Future materially different measures require fresh Feature Admission rather than leaving DQ-001 permanently open.

---

# 70. Decisions Explicitly Not Resolved

This amendment does NOT resolve:

```text
MS-PROT-083-DQ-002
    Initial Business Health Indicator portfolio

MS-PROT-083-DQ-003
    analytical persistence/time-series architecture

MS-PROT-083-DQ-004
    initial Analytical Method portfolio

MS-PROT-083-DQ-005
    Method Qualification process

MS-PROT-083-DQ-006
    recommendation prioritisation

MS-PROT-083-DQ-007
    external context portfolio

MS-PROT-083-DQ-009
    merchant analytical surface

MS-PROT-083-DQ-010
    reports and exports

MS-PROT-083-DQ-011
    analytical retention/lifecycle portfolio

MS-PROT-083-DQ-012
    adaptive learning

MS-PROT-083-DQ-013
    cross-merchant benchmarking

MS-PROT-083-DQ-014
    Currency Normalisation

MS-PROT-083-DQ-015
    complete Financial Health composition
```

No MS-PROT-084 deferred decision is resolved.

---

# 71. Implementation Boundary

Approval authorises semantic/governance formalisation only.

It does NOT authorise:

```text
database migrations
origin fields
analytics tables
time-series storage
Java implementation
REST endpoints
dashboard implementation
charts
background aggregation
warehouse infrastructure
POS changes
merchant form changes
provider changes
production deployment
merchant activation
```

Implementation must follow the then-current `IMPLEMENTATION-RULES.md`.

---

# 72. Final Authority Statement

Main Street's initial general Business Intelligence portfolio SHALL remain a small set of exact, source-qualified operational measurements rather than a generic KPI framework.

The accepted Measure portfolio is exactly:

```text
business-intelligence /
order-commitment-count@1

business-intelligence /
appointment-occurrence-outcome-count@1

business-intelligence /
booking-utilisation-outcome-count@1

business-intelligence /
outstanding-payment-obligation-count@1

business-intelligence /
outstanding-payment-obligation-value@1

business-intelligence /
inventory-position-constraint-count@1

business-intelligence /
scheduled-commitment-person-duration@1
```

For Orders, Appointments and Bookings, Main Street SHALL additionally support the bounded analytical **Commitment Interaction Origin** dimension:

```text
ONLINE
WALK_IN
TELEPHONE
OTHER_REPRESENTED
UNRESOLVED
```

The dimension derives from authoritative commitment-establishment provenance under MS-PROT-059 and the applicable source capability.

It SHALL NOT be inferred from:

```text
POS usage
dashboard usage
Payment method
Fulfilment method
Marketing interaction
AI probability
```

A telephone transaction entered through POS remains `TELEPHONE`. An online Order collected in store remains `ONLINE`. A walk-in transaction entered through a browser dashboard remains `WALK_IN`.

Interaction Origin provides factual intelligence about **how customers transact with the merchant** without creating channel-specific business objects, Marketing attribution, customer journey analytics or a parallel BI-owned sale-channel authority.

Together with the accepted Campaign and Customer-Return Measure portfolios, this amendment completes the initial Analytical Measure portfolio and resolves `MS-PROT-083-DQ-001`.

```text
VISION CONFORMANCE:
VISION-CONFORMING

FALSIFICATION:
PASS

AMBIGUITY REVIEW:
PASS

ARCHITECTURE REVIEW:
PASS

RECOMMENDATION:
ACCEPT

MANUAL APPROVAL:
GRANTED — 10 September 2026

STATUS:
ACCEPTED

IMPLEMENTATION ACTIVATION:
NONE
```

### End of accepted MS-PROT-083 v1.3
