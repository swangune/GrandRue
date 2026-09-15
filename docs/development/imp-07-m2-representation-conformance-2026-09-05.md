# IMP-07 M2 Conformance — Request-scoped Merchant Enquiry Representation

**Date:** 5 September 2026

**Node:** IMP-07-M2 — Request-scoped merchant Enquiry representation

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

This is implementation evidence only. It creates no semantic authority.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern selection and closure. Composite MS-PROT-043, particularly v1.4 §§22–25, governs the initial request-scoped merchant read, separate submission-time provenance and current subject facts, and the four MERCHANT Exposure families. Composite Projection/Exposure authority, including MS-PROT-027, governs serviceability, exact observation affinity and positive membership.

M1 closure `8e9fc480f07acaf1b6c83c55cd4a9f7bc1bbbc13`, run `33986872166`, passed 1025 unit/governance + 353 PostgreSQL integration tests, zero failures/errors/skips. G0 and E1 are complete.

## Implementation and structure

Enquiry owns three separate immutable representation values: submission content (time, question and submission semantic context), supplied contact, and submission-time subject revision provenance. No permitted content or subject fragment contains a complete EnquirySubmission or a withheld contact value.

The authority-backed read port acquires one merchant-qualified immutable E1 submission, verifies returned scope/identity, and derives both material and source evidence from that read. Existing append-only Enquiry identity is stable source progress within its merchant-scoped read. Missing records produce no fragments and unavailable/missing source evidence. Optional contact and subject fragments exist only when their authoritative values exist.

The observation binds the exact source evidence and immutable fragments to one established request and rejects a different Merchant Scope. Each fragment carries its exact M1 family and Enquiry instance identity.

Generic MerchantProjectionAssemblyService requires MERCHANT_OPERATIONAL API Exposure and reuses BoundedProjectionFragmentSelector. The selector requires exact P2 bounded-read affinity, matching request/release Exposure, serviceable source material and exact positive membership. It returns the original selected fragment objects. Assembly performs no owner read, policy re-evaluation, material refresh or transport conversion. Denied contact cannot leak through a separately permitted family.

Original subject provenance remains explicitly named submissionTimeSubject. This implementation does not acquire current subject data and cannot replace original provenance with it. Optional known CustomerContext, communication and attention material remain outside this minimum read; no authority source or placeholder is invented. No new schema, SQL, inbox database, workflow, privilege grant or semantic amendment was added.

## Verification trace

The first focused test compilation failed on absent M2 types and an incorrect test API enum spelling. The first production compile also identified that spelling; both were corrected to the existing MERCHANT_OPERATIONAL enum. These compile failures are recorded as implementation/fixture errors, not evidence of a pre-existing production behavior defect. Nine focused tests then passed. A tenth test covers positive membership for another Enquiry under the same request.

Local full verification passed **1035 tests, zero failures/errors/skips** using Java 26 and the Java 25 target.

Ten unit/composition tests prove separated immutable values, denied contact removal, original fragment identity/no assembly reload, authorization revocation between read and Exposure, missing/nonserviceable reads, positive Exposure unable to rescue a nonserviceable read, different P2 read/request rejection, merchant isolation, absent optional material, non-merchant API rejection, and cross-Enquiry membership rejection.

Two PostgreSQL tests compose the real E1 store/read port with M1 authorization/Exposure and generic assembly. They prove contact exclusion and current revocation without changing durable submission evidence, exact original revision provenance, missing optional material and foreign-merchant absence.

Implementation commit: `7ec5667f845b1e098a44da5e2b9b21c6d6674e2a`.

Full Java 25/PostgreSQL CI run `33987788979`: SUCCESS — 1035 unit/governance + 355 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Limits and closure

The tests compose the real owner read, current M1 requirement evaluator, audience admission, Exposure resolver and generic selector. P2 outcomes at the established serviceability seam, active configuration, principal currentness and privilege identifiers are deterministic fixtures. This is not evidence of a deployed serviceability/privilege portfolio or complete transport wiring. Concrete adapters must establish trusted request context and supply current authorities; internal material acquisition itself grants no observation permission.

Evidence, graph, status and programme gate close together only on successful synchronization CI. All core V1 owner prerequisites are now complete; the original graph also requires adapters. Select T1 next for the mandatory vertical path, followed by required T2/T3 adapters and V1 integration. IMP-07 remains IN_PROGRESS.
