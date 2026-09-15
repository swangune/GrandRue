# MS-PROT-042 v1.10 — TimeProposal Expiry & Appointment Proposal Capacity Protection Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.10  
**Status:** **ACCEPTED after complete proposal, conformance review, falsification and manual approval on 9 September 2026**  
**Work package:** `MS-PROT-042-GRP-02`  
**Authority type:** Appointment/Scheduling semantic amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-042 through v1.9 within TimeProposal acceptance validity, expiry and proposal-specific capacity protection  
**Depends on:** composite MS-PROT-042 through v1.9; accepted Resource/Allocation authority; MS-PROT-025; MS-PROT-040; MS-PROT-054; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-065; composite MS-PROT-069; MS-PROT-072  
**Historical evidence only:** MS-PROT-006 Hold concepts incorporated by accepted MS-PROT-042; this amendment does not promote MS-PROT-006 wholesale  
**Resolves:** `MS-PROT-042-GRP-02` — TimeProposal expiry + Appointment proposal Hold applicability  
**Implementation activation:** NONE

---

# 1. Governing Decision

A `TimeProposal` SHALL have a bounded period during which the customer may authoritatively accept it.

A TimeProposal remains:

```text
proposed customer decision opportunity
```

and SHALL NOT itself become:

```text
Appointment
Allocation
capacity ownership
Hold
```

Capacity protection while the customer considers a TimeProposal is optional and explicit.

The initial model therefore supports:

```text
UNPROTECTED TimeProposal
        or
PROTECTED TimeProposal
```

with:

```text
UNPROTECTED
    → no capacity is reserved

PROTECTED
    → exact required capacity is temporarily protected
      by a Proposal Hold
```

Both still require current authoritative Appointment confirmation after customer acceptance.

---

# 2. Feature Admission

## Representation Test — PASS

Main Street must distinguish:

```text
merchant offered 14:00
customer can still respond

merchant offered 14:00
response period ended

merchant offered 14:00
but did not reserve capacity

merchant offered 14:00
and intentionally reserved capacity
```

These are materially different business facts.

## Coordination Test — PASS

Proposal validity coordinates:

```text
Appointment
Scheduling
Customer decision
Capacity
Allocation
Timers
```

without transferring their ownership.

## Administrative-Compression Test — PASS

Main Street determines expiry and releases proposal capacity automatically.

The merchant does not maintain:

```text
temporary slot locks
expiry jobs
capacity cleanup
stale proposal lists
```

manually.

## ERP-drift — PASS

No workflow builder, queue-management suite or generic reservation engine is introduced.

---

# 3. TimeProposal Remains Distinct

Canonical:

```text
TIME PROPOSAL
    merchant offers a candidate interval

APPOINTMENT
    agreed scheduled-service commitment

PROPOSAL HOLD
    temporary capacity protection

ALLOCATION
    authoritative capacity claim
    supporting a committed Appointment
```

Therefore:

```text
TimeProposal ≠ Appointment
TimeProposal ≠ Proposal Hold
Proposal Hold ≠ Appointment
Proposal Hold ≠ final Allocation
```

---

# 4. Exact SchedulingRequest Affinity

Every TimeProposal in this merchant-controlled scheduling flow SHALL be affined to:

```text
MerchantScope
+
SchedulingRequest
+
CustomerContext relationship
+
exact proposed interval
+
applicable Offering / scheduled-operation context
```

A TimeProposal SHALL NOT float as an unidentified calendar suggestion.

---

# 5. TimeProposal Is Immutable in Meaning

Once issued, a TimeProposal's material meaning SHALL NOT be silently edited.

Material proposal facts include at least:

```text
proposed interval
customer/SchedulingRequest affinity
acceptance deadline
protection mode
relevant configuration/release provenance
```

If the merchant wishes to offer a materially different time or deadline:

```text
new TimeProposal
```

is created.

The old proposal is retained historically.

---

# 6. Proposal Acceptance Deadline

Every TimeProposal SHALL have one exact **Proposal Acceptance Deadline**.

The deadline answers:

> Until what authoritative instant may this customer decision still be accepted for this exact proposal?

There SHALL be no timeless open TimeProposal.

---

# 7. Zero-Administration Default Deadline

Main Street SHALL NOT require merchants to configure a generic:

```text
24 hour
48 hour
72 hour
```

proposal-expiry setting merely to use Appointment scheduling.

Where no earlier deadline is established by supported merchant intent or applicable accepted policy:

> **The default Proposal Acceptance Deadline is the start of the proposed Appointment interval.**

Example:

```text
Proposed Appointment:
Tuesday 14:00–15:00

No earlier response deadline specified

        ↓

Proposal Acceptance Deadline:
Tuesday 14:00
```

This is a semantic boundary rather than an arbitrary platform-duration policy.

---

# 8. Earlier Merchant Deadline

A merchant MAY establish an earlier response deadline where the business genuinely requires one.

Example:

```text
"I can keep 14:00 open for you
until 17:00 today."
```

Main Street may translate that to:

```text
Proposal Acceptance Deadline
= today 17:00
```

provided the resulting deadline is deterministically valid.

The merchant SHALL NOT need to configure duration arithmetic or a scheduling engine.

---

# 9. Policy-Derived Earlier Deadline

A registered merchant Appointment policy MAY establish an earlier proposal-response rule.

Example:

```text
Customer must confirm merchant-proposed
appointments by 18:00 the previous day.
```

Main Street may deterministically derive the proposal deadline from that accepted policy.

AI interpretation remains subject to the existing merchant policy validation rules.

---

# 10. Effective Deadline Ceiling

The Proposal Acceptance Deadline MUST NOT be later than:

```text
proposedInterval.start
```

and MUST respect any exact applicable accepted owner-qualified cutoff that already makes later acceptance invalid.

Conceptually:

```text
effective deadline
    =
earliest applicable of:

explicit proposal response deadline
registered applicable response cutoff
proposed Appointment start
```

Main Street SHALL NOT invent an unstated cutoff.

---

# 11. Deadline Is Historical Proposal Truth

Once the TimeProposal is issued, its Proposal Acceptance Deadline is immutable.

A later merchant policy change SHALL NOT silently rewrite:

```text
yesterday's proposal deadline
```

The proposal retains the deadline and configuration provenance under which it was issued.

Current Appointment confirmation still performs its required revalidation.

---

# 12. Authoritative Time

Deadline evaluation SHALL use Main Street authoritative time.

Rejected:

```text
customer device clock
merchant browser clock
email timestamp supplied by client
AI interpretation of "probably before deadline"
```

A customer-controlled timestamp cannot manufacture timely acceptance.

---

# 13. Deadline Boundary

Acceptance eligibility is:

```text
authoritative current time < acceptanceDeadline
```

At:

```text
authoritative current time >= acceptanceDeadline
```

the TimeProposal is expired if no earlier terminal customer/merchant decision has been authoritatively established.

The deadline is therefore exclusive.

---

# 14. TimeProposal Disposition

The minimum current disposition vocabulary is:

```text
OPEN
ACCEPTED
DECLINED
WITHDRAWN
EXPIRED
```

`OPEN` means:

```text
no terminal decision exists
AND
current authoritative time < acceptance deadline
AND
the SchedulingRequest remains applicable
```

These dispositions describe TimeProposal decision truth.

They are not Appointment states.

---

# 15. `ACCEPTED`

`ACCEPTED` means:

> The customer's authoritative acceptance of this exact proposal was validly established while the proposal was OPEN.

Existing composite MS-PROT-042 authority remains:

```text
TimeProposal ACCEPTED
        ≠
Appointment confirmed
```

Acceptance triggers the applicable confirmation path.

---

# 16. Failed Confirmation After Acceptance

If:

```text
TimeProposal = ACCEPTED
```

but final authoritative Appointment confirmation fails:

```text
TimeProposal remains ACCEPTED
```

as historical customer-decision evidence.

Main Street SHALL NOT rewrite it to:

```text
OPEN
EXPIRED
DECLINED
```

merely because commitment failed.

The SchedulingRequest may remain unresolved and another proposal may subsequently be issued.

---

# 17. `DECLINED`

`DECLINED` means:

> The customer authoritatively declined that exact TimeProposal while it was OPEN.

Declining:

```text
does not cancel an Appointment
```

because no Appointment exists from that proposal merely by being proposed.

A new TimeProposal may later be issued under the same unresolved SchedulingRequest.

---

# 18. `WITHDRAWN`

`WITHDRAWN` means:

> An authorised merchant-side operation withdrew an OPEN TimeProposal before customer acceptance.

Withdrawal SHALL preserve historical evidence that the proposal existed.

It SHALL NOT delete it.

---

# 19. `EXPIRED`

`EXPIRED` means:

> No terminal acceptance/decline/withdrawal was established before the Proposal Acceptance Deadline.

Expiry is deterministically established by:

```text
proposal deadline
+
authoritative time
```

No merchant action is required.

---

# 20. Expiry Is Not a Timer Side Effect

A background worker does not make a proposal expired.

Canonical:

```text
deadline reached
        ↓
proposal is semantically expired
```

A timer may:

```text
wake processing
release/clean physical records
update projections
surface follow-up work
```

but timer execution is not the authority for expiry.

Therefore a delayed worker cannot extend proposal validity.

---

# 21. Expired Proposal Cannot Be Accepted

Once:

```text
current authoritative time >= deadline
```

a new acceptance attempt SHALL fail as proposal-expired.

The merchant/customer must use:

```text
new proposal
or
another valid scheduling path
```

The old proposal SHALL NOT be resurrected by changing its deadline.

---

# 22. Proposal Expiry Does Not Close SchedulingRequest Universally

A TimeProposal expiring means:

```text
that proposal is no longer acceptable
```

It does not universally mean:

```text
customer abandoned scheduling
SchedulingRequest rejected
SchedulingRequest resolved
merchant no longer wants appointment
```

The SchedulingRequest retains its own accepted lifecycle meaning.

A new proposal may be issued.

---

# 23. Capacity-Protection Modes

Every TimeProposal SHALL explicitly resolve to one of:

```text
UNPROTECTED
PROTECTED
```

No implicit capacity promise is permitted.

The protection mode is immutable for the proposal once externally issued.

---

# 24. Default Protection Mode

The initial default is:

```text
UNPROTECTED
```

unless accepted merchant policy or an authorised merchant action explicitly requires temporary capacity protection.

Therefore ordinary TimeProposal semantics remain consistent with existing authority:

```text
TimeProposal
    ≠
Hold
```

---

# 25. `UNPROTECTED`

An UNPROTECTED TimeProposal:

```text
does not consume capacity
does not reserve the interval
does not prevent another valid commitment
```

Customer acceptance still causes:

```text
current Scheduling revalidation
+
required capacity claim
+
Appointment confirmation
```

If capacity has been consumed meanwhile, confirmation may fail.

This is accepted business truth, not a concurrency defect.

---

# 26. Unprotected Proposal Must Not Be Presented as Reserved

Main Street SHALL NOT tell the customer:

```text
"This time is reserved for you"
```

where no Proposal Hold exists.

Business-facing wording may instead communicate:

```text
Proposed time
Confirm by <deadline>
Availability will be checked when you confirm
```

Exact presentation remains downstream UX scope.

The semantic prohibition against falsely implying reservation is normative.

---

# 27. `PROTECTED`

A PROTECTED TimeProposal means:

> Main Street has established an exact temporary capacity claim for the TimeProposal under the applicable capacity authority.

The claim exists only for the proposal's required capacity.

It does not create an Appointment.

---

# 28. Appointment Proposal Hold

The capacity claim associated with a PROTECTED TimeProposal is called an:

**Appointment Proposal Hold**

It is proposal-specific capacity protection.

Conceptually:

```text
AppointmentProposalHold
{
    MerchantScope
    TimeProposal identity
    SchedulingRequest affinity
    proposed interval
    exact protected capacity claim
    expiresAt
    provenance
}
```

