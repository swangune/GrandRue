# MS-PROT-056 — Analytical Portfolio Allocation & Presentation Access Amendment

**Document ID:** MS-PROT-056
**Version:** 1.8
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete amendment and limited formalisation scope
**Authority type:** Commercial allocation policy amendment
**Governed by:** `DESIGN-RULES.md` v2.3; `DOCUMENT-GOVERNANCE.md` v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-056 v1.7 within analytical portfolio allocation and supporting presentation access only
**Depends on:** MS-PROT-056 v1.5 §§12–20; v1.7 §§4, 7–12; MS-PROT-083 v1.1 §20, v1.2 §§26–42 and v1.4; composite MS-PROT-087
**Implementation activation:** NONE
**Purpose:** Complete the commercial placement of existing customer-return and campaign measure portfolios and prevent analytical presentation from imposing a separate higher-tier requirement.

### 1. Governing decision

The initial customer-return measure portfolio SHALL belong to BUSINESS.

The initial campaign measure portfolio SHALL belong to GROWTH.

Presentation of an otherwise commercially entitled analytical service SHALL NOT require a higher standard tier solely because that presentation uses the Merchant Analytical Surface, natural-language explanation, charts or progressive disclosure.

These are commercial allocations, not executable entitlement grants.

### 2. Scope and non-goals

This amendment governs commercial placement of the exact existing portfolios named below.

It does not introduce:

- new measures, indicators, forecasts or analytical methods;
- new customer classifications, marketing audiences or campaign operations;
- executable entitlement identities or target bindings;
- prices, quotas or catalogue publication dates;
- new authorisation, Exposure or residual-access classifications;
- production activation or implementation readiness.

FREE reservations for presence-performance analytics remain reservations under MS-PROT-056 v1.7 §5.

### 3. Customer-return portfolio: BUSINESS

BUSINESS SHALL commercially include these four MS-PROT-083 v1.2 §26 Measure Definition families:

```text
customer-return/qualified-activity-count@1
customer-return/activity-customer-classification-count@1
customer-return/returning-customer-share@1
customer-return/returned-after-quiet-period-count@1
```

GROWTH SHALL include the same allocations through the standard hierarchy.

Their definitions, source relationships, observation windows, historical coverage, identity correction and data-protection requirements remain owned by MS-PROT-083 and its referenced source authorities.

Commercial allocation SHALL NOT:

- turn activity counts into customer counts;
- present returning-customer share as a universal retention rate;
- replace unresolved history with a favourable classification;
- introduce a default quiet-period duration;
- create durable customer lifecycle or scoring fields.

### 4. Campaign measure portfolio: GROWTH

GROWTH SHALL commercially include these five MS-PROT-083 v1.1 §20 Measure Definition families:

```text
campaign/recipient-eligibility-count@1
campaign/direct-email-responsibility-count@1
campaign/direct-email-delivery-evidence-count@1
campaign/campaign-linked-suppression-count@1
campaign/website-announcement-publication-count@1
```

This allocation accompanies the Campaign and audience portfolios already placed in GROWTH by MS-PROT-056 v1.7 §9.

It SHALL NOT introduce open rates, click-through rates, conversion rates, campaign revenue, retention uplift or causal attribution.

Standalone publication and announcement services SHALL retain their existing allocation. A campaign publication measure does not reclassify ordinary publication as GROWTH.

### 5. Existing BUSINESS analytical allocations

The five Financial Health families in MS-PROT-056 v1.7 §7 and seven general operational BI families in §8 remain BUSINESS allocations.

Financial monitoring remains governed by v1.7 §7. Presentation of `MONITOR` SHALL NOT create a schedule, Merchant Attention occurrence or consequential operation.

This amendment does not change taxation, invoicing, quotation, payroll, payslip or agentic CRM placement.

### 6. Analytical presentation follows the represented service

MS-PROT-083 v1.4 continues to govern the `analytics-workspace` contribution and `business-intelligence/merchant-analytics` Projection Contract.

Neither identity SHALL be treated as a blanket entitlement to every analytical portfolio.

For an analytical service admitted through an exact catalogue binding:

- its supporting presentation SHALL NOT impose an additional higher-tier requirement;
- workspace eligibility SHALL remain subject to MS-PROT-083 v1.4 §3;
- each represented artifact SHALL remain subject to its applicable commercial permission, actor authority and Exposure requirements;
- permission to observe one portfolio SHALL NOT grant access to another portfolio.

