# MS-PROT-085 — Initial Enquiry Review Commercial Access Amendment

**Document ID:** MS-PROT-085
**Version:** 1.3
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete proposal and limited formalisation scope
**Authority type:** Merchant Attention commercial-access classification
**Governed by:** DESIGN-RULES v2.3; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-085 v1.1 §§6, 8–9 within commercial-access classification only
**Depends on:** MS-PROT-085 v1.0 §§11, 14, 24–25, 29–34; MS-PROT-085 v1.1; MS-PROT-043 v1.5 §§6–9; MS-PROT-056 v1.7 §10 and v1.9 §§4–6; composite MS-PROT-062 and MS-PROT-065
**Implementation activation:** NONE
**Purpose:** Resolve the commercial access requirements of initial Enquiry review without introducing another paid prerequisite or weakening source and handler authority.

## 1. Governing decision

For exactly `enquiry/initial-submission-review@1`, Commercial Entitlement requirements SHALL be:

| Activity | Commercial requirement |
|---|---|
| Validated post-commit contribution, occurrence establishment and recovery | No Commercial Entitlement required for this progression |
| Merchant observation of the occurrence and its handling representation | `OBSERVE_ENQUIRY` through `enquiry/merchant-observation-access@1` |
| Explicit acknowledgement | The same Enquiry observation requirement |
| `RecordInitialEnquiryReview` | The same Enquiry observation requirement |

No independent Attention entitlement SHALL be required for these activities within this exact family.

These classifications do not grant actor privileges, establish semantic applicability, activate the family or bypass source-data access.

## 2. Scope and unchanged meaning

This amendment supplies the commercial classification left unspecified by “applicable Commercial Entitlement” in MS-PROT-085 v1.1 §8.

It does not change:

- the captured Attention family or version;
- occurrence identity;
- activation and no-backfill rules;
- acknowledgement meaning;
- `INITIAL_REVIEW_RECORDED` meaning;
- Controller-only observation and handling;
- current source serviceability and Exposure requirements;
- handling concurrency or idempotency.

It does not admit staff handling, assignment, snooze, response obligations, Notification responsibilities or Candidate Actions.

`customer-communication/human-response-required@1` and every other Attention family remain outside this amendment.

## 3. Ownership and contract qualification

Merchant Attention owns occurrence establishment, acknowledgement, handling facts and their projections.

Enquiry owns the underlying submission and the `enquiry/merchant-observation-access@1` contract.

Commercial owns the binding and grant evaluation satisfying `OBSERVE_ENQUIRY`.

The Enquiry observation requirement SHALL be evaluated for the same trusted Merchant Scope and exact source Enquiry referenced by the Attention occurrence.

The requirement SHALL NOT be substituted with:

- an Enquiry origination grant;
- a tier-name comparison;
- former paid membership;
- possession of an occurrence identifier;
- an unrelated source-observation grant.

No independent Attention entitlement means no additional commercial prerequisite. It does not mean unrestricted access.

## 4. Post-commit establishment and recovery

The exemption in Section 1 applies only to progression from an authoritative qualifying Enquiry acceptance under MS-PROT-085 v1.1 §§5–6.

Establishment and recovery SHALL still validate:

- exact source owner and Merchant Scope;
- authoritative Enquiry acceptance evidence;
- the initial-review activation boundary covering that acceptance;
- captured contract identity and supported execution;
- stable occurrence identity;
- required current protection and data-use authority;
- bounded trusted execution context.

A submitted event, client identifier or claimed historical grant SHALL NOT substitute for that evidence.

Current entitlement to originate another Enquiry SHALL NOT be required merely to finish the qualifying acceptance’s durable Attention progression.

The exemption SHALL NOT permit creation of another Enquiry, another handling episode, a Message, Notification or business commitment.

No merchant-facing observation permission follows from successful establishment.

## 5. Merchant observation

Observation SHALL require current satisfaction of `OBSERVE_ENQUIRY` through the exact Enquiry access contract, together with MS-PROT-085 v1.1 §8’s Controller, source-access, serviceability and Exposure requirements.

This requirement applies to representations of outstanding and historically handled initial-review occurrences.

An occurrence’s existence SHALL NOT disclose protected source existence or detail when observation is prohibited.

Coverage SHALL remain governed by MS-PROT-085 v1.0 §25 and v1.1 §11. Access denial or unavailable evaluation SHALL NOT become a claim that no Enquiries exist or all customer work is handled.

## 6. Acknowledgement and recording review

Acknowledgement and `RecordInitialEnquiryReview` SHALL require:

1. an explicit eligible actor instruction;
2. current Controller and Actor Authorisation;
3. current Enquiry observation permission;
4. the source-data, serviceability and Exposure checks required by MS-PROT-085 v1.1 §8;
5. exact occurrence and operation affinity;
6. the applicable current-revision check.

The commercial observation grant alone SHALL NOT authorise either mutation.

Acknowledgement remains evidence of awareness under MS-PROT-085 v1.0 §14. It does not satisfy initial review.

`RecordInitialEnquiryReview` remains governed by MS-PROT-085 v1.1 §9. It records review, not a response or resolution.