This representation is conceptual.

It does not prescribe a Java class or database table.

---

# 29. This Amendment Does Not Promote Historical MS-PROT-006 Wholesale

The accepted authority for Appointment Proposal Hold established here is limited to:

```text
TimeProposal-specific
temporary capacity protection
```

This amendment does not make every historical MS-PROT-006 proposition current authority.

Generic future Hold use outside this accepted scope still requires applicable governing authority.

---

# 30. Proposal Hold Expiry

For a PROTECTED TimeProposal:

> **The Appointment Proposal Hold expires at the exact Proposal Acceptance Deadline.**

Therefore:

```text
Proposal Hold expiry
    =
Proposal Acceptance Deadline
```

for this specific use.

This is not a universal Hold duration.

No:

```text
10-minute
15-minute
30-minute
```

platform-wide Hold duration is introduced.

---

# 31. Hold Cannot Outlive Proposal

Hard invariant:

```text
Appointment Proposal Hold
    MUST NOT remain capacity-blocking
    after the TimeProposal can no longer be accepted
```

Thus:

```text
now >= acceptanceDeadline
        ↓
proposal no longer acceptable
+
proposal Hold no longer constrains new capacity decisions
```

even if asynchronous cleanup has not yet physically removed the Hold record.

---

# 32. Expired Holds Must Be Ignored Authoritatively

Capacity/Scheduling evaluation SHALL treat an Appointment Proposal Hold as active only while:

```text
authoritative current time < hold.expiresAt
```

A delayed cleanup worker SHALL NOT cause expired capacity to remain blocked.

This prevents infrastructure lag from becoming false business scarcity.

---

# 33. Protected Proposal Creation Must Be Atomic

Where the merchant requests a PROTECTED TimeProposal:

```text
TimeProposal
+
Appointment Proposal Hold
```

must be established within the applicable consistency/orchestration invariant before the proposal is externally represented as protected.

Rejected:

```text
send proposal
        ↓
try to obtain Hold afterward
```

If required capacity protection cannot be established:

```text
PROTECTED proposal creation fails
```

or the merchant is explicitly offered an unprotected alternative.

There SHALL be no silent downgrade.

---

# 34. Protection Is Exact

The Proposal Hold SHALL protect only the capacity required by the proposed Appointment under the exact accepted Scheduling/Resource semantics.

It SHALL NOT create:

```text
merchant-wide lock
staff-wide lock beyond required interval
whole-day lock
all equivalent resources lock
unrelated inventory reservation
```

unless those capacities are genuinely part of the exact Appointment requirement.

---

# 35. Proposal Hold Does Not Guarantee Every Appointment Condition

A PROTECTED proposal protects its exact capacity claim.

It does not guarantee that every other condition will remain satisfiable.

For example, later authoritative evidence may establish:

```text
merchant-side emergency closure
resource becomes unusable
regulatory restriction
customer eligibility failure
material policy change requiring renewed terms
```

Final `appointment.confirm` therefore still performs current authoritative revalidation.

Hard rule:

```text
PROTECTED
    ≠
Appointment guaranteed
```

---

# 36. Customer Acceptance of Protected Proposal

Where the customer validly accepts a PROTECTED TimeProposal:

```text
TimeProposal ACCEPTED
        ↓
current Scheduling/operation revalidation
        ↓
final Appointment commitment attempt
```

The existing Hold SHALL protect its capacity through the authoritative commitment attempt subject to its deadline.

---

# 37. No Release-and-Reacquire Gap

Where the protected capacity is required by the Appointment:

```text
Proposal Hold
        ↓
Appointment Allocation
```

must transition under an atomic consistency invariant.

Rejected:

```text
release Proposal Hold
        ↓
capacity temporarily free
        ↓
try to allocate Appointment
```

because another concurrent commitment could consume the interval in that gap.

A conforming implementation may:

```text
convert
replace
release-and-allocate atomically
```

provided no externally observable unprotected window exists.

---

