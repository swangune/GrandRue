# MS-PROT-022 v1.5 — Resolved Configuration Package & Static-Dynamic Resolution Boundary Amendment

**Document ID:** MS-PROT-022  
**Version:** 1.5  
**Status:** **ACCEPTED after architectural review, cross-domain falsification and manual approval**  
**Amends:** MS-PROT-022 v1.4  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.4, MS-PROT-023, MS-PROT-027 v1.1, MS-PROT-040 v1.0, MS-PROT-047 v1.0, MS-PROT-048 v1.1, MS-PROT-049 v1.0  
**Purpose:** Extend configuration resolution so newer capability-configuration, fulfilment-provider and surface-contribution contracts compose deterministically without collapsing immutable configuration, live operational state, actor authority or provider health into one monolithic executable model.

---

## 1. Governing decision

> **Main Street configuration resolution shall produce an immutable Resolved Configuration Package from a pinned semantic registry release and one validated merchant configuration revision. Live actor authority, provider connection health, residual obligations, customer context, availability and other operational facts are applied later through contextual resolution and shall not require recompilation merely because they change.**

Canonical architecture:

```text
Pinned Semantic Registry Release
        +
Validated Merchant Configuration Revision
        ↓
Deterministic Static Resolution
        ↓
Resolved Configuration Package
        │
        ├── Executable Semantic Model
        ├── Resolved Capability Configuration
        ├── Fulfilment Plan
        └── Static Surface Contribution Catalogue
        ↓
Runtime / Projection Context
        +
current actor authority
current provider health
current operational obligations
current customer/public context
current authoritative state
        ↓
Contextual Resolution
        ├── command/action eligibility
        ├── degraded fulfilment status
        ├── residual management requirements
        └── audience-visible surface contributions
```

Hard rule:

> **A changing runtime fact shall not become a reason to rebuild the merchant's immutable configuration package unless the authoritative configuration itself changed.**

---

## 2. Why this amendment is required

MS-PROT-022 already owns deterministic resolution of merchant configuration into a merchant-scoped operational model.

Since MS-PROT-022 v1.4, later accepted designs added three new contracts:

- MS-PROT-047 — capability-owned bounded configuration decisions;
- MS-PROT-048 — fulfilment roles and provider bindings;
- MS-PROT-049 — capability surface contributions.

A naive implementation could extend `ExecutableMerchantModel` until it contains:

```text
semantic operations
configuration values
provider credentials
provider health
surface visibility
actor permissions
outstanding commitments
current inventory
current availability
```

That is rejected.

These facts have different authorities and change at different rates.

The amendment therefore refines MS-PROT-022 by separating:

```text
STATIC DETERMINISTIC RESOLUTION
        from
CONTEXTUAL RUNTIME RESOLUTION
```

---

## 3. Static resolution

Static resolution answers:

> **Given this exact semantic registry release and this exact validated merchant configuration revision, what Main Street semantics, bounded configuration, fulfilment obligations/bindings and statically applicable surface contributions are valid for this merchant?**

Its inputs are immutable for one compilation:

```text
semantic registry release identity
merchant configuration revision identity
merchant configuration values
registered capability definitions
registered configuration contracts
registered fulfilment contracts
registered surface-contribution contracts
```

Static resolution shall be deterministic.

Given identical authoritative inputs, it shall produce an equivalent resolved package.

---

## 4. Contextual resolution

Contextual resolution answers questions that cannot be determined from configuration alone.

Examples:

```text
May this actor execute ConfirmBooking now?

Is the merchant's external scheduler currently connected?

Must a residual Bookings workspace remain because future bookings still exist?

May this customer see this particular booking?

Is this resource actually available at 14:00?
```

These depend on live or request-specific facts.

Contextual resolution may use the immutable Resolved Configuration Package as an input but shall not redefine its semantic meaning.

---

## 5. Resolved Configuration Package

The canonical derived artefact is the **Resolved Configuration Package**.

Conceptually:

```text
ResolvedConfigurationPackage
{
    merchantIdentity
    sourceConfigurationRevision
    semanticRegistryRelease

    executableSemanticModel
    resolvedConfiguration
    fulfilmentPlan
    staticSurfaceContributionCatalogue

    provenance
}
```

