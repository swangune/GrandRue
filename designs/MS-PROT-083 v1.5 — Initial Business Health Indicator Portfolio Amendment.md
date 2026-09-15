# MS-PROT-083 v1.5 — Initial Business Health Indicator Portfolio Amendment

**Document ID:** MS-PROT-083  
**Version:** 1.5  
**Status:** ACCEPTED  
**Approved:** 15 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Business Intelligence semantic amendment  
**Design node:** `MS-PROT-083-DQ-002 — Initial Business Health Indicator portfolio`  
**Governed by:** `MS-DESIGN-RULES-001`; `DOCUMENT-GOVERNANCE.md`  
**Fundamental authority:** `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-083 through v1.4  
**Depends on:** composite MS-PROT-027; composite MS-PROT-053; composite MS-PROT-058; composite MS-PROT-083 through v1.4; MS-PROT-084 v1.1; composite MS-PROT-085  
**Resolves:** `MS-PROT-083-DQ-002`  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING

**Product identity note:** `Main Street` in this authority refers to the product currently named **GrandRue**. Stable `MS-*` authority identifiers, historical provenance and `mainstreet.*` implementation identifiers remain unchanged until a separately governed migration changes them.

---

# 0. Fundamental Vision Conformance

## 0.1 Outcome

**VISION-CONFORMING**

The purpose of Business Health is not to make the merchant administer a KPI framework.

The intended experience is:

```text
business operates
        ↓
Main Street observes governed evidence
        ↓
Main Street identifies a condition
whose business meaning is actually known
        ↓
Main Street explains the bounded concern
        ↓
merchant handles only what requires judgement
```

The merchant SHALL NOT be required to configure:

```text
KPI thresholds
traffic-light rules
metric weights
health-score formulas
seasonality models
statistical baselines
dashboard alert rules
```

merely to obtain initial Business Health support.

Where Main Street does not yet possess sufficient semantics to distinguish normal variation from material business concern, Main Street SHALL present the underlying governed analytical observation rather than manufacture a Business Health assessment.

## 0.2 Feature Admission

The amendment satisfies the **Coordination Test** because accepted analytical and Financial Health artifacts require one bounded composition into Business Health without transferring semantic ownership.

The amendment satisfies the **Administrative-Compression Test** because merchants should not have to convert raw operational exceptions into a self-administered health model.

The amendment deliberately rejects broader KPI-health classification where the accepted corpus does not yet establish truthful materiality.

---

# 1. Problem

Composite MS-PROT-083 defines:

```text
Analytical Observations
        ↓
Business Health Indicators
        ↓
Business Health Assessments
```

and establishes the canonical assessment vocabulary:

```text
NO_MATERIAL_CONCERN
MONITOR
MATERIAL_CONCERN
UNKNOWN
NOT_APPLICABLE
```

but does not establish the initial non-financial Business Health Indicator portfolio.

The accepted Measure portfolio now contains useful observations across:

```text
Orders
Appointments
Bookings
Payment
Inventory
Workforce Scheduling
Customer Return
Campaign execution
```

but the existence of a measurable quantity does not establish that Main Street knows what quantity, rate or change constitutes a material business concern.

The design problem is therefore:

> Which currently supported conditions may Main Street truthfully classify as Business Health without inventing unsupported thresholds, business-model assumptions or a universal score?

---

# 2. Governing Decision

The initial Business Health portfolio SHALL be deliberately narrow.

The initial directly MS-PROT-083-owned non-financial Business Health Indicator portfolio contains exactly one family:

```text
business-health/
inventory-claim-integrity@1
```

Business Health SHALL additionally compose the already accepted Financial Health assessments established through MS-PROT-084 v1.1.

The Financial Health indicators SHALL retain their existing identities:

```text
financial-health/current-due-commitment-coverage@1

financial-health/near-term-commitment-pressure@1

financial-health/overdue-receivable-pressure@1

financial-health/financing-arrears@1

financial-health/regulatory-financial-pressure@1
```

MS-PROT-083 v1.5 SHALL NOT duplicate, rename or reinterpret those Financial Health definitions.

Canonical composition:

```text
source-owned business facts
        ↓
registered Measure Definitions
        ↓
Analytical Observations
        ↓
business-health/inventory-claim-integrity@1
        +
accepted Financial Health assessments
        ↓
