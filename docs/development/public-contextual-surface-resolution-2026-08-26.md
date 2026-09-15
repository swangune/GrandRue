# Public Contextual Surface Resolution — Implementation Evidence

**Date:** 26 August 2026  
**Status:** VERIFIED implementation evidence — non-authoritative  
**Branch:** `development`  
**Verified executable code head:** `e2afd2e2a3ce49180ed7720f83a23c953b7a66a7`  
**Maven/PostgreSQL verification:** GitHub Actions run `32971970163` — SUCCESS

This document records implementation evidence only. Semantic authority remains in the accepted design corpus, principally MS-PROT-027 v1.2/v1.3 and MS-PROT-049 v1.0-v1.2.

## 1. Scope closed by this slice

The previous implementation-status/conformance records pre-date the executable storefront and still describe PUBLIC Exposure/projection filtering as absent. For this bounded scope, this evidence supersedes those historical statements.

The implemented PUBLIC read path is now:

```text
Active Configuration Release
        ↓
SurfaceContributionRegistrySnapshot
        ↓
StaticSurfaceContributionCatalogue
        ↓ candidate membership only
PublicContextualSurfaceResolver
        ├── ProjectionServiceabilityAuthority
        └── PublicSurfaceExposureAuthority
        ↓ restricted PUBLIC candidates
Public Interaction participation projector
        ↓
PublicInteractionBindingExposureAuthority
        ↓ audience-safe subject bindings
PrototypeStorefrontSurfaceProjection
        ↓
Next.js storefront presentation
```

Neither Projection Serviceability nor Exposure can manufacture Surface membership or grant execution authority.

## 2. Production contracts added

```text
ExposureDecision
    EXPOSE
    WITHHOLD

ProjectionServiceabilityAuthority
PublicSurfaceExposureAuthority
PublicInteractionBindingExposureAuthority
PublicSurfaceResolutionContext
PublicContextualSurfaceResolver
```

The implementation deliberately does **not** define:

- one global projection TTL;
- one universal projection lifecycle/status enum;
- a generic cache framework;
- a generic public execution endpoint;
- a second subject-participation truth store; or
- merchant-category-driven Surface logic.

## 3. Projection Serviceability behavior

For every declared `projectionRequirement` on a candidate PUBLIC contribution:

```text
serviceability = established true
    → continue contextual resolution

serviceability = false
or no safely established decision
    → remove only the affected contribution
```

A contribution with **no declared projection requirement** is not forced into a synthetic Projection Contract lifecycle.

The standard prototype currently declares no Projection Contract dependency on its storefront contributions. Its default Projection Serviceability adapter therefore grants nothing: any future projection requirement will fail closed until a real owner/contract adapter is introduced.

This preserves the MS-PROT-027 v1.3 synchronous request-scoped exception while preventing future registered dependencies from being treated as implicitly current/serviceable.

## 4. Contribution-level Exposure behavior

Exposure is evaluated only after static candidate membership and only for PUBLIC candidates.

```text
EXPOSE
    → candidate may remain

WITHHOLD
or no safely established decision
    → candidate removed
```

The Exposure authority is never asked to create a contribution that is absent from the active static catalogue.

Non-PUBLIC contributions are not submitted to the PUBLIC Exposure authority.

## 5. Subject-level Exposure behavior

MS-PROT-049 v1.2 permits one PUBLIC interaction contribution to have zero, one or many subject bindings. Therefore contribution-level Exposure is not treated as proof that every bound subject may be observed publicly.

The prototype now applies a second, subject-level filter after authoritative participation has produced binding candidates:

```text
PUBLIC_INTERACTION contribution survives
        ↓
authoritative subject-interaction participation
        ↓ binding candidates
PublicInteractionBindingExposureAuthority
        ↓
audience-safe bindings
```

A hidden subject can therefore produce:

```text
Appointment contribution remains applicable
+
Garden Maintenance subject Exposure = WITHHOLD
        ↓
Appointment contribution remains
binding list = empty
```

This preserves the accepted rule that an internally valid Offering may remain usable through merchant-assisted/telephone channels while being hidden from the public storefront.

The Surface/binding layer owns no subject-interaction participation truth.

## 6. Fail-closed and isolation evidence

`PublicContextualSurfaceResolverTest` proves:

- serviceable + exposed PUBLIC candidates survive;
- `false` Projection Serviceability removes the affected candidate;
- missing/indeterminate Projection Serviceability also removes the affected candidate;
- failure of one projection-dependent contribution does not erase an unrelated PUBLIC contribution;
- `WITHHOLD` removes an existing PUBLIC candidate;
- missing/indeterminate Exposure fails closed;
- Exposure sees only PUBLIC candidates already present in the static catalogue;
- non-PUBLIC contributions are not submitted to PUBLIC Exposure; and
- cross-merchant and semantic-release mismatches fail deterministically.

`PrototypeStorefrontSurfaceProjectionTest` additionally proves:

- same-trade merchants still receive different PUBLIC surfaces from active semantics;
- contribution-level Exposure can remove Appointment from a bookable gardener storefront without category branching;
- subject-level Exposure can remove `garden-maintenance` while retaining the Appointment contribution with zero bindings;
- Booking bindings still expose `standard-room`, never internal Allocation capacity identity; and
- Ordering bindings still expose `milk-2l`, never internal `sku-1`.

## 7. Test-first evidence

Integration RED commit:

```text
2928584ea8be4b28582c4abac5e1ffcfe1c2e2ff
```

GitHub Actions run:

```text
32971657952 — FAILURE
```

The failure occurred at test compilation because the required contextual storefront factory did not yet exist:

```text
cannot find symbol:
PrototypeStorefrontSurfaceProjection.withContextAuthorities(...)
```

The production path was then implemented and extended with subject-level Exposure.

Final executable verification:

```text
head: e2afd2e2a3ce49180ed7720f83a23c953b7a66a7
run:  32971970163
command: mvn --batch-mode clean verify -Ppostgres-it
result: SUCCESS
```

## 8. Explicit remaining boundaries

This slice does not claim complete MS-PROT-027 implementation.

Still evidence-driven/downstream:

- concrete persisted/cached/asynchronous Projection Contracts when an A-G applicability trigger actually exists;
- source-specific freshness/checkpoint/provenance adapters;
- rebuild/catch-up machinery for projections that claim rebuildability;
- multi-source projection currentness where divergent evidence matters;
- provider-backed projection serviceability where a concrete provider projection is introduced;
- CUSTOMER-context Surface resolution; and
- production merchant-authored Exposure policy/configuration sources beyond explicit prototype fixtures.

Those gaps must not be filled with an implicit TTL, implicit `current=true`, business-category inference, or frontend-owned policy.

## 9. Conformance result

```text
STATIC PUBLIC MEMBERSHIP              IMPLEMENTED
PUBLIC PROJECTION SERVICEABILITY SEAM IMPLEMENTED
FAIL-CLOSED MISSING SERVICEABILITY    IMPLEMENTED
PUBLIC CONTRIBUTION EXPOSURE FILTER   IMPLEMENTED
PUBLIC SUBJECT/BINDING EXPOSURE       IMPLEMENTED
SAME-TRADE CONFIG DIFFERENCE          PRESERVED
EXECUTION AUTHORITY SEPARATION        PRESERVED
BUSINESS-CATEGORY BRANCHING           ABSENT
FULL MAVEN/POSTGRESQL VERIFICATION    GREEN
```

The next implementation work should be chosen from remaining accepted conformance gaps, not by adding speculative Projection Contracts or generalized cache infrastructure without an actual MS-PROT-027 v1.3 applicability trigger.
