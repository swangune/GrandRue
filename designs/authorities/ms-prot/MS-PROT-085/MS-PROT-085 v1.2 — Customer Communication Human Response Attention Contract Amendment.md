# MS-PROT-085 v1.2 — Customer Communication Human Response Attention Contract Amendment

**Document ID:** MS-PROT-085  
**Version:** 1.2  
**Status:** ACCEPTED  
**Approved:** 9 September 2026 by explicit manual approval  
**Authority type:** Merchant Attention portfolio-extension amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-085 through v1.1 only by adding one Customer Communication handling source family  
**Depends on:** Composite MS-PROT-085 through v1.1; composite MS-PROT-086 through v1.3; composite MS-PROT-027; composite MS-PROT-053; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-065  
**Implementation activation:** NONE

---

# 1. Governing Decision

The Merchant Attention portfolio SHALL add exactly one Customer Communication contract:

```text
customer-communication / human-response-required@1
```

This is in addition to:

```text
enquiry / initial-submission-review@1
```

selected by MS-PROT-085 v1.1.

The new contract means:

> A specific accepted inbound customer Message has reached a governed customer-service outcome that requires human review or response handling.

It does not mean the customer's underlying business request is approved.

# 2. Source Owner

**Source owner:** Customer Communication.

**Source subject:** Exact accepted inbound `ConversationMessage` plus its exact `CustomerServiceResponseAssessment`.

The Attention system SHALL NOT classify the Message itself.

Customer Communication establishes the qualifying response requirement first.

# 3. Applicability

The Attention contract applies where the exact response assessment produces:

```text
DRAFT_FOR_HUMAN_REVIEW
```

or:

```text
HUMAN_RESPONSE_REQUIRED
```

It also applies where `UNRESOLVED` and Customer Communication can establish that the customer request remains pending human review rather than safely producing `NO_RESPONSE_REQUIRED`.

An automatically answered Message SHALL NOT create an occurrence merely because it was inbound.

# 4. Activation Identity

Logical occurrence identity SHALL include:

```text
Merchant Scope
+
customer-communication / human-response-required@1
+
Customer Communication owner
+
triggering ConversationMessage identity
+
exact response-assessment activation
```

Technical retries converge.

A later distinct inbound customer Message may create its own occurrence.

Presentation MAY group multiple occurrences by Conversation without merging their semantic identity.

# 5. Required Handling

The occurrence remains `REQUIRES_HANDLING` until one of the accepted satisfaction paths occurs.

Initial satisfaction paths are exactly:

```text
HUMAN_RESPONSE_ACCEPTED
HANDLED_OUTSIDE_MAIN_STREET_RECORDED
```

# 6. HUMAN_RESPONSE_ACCEPTED

`HUMAN_RESPONSE_ACCEPTED` means:

> A currently authorised human merchant-side participant accepted an outbound ConversationMessage in response to this handling occurrence.

The exact Message identity SHALL be retained in the handling evidence.

This disposition does not prove external delivery, customer read, customer satisfaction or source business resolution.

# 7. HANDLED_OUTSIDE_MAIN_STREET_RECORDED

This disposition means:

> An eligible merchant-side actor explicitly recorded that the customer matter was handled through another legitimate path.

Examples may include telephone, in-person discussion or another accepted channel.

It does not create a ConversationMessage and does not claim that Main Street observed the external communication.

It is explicit actor evidence, not inference from elapsed time.

# 8. Acknowledgement

Attention acknowledgement SHALL NOT satisfy this contract.

Opening the Conversation SHALL NOT satisfy it.

Reading the Message SHALL NOT satisfy it.

AI summarisation SHALL NOT satisfy it.

# 9. Assignment and Snooze

The initial contract SHALL NOT require assignment or snooze.

Those capabilities remain available only where later contract amendment deliberately activates them for this source family.

A micro/small merchant SHALL not be forced to administer a queue.

# 10. Eligible Handlers

An eligible handler must be a current merchant-side actor who independently has current authority to observe the exact Conversation, observe the required customer Message, perform the chosen handling operation, and respond through Customer Communication where sending a Message.

Assignment, if later added, SHALL NOT grant that authority.

# 11. Candidate Action

The contract MAY expose one Candidate Action equivalent to:

```text
Reply in Conversation
```

This is a reference to the accepted Customer Communication response operation.

It is not an executable command by itself.

Source-object business actions such as Refund, Cancel Booking or Amend Order SHALL NOT become Candidate Actions merely because the customer requested them in text.

# 12. Human Handoff Commitment

Main Street may represent human handoff as successfully established only after this Attention occurrence commits.

Permitted meaning:

```text
passed to the business for review
```

Prohibited inferred meaning includes that the merchant accepted the request, guarantees a response, guarantees a response time, or resolved the issue.

# 13. No Universal Response SLA

This contract establishes no universal response deadline, response-time target, priority, urgency score or service-level agreement.

Where another owner has a real deadline or obligation, that source retains it.

# 14. Automatic Response Failure

