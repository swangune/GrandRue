# MS-PROT-081 v1.4 — Workforce Scheduling, Timekeeping & Leave Commercial Access Contracts Amendment

**Document ID:** MS-PROT-081  
**Version:** 1.4  
**Status:** ACCEPTED  
**Approved:** 15 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Workforce Scheduling / Timekeeping / Leave commercial-access classification amendment  
**Design node:** `MS-PROT-081-DQ-020 — Commercial entitlement/tier packaging`  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-081 through v1.3 within commercial-access classification only  
**Depends on:** Composite MS-PROT-056 through v1.9; composite MS-PROT-062; composite MS-PROT-063; composite MS-PROT-074; MS-PROT-076; composite MS-PROT-081 through v1.3  
**Preserves:** MS-PROT-080 Workforce Compensation authority; MS-PROT-091 Rota ownership; composite MS-PROT-075 Notification delivery authority; all unresolved MS-PROT-081 DQs other than DQ-020  
**Resolves:** `MS-PROT-081-DQ-020` for the MS-PROT-081-owned Workforce Scheduling / Timekeeping / Leave portfolio  
**Does not resolve:** `MS-PROT-056-V17-DQ-001`; `MS-PROT-080-V11-DQ-016`; any MS-PROT-091 commercial classification  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

**Product identity note:** `Main Street` references inherited from earlier authorities refer to the product currently named **GrandRue**. Stable `MS-*` and `mainstreet.*` identifiers remain unchanged pending separately governed migration.

---

# 1. Governing Decision

Composite MS-PROT-081 SHALL define exactly the following initial owner-qualified commercial-access contracts.

## 1.1 Commercially protected contracts

| Exact access-contract identity | Protected commercial purpose | Standard allocation |
|---|---|---|
| `workforce-scheduling/arrangement-authoring-access@1` | `MAINTAIN_WORKFORCE_SCHEDULING_TERMS` | BUSINESS + GROWTH |
| `workforce-scheduling/work-planning-authoring-access@1` | `PLAN_WORKFORCE_SCHEDULE` | BUSINESS + GROWTH |
| `workforce-scheduling/shift-offer-acceptance-access@1` | `COMMIT_OFFERED_WORK` | BUSINESS + GROWTH |
| `workforce-scheduling/leave-request-origination-access@1` | `REQUEST_WORKFORCE_LEAVE` | BUSINESS + GROWTH |

The binding target family for each protected contract is:

```text
OPERATION_ACCESS
```

FREE SHALL NOT receive these protected purposes through the standard plan catalogue.

## 1.2 Explicit no-Commercial-Entitlement contracts

The following exact owner-qualified contracts require **no Commercial Entitlement** within their bounded scopes:

```text
workforce-scheduling/merchant-observation-access@1

workforce-scheduling/personal-observation-access@1

workforce-scheduling/existing-scheduled-work-resolution-access@1

workforce-scheduling/existing-shift-offer-resolution-access@1

workforce-scheduling/existing-leave-request-resolution-access@1

workforce-scheduling/arrangement-ending-access@1
```

Explicit exemption means only:

> no Commercial Entitlement is required for the exact bounded owner operation.

It does not waive:

```text
semantic validity
Merchant Scope
Membership/Arrangement affinity
Actor Authorisation
trusted execution context
account lifecycle
privacy
Exposure
jurisdictional constraints
concurrency
idempotency
source currentness
provider requirements where applicable
```

Missing classification is not an exemption.

---

# 2. Ownership Boundary

MS-PROT-081 continues to own:

```text
Workforce Scheduling Arrangement
Workforce Time Terms
Shift Offer
Scheduled Work Commitment
Scheduled Break
Time Capture Event
Time Correction Evidence
Approved Worked-Time Evidence
Leave Request
Leave Decision
Approved Leave Evidence
workforce-owned projections
```

Commercial continues to own:

```text
Commercial Entitlement identity
Commercial Access Binding
grant provenance
effective commercial permission
plan-revision grant sets
catalogue publication
```