Business Health Profile
```

The Business Health Profile SHALL NOT produce a universal overall Business Health status or numeric score.

---

# 3. Ownership Boundary

Source capability ownership remains unchanged.

```text
Inventory
    owns stock position
    Inventory Claims
    claim resolutions
    Inventory scope
    stock-movement truth

Financial Operations /
applicable source capabilities
    own their accepted
    financial facts and evidence

Business Intelligence
    owns the exact derived
    Business Health interpretation

Merchant Attention
    owns handling coordination

Projection / Exposure
    owns read/presentation
    serviceability and exposure
```

A Business Health Assessment SHALL NOT mutate, discharge, correct or resolve its source condition.

Analytical interpretation SHALL NOT transfer source ownership.

---

# 4. Initial General Business Health Indicator Portfolio

The initial general non-financial portfolio is exactly:

```text
business-health/
inventory-claim-integrity@1
```

No other MS-PROT-083 general Business Health Indicator family is admitted by this amendment.

The small size of this portfolio is deliberate.

Main Street SHALL NOT enlarge the portfolio merely to produce symmetry across dashboard categories.

---

# 5. `business-health/inventory-claim-integrity@1`

## 5.1 Bounded Business Question

The Indicator answers only:

> Does currently represented authoritative Inventory contain any active Inventory Claim shortfall within the declared assessment scope?

It does not answer:

```text
Is inventory generally healthy?

Do we have enough stock?

Should the merchant reorder?

Will stock run out?

Is product availability good?

Is inventory efficiently managed?

Is the business overstocked?

Is the business understocked?
```

## 5.2 Analytical Dimension

The Indicator dimension is:

```text
INVENTORY_CLAIM_INTEGRITY
```

## 5.3 Required Measure

The Indicator consumes only the accepted:

```text
business-intelligence/
inventory-position-constraint-count@1
```

for the exact constraint dimension:

```text
ACTIVE_CLAIM_SHORTFALL
```

No independent stock arithmetic SHALL be introduced by the Indicator evaluator.

## 5.4 Reference Baseline

Reference baseline semantics are:

```text
NONE_CURRENT_STATE
```

This Indicator is a current-state integrity assessment.

It does not require:

```text
historical average
seasonal baseline
merchant target
peer benchmark
forecast
statistical method
```

## 5.5 Applicability

The Indicator returns:

```text
NOT_APPLICABLE
```

where accepted Merchant Configuration and Inventory semantics establish that Inventory is not applicable to the declared business/assessment scope.

Where Inventory is applicable, the absence of sufficient Inventory evidence SHALL NOT become `NOT_APPLICABLE`.

## 5.6 Evidence Sufficiency

A conclusive assessment requires an applicable current Analytical Observation whose evidence coverage is:

```text
COMPLETE_FOR_DECLARED_SCOPE
```

under the governing Measure Definition and its accepted Inventory bindings.

The assessment SHALL preserve:

```text
Merchant Scope

declared Inventory assessment scope

Measure Definition identity/version

evaluation time

source/currentness evidence

Inventory scope affinity

evidence coverage

known exclusions

source provenance
```

Where required completeness, currentness, position evidence, claim evidence or quantity/unit compatibility cannot be established:

```text
assessment = UNKNOWN
```

## 5.7 Assessment Semantics

Given sufficient current evidence:

```text
ACTIVE_CLAIM_SHORTFALL count > 0
        ↓
MATERIAL_CONCERN
```

and:

```text
ACTIVE_CLAIM_SHORTFALL count = 0
        ↓
NO_MATERIAL_CONCERN
```

The v1 Indicator does not emit:

```text
MONITOR
```

There is no trend or intermediate-risk interpretation in this definition.

## 5.8 Materiality Semantics

`ACTIVE_CLAIM_SHORTFALL` already means:

```text
authoritative stock on hand
<
remaining authoritative
active Inventory Claim quantity
```

Accepted Inventory authority establishes that this condition requires operational reconciliation and prohibits further incompatible claims.

Therefore materiality does not depend on an arbitrary KPI threshold.

The `MATERIAL_CONCERN` assessment means only:

> At least one represented Inventory position within the declared complete assessment scope currently has active claimed quantity exceeding recorded stock on hand.

It SHALL NOT be expanded into:

```text
the business is unhealthy

inventory management is poor

the merchant ordered too little stock

customers will definitely be affected

