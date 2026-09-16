# MS-PROT-091 v1.1 — Workforce Rota Commercial Access Contracts Amendment

**Document ID:** MS-PROT-091  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 15 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Workforce Rota commercial-access classification amendment  
**Design node:** Commercial-access prerequisite for `MS-PROT-056-V17-DQ-001`; no prior MS-PROT-091 commercial DQ identifier exists  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** MS-PROT-091 v1.0 within commercial-access classification only  
**Depends on:** Composite MS-PROT-056 through v1.9; composite MS-PROT-062; composite MS-PROT-074; composite MS-PROT-081 through v1.4; MS-PROT-091 v1.0  
**Preserves:** MS-PROT-081 ownership of Workforce Scheduling Arrangement, ShiftOffer and ScheduledWorkCommitment; MS-PROT-080 compensation ownership; MS-PROT-075 Notification ownership  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

**Product identity note:** inherited `Main Street` references identify the product currently named **GrandRue**. Stable `MS-*` and `mainstreet.*` identifiers remain unchanged pending separately governed migration.

---

# 1. Governing Decision

MS-PROT-091 SHALL define exactly three initially protected commercial purposes:

```text
PLAN_WORKFORCE_SCHEDULE

PARTICIPATE_IN_OPEN_ROTA_SELECTION

DECLARE_OPERATIONAL_SCHEDULING_UNAVAILABILITY
```

`PLAN_WORKFORCE_SCHEDULE` deliberately reuses the accepted MS-PROT-081 commercial-purpose vocabulary because rota composition and direct workforce planning belong to the same commercial operating value.

Reuse of the purpose does **not** collapse the owner-qualified targets.

The following access contracts SHALL govern the MS-PROT-091 portfolio.

| Access contract | Commercial requirement | Standard allocation |
|---|---|---|
| `workforce-rota/authoring-access@1` | `PLAN_WORKFORCE_SCHEDULE` | BUSINESS + GROWTH |
| `workforce-rota/open-selection-participation-access@1` | `PARTICIPATE_IN_OPEN_ROTA_SELECTION` | BUSINESS + GROWTH |
| `workforce-rota/schedule-exclusion-origination-access@1` | `DECLARE_OPERATIONAL_SCHEDULING_UNAVAILABILITY` | BUSINESS + GROWTH |
| `workforce-rota/merchant-observation-access@1` | No Commercial Entitlement | bounded exemption |
| `workforce-rota/personal-observation-access@1` | No Commercial Entitlement | bounded exemption |
| `workforce-rota/existing-rota-resolution-access@1` | No Commercial Entitlement | bounded residual access |
| `workforce-rota/existing-open-selection-resolution-access@1` | No Commercial Entitlement | bounded residual access |
| `workforce-rota/existing-schedule-exclusion-resolution-access@1` | No Commercial Entitlement | bounded residual access |

Protected bindings use:

```text
OPERATION_ACCESS
```

FREE SHALL receive none of the three protected purposes through the standard plan catalogue.

This amendment establishes owner classifications only.

It does not mint concrete `CommercialEntitlementIdentity` values.

---

# 2. Ownership Boundary

MS-PROT-091 continues to own:

```text
RotaPeriod

Shift work requirement

required Shift headcount

rota publication/revision

OPEN_SELECTION eligibility

ShiftClaim

open-selection concurrency

ScheduleExclusion

conditional WorkSite

rota location configuration

rota conflicts

rota coordination provenance

reassignment coordination
```

MS-PROT-081 continues to own:

```text
WorkforceSchedulingArrangement

WorkforceTimeTermsRevision

targeted ShiftOffer

ShiftOfferDecision

ScheduledWorkCommitment

ScheduledBreak

Timekeeping

Leave
```

Commercial owns only:

```text
entitlement identity

binding

grant provenance

effective Commercial permission

plan grant sets

catalogue publication
```

An MS-PROT-091 commercial contract SHALL NOT become a second scheduling authority.

