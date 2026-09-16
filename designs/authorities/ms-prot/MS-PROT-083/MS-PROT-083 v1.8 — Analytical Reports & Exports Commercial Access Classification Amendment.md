# MS-PROT-083 v1.8 — Analytical Reports & Exports Commercial Access Classification Amendment

**Document ID:** MS-PROT-083  
**Version:** 1.8  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Business Intelligence analytical-delivery commercial-access classification amendment  
**Design node:** owner/supporting classification prerequisite for `MS-PROT-056-V17-DQ-001`  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-083 through v1.7 within analytical report/export commercial-access classification only  
**Depends on:** MS-PROT-083 v1.6; MS-PROT-083 v1.7; composite MS-PROT-056 through v1.9; composite MS-PROT-027; composite MS-PROT-053; MS-PROT-064; composite MS-PROT-062; applicable Actor Authorisation, Exposure, Resource Protection and lifecycle authorities  
**Preserves:** Business Intelligence ownership of analytical meaning; source-capability ownership of business facts; v1.6 analytical-delivery fidelity and externalisation boundaries; v1.7 analytical-evaluation commercial classifications; Commercial ownership of final entitlement identities and catalogue bindings  
**Partially resolves:** `MS-PROT-056-V17-DQ-001` by supplying the outstanding Business Intelligence report/export delivery classifications  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Manual approval:** GRANTED — 16 September 2026  
**Repository formalisation:** COMPLETE FOR THIS AUTHORITY DOCUMENT

---

# 0. Fundamental Vision Conformance

GrandRue SHALL permit a merchant to make otherwise-authorised analytical material portable without requiring the merchant to understand or purchase internal delivery architecture as a second product.

Canonical:

```text
merchant is legitimately permitted
to use or observe analytical material
        ↓
merchant explicitly requests
an accepted report/export
        ↓
GrandRue faithfully externalises
that already-governed material
```

Not:

```text
merchant has analytics
        ↓
must purchase another entitlement
solely because GrandRue renders
the same governed meaning into
PDF or bounded CSV
```

However:

```text
interactive presentation
        ≠
externalisation
```

because a downloaded file leaves GrandRue's continuing Exposure control.

Therefore report/export delivery retains its stronger:

```text
Actor Authorisation
Data Protection
Exposure
Audit
Resource Protection
currentness
externalisation
```

requirements without automatically becoming a second commercial toll.

---

# 1. Governing Decision

Composite MS-PROT-083 SHALL define the following exact owner-qualified analytical-delivery access contracts:

| Exact owner-qualified access contract | Exact purpose | Commercial classification | Target family |
|---|---|---|---|
| `business-intelligence/merchant-analytical-report-access@1` | `DELIVER_MERCHANT_ANALYTICAL_REPORT` | `NO INDEPENDENT COMMERCIAL ENTITLEMENT` | `PLATFORM_SERVICE_ACCESS` |
| `business-intelligence/measure-observation-export-access@1` | `EXPORT_MEASURE_OBSERVATIONS` | `NO INDEPENDENT COMMERCIAL ENTITLEMENT` | `PLATFORM_SERVICE_ACCESS` |

These access contracts govern commercial admission to the exact v1.6 delivery contracts:

```text
business-intelligence/merchant-analytical-report@1

business-intelligence/measure-observation-export@1
```

No `CommercialEntitlementIdentity` SHALL be minted merely because these two supporting delivery access contracts exist.

They are explicit no-entitlement classifications.

They are not:

```text
implicit FREE grants

implicit BUSINESS grants

implicit GROWTH grants

wildcard analytics grants
```

Missing classification outside these exact contracts remains unresolved rather than exempt.

---

# 2. Exact Scope

This amendment classifies only:

```text
business-intelligence/
merchant-analytical-report@1

business-intelligence/
measure-observation-export@1
```

as established by MS-PROT-083 v1.6.

It does not classify:

```text
saved reports

scheduled reports

scheduled email delivery

public report links

anonymous downloads

automatic third-party delivery

XLSX workbooks

JSON analytical API products

general raw-data export

account portability archives

custom report builders

custom analytical formulas

cross-merchant reporting

future report/export contract versions
```

unless a later accepted authority explicitly includes them.

---

# 3. Why Delivery Has No Independent Commercial Entitlement

The current v1.6 delivery contracts:

```text
do not create business truth

do not create analytical meaning

do not define new measures

do not grant source-record access

do not create actor authority

do not create Campaign authority

do not create provider authority
```

They externalise analytical material whose legitimacy must already have been established.

Therefore:

