# MS-PROT-083 — Analytical Measurement, Business Health & Operational Decision Support Model

**Document ID:** MS-PROT-083  
**Version:** 1.0  
**Status:** ACCEPTED  
**Approved:** 8 September 2026 by explicit manual approval  
**Authority type:** Cross-capability analytical semantic/design authority  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`

**Depends on:** composite MS-PROT-026; composite MS-PROT-027; composite MS-PROT-043; MS-PROT-054; composite MS-PROT-053; composite MS-PROT-055; composite MS-PROT-057; MS-PROT-059; applicable authorisation/execution authorities; composite MS-PROT-068; MS-PROT-072; MS-PROT-082; and applicable accepted semantic owners supplying analytical evidence.

**Amends:** None  
**Supersedes:** None

**Narrows:** `MS-PROT-082-DQ-007` by establishing the analytical-measurement, Business Health, diagnosis, forecasting, scenario-evaluation and operational-decision-support substrate. It does **not** resolve complete Financial Health / Financial Intelligence semantics.

**Preserves:** MS-PROT-043 Merchant Attention ownership; MS-PROT-057 AI non-authority; MS-PROT-068 Operational Health ownership; MS-PROT-027 Projection/Exposure authority; MS-PROT-053 data-use/lifecycle authority; source-capability semantic ownership; MS-PROT-082 regulatory authority.

**Purpose:** Define how Main Street converts capability-owned business evidence into trustworthy measurements, Business Health assessments, evidence-grounded analytical claims, qualified forecasts, hypothetical scenario evaluations, quantified impact estimates and non-authoritative business recommendations without turning analytics, AI or materialised analytical data into a second source of business truth.

---

# 0. Fundamental Vision Conformance

## 0.1 Outcome

**VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The architecture introduces considerable internal precision because trustworthy cross-capability business analysis requires:

- exact source meaning;
- temporal affinity;
- evidence coverage;
- cross-source consistency;
- metric versioning;
- uncertainty;
- statistical qualification;
- data-use authority;
- historical comparability; and
- recommendation revalidation.

That complexity SHALL remain primarily inside Main Street.

The merchant-facing objective is:

```text
business operates
        ↓
Main Street observes governed evidence
        ↓
Main Street measures what happened
        ↓
Main Street interprets material change
        ↓
Main Street identifies risk / opportunity
        ↓
Main Street explains what matters
        ↓
merchant decides only where judgement is required
```

The merchant SHALL NOT need to become a:

```text
data analyst
BI administrator
statistician
SQL user
dashboard designer
forecasting specialist
metric designer
AI prompt engineer
```

to understand their business.

---

## 0.2 Administrative Compression

Main Street SHOULD absorb, where safely supported:

```text
metric calculation
historical comparison
trend interpretation
evidence completeness
diagnostic analysis
forecasting
scenario evaluation
recommendation preparation
natural-language explanation
```

The merchant supplies ordinary business reality and judgement.

Main Street supplies analytical expertise.

---

## 0.3 Exception-Driven Operation

Business Intelligence SHOULD increasingly support the merchant question:

> **What needs my attention, why, and what should I consider doing?**

It MUST NOT force the merchant to browse a large analytics application merely to discover an important exception.

This does not transfer Merchant Attention ownership from MS-PROT-043.

---

## 0.4 Target-Market Proportionality

The target is micro and small businesses.

MS-PROT-083 therefore does NOT justify:

```text
enterprise BI platforms
merchant-authored analytics DSLs
arbitrary dashboard builders
large mandatory data warehouses
general-purpose data science workbenches
enterprise workforce-ranking systems
```

unless later evidence establishes a materially necessary target-business requirement.

---

## 0.5 Feature Admission

MS-PROT-083 satisfies:

**Coordination Test** — Main Street's business value depends materially on interpreting facts across already independent capabilities.

**Administrative-Compression Test** — without governed intelligence, the merchant must manually reconcile reports, calculate metrics and interpret operational signals.

It does not require an additional merchant-facing software module merely to satisfy these tests.

---

# 1. Governing Decision

Main Street SHALL implement Business Intelligence as a **derived analytical and operational-decision-support capability** over already authoritative business evidence.

Canonical:

```text
AUTHORITATIVE BUSINESS FACTS
        ↓
accepted owner-qualified
query / event / projection contracts
        ↓
Analytical Input Bindings
        ↓
Analytical Evaluation Context
        ↓
Analytical Measure Definitions
        ↓
Analytical Observations
        ↓
Business Health Indicators
        ↓
Analytical Claims
        ↓
Forecasts / Scenario Evaluations /
Impact Estimates where justified
        ↓
Business Recommendations
        ↓
Merchant Attention where applicable
        ↓
merchant review / instruction
        ↓
normal application operation
        ↓
capability-owned execution
```

Business Intelligence SHALL NOT become:

```text
BusinessState
MerchantBrain
UniversalBusinessAggregate
UniversalMerchantLifecycle
UniversalDecisionEngine
```

---

# 2. Explicit Non-Goals

MS-PROT-083 does NOT establish a:

```text
general ledger
accounting ledger
accounts-payable capability
accounts-receivable capability
bank-account authority
cash-position authority
profit authority
cash-flow authority
tax engine
payroll engine
Financial Health authority
universal Business Health score
cross-merchant benchmarking network
data-warehouse mandate
machine-learning platform
worker-performance system
autonomous business optimiser
```

It does NOT own:

- Orders;
- Bookings;
- Appointments;
- Inventory;
- Payment;
- Workforce;
- Customers;
- Publication;
- Merchant Configuration;
- Regulatory Determinations;
- Regulatory Administrative Requirements;
- Provider Readiness;
- Merchant Attention; or
- platform Operational Health.

---

# 3. Analytical Ownership Boundary

Every business fact retains its existing semantic owner.

Business Intelligence owns only analytical meaning explicitly introduced through accepted analytical definitions.

Canonical:

```text
Payment
    owns Payment facts

Scheduling
    owns Appointment facts

Inventory
    owns Inventory facts

Workforce
    owns Workforce facts

JRA
    owns Regulatory Determinations

         ↓ references

Business Intelligence
    owns only defined
    analytical derivations
```

Observation does not transfer ownership.

Aggregation does not transfer ownership.

Materialisation does not transfer ownership.

---

# 4. Analytical Input Binding

An **Analytical Input Binding** identifies how one already accepted owner-qualified source contract may participate in a specific analytical purpose.

Conceptually:

```text
AnalyticalInputBinding
{
    bindingIdentity
    bindingVersion

    analyticalPurpose

    sourceSemanticOwner

    exactSourceContractIdentity
    exactSourceContractVersion

    permittedSourceElements

    governingTimeSemantics
    requiredProvenance
    currentnessRequirements

    dataUsePurpose

    coverageSemantics
}
```

The source MAY be an accepted:

```text
owner query
Domain Event Contract
Projection Contract
other registered owner-qualified
read/evidence contract
```

An Analytical Input Binding is not source authority.

---

# 5. Binding Immutability and Historical Affinity

A material change to:

```text
source contract
permitted source elements
analytical purpose
governing-time meaning
currentness requirement
coverage meaning
data-use purpose
```

SHALL require a new binding version.

Historical analytical artifacts MUST retain affinity to the exact binding version used.

An old observation MUST NOT later be interpreted using whichever source binding happens to be current.

---

# 6. No Parallel Analytical Source Authority

MS-PROT-083 deliberately rejects a universal `AnalyticalSourceContract`.

If an acceptable owner-qualified source contract does not exist:

```text
analytical use is not implementation-ready
```

Business Intelligence MUST NOT create a competing business-fact definition merely to make analysis convenient.

---

# 7. Analytical Measure Definition

Every material analytical metric SHALL have an explicit versioned **Analytical Measure Definition**.

Conceptually:

```text
AnalyticalMeasureDefinition
{
    measureIdentity
    measureVersion

    analyticalOwner

    inputBindings

    dependencyMeasures

    analyticalGrain
    dimensions

    timeBasis
    observationWindow

    unit

    calculationSemantics
    aggregationSemantics

    consistencyRequirement

    evidenceSufficiencyRequirement
    coverageRequirement

    comparisonSemantics

    correctionSemantics
    missingEvidenceSemantics

    currencySemantics where applicable

    dataUsePurpose
}
```

Charts, reports, AI prompts and APIs SHALL NOT define independent metric formulas.

---

# 8. Measure Ownership

Every Measure Definition SHALL have exactly one analytical semantic owner.

The owner MAY be:

```text
source capability
```

where the analytical meaning naturally belongs to one existing capability, or:

```text
Business Intelligence
```

where the metric has genuinely independent cross-capability analytical meaning.

Catalogue registration does not transfer ownership.

---

# 9. Undefined Metric Prohibition

Main Street MUST NOT display or answer with an undefined metric merely because its name is familiar.

High-risk examples include:

```text
revenue
profit
margin
cash flow
cash runway
conversion
customer value
retention
utilisation
productivity
growth
```

Canonical:

```text
familiar business term
        ≠
