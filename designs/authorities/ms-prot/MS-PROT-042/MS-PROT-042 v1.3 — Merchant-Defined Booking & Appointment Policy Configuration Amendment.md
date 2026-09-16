# MS-PROT-042 v1.3 — Merchant-Defined Booking & Appointment Policy Configuration Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.3  
**Status:** **ACCEPTED after graph-aware iterative falsification and manual approval**  
**Amends:** MS-PROT-042 v1.2  
**Closes:** DDR-OD-006 — Booking / Appointment lifecycle and policy contract  
**Depends on:** MS-PROT-039, MS-PROT-042 v1.2, MS-PROT-055 and applicable configuration/compiler governance  
**Purpose:** Establish the authority boundary for Booking and Appointment operating-policy configuration, AI-assisted policy interpretation, mandatory merchant validation, deterministic activation, historical policy affinity, and cross-capability consequences without prescribing how merchants operate their businesses.

---

# 1. Governing principle

Main Street is operating infrastructure. It shall not prescribe how a merchant must run its Booking or Appointment operations.

The merchant owns its business operating policy.

Main Street owns:

- the registered semantic vocabulary through which supported policy is represented;
- the infrastructure used to capture and edit that policy;
- semantic and invariant validation;
- deterministic policy evaluation;
- capability-owned execution contracts;
- policy provenance and auditability; and
- business-facing projections that allow the merchant to understand the consequences of configured policy.

Therefore:

> **The merchant decides how the business operates; Main Street provides structured, validated and reliable infrastructure that streamlines those operations.**

---

# 2. Policy freedom versus semantic authority

Merchant policy freedom shall not be confused with arbitrary executable semantic freedom.

A merchant may define supported operating choices such as cancellation, rescheduling, deposit treatment, refund treatment, notice periods, no-show consequences and related Booking/Appointment rules where Main Street has registered semantics capable of representing them.

A merchant shall not:

- inject executable code into Main Street policy evaluation;
- invent runtime states;
- create arbitrary semantic relationships;
- directly mutate Payment, Scheduling or other capability-owned state;
- bypass semantic validation; or
- cause an AI agent to expand executable semantics merely by describing an unsupported rule.

Canonical boundary:

```text
MERCHANT
    owns business-policy intent

MAIN STREET
    owns supported semantic representation
    and deterministic execution authority
```

---

# 3. Capability-relevant configurable semantics

Main Street shall provide registered configurable semantics sufficient to represent materially variable merchant scenarios without creating business-category-specific policy classes.

Candidate semantic dimensions may include, where justified by the owning capability:

- actor;
- permitted operation;
- notice/cutoff duration;
- time-relative condition;
- cancellation permission;
- rescheduling permission;
- rescheduling count/limit;
- deposit treatment;
- refund treatment;
- late-cancellation consequence;
- no-show consequence;
- merchant-initiated cancellation consequence;
- payment-due condition; and
- other capability-relevant deterministic dimensions accepted through design governance.

This list is not an unrestricted rule language and is not a frozen universal schema. Semantics shall be added when demonstrated merchant scenarios require additional deterministic expressiveness.

Main Street shall prefer reusable value semantics where meaning is genuinely shared, while retaining capability ownership where Booking and Appointment meanings differ.

Rejected design:

```text
SalonCancellationPolicy
HotelCancellationPolicy
ConsultantCancellationPolicy
MechanicCancellationPolicy
```

Preferred direction:

```text
registered reusable semantic dimensions
        +
Booking-owned policy meaning
        or
Appointment-owned policy meaning
```

---

# 4. Specialist NLP policy-configuration agent

Main Street may provide a specialised NLP-assisted agent to help merchants configure Booking and Appointment policy.

The agent may:

- interpret natural-language merchant intent;
- map supported intent to registered semantics;
- propose candidate policy configuration;
- explain the proposed consequences in business-facing language;
- identify ambiguity or missing dimensions;
- ask targeted follow-up questions;
- propose modifications to existing policy; and
- explain which parts of an intended policy are unsupported.

The agent shall not:

- become policy authority;
- invent unsupported semantic dimensions;
- generate arbitrary executable policy logic;
- activate configuration;
- bypass merchant review;
- bypass deterministic validation; or
- directly authorise Booking, Appointment, Payment, Scheduling or Notification state changes.

Governing rule:

> **Agent comprehension does not expand the Main Street semantic registry.**

---

# 5. Ambiguity must remain unresolved

An agent shall not manufacture policy values merely to complete a configuration.

Example merchant statement:

```text
I normally refund people if they give me enough notice.
```

Material unresolved dimensions may include:

```text
notice period = unresolved
refund scope = unresolved
exception meaning = unresolved
```

The agent shall ask targeted questions or otherwise leave those dimensions unresolved.

The following behaviour is prohibited:

```text
"enough notice"
    ↓
agent silently assumes 24 hours
```

where the merchant did not establish that meaning.

---

# 6. Mandatory manual validation gate

Every agent-generated or agent-modified merchant policy configuration must pass an explicit human merchant validation gate before it may proceed toward activation.

There shall be no confidence-based bypass.

Prohibited:

```text
agent confidence > threshold
        ↓
auto-activate policy
```

Required:

```text
Merchant natural-language intent
        ↓
Specialist agent interpretation
        ↓
Candidate policy configuration
        ↓
MANUAL MERCHANT VALIDATION GATE
        ↓
Merchant approves or edits
        ↓
Deterministic Main Street validation
        ↓
Authoritative policy configuration
```

This gate applies to initial policy creation and subsequent agent-assisted policy modification.

---

# 7. Manual validation is a meaning-validation surface

The manual gate shall expose material policy consequences in business-facing language.

It shall not rely solely on internal semantic identifiers or ask the merchant to approve an opaque configuration.

Example:

```text
If a customer cancels less than 24 hours before the appointment:

• the appointment will be cancelled;
• the scheduled time becomes available again where capacity semantics permit;
• the £20 deposit will not be refunded.
```

The merchant must be able to identify and correct an incorrect interpretation before approval.

Merchant approval establishes intended policy meaning. It does not establish semantic validity by itself.

---

# 8. Deterministic validation gate

After merchant approval, Main Street shall deterministically validate the candidate against registered semantics and governing invariants before activation.

Validation shall reject configurations that are structurally invalid, contradictory, unreachable or otherwise semantically inconsistent.

Example contradiction:

```text
customer cancellation prohibited after 24h

AND

if customer cancels after 12h retain deposit
```

where the latter branch depends on an operation that the former makes unreachable for the overlapping period.

Manual approval shall not override deterministic invalidity.

Therefore:

```text
HUMAN GATE
    validates intended business meaning

DETERMINISTIC GATE
    validates executable semantic consistency
```

Both are required for agent-generated policy.

---

# 9. Unsupported merchant policy

Main Street may understand a merchant's intended rule without being capable of executing it.

For example:

```text
Refund 43% if the customer cancels on a rainy Tuesday
unless they have booked more than three times.
```

If weather, customer-history predicates or the requested calculation are not registered policy semantics, the correct result is:

```text
UNDERSTOOD INTENT
        ≠
SUPPORTED EXECUTABLE POLICY
```

The agent may explain supported alternatives or identify unsupported dimensions. It shall not create executable code or hidden semantic extensions.

New policy semantics require the applicable Main Street design-governance process.

---

# 10. Policy configuration is declarative

Authoritative merchant policy shall be declarative configuration.

Conceptually:

```text
Merchant policy intent
        ↓
registered policy semantics
        ↓
validated declarative configuration
        ↓
deterministic runtime evaluation
```

The merchant describes what operational rule should apply within supported semantics. The merchant does not supply procedural implementation.

This preserves Main Street's composite programming model:

- declarative configuration for variability;
- deterministic transformation/validation for semantic authority;
- imperative capability-owned runtime execution;
- selective events for post-commit reactions; and
- ports/adapters for external providers.

---

# 11. Commitment lifecycle and merchant policy are separate authorities

Booking and Appointment retain ownership of their commitment semantics.

Merchant policy determines whether supported operations are permitted and which configured consequences follow under the current facts.

Policy shall not be collapsed into commitment-state names such as:

```text
CANCELLED_AND_REFUNDED
CANCELLED_NON_REFUNDABLE
PAID_CONFIRMED
REFUND_PENDING_APPOINTMENT
```

Cancellation truth, payment truth, refund truth and fulfilment truth remain distinguishable.

Example:

```text
Appointment = CANCELLED
Refund requirement = EXISTS
Provider refund execution = FAILED
```

The failed refund shall not restore the Appointment to CONFIRMED.

---

# 12. Cancellation permission and commercial consequence are independently representable

Main Street shall support the distinction between whether an operation is permitted and what commercial consequence follows if it occurs.

Example merchant A:

```text
customer cancellation <24h
    → prohibited
```

Example merchant B:

```text
customer cancellation <24h
    → permitted
    → deposit retained
```

These are materially different policies and shall not require different Appointment lifecycle classes.

---

# 13. Capability-owned consequence execution

Policy evaluation may determine that one or more consequences should occur, but it shall not directly mutate foreign capability state.

