# MS-PROT-079 — Remaining Backend Design Dependency Governance Model

**Document ID:** MS-PROT-079  
**Version:** 1.0  
**Status:** ACCEPTED  
**Approved:** Manual approval on 26 August 2026  
**Authority type:** Product/design sequencing authority  
**Governed by:** `DESIGN-RULES.md` v2.1 and `DOCUMENT-GOVERNANCE.md`  
**Depends on:** The current accepted semantic/design corpus navigated through `AUTHORITY-INDEX.md`; ADR-009 through ADR-012; MS-TAS-RECOVERY-001  

**Closes:** Remaining-backend design dependency-order ambiguity  
**Purpose:** Establish the mandatory scope, dependency graph, stage gates and completion discipline for the remaining Main Street backend **design**, while explicitly excluding prototype and presentation/UI implementation.

---

## 1. Governing Decision

Main Street SHALL complete the remaining backend design in explicit dependency order before treating prototype/UI development as the active programme.

The governing progression is:

```text
FOUNDATIONAL EXECUTION
        ↓
MERCHANT DEFINITION
        ↓
READ / ACCESS INFRASTRUCTURE
        ↓
PRIMARY BUSINESS CAPABILITIES
        ↓
COMMERCIAL / FULFILMENT
        ↓
CROSS-CUTTING COMPLETION
        ↓
DELIVERY CONTRACTS
        ↓
OPERATIONS
```

This is a **design dependency graph**.

It does not authorise implementation.

The programme is therefore:

```text
remaining backend design
        ↓
backend design completeness
        ↓
later implementation under IMPLEMENTATION-RULES
        ↓
prototype/final presentation work when separately activated
```

A later design target MUST NOT become the active completion target while an earlier required target remains materially unresolved.

---

## 2. Problem and Scope

Main Street already possesses a substantial accepted semantic and architectural corpus.

The remaining problem is not to rediscover the platform from first principles. The problem is to ensure that the accepted architecture is sufficiently complete, integrated and implementation-constraining across all required backend concerns before implementation or presentation work is allowed to fill gaps implicitly.

This authority governs design completion across:

```text
Domain / capability model

Application orchestration

Configuration and compiler

Runtime execution

Authentication / sessions / authority

Persistence / transactions

Background work

Domain events

Provider ports and adapters

Payments

Notifications

Media

Data protection

Read models / projections

Exposure decisions

Merchant/customer operational APIs

AI Concierge backend

Semantic-release/runtime bootstrap

Recovery / operational architecture
```

These are the required **backend design dimensions**.

They are not an alternative execution order.

The numbered dependency graph in Section 7 determines the work order through which these dimensions are closed.

---

## 3. Explicit Non-Goals

This authority does not govern the implementation or completion of:

```text
prototype/**

storefront-web prototype behaviour

merchant-web UI

customer/storefront UI

dashboard layouts

navigation

React/Next components

CSS

screen flows

presentation styling
```

It also does not:

- authorise production code;
- authorise test implementation;
- authorise migrations;
- authorise provider configuration;
- authorise live-environment changes;
- establish frontend technology;
- define final visual design;
- make business-category-specific backend models;
- redefine accepted semantic ownership;
- replace `IMPLEMENTATION-RULES.md`;
- convert a sequencing concern into semantic ownership; or
- require a new design authority where the existing accepted corpus already determines the required behaviour precisely.

A backend API, projection, Exposure contract or application operation remains within scope even when a future UI will consume it.

---

## 4. Canonical Terminology

### 4.1 Remaining Backend Design

**Remaining Backend Design** means the material semantic, application, runtime, infrastructure-boundary and operational architecture still required to make the Main Street backend implementation-constraining and internally coherent.

It does not mean remaining backend implementation.

### 4.2 Backend Design Dimension

A **Backend Design Dimension** is one of the cross-cutting design responsibilities listed in Section 2.

A dimension may span multiple numbered targets.