defined Main Street metric
```

If sufficient semantics do not exist, Main Street SHALL use the narrower supported measurement or say that the requested result cannot currently be established.

---

# 10. Measure Dependency Graph

A Measure Definition MAY depend upon other registered Measure Definitions.

For one exact analytical definition release, the dependency graph MUST be acyclic and deterministically resolvable.

Rejected:

```text
Measure A
    ↓
Measure B
    ↓
Measure C
    ↓
Measure A
```

No generic fixed-point analytical model is authorised by MS-PROT-083.

A detected cycle invalidates the affected definition set.

---

# 11. Analytical Evaluation Context

An **Analytical Evaluation Context** preserves the exact evidence context against which one analytical evaluation occurs.

It SHALL preserve enough information to determine:

- Merchant Scope;
- Measure Definition identity/version;
- exact Analytical Input Binding identities/versions;
- exact source contracts;
- source revisions/checkpoints/observation anchors where applicable;
- observation interval;
- currentness/freshness evidence;
- consistency evidence;
- current protected-data use-authority evidence where required;
- evidence coverage;
- evaluation time; and
- external-context affinity where applicable.

---

# 12. Multi-Source Consistency

A multi-source measure MUST NOT assume independently read source values formed one coherent business state merely because they were retrieved close together.

Every applicable Measure Definition SHALL specify an **Analytical Consistency Requirement**.

Conceptually:

```text
AnalyticalConsistencyRequirement
{
    requiredSourceSet

    acceptableAffinityPredicate

    maximumPermittedDivergence
        where applicable

    correctionHandling

    unresolvedConsequence
}
```

The requirement need not use one platform-wide algorithm.

It MUST, however, be deterministic.

Terms such as:

```text
roughly current
close enough
recent enough
```

are insufficient unless given exact meaning by the relevant definition.

---

# 13. Consistency Failure

If required analytical consistency cannot be established:

```text
evaluation = UNRESOLVED
```

or an owner-qualified semantically equivalent outcome SHALL result.

Main Street MUST NOT silently combine incompatible source snapshots.

---

# 14. Analytical Observation

An **Analytical Observation** is a DERIVED result produced from one exact Measure Definition under one exact Analytical Evaluation Context.

It SHALL retain:

```text
observationIdentity

measureIdentity/version

MerchantScope

dimensions

observationInterval

exact EvaluationContext

value/unit

evidenceCoverage

knownExclusions

createdAt
```

It is analytical evidence.

It is not source business truth.

---

# 15. Historical Measure Affinity

Historical Analytical Observations SHALL remain bound to the exact Measure Definition version under which they were produced.

Changing:

```text
formula
aggregation
comparison basis
time basis
coverage rules
```

does not rewrite historical analytical meaning.

---

# 16. Measure Compatibility

Versioning does not imply comparability.

Default:

```text
same Measure Definition version
        ↓
potentially comparable,
subject to its ordinary rules

different Measure Definition versions
        ↓
NOT COMPARABLE
```

Cross-version comparison is permitted only where:

1. an accepted compatibility rule establishes material semantic equivalence; or
2. both compared periods are recomputed under one common applicable Measure Definition.

The UI, reporting layer and AI layer MUST NOT bypass this requirement.

---

# 17. Evidence Coverage

Every material observation SHALL establish one of:

```text
COMPLETE_FOR_DECLARED_SCOPE
PARTIAL_KNOWN
COVERAGE_UNKNOWN
```

## 17.1 COMPLETE_FOR_DECLARED_SCOPE

All evidence required by the Measure Definition for the declared analytical scope has been established.

This does NOT automatically mean:

```text
complete for the merchant's
entire real-world business
```

## 17.2 PARTIAL_KNOWN

Main Street knows that material source activity is excluded.

## 17.3 COVERAGE_UNKNOWN

Main Street cannot establish whether material activity exists beyond the represented evidence.

---

# 18. Whole-Business Coverage

Whole-business coverage is itself a proposition requiring owner-qualified evidence.

Main Street MUST NOT infer:

```text
all Main Street-controlled channels
        =
all business activity
```

merely because Main Street sees all of its own systems.

Whole-business coverage may eventually be established from accepted evidence including, where applicable:

```text
merchant operating configuration
external financial/accounting integrations
provider/channel coverage
other accepted owner facts
```

If complete coverage cannot be established:

```text
whole-business claim prohibited
```

---

# 19. Missing Evidence Is Not Zero

Hard distinction:

```text
known zero
    ≠ no observation
    ≠ unavailable evidence
    ≠ partial evidence
    ≠ unknown
```

A missing row, unconnected provider or absent historical period MUST NOT silently become numeric zero.

---

# 20. Time Semantics

Every time-based Measure Definition SHALL define the applicable business time.

Examples:

```text
Order commitment time
Appointment scheduled interval
Appointment completion time
Payment Application time
Refund occurrence time
Provider Settlement time
Inventory Movement time
```

These are not interchangeable.

Server processing time MUST NOT silently replace defined business time.

---

# 21. Comparison Semantics

A comparative metric SHALL define its comparison basis.

Examples may include:

```text
same weekday over preceding comparable weeks
previous calendar month
same period previous year
previous equivalent trading period
```

Main Street SHALL NOT display:

> down 12%

without a truthful comparison definition.

Materially non-comparable periods yield an unresolved or explicitly qualified comparison.

---

# 22. Currency Semantics

Every monetary observation retains exact currency identity.

Main Street MUST NOT calculate:

```text
GBP 1,000
+
EUR 1,000
=
2,000
```

A cross-currency aggregate requires separately accepted currency-normalisation authority containing at least:

- rate source;
- rate time;
- currency pair;
- conversion convention; and
- provenance.

Until such authority exists:

```text
different currencies remain partitioned
```

---

# 23. Current Data-Use Authority

A Measure Definition permitting an analytical purpose does not itself establish that protected source data may currently be used.

Where required:

```text
Measure Definition permits purpose
        +
current Data Protection use authority
        ↓
source evidence eligible
```

If current use authority cannot be established:

```text
that protected source evidence
is unavailable to the evaluation
```

Retention does not imply analytical-use permission.

---

# 24. Derived Analytical Data Protection

Derived does not mean unrestricted.

Analytical Observations, Claims, Forecasts, Scenario Evaluations, Impact Estimates and Recommendations may themselves contain:

```text
personal information
worker information
customer information
financial information
commercially sensitive information
```

They SHALL receive applicable:

```text
protection classification
purpose/use authority
retention requirements
Exposure/access constraints
disposition handling
```

under accepted data-protection authority.

---

# 25. Business Health Indicator Definition

A **Business Health Indicator Definition** interprets one bounded business-health question.

Conceptually:

```text
BusinessHealthIndicatorDefinition
{
    indicatorIdentity
    version

    dimension

    applicabilityPredicate

    requiredMeasures

    referenceBaselineSemantics

    evidenceSufficiency

    interpretationRules

    assessmentSemantics

    uncertaintySemantics

    materialitySemantics
}
```

A Business Health Indicator is DERIVED analytical meaning.

It does not create source business facts.

---

# 26. Business Health Assessment

The canonical initial assessment vocabulary is:

```text
NO_MATERIAL_CONCERN
MONITOR
MATERIAL_CONCERN
UNKNOWN
NOT_APPLICABLE
```

## 26.1 NO_MATERIAL_CONCERN

Sufficient evidence exists and the Indicator Definition establishes no material concern.

## 26.2 MONITOR

Sufficient evidence identifies a developing condition worth monitoring but does not currently establish a material concern.

## 26.3 MATERIAL_CONCERN

Sufficient evidence establishes a materially unfavourable condition.

## 26.4 UNKNOWN

The Indicator applies, but sufficient evidence cannot be established.

## 26.5 NOT_APPLICABLE

The Indicator is not relevant to the merchant's current supported operating model.

---

# 27. Business Health Does Not Own Merchant Attention

A `MATERIAL_CONCERN` does not automatically mean:

```text
Merchant Attention = required
```

Canonical:

```text
Business Health Assessment
    describes analytical business condition

