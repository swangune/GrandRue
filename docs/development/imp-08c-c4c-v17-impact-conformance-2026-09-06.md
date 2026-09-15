# IMP-08C-C4C — IMPLEMENTATION-RULES v1.7 Impact and Conformance Review

**Date:** 6 September 2026
**Target:** IMP-08C-C4C — Reaction registration and identity
**Macro:** IMP-08C — Durable Execution Foundation
**Review decision:** CONFORMING_COMPLETE for C4C's original declaration/identity scope, effective on the synchronized closing gate. C4D is READY to implement; no C4D behavior is claimed complete.
**Evidence type:** Current implementation evidence; not semantic authority.

## Baseline, freshness and preservation

OBSERVED: clean `development` was fast-forwarded from `1c80157f13d78973f7017272b078bf69a9b482d7` to `1b3acaac3c933b0c0bc7cc3c97735b81b3b62780`. The intervening commits `2d161b37` and `1b3acaac` accepted/indexed IMPLEMENTATION-RULES v1.7. Current review proof head: `c357953c2217689be21dbe50b84bc03d3092b0f0`.

Scope was selected by the current user instruction and the live graph edge C4C → C4D. Reviewed: seven C4C production types, their test class, original C4C selection, and only the directly referenced source-contract/Standing Free owner boundary needed to interpret the declaration. Completed historical nodes were not re-audited.

Historical v1.6 evidence remains untouched: `docs/development/imp-08c-c4c-reaction-contract-conformance-2026-09-06.md`, Git blob `2de4ac61d9753c18e4079be0453417c214fa9ed6`. Its result is historical evidence, not the basis of the current v1.7 completion decision. Original scope is independently recorded in `docs/development/imp-08c-c4c-selection-2026-09-06.md`: static declarations, exact lookup, stable identity; execution/acknowledgement were explicitly subsequent work.

## Exact governing pointers

These labels identify terminal accepted constituents, not an Authority Index or remembered summary. Section number and heading are primary; source line numbers below are supplementary implementation navigation.

- **G — MS-IMPLEMENTATION-RULES-001 v1.7**, `designs/IMPLEMENTATION-RULES.md`: §30 — Comment, Documentation & Design-Traceability Rule; §30.1 — Exact Authority Pointer Contract; §30.2 — Trace-Anchor Placement Rule; §30.3 — Clean-Code-First Rule; §30.5 — Tests-to-Authority Traceability; §30.6 — Bidirectional Traceability; §30.8 — Authority-Change Impact Rule; §47 — Implementation Evidence; §52.14 — Mandatory implementation-status synchronisation; §§52.15–52.20 — Repository-Evidence and Freshness Rule, Observation, Deduction and Uncertainty Rule, No Inference Across Evidence Gaps, Counterevidence Before Completion, Evidence Locality Rule, AI Evidence-Hierarchy Rule.
- **E — MS-PROT-026 v1.1**, `designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md`: §13 — EventReactionContract; §14 — Registration Is Required; §15 — Reaction Owner; §16 — Event Reaction Identity; §17 — Per-Reaction Acknowledgement; §23 — Reaction Deduplication; §24 — Downstream Idempotency Remains Separate; §26 — Reaction Ordering Requirement; §29 — Reaction Supersession; §30 — Coalescing; §31 — Schema / Contract Evolution; §32 — Historical Affinity; §36 — Reaction Data Requirement; §47 — Event Receipt Does Not Carry Actor Authority; §48 — Merchant Scope.
- **C — MS-PROT-056 v1.6**, `designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md`: §1 — Governing decision; §2 — Semantic ownership; §3 — Standing Free baseline fact; §5 — Idempotency and duplicate delivery.
- **P — MS-PROT-063 v1.0**, `designs/MS-PROT-063 — Authentication, Session & Trusted Execution Principal Establishment Model.md`: §12 — System and scheduled principals. This governs the declared principal requirement, not proof of a C4D implementation.
- **M — MS-IMP-001 v1.0**, `designs/MS-IMP-001.md`: §16 — IMP-08C — Durable Execution Foundation. This supplies macro scope/prerequisites, not evidence that they are implemented.