This amendment determines which MS-PROT-081 access points require which commercial purpose.

It does not create the final Commercial Entitlement identities.

Those remain part of the complete manifest work under:

```text
MS-PROT-056-V17-DQ-001
```

---

# 3. No Tier-Owned Workforce Semantics

Rejected:

```text
if plan == BUSINESS
    enable workforce
```

Rejected:

```text
BUSINESS_WORKFORCE
    → all MS-PROT-081 operations
```

Rejected:

```text
workforce-scheduling/*
```

Canonical:

```text
exact owner access contract
        ↓
exact protected purpose
        ↓
exact Commercial Access Binding
        ↓
effective grant source
        ↓
runtime composition with all
other independent predicates
```

BUSINESS is commercial packaging.

It is not Workforce Scheduling authority.

---

# 4. Arrangement Authoring

## 4.1 Contract

```text
workforce-scheduling/arrangement-authoring-access@1
```

requires:

```text
MAINTAIN_WORKFORCE_SCHEDULING_TERMS
```

## 4.2 Bounded scope

The contract covers only existing MS-PROT-081 owner behaviour required to establish new scheduling participation or materially establish/change future scheduling rules, including where otherwise semantically valid:

```text
establishing a new
Workforce Scheduling Arrangement

establishing a new applicable
Workforce Time Terms Revision
```

A change of Compensation Relationship affinity still requires the new Arrangement semantics already governed by v1.1.

The commercial purpose does not make such a change semantically valid.

## 4.3 Exclusions

It does not grant:

```text
Merchant Membership
Compensation Relationship
Compensation Terms
Payroll
jurisdiction rules
Role Assignment
customer-facing Resource linkage
Appointment authority
Rota Shift authority
```

---

# 5. Arrangement Ending

## 5.1 Contract

```text
workforce-scheduling/arrangement-ending-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for the existing:

```text
ACTIVE → ENDED
```

Arrangement lifecycle consequence where independently valid.

## 5.2 Reason

Commercial loss MUST NOT require a merchant to retain an unwanted Workforce Scheduling Arrangement merely because permission to establish new workforce activity has ended.

Ending an Arrangement is cleanup/lifecycle restriction, not acquisition of new paid operating capacity.

## 5.3 Boundary

Ending an Arrangement does not itself:

```text
cancel existing Scheduled Work Commitments
rewrite Time Capture Events
delete history
end Merchant Membership
end a Compensation Relationship
cancel customer Appointments
```

Any unresolved active-work consequence remains governed by the applicable owner authority.

This amendment does not invent one.

---

# 6. Work Planning Authoring

## 6.1 Contract

```text
workforce-scheduling/work-planning-authoring-access@1
```

requires:

```text
PLAN_WORKFORCE_SCHEDULE
```

## 6.2 Bounded scope

The contract covers, where otherwise valid under composite MS-PROT-081:

```text
issuing a targeted Shift Offer

directly establishing a new
Scheduled Work Commitment

materially revising an existing
Scheduled Work Commitment

materially revising/replacing
a Shift Offer where owner semantics permit
```

These operations create or materially change future work expectation.

They therefore require current commercial permission for new workforce operation.

---

# 7. Material Revision Is Not Residual Cleanup

A material shift revision may change:

```text
work date
start time
end time
material location/assignment
Scheduled Break structure
another acceptance-material term
```

It may therefore create a materially different future obligation.

Consequently:

```text
existing Scheduled Work Commitment
+
commercial grant ended
```

does not automatically authorise:

```text
materially reschedule the work
```

under the no-entitlement resolution contract.

Canonical:

```text
CANCEL / RELEASE exact existing commitment
    → existing-scheduled-work-resolution-access@1

MATERIALLY REVISE existing commitment
    → work-planning-authoring-access@1
    → PLAN_WORKFORCE_SCHEDULE required
