# MS-PROT-083 v1.4 — Merchant Analytical Surface, Progressive Disclosure & Presentation Fidelity Amendment

**Document ID:** MS-PROT-083  
**Version:** 1.4  
**Status:** ACCEPTED — explicit manual approval granted 14 September 2026  
**Approved:** 14 September 2026 by explicit manual approval after Fundamental Vision Conformance, authority-boundary review, falsification and complete pre-approval presentation  
**Authority type:** Business Intelligence analytical-presentation amendment  
**Design node:** `MS-PROT-083-DQ-009 — Merchant Analytical Surface`  
**Governed by:** `MS-DESIGN-RULES-001`; `DOCUMENT-GOVERNANCE.md`  
**Fundamental authority:** `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-083 through v1.3  
**Depends on:** composite MS-PROT-027; composite MS-PROT-035; composite MS-PROT-037; composite MS-PROT-049; composite MS-PROT-056 where commercial entitlement applies; composite MS-PROT-057; applicable authorisation and trusted-context authority; composite MS-PROT-083 through v1.3; composite MS-PROT-085; MS-PROT-093; MS-PROT-094  
**Implementation activation:** NONE  
**Resolves:** `MS-PROT-083-DQ-009`  
**Does not resolve:** `MS-PROT-083-DQ-002`, `MS-PROT-083-DQ-006`, `MS-PROT-083-DQ-010`, or any other currently deferred analytical question not expressly identified here.

---

# 0. Fundamental Vision Conformance

## 0.1 Outcome

**VISION-CONFORMING**

Main Street already possesses governed analytical meaning. The remaining problem is how to expose that meaning to ordinary merchants without requiring them to understand BI software.

The intended merchant experience is:

```text
governed business evidence
        ↓
governed analytical meaning
        ↓
Main Street explains what is happening
        ↓
merchant can understand enough immediately
        ↓
detail is available when wanted
        ↓
merchant decides where judgement is required
```

The merchant SHALL NOT need to understand:

```text
analytical artifact types
Measure Definition internals
epistemic taxonomies
coverage machinery
method qualification
projection architecture
BI schemas
query languages
dashboard builders
```

in order to understand their business.

## 0.2 Feature Admission

### Representation Test

**SUPPORTING PASS**

The amendment does not create new business truth. It permits existing governed analytical meaning to be represented faithfully to the merchant.

### Coordination Test

**PASS**

Without a shared presentation contract, web, Android, iOS, Windows and macOS clients could independently reinterpret analytical meaning, qualifiers, evidence and action semantics.

### Administrative-Compression Test

**PASS**

Without an intelligible analytical surface, merchants would need to interpret raw metrics, export data, reconcile reports or learn analytical software conventions.

### Role Simplicity

**PASS**

The design presents business meaning rather than internal BI architecture.

### Target-Market Proportionality

**PASS**

The model deliberately rejects a general-purpose BI application, dashboard builder, arbitrary formula system and enterprise analytical workbench.

---

# 1. Governing Decision

Main Street SHALL provide a **bounded Merchant Analytical Surface** over governed analytical artifacts.

The Merchant Analytical Surface is presentation and decision-support infrastructure.

It is NOT:

```text
business authority
a second analytical model
a general dashboard builder
a universal UI schema
a data warehouse
a report engine
a Merchant Attention replacement
a recommendation-ranking engine
```

Canonical:

```text
authoritative business facts
        ↓
MS-PROT-083 analytical semantics
        ↓
governed Analytical Artifacts
        ↓
merchant-authorised analytical projection
        ↓
Merchant Analytical Surface
        ↓
business-language presentation
        ↓
progressive disclosure
        ↓
merchant understanding / decision
```

Presentation SHALL NOT add analytical substance.

---

# 2. Ownership Boundary

Ownership remains:

```text
source capabilities
    own business truth

MS-PROT-083
    owns analytical meaning

MS-PROT-027
    owns Projection / serviceability semantics

MS-PROT-037
    owns merchant-experience composition

MS-PROT-049
    owns capability surface contributions

MS-PROT-085
    owns Merchant Attention / handling semantics

