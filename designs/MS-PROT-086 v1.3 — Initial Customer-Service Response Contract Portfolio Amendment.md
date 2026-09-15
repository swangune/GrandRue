# MS-PROT-086 v1.3 — Initial Customer-Service Response Contract Portfolio Amendment

**Document ID:** MS-PROT-086  
**Version:** 1.3  
**Status:** ACCEPTED  
**Approved:** 9 September 2026 by explicit manual approval  
**Authority type:** Customer Communication response-portfolio semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-086 through v1.2 only within the initial Customer-Service Response Contract portfolio  
**Depends on:** Composite MS-PROT-027; MS-PROT-035; composite MS-PROT-042; composite MS-PROT-043; composite MS-PROT-053 through v1.3; composite MS-PROT-055; composite MS-PROT-057; MS-PROT-059; composite MS-PROT-060; composite MS-PROT-061; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-065; MS-PROT-067; composite MS-PROT-068; composite MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-073; composite MS-PROT-075; composite MS-PROT-077; composite MS-PROT-085 through the paired v1.2 amendment  
**Resolves:** `MS-PROT-086-DQ-002` — Initial Customer-Service Response Contract portfolio  
**Implementation activation:** NONE

---

# 1. Purpose

This amendment selects the initial production-semantic portfolio under which Main Street may automatically answer routine customer-service questions.

The objective is:

```text
routine factual customer question
        ↓
Main Street obtains exact governed facts
        ↓
deterministic response eligibility
        ↓
safe automated response where fully supported
        ↓
human handoff where not safely automatable
```

It does not create a general autonomous customer-service agent.

# 2. Governing Principle

Main Street SHALL automate **fact retrieval and explanation**, not merchant judgement.

Canonical:

```text
customer language
        ↓
request-family interpretation
        ↓
exact registered Response Contract
        ↓
owner-qualified source evidence
        ↓
current access + Projection/Exposure
        ↓
coverage evaluation
        ↓
deterministic response eligibility
        ↓
optional AI language generation
        ↓
deterministic response validation
        ↓
accepted ConversationMessage
```

AI MAY help understand or phrase an answer.

AI SHALL NOT decide that Main Street has authority to answer.

# 3. Initial Portfolio

The initial `CustomerServiceResponseContract` portfolio SHALL contain exactly seven response-contract families:

```text
customer-service/public-merchant-information@1
customer-service/public-offering-information@1
customer-service/published-policy-information@1
customer-service/current-scheduling-availability@1
customer-service/related-booking-appointment-information@1
customer-service/related-order-fulfilment-shipment-information@1
customer-service/related-payment-refund-information@1
```

No other request family is automatically answerable merely because an AI model believes it knows the answer.

# 4. Contract 1 — Public Merchant Information

## `customer-service/public-merchant-information@1`

This contract MAY answer factual questions concerning currently customer-exposable merchant-presence information.

Eligible facts include, where governed and exposed:

- business name and public description;
- current public contact information;
- merchant location;
- service area;
- external public presence;
- normal Public Business Hours;
- current open/closed observation where deterministically resolvable from governing time, location/timezone and current hours authority.

Examples include where the business is located, opening/closing time, whether it is open on Sunday, public contact details and whether it serves an area.

Required evidence SHALL derive from the exact current `platform/merchant-presence` or applicable owner-qualified source material and PUBLIC Exposure.

It SHALL NOT answer from model knowledge, cached website text lacking current authority, search-engine snippets, provider business listings merely because they exist, an old Conversation, or merchant contact values inferred from prior Messages.

Where authoritative sources conflict or required currentness cannot be established, `AUTOMATED_INFORMATIONAL_RESPONSE_ELIGIBLE` is prohibited.

# 5. Contract 2 — Public Offering Information

## `customer-service/public-offering-information@1`

This contract MAY answer factual questions about currently published and customer-exposable Products, Services, Offerings, bookable-resource categories, published descriptions, published price or price basis, and supported publicly exposed fulfilment/service characteristics.

The response SHALL preserve the exact semantics of the authoritative price representation.

Therefore `from £50` MUST NOT become `£50`, and `price available on quotation` MUST NOT become an invented numerical price.

