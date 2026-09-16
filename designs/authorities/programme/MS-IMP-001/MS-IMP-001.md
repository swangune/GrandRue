# MS-IMP-001 — Production Implementation Dependency Governance

**Document ID:** MS-IMP-001  
**Version:** 1.0  
**Status:** ACCEPTED  
**Approved:** Manual approval on 28 August 2026 after in-chat sequence proposal, review and implementation-governance classification  
**Authority type:** Implementation-programme governance  
**Governs:** Production implementation sequencing and implementation-node eligibility  
**Subordinate to:** Accepted Main Street semantic/design/TAS/ADR authority  
**Executed through:** `designs/IMPLEMENTATION-RULES.md`  
**Depends on:** completed MS-PROT-079 Targets 1–21; current Authority Index; current DDR; MS-IMPLEMENTATION-RULES-001  
**Purpose:** Define the dependency-complete implementation sequence Main Street shall follow now that the governed backend design programme is complete.

---

## 1. Governing Decision

Main Street SHALL NOT implement production capability code by:

```text
document number
capability popularity
UI visibility
ease of implementation
package order
```

Implementation SHALL proceed according to the smallest dependency-complete executable graph capable of proving the accepted architecture progressively.

Canonical:

```text
ACCEPTED DESIGN CORPUS
        ↓
MS-IMP-001 MACRO GRAPH
        ↓
dynamic fine-grained implementation graph
        ↓
smallest READY implementation node
        ↓
IMPLEMENTATION-RULES execution loop
        ↓
tests
        ↓
minimum code
        ↓
verification
        ↓
evidence
        ↓
commit
        ↓
refresh graph
```

---

## 2. What This Document Governs

MS-IMP-001 governs:

```text
macro implementation targets
dependency order
programme gates
parallel branches
target readiness
target completion
critical-path preference
```

It does NOT redefine:

```text
business semantics
capability ownership
provider responsibility
transactions
idempotency
Exposure
API meaning
business lifecycle
```

Those remain governed by substantive accepted authority.

---

## 3. Macro Target vs Execution Node

An `IMP-XX` target is a **macro implementation target**.

It MAY contain many smaller implementation nodes.

Example:

```text
IMP-12 — Ordering / Inventory

    ├── InventoryPosition persistence
    ├── stock adjustment
    ├── Inventory Claim
    ├── Order Commitment
    ├── concurrent commitment protection
    ├── amendment
    ├── release
    ├── projections
    └── APIs
```

The Implementation Rules SHALL still select the smallest coherent READY node.

Therefore:

```text
MS-IMP-001
    governs macro eligibility

Implementation Rules
    govern fine-grained execution
```

---

## 4. Edge Types

The graph recognises three implementation dependency types.

### HARD_DEPENDENCY

The downstream target cannot be implemented correctly without the upstream implementation.

```text
A ──HARD──> B
```

B cannot become READY until A is satisfied.

### PROGRAMME_GATE

The downstream implementation may be theoretically possible, but Main Street deliberately requires the upstream proof first because it materially reduces architectural or operational risk.

```text
A ──GATE──> B
```

A gate is binding implementation governance even though it is not business semantics.

### CONDITIONAL_DEPENDENCY

Required only where a particular sub-path uses that dependency.

```text
A ──CONDITIONAL──> B
```

Example:

```text
Provider Foundation
    required for external-calendar path

but not
    basic internal scheduling
```

---

## 5. Existing Implementation May Satisfy a Dependency

The repository is not assumed empty.

IMP-00 may classify an existing target or child node as:

```text
CONFORMING_COMPLETE
PARTIALLY_CONFORMING
ABSENT
CONFLICTING
```

Only `CONFORMING_COMPLETE` may satisfy an implementation prerequisite without new production code.

Existing code is evidence.

Its mere existence is not proof of conformance.

---

# IMPLEMENTATION GRAPH

## 6. IMP-00 — Current Implementation Baseline

Purpose:

> Establish exactly what production/test implementation already exists and whether it conforms to the now-complete authority corpus.

Required work:

```text
inspect production source
inspect tests
run full baseline suite
map implementation → authority
classify each macro target
identify accidental/prototype semantics
identify conflicting ownership
identify already-satisfied prerequisites
```

No broad refactoring belongs in IMP-00.

Output:

```text
Implementation Conformance Baseline
+
fine-grained initial dependency graph
```

Completion gate:

```text
current repository known
current tests known
authority coverage known
conflicting implementation identified
next READY nodes determinable
```

---