MS-PROT-093
    owns first-party client architecture

MS-PROT-094
    owns merchant presentation convergence

this amendment
    owns analytical presentation fidelity
    and progressive-disclosure requirements
```

The Merchant Analytical Surface MUST NOT redefine any upstream artifact.

---

# 3. Initial Surface Contribution

Business Intelligence SHALL define one bounded merchant `WORKSPACE` contribution:

```text
owner:
    business-intelligence

contribution:
    analytics-workspace

kind:
    WORKSPACE

audience:
    MERCHANT
```

This does NOT mean every merchant automatically receives an Analytics navigation item.

Eligibility remains determined through the accepted surface-composition authorities.

The contribution is eligible only where:

```text
applicable analytical semantics exist
+
merchant configuration permits them
+
commercial entitlement permits them where applicable
+
current actor authority permits observation
```

Absence of sufficient historical evidence SHALL NOT by itself hide an otherwise applicable analytical workspace.

A merchant may legitimately see:

> There is not enough evidence yet.

`UNKNOWN` is analytical meaning, not absence of a product surface.

---

# 4. Merchant Analytical Surface Projection

The initial analytical presentation contract SHALL use the Projection Contract identity:

```text
business-intelligence/merchant-analytics
```

The projection SHALL be request-scoped by default.

Persistent analytical materialisation is NOT introduced by this amendment.

Conceptually:

```text
trusted Merchant Scope
+
current actor context
+
current Exposure / authorisation
+
eligible governed analytical artifacts
+
requested analytical scope
        ↓
business-intelligence/merchant-analytics
        ↓
Merchant Analytical Surface Representation
```

A later persistent/materialised implementation remains governed by MS-PROT-027 and existing MS-PROT-083 materialisation rules.

---

# 5. Surface Representation Boundary

A Merchant Analytical Surface Representation SHALL carry enough structured meaning for a first-party client to present analytical material faithfully.

Conceptually:

```text
MerchantAnalyticalSurfaceRepresentation
{
    projection identity
    Merchant Scope
    generated-at/currentness context
    Projection Serviceability

    applicable analytical entries
}
```

An analytical entry SHALL reference an existing governed artifact rather than copy or redefine its analytical meaning.

An entry MAY represent an applicable:

```text
Analytical Observation
Analytical Claim
Business Insight
Business Health Assessment
Business Forecast
Impact Estimate
Scenario Evaluation
Business Recommendation
```

only where that artifact is currently authorised by accepted authority.

Presentation metadata MAY contain:

```text
business-facing statement
subject scope
business-time scope
comparison basis
epistemic class where applicable
evidence coverage
uncertainty where applicable
currentness / premise condition
detail references
candidate-action handoff where permitted
```

Such metadata is derived presentation material.

It is not independent analytical authority.

---

# 6. No Client-Side Analytical Reinvention

First-party clients MUST consume governed analytical artifacts.

They MUST NOT calculate independent business meaning from raw source facts merely because the data is available.

Rejected:

```text
raw Orders
    ↓
iOS locally calculates "conversion"

raw Appointments
    ↓
web locally invents "utilisation"

payment records
    ↓
Android locally labels "revenue"
```

Permitted client-side work includes purely presentational concerns such as:

```text
formatting
localisation
layout
interaction
accessible representation
platform-native navigation
visual encoding
```

provided analytical meaning is unchanged.

---

# 7. Progressive Disclosure Contract

The Merchant Analytical Surface SHALL use progressive disclosure.

Progressive disclosure governs **how much explanation is initially visible**, not whether material analytical qualification may be hidden.

Three semantic disclosure layers are established.

## 7.1 Immediate Meaning

The initial presentation SHOULD communicate, in ordinary business language:

```text
WHAT
what happened / may happen

MEANING
why the information matters where governed

ACTION
what may be worth considering where applicable
```

It SHALL also expose any material qualifier whose omission would cause a reasonable merchant to misunderstand the statement.

## 7.2 Explanation

A merchant SHALL be able to inspect:

```text
WHY / EVIDENCE
what supports the statement

CERTAINTY
whether it is observed, derived,
inferred or unknown

