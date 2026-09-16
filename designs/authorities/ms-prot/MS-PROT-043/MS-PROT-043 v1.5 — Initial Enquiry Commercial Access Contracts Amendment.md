# MS-PROT-043 — Initial Enquiry Commercial Access Contracts Amendment

**Document ID:** MS-PROT-043
**Version:** 1.5
**Status:** ACCEPTED
**Approved:** 14 September 2026 — explicit manual approval of the complete Enquiry access-contract proposal and limited formalisation scope
**Authority type:** Enquiry-owned commercial access-purpose contracts
**Governed by:** DESIGN-RULES v2.3; DOCUMENT-GOVERNANCE v2.3; MS-FUNDAMENTAL-VISION-001
**Amends:** MS-PROT-043 v1.4 within commercial access classification of the bounded submission and observation paths below
**Depends on:** MS-PROT-043 v1.2–v1.4; MS-PROT-046 v1.2 §§16–20; MS-PROT-056 v1.7 §§4–5, 10–11 and v1.9 §§3–6; composite MS-PROT-062
**Implementation activation:** NONE
**Purpose:** Define exact Enquiry-owned access points and protected purposes that Commercial can subsequently bind into the standard catalogue.

## 1. Governing decision

Enquiry SHALL define three initial commercial access contracts:

| Exact access-contract identity | Protected purpose |
|---|---|
| `enquiry/general-submission-access@1` | `ORIGINATE_ENQUIRY` |
| `enquiry/opportunity-submission-access@1` | `ORIGINATE_ENQUIRY` |
| `enquiry/merchant-observation-access@1` | `OBSERVE_ENQUIRY` |

These contracts classify access to already-governed Enquiry behaviour. They SHALL NOT create separate Enquiry business objects, channel-specific submission operations or an additional Enquiry lifecycle.

Commercial owns the entitlement identities and bindings referencing these contracts. Enquiry owns the target behaviour and purpose classification.

## 2. Scope and exclusions

The initial portfolio covers:

- merchant-general Enquiry submission without a subject;
- Enquiry submission about an exact Publication-owned Opportunity;
- merchant observation of one stored Enquiry.

It does not admit:

- direct Enquiry participation for every Publication;
- additional subject-specific access contracts;
- Conversation creation, message sending or automated responses;
- CustomerContext reconciliation;
- Attention acknowledgement or review;
- Notification delivery;
- Booking, Appointment, Order, Payment or other commitment creation.

Existing accepted semantics for other Enquiry subjects remain unchanged. Their executable catalogue admission requires separately identified access contracts; this amendment does not supply wildcard coverage.

## 3. Identity and version affinity

An access contract is identified by its exact owner, contract identifier and revision.

These identities SHALL NOT be treated as:

- Commercial Entitlement identities;
- Exposure contract identities;
- merchant-specific Enquiry identities;
- transport endpoint names.

One contract revision SHALL retain immutable meaning. A later revision or additional subject family SHALL NOT become covered merely because it shares an owner, namespace or operation name.

Commercial binding and historical interpretation SHALL preserve the exact target revision under MS-PROT-056 v1.9.

## 4. General submission access

`enquiry/general-submission-access@1` applies only when the submitted Enquiry has no semantic subject.

Its protected purpose is `ORIGINATE_ENQUIRY`.

The underlying submission SHALL follow MS-PROT-043 v1.4 §§2–3, 7–12 and 15–17:

- trusted Merchant Scope;
- legitimate current merchant-general Enquiry participation;
- Enquiry-owned requirements;
- minimum unresolved customer input;
- immutable submission evidence;
- logical-request idempotency.

Public and merchant-assisted channels SHALL use the same commercial purpose when they represent this same business operation.

A public submitter SHALL NOT require a paid customer subscription or a manufactured CustomerAccount, CustomerContext, Booking, Order or Payment.

Commercial permission is evaluated for the merchant receiving the Enquiry. It does not replace public-interaction legitimacy, actor/trust requirements or Resource Protection Admission.

## 5. Opportunity submission access