---

# 3. No Rota Wildcard

Rejected:

```text
BUSINESS
    → everything under MS-PROT-091
```

Rejected:

```text
workforce-rota/*
```

Rejected:

```text
hasBusinessPlan()
    → allowRotaOperation()
```

Canonical:

```text
exact owner-qualified
MS-PROT-091 access contract
        ↓
exact protected purpose
or explicit exemption
        ↓
Commercial Access Binding
        ↓
independent runtime predicates
```

A BUSINESS plan label never determines:

```text
Shift eligibility

Arrangement eligibility

headcount

worker authority

overlap safety

ScheduleExclusion applicability

WorkSite validity

ScheduledWorkCommitment validity
```

---

# 4. Rota Authoring

## 4.1 Contract

```text
workforce-rota/authoring-access@1
```

requires:

```text
PLAN_WORKFORCE_SCHEDULE
```

## 4.2 Protected scope

Where otherwise admitted by MS-PROT-091, the contract includes:

```text
creating a RotaPeriod

creating a Shift requirement

materially revising a Shift

changing required headcount

changing allocation mode

publishing a rota

materially revising a published rota

establishing/changing applicable
rota location configuration

creating or materially changing
a WorkSite where required

material WorkSite reassignment
for future Shift requirements
```

These operations establish or materially alter future workforce-planning requirements.

## 4.3 Publication

Rota publication remains protected because publication converts planning state into a stable operational rota revision.

Publication does not itself schedule a person.

The commercial requirement therefore does not alter:

```text
Shift
    ≠
ScheduledWorkCommitment
```

---

# 5. Material Revision Versus Existing-State Resolution

The following distinction is mandatory.

```text
materially change future work
        → protected authoring

remove / close / restrict
existing rota state
        → potentially residual resolution
```

Examples requiring protected authoring:

```text
09:00–17:00
    →
13:00–21:00

required_headcount 2
    →
required_headcount 5

WORKSPACE_LOCATION
    →
new SHIFT_WORK_SITE

DIRECT_ASSIGNMENT
    →
OPEN_SELECTION
```

Commercial downgrade MUST NOT turn:

```text
existing Shift identity
```

into perpetual permission to redesign future work.

---

# 6. Existing Rota Resolution

## 6.1 Contract

```text
workforce-rota/existing-rota-resolution-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

within its bounded scope.

## 6.2 Permitted scope

Where already valid under MS-PROT-091, this may include:

```text
cancelling an existing Shift

closing an existing RotaPeriod

discarding a still
non-consequential DRAFT Shift

recovering an already committed
rota mutation result
```

The contract exists so commercial loss does not force merchants to retain unwanted future work requirements.

## 6.3 Cancellation

Cancellation of a published Shift preserves the history already required by MS-PROT-091.

Any downstream ScheduledWorkCommitment consequence still passes through MS-PROT-081.

The residual Rota contract does not acquire authority to cancel or rewrite the MS-PROT-081 commitment directly.

## 6.4 Exclusion

The residual contract does not permit:

```text
material rescheduling

new Shift creation

additional headcount

new WorkSite assignment

new allocation mode

reassignment to another worker

publication of new rota content
```

---

# 7. DIRECT_ASSIGNMENT and TARGETED_OFFER

MS-PROT-091 coordinates these modes but does not own their worker-commitment semantics.

Therefore:

```text
rota authoring / Shift requirement
    → workforce-rota/authoring-access@1

direct worker assignment
    → applicable MS-PROT-081
      protected access contract

targeted ShiftOffer
    → applicable MS-PROT-081
      protected access contract
```

Where one user operation performs both:

```text
create/revise rota requirement
+
create worker commitment
```

all applicable owner-qualified Commercial requirements must be satisfied.

No single Rota entitlement may wildcard-cover the MS-PROT-081 operation.

---

# 8. Open-Selection Participation

## 8.1 Contract

```text
workforce-rota/open-selection-participation-access@1
```

requires:

```text
PARTICIPATE_IN_OPEN_ROTA_SELECTION
```

## 8.2 Worker claim

Creating a new ShiftClaim against an OPEN_SELECTION Shift requires current commercial permission for the Merchant Scope.

The worker does not require a personal subscription.

The worker still requires:

```text
eligible Membership

