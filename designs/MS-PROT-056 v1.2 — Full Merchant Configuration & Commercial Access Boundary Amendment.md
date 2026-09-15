# MS-PROT-056 v1.2 — Full Merchant Configuration & Commercial Access Boundary Amendment

**Document ID:** MS-PROT-056  
**Version:** 1.2  
**Status:** **ACCEPTED after architecture trade-off review, onboarding-to-entitlement simulation and manual approval**  
**Amends:** MS-PROT-056 v1.0 and MS-PROT-056 v1.1  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.5, MS-PROT-023 v1.0, MS-PROT-026 v1.0, MS-PROT-028 v1.3, MS-PROT-037 v1.0, MS-PROT-038 v1.0, MS-PROT-040 v1.0, MS-PROT-047 v1.0, MS-PROT-049 v1.0, MS-PROT-052 v1.1, MS-PROT-054 v1.0, MS-PROT-056 v1.0, MS-PROT-056 v1.1  
**Purpose:** Formalise the boundary among complete merchant business configuration, the initial 30-day full-experience entitlement, post-trial subscription entitlements, applicable functionality, runtime access and capability-owned execution. This amendment prevents subscription plans from narrowing business discovery, rewriting merchant semantics, creating plan-specific architecture or forcing one universal feature-delivery mechanism.

---

# 1. Governing amendment

MS-PROT-056 v1.0 and v1.1 remain authoritative except where this amendment is more specific.

The governing decision is:

> **Main Street shall fully configure the merchant according to the supported needs of the business before commercial entitlement is used to determine ongoing access. Trial and subscription plans govern commercial access to applicable Main Street functionality; they shall not redefine, reduce, manufacture or recompile the merchant's semantic operating model. Each promised plan feature shall be implemented according to its actual semantic responsibility using the established composite architecture and the programming paradigm appropriate to that responsibility.**

Canonical separation:

```text
MERCHANT BUSINESS NEED
        ↓
ADAPTIVE DISCOVERY
        ↓
COMPLETE SUPPORTED MERCHANT CONFIGURATION
        ↓
DETERMINISTIC SEMANTIC COMPILATION
        ↓
RESOLVED MERCHANT OPERATING MODEL
        │
        ├─────────────── semantic applicability
        │
SUBSCRIPTION / TRIAL COMMERCIAL STATE
        ↓
EFFECTIVE COMMERCIAL ENTITLEMENTS
        │
        └─────────────── commercial permission
                        ↓
                ACCESS DECISION
                        ↓
          EXISTING CAPABILITY / SERVICE / SURFACE
                        ↓
              CAPABILITY-OWNED EXECUTION
```

Hard invariant:

> **Commercial packaging follows the merchant model; it does not define the merchant model.**

---

# 2. Full initial configuration is mandatory within supported scope

Main Street onboarding shall discover and configure everything required for the merchant's supported operating model, subject only to genuinely optional or future integrations that are not required to operate that model.

Rejected:

```text
current plan = FREE
        ↓
do not configure Booking although the business requires Booking
```

Rejected:

```text
trial merchant
        ↓
configure only enough for website presence
        ↓
finish the actual business model after upgrade
```

Accepted:

```text
merchant intent
        ↓
registered discovery
        ↓
all supported materially required semantic branches
        ↓
required merchant decisions and structured data
        ↓
merchant review / approval
        ↓
compiler validation
        ↓
complete resolved merchant operating model
```

A merchant may later change the business configuration under MS-PROT-040, but a subscription plan shall not be used as a reason to leave the initial business model intentionally incomplete.

Hard rule:

> **Initial configuration completeness is determined by business need and accepted semantic requirements, not by current commercial entitlement.**

---

# 3. Full configuration does not mean every optional integration is preconfigured

The previous section does not require Main Street to force merchants through optional setup that is not necessary for the supported business model to operate.

Examples may include:

```text
optional social-network connection
optional external calendar projection
optional secondary provider integration
optional growth campaign preferences
optional custom domain connection
```

These may be configured later when the merchant chooses to use them.

The governing distinction is:

```text
REQUIRED TO REPRESENT / OPERATE BUSINESS CORRECTLY
    → resolve during initial configuration

OPTIONAL ADDITIONAL INTEGRATION OR ENHANCEMENT
    → may remain unconfigured until chosen
```

This distinction shall not be used to defer core business semantics merely because a corresponding function will later require a paid entitlement.

---