`enquiry/opportunity-submission-access@1` applies only to an Enquiry carrying an exact Publication-owned Opportunity subject through the accepted Opportunity-to-Enquiry participation.

Its protected purpose is `ORIGINATE_ENQUIRY`.

The submission SHALL preserve and revalidate:

- exact Merchant Scope;
- Opportunity identity;
- current registered subject-interaction participation;
- current subject eligibility and Exposure;
- submission-time provenance.

MS-PROT-043 v1.4 §§4–6 and 13–14 and MS-PROT-046 v1.2 §§16–20 govern these requirements.

A stale, withdrawn, forged or cross-merchant subject binding SHALL NOT be converted into a general Enquiry.

The general-submission contract SHALL NOT be used to bypass the Opportunity contract by discarding a structurally supplied subject.

This contract grants no right to apply for, reserve, purchase or otherwise obtain the Opportunity’s subject matter.

## 6. Merchant observation access

`enquiry/merchant-observation-access@1` applies to merchant observation of an exact stored Enquiry in the current trusted Merchant Scope.

Its protected purpose is `OBSERVE_ENQUIRY`.

The bounded representation remains governed by MS-PROT-043 v1.4 §§22–25 and its four MERCHANT Exposure families:

```text
enquiry/merchant-submission-content
enquiry/merchant-submitted-contact
enquiry/merchant-subject-context
enquiry/merchant-communication-summary
```

Commercial permission SHALL NOT confer Actor Authorisation, data-use permission or Exposure.

The access contract does not grant:

- PUBLIC access to stored submissions;
- customer Enquiry-history access;
- access to an entire Conversation;
- unrestricted current subject data;
- general CustomerContext history.

Current subject information and communication material remain subject to their source owners’ access and Exposure contracts. Submission-time provenance SHALL remain distinguishable from current source truth.

Observation SHALL NOT record acknowledgement, initial review, a response or resolution.

## 7. Commercial requirements and tier placement

Each contract requires its stated protected purpose to be satisfied through an exact Commercial Access Binding under MS-PROT-056 v1.9.

Missing binding information SHALL NOT mean “no entitlement required.”

For these Enquiry-owned contracts, no additional Enquiry commercial purpose is required beyond the purpose stated in Section 1. Independently invoked operations remain governed by their own contracts.

The three purposes belong within FREE under MS-PROT-056 v1.7 §5 and therefore within the explicit BUSINESS and GROWTH supersets.

This amendment does not assign entitlement identities, publish plan revisions or establish a complete manifest.

Entitlement satisfaction SHALL derive from independently valid grant sources, not comparison with a mutable tier label.

## 8. Supporting dependencies

The following dependencies SHALL remain explicit during catalogue assembly:

| Path | Required supporting boundary |
|---|---|
| General submission | Current merchant-general Public Interaction and Enquiry applicability |
| Opportunity submission | Publication-owned Opportunity participation, current eligibility and Exposure |
| Merchant observation | Enquiry request-scoped representation, trusted merchant observation and applicable Exposure |
| Initial review, when activated | Separate `enquiry/initial-submission-review@1` Attention contract |
| Notification, when independently established | Separate source and Notification contracts |
| Continuing platform messaging | Separate Customer Communication contracts |

Attention, Notification and Conversation SHALL NOT become prerequisites for committing the minimal Enquiry unless an independently accepted owning contract explicitly requires that relationship.

The accepted initial-review contract creates no Notification responsibility. This amendment SHALL NOT introduce one.

Necessary supporting access SHALL NOT be placed behind a higher-tier requirement contrary to MS-PROT-056 v1.7 §10.

However, a supporting dependency’s missing commercial classification SHALL NOT be silently treated as exempt. An affected catalogue binding remains inadmissible until its dependency requirements are resolved.

## 9. New use, history and residual access

Origination is new Enquiry activity.

Observation is a distinct current access purpose. Enquiry existence, age or former paid-plan membership SHALL NOT independently establish commercial observation permission.