This is conceptual and does not mandate one Java class or database table.

The package is derived state. It is not a new semantic authority.

---

## 6. Executable Semantic Model

The executable semantic portion contains the already-accepted MS-PROT-022 materialised semantics, including where applicable:

```text
active capabilities + activation reasons
applicable Operational Object definitions
applicable schemas and fields
applicable typed relationships
applicable operations/effects
resolved policy values
applicable requirements
configured offering/resource bindings
stable schedule configuration
```

MS-PROT-022 v1.4 continues to govern typed relationship applicability and executable relationship effects.

This amendment does not redefine those rules.

---

## 7. Resolved capability configuration

MS-PROT-047 introduces capability-owned configuration decisions beyond the currently implemented policy subset.

Static resolution shall therefore resolve applicable configuration decisions only after the active capability set is known.

Canonical order:

```text
active capabilities
        ↓
configuration-decision applicability
        ↓
explicit merchant value / registered default
        ↓
validated effective value
```

For each applicable decision, resolution preserves provenance such as:

```text
DEFAULTED
EXPLICITLY_SELECTED
```

where the accepted configuration contract requires it.

For a decision whose owner is inactive:

```text
NOT_APPLICABLE
```

A merchant value may not activate its owner.

---

## 8. Required unresolved configuration is a compilation failure

If an applicable required configuration decision has:

```text
no valid explicit value
and
no registered default
```

then the candidate cannot produce a valid resolved package.

The compiler must fail with a configuration gap.

Rejected:

```text
missing value
    ↓
compiler guesses
```

Rejected:

```text
missing value
    ↓
AI decides during compilation
```

The deterministic compiler never performs inference.

---

## 9. Fulfilment Plan

MS-PROT-048 separates capability semantics from the mechanism that fulfils them.

The resolved package therefore contains a **Fulfilment Plan**, not live integration state.

Conceptually:

```text
FulfilmentPlan
{
    applicableFulfilmentRoles
    resolvedBindings
    providerCompatibilityEvidence
    bindingScope
    providerContractProvenance
}
```

Examples:

```text
Scheduling / calendar-storage
    → Google-backed adapter

Notification / email-delivery
    → internal/open implementation or registered external service

Payment / payment-execution
    → registered external payment provider
```

The plan expresses what binding is valid for the configuration.

It does not assert that the provider is currently healthy or reachable.

---

## 10. Provider binding versus provider health

These are distinct:

```text
provider binding
    = configuration-derived fulfilment plan

provider credentials
    = secure integration state

provider connection health
    = live operational/integration state
```

Example:

```text
Scheduling fulfilment role
    bound to Provider P
```

may remain true while:

```text
Provider P connection = UNHEALTHY
```

The merchant configuration is not recompiled merely because the connection becomes unhealthy.

Contextual fulfilment resolution determines the resulting degradation.

---

## 11. Open-first selection remains upstream of runtime health

MS-PROT-048 v1.1 remains authoritative for provider/implementation selection priority:

```text
modifiable open library
        ↓ if unsuitable / unsustainable / unsafe
open/self-hostable software
        ↓ if unsuitable / unsustainable / unsafe
paid external service
```

Once an accepted binding is represented in merchant/platform configuration, static resolution validates that binding against the registered fulfilment contract.

The compiler shall not dynamically choose a more convenient paid provider during runtime.

---

## 12. Static Surface Contribution Catalogue

MS-PROT-049 defines capability surface contributions.

Static resolution may materialise the contributions whose **configuration-level prerequisites** are satisfied.

Conceptually:

```text
StaticSurfaceContributionCatalogue
{
    contribution identities
    audiences
    kinds
    composition targets
    projection requirements
    supported operation references
    static eligibility evidence
}
```

This catalogue is not the final visible UI.

It says only which registered contributions may participate before live context is applied.

---

## 13. Surface catalogue versus visible surface

The following must remain distinct:

```text
static contribution applicability
        ≠
current actor-visible surface
```

For example:

```text
Booking active
        ↓
Bookings WORKSPACE contribution statically applicable
```

