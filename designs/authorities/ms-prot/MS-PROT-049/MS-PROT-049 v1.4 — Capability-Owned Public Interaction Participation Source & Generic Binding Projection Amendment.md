# MS-PROT-049 v1.4 — Capability-Owned Public Interaction Participation Source & Generic Binding Projection Amendment

**Document ID:** MS-PROT-049  
**Version:** 1.4  
**Status:** **ACCEPTED by manual approval on 3 September 2026**  
**Approved:** Manual approval on 3 September 2026 after in-chat proposal, authority review, dependency-cycle proof, cross-domain falsification, ambiguity review and recommendation  
**Amends:** MS-PROT-049 v1.0–v1.3 within production Public Interaction participation-source and binding-projection scope  
**Depends on:** composite MS-PROT-027; MS-PROT-043 v1.4; MS-PROT-046 v1.2; MS-PROT-049 v1.0–v1.3; MS-PROT-059; MS-PROT-062  
**Governed by:** `DESIGN-RULES.md`; `MS-AVS-001`  
**Purpose:** Define the missing production contract through which capability-owned subject-interaction participation is supplied to the generic Public Interaction Binding projection, while permitting the generic binding infrastructure to exist safely with zero registered production participation sources.

---

## 1. Governing Decision

MS-PROT-049 v1.2 remains authoritative that Public Interaction Binding is a projection of independently authoritative subject-interaction participation and owns none of that participation truth.

This amendment establishes the production boundary required to implement that rule.

Canonical:

```text
CAPABILITY / CONFIGURATION / RELATIONSHIP OWNER
        ↓
authoritative subject-interaction participation
        ↓
CAPABILITY-OWNED PARTICIPATION SOURCE
        +
current applicable PUBLIC_INTERACTION contribution
        +
already-serviceable and publicly exposed projection material
        ↓
GENERIC PUBLIC INTERACTION BINDING PROJECTION
        ↓
audience-safe Public Interaction Binding
```

Hard invariant:

```text
generic binding projector
    ≠ participation authority

participation source
    ≠ Exposure authority

participation fact
    ≠ availability
    ≠ authorisation
    ≠ execution permission
```

A generic production binding projector MAY be fully implemented while the set of registered production participation sources is empty.

With zero applicable authoritative sources:

```text
no authoritative participation fact
        ↓
no subject binding
```

This is a valid fail-closed production state.

It is not an error, placeholder relationship or permission to infer participation.

---

## 2. Public Interaction Participation Source

A **Public Interaction Participation Source** is a capability-owned production boundary that establishes positive subject-interaction participation from an already accepted semantic/configuration/typed-relationship authority.

A source may materialise participation from, as applicable:

```text
registered capability semantics
resolved merchant configuration
Offering-supported-operation semantics
Publication participation definitions
Booking/Appointment subject semantics
Enquiry SUBJECT relationships
another separately accepted participation authority
```

The generic Surface layer SHALL NOT decide which business facts make the participation true.

The source SHALL remain owned by the capability or accepted semantic authority that owns that fact.

---

## 3. Registered Participation Definition

Where participation must be registered rather than inferred dynamically, Main Street SHALL represent it through an owner-qualified, release-affined **Public Interaction Participation Definition**.

The definition MUST preserve enough information to determine:

```text
participation owner
subject semantic family / applicability
PUBLIC_INTERACTION contribution
applicable registered operation
owner-specific participation role where materially required
semantic-release affinity
```

The exact Java representation is implementation detail.

A registered definition identifies legitimate participation semantics.

It MUST NOT become:

```text
availability state
price authority
stock authority
capacity authority
provider readiness
actor authorisation
commercial entitlement
execution permission
frontend presentation metadata
```

A definition MAY establish uniform participation for an entire accepted subject family where the owning authority explicitly defines that meaning.

Otherwise the capability-owned source MUST establish the instance-specific positive fact.

---

## 4. Participation Fact

A positive participation result MUST preserve sufficient authoritative identity to establish:

```text
Merchant Scope
semantic subject owner / namespace
semantic subject type or kind
semantic subject identity
PUBLIC_INTERACTION contribution identity
registered operation reference
participation-source / definition provenance
applicable owner-owned role information where required
```

It MUST also preserve sufficient semantic/configuration affinity to prevent a result established for one incompatible resolved merchant context from being silently reused in another.

