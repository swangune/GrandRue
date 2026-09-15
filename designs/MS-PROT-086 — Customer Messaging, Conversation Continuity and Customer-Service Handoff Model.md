# MS-PROT-086 — Customer Messaging, Conversation Continuity and Customer-Service Handoff Model

**Document ID:** MS-PROT-086  
**Version:** 1.0  
**Status:** ACCEPTED  
**Approved:** 8 September 2026 by explicit manual approval  
**Authority type:** Customer Communication and cross-capability interaction semantic/design authority  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`

**Depends on:** composite MS-PROT-027; composite MS-PROT-035; composite MS-PROT-043 through v1.4; composite MS-PROT-053; composite MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-065; composite MS-PROT-066; MS-PROT-067; composite MS-PROT-068; composite MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-073; composite MS-PROT-074; composite MS-PROT-075; and MS-PROT-085.

**Amends:** None  
**Supersedes:** None

**Resolves:** `MS-PROT-043-V14-DQ-006` — exact durable Conversation creation policy and channel mapping.

**Does not resolve:** `MS-PROT-043-V14-DQ-007` — Enquiry/communication retention periods; `MS-PROT-043-V14-DQ-008` — Enquiry/attention telemetry; `MS-PROT-085-DQ-001` — initial Attention Contract portfolio; the initial customer-messaging channel portfolio; the initial customer-service response-contract portfolio; the production guest-conversation access mechanism; or the initial conversation-attachment portfolio.

**Preserves:** Enquiry and source-capability business-truth ownership; Conversation and Message communication ownership; Notification delivery ownership; Merchant Attention handling ownership; authorisation, entitlement, eligibility, provider-readiness, Projection and Exposure boundaries; immutable evidence; AI non-authority; provider neutrality; data minimisation; and the modular-monolith capability architecture.

**Purpose:** Define the minimum sufficiently expressive durable customer-messaging, Conversation-continuity and customer-service handoff model through which Main Street can preserve coherent merchant/customer communication across supported channels without turning communication into source business truth, a universal ticket lifecycle, an AI authority surface or an enterprise contact-centre platform.

---

# 0. Fundamental Vision Conformance

## 0.1 Outcome

**VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

Customer messaging supports administrative compression by giving merchants one role-native view of relevant recorded communication while allowing customers to use supported channels that fit the business relationship.

The internal distinctions among Enquiry, Conversation, Message, Notification, Merchant Attention, channel delivery, source business truth and executable operations are necessary complexity. They prevent a simple merchant-facing conversation from becoming a competing order, booking, payment, case-management or provider-inbox authority.

That complexity SHALL remain primarily inside Main Street. A sole trader or ordinary worker MUST NOT need to configure queues, ticket states, routing graphs, AI confidence thresholds or provider identifiers to answer a customer.

## 0.2 Feature Admission

The model satisfies:

- the **Representation Test**, because durable two-way customer communication has identity, participant, access, ordering and provenance that cannot be represented safely as transient notification evidence;
- the **Coordination Test**, because customer communication may concern independently owned business facts without acquiring their mutation authority; and
- the **Administrative-Compression Test**, because supported channels may be observed through one role-native communication surface without requiring the merchant to reconcile provider inboxes manually.

It does not justify a social network, generic CRM or enterprise contact-centre platform.

## 0.3 Integration Versus Native Ownership

External providers MAY transport messages. Main Street requires a small native communication record only where it must preserve channel-independent Conversation and Message meaning, current access, cross-capability relationships and governed handoff.

Provider-specific inboxes, thread identifiers and delivery states remain integration evidence. They do not become Main Street Conversation identity or business truth.

---

# 1. Problem

Main Street already distinguishes:

```text
Enquiry
    = customer/business-contact request truth

Conversation
    = durable communication continuity

Message
    = accepted communication contribution

Notification
    = outbound communication intent and delivery evidence

Merchant Attention
    = handling coordination

source capability
    = order, booking, payment or other business truth
```

Without a governing model, an implementation could:

- create a Conversation for every Enquiry whether communication continues or not;
- force all Conversations through a universal `OPEN / RESOLVED / CLOSED` lifecycle;
- merge unrelated matters because they share an email address, customer or order;
- split one exchange merely because a customer changes channel;
- treat provider thread identifiers as business identity;
- let an AI confidence score authorise a customer answer;
- expose private order, booking or payment facts through weak contact matching;
- treat a message such as “cancel it” as an executable command;
- claim that an undelivered outbound Message reached the customer;
- tell a customer that a human handoff succeeded before durable Attention exists;
- preserve an overbroad “complete customer history” contrary to data minimisation; or
- evolve into a general ticketing and contact-centre product.

---

# 2. Purpose and Scope

MS-PROT-086 governs:

- durable Conversation creation and reuse;
- Message acceptance and immutable recorded continuity;
- participant, guest-access and channel bindings;
- relationship of communication to independently owned business subjects;
- inbound and outbound communication boundaries;
- customer-service response assessment;
- AI-assisted, automated and human response boundaries;
- Merchant Attention handoff;
- data minimisation, attachments, retries, concurrency and failure honesty; and
- the production decisions that remain deferred.

It applies to supported two-way customer communication. It does not make every notification, Enquiry, business event or provider interaction a Conversation.

---

# 3. Explicit Non-Goals

MS-PROT-086 does not create:

- a universal Conversation lifecycle;
- a ticket, case, task, queue or workflow engine;
- a CRM or universal customer timeline;
- a social network;
- a general contact-centre platform;
- a requirement that every Enquiry create a Conversation;
- a requirement that every merchant enable AI or automated responses;
- a business-operation command language embedded in messages;
- a source of order, booking, payment, refund, return or other capability truth;
- a Notification replacement;
- a Merchant Attention replacement;
- a provider-specific messaging architecture;
- marketing-campaign authority;
- internal staff-note semantics;
- concrete retention durations;
- concrete channel/provider selection;
- concrete guest token/session technology; or
- concrete attachment, automated-response or UI portfolio selection.

---

# 4. Governing Principle

```text
customer or merchant communication intent
        ↓
accepted creation / continuity contract
        ↓
