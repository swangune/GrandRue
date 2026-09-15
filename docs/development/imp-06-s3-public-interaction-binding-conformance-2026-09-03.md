# IMP-06 S3 Public Interaction Binding — Conformance Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro-node:** IMP-06 — Read, Exposure & Transport Spine  
**Fine-grained node:** S3 — Production Public Interaction Binding projection  
**Date:** 3 September 2026  
**Status:** **CONFORMING_COMPLETE**

---

## 1. Governing authority

S3 is governed by:

- `designs/MS-PROT-049 v1.4 — Capability-Owned Public Interaction Participation Source & Generic Binding Projection Amendment.md`;
- surviving MS-PROT-049 v1.0-v1.3 Surface/Public Interaction authority;
- composite MS-PROT-027 Projection/Exposure/Audience Observation authority;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

This record is implementation evidence only. It creates no semantic or architectural authority.

The accepted S3 rule is an intersection, not an inference:

```text
applicable PUBLIC_INTERACTION contribution semantics
        +
positive owner-established subject ↔ interaction participation
        +
already P2/E4-approved and S2-selected public Projection material
        ↓
generic Public Interaction Binding projection
```

Generic Surface code owns none of the three upstream truths.

---

## 2. Design-gate formalisation

The hidden programme cycle was resolved by accepted MS-PROT-049 v1.4 and formalised on `development` as:

```text
44b395d4a8cfbfe79ec74c58923fbc00b8c16b44
docs(imp-06): accept S3 participation source boundary
```

Verification:

```text
GitHub Actions run: 33812854704
job:                100838437668
command:            mvn --batch-mode clean verify -Ppostgres-it
result:             SUCCESS
```

That authority permits a valid zero-source production registry which fails closed to zero positive bindings. It does not permit Surface, Exposure, transport, merchant classification, routes, labels, providers or AI to infer participation.

---

## 3. RED evidence

Tests-only RED commit:

```text
ef9fa54a58b8f686fabb4b3acde6717e7967094c
test(imp-06): add S3 public interaction binding RED
```

RED workflow:

```text
run: 33813750624
job: 100841186424
```

Production compilation succeeded. Test compilation then failed on the intentionally absent S3 production types. The failure was therefore caused by the missing accepted S3 implementation, not by an unrelated repository, environment or PostgreSQL failure.

The initial RED required:

1. zero registered sources → empty bindings without inference;
2. one explicit test-only source → one exact positive binding; and
3. participation for a subject present only in raw bounded-read material but absent from `PublicCustomerProjectionAssembly.selectedFragments()` → no binding.

---

## 4. Minimum production GREEN

Production GREEN commit:

```text
3404ce25dbe8c8e452e079a9f37b20afee9d7488
feat(imp-06): add S3 participation binding projection
```

Production additions:

```text
PublicInteractionParticipationSourceIdentity
PublicInteractionParticipationRequest
PublicInteractionParticipationSource
PublicInteractionParticipationSourceRegistration
PublicInteractionParticipationSourceRegistrySnapshot
PublicInteractionParticipationFact
PublicInteractionBinding
PublicInteractionBindingProjector
PublicInteractionBindingProjectionStructuralException
```

The implementation establishes:

- owner-qualified participation-source identity and explicit registration;
- immutable exact-release source-registry snapshots;
- a valid empty production registry;
- exact Merchant Scope / semantic-release / resolved-model request affinity;
- positive owner-established participation facts carrying contribution, registered operation, subject, source provenance and optional semantic role;
- an internal immutable Public Interaction Binding projection;
- exact same-bounded-read assembly affinity;
- eligibility only from `PublicCustomerProjectionAssembly.selectedFragments()`;
- `PUBLIC_INTERACTION` contribution filtering without business-type inference;
- exact contribution and supported-operation validation;
- source provenance validation;
- deterministic immutable output; and
- structural fail-closed handling for cross-context contradictions and duplicate semantic ownership.

Baseline GREEN verification:

```text
GitHub Actions run: 33814202112
job:                100842578286
command:            mvn --batch-mode clean verify -Ppostgres-it
result:             SUCCESS
```

---

## 5. Adversarial hardening

Tests-only hardening commit:

```text
97a5fef2a9834d5d9dfe620867b46a4f255a2def
test(imp-06): harden S3 participation binding invariants
```

Final code-bearing verification:

```text
GitHub Actions run: 33814624759
job:                100843888917
head:               97a5fef2a9834d5d9dfe620867b46a4f255a2def
command:            mvn --batch-mode clean verify -Ppostgres-it
result:             SUCCESS
```

The adversarial suite proves:

- an assembly from another bounded read is rejected even when subject identity happens to match;
- cross-Merchant Scope model reuse fails closed;
- cross-semantic-release model or source-registry reuse fails closed;
- wrong resolved-model identity/version fails closed;
- contribution laundering fails closed;
- an operation absent from the registered contribution fails closed;
- source-provenance laundering fails closed;
- two distinct sources claiming the same exact semantic participation ownership fail closed;
- non-`PUBLIC_INTERACTION` contributions do not invoke participation sources;
- one interaction may bind multiple selected subjects;
- one selected subject may bind multiple interactions;
- registry and result collections are immutable snapshots;
- `PublicInteractionParticipationFact` and `PublicInteractionBinding` do not carry `BoundedProjectionReadBinding`; and
- the S3 fact/binding/projector boundary contains no Repository, Provider, Availability, Authorization, Execution, HTTP, JsonNode or post-binding `PublicInteractionBindingExposureAuthority` dependency.

The projector is stateless and has no declared dependency fields.

---

## 6. Boundary decisions preserved

S3 closes with these distinctions intact:

```text
participation source
    ≠ Surface authority
    ≠ Exposure authority

Exposure positive membership
    ≠ participation truth

zero registered sources
    ≠ positive participation
    → zero bindings

Public Interaction Binding
    ≠ current availability
    ≠ provider readiness
    ≠ price authority
    ≠ Actor Authorisation
    ≠ execution permission
    ≠ transport DTO

BoundedProjectionReadBinding
    = opaque observation affinity only
    ≠ subject identity
    ≠ participation identity
    ≠ continuation identity
    ≠ execution authority
```

The historical prototype `PublicInteractionBindingExposureAuthority` is intentionally not invoked by production S3. Public material entering S3 has already passed the governed P2/E4/S2 path; adding a second post-binding Exposure decision would create a competing Exposure authority.

---

## 7. Explicit nonclaims

S3 completion does **not** claim:

- a concrete Publication → Enquiry participation source;
- a production `Opportunity → enquiry / send-enquiry` source;
- Booking, Ordering, Appointment or other capability-specific participation semantics;
- current availability, provider readiness, actor authorisation or execution permission;
- an HTTP/controller/transport representation;
- a concrete T2d adapter;
- a concrete T3c mapping;
- T4c opaque continuation encoding;
- IMP-07 implementation; or
- retirement of `MS-WATCH-002`.

The first real backend participation source remains downstream with the owning vertical slice and must receive its own RED/GREEN/adversarial/integration proof.

---

## 8. S3 decision

S3 completion criteria are satisfied:

```text
accepted owner-routed source boundary                 PASS
valid fail-closed zero-source production state        PASS
positive test-source mechanics                        PASS
exact S2-selected subject intersection                PASS
merchant/release/model affinity                       PASS
contribution/operation affinity                       PASS
source provenance                                     PASS
duplicate semantic ownership rejection                PASS
many-to-many representability                         PASS
no participation inference                            PASS
no owner-private business semantics in generic S3     PASS
no downstream availability/execution authority        PASS
full Maven/PostgreSQL verification                    PASS
```

> **S3 — Production Public Interaction Binding projection: CONFORMING_COMPLETE.**

A separate IMP-06 macro completion review is still required before the IMP-06 → IMP-07 programme gate may open.
