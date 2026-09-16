# MS-PROT-042 v1.15 — Capacity-Demand Exception Feature Admission & Ownership Disposition Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.15  
**Status:** **ACCEPTED after Feature Admission, Fundamental Vision Conformance, ownership review, capacity-invariant falsification and manual approval**  
**Work package:** `MS-PROT-042-GRP-06`  
**Authority type:** Feature-admission, ownership and scope-disposition amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-042 through v1.14 only within the historical waiting-list / overbooking deferred-scope classification  
**Depends on:** MS-PROT-025; composite MS-PROT-042 through v1.14; composite MS-PROT-043; MS-PROT-059; MS-PROT-062; MS-PROT-064; MS-PROT-072; composite MS-PROT-075; MS-PROT-085; MS-PROT-086  
**Resolves:** `MS-PROT-042-GRP-06` — Capacity-Demand Exceptions  
**Implementation activation:** NONE

---

# 1. Governing Decision

The two historical GRP-06 questions SHALL NOT be treated as one capability.

The accepted disposition is:

```text
WAITING LIST
    → ADMIT

    → real target-business operation

    → promote to a separate
      bounded coordination capability

    → NOT owned by Booking
      or Appointment


OVERBOOKING
    → DO NOT ADMIT
      into the initial Main Street portfolio

    → ordinary capacity invariants remain intact

    → no merchant policy may manufacture
      excess commitment authority
```

Therefore:

```text
GRP-06
    ≠
one MS-PROT-042 feature
```

---

# 2. Why Fresh Admission Was Required

MS-PROT-042 v1.2 historically listed:

```text
waiting-list semantics

overbooking policy
```

as deferred questions.

Their appearance in that historical list does not establish:

```text
product necessity

feature admission

semantic ownership

implementation priority
```

Current DESIGN-RULES require each material capability to justify itself through Main Street's own product purpose.

---

# 3. Waiting-List Feature Admission

## 3.1 Representation Test — PASS

A customer may legitimately be:

```text
interested in a specific unavailable
booking/appointment opportunity

but

not yet committed
```

This business fact is materially distinct from:

```text
Booking

Appointment

Hold

Enquiry

Conversation

Merchant Attention
```

Example:

```text
Customer:
"If someone cancels the 2pm haircut,
please put me on the list."
```

The merchant has undertaken a real operational coordination responsibility.

No Booking or Appointment yet exists.

---

# 4. Waiting List Is Not an Enquiry

An Enquiry can represent initial customer contact.

It does not inherently represent:

```text
authoritative waitlist membership

queue ordering

exact unavailable demand

withdrawal from the list

promotion after capacity becomes available

duplicate-safe queue participation
```

Using Enquiry as the authoritative waitlist would therefore overload communication semantics with capacity-demand coordination truth.

Rejected:

```text
Enquiry
    secretly doubles as
Waitlist Entry
```

---

# 5. Waiting List Is Not Merchant Attention

Merchant Attention may surface:

```text
capacity became available

waitlisted demand needs a decision

promotion failed

customer response requires handling
```

But Merchant Attention owns handling workflow.

It does not own:

```text
who is waiting

what they are waiting for

queue precedence

whether their demand remains current
```

---

# 6. Waiting-List Coordination Test — PASS

A waitlist legitimately coordinates:

```text
customer demand
        +
Booking / Appointment owner-qualified
availability evidence
        +
Resource / Allocation capacity
        +
customer communication
```

without taking ownership of any of those source facts.

Canonical:

```text
source capability
    owns availability / commitment

Waitlist
    owns unresolved queued demand

Notification
    owns delivery
```

---

# 7. Waiting-List Administrative-Compression Test — PASS

Without native bounded coordination, target merchants commonly need to maintain:

```text
paper cancellation lists

notes

spreadsheets

WhatsApp messages

manual callback ordering

memory of who asked first

manual removal after booking
```

Main Street can safely absorb much of this recurring administration.

Therefore the Administrative-Compression Test passes.

---

# 8. Waiting-List Vision Conformance

The capability directly supports:

```text
merchant says:
"put them on the cancellation list"

        ↓

Main Street translates

        ↓

bounded authoritative waitlist semantics
```

Normal staff need only understand:

```text
customer is waiting

space became available

customer accepted / declined / withdrew
```

They do not need to understand queue infrastructure.

**Vision outcome: VISION-CONFORMING**

---

# 9. Waiting-List Ownership

A waitlist entry exists precisely because:

```text
Booking does not yet exist
or
Appointment does not yet exist
```

