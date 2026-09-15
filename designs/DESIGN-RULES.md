# Main Street Design Rules

**Document ID:** MS-DESIGN-RULES-001  
**Version:** 2.4
**Status:** Accepted  
**Applies from:** 5 September 2026  
**Governed by:** `DOCUMENT-GOVERNANCE.md`  
**Fundamental product-purpose authority:** `MS-FUNDAMENTAL-VISION-001` — `docs/foundation/Fundamental-Vision-Mission-and-Product-Constitution.md`  
**Purpose:** Define the mandatory rules for proposing, evaluating, falsifying, presenting, approving, formalising and maintaining Main Street design decisions, including Fundamental Vision Conformance, strict pre-approval repository isolation, established MS-PROT document-format requirements, normative-writing rules and ambiguity-elimination requirements.

---

# Part I — Core Design Governance

## 0. Fundamental Vision Conformance Authority

Main Street design MUST conform to the accepted `MS-FUNDAMENTAL-VISION-001 — Main Street Fundamental Vision, Mission and Product Constitution`.

The Fundamental Vision defines **why Main Street exists**. Design authorities define **how Main Street fulfils that purpose**.

Therefore:

```text
Fundamental Vision / Mission
        ↓
Product Philosophy
        ↓
Design / Architecture / AI Principles
        ↓
Product Requirements
        ↓
MS-PROT / TAS / ADR
        ↓
Implementation Governance
        ↓
Implementation
```

A lower-level authority MUST NOT silently contradict an applicable higher-level product-purpose constraint.

Technical correctness is insufficient where the resulting design materially undermines Main Street's fundamental mission.

### 0.1 Mandatory Vision Conformance Gate

Before ordinary architectural review, every material proposal MUST be classified through Fundamental Vision Conformance.

The gate MUST test the following.

#### 0.1.1 Business-to-Software Translation

Does the design allow Main Street to absorb software interpretation/configuration work that would otherwise fall on the merchant?

The design SHOULD move toward:

```text
merchant describes business reality
        ↓
Main Street translates
        ↓
registered supported semantics
        ↓
approved operating configuration
```

It SHOULD NOT unnecessarily require merchants to understand software categories or Main Street's internal architecture.

#### 0.1.2 Administrative Compression

Does the proposal reduce or increase merchant software administration?

Review MUST consider effects on:

- configuration burden;
- duplicate data entry;
- manual reconciliation;
- routine maintenance;
- cross-system coordination;
- software concepts the merchant must understand; and
- recurring administrative operations.

A design that materially increases these burdens MUST demonstrate why the complexity is intrinsic to the business, safety, security, legal requirement or unavoidable correctness constraint.

Implementation convenience alone is insufficient justification.

#### 0.1.3 Ordinary-Staff Training

For routine staff operations, ask:

> Would an ordinary worker need formal Main Street product training to use this correctly?

If YES, the proposal is presumptively non-conforming.

It MAY survive only where the interaction complexity is demonstrably intrinsic to:

```text
the worker's actual job
merchant operating policy
legal/compliance obligation
safety/security requirement
```

rather than Main Street's software structure.

Canonical:

> **Staff learn their job and the business. They should not need to learn Main Street.**

#### 0.1.4 Role-Native Operation

Does each actor see the business information, actions and decisions relevant to their role, or does the design expose generic software modules, internal abstractions or unrelated capability administration?

Role-native projection SHOULD be preferred over universal application/module navigation.

#### 0.1.5 Target-Market Proportionality

Is the proposed complexity appropriate for Main Street's target:

```text
micro and small businesses
with genuine operational complexity
but limited software-administration capacity?
```

A design MAY correctly model an enterprise requirement while still being inappropriate for Main Street.

Such scope SHOULD be rejected or deferred rather than accepted merely because the architecture can represent it.

#### 0.1.6 Capability Depth

Where a mature competitor or specialist product already provides similar functionality, ask:

> What minimum depth does Main Street actually require to represent and coordinate the target business correctly?

The goal is NOT:

```text
feature parity
maximum configurability
enterprise depth
```

The goal is:

```text
minimum sufficiently expressive capability
```

#### 0.1.7 Ownership Versus Integration

For each proposed capability, ask:

> Does Main Street need authoritative ownership of this business fact to fulfil its operating role?

If YES:

```text
native capability may be justified
```

If NO:

```text
provider integration SHOULD be preferred
where practical
```

A design MUST NOT reproduce mature external infrastructure merely to increase Main Street's feature count.

#### 0.1.8 Cross-Capability Value

Does the proposal improve Main Street's ability to coordinate already authoritative business facts?

Examples:

```text
workforce absence
        ↓
customer capacity consequence
```

```text
worked-time evidence
        ↓
compensation consequence
```

```text
inventory state
        ↓
fulfilment eligibility
```

Cross-capability coordination is valuable only when ownership remains bounded and explicit.

#### 0.1.9 Exception-Driven Operation

Where practical, does the design reduce routine software administration and surface decision, exception, approval, conflict or risk instead?

Design SHOULD increasingly enable the merchant to ask:

> What needs my attention?

rather than:

> Which software module should I manage?

#### 0.1.10 Business Evolution

Can the capability be introduced progressively as the merchant's business evolves?

Main Street SHOULD prefer:

```text
new business fact
        ↓
candidate capability/configuration change
        ↓
minimal clarification
        ↓
merchant approval
        ↓
activation
```

over forcing merchants to anticipate and configure future complexity during initial onboarding.

### 0.2 Vision-Conformance Outcome

The Vision Conformance Gate MUST produce exactly one of:

```text
VISION-CONFORMING
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
VISION-NON-CONFORMING
VISION-UNRESOLVED
```

`VISION-CONFORMING` means the proposal directly supports the Fundamental Vision.

`VISION-CONFORMING WITH JUSTIFIED COMPLEXITY` means the proposal introduces necessary complexity, but that complexity is intrinsic to correct business representation, safety, security, law, privacy or another accepted invariant. The justification MUST be explicit.

`VISION-NON-CONFORMING` means the proposal materially moves Main Street away from its fundamental purpose. It MUST be rejected or fundamentally revised.

`VISION-UNRESOLVED` means material uncertainty remains about conformity. The proposal MUST NOT proceed to acceptance until resolved.

### 0.3 Competitive Feature Rule

The existence of a feature in Square, Shopify, Zoho, Odoo, Wix, Jobber, vertical SaaS or another competitor is neither evidence for nor against implementing it.

A proposed feature MUST be justified through Main Street's own vision.

Rejected reasoning:

```text
"Competitor X has it."
```

Required reasoning:

```text
"This business operation is materially required
by Main Street's target merchants,
and this is the simplest conforming way
to represent or coordinate it."
```

### 0.4 Feature Admission Rule

A proposed future capability SHOULD satisfy at least one of:

**Representation Test** — Without it, Main Street cannot faithfully represent a material operation of target businesses.

**Coordination Test** — Without it, Main Street cannot correctly coordinate existing authoritative capabilities.

**Administrative-Compression Test** — Without it, target merchants retain significant avoidable recurring administration that Main Street can safely absorb.

