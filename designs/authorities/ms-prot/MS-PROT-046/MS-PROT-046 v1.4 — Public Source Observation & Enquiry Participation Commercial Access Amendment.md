# MS-PROT-046 — Public Source Observation & Enquiry Participation Commercial Access Amendment

**Document ID:** MS-PROT-046
**Version:** 1.4
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete proposal and limited formalisation scope
**Authority type:** Publication-owned commercial-access classification
**Governed by:** DESIGN-RULES v2.3; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-046 v1.2 §§13–20 within commercial-access classification only
**Depends on:** Composite MS-PROT-046 through v1.3; MS-PROT-043 v1.5; MS-PROT-049 v1.2 and v1.4; composite MS-PROT-027 and MS-PROT-062; MS-PROT-056 v1.7 §§5, 10 and v1.9 §§4–6
**Implementation activation:** NONE
**Purpose:** Resolve the commercial classification of bounded public Publication reads and Opportunity-to-Enquiry participation without confusing observation, participation, delivery and execution.

## 1. Governing decision

Publication SHALL define these exact access contracts:

| Access contract | Commercial classification |
|---|---|
| `publication/public-source-observation-access@1` | No Commercial Entitlement required for the bounded source observation defined in Section 3 |
| `publication/opportunity-enquiry-participation-access@1` | No independent Commercial Entitlement required for the participation evaluation defined in Section 4 |

Neither contract grants permission to execute Enquiry submission or any Publication mutation.

These contracts classify existing source responsibilities. They introduce no new public endpoint, business object, publication lifecycle or delivery service.

## 2. Identity, ownership and scope

Each access contract is identified by its exact owner, contract identifier and revision. Its meaning SHALL remain immutable.

An access-contract identity is not an Exposure contract, Public Interaction Binding, entitlement identity or transport endpoint.

Publication retains ownership of publication truth and its Opportunity participation definition. Surface owns binding projection, not participation truth. Enquiry owns submission. Commercial owns independently required grants.

A later access-contract revision, additional Exposure family or new subject-interaction relationship SHALL NOT acquire this classification merely by sharing a namespace.

## 3. Bounded public source observation

`publication/public-source-observation-access@1` applies only to existing PUBLIC source reads supplying these four MS-PROT-046 v1.2 §13 families:

```text
publication/public-published-content-representation
publication/public-opportunity-representation
publication/public-opportunity-actionability
publication/public-announcement-representation
```

Observation SHALL continue to require:

- exact Merchant Scope and source identity;
- legitimate Publication lifecycle state;
- the authoritative published revision;
- applicable Publication-owned exposure-window conditions;
- current Projection Serviceability;
- current PUBLIC Exposure;
- applicable data-use and Resource Protection requirements.

The read SHALL preserve the exact semantic-release and observation affinity required by composite MS-PROT-027.

No commercial exemption SHALL expose a draft, withdrawn object, unpublished revision, private source detail, administrative metadata or unrestricted historical revision.

This contract does not establish a general browse, search, export or bulk-extraction API.

## 4. Opportunity-to-Enquiry participation

`publication/opportunity-enquiry-participation-access@1` applies only to the accepted relationship:

```text
Publication-owned Opportunity
    → enquiry/send-enquiry
```

The relationship remains governed by MS-PROT-046 v1.2 §§16–20 and MS-PROT-049 v1.4.

Publication’s participation evaluation SHALL establish its result from the accepted owner-qualified definition and exact merchant, subject and semantic context.

The absence of an independent Publication entitlement SHALL NOT:

- manufacture participation;
- substitute Exposure for participation;
- permit ambiguous source ownership;
- turn every PublishedContent or Announcement into an Enquiry subject;
- allow cross-merchant or incompatible cross-release reuse.

The generic binding projector SHALL continue to compose only the intersection of legitimate interaction contribution, owner-established participation and serviceable publicly exposed material.

A positive participation result remains distinct from current operation availability and execution permission.

## 5. Enquiry submission remains protected

A projected Opportunity-to-Enquiry binding SHALL NOT satisfy `ORIGINATE_ENQUIRY`.

Submission SHALL independently satisfy:

```text
enquiry/opportunity-submission-access@1
    +
ORIGINATE_ENQUIRY
```

through its accepted Commercial Access Binding, together with the remaining authoritative checks in MS-PROT-043 v1.5 §5.

Backend submission SHALL revalidate current participation and source eligibility. A previously rendered binding creates no grandfathered right.

The general-Enquiry path SHALL NOT be substituted by discarding a stale or otherwise invalid Opportunity subject.

This amendment does not determine additional public interaction-availability presentation states.

## 6. Observation and actionability remain distinct

Commercial classification SHALL NOT collapse:

```text
Publication lifecycle
public Exposure
Opportunity actionability
subject-interaction participation
Enquiry execution permission
```

