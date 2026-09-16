# MS-PROT-083 v1.6 — Merchant Analytical Reports & Exports Amendment

**Document ID:** MS-PROT-083  
**Version:** 1.6  
**Status:** ACCEPTED  
**Approved:** 15 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Business Intelligence analytical-delivery amendment  
**Design node:** `MS-PROT-083-DQ-010 — Reports and Exports`  
**Governed by:** `MS-DESIGN-RULES-001`; `DOCUMENT-GOVERNANCE.md`  
**Fundamental authority:** `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-083 through v1.5  
**Depends on:** composite MS-PROT-027; composite MS-PROT-035; composite MS-PROT-053; MS-PROT-064; applicable composite MS-PROT-056 commercial authority; applicable authorisation/trusted-context authority; composite MS-PROT-083 through v1.5; MS-PROT-093; MS-PROT-094  
**Resolves:** `MS-PROT-083-DQ-010`  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING

**Product identity note:** `Main Street` references in inherited `MS-*` authorities refer to the product currently named **GrandRue**. Stable authority identifiers and existing `mainstreet.*` implementation identities remain unchanged pending separately governed migration.

---

# 0. Fundamental Vision Conformance

## 0.1 Outcome

**VISION-CONFORMING**

Target merchants legitimately need to:

```text
read a business summary
print/share a business summary
take one governed measure into a spreadsheet
share analytical evidence with an adviser
retain a human-readable snapshot for their own use
```

without learning:

```text
BI query languages
report builders
dashboard configuration
metric formulas
data schemas
export pipelines
analytical warehouses
```

The intended experience is:

```text
merchant chooses:
"Download report"
or
"Export this measure"

        ↓

GrandRue preserves the
governed analytical meaning

        ↓

ordinary PDF
or spreadsheet-compatible CSV
```

Internal fidelity requirements may be sophisticated.

Merchant administration remains small.

## 0.2 Feature Admission

### Representation Test

**SUPPORTING PASS**

Reports and exports do not create new business truth. They faithfully represent analytical truth that already exists.

### Coordination Test

**PASS**

A common export contract prevents web, native clients and future delivery paths from independently redefining:

```text
coverage
currency
comparison
currentness
uncertainty
epistemic meaning
Business Health meaning
```

### Administrative-Compression Test

**PASS**

Without bounded export support, merchants would manually copy figures, take screenshots, reconstruct spreadsheets or reconcile incompatible reports.

### Target-Market Proportionality

**PASS**

The initial design deliberately rejects:

```text
report designer
custom analytics DSL
arbitrary joins
custom formulas
scheduled-report platform
data warehouse
enterprise reporting suite
```

---

# 1. Problem

Composite MS-PROT-083 already permits governed:

```text
metrics
charts
comparisons
reports
exports
```

and requires reports to consume governed Analytical Observations rather than define independent metric formulas.

MS-PROT-083 v1.4 additionally establishes:

```text
presentation fidelity
Mandatory Honesty Envelope
coverage qualification
epistemic distinction
chart truthfulness
cross-platform conformance
role-native access
```

The remaining question is:

> What report/export products may GrandRue initially produce, in what formats, and under what authority and fidelity constraints?

The answer must prevent downloaded material from becoming:

```text
a second analytics engine
a stale-currentness illusion
a raw-data escape hatch
a privacy bypass
a hidden accounting statement
an executable recommendation
```

---

# 2. Governing Decision

The initial production-semantic analytical delivery portfolio contains exactly two contract families:

```text
business-intelligence /
merchant-analytical-report@1

business-intelligence /
measure-observation-export@1
```

The initial transport formats are exactly:

```text
merchant-analytical-report@1
    → PDF
    → application/pdf

measure-observation-export@1
    → UTF-8 CSV
    → text/csv; charset=utf-8
```

No other analytical report/export format is admitted by v1.6.

In particular, v1.6 does not require:

```text
XLSX
JSON export
ZIP packages
PowerPoint
scheduled email reports
public report links
report templates
saved report definitions
```

Future formats MAY be added without reopening DQ-010 where they preserve the accepted semantics and introduce no materially new authority. A materially different delivery model requires fresh review.

---

# 3. Core Boundary

Canonical:

```text
authoritative source facts
        ↓
