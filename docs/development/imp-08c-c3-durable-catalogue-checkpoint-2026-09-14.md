# IMP-08C-C3 Durable Catalogue Checkpoint — 14 September 2026

**Node:** IMP-08C-C3 — Execution failures/outcome ledger
**State:** IN_PROGRESS
**Branch:** development
**Inspected implementation base:** da955a99dfd5c66cf54dd96c75cf0e3f64d6efae
**Classification:** C — Commercial persistence, platform-context admission boundary and historical-read coordination
**Closure claim:** Bounded storage implementation only; no C3 closure or production activation

## Governing authority

This is implementation evidence, not semantic authority or a concrete production manifest. AUTHORITY-INDEX v4.15 resolves the governing composition. The implemented rules are supplied by:

- MS-PROT-056 v1.9, `designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md`: §7 — Publication authority; §8 — Publication operation; §9 — Initial effective start and succession; §10 — Historical resolution; §11 — Standing Free and pre-existing accounts; §12 — Idempotency, concurrency and failure; §13 — Retention, recovery and runtime boundaries. §§4–6 continue to govern immutable manifest structure and retained binding identity. §17 retains the actual production-manifest manual gate.
- MS-PROT-063 v1.0, `designs/MS-PROT-063 — Authentication, Session & Trusted Execution Principal Establishment Model.md`: §3.4 — Trusted Execution Context; §4 — Authentication is not authorisation; §19 — Platform scope; §20 — Context propagation boundary. The new context represents already-established attribution; it does not authenticate a caller or grant permission.
- MS-PROT-062 v1.0, `designs/MS-PROT-062 — Runtime Access, Eligibility & Execution Decision Composition Model.md`, §2 — Governing principle, preserves predicate ownership and authoritative-boundary revalidation. No universal policy engine or substitute merchant entitlement is introduced.
- MS-PROT-072 v1.0, `designs/MS-PROT-072 — Cross-Capability Application Orchestration & Consistency Model.md`: §5 — Transaction boundary rule; §7 — Logical application-request identity; §8 — Layered idempotency. No foreign owner mutation joins catalogue publication.
- MS-PROT-032 v1.1, `designs/MS-PROT-032 —Backend Module, Bounded Context & Dependency Architecture.md`, §3 — Core backend layers, governs the inward-facing owner ports and infrastructure adapter. This extends existing jOOQ/Spring/PostgreSQL architecture; no dependency or provider is added.

IMPLEMENTATION-RULES §§2–3, 7–8, 15–21, 30.1, 47–49, 52.14 and 55 govern verification, scope and closure. No accepted design, DDR entry, semantic lexicon entry or programme dependency is changed.

## Implemented responsibilities

- `src/main/java/mainstreet/commercial/CommercialCatalogueStore.java`: Commercial-owned publication/exact-generation/effective-time port; provides the existing `FreePlanRevisionAuthority` contract through authoritative historical reads.
- `CommercialCataloguePublicationRequest`, `CommercialCataloguePublication` and `CataloguePublicationException` in that package: immutable request, retained result and distinguishable publication failures. `CatalogueResolutionException` now distinguishes technical resolution failure from absence, coverage, future and integrity outcomes.
- `src/main/java/mainstreet/application/TrustedPlatformExecutionContext.java`: established PLATFORM attribution without a fabricated merchant scope or assumed human-only principal.
- `src/main/java/mainstreet/commercial/CommercialCataloguePublicationAdmission.java`: mandatory injected trusted validation boundary. Current platform publication authority is checked even on retries. New publication requires exact approved-content, target/support and allocation validation. There is no permissive default or production implementation in this checkpoint.
- `src/main/java/mainstreet/infrastructure/persistence/commercial/CommercialCatalogueManifestCodec.java`: versioned deterministic binary representation of all immutable manifest fields, including explicit grants, bindings, support classifications and evidence references. It retains historical content independently of live registration, handles UTF-8 without short-string truncation, rejects malformed/trailing content and fails explicitly on unsupported encoding versions. It is not an approval signature or tamper-proof security mechanism.
- `src/main/java/mainstreet/infrastructure/persistence/commercial/JooqCommercialCatalogueStore.java`: required-admission publication; atomic receipt/manifest/predecessor/time/head persistence; retained identity checks across all generations; exact retry recovery; historical selection using the accepted immutable snapshot algorithm; persisted read fence.
- `src/main/resources/db/migration/V66__commercial__create_immutable_catalogue_publication.sql`: publication chain and singleton coordination row. The initial singleton is not a catalogue, P0, a production grant or account backfill. Database constraints enforce unique identities, unique instants, one initial generation and at most one immediate successor per predecessor.

## Consistency and precision mechanism

Publication and historical reads acquire the same singleton row lock. Each operation runs in its own READ_COMMITTED transaction and returns only after its effect/receipt/read fence commits; an unrelated outer transaction cannot erase an already-returned result. The normal adapter obtains `clock_timestamp()` inside this boundary. Deterministic time substitution is restricted to a package-private test constructor.

