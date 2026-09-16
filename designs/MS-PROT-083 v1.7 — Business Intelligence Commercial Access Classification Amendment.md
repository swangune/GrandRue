# MS-PROT-083 v1.7 — Business Intelligence Commercial Access Classification Amendment

**Document ID:** MS-PROT-083  
**Version:** 1.7  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Business Intelligence commercial-access classification amendment  
**Design node:** owner-classification prerequisite for `MS-PROT-056-V17-DQ-001`  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-083 through v1.6 within commercial-access classification only  
**Depends on:** composite MS-PROT-083 through v1.6; composite MS-PROT-056 through v1.9; MS-PROT-084 v1.1; composite MS-PROT-087; composite MS-PROT-027; composite MS-PROT-053; applicable source-capability, Actor Authorisation, Exposure, Resource Protection and lifecycle authorities  
**Preserves:** Business Intelligence ownership of analytical meaning; source-capability ownership of business facts; Financial Operations ownership of finance-native source facts; Marketing ownership of Campaign semantics; analytical honesty, coverage and provenance; merchant configuration independent of subscription state  
**Partially resolves:** `MS-PROT-056-V17-DQ-001` by supplying the Business Intelligence owner classifications  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Manual approval:** GRANTED

**Version-assignment note:** the complete authority was approved in ChatGPT under the working proposal identity `MS-PROT-083 v1.5`. Live `development` already contained accepted, unrelated `MS-PROT-083 v1.5 — Initial Business Health Indicator Portfolio Amendment` and `MS-PROT-083 v1.6 — Merchant Analytical Reports & Exports Amendment`. The approved commercial semantics are therefore formalised unchanged as the next free authority identity, **MS-PROT-083 v1.7**. The renumbering creates no semantic amendment beyond avoiding authority-identity collision.

**Product identity note:** inherited `Main Street` references identify the product currently named **GrandRue**. Stable `MS-*` and `mainstreet.*` identifiers remain unchanged pending separately governed migration.

---

# 0. Fundamental Vision Conformance

GrandRue SHALL let an ordinary merchant understand the business without requiring the merchant to administer analytics infrastructure, dashboard subscriptions or internal entitlement mechanics.

Commercial packaging may govern whether GrandRue performs a protected analytical evaluation.

Commercial packaging SHALL NOT:

```text
redefine the source business fact

turn an analytical measure into source authority

make presentation itself a second commercial toll

convert BUSINESS analytics into Campaign authority

convert GROWTH Campaign analytics into Campaign execution

erase retained analytical history on downgrade

manufacture certainty from missing evidence

turn AI explanation into analytical authority
```

The initial commercial model is deliberately a **2 + 1 structure**:

```text
1. BUSINESS analytics service
2. GROWTH Campaign analytics service
+
1. entitlement-free supporting presentation path
```

This keeps commercial packaging aligned with merchant value rather than with internal architecture.

---

# 1. Governing Decision

Composite MS-PROT-083 SHALL define exactly two initially protected Business Intelligence commercial purposes:

```text
USE_BUSINESS_ANALYTICS

USE_CAMPAIGN_ANALYTICS
```

The exact owner-qualified access contracts SHALL be:

| Exact owner-qualified access contract | Commercial classification | Standard allocation | Target family |
|---|---|---|---|
| `business-intelligence/business-analytics-evaluation-access@1` | `USE_BUSINESS_ANALYTICS` | BUSINESS + GROWTH | `PLATFORM_SERVICE_ACCESS` |
| `business-intelligence/campaign-analytics-evaluation-access@1` | `USE_CAMPAIGN_ANALYTICS` | GROWTH | `PLATFORM_SERVICE_ACCESS` |
| `business-intelligence/merchant-analytics-presentation-access@1` | `NO INDEPENDENT COMMERCIAL ENTITLEMENT` | plan-independent supporting presentation of otherwise-authorised analytical material | `PRESENTATION_PRIVILEGE` |

FREE SHALL NOT receive either protected analytical-evaluation purpose through the standard catalogue.

The presentation classification is explicit. Missing classification is not an exemption.