Exact representation of that affinity is implementation detail.

A generic string such as:

```text
subjectReference = "123"
```

is insufficient as the sole semantic identity where it cannot be authoritatively resolved to the required subject identity.

Opaque public-safe transport references remain permitted under MS-PROT-049 v1.2.

---

## 5. Source Registration and Single Ownership

Participation sources MUST be registered through an owner-qualified production boundary.

The generic binding projector MUST NOT discover semantic owners through:

```text
business category
subject type convention
Java class name
frontend route
component name
provider
presentation label
AI inference
```

Registered source routing may use semantic identifiers declared by accepted authority.

For any exact participation fact, the system MUST resolve to at most one semantic owner.

If two registered production sources claim authority over the same exact participation fact and accepted authority does not define a deterministic ownership distinction:

```text
ambiguous ownership
        ↓
structural configuration/assembly failure
```

Main Street MUST NOT use first-wins, arbitrary priority or runtime guessing.

---

## 6. Zero-Source Production State

An empty participation-source registry is valid.

Canonical behaviour:

```text
generic S3 projector present
+
zero registered production participation sources
        ↓
successful empty subject-binding result
```

The following are prohibited:

```text
default every exposed subject to the interaction
generate placeholder bindings
assume all Offerings support the interaction
assume all Publications support Enquiry
use the interaction contribution itself as participation proof
treat Exposure membership as participation
```

This rule distinguishes:

```text
generic infrastructure completeness
        ≠
concrete capability participation portfolio completeness
```

A test-only source fixture MAY be used to prove the generic projector's positive-path mechanics.

A test fixture does not count as a production participation source.

---

## 7. Generic Binding Projection Responsibility

The generic Public Interaction Binding projector owns only composition.

It SHALL consume:

```text
currently applicable PUBLIC_INTERACTION contribution semantics
+
owner-established participation results
+
public projection material that has already satisfied applicable
Projection Serviceability and Exposure constraints
```

It SHALL produce audience-safe bindings only for the intersection of those independently established facts.

Therefore:

```text
participates but not publicly exposed
    → no public binding

publicly exposed but no participation fact
    → no public binding

PUBLIC_INTERACTION contribution absent
    → no binding for that interaction

all required dimensions satisfied
    → binding may be projected
```

The projector SHALL NOT reacquire owner business values merely because a candidate survived Exposure.

It SHALL NOT reinterpret Projection material to invent participation.

---

## 8. Projection-Material Affinity

S3 SHALL compose against the already-governed public Projection/Exposure path rather than create a competing read path.

Where Public Interaction Binding is derived from projected subject material, the implementation MUST preserve an exact association between:

```text
the subject represented by the selected public material
and
the subject for which participation was established
```

That association MUST be owner-established or mechanically proven.

The generic projector MUST NOT conclude semantic identity merely because two arbitrary strings, labels or object kinds appear equal.

MS-PROT-027's bounded-read and same-observation guarantees remain authoritative for projected business values.

This amendment does not convert `BoundedProjectionReadBinding` into:

```text
semantic subject identity
participation identity
continuation identity
execution authority
```

---

## 9. Merchant, Release and Context Isolation

A participation result SHALL NOT be reusable across an incompatible Merchant Scope or semantic context.

At minimum, implementations MUST reject or withhold results when the required affinity cannot be established across:

```text
Merchant Scope
semantic registry release
applicable resolved merchant semantics
interaction contribution
subject identity
participation source
```

Cross-merchant reuse MUST fail closed.

Cross-release reuse MUST fail closed where the participation definition or source is release-affined.

A stale result MUST NOT manufacture a new binding under incompatible current semantics.

---

## 10. Missing, Unresolved and Malformed Sources

The following candidate-local situations produce no positive binding:

```text
no applicable participation source
owner establishes no participation
participation cannot currently be resolved
subject is not represented in the public selected material
subject is withheld by Exposure
required public projection is not serviceable
```

A structural contradiction such as:

```text
wrong Merchant Scope
wrong semantic release
duplicate semantic ownership
result claims a different contribution than the evaluated one
result claims a different subject than its established candidate affinity
```

MUST fail closed rather than silently degrade into a different binding.

The implementation MAY distinguish candidate-local absence from whole-resolution structural failure where existing Projection/Exposure architecture already requires that distinction.

---

## 11. Many-to-Many Preservation