MS-PROT-083 definitions
        ↓
governed analytical artifacts
        ↓
authorised analytical projection
        ↓
report/export representation
        ↓
file transport
```

Not:

```text
raw source records
        ↓
report code invents formula
        ↓
downloaded "metric"
```

Hard rule:

> **Report and export generation is presentation/externalisation of governed analytical meaning. It is not analytical semantic authority.**

---

# 4. Ownership

Ownership remains:

```text
source capabilities
    business truth

MS-PROT-083
    analytical meaning

MS-PROT-027
    Projection/serviceability

MS-PROT-035
    production transport contracts

MS-PROT-053
    data use/lifecycle constraints

MS-PROT-064
    Audit evidence

authorisation authorities
    current actor access

MS-PROT-093 / 094
    first-party client and
    presentation convergence

this amendment
    analytical report/export
    fidelity and initial portfolio
```

No file format receives semantic ownership.

---

# 5. Analytical Delivery Context

Every report/export generation SHALL operate under one exact **Analytical Delivery Context**.

Conceptually:

```text
AnalyticalDeliveryContext
{
    deliveryContractIdentity
    deliveryContractVersion

    MerchantScope

    requestedAnalyticalScope

    exactIncludedArtifactReferences
    exactDefinitionVersions

    exactEvaluationContextReferences

    generatedAt

    currentActorAuthorityEvidence

    applicableDataUseEvidence

    applicableExposureEvidence

    outputFormat
}
```

The context is a request/evaluation construct.

v1.6 does not require it to become a durable business aggregate.

It exists to ensure that a downloaded file can never truthfully be described as:

```text
"the current numbers"
```

without preserving what was evaluated and when.

---

# 6. `business-intelligence/merchant-analytical-report@1`

## 6.1 Purpose

This contract produces a human-readable analytical snapshot intended for:

```text
merchant review
printing
ordinary sharing
discussion with an adviser
record of an analytical view
```

It is not a statutory document.

## 6.2 Source Material

The report MAY consume currently authorised:

```text
Analytical Observations
Analytical Claims
Business Insights
Business Health Assessments
Financial Health material
Business Forecasts
Impact Estimates
Scenario Evaluations
Business Recommendations
```

only where each included artifact is independently governed.

No inclusion creates new analytical authority.

## 6.3 Governing Projection

The report SHALL consume analytical material consistent with:

```text
business-intelligence/merchant-analytics
```

or an equivalently governed report projection derived from the same accepted artifacts.

Report generation SHALL NOT reacquire raw owner data merely to construct a different analytical answer.

---

# 7. Report Snapshot Semantics

A generated PDF is a **frozen representation**.

Canonical:

```text
report generated at T
        ↓
represents exact artifacts/evaluations
selected at T

later business change
        ↓
does not rewrite old PDF
```

Therefore:

```text
generated report
    ≠ continuously current dashboard
```

The PDF SHALL communicate its generation/as-of meaning clearly.

Where included sections have materially different evaluation times, the report MUST NOT falsely imply that they constitute one atomic whole-business snapshot.

---

# 8. Report Honesty Envelope

The PDF SHALL preserve all material qualification required by composite MS-PROT-083.

At minimum, where applicable:

```text
requested period/scope
generation time
business-time scope
coverage
known exclusions
comparison basis
currency
uncertainty
epistemic meaning
currentness
premise status
```

`PARTIAL_KNOWN` and `COVERAGE_UNKNOWN` SHALL NOT become complete coverage.

`UNKNOWN` SHALL NOT become zero.

Different currencies SHALL remain partitioned absent accepted Currency Normalisation.

Non-comparable periods SHALL NOT be represented as directly comparable.

---

# 9. Recommendations in Reports

A Business Recommendation MAY appear in a PDF only as decision-support material bound to its generation time.

Before inclusion as current advice:

```text
Recommendation Premise Assessment
    = PREMISES_CURRENT
```

must hold at report generation.

A recommendation with:

```text
PREMISES_STALE
or
PREMISES_UNRESOLVED
```

MUST NOT appear as currently valid advice.

A PDF recommendation SHALL NOT itself contain execution authority.

Canonical:

```text
downloaded recommendation
    ≠ current command
    ≠ Merchant Intent
    ≠ Actor Authorisation
