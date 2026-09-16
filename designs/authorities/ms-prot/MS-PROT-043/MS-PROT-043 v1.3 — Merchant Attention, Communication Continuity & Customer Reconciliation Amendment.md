# MS-PROT-043 v1.3 — Merchant Attention, Communication Continuity & Customer Reconciliation Amendment

**Document ID:** MS-PROT-043  
**Version:** 1.3  
**Status:** **ACCEPTED after proposal rejection, redesign and manual approval**  
**Amends:** MS-PROT-043 v1.2  
**Closes:** DDR-OD-007 — Enquiry / Conversation / CustomerContext lifecycle and reconciliation  
**Purpose:** Separate durable business truth, communication continuity, merchant work/attention and customer-identity reconciliation so Main Street can streamline merchant handling without imposing artificial lifecycles, CRM funnels or destructive identity merging.

---

# 1. Governing decision

Main Street shall not force merchant work-management state into Enquiry, Conversation or CustomerContext merely because merchants need to know what requires attention.

The accepted model separates three graphs:

```text
1. BUSINESS-TRUTH GRAPH
2. COMMUNICATION GRAPH
3. MERCHANT-ATTENTION GRAPH
```

Customer reconciliation is represented through governed merchant-scoped graph relationships rather than destructive object merging.

Governing principle:

> **Business objects preserve business truth. Communication objects preserve communication history. Merchant-attention infrastructure determines what currently requires merchant action. Reconciliation relationships express identity conclusions without rewriting historical ownership.**

---

# 2. Enquiry is a durable business fact

An Enquiry represents the durable fact that a customer asked the merchant something, requested information or requested further action.

Main Street shall not require the Enquiry itself to cycle through a universal operational workflow such as:

```text
OPEN
RESPONDED
WAITING
CLOSED
REOPENED
```

where those labels merely describe the merchant's current handling position.

The Enquiry remains independently identifiable and auditable regardless of whether the merchant has replied, handled it, snoozed it or later receives additional communication.

The following are therefore distinguishable:

```text
Enquiry truth
    ≠
merchant attention state
```

---

# 3. Response is communication evidence, not Enquiry lifecycle

Whether the merchant or customer has responded shall be derived from communication facts and projections where required.

Examples include:

- last inbound activity;
- last outbound activity;
- unread activity;
- response timestamp;
- responding participant; and
- related message history.

Main Street shall not introduce a universal `RESPONDED` Enquiry lifecycle state solely to represent facts already present in communication history.

---

# 4. Conversation is durable communication context

Conversation represents continuing merchant-scoped communication context.

A Conversation may contain Messages and registered relationships to CustomerContext, Enquiry, Booking, Appointment, Order or other supported subjects.

Main Street shall not introduce a universal `OPEN/CLOSED` Conversation business lifecycle unless future evidence demonstrates a distinct invariant requiring it.

Whether further communication is permitted may instead depend on the appropriate authorities, including:

- participant authority;
- channel availability;
- merchant communication policy;
- exposure rules;
- abuse/safety controls;
- retention/privacy controls; and
- other registered communication semantics.

Archival, inbox visibility, muting or similar merchant work-management behaviour shall not automatically redefine Conversation business truth.

---

# 5. CustomerContext is a durable merchant-scoped relationship

CustomerContext represents the merchant-scoped durable relationship context through which Main Street can associate relevant customer interactions and operations.

It shall not become a universal CRM funnel.

Main Street rejects universal CustomerContext states such as:

```text
LEAD
PROSPECT
QUALIFIED
CUSTOMER
LOST
```

unless a future separately accepted capability explicitly requires such semantics for its own bounded purpose.

CustomerContext also shall not acquire generic `ACTIVE/INACTIVE` state merely to approximate recent activity.

---

# 6. Merchant Attention is a separate infrastructure concern

Main Street shall provide merchant-attention/work-handling infrastructure independently from the durable business objects that generated the work.

Merchant Attention may determine whether an item currently requires merchant action based on registered facts and configured handling policy.

Potential attention semantics may include, where justified:

- requires attention;
- currently does not require attention;
- unread inbound activity;
- assignment to authorised staff;
- snoozed-until time;
- last inbound/outbound activity;
- merchant-configured priority; and
- other registered handling semantics.

These are infrastructure/work-management semantics and shall not automatically become lifecycle states of Enquiry, Conversation, Booking or Appointment.

---

# 7. Merchant Attention is adaptable

Main Street shall not require every merchant to operate a sophisticated inbox workflow.

An information publisher may need only a minimal projection of new/unhandled communication.

A merchant with staff and substantial customer operations may use richer configured attention semantics such as assignment, snoozing or prioritisation where those capabilities are supported.

This preserves the governing Main Street principle:

> **Main Street streamlines merchant operations without prescribing how the merchant must run them.**

---

# 8. New communication does not mutate Enquiry automatically

Where a customer sends a new Message after the merchant previously considered an Enquiry handled, Main Street shall record the communication fact and update applicable attention projections.

It shall not require:

```text
Enquiry CLOSED → OPEN
```

merely because another Message arrived.

Example:

```text
Enquiry E1
    durable request

merchant handles request
    ↓
attention no longer required

customer sends another message
    ↓
Conversation receives Message
    ↓
Merchant Attention may require action again

Enquiry E1 remains the same durable business fact
```

---

# 9. Related capability changes do not automatically mutate Enquiry

An Enquiry may result in or relate to an Appointment, Booking, Order or other object.

That relationship shall be expressed through registered provenance/subject relationships rather than object transformation.

Example:

```text
Enquiry E1
    ↓ ORIGINATES / RELATED_TO
Appointment A1
```

Creating A1 does not transform E1 into an Appointment and does not require an `ENQUIRY_CONVERTED` state.

Likewise, later cancellation or modification of A1 shall not automatically reopen or otherwise mutate E1.

If the resulting event creates a new merchant work requirement, Merchant Attention may surface that requirement independently.

---

# 10. Conversation does not own business mutation

Messages and Conversations preserve communication facts.

A Message containing business intent does not itself mutate Booking, Appointment, Order, Payment or other authoritative state.

Example:

```text
Message:
"Cancel my booking"
        ↓
intent interpretation
        ↓
authorised Booking operation
        ↓
Booking capability
```

Rejected:

```text
Message
    ↓
direct Booking state mutation
```

This remains consistent with Main Street's AI boundary: AI may infer intent but does not become execution authority.

---

# 11. CustomerContext association

Where Main Street already has reliable authoritative correlation between an interacting customer and an existing merchant-scoped CustomerContext, new interactions may reuse that CustomerContext directly.

Example:

```text
authenticated customer identity
        ↓
existing merchant-scoped CustomerContext C1
        ↓
new Enquiry / Appointment / Booking → C1
```

This is association, not reconciliation.

Mutable contact-value equality alone shall not constitute authoritative identity correlation.

---

# 12. Weak matching and AI inference

Contact details, names and other weak evidence may be used to infer a possible existing merchant-customer relationship.

Example:

```text
same email/name
        ↓
possible existing customer
```

but not:

```text
same email/name
        ↓
automatic identity reconciliation
```

A specialist AI agent may:

- infer possible matches;
- explain evidence;
- propose reconciliation; and
- request merchant review.

It shall not create authoritative reconciliation merely from probabilistic inference.

Specialists infer; they do not create semantics or identity truth.

---

# 13. Non-destructive CustomerContext reconciliation

Where two CustomerContexts within the same merchant scope are authoritatively determined to represent the same merchant-customer relationship, Main Street shall reconcile them through a governed graph relationship or semantically equivalent non-destructive mechanism.

Conceptually:

```text
CustomerContext C2
        │
  RECONCILED_TO
        ▼
CustomerContext C1
```

Reconciliation shall not require rewriting every historical capability-owned reference from C2 to C1.

Existing objects retain their original relationships and provenance.

Example:

```text
C1
 ├── Enquiry E1
 └── Appointment A1

C2
 ├── Booking B1
 └── Conversation V1

C2 RECONCILED_TO C1
```

After reconciliation:

```text
E1 remains related to C1
A1 remains related to C1
B1 remains related to C2
V1 remains related to C2
```

