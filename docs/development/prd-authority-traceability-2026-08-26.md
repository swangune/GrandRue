# Main Street PRD → Accepted Authority Traceability — updated 8 September 2026

**Status:** DERIVED NAVIGATION / COVERAGE EVIDENCE — NOT SEMANTIC AUTHORITY  
**Branch:** `development`  
**Originally established:** 26 August 2026  
**Updated:** 8 September 2026  
**Governed by:** `designs/DOCUMENT-GOVERNANCE.md`, `designs/DESIGN-RULES.md`  
**Purpose:** Expose material PRD concerns that are covered, compositionally covered, intentionally deferred or still lack accepted semantic/design ownership. This file is derived navigation evidence and MUST NOT override Product requirements or accepted semantic/TAS/ADR authority.

---

## 1. Coverage States

```text
COVERED
    accepted authority directly owns the material semantic concern

COVERED AS COMPOSITION
    the PRD names an engine/feature/surface, but accepted capability,
    orchestration, provider, projection, Exposure or surface authorities
    collectively provide the required ownership

PARTIAL — GOVERNED GAP
    material subset is covered but at least one required semantic owner
    remains under governed review

DEFERRED PRODUCT SEMANTICS
    product requirement exists, but semantic design is intentionally not yet
    required for the current implementation slice

STRATEGIC / EXPERIENCE EVIDENCE
    product/market/persona/journey/goal prose constrains design but does not
    require one-to-one MS-PROT ownership
```

A PRD document title is not proof that a same-named software module or capability should exist.

---

## 2. Product-Boundary Revision

The PRD makes the following product hierarchy explicit:

```text
merchant intent
      ↓
approved merchant configuration
      +
capability-owned authoritative state
      ↓
governed Main Street application/runtime execution
      ↓
projections / Exposure / interaction contracts
      ↓
delivery surfaces
```

Delivery surfaces include merchant dashboard, staff/POS experiences, customer interaction flows, public website/storefront, integrations/APIs and AI-assisted interaction.

### 2.1 Authority consequence

This product revision does **not** create a universal `MerchantOperationalModel` aggregate or semantic owner.

`merchant operational model/context` is product/architecture shorthand for coherent composition of bounded authorities such as MS-PROT-051 Profile/Presence, MS-PROT-040 configuration/activation, composite MS-PROT-042 Booking/Appointment Scheduling, MS-PROT-043 Enquiry/Customer context, MS-PROT-046 Publication, MS-PROT-055 Payment, MS-PROT-058 Inventory, composite MS-PROT-074 Workforce delegation/self-service, composite MS-PROT-080 Workforce Compensation, composite MS-PROT-081 Workforce Scheduling/Timekeeping/Leave, MS-PROT-077 Order and applicable later owners.

No authority is transferred into Website, AI, Dashboard or a new central merchant aggregate.

### 2.2 Delivery-surface consequence

MS-PROT-029 v1.2 establishes the merchant operational core/runtime as authoritative and websites, dashboards, staff/POS, customer, integration and AI interaction as surfaces/adapters over shared capability-owned semantics.

MS-PROT-036 v1.2 establishes the storefront as a replaceable projection-and-interaction surface.

### 2.3 AI consequence

MS-PROT-057 establishes AI as assistance/inference rather than semantic, merchant-intent, business-state or deterministic-validation authority. MS-PROT-038 establishes onboarding inference as proposal over registered semantics with merchant correction and deterministic validation.

### 2.4 Workforce Compensation / Payroll consequence — accepted 5 September 2026

Product intent requires Main Street to support **Workforce Compensation**, including employer Payroll and jurisdiction-valid Non-Payroll compensation, as optional post-MVP operating scope.

MS-PROT-080 v1.1 completely supersedes v1.0; composite MS-PROT-080 through v1.3 now consumes exact Workforce Scheduling Arrangement-affined worked-time, break and leave evidence without transferring scheduling/timekeeping/leave ownership into Payroll or discovering the applicable Compensation Relationship later from current person/Membership state.