but:

```text
staff actor lacks booking-read authority
        ↓
Bookings workspace not visible to that actor
```

No recompilation occurs.

Actor authority is contextual.

---

## 14. Residual obligations are contextual

MS-PROT-040 establishes that deactivating a capability does not abandon outstanding commitments.

Example:

```text
Booking disabled for new activity
        +
7 future bookings remain
```

The immutable package records that Booking is inactive for new activity under the current configuration.

The fact that seven commitments remain is authoritative operational state.

Contextual surface resolution may therefore retain:

```text
Bookings management workspace
```

while removing:

```text
Create booking
Public booking entry point
```

The configuration compiler shall not need to rebuild the package each time the outstanding booking count changes.

---

## 15. Current actor authority is contextual

Actor role, authentication state and command authority shall not be compiled as permanent merchant semantics.

The same resolved configuration package may serve:

```text
merchant controller
front-desk staff
content editor
inventory staff
customer/public visitor
```

Contextual authority determines which operations and surfaces are available to each actor.

MS-PROT-023/028 remain authoritative for execution/authority boundaries.

---

## 16. Availability is contextual

Stable scheduling parameters belong to configuration.

Actual availability does not.

```text
working hours
buffers
booking horizon
    → resolved configuration

current appointments
current allocations
current unavailability
current provider data
request time
    → contextual availability decision
```

Therefore:

> **AvailabilityDecision shall never be materialised as immutable configuration truth.**

---

## 17. Customer/public visibility is contextual where necessary

A surface contribution may be statically eligible for the CUSTOMER or PUBLIC audience while specific data visibility remains contextual.

Example:

```text
Customer booking-history surface contribution
        ↓
customer relationship/context validation
        ↓
only that customer's permitted booking projection
```

The static package cannot pre-authorise access to arbitrary runtime records.

MS-PROT-027 remains authoritative for exposure and projection.

---

## 18. Deterministic compilation order

The accepted high-level static resolution order is:

```text
1. Pin semantic registry release.

2. Validate configuration revision identity/scope and all direct references.

3. Resolve merchant-selected capability seeds.

4. Expand transitive REQUIRES closure.

5. Reject capability conflicts.

6. Establish effective active capability set and activation provenance.

7. Resolve applicable capability configuration decisions.
   - validate owner activity
   - resolve explicit values
   - apply registered defaults
   - reject invalid/unresolved required values
   - preserve provenance

8. Resolve applicable schemas, Operational Objects and typed relationships.

9. Materialise applicable operations, effects and requirements.

10. Resolve stable offering/resource/schedule bindings where represented.

11. Determine applicable fulfilment roles.

12. Validate fulfilment bindings against required obligations and scope.

13. Materialise statically applicable surface contributions and composition-target references.

14. Validate cross-part consistency.

15. Materialise immutable Resolved Configuration Package with provenance.
```

This order is architectural. Implementation may use optimised internal passes if externally observable semantics remain equivalent and traceable.

---

## 19. Cross-part consistency

The package must be coherent across its constituent contracts.

Examples of invalid outcomes:

```text
surface ACTION references operation not present in executable semantic model

fulfilment binding references inactive capability role

configuration decision value belongs to inactive capability

public interaction contribution exposes unsupported operation

provider binding cannot satisfy mandatory fulfilment obligations
```

Static resolution shall reject such contradictions rather than defer them to UI/runtime failure.

---

## 20. What static compilation explicitly excludes

Static compilation shall not depend on:

```text
AI inference
natural-language reasoning
current provider connection health
current provider outage status
current inventory quantity
current booking/order count
current allocation state
current appointment state
current user session
current actor privileges as an individual session fact
current customer identity
current availability
current network latency
current message-delivery retry state
```

These facts may affect contextual resolution or runtime execution.

They shall not redefine the merchant's compiled semantic package.

---

## 21. What causes recompilation

Recompilation is required when authoritative static inputs change materially.

Examples:

```text
new merchant configuration revision activated
semantic registry release changes for the revision
capability selection changes
bounded configuration value changes
provider binding configuration changes
stable offering/resource/schedule configuration changes
```

