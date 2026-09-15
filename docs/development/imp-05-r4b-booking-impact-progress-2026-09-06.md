# IMP-05-R4B — Booking availability and residual-management impact increment

**Rules:** IMPLEMENTATION-RULES v1.7. **Node state:** IN_PROGRESS. **Entry baseline:** clean `development@82832ed3227cef96f7c0f13894f3bf2a2de370ad`, equal to origin. R4A implementation `5bfd53a8b3a092e34e1345746a2b7273e076a620`, CI `34030230932`: SUCCESS, 1139 unit/governance + 401 PostgreSQL tests, zero failures/errors/skips. Closure CI `34030492958` was still in progress at entry; it is not represented as completed baseline verification here.

## Exact authority

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, v1.0 §20 Configuration diff, §21 Impact analysis, §22 Impact classifications, §§23–24 protected commitments and historical affinity, §26 Capability deactivation, §56 Booking capability deactivation validation.
- `designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md`, §4 Semantic-registry affinity; §5 Validation and Resolved Configuration Package production.
- `designs/MS-PROT-042 v1.4 — Booking Residual Obligation & Discharge Amendment.md`, §1 Governing decision, §2 Narrow reservation-commitment state, §3 Outstanding Booking-owned obligation, §4 Discharge, §5 Time passage is not universal discharge authority.
- `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`, §§9–11 classified business-facing effects and exact-result production predicate.
- `designs/IMPLEMENTATION-RULES.md`, v1.7 §§30, 46–48, §52 canonical loop and §§52.14–52.19. This increment does not change approval policy or any accepted macro edge.

## Implemented responsibility and ownership

`src/main/java/mainstreet/booking/BookingAvailabilityImpactAssessment.java:35`, `assess`: compare the candidate's validated compiled capability membership with the historical base compiled under its own pinned registry release. Direct selection names alone cannot establish whether Booking is active because registered dependencies may activate it. Missing historical release fails assessment.

Enabling/disabling new Booking activity produces an intelligible effect and a CONSEQUENTIAL finding. If new Booking activity is disabled, consume `BookingResidualObligationAuthority.hasOutstandingBookingObligation`; a positive owner result produces an INFORMATIONAL residual-management finding. It does not invent a schedule conflict or relabel every outstanding reservation as EXISTING_COMMITMENT_CONFLICT. The actual owner adapter is `src/main/java/mainstreet/infrastructure/persistence/booking/JooqBookingResidualObligationAuthority.java`, `hasOutstandingBookingObligation`: merchant-scoped IN_FORCE reservation truth. The assessment has no mutation port and does not discharge, cancel, reschedule or otherwise change bookings.

Paradigm fit: Booking owns the operational meaning and effect description; Configuration supplies immutable validated context; the compiler supplies exact static dependency closure; the existing database adapter supplies Booking-owned current facts. No generic compiler branch, new semantic policy, new database schema or library is introduced.

## Tests and verification scope

`src/test/java/mainstreet/booking/BookingAvailabilityImpactAssessmentTest.java` methods at :27, :40, :56, :67, :79, :93 and :103: initial enablement without invented self-service, deactivation with residual obligations, already-disabled residual management, narrow unchanged-membership scope, both directions of release-dependent activation closure, missing historical release and unavailable owner.

`src/test/java/mainstreet/infrastructure/persistence/booking/JooqBookingResidualObligationAuthorityIT.java` new methods at :108 and :122: real PostgreSQL owner-backed assessment, elapsed IN_FORCE reservation remains relevant and unchanged, RELEASED and foreign-merchant rows do not create residual findings. Compiler/package resolution and Booking storage/assessment are real. Configuration revision/validation context is constructed in this bounded adapter test; R4C still must prove the complete durable Configuration pipeline.

Tests-first RED: targeted Maven failed on missing `BookingAvailabilityImpactAssessment`. Targeted GREEN: all 16 Booking/orchestration tests passed. Full local `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 test`: BUILD SUCCESS, 1146 tests, zero failures/errors/skips. Implementation baseline `ffa057b51c70177c20fb2fcd2ad87ae5f118ce9d`, Java 25/PostgreSQL 18 CI `34030941696`, job `101480190715`: SUCCESS, 1146 unit/governance + 403 PostgreSQL tests, zero failures/errors/skips. Both new PostgreSQL cases passed. This proves the bounded Booking increment; it does not complete R4B. `git diff --check` passed.

## Counterevidence and remaining R4B gap

- A direct selection diff would miss dependency-induced Booking activation/deactivation; tests exercise unchanged direct selections across two different pinned releases.
- An elapsed reservation interval would falsely erase an obligation; PostgreSQL proof must use IN_FORCE truth and compare the stored row before/after assessment.
- An outstanding Booking is not automatically a scheduling conflict. This implementation reports required residual management, not a fabricated conflict.
- No findings from this narrow assessment means no finding about **Booking membership/residual management**; it does not certify Booking policy, scheduling, payment, provider, access or other capability effects.
- R4B still requires concrete coverage of remaining capability effects, resolved policy changes, binding/routing effects and applicable owner-backed commitment conflicts. Composition must establish all applicable assessment coverage; one Booking assessor cannot certify that coverage.
- R4C durable review integration remains blocked on full R4B completion. R4B, R4, and IMP-05 must not be marked CONFORMING_COMPLETE on this increment. No historical evidence is rewritten.
