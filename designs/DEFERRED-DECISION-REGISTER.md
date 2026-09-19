# GrandRue Deferred Decision Register

**Document ID:** MS-DEFERRED-DECISION-REGISTER-001
**Version:** 5.2
**Status:** Accepted governance navigation authority
**Governed by:** `DOCUMENT-GOVERNANCE.md`
**Purpose:** Provide the single current work-queue view of unresolved, deferred, resolved and deliberately promoted GrandRue design decisions while using Git history and accepted resolving authorities as provenance.

---

## 1. Governing Rule

This file is the sole current Deferred Decision Register.

Future accepted DDR changes MUST be merged into this file and versioned in place. Separate `CURRENT`, closure-amendment or version-companion DDR files MUST NOT be created.

Git history preserves the register's revision history. Accepted MS-PROT authorities remain the substantive source of semantic decisions that resolve or narrow a deferred item; accepted TAS/ADR authorities may resolve deliberately promoted production/implementation-architecture questions while remaining subordinate to those semantic authorities.

```text
DEFERRED-DECISION-REGISTER.md
    → current design work queue / status

AUTHORITY-INDEX.md
    → current accepted authority navigation

accepted authority
    → substantive answer within its layer/scope
```

---

## 2. Current Operational-Decision Status

| Decision | Current status | Resolving authority / current position |
|---|---|---|
| DDR-OD-001 | **RESOLVED** | Composite MS-PROT-053 through v1.3 — Data Protection, Retention, Durable Evidence, Purpose-Bound Personal Media Use, Production Data Lifecycle Evaluation/Disposition/Convergence, and Enquiry/Customer Communication retention qualification/periods |
| DDR-OD-002 | **RESOLVED** | MS-PROT-054 — Semantic Release, Compatibility & Migration Model |
| DDR-OD-003 | **RESOLVED** | Composite MS-PROT-055 through v1.1 — Money, Commercial Terms, Payment Obligations, Provider Execution, Reconciliation & Refund Execution |
| DDR-OD-004 | **RESOLVED** | MS-PROT-056 accepted amendment chain — subscription plans, full-experience trial, Merchant Commercial Agreement, entitlement binding and commercial-access boundary |
| DDR-OD-005 | **RESOLVED** | MS-PROT-050 v1.3 and surviving earlier MS-PROT-050 authority — time/operating-hours resolution scope |
| DDR-OD-006 | **RESOLVED** | MS-PROT-042 v1.3 and surviving Booking/Appointment authority — merchant-set policy boundary |
| DDR-OD-007 | **RESOLVED** | MS-PROT-043 v1.3 and surviving CustomerContext/Enquiry authority |
| DDR-OD-008 | **RESOLVED** | MS-PROT-027 v1.2 and surviving Projection/Exposure authority |
| DDR-OD-009 | **RESOLVED** | MS-PROT-044 v1.1 plus composite MS-PROT-058 through v1.2 — Product/Offering/Inventory/variant boundary, Inventory authority and returned-stock receipt/disposition |

No original DDR-OD row is currently open.

### 2.1 Implementation-discovered design gates

| ID | Status | Decision | Resolving authority / current position |
|---|---|---|---|
| IMP-05-B2-DG-001 | **RESOLVED** | Stable weekly Public Business Hours mutation, revision/currentness, concurrency, idempotency and actor authority | Resolved by MS-PROT-050 v1.4: immutable exact scope-affined revisions, one durable current pointer, explicit withdrawal, exact expected revision, logical retry identity and current-Controller authority; location-scoped implementation remains dependent on Merchant Location authority |
| IMP-05-A3B-DG-001 | **RESOLVED** | Minimum versioned Merchant Service Area structured/bounded-geography representation and immutable revision evidence | Resolved by MS-PROT-051 v1.3: `NAMED_AREA`, `MERCHANT_LOCATION_RADIUS`, `COUNTRY_WIDE` and `REMOTE_COUNTRIES` variants, exact Location-revision affinity where applicable, merchant-approved description/exposure and a hard non-executable geography boundary |
| IMP-05-A3D-DG-001 | **RESOLVED** | Minimum versioned non-standardising Merchant Classification Entry representation, stable identity and mutation/revision evidence | Resolved by MS-PROT-051 v1.4: independent merchant-scoped entry identities, immutable `CATEGORY`, `DISCOVERY_TAG` or `CONTEXTUAL_DESCRIPTOR` kind, merchant-approved normalized free-text labels, explicit source exposure, exact revision/currentness/concurrency/retry evidence and a hard non-executable boundary; `MS-PROT-051-V11-DQ-012` remains deferred and inactive |
| IMP-05-R2-DG-001 | **RESOLVED** | Durable applicable approval authority for ordinary non-initial Configuration Revisions, including current Controller authority, exact evidence affinity and post-transfer applicability | Resolved by MS-PROT-040 v1.6: current active Merchant Controller only for the current MVP; exact validation/impact/package and Controller Relationship affinity; historical approval remains append-only but loses applicability when that exact relationship is no longer current; no Workforce approval privilege is created |
| IMP-05-R3-DG-001 | **RESOLVED** | Concrete ordinary non-initial Configuration activation actor and trusted execution/current-account authority boundary | Resolved by MS-PROT-040 v1.7: trusted authenticated execution context plus current ACTIVE Merchant Controller, OPEN Merchant Account and no effective account-wide Suspension; caller-supplied principal identifiers, staff roles and historical approval do not establish activation authority; reinstatement remains a fresh current decision |
| IMP-05-R3B-DG-001 | **RESOLVED** | Exact causal affinity that distinguishes a fresh Configuration reinstatement decision from historical approval/evidence when revision or Controller Relationship identity recurs | Resolved by MS-PROT-040 v1.8: every reinstatement decision is bound to the exact current Reinstatement Basis Activation; validation, impact review and approval carry that exact basis; superseded initial revisions are eligible only through the bounded reinstatement path; historical approval, timestamp recency and same-revision recurrence do not establish current applicability |
| IMP-06-P2-DG-001 | **RESOLVED** | Exact production Projection Serviceability evaluator binding, current source-evidence input, fail-closed result and retained provenance | Resolved by MS-PROT-027 v1.8: exact-release category-qualified evaluator bindings, server-established evidence for every declared source, deterministic `FULLY_SERVICEABLE`, `REDUCED_SERVICEABLE` or `NOT_SERVICEABLE` evaluation and exact consumed evaluator/evidence provenance; P2 adds no projection persistence, transport or Exposure authority |
| IMP-06-E3-DG-001 | **RESOLVED** | Exact trusted Audience Observation Context establishment, subject/extension boundary, current audience admission and API-surface-bound Exposure output | Resolved by MS-PROT-027 v1.9: internal scope/release establishment, exact Active Release and Semantic Registry Release affinity, closed observation subjects, typed owner-bound contextual proofs/contributions, current fail-closed admission and opaque API/internal result separation; E3 adds no E4 evaluator, raw credential, continuation, HTTP or semantic-bundle encoding authority |
| IMP-06-E3-DG-002 | **RESOLVED** | Exact production representation and invariants of `ApiExposedElementSet`, including retained identity/provenance and the authority boundary between Exposure resolution and downstream API transport | Resolved by MS-PROT-027 v1.10: immutable positive-membership-only API result, private request/invocation/release/contract provenance, structural failure distinct from successful empty resolution, and a shallow policy-free result binder. Later MS-PROT-027 v1.11 refines positive membership to preserve required stable candidate-instance identity without reopening this E3 boundary. |
| IMP-06-E4-DG-001 | **RESOLVED** | Exact owner-evaluator runtime binding, multi-instance member identity and deterministic production Exposure resolution without semantic gravity | Resolved by MS-PROT-027 v1.11 together with MS-PROT-051 v1.5: instance-aware membership, deterministic element/audience contract resolution, exact-release owner-filtered read-only batch evaluators, Profile-owned Merchant Location Exposure-choice authority and value-blind E4. The then-downstream S2/T1b4 bounded-read representation-affinity gate is separately resolved by MS-PROT-027 v1.13. |
| IMP-06-E4-DG-002 | **RESOLVED** | Exact mechanical evaluator-result submission affinity needed to reject cross-request, cross-invocation and cross-evaluator result reuse without contaminating stable candidate identity | Resolved by MS-PROT-027 v1.12: E4 issues a fresh opaque binding for every candidate × logical evaluator call, owner evaluators receive immutable binding-plus-candidate submissions and return binding-plus-decision rows, and E4 requires exact current-call binding coverage. The binding is ephemeral correlation/integrity evidence only; it does not enter `OwnerExposureEvaluationContext`, stable candidate identity, positive Exposure membership, persistence, semantic bundles or downstream read authority. |
| IMP-06-S2-T1B4-DG-001 | **RESOLVED** | Exact bounded Projection read material and representation affinity needed to ensure P2/E4-approved candidates render values from the same request-bound observation without post-E4 owner-state reacquisition | Resolved by MS-PROT-027 v1.13: request-bound typed Projection material carries exact source-progress/evidence affinity and a fresh bounded-read binding; P2 is bound to that exact read; value/revision-affined E4 owner evaluators consume owner-qualified expected-progress observation contributions and fail closed on mismatch; final S2/T1b4 representation selects positive members only from the same bounded read. E4 remains value-blind, no generic payload envelope or final-currentness fence is introduced, and T4 continuation remains separate. |
| IMP-06-S3-DG-001 | **RESOLVED** | Production capability-owned Public Interaction participation-source contract and generic zero-source S3 completion boundary | Resolved by MS-PROT-049 v1.4: owner-qualified capability-owned participation sources, deterministic single-owner routing and a valid fail-closed empty production-source registry allow the generic S3 projector to be conformingly implemented without concrete capability participation; concrete production sources remain with their owning vertical slices and the `IMP-06 HARD → IMP-07` macro edge is unchanged. |

---

## 3. Accepted Designs Added After the Original DDR Scope

The original DDR predates later accepted authorities. The following accepted semantic/design authorities MUST NOT be reopened merely because they did not originate as DDR rows:

```text
MS-PROT-057
    Merchant AI Concierge & Specialist Semantic Inference Graph
    including accepted v1.1 Production AI Inference Contract, Provenance & Safety Boundary Amendment
    and accepted v1.2 AI Assistance Responsibility, Merchant-Intent & Progressive Website Assistance Governance Amendment

MS-PROT-058
    Inventory Authority, Stock Claims & Availability Model
    including accepted v1.1 Inventory Position, Mutation & Quantity Claim Execution Contract Amendment
    and accepted v1.2 Returned Stock Receipt, Disposition & Sellable Re-entry Contract Amendment

MS-PROT-059
    Unified Interaction Channels, Authoritative Operations & Idempotency Model

MS-PROT-060
    Physical Order Fulfilment, Shipment & Merchant Delivery Policy Model
    including accepted v1.1 Production Order Fulfilment, Shipment Execution & Tracking Contract Amendment

MS-PROT-061
    Merchant Return Policy, Customer Contact & Courier Return-Label Integration Boundary
    including accepted v1.1 Optional Merchant Returns Policy Capability & Production Return-Action Execution Contract Amendment
    and accepted v1.2 Returns Commercial Access Classification Amendment

MS-PROT-062
    Runtime Access, Eligibility & Execution Decision Composition Model
    including accepted v1.1 Resource Protection Admission amendment

MS-PROT-063
    Authentication, Session & Trusted Execution Principal Establishment Model
    including accepted v1.1 Staff Operational Device Context amendment
    and accepted v1.2 Personal Workforce Self-Service Trusted Context Amendment

MS-PROT-064
    Audit Evidence, Operational Intervention & Administrative Action Model

MS-PROT-065
    Durable Background Work, Timers, Retry & Scheduled Execution Model
    including accepted v1.1 Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment

MS-PROT-066
    Media Asset, Optimisation, Rendition & Access Lifecycle Model
    including accepted v1.1 video-delivery scope and v1.2
    Media Delivery Fidelity & Long-Form Information amendment

MS-PROT-067
    Credential, Secret & External Connection Security Model

MS-PROT-068
    Operational Observability, Health & Diagnostic Evidence Model
    including accepted v1.1 Production Operational Evidence, Health & Alerting Contract Amendment

MS-PROT-069
    Network Failure, Execution Uncertainty & Acknowledgement Model
    including accepted v1.1 Production Reconciliation Responsibility, Evidence & Resolution Contract Amendment

MS-PROT-070
    Resilience, Controlled Degradation & Dependency Failure Model

MS-PROT-071
    Merchant Account Establishment & Tenancy Identity Model

MS-PROT-072
    Cross-Capability Application Orchestration & Consistency Model

MS-PROT-073
    Platform Resource Fairness, Rate Limiting & Abuse Protection Model

MS-PROT-074
    Merchant Membership, Workforce Delegation & Operational Device Access Model
    including accepted v1.1 Personal Workforce Self-Service & Workforce Participation Amendment
    and accepted v1.2 Membership-Relative Personal Workforce Self-Service Authorisation Amendment

MS-PROT-075
    Notification Intent, Recipient, Channel & Delivery Evidence Model
    including accepted v1.1 Production Notification Provider Delivery, Attempt & Evidence Execution Contract Amendment
    and accepted v1.2 Notification Policy Portfolio Closure Amendment

MS-PROT-076
    Merchant Account Controller Transfer, Suspension & Closure Lifecycle Model

MS-PROT-077
    Order Commitment, Amendment & Lifecycle Model
    including accepted v1.1 Order Amendment, Release, Inventory Consequence & Customer Relationship Execution Contract Amendment
    and accepted v1.2 Ordering Commercial Access Classification Amendment

MS-PROT-078
    Adequate Capability Portfolio & AI Concierge Live-Testing Readiness Model

MS-PROT-079
    Remaining Backend Design Dependency Governance Model

MS-PROT-080
    v1.1 Workforce Compensation, Jurisdiction Pay Treatment, Payroll & Compensation Document Model
    completely superseding MS-PROT-080 v1.0 within the MS-PROT-080 authority scope
    plus accepted v1.2 Worked-Time, Break & Paid-Leave Compensation Amendment
    plus accepted v1.3 Workforce Compensation Affinity, Break Consequence & Self-Service Amendment
    and accepted v1.4 Workforce Compensation & Payroll Commercial Access Contracts Amendment

MS-PROT-081
    Workforce Scheduling, Timekeeping, Break & Leave Model
    plus accepted v1.1 Workforce Scheduling Arrangement & Evidence Affinity Amendment
    plus accepted v1.2 Cross-Arrangement Overlap & Merchant Scheduling-Buffer Amendment
    plus accepted v1.3 Workforce Scheduling, Leave Notification & Reminder Contract Portfolio Amendment
    and accepted v1.4 Workforce Scheduling, Timekeeping & Leave Commercial Access Contracts Amendment

MS-PROT-082
    Jurisdiction, Regulatory Knowledge & Regulatory Administration Model
    plus accepted v1.1 Initial Commercial Jurisdiction, Regulatory Purpose & Filing Scope Portfolio Amendment

MS-PROT-083
    Analytical Measurement, Business Health & Operational Decision Support Model
    plus accepted v1.1 Campaign Evidence, Measurement & Attribution Boundary Amendment
    plus accepted v1.2 Customer Return Behaviour, Retention Boundary & Re-engagement Analytical Handoff Amendment
    plus accepted v1.3 Initial General Business Intelligence Measure Portfolio Amendment
    plus accepted v1.4 Merchant Analytical Surface, Progressive Disclosure & Presentation Fidelity Amendment
    plus accepted v1.5 Initial Business Health Indicator Portfolio Amendment
    plus accepted v1.6 Initial Merchant Analytical Report Contract Amendment
    plus accepted v1.7 Business Intelligence Commercial Access Classification Amendment
    and accepted v1.8 Analytical Reports & Exports Commercial Access Classification Amendment

MS-PROT-084
    accepted v1.1 Financial Operations, Financial Evidence & Financial Health Complete Composition
    establishing the bounded residual Financial Operations owner, initial operating-cost/evidence/Financial Health portfolios,
    MS-PROT-092 financial evidence-consumption contract, anti-shadow-ledger/anti-accounting boundaries and the exact
    DQ-001..016 disposition recorded in Section 4.15A. The earlier standalone targeted amendment remains historical evidence.
    accepted v1.2 Financial Operations & Evidence Commercial Access Classification Amendment
    classifying protected establishment of new/materially expanded Financial Operations truth for BUSINESS + GROWTH and
    bounded preparation, existing-record observation and pure existing-record resolution without an independent entitlement;
    MS-PROT-084 deferred-decision states remain unchanged and MS-PROT-056-V17-DQ-001 remains open.

MS-PROT-085
    Merchant Attention, Work Handling and Governed Action Handoff Model
    plus accepted v1.1 Initial Enquiry Review Attention Contract
    plus accepted v1.2 Customer Communication Human Response Attention Contract Amendment
    plus accepted v1.3 Initial Enquiry Review Commercial Access Classification Amendment
    and accepted v1.4 Customer Communication Human Response Commercial Access Amendment
    classifying `customer-communication/human-response-required@1` as a supporting Attention path with no independent Commercial Entitlement while preserving exact Customer Communication access and actor gating; Customer Communication is not reclassified to FREE, no final entitlement identity is minted, and MS-PROT-056-V17-DQ-001 remains open.

MS-PROT-086
    Customer Messaging, Conversation Continuity and Customer-Service Handoff Model
    plus accepted v1.1 Initial Customer Messaging Channel Portfolio
    plus accepted v1.2 Conversation Browser Access, Resume & View Contract Amendment
    plus accepted v1.3 Initial Customer-Service Response Contract Portfolio Amendment
    and accepted v1.4 Customer Communication Commercial Access Classification Amendment
    classifying `customer-communication/message-participation-access@1` / `CONDUCT_CUSTOMER_COMMUNICATION` and `customer-communication/automated-response-access@1` / `AUTOMATE_ROUTINE_CUSTOMER_SERVICE` for BUSINESS + GROWTH, while bounded non-committing human preparation, existing-Conversation observation and committed-communication progression require no independent Commercial Entitlement; no final entitlement identity is minted, MS-PROT-056-V17-DQ-001 remains OPEN and implementation activation remains NONE.

MS-PROT-087
    Merchant Marketing Campaign, Audience Eligibility and Governed Outreach Model
    plus accepted v1.1 Initial Campaign Purpose & Outreach Portfolio Amendment
    plus accepted v1.2 Initial Audience Definition & Attribute Portfolio Amendment
    plus accepted v1.3 Direct Outreach Safety & Automated Campaign Execution Amendment
    and accepted v1.4 Merchant Marketing Commercial Access Classification Amendment
    classifying `marketing/campaign-service-access@1` / `CONDUCT_MARKETING_CAMPAIGNS` for GROWTH, while bounded non-committing preparation, existing Marketing-state observation and Campaign restriction require no independent Commercial Entitlement; no final entitlement identity is minted, MS-PROT-087 deferred-decision states remain unchanged, MS-PROT-056-V17-DQ-001 remains OPEN and implementation activation remains NONE.

MS-PROT-088
    Merchant Brand Namespace, Custom Domain & Business Email Identity Model
    accepted v1.0 establishing provider-neutral Merchant Brand Infrastructure, connect/register/optional-transfer domain paths,
    purpose-bounded domain-control evidence, independently activatable website/email bindings, merchant portability,
    branded sender identity without generic mailbox hosting, existing-infrastructure preservation and exception-driven administration.
    MS-PROT-088 v1.0 has no retained semantic DQ for its defined initial portfolio; implementation/vendor/configuration choices
    remain with their accepted owning authorities and implementation activation remains NONE.
    accepted v1.1 Website Namespace Use Commercial Access Contracts Amendment
    accepted v1.2 Website Binding Selection, Disconnection & Commercial Access Amendment
    accepted v1.3 Platform Website Namespace Allocation & Retirement Amendment
    accepted v1.4 Website Connection Request & Bounded Completion Amendment
    and accepted v1.5 Website DNS, Certificate Execution & Recovery Amendment

MS-PROT-089
    Capacity Waitlist & Availability Opportunity Coordination Model
    accepted v1.0 resolving the promoted Waitlist node from MS-PROT-042 v1.15 with exact Appointment/Booking target families,
    CustomerContext-bound durable Waitlist Entries, FIFO authoritative Queue Precedence, deterministic source-qualified admission,
    one active Promotion per exact target, source-owned protection/commitment authority, bounded customer response semantics,
    Notification and Merchant Attention integration, failure/uncertainty preservation and explicit anti-ERP/anti-overbooking boundaries.
    MS-PROT-089 v1.0 has no retained semantic DQ for its defined initial portfolio and implementation activation remains NONE.

MS-PROT-090
    External Review Solicitation, Reputation Observation & Merchant Response Coordination Model
    accepted v1.0 establishing bounded external-review coordination around source-owned customer-experience truth and provider-owned review truth;
    the initial portfolio admits exact Appointment/Booking Review Experience Contracts, neutral `ALL_ELIGIBLE_ONCE` EMAIL solicitation,
    one solicitation per CustomerContext × Review Destination, request-scoped provider review observation, sentiment-neutral Merchant Attention,
    and exact merchant approval/currentness for provider-bound public replies while rejecting review gating, incentives, native ratings,
    reputation scores, historical backfill, repeat reminders, automatic replies and reviewer-to-CustomerContext inference.
    MS-PROT-090 v1.0 has no retained semantic DQ for its defined initial portfolio and implementation activation remains NONE.

MS-PROT-093
    First-Party Merchant Client Architecture, Installed-Client Compatibility & Native Delivery Amendment
    accepted v1.0 establishing Contract-Driven First-Party Client Architecture across merchant-web and admitted Android, iOS,
    Windows and macOS native client classes; independently deployed installed-client compatibility; Client Contract Representation
    Versioning; deterministic Client Contract Bundle and first-party SDK generation; generated-artifact ownership classes; shared
    design language and cross-platform conformance; bounded compile-time declarative generation; purpose-built native UX; a native
    opaque server-authoritative session-transport profile; offline authority boundaries; and explicit rejection of a universal
    client UI runtime. MS-PROT-093 preserves merchant-web as first-class, leaves MS-PROT-036 storefront/customer architecture
    unchanged and does not itself promote native-client implementation sequencing.

MS-PROT-026 v1.1
    Production Domain Event Publication, Reaction & Consumption Contract Amendment

MS-PROT-027 v1.3
    Projection Freshness, Serviceability & Rebuild Contract Amendment

MS-PROT-027 v1.4
    Initial Production Projection Portfolio & Concrete Contract Registration Amendment

MS-PROT-027 v1.5
    Production Exposure Resolution & Initial Exposure Contract Portfolio Amendment

MS-PROT-027 v1.11
    Instance-Aware Exposure Membership & Owner-Filtered Deterministic Resolution Amendment

MS-PROT-027 v1.12
    Opaque Evaluator Submission Affinity Amendment

MS-PROT-027 v1.13
    Bounded Projection Read Material & Representation Affinity Amendment

MS-PROT-035 v1.1
    Production API Contract Registration, Transport Outcome & Initial Surface Portfolio Amendment

MS-PROT-040 v1.1
    Configuration Revision, Resolved Package & Atomic Activation Amendment

MS-PROT-040 v1.2
    Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment

MS-PROT-040 v1.3
    Configuration Validation, Impact Review & Approval Evidence Amendment

MS-PROT-040 v1.4
    Serving Deployment Admission Evidence & Generation Fence Amendment

MS-PROT-040 v1.5
    Release-Purpose Admission & Ordinary Release Reference Amendment

MS-PROT-040 v1.6
    Replacement & Reinstatement Configuration Approval Authority Amendment

MS-PROT-040 v1.7
    Non-Initial Configuration Activation Authority Amendment

MS-PROT-040 v1.8
    Configuration Reinstatement Decision Affinity Amendment

MS-PROT-050 v1.4
    Stable Business Hours Mutation, Revision & Currentness Amendment

MS-PROT-050 v1.5
    Dated Business Operating Override Mutation, Revision & Currentness Amendment

MS-PROT-050 v1.6
    Public Business Hours Commercial Access Contracts Amendment

MS-PROT-043 v1.4
    Production Enquiry Submission, Provenance & Merchant Observation Amendment

MS-PROT-046 v1.2
    Production Publication Revision, Exposure & Public Interaction Amendment

MS-PROT-048 v1.4
    Provider Connection, Operational Readiness & Runtime Fulfilment Admission Amendment

MS-PROT-048 v1.5
    Platform-Scoped Fulfilment Requirement, Routing & Serving-Affinity Contract Amendment

MS-PROT-048 v1.6
    Geographic Carrier Serviceability & Shipment Route Resolution Amendment

MS-PROT-049 v1.4
    Capability-Owned Public Interaction Participation Source & Generic Binding Projection Amendment

MS-PROT-051 v1.1
    Merchant Profile & Location Authoritative Mutation, Revision & Onboarding Handoff Amendment

MS-PROT-051 v1.5
    Merchant Location Public Exposure Choice Revision & Persistence Amendment

MS-PROT-052 v1.2
    Onboarding Case Lifecycle, Concurrency & Initial Configuration Intent Handoff Amendment

MS-PROT-057 v1.1
    Production AI Inference Contract, Provenance & Safety Boundary Amendment

MS-PROT-057 v1.2
    AI Assistance Responsibility, Merchant-Intent & Progressive Website Assistance Governance Amendment

MS-PROT-042 v1.4
    Booking Residual Obligation & Discharge Amendment

MS-PROT-042 v1.5
    Booking, Appointment & Scheduling Execution Contract Amendment

MS-PROT-042 v1.6
    Production Scheduling Evidence, Appointment Admission & Customer Surface Requirement Amendment

MS-PROT-042 v1.7
    Workforce Availability Constraint Interoperability Amendment

MS-PROT-042 v1.8
    Arrangement-Qualified Workforce Availability Interoperability Amendment

MS-PROT-042 v1.9
    Appointment Occurrence Outcome, Evidence & Correction Amendment

MS-PROT-042 v1.10
    TimeProposal Expiry & Appointment Proposal Capacity Protection Amendment

MS-PROT-042 v1.11
    Merchant-Initiated Reschedule Customer Decision & Appointment Check-In Evidence Amendment

MS-PROT-042 v1.12
    Appointment Support Requirement, Assignment Continuity & Resource Substitution Amendment

MS-PROT-042 v1.13
    Recurring & Shared Appointment Commitment Amendment

MS-PROT-053 v1.1
    Purpose-Bound Personal Media Use Amendment

MS-PROT-053 v1.2
    Production Data Lifecycle Evaluation, Disposition & Convergence Contract Amendment

MS-PROT-053 v1.3
    Enquiry and Customer Communication Retention Qualification & Period Amendment

MS-PROT-055 v1.1
    Payment Obligation, Provider Execution, Reconciliation & Refund Execution Contract Amendment

MS-PROT-055 v1.2
    Payment Commercial Access Classification Amendment

MS-PROT-058 v1.2
    Returned Stock Receipt, Disposition & Sellable Re-entry Contract Amendment

MS-PROT-058 v1.3
    Inventory Commercial Access Classification Amendment

MS-PROT-060 v1.1
    Production Order Fulfilment, Shipment Execution & Tracking Contract Amendment

MS-PROT-060 v1.2
    Order Fulfilment & Shipment Commercial Access Classification Amendment

MS-PROT-061 v1.1
    Optional Merchant Returns Policy Capability & Production Return-Action Execution Contract Amendment

MS-PROT-061 v1.2
    Returns Commercial Access Classification Amendment

MS-PROT-063 v1.2
    Personal Workforce Self-Service Trusted Context Amendment

MS-PROT-065 v1.1
    Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment

MS-PROT-068 v1.1
    Production Operational Evidence, Health & Alerting Contract Amendment

MS-PROT-069 v1.1
    Production Reconciliation Responsibility, Evidence & Resolution Contract Amendment

MS-PROT-074 v1.1
    Personal Workforce Self-Service & Workforce Participation Amendment

MS-PROT-074 v1.2
    Membership-Relative Personal Workforce Self-Service Authorisation Amendment

MS-PROT-075 v1.1
    Production Notification Provider Delivery, Attempt & Evidence Execution Contract Amendment

MS-PROT-075 v1.2
    Notification Policy Portfolio Closure Amendment

MS-PROT-077 v1.2
    Ordering Commercial Access Classification Amendment

MS-PROT-080 v1.2
    Worked-Time, Break & Paid-Leave Compensation Amendment

MS-PROT-080 v1.3
    Workforce Compensation Affinity, Break Consequence & Self-Service Amendment

MS-PROT-080 v1.4
    Workforce Compensation & Payroll Commercial Access Contracts Amendment

MS-PROT-081 v1.1
    Workforce Scheduling Arrangement & Evidence Affinity Amendment

MS-PROT-081 v1.2
    Cross-Arrangement Overlap & Merchant Scheduling-Buffer Amendment

MS-PROT-081 v1.3
    Workforce Scheduling, Leave Notification & Reminder Contract Portfolio Amendment

MS-PROT-081 v1.4
    Workforce Scheduling, Timekeeping & Leave Commercial Access Contracts Amendment

MS-PROT-091 v1.1
    Workforce Rota Commercial Access Contracts Amendment

MS-PROT-082 v1.1
    Initial Commercial Jurisdiction, Regulatory Purpose & Filing Scope Portfolio Amendment

MS-PROT-083 v1.1
    Campaign Evidence, Measurement & Attribution Boundary Amendment

MS-PROT-083 v1.2
    Customer Return Behaviour, Retention Boundary & Re-engagement Analytical Handoff Amendment

MS-PROT-083 v1.3
    Initial General Business Intelligence Measure Portfolio Amendment

MS-PROT-083 v1.4
    Merchant Analytical Surface, Progressive Disclosure & Presentation Fidelity Amendment

MS-PROT-083 v1.5
    Initial Business Health Indicator Portfolio Amendment

MS-PROT-083 v1.6
    Initial Merchant Analytical Report Contract Amendment

MS-PROT-083 v1.7
    Business Intelligence Commercial Access Classification Amendment

MS-PROT-083 v1.8
    Analytical Reports & Exports Commercial Access Classification Amendment

MS-PROT-084 v1.1
    Financial Operations, Financial Evidence & Financial Health Complete Composition

MS-PROT-084 v1.2
    Financial Operations & Evidence Commercial Access Classification Amendment

MS-PROT-056 v1.4
    Initial Full-Experience Trial Establishment Amendment

MS-PROT-056 v1.5
    Merchant Commercial Agreement, Entitlement Binding & Residual Access Amendment

MS-PROT-056 v1.6
    Standing Free Baseline Temporal Anchor Amendment

MS-PROT-056 v1.7
    Standard Tier Allocation & Future Commercial Portfolio Amendment

MS-PROT-056 v1.8
    Analytical Portfolio Allocation & Presentation Access Amendment

MS-PROT-056 v1.9
    Commercial Catalogue Binding, Publication & Historical Resolution Amendment

MS-PROT-066 v1.2
    Media Delivery Fidelity & Long-Form Information Amendment
```

Accepted implementation/production-architecture authority added after the original DDR scope:

```text
ADR-009
    Capability-Owned Residual Authority Composition

ADR-010
    Semantic Release Assembly

ADR-011
    Semantic Release Publication, Retention & Bootstrap

ADR-012
    Runtime Semantic Execution & Deployment Compatibility

ADR-013
    Packaged Semantic Definition Materialisation & Production Bootstrap

ADR-014
    High-Assurance Authentication, Session & Trusted Browser Execution Architecture

ADR-015
    WebAuthn / Passkey Production Adapter

ADR-016
    Exposure Definition Packaging & Semantic Release Materialisation

MS-TAS-RECOVERY-001
    Production Backup, Restore, Corruption Recovery & Disaster Recovery Architecture
    including accepted v1.1 Exact Release, Durable Reaction & Post-Restore Authority Reconciliation Amendment
```

Current authority navigation is governed by `AUTHORITY-INDEX.md` and the accepted documents themselves. TAS/ADR architecture remains subordinate to accepted MS-PROT semantic/design authority within overlapping scope.

---

## 4. Retained Future / Deferred Scope

A resolved parent decision may intentionally leave narrower future scope. Such future scope is not automatically an active design task.

Current retained examples include:

```text
MS-PROT-053 through v1.3
    jurisdiction-specific lawful-basis/legal-policy mapping and statutory retention durations outside the concrete Enquiry/Customer Communication baseline resolved by v1.3
    exact guardian/representative evidence mechanisms where a future capability requires them
    subject-access/privacy administration UI
    future private parent/child or other restricted-media relationship semantics
    formal legal controller/processor classification
    marketing-law and cookie-consent implementation
    data portability/export API
    exact deletion SQL and physical purge strategy
    exact anonymisation / cryptographic-erasure mechanisms
    provider-specific deletion API/SDK and data-residency selection
    international-transfer legal mechanisms
    exact background scheduler/worker and retry/backoff — implementation under composite MS-PROT-065
    operational privacy/reconciliation tooling — semantics governed by composite MS-PROT-068/MS-PROT-069; exact tooling remains implementation/operator scope
    production privacy/API DTOs — API implementation under composite MS-PROT-035

MS-PROT-055 v1.1
    exact PayPal/Klarna/Stripe/etc. provider selection
    provider SDK/library
    hosted-checkout/redirect UI
    exact API DTOs/routes — API implementation under composite MS-PROT-035
    payment retry presentation UX — presentation implementation constrained by composite MS-PROT-035
    specific customer receipt presentation
    chargebacks/disputes
    credit ledger
    generic overpayment-credit model
    FX
    tax engines
    discount/coupon/promotion engines
    invoice/accounting/general ledger
    bank/provider payout reconciliation
    revenue recognition
    generic provider/background implementation — implementation under composite MS-PROT-065; reconciliation semantics governed by composite MS-PROT-069
    exact PostgreSQL layout/indexes/locking
    exact secure guest credential representation — tracked by ADR-014-DQ-011

MS-PROT-056
    multi-location / medium-scale pricing details
    scale thresholds
    commercial usage/capacity metering
    negotiated large-merchant terms
    cross-Merchant-Account repeat-trial eligibility / abuse prevention
    exact payment grace / delinquency policy
    capability-specific classification of ambiguous residual operations

MS-PROT-057 v1.2
    number of deployed agent processes
    number of model deployments
    exact Luna/Sol mappings
    future model names/providers
    token and monetary ceilings
    exact suggestion cooldown duration
    exact website-gap scoring algorithm
    exact suggestion presentation UX
    exact dismissal storage representation
    exact contextual-completeness algorithm
    initial Business Intelligence measure catalogue — RESOLVED by composite MS-PROT-083 through v1.3; future materially different measures require fresh Feature Admission
    Customer-side AI architecture
    Provider assistance architecture
    Payment assistance architecture
    Fulfilment assistance architecture
    Returns assistance architecture
    full Image Amplification transformation catalogue
    concrete Java implementation

MS-PROT-058 through v1.2
    automatic claim-expiry timing
    backorders / overselling policy
    batch / lot selection
    serialised-unit architecture
    in-transit stock
    advanced warehouse management
    provider-specific stock integrations
    serialised returned-unit identity
    lot/batch returns
    repair/reconditioning workflow
    quarantine workflow
    return-to-vendor
    supplier returns
    warehouse reverse logistics
    customer-facing returned-receipt projection
    automated inspection
    condition-scoring AI
    exact Inventory API — API implementation under composite MS-PROT-035
    exact SQL / jOOQ schema and locking

MS-PROT-060 v1.1
    customer self-service return processing
    live carrier-rate quoting
    package-level logistics
    warehouse management
    route optimisation
    advanced multi-Order Shipment consolidation
    delivery-slot optimisation
    customs / import management
    carrier-specific workflow vocabularies
    exact carrier provider / SDK
    exact provider retry / backoff
    exact Shipment preparation adapter shapes
    exact SQL / jOOQ representation
    exact REST / API representations — API implementation under composite MS-PROT-035
    tracking UI wording / layout — presentation implementation constrained by composite MS-PROT-035
    generic background-work machinery — implementation under composite MS-PROT-065
    generic reconciliation/operator tooling — semantics governed by composite MS-PROT-069; exact tooling remains implementation/operator scope
    exact data-retention periods — governed by composite MS-PROT-053; concrete period remains policy/legal implementation

MS-PROT-061 v1.1
    customer self-service return workflow
    automatic return adjudication
    automatic Refund from policy
    advanced commercial exchange
    store credit
    repair/reconditioning workflow
    dedicated reverse-logistics aggregate
    package-level returns
    marketplace disputes
    exact courier adapter
    exact APIs — API implementation under composite MS-PROT-035
    return UI — presentation implementation constrained by composite MS-PROT-035
    generic background machinery — implementation under composite MS-PROT-065
    generic operator/reconciliation tooling — semantics governed by composite MS-PROT-069; exact tooling remains implementation/operator scope

MS-PROT-066
    exact photographic codecs/quality factors/responsive dimensions
    exact long-form INFORMATION_VIDEO duration/upload-size limits
    adaptive-streaming/CDN/transcoder implementation
    any future explicitly authorised media-editing capability

MS-PROT-073
    commercial/contractual differentiated service guarantees remain outside Platform Resource Protection
    cross-account commercial trial eligibility remains outside Platform Resource Protection
    permanent account/security consequences remain owned by their applicable authorities

MS-PROT-074 through v1.2
    exact staff PIN policy and credential implementation
    exact staff personal-authenticator implementation where relevant
    default merchant Role templates
    nested Groups / explicit DENY precedence
    delegated operational-device enrolment by non-controllers — tracked by ADR-014-DQ-012
    durable offline staff operational-data architecture — tracked by ADR-014-DQ-013 and constrained by MS-PROT-093 where first-party native client transport/offline behaviour participates
    general HR semantics outside the accepted Workforce Scheduling/Compensation boundaries
    cross-merchant workforce administration

MS-PROT-080 through v1.4
    MS-PROT-080-V11-DQ-001 first Workforce Compensation jurisdiction rollout
    MS-PROT-080-V11-DQ-002 exact jurisdiction treatment-resolution mechanism
    MS-PROT-080-V11-DQ-003 exact jurisdiction-specific fact/evidence schema
    MS-PROT-080-V11-DQ-004 exact Java/persistence representation
    MS-PROT-080-V11-DQ-006 exact project/work-progress owner and internal contract
    MS-PROT-080-V11-DQ-007 exact calculation engines/libraries/providers
    MS-PROT-080-V11-DQ-008 exact regulatory APIs, credentials and certification
    MS-PROT-080-V11-DQ-009 exact payment/remittance rails
    MS-PROT-080-V11-DQ-010 exact correction/off-cycle rules per jurisdiction
    MS-PROT-080-V11-DQ-011 exact cross-border/treaty/social-security treatment
    MS-PROT-080-V11-DQ-012 exact jurisdiction retention/privacy policies
    MS-PROT-080-V11-DQ-013 exact generic Payee Compensation Self-Service authentication, API/Surface/Audience representation and legal-entity representative-access mechanism
    MS-PROT-080-V11-DQ-014 exact merchant/Payee onboarding wording
    MS-PROT-080-V11-DQ-015 exact generic professional/licensing capability
    MS-PROT-080-V11-DQ-016 RESOLVED by v1.4 — commercial packaging and entitlement
    MS-PROT-080-V11-DQ-017 RESOLVED by MS-PROT-082 v1.0 — jurisdiction support is purpose-specific, partial territorial support is valid, and whole-country support MUST NOT be inferred from one supported capability/purpose
    MS-PROT-080-V11-DQ-018 exact digital-signature/e-signature technology and assurance requirements per jurisdiction
    MS-PROT-080-V11-DQ-019 exact document rendering/storage technology
    MS-PROT-080-V11-DQ-020 exact jurisdiction document templates and legally required clauses/content

MS-PROT-081 through v1.4
    MS-PROT-081-DQ-001 exact Java/persistence representation
    MS-PROT-081-DQ-002 exact shift-swap / shift-cover workflow
    MS-PROT-081-DQ-003 exact overtime authority and jurisdiction rules
    MS-PROT-081-DQ-004 exact minimum/maximum working-time compliance rules by jurisdiction
    MS-PROT-081-DQ-005 exact break-compliance rules by jurisdiction
    MS-PROT-081-DQ-006 exact leave category/entitlement formulas by jurisdiction
    MS-PROT-081-DQ-007 exact holiday-pay calculation rules — owned with MS-PROT-080 jurisdiction implementation
    MS-PROT-081-DQ-008 exact GPS/QR/NFC/shared-terminal attendance mechanisms
    MS-PROT-081-DQ-009 exact anti-time-theft/fraud evidence policy
    MS-PROT-081-DQ-010 exact time-rounding rules
    MS-PROT-081-DQ-011 exact manager time-approval policy and optional self-attestation rules
    MS-PROT-081-DQ-012 exact unscheduled-work handling
    MS-PROT-081-DQ-013 exact staff personal-surface API/UI
    MS-PROT-081-DQ-014 exact leave-balance projection representation
    MS-PROT-081-DQ-015 PARTIALLY RESOLVED by v1.3 — Scheduling/Leave/Scheduled Work reminder contract portfolio resolved; exact Timekeeping notification/reminder semantics remain deferred pending DQ-009 through DQ-012
    MS-PROT-081-DQ-016 exact linkage representation between Workforce Scheduling Arrangement and customer-facing Resource
    MS-PROT-081-DQ-017 exact remediation workflow when leave/workforce changes conflict with committed Appointments
    MS-PROT-081-DQ-018 exact legal/commercial treatment of shift cancellation after acceptance
    MS-PROT-081-DQ-019 exact native-mobile/offline workforce support — generic first-party native client and offline authority boundaries are now governed by MS-PROT-093; workforce-specific support remains deferred
    MS-PROT-081-DQ-020 RESOLVED by v1.4 — commercial entitlement/tier packaging
    MS-PROT-081-DQ-021 RESOLVED by v1.2 — exact symmetric cross-Arrangement overlap policy, one-off override and optional merchant-owned MinimumInterCommitmentBuffer
    MS-PROT-081-DQ-022 exact retrospective time/compensation attribution mechanism when historical workforce evidence had no Compensation Relationship affinity

MS-PROT-082 through v1.1
    MS-PROT-082-DQ-001 RESOLVED by v1.1 — initial CommercialJurisdiction portfolio exactly England + Wales
    MS-PROT-082-DQ-002 RESOLVED by v1.1 — initial Regulatory Purpose and FilingScopeReference subtype portfolios
    MS-PROT-082-DQ-003 exact physical representation of Regulatory Rule Set Releases/evaluator bindings
    MS-PROT-082-DQ-004 initial regulatory-provider portfolio
    MS-PROT-082-DQ-005 exact regulatory-source monitoring automation
    MS-PROT-082-DQ-006 professional escalation network/integration model
    MS-PROT-082-DQ-007 RESOLVED COMPOSITIONALLY by composite MS-PROT-083 + MS-PROT-084 v1.1

MS-PROT-083 through v1.8
    MS-PROT-083-DQ-001 initial Analytical Measure portfolio — RESOLVED by v1.1 campaign slice + v1.2 customer-return slice + v1.3 initial general Business Intelligence portfolio including Commitment Interaction Origin; future materially different measures require fresh Feature Admission
    MS-PROT-083-DQ-002 initial Business Health Indicator portfolio — RESOLVED by v1.5 for the initial portfolio
    MS-PROT-083-DQ-003 analytical persistence/time-series architecture
    MS-PROT-083-DQ-004 initial Analytical Method portfolio
    MS-PROT-083-DQ-005 method-qualification operational process
    MS-PROT-083-DQ-006 recommendation prioritisation
    MS-PROT-083-DQ-007 external context portfolio
    MS-PROT-083-DQ-008 Merchant Attention integration — RESOLVED by MS-PROT-085 v1.0
    MS-PROT-083-DQ-009 merchant analytical surface — RESOLVED by v1.4
    MS-PROT-083-DQ-010 reports and exports — RESOLVED for the initial on-demand CSV/PDF portfolio by v1.6
    MS-PROT-083-DQ-011 analytical retention/lifecycle portfolio
    MS-PROT-083-DQ-012 adaptive learning
    MS-PROT-083-DQ-013 cross-merchant benchmarking
    MS-PROT-083-DQ-014 currency normalisation
    MS-PROT-083-DQ-015 Financial Health composition — RESOLVED by MS-PROT-084 v1.1
    v1.7 adds Business Intelligence analytical-evaluation and presentation commercial classifications only
    v1.8 adds the two v1.6 analytical-delivery access classifications with no independent Commercial Entitlement
    neither v1.7 nor v1.8 reopens or resolves any MS-PROT-083-DQ-* row

MS-PROT-084 through v1.2
    RESOLVED: DQ-001 Initial Operating Cost Portfolio
    RESOLVED: DQ-002 Initial Financial Health Portfolio
    DEFERRED — INACTIVE: DQ-003 Financial Account Provider Portfolio
    RESOLVED: DQ-004 Financial Evidence Classification Portfolio
    DEFERRED — INACTIVE: DQ-005 Counterparty / Supplier Authority
    DEFERRED — INACTIVE: DQ-006 Invoice Authority
    DEFERRED — INACTIVE: DQ-007 Outgoing Merchant Payment Execution
    DEFERRED — INACTIVE: DQ-008 Inventory Financial Valuation
    DEFERRED — INACTIVE: DQ-009 Capital Asset / Depreciation
    DEFERRED — INACTIVE: DQ-010 Profitability Portfolio
    DEFERRED — INACTIVE: DQ-011 Currency Normalisation
    DEFERRED — INACTIVE: DQ-012 Professional Accounting Integration
    RESOLVED: DQ-013 Financial Document Extraction
    RESOLVED BY OWNERSHIP: DQ-014 Financial Evidence Retention
    RESOLVED: DQ-015 Statutory Accounting Boundary
    RESOLVED — INITIAL PORTFOLIO EMPTY: DQ-016 Financing Calculation Contracts
    v1.2 adds Financial Operations commercial-access classification only and does not reopen or resolve any MS-PROT-084-DQ-* row

MS-PROT-085 through v1.4
    MS-PROT-085-DQ-001 initial Attention Contract portfolio — RESOLVED by v1.1; exactly enquiry / initial-submission-review@1 selected
    v1.2 adds exactly customer-communication / human-response-required@1 as the accepted Customer Communication human-response handling family; merchant activation and implementation remain separately governed
    v1.3 adds the initial-Enquiry-review commercial classification without reopening DQ-001
    v1.4 adds the Customer Communication human-response supporting commercial classification with no independent Attention entitlement, preserves exact Customer Communication access/actor requirements, and does not reopen DQ-001

MS-PROT-086 through v1.4
    MS-PROT-086-DQ-001 initial Customer Messaging Channel portfolio — RESOLVED by v1.1; exactly Merchant Website Messaging + Conversation-Bound Email selected through `WEBSITE_MESSAGE_CREATE_V1` and `EMAIL_REPLY_CONTINUE_V1`; merchant activation and implementation remain separately governed
    MS-PROT-086-DQ-002 initial Customer-Service Response Contract portfolio — RESOLVED by v1.3; exactly seven fact-first response families selected with complete request coverage, owner-qualified evidence/access, deterministic validation and the paired MS-PROT-085 v1.2 human-response Attention path; AI confidence and generic FAQ/RAG material are not authority
    MS-PROT-086-DQ-003 production Guest Conversation Access mechanism — RESOLVED by v1.2; registered-customer browser discovery/view/continuation is participant-authority based, guest browser access uses an exact purpose-bound one-Conversation Grant, email/contact equality is not authority, and physical guest credential representation remains separately gated by ADR-014-DQ-011
    MS-PROT-086-DQ-004 initial Conversation Attachment portfolio — ACTIVE BEFORE PRODUCTION MESSAGE ATTACHMENTS
    v1.4 adds Customer Communication commercial classification only and changes no MS-PROT-086-DQ-* state

MS-PROT-087 through v1.4
    MS-PROT-087-DQ-001 initial Campaign Purpose and Outreach portfolio — RESOLVED by v1.1; exactly `marketing/merchant-news-awareness@1`, `marketing/offering-awareness@1`, `marketing/customer-appreciation@1` and `marketing/customer-reengagement@1` selected with `WEBSITE_ANNOUNCEMENT_V1` and `DIRECT_EMAIL_MARKETING_V1`; standalone website Announcements remain Publication-owned; initial direct marketing is relationship-based and implementation/merchant activation remain separately governed
    MS-PROT-087-DQ-002 initial Audience Definition and Attribute portfolio — RESOLVED by v1.2; exactly `marketing-audience/existing-customer@1`, `marketing-audience/recent-customer-relationship@1` and `marketing-audience/previous-customer-reengagement@1` selected; recency uses CustomerContext-associated Order, Booking and Appointment commitment evidence only, insufficient negative-evidence coverage yields `UNRESOLVED`, and audience membership does not grant marketing permission
    MS-PROT-087-DQ-003 Marketing Permission, Suppression and Contact Policy portfolio — RESOLVED by v1.3 / MKT-GRP-01; jurisdiction-qualified direct-email permission, durable suppression, immediate unsubscribe, contact-pressure and pre-externalisation revalidation semantics accepted
    MS-PROT-087-DQ-004 Automated Campaign Trigger and Recurrence portfolio — RESOLVED by v1.3 / MKT-GRP-01; exact-revision-affined bounded standing authority, scheduled-single and periodic-audience triggers, weekly/monthly local recurrence, no catch-up/replay and per-occurrence revalidation accepted
    MS-PROT-087-DQ-005 Campaign Measure and Attribution portfolio — RESOLVED by MS-PROT-083 v1.1 / MKT-GRP-02; five direct-execution Campaign measure families and `DIRECT_EXECUTION_TRACE_V1` accepted, with no commercial causation, conversion, revenue, retention or ROI claims
    MS-PROT-087-DQ-006 Paid Advertising, Spend and External Optimisation authority — ACTIVE BEFORE PAID ADVERTISING OR EXTERNAL CAMPAIGN SPEND
    v1.4 adds Marketing commercial-access classification only: `marketing/campaign-service-access@1` / `CONDUCT_MARKETING_CAMPAIGNS` is protected for GROWTH while bounded non-committing preparation, existing Marketing-state observation and Campaign restriction require no independent Commercial Entitlement; it changes no MS-PROT-087-DQ-* state and leaves the final MS-PROT-056-V17-DQ-001 catalogue manifest open

MS-PROT-075 through v1.2
    NO RETAINED MS-PROT-075 NOTIFICATION DECISIONS REMAIN
    v1.2 closes the former provider-selection/status-mapping, retry/backoff, rendering, Notification Preference,
    marketing-contact-policy ownership, reminder-timing ownership, batching/digest, quiet-hours and open/click-tracking tail.
    Specific provider vendors, SDK/client choice, webhook framework, worker/queue/broker technology, API DTO/routes,
    operator/dashboard presentation, sender-domain/DKIM/SPF mechanics and rendering-library selection are explicitly
    release/configuration or implementation choices under their owning accepted authorities rather than semantic DQs.
    Retention remains governed by composite MS-PROT-053; jurisdiction-specific regulatory/contact permission remains
    owned by Data Protection/regulatory authority and, for Marketing direct outreach, is separately gated by
    `MS-PROT-087-DQ-003`; marketing campaign architecture remains MS-PROT-087-owned. New materially different
    Notification capabilities require normal feature admission rather than reopening this closed retained list.

MS-PROT-076
    future co-controller / quorum-governance model if demonstrated by merchant scale
    exact exceptional Merchant Control Recovery evidence and provider/mechanism — tracked by ADR-014-DQ-004 where authentication/recovery proof participates
    detailed Merchant Account Suspension reason/policy catalogue
    exact catalogue of post-closure residual/remediation operations
    future terminal-account reopening/migration semantics if ever required

MS-PROT-077 v1.1
    advanced Order amendment policy catalogues
    exchanges/substitutions beyond accepted boundary
    shopping basket/cart durable lifecycle if independent authority is demonstrated
    advanced tax/legal invoice semantics
    offline authoritative Ordering
    exact secure guest contextual-access credential representation — tracked by ADR-014-DQ-011
    capability-specific Payment policy remains with the applicable commitment authority while Payment execution is governed by composite MS-PROT-055 through v1.1
    generic background/event implementation — implementation under composite MS-PROT-026/MS-PROT-065
    reconciliation/observability semantics — governed by composite MS-PROT-068/MS-PROT-069; exact tooling remains implementation scope
    exact API representations — API implementation under composite MS-PROT-035

MS-PROT-027 v1.3
    concrete cache/index technology
    exact TTLs where a specific Projection Contract legitimately uses time
    universal observability dashboards for projection lag
    one transport representation for stale/degraded reads — API implementation under composite MS-PROT-035
    generic projection framework/DSL
    automated projection-repair tooling

MS-PROT-027 v1.4
    exact source checkpoint/revision representation
    physical Merchant Presence materialisation only if justified
    storefront CDN/static-generation contract only if introduced
    Calendar materialisation/cache strategy only if justified
    projection rebuild/update worker details — implementation under composite MS-PROT-026/MS-PROT-065
    transport details governed by composite MS-PROT-035 where externally represented; observability details governed by composite MS-PROT-068
    dedicated cache/search-index technology only when demonstrated
    projection-policy evaluator semantic ownership and change-amplification under capability growth — implementation-architecture review under ADR-010 and IMPLEMENTATION-RULES before a third projection-owning capability (or equivalent expansion) makes the distinction material; release assembly may be re-examined to ensure it remains mechanical composition of owner-supplied evaluators rather than a multi-owner policy implementation home

MS-PROT-027 through v1.13
    exact API/application operations for changing Profile Exposure choices — API implementation under composite MS-PROT-035
    CUSTOMER Exposure requirement evaluators for any later capability scopes not already resolved by their owners
    capability-owned MERCHANT field-observation privilege/context mappings
    exact API distinction among withheld, absent, not-serviceable and unavailable — API implementation under composite MS-PROT-035
    Exposure-resolution telemetry/diagnostics — implementation under composite MS-PROT-068
    any Exposure-result cache only if measured need justifies it
    CDN/static-storefront Exposure-revocation propagation if introduced
    search/index convergence when exposed elements become withheld
    distributed evaluator submission-affinity representation/protocol only if owner evaluators later cross a process/service boundary
    concrete owner/query adapter, SQL/jOOQ and route/DTO mechanics for bounded Projection reads remain implementation detail under IMPLEMENTATION-RULES and composite MS-PROT-035
    stronger final-byte/linearizable revocation or cross-capability snapshot semantics require a separate governed design decision if a concrete future requirement demonstrates need
    finer owner progress-token scope may be introduced when conservative false-negative withholding becomes operationally material, without weakening generic same-read affinity
    MS-PROT-051 v1.5 governs the exact Merchant Location public-choice persistence model

IMP-05-R2-DG-001

Resolved by MS-PROT-040 v1.6.

Canonical outcome:

ordinary non-initial Configuration approval
    -> current active Merchant Controller
    -> exact successful validation evidence
    -> exact completed impact-review evidence
    -> exact Resolved Configuration Package
    -> exact approving principal
    -> exact Controller Relationship

Historical approval is append-only.

Historical approval does not remain currently applicable after the exact Controller Relationship stops being current.

The same human Identity later becoming Controller through another Controller Relationship does not revive the earlier approval.

No configuration.approve Workforce privilege is created.

IMP-05-R3-DG-001

Resolved by MS-PROT-040 v1.7.

Canonical outcome:

ordinary non-initial Configuration activation
    -> trusted authenticated execution context
    -> Merchant Account OPEN
    -> no effective account-wide Suspension
    -> current ACTIVE Merchant Controller
    -> exact current approval
    -> existing concurrency / compatibility / serving-admission predicates

A caller-supplied initiatingPrincipalIdentifier is not actor authority.

A staff role is not actor authority.

Historical approval is not current activation authority.

Reinstatement remains a fresh current decision.

No configuration.activate Workforce privilege is created.

MS-PROT-043 v1.4
    exact Enquiry persistence representation
    exact logical idempotency-key representation
    exact subject-provenance encoding
    exact Enquiry API/form transport representation — API implementation under composite MS-PROT-035
    exact Merchant Attention persistence/model beyond minimal projection
    exact durable Conversation creation policy/channel mapping
    Enquiry/attention telemetry — implementation under composite MS-PROT-068

MS-PROT-046 v1.2
    exact Java representation of Publication identities/revisions
    exact PostgreSQL layout/indexes
    exact transport contracts for create/revise/publish/withdraw/republish — API implementation under composite MS-PROT-035
    Search/index architecture
    CDN/static-generation Publication-revocation convergence
    social-distribution worker/provider implementation — implementation under composite MS-PROT-026/MS-PROT-065 plus provider authority
    Subscription public transport and delivery preference UX — API/presentation implementation under composite MS-PROT-035
    exact retained historical Publication revision period — governed by composite MS-PROT-053; concrete period remains policy/legal implementation

MS-PROT-048 through v1.5
    exact ProviderConnection Java and persistence representation
    exact persisted ProviderConnection lifecycle vocabulary
    exact Provider Readiness resolver/result representation
    provider-contract declaration of readiness evidence requirements
    evidence freshness/expiry rules per provider
    provider-specific OAuth/API-key/etc. connection mechanics
    connection reconciliation/background validation mechanism — implementation under composite MS-PROT-065; reconciliation semantics governed by composite MS-PROT-069
    Provider Readiness telemetry and operational dashboards — implementation under composite MS-PROT-068
    public/API representation of connection/readiness failures — API implementation under composite MS-PROT-035
    exact ProviderConnection storage/index strategy
    any readiness cache only if measured need justifies it
    provider-wide status ingestion mechanism
    Provider & Integration AI Assistant concrete contracts
    capability-specific degraded-operation rules outside the Scheduling, Payment, Shipment, Returns return-label and Notification scopes already resolved by their owning authorities
    hot-swappable platform provider routing without serving promotion
    weighted provider traffic splitting
    active-active provider routing
    automatic provider cost optimisation
    automatic cross-provider failover policy
    provider marketplace
    exact PlatformFulfilmentBindingSetRevision persistence format
    exact deployment-manifest encoding for platform routing affinity
    exact platform fulfilment administrative API/UI — API/presentation implementation under composite MS-PROT-035

MS-PROT-068 through v1.1
    OpenTelemetry or other SDK selection
    metrics/log/trace backend selection
    alerting/paging product
    exact metric names, histogram buckets and thresholds
    sampling strategy
    SLO/SLI catalogue
    dashboard layout
    log format
    exact health endpoints
    telemetry persistence and retention durations
    anomaly detection
    AI operational assistant
    exact operator UI
    merchant/customer status APIs — API implementation under composite MS-PROT-035 where externally represented

MS-PROT-069 through v1.1
    provider-specific reconciliation APIs and status mappings — provider/API implementation under composite MS-PROT-035/MS-PROT-069
    exact reconciliation persistence/index representation
    exact scheduler cadence/backoff timing
    numeric escalation thresholds
    bulk reconciliation scans
    operator dashboard
    operator API — API implementation under composite MS-PROT-035
    merchant/customer pending-status API — API implementation under composite MS-PROT-035
    reconciliation reporting
    incident-management integration
    automated anomaly detection
    AI operational assistance
    exact audit presentation
    SQL/indexing/partitioning

MS-PROT-035 through v1.1, as amended by MS-PROT-093 within first-party installed-client scope
    exact REST URI naming
    exact HTTP status mapping
    exact JSON property naming
    Jackson configuration
    exact error-message wording/localisation
    exact request/correlation header names
    exact idempotency header name and persistence representation
    exact optimistic-concurrency transport encoding such as ETag versus body revision
    exact pagination-token encoding and cursor/offset mechanics where semantics remain preserved
    numeric page-size defaults/maximums
    API gateway/BFF product or framework
    concrete CORS and ADR-014-conformant CSRF/cookie implementation
    SSE versus WebSocket where live projections are introduced
    exact media upload/download protocol and signed-reference mechanism
    exact external URI-version syntax
    provider-specific webhook routes and payload mapping
    GraphQL introduction if later justified
    exact controller/package layout
    API documentation portal
    exact OpenAPI/JSON Schema/Client Contract Bundle encoding and code-generation tooling — implementation scope under MS-PROT-093
    first-party merchant-client SDK generation semantics and generated-artifact ownership — RESOLVED by MS-PROT-093 v1.0
    any future independently published third-party client SDK product/compatibility policy requires its own applicable product/implementation decision rather than inheriting first-party client assumptions

ADR-011
    broader build/promotion tooling outside ADR-013's accepted production materialisation contract
    safe automatic release retirement — tracked by ADR-013-DQ-012
    dedicated semantic-registry service if future scale/topology justifies it — tracked by ADR-013-DQ-009

ADR-012
    compatibility-aware request routing technology — tracked by ADR-013-DQ-010 where the production materialisation topology requires it
    dedicated historical execution pools or dynamic old binaries if ever justified

MS-TAS-RECOVERY-001 through v1.1
    concrete cloud provider and managed PostgreSQL product
    concrete WAL/archive product
    concrete object-storage / secret/KMS / backup products
    exact backup encryption mechanism
    exact RecoveryManifest encoding
    exact RecoveryReleaseBundle archive format
    exact semantic bundle archive format
    exact digest/signature algorithm
    infrastructure-as-code and monitoring/alerting product selection
    exact DR orchestration scripts
    exact restore-validation implementation
    exact SQL integrity queries
    exact Event Reaction persistence schema
    exact DurableWork persistence schema
    exact provider reconciliation clients
    exact recovery operator UI/API
    exact health/dashboard/alert implementation
    exact immutable recovery-log technology if required to preserve post-target non-resurrectable evidence across PITR
    exact runbook wording
    future customer-facing SLA/SLO adoption or revised recovery objectives
    any future active-active / distributed-authority regional architecture
```

### 4.1 ADR-013 Deferred Question Catalogue

ADR-013 is Design-Closed while retaining the following narrower questions. Each identifier is stable and mirrored in ADR-013 itself.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| ADR-013-DQ-001 | **DEFERRED — INACTIVE** | Exact bundle encoding / serialization representation | Implementation detail — production bootstrap implementation | Before the first production bundle codec is selected or interoperability makes the representation architecturally material |
| ADR-013-DQ-002 | **DEFERRED — INACTIVE** | Exact cryptographic digest algorithm | Security/build implementation detail unless policy makes it architectural | Before production integrity metadata is finalised, or earlier if security/compliance policy constrains the algorithm |
| ADR-013-DQ-003 | **DEFERRED — INACTIVE** | Archive compression | Implementation/operations detail | When deployment artefact size, startup latency or distribution cost makes compression material |
| ADR-013-DQ-004 | **DEFERRED — INACTIVE** | Exact filesystem/classpath/resource layout | Runtime packaging implementation detail | When the concrete bundle-loading implementation is selected |
| ADR-013-DQ-005 | **DEFERRED — INACTIVE** | Build-plugin implementation | Build engineering implementation detail | When automated deterministic bundle construction is implemented |
| ADR-013-DQ-006 | **DEFERRED — INACTIVE** | CI publication/promotion workflow | Release engineering implementation detail | Before production release automation must publish/promote semantic bundles |
| ADR-013-DQ-007 | **DEFERRED — INACTIVE** | Deployment artefact signing / KMS-backed authenticity mechanism | Security architecture candidate | When threat modelling, compliance, multi-party distribution or supply-chain assurance requires authenticity beyond digest integrity |
| ADR-013-DQ-008 | **DEFERRED — INACTIVE** | Remote archival store technology for retained semantic releases | Production/recovery architecture candidate | When deployment-packaged artefacts alone are insufficient for required retention, recovery or historical reconstruction scale |
| ADR-013-DQ-009 | **DEFERRED — INACTIVE** | Dedicated semantic-registry service / remote definition retrieval | Platform topology architecture candidate | When independent semantic publication cadence, scale or topology demonstrates that deployment packaging is materially constraining |
| ADR-013-DQ-010 | **DEFERRED — INACTIVE** | Compatibility-aware request routing / serving-cohort partitioning | Runtime/deployment architecture candidate | When uniform serving-cohort materialisation becomes operationally too restrictive and mixed support cohorts are required |
| ADR-013-DQ-011 | **DEFERRED — INACTIVE** | Dynamic historical semantic materialisation | Historical execution architecture candidate | When required historical release volume makes packaging all required runtime materialisation impractical |
| ADR-013-DQ-012 | **DEFERRED — INACTIVE** | Automatic safe semantic-release retirement | Release-lifecycle architecture candidate | When manual retirement is operationally material and the no-orphan/recovery safety predicates can be proven mechanically |
| ADR-013-DQ-013 | **DEFERRED — INACTIVE** | Exact observability dashboard/alerting for semantic materialisation state | Observability/operations implementation under composite MS-PROT-068 | When production support requires a dedicated operational view beyond baseline logs/metrics/health evidence |

Traceability rules:

1. The `ADR-013-DQ-*` identifier MUST remain stable.
2. Promotion to material design work requires `DESIGN-RULES.md`; classification as implementation detail does not by itself create an ADR/TAS requirement.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. A deferred question MUST NOT reopen ADR-013's accepted materialisation invariants unless a governed amendment explicitly does so.
5. Git history preserves status transitions in this canonical register.

Target 3's Configuration activation/admission contract is now **RESOLVED** by MS-PROT-040 v1.2. Its narrower retained questions are catalogued in Section 4.3.

### 4.2 ADR-014 Deferred Question Catalogue