```text
commercially permitted analytical service
        +
otherwise-authorised analytical artifact
        +
qualified v1.6 delivery request
        ↓
supporting report/export delivery
```

does not justify:

```text
second Commercial Entitlement
solely for representation transport
```

The stronger consequences of externalisation are governed independently.

---

# 4. Merchant Analytical Report Access

GrandRue SHALL define:

```text
business-intelligence/
merchant-analytical-report-access@1
```

with exact purpose:

```text
DELIVER_MERCHANT_ANALYTICAL_REPORT
```

and classification:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

Its exact semantic delivery target is:

```text
business-intelligence/
merchant-analytical-report@1
```

The contract permits only request-scoped delivery satisfying MS-PROT-083 v1.6.

It does not itself authorise any analytical artifact included in the report.

Every included artifact must independently have a legitimate basis such as:

```text
current protected analytical permission

or

otherwise-authorised observation
of an already-established retained artifact
```

together with all applicable non-commercial predicates.

---

# 5. Measure Observation Export Access

GrandRue SHALL define:

```text
business-intelligence/
measure-observation-export-access@1
```

with exact purpose:

```text
EXPORT_MEASURE_OBSERVATIONS
```

and classification:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

Its exact semantic delivery target is:

```text
business-intelligence/
measure-observation-export@1
```

The contract does not authorise:

```text
a new Measure Definition

new analytical dimensions

new comparisons

new evaluation

general source-record export

cross-measure flat-file aggregation
```

The accepted v1.6 rule remains:

```text
one exact Measure Definition identity/version
        ↓
one bounded CSV export
```

---

# 6. Analytical Evaluation Remains Independently Protected

No-entitlement delivery SHALL NOT bypass MS-PROT-083 v1.7.

Where fulfilling a report/export request requires GrandRue to perform a new analytical evaluation or governed re-evaluation, the applicable current protected permission remains mandatory.

Canonical examples:

```text
new BUSINESS analytical evaluation
    →
USE_BUSINESS_ANALYTICS

new Campaign analytical evaluation
    →
USE_CAMPAIGN_ANALYTICS
```

Therefore:

```text
report/export delivery access
    ≠
permission to calculate analytics
```

and:

```text
EXPORT_MEASURE_OBSERVATIONS
    ≠
USE_BUSINESS_ANALYTICS
    ≠
USE_CAMPAIGN_ANALYTICS
```

---

# 7. Delivery of Already-Established Analytical Material

Where an analytical artifact was legitimately established previously and remains observable under applicable:

```text
retention
Actor Authorisation
Exposure
data-use
residual-access
```

authority, loss of the entitlement required for new evaluation SHALL NOT by itself manufacture a separate report/export toll.

Example:

```text
BUSINESS permission valid
        ↓
monthly observation committed

later BUSINESS permission absent
        ↓
retained observation remains
legitimately observable

merchant requests bounded export
        ↓
no new analytical evaluation
        ↓
delivery may proceed if every
current non-commercial predicate
still permits externalisation
```

By contrast:

```text
merchant requests current month
but no observation exists
        ↓
new evaluation required
        ↓
current analytical permission required
```

A delivery request SHALL NOT disguise recomputation as historical access.

---

# 8. Campaign Boundary

The no-entitlement delivery classification does not weaken GROWTH Campaign analytics protection.

A BUSINESS merchant SHALL NOT obtain new Campaign analytical material merely by requesting:

```text
PDF

or

CSV
```

If the requested Campaign analytical material requires new evaluation:

```text
USE_CAMPAIGN_ANALYTICS
```

remains required.

Likewise:

```text
Campaign analytical delivery
    ≠
Campaign preparation

Campaign analytical delivery
    ≠
Campaign execution

Campaign analytical delivery
    ≠
outbound-contact authority
```

Marketing retains its existing ownership.

---

# 9. Mixed Analytical Reports

A PDF may contain material from more than one analytical portfolio only where every included artifact independently passes its governing predicates.

Canonical:

```text
report section A
    → BUSINESS analytical material

report section B
    → Campaign analytical material

report generation
    ↓
evaluate authority independently
for A and B
```

No report-level access decision may convert:

```text
permission for A
```

into:

```text
permission for B
```

A broad report may include only eligible material under v1.6's accepted omission/honesty rules.

---

# 10. Externalisation Remains Consequential

Classification as:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

does not make externalisation unrestricted.

Every delivery continues to require the applicable v1.6 conditions, including:

```text
trusted Merchant Scope

authenticated execution context

current Actor Authorisation

current access to included material

applicable data-use authority

applicable Exposure authority

required Audit treatment

final externalisation revalidation

Resource Protection
```