```

This supplies the owner-specific classification required by MS-PROT-056 v1.5 for an otherwise ambiguous modification operation.

---

# 8. Shift Offer Acceptance

## 8.1 Contract

```text
workforce-scheduling/shift-offer-acceptance-access@1
```

requires:

```text
COMMIT_OFFERED_WORK
```

## 8.2 Meaning

It covers exactly an authorised worker:

```text
ACCEPT
```

of one exact currently actionable Shift Offer.

Acceptance establishes a Scheduled Work Commitment.

A Shift Offer is explicitly not itself that commitment.

Therefore an offer issued while BUSINESS permission existed does not create perpetual future commercial authority to accept it after that permission ends.

## 8.3 Merchant commercial permission

The commercial permission is evaluated for the Merchant Scope.

The worker does not need a separate personal GrandRue subscription.

The worker still requires every applicable:

```text
Membership
Arrangement
personal trusted context
offer eligibility
actor authority
current offer
concurrency
```

condition.

---

# 9. Existing Shift Offer Resolution

## 9.1 Contract

```text
workforce-scheduling/existing-shift-offer-resolution-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

## 9.2 Bounded scope

The contract covers:

```text
REJECT
```

of an exact existing Shift Offer by its authorised target and, where already established by accepted owner semantics, explicit source-owned actions that make an existing offer non-actionable without accepting it.

Examples may include an already-governed:

```text
withdrawal
cancellation
supersession
```

The contract does not create those lifecycle mechanisms.

Passive owner-defined expiry likewise does not require a commercial grant merely to become true.

## 9.3 Exclusion

It does not cover:

```text
ACCEPT
```

Acceptance remains governed by Section 8.

---

# 10. Existing Scheduled Work Resolution

## 10.1 Contract

```text
workforce-scheduling/existing-scheduled-work-resolution-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for the bounded resolution and evidence operations below.

## 10.2 Release / cancellation

An otherwise-authorised merchant may explicitly release/cancel an existing Scheduled Work Commitment without current BUSINESS entitlement.

Commercial loss MUST NOT force the merchant to keep a future work commitment active.

Cancellation preserves history under existing MS-PROT-081 authority.

## 10.3 Time capture for committed work

Where one exact Scheduled Work Commitment already exists and all other MS-PROT-081 requirements remain satisfied, its authorised participant may continue to record the accepted:

```text
CLOCK_IN
BREAK_START
BREAK_END
CLOCK_OUT
```

events without a current BUSINESS entitlement.

Canonical:

```text
Scheduled Work Commitment
established while valid
        ↓
commercial grant later ends
        ↓
worker may still truthfully
record performance of that
existing commitment
```

Commercial loss MUST NOT force inaccurate attendance evidence.

## 10.4 Time correction and approval

The contract additionally covers otherwise-authorised:

```text
correction of existing
work/time evidence

approval/rejection of the
relevant existing recorded
worked-time evidence
```

where those operations remain semantically valid.

This preserves truthful reconciliation and Compensation evidence.

It does not grant Workforce Compensation permission.

## 10.5 Unscheduled work exclusion

The contract does NOT authorise new unmatched/unscheduled-work recording.

Where no qualifying Scheduled Work Commitment exists, the unresolved policy under:

```text
MS-PROT-081-DQ-012
```

remains controlling.

This amendment MUST NOT use commercial classification to invent that policy.

---

# 11. Leave Request Origination

## 11.1 Contract

```text
workforce-scheduling/leave-request-origination-access@1
```

requires:

```text
REQUEST_WORKFORCE_LEAVE
```

## 11.2 Scope

The contract covers an otherwise eligible workforce participant submitting a new Leave Request under one exact Workforce Scheduling Arrangement.

It does not establish:

```text
leave entitlement
leave approval
leave pay
statutory leave
Compensation
```

## 11.3 Materially different request

Where changing an existing request requires a new Leave Request under the governing owner semantics, the replacement/new request requires current commercial permission.

The resolution exemption below MUST NOT be used to manufacture a materially different Leave Request.

---

# 12. Existing Leave Request Resolution

## 12.1 Contract

```text
workforce-scheduling/existing-leave-request-resolution-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

## 12.2 Bounded scope

For one exact existing pending Leave Request, the contract covers:

