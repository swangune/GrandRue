# MS-PROT-048 v1.2 — Merchant Fulfilment Binding Set Revision & Activation Affinity Amendment

**Document ID:** MS-PROT-048  
**Version:** 1.2  
**Status:** **ACCEPTED after implementation-discovered authority review and manual approval**  
**Amends:** MS-PROT-048 v1.1 — Open-First Fulfilment Selection Amendment  
**Preserves:** MS-PROT-048 v1.0 fulfilment-role/provider semantics and MS-PROT-048 v1.1 open-first selection priority outside this amendment's scope  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.5, MS-PROT-040 v1.1, MS-PROT-047 v1.0, MS-PROT-048 v1.0-v1.1, MS-PROT-054 v1.0, MS-PROT-067 v1.0  
**Purpose:** Resolve the authoritative representation and activation affinity of merchant-specific Provider Fulfilment bindings without creating a second independently active configuration authority or collapsing provider connection state into Merchant Configuration.

---

## 1. Governing decision

> **Merchant-specific fulfilment routing SHALL be represented by an immutable, merchant-scoped `FulfilmentBindingSetRevision`. Every Merchant Configuration Revision that requires merchant-specific fulfilment routing SHALL pin the exact binding-set revision used to resolve its Fulfilment Plan. A Fulfilment Binding Set Revision has no independent activation lifecycle: Merchant Configuration Revision remains the sole configuration activation authority.**

Canonical relationship:

```text
MerchantConfigurationRevision C27
        │
        │ pins exact revision
        ▼
FulfilmentBindingSetRevision F8
        │
        ▼
static validation + resolution
        │
        ▼
ResolvedConfigurationPackage RCP(C27)
        └── FulfilmentPlan
```

The hybrid model separates stable technical routing configuration from capability configuration while retaining one unambiguous activation authority.

---

## 2. Why this amendment is required

MS-PROT-048 v1.0 already establishes that a `FulfilmentBinding` selects how an applicable Fulfilment Role is discharged for a merchant/configuration context and that binding changes are controlled configuration/integration changes.

MS-PROT-022 v1.5 already requires the immutable Resolved Configuration Package to include a Fulfilment Plan derived deterministically from exact static inputs.

The unresolved question was narrower:

> **Where does the authoritative merchant-specific routing selection live, and how is it version-affined to the Merchant Configuration Revision without creating two independently active configuration authorities?**

This amendment resolves that question.

---

## 3. Fulfilment Binding Set Revision

A `FulfilmentBindingSetRevision` is an immutable subordinate merchant operating-configuration artefact containing stable Provider Fulfilment routing selections.

Conceptually:

```text
FulfilmentBindingSetRevision
{
    bindingSetIdentity
    revision
    merchantScope
    semanticRegistryRelease
    bindings[]
}
```

Each revision is immutable after it is referenced by a governed Merchant Configuration Revision.

The exact physical representation is implementation-specific.

---

## 4. Exact revision reference

A Merchant Configuration Revision SHALL retain an exact immutable reference to its governing binding-set revision where merchant-specific fulfilment routing is present.

Conceptually:

```text
MerchantConfigurationRevision
{
    ...
    fulfilmentBindingSetRevisionReference?
}
```

The reference SHALL identify one exact binding-set revision, not merely a mutable binding-set name or "current" pointer.

Rejected:

```text
configuration C27
    → fulfilment bindings = CURRENT
```

Accepted:

```text
configuration C27
    → binding-set F / revision 8
```

Historical reconstruction must not depend on whatever binding revision happens to be current later.

---

## 5. One activation authority

`FulfilmentBindingSetRevision` SHALL NOT have an independent `ACTIVE` state that competes with Merchant Configuration activation.

Rejected:

```text
MerchantConfigurationRevision C27 = ACTIVE
FulfilmentBindingSetRevision F8 = ACTIVE
FulfilmentBindingSetRevision F9 = ACTIVE
```

because this creates ambiguity over the effective static configuration.