ADR-014 is Design-Closed while retaining the following narrower questions. Each identifier is stable and mirrored in ADR-014 itself. MS-PROT-086 v1.2 resolves the semantic guest Conversation browser-access mechanism while activating the still-unresolved physical guest contextual-access credential representation for production guest browser access. MS-PROT-093 v1.0 resolves ADR-014-DQ-014 for installed native merchant clients while preserving ADR-014's browser-specific trusted-execution profile.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| ADR-014-DQ-001 | **RESOLVED** | Exact WebAuthn/passkey implementation/provider/library | Resolved by ADR-015 — WebAuthn / Passkey Production Adapter | Spring Security `spring-security-webauthn` is the initial replaceable ceremony/verification adapter; GrandRue retains authoritative session and business-authority boundaries |
| ADR-014-DQ-002 | **DEFERRED — INACTIVE** | Exact hardware-security-key requirement for platform/high-risk roles | Security policy | Before high-privilege platform administration is production-enabled |
| ADR-014-DQ-003 | **DEFERRED — INACTIVE** | Exact password/KDF implementation where fallback passwords exist | Security implementation detail | Before fallback password credentials reach production |
| ADR-014-DQ-004 | **DEFERRED — INACTIVE** | Exact recovery evidence/mechanism for lost authenticators | Security architecture | Before production account/control recovery |
| ADR-014-DQ-005 | **DEFERRED — INACTIVE** | Exact CSRF implementation/library | Web-security implementation detail | During privileged browser implementation |
| ADR-014-DQ-006 | **RESOLVED** | Exact persistent Session Record schema/index strategy | Resolved by verified IMP-04 Session Record implementation evidence | V29 durable `session_record` persistence and `JooqSessionRecordStore` are implemented and verified through the PostgreSQL integration gate; later schema evolution remains ordinary governed implementation |
| ADR-014-DQ-007 | **DEFERRED — INACTIVE** | Exact session cache technology/invalidation strategy | Performance/security architecture | Only if authoritative persistence lookup becomes materially expensive |
| ADR-014-DQ-008 | **DEFERRED — INACTIVE** | Exact device-key storage/hardware binding strategy by client platform | Device-security implementation architecture | Before operational-device implementation |
| ADR-014-DQ-009 | **DEFERRED — INACTIVE** | Federated/OIDC/social authentication | Provider architecture candidate | When product requirements justify external identity federation |
| ADR-014-DQ-010 | **DEFERRED — INACTIVE** | CustomerAccount-specific authentication policy | Product/security design | Before optional customer accounts become production-active |
| ADR-014-DQ-011 | **DEFERRED — ACTIVE BEFORE PRODUCTION GUEST CONVERSATION BROWSER ACCESS** | Exact guest contextual-access credential representation | Security architecture | Before production guest Conversation browser access under MS-PROT-086 v1.2; the credential must prove only the exact purpose-bound Guest Conversation Access Grant and must not become Conversation identity, CustomerContext identity or cross-object authority |
| ADR-014-DQ-012 | **DEFERRED — INACTIVE** | Delegated staff-device enrolment by non-Controller administrators | Semantic/design candidate | When merchant evidence demonstrates Controller-only enrolment is inadequate |
| ADR-014-DQ-013 | **DEFERRED — INACTIVE** | Offline staff operational authority | Security/offline architecture | If offline merchant operations become required; MS-PROT-093 establishes the generic first-party client rule that consequential offline mutation is `ONLINE REQUIRED` unless the owning operation explicitly permits safe deferred submission, but does not create staff-specific offline authority |
| ADR-014-DQ-014 | **RESOLVED** | Native mobile authentication/session representation | Resolved by MS-PROT-093 v1.0 | Installed native merchant clients use the existing Identity, Session, Merchant Scope, Membership and Actor Authorisation authorities; where applicable passkey authentication uses platform-native authenticator facilities; a native client may receive a revocable high-entropy opaque server-authoritative session credential protected by platform secure storage and protected transport; browser cookie/CSRF rules remain browser-specific; exact header syntax, secure-storage API, rotation, proof-of-possession and attestation remain downstream unless threat-model evidence makes them material |
| ADR-014-DQ-015 | **DEFERRED — INACTIVE** | Dedicated distributed session service | Platform architecture | Only if future topology makes relational session authority unsuitable |
| ADR-014-DQ-016 | **DEFERRED — INACTIVE** | Risk-scoring model and automated security response | Security architecture | When sufficient telemetry/threat evidence justifies adaptive security |
| ADR-014-DQ-017 | **DEFERRED — INACTIVE** | Exact CSP/Trusted Types browser hardening profile | Frontend security implementation | Before privileged web production hardening |

Traceability rules:

1. The `ADR-014-DQ-*` identifier MUST remain stable.
2. Promotion to material design work requires `DESIGN-RULES.md`; implementation-detail classification does not itself require another ADR/TAS.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. A deferred question MUST NOT weaken ADR-014's accepted authentication/session invariants without governed amendment.
5. Git history preserves status transitions in this canonical register.

### 4.3 MS-PROT-040 v1.2 Deferred Question Catalogue

MS-PROT-040 v1.2 Design-Closes Target 3 while retaining the following narrower questions. Each identifier is stable and mirrored in the accepted amendment itself.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-040-V12-DQ-001 | **RESOLVED** | Exact persistence/control-plane representation of the Ordinary New-Configuration Semantic Release Reference | Resolved by MS-PROT-040 v1.5 | Append-only exact release-purpose decisions; current pointer per exact release/purpose; immutable ordinary-reference revisions; separate singleton versioned ordinary pointer; atomic dual-purpose proof on advance; pinned-release activation retains exact current decision identities |
| MS-PROT-040-V12-DQ-002 | **RESOLVED** | Exact storage/representation of Serving Deployment Admission Snapshots | Resolved by MS-PROT-040 v1.4 | Immutable normalized PostgreSQL snapshot header, materialised-release/digest evidence and path-scoped executable-support evidence; mutable currentness remains in a separate control record |
| MS-PROT-040-V12-DQ-003 | **RESOLVED** | Exact executable-support manifest/evidence encoding | Resolved by MS-PROT-040 v1.4 | Exact normalized generation/path/semantic-release/contract tuples; no boolean, release-wide flag or opaque caller assertion establishes support |
| MS-PROT-040-V12-DQ-004 | **RESOLVED** | Exact canonical identity/digest representation for a Configuration New-Activity Execution Requirement Set | Resolved by MS-PROT-040 v1.4 | `ms-reqset-v1:sha256:<lowerhex>` over the accepted versioned, sorted, length-prefixed UTF-8 encoding, with immutable normalized exact requirement evidence |
| MS-PROT-040-V12-DQ-005 | **RESOLVED** | Exact database/control-plane locking, CAS or fencing mechanism between activation and deployment promotion | Resolved by MS-PROT-040 v1.4 | One durable ordinary-cohort `STABLE`/`PROMOTING` control record; activation shared lock, promotion exclusive lock, two-phase external switch and fail-closed reconciliation |
| MS-PROT-040-V12-DQ-006 | **DEFERRED — INACTIVE** | Exact operator/automation workflow for advancing the ordinary new-configuration release | Release operations | Before routine production semantic-release promotion |
| MS-PROT-040-V12-DQ-007 | **DEFERRED — INACTIVE** | Staged rollout with multiple new-configuration semantic targets or compatibility-aware serving cohorts | Future deployment architecture | When one homogeneous ordinary serving cohort becomes materially restrictive |
| MS-PROT-040-V12-DQ-008 | **DEFERRED — INACTIVE** | Maximum age/revalidation policy for an approved but not-yet-activated revision where semantics have not changed | Security/operational policy | If long-lived pending configuration approvals become operationally material |
| MS-PROT-040-V12-DQ-009 | **DEFERRED — INACTIVE** | Exact transport/API representation of the new deployment-admission rejection classes | API implementation under composite MS-PROT-035 | During production API implementation |
| MS-PROT-040-V12-DQ-010 | **DEFERRED — INACTIVE** | Future delegation of ordinary first-configuration approval/activation to non-Controller merchant administrators | Semantic/access design | Only if merchant evidence shows Controller-only bootstrap authority is materially inadequate |
| MS-PROT-040-V12-DQ-011 | **DEFERRED — INACTIVE** | Exact retention period/storage strategy for deployment-admission provenance | Data protection/operations implementation under composite MS-PROT-053 | When concrete retention policy/storage is implemented; Target 17 deliberately does not invent a universal numeric duration |
| MS-PROT-040-V12-DQ-012 | **DEFERRED — INACTIVE** | Future merchant-selectable semantic release/channel | Product/semantic architecture candidate | Only if an explicit need arises for merchants to choose between simultaneously supported semantic release channels |

Traceability rules:

1. The `MS-PROT-040-V12-DQ-*` identifier MUST remain stable.
2. Material architecture promotion follows `DESIGN-RULES.md`; implementation details proceed under `IMPLEMENTATION-RULES.md` where applicable.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. A deferred question MUST NOT weaken MS-PROT-040 v1.2's exact-release, deployment-admission, generation-fencing or bidirectional configuration/deployment safety invariants without governed amendment.
5. Git history preserves status transitions in this canonical register.

### 4.4 MS-PROT-052 v1.2 Deferred Implementation Catalogue

MS-PROT-052 v1.2 Design-Closes Target 4 while retaining the following downstream implementation choices. The stable identifiers below are governance traceability aliases for the choices listed in MS-PROT-052 v1.2 Section 29; they do not promote those choices into material design decisions.

| ID | Status | Deferred question / implementation choice | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-052-V12-DQ-001 | **RESOLVED** | PostgreSQL table layout for Onboarding Cases, revisions, answers and submission evidence | Verified V34–V37 owner persistence | `docs/development/imp-05-onboarding-case-evidence-store-2026-08-28.md` and `imp-05-onboarding-exact-submission-initial-intent-2026-08-29.md`; immutable revisions/answers and exact submission/handoff evidence; reconciliation recorded in `imp-05-r1b-durable-change-conformance-2026-09-06.md` |
| MS-PROT-052-V12-DQ-002 | **RESOLVED** | Aggregate persistence style | Verified `JooqOnboardingCaseEvidenceStore` transaction boundary | `docs/development/imp-05-onboarding-case-evidence-store-2026-08-28.md`; durable current case plus immutable revision/answer evidence; no snapshot-optimization decision is implied |
| MS-PROT-052-V12-DQ-003 | **DEFERRED — INACTIVE** | Snapshot versus deterministic reconstruction optimisation | Performance/persistence implementation | When observed state volume or resume latency makes snapshotting material |
| MS-PROT-052-V12-DQ-004 | **RESOLVED** | jOOQ record layout | Verified `JooqOnboardingCaseEvidenceStore` mapping | `docs/development/imp-05-onboarding-case-evidence-store-2026-08-28.md`; production owner-table mappings and PostgreSQL tests exist; later mapping evolution remains implementation detail |
| MS-PROT-052-V12-DQ-005 | **DEFERRED — INACTIVE** | REST endpoint naming and transport representation | API implementation under composite MS-PROT-035 | During production onboarding API implementation |
| MS-PROT-052-V12-DQ-006 | **DEFERRED — INACTIVE** | Background worker implementation for any asynchronous handoff/reconciliation path | Background-process implementation under composite MS-PROT-065 | During implementation if asynchronous handling is selected |
| MS-PROT-052-V12-DQ-007 | **DEFERRED — INACTIVE** | Message broker choice | Infrastructure implementation under composite MS-PROT-026/MS-PROT-065 | Only if accepted orchestration requires a broker rather than an in-process/durable alternative |
| MS-PROT-052-V12-DQ-008 | **DEFERRED — INACTIVE** | Frontend route structure | UI/presentation implementation | When merchant-web onboarding routes are implemented |
| MS-PROT-052-V12-DQ-009 | **DEFERRED — INACTIVE** | Progress-display representation | UI/presentation implementation | When onboarding UX/progress presentation is designed |
| MS-PROT-052-V12-DQ-010 | **DEFERRED — INACTIVE** | Autosave timing | UI/application implementation policy | When merchant-web autosave behaviour is implemented and tested |
| MS-PROT-052-V12-DQ-011 | **DEFERRED — INACTIVE** | Localisation implementation | Presentation implementation | When multilingual onboarding presentation is implemented |
| MS-PROT-052-V12-DQ-012 | **DEFERRED — INACTIVE** | Analytics / experimentation framework | Analytics/onboarding implementation | When question optimisation or experimentation is introduced |
| MS-PROT-052-V12-DQ-013 | **DEFERRED — INACTIVE** | AI model/provider | Target 6 / AI implementation architecture | During Target 6 production AI inference boundary and provider selection |

Traceability rules:

1. The `MS-PROT-052-V12-DQ-*` identifier MUST remain stable once used in governance or implementation evidence.
2. These rows remain inactive implementation/downstream choices unless evidence deliberately promotes one into material design under `DESIGN-RULES.md`.
3. Resolution MUST record the relevant implementation evidence or later accepted authority, as applicable.
4. None of these choices may weaken MS-PROT-052 v1.2's durable-case, revision-concurrency, correction-provenance, exact-review, immutable-intent or non-authoritative-AI boundaries.
5. Git history preserves status transitions in this canonical register.

### 4.5 MS-PROT-051 v1.1 Deferred Question Catalogue

MS-PROT-051 v1.1 Design-Closes Target 5 while retaining the following narrower implementation, downstream-design and future semantic questions. Each identifier is stable and mirrored in the accepted amendment itself.

Accepted MS-PROT-051 v1.6 supplies bounded commercial classifications for existing Profile public source observation, merchant preparation, observation, authoring, retirement and privacy-only restriction. It does not close or promote any Profile deferred decision below. Exact entitlement identities and the complete catalogue remain open under `MS-PROT-056-V17-DQ-001` in Section 4.19; production activation and C3 completion are not authorised.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-051-V11-DQ-001 | **DEFERRED — INACTIVE** | Exact PostgreSQL table/index layout for descriptor/contact/location/service-area/external-presence revisions | Persistence implementation | During production Profile persistence implementation |
| MS-PROT-051-V11-DQ-002 | **RESOLVED** | Exact structured international postal-address representation | Resolved by MS-PROT-051 v1.2: provider-neutral `PostalAddressV1`, exact original and normalized structured evidence, schema/normalization/country-registry affinity and optional accepted-coordinate provenance | Merchant Location identity remains independent of address; provider/geocoder workflow remains deferred under DQ-003 |
| MS-PROT-051-V11-DQ-003 | **DEFERRED — INACTIVE** | Exact geocoding/map provider and coordinate-normalisation process | Provider/location implementation | When coordinate/geocoding support is introduced |
| MS-PROT-051-V11-DQ-004 | **DEFERRED — INACTIVE** | Exact UI/API workflow distinguishing same-place correction from relocation | API/presentation implementation under composite MS-PROT-035 | During merchant Profile API/UX implementation |
| MS-PROT-051-V11-DQ-005 | **RESOLVED** | Exact Profile projection contracts, freshness and rebuild evidence | Resolved by MS-PROT-027 v1.4 | `platform/merchant-presence` now governs initial request-scoped projection, source-revision provenance, conservative stale-serving, reduced serviceability, rebuild/recompute and Exposure separation |
| MS-PROT-051-V11-DQ-006 | **RESOLVED** | Exact runtime Profile/Location exposure-policy representation and resolver | Resolved by composite MS-PROT-027 through v1.12 | Exact release-affined Exposure Element Contracts, trusted Audience Observation Context, owner-qualified Profile choices, instance-aware deterministic E4, opaque evaluator submission affinity and fail-closed behaviour govern the Profile/Location Exposure boundary; MS-PROT-051 v1.5 supplies exact Merchant Location choice persistence semantics. |
| MS-PROT-051-V11-DQ-007 | **DEFERRED — INACTIVE** | Exact registered profile-management privilege identifiers/default Role templates | Access implementation | Before delegated staff Profile administration is exposed |
| MS-PROT-051-V11-DQ-008 | **DEFERRED — INACTIVE** | Exact durable mechanism for submitted-Onboarding → Profile bootstrap consequence | Orchestration/background implementation under composite MS-PROT-026/MS-PROT-065 | During implementation if an asynchronous durable handoff is selected |
| MS-PROT-051-V11-DQ-009 | **DEFERRED — INACTIVE** | Exact Profile/Location REST/transport contracts | API implementation under composite MS-PROT-035 | During production Profile/Location API implementation |
| MS-PROT-051-V11-DQ-010 | **DEFERRED — INACTIVE** | Historical Profile/Location revision retention periods | Data protection/legal-policy implementation under composite MS-PROT-053 | When concrete retention policy is implemented; Target 17 supplies lifecycle semantics but deliberately not a universal numeric duration |
| MS-PROT-051-V11-DQ-011 | **DEFERRED — INACTIVE** | Exact external profile-sync conflict policy/provider adapters | Provider/integration architecture | When two-way external profile synchronisation is introduced |
| MS-PROT-051-V11-DQ-012 | **DEFERRED — INACTIVE** | Exact merchant classification taxonomy/governance | Product/discovery metadata | When classification must become standardised across production features |
| MS-PROT-051-V11-DQ-013 | **DEFERRED — INACTIVE** | Bulk multi-location editing/copy semantics | Application/UX implementation | When merchant scale demonstrates need |
| MS-PROT-051-V11-DQ-014 | **DEFERRED — INACTIVE** | Future reactivation of a RETIRED location identity | Semantic candidate | Only if production evidence shows new identity on reopening causes material operational harm |

Traceability rules:

1. The `MS-PROT-051-V11-DQ-*` identifier MUST remain stable once used in governance or implementation evidence.
2. Material semantic/architecture promotion follows `DESIGN-RULES.md`; implementation details proceed under `IMPLEMENTATION-RULES.md` where appropriate.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. No deferred item may silently weaken MS-PROT-051 v1.1's fact-identity, revision, retirement, onboarding-conflict or ownership boundaries.
5. Git history preserves status transitions in this canonical register.

### 4.6 MS-PROT-057 v1.1 Deferred Question Catalogue

MS-PROT-057 v1.1 Design-Closes Target 6 while retaining the following narrower implementation, security, operational and future AI-platform questions. Each identifier is stable and mirrored in the accepted amendment itself.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-057-V11-DQ-001 | **DEFERRED — INACTIVE** | Initial AI provider/model selection | AI implementation architecture | Before production AI adapter implementation |
| MS-PROT-057-V11-DQ-002 | **DEFERRED — INACTIVE** | Exact provider contractual retention/training/data-residency configuration | Security/data-protection/legal-policy implementation under composite MS-PROT-053 | Before production merchant data is sent externally |
| MS-PROT-057-V11-DQ-003 | **DEFERRED — INACTIVE** | Exact AI SDK/client library | Implementation detail | During adapter implementation |
| MS-PROT-057-V11-DQ-004 | **DEFERRED — INACTIVE** | Exact prompt-template representation/version store | AI implementation architecture | During production prompt deployment |
| MS-PROT-057-V11-DQ-005 | **DEFERRED — INACTIVE** | Exact structured-output/schema mechanism | AI implementation detail | During adapter implementation |
| MS-PROT-057-V11-DQ-006 | **DEFERRED — INACTIVE** | Exact token/context/output budgets | Resource/AI operations policy | During production load/cost testing |
| MS-PROT-057-V11-DQ-007 | **DEFERRED — INACTIVE** | Exact provider timeout/retry/backoff policy | Resilience implementation | During provider adapter implementation |
| MS-PROT-057-V11-DQ-008 | **DEFERRED — INACTIVE** | Initial provider/model failover matrix | AI resilience architecture | Before automatic model/provider failover is enabled |
| MS-PROT-057-V11-DQ-009 | **DEFERRED — INACTIVE** | Exact inference evaluation corpus and promotion thresholds | AI quality/security implementation | Before first production inference deployment |
| MS-PROT-057-V11-DQ-010 | **DEFERRED — INACTIVE** | Exact prompt-injection detection/filtering stack | AI security implementation | Before external/untrusted document ingestion is enabled |
| MS-PROT-057-V11-DQ-011 | **DEFERRED — INACTIVE** | RAG/vector retrieval architecture | Future AI retrieval architecture | When static bounded context packages become materially insufficient |
| MS-PROT-057-V11-DQ-012 | **DEFERRED — INACTIVE** | Embedding model/vector-store product | Future implementation | Only if RAG is introduced |
| MS-PROT-057-V11-DQ-013 | **DEFERRED — INACTIVE** | Exact inference-record retention periods | Data protection/legal-policy implementation under composite MS-PROT-053 | When concrete inference-record retention policy is implemented; Target 17 deliberately leaves exact durations downstream |
| MS-PROT-057-V11-DQ-014 | **DEFERRED — INACTIVE** | Exact AI observability dashboards/quality-drift alerts | Observability implementation under composite MS-PROT-068 | During production AI observability implementation |
| MS-PROT-057-V11-DQ-015 | **DEFERRED — INACTIVE** | Exact API representation of AI results/clarification | API implementation under composite MS-PROT-035 | During production AI API implementation |
| MS-PROT-057-V11-DQ-016 | **DEFERRED — INACTIVE** | Fine-tuning/distillation dataset governance and pipeline | Future AI learning architecture | Before production data is used to train/distil a GrandRue model; MS-PROT-053 v1.2 requires separate accepted purpose/use authority |
| MS-PROT-057-V11-DQ-017 | **DEFERRED — INACTIVE** | Self-hosted/small specialised model strategy | Future AI platform architecture | When scale, privacy, latency or cost justifies owning inference infrastructure |
| MS-PROT-057-V11-DQ-018 | **DEFERRED — INACTIVE** | Persistent conversational memory storage implementation | AI/product implementation | When persistent assistant memory beyond bounded interaction history is introduced |
| MS-PROT-057-V11-DQ-019 | **DEFERRED — INACTIVE** | Read-only specialist retrieval tool catalogue | Capability-specific later targets | When a later capability needs AI-context retrieval beyond supplied projections |
| MS-PROT-057-V11-DQ-020 | **DEFERRED — INACTIVE** | Exact human-feedback labelling/evaluation workflow | AI quality implementation | Before systematic merchant-correction learning begins |

Traceability rules:

1. The `MS-PROT-057-V11-DQ-*` identifier MUST remain stable once used in governance or implementation evidence.
2. Material semantic/architecture promotion follows `DESIGN-RULES.md`; implementation details proceed under `IMPLEMENTATION-RULES.md` where appropriate.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. No deferred item may silently weaken composite MS-PROT-057's inference-contract, bounded-context, deterministic-validation, merchant-intent-confirmation, AI-optionality, progressive-website-assistance or model-deployment-evaluation boundaries.
5. Git history preserves status transitions in this canonical register.

### 4.7 MS-PROT-027 v1.4 Deferred Question Catalogue

MS-PROT-027 v1.4 Design-Closes Target 7 while retaining the following narrower implementation, performance, delivery and later-target questions. Each identifier is stable and mirrored in the accepted amendment itself.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-027-V14-DQ-001 | **RESOLVED** | Exact Java representation/registration mechanism for Projection Contracts | Resolved by MS-PROT-027 v1.7 | Immutable owner-qualified typed definitions, closed applicability/materialisation vocabularies, exact-release registry lookup and the initial Merchant Presence/Calendar registrations; serviceability evaluation is resolved by MS-PROT-027 v1.8 |
| MS-PROT-027-V14-DQ-002 | **DEFERRED — INACTIVE** | Exact source-revision/checkpoint representation | Implementation detail | When first non-request-scoped projection is implemented |
| MS-PROT-027-V14-DQ-003 | **DEFERRED — INACTIVE** | Whether any initial Merchant Presence data needs physical cache/materialisation | Performance architecture | Only if measured storefront performance requires it |
| MS-PROT-027-V14-DQ-004 | **DEFERRED — INACTIVE** | Exact CDN/static-generation contract for storefronts | Delivery architecture | Before CDN/static storefront generation is enabled |
| MS-PROT-027-V14-DQ-005 | **DEFERRED — INACTIVE** | Exact Calendar projection materialisation/cache strategy | Performance / Scheduling implementation | MS-PROT-042 v1.6 establishes request-scoped initial Scheduling evaluation; revisit physical Calendar materialisation/cache only when measured performance requirements justify it |
| MS-PROT-027-V14-DQ-006 | **DEFERRED — INACTIVE** | Exact projection rebuild worker/process mechanism | Projection/background implementation under composite MS-PROT-026/MS-PROT-065 | When a materialised projection requires asynchronous rebuild |
| MS-PROT-027-V14-DQ-007 | **DEFERRED — INACTIVE** | Exact projection-update idempotency/checkpoint persistence schema | Projection/persistence implementation under composite MS-PROT-026/MS-PROT-065 | When asynchronous projection updates are introduced |
| MS-PROT-027-V14-DQ-008 | **DEFERRED — INACTIVE** | Exact stale/degraded transport representation | API implementation under composite MS-PROT-035 | During production API representation implementation |
| MS-PROT-027-V14-DQ-009 | **DEFERRED — INACTIVE** | Exact projection lag/serviceability operational telemetry | Observability implementation under composite MS-PROT-068 | During production Projection observability implementation |
| MS-PROT-027-V14-DQ-010 | **DEFERRED — INACTIVE** | Whether Redis or another dedicated cache is justified | Implementation/performance architecture | Only after measured DB/query load demonstrates need |
| MS-PROT-027-V14-DQ-011 | **DEFERRED — INACTIVE** | Exact search-index technology and rebuild contract | Later owning capability | When Publication/Product/search requirements demonstrate an index is needed |
| MS-PROT-027-V14-DQ-012 | **DEFERRED — INACTIVE** | Generic reusable projection-support library/framework | Future implementation architecture | Only after multiple concrete implementations demonstrate meaningful common behaviour |
| MS-PROT-027-V14-DQ-013 | **DEFERRED — INACTIVE** | Exact reduced/degraded Calendar presentation semantics | UI/presentation implementation | MS-PROT-042 v1.6 resolves backend `UNRESOLVED` Scheduling meaning; revisit exact wording/icons/layout only when Calendar presentation is designed |
| MS-PROT-027-V14-DQ-014 | **DEFERRED — INACTIVE** | Exact field/element-level Profile projection provenance encoding | Persistence/query implementation | During Merchant Presence implementation; MS-PROT-027 v1.13 now constrains this implementation to exact bounded-read material/source-progress affinity without prescribing the owner-specific encoding |

Traceability rules:

1. The `MS-PROT-027-V14-DQ-*` identifier MUST remain stable.
2. Material architecture promotion follows `DESIGN-RULES.md`; implementation detail follows `IMPLEMENTATION-RULES.md` where applicable.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. No deferred item may silently weaken MS-PROT-027 v1.4's explicit owner, freshness, serviceability, Exposure separation, source-authority, request-scoped-default or no-stale-write invariants.
5. Git history preserves status transitions in this canonical register.

### 4.8 MS-PROT-027 v1.5 Deferred Question Catalogue

MS-PROT-027 v1.5 Design-Closes Target 8 while retaining the following narrower implementation, later-capability, delivery and operational questions. Later accepted composite MS-PROT-027 through v1.13 resolves additional runtime/result/downstream representation questions without reopening Target 8.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-027-V15-DQ-001 | **RESOLVED** | Exact Java representation and registration mechanism for Exposure Element Contracts | Resolved by MS-PROT-027 v1.6: immutable typed owner-qualified contracts, exact-release registry snapshot and initial code-registered PUBLIC Merchant Presence portfolio; v1.11 adds explicit member-identity semantics and deterministic audience-variant indexing | Current implementation must evolve the accepted contract representation under v1.11; no design question remains |
| MS-PROT-027-V15-DQ-002 | **RESOLVED** | Exact semantic-bundle encoding/materialisation of Exposure definitions | Resolved by ADR-016 and verified E2 implementation evidence | Semantic bundle v2 carries an integrity-covered Exposure-definition constituent and exact-release all-or-nothing materialisation; v1.11 evolves the inner Exposure-definition encoding without changing the outer bundle architecture |
| MS-PROT-027-V15-DQ-003 | **RESOLVED** | Exact PostgreSQL representation of Profile-owned public Exposure choices | Resolved by MS-PROT-051 v1.5 together with the existing revision-affined Contact Point persistence model | Merchant Location uses independent choice revision/current-pointer relations keyed to stable Location identity; Contact Point keeps revision-affined choice. Production implementation/conformance is the current LEC1 work, not an open design decision. |
| MS-PROT-027-V15-DQ-004 | **DEFERRED — INACTIVE** | Exact transport/application operation representation for changing Profile Exposure choices | API implementation under composite MS-PROT-035 | During production Profile Exposure API implementation |
| MS-PROT-027-V15-DQ-005 | **DEFERRED — INACTIVE** | Exact CUSTOMER Exposure requirement evaluators for later capability scopes | Owning capability targets | Booking/Appointment scope resolved by MS-PROT-042 v1.6; Ordering scope resolved by MS-PROT-077 v1.1; Payment scope resolved by MS-PROT-055 v1.1; Order Fulfilment/Shipment tracking scope resolved by MS-PROT-060 v1.1 through `ordering / related-customer-order`; Returns customer scope resolved by MS-PROT-061 v1.1 through reuse of `ordering / related-customer-order`; revisit any other remaining customer-facing capability scope during its applicable target |
| MS-PROT-027-V15-DQ-006 | **DEFERRED — INACTIVE** | Exact MERCHANT field-observation privilege/context mappings where a capability requires them | Owning capability/access implementation | When a protected MERCHANT read element requires concrete privilege/context mapping |
| MS-PROT-027-V15-DQ-007 | **RESOLVED** | Concrete data-protection restriction-evaluator integration and lifecycle consequences | Resolved semantically by MS-PROT-053 v1.2 | Owner-qualified lifecycle evaluation, fail-closed use authority, owner-safe disposition and Projection/Exposure convergence now govern the production semantic boundary; exact Java/persistence implementation remains downstream |
| MS-PROT-027-V15-DQ-008 | **DEFERRED — INACTIVE** | Exact API distinction among withheld, absent, not-serviceable and unavailable representations | API implementation under composite MS-PROT-035 | During production API representation implementation |
| MS-PROT-027-V15-DQ-009 | **DEFERRED — INACTIVE** | Exposure-resolution operational telemetry and diagnostic metrics | Observability implementation under composite MS-PROT-068 | During production Exposure observability implementation |
| MS-PROT-027-V15-DQ-010 | **DEFERRED — INACTIVE** | Whether Exposure-result caching/memoisation is ever justified | Performance architecture | Only after measured need demonstrates request-scoped resolution is insufficient |
| MS-PROT-027-V15-DQ-011 | **DEFERRED — INACTIVE** | CDN/static-storefront Exposure-revocation propagation contract | Delivery architecture | If CDN/static storefront delivery is introduced |
| MS-PROT-027-V15-DQ-012 | **DEFERRED — INACTIVE** | Search/index convergence when an exposed element becomes withheld | Search/discovery implementation | When search/indexing is introduced |

Traceability rules:

1. The `MS-PROT-027-V15-DQ-*` identifier MUST remain stable.
2. Material architecture promotion follows `DESIGN-RULES.md`; implementation detail follows `IMPLEMENTATION-RULES.md` where applicable.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. No deferred item may weaken composite MS-PROT-027's fail-closed exact-release, owner-qualified policy, current revocation, audience-context, instance-membership, evaluator-submission-affinity, bounded-read same-material affinity or authority-separation invariants.
5. Git history preserves status transitions in this canonical register.

### 4.9 MS-PROT-048 v1.4 Deferred Question Catalogue

