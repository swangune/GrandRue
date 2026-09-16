# MS-PROT-086 v1.1 — Initial Customer Messaging Channel Portfolio

**Document ID:** MS-PROT-086  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 8 September 2026 by explicit manual approval  
**Authority type:** Customer Messaging production-channel portfolio amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; MS-FUNDAMENTAL-VISION-001  
**Depends on:** MS-PROT-086 v1.0; composite MS-PROT-043; MS-PROT-059; composite MS-PROT-075; composite MS-PROT-053; MS-PROT-067; MS-PROT-085; applicable CustomerContext, Identity, Session, Actor Authorisation, Commercial Entitlement, Projection/Exposure and provider-readiness authority  
**Amends:** MS-PROT-086 only within initial production Customer Messaging channel selection and the exact creation/continuity contracts defined below  
**Resolves:** `MS-PROT-086-DQ-001`  
**Purpose:** Select the smallest production Customer Messaging channel portfolio that permits a customer to initiate contact from the merchant website and preserves asynchronous continuity through email without turning provider threads, contact endpoints or transport state into business identity or Conversation truth.

## 1. Governing decision

The initial production Customer Messaging channel portfolio SHALL contain exactly two channel families:

1. **Merchant Website Messaging** — customer-initiated, public, text-only messaging that creates a new Conversation.
2. **Conversation-Bound Email** — text-only merchant outbound continuation and customer inbound reply continuation for an already-existing Conversation.

The initial portfolio SHALL NOT activate:

- arbitrary inbound email;
- merchant mailbox ingestion;
- email-address-to-Conversation lookup as continuity authority;
- SMS;
- WhatsApp or social messaging;
- generic provider/live-chat inbox semantics;
- guest browser Conversation resume/view;
- autonomous customer-service replies; or
- Message attachments.

Conversation remains canonical. Email addresses, email threads, provider threads, subjects, RFC Message IDs, browser sessions and provider routing identifiers are transport/correlation evidence only and SHALL NOT become Conversation identity or participant identity.

Every MS-PROT-086 v1.0 rule remains unchanged except the statement that the initial production channel portfolio is unselected and any downstream clause necessarily qualified by that former deferral.

## 2. Problem and scope

A customer must be able to contact a merchant from the merchant's Main Street-powered website without configuring a communications provider or creating a Main Street account merely to send an ordinary public message.

The merchant must be able to respond from Main Street's business-native operating surface, and the customer must be able to continue the same Conversation asynchronously even after leaving the website.

The smallest portfolio satisfying both requirements is website initiation plus Conversation-bound email continuation.

This amendment governs:

- exact initial channel families;
- new-Conversation creation through website messaging;
- registered-customer identity binding when authenticated identity already exists;
- guest/unverified reply-endpoint semantics;
- Conversation-bound outbound and inbound email continuation;
- provider-neutral correlation;
- delivery distinction;
- abuse controls;
- failure and degradation semantics; and
- the boundary with MS-PROT-086-DQ-002, DQ-003 and DQ-004.

It does not create a general mailbox, ticketing product, contact-centre product, social-messaging hub or autonomous support agent.

## 3. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.**

The customer interacts with the merchant's website and merchant communication surface. Main Street remains infrastructure rather than the merchant's visible destination.

A merchant enables customer messaging in business language; Main Street owns provider routing, correlation, retry and delivery complexity below that surface. The merchant is not required to configure SMTP, provider APIs, webhooks, thread IDs or reply routing.

The architecture remains provider-replaceable because Conversation and Message truth are independent of provider transport identity.

The initial portfolio is intentionally narrower than the model's future representational capacity.

## 4. Initial channel portfolio

### 4.1 Merchant Website Messaging

Merchant Website Messaging SHALL:

- originate from the merchant-branded public website surface;
- accept customer-authored text within supported limits;
- create a new Conversation for each independent initiation;
- create the first customer Message atomically with that Conversation;
- record website-channel provenance;
- preserve exact Merchant Scope; and
- use the creation contract defined in Section 5.

It SHALL NOT infer an existing Conversation merely because the customer supplies the same name, email address, phone number or other contact value as an earlier interaction.

### 4.2 Conversation-Bound Email

Conversation-Bound Email SHALL support only:

- a merchant response from an existing Conversation delivered to an accepted customer reply endpoint; and
- a customer reply to that Conversation-bound email route being appended to the same Conversation when deterministic correlation succeeds.

It SHALL NOT permit an unsolicited arbitrary inbound email to create a Conversation under this portfolio.

## 5. Website creation contract