The composite MS-PROT-080 authority establishes:

```text
Workforce access
    ≠ Compensation Relationship
    ≠ Jurisdiction Pay Treatment
    ≠ Payroll

Compensation Relationship
    → merchant-scoped relationship under which compensation is administered

Compensation Terms
    → may establish calculated or negotiated amounts

Jurisdiction Pay Treatment
    → relationship-scoped PAYROLL or NON_PAYROLL route

NON_PAYROLL
    ≠ no payer-side statutory obligation

Approved Worked-Time Evidence
    ≠ Approved Leave Evidence
    ≠ break-related Compensation consequence
    ≠ Compensation Amount

Merchant Membership
    ≠ Compensation self-service authority

Project Progress
    ≠ Compensation Amount unless Compensation Terms establish that relation

Payroll calculation
    = deterministic and jurisdiction-qualified

Generated agreement / payslip / payment statement
    = governed document/evidence representation
    ≠ competing compensation or calculation authority
```

Composite MS-PROT-080 preserves Workforce access boundaries, reuses MS-PROT-055 monetary semantics without transferring Workforce Compensation into customer Payment, composes with MS-PROT-048 provider fulfilment, MS-PROT-053 privacy, MS-PROT-062/063/064/067 access/security/audit and MS-PROT-065/069/070 durable work/uncertainty/degradation.

Workforce Compensation and Payroll remain post-MVP. Acceptance establishes design authority, not implementation sequencing.

### 2.5 Workforce Scheduling / Timekeeping / Leave consequence — accepted 5 September 2026

Composite MS-PROT-081 through v1.1 establishes a separate Workforce Scheduling, Timekeeping, Scheduled Break and Leave boundary and introduces a narrow **Workforce Scheduling Arrangement** so one Merchant Membership can participate in multiple distinct scheduling/time/leave rule sets and, where applicable, exact Compensation Relationships. It composes with composite MS-PROT-074 through v1.2 personal workforce participation/authorisation, MS-PROT-063 v1.2 Personal Workforce Self-Service Context, composite MS-PROT-080 through v1.3 compensation affinity and composite MS-PROT-042 through v1.8 Appointment interoperability.

It establishes:

```text
Merchant Membership
    ≠ Workforce Scheduling Arrangement
    ≠ Compensation Relationship

Workforce Scheduling Arrangement
    → one coherent scheduling/time/leave affinity
    → optional exact Compensation Relationship affinity

Workforce Scheduling
    ≠ Appointment Scheduling

Shift Offer
    ≠ Scheduled Work Commitment

Scheduled Work Commitment
    → exact Workforce Scheduling Arrangement
    ≠ Attendance

Scheduled Break
    ≠ Actual Break
    ≠ Worked Time
    ≠ break-related Compensation consequence

Excess Break Time
    ≠ Worked Time
    → MUST NOT contribute to time-based compensation

Approved Leave
    → exact Workforce Scheduling Arrangement
    ≠ Leave Pay

Personal Workforce Self-Service
    ≠ Staff Operational Context
    ≠ automatic Compensation access
```

Staff may use a bounded personal-device self-service surface for their own schedule, offered shifts, clock events, break events, time corrections and leave requests/decisions without that device becoming a general merchant-operational device. Own-subject workforce actions may derive Actor Authorisation from exact current Membership + Scheduling Arrangement + trusted self-service context; another-worker administration remains merchant operational work. Compensation/pay-document access additionally requires exact related-Payee/Compensation Relationship authority under MS-PROT-080.

Where a Workforce Scheduling Arrangement is explicitly linked to a customer-facing schedulable Resource, Appointment Scheduling may consume current owner-qualified Workforce availability evidence from that exact Arrangement. Identity, display-name or Membership similarity alone does not create the link. Workforce Scheduling does not own Appointment commitment truth, and later workforce changes do not silently cancel existing customer Appointments.

Acceptance of this package does not reprioritise production implementation.