## 7. IMP-01 — Engineering & Conformance Foundation

Depends:

```text
IMP-00 HARD
```

Purpose:

Create the implementation environment required to falsify everything that follows.

Scope includes:

```text
reproducible build
architecture/conformance checks
module dependency checks
deterministic Clock/test support
production-representative PostgreSQL integration harness
migration verification harness
test fixture standards
authority-to-test traceability
full-suite baseline
```

Exact tooling remains implementation detail subject to dependency-adoption rules.

---

## 8. IMP-02 — Persistence & Semantic Release Foundation

Depends:

```text
IMP-01 HARD
```

Scope:

```text
PostgreSQL production persistence foundation
Flyway/migration baseline
transaction support
concurrency/version primitives
exact semantic-definition bundles
Published Semantic Definition evidence
SemanticReleaseAssembly persistence/materialisation support
packaged release integrity/provenance
exact-release bootstrap
```

Completion requires proving:

```text
exact release
    ≠ latest release

schema state
    ≠ semantic release

materialisable
    ≠ executable
```

---

## 9. IMP-03 — Semantic Execution Spine

Depends:

```text
IMP-02 HARD
```

Scope:

```text
Semantic registries
Capability Registry
Capability composition infrastructure
Configuration Compiler
Resolved Configuration Package
Operation Runtime
exact semantic/configuration affinity
registered operation execution
semantic compatibility checks
```

This is the executable semantic spine.

---

## 10. IMP-04 — Merchant Account, Scope, Identity & Trust

Depends:

```text
IMP-02 HARD
```

IMP-03 and IMP-04 MAY proceed independently after IMP-02.

Scope:

```text
Merchant Account
Controller establishment
Merchant Scope
authentication
WebAuthn/passkey implementation
sessions
Trusted Execution Context
trusted principal
credential/security foundation
baseline Audit
```

Completion must prove:

```text
identity
    ≠ authority

identifier
    ≠ Merchant Scope

session
    ≠ immutable merchant authority
```

---

## 11. IMP-05 — Merchant Definition, Configuration & Activation

Depends:

```text
IMP-03 HARD
IMP-04 HARD
```

Scope:

```text
Merchant Profile
Location
Business Hours configuration foundation
Onboarding Case
structured onboarding answers
AI proposal boundary where required
Initial Configuration Intent
Configuration Revision
approval
compilation
activation
entitlement
runtime applicability / eligibility
```

Completion milestone:

```text
Merchant
    can be established
        ↓
defined
        ↓
configured
        ↓
compiled
        ↓
activated
```

---

## 12. IMP-06 — Read, Exposure & Transport Spine

Depends:

```text
IMP-05 HARD
```

Scope:

```text
Projection Contract infrastructure
Projection Serviceability
Exposure resolution
Surface Contributions
Audience Observation Context
Resource Protection
API Contract infrastructure
trusted transport-context resolution
problem/outcome mapping foundation
bounded query/pagination infrastructure
```

No broad catalogue of endpoints is required yet.

---

# FIRST ARCHITECTURAL PROOF

## 13. IMP-07 — First Complete Vertical Slice: Publication → Enquiry

Depends:

```text
IMP-06 HARD
```

This is the first mandatory production vertical slice.

Path:

```text
Merchant established
        ↓
Merchant configured
        ↓
Merchant Profile exposed
        ↓
Merchant creates Publication
        ↓
Publication becomes publicly serviceable
        ↓
customer observes merchant/publication
        ↓
customer submits Enquiry
        ↓
merchant observes Enquiry
```

The slice SHALL include, as applicable:

```text
domain invariant
persistence
application operation
transaction
projection
Exposure
public API
merchant API
authority tests
scope tests
concurrency/idempotency tests where relevant
full integration path
```

### IMP-07 Programme Gate

No high-risk provider/financial implementation begins before IMP-07 demonstrates that the fundamental Main Street architecture works vertically.

---

# PARALLEL FOUNDATION BRANCHES

After IMP-07, three branches become eligible.

## 14. IMP-08A — Merchant Assistance Foundation

Depends:

```text
IMP-07 PROGRAMME_GATE
```

Scope:

```text
Media
PersonalDataUseBasis integration
AI inference boundary
AI proposal/approval boundary
merchant-side assistance
controlled website/content assistance
```

AI must remain non-authoritative.

---

## 15. IMP-08B — Workforce Foundation

Depends:

```text
IMP-07 PROGRAMME_GATE
IMP-04 HARD
```

Scope:

```text
Workforce Membership
invitation
roles/groups
delegation
operational device context
staff principal
staff attribution
Audit integration
```

Staff uses merchant operational semantics.

It does not create a parallel business model.

---

## 16. IMP-08C — Durable Execution Foundation

Depends:

```text
IMP-07 PROGRAMME_GATE
IMP-02 HARD
IMP-03 HARD
```

Scope:

```text
Domain Event occurrence
atomic publication responsibility
EventReactionContract execution
per-reaction acknowledgement
DurableWorkInstruction
WorkAttempt
retry classification
uncertainty
reconciliation progression
baseline Operational Evidence
```

This implementation MUST precede consequential provider work.

---

# PROVIDER PROOF

## 17. IMP-09 — Provider Foundation

Depends:

```text
IMP-08C HARD
IMP-05 HARD
IMP-04 HARD
```

Scope:

```text
ProviderConnection
credential references
Fulfilment Requirement
Fulfilment Binding
merchant/platform routing
Provider Readiness
historical provider affinity
authenticated callback ingress foundation
provider evidence boundary
```

No specific provider may redefine domain semantics.

---

## 18. IMP-10 — Notification Provider Vertical Slice

Depends:

```text
IMP-09 HARD
IMP-08C HARD
```

Purpose:

> Prove the external-provider execution architecture using a lower-business-risk effect before applying it to money or physical fulfilment.

Scope:

```text
NotificationIntent
NotificationDispatch
DeliveryAttempt
exact provider-effect identity
Provider Readiness
external provider adapter
provider callback/evidence
retry
execution uncertainty
reconciliation
operational evidence
```

### Programme Gate

```text
IMP-10 ──GATE──> IMP-13 Payment
```

Main Street proves provider execution on Notifications before financial provider execution.

---

# CORE BUSINESS STRESS TESTS

## 19. IMP-11 — Scheduling Vertical Slice

Depends:

```text
IMP-07 HARD
IMP-08C HARD
```

Scope:

```text
Business Hours
Calendar
Scheduling evaluation
Booking
Appointment
availability
concurrent commitment protection
rescheduling/cancellation where accepted
projections
Exposure
customer/merchant APIs
```

Conditional:

```text
IMP-09
    required for external Calendar/provider path

IMP-10
    required for Notification reactions
```

Scheduling must remain production-capable without requiring an external calendar provider where accepted semantics permit internal operation.

---

## 20. IMP-12 — Ordering & Inventory Vertical Slice

Depends:

```text
IMP-07 HARD
IMP-08C HARD
```

Scope:

```text
Offering/Product connection
Inventory Position
Inventory Movement
Inventory Claim
Order Commitment
quantity reservation/consumption
amendment
release
concurrency
historical provenance
projections
Exposure
public/customer/merchant APIs
```

Completion must include meaningful concurrent-order tests.

---

# COMMERCIAL / PHYSICAL CONSEQUENCES

## 21. IMP-13 — Payment

Depends:

```text
IMP-09 HARD
IMP-08C HARD
IMP-10 PROGRAMME_GATE
```

At least one real accepted Payment Obligation producer from IMP-11 or IMP-12 must be available before IMP-13 is considered complete.

Scope:

```text
Money
Payment Obligation
PaymentExecutionRequest
provider effect
ProviderPaymentEvidence
PaymentApplication
RefundExecutionRequest
refund evidence
idempotency
uncertainty
reconciliation
API outcomes
```

Payment remains independent from Order/Booking ownership.

---

## 22. IMP-14 — Order Fulfilment & Shipment

Depends:

```text
IMP-12 HARD
IMP-09 HARD
IMP-08C HARD
IMP-13 PROGRAMME_GATE
```

Scope:

```text
Order Fulfilment
Satisfaction Portion
Inventory consequence
Shipment
ShipmentPreparationRequest
provider preparation
tracking evidence
Shipment outcome
redispatch
uncertainty/reconciliation
```

Payment remains a programme gate, not semantic ownership of fulfilment.

---

## 23. IMP-15 — Returns

Depends:

```text
IMP-12 HARD
IMP-13 HARD
IMP-14 HARD
```

Scope:

```text
optional Returns configuration
Return Policy
merchant Return Decision boundary
ReturnLabelPreparationRequest
provider label preparation
Refund integration
replacement integration
ReturnedStockReceipt
ReturnedStockDisposition
SELLABLE_REENTRY
historical policy affinity
```

The implementation must preserve:

```text
Returns disabled
    ≠ merchant cannot accept return

Refund
    ≠ requires Returns

Inventory return receipt
    ≠ requires Returns
```

---

# SYSTEM-WIDE HARDENING

## 24. IMP-16 — Cross-System Lifecycle & Historical Hardening

May begin incrementally earlier.

Cannot complete until:

```text
IMP-08A COMPLETE
IMP-08B COMPLETE
IMP-08C COMPLETE
IMP-10 COMPLETE
IMP-11 COMPLETE
IMP-12 COMPLETE
IMP-13 COMPLETE
IMP-14 COMPLETE
IMP-15 COMPLETE
```

Scope:

```text
DataLifecycleContract execution
use-vs-retention enforcement
owner-safe disposition
Merchant Account suspension/closure
Controller lifecycle
residual authority
historical configuration affinity
historical semantic-release affinity
provider change affinity
entitlement loss
projection convergence
concurrency matrix
failure matrix
security negative paths
cross-capability idempotency review
```

This is not a substitute for earlier per-node testing.

It is the system-wide consistency pass.

---

# DELIVERY COMPLETION

## 25. IMP-17 — Complete Production API Portfolio

Depends:

```text
IMP-16 HARD
IMP-06 HARD
```

APIs already implemented vertically remain.

IMP-17 completes missing contracts across:

```text
PUBLIC
CUSTOMER_CONTEXTUAL
MERCHANT_OPERATIONAL
PLATFORM_IDENTITY_BOOTSTRAP
PLATFORM_ADMINISTRATIVE
INTEGRATION_INGRESS
```

Scope includes final:

```text
API Contract coverage
DTO boundaries
safe problem mapping
pagination/bounds
idempotency mapping
concurrency/preconditions
media transfer contracts
callback contracts
administrative reconciliation contracts
```

No generic CRUD or force-status API is permitted.

---

# PRODUCTION OPERABILITY

## 26. IMP-18 — Production Operability

Depends:

```text
IMP-16 HARD
IMP-17 HARD
IMP-09 HARD
IMP-08C HARD
```

Scope:

```text
production migrations
production provider adapters
background workers
scheduler/work execution
credentials/secrets wiring
Operational Evidence portfolio
health
alerts
reconciliation tooling
deployment semantic admission
runtime configuration
failure/degradation behaviour
```

Exact infrastructure products remain subordinate to accepted architecture/dependency rules.

---

# RECOVERY

## 27. IMP-19 — Backup, Restore & Disaster Recovery

Depends:

```text
IMP-18 HARD
IMP-16 HARD
IMP-02 HARD
IMP-03 HARD
IMP-08C HARD
```

Scope:

```text
backup
PITR
canonical-media recovery
semantic-release recovery
Event Reaction recovery
Durable Work recovery
provider-effect reconciliation
post-restore non-resurrectable authority
Recovery Candidate validation
fencing
promotion
restore/DR exercises
```

Completion requires an actual recovery exercise against the implemented architecture.

---

# LIVE READINESS

## 28. IMP-20 — Controlled Live-Readiness

Depends:

```text
IMP-00..IMP-19 COMPLETE
```

Purpose:

Determine whether implemented Main Street is ready for controlled live use.

Validate:

```text
Capability Adequacy
Composition Adequacy
AI Concierge Adequacy
security
provider behaviour
operational readiness
data lifecycle
recovery
failure behaviour
material merchant variation
business-type neutrality
```

Use materially different merchants, including:

```text
information-only publisher
service professional
appointment-led merchant
product retailer
hybrid merchant
bookable-resource merchant
```

Passing one merchant archetype is insufficient.

---

## 29. Canonical Dependency Graph

```text
IMP-00
  ↓
IMP-01
  ↓
IMP-02
  ├─────────────┐
  ↓             ↓
IMP-03        IMP-04
  └──────┬──────┘
         ↓
       IMP-05
         ↓
       IMP-06
         ↓
       IMP-07
      ┌──┼──┐
      ↓  ↓  ↓
   IMP-08A
      IMP-08B
         IMP-08C
            ↓
          IMP-09
            ↓
          IMP-10 ─────GATE────┐
                              │
IMP-07 + IMP-08C              │
      ├──────────────┐        │
      ↓              ↓        │
    IMP-11         IMP-12     │
      │              │        │
      └──────┬───────┘        │
             ↓                │
           IMP-13 ◄───────────┘
             ↓
           IMP-14
             ↓
           IMP-15

IMP-08A
IMP-08B
IMP-08C
IMP-10
IMP-11
IMP-12
IMP-13
IMP-14
IMP-15
    └───────────────┐
                    ↓
                  IMP-16
                    ↓
                  IMP-17
                    ↓
                  IMP-18
                    ↓
                  IMP-19
                    ↓
                  IMP-20
```