This family SHALL NOT answer bespoke quotations, negotiated prices, unpublished discounts, current inventory quantity, unsupported stock availability, future promotions, or inferred products/services not yet authoritative.

# 6. Contract 3 — Published Policy Information

## `customer-service/published-policy-information@1`

This contract MAY explain a currently applicable merchant policy where that policy already has accepted source authority and customer exposure.

Eligible examples include published cancellation rules, published rescheduling rules, published returns policy, published delivery or collection policy, and other accepted recurring merchant policy explicitly exposed to customers.

The contract explains the policy. It SHALL NOT determine an individual customer's entitlement or exercise merchant discretion.

Hard distinction:

```text
merchant policy says returns are normally accepted within X
        ≠
this customer's particular return is approved
```

Requests to approve a return, determine definitive refund entitlement, make an exception, or waive a charge require another response mode.

This contract SHALL NOT provide legal advice about whether the merchant policy is lawful or what statutory rights override it.

# 7. Contract 4 — Current Scheduling Availability

## `customer-service/current-scheduling-availability@1`

This contract MAY answer current Scheduling availability questions only from sufficiently current Scheduling-owned evidence.

Eligibility requires exact Merchant Scope; sufficiently resolved Offering/Service/Resource context; sufficiently resolved requested time scope; current Scheduling serviceability; all required owner-qualified availability evidence; applicable customer Exposure; and governing observation time.

The response MUST preserve:

```text
availability observation
    ≠ reservation
    ≠ hold
    ≠ Appointment
    ≠ Booking
    ≠ future guarantee
```

Permitted language may state that particular times are available at the moment. It MUST NOT state that a slot has been reserved unless an independently authorised Scheduling/Booking operation committed that result.

Inventory-stock availability is NOT part of this initial contract.

# 8. Contract 5 — Related Booking / Appointment Information

## `customer-service/related-booking-appointment-information@1`

This contract MAY answer factual questions about an exact existing Booking or Appointment where the source owner's current customer-access requirements are independently satisfied.

Eligible facts MAY include, where exposed, date, time, merchant/location, relevant booked service/resource, currently authoritative Booking/Appointment state or comparable owner-owned fact, and other exact customer-visible non-discretionary details.

Eligibility requires independently valid source-object access.

Hard rules:

```text
Conversation participation
    ≠ Booking access

Guest Conversation Access Grant
    ≠ Booking access
```

The MS-PROT-086 v1.2 browser grant alone SHALL NOT expose Booking or Appointment information.

This contract SHALL NOT automatically cancel, reschedule, rebook, waive fees, approve lateness, promise resource availability, or make merchant exceptions.

Requests for those effects require separately authorised source operations or human handling.

# 9. Contract 6 — Related Order / Fulfilment / Shipment Information

## `customer-service/related-order-fulfilment-shipment-information@1`

This contract MAY answer factual questions about an exact Order and its independently authoritative fulfilment/shipment evidence where current related-customer access is established.

Eligible facts MAY include ordered commitments; currently authoritative fulfilment evidence; dispatch facts; Shipment tracking evidence; delivered/not-delivered outcome where source authority establishes it; and customer-visible delivery/collection information.

The contract SHALL NOT manufacture one generic `OrderStatus` from separate Order, Fulfilment and Shipment truths. It SHALL explain the applicable source-owned facts.

It SHALL NOT automatically cancel an Order, amend quantities, change delivery address, authorise replacement, initiate a return, refund, promise redelivery, or declare provider uncertainty resolved.

# 10. Contract 7 — Related Payment / Refund Information

## `customer-service/related-payment-refund-information@1`

This contract MAY answer factual questions about exact customer-visible Payment or Refund evidence where source-owner access is independently satisfied.

Eligible facts MAY include, where exposed, applicable Payment Obligation amount, current amount due where authoritative, accepted Payment Application evidence, provider execution outcome only after Main Street has interpreted it into governed Payment evidence, accepted Refund evidence, and unresolved payment/provider state stated honestly.

The contract SHALL preserve:

```text
customer obligation
    ≠ provider transaction
    ≠ Payment Application
    ≠ settlement
    ≠ Refund
```

