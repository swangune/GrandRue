# MS-PROT-020 — Canonical Semantic Schema & Compiler Boundary

**Version:** 1.3  
**Status:** **Accepted**  
**Evidence base:** Validated findings from MS-PROT-005 through MS-PROT-019; the capability-composition conclusions are incorporated directly below  
**Purpose:** Define the minimum semantic authority model and the boundary between platform-owned semantics, merchant configuration, deterministic compilation and runtime execution.

## 1. Governing principle

> **Main Street owns semantic meaning. Merchant configuration references and parameterises supported semantics. The compiler validates and resolves; it does not invent behaviour.**

```text
Platform-owned semantics
        ↓
Registered definitions
        ↓
Merchant configuration
        ↓
Deterministic compiler/resolver
        ↓
Resolved operational model
        ↓
Generic runtime
```

AI may assist before configuration acceptance, but has no independent semantic authority.

## 2. Scope

MS-PROT-020 governs semantic definitions, configuration, compilation, runtime concepts and domain facts/evidence. It is not a universal ontology for identity, tenancy, media, presentation or every platform concern.

Four broad categories are sufficient:

```text
DEFINITIONS     platform-owned semantic meaning
CONFIGURATION   merchant-scoped permitted selections/values
RUNTIME         context, intent, operational objects and decisions
FACTS/EVIDENCE  meaningful outcomes and execution evidence
```

## 3. Construction hierarchy

Main Street builds reusable business machinery through a controlled hierarchy:

```text
Platform invariants
        ↓ constrain
Primitives
        ↓ generalised by
Abstractions
        ↓ composed and bounded by
Frameworks
        ↓ realised and owned by
Capabilities
        ↓ expose
Policies and configuration surfaces
        ↓ selected and valued by
Merchant configuration
```

The terms have distinct meanings:

- A **Primitive** is the smallest reusable semantic contract or deterministic behaviour that is worth naming independently. It is platform-owned and carries no merchant-specific meaning by itself.
- An **Abstraction** is a shared semantic contract that captures meaning common to more than one concrete use. It may define vocabulary and extension points, but it does not own merchant state or runtime authority.
- A **Framework** is a validated composition of primitives and abstractions with explicit invariants and bounded extension points. It is reusable machinery, not a merchant, industry or website template.
- A **Capability** is a concrete semantic owner. It owns the meaning of its applicable operations, resources, policies and authoritative fulfilment behaviour, while collaborating through explicit contracts.
- A **Policy** exposes bounded variability already owned by a capability or other explicit semantic owner.
- **Merchant configuration** selects capabilities and their exposed configuration surfaces; it does not assemble raw primitives or frameworks.

Primitive, Abstraction and Framework describe how platform semantics are constructed and reused. They need not each become a globally registered runtime object. Their contracts must nevertheless remain traceable to the Capability or platform invariant that owns executable behaviour.

This hierarchy replaces the missing external “Capability Composition Revisit” dependency: its accepted conclusion is that reusable construction belongs below explicit capability ownership and must never become merchant-specific template generation.

## 4. Semantic identity and ownership

Minimum semantic identity:

```text
SemanticIdentity
{
    identifier
    kind
}
```

Versioning is separate from semantic identity and remains an evolution concern.

Not every semantic object requires global registration. Top-level concepts may be globally addressable while `State`, `Transition` and `Operation` commonly remain owner-scoped.

Every semantic behaviour must have clear ownership even when definitions are related rather than physically nested.

## 5. Canonical semantic concepts

Minimum top-level concepts:

```text
Capability
OfferingSchema
Resource
DataConcept
Requirement
Policy
EventDefinition
```

Common owner-scoped concepts:

```text
Operation
State
Transition
```

`Rule` remains a bounded platform-owned deterministic consequence; no general rule DSL is accepted. Invariants are platform/domain truths enforced at the appropriate architectural boundary and need not be registry objects.

`OfferingSchema` is a platform-owned contract describing permitted offering structure, parameter types and capability/resource bindings. A merchant's configured offering is data conforming to that schema, not a new semantic definition.

## 6. Key semantic boundaries

