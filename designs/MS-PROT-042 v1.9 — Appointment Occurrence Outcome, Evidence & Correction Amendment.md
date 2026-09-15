# MS-PROT-042 v1.9 — Appointment Occurrence Outcome, Evidence & Correction Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.9  
**Status:** **ACCEPTED by manual approval on 9 September 2026**  
**Approved:** Manual approval of the complete proposed authority plus explicit grouped-deferred-scope refinement on 9 September 2026  
**Authority type:** Appointment semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-042 through v1.8 within Appointment post-commit occurrence/outcome truth, outcome evidence, outcome correction and ordinary post-outcome mutation  
**Depends on:** composite MS-PROT-042 through v1.8; composite MS-PROT-043; MS-PROT-054; MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-069; MS-PROT-072  
**Completes:** the Appointment-specific outcome/lifecycle scope expressly left open by MS-PROT-042 v1.2 and bounded by MS-PROT-042 v1.3 §17  
**Does not resolve:** exact Booking lifecycle/outcome semantics or the grouped retained MS-PROT-042 work packages in §75  
**Implementation activation:** NONE

---

# 1. Governing Decision

Main Street SHALL represent the authoritative operational outcome of an Appointment separately from:

```text
Appointment commitment
Appointment cancellation
Appointment rescheduling
Scheduling availability
Payment
Refund
Workforce time
broader service/work fulfilment
customer satisfaction
```

An Appointment establishes:

> an agreed customer-related interval during which a supported service or interaction is expected to occur.

This amendment establishes how Main Street later records what happened to that **scheduled interaction**.

Canonical:

```text
Appointment commitment
        ↓
scheduled interaction becomes due
        ↓
authoritative evidence
        ↓
Appointment Occurrence Outcome
```

Hard rule:

> **Time passing does not create an Appointment Occurrence Outcome.**

---

# 2. Why This Amendment Is Required

The accepted Appointment model already distinguishes confirmed, cancelled and rescheduled commitment consequences but deliberately refused to infer completion from the clock.

MS-PROT-042 v1.3 further established that facts such as:

```text
service completed
customer no-show
merchant/provider no-show
partial fulfilment
```

must not be forced into cancellation, payment or generic lifecycle state merely for software convenience.

The missing contract is therefore:

```text
who owns the outcome?
what exact outcome vocabulary exists?
what evidence may establish it?
how is it corrected?
what does it imply?
what does it explicitly not imply?
```

This amendment closes that boundary for Appointment.

---

# 3. Feature Admission

## Representation Test — PASS

Without an Appointment outcome model, Main Street cannot faithfully distinguish:

```text
appointment occurred
customer did not attend
scheduled interaction partly occurred
scheduled interaction failed on merchant side
nothing authoritative is yet known
```

for service-based businesses.

## Coordination Test — PASS

Appointment outcome may legitimately inform downstream:

```text
merchant policy
analytics
customer communication
future review solicitation
operational attention
```

without allowing those downstream capabilities to invent Appointment truth.

## Administrative-Compression Test — PASS

Main Street can eventually consume exact operational evidence and reduce manual follow-up while retaining a simple human fallback.

## ERP-drift test — PASS

This amendment does not create:

```text
work orders
project management
case management
field-service management
service-delivery workflow builder
generic job-status engine
```

It represents only the outcome of an existing Appointment commitment.

---

# 4. Appointment Owns Appointment Occurrence Outcome

Appointment SHALL own the authoritative outcome of the customer-related interaction represented by one exact Appointment.

Rejected:

```text
Calendar determines completion
Notification determines completion
Payment determines completion
Review/Reputation determines completion
AI determines completion
Workforce determines completion
provider meeting status determines completion
```

Accepted:

```text
Appointment authority
    consumes sufficient accepted evidence
        ↓
    establishes Appointment Occurrence Outcome
```

Other capabilities may consume that outcome.

They MUST NOT independently recreate it.

---

# 5. Appointment Occurrence Outcome Is Not Universal Service Fulfilment

An Appointment concerns the scheduled interaction.

It may or may not represent the entirety of a broader business service.

Example:

```text
Mechanic Appointment
09:00 vehicle drop-off
```

The customer and merchant may successfully complete that Appointment interaction.

That does not prove:

```text
vehicle repaired
parts installed
repair accepted
invoice settled
vehicle collected
```

Likewise:

```text
Solicitor consultation occurred
```

does not prove:

```text
legal matter completed
```

Hard invariant:

> **Appointment occurrence establishes only the outcome of the scheduled Appointment interaction. It SHALL NOT manufacture broader work/service fulfilment truth.**

---

# 6. Initial Appointment Outcome Vocabulary

The initial closed outcome vocabulary is:

```text
OCCURRED
PARTIAL_OCCURRENCE
CUSTOMER_NO_SHOW
MERCHANT_SIDE_NON_OCCURRENCE
```

No generic merchant-configurable outcome names are introduced.

No AI-generated outcome values are permitted.

---

# 7. `OCCURRED`

`OCCURRED` means:

> **Sufficient authoritative evidence establishes that the customer-related interaction represented by the Appointment materially took place.**