### 4.3 Design Target

A **Design Target** is one numbered node in the dependency graph whose applicable remaining design must be proven complete before progression.

### 4.4 Design Stage

A **Design Stage** is an ordered group of related Design Targets.

### 4.5 Design-Closed

A Design Target is **Design-Closed** when:

1. its applicable accepted authority is sufficient;
2. any required new/amended authority has passed `DESIGN-RULES.md`;
3. no unresolved material design ambiguity remains within required scope;
4. applicable conformance is satisfied; and
5. the closure does not depend on UI/prototype assumptions or future implementation invention.

Design-Closed does not mean implemented.

### 4.6 Dependency Repair

A **Dependency Repair** is design work required to repair an accepted cross-cutting authority that blocks the currently active target.

Dependency Repair does not count as advancing to the later numbered target that normally closes the same dimension.

---

## 5. Backend Design Completeness Model

Backend completeness is two-dimensional:

```text
DEPENDENCY ORDER
    numbered targets
        ×
DESIGN DIMENSIONS
    domain / orchestration / runtime /
    persistence / security / provider /
    events / projections / APIs / etc.
```

A target is not complete merely because its principal capability has a design document.

All applicable backend dimensions must also be determined.

Therefore:

```text
Target complete
    =
primary subject complete
+
applicable cross-cutting contracts complete
+
no material downstream invention required
```

---

## 6. Backend Design Dimension Mapping

The design dimensions have the following principal closure points.

| Backend design dimension | Principal closure location |
|---|---|
| Domain / capability model | Throughout; principally Targets 10–15 |
| Application orchestration | Targets 10–20, governed by existing orchestration authority |
| Configuration and compiler | Targets 3–6 |
| Runtime execution | Targets 1–3 and all executable capability targets |
| Authentication / sessions / authority | Target 2 |
| Persistence / transactions | Every state-mutating target; no standalone late deferral |
| Background work | Target 18, with earlier capability requirements defined when needed |
| Domain events | Target 18, with source-event contracts defined by owning capabilities earlier |
| Provider ports and adapters | Generic boundary at Target 9; concrete providers with owning targets |
| Payments | Target 13 |
| Notifications | Target 16 |
| Media | Principally Targets 5, 10, 17 and 20 |
| Data protection | Target 17, while constraining every earlier data-bearing design |
| Read models / projections | Target 7 |
| Exposure decisions | Target 8 |
| Merchant/customer operational APIs | Target 20 |
| AI Concierge backend | Foundation at Target 6; capability participation continues through later targets |
| Semantic-release/runtime bootstrap | Target 1 |
| Recovery / operational architecture | Target 21, while accepted recovery constraints apply throughout |

A cross-stage dimension MUST NOT disappear merely because it lacks a dedicated numbered target.

---

## 7. Mandatory Remaining Backend Design Dependency Graph

The following order is normative:

```text
FOUNDATIONAL EXECUTION
│
├── 1. Production semantic-release bootstrap
├── 2. Authentication/session establishment
└── 3. Initial merchant configuration bootstrap
        ↓
MERCHANT DEFINITION
│
├── 4. Onboarding engine
├── 5. Merchant Profile / Location
└── 6. AI inference boundary
        ↓
READ / ACCESS INFRASTRUCTURE
│
├── 7. Projection contracts
├── 8. Exposure resolution
└── 9. Provider readiness
        ↓
PRIMARY BUSINESS CAPABILITIES
│
├── 10. Publication / Enquiry
├── 11. Booking / Appointment / Scheduling
└── 12. Ordering / Inventory
        ↓
COMMERCIAL / FULFILMENT
│
├── 13. Payment
├── 14. Order Fulfilment / Shipment
└── 15. Returns
        ↓
CROSS-CUTTING COMPLETION
│
├── 16. Notification providers
├── 17. Data protection lifecycle
├── 18. Events / background processes
└── 19. Observability / reconciliation
        ↓
DELIVERY CONTRACTS
│
└── 20. Production APIs
        ↓
OPERATIONS
│
└── 21. Backup / restore / disaster recovery
```