# 38. Acceptance Still Does Not Override Other Constraints

Even while its capacity remains protected:

```text
TimeProposal ACCEPTED
```

does not bypass:

```text
current Actor Authorisation
applicable policy
customer authority
current non-held Scheduling constraints
current protection/security constraints
required Payment conditions
other invariant-required evidence
```

Acceptance is customer-decision evidence.

It is not universal execution authority.

---

# 39. Protected Acceptance Near Deadline

Acceptance does not create an indefinite extension of Proposal Hold.

If the customer's acceptance is authoritatively established before the deadline, Main Street may proceed with the immediate authoritative confirmation/reconciliation path.

But the proposal-specific Hold SHALL NOT become an open-ended payment or workflow reservation.

Any longer-lived capacity protection required by a separately governed payment or pre-commitment process requires its own accepted authority.

---

# 40. Definitive Confirmation Failure

If customer acceptance is valid but Appointment confirmation definitively fails:

```text
TimeProposal remains ACCEPTED
Appointment does not exist
```

and any remaining Proposal Hold SHALL be released once it is safe to establish that no successful Appointment commitment consumed it.

No merchant action is required to free the capacity.

---

# 41. Execution Uncertainty

If Main Street cannot determine whether the Appointment commitment succeeded:

```text
do not blindly release
do not blindly allocate again
do not offer capacity to another customer
```

until the uncertainty is reconciled under MS-PROT-069.

The system SHALL preserve the capacity invariant while the commitment outcome is genuinely uncertain.

---

# 42. Customer Decline Releases Protection

For a PROTECTED TimeProposal:

```text
DECLINED
        ↓
Proposal Hold released automatically
```

The merchant does not manually reopen the interval.

Scheduling derives resulting availability from current remaining constraints.

---

# 43. Merchant Withdrawal Releases Protection

For a PROTECTED TimeProposal:

```text
WITHDRAWN
        ↓
Proposal Hold released automatically
```

Withdrawal and release SHALL preserve the required consistency invariant.

Historical proposal evidence remains.

---

# 44. Expiry Releases Protection

For a PROTECTED TimeProposal:

```text
deadline reached
        ↓
EXPIRED
        +
Hold no longer capacity-blocking
```

No merchant action such as:

```text
release slot
make available
clear hold
```

is required.

---

# 45. Availability After Release Is Recalculated

Hold release SHALL NOT set:

```text
available = true
```

blindly.

Instead:

```text
Proposal Hold removed/expired
        ↓
Scheduling evaluates all
remaining authoritative constraints
        ↓
result may be
SCHEDULABLE
NOT_SCHEDULABLE
UNRESOLVED
```

This preserves composite MS-PROT-042 v1.6.

---

# 46. Protected-Proposal Cardinality

The initial portfolio supports:

> **At most one current PROTECTED TimeProposal per SchedulingRequest.**

This prevents one unresolved customer request from silently consuming several Appointment intervals merely because multiple alternatives were proposed.

Before another protected proposal may be established for the same SchedulingRequest:

```text
prior protected proposal
    must be terminal
    or be withdrawn/released
```

within a safe consistency boundary.

---

# 47. Unprotected Alternatives Are Not Automatically Prohibited

This amendment does not require a universal prohibition on multiple UNPROTECTED proposal alternatives.

However:

```text
one proposal accepted
+
Appointment successfully committed
```

makes competing proposal alternatives for that resolved SchedulingRequest non-committable according to the SchedulingRequest/Appointment authority.

No other proposal may create a duplicate commitment merely because it remains historically visible.

---

# 48. Re-Proposing Same Interval

After:

```text
EXPIRED
DECLINED
WITHDRAWN
```

the merchant may later offer the same clock interval again only through:

```text
new TimeProposal identity
+
new current Scheduling evaluation
+
new deadline
+
new protection decision
```

The old proposal is not reopened.

---

# 49. Idempotency

Proposal operations SHALL comply with MS-PROT-059.

For the same logical operation:

```text
same acceptance retry
    → same accepted result

same decline retry
    → same declined result

same withdrawal retry
    → same withdrawn result
```