```text
authorised APPROVE

authorised REJECT

authorised worker withdrawal
```

where the existing owner semantics permit the operation.

This prevents commercial downgrade from stranding a request already entrusted to GrandRue for resolution.

## 12.3 Approval boundary

Approval remains subject to:

```text
leave eligibility/rule evidence
merchant actor authority
exact Arrangement affinity
jurisdiction rules
schedule-conflict rules
current request state
concurrency
```

If approval conflicts with an existing Scheduled Work Commitment, the existing MS-PROT-081 conflict rules continue to govern.

The exemption does not permit hidden shift deletion.

Any legitimate release of an exact existing Scheduled Work Commitment required by the accepted resolution path may use Section 10.

## 12.4 No mutation-by-resolution

The exemption does not authorise the merchant to replace:

```text
14–18 September
```

with:

```text
14–28 September
```

and call that approval.

A materially different request remains new activity.

---

# 13. Merchant Observation

## 13.1 Contract

```text
workforce-scheduling/merchant-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for otherwise-authorised inspection of existing MS-PROT-081-owned information.

This may include, according to existing actor/data authority:

```text
Workforce Scheduling Arrangements
Workforce Time Terms
Shift Offers
Scheduled Work Commitments
Scheduled Breaks
Time Capture evidence
time corrections
Approved Worked-Time Evidence
Leave Requests
Leave Decisions
Approved Leave Evidence
retained workforce history
```

## 13.2 Boundary

This contract is observation only.

It does not grant:

```text
new scheduling
material amendment
new Leave Request
Compensation information
Payroll
bulk export
analytics
cross-merchant access
Rota authority
```

A merchant must not lose legitimate access to records merely because the commercial grant that once permitted their creation later ends.

Retention and permission to observe remain separate.

---

# 14. Personal Observation

## 14.1 Contract

```text
workforce-scheduling/personal-observation-access@1
```

requires:

```text
NO COMMERCIAL ENTITLEMENT
```

for otherwise-authorised personal observation under accepted Personal Workforce Self-Service authority.

It may expose only the participant's permitted own information, such as:

```text
own Scheduled Work Commitments
own Shift Offers
own Scheduled Breaks
own Time Capture evidence
own time corrections
own Leave Requests / decisions
own retained workforce history
```

## 14.2 No operational elevation

Commercial exemption does not convert Personal Workforce Self-Service into merchant-operational authority.

The personal context still cannot thereby:

```text
manage another worker
approve another person's leave
approve another person's time
access customer information
access merchant-wide reports
```

---

# 15. Standard Tier Allocation

The four protected purposes in this amendment belong to BUSINESS:

```text
MAINTAIN_WORKFORCE_SCHEDULING_TERMS

PLAN_WORKFORCE_SCHEDULE

COMMIT_OFFERED_WORK

REQUEST_WORKFORCE_LEAVE
```

GROWTH SHALL explicitly include the same grants through the standard hierarchy.

FREE SHALL not include them in the standard grant set.

Therefore:

```text
FREE
    no new protected
    MS-PROT-081 workforce activity

BUSINESS
    exact admitted workforce grants

GROWTH
    same workforce grants
    + independently governed
      Growth services
```

No runtime implementation may determine permission by comparing tier-name strings.

---

# 16. Standing Free, Trial and Independent Grant Sources

This amendment does not modify the existing MS-PROT-056 grant-source model.

A commercially protected workforce operation may be satisfied by any independently valid accepted grant source carrying its eventual exact entitlement identity, including where applicable:

```text
full-experience trial
paid agreement
commercial remediation
another future accepted source
```

Standing Free does not receive the four protected workforce purposes merely because Workforce records exist.

A worker's personal action is evaluated against the merchant's applicable commercial permission, not a personal subscription plan.

---

# 17. Commercial Loss

Where all effective grants for the protected workforce purposes end, GrandRue SHALL distinguish:

## Blocked new protected activity

```text
new Workforce Scheduling Arrangement
new Workforce Time Terms authoring
new targeted Shift Offer
new directly assigned Scheduled Work Commitment
material Scheduled Work revision
acceptance of an existing Shift Offer
new Leave Request
```

from:

## Still potentially permitted resolution/observation

```text
observe existing workforce records