Merchant Attention
    determines current merchant
    handling/work visibility
```

MS-PROT-043 remains authoritative for Merchant Attention.

---

# 28. No Universal Business Health Score

MS-PROT-083 rejects a canonical:

```text
Business Health = 82 / 100
```

for v1.0.

A universal weighted score risks:

- arbitrary weighting;
- false precision;
- inappropriate cross-business comparison;
- severe-issue masking;
- missing-evidence distortion.

A future authority MAY propose a summary score only if evidence demonstrates truthful value beyond the indicator profile.

---

# 29. Business Health Profile

A **Business Health Profile** is a DERIVED projection of applicable Business Health Assessments.

Example:

```text
Demand               NO_MATERIAL_CONCERN
Capacity             MATERIAL_CONCERN
Customer continuity  MONITOR
Inventory            NO_MATERIAL_CONCERN
Workforce coverage   NO_MATERIAL_CONCERN
Financial Health     not currently governed
```

The profile is not a merchant lifecycle state.

---

# 30. Business Health vs Operational Health

Business Health and MS-PROT-068 Operational Health are different concepts.

Canonical:

```text
Business Health
    condition/performance of
    the merchant's business

Operational Health
    condition/diagnosability of
    Main Street platform/provider/
    background/projection infrastructure
```

Hard distinction:

```text
Business Health Indicator
    ≠ Operational Health Observation
    ≠ HealthSignal
    ≠ Provider Readiness
```

Bare `HealthSignal` SHALL NOT be used normatively for Business Health.

---

# 31. Analytical Claim

An **Analytical Claim** is one atomic analytical proposition supported by exact governed evidence.

Conceptually:

```text
AnalyticalClaim
{
    claimIdentity

    epistemicClass

    claimMeaning

    MerchantScope
    subjectScope

    supportingObservationReferences

    supportingClaimReferences
        where applicable

    methodContractReference
        where INFERRED

    methodQualificationReference
        where INFERRED

    uncertainty
        where applicable

    evidenceCoverage

    createdAt

    validity/currentnessConditions
}
```

---

# 32. Epistemic Classes

Every material Analytical Claim has exactly one of:

```text
OBSERVED
DERIVED
INFERRED
UNKNOWN
```

---

# 33. OBSERVED

An `OBSERVED` claim reports an exact Analytical Observation.

Example:

> Recorded Saturday appointment utilisation was 91%.

It does not explain why.

---

# 34. DERIVED

A `DERIVED` claim follows deterministically from accepted observations and/or other deterministic claims.

Example:

> Saturday utilisation was 24 percentage points higher than Tuesday utilisation over the selected comparable periods.

---

# 35. INFERRED

An `INFERRED` claim depends on a currently qualified Analytical Method.

Examples include:

```text
statistical association
diagnostic hypothesis
probabilistic classification
forecast proposition
```

An inferred claim MUST NOT be presented as observed fact.

---

# 36. UNKNOWN

`UNKNOWN` communicates that the proposition cannot currently be established from sufficient governed evidence.

Example:

> There is not enough evidence yet to determine whether customer retention is declining.

UNKNOWN is a valid result.

---

# 37. Business Insight

A **Business Insight** is a merchant-relevant composition of one or more Analytical Claims around one coherent business question.

An Insight MAY contain:

```text
OBSERVED
+
DERIVED
+
INFERRED
```

claims.

Each underlying claim retains its own epistemic class.

The Insight SHALL NOT acquire a blanket `OBSERVED` label merely because one component is observed.

---

# 38. Mechanistic Diagnosis

Main Street MAY make a causal/mechanistic statement where accepted authoritative semantics deterministically establish the chain.

Example:

```text
approved worker leave
        ↓
authoritative worker availability change
        ↓
registered worker/resource relationship
        ↓
schedulable capacity reduction
```

Where all relevant links are established, Main Street may truthfully explain that the capacity reduction resulted from the availability change.

---

# 39. Statistical Diagnosis

Where the relationship is statistical rather than mechanistically established, Main Street SHALL use associative or hypothesis language unless the applicable Analytical Method is explicitly qualified to support stronger causal inference.

Valid:

> Cancellations were higher among appointments booked less than 24 hours beforehand.

Not automatically valid:

> Short-notice bookings caused the cancellations.

---

# 40. Causal Claim Rule

Correlation does not establish causation.

An AI model's reasoning or confidence does not establish causation.

A statistical causal claim requires an Analytical Method Contract and current Method Qualification explicitly permitting causal claim semantics for the applicable data regime.

---

# 41. Analytical Method Contract

Every nontrivial statistical/probabilistic method producing production `INFERRED` claims, Forecasts or probabilistic Impact Estimates SHALL operate under a versioned **Analytical Method Contract**.

Conceptually:

```text
AnalyticalMethodContract
{
    methodIdentity
    methodVersion

    analyticalPurpose

    permittedClaimKinds

    inputDefinitions

    applicableMerchantDataRegime

    minimumEvidenceRequirements

    trainingCalibrationProvenance
        where applicable

    validationRequirements

    uncertaintyContract

    driftInvalidationRules

    outputContract

    prohibitedInterpretations
}
```

The contract describes what the method claims to do.

It does not qualify itself.

---

# 42. Analytical Method Qualification

An **Analytical Method Qualification** is platform evidence that one exact Method Contract version may currently be used for specified analytical purposes.

Conceptually:

```text
AnalyticalMethodQualification
{
    qualificationIdentity

    exactMethodIdentity
    exactMethodVersion

    permittedAnalyticalPurposes
    permittedClaimKinds

    applicableDataRegime

    validationEvidence

    minimumPerformanceEvidence

    qualifiedFrom

    assuranceUntilOrReviewCondition

    qualificationAuthority

    invalidationEvidence
        where applicable
}
```

---

# 43. Method Qualification Rule

A production probabilistic output requires current qualification covering:

```text
exact method
+
exact method version
+
requested analytical purpose
+
claim kind
+
applicable data regime
```

The following are insufficient:

```text
method produces an answer
model says confidence is high
algorithm is industry-standard
provider calls model "accurate"
```

---

# 44. Method Qualification Can Expire

Qualification is not permanent.

Material invalidation conditions may include:

```text
input distribution change
calibration degradation
underlying model/provider change
validation assumptions no longer holding
material method defect
```

Therefore:

```text
Method Contract exists
    ≠
Method currently qualified
```

Where current qualification cannot be established:

```text
new INFERRED output prohibited
```

Deterministic analytical observations may continue.

---

# 45. AI Inference Contract Composition

`Analytical Method Contract` and `AI Inference Contract` govern different concerns.

```text
Analytical Method Contract
    analytical/statistical validity

AI Inference Contract
    bounded AI invocation,
    context, validation and safety
```

Where an analytical method uses AI inference:

```text
BOTH authorities apply
```

Neither substitutes for the other.

---

# 46. Sparse-Data Rule

Main Street serves businesses that may have:

```text
little history
low transaction volume
strong seasonality
partial channel coverage
```

When a Method Contract's minimum evidence cannot be satisfied:

```text
INSUFFICIENT_EVIDENCE
```

or a semantically equivalent explicit failure SHALL result.

Main Street SHOULD prefer:

> There isn't enough history yet.

over fabricated predictive precision.

---

# 47. Business Forecast

A **Business Forecast** estimates a future analytical measure or condition.

It SHALL retain:

- forecast identity;
- target measure;
- forecast horizon;
- exact Method Contract;
- exact current Method Qualification;
- exact input observations/evaluation contexts;
- estimate/range;
- uncertainty;
- assumptions;
- evidence coverage;
- generatedAt; and
- invalidation/currentness requirements.

A Forecast is not future business truth.

---

# 48. Impact Estimate

An **Impact Estimate** quantifies a possible consequence of an observed condition, forecast or hypothetical action.

Examples include:

```text
estimated lost demand
estimated additional bookings
estimated capacity effect
estimated monetary effect
```

A material Impact Estimate requires:

```text
defined Analytical Measures
+
qualified Analytical Method
```

where probabilistic estimation is involved.

AI MUST NOT invent quantitative impact.

If no governed method supports the number:

```text
omit the number
```

---

# 49. Scenario Evaluation

A **Scenario Evaluation** evaluates an explicitly hypothetical set of business assumptions.

Conceptually:

```text
ScenarioEvaluation
{
    scenarioIdentity

    baselineEvaluationContext

    hypotheticalAssumptions
    assumptionProvenance

    affectedMeasureDefinitions

    methodContracts
        where applicable

    methodQualifications
        where applicable

    scenarioObservations

    ImpactEstimates

    uncertainty
    coverage

    createdAt
}
```

---

# 50. Scenario Is Not Forecast

Hard distinction:

```text
Forecast
    what may happen under defined
    current/baseline assumptions

