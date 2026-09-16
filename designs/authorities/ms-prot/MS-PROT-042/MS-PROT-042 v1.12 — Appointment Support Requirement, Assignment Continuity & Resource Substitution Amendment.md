# MS-PROT-042 v1.12 — Appointment Support Requirement, Assignment Continuity & Resource Substitution Amendment

**Document ID:** MS-PROT-042  
**Version:** 1.12  
**Status:** **ACCEPTED by manual approval on 9 September 2026**  
**Approved:** Manual approval of the exact complete MS-PROT-042 v1.12 proposal presented in ChatGPT on 9 September 2026  
**Work package:** `MS-PROT-042-GRP-04`  
**Authority type:** Appointment / Scheduling / Resource interoperability semantic amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-042 through v1.11 within Appointment supporting-resource requirements, assignment continuity, staff substitution and Resource reassignment  
**Depends on:** composite MS-PROT-020; MS-PROT-025; composite MS-PROT-042 through v1.11; composite MS-PROT-043; MS-PROT-054; MS-PROT-057; MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-064; composite MS-PROT-069; MS-PROT-072; composite MS-PROT-075; composite MS-PROT-081  
**Resolves:** `MS-PROT-042-GRP-04` — staff substitution + Resource reassignment  
**Implementation activation:** NONE

---

# 1. Governing Decision

Main Street SHALL distinguish:

```text
what support an Appointment requires

        ≠

which concrete Resource currently
satisfies that requirement
```

Canonical ownership:

```text
APPOINTMENT
    owns the customer-commitment meaning
    of required Appointment support

SCHEDULING
    determines whether candidate/current
    support can validly satisfy the
    Appointment at its scheduled interval

RESOURCE / ALLOCATION
    owns concrete Resource identity,
    capacity and current Allocation truth

WORKFORCE
    owns worker scheduling,
    leave and workforce availability truth
```

A change of concrete supporting Resource SHALL NOT automatically mean that the Appointment itself has been rescheduled, cancelled or replaced.

---

# 2. Core Distinction

Main Street SHALL model:

```text
Appointment Support Requirement

        separately from

Current Supporting Resource
```

Example:

```text
Haircut Appointment
14:00–15:00

Support Requirement:
    one eligible stylist

Current supporting Resource:
    Stylist Sarah
```

If Sarah becomes unavailable and Bob validly replaces her:

```text
Appointment
    remains the same customer commitment

Support Requirement
    remains:
    one eligible stylist

Current Allocation
    changes:
    Sarah → Bob
```

This is assignment continuity, not Appointment rescheduling.

---

# 3. Feature Admission

## Representation Test — PASS

Main Street must distinguish:

```text
customer requires any eligible stylist

customer specifically requires Sarah

Sarah is currently assigned

Sarah becomes unavailable

Bob can validly substitute

Bob cannot satisfy the promised requirement
```

Those are materially different business facts.

## Coordination Test — PASS

The feature coordinates:

```text
Appointment
Scheduling
Resource
Allocation
Workforce
Customer decision
Notification
```

without transferring ownership among them.

## Administrative-Compression Test — PASS

A merchant should see:

```text
Sarah is unavailable.
Bob can cover this appointment.

[Use Bob]
```

rather than manually:

```text
remove worker allocation
recalculate capacity
edit appointment
update calendar
check leave
reassign resource
notify customer
```

## ERP-drift — PASS

The feature does not introduce:

```text
field-service management
dispatch optimisation
generic task assignment
work-order management
resource-planning ERP
skills-management suite
generic workforce scheduler
```

---

# 4. Appointment Support Requirement

An **Appointment Support Requirement** is:

> The Appointment-owned statement of what supporting Resource capability or identity must be available for the agreed Appointment interaction.

Examples include:

```text
one eligible stylist

one eligible consultation room

one eligible treatment chair

exact practitioner Sarah

exact specialist machine R7
```

It describes what the Appointment requires.

It does not identify the current Allocation unless exact identity itself is part of the requirement.

---

# 5. Support Requirement Is Contextual

Not every Appointment requires an explicit support requirement.

Example:

```text
sole-trader online consultation
```

may require no separate capacity-bearing Resource beyond the Appointment semantics already established.

Main Street SHALL NOT manufacture support requirements merely for architectural uniformity.

---

# 6. Initial Requirement Selection Modes

The initial closed selection modes are:

```text
ANY_ELIGIBLE_RESOURCE

EXACT_RESOURCE
```

No arbitrary merchant-defined assignment semantics or free-form executable matching rules are introduced.

---

# 7. `ANY_ELIGIBLE_RESOURCE`

`ANY_ELIGIBLE_RESOURCE` means:

> Any Resource that currently satisfies the registered Scheduling/Resource requirements for the Appointment may support the Appointment.

Example:

```text
Appointment:
haircut 14:00

Requirement:
ANY_ELIGIBLE_RESOURCE
for registered stylist requirement
```

Possible valid supporting Resources:

```text
Stylist Sarah
Stylist Bob
Stylist Aisha
```

if each independently satisfies the authoritative requirement.

---

# 8. `EXACT_RESOURCE`

`EXACT_RESOURCE` means:

> The exact Resource identity is itself part of the Appointment's required support meaning.

Example:

```text
Customer selected:
Sarah

Appointment requirement:
EXACT_RESOURCE
Resource = Stylist Sarah
```