This amendment does not mint the final `CommercialEntitlementIdentity` values. Those remain Commercial-owned manifest work under `MS-PROT-056-V17-DQ-001`.

---

# 2. Why Two Protected Analytical Purposes

The accepted commercial model distinguishes two merchant propositions:

```text
BUSINESS
    understand and monitor
    the operation of the business

GROWTH
    understand governed Campaign
    execution evidence and performance
```

Those purposes differ materially in commercial placement and source context.

They SHALL NOT be collapsed into one wildcard such as:

```text
USE_ALL_ANALYTICS
```

because that would allow BUSINESS access to manufacture GROWTH Campaign analytics permission.

They SHALL also not be fragmented into one entitlement per measure family merely because analytical definitions are separately identified.

Rejected commercial fragmentation includes:

```text
ORDER_COUNT_ENTITLEMENT

FINANCIAL_HEALTH_ENTITLEMENT

CUSTOMER_RETURN_ENTITLEMENT

INVENTORY_HEALTH_ENTITLEMENT

CAMPAIGN_DELIVERY_COUNT_ENTITLEMENT
```

Semantic identity remains exact. Commercial packaging remains comprehensible.

---

# 3. BUSINESS Analytics Protected Portfolio

`business-intelligence/business-analytics-evaluation-access@1` protects new evaluation or governed re-evaluation within the accepted initial BUSINESS analytical portfolio.

That portfolio consists of the exact accepted families already placed in BUSINESS by composite MS-PROT-056, plus the accepted Business Health family now present in composite MS-PROT-083.

## 3.1 Financial Health

The five Financial Health families are:

```text
financial-health/current-due-commitment-coverage@1
financial-health/near-term-commitment-pressure@1
financial-health/overdue-receivable-pressure@1
financial-health/financing-arrears@1
financial-health/regulatory-financial-pressure@1
```

Financial Health remains derived analytical meaning under MS-PROT-083. MS-PROT-084 retains ownership of the applicable finance-native source facts and evidence.

## 3.2 General Operational BI

The seven accepted operational BI families are:

```text
business-intelligence/order-commitment-count@1
business-intelligence/appointment-occurrence-outcome-count@1
business-intelligence/booking-utilisation-outcome-count@1
business-intelligence/outstanding-payment-obligation-count@1
business-intelligence/outstanding-payment-obligation-value@1
business-intelligence/inventory-position-constraint-count@1
business-intelligence/scheduled-commitment-person-duration@1
```

Where the accepted semantic identity is represented without repeating the namespace in source text, `business-intelligence` remains the governing namespace established by MS-PROT-083.

## 3.3 Customer-Return Analytics

The four accepted customer-return families are:

```text
customer-return/qualified-activity-count@1
customer-return/activity-customer-classification-count@1
customer-return/returning-customer-share@1
customer-return/returned-after-quiet-period-count@1
```

BUSINESS access to these families SHALL NOT grant Campaign preparation, audience construction, outreach or Marketing execution.

## 3.4 Initial General Business Health

The initial directly MS-PROT-083-owned non-financial Business Health family is:

```text
business-health/inventory-claim-integrity@1
```

Its source ownership, evidence requirements, assessment vocabulary and non-mutation boundary remain unchanged.

## 3.5 No Wildcard Expansion

`USE_BUSINESS_ANALYTICS` applies only to accepted analytical families explicitly included in the applicable published Commercial Access Binding and catalogue revision.

It SHALL NOT silently include:

```text
future forecasts
future optimisation
future profitability analytics
future presence-performance analytics
future regulatory analytics
future accounting analytics
future agentic CRM analytics
arbitrary custom metrics
```

A newly accepted analytical family requires deliberate commercial classification before a catalogue may grant it through this purpose.

---

# 4. GROWTH Campaign Analytics Protected Portfolio

`business-intelligence/campaign-analytics-evaluation-access@1` protects new evaluation or governed re-evaluation of the accepted Campaign analytical portfolio.

The initial protected Campaign measure families are exactly:

```text
campaign/recipient-eligibility-count@1
campaign/direct-email-responsibility-count@1
campaign/direct-email-delivery-evidence-count@1
campaign/campaign-linked-suppression-count@1
campaign/website-announcement-publication-count@1
```