Scenario Evaluation
    what the analytical model estimates
    under explicitly hypothetical assumptions
```

They MUST NOT be conflated.

---

# 51. Scenario Assumptions Are Not Business State

Canonical:

```text
Scenario assumption
    ≠ Merchant Configuration
    ≠ Merchant Intent
    ≠ Command
    ≠ future commitment
```

Merchant:

> What happens if I open Sundays?

does not instruct Main Street to change opening hours.

Correct:

```text
merchant question
        ↓
hypothetical assumption
        ↓
Scenario Evaluation
        ↓
merchant reviews result
```

Only a subsequent explicit instruction may enter normal Reconfiguration.

---

# 52. Financial Analytical Boundary

MS-PROT-083 MAY analyse monetary facts only according to their already accepted meaning.

Hard distinctions include:

```text
Order committed amount
    ≠ Payment Application

Payment Application
    ≠ Provider Settlement

Provider Settlement
    ≠ Revenue

Revenue
    ≠ Profit
```

MS-PROT-083 SHALL NOT manufacture:

```text
profit
gross margin
net margin
cash flow
cash runway
free cash
complete Financial Health
```

from nearby operational facts.

---

# 53. Financial Health Boundary

Complete Financial Health requires additional authoritative semantics that may include:

```text
business expense
supplier payable
customer receivable
bank/cash position
debt commitment
recurring operating cost
capital expenditure
external-channel financial evidence
accounting classification
```

MS-PROT-083 does not establish those facts.

A separate material Financial Operations / Financial Health authority is required.

---

# 54. JRA Boundary

MS-PROT-082 Regulatory Determinations MAY be consumed as analytical evidence where authorised.

Business Intelligence SHALL NOT:

- recalculate JRA rules independently;
- reinterpret regulatory law;
- override a Regulatory Determination;
- change jurisdiction applicability; or
- convert regulatory uncertainty into analytical certainty.

JRA retains regulatory truth.

---

# 55. Business Recommendation

A **Business Recommendation** is a non-authoritative decision-support artifact suggesting that the merchant consider an action.

A material Recommendation SHALL retain:

```text
recommendationIdentity

recommendationDefinition/version

businessQuestion

supportingAnalyticalClaims

supportingForecasts
supportingScenarioEvaluations
supportingImpactEstimates
    where applicable

evidenceCoverage

rationale

expectedBenefit
    where governed

materialDownsideTradeOff
    where known

uncertainty

candidateActionFamily

premiseRevalidationRequirements

createdAt
```

---

# 56. Recommendation Definition

A **Recommendation Definition** establishes the semantics of a recurring or consequential recommendation family.

It SHALL identify:

- required analytical premises;
- minimum evidence;
- permitted recommendation meaning;
- eligible operation family where applicable;
- required merchant decisions;
- premise-revalidation rules;
- permitted Impact Estimate semantics;
- material trade-off/risk requirements; and
- prohibited automated effects.

---

# 57. When Recommendation Definition Is Mandatory

A Recommendation Definition is REQUIRED when a recommendation:

- may contribute to Merchant Attention;
- contains a quantified Impact Estimate;
- references a Main Street operation;
- identifies an external/professional action;
- is retained for later outcome evaluation; or
- can materially affect a merchant business decision.

Ungoverned AI advice SHALL NOT become a first-class `Business Recommendation`.

---

# 58. Recommendation Rationale Must Be Evidence-Grounded

Every material:

```text
factual
diagnostic
causal
predictive
quantitative
```

proposition in a Recommendation rationale SHALL resolve to a governed Analytical Claim, Forecast, Scenario Evaluation or Impact Estimate.

Free-text wording is presentation.

The analytical propositions are governed evidence.

---

# 59. Recommendation Is Not Authority

Hard distinction:

```text
Business Recommendation
    ≠ Merchant Intent
    ≠ Actor Authorisation
    ≠ Commercial Entitlement
    ≠ Operational Eligibility
    ≠ Command
    ≠ Provider authority
```

Business Intelligence recommends.

It does not execute.

---

# 60. Recommendation Premise Assessment

Before a retained Recommendation is represented as currently actionable, Main Street SHALL evaluate its analytical premises.

The canonical result is:

```text
PREMISES_CURRENT
PREMISES_STALE
PREMISES_UNRESOLVED
```

---

# 61. PREMISES_CURRENT

`PREMISES_CURRENT` means required evidence has been revalidated according to the Recommendation Definition, including where applicable:

```text
source evidence current enough
Measure Definitions still applicable
Method Qualification still current
external context still suitable
Impact Estimate premises still valid
```

It does NOT mean the recommended operation itself is executable.

---

# 62. PREMISES_STALE

`PREMISES_STALE` means material analytical premises have changed sufficiently that the retained Recommendation is no longer represented as current advice.

The Recommendation may remain historical evidence.

---

# 63. PREMISES_UNRESOLVED

`PREMISES_UNRESOLVED` means Main Street cannot establish whether the material analytical premises remain sufficiently current.

It MUST NOT be treated as `PREMISES_CURRENT`.

---

# 64. Recommendation Premises vs Execution Eligibility

Hard separation:

```text
Recommendation Premise Assessment
    Are the analytical premises still current?

Projection Serviceability
    May this representation currently be served?

Actor Authorisation
    May this actor request the action?

Commercial Entitlement
    Is the functionality commercially permitted?

Operational Eligibility
    May the operation proceed under current
    capability/runtime facts?
```

No one result substitutes for another.

---

# 65. Merchant Approval and Execution

Canonical:

```text
Recommendation
        ↓
merchant reviews
        ↓
merchant accepts / modifies
        ↓
NEW MERCHANT INSTRUCTION
        ↓
normal application operation
        ↓
current authorisation
current entitlement
current provider readiness
current capability state
current policy/invariants
        ↓
capability-owned execution
```

Accepting a Recommendation does not freeze yesterday's business state.

---

# 66. No Autonomous Business Optimisation

MS-PROT-083 does NOT authorise:

```text
automatic price changes
automatic shift changes
automatic opening-hour changes
automatic campaign sending
automatic customer targeting
automatic inventory purchasing
automatic money movement
```

solely because Business Intelligence recommends them.

A future delegated/autonomous-action authority would require separate governance for:

```text
delegated authority
action classes
limits
revocation
risk controls
monitoring
recovery
merchant controls
```

---

# 67. Recommendation Outcome Link

Main Street MAY retain a **Recommendation Outcome Link** connecting:

```text
Recommendation
        ↓
merchant instruction
        ↓
application request
        ↓
authoritative resulting facts
        ↓
later Analytical Observations
```

This proves traceability.

It does NOT itself establish:

```text
Recommendation caused outcome
```

---

# 68. Learning Boundary

`LEARN` means Main Street MAY use governed historical recommendation/outcome evidence to evaluate or improve future decision support where applicable authority permits.

It does NOT automatically authorise:

```text
foundation-model training
cross-merchant model learning
self-modifying metric formulas
self-modifying business policy
automatic semantic mutation
automatic recommendation-policy mutation
```

AI and Data Protection authorities remain applicable.

---

# 69. Merchant Attention Boundary

MS-PROT-043 remains authoritative for merchant work/attention.

MS-PROT-083 SHALL NOT introduce a competing:

```text
DecisionSupportItem
BIInboxItem
AnalyticsAttentionItem
```

authority.

Canonical:

```text
Business Insight /
Business Recommendation
        ↓
registered Merchant Attention integration
        ↓