Another stylist cannot satisfy that same requirement merely because they possess similar characteristics.

Changing Sarah to Bob therefore requires a change to the Appointment Support Requirement, not merely operational reassignment.

---

# 9. Exact Resource Is Not Inferred From Current Assignment

The fact that:

```text
Sarah is currently allocated
```

does not imply:

```text
customer was promised Sarah
```

Therefore:

```text
current assignment
    ≠
EXACT_RESOURCE requirement
```

This distinction prevents internal operating decisions from accidentally becoming customer contract terms.

---

# 10. Customer-Facing Representation Must Preserve the Distinction

If the requirement is:

```text
ANY_ELIGIBLE_RESOURCE
```

Main Street SHALL NOT present the current assignment in customer-facing language that falsely creates an exact-resource promise.

Rejected:

```text
Your appointment is guaranteed
with Sarah
```

where Sarah is merely the current operational assignment.

Permitted presentation may distinguish:

```text
Currently assigned: Sarah
```

where exposing the assignment is useful and the presentation makes its non-binding status clear.

Exact UX wording remains downstream.

---

# 11. Appointment Owns Requirement; Resource Does Not

A Resource cannot determine:

```text
what the customer was promised
```

merely because it is currently allocated.

Likewise Allocation cannot transform:

```text
ANY_ELIGIBLE_RESOURCE
```

into:

```text
EXACT_RESOURCE
```

Appointment remains the requirement authority.

---

# 12. Resource / Allocation Owns Concrete Assignment

Which concrete Resource currently satisfies an Appointment Support Requirement SHALL remain Resource/Allocation-owned operational truth.

Conceptually:

```text
Appointment Support Requirement
        ↓ requires

Resource / Allocation authority
        ↓ establishes

current supporting Resource
```

Appointment MAY coordinate the requirement.

It SHALL NOT steal:

```text
Resource state
capacity
Allocation ownership
```

from their accepted authorities.

---

# 13. No Duplicate Staff Semantic

Main Street SHALL NOT create an Appointment-specific:

```text
AppointmentStaff
```

identity separate from accepted Resource/Workforce semantics merely to support substitution.

Where a worker participates as schedulable support:

```text
Workforce person/arrangement
        ↔
schedulable Resource
        ↔
Appointment support
```

shall use accepted explicit relationships.

---

# 14. Worker-Backed Resource Requires Exact Workforce Arrangement Affinity

For a worker-backed Resource, current Workforce availability may participate only through the accepted:

```text
Workforce Scheduling Arrangement
        ↔
schedulable Resource
```

link.

The following are insufficient:

```text
same Identity

same name

same Merchant Membership

same Compensation Relationship

same email

same staff number
```

This preserves composite MS-PROT-042 v1.8.

---

# 15. Workforce Retains Workforce Truth

Appointment SHALL NOT own or mutate:

```text
worker shift
leave
break
worked time
Workforce Scheduling Arrangement
compensation
```

Changing an Appointment's supporting worker does not alter Workforce truth.

Likewise:

```text
Approved Leave
```

may establish that a worker-backed Resource cannot satisfy the Appointment, but it does not itself reassign or cancel the Appointment.

---

# 16. Requirement Revision

Appointment Support Requirement is business-significant commitment evidence and SHALL be independently revisionable.

Conceptually:

```text
AppointmentSupportRequirementRevision
{
    requirementIdentity

    MerchantScope

    appointmentIdentity

    registered support-requirement identity

    selectionMode

    exactResourceIdentity?    

    requirement provenance

    customer/policy provenance where required

    supersedesRequirementRevision?
}
```

This is conceptual rather than a prescribed persistence class.

---

# 17. Support Requirement Is Independently Revisioned From Scheduled Interval

An ordinary Appointment reschedule SHALL NOT erase or silently recreate its support requirement.

Example:

```text
Appointment A
10:00
EXACT_RESOURCE Sarah

        ↓ reschedule

Appointment A
15:00
```

still requires:

```text
EXACT_RESOURCE Sarah
```

unless an independently authorised support-requirement change occurs.

The new interval must, however, revalidate whether Sarah can satisfy that requirement at 15:00.

---

# 18. Why Independent Revision Matters

Rejected:

```text
Appointment time revision
        automatically resets
staff/resource promise
```

because:

```text
time commitment
```

and:

```text
support requirement
```

are materially distinct.

Main Street SHALL preserve both affinities explicitly.

---

# 19. Appointment Support Evaluation

Scheduling SHALL provide an **Appointment Support Evaluation** for each applicable current support requirement.

Initial result classes:

```text
SATISFIED

UNSATISFIED

UNRESOLVED
```

This is an evaluation result, not a lifecycle state.

---

# 20. `SATISFIED`

`SATISFIED` means:

> Sufficient current authoritative evidence establishes that the Appointment has a valid current supporting Resource satisfying the requirement for the applicable scheduled interval.

It does not mean:

```text
Appointment occurred

worker attended

customer arrived

payment succeeded

support cannot later change
```

---

# 21. `UNSATISFIED`

`UNSATISFIED` means:

> Sufficient authoritative evidence establishes that the current support requirement is not presently satisfied.

Examples:

```text
assigned worker now on approved leave

required Resource removed from service

current Allocation no longer valid

EXACT_RESOURCE unavailable
```