---

## 3. Current High-Value Traceability Matrix

| PRD concern / file | Coverage | Current authority / position |
|---|---|---|
| `foundation.md`, `vision.md`, `mission.md`, `problem-statement.md`, `product-positioning.md`, `guiding-principles.md`, `product-philosophy.md` | **STRATEGIC / EXPERIENCE EVIDENCE** | Operational core/runtime is the durable product centre; Workforce Scheduling and Workforce Compensation are optional future operational scope unless separately sequenced. |
| `merchant-onboarding-experience.md` | **COVERED** | MS-PROT-038, MS-PROT-039, MS-PROT-040, MS-PROT-052 and applicable identity/trust/account authorities. Workforce Scheduling/Compensation are not universal onboarding requirements. |
| `business-profile-specs.md` | **COVERED** | MS-PROT-051, MS-PROT-050, MS-PROT-066, MS-PROT-027. |
| `website-generation-engine.md` | **COVERED AS COMPOSITION** | MS-PROT-036 v1.2, MS-PROT-029 v1.2, MS-PROT-049, MS-PROT-027, MS-PROT-051, MS-PROT-066. |
| `merchant-dashboard.md` | **COVERED** | MS-PROT-037, MS-PROT-049, MS-PROT-027, MS-PROT-062; workforce-management projections are additionally constrained by composite MS-PROT-081 and composite MS-PROT-074/MS-PROT-063. |
| `ai-platform.md`; `ai-platfome-and automation-engine.md` | **COVERED AS COMPOSITION** | MS-PROT-057 + applicable capability/application authorities. AI has no autonomous jurisdiction-treatment, Payroll approval, statutory-calculation, worked-time approval, leave approval, Scheduling Arrangement selection or settlement authority. |
| `booking-and-commerce-engine.md` | **COVERED AS COMPOSITION** | Booking/Appointment composite MS-PROT-042 through v1.8; Order MS-PROT-077; Payment MS-PROT-055; Inventory MS-PROT-058; Fulfilment MS-PROT-060; Returns MS-PROT-061; orchestration MS-PROT-072. |
| `customer-relationship-management.md` | **COVERED** | MS-PROT-043. |
| `customer-support-and-communication.md` | **COVERED AS COMPOSITION — NOT IMPLEMENTATION-PROMOTED** | Composite MS-PROT-043 + MS-PROT-075 + MS-PROT-085 + MS-PROT-086, with applicable access, data-protection and source authorities. Production channel, automated-response, guest-access and attachment portfolios remain gated. |
| `dps-engine.md` | **COVERED AS COMPOSITION** | MS-PROT-051/026/048/065/064/067/069/070/075 composition. |
| `identity-and-authentication.md` | **COVERED** | MS-PROT-028; composite MS-PROT-063 through v1.2; MS-PROT-071; composite MS-PROT-074 through v1.2; MS-PROT-076. |
| `local-discovery-and-seoengine.md` | **COVERED AS COMPOSITION / IMPLEMENTATION-HEAVY** | MS-PROT-051/036/027 plus provider/credential/resilience authorities. |
| `announcement-engine.md` | **COVERED** | MS-PROT-046 + MS-PROT-075 where delivery is required. |
| `platform-integration.md` | **COVERED AS COMPOSITION** | MS-PROT-048 provider fulfilment + composite MS-PROT-080 jurisdiction treatment/regulatory execution boundary + MS-PROT-067/069/070/065. Exact jurisdiction/provider/API bindings remain deferred. |
| `team-and-staff-management.md` — workforce access | **COVERED** | Composite MS-PROT-074 through v1.2 + composite MS-PROT-063 through v1.2 + MS-PROT-062. |
| `team-and-staff-management.md` — scheduling / timekeeping / breaks / leave | **COVERED — NOT IMPLEMENTATION-PROMOTED** | Composite MS-PROT-081 through v1.1; own-subject access by composite MS-PROT-074/MS-PROT-063; Appointment interoperability by composite MS-PROT-042 through v1.8; compensation handoff by composite MS-PROT-080 through v1.3. |
| `team-and-staff-management.md` — Workforce Compensation / Payroll extension | **COVERED — POST-MVP** | Composite MS-PROT-080 through v1.3. Compensation Relationships, Compensation Terms and jurisdiction-qualified treatment remain distinct from Workforce access, scheduling and universal employment classification; staff pay access requires exact related-Payee/Compensation Relationship authority. |
| `product-roadmap-and-future-capabilities.md`; `minimum-viable-product.md` — Workforce Scheduling / Compensation / Payroll | **STRATEGIC PRODUCT SCOPE — NOT CURRENTLY SEQUENCED** | Accepted architecturally by composite MS-PROT-081 and composite MS-PROT-080, but acceptance does not alter the current implementation programme. |
| `subscription-and-billing.md` | **COVERED** | MS-PROT-056 + MS-PROT-055. Workforce Compensation remains distinct. |
| `analytics-and-business-intelligence.md` | **COVERED — NOT IMPLEMENTATION-PROMOTED** | MS-PROT-083 governs Analytical Measures, Business Health, claims, methods, forecasts, scenarios, recommendations and evidence coverage. Concrete production portfolios remain deferred under its registered DQs. |
| `marketing-and-campaign-engine.md` | **COVERED — NOT IMPLEMENTATION-PROMOTED** | MS-PROT-087 governs bounded Campaign purpose/revision, Audience Definitions, recipient eligibility, approval, occurrences, suppression and outcome evidence; MS-PROT-046/075/086/083 and data-protection/source authorities retain Publication, delivery, Conversation and analytical ownership. Production portfolios remain gated by `MS-PROT-087-DQ-001` through `MS-PROT-087-DQ-006`. |