A proposal satisfying none of these SHOULD normally be rejected.

### 0.5 Complexity Burden Rule

Main Street MUST absorb more software complexity than it exposes.

Therefore:

```text
internal architectural complexity
        MAY increase

merchant-visible software complexity
        MUST NOT increase proportionally
```

A new capability that requires significant additional merchant configuration or staff training MUST demonstrate proportionate business value and lack of a simpler conforming alternative.

### 0.6 Business Language Rule

Merchant and staff interaction SHOULD use the language of the business rather than Main Street's internal semantic model.

Preferred:

```text
Do workers need to accept offered shifts?
```

Avoid:

```text
Configure Workforce Scheduling Arrangement acceptance mode.
```

Preferred:

```text
Your shift starts at 08:00.
[Clock In]
```

Avoid:

```text
Create Time Capture Event.
```

Internal semantic precision remains mandatory in authoritative design and implementation. It does not need to leak into ordinary user interaction.

### 0.7 Architecture Vision-Conformance Rule

Architecture review MUST evaluate whether the chosen architecture supports:

```text
business-model-first configuration
generic capability composition
progressive activation
administrative compression
role-native projections
provider replaceability
cross-capability coordination
low merchant-visible complexity
```

An architecture that is technically elegant but systematically transfers complexity to merchants is non-conforming.

### 0.8 AI Vision-Conformance Rule

AI design MUST be evaluated not by whether AI is present, but whether AI reduces the translation and administrative burden while preserving deterministic authority.

Canonical:

```text
human business language
        ↓
AI interpretation
        ↓
registered semantics
        ↓
deterministic validation
        ↓
authorised execution
```

AI MUST NOT be introduced merely because AI capability is commercially fashionable.

### 0.9 Fundamental Override Rule

If an otherwise valid proposal materially contradicts the Fundamental Vision:

```text
the proposal changes
```

not:

```text
the vision is silently weakened
```

The Fundamental Vision itself may only be changed through an explicit material governance cycle that identifies that the product's fundamental purpose is being reconsidered.

No MS-PROT, TAS, ADR, PRD, implementation rule or implementation decision may implicitly amend the Fundamental Vision.

---

## 1. Governing Principle

Main Street design begins with the accepted Fundamental Vision and the target-business reality that must be represented, and proceeds toward implementation. Technology, patterns, abstractions and data structures are selected because they fit the domain and its constraints; the domain is not reshaped merely to justify a preferred technical mechanism.

```text
Fundamental Main Street Vision
        ↓
Target-business reality / business need
        ↓
VISION CONFORMANCE
        ↓
Business rules and invariants
        ↓
Workflows and capability responsibilities
        ↓
Semantic/configuration model
        ↓
Application and platform responsibilities
        ↓
Architecture
        ↓
Technology
        ↓
Implementation
```

Business reality remains authoritative for understanding the merchant domain. The Fundamental Vision determines whether Main Street should solve that problem and under what product constraints.

A design is not accepted merely because it is technically feasible, familiar, fashionable, easy to implement, or discussed extensively.

Accepted design MUST survive the Main Street validation process, satisfy applicable Fundamental Vision Conformance and remain consistent with the current accepted authorities applicable to its scope as resolved through DOCUMENT-GOVERNANCE.md and AUTHORITY-INDEX.md.

---

## 2. Mandatory Design-Decision Lifecycle

Every material Main Street design decision MUST follow this lifecycle:

```text
DESIGN / PROPOSE
        ↓
FUNDAMENTAL VISION CONFORMANCE
        ↓
REVIEW
        ↓
FALSIFICATION
        ↓
RECOMMENDATION
        ↓
PRESENT COMPLETE FINAL PROPOSED AUTHORITY IN CHATGPT
        ↓
EXPLICIT MANUAL APPROVAL
        ↓
WRITE THE APPROVED AUTHORITY TO THE REPOSITORY
        ↓
UPDATE AUTHORITY INDEX / DDR / LEXICON WHERE APPLICABLE
        ↓
REVIEW IMPLEMENTATION-RULES IMPACT
        ↓
CORPUS CONFORMANCE
        ↓
COMMIT TO development
```

The lifecycle has a hard repository boundary:

```text
before explicit manual approval
        =
ChatGPT only

after explicit manual approval
        =
repository formalisation permitted
```

No material proposal may skip directly from discussion, recommendation or apparent consensus to repository authority.

### 2.1 Design / Propose

A proposal MUST state the problem, business/domain need, affected capabilities/boundaries, proposed model, architectural fit, alternatives, trade-offs, assumptions requiring validation and the applicable Fundamental Vision relationship.

Design work remains non-authoritative before explicit manual approval.

Material design drafting MUST take place in ChatGPT and MUST NOT be written to the repository before approval.

### 2.2 Fundamental Vision Conformance

Before ordinary review, the proposal MUST be evaluated under Section 0 and receive one of the four defined Vision-Conformance outcomes.

A proposal classified `VISION-NON-CONFORMING` MUST be rejected or fundamentally revised.

A proposal classified `VISION-UNRESOLVED` MUST NOT proceed to acceptance until the uncertainty is resolved.

### 2.3 Review

Review MUST test the proposal against the Fundamental Vision, accepted architecture, existing semantic ownership, capability boundaries, invariants, merchant authority, configuration/compiler/runtime separation, internal contracts, applicable accepted MS-PROT/TAS/ADR authorities resolved through current authority navigation, and implementation consequences where relevant.

Review MUST explicitly ask all three:

```text
Is this design correct?
```

and:

```text
Should Main Street work this way?
```

and:

```text
Is this the best justified approach for Main Street among the credible alternatives examined?
```

"Best" means the strongest justified fit with Main Street's Fundamental Vision, merchant simplicity, semantic ownership, safety, implementation cost and maintenance cost. It does not require theoretical perfection or exhaustive exploration of every conceivable alternative.

Review MUST explain why the chosen approach is preferable to the credible alternatives examined, including a smaller change, reuse of existing authority, or doing nothing where applicable.

A proposal MAY be semantically correct, architecturally clean and technically feasible and still be rejected because it violates the Main Street Fundamental Vision.

Review MUST actively seek unnecessary complexity, duplication, hidden coupling, semantic leakage, authority collision, business-type assumptions, speculative machinery, unnecessary merchant configuration, unnecessary staff training and product-suite drift.

Review notes, conflict analyses and intermediate design artefacts MUST remain in ChatGPT before approval.

### 2.4 Falsification

Falsification is mandatory. Its objective is to find credible conditions under which the proposal fails, not merely demonstrate that it can work.

Falsification SHOULD use concrete merchant scenarios, state transitions, dependency/ownership graphs, lifecycle graphs and counterexamples.

It SHOULD ask whether the proposal fails for other valid merchant domains, duplicates authoritative state, creates shared mutation authority, confuses configuration/runtime state, lets entitlement or providers redefine semantics, overrides merchant policy, introduces speculative abstraction, or fails under upgrade/downgrade, provider failure, partial fulfilment, concurrency, retries or long-running commitments.

