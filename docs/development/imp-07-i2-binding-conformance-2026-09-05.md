# IMP-07 I2 Conformance — Concrete Opportunity Public Interaction Binding

**Date:** 5 September 2026

**Node:** IMP-07-I2 — Concrete Public Interaction Binding proof

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

This is implementation evidence only. It creates no semantic authority.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern selection, verification and closure. MS-PROT-049 v1.4 §§7–10, 15 governs the intersection of applicable interaction, owner-established participation and serviceable exposed material. MS-PROT-046 v1.2 §§16–20 preserves explicit Opportunity participation, visibility and execution revalidation. Composite MS-PROT-027, including v1.13 §20, forbids Exposure rescuing a non-serviceable read.

I1 closure `db733f35950d2a22faef6d5dc163358b751bbb18`, run `33981821452`, passed 988 unit/governance and 329 PostgreSQL integration tests and satisfied I2's prerequisite. The current governing files were unchanged from that verified head.

## Proof and implementation result

`OpportunityPublicInteractionBindingI2Test` composes the real ConfigurationCompiler, P5 authoritative representation adapter, Publication material-affinity contribution, ExposureResolver, API Exposure binder, S2 assembly service, I1 production participation source and S3 binding projector.

Eight tests prove:

1. A compiled merchant context yields the exact owner-qualified Opportunity → enquiry/send-enquiry binding. The selected fragment is the original published-revision fragment despite an unpublished edit. S3 reads subject identity through I1 but does not reacquire representation values.
2. Exposed material without a registered source or applicable interaction yields no binding.
3. Publication window lower/upper boundaries withhold binding independently of participation.
4. Draft, withdrawn and missing Opportunities supply no public fragment; a non-serviceable path is rejected before binding.
5. Republish or withdrawal after acquisition and before Exposure withholds the stale binding.
6. A non-serviceable P2 result is structurally rejected by S2, including when contradictory positive Exposure is supplied; participation is not consulted.
7. Participation for another real Opportunity cannot attach to the selected subject.
8. Incompatible source-registry release, merchant/model/release and a different bounded read are rejected.

Existing production components satisfied these contracts. No production changes, new coordinator, persistence, transport, dependency or design amendment was required. MS-IMP-001's existing-code satisfaction rule applies: correct production implementation was not duplicated to manufacture a RED/GREEN cycle.

## Verification trace and limits

- First run: 8 tests, 2 fixture errors because negative serviceability outcomes lacked required reasons.
- Second run: 8 tests, 2 expectation errors because S2 rejects NOT_SERVICEABLE rather than returning empty assembly. Expectations were corrected to the accepted contract and strengthened to require the specific structural exception. Production behavior was unchanged.
- Local full suite: **996 tests, zero failures/errors/skips**; Java 26 compiling to the repository's Java 25 target.
- Proof commit: `c499439951d594f310992807805ba97d2d294605`.
- Full Java 25/PostgreSQL CI run `33982320477`: SUCCESS — 996 unit/governance + 329 PostgreSQL integration tests, zero failures/errors/skips.
- Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

The Publication authority uses deterministic test data; P2 outcomes and the static contribution catalogue are established seam fixtures, as in P5. This is a concrete internal composition proof, not a new serviceability policy portfolio, production configuration deployment, PostgreSQL-specific I2 test, HTTP end-to-end path or Enquiry mutation proof. The full PostgreSQL suite remains a regression gate.

## Closure and next work

Capability ownership, same-read material selection, scope/release/model affinity and the separation of participation from visibility/actionability/execution remain intact. No accepted invariant was weakened. This evidence, the I2 graph, implementation status and executable programme gate close together only on successful synchronization CI.

E1 — durable Enquiry submission/provenance foundation — becomes READY. E2 depends on E1; E3 depends on I2 + E1. IMP-07 remains IN_PROGRESS and later macro gates are unchanged.
