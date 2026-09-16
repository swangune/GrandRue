# MS-PROT-085 — Merchant Attention, Work Handling and Governed Action Handoff Model

**Document ID:** MS-PROT-085  
**Version:** 1.0  
**Status:** ACCEPTED  
**Approved:** 8 September 2026 by explicit manual approval  
**Authority type:** Cross-capability Merchant Attention semantic/design authority  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`

**Depends on:** composite MS-PROT-027; composite MS-PROT-043 through v1.4; composite MS-PROT-053; composite MS-PROT-057; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-065; composite MS-PROT-068; composite MS-PROT-069; MS-PROT-070; MS-PROT-072; composite MS-PROT-074; composite MS-PROT-075; MS-PROT-082; and MS-PROT-083.

**Amends:** None  
**Supersedes:** None

**Resolves:** `MS-PROT-043-V14-DQ-005` — exact Merchant Attention persistence/model beyond minimal projection; `MS-PROT-083-DQ-008` — registered Business Insight/Business Recommendation integration with Merchant Attention.

**Does not resolve:** `MS-PROT-083-DQ-006` — recommendation prioritisation; `MS-PROT-083-DQ-009` — merchant analytical surface.

**Preserves:** source-capability business-truth ownership; Notification ownership; Business Intelligence ownership; Operational Health and Operational Alert ownership; Reconciliation ownership; Projection/Exposure authority; authorisation, entitlement, eligibility and provider-readiness boundaries; Audit authority; AI non-authority; provider neutrality; and the modular-monolith capability architecture.

**Purpose:** Define the minimum sufficiently expressive Merchant Attention model by which Main Street identifies, presents and records handling of business situations requiring human attention, while preserving the source owner's business truth and requiring a new authorised instruction before any candidate action becomes executable.

---

# 0. Fundamental Vision Conformance

## 0.1 Outcome

**VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

Merchant Attention directly supports exception-driven, role-native operation and administrative compression. It lets a merchant or eligible worker ask:

> **What needs my attention, why, by when, and what can I do?**

without requiring them to discover and administer Main Street through internal capability modules.

The internal distinction between source business truth, attention handling, notification, operational alerting, analytical recommendation and executable action is necessary complexity. It prevents a convenient attention surface from becoming a second source of business truth, a hidden workflow engine or an unsafe command dispatcher.

That complexity SHALL remain primarily inside Main Street.

## 0.2 Feature Admission

Merchant Attention satisfies:

- the **Coordination Test**, because already-authoritative capabilities need one governed way to expose matters requiring handling without sharing mutation authority; and
- the **Administrative-Compression Test**, because target merchants should not need to inspect many capability modules to discover exceptions, decisions or unresolved work.

It does not justify a general-purpose work-management product.

## 0.3 Low-Software-Capacity Merchant Outcome

A sole trader SHALL be able to use Merchant Attention without configuring queues, workflows, priorities, routing rules, ticket states or automation graphs.

Richer assignment and delegation MAY be available where the merchant's accepted workforce and authorisation model supports them, but those mechanics MUST NOT become mandatory administration for simple businesses.

---

# 1. Problem

Main Street already has distinct authoritative facts that may require a person to notice, decide or act. Examples include:

- a new Enquiry or inbound customer message;
- an owner-qualified Reconciliation requiring evidence or intervention;
- a current Business Recommendation whose premises remain valid;
- a Regulatory Administrative Requirement;
- a merchant-actionable provider or operational degradation; and
- a capability-owned commitment exception.

Without a governed coordination model, an implementation could:

- treat notifications as work truth;
- treat alert acknowledgement as resolution;
- duplicate one matter across retries or channels;
- invent universal priority scores;
- let assignment grant authority;
- let snooze alter a source deadline;
- execute a suggested action without a new actor instruction;
- present an empty list as proof that the business is safe; or
- grow into a generic ticketing/workflow platform.

---

# 2. Purpose and Scope

Merchant Attention answers only:

```text
Does an accepted source situation require handling?
Who is currently eligible to handle it?
What Attention-owned handling has occurred?
When should it resurface?
What governed action may the actor consider?
```

It coordinates handling around source-owned facts. It does not replace those facts.

---

# 3. Explicit Non-Goals

MS-PROT-085 does not create:

- a universal workflow engine;
- a project-management or task-management platform;
- a CRM or ticketing system;
- a generic case-management engine;
- a universal business-priority score;
- an employee-performance or productivity system;
- an Operational Alert replacement;
- a Notification replacement;
- a Business Recommendation replacement;
- a Reconciliation replacement;
- a command dispatcher;
- a second source of business lifecycle truth; or
- authority for one capability to mutate another capability's facts.

---

# 4. Governing Principle

```text
source owner establishes business meaning
        ↓