```

A later merchant action still enters the ordinary live application flow and revalidates current authority and source state.

---

# 10. Financial and Regulatory Wording

An analytical report SHALL NOT present itself as:

```text
statutory accounts
profit-and-loss statement
balance sheet
tax return
regulatory filing
audited financial statement
professional accounting advice
legal advice
```

unless a separately accepted authority actually establishes that product.

Financial Health remains operational analytical meaning under its accepted authorities.

---

# 11. Report Visualisation

Charts in PDF remain presentation.

They SHALL preserve the v1.4 chart rules, including:

```text
missing != zero

incompatible versions
    != continuous series

different currencies
    remain partitioned

observed
    distinguishable from forecast
```

Material chart information SHALL also have an accessible textual or tabular representation.

Colour alone MUST NOT carry the governing analytical meaning.

---

# 12. `business-intelligence/measure-observation-export@1`

## 12.1 Purpose

This contract provides a spreadsheet-compatible export of governed Analytical Observations.

Its initial purpose is deliberately narrower than the PDF report.

It SHALL export exactly one:

```text
Measure Definition identity
+
Measure Definition version
```

per file.

This prevents one flat file from collapsing materially different analytical schemas.

## 12.2 Permitted Scope

One CSV MAY contain multiple Analytical Observations for that exact Measure Definition/version across a supported requested scope, such as:

```text
periods
dimensions
subjects
```

where the Measure Definition permits those analytical scopes.

The export contract does not create a new dimension or comparison.

---

# 13. No Mixed Measure Definitions in One CSV

Rejected:

```text
Order count
+
returning-customer share
+
inventory constraint count
+
payment value

        ↓

one generic flat CSV schema
```

The initial rule is:

```text
one exact Measure Definition/version
    → one CSV
```

This avoids a universal BI-record schema and preserves semantic clarity.

A future multi-measure workbook/package requires separate admission if merchant evidence demonstrates material value.

---

# 14. CSV Transport Contract

The initial CSV SHALL use:

```text
UTF-8
comma-delimited
header row
literal values
```

and SHALL be interoperable with ordinary spreadsheet software.

The exact line-ending implementation is not analytical authority.

The CSV MUST NOT contain:

```text
macros
spreadsheet formulas
executable content
external workbook links
```

Text originating from merchant, customer, provider or other untrusted material MUST be encoded as literal content and MUST NOT become executable spreadsheet formula content.

---

# 15. Measure CSV Schema

Every exported row SHALL identify or preserve, directly or through deterministic file-level constants:

```text
Measure Definition identity
Measure Definition version

Analytical Observation identity

Merchant Scope

observation interval
or applicable as-of time

evaluation time

value

unit

currency where applicable

evidence coverage

known exclusions where applicable

registered analytical dimensions
```

Dimension columns SHALL be derived only from the exact Measure Definition.

A renderer MUST NOT invent:

```text
channel
category
customer type
location
worker
product grouping
```

or any other dimension merely because the source data could technically be grouped that way.

---

# 16. CSV Value Fidelity

Numeric export SHALL preserve authoritative analytical numeric precision.

The CSV layer MUST NOT:

```text
round into materially different meaning
localise decimal syntax ambiguously
combine currencies
convert missing evidence to 0
recalculate percentages
recalculate totals
```

Display localisation belongs to human presentation.

Machine-readable export values retain unambiguous representation.

---

# 17. Absence of Rows

`measure-observation-export@1` exports governed observations.

Therefore:

```text
absence of CSV row
    ≠ zero