the business should reorder
```

without separately governed evidence.

## 5.9 Known Zero and Fully Claimed Stock

The following states SHALL NOT independently create a Business Health concern under this Indicator:

```text
KNOWN_ZERO_STOCK

FULLY_CLAIMED
```

because neither state, by itself, establishes an incompatible active claim.

Therefore:

```text
known zero stock
    ≠ Business Health concern
      under this Indicator
```

and:

```text
fully claimed stock
    ≠ Business Health concern
      under this Indicator
```

Other analytical or operational semantics may treat those facts differently for another accepted purpose.

---

# 6. Financial Health Composition

MS-PROT-084 v1.1 already establishes the initial Financial Health Indicator portfolio.

MS-PROT-083 v1.5 SHALL compose resulting Financial Health assessments into the Business Health Profile by reference.

Canonical:

```text
Financial Health assessment
        ↓
Business Health Profile
        as Financial Health material
```

Not:

```text
Financial Health assessment
        ↓
new duplicated
Business Health Indicator
```

The exact Financial Health identity, version, Currency partition, evidence lineage, governing time, evidence coverage and assessment SHALL remain intact.

A Financial Health `UNKNOWN` SHALL remain `UNKNOWN`.

A Financial Health `MATERIAL_CONCERN` SHALL remain a concern in its exact Financial Health dimension.

No weighting or conversion to a general Business Health score is permitted.

---

# 7. No Overall Business Health Status

The initial Business Health Profile SHALL NOT calculate:

```text
HEALTHY

UNHEALTHY

GOOD

POOR

RED

AMBER

GREEN

82 / 100

B+

HIGH RISK
```

as a merchant-wide authoritative Business Health result.

A merchant may simultaneously have:

```text
Inventory Claim Integrity
    NO_MATERIAL_CONCERN

Financial Health /
Overdue Receivable Pressure
    MONITOR

Financial Health /
Financing Arrears
    NOT_APPLICABLE
```

without Main Street collapsing those assessments into one synthetic result.

Severe issues SHALL NOT be masked by averaging with unrelated healthy dimensions.

---

# 8. Candidate Indicators Explicitly Not Admitted

The following are not admitted into the initial Business Health Indicator portfolio.

## 8.1 Demand / Order Volume Health

`order-commitment-count@1` is measurable.

However:

```text
lower Order count
    ≠ materially unhealthy demand
```

without accepted semantics for comparable periods, seasonality, expected demand and materiality.

No:

```text
sales are unhealthy
demand warning
business slowdown
```

assessment is authorised from Order counts alone.

## 8.2 Appointment / Booking Outcome Health

Appointment and Booking occurrence/utilisation outcomes are measurable.

However the corpus does not currently establish a universal threshold at which:

```text
CUSTOMER_NO_SHOW

MERCHANT_SIDE_NON_OCCURRENCE

CUSTOMER_NON_UTILISATION

MERCHANT_SIDE_NON_HONOUR
```

becomes an aggregate Business Health `MONITOR` or `MATERIAL_CONCERN`.

Individual source conditions may participate in separately governed Merchant Attention or operational handling.

They SHALL NOT acquire aggregate Business Health meaning through this amendment.

## 8.3 Customer Return Health

Accepted customer-return measures remain valid analytical observations.

They SHALL NOT become:

```text
retention health
loyalty health
churn health
```

because return frequency is materially business-model dependent.

`returning-customer-share@1` remains distinct from a universal retention rate.

## 8.4 Workforce Coverage Health

`scheduled-commitment-person-duration@1` establishes represented scheduled person-duration.

It does not establish required labour demand.

Therefore:

```text
scheduled person-duration
    ≠ adequate staffing
```

and no initial Workforce Health Indicator is admitted.

## 8.5 Campaign Health

Campaign measures establish bounded execution evidence.

They do not establish commercial success, customer engagement quality, Campaign ROI or business growth.

No Campaign Business Health Indicator is admitted.

## 8.6 General Stock Health

`inventory-position-constraint-count@1` does not establish generic stock health.

In particular:

```text
KNOWN_ZERO_STOCK
```

may be intentional, temporary, expected or irrelevant to current merchant policy.

No:

```text
low stock health
stockout health
inventory turnover health
```

Indicator is admitted.

---

# 9. Future Indicator Admission

A future Business Health Indicator requires normal Feature Admission.

A proposed future Indicator SHALL establish, at minimum:

```text
exact business question