Therefore Booking or Appointment cannot own waitlist membership without expanding their commitment semantics into pre-commitment demand management.

The new authority should instead own only:

```text
waitlist participation

exact demand affinity

ordering / precedence semantics

withdrawal

promotion coordination

promotion outcome
```

It SHALL NOT own:

```text
Booking

Appointment

Availability

Resource capacity

Allocation

Payment

Notification delivery

Customer identity

Merchant Attention
```

---

# 10. Waiting-List Promotion

The admitted capability SHALL be promoted to a new design node.

Recommended authority:

```text
MS-PROT-089
Capacity Waitlist &
Availability Opportunity Coordination Model
```

The exact title may be normalised during that proposal, but the semantic ownership is fixed by this disposition:

> **Waitlist owns queued unmet demand, not the eventual customer commitment.**

---

# 11. Waitlist Does Not Reserve Capacity by Membership

Hard boundary for the future authority:

```text
Waitlist Entry
    ≠ Hold

Waitlist Entry
    ≠ Allocation

Waitlist Entry
    ≠ Booking

Waitlist Entry
    ≠ Appointment
```

Joining a waitlist cannot reduce ordinary availability.

Any future protected offer must use separately accepted source-owned capacity-protection authority.

---

# 12. Waitlist Cannot Manufacture Availability

Waitlist may react when a source capability establishes relevant opportunity.

It SHALL NOT infer:

```text
Appointment slot became available

Booking capacity exists

Resource can now be allocated
```

from:

```text
clock passage

customer cancellation notification

stale projection

AI prediction

absence of a database row
```

Source authority must establish the applicable current evidence.

---

# 13. Waitlist Cannot Guarantee Commitment

Even where a customer is:

```text
first on the waitlist
```

that does not itself mean:

```text
Booking guaranteed

Appointment guaranteed

capacity owned
```

Final commitment remains subject to the ordinary source-authoritative revalidation and concurrency invariant.

---

# 14. No Generic Queue Platform

Feature Admission applies only to capacity-related customer demand.

It does not admit:

```text
generic work queues

support-ticket queues

task queues

lead queues

sales pipelines

CRM stages

restaurant table-host systems

warehouse queues

employee queues

generic workflow routing
```

A mature generic queue engine is not needed.

---

# 15. Overbooking — Representation Test

Some legitimate businesses do intentionally accept more customer commitments than their ordinary expected usable capacity.

Therefore, in the abstract:

**Representation Test: PASS**

This alone does not require Main Street to implement the feature.

Feature Admission is followed by Vision Conformance and proportionality review.

---

# 16. Meaning of Overbooking

Overbooking is not:

```text
two customers saw stale availability
```

That is a concurrency problem.

Overbooking means intentionally permitting:

```text
authoritative customer commitments
    >
ordinary simultaneously satisfiable capacity
```

under a merchant/platform policy.

This is a fundamentally different semantic decision.

---

# 17. Overbooking Would Deliberately Relax a Capacity Invariant

Accepted persistence/consistency authority currently protects scenarios such as:

```text
two customers booking one slot

two bookings allocating one room
```

through authoritative concurrency enforcement.

An overbooking capability would need an explicit new invariant such as:

```text
committed demand
    <=
physical capacity
+
authorised oversubscription allowance
```

This is not a configuration tweak.

It is a material change to commitment admission.

---

# 18. Merchant Policy Alone Cannot Create Overbooking Authority

MS-PROT-042 v1.3 preserves merchant policy freedom only within registered supported semantics.

Therefore a merchant cannot currently configure:

```text
allow two bookings for one room

overbook each class by 3

accept 110% capacity
```

and thereby override capacity protection.

Canonical:

```text
merchant policy freedom
    ≠
authority to violate
unmodified platform invariants
```

---

# 19. Correct Overbooking Is Not a Small Feature

A truthful overbooking model would potentially require:

```text
oversubscription limits

affected commitment classes

expected non-utilisation evidence

risk calculations

capacity recovery strategy

priority/displacement rules

customer-remediation policy

refund/compensation coordination

merchant-side non-honour consequences

jurisdiction/regulatory implications

customer communication

auditability

forecast/model governance
```

Without those semantics, Main Street would simply be permitting unsafe double booking.

---

# 20. AI Prediction Does Not Solve Overbooking

Rejected:

```text
AI predicts 15% no-show rate
        ↓
accept 15% excess bookings
```

A probability does not create:

```text
capacity

customer-remediation authority

commercial consequence authority

regulatory permission
```

and AI cannot authoritatively relax commitment invariants.

---