Opening a page, reading a Notification or obtaining an AI summary SHALL NOT perform either operation.

## 7. Tier placement and supporting access

This initial-review workflow is supporting access for the FREE Enquiry portfolio under MS-PROT-056 v1.7 §10.

BUSINESS and GROWTH SHALL NOT become additional commercial prerequisites for its observation or handling.

The complete catalogue SHALL identify this supporting relationship and resolve the Enquiry observation binding before claiming the path is commercially admitted.

Missing Enquiry binding information SHALL NOT be interpreted as satisfaction of the requirement.

This amendment supplies no Commercial Entitlement identity or plan grant and publishes no catalogue generation.

## 8. Entitlement changes and historical preservation

Current observation and handling SHALL use independently valid grant sources under Commercial authority.

Loss of a paid grant SHALL NOT remove permission still supplied by FREE, trial or another valid source.

If the required Enquiry observation permission is not satisfied:

- new merchant observation and handling SHALL be denied;
- existing Enquiries, occurrences and handling facts SHALL NOT be deleted or rewritten;
- lack of permission SHALL NOT establish review, resolution or `NO_LONGER_APPLICABLE`;
- qualified post-commit progression may continue under Section 4 without granting observation.

This amendment creates no residual commitment-resolution classification. Occurrence existence does not itself establish permanent commercial permission.

Historical facts retain their original meaning. Current access remains a separate determination.

## 9. Failure, concurrency and retries

Occurrence establishment SHALL preserve the uniqueness and convergence rules in MS-PROT-085 v1.1 §6.

Acknowledgement SHALL preserve MS-PROT-085 v1.0 §31’s logical-operation affinity and concurrency requirements. Recording review SHALL preserve MS-PROT-085 v1.1 §9.

An authorised retry of the same logical operation SHALL converge on its existing result. Retry SHALL NOT duplicate or erase handling facts.

Recovery or result retrieval SHALL NOT bypass current disclosure and actor requirements.

Commercial denial, stale handler authority, source unavailability, contract failure and handling conflict SHALL remain distinguishable. Technical inability to establish a commercial decision SHALL NOT be represented as an authoritative absence of entitlement.

Attention failure SHALL NOT roll back Enquiry acceptance. No distributed transaction spanning Enquiry, Commercial and Attention is introduced.

## 10. Provider, AI and activation boundaries

No provider call or live AI service is required by this commercial classification.

Notification delivery remains outside the initial-review contract. Provider success or failure does not record acknowledgement or review.

AI SHALL NOT supply missing authority, perform handling without an authorised instruction or extend this exemption to another family.

Commercial permission SHALL NOT activate initial review. The accepted configuration and activation requirements remain mandatory.

## 11. Conformance obligations

Downstream implementation SHALL demonstrate:

- exact family qualification;
- correct Enquiry observation-purpose evaluation;
- no independent Attention paywall;
- no origination-grant substitution;
- current Controller and source-access enforcement;
- safe progression after commercial changes;
- no protected disclosure through recovery;
- idempotent establishment and handling;
- preserved activation boundaries and honest coverage.

These obligations are not satisfied merely by accepting this amendment.

## 12. Closure and amendment effect

This amendment resolves the commercial classification of progression, observation, acknowledgement and recording review for exactly `enquiry/initial-submission-review@1`.

It does not reopen `MS-PROT-085-DQ-001`, whose initial-family selection remains resolved.

`MS-PROT-056-V17-DQ-001` remains OPEN. Exact Enquiry entitlement bindings, other supporting classifications and the complete three-tier manifest remain outstanding.

No production activation, C3 completion or implementation-node promotion is authorised.

## 13. Review and falsification

**Vision result: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.** A sole trader can review a received question without buying another component. Security, provenance and recovery complexity remain internal; no additional merchant configuration is introduced.

| Challenge | Required outcome |
|---|---|
| Enquiry commits; commercial state changes before Attention recovery | Qualified progression continues; observation is evaluated separately |
| Caller fabricates an Enquiry acceptance | Source validation rejects establishment |
| Merchant has origination permission but lacks observation permission | Observation and handling denied |
| FREE permission remains after paid cancellation | Initial review does not acquire a paid prerequisite |
| Controller authority ends while the page remains open | Handling rejected |
| Merchant opens the occurrence accidentally | No acknowledgement or review recorded |
| Two devices submit the same review | Existing idempotency and concurrency rules prevent duplicate effects |
| Notification provider is unavailable | In-product review remains independent |
| Another Attention family attempts to reuse the exemption | Rejected as outside the exact scope |

**Alternatives rejected:** a separate FREE Attention entitlement, which duplicates the supporting-access prerequisite; completely ungated merchant handling, which bypasses Enquiry access; and rechecking origination entitlement during recovery, which conflates a committed submission with new activity.

**Ambiguity review:** commercial exemption is confined to durable progression; merchant access depends on an exact source contract; mutation authority remains independent. No broader Attention entitlement policy is inferred.

Evidence consists of accepted-authority inspection and these design scenarios. No implementation or performance proof is claimed.