accepted Attention Contract qualifies a handling episode
        ↓
Merchant Attention owns handling coordination only
        ↓
actor considers a Candidate Action
        ↓
new actor instruction
        ↓
current runtime authority and source-owner operation
```

Hard distinction:

```text
attention required
        ≠ source fact changed
        ≠ notification delivered
        ≠ actor authorised
        ≠ action instructed
        ≠ action executed
        ≠ source condition resolved
```

---

# 5. Canonical Concepts

## 5.1 Attention Contract

An **Attention Contract** is an immutable, versioned, owner-qualified semantic definition describing when and how one registered source family may contribute a handling episode to Merchant Attention.

## 5.2 Attention Contribution

An **Attention Contribution** is source-owner-qualified evidence or a request for Attention evaluation. It is not sufficient authority by itself to establish or mutate a Merchant Attention Occurrence.

## 5.3 Merchant Attention Occurrence

A **Merchant Attention Occurrence** is a durable Attention-owned fact that one qualifying source episode requires or required handling within one exact Merchant Scope.

## 5.4 Attention Handling Fact

An **Attention Handling Fact** is immutable evidence of an Attention-owned handling operation such as acknowledgement, assignment, snooze or a contract-defined disposition.

## 5.5 Candidate Action

A **Candidate Action** is a safe reference to a registered source-owner operation or an explicitly external handling path that an eligible actor may consider. It is not a command, Merchant Intent, authorisation decision or execution result.

## 5.6 Attention Coverage

**Attention Coverage** records which applicable registered source families were evaluable for one attention observation and with what serviceability outcome.

---

# 6. Semantic Ownership

## 6.1 Source Owner Retains

The source capability retains authority over:

- the source business fact;
- exact source-subject identity;
- source lifecycle and resolution;
- source severity, materiality or deadline where those meanings exist;
- source-specific eligible operations;
- source evidence and provenance; and
- the meaning of whether the underlying condition is satisfied, discharged, completed, corrected or otherwise resolved.

## 6.2 Merchant Attention Owns

Merchant Attention owns only:

- the Merchant Attention Occurrence;
- Attention acknowledgement;
- Attention assignment;
- Attention snooze;
- contract-defined Attention disposition;
- immutable handling history;
- the current Attention handling projection; and
- Attention Coverage.

## 6.3 Preserved Owners

Notification retains delivery/preference/channel evidence. Business Intelligence retains analytical claims and Business Recommendations. Operational Observability retains Operational Health and Operational Alerts. Reconciliation retains the exact reconciliation question and outcome. Audit retains cross-cutting administrative evidence.

No contribution to Merchant Attention transfers those responsibilities.

---

# 7. Attention Contract

Conceptually:

```text
AttentionContract {
    contractFamily
    contractVersion
    sourceOwner
    sourceSubjectType
    applicabilityRule
    contributionAuthority
    activationIdentityRule
    currentnessRule
    recurrenceRule
    eligibleHandlerRule
    allowedHandlingOperations
    handlingSatisfactionRule
    candidateActionDefinitions
    sourceDeadlineAndUrgencySemantics
    snoozePolicy
    detailProjectionRule
    protectionAndExposureRule
    notificationPolicy
    failureClassification
}
```

An Attention Contract SHALL be immutable and versioned. A Merchant Attention Occurrence SHALL preserve the exact contract family/version under which it was established.

No arbitrary source record, event, alert, message, recommendation or AI inference may become Merchant Attention merely because it appears important. The source family requires an accepted registered Attention Contract.

---

# 8. Attention Contribution

An accepted Attention Contribution SHALL preserve:

- exact Merchant Scope;
- source owner;
- exact source-subject reference;
- activation identity evidence;
- applicable source evidence/progress;
- contribution authority;
- governing time/currentness evidence; and
- provenance.

The Attention capability SHALL validate the contribution against the applicable Attention Contract. Event delivery, projection material, provider evidence or AI output alone MUST NOT establish authority.

---

# 9. Occurrence Identity

Logical Merchant Attention Occurrence identity is:

```text
Merchant Scope
+ Attention Contract family
+ source owner
+ exact source subject
+ activation identity
```

Repeated delivery of the same logical contribution SHALL converge on the same occurrence.

The following are not sufficient identity:

- similar text;
- equal monetary amount;
- equal customer or worker name;
- equal deadline;
- equal category;
- equal recommendation wording; or
- delivery/event/attempt identity alone.

---

# 10. Recurrence and Reactivation

The Attention Contract SHALL distinguish retry/redelivery from a genuinely new handling episode.

```text
same qualifying episode
    → same activation identity
    → same Merchant Attention Occurrence