durable Conversation and immutable Message
        ↓
optional owner-qualified relationship to business subject
        ↓
Notification delivery and/or Merchant Attention handoff
        ↓
separate authorised source-owner operation where required
```

Hard distinctions:

```text
Conversation
    ≠ Enquiry
    ≠ Notification
    ≠ Merchant Attention
    ≠ business-operation aggregate
    ≠ provider thread

Message accepted
    ≠ delivered
    ≠ read
    ≠ understood
    ≠ acknowledged as business fact
    ≠ executable instruction
    ≠ source operation completed
```

---

# 5. Canonical Model

Conceptually:

```text
ConversationCreationContract {
    contractIdentity
    version
    applicableSourceOrInitiationFamily
    creationAndReuseSemantics
    participantRequirements
    relationshipRequirements
    channelBindingRequirements
    provenanceRequirements
}

Conversation {
    conversationIdentity
    MerchantScope
    createdUnderContract
    createdAt
    provenance
}

ConversationRelationship {
    conversationIdentity
    relationshipKind
    ownerQualifiedSubjectReference
    establishedBy
    establishedAt
    provenance
}

ConversationParticipantBinding {
    conversationIdentity
    participantSide
    actorOrRelationshipReference
    accessBasis
    validFrom
    validUntil?
    provenance
}

GuestConversationAccessGrant {
    conversationIdentity
    guestRelationshipReference
    purpose
    permittedOperations
    validFrom
    validUntil
    revocationEvidence?
    provenance
}

ConversationChannelBinding {
    conversationIdentity
    MerchantScope
    providerConnectionOrRoute
    channel
    externalThreadReference?
    validFrom
    validUntil?
    provenance
}

ConversationMessage {
    messageIdentity
    conversationIdentity
    logicalMessageIdentity
    direction
    participantBindingReference
    acceptedContentReference
    acceptedAt
    externallyReportedAuthoredAt?
    channelBindingReference?
    sourceEvidence
    provenance
}

CustomerServiceResponseContract {
    contractIdentity
    version
    applicableRequestFamily
    permittedEvidence
    prohibitedCommitments
    responseModeRules
    handoffRules
    provenance
}

CustomerServiceResponseAssessment {
    assessmentIdentity
    conversationIdentity
    triggeringMessageIdentity
    responseContractIdentity
    evidenceCoverage
    outcome
    assessedAt
    provenance
}
```

The physical representation remains an implementation concern except where a later accepted authority or active design gate requires further semantic precision.

---

# 6. Semantic Ownership

## 6.1 Customer Communication Owns

Customer Communication owns:

- durable Conversation identity;
- creation/reuse decisions under the applicable Conversation Creation Contract;
- participant, guest-access and channel bindings;
- immutable accepted Message records;
- communication-only relationships to owner-qualified subjects;
- customer-service response assessments; and
- communication continuity projections.

## 6.2 Other Owners Retain

Enquiry owns Enquiry truth and submission provenance.

A source capability owns its business aggregate, lifecycle, decision, commitment and operation.

Notifications owns outbound communication intent, channel selection where governed, dispatch attempts and delivery/read evidence.

Merchant Attention owns handling occurrence and handling facts.

Identity, Merchant Scope, Membership, Role, Actor Authorisation, Commercial Entitlement, Operational Eligibility, provider readiness, Projection and Exposure remain governed by their accepted owners.

## 6.3 No Ownership Transfer

A Conversation relationship, Message reference, response assessment, Notification outcome or Attention occurrence MUST NOT transfer ownership or mutation authority.

---

# 7. Conversation Creation Contract

A durable Conversation SHALL be created or reused only under an accepted `ConversationCreationContract` or semantically equivalent owner-qualified authority.

The contract SHALL establish:

- the applicable initiation or source family;
- exact Merchant Scope;
- evidence required to create or reuse;
- participant-binding requirements;
- permitted business-subject relationships;
- channel-binding rules;
- logical retry identity;
- conflict and unresolved outcomes; and
- historical provenance.

The creation decision SHALL produce exactly one of:

```text
CREATE_NEW
LINK_EXISTING
NO_CONVERSATION_REQUIRED
REJECTED
UNRESOLVED
```

`NO_CONVERSATION_REQUIRED` is a valid accepted outcome. Main Street MUST NOT create durable communication state merely because it can.

---

# 8. Direct Messaging Initiation

Where a supported customer or merchant action directly initiates durable messaging, one atomic application operation MAY establish:

```text
Conversation
+ first ConversationMessage
+ required participant binding
+ required channel or guest-access binding
+ applicable owner-qualified relationship
```

The operation SHALL validate all required current authority and access before commit.

If any mandatory element cannot be established, the operation MUST reject or remain unresolved. It MUST NOT leave a reusable empty Conversation that falsely suggests a communication relationship.

---

# 9. Enquiry Relationship

An Enquiry MAY:

- remain a durable Enquiry without a Conversation;
- contribute to creation of a new Conversation under an accepted creation contract; or
- be linked to an existing Conversation where the contract's strong reuse evidence is satisfied.

Hard distinction:

```text
Enquiry accepted
    ≠ Conversation required
    ≠ CustomerContext created
    ≠ Message accepted
    ≠ Merchant Attention established