A client SHALL NOT receive protected analytical data and remove unauthorised content locally.

---

# 11. Downloaded-Copy Boundary

The accepted distinction remains:

```text
permission to observe within GrandRue
        ≠
GrandRue can revoke an already
downloaded external copy
```

Therefore delivery continues to require explicit actor initiation.

This commercial classification creates no authority for:

```text
automatic email

scheduled distribution

public links

guest analytical access

third-party automatic upload
```

---

# 12. Resource Protection Is Not Commercial Entitlement

Reports and exports may consume:

```text
CPU

memory

temporary storage

file-generation capacity

large analytical scans
```

GrandRue MAY enforce accepted Resource Protection limits on:

```text
period

row count

file size

generation cost

request frequency
```

without creating a separate Commercial Entitlement.

Canonical:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
        ≠
unlimited resource consumption
```

An oversized or unsafe request fails under Resource Protection rather than by manufacturing a higher commercial tier.

Exact quantitative commercial allowances remain separate DQ-003 work where they become product-semantic commercial limits.

---

# 13. Target-Family Distinction

Both delivery access contracts use:

```text
PLATFORM_SERVICE_ACCESS
```

rather than:

```text
PRESENTATION_PRIVILEGE
```

because generation and externalisation are platform services with consequential data-delivery effects.

This does not mean they require an entitlement.

The distinction is architectural:

```text
merchant-analytics-presentation-access@1
    → PRESENTATION_PRIVILEGE
    → no independent entitlement

merchant analytical report/export access
    → PLATFORM_SERVICE_ACCESS
    → no independent entitlement
```

Different target families may legitimately share the same no-independent-entitlement commercial classification.

---

# 14. No Implicit FREE Analytics

This amendment SHALL NOT be interpreted as:

```text
FREE
    → business analytics

FREE
    → Campaign analytics

FREE
    → arbitrary report generation

FREE
    → arbitrary CSV export
```

A report/export can contain only analytical material independently available to the merchant.

Therefore:

```text
no eligible analytical material
        ↓
no report/export entitlement
can manufacture it
```

If a future accepted FREE analytical service is deliberately admitted, the current supporting-delivery rule may serve that otherwise-authorised analytical material without imposing a higher standard tier, subject to exact contract/revision compatibility.

---

# 15. Commercial Catalogue Consequence

Because both access contracts require:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

the final Commercial Catalogue Manifest SHALL NOT mint plan grants solely for:

```text
DELIVER_MERCHANT_ANALYTICAL_REPORT

EXPORT_MEASURE_OBSERVATIONS
```

Instead, manifest validation SHALL recognise the two contracts as explicit supporting no-entitlement classifications.

The final catalogue must still preserve the independent protected bindings for:

```text
USE_BUSINESS_ANALYTICS

USE_CAMPAIGN_ANALYTICS
```

and every other applicable owner-qualified protected purpose.

No-entitlement classification must never be inferred from absence of a binding.

---

# 16. Ownership

Business Intelligence retains ownership of:

```text
analytical meaning

analytical artifacts

report/export semantic fidelity

delivery-contract semantics
```

Commercial retains ownership of:

```text
CommercialEntitlementIdentity

Commercial Access Binding

plan grant sets

grant provenance

catalogue publication
```

Source capabilities retain business truth.

Actor Authorisation retains actor permission.

Exposure and Data Protection retain disclosure/use authority.

Audit retains audit semantics.

Resource Protection retains bounded-resource admission.

MS-PROT-027 retains applicable Projection/serviceability authority.

No authority is transferred by this amendment.

---

# 17. Alternatives Considered

## 17.1 Separate BUSINESS Report Entitlement

**REJECTED**

The merchant would receive BUSINESS analytics but require a second entitlement to make the already-authorised governed material portable.

There is no accepted semantic or commercial value distinction sufficient to justify that additional entitlement.

## 17.2 GROWTH-Only Reports and Exports

**REJECTED**

This would make ordinary BUSINESS operational and financial analytics portable only after a tier upgrade, contrary to the supporting-service policy.

It would also turn delivery format into the commercial boundary instead of the analytical service itself.

## 17.3 PDF Entitlement-Free, CSV GROWTH-Only

**REJECTED**

v1.6 already deliberately bounds CSV to one exact Measure Definition/version and prevents raw-data export.

Its greater externalisation sensitivity is handled by Authorisation, Data Protection, Audit and Resource Protection.

No evidence currently justifies turning spreadsheet compatibility into a higher-tier commercial purpose.

## 17.4 One Wildcard Analytics Export Permission

**REJECTED**

A wildcard such as:

```text
EXPORT_ALL_ANALYTICS
```

could obscure the independent commercial and actor requirements of the represented analytical portfolio.

## 17.5 Both Current Delivery Contracts Require No Independent Entitlement

**SELECTED**

This preserves:

```text
commercial protection
at analytical evaluation
        +