The configuration lifecycle in MS-PROT-040 still governs approval/activation.

The package shall identify its source configuration revision and registry context.

---

## 22. What does not cause recompilation by itself

Examples:

```text
provider briefly disconnects
new enquiry arrives
booking becomes confirmed
stock quantity changes
staff member logs in
customer opens secure link
one outstanding booking is completed
resource becomes occupied
notification retry succeeds
```

These are runtime/context changes.

Derived projections may update, but the immutable configuration package remains valid unless a configuration-level invariant is actually broken.

---

## 23. Package provenance

The package shall retain enough provenance to explain:

```text
which configuration revision produced it
which semantic registry release was used
why each capability is active
why each bounded configuration value has its effective value
which fulfilment binding satisfies each applicable role
which static surface contribution originates from which registered definition
```

This provenance is required for:

```text
debugging
support
impact analysis
configuration review
historical explanation
safe evolution
```

---

## 24. Package identity and immutability

A resolved package is immutable for its source inputs.

Conceptually:

```text
merchant M
configuration revision C19
semantic registry R8
        ↓
resolved package P(M,C19,R8)
```

A new configuration revision produces a new package.

The old package may remain historically necessary for existing commitments that retain configuration affinity under MS-PROT-040.

---

## 25. Historical commitment affinity

Existing commitments may retain affinity to an older configuration revision.

Therefore runtime may need access to:

```text
current active resolved package
        +
historical resolved package / reconstructable equivalent
```

for different operations.

This does not mean two merchant configurations are simultaneously authoritative for initiating new activity.

It preserves the semantics under which existing commitments were created.

---

## 26. No giant capability descriptor

This amendment does not justify combining all registered contracts into one mutable capability descriptor.

Preferred conceptual ownership remains:

```text
Capability
   ├── Semantic Contract
   ├── Configuration Contract
   ├── Fulfilment Contract
   └── Surface Contribution Contract
```

Resolution composes these contracts into one merchant-scoped package while preserving their distinct authorities.

---

## 27. No giant executable model requirement

Likewise, `ResolvedConfigurationPackage` is an architectural envelope, not a demand for one giant object.

Implementation may use:

```text
ExecutableMerchantModel
ResolvedConfigurationValues
ResolvedFulfilmentPlan
ResolvedSurfaceCatalogue
```

or equivalent immutable structures.

The governing requirement is separation of authority and lifecycle, not a particular object graph.

---

## 28. Compatibility with current implementation

The current implementation already materialises a bounded subset of this architecture:

```text
MerchantConfiguration
    capability identifiers
    policy selections

ConfigurationCompiler
    capability closure
    relationship applicability
    operation/effect materialisation
    policy resolution

ExecutableMerchantModel
    immutable resolved executable subset
```

That implementation remains valid.

MS-PROT-022 v1.5 does **not** require immediate implementation of every later contract.

Hard implementation rule:

> **Add each new resolved-package component only when an accepted capability contract and failing conformance test require it.**

No speculative generic framework is authorised by this amendment.

---

## 29. Cross-domain falsification

### Information publisher

Static package may contain:

```text
Publication
Enquiry
optional Subscription/Notification
publication configuration
publication fulfilment roles where applicable
public content interactions
```

No Booking, Inventory or Payment semantics are invented.

Runtime changes in publication counts do not cause recompilation.

**PASS**

### Online consultant using external scheduler

Static package contains:

```text
Scheduling/Booking semantics
resolved scheduling configuration
external scheduling fulfilment binding
Calendar/Booking surface contributions
```

Provider connection health remains contextual.

**PASS**

### External scheduler disconnects

No configuration revision changed.

Required result:

```text
same resolved package
        +
contextual degraded provider status
        +
attention/repair surface
```

No recompilation required.

**PASS**

### Motel

Static package may contain Booking and Allocation/Resource semantics with stable configuration.

Current room occupancy and remaining bookings are contextual runtime state.

No Motel-specific compiler branch required.

**PASS**

### Retailer

Static package contains Catalogue/Commerce/Inventory semantics and configuration.

Current stock quantities and orders remain runtime facts.

**PASS**

### Hybrid merchant