It does not mean:

```text
customer was satisfied
every broader service deliverable was completed
payment was received
staff worked the scheduled duration
no complaint exists
no refund is due
review solicitation is universally appropriate
```

Example:

```text
Haircut appointment
        ↓
scheduled customer/stylist interaction takes place
        ↓
OCCURRED
```

---

# 8. `PARTIAL_OCCURRENCE`

`PARTIAL_OCCURRENCE` means:

> **Sufficient authoritative evidence establishes that the scheduled Appointment interaction began or materially occurred, but did not occur to the extent required to classify the interaction as `OCCURRED`.**

It does not establish:

```text
percentage complete
amount payable
refund amount
fault
customer satisfaction
broader service fulfilment percentage
```

Those consequences require their own authority.

Main Street SHALL NOT invent a numeric completion percentage.

---

# 9. `CUSTOMER_NO_SHOW`

`CUSTOMER_NO_SHOW` means:

> **The Appointment remained an expected customer interaction, but sufficient authoritative evidence establishes that the customer did not participate sufficiently for that Appointment interaction to occur.**

It is not:

```text
Appointment cancellation
Payment failure
customer misconduct determination
automatic deposit forfeiture
automatic refund denial
```

Applicable no-show commercial consequences remain merchant-policy and capability-owned consequences under existing authority.

---

# 10. `MERCHANT_SIDE_NON_OCCURRENCE`

`MERCHANT_SIDE_NON_OCCURRENCE` means:

> **The Appointment remained an expected customer interaction, but sufficient authoritative evidence establishes that the scheduled interaction did not materially occur because the merchant side could not or did not provide the expected Appointment interaction.**

It may cover circumstances such as:

```text
assigned worker unavailable
merchant failed to attend
merchant operational failure
required merchant-side resource unavailable
```

where the Appointment was not validly cancelled beforehand.

It does not determine:

```text
legal liability
customer compensation
refund amount
worker fault
disciplinary consequence
```

---

# 11. Cancellation Is Not an Outcome

Appointment cancellation remains separately owned by existing composite MS-PROT-042 authority.

Canonical:

```text
Appointment cancelled before interaction
        ↓
CANCELLED commitment consequence
```

not:

```text
CUSTOMER_NO_SHOW
MERCHANT_SIDE_NON_OCCURRENCE
```

An Appointment validly cancelled before the expected interaction SHALL NOT later receive an ordinary occurrence outcome for that cancelled commitment.

If the cancellation itself was erroneous, the applicable correction/recovery authority must address that fact rather than falsifying an occurrence outcome.

---

# 12. Absence of Outcome Is Meaningful

The absence of an authoritative Appointment Occurrence Outcome means:

```text
NO AUTHORITATIVE OUTCOME RECORDED
```

It SHALL NOT mean:

```text
OCCURRED
CUSTOMER_NO_SHOW
MERCHANT_SIDE_NON_OCCURRENCE
PARTIAL_OCCURRENCE
```

No `UNKNOWN` persistence row is required merely to represent absence.

Consumers requiring an outcome SHALL fail closed or remain unresolved when one cannot be established.

---

# 13. Clock Passage Is Never Outcome Evidence By Itself

Rejected:

```text
scheduled end time < now
        ↓
Appointment = completed
```

and:

```text
scheduled start time passed
customer did not interact with Main Street
        ↓
CUSTOMER_NO_SHOW
```

Time may determine that an outcome is now worth observing.

Time alone cannot determine what happened.

---

# 14. Payment Is Not Outcome Evidence By Itself

Rejected:

```text
payment succeeded
        ↓
OCCURRED
```

and:

```text
refund issued
        ↓
Appointment did not occur
```

A merchant may legitimately:

```text
take payment before service
perform service before payment
refund after a service occurred
retain payment after a no-show where valid policy permits
```

Payment retains independent authority.

---

# 15. Workforce Evidence Is Not Appointment Outcome By Itself

The following do not independently prove the customer interaction occurred:

```text
worker clocked in
worker had a scheduled shift
worker was not on leave
worker remained at location
worker clocked out
```

Workforce evidence may participate only through a separately accepted exact outcome-evidence contract.

Appointment outcome does not establish worked time or compensation truth in return.

---

# 16. Provider Presence Is Not Appointment Outcome By Itself

Examples:

```text
video-call room opened
calendar meeting existed
customer clicked meeting link
provider says call connected
telephony connection established
```

do not universally prove the supported Appointment interaction occurred.

A provider-derived signal may become sufficient evidence only through an exact accepted owner-qualified Appointment Outcome Evidence Contract establishing that its meaning is adequate for the relevant Appointment semantics.

Provider convention does not define Appointment truth.

---

# 17. AI Cannot Establish Outcome

AI MAY assist an authorised merchant actor by:

```text
summarising evidence
highlighting appointments needing an outcome
suggesting a likely outcome for human review
explaining the difference between outcome choices
```

AI SHALL NOT:

```text
commit OCCURRED from conversational inference
infer no-show from silence
infer completion from calendar time
infer outcome from payment
infer outcome from customer sentiment
override contradictory evidence
```