The standard allocation is:

```text
GROWTH
```

through the accepted tier hierarchy.

This classification SHALL NOT introduce or imply:

```text
open rate
click-through rate
conversion rate
campaign revenue
ROI
ROAS
retention uplift
causal attribution
```

unless separately accepted analytical authority later establishes those meanings.

`USE_CAMPAIGN_ANALYTICS` SHALL NOT grant Campaign creation, audience mutation, message approval, contact permission, Notification provider permission or Campaign execution.

Marketing remains the semantic owner of Campaign and audience operations.

---

# 5. Presentation Has No Independent Commercial Toll

The exact supporting contract is:

```text
business-intelligence/merchant-analytics-presentation-access@1
```

Its commercial classification is:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

This applies to presentation of otherwise-authorised analytical material through accepted merchant analytical surfaces, including the accepted:

```text
analytics-workspace

business-intelligence/merchant-analytics
Projection Contract
```

and equivalent accepted delivery surfaces that preserve the same represented analytical authority.

The supporting presentation path may include, where otherwise authorised:

```text
plain-language labels

charts

comparisons

progressive disclosure

Mandatory Honesty Envelope material

coverage qualification

epistemic distinction

accessible deterministic representation

natural-language explanation
```

Presentation SHALL NOT require a higher standard tier merely because an entitled analytical result is shown through one of these forms.

The presentation contract is not a blanket permission to every analytical portfolio.

For each represented artifact, the system must still establish the applicable protected analytical permission or legitimate residual observation basis, together with Actor Authorisation, Exposure, merchant scope and lifecycle requirements.

---

# 6. Evaluation Is the Protected Commercial Boundary

Protected commercial permission is required when GrandRue performs a new analytical evaluation whose result belongs to one of the protected portfolios.

Examples include:

```text
calculate a new operational BI observation

re-evaluate Financial Health from current evidence

compute a new customer-return observation

compute or refresh the accepted Business Health indicator

compute a new Campaign analytical observation
```

The protected boundary is the analytical service evaluation, not the existence of source facts.

Therefore:

```text
Order exists
    ≠
USE_BUSINESS_ANALYTICS granted

Campaign exists
    ≠
USE_CAMPAIGN_ANALYTICS granted

Financial Operations evidence exists
    ≠
Financial Health evaluation granted
```

Commercial permission also does not establish that an analytical definition is applicable or that sufficient evidence exists.

---

# 7. Source Data Is Not an Analytics Entitlement

Access to an authoritative source record SHALL NOT be reclassified as advanced analytics merely because that record can support a measure.

Conversely, an analytics entitlement SHALL NOT grant unrestricted source-record access.

Canonical:

```text
source capability
    owns business fact

Business Intelligence
    owns derived analytical meaning

Commercial
    owns permission to use
    protected analytical service
```

Examples:

```text
Booking access
    does not automatically grant
    Booking analytics

Business analytics access
    does not grant Booking mutation

Financial Health access
    does not grant Payment execution

Campaign analytics access
    does not grant Campaign execution
```

All source evidence remains subject to its own ownership, Actor Authorisation, Exposure, data-protection and lifecycle rules.

---

# 8. Applicability Remains Independent of Subscription

Merchant Configuration and actual business activity determine whether a measure family is semantically applicable.

Commercial state does not determine whether the merchant is conceptually:

```text
an Order merchant

a Booking merchant

a merchant with workforce activity

a merchant with finance-native records

a merchant with customer-return evidence

a merchant with Campaign activity
```

Therefore the system SHALL NOT introduce entitlement purposes such as:

```text
ENABLE_ANALYTICS_CONFIGURATION

ACTIVATE_FINANCIAL_HEALTH

CONFIGURE_BUSINESS_HEALTH

KEEP_CAMPAIGN_MEASURES_ENABLED
```

Commercial permission governs protected evaluation only.

---

# 9. Downgrade and Retained Analytical History

Loss of `USE_BUSINESS_ANALYTICS` or `USE_CAMPAIGN_ANALYTICS` SHALL NOT delete, rewrite or semantically invalidate retained analytical artifacts that were legitimately established while permission existed.