Required:

```text
ACTIVE MerchantConfigurationRevision C27
        ↓ exact reference
FulfilmentBindingSetRevision F8
```

MS-PROT-040 remains authoritative for configuration approval, activation, supersession and historical affinity.

---

## 6. Binding changes create a new Merchant Configuration Revision

A stable routing change SHALL create:

1. a new immutable `FulfilmentBindingSetRevision`; and
2. a new Merchant Configuration Revision referencing that exact binding-set revision.

Example:

```text
C27 → F8
F8: calendar-storage → Provider A

merchant changes routing
        ↓
F9: calendar-storage → Provider B
        ↓
C28 → F9
        ↓
validate / approve / activate C28
```

The previous pair remains historical truth.

The binding set does not silently switch underneath an already activated Merchant Configuration Revision.

---

## 7. Reuse of an unchanged binding-set revision

A new Merchant Configuration Revision MAY reuse an existing binding-set revision when fulfilment routing has not changed and the revision remains compatible with the new static configuration and pinned semantic registry release.

Example:

```text
C28 → F9

merchant changes Business Hours only
        ↓
C29 → F9
```

This avoids duplicating unchanged routing configuration while preserving exact affinity.

---

## 8. Capability Configuration remains separate

A `FulfilmentBindingSetRevision` is not `ResolvedCapabilityConfiguration` and a `FulfilmentBindingSelection` is not a `CapabilityConfigurationDecision`.

Canonical distinction:

```text
Capability Configuration
    WHAT bounded semantic variability applies

Provider Fulfilment Binding
    HOW an already-applicable technical responsibility is discharged
```

Therefore fulfilment selection:

- cannot activate a capability;
- cannot create an operation;
- cannot define policy meaning;
- cannot create a Requirement;
- cannot redefine an Operational Object;
- cannot weaken the capability's obligations to fit a provider.

MS-PROT-047 remains authoritative for capability-owned bounded configuration.

---

## 9. Binding selection

Conceptually a binding selection contains only stable routing information required to identify the selected fulfiller for one registered role.

```text
FulfilmentBindingSelection
{
    roleIdentity
    semanticContext?
    fulfillerKind
    fulfillerIdentity
    providerConnectionIdentity?
}
```

`fulfillerKind` remains:

```text
INTERNAL
EXTERNAL_PROVIDER
```

The binding selection is declarative routing configuration. It is not executable provider behaviour.

---

## 10. ProviderConnection reference is identity, not connection state

An external binding MAY reference a stable `ProviderConnection` identity when a particular merchant/provider account relationship is required.

The binding-set revision SHALL NOT contain mutable connection state such as:

```text
OAuth/access tokens
API keys
credential material
credential generations
connection health
provider outage state
webhook delivery state
sync cursors
last reconciliation time
provider latency
retry counters
```

These remain owned by the applicable credential, connection, observability and runtime authorities.

Canonical separation:

```text
FulfilmentBindingSetRevision
    → ProviderConnection identity

ProviderConnection / Credential / Readiness state
    → live technical authority
```

MS-PROT-067 remains authoritative for credential and connection security boundaries.

---

## 11. Connection changes that do not change routing

The following do not create a new binding-set revision by themselves:

```text
credential rotation
token refresh
provider temporarily unavailable
health changes
webhook subscription renewal
sync-cursor movement
reconciliation timestamps
retry state
network latency
```

These are live integration facts.

They SHALL NOT cause Merchant Configuration recompilation merely because they changed.

---

## 12. Changes that require a new binding-set revision

A new binding-set revision is required when stable routing identity or scope changes materially, including where applicable:

```text
INTERNAL → EXTERNAL_PROVIDER
EXTERNAL_PROVIDER → INTERNAL
Provider A → Provider B
provider account / ProviderConnection identity changes where it governs routing
role binding scope changes
semantic-context binding changes
registered fallback routing changes where such fallback is accepted
```

The new binding-set revision becomes effective only through a new Merchant Configuration Revision that pins it and passes the governed configuration lifecycle.