```

The Enquiry remains independently identifiable and owner-qualified after any Conversation relationship is established.

---

# 10. Conversation Continuity and Reuse

Conversation reuse SHALL be evidence-based and contract-qualified.

Reuse MUST NOT rely solely on:

- the same CustomerContext;
- the same email address or telephone number;
- the same order, booking or payment;
- the same contact form;
- the same external provider account;
- semantic similarity inferred by AI; or
- a merchant preference to keep one thread per customer.

Strong exact evidence MAY include an authenticated continuation, a valid purpose-bound guest access grant, an accepted external-thread binding with current provider evidence, or another explicitly registered continuity proof.

Where reuse cannot be established safely:

```text
reuse = UNRESOLVED or CREATE_NEW under the applicable contract
```

The system MUST NOT silently merge communication histories.

---

# 11. Conversation Relationships to Business Subjects

A Conversation MAY hold owner-qualified relationships to one or more business subjects.

A relationship SHALL preserve:

- owning capability;
- exact subject identity;
- relationship kind;
- evidence and actor/system authority that established the link;
- effective time; and
- provenance.

A relationship means only that the communication materially concerns the subject under the relationship semantics.

It MUST NOT imply:

- access to the subject;
- authority to mutate the subject;
- identity equality between participant and subject;
- customer ownership of the subject;
- lifecycle coupling; or
- that every Message concerns every linked subject.

Current observation of related subject material requires the source owner's access and Projection/Exposure rules at read time.

---

# 12. Conversation Participants

Participant bindings SHALL distinguish at least:

```text
CUSTOMER_SIDE
MERCHANT_SIDE
AUTOMATED_ASSISTANCE
```

or semantically equivalent closed owner-qualified outcomes.

A participant binding is not merely a delivery address. It establishes the accepted basis on which an actor, relationship or automated assistant participates in the Conversation.

Hard distinctions:

```text
participant
    ≠ email address
    ≠ telephone number
    ≠ provider account
    ≠ current authorisation

AUTOMATED_ASSISTANCE
    ≠ human merchant participant
    ≠ source-owner authority
```

Merchant-side observation and response SHALL revalidate current Merchant Scope, Membership/Role and Actor Authorisation. Historical participation does not preserve current access.

---

# 13. Guest Conversation Access

A guest MAY continue a Conversation only through a valid purpose-bound `GuestConversationAccessGrant` or semantically equivalent accepted mechanism.

The grant SHALL bind:

- one exact Conversation;
- one guest relationship or accepted continuation subject;
- permitted operations;
- validity period;
- revocation or invalidation semantics;
- protection against cross-Merchant and cross-Conversation reuse; and
- provenance.

Possession of contact information is insufficient.

The exact production token, session, link, cookie or equivalent transport remains deferred under `MS-PROT-086-DQ-003`.

---

# 14. Channel Binding

A `ConversationChannelBinding` SHALL be scoped by:

```text
Merchant Scope
+ provider connection or accepted route
+ channel
+ external thread reference where applicable
+ validity/provenance
```

A channel is a transport/interaction route. It is not Conversation identity.

Provider-specific thread, mailbox, phone number, sender address or conversation identifiers MAY be retained as binding evidence. They MUST NOT redefine Main Street participant, Conversation, Message or business-subject meaning.

A provider route change MUST NOT rewrite historical Message provenance.

---

# 15. Cross-Channel Continuity

One Conversation MAY use multiple supported channels.

Cross-channel association SHALL occur only under accepted evidence that establishes continuity for the exact Conversation and Merchant Scope.

The following alone are insufficient:

- matching contact value;
- matching display name;
- AI semantic similarity;
- proximity in time;
- the same related business subject; or
- merchant assumption.

Where continuity is unresolved, Main Street SHALL preserve separate records or an explicit unresolved association rather than silently merge.

---

# 16. Conversation Message and Draft

A `ConversationMessage` is an immutable accepted communication contribution within one exact Conversation.

A draft is mutable preparation and is not a Message until accepted by the applicable operation.

Message acceptance SHALL preserve:

- exact Conversation and Merchant Scope;
- logical message identity;
- direction;
- participant binding;
- accepted content or controlled content reference;
- acceptance time;
- externally reported authored time where available;
- channel/source evidence;
- attachment references where permitted; and
- provenance.

Editing a sent or received Message in place is prohibited. Correction, redaction display, withdrawal or lawful removal effects require explicit immutable evidence under the applicable authority.

---

# 17. Time and Ordering

`acceptedAt` records when Main Street accepted the Message.

`externallyReportedAuthoredAt` MAY preserve a provider/customer-reported authored time, but it MUST NOT be treated as trusted Main Street acceptance time without accepted evidence.

A Conversation MAY expose an accepted sequence for deterministic continuity. That sequence establishes only Main Street's accepted ordering under the applicable ingestion rules.

It MUST NOT claim exact real-world authorship order where channels, provider delay, offline clients, clock skew or concurrent submissions make that fact unknowable.

---

# 18. Idempotency and Duplicate Delivery

Every Message-acceptance operation SHALL have a stable logical identity within the applicable Merchant Scope, Conversation and channel/source context.

Duplicate technical delivery or retry of the same logical Message SHALL converge on one accepted Message.

Reuse of one logical identity for materially different content, participant, Conversation, source or direction SHALL conflict.

Provider event identifiers MAY contribute to idempotency evidence but MUST NOT be assumed globally unique or semantically sufficient beyond their registered provider scope.

---

# 19. Inbound External Message Processing

Before accepting an externally received Message, Main Street SHALL:

1. authenticate or otherwise validate the provider/source under the accepted connection contract;
2. establish exact Merchant Scope;
3. resolve the applicable channel binding;
4. establish creation or continuity under the applicable Conversation Creation Contract;
5. bind the participant under accepted evidence;
6. validate content/resource limits;
7. preserve provider/source provenance; and
8. commit the Message idempotently.

Unknown, forged, conflicting or insufficiently correlated evidence MUST NOT create a CustomerContext, Conversation, participant, business relationship or source operation by guess.

It SHALL be rejected, quarantined or left unresolved under an owner-qualified failure outcome.

---

# 20. Outbound Message and Delivery

Acceptance of an outbound Conversation Message records the merchant/customer-service communication contribution. It does not prove external delivery.

Where external dispatch is required:

```text
ConversationMessage accepted
        ↓
Notification intent / delivery operation
        ↓
provider attempt and evidence
        ↓