exact ACTIVE
WorkforceSchedulingArrangement

current Workforce Time Terms

current Shift revision

remaining headcount

actor authority

all applicable conflicts /
exclusions / buffers
```

## 8.3 AUTO_CONFIRM

Where:

```text
AUTO_CONFIRM
```

applies, the worker's commercial check occurs before the atomic transaction that establishes:

```text
ShiftClaim = ACCEPTED
+
ScheduledWorkCommitment
```

An old view of an open Shift is not durable commercial authority.

## 8.4 APPROVAL_REQUIRED

Creating the PENDING ShiftClaim uses the protected open-selection service.

Subsequent:

```text
ACCEPT
```

also requires current:

```text
PARTICIPATE_IN_OPEN_ROTA_SELECTION
```

because acceptance creates a new ScheduledWorkCommitment.

The fact that the claim was created while BUSINESS existed does not create perpetual authority to accept it after entitlement loss.

---

# 9. Existing Open-Selection Resolution

## 9.1 Contract

```text
workforce-rota/existing-open-selection-resolution-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for bounded negative resolution of an existing claim.

## 9.2 Permitted operations

Where already valid, the contract may cover:

```text
worker withdrawal
of PENDING claim

scheduler REJECT
of PENDING claim

passive expiry
becoming effective

committed-result recovery
```

These operations do not create a new ScheduledWorkCommitment.

## 9.3 Acceptance explicitly excluded

```text
ACCEPT
```

is not residual cleanup.

It creates scheduled work.

It therefore remains under:

```text
open-selection-participation-access@1
```

---

# 10. Reassignment

MS-PROT-091 reassignment preserves old and new commitment identity.

Where reassignment requires:

```text
release old ScheduledWorkCommitment
+
establish a new worker commitment
```

the commercial composition is likewise asymmetric.

The old commitment may be resolved through the applicable MS-PROT-081 residual contract.

The new assignment still requires every applicable current protected permission.

For an MS-PROT-091-coordinated rota reassignment, this includes:

```text
PLAN_WORKFORCE_SCHEDULE
```

where the rota itself is materially revised, plus the applicable owner-qualified scheduling permission for creation of the new commitment.

Rejected:

```text
reassignment
    = residual cleanup
```

merely because an earlier worker had already been assigned.

---

# 11. ScheduleExclusion Origination

## 11.1 Contract

```text
workforce-rota/schedule-exclusion-origination-access@1
```

requires:

```text
DECLARE_OPERATIONAL_SCHEDULING_UNAVAILABILITY
```

for creation of a new ScheduleExclusion request/fact where existing MS-PROT-091 semantics permit it.

## 11.2 Meaning

A ScheduleExclusion represents operational scheduling unavailability.

It remains distinct from:

```text
LeaveRequest

ApprovedLeaveEvidence

statutory leave
```

Commercial permission does not alter that distinction.

## 11.3 Worker commercial relationship

The worker does not purchase a plan.

The required Commercial permission is evaluated for the merchant service.

The worker still requires the exact:

```text
Membership

Scheduling Arrangement

personal context

operation authority
```

required by the source authority.

---

# 12. Existing ScheduleExclusion Resolution

## 12.1 Contract

```text
workforce-rota/existing-schedule-exclusion-resolution-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for bounded resolution of an already-existing ScheduleExclusion.

Where existing semantics permit, this may include:

```text
APPROVE / activate
an existing REQUESTED exclusion

DECLINE

withdraw

passive expiry

recover committed result
```

## 12.2 Reason

These operations restrict, resolve or clear an existing operational-unavailability fact.

They do not themselves establish a new work commitment.

A pending exclusion request must not be stranded merely because paid Rota access ended.

## 12.3 No new exclusion through resolution

The contract MUST NOT be used to:

```text
change the interval materially