Conceptually:

```text
Policy evaluation
        ↓
registered authorised consequences
        ↓
┌───────────────┬────────────────┬────────────────┐
▼               ▼                ▼
Booking /       Scheduling /     Payment
Appointment     capacity         operations
operation       operation
                                 │
                                 ▼
                           provider adapter
```

Notifications similarly remain owned by the Notification subsystem.

For example, an Appointment cancellation policy may result in:

- an Appointment cancellation operation;
- release/recalculation of applicable scheduled capacity;
- creation or execution of an authorised refund consequence through Payment; and
- a notification reaction.

The policy evaluator coordinates through explicit contracts. It does not become the owner of those capabilities.

---

# 14. Historical policy affinity

A later merchant policy change shall not silently reinterpret an existing customer commitment.

Main Street shall retain sufficient policy provenance to determine the authoritative policy semantics applicable when a Booking or Appointment commitment was established.

Conceptually:

```text
Appointment A100
    established under
Policy Version P7

Later merchant policy
    P8

A100
    retains applicable P7 affinity
```

unless a separately accepted and explicit operation validly changes the applicable terms for that commitment.

The implementation may use version references, immutable policy snapshots, resolved-policy evidence or another deterministic mechanism, provided the historical-affinity invariant is preserved.

---

# 15. Policy changes before commitment

Where no authoritative customer commitment yet exists, the current applicable merchant policy may change.

Before commitment, Main Street shall revalidate the policy and other material terms required by the owning operation.

If material customer-facing terms have changed after they were previously displayed, Main Street shall not silently bind the customer to materially different terms without presenting the updated terms through the applicable customer flow.

This rule does not create a policy hold unless a separate accepted semantic explicitly establishes one.

---

# 16. Agent-assisted policy modification

An agent-assisted change to an existing policy shall create a candidate revision rather than mutate the authoritative policy immediately.

Required flow:

```text
Current authoritative policy
        ↓
Agent proposes revision
        ↓
Material diff/consequence projection
        ↓
Manual merchant validation
        ↓
Merchant approval
        ↓
Deterministic validation
        ↓
New authoritative policy version
```

Existing commitments remain governed by the historical-affinity rule.

---

# 17. Fulfilment and operational outcome are distinguishable from commitment state

Main Street shall not force materially different operational facts into inaccurate commitment or payment states merely for lifecycle simplicity.

Examples include:

- service completed;
- customer no-show;
- merchant/provider no-show;
- partial fulfilment; and
- other capability-evidenced outcomes.

A no-show shall not automatically be represented as cancellation, payment failure or successful completion.

The architecture shall permit commitment truth and fulfilment/outcome truth to remain distinguishable where materially necessary.

This amendment does not freeze a universal fulfilment-outcome enumeration. Detailed outcome vocabulary shall be introduced only when capability evidence demonstrates that the distinction is required.

Merchant-configured consequences may subsequently react to registered outcomes where supported.

---

# 18. Payment and refund authority remain separate

MS-PROT-055 remains authoritative for Money, PaymentObligation, PaymentApplication, provider payment evidence and Refund semantics.

A merchant policy may determine that a refund consequence is required, permitted, reduced or absent within registered semantics.

That policy result does not itself constitute provider refund evidence.

Likewise:

```text
REFUNDED
    ≠
CANCELLED
```

A merchant may refund a customer while still honouring the underlying commitment where the merchant's valid policy and operation permit it.

Conversely, cancellation may occur without a refund where the applicable merchant policy and legal/contractual constraints permit it.

---

# 19. Policy evaluation does not replace authoritative mutation

Event-driven reactions may be used after authoritative operations commit, but events shall not replace required transactional mutation.

Where cancellation requires atomic commitment/capacity invariants, the authoritative mutation remains within the owning transactional boundary.

Post-commit events may support:

- notifications;
- analytics;
- projections;
- independent provider workflows where appropriate; and
- other decoupled reactions.

This amendment therefore preserves the established composite architecture rather than imposing a universal event-driven policy engine.

---

# 20. AI boundary

The accepted AI boundary is:

```text
AI
    probabilistic interpretation assistance

HUMAN MERCHANT
    business-intent validation authority

DETERMINISTIC MAIN STREET CORE
    semantic and execution authority
```

No AI confidence score shall substitute for merchant manual validation of agent-generated policy configuration.

No agent shall become a semantic gatekeeper deciding which supported business policy a merchant is allowed to prefer merely because another policy appears more conventional.

---

# 21. Graph-aware falsification record

The accepted design emerged through iterative falsification rather than a one-pass lifecycle design.