condition ceased or handling episode ended
+ new qualifying episode
    → new activation identity
    → new Merchant Attention Occurrence
```

Canonical example:

```text
customer message M1 requires handling
    → occurrence A

M1 handled

customer later sends message M2
    → occurrence B
```

M2 is not a retry of M1 merely because both belong to the same Enquiry or Conversation.

---

# 11. Establishment

A Merchant Attention Occurrence MAY be established only where:

1. an accepted Attention Contract applies;
2. the source owner and source-subject type are registered;
3. Merchant Scope and source identity are valid;
4. a stable activation identity is established;
5. required source evidence is present;
6. the source episode currently qualifies under the captured contract;
7. applicable protection and data-use rules permit the required material; and
8. no occurrence with the same logical identity already exists.

If material establishment evidence is missing, contradictory, stale or unauthorised:

```text
establishment = UNRESOLVED / REJECTED
```

Main Street MUST NOT manufacture a best-effort occurrence.

---

# 12. Durable Representation

The exact physical storage technology remains implementation detail, but the logical durable model SHALL preserve:

```text
MerchantAttentionOccurrence {
    occurrenceIdentity
    MerchantScope
    capturedAttentionContract
    sourceOwner
    sourceSubjectReference
    activationIdentity
    minimalRequiredSourceProvenance
    establishedAt
    handlingFacts[]
    optimisticRevision
}
```

Current handling state SHALL be derived from immutable occurrence and handling facts. An implementation MUST NOT erase handling history by overwriting a single mutable status field.

No generic serialized source payload is required. Source detail remains source-owned and is read through governed Projection/Exposure or another accepted owner-qualified contract.

---

# 13. Current Handling Outcomes

The current Attention handling projection SHALL distinguish outcomes equivalent to:

```text
REQUIRES_HANDLING
SNOOZED
HANDLING_SATISFIED
NO_LONGER_APPLICABLE
UNRESOLVED
```

These are Attention outcomes only.

They MUST NOT be interpreted as the source business object's lifecycle state.

`UNRESOLVED` applies where current handling meaning cannot be established safely, including stale contract/source evidence, contradictory handling facts, unavailable required evaluation or authority failure.

---

# 14. Acknowledgement

Attention acknowledgement means only:

```text
an eligible actor records awareness of this Merchant Attention Occurrence
```

It does not mean:

- the source condition is resolved;
- the actor accepts liability or responsibility;
- a payment, refund or other obligation is authorised;
- a Reconciliation is complete;
- an Operational Alert is cleared;
- a Business Recommendation is accepted;
- a Candidate Action is instructed; or
- a Notification was delivered, opened or read.

Acknowledgement SHALL record the actor, governing time, exact occurrence/revision and provenance.

---

# 15. Assignment

Assignment routes handling responsibility within Merchant Attention.

It does not:

- grant access;
- create workforce membership;
- create a Role;
- change source ownership;
- authorise a Candidate Action; or
- guarantee that the assignee remains eligible.

Assignment requires current actor authority and a handler eligible under the Attention Contract.

On observation and before consequential handling, Main Street SHALL re-evaluate current handler authority. A stale assignment is retained historically but is ineffective. The occurrence SHALL return to an eligible pool or become `UNRESOLVED` according to the contract; it MUST NOT remain actionable merely because an earlier assignment existed.

---

# 16. Snooze

Snooze postpones ordinary resurfacing of an Attention occurrence. It does not change:

- a source deadline;
- an obligation due time;
- regulatory timing;
- source lifecycle;
- source currentness;
- operational severity; or
- underlying business risk.

An Attention Contract that permits snooze SHALL define:

- who may snooze;
- permitted bounds;
- whether and how source change wakes the occurrence;
- whether escalation remains visible; and
- expiry behaviour.

Snooze MUST NOT suppress a contract-defined escalation or deadline consequence that is required to remain visible.

---

# 17. Disposition

An Attention Contract MAY define a closed vocabulary of Attention-only dispositions where the source semantics justify them.

A disposition SHALL record actor, reason category where required, governing time, exact occurrence/revision and provenance.

Free-text dismissal alone MUST NOT satisfy consequential work, discharge an obligation, resolve a source condition or override a required escalation.

---

# 18. Source Resolution and Handling Satisfaction

Hard distinction:

```text
source condition resolved
    ≠ Attention handling satisfied
