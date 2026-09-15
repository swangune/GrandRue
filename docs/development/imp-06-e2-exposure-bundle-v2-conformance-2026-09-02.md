# IMP-06 E2 Exposure Definition Bundle v2 Conformance Evidence

**Date:** 2026-09-02  
**Status:** CONFORMING_COMPLETE — integrated on `development`  
**Edge:** IMP-06 E2 — Semantic Definition Bundle v2 / Exposure packaging and semantic-release materialisation  
**Design authority:** ADR-016 — Exposure Definition Packaging & Semantic-Release Materialisation  
**Pull request:** #43 — `IMP-06 E2 Exposure bundle v2 materialisation`  
**Verified candidate implementation SHA:** `fadcd97186227882dc0b0dfd65679ee28f12145d`  
**Verified candidate CI:** Maven Tests #1385, workflow run `33594731033` — PASS  
**Integrated development SHA:** `5956a34b69c0de90e0129a700934bc9c1dff5808`  
**Integrated development CI:** Maven Tests #1387, workflow run `33595772698` — PASS  
**Full gate:** `mvn --batch-mode clean verify -Ppostgres-it`

## 1. Scope

This record captures executable conformance evidence for IMP-06 E2 only. It does not mark IMP-06 as a whole complete.

E2 implements the accepted four-section Semantic Definition Bundle v2 boundary required by ADR-016:

1. semantic registry definitions;
2. projection/surface definitions;
3. fulfilment/execution definitions; and
4. Exposure definitions.

The semantic-release layer remains responsible only for package integrity, provenance, typed decoding, exact-release coherence and typed assembly. Exposure decision semantics remain owned outside semantic-release materialisation.

## 2. Tests-first evidence

The implementation followed RED → GREEN progression.

- The initial conformance tests required v2/Exposure APIs that did not yet exist and failed before production implementation.
- After the v2 production path was introduced, Maven Tests #1382 (workflow run `33593088281`) compiled and passed the unit suite but exposed nine PostgreSQL integration-test fixture errors. The failures were the intended fail-closed v2 boundary: three legacy fixture helpers declared `CURRENT_FORMAT` while omitting explicit Exposure evidence.
- The repair changed only those fixture helpers. Each now supplies an explicit empty Exposure section, includes it in the v2 digest and passes it to the v2 bundle constructor. No production fallback was introduced.
- The fixture-only repair advanced the candidate from `39cafb7f84110000d406e85eadee36ad22e6faa1` to `fadcd97186227882dc0b0dfd65679ee28f12145d`.
- Maven Tests #1385 on exact head `fadcd97186227882dc0b0dfd65679ee28f12145d` passed the complete PostgreSQL verification profile.
- The documentation-inclusive PR head was then verified GREEN before integration.
- PR #43 was integrated into `development` without semantic alteration, preserving the verified history.
- Maven Tests #1387 / workflow run `33595772698` passed the full PostgreSQL profile on exact integrated head `5956a34b69c0de90e0129a700934bc9c1dff5808`.

The integration-test repair therefore represents “registry present with zero Exposure definitions”, not “Exposure evidence absent”. The distinction required by ADR-016 remains structural and fail-closed.

## 3. ADR-016 conformance

The integrated implementation proves the following accepted invariants:

- Semantic Definition Bundle v1 remains the historical three-section representation.
- Semantic Definition Bundle v2 has an explicit fourth Exposure-definition section.
- Current semantic-release materialisation requires v2 rather than reinterpreting v1.
- Missing Exposure evidence is rejected.
- Explicit empty Exposure evidence is valid and remains distinguishable from missing evidence.
- The v2 content digest binds the Exposure section together with the other bundle sections and provenance.
- The text codec preserves explicit v1/v2 envelope shapes rather than manufacturing compatibility state.
- Semantic release assembly includes a typed Exposure registry constituent and enforces exact-release affinity across all constituents.
- No missing-to-empty compatibility overload or fallback is present.

## 4. Semantic-gravity and ownership falsification

Static review and executable governance tests found no transfer of Exposure policy ownership into semantic-release infrastructure.

`SemanticReleaseMaterialiser` retains four typed decoder dependencies and performs only:

- supported-envelope validation;
- typed section decoding;
- exact-release checks; and
- construction of `SemanticReleaseAssembly`.

`SemanticReleaseAssembly` is a typed four-constituent release-coherence structure. It does not interpret Exposure policy.

The E2 ownership conformance test forbids the materialisation layer from acquiring dependencies on Exposure decision semantics, merchant-choice semantics, Exposure requirement evaluation or initial Exposure policy portfolios.

The E2 semantic-gravity result is therefore **PASS**:

> Generic packaging, transport, decoding and release-coherence are allowed. Generic interpretation or ownership of Exposure semantics is not.

## 5. Integration closure

All E2 closure gates are satisfied:

1. the documentation-inclusive PR head passed the full `mvn --batch-mode clean verify -Ppostgres-it` gate;
2. PR #43 was integrated into `development` without divergence or semantic alteration;
3. exact integrated head `5956a34b69c0de90e0129a700934bc9c1dff5808` passed Maven Tests #1387 / run `33595772698`;
4. this focused evidence is updated to the integrated proof; and
5. the active implementation-status and IMP-06 graph are refreshed as part of the same governance closure sequence before downstream work begins.

The authoritative E2 state is therefore:

**IMP-06 E2: CONFORMING_COMPLETE.**

E2 no longer blocks E4. Any E4 work must still independently satisfy its accepted substantive authority and the `IMPLEMENTATION-RULES.md` RED → GREEN lifecycle.