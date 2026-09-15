# IMP-08C-C3 Manifest Structure Checkpoint — 14 September 2026

**Node:** IMP-08C-C3 — Execution failures/outcome ledger
**State:** IN_PROGRESS
**Branch:** development
**Inspected implementation base:** dcd0ba2746a5f1d7a138273d4eceefc959f862b6
**Classification:** B — implementation of already accepted Commercial-owned invariants
**Closure claim:** NONE

## Authority and scope

This record is implementation evidence, not a concrete catalogue proposal or design authority. Current composition is resolved through AUTHORITY-INDEX v4.15. The exact governing constituent is MS-PROT-056 v1.9, `designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md`: §4 — Exact binding requirements; §5 — Binding identity and satisfaction; §6 — Manifest completeness; §8 — Publication operation; §13 — Retention, recovery and runtime boundaries. Its §17 — Deferred decisions and readiness preserves the complete concrete manifest approval gate.

Target-family representation reuses the existing enum under MS-PROT-056 v1.0, `designs/MS-PROT-056 — Subscription Plans, Full-Experience Entitlements & Commercial Availability Model.md`, §7 — Initial entitlement target families. Supporting access checks implement the mechanically expressible same-plan requirement from v1.9 §6, preserving `designs/MS-PROT-056 v1.7 — Standard Tier Allocation & Future Commercial Portfolio Amendment.md`, §10 — Supporting services and platform quality, and `designs/MS-PROT-056 v1.8 — Analytical Portfolio Allocation & Presentation Access Amendment.md`, §6 — Analytical presentation follows the represented service. No allocation table is translated into production grants.

IMPLEMENTATION-RULES §§2–3, 7–8, 11–12, 30.1, 47–49, 52.14 and 55 govern the cycle. Ordinary immutable Java values and deterministic validation fit this bounded Commercial responsibility. No framework, infrastructure dependency, cross-owner mutation, persistence migration, authority amendment or new production registration is introduced.

## Implemented responsibilities

- `src/main/java/mainstreet/commercial/CommercialAccessTarget.java`: explicit owner, target identity and governing contract revision. Rejects blank components and literal wildcard syntax; it does not prove that an arbitrary nonblank reference names an accepted target.
- `src/main/java/mainstreet/commercial/CommercialSupportingAccessRequirement.java`: exact supporting target, explicit required-purpose set and retained classification-authority reference. An explicitly empty set carries a claimed no-independent-entitlement classification; missing/null classification information is not treated as an exemption.
- `src/main/java/mainstreet/commercial/CommercialAccessBinding.java`: immutable entitlement-to-target/purpose binding evidence, target family, governing authority, support requirements and new-use/residual-boundary authority reference.
- `src/main/java/mainstreet/commercial/CommercialCatalogueManifest.java`: immutable proposed content containing the existing three-plan revision, complete referenced binding snapshots, allocation-evidence references and approval-provenance reference. It rejects unbound grants, duplicate entitlement meanings, duplicate exact target/purpose identities, missing supporting-purpose bindings, conflicting no-entitlement classifications and support not granted in the same plan. Validation of every binding's support enforces transitive grant closure without recursive runtime execution.
- `CommercialCatalogueManifest.requireCompatibleWith(...)`: pairwise retained-generation checks for catalogue-content identity, entitlement binding immutability, canonical exact-target/purpose identity and plan-content identity. A future publisher must compare against all retained generations, not merely the immediate predecessor. Unchanged bindings may be repackaged into new explicit plan snapshots; existing snapshots remain unchanged.

All binding definitions are embedded as immutable snapshots rather than resolved from a mutable live registry. Reused identities therefore retain their exact definition content for compatibility checks. Existing runtime entitlement resolution and the legacy definition registry are unchanged; the new model is not an alternate runtime grant authority.

## Test-first sequence and verification

`src/test/java/mainstreet/commercial/CommercialCatalogueManifestTest.java` was added before production types. The first targeted Maven run failed compilation because the four new types did not exist. After implementation, the focused command `mvn --batch-mode '-Dtest=CommercialCatalogueManifestTest,StandardPlanCatalogueHistoryTest,StandardPlanCatalogueRevisionTest' test` passed 35 tests (18 manifest, 11 historical-selection, 6 plan-revision), zero failures/errors/skips, in 21.068 seconds. Three additional falsification tests then covered transitive support, BUSINESS support incorrectly available only in GROWTH, and changed retained support/residual-boundary evidence.

Full gate `mvn --batch-mode clean verify -Ppostgres-it` passed 1,287 unit/governance tests and 434 PostgreSQL integration tests with zero failures/errors/skips. This includes all 21 manifest tests after the additional falsification coverage. Maven reported BUILD SUCCESS in 4 minutes 39 seconds, finishing at 2026-09-14T23:04:44+01:00. The disposable local PostgreSQL 18.6 database exercised all 65 existing migrations; this change adds no migration. The full run revalidates existing integration coverage, not nonexistent catalogue-storage behaviour.

After evidence/graph/status synchronization, `mvn --batch-mode '-Dtest=mainstreet/governance/*Test,mainstreet/commercial/*Test,StandingFreeFromMerchantAccountEstablishedHandlerTest' test` passed 132 tests, zero failures/errors/skips, in 24.931 seconds (finished 2026-09-14T23:05:25+01:00). The instructed branch and governing authority remained unchanged. The disposable PostgreSQL container and its ephemeral test data were removed after full verification.

## Falsification and non-claims

The model checks supplied structure and retained-content compatibility, not the truth or authenticity of supplied evidence. Fixture strings deliberately do not pretend to be accepted registrations. Nonblank references must still resolve against accepted target contracts; omitted real supporting dependencies, vague natural-language targets, unresolved reservations, allocation-policy violations and forged approval references require trusted publication admission checks. Constructing this model must never be used as evidence that those checks passed.

In particular, the approval reference is retained data, not an approval token, caller principal, signature or exact-approved-content verification. This checkpoint does not implement publication authorisation, approval-affinity verification, publication-request idempotency, atomic storage, authoritative publication timestamps, database publication/read race fencing, storage-backed historical resolution or worker activation. It publishes no production or fixture catalogue.

Tests verify explicit multi-purpose support, owner/revision qualification, immutable nested sets, history compatibility and same-plan support including indirect dependencies. They do not prove external contract resolution, trusted approval, PostgreSQL catalogue persistence or process recovery. Existing PostgreSQL worker/owner tests remain independently scoped evidence.

## Remaining C3 work

1. Implement the durable publisher and storage-backed reader with trusted caller/approval/contract/allocation validation, atomic immutable evidence and request affinity, predecessor conflict handling, historical-read coordination and PostgreSQL recovery/concurrency proof. This checkpoint supplies the structural input model, not that implementation.
2. Resolve `MS-PROT-056-V17-DQ-001` through the complete chat-only DESIGN-RULES proposal, review/falsification/ambiguity cycle and explicit manual approval of exact production entitlement identities, bindings, support classifications and all three grant sets. Neither these fixture manifests nor general session permission closes that gate.
3. Complete worker composition and rollout ordering, including initial catalogue before the ordinary new-account path and explicit pre-P0 failure; complete final C3 verification before node closure.

No new design blocker was resolved or crossed in this cycle. C3 stays IN_PROGRESS and C5's dependency state is unchanged. Durable storage remains the next implementation responsibility, not further website expansion.