A model confidence score never creates Appointment outcome authority.

---

# 18. Appointment Outcome Evidence

Every authoritative outcome SHALL have sufficient provenance establishing why Main Street was permitted to record that outcome.

Initial evidence-source classes are:

```text
AUTHORISED_HUMAN_ATTESTATION
REGISTERED_OWNER_QUALIFIED_EVIDENCE
```

These classify authority sources; they do not prescribe persistence enums.

---

# 19. Authorised Human Attestation

An appropriately authorised merchant-side actor MAY attest the Appointment outcome.

The actor must have current authority for the relevant merchant-scoped Appointment operation.

Examples might include:

```text
Controller
authorised receptionist
authorised service worker
authorised manager
```

depending on merchant role configuration.

Possessing access to the Appointment for viewing does not automatically grant outcome-mutation authority.

---

# 20. Customer Cannot Mutate Merchant Appointment Outcome

A customer may:

```text
send a message
raise a complaint
provide feedback
dispute what occurred
```

through applicable authorities.

Customer assertion alone SHALL NOT directly mutate the merchant-owned Appointment Occurrence Outcome.

A customer relationship to the Appointment grants neither:

```text
appointment.record-outcome
appointment.correct-outcome
```

authority.

Customer evidence may be retained/considered through separately governed processes without silently becoming Appointment truth.

---

# 21. Registered Owner-Qualified Evidence

Main Street MAY later establish Appointment outcome automatically when an accepted exact contract proves that another authoritative source supplies evidence sufficient for the exact outcome.

Conceptually:

```text
source-owned evidence
        +
exact Appointment affinity
        +
registered Appointment Outcome Evidence Contract
        +
deterministic validation
        ↓
Appointment outcome
```

There is no generic:

```text
"activity happened somewhere"
        ↓
Appointment OCCURRED
```

rule.

Initially, zero automated evidence contracts is a valid production configuration.

---

# 22. Canonical Operations

This amendment registers two semantic operation families:

```text
appointment.record-outcome
appointment.correct-outcome
```

They remain Appointment-owned operations.

---

# 23. `appointment.record-outcome`

`appointment.record-outcome` establishes the first authoritative occurrence outcome for one exact current Appointment commitment.

Minimum semantic input is equivalent to:

```text
trusted Merchant Scope
logical command identity
Appointment identity
expected Appointment revision/currentness
one registered Appointment outcome
outcome evidence/provenance
authorised initiating principal or
registered trusted evidence authority
```

The exact transport representation is downstream.

---

# 24. Exact Appointment Affinity

An outcome MUST bind to the exact Appointment commitment/revision to which the evidence applies.

This is essential because:

```text
Appointment A
09:00
        ↓ rescheduled
Appointment A
15:00
```

retains Appointment identity under accepted rescheduling semantics.

A stale actor or process observing the former 09:00 commitment SHALL NOT subsequently record:

```text
CUSTOMER_NO_SHOW
```

against the current 15:00 Appointment.

The operation MUST fail currentness validation where its expected Appointment revision no longer governs.

---

# 25. Outcome and Rescheduling

Once an authoritative Appointment Occurrence Outcome exists, ordinary rescheduling of that same completed/observed Appointment commitment SHALL NOT proceed.

A later customer interaction is a new Appointment commitment unless another future accepted authority explicitly establishes a different relationship.

Example:

```text
Appointment A
CUSTOMER_NO_SHOW

merchant agrees another date
        ↓
new Appointment B
```

rather than rewriting A's no-show history as if it never happened.

---

# 26. Outcome and Cancellation

Once an authoritative occurrence outcome exists, ordinary cancellation of that same Appointment SHALL NOT be used to rewrite what happened.

Example:

```text
Appointment occurred
customer later receives refund
```

shall remain:

```text
Appointment outcome = OCCURRED
Refund = separately governed fact
```

not:

```text
Appointment = CANCELLED
```

merely to explain the refund.

---

# 27. No Generic `COMPLETED` Commitment State

This amendment refines the earlier conceptual Appointment-completion language.

Main Street SHALL NOT require a universal Appointment commitment lifecycle such as:

```text
PENDING
CONFIRMED
IN_PROGRESS
COMPLETED
CANCELLED
```

Instead:

```text
Appointment commitment truth
+
Appointment amendments/rescheduling
+
Appointment cancellation where applicable
+
Appointment Occurrence Outcome where established
```

compose the authoritative history.

A UI MAY render business language such as `Completed` when a registered projection can deterministically derive that wording from the authoritative facts.

The UI word does not create a canonical `COMPLETED` lifecycle state.

---

# 28. Outcome Revision

Appointment outcome evidence is business-significant and may be entered incorrectly.

Therefore outcome correction SHALL preserve history.

Conceptually:

```text
AppointmentOutcomeRevision
{
    outcomeRevisionIdentity
    MerchantScope
    appointmentIdentity
    exact Appointment revision affinity
    outcome
    evidence/provenance
    establishedAt
    supersedesOutcomeRevision?
    authorising provenance
}
```

The representation is conceptual rather than a prescribed Java class.

---

# 29. One Current Outcome