change the Arrangement

replace the reason/source
with materially new meaning

create a new exclusion
```

A materially different exclusion is new origination.

---

# 13. Withdrawal of ACTIVE ScheduleExclusion

Withdrawal of an existing ACTIVE ScheduleExclusion may remove a scheduling restriction.

It nevertheless requires no independent Commercial Entitlement because withdrawal itself does not create a Shift or ScheduledWorkCommitment.

Any subsequent new scheduling operation still requires its applicable current protected Commercial permission.

Thus:

```text
withdraw exclusion
    ≠
permission to schedule
```

---

# 14. Merchant Observation

## 14.1 Contract

```text
workforce-rota/merchant-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for otherwise-authorised observation of retained MS-PROT-091 information.

Potential scope includes:

```text
RotaPeriods

Shift requirements

headcount

allocation modes

ShiftClaims

ScheduleExclusions

WorkSites

rota revisions

conflicts

coordination provenance

derived fill-state projections
```

Observation does not grant mutation.

Commercial downgrade MUST NOT itself make legitimate historical Rota records inaccessible to the merchant.

---

# 15. Personal Observation

## 15.1 Contract

```text
workforce-rota/personal-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for otherwise-authorised worker observation of their own permitted Rota material.

Potential scope includes:

```text
eligible open Shifts

own ShiftClaims

own relevant ScheduleExclusions

own applicable WorkSite details

own Rota-related
scheduling projections
```

The existing MS-PROT-081 personal-surface boundary remains controlling.

The exemption creates no merchant-operational authority.

---

# 16. Worker Has No Personal Subscription Requirement

Neither:

```text
PARTICIPATE_IN_OPEN_ROTA_SELECTION
```

nor:

```text
DECLARE_OPERATIONAL_SCHEDULING_UNAVAILABILITY
```

means that the worker buys or possesses a personal subscription.

Commercial permission is merchant-scoped.

Worker authority remains relationship- and operation-scoped.

---

# 17. Configuration

MS-PROT-091 admits merchant-conditioned configuration such as:

```text
DIRECT_ASSIGNMENT

TARGETED_OFFER

OPEN_SELECTION

AUTO_CONFIRM

APPROVAL_REQUIRED

NO_SHIFT_LOCATION

WORKSPACE_LOCATION

SHIFT_WORK_SITE
```

Establishing or materially changing a configuration that changes future Rota operation falls under:

```text
workforce-rota/authoring-access@1
→ PLAN_WORKFORCE_SCHEDULE
```

Configuration inference itself does not make the result authoritative.

Merchant approval and existing Configuration authority remain independently applicable where required.

Commercial permission does not activate irrelevant configuration for a merchant.

---

# 18. WorkSite Boundary

Where conditional WorkSite semantics are admitted:

```text
WorkSite authoring / material revision
    → PLAN_WORKFORCE_SCHEDULE
```

Observation of existing WorkSite information through an otherwise-authorised Rota view requires no Commercial Entitlement.

Navigation remains external-provider responsibility.

Commercial access does not create:

```text
routing

traffic data

navigation history

provider identity as canonical WorkSite
```

No separate premium tier is created for variable-site scheduling.

---

# 19. Conflict Override

A scheduling conflict override is not automatically residual merely because the conflicting objects already exist.

Where an override participates in creation or material revision of future work:

```text
current protected Commercial
permission remains required
```

along with:

```text
explicit actor authority

reason

audit evidence

applicable MS-PROT-081
overlap/buffer policy
```

A no-entitlement resolution contract MUST NOT become a backdoor for new commitments.

---

# 20. Commercial Loss

When all valid grants for the protected Rota purposes end:

## New protected use is blocked

```text
new RotaPeriod

new Shift

material Shift revision

new rota publication

material published-rota revision

new/mutated WorkSite for future Shift