```

An Attention Contract SHALL define what source evidence or Attention-owned handling is sufficient for `HANDLING_SATISFIED` or `NO_LONGER_APPLICABLE`.

A source may resolve independently of the Attention surface. Merchant Attention then records or derives the applicable Attention consequence without claiming ownership of the resolution.

Conversely, acknowledgement, assignment, snooze or a permitted disposition MAY satisfy the handling contract while leaving the source condition unchanged where that is the accepted meaning.

---

# 19. Candidate Actions and Governed Handoff

A Candidate Action SHALL identify either:

- an accepted registered source-owner operation; or
- an explicitly described external handling path where Main Street does not own execution.

Selecting or accepting a Candidate Action is not execution. It begins a new instruction flow:

```text
Candidate Action
        ↓
new actor instruction / Merchant Intent
        ↓
current authentication and Actor Authorisation
        ↓
current Commercial Entitlement where applicable
        ↓
current source state and Operational Eligibility
        ↓
current Provider Readiness where applicable
        ↓
source-owner operation
        ↓
authoritative result / accepted uncertainty
```

Candidate Action material MUST NOT contain authority that bypasses current revalidation. Attention cannot execute by mutating source data directly.

---

# 20. Business Intelligence Integration

A Business Insight or Business Recommendation MAY contribute to Merchant Attention only where its accepted Recommendation Definition or other analytical definition identifies:

- an applicable Attention Contract;
- sufficient evidence coverage;
- a current Recommendation Premise Assessment;
- a stable activation identity;

- a currently accessible intended handler class; and
- why human handling is warranted.

Only `PREMISES_CURRENT`, or semantically equivalent accepted currentness, may support a current Candidate Action.

```text
PREMISES_STALE / PREMISES_UNRESOLVED
    → no current executable implication
```

Merchant Attention MAY retain historical evidence that a recommendation formerly required handling, but it MUST NOT present stale analytical premises as a current action.

MS-PROT-085 resolves the architecture of registered BI-to-Attention contribution and therefore resolves `MS-PROT-083-DQ-008`.

It does not resolve recommendation ranking/materiality ordering under `MS-PROT-083-DQ-006` or the complete merchant analytical surface under `MS-PROT-083-DQ-009`.

---

# 21. Operational Alert Integration

An Operational Alert MAY contribute to Merchant Attention only where:

- the merchant is materially affected;
- an eligible merchant actor can take a meaningful action or decision;
- a safe merchant-facing projection exists;
- internal infrastructure/security/other-merchant detail is excluded;
- an accepted Attention Contract applies; and
- the contribution does not misrepresent Provider Health, Provider Readiness, execution outcome or business truth.

Operational Alert severity or existence does not automatically create merchant-facing Attention.

Attention acknowledgement does not acknowledge, clear or resolve the Operational Alert unless the Operational Observability owner separately defines and accepts that consequence.

---

# 22. Reconciliation Integration

A Reconciliation MAY contribute to Merchant Attention only where its owner-qualified contract requires merchant or authorised staff evidence, decision or intervention.

The contribution SHALL preserve the exact reconciliation subject and question without transferring Reconciliation ownership.

Acknowledging, assigning, snoozing or disposing of the Attention occurrence MUST NOT complete the Reconciliation. Reconciliation completes only under its accepted evidence and owner-operation semantics.

---

# 23. Notification Relationship

Notification MAY communicate that Attention exists, has changed or is approaching a contract-defined resurfacing/escalation point.

Hard distinctions:

```text
Notification accepted / delivered / opened / read
    ≠ Attention acknowledged
    ≠ Attention handling satisfied
    ≠ source condition resolved
