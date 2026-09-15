# IMP-07 I1 Conformance — Opportunity Enquiry Participation Source

**Date:** 5 September 2026  
**Node:** IMP-07-I1 — Owner-qualified Opportunity → enquiry/send-enquiry participation source  
**Macro:** IMP-07 — Publication → Enquiry vertical slice  
**Branch:** development  
**Status:** CONFORMING_COMPLETE effective when the cycle-closing synchronization head passes full verification

This is implementation evidence, not semantic or programme authority.

## Authority and prerequisites

- MS-IMP-001 v1.0, preserved within IMP-07 by v1.1; IMPLEMENTATION-RULES v1.6 §§7–10, 46–48, 52.14, 53.
- MS-PROT-046 v1.2 §§16–18 establishes the explicit initial Opportunity → enquiry/send-enquiry portfolio; §§19–20 preserve presentation and execution revalidation boundaries.
- MS-PROT-049 v1.4 §§3–5, 9, 13, 15 establishes owner-qualified release-affined definitions, scoped participation facts, source registration and separation from availability/execution.
- Composite MS-PROT-027 and completed IMP-06 S3 retain Projection/Exposure and generic binding composition ownership.
- P5 closure head `8bb87588b1e013f46e15d2be4c681a0b313099cf`, run `33960381639`, passed 977 unit/governance and 329 PostgreSQL integration tests. This satisfied I1's P5/G0 prerequisites.

## Implementation and boundaries

`OpportunityEnquiryParticipationDefinition` explicitly registers only the Opportunity family and enquiry/send-enquiry relationship. Its factory verifies publication/opportunity and the Enquiry-owned send-enquiry operation in one semantic registry release. Fixed owner-qualified source/contribution identifiers represent accepted semantics; there is no universal participation rules engine.

`OpportunityEnquiryParticipationSource` implements the existing source contract for an immutable bounded set of Opportunity identity hints and one captured resolved merchant model. It checks exact request/model affinity, applicable capability/object/operation semantics and contribution identity, audience and operation. It reads Publication current state only to establish subject existence and exact owner/scope/identity; it does not acquire public material, revisions or deadlines.

The family relationship is independent of DRAFT/PUBLISHED/WITHDRAWN, Exposure windows and Opportunity actionability. Participation grants neither visibility nor submission permission. Generic S3 must intersect it with serviceable Exposure-selected material. Runtime stale-binding revalidation remains E3; a withdrawn subject is never grandfathered for execution by I1.

Registration uses `PublicInteractionParticipationSourceRegistration`; the empty generic registry remains valid. No generic Surface production code changed. No schema, migration, API, Enquiry mutation, provider, configuration activation or dependency was introduced.

## Executable proof

`OpportunityEnquiryParticipationSourceTest` contains 11 tests covering:

- exact positive merchant/release/model/contribution/operation/subject/source fact and registration;
- missing instances and non-Opportunity identifiers;
- stable family participation separate from lifecycle, Exposure and actionability;
- inactive capabilities or absent resolved operation;
- registered Opportunity family absent from the resolved model;
- wrong contribution owner/name, unsupported operation and non-PUBLIC audience;
- wrong merchant/release/model/version rejected before owner reads;
- definition/model release mismatch;
- owner response with wrong merchant or subject;
- missing registered family or wrong/missing operation owner;
- immutable bounds/results and repeated read-only evaluation.

## Verification trace

- Local P5 baseline: 3 tests PASS.
- Initial I1 RED: compilation failed because the two production classes were absent.
- Initial GREEN attempt: 9 passed, 1 fixture error; WITHDRAWN incorrectly lacked historical published-revision evidence. Fixture corrected without changing the asserted contract.
- Adversarial resolved-family RED: 11 tests, exactly 1 failure (`expected true but was false`) for an existing Opportunity under a model with no applicable Opportunity family.
- Guard added; local full suite: **988 tests, zero failures/errors/skips**. Local JVM: Java 26 with the repository's Java 25 compilation target.
- Code-bearing commit: `1065904c9e3cabc3a20d2ba7be9296c8e6f7c5b8`.
- GitHub Actions run `33981525405`: SUCCESS — 988 unit/governance + 329 PostgreSQL integration tests, zero failures/errors/skips.
- Authoritative full gate: `mvn --batch-mode clean verify -Ppostgres-it` on Java 25 and PostgreSQL 18. Local Docker daemon was unavailable; no local PostgreSQL result is claimed.

## Review and closure

Paradigm fit: Publication owns the family relationship; registered semantics supply release affinity; ordinary typed Java performs the bounded read/identity transformation. Existing inward-facing Publication authority is reused. No transaction, event or asynchronous path is added to this read-only source. Generic S3 remains the sole binding composition boundary.

No accepted test was weakened and no material design decision was required. I2 is next after graph, status and programme-gate synchronization passes the full gate. I1 completion does not claim concrete binding integration or the full Publication → Enquiry slice.
