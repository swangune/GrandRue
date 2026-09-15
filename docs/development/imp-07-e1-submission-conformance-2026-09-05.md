# IMP-07 E1 Conformance — Durable Enquiry Submission and Provenance

**Date:** 5 September 2026

**Node:** IMP-07-E1 — Durable Enquiry submission/provenance foundation

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

This is implementation evidence only. It creates no semantic authority.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern this cycle. Composite MS-PROT-043 v1.2 §§9, 19–23, v1.3 §§2–3 and v1.4 §§8–14, 16–23 govern Enquiry-owned immutable initial submission truth, supplied contact values, optional trusted CustomerContext reference, exact SUBJECT provenance and separation from communication, attention and commitments. MS-PROT-049 v1.2/v1.4 preserve the distinction between binding projection and execution authority. MS-PROT-025 and MS-PROT-034 govern ownership, atomicity and PostgreSQL/jOOQ/Flyway persistence.

I2 closure `b6bff57c580178c8b6ee9c38fd277bb8fa1aaead`, run `33982554318`, passed 996 unit/governance + 329 PostgreSQL integration tests, zero failures/errors/skips. E1's dependency was satisfied before implementation.

## Implementation and bounded choices

- Immutable `EnquirySubmission` stores merchant-scoped identity, exact submission time, original question, supplied contact values and submission-time registry/model identity evidence.
- Optional `EnquiryRevisionProvenance` records the owner-qualified object type, exact subject identity and reconstructible revision within the Enquiry's merchant scope. It is evidence for the SUBJECT relationship, not a new EnquirySubject Operational Object or subject snapshot.
- Optional CustomerContext identity records only a reference supplied by the trusted application caller. No matching by contact, authentication inference, CustomerContext creation or account creation occurs.
- `EnquirySubmissionStore` is an internal append/read persistence port. `JooqEnquirySubmissionStore` inserts all evidence in one statement and participates in a caller's Spring transaction through a transaction-aware DSLContext.
- Flyway V56 creates `enquiry_submission` with merchant-qualified primary key and all-or-none subject/revision constraints. Epoch seconds plus nanoseconds preserve exact Instant precision. Contact/question text is not normalized.
- An occupied identity raises a conflict and never updates evidence through the store. A new identity with identical content is a distinct submission. Logical-request retry reconciliation belongs to E2.

No generic form DSL, arbitrary context map, public endpoint, stored workflow state, Conversation, notification, commitment, customer reconciliation or subject-owner mutation was introduced. Core immutable values depend on domain contracts only; SQL remains in the infrastructure adapter. The exact encoding choices are implementation details explicitly deferred to persistence implementation by MS-PROT-043 v1.4, not new semantic authority.

## Verification trace

The first focused test compilation failed on the absent Enquiry types. After implementation, all five value-contract tests passed. Local full verification passed **1001 tests, zero failures/errors/skips** using Java 26 and the Java 25 target.

Eight PostgreSQL integration tests prove exact reconstruction (including nanoseconds and supplied whitespace), general submission with absent optional values, missing reads, merchant isolation, conflict without evidence overwrite, distinct human submissions with identical content, caller transaction rollback, rejection of partial subject evidence, and durable R7 provenance after the real Publication authority advances to R8 and withdraws. The cross-owner fixture is rolled back; production Enquiry persistence neither writes Publication nor adds a cross-owner foreign key.

Implementation commit: `83f433e39de709c587cfa32385b7af0151cec53c`.

Full Java 25/PostgreSQL CI run `33983277431`: SUCCESS — 1001 unit/governance + 337 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Conformance limits and closure

This port stores already-resolved facts; constructors and append are not trusted public submission orchestration. Current merchant/interaction/subject/Exposure checks and trusted customer association resolution must be performed before use. E3 retains authoritative revalidation ownership. Contact requirements are deliberately not decided by persistence. Revision-less subject owners require their own bounded evidence implementation before using this path; the current vertical slice has reconstructible Opportunity revisions.

Immutability is enforced by immutable values and the append-only store contract, with identity and evidence-shape constraints in PostgreSQL. Direct administrative SQL is not an Enquiry mutation API. The single-row rollback proof does not claim E2's full logical-request idempotency/transaction protocol.

This evidence, the E1 graph, implementation status and programme gate close together only on successful synchronization CI. E2, E3 and M1 then become READY; select E2 next for logical retry idempotency and the application transaction proof. IMP-07 remains IN_PROGRESS.
