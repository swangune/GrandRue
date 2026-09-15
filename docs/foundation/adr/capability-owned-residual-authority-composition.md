# ADR-009 — Capability-Owned Residual Authority Composition

> **ADR ID:** ADR-009
> **Status:** Accepted
> **Date:** 25 August 2026
> **Owner:** Architecture Team
> **Approved:** Manual approval on 25 August 2026

---

## 1. Context

MS-PROT-040 requires capability deactivation to preserve existing commitments. MS-PROT-049 therefore preserves the minimum merchant operating surface required to discharge outstanding residual obligations. MS-PROT-042 v1.4 establishes that Booking owns the authoritative decision about whether a Booking reservation obligation remains outstanding.

The existing Surface boundary already expresses the platform-facing question:

```text
ResidualSurfaceObligationAuthority

hasOutstandingObligations(
    MerchantScope,
    capabilityIdentifier
)
```

Booking now exposes the capability-owned authority:

```text
BookingResidualObligationAuthority

hasOutstandingBookingObligation(
    MerchantScope
)
```

The remaining implementation question is how Surface consumes capability-owned residual decisions without transferring lifecycle semantics into Surface or introducing capability-name branching.

---

## 2. Problem Statement

How shall Main Street compose multiple capability-owned residual-obligation authorities behind the existing Surface port while preserving semantic ownership, deterministic wiring and fail-closed residual management?

---

## 3. Options Considered

### Option A — Capability-name branching in Surface

Use `if`/`switch` logic on capability identifiers inside `ContextualSurfaceResolver` or another Surface component.

Rejected because Surface would accumulate knowledge of concrete capabilities and every new capability would require editing central Surface logic.

### Option B — Generic residual-obligation persistence or lifecycle DSL

Move residual truth into a platform-owned table, state machine or expression language.

Rejected because this would transfer semantic ownership away from the capability that owns the underlying commitment and would introduce generic lifecycle machinery without evidence.

### Option C — Runtime discovery or semantic-registry service lookup

Discover residual authorities through reflection, Spring bean names, classpath scanning or the semantic `CapabilityRegistry`.

Rejected because implementation-container identity must not become semantic identity, and semantic registries shall contain semantic definitions rather than live Java services.

### Option D — Immutable composition-root composite

Keep `ResidualSurfaceObligationAuthority` as the only Surface-facing port. At application assembly, create one immutable composite that binds registered capability identifiers to narrow delegates owned by those capabilities. Surface asks the composite; the composite routes by identity only; the owning capability decides residual truth.

---

## 4. Decision

Main Street adopts **Option D: immutable capability-owned residual authority composition**.

The application composition boundary shall construct an immutable implementation of `ResidualSurfaceObligationAuthority` from explicit capability-qualified bindings.

Conceptually:

```text
ContextualSurfaceResolver
        ↓
ResidualSurfaceObligationAuthority
        ↓
immutable composite
        ├── booking     → BookingResidualObligationAuthority
        ├── appointment → Appointment-owned residual authority
        └── ...
```

The composite owns **identity-to-delegate composition only**. It does not own or combine capability lifecycle semantics.

A capability binding may be represented by a narrow internal contract equivalent to:

```text
ResidualObligationBinding
{
    capabilityIdentifier
    residualObligationProbe
}
```

The probe asks one capability-owned authority for one merchant scope. The platform composite shall not evaluate internal reasons such as cancellation state, fulfilment outcome, payment state, reservation timestamps or other capability-owned facts.

---

## 5. Assembly Invariants

Application assembly shall validate the following invariants:

1. Every capability that owns a registered `ACTIVE_OR_RESIDUAL` merchant contribution has exactly one residual-obligation binding available to the Surface residual composite.
2. Duplicate bindings for the same capability identifier are rejected.
3. A missing required binding is an assembly/configuration failure; it shall not silently evaluate to `false`.
4. Runtime lookup of an unbound capability fails closed rather than hiding a potentially required residual-management surface.
5. Additional capability-owned bindings may exist without transferring semantic ownership to Surface.
6. The composite and its binding set are immutable after assembly.

The validation concerns capability identity and required binding presence only. It does not validate or reinterpret the capability's internal residual semantics.

---

## 6. Ownership Boundary

```text
Capability-owned authoritative facts
        ↓
Capability-owned residual authority
        ↓
boolean result for MerchantScope
        ↓
immutable Surface residual composite
        ↓
ResidualSurfaceObligationAuthority
        ↓
ContextualSurfaceResolver
```

Hard rules:

- Surface composition asks; the owning capability decides.
- The composite delegates exactly once to the selected capability authority.
- Surface shall not aggregate multiple internal predicates for one capability.
- The semantic `CapabilityRegistry` shall not become a service locator.
- Spring bean names, reflection or classpath discovery shall not define semantic routing.
- No generic residual lifecycle or generic residual persistence model is introduced by this ADR.

---

## 7. Consequences

### Positive

- Adding a new capability does not require editing `ContextualSurfaceResolver`.
- Capability-specific residual semantics remain capability-owned.
- Missing residual authority cannot silently remove required merchant-management surfaces.
- Wiring is deterministic, explicit and testable.
- The architecture reuses the existing Surface port rather than introducing a new routing subsystem.

### Negative

- The application composition root must explicitly provide one binding for every residual-manageable capability.
- Assembly validation must compare the pinned Surface contribution definitions with the available residual bindings.
- Each new residual-manageable capability requires a small composition binding in addition to its capability-owned authority.

---

## 8. Initial Implementation Scope

The first executable binding is Booking:

```text
booking
    → BookingResidualObligationAuthority
```

The initial implementation shall prove:

- Booking residual truth is delegated to `BookingResidualObligationAuthority`;
- duplicate Booking bindings are rejected;
- a registered Booking `ACTIVE_OR_RESIDUAL` contribution without a Booking binding is rejected during assembly;
- an unbound runtime capability lookup fails closed;
- no Booking lifecycle semantics are added to Surface.

Appointment, Order and other capability-owned residual authorities are outside this implementation slice until their accepted semantics and authorities exist.

---

## 9. Rationale

This decision preserves the accepted semantic ownership boundary while solving only the implementation-composition problem that exists today.

The existing Surface port already asks the correct generic question, so adding a separate router or service registry would duplicate responsibility. An immutable composite is sufficient to connect that port to capability-owned authorities without making Surface aware of Booking, Appointment, Order or other capability internals.

Fail-closed assembly validation is required because silently treating an absent binding as `false` could remove a residual-management surface while the owning capability still has outstanding obligations. Explicit immutable bindings make the composition deterministic and testable while avoiding speculative generic lifecycle machinery.

---

## 10. Affected Components

This ADR affects only implementation composition around:

```text
mainstreet.surface.ResidualSurfaceObligationAuthority
mainstreet.surface.ContextualSurfaceResolver
capability-owned residual-obligation authorities
application/runtime composition wiring
SurfaceContributionRegistrySnapshot assembly validation
```

It does not alter the semantic ownership or lifecycle rules of Booking, Appointment, Order, Payment, Notification, Provider Fulfilment or other capabilities.

---

## 11. Future Considerations

Future residual-manageable capabilities may add their own capability-owned residual authority and one explicit application-composition binding.

If evidence later shows that explicit immutable composition becomes operationally inadequate, a replacement architecture may be proposed through a new ADR. Any such replacement must preserve the semantic rule that capability-owned residual truth is not transferred to Surface or to a generic platform lifecycle model.

No dynamic discovery mechanism, generic residual lifecycle DSL, or generic residual persistence model is authorised by this ADR.
