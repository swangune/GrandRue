# IMP-05-R4B Fulfilment Impact Assessment Progress — 7 September 2026

**Implementation node:** `IMP-05-R4B — Concrete business-effect and commitment assessments`  
**Status:** `IN_PROGRESS`  
**Implementation commit:** `9d66c6aff9d5bb2e9e240a7029c5cee8ef250c25`

## Accepted authority

This increment implements the bounded Fulfilment-owned configuration-impact meaning already accepted by:

- composite `MS-PROT-048` through v1.5 — Capability Fulfilment Provider Contract and accepted amendments;
- `MS-PROT-048 v1.2 — Merchant Fulfilment Binding Set Revision & Activation Affinity Amendment`, which requires exact immutable binding-set affinity and historical reconstruction from that exact revision;
- `MS-PROT-048 v1.3 — Fulfilment Requirement Applicability & Obligation Subset Amendment`, which makes applicable Fulfilment obligations configuration-specific;
- composite `MS-PROT-040`, for Merchant Configuration revision/activation affinity and impact-review context.

`designs/AUTHORITY-INDEX.md` remains authoritative for current accepted-authority navigation. This evidence document does not create or amend semantic authority.

## Implemented scope

The implementation adds and verifies:

- `src/main/java/mainstreet/fulfilment/FulfilmentConfigurationImpactAssessment.java`;
- exact historical Fulfilment binding-set lookup through `SemanticReleaseAssemblyRepository`;
- `src/test/java/mainstreet/fulfilment/FulfilmentConfigurationImpactAssessmentTest.java`.

For a populated base configuration, the assessment reconstructs the base Fulfilment Plan from the base configuration's exact semantic release and exact pinned Fulfilment binding-set revision. It then compares that resolved historical plan with the candidate plan rather than comparing mutable current state or revision identifiers alone.

## Semantic ownership implemented

Within the same semantic release, the assessment treats the following resolved Fulfilment-owned changes as consequential configuration effects:

- role/context binding addition or removal;
- required-obligation changes;
- fulfiller-kind changes;
- fulfiller-identity changes;
- provider-connection changes.

For first configuration, an established binding records the corresponding Fulfilment business effect and identifies the required supporting service as either the selected provider connection or fulfiller.

These are Fulfilment routing/integration facts. The assessment does not reinterpret capability semantics, policy meaning, provider readiness or runtime availability.

## Negative boundaries

The implementation deliberately does **not** infer any of the following:

1. **Binding-revision provenance is not business meaning.** A new immutable binding-set revision with the same resolved Fulfilment business facts does not by itself create an impact.
2. **Cross-release identifier equality is not semantic equivalence.** Apparently identical role/binding identifiers across different semantic releases are not treated as proof that the plans are impact-comparable. Without an explicit owner-provided equivalence rule, comparison fails closed.
3. **Missing historical authority is not reconstructed from current state.** If the exact historical semantic release or binding-set revision cannot be resolved, the populated-base comparison fails closed.
4. **Provider connection identity is not provider readiness.** This assessment compares selected routing, not mutable connection health/readiness.
5. **Fulfilment impact is not production policy interpretation.** The slice does not supply concrete policy-owner callbacks or policy business meaning.
6. **Fulfilment impact is not generic applicable-commitment semantics.** It contributes owner-backed business-effect evidence but does not prove all commitment-conflict categories required by `IMP-05-R4B`.

## Falsification and counterevidence

The focused test suite includes counterexamples that would fail an ID-based or provenance-based implementation:

- a changed fulfiller produces an existing-commitment conflict;
- changed required obligations produce an existing-commitment conflict;
- a provenance-only binding revision with unchanged resolved routing produces no impact;
- identical-looking identifiers across different semantic releases do not establish equivalence and fail closed;
- an unavailable historical binding revision fails closed;
- an unavailable historical semantic release fails closed;
- first configuration with a provider-backed binding establishes the expected supporting-service evidence.

These counterexamples specifically reject mutable-current reconstruction, revision-string heuristics and cross-release semantic guessing.

## Verification

Implementation verification passed the configured full gate:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

GitHub Actions `Maven Tests #1706` also completed successfully for implementation commit `9d66c6aff9d5bb2e9e240a7029c5cee8ef250c25`.

Focused Fulfilment coverage consists of seven tests in `FulfilmentConfigurationImpactAssessmentTest` covering routing, obligation, provenance-only, cross-release and missing-history boundaries plus first-configuration supporting-service evidence.

## Remaining `IMP-05-R4B` gaps

This increment closes the static Fulfilment binding/routing comparison slice only. `IMP-05-R4B` remains `IN_PROGRESS` because accepted owner-backed impact coverage is still incomplete, including at least:

- concrete production policy interpretations where accepted policy owners provide materiality semantics;
- applicable-commitment conflict semantics not already established by bounded owner assessments;
- resource-owned effects and any other accepted business-semantic owner not yet represented in the production assessment registry.

No downstream node is unlocked by this checkpoint alone.

## Conclusion

The Fulfilment slice now provides deterministic, historical-authority-affined impact evidence without treating routing provenance, current mutable state or identifier equality as semantic authority. The evidence is intentionally bounded and does not claim completion of `IMP-05-R4B`.