The initial website creation contract is:

`WEBSITE_MESSAGE_CREATE_V1`

An accepted invocation SHALL atomically and idempotently establish:

- one new Conversation;
- the first customer Message;
- exact Merchant Scope;
- the applicable public/external interaction classification;
- website-channel provenance;
- participant evidence according to Sections 6 and 7;
- a Conversation-scoped customer reply endpoint when one is accepted for email continuation; and
- required audit/evidence facts.

The contract SHALL use `CREATE_NEW` for every independent website initiation.

The contract SHALL require:

1. exact trusted Merchant Scope;
2. current eligibility for website messaging under accepted configuration/activation authority;
3. non-empty supported text within accepted limits;
4. a logical operation identity adequate for retry convergence;
5. successful applicable abuse/protection checks; and
6. any reply endpoint required by the chosen continuation path to satisfy the accepted syntactic and policy checks.

If an authenticated CustomerContext is supplied, its current identity/access context SHALL be validated before participant binding.

A repeated invocation with the same logical operation identity and same intent SHALL converge on the established result. Reuse of that identity for materially different intent SHALL conflict.

A later independent website submission uses a new logical operation and creates a new Conversation even when the same customer identity or contact endpoint is involved.

## 6. Authenticated registered-customer participant binding

When the website customer is already authenticated as a registered customer under accepted CustomerContext/Identity/Session authority, Main Street SHALL bind the Conversation participant to that authenticated CustomerContext.

The authenticated CustomerContext, not the email transport endpoint, is the authoritative customer-side participant identity for that interaction.

The website flow SHOULD NOT ask the authenticated customer to re-enter name or email merely to reconstruct identity already established by the current authenticated context. Additional contact information MAY be requested only where required for the selected communication path or another separately authorised purpose.

If a registered customer's chosen reply email differs from an account-associated email:

- the authenticated CustomerContext remains participant identity;
- the supplied reply email remains a communication endpoint only;
- the endpoint SHALL NOT overwrite, merge or redefine CustomerContext identity; and
- no Conversation merge or reuse may be inferred from the endpoint.

A registered customer's authenticated identity does not by itself grant persistent browser transcript/resume/view authority beyond the currently authorised interaction surface. That access remains governed by `MS-PROT-086-DQ-003` as refined in Section 13.

## 7. Guest and unverified reply-endpoint semantics

For an unauthenticated/public customer, an accepted website-supplied reply email is an **unverified customer-supplied communication endpoint**.

It SHALL NOT by itself establish:

- canonical CustomerContext identity;
- customer authentication;
- a Guest Conversation Access Grant;
- participant identity beyond the evidence permitted by MS-PROT-086;
- protected source-record access;
- business-object access;
- CustomerContext merge/reuse authority; or
- authority to disclose protected Conversation history.

An optional customer name supplied in this path is presentation metadata only unless a separate accepted authority establishes stronger meaning.

This portfolio does not require email-address verification before ordinary public message creation. That choice deliberately minimizes contact friction while keeping the endpoint's epistemic status explicit and preventing it from becoming protected-access authority.

The customer-facing surface SHOULD permit inspection/correction of the reply endpoint before submission where one is required.

## 8. Conversation-bound email continuation contract

The initial email continuation contract is:

`EMAIL_REPLY_CONTINUE_V1`

It permits exactly two modes:

1. merchant outbound response from an existing Conversation; and
2. customer inbound email reply to that same Conversation through deterministic Conversation-bound routing.

No generic inbound-email-to-new-Conversation mode is selected.

### 8.1 Merchant outbound response

A merchant-authored Conversation Message SHALL become canonical according to MS-PROT-086 before email dispatch is requested.

Email dispatch SHALL then be requested through the applicable provider-neutral Notification/delivery authority.

The system SHALL preserve the distinction between:

- canonical Message acceptance;
- dispatch request acceptance;
- provider acceptance;
- delivery evidence;
- delivery failure; and
- provider outcome unknown.

Email delivery failure or uncertainty SHALL NOT roll back or delete the canonical Conversation Message.

No automatic SMS, social or other-channel fallback is authorised by this portfolio.

### 8.2 Customer inbound reply

A customer email reply MAY append a new customer Message only when the deterministic correlation requirements in Section 10 succeed.

Provider delivery of an inbound event is not itself sufficient authority to append to a Conversation.

## 9. Conversation-bound reply route

An outbound Conversation email MAY establish an opaque reply route bound to:

- one exact Merchant Scope;
- one exact Conversation;
- one provider connection/path; and
- the expected customer reply endpoint required by the selected route.