Conditional provider/calendar/notification subedges remain governed by their target contracts.

---

## 30. Ready-Target Rule

A macro target is READY only when:

```text
all HARD dependencies satisfied
all applicable PROGRAMME_GATES satisfied
required authorities accepted
no DESIGN_ESCALATION blocks it
repository baseline is known
its child-node graph can be derived
without inventing semantics
```

---

## 31. Existing-Code Satisfaction Rule

IMP-00 may prove that all or part of a later target is already conforming.

In that case:

```text
existing implementation
        +
required tests
        +
full conformance
        ↓
dependency satisfied
```

Main Street SHALL NOT reimplement correct code merely to follow the sequence ceremonially.

---

## 32. Vertical-Completion Rule

Implementation SHALL prefer:

```text
accepted use case
        ↓
domain
        ↓
persistence
        ↓
application
        ↓
projection
        ↓
Exposure
        ↓
transport
        ↓
security
        ↓
failure/concurrency/idempotency
        ↓
tests
```

over horizontal layer completion such as:

```text
all entities
then
all repositories
then
all services
then
all APIs
```

unless a foundational IMP target explicitly requires shared infrastructure first.

---

## 33. Target Completion Gate

An IMP target is COMPLETE only when all required child nodes have:

```text
governing authority traced
required tests
minimum conforming implementation
persistence/concurrency verification where applicable
negative-authority verification
idempotency/retry verification where applicable
provider-failure verification where applicable
architecture/conformance verification
full Maven suite success
implementation evidence
traceable commit
```

and no unresolved implementation/design blocker remains.

---

## 34. Design Escalation

If implementation discovers:

```text
MISSING_SEMANTIC_RULE
MISSING_OWNER
MISSING_OPERATION_CONTRACT
MISSING_FAILURE_BEHAVIOUR
MISSING_CONCURRENCY_RULE
MISSING_IDEMPOTENCY_RULE
MISSING_PROVIDER_CONTRACT
MISSING_EXPOSURE_RULE
CONTRADICTORY_AUTHORITY
```

the affected node becomes:

```text
BLOCKED_DESIGN
```

Its dependent nodes remain blocked.

Independent READY nodes may continue.

---

## 35. Automatic Implementation Authority

Once MS-IMP-001 is accepted and formalised, it authorises the `IMPLEMENTATION-RULES.md` automation to execute READY implementation nodes without seeking repeated manual permission, provided:

```text
the governing design is already accepted

the node remains inside MS-IMP-001

the change stays inside IMPLEMENTATION-RULES
automatic-change boundaries

no MANUAL_APPROVAL condition is encountered

no DESIGN_ESCALATION is encountered
```

This is implementation authorisation.

It is not authority to change the accepted design.

---

## 36. Graph Refinement

Implementation Rules MAY automatically:

```text
split a macro target into smaller child nodes
discover already-satisfied prerequisites
add implementation-only dependency edges
remove an edge proven unnecessary by existing
accepted implementation architecture
reorder READY child nodes
```

only where the refinement does not alter the macro programme gates or substantive accepted authority.

Changing:

```text
macro target meaning
HARD macro dependencies
PROGRAMME_GATE ordering
semantic ownership
provider responsibility
transaction/concurrency/idempotency meaning
```

requires the applicable approval lifecycle.

---

## 37. Evidence

Every completed macro target SHALL maintain implementation evidence including:

```text
target identity
governing authorities
child nodes
existing implementation reused
new implementation
tests
verification
design escalations encountered/resolved
commits
newly unlocked macro targets
```

The evidence is implementation history.

It is not semantic authority.

---

## 38. Current Programme State

Upon acceptance and formalisation:

```text
IMP-00 — READY

IMP-01..IMP-20 — PENDING /
BLOCKED_DEPENDENCY
```

IMP-00 is the only initial implementation target.

---

## 39. Acceptance Statement

> **Main Street implementation shall progress by proving the smallest dependency-complete executable architecture, then adding capability complexity vertically and only after its prerequisites are demonstrated. Existing conforming implementation is preserved; high-risk provider and financial effects are preceded by lower-risk architectural proofs; each completed target unlocks only the work that can now be implemented without guessing; and any newly discovered design gap blocks only its dependent branch rather than encouraging implementation to invent architecture.**