## Bidirectional responsibility / code / test map

All test methods below are in `src/test/java/mainstreet/semantic/event/EventReactionContractTest.java`. The exact method names are stable test anchors.

| Responsibility and exact provision | Production location | Executable proof location |
| --- | --- | --- |
| Required owner reaction responsibilities, E §13 — EventReactionContract; E §15 — Reaction Owner; E §36 — Reaction Data Requirement | `src/main/java/mainstreet/semantic/event/EventReactionContractDefinition.java:11`, canonical constructor at :26; `src/main/java/mainstreet/semantic/event/EventReactionContractIdentity.java:4` and `src/main/java/mainstreet/semantic/event/EventReactionTargetReference.java:4` | :32 `mandatory_contract_semantics_cannot_be_missing_or_blank`; :61 `registry_rejects_duplicates_and_defensively_copies_definitions_and_required_data` |
| Stable logical identity, E §16 — Event Reaction Identity; E §23 — Reaction Deduplication; E §24 — Downstream Idempotency Remains Separate | `src/main/java/mainstreet/semantic/event/EventReactionIdentity.java:6`; `src/main/java/mainstreet/semantic/event/EventReactionContractAffinity.java:6` | :17 `duplicate_delivery_keeps_one_identity_while_other_reactions_and_events_are_independent`; :24 `registry_release_is_affinity_not_a_new_logical_reaction` |
| Exact release/source lookup, E §31 — Schema / Contract Evolution; E §32 — Historical Affinity | `src/main/java/mainstreet/semantic/event/EventReactionContractRegistrySnapshot.java:10` constructor and :27 `contract` | :49 `lookup_requires_exact_reaction_release_and_exact_source_contract_affinity`; :76 `empty_registry_and_null_inputs_do_not_enable_listeners` |
| Owner isolation / single identity definition, E §13 — EventReactionContract; E §15 — Reaction Owner | same registry constructor, owner-qualified key | :84 `same_named_reactions_of_different_owners_coexist_without_lookup_collision`; :92 `conflicting_definition_cannot_hide_behind_the_same_registered_identity` |
| Concrete Commercial declaration, C §1 — Governing decision; C §2 — Semantic ownership; C §3 — Standing Free baseline fact; C §5 — Idempotency and duplicate delivery | `src/main/java/mainstreet/application/StandingFreeEventReactionContract.java:14` IDENTITY, :16 AFFINITY, :18 DEFINITION, :35 registry | :104 `standing_free_registration_preserves_owner_source_temporal_and_downstream_semantics` |
| Declaration-only principal/data/order/retry references, E §§13, 24, 26, 29, 30, 36, 47, 48 (headings above); P §12 — System and scheduled principals | definition's named fields; concrete DEFINITION references in `StandingFreeEventReactionContract.java:18` | :32 required references; :104 concrete data/intent references. **These prove declaration, not policy evaluation or runtime enforcement.** |

The source affinity is the existing `MerchantAccountEstablishedEventContract.AFFINITY` at `src/main/java/mainstreet/merchantaccount/MerchantAccountEstablishedEventContract.java`. Inspection was limited to that dependency identity and its use; this review does not reopen C4B1.

The concrete declaration's short string values are stable semantic reference tokens, not terminal documentary pointers. This map resolves them to the exact accepted constituents. Their values were not renamed to improve documentation, which would change registered contract equality.

## v1.7 impact and clean-code review

OBSERVED: prior class comments used broad document references without exact constituent paths. The historical evidence omitted a bidirectional method-level map and explicit falsification results. These are **traceability/evidence gaps** under G §§30.1, 30.6, 47 and 52.18; they are not evidence of a changed business rule.

