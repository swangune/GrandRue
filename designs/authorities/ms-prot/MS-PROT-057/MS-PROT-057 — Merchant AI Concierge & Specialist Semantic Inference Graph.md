# MS-PROT-057 — Merchant AI Concierge & Specialist Semantic Inference Graph

**Document ID:** MS-PROT-057  
**Version:** 1.0  
**Status:** ACCEPTED after proposal, review, graph-aware falsification, recommendation and manual approval  
**Purpose:** Govern Main Street's persistent merchant-facing AI assistance, specialist-agent inference, task routing, manual approval, deterministic validation and capability-owned execution without creating a parallel semantic authority.

---

# 1. Governing principle

Main Street uses AI consistently as a probabilistic interpretation and inference layer at human-language boundaries. AI is not a source of executable semantics or authoritative business state.

The governing pattern is:

```text
MERCHANT INTENT
      ↓
AI INTERPRETATION / INFERENCE
      ↓
EXISTING REGISTERED MAIN STREET SEMANTICS
      ↓
CANDIDATE STRUCTURED MEANING
      ↓
MANUAL MERCHANT VALIDATION WHERE AUTHORITY REQUIRES IT
      ↓
DETERMINISTIC VALIDATION
      ↓
CAPABILITY-OWNED EXECUTION
```

> **AI interprets. Main Street semantics define. Humans approve. Deterministic systems execute.**

No AI component may become a parallel semantic or execution authority.

---

# 2. Persistent merchant-facing assistant

Main Street shall provide one persistent merchant-facing natural-language assistant accessible contextually throughout the merchant dashboard, including through a floating interaction control where appropriate.

The assistant acts as the merchant's single conversational doorway into Main Street's AI-assisted infrastructure.

The merchant shall not need to understand Main Street's internal bounded contexts, capability ownership or specialist-agent topology in order to request assistance.

Examples of merchant instructions include:

```text
"We are closed next Monday."

"Let customers cancel appointments up to 24 hours beforehand,
but after that retain the deposit."

"Add James as a staff member and let him manage bookings but not payments."

"From September extend consultations to one hour,
close Sundays and increase the consultation price to £80."
```

The assistant may use safe dashboard context to resolve references such as "this booking" or "increase this to £75". UI context is evidence for interpretation and never execution authority.

---

# 3. Merchant-facing concierge responsibility

The front-facing assistant is a concierge/orchestration interface rather than a universal domain authority.

It may:

- receive natural-language merchant instructions;
- maintain relevant conversational context;
- infer high-level merchant intent;
- detect ambiguity;
- ask clarification questions;
- decompose compound requests into bounded tasks;
- identify the registered specialist responsibilities implicated by a request;
- route bounded inference tasks;
- consolidate specialist candidate results;
- explain proposed consequences in merchant-facing language; and
- present candidate changes or operations for merchant validation/approval.

It shall not:

- own Booking, Appointment, Payment, Scheduling, Staff, Publication or other capability semantics;
- invent executable semantics;
- directly mutate authoritative state;
- bypass specialist/capability contracts;
- bypass merchant approval where required; or
- bypass deterministic validation.

Main Street shall reject a design in which the front-facing assistant becomes a semantic monolith merely because it provides a unified merchant experience.

---

# 4. Specialist agents

Main Street may deploy specialist AI agents aligned with bounded backend capability/configuration responsibilities where specialist inference materially improves merchant interaction.

Examples may include specialists for:

- Booking;
- Appointment/Scheduling policy;
- Business Hours;
- Publication/website configuration;
- Offering/catalogue configuration;
- Staff/access configuration;
- Inventory;
- Notifications;
- analytics interpretation; and
- other accepted Main Street capability areas.

Specialist agents are inference specialists, not semantic owners.

A specialist's expertise consists of understanding how to infer merchant intent against the registered semantic vocabulary and safe contextual projections relevant to its bounded responsibility.

---

# 5. Specialist semantic-inference invariant

No specialist agent shall create Main Street semantics.

A specialist agent may:

- infer;
- classify;
- translate merchant language into candidate semantic mappings;
- propose configuration using existing semantics;
- explain consequences;
- identify ambiguity;
- request clarification;
- identify unsupported intent; and
- contribute bounded candidate nodes to an AI task/change graph.

A specialist agent shall not:

- invent semantics;
- register semantics;
- create capability relationships;
- create arbitrary executable rules;
- manufacture unsupported policy dimensions;
- mutate authoritative capability state;
- bypass approval; or
- bypass deterministic validation.

Where no registered semantic mapping exists:

```text
Merchant intent
      ↓
Specialist inference
      ↓
No authorised semantic representation
      ↓
UNSUPPORTED or UNRESOLVED
      ↓
clarify or explain limitation
```

The following is prohibited:

```text
No semantic match
      ↓
agent creates executable meaning
```

> **Agent comprehension does not expand the semantic registry.**

---

# 6. AI task graph

A merchant instruction may span one or several Main Street responsibilities. The concierge shall be capable of representing the interpreted work as a bounded AI task graph.

Example:

```text
Merchant instruction
      ↓
Concierge
      ↓
Intent decomposition
      │
      ├── Business Hours inference task
      ├── Appointment/Scheduling inference task
      ├── Offering inference task
      └── Notification inference task
```

The AI task graph exists to coordinate inference and presentation. It does not redefine the Main Street semantic/capability graph.

Governing invariant:

> **The AI task graph may navigate the semantic and capability graph but shall never redefine semantic ownership, capability relationships or executable authority.**

---

# 7. Candidate change graph

Where merchant intent implies multiple configuration changes or operations, specialist outputs may be assembled into an explicit candidate change graph/change set.

Example:

```text
CandidateChangeSet
│
├── Business Hours
│   Sunday → CLOSED
│
├── Appointment configuration
│   duration 30m → 60m
│
├── Offering
│   price £65 → £80
│
└── Notification
    notify affected customers
```

Dependencies between candidate changes shall be explicit where material.

The candidate graph remains non-authoritative until the applicable approval and deterministic validation gates have passed.

Where nodes are semantically separable, Main Street may permit partial approval. Where dependencies make partial approval invalid, deterministic validation shall prevent an inconsistent subset from becoming authoritative.

---

# 8. Manual merchant validation and approval

Where AI inference would create or modify merchant settings, policy or another merchant-authoritative configuration, a manual merchant validation gate is mandatory.

The concierge may receive the merchant's approval conversationally, but the AI itself is not approval authority.

Main Street shall record the merchant's explicit approval as human intent authority.

The review surface shall present material consequences in business-facing language and permit correction before approval.

Required pattern:

```text
AI candidate
      ↓
merchant-visible meaning/consequences
      ↓
MANUAL MERCHANT VALIDATION
      ↓
explicit approval/edit/rejection
```

No confidence score may bypass this gate.

---

# 9. Deterministic validation after approval

Merchant approval is necessary where required but is not sufficient for execution.

Approved candidate configuration/change sets shall pass deterministic semantic, authorisation and invariant validation before activation or execution.

```text
Merchant approval
      ↓
Deterministic validation/compiler
      ↓
valid?
 ┌────┴────┐
NO         YES
│           │
reject/     authorised
explain     execution plan
```

AI shall not adjudicate deterministic validity where the governing semantic/compiler/runtime authority already exists.

---

# 10. Capability-owned execution

Approved and validated work shall execute through existing capability-owned application contracts.

Specialist agents shall not write directly to capability databases or provider integrations.

Preferred boundary:

```text
Candidate semantic mapping
      ↓
merchant approval
      ↓
deterministic validation
      ↓
authorised application operation
      ↓
owning capability
      ↓
authoritative state
```

Specialists should consume registered contracts, safe projections and bounded contextual data rather than unrestricted database access.

Specialist output should consist of bounded forms such as:

- candidate configuration changes;
- candidate operations;
- clarification requirements;
- unsupported-intent findings; and
- explanations.

It shall not consist of arbitrary SQL, code or direct state mutation instructions.

---

# 11. Configuration intent versus operational intent

The merchant assistant shall distinguish configuration intent from operational intent.

Examples:

```text
CONFIGURATION
"Customers can cancel until 24 hours before."
"Open Saturdays from 10 to 2."

OPERATION
"Cancel tomorrow's 3pm appointment."
"Mark room 12 unavailable."
```

Configuration intent follows the applicable candidate-configuration, manual-validation and deterministic-validation path.

Operational intent follows the owning capability's authorisation, confirmation, invariant and execution contract. Where an operational action materially requires merchant confirmation, the concierge shall obtain it; AI shall not infer irreversible authority merely from contextual probability.

---

# 12. Context-aware dashboard assistance

The assistant may receive bounded contextual information from the merchant dashboard, including the current surface, selected entity, merchant/location scope and other safe identifiers required for interpretation.

Example:

```text
current surface = Booking
selected entity = Booking B1042
merchant scope = M123

Merchant:
"Make this refundable until tomorrow."
```

Context may help resolve "this" but shall not grant permission to modify the object.

> **Context assists inference; authorisation governs execution.**

---

# 13. No uncontrolled agent swarm