```

The generated export SHALL communicate that rule through its export contract/documentation.

Where no governed observation exists for the requested exact scope:

```text
NO_EXPORTABLE_ANALYTICAL_MATERIAL
```

or an equivalent explicit transport-safe outcome SHALL result.

GrandRue SHOULD NOT return an apparently valid empty analytical file that could reasonably be interpreted as proof of zero activity.

---

# 18. Authorisation

Every report/export is a `MERCHANT_OPERATIONAL` concern unless later authority explicitly admits another audience.

Generation requires:

```text
trusted Merchant Scope
+
current authenticated execution context
+
current actor authority
+
current access to every included
analytical artifact
```

Authorisation SHALL occur server/application-side.

A client MUST NOT receive unauthorised analytics and remove columns or pages locally.

---

# 19. Explicit Versus Broad Selection

For an explicitly requested analytical artifact that the actor may not observe:

```text
request = REJECTED
```

without disclosing protected analytical substance.

For a broad request such as:

```text
Download my business analytics report
```

GrandRue MAY compose only those analytical entries currently eligible for that actor.

The resulting report MUST NOT imply that omitted inaccessible dimensions were assessed as healthy, zero or nonexistent.

---

# 20. Data Protection and Externalisation

Derived analytics remain governed data.

Report/export generation SHALL therefore preserve:

```text
protection classification
purpose/use authority
current access
data minimisation
applicable Exposure
```

before file externalisation.

Stored data does not become exportable merely because GrandRue possesses it.

If required current use/export authority is:

```text
UNRESOLVED
```

the affected material fails closed.

---

# 21. Downloaded-Copy Boundary

A completed download creates a copy outside GrandRue's continuing authoritative Exposure control.

Therefore:

```text
permission to view now
        ≠
GrandRue can later revoke
an already downloaded copy
```

This consequence is intrinsic to export.

The operation therefore requires explicit actor initiation.

v1.6 SHALL NOT create automatic external distribution merely because an actor may view analytics interactively.

---

# 22. Audit

MS-PROT-064 remains Audit authority.

Where governing privacy/security authority requires evidence for an analytical export, Audit classification SHALL use:

```text
DATA_EXPORT
```

or another accepted equivalent classification.

Audit evidence SHOULD preserve the minimum necessary:

```text
actor/principal
Merchant Scope
delivery contract
requested analytical scope
format
generation/completion time
outcome
correlation
```

It SHOULD reference analytical artifacts rather than copy report/CSV payloads into Audit.

Audit MUST NOT become report storage.

---

# 23. Final Externalisation Revalidation

Immediately before a generated file is externalised, GrandRue SHALL ensure that material authority relied upon for the delivery remains applicable where the applicable contract requires currentness.

If, during generation:

```text
actor authority is revoked

protected-data use becomes prohibited

Merchant Scope changes

material analytical eligibility
can no longer be established
```

the file MUST NOT be externalised as though the earlier authority still applied.

This does not require a universal distributed snapshot mechanism.

It requires fail-closed current authority at the consequential delivery boundary.

---

# 24. Request-Scoped Generation

The initial portfolio is request-scoped.

v1.6 introduces no requirement for:

```text
saved reports
report-history store
export-history store
analytical warehouse
scheduled generation
background report jobs
persistent generated files
```

Ordinary response buffering or temporary transport material is implementation detail and MUST NOT become durable analytical authority.

---

# 25. No Saved Report Definition

The initial portfolio has no:

```text
SavedReport
ReportTemplate
CustomDashboard
ReportSchedule
ReportSubscription
```

authoritative concept.

The merchant MAY choose a supported:

```text
period
subject scope
registered dimension
comparison
```

only where the governing analytical definition already permits it.

This is scope selection.

It is not report programming.

---

# 26. No Scheduled Delivery

v1.6 does not authorise:

```text
email me this every Monday
send this to my accountant monthly
push a weekly spreadsheet
upload this automatically to Dropbox
```

Those behaviours would introduce additional questions involving some combination of:

```text
standing merchant authority
background work
recipient authority
Notification/provider externalisation
retention
delivery failure
revocation
```

They require separate admission.

---

# 27. No Public Share Links

The initial portfolio contains no:

```text
public report URL
share-by-token report
guest analytical access
customer analytical report
anonymous CSV download
```

A downloaded file may be voluntarily shared by the merchant outside GrandRue.

GrandRue does not create a new public Exposure mechanism under v1.6.

---

# 28. No General Raw-Data Export

`MS-PROT-083-DQ-010` concerns analytical reports and exports.

It does NOT establish a generic:

```text
export all Orders
export all customers
export all messages
export all worker records
export entire database
GDPR portability archive
account backup
```

Those are separate source/data-portability responsibilities.

The analytical export consumes analytical artifacts, not arbitrary source tables.

---

# 29. No Client-Side Reinvention

A first-party client MAY perform formatting and download interaction.

It MUST NOT:

```text
fetch raw Orders
calculate a metric locally
write that result into a PDF