new OPEN_SELECTION ShiftClaim

acceptance of PENDING ShiftClaim

new ScheduleExclusion
```

## Existing bounded state may still be observed/resolved

```text
observe existing rota history

cancel existing Shift

close existing RotaPeriod

discard non-consequential draft
where owner semantics permit

withdraw/reject existing ShiftClaim

resolve existing ScheduleExclusion

observe personal Rota information

recover exact previously committed result
```

Commercial loss does not mutate the source facts automatically.

---

# 21. No Revival

Later re-entitlement does not revive:

```text
cancelled Shift

closed RotaPeriod

rejected ShiftClaim

withdrawn ShiftClaim

expired ShiftClaim

declined ScheduleExclusion

withdrawn ScheduleExclusion

expired ScheduleExclusion
```

Commercial permission permits otherwise-valid new activity.

It does not rewrite history.

---

# 22. Notification Boundary

Rota state may require supporting communication.

Notification remains MS-PROT-075-owned.

A necessary Notification required to complete an included BUSINESS Rota workflow MUST NOT become a hidden GROWTH requirement.

Notification failure does not invalidate:

```text
RotaPeriod

Shift

ShiftClaim

ScheduleExclusion

ScheduledWorkCommitment
```

where those source facts were otherwise validly established.

This amendment creates no Notification Contract.

---

# 23. Compensation Boundary

Rota commercial permission establishes no:

```text
wage

pay rate

payable time

Compensation Relationship

Payroll participation

payment obligation
```

MS-PROT-080 remains the Compensation owner.

The fact that a Shift or claim ultimately leads to compensated work does not cause the Rota entitlement to grant Compensation administration.

---

# 24. Availability Boundary

Commercial permission to operate Rota does not make its state customer-bookable.

The existing authoritative path remains:

```text
MS-PROT-091
        ↓
MS-PROT-081
workforce availability
        ↓
customer Scheduling authority
```

No:

```text
BUSINESS entitlement
        ↓
rota Shift
        ↓
customer availability
```

shortcut is introduced.

---

# 25. Standard Tier Allocation

The three protected purposes belong to BUSINESS:

```text
PLAN_WORKFORCE_SCHEDULE

PARTICIPATE_IN_OPEN_ROTA_SELECTION

DECLARE_OPERATIONAL_SCHEDULING_UNAVAILABILITY
```

GROWTH explicitly includes them.

FREE does not.

Canonical:

```text
FREE
    no protected Rota operation

BUSINESS
    exact Rota operating purposes

GROWTH
    same Rota purposes
    + independently governed
      Growth services
```

The purpose:

```text
PLAN_WORKFORCE_SCHEDULE
```

is shared conceptually with MS-PROT-081 commercial classification but each exact owner-qualified target remains independently bound.

A concrete entitlement for:

```text
workforce-scheduling/work-planning-authoring-access@1
```

does not satisfy:

```text
workforce-rota/authoring-access@1
```

unless the final Commercial catalogue explicitly and lawfully establishes the corresponding separate bindings/grants.

No namespace wildcard is permitted.

---

# 26. Supporting-Service Composition

The final catalogue must recognise cross-owner workflows.

Examples:

## Direct assignment from a Rota Shift

Potential commercial composition:

```text
MS-PROT-091
PLAN_WORKFORCE_SCHEDULE

+

MS-PROT-081
applicable new-commitment
commercial permission
```

## Targeted offer from Rota coordination

```text
MS-PROT-091
PLAN_WORKFORCE_SCHEDULE

+

MS-PROT-081
targeted-offer / acceptance
commercial permissions
```

according to the exact operation.

## Open selection

The MS-PROT-091 open-selection operation owns its commercial classification.

The fact that an accepted claim atomically produces an MS-PROT-081 ScheduledWorkCommitment does not permit Commercial to substitute an unrelated targeted-offer entitlement.

The source-operation boundary remains authoritative.

---

# 27. Trial and Independent Grant Sources

This amendment changes no accepted grant-source semantics.

The eventual Rota entitlement identities may be satisfied by independently valid accepted sources such as:

```text
full-experience trial