Where retention, Actor Authorisation and Exposure permit, an already-established artifact may remain observable through the entitlement-free presentation path.

However:

```text
retained artifact exists
        ↓
DOES NOT GRANT
new analytical evaluation
```

A merchant SHALL NOT use retained history as a dormant-record bypass for unlimited new analytics after commercial permission expires.

Examples:

```text
previous monthly observation retained
    → may remain observable where otherwise authorised

new current-month evaluation
    → requires current protected permission

previous Financial Health assessment retained
    → may remain historical evidence

reassessment against current evidence
    → requires current BUSINESS analytical permission
```

Commercial loss does not manufacture a new data-retention rule. Composite MS-PROT-053 remains controlling.

---

# 10. Retry and Already-Committed Evaluation

Recovery of an already-committed analytical result after acknowledgement loss SHALL NOT be treated as a new protected analytical evaluation.

Canonical:

```text
protected permission valid
        ↓
evaluation committed
        ↓
response lost
        ↓
permission later expires
        ↓
recover exact committed result
without recomputation
```

This is residual recovery of an existing result.

By contrast, work that did not commit before permission expired must revalidate current commercial permission before performing a new evaluation.

A retry token SHALL NOT become permission to recompute a new result under changed evidence.

---

# 11. Monitoring and Reassessment

Accepted Financial Health monitoring remains BUSINESS scope.

This amendment classifies the analytical evaluation permission only.

It does not create:

```text
scheduler authority

background-work authority

Merchant Attention occurrence

Notification delivery

Payment collection

filing authority

consequential remediation
```

Where an accepted trigger or schedule asks Business Intelligence to perform a new protected reassessment, current `USE_BUSINESS_ANALYTICS` permission is required unless the operation is recovery of an already-committed exact result.

`MONITOR` remains analytical meaning, not execution authority.

---

# 12. Reports and Exports Remain Separately Classified Work

MS-PROT-083 v1.6 defines the semantic delivery contracts:

```text
business-intelligence/merchant-analytical-report@1

business-intelligence/measure-observation-export@1
```

This amendment does not assign those delivery contracts an independent protected commercial purpose and does not silently classify them as free.

Their exact commercial treatment remains separate catalogue work.

In particular, v1.7 SHALL NOT infer that:

```text
BUSINESS analytics permission
    automatically grants every report/export product
```

or:

```text
presentation without an independent entitlement
    makes bulk/export delivery commercially unrestricted
```

The accepted v1.6 representation, fidelity, privacy and authority requirements remain unchanged.

---

# 13. Financial Operations Boundary

MS-PROT-084 v1.2 separately classifies Financial-Operations-owned record establishment.

The permissions remain orthogonal:

```text
ESTABLISH_FINANCIAL_OPERATIONS_RECORD
    ≠
USE_BUSINESS_ANALYTICS
```

A merchant may be permitted to maintain Financial Operations records without a particular analytical evaluation being currently permitted, or vice versa where another legitimate source supplies the required analytical evidence.

Neither permission substitutes for the other.

Financial Health remains an MS-PROT-083 analytical responsibility even though its source composition consumes MS-PROT-084 material.

---

# 14. Campaign and Marketing Boundary

The permissions remain orthogonal:

```text
USE_CAMPAIGN_ANALYTICS
    ≠
Campaign preparation
    ≠
Campaign execution
    ≠
Audience mutation
    ≠
Outbound contact authority
```

BUSINESS customer-return analytics may support a merchant-initiated contextual handoff into Marketing where accepted authority permits.

That handoff does not create GROWTH Campaign analytics permission and does not create Campaign execution permission.

Likewise, GROWTH Campaign analytics permission does not itself establish lawful contact, audience eligibility, suppression satisfaction, merchant approval or provider readiness.

---

# 15. AI Boundary

AI may assist explanation or interaction around analytical material only within the accepted AI and analytical authorities.

AI SHALL NOT:

```text
manufacture USE_BUSINESS_ANALYTICS

manufacture USE_CAMPAIGN_ANALYTICS

convert presentation into evaluation

invent source evidence

convert UNKNOWN into a favourable assessment

manufacture coverage

create a new metric family

infer causal attribution where not accepted

perform Campaign execution from analytical output
```