# 21. Overbooking Administrative-Compression Review

Overbooking could theoretically reduce unused capacity.

But it would introduce significant additional:

```text
merchant policy

risk handling

exception handling

customer remediation

financial consequences
```

The administrative burden created is disproportionate to the initial target-market need.

A simpler conforming mechanism exists:

```text
real capacity fills
        ↓
additional demand enters Waitlist
        ↓
capacity becomes available
        ↓
demand is promoted
```

---

# 22. Target-Market Proportionality

For Main Street's current micro/small-business target, a generic oversubscription engine is materially deeper than required to represent ordinary local-business operations safely.

It resembles specialist:

```text
hospitality revenue management

airline inventory optimisation

large event yield management
```

more than minimum operating infrastructure.

---

# 23. Overbooking Vision Outcome

**VISION-NON-CONFORMING for the initial Main Street portfolio**

Reason:

The feature would add substantial customer-risk, capacity, forecasting and remediation complexity when a simpler admitted waitlist mechanism can address the principal excess-demand administration problem without intentionally creating unsatisfiable simultaneous commitments.

---

# 24. Overbooking Recommendation

**RECOMMENDATION: REJECT**

for the initial product portfolio.

`REJECT` here means:

```text
not an accepted Main Street capability

no current semantic authority

no hidden configuration switch

no implementation
```

It does not mean the question can never be reconsidered.

Future reconsideration requires:

```text
new business evidence
+
fresh Feature Admission
+
fresh Vision Conformance
+
new explicit capacity-invariant design
```

---

# 25. Capacity Configuration Must Not Become Hidden Overbooking

Rejected circumvention:

```text
physical capacity = 4

merchant wants 6 commitments

        ↓

configure capacity = 6
```

where Main Street knows the authoritative meaning is physically/simultaneously four.

Configuration must represent genuine supported business capacity.

It SHALL NOT be used to disguise intentional oversubscription.

---

# 26. Waitlist Is Preferred Excess-Demand Mechanism

For the initial portfolio:

```text
capacity available
    → source commitment may proceed

capacity unavailable
    → eligible customer may join Waitlist

later capacity opportunity
    → Waitlist coordinates promotion

source commitment
    → ordinary source-owner authority
```

This preserves truthful capacity semantics.

---

# 27. Fundamental Vision Comparison

| Test | Waiting List | Overbooking |
|---|---|---|
| Representation | PASS | PASS in some domains |
| Coordination | PASS | Materially alters commitment admission |
| Administrative compression | PASS | Mixed; adds major remediation complexity |
| Ordinary staff simplicity | PASS | Higher exception burden |
| Target-market proportionality | PASS | FAIL for initial portfolio |
| Simpler conforming alternative | No equivalent existing authority | **Waitlist** |
| Capacity invariant impact | Preserves | Deliberately relaxes |
| Vision outcome | **VISION-CONFORMING** | **VISION-NON-CONFORMING** |

---

# 28. Falsification — Full Salon Day

Scenario:

```text
all haircut slots full

customer asks:
"let me know if 2pm opens"
```

Without Waitlist, merchant manually records and remembers the request.

With admitted bounded Waitlist:

```text
queued demand can be represented
without creating Appointment
```

**WAITLIST ADMISSION PASS**

---

# 29. Falsification — Last Hotel Room

Scenario:

```text
one room remains

two customers commit concurrently
```

Expected:

```text
ordinary capacity invariant
permits only valid coexistence
```

The second customer may become waitlist-eligible.

Main Street does not call the race:

```text
overbooking
```

**PASS**

---

# 30. Falsification — Merchant Wants Two Customers for One Appointment Slot

Merchant says:

```text
"Book both.
One usually cancels."
```

Initial Main Street result:

```text
second incompatible commitment
not admitted
```

Merchant may use the admitted Waitlist instead.

**PASS**

---

# 31. Falsification — AI Predicts Likely Cancellation

AI gives:

```text
Customer A has 70% chance of cancelling
```

Expected:

```text
existing capacity remains committed
```

No excess commitment authority is created.

**PASS**

---

# 32. Falsification — Low-Software-Capacity Merchant

Merchant says:

```text
"If anyone cancels,
give the space to the next person."
```

Expected architectural result:

```text
Waitlist capability
absorbs queue administration

merchant does not configure:
capacity ledgers
event handlers
priority algorithms
notification workflows
```

**PASS**

---

# 33. Falsification — ERP Drift

Proposed feature expands into:

```text
lead queues

customer stages

campaigns

generic queue designer

multi-rule workflow automation
```

Expected:

```text
outside admitted scope
```

**PASS**

---

# 34. Ownership Matrix

| Truth | Owner |
|---|---|
| Waitlist membership | Future Waitlist authority |
| Waitlist ordering/precedence | Future Waitlist authority |
| Demand being promoted | Future Waitlist authority |
| Booking availability | Booking / applicable Resource authority |
| Appointment schedulability | Scheduling |
| Capacity | Resource / Allocation |
| Booking | Booking |
| Appointment | Appointment |
| CustomerContext | Customer relationship authority |
| Customer notification delivery | Notification |
| Human handling/exception | Merchant Attention where registered |
| Payment/refund | Payment |
| Overbooking authority | **None — not admitted** |

---

# 35. Hard Invariants

```text
INV-042-V115-001
Historical placement of a deferred question does not establish feature admission or ownership.

INV-042-V115-002
Waitlist is admitted as a separate coordination capability.

INV-042-V115-003
Booking and Appointment do not own Waitlist membership merely because eventual commitment may belong to them.

INV-042-V115-004
Waitlist Entry is not Booking, Appointment, Hold or Allocation.

INV-042-V115-005
Waitlist membership cannot reserve capacity.

INV-042-V115-006
Waitlist cannot manufacture Booking availability or Appointment schedulability.

INV-042-V115-007
Waitlist promotion cannot bypass ordinary source-owner commitment revalidation.

INV-042-V115-008
The admitted Waitlist scope is capacity-related customer demand, not a generic queue engine.

INV-042-V115-009
Overbooking is not admitted into the initial Main Street portfolio.

INV-042-V115-010
Merchant policy cannot create overbooking authority where no accepted overbooking semantics exist.

INV-042-V115-011
AI prediction cannot create excess commitment capacity.

INV-042-V115-012
Capacity configuration cannot knowingly disguise intentional overbooking.

INV-042-V115-013
Ordinary concurrency protection remains authoritative.

INV-042-V115-014
Future overbooking consideration requires fresh Feature Admission and explicit capacity-invariant redesign.

INV-042-V115-015
Waitlist is the preferred initial mechanism for excess demand after real capacity is exhausted.
```

---

# 36. Rejected Alternatives

Rejected:

```text
Waiting List owned by Booking

Waiting List owned by Appointment

Enquiry doubles as Waitlist

Merchant Attention doubles as Waitlist

Waitlist Entry reserves capacity

generic CRM lead queue

generic queue engine

overbooking as a boolean merchant setting

overbookBy = N

capacity inflation used as hidden overbooking

AI no-show prediction creates extra capacity

universal oversubscription percentage

silent double-booking

provider decides overbooking policy
```

---

# 37. GRP-06 Disposition

`MS-PROT-042-GRP-06` is resolved with two explicit dispositions:

```text
WAITING LIST
    → ADMITTED
    → RECLASSIFIED OUT OF MS-PROT-042
    → promote to MS-PROT-089 design node

OVERBOOKING
    → REJECTED
      for initial portfolio
```

No MS-PROT-042 semantic DQ remains from the grouped v1.9 cleanup.

---

# 38. Sequencing Consequence

Acceptance does **not** immediately return execution to an unrelated global node.

Because Waiting List passed Feature Admission as a direct result of this grouped cleanup:

```text
MS-PROT-042 v1.15
        ↓
MS-PROT-089
Capacity Waitlist /
Availability Opportunity Coordination
        ↓
complete its DESIGN-RULES lifecycle
        ↓
then return to ordinary
SEQUENCE.md dependency frontier
```

This preserves the manual direction to solve the grouped unresolved decisions rather than merely rename them.

---

# 39. Implementation-Rules Impact

**Implementation activation: NONE**

This amendment does not authorise:

```text
waitlist code

database queue tables

automatic customer promotion

overbooking configuration

capacity-invariant changes
```

The future MS-PROT-089 authority must first define the admitted Waitlist semantics and receive explicit approval.

---

# 40. Recommendation and Accepted Disposition

The accepted disposition:

1. admits the part of GRP-06 that materially supports Main Street's mission;
2. rejects disproportionate oversubscription complexity;
3. preserves truthful capacity invariants;
4. prevents Booking/Appointment semantic gravity;
5. promotes Waitlist to the owner it actually needs;
6. keeps excess-demand handling aligned with exception-driven, low-administration operation.

The amendment passed:

```text
Feature Admission
Fundamental Vision Conformance
ownership review
target-market proportionality
anti-ERP review
capacity-invariant review
falsification
manual approval
```

**Implementation activation remains NONE.**