Merchant Attention determines
merchant handling semantics
```

---

# 70. Reverse Attention Boundary

Merchant Attention may own such handling facts as:

```text
requires handling
assigned
snoozed
unread
```

where accepted.

It SHALL NOT redefine:

```text
metric
observation
Business Health Assessment
Analytical Claim
Forecast
Scenario Evaluation
Recommendation
```

---

# 71. Workforce Boundary

Business Intelligence MAY consume authorised workforce evidence for legitimate operational analysis including:

```text
aggregate staffing coverage
capacity constraint
schedule coverage
workforce availability
```

MS-PROT-083 SHALL NOT authorise generic:

```text
individual worker performance scores
comparative worker rankings
disciplinary recommendations
termination recommendations
pay-reduction recommendations
promotion decisions
```

A future consequential worker-evaluation capability requires separate material authority.

---

# 72. Customer Analytics Boundary

Customer evidence MAY participate only under applicable data-use authority.

Business Intelligence SHOULD prefer:

```text
aggregated
cohort-based
non-identifying
```

evidence where individual identity is unnecessary.

The existence of CustomerContext history does not automatically permit BI reuse.

---

# 73. External Context

Business Intelligence MAY consume bounded external evidence such as:

```text
public holidays
weather
local events
approved contextual/economic signals
```

where an accepted provider/source contract preserves:

- source;
- geographic scope;
- observation time;
- freshness;
- provenance; and
- uncertainty/quality where applicable.

External context is evidence.

It is not merchant business truth.

---

# 74. AI Explanation Boundary

AI MAY translate governed analytical evidence into ordinary business language.

Canonical:

```text
governed Claims
+
Business Health Assessments
+
Forecasts
+
Scenario Evaluations
+
Impact Estimates
+
Recommendations
        ↓
bounded AI explanation
        ↓
merchant-facing language
```

Rejected:

```text
raw database
        ↓
LLM
        ↓
invent metric
        ↓
invent diagnosis
        ↓
invent recommendation
```

---

# 75. No AI Analytical Claim Laundering

Every material factual, diagnostic, causal, predictive or quantitative proposition introduced by a merchant-facing AI analytical explanation MUST resolve to a governed:

```text
Analytical Claim
Business Forecast
Scenario Evaluation
Impact Estimate
Business Recommendation
```

or another separately accepted analytical artifact.

AI may improve language.

AI SHALL NOT add new analytical substance merely because it sounds plausible.

---

# 76. Merchant Explanation Contract

Material merchant-facing analytical explanation SHOULD answer:

```text
WHAT
What happened / may happen?

WHY / EVIDENCE
What supports the statement?

MEANING
Why does it matter to this business?

ACTION
Is anything worth considering?

CERTAINTY
What is observed, derived,
inferred or unknown?

COVERAGE
Which business activity is represented?
```

The default interface SHOULD use ordinary business language.

Technical metrics remain available through progressive disclosure.

---

# 77. Standard Analytics Remain Available

Administrative compression does not mean hiding evidence.

Main Street MAY expose governed:

```text
metrics
charts
trend lines
comparisons
filters
reports
exports
```

where useful.

The default merchant experience SHOULD nevertheless prioritise interpretation and material decisions over analytical software navigation.

---

# 78. Reports

Reports SHALL consume governed Analytical Observations and analytical artifacts.

Report code MUST NOT define independent metric formulas.

Canonical:

```text
Measure Definition
        ↓
Analytical Observation
        ↓
Report Projection
```

A report is presentation.

It is not analytical semantic authority.

---

# 79. Projection and Materialisation

MS-PROT-083 does NOT require an initial universal analytics warehouse.

Preferred progression:

```text
accepted source evidence
        ↓
request-scoped evaluation
        ↓
materialise only when justified
```

Where analytical material satisfies accepted MS-PROT-027 Projection Contract triggers, MS-PROT-027 governs its:

```text
freshness
serviceability
provenance
rebuildability
Exposure
```

as applicable.

---

# 80. Analytical Storage Is Not Business Authority

A materialised analytics store SHALL NOT become operational authority.

Rejected:

```text
BI says appointment capacity exists
        ↓
create Appointment directly
```

Required:

```text
BI recommends action
        ↓
merchant instruction
        ↓
Scheduling evaluates
current authoritative facts
```

---

# 81. Domain Event Use

Domain Events MAY participate in analytics only through legitimate accepted reaction/use boundaries.

A BI consumer does not redefine another owner's event semantics.

Operational logs and telemetry MUST NOT substitute for authoritative business facts merely because they are easy to aggregate.

---

# 82. Business-Type Neutrality

Analytical applicability SHALL derive from:

```text
active capabilities
+
registered Measure Definitions
+
available governed evidence
+
Business Health applicability
```

Rejected as the primary architecture:

```text
if BARBERSHOP
if RESTAURANT
if MOTEL
```

Different businesses receive different analytical portfolios because their operating capabilities and evidence differ—not because BI is implemented as vertical exception logic.

---

# 83. Failure and Degradation

Business Intelligence SHALL degrade independently where safe.

Examples:

```text
AI unavailable
    → deterministic observations continue

forecast method unqualified
    → deterministic observations continue

one source unavailable
    → affected measure unresolved;
      unrelated metrics continue

external context unavailable
    → dependent inference unavailable

Recommendation premises stale
    → Recommendation no longer current

analytics projection unavailable
    → underlying business capability continues
```

BI failure SHALL NOT ordinarily disable the capability being analysed.

---

# 84. Illustrative Capacity Example

Suppose governed measures establish:

```text
Tuesday 13:00–17:00
utilisation = 42%

Thursday 13:00–17:00
utilisation = 91%
```

`OBSERVED`:

> Tuesday utilisation was 42%; Thursday utilisation was 91%.

`DERIVED`:

> Thursday utilisation was 49 percentage points higher than Tuesday over this comparison.

If a qualified method supports persistence:

`INFERRED`:

> The imbalance appears persistent rather than a one-period anomaly.

A governed Recommendation may say:

> Consider reviewing whether some Tuesday capacity would be more useful on Thursday.

BI does not change the workforce schedule.

---

# 85. Illustrative Mechanistic Diagnosis

Suppose authoritative facts establish:

```text
approved worker leave
        ↓
workforce availability change
        ↓
explicit workforce-resource relationship
        ↓
Appointment capacity reduction
```

Main Street may truthfully explain the capacity reduction mechanistically.

No probabilistic model is required.

---

# 86. Illustrative Partial Financial Evidence

Suppose Main Street observes:

```text
Payment Applications   £6,700
Provider Settlements   £6,200
Refunds                   £300
```

Potentially valid:

> £6,700 of payment value was applied to represented obligations during this period.

Not valid from these facts alone:

> Revenue was £6,700.

Not valid:

> Profit was £5,900.

Trustworthiness requires refusing unsupported accounting meaning.

---

# 87. Illustrative Sparse Evidence

Suppose:

```text
7 days history
4 appointments
no comparable historical period
```

Valid:

> You recorded four appointments this week. There isn't enough comparable history yet to determine a reliable demand trend.

Rejected:

> Demand is forecast to increase 23% next month.

---

# 88. Illustrative Scenario

Merchant asks:

> What happens if I open on Sundays?

Correct:

```text
current Business Hours
+
Sunday hypothetical assumption
+
relevant demand/capacity evidence
        ↓
Scenario Evaluation
```

The resulting Scenario may explain potential capacity or demand effects.

It does not change Business Hours.

---

# 89. Illustrative Stale Recommendation

09:00:

```text
forecast capacity constraint
        ↓
Recommendation:
consider adding Saturday capacity
```

12:00:

```text
three cancellations
+
another worker accepts a shift
```

Result:

```text
Recommendation Premise Assessment
    = PREMISES_STALE