This does not automatically cancel the Appointment.

---

# 22. `UNRESOLVED`

`UNRESOLVED` means:

> The support requirement applies, but evidence sufficient to establish whether it is currently satisfied cannot be obtained.

Examples:

```text
required Workforce evidence unavailable

Resource status cannot be established

required Allocation evidence uncertain
```

Main Street SHALL NOT treat:

```text
UNRESOLVED
```

as:

```text
SATISFIED
```

---

# 23. No Appointment `AT_RISK` State

Main Street SHALL NOT introduce:

```text
AT_RISK

NEEDS_REASSIGNMENT

STAFF_UNAVAILABLE
```

as universal Appointment lifecycle states.

Those may be merchant-facing projections of current authoritative evidence.

The underlying facts remain:

```text
Appointment commitment

+

Support Requirement

+

Support Evaluation
```

---

# 24. Existing Appointment Survives Support Failure

If:

```text
Appointment confirmed

        ↓

Sarah later becomes unavailable
```

then:

```text
Appointment remains committed
```

unless a separately authorised cancellation/rescheduling operation occurs.

This preserves v1.7/v1.8.

The system instead has:

```text
support requirement UNSATISFIED
```

and must seek or surface appropriate remediation.

---

# 25. Two Classes of Support Change

Main Street SHALL distinguish:

```text
SUBSTITUTION_WITHIN_REQUIREMENT

MATERIAL_SUPPORT_REQUIREMENT_CHANGE
```

This is the core GRP-04 boundary.

---

# 26. `SUBSTITUTION_WITHIN_REQUIREMENT`

A change is `SUBSTITUTION_WITHIN_REQUIREMENT` when:

> The replacement Resource independently satisfies exactly the same current Appointment Support Requirement.

Example:

```text
Requirement:
ANY_ELIGIBLE_RESOURCE
qualified stylist

Current:
Sarah

Target:
Bob
```

where Bob authoritatively satisfies the same requirement.

No customer commitment meaning changes.

---

# 27. Within-Requirement Substitution Does Not Require Customer Acceptance

Where the current requirement is unchanged:

```text
Sarah → Bob
```

may be performed without a new customer decision if both Resources satisfy:

```text
ANY_ELIGIBLE_RESOURCE
```

and all other authority/invariants are satisfied.

Reason:

> The customer agreed to the requirement, not the current internal assignment.

---

# 28. Within-Requirement Substitution Is Not Rescheduling

If:

```text
time remains 14:00–15:00
```

then:

```text
support Resource changes
```

does not invoke Appointment rescheduling semantics.

The Appointment scheduled revision need not change solely because its Resource Allocation changes.

---

# 29. Within-Requirement Substitution Does Not Require Universal Notification

Main Street SHALL NOT send customers operational noise merely because an internal non-material Resource assignment changed.

Therefore:

```text
SUBSTITUTION_WITHIN_REQUIREMENT
```

does not universally create a Notification obligation.

A merchant policy or applicable customer-facing commitment MAY separately require communication.

---

# 30. Material Requirement Change

A change is a:

**MATERIAL_SUPPORT_REQUIREMENT_CHANGE**

when the proposed replacement does not satisfy the exact current support requirement without changing that requirement's meaning.

Example:

```text
Current requirement:
EXACT_RESOURCE Sarah

Proposed:
Bob
```

The system cannot simply claim:

```text
Bob satisfies Sarah
```

Instead the merchant is proposing:

```text
EXACT_RESOURCE Sarah
        ↓
EXACT_RESOURCE Bob
```

or another authorised requirement change.

---

# 31. Material Change Is Appointment Commitment Amendment

A material support change is not merely Resource reassignment.

It changes a customer-relevant Appointment commitment term.

Appointment therefore owns whether the requirement may be changed.

Resource/Allocation still owns the resulting concrete capacity claim.

---

# 32. Customer-Decision Modes Reuse v1.11 Authority

For merchant-initiated material support changes, the initial customer-decision modes are:

```text
CUSTOMER_ACCEPTANCE_REQUIRED

MERCHANT_AUTHORISED_WITHOUT_NEW_ACCEPTANCE
```

The same governing principle from v1.11 applies:

> Absent exact accepted historical Appointment-policy authority permitting the merchant to change the customer-material support requirement without a new decision, customer acceptance is required.

---

# 33. Historical Policy Affinity Applies

A later merchant policy change SHALL NOT retroactively create authority to replace a customer-promised exact Resource.

Example:

```text
Appointment established under P7:

customer chooses Sarah

        ↓

merchant later activates P8:

merchant may substitute practitioners
```

P8 does not silently rewrite the existing Appointment's P7 customer terms.

---

# 34. Material Change Proposal

Where customer acceptance is required, Main Street SHALL use an:

**AppointmentSupportChangeProposal**

Conceptually:

```text
AppointmentSupportChangeProposal
{
    MerchantScope

    Appointment identity

    expected current Appointment revision

    expected current
    AppointmentSupportRequirementRevision

    proposed support requirement

    proposal deadline

    protection mode

    policy provenance

    proposal identity

    provenance
}
```

---

# 35. AppointmentSupportChangeProposal Is Not Assignment

Hard distinctions:

```text
SupportChangeProposal
    ≠ support requirement change

SupportChangeProposal
    ≠ Resource Allocation

customer acceptance
    ≠ final successful Resource assignment
```