Falsification MUST also attempt to prove that the proposal causes Main Street to drift toward conventional ERP complexity, generic software-suite behaviour, merchant-administered integration, feature accumulation, business-type hard-coding, unnecessary configuration, unnecessary staff training, enterprise complexity inappropriate to target businesses, AI-created semantic authority, or module-centric rather than business-centric interaction.

Falsification MUST include at least one scenario testing the proposal from the perspective of a low-software-capacity target merchant. Where staff are affected, at least one ordinary-staff scenario SHOULD also be tested.

#### Cross-Domain Generality Evidence

Where a proposal claims platform-wide, cross-business, cross-domain or generally reusable semantic/architectural applicability, falsification MUST test that claim against materially different valid merchant domains sufficient to challenge the proposed abstraction.

Success in one merchant archetype, one capability, one vertical or one happy-path scenario does not establish platform generality.

A narrowly scoped authority does not require artificial cross-domain testing unrelated to its claimed scope.

A design MUST NOT obtain a narrow approval and later be treated as platform-general without the additional evidence required by the broader claim.

#### Validation-Evidence Traceability

Material validation and acceptance claims MUST satisfy the evidence requirements in Section 75.

A failed falsification requires revision or rejection, not rationalisation.

Falsification evidence MUST remain in ChatGPT until the resulting authority has received explicit manual approval.

### 2.5 Recommendation

The recommendation MUST state exactly one of:

```text
ACCEPT
REVISE
REJECT
DEFER
```

and MUST explain the evidence and trade-offs supporting that recommendation.

A recommendation of `ACCEPT` is a design-review conclusion only.

A final `RECOMMENDATION: ACCEPT` MUST NOT be issued for a material proposal unless its Vision Conformance result is either:

```text
VISION-CONFORMING
```

or:

```text
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
```

A proposal classified `VISION-NON-CONFORMING` MUST receive `REJECT` or `REVISE`.

A proposal classified `VISION-UNRESOLVED` MUST receive `DEFER` or `REVISE` until the uncertainty is resolved.

Hard distinction:

```text
RECOMMENDATION: ACCEPT
        ≠
MANUAL APPROVAL: GRANTED
        ≠
STATUS: ACCEPTED
```

`ACCEPT` MUST NOT be treated as approval, repository authority or permission to write the design to the repository.

### 2.6 Complete Pre-Approval Presentation

After review, falsification and recommendation are complete, the complete final proposed authority MUST be presented in ChatGPT before approval is requested or inferred.

The presentation MUST contain the complete normative meaning intended for the repository.

The user MUST be able to evaluate the final authority without reconstructing it from earlier discussion, partial drafts or repository files.

No repository copy of the proposed authority may exist at this stage.

### 2.7 Explicit Manual Approval

A material semantic, architecture or governance decision becomes eligible for repository formalisation only after explicit manual approval of the presented final proposal.

Silence, continuation, recommendation, agreement with an individual principle, absence of objection, prior discussion or `RECOMMENDATION: ACCEPT` MUST NOT be interpreted as approval of a complete authority.

Approval must be attributable to the complete proposed authority presented for approval.

### 2.8 Repository Isolation Before Approval

Unapproved design documentation MUST leave no design-documentation trace in the Main Street repository.

Before explicit manual approval, the following are prohibited for the proposed design:

```text
tracked draft files
development/review files
proposal files
falsification files
recommendation files
temporary design authorities
PROPOSED authority files
draft ADR/TAS/MS-PROT files
Authority Index entries
DDR resolution entries asserting the proposal
Lexicon changes deriving from the proposal
design-specific branches
design-specific commits
tags
pull requests containing the design
repository issues used to persist the substantive design
other repository artefacts containing the unapproved design
```

The repository is an approved-authority store, not a design workspace.

Existing repository authority MAY be read during design work. Existing implementation MAY be inspected as evidence. Neither activity permits unapproved design material to be written back to the repository.

### 2.9 Formalisation After Approval

Only after explicit manual approval may the approved authority be written to the repository.

The repository version MUST preserve the exact approved normative meaning.

Formalisation MUST NOT:

- introduce a new material rule;
- remove an approved rule;
- weaken or strengthen approved requirement language;
- change ownership;
- change lifecycle or operation semantics;
- add an unreviewed exception;
- resolve a deferred question;
- alter a trade-off; or
- make another material architectural decision.

If repository formalisation would require any material change, the changed complete authority MUST return to ChatGPT for review and explicit approval before repository write.

### 2.10 Post-Approval Repository Work

After approval, repository work proceeds:

```text
write approved authority
        ↓
update AUTHORITY-INDEX.md where applicable
        ↓
update DEFERRED-DECISION-REGISTER.md where applicable
        ↓
update CANONICAL-SEMANTIC-LEXICON.md where applicable
        ↓
review IMPLEMENTATION-RULES impact
        ↓
run DESIGN-CORPUS-CONFORMANCE
        ↓
commit to development
        ↓
report traceability
```

These post-approval changes MUST be limited to consequences of the approved authority.

A material additional design discovered during formalisation or conformance MUST return to the beginning of this lifecycle.

---

## 3. Iterative Graph and Loop Rule

Design decisions form a dependency graph and MUST be evaluated iteratively. Later evidence MAY reopen an accepted decision through a new governed cycle; reopening does not erase history.

When a design affects multiple capabilities, ownership, dependency and communication edges MUST be explicit.

---

## 4. Chat-First Drafting, Markdown and Self-Sufficiency Rule

Before approval, material design drafting, review, falsification and recommendation MUST occur in ChatGPT and MUST NOT be persisted in the repository.

After approval, formal design authorities MUST be Markdown (`.md`).

A formal repository authority MUST be self-sufficient together with explicit repository references. It MUST NOT require the originating ChatGPT conversation to determine approved meaning, exceptions, rejected alternatives or normative boundaries.

ChatGPT is therefore the mandatory design workspace before approval; the repository is the durable authority store after approval.

---

## 5. Required Design-Document Qualities

A formal design document MUST be strict, explicit, implementation-constraining, internally consistent, traceable, explicit about ownership/invariants/scope/exclusions, and sufficiently complete that another engineer can implement without inventing a business rule.

Normative content MUST use the precision rules in Part II.

---

## 6. Required Metadata

Every new or materially amended authority MUST contain the applicable metadata used by the established Main Street MS-PROT authority format.

At minimum, where applicable:

```text
Document ID
Version
Status
Approved
Authority type where useful
Governed by
Amends
Supersedes
Depends on
Closes
Purpose
```

A field that materially affects authority navigation MUST NOT be omitted merely because its value appears obvious from filename or chronology.

Current authority navigation belongs in `AUTHORITY-INDEX.md`; substantive semantics remain in accepted owning authorities.

---

## 7. Composite Architecture Preservation Rule

Designs MUST preserve Main Street's accepted composite/business-driven architecture unless an explicit architectural amendment is separately proposed, falsified and approved.

The default composite model is:

```text
capability-oriented bounded contexts/modules
+
modular monolith initially
+
declarative semantic/configuration compilation
+
transactional consistency where atomic invariants require it
+
selective post-commit event-driven reactions
+
explicit lifecycle/state modelling where business state matters
+
ports/adapters around providers and infrastructure
+
explicit application orchestration across capabilities
```

No proposal may silently introduce a competing architectural paradigm. Patterns such as microservices, CQRS, distributed queues or event-driven infrastructure require demonstrated need rather than convention.

---

## 8. Capability Ownership Rule

Each authoritative business truth MUST have exactly one semantic owner.

Other capabilities MAY reference, request, react to committed facts, project or derive read models, but MUST NOT maintain a competing authoritative copy.

Cross-capability behaviour SHOULD use explicit application orchestration, registered relationships, committed events or accepted internal contracts rather than direct semantic leakage.

---

## 9. Configuration, Compiler and Runtime Separation

Design MUST preserve:

```text
Merchant intent / approved choice
        ↓
Authoritative configuration
        ↓
Deterministic validation / compilation
        ↓
Resolved merchant semantics
        ↓
Contextual runtime decision
        ↓
Capability-owned execution
```

Configuration MUST NOT become a container for live operational state. The compiler MUST NOT use probabilistic AI to invent missing required semantics. Runtime authority, provider health, operational eligibility, current availability and outstanding obligations remain distinct from stable configuration unless explicitly governed otherwise.

---

## 10. Merchant Authority Rule

Main Street streamlines merchant operations; it does not decide how merchants should operate unless an accepted legal, security, safety or platform invariant requires restriction.

Merchant-set operating, scheduling, delivery, return and similar supported business policies remain merchant decisions within registered Main Street semantics.

Main Street MAY assist configuration but MUST NOT silently substitute Main Street's preferred policy. Main Street is infrastructure, not a marketplace or general business adjudicator.

---

## 11. AI Design Rule

AI is inferential/assistive, not semantic authority.

Specialists MUST infer against registered semantics and MUST NOT create/register semantics, invent executable relationships/business rules, mutate authoritative state merely from NLP interpretation, or bypass required approval/deterministic validation.

```text
Merchant natural-language intent
        ↓
Concierge interpretation
        ↓
Specialist inference
        ↓
Candidate registered configuration/policy
        ↓
Merchant review/approval
        ↓
Manual validation gate where required
        ↓
Deterministic validation/compilation
        ↓
Authoritative configuration lifecycle
```

---

## 12. Manual Validation Gate Rule

Where an AI proposal will become authoritative configuration, the design MUST preserve merchant/manual validation and approval where required by the governing capability. Agent confidence is not authority.

---

## 13. Provider Neutrality Rule

Providers/adapters MUST NOT own Main Street business semantics.

Design MUST distinguish business meaning, Provider Fulfilment, provider evidence and Provider Readiness. Provider failure MUST NOT silently rewrite existing business truth.

---

## 14. Subscription and Entitlement Rule

Commercial packaging follows the merchant's business model; it MUST NOT define that model.

Configuration represents what the business needs. Subscription state governs commercial access rather than rewriting configuration.

Design MUST distinguish Semantic Applicability, Commercial Entitlement, Actor Authorisation, Operational Eligibility, Provider Readiness and Surface Exposure; these MUST NOT collapse into one vague `enabled/available/allowed` flag.

---

## 15. Channel Convergence Rule

Website, dashboard, POS, phone, walk-in, API and AI-assisted entry SHOULD converge on the same capability-owned business operation when they represent the same business intent.

Channel-specific interaction/projection may differ; authoritative truth MUST NOT duplicate solely because channel differs.

---

## 16. Business-Type Neutrality Rule

Business category/niche MAY provide onboarding context but MUST NOT own behaviour representable by generic capabilities.

A new merchant domain SHOULD first falsify existing primitives/composition. New semantics are justified only for materially new business meaning that cannot be represented without distortion.

---

## 17. ADT and Technical-Mechanism Selection Rule

A proposed ADT, pattern, framework or mechanism is not privileged because it was suggested first.

If unsuitable for the semantics, invariants, scale, lifecycle or operational characteristics, review MUST recommend a better approach. The design must choose the mechanism that fits the task rather than force the task into the mechanism. Trade-offs MUST be explicit.

---

## 18. Simplicity and Deferred Complexity Rule

Use the simplest system that correctly represents materially different merchant operations without accumulating business-specific exceptions.

Speculative complexity SHOULD be deferred, but real correctness requirements—atomicity, durability, security, privacy and similar—MUST NOT be deferred when already required by accepted behaviour.

---

## 19. Mobile and Resource-Conscious Rule

Merchant operation is mobile-first. Designs SHOULD account for constrained screens, variable devices/networks, storage, memory, battery and unnecessary client complexity.

A backend design that makes ordinary merchant operation impractical on a phone is incomplete.

---

## 20. Projection and Exposure Rule

Authoritative state, projection and Exposure MUST remain distinct:

```text
Authoritative business fact
        ↓
Projection / read representation
        ↓
Exposure decision
        ↓
Audience surface
```

Presentation MUST NOT redefine domain state.

---

## 21. Status and Lifecycle Rule

Do not introduce authoritative `status` merely because a UI needs a label. Where lifecycle matters, model lifecycle/facts explicitly; presentation status is a projection unless domain-justified.

---

## 22. Internal Communication Contract Rule

Cross-capability/adaptable subsystem contracts MUST preserve ownership and identify, where applicable, requester, owner, semantic input/output, mutation boundary, failure, retry/idempotency, event/reaction, entitlement/authorisation and provider participation.

Internal communication MUST NOT create shared mutable ownership.

---

## 23. Transaction and Idempotency Rule

Atomic business invariants MUST be transactionally protected at the appropriate boundary.

Repeated commands, callbacks, retries and multi-channel actions MUST explicitly address idempotency/duplicate prevention. Repeated transport MUST NOT multiply authoritative effect unless repetition is intentionally additive under the owning semantics.

---

## 24. Events Rule

Events SHOULD support post-commit decoupling where appropriate. They MUST NOT replace atomic transactions required for invariants. Downstream reaction MUST NOT retroactively redefine the committed source fact.

---

## 25. Historical Traceability Rule

Historical semantic/design evidence MUST NOT be silently rewritten merely for repository neatness. Material semantic changes require explicit accepted amendment/supersession where scope provenance matters.

This differs from single-current governance/rule documents governed by `DOCUMENT-GOVERNANCE.md`, whose revision history is preserved by Git.

Historical traceability begins at approved repository authority.

Unapproved proposals, reviews, falsification notes and recommendations MUST NOT be committed merely to preserve design history. Their absence from repository history is intentional under Section 2.8.

Once a design has been explicitly approved and formalised, later material semantic changes require the normal amendment/supersession rules and MUST NOT silently rewrite approved semantic history.

---

## 26. Terminology Rule

Normative documents MUST use canonical terminology where ambiguity affects implementation. `CANONICAL-SEMANTIC-LEXICON.md` governs qualification/disambiguation but not semantic ownership.

---

## 27. Deferred Decisions Rule

