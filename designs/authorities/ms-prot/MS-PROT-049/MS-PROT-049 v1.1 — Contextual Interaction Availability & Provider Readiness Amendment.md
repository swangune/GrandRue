# MS-PROT-049 v1.1 — Contextual Interaction Availability & Provider Readiness Amendment

**Document ID:** MS-PROT-049  
**Version:** 1.1  
**Status:** **ACCEPTED after design review and manual approval**  
**Amends:** MS-PROT-049 v1.0  
**Depends on:** MS-PROT-022 v1.5, MS-PROT-048 through v1.3, MS-PROT-062 v1.1, MS-PROT-068 v1.0, MS-PROT-070 v1.0  
**Purpose:** Define the bounded contextual relationship between provider readiness and surface interaction availability without allowing dependency health to redefine capability/surface meaning, grant operation authority, enter the immutable Resolved Configuration Package as live truth, or create provider-specific UI semantics.

---

## 1. Governing decision

> **Surface contribution inclusion, interaction availability and backend operation authority are distinct decisions. Provider Readiness may affect the current availability of an explicitly provider-dependent interaction, but it shall not silently remove the semantic workspace, deactivate the owning capability, grant authority, or redefine the contribution itself.**

Canonical separation:

```text
registered surface contribution
        +
contextual inclusion evidence
        ↓
contribution included
        │
        ├── non-interaction contribution
        │       → presentation remains governed by ordinary composition
        │
        └── interaction contribution
                +
          current dependency readiness
                ↓
          interaction availability

backend execution authority
        remains independently enforced
```

Hard rule:

```text
surface inclusion
        ≠
interaction availability
        ≠
operation authority
```

---

## 2. Scope

This amendment governs the first Provider Readiness surface slice for contextually included **MERCHANT ACTION** contributions.

It defines:

```text
interaction availability states
explicit fulfilment-role dependency
live readiness consumption
fail-closed unavailable handling
degraded interaction handling
workspace-preservation rule
authority separation
attention non-synthesis
```

It does not define:

```text
ProviderConnection storage
provider-health calculation
health-check transport
provider credentials
circuit-breaker state
retry/fallback policy
frontend widgets
button styling
route behaviour
customer/public interaction availability
arbitrary eligibility expressions
```

Those remain governed by their owning authorities or later amendments.

---

## 3. Interaction availability algebra

The initial presentation-neutral interaction availability algebra is:

```text
AVAILABLE
DEGRADED
UNAVAILABLE
```

Meaning:

- **AVAILABLE** — current dependency evidence does not prevent presentation of the interaction as normally usable.
- **DEGRADED** — the interaction remains semantically applicable, but current dependency conditions require the presentation to communicate reduced or impaired operability.
- **UNAVAILABLE** — the interaction remains semantically recognisable where otherwise included, but current dependency evidence does not justify presenting it as executable now.

These states are contextual presentation/operability evidence. They are not business state and do not grant or revoke backend operation authority.

---

## 4. Explicit fulfilment-role dependency

A provider-dependent ACTION contribution shall identify the registered **Fulfilment Role** whose current readiness affects that interaction.

Conceptually:

```text
SurfaceInteractionAvailabilityContract
{
    requiredFulfilmentRole?
}
```

The reference is static semantic/configuration metadata. It may therefore travel with the static surface definition/catalogue.

The current readiness of that role is live contextual state and shall not be captured as immutable truth in the Resolved Configuration Package.

Hard rule:

> **The surface resolver shall not infer a fulfilment dependency from provider name, operation name, capability identifier, route, presentation label or business category.**

---

## 5. Initial applicability boundary

For this amendment, a fulfilment-role interaction dependency may be declared only by a contribution of kind:

```text
ACTION
```

Other contribution kinds do not acquire an interaction-availability state merely because they share a composition target with a provider-dependent action.

This prevents provider degradation from collapsing an entire workspace or other semantic surface.

---

## 6. Live readiness authority

The surface layer may consume a current readiness projection from the authority responsible for provider/fulfilment operational readiness.

Conceptually:

```text
MerchantScope
    +
FulfilmentRoleIdentity
        ↓
current readiness authority
        ↓
interaction-availability projection
```

The surface layer does not own ProviderConnection state, credentials, health observations or resilience policy.

MS-PROT-048 remains authoritative for fulfilment binding/provider participation. MS-PROT-068 remains authoritative for operational health evidence. MS-PROT-070 remains authoritative for controlled degradation, retry/fallback and dependency failure consequences.

---

## 7. Fail-closed dependency evidence

For a provider-dependent ACTION contribution, missing, unknown, stale-beyond-contract or otherwise unproven readiness shall not be treated as AVAILABLE.

Initial surface rule:

```text
readiness proves normal operation
        → AVAILABLE

readiness proves bounded degraded operation
        → DEGRADED

readiness proves unavailable
or readiness is not established
        → UNAVAILABLE
```

This does not imply that every missing telemetry signal means the provider is business-failed. It means only that the surface layer lacks sufficient current evidence to present the dependent interaction as normally available.

---

## 8. Workspace-preservation invariant

Provider degradation shall not redefine contribution inclusion for an otherwise legitimate workspace.

Example:

```text
Scheduling active
Calendar workspace eligible
external calendar fulfilment bound
provider unavailable
```

Required result:

```text
Calendar workspace             INCLUDED
provider-dependent action      INCLUDED + UNAVAILABLE
unrelated action               unaffected
```

Rejected:

```text
provider unavailable
        ↓
Calendar workspace removed
        ↓
Scheduling appears deactivated
```

This preserves MS-PROT-049 v1.0 section 19.

---

## 9. Degraded interaction invariant

A degraded provider condition may produce:

```text
ACTION included
        +
interaction availability = DEGRADED
```

It shall not by itself:

```text
deactivate capability
remove workspace
change merchant configuration
rewrite fulfilment binding
activate fallback
invent successful execution
```

Any fallback/rebinding behavior remains governed by MS-PROT-048 and MS-PROT-070.

---

## 10. Actor authority precedes usability presentation

Current actor authority remains an independent contextual filter.

Example:

```text
provider ready
        +
actor lacks required privilege
        ↓
ACTION not included for that actor
```

The provider being AVAILABLE cannot manufacture actor authority.

Likewise, an ACTION being rendered as AVAILABLE does not authorize backend execution. The runtime execution boundary must revalidate applicable authority according to MS-PROT-023/MS-PROT-062.

---

## 11. Independent interactions remain independent

A provider condition affects only interactions that explicitly declare the corresponding fulfilment-role dependency.

Example:

```text
Calendar workspace
    ├── View appointments            independent
    ├── Create internal appointment  independent
    └── Push to external calendar    depends on external-calendar role
```

If the external provider degrades:

```text
View appointments            unaffected
Create internal appointment  unaffected
Push to external calendar    DEGRADED / UNAVAILABLE
```

No composition-target-wide degradation is inferred.

---

## 12. Attention is not synthesized

Provider degradation may make an already-registered `ATTENTION` contribution applicable under its own accepted contextual contract.

This amendment does not authorise the resolver to invent an ATTENTION contribution merely because a provider is degraded or unavailable.

Hard rule:

> **Operational health evidence may affect registered contributions; it does not create new surface semantics.**

---

## 13. Static/dynamic boundary

The static surface contribution may retain:

```text
interaction availability contract
required FulfilmentRoleIdentity
```

The immutable RCP/static catalogue shall not retain as current truth:

```text
provider currently healthy
provider currently degraded
provider currently unavailable
connection currently authorised
current circuit state
current latency
current outage
```

Those are contextual/live facts.

A change in provider readiness alone therefore does not create a new Merchant Configuration Revision or Resolved Configuration Package.

---

## 14. No universal rule DSL

This amendment does not introduce:

```text
AND / OR expression trees
scripts
SpEL
merchant-authored predicates
AI-authored executable conditions
provider-name branching
business-category branching
```

The initial contract is intentionally bounded to one optional registered fulfilment-role dependency for an ACTION contribution.

New dependency shapes require evidence of materially different semantics and the normal design-governance process.

---

## 15. Cross-domain falsification

### External calendar unavailable

```text
Calendar workspace             INCLUDED
External-calendar action       INCLUDED + UNAVAILABLE
```

Workspace meaning survives provider failure.

**PASS**

### External calendar degraded

```text
Calendar workspace             INCLUDED
External-calendar action       INCLUDED + DEGRADED
```

**PASS**

### External calendar ready

```text
External-calendar action       INCLUDED + AVAILABLE
```

**PASS**

### Restricted staff actor

```text
Provider ready
Staff lacks action privilege
        ↓
provider-dependent ACTION excluded
```

Provider readiness does not grant actor authority.

**PASS**

### Unrelated internal action

```text
External provider unavailable
Internal action has no fulfilment-role dependency
        ↓
Internal action availability unaffected
```

**PASS**

### Missing readiness evidence

```text
ACTION has explicit fulfilment-role dependency
readiness not established
        ↓
UNAVAILABLE
```

Fail closed without redefining capability semantics.

**PASS**

### Provider failure without registered attention

```text
provider unavailable
no ATTENTION contribution registered
        ↓
no ATTENTION contribution invented
```

**PASS**

---

## 16. Rejected alternatives

| Alternative | Rejection reason |
|---|---|
| Hide workspace when provider fails | Confuses technical fulfilment health with semantic applicability |
| Remove provider-dependent ACTION entirely on outage | Loses useful contextual meaning and repair/degradation presentation; authority filtering remains a separate reason for exclusion |
| Put readiness inside the RCP | Violates MS-PROT-022 static/dynamic boundary |
| Infer dependency from provider/operation names | Creates hidden coupling and business/provider-specific branching |
| Let surface availability grant command authority | Violates MS-PROT-023/MS-PROT-049 authority separation |
| Auto-create attention on provider failure | Invents unregistered surface semantics |
| Auto-switch provider | Violates fulfilment binding and resilience authority |
| Add generic predicate DSL | Premature second programming language |

---

## 17. Implementation acceptance criteria

An implementation conforming to this amendment shall provide executable evidence that:

1. a provider-dependent MERCHANT ACTION may resolve to AVAILABLE, DEGRADED or UNAVAILABLE;
2. a provider-unavailable ACTION remains semantically included when its ordinary contextual eligibility is satisfied;
3. provider failure does not remove the containing workspace;
4. an unrelated interaction remains unaffected;
5. an unauthorized actor does not receive an ACTION merely because its provider is ready;
6. missing/unproven readiness fails closed to UNAVAILABLE for an explicitly dependent ACTION;
7. non-ACTION contributions cannot declare the initial fulfilment-role interaction dependency;
8. the static catalogue preserves only the dependency reference, not live readiness;
9. no ATTENTION contribution is synthesized;
10. backend operation authority remains independently enforced.

---

## 18. Amendment effect

This amendment is additive and narrower than MS-PROT-049 v1.0.

It does not alter:

```text
surface contribution ownership
audience model
composition-target semantics
residual-management semantics
actor-authority separation
projection authority
fulfilment binding authority
ProviderConnection authority
resilience/fallback authority
```

Where this amendment is more specific about provider-readiness consequences for contextually included MERCHANT ACTION contributions, v1.1 governs. All unrelated MS-PROT-049 v1.0 decisions remain in force.