The existing support requirement remains authoritative until the material change commits.

---

# 36. Exact Currentness Affinity

A material support proposal SHALL bind to:

```text
exact Appointment identity

+

expected current Appointment revision

+

expected current Support Requirement revision
```

This prevents stale proposals from mutating:

```text
a rescheduled Appointment

an already changed support requirement

a completed/observed Appointment
```

---

# 37. Customer Decision Authority

Customer decision MAY be established using the accepted v1.11 mechanisms:

```text
DIRECT_CUSTOMER_ACTION

AUTHORISED_MERCHANT_ATTESTATION_OF_CUSTOMER_DECISION
```

Phone and in-person customer decisions remain supported.

No CustomerAccount is universally required.

---

# 38. Conversation and AI Remain Non-Authoritative

Rejected:

```text
Customer:
"Bob is fine"

        ↓

Conversation / AI
automatically changes requirement
```

Conversation content may be evidence for an authorised human operation.

AI may suggest interpretation.

Neither owns customer-decision truth.

---

# 39. Material Change Accepted But Execution Fails

If:

```text
customer accepts Bob
```

but current authoritative evidence later establishes:

```text
Bob unavailable
```

then:

```text
proposal remains ACCEPTED

existing support requirement
remains authoritative

material support change
does not partially commit
```

Customer decision history is not rewritten.

---

# 40. Proposal Deadline Before Scheduled Start

For an asynchronous material support proposal issued before the Appointment begins, the v1.10 deadline model applies.

Absent an earlier accepted cutoff:

```text
Proposal Acceptance Deadline
    =
scheduled Appointment interval start
```

No arbitrary substitute-response timeout is introduced.

---

# 41. At-Service-Time Material Substitution

Real micro-business operation may discover a required substitution when the customer is already present.

Example:

```text
customer arrived for Sarah

Sarah suddenly unavailable

Bob is available
```

Main Street SHALL NOT require creation of a fictitious hours-long asynchronous proposal.

Where customer acceptance is required, it may be established synchronously:

```text
merchant explains substitution

customer agrees

authorised merchant actor
records customer decision

material change commits
```

subject to all current validity requirements.

---

# 42. No Open-Ended Proposal After Appointment Has Started

An asynchronous OPEN support-change proposal SHALL NOT remain indefinitely outstanding after its decision window is no longer meaningful.

At or after the scheduled interaction begins, the initial model requires:

```text
a current synchronous customer decision

or

exact existing policy authority
for merchant action
```

rather than an invented generic extension period.

---

# 43. Optional Target Capacity Protection

Before the customer decides, a material support-change proposal MAY be:

```text
UNPROTECTED

or

PROTECTED
```

Protection is explicit.

The target Resource is not automatically held.

---

# 44. Appointment Assignment Proposal Hold

A PROTECTED material change may establish an:

**Appointment Assignment Proposal Hold**

against the exact target Resource capacity required for the Appointment interval.

The existing support Allocation remains valid while the proposal is unresolved.

Therefore a bounded temporary double claim may exist:

```text
existing current support capacity

+

candidate target Hold
```

where necessary to protect both customer commitment and proposed alternative.

---

# 45. Proposal Hold Does Not Change Requirement

```text
Appointment Assignment Proposal Hold
    ≠
Appointment Support Requirement

    ≠
customer acceptance

    ≠
final Resource Allocation
```

It protects candidate capacity only.

---

# 46. Proposal Hold Deadline

The Assignment Proposal Hold SHALL expire with the exact support-change proposal deadline.

A delayed cleanup process SHALL NOT leave expired candidate capacity authoritatively blocked.

The v1.10 semantic-expiry rule applies.

---

# 47. Within-Requirement Reassignment Operation

This amendment establishes the semantic operation family:

```text
appointment.reassign-support
```

It means:

> Replace the concrete Resource currently satisfying an Appointment Support Requirement with another Resource that independently satisfies that same requirement, without changing the Appointment Support Requirement itself.

Appointment owns the coordination intent.

Resource/Allocation retains authority for concrete capacity mutation.

---

# 48. Material Requirement Change Operation

This amendment also establishes:

```text
appointment.change-support-requirement
```

It means:

> Authoritatively revise the Appointment Support Requirement after the applicable customer/policy authority has been established and the replacement requirement can be satisfied under current Scheduling/Resource constraints.

It is not a generic Resource mutation operation.

---

# 49. Target Must Be Revalidated

No current or historical candidate list grants authority to reassign.

Before commit Main Street SHALL establish:

```text
target Resource exists

correct MerchantScope

target satisfies exact requirement

target is schedulable for exact Appointment interval

required Workforce evidence is sufficient

required capacity can be claimed

applicable policy allows operation
```

A stale “available worker” screen is advisory only.

---

# 50. `UNRESOLVED` Target Cannot Be Used

If required target evidence is:

```text
UNRESOLVED
```

Main Street SHALL NOT:

```text
assume target is available
```

or:

```text
assign with warning
```

The reassignment/change must fail closed or remain unresolved.

---

# 51. Automatic Resource Selection Is Not Granted Here

This amendment does not authorise Main Street to invent:

```text
best worker

nearest worker

cheapest worker

least busy worker

highest rated worker
```

selection algorithms.

Automatic substitution may occur only if a separately accepted deterministic Resource/Scheduling selection authority supplies the target.