COVERAGE
which represented business activity
supports the statement
```

without requiring specialist analytical knowledge.

## 7.3 Analytical Detail

Further disclosure MAY expose:

```text
exact Measure Definition
exact period
comparison semantics
source scope
coverage details
artifact provenance
method identity / qualification
forecast assumptions
scenario assumptions
technical uncertainty detail
```

where relevant.

The exact platform gesture, route, panel, sheet, page or interaction mechanism remains a client-presentation decision.

---

# 8. Mandatory Honesty Envelope

Progressive disclosure MUST NOT become qualification concealment.

A primary analytical presentation MUST NOT omit a qualifier where omission would materially change the merchant's likely interpretation.

The following are mandatory.

## 8.1 Coverage

`PARTIAL_KNOWN` or `COVERAGE_UNKNOWN` MUST NOT be presented as complete business coverage.

Whole-business wording is prohibited unless whole-business coverage is governed and established.

## 8.2 Unknown

`UNKNOWN` MUST NOT be rendered as:

```text
zero
normal
no concern
no activity
success
```

unless a separate authoritative proposition establishes that meaning.

## 8.3 Inference

An `INFERRED` claim MUST be recognisable as an estimate, hypothesis, association, probability or other appropriately qualified inference.

It MUST NOT visually or linguistically masquerade as an `OBSERVED` fact.

## 8.4 Forecasts

A Forecast SHALL expose material:

```text
horizon
estimate/range
uncertainty
assumptions where required for truthful interpretation
```

## 8.5 Recommendations

A retained Recommendation with:

```text
PREMISES_STALE
```

or:

```text
PREMISES_UNRESOLVED
```

MUST NOT appear as currently valid advice.

---

# 9. Natural-Language Analytical Presentation

Main Street SHOULD make ordinary business language the default analytical explanation.

Natural-language output MAY be produced through:

```text
deterministic presentation
bounded AI explanation
or a combination
```

AI is optional to surface operation.

If AI explanation is unavailable:

```text
governed observations
+
deterministic business-language representation
+
charts/tables where applicable
```

SHALL remain capable of serving the merchant.

Every material factual, diagnostic, causal, predictive or quantitative statement in natural-language presentation MUST resolve to governed analytical material.

Natural language MAY simplify wording.

It MUST NOT simplify away material qualification.

---

# 10. Epistemic Presentation

The internal epistemic classes remain:

```text
OBSERVED
DERIVED
INFERRED
UNKNOWN
```

Merchant-facing presentation need not expose those exact technical tokens.

Equivalent business language MAY be used.

For example:

```text
OBSERVED
    "Recorded..."

DERIVED
    "Compared with..."

INFERRED
    "The available evidence suggests..."
    "Estimated..."

UNKNOWN
    "There is not enough evidence yet..."
```

These examples are not mandatory copy.

The semantic distinction is mandatory.

---

# 11. Chart and Visualisation Contract

A chart is presentation.

A chart MUST NOT create analytical meaning that does not exist in the governed artifacts it represents.

## 11.1 Missing evidence

Missing or unavailable evidence MUST appear as missing/unavailable information.

It MUST NOT silently become zero.

## 11.2 Comparison

Points or periods MUST NOT be visually represented as directly comparable when the governing Measure Definitions establish that they are materially non-comparable.

## 11.3 Measure revisions

Materially incompatible Measure Definition versions MUST NOT be joined into one apparently continuous series without accepted comparability semantics.

## 11.4 Currency

Different currencies SHALL remain visibly partitioned unless accepted currency-normalisation authority permits aggregation.

## 11.5 Trend lines and smoothing

A visual trend, forecast band, smoothing function or fitted relationship that introduces analytical interpretation requires the corresponding governed analytical meaning.

A client MUST NOT manufacture a statistical trend merely as visual decoration.

## 11.6 Forecast distinction

Observed history and Forecast output MUST remain distinguishable.

## 11.7 Visual honesty

Visual encoding MUST NOT materially reverse, conceal or exaggerate the governed analytical relationship.

Colour alone MUST NOT carry material analytical meaning.

An accessible textual or tabular alternative SHALL be available where a chart communicates material information.

---

# 12. Filtering and Analytical Scope

Merchant interaction MAY select supported analytical:

```text
period
subject
accepted analytical dimension
comparison basis
```

only where permitted by the governing Measure Definition or analytical artifact.

A filter:

```text
changes requested analytical scope
```

It does NOT:

```text
change a Measure Definition
invent a new formula
create a new analytical dimension
change evidence coverage
establish comparability
```

An unsupported requested scope SHALL fail closed or return an unresolved/unavailable analytical result rather than approximating new semantics.

---

# 13. Business Health Presentation

This amendment creates no new Business Health Indicator.

It establishes no new threshold.

It establishes no universal traffic-light system.

It establishes no universal Business Health score.

A Business Health Assessment MAY be presented only when a separately accepted Indicator Definition and resulting assessment exist.

Presentation SHALL preserve the accepted assessment vocabulary and meaning.

Colour or iconography MAY supplement presentation.

It MUST NOT create the underlying assessment.

`MS-PROT-083-DQ-002` therefore remains unresolved.

---

# 14. Business Recommendation Presentation

A Business Recommendation SHALL be presented as decision support.

It MUST NOT appear as an already authorised command.

A recommendation presentation MAY provide a candidate-action affordance.

Such an affordance SHALL result in:

```text
merchant review
        ↓