The numeric order is binding for the remaining-backend design programme unless a later approved authority amends it.

---

## 8. Strict Dependency Rule

Target `N+1` MUST NOT become the active backend design target until Target `N` is Design-Closed.

A target is not Design-Closed merely because:

- an MS-PROT with a related title exists;
- some implementation already exists;
- tests already pass;
- a prototype demonstrates the happy path;
- a data model exists;
- an API can technically be built;
- the framework provides a default;
- an existing class appears adequate; or
- an engineer can infer a conventional answer.

Read-only inspection of later areas is permitted to identify dependencies and falsification evidence.

Such inspection MUST NOT create later-target authority or bypass the active target.

---

## 9. Foundational Execution

### 9.1 Target 1 — Production Semantic-Release Bootstrap

Target 1 closes the production design for obtaining, validating, reconstructing and making exact accepted Semantic Registry Releases executable across startup/deployment/restart.

It must preserve:

- exact semantic release identity;
- no implicit `latest` substitution;
- semantic-release immutability;
- compatibility with active/historical configuration affinity;
- exact executable-support evidence;
- bootstrap failure behaviour;
- runtime readiness;
- restart/deployment semantics; and
- recovery compatibility.

ADR-010, ADR-011 and ADR-012 remain upstream accepted authority.

Target 1 determines the remaining concrete production bootstrap architecture without redefining MS-PROT-054 semantics.

### 9.2 Target 2 — Authentication / Session Establishment

Target 2 closes backend design for establishing trusted execution principals and sessions.

It includes applicable:

- Merchant Controller identity;
- Merchant Membership;
- staff/delegated execution;
- customer/account principals;
- session establishment;
- revocation;
- credential/trust boundary;
- operational-device context;
- tenant binding;
- suspension/closure consequences; and
- authorisation-context propagation.

Authentication MUST remain distinct from Actor Authorisation and capability-specific Operational Eligibility.

### 9.3 Target 3 — Initial Merchant Configuration Bootstrap

Target 3 closes the first authoritative Merchant Configuration establishment path.

It includes:

```text
merchant account established
        ↓
initial configuration intent
        ↓
configuration revision
        ↓
semantic release affinity
        ↓
deterministic validation / compilation
        ↓
resolved configuration package
        ↓
approval
        ↓
atomic activation
```

Initial bootstrap MUST NOT manufacture business policy from implementation defaults.

---

## 10. Merchant Definition

### 10.1 Target 4 — Onboarding Engine

Target 4 closes the backend design for structured onboarding progression.

It must determine:

- question sequencing;
- registered options;
- conditional next-question selection;
- persistence of onboarding progress where authoritative;
- configuration candidate generation;
- merchant correction;
- retry/resume behaviour;
- incomplete onboarding;
- final approval boundary; and
- hand-off into authoritative configuration.

Business category remains onboarding context, not semantic owner.

### 10.2 Target 5 — Merchant Profile / Location

Target 5 closes backend design for merchant public/operational profile and location participation.

It includes applicable:

- authoritative merchant profile facts;
- location facts;
- contact/public information;
- physical/online applicability;
- maps/location-provider boundary;
- projection relationship;
- Exposure relationship;
- media references; and
- verification/trust relationships without transferring semantic ownership.

### 10.3 Target 6 — AI Inference Boundary

Target 6 establishes the backend AI Concierge/specialist inference foundation.

AI may:

```text
interpret
infer
clarify
propose
explain
```

AI MUST NOT:

```text
invent semantics
register semantics
become business-fact authority
bypass deterministic validation
bypass merchant approval
write capability persistence directly
manufacture provider success
```

Target 6 does not imply that later capability-specific Concierge participation is already complete.

Every later capability target must close its own AI-operation boundary where AI participation applies.

---