The route SHALL be:

- opaque;
- unguessable to the practical extent required by security authority;
- revocable;
- provider-replaceable; and
- designed not to expose raw Conversation identity unnecessarily.

Possession of the route alone SHALL NOT grant:

- participant authentication;
- guest browser access;
- Conversation transcript access;
- protected source-record access; or
- business-object access.

The route is correlation authority for the bounded inbound transport path, not a universal bearer credential for Conversation data.

## 10. Deterministic inbound email correlation

Appending an inbound email reply to an existing Conversation SHALL require all of the following evidence to succeed:

1. an authenticated/validated provider event under the applicable provider-security authority;
2. an active Conversation-bound reply route;
3. exact Merchant Scope correspondence;
4. exact Conversation binding;
5. sender endpoint compatibility with the reply endpoint expected by that route under the accepted endpoint-matching policy;
6. idempotent provider-event identity adequate to prevent duplicate Message creation;
7. supported non-empty text after accepted sanitisation/normalisation; and
8. current lifecycle/channel eligibility for accepting that continuation.

RFC `Message-ID`, `In-Reply-To`, `References`, subject text and sender display name MAY be retained as provenance/correlation evidence but SHALL NOT independently authorise Conversation selection.

If correlation is contradictory, ambiguous or insufficient, Main Street MUST NOT guess a Conversation and MUST NOT append the content to an arbitrarily selected Conversation.

A forwarded message or message arriving from an incompatible sender endpoint therefore does not silently gain Conversation authority merely because its subject or quoted headers resemble a valid thread.

## 11. Provider neutrality and merchant operating surface

Customer Messaging SHALL remain provider-neutral at the semantic layer.

Provider-specific:

- message/thread identifiers;
- API concepts;
- webhook payloads;
- credentials;
- bounce codes;
- routing addresses; and
- retry mechanics

remain adapter, security, delivery or evidence concerns and SHALL NOT redefine Conversation semantics.

Merchants SHALL NOT be required to configure SMTP, provider APIs, webhook routing or provider thread behavior.

The merchant responds from Main Street's business-native communication surface. Main Street SHALL NOT require the merchant to operate a provider inbox, ticket folder, queue taxonomy or channel-management console in order to use this initial portfolio.

A permitted merchant-facing control is conceptually equivalent to:

> Allow customers to message this business.

The exact UX wording remains downstream presentation detail provided it preserves the accepted semantics.

## 12. Text and content boundary

The initial portfolio is text-only.

Supported HTML email content SHALL be sanitised/normalised to the accepted safe text representation. Active remote resources SHALL NOT be required to render or accept a customer Message.

When an inbound email contains supported text plus attachments, the text MAY continue under this contract while attachments are omitted, discarded or quarantined according to applicable security/evidence policy. The attachment SHALL NOT become a Conversation Message attachment under this amendment.

An attachment-only email MUST NOT cause Main Street to manufacture an empty successful Conversation Message merely to imply supported receipt.

## 13. Boundary with remaining MS-PROT-086 decisions

### 13.1 DQ-002 — automated customer-service response

`MS-PROT-086-DQ-002` remains unresolved.

This portfolio does not authorise autonomous customer-service replies, source-business commitments or AI-generated outbound responses without the separately accepted response-contract authority required by MS-PROT-086.

### 13.2 DQ-003 — Conversation browser access/resume/view

`MS-PROT-086-DQ-003` remains unresolved.

This portfolio does not create a Guest Conversation Access Grant or permit an unauthenticated guest to return later and observe/resume a Conversation in the browser.

The later DQ-003 resolution SHALL distinguish at least these authority cases:

```text
authenticated registered customer
    -> verify authenticated CustomerContext participation in the Conversation
    -> apply current access/Exposure authority
    -> permit only the accepted Conversation read/write operations
```

from:

```text
guest customer
    -> establish/prove a separate purpose-bound Conversation access capability
    -> apply current access/Exposure authority
    -> permit only the accepted guest Conversation operations
```

This amendment establishes only that authenticated CustomerContext may already be bound as participant identity. It does not pre-approve the browser resume/view mechanism, scope, lifetime or operations for either case.

### 13.3 DQ-004 — attachments

`MS-PROT-086-DQ-004` remains unresolved.

No Conversation Message attachment portfolio is activated by this amendment.

## 14. Abuse and protection requirements

Website initiation SHALL be subject to applicable protection controls before unbounded canonical proliferation, including at minimum policy-capable handling of:

- rate/volume abuse;
- request-size limits;
- bot/automated abuse signals where applicable;
- duplicate logical operations; and
- current merchant/channel eligibility.

Conversation-bound email ingestion SHALL be capable of rejecting, suppressing or isolating as appropriate:

- duplicate provider events;
- bounce loops;
- automatic-response loops;
- malformed or unsupported content;
- oversized content;
- invalid/revoked routes;
- incompatible sender endpoints; and
- replay.

Concrete numerical thresholds, algorithms and provider mechanics remain implementation/security-policy choices unless separately promoted as material design authority.

## 15. Failure and degradation semantics

### 15.1 Website initiation

If failure occurs before the atomic Conversation/first-Message commit, the system MUST NOT tell the customer that the message was sent.

If commit outcome is technically uncertain, the system MUST NOT manufacture certainty. Retry/reconciliation SHALL use the same logical operation identity so that successful prior establishment converges rather than duplicates.

A failure after canonical Message creation MUST NOT delete or roll back the accepted Message merely because a downstream notification, projection or provider action failed.

### 15.2 Outbound email

The customer/merchant surface and evidence model SHALL preserve the distinction between:

- Message accepted;
- dispatch pending;
- dispatch/provider failure;
- provider outcome unknown; and
- available delivery evidence.

A provider outage does not mean the canonical Message disappeared and does not justify a false successful-delivery claim.

### 15.3 Inbound email

Provider retries SHALL converge idempotently.

A provider outage or delayed webhook does not establish that no customer reply exists.

An invalid or ambiguous route MUST NOT silently reroute content to another Conversation.

## 16. Lifecycle and deactivation

Disabling public customer messaging SHALL stop new website Conversation initiation under `WEBSITE_MESSAGE_CREATE_V1` after the applicable activation boundary.

That deactivation does not by itself:

- delete existing Conversations or Messages;
- manufacture Conversation closure;
- define reopening semantics;
- invalidate already-established historical evidence; or
- authorise continued inbound/outbound transport contrary to current eligibility rules.

Existing Conversation continuity remains subject to current access, lifecycle, retention, provider and channel eligibility authority rather than an invented universal open/closed state.

## 17. Hard invariants

1. Conversation is canonical; channel/provider threads are not.
2. Independent website initiations create independent Conversations.
3. Matching name, email or phone does not merge or reuse a Conversation.
4. An authenticated registered customer's CustomerContext may be bound as participant identity without making its email address identity authority.
5. A guest reply endpoint is unverified communication data, not authentication or protected-access authority.
6. Website creation is atomic and idempotent across Conversation plus first Message.
7. Merchant outbound Message truth precedes provider delivery state.
8. Delivery failure cannot erase a canonical Message.
9. Inbound email append requires deterministic Conversation-bound correlation.
10. Subject lines and RFC thread headers cannot independently select a Conversation.
11. Ambiguous correlation is unresolved/rejected, never guessed.
12. Reply-route possession alone grants no browser transcript or source-record access.
13. The initial portfolio has no generic inbound mailbox semantics.
14. The initial portfolio has no SMS, social or automatic channel fallback.
15. The initial portfolio has no Message attachments.
16. The initial portfolio grants no autonomous customer-service response authority.
17. Registered-customer browser continuation and guest browser continuation remain distinct access problems under DQ-003.
18. Provider implementation detail remains below provider-neutral Conversation semantics.

## 18. Alternatives and trade-offs

**Rejected: website-only messaging.** It cannot preserve ordinary asynchronous continuity after a customer leaves the browser unless DQ-003 is resolved immediately. Conversation-bound email provides a smaller first continuation path.

**Rejected: generic email inbox ingestion.** Arbitrary inbound email would require materially broader sender identity, mailbox, routing, spam, thread-correlation and Conversation-creation authority and risks turning provider email semantics into canonical business semantics.

**Rejected: SMS in the initial portfolio.** It adds provider, cost, endpoint, regulatory and abuse surface without being necessary to establish the first viable asynchronous continuity path.

**Rejected: WhatsApp/social messaging in the initial portfolio.** These add provider-specific identity, policy, lifecycle and routing semantics that are not required for the first production slice.

**Rejected: mandatory email verification before every public website message.** It imposes disproportionate friction for ordinary merchant contact. The safer architectural response is to retain the endpoint as explicitly unverified and prohibit it from becoming identity or protected-access authority.

**Chosen:** website initiation plus Conversation-bound email continuation, with authenticated CustomerContext binding when a registered signed-in customer is already known.