An earlier hypothesis risked giving Main Street excessive policy authority by defining platform-selected cancellation/refund/no-show rules.

That hypothesis was rejected after establishing the stronger invariant:

```text
Main Street does not tell merchants how to run their operations.
Main Street provides infrastructure that streamlines them.
```

The revised hypothesis established merchant policy ownership with Main Street-owned semantic representation.

A specialised NLP configuration agent was then introduced as an interpretation aid, subject to a mandatory merchant validation gate.

Falsification identified two material defects requiring refinement:

## 21.1 Ambiguous natural language

A model that forced every agent interpretation into a complete configuration could invent values such as notice periods or refund scope.

Resolution:

> Material ambiguity remains unresolved and triggers targeted clarification rather than semantic invention.

## 21.2 Policy mutation and existing commitments

A model that evaluated all commitments against the merchant's current policy could silently change the terms of existing commitments after a merchant edited policy.

Resolution:

> Existing commitments retain sufficient historical policy affinity/provenance to prevent later policy changes from silently reinterpreting them.

The revised model was then tested against:

- cancellation prohibition;
- cancellation with retained deposit;
- refundable cancellation;
- merchant-initiated cancellation;
- contradictory policy branches;
- unsupported merchant rules;
- agent ambiguity;
- agent-assisted policy modification;
- refund failure after valid cancellation;
- refund without cancellation;
- cancellation without payment;
- price-changing commitment modification;
- policy changes before commitment;
- policy changes after commitment;
- customer no-show;
- merchant/provider no-show;
- partial fulfilment;
- cross-capability capacity effects;
- Payment/refund consequences;
- Notification reactions; and
- capability ownership boundaries.

No material contradiction remained within the declared scope after the revisions above.

---

# 22. Validation matrix

| Constraint | Result |
|---|---|
| Merchant remains operating-policy authority | PASS |
| Main Street does not prescribe universal business policy | PASS |
| Policy remains within registered deterministic semantics | PASS |
| Arbitrary merchant executable logic is excluded | PASS |
| Agent may interpret but not activate policy | PASS |
| Mandatory merchant manual validation preserved | PASS |
| Ambiguous agent interpretation cannot invent material values | PASS |
| Merchant approval cannot bypass deterministic validation | PASS |
| Existing commitments preserve historical policy affinity | PASS |
| Booking/Appointment retain commitment ownership | PASS |
| Scheduling/capacity authority remains separate | PASS |
| Payment/refund authority remains separate | PASS |
| Notification authority remains separate | PASS |
| Refund failure cannot reverse commitment cancellation | PASS |
| No-show is not forced into cancellation/payment/completion truth | PASS |
| Business-category policy subclasses are unnecessary | PASS |
| Composite architecture/programming model preserved | PASS |
| AI remains probabilistic at the boundary and deterministic at the core | PASS |

---

# 23. Prohibited designs

The following are rejected:

- Main Street prescribing one universal cancellation/refund/no-show policy for merchants;
- business-category-specific policy classes where composition suffices;
- arbitrary merchant-authored executable policy code;
- agent-generated semantic extensions;
- confidence-based bypass of merchant manual validation;
- opaque manual approval screens that do not expose material consequences;
- treating merchant approval as a bypass of deterministic semantic validation;
- evaluating old commitments against new merchant policy without historical affinity;
- combined lifecycle states that collapse cancellation, payment and refund truth;
- policy evaluators directly mutating foreign capability state;
- universal event-driven mutation replacing authoritative transactional invariants; and
- premature universal fulfilment-outcome catalogues without capability evidence.

---

# 24. Accepted result

DDR-OD-006 is closed by the following governing model:

```text
MERCHANT
    decides operating policy
        ↓
SPECIALIST NLP AGENT
    interprets and proposes only
        ↓
REGISTERED MAIN STREET SEMANTICS
    bound executable meaning
        ↓
MANUAL MERCHANT VALIDATION
    confirms intended business meaning
        ↓
DETERMINISTIC VALIDATION
    protects semantic consistency
        ↓
VERSIONED DECLARATIVE POLICY
    preserves provenance
        ↓
POLICY EVALUATION
    determines authorised consequences
        ↓
CAPABILITY-OWNED CONTRACTS
    execute Booking / Appointment / Scheduling /
    Payment / Notification consequences
```

> **Main Street shall not tell merchants how to run their Booking or Appointment operations. It shall provide configurable semantics and reliable infrastructure through which merchant-defined policy can be understood, manually validated, deterministically validated, preserved and executed without surrendering semantic authority to merchants or AI agents.**