Known unresolved material questions MUST be recorded in `DEFERRED-DECISION-REGISTER.md` rather than silently resolved by implementation assumptions.

---

## 28. Corpus Conformance Rule

Formalisation is incomplete until applicable `DESIGN-CORPUS-CONFORMANCE.md` checks pass. Mechanical checks do not replace human semantic review/falsification.

Design corpus conformance MUST include applicable Fundamental Vision structural/navigation checks and human review of material accepted authorities for contradiction with `MS-FUNDAMENTAL-VISION-001`.

Conformance SHOULD detect, where reasonably possible:

```text
obsolete module-centric assumptions
business-type hard-coding
unnecessary merchant configuration
conflicting product-purpose statements
feature-parity language presented as product justification
obsolete statements narrowing Main Street to website/online-presence software
```

Mechanical checks MUST NOT claim to prove arbitrary product-purpose or semantic coherence.

---

## 29. Implementation Relationship

Accepted design governs within its scope. Tests/implementation are conformance evidence, not substitute authority.

If implementation evidence reveals a design flaw, reopen design through the governed lifecycle. `IMPLEMENTATION-RULES.md` governs implementation process and composite-architecture conformance.

Implementation remains subordinate to the Fundamental Vision through the accepted authority chain and MUST NOT silently weaken it.

---

## 30. Approval Meaning

Explicit approval means that the complete final authority presented in ChatGPT has been authorised for repository formalisation.

Approval permits:

```text
write approved authority
        ↓
update applicable governance navigation
        ↓
run corpus conformance
        ↓
commit to development
        ↓
report traceability
```

Approval does not authorise implementation unless implementation is separately in scope.

Approval also does not authorise materially changing the approved text during formalisation.

A recommendation of `ACCEPT` is not approval.

An authority becomes `ACCEPTED` only through explicit manual approval followed by conforming repository formalisation.

---

## 31. Core Approval Review

Before recommending approval, review MUST establish the business problem, Fundamental Vision Conformance result, semantic owner, dependencies/amendments, composite-architecture fit, merchant authority, business-type neutrality, configuration/runtime/entitlement/provider/presentation distinctions, alternatives/trade-offs, falsification, failure/retry/duplicate behaviour, cross-domain applicability, AI/provider boundaries and self-sufficiency of the resulting authority.

For a materially significant MS-PROT/TAS/ADR design, the final proposal SHOULD contain or be accompanied by a concise Vision Conformance statement identifying:

```text
how the proposal supports the Fundamental Vision;
what user-visible complexity it introduces;
why that complexity is necessary;
whether integration was considered instead of native ownership;
and which feature-admission test it satisfies.
```

This statement is evidence of conformity. It does not transfer semantic authority to the Fundamental Vision document.

Unknown material answers block recommendation unless safely deferred.

---

# Part II — Normative Precision and Ambiguity Elimination

## 32. Anti-Ambiguity Principle

A normative design statement is sufficiently precise only when two competent engineers, working independently from the accepted corpus, can derive materially equivalent semantic behaviour without inventing an unstated business rule.

If materially different interpretations remain reasonable, the design is ambiguous and MUST NOT be treated as implementation-ready.

Implementation convenience, convention, framework defaults, AI inference, industry custom or engineer preference MUST NOT silently resolve a missing semantic decision.

---

## 33. Normative Versus Explanatory Content

Normative rules establish required/prohibited/permitted behaviour. Explanatory examples, rationale and scenarios MUST NOT silently introduce requirements.

If an example conflicts with a normative rule, the normative rule governs and the example MUST be corrected. Behaviour appearing only in an example is not authoritative unless specified normatively.

---

## 34. Normative Keywords

- **MUST** — required for conformance.
- **MUST NOT** — prohibited for conformance.
- **SHOULD** — expected unless a documented justified exception exists.
- **SHOULD NOT** — expected to be avoided unless a documented justified exception exists.
- **MAY** — explicitly permitted but not required.
- **OWNS** — semantic authority for identified business truth.
- **AUTHORITATIVE** — source against which conflicting representations are reconciled.
- **DERIVED** — computed from authoritative facts and not independently authoritative.
- **PROJECTION** — representation for a consumer/surface, not mutation authority.

Normative synonyms MUST NOT be used casually where they create conflicting requirement strength.

---

## 35. Mandatory Semantic Definition Contract

Every newly introduced normative semantic concept MUST identify all applicable items below; uncertain applicability MUST NOT be silently omitted:

1. canonical name;
2. semantic definition;
3. owning capability/context;
4. authoritative source of truth;
5. identity/equality rule;
6. scope;
7. creation/establishment conditions;
8. invariants;
9. allowed mutations;
10. mutation authority;
11. lifecycle/fact model where applicable;
12. terminal conditions where applicable;
13. operation inputs;
14. outputs/results;
15. relationships;
16. cross-capability effects;
17. transaction boundary;
18. retry/idempotency semantics;
19. failure semantics;
20. configuration/runtime/derived/projection classification;
21. Commercial Entitlement relationship if applicable;
22. Actor Authorisation relationship if applicable;
23. provider relationship if applicable;
24. Projection/Exposure relationship if applicable;
25. explicit exclusions/non-ownership;
26. governing authority references.

A happy-path lifecycle alone is insufficient specification.

---

## 36. Single Semantic Owner Requirement

Every authoritative fact MUST have exactly one owner. A design MUST answer:

```text
Who owns this truth?
Who may mutate it?
Who may only observe/reference/request/react?
```

Shared mutable ownership MUST NOT be inferred. Apparent overlap MUST be resolved or explicitly classified as a design conflict.

---

## 37. Explicit Predicate Rule

A normative condition controlling behaviour MUST be a determinable predicate or explicitly delegated to a named accepted authority.

Terms such as `valid`, `eligible`, `available`, `active`, `enabled`, `appropriate`, `relevant`, `complete`, `ready`, `processed`, `resolved`, `successful` or `authorised` MUST NOT determine behaviour without defined predicates/authority.

---

## 38. Ambiguous Phrase Rule

Normative requirements MUST NOT rely on undefined phrases such as:

```text
normally
generally
typically
as needed
as required
where appropriate
when necessary
if relevant
if applicable
reasonable
suitable
adequate
proper
etc.
and so on
should support
can handle
may be available
some cases
other cases
similar
```

Such wording MAY appear in non-normative rationale where it does not determine behaviour.

---

## 39. Undefined Pronoun and Reference Rule

Normative prose MUST NOT depend on ambiguous referents such as `it`, `this`, `that`, `they`, `the system`, `the service`, `the policy`, `existing behaviour` or `current rules` where multiple referents are plausible.

Use the canonical semantic name or explicit authority.

---

## 40. Cross-Reference Precision Rule

Normative references such as `as previously defined`, `existing policy`, `earlier design`, `as discussed`, or `current architecture` MUST identify a resolvable governing authority where behaviour depends on the reference.

Cross-references SHOULD identify document ID plus scope/section/concept where material. Detailed semantics SHOULD be referenced from the owner rather than duplicated.

---

## 41. Authority Conflict Rule

When normative statements appear inconsistent:

```text
identify both authorities
    ↓
resolve lifecycle status
    ↓
resolve amendment/supersession scope
    ↓
identify semantic ownership
    ↓
distinguish normative from explanatory content
    ↓
apply canonical terminology
    ↓
material inconsistency remains?
    YES → classify ambiguity/contradiction and STOP affected implementation
```

Framework defaults, implementation precedent and AI judgement MUST NOT silently choose the winner.

---

## 42. Underspecification Rule

If accepted authority does not determine a material business-semantic decision, implementation MUST classify it as:

```text
IMPLEMENTATION DETAIL
DESIGN UNDERSPECIFICATION
AUTHORITY AMBIGUITY
SEMANTIC CONTRADICTION
```

Only a true implementation detail may be resolved during ordinary implementation. Other classes return to governed design.

---

## 43. Implementation Detail Boundary

A choice is an implementation detail only when all materially observable business semantics remain equivalent.

Ownership, permission, obligation creation/completion, authoritative-vs-derived status, duplicate/retry outcome, entitlement/configuration effects, provider evidence effects, approval requirements and commit/event ordering are NOT implementation details when unspecified.

---

## 44. Positive and Negative Boundary Rule

Every material design MUST specify what the owner/component does and, where neighbouring responsibilities could reasonably be confused, what it MUST NOT do.

Negative boundaries are mandatory when review identifies a plausible ownership collision.

---

## 45. Lifecycle Precision Rule

Where lifecycle is authoritative, define states/facts, entry conditions, transitions, transition authority, non-obvious prohibited transitions, terminal conditions, retry behaviour, concurrency behaviour and post-failure truth.

A generic `status` MUST NOT substitute for a required lifecycle/fact model.

---

## 46. Operation Contract Rule

Every material authoritative operation MUST specify directly or by accepted reference:

```text
Operation name
Owning capability
Actor/principal requirements
Semantic Applicability
Commercial Entitlement requirement if any
Operational preconditions
Input semantics
Authoritative facts read
Authoritative facts mutated
Atomicity boundary
Idempotency/duplicate semantics where applicable
Success result
Business rejection result
Technical/provider failure behaviour
Committed events if any
Post-commit reactions if any
Projection/Exposure consequences if any
```

Generic verbs such as `process`, `handle`, `manage`, `execute` or `complete` MUST NOT replace specification of authoritative effect.

---

## 47. Internal Contract Precision Rule

A cross-capability contract MUST identify requester/use case, receiving semantic owner, command/query/event classification, required identifiers/inputs, validation authority, mutation authority, transaction relationship, result/rejection semantics, retry/idempotency and event/reaction contract where applicable.

---

## 48. Event Precision Rule

A normative event definition MUST identify event name, source owner, fact already true, commit timing, semantic payload/references, delivery expectations where material, duplicate/replay expectations, required/optional consumers where governed, and whether consumer failure can affect the already-committed source fact.

Words such as `Requested`, `Completed`, `Failed`, or `Updated` in event names MUST have precise meaning.

---

## 49. Configuration Precision Rule

A configuration rule MUST state who chooses the value, allowed semantic values/extension mechanism, validation, default semantics if any, absence semantics, compile-time/runtime resolution, merchant-vs-platform control, effect on future actions vs existing commitments, and version/provenance where historical affinity matters.

`Default` MUST NOT silently mean that Main Street decides merchant policy.

---

## 50. Entitlement Precision Rule

Design involving commercial access MUST explicitly distinguish Semantic Applicability, Commercial Entitlement, Actor Authorisation, Operational Eligibility, Provider Readiness and Surface Exposure.

One `enabled`, `disabled`, `available`, `active` or `allowed` concept MUST NOT collapse these dimensions.

---

## 51. Provider Precision Rule

Where a provider participates, identify Main Street semantic owner, provider responsibility, port/adapter boundary, provider identifier/evidence, failure behaviour, retry/idempotency, readiness effects on new operations, survival of existing business truth, and callback classification.

Provider terminology MUST NOT replace Main Street business terminology in the owning domain.

---

## 52. AI Precision Rule

AI-assisted design MUST specify user input, inference scope, registered semantics, candidate output type, ambiguity/confidence handling where relevant, merchant approval, manual validation where required, deterministic validation, authoritative mutation path and prohibited AI actions.

Phrases such as `AI decides/configures/handles/determines` are prohibited normatively unless inferential versus authoritative responsibility is explicit.

---

## 53. Merchant Policy Precision Rule

Merchant-set policy MUST be explicit:

```text
Merchant chooses policy
        ↓
Main Street validates registered semantics
        ↓
Main Street stores/compiles approved configuration
        ↓
Main Street executes merchant policy consistently
```

Main Street choosing how the merchant should operate is prohibited unless separately justified by an accepted platform/legal/security invariant.

---

## 54. Quantity, Time and Boundary Precision Rule

Normative quantities/time rules MUST define units, inclusivity/exclusivity, anchors/reference events, timezones, currencies and denominators where ambiguity is material.

Relative terms such as `soon`, `recent`, `later`, `near`, `current`, `temporarily` or `immediately` MUST NOT govern deterministic behaviour without definition/reference.

---

## 55. Collection and Cardinality Rule

Where relationships affect invariants, cardinality MUST be explicit. Articles or vague terms (`a`, `the`, `some`, `multiple`, `many`, `related`) MUST NOT be relied on to infer material cardinality.

---

## 56. Identity and Equality Rule

Durable semantic objects MUST specify whether identity/equality is based on durable ID, capability scope, merchant scope, natural/business key, composite key or value equality.

Attribute similarity MUST NOT imply identity unless the owning authority says so.

---

## 57. Error and Rejection Rule

Where recovery/caller behaviour differs, distinguish:

```text
BUSINESS REJECTION
AUTHORISATION REJECTION
ENTITLEMENT REJECTION
VALIDATION REJECTION
CONFLICT
PROVIDER / TECHNICAL FAILURE
```

Generic `failed` MUST NOT erase material distinctions.

---

## 58. Retry and Duplicate-Action Rule

Any operation reachable through retriable transport, callbacks, UI repeat, phone/manual entry, AI assistance or multiple channels MUST answer:

1. Can the same business intent arrive more than once?
2. What identifies the same logical intent where required?
3. Is repetition idempotent, rejected or intentionally additive?
4. Which invariant prevents duplicate effect?
5. What happens if the first attempt committed but acknowledgement was lost?

`Retry safely` is insufficient specification.

---

## 59. Concurrency Rule

Where concurrent actions can violate an invariant, design MUST identify that invariant and the required atomicity/serialization guarantee. Implementation technology MAY remain downstream unless semantically required.

---

## 60. Diagram Rule

Diagrams are normative only to the extent explicitly stated. Material edges SHOULD identify whether they represent ownership, calls/commands, references, events/reactions, projection, transitions or dependencies.

A generic `A → B` MUST NOT remain unlabeled where edge semantics affect implementation.

---

## 61. Table Rule

Tables MUST NOT compress materially different semantic dimensions into one column such as `Status`, `Enabled`, `Owner` or `Behaviour` where doing so hides required distinctions.