- `Resource`/`ResourceDefinition` is semantic meaning; `ConfiguredResource` is stable merchant configuration; `ResourceInstanceState` is live operational state.
- `OfferingSchema` defines permitted structure; `MerchantOffering` supplies merchant-owned identity, content and commercial/configuration values.
- `Operation` defines what may be done; `Command` is a concrete request to perform it.
- An Operation need not universally be one state transition.
- `DataConcept` defines meaning; it is not a data instance or Requirement.
- `Requirement` is an explicit satisfiable prerequisite belonging to a semantic context; it is not every runtime guard or invariant.
- `Policy` is bounded merchant-controlled variability within semantics that already exist; Policy cannot create a capability.
- Policy selection must distinguish `NOT_APPLICABLE`, `DEFAULTED` and `EXPLICITLY_SELECTED` where relevant.
- `AvailabilityDecision` is contextual and advisory; it cannot create `Allocation`, `Hold` or `Commitment`.
- `Allocation` is an authoritative scoped claim against applicable capacity.
- `Hold` is an optional temporary pre-commitment claim; payment-required does not imply hold-required.
- `OperationExecution` is execution evidence, not automatically a Domain Event.
- `DomainEvent` is a meaningful immutable fact owned by its semantic capability.

## 7. Capability relationships

Cross-capability collaboration has three distinct forms:

1. **Static semantic relationships** — e.g. `REQUIRES`, `SUPPORTS`, `DEPENDS_ON`, `CONFLICTS_WITH`.
2. **Synchronous coordination** — where multiple capability effects jointly establish one invariant.
3. **Independent event reaction** — where a committed fact is observed by another capability.

A universal `trigger → consequence` operation-composition mechanism is rejected.

## 8. Merchant configuration boundary

Merchant configuration may reference registered semantics and supply values within their permitted configuration surface. It cannot contain:

```text
new capabilities
new operations
new states/transitions
new requirements
new policies/policy values
arbitrary executable rules
arbitrary code
raw primitive/framework composition
```

Business category is contextual information only. It must not determine generic compiler/runtime behaviour.

## 9. Compiler boundary

The compiler/resolver may:

- resolve registered references;
- verify identity, kind, ownership and applicability;
- validate capability dependencies/conflicts;
- validate policy selections;
- apply bounded platform-owned semantic consequences;
- validate invariants;
- materialise a merchant-scoped resolved operational model.

It may not:

- invent semantics or relationships;
- infer behaviour from business category, identifier names or prose;
- generate arbitrary runtime code;
- propagate initiating-actor authority into downstream operations.

## 10. Runtime boundary

Runtime concepts remain conceptually distinct:

```text
Interaction
Command
ResourceInstance / ResourceInstanceState
AvailabilityDecision
Allocation
Hold
OperationExecution
DomainEvent
```

`Commitment` remains a cross-domain concept representing an accepted operational obligation; its universal representation is deliberately deferred.

Stable command identity is required where duplicate delivery/retry makes repeated execution material; lifecycle rejection alone is not idempotency.

## 11. Handover alignment — 20 August 2026

The broader merchant product boundary does not alter this semantic architecture.

Main Street may support physical merchants, online consultants, information publishers and hybrid operators through the same semantic/configuration machinery. Physical premises are not a semantic eligibility requirement. AI inference may propose supported semantics, but merchant choice and compiler validity remain authoritative.

## 12. Accepted invariants

1. Platform semantics own meaning; configuration cannot redefine it.
2. AI/inference selects from supported semantics but cannot invent semantic relationships or runtime behaviour.
3. Business category and physical presence are not runtime architecture.
4. Requirements, Policies, guards, invariants and data remain distinct.
5. Availability is advisory; authoritative allocation/commitment requires revalidation.
6. Capability ownership survives composition.
7. No universal workflow, rule language or operation-chaining mechanism is accepted.
8. Runtime evidence and domain facts remain distinct.
9. Merchant configuration is declarative and bounded.
10. The resolved operational model is derived, merchant-scoped and runtime-consumable; it is not a second semantic registry.
11. Primitives, abstractions and frameworks are platform construction mechanisms, not merchant or industry templates.
12. Capability ownership is the boundary at which reusable construction acquires concrete semantic and runtime authority.
13. Merchants configure capabilities and their bounded surfaces; they do not compose raw primitives or frameworks.
14. Offering schema and merchant offering data remain distinct.

## 13. Deferred decisions

Detailed merchant configuration belongs to MS-PROT-021. Version compatibility, schema evolution, persistence, APIs, presentation and identity/trust are governed by downstream specifications.

## Governance verdict

**ACCEPTED.** Earlier candidate defects were removed through review/falsification and subsequent documents depend on this revised contract.

**Revision 1.3:** Incorporated the previously external capability-composition conclusion, defined Primitive/Abstraction/Framework/Capability boundaries, rejected templates as a construction mechanism, and added the OfferingSchema boundary.
