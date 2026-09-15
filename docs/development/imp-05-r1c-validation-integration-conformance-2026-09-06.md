# IMP-05-R1C — Stored replacement validation integration

**Rules:** IMPLEMENTATION-RULES v1.7. **State:** CONFORMING_COMPLETE for real stored replacement validation/package-evidence integration. **Entry baseline:** `development@f1f461e16f2fbe7f823cac670c23a758146d33a1`, synchronized R1B closure. R1B implementation `bc19f8d9dd1f9bee4c4ccc40cdf0e7c90a124da3` passed Java 25/PostgreSQL 18 CI `34027581876`: 1130 unit/governance + 398 integration tests, zero failures/errors/skips.

## Authority and bounded responsibility

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, v1.0 §19 Candidate generation: a generated candidate enters validation; durable creation does not establish validity.
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`, §3 Configuration Revision identity and immutability, §4 Semantic-registry affinity, §5 Validation and Resolved Configuration Package production: validate the exact immutable revision and its pinned release.
- `designs/MS-PROT-040 v1.5 — Release-Purpose Admission & Ordinary Release Reference Amendment.md`, §9 Configuration-Revision Creation and Validation: retain revision release affinity.
- `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`, §§6–8: retained validation-evidence shape and production predicate; reuse does not extend its ordinary-first approval policy to later revisions.
- `designs/IMPLEMENTATION-RULES.md`, v1.7 §§46–48, §52 canonical loop, §§52.14–52.19 and §53.2.

The required change is the validation persistence consumer's source lookup. Configuration continues to own immutable source contents; the compiler/package resolver derives a package; the evidence writer verifies exact source affinity in its transaction. Initial Onboarding provenance is not a prerequisite of every Configuration Revision. No new semantic authority, dependency or library is needed.

## Tests and evidence scope

`src/test/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationValidationEvidenceAuthorityIT.java`: `validates_a_real_stored_replacement_without_initial_provenance`, `rejects_wrong_release_for_a_real_stored_replacement`, and `durable_replacement_creation_does_not_bypass_compiler_validation`. The helper `storedReplacement` uses the actual R1B PostgreSQL writer, then reads persisted contents through the common owner reader. Existing initial handoff, compiler/package resolver and validation persistence are real; source authorization is a supplied fixture decision, not production actor-policy proof.

The successful path must retain exact revision/release and historical evidence retry, leave the initial revision intact, and produce no approval. Wrong release must be classified as affinity mismatch, not missing revision. A durable but semantically invalid candidate must fail compiler validation without evidence.

Tests were added before production changes. Six graph tests and compilation passed locally. PostgreSQL RED at `0bb47ad839b307c5e067e7f90bc8eed1515940e8`, run `34028007194`, job `101472326378`: 1130 unit/governance passed; 401 PostgreSQL cases ran with exactly one failure and one error. The positive replacement path reported CONFIGURATION_REVISION_NOT_FOUND; the wrong-release test expected RESOLVED_PACKAGE_AFFINITY_MISMATCH but received CONFIGURATION_REVISION_NOT_FOUND. The compiler-rejection case passed. These are the expected initial-only-reader failures, not an environment failure.

`src/main/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationValidationEvidenceAuthority.java`, `recordInsideTransaction` :130, common lookup :146: reads `MerchantConfiguration` through `ConfigurationRevisionAuthority.configuration` and retains `requireSourceConfiguration` before evidence insertion. No initial-provenance decoding, weakened affinity predicate or caller Boolean is introduced. Exact integration test locations are :252 (successful path), :274 (wrong release), :293 (compiler rejection), :300 (real stored replacement fixture).

Full local `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 test` passed 1130 tests, zero failures/errors/skips, BUILD SUCCESS. `git diff --check` passed. Final implementation baseline `f2c2b7d4522272314f0994d2a21c4ba28cf181be`, Java 25/PostgreSQL 18 CI `34028256995`, job `101473005592`: SUCCESS, 1130 unit/governance + 401 PostgreSQL tests, zero failures/errors/skips. All three new cases passed. This closes R1C and the R1 contract/persistence/validation parent, with R4 READY after synchronized closure is committed. It does not close IMP-05.

## Falsification and limits

Closure verification exposed a test-fixture defect in `ImplementationGraphIntegrityTest.declared_child_completion_requires_complete_dependencies_and_child_graph_is_acyclic`: its injected completed parent assumed the live R1C prerequisite was still unfinished. Once R1C completed, the injection was valid and the expected rejection disappeared. The negative fixture now explicitly contains a completed parent and an incomplete child, independent of live progress. The real graph is still validated before this negative fixture. This preserves the completion standard rather than retaining an obsolete state assumption.

Seek failure where the initial-only reader hides a stored replacement, where a compiled package uses another release, or where persistence alone is treated as validation. Passing common-reader tests alone cannot close this node: actual PostgreSQL writer/compiler/evidence integration is required. Fixture authorization does not establish concrete origin/actor admission. No impact production, approval, activation, reinstatement, provider readiness or deployment is certified here. R4, R2 and R3 remain scheduled, and IMP-05 remains PARTIALLY_CONFORMING until all required work is complete.