new explicit merchant instruction
        ↓
normal application operation
        ↓
current authorisation
current entitlement
current business state
current provider readiness
current policies/invariants
        ↓
capability-owned execution
```

The Recommendation itself never executes the action.

---

# 15. Recommendation Ordering Boundary

This amendment establishes no semantic priority model for concurrent Recommendations.

It MUST NOT create:

```text
AI priority score
universal recommendation score
business-importance ranking
materiality ranking
```

unless separately accepted authority permits it.

Where a client requires an ordering before `MS-PROT-083-DQ-006` is resolved, that ordering SHALL be deterministic and explicitly non-semantic.

Presentation order MUST NOT be represented as analytical importance.

`MS-PROT-083-DQ-006` remains unresolved.

---

# 16. Merchant Attention Boundary

The Merchant Analytical Surface SHALL NOT create:

```text
BI inbox
analytics task queue
analytics unread authority
analytics handling lifecycle
analytics priority queue
```

where those semantics belong to Merchant Attention.

Where an analytical artifact participates in Merchant Attention:

```text
Analytical Artifact
        ↓
accepted MS-PROT-085 integration
        ↓
Merchant Attention
        ↓
merchant handling presentation
```

MS-PROT-085 remains authoritative for:

```text
requires handling
assignment
snooze
read/unread
handling lifecycle
```

The analytical workspace MAY navigate to or explain the source analytical artifact.

It MUST NOT independently recreate Attention state.

---

# 17. Role-Native and Sensitive Presentation

Analytical visibility MUST be actor-authorised at the server/application boundary.

Hiding a card, chart or menu item is not authorisation.

The surface MUST NOT transmit analytical material to a client merely because that client later hides it.

Examples include potentially sensitive:

```text
financial information
worker information
customer information
commercially sensitive information
```

The applicable authorisation, Exposure and data-protection authorities govern before presentation.

---

# 18. Merchant-Surface Composition

The Merchant Analytical Surface participates in the existing merchant-client composition model.

It does not own:

```text
top-level navigation hierarchy
home-screen layout
native tab structure
desktop window structure
route names
breakpoints
component geometry
```

MS-PROT-037, MS-PROT-093 and MS-PROT-094 remain authoritative for those boundaries.

A client MAY expose analytical material:

```text
inside the Analytics workspace
inside an authorised operational context
through accepted Merchant Attention
through another already-governed merchant surface
```

where composition authority permits.

There MUST NOT be a requirement that every analytical artifact create its own screen.

---

# 19. Cross-Platform Presentation Conformance

All first-party merchant clients SHALL preserve:

```text
same analytical meaning
same material qualification
same evidence-coverage meaning
same recommendation authority boundary
same currentness semantics
```

They need not preserve:

```text
pixel layout
navigation implementation
chart library
interaction gesture
window arrangement
information density
```

Canonical:

```text
same analytical truth
+
same honesty requirements
+
platform-native interaction
```

not:

```text
identical pixels everywhere
```

---

# 20. Mobile-First Requirement

The analytical surface SHALL remain usable for ordinary merchant operation from a phone.

The default experience SHOULD avoid requiring:

```text
wide desktop-only tables
large simultaneous chart grids
precision pointer interaction
multi-window comparison
large client-side datasets
```

Detail MAY deepen progressively.

Large analytical collections SHOULD use bounded server-side querying, pagination or suitable projections rather than mandatory full client download.

---

# 21. Failure and Degradation

Analytical presentation SHALL degrade at the smallest safe scope.

Examples:

```text
one measure unavailable
    → affected entry unavailable;
      unrelated entries continue

