# IMP-07 E3 Conformance — Authoritative Opportunity Enquiry Revalidation

**Date:** 5 September 2026

**Node:** IMP-07-E3 — Stale subject-binding authoritative revalidation

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

This is implementation evidence only. It creates no semantic authority.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern this cycle. MS-PROT-043 v1.4 §§2–7, 10–16 requires current merchant/interaction/subject revalidation, fail-closed stale selection, retained subject meaning and immutable provenance. Composite MS-PROT-049 v1.2/v1.4 distinguishes untrusted binding locators from owner participation and execution authority. MS-PROT-046 v1.2 and P4 authority govern current published Opportunity visibility separately from participation and actionability. MS-PROT-025/034 govern the transaction and SQL-explicit implementation. ActiveRelease retains its existing invocation-captured immutable configuration semantics.

I2 + E1 dependencies were complete. E2 closure `d25961238b99af5a1edf893495fceeaec41760a1`, run `33984462969`, passed 1004 unit/governance + 346 PostgreSQL integration tests, zero failures/errors/skips. Its transaction/preparation seam is used without adding a semantic dependency.

## Implementation

`OpportunityEnquirySubmissionPreparation` implements the E2 preparation seam for merchant-general Enquiry and the accepted initial Publication Opportunity path:

- Compare supplied merchant with the scope established by the trusted application boundary.
- Capture the current active release and require the serving semantic registry, active Enquiry capability, registered Enquiry-owned operation and exact current PUBLIC_INTERACTION contribution.
- Fail closed for additional privilege/provider/customer surface gates this bounded implementation cannot resolve.
- For a subject, require publication/opportunity identity, current PUBLISHED lifecycle and exact selected published revision. An unpublished material revision may coexist with the still-published selected revision.
- Require the explicit I1 participation definition and derive fresh participation through the real Publication-owned source. Exposure does not manufacture participation.
- Re-read exact published material and evaluate the P4 Publication window. Missing, forged, draft, withdrawn, changed published revision, absent source or out-of-window subjects fail without becoming general Enquiries.
- Invoke the required Enquiry-owned remaining-requirements preparation, reject changes to supplied intent, and recheck current published affinity/window afterward.
- Record the actual validation time and captured authoritative registry/model provenance, rather than accepting those from delegated preparation.

`OpportunityPublicationSubmissionLock` is a narrow Publication-owned read/consistency port. The existing jOOQ authority implements it with a merchant-qualified `SELECT FOR SHARE` and rejects use outside an active transaction. It holds the current row against revision/lifecycle updates until E2 commits or rolls back. Enquiry gains no Publication mutation authority.

`OpportunityPublicExposureWindow` extracts P4's existing lower-inclusive/upper-exclusive exact-instant and inclusive-calendar-date rule without changing it. Both the P4 evaluator and E3 reuse it. E3 is explicitly bound to the current P4 portfolio: PUBLIC representation defaults to EXPOSE, has no merchant-choice requirement and requires the Publication window. It does not reinterpret an arbitrary Exposure portfolio or implement P6 actionability.

## Verification trace

The first focused compilation failed on absent E3 types. The initial nine revalidation tests then passed. Additional tests covered unsupported semantic release, late expiry and unresolved contextual privilege requirements.

Local full verification passed **1015 tests, zero failures/errors/skips** using Java 26 and the Java 25 target, including existing P4/P5/I1/I2 regressions. Eleven E3 unit tests cover exact provenance, general subject absence, scope/current release, missing interaction/source, forged owner/type/revision, missing/draft/withdrawn/republished subjects, temporal boundaries, owner affinity, delegated requirements, silent downgrade, late expiry and unsupported contextual gates.

Five real PostgreSQL tests compose the E3 preparation, E2 application service and Publication authority:

1. Exact subject submission commits and retries return the original result after later withdrawal without new preparation.
2. Withdrawal before a new submission rejects with neither Enquiry nor receipt.
3. Changing the published revision rejects the old selection; an independent new general Enquiry remains possible.
4. Lock acquisition outside a transaction is rejected.
5. A concurrent withdrawal hits the expected PostgreSQL lock timeout (SQLSTATE 55P03) while validated submission holds the row. Submission commits exact R7 evidence; withdrawal then succeeds. Latches and a bounded database timeout prove contention without test sleeps.

Implementation commit: `b39f4db5dbfff4ed3472d3d47f659d4f436079e7`.

Full Java 25/PostgreSQL CI run `33985246923`: SUCCESS — 1015 unit/governance + 351 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Scope and conformance limits

This is internal subject/interaction revalidation, not public transport or complete deployment wiring. Trusted merchant/access establishment remains the application adapter's responsibility, including access to committed retries before invoking E2. A retry key is not authority. The required remaining-requirements delegate retains Enquiry contextual input, trusted CustomerContext association and identity-generation responsibilities; there is no permissive default, invented universal contact schema or contact-based customer inference.

Unit tests use deterministic active configuration and Publication seam fixtures; PostgreSQL tests use real Publication, lock and Enquiry transactions with the same active-configuration/remaining-requirements fixtures. They do not claim a deployed production requirements portfolio, authentication endpoint or arbitrary subject/provider support.

The configuration is captured from current authority per invocation and remains pinned as ActiveRelease specifies. Temporal eligibility is evaluated at recorded submission time; no future-time guarantee is implied. No subject snapshot, workflow state, notification, Conversation, downstream commitment, semantic amendment or database migration was added.

This evidence, graph, status and programme gate close together only on successful synchronization CI. Select M1 next for MERCHANT Enquiry Exposure. T2 remains an on-demand adapter after E2/E3; IMP-07 remains IN_PROGRESS.