or

fetch Payment rows
invent "revenue"
write that result into CSV
```

Both formats consume server/application-governed analytical meaning.

---

# 30. Currentness and Corrections

A file preserves what was governed at generation.

Later source correction may cause a later report/export to differ.

Canonical:

```text
File F1 generated
using source/evaluation state R1

later authoritative correction R2

File F1
    remains historical externalised representation

new generation F2
    uses current applicable evidence R2
```

GrandRue SHALL NOT pretend F1 was retrospectively rewritten.

---

# 31. Report Failure Isolation

For `merchant-analytical-report@1`, one unavailable analytical section SHOULD degrade at the smallest safe scope.

Example:

```text
Order analytics available
Customer-return history unavailable
Financial Health UNKNOWN
```

may yield one report containing:

```text
Order section
    available

Customer-return section
    insufficient evidence

Financial Health
    UNKNOWN
```

The report MUST NOT silently omit uncertainty where omission would imply a favourable or complete result.

---

# 32. Export Failure

`measure-observation-export@1` SHALL fail if the requested Measure Definition/version cannot be evaluated or exported truthfully for the requested scope.

A partial CSV MAY be produced only where:

```text
the governing analytical semantics
explicitly permit the represented
partial scope

+
coverage is preserved
```

A partial file MUST NOT masquerade as complete.

---

# 33. Resource Protection

Reports/exports remain subject to accepted resource-protection and bounded-query requirements.

An implementation MAY enforce bounded:

```text
period
row count
file size
generation cost
```

without changing analytical meaning.

Exact numeric operational limits are not established by v1.6 unless they become product-semantic or commercial limits.

An oversized request SHALL fail explicitly rather than silently truncate analytical meaning.

---

# 34. Commercial Entitlement Boundary

This amendment does not allocate report/export functionality to a subscription tier.

It does not create plan-type branching in analytical semantics.

Applicable commercial access remains governed by composite MS-PROT-056.

Hard distinction:

```text
analytical meaning
    ≠ commercial access
```

and:

```text
format availability
    ≠ permission to observe
underlying analytical material
```

---

# 35. AI Boundary

AI MAY assist with:

```text
business-language report narrative
plain-language explanation
section summaries
```

only from governed analytical artifacts.

AI SHALL NOT:

```text
add a metric
alter a value
invent a missing period
create a new comparison
erase uncertainty
rewrite coverage
change Business Health
invent a recommendation
```

If AI is unavailable, deterministic report generation remains functional.

CSV export never requires AI.

---

# 36. Initial Portfolio Exclusions

v1.6 explicitly does not introduce:

```text
report builder
custom formulas
arbitrary joins
XLSX workbook export
JSON analytics API product
scheduled reporting
saved reports
report subscriptions
public sharing
third-party automatic delivery
custom report templates
cross-merchant reports
row-level raw customer export
row-level workforce export
general data portability
statutory accounting statements
regulatory filings
analytical persistence architecture
```

---

# 37. Alternatives Considered

## 37.1 Screenshots / Browser Print Only

**REJECTED**

Simple, but does not establish consistent transport, metadata, coverage fidelity or cross-platform report semantics.

## 37.2 Arbitrary Report Builder

**REJECTED**

Transfers BI administration to merchants and creates metric/formula drift.

## 37.3 Raw Source-Data Export

**REJECTED**

Conflates analytical export with capability-owned business-data export and creates privacy/ownership ambiguity.

## 37.4 Universal Flat CSV of Every Analytical Artifact

**REJECTED**

Different artifact kinds and Measure Definitions cannot be truthfully collapsed into one generic row schema without semantic loss.

## 37.5 Initial PDF + XLSX + CSV + JSON Portfolio

**REJECTED**

Adds format/versioning/support complexity before evidence demonstrates need.

## 37.6 PDF + Single-Measure CSV

**SELECTED**

Provides:

```text
human-readable portable summary
+
spreadsheet-compatible quantitative evidence
```

while preserving a small initial semantic and transport surface.

## 37.7 Saved/Scheduled Reporting

**DEFERRED**

Would activate additional durable-work, recipient, externalisation, retention and revocation concerns not required for the initial merchant need.

---

# 38. Falsification

## F1 — Partial Whole-Business Coverage

A merchant has off-platform trade not represented in GrandRue.

Expected:

```text
PDF states represented scope /
partial coverage