Corrected two ownership-boundary comments: the reaction definition and the concrete Standing Free declaration. They state the local WHY plus exact file/section headings. No per-field/per-branch citation saturation was added. Naming, immutable records and the separate registry/identity types already express ordinary WHAT/HOW; additional behavioral refactoring was not justified by this review.

Production executable behavior and semantic reference values are unchanged. Two adversarial tests strengthen proof of distinct-owner coexistence and rejection of a conflicting definition under an existing identity.

## Counterevidence and completion falsification

| Attempt to falsify completion | Current observation / result | Classification and effect |
| --- | --- | --- |
| Same textual reaction name belongs to two owners; lookup collides | Both definitions coexist and resolve independently in added test :84 | PASS for owner-qualified registration |
| Same identity registered twice with different semantics | Added test :92 rejects conflicting definitions; original :61 rejects identical duplicates | PASS; no last-write-wins redefinition |
| Wrong source owner/release or wrong reaction release silently uses current meaning | Test :49 returns empty; direct inspection of `contract` checks both affinities | PASS for static resolution |
| A release change manufactures a fresh logical reaction | Test :24 changes affinity while logical key stays equal | PASS; this is not a durable deduplication proof |
| Mutation through supplied collections or returned collections changes registration | Test :61 clears input sets/lists and attempts mutation of returned collections | PASS; snapshot remains immutable |
| Missing semantics create permissive defaults | Test :32 exercises nine blank reference positions, empty data and malformed identities; :76 exercises null/empty registry | PASS for declared validation scope |
| Typed IDs/registry construction prove trusted source or actor authority | Public constructors accept well-formed identifiers without proving source facts, principal authority or runtime applicability | **Counterevidence to any broader runtime completion claim.** Expected boundary of C4C; C4D must establish trust/current evaluation |
| A nonblank reference proves a resolvable owner operation/policy | Constructor validates form only; `target` and policy references are not executed | **Not proved and not claimed.** C4D implementation must bind accepted owner behavior explicitly |
| Existing handler or neighbouring PostgreSQL tests prove durable per-reaction acknowledgement | Current production-reference search finds declarations; C4C contains no receipt/acknowledgement store or execution path | **False broader inference rejected.** E §17 remains C4D work |
| Code contains an alternative execution/listener bypass inside C4C | Inspected all seven production types and current references using `rg -n 'EventReaction|StandingFreeEventReactionContract' src/main/java --glob '*.java'`; methods only construct/validate/look up immutable values | No executable bypass found within the reviewed component; no claim about all future callers |
| Governance version was merely advanced without impact review | v1.7 diff inspected; exact anchors repaired; fresh targeted tests and full CI recorded below | PASS after remediation; old v1.6 record retained |

DEDUCED: the original C4C completion claim survives under current v1.7 evidence. This follows from inspected code and adversarial verification, not from its historical COMPLETE label. No behavioral gap requiring a new semantic decision was discovered within C4C.

UNCERTAIN / outside evidence: durable reaction persistence, actual source/principal establishment, concurrent execution, transaction rollback around reactions, lost-acknowledgement recovery, retry disposition, deployment and all C4D callers. These are not promoted from static-component or regression evidence.

## Verification and evidence locality