end an Arrangement where otherwise valid

release/cancel an existing
Scheduled Work Commitment

record time against an exact
existing Scheduled Work Commitment

correct/approve/reject existing
worked-time evidence

reject/resolve an existing Shift Offer
without accepting it

approve/reject/withdraw an exact
existing Leave Request
```

All latter operations remain subject to their non-commercial authority.

---

# 18. No Revival After Re-Entitlement

A later valid BUSINESS/GROWTH grant:

```text
does not revive an ENDED Arrangement

does not revive a cancelled
Scheduled Work Commitment

does not revive a rejected,
withdrawn, expired or otherwise
non-actionable Shift Offer

does not reopen an already resolved
Leave Request
```

Commercial permission permits otherwise-valid current operations.

It does not rewrite workforce history.

---

# 19. Rota / MS-PROT-091 Boundary

This amendment does not classify MS-PROT-091-owned Rota semantics.

In particular, it does not establish Commercial permission for:

```text
Rota Shift work requirements
open shifts
Shift Claims
rota composition
rota-owned staffing requirements
another MS-PROT-091 operation
```

A Rota operation that ultimately establishes an MS-PROT-081 Scheduled Work Commitment remains commercially classified first at its owning operation boundary.

The existence of:

```text
PLAN_WORKFORCE_SCHEDULE
or
COMMIT_OFFERED_WORK
```

does not create wildcard permission for an MS-PROT-091 target.

MS-PROT-056 v1.7's composite BUSINESS allocation across MS-PROT-081 and MS-PROT-091 therefore still requires an independently accepted MS-PROT-091 commercial classification before the complete standard catalogue can be closed.

---

# 20. Workforce Compensation Boundary

Nothing in this amendment grants:

```text
Compensation Relationship access
Compensation Terms authoring
Compensation calculation
Payroll calculation
pay-document access
Payroll execution
Non-Payroll payment execution
```

MS-PROT-080 remains the source owner for those responsibilities.

`MS-PROT-080-V11-DQ-016` remains independent and unresolved.

The fact that Approved Worked-Time Evidence or Approved Leave Evidence may later be consumed by Workforce Compensation does not allow MS-PROT-081 commercial permission to satisfy a Compensation commercial requirement.

---

# 21. Notification Boundary

The workforce Notification Contracts accepted by MS-PROT-081 v1.3 retain their source meaning.

This amendment does not create a separate workforce Notification entitlement.

Necessary supporting communication for an already included workforce service MUST NOT become an artificial higher-tier obstacle contrary to MS-PROT-056 v1.7.

However:

```text
necessary supporting service
    ≠ missing commercial classification
      automatically means exempt
```

Where a cross-owner supporting access classification is materially required by the complete catalogue, that classification must be resolved by its applicable authority rather than invented here.

Notification delivery remains owned by composite MS-PROT-075.

---

# 22. Quantitative Packaging Boundary

This amendment establishes no:

```text
per-worker price
per-Arrangement price
shift quota
Leave Request quota
clock-event quota
location charge
usage allowance
"unlimited" claim
```

Those concerns remain part of:

```text
MS-PROT-056-V17-DQ-003
```

where materially required.

A merchant having multiple legitimate Workforce Scheduling Arrangements for one Membership does not, by itself, alter the semantic commercial classification established here.

---

# 23. Actor and Trust Boundary

Commercial permission is necessary only where this amendment says it is required.

It is never sufficient.

Examples:

```text
BUSINESS merchant
+
worker lacking valid Arrangement
    → cannot accept offer

BUSINESS merchant
+
manager lacking operation authority
    → cannot approve leave

no current BUSINESS grant
+
worker with exact pre-existing
Scheduled Work Commitment
+
valid personal context
    → may still record qualifying
      existing-work evidence