# 4. The 30-day full-experience period operates over the complete merchant model

Every new Main Street merchant account receives the initial 30-day full-experience entitlement governed by MS-PROT-056 v1.0.

The trial is a temporary commercial entitlement source.

It is not:

```text
a semantic configuration
a separate merchant type
a plan-defined business model
a special runtime architecture
a second onboarding mode
```

Canonical interpretation:

```text
Complete Resolved Merchant Model
        +
30-Day Full-Experience Entitlements
        ↓
Applicable trial-accessible functionality
```

Hard rule:

> **The trial exposes the merchant-relevant supported Main Street operating model without changing its semantic meaning.**

---

# 5. Trial expiry changes commercial access, not merchant semantics

At expiry of the initial full-experience period, Main Street resolves the merchant's standing or selected plan entitlements.

Rejected:

```text
trial expires
        ↓
remove Booking from merchant configuration
```

Rejected:

```text
trial expires
        ↓
recompile merchant as a Free business
```

Accepted:

```text
trial expires
        ↓
full-experience entitlement source becomes ineffective
        ↓
standing/selected-plan entitlements remain
        ↓
commercial access is re-evaluated
```

The resolved semantic configuration remains independently authoritative unless the merchant separately changes it through the accepted configuration lifecycle.

---

# 6. Applicability and entitlement are orthogonal

Main Street shall distinguish:

```text
APPLICABILITY
    Does this functionality make semantic/business sense
    for this merchant's resolved operating model?

ENTITLEMENT
    Is this merchant commercially permitted
    to use this Main Street functionality now?
```

These are independent dimensions.

Conceptually:

| Applicable | Entitled | Result |
|---|---|---|
| NO | NO | not relevant and not commercially available |
| NO | YES | commercially granted but not surfaced/executed because the merchant model does not require it |
| YES | NO | relevant/configured semantics preserved; protected access unavailable subject to continuity rules |
| YES | YES | commercially accessible subject to all other runtime requirements |

Hard invariant:

> **Entitlement shall not manufacture applicability, and applicability shall not manufacture entitlement.**

---

# 7. Standard plans remain product promises, not architectural layers

MS-PROT-056 v1.1 remains authoritative:

```text
FREE     → Establish
BUSINESS → Operate
GROWTH   → Grow
```

These are product value promises.

They are not architectural layers and shall not produce:

```text
FreeArchitecture
BusinessArchitecture
GrowthArchitecture
```

They shall not produce plan-owned domain models such as:

```text
BusinessBooking
GrowthBooking
FreeMerchantDomain
```

Hard rule:

> **Establish, Operate and Grow describe classes of merchant value delivered by Main Street; they do not own business semantics or runtime architecture.**

---

# 8. A pricing-page feature is not an architectural type

Items marketed as subscription features may correspond to materially different architectural responsibilities.

Examples:

```text
Online Booking
    → Booking / Scheduling domain capability and application use cases

Advanced Analytics
    → projection / analytics responsibility

Notifications
    → selective post-commit reaction and delivery service

Custom Domain
    → presentation / infrastructure service

External Provider Integration
    → port / adapter and fulfilment boundary

Marketing Automation
    → orchestration, events, policies and potentially a bounded module where its own authoritative lifecycle exists

AI-Assisted Campaign Drafting
    → probabilistic interpretation/generation at a controlled boundary
```

Rejected:

```text
pricing feature
    → automatically create Capability
```

Rejected:

```text
pricing feature
    → automatically create generic Extension
```

The feature shall first be classified by actual semantic responsibility.

---

# 9. Composite architecture governs feature implementation

No universal architectural mechanism shall be introduced solely because multiple items appear in a subscription catalogue.

Each promised feature shall use the simplest appropriate mechanism consistent with Main Street's composite architecture.

Permitted examples include:

```text
transactional domain model
state/lifecycle model
deterministic transformation
application orchestration
post-commit domain event reaction
projection/read model
port/adapter
presentation composition
probabilistic AI boundary
```

Hard rule:

> **Plan packaging shall not override the architectural mechanism naturally required by the feature's semantic responsibility.**

---

# 10. No universal extension framework is authorised by this amendment

This amendment explicitly does not introduce a first-class universal architecture consisting of concepts such as:

```text
PromiseCapability
ExtensionRegistry
UniversalContributionRegistry
GrowthExtension
PlanModule
FeatureBus
```