## 11. Read / Access Infrastructure

### 11.1 Target 7 — Projection Contracts

Target 7 closes generic and required backend projection contracts.

It must determine applicable:

- authoritative source;
- projection identity;
- freshness;
- staleness;
- rebuild;
- serviceability;
- failure;
- reconciliation;
- access requirements; and
- non-authoritative status.

Projection MUST NOT become mutation authority.

### 11.2 Target 8 — Exposure Resolution

Target 8 closes backend design determining whether already-valid information may be exposed to a specific surface/audience.

Exposure remains distinct from:

```text
Semantic Applicability
Commercial Entitlement
Actor Authorisation
Operational Eligibility
Provider Readiness
Projection freshness
```

Exposure MUST NOT redefine source business truth.

### 11.3 Target 9 — Provider Readiness

Target 9 closes the generic provider-participation architecture.

It includes:

- provider port boundaries;
- provider connection/readiness evidence;
- credential/secret relationship;
- provider unavailability;
- retries and uncertainty;
- callbacks;
- health/readiness;
- degradation; and
- separation of provider evidence from Main Street business truth.

Target 9 does not require every concrete provider integration to be fully designed.

Concrete provider-specific semantics remain with the capability/process that requires them.

---

## 12. Primary Business Capabilities

### 12.1 Target 10 — Publication / Enquiry

Target 10 closes backend design for:

- publication/information lifecycle;
- public information content;
- media participation;
- public projection/Exposure;
- customer enquiry establishment;
- enquiry continuation;
- anonymous/customer-context behaviour;
- operational attention;
- channel convergence; and
- AI Concierge participation where applicable.

Publication and Enquiry remain separate authorities.

### 12.2 Target 11 — Booking / Appointment / Scheduling

Target 11 closes the integrated backend execution design for:

```text
Scheduling
Booking
Appointment
Business Hours
Calendar projections/integration
Resource availability/allocation relationships
```

It must preserve their accepted ownership distinctions.

A Booking MUST NOT silently become Appointment authority.

Calendar MUST NOT become Scheduling authority.

Resource Allocation MUST NOT be conflated with the customer's booked subject.

### 12.3 Target 12 — Ordering / Inventory

Target 12 closes the backend design for the commerce commitment and stock relationship.

It includes:

- Product/Offering/Variant references;
- Order Commitment;
- Order Commitment Portions;
- Inventory Claims;
- stock availability;
- stock mutation;
- order amendment/release;
- channel convergence;
- atomic stock-protection invariants;
- retry/idempotency; and
- historical committed truth.

Inventory MUST NOT become Order authority and Order MUST NOT become Inventory authority.

---

## 13. Commercial / Fulfilment

### 13.1 Target 13 — Payment

Target 13 closes backend payment design across applicable business commitments.

It must preserve:

```text
business commitment
≠
Payment Obligation
≠
provider payment attempt/evidence
≠
settlement
≠
Refund
```

Payment-provider evidence MUST NOT manufacture or rewrite the business commitment it relates to.

### 13.2 Target 14 — Order Fulfilment / Shipment

Target 14 closes backend design for satisfaction of Order commitments and physical movement.

It must preserve:

```text
Order truth
≠
Order Fulfilment truth
≠
Shipment truth
```

Merchant delivery policy, provider logistics evidence and fulfilment completion remain separately governed.

### 13.3 Target 15 — Returns

Target 15 closes the currently required return boundary, including:

- merchant return policy;
- customer return contact;
- eligibility/policy relationship;
- downstream fulfilment consequences;
- return-label provider boundary where applicable;
- payment/refund relationship; and
- explicit limits of current reverse-fulfilment semantics.

Returns MUST NOT become a hidden universal reverse-order aggregate.

---

## 14. Cross-Cutting Completion

### 14.1 Target 16 — Notification Providers

Target 16 closes production notification-provider design.

The owning capability/process determines **why** notification exists.

Notifications owns delivery responsibility/evidence.