```

A subscription never creates a staff privilege.

---

# 24. Data and Exposure Boundary

No-entitlement observation does not mean unrestricted data access.

All existing:

```text
Merchant Scope
relationship
privacy
data-use
retention
Exposure
historical-access
account-lifecycle
```

rules remain effective.

Commercial loss does not imply data deletion.

Retention does not imply access.

Observation permission does not imply export permission.

---

# 25. AI Boundary

AI may assist with a permitted workforce interaction only within separately accepted AI authority.

AI SHALL NOT infer that a missing commercial grant exists.

AI SHALL NOT convert:

```text
FREE
```

into permission to create new workforce activity.

AI SHALL NOT treat an existing Shift Offer as an existing Scheduled Work Commitment.

AI SHALL NOT use an exempt resolution contract to manufacture:

```text
new shifts
material rescheduling
new Leave Requests
new Arrangements
```

Deterministic workforce operation remains available without live AI where otherwise supported.

---

# 26. Preserved Deferred Questions

This amendment does not resolve or implicitly answer:

```text
MS-PROT-081-DQ-001
exact Java/persistence representation

MS-PROT-081-DQ-002
shift swap / shift cover

MS-PROT-081-DQ-003..007
jurisdiction/overtime/break/leave/pay rules

MS-PROT-081-DQ-008
attendance mechanism

MS-PROT-081-DQ-009
anti-time-theft/fraud

MS-PROT-081-DQ-010
time rounding

MS-PROT-081-DQ-011
manager approval/self-attestation policy

MS-PROT-081-DQ-012
unscheduled-work handling

MS-PROT-081-DQ-013
personal API/UI

MS-PROT-081-DQ-014
leave-balance representation

MS-PROT-081-DQ-015
remaining Timekeeping notifications

MS-PROT-081-DQ-016
Resource linkage representation

MS-PROT-081-DQ-017
customer-commitment remediation

MS-PROT-081-DQ-018
legal/commercial consequence of shift cancellation

MS-PROT-081-DQ-019
native/offline support

MS-PROT-081-DQ-021
already resolved by v1.2

MS-PROT-081-DQ-022
retrospective compensation attribution
```

In particular, the no-entitlement existing-work contract MUST NOT be used to invent an answer to DQ-012.

---

# 27. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

The merchant-facing commercial model remains:

```text
FREE — Establish
BUSINESS — Operate
GROWTH — Grow
```

Merchants and ordinary workers do not administer:

```text
Commercial Entitlement identities
access-contract revisions
residual-access graphs
grant provenance
```

The additional internal distinction exists to prevent commercially ending BUSINESS access from corrupting real workforce commitments or attendance evidence.

This is intrinsic correctness complexity.

It supports Administrative Compression because the platform handles the distinction automatically.

---

# 28. Falsification

## F1 — Business subscription ends during a shift

A worker has a valid existing Scheduled Work Commitment and has already clocked in.

BUSINESS permission ends before clock-out.

Expected:

```text
BREAK_START / BREAK_END / CLOCK_OUT
remain permitted where otherwise valid
```

GrandRue must not force false time evidence.

**PASS**

## F2 — Future shift already committed before downgrade

BUSINESS ends.

Expected:

merchant may observe or cancel the existing commitment.

Worker may fulfil/evidence it if the commitment remains valid.

Merchant cannot materially reschedule it using the exempt resolution contract.

**PASS**

## F3 — Shift Offer outstanding at downgrade

Expected:

```text
worker may observe offer
worker may REJECT offer
worker may not ACCEPT offer
without current COMMIT_OFFERED_WORK
```

because:

```text
Shift Offer
    ≠ Scheduled Work Commitment