```

Notification-channel failure MUST NOT erase, complete or silently hide the durable in-product Merchant Attention Occurrence.

Notification preference and delivery semantics remain owned by composite MS-PROT-075.

---

# 24. Observation, Protection and Exposure

An actor may observe a Merchant Attention Occurrence only where:

- Merchant Scope is current and exact;
- the actor is currently an eligible handler or otherwise has accepted observation authority;
- current source access permits the projected detail;
- the occurrence is serviceable;
- applicable data-use and protection rules permit the use; and
- Projection/Exposure permits the representation for the exact audience and surface.

Occurrence existence MUST NOT leak protected source existence or detail. A safe Attention summary may expose less than the source-owner detail projection.

Assignment does not override these checks.

---

# 25. Attention Coverage and Honest Empty State

Attention observation SHALL preserve coverage outcomes equivalent to:

```text
SERVICEABLE
DEGRADED
UNAVAILABLE
NOT_APPLICABLE
```

Coverage is source-family and observation-qualified. It is not a claim that all possible business concerns are known.

An empty visible list means only:

```text
no visible current Merchant Attention Occurrences
from the applicable registered source portfolio
that was serviceable for this observation
```

It MUST NOT mean:

- the business is safe;
- every capability was checked;
- no obligations or deadlines exist;
- no operational degradation exists;
- no customer is waiting;
- all recommendations are current; or
- no unresolved evidence exists.

Where material applicable coverage is degraded or unavailable, the surface SHALL preserve that uncertainty rather than display an unqualified reassuring empty state.

---

# 26. Presentation Semantics

The role-native presentation SHOULD answer, where established:

```text
What needs handling?
Why does it need handling?
By when?
Who is handling it?
What may I consider doing?
What is uncertain or unavailable?
```

Presentation SHALL use business language and progressive disclosure. Internal contract identifiers, capability modules, event mechanics and infrastructure terms SHOULD remain hidden from ordinary users.

Main Street MUST NOT fabricate urgency, certainty, priority, deadlines, severity or recommended action.

MS-PROT-085 does not establish a universal numeric priority or a complete screen/dashboard layout.

---

# 27. Sole-Trader and Small-Team Operation

For a sole trader, Merchant Attention MAY present one role-native list without assignment configuration.

For a small team, contract-defined eligible-handler groups and explicit assignment MAY be used where workforce membership, Role and source access are already established.

The design MUST NOT require every merchant to model departments, queues, service-level agreements, ticket types or workflow states.

---

# 28. Ordinary-Staff Boundary

Ordinary staff SHALL see only attention relevant to their real business role and current authority.

They SHOULD NOT need training in Merchant Attention lifecycle vocabulary. User interaction may use business language such as:

```text
Needs your reply
Assigned to you
Remind me tomorrow
Handled
```

while the authoritative model preserves the exact semantics defined here.

No Attention handling outcome may be used as an employee-performance judgement without separate accepted authority.

---

# 29. Entitlement and Progressive Capability

Commercial Entitlement MAY govern optional richer Attention features such as team assignment or advanced routing.

Entitlement change MUST NOT:

- erase source facts;
- erase Merchant Attention Occurrences or handling history;
- manufacture source resolution;
- transfer ownership; or
- make an unauthorised actor eligible.

Loss of a richer feature SHALL degrade conservatively to the simpler supported handling surface or an explicit unavailable outcome.

---

# 30. Source Commit and Durable Reaction

Where Attention establishment follows a source mutation:

```text
source transaction commits authoritative source fact
        ↓
durable owner-qualified post-commit reaction
        ↓
Attention Contribution evaluation
        ↓