MS-PROT-048 v1.4 Design-Closes Target 9 while retaining the following narrower implementation, provider-adapter, operational and later-target questions. Each identifier is stable and mirrored in the accepted amendment itself. Later MS-PROT-048 v1.5 resolves the platform-originated requirement/routing gap for Target 16 without changing these identifiers.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-048-V14-DQ-001 | **DEFERRED — INACTIVE** | Exact Java representation of ProviderConnection | Provider implementation | During production ProviderConnection implementation |
| MS-PROT-048-V14-DQ-002 | **DEFERRED — INACTIVE** | Exact persisted ProviderConnection lifecycle vocabulary | Provider persistence implementation | During ProviderConnection persistence design |
| MS-PROT-048-V14-DQ-003 | **DEFERRED — INACTIVE** | Exact Java Provider Readiness resolver/result representation | Runtime implementation | During provider-dependent runtime implementation |
| MS-PROT-048-V14-DQ-004 | **DEFERRED — INACTIVE** | Exact provider-contract declaration of readiness evidence requirements | Provider adapter implementation | When the first concrete provider readiness adapter is implemented |
| MS-PROT-048-V14-DQ-005 | **DEFERRED — INACTIVE** | Exact health-evidence freshness/expiry rules per provider | Provider implementation | During concrete provider readiness implementation |
| MS-PROT-048-V14-DQ-006 | **DEFERRED — INACTIVE** | OAuth/API-key/etc. provider-specific connection mechanics | Individual provider adapters | When each provider integration is implemented |
| MS-PROT-048-V14-DQ-007 | **DEFERRED — INACTIVE** | Connection reconciliation/background validation mechanism | Provider/background implementation under composite MS-PROT-065; reconciliation semantics under composite MS-PROT-069 | During implementation of provider connection validation/reconciliation |
| MS-PROT-048-V14-DQ-008 | **DEFERRED — INACTIVE** | Provider readiness telemetry and operational dashboards | Observability implementation under composite MS-PROT-068 | During production Provider Readiness observability implementation |
| MS-PROT-048-V14-DQ-009 | **DEFERRED — INACTIVE** | Public/API representation of connection/readiness failures | API implementation under composite MS-PROT-035 | During production API implementation |
| MS-PROT-048-V14-DQ-010 | **DEFERRED — INACTIVE** | Exact persistent storage/index strategy for ProviderConnections | Persistence implementation | During ProviderConnection persistence implementation |
| MS-PROT-048-V14-DQ-011 | **DEFERRED — INACTIVE** | Whether readiness caching is ever justified | Performance architecture | Only after measured need demonstrates current-state resolution is insufficient |
| MS-PROT-048-V14-DQ-012 | **DEFERRED — INACTIVE** | Exact provider-wide status ingestion mechanism | Operations/provider implementation | When provider-wide status ingestion becomes necessary |
| MS-PROT-048-V14-DQ-013 | **DEFERRED — INACTIVE** | Provider & Integration AI Assistant concrete contracts | Future AI/provider assistance | When provider-assistance AI is deliberately promoted |
| MS-PROT-048-V14-DQ-014 | **DEFERRED — INACTIVE** | Capability-specific degraded-operation rules outside resolved Scheduling, Payment, Shipment, Returns return-label and Notification scopes | Owning later capability targets | Scheduling-specific degraded-operation semantics resolved by MS-PROT-042 v1.6; Payment-specific semantics resolved by MS-PROT-055 v1.1; Shipment-specific semantics resolved by MS-PROT-060 v1.1; Returns return-label-specific semantics resolved by MS-PROT-061 v1.1; Notification-specific semantics resolved by MS-PROT-075 v1.1; revisit any other remaining provider-dependent capability scope when it requires a concrete degraded path |

Traceability rules:

1. The `MS-PROT-048-V14-DQ-*` identifier MUST remain stable.
2. Material architecture promotion follows `DESIGN-RULES.md`; implementation detail follows `IMPLEMENTATION-RULES.md` where applicable.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. No deferred item may weaken composite MS-PROT-048's obligation-qualified, fail-closed, routing/binding/connection/credential separation, no-substitution or execution-uncertainty boundaries.
5. Git history preserves status transitions in this canonical register.

### 4.10 MS-PROT-046 v1.2 Deferred Question Catalogue

MS-PROT-046 v1.2 is one half of the Target-10 Design-Closure package and retains the following narrower implementation, delivery and later-target questions.

Accepted MS-PROT-046 v1.4 resolves only the commercial classification of bounded PUBLIC source observation and Opportunity-to-Enquiry participation. Accepted v1.5 additionally supplies bounded merchant preparation, observation, authoring and withdrawal commercial classifications. Neither amendment closes or promotes the Publication deferred questions below; the remaining catalogue work is tracked under `MS-PROT-056-V17-DQ-001` in Section 4.19.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-046-V12-DQ-001 | **DEFERRED — INACTIVE** | Exact Java representation of Publication identities/revisions | Publication implementation | During production Publication implementation |
| MS-PROT-046-V12-DQ-002 | **DEFERRED — INACTIVE** | Exact PostgreSQL layout/indexes | Persistence implementation | During production Publication persistence implementation |
| MS-PROT-046-V12-DQ-003 | **DEFERRED — INACTIVE** | Exact transport contracts for create/revise/publish/withdraw/republish | API implementation under composite MS-PROT-035 | During production Publication API implementation |
| MS-PROT-046-V12-DQ-004 | **DEFERRED — INACTIVE** | Search/index architecture | Search/discovery implementation | When search/discovery is introduced |
| MS-PROT-046-V12-DQ-005 | **DEFERRED — INACTIVE** | CDN/static-generation Publication-revocation convergence | Delivery architecture | If CDN/static storefront generation is introduced |
| MS-PROT-046-V12-DQ-006 | **DEFERRED — INACTIVE** | Social-distribution worker/provider implementation | Provider/background implementation under composite MS-PROT-026/MS-PROT-065 | When social-distribution implementation is introduced |
| MS-PROT-046-V12-DQ-007 | **DEFERRED — INACTIVE** | Subscription public transport and delivery preference UX | API/presentation implementation under composite MS-PROT-035 | During subscription API/presentation implementation; Target 16 closes provider-delivery execution but not UI/transport detail |
| MS-PROT-046-V12-DQ-008 | **DEFERRED — INACTIVE** | Exact retained historical Publication revision period | Data protection/legal-policy implementation under composite MS-PROT-053 | When concrete retention policy is implemented; Target 17 deliberately does not invent a universal numeric period |

Traceability rules:

1. The `MS-PROT-046-V12-DQ-*` identifier MUST remain stable.
2. Material semantic/architecture promotion follows `DESIGN-RULES.md`; implementation detail follows `IMPLEMENTATION-RULES.md` where applicable.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. No deferred item may weaken MS-PROT-046 v1.2's stable-identity/revision, concurrency, idempotency, lifecycle/Exposure separation, explicit subject-participation or request-scoped-read invariants.
5. Git history preserves status transitions in this canonical register.

### 4.11 MS-PROT-043 v1.4 Deferred Question Catalogue

MS-PROT-043 v1.4 is one half of the Target-10 Design-Closure package and retains narrower implementation, communication and later-target questions. MS-PROT-053 v1.3 resolves DQ-007's Enquiry/Customer Communication retention-period scope without reopening the remaining items.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-043-V14-DQ-001 | **DEFERRED — INACTIVE** | Exact Enquiry persistence representation | Enquiry implementation | During production Enquiry persistence implementation |
| MS-PROT-043-V14-DQ-002 | **DEFERRED — INACTIVE** | Exact logical idempotency-key representation | Enquiry/API implementation | During Enquiry command/API implementation |
| MS-PROT-043-V14-DQ-003 | **DEFERRED — INACTIVE** | Exact subject-provenance encoding | Enquiry persistence implementation | During production Enquiry persistence implementation |
| MS-PROT-043-V14-DQ-004 | **DEFERRED — INACTIVE** | Exact Enquiry API/form transport representation | API implementation under composite MS-PROT-035 | During production Enquiry API implementation |
| MS-PROT-043-V14-DQ-005 | **RESOLVED** | Exact Merchant Attention persistence/model beyond minimal projection | Resolved by MS-PROT-085 v1.0 | Accepted 8 September 2026; MS-PROT-085 establishes the bounded Merchant Attention contract, occurrence, persistence, handling, coverage and action-handoff model |
| MS-PROT-043-V14-DQ-006 | **RESOLVED** | Exact durable Conversation creation policy/channel mapping | Resolved by MS-PROT-086 v1.0 | Accepted 8 September 2026; MS-PROT-086 establishes contract-governed Conversation creation/reuse, participant/guest/channel bindings, immutable Message continuity and provider-neutral channel mapping while leaving production portfolios separately gated |
| MS-PROT-043-V14-DQ-007 | **RESOLVED** | Enquiry/communication retention periods | Resolved by MS-PROT-053 v1.3 | Accepted 8 September 2026; 12-month Transitory Enquiry baseline, 24-month per-Message Ordinary Customer Communication baseline, shorter bounded provider/security/tombstone periods, minimum-scope qualified business-evidence inheritance, plan-neutral lifecycle and recovery-owned backup retention |
| MS-PROT-043-V14-DQ-008 | **DEFERRED — INACTIVE** | Enquiry/attention telemetry | Observability implementation under composite MS-PROT-068 | During production Enquiry/attention observability implementation |

Traceability rules:

1. The `MS-PROT-043-V14-DQ-*` identifier MUST remain stable.
2. Material semantic/architecture promotion follows `DESIGN-RULES.md`; implementation detail follows `IMPLEMENTATION-RULES.md` where applicable.
3. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
4. No deferred item may weaken MS-PROT-043 v1.4's durable-Enquiry-truth, current-subject revalidation, immutable-submission-provenance, idempotency, no-automatic-CustomerContext/Conversation/commitment or request-scoped-read invariants.
5. Git history preserves status transitions in this canonical register.

### 4.12 MS-PROT-080 v1.1 Deferred Question Catalogue

Composite MS-PROT-080 through v1.4 accepts the generic Workforce Compensation architecture and its commercial-access classification while deliberately retaining the following jurisdiction, implementation, integration, presentation and future-capability questions. MS-PROT-080 v1.2 resolves the Timekeeping/payable-time ownership question, MS-PROT-080 v1.3 requires exact Workforce Scheduling Arrangement/Compensation Relationship affinity, independently governs related-Payee Compensation self-service, removes `Paid Break Entitlement` as a canonical generic concept and narrows generic Payee access mechanics, MS-PROT-080 v1.4 resolves DQ-016 for exact owner-qualified commercial-access classification without minting concrete Commercial Entitlement identities or activating implementation, and MS-PROT-082 v1.0 resolves only the product semantics of partial versus full jurisdiction support without resolving Payroll-specific rules or implementation.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-080-V11-DQ-001 | **DEFERRED — INACTIVE** | First Workforce Compensation jurisdiction rollout | Product / implementation sequencing | Before the first Workforce Compensation implementation slice is promoted |
| MS-PROT-080-V11-DQ-002 | **DEFERRED — INACTIVE** | Exact jurisdiction treatment-resolution mechanism | Jurisdiction design/implementation | Before a jurisdiction treatment resolver is implemented; MS-PROT-082 supplies the generic JRA substrate but does not define Payroll-specific Regulatory Purpose inputs/outputs or jurisdiction formulas |
| MS-PROT-080-V11-DQ-003 | **DEFERRED — INACTIVE** | Exact jurisdiction-specific fact/evidence schema | Jurisdiction semantic/implementation design | Before the first jurisdiction fact/evidence contract is implemented; MS-PROT-082 requires owner-qualified source facts/provenance but does not define the Payroll-specific schema |
| MS-PROT-080-V11-DQ-004 | **DEFERRED — INACTIVE** | Exact Java/persistence representation | Workforce Compensation implementation | During production Workforce Compensation implementation |
| MS-PROT-080-V11-DQ-005 | **RESOLVED** | Exact Timekeeping and payable-time authority | Resolved by composite MS-PROT-081 through v1.1 + composite MS-PROT-080 through v1.3 | Workforce Scheduling/Timekeeping owns arrangement-affined Scheduled Work, break, attendance and Approved Worked-Time evidence; Workforce Compensation owns monetary consequences, including break/leave compensation, and consumes the exact applicable Compensation Relationship affinity rather than searching current person/Membership state |
| MS-PROT-080-V11-DQ-006 | **DEFERRED — INACTIVE** | Exact project/work-progress owner and internal contract | Future Project/Work Progress owner | Before project/progress-derived compensation becomes production-active |
| MS-PROT-080-V11-DQ-007 | **DEFERRED — INACTIVE** | Exact calculation engines/libraries/providers | Jurisdiction Payroll implementation | Before a concrete jurisdiction calculation implementation is selected |
| MS-PROT-080-V11-DQ-008 | **DEFERRED — INACTIVE** | Exact regulatory APIs, credentials and certification | Regulatory integration / security implementation | Before production regulatory submission is enabled |
| MS-PROT-080-V11-DQ-009 | **DEFERRED — INACTIVE** | Exact payment/remittance rails | Payment/banking fulfilment | Before Workforce Compensation funds execution is enabled |
| MS-PROT-080-V11-DQ-010 | **DEFERRED — INACTIVE** | Exact correction/off-cycle rules per jurisdiction | Jurisdiction Payroll design | Before the first jurisdiction correction/off-cycle implementation |
| MS-PROT-080-V11-DQ-011 | **DEFERRED — INACTIVE** | Exact cross-border/treaty/social-security treatment | Jurisdiction/cross-border design | Before a supported jurisdiction requires cross-border treatment |
| MS-PROT-080-V11-DQ-012 | **DEFERRED — INACTIVE** | Exact jurisdiction retention/privacy policies | Data protection/legal-policy implementation under composite MS-PROT-053 | Before concrete jurisdiction retention/privacy configuration is implemented |
| MS-PROT-080-V11-DQ-013 | **DEFERRED — INACTIVE** | Exact generic Payee Compensation Self-Service authentication, API/Surface/Audience representation and legal-entity representative-access mechanism | API/security/presentation/access implementation | Current Membership-bound staff self-service trust is governed by composite MS-PROT-063/MS-PROT-074 and Compensation relationship eligibility by MS-PROT-080; revisit Payees without Merchant Membership, former Payees without current Membership, legal-entity representatives and any required new API/Exposure audience/context before generic Payee self-service is production-enabled |
| MS-PROT-080-V11-DQ-014 | **DEFERRED — INACTIVE** | Exact merchant/Payee onboarding wording | Presentation/localisation | During jurisdiction-specific Workforce Compensation UX design |
| MS-PROT-080-V11-DQ-015 | **DEFERRED — INACTIVE** | Exact generic professional/licensing capability | Future regulated-work capability | When cross-domain professional/licensing evidence requires a generic owner |
| MS-PROT-080-V11-DQ-016 | **RESOLVED** | Commercial packaging and entitlement | Resolved by MS-PROT-080 v1.4 | Accepted 15 September 2026: exact Workforce Compensation commercial access contracts and protected purposes `MAINTAIN_WORKFORCE_COMPENSATION_TERMS` and `ADMINISTER_WORKFORCE_COMPENSATION` are classified for BUSINESS + GROWTH with bounded no-entitlement preparation/observation/residual-resolution contracts; exact Commercial Entitlement identities and the complete catalogue remain under `MS-PROT-056-V17-DQ-001` |
| MS-PROT-080-V11-DQ-017 | **RESOLVED** | Exact product semantics for partial versus full jurisdiction support | Resolved by MS-PROT-082 v1.0 | Support is declared per exact Regulatory Purpose + Jurisdiction scope + Jurisdiction Pack Release + effective support interval; partial territorial support is valid, and whole-country support cannot be inferred from payments, currency, website delivery, one provider or one supported purpose |
| MS-PROT-080-V11-DQ-018 | **DEFERRED — INACTIVE** | Exact digital-signature/e-signature technology and assurance requirements per jurisdiction | Security/legal/document implementation | Before agreement e-signature is production-enabled |
| MS-PROT-080-V11-DQ-019 | **DEFERRED — INACTIVE** | Exact document rendering/storage technology | Document implementation | Before production Compensation Document generation/storage |
| MS-PROT-080-V11-DQ-020 | **DEFERRED — INACTIVE** | Exact jurisdiction document templates and legally required clauses/content | Jurisdiction/legal/document design | Before jurisdiction-specific agreement/pay-document production |

Traceability rules:

1. The `MS-PROT-080-V11-DQ-*` identifier MUST remain stable.
2. These questions remain inactive while Workforce Compensation is post-MVP and unsequenced unless a later sequencing authority promotes them.
3. Promotion of a material semantic/architecture question follows `DESIGN-RULES.md`; implementation details follow `IMPLEMENTATION-RULES.md` where applicable.
4. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
5. No deferred item may weaken composite MS-PROT-080 through v1.4's relationship-scoped treatment, Payroll/Non-Payroll separation, deterministic calculation, document-authority, exact Workforce Scheduling Arrangement/Compensation Relationship affinity, related-Payee self-service boundary, historical-affinity, privacy, provider-neutrality or commercial-access ownership boundaries.
6. Git history preserves status transitions in this canonical register.

### 4.13 MS-PROT-081 Deferred Question Catalogue

Composite MS-PROT-081 through v1.4 accepts Workforce Scheduling, Timekeeping, Scheduled Break, Leave, arrangement-affinity, cross-Arrangement overlap/buffer, the bounded Scheduling/Leave/Scheduled Work reminder Notification Contract portfolio, and exact owner-qualified commercial-access classifications while deliberately retaining the following implementation, jurisdiction, attendance-mechanism, presentation and later-policy questions. v1.2 resolves DQ-021. v1.3 partially resolves DQ-015 for Scheduling/Leave/Scheduled Work reminders while leaving exact Timekeeping notification/reminder semantics deferred pending DQ-009 through DQ-012. v1.4 resolves DQ-020 without minting concrete Commercial Entitlement identities or activating implementation. The identifiers remain stable across the amendment chain.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-081-DQ-001 | **DEFERRED — INACTIVE** | Exact Java/persistence representation | Workforce Scheduling/Timekeeping implementation | During production Workforce Scheduling/Timekeeping implementation |
| MS-PROT-081-DQ-002 | **DEFERRED — INACTIVE** | Exact shift-swap / shift-cover workflow | Workforce Scheduling semantic/application design | When merchants require shift swaps/covers beyond accepted assignment/offer semantics |
| MS-PROT-081-DQ-003 | **DEFERRED — INACTIVE** | Exact overtime authority and jurisdiction rules | Workforce Compensation/jurisdiction design | Before overtime becomes production-active in a supported jurisdiction |
| MS-PROT-081-DQ-004 | **DEFERRED — INACTIVE** | Exact minimum/maximum working-time compliance rules by jurisdiction | Jurisdiction workforce/legal-policy design | Before a supported jurisdiction requires working-time compliance enforcement |
| MS-PROT-081-DQ-005 | **DEFERRED — INACTIVE** | Exact break-compliance rules by jurisdiction | Jurisdiction workforce/legal-policy design | Before production break-compliance enforcement is activated |
| MS-PROT-081-DQ-006 | **DEFERRED — INACTIVE** | Exact leave category/entitlement formulas by jurisdiction | Jurisdiction workforce/leave design | Before leave entitlement calculation is production-active |
| MS-PROT-081-DQ-007 | **DEFERRED — INACTIVE** | Exact holiday-pay calculation rules | MS-PROT-080 jurisdiction implementation | Before paid leave/holiday calculation is production-active |
| MS-PROT-081-DQ-008 | **DEFERRED — INACTIVE** | Exact GPS/QR/NFC/shared-terminal attendance mechanisms | Attendance/security implementation | Before a concrete attendance capture mechanism is selected |
| MS-PROT-081-DQ-009 | **DEFERRED — INACTIVE** | Exact anti-time-theft/fraud evidence policy | Security/workforce policy | When production evidence justifies stronger attendance-integrity controls |
| MS-PROT-081-DQ-010 | **DEFERRED — INACTIVE** | Exact time-rounding rules | Workforce/jurisdiction policy | Before time rounding becomes production-active |
| MS-PROT-081-DQ-011 | **DEFERRED — INACTIVE** | Exact manager time-approval policy and optional self-attestation rules | Workforce application/policy | Before time approval workflow is implemented |
| MS-PROT-081-DQ-012 | **DEFERRED — INACTIVE** | Exact unscheduled-work handling | Workforce Scheduling/Timekeeping policy | Before unmatched attendance is production-active |
| MS-PROT-081-DQ-013 | **DEFERRED — INACTIVE** | Exact staff personal-surface API/UI | API/presentation implementation | Before Personal Workforce Self-Service delivery is implemented; any first-party native client implementation must additionally conform to MS-PROT-093 |
| MS-PROT-081-DQ-014 | **DEFERRED — INACTIVE** | Exact leave-balance projection representation | Projection/presentation implementation | Before leave balance is surfaced |
| MS-PROT-081-DQ-015 | **PARTIALLY RESOLVED — SCHEDULING/LEAVE/REMINDER PORTFOLIO RESOLVED; TIMEKEEPING REMAINDER DEFERRED** | Exact notification contracts for shift/leave/time actions | Scheduling/Leave slice resolved by MS-PROT-081 v1.3 with composite MS-PROT-075 delivery; Timekeeping remainder remains Workforce Timekeeping + MS-PROT-075 | Scheduling/Leave/Scheduled Work reminder contract portfolio accepted 9 September 2026; revisit Timekeeping notifications/reminders only after applicable DQ-009 through DQ-012 policy is accepted and before production Timekeeping notifications are implemented |
| MS-PROT-081-DQ-016 | **DEFERRED — INACTIVE** | Exact linkage representation between Workforce Scheduling Arrangement and customer-facing Resource | Workforce/Appointment interoperability implementation | Before the first linked staff Resource integration is implemented; MS-PROT-042 v1.8 requires arrangement-qualified affinity rather than Membership/Identity matching |
| MS-PROT-081-DQ-017 | **DEFERRED — INACTIVE** | Exact remediation workflow when leave/workforce changes conflict with committed Appointments | Workforce/Appointment orchestration design | Before production schedule changes can encounter committed-customer conflicts |
| MS-PROT-081-DQ-018 | **DEFERRED — INACTIVE** | Exact legal/commercial treatment of shift cancellation after acceptance | Jurisdiction/workforce policy | Before cancellation compensation/notice consequences are implemented |
| MS-PROT-081-DQ-019 | **DEFERRED — INACTIVE** | Exact native-mobile/offline workforce support | Workforce delivery/application design constrained by MS-PROT-093 | Before native/offline workforce operation is introduced; MS-PROT-093 already governs the generic first-party native-client portfolio, native session transport, cached-read non-authority and default `ONLINE REQUIRED` mutation boundary, while workforce-specific UX/authority mechanics remain deferred here |
| MS-PROT-081-DQ-020 | **RESOLVED** | Commercial entitlement/tier packaging | Resolved by MS-PROT-081 v1.4 | Accepted 15 September 2026: exact Workforce Scheduling/Timekeeping/Leave access contracts are commercially classified, including protected `MAINTAIN_WORKFORCE_SCHEDULING_TERMS`, `PLAN_WORKFORCE_SCHEDULE`, `COMMIT_OFFERED_WORK` and `REQUEST_WORKFORCE_LEAVE` purposes for BUSINESS + GROWTH with bounded no-entitlement observation/resolution/ending contracts; exact Commercial Entitlement identities and the complete catalogue remain under `MS-PROT-056-V17-DQ-001` |
| MS-PROT-081-DQ-021 | **RESOLVED** | Exact cross-Arrangement scheduling-overlap policy | Resolved by MS-PROT-081 v1.2 | Accepted 9 September 2026: symmetric exact Arrangement-pair policy, explicit one-off override, no universal allow/reject, and optional merchant-owned Membership-wide `MinimumInterCommitmentBuffer`; future material change requires governed amendment |
| MS-PROT-081-DQ-022 | **DEFERRED — INACTIVE** | Exact retrospective time/compensation attribution mechanism when historical workforce evidence had no Compensation Relationship affinity | Workforce Scheduling/Compensation correction design | Before retrospective compensation attribution is required; silent later binding remains prohibited |

Traceability rules:

1. The `MS-PROT-081-DQ-*` identifier MUST remain stable.
2. Unresolved questions remain inactive until deliberately promoted by product/implementation sequencing or a concrete jurisdiction requirement; resolved/partially resolved rows retain their accepted status and narrower remainder where applicable.
3. Promotion of a material semantic/architecture question follows `DESIGN-RULES.md`; implementation details follow `IMPLEMENTATION-RULES.md` where applicable.
4. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
5. No deferred item may weaken composite MS-PROT-081 through v1.4's separation of Merchant Membership, Workforce Scheduling Arrangement, Scheduling/Timekeeping/Leave, Appointment Scheduling and Workforce Compensation/Payroll authority, its exact evidence-affinity and cross-Arrangement overlap/buffer rules, source-owned workforce Notification boundaries or commercial-access ownership boundaries.
6. Git history preserves status transitions in this canonical register.

### 4.14 MS-PROT-082 Deferred Decision Catalogue

Composite MS-PROT-082 through v1.1 establishes the generic Jurisdiction & Regulatory Administration substrate and the first bounded jurisdiction/purpose/filing-scope vocabulary. v1.1 resolves DQ-001 and DQ-002. MS-PROT-084 v1.1, composed with MS-PROT-083, resolves the remaining Financial Health / Financial Intelligence portion of DQ-007. Physical rule-artifact representation, provider selection, source-monitoring automation and professional-escalation integration remain downstream.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-082-DQ-001 | **RESOLVED** | Initial commercial jurisdiction portfolio | Resolved by MS-PROT-082 v1.1 | Exactly England and Wales; vocabulary admission does not activate a Jurisdiction Pack or public support |
| MS-PROT-082-DQ-002 | **RESOLVED** | Initial Regulatory Purpose / FilingScopeReference subtype portfolio | Resolved by MS-PROT-082 v1.1 | Exactly three Regulatory Purposes and four FilingScopeReference subtypes; vocabulary admission does not activate production regulatory support |
| MS-PROT-082-DQ-003 | **DEFERRED — INACTIVE** | Exact persistence/serialization representation of Regulatory Rule Set Releases and evaluator bindings | JRA implementation detail | During conforming JRA implementation when a concrete representation is required; no universal rule DSL is authorised by this deferral |
| MS-PROT-082-DQ-004 | **DEFERRED — ACTIVE BEFORE PROVIDER-BACKED SUPPORT DECLARATION** | Initial external regulatory-provider portfolio | Provider/integration implementation | Before any corresponding `PROVIDER_BACKED` Jurisdiction Support Declaration is activated |
| MS-PROT-082-DQ-005 | **DEFERRED — ACTIVE BEFORE PRODUCTION REGULATORY SUPPORT** | Exact regulatory-source change-monitoring mechanism/frequency | Regulatory operations / source-assurance implementation | Before production support; every production pack must nevertheless have a concrete accepted assurance boundary |
| MS-PROT-082-DQ-006 | **DEFERRED — INACTIVE** | Whether GrandRue integrates directly with accountants/tax/legal specialists or supplies evidence for merchant-selected professionals | Future professional-escalation integration | When integrated professional escalation is deliberately productised; `ESCALATION_REQUIRED` remains valid without a professional marketplace |
| MS-PROT-082-DQ-007 | **RESOLVED** | Canonical Business Health & Financial Intelligence semantics | Resolved compositionally by composite MS-PROT-083 + MS-PROT-084 v1.1 | MS-PROT-083 owns analytical/Business Health semantics; MS-PROT-084 v1.1 supplies the missing Financial Operations/Financial Health source composition without transferring JRA or source-capability ownership |

Traceability rules:

1. The `MS-PROT-082-DQ-*` identifiers MUST remain stable.
2. `ACTIVE BEFORE ...` is a gate condition, not implementation authorisation and not automatic promotion ahead of the current implementation programme.
3. Material semantic/architecture promotion follows `DESIGN-RULES.md`; implementation detail follows `IMPLEMENTATION-RULES.md` where applicable.
4. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
5. No deferred item may weaken composite MS-PROT-082's purpose-qualified jurisdiction, source-backed rule, historical-affinity, support-dimension separation, provider-neutrality, explicit-unsupported-outcome, AI non-authority or capability-ownership invariants.
6. Git history preserves status transitions in this canonical register.

### 4.15 MS-PROT-083 Deferred Question Catalogue

Composite MS-PROT-083 through v1.8 establishes the generic analytical-measurement, Business Health, qualified-method and operational-decision-support substrate together with its accepted commercial-access classifications. v1.1 resolves the campaign slice of the initial Analytical Measure portfolio; v1.2 resolves the customer-return slice; v1.3 resolves the remaining initial general Business Intelligence portfolio. `MS-PROT-083-DQ-001` is resolved. MS-PROT-083 v1.4 resolves the Merchant Analytical Surface under DQ-009, v1.5 resolves the initial Business Health Indicator portfolio under DQ-002, v1.6 resolves the initial on-demand analytical report/export portfolio under DQ-010, and MS-PROT-084 v1.1 resolves complete Financial Health composition under DQ-015. MS-PROT-083 v1.7 adds Business Intelligence owner-qualified analytical-evaluation commercial classification: `USE_BUSINESS_ANALYTICS` for BUSINESS + GROWTH, `USE_CAMPAIGN_ANALYTICS` for GROWTH, and merchant analytical presentation with no independent Commercial Entitlement. MS-PROT-083 v1.8 adds explicit no-independent-entitlement classification for `business-intelligence/merchant-analytical-report-access@1` / `DELIVER_MERCHANT_ANALYTICAL_REPORT` and `business-intelligence/measure-observation-export-access@1` / `EXPORT_MEASURE_OBSERVATIONS`, preserving the v1.6 externalisation requirements and the v1.7 evaluation permissions. Neither v1.7 nor v1.8 reopens, closes or otherwise changes any `MS-PROT-083-DQ-*` state. Analytical persistence, methods, recommendation ranking, external context, lifecycle policy, learning, benchmarking and currency normalization remain downstream. MS-PROT-085 v1.0 resolves the registered Merchant Attention integration architecture.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-083-DQ-001 | **RESOLVED** | Initial Analytical Measure portfolio | Resolved by composite MS-PROT-083 through v1.3 | v1.1 campaign slice + v1.2 customer-return slice + v1.3 initial general BI portfolio; future materially different measures require fresh Feature Admission |
| MS-PROT-083-DQ-002 | **RESOLVED** | Initial Business Health Indicator portfolio | Resolved by MS-PROT-083 v1.5 | Exactly one directly MS-PROT-083-owned initial non-financial family, `business-health/inventory-claim-integrity@1`, plus composition of the five accepted MS-PROT-084 Financial Health assessments by reference; no universal Business Health score or unsupported threshold portfolio |
| MS-PROT-083-DQ-003 | **DEFERRED — INACTIVE** | Analytical persistence / time-series architecture | BI implementation architecture | When request-scoped evaluation is insufficient and physical analytical persistence is justified |
| MS-PROT-083-DQ-004 | **DEFERRED — INACTIVE** | Initial Analytical Method portfolio | BI/statistical-method design | Before any nontrivial production probabilistic inference/forecasting method is enabled |
| MS-PROT-083-DQ-005 | **DEFERRED — INACTIVE** | Method Qualification operational process | BI assurance / operational governance | Before the first production Analytical Method Qualification is granted |
| MS-PROT-083-DQ-006 | **DEFERRED — INACTIVE** | Recommendation prioritisation | BI product/decision-support design | Before multiple concurrent Business Recommendations require production ranking/materiality ordering |
| MS-PROT-083-DQ-007 | **DEFERRED — INACTIVE** | External context portfolio | BI provider/source design | Before weather, public-holiday, local-event, economic or other external contextual evidence participates in production BI |
| MS-PROT-083-DQ-008 | **RESOLVED** | Merchant Attention integration | Resolved by MS-PROT-085 v1.0 | Accepted 8 September 2026; only registered, sufficiently covered and premise-current analytical contributions may enter Merchant Attention, without making a Candidate Action executable authority |
| MS-PROT-083-DQ-009 | **RESOLVED** | Merchant analytical surface | Resolved by MS-PROT-083 v1.4 §27 | Accepted 14 September 2026; bounded Analytics WORKSPACE contribution, request-scoped merchant analytical projection, presentation fidelity, progressive disclosure and Mandatory Honesty Envelope; no implementation activation and no closure of DQ-006 |
| MS-PROT-083-DQ-010 | **RESOLVED — INITIAL ON-DEMAND REPORT PORTFOLIO** | Reports and exports | Resolved by MS-PROT-083 v1.6 | Exactly one initial `business-intelligence / merchant-analytical-report@1` contract with `ON_DEMAND_SNAPSHOT` generation and CSV/PDF encodings; scheduled/digest/professional/statutory exports remain outside the initial portfolio and require fresh admission |
| MS-PROT-083-DQ-011 | **DEFERRED — INACTIVE** | Analytical retention/lifecycle portfolio | Data-protection/legal-policy implementation under composite MS-PROT-053 | Before concrete retention/minimisation rules for analytical artifacts are implemented |
| MS-PROT-083-DQ-012 | **DEFERRED — INACTIVE** | Adaptive learning | Future AI/BI learning architecture | Before merchant-specific adaptation, cross-merchant learning, training or distillation uses production analytical/outcome evidence |
| MS-PROT-083-DQ-013 | **DEFERRED — INACTIVE** | Cross-merchant benchmarking | Separate material BI/data-protection authority | Before any cross-merchant benchmark/cohort comparison is enabled |
| MS-PROT-083-DQ-014 | **DEFERRED — INACTIVE** | Currency normalisation | Financial/BI semantic and provider design | Before a production analytical measure aggregates values across currencies; remains materially affined with MS-PROT-084-DQ-011 |
| MS-PROT-083-DQ-015 | **RESOLVED** | Complete Financial Health composition | Resolved by MS-PROT-084 v1.1 | Financial Health is now a constrained MS-PROT-083 analytical specialization over owner-qualified Financial Operations, Payment/source and Regulatory financial facts; statutory accounting and profitability remain outside the initial portfolio |