For one exact Appointment:

```text
zero or one current Appointment Occurrence Outcome
```

may exist.

Historical superseded outcome revisions MAY coexist for auditability.

Main Street SHALL NOT permit two independently current contradictory outcomes.

---

# 30. `appointment.correct-outcome`

`appointment.correct-outcome` SHALL:

```text
reference the exact current outcome revision
supply the corrected registered outcome
supply correction authority/provenance
atomically create a new outcome revision
supersede the exact prior current revision
```

The prior fact remains historical evidence.

Rejected:

```text
UPDATE appointment
SET outcome = ...
```

where the former authoritative outcome becomes unrecoverable.

---

# 31. Correction Is Not History Erasure

Example:

```text
Receptionist accidentally records
CUSTOMER_NO_SHOW
        ↓
Manager establishes that customer attended
        ↓
appointment.correct-outcome
        ↓
Current outcome:
OCCURRED

Historical outcome:
CUSTOMER_NO_SHOW
superseded
```

Downstream consumers SHALL use the current outcome where current truth is required.

Audit/provenance retains the correction history.

---

# 32. Correction Does Not Automatically Reverse Foreign Effects

Suppose an incorrect `CUSTOMER_NO_SHOW` previously triggered an independently committed Payment or other consequence.

Correcting the Appointment outcome SHALL NOT silently rewrite that foreign capability.

Canonical:

```text
correct Appointment outcome
        ↓
new authoritative Appointment truth
        ↓
separately governed reconciliation /
remediation may become required
```

No cross-capability rollback illusion is introduced.

---

# 33. Idempotency

Both outcome operations SHALL comply with MS-PROT-059.

For the same logical command:

```text
same intent retry
    → stable prior result

same logical identity + materially changed intent
    → conflict
```

Transport retry SHALL NOT create duplicate outcome revisions.

---

# 34. Concurrency

Concurrent conflicting first-outcome attempts MUST preserve:

```text
at most one current outcome
```

Concurrent correction MUST require exact current-outcome affinity or an equivalent optimistic concurrency invariant.

Example:

```text
Actor A corrects NO_SHOW → OCCURRED
Actor B concurrently corrects NO_SHOW → PARTIAL_OCCURRENCE
```

Only a correction based on the still-current expected revision may commit.

The stale operation must fail rather than silently overwrite the newer truth.

---

# 35. Transaction Boundary

A successful first outcome operation SHALL atomically establish at minimum:

```text
Appointment outcome revision
+
current-outcome authority
+
handled logical command/result
+
required audit/outbox evidence
```

where those mechanisms are required by accepted implementation architecture.

A correction SHALL atomically:

```text
create correcting revision
+
replace current pointer/currentness authority
+
retain supersession relation
+
record handled command/result
+
required evidence
```

No partial current outcome is permitted.

---

# 36. Network Uncertainty

If outcome mutation commits but acknowledgement is lost:

```text
retry same logical command
        ↓
resolve existing committed result
```

not:

```text
create second outcome revision
```

If Main Street cannot establish whether an external evidence-producing operation occurred, applicable MS-PROT-069 reconciliation semantics govern.

Technical uncertainty cannot manufacture an outcome.

---

# 37. Outcome Does Not Rewrite Scheduled Interval

Recording `OCCURRED` does not rewrite `scheduled_interval` into actual occurrence time.

If actual-time evidence is needed by a future capability, that must be represented under the authority that owns that fact.

Appointment outcome is not Workforce timekeeping.

---

# 38. Outcome Does Not Release Time Early

If an Appointment outcome is recorded before the scheduled interval ends, Main Street SHALL NOT automatically reopen the remaining scheduled capacity merely because the interaction was recorded as having occurred.

Existing scheduled interval, buffers and capacity/allocation remain governed by Scheduling/Resource authority.

This prevents:

```text
service finished 10 minutes early
        ↓
Main Street unexpectedly offers
that 10 minutes to another customer
```

without accepted scheduling policy.

---

# 39. Policy Consequences

Appointment Occurrence Outcome MAY be consumed by the merchant's accepted Appointment policy semantics.

For example:

```text
CUSTOMER_NO_SHOW
        ↓
merchant's accepted no-show policy evaluation
```

may legitimately establish a separately governed consequence.

However:

```text
outcome
    ≠ consequence
```

The outcome itself SHALL NOT directly create refund, payment capture, deposit forfeiture, new Appointment, Notification or worker sanction without the applicable owning contract.

---

# 40. Notification Boundary

Recording an outcome does not inherently require a Notification.

An originating Appointment/policy contract MAY determine that communication is required.

If so:

```text
Appointment / policy authority
        determines why

MS-PROT-075
        delivers through a permitted path
```

Notification never determines the outcome.

---

# 41. Customer Messaging Boundary

A customer may message:

```text
"I was there."
"The barber never turned up."
"The appointment stopped halfway."
```

That Conversation is communication evidence.

It SHALL NOT directly mutate Appointment outcome.

A merchant-side authorised operation may respond to that evidence through the Appointment authority.

---

# 42. Analytics Boundary

MS-PROT-083 MAY consume current Appointment outcome evidence through accepted Analytical Input Bindings.

