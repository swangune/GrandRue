# MS-PROT-091 — Workforce Rota Composition & Assignment State

**Document ID:** MS-PROT-091  
**Version:** 1.0  
**Status:** **ACCEPTED by manual approval on 9 September 2026**  
**Approved:** Manual approval of the complete revised authority presented in ChatGPT on 9 September 2026 after post-approval corpus-conformance correction  
**Authority type:** Workforce rota-composition and assignment-acquisition semantic/design authority  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Depends on:** composite MS-PROT-074; composite MS-PROT-081  
**Design node:** Priority-A Workforce / Rota Composition  
**Implementation activation:** NONE  
**Previous review:** The earlier complete proposal received manual approval on 9 September 2026 but was not formalised because corpus conformance identified material collision with composite MS-PROT-081. This revised complete authority received fresh manual approval on 9 September 2026.

---

# 1. Purpose

MS-PROT-091 defines Main Street's canonical semantics for:

- composing merchant workforce rotas;
- representing work requirements before a particular worker is committed;
- coordinating direct assignment and existing targeted Shift Offers;
- supporting optional worker-picked open shifts;
- controlling assignment cardinality and concurrency;
- representing operational scheduling exclusions distinct from Leave;
- publishing and revising rotas;
- representing variable Work Sites where materially required;
- preserving reassignment and rota-change provenance.

MS-PROT-091 does **not** create a second canonical worker-scheduling commitment.

Composite MS-PROT-081 remains authoritative for:

```text
Workforce Scheduling Arrangement
Workforce Time Terms Revision
Shift Offer
Shift Offer Decision
Scheduled Work Commitment
Scheduled Break
Timekeeping
Worked-Time evidence
Leave
Approved Leave evidence
```

The canonical authority path is:

```text
MS-PROT-074
Merchant Membership / workforce participation
        ↓
MS-PROT-081
Workforce Scheduling Arrangement
+ Workforce Time Terms
        ↓
MS-PROT-091
rota composition
+ open-shift coordination
+ cardinality
+ work-site refinement
        ↓
MS-PROT-081
Scheduled Work Commitment
+ workforce availability evidence
        ↓
MS-PROT-042
Appointment / capacity consumers
```

No downstream capability may interpret MS-PROT-091 planning or rota state directly as customer-facing workforce availability.

---

# 2. Governing Decision

Main Street SHALL distinguish:

```text
work that needs somebody
        ≠
a particular person scheduled to perform it
```

MS-PROT-091 may own the first.

MS-PROT-081 `ScheduledWorkCommitment` owns the second.

Therefore:

> **A rota Shift is a work requirement. A Scheduled Work Commitment is the authoritative fact that one exact workforce participant is scheduled against that requirement.**

No `ShiftAssignment` aggregate shall duplicate `ScheduledWorkCommitment`.

Where merchant or worker interfaces use words such as:

```text
assigned
my shift
filled
staffed
```

those are projections over authoritative `ScheduledWorkCommitment` state.

---

# 3. Authority Boundary

## 3.1 MS-PROT-091 owns

MS-PROT-091 owns:

- RotaPeriod;
- rota Shift identity and revision;
- required Shift headcount;
- rota publication/revision;
- open worker-selection eligibility;
- ShiftClaim;
- open-claim concurrency;
- operational ScheduleExclusion where distinct from Leave;
- conditional WorkSite semantics;
- fixed-site/variable-site scheduling-location configuration;
- rota conflict observation;
- rota coordination provenance;
- reassignment coordination;
- derived rota projections.

## 3.2 Composite MS-PROT-081 retains

Composite MS-PROT-081 retains:

- WorkforceSchedulingArrangement;
- WorkforceTimeTermsRevision;
- targeted ShiftOffer;
- ShiftOfferDecision;
- ScheduledWorkCommitment;
- ScheduledBreak;
- TimeCaptureEvent;
- attendance/worked-time evidence;
- leave requests/decisions;
- ApprovedLeaveEvidence;
- workforce availability evidence.

## 3.3 MS-PROT-091 does not own

MS-PROT-091 does not own:

- employment status;
- employment-contract formation;
- recruitment;
- payroll;
- wages;
- compensation;
- attendance;
- clock-in/out;
- timesheets;
- statutory leave entitlement;
- performance management;
- customer appointment commitment;
- customer-facing availability;
- Resource authority;
- cross-workspace labour marketplaces;
- navigation/routing;
- AI workforce optimisation;
- statutory HR administration.

---

# 4. Workforce Scheduling Arrangement Affinity

Every worker-specific scheduling action governed or introduced by MS-PROT-091 MUST resolve to one exact:

```text
WorkforceSchedulingArrangement
```

from composite MS-PROT-081.

Merchant Membership alone is insufficient.

Therefore:

```text
Merchant Membership
        ↓
Workforce Scheduling Arrangement
        ↓
Workforce Time Terms Revision
        ↓
worker-specific scheduling action
```

A person may have multiple Scheduling Arrangements with the same merchant.

Main Street MUST NOT guess the applicable Arrangement from:

- job title;
- current pay relationship;
- most recent Shift;
- Identity;
- Merchant Membership alone;
- AI inference.

Where the correct Arrangement cannot be established:

```text
UNRESOLVED
```

is required.

---

# 5. Core Concepts

MS-PROT-091 introduces or refines:

1. `RotaPeriod`
2. `Shift`
3. `ShiftClaim`
4. `ScheduleExclusion`
5. `WorkSite` — conditionally admitted
6. merchant rota configuration
7. rota revision/audit evidence

It consumes but does not redefine:

8. `WorkforceSchedulingArrangement`
9. `WorkforceTimeTermsRevision`
10. `ShiftOffer`
11. `ShiftOfferDecision`
12. `ScheduledWorkCommitment`

from MS-PROT-081.

---

# 6. RotaPeriod

A `RotaPeriod` is an operational coordination container grouping Shift requirements for planning, publication and human comprehension.

Canonical attributes include:

```text
rota_period_id
workspace_id
starts_at
ends_at
schedule_timezone
state
revision
created_at
created_by
```

A RotaPeriod may correspond to:

- a week;
- a fortnight;
- a month;
- another operational planning interval.

Its duration creates no payroll or contractual meaning.

---

# 7. RotaPeriod Lifecycle

Canonical states:

```text
DRAFT
  ↓
PUBLISHED
  ↓
CLOSED
```

`CLOSED` means the rota period is no longer ordinarily being edited.

It does not establish:

- attendance;
- completion of work;
- payable hours;
- payroll completion.

Consequential changes after publication occur through revision rather than destructive rewriting.

---

# 8. Shift

A `Shift` represents an operational requirement for work over an explicit interval.

Canonical attributes include:

```text
shift_id
workspace_id
rota_period_id
starts_at
ends_at
required_headcount
allocation_mode
operational_role_ref?
classification_requirement?
state
revision
```

Location attributes are admitted only where the merchant's operating model requires them.

A Shift may exist with:

```text
zero ScheduledWorkCommitments
```

because the work requirement may not yet have been staffed.

---

# 9. Shift Does Not Schedule a Person

Hard invariant:

```text
Shift
    ≠
ScheduledWorkCommitment
```

A Shift says:

> work is required.

A ScheduledWorkCommitment says:

> this exact workforce participant, under this exact Workforce Scheduling Arrangement and Workforce Time Terms Revision, is scheduled to perform it.

Only MS-PROT-081 owns the latter fact.

---

# 10. Shift Lifecycle

Canonical states:

```text
DRAFT
PUBLISHED
CANCELLED
```

Typical flow:

```text
DRAFT
 ├──→ PUBLISHED
 └──→ discarded where still non-consequential

PUBLISHED
 └──→ CANCELLED
```

No automatic `COMPLETED` state exists.

Time passing does not prove work occurred.

---

# 11. Assignment Cardinality

Each Shift MUST declare:

```text
required_headcount >= 1
```

Example:

```text
required_headcount = 1
```

means one ScheduledWorkCommitment may fill the requirement.

```text
required_headcount = 4
```

means up to four qualifying ScheduledWorkCommitments may fill it.

Headcount MUST NOT be inferred from:

- current commitments;
- business type;
- historical rota;
- role name.

A derived rota projection may describe a Shift as:

```text
UNFILLED
PARTIALLY_FILLED
FILLED
```

but those are derived statuses.

The authoritative worker-specific facts remain the underlying ScheduledWorkCommitments.

---

# 12. Work Allocation Modes

A Shift may use one supported allocation mode:

```text
DIRECT_ASSIGNMENT
TARGETED_OFFER
OPEN_SELECTION
```

These are work-allocation mechanisms.

They do not classify employment.

---

# 13. DIRECT_ASSIGNMENT

`DIRECT_ASSIGNMENT` preserves the accepted MS-PROT-081 mechanism.

Where the applicable Workforce Time Terms permit direct assignment:

```text
authorised scheduler
        ↓
selects exact Workforce Scheduling Arrangement
        ↓
validates Shift + terms + constraints
        ↓
MS-PROT-081 ScheduledWorkCommitment
```

No separate ShiftAssignment authority is created.

---

# 14. TARGETED_OFFER

`TARGETED_OFFER` preserves MS-PROT-081 `ShiftOffer` and `ShiftOfferDecision`.

Canonical flow:

```text
merchant
    ↓
ShiftOffer
to exact Workforce Scheduling Arrangement
    ↓
worker ACCEPT
    ↓
ScheduledWorkCommitment

worker REJECT
    ↓
no ScheduledWorkCommitment
```

MS-PROT-091 does not replace, rename or duplicate this targeted offer mechanism.

---

# 15. OPEN_SELECTION

`OPEN_SELECTION` is the additional rota-composition mechanism introduced by MS-PROT-091.

It applies where:

- a Shift is made available to a bounded eligible workforce population within one merchant workspace;
- individual eligible workers may select it;
- a particular worker was not pre-selected by the merchant.

This is internal workforce scheduling.

It is not:

- recruitment;
- a job marketplace;
- cross-merchant labour brokering;
- employment-contract formation.

---

# 16. ShiftClaim

Worker selection of an OPEN_SELECTION Shift is represented by a canonical `ShiftClaim`.

Attributes include:

```text
shift_claim_id
workspace_id
shift_id
workforce_membership_id
workforce_scheduling_arrangement_id
workforce_time_terms_revision_id
state
claimed_at
resolved_at?
resolved_by?
resolution_reason?
shift_revision
idempotency_key
```

A claim MUST identify one exact Workforce Scheduling Arrangement.

A claim means:

> this existing eligible workforce participant has selected/requested this open Shift.