Such abstractions require separate evidence showing repeated semantic need across concrete feature designs.

Existing accepted contracts, including capability surface contributions under MS-PROT-049, shall be reused where they already fit.

Hard rule:

> **Main Street shall not build an internal platform-within-the-platform merely to realise subscription packaging.**

---

# 11. Domain capabilities remain plan-blind

Capability-owned business semantics shall not branch on plan identity.

Rejected:

```text
if plan == BUSINESS
    allow Booking transition
```

Rejected:

```text
if plan == GROWTH
    Booking uses richer semantic meaning
```

Correct:

```text
commercial access boundary
        ↓
allowed application use case
        ↓
Booking capability
        ↓
Booking invariants and state transitions
```

The Booking capability does not need to know whether access was granted by:

```text
trial
Business plan
Growth plan
future valid commercial grant
```

Hard invariant:

> **Equivalent business operations retain equivalent domain meaning across plan levels.**

---

# 12. Entitlement is enforced at an access/use-case boundary

Commercial permission shall normally be evaluated before a protected application use case enters capability-owned authoritative execution.

Canonical shape:

```text
Request
    ↓
Merchant Scope resolution
    ↓
Actor authentication / authorisation where applicable
    ↓
Commercial entitlement decision
    ↓
Application use case
    ↓
Capability-owned domain execution
```

UI hiding alone is insufficient enforcement.

Rejected:

```text
hide button
    = commercial enforcement
```

Rejected:

```text
Domain Entity asks SubscriptionPlan directly
```

The exact application-service/interface implementation remains downstream.

---

# 13. Commercial entitlement is distinct from actor authority

These questions remain separate:

```text
ACTOR AUTHORITY
    May this actor perform the operation for this merchant?

COMMERCIAL ENTITLEMENT
    Has this merchant acquired commercial access to the Main Street function?
```

Both may independently be true or false.

No plan shall grant actor privileges, and no actor privilege shall manufacture a commercial entitlement.

MS-PROT-028 remains authoritative for identity/actor/trust boundaries.

---

# 14. Commercial entitlement is distinct from provider and trust state

Commercial permission does not imply provider readiness or trust satisfaction.

Conceptually:

```text
commercially entitled
+
actor authorised
+
trust satisfied where required
+
provider available where required
+
operational invariants satisfied
        ↓
effective execution eligibility
```

The failure of one dimension shall not be mislabelled as failure of another.

---

# 15. Plan changes shall not rewrite merchant configuration

Upgrade, downgrade, trial expiry and subscription cancellation shall not by themselves create a new Merchant Configuration revision.

Rejected:

```text
BUSINESS → FREE
        ↓
configuration migration deleting Booking
```

Rejected:

```text
FREE → BUSINESS
        ↓
reconstruct merchant graph from pricing catalogue
```

Correct:

```text
same merchant semantic configuration
        +
new effective entitlement context
        ↓
changed commercial access
```

A separate merchant-initiated or otherwise valid configuration change remains governed by MS-PROT-040.

---

# 16. Upgrade shall normally restore access without business reconstruction

Where a merchant's preserved configuration remains compatible with current accepted semantics, gaining an entitlement shall permit restoration of the corresponding applicable access without requiring the merchant to rebuild the business model.

Additional setup may still be required for genuinely optional provider/integration choices or newly introduced requirements that were never part of the core merchant configuration.

Hard rule:

> **Upgrade is a commercial-access transition, not a re-onboarding event.**

---

# 17. Existing commitments survive entitlement loss

A trial expiry, downgrade, cancellation or other entitlement loss shall not invalidate or rewrite business commitments already established while the merchant had valid access.

Examples include, where applicable:

```text
accepted Booking
accepted Appointment
accepted Order
established PaymentObligation
other capability-owned durable commitments
```

Rejected:

```text
entitlement lost
        ↓
commitment becomes invalid
```

Rejected:

```text
entitlement lost
        ↓
delete historical business truth
```

Hard invariant:

> **Commercial access may constrain future protected use; it shall not retroactively erase or reinterpret valid historical commitments.**

---

# 18. Residual management remains capability-owned

Where an existing commitment requires continued merchant action after entitlement loss, Main Street must preserve the minimum access required to manage or fulfil that commitment according to its owning semantic authority and accepted continuity rules.

Examples may include:

```text
viewing an existing booking
performing required fulfilment actions
legitimate cancellation or completion
accessing information necessary to honour the commitment
```