Examples might eventually support measures such as:

```text
appointment occurrence rate
customer no-show rate
merchant-side non-occurrence rate
```

But:

```text
OCCURRED
    ≠ satisfied customer

CUSTOMER_NO_SHOW
    ≠ bad customer

MERCHANT_SIDE_NON_OCCURRENCE
    ≠ proven worker fault
```

Analytics must retain epistemic discipline.

---

# 43. Review/Reputation Boundary

A future review/reputation authority MAY consume Appointment outcome.

However:

> **`OCCURRED` SHALL NOT become a universal “send a review request” flag.**

A future review-request contract must establish that the Appointment occurrence is sufficient evidence that the relevant customer experience has reached an appropriate solicitation point.

Example:

```text
Haircut Appointment OCCURRED
```

may be sufficient under an accepted review contract.

But:

```text
Mechanic vehicle-drop-off Appointment OCCURRED
```

does not prove:

```text
repair completed
```

and therefore may be insufficient.

This amendment enables Review/Reputation without transferring source truth to it.

---

# 44. Booking Boundary

This amendment does not define Booking utilisation, stay completion, resource return, check-in/check-out or other reservation outcome semantics.

Rejected:

```text
Appointment outcome vocabulary
        ↓
reuse universally for Booking
```

A hotel stay and a consultation appointment remain materially different commitments.

Exact Booking outcome/lifecycle depth remains outside this amendment.

---

# 45. Order Fulfilment Boundary

Order Fulfilment retains its accepted satisfaction anchors under MS-PROT-060.

Appointment `OCCURRED` is not Order Fulfilment, `COLLECTION_HANDOVER` or `OUTBOUND_MOVEMENT_HANDOVER`.

No universal customer-experience-completion aggregate is introduced.

---

# 46. Workforce Boundary

Appointment outcome does not establish:

```text
worker attendance
worked duration
break compliance
leave state
compensation amount
```

MS-PROT-081/MS-PROT-080 retain those authorities.

A worker may have clocked in while an Appointment results in `CUSTOMER_NO_SHOW`.

Both facts can simultaneously be true.

---

# 47. Audit

Outcome establishment and correction are business-significant operations and SHALL produce applicable MS-PROT-064 audit evidence.

Audit evidence SHOULD preserve:

```text
merchant scope
actor / evidence authority
Appointment identity
expected Appointment revision
outcome revision
operation identity
correction relationship where applicable
decision time
```

without turning Audit into Appointment authority.

---

# 48. Data Lifecycle

Appointment outcome/evidence retention and personal-data treatment remain governed by MS-PROT-053 and applicable capability/evidence requirements.

This amendment introduces no universal numeric retention period.

Free-text evidence is not required by the semantic contract.

Implementations SHOULD avoid collecting unnecessary narrative solely to support an outcome classification.

---

# 49. Merchant Experience

For a human-attested Appointment, the merchant-facing interaction SHOULD use ordinary business language.

Example:

```text
What happened with this appointment?

[It took place]
[Partly took place]
[Customer didn't attend]
[We couldn't provide it]
```

The merchant SHALL NOT be required to understand:

```text
AppointmentOutcomeRevision
evidence contract
semantic affinity
current-pointer concurrency
```

Those are Main Street responsibilities.

---

# 50. Exception-Driven Operation

Main Street SHOULD avoid making merchants manually update every Appointment where sufficient accepted evidence can safely establish the outcome.

Where such evidence does not exist:

```text
Appointment needs outcome
```

may be surfaced at an appropriate role-native operational point.

Main Street SHOULD prioritise unresolved, conflicting or consequential outcomes rather than creating an administrative status-maintenance screen.

---

# 51. Ordinary Staff Training

A worker already knows whether:

```text
the appointment took place
the customer did not arrive
the interaction stopped partway
the business could not provide it
```

The system therefore asks for business facts the worker understands.

It SHALL NOT require the worker to learn a Main Street lifecycle model.

---

# 52. Falsification — Clock Passes

Scenario:

```text
Appointment ends at 14:00
time now = 14:30
no outcome evidence exists
```

Expected:

```text
no authoritative outcome
```

not `OCCURRED`.

**PASS**

---

# 53. Falsification — Payment Before Appointment

Scenario:

```text
customer pays £40 before appointment
customer later does not attend
```

Expected:

```text
Payment truth remains successful
Appointment outcome may be CUSTOMER_NO_SHOW
```

No contradiction.

**PASS**

---

# 54. Falsification — Rescheduled Stale Screen

Scenario:

```text
Receptionist A opens 09:00 Appointment
another actor reschedules same Appointment to 15:00
Receptionist A later submits CUSTOMER_NO_SHOW against old revision
```

Expected:

```text
currentness check fails
no outcome recorded
```

**PASS**

---

# 55. Falsification — Concurrent Staff Outcomes

Scenario:

```text
Staff A → OCCURRED
Staff B → CUSTOMER_NO_SHOW
```

against the same current Appointment.

Expected:

```text
at most one current result commits
stale/conflicting attempt rejected
```

**PASS**

---