Where a Message was initially eligible for automatic response but no conforming response can be accepted because generation or deterministic validation fails, Customer Communication MAY contribute to this human-response contract once the automated path is no longer safely able to complete.

AI-provider failure itself is not Attention authority.

The Customer Communication-owned failure/handoff decision is.

# 15. Response Acceptance Reaction

Where an eligible human responds through the Attention interaction:

```text
human instruction
    ↓
Customer Communication accepts outbound Message
    ↓
durable response evidence
    ↓
Attention handling reaction
    ↓
HUMAN_RESPONSE_ACCEPTED
```

Failure of the Attention reaction SHALL NOT erase the accepted Message.

The reaction SHALL retry idempotently until handling evidence converges or becomes operationally unresolved.

# 16. External Delivery

`HUMAN_RESPONSE_ACCEPTED` means a Message was accepted by Customer Communication.

It does not mean delivered.

Notification remains the delivery owner.

The merchant surface SHALL preserve any relevant delivery failure separately.

# 17. Deactivation

Deactivating automated customer service prevents new automated assessments according to its governing configuration.

It SHALL NOT delete existing human-response occurrences, erase handling facts, erase Messages or manufacture satisfaction.

Existing occurrences remain governed by residual access/lifecycle rules.

# 18. Data Lifecycle

Attention records retain only the minimum source provenance required for handling truth.

The full Conversation SHALL not be duplicated into Merchant Attention.

Retention and disposition remain governed by MS-PROT-053.

# 19. Hard Invariants

1. Customer Communication owns the determination that a response requires a person.
2. Merchant Attention owns only human-handling coordination.
3. The underlying business capability retains business truth and operation authority.
4. One qualifying triggering Message/assessment produces at most one logical human-response occurrence for the same activation.
5. Retries converge; a later distinct customer Message may create a new occurrence.
6. An automatically answered Message does not create an occurrence merely because it was inbound.
7. Opening, reading, acknowledgement and AI summarisation do not satisfy handling.
8. `HUMAN_RESPONSE_ACCEPTED` requires an accepted outbound human ConversationMessage.
9. Human response acceptance does not prove delivery, read, satisfaction or source resolution.
10. `HANDLED_OUTSIDE_MAIN_STREET_RECORDED` requires explicit eligible-actor evidence.
11. Assignment does not grant authority.
12. The only initial Candidate Action is equivalent to `Reply in Conversation`.
13. Customer-requested Refund, Cancel Booking, Amend Order or similar source operations do not become Attention commands.
14. Human handoff may be claimed only after durable Attention establishment.
15. No universal response SLA is created.
16. AI-provider failure alone is not Attention authority.
17. Attention reaction failure after Message acceptance does not roll back the Message.
18. Notification owns delivery evidence.
19. Deactivation does not erase existing occurrence or handling history.
20. Merchant Attention does not duplicate the full Conversation.
21. No implementation is activated by acceptance.

# 20. Falsification and Ambiguity Review

The contract passes the following required scenarios:

- safely auto-answered public opening-time question → no human occurrence;
- explicit request for a person → one occurrence;
- refund-approval request → one occurrence and no refund authority;
- repeated delivery of the same qualifying event → one occurrence;
- second distinct unresolved customer request → new occurrence;
- merchant opens Message → still requires handling;
- merchant replies through Conversation → handling may become `HUMAN_RESPONSE_ACCEPTED` only after Message acceptance;
- Notification later fails → accepted human Message remains while delivery is separately represented;
- merchant handles the matter by telephone → explicit `HANDLED_OUTSIDE_MAIN_STREET_RECORDED` may satisfy;
- AI claims the issue is handled → no satisfaction;
- merchant lacks current response authority → handling operation is rejected/withheld;
- Attention reaction fails after human Message commit → Message survives and handling convergence retries;
- low-software-capacity merchant → sees a customer matter requiring response rather than a ticket-workflow configuration surface.

**Falsification result:** PASS.

Ambiguity is closed between response requirement, Attention handling, Message acceptance, external delivery, source business resolution and customer satisfaction. None implies another.

# 21. Fundamental Vision Conformance

**VISION-CONFORMING**

The contract turns automation failure, judgement and customer escalation into exception-driven merchant work without forcing queue administration.

It creates exactly the human escape path necessary for safe automation.

# 22. Architecture Review, Governance and Implementation Consequence

**Architecture review:** PASS. Customer Communication owns the determination that a response requires a person. Merchant Attention owns only human-handling coordination. The business capability mentioned by the customer continues to own its own truth and operations.

**Recommendation:** ACCEPT.  
**Manual approval:** GRANTED — 9 September 2026.  
**Repository formalisation:** AUTHORISED.  
**Implementation promotion:** NONE.

Acceptance adds exactly `customer-communication / human-response-required@1` to the accepted Merchant Attention portfolio alongside `enquiry / initial-submission-review@1`.

Acceptance does not implement automated customer service, activate this contract for any merchant, implement Attention code, create APIs, enable assignment/snooze, create response SLAs or change the current production implementation sequence.