The production contract MUST preserve MS-PROT-049 v1.2 many-to-many semantics.

Valid outcomes include:

```text
Subject A → Appointment
Subject A → Enquiry

Subject B → Ordering

Appointment ← Subject A
Appointment ← Subject C
Appointment ← Subject D
```

The generic projector MUST NOT assume one subject, one interaction or one source globally.

Single ownership applies to each exact participation fact, not to the entire Public Interaction subsystem.

---

## 12. Participation Role Boundary

Where a participation relationship requires an owner-specific semantic role, the participation source MUST preserve sufficient role information for authoritative backend resolution.

Generic Surface MUST NOT invent a universal role taxonomy merely for presentation.

The browser MUST NOT be required to know internal execution-role identifiers.

Role information carried through a binding remains semantic context, not execution authority.

---

## 13. Participation Remains Distinct from Availability

A positive participation fact may remain valid while execution is temporarily impossible.

Examples:

```text
Offering supports Appointment
but no slots are currently free

Room proposition supports Booking
but requested dates have no capacity

Product supports Ordering
but current stock is zero
```

Therefore a participation source MUST NOT redefine the stable relationship merely because current availability is negative.

Applicable availability, capacity, stock and scheduling authorities remain independently governing.

---

## 14. Runtime Execution Revalidation

Possession or rendering of a Public Interaction Binding remains non-authoritative for mutation.

When an interaction is submitted, the applicable application/capability boundary MUST re-establish current participation and all other required runtime authorities under existing MS-PROT-049, MS-PROT-059 and MS-PROT-062 rules.

A stale projected binding does not grandfather new activity.

---

## 15. Initial Publication → Enquiry Source

MS-PROT-046 v1.2 remains authoritative that the first production portfolio includes:

```text
Opportunity
    → enquiry / send-enquiry
```

and that Publication owns subject participation where registered for this Target-10 composition.

This concrete production participation definition/source belongs to the Publication → Enquiry vertical slice.

It SHALL use the generic participation-source contract established by this amendment.

The generic S3 infrastructure SHALL NOT duplicate or pre-implement Publication business semantics merely to make IMP-06 complete.

---

## 16. Information-Only and General Enquiry Boundary

A merchant-general Enquiry may legitimately exist without a subject-specific binding.

Therefore:

```text
general enquiry interaction applicable
+
no subject-specific participation source
```

does not require a manufactured generic subject binding.

The subject-specific S3 projection and merchant-general public interaction remain distinct.

---

## 17. Programme Consequence

This amendment does not change the `MS-IMP-001` macro dependency:

```text
IMP-06 HARD → IMP-07
```

It removes the hidden cycle inside S3 by distinguishing generic infrastructure from concrete capability participation.

The executable sequence becomes:

```text
IMP-06
    generic participation-source contract
        ↓
    generic S3 binding projector
        ↓
    zero production sources is valid and fail-closed
        ↓
    generic S3 infrastructure can be conformingly completed

IMP-06 COMPLETE
        ↓ HARD
IMP-07 Publication → Enquiry
        ↓
    register first real production participation source
        ↓
    Opportunity → enquiry/send-enquiry
        ↓
    prove positive vertical integration
```

No Publication, Enquiry, Booking, Appointment or Ordering semantic implementation is pulled into IMP-06.

Concrete source implementation remains with its owning capability target.

---

## 18. S3 Conformance Boundary

Generic S3 conformance SHALL prove at minimum:

```text
zero registered sources → empty bindings
missing source never causes inference
one valid test source can produce a positive binding
source result cannot bind an unexposed/unserviceable subject
wrong Merchant Scope fails closed
wrong release/context fails closed
duplicate source ownership is rejected
wrong contribution affinity is rejected
wrong subject affinity is rejected
many-to-many bindings remain representable
projector owns no owner-private business semantics
no availability/execution authority enters the binding
```

Generic S3 completion does NOT claim that any particular production interaction family has a live participation source.

Concrete production source conformance is proved by its owning vertical slice.

---

## 19. Rejected Alternatives

The following alternatives are rejected.

### Infer participation from Surface or Exposure

Rejected because it violates MS-PROT-049's single-owner participation invariant.

### Pull Opportunity → Enquiry production semantics into IMP-06

Rejected because it moves concrete Publication/Enquiry capability behaviour ahead of the first governed vertical slice solely to satisfy programme ordering.