Deterministic presentation of otherwise-authorised analytical material SHALL remain available during live AI unavailability where the accepted surface contract requires it.

---

# 16. Commercial Grant Sources

The protected purposes may be satisfied by any accepted current Commercial grant source, including where applicable:

```text
trial

paid Commercial Agreement

Standing Free only if a separately accepted
historical affinity explicitly grants the
exact protected purpose

future accepted remediation or promotional grant
```

Runtime evaluation SHALL resolve exact effective commercial permission.

It SHALL NOT branch directly on mutable plan labels such as:

```text
if plan == BUSINESS

if plan == GROWTH
```

The published catalogue and grant provenance remain controlling.

---

# 17. Failure Semantics

Commercial denial SHALL remain distinct from analytical inapplicability, unavailable evidence and technical failure.

A conforming implementation SHALL preserve at least the following conceptual distinctions where applicable:

```text
COMMERCIAL_PERMISSION_DENIED

COMMERCIAL_PERMISSION_UNRESOLVED

ANALYTICAL_DEFINITION_NOT_APPLICABLE

ACTOR_NOT_AUTHORISED

SOURCE_EVIDENCE_UNAVAILABLE

COVERAGE_INSUFFICIENT_OR_UNRESOLVED

ANALYTICAL_RESULT_UNKNOWN

DATA_LIFECYCLE_RESTRICTED

CONFLICT

TECHNICAL_FAILURE
```

The exact public/API representation remains subject to its governing transport authority.

The system SHALL NOT report:

```text
"no data"
```

when the actual reason is commercial denial.

It SHALL NOT report:

```text
"upgrade required"
```

when the analytical definition is semantically inapplicable or the actor is unauthorised.

---

# 18. Resource Protection

`NO INDEPENDENT COMMERCIAL ENTITLEMENT` does not mean unbounded resource consumption.

Presentation remains subject to accepted Resource Protection, abuse protection, pagination, payload, compute and delivery constraints.

Likewise, a protected analytical entitlement is permission to use an admitted service, not a promise of infinite compute, infinite history or arbitrary custom analytical queries.

Resource governance remains distinct from subscription semantics.

---

# 19. Catalogue Consequence

If admitted into a concrete Commercial Catalogue Manifest, this owner classification supplies the following protected bindings:

```text
business-intelligence/business-analytics-evaluation-access@1
+
USE_BUSINESS_ANALYTICS
+
PLATFORM_SERVICE_ACCESS
+
BUSINESS / GROWTH standard allocation
```

and:

```text
business-intelligence/campaign-analytics-evaluation-access@1
+
USE_CAMPAIGN_ANALYTICS
+
PLATFORM_SERVICE_ACCESS
+
GROWTH standard allocation
```

It also supplies the explicit no-independent-entitlement classification:

```text
business-intelligence/merchant-analytics-presentation-access@1
+
NO INDEPENDENT COMMERCIAL ENTITLEMENT
+
PRESENTATION_PRIVILEGE
```

The final catalogue SHALL NOT infer from these bindings:

```text
source mutation authority

Campaign execution authority

raw-data export authority

report/export permission

future forecasting permission

future optimisation permission

accounting authority

provider authority
```

---

# 20. DQ-001 Consequence

Acceptance of this amendment supplies the Business Intelligence owner-side commercial classifications needed by `MS-PROT-056-V17-DQ-001`.

It does **not** close DQ-001.

The complete concrete Commercial Catalogue Manifest still requires, at minimum:

```text
remaining owner classifications

stable final CommercialEntitlementIdentity values

complete owner-qualified Commercial Access Bindings

explicit FREE grant set

explicit BUSINESS grant set

explicit GROWTH grant set

publication-time hierarchy and consistency checks
```

No missing owner classification may be interpreted as an exemption merely because Business Intelligence is now classified.

---

# 21. Falsification

The accepted boundary MUST survive the following cases.

## 21.1 FREE merchant requests a new operational BI calculation

Expected:

```text
semantic applicability preserved
source business facts preserved
USE_BUSINESS_ANALYTICS absent
new protected evaluation denied
```