delivery outcome or uncertainty
```

Notification owns dispatch attempt and delivery/read evidence under composite MS-PROT-075.

A delivery failure MUST NOT erase or rewrite the accepted outbound Message. The communication surface SHALL preserve the distinction between recorded Message and external delivery state.

---

# 21. Read, Response and Acknowledgement Evidence

Provider-reported delivered, opened, read, typing or response evidence MAY be retained only under the applicable Notification/channel evidence contract.

Such evidence MUST NOT by itself establish:

- that a person understood the Message;
- acceptance of business terms;
- acknowledgement of an order, booking, payment, cancellation or refund;
- source lifecycle transition;
- Merchant Attention acknowledgement; or
- legal receipt where separate legal semantics are required.

A later inbound Message is a new Message. It does not retroactively prove the meaning of earlier delivery evidence.

---

# 22. Communication Availability

Communication availability SHALL be derived from current accepted evidence and produce an outcome equivalent to:

```text
AVAILABLE
TEMPORARILY_UNAVAILABLE
NOT_PERMITTED
UNRESOLVED
```

The derivation MAY consider channel support, provider readiness, entitlement where applicable, participant access, guest-grant validity, abuse protection, source restrictions and current operational health.

`AVAILABLE` does not guarantee delivery or response.

`TEMPORARILY_UNAVAILABLE` does not revoke historical Conversation truth.

`NOT_PERMITTED` is distinct from provider failure.

`UNRESOLVED` MUST NOT be presented as available.

---

# 23. Message Is Not a Business Operation

Customer or merchant language inside a Message is communication evidence. It is not automatically a Command, Merchant Intent or accepted source-owner instruction.

Examples include:

```text
“cancel my booking”
“refund this order”
“change the delivery address”
“I accept the quote”
“reserve it for me”
```

A source consequence requires the separately accepted source-owner operation, current identity/access/authority, exact subject, deterministic validation, concurrency and outcome semantics.

An automated or human response MUST NOT claim that such an operation completed unless accepted source evidence establishes that result.

---

# 24. Customer-Service Response Contract

A `CustomerServiceResponseContract` is an immutable versioned semantic definition governing what response modes are permitted for one registered request family.

It SHALL identify:

- applicable triggering Message/request family;
- evidence sources and required currentness;
- coverage requirements;
- response modes;
- prohibited commitments and judgements;
- protected-information/access requirements;
- human-handoff rules;
- deterministic validation;
- provenance and historical affinity; and
- failure/unresolved behavior.

No production automated-response family may operate without an accepted contract from the portfolio selected under `MS-PROT-086-DQ-002`.

---

# 25. Customer-Service Response Assessment

For one triggering Message, the applicable response assessment SHALL produce exactly one of:

```text
AUTOMATED_INFORMATIONAL_RESPONSE_ELIGIBLE
DRAFT_FOR_HUMAN_REVIEW
HUMAN_RESPONSE_REQUIRED
NO_RESPONSE_REQUIRED
UNRESOLVED
```

The assessment is communication-owned decision evidence. It is not the response itself, source business truth or a grant of authority.

AI confidence alone MUST NOT select `AUTOMATED_INFORMATIONAL_RESPONSE_ELIGIBLE`.

Where required evidence, coverage, access, contract resolution or currentness is insufficient, the result SHALL be `HUMAN_RESPONSE_REQUIRED` or `UNRESOLVED` according to the accepted contract. The system MUST NOT fill the gap with plausible language.

---

# 26. Automated Informational Response Eligibility

An automated informational response MAY be eligible only where all of the following are established:

- an accepted current Customer-Service Response Contract applies;
- the request is within its registered informational scope;
- every required source is authorised, current and sufficiently covered;
- the response creates no business commitment, exception, negotiation, regulated judgement or discretionary decision;
- protected information is exposed only through current relationship-bound access;
- deterministic validation confirms the response conforms to the contract;
- provenance is retained;
- the response is represented transparently as automated; and
- a truthful human-handoff path is available where the contract requires it.

Failure of any required condition prohibits automatic response.

---

# 27. Mutable Availability and Similar Current Information

Where an automated or drafted response uses mutable information such as opening status, stock indication, booking availability or delivery estimate, the response SHALL:

- identify the applicable observation time or bounded validity where material;
- preserve source and Projection/Exposure currentness;
- avoid implying reservation, commitment or guarantee unless the source owner independently established it; and
- degrade honestly when the observation is unavailable or stale.

Canonical distinction:

```text
availability observed at time T
    ≠ reserved
    ≠ guaranteed at later time
    ≠ booking/order accepted