Traceability rules:

1. The `MS-PROT-083-DQ-*` identifiers MUST remain stable.
2. Unresolved questions remain inactive until deliberately promoted by product/implementation sequencing or their explicit revisit condition; resolved rows retain their accepted status unless a later accepted authority explicitly reopens them.
3. Promotion of material semantic/architecture questions follows `DESIGN-RULES.md`; implementation details follow `IMPLEMENTATION-RULES.md`.
4. Resolution MUST record the resolving accepted authority or implementation evidence, as applicable.
5. No deferred item may weaken composite MS-PROT-083's source-ownership, exact-definition/evaluation-affinity, coverage honesty, method-qualification, recommendation non-authority, AI claim-grounding, data-protection, Business/Operational Health separation or no-autonomous-mutation invariants.
6. MS-PROT-083 v1.7-v1.8 commercial classifications do not convert any deferred analytical expansion, unavailable analytical material, missing Actor Authorisation or failed externalisation predicate into an available feature.
7. Git history preserves status transitions in this canonical register.

### 4.15A MS-PROT-084 Deferred Decision Catalogue

MS-PROT-084 v1.1 is the complete accepted Financial Operations / Financial Evidence / Financial Health semantic composition. MS-PROT-084 v1.2 composes with it only within commercial-access classification: it protects establishment of new or materially expanded Financial-Operations-owned truth for BUSINESS + GROWTH and explicitly classifies bounded preparation, existing-record observation and pure existing-record resolution without an independent Commercial Entitlement. v1.2 does not reopen, close or otherwise change any `MS-PROT-084-DQ-*` state. The subject-wide deferred-decision sweep in v1.1 resolved the initial operating-cost, Financial Health, evidence-classification, financial-document-consumption, retention-ownership, statutory-accounting-boundary and initial financing-calculation questions while deliberately keeping provider/vendor, supplier/invoice, outgoing-payment, accounting-depth, profitability, currency-normalisation and professional-accounting expansion inactive.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-084-DQ-001 | **RESOLVED** | Initial Operating Cost Portfolio | Resolved by MS-PROT-084 v1.1 | Exactly `ONE_OFF`, `RECURRING_FIXED`, `RECURRING_VARIABLE`; no accounting category taxonomy |
| MS-PROT-084-DQ-002 | **RESOLVED** | Initial Financial Health Portfolio | Resolved by MS-PROT-084 v1.1 | Exactly five deterministic initial Financial Health indicator families; no universal score or unsupported profitability claim |
| MS-PROT-084-DQ-003 | **DEFERRED — INACTIVE** | Financial Account Provider Portfolio | Provider/integration implementation/product admission | Before concrete financial-account provider activation |
| MS-PROT-084-DQ-004 | **RESOLVED** | Financial Evidence Classification Portfolio | Resolved by MS-PROT-084 v1.1 | Exactly seven evidence-form families; evidence form does not establish economic meaning |
| MS-PROT-084-DQ-005 | **DEFERRED — INACTIVE** | Counterparty / Supplier Authority | Future supplier/procurement feature admission | Only if richer supplier/counterparty operations pass Feature Admission; bounded Counterparty Reference is sufficient initially |
| MS-PROT-084-DQ-006 | **RESOLVED** | Invoice Authority | Resolved by MS-PROT-096 v1.0 | Outbound merchant-issued customer Invoice truth is Invoicing-owned; Payment retains Payment Obligation/Amount Due/payment truth; Financial Operations remains residual and does not duplicate Invoice-bound customer obligation truth |
| MS-PROT-084-DQ-007 | **DEFERRED — INACTIVE** | Outgoing Merchant Payment Execution | Payment/banking execution architecture | Before GrandRue initiates merchant money movement |
| MS-PROT-084-DQ-008 | **DEFERRED — INACTIVE** | Inventory Financial Valuation | Accounting-depth feature admission | Before Inventory becomes authoritative financial-accounting value |
| MS-PROT-084-DQ-009 | **DEFERRED — INACTIVE** | Capital Asset / Depreciation | Accounting-depth feature admission | Before depreciation or capital-asset accounting is introduced; Capital Acquisition alone does not establish those semantics |
| MS-PROT-084-DQ-010 | **DEFERRED — INACTIVE** | Profitability Portfolio | Financial/accounting semantic authority | Before broad profit claims are made; initial Financial Health explicitly excludes profit/accounting profit |
| MS-PROT-084-DQ-011 | **DEFERRED — INACTIVE** | Currency Normalisation | Financial/BI conversion authority | Before authoritative cross-currency conversion or aggregation; currencies remain partitioned |
| MS-PROT-084-DQ-012 | **DEFERRED — INACTIVE** | Professional Accounting Integration | Provider/professional integration admission | Before concrete accountant/accounting-provider integration |
| MS-PROT-084-DQ-013 | **RESOLVED** | Financial Document Extraction | Resolved by MS-PROT-084 v1.1 composed with MS-PROT-092 | MS-PROT-092 owns DocumentIntake/ExtractionCandidate/EvidenceHandoff; Financial Operations owns the exact consuming/admission contract only |
| MS-PROT-084-DQ-014 | **RESOLVED BY OWNERSHIP** | Financial Evidence Retention | Resolved by MS-PROT-084 v1.1 composed with MS-PROT-053/JRA | Financial Operations consumes purpose/lifecycle/legal-retention authority; it does not invent a universal numeric retention period |
| MS-PROT-084-DQ-015 | **RESOLVED** | Statutory Accounting Boundary | Resolved by MS-PROT-084 v1.1 | Statutory/general-ledger accounting is explicitly outside the current authority and requires fresh Feature Admission/material authority |
| MS-PROT-084-DQ-016 | **RESOLVED — INITIAL PORTFOLIO EMPTY** | Financing Calculation Contracts | Resolved by MS-PROT-084 v1.1 | GrandRue may represent externally supplied/validated schedules; authoritative GrandRue APR/interest/amortisation/etc. calculation is not currently admitted |

Traceability rules:

1. The `MS-PROT-084-DQ-*` identifiers MUST remain stable.
2. Resolution of an initial portfolio or ownership boundary does not authorise a materially broader financial/accounting feature; fresh Feature Admission and DESIGN-RULES apply.
3. Deferred items remain inactive until deliberately promoted by product/implementation sequencing or their exact revisit condition.
4. No deferred item may weaken MS-PROT-084 v1.1's residual financial ownership, no-shadow-ledger, canonical Money/currency, economic-exposure-overlap, mixed-use-account, evidence-admission, provider-neutrality, AI non-authority, coverage-honesty or anti-ERP boundaries.
5. `MS-PROT-083-DQ-014` and `MS-PROT-084-DQ-011` remain separate but materially affined currency-normalisation questions; neither is activated because the initial portfolio remains currency-partitioned.
6. MS-PROT-084 v1.2's commercial classification does not convert any deferred Financial Operations expansion into a commercially available feature.
7. Git history preserves status transitions in this canonical register.

### 4.16 MS-PROT-085 Deferred Question Catalogue

MS-PROT-085 v1.0 establishes the bounded Merchant Attention model. Accepted v1.1 resolves initial portfolio selection with exactly `enquiry / initial-submission-review@1`. Accepted v1.2 later adds exactly `customer-communication / human-response-required@1` as a bounded Customer Communication human-response handling family without reopening DQ-001, creating a generic queue, or activating implementation.

Accepted MS-PROT-085 v1.3 resolves only the initial-Enquiry-review commercial classification: qualified post-commit progression requires no Commercial Entitlement; merchant observation, acknowledgement and recording review require the exact Enquiry observation permission without an independent Attention entitlement. It preserves DQ-001's resolved initial-family selection and does not change the Customer Communication family or activate implementation.

Accepted MS-PROT-085 v1.4 resolves the remaining supporting commercial classification for exactly `customer-communication / human-response-required@1`: authoritative post-assessment occurrence establishment/recovery and Attention coordination require no independent Commercial Entitlement, while merchant observation and human response remain gated by exact independently valid Customer Communication access and actor authority. It does not make Customer Communication FREE, mint a final entitlement identity or replace exact bindings with a BUSINESS tier check. With v1.3 it completes the commercial classification of the currently accepted two-family Merchant Attention portfolio without reopening `MS-PROT-085-DQ-001` or activating implementation. The outstanding catalogue work remains under `MS-PROT-056-V17-DQ-001` in Section 4.19.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-085-DQ-001 | **RESOLVED** | Initial Attention Contract portfolio | Resolved by MS-PROT-085 v1.1 | Accepted 8 September 2026; exactly `enquiry / initial-submission-review@1` selected; no assignment, snooze, response obligation or historical backfill; v1.2 adds a separately accepted Customer Communication human-response family without reopening the initial-portfolio resolution |

Traceability rules:

1. The `MS-PROT-085-DQ-*` identifier MUST remain stable.
2. `ACTIVE BEFORE PRODUCTION MERCHANT ATTENTION` is a gate condition, not implementation authorisation and not automatic promotion ahead of the current implementation programme.
3. Material semantic/architecture promotion follows `DESIGN-RULES.md`; implementation detail follows `IMPLEMENTATION-RULES.md`.
4. Resolution MUST establish the exact initial source families and owner-qualified Attention Contracts without creating a universal workflow, priority or task platform.
5. No portfolio decision may weaken MS-PROT-085's source-ownership, stable-identity, recurrence, handling/source-resolution separation, current-authority, Candidate-Action, coverage-honesty, AI non-authority or anti-ERP invariants.
6. Git history preserves status transitions in this canonical register.

### 4.17 MS-PROT-086 Deferred Question Catalogue

MS-PROT-086 v1.0 establishes the bounded Customer Messaging, Conversation Continuity and Customer-Service Handoff model. Accepted v1.1 resolves the first production channel portfolio with exactly Merchant Website Messaging plus Conversation-Bound Email. Accepted v1.2 resolves browser access/resume/view/continue semantics for registered customers and guests while leaving exact guest credential representation to ADR-014-DQ-011. Accepted v1.3 resolves the initial Customer-Service Response Contract portfolio with seven fact-first response families and a paired MS-PROT-085 v1.2 human-response Attention path. MS-PROT-053 v1.3 separately resolves the concrete Enquiry/Customer Communication retention prerequisite. Accepted v1.4 adds Customer Communication commercial-access classification only: it protects `CONDUCT_CUSTOMER_COMMUNICATION` and `AUTOMATE_ROUTINE_CUSTOMER_SERVICE` for BUSINESS + GROWTH through exact owner-qualified access contracts and classifies bounded non-committing human preparation, existing-Conversation observation and committed-communication progression with no independent Commercial Entitlement. It changes no `MS-PROT-086-DQ-*` state, does not resolve ADR-014-DQ-011, does not activate Message attachments and does not activate Customer Communication implementation.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-086-DQ-001 | **RESOLVED** | Initial Customer Messaging Channel portfolio | Resolved by MS-PROT-086 v1.1 | Accepted 8 September 2026; exactly Merchant Website Messaging + Conversation-Bound Email selected through `WEBSITE_MESSAGE_CREATE_V1` and `EMAIL_REPLY_CONTINUE_V1`; authenticated CustomerContext participant identity is preserved for registered signed-in customers; guest reply email remains unverified transport; no generic inbound mailbox, SMS/social channel, attachment capability, merchant activation or implementation is authorised |
| MS-PROT-086-DQ-002 | **RESOLVED** | Initial Customer-Service Response Contract portfolio | Resolved by MS-PROT-086 v1.3 | Accepted 9 September 2026; exactly seven fact-first response families selected for public merchant information, public offering information, published policy information, current Scheduling availability, related Booking/Appointment information, related Order/Fulfilment/Shipment information and related Payment/Refund information; automatic substantive response requires complete material-request coverage, exact owner-qualified evidence/access, deterministic validation and the paired MS-PROT-085 v1.2 human-response Attention path; AI confidence, generic FAQ/RAG material and Conversation access do not create authority |
| MS-PROT-086-DQ-003 | **RESOLVED** | Production Guest Conversation Access mechanism | Resolved by MS-PROT-086 v1.2 | Accepted 8 September 2026; registered customers use authenticated CustomerContext participant authority for bounded merchant-scoped discovery/view/continuation, while guests use a revocable purpose-bound one-Conversation Guest Conversation Access Grant with VIEW and APPEND_TEXT_MESSAGE only; contact-value equality, Conversation identifiers and email routes do not grant browser access; exact credential representation remains separately gated by ADR-014-DQ-011 |
| MS-PROT-086-DQ-004 | **DEFERRED — ACTIVE BEFORE PRODUCTION MESSAGE ATTACHMENTS** | Initial Conversation Attachment portfolio | Customer Communication composed with Media, security, abuse protection, data protection and provider authority | Before a production Conversation Message can include an attachment |

Traceability rules:

1. The `MS-PROT-086-DQ-*` identifiers MUST remain stable.
2. Each `ACTIVE BEFORE ...` status is a gate condition, not implementation authorisation and not automatic promotion ahead of the current implementation programme.
3. Material semantic/architecture promotion follows `DESIGN-RULES.md`; implementation detail follows `IMPLEMENTATION-RULES.md`.
4. Resolution MUST preserve MS-PROT-086's Conversation/Enquiry/Notification/Attention/source separation, exact Merchant Scope, strong continuity evidence, current access, immutable/idempotent Message, delivery distinction, response-contract, AI non-authority, data-minimisation and anti-contact-centre invariants.
5. The v1.3 resolution of `MS-PROT-086-DQ-002` fixes the seven initial response families, complete qualitative coverage requirement, fact-first owner-qualified evidence boundary, deterministic validation and paired human-handoff requirement; AI confidence remains non-authoritative.
6. The v1.2 resolution of `MS-PROT-086-DQ-003` preserves the distinction between authenticated CustomerContext participant identity and separately authorised guest Conversation access; it does not resolve ADR-014-DQ-011's physical credential representation.
7. MS-PROT-086 v1.4 supplies commercial classification only and changes no semantic DQ state.
8. Git history preserves status transitions in this canonical register.

### 4.18 MS-PROT-087 Deferred Question Catalogue

Composite MS-PROT-087 through v1.4 establishes bounded Merchant Marketing Campaign, Audience Definition, recipient eligibility, approval, occurrence, suppression and outcome-evidence semantics together with the accepted Marketing commercial-access classification. Accepted v1.1 resolves the initial Campaign Purpose and Outreach portfolio. Accepted v1.2 resolves the initial Audience Definition and Attribute portfolio. Accepted v1.3 resolves permission/contact policy and bounded automation together as `MKT-GRP-01`. Accepted MS-PROT-083 v1.1 then resolves the Campaign Measure and Attribution portfolio as `MKT-GRP-02`. Accepted MS-PROT-087 v1.4 classifies the current bounded Marketing Campaign service commercially without changing any Marketing deferred-decision state; paid advertising/external spend remains downstream under `MS-PROT-087-DQ-006`.

| ID | Status | Deferred question | Classification / future owner | Revisit condition |
|---|---|---|---|---|
| MS-PROT-087-DQ-001 | **RESOLVED** | Initial Campaign Purpose and Outreach portfolio | Resolved by MS-PROT-087 v1.1 | Accepted 9 September 2026; exactly four purpose families — `marketing/merchant-news-awareness@1`, `marketing/offering-awareness@1`, `marketing/customer-appreciation@1`, `marketing/customer-reengagement@1` — and exactly `WEBSITE_ANNOUNCEMENT_V1` + `DIRECT_EMAIL_MARKETING_V1` selected; standalone website Announcements remain MS-PROT-046-owned; initial direct marketing is relationship-based rather than prospecting-based; implementation and merchant activation remain separately governed |
| MS-PROT-087-DQ-002 | **RESOLVED** | Initial Audience Definition and Attribute portfolio | Resolved by MS-PROT-087 v1.2 | Accepted 9 September 2026; exactly `marketing-audience/existing-customer@1`, `marketing-audience/recent-customer-relationship@1` and `marketing-audience/previous-customer-reengagement@1` selected; initial recency activity is limited to CustomerContext-associated Order, Booking and Appointment commitments; cancellation/release does not erase historical relationship activity; missing/incomplete negative evidence yields `UNRESOLVED`; Payment, Enquiry and Conversation do not independently become audience activity; CRM lifecycle labels, spend/value scoring, product-history segmentation, sensitive/proxy targeting and AI-selected audiences remain excluded; DQ-003 remains a separate hard gate before direct marketing externalisation |
| MS-PROT-087-DQ-003 | **RESOLVED** | Marketing Permission, Suppression and Contact Policy portfolio | Resolved by MS-PROT-087 v1.3 / `MKT-GRP-01` | Accepted 9 September 2026; direct email requires a current jurisdiction-qualified `DirectMarketingContactPolicyDetermination`; durable endpoint/CustomerContext Marketing suppression dominates permission; unsubscribe is immediate and account-free; initial cross-campaign contact pressure is 1/24h, 2/7d and 6/30d per MerchantScope × normalised EMAIL endpoint; every mutable condition is revalidated before externalisation |
| MS-PROT-087-DQ-004 | **RESOLVED** | Automated Campaign Trigger and Recurrence portfolio | Resolved by MS-PROT-087 v1.3 / `MKT-GRP-01` | Accepted 9 September 2026; initial automation binds one exact Campaign Revision for at most 12 calendar months, supports exactly `SCHEDULED_SINGLE_OCCURRENCE_V1` and `PERIODIC_AUDIENCE_REEVALUATION_V1`, permits weekly/monthly local recurrence only, applies `ONCE_PER_AUTOMATION_CONTRACT`, forbids catch-up/replay and source-event triggers, and requires current audience/permission/suppression/contact/source/provider revalidation per occurrence |
| MS-PROT-087-DQ-005 | **RESOLVED** | Campaign Measure and Attribution portfolio | Resolved by MS-PROT-083 v1.1 / `MKT-GRP-02` | Accepted 9 September 2026; initial portfolio contains exactly `campaign / recipient-eligibility-count@1`, `campaign / direct-email-responsibility-count@1`, `campaign / direct-email-delivery-evidence-count@1`, `campaign / campaign-linked-suppression-count@1` and `campaign / website-announcement-publication-count@1`, with exactly `DIRECT_EXECUTION_TRACE_V1`; open/click/conversion/revenue/retention/ROI and downstream commercial-causation claims remain prohibited absent fresh feature admission and applicable analytical authority |
| MS-PROT-087-DQ-006 | **DEFERRED — ACTIVE BEFORE PAID ADVERTISING OR EXTERNAL CAMPAIGN SPEND** | Paid Advertising, Spend and External Optimisation authority | Future marketing/financial/external-provider design | Before GrandRue commits advertising spend, participates in auctions, controls external advertising budgets or performs external ad optimisation |

Traceability rules:

1. The `MS-PROT-087-DQ-*` identifiers MUST remain stable.
2. Each `ACTIVE BEFORE ...` status is a gate condition, not implementation authorisation and not automatic promotion ahead of the current implementation programme.
3. Material semantic/architecture promotion follows `DESIGN-RULES.md`; implementation detail follows `IMPLEMENTATION-RULES.md`.
4. Resolution MUST preserve MS-PROT-087's Campaign/CustomerContext/Publication/Notification/Conversation/source/analytics separation, exact Merchant Scope, current eligibility, permission/suppression distinction, approval affinity, provider uncertainty, AI non-authority, data minimisation and anti-marketing-suite invariants.
5. MS-PROT-087 v1.1 fixes the initial purpose/outreach portfolio and v1.2 fixes the initial Audience Definition/attribute portfolio. DQ-003 and DQ-004 are resolved by MS-PROT-087 v1.3 / `MKT-GRP-01`, and DQ-005 is resolved by MS-PROT-083 v1.1 / `MKT-GRP-02`; paid advertising/external spend remains gated by DQ-006.
6. `MS-PROT-087-DQ-006` MUST NOT be resolved by ordinary Campaign implementation; paid external effects require explicit financial/provider authority.
7. MS-PROT-087 v1.4 supplies Marketing commercial classification only, changes no `MS-PROT-087-DQ-*` state, mints no final Commercial Entitlement identity and leaves `MS-PROT-056-V17-DQ-001` OPEN.
8. Git history preserves status transitions in this canonical register.

Numerical thresholds, concrete rate-limiting algorithms and other security mechanics not listed here remain implementation/security-policy choices unless later evidence makes them material architecture.

A future-scope item becomes active only when deliberately promoted through the governed design lifecycle, activated by an accepted sequencing authority, or reaches an explicit accepted `ACTIVE BEFORE ...` gate condition.

---

### 4.19 MS-PROT-056 v1.7 Commercial Allocation Follow-on Decisions

MS-PROT-056 v1.7, accepted 14 September 2026, establishes commercial allocation policy only. Its §12 retains the following unresolved work; none of these records authorises executable grants, implementation activation or a programme-readiness change.

MS-PROT-056 v1.8, accepted 14 September 2026, adds the customer-return and campaign measure allocations and supporting presentation-access boundary. Its §12 left all four following decisions OPEN at acceptance; neither Commercial allocation amendment activates the executable catalogue or C3.

MS-PROT-056 v1.9, accepted 14 September 2026, subsequently resolves DQ-002 only for catalogue-publication and temporal-selection policy through §§8–13 and §17. This scoped resolution does not establish an initial production catalogue. DQ-001, DQ-003 and DQ-004 remain OPEN; implementation activation and C3 completion are not authorised.

MS-PROT-043 v1.5, accepted 14 September 2026, narrows DQ-001 through three Enquiry-owned access-point and purpose classifications: `enquiry/general-submission-access@1` and `enquiry/opportunity-submission-access@1` protect `ORIGINATE_ENQUIRY`; `enquiry/merchant-observation-access@1` protects `OBSERVE_ENQUIRY`. Its §§7–8 and §12 leave exact Commercial Entitlement identities, bindings, supporting-contract closure and the complete three-tier manifest unresolved. Other subject-specific Enquiry access contracts are not supplied. DQ-001 remains OPEN; no catalogue admission, production activation or C3 completion is established by this bounded progress.

MS-PROT-085 v1.3, accepted 14 September 2026, resolves the supporting commercial classification for exactly `enquiry/initial-submission-review@1`. Qualified post-commit progression requires no Commercial Entitlement; observation, acknowledgement and recording review require `OBSERVE_ENQUIRY` through `enquiry/merchant-observation-access@1`, with no independent Attention entitlement. Exact Enquiry bindings, other supporting classifications and the complete three-tier manifest remain outstanding. DQ-001 remains OPEN; C3 activation and implementation-node readiness are unchanged.

MS-PROT-085 v1.4, accepted 16 September 2026, resolves the remaining supporting commercial classification for exactly `customer-communication/human-response-required@1`. Authoritative post-assessment occurrence establishment/recovery and the Attention coordination layer require no independent Commercial Entitlement; merchant observation and `HUMAN_RESPONSE_ACCEPTED` remain gated by independently valid exact Customer Communication access and actor requirements, while `HANDLED_OUTSIDE_MAIN_STREET_RECORDED` retains the same source/actor boundary. v1.4 does not make Customer Communication FREE, mint a final entitlement identity or replace exact Customer Communication bindings with a BUSINESS tier check. With v1.3 it completes the commercial classification of the currently accepted two-family Merchant Attention portfolio. DQ-001 remains OPEN pending Customer Communication exact commercial contracts/bindings, remaining owner/supporting classifications, concrete entitlement identities and the complete manifest; no implementation activation or C3 completion is authorised.

MS-PROT-086 v1.4, accepted 16 September 2026, further narrows DQ-001 through two exact Customer Communication protected access contracts: `customer-communication/message-participation-access@1` / `CONDUCT_CUSTOMER_COMMUNICATION` → BUSINESS + GROWTH, and `customer-communication/automated-response-access@1` / `AUTOMATE_ROUTINE_CUSTOMER_SERVICE` → BUSINESS + GROWTH. It also classifies `customer-communication/non-committing-human-preparation-access@1`, `customer-communication/existing-conversation-observation-access@1` and `customer-communication/committed-communication-progression-access@1` with NO INDEPENDENT COMMERCIAL ENTITLEMENT. No final `CommercialEntitlementIdentity` is minted; exact Commercial-owned bindings and the complete manifest remain outstanding. DQ-001 remains OPEN; no implementation activation or C3 completion is authorised.

MS-PROT-087 v1.4, accepted 16 September 2026, further narrows DQ-001 through exactly one protected Marketing service contract: `marketing/campaign-service-access@1` / `CONDUCT_MARKETING_CAMPAIGNS` → GROWTH as `PLATFORM_SERVICE_ACCESS`. It also classifies `marketing/non-committing-preparation-access@1`, `marketing/existing-marketing-state-observation-access@1` and `marketing/campaign-restriction-access@1` with NO INDEPENDENT COMMERCIAL ENTITLEMENT. It preserves current commercial revalidation before protected new use/externalisation and deliberately creates no generic committed-Campaign progression exemption. No final `CommercialEntitlementIdentity` is minted; exact Commercial-owned binding identity and the complete manifest remain outstanding. DQ-001 remains OPEN; `MS-PROT-087-DQ-006` is unchanged and no implementation activation or C3 completion is authorised.

MS-PROT-046 v1.4, accepted 14 September 2026, resolves two Publication-side supporting classifications through `publication/public-source-observation-access@1` and `publication/opportunity-enquiry-participation-access@1`, without independent Publication entitlements for their bounded source responsibilities. Enquiry execution, delivery and administration retain their independent requirements. DQ-001 remains OPEN; exact grants, remaining supporting contracts and the complete manifest are not established, and C3 activation remains gated.

MS-PROT-043 v1.6, accepted 14 September 2026, resolves the bounded public Enquiry entry and non-committing preparation classification through `enquiry/public-interaction-preparation-access@1`, with no independent preparation entitlement. Submission still requires its exact v1.5 origination permission; source access and delivery remain separate. DQ-001 remains OPEN pending remaining supporting classifications, exact grants and the complete manifest. No implementation activation or C3 completion is authorised.

Paired MS-PROT-088 v1.1 and MS-PROT-036 v1.3, accepted 14 September 2026, narrow DQ-001 through `merchant-brand-infrastructure/platform-website-namespace-use@1` / `USE_PLATFORM_WEBSITE_NAMESPACE`, `merchant-brand-infrastructure/custom-website-domain-use@1` / `USE_MERCHANT_CONTROLLED_WEBSITE_DOMAIN`, and common `storefront/website-delivery-access@1` / `SERVE_CUSTOMER_WEBSITE`. Delivery requires both the common purpose and the actual binding's namespace purpose; MS-PROT-094 presentation/composition and shared-rendering authority remains unchanged. Initial composition-publication access, namespace provisioning, cutover operations, remaining supporting classifications, exact grants and the complete manifest are not established. DQ-001 remains OPEN; prices and allowances remain under DQ-003. No catalogue publication, implementation activation, C3 completion or programme promotion is authorised.

MS-PROT-036 v1.4, accepted 14 September 2026, supplies the bounded composition-publication lifecycle and the exact `storefront/composition-preparation-access@1`, `storefront/composition-publication-access@1`, `storefront/platform-presentation-maintenance-access@1` and `storefront/composition-withdrawal-access@1` classifications. Merchant publication requires `PUBLISH_STOREFRONT_COMPOSITION` in FREE and explicit paid supersets; the other three contracts require no independent Commercial Entitlement within their defined scopes. Storefront Publication Selection and the mixed service/product business-evolution handoff preserve independently owned Configuration, source, dashboard, namespace and delivery authority. DQ-001 remains OPEN pending remaining supporting classifications, exact bindings and the complete catalogue; namespace provisioning/cutover, production retention qualification and implementation proof are not supplied by this acceptance. No production activation, C3 completion or programme promotion is authorised.

MS-PROT-088 v1.2, accepted 14 September 2026, supplies the local binding-selection and disconnection lifecycle through `merchant-brand-infrastructure/inspect-website-binding@1`, `merchant-brand-infrastructure/select-website-binding@1` and `merchant-brand-infrastructure/disconnect-website-binding@1`. §§3–9 establish immutable Website Binding Selection, independent namespace-family boundaries, current Controller authority, atomic routing changes and retry/concurrency protection, including a new disconnection revision when already unbound. Selection consumes the applicable existing namespace-use commercial purpose without an independent setup entitlement; inspection and disconnection require no Commercial Entitlement within their bounded scopes. §§10–14 preserve provider/delivery separation and leave platform allocation/release, external DNS/certificate execution and continuation, production retention qualification, remaining supporting classifications, exact bindings and the complete catalogue outstanding. DQ-001 remains OPEN; no production activation, C3 completion or programme promotion is authorised.

MS-PROT-088 v1.3, accepted 14 September 2026, supplies the initial website-qualified platform allocation, naming and retirement lifecycle through `merchant-brand-infrastructure/inspect-platform-website-namespace@1`, `merchant-brand-infrastructure/allocate-platform-website-namespace@1` and `merchant-brand-infrastructure/retire-platform-website-namespace@1`. §§3–12 establish at most one ASSIGNED Platform Website Namespace Assignment per Merchant Scope, readable wording with a permanently non-reused discriminator, current Controller approval, immutable allocation evidence, terminal retirement, binding-safe concurrency and replay without revival. Allocation consumes the existing platform namespace-use purpose without an independent setup entitlement; bounded inspection and retirement require no Commercial Entitlement. Disconnection retains assignment, and subscription or account changes do not manufacture retirement. §§13–15 retain lifecycle/disposition and recovery qualification, production parent-domain selection, external DNS/certificate provisioning, exact bindings and the complete catalogue. Additional namespace purposes, seamless migrations and privileged post-closure cleanup remain separately governed admission boundaries. DQ-001 remains OPEN; no production activation, C3 completion or programme promotion is authorised.

MS-PROT-088 v1.4, accepted 15 September 2026, supplies bounded Website Connection Request and deferred local-completion authority through `merchant-brand-infrastructure/inspect-website-connection@1`, `merchant-brand-infrastructure/request-website-connection@1`, `merchant-brand-infrastructure/cancel-website-connection@1` and `merchant-brand-infrastructure/complete-website-connection@1`, with the registered `merchant-brand-infrastructure/website-connection-completion@1` background contract. §§3–12 establish immutable approved content, one pending request per scope/family, current Controller and commercial revalidation, terminal cancellation/supersession/stopping, atomic local completion and retry recovery without revival. Inspection and cancellation require no Commercial Entitlement; request and completion consume the applicable existing namespace-use purpose without an independent preparation/completion entitlement. §7 supplies intended-binding preparation authority only for composition with separately accepted external contracts. §§13–16 retain external DNS/certificate execution, maintenance, cleanup, reconciliation, concrete data-lifecycle qualification, exact grants and the complete manifest. DQ-001 remains OPEN; no production activation, C3 completion or programme promotion is authorised.

MS-PROT-088 v1.5, accepted 15 September 2026, supplies bounded website DNS/certificate execution, maintenance and recovery through the exact application contracts in §4, fulfilment roles/routing in §6, ReconciliationContract in §14 and BackgroundWorkContracts in §16. §§3–16 establish immutable Website Infrastructure Plan content, conditional resource changes, preparation/local-selection/DNS-cutover ordering, current execution authority, safe residual cleanup, durable external-effect identity and historical reconciliation without provider-driven routing activation. §5 uses existing namespace-use purposes for preparation, cutover and maintenance without independent DNS/certificate entitlements; inspection, cancellation, evidence receipt, historical reconciliation and qualified cleanup require no Commercial Entitlement. §§2, 17–18 retain initial domain-control proof mechanisms, concrete provider/security/lifecycle qualification, executable support and production verification. DQ-001 remains OPEN pending exact bindings and the complete catalogue; no vendor selection, production activation, catalogue publication or C3 completion is authorised.

MS-PROT-046 v1.5, accepted 15 September 2026, supplies four exact Publication access contracts: `publication/merchant-preparation-access@1`, `publication/merchant-observation-access@1`, `publication/merchant-authoring-access@1` and `publication/merchant-withdrawal-access@1`. §§1–7 classify bounded preparation, authorised observation and existing withdrawal without independent paid requirements; initial draft establishment, material revision, publication and explicit republication require `AUTHOR_AND_PUBLISH_INFORMATION` through the exact authoring access target, allocated to FREE with explicit BUSINESS/GROWTH inclusion. §§8–12 preserve existing lifecycle/concurrency/history, current-authority checks, receipt recovery without replay, and independently governed Exposure, delivery, Enquiry, Marketing, Notification and provider responsibilities. Exact entitlement identity and the complete manifest remain outstanding. DQ-001 remains OPEN; existing Publication deferred decisions are unchanged, and no production activation or C3 completion is authorised.