PASS.

## 21.2 BUSINESS merchant requests accepted operational BI

Expected:

```text
exact Business analytics grant may satisfy
USE_BUSINESS_ANALYTICS
```

subject to source, actor, coverage, lifecycle and other predicates.

PASS.

## 21.3 BUSINESS merchant requests Campaign analytics

Expected:

```text
USE_BUSINESS_ANALYTICS
DOES NOT satisfy
USE_CAMPAIGN_ANALYTICS
```

PASS.

## 21.4 GROWTH merchant requests accepted Campaign analytics

Expected:

```text
exact GROWTH grant may satisfy
USE_CAMPAIGN_ANALYTICS
```

subject to all analytical predicates.

PASS.

## 21.5 Merchant loses BUSINESS/GROWTH access after analytical history exists

Expected:

```text
retained artifacts are not deleted
legitimate retained observation may continue
new evaluation remains protected
```

PASS.

## 21.6 Merchant opens analytical workspace after downgrade

Expected:

```text
presentation itself requires
no independent entitlement

but no protected portfolio may be
newly evaluated without current permission
```

PASS.

## 21.7 Source Order/Booking/Payment record remains accessible

Expected:

```text
source access does not manufacture
analytics permission
```

PASS.

## 21.8 Financial Operations record establishment is permitted

Expected:

```text
ESTABLISH_FINANCIAL_OPERATIONS_RECORD
DOES NOT manufacture
USE_BUSINESS_ANALYTICS
```

PASS.

## 21.9 Financial Health assessment is retained but current evidence changes

Expected:

```text
old retained assessment may remain historical
new reassessment requires current
USE_BUSINESS_ANALYTICS
```

PASS.

## 21.10 Campaign analytics is available

Expected:

```text
no Campaign, audience or outbound-contact
operation is authorised merely by analytics access
```

PASS.

## 21.11 AI explanation is unavailable

Expected:

```text
otherwise-authorised deterministic presentation
continues where required
```

PASS.

## 21.12 Merchant requests PDF/CSV delivery

Expected:

```text
v1.7 does not invent report/export
commercial permission
```

PASS.

## 21.13 Resource abuse targets entitlement-free presentation

Expected:

```text
Resource Protection still applies
```

PASS.

---

# 22. Rejected Alternatives

The following alternatives are rejected for the current standard catalogue:

```text
one USE_ALL_ANALYTICS wildcard

GROWTH required for every analytical service

one entitlement per metric family

analytics workspace as a separate premium product

presentation as a second toll

source-record access automatically granting analytics

analytics automatically granting source mutation

BUSINESS customer understanding automatically granting Marketing

Campaign analytics automatically granting Campaign execution

retained history granting unlimited future recomputation

report/export permissions inferred from ordinary presentation

AI confidence granting analytical permission
```

---

# 23. Deferred and Out of Scope

This amendment does not resolve or activate:

```text
final CommercialEntitlementIdentity values

complete DQ-001 manifest

prices

allowances or quotas

presence-performance analytics

future forecasting

future optimisation

profitability analytics

agentic CRM

new Campaign attribution methods

report/export commercial classification

new report/export formats

arbitrary custom BI/querying

implementation activation
```

Each remains governed by its existing authority or deferred state.

---

# 24. Final Conformance

| Requirement | Result |
|---|---|
| Fundamental Vision | PASS |
| merchant simplicity | PASS |
| configuration independence from subscription | PASS |
| plan/semantic separation | PASS |
| exact BUSINESS vs GROWTH analytical boundary | PASS |
| supporting presentation has no second toll | PASS |
| source ownership preserved | PASS |
| Financial Operations boundary preserved | PASS |
| Marketing/Campaign authority preserved | PASS |
| historical analytical artifacts preserved | PASS |
| no dormant-history recomputation bypass | PASS |
| analytical honesty and coverage preserved | PASS |
| AI boundary preserved | PASS |
| report/export scope not invented | PASS |
| Resource Protection preserved | PASS |
| DQ-001 remains open | PASS |
| no implementation activation | PASS |

**Final result:** ACCEPTED.