---

## 4. Workforce Scheduling / Compensation Falsification Result

| Hypothesis | Result |
|---|---|
| Everyone who performs work belongs in Payroll | **REJECTED** — Workforce Compensation can resolve to Payroll or Non-Payroll per relationship and jurisdiction |
| Merchant selecting `self-employed` is sufficient | **REJECTED** — merchant labels are evidence, not jurisdiction-treatment authority |
| Hourly pay, salary, invoice, project pay or no guaranteed hours determines legal/pay treatment | **REJECTED** — compensation mechanism and treatment are separate |
| Payroll treatment determines whether shifts are direct-assigned or accept/reject | **REJECTED** — Workforce Time Terms own the work-allocation mechanism |
| One Merchant Membership always has one scheduling/pay relationship | **REJECTED** — one Membership may hold multiple exact Workforce Scheduling Arrangements and Compensation Relationships |
| Worked-time evidence may locate the worker's current Compensation Relationship later | **REJECTED** — evidence retains exact Scheduling Arrangement/Compensation Relationship affinity; current-state lookup is unsafe |
| Leave for one work arrangement automatically applies to every arrangement for that person | **REJECTED** — Leave is Scheduling Arrangement-scoped; multi-arrangement UX may coordinate but does not merge authority |
| Same person has one global employment/pay status across merchants | **REJECTED** — treatment attaches to the Compensation Relationship |
| Non-Payroll means no payer-side statutory obligations | **REJECTED** — withholding, reporting, contribution, remittance or document duties may remain jurisdiction-qualified |
| Compensation must be salary/hour formula | **REJECTED** — negotiated, project-progress and milestone amounts are also supported |
| Scheduled hours equal worked hours | **REJECTED** — Scheduled Work Commitment and Timekeeping evidence are distinct |
| Scheduled Break proves the break actually taken | **REJECTED** — Actual Break derives from Timekeeping evidence |
| `Paid Break Entitlement` is needed as a generic authority | **REJECTED** — break monetary consequence is Compensation-owned and derived from exact break evidence, Compensation Terms and jurisdiction rules |
| Excess break counts as Worked Time or hourly pay | **REJECTED** — Excess Break Time is not Worked Time and cannot contribute to time-based compensation |
| Excess break automatically reduces salary | **REJECTED** — fixed/salaried monetary consequence requires applicable terms and jurisdiction rules |
| Approved leave itself calculates holiday pay | **REJECTED** — Leave owns approved absence; Workforce Compensation owns monetary consequence |
| Payroll participation automatically determines leave eligibility | **REJECTED** — relationship/time terms and jurisdiction govern leave eligibility |
| Merchant Membership itself grants own-shift/time/leave authority | **REJECTED** — own-subject authority additionally requires exact trusted self-service context, Arrangement where applicable and capability-owned eligibility |
| Staff personal phone must become a merchant-operational device to view own rota/time/leave | **REJECTED** — Personal Workforce Self-Service Context is purpose-bound and narrower than Staff Operational Context |
| A manager may approve another worker's time/leave from personal self-service merely because they have a manager role | **REJECTED** — another-worker administration remains merchant operational work |
| Merchant Membership automatically grants pay-document access | **REJECTED** — Compensation requires exact related-Payee/Compensation Relationship authority |
| Ending Membership automatically destroys historical payslip eligibility | **REJECTED** — historical Compensation eligibility may survive; exact former-Payee authentication/API/Surface/Audience remains deferred |
| Legal-entity Payee representative is established by authenticated human identity alone | **REJECTED** — separate representative authority is required |
| A schedulable Resource can consume every schedule/leave fact of the same Membership | **REJECTED** — only explicitly linked Workforce Scheduling Arrangement evidence may constrain the Resource |
| Workforce schedule/leave may silently cancel an existing customer Appointment | **REJECTED** — Appointment commitment remains independently authoritative; conflict/remediation is explicit |
| Agreement acceptance always requires Scheduling + Compensation mutation atomicity | **REJECTED** — MS-PROT-072 permits cross-capability atomicity only where a separately accepted invariant requires it |
| Project progress automatically determines payable amount | **REJECTED** — only Compensation Terms may establish that relationship |
| Verbal compensation terms cannot be tracked | **REJECTED** — provenance-aware merchant-recorded verbal terms are permitted where the jurisdiction permits them |
| Payment must pass through Main Street to be tracked | **REJECTED** — merchant-reported external payment can be recorded without becoming bank-settlement truth |
| Generated agreement can manufacture contractor/self-employed treatment | **REJECTED** — treatment precedes legally treatment-dependent document generation |
| Generated payslip/payment statement may redefine the underlying calculation | **REJECTED** — documents derive from authoritative Compensation/Payroll facts |
| Customer Payment can own Workforce Compensation | **REJECTED** — commercial compensation and customer Payment remain separately owned |
| Main Street should build one universal statutory scheme/tax ontology | **REJECTED** — jurisdiction contracts isolate regulatory variation |
| Payroll provider must sit between Main Street and government | **REJECTED** — direct authority adapters and mixed fulfilment are valid |
| HTTP success proves statutory filing/remittance | **REJECTED** — transport, acknowledgement, processing and obligation satisfaction differ |
| Full country support can mean employee Payroll only | **REJECTED** — declared Workforce Compensation support must state and validate its Payroll and Non-Payroll scope |
| AI may classify relationships, approve time/leave, select ambiguous Scheduling Arrangement or calculate statutory amounts authoritatively | **REJECTED** — AI remains assistive only |
| Acceptance of the workforce package should immediately change the implementation sequence | **REJECTED** — semantic acceptance does not reprioritise implementation |