CSV rows retain coverage evidence

whole-business claim prohibited
```

**PASS**

## F2 — Missing Evidence

A month lacks authoritative evidence.

Expected:

```text
missing month != zero
```

No report chart manufactures a zero point.

No CSV cell manufactures numeric zero.

**PASS**

## F3 — Mixed Currencies

Observations contain GBP and EUR.

Expected:

```text
separate currency representation
```

No total of:

```text
GBP + EUR
```

without accepted normalisation.

**PASS**

## F4 — Measure Definition Changes

Historical observations exist under `measure@1`; newer ones use `measure@2`.

Expected:

```text
one CSV
    cannot mix @1 and @2
```

unless a separately governed common-definition recomputation first establishes the exported observations.

**PASS**

## F5 — Unauthorised Staff Export

A worker can access operational scheduling but not financial analytics.

Expected:

```text
financial report/export
    rejected or omitted according
    to requested-scope semantics
```

No client-side hiding.

**PASS**

## F6 — Sensitive Analytical Material

An export contains protected customer-derived analytics.

Expected:

```text
current data-use authority
+
current actor authority
+
applicable DATA_EXPORT audit
```

where required.

**PASS**

## F7 — Authority Revoked During Generation

Actor authority is lost before file delivery.

Expected:

```text
externalisation fails closed
```

where current authority is required.

**PASS**

## F8 — Recommendation Goes Stale

Recommendation premises become stale before report generation completes.

Expected:

```text
not presented as current advice
```

**PASS**

## F9 — Report Read Months Later

The business changed after PDF generation.

Expected:

```text
PDF remains clearly an
as-of-generation snapshot
```

not current platform truth.

**PASS**

## F10 — Inventory Claim Concern

Business Health reports:

```text
inventory-claim-integrity
    = MATERIAL_CONCERN
```

Expected:

PDF may report the bounded condition.

It does not expand the wording into:

```text
your inventory is unhealthy
your business is unhealthy
buy more stock
```

**PASS**

## F11 — Financial Health Material

PDF contains Financial Health.

Expected:

It preserves exact Financial Health semantics and qualifications.

It does not become statutory accounts or P&L.

**PASS**

## F12 — CSV Formula Injection

A future analytical dimension contains untrusted text beginning with spreadsheet control syntax.

Expected:

```text
literal data only
```

No executable spreadsheet formula.

**PASS**

## F13 — Retry Download

The merchant repeats the same download.

Expected:

No business fact is duplicated or mutated.

Each actual sensitive externalisation may retain appropriate audit evidence.

**PASS**

## F14 — No Data

No exportable Analytical Observation exists.

Expected:

explicit:

```text
NO_EXPORTABLE_ANALYTICAL_MATERIAL
```

not an ambiguous empty file interpreted as zero.

**PASS**

## F15 — Hybrid Merchant

A salon has Appointments and retail Orders.

Expected:

PDF may compose both applicable governed analytical families.

A CSV remains one Measure Definition/version at a time.

No salon-specific reporting architecture is required.

**PASS**

## F16 — AI Failure

Narrative generation unavailable.

Expected:

deterministic PDF presentation and CSV export continue.

**PASS**

## F17 — Merchant on Phone

Merchant requests:

```text
Download report
```

No report-builder configuration is required.

**PASS**

## F18 — Accountant Wants Monthly Automatic Email

Expected:

not authorised by v1.6.

Requires separate scheduled/external delivery design.

**PASS**

---

# 39. Ambiguity Review

The following terms are fixed.

### Report

A human-readable request-scoped analytical snapshot.

Not analytical authority.

### Export

Explicit externalisation of governed analytical representation into an admitted file format.

Not generic business-data portability.

### Current

Current only under the governing analytical/currentness semantics at generation time.

A downloaded file is not continuously current.

### Complete

Complete only for the declared analytical scope under the governing evidence-coverage semantics.

Not automatically complete for the merchant's real-world business.

### CSV

A single exact Measure Definition/version export.

Not a generic BI row store.

### Download

Delivery of a generated representation to the authorised actor.

Not durable report storage.

### Report sharing

Outside-platform merchant handling of an already downloaded file.

Not GrandRue public Exposure or provider delivery authority.

**Ambiguity Review: PASS**

---

# 40. Hard Invariants

1. Reports and exports never define analytical formulas.
2. Reports and exports consume governed analytical artifacts only.
3. The initial portfolio contains exactly two contract families.
4. The initial report format is PDF.
5. The initial quantitative export format is UTF-8 CSV.
6. One CSV contains exactly one Measure Definition identity/version.
7. Report/export generation does not transfer source or analytical ownership.
8. Missing evidence is never represented as zero.
9. Whole-business claims require whole-business coverage.
10. Cross-currency aggregation remains prohibited absent accepted Currency Normalisation.
11. Measure-version compatibility rules remain binding in reports and exports.
12. Report visualisations remain presentation, not analytical authority.
13. Downloaded reports are generation-time snapshots, not continuously current truth.
14. Recommendations in reports remain non-authoritative and generation-time qualified.
15. A downloaded Recommendation is never executable authority.
16. Current actor authority is required before analytical externalisation.
17. Applicable data-use authority is required before protected analytical externalisation.
18. Sensitive data export uses MS-PROT-064 Audit semantics where required.
19. Audit does not retain full analytical payload merely because an export occurred.
20. Initial generation is request-scoped.
21. v1.6 creates no saved-report or scheduled-report authority.
22. v1.6 creates no public share-link authority.
23. v1.6 creates no general raw business-data export.
24. CSV content is literal and non-executable.
25. Large requests fail explicitly rather than silently truncate analytical meaning.
26. Commercial Entitlement remains separate from analytical semantics.
27. AI explanation is optional and never alters analytical meaning.
28. Acceptance does not activate implementation.

---

# 41. Deferred-Decision Disposition

Upon explicit manual approval and conforming repository formalisation:

```text
MS-PROT-083-DQ-010
Reports and Exports
    → RESOLVED