It does not mean:

> this worker is scheduled.

---

# 17. ShiftClaim Lifecycle

Canonical states:

```text
PENDING
ACCEPTED
REJECTED
WITHDRAWN
EXPIRED
```

A `PENDING` claim is not a ScheduledWorkCommitment.

Only an `ACCEPTED` claim may participate in creation of a ScheduledWorkCommitment.

---

# 18. Open-Selection Confirmation Policy

Where OPEN_SELECTION is materially required, merchant configuration may select:

```text
AUTO_CONFIRM
APPROVAL_REQUIRED
```

This configuration MUST NOT appear for merchants that do not use OPEN_SELECTION.

## 18.1 AUTO_CONFIRM

For a valid claim:

```text
validate claim
+
validate exact Arrangement
+
validate current Workforce Time Terms
+
validate Shift revision
+
validate remaining headcount
+
validate relevant conflicts
        ↓
atomically
        ↓
ShiftClaim = ACCEPTED
+
MS-PROT-081 ScheduledWorkCommitment created
```

If the commitment cannot be created, the transaction MUST NOT leave an accepted claim falsely implying scheduled work.

## 18.2 APPROVAL_REQUIRED

A valid worker action creates:

```text
ShiftClaim = PENDING
```

No ScheduledWorkCommitment exists yet.

An authorised scheduler may subsequently:

```text
ACCEPT
    → atomically create ScheduledWorkCommitment

REJECT
    → retain rejected claim evidence
```

A pending claim does not ordinarily consume confirmed Shift headcount.

Any future claim-reservation mechanism requires separate authority.

---

# 19. Extension to ScheduledWorkCommitment Provenance

MS-PROT-091 extends the accepted work-allocation provenance portfolio without replacing MS-PROT-081.

A ScheduledWorkCommitment may originate through:

```text
DIRECT_ASSIGNMENT
ACCEPTED_TARGETED_SHIFT_OFFER
ACCEPTED_OPEN_SHIFT_CLAIM
```

Every resulting commitment still retains the exact MS-PROT-081-required affinity:

```text
Merchant Scope
Merchant Membership
Workforce Scheduling Arrangement
Workforce Time Terms Revision
shift identity
shift revision
scheduled interval
Scheduled Breaks where applicable
provenance
```

---

# 20. Assignment Eligibility

Before any new ScheduledWorkCommitment is created through an MS-PROT-091-coordinated path, Main Street MUST establish at minimum:

1. Merchant Membership exists.
2. Membership belongs to the same Merchant Scope/workspace.
3. exact Workforce Scheduling Arrangement exists.
4. Arrangement is ACTIVE.
5. Arrangement belongs to the referenced membership.
6. applicable Workforce Time Terms Revision is exact and current for the operation.
7. Shift belongs to the same workspace.
8. Shift is in an assignable state.
9. remaining headcount exists.
10. required classification/role constraints are satisfied where applicable.
11. applicable MS-PROT-081 Approved Leave does not prohibit the commitment.
12. applicable active ScheduleExclusion does not prohibit the commitment.
13. applicable same-Arrangement overlap constraints are satisfied.
14. applicable composite MS-PROT-081 cross-Arrangement overlap and `MinimumInterCommitmentBuffer` constraints are satisfied where their predicates apply.
15. actor/worker has authority for the chosen allocation mechanism.
16. Shift revision is current.
17. the mutation is atomic.

Classification constrains suitability.

It does not grant authority.

---

# 21. Concurrent Open Selection

Concurrency MUST be deterministic.

For:

```text
required_headcount = 1
```

two workers may legitimately claim simultaneously.

Only one transaction may create the final ScheduledWorkCommitment.

The winning transaction must atomically validate:

```text
current Shift revision
+
remaining headcount
+
claim eligibility
+
Arrangement eligibility
```

A competing transaction that loses the race receives a stable domain result equivalent to:

```text
SHIFT_FILLED
```

No last-write-wins replacement is permitted.

---

# 22. Multi-Person Shift

For:

```text
required_headcount = 4
```

up to four qualifying current ScheduledWorkCommitments may fill the Shift.

A fifth commitment MUST NOT be created merely because multiple clients observed stale available capacity.

Headcount enforcement is transactional.

---

# 23. Idempotency

Claim and scheduling mutations must tolerate retries.

The same logical request retried with the same idempotency identity MUST NOT produce:

- duplicate ShiftClaims;
- duplicate ScheduledWorkCommitments;
- duplicate capacity consumption;
- duplicate release/cancellation effects.

A client that loses the response after commit must be able to recover the committed state.

---

# 24. Same-Arrangement Overlap

Within one exact Workforce Scheduling Arrangement, Main Street MUST NOT ordinarily create incompatible overlapping ScheduledWorkCommitments.

Where an applicable accepted policy permits an override, the override must preserve:

```text
conflicting commitments
actor
timestamp
reason
applicable revisions
```

No conflicting commitment may be silently deleted.

---

# 25. Cross-Arrangement Overlap

One Merchant Membership may have multiple Workforce Scheduling Arrangements.

MS-PROT-091 therefore MUST NOT convert person identity into a universal overlap prohibition.

For different Scheduling Arrangements belonging to the same person:

```text
overlap MUST be detectable
overlap MUST NOT be silently assumed safe
overlap MUST NOT be universally rejected by MS-PROT-091
```

The exact policy is governed by:

```text
MS-PROT-081 v1.2
Cross-Arrangement Overlap & Merchant Scheduling-Buffer Amendment
```

MS-PROT-091 MUST NOT own, infer, duplicate or override `ArrangementOverlapPolicy`, `CrossArrangementOverlapOverride` or `MinimumInterCommitmentBuffer`.

Where an MS-PROT-091-coordinated operation would create or materially revise an MS-PROT-081 `ScheduledWorkCommitment`, the applicable MS-PROT-081 v1.2 overlap or sequential-buffer predicate MUST be evaluated by the owning Workforce Scheduling authority.

---

# 26. Cross-Workspace Boundaries

MS-PROT-091 creates no global labour-coordination authority across independent merchant workspaces.

A worker relationship in Workspace A and another in Workspace B do not create a global scheduling aggregate.

Cross-workspace scheduling coordination requires separate authority.

---

# 27. ScheduleExclusion

A `ScheduleExclusion` represents a merchant-operational interval during which one exact Workforce Scheduling Arrangement should not ordinarily receive a new ScheduledWorkCommitment.

Attributes include:

```text
schedule_exclusion_id
workspace_id
workforce_membership_id
workforce_scheduling_arrangement_id
starts_at
ends_at
source
state
operational_reason_code?
created_at
created_by
```

It is deliberately distinct from MS-PROT-081 Leave.

---

# 28. ScheduleExclusion Is Not Leave

Hard distinction:

```text
ScheduleExclusion
    ≠
LeaveRequest
    ≠
LeaveDecision
    ≠
ApprovedLeaveEvidence
    ≠
statutory leave
```

A ScheduleExclusion may represent operational unavailability that does not require the legal/contractual Leave model.

Where the business fact is genuinely Leave, MS-PROT-081 MUST be used instead.

Main Street MUST NOT use ScheduleExclusion as a shortcut around Leave authority.

---

# 29. ScheduleExclusion Lifecycle

Canonical states:

```text
REQUESTED
ACTIVE
DECLINED
WITHDRAWN
EXPIRED
```

Typical flow:

```text
REQUESTED
 ├──→ ACTIVE
 └──→ DECLINED

ACTIVE
 ├──→ WITHDRAWN
 └──→ EXPIRED
```

`ACTIVE` means only:

> ordinary new scheduling is excluded over that interval for that exact Scheduling Arrangement.

It creates no pay or statutory entitlement.

---

# 30. Exclusion Enforcement

An applicable ACTIVE ScheduleExclusion blocks ordinary creation of a conflicting ScheduledWorkCommitment.

Any permitted override must be:

- explicit;
- authorised;
- reasoned;
- attributable;
- auditable.

The exclusion itself remains preserved.

---

# 31. Approved Leave Precedence

MS-PROT-081 ApprovedLeaveEvidence remains independently authoritative.

Where Approved Leave conflicts with proposed scheduling:

```text
MS-PROT-081 rules govern
```

MS-PROT-091 MUST NOT:

- downgrade Approved Leave into a ScheduleExclusion;
- reinterpret it;
- silently override it;
- duplicate its approval lifecycle.

---

# 32. Arrangement or Membership Deactivation

If a Workforce Scheduling Arrangement becomes ENDED:

- no new ShiftClaim may be accepted for it;
- no new ScheduledWorkCommitment may be created for it;
- historical claims/commitments remain preserved;
- future rota consequences require explicit resolution.

If Merchant Membership ceases to permit workforce participation, the same preservation rule applies.

No destructive cascade is permitted.

---

# 33. Reassignment

Reassignment MUST NOT rewrite one ScheduledWorkCommitment so that it appears always to have belonged to another worker or Arrangement.

Instead:

```text
existing ScheduledWorkCommitment
    ↓ explicit release/cancellation/revision consequence
historical provenance preserved
    +
new exact worker/Arrangement validation
    ↓
new ScheduledWorkCommitment
```

Where useful, coordination evidence may retain a relationship such as:

```text
replacement_of_commitment_id
```

without merging the two commitment identities.

---

# 34. Shift Cancellation

Cancelling a published Shift preserves:

- Shift identity;
- prior revisions;
- affected ShiftClaims;
- affected ScheduledWorkCommitment references;
- cancellation actor;
- timestamp;
- reason where applicable.

Downstream commitment consequences must occur through the owning scheduling authority.

Hard deletion of consequential scheduling evidence is prohibited.

---

# 35. Draft Deletion

A DRAFT Shift may be discarded before it has become consequential.

Once a Shift has:

- been published;
- received a ShiftClaim;
- produced a ShiftOffer;
- produced a ScheduledWorkCommitment;
- participated in an override;
- otherwise become operational evidence,

its identity/history must be preserved.

---

# 36. Rota Publication

Publication establishes a stable operational rota revision.

Publication must atomically reject structurally invalid rota state including:

- invalid intervals;
- cross-workspace references;
- impossible headcount;
- stale revisions;
- invalid WorkSite requirements;
- already-established prohibited same-Arrangement conflicts.

Publication does not itself establish or mutate `ArrangementOverlapPolicy`, `CrossArrangementOverlapOverride` or `MinimumInterCommitmentBuffer`.

Where publication validates or coordinates already worker-affined commitments, the applicable MS-PROT-081 v1.2 scheduling policy remains authoritative and any unresolved or prohibited conflict remains explicit.