Reusing the same command identity with materially different intent SHALL fail.

---

# 50. Concurrent Acceptance and Withdrawal

If customer acceptance and merchant withdrawal race:

```text
one current TimeProposal disposition
```

may win according to the authoritative transaction/currentness invariant.

Rejected:

```text
ACCEPTED
and
WITHDRAWN
both current
```

The losing stale operation receives a deterministic conflict/outcome.

---

# 51. Concurrent Acceptance and Expiry

Customer-side transmission time is insufficient.

The acceptance operation SHALL re-evaluate deadline validity using authoritative Main Street time at its governing decision boundary.

Therefore:

```text
client clicked before deadline
but no valid acceptance was authoritatively established before deadline
```

does not manufacture acceptance.

Network retry remains governed by the logical command/idempotency contract.

---

# 52. Merchant Policy Ownership

Main Street does not decide that merchants must reserve every proposed time.

A merchant may operate:

```text
UNPROTECTED proposal policy
```

or, where supported and approved:

```text
PROTECT proposed time while customer decides
```

Main Street owns the deterministic semantic representation and execution.

Merchant business intent remains merchant-owned under MS-PROT-042 v1.3.

---

# 53. AI Boundary

AI MAY:

```text
interpret:
"hold the time until tomorrow at noon"

suggest a response deadline

explain that an unprotected proposal
does not reserve the slot

propose merchant policy configuration
```

subject to existing human validation requirements.

AI SHALL NOT:

```text
invent an acceptance deadline

silently activate protection

declare capacity protected without Hold evidence

extend an expired proposal

override a deadline

override current Scheduling
```

---

# 54. Merchant Experience

Normal merchant interaction should be equivalent to:

```text
Offer customer:
Tuesday 14:00

Confirm by:
Tuesday 14:00
[change if needed]

Reserve this time while they decide?
No
```

or, where merchant policy already establishes the usual choice:

```text
Offer Tuesday 14:00
[Send]
```

with Main Street deriving the accepted configuration.

Technical Hold objects are not merchant administration.

---

# 55. Customer Experience — Unprotected

The customer may see business-facing meaning equivalent to:

```text
Bella Salon suggested:

Tuesday at 2:00 PM

Confirm before 2:00 PM Tuesday.

This time will be checked when you confirm.
```

Exact wording remains presentation scope.

Main Street SHALL NOT imply reserved capacity.

---

# 56. Customer Experience — Protected

Where exact Proposal Hold authority exists, the customer may be told meaning equivalent to:

```text
Tuesday at 2:00 PM
is being held for you until
Monday at 5:00 PM.
```

That claim may be made only while the Proposal Hold is current.

---

# 57. Exception-Driven Administration

The merchant SHALL NOT manage a list of expired Holds.

Main Street automatically handles:

```text
deadline evaluation
capacity expiry
release
currentness
timer cleanup
```

The merchant should be surfaced only where a genuine decision or exceptional conflict remains.

---

# 58. Data and Audit

Main Street SHALL retain sufficient owner-qualified evidence for material proposal decisions, including where applicable:

```text
TimeProposal identity
SchedulingRequest
customer relationship
proposed interval
acceptance deadline
protection mode
terminal disposition
decision authority
decision time
Hold affinity
configuration/release provenance
```

according to applicable lifecycle/data-protection authority.

Free-text administrative notes are not required.

---

# 59. Falsification — Unprotected Race

Scenario:

```text
Customer A gets unprotected 14:00 proposal
Customer B later successfully commits 14:00
Customer A then accepts before deadline
```

Expected:

```text
A's TimeProposal = ACCEPTED
final Appointment confirmation
revalidates current Scheduling
        ↓
fails because capacity is gone
```

No double allocation.

**PASS**

---

# 60. Falsification — Protected Race

Scenario:

```text
Customer A receives protected 14:00 proposal
Hold is active
Customer B attempts 14:00 commitment
```

Expected:

```text
A's exact held capacity
cannot be concurrently consumed by B
```