The Workforce Scheduling/Timekeeping/Leave and Workforce Compensation boundaries are accepted. Concrete implementations remain downstream until deliberately sequenced.

---

## 5. MVP-Specific Coverage Check

| MVP requirement | Current result |
|---|---|
| Merchant registration/onboarding | COVERED |
| Merchant profile/hours/location/media | COVERED FOUNDATION |
| Website/storefront | COVERED AS DELIVERY-SURFACE COMPOSITION |
| Products/services/publication | COVERED FOUNDATION |
| Enquiries/customer context | COVERED |
| Booking/Appointment | COVERED |
| Online physical-product Order | COVERED — MS-PROT-077 + v1.1 |
| Payment | COVERED |
| Staff management/access | COVERED |
| Staff scheduling/timekeeping/leave | **ACCEPTED DESIGN — NOT CURRENTLY IMPLEMENTATION-PROMOTED** |
| Basic communication/notifications | COVERED |
| Announcement/social syndication | COVERED AS COMPOSITION |
| AI-assisted interaction | COVERED AT CURRENT BOUNDARY |
| Basic Analytics | **SEMANTIC DESIGN REQUIRED BEFORE ANALYTICS IMPLEMENTATION** |
| Workforce Compensation / Payroll | **ACCEPTED DESIGN — DEFERRED BEYOND MVP** |