Normative summaries MUST preserve detailed normative meaning; explanatory tables must be identifiable as such.

---

## 62. Example and Scenario Rule

Examples MUST be identifiable as examples and MUST NOT be the sole location of a material invariant.

At least one counterexample/failure scenario SHOULD accompany boundaries likely to be misunderstood. Domain-specific examples MUST NOT turn generic capabilities into business-type-specific architecture.

---

## 63. Scope and Non-Goal Rule

Every material design authority MUST define governed problem, explicit exclusions/non-goals, neighbouring ownership and deferred future concerns.

`Out of scope` MUST NOT defer a correctness invariant required by the approved behaviour.

---

## 64. Semantic Amendment Precision Rule

A semantic/design amendment MUST identify amended authority, affected scope, previous rule, new rule and rules left unchanged.

A later semantic version MUST NOT be assumed to replace all earlier rules unless complete supersession/republication is explicit.

This rule does not require separate amendment files for the single-current governance/rule authorities governed by `DOCUMENT-GOVERNANCE.md`.

---

## 65. Supersession Rule

Semantic/design supersession MUST identify whether it is complete, section-specific, concept-specific or rule-specific. Unresolvable supersession scope is an authority defect requiring repair.

---

## 66. Historical Affinity Rule

Where existing commitments depend on the configuration/policy effective at creation, the design MUST state whether historical affinity survives later policy/configuration changes.

Current policy MUST NOT automatically rewrite historical commitments unless explicitly governed.

---

## 67. Security and Privacy Boundary Rule

Where identity, authorisation, personal data or Exposure matters, define the semantic boundary rather than relying on generic `secure/private/verified/trusted/authorised` phrasing.

Possession of data MUST NOT imply permission to expose it.

---

## 68. Default Rule

Every merchant/customer-visible default MUST be classified, where applicable, as:

```text
PLATFORM INVARIANT DEFAULT
MERCHANT-OVERRIDABLE DEFAULT
INFERENCE SEED REQUIRING APPROVAL
PRESENTATION DEFAULT
IMPLEMENTATION DEFAULT WITH NO SEMANTIC EFFECT
```

The design MUST say whether/how it can change. A default MUST NOT silently become immutable business policy.

---

## 69. Optionality Rule

`Optional` MUST identify what is optional and for whom. Distinguish, where relevant:

```text
semantically not applicable
applicable but merchant declines use
configured but not commercially entitled
entitled but actor unauthorised
actor authorised but operationally ineligible
provider not ready
hidden from a surface
```

These MUST NOT collapse into one disabled/optional state.

---

## 70. Open Question Rule

A document with unresolved material questions MUST NOT be marked implementation-ready for that scope.

Material open questions MUST be resolved or explicitly deferred in `DEFERRED-DECISION-REGISTER.md` with a boundary proving current implementation does not depend on the missing decision.

Uncontrolled `TBD`, `TODO`, question marks or equivalent normative placeholders are conformance failures.

---

## 71. No Chat-Dependency Rule

Formal authority MUST NOT require chat history to determine approved meaning, terminology, exceptions or normative boundaries.

---

## 72. No AI Gap-Filling Rule

AI used in design, coding, testing or review MUST NOT fill semantic gaps from plausibility. Missing material behaviour MUST be surfaced as a design gap even where a conventional answer seems obvious.

---

## 73. Testability Rule

Every material invariant SHOULD be expressible as observable conformance conditions. Tests do not replace design; they provide implementation-conformance evidence.

---

## 74. Trade-Off Recording Rule

Where valid qualities conflict, record competing objectives, alternatives, chosen trade-off, rejected alternatives, accepted consequences and the condition that would justify revisiting the decision.

Do not present a trade-off as if all desirable properties were achieved simultaneously.

---

## 75. Falsification and Validation Evidence Rule

Every material validation or acceptance claim MUST identify evidence sufficient to support the claim.

Appropriate evidence MAY include:

```text
merchant/business scenarios
counterexamples
ownership/dependency graphs
state/lifecycle analysis
sequence analysis
failure-mode analysis
executable tests
integration/end-to-end evidence
prototype or implementation evidence
domain/competitor research
performance measurements
security/privacy analysis
other evidence appropriate to the claim
```

Evidence MUST be traceable to the claim it supports.

The existence, quantity or technical sophistication of evidence does not establish validation where that evidence does not test the relevant claim.

For example:

```text
many passing unit tests
    ≠ proof of cross-domain generality

one successful merchant vertical
    ≠ proof of platform generality

working provider integration
    ≠ proof of provider independence

working prototype
    ≠ proof of architectural fitness
```

Material accepted authority SHOULD preserve concise evidence of important falsification cases that materially shaped its boundary.

Where an accepted authority claims platform-wide, cross-business or cross-domain generality, its review evidence MUST include materially different valid merchant domains sufficient to challenge that claim.

Where a material architectural claim can only be established through implementation evidence, design approval MUST identify the required downstream implementation/programme/conformance proof rather than representing the unperformed proof as already satisfied.

Such downstream proof remains governed by `IMPLEMENTATION-RULES.md`, `MS-IMP-001` or another applicable accepted implementation authority.

Design acceptance therefore does not require pretending that evidence exists before it can exist, while implementation evidence does not retroactively become semantic authority.

---

## 76. Mandatory MS-PROT Document Format Rule

New material Main Street design authorities MUST follow the established format and presentation discipline of the accepted MS-PROT document series.

The format demonstrated by the accepted MS-PROT corpus—not an ad hoc architecture-document template—is the governing design-document style.

A new material authority MUST begin with:

```text
# <Document ID> — <Canonical Title>

Document ID
Version
Status
Approved
Authority type where applicable
Governed by
Depends on where applicable
Amends / Supersedes where applicable
Closes where applicable
Purpose
```

The normative body MUST then follow the established MS-PROT pattern:

```text
1. Governing Decision / Accepted Decision
2. Problem and Scope / Problem and Objective
3. Explicit Non-Goals where material
4. Canonical Terminology / Canonical Definitions
5+. Semantic model sections appropriate to the subject
     ↓
ownership
identity and scope
invariants
operations/contracts
lifecycle/facts
configuration/runtime distinction
cross-capability boundaries
entitlement/authorisation/exposure where relevant
provider boundary where relevant
failure/retry/idempotency/concurrency
historical affinity where relevant
falsification
trade-offs/consequences
deferred/future scope
acceptance/conformance boundary
amendment/supersession effect where applicable
```

The precise middle-section headings MUST reflect the subject being governed, as previous MS-PROT authorities do. A generic fixed template MUST NOT distort the subject merely to satisfy identical headings.

However, the following characteristics of the established MS-PROT format are mandatory:

1. the governing/accepted decision appears near the beginning rather than being buried after analysis;
2. problem and governed scope are explicit;
3. non-goals are explicit where neighbouring ownership could be confused;
4. canonical terminology is defined before terminology is used to carry substantial normative meaning;
5. semantic ownership and negative ownership boundaries are explicit;
6. diagrams/tables support rather than replace normative prose;
7. hard invariants are stated normatively;
8. operation/lifecycle/failure behaviour is specified where applicable;
9. alternatives and trade-offs are not hidden;
10. important falsification cases are preserved in the accepted authority where they materially shaped the decision;
11. implementation technology appears only when the authority legitimately governs implementation architecture or a semantic constraint requires it;
12. the document closes with sufficient acceptance/conformance meaning to determine what the authority actually established.

A new design authority MUST NOT use a materially different document style merely because another documentation convention, ADR template, RFC template or generated architecture template is available.

TAS and ADR documents MAY retain their correct authority classification, but when they constitute material Main Street design authority they MUST use the established MS-PROT presentation discipline unless an explicitly approved reason requires a different representation.

Non-applicable semantic dimensions MAY be omitted or explicitly marked `NOT APPLICABLE`, but omission MUST NOT create ambiguity.

The objective is consistent engineering reasoning and authority readability across the Main Street corpus, not superficial section-number uniformity.

---

## 77. Mandatory Ambiguity Review

Before recommendation, reviewer MUST separately ask whether:

1. any normative sentence admits materially different implementations;
2. every authoritative fact has one owner;
3. overloaded terms are qualified;
4. `enabled`, `available`, `active`, `valid`, `status`, `policy`, `customer`, `location`, `operation`, `fulfilment` are unambiguous;
5. conditions are determinable predicates/references;
6. pronouns/references are unambiguous;
7. cardinalities are explicit where material;
8. identity/equality is explicit where material;
9. transaction/concurrency guarantees are explicit;
10. retries/duplicates are defined;
11. business rejection vs technical failure is distinguishable;
12. configuration/runtime/projection are separated;
13. applicability/entitlement/authorisation/eligibility/readiness/exposure are separated;
14. provider facts are distinguished from business truth;
15. merchant policy is distinguished from platform policy;
16. AI inference is distinguished from approval/execution;
17. examples remain subordinate to rules;
18. negative boundaries exist where ownership could collide;
19. cross-document references resolve without chat;
20. amendment/supersession scope is explicit;
21. open questions are resolved/safely deferred;
22. important invariants are testable;
23. another engineer can implement without inventing a business rule;
24. Fundamental Vision Conformance has been determined;
25. merchant-visible complexity is justified and proportionate;
26. ordinary-staff product-training burden has been challenged where staff are affected.

Any material `NO` or `UNCLEAR` blocks recommendation until resolved or safely deferred.

---

## 78. Ambiguity Severity Classification

```text
A — CONTRADICTION
B — SEMANTIC AMBIGUITY
C — AUTHORITY AMBIGUITY
D — OWNERSHIP OVERLAP
E — TERMINOLOGY DRIFT
F — STALE/SUPERSEDED CLAIM
G — UNDERSPECIFICATION
H — SAFE OVERLAP
```

`H` is not a defect. A–G require resolution or explicit safe deferral before affected scope is implementation-ready.

---

## 79. Ambiguity Resolution Loop

```text
DISCOVER
   ↓
CLASSIFY
   ↓
TRACE AUTHORITIES
   ↓
BUILD CONFLICT / OWNERSHIP GRAPH
   ↓
IDENTIFY POSSIBLE RESOLUTIONS
   ↓
FALSIFY
   ↓
RECOMMEND
   ↓
PRESENT COMPLETE FINAL PROPOSED AUTHORITY IN CHATGPT
   ↓
SEEK EXPLICIT MANUAL APPROVAL
   ↓
APPROVED?
   ├── NO → repository remains untouched
   └── YES
         ↓
      AMEND / CLARIFY AUTHORITY IN REPOSITORY
         ↓
      UPDATE INDEX / LEXICON / DDR
         ↓
      CORPUS CONFORMANCE
         ↓
      COMMIT
```

A documentation ambiguity MUST NOT be repaired through an unreviewed code choice.

An unapproved ambiguity resolution MUST NOT be persisted in the repository.

---

## 80. Final Implementation-Readiness Gate

A material design is implementation-ready only when all applicable items are true:

```text
[ ] Governing authority identifiable
[ ] Fundamental Vision Conformance result is VISION-CONFORMING or VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
[ ] Any justified vision-related complexity is explicit
[ ] Scope and non-goals explicit
[ ] Canonical terms defined/referenced
[ ] Semantic ownership unique
[ ] Authoritative state identified
[ ] Invariants explicit
[ ] Operations/mutation authority explicit
[ ] Lifecycle/fact semantics explicit where applicable
[ ] Configuration/runtime boundary explicit
[ ] Applicability/entitlement/authorisation/eligibility/readiness/exposure separated
[ ] Cross-capability contracts explicit
[ ] Provider boundary explicit where applicable
[ ] Integration-vs-native ownership considered where applicable
[ ] AI authority boundary explicit where applicable
[ ] Merchant-policy ownership explicit where applicable
[ ] Failure behaviour defined
[ ] Retry/idempotency defined where applicable
[ ] Concurrency/transaction guarantees defined where applicable
[ ] Projection/Exposure does not redefine authoritative truth
[ ] Historical-policy affinity defined where applicable
[ ] Target-market proportionality tested
[ ] Merchant/staff visible complexity challenged
[ ] Trade-offs recorded
[ ] Falsification performed
[ ] No unresolved material ambiguity remains
[ ] Open questions resolved or safely deferred
[ ] Conformance conditions identifiable
[ ] Material validation/acceptance claims are traceable to supporting evidence
[ ] Any claimed platform-wide/cross-domain generality has been falsified against materially different valid merchant domains
[ ] Any material validation claim requiring downstream implementation proof identifies the applicable implementation/programme/conformance gate rather than being treated as already proven
[ ] Another engineer can implement without inventing a business rule
```

Failure of any applicable item blocks implementation readiness.

---

## 81. Single-Current-Document Rule

`DESIGN-RULES.md` is the sole current design-rules authority. Future approved changes to design-governance rules MUST be integrated into this file and versioned in place. Git history preserves revision provenance.

This rule does not flatten substantive MS-PROT semantic amendment chains where separate scope provenance remains necessary.

The single-current-document rule does not permit an unapproved revision to be committed temporarily. The canonical file remains unchanged until its proposed replacement text has received explicit manual approval.

---

## 82. Governing Writing Rule

> **A Main Street design document must not merely communicate intent. It must constrain interpretation sufficiently that implementation cannot legitimately invent, merge, relocate or contradict business semantics that the design failed to state. Where the design does not determine a material business behaviour, reopen design rather than guess.**

---

## 83. Repository Authority Boundary

The Main Street repository MUST contain approved design authority, accepted historical authority and permitted implementation/evidence artefacts only.

For material design work:

```text
ChatGPT
    owns the pre-approval design workspace

Repository
    stores the post-approval durable authority
```

The transition between them is explicit manual approval.

No recommendation, confidence level, prior precedent, model judgement or technical necessity may bypass that boundary.

> **Design here. Review here. Falsify here. Recommend here. Present the complete authority here. Only explicit approval permits the design to enter the repository.**