paid Merchant Commercial Agreement

commercial remediation

other accepted source
```

Standing FREE does not receive protected Rota purposes through the standard catalogue.

Expiration of one grant source does not erase source facts already established.

---

# 28. Quantitative Packaging Boundary

This amendment introduces no:

```text
per-worker charge

per-Shift charge

per-RotaPeriod charge

per-WorkSite charge

open-claim quota

headcount quota

location surcharge

"unlimited rota" claim
```

Those remain under the applicable future commercial-feasibility/pricing authority, including `MS-PROT-056-V17-DQ-003` where applicable.

Multiple WorkSites or larger required headcount do not by themselves redefine semantic Commercial classification.

---

# 29. Actor and Trust Boundary

Commercial permission remains only one runtime predicate.

Examples:

```text
BUSINESS
+
ordinary worker
    ≠ scheduler authority
```

```text
BUSINESS
+
invalid Scheduling Arrangement
    ≠ valid ShiftClaim
```

```text
no current BUSINESS grant
+
existing PENDING ShiftClaim
    → worker may withdraw
    → scheduler may reject
    → scheduler may not ACCEPT
      solely through residual access
```

```text
no current BUSINESS grant
+
existing Shift
    → may cancel if otherwise authorised
    → may not materially redesign
      through residual access
```

---

# 30. Data, Privacy and Exposure

No-entitlement observation or resolution does not mean unrestricted data access.

Every applicable:

```text
Merchant Scope

Membership

Arrangement affinity

Actor Authorisation

privacy rule

Exposure

retention policy

account restriction

resource-protection rule
```

remains independently binding.

Observation does not imply export.

Commercial loss does not imply deletion.

Retention does not imply access.

---

# 31. AI Boundary

AI MAY assist only within separately accepted authority.

AI SHALL NOT:

```text
invent a Commercial grant

choose a protected purpose

create a Shift autonomously

accept a ShiftClaim autonomously
without accepted authority

reinterpret Leave as ScheduleExclusion

convert residual cleanup
into new scheduling activity

fabricate WorkSite data