It SHALL NOT authorise a refund, decide a dispute, alter an obligation, waive money owed, interpret a chargeback, promise provider settlement, give financial/legal advice, or expose Payment information from Conversation identity alone.

# 11. Request Classification

A request-family candidate MAY be produced deterministically or with AI assistance.

The candidate SHALL NOT become authority until the applicable contract resolver establishes exact contract family/version, exact Merchant Scope, applicable request semantics, required evidence sources, required access, currentness, coverage, and current contract activation.

AI confidence SHALL NOT substitute for any of those conditions.

# 12. Material Request Atoms

A triggering customer Message MAY contain more than one material request.

Example:

```text
"What time is my appointment and can you cancel it?"
```

The first part is potentially informational. The second requests a business mutation.

Initial automated response SHALL occur only when **every material request atom** is eligible for safe automated handling.

Therefore a mixed request containing a material non-informational action SHALL NOT be automatically answered in part while silently deferring the action. It SHALL instead become `HUMAN_RESPONSE_REQUIRED` or an equivalent human-reviewed draft path.

# 13. Coverage Model

Response coverage SHALL use closed qualitative outcomes equivalent to:

```text
COMPLETE_FOR_REQUEST
INCOMPLETE
UNRESOLVED
```

A percentage confidence/coverage score SHALL NOT authorize response.

`AUTOMATED_INFORMATIONAL_RESPONSE_ELIGIBLE` requires `COMPLETE_FOR_REQUEST` for every material request atom.

`INCOMPLETE` or `UNRESOLVED` prohibits automatic substantive response.

Main Street MUST NOT fill missing source facts with probable language.

# 14. Response Evidence Bundle

Automated eligibility SHALL be based on a bounded evidence package equivalent to:

```text
ResponseEvidenceBundle {
    MerchantScope
    triggeringMessage
    responseContractIdentity/version
    requestFamily
    requestedFactScope
    governingTime
    sourceEvidenceReferences
    sourceProgress/currentness
    ProjectionServiceability
    ExposureDecisions
    relationship/accessEvidence where required
    evidenceCoverage
}
```

The package SHALL contain only information needed for the response purpose.

It SHALL NOT become a new source of business truth.

# 15. Fact-First Response Plan

Before an automated response is composed, Main Street SHALL establish the exact facts that may be communicated.

Conceptually:

```text
ResponseFactSet {
    factReferences
    permittedQualifiers
    observationTime where material
    prohibitedImplications
}
```

If Scheduling establishes `15:00 AVAILABLE` at observation time T, the response may communicate that observation. It may not infer that `15:00` is reserved.

# 16. Deterministic Rendering Preferred

Where straightforward deterministic rendering can produce a natural and useful answer, Main Street SHOULD prefer it over model inference.

Examples include opening hours, appointment time, current amount due and shipment delivery outcome.

AI SHALL NOT be invoked merely because customer service is branded as an AI feature.

# 17. AI Language Generation

Where AI contributes language, it receives only the bounded triggering context, Response Fact Set, contract rules and permitted presentation context.

It SHALL NOT receive an unrestricted merchant/customer database or full historical customer dossier.

AI output remains untrusted.

# 18. Deterministic Response Validation

Before accepting an AI-assisted automated response as a Message, deterministic validation SHALL establish that:

- every material factual claim is supported by the Response Fact Set;
- numerical amounts are authorized;
- dates and times are authorized;
- price qualifiers are preserved;
- statuses/outcomes correspond to owner-qualified facts;
- no prohibited commitment is introduced;
- no unsupported certainty is introduced;
- no protected field exceeds current Exposure;
- no source operation is claimed;
- no human action is falsely claimed;
- required automation disclosure is preserved.

Failure of validation prohibits automatic send.

# 19. Clarification

An automated clarification MAY occur where one selected response family is plausible, one or more non-sensitive selectors are genuinely missing, requesting the selector does not disclose protected information, and the clarification does not make a commitment.

A customer SHALL NOT be trapped in repeated automated clarification.

For one unresolved triggering request, Main Street MAY perform at most one automated clarification cycle.

If sufficient evidence still cannot be established afterwards, the result SHALL be `HUMAN_RESPONSE_REQUIRED` where a human path is applicable.