The Mandatory Honesty Envelope, accessible representation and deterministic presentation during AI unavailability SHALL NOT be premium tier differentiators.

### 7. Customer understanding does not grant Marketing authority

BUSINESS access to customer-return measures SHALL NOT grant GROWTH Campaign preparation or execution purposes.

The merchant-initiated contextual handoff governed by MS-PROT-083 v1.2 §39 remains a handoff into Marketing, not permission to execute Marketing operations.

Any resulting Marketing operation remains subject to its own commercial permission, actor authority, audience rules, suppression, contact policy, approval and provider requirements.

Customer-return evaluation SHALL NOT automatically create or execute a Campaign.

### 8. Ownership, lifecycle and historical boundaries

Commercial owns allocation and entitlement definitions. Business Intelligence owns analytical meaning. Source capabilities own business facts. Marketing owns Campaign semantics. Presentation remains governed by MS-PROT-083 v1.4 and its composition authorities.

This amendment introduces no authoritative mutation operation, event, provider integration or new durable fact type.

Consequently, it introduces no independent transaction, retry or concurrency mechanism.

Exact entitlement admission remains governed by MS-PROT-056 v1.7 §4. Grant provenance and independent grant sources remain governed by v1.5 §§12–16. Historical Standing Free affinity remains governed by v1.6.

This amendment SHALL NOT rewrite existing plan snapshots, migrate Standing Free baselines or delete retained analytical history.

It creates no additional residual-access classification. Retention of historical information SHALL NOT itself imply commercial permission for a new analytical evaluation.

### 9. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING.**

The allocation lets an ordinary merchant understand represented customer continuity within BUSINESS, while separately purchasing Campaign services through GROWTH.

Merchants and staff need not administer a separate dashboard subscription or understand analytical infrastructure.

No new capability or external infrastructure is being recreated. The proposal allocates existing governed services and preserves role-native, business-language presentation.

### 10. Review and falsification

Evidence consists of authority inspection and scenario analysis, not executable implementation proof.

| Challenge | Required outcome |
|---|---|
| Barber wants to understand returning customers | BUSINESS includes the four bounded customer-return measures |
| Consultant has incomplete customer history | The returning-share result remains unresolved where its definition requires |
| Retailer wants campaign delivery evidence | The five campaign measures belong to GROWTH; delivery evidence does not become sales attribution |
| Hybrid merchant has qualifying Order and Appointment activity | Existing cross-capability customer-return semantics remain controlling |
| BUSINESS merchant selects a re-engagement handoff | Analytical access does not bypass Marketing’s commercial and execution checks |
| Worker lacks financial observation authority | Subscription level does not expose financial analytical material |
| AI explanation is unavailable | Deterministic presentation of entitled analytical material remains supported |
| Merchant loses GROWTH while BUSINESS remains | BUSINESS allocations remain; existing history and residual access follow their separate authorities |

These scenarios support the allocation boundary. They do not prove production performance, operational readiness or universal platform generality.

### 11. Alternatives and trade-offs

**Rejected: place all customer-return analytics in GROWTH.**
This would put bounded understanding of existing customer activity outside BUSINESS despite its ordinary operational value.

**Rejected: make the Analytics workspace itself GROWTH-only.**
That would obstruct presentation of BUSINESS financial and operational services.

**Rejected: let BUSINESS analytical access include Campaign execution.**
Understanding customer activity and conducting discretionary outreach are separately governed purposes.

**Chosen trade-off:** BUSINESS includes substantial customer understanding; GROWTH adds governed Campaign services and their measures. Pricing and cost feasibility remain unresolved.

### 12. Amendment effect and readiness

This amendment adds explicit customer-return and campaign measure allocation and clarifies supporting analytical presentation access.

All other MS-PROT-056 v1.7 allocations, reservations and admission barriers remain unchanged.

`MS-PROT-056-V17-DQ-001` remains OPEN: this amendment narrows analytical allocation uncertainty but does not supply the complete entitlement catalogue or exact target bindings.

DQ-002, DQ-003 and DQ-004 remain OPEN.

**The executable catalogue and C3 activation remain not implementation-ready.**