It does not own:

- underlying business fact;
- customer acknowledgement;
- consent;
- Exposure; or
- provider business semantics.

### 14.2 Target 17 — Data Protection Lifecycle

Target 17 closes backend data-protection lifecycle design across the completed capability portfolio.

It includes applicable:

- purpose;
- retention;
- erasure;
- anonymisation;
- redaction;
- durable evidence;
- media implications;
- closure consequences;
- historical commitments; and
- projection convergence.

Target 17 is a **completion stage**, not the first point at which data-protection rules apply.

Accepted data-protection authority constrains every earlier target that handles protected data.

### 14.3 Target 18 — Events / Background Processes

Target 18 closes the generic asynchronous execution model.

It includes:

```text
Domain Events
post-commit reactions
durable background work
timers
scheduled execution
retries
duplicate delivery
lost acknowledgement
revalidation
dead/unrecoverable work
```

Earlier capabilities MUST already define the events/background consequences material to their own semantics.

Target 18 completes the generic operational machinery and cross-capability consistency.

Events MUST NOT replace transactions required for atomic invariants.

### 14.4 Target 19 — Observability / Reconciliation

Target 19 closes backend design for determining what happened when distributed/provider/background execution is uncertain.

It includes:

- operational health;
- diagnostics;
- correlation;
- reconciliation;
- provider uncertainty;
- background-work visibility;
- projection lag;
- recovery evidence; and
- operator intervention boundaries.

Observability evidence MUST NOT become business authority merely because it reports business activity.

---

## 15. Delivery Contracts

### 15.1 Target 20 — Production APIs

Target 20 closes production merchant/customer operational delivery contracts over the already-governed backend.

It includes applicable:

- merchant operational APIs;
- customer/storefront operational APIs;
- authentication/session binding;
- request identity;
- idempotency;
- command/query distinction;
- business rejection;
- authorisation rejection;
- entitlement rejection;
- conflict;
- provider/technical failure;
- projections;
- Exposure;
- pagination/versioning where semantically required; and
- AI Concierge application entry where applicable.

Target 20 does not design screens.

```text
API contract
        ≠
screen flow
        ≠
React component
        ≠
navigation
        ≠
visual layout
```

Earlier capability targets must already have defined their authoritative application operations.

---

## 16. Operations

### 16.1 Target 21 — Backup / Restore / Disaster Recovery

Target 21 closes the remaining operational architecture needed to preserve accepted business and semantic truth through recovery.

It includes applicable:

- backup;
- restore;
- corruption recovery;
- disaster recovery;
- recovery release identity;
- fencing;
- session invalidation;
- projection rebuild;
- reconciliation;
- recovery validation;
- promotion;
- operational exercises; and
- recovery evidence.

MS-TAS-RECOVERY-001 remains accepted authority.

Target 21 is a final completeness target, not permission for earlier design to ignore recoverability.

---

## 17. Cross-Stage Baseline Obligations

The numbered sequence controls **completion order**, not the date on which an invariant begins to matter.

Therefore, throughout every stage:

### Security and identity

Accepted authentication, credential, tenant-isolation and authorisation rules apply whenever relevant.

### Data protection

Accepted data-protection rules apply whenever protected data is handled.

### Persistence and transactions

Every authoritative mutation must identify its persistence/transaction boundary where material.

### Concurrency and idempotency

Every retriable/concurrent operation must define duplicate and race behaviour where material.

### Events and background work

Earlier capability designs must define required event/background semantics even though generic completion occurs at Target 18.

### Recovery

Earlier persistence/runtime designs must remain compatible with accepted recovery authority even though operational closure occurs at Target 21.

### Observability

An earlier target must define required business/reconciliation evidence where correctness depends on it even though generic observability completion occurs at Target 19.

A later stage MUST NOT be used as a justification for leaving a correctness requirement undefined in an earlier stage.

---

## 18. Per-Target Governed Design Lifecycle