AI unavailable
    → deterministic presentation continues

Forecast qualification invalid
    → Forecast unavailable;
      observations continue

one source unavailable
    → affected coverage/currentness changes;
      missing evidence is not zero

Recommendation premises stale
    → retained historically;
      not current advice

analytical projection unavailable
    → business operation continues
```

A partially degraded surface MUST NOT silently pretend complete serviceability.

---

# 22. No Universal UI DSL

`business-intelligence/merchant-analytics` SHALL NOT become a generic server-driven UI language.

The analytical representation MAY communicate:

```text
analytical meaning
scope
qualification
coverage
currentness
available detail
available governed action handoff
```

It MUST NOT prescribe arbitrary:

```text
view hierarchy
pixel geometry
generic component graph
cross-platform navigation tree
CSS
platform widgets
universal form definitions
```

Contract-driven clients remain responsible for appropriate native/web presentation.

---

# 23. Cross-Domain Validation

## 23.1 Information publisher

A publication-first merchant may receive only analytical material relevant to supported publication, enquiry or other active semantics.

It does not acquire Inventory or Order analytics merely because Main Street supports them globally.

**PASS**

## 23.2 Consultant

A consultant may receive applicable Appointment, Booking, Payment Obligation or customer-continuity analytical material where governed.

The same presentation contract applies without consultant-specific BI code.

**PASS**

## 23.3 Retail merchant

A retailer may receive applicable Order, Inventory and represented Payment analytical material.

No retailer-specific analytical architecture is required.

**PASS**

## 23.4 Hybrid merchant

A hybrid merchant may receive a composition of applicable analytical material from several capability families.

The same surface contract continues to apply.

**PASS**

Business category does not determine analytical truth or UI architecture.

---

# 24. Falsification

The proposal was tested against the following failure modes.

## F1 — Partial off-platform business activity

A merchant operates through Main Street and external channels.

Failure:

```text
represented Main Street activity
displayed as
whole-business activity
```

Resolution:

Coverage qualification is mandatory; whole-business language requires established whole-business coverage.

**PASS**

## F2 — Missing history

A month contains unavailable evidence.

Failure:

```text
missing month
    → zero
    → false downward trend
```

Resolution:

Missing evidence is not zero and charts preserve gaps/unavailability.

**PASS**

## F3 — Mixed currencies

Failure:

```text
GBP + EUR
    → one unlabeled total