strict externalisation governance
        +
no second architectural toll
```

while retaining exact owner-qualified delivery classifications.

---

# 18. Falsification

## F1 — BUSINESS Merchant Downloads Operational Report

Merchant has current:

```text
USE_BUSINESS_ANALYTICS
```

and authority to observe the included artifacts.

Expected:

```text
PDF delivery allowed
without a second Commercial Entitlement
```

**PASS**

## F2 — BUSINESS Merchant Requests Campaign CSV

The requested Campaign observation requires new Campaign evaluation.

Expected:

```text
USE_CAMPAIGN_ANALYTICS absent
        ↓
Campaign evaluation unavailable
        ↓
export access cannot bypass it
```

**PASS**

## F3 — GROWTH Merchant Exports Campaign Measure

Merchant has the required Campaign analytical permission and all other predicates.

Expected:

```text
single-measure CSV may be delivered
without a second export entitlement
```

**PASS**

## F4 — Downgraded Merchant Has Retained Historical Observation

No new evaluation is required and retained observation remains legitimately accessible.

Expected:

```text
bounded externalisation may proceed
subject to current externalisation predicates
```

**PASS**

## F5 — Downgraded Merchant Requests Recalculation

Requested output requires current recomputation.

Expected:

```text
delivery access does not authorise evaluation
```

Current applicable analytical permission is required.

**PASS**

## F6 — Staff Member Lacks Financial Observation Authority

Merchant subscription otherwise includes BUSINESS analytics.

Expected:

```text
financial material rejected or omitted
according to exact v1.6 request semantics
```

No client-side filtering.

**PASS**

## F7 — FREE Merchant Requests Business Report

No independently authorised analytical material exists.

Expected:

```text
no-entitlement delivery
does not manufacture analytics
```

**PASS**

## F8 — Mixed BUSINESS and Campaign PDF

Actor is authorised for BUSINESS analytics but not Campaign analytics.

Expected:

```text
BUSINESS sections may qualify

Campaign sections do not become
authorised merely because the report
contract is available
```

**PASS**

## F9 — Huge CSV

Commercial classification is no-entitlement.

The request exceeds accepted resource bounds.

Expected:

```text
Resource Protection rejects
or requires a bounded request

no silent truncation

no invented higher-tier entitlement
```

**PASS**

## F10 — Sensitive Customer-Derived Analytics

Expected:

```text
Data Protection
+
Exposure
+
Actor Authorisation
+
Audit where required
```

remain mandatory before externalisation.

**PASS**

## F11 — Authority Revoked During Generation

Expected:

```text
final externalisation revalidation fails
```

where current authority is required.

**PASS**

## F12 — Public Share Link Requested

Expected:

```text
not authorised by this amendment
```

**PASS**

## F13 — Scheduled Accountant Email Requested

Expected:

```text
not authorised by this amendment
```

Separate standing-authority and delivery design remains necessary.

**PASS**

## F14 — Raw Order Data Requested as CSV

Expected:

```text
measure-observation export
does not become source-data portability
```

**PASS**

## F15 — Future XLSX Product

Expected:

```text
not automatically admitted
merely because CSV @1
has no independent entitlement
```

Exact accepted future contract/revision determines classification.

**PASS**

---

# 19. Ambiguity Review

### No independent Commercial Entitlement

Means the exact supporting access contract requires no Commercial Entitlement of its own.

It does not mean unrestricted access.

### Report access

Permission to invoke the exact v1.6 PDF delivery path for otherwise-authorised material.

It does not grant analytical evaluation.

### Export access

Permission to invoke the exact v1.6 single-measure CSV externalisation path for otherwise-authorised observations.

It is not raw-data portability.

### Supporting delivery

A transport/externalisation service whose commercial eligibility follows the legitimacy of the represented analytical material rather than introducing another standard entitlement.

### Historical delivery

Externalisation of an already-established artifact without recomputation.

It does not authorise new evaluation.

### Resource limit

A platform-protection predicate.

It is not automatically a subscription-tier distinction.

**Ambiguity Review: PASS**

---

# 20. Hard Invariants

1. Report/export delivery creates no analytical meaning.
2. Report/export delivery creates no source business truth.
3. Exactly two v1.6 delivery access contracts are classified by this amendment.
4. Both require `NO INDEPENDENT COMMERCIAL ENTITLEMENT`.
5. No final `CommercialEntitlementIdentity` is minted here.
6. No-entitlement delivery is not an implicit FREE grant.
7. New analytical evaluation still requires its exact v1.7 protected permission.
8. BUSINESS report/export delivery cannot manufacture Campaign analytics access.
9. GROWTH Campaign analytics access cannot manufacture Campaign execution authority.
10. Retained historical artifacts may be externalised only where current non-commercial predicates permit.
11. Historical delivery must not disguise new computation.
12. Externalisation remains subject to Actor Authorisation.
13. Externalisation remains subject to Data Protection and Exposure.
14. Audit requirements remain independently applicable.
15. Resource Protection remains independently applicable.
16. Resource limits do not automatically create a new entitlement.
17. Report/export access grants no raw-data portability.
18. Report/export access grants no public sharing authority.
19. Report/export access grants no scheduled delivery authority.
20. Report/export access grants no third-party automatic delivery authority.
21. Supporting delivery does not weaken v1.6 fidelity/currentness requirements.
22. Missing commercial classification outside the exact two contracts is not an exemption.
23. Final catalogue publication remains Commercial-owned.
24. DQ-001 remains OPEN.
25. Acceptance does not activate implementation.

---

# 21. DQ-001 Effect

Upon explicit manual approval and conforming repository formalisation:

```text
MS-PROT-056-V17-DQ-001
    remains OPEN