Publication timestamps use PostgreSQL microsecond precision. Successful effective-time reads retain the maximum exact queried Instant as lossless text, preserving nanosecond boundaries. A new publication must be strictly later than both the previous publication and the retained read watermark. Clock regression or insufficient precision fails publication; it never manufactures a future timestamp or backdates. Failed historical reads do not establish a successful-read watermark. Exact-generation reads do not reinterpret time or substitute the head.

The adapter loads and validates retained history while holding the lock. A committed request retry rechecks current caller authority and exact input/principal affinity, then returns the original retained result without re-admitting historical content against today's definitions. New requests validate all retained identity affinities and the exact expected predecessor before committing.

The first implementation deliberately serializes catalogue access and validates the retained chain. This is a bounded, correctness-first catalogue implementation, not a proven high-throughput cache or final performance qualification. Any later optimisation must retain the same publication/read-history invariant.

## Test-first sequence and observed verification

The initial `JooqCommercialCatalogueStoreIT` specifications failed compilation before the new types and adapter existed. After implementation, 32 focused existing catalogue tests passed. The first targeted PostgreSQL run passed 14 tests in 33.780 seconds. Expanded verification passed the codec/governance selection and 19 PostgreSQL tests in 35.100 seconds, including both actual publication/read lock-wait directions. Two further cases then cover concurrent identical publication and identity reuse against a non-head retained generation.

Full gate `mvn --batch-mode clean verify -Ppostgres-it` passed 1,292 unit/governance tests and 455 PostgreSQL integration tests, with zero failures/errors/skips. This includes five codec tests and all 21 catalogue persistence tests. Maven reported BUILD SUCCESS in 4 minutes 45 seconds, finishing at 2026-09-14T23:21:31+01:00. The local disposable PostgreSQL 18.6 database applied/validated all 66 migrations. This verifies the bounded adapter and existing regression suite, not production admission or C3 completion.

After graph/evidence/status synchronization, `mvn --batch-mode '-Dtest=mainstreet/governance/*Test,mainstreet/commercial/*Test,CommercialCatalogueManifestCodecTest,StandingFreeFromMerchantAccountEstablishedHandlerTest' test` passed 137 tests with zero failures/errors/skips in 25.246 seconds, finishing at 2026-09-14T23:22:25+01:00. The instructed branch and accepted authority remained unchanged. The disposable database container and its ephemeral fixture data were removed after the full gate.

## Falsification scope

`src/test/java/mainstreet/infrastructure/persistence/commercial/JooqCommercialCatalogueStoreIT.java` exercises retained manifest/receipt recovery with a recreated adapter, half-open historical boundaries, missing/old/future coverage, changed payload/predecessor identity conflicts, competing publishers, explicit admission rejection, authorization rejection on retry, historical retry without live-definition reinterpretation, rollback after partial SQL execution, time regression/precision failure, both publication/read concurrency orders, independent caller-transaction rollback, actual database time, malformed stored bytes, missing coordination state and unavailable storage.

The two concurrency tests use latches and inspect PostgreSQL's actual ungranted lock state before releasing the holder. They are controlled interleaving proofs, not a claim to have explored all possible schedules. Recreated adapters model lost-acknowledgement recovery; no operating-system process-kill or disaster-recovery drill is claimed.

`src/test/java/mainstreet/infrastructure/persistence/commercial/CommercialCatalogueManifestCodecTest.java` verifies complete round-trip content, deterministic set encoding, malformed lengths/truncation/trailing data, unsupported encoding and long Unicode evidence. Codec decoding never consults a current binding registry.

## Admission and production non-claims

The injected admission contract is deliberately not implemented from guessed rules, an `approved=true` field, a nonblank reference, merchant-controller status, subscription level or provider credentials. The integration tests supply an explicitly isolated fixture admission implementation; that proves the store invokes the boundary, not that production authentication, authorisation, exact approval or source-contract resolution has been implemented.

No production bean, transport endpoint, approved manifest loader, catalogue seed, scheduled worker or ordinary-account rollout switch is added. Existing baseline recovery still uses the earlier handler's committed-baseline-first path; this change does not rewrite any baseline or account establishment instant. No reserved service is activated.

## Remaining C3 gate and next work

1. Supply the production trusted admission composition and retained exact approval/contract/allocation evidence. The interface and fixture checks are not substitutes for those implementations.
2. Resolve `MS-PROT-056-V17-DQ-001` through the complete DESIGN-RULES chat-only review/falsification/ambiguity process and explicit approval of the concrete FREE/BUSINESS/GROWTH manifest, exact bindings and necessary support classifications. General session permission does not approve those values.
3. Compose the real worker/resolver path, establish the approved initial catalogue before the dependent ordinary new-account path, preserve explicit pre-P0 failure, and complete final C3 production/recovery verification before CONFORMING_COMPLETE.

The user has authorised automatic continuation and a push only when C3 is completed. This checkpoint does not satisfy that condition. It must remain local; C3 stays IN_PROGRESS and no upstream/downstream programme state is promoted.