Product and service capabilities resolve in one package through registered semantics.

No Hybrid template/classification compiler path appears.

**PASS**

### Restricted staff actor

Same package is reused.

Actor-specific command/surface eligibility is contextual.

No staff-specific recompilation required.

**PASS**

### Capability deactivated with residual commitments

New package reflects deactivation for new activity.

Residual operational obligations remain live state and drive temporary management surfaces contextually.

**PASS**

### Provider binding change

Changing from Provider A to Provider B is a configuration-level change and therefore produces a new validated configuration revision/package.

Historical commitments retain binding/configuration provenance where required.

**PASS**

---

## 30. Rejected alternatives

### Alternative A — New MS-PROT-050 configuration resolver

Rejected because MS-PROT-022 already owns deterministic configuration resolution. A second authority would create overlap and eventual contradiction.

### Alternative B — Put every concern inside `ExecutableMerchantModel`

Rejected because provider health, actor authority, residual obligations and other contextual facts have different lifecycles and would force unnecessary recompilation.

### Alternative C — Resolve providers dynamically for convenience

Rejected because provider selection/binding is configuration/fulfilment authority and must remain traceable, validated and governed by MS-PROT-048.

### Alternative D — Compile final dashboard visibility

Rejected because actor authority, residual obligations and provider health are contextual and change independently of configuration.

### Alternative E — Let runtime reinterpret raw merchant configuration

Rejected by the original MS-PROT-022 boundary; it leaks configuration semantics into runtime and creates repeated interpretation.

---

## 31. Accepted invariants

1. MS-PROT-022 remains the sole authority for deterministic configuration resolution.
2. Static resolution uses one pinned semantic registry release and one validated merchant configuration revision.
3. Static resolution is deterministic and side-effect free.
4. The canonical derived envelope is a Resolved Configuration Package.
5. The package may contain separate executable semantics, resolved configuration, fulfilment plan and static surface catalogue.
6. Package composition does not merge the authorities of those contracts.
7. Applicable bounded configuration decisions resolve only after active capabilities are known.
8. Configuration values cannot activate their owning capability.
9. Required unresolved configuration causes compilation failure rather than inference.
10. Fulfilment binding belongs to static resolution; provider credentials/health do not.
11. Static surface contributions are not final actor-visible UI.
12. Actor authority is contextual.
13. Residual operational obligations are contextual.
14. Current availability is contextual.
15. Current inventory, booking, allocation and other operational facts are contextual.
16. Provider outages/degradation do not require recompilation by themselves.
17. Configuration/provider-binding changes do require a new validated revision/package.
18. Cross-part references must be validated before package materialisation.
19. Package provenance identifies source configuration and registry context.
20. Existing commitments may retain affinity to historical packages/reconstructable equivalents.
21. No business-category compiler branches are introduced.
22. No giant capability descriptor or giant Java package object is mandated.
23. New package components are implemented only when accepted contracts and failing tests require them.

---

## 32. Implementation consequence

The current Java implementation should remain unchanged at acceptance of this amendment.

The next implementation slice must be driven by concrete evidence.

For example, when the first non-policy MS-PROT-047 configuration decision is implemented, RED tests should prove:

```text
owner must be active
value must satisfy registered domain
required unresolved value fails
explicit/default provenance is preserved
```

When the first concrete MS-PROT-048 provider binding is implemented, RED tests should prove:

```text
binding cannot activate capability
binding satisfies applicable fulfilment role
provider health is not embedded in compiled package
```

When the first MS-PROT-049 contribution is implemented, RED tests should prove:

```text
static contribution applicability is configuration-derived
actor visibility is not compiled into permanent semantic authority
```

Until such evidence exists, speculative implementation is rejected.

---

## Governance verdict

**ACCEPTED.**

MS-PROT-022 v1.5 preserves v1.4's relationship-applicability authority and extends the configuration-resolution model to integrate MS-PROT-047, MS-PROT-048 and MS-PROT-049 without creating a second resolver specification or a monolithic executable model.

The decisive boundary is:

> **Compile stable merchant semantics and configuration once per authoritative revision; resolve changing operational context at runtime without redefining that compiled meaning.**
