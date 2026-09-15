# IMP-07 T2A Conformance — General PUBLIC Enquiry Command Delivery

**Date:** 5 September 2026

**Node:** IMP-07-T2A — Merchant-general PUBLIC Enquiry command delivery

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

Implementation evidence only; no semantic authority is created.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern execution. Composite MS-PROT-035 governs explicit PUBLIC commands, trusted scope, logical retry, safe completed/rejected/uncertain outcomes and transport-owned bounds. Composite MS-PROT-043 v1.2/v1.4 governs general versus subject-specific Enquiry, contextual owner requirements, supplied contact versus identity, no automatic CustomerContext and immutable submission. MS-PROT-059 governs logical-request idempotency.

T1B/T1 closure `eca3df9d76e271b4d2c982fbcf3b2c427d743cf2`, run `33989939024`, passed 1052 unit/governance + 357 PostgreSQL integration tests, zero failures/errors/skips.

Before implementation, `imp-07-t2-decomposition-2026-09-05.md` refined T2 into general command delivery (T2A) and Opportunity binding carry-forward/submission (T2B). I2 bindings remain internal and T1 omits revision/binding internals. T2A does not invent a browser token or silently reinterpret subject-specific input.

## Implementation

PublicGeneralEnquiryContract registers enquiry/public-general-enquiry as PUBLIC COMMAND over enquiry/send-enquiry, with a public visitor class, owner input/retry references, atomic completion, safe acknowledgement/problem representations and submitted-contact data classification.

The opt-in `enquiry-public-api` profile wires POST `/api/public/storefronts/{locator}/enquiries/general`. An explicit server-configured locator registry establishes Merchant Scope through the registered API scope authority. There is no locator-to-Merchant-ID fallback. Client scope/customer/subject fields and all other unknown JSON fields are rejected; the general route cannot silently downgrade an Opportunity enquiry.

The input carries only question and supplied name/email/telephone. Transport limits are 10,000 characters for the required nonblank question, 500 for each optional contact value, and 200 for the required nonblank Idempotency-Key. These are parsing/bounds choices, not a universal Enquiry requirement schema. Supplied strings are preserved without identity inference or normalization. Query parameters are rejected.

A mandatory PublicEnquirySubmissionAdmissionAuthority checks current permission/protection for this registered anonymous public operation before every application attempt, including retries. Missing authority cannot acquire a permissive default. It grants no stored-data observation or CustomerContext access.

The client retry key is translated to a deterministic operation/merchant-qualified ApplicationRequestIdentity with an unambiguous length-delimited Merchant Scope. It is not the Enquiry identity, trace identity or access proof. E2 remains responsible for atomic submission/receipt storage, exact intent conflict and original-result reconciliation.

Fresh submissions use existing E3 OpportunityEnquirySubmissionPreparation in its general path with no subject binding. It checks the active Enquiry operation and PUBLIC interaction and invokes mandatory PublicGeneralEnquiryRequirements inside the E2 transaction. That owner preparation must resolve applicable requirements and generate identity without append or external effects. It is a specialized functional owner contract, not an implemented universal contact schema. General delivery supplies neither a subject nor a CustomerContext and does not read Publication state.

Success returns only `{"outcome":"COMPLETED"}` with no Enquiry identifier, submitted values, stored provenance or read-access token. The same acknowledgement is used for successful replay. Known invalid/admission/applicability/conflict failures return REJECTED with a safe ApiProblem. Unexpected application failures return OUTCOME_UNCERTAIN/503 rather than falsely asserting that no commit occurred. Known pre-application rejection is represented by a private-construction command-boundary exception, separate from uncertain application failures. All responses use Cache-Control: no-store.

## Verification trace

Initial RED was test compilation on absent T2A types. Seven initial tests passed; the eighth failed because re-stubbing the application's existing Mockito answer invoked it with null matcher arguments. The fixture was corrected with doThrow; this was not a production defect. All eight focused tests then passed. A ninth test verifies opt-in Spring profile construction with all required providers.

Local full verification passed **1061 tests, zero failures/errors/skips**, using Java 26 and the Java 25 target.

Nine unit/composition tests cover exact PUBLIC command registration, real E3 general preparation and content-free acknowledgement, explicit unknown/subject/customer/scope rejection, missing/invalid retry input, unregistered locator rejection, current admission before application, unmet owner requirements, inactive interaction rejection, conflict versus uncertain response semantics, unambiguous scoped retry identities and Spring profile wiring. The general path is verified not to invoke Publication state/lock providers.

Four PostgreSQL proofs compose HTTP delivery with the real E2 transaction/receipt and E1 stores: replay after interaction change with no fresh preparation; changed-intent conflict plus a distinct new logical submission of identical content; a simulated lost result after actual commit followed by duplicate-free retry; and admission revocation preventing access to the application retry boundary.

Implementation commit: `115db6a825beafc5a6cd58cba5a0fbbe37c87332`.

Full Java 25/PostgreSQL CI run `33990790405`: SUCCESS — 1061 unit/governance + 361 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Structure, limits and closure

Transport code remains Enquiry-owned and composes existing E2/E3 authority. No migrations, receipt changes, CustomerContext creation, contact matching, communication effects, generic form language or semantic amendment were added.

The profile is implemented and tested, not deployed by this cycle. Deployment must supply trusted route configuration, current public admission, active configuration/registry, durable E2 application service and Enquiry-owned requirement preparation. Tests use deterministic current-admission/requirement providers and active configuration; PostgreSQL tests replace the application/storage fixture with real durable boundaries.

Evidence, graph, status and programme gate close together on successful synchronization CI. T2A completes only the merchant-general path. T2B must implement the concrete Opportunity binding handoff with stable retry intent and E3 revalidation. T2 and V1 remain incomplete; IMP-07 stays IN_PROGRESS.