---

## 13. Static resolution inputs

Where a Merchant Configuration Revision references a binding-set revision, static resolution SHALL use the exact referenced revision together with the pinned semantic registry and registered fulfilment contracts.

Canonical flow:

```text
Pinned Semantic Registry Release
        +
Merchant Configuration Revision C27
        +
exact Fulfilment Binding Set Revision F8
        +
registered Fulfilment Contracts
        ↓
Deterministic static validation
        ↓
ResolvedConfigurationPackage
        └── FulfilmentPlan
```

The compiler/resolver SHALL fail closed when the exact referenced binding-set revision cannot be resolved.

---

## 14. Required static validation

Before a referenced binding selection enters the Fulfilment Plan, static resolution SHALL validate at least all facts determinable from accepted static authority, including:

```text
merchant-scope affinity
binding-set revision identity affinity
semantic-registry-release affinity
role registration
role static applicability
fulfiller kind validity
internal fulfiller registration/identity where governed
external provider registration where applicable
provider/role compatibility evidence available under the registered contract
provider-connection reference shape where required
cross-part consistency with the executable semantic model
```

A binding cannot make an inactive capability/role applicable.

Where the registered fulfilment contract cannot prove required compatibility, compilation must not invent compatibility.

---

## 15. Fulfilment Plan remains derived state

The `FulfilmentPlan` remains a deterministic component of the Resolved Configuration Package.

Conceptually it records:

```text
applicable Fulfilment Roles
resolved bindings
provider compatibility evidence
binding scope
provider-contract provenance
binding-set revision provenance
```

It is not a second merchant authority.

The source authorities remain the Merchant Configuration Revision, its exact binding-set reference, the exact binding-set revision and registered fulfilment contracts.

---

## 16. Provider health remains contextual

The binding:

```text
calendar-storage → Provider P
```

may remain static truth while:

```text
ProviderConnection P = UNHEALTHY
```

Therefore:

```text
binding selection
        ≠
Provider Readiness
```

Contextual resolution/runtime determines degraded operation behaviour under MS-PROT-048, MS-PROT-062, MS-PROT-068, MS-PROT-069 and MS-PROT-070 as applicable.

A provider outage does not automatically create F9, C28 or a new RCP.

---

## 17. Historical affinity

An authoritative operation that depends materially on Provider Fulfilment SHALL retain enough immutable provenance to establish the governing configuration/binding contract.

At minimum the architecture SHALL permit reconstruction from:

```text
historical Merchant Configuration Revision
        ↓
exact Fulfilment Binding Set Revision
        ↓
resolved Fulfilment Plan / provider-contract provenance
```

A later provider switch SHALL NOT rewrite the binding provenance of earlier actions.

MS-PROT-040 and MS-PROT-054 remain authoritative for historical configuration and semantic-release affinity.

---

## 18. No duplicate activation lifecycle

The binding-set revision may have persistence/workflow states needed to construct and validate immutable revisions, but those states SHALL NOT become a second merchant-operational activation authority.

The only question that determines which binding set governs new activity is:

> **Which exact binding-set revision is pinned by the currently active Merchant Configuration Revision?**

This rule is invariant regardless of storage technology.

---

## 19. Open-first selection remains unchanged

MS-PROT-048 v1.1 remains authoritative for selection priority:

```text
modifiable open library
        ↓ if unsuitable / unsustainable / unsafe
open/self-hostable software
        ↓ if unsuitable / unsustainable / unsafe
paid external service
```

This amendment defines how an accepted routing choice is versioned and activated. It does not weaken or reorder v1.1's selection hierarchy.

---

## 20. Rejected alternatives

The following are rejected:

1. Embedding mutable provider connection/health/credentials in Merchant Configuration.
2. Treating a provider binding as a capability policy value.
3. Allowing ProviderConnection state to activate capability semantics.
4. A separately active Integration Configuration revision that can diverge from the active Merchant Configuration Revision.
5. Mutable `current binding` pointers used by historical Configuration Revisions.
6. Silent runtime provider switching outside an accepted fallback contract.
7. Duplicating unchanged binding data into every Merchant Configuration Revision when an immutable compatible binding-set revision can be safely reused.
8. Binding-set revisions that are not merchant-scoped or exact-revision identifiable.

---

## 21. Falsification

### 21.1 Provider-only routing change

```text
C27 → F8 → Provider A
C28 → F9 → Provider B
```

C28 activation atomically changes the governing static package. C27/F8 remain historical.

**PASS**

### 21.2 Business-hours-only change

```text
C28 → F9
C29 → F9
```

Same fulfilment routing is reused while a separate stable business setting changes.

**PASS**

### 21.3 Token rotation

```text
C28 → F9 → ProviderConnection PC17
PC17 credential generation 4 → 5
```

No new F or C revision is required solely for credential rotation.

**PASS**

### 21.4 Provider outage

```text
C28 → F9 → Provider P
Provider P readiness = UNHEALTHY
```

C28/F9 remain the governing configuration. Contextual execution/degradation changes; static configuration does not.

**PASS**

### 21.5 Binding cannot activate capability

```text
Scheduling inactive
F9 contains scheduling role binding
```

Static resolution rejects the contradiction rather than activating Scheduling.

**PASS**

### 21.6 Cross-merchant binding-set reference

```text
Merchant A configuration
    → Merchant B binding-set revision
```

Rejected by merchant-scope affinity.

**PASS**

---

## 22. Accepted invariants added by v1.2

1. Merchant-specific Provider Fulfilment routing is represented by immutable merchant-scoped `FulfilmentBindingSetRevision`.
2. Merchant Configuration Revision pins the exact binding-set revision used for its RCP.
3. Fulfilment Binding Set Revision has no independent operational activation authority.
4. Merchant Configuration Revision remains the sole configuration activation authority.
5. Stable routing changes create a new binding-set revision and a new Merchant Configuration Revision referencing it.
6. Unchanged compatible binding-set revisions may be reused by later Merchant Configuration Revisions.
7. Fulfilment binding selection is not Capability Configuration.
8. ProviderConnection identity may be referenced; credentials, health and mutable connection state remain outside static configuration.
9. Credential/health/transient provider changes do not cause new configuration revisions by themselves.
10. Exact merchant scope, revision identity and semantic-release affinity are validated statically.
11. A binding cannot make an inactive capability or role applicable.
12. Historical actions retain reconstructable governing binding/provider-contract provenance.
13. MS-PROT-048 v1.1 open-first selection priority remains unchanged.

---

## 23. Implementation consequence

Implementation may now proceed with a non-empty merchant `FulfilmentPlan` only through the governed hybrid path:

```text
MerchantConfiguration
    exact FulfilmentBindingSetRevisionReference
        ↓
resolve exact immutable FulfilmentBindingSetRevision
        ↓
validate merchant + semantic release + role/applicability + fulfiller
        ↓
materialise ResolvedFulfilmentBinding(s)
        ↓
ResolvedConfigurationPackage.fulfilmentPlan
```

Tests SHALL prove at minimum:

- exact revision reference is required;
- cross-merchant binding-set use fails;
- semantic-release mismatch fails;
- binding to an inactive capability-owned role fails;
- internal bindings do not require ProviderConnection;
- external bindings cannot smuggle mutable provider state into configuration;
- changing routing requires a new binding-set revision/reference while the old revision remains immutable;
- resolved package contains the exact selected binding provenance.

---

## Governance verdict

**ACCEPTED.** Manual approval selected the hybrid model because it preserves one activation authority while keeping technical routing configuration independently versionable and reusable.

### Canonical decision

> **Separate the revisioned artefacts; do not separate activation authority. Merchant Configuration Revision pins the exact immutable Fulfilment Binding Set Revision, and only activation of the Merchant Configuration Revision makes that routing effective for new activity.**