Each numbered target proceeds through:

```text
IDENTIFY CURRENT ACCEPTED AUTHORITY
        ↓
DEFINE THE TARGET'S REMAINING DESIGN-COMPLETION QUESTION
        ↓
TRACE EXISTING AUTHORITY AND IMPLEMENTATION EVIDENCE
        ↓
CLASSIFY EACH GAP
        │
        ├── ALREADY SUFFICIENTLY GOVERNED
        │
        ├── TRUE IMPLEMENTATION DETAIL
        │
        ├── MATERIAL DESIGN UNDERSPECIFICATION
        │
        ├── AUTHORITY AMBIGUITY / CONTRADICTION
        │
        └── EXPLICITLY DEFERRED / NOT CURRENTLY APPLICABLE
        ↓
IF MATERIAL DESIGN IS REQUIRED:
        DESIGN / PROPOSE IN CHATGPT
        ↓
        REVIEW IN CHATGPT
        ↓
        FALSIFY IN CHATGPT
        ↓
        RECOMMEND IN CHATGPT
        ↓
        PRESENT COMPLETE PROPOSED AUTHORITY IN CHATGPT
        ↓
        EXPLICIT MANUAL APPROVAL
        ↓
        ONLY THEN FORMALISE IN REPOSITORY
        ↓
        UPDATE INDEX / DDR / LEXICON WHERE APPLICABLE
        ↓
        CORPUS CONFORMANCE
        ↓
TARGET CLOSURE REVIEW
        ↓
NEXT NUMBERED TARGET
```

No unapproved target design may leave a repository trace.

---

## 19. Target Closure Contract

A target may be recommended `DESIGN-CLOSED` only when all applicable conditions hold:

1. its governing semantic owner or owners are identifiable;
2. authoritative facts are uniquely owned;
3. scope and exclusions are explicit;
4. application operations are sufficiently defined;
5. configuration/runtime distinctions are explicit;
6. persistence and transaction boundaries are sufficiently defined;
7. cross-capability contracts are explicit;
8. lifecycle/fact semantics are explicit where required;
9. retry/idempotency/concurrency semantics are explicit where required;
10. business rejection and technical/provider failure remain distinguishable;
11. authentication/authorisation/entitlement distinctions are preserved;
12. provider participation does not redefine business truth;
13. Projection and Exposure do not redefine authoritative truth;
14. AI authority boundaries are explicit where applicable;
15. data-protection consequences are determined where applicable;
16. event/background consequences are determined where applicable;
17. historical affinity is preserved where applicable;
18. no material semantic question remains for implementation to invent;
19. any remaining implementation choices are genuinely behaviour-preserving implementation details;
20. UI/prototype assumptions are not required to determine backend behaviour; and
21. the applicable design corpus is self-sufficient.

A target is not implementation-complete merely because it is Design-Closed.

---

## 20. Dependency Repair and Reopen Rule

If work on Target `N` reveals that an accepted cross-cutting authority is insufficient:

```text
Target N remains active
        ↓
classify the blocking design gap
        ↓
repair/amend the owning authority through DESIGN-RULES
        ↓
return to Target N
```

This does not count as advancing to a later target.

If work on a later target demonstrates that an earlier target was wrongly closed:

```text
stop affected progression
        ↓
identify earliest affected target
        ↓
reopen that target
        ↓
repair governed authority
        ↓
re-evaluate downstream affected targets
```

Closure history MUST NOT be protected at the expense of correctness.

---

## 21. Prototype and Presentation Isolation

The backend design programme MUST NOT depend on presentation artefacts to determine business meaning.

The following remain outside this dependency graph:

```text
prototype/**
storefront-web prototype behaviour
merchant-web UI
customer/storefront UI
dashboard layouts
navigation
React/Next components
CSS
screen flows
presentation styling
```

These MAY later consume:

```text
application operations
production APIs
projections
Exposure decisions
```

They MUST NOT determine:

```text
semantic ownership
business invariants
transaction boundaries
authorisation
payment truth
provider truth
configuration meaning
lifecycle
```

---

## 22. Relationship to MS-PROT-078

MS-PROT-078 remains authoritative for:

- Adequate Capability Portfolio;
- Capability Adequacy;
- Composition Adequacy;
- AI Concierge Adequacy;
- Pilot Surface meaning;
- Live-Testing Entry Readiness;
- Live-Readiness Demonstration;
- controlled-wave safety expectations; and
- the rule that a successful isolated pilot is insufficient proof of Main Street readiness.

This authority changes only the **current programme order**.

The previous sequencing proposition:

```text
live-testing readiness dependency audit
        ↓
backend completion when evidence requires it
```

becomes:

```text
remaining backend design dependency completion
        ↓
later implementation / readiness activity under applicable authority
```

MS-PROT-078 does not cease to govern live-testing evidence.

---

## 23. Falsification Cases

### 23.1 Data protection appears too late

**Challenge:** Target 17 follows capabilities that already handle personal data.

**Result:** Survives.

Target 17 is completion of the portfolio-wide lifecycle, not first applicability. Accepted protection rules constrain earlier targets from the beginning.

### 23.2 Background work appears too late

**Challenge:** Booking, payment or onboarding may require timers/retries earlier than Target 18.

**Result:** Survives.

Earlier capability targets must specify their required timers/events. Target 18 completes the generic operational machinery.

### 23.3 Provider readiness appears before provider-dependent capabilities

**Challenge:** Concrete payment/notification providers are not known at Target 9.

**Result:** Survives.

Target 9 establishes the generic provider boundary/readiness semantics. Capability-specific adapters remain designed with their owner.

### 23.4 Projection design appears before capability completion

**Challenge:** Projections derive from capability truth.

**Result:** Survives because the underlying capability semantics already exist in the accepted corpus.

Target 7 closes the generic projection contract. Later targets remain responsible for capability-specific projection obligations.

### 23.5 Production APIs appear too late

**Challenge:** Capability design needs contracts earlier.

**Result:** Survives.

Earlier targets define authoritative application operations/internal contracts. Target 20 closes external production delivery contracts.

### 23.6 Backup/DR appears too late

**Challenge:** Persistence architecture must be recoverable before Target 21.

**Result:** Survives.

Existing recovery authority constrains earlier persistence/runtime design. Target 21 is operational-completeness closure.

### 23.7 Media has no numbered target

**Challenge:** Media could disappear from the sequence.

**Result:** Survives only because this authority explicitly classifies Media as a cross-stage Backend Design Dimension principally closed through Merchant Profile, Publication, Data Protection and API delivery.

### 23.8 AI Concierge has only an early inference target

**Challenge:** Operational Concierge behaviour spans all capabilities.

**Result:** Survives only if Target 6 is treated as the foundation and each later capability target closes its capability-specific AI participation.

### 23.9 Strict sequencing reduces parallel design speed

**Challenge:** Independent areas could be designed simultaneously.

**Result:** Accepted consequence.

The programme optimises for dependency coherence, traceability and prevention of downstream assumption rather than maximum parallel drafting throughput.

---

## 24. Trade-Offs and Consequences

### Chosen trade-off — dependency order over parallel completion

Benefit:

- fewer downstream assumptions;
- earlier discovery of foundational gaps;
- stronger traceability;
- cleaner implementation hand-off;
- reduced risk of frontend/prototype behaviour becoming accidental authority.

Cost:

- some independently analysable later concerns wait for earlier closure.

Accepted consequence:

Read-only investigation may occur ahead, but authority progression remains ordered.

### Chosen trade-off — dimensions plus stages

A single flat sequence cannot accurately represent concerns such as persistence, security, media or data protection that span many targets.

Therefore this authority uses:

```text
one ordered dependency graph
+
one cross-cutting completeness matrix
```