# 56. Falsification — Incorrect Human Entry

Scenario:

```text
CUSTOMER_NO_SHOW entered accidentally
```

Expected:

```text
authorised correction
        ↓
new immutable outcome revision
        ↓
old revision retained
```

**PASS**

---

# 57. Falsification — Cancelled Appointment

Scenario:

```text
Appointment validly cancelled before scheduled interaction
scheduled time later passes
```

Expected:

```text
no CUSTOMER_NO_SHOW
no MERCHANT_SIDE_NON_OCCURRENCE
```

**PASS**

---

# 58. Falsification — Provider Meeting Connected

Scenario:

```text
video provider says participant connected for 12 seconds
```

Expected without an accepted evidence contract:

```text
no OCCURRED outcome
```

**PASS**

---

# 59. Falsification — AI Inference

Scenario:

AI reads:

```text
"Thanks, that was really helpful."
```

from a Conversation.

Expected:

```text
AI may suggest OCCURRED for review
AI cannot commit it
```

**PASS**

---

# 60. Falsification — Mechanic Drop-Off

Scenario:

```text
09:00 mechanic drop-off Appointment
customer hands over vehicle
```

Expected:

```text
Appointment may become OCCURRED
vehicle repair complete = NOT ESTABLISHED
```

A downstream reputation system cannot universally treat this as completed repair.

**PASS**

---

# 61. Falsification — Haircut

Scenario:

```text
customer attends haircut
scheduled interaction takes place
```

Expected:

```text
OCCURRED
```

A future Review contract may consume that result if it explicitly establishes Appointment occurrence as sufficient for that review purpose.

**PASS**

---

# 62. Falsification — Partial Interaction

Scenario:

Consultation starts but ends materially early before the expected Appointment interaction is completed.

Expected:

```text
PARTIAL_OCCURRENCE
```

No automatic 50% refund, 50% payment or negative review is invented.

**PASS**

---

# 63. Falsification — Merchant-Side Failure

Scenario:

```text
customer arrives
assigned worker does not
Appointment was never cancelled
```

Expected:

```text
MERCHANT_SIDE_NON_OCCURRENCE
```

without automatically attributing disciplinary fault or calculating refund.

**PASS**

---

# 64. Falsification — Workforce Time

Scenario:

```text
staff clocked in 09:00–17:00
customer no-show at 11:00
```

Expected:

```text
Workforce evidence remains valid
Appointment = CUSTOMER_NO_SHOW
```

No contradiction.

**PASS**

---

# 65. Falsification — Review Gating

Scenario:

A future reputation workflow wants:

```text
OCCURRED → ask public review
CUSTOMER_NO_SHOW → do nothing
```

That may be legitimate because no customer experience occurred in the no-show case.

But a proposed workflow:

```text
customer sentiment positive
    → public review
customer sentiment negative
    → private feedback only
```

cannot derive authority from this amendment.

Appointment outcome says what happened.

It does not manipulate review sentiment.

**PASS**

---

# 66. Falsification — Low-Software-Capacity Merchant

Scenario:

A three-person salon needs to record an exceptional past Appointment.

Expected UI:

```text
What happened?

It took place
Customer didn't attend
Partly took place
We couldn't provide it
```

not a lifecycle-orchestration administration surface.

**PASS**

---

# 67. Cross-Authority Review

The proposal preserves:

```text
Appointment
    owns scheduled-interaction outcome

Scheduling
    owns schedulability

CustomerContext
    owns customer relationship

Payment
    owns payment/refund truth

Workforce
    owns worked-time/workforce truth

Notification
    owns delivery

Customer Messaging
    owns Conversation

Analytics
    owns derived measures/claims

Review/Reputation
    remains future downstream coordination

Audit
    owns audit evidence

AI
    remains non-authoritative
```

No shared mutable owner is introduced.

---

# 68. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The proposal adds internal complexity for evidence provenance, revision affinity, idempotency, concurrency, correction history and cross-capability isolation because those mechanisms are necessary to avoid false operational truth.

That complexity is hidden from ordinary merchants and staff.

The merchant is asked only about business reality:

```text
What happened with this appointment?
```

Main Street absorbs the software architecture required to make that answer reliable.

---

# 69. Business-to-Software Translation

The design supports:

```text
worker understands real event
        ↓
"It took place"
        ↓
Main Street translates
        ↓
Appointment Occurrence Outcome
        ↓
safe downstream coordination
```

rather than expecting the merchant to maintain system lifecycle states.

**VISION-CONFORMING**

---

# 70. Administrative Compression

The proposal permits future owner-qualified automatic evidence while retaining human attestation as the safe fallback.

Therefore Main Street can progressively move from:

```text
merchant records every outcome
```

toward:

```text
Main Street establishes what it safely can
        ↓
merchant sees only unresolved exceptions
```

without compromising deterministic authority.

---

# 71. Anti-ERP Review

Rejected expansion includes:

```text
generic job cards
work-order status
project workflow
customer-success lifecycle
case management
service-delivery ERP
arbitrary merchant state machines
generic workflow designer
```

The amendment remains constrained to one missing Appointment-owned business fact.

---