MS-PROT-081 v1.4, accepted 15 September 2026, resolves `MS-PROT-081-DQ-020` for owner-qualified Workforce Scheduling, Timekeeping and Leave commercial access. It classifies protected `MAINTAIN_WORKFORCE_SCHEDULING_TERMS`, `PLAN_WORKFORCE_SCHEDULE`, `COMMIT_OFFERED_WORK` and `REQUEST_WORKFORCE_LEAVE` purposes for BUSINESS + GROWTH and bounded no-entitlement observation/resolution/ending contracts. Concrete `CommercialEntitlementIdentity` values and the complete standard catalogue remain outstanding under DQ-001; implementation activation remains NONE.

MS-PROT-080 v1.4, accepted 15 September 2026, resolves `MS-PROT-080-V11-DQ-016` for owner-qualified Workforce Compensation and Payroll commercial access. It classifies protected `MAINTAIN_WORKFORCE_COMPENSATION_TERMS` and `ADMINISTER_WORKFORCE_COMPENSATION` purposes for BUSINESS + GROWTH and bounded no-entitlement preparation/observation/residual-resolution contracts. Concrete `CommercialEntitlementIdentity` values and the complete standard catalogue remain outstanding under DQ-001; implementation activation remains NONE.

MS-PROT-091 v1.1, accepted 15 September 2026, classifies the Workforce Rota commercial-access portfolio: `PLAN_WORKFORCE_SCHEDULE`, `PARTICIPATE_IN_OPEN_ROTA_SELECTION` and `DECLARE_OPERATIONAL_SCHEDULING_UNAVAILABILITY` are protected for BUSINESS + GROWTH, with bounded no-entitlement observation/resolution contracts. It does not mint concrete entitlement identities or close the complete catalogue. DQ-001 remains OPEN and implementation activation remains NONE.

MS-PROT-050 v1.5, accepted 15 September 2026, supplies the dated `BusinessOperatingOverride` mutation/currentness owner-operation contract required before that capability could be commercially classified. It does not itself resolve DQ-001 or define Commercial Entitlement identity/binding. MS-PROT-050 v1.6, accepted 15 September 2026, then classifies Business Hours commercial access: `MAINTAIN_MERCHANT_PRESENCE` protects stable-hours authoring and dated-override maintenance for FREE + BUSINESS + GROWTH, while bounded public/merchant observation and standard withdrawal require no independent Commercial Entitlement. Concrete entitlement identities and the complete catalogue remain outstanding. DQ-001 remains OPEN and implementation activation remains NONE.

Later accepted owner-classification amendments further narrow DQ-001 without closing it: Inventory v1.3, Payment v1.2, Ordering v1.2 and Order Fulfilment/Shipment v1.2 classify their protected new-activity boundaries and bounded residual/support paths; Returns v1.2 explicitly contributes no new standard entitlement identity for its bounded applicable-policy observation, existing-Order resolution support, return-label preparation and existing-label observation/reconciliation contracts; MS-PROT-084 v1.2 classifies `financial-operations/record-establishment-access@1` / `ESTABLISH_FINANCIAL_OPERATIONS_RECORD` for BUSINESS + GROWTH while explicitly classifying bounded Financial Operations preparation, existing-record observation and pure existing-record resolution without an independent Commercial Entitlement; MS-PROT-083 v1.7 classifies `business-intelligence/business-analytics-evaluation-access@1` / `USE_BUSINESS_ANALYTICS` for BUSINESS + GROWTH, `business-intelligence/campaign-analytics-evaluation-access@1` / `USE_CAMPAIGN_ANALYTICS` for GROWTH, and merchant analytical presentation without an independent Commercial Entitlement; and MS-PROT-083 v1.8 classifies `business-intelligence/merchant-analytical-report-access@1` / `DELIVER_MERCHANT_ANALYTICAL_REPORT` and `business-intelligence/measure-observation-export-access@1` / `EXPORT_MEASURE_OBSERVATIONS` with no independent Commercial Entitlement. v1.8 preserves the underlying v1.7 analytical-evaluation permission or legitimate retained-artifact access together with current Actor Authorisation, Exposure, data-use, Audit and Resource Protection at externalisation. These amendments preserve their owner boundaries, do not mint the final entitlement identities and do not activate implementation. MS-PROT-083 v1.8 completes the current Business Intelligence owner/supporting classification contribution but not the final Commercial-owned bindings or manifest.

MS-PROT-092 v1.1, accepted 16 September 2026, further narrows DQ-001 by classifying five exact Document Evidence Coordination supporting access contracts for purpose-bound intake establishment, Extraction Candidate processing, evidence validation, owner-qualified Evidence Handoff and bounded coordination observation. All five are `PLATFORM_SERVICE_ACCESS` with `NO INDEPENDENT COMMERCIAL ENTITLEMENT`; they mint no `CommercialEntitlementIdentity`, grant no consuming-owner permission or Actor Authorisation, and do not weaken Media, Data Protection, Exposure, security, Provider Readiness, Resource Protection or AI non-authority boundaries. DQ-001 remains OPEN for remaining owner/supporting classifications, concrete entitlement identities, exact bindings and the complete manifest; implementation activation remains NONE.

MS-PROT-042 v1.16, accepted 16 September 2026, further narrows DQ-001 through Booking/Appointment/Scheduling commercial-access classification. `booking/reservation-commitment-establishment-access@1` / `ESTABLISH_BOOKING_RESERVATION_COMMITMENT` and `appointment/time-commitment-establishment-access@1` / `ESTABLISH_APPOINTMENT_TIME_COMMITMENT` are protected for BUSINESS + GROWTH. Bounded Booking/Appointment preparation, existing-commitment observation and exact owner-classified residual resolution require no independent Commercial Entitlement; Scheduling availability evaluation is supporting infrastructure with no independent entitlement. Appointment rescheduling into a different agreed interval remains protected new commitment scope. No final `CommercialEntitlementIdentity` is minted; DQ-001 remains OPEN and implementation activation remains NONE.

MS-PROT-044 v1.2, accepted 16 September 2026, supplies exact owner-qualified Offering/Product/ProductVariant merchant-definition observation and authoring access targets required for later commercial classification. It makes no commercial allocation, mints no entitlement identity or binding, explicitly excludes Listing and public-source access, and does not itself narrow the deferred commercial-classification decision. DQ-001 remains OPEN pending commercial classification of these targets, remaining owner/supporting classifications, concrete entitlement identities, exact bindings and the complete manifest; implementation activation remains NONE.

MS-PROT-095 v1.0, accepted 19 September 2026, admits native Quotation semantics without introducing a duplicate Quotation Request owner: customer requests remain Enquiry-owned, while Quotation owns merchant-issued quoted commercial offers, immutable issued revisions and recipient response facts. It classifies `quotation/commercial-offer-issuance-access@1` / `ISSUE_QUOTATION_COMMERCIAL_OFFER` for BUSINESS + GROWTH and classifies bounded new-offer preparation, existing-offer observation, recipient response and existing-offer resolution without independent Commercial Entitlement. It partially resolves `MS-PROT-056-V17-DQ-004` only for the Quotation reservation and further narrows DQ-001; final `CommercialEntitlementIdentity` definitions, exact bindings and the complete standard catalogue remain open. `MS-PROT-084-DQ-006` was subsequently resolved by MS-PROT-096 v1.0; MS-PROT-095 itself did not resolve it. Implementation activation remains NONE.

MS-PROT-096 v1.0, accepted 19 September 2026, admits native outbound customer Invoicing. Invoicing owns issued billing records while Payment retains Payment Obligation, Amount Due, payment execution/evidence, PaymentApplication and Refund ownership. Exact `Invoice → CustomerContext` linkage is preserved when a durable merchant-customer relationship exists, enabling merchant customer-history projection without duplicating Invoice or Payment truth; one-off guest invoices do not manufacture CustomerContext. `invoicing/invoice-issuance-access@1` / `ISSUE_CUSTOMER_INVOICE` is classified for BUSINESS + GROWTH, while bounded preparation, existing-Invoice observation and existing-Invoice resolution require no independent Invoicing entitlement. `MS-PROT-084-DQ-006` is RESOLVED; `MS-PROT-056-V17-DQ-004` is further partially resolved for native Invoicing; DQ-001 remains OPEN for final entitlement identities, exact bindings and the complete standard catalogue. Implementation activation remains NONE.

| ID | Status | Deferred question | Authority / admission barrier |
|---|---|---|---|
| MS-PROT-056-V17-DQ-001 | **OPEN** | Exact entitlement definitions, target bindings and complete standard catalogue | MS-PROT-056 v1.7 §§4, 11–12 and v1.8 §§3–8, 12; bounded owner classifications now include Profile v1.6, Publication v1.4/v1.5, Enquiry v1.5/v1.6, Attention through v1.4, Customer Communication through v1.4, Marketing through v1.4, Storefront/Merchant Brand Infrastructure amendments, Workforce Scheduling v1.4, Workforce Compensation v1.4, Workforce Rota v1.1, Business Hours v1.6, Booking/Appointment/Scheduling v1.16, Inventory v1.3, Payment v1.2, Ordering v1.2, Order Fulfilment/Shipment v1.2, Returns v1.2, Financial Operations v1.2, Business Intelligence through v1.8, Document Evidence Coordination v1.1, Quotation v1.0 and Invoicing v1.0. MS-PROT-044 v1.2 supplies exact Offering/Product/ProductVariant target access contracts, but those targets remain commercially unclassified. Quotation v1.0 supplies its owner-qualified issuance plus bounded preparation/observation/response/resolution classifications but mints no final entitlement identity. Concrete `CommercialEntitlementIdentity` definitions, exact bindings, remaining owner/supporting classifications and the complete standard manifest remain outstanding; runtime grants must not be inferred from allocation/classification tables |
| MS-PROT-056-V17-DQ-002 | **RESOLVED — CATALOGUE-PUBLICATION AND TEMPORAL-SELECTION POLICY ONLY** | Catalogue publication, initial effective start and historical resolution | MS-PROT-056 v1.9 §§8–13, 17; initial start derives from the first successful authoritative publication, subsequent generations retain half-open historical selection, and uncovered history fails explicitly; no production catalogue, implementation activation or C3 completion is authorised |
| MS-PROT-056-V17-DQ-003 | **OPEN** | Prices, quantitative allowances and commercial feasibility | MS-PROT-056 v1.7 §§3, 12, 15; no sale-ready pricing or unlimited-use claims |
| MS-PROT-056-V17-DQ-004 | **PARTIALLY RESOLVED — QUOTATION + NATIVE INVOICING ADMITTED; OTHER RESERVATIONS OPEN** | Exact reserved service portfolios and their admission dependencies | MS-PROT-095 v1.0 admits Quotation and MS-PROT-096 v1.0 admits native Invoicing. Other MS-PROT-056 v1.7 reserved portfolios remain subject to their own admission dependencies and MUST NOT be activated by these resolutions |

MS-PROT-095 v1.0 did not resolve `MS-PROT-084-DQ-006`; MS-PROT-096 v1.0 now resolves that exact native Invoice Authority gate. Other capability deferrals retain their identities and ownership. MS-PROT-084 v1.2 changes only Financial Operations commercial classification and leaves all MS-PROT-084 deferred decisions unchanged. MS-PROT-083 v1.7-v1.8 change only Business Intelligence commercial classification and leave all MS-PROT-083 deferred decisions unchanged. v1.8 completes the current Business Intelligence report/export supporting classifications but does not mint entitlement identities, publish grants or activate implementation. MS-PROT-092 v1.1 changes only Document Evidence Coordination commercial classification, introduces no new MS-PROT-092 semantic deferred decision, contributes zero final entitlement identities and does not publish grants or activate implementation. MS-PROT-042 v1.16 changes only Booking/Appointment/Scheduling commercial classification, contributes no final entitlement identity and does not activate implementation. MS-PROT-044 v1.2 supplies prerequisite Offering/Product/ProductVariant owner-qualified access targets only; it makes no commercial allocation, closes no commercial DQ and activates no implementation. MS-PROT-085 v1.4 changes only the remaining Customer Communication human-response Attention supporting commercial classification, reopens no MS-PROT-085 deferred decision, contributes no final entitlement identity and does not publish grants or activate implementation. MS-PROT-086 v1.4 changes only Customer Communication commercial classification, changes no MS-PROT-086 deferred decision, does not resolve ADR-014-DQ-011, does not activate Message attachments, contributes no final entitlement identity and does not publish grants or activate Customer Communication implementation. MS-PROT-087 v1.4 changes only Marketing commercial classification, changes no MS-PROT-087 deferred decision, leaves `MS-PROT-087-DQ-006` unchanged, contributes no final entitlement identity and does not publish grants or activate Marketing implementation. The MS-PROT-082 v1.1 lifecycle-status mismatch recorded during allocation-policy approval is no longer current: the amendment now records explicit manual acceptance on 14 September 2026, incorporated from remote commit `24bfeaaf`. This is separate acceptance provenance, not a resolution by MS-PROT-056 v1.7; the approved allocation policy remains unchanged. The executable catalogue and C3 activation remain not implementation-ready under MS-PROT-056 v1.7 §16.

---

## 5. Current Promoted Design-Review Queue

RESOLVED — FINANCIAL OPERATIONS / FINANCIAL EVIDENCE / FINANCIAL HEALTH COMPLETE COMPOSITION
    The incomplete MS-PROT-084 composition blocker and all handoff-derived MS-PROT-084 deferred decisions were reviewed under DESIGN-RULES as one subject-wide Financial Operations pass. The complete replacement composition was accepted as MS-PROT-084 v1.1 after Fundamental Vision Conformance, ownership review, recovery analysis, falsification, complete pre-approval presentation and explicit manual approval on 13 September 2026.
    Accepted result:
        Financial Operations is a bounded residual financial owner rather than a universal ledger; source capabilities retain their own truth. Initial Operating Cost, Financial Evidence and deterministic Financial Health portfolios are established. MS-PROT-092 supplies the generic document/extraction substrate while MS-PROT-084 owns the financial consuming/admission contract. Currency remains partitioned absent accepted normalisation authority; mixed-use account relevance does not establish whole-balance business cash; semantically distinct views of one economic exposure are not blindly additive; Financial Health remains derived MS-PROT-083 analytical meaning; statutory/general-ledger accounting remains outside current scope. MS-PROT-083-DQ-015 is resolved; the remaining Financial Health / Financial Intelligence portion of MS-PROT-082-DQ-007 is resolved compositionally by MS-PROT-083 + MS-PROT-084 v1.1. Seven MS-PROT-084 DQs are resolved and nine remain deferred/inactive as catalogued in Section 4.15A. MS-PROT-084 v1.2 later adds only the accepted Financial Operations commercial-access classification and does not reopen those deferred decisions. No implementation activation or reprioritisation follows from either acceptance.

RESOLVED — FIRST-PARTY MERCHANT CLIENT ARCHITECTURE, INSTALLED-CLIENT COMPATIBILITY & NATIVE DELIVERY
    The merchant-client delivery architecture and the previously deferred native-client/session/client-SDK questions were governed under DESIGN-RULES and resolved by accepted MS-PROT-093 v1.0 after Fundamental Vision Conformance, architecture review, falsification, trade-off review, complete pre-approval presentation, structural/UI visualisation and explicit manual approval on 13 September 2026.
    Accepted result:
        GrandRue uses Contract-Driven First-Party Client Architecture. merchant-web remains first-class; Android, iOS, Windows and macOS are admitted native merchant-client platform families without requiring simultaneous release. Independently installed first-party clients require representation-version compatibility and explicit CLIENT_UPDATE_REQUIRED handling. Deterministic first-party client SDK generation, generated-artifact ownership classes, shared design language, cross-platform conformance, platform-local reuse, bounded compile-time declarative generation and purpose-built native UX are accepted. Universal client UI/runtime DSL authority is rejected. Native sessions remain opaque, revocable and server-authoritative; offline consequential mutation is ONLINE REQUIRED unless the owning operation explicitly permits safe deferred submission. ADR-014-DQ-014 and the former first-party client-SDK-generation deferral are resolved. Workforce-specific native/offline semantics such as MS-PROT-081-DQ-019 remain deferred. Implementation sequencing is not activated by this design acceptance.

RESOLVED — INITIAL GENERAL BUSINESS INTELLIGENCE MEASURE PORTFOLIO
    The remaining initial general Analytical Measure portfolio retained by MS-PROT-083-DQ-001 was governed under DESIGN-RULES and resolved by accepted MS-PROT-083 v1.3 after Feature Admission, Fundamental Vision conformance, ownership review, falsification, ambiguity review and explicit manual approval on 10 September 2026.
    Accepted result:
        Exactly seven general Measure Definition families are admitted: order-commitment-count@1, appointment-occurrence-outcome-count@1, booking-utilisation-outcome-count@1, outstanding-payment-obligation-count@1, outstanding-payment-obligation-value@1, inventory-position-constraint-count@1 and scheduled-commitment-person-duration@1. Orders, Appointments and Bookings may additionally use the bounded Commitment Interaction Origin dimension `ONLINE`, `WALK_IN`, `TELEPHONE`, `OTHER_REPRESENTED`, `UNRESOLVED`, derived from MS-PROT-059/source commitment-establishment provenance rather than BI-owned sales-channel truth. Missing and non-applicable remain distinct from zero; Currency remains separated; Payment is not Revenue; origin is not technical entry surface, Payment/Fulfilment method or Marketing attribution; Business Health is not inferred. DQ-001 is RESOLVED. DQ-002 and the remaining BI tail remain separate deferred work. Implementation activation remains NONE.

RESOLVED — WORKFORCE SCHEDULING / LEAVE NOTIFICATION & REMINDER CONTRACT PORTFOLIO
    The exact workforce communication portfolio retained by MS-PROT-081-DQ-015 was governed under DESIGN-RULES and partially resolved by accepted MS-PROT-081 v1.3 after Fundamental Vision conformance, ownership review, falsification, ambiguity review and explicit manual approval on 9 September 2026.
    Accepted result:
        Exactly seven source-owned contracts are admitted: scheduled-work-established@1, scheduled-work-materially-revised@1, scheduled-work-released@1, targeted-shift-offer-issued@1, targeted-shift-offer-no-longer-actionable@1, leave-decision-recorded@1 and scheduled-work-reminder@1. Workforce Scheduling/Leave owns source meaning; composite MS-PROT-075 owns delivery; MS-PROT-065 owns durable wake-up. No universal reminder cadence or universal leave number is created. Exact Timekeeping notification/reminder semantics remain deferred pending DQ-009 through DQ-012. DQ-015 is PARTIALLY RESOLVED. Implementation activation remains NONE.

RESOLVED — WORKFORCE CROSS-ARRANGEMENT OVERLAP & MERCHANT SCHEDULING-BUFFER POLICY
    The cross-Arrangement scheduling overlap question retained by MS-PROT-081-DQ-021 was governed under DESIGN-RULES and resolved by accepted MS-PROT-081 v1.2 after complete proposal, review, falsification and explicit manual approval on 9 September 2026.
    Accepted result:
        Cross-Arrangement overlap is neither universally allowed nor prohibited. A symmetric exact Arrangement-pair policy or exact one-off override governs overlap; absence remains unresolved. An optional merchant-owned Membership-wide MinimumInterCommitmentBuffer governs sequential non-overlapping ScheduledWorkCommitments without constraining unassigned rota Shifts. DQ-021 is RESOLVED. Implementation activation remains NONE.

RESOLVED — CUSTOMER RETURN BEHAVIOUR, RETENTION BOUNDARY & RE-ENGAGEMENT ANALYTICAL HANDOFF
    The Priority-B customer retention/re-engagement roadmap node was re-evaluated under DESIGN-RULES rather than promoted as a standalone capability.
    Resolved for the initial portfolio by accepted MS-PROT-083 v1.2 after Feature Admission, Fundamental Vision review, ownership review, falsification, ambiguity review and explicit manual approval on 9 September 2026.
    Accepted result:
        No standalone Retention/CRM capability is created. CustomerContext retains merchant-customer relationship truth; source commerce capabilities retain activity truth; Business Intelligence owns only defined customer-return analytical meaning; Marketing retains re-engagement Campaign/Audience authority. Exactly three Customer Return Activity contract families and four customer-return Measure Definition families are admitted. Missing history remains unresolved rather than becoming zero; returning-customer-share is not a universal retention rate; no durable lifecycle labels, churn/propensity scoring, automatic re-engagement trigger, Campaign return conversion or causal claim is authorised. At the v1.2 acceptance point DQ-001 remained partially resolved; accepted MS-PROT-083 v1.3 subsequently completes the initial general portfolio and resolves DQ-001. Implementation activation remains NONE.

Promotion records the need for governed design work. It does not pre-accept an answer or reserve a document number.