An Enquiry is not a Booking, Order or other commitment. This amendment therefore establishes no residual commitment-resolution classification under MS-PROT-056 v1.5 §§17–20.

Loss of a commercial grant SHALL NOT delete Enquiry evidence, rewrite submission history or change retention obligations.

A paid-plan downgrade SHALL NOT remove permission still supplied by an independently valid FREE, trial or other accepted source.

Retention, permission to observe and permission to originate SHALL remain distinct.

## 10. Execution, concurrency and failure

The access contracts introduce no additional business transaction or durable permission token.

Submission continues through the Enquiry owner’s authoritative consistency boundary. Concurrency-sensitive commercial and operational conditions SHALL be revalidated under MS-PROT-062; a previously rendered button or preliminary permission result is insufficient.

Submission idempotency remains governed by MS-PROT-043 v1.4 §§15–16:

- one logical submission produces at most one Enquiry;
- retry reconciles with the committed result;
- independent human submissions are not deduplicated by similar content.

This amendment does not broaden the information a retry may disclose.

Commercial rejection SHALL produce no new Enquiry. It SHALL NOT undo a previously committed Enquiry.

Entitlement rejection, authorisation rejection, invalid or stale participation, Resource Protection rejection and technical failure SHALL remain distinguishable internally. Public responses SHALL preserve the applicable disclosure restrictions.

Technical uncertainty SHALL NOT become an invented denial, successful submission or empty observation.

## 11. Ownership, providers and AI

Enquiry retains submission truth. Publication retains Opportunity truth. Commercial retains grants. Attention retains handling facts. Customer Communication retains messages and continuity.

Provider delivery failure SHALL NOT erase an accepted Enquiry.

Live AI availability SHALL NOT be required for deterministic submission or authorised observation. AI assistance grants no additional commercial, actor or source authority.

No new provider responsibility, background schedule or autonomous operation is established.

## 12. Conformance and amendment effect

Conforming downstream implementation SHALL demonstrate:

- exact contract selection;
- no subject-dropping bypass;
- channel convergence;
- current grant-source composition;
- actor and Exposure independence;
- preservation of submission provenance;
- retry safety;
- no accidental Conversation, Attention handling or commitment creation.

This amendment resolves only the three Enquiry access-point and purpose classifications.

`MS-PROT-056-V17-DQ-001` remains OPEN pending complete entitlement definitions, supporting-contract closure and the three-tier manifest. Pricing, reserved-service admission and other catalogue work remain unchanged.

No production activation or implementation-node completion is authorised.

## 13. Review and falsification

**Vision result: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.** Merchants receive questions without configuring a software suite. Exact access identities remain internal; customers and ordinary staff gain no additional configuration burden.

| Counterexample | Required outcome |
|---|---|
| Information-only merchant has no Offering or Orders | General Enquiry remains a valid FREE purpose |
| Scholarship Opportunity is withdrawn after a page loads | Submission fails; no conversion to general Enquiry |
| Two customers submit identical questions | Separate logical requests remain separate Enquiries |
| Merchant loses GROWTH but retains FREE grants | These purposes remain commercially satisfied |
| Staff member lacks permission to read submitted contact details | FREE entitlement does not expose those details |
| Merchant opens an Enquiry | No automatic “Reviewed” or “Responded” fact |
| Email delivery fails after submission | The Enquiry remains committed |
| Future subject type is registered | No automatic coverage through these exact contracts |

These are authority-based design scenarios, not executed tests.

**Alternatives rejected:** one wildcard “all Enquiry” grant; separate channel-based grants; treating FREE as absence of commercial checking; treating every historical Enquiry as residual commitment access.

**Ambiguity result:** the proposed contract scope, ownership, purpose selection and exclusions are explicit. Remaining Commercial bindings and supporting-service classifications are deliberately outside this amendment and continue to block catalogue admission.

**Implementation impact:** C3 remains `IN_PROGRESS` with production activation gated; C5 remains dependency-blocked. Existing Enquiry implementation requires a later conformance review—this proposal does not establish that it already enforces these classifications.
