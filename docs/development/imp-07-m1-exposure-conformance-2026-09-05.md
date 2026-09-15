# IMP-07 M1 Conformance — MERCHANT Enquiry Exposure

**Date:** 5 September 2026

**Node:** IMP-07-M1 — MERCHANT Enquiry Exposure

**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

This is implementation evidence only. It creates no semantic authority.

## Authority and prerequisite

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern selection and closure. MS-PROT-043 v1.4 §§22–25 names the four initial MERCHANT Enquiry Exposure families, requires legitimate merchant observation and applicable Actor Authorisation, preserves submission/current subject distinctions and grants no PUBLIC or automatic CUSTOMER history access. Composite MS-PROT-027 governs instance-qualified positive Exposure membership, owner requirements and separation from representation. MS-PROT-062/063/074 govern current merchant-scoped Actor Authorisation.

M1 dependencies E1 + G0 were complete. E3 closure `cf276ec7142072ed332f59354da672894ccf7a79`, run `33985515264`, passed 1015 unit/governance + 351 PostgreSQL integration tests, zero failures/errors/skips.

## Implementation

The code-owned `EnquiryMerchantExposureContractPortfolio` registers exactly:

- enquiry / merchant-submission-content
- enquiry / merchant-submitted-contact
- enquiry / merchant-subject-context
- enquiry / merchant-communication-summary

Every family is MERCHANT-only and instance-qualified by enquiry/enquiry. Baseline EXPOSE remains conditional on the mandatory owner `merchant-enquiry-observation` requirement; there is no merchant-choice override, PUBLIC contract or CUSTOMER contract.

`EnquiryMerchantExposureCandidateSource` reads E1 facts through the merchant-scoped owner store and checks returned scope/identity affinity. It produces identity-only candidates for submission content, present supplied contact and present subject-revision context. Missing records produce no candidate. No submission text, contact value or subject snapshot crosses into the generic Exposure evaluator.

`EnquiryMerchantExposureRequirementEvaluator` requires explicit privilege bindings for all four exact families at construction. It checks current scoped Actor Authorisation for each family before reading Enquiry state, then verifies current owner candidacy. Denial or absent material withholds membership; authority failure is UNRESOLVED and also withholds membership. An invocation-local cache avoids repeated reads of one immutable Enquiry but never caches authorization across requests.

The existing audience admission evaluator remains responsible for current authentication and merchant association. Admission alone does not satisfy the family privilege requirement. Required privilege identifiers are supplied explicitly by owner composition; M1 creates no built-in universal read privilege, role grant or permissive fallback.

The communication-summary family is registered, but E1 owns no communication facts. Its material candidate is unavailable and forged attempts remain withheld. M1 does not invent communication history or empty successful placeholders.

## Verification trace

The first focused test compilation failed on absent M1 types. All ten focused tests passed after implementation. Local full verification passed **1025 tests, zero failures/errors/skips** using Java 26 and the Java 25 target.

Ten M1 unit/composition tests cover exact portfolio registration, available owner candidates, positive merchant exposure, current privilege revocation before store reads, independent family privilege decisions, missing/failed authorization binding, PUBLIC/CUSTOMER denial, forged/missing/cross-merchant/unavailable candidates, expired authentication/lost merchant association, and missing evaluator fail-closed behavior. They compose the real audience admission evaluator and generic Exposure resolver with deterministic authority fixtures.

Two PostgreSQL tests use the real E1 store, candidate source, requirement evaluator and generic resolver. They prove durable positive membership followed by current privilege revocation without changing submission evidence, and denial of a stored foreign Enquiry even when the actor is otherwise authorized in the requested merchant scope.

Implementation commit: `8fe221d9acbbbd4e772be9eee5896f9058ffa4e7`.

Full Java 25/PostgreSQL CI run `33986535598`: SUCCESS — 1025 unit/governance + 353 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

## Structural review, limits and closure

Enquiry owns the references, portfolio, material candidacy and requirement evaluator. SQL remains in the existing E1 infrastructure adapter. Generic admission/Exposure and ActorAuthorisationAuthority remain unchanged; no metadata DSL, new persistence, privilege grant, workflow state, identity reconciliation, communication side effect or semantic authority amendment was added.

This establishes Exposure membership, not M2 merchant representation, an HTTP endpoint, a deployed privilege-binding portfolio or communication material availability. Test privilege names and currentness/membership decisions are fixtures. Production owner composition must supply its applicable privilege mapping and current authorities; the implementation rejects missing mappings. M2 must select permitted material under the same observation and preserve original submission provenance separately from current subject data.

Evidence, graph, status and programme gate close together only on successful synchronization CI. M2 becomes READY on that gate. IMP-07 remains IN_PROGRESS.