subject to the registered capacity model.

**PASS**

---

# 61. Falsification — Delayed Cleanup

Scenario:

```text
Proposal deadline = 12:00
cleanup worker runs at 12:07
another customer checks at 12:01
```

Expected:

```text
expired Hold does not constrain
12:01 availability
```

because expiry authority is time-based, not worker-based.

**PASS**

---

# 62. Falsification — Exact Deadline

Scenario:

```text
deadline = 12:00:00
acceptance decision evaluated at 12:00:00
```

Expected:

```text
EXPIRED
acceptance rejected
```

**PASS**

---

# 63. Falsification — Wrong Customer Device Clock

Scenario:

Customer device says 11:58 but Main Street authoritative time is 12:02.

Expected:

```text
proposal expired
```

**PASS**

---

# 64. Falsification — Customer Accepts, Confirmation Fails

Scenario:

```text
protected proposal accepted at 11:50
current non-capacity policy prevents confirmation
```

Expected:

```text
TimeProposal remains ACCEPTED
no Appointment
Hold safely released after definitive failure
SchedulingRequest may continue
```

**PASS**

---

# 65. Falsification — Merchant Withdraws

Scenario:

```text
protected proposal OPEN
merchant withdraws
```

Expected:

```text
WITHDRAWN
+
capacity released automatically
```

**PASS**

---

# 66. Falsification — Two Protected Alternatives

Scenario:

Merchant attempts to hold both:

```text
14:00
16:00
```

for one unresolved SchedulingRequest.

Expected initial portfolio:

```text
second protected proposal requires
prior protected proposal to become terminal/released
```

No silent double reservation.

**PASS**

---

# 67. Falsification — Policy Changes After Proposal

Scenario:

Proposal issued with:

```text
deadline Tuesday 14:00
```

Merchant later changes general proposal policy.

Expected:

```text
existing proposal deadline remains Tuesday 14:00
```

Current Appointment confirmation still performs required current revalidation.

**PASS**

---

# 68. Falsification — Payment Takes Too Long

Scenario:

Protected proposal accepted shortly before deadline but a separate payment prerequisite cannot complete before protection ends.

Expected:

```text
proposal-specific Hold does not silently
turn into indefinite payment Hold
```

If capacity protection beyond the proposal contract is required, it needs separately accepted authority.

**PASS**

---

# 69. Falsification — Low-Software-Capacity Merchant

Merchant says:

```text
"Offer her Tuesday at two.
Keep it for her until tomorrow lunchtime."
```

Expected Main Street translation:

```text
TimeProposal
Tuesday 14:00
+
deadline tomorrow 12:00
+
PROTECTED
+
Proposal Hold until tomorrow 12:00
```

The merchant does not configure:

```text
TTL
lock duration
scheduler
capacity lease
expiration worker
```

**PASS**

---

# 70. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The intrinsic complexity is:

```text
time boundary
concurrency
temporary capacity protection
expiry
release
retry
uncertainty
```

Main Street absorbs it.

Merchant-facing meaning remains:

```text
Offer this time
+
optionally keep it for them
+
when should they respond?
```

The proposal reduces software administration rather than adding it.

---

# 71. Anti-ERP Review

The amendment does not introduce:

```text
generic workflow engine
reservation administration module
queue manager
custom timer builder
capacity-rule DSL
enterprise scheduling suite
```

Only the minimum semantics needed for merchant-proposed Appointment times are established.

**PASS**

---

# 72. Hard Invariants