idempotent Merchant Attention Occurrence establishment
```

The source fact MUST survive Attention failure. Attention failure MUST NOT roll back or falsify an already committed source fact.

The affected Attention Coverage SHALL remain degraded/unavailable until convergence is restored. The system MUST NOT present false completeness during the gap.

---

# 31. Idempotency and Concurrency

Occurrence establishment, acknowledgement, assignment, snooze and disposition SHALL have stable logical operation identity and exact Merchant Scope/occurrence affinity.

Duplicate delivery or retry with the same logical intent SHALL converge. Reuse of the same idempotency identity for materially different intent SHALL conflict.

Consequential handling operations SHALL use optimistic concurrency or an equivalently strong current-revision check.

Concurrent incompatible handling operations MUST NOT silently use last-write-wins. The system SHALL preserve an explicit conflict or unresolved outcome and retain attempted-operation evidence where required by Audit.

---

# 32. Failure Classification

Merchant Attention SHALL distinguish at least:

```text
SOURCE_UNAVAILABLE
SOURCE_EVIDENCE_STALE
CONTRACT_UNAVAILABLE_OR_UNSUPPORTED
CONTRIBUTION_INVALID
OCCURRENCE_CONFLICT
HANDLER_AUTHORITY_STALE
PROJECTION_OR_EXPOSURE_UNAVAILABLE
HANDLING_CONCURRENCY_CONFLICT
CANDIDATE_ACTION_UNAVAILABLE
OUTCOME_UNCERTAIN
```

Equivalent owner-qualified closed classifications MAY be used.

Failure MUST NOT be converted into false absence, false completion, invented source state or automatic action.

---

# 33. Historical Affinity

A Merchant Attention Occurrence and each Attention Handling Fact SHALL preserve the contract, source reference, activation identity, actor authority/provenance and governing time applicable when the fact occurred.

Later contract, Role, entitlement, source-state or provider changes MUST NOT rewrite historical handling meaning.

Current actionability is re-evaluated from current authority; historical truth remains immutable.

---

# 34. Data Protection and Minimisation

Merchant Attention SHALL retain only the source reference, projection material and provenance necessary for handling, audit and accepted lifecycle obligations.

It MUST NOT become a generic duplicate store of source records, message bodies, analytical datasets, operational diagnostics or provider payloads.

Retention, deletion, legal hold, anonymisation, purpose limitation and subject-right consequences remain governed by composite MS-PROT-053 and applicable source authority.

Where source detail becomes unavailable or must be removed, the Attention record MAY retain a minimal lawful historical handling fact without recreating prohibited detail.

---

# 35. Audit and Administrative Intervention

High-consequence assignment, disposition, forced handling repair or administrative intervention SHALL produce MS-PROT-064-governed Audit evidence.

Administrative tooling may repair Attention coordination only through accepted Attention operations. It MUST NOT force source reality, bypass source-owner operations or rewrite immutable handling history.

---

# 36. AI Boundary

AI MAY:

- summarise safe source detail;
- explain why an occurrence requires attention;
- group related visible occurrences without merging identity;
- suggest a Candidate Action already permitted by the Attention Contract; and
- prepare a draft instruction for actor review.

AI MUST NOT:

- invent an occurrence;
- establish source severity, deadline or resolution;
- fabricate coverage;
- acknowledge, assign, snooze, dispose or satisfy handling without authorised actor/system semantics;
- suppress an occurrence;
- create a Candidate Action outside the accepted contract;
- treat a Business Recommendation as authority;
- instruct or execute a source-owner operation; or
- claim that action succeeded.

AI output remains non-authoritative until accepted deterministic semantics establish the applicable fact or instruction.

---

# 37. Business-Type and Provider Neutrality

Merchant Attention SHALL be expressed through registered source and contract semantics, not hard-coded business-type branches.

The same model MAY coordinate attention for a shop, salon, tradesperson, clinic, hospitality business or other supported merchant without assuming one industry's workflow.

Provider-specific alerts, links or evidence MAY contribute only through owner-qualified contracts. A provider does not own Merchant Attention meaning.

---

# 38. Configuration, Compilation and Runtime Boundary

Accepted configuration MAY select or parameterise supported Attention Contracts only through registered semantics and merchant authority.

Compilation/materialisation MAY assemble the exact contract portfolio for an Active Release.

Runtime SHALL:

- resolve the exact applicable contract version;
- validate source contribution and activation identity;
- evaluate current handler authority and source currentness;
- persist immutable handling facts;
- preserve coverage; and
- hand Candidate Actions back to accepted source-owner operations.

Configuration, compiler materialisation, AI interpretation, provider data or presentation code MUST NOT invent new Attention semantics at runtime.

---

# 39. Initial Contract Portfolio Deferred Decision

## MS-PROT-085-DQ-001 — Initial Attention Contract Portfolio

**Status:** DEFERRED — ACTIVE BEFORE PRODUCTION MERCHANT ATTENTION  
**Owner:** Merchant Attention semantic/product design composed with each source owner  
**Revisit condition:** Before any production Merchant Attention source family is activated.

MS-PROT-085 establishes the generic contract and runtime semantics but does not activate a production source portfolio.

Candidate source families requiring separate portfolio selection and exact owner-qualified contracts include:

- Enquiry and inbound customer communication;
- owner-qualified Reconciliation;
- current Business Recommendations;
- Regulatory Administrative Requirements;
- merchant-actionable provider/operational degradation; and
- capability-owned commitment exceptions.

Listing a candidate does not activate it, reserve its presentation, establish a priority or imply that every instance requires Attention.

---

# 40. Hard Invariants

## INV-085-001 — Source Ownership Is Preserved

Merchant Attention MUST NOT own or mutate the source business fact, source lifecycle or source resolution.

## INV-085-002 — Stable Occurrence Identity

Every Merchant Attention Occurrence SHALL have exact Merchant Scope, Attention Contract family, source owner, source subject and activation identity.

## INV-085-003 — Recurrence Is Not Retry

A new qualifying source episode MUST NOT be collapsed into a prior occurrence merely because it concerns the same source aggregate; duplicate delivery MUST NOT create a new occurrence.

## INV-085-004 — Handling Is Not Source Resolution

Attention acknowledgement, assignment, snooze, disposition or satisfaction MUST NOT imply source resolution unless the source owner separately establishes that consequence.

## INV-085-005 — Notification Is Not Handling

Notification acceptance, delivery, open or read evidence MUST NOT establish Attention acknowledgement or handling satisfaction.

## INV-085-006 — Assignment Is Not Authority

Attention assignment MUST NOT grant access, authorisation, entitlement, eligibility or source-operation authority.

## INV-085-007 — Snooze Does Not Change Source Time

Snooze MUST NOT alter a source deadline, due time, obligation, currentness or required escalation.

## INV-085-008 — Candidate Action Is Not Command

A Candidate Action MUST NOT execute or bypass a new instruction and current runtime authority checks.

## INV-085-009 — Stale Recommendation Is Not Current Action

A Business Recommendation with stale or unresolved premises MUST NOT be presented as a current Candidate Action.

## INV-085-010 — Operational Alert Is Not Automatic Merchant Attention

Operational Alert existence or severity alone MUST NOT create merchant-facing Attention.

## INV-085-011 — Empty State Is Coverage-Qualified

An empty Attention result MUST NOT imply business safety or complete source coverage.

## INV-085-012 — Current Handler Authority

Observation and consequential handling SHALL require current eligible-handler authority; historical assignment is insufficient.

## INV-085-013 — Cross-Merchant Isolation

Occurrence identity, contribution, handling, projection and Candidate Action SHALL remain affined to one exact Merchant Scope.

## INV-085-014 — Immutable Handling Evidence

Attention handling history SHALL be append-only or equivalently immutable; current projection MUST NOT erase prior handling facts.

## INV-085-015 — AI Non-Authority

AI MUST NOT establish, clear, assign, suppress or execute Merchant Attention authority.

## INV-085-016 — No Universal Workflow Platform

Merchant Attention MUST remain a bounded exception-handling coordination capability and MUST NOT become a generic workflow, ticketing or task platform.

---

# 41. Alternatives and Trade-Offs

## 41.1 Rejected — Collect Alerts and Allow Dismissal

This is superficially simple but fails identity, recurrence, source-resolution separation, coverage, current authority and action-safety requirements.

## 41.2 Rejected — Notification Inbox as Attention

Delivery evidence is not business handling, source resolution or current actionability. Channel failure would also make the business-operational surface untrustworthy.

## 41.3 Rejected — Generic Workflow / Ticket Engine

It would expose software administration, invite duplicated source lifecycles and exceed the minimum depth required by target merchants.

## 41.4 Rejected — Business Intelligence Owns Attention

Not all handling needs are analytical, and Business Recommendations remain non-authoritative. This would collapse source facts, analytical interpretation and human handling.

## 41.5 Accepted Trade-Off

The accepted model adds explicit contracts, durable occurrence identity, immutable handling facts and coverage qualification. This increases internal implementation precision but prevents larger merchant-visible workflow administration and protects capability ownership.

---

# 42. Falsification and Ambiguity Review

## 42.1 First Falsification

Initial hypothesis:

```text
collect source alerts
+ display them in one list
+ allow dismiss
```

**Outcome:** FAIL.

Counterexamples showed that the model could:

- duplicate matters on retry;
- collapse later customer messages into old work;
- let dismissal imply source resolution;
- hide source deadlines through snooze;
- keep work assigned after authority ended;
- present stale recommendations as current action;
- claim an empty safe state during source failure;
- lose updates under concurrent handling; and
- accept arbitrary source contributions without a semantic contract.

The design was revised to add stable activation identity, explicit recurrence, source/handling separation, deadline-preserving snooze, current handler revalidation, recommendation-premise validation, coverage, optimistic concurrency and mandatory Attention Contracts.

## 42.2 Second Falsification

The revised model was tested against:

- a sole trader receiving repeated customer messages;
- a small team where an assignee leaves the business;
- a reconciliation needing merchant evidence;
- an operational provider incident containing unsafe internal detail;
- a recommendation whose premises become stale;
- notification failure;
- source-commit success followed by Attention-reaction failure;
- retry/redelivery and concurrent handling; and
- entitlement downgrade.

**Outcome:** PASS.

No scenario required Merchant Attention to own source truth, grant authority, execute actions or expose a general workflow platform.

## 42.3 Ambiguity Review

The final proposal was checked for ambiguity across:

- source truth versus handling state;
- occurrence identity versus recurrence;
- acknowledgement versus notification read;
- assignment versus authority;
- snooze versus source time;
- handling satisfaction versus source resolution;
- Candidate Action versus command;
- Business Recommendation versus current action;
- Operational Alert versus merchant-facing Attention;
- visible absence versus coverage;
- immutable history versus current projection; and
- generic semantics versus the initial production portfolio.

**Outcome:** PASS.

---

# 43. Acceptance and Governance Outcome

## 43.1 Acceptance Criteria

MS-PROT-085 is acceptable only if:

- source capabilities retain business truth and resolution ownership;
- only registered owner-qualified Attention Contracts may establish occurrences;
- occurrence identity distinguishes retry from recurrence;
- acknowledgement, assignment, snooze and disposition remain Attention-only facts;
- assignment cannot grant authority;
- snooze cannot alter source deadlines or obligations;
- Candidate Actions require a new instruction and current runtime checks;
- stale or unresolved Business Recommendations cannot be current actions;
- Operational Alerts cannot automatically become merchant-facing Attention;
- Reconciliation cannot be completed by Attention handling alone;
- Notification delivery/read cannot satisfy Attention handling;
- observation is current-authority, protection and Exposure governed;
- empty results are coverage-qualified;
- handling history is immutable and concurrency-safe;
- AI cannot create, clear or execute Attention authority;
- ordinary merchants are not required to configure workflows; and
- the initial production Attention Contract portfolio remains explicitly deferred until separately selected.

## 43.2 Governance Outcome

**Fundamental Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Design completeness:** PASS  
**First falsification:** FAIL → REVISED  
**Second falsification:** PASS  
**Ambiguity review:** PASS  
**Source/cross-capability ownership:** PASS  
**Administrative compression:** PASS  
**Ordinary-staff simplicity:** PASS  
**AI non-authority:** PASS  
**Provider neutrality:** PASS  
**Anti-ERP proportionality:** PASS  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED — 8 September 2026  
**Repository formalisation:** AUTHORISED

## 43.3 Implementation Consequence

Acceptance establishes semantic/design authority only.

It does not:

- activate a production Attention Contract portfolio;
- reprioritise the accepted implementation programme;
- authorise production code, migrations, UI, provider integration or deployment;
- resolve recommendation prioritisation or the merchant analytical surface; or
- repair the separately blocked incomplete MS-PROT-084 base composition.

Implementation SHALL proceed only through `designs/IMPLEMENTATION-RULES.md` after the applicable dependency and sequencing gates are satisfied.