```

---

# 28. Protected Business Information

Private order, booking, payment, customer or other protected business information MAY be used in a response only when current accepted access establishes the exact participant/guest relationship to the exact subject and permits that purpose.

Contact-value equality, Conversation participation, historical access, a related subject link or AI inference is insufficient by itself.

The response SHALL expose only the minimum material permitted by the applicable Projection/Exposure and data-protection authority.

---

# 29. AI Assistance Boundary

AI MAY:

- classify a Message under registered semantics;
- suggest a Conversation relationship for deterministic validation;
- summarize visible recorded Messages and authorised related material;
- draft a response under an accepted response contract;
- produce an automated informational candidate where the contract permits;
- ask a bounded clarification question; and
- recommend human handoff.

AI MUST NOT:

- invent business facts, prices, policies, availability or delivery outcomes;
- establish identity, participant binding, guest access or Conversation reuse;
- decide refunds, cancellations, returns, exceptions, complaints or disputes;
- promise discounts, compensation, reservations or special treatment;
- negotiate or make a binding commitment;
- expose unauthorised business or personal data;
- treat Message text as executable instruction;
- claim a business operation or human handoff succeeded without durable evidence;
- suppress a required human handoff;
- create new response authority from confidence; or
- mutate source business truth.

AI output remains non-authoritative until accepted deterministic semantics establish the applicable Message, assessment, instruction or source operation.

---

# 30. Automated Response Provenance and Transparency

An automated response SHALL preserve:

- the exact response contract and version;
- triggering Message;
- material source/evidence references;
- coverage/currentness evidence;
- AI/model provenance where AI contributed;
- deterministic validation outcome;
- accepted Message identity;
- governing time; and
- delivery evidence separately where applicable.

Customer-facing presentation SHALL make clear that the response is automated. It MUST NOT impersonate a named human worker or falsely imply human review.

Main Street MAY present the merchant's business identity while truthfully disclosing automation.

---

# 31. Human Request and Handoff

Where a customer explicitly requests a person and the merchant offers an applicable human-contact path, the assessment SHALL be `HUMAN_RESPONSE_REQUIRED`.

AI MAY collect only the minimum additional information required by the accepted contract and MUST NOT obstruct, repeatedly deflect or falsely claim that self-service is mandatory.

Main Street may tell the customer that a handoff succeeded only after a durable Merchant Attention Occurrence or another accepted owner-qualified human-handling record has committed.

If durable handoff cannot be established, the response SHALL state that human handoff is unavailable or unresolved and provide any accepted alternative. It MUST NOT claim that “someone will get back to you” without authority and durable evidence for that consequence.

---

# 32. Merchant Attention Integration

An accepted inbound Conversation Message MAY contribute a distinct source episode to Merchant Attention only under an activated MS-PROT-085 Attention Contract.

The source reference SHALL include the exact Message identity and Conversation identity. Repeated technical delivery of the same Message MUST NOT create multiple Attention occurrences.

A later distinct inbound Message MAY create a distinct Attention occurrence even within the same Conversation where the activated Attention Contract so provides.

`MS-PROT-085-DQ-001` remains unresolved. MS-PROT-086 does not activate any production Attention source family.

Attention acknowledgement, assignment, snooze or disposition does not alter Message or Conversation truth and does not resolve the customer's source business request.

---

# 33. Notification Integration

Notifications owns outbound dispatch, provider attempts and delivery/read evidence.

Customer Communication owns the accepted outbound Message and communication continuity.

Notification failure, retry exhaustion or unknown provider outcome MUST NOT:

- erase the Message;
- delete the Conversation;
- manufacture a customer response;
- establish business acknowledgement;
- clear Merchant Attention; or
- rewrite source business truth.

A later successful delivery attempt SHALL converge on the same accepted outbound Message and Notification intent where the applicable logical operation is the same.

---

# 34. Untrusted Content and Prompt Injection

Customer Messages, provider metadata, attachment text and linked external content are untrusted inputs.

They MUST NOT:

- expand AI permissions;
- change system or response-contract instructions;
- grant participant or source access;
- activate tools or source operations;
- override data-protection, Exposure or authorisation rules;
- disclose hidden instructions, secrets or unrelated records; or
- create semantic authority.

AI and deterministic processing SHALL consume only the bounded, purpose-built context permitted by composite MS-PROT-057 and the applicable response contract.

---

# 35. Data Protection, Recorded History and Minimisation

Customer Communication SHALL preserve the recorded Message and relationship history required for accepted continuity, evidence, audit and lawful lifecycle obligations.

Hard distinction:

```text
recorded communication history
    ≠ complete history of every customer interaction
    ≠ permanent retention
    ≠ universal customer profile