Publication does not prove a worker has read the rota.

---

# 37. Rota Revision

Consequential post-publication changes create a new revision.

Examples include:

- Shift start/end change;
- Shift cancellation;
- required headcount change;
- WorkSite change;
- material work-location change;
- allocation-mode change where still valid.

Where a change affects an existing MS-PROT-081 ScheduledWorkCommitment or acceptance-material terms, the applicable MS-PROT-081 revision/acceptance rules govern.

091 MUST NOT silently carry previous worker acceptance across a change that 081 treats as material.

---

# 38. Concurrency and Stale Writes

Mutations must target current relevant revisions.

Main Street shall reject or explicitly reconcile stale writes where concurrent changes affect:

- Shift interval;
- headcount;
- allocation mode;
- WorkSite;
- claim resolution;
- ScheduleExclusion;
- commitment coordination.

No unsafe last-write-wins semantics are permitted.

---

# 39. Merchant-Conditioned Location Semantics

Scheduling location is not universal merchant functionality.

Main Street admits location semantics only when materially required by the merchant's operation.

Canonical configuration modes:

```text
NO_SHIFT_LOCATION
WORKSPACE_LOCATION
SHIFT_WORK_SITE
```

These are merchant-owned configuration semantics.

They need not be presented as a universal settings page.

---

# 40. NO_SHIFT_LOCATION

Used when physical destination has no meaningful rota role.

Main Street SHALL omit unnecessary:

- WorkSite management;
- map controls;
- shift-location selectors;
- navigation actions.

---

# 41. WORKSPACE_LOCATION

Used when work ordinarily occurs at the merchant's established location.

The workspace location is implicit.

The merchant SHALL NOT be forced to repeatedly choose or copy the same address for every Shift.

The known workplace may be projected to workers where useful.

---

# 42. SHIFT_WORK_SITE

Used where Shifts may occur at materially different destinations.

Examples of operational models include:

- security staffing;
- agency assignments;
- cleaning;
- field maintenance;
- event staffing.

These are examples, not business-type hardcoding.

---

# 43. WorkSite

Where SHIFT_WORK_SITE is admitted, a canonical WorkSite may contain:

```text
work_site_id
workspace_id
location_name
address
latitude?
longitude?
map_metadata?
operational_notes?
state
```

Human-readable names are required where operationally useful, for example:

```text
Tesco Swansea Marina
Cardiff City Stadium — Gate 4
Client A — Riverside Office
```

A WorkSite may represent merchant premises, client premises, event venue or temporary workplace.

It does not imply ownership of that place.

---

# 44. WorkSite and MS-PROT-081 Shift Revision

Where location is material to a ScheduledWorkCommitment, the exact applicable Shift revision MUST identify or resolve the applicable location semantics.

A material WorkSite change therefore participates in the accepted MS-PROT-081 rule that material location/assignment changes require an appropriate Shift revision and may require renewed worker acceptance where applicable.

MS-PROT-091 does not bypass that rule.

---

# 45. Physical and Non-Physical Work

A variable-site merchant may also have:

- remote administration;
- telephone standby;
- training;
- another non-physical duty.

Main Street MUST NOT create fake coordinates or placeholder WorkSites.

A physical variable-site Shift requires its applicable WorkSite.

A non-physical Shift does not.

This distinction is admitted only where the merchant actually requires it.

---

# 46. Navigation Boundary

Main Street owns:

> **Where am I expected to work?**

External navigation software owns:

> **How do I get there?**

Where a worker-facing surface exposes WorkSite metadata, the destination may be handed to:

- Google Maps;
- Apple Maps;
- Waze;
- another supported navigation application;
- an operating-system chooser.

Main Street does not own:

- routing;
- turn-by-turn navigation;
- traffic;
- navigation history;
- travel-time optimisation.

---

# 47. Provider Independence

A provider URL or provider identifier MUST NOT become canonical WorkSite identity.

Canonical location state must survive provider replacement or failure.

Provider-specific links are derived integration/presentation material only.

---

# 48. Availability Boundary

A Shift or ScheduledWorkCommitment does not establish customer bookability.

Example:

```text
worker scheduled
09:00–17:00
```

does not mean:

```text
customer may book
09:00–17:00
```

Customer-facing availability may additionally depend on:

- qualifications;
- explicit workforce-to-Resource affinity;
- existing commitments;
- buffers;
- merchant hours;
- resources;
- capacity;
- manual blocks;
- service requirements;
- other authoritative evidence.

Canonical path:

```text
MS-PROT-091 rota evidence
        ↓
MS-PROT-081 workforce scheduling/availability authority
        ↓
MS-PROT-042
```

Direct:

```text
MS-PROT-091 → MS-PROT-042
```

interpretation is prohibited.

---

# 49. Downstream Failure Isolation

Failure of:

- customer-facing availability projection;
- Appointment projection;
- Notification;
- external navigation provider;

must not erase or corrupt valid rota/claim/commitment facts.

A downstream consumer unable to establish authoritative workforce availability must fail closed rather than infer it from rota state.

---

# 50. Permissions

Workforce membership does not grant scheduler authority.

An eligible worker may, where the merchant's admitted model permits:

- observe own scheduling information;
- observe eligible targeted Shift Offers under 081;
- make an OPEN_SELECTION ShiftClaim;
- withdraw an eligible pending claim;
- submit a permitted own ScheduleExclusion request;
- withdraw an eligible exclusion request.