A merchant-facing projection may resolve C1 and reconciled contexts into one unified history without rewriting source truth.

---

# 14. Reconciliation invariants

A CustomerContext reconciliation relationship shall satisfy at least the following invariants:

1. both contexts belong to the same merchant scope;
2. the relationship is explicitly authorised;
3. provenance/evidence of the reconciliation is retained;
4. the reconciliation graph is acyclic;
5. deterministic resolution can identify the current reconciliation root where required;
6. reconciliation does not mutate foreign capability-owned historical objects merely to simplify projection; and
7. reconciliation can be reversed/separated without reconstructing original history.

Cross-merchant reconciliation is prohibited for merchant operational context.

---

# 15. Reconciliation reversal

Where a reconciliation is later determined to be incorrect, Main Street shall be able to remove or reverse the reconciliation relationship without reconstructing historical capability ownership.

Example:

```text
C2 RECONCILED_TO C1
        ↓
remove reconciliation
```

The original histories remain:

```text
C1 → original C1-linked objects
C2 → original C2-linked objects
```

Main Street shall not guess how objects created after reconciliation should be reassigned to another CustomerContext.

Where such correction is materially required, it shall use an explicit authorised operation owned by the applicable capability or another accepted reconciliation mechanism.

---

# 16. Reconciliation graph resolution

Reconciliation may form an acyclic chain where implementation needs require it.

Example:

```text
C3 → C2 → C1
```

A deterministic resolver may resolve C3 to reconciliation root C1 for merchant-facing projections and future association where appropriate.

Cycles such as:

```text
C1 → C3 → C2 → C1
```

are invalid and shall be rejected deterministically.

The implementation may flatten or optimise reconciliation internally if historical provenance and reversibility invariants remain intact.

---

# 17. Future activity after reconciliation

Where a reconciled relationship has an established current/root CustomerContext, new interactions may normally associate with that resolved context.

This does not retroactively rewrite historical objects attached to reconciled contexts.

If reconciliation is later reversed, Main Street shall not automatically infer that post-reconciliation objects belonged to the previously subordinate context.

Any material reassignment requires explicit governed correction.

---

# 18. Unified merchant-facing history is a projection

Main Street may present a unified customer history by resolving CustomerContext reconciliation relationships and aggregating permitted merchant-scoped projections.

Conceptually:

```text
CustomerContext C1
    +
reconciled contexts
    +
permitted related projections
        ↓
Unified merchant-facing customer history
```

The unified view shall not imply that underlying historical references were always attached to one physical CustomerContext.

Projection convenience shall not overwrite provenance.

---

# 19. AI concierge integration

The Main Street merchant AI concierge may use the Business Truth, Communication and Merchant Attention graphs to help merchants understand and operate their business.

Examples:

```text
"Which customers need a reply?"
"Show me enquiries about tomorrow's appointments."
"These two records look like the same customer."
```

The concierge and specialist agents may infer, explain and propose supported operations.

They shall not:

- create new semantics;
- silently reconcile CustomerContexts;
- mutate capability-owned objects directly;
- treat attention projections as business truth; or
- bypass applicable manual/deterministic validation gates.

The AI task graph may navigate these graphs but shall not redefine their authority.

---

# 20. Composite architecture consequence

The accepted model preserves Main Street's composite architecture and programming model:

```text
Business objects
    → capability/domain modelling

Merchant handling variability
    → declarative/configurable attention semantics

Identity reconciliation
    → governed graph relationship

Merchant views
    → projections

AI assistance
    → probabilistic inference at boundary

Authoritative operations
    → deterministic capability-owned execution
```

No universal CRM engine, support-ticket engine or destructive customer-merge subsystem is required.

---

# 21. Rejected proposal record

An earlier DDR-OD-007 proposal attempted to close the problem using:

```text
Enquiry OPEN ↔ CLOSED
Conversation durable context
CustomerContext reconciliation
```

with explicit Enquiry reopening.

That proposal was manually rejected because it still treated merchant handling state as if it belonged intrinsically to the Enquiry lifecycle.

The redesign asked the more fundamental operational questions:

- what business truth must remain durable?
- what communication history must remain durable?
- what currently requires merchant attention?
- what identity conclusion is being made?

This produced the accepted separation of Business Truth, Communication, Merchant Attention and Reconciliation concerns.

---

# 22. Graph-aware falsification record

The revised model was tested against:

- merchant response to an Enquiry;
- customer reply after prior handling;
- Enquiry leading to Appointment creation;
- later Appointment cancellation;
- one Conversation relating to multiple business objects;
- information publishers requiring only minimal inbox handling;
- operational merchants requiring richer attention handling;
- authenticated returning customers;
- same-name/same-email weak matching;
- AI-assisted possible-customer inference;
- genuine duplicate CustomerContexts;
- mistaken reconciliation;
- reconciliation reversal;
- reconciliation chains/cycles;
- future activity after reconciliation; and
- cross-merchant isolation.

The model rejected the following unnecessary or unsafe abstractions:

```text
Enquiry RESPONDED lifecycle state
Enquiry OPEN/CLOSED as universal work-management lifecycle
automatic Enquiry reopening on inbound message
universal Conversation OPEN/CLOSED business lifecycle
universal CustomerContext CRM funnel
same-contact automatic identity merge
destructive CustomerContext merge
AI-authorised reconciliation
cross-merchant CustomerContext reconciliation
Enquiry-to-Appointment object transformation
```

No material contradiction remained within the approved scope.

---

# 23. Validation matrix

| Constraint | Result |
|---|---|
| Business truth separated from merchant work state | PASS |
| Communication history remains independently durable | PASS |
| Enquiry avoids artificial support-ticket lifecycle | PASS |
| Conversation avoids overloaded generic status | PASS |
| CustomerContext avoids universal CRM funnel | PASS |
| Merchant Attention can adapt to merchant operational needs | PASS |
| New messages can surface work without mutating Enquiry | PASS |
| Related capability changes do not mutate Enquiry automatically | PASS |
| Reliable existing association can reuse CustomerContext | PASS |
| Weak contact matches cannot create identity truth | PASS |
| AI inference cannot authorise reconciliation | PASS |
| Reconciliation is merchant-scoped | PASS |
| Reconciliation preserves historical provenance | PASS |
| Reconciliation is reversible without reconstructing history | PASS |
| Reconciliation graph cycles are deterministically rejected | PASS |
| Unified customer history remains a projection | PASS |
| Cross-capability ownership remains intact | PASS |
| Composite architecture/programming model preserved | PASS |

---

# 24. Prohibited designs

The following are rejected:

- using Enquiry lifecycle as a universal merchant inbox workflow;
- treating `RESPONDED`, `WAITING` or similar communication facts as universal Enquiry states;
- automatically reopening Enquiry on every inbound Message;
- universal Conversation `OPEN/CLOSED` state without a distinct invariant;
- universal CRM funnel states on CustomerContext;
- destructive duplicate-customer merges that rewrite historical foreign references;
- identity reconciliation based solely on mutable contact equality;
- AI-authorised reconciliation without applicable human/authorised validation;
- cross-merchant CustomerContext reconciliation for merchant operational history;
- cycles in the reconciliation graph;
- projections rewriting source provenance; and
- direct Message/Conversation mutation of capability-owned business commitments.

---

# 25. Accepted result

DDR-OD-007 is closed by the following architecture:

```text
BUSINESS TRUTH GRAPH
    Enquiry
    CustomerContext
    Booking / Appointment / Order / ...

COMMUNICATION GRAPH
    Conversation
    Message
    typed relationships

MERCHANT ATTENTION GRAPH
    actionable/unhandled work
    assignment/snooze/priority where configured

IDENTITY RECONCILIATION GRAPH
    merchant-scoped governed CustomerContext relationships

AI
    infers and proposes across these graphs
    but does not create semantics or authority
```

> **Main Street shall preserve durable business and communication truth while projecting merchant attention separately and reconciling duplicate customer relationships through governed, reversible graph relationships. Operational convenience shall not be purchased by corrupting provenance, imposing a universal CRM/support workflow, or granting AI identity authority.**