```

Main Street MUST NOT claim a complete customer timeline merely because several channels are connected.

It SHALL minimise duplicated source data, provider payloads, identity data and attachment material. Retention, deletion, legal hold, anonymisation, subject rights and purpose limitation remain governed by composite MS-PROT-053.

Exact Enquiry/communication retention periods remain deferred under `MS-PROT-043-V14-DQ-007`.

---

# 36. Attachments

Conversation attachments SHALL use composite MS-PROT-066 Media semantics.

Upload or provider receipt does not by itself mean:

```text
validated
attached to a Message
safe to process
authorised for participant exposure
retained indefinitely
```

A Message attachment relationship SHALL preserve exact Media identity/version, purpose, participant/source evidence, validation status, access/Exposure and provenance.

The initial supported attachment types, limits and processing portfolio remain deferred under `MS-PROT-086-DQ-004`.

---

# 37. Internal Notes Boundary

An internal merchant/staff note is not a customer Conversation Message.

Internal notes require a separately typed, protected model with exact visibility, authorisation, retention, audit and non-exposure semantics before production use.

A presentation layer MUST NOT place internal notes in customer-visible Message collections or send them through customer channels.

MS-PROT-086 does not create that internal-note model.

---

# 38. Marketing Boundary

Transactional or service communication under MS-PROT-086 does not create marketing-campaign, audience-selection, promotional-consent or advertising authority.

A Conversation participant or prior Message does not by itself authorise marketing contact.

Where marketing communication is later supported, it requires the applicable separate semantic, consent, preference, provider and data-protection authority.

---

# 39. Abuse and Resource Protection

Inbound creation, continuation, Message acceptance, guest access, attachments, automated responses and human handoff SHALL remain subject to composite MS-PROT-062, MS-PROT-070 and MS-PROT-073.

Abuse protection MUST NOT silently merge identities, fabricate successful acceptance, expose cross-Merchant data or convert an unavailable provider into a business rejection.

Where a customer-visible rejection is permitted, it SHOULD use plain language without exposing internal security or diagnostic detail.

---

# 40. Transaction, Reaction, Failure and Concurrency

## 40.1 Commit Boundary

The authoritative Message-acceptance transaction SHALL commit the Message and every mandatory creation/continuity binding that must be true for it to exist.

Notification dispatch, Merchant Attention establishment, AI assessment and other cross-capability effects SHALL normally proceed as durable post-commit reactions under composite MS-PROT-065 and MS-PROT-072.

Failure of a reaction MUST NOT roll back or erase the committed Message.

## 40.2 Failure Classification

Customer Communication SHALL distinguish outcomes equivalent to:

```text
CREATION_CONTRACT_UNAVAILABLE
CONTINUITY_UNRESOLVED
PARTICIPANT_BINDING_INVALID
GUEST_ACCESS_INVALID_OR_EXPIRED
CHANNEL_BINDING_UNRESOLVED
PROVIDER_EVIDENCE_INVALID
MESSAGE_IDEMPOTENCY_CONFLICT
CONTENT_OR_ATTACHMENT_REJECTED
RELATED_SUBJECT_ACCESS_DENIED
RESPONSE_CONTRACT_UNAVAILABLE
RESPONSE_EVIDENCE_STALE_OR_INCOMPLETE
HUMAN_HANDOFF_UNAVAILABLE
DELIVERY_UNKNOWN
CONCURRENT_UPDATE_CONFLICT
OUTCOME_UNCERTAIN
```

Equivalent owner-qualified closed classifications MAY be used.

Failure MUST NOT be collapsed into false absence, false delivery, false human handoff, invented continuity or source-operation success.

## 40.3 Concurrency

Conversation creation/reuse and Message acceptance SHALL use exact logical operation identity and sufficiently strong concurrency control.

Concurrent attempts MUST NOT:

- create duplicate Conversations for one proven exact continuation;
- silently merge different continuations;
- duplicate one logical Message;
- attach a Message to a different Conversation;
- reuse a guest grant across scopes; or
- overwrite immutable Message history.

Conflicting material intent SHALL produce an explicit conflict or unresolved outcome.

## 40.4 Reaction Convergence

Notification, Merchant Attention and response-assessment reactions SHALL be independently idempotent and preserve their owner-qualified status.

One reaction's success or failure MUST NOT be used as evidence that another reaction completed.

---

# 41. Architecture Fit

The model fits Main Street's accepted modular monolith.

A bounded Customer Communication capability MAY own the Conversation, participant/channel binding and Message persistence described here. Cross-capability relationships SHALL use explicit application orchestration, registered contracts, owner-qualified references and durable post-commit reactions.

MS-PROT-086 does not require:

- a separate microservice;
- a message broker product;
- a central enterprise Conversation Engine;
- a universal workflow database;
- provider-owned canonical state; or
- one shared lifecycle across business capabilities.

Technology, physical schemas, transport endpoints, background-worker machinery and UI structure remain implementation concerns unless activated by a later design gate.

---

# 42. Deferred Decisions

## 42.1 MS-PROT-086-DQ-001 — Initial Customer Messaging Channel Portfolio

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION CUSTOMER MESSAGING  
**Owner:** Customer Communication product/semantic design composed with provider, Notification, security and data-protection authority  
**Revisit condition:** Before any production two-way customer-messaging channel is enabled.

Resolution SHALL select exact channel families and their creation, continuity, provider-correlation, delivery, abuse and degradation contracts. Listing email, web messaging, SMS or social messaging as examples does not activate them.

## 42.2 MS-PROT-086-DQ-002 — Initial Customer-Service Response Contract Portfolio

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION AUTOMATED CUSTOMER SERVICE  
**Owner:** Customer Communication/customer-service semantic design composed with each source owner and AI/data-protection authority  
**Revisit condition:** Before any production automated customer-service response is enabled.

Resolution SHALL select exact informational request families, required evidence and coverage, prohibited commitments, human-handoff rules, deterministic validation and response provenance. It MUST NOT use a generic AI-confidence threshold as authority.

## 42.3 MS-PROT-086-DQ-003 — Production Guest Conversation Access Mechanism

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION GUEST CONVERSATION CONTINUATION  
**Owner:** Customer Communication composed with Identity, Session, security, data protection and access architecture  
**Revisit condition:** Before a guest can resume or observe a production Conversation.

Resolution SHALL establish the exact protected grant/transport mechanism, expiry, revocation, replay resistance, scope binding and recovery behavior without turning contact information into authentication.

## 42.4 MS-PROT-086-DQ-004 — Initial Conversation Attachment Portfolio

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION MESSAGE ATTACHMENTS  
**Owner:** Customer Communication composed with Media, security, abuse protection, data protection and provider authority  
**Revisit condition:** Before a production Conversation Message can include an attachment.

Resolution SHALL select supported media/document types, limits, validation/scanning, rendition, access, retention and failure behavior under composite MS-PROT-066.

---

# 43. Hard Invariants

## INV-086-001 — Exact Merchant Scope

Every Conversation, Message, participant binding, guest grant, channel binding, response assessment and relationship SHALL be affined to one exact Merchant Scope.

## INV-086-002 — No Universal Conversation Lifecycle

Conversation continuity MUST NOT be governed by a universal `CREATED / AI_PROCESSING / WAITING / ACTIVE / RESOLVED / CLOSED / REOPENED` state machine.

## INV-086-003 — Conversation Creation Is Contract-Governed

A Conversation SHALL be created or reused only under an accepted owner-qualified Conversation Creation Contract.

## INV-086-004 — Enquiry Does Not Require Conversation

Enquiry acceptance MUST NOT automatically create or require a Conversation.

## INV-086-005 — Strong Reuse Evidence

Customer, contact, business-subject or semantic similarity alone MUST NOT merge or reuse a Conversation.

## INV-086-006 — Channel Is Not Conversation Identity

Provider channel or external-thread identity MUST NOT become canonical Conversation identity.

## INV-086-007 — Participant Is Not Endpoint

An email address, telephone number, provider account or channel endpoint MUST NOT by itself establish a Conversation participant.

## INV-086-008 — Guest Access Is Purpose-Bound

Guest continuation SHALL require a valid Conversation-bound purpose-qualified access grant or semantically equivalent accepted authority.

## INV-086-009 — Relationship Does Not Grant Access

A Conversation relationship to a business subject MUST NOT grant observation or mutation authority over that subject.

## INV-086-010 — Current Merchant-Side Authority

Merchant-side observation and response SHALL require current applicable Membership/Role, Actor Authorisation, Entitlement/Eligibility and protection checks.

## INV-086-011 — Message Immutability

An accepted Conversation Message SHALL be immutable; corrections and lifecycle effects require separate governed evidence.

## INV-086-012 — Message Idempotency

Retry or duplicate delivery of one logical Message SHALL converge; materially different reuse of its identity SHALL conflict.

## INV-086-013 — Accepted Time Is Not Authored Time

Main Street acceptance time and externally reported authorship time SHALL remain distinct.

## INV-086-014 — Accepted Sequence Does Not Claim Real-World Order

A deterministic accepted sequence MUST NOT claim exact real-world message order where evidence cannot establish it.

## INV-086-015 — Message Accepted Is Not Delivered

Acceptance of an outbound Message MUST NOT establish external delivery, read or understanding.

## INV-086-016 — Delivery or Read Is Not Business Acknowledgement

Provider delivery/read evidence MUST NOT establish business-term acceptance, source transition or Merchant Attention handling.

## INV-086-017 — Message Is Not Command

Natural language within a Message MUST NOT directly execute or mutate a source business operation.

## INV-086-018 — Source Ownership Is Preserved

Conversation, Message, response assessment, Notification and Attention evidence MUST NOT redefine or mutate source-capability truth.

## INV-086-019 — AI Confidence Is Not Permission

An AI confidence value MUST NOT grant automated-response, data-access, business-decision or execution authority.

## INV-086-020 — Automated Response Is Contract-Grounded

An automated response SHALL require an applicable accepted response contract, authorised current evidence, sufficient coverage and deterministic validation.

## INV-086-021 — Automated Response Cannot Create Commitment

Automated informational response MUST NOT negotiate, decide a discretionary matter or create a business commitment, exception, reservation, refund or cancellation.

## INV-086-022 — Protected Information Requires Exact Relationship-Bound Access

Conversation participation, contact equality or a subject link alone MUST NOT expose private order, booking, payment, customer or other protected business information.

## INV-086-023 — Human Request Is Honoured

Where an applicable merchant human-contact path exists, an explicit customer request for a person SHALL produce human-response-required semantics.

## INV-086-024 — Handoff Claims Require Durable Evidence

Main Street MUST NOT tell a customer that human handoff succeeded until the applicable durable handling record has committed.

## INV-086-025 — Customer Content Is Untrusted

Message, provider and attachment content MUST NOT expand AI, access, semantic or execution authority.

## INV-086-026 — Notification Ownership Is Preserved

Notifications owns dispatch and provider delivery/read evidence; Customer Communication owns Message and Conversation continuity.

## INV-086-027 — Attention Ownership Is Preserved

Merchant Attention owns handling coordination; a Message may contribute only through an activated Attention Contract and Attention handling does not resolve communication or source truth.

## INV-086-028 — Recorded History Is Not Complete History

Main Street MUST NOT describe recorded communication as a complete customer history unless complete coverage is independently established.

## INV-086-029 — Internal Note Is Not Customer Message

Internal merchant/staff notes MUST NOT share customer-visible Message semantics without a separately accepted typed protected model.

## INV-086-030 — No Enterprise Contact-Centre Platform

Customer Communication MUST remain a bounded messaging and service-handoff capability and MUST NOT become a generic ticketing, workflow, CRM or enterprise contact-centre platform.

---

# 44. Alternatives and Trade-Offs

## 44.1 Rejected — Universal Conversation Lifecycle

A universal `CREATED → AI_PROCESSING → WAITING → ACTIVE → RESOLVED → CLOSED → REOPENED` lifecycle confuses communication continuity with request resolution, AI processing, human work and source business state. Different Conversations may continue without a single objective that becomes “resolved”.

## 44.2 Rejected — Enquiry Equals Conversation

An Enquiry is durable customer/business-contact truth. Some enquiries require no continued exchange; direct messaging may begin without an Enquiry. Equality would duplicate or force state.

## 44.3 Rejected — Notifications as Messaging

Notification intent and delivery evidence cannot represent inbound participation, durable Conversation continuity or customer-service response assessment.

## 44.4 Rejected — One Conversation Per Customer

This merges unrelated contexts, broadens access and creates an unbounded customer dossier. Reuse must be contract- and evidence-specific.

## 44.5 Rejected — Provider Inbox as Authority

Provider identities and threads are channel evidence. Making them canonical would couple semantics to providers and prevent safe cross-channel continuity.

## 44.6 Rejected — Contact Correlation

Matching contact details is insufficient identity, access and continuity evidence and creates cross-person and cross-context disclosure risk.

## 44.7 Rejected — AI Confidence Threshold as Response Authority

Confidence does not establish evidence coverage, access, currentness, permitted business meaning or lack of commitment.

## 44.8 Rejected — Mandatory AI First

Merchants and customers may communicate directly. AI is optional assistance governed by accepted contracts, not a mandatory gatekeeper.

## 44.9 Rejected — Automatic Business Operations from Messages

Message text is ambiguous and lacks source-owner operation semantics, current authority, concurrency and deterministic validation.

## 44.10 Rejected — Enterprise Contact Centre

Queues, cases, service-level agreements, scripts, workforce performance, routing graphs and universal dispositions exceed the minimum depth required for Main Street's target merchants.

## 44.11 Accepted Trade-Off

The accepted model adds explicit creation, participant, access, channel and response contracts plus immutable Message evidence. This increases internal precision but protects customer data, source ownership, provider replaceability and merchant simplicity.

---

# 45. Falsification and Ambiguity Review

## 45.1 First Falsification

Initial hypothesis:

```text
unified provider inbox
+ universal open/closed conversation
+ AI answers when confident
```

**Outcome:** FAIL.

Counterexamples showed that the model could:

1. force an Enquiry into durable messaging without need;
2. merge unrelated matters for one contact;
3. expose a private order through contact equality;
4. let provider thread identity own the Conversation;
5. confuse Message acceptance with delivery;
6. let AI confidence substitute for evidence and permission;
7. turn “cancel it” into an unsafe source command;
8. claim human escalation before durable handoff;
9. create a universal customer dossier; and
10. expand toward contact-centre workflow complexity.

The design was revised to add explicit creation outcomes, strong continuity evidence, relationship-bound access, provider-neutral channel bindings, delivery separation, response contracts/assessments, source-operation separation, durable Attention handoff, data minimisation and an anti-contact-centre boundary.

## 45.2 Second Falsification

The revised model was tested against:

- a website Enquiry that needs no reply;
- a guest who continues one exact conversation through a protected link;
- one customer using email and web messaging for the same proven thread;
- two people sharing a contact address;
- a merchant worker whose Membership ends while previously participating;
- an inbound forged provider event;
- concurrent duplicate delivery;
- an outbound Message whose provider outcome is unknown;
- a request for current booking availability;
- a request to cancel and refund;
- a customer asking for a person;
- a prompt-injection attempt in Message content;
- a Message with an attachment;
- internal staff notes;
- Notification failure after Message commit; and
- a sole trader operating without queues, tickets or AI configuration.

**Outcome:** PASS, subject to the four explicit production portfolio deferred decisions.

## 45.3 Residual Falsification

Three residual ambiguity risks remained:

1. an accepted Message sequence could be misread as exact real-world authorship order;
2. an internal note could leak if represented as an ordinary Message; and
3. recorded communication could be described as a complete customer history without coverage.

Targeted revisions were added to Sections 17, 35 and 37 and to hard invariants `INV-086-014`, `INV-086-028` and `INV-086-029`.

**Final falsification outcome:** PASS.

## 45.4 Cross-Clause Ambiguity Review

The final authority was checked across:

- Enquiry versus Conversation;
- Conversation versus universal request/case lifecycle;
- participant versus endpoint;
- Conversation identity versus provider thread;
- creation/reuse versus retry;
- Message versus draft;
- accepted time versus authored time;
- accepted sequence versus real-world order;
- Message acceptance versus delivery/read;
- read versus business acknowledgement;
- relationship versus access;
- Message language versus executable instruction;
- AI confidence versus response authority;
- automated information versus business commitment;
- human-handoff intent versus durable handoff;
- Conversation Message versus internal note;
- recorded history versus complete history;
- Notification versus Customer Communication;
- Merchant Attention versus source resolution; and
- generic model versus production portfolios.

**Outcome:** PASS.

---

# 46. Corpus Conformance and Implementation Relationship

## 46.1 Accepted Authority Conformance

MS-PROT-086 composes with:

- MS-PROT-043 for CustomerContext/Enquiry separation and optional Conversation creation;
- MS-PROT-059 for channel convergence, channel-independent semantics and idempotency;
- MS-PROT-075 for Notification intent and delivery evidence;
- MS-PROT-085 for Merchant Attention handoff;
- MS-PROT-057 for AI assistance and purpose-built context;
- MS-PROT-053 for data minimisation and lifecycle;
- MS-PROT-066 for Media/attachment semantics;
- MS-PROT-062, MS-PROT-063, MS-PROT-067, MS-PROT-070 and MS-PROT-073 for access, trusted context, external connection security, degradation and abuse protection;
- MS-PROT-069 for uncertainty/reconciliation; and
- applicable source owners, Projection and Exposure for business facts and protected representation.

No ownership collision or architecture change is introduced.

## 46.2 Lower-Authority Corpus Reconciliation

The following earlier Draft documents contain statements that conflict with this accepted authority and SHALL remain historical evidence only within overlapping scope:

- `docs/platform-services/conversation-engine.md`;
- `docs/data/conversation-lifecycle-model.md`;
- `docs/business-rules/customer-communication-rules.md`;
- `docs/workflows/business-operations/customer-communication.md`; and
- `docs/development/PRD/customer-support-and-communication.md`.

In particular, their universal Conversation lifecycle, central Conversation Engine, confidence-based AI authority, automatic Conversation/Attention conclusions and complete-history claims do not govern.

## 46.3 Implementation Rules Impact

`designs/IMPLEMENTATION-RULES.md` already requires accepted semantic authority, current runtime checks, idempotency, immutable evidence, exact traceability and escalation of missing material semantics.

**Implementation-rules amendment:** NOT REQUIRED.

## 46.4 Sequencing

MS-PROT-086 is accepted post-baseline authority and is not currently implementation-promoted.

It does not reprioritise MS-IMP-001, alter the active IMP-05 work, or make IMP-06/IMP-07 READY.

Production Customer Communication remains gated by the applicable deferred decisions and a conforming implementation node under `designs/IMPLEMENTATION-RULES.md`.

**Corpus conformance:** PASS.

---

# 47. Acceptance and Governance Outcome

## 47.1 Acceptance Criteria

MS-PROT-086 is acceptable only if:

- Conversation creation and reuse are governed by accepted owner-qualified contracts;
- Enquiry remains distinct and may exist without a Conversation;
- no universal Conversation lifecycle is introduced;
- participant, guest access and channel bindings remain distinct;
- Conversation relationships do not grant source access or mutation;
- Message truth is immutable, idempotent and distinct from draft, delivery, read and business acknowledgement;
- accepted ordering does not overclaim real-world ordering;
- Message language cannot directly execute a source operation;
- automated responses require accepted contracts, current authorised evidence, sufficient coverage and deterministic validation;
- AI confidence never grants response or execution authority;
- protected information requires exact relationship-bound current access;
- explicit human requests and handoff claims are handled honestly;
- customer content remains untrusted;
- Notification, Merchant Attention and source-capability ownership remain separate;
- recorded history is data-minimised and not represented as universally complete;
- internal notes cannot leak through Message semantics;
- the model remains provider-neutral and bounded below enterprise contact-centre scope;
- the four production portfolio decisions remain deferred until their gates; and
- the current implementation sequence remains unchanged.

## 47.2 Governance Outcome

**Fundamental Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Design completeness:** PASS  
**First falsification:** FAIL → REVISED  
**Second falsification:** PASS WITH THREE RESIDUAL AMBIGUITIES  
**Targeted revision:** COMPLETED  
**Final falsification:** PASS  
**Cross-clause ambiguity review:** PASS  
**Corpus conformance:** PASS  
**Customer Communication ownership:** PASS  
**Source-capability ownership:** PASS  
**Notification ownership:** PASS  
**Merchant Attention ownership:** PASS  
**AI non-authority:** PASS  
**Provider neutrality:** PASS  
**Data minimisation:** PASS  
**Anti-contact-centre proportionality:** PASS  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED — 8 September 2026  
**Repository formalisation:** AUTHORISED

## 47.3 Implementation Consequence

Acceptance establishes semantic/design authority only.

It does not:

- activate a production messaging channel;
- activate an automated customer-service response family;
- authorise guest Conversation continuation or Message attachments before their gates;
- set retention periods or telemetry;
- activate a Merchant Attention source family;
- select providers or transport technology;
- authorise production code, migrations, APIs, UI or deployment;
- reprioritise the accepted implementation programme; or
- repair the separately blocked incomplete MS-PROT-084 base composition.

Implementation SHALL proceed only through `designs/IMPLEMENTATION-RULES.md` after the applicable dependency, deferred-decision and sequencing gates are satisfied.