```

Historical Recommendation remains retained where appropriate.

It is no longer presented as current advice.

---

# 90. Cross-Merchant Benchmarking

Cross-merchant benchmarking is NOT authorised by MS-PROT-083.

A later authority must separately govern:

```text
purpose
cohort construction
comparability
minimum cohort size
re-identification risk
industry semantics
location effects
data protection
participation rules
benchmark methodology
```

A shared category label is not sufficient evidence of business comparability.

---

# 91. Hard Invariants

### INV-083-001 — Source Ownership

Analytical use MUST NOT transfer business-fact ownership.

### INV-083-002 — No Parallel Source Authority

Business Intelligence MUST reference accepted owner contracts rather than invent competing source semantics.

### INV-083-003 — Binding Version Affinity

Every material Analytical Input Binding is immutable/version-affined.

### INV-083-004 — Metric Definition Required

Every material metric requires explicit versioned semantics.

### INV-083-005 — Acyclic Measure Dependencies

Analytical measure dependencies MUST form a resolvable acyclic graph unless a separate future authority establishes otherwise.

### INV-083-006 — Exact Evaluation Affinity

Every retained observation remains attributable to exact definitions and inputs.

### INV-083-007 — Deterministic Multi-Source Consistency

Multi-source evaluation requires an exact consistency predicate.

### INV-083-008 — Missing Is Not Zero

Missing evidence MUST NOT silently become numeric zero.

### INV-083-009 — Coverage Honesty

Whole-business claims require established whole-business coverage evidence.

### INV-083-010 — Historical Meaning

Metric-definition changes MUST NOT silently reinterpret historical observations.

### INV-083-011 — Cross-Version Comparison Safety

Different metric versions are non-comparable unless accepted compatibility/recomputation establishes otherwise.

### INV-083-012 — Currency Safety

Different currencies MUST NOT be aggregated without accepted normalisation authority.

### INV-083-013 — Current Data-Use Authority

Permitted analytical purpose does not substitute for required current data-use authority.

### INV-083-014 — Derived Data Remains Governed

Derived analytical outputs remain subject to data-protection and Exposure constraints.

### INV-083-015 — No Universal Health Score

No universal weighted Business Health score is established.

### INV-083-016 — Business/Operational Health Separation

Business Health MUST remain distinct from Operational Health.

### INV-083-017 — Merchant Attention Ownership

Business Health assessments and Recommendations MUST NOT re-own Merchant Attention.

### INV-083-018 — Epistemic Classification

Every material Analytical Claim is OBSERVED, DERIVED, INFERRED or UNKNOWN.

### INV-083-019 — No Causal Laundering

Association MUST NOT become causation without sufficient qualified evidence.

### INV-083-020 — Method Contract Required

Probabilistic production inference requires a versioned Analytical Method Contract.

### INV-083-021 — Current Method Qualification

A Method Contract alone does not authorise inferential use; current qualification is mandatory.

### INV-083-022 — Method Degradation Fails Closed

Expired/invalidated method qualification prohibits new affected inferred outputs.

### INV-083-023 — Sparse-Data Honesty

Insufficient evidence produces uncertainty rather than fabricated prediction.

### INV-083-024 — Quantified Impact Governance

Material quantified Impact Estimates require governed measures and qualified methods where probabilistic.

### INV-083-025 — Scenario Separation

Scenario assumptions are not Merchant Configuration, Merchant Intent or Commands.

### INV-083-026 — Financial Meaning Boundary

Order/Payment convenience data MUST NOT manufacture accounting or Financial Health truth.

### INV-083-027 — JRA Ownership

BI MUST NOT reinterpret regulatory authority.

### INV-083-028 — Recommendation Definition

Consequential Recommendations require Recommendation Definitions.

### INV-083-029 — Evidence-Grounded Recommendation Rationale

Material Recommendation propositions must resolve to governed analytical evidence.

### INV-083-030 — Recommendation Non-Authority

A Recommendation is not merchant intent, authorisation or execution authority.

### INV-083-031 — Recommendation Premise Revalidation

Retained Recommendations require premise assessment before current-action presentation.

### INV-083-032 — Method Qualification Participates in Recommendation Currentness

A recommendation dependent on an unqualified method cannot have `PREMISES_CURRENT`.

### INV-083-033 — No Autonomous Optimisation

BI SHALL NOT directly mutate merchant business state from its own analytical output.

### INV-083-034 — Outcome Link Is Not Causality

Recommendation outcome linkage proves traceability, not causality.

### INV-083-035 — AI Explanation Does Not Create Analytical Substance

Merchant-facing AI explanations may not introduce unsupported material claims.

### INV-083-036 — AI/Analytical Method Composition

When AI implements an analytical method, both AI Inference and Analytical Method governance apply.

### INV-083-037 — Worker Consequential-Decision Boundary

Generic BI SHALL NOT introduce individual-worker consequential employment recommendations.

### INV-083-038 — Analytical Storage Is Derived

Materialised analytics MUST NOT become source or execution authority.

### INV-083-039 — Event Ownership

BI consumption of Domain Events MUST NOT redefine the owner's event meaning.

### INV-083-040 — Business-Type Neutrality

Analytics applicability derives from capabilities and governed definitions, not vertical-name branching.

### INV-083-041 — Failure Isolation

BI failure MUST NOT ordinarily disable unrelated underlying business operation.

### INV-083-042 — Reports Do Not Define Metrics

Reports, dashboards and UI code MUST consume governed analytical definitions rather than create competing formulas.

---

# 92. First Falsification Record

The first proposition did **not** pass falsification.

The following failures were identified and required architectural revision:

| Attack | Failure | Revision |
|---|---|---|
| Financial closure | MS-PROT-083 attempted to resolve Financial Health without financial source semantics | Financial Health separated to future authority |
| Analytical source gravity | New source contract duplicated Query/Event/Projection boundaries | Replaced with non-owning Analytical Input Binding |
| Attention duplication | `DecisionSupportItem` duplicated MS-PROT-043 | Removed; Merchant Attention preserved |
| Arbitrary Business Health | Health judgement lacked governed interpretation | Business Health Indicator Definition introduced |
| Cross-source inconsistency | Values could represent incompatible snapshots | Analytical Evaluation Context + consistency requirement introduced |
| Forecast qualification | Method structure did not prove predictive validity | Analytical Method Contract introduced |
| Quantified impact | Recommendation could contain unsupported monetary number | Impact Estimate governance introduced |
| Recommendation staleness | Old recommendation could remain current | Premise revalidation introduced |
| Worker surveillance | Generic BI could rank/consequentially evaluate workers | Explicit worker boundary introduced |
| Currency mixing | Heterogeneous currency values could be summed | Hard currency invariant introduced |
| Health terminology | Business/Operational Health could collide | Qualified Business Health boundary introduced |
| Partial-data overclaim | Platform-controlled activity could masquerade as whole business | Coverage model + whole-business rule introduced |

**Outcome:** original proposition **FAILED → REVISED**.

---

# 93. Ambiguity Review Record

After first revision, the following material ambiguities were explicitly reviewed.

| Potential ambiguity | Resolution |
|---|---|
| BI vs source ownership | Source owners remain authoritative |
| Binding vs source contract | Binding references; does not redefine |
| Measure vs source fact | Measure is derived analytical meaning |
| Observation vs business fact | Observation is derived evidence |
| Observation vs Claim | Observation is value; Claim is analytical proposition |
| Insight vs Claim | Insight composes Claims; does not change their epistemic class |
| Business Health vs Operational Health | Fully qualified separate concepts |
| Business Health vs Merchant Attention | Health describes condition; Attention owns work handling |
| UNKNOWN vs NOT_APPLICABLE | applicable/insufficient vs genuinely inapplicable |
| Missing vs zero | explicitly separate |
| Complete declared scope vs whole business | explicitly separate |
| Forecast vs fact | forecast remains probabilistic |
| Forecast vs Scenario | expected evolution vs hypothetical assumption |
| Recommendation vs intent | recommendation never establishes merchant intent |
| Recommendation premises vs operational eligibility | separate current decisions |
| Financial analytics vs Financial Health | complete Financial Health remains separate |
| JRA vs BI | JRA owns regulatory determination |
| Worker capacity analysis vs personnel judgement | aggregate operations allowed; consequential worker scoring excluded |
| Stored data vs analytical use | current use authority required |
| BI outage vs business outage | independently degradable |

**Outcome:** ambiguity review **PASSED after revision**.

---

# 94. Second Falsification Record

The revised proposition was attacked again.

The second pass identified further defects.

| Attack | Failure | Final revision |
|---|---|---|
| Binding evolution | Binding could change without historical affinity | immutable binding version added |
| Metric-version comparison | v1/v2 metrics could create false trends | Measure Compatibility rule added |
| Method self-qualification | Method Contract could effectively certify itself | independent Method Qualification added |
| Model drift | Old qualification could persist indefinitely | qualification assurance/invalidation added |
| Health `ATTENTION` vocabulary | collided with Merchant Attention | replaced with `MATERIAL_CONCERN` |
| Data-use timing | declared purpose did not prove current permission | runtime use-authority evidence added |
| Derived-data privacy | analytical outputs could escape lifecycle governance | derived-data protection rule added |
| AI prose hallucination | explanation could add unsupported analysis | claim-grounded explanation invariant added |
| Stale method/recommendation | unchanged source facts could hide method invalidation | method qualification included in premise assessment |
| Hypothetical analysis | scenario could leak into Merchant Intent/configuration | Scenario Evaluation introduced |
| Whole-business assumption | BI could treat known channels as complete business | whole-business coverage made evidence-dependent |
| Analytical cycles | composite metric dependency could cycle | acyclic definition invariant added |
| Recommendation rationale | AI could attach unsupported explanation | analytical-evidence grounding made mandatory |

After these revisions, all second-pass failure families were retested.

**Outcome:** second falsification **PASSED**.

---

# 95. Trade-Offs

MS-PROT-083 deliberately accepts:

```text
more internal analytical contracts
more provenance
more UNKNOWN results
more conservative predictions
method qualification overhead
definition-version management
```

in exchange for:

```text
trustworthy merchant explanations
historical correctness
cross-capability intelligence
reduced hallucination
truthful uncertainty
safe recommendation/action separation
international extensibility
lower merchant analytical burden
```

This complexity is internal and therefore consistent with Main Street's product purpose.

---

# 96. Rejected Alternatives

## 96.1 Dashboard-First Analytics

**Rejected.**

Charts do not establish metric semantics.

## 96.2 LLM-First Business Analysis

**Rejected.**

Language-model fluency cannot substitute for governed analytical meaning.

## 96.3 Universal Analytics Source Model

**Rejected.**

Would duplicate capability ownership.

## 96.4 Universal Analytics Warehouse as Authority

**Rejected.**

Derived storage must not become business truth.

## 96.5 Universal Business Health Score

**Rejected for v1.0.**

Creates false precision and arbitrary weighting.

## 96.6 BI-Owned Merchant Attention

**Rejected.**

MS-PROT-043 already owns Merchant Attention.

## 96.7 Payment Data as Accounting Truth

**Rejected.**

Payment semantics are not sufficient for profit/cash/Financial Health.

## 96.8 Forecast Everything

**Rejected.**

Sparse or inapplicable evidence yields uncertainty.

## 96.9 Recommendation Auto-Execution

**Rejected.**

Merchant instruction and current capability authority remain mandatory.

## 96.10 Vertical-Specific BI Branches

**Rejected.**

Capability/evidence applicability is the governing mechanism.

---

# 97. Deferred Decisions

## MS-PROT-083-DQ-001 — Initial Analytical Measure Portfolio

**State:** DEFERRED.

Define the first production metric catalogue, owners and exact Input Bindings.

This absorbs the still-deferred MS-PROT-057 concern labelled “Business Intelligence metric catalogue” without prematurely resolving the catalogue itself.

---

## MS-PROT-083-DQ-002 — Initial Business Health Indicator Portfolio

**State:** DEFERRED.

Select the first production Business Health dimensions and Indicator Definitions.

---

## MS-PROT-083-DQ-003 — Analytical Persistence / Time-Series Architecture

**State:** DEFERRED.

Determine when request-scoped evaluation becomes insufficient and what physical analytical persistence is justified.

---

## MS-PROT-083-DQ-004 — Initial Analytical Method Portfolio

**State:** DEFERRED.

Select first forecasting/statistical methods, qualification evidence and calibration requirements.

---

## MS-PROT-083-DQ-005 — Method Qualification Operational Process

**State:** DEFERRED.

Define exact qualification reviewers/evidence pipeline, review cadence and invalidation operations before probabilistic production methods are enabled.

---

## MS-PROT-083-DQ-006 — Recommendation Prioritisation

**State:** DEFERRED.

Define exact recommendation/materiality ranking algorithm.

---

## MS-PROT-083-DQ-007 — External Context Portfolio

**State:** DEFERRED.

Select external contextual source/provider families.

---

## MS-PROT-083-DQ-008 — Merchant Attention Integration

**State:** DEFERRED.

Define the exact registered integration through which qualifying Business Insights/Recommendations contribute to MS-PROT-043 Merchant Attention.

---

## MS-PROT-083-DQ-009 — Merchant Analytical Surface

**State:** DEFERRED.

Define exact dashboard, natural-language, charts and progressive-disclosure UX.

---

## MS-PROT-083-DQ-010 — Reports and Exports

**State:** DEFERRED.

Define production report/export portfolio and transport formats.

---

## MS-PROT-083-DQ-011 — Analytical Retention/Lifecycle Portfolio

**State:** DEFERRED.

Define exact retention and minimisation policies for analytical artifacts under MS-PROT-053.

---

## MS-PROT-083-DQ-012 — Adaptive Learning

**State:** DEFERRED.

Define any merchant-specific adaptation, cross-merchant learning, training or distillation authority.

---

## MS-PROT-083-DQ-013 — Cross-Merchant Benchmarking

**State:** DEFERRED.

Requires separate material governance before activation.

---

## MS-PROT-083-DQ-014 — Currency Normalisation

**State:** DEFERRED.

Define FX normalisation only if cross-currency aggregated analytics becomes necessary.

Until resolved, currency values remain partitioned.

---

## MS-PROT-083-DQ-015 — Financial Health Composition

**State:** DEFERRED TO SEPARATE MATERIAL AUTHORITY.

A future authority must define the financial facts and financial-health semantics absent from MS-PROT-083.

`MS-PROT-082-DQ-007` remains unresolved until the combined Business Health + Financial Health authority composition is reviewed.

---

# 98. Conformance Test Obligations

A conforming implementation MUST demonstrate at least:

### BI-C01
Changing a Measure Definition formula does not reinterpret retained historical observations.

### BI-C02
Materially different metric versions cannot be compared without explicit compatibility/recomputation.

### BI-C03
Changing an Input Binding does not reinterpret old observations.

### BI-C04
Missing evidence does not become zero.

### BI-C05
Partial channel coverage cannot be presented as whole-business performance.

### BI-C06
Whole-business coverage cannot be inferred merely from complete Main Street-controlled channels.

### BI-C07
Different currencies cannot be accidentally aggregated.

### BI-C08
A multi-source metric cannot combine inputs violating its exact consistency predicate.

### BI-C09
Analytical dependency cycles invalidate the affected definition graph.

### BI-C10
A UI/report cannot introduce a new metric formula.

### BI-C11
AI cannot create an undefined metric.

### BI-C12
AI cannot transform an INFERRED claim into OBSERVED.

### BI-C13
AI analytical prose cannot add an unsupported material proposition.

### BI-C14
Sparse history blocks a method below its minimum-evidence requirement.

### BI-C15
A Method Contract without current Qualification cannot produce production inference.

### BI-C16
Expired/invalidated Method Qualification blocks new affected output.

### BI-C17
A quantified Impact Estimate cannot exist without governed analytical semantics.

### BI-C18
A Scenario assumption cannot mutate Merchant Configuration.

### BI-C19
A Scenario question cannot establish Merchant Intent.

### BI-C20
A retained Recommendation dependent on an invalidated method cannot be `PREMISES_CURRENT`.

### BI-C21
A stale Recommendation cannot remain represented as current action.

### BI-C22
Recommendation acceptance still invokes ordinary current capability validation.

### BI-C23
BI cannot directly modify price, hours, staffing, inventory or other capability truth.

### BI-C24
BI cannot create a second Merchant Attention owner.

### BI-C25
Business Health cannot be interpreted as Operational Health.

### BI-C26
Payment Settlement cannot silently become revenue.

### BI-C27
BI cannot independently recalculate JRA regulatory rules.

### BI-C28
Retained protected data cannot be analytically reused without required current use authority.

### BI-C29
Derived analytical data remains subject to applicable protection/lifecycle authority.

### BI-C30
Generic BI cannot introduce consequential individual-worker ranking/recommendations.

### BI-C31
Analytics store data cannot authorise operational mutation.

### BI-C32
BI degradation leaves unrelated business capabilities usable.

### BI-C33
Dashboard and report representations consume the same governed metric definition.

### BI-C34
Recommendation rationale cannot contain an ungrounded material claim.

---

# 99. MVP Boundary

MS-PROT-083 does NOT require initial implementation of:

```text
machine-learning forecasting
cross-merchant benchmarking
overall Business Health score
complex causal inference
analytics warehouse
full Financial Health
```

A useful initial implementation MAY consist of:

```text
small governed metric portfolio
+
deterministic historical comparison
+
small Business Health indicator portfolio
+
truthful evidence coverage
+
plain-language deterministic explanation
+
high-value deterministic recommendations
+
explicit UNKNOWN / insufficient-evidence outcomes
```

This is sufficient to establish the architectural substrate without premature data-science complexity.

---

# 100. Implementation Architecture Consequence

MS-PROT-083 does NOT justify a new microservice by itself.

The intended initial architecture remains compatible with Main Street's modular monolith and existing composite boundaries.

Conceptually:

```text
capability contexts
    │
    ├── owner queries
    ├── projections
    └── Domain Events
            ↓
