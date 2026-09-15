# IMP-07 V1 Conformance — Complete Publication → Enquiry Vertical Proof

**Date:** 5 September 2026

**Node:** IMP-07-V1 — Full Publication → Enquiry integration proof

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

Implementation evidence only; no semantic authority is created.

## Authority and prerequisites

MS-IMP-001 v1.0 + v1.1 §13 requires the full path from an established/configured merchant and exposed Merchant Profile through Publication, public customer observation, Enquiry submission and merchant observation. IMPLEMENTATION-RULES v1.6 governs tests-first verification, retained evidence and synchronized closure. Composite MS-PROT-027, MS-PROT-035, MS-PROT-043 and MS-PROT-046 govern bounded observation, transport, Enquiry and Publication. MS-PROT-059 governs logical retry; ADR-014 governs session continuity.

T3 closure `6657a12b20218970312a1f0ace907e36d1db4d08`, run `33993321981`, passed 1079 unit/governance + 370 PostgreSQL integration tests, zero failures/errors/skips. All required owner nodes and T1/T2/T3 adapters conform. P6 actionability remains a separate dependency-blocked branch.

## Integrated proof

PublicationEnquiryVerticalV1IT uses a single Merchant Scope from the real durable MerchantAccountEstablisher and PostgreSQL bootstrap store throughout each scenario. The shared establishment fixture executes the existing IMP-05 onboarding, initial intent materialisation, compilation, validation, impact review, approval, executable-support/serving admission, durable configuration activation, first trial and guarded runtime assertions. It also creates the durable Merchant Public Descriptor through its owner authority.

The original JooqMerchantEstablishmentToActiveRuntimeIT retains its named test and all 11 assertions by calling the shared fixture in its original configuration. V1 selects a separate explicit fixture variant with the registered publication/opportunity object, enquiry/send-enquiry creation operation and PUBLIC interaction contribution. That package is validated, approved, admitted and activated through the same durable path; V1 does not mock activation or install a bypass pointer.

The profile is read from PostgreSQL through the Profile-owned projection port, real initial P2 portfolio, PUBLIC admission and Exposure, exact assembly and the registered PublicMerchantPresenceQueryPath. The selected descriptor includes the established merchant name. Unacquired presence sources carry explicit UNAVAILABLE/MISSING/UNVERIFIABLE evidence; they are not marked current or not applicable. This proves the existing bounded public profile path; it does not claim a new HTTP profile DTO or route.

The same merchant then creates and publishes Opportunity material through JooqOpportunityPublicationApplicationService, exercising durable draft/material, transaction, retry receipt and Publication History. The proof combines actual controller routes in one MockMvc composition:
- T1 PUBLIC Opportunity GET.
- T2B exact I2 binding GET and PUBLIC Enquiry POST.
- T3 authenticated MERCHANT Enquiry GET.

Those routes share the real durable activation, Publication authority/lock, Enquiry application/receipt/submission stores and Session Record store. Public queries execute real acquisition, P2, Exposure and selected mapping. Submission carries the exact selected binding into E2/E3 and merchant delivery observes the resulting immutable submission. The proof calls no mocked persistence or activation boundary.

Four scenarios prove:
1. Established/configured merchant and public profile → published Opportunity → browser binding → submitted Enquiry → permitted merchant content/contact/original subject revision. Stored semantic release matches the activated release; supplied contact creates no CustomerContext.
2. Publication R1 → R2 changes the public title and fresh binding. Reusing a prior key with R2 conflicts; R1 intent still replays; a fresh stale request rejects; a fresh R2 request succeeds. Merchant observations retain R1 for the original submission and R2 for the new submission.
3. Four concurrent HTTP deliveries of one logical submission reconcile to one preparation and one durable Enquiry. A separately committed submission whose result is lost returns OUTCOME_UNCERTAIN, then reconciles after withdrawal without duplication. Fresh submission after withdrawal rejects, while merchant observation retains original revision evidence.
4. A binding submitted into a different trusted route scope rejects before creation. Contact-family withholding leaves permitted content intact and removes supplied contact from the response. Revoked public admission blocks even replay; persisted session revocation blocks merchant observation. Stored contact remains unchanged.

Concurrency uses bounded latches/future waits and actual PostgreSQL transactions/advisory locking. No timing-only sleep or mocked single-commit assertion replaces the durable count.

## Verification trace

Tests-first RED was compilation on the absent shared fixture and a mistaken membership-fixture principal accessor. The fixture was extracted, the existing test delegated without losing assertions, and the principal parameter was corrected to its existing String contract. These compile errors are recorded as fixture construction corrections, not pre-existing runtime defects.

Local full verification passed **1079 tests, zero failures/errors/skips** on Java 26 targeting Java 25. The integration tests compile locally and run under the required PostgreSQL CI profile.

The first PostgreSQL run `33994929897` at `aaafda4a80b31e37f9f023837e45cc71baddc403` failed all four new cases during profile setup: the fixture omitted evidence rows for the other declared Merchant Presence sources. The production engine correctly rejected the incomplete evidence set. The fixture now explicitly marks unacquired sources unavailable, allowing real policy to decide truthful reduced serviceability. Six existing serviceability regressions passed after correction; no policy was relaxed.

Implementation/proof commit: `fccef99cf13789ac9302bd4efe3bec237c61eede`.

Full Java 25/PostgreSQL CI run `33995174577`: SUCCESS — 1079 unit/governance + 374 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Structure and limits

This cycle adds the integrated proof and a shared test fixture. No production source, schema, transport semantics, authority grant or retry behavior needed modification. Prior tests/evidence remain retained and the full suite verifies the fixture extraction.

The fixture supplies explicit deterministic platform establishment/approval, scoped-principal, security-generation, merchant membership, current public admission, family privilege and remaining Enquiry requirement providers. Durable owner/configuration/session truth and all integrated runtime/observation/submission boundaries execute production code. Fixture semantic bundles, support descriptors, clocks and binding keys are test composition, not deployment claims.

HTTP routes are exercised in MockMvc, with actual production controllers and services; this is not a deployed network server or browser UI. Profile observation uses the accepted bounded query handoff. No provider call, communication effect, current-subject projection, CustomerContext inference or P6 actionability is introduced.

## Closure

On successful cycle-closing synchronization CI, V1 conforms and IMP-07's mandatory programme gate is demonstrated. The graph, implementation status, macro closure and executable programme-gate test synchronize this evidence. IMP-08A/B/C become eligible for dependency/remaining-work assessment under MS-IMP-001; they are not automatically complete and no Git branch creation is authorized. Consequential provider/financial work still requires its own downstream hard prerequisites.