Identity or protected-subject uncertainty SHALL NOT be resolved through casual clarification asking the customer to type private account details into the Conversation.

# 20. Requests That Require Human Handling

The initial portfolio SHALL classify the following as `HUMAN_RESPONSE_REQUIRED` unless another separately accepted source operation already handles them through a different governed interaction:

- explicit request to speak to a person;
- complaint;
- dispute;
- negotiation;
- bespoke quotation;
- discount request;
- compensation request;
- exception to merchant policy;
- cancellation decision;
- refund decision;
- return approval;
- order amendment;
- booking/appointment change requiring execution;
- payment dispute;
- chargeback issue;
- ambiguous protected-record ownership;
- safety-critical judgement;
- legal/regulatory judgement;
- discriminatory or high-risk discretionary decision;
- unsupported mixed request containing a material non-informational action;
- any request whose correct answer depends on merchant discretion.

# 21. No Generic FAQ Knowledge Base Authority

A document, website paragraph, old response, model memory or generic FAQ corpus SHALL NOT automatically become customer-service authority.

Merchant-authored information may be used only when it already belongs to an accepted authoritative/publication/policy owner and current Exposure permits use.

Rejected as independent business authority:

```text
website scrape
    ↓
vector database
    ↓
AI answer
```

Retrieval technology MAY assist access to already governed material but does not create authority.

# 22. No General Web Knowledge as Merchant Truth

Automated customer service SHALL NOT use general web search or model world knowledge to invent merchant-specific hours, services, prices, policies, stock, availability, order state, payment state, or booking state.

External authoritative information requires separately accepted source/provider authority.

# 23. Protected Information

For response families 5–7, all required source-object access SHALL be established independently.

The following are insufficient:

```text
same email
same phone
same name
Conversation relationship
Conversation participant
Guest Conversation Access Grant
AI inference
```

A response SHALL disclose only fields permitted by the current source-owner CUSTOMER Exposure and data-use authority.

# 24. Registered and Guest Customers

Registered customer access MAY satisfy a protected source requirement only where authenticated CustomerContext/source participation authority actually establishes it.

Guest customer access MAY satisfy a protected source requirement only through an independently valid source-specific contextual access authority.

The Conversation browser grant introduced by MS-PROT-086 v1.2 remains limited to the Conversation.

# 25. Automated Response Message

A successfully generated automated answer SHALL become an immutable outbound `ConversationMessage`.

Its participant basis SHALL be `AUTOMATED_ASSISTANCE` or the existing semantically equivalent participant classification.

The Message SHALL preserve triggering Message, response assessment, exact contract family/version, source/evidence provenance, governing time, model/inference provenance where applicable, and deterministic validation evidence.

# 26. Transparency

The customer SHALL be able to determine that an automated response was automated.

Main Street MAY speak through the merchant's business identity.

It SHALL NOT impersonate a named worker, the merchant personally, a professional advisor, or a human reviewer.

Exact UI wording remains presentation scope.

# 27. Delivery

Automated Message acceptance and external delivery remain distinct:

```text
automated Message accepted
        ↓
Notification
        ↓
provider attempt
        ↓
delivery evidence
```

Provider failure SHALL NOT erase the accepted automated Message or cause automatic fabrication of a second answer.

Delivery uncertainty SHALL remain delivery uncertainty.

# 28. Automated Response Idempotency

For one response assessment, the logical automated-response operation SHALL have a stable identity.

Retry of the same logical response SHALL converge on one accepted outbound Message.

Technical retries, AI-provider retries and Notification retries SHALL NOT create repeated customer answers.

A later materially distinct customer Message creates a new response-assessment opportunity.

# 29. Final Revalidation

Immediately before automated Message acceptance, Main Street SHALL revalidate all correctness-sensitive mutable evidence.

This includes where applicable source currentness, customer/source access, Exposure, merchant/account availability, Response Contract activation and data-use authority.

A response eligible earlier is not entitled to send if material authority has since changed.

# 30. Draft for Human Review

Main Street MAY produce a draft where the Response Assessment is `DRAFT_FOR_HUMAN_REVIEW`.

A draft is not a ConversationMessage, delivery evidence, business truth or merchant approval. It MUST NOT be sent automatically and SHALL be refreshed/revalidated where it contains mutable facts before human send.