### Amend MS-IMP-001 so IMP-07 may begin before IMP-06

Rejected because the deadlock can be removed without weakening the existing HARD architectural proof sequence.

### Mark S3 complete while implementing no generic participation boundary

Rejected because it would remove an accepted Public Interaction Binding responsibility rather than implement it.

### Build a universal interaction/subject rules engine

Rejected as unnecessary abstraction and a semantic-gravity risk.

The smallest adequate repair is the generic owner-routed source contract plus fail-closed projector.

---

## 20. Falsification Record

**No production sources registered:** PASS — output is empty without inference.

**Publication exposed, Enquiry active, but no registered Opportunity participation:** PASS — no Opportunity binding.

**Opportunity source arrives in IMP-07:** PASS — generic S3 requires no redesign.

**Two capabilities claim the same exact participation fact:** PASS — ambiguity is rejected rather than arbitrated by Surface.

**Subject is exposed but not a participant:** PASS — no binding.

**Subject participates but Exposure WITHHOLDs it:** PASS — no public binding.

**Subject participates but current stock/capacity is zero:** PASS — participation remains semantically distinct from availability.

**Cross-merchant source result reuse:** PASS — fails closed.

**Stale binding submitted after participation removal:** PASS — execution revalidates current participation.

**Information-only merchant with general Enquiry and no subject sources:** PASS — general Enquiry remains valid without a fake subject.

**Future Appointment/Booking/Ordering sources:** PASS — each may supply its own owner-established participation without changing the generic projector.

**Performance pressure from many sources:** PASS conditionally — implementations may batch or owner-route evaluation, but optimisation MUST preserve ownership and MUST NOT create a generic semantic world-state/planner without separate evidence and authority.

---

## 21. Governance and Watch Consequence

The observed IMP-06/IMP-07 cycle satisfies `MS-WATCH-002` promotion Criterion A.

The exact design gate is:

```text
IMP-06-S3-DG-001
    Production capability-owned Public Interaction participation-source
    contract and generic zero-source S3 completion boundary
```

Upon acceptance of this amendment:

```text
IMP-06-S3-DG-001
    → RESOLVED by MS-PROT-049 v1.4
```

`MS-WATCH-002` SHALL remain active as a persistent programme/architecture watch.

Resolving the present deadlock does not prove that similar ownership/programme cycles cannot recur in later interaction families.

---

## 22. Explicit Non-Goals

This amendment does not define:

```text
exact Java class/interface names
database tables
HTTP routes
transport DTOs
opaque token encoding
generic subject repository
universal participation-role enum
availability model
price or commercial-term model
authorisation model
provider readiness
operation dispatch
form schemas
frontend component structure
universal interaction rules DSL
```

Those concerns remain governed by their existing authorities or later implementation choices.

---

## 23. Hard Invariants

1. Every positive subject-interaction participation fact has exactly one accepted semantic owner.
2. Generic Surface owns no participation truth.
3. Exposure does not establish participation.
4. Subject type does not establish participation.
5. Business category, route, labels, provider identity and AI do not establish participation.
6. Participation sources remain capability/authority owned.
7. Source registration is owner-qualified and deterministic.
8. Zero production sources is a valid fail-closed generic-infrastructure state.
9. Zero sources produce zero subject bindings.
10. Concrete production participation sources remain with their owning capability vertical slices.
11. Public binding requires independently legitimate participation and public serviceability/exposure.
12. Binding does not establish availability, price, authorisation or execution permission.
13. Semantic subject identity remains authoritatively resolvable.
14. Wrong Merchant Scope or incompatible release/context reuse fails closed.
15. Duplicate semantic ownership is rejected.
16. Many-to-many participation remains valid.
17. Runtime mutation revalidates current participation independently.
18. `BoundedProjectionReadBinding` does not become semantic participation identity.
19. IMP-06 does not acquire Publication, Enquiry, Booking, Appointment or Ordering business semantics merely to close S3.
20. The existing IMP-06 → IMP-07 HARD macro dependency remains unchanged.

---

## 24. Governing Principle

> **Main Street may complete the generic machinery required to project Public Interaction Bindings before any concrete capability supplies a positive production participation source, provided the empty-source state fails closed. Concrete participation remains owned and introduced by the capability that owns the relationship; programme sequencing never justifies transferring that truth into generic Surface or Exposure infrastructure.**