```

but the outstanding Business Intelligence v1.6 report/export commercial classification is completed.

The completed Business Intelligence classification set would then consist of:

```text
business-intelligence/
business-analytics-evaluation-access@1
    →
USE_BUSINESS_ANALYTICS
    →
BUSINESS + GROWTH
    →
PLATFORM_SERVICE_ACCESS

business-intelligence/
campaign-analytics-evaluation-access@1
    →
USE_CAMPAIGN_ANALYTICS
    →
GROWTH
    →
PLATFORM_SERVICE_ACCESS

business-intelligence/
merchant-analytics-presentation-access@1
    →
NO INDEPENDENT COMMERCIAL ENTITLEMENT
    →
PRESENTATION_PRIVILEGE

business-intelligence/
merchant-analytical-report-access@1
    →
DELIVER_MERCHANT_ANALYTICAL_REPORT
    →
NO INDEPENDENT COMMERCIAL ENTITLEMENT
    →
PLATFORM_SERVICE_ACCESS

business-intelligence/
measure-observation-export-access@1
    →
EXPORT_MEASURE_OBSERVATIONS
    →
NO INDEPENDENT COMMERCIAL ENTITLEMENT
    →
PLATFORM_SERVICE_ACCESS
```

The following remain outstanding under DQ-001:

```text
other remaining owner/supporting classifications

stable final CommercialEntitlementIdentity values

exact Commercial Access Bindings

FREE grant set

BUSINESS grant set

GROWTH grant set

complete Commercial Catalogue Manifest

cross-binding validation

explicit manifest approval
```

MS-PROT-083-DQ-010 remains resolved by v1.6 and is not reopened.

---

# 22. Governance Outcome

**Feature Admission:** PASS  
**Fundamental Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Semantic ownership:** PASS  
**Commercial/semantic separation:** PASS  
**Supporting-service policy:** PASS  
**Evaluation/delivery separation:** PASS  
**Presentation/externalisation distinction:** PASS  
**Actor-authority preservation:** PASS  
**Data-protection boundary:** PASS  
**Audit boundary:** PASS  
**Resource-protection boundary:** PASS  
**Residual-history boundary:** PASS  
**Campaign isolation:** PASS  
**No second commercial toll:** PASS  
**No wildcard analytics permission:** PASS  
**Ambiguity review:** PASS  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED — 16 September 2026  
**Repository formalisation:** COMPLETE FOR THIS AUTHORITY DOCUMENT

---

# 23. Acceptance Statement

MS-PROT-083 v1.8 classifies the two accepted v1.6 analytical-delivery paths as explicit supporting services requiring no independent Commercial Entitlement.

Canonical:

```text
protected analytical evaluation
    remains protected

otherwise-authorised retained analytics
    remain subject to their own access basis

PDF / bounded CSV delivery
    adds no second commercial toll

externalisation
    remains strictly governed
```

> **GrandRue may protect the analytical service without charging a second architectural toll merely to make already-authorised analytical meaning portable.**