```

The following remain unchanged:

```text
MS-PROT-083-DQ-003
Analytical Persistence / Time-Series Architecture

MS-PROT-083-DQ-004
Initial Analytical Method Portfolio

MS-PROT-083-DQ-005
Method Qualification Operational Process

MS-PROT-083-DQ-006
Recommendation Prioritisation

MS-PROT-083-DQ-007
External Context Portfolio

MS-PROT-083-DQ-011
Analytical Retention/Lifecycle Portfolio

MS-PROT-083-DQ-012
Adaptive Learning

MS-PROT-083-DQ-013
Cross-Merchant Benchmarking

MS-PROT-083-DQ-014
Currency Normalisation
```

DQ-002 remains resolved by accepted MS-PROT-083 v1.5.

DQ-009 remains resolved by accepted MS-PROT-083 v1.4.

DQ-015 remains resolved by accepted MS-PROT-084 v1.1.

---

# 42. Governance Outcome

**Feature Admission:** PASS  
**Fundamental Vision Conformance:** VISION-CONFORMING  
**Administrative Compression:** PASS  
**Role-native operation:** PASS  
**Semantic ownership:** PASS  
**Presentation/analytical separation:** PASS  
**Report formula non-authority:** PASS  
**Coverage honesty:** PASS  
**Currency safety:** PASS  
**Currentness semantics:** PASS  
**Authorisation:** PASS  
**Data-protection externalisation boundary:** PASS  
**Audit composition:** PASS  
**Transport portfolio proportionality:** PASS  
**Anti-report-builder boundary:** PASS  
**Anti-warehouse boundary:** PASS  
**Cross-platform compatibility:** PASS  
**Mobile-use falsification:** PASS  
**Ambiguity review:** PASS  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED — 15 September 2026  
**Repository formalisation:** COMPLETE FOR THIS AUTHORITY DOCUMENT

---

# 43. Acceptance Statement

MS-PROT-083 v1.6 establishes a deliberately bounded initial reports-and-exports portfolio:

```text
governed analytics
        ↓
PDF business report
or
single-measure CSV
```

without creating a report builder, raw-data export system, analytics warehouse, scheduled-report infrastructure or alternative analytical authority.

> **GrandRue may make governed business intelligence portable without allowing portability to weaken the meaning, qualification or authority of the intelligence being exported.**