The trade-off is deliberate: registered customers can be identified strongly, but persistent browser transcript/resume is not silently bundled into this portfolio; guest continuation remains email-based until DQ-003 deliberately resolves access semantics.

## 19. Falsification and ambiguity review

| Counterexample | Result under the accepted portfolio |
|---|---|
| Browser retries the same submit after uncertain response | Same logical operation converges; no duplicate Conversation/first Message |
| Customer closes browser immediately after sending | Conversation survives; merchant can respond through accepted email continuation when a valid reply endpoint exists |
| Same guest email contacts merchant again later | New independent Conversation; no merge/reuse inferred |
| Signed-in registered customer sends a new message | New Conversation binds the authenticated CustomerContext as participant identity |
| Registered customer supplies a different reply email | CustomerContext remains identity; endpoint is transport only and cannot overwrite/merge identity |
| Customer replies to valid Conversation-bound email | Deterministic route may append to that exact Conversation |
| Email is forwarded and reply comes from incompatible endpoint | No silent append; reject/isolate/unresolved according to policy |
| Customer edits subject or provider omits advisory thread headers | Subject/header text is not authority; valid opaque route remains the binding evidence where other requirements pass |
| Opaque reply route leaks | Possession alone grants no browser/source read authority |
| Provider is unavailable after Message acceptance | Canonical Message survives; delivery state remains pending/failed/unknown truthfully |
| Email contains text and attachment | Supported text may continue; attachment does not become a Message attachment |
| Email contains attachment only | No fabricated empty successful Message |
| Provider sends bounce/automatic-response loop | Protection controls prevent it becoming an ordinary customer Message merely by repetition |
| Bot floods website form | Abuse controls apply before unbounded canonical proliferation |
| Guest mistypes reply email | Residual misdelivery risk remains; endpoint is explicitly unverified and cannot authorise protected disclosure |
| Merchant disables public messaging | New website initiation stops; no invented Conversation closure or history deletion |
| Correlation evidence conflicts | Main Street does not guess a Conversation |

The registered-customer case falsifies any assumption that all website customers should be reduced to an unverified email endpoint. The corrected model binds authenticated CustomerContext when present while preserving email as transport.

The guest case falsifies the opposite assumption that possession of an email route should imply browser access. DQ-003 remains separate.

**Review conclusion:** The selected portfolio is the smallest channel set that preserves public website initiation plus asynchronous two-way continuity while maintaining provider neutrality and identity boundaries.

**Ambiguity-review conclusion:** Conversation creation, authenticated participant binding, guest endpoint status, email correlation, provider evidence, delivery distinction, abuse/degradation and DQ boundaries are explicit. No generic mailbox, attachment, guest browser or autonomous-response semantics are implied.

## 20. Residual risk

A guest may mistype the reply email endpoint, causing a merchant response to be delivered to the wrong address or become undeliverable.

Mitigation SHALL preserve all of the following:

- the endpoint remains explicitly unverified;
- the customer-facing surface should permit inspection/correction before submission;
- endpoint possession alone never grants protected Conversation or source access; and
- stronger current authorization remains required before protected data is disclosed.

Mandatory verification before ordinary public contact is not selected because the friction is disproportionate to the contact use case and would incorrectly encourage treating endpoint verification as complete customer identity authority.

## 21. Governance and implementation consequence

This approved amendment:

- resolves `MS-PROT-086-DQ-001`;
- selects Merchant Website Messaging and Conversation-Bound Email as the exact initial production Customer Messaging channel families;
- establishes `WEBSITE_MESSAGE_CREATE_V1` and `EMAIL_REPLY_CONTINUE_V1` as the initial bounded creation/continuity contracts;
- establishes authenticated CustomerContext participant binding when a registered customer is already signed in;
- preserves guest email as unverified transport data;
- preserves `MS-PROT-086-DQ-002` as unresolved;
- preserves `MS-PROT-086-DQ-003` as unresolved while requiring its later access design to distinguish authenticated registered customers from guests;
- preserves `MS-PROT-086-DQ-004` as unresolved;
- requires updates to the Authority Index, Deferred Decision Register and relevant canonical terminology;
- requires an update to `SEQUENCE.md`'s current status overlay; and
- does not reprioritise or activate production implementation.

Acceptance does not activate messaging for a merchant, implement or deploy a channel, select an email provider, authorise SMS/social channels, authorise Message attachments, grant guest transcript access or authorise autonomous customer-service replies.

**Fundamental Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED — 8 September 2026  
**Repository formalisation:** AUTHORISED  
**Implementation promotion:** NONE