Human editing of the draft does not transfer AI into semantic authority.

The resulting Message becomes authoritative communication only when the current human response operation accepts it.

# 31. Human Response Handoff Dependency

Production automated customer service under this portfolio requires the paired:

```text
customer-communication / human-response-required@1
```

Merchant Attention Contract defined by MS-PROT-085 v1.2.

Main Street MUST NOT activate these automated response contracts while having no truthful escalation path for requests the contracts refuse to answer.

# 32. Human-Handoff Language

After the corresponding Merchant Attention Occurrence has durably committed, Main Street MAY truthfully communicate wording equivalent to:

```text
"I've passed this to the business for review."
```

It SHALL NOT promise that someone will reply within a duration, that the issue will be resolved, or that the merchant has accepted the request unless another accepted authority establishes that exact obligation/outcome.

If durable handoff cannot be established, Main Street SHALL say that it cannot safely complete the handoff and MAY provide another currently valid contact path.

# 33. Non-Requests

A Message that is established as a non-request acknowledgement may produce `NO_RESPONSE_REQUIRED`.

Where it is materially unclear whether the Message contains a customer request, Main Street SHALL NOT use AI confidence alone to suppress merchant handling.

# 34. Activation

The response portfolio is not automatically active for every merchant.

Production automatic response requires applicable Customer Messaging capability; selected response-contract support in the serving release; current commercial entitlement where applicable; accepted merchant/configuration activation; required source capabilities; current runtime access/eligibility; and the paired human-handoff path.

This amendment does not select pricing tier or merchant-facing activation wording.

Main Street SHOULD avoid per-question-family configuration burden. A simple portfolio-level merchant choice MAY be used downstream, subject to existing configuration authority.

# 35. Commercial Boundary

Commercial tier may govern access to automated customer service.

Commercial state SHALL NOT redefine source truth, Response Contract semantics, protected-data access, evidence coverage, or what AI is permitted to claim.

Loss of entitlement stops new automated use according to commercial authority; it does not rewrite historical Messages.

# 36. Data Lifecycle

Response assessments, AI artefacts, evidence packages, drafts and derived response metadata remain governed by MS-PROT-053.

They SHALL NOT be retained merely because they may help future AI.

Unless independently required, a derived response artefact SHALL NOT outlive its justified source/purpose lifecycle.

Full provider payloads or source-object copies SHALL NOT be retained merely to prove that an answer was generated. Minimum provenance is preferred.

# 37. Conversation Context

Only the minimum Conversation segment necessary to understand the current customer request MAY enter automated response context.

Retention of old Messages does not authorize their use.

An old unrelated Message SHALL NOT influence the current answer merely because it remains stored.

# 38. Prompt Injection

Customer text remains untrusted.

Customer statements do not modify source authority, access, Response Contract, tool permissions, merchant policy, or system instructions.

# 39. Provider Neutrality

AI provider, email provider, calendar provider and payment provider do not own response semantics.

Provider changes SHALL NOT require redefining these seven response families.

# 40. Failure Outcomes

The initial portfolio SHALL preserve closed outcomes equivalent to:

```text
RESPONSE_CONTRACT_NOT_APPLICABLE
REQUEST_FAMILY_UNRESOLVED
REQUEST_REQUIRES_HUMAN_JUDGEMENT
SOURCE_EVIDENCE_INCOMPLETE
SOURCE_EVIDENCE_UNRESOLVED
SOURCE_EVIDENCE_STALE
PROTECTED_ACCESS_NOT_ESTABLISHED
EXPOSURE_PROHIBITS_RESPONSE
AUTOMATED_RESPONSE_VALIDATION_FAILED
AUTOMATED_RESPONSE_GENERATION_UNAVAILABLE
HUMAN_HANDOFF_UNAVAILABLE
OUTCOME_UNCERTAIN
```

No failure may be converted into a plausible fabricated answer.

# 41. Architecture

The initial portfolio requires no separate customer-service microservice, universal knowledge graph, ticket engine or autonomous-agent fleet.

It composes:

```text
Customer Communication
+
source-owner reads
+
Projection / Exposure
+
AI inference where useful
+
deterministic validation
+
Notification
+
Merchant Attention
```