An authorised scheduling actor may, where permitted:

- create/revise Shifts;
- publish rotas;
- perform direct assignment through 081;
- create targeted Shift Offers through 081;
- resolve approval-required ShiftClaims;
- resolve relevant conflicts;
- cancel Shifts;
- resolve ScheduleExclusions;
- perform authorised overrides.

Exact API/UI mechanics are not defined here.

---

# 51. Personal-Surface Boundary

MS-PROT-081 already establishes bounded Personal Workforce Self-Service while retaining:

```text
MS-PROT-081-DQ-013
Exact staff personal-surface API/UI
```

MS-PROT-091 does not resolve DQ-013.

Its worker actions are semantic capabilities only.

Exact:

- routes;
- endpoints;
- page structure;
- navigation;
- authentication transport;
- physical interaction contract;

remain subject to the owning deferred/implementation authorities.

---

# 52. Privacy

Scheduling should retain the minimum information necessary to coordinate work.

An ordinary rota may need:

```text
Unavailable 13:00–17:00
```

without exposing a private explanation.

Sensitive health information is not baseline rota data.

Private explanatory information, where lawfully held by another authority, must remain separately protected.

---

# 53. Audit Evidence

Consequential actions must retain appropriate provenance, including:

- publication;
- Shift revision;
- Shift cancellation;
- direct-assignment coordination;
- open ShiftClaim;
- claim resolution;
- reassignment;
- ScheduleExclusion decision;
- conflict override;
- WorkSite change;
- headcount change.

Evidence should preserve as applicable:

```text
actor
timestamp
workspace
affected identity
prior revision
resulting revision
reason
source mechanism
```

---

# 54. Notification Boundary

MS-PROT-091 does not resolve:

```text
MS-PROT-081-DQ-015
Exact notification contracts for shift/leave/time actions
```

Scheduling state may become input to Notification once an accepted Notification Contract authorises the delivery.

Notification failure must not roll back authoritative rota or scheduling state.

No exact shift-notification payload or delivery contract is introduced by MS-PROT-091.

---

# 55. Mobile and Role-Native Projection Requirements

Where rota semantics are projected on a worker or scheduler surface, the projection must be operable on a phone and distinguish relevant states such as:

- draft/published Shift;
- open Shift;
- pending claim;
- accepted scheduling commitment;
- cancelled Shift;
- conflict;
- ScheduleExclusion;
- WorkSite where relevant;
- stale revision.

Critical state must not rely solely on:

- hover;
- colour;
- desktop-only gestures.

This establishes projection requirements, not the exact personal-surface contract deferred under DQ-013.

---

# 56. Derived Projections

Permitted derived projections include:

```text
My shifts today
My upcoming scheduled work
weekly rota
unfilled Shifts
partially filled Shifts
open Shifts
pending ShiftClaims
rota conflicts
upcoming ScheduleExclusions
```

These projections do not become independent authority.

---

# 57. Merchant Feature Admission

Main Street's global capability breadth must not become universal merchant-visible complexity.

Therefore:

- no OPEN_SELECTION → no claim-management controls;
- no variable work sites → no WorkSite management;
- fixed workplace → no repeated address selection;
- no scheduling-location requirement → no location controls;
- no approval-required claim policy → no approval queue;
- no material ScheduleExclusion need → no unnecessary exclusion workflow.

Configuration may be inferred from merchant business reality and corrected by the merchant.

The merchant need not understand Main Street's internal capability vocabulary.

---

# 58. Configuration Inference

Example:

```text
“I run a barbershop.
All four barbers work here,
and I put them on the rota.”
```

may result in:

```text
workforce scheduling
DIRECT_ASSIGNMENT
WORKSPACE_LOCATION
```

Example:

```text
“I run a security company.
Eligible guards pick available shifts
at different client sites.”
```

may result in:

```text
workforce scheduling
OPEN_SELECTION
SHIFT_WORK_SITE
```

Main Street may infer these semantics.

The merchant must be able to correct materially incorrect inference.

---

# 59. Core Invariants

## INV-091-01 — No Duplicate Scheduling Commitment

`ScheduledWorkCommitment` under MS-PROT-081 remains the sole canonical worker-scheduled fact.

## INV-091-02 — Exact Arrangement Affinity

Every worker-specific 091 scheduling operation must identify one exact Workforce Scheduling Arrangement.

## INV-091-03 — Workspace Isolation

No rota fact acquires authority across unrelated workspaces.

## INV-091-04 — Active Arrangement

A new commitment requires an eligible ACTIVE Scheduling Arrangement.

## INV-091-05 — Claim Is Not Commitment

A pending or rejected ShiftClaim does not establish scheduled work.

## INV-091-06 — Explicit Cardinality

Required headcount is explicit.

## INV-091-07 — Concurrency Safety

Concurrent acquisition cannot exceed required headcount.

## INV-091-08 — Idempotency

Retries cannot duplicate claims or commitments.

## INV-091-09 — No Attendance Inference

Scheduled work does not prove attendance.

## INV-091-10 — No Payroll Inference

Scheduled work establishes no wage/payroll consequence.

## INV-091-11 — No Customer-Bookability Inference

Rota state cannot directly create customer availability.

## INV-091-12 — MS-PROT-081 Availability Authority Preserved

Downstream scheduling availability flows through 081.