Without such authority:

```text
Main Street may surface valid eligible alternatives

merchant chooses
```

---

# 52. AI Cannot Choose Authoritatively

AI MAY:

```text
explain the conflict

summarise candidate evidence

suggest eligible alternatives
where deterministic eligibility
has already been established
```

AI SHALL NOT:

```text
declare an ineligible Resource eligible

invent workforce availability

select a Resource authoritatively
based on model preference

commit a substitution

create customer acceptance
```

---

# 53. Atomic Reassignment

Where a current valid Allocation is being replaced:

```text
current supporting Allocation

        ↓

target Resource validation

        ↓

one authoritative consistency boundary

        ↓

establish target capacity claim
+
replace/release old support claim
+
record assignment transition
```

must preserve the Appointment support invariant.

Rejected:

```text
release Sarah

        ↓

try to allocate Bob

        ↓

Bob unavailable

        ↓

Appointment unnecessarily lost
its previously valid support
```

---

# 54. No Release/Reacquire Gap

Where the old support remains valid until substitution:

> Main Street SHALL NOT release it before the target replacement is authoritatively secured.

A conforming implementation may:

```text
convert
replace
swap
claim-and-release atomically
```

provided there is no externally observable invariant-breaking gap.

---

# 55. Failure Preserves Old Assignment Where Still Valid

If target reassignment fails while the old supporting Allocation remains valid:

```text
old assignment remains authoritative
```

No partial replacement occurs.

---

# 56. Old Support May Already Be Invalid

A different case exists when:

```text
Sarah already became unavailable
```

before reassignment.

Main Street cannot falsely claim:

```text
Sarah remains valid
```

merely because Bob replacement failed.

Correct result:

```text
Appointment still exists

Support Requirement remains

Support Evaluation = UNSATISFIED

replacement attempt failed
```

This distinction is mandatory.

---

# 57. Existing Commitment Does Not Depend on Pretending Invalid Support Is Valid

Appointment commitment truth and current support satisfaction remain distinct.

Therefore:

```text
Appointment exists
+
required support currently unsatisfied
```

is a legitimate operational state of affairs.

Main Street should surface the exception rather than rewrite history.

---

# 58. Concurrency

Concurrent substitution attempts against the same current support assignment SHALL preserve:

```text
at most one current authoritative
support Allocation for that exact
single-capacity requirement
```

Example:

```text
Actor A:
Sarah → Bob

Actor B:
Sarah → Carol
```

Only an operation whose expected current assignment/currentness still governs may commit.

The stale operation fails.

---

# 59. Idempotency

Assignment/substitution operations SHALL comply with MS-PROT-059.

```text
same logical command
+
same material intent
    → stable prior result

same logical command
+
different Resource/requirement
    → conflict
```

Transport retries do not create repeated Allocation transitions.

---

# 60. Multi-Resource Requirements

This amendment does not require every support requirement to have capacity cardinality one.

A separately registered support requirement may legitimately require:

```text
multiple capacity units
```

where accepted semantics already support that model.

However this amendment does not introduce:

```text
arbitrary staff teams
crew composition
shift teams
skill matrices
```

as new Appointment semantics.

---

# 61. Rescheduling Boundary

When an Appointment changes time:

```text
Support Requirement
    normally persists

current Allocation
    must be revalidated for new interval
```

Example:

```text
EXACT_RESOURCE Sarah
10:00 → 15:00
```

cannot commit merely because Sarah was valid at 10:00.

Sarah must be valid for 15:00.

---

# 62. Generic Resource After Reschedule

For:

```text
ANY_ELIGIBLE_RESOURCE
```

a reschedule may result in:

```text
old interval:
Sarah

new interval:
Bob
```

without changing the Appointment Support Requirement if both validly satisfy the same requirement.

The time move remains Appointment rescheduling.

The supporting Resource difference remains Assignment continuity.

---

# 63. Check-In Boundary

A support reassignment does not rewrite Appointment Check-In evidence.

Example:

```text
customer checked in

Sarah becomes unavailable

Bob validly substitutes
```

The customer's arrival fact remains true.

Check-In belongs to the Appointment interaction, not the current supporting Resource.

---

# 64. Material Support Change After Check-In

If the customer is already checked in and:

```text
EXACT_RESOURCE Sarah
```

must become:

```text
EXACT_RESOURCE Bob
```

a synchronous customer decision may authorise the material change.

The previous check-in remains valid because:

```text
customer arrival
```

has not been falsified by changing practitioner.

---

# 65. Occurrence Outcome Boundary

Once an authoritative Appointment Occurrence Outcome exists:

```text
ordinary support reassignment
or
ordinary support-requirement amendment
```

SHALL NOT be used to rewrite the already observed interaction.

Historical corrections must use the appropriate correction/reconciliation authority.

---

# 66. Support Failure Does Not Determine Occurrence Outcome

If a worker becomes unavailable before service:

```text
Support Evaluation = UNSATISFIED
```

does not automatically mean:

```text
MERCHANT_SIDE_NON_OCCURRENCE
```

because the merchant may successfully substitute another valid Resource.

Occurrence outcome is established only from v1.9-authorised outcome evidence.

---

# 67. Resource Reassignment Does Not Determine Workforce Consequences

Changing:

```text
Sarah → Bob
```

does not:

```text
cancel Sarah's shift

create Bob's shift

record Bob worked time

change leave

calculate compensation
```

Workforce retains those authorities.

---

# 68. Appointment Assignment Does Not Manufacture Worker Availability

Likewise:

```text
Bob assigned to Appointment
```

does not prove:

```text
Bob is scheduled to work
```

unless the relevant accepted Workforce evidence independently establishes it.

Circular authority is prohibited.

---

# 69. Booking Boundary

A Resource may participate in both:

```text
Booking
and
Appointment
```

under distinct commitments.

Appointment assignment semantics SHALL NOT silently modify a Booking's booked subject.

Example:

```text
Booking:
Meeting Room A
as exact booked subject

Appointment:
consultation associated with room
```

Appointment cannot internally substitute:

```text
Room A → Room B
```

in a way that rewrites the Booking's customer commitment.

The Booking owner must authorise any applicable Booking change.

---

# 70. Generic Appointment Room Example

Different case:

```text
Appointment requirement:
ANY_ELIGIBLE_RESOURCE
consultation room

Current Allocation:
Room A
```

If Room B satisfies the same requirement:

```text
Room A → Room B
```

may be an ordinary within-requirement Resource reassignment.

No Booking is manufactured.

---

# 71. Customer Communication Boundary

Within-requirement reassignment does not universally require communication.

Material support requirement change committed without a new customer decision under valid merchant authority SHALL create a customer communication responsibility.

Where the customer directly accepted the change, additional communication may still be useful but is not universally required by this semantic authority.

Notification remains delivery owner.

---

# 72. Calendar Boundary

Calendar may project current supporting Resource information where useful.

Calendar SHALL NOT:

```text
become assignment authority

infer support requirement

mutate Allocation

infer staff substitution
from external event title
```

Authoritative changes occur through accepted operations.

---

# 73. Merchant Attention Boundary

A support requirement becoming:

```text
UNSATISFIED
or
UNRESOLVED
```

may legitimately become a future/current Merchant Attention input under an accepted exact Attention Contract.

This amendment does not automatically register such a contract.

It preserves the option for exception-driven operation.

---

# 74. Merchant UX

For generic support:

```text
Sarah is unavailable for
Sarah Jones — Haircut — 2:00 PM

Bob is available and can cover.

[Use Bob]
```

Main Street should not ask:

```text
Change Allocation Claim?
Rebind Workforce Resource?
```

---

# 75. Merchant UX for Exact Customer Requirement

Where the customer requested Sarah:

```text
Sarah is unavailable.

This customer selected Sarah.

Bob is available.

[Ask customer about Bob]
[Keep current appointment]
[Other options]
```

Main Street exposes the business distinction rather than expecting the merchant to understand semantic ownership.

---

# 76. Exception-Driven Operation

Where Main Street has sufficient deterministic authority to preserve the same support requirement safely, administrative effort SHOULD be minimised.

Long-term target:

```text
ordinary equivalent substitution
    → automated where accepted
      selection authority exists

material or unresolved substitution
    → merchant attention
```

No automatic selector is authorised by this amendment itself.

---

# 77. Falsification — Generic Stylist Substitution

Scenario:

```text
Haircut Appointment
Requirement:
ANY_ELIGIBLE_RESOURCE / stylist

Sarah currently assigned

Sarah unavailable

Bob authoritatively eligible
and available
```

Expected:

```text
Sarah → Bob

Appointment unchanged

Support Requirement unchanged

no customer acceptance required
```

**PASS**

---

# 78. Falsification — Named Stylist

Scenario:

```text
Customer explicitly selected Sarah

Requirement:
EXACT_RESOURCE Sarah

merchant wants Bob instead
```

Expected:

```text
ordinary reassignment prohibited

material Support Requirement change required

customer acceptance required
unless historical applicable policy
authorises otherwise
```

**PASS**

---

# 79. Falsification — Later Policy Change

Scenario:

Appointment was established when exact practitioner substitution required customer agreement.

Merchant later enables unilateral substitution.

Expected:

```text
new policy does not
retroactively authorise
change to existing Appointment
```

**PASS**

---

# 80. Falsification — Membership Similarity

Scenario:

Bob has the same Merchant Membership and display role as Sarah but no exact applicable:

```text
Workforce Scheduling Arrangement
↔
schedulable Resource
```

link.

Expected:

```text
Bob cannot be treated
as workforce-qualified target
through inference
```

**PASS**

---

# 81. Falsification — Compensation Relationship Shortcut

Scenario:

Bob has a Compensation Relationship but the relevant Workforce Scheduling Arrangement ↔ Resource affinity is absent.

Expected:

```text
not sufficient
```

**PASS**

---

# 82. Falsification — Leave After Appointment Confirmation

Scenario:

```text
Sarah assigned

Appointment confirmed

later:
Approved Leave
```

Expected:

```text
Appointment remains committed

support may become UNSATISFIED

no automatic cancellation

no automatic occurrence outcome
```

**PASS**

---

# 83. Falsification — Target Evidence Unresolved

Scenario:

Bob appears eligible, but current required Workforce evidence cannot be established.

Expected:

```text
Target support evaluation:
UNRESOLVED

no assignment commit
```

**PASS**

---

# 84. Falsification — Concurrent Reassignment

Scenario:

```text
Sarah → Bob

and

Sarah → Carol

concurrently
```

