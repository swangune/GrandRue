# IMP-08C-C3 Catalogue History Selection Checkpoint — 14 September 2026

**Node:** IMP-08C-C3 — Execution failures/outcome ledger
**State:** IN_PROGRESS
**Branch:** development
**Inspected implementation base:** a35ef963febb93d6f11f583811e031b676050891
**Closure claim:** NONE

## Authority and scope

This record is implementation evidence, not new design authority. The bounded change implements already accepted MS-PROT-056 v1.9, `designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md`, §6 (distinct plan identities), §§9–10 (historical intervals and explicit resolution failure), §11 (establishment-time Standing Free selection) and §13 (immutable historical interpretation). MS-PROT-056 v1.6 §5 continues to govern existing-baseline recovery. IMPLEMENTATION-RULES §§2–3, 30.8, 47–49 and 52.14 govern this implementation cycle.

The application uses a deterministic immutable read model in the Commercial owner. It introduces no framework, provider, new domain owner, new catalogue policy, runtime configuration or website dependency.

## Implemented and observed

- `src/main/java/mainstreet/commercial/PublishedStandardPlanCatalogueRevision.java` represents the read projection of one publication's immutable plan snapshots, publication instant and predecessor reference. Constructing it is not catalogue publication, manifest validation or approval.
- `src/main/java/mainstreet/commercial/StandardPlanCatalogueHistory.java` validates a complete immutable history through an explicitly supplied authoritative read instant, checks predecessor continuity and retained plan-identity consistency, resolves exact revision references and selects half-open temporal windows. It implements `FreePlanRevisionAuthority` for that bounded snapshot.
- `src/main/java/mainstreet/commercial/CatalogueResolutionException.java` distinguishes no established catalogue, uncovered historical time, future selection and history-integrity failure. Storage/IO failure remains the future adapter's responsibility; no unavailable storage is converted to an empty snapshot.
- `src/main/java/mainstreet/commercial/StandardPlanCatalogueRevision.java` rejects repeated plan revision identities across its three plan slots, preserving explicit grant-set monotonicity and immutable snapshots.
- `src/test/java/mainstreet/commercial/StandardPlanCatalogueHistoryTest.java` exercises interval boundaries at nanosecond precision, exact lookup, absent/old/future history, input copying, duplicate identities/instants, predecessor corruption, conflicting reused plan identities and snapshot read-time limits.
- `src/test/java/mainstreet/application/StandingFreeFromMerchantAccountEstablishedHandlerTest.java` composes the actual history selector with the existing handler. A delayed event selects its establishment-time FREE revision, and missing/uncovered history cannot create a baseline or invoke candidate identity generation.

## Test-first sequence and verification

The first focused run failed test compilation because the historical read model and typed resolution failures were absent. After adding those types, the history tests passed, but `StandardPlanCatalogueRevisionTest.catalogue_requires_three_distinct_plan_revision_identities` failed because the existing implementation accepted repeated identities. The cardinality correction was then implemented.

The focused command `mvn --batch-mode '-Dtest=StandardPlanCatalogueHistoryTest,StandardPlanCatalogueRevisionTest,StandingFreeFromMerchantAccountEstablishedHandlerTest' test` passed 25 tests, with zero failures/errors/skips.

Full gate: `mvn --batch-mode clean verify -Ppostgres-it` passed 1,266 unit/governance tests and 434 PostgreSQL integration tests, with zero failures/errors/skips. Maven reported BUILD SUCCESS in 4 minutes 37 seconds, finishing at 2026-09-14T22:50:36+01:00. The run used a disposable local PostgreSQL 18.6 container and all 65 existing migrations; this checkpoint adds no migration. It revalidates the existing worker/owner integration coverage but does not turn the new pure history tests into storage-publication or scheduler-activation proof.

## Falsification and limits

The read model cannot answer after its supplied read instant, so an old read cannot silently act as today's latest catalogue. The source must still establish that instant and complete history consistently with publication. These pure tests do not prove database publication/read serialization, real process restart or storage authenticity.

All catalogue identifiers and grants in the tests are fixtures. They do not constitute an approved production manifest. No production catalogue was published and no account or worker was activated.

The read projection intentionally does not represent complete binding definitions or approval evidence. It is not a substitute for the v1.9 §§4–8 publisher, durable manifest store, trusted publication-authorisation boundary or storage-backed history reader. Existing-baseline recovery remains independent of a fresh history read through the unchanged owner handler.

## Remaining C3 prerequisites

1. Implement durable manifest/binding publication and consistent storage-backed historical reads under MS-PROT-056 v1.9, with exact approval and publication-authorisation checks and real PostgreSQL concurrency/recovery proof.
2. Resolve `MS-PROT-056-V17-DQ-001`: present and obtain explicit approval of the complete concrete manifest, exact entitlement identities, owner-qualified bindings, required supporting classifications and all three explicit grant sets. Neither allocation policy nor test fixtures satisfy this manual gate.
3. Complete production worker composition/activation with the accepted historical resolver, initial-catalogue/account ordering and explicit pre-P0 failure. Do not fabricate history for uncovered accounts.
4. Complete C3's production-path verification and synchronized closure evidence before changing the node to CONFORMING_COMPLETE.

These are not a requirement to implement every website or paid feature first: MS-PROT-056 v1.9 §6 distinguishes a published catalogue from implementation/availability of every granted service. The broader catalogue-design queue remains separate from website implementation and does not authorise hidden exemptions or incomplete binding admission.