# 72. Hard Invariants

```text
INV-042-V19-001
Clock passage alone cannot establish an Appointment Occurrence Outcome.

INV-042-V19-002
Appointment owns Appointment Occurrence Outcome.

INV-042-V19-003
Appointment Occurrence Outcome is distinct from commitment, cancellation, payment, refund, workforce and broader service-fulfilment truth.

INV-042-V19-004
The initial outcome vocabulary is exactly OCCURRED, PARTIAL_OCCURRENCE, CUSTOMER_NO_SHOW and MERCHANT_SIDE_NON_OCCURRENCE.

INV-042-V19-005
Absence of outcome remains absence of authoritative outcome.

INV-042-V19-006
A valid pre-occurrence cancellation cannot later be reclassified as no-show/non-occurrence merely because time passes.

INV-042-V19-007
Every outcome is affined to the exact governing Appointment revision.

INV-042-V19-008
A stale pre-reschedule observation cannot establish outcome against the rescheduled Appointment revision.

INV-042-V19-009
At most one current occurrence outcome exists per Appointment.

INV-042-V19-010
Outcome correction preserves superseded historical evidence.

INV-042-V19-011
Outcome correction cannot silently reverse independently committed foreign-capability effects.

INV-042-V19-012
Customer relationship does not grant Appointment outcome mutation authority.

INV-042-V19-013
AI confidence cannot create Appointment outcome authority.

INV-042-V19-014
Payment, Workforce and provider-presence evidence do not independently prove Appointment occurrence.

INV-042-V19-015
Appointment OCCURRED does not universally prove broader service/work fulfilment.

INV-042-V19-016
Appointment outcome does not modify scheduled capacity or scheduled interval merely because the interaction ended early.

INV-042-V19-017
No universal Appointment COMPLETED lifecycle state is required; authoritative facts compose the lifecycle.

INV-042-V19-018
Review/Reputation or another downstream consumer cannot manufacture Appointment outcome truth.

INV-042-V19-019
A future automated outcome source requires an exact accepted owner-qualified evidence contract.

INV-042-V19-020
Booking outcome semantics are not created by this amendment.
```

---

# 73. Rejected Alternatives

Rejected:

```text
appointment end time => COMPLETED
payment success => COMPLETED
calendar meeting ended => COMPLETED
AI confidence => COMPLETED
customer reply => COMPLETED
universal Appointment/Booking fulfilment state machine
generic service-completion aggregate
Review system decides whether service happened
mutable outcome column with no correction history
two simultaneous current outcomes
rescheduling after recorded outcome as if original event never happened
outcome automatically releases remaining scheduled interval
Appointment OCCURRED means all merchant work is complete
```

---

# 74. Scope Deliberately Excluded

This amendment does not define:

```text
Booking/stay/resource-use outcomes
hotel check-in/check-out
repair/work-order completion
project completion
Order Fulfilment
quantified partial service fulfilment
customer satisfaction
complaint/dispute resolution
review/reputation semantics
review-request eligibility
review-provider integration
refund calculation
no-show charge amounts
worker disciplinary consequence
generic service-delivery lifecycle
specific provider evidence contracts
specific automated outcome integrations
UI implementation technology
```

Those remain with their existing owner or require normal feature admission when justified.

---

# 75. Grouped Retained MS-PROT-042 Semantic Work Packages

Manual approval of this amendment included the direction that remaining unresolved MS-PROT-042 decisions SHALL no longer remain as one loose historical list. They are grouped into small semantic work packages and SHALL be resolved group-by-group after v1.9 formalisation.

The grouping is navigation/prioritisation authority only. Each group still requires the normal DESIGN-RULES lifecycle and explicit manual approval before becoming substantive authority.

## 75.1 `MS-PROT-042-GRP-01 — Booking Outcome & Change Semantics`

Related retained decisions:

```text
broader Booking lifecycle / natural discharge / utilisation outcome
+
Booking modification rules specialised by booked subject/capability
```

Why grouped:

Both determine how an existing Booking evolves after confirmation without collapsing reservation commitment, fulfilment and capability-specific booked-subject truth.

Owner:

```text
MS-PROT-042 Booking
+
Resource / Allocation composition where applicable
```

## 75.2 `MS-PROT-042-GRP-02 — Appointment Proposal Expiry & Capacity Protection`

Related retained decisions:

```text
TimeProposal expiry semantics/defaults
+
explicit Hold applicability/policy for proposed Appointments
```

Why grouped:

Both govern the interval between merchant proposal and Appointment commitment and must preserve the distinction:

```text
TimeProposal ≠ Hold ≠ Appointment
```

Owner:

```text
MS-PROT-042 Appointment/Scheduling
+
MS-PROT-006 Hold authority
```

## 75.3 `MS-PROT-042-GRP-03 — Repeated & Shared Appointment Commitments`

Related retained decisions:

```text
recurring Appointment semantics
+
group / multi-customer Appointment semantics
```

Why grouped:

Both challenge the current one-customer/one-commitment shape and require careful identity, capacity, amendment, cancellation and CustomerContext affinity without introducing a generic recurrence/workflow engine.

Owner:

```text
MS-PROT-042 Appointment
+
CustomerContext
+
Resource / Capacity where applicable
```

## 75.4 `MS-PROT-042-GRP-04 — Appointment Assignment Continuity`

Related retained decisions:

```text
staff substitution for an existing Appointment
+
resource reassignment for an existing Appointment
```

Why grouped:

Both ask whether a committed Appointment may preserve customer commitment identity while the internal fulfiller/resource changes.

Owner:

```text
MS-PROT-042 Appointment
+
MS-PROT-081 Workforce where staff is involved
+
Resource / Allocation authority
```

## 75.5 `MS-PROT-042-GRP-05 — Merchant-Initiated Appointment Change & Customer Participation`

Related retained decisions:

```text
customer acceptance requirements for merchant-initiated rescheduling
+
Appointment arrival/check-in semantics
```

Why grouped:

Both concern customer participation evidence around an already-established Appointment and must distinguish customer acknowledgement/arrival from commitment truth, occurrence outcome and merchant authority.

Owner:

```text
MS-PROT-042 Appointment
+
CustomerContext / customer interaction authority where applicable
```

## 75.6 Cross-authority candidates not automatically owned by MS-PROT-042

The following historical v1.2 deferred items SHALL NOT be treated as automatic MS-PROT-042 amendments merely because they were once listed there:

```text
waiting-list semantics
+
overbooking policy
```

They are related capacity/coordination exceptions but require fresh feature admission and ownership review.

Likely composition:

```text
Waiting list
    → potential new coordination capability consuming
      Booking/Appointment availability and CustomerContext

Overbooking
    → Booking/Appointment + Resource/Capacity/Allocation policy
```

Neither is authorised by v1.9.

## 75.7 Historical items whose authority boundary is already resolved

The following historical v1.2 deferred families SHALL NOT be reopened as generic unresolved MS-PROT-042 design questions:

```text
Booking/Appointment payment-policy authority
late-cancellation policy authority
no-show commercial consequence authority
refund consequence ownership
```

Composite MS-PROT-042 v1.3 establishes merchant policy ownership and registered deterministic policy semantics; MS-PROT-055 retains Payment/Refund authority. Concrete future policy vocabulary may still be admitted when target scenarios require it.

## 75.8 Historical implementation/composition items

The following are not standalone generic MS-PROT-042 semantic work packages:

```text
specific Booking capability catalogue
motel/allocation detail model
specific resource-booking representations
```

They remain target-capability / Resource / Allocation composition work and SHALL be promoted only when dependency evidence makes a concrete model necessary.

---

# 76. Deferred-Scope Outcome

The Appointment occurrence/outcome boundary defined here requires no retained semantic DQ for its initial portfolio.

The previously broad unresolved question:

```text
exact Appointment lifecycle states
```

is resolved within this scope by rejecting a universal terminal-state machine and adopting:

```text
commitment facts
+
amendment/rescheduling facts
+
cancellation where applicable
+
separate Appointment Occurrence Outcome
```

Remaining MS-PROT-042 semantic work is now represented by the grouped packages in §75 rather than a loose historical list.

Exact Booking outcome/lifecycle depth remains unresolved specifically within `MS-PROT-042-GRP-01`.

Future broader work/service fulfilment is a new feature boundary rather than hidden unfinished Appointment work.

---

# 77. Implementation-Rules Impact

Acceptance of this proposal SHALL NOT activate implementation.

A future conforming implementation must preserve at minimum:

```text
exact Appointment revision affinity
trusted Merchant Scope
actor/evidence authority
idempotency
one-current-outcome invariant
immutable correction provenance
safe concurrency
audit evidence
no cross-capability mutation leakage
no clock/payment/AI completion inference
```

Exact API, schema, persistence and UI implementation remain downstream.

---

# 78. Dependency-Graph Consequence

Once accepted, the graph obtains a trustworthy source edge:

```text
Appointment
        ↓
Appointment Occurrence Outcome
```

which may later be consumed by merchant policy, Analytics / Business Health, Merchant Attention where justified, Customer Communication where justified, and Review / Reputation without any of those consumers inventing Appointment truth.

This removes the principal Appointment-side blocker discovered while evaluating the Review/Reputation node.

The grouped retained 042 work packages remain independently selectable dependency nodes and SHALL be solved after v1.9 formalisation before the programme treats MS-PROT-042's historical deferred portfolio as exhausted.

---

# 79. Recommendation and Acceptance Statement

**RECOMMENDATION: ACCEPT — ACCEPTED by explicit manual approval on 9 September 2026.**

This is the smallest authority that closes the missing Appointment operational-outcome boundary while preserving the existing Scheduling, Payment, Workforce, Customer, AI and provider ownership model.

It avoids both failure modes:

```text
too little:
    clock/calendar/downstream systems invent completion

too much:
    Main Street builds generic service-management ERP
```

The authority passed:

```text
Feature Admission
Fundamental Vision Conformance
ownership review
ambiguity review
cross-capability review
anti-ERP review
falsification
```

The remaining MS-PROT-042 unresolved scope is explicitly grouped in §75 for subsequent design passes.