override headcount or overlap rules
```

Deterministic Rota operation must not depend on live AI where current authority provides a non-AI path.

---

# 32. Falsification

## F1 — Single-site barber creates weekly rota

Expected:

```text
new RotaPeriod / Shifts
→ PLAN_WORKFORCE_SCHEDULE
```

The merchant is not forced to configure WorkSites.

**PASS**

## F2 — Merchant directly assigns worker

Expected:

Rota planning permission does not replace the applicable MS-PROT-081 commitment permission.

**PASS**

## F3 — Merchant uses targeted offer

Expected:

MS-PROT-081 retains ShiftOffer commercial classification.

MS-PROT-091 does not absorb it.

**PASS**

## F4 — Security worker claims open Shift

Expected:

```text
PARTICIPATE_IN_OPEN_ROTA_SELECTION
```

required for the merchant scope.

Worker needs no personal subscription.

**PASS**

## F5 — AUTO_CONFIRM claim after downgrade

Worker saw Shift while BUSINESS existed but acts after the last grant ended.

Expected:

claim/commit transaction is commercially denied.

Old visibility is not durable permission.

**PASS**

## F6 — Pending claim after downgrade

Expected:

```text
withdraw → allowed
reject   → allowed
accept   → protected / denied
```

assuming no independent valid grant source.

**PASS**

## F7 — Existing published Shift is cancelled after downgrade

Expected:

cancellation remains available where otherwise authorised.

History is preserved.

Affected worker commitments resolve through MS-PROT-081.

**PASS**

## F8 — Existing Shift moved after downgrade

Merchant changes Monday 09:00 to Tuesday 13:00.

Expected:

material revision requires current:

```text
PLAN_WORKFORCE_SCHEDULE
```

**PASS**

## F9 — Required headcount increased after downgrade

Expected:

protected new rota planning.

Not residual.

**PASS**

## F10 — RotaPeriod closed after downgrade

Expected:

bounded existing-state resolution may remain available.

**PASS**

## F11 — Worker submits new operational exclusion on FREE

Expected:

protected purpose required.

The absence is not silently converted to Leave.

**PASS**

## F12 — Pending exclusion after downgrade

Expected:

otherwise-authorised approve/decline/withdraw resolution remains possible.

No new exclusion is manufactured.

**PASS**

## F13 — ACTIVE exclusion withdrawn after downgrade

Expected:

withdrawal may occur.

It does not itself authorise a new Shift or commitment.

**PASS**

## F14 — Variable-site merchant changes WorkSite

Expected:

material future Rota change requires protected authoring.

**PASS**

## F15 — Merchant only observes historical rota

Expected:

no Commercial Entitlement required, subject to ordinary access predicates.

**PASS**

## F16 — Worker views own ShiftClaim history

Expected:

no personal subscription and no independent Commercial Entitlement.

**PASS**

## F17 — Reassignment

Expected:

release of old commitment may be residual.

Creation of replacement work remains protected.

**PASS**

## F18 — Concurrent final open-shift slot

Expected:

commercial permission does not weaken atomic headcount enforcement.

Only one valid commitment may consume the final slot.

**PASS**

## F19 — Payroll consumer reads Rota

Expected:

Rota entitlement does not create Compensation authority.

**PASS**

## F20 — Appointment engine reads Rota directly

Expected:

still prohibited.

Commercial access changes nothing about customer bookability.

**PASS**

## F21 — Merchant never uses OPEN_SELECTION

Expected:

no open-selection controls need be surfaced despite BUSINESS entitlement.

Commercial packaging does not activate irrelevant semantics.

**PASS**

## F22 — Merchant never uses variable WorkSites

Expected:

no WorkSite administration burden.

**PASS**

## F23 — AI unavailable

Expected:

ordinary deterministic Rota behaviour survives.

**PASS**

## F24 — Low-software-capacity merchant

Merchant sees:

```text
Create rota
Add shift
Publish
Cancel shift
```

not:

```text
CommercialEntitlementIdentity
owner-qualified target
grant-source graph
```

**PASS**

---

# 33. Alternatives

## One `WORKFORCE` entitlement

**REJECTED**

Would improperly combine:

```text
MS-PROT-081 Scheduling

MS-PROT-091 Rota

MS-PROT-080 Compensation
```

under one wildcard permission.

## Treat all MS-PROT-091 mutations as one protected contract

**REJECTED**

Would make cancellation, rejection and historical cleanup depend unnecessarily on paid access.

## Let MS-PROT-081 v1.4 implicitly cover MS-PROT-091

**REJECTED**

Would violate owner-qualified commercial classification.

## Make all worker Rota actions free because worker does not own subscription

**REJECTED**

The commercial predicate belongs to the merchant service, not the individual worker.

## Accept pending open claims after downgrade

**REJECTED**

Acceptance creates a new ScheduledWorkCommitment.

## Require paid entitlement merely to reject/withdraw a claim

**REJECTED**

Would strand pending state and create artificial commercial lock-in.

## Treat ScheduleExclusion as Leave

**REJECTED**

Directly contradicts MS-PROT-091 ownership boundaries.

---

# 34. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

Internal commercial precision increases.

Merchant administration does not.

The model allows GrandRue to present:

```text
Business includes staff rota
```

while internally preserving:

```text
Rota requirements

worker commitment

open selection

operational unavailability

Compensation

actor authority

commercial permission
```

as independent bounded meanings.

That complexity is necessary to avoid commercial lock-in and semantic corruption.

It is not merchant configuration.

The proposal therefore satisfies:

```text
Business-to-Software Translation

Administrative Compression

ordinary-staff simplicity

role-native operation