This residual access is not a new free-plan entitlement to create unlimited new paid-plan activity.

It exists because already-established obligations must remain manageable.

The exact operation-by-operation residual-access catalogue remains owned by the relevant capability and commercial-continuity specifications; this amendment does not invent one universal residual mode.

---

# 19. No universal post-trial degradation state machine is introduced

This amendment does not standardise one cross-platform enum such as:

```text
FULL
READ_ONLY
CREATE_DISABLED
FULFIL_EXISTING_ONLY
HIDDEN
```

Different functionality has materially different degradation semantics.

Examples:

```text
custom domain
    → routing/presentation consequence

advanced analytics
    → projection/service access consequence

notification channel
    → delivery-service consequence

Booking
    → new-activity access plus residual commitment-management consequence
```

A later design may introduce a reusable vocabulary only if repeated concrete feature evidence proves it semantically valid.

---

# 20. Applicable surface composition follows semantics plus entitlement

Merchant and customer surfaces shall consider at least:

```text
semantic applicability
+
commercial entitlement
+
actor/context eligibility where applicable
+
residual-management requirements
```

A higher plan shall not force irrelevant capability surfaces into the dashboard merely because the plan technically grants them.

Example:

```text
Grocery merchant
    Business entitlement grants operational class
    Booking not semantically applicable
    → no Booking workspace

Consultant
    same Business plan
    Booking semantically applicable
    → Booking workspace/action may be available
```

MS-PROT-037 and MS-PROT-049 remain authoritative for dashboard and capability surface composition.

---

# 21. Adaptive plan presentation is permitted; adaptive contractual meaning is not

Main Street may explain a plan using merchant-relevant functionality derived from the resolved merchant model.

Conceptually:

```text
stable plan entitlement revision
        +
merchant applicability
        ↓
merchant-specific plan explanation
```

Permitted:

```text
Mechanic sees Business value as:
    online appointments
    operational messaging

Retailer sees Business value as:
    ordering
    inventory operations
```

provided the underlying plan revision remains the same commercial authority.

Rejected:

```text
business category
    ↓
invent merchant-specific hidden entitlement contract
```

Hard rule:

> **Presentation may adapt to relevance; entitlement meaning remains revision-stable and explicit.**

---

# 22. Plan recommendation remains non-authoritative

Main Street may compare the merchant's applicable configured functionality with standard plan entitlements and recommend the lowest plan that preserves the desired access.

The recommendation shall not:

```text
rewrite configuration
force a plan
create capability relevance
change semantic meaning
```

A merchant may select a lower plan where commercially supported and accept the resulting access consequences.

MS-PROT-056 v1.1 remains authoritative for this rule.

---

# 23. New bounded modules remain permitted where new semantics genuinely exist

The requirement to preserve the current architecture does not prohibit Main Street from adding new bounded modules.

A new promised feature may justify a new module if it introduces genuine independent semantic ownership, lifecycle, invariants or authoritative state.

Example criterion:

```text
feature has its own durable authoritative state
+
meaningful lifecycle
+
independent invariants
+
well-defined contracts
        ↓
new bounded module may be justified
```

This is evolution within the established modular/composite architecture, not replacement of it.

Rejected:

```text
new pricing-page item
    → automatically create new module
```

---

# 24. Reusable cross-capability services must communicate through explicit contracts

Where a feature is genuinely reusable across capabilities, it may consume explicit registered contracts appropriate to the interaction.

Examples:

```text
post-commit independent reaction
    → domain event

synchronous use-case coordination
    → application/capability contract

external provider
    → port/adapter

read-only insight
    → projection/query contract

surface composition
    → accepted surface contribution contract
```

Rejected:

```text
consumer directly reads or mutates another capability's persistence
```

Rejected:

```text
one universal untyped feature bus
```

The owning capability remains authoritative over its state.

---

# 25. AI remains outside deterministic authority

Where a plan promise includes AI-assisted functionality, AI may interpret, draft, classify, summarise, recommend or otherwise assist within its accepted boundary.

AI shall not:

```text
manufacture entitlement
change plan revision meaning
rewrite merchant semantic configuration
bypass capability invariants
mutate another capability's state without an authorised deterministic path
```

AI-generated actions requiring business mutation must re-enter the deterministic application/capability authority path.

---

# 26. Implementation fitness constraints

Implementation shall preserve at minimum the following testable properties:

```text
same merchant configuration compiles independently of plan identity

plan change does not automatically create Merchant Configuration revision

capability domain logic contains no plan-name branching for semantic meaning

trial expiry does not delete configured semantics

upgrade does not require rebuilding compatible merchant configuration

entitlement loss does not rewrite valid historical commitments

irrelevant but technically granted functionality is not surfaced merely because of plan level

actor authority and commercial entitlement remain independently testable

UI hiding alone cannot bypass backend commercial access enforcement

cross-capability feature implementations cannot directly mutate foreign authoritative state
```

Exact test classes, package names and frameworks remain implementation decisions.

---

# 27. Explicitly rejected architectures

The following are rejected by this amendment:

```text
plan-driven merchant configuration
plan-specific semantic graphs
FreeArchitecture / BusinessArchitecture / GrowthArchitecture
universal feature flags as semantic capability authority
plan-name checks inside domain models
subscription-driven semantic recompilation
pricing catalogue as capability taxonomy
one universal Extension framework
one universal event-driven feature mechanism
business-category-specific plan execution logic
entitlement loss deleting or reinterpreting historical commitments
```

These patterns require a future explicit amendment supported by stronger evidence before they may be introduced.

---

# 28. Decisions deliberately left downstream

This amendment does not decide:

```text
exact entitlement persistence schema
exact Java class/interface structure
exact cache strategy for entitlement decisions
exact API endpoint design
exact entitlement-check middleware/framework
exact post-trial access mode for every individual feature
exact operation-by-operation residual-management catalogue
future add-on catalogue
future promotional grants
future usage/capacity metering
future scale pricing
future negotiated merchant terms
```

The exact post-trial and downgrade behaviour for a particular functionality must follow its semantic ownership and commercial policy while satisfying the hard invariants in this document.

A downstream decision may not use this deferral to violate configuration independence or commitment preservation.

---

# 29. Cross-domain validation evidence

This amendment was tested conceptually against materially different merchant models.

## Mobile mechanic

```text
Publication
Offering
Enquiry
Scheduling
Booking
customer service location
Payment arrangement
```

The model supports complete configuration before entitlement filtering, full relevant trial access, post-trial restriction of protected new functions without deleting Booking semantics, and preservation of existing appointments.

## Information publisher

```text
Publication
Enquiry
subscriber/update interactions where supported
```

The model does not manufacture Booking, Ordering or Inventory merely because a higher plan grants operational classes that could apply to other merchants.

## Grocery / retailer

```text
Catalogue
Ordering
Inventory
Payment
fulfilment
```

The same standard plans remain valid while applicability filters out irrelevant Booking functionality.

## Growth-oriented functions

Analytics, segmentation, automation, provider integrations and AI assistance can use their appropriate projection/event/orchestration/adapter/AI mechanisms without becoming Booking/Ordering semantics.

Result:

> **The approved boundary survives materially different merchant configurations without requiring business-type branches or plan-specific domain models.**

---

# 30. Trade-off verdict

Main Street intentionally accepts additional commercial-access and continuity reasoning in exchange for stronger semantic stability.

Accepted cost:

```text
explicit entitlement decisions
feature-by-feature architectural classification
continuity-aware access after entitlement loss
clearer application-boundary enforcement
```

Avoided cost:

```text
plan/domain coupling
semantic corruption on downgrade
business-type branches
re-onboarding on upgrade
universal extension-framework complexity
future semantic migration caused only by pricing changes
```

The accepted trade-off is favourable because commercial packaging is expected to evolve more frequently than merchant semantic meaning.

---

# 31. Governance verdict

**ACCEPTED.**

MS-PROT-056 shall now be interpreted as:

```text
MS-PROT-056 v1.0
    Subscription Plans, Full-Experience Entitlements
    & Commercial Availability Model

        +

MS-PROT-056 v1.1
    Standard Plan Hierarchy & Multi-Location
    Commercial Scale Amendment

        +

MS-PROT-056 v1.2
    Full Merchant Configuration & Commercial
    Access Boundary Amendment
```

Current governing summary:

```text
FREE     → Establish
BUSINESS → Operate
GROWTH   → Grow

merchant business need
    → full supported semantic configuration

trial / plan
    → commercial entitlement

applicability × entitlement
    → accessible functionality

accessible functionality
    → existing composite architecture

plan change
    ≠ semantic redefinition

entitlement loss
    ≠ historical commitment loss
```

No new universal extension framework or plan-specific architecture is authorised.