applicability predicate

required Measure Definitions

reference-baseline semantics

evidence-sufficiency rule

comparison semantics where applicable

assessment semantics

uncertainty semantics

materiality semantics

business-model dependence

source/currentness requirements
```

A future Indicator MUST NOT use familiarity of a KPI as justification.

Where materiality requires statistical, probabilistic or forecasting machinery, the proposal SHALL also satisfy the applicable MS-PROT-083 Analytical Method and Method Qualification authorities before production use.

---

# 10. Business Health Profile

A Business Health Profile remains a DERIVED analytical projection.

Each profile entry SHALL preserve the exact:

```text
Indicator identity/version

assessment

Merchant Scope

subject/assessment scope

evaluation/governing time

required observation references

evidence coverage

known exclusions

uncertainty

currentness
```

The Profile SHALL NOT manufacture new analytical meaning from presentation order, colour, grouping or summarisation.

The initial Profile MAY contain:

```text
business-health/
inventory-claim-integrity@1
```

where applicable, together with applicable Financial Health assessments supplied under MS-PROT-084 v1.1.

Absence of another Business Health dimension SHALL NOT be presented as evidence that the corresponding part of the business is healthy.

---

# 11. Merchant Analytical Surface

Composite MS-PROT-083 v1.4 remains authoritative for presentation.

A Business Health Assessment may be exposed only through governed analytical presentation.

For `inventory-claim-integrity@1`, merchant-facing language SHALL communicate the bounded condition.

Permissible meaning includes:

```text
Reserved stock exceeds recorded stock
for one or more inventory positions.
```

It SHALL NOT broaden the statement to:

```text
Your inventory is unhealthy.

Your business is in poor health.

You need to buy more stock.
```

without separate accepted authority.

Material coverage or currentness qualification SHALL remain visible where omission would mislead the merchant.

Colour MAY supplement presentation but SHALL NOT create semantic meaning.

---

# 12. Merchant Attention Boundary

This amendment creates no new Attention Contract.

Canonical:

```text
Business Health Assessment
    describes analytical condition

Merchant Attention
    determines whether and how
    human handling is coordinated
```

A `MATERIAL_CONCERN` SHALL NOT automatically:

```text
create a Merchant Attention Occurrence

send a Notification

assign a worker

create a task

execute a corrective operation
```

A future Attention integration requires an accepted registered MS-PROT-085 Attention Contract or another already accepted source-specific contract.

---

# 13. Correction and Currentness

A Business Health Assessment is bound to the exact Analytical Evaluation Context under which it was produced.

If authoritative source facts are later corrected:

```text
historical assessment
    remains historical analytical evidence

new evaluation
    uses current corrected source facts
```

The analytical layer SHALL NOT rewrite historical source truth.

A current merchant surface SHALL NOT present a stale historical assessment as current where the Indicator's currentness requirements no longer hold.

---

# 14. Persistence Boundary

This amendment creates no new analytical persistence or time-series architecture.

Request-scoped evaluation remains sufficient for the semantics established here.

`MS-PROT-083-DQ-003 — Analytical persistence / time-series architecture` remains deferred.

No implementation may use this amendment as authority to introduce:

```text
analytics warehouse
Business Health snapshot store
time-series database
materialised health history
```

unless independently justified by accepted implementation/design authority.

---

# 15. AI Boundary

AI MAY:

```text
explain a governed Business Health Assessment

translate its evidence into ordinary business language

explain why an assessment is UNKNOWN

help the merchant navigate to supporting detail
```

AI SHALL NOT:

```text
create an Indicator Definition

invent a threshold

change an assessment

infer missing stock

override UNKNOWN

convert a KPI into Business Health

produce an overall health score

create Merchant Attention

execute corrective action
```

The deterministic registered Indicator Definition remains authoritative.

---

# 16. Commercial Entitlement and Authorisation

Business Health semantic applicability is distinct from:

```text
Commercial Entitlement

Actor Authorisation

Projection serviceability

