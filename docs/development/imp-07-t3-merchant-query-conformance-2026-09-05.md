# IMP-07 T3 Conformance — MERCHANT Enquiry Query Adapter

**Date:** 5 September 2026

**Node:** IMP-07-T3 — MERCHANT Enquiry query adapter

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

Implementation evidence only; no semantic authority is created.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern execution. Composite MS-PROT-035 governs explicit MERCHANT_OPERATIONAL query contracts, trusted scope and safe observation delivery. Composite MS-PROT-043 v1.4 §§22–25 governs merchant Enquiry observation, separate submitted contact and submission-time subject provenance, and the accepted Exposure families. MS-PROT-027 governs serviceability and exact bounded selection. ADR-014 and the existing session/currentness boundaries govern opaque credential resolution; authentication grants no merchant authority by itself.

T2B/T2 closure `11010d07b76d79b8086a19cfd15c5334750aad63`, run `33992305320`, passed 1069 unit/governance + 366 PostgreSQL integration tests, zero failures/errors/skips. M2 is conforming and supplies the immutable request-scoped representation. The current graph selects T3 without another decomposition.

## Implementation

The opt-in `enquiry-merchant-api` profile registers the Enquiry-owned MERCHANT_OPERATIONAL query and GET `/api/merchant/workspaces/{locator}/enquiries/{identity}`. A mandatory server-owned workspace locator registry establishes Merchant Scope through the exact API scope rule. There is no Merchant-ID fallback and no client principal, role or privilege input.

The route accepts exactly one Authorization bearer header. Missing, duplicate, malformed or unresolved credentials produce a safe UNAUTHENTICATED response with HTTP 401 and a Bearer challenge. The credential is bounded to 4,096 characters and the existing opaque credential alphabet. Query parameters are rejected. No credential is logged, echoed or persisted by this adapter.

Generic MerchantBoundedQueryExecution establishes the exact request and captured configuration release, resolves the credential through SessionTrustedExecutionContextEstablisher, and establishes the merchant-interactive audience context. Existing current session and merchant-association admission precedes material acquisition. After real M2 acquisition and P2 evaluation, admission is evaluated again before Exposure, so revocation during acquisition fails closed. API binding, request/read affinity and exact Exposure binding remain generic Surface responsibilities.

The Enquiry-owned serviceability portfolio requires complete current immutable submission evidence with exact source progress for freshness, serviceability, missing-evidence and stale-serving policy categories. Submission identity is stable progress under E1 immutability; there is no stale fallback. Missing records return a safe absent result after admission.

Current M1 evaluation separately checks actor authorization for each family and current owner candidacy. MerchantEnquiryObservationPrivileges requires explicit privilege mappings for all four accepted families; it supplies no universal read privilege, membership grant or permissive default.

MerchantEnquiryResponseAdapter reuses MerchantProjectionAssemblyService and maps only the exact selected typed fragments. It validates the query/projection/read-use, exact resource candidates and unique families. It has no repository or authorization dependency and cannot reacquire values after selection. The transport DTO contains an Enquiry identity and optional, separately selected submission, contact and subjectContext objects. An empty selection returns the same safe 404 as absence.

Submission contains question and exact time. Contact is explicitly supplied name/email/telephone and disappears entirely when withheld. SubjectContext carries owner/type/subject identity and an explicitly named submissionRevisionIdentity. Original subject evidence never becomes current subject truth. Internal model/session/request/source evidence is omitted. If only contact is authorized, content and subject remain absent; no primary-family grant is inferred.

Every response uses Cache-Control: no-store. Known query failures map to safe 400/401/403/404/503 problems; unexpected failures produce REPRESENTATION_UNAVAILABLE without exception details or submitted values. The path is read-only and never creates a submission, retry receipt, communication effect or session-activity write.

## Verification trace

The initial tests-first RED was compilation on absent T3 production types. The first implementation compile exposed incorrect owner-reference accessor names and an attempt to call a package-private identity helper; mapping was corrected to use the public exact-candidate factory and existing accessors. Eight initial targeted tests passed. An ambiguous static import in the new integration fixture was corrected before full verification. These are compile/fixture corrections, not claimed pre-existing runtime defects.

Local full verification passed **1079 tests, zero failures/errors/skips**, on Java 26 targeting Java 25.

Ten unit/composition tests verify credential-to-selected-response composition, exact original revision, omission of internal evidence, contact withholding and contact-only selection, missing/invalid/revoked credentials, current merchant association before reads, revocation during acquisition, current family privilege revocation, foreign/absent/all-withheld equivalence, rejected client parameters and duplicate credentials, real P2 missing-progress rejection, absent optional families, safe unexpected failures, cross-resource/read/request mapping rejection, no adapter reacquisition, and actual Spring profile wiring with mandatory privilege mappings.

Four PostgreSQL tests use real JooqEnquirySubmissionStore and JooqSessionRecordStore with the complete query path. They prove exact original material and unchanged submission/session/receipt stores, persisted session revocation blocking reuse of the credential, contact-family withholding preserving other selected values, and safe foreign-scope/absent behavior plus lost merchant association. Configuration, principal resolution, security-generation, membership and privilege providers are deterministic fixtures; durable record lookup, session resolution/currentness, admission, P2, M1 Exposure and selected response mapping execute through production boundaries.

Implementation commit: `26674121a673015ea9155ad5222dd7eaddbb2932`.

Full Java 25/PostgreSQL CI run `33993070421`: SUCCESS — 1079 unit/governance + 370 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Structure, limits and closure

Generic Surface orchestration stays owner-neutral; Enquiry owns the logical API, serviceability, family privilege mapping and DTO conversion. Existing M1/M2, session/runtime, E1/E2 and PUBLIC adapters remain unchanged. No migration, semantic amendment, role grant, inbox/workflow or generic transport framework was introduced.

Deployment must explicitly provide trusted workspace routes, active configuration/registry, durable submission/session providers, scoped principal resolution, current session security generation, merchant association, platform protection, actor authorization and all family privileges. The profile is implemented and tested, not deployed. This is the initial interactive merchant observation path; it does not assert staff-device execution trust.

Optional current subject, known CustomerContext, communication and attention material remain outside the minimum M2/T3 read. No placeholders or owner authority are invented for them.

Evidence, graph, status and programme gate close together on successful synchronization CI. T3 then completes the required adapters and makes V1 READY for its integrated Publication → Enquiry proof. T3 does not itself close V1 or IMP-07; P6 actionability remains separately dependency-blocked.