```

**PASS**

## F4 — Material shift revision after downgrade

Merchant changes Monday 09:00–17:00 to Tuesday 13:00–21:00.

Expected:

requires:

```text
PLAN_WORKFORCE_SCHEDULE
```

Existing-work resolution exemption does not apply.

**PASS**

## F5 — Shift cancellation after downgrade

Merchant needs to remove a future existing work obligation.

Expected:

explicit cancellation/release remains available subject to owner authority.

**PASS**

## F6 — Pending Leave Request at downgrade

Employee submitted the exact request while service was commercially available.

Expected:

merchant may APPROVE or REJECT.

Worker may withdraw.

The request is not stranded.

**PASS**

## F7 — Merchant tries to enlarge pending leave after downgrade

Expected:

not resolution.

A materially different/new request requires current:

```text
REQUEST_WORKFORCE_LEAVE
```

**PASS**

## F8 — New Leave Request on FREE

No independent valid grant source exists.

Expected:

commercial denial of the new Leave Request.

Personal observation of historical permitted information may remain.

**PASS**

## F9 — Time correction after downgrade

A missed BREAK_END must be corrected for already-existing work evidence.

Expected:

correction/approval path remains available.

Commercial loss cannot require knowingly false worked-time evidence.

**PASS**

## F10 — Unscheduled clock-in

No Scheduled Work Commitment exists.

Expected:

this amendment does not authorise the operation through the existing-work exemption.

`MS-PROT-081-DQ-012` remains unresolved.

**PASS**

## F11 — Manager lacks authority

Merchant has BUSINESS.

Manager lacks applicable leave/time authority.

Expected:

commercial permission does not confer actor authority.

**PASS**

## F12 — Personal phone

Worker uses Personal Workforce Self-Service.

Expected:

own observation and permitted own resolution remain bounded to the personal context.

The phone does not become a merchant-operational device.

**PASS**

## F13 — Multi-Arrangement worker

Alex has Arrangements A and B.

Expected:

grant satisfaction does not collapse their identities.

Every operation retains exact Arrangement affinity.

**PASS**

## F14 — Compensation

Merchant has all four MS-PROT-081 BUSINESS purposes.

Expected:

no Compensation or Payroll permission follows.

**PASS**

## F15 — Rota open ShiftClaim

A worker claims an MS-PROT-091-owned open Shift.

Expected:

this amendment supplies no commercial classification for that Rota operation.

MS-PROT-081's offer-acceptance contract cannot be substituted.

**PASS**

## F16 — Notification failure

A valid Scheduled Work Commitment was created while authorised.

Its notification later fails.

Expected:

commitment remains true.

Commercial permission and Notification delivery remain separate.

**PASS**

## F17 — Low-software-capacity café merchant

Merchant schedules workers, approves leave and records time without configuring entitlement identities.

Expected:

merchant sees only business actions.

GrandRue performs commercial composition internally.

**PASS**

## F18 — Consultant without workforce scheduling

Merchant does not use MS-PROT-081 semantics.

Expected:

BUSINESS commercial packaging does not manufacture Arrangements, shifts, timekeeping or Leave.

**PASS**

## F19 — Re-entitlement

BUSINESS later returns.

Expected:

ended Arrangements, cancelled work, rejected offers and resolved leave do not revive.

**PASS**

---

# 29. Alternatives

## One workforce wildcard

**REJECTED**

```text
WORKFORCE_ALL
```

would erase owner-purpose boundaries and could accidentally include Rota or Compensation.

## One entitlement per UI screen

**REJECTED**

Screens are presentation, not commercial authority.

## Require BUSINESS for every historical read

**REJECTED**

Would obstruct legitimate inspection and resolution of records and commitments established while authority existed.

## Allow every old operation after downgrade

**REJECTED**

Would convert historical ownership into perpetual entitlement for new activity.

## Treat a Shift Offer as residual committed work

**REJECTED**

MS-PROT-081 explicitly distinguishes an offer from a Scheduled Work Commitment.

## Block timekeeping when entitlement ends

**REJECTED**

Could corrupt attendance and downstream Compensation evidence.

## Let this amendment classify Rota

**REJECTED**

MS-PROT-091 retains Rota ownership.

## Let this amendment classify Payroll

**REJECTED**

MS-PROT-080 retains Compensation/Payroll ownership.

---

# 30. Hard Invariants

1. Plan identity never determines Workforce semantics.
2. Commercial permission never creates an Arrangement, Shift Offer, Scheduled Work Commitment, Time Capture Event or Leave fact.
3. Exactly four initial MS-PROT-081 protected commercial purposes are admitted.
4. The four protected purposes belong to BUSINESS and GROWTH, not standard FREE.
5. A worker does not require a separate personal subscription.
6. Arrangement/terms authoring requires current protected permission.
7. New work planning requires current protected permission.
8. Accepting a Shift Offer requires current protected permission.
9. A Shift Offer is not residual Scheduled Work Commitment authority.
10. New Leave Request origination requires current protected permission.
11. Merchant observation of existing MS-PROT-081 records has no independent Commercial Entitlement.
12. Personal observation of otherwise-authorised own records has no independent Commercial Entitlement.
13. Cancellation/release of existing Scheduled Work has no independent Commercial Entitlement.
14. Qualifying time capture for exact existing Scheduled Work has no independent Commercial Entitlement.
15. Correction/approval of qualifying existing work evidence has no independent Commercial Entitlement.
16. The existing-work exemption does not authorise unmatched/unscheduled-work semantics.
17. Resolution of an exact existing Shift Offer without accepting it has no independent Commercial Entitlement.
18. Resolution of an exact existing Leave Request has no independent Commercial Entitlement.
19. Existing-request resolution cannot manufacture a materially different Leave Request.
20. Ending an Arrangement has no independent Commercial Entitlement.
21. No exemption waives actor, scope, privacy, Exposure, lifecycle or jurisdiction authority.
22. Material rescheduling remains protected new use, not residual cleanup.
23. Commercial loss does not delete workforce history.
24. Commercial re-entitlement does not revive terminated source facts.
25. Rota commercial classification remains MS-PROT-091-owned.
26. Compensation/Payroll commercial classification remains MS-PROT-080-owned.
27. Notification delivery remains MS-PROT-075-owned.
28. This amendment establishes no price, quota or usage allowance.
29. Exact Commercial Entitlement identities remain MS-PROT-056 catalogue work.
30. Acceptance does not activate implementation.

---

# 31. Deferred-Decision Disposition

With explicit manual approval and conforming repository formalisation:

```text
MS-PROT-081-DQ-020
Commercial entitlement/tier packaging
    → RESOLVED