Expected:

```text
one current transition may commit

stale competing transition fails
```

**PASS**

---

# 85. Falsification — Failed Replacement With Valid Old Resource

Scenario:

```text
Sarah still valid

Bob replacement attempt fails
```

Expected:

```text
Sarah remains current support
```

**PASS**

---

# 86. Falsification — Old Resource Already Invalid

Scenario:

```text
Sarah no longer valid

Bob replacement fails
```

Expected:

```text
Appointment remains committed

Support Requirement remains

Support Evaluation = UNSATISFIED

Main Street does not falsely
restore Sarah's validity
```

**PASS**

---

# 87. Falsification — Generic Room Reassignment

Scenario:

```text
Requirement:
ANY_ELIGIBLE_RESOURCE
consultation room

Room A becomes unavailable

Room B satisfies requirement
```

Expected:

```text
Room A → Room B
without customer commitment amendment
```

**PASS**

---

# 88. Falsification — Exact Booked Room

Scenario:

A separate Booking promises exact Room A.

Appointment operational logic proposes Room B.

Expected:

```text
Appointment cannot rewrite
Booking booked-subject truth
```

**PASS**

---

# 89. Falsification — Check-In Then Worker Change

Scenario:

```text
customer checked in

generic stylist Sarah unavailable

Bob substitutes
```

Expected:

```text
Check-In remains true

Appointment remains same

no new Check-In required
solely because assignment changed
```

**PASS**

---

# 90. Falsification — Named Worker Change After Check-In

Scenario:

Customer arrived for explicitly promised Sarah.

Sarah cannot provide service.

Customer agrees in person to Bob.

Expected:

```text
authorised synchronous
customer decision

Support Requirement revised

Bob validly assigned

existing Check-In remains true
```

**PASS**

---

# 91. Falsification — Outcome Already Recorded

Scenario:

```text
Appointment outcome = OCCURRED

merchant later edits supporting worker
```

Expected:

```text
ordinary reassignment rejected
```

Historical correction/reconciliation is required where evidence was wrong.

**PASS**

---

# 92. Falsification — Reschedule With Exact Worker

Scenario:

```text
Appointment:
10:00 with exact Sarah

reschedule:
15:00
```

Sarah is unavailable at 15:00.

Expected:

```text
reschedule cannot treat
old Sarah validity as sufficient
```

The exact requirement persists and must be revalidated.

**PASS**

---

# 93. Falsification — AI Reassignment

Scenario:

AI decides:

```text
Bob looks least busy.
Use Bob.
```

Expected:

```text
no authoritative assignment
```

unless deterministic accepted eligibility/selection authority independently establishes the target and an authorised operation commits it.

**PASS**

---

# 94. Falsification — No Automatic Selector

Scenario:

Three Resources are eligible.

No accepted selection policy exists.

Expected:

```text
Main Street does not invent
alphabetical/random/least-busy choice

merchant may choose
from valid alternatives
```

**PASS**

---

# 95. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The proposal introduces internal complexity around:

```text
Support Requirement revisions

Resource affinity

Workforce Arrangement affinity

current support evaluation

capacity replacement

customer-material versus
operational substitution

concurrency

historical policy

customer decision evidence
```

because that complexity is necessary to preserve customer commitments without forcing merchants to administer separate scheduling, workforce and resource systems.

Merchant-facing meaning remains:

```text
Who can cover this appointment?

Was this exact person/resource
promised to the customer?

Does the customer need to agree?
```

Main Street absorbs the technical model.

---

# 96. Administrative-Compression Result

The architecture supports the desired progression:

```text
current support fails
        ↓
Main Street understands why
        ↓
Main Street finds authoritative
valid alternatives where possible
        ↓
equivalent change:
    simple reassignment

material change:
    customer-decision flow
        ↓
merchant sees only
the business decision
```

No cross-system reconciliation is required from the merchant.

---

# 97. Anti-ERP Review

Explicitly excluded:

```text
generic workforce assignment engine

generic dispatch board

field-service routing

worker ranking

route optimisation

skills-management platform

HR competency system

shift management

project resource management

work orders

crew planning

generic task ownership

generic capacity planning ERP
```

The amendment is limited to:

> preserving an existing Appointment when its supporting Resource changes.

**PASS**

---

# 98. Hard Invariants