```text
INV-042-V110-001
TimeProposal is not Appointment.

INV-042-V110-002
TimeProposal is not capacity ownership.

INV-042-V110-003
Every TimeProposal has one exact acceptance deadline.

INV-042-V110-004
Absent an earlier accepted deadline, the default deadline is proposedInterval.start.

INV-042-V110-005
No universal numeric proposal-expiry duration is introduced.

INV-042-V110-006
Acceptance at or after the deadline is invalid.

INV-042-V110-007
Client/device time cannot establish timely acceptance.

INV-042-V110-008
Accepted TimeProposal remains accepted even if Appointment confirmation subsequently fails.

INV-042-V110-009
Expiry of a TimeProposal does not universally close its SchedulingRequest.

INV-042-V110-010
UNPROTECTED is the initial default protection mode.

INV-042-V110-011
An UNPROTECTED proposal must not be represented as reserved.

INV-042-V110-012
A PROTECTED proposal requires an exact Appointment Proposal Hold before protection is externally claimed.

INV-042-V110-013
No silent downgrade from PROTECTED to UNPROTECTED is permitted.

INV-042-V110-014
Proposal Hold expiry equals the exact proposal acceptance deadline.

INV-042-V110-015
Expired Proposal Holds cannot continue constraining capacity because cleanup is delayed.

INV-042-V110-016
Proposal Hold → Appointment Allocation transition has no release/reacquire race window.

INV-042-V110-017
Proposal protection does not bypass final Appointment revalidation.

INV-042-V110-018
Decline, withdrawal and expiry automatically end proposal-specific capacity protection.

INV-042-V110-019
At most one current protected TimeProposal exists per SchedulingRequest in the initial portfolio.

INV-042-V110-020
Proposal-specific Hold cannot silently become an indefinite Payment/workflow Hold.

INV-042-V110-021
AI cannot invent, extend or activate proposal/deadline/protection authority.

INV-042-V110-022
This amendment does not promote historical MS-PROT-006 wholesale.
```

---

# 73. Rejected Alternatives

Rejected:

```text
all TimeProposals reserve capacity

no TimeProposal ever reserves capacity

universal 10-minute proposal expiry

universal 24-hour proposal expiry

proposal never expires

customer device timestamp determines acceptance

expired proposal can be reopened

Hold cleanup worker determines semantic expiry

protected proposal sent before capacity Hold commits

silent protected → unprotected fallback

release Hold then separately attempt Allocation

Hold implies Appointment guarantee

acceptance implies Appointment confirmation

multiple simultaneous protected proposal slots
for one SchedulingRequest in initial scope

proposal Hold silently persists through
arbitrary external payment workflow
```

---

# 74. Resolved Work-Package Outcome

`MS-PROT-042-GRP-02` is fully resolved by this amendment.

Resolved:

```text
TimeProposal expiry semantics/default
+
Appointment proposal capacity-protection applicability
```

The result is:

```text
every proposal has deterministic deadline

default deadline = proposed interval start

earlier merchant/policy deadline allowed

UNPROTECTED default

PROTECTED explicit

Proposal Hold expiry = proposal deadline

automatic release

no cleanup lag authority

no release/reacquire gap
```

No retained semantic DQ remains for the initial GRP-02 portfolio.

---

# 75. Implementation-Rules Impact

Acceptance SHALL NOT activate implementation.

A future implementation must preserve:

```text
authoritative time
deadline currentness
idempotency
terminal disposition consistency
capacity concurrency
expired-Hold exclusion
atomic protected proposal creation
atomic Hold/Allocation transition
uncertainty reconciliation
tenant/customer affinity
auditability
```

Exact:

```text
table structure
timer technology
database lock strategy
API transport
UI component
background cleanup schedule
```

remain implementation concerns.

---

# 76. Next Group Consequence

With acceptance and formalisation:

```text
MS-PROT-042-GRP-02
    → RESOLVED
```

and the grouped execution overlay advances to:

```text
MS-PROT-042-GRP-05
Merchant-Initiated Appointment Change
+
Customer Participation
```

No return to the global graph occurs yet because the approved grouped-cleanup sequence remains active.

---

# 77. Recommendation

**RECOMMENDATION: ACCEPT**

The authority resolves proposal expiry and capacity protection without:

```text
arbitrary platform timeouts
mandatory Holds
merchant scheduling administration
capacity race windows
or a generic workflow engine
```

It preserves the accepted distinction:

```text
proposal
≠
capacity protection
≠
Appointment commitment
```

while making each relationship executable and deterministic.

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

Manual approval was given on 9 September 2026.