```text
RESOLVED — INITIAL MARKETING AUDIENCE DEFINITION & ATTRIBUTE PORTFOLIO
    The audience-targeting portfolio retained by composite MS-PROT-087 through v1.1 was governed under DESIGN-RULES
    and resolved by accepted MS-PROT-087 v1.2 after dependency selection, Fundamental Vision review, ownership review,
    falsification, ambiguity review and explicit manual approval on 9 September 2026.
    Accepted result:
        MS-PROT-087-DQ-002 is resolved. Exactly three Audience Definition families are selected —
        marketing-audience/existing-customer@1, marketing-audience/recent-customer-relationship@1 and
        marketing-audience/previous-customer-reengagement@1. CustomerContext remains relationship authority;
        bounded recency uses only owner-qualified Order, Booking and Appointment commitment evidence. Missing or
        incomplete evidence cannot prove inactivity and yields UNRESOLVED. Payment, Enquiry and Conversation do not
        independently extend the initial activity set. Audience membership does not establish permission or endpoint
        authority; DQ-003 remains the direct-marketing externalisation gate. No implementation or merchant activation
        is authorised.

RESOLVED — INITIAL MARKETING CAMPAIGN PURPOSE & OUTREACH PORTFOLIO
    The first production-semantic Campaign portfolio retained by MS-PROT-087 v1.0 was governed under DESIGN-RULES
    and resolved by accepted MS-PROT-087 v1.1 after Fundamental Vision review, architecture/ownership review,
    falsification and explicit manual approval on 9 September 2026.
    Accepted result:
        MS-PROT-087-DQ-001 is resolved. Exactly four purpose families — marketing/merchant-news-awareness@1,
        marketing/offering-awareness@1, marketing/customer-appreciation@1 and marketing/customer-reengagement@1 —
        and exactly WEBSITE_ANNOUNCEMENT_V1 + DIRECT_EMAIL_MARKETING_V1 are selected. Standalone website Announcements
        remain MS-PROT-046-owned; initial direct marketing is relationship-based rather than prospecting; direct email
        remains MS-PROT-075 Notification rather than Conversation-Bound Email. DQ-002 and DQ-003 still gate direct
        externalisation; DQ-004 through DQ-006 remain separate. No implementation or merchant activation is authorised.

RESOLVED — INITIAL CUSTOMER-SERVICE RESPONSE & HUMAN HANDOFF PORTFOLIO
    The automated customer-service response portfolio retained by MS-PROT-086 v1.0 was governed under DESIGN-RULES
    and resolved by accepted MS-PROT-086 v1.3 together with accepted MS-PROT-085 v1.2 after Fundamental Vision review,
    architecture review, falsification, ambiguity review and explicit manual approval on 9 September 2026.
    `MS-PROT-086-DQ-002` is resolved. Exactly seven fact-first response families are selected. Automatic substantive
    response requires complete material-request coverage, owner-qualified current evidence/access and deterministic
    validation. Unsupported, judgement/action-bearing or explicitly human requests use the durable
    `customer-communication / human-response-required@1` Attention path. AI confidence and generic FAQ/RAG material
    are not authority. The package does not activate or reprioritise implementation.

RESOLVED — CONVERSATION BROWSER ACCESS / RESUME / VIEW
    The production Conversation browser-access question retained by MS-PROT-086 v1.1 was governed under
    DESIGN-RULES and resolved by accepted MS-PROT-086 v1.2 after Fundamental Vision review, architecture
    review, falsification, ambiguity review and explicit manual approval on 8 September 2026.
    `MS-PROT-086-DQ-003` is resolved. Registered access is participant-authority based; guest access is
    purpose-bound to one exact Conversation and does not arise from email/contact equality. Exact guest
    credential representation remains a separate active-before-production security gate under ADR-014-DQ-011.
    The amendment does not activate or reprioritise implementation.

RESOLVED — ENQUIRY / CUSTOMER COMMUNICATION RETENTION QUALIFICATION & PERIODS
    Concrete Enquiry/Customer Communication lifecycle policy promoted after MS-PROT-086 v1.1 exposed
    the need for a production retention prerequisite before Customer Messaging could be considered complete.
    Resolved by MS-PROT-053 v1.3 after Fundamental Vision review, architecture review, falsification,
    ambiguity review and explicit manual approval on 8 September 2026. DQ MS-PROT-043-V14-DQ-007
    is resolved. The amendment establishes 12-month Transitory Enquiry and 24-month per-Message ordinary
    communication baselines, bounded provider/security/tombstone periods, minimum-scope qualified evidence,
    plan-neutral lifecycle semantics and recovery-owned backup retention. It does not activate or reprioritise implementation.

RESOLVED — WORKFORCE SCHEDULING / TIMEKEEPING / LEAVE / COMPENSATION AFFINITY CORRECTION PACKAGE
    Workforce Scheduling Arrangement, arrangement-scoped Shift Offers, Scheduled Work Commitments,
    Time Capture Events, Approved Worked-Time evidence and Leave; membership-relative Personal Workforce
    Self-Service Actor Authorisation; exact related-Payee Compensation access; break-compensation ownership;
    and arrangement-qualified Appointment Resource interoperability.
    Resolved by composite MS-PROT-081 through v1.1 together with composite MS-PROT-074 through v1.2,
    MS-PROT-063 v1.2, composite MS-PROT-080 through v1.3 and composite MS-PROT-042 through v1.8 after
    corpus conflict audit, falsification, cross-clause ambiguity review and explicit manual approval on
    5 September 2026. The package does not reprioritise production implementation.

RESOLVED — IMPLEMENTATION-DISCOVERED DESIGN GATE — IMP-06-E3-DG-002
    ApiExposedElementSet production result contract and authority boundary.
    Resolved by MS-PROT-027 v1.10 after governed review, falsification,
    ambiguity review and explicit manual approval. E3 remains conforming; v1.11
    later refines member identity for E4 without reopening the v1.10 result boundary.

RESOLVED — IMPLEMENTATION-DISCOVERED DESIGN GATE — IMP-06-E4-DG-001
    Owner-evaluator runtime binding, deterministic multi-instance E4 and
    Profile Location merchant-choice persistence boundary.
    Resolved by MS-PROT-027 v1.11 + MS-PROT-051 v1.5 after combined
    falsification, root-cause review and explicit manual approval on 2 September 2026.
    Its then-downstream S2/T1b4 bounded-read representation-affinity question is
    separately resolved by MS-PROT-027 v1.13.

RESOLVED — IMPLEMENTATION-DISCOVERED DESIGN GATE — IMP-06-E4-DG-002
    Mechanical evaluator-result submission affinity for exact cross-request,
    cross-invocation and cross-evaluator rejection.
    Resolved by MS-PROT-027 v1.12 after implementation falsification exposed that
    the v1.11 result rows carried candidate + decision but no execution-affinity
    evidence. The accepted correction uses a fresh opaque per-candidate binding
    for each logical evaluator call and keeps stable candidate identity,
    OwnerExposureEvaluationContext and positive membership unchanged.

RESOLVED — IMPLEMENTATION-DISCOVERED DESIGN GATE — IMP-06-S2-T1B4-DG-001
    Bounded Projection read material and same-read representation affinity.
    Resolved by MS-PROT-027 v1.13 after downstream dependency recomputation and
    falsification showed that P2 retained exact evidence and E4 retained exact
    membership but neither carried the value material needed to prevent post-E4
    re-fetch. The accepted correction introduces request-bound typed Projection
    material, exact material/source/P2 affinity, owner-qualified expected-progress
    contributions for revision-affined E4 policy and same-read final selection,
    while preserving value-blind E4 and separate T4 continuation authority.

RESOLVED — IMPLEMENTATION-DISCOVERED DESIGN GATE — IMP-06-S3-DG-001
    Production capability-owned Public Interaction participation-source contract
    and generic zero-source S3 completion boundary.
    The post-T4b macro review proved an actual hard dependency cycle: MS-PROT-046
    v1.2 requires the first concrete Opportunity → enquiry/send-enquiry production
    participation source inside the IMP-07 Publication → Enquiry vertical slice,
    while MS-IMP-001 HARD-blocks IMP-07 on complete IMP-06. Resolved by accepted
    MS-PROT-049 v1.4, which introduces the generic owner-routed participation-source
    boundary and permits a fail-closed empty production-source registry for generic
    S3 completion. Concrete capability sources remain with their owning vertical
    slices and the IMP-06 → IMP-07 HARD edge remains unchanged.

RESOLVED — ARCHITECTURAL VERTICAL SLICE
    Configuration Revision, Compilation & Activation
    Resolved by composite MS-PROT-040 v1.0 + v1.1 + v1.2 + v1.3 + v1.4 + v1.5.

RESOLVED — ARCHITECTURAL VERTICAL SLICE
    Initial Full-Experience Trial Establishment
    Resolved by MS-PROT-056 v1.4.

RESOLVED — ARCHITECTURAL VERTICAL SLICE
    Commercial Agreement & Entitlement Binding
    Resolved by MS-PROT-056 v1.5 and narrowed by later accepted commercial amendments.

RESOLVED — ARCHITECTURAL VERTICAL SLICE
    Cross-Capability Application Orchestration & Consistency
    Resolved by MS-PROT-072.

RESOLVED — PREVIOUSLY PROMOTED
    Resource Fairness, Rate Limiting & Abuse Protection
    Resolved by MS-PROT-073 together with MS-PROT-062 v1.1.

RESOLVED — PREVIOUSLY PROMOTED
    Staff / Team identity, merchant relationship, delegated authority and lifecycle
    Resolved by composite MS-PROT-074 through v1.2 together with composite MS-PROT-063 through v1.2.

RESOLVED — PREVIOUSLY PROMOTED
    Notification intent, recipient preference, channel selection,
    delivery attempt and delivery evidence
    Resolved by composite MS-PROT-075 through v1.2; v1.1 completes concrete provider execution and v1.2 closes the retained Notification policy/design tail.

RESOLVED — BOUNDARY REVIEW
    Merchant/controller ownership transfer, suspension,
    closure and terminal account lifecycle
    Resolved by MS-PROT-076.

RESOLVED — DOMAIN STRESS TEST
    Nursery / daycare / Montessori media-heavy semantic validation
    Existing capability semantics plus MS-PROT-066 v1.2 and MS-PROT-053 v1.1
    remain sufficient for the tested public/media boundary.

RESOLVED — PRODUCTION ARCHITECTURE / TAS
    Backup, restore, corruption recovery and disaster recovery
    Resolved by MS-TAS-RECOVERY-001.

RESOLVED — EXECUTABLE ARCHITECTURE COMPLETENESS — EAC-001
    Semantic Release Publication, Retention & Bootstrap Architecture
    Review/falsification evidence:
        docs/development/executable-architecture-completeness-review-2026-08-25.md
        docs/development/executable-architecture-recommendation-falsification-2026-08-25.md
        docs/development/executable-architecture-second-order-falsification-2026-08-25.md
    Resolving authority:
        ADR-011 — docs/foundation/adr/semantic-release-publication-retention-bootstrap.md

RESOLVED — EXECUTABLE ARCHITECTURE COMPLETENESS — EAC-002
    Runtime Semantic Execution Support & Deployment Compatibility
    Review/falsification evidence:
        docs/development/executable-architecture-completeness-review-2026-08-25.md
        docs/development/executable-architecture-recommendation-falsification-2026-08-25.md
        docs/development/executable-architecture-second-order-falsification-2026-08-25.md
    Resolving authority:
        ADR-012 — docs/foundation/adr/runtime-semantic-execution-compatibility.md

RESOLVED — EXECUTABLE ARCHITECTURE COMPLETENESS — EAC-003
    Projection Contract, Freshness & Availability Boundary
    Review/falsification evidence:
        docs/development/executable-architecture-completeness-review-2026-08-25.md
        docs/development/executable-architecture-recommendation-falsification-2026-08-25.md
        docs/development/executable-architecture-second-order-falsification-2026-08-25.md
    Resolving authority:
        MS-PROT-027 v1.3 — Projection Freshness, Serviceability & Rebuild Contract Amendment

RESOLVED — ORDER / ORDERING SEMANTIC AUTHORITY GAP
    Review/falsification evidence:
        docs/development/order-authority-design-review-falsification-2026-08-26.md
    Manual approval:
        26 August 2026 — approved for documentation and implementation
    Resolving authority:
        MS-PROT-077 — Order Commitment, Amendment & Lifecycle Model
    Accepted result:
        Ordering owns the purchase/order commitment, durable commitment portions,
        committed subject/quantity/term provenance and Order-side amendment/release truth.
        Inventory, Money/Payment, Order Fulfilment and Shipment retain independent
        authority. Required Inventory Claims are atomic with Order commitment where
        stock protection is a commitment invariant. Universal Business Order and
        giant cross-capability OrderStatus are rejected.

RESOLVED — BOOKING / APPOINTMENT EXECUTION-CONTRACT GAP
    Review/falsification evidence:
        docs/development/motel-booking-execution-design-review-falsification-2026-08-26.md
    Manual approval:
        26 August 2026 — approved after Appointment/Scheduling boundary review
    Resolving authority:
        MS-PROT-042 v1.5 — Booking, Appointment & Scheduling Execution Contract Amendment
    Accepted result:
        booking.confirm establishes Booking only; appointment.confirm establishes
        Appointment only after applicable Scheduling revalidation; Scheduling owns
        scheduling constraints and Appointment availability decisions; Calendar is
        projection/integration authority; booked-subject truth remains distinct from
        internal Resource Allocation; no Motel/Consultant business-type branch is created.

RESOLVED — LIVE-TESTING READINESS GOVERNANCE
    Governed in-chat proposal/review/falsification completed under DESIGN-RULES.
    Manual approval:
        26 August 2026 — approved as the governance authority for live-testing readiness
    Resolving authority:
        MS-PROT-078 — Adequate Capability Portfolio & AI Concierge Live-Testing Readiness Model
    Accepted result:
        readiness requires Capability Adequacy, Composition Adequacy and AI Concierge
        Adequacy across a materially varied merchant portfolio; individual pilots remain
        incremental evidence. MS-PROT-079 later supersedes only the former programme-order
        rule; MS-PROT-078's substantive live-readiness evidence semantics survive.

RESOLVED — REMAINING BACKEND DESIGN DEPENDENCY GOVERNANCE
    Governed in-chat proposal/review/falsification completed under DESIGN-RULES v2.1.
    Manual approval:
        26 August 2026 — approved as the current remaining-backend design sequencing authority
    Resolving authority:
        MS-PROT-079 — Remaining Backend Design Dependency Governance Model
    Accepted result:
        remaining backend design proceeds through a binding 21-target dependency graph;
        cross-cutting backend dimensions remain applicable throughout; prototype/** and
        presentation/UI work are excluded; implementation is not authorised; Target 1 —
        Production semantic-release bootstrap was established as the first design target.

RESOLVED — MS-PROT-079 TARGET 1 — PRODUCTION SEMANTIC-RELEASE BOOTSTRAP
    Governed entirely in chat through design trace, review, falsification and ambiguity review.
    Manual approval:
        26 August 2026 — approved with mandatory cataloguing of all deferred questions
    Resolving authority:
        ADR-013 — Packaged Semantic Definition Materialisation & Production Bootstrap
        docs/foundation/adr/packaged-semantic-definition-materialisation-production-bootstrap.md
    Accepted result:
        production semantic definitions are supplied as immutable exact-release bundles
        packaged with the backend deployment, deterministically validated/materialised into
        an immutable process-local exact-release SemanticReleaseAssembly repository; ordinary
        unpartitioned serving cohorts have uniform materialisation coverage; materialisation
        remains distinct from ADR-012 executable-contract support; implicit release fallback
        is prohibited. Deferred questions ADR-013-DQ-001..013 are catalogued in Section 4.1.

RESOLVED — MS-PROT-079 TARGET 2 — AUTHENTICATION / SESSION ESTABLISHMENT
    Governed entirely in chat through authority trace, architecture review, security hardening,
    falsification, ambiguity review and complete-authority approval.
    Manual approval:
        26 August 2026 — approved after high-assurance security hardening
    Resolving authority:
        ADR-014 — High-Assurance Authentication, Session & Trusted Browser Execution Architecture
        docs/foundation/adr/high-assurance-authentication-session-trusted-browser-execution.md
    Accepted result:
        Merchant Controller authentication is passkey/WebAuthn-first; human browser continuity
        uses high-entropy opaque server-authoritative sessions with host-bound protected cookies;
        mutable merchant authority remains server-resolved; staff operation requires independent
        device plus person evidence; Controller/staff sessions are short and bounded; critical
        Controller operations require recent step-up authentication; deferred questions
        ADR-014-DQ-001..017 are catalogued in Section 4.2.

RESOLVED — MS-PROT-079 TARGET 3 — INITIAL MERCHANT CONFIGURATION BOOTSTRAP
    Governed entirely in chat through accepted-authority trace, implementation-evidence audit,
    gap classification, architecture review, falsification, ambiguity review and complete-authority approval.
    Manual approval:
        26 August 2026 — approved with complete deferred-question catalogue
    Resolving authority:
        MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment
        designs/MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment.md
    Accepted result:
        Merchant Account may exist without an active configuration and no placeholder/default
        configuration is manufactured; ordinary new configuration pins one exact admitted
        Semantic Registry Release; the exact RCP deterministically yields the new-activity
        execution-requirement set; activation consumes ADR-013 materialisation evidence and
        ADR-012 contract-scoped executable-support evidence; serving-generation fencing prevents
        activation/deployment races; deployment promotion may not strand active required
        configurations; current Merchant Controller authority governs ordinary first activation.
        Deferred questions MS-PROT-040-V12-DQ-001..012 are catalogued in Section 4.3.

RESOLVED — MS-PROT-079 TARGET 4 — ONBOARDING ENGINE
    Governed entirely in chat through accepted-authority trace, design-gap classification,
    lifecycle/concurrency review, falsification, ambiguity review and complete-authority approval.
    Manual approval:
        27 August 2026 — approved after correcting the amendment number to compose with existing MS-PROT-052 v1.1
    Resolving authority:
        MS-PROT-052 v1.2 — Onboarding Case Lifecycle, Concurrency & Initial Configuration Intent Handoff Amendment
        designs/MS-PROT-052 v1.2 — Onboarding Case Lifecycle, Concurrency & Initial Configuration Intent Handoff Amendment.md
    Accepted result:
        ordinary initial onboarding is a durable merchant-scoped Onboarding Case rather than a
        transient wizard or security Session; material mutations advance exact case revisions,
        stale concurrent writes conflict, corrections preserve provenance and deterministically
        recompute/prune the active candidate graph, final review binds one exact case revision,
        submission creates one immutable retry-safe Initial Configuration Intent, and MS-PROT-040
        remains the first Configuration Revision / semantic-release / activation authority.
        Deferred implementation choices MS-PROT-052-V12-DQ-001..013 are catalogued in Section 4.4.

RESOLVED — MS-PROT-079 TARGET 5 — MERCHANT PROFILE / LOCATION
    Governed entirely in chat through accepted-authority trace, production-ownership gap classification,
    implementation-evidence audit, lifecycle/concurrency review, falsification, ambiguity review and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-5 authority
    Resolving authority:
        MS-PROT-051 v1.1 — Merchant Profile & Location Authoritative Mutation, Revision & Onboarding Handoff Amendment
        designs/MS-PROT-051 v1.1 — Merchant Profile & Location Authoritative Mutation, Revision & Onboarding Handoff Amendment.md
    Accepted result:
        Merchant Profile remains one management composition over independently authoritative profile/presence
        fact families rather than one BusinessProfile aggregate; facts use stable merchant-scoped identities and
        exact revisions; same-fact stale writes conflict while unrelated edits remain independent; mutations are
        retry-idempotent; MerchantLocation follows ACTIVE → RETIRED with correction distinct from relocation and
        material relocation requiring a new identity; active executable references prevent unsafe retirement while
        historical meaning survives; submitted onboarding may adopt reviewed candidates only through Profile-owned
        operations and may not overwrite newer direct authority; Profile remains separate from Configuration,
        projections, Exposure, trust and external-provider authority. Deferred questions
        MS-PROT-051-V11-DQ-001..014 are catalogued in Section 4.5.

RESOLVED — MS-PROT-079 TARGET 6 — AI INFERENCE BOUNDARY
    Governed entirely in chat through accepted-authority trace, production-boundary gap classification,
    security/prompt-injection/data-boundary review, implementation-evidence audit, falsification,
    ambiguity review and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-6 authority; later v1.2 merchant-assistance refinement manually approved on 27 August 2026 without reopening Target 6
    Resolving authority:
        MS-PROT-057 v1.1 — Production AI Inference Contract, Provenance & Safety Boundary Amendment
        designs/MS-PROT-057 v1.1 — Production AI Inference Contract, Provenance & Safety Boundary Amendment.md
    Later accepted refinement:
        MS-PROT-057 v1.2 — AI Assistance Responsibility, Merchant-Intent & Progressive Website Assistance Governance Amendment
        designs/MS-PROT-057 v1.2 — AI Assistance Responsibility, Merchant-Intent & Progressive Website Assistance Governance Amendment.md
    Accepted result:
        every material production inference executes through a versioned AI Inference Contract with
        purpose-built tenant-isolated context; logical inference requests are distinct from provider attempts;
        model output is untrusted until deterministic schema/semantic validation; unknown semantic identifiers
        fail closed; conversation memory, confidence, external content and provider-native structured output do
        not become authority; validated results are immutable and exact-approval-affined; retries are acknowledgement-safe;
        provider/model failover is limited to qualified AI Inference Deployments; material deployment changes require
        evaluation; production interaction data does not automatically become training data; deterministic non-AI
        fallbacks survive. v1.2 further makes merchant assistance contract-first rather than a fixed agent fleet,
        requires merchant confirmation before materially inferred meaning is promoted into authoritative intent,
        preserves direct/manual ordinary operations, establishes bounded merchant-side assistance responsibilities,
        progressive non-blocking Website Population & Management, deterministic-first image/video optimisation and
        frugal per-contract D0/M1/M2/M3 inference selection. Deferred questions MS-PROT-057-V11-DQ-001..020 remain
        catalogued in Section 4.6; v1.2's additional retained implementation/future scope is listed in Section 4.

RESOLVED — MS-PROT-079 TARGET 7 — PROJECTION CONTRACTS
    Governed entirely in chat through accepted-authority trace, concrete-projection gap classification,
    implementation-evidence audit, architecture review, falsification, ambiguity review and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-7 authority
    Resolving authority:
        MS-PROT-027 v1.4 — Initial Production Projection Portfolio & Concrete Contract Registration Amendment
        designs/MS-PROT-027 v1.4 — Initial Production Projection Portfolio & Concrete Contract Registration Amendment.md
    Later accepted implementation-discovered read-affinity refinement:
        MS-PROT-027 v1.13 — Bounded Projection Read Material & Representation Affinity Amendment
    Accepted result:
        MS-PROT-027 v1.3 remains the generic Projection Contract authority while initial production defaults to
        authoritative queries and request-scoped composition; no speculative generic projection engine is required;
        `platform/merchant-presence` and `calendar/merchant-calendar` are the initial concrete cross-cutting contracts;
        Merchant Presence preserves independently authoritative Profile/Location/Hours sources, current source-revision
        evidence, conservative stale-serving and Exposure separation; Calendar preserves GrandRue commitments during
        optional external-provider failure while refusing to manufacture current availability from unknown evidence;
        storefront and dashboard composition remain initially request-scoped; cached dashboard state never becomes Actor
        Authorisation; every later capability target must classify required reads under v1.3 before Design-Closure.
        v1.13 later closes the implementation-discovered S2/T1b4 same-read material-affinity gap by binding typed
        request-scoped Projection material to exact P2 evidence/results and exact E4 candidate/positive-member selection
        without making E4 value-aware or introducing post-E4 latest-state acquisition. `MS-PROT-051-V11-DQ-005` is
        resolved and deferred questions MS-PROT-027-V14-DQ-001..014 are catalogued in Section 4.7.

RESOLVED — MS-PROT-079 TARGET 8 — EXPOSURE RESOLUTION
    Governed entirely in chat through accepted-authority trace, runtime-gap classification,
    implementation-evidence review, falsification, ambiguity review, corpus-conformance review and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-8 authority
    Resolving authority:
        MS-PROT-027 v1.5 — Production Exposure Resolution & Initial Exposure Contract Portfolio Amendment
        designs/MS-PROT-027 v1.5 — Production Exposure Resolution & Initial Exposure Contract Portfolio Amendment.md
    Later accepted implementation-discovered refinements:
        MS-PROT-027 v1.10 — Exposure Result Boundary & Fail-Closed Resolution Amendment
        MS-PROT-027 v1.11 — Instance-Aware Exposure Membership & Owner-Filtered Deterministic Resolution Amendment
        MS-PROT-027 v1.12 — Opaque Evaluator Submission Affinity Amendment
        MS-PROT-027 v1.13 — Bounded Projection Read Material & Representation Affinity Amendment
        MS-PROT-051 v1.5 — Merchant Location Public Exposure Choice Revision & Persistence Amendment
    Accepted result:
        Exposure remains an element-level deterministic EXPOSE/WITHHOLD decision over already-legitimate candidates;
        exact owner-qualified Exposure Element Contracts are release-affined and fail closed when missing or unresolved;
        Audience Observation Context is established by trusted GrandRue boundaries rather than client assertion;
        merchant Exposure choices remain owned by applicable fact/capability authorities; privacy/security and relationship
        constraints outrank merchant choice; Surface membership, Projection Serviceability, authentication, Actor
        Authorisation, Provider Readiness and AI inference remain distinct. v1.10 establishes the positive-only fail-closed
        result boundary, v1.11 preserves stable candidate-instance identity for multi-instance elements and governs
        deterministic owner-filtered batch evaluation without making E4 value-aware, and v1.12 adds exact ephemeral
        per-submission evaluator-result affinity so cross-request/cross-invocation reuse is mechanically rejectable without
        leaking private request/admission bindings or altering candidate/member identity. v1.13 preserves those E4 semantics
        while adding owner-qualified expected material-progress evidence where policy is revision-affined and same-read
        downstream representation selection. MS-PROT-051 v1.5 gives Merchant Location public choice its independent
        stable-Location-scoped Profile persistence lifecycle. DQ-002 and DQ-003 are resolved; remaining deferred questions
        are catalogued in Section 4.8.

RESOLVED — MS-PROT-079 TARGET 9 — PROVIDER READINESS
    Governed entirely in chat through accepted-authority trace, runtime-gap classification,
    implementation-evidence review, falsification, corpus-conformance review and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-9 authority
    Resolving authority:
        MS-PROT-048 v1.4 — Provider Connection, Operational Readiness & Runtime Fulfilment Admission Amendment
        designs/MS-PROT-048 v1.4 — Provider Connection, Operational Readiness & Runtime Fulfilment Admission Amendment.md
    Accepted result:
        Fulfilment Requirement, Fulfilment Binding, ProviderConnection and Provider Readiness remain distinct;
        ProviderConnection is stable live operational integration identity rather than credential identity;
        Provider Readiness is current role/context/obligation-qualified runtime evidence with READY, DEGRADED,
        NOT_READY and UNKNOWN outcomes; UNKNOWN fails closed for new dependent execution; provider-wide health,
        credential usability, circuit state and connection state contribute bounded evidence without becoming business
        truth; readiness is revalidated at execution; provider or connection substitution requires separately authorised
        fallback/binding semantics; callback evidence remains independently authenticated/interpreted; execution uncertainty
        after an attempted provider call remains governed by MS-PROT-069. Deferred questions
        MS-PROT-048-V14-DQ-001..014 are catalogued in Section 4.9.

RESOLVED — MS-PROT-079 TARGET 10 — PUBLICATION / ENQUIRY
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    production-gap classification, two-owner architecture review, falsification, corpus-conformance review and complete-package approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-10 two-authority closure package
    Resolving authorities:
        MS-PROT-046 v1.2 — Production Publication Revision, Exposure & Public Interaction Amendment
        designs/MS-PROT-046 v1.2 — Production Publication Revision, Exposure & Public Interaction Amendment.md
        MS-PROT-043 v1.4 — Production Enquiry Submission, Provenance & Merchant Observation Amendment
        designs/MS-PROT-043 v1.4 — Production Enquiry Submission, Provenance & Merchant Observation Amendment.md
    Later accepted generic participation-source refinement:
        MS-PROT-049 v1.4 — Capability-Owned Public Interaction Participation Source & Generic Binding Projection Amendment
    Accepted result:
        Publication preserves stable merchant-scoped information-object identity with immutable materially relevant
        revisions, same-object optimistic concurrency and retry idempotency; `DRAFT | PUBLISHED | WITHDRAWN` remains
        the shared lifecycle while Publication-owned `publishFrom/publishUntil` evidence composes through MS-PROT-027
        Exposure rather than creating `SCHEDULED`/`EXPIRED` states; initial public Publication reads are request-scoped,
        explicit owner-qualified PUBLIC Exposure contract families are registered, and Opportunity → Enquiry subject
        participation is explicit rather than inferred from visibility or frontend convention. Enquiry preserves the
        immutable submitted customer request and context, revalidates current subject/interation authority, carries
        structured context forward, records minimum reconstructible submission-time subject provenance, is logically
        idempotent, does not automatically create CustomerContext/Conversation/commitment state, and initially composes
        merchant reads request-scoped with explicit MERCHANT Exposure contracts. MS-PROT-049 v1.4 later defines the
        generic owner-routed production participation-source boundary used by concrete capability sources without moving
        the Opportunity → Enquiry source out of its owning vertical slice. Deferred questions
        MS-PROT-046-V12-DQ-001..008 and MS-PROT-043-V14-DQ-001..008 are catalogued in Sections 4.10 and 4.11.

RESOLVED — MS-PROT-079 TARGET 11 — BOOKING / APPOINTMENT / SCHEDULING
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    production-gap classification, Scheduling/provider/Calendar/customer-context boundary review,
    falsification, ambiguity review, corpus-conformance review and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-11 authority
    Resolving authority:
        MS-PROT-042 v1.6 — Production Scheduling Evidence, Appointment Admission & Customer Surface Requirement Amendment
        designs/MS-PROT-042 v1.6 — Production Scheduling Evidence, Appointment Admission & Customer Surface Requirement Amendment.md
    Later accepted workforce-availability interoperability refinements:
        MS-PROT-042 v1.7 — Workforce Availability Constraint Interoperability Amendment
        MS-PROT-042 v1.8 — Arrangement-Qualified Workforce Availability Interoperability Amendment
    Accepted result:
        Scheduling distinguishes SCHEDULABLE, NOT_SCHEDULABLE and UNRESOLVED from sufficient current
        owner-qualified evidence; unresolved required evidence cannot authorise Appointment commitment;
        final appointment.confirm revalidates Scheduling and invariant-required capacity; external calendar/provider
        state supplies evidence rather than Scheduling authority; initial Scheduling availability remains request-scoped;
        Booking remains independent unless separately governed; owner-qualified Booking/Appointment CUSTOMER relationship
        requirements are established without making authentication, identifiers, Surface or Exposure mutation authority.
        MS-PROT-042 v1.7 permits owner-qualified Workforce availability evidence from MS-PROT-081 to constrain new Appointment
        commitments; v1.8 requires that evidence to be bound to an explicit Workforce Scheduling Arrangement ↔ schedulable
        Resource link rather than Merchant Membership or Identity similarity, while preserving the rule that later workforce
        changes cannot silently cancel existing Appointments. The Booking/Appointment scope of MS-PROT-027-V15-DQ-005 and
        Scheduling-specific scope of MS-PROT-048-V14-DQ-014 are resolved; MS-PROT-027-V14-DQ-013 is narrowed to downstream
        presentation semantics.

RESOLVED — MS-PROT-079 TARGET 12 — ORDERING / INVENTORY
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    production-gap classification, ownership review, operation-contract review, partial-quantity
    falsification, read/Exposure/customer-context review, ambiguity review and complete two-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-12 two-authority closure package
    Resolving authorities:
        MS-PROT-058 v1.1 — Inventory Position, Mutation & Quantity Claim Execution Contract Amendment
        designs/MS-PROT-058 v1.1 — Inventory Position, Mutation & Quantity Claim Execution Contract Amendment.md
        MS-PROT-077 v1.1 — Order Amendment, Release, Inventory Consequence & Customer Relationship Execution Contract Amendment
        designs/MS-PROT-077 v1.1 — Order Amendment, Release, Inventory Consequence & Customer Relationship Execution Contract Amendment.md
    Accepted result:
        Inventory explicitly establishes merchant/subject/scope-qualified stock positions and distinguishes
        unestablished position from known zero; stock adjustment/transfer and claim/release/expiry/fulfil operations
        preserve provenance and idempotency; original Inventory Claim quantity remains immutable while quantity-bearing
        resolution facts support partial release/fulfilment; planned stock consumption protects active claims while
        physical corrections may truthfully reveal shortfall; Inventory reads remain initially request-scoped and never
        become reservation authority. Ordering preserves immutable original commitment portions; quantity reduction uses
        release evidence, quantity increase/replacement uses new amendment-established commitment evidence, and required
        Inventory claim release/new-claim consequences compose atomically with Order amendment/release. The owner-qualified
        `ordering / related-customer-order` requirement closes the Ordering scope of MS-PROT-027-V15-DQ-005 without making
        CustomerAccount authentication, identifiers, contact similarity, Surface or Exposure mutation authority.

RESOLVED — MS-PROT-079 TARGET 13 — PAYMENT
    Governed entirely in chat through accepted-authority trace, current payment-implementation evidence review,
    design-gap classification, due-condition and obligation-adjustment review, provider execution/uncertainty review,
    reconciliation/refund/customer-relationship review, read/Exposure classification, falsification and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-13 authority
    Resolving authority:
        MS-PROT-055 v1.1 — Payment Obligation, Provider Execution, Reconciliation & Refund Execution Contract Amendment
        designs/MS-PROT-055 v1.1 — Payment Obligation, Provider Execution, Reconciliation & Refund Execution Contract Amendment.md
    Accepted result:
        Payment preserves business commitment, Payment Obligation, PaymentExecutionRequest, provider payment evidence,
        PaymentApplication, provider settlement, RefundExecutionRequest and Refund as distinct facts rather than one
        universal Payment status. Payment Obligations remain immutable historical truth and later authorised amount changes
        use PaymentObligationAdjustment; deterministic registered due conditions resolve as DUE, NOT_DUE or UNRESOLVED,
        keeping outstanding amount distinct from current amount due. Provider financial side effects begin only after durable
        exact Payment execution/refund requests exist with historical binding/correlation provenance; raw provider status does
        not create GrandRue Payment truth; evidence is recorded before reconciliation and uncertain side effects are not
        blindly retried. PaymentApplications preserve obligation/evidence bounds and many-to-many partial application;
        Refund is later Payment-owned truth and does not rewrite payment, obligation or source commitment. Initial Payment
        reads remain request-scoped. `payment / related-customer-payment-obligation` delegates relationship authority to the
        exact owner-qualified commercial source. The Payment scope of MS-PROT-027-V15-DQ-005 and Payment-specific scope of
        MS-PROT-048-V14-DQ-014 are resolved without closing unrelated capability branches.

RESOLVED — MS-PROT-079 TARGET 14 — ORDER FULFILMENT / SHIPMENT
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    gap classification, fulfilment-quantity review, Order release/fulfilment concurrency review,
    Inventory consistency review, Shipment identity/fact review, provider execution/uncertainty review,
    redelivery/replacement-stock review, customer/projection review, falsification, ambiguity review
    and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-14 authority
    Resolving authority:
        MS-PROT-060 v1.1 — Production Order Fulfilment, Shipment Execution & Tracking Contract Amendment
        designs/MS-PROT-060 v1.1 — Production Order Fulfilment, Shipment Execution & Tracking Contract Amendment.md
    Accepted result:
        Order Fulfilment is durable merchant-scoped quantity-bearing satisfaction evidence against exact Order
        Commitment Portions and does not rewrite Order truth; collection handover and outbound-movement handover are
        explicit Satisfaction Anchors. Stock-protected satisfaction composes atomically with Inventory claim fulfilment
        and physical stock movement, while fulfilment/release concurrency cannot consume the same remaining quantity.
        Inventory shortfall blocks normal fulfilment without fabricated stock, and already-occurred physical action enters
        bounded reconciliation rather than semantic repair. Shipment is independent physical-movement attempt/evidence,
        uses durable identity, dispatch facts and a small DELIVERED/NOT_DELIVERED terminal outcome vocabulary rather than
        a carrier lifecycle. Provider-dependent preparation begins only from a durable correlated request under current
        Provider Readiness; callbacks/evidence are authenticated and interpreted; uncertainty is not blindly retried.
        Redelivery may create another Shipment without duplicating Order Fulfilment, while replacement goods consume real
        additional Inventory. Customer tracking reuses `ordering / related-customer-order`, remains request-scoped initially
        and does not create public tracking authority. The Order Fulfilment/Shipment scope of MS-PROT-027-V15-DQ-005,
        Shipment-specific scope of MS-PROT-048-V14-DQ-014 and the Inventory shortfall handoff from MS-PROT-058 v1.1 are resolved.

RESOLVED — MS-PROT-079 TARGET 15 — RETURNS
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    optional-capability/merchant-authority correction, provider-label review, returned-stock ownership review,
    Refund/replacement/Inventory independence review, historical-affinity review, customer/projection review,
    falsification, ambiguity review and complete revised two-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete revised Target-15 two-authority closure package
    Resolving authorities:
        MS-PROT-061 v1.1 — Optional Merchant Returns Policy Capability & Production Return-Action Execution Contract Amendment
        designs/MS-PROT-061 v1.1 — Optional Merchant Returns Policy Capability & Production Return-Action Execution Contract Amendment.md
        MS-PROT-058 v1.2 — Returned Stock Receipt, Disposition & Sellable Re-entry Contract Amendment
        designs/MS-PROT-058 v1.2 — Returned Stock Receipt, Disposition & Sellable Re-entry Contract Amendment.md
    Accepted result:
        Returns is optional merchant-policy infrastructure whose current applicability comes only from accepted Merchant
        Configuration; Ordering, Product, Inventory, Shipment, Payment, business category, customer request and AI inference
        do not activate it. Returns disabled does not prohibit merchant business discretion: customers may still contact the
        merchant and the merchant may independently authorise Refund, replacement, physical returned-stock receipt or another
        legitimate remediation through the owning capability. Structured Returns keeps recurring policy distinct from the
        merchant's specific business decision, preserves historical Order return-policy provenance and bounded residual
        authority, rejects ReturnCase/ReturnRequest/ReturnStatus workflow authority, and makes provider-backed return-label
        preparation durable/correlated/provider-ready infrastructure rather than commercial approval. Inventory independently
        owns ReturnedStockReceipt and ReturnedStockDisposition whether Returns is enabled or disabled; physical receipt is not
        sellable stock, only SELLABLE_REENTRY may create a positive provenance-bearing `RETURN` Inventory movement, and original
        outbound claim/movement/Fulfilment history remains true. Refund, replacement and Inventory receipt do not universally
        depend on Returns capability applicability. Returns customer observation reuses `ordering / related-customer-order`.
        The Returns customer scope of MS-PROT-027-V15-DQ-005, return-label-specific scope of MS-PROT-048-V14-DQ-014 and the
        returned-stock/re-entry/disposition deferrals from MS-PROT-058/MS-PROT-060 are resolved without closing later self-service,
        exchange, reverse-logistics, API, background-process or reconciliation scope.

RESOLVED — MS-PROT-079 TARGET 16 — NOTIFICATION PROVIDERS
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    provider-model gap classification, platform-authority requirement/routing review, DeliveryAttempt identity/idempotency
    review, Provider Readiness/degradation/fallback review, provider-evidence/callback review, recipient/consent/Exposure
    boundary review, falsification, ambiguity review and complete two-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-16 two-authority closure package
    Resolving authorities:
        MS-PROT-048 v1.5 — Platform-Scoped Fulfilment Requirement, Routing & Serving-Affinity Contract Amendment
        designs/MS-PROT-048 v1.5 — Platform-Scoped Fulfilment Requirement, Routing & Serving-Affinity Contract Amendment.md
        MS-PROT-075 v1.1 — Production Notification Provider Delivery, Attempt & Evidence Execution Contract Amendment
        designs/MS-PROT-075 v1.1 — Production Notification Provider Delivery, Attempt & Evidence Execution Contract Amendment.md
    Accepted result:
        registered platform authorities may own Fulfilment Requirements without being disguised as business capabilities;
        merchant-routed and platform-routed fulfilment remain distinct; shared GrandRue technical infrastructure uses immutable
        serving-affined PlatformFulfilmentBindingSetRevision rather than fake Merchant Scope or mutable global provider choice,
        while historical provider/binding/connection affinity survives provider change. Notifications concretises the
        provider-neutral `notification / message-delivery` role with channel-qualified paths, requires static compatibility
        before runtime readiness, and creates a durable exact DeliveryAttempt before every external effect. Provider effect
        idempotency/correlation is attempt-scoped rather than universally Dispatch-scoped; same-attempt replay and separately
        authorised later attempts remain distinct. DEGRADED may proceed only when every hard Notification Contract obligation
        remains provably satisfiable; UNKNOWN/NOT_READY fail closed for new work. Same-channel provider fallback and cross-channel
        fallback are distinct, channel change creates a new Dispatch, and uncertain prior effects block blind duplication.
        Synchronous/asynchronous provider evidence is authenticated, correlated and interpreted before DeliveryEvidence;
        provider acceptance/delivery/read evidence remains distinct from business acknowledgement. Recipient relationship,
        endpoint, preference, consent/policy, Exposure and other mutable delivery eligibility are revalidated where required
        before new externalisation. Initial Notification reads remain request-scoped. The Notification-specific scope of
        MS-PROT-048-V14-DQ-014 and the platform-originated requirement/routing gap retained by MS-PROT-048 v1.3 are resolved;
        Target 17 retains concrete data-protection/consent/retention/transfer questions and Targets 18–20 retain machinery,
        observability/reconciliation and API representation respectively.

RESOLVED — MS-PROT-079 TARGET 17 — DATA PROTECTION LIFECYCLE
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    production lifecycle-gap classification, use-versus-retention review, owner-safe disposition review,
    provider/projection convergence review, Audit/recovery/AI boundary review, falsification,
    ambiguity review and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-17 authority
    Resolving authority:
        MS-PROT-053 v1.2 — Production Data Lifecycle Evaluation, Disposition & Convergence Contract Amendment
        designs/MS-PROT-053 v1.2 — Production Data Lifecycle Evaluation, Disposition & Convergence Contract Amendment.md
    Later accepted communication-retention qualification:
        MS-PROT-053 v1.3 — Enquiry and Customer Communication Retention Qualification & Period Amendment
        designs/MS-PROT-053 v1.3 — Enquiry and Customer Communication Retention Qualification & Period Amendment.md
    Accepted result:
        Data Protection constrains use, retention and disposition without becoming a second business owner;
        materially governed lifecycle scopes use owner-qualified DataLifecycleContracts; current lifecycle
        evaluation is explicitly RETAIN, DISPOSITION_DUE or UNRESOLVED; use eligibility remains separate from
        retention so retained data is not automatically reusable, publishable, AI-eligible or provider-transmittable;
        UNRESOLVED authorises neither irreversible destruction nor renewed ordinary use. Consequential disposition
        executes through the owning authority with final revalidation, concurrency protection and idempotency rather
        than generic cross-domain deletion; CustomerContext is not a cascade-deletion root and Merchant Account closure
        is not DELETE ALL. Historical evidence is minimised rather than inflated into full-payload retention; provider-side
        disposition remains an independently evidenced obligation; Projection/Exposure and other derived copies must converge
        after authoritative disposition/restriction; stale copies, backups, Audit/event payloads and AI convenience do not
        manufacture continuing data authority. Time/event review triggers are semantic obligations while the generic durable
        scheduler/retry mechanism remains Target 18. MS-PROT-053 v1.3 additionally resolves concrete Enquiry/Customer
        Communication retention qualification and baseline periods while preserving source ownership, use/retention separation,
        minimum evidence and Recovery-owned backup retention. The semantic scope of MS-PROT-027-V15-DQ-007 and
        MS-PROT-043-V14-DQ-007 are resolved; jurisdiction-specific statutory periods outside that communication baseline,
        provider SDKs, SQL/anonymisation mechanics, operational tooling and API representation remain downstream.

RESOLVED — MS-PROT-079 TARGET 18 — EVENTS / BACKGROUND PROCESSES
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    event/reaction and background-work production-gap classification, identity/acknowledgement/ordering/retry/timer/
    reconciliation review, falsification, ambiguity review and complete two-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-18 two-authority approval
    Resolving authorities:
        MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment
        designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md
        MS-PROT-065 v1.1 — Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment
        designs/MS-PROT-065 v1.1 — Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment.md
    Accepted result:
        Domain Events remain already-occurred owner-qualified facts, while production publication and consumption are made
        explicit through exact Event Contracts and independently registered EventReactionContracts. One event may have multiple
        independently progressing reactions; completion acknowledgement is per reaction rather than global; duplicate delivery
        converges on one logical reaction; ordering is scoped only where semantics require it; arbitrary listener code does not
        create authority. Durable background responsibility is governed by owner-qualified BackgroundWorkContracts, stable
        DurableWorkInstructions and separately identified WorkAttempts; work, attempt, downstream command and provider-effect
        identities remain distinct. Timers wake reevaluation rather than creating business authority; recurrence, missed-occurrence
        and overdue behaviour are owner-defined; stale work revalidates; lost acknowledgement and provider uncertainty resolve or
        reconcile rather than duplicate; retry exhaustion cannot silently discard responsibility. Projection updates, lifecycle
        reviews and provider reconciliation preserve their owning authorities and historical affinity. Broker, queue, scheduler,
        SQL, lease, retry-count and backoff technologies remain deferred implementation choices rather than Target-18 semantics.

RESOLVED — MS-PROT-079 TARGET 19 — OBSERVABILITY / RECONCILIATION
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    operational-evidence/reconciliation production-gap classification, ownership review, uncertainty/provider-affinity review,
    alerting/manual-intervention review, falsification, ambiguity review and complete two-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-19 two-authority approval
    Resolving authorities:
        MS-PROT-068 v1.1 — Production Operational Evidence, Health & Alerting Contract Amendment
        designs/MS-PROT-068 v1.1 — Production Operational Evidence, Health & Alerting Contract Amendment.md
        MS-PROT-069 v1.1 — Production Reconciliation Responsibility, Evidence & Resolution Contract Amendment
        designs/MS-PROT-069 v1.1 — Production Reconciliation Responsibility, Evidence & Resolution Contract Amendment.md
    Accepted result:
        operational evidence remains distinct from business truth while materially important production responsibilities
        gain owner-qualified OperationalEvidenceContracts with bounded diagnosability, safe correlation, explicit evidence
        freshness/UNKNOWN handling and alerting that requests attention without direct mutation authority. Provider Health,
        Provider Readiness, execution outcome and provider evidence remain separate; stuck Event Reactions, Durable Work,
        projection convergence, lifecycle convergence and unresolved uncertainty become operationally discoverable without
        telemetry owning their state. Reconciliation becomes an owner-qualified contract over an exact uncertain/discrepant
        subject and historical execution path; it is distinct from retry, compensation and projection repair; admissible
        evidence is authenticated/correlated/interpreted before owner action; historical provider/binding/connection affinity
        survives provider change; automatic resolution requires deterministic sufficient evidence; manual resolution uses
        registered owner operations plus Audit and cannot force reality. Completion is defined by the exact reconciliation
        question rather than WorkAttempt or alert acknowledgement; unresolved evidence remains uncertain and may escalate
        without inventing success/failure. Exact telemetry products, metric names, dashboards, provider reconciliation APIs,
        operator UI, scheduler cadence and thresholds remain deferred implementation/API choices.

RESOLVED — MS-PROT-079 TARGET 20 — PRODUCTION APIS
    Governed entirely in chat through accepted-authority trace, implementation-evidence review,
    surface/trust-boundary classification, idempotency/concurrency/outcome/error review,
    Projection/Exposure/callback/media/resource-protection review, initial portfolio review,
    compatibility review, falsification, ambiguity review and complete-authority approval.
    Manual approval:
        27 August 2026 — approved as the complete Target-20 authority
    Resolving authority:
        MS-PROT-035 v1.1 — Production API Contract Registration, Transport Outcome & Initial Surface Portfolio Amendment
        designs/MS-PROT-035 v1.1 — Production API Contract Registration, Transport Outcome & Initial Surface Portfolio Amendment.md
    Later accepted bounded-query refinement:
        MS-PROT-027 v1.13 — Bounded Projection Read Material & Representation Affinity Amendment
    Later accepted installed-client compatibility refinement:
        MS-PROT-093 v1.0 — First-Party Merchant Client Architecture, Installed-Client Compatibility & Native Delivery Amendment
    Accepted result:
        production APIs are explicit audience- and owner-qualified transport contracts over accepted
        application operations and projections rather than CRUD/domain serialisation. PUBLIC,
        CUSTOMER_CONTEXTUAL, MERCHANT_OPERATIONAL, PLATFORM_IDENTITY_BOOTSTRAP,
        PLATFORM_ADMINISTRATIVE and INTEGRATION_INGRESS remain distinct trust surfaces; staff uses
        merchant operational semantics under staff authority rather than a parallel Staff business API;
        guest contextual access remains distinct from PUBLIC; bootstrap/platform operation requires no fake
        Merchant Scope. Trusted scope/principal is established server-side; identifiers do not grant access.
        Client retry identity is contract/scope-affined, reused different intent conflicts, optimistic
        concurrency remains owner-authoritative, and transport distinguishes COMPLETED,
        ACCEPTED_PENDING, REJECTED and OUTCOME_UNCERTAIN. Stable safe problem categories compose
        existing runtime owner classifications without making HTTP status business authority. Queries
        consume Projection Serviceability and Exposure, growing collections remain bounded, callback
        authentication/correlation remains evidence ingress rather than business truth, and media upload
        remains distinct from validated asset/attachment/Exposure. v1.13 requires query representation to
        select exposed values from the exact P2-governed bounded Projection read rather than re-querying
        current owner state after Exposure. MS-PROT-093 additionally requires independently installed first-party
        client representation compatibility, explicit CLIENT_UPDATE_REQUIRED handling and deterministic client-contract/
        SDK derivation without moving semantic authority into client tooling. Initial production API portfolio is registered
        across bootstrap, merchant, public, customer/guest, integration and platform-admin responsibilities. Exact routes,
        JSON, HTTP mapping, headers, concrete codegen technology, pagination encoding and media transport mechanics remain implementation choices.

RESOLVED — MS-PROT-079 TARGET 21 — BACKUP / RESTORE / DISASTER RECOVERY
    Governed entirely in chat through accepted recovery-authority trace, implementation-evidence review,
    later-authority compatibility review, exact-semantic-release/Event-Reaction/post-restore-authority gap classification,
    corruption/recovery/provider/fencing review, falsification, ambiguity review and complete-authority approval.
    Manual approval:
        28 August 2026 — approved as the complete Target-21 recovery-architecture amendment
    Resolving authority:
        MS-TAS-RECOVERY-001 v1.1 — Exact Release, Durable Reaction & Post-Restore Authority Reconciliation Amendment
        docs/development/TAS/backup-restore-disaster-recovery-v1.1-amendment.md
    Accepted result:
        v1.0’s active-primary/pilot-light topology, coherent PostgreSQL PITR, canonical-media recovery,
        secret separation, provider reconciliation, fencing, validation and exercise cadence survive.
        Recovery now requires exact source-release definition evidence/materialisation where genuinely required,
        keeps materialisability distinct from executable support, restores Domain Event publication and individual
        Event Reaction progression without global acknowledgement or broker authority, preserves DurableWork/
        WorkAttempt/downstream-intent identity, reconciles uncertain provider effects with historical path affinity,
        and prevents PITR from resurrecting later non-resurrectable privacy/security/terminal Merchant Account/
        ended Controller authority. Candidate validation and recovery exercises must prove those invariants.
        Concrete cloud/backup/KMS/archive/runbook/operator tooling and any future active-active design remain deferred.
```