It deliberately avoids creating two competing sequences.

### Chosen trade-off — final UI excluded

This delays presentation work but protects the backend from being shaped around temporary screen behaviour.

Backend contracts should be stable enough that later merchant/customer surfaces are consumers, not semantic authors.

---

## 25. Deferred and Future Scope

This authority does not decide:

- production implementation sequencing after design completion;
- frontend implementation sequencing;
- final live-test wave ordering;
- exact framework/library choices that remain genuine implementation details;
- exact provider products where no material architecture decision is required;
- infrastructure-as-code implementation;
- cloud deployment product selection;
- final UI architecture; or
- post-backend-design launch scheduling.

Such matters require their applicable accepted authority when activated.

---

## 26. Conformance and Acceptance Criteria

This authority conforms only if:

```text
[ ] the 21 backend design targets remain present
[ ] numeric dependency order is explicit
[ ] design is distinguished from implementation
[ ] backend design dimensions are explicitly covered
[ ] prototype/** and UI/presentation work are excluded
[ ] cross-cutting concerns constrain earlier targets where applicable
[ ] provider readiness is distinguished from concrete provider implementation
[ ] internal application contracts are distinguished from Production APIs
[ ] AI inference foundation is distinguished from later capability-specific AI participation
[ ] Media remains an explicit backend design dimension
[ ] MS-PROT-078 live-readiness semantics survive
[ ] target closure cannot depend on implementation invention
[ ] downstream discoveries reopen the earliest affected design target
[ ] all material design follows DESIGN-RULES chat-first approval
[ ] no unapproved design enters the repository
```

After approval, governance completion also requires:

```text
MS-PROT-079 formalised
+
AUTHORITY-INDEX updated
+
DEFERRED-DECISION-REGISTER current-next-action updated
+
DOCUMENT-GOVERNANCE impact reviewed
+
CANONICAL-SEMANTIC-LEXICON reviewed where applicable
+
DESIGN-CORPUS-CONFORMANCE passed
```

---

## 27. Amendment and Supersession Effect

If approved, this authority SHALL:

1. establish the numbered graph in Section 7 as the current remaining-backend **design** dependency order;
2. supersede MS-PROT-078 only in its statement that production-backend work is selected according to live-testing evidence dependency;
3. preserve every substantive live-testing/readiness rule in MS-PROT-078;
4. replace the current DDR instruction that the immediate task is the Live-Testing Readiness Dependency Graph;
5. make **Target 1 — Production semantic-release bootstrap** the first active remaining-backend design target;
6. preserve all underlying accepted MS-PROT/TAS/ADR semantic and architectural authorities;
7. authorise no implementation;
8. authorise no prototype/UI work; and
9. require any future change to this sequence to pass the governed design lifecycle.

The resulting programme is:

```text
COMPLETE REMAINING BACKEND DESIGN
        ↓
1. Production semantic-release bootstrap
2. Authentication/session establishment
3. Initial merchant configuration bootstrap
4. Onboarding engine
5. Merchant Profile / Location
6. AI inference boundary
7. Projection contracts
8. Exposure resolution
9. Provider readiness
10. Publication / Enquiry
11. Booking / Appointment / Scheduling
12. Ordering / Inventory
13. Payment
14. Order Fulfilment / Shipment
15. Returns
16. Notification providers
17. Data protection lifecycle
18. Events / background processes
19. Observability / reconciliation
20. Production APIs
21. Backup / restore / DR
        ↓
BACKEND DESIGN COMPLETE
        ↓
LATER IMPLEMENTATION / PROTOTYPE / UI
UNDER THEIR APPLICABLE GOVERNANCE
```

> **Main Street shall finish the backend design in dependency order: establish the execution foundation, define the merchant, establish read/access infrastructure, close primary capability semantics, close commercial and fulfilment dependencies, complete cross-cutting infrastructure, define production delivery contracts, and finally close operational recovery architecture. Presentation work does not determine this backend design.**