Main Street shall not implement open-ended autonomous agent-to-agent delegation in which specialists independently create objectives, recursively invoke other agents or execute unbounded workflows without traceable merchant intent.

Preferred model:

```text
Merchant instruction
      ↓
one bounded interaction/session authority
      ↓
AI task graph
      ↓
bounded specialist inference
      ↓
consolidated candidate result
      ↓
merchant approval where required
      ↓
deterministic execution
```

Every material candidate change shall remain traceable to merchant intent and the relevant inference/approval record.

---

# 14. Relationship to Main Street composite architecture

This AI architecture is an interaction/inference layer over Main Street's established composite architecture. It is not a replacement architecture.

```text
SYSTEM
    Composite architecture

DEPLOYMENT
    Modular monolith initially

DOMAIN
    Capability/bounded-context ownership

VARIABILITY
    Declarative configuration

AI
    Probabilistic interpretation/inference

HUMAN
    Merchant intent/approval authority where required

SEMANTIC RESOLUTION
    Deterministic compiler/validation

RUNTIME
    Capability-owned imperative execution

REACTIONS
    Selective event-driven mechanisms

INFRASTRUCTURE
    Ports/adapters
```

The presence of a unified conversational interface shall not collapse bounded contexts or capability ownership.

---

# 15. Consistency with Main Street-wide AI use

This document formalises an existing Main Street-wide AI principle rather than introducing a separate AI philosophy.

Wherever AI is used to interpret merchant intent, onboarding answers, configuration intent, policy language or similar uncertain human input, the same boundary applies:

```text
uncertain human input
      ↓
AI inference
      ↓
registered semantics
      ↓
structured candidate
      ↓
human validation where required
      ↓
deterministic authority
```

No AI agent in Main Street is a source of executable semantics.

---

# 16. Governance invariants

Any future Main Street AI-assistant design must demonstrate all of the following:

1. AI remains an interpretation/inference layer rather than semantic authority.
2. Specialist agents infer against registered semantics and never create semantics.
3. The concierge does not become a universal semantic owner.
4. AI task graphs do not redefine the capability graph.
5. Merchant-authoritative configuration changes pass manual merchant validation.
6. Manual approval does not bypass deterministic validation.
7. Capability-owned execution remains authoritative.
8. Context assists interpretation but never grants execution authority.
9. Unsupported intent remains unsupported/unresolved rather than causing semantic invention.
10. Material actions remain traceable to merchant intent and approval.
11. Specialist agents receive bounded contracts/context rather than unrestricted backend authority.
12. Agent orchestration remains bounded and does not become an uncontrolled autonomous swarm.

A proposal that violates any applicable invariant shall be rejected or returned to the design loop.

---

# 17. Prohibited designs

The following are rejected:

- a universal AI super-agent that owns backend semantics;
- specialist-created semantics;
- AI-generated executable rules outside the semantic registry;
- direct agent database mutation;
- agent activation of merchant settings without required human validation;
- confidence-based approval bypass;
- agent task graphs that redefine capability relationships;
- unrestricted specialist access to backend state;
- open-ended autonomous agent swarms;
- hidden AI mutation without traceable merchant intent;
- treating UI context as execution authority; and
- duplicating deterministic compiler/runtime authority inside probabilistic agents.

---

# 18. Accepted architecture

```text
                         MERCHANT
                            │
                            ▼
                 MAIN STREET ASSISTANT
                  persistent NLP doorway
                            │
                            ▼
                      INTENT GRAPH
                            │
             ┌──────────────┼──────────────┐
             ▼              ▼              ▼
         SPECIALIST      SPECIALIST      SPECIALIST
           AGENT           AGENT           AGENT
             │              │              │
             └──────────────┼──────────────┘
                            │
                 infer against existing
                   registered semantics
                            │
                            ▼
                  CANDIDATE CHANGE GRAPH
                            │
                            ▼
                 MANUAL MERCHANT APPROVAL
                    where authority requires
                            │
                            ▼
                  DETERMINISTIC VALIDATION
                            │
                            ▼
                   AUTHORISED CHANGE PLAN
                            │
             ┌──────────────┼──────────────┐
             ▼              ▼              ▼
         CAPABILITY      CAPABILITY      CAPABILITY
           OWNER           OWNER           OWNER
                            │
                            ▼
                    AUTHORITATIVE STATE
```

> **Main Street shall present AI to merchants as one coherent, context-aware assistant while internally using bounded specialist inference over the existing semantic/capability graph. Specialists infer; they do not create semantics. Merchant approval establishes human intent where required, and only deterministic Main Street authority may validate and execute the resulting capability-owned changes.**