A publicly observable Opportunity may have an actionability outcome that does not permit its underlying application or commitment.

Displaying that outcome does not change it.

Enquiry participation SHALL follow its accepted owning contract rather than assuming every underlying Opportunity actionability condition is also an Enquiry condition.

An external application link remains a link; it does not become Enquiry execution or Main Street commitment authority.

## 7. Delivery and administration remain separate

These exemptions apply to the Publication source responsibilities specified above.

They SHALL NOT grant:

- merchant website hosting or delivery;
- a platform-provided address or custom domain;
- Publication creation, revision, publishing, withdrawal or republication;
- social distribution;
- Notification delivery;
- subscriber enrolment or subscription management;
- AI content generation.

The applicable owner and commercial contracts continue to govern those services.

This amendment creates no alternate route around a delivery service’s controls. A consuming surface must independently satisfy its own accepted requirements.

The FREE allocation of Publication under MS-PROT-056 v1.7 remains unchanged. That allocation does not require a separate entitlement for every internal source-read dependency.

## 8. Read, retry and failure semantics

These contracts introduce no authoritative mutation, durable commercial permission token or domain event.

Public reads and binding derivations remain request-scoped under MS-PROT-046 v1.2 §15 and MS-PROT-049 v1.4.

A repeated read may return a different result when authoritative source state or current access changes. It SHALL NOT reuse an earlier positive observation as current authority.

Missing participation, withheld Exposure, unavailable source evidence and structural inconsistency SHALL retain the distinctions required by MS-PROT-049 v1.4 §10.

Technical failure SHALL NOT become fabricated content, participation, commercial denial or successful Enquiry submission.

No cross-capability transaction is introduced.

## 9. History, security and AI boundaries

Retained publication history does not become PUBLIC merely because the current source-read contract requires no Commercial Entitlement.

Current source and Exposure requirements remain mandatory. This amendment establishes no historical-access or residual commitment-resolution classification.

Public observation grants no merchant administration privilege, CustomerAccount access or Enquiry-history access.

AI SHALL NOT create participation, select an unpublished revision as public, resolve missing authority or bypass the target operation’s commercial checks.

No live AI service or new provider responsibility is introduced by these classifications.

## 10. Catalogue dependency consequences

A catalogue binding that consumes these exact source responsibilities SHALL record their explicit commercial classification and governing authority.

It SHALL NOT invent separate Publication source-read or participation entitlement requirements for the scope covered here.

Independently required Enquiry, delivery or administrative bindings must still resolve. Their absence SHALL NOT be treated as exemption.

This amendment does not establish Commercial Entitlement identities, plan revisions or a complete manifest.

## 11. Conformance obligations

Downstream implementation SHALL demonstrate:

- exact access-contract scope;
- preservation of the four PUBLIC Exposure boundaries;
- no unpublished-revision or historical-data leakage;
- no invented participation;
- exact merchant and semantic-context affinity;
- current revalidation after withdrawal;
- independent Enquiry commercial enforcement;
- no delivery or administration bypass;
- truthful handling of unavailable and contradictory evidence.

Acceptance is not proof that existing implementation satisfies these obligations.

## 12. Closure and amendment effect

This amendment resolves only the commercial classification of the two Publication source responsibilities defined here.

Publication administration, website delivery, general merchant interaction support and other catalogue dependencies remain separate work.

`MS-PROT-056-V17-DQ-001` remains OPEN. Existing Publication deferred decisions are not closed or promoted by this amendment.

No production catalogue, implementation activation or C3 completion is authorised.

## 13. Review and falsification

**Vision result: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.** Customers can read legitimately public information without unnecessary commercial machinery. Internal boundaries prevent that simplicity from becoming unrestricted execution or data access.

| Challenge | Required outcome |
|---|---|
| Information publisher has no transactional capabilities | Public Publication reads require no fabricated Booking or Order |
| Published Opportunity has a newer unpublished revision | Only the authoritative published material can enter PUBLIC observation |
| Opportunity is withdrawn after binding projection | Submission revalidation rejects the stale binding |
| Announcement is public but has no Enquiry participation | No subject-specific Enquiry binding is invented |
| Enquiry origination permission is absent | Public source observation does not authorise submission |
| Website delivery permission is absent | Source classification supplies no hosting or delivery grant |
| Client requests a retained private revision | Public-source exemption does not expose it |
| Source evidence is unavailable | No fabricated positive participation or commercial denial |

**Alternatives rejected:** charging separately for each supporting source read; treating public Exposure as submission permission; granting all Publication operations through one wildcard.

**Ambiguity review:** exact source scope, ownership, exemptions, independent target requirements and exclusions are explicit. No universal public-access policy is introduced.

The evidence is accepted-authority inspection and these scenarios, not executed implementation tests.
