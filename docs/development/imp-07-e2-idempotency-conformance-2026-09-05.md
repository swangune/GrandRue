# IMP-07 E2 Conformance — Enquiry Retry Idempotency and Transaction Proof

**Date:** 5 September 2026

**Node:** IMP-07-E2 — Enquiry retry idempotency + transaction proof

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

This is implementation evidence only. It creates no semantic authority.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern selection and closure. MS-PROT-043 v1.4 §§11–16 requires immutable initial evidence, reconciliation of repeated logical deliveries and separation from independent human submissions. MS-PROT-059 §§14–16 distinguishes application request identity from operation/target identity and from current business-invariant revalidation. MS-PROT-025 and MS-PROT-034 govern explicit transaction boundaries and PostgreSQL/jOOQ/Flyway persistence.

E1 closure `5ee6b62ef963856776d34c7aba41806f2cb9490a`, run `33983582172`, passed 1001 unit/governance + 337 PostgreSQL integration tests, zero failures/errors/skips. E2 was READY before implementation.

## Implementation and invariants

`EnquirySubmissionIntent` retains exact merchant, supplied question/contact, optional selected subject revision and optional trusted CustomerContext reference. Generated Enquiry identity, time and resolved semantic evidence are separate from stable supplied intent.

The internal `EnquirySubmissionApplicationService` composes logical retry handling with `EnquirySubmissionPreparation`. The PostgreSQL adapter locks the full logical request's hash transactionally, reads its durable receipt, checks merchant and exact stored intent, and returns the original Enquiry before any new preparation. Hash collisions only serialize work; full request identity remains the persisted key. Request identity is global within this bounded operation, following the existing ApplicationRequestIdentity/Publications application pattern.

For an uncommitted request, preparation executes inside the same Spring REQUIRED transaction, must preserve the supplied intent, and returns the newly prepared immutable fact. The service appends that Enquiry and its request/result receipt atomically. A different intent under an occupied request identity raises a conflict without disclosing the original evidence. Separate request identities with identical content may produce distinct Enquiries.

Flyway V57 adds the receipt table, with exact request identity, merchant-qualified result foreign key and unique result reference. Receipt comparison uses immutable E1 evidence instead of copying contact data or introducing an ad hoc fingerprint. The E1 integration fixture now truncates both related Enquiry tables.

The preparation seam is internal trusted application code, not a frontend callback or generic dispatch endpoint. It must perform current authoritative checks and must not append Enquiries or perform external effects. The service verifies that preparation preserves scope, question, contact, selected subject revision and trusted customer reference; a silent subject-to-general downgrade fails before append.

## Verification trace

The first test compilation failed on absent E2 types. Production implementation then compiled successfully. Three unit tests cover stable intent despite generated/context changes, exact supplied-value comparison and invalid context rejection.

Local full verification passed **1004 tests, zero failures/errors/skips** using Java 26 and the Java 25 target. An earlier run passed 1001 tests before the three added unit tests; an overlapping log redirection prevented one subsequent launch, after which the completed process was confirmed and the final suite ran successfully in its own log.

Nine PostgreSQL tests cover:

1. Retry through a new adapter returns the exact original result without calling preparation again.
2. Concurrent deliveries invoke preparation once and commit one Enquiry and one receipt.
3. Changed merchant, question, contact, subject or customer context conflicts before preparation.
4. Distinct logical requests with identical content remain distinct human submissions.
5. Failure inserting the receipt rolls back the already-appended Enquiry; a clean retry succeeds.
6. Enquiry identity failure leaves no receipt or overwrite; a corrected retry can succeed.
7. Rejected preparation and subject downgrade persist neither fact nor receipt.
8. Caller transaction rollback removes both records.
9. Separate merchant requests can use the same merchant-scoped business identity without leakage.

Implementation commit: `415ba5c534b5e12ddbdb087583fd87401bd8dcd6`.

Full Java 25/PostgreSQL CI run `33984114714`: SUCCESS — 1004 unit/governance + 346 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Structural review, limits and closure

Immutable intent and preparation/service contracts stay in the Enquiry package; jOOQ and Spring transaction orchestration remain in infrastructure. No Enquiry workflow lifecycle, subject-owner mutation, customer inference, notification, Conversation, commitment, transport or semantic authority amendment was added.

E2 proves retry/transaction mechanics, not the concrete current merchant/subject/Exposure/participation resolver. E3 must supply that authoritative preparation. Trusted access to the merchant and original result is still required on retries; a request key does not grant access. Tests use deterministic preparation fixtures and real PostgreSQL transactions; they do not claim public end-to-end revalidation.

The graph, status, evidence and programme gate close together only on successful synchronization CI. Select E3 next; M1 remains independently READY. IMP-07 remains IN_PROGRESS.