Surface Exposure
```

Commercial packaging MAY govern whether an eligible analytical presentation is exposed.

It SHALL NOT change the Business Health assessment itself.

A hidden or commercially unavailable assessment does not acquire a different semantic result.

---

# 17. Alternatives Considered

## 17.1 Broad KPI Health Portfolio

Rejected.

A portfolio covering:

```text
Demand
Customer continuity
Appointment health
Booking health
Workforce coverage
Campaign performance
Inventory
Financial Health
```

would appear comprehensive but would require unsupported materiality rules for several dimensions.

Feature breadth is not sufficient justification.

## 17.2 Universal Red/Amber/Green or Numeric Score

Rejected.

It introduces arbitrary weighting, false precision and severe-issue masking and conflicts with existing MS-PROT-083 authority.

## 17.3 Merchant-Configured Health Thresholds

Rejected for the initial portfolio.

This would transfer analytics administration to the target merchant and undermine Administrative Compression.

Merchant policy MAY legitimately influence future business-specific indicators where the underlying business decision genuinely belongs to the merchant, but Main Street SHALL NOT require merchants to become KPI administrators merely because the platform lacks accepted semantics.

## 17.4 Automatically Learned Merchant Baselines

Deferred.

Adaptive baselines may eventually reduce business-model assumptions but introduce questions concerning:

```text
historical sufficiency
seasonality
structural change
statistical method
method qualification
false-positive control
explainability
```

Those questions are disproportionate to the initial portfolio and may activate DQ-004/DQ-005.

## 17.5 Continue Deferring All General Business Health

Rejected.

The accepted corpus already contains one bounded non-financial condition whose materiality follows directly from authoritative Inventory semantics, while Financial Health composition already exists.

Continuing total deferral would leave valid accepted analytical meaning unnecessarily unusable.

---

# 18. Falsification

## 18.1 Inventory-Tracked Retail Merchant

```text
stock on hand = 8
remaining active claims = 10
```

Expected:

```text
inventory-position constraint
    = ACTIVE_CLAIM_SHORTFALL

Business Health
    = MATERIAL_CONCERN
```

No source mutation occurs.

**PASS**

## 18.2 Physical Correction After Damage

```text
previous stock = 10
active claims = 8
physical correction = 6
```

Inventory authority preserves both current stock truth and active claims.

Expected:

```text
ACTIVE_CLAIM_SHORTFALL
    exists

Business Health
    = MATERIAL_CONCERN
```

The Indicator does not release claims or invent replenishment.

**PASS**

## 18.3 Fully Claimed Inventory

```text
stock on hand = 5
active claims = 5
```

Expected:

```text
FULLY_CLAIMED
```

but:

```text
inventory-claim-integrity
    = NO_MATERIAL_CONCERN
```

provided evidence coverage is complete.

The result says only that active claims do not exceed represented stock.

It does not say stock availability is healthy.

**PASS**

## 18.4 Known Zero With No Claims

```text
stock on hand = 0
active claims = 0
```

Expected:

```text
KNOWN_ZERO_STOCK

inventory-claim-integrity
    = NO_MATERIAL_CONCERN
```

for claim integrity only.

No reorder recommendation or generic stock warning is created.

**PASS**

## 18.5 Missing Inventory Evidence

Inventory applies, but required position or claim coverage cannot be established.

Expected:

```text
UNKNOWN
```

not:

```text
NO_MATERIAL_CONCERN
```

**PASS**

## 18.6 Non-Inventory Merchant

A solicitor uses appointments but no Inventory capability.

Expected:

```text
inventory-claim-integrity
    = NOT_APPLICABLE
```

No fake zero-stock assessment is displayed.

**PASS**

## 18.7 Hybrid Merchant

A salon provides services and also sells tracked products.

Inventory Claim Integrity evaluates the represented Inventory scope.

Customer-return and Appointment measures remain independently observable but do not become unsupported health judgments.

**PASS**

## 18.8 Seasonal Motel

The motel's returning-customer share falls materially during one period.

Expected:

```text
customer-return observation
    may be presented

Business Health concern
    NOT automatically created
```

because low repeat behaviour may be legitimate for the business model.

**PASS**

## 18.9 Falling Order Count

A merchant records 100 Orders last month and 60 this month.

Expected:

```text
comparison may be presented
where comparison semantics permit

Demand MATERIAL_CONCERN
    NOT established