EAC-001, EAC-002, EAC-003, the Order/Ordering authority gap, the Booking/Appointment execution-contract gap and MS-PROT-079 Targets 1–21 are closed. The binding MS-PROT-079 remaining-backend design programme is complete. Future gaps still enter the governed lifecycle; programme completion does not itself authorise implementation.

`IMP-06-E3-DG-002`, `IMP-06-E4-DG-001`, `IMP-06-E4-DG-002`, `IMP-06-S2-T1B4-DG-001` and `IMP-06-S3-DG-001` are resolved. No current promoted E3/E4/S2/T1b4/S3 design gate blocks the approved IMP-06 implementation sequence.

Composite MS-PROT-082 through v1.1 is accepted post-baseline authority but is not currently implementation-promoted. DQ-001 and DQ-002 are resolved by v1.1; DQ-007 is resolved compositionally by composite MS-PROT-083 + MS-PROT-084 v1.1. DQ-003 through DQ-006 retain their Section 4.14 states and do not reprioritise the current production implementation graph.

Composite MS-PROT-083 through v1.8 is likewise accepted post-baseline authority but is not currently implementation-promoted. `MS-PROT-083-DQ-001` is resolved by the v1.1 Campaign, v1.2 Customer-Return and v1.3 initial general BI portfolios; DQ-002 is resolved by v1.5; DQ-009 is resolved by v1.4; DQ-010 is resolved for the initial on-demand report/export portfolio by v1.6; and `MS-PROT-083-DQ-015` is resolved by MS-PROT-084 v1.1. v1.7 adds the Business Intelligence analytical-evaluation and supporting-presentation commercial classifications; v1.8 classifies the exact v1.6 report/export supporting delivery access paths with no independent Commercial Entitlement. Together they partially narrow `MS-PROT-056-V17-DQ-001`, change no MS-PROT-083 DQ state, and do not activate or reprioritise implementation. The remaining unresolved tail retains the Section 4.15 states.

Composite MS-PROT-084 through v1.2 is accepted post-baseline Financial Operations / Financial Evidence / Financial Health authority but is not currently implementation-promoted. v1.1 resolves DQ-001, DQ-002, DQ-004, DQ-013, DQ-014, DQ-015 and DQ-016 and leaves DQ-003, DQ-005, DQ-006, DQ-007, DQ-008, DQ-009, DQ-010, DQ-011 and DQ-012 deferred/inactive. v1.2 adds only the Financial Operations owner-qualified commercial-access classification and partially resolves `MS-PROT-056-V17-DQ-001`; it changes no MS-PROT-084 DQ state and does not activate or reprioritise implementation.

Composite MS-PROT-053 through v1.3 remains accepted lifecycle authority. v1.3 resolves `MS-PROT-043-V14-DQ-007` for Enquiry/Customer Communication retention qualification and periods but does not promote Data Lifecycle or Customer Messaging implementation and does not reprioritise the current production implementation graph.

Composite MS-PROT-085 through v1.4 is accepted post-baseline authority but is not currently implementation-promoted. v1.1 resolves `MS-PROT-085-DQ-001` in Section 4.16 by selecting the initial Enquiry review family. v1.2 adds exactly `customer-communication / human-response-required@1` as the Customer Communication human-response handling family required by the paired MS-PROT-086 v1.3 automated-response portfolio. v1.3 classifies the bounded initial-Enquiry-review commercial access without creating an independent Attention entitlement. v1.4 classifies the Customer Communication human-response Attention path as supporting access with no independent Attention entitlement while preserving exact Customer Communication commercial and actor requirements; together v1.3-v1.4 complete the current two-family Attention supporting-commercial classification without closing `MS-PROT-056-V17-DQ-001`. Merchant activation and implementation remain separately governed. It does not reprioritise the current production implementation graph.

Composite MS-PROT-086 through v1.4 is accepted post-baseline authority but is not currently implementation-promoted. v1.1 resolves `MS-PROT-086-DQ-001` by selecting Merchant Website Messaging plus Conversation-Bound Email. v1.2 resolves `MS-PROT-086-DQ-003` with distinct registered-participant and guest purpose-bound browser-access authority. v1.3 resolves `MS-PROT-086-DQ-002` with exactly seven fact-first customer-service response families, complete request coverage, owner-qualified evidence/access, deterministic response validation and the paired MS-PROT-085 v1.2 human-response Attention path. v1.4 supplies commercial classification only, changes no MS-PROT-086 DQ state, does not resolve ADR-014-DQ-011, does not activate Message attachments or Customer Communication implementation, and does not alter the implementation frontier. `MS-PROT-086-DQ-004` remains separately gated. Exact guest contextual-access credential representation remains `ADR-014-DQ-011 — ACTIVE BEFORE PRODUCTION GUEST CONVERSATION BROWSER ACCESS`. The accepted amendments do not reprioritise the current production implementation graph.

Composite MS-PROT-087 through v1.4 is accepted post-baseline authority but is not currently implementation-promoted. v1.1 resolves `MS-PROT-087-DQ-001` with exactly four initial purpose families and exactly `WEBSITE_ANNOUNCEMENT_V1` plus `DIRECT_EMAIL_MARKETING_V1`; standalone website Announcements remain Publication-owned and the initial direct Campaign portfolio remains relationship-based. v1.2 resolves `MS-PROT-087-DQ-002` with exactly three relationship/recency Audience Definition families using CustomerContext plus owner-qualified Order, Booking and Appointment commitment evidence while preserving fail-closed `UNRESOLVED` negative-evidence handling and anti-CRM boundaries. `MS-PROT-087-DQ-003` and `MS-PROT-087-DQ-004` are resolved together by v1.3 / `MKT-GRP-01`; `MS-PROT-087-DQ-005` is resolved by MS-PROT-083 v1.1 / `MKT-GRP-02` with a deterministic direct-execution measurement portfolio and no commercial-causation authority. v1.4 supplies Marketing commercial classification only: exactly `marketing/campaign-service-access@1` / `CONDUCT_MARKETING_CAMPAIGNS` is protected for GROWTH, while bounded non-committing preparation, existing Marketing-state observation and Campaign restriction require no independent Commercial Entitlement. It changes no Marketing DQ state, mints no final entitlement identity, leaves `MS-PROT-056-V17-DQ-001` OPEN and does not alter the implementation frontier. `MS-PROT-087-DQ-006` remains the sole retained Marketing production gate.

Composite MS-PROT-081 through v1.4 is accepted post-baseline Workforce Scheduling/Timekeeping/Leave authority but is not currently implementation-promoted. v1.2 resolves `MS-PROT-081-DQ-021`; v1.3 partially resolves `MS-PROT-081-DQ-015` for the bounded Scheduling/Leave/Scheduled Work reminder Notification Contract portfolio while leaving the exact Timekeeping notification/reminder remainder deferred pending DQ-009 through DQ-012; v1.4 resolves `MS-PROT-081-DQ-020` for owner-qualified commercial-access classification while leaving concrete entitlement identities and the complete catalogue under `MS-PROT-056-V17-DQ-001`. None of these amendments activates or reprioritises production implementation.

Composite MS-PROT-080 through v1.4 is accepted post-baseline Workforce Compensation/Payroll authority but is not currently implementation-promoted. v1.4 resolves `MS-PROT-080-V11-DQ-016` for owner-qualified commercial-access classification while leaving concrete entitlement identities and the complete catalogue under `MS-PROT-056-V17-DQ-001`. The remaining Workforce Compensation deferred tail retains the Section 4.12 states and no implementation activation follows.

MS-PROT-091 through v1.1 is accepted post-baseline Workforce Rota authority but is not currently implementation-promoted. v1.1 supplies bounded owner-qualified commercial-access classifications without creating concrete entitlement identities, closing the complete catalogue or activating implementation.

Composite MS-PROT-050 through v1.6 is accepted Business Hours authority. v1.5 supplies dated-override mutation/currentness authority and v1.6 supplies bounded owner-qualified commercial-access classifications. Neither closes `MS-PROT-056-V17-DQ-001` or activates implementation.

MS-PROT-093 v1.0 is accepted post-baseline first-party merchant-client architecture but is not currently implementation-promoted. It resolves `ADR-014-DQ-014` and the former first-party merchant-client SDK-generation deferral, admits merchant-web plus Android/iOS/Windows/macOS delivery classes, and constrains any later client implementation through installed-client compatibility, generated-artifact provenance, conformance, native-session and offline-authority rules. It does not reprioritise the current production implementation graph or close workforce-specific DQ-019.

A future gap MUST still enter the governed lifecycle before implementation may invent its answer.

---

## 6. Work-Queue Rule

The current design backlog is derived from:

```text
explicit unresolved/deferred items in this register
+
deliberately promoted future-scope items
+
accepted sequencing authorities activating a design target
+
new ambiguity/evidence discovered through design or implementation
```

Do not infer an open decision merely because:

- an old Git revision showed `OPEN_DESIGN`;
- an earlier historical DDR amendment predates its closure;
- an accepted design leaves implementation detail downstream;
- a filename/version is older than a later semantic amendment; or
- a product/PRD document contains functionality not yet promoted for semantic design.

---

## 7. Promotion Rule

A newly discovered gap enters this register only when review determines that it materially requires a semantic, architectural or production-architecture decision.

Promotion means:

```text
needs governed design work
```

Promotion does **not** mean:

```text
proposed answer accepted
new capability accepted
document number reserved permanently
implementation authorised
```

---

## 8. Closure Rule

When an accepted design or production-architecture authority resolves or materially narrows an item:

```text
formalise accepted authority
    ↓
update AUTHORITY-INDEX.md
    ↓
update this register
    ↓
update CANONICAL-SEMANTIC-LEXICON.md where applicable
    ↓
review IMPLEMENTATION-RULES.md impact
    ↓
run DESIGN-CORPUS-CONFORMANCE.md
```

The register entry SHOULD identify the resolving authority rather than restating the full solution.

---

## 9. Historical Provenance

Earlier DDR states, rationales and closure steps remain recoverable through Git history and accepted resolving authorities.

Obsolete standalone DDR closure-amendment/current-view files are not required once their current status is integrated here because they are governance-navigation revisions, not independent semantic or TAS authorities.

This differs deliberately from scope-aware MS-PROT amendment chains, whose separate provenance may be required to determine current semantic meaning.

---

## 10. Hard Invariants

1. `DEFERRED-DECISION-REGISTER.md` is the only current DDR authority.
2. Future DDR revisions MUST be merged into this file.
3. Git history is the DDR revision provenance.
4. Accepted authorities, not DDR prose, own substantive resolved meaning within their layer/scope.
5. Historical open status MUST NOT reopen an item shown resolved here without a later accepted reopening authority.
6. Promotion to the queue MUST NOT be treated as acceptance of the proposed answer.
7. Resolved decisions MAY retain narrower future scope without reopening the parent decision.
8. `AUTHORITY-INDEX.md` MUST remain the source for current accepted authority navigation.
9. This register MUST be updated when an accepted decision changes current work-queue status.
10. Where an accepted sequencing authority activates a design target, the target becomes current without pre-accepting a substantive answer to any gap discovered within it.
11. Stable deferred-question identifiers introduced by an accepted authority MUST remain traceable in this canonical register until resolved, superseded or explicitly withdrawn by accepted authority.
12. A successor implementation-discovered design gate MUST NOT retroactively reopen a correctly resolved predecessor gate; the new gap is tracked independently unless accepted authority explicitly supersedes the earlier resolution.

---

## 11. Current Next Action

MS-PROT-079 governed and Design-Closed the remaining-backend design programme. Later implementation-discovered amendments, including composite MS-PROT-027 through v1.14, MS-PROT-051 v1.5, MS-PROT-049 v1.4, the accepted composite Workforce Scheduling/Timekeeping/Leave/Compensation-affinity package through MS-PROT-081 v1.4 and MS-PROT-080 v1.4, MS-PROT-091 through v1.1, composite MS-PROT-050 through v1.6, composite MS-PROT-082 through v1.1, composite MS-PROT-083 through v1.8, composite MS-PROT-084 through v1.2, composite MS-PROT-053 through v1.3, composite MS-PROT-085 through v1.4, composite MS-PROT-086 through v1.4, composite MS-PROT-087 through v1.4, accepted MS-PROT-090 v1.0 and accepted MS-PROT-093 v1.0 refine the accepted corpus without reopening the completed 21-target programme or reprioritising production implementation.

The binding design sequence remains complete:

```text
FOUNDATIONAL EXECUTION
1. Production semantic-release bootstrap       — DESIGN-CLOSED by ADR-013
2. Authentication/session establishment        — DESIGN-CLOSED by ADR-014; installed-native session transport refined by MS-PROT-093
3. Initial merchant configuration bootstrap    — DESIGN-CLOSED by composite MS-PROT-040 through v1.5
        ↓
MERCHANT DEFINITION
4. Onboarding engine                            — DESIGN-CLOSED by MS-PROT-052 v1.2
5. Merchant Profile / Location                  — DESIGN-CLOSED by composite MS-PROT-051 through v1.5
6. AI inference boundary                        — DESIGN-CLOSED by composite MS-PROT-057 through v1.2
        ↓
READ / ACCESS INFRASTRUCTURE
7. Projection contracts                         — DESIGN-CLOSED by composite MS-PROT-027 through v1.8; bounded-read implementation affinity refined by v1.13-v1.14
8. Exposure resolution                          — DESIGN-CLOSED by composite MS-PROT-027 through v1.12
9. Provider readiness                           — DESIGN-CLOSED by MS-PROT-048 v1.4
        ↓
PRIMARY BUSINESS CAPABILITIES
10. Publication / Enquiry                       — DESIGN-CLOSED by MS-PROT-046 v1.2 + MS-PROT-043 v1.4; generic participation-source boundary refined by MS-PROT-049 v1.4
11. Booking / Appointment / Scheduling          — DESIGN-CLOSED for the production baseline by composite MS-PROT-042 through v1.9; grouped retained semantic work is queued below
12. Ordering / Inventory                        — DESIGN-CLOSED by MS-PROT-058 v1.1 + MS-PROT-077 v1.1
        ↓
COMMERCIAL / FULFILMENT
13. Payment                                     — DESIGN-CLOSED by composite MS-PROT-055 through v1.1
14. Order Fulfilment / Shipment                 — DESIGN-CLOSED by composite MS-PROT-060 through v1.1
15. Returns                                     — DESIGN-CLOSED by MS-PROT-061 v1.1 + MS-PROT-058 v1.2
        ↓
CROSS-CUTTING COMPLETION
16. Notification providers                     — DESIGN-CLOSED by MS-PROT-048 v1.5 + composite MS-PROT-075 through v1.2; provider execution closed by v1.1 and retained Notification policy/design tail closed by v1.2
17. Data protection lifecycle                  — DESIGN-CLOSED by composite MS-PROT-053 through v1.3; lifecycle/convergence base closed by v1.2 and Enquiry/Customer Communication retention qualified by v1.3
18. Events / background processes              — DESIGN-CLOSED by MS-PROT-026 v1.1 + MS-PROT-065 v1.1
19. Observability / reconciliation             — DESIGN-CLOSED by MS-PROT-068 v1.1 + MS-PROT-069 v1.1
        ↓
DELIVERY CONTRACTS
20. Production APIs                            — DESIGN-CLOSED by composite MS-PROT-035 through v1.1; bounded-query affinity refined by MS-PROT-027 v1.13 and installed-client compatibility/client-SDK scope refined by MS-PROT-093
        ↓
OPERATIONS
21. Backup / restore / disaster recovery       — DESIGN-CLOSED by composite MS-TAS-RECOVERY-001 through v1.1
```

**Current active MS-PROT-079 target:** None — the 21-target remaining-backend design programme is complete.

**Current promoted design work:** None for the E3/E4/S2/T1b4/S3 sequence. `IMP-06-E3-DG-002`, `IMP-06-E4-DG-001`, `IMP-06-E4-DG-002`, `IMP-06-S2-T1B4-DG-001` and `IMP-06-S3-DG-001` are resolved. The accepted composite MS-PROT-081 through v1.4 workforce package is not implementation-promoted: DQ-021 is resolved by v1.2, DQ-015 is partially resolved by v1.3 with the Timekeeping notification/reminder remainder deferred, and DQ-020 is resolved by v1.4 while the concrete entitlement manifest remains under `MS-PROT-056-V17-DQ-001`. Composite MS-PROT-080 through v1.4 and MS-PROT-091 through v1.1 are likewise not implementation-promoted; their accepted commercial-access classifications do not activate production. Composite MS-PROT-050 through v1.6 supplies dated-override ownership and Business Hours commercial classification without closing the Commercial catalogue. Composite MS-PROT-083 through v1.8 is also not implementation-promoted; v1.7 adds the Business Intelligence analytical-evaluation/presentation classifications and v1.8 adds the exact v1.6 report/export supporting-delivery classifications, with no production activation. Its resolved/deferred analytical tail remains governed by Section 4.15. Composite MS-PROT-082 through v1.1 and composite MS-PROT-084 through v1.2 are likewise not implementation-promoted; MS-PROT-084 v1.2 supplies Financial Operations commercial classification only and its unresolved semantic tail remains in Section 4.15A. Composite MS-PROT-053 through v1.3 lifecycle, composite MS-PROT-085 through v1.4 Merchant Attention, composite MS-PROT-086 through v1.4 Customer Communication, composite MS-PROT-087 through v1.4 Marketing and MS-PROT-093 first-party merchant-client architecture are likewise not implementation-promoted. ADR-014-DQ-011 remains the active-before-production guest credential-representation gate; ADR-014-DQ-014 is resolved by MS-PROT-093.

**Current implementation work under `IMPLEMENTATION-RULES.md`:** after the MS-PROT-049 v1.4 formalisation/corpus-conformance head verifies successfully, S3 generic Public Interaction participation-source and binding-projection infrastructure is the smallest READY production-code node. Its production source registry may be empty and must fail closed to zero bindings; tests must also prove the positive generic mechanics through test-only owner source fixtures. S3 must compose only independently established participation with already-governed serviceability/Exposure-selected material, reject wrong scope/release/contribution/subject affinity and duplicate ownership, preserve many-to-many participation, and own no availability or execution authority. Concrete `Opportunity → enquiry/send-enquiry` production participation remains with IMP-07 and must not be pulled into IMP-06.

T2d and T3c remain ON_DEMAND. T4c remains blocked on a real concrete query adapter and shall not be manufactured merely to close IMP-06. IMP-06 remains PARTIALLY_CONFORMING until S3 implementation and its closure cycle complete; IMP-07 remains HARD-blocked until formal IMP-06 completion. `MS-WATCH-002` remains active.

MS-PROT-084 v1.1 acceptance authorises semantic/design authority and governance composition only. MS-PROT-084 v1.2 additionally authorises Financial Operations commercial-access classification only. Neither activates Financial Operations implementation, financial-account provider integration, outgoing merchant payment execution, statutory accounting or any deferred financial expansion. MS-IMP expansion remains a later implementation-governance step.

MS-PROT-083 v1.7-v1.8 acceptance authorises Business Intelligence commercial-access classification and governance composition only. v1.7 protects new business/campaign analytical evaluation and exempts supporting presentation from an independent entitlement; v1.8 classifies the exact v1.6 PDF report and bounded single-measure CSV delivery access paths with no independent Commercial Entitlement. Neither activates Business Intelligence implementation, creates report/export availability where underlying analytical access or externalisation predicates fail, authorises new analytical methods or Campaign execution, permits source mutation, or activates any deferred analytical expansion.

MS-PROT-093 acceptance authorises client architecture and governance composition only. It does not by itself activate Android, iOS, Windows, macOS or merchant-web client implementation, choose native UI frameworks, set platform implementation sequence or alter the current backend implementation frontier.

Programme completion does not mutate or authorise production code, tests, migrations, provider state, backup infrastructure or live environments. `prototype/**`, storefront/merchant/customer UI, dashboard layouts/navigation, React/Next components, CSS, screen flows and presentation styling remain outside the completed backend-design programme.

---

## 12. Acceptance Statement

GrandRue maintains one canonical Deferred Decision Register with explicit design work ordered ahead of implementation assumptions.

> **One current queue, explicit resolving authorities, stable deferred-question traceability, Git history for register provenance, and no accidental reopening of settled design.**

---

## MS-PROT-042 Grouped Retained Semantic Work Queue

**Status:** QUEUED BY MANUAL DIRECTION ON 9 SEPTEMBER 2026
**Source authority:** MS-PROT-042 v1.9 §75
**Execution rule:** Each group is a separate design pass under current `designs/DESIGN-RULES.md`; grouping does not pre-approve any substantive answer.

| Order | Work package | Status | Scope / owner boundary |
|---:|---|---|---|
| 1 | `MS-PROT-042-GRP-02` — Appointment Proposal Expiry & Capacity Protection | **RESOLVED — MS-PROT-042 v1.10** | Deterministic TimeProposal deadline + explicit proposal-specific capacity protection; MS-PROT-006 remains historical evidence rather than wholesale promoted authority |
| 2 | `MS-PROT-042-GRP-05` — Merchant-Initiated Appointment Change & Customer Participation | **RESOLVED — MS-PROT-042 v1.11** | Customer-decision authority for merchant-initiated rescheduling + Appointment-owned arrival/check-in evidence and correction |
| 3 | `MS-PROT-042-GRP-04` — Appointment Assignment Continuity | **RESOLVED — MS-PROT-042 v1.12** | Appointment Support Requirement + staff/Resource substitution continuity; Appointment owns support meaning while Resource/Allocation owns concrete supporting capacity and Workforce owns worker availability |
| 4 | `MS-PROT-042-GRP-03` — Repeated & Shared Appointment Commitments | **RESOLVED — MS-PROT-042 v1.13** | Appointment Series + independently committed recurring occurrences + shared Appointment participation/access/attendance semantics |
| 5 | `MS-PROT-042-GRP-01` — Booking Outcome & Change Semantics | **RESOLVED — MS-PROT-042 v1.14** | Booking Natural Discharge Contract + Booking Utilisation Outcome + immutable Booking Commitment Revision and capacity-safe booked-subject/reservation-scope modification semantics |
| 6 | `MS-PROT-042-GRP-06` — Capacity-Demand Exceptions | **RESOLVED — MS-PROT-042 v1.15** | Waiting list admitted and reclassified to separately owned future `MS-PROT-089`; overbooking rejected for the initial portfolio; ordinary capacity invariants preserved |

MS-PROT-042 v1.10 resolves `MS-PROT-042-GRP-02` completely for the initial portfolio. No retained GRP-02 semantic DQ remains; the grouped queue advances to `MS-PROT-042-GRP-05`.

MS-PROT-042 v1.11 resolves `MS-PROT-042-GRP-05` completely for the initial portfolio. No retained GRP-05 semantic DQ remains; the grouped queue advances to `MS-PROT-042-GRP-04`.

MS-PROT-042 v1.12 resolves `MS-PROT-042-GRP-04` completely for the initial portfolio. No retained GRP-04 semantic DQ remains; the grouped queue advances to `MS-PROT-042-GRP-03`.

MS-PROT-042 v1.13 resolves `MS-PROT-042-GRP-03` completely for the initial portfolio. No retained GRP-03 semantic DQ remains; the grouped queue advances to `MS-PROT-042-GRP-01`.

MS-PROT-042 v1.14 resolves `MS-PROT-042-GRP-01` completely for the initial portfolio. No retained GRP-01 semantic DQ remains; the grouped queue advances to `MS-PROT-042-GRP-06`, which must first pass fresh Feature Admission and ownership reassessment before any waiting-list or overbooking authority is proposed.

MS-PROT-042 v1.15 resolves `MS-PROT-042-GRP-06` completely: bounded capacity waitlist coordination is admitted but reclassified out of MS-PROT-042 and promoted to the unapproved `MS-PROT-089` design node; overbooking is rejected for the initial portfolio and cannot be introduced by merchant policy, AI prediction or disguised capacity configuration. No retained semantic DQ remains in the MS-PROT-042 grouped v1.9 cleanup.

Historical v1.2 items concerning generic payment-policy ownership, late-cancellation policy authority, no-show commercial consequence authority and refund consequence ownership are not reopened: composite MS-PROT-042 v1.3 plus MS-PROT-055 already establish those ownership boundaries. Specific Booking catalogues, motel/allocation detail and physical resource-booking representations remain target composition/implementation work rather than this semantic queue.

The MS-PROT-042 grouped cleanup is complete. MS-PROT-089 v1.0 has now completed its separately governed lifecycle and resolves the admitted capacity-waitlist design node with no retained semantic DQ for the initial portfolio. Overbooking remains rejected for the initial portfolio under MS-PROT-042 v1.15. Sequencing therefore returns to the global `SEQUENCE.md` dependency frontier under current `designs/DESIGN-RULES.md`.

---

## IMP-05-R3B-DG-001 — Implementation-discovered design-gate outcome

Resolved by MS-PROT-040 v1.8.

Canonical outcome:

```text
Configuration reinstatement
    -> target must be a genuinely superseded previously activated revision
    -> exact current Reinstatement Basis Activation
    -> fresh basis-affined validation/package evidence
    -> fresh basis-affined impact review
    -> new current Controller approval carrying the same exact basis
    -> activation revalidates that the exact basis still owns the current pointer
    -> current compatibility / serving admission / concurrency
    -> new Configuration Activation fact
```

Revision identity alone is not a reinstatement basis.

Timestamp recency is not a reinstatement basis.

The original first-configuration approval cannot authorise reinstatement of a superseded initial revision.

A historical replacement or reinstatement approval cannot revive merely because the same Configuration Revision or same Controller Relationship becomes current again.

No Workforce, system, AI or scheduled reinstatement authority is created.