```text
INV-042-V112-001
Appointment owns Appointment Support Requirement; Resource/Allocation owns concrete current supporting capacity.

INV-042-V112-002
Workforce owns workforce availability and cannot silently rewrite Appointment commitment truth.

INV-042-V112-003
Current concrete Resource assignment does not by itself make Resource identity a customer commitment.

INV-042-V112-004
The initial Support Requirement selection modes are ANY_ELIGIBLE_RESOURCE and EXACT_RESOURCE.

INV-042-V112-005
EXACT_RESOURCE cannot be satisfied by a different Resource merely because the target appears equivalent.

INV-042-V112-006
ANY_ELIGIBLE_RESOURCE substitution may change concrete Resource without changing Appointment commitment meaning.

INV-042-V112-007
Worker-backed support requires exact accepted Workforce Scheduling Arrangement ↔ schedulable Resource affinity.

INV-042-V112-008
Merchant Membership, Identity or Compensation Relationship similarity cannot manufacture that affinity.

INV-042-V112-009
Support Requirement is independently revisioned from scheduled-interval revisions.

INV-042-V112-010
Rescheduling preserves Support Requirement unless independently authorised change occurs.

INV-042-V112-011
Rescheduling must revalidate support against the new interval.

INV-042-V112-012
Support becoming UNSATISFIED or UNRESOLVED does not cancel the Appointment.

INV-042-V112-013
Support failure does not automatically establish Appointment Occurrence Outcome.

INV-042-V112-014
SUBSTITUTION_WITHIN_REQUIREMENT does not require customer acceptance.

INV-042-V112-015
MATERIAL_SUPPORT_REQUIREMENT_CHANGE requires the applicable customer/policy authority.

INV-042-V112-016
Later policy cannot retroactively create authority to alter an existing customer-material support requirement.

INV-042-V112-017
Conversation content and AI inference cannot create customer acceptance.

INV-042-V112-018
Target assignment must be authoritatively revalidated before commit.

INV-042-V112-019
UNRESOLVED target evidence cannot be treated as valid assignment authority.

INV-042-V112-020
Replacing a still-valid current Allocation must not create a release/reacquire gap.

INV-042-V112-021
Failed replacement leaves the old assignment intact only where that old assignment remains independently valid.

INV-042-V112-022
A failed replacement cannot manufacture validity for an already unavailable Resource.

INV-042-V112-023
Concurrent assignment changes cannot produce contradictory current assignments for one single-capacity support requirement.

INV-042-V112-024
Appointment reassignment does not mutate Workforce schedule, leave, worked time or compensation.

INV-042-V112-025
Appointment support reassignment cannot silently rewrite a separate Booking booked subject.

INV-042-V112-026
Check-In remains distinct from support assignment.

INV-042-V112-027
Support assignment/change is not Appointment rescheduling when the scheduled interval does not change.

INV-042-V112-028
Ordinary support mutation is prohibited after authoritative Appointment Occurrence Outcome where it would rewrite historical interaction truth.

INV-042-V112-029
AI does not gain Resource eligibility, selection, customer-decision or assignment authority.

INV-042-V112-030
No automatic Resource-selection heuristic exists absent separately accepted deterministic selection authority.
```

---

# 99. Rejected Alternatives

Rejected:

```text
Appointment owns worker identity
and worker schedule

every assigned worker is
customer-promised

staff change always
requires customer acceptance

staff change never
requires customer acceptance

any employee may replace
any other employee

same Membership implies
schedulable Resource equivalence

same Compensation Relationship
implies Appointment eligibility

worker leave automatically
cancels Appointment

worker unavailable automatically
means MERCHANT_SIDE_NON_OCCURRENCE

release old assignment first,
then try replacement

failed replacement restores
an actually invalid old Resource

current assignment rewritten
inside Appointment row
without Resource authority

rescheduling clears
support requirements

Appointment support change
silently rewrites Booking

AI chooses "best" worker

generic least-busy assignment

generic dispatch engine

generic skills/competency DSL

generic field-service management
```

---

# 100. Resolved Work-Package Outcome

If accepted:

```text
MS-PROT-042-GRP-04
    → RESOLVED
```

The initial portfolio will have resolved:

```text
staff substitution

+

Resource reassignment
```

through one coherent semantic boundary:

```text
Appointment Support Requirement
        ≠
current Resource Allocation
```

No retained semantic DQ remains inside GRP-04.

---

# 101. Deliberately Non-Deferred Adjacent Scope

The following are not hidden unfinished GRP-04 decisions:

```text
automatic worker ranking
automatic Resource optimisation
worker skill/qualification model
Workforce shift design
generic dispatch
Booking Resource reassignment
field-service routing
generic staff teams
```

They belong to other authorities or require fresh Feature Admission.

The absence of an automatic target-selection algorithm is a valid initial state.

---

# 102. Implementation-Rules Impact

Acceptance SHALL NOT activate implementation.

A future conforming implementation must preserve:

```text
Appointment Support Requirement ownership

exact support-requirement revision affinity

Resource/Allocation ownership

Workforce Arrangement ↔ Resource affinity

current Scheduling evidence

SATISFIED / UNSATISFIED / UNRESOLVED distinction

idempotency

concurrency

atomic replacement where required

customer-decision provenance

historical policy affinity

auditability

no AI authority
```

Exact:

```text
schema
API shape
locking mechanism
Resource selector
UI components
database transaction mechanism
```

remain downstream.

---

# 103. Next Group Consequence

After acceptance and formalisation:

```text
MS-PROT-042-GRP-04
    → RESOLVED
```

and the grouped execution overlay advances to:

```text
MS-PROT-042-GRP-03

Repeated & Shared Appointment Commitments

recurring Appointment
+
group / multi-customer Appointment
```

The global dependency graph remains paused under the approved grouped-cleanup direction.

---

# 104. Recommendation

**RECOMMENDATION: ACCEPT**

The proposal resolves the principal ambiguity by refusing to collapse:

```text
customer commitment

worker identity

Resource identity

current Allocation

Workforce availability
```

into one mutable Appointment record.

It permits Main Street to handle ordinary operational substitution cheaply while protecting customer-material promises.

It passes:

```text
Feature Admission

Fundamental Vision Conformance

ownership review

ambiguity review

cross-capability review

concurrency review

anti-ERP review

falsification
```

It is authoritative by manual approval recorded in this accepted amendment.