The accepted workforce package introduces no new blocker to the current implementation programme because acceptance does not itself alter sequencing.

---

## 6. Analytics Gap Classification

Any future Analytics authority must derive from existing capability-owned facts and define explicit metric identity, calculation/aggregation rules, scope/dimensions, units/currency, time semantics, lineage/provenance, freshness/serviceability and access/Exposure.

Future labour-cost, contractor-payment, Workforce Scheduling, Workforce Compensation or Payroll analytics must follow the same rule and cannot become operational truth.

---

## 7. Strategic / Experience PRD Documents

The following remain primarily product/market/experience evidence and should not be forced into one-to-one semantic authorities merely because their prose changes:

```text
constraints-and-assumptions.md
customer-journey.md
foundation.md
glossary.md
goals-and-metrics.md
guiding-principles.md
market-opportunity.md
merchant-journey.md
merchnat-personas.md
minimum-viable-product.md
mission.md
non-goals.md
problem-statement.md
product-philosophy.md
product-positioning.md
product-roadmap-and-future-capabilities.md
product-success-metrics.md
risk-and-mitigation.md
scalability-and-future-evolution.md
target.md
vision.md
```

When strategic prose introduces a material rule needed by implementation, that rule must trace to an accepted semantic/design owner before code invents it.

---

## 8. Maintenance Rule

This traceability view should be updated when a material PRD concern is deliberately selected for implementation, a new semantic/design authority closes a listed gap, a PRD requirement materially changes, or vertical-slice review discovers a misclassification.

Accepted authority navigation remains `designs/AUTHORITY-INDEX.md`.

---

## 9. Current Result

```text
AI-era operational-core product boundary              COVERED
Website as replaceable delivery surface               COVERED
AI as subordinate assistance/interaction              COVERED
Adaptive onboarding alignment                         COVERED
Order / Ordering                                      COVERED — MS-PROT-077
Workforce access                                      COVERED — composite MS-PROT-074 / MS-PROT-063
Workforce Scheduling / Timekeeping / Leave            COVERED — composite MS-PROT-081 through v1.1
Workforce Compensation product requirement            APPROVED — POST-MVP
Workforce Compensation semantic/design boundary       COVERED — composite MS-PROT-080 through v1.3
Workforce ↔ Appointment affinity                      COVERED — composite MS-PROT-042 through v1.8
Generic/former/legal-entity Payee access transport    DEFERRED UNTIL PROMOTED
Jurisdiction/provider/API implementation              DEFERRED UNTIL SEQUENCED
Basic Analytics                                       DEFERRED PRODUCT SEMANTIC DESIGN
Generic Campaign semantics                            DEFERRED PRODUCT SEMANTIC DESIGN
Other inspected MVP foundation                        COVERED / COVERED AS COMPOSITION
```

Main Street can therefore support staff scheduling/timekeeping/leave and Workforce Compensation internationally without collapsing workforce access, Scheduling Arrangements, customer Appointment Scheduling, compensation, Payroll, customer Payment, one universal statutory ontology or an external provider into one owner.