- Targeted command: `mvn --batch-mode -Dmaven.repo.local=<existing-workspace-cache> -Dtest=EventReactionContractTest test`: **9 tests, zero failures/errors/skips**, Java 26 targeting Java 25, 6 September 2026.
- Full proof CI: run `34008837843`, head `c357953c2217689be21dbe50b84bc03d3092b0f0`: **SUCCESS — 1108 unit/governance + 380 PostgreSQL integration tests, zero failures/errors/skips**.
- Required CI command: `mvn --batch-mode clean verify -Ppostgres-it`, Java 25/PostgreSQL 18.
- Full-suite execution is regression verification; it is not a re-audit or new completion decision for every historical node.
- No schema, persistence or runtime behavior changed. The 380 PostgreSQL tests do not prove C4D reaction acknowledgement.
- Closing consistency gate must pass for this evidence, the new live graph and implementation-status.md. Subsequent material changes to these C4C responsibilities or governing clauses require a scoped freshness review under G §30.8 and §52.15.
- Scoped evidence-gate source: `src/test/java/mainstreet/governance/C4CFrontierEvidenceConformanceTest.java`, methods `frontier_points_to_current_scoped_evidence_while_historical_evidence_is_preserved` and `current_review_terminal_authority_pointers_resolve_to_exact_constituent_headings`. They check current navigation, exact constituent headings and the historical Git-blob digest; they do not re-audit historical implementations.
- Closing local command: `mvn --batch-mode -Dmaven.repo.local=<existing-workspace-cache> -Dtest=C4CFrontierEvidenceConformanceTest,ImplementationProgrammeGateConformanceTest test`: **3 tests, zero failures/errors/skips**. The synchronized commit must also pass the full required CI gate before C4D starts.

## Frontier decision

C4C: **CONFORMING_COMPLETE under current v1.7 evidence** once synchronized full CI passes.
C4D: **READY** to implement its accepted Standing Free reaction execution/acknowledgement target under v1.7; readiness is not proof of implementation and does not waive its own dependency/refinement checks.
No older completion claim has been re-issued. The current graph carries those nodes as inherited historical state and points here only for the live C4C → C4D edge.

## Local v1.9 governance integration — 14 September 2026

This supplement preserves the dated v1.7 review above; it does not reissue that
historical proof or change any implementation-node state.

OBSERVED: merge `6968a84d` combines remote `880aab5f` with local `f6aecd87`,
preserving the three local worker-recovery/manual-gate commits. Accepted
MS-IMPLEMENTATION-RULES-001 v1.9 changes its metadata and §51 — Relationship to
Design and Programme Governance to remove the non-operative MS-AVS-001 reference.
The exact heading checks exercised by this evidence gate, including §30.1 — Exact
Authority Pointer Contract, §30.8 — Authority-Change Impact Rule, §47 —
Implementation Evidence and §52.19 — Evidence Locality Rule, remain unchanged.
Current governance resolution is explicit in MS-DOCUMENT-GOVERNANCE-001 v2.3,
`designs/DOCUMENT-GOVERNANCE.md`, §19.2 — `MS-AVS-001` Legacy Resolution.

The first local `mvn --batch-mode clean verify -Ppostgres-it` run falsified a
clean integration claim: 1251 unit/governance tests ran with one failure, because
`C4CFrontierEvidenceConformanceTest.current_review_terminal_authority_pointers_resolve_to_exact_constituent_headings`
still asserted version 1.8. PostgreSQL integration tests were not reached.

The repair updates that exact version assertion and the test's authority comment
to v1.9. It retains every constituent-heading assertion, navigation assertion and
the historical Git-blob digest check. No production code, business rule, accepted
authority or historical proof content is changed. This is a scoped traceability
repair under MS-IMPLEMENTATION-RULES-001 v1.9,
`designs/IMPLEMENTATION-RULES.md`, §§30.8–30.9 — Authority-Change Impact Rule and
Stale-Reference Rule, not a new design decision.

Verification on the merged local tree with this repair:

- `mvn --batch-mode -Dtest=C4CFrontierEvidenceConformanceTest,AgentInstructionsConformanceTest,ImplementationProgrammeGateConformanceTest test`:
  7 tests, zero failures/errors/skips.
- `mvn --batch-mode clean verify -Ppostgres-it`: BUILD SUCCESS,
  1251 unit/governance + 434 PostgreSQL integration tests, zero
  failures/errors/skips; 4 minutes 37 seconds, completed 14 September 2026 at
  19:59:20 Europe/London. PostgreSQL 18.6 applied/validated all 65 migrations.

C3 remains IN_PROGRESS; its Commercial catalogue activation path remains
DESIGN_ESCALATION pending the complete chat-only design/manual-approval lifecycle.
No production-readiness, catalogue acceptance or remote-CI claim is made.