Business Intelligence bounded context
    ├── analytical definitions
    ├── evaluation
    ├── Business Health
    ├── qualified methods
    ├── scenarios
    └── recommendations
            ↓
Merchant Attention / merchant interface
            ↓
existing application operations
```

A separate analytics service/store MAY be proposed later only if implementation evidence establishes a concrete need.

---

# 101. Corpus-Conformance Findings

The complete revised authority has been reviewed against the accepted corpus.

## 101.1 MS-PROT-043

**PASS.**

Merchant Attention remains independently owned.

MS-PROT-083 does not create a competing attention aggregate.

## 101.2 MS-PROT-057

**PASS.**

MS-PROT-083 concretises the already accepted rule that Business Intelligence sits above deterministic analytical semantics and cannot autonomously mutate business state.

AI remains an explanation/inference mechanism subject to accepted Inference Contracts.

## 101.3 MS-PROT-068

**PASS.**

Business Health is explicitly distinct from platform/provider Operational Health.

## 101.4 MS-PROT-027

**PASS.**

Analytical materialisation remains DERIVED Projection state and obtains no business mutation authority.

## 101.5 MS-PROT-053

**PASS.**

Analytical data use and derived analytical artifacts remain subject to current purpose/use/lifecycle authority.

## 101.6 MS-PROT-055

**PASS.**

Payment semantics remain distinct from accounting/financial semantics.

## 101.7 MS-PROT-072

**PASS.**

Recommendations lead to new merchant instructions and normal application/capability execution rather than BI-owned orchestration authority.

## 101.8 MS-PROT-082

**PASS WITH INTENTIONAL PARTIAL CLOSURE.**

MS-PROT-083 establishes the Business Health/analytical portion but does not create Financial Health semantics.

`MS-PROT-082-DQ-007` therefore remains deferred.

## 101.9 Canonical Semantic Lexicon

**PASS SUBJECT TO POST-APPROVAL TERMINOLOGY UPDATE.**

The accepted lexicon currently does not own the new analytical taxonomy.

After approval, high-risk distinctions should be registered without duplicating substantive MS-PROT-083 semantics.

## 101.10 Deferred Decision Register

**PASS SUBJECT TO POST-APPROVAL UPDATE.**

MS-PROT-083's new DQs must enter the single current DDR.

The older generic “Business Intelligence metric catalogue” deferral remains substantively unresolved and becomes traceable to `MS-PROT-083-DQ-001`.

`MS-PROT-082-DQ-007` remains unresolved pending Financial Health authority.

## 101.11 Lower-Authority Analytics PRD

**STALE / RECONCILIATION REQUIRED AFTER APPROVAL.**

Existing PRD material describing a generic Analytics module, metric lists and possible AI-generated Business Health score must become subordinate to MS-PROT-083.

No stale PRD language blocks acceptance of the higher authority.

## 101.12 IMPLEMENTATION-RULES

**NO AMENDMENT REQUIRED.**

The existing design→tests→minimum-production-code lifecycle and manual design-gate rules already govern implementation.

MS-PROT-083 does not introduce a new programming paradigm or implementation lifecycle.

---

# 102. Post-Approval Formalisation Consequences

Following explicit manual approval, formalisation SHALL include:

1. create accepted `designs/MS-PROT-083 — Analytical Measurement, Business Health & Operational Decision Support Model.md`;
2. extend `AUTHORITY-INDEX.md` through MS-PROT-083;
3. update `DEFERRED-DECISION-REGISTER.md` with `MS-PROT-083-DQ-001` through `DQ-015`;
4. preserve `MS-PROT-082-DQ-007` as unresolved and note required future Financial Health composition;
5. trace the older MS-PROT-057 Business Intelligence metric-catalogue deferral to `MS-PROT-083-DQ-001` without falsely resolving it;
6. update `CANONICAL-SEMANTIC-LEXICON.md` for high-risk terms, at minimum:
   - `Business Health` vs `Operational Health`;
   - `Business Recommendation` vs governance/design recommendation;
   - `Analytical Claim`;
   - `Scenario Evaluation`;
   - `Recommendation Premise Assessment`;
7. reconcile lower-authority `docs/development/PRD/analytics-and-business-intelligence.md` only where it contradicts the accepted authority;
8. review any other lower-authority business-health/BI language discovered during formalisation;
9. leave production code, migrations, tests and implementation programme state unchanged unless separately authorised.

No `IMPLEMENTATION-RULES.md` amendment is required.

---

# 103. Acceptance Boundary

Approval of MS-PROT-083 establishes:

1. Business Intelligence as derived decision support rather than business-state authority;
2. versioned Analytical Input Bindings;
3. versioned Analytical Measure Definitions;
4. deterministic multi-source consistency requirements;
5. exact Analytical Evaluation Context affinity;
6. Analytical Observation semantics;
7. coverage and whole-business-claim rules;
8. historical and cross-version comparison safety;
9. Business Health Indicator/Assessment/Profile semantics;
10. explicit Business vs Operational Health separation;
11. Analytical Claim epistemic classes;
12. diagnostic/causal boundaries;
13. Analytical Method Contracts;
14. independent Analytical Method Qualification;
15. sparse-data uncertainty;
16. Business Forecast semantics;
17. Impact Estimate governance;
18. Scenario Evaluation semantics;
19. Financial Health boundary;
20. JRA/BI separation;
21. Business Recommendation semantics;
22. Recommendation Definition requirements;
23. Recommendation Premise Assessment;
24. merchant-instruction-before-execution boundary;
25. no autonomous optimisation;
26. recommendation/outcome traceability without automatic causal claims;
27. Merchant Attention integration boundary;
28. worker consequential-decision exclusion;
29. protected analytical-use/lifecycle rules;
30. AI explanation grounding;
31. analytical materialisation as derived Projection state;
32. business-type-neutral analytical applicability.

Approval SHALL NOT:

- define the first metric catalogue;
- define complete Financial Health;
- define profit/cash-flow semantics;
- choose forecasting models;
- qualify any actual model;
- enable cross-merchant benchmarking;
- create an analytics warehouse;
- authorise autonomous action;
- authorise implementation;
- change the implementation sequence; or
- resolve `MS-PROT-082-DQ-007` in full.

---

# 104. Residual Risks

The architecture intentionally leaves several future risks unresolved rather than pretending to solve them prematurely:

1. Financial Health remains impossible to complete until authoritative financial-source semantics are accepted.
2. Useful forecasting for low-volume merchants may remain limited even under correct method governance.
3. Whole-business coverage may remain unavailable until external financial/channel integrations mature.
4. Recommendation quality requires empirical validation after real merchant use.
5. Method Qualification introduces operational governance overhead that must remain proportionate.
6. Cross-merchant benchmarking could create privacy and comparability problems and therefore remains prohibited.
7. Business Health Indicator definitions may themselves become overcomplicated if the first portfolio is not deliberately small.

These are residual product/implementation constraints.

They are not unresolved ownership contradictions inside MS-PROT-083.

---

# 105. Final Governing Statement

> **Main Street Business Intelligence transforms governed business evidence into trustworthy measurement, Business Health interpretation and operational decision support without becoming the owner of the business facts it analyses.**

> **Every material metric has defined semantics. Every material analytical claim preserves whether it is observed, derived, inferred or unknown. Probabilistic methods require current independent qualification. Hypothetical scenarios remain hypothetical. Quantified impacts require governed evidence. Recommendations remain advice until the merchant creates a new instruction through ordinary Main Street authority.**

> **Main Street should make sophisticated business analysis intuitive for micro and small-business merchants while preserving uncertainty whenever evidence is incomplete. The correct response to insufficient evidence is not fabricated precision; it is an intelligible statement of what Main Street knows, what it does not know and what—if anything—the merchant needs to do.**

---

# 106. Governance Assessment

**FUNDAMENTAL VISION CONFORMANCE:**  
`VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`

**DESIGN / PROPOSE:** COMPLETE

**FIRST FALSIFICATION:** FAILED → REVISED

**AMBIGUITY REVIEW:** PASSED AFTER REVISION

**SECOND FALSIFICATION:** PASSED AFTER SECOND-PASS AMENDMENTS

**CORPUS CONFORMANCE:** PASSED

**UNRESOLVED COUNTEREXAMPLES INSIDE GOVERNED SCOPE:** NONE IDENTIFIED

**FINANCIAL HEALTH:** DELIBERATELY OUTSIDE THIS AUTHORITY

**RECOMMENDATION:** `ACCEPT`

**MANUAL APPROVAL:** GRANTED 8 September 2026

**STATUS:** ACCEPTED

Canonical distinction:

```text
RECOMMENDATION: ACCEPT
        ≠
MANUAL APPROVAL: GRANTED
        =
STATUS: ACCEPTED
```

### End of MS-PROT-083