target-market proportionality

business evolution
```

---

# 35. Hard Invariants

1. MS-PROT-091 commercial permission never owns ScheduledWorkCommitment truth.
2. BUSINESS/GROWTH tier identity never determines Rota semantics.
3. Exactly three initial protected Rota purposes are admitted.
4. All three belong to BUSINESS and GROWTH, not standard FREE.
5. `PLAN_WORKFORCE_SCHEDULE` reuse does not collapse MS-PROT-081 and MS-PROT-091 targets.
6. New Rota/Shift planning requires protected permission.
7. Material Shift revision requires protected permission.
8. Rota publication requires protected permission.
9. New OPEN_SELECTION participation requires protected permission.
10. Acceptance of a PENDING ShiftClaim remains protected because it creates scheduled work.
11. Rejection/withdrawal of an existing claim requires no Commercial Entitlement.
12. New ScheduleExclusion origination requires protected permission.
13. Resolution of an existing ScheduleExclusion requires no Commercial Entitlement.
14. Existing Shift cancellation may remain commercially residual.
15. Residual cancellation does not authorise material rescheduling.
16. Observation of retained Rota information requires no independent Commercial Entitlement.
17. Worker personal observation requires no personal subscription.
18. Reassignment cannot use residual cleanup to authorise a new worker commitment.
19. WorkSite authoring remains conditional on merchant operating need.
20. Rota commercial access creates no Compensation meaning.
21. Rota commercial access creates no customer availability.
22. Necessary supporting services do not become hidden higher-tier barriers.
23. Exact Commercial Entitlement identities remain MS-PROT-056-owned.
24. Missing classification is never treated as exemption.
25. Acceptance of this amendment does not activate implementation.

---

# 36. MS-PROT-056-V17-DQ-001 Effect

Upon approval and formalisation, the known owner-side Workforce commercial-classification blockers identified during the DQ-001 revision become:

```text
MS-PROT-081
    commercial classification
    → resolved by v1.4

MS-PROT-080
    commercial classification
    → resolved by v1.4

MS-PROT-091
    commercial classification
    → resolved by this v1.1
```

This does not automatically prove the **entire** standard catalogue complete.

Before returning DQ-001 to `ACCEPT`, Commercial must still perform the final corpus-wide completeness audit required by the revised DQ-001 design:

```text
every v1.7/v1.8 defined-scope allocation
        ↓
exact owner-qualified
commercial classification
        ↓
exact entitlement identity
        ↓
exact binding
        ↓
explicit plan grant sets
```

Any additional unclassified target discovered by that audit remains a blocker.

---

# 37. Governance Outcome

**Feature Admission:** PASS  
**Fundamental Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Owner-qualified target separation:** PASS  
**Rota / ScheduledWorkCommitment separation:** PASS  
**Open-selection commercial boundary:** PASS  
**Residual cancellation boundary:** PASS  
**ScheduleExclusion / Leave separation:** PASS  
**Worker / merchant commercial separation:** PASS  
**Compensation boundary:** PASS  
**Customer-availability boundary:** PASS  
**Anti-wildcard review:** PASS  
**Low-software-capacity merchant falsification:** PASS  
**Ordinary-worker falsification:** PASS  
**Ambiguity review:** PASS  

**RECOMMENDATION: ACCEPT**

**Manual approval:** GRANTED — 15 September 2026  
**Repository formalisation:** AUTHORISED  
**Implementation activation:** NONE

---

# 38. Acceptance Statement

> **GrandRue may commercially protect new rota planning, open-shift participation and new operational scheduling-exclusion use without making commercial downgrade a barrier to observing or safely resolving rota state already established. Rota commercial permission remains owner-qualified: it neither replaces MS-PROT-081 ScheduledWorkCommitment authority nor expands into Compensation, customer availability or generic workforce permission. Negative resolution and restriction of existing rota state may remain commercially available, while operations that create or materially alter future work continue to require current BUSINESS-level permission.**