## INV-091-13 — Conflict Visibility

Conflicts cannot be silently overwritten.

## INV-091-14 — Cross-Arrangement Policy Ownership Preservation

MS-PROT-091 does not own, infer or override the MS-PROT-081 v1.2 cross-Arrangement overlap policy or Membership-wide `MinimumInterCommitmentBuffer` policy.

## INV-091-15 — Explicit Override

Any permitted override is explicit and auditable.

## INV-091-16 — Leave Authority Preserved

ScheduleExclusion cannot replace MS-PROT-081 Leave.

## INV-091-17 — Published History Preservation

Consequential rota history is retained.

## INV-091-18 — Reassignment Provenance

Worker replacement preserves old and new commitment identities.

## INV-091-19 — Privacy Minimisation

Operational unavailability need not reveal sensitive reason.

## INV-091-20 — Conditional Location Admission

Location capability is absent where irrelevant.

## INV-091-21 — No Fixed-Site Repetition

Fixed workplace location is not repeatedly configured per Shift.

## INV-091-22 — Canonical Destination Independence

WorkSite identity does not depend on a navigation provider.

## INV-091-23 — No Fictitious Location

Non-physical work does not require fake destination data.

## INV-091-24 — Acquisition-Method Independence

Once scheduled, direct assignment, targeted offer and open claim all converge on the same 081 ScheduledWorkCommitment authority.

## INV-091-25 — No Marketplace Expansion

OPEN_SELECTION remains intra-workspace workforce scheduling.

## INV-091-26 — Deferred Personal-Surface Preservation

091 does not resolve MS-PROT-081-DQ-013.

## INV-091-27 — Deferred Notification Preservation

091 does not resolve MS-PROT-081-DQ-015.

---

# 60. Formal Ambiguity Review

## A-01 — Does 091 create a second worker-assignment aggregate?

**No.** ScheduledWorkCommitment remains canonical.

**RESOLVED.**

## A-02 — Is Merchant Membership sufficient affinity?

**No.** Exact Workforce Scheduling Arrangement is mandatory.

**RESOLVED.**

## A-03 — Is OPEN_SELECTION the same as MS-PROT-081 ShiftOffer?

**No.** ShiftOffer is targeted to an exact Arrangement; ShiftClaim is worker-initiated selection of an eligible open Shift.

**RESOLVED.**

## A-04 — What does accepted open claim create?

An MS-PROT-081 ScheduledWorkCommitment.

**RESOLVED.**

## A-05 — Does a claim establish contractual acceptance generally?

No. It is a bounded scheduling operation.

**RESOLVED.**

## A-06 — Can two workers take the last slot?

No. Final commitment admission is atomic.

**RESOLVED.**

## A-07 — Can a Shift require multiple people?

Yes, through explicit required_headcount.

**RESOLVED.**

## A-08 — Are cross-Arrangement overlaps automatically prohibited?

No. Detection remains required; exact overlap handling is now governed by MS-PROT-081 v1.2 and remains outside MS-PROT-091 ownership.

**RESOLVED.**

## A-09 — Does same person imply same Scheduling Arrangement?

No.

**RESOLVED.**

## A-10 — Is ScheduleExclusion another name for Leave?

No.

**RESOLVED.**

## A-11 — Does location belong to every Shift?

No.

**RESOLVED.**

## A-12 — Must fixed-site merchants configure location repeatedly?

No.

**RESOLVED.**

## A-13 — Is provider map identity canonical?

No.

**RESOLVED.**

## A-14 — Does variable-site scheduling require in-app navigation?

No.

**RESOLVED.**

## A-15 — Does every variable-site Shift require coordinates?

No.

**RESOLVED.**

## A-16 — Does worker-picked scheduling require a different WorkSite model?

No.

**RESOLVED.**

## A-17 — Does agency-style scheduling make Main Street recruitment authority?

No.

**RESOLVED.**

## A-18 — Are all scheduling features universal merchant settings?

No.

**RESOLVED.**

## A-19 — Does 091 define the exact staff application/API?

No; DQ-013 remains.

**RESOLVED.**

## A-20 — Does 091 define exact shift notification contracts?

No; DQ-015 remains.

**RESOLVED.**

## A-21 — Does a passed Shift time prove completion?

No.

**RESOLVED.**

## A-22 — Does rota publication prove worker acknowledgement?

No.

**RESOLVED.**

---

# 61. Falsification Review

## F-01 — Directly assigned barber

Exact Scheduling Arrangement produces one 081 ScheduledWorkCommitment.

**PASS.**

## F-02 — Worker has two Arrangements with same merchant

Scheduling must choose the exact Arrangement; membership alone cannot resolve it.

**PASS.**

## F-03 — Worker selects an open Shift

ShiftClaim is created; only accepted claim creates commitment.

**PASS.**

## F-04 — Pending approval-required claim

Worker is not represented as scheduled.

**PASS.**

## F-05 — Two simultaneous claims for one slot

Only one commitment may commit.

**PASS.**

## F-06 — Four-person Shift

Four valid commitments may fill it; fifth is rejected.

**PASS.**

## F-07 — Duplicate client retry

No duplicate claim or commitment.

**PASS.**

## F-08 — Targeted offer

Existing 081 ShiftOffer/Decision remains the mechanism; 091 does not duplicate it.

**PASS.**

## F-09 — Same-Arrangement prohibited overlap