within Main Street's accepted modular-monolith architecture.

# 42. Explicitly Deferred

This amendment does not activate or resolve automatic refunds; automatic cancellations; automatic rescheduling; automatic order amendment; automatic returns approval; negotiation; bespoke quotations; discretionary compensation; legal advice; complaint adjudication; dispute resolution; inventory stock-response automation; attachment understanding; voice/phone agent; SMS/social customer-service portfolio expansion; marketing; cross-merchant customer assistant; autonomous source-tool execution from customer text; or generic RAG knowledge-base authority.

# 43. Hard Invariants

1. Only the seven selected Response Contract families may qualify for production automated substantive customer answers under this portfolio.
2. AI confidence never grants response authority.
3. Every automated factual claim must map to current owner-qualified evidence.
4. Complete material-request coverage is required for automatic substantive response.
5. Any material action/judgement atom prevents automatic send for the whole triggering Message.
6. Conversation access does not grant source-object access.
7. Guest Conversation access does not grant Booking, Order or Payment access.
8. Automatic responses never create business commitments.
9. Availability observations never create reservations.
10. Published policy explanation never creates individual entitlement.
11. Provider state never silently becomes source business truth.
12. AI-generated language is deterministically validated before acceptance.
13. Automated responses are transparently automated.
14. Accepted Message and delivery remain distinct.
15. Automated retries converge on one response Message.
16. At most one automated clarification cycle is permitted for one unresolved triggering request.
17. Explicit human request overrides automation.
18. Human handoff is claimed only after durable Attention establishment.
19. Response/Attention derived artefacts remain subject to MS-PROT-053 lifecycle and minimisation.
20. Commercial packaging cannot redefine semantic eligibility.
21. No implementation is activated by acceptance.

# 44. Rejected Alternatives

**Generic autonomous customer-service agent:** rejected because it would collapse classification, source authority, judgement and execution into model behaviour.

**Generic RAG over website and merchant documents:** rejected as authority. Retrieval may assist, but ungoverned text cannot override capability-owned facts.

**Public-information-only automation:** rejected as too weak because existing customer relationship/Exposure architecture can safely support factual Booking, Order and Payment answers where exact access exists.

**Automatic business operations from customer messages:** rejected. Customer text remains communication until a separately authorised source operation executes.

**AI-confidence threshold:** rejected because confidence measures prediction, not authority or evidence sufficiency.

**Automatic partial answers for mixed action requests:** rejected initially because a partial automated answer can conceal that a material customer request still requires action.

**Every inbound Message creates merchant work:** rejected because routine questions may be answered automatically and acknowledgements may require no response.

**No human-handoff contract:** rejected because automation without a durable escape path would create a chatbot dead end and false claims about merchant follow-up.

# 45. Fundamental Vision Conformance

**VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The design directly advances administrative compression:

```text
routine fact retrieval
    → Main Street handles

judgement / exception / risk
    → merchant handles
```

The merchant does not configure intents, confidence thresholds, prompts, RAG collections, routing graphs or escalation trees.

Internal evidence and validation complexity is justified because it prevents automated customer service from making false commitments or leaking customer data.

# 46. Architecture Review, Governance and Implementation Consequence

**Architecture review:** PASS. The seven contracts are source-consumers, not source owners. They introduce no duplicate business truth, universal customer status, generic ticket lifecycle, AI semantic authority, provider ownership or business-type branching.

**Falsification:** PASS against public facts, source staleness, price qualifiers, Scheduling races, protected-access failures, provider uncertainty, mixed requests, prompt injection, retries, delivery failure and low-software-capacity merchant scenarios.

**Recommendation:** ACCEPT.  
**Manual approval:** GRANTED — 9 September 2026.  
**Repository formalisation:** AUTHORISED.  
**Implementation promotion:** NONE.

Acceptance resolves `MS-PROT-086-DQ-002`. `MS-PROT-086-DQ-004` remains unresolved. `ADR-014-DQ-011` remains separately active before production Guest Conversation browser access. Acceptance does not implement automated customer service, deploy an AI model, activate any merchant, alter pricing, create APIs, implement provider adapters or change the current implementation sequence.