```

Resolution means:

> the complete currently accepted MS-PROT-081-owned Workforce Scheduling / Timekeeping / Leave portfolio now has owner-qualified commercial-access classifications sufficient for later exact MS-PROT-056 catalogue binding.

It does not mean:

```text
MS-PROT-056-V17-DQ-001 resolved
MS-PROT-080-V11-DQ-016 resolved
MS-PROT-091 classified
prices established
catalogue published
C3 completed
implementation activated
```

---

# 32. Governance Result

**Feature Admission:** PASS  
**Fundamental Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Owner-qualified scope:** PASS  
**Tier allocation conformance:** PASS  
**New-use / resolution separation:** PASS  
**ShiftOffer / commitment separation:** PASS  
**Time-evidence safety:** PASS  
**Leave-resolution boundary:** PASS  
**Personal / merchant authority separation:** PASS  
**Compensation ownership:** PASS  
**Rota ownership:** PASS  
**Notification ownership:** PASS  
**Anti-wildcard review:** PASS  
**Low-software-capacity merchant falsification:** PASS  
**Ordinary-staff falsification:** PASS  
**Ambiguity review:** PASS

**RECOMMENDATION: ACCEPT**

**Manual approval:** GRANTED on 15 September 2026  
**Repository formalisation:** AUTHORIZED AND RECORDED

---

# 33. Acceptance Statement

GrandRue's BUSINESS workforce service commercially permits merchants to establish and materially operate new Workforce Scheduling activity without making the subscription tier the owner of workforce semantics.

When that commercial permission ends, GrandRue continues to preserve truthful history and permits the bounded observation and resolution needed for already-established workforce facts, while preventing those resolution paths from becoming backdoors for new paid activity.

> **Commercial permission governs new protected workforce use; it must neither manufacture workforce authority while present nor corrupt existing workforce commitments and evidence when it ends.**