Ordinary commitment is blocked or explicit permitted override is required.

**PASS.**

## F-10 — Different Arrangements overlap

Conflict is detectable; MS-PROT-091 delegates the exact decision to the MS-PROT-081 v1.2 owner-qualified overlap policy and does not invent or duplicate it.

**PASS.**

## F-11 — Approved Leave conflicts

081 Leave authority blocks/qualifies scheduling; ScheduleExclusion is not substituted.

**PASS.**

## F-12 — Operational exclusion

Active applicable exclusion blocks ordinary commitment without creating statutory leave meaning.

**PASS.**

## F-13 — Arrangement ends

No new claim/commitment; historical evidence survives.

**PASS.**

## F-14 — Worker replaced

Old commitment history survives and new commitment gets separate identity.

**PASS.**

## F-15 — Shift time passes

No attendance or completion inference.

**PASS.**

## F-16 — Payroll reads rota

091 cannot establish payable hours or wages.

**PASS.**

## F-17 — Appointment engine reads Shift directly

Direct customer-bookability inference is prohibited.

**PASS.**

## F-18 — Single-site barber

No repetitive WorkSite selector.

**PASS.**

## F-19 — Security company with client sites

Different Shifts can resolve different WorkSites.

**PASS.**

## F-20 — Worker considers open site-based Shift

Destination can be known before claim where material.

**PASS.**

## F-21 — Remote shift at variable-site merchant

No fake WorkSite required.

**PASS.**

## F-22 — Mapping provider unavailable

Canonical WorkSite survives.

**PASS.**

## F-23 — WorkSite changes after worker acceptance

Material revision flows through 081 acceptance/revision rules.

**PASS.**

## F-24 — Notification delivery fails

Canonical scheduling state survives.

**PASS.**

## F-25 — Availability projection fails

Booking/Appointment fails closed rather than reading 091 directly.

**PASS.**

## F-26 — Merchant never uses open shifts

No ShiftClaim settings/workflow is exposed.

**PASS.**

## F-27 — Merchant never uses variable locations

No WorkSite administration is exposed.

**PASS.**

## F-28 — Ordinary worker uses phone

Semantic state can be projected role-natively without requiring internal architecture knowledge.

**PASS.**

## F-29 — Exact personal API is needed during implementation

DQ-013 prevents implementation from inventing it.

**PASS.**

## F-30 — Exact notification payload is needed during implementation

DQ-015 prevents implementation from inventing it.

**PASS.**

---

# 62. Fundamental Vision Conformance

**Result: VISION-CONFORMING**

The proposal:

- represents genuine target-merchant workforce operations;
- absorbs complexity internally;
- avoids universal configuration;
- supports progressive capability admission;
- preserves business-language interaction;
- avoids business-type hardcoding;
- keeps staff interaction role-native;
- prevents Main Street from expanding into HRIS/ERP scope.

---

# 63. Authority Review

**PASS**

The revised authority no longer duplicates MS-PROT-081 ScheduledWorkCommitment, ShiftOffer or ShiftOfferDecision.

---

# 64. Persistence Review

**PASS**

Canonical identities and revision/provenance boundaries are explicit.

---

# 65. Failure / Recovery Review

**PASS**

Concurrency, idempotency, stale writes, failure isolation and owner-qualified cross-Arrangement policy handling are explicit.

---

# 66. Permissions / Privacy Review

**PASS**

Scheduling authority is separate from mere workforce participation; sensitive reasons are not baseline rota facts.

---

# 67. Deferred-Authority Review

**PASS**

The authority explicitly preserves:

```text
MS-PROT-081-DQ-013
Exact staff personal-surface API/UI

MS-PROT-081-DQ-015
Exact notification contracts
```

`MS-PROT-081-DQ-021` is resolved by accepted MS-PROT-081 v1.2 and is not reopened or re-owned by MS-PROT-091.

---

# 68. Recommendation

**RECOMMENDATION: ACCEPT**

The revised authority preserves the business intent of the previously reviewed MS-PROT-091 while removing the material corpus conflicts discovered during formalisation.

The decisive correction is:

```text
Rota / open-shift coordination
        → MS-PROT-091

actual worker scheduling commitment
        → MS-PROT-081 ScheduledWorkCommitment
```

rather than maintaining two competing assignment truths.

---

# 69. Approval and Implementation Boundary

This complete revised authority received explicit manual approval on 9 September 2026.

Approval authorises repository formalisation and governance-navigation consequences.

Approval does **not** activate implementation.

Implementation remains governed separately by `designs/IMPLEMENTATION-RULES.md` and the applicable implementation programme/readiness authorities.

---

# 70. Final Authority Statement

> **MS-PROT-091 establishes Main Street's canonical rota-composition, open-shift claim, cardinality, operational scheduling-exclusion and conditional work-site semantics while preserving composite MS-PROT-081 as the sole authority for Workforce Scheduling Arrangements, targeted Shift Offers, Scheduled Work Commitments, timekeeping, Leave and workforce availability. Every worker-specific rota operation is exact-Arrangement-affined; open worker selection converges atomically on an MS-PROT-081 ScheduledWorkCommitment; cross-Arrangement overlap and merchant-owned sequential inter-commitment buffer semantics are governed by MS-PROT-081 v1.2 rather than MS-PROT-091; and rota state cannot be misinterpreted as attendance, payroll, employment entitlement or customer-facing bookability.**