```

Resolution:

Currencies remain partitioned absent accepted conversion authority.

**PASS**

## F4 — AI outage

Failure:

Analytics becomes unusable because narrative explanation depends on an LLM.

Resolution:

Deterministic analytical presentation remains valid and required.

**PASS**

## F5 — Client semantic drift

Failure:

Web calls an inferred relationship fact while iOS labels it estimate.

Resolution:

Shared fidelity contract requires identical epistemic meaning across clients.

**PASS**

## F6 — Universal server-driven UI

Failure:

The projection evolves into a generic card/component/navigation DSL.

Resolution:

The contract carries analytical meaning, not generic UI structure.

**PASS**

## F7 — Recommendation ranking

Failure:

The overview ranks competing Recommendations despite DQ-006 remaining deferred.

Resolution:

No semantic prioritisation is established.

**PASS**

## F8 — Generic RAG Business Health

Failure:

The interface manufactures green/amber/red health state from ordinary metrics.

Resolution:

Only accepted Business Health Assessments may be presented; no new threshold/status is created.

**PASS**

## F9 — Stale recommendation

Failure:

Yesterday's valid Recommendation retains an active-looking action after premises become stale.

Resolution:

Premise state is part of presentation fidelity; stale/unresolved advice cannot appear current.

**PASS**

## F10 — Hidden qualification

Failure:

A confident headline is shown while `PARTIAL_KNOWN`, uncertainty or inferred status is hidden several interactions away.

Resolution:

The Mandatory Honesty Envelope requires materially interpretation-changing qualifiers at the primary presentation level.

**PASS**

## F11 — Sensitive API overexposure

Failure:

Server sends financial/workforce analytics to an unauthorised staff client and relies on UI hiding.

Resolution:

Authorisation and Exposure occur before representation.

**PASS**

## F12 — Business-category dashboard branching

Failure:

SalonAnalytics, MotelAnalytics and ConsultantAnalytics evolve independently.

Resolution:

Applicability derives from capabilities, accepted analytical artifacts and actor authority.

**PASS**

---

# 25. Accepted Invariants

1. The Merchant Analytical Surface is presentation, not analytical or business authority.
2. Business Intelligence contributes one bounded merchant Analytics workspace rather than one screen per measure/capability.
3. `business-intelligence/merchant-analytics` is the initial analytical merchant Projection Contract identity.
4. Request-scoped analytical surface resolution is the default.
5. Clients consume governed analytical artifacts rather than independently deriving business meaning from raw facts.
6. Material qualification cannot be hidden by progressive disclosure.
7. `UNKNOWN` is never silently represented as zero or normal.
8. Whole-business claims require established whole-business coverage.
9. `INFERRED` claims remain distinguishable from `OBSERVED` facts.
10. Missing evidence remains missing evidence in charts and comparisons.
11. Analytical visualisation cannot create an ungoverned trend, forecast or causal interpretation.
12. Different currencies remain partitioned absent accepted normalisation authority.
13. Natural-language analytical explanation may simplify language but not analytical meaning.
14. AI is optional to analytical presentation.
15. Recommendation presentation does not confer execution authority.
16. Recommendation prioritisation remains outside this amendment.
17. Merchant Attention remains separate from analytical meaning.
18. Business Health status is presented only from accepted Business Health authority.
19. Cross-platform clients preserve analytical fidelity while using platform-native presentation.
20. The analytical representation does not become a universal UI DSL.
21. Ordinary analytical use remains mobile-first.
22. Business Intelligence failure does not ordinarily disable the business capability being analysed.
23. Business-type-specific analytical application branches are prohibited as the governing architecture.

---

# 26. Remaining Deferred Scope

This amendment deliberately leaves unresolved:

```text
MS-PROT-083-DQ-002
Initial Business Health Indicator portfolio

MS-PROT-083-DQ-006
Recommendation prioritisation

MS-PROT-083-DQ-010
Exact report/export formats
```

and every other MS-PROT-083 deferred question not expressly resolved here.

It also does not govern:

```text
exact route names
pixel layout
component library
chart library
native navigation mechanics
localisation copy
merchant-authored dashboard layouts
arbitrary analytical formulas
saved analytical-view customisation
report/export delivery formats
```

unless separately accepted authority already governs them.

---

# 27. Resolution of DQ-009

`MS-PROT-083-DQ-009 — Merchant Analytical Surface` is resolved by establishing:

```text
bounded Analytics WORKSPACE contribution
+
request-scoped merchant analytical projection
+
presentation-fidelity requirements
+
natural-language contract
+
chart/visualisation contract
+
progressive-disclosure contract
+
Mandatory Honesty Envelope
+
cross-platform conformance
+
role / Exposure boundary
+
recommendation action-handoff boundary
```

without creating:

```text
a second dashboard architecture
a new business-semantic owner
a BI priority engine
a generic UI DSL
a report/export authority
```

---

# 28. Governance Record

**Vision Conformance:** VISION-CONFORMING  
**Falsification:** PASS  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED — 14 September 2026  
**Repository formalisation:** AUTHORISED