```

without accepted seasonality/materiality semantics.

**PASS**

## 18.10 Low-Software-Capacity Sole Trader

The merchant is not asked to configure:

```text
warning thresholds
weighted scores
traffic-light rules
baseline periods
```

The platform surfaces only governed meaning it can establish.

**PASS**

## 18.11 Financial Health Composition

One Financial Health Indicator returns `MATERIAL_CONCERN`; another returns `UNKNOWN`; Inventory Claim Integrity returns `NO_MATERIAL_CONCERN`.

Expected:

```text
all three retain their exact results
```

with no averaging or universal overall status.

**PASS**

## 18.12 Merchant Attention Separation

Inventory Claim Integrity returns `MATERIAL_CONCERN`.

Expected:

```text
no Attention Occurrence
unless a separately accepted
Attention Contract applies
```

**PASS**

---

# 19. Hard Invariants

1. Business Health is derived analytical meaning, not source business truth.
2. Source capabilities retain their existing semantic ownership.
3. The initial directly MS-PROT-083-owned non-financial Indicator portfolio contains exactly `business-health/inventory-claim-integrity@1`.
4. Financial Health assessments compose by reference under their existing identities.
5. No universal Business Health score or overall Business Health status is authorised.
6. Measurability does not imply health-classification authority.
7. `ACTIVE_CLAIM_SHORTFALL > 0` under sufficient current evidence yields `MATERIAL_CONCERN`.
8. Zero active claim shortfalls under sufficient current evidence yields `NO_MATERIAL_CONCERN` for Inventory Claim Integrity only.
9. Insufficient applicable evidence yields `UNKNOWN`.
10. Inventory non-applicability yields `NOT_APPLICABLE`.
11. `KNOWN_ZERO_STOCK` and `FULLY_CLAIMED` do not independently create a Business Health concern under this Indicator.
12. The initial Inventory Claim Integrity Indicator does not emit `MONITOR`.
13. Customer-return metrics do not become retention-health semantics.
14. Order counts do not become demand-health semantics.
15. Appointment/Booking outcomes do not acquire aggregate health materiality without separate accepted authority.
16. Scheduled person-duration does not establish staffing adequacy.
17. Campaign execution evidence does not establish Campaign Business Health.
18. Business Health does not create Merchant Attention.
19. Business Health does not create Notifications or executable operations.
20. AI does not create or alter Business Health authority.
21. Commercial Entitlement controls access, not semantic assessment.
22. This amendment creates no analytical persistence architecture.
23. Historical assessments retain exact evaluation/source affinity.
24. Future Indicator families require normal Feature Admission.
25. Acceptance of this amendment does not activate implementation.

---

# 20. Deferred-Decision Disposition

Upon explicit manual approval and conforming repository formalisation:

```text
MS-PROT-083-DQ-002
Initial Business Health Indicator portfolio
    → RESOLVED
```

The following remain unresolved and are not activated by this amendment:

```text
MS-PROT-083-DQ-003
Analytical persistence / time-series architecture

MS-PROT-083-DQ-004
Initial Analytical Method portfolio

MS-PROT-083-DQ-005
Method Qualification operational process

MS-PROT-083-DQ-006
Recommendation prioritisation

MS-PROT-083-DQ-007
External context portfolio
```

Any additional currently deferred MS-PROT-083 decision remains unchanged unless expressly amended by another accepted authority.

---

# 21. Governance Outcome

**Fundamental Vision Conformance:** VISION-CONFORMING  
**Administrative Compression:** PASS  
**Semantic ownership:** PASS  
**Business-type neutrality:** PASS  
**Anti-ERP proportionality:** PASS  
**Undefined-metric protection:** PASS  
**Materiality precision:** PASS  
**Financial Health composition:** PASS  
**Merchant Attention isolation:** PASS  
**AI non-authority:** PASS  
**Projection/presentation separation:** PASS  
**Persistence non-promotion:** PASS  
**Cross-domain falsification:** PASS  
**Low-software-capacity merchant falsification:** PASS  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED  
**Repository formalisation:** AUTHORISED

---

# 22. Acceptance Statement

MS-PROT-083 v1.5 establishes the initial Business Health Indicator portfolio using only analytical conditions whose materiality can presently be established without arbitrary cross-business thresholds.

It deliberately prefers:

```text
small truthful portfolio
```

over:

```text
broad speculative dashboard
```

and preserves ordinary analytical observations where Main Street can measure a business condition but cannot yet truthfully judge that condition as healthy or unhealthy.

> **Main Street should classify business health only where it knows what the evidence means—not merely because it can calculate a number.**
