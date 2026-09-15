# MS-PROT-081 v1.3 — Workforce Scheduling, Leave Notification & Reminder Contract Portfolio Amendment

**Document ID:** MS-PROT-081  
**Version:** 1.3  
**Status:** **ACCEPTED by explicit manual approval on 9 September 2026**  
**Approved:** Explicit manual approval of the complete final proposed authority in ChatGPT on 9 September 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-081 v1.0 + v1.1 + v1.2 within Workforce Scheduling/Leave notification requirements and Scheduled Work reminder semantics  
**Depends on:** Composite MS-PROT-075 through v1.2; MS-PROT-065; MS-PROT-074; MS-PROT-082; MS-PROT-091  
**Partially resolves:** `MS-PROT-081-DQ-015 — Exact notification contracts for shift/leave/time actions`  
**Preserves:** `MS-PROT-081-DQ-009`, `MS-PROT-081-DQ-010`, `MS-PROT-081-DQ-011`, `MS-PROT-081-DQ-012` and the Timekeeping-notification portion of `MS-PROT-081-DQ-015` as unresolved  
**Implementation activation:** NONE  
**Purpose:** Establish the bounded source-owned Notification Contract portfolio for worker-specific Scheduled Work, targeted Shift Offers, Leave decisions and Scheduled Work reminders without transferring workforce truth, reminder timing, Notification delivery, jurisdictional leave authority or Timekeeping policy to the wrong capability.

---

# 1. Governing Decision

Composite MS-PROT-081 SHALL establish a bounded initial workforce Notification Contract portfolio for authoritative worker-specific scheduling and Leave facts.

The governing separation is:

```text
business / jurisdictional rule or fact
        ↓
authoritative Workforce Scheduling / Leave fact
        ↓
source-owned communication requirement
        ↓
MS-PROT-075 Notification Contract / Intent / delivery lifecycle
        ↓
provider or internal fulfilment path
```

The owning workforce process determines:

```text
why the communication exists
whether the communication remains applicable
which exact workforce fact/revision it communicates
business reminder timing where a reminder is configured
business consequence, if any
```

Composite MS-PROT-075 owns:

```text
NotificationContract infrastructure
NotificationIntent
recipient/channel resolution
NotificationPreference application
rendering
NotificationDispatch
DeliveryAttempt
DeliveryEvidence
provider-delivery coordination
```

MS-PROT-065 owns durable wake-up and scheduled background execution.

Notification transport MUST NOT become Workforce Scheduling, Leave, Timekeeping or jurisdictional authority.

---

# 2. Scope

This amendment admits exactly the following initial Workforce Scheduling/Leave Notification Contract portfolio:

```text
scheduled-work-established@1
scheduled-work-materially-revised@1
scheduled-work-released@1
targeted-shift-offer-issued@1
targeted-shift-offer-no-longer-actionable@1
leave-decision-recorded@1
scheduled-work-reminder@1
```

The portfolio is deliberately bounded.

This amendment does NOT establish an unrestricted generic `workforce-event-notification` contract and does NOT authorise arbitrary future workforce communications merely because Notifications can transport them.

---

# 3. Non-Goals

This amendment does NOT define:

```text
Timekeeping anomaly notifications
clock-in / clock-out reminders
missed-clock notifications
break-compliance reminders
manager time-approval reminders
unscheduled-work notifications
fraud / anti-time-theft alerts
jurisdiction-specific working-time notices
statutory leave entitlement formulas
statutory leave-pay calculations
provider selection
provider retry/backoff
Notification rendering technology
universal workforce reminder cadence
mandatory acknowledgement of schedule messages
worker surveillance / disciplinary scoring
```

Exact Timekeeping notification/reminder semantics remain deferred because the relevant Timekeeping policy questions remain unresolved under `MS-PROT-081-DQ-009` through `MS-PROT-081-DQ-012`.

---

# 4. Ownership and Source-Truth Boundary

## 4.1 Workforce Scheduling / Leave owns source meaning

Composite MS-PROT-081 owns the communication requirement only where the requirement derives from a source fact owned by Workforce Scheduling/Leave.

The source fact remains authoritative even if Notification delivery fails.

Therefore:

```text
Notification delivered
    ≠ ScheduledWorkCommitment established

Notification failed
    ≠ ScheduledWorkCommitment absent

Notification read
    ≠ worker accepted a material change

Notification delivered
    ≠ LeaveDecision legally correct
```

## 4.2 MS-PROT-091 Shift boundary

MS-PROT-091 owns the rota `Shift` work requirement and rota-composition semantics.

Composite MS-PROT-081 owns the worker-specific `ScheduledWorkCommitment`, arrangement-affined `ShiftOffer` semantics and Leave facts within its accepted scope.

Canonical distinction:

```text
Shift
    = work requirement / rota need

ScheduledWorkCommitment
    = authoritative fact that one exact workforce participant is scheduled
```

An unassigned Shift does not by itself create a worker Notification responsibility.

## 4.3 Notification delivery remains MS-PROT-075-owned

The seven contracts in this amendment establish source-qualified communication responsibilities. They do not duplicate MS-PROT-075 delivery semantics.

The following remain governed by composite MS-PROT-075:

```text
recipient endpoint resolution
channel selection
Notification Preference
quiet-hour treatment
provider/internal fulfilment
rendering
DeliveryAttempt
DeliveryEvidence
provider evidence interpretation
transport retry/reconciliation
```

---

# 5. Common Workforce Notification Contract Rules

Every admitted contract MUST retain enough source affinity to identify the exact authoritative workforce fact or revision that caused the logical communication responsibility.

At minimum, where applicable, source affinity SHALL include:

```text
Merchant Scope
Merchant Membership
Workforce Scheduling Arrangement
source aggregate/fact identity
exact material source revision
correlation / causation
source effective time
communication requirement identity
```

Where the source fact is a `ScheduledWorkCommitment`, the contract SHALL retain the exact Scheduled Work Commitment identity and material revision.

Where the source fact is a targeted `ShiftOffer`, the contract SHALL retain the exact Shift Offer identity/revision and exact target Workforce Scheduling Arrangement.

Where the source fact is a `LeaveDecision`, the contract SHALL retain the exact Leave Request and Leave Decision affinity required to explain the decision communicated.

Notification content MUST be derived from the source fact under deterministic source-grounded projection. Notification content MUST NOT become a competing mutable copy of workforce truth.

---

# 6. Recipient Semantics

The ordinary recipient for the seven contracts is the exact workforce participant represented by the applicable Merchant Membership and Workforce Scheduling Arrangement.

Recipient semantics MUST NOT be inferred from:

```text
job title
role-name similarity
current pay relationship
latest Compensation Relationship
email-address similarity
AI inference
historical recipient choice
```

Where a communication is arrangement-affined, the recipient relationship SHALL remain arrangement-qualified even when the same Identity/Merchant Membership participates through multiple Arrangements.

MS-PROT-075 owns the later resolution of permitted channel endpoints.

A current relationship/responsibility recipient MUST be revalidated according to MS-PROT-075 before operational details are externalised.

---

# 7. Contract — `scheduled-work-established@1`

## 7.1 Trigger

A logical `scheduled-work-established@1` communication responsibility exists when one new `ScheduledWorkCommitment` becomes authoritative for one exact Merchant Membership and Workforce Scheduling Arrangement.

The source mechanism MAY be:

```text
authorised direct assignment
accepted targeted ShiftOffer
accepted open ShiftClaim / another accepted allocation path
```

where that path conformingly establishes the Scheduled Work Commitment.

The Notification responsibility derives from the committed Scheduled Work Commitment, not from an earlier proposal, draft Shift, attempted assignment or provider callback.

## 7.2 Source affinity

The communication MUST identify the exact Scheduled Work Commitment and exact establishing material revision.

A retry or duplicate source-event delivery MUST NOT create another logical Notification responsibility for the same commitment establishment.

## 7.3 Meaning

This contract communicates that the worker has authoritative scheduled work. It does not prove that work occurred and does not replace any acceptance evidence required by the applicable Workforce Time Terms.

---

# 8. Contract — `scheduled-work-materially-revised@1`

## 8.1 Trigger

A logical `scheduled-work-materially-revised@1` responsibility exists when an already-established Scheduled Work Commitment acquires a new authoritative material revision that changes worker-relevant scheduled-work meaning.

Materiality follows composite MS-PROT-081 and applicable accepted scheduling authority. At minimum, previously accepted material dimensions such as start time, end time, work date, material location/assignment or scheduled-break structure remain material where applicable.

No Notification responsibility is created merely because a projection was regenerated or non-material presentation metadata changed.

## 8.2 Exact revision affinity

The communication MUST retain affinity to:

```text
Scheduled Work Commitment identity
superseded material revision
new material revision
material-change provenance
```

A worker MUST NOT receive a materially-revised communication whose payload was rendered from a stale superseded revision after a later material revision has become authoritative.

## 8.3 Acceptance boundary

Delivery or reading of this notification MUST NOT manufacture worker acceptance where the applicable Workforce Time Terms require renewed acceptance for the material change.

---

# 9. Contract — `scheduled-work-released@1`

## 9.1 Trigger

A logical `scheduled-work-released@1` responsibility exists when an authoritative future Scheduled Work Commitment is explicitly released/cancelled so that the worker's future expectation to perform that scheduled work no longer remains.

The communication derives from the authoritative release/cancellation fact. An attempted cancellation that does not commit MUST NOT create this logical responsibility.

## 9.2 Historical preservation

Release communication MUST NOT imply deletion of:

```text
original Scheduled Work Commitment
prior revisions
acceptance evidence
attendance evidence
prior communication evidence
```

The released source fact remains historically traceable.

---

# 10. Contract — `targeted-shift-offer-issued@1`

## 10.1 Trigger

A logical `targeted-shift-offer-issued@1` responsibility exists when a Shift Offer becomes authoritative and actionable for one exact target Merchant Membership and Workforce Scheduling Arrangement.

An unassigned/open Shift visible to an eligible pool MUST NOT automatically generate one notification per eligible worker under this contract.

## 10.2 Meaning

The communication informs the target worker that an actionable offer exists.

It does NOT create:

```text
ScheduledWorkCommitment
acceptance
rejection
priority entitlement
customer-facing capacity
```

## 10.3 Idempotency

Duplicate publication, retry or wake-up for the same exact offer/revision MUST NOT multiply the logical issued-offer Notification responsibility.

---

# 11. Contract — `targeted-shift-offer-no-longer-actionable@1`

## 11.1 Trigger

A logical `targeted-shift-offer-no-longer-actionable@1` responsibility exists when a previously actionable targeted Shift Offer becomes authoritatively non-actionable without that exact pending offer remaining available for the target worker to accept.

The source fact MAY arise from an accepted offer lifecycle outcome such as withdrawal, expiry, supersession, cancellation or another owner-defined terminal/non-actionable disposition.

The contract communicates the source-owned fact; it does not invent the reason.

## 11.2 Currentness

Before externalisation, source applicability MUST be revalidated sufficiently to prevent a stale "no longer actionable" message from overriding a newer independently actionable offer/revision.

This contract does not prohibit a later new Shift Offer. A later offer is a distinct source responsibility with its own identity/revision.

---

# 12. Contract — `leave-decision-recorded@1`

## 12.1 Trigger

A logical `leave-decision-recorded@1` responsibility exists when an authorised `LeaveDecision` is authoritatively recorded for one exact Leave Request and Workforce Scheduling Arrangement.

The initial decision outcomes communicated by this contract are:

```text
APPROVE
REJECT
```

Submitting a Leave Request does not create an approval/rejection communication responsibility.

## 12.2 Leave truth and jurisdiction boundary

The notification communicates the authoritative recorded Leave Decision; it does not determine Leave entitlement, statutory minimums, leave-pay treatment or jurisdictional legality.

Composite MS-PROT-081 preserves:

```text
Leave entitlement/rule evidence
    ≠ Leave Request
    ≠ Leave Decision
    ≠ Approved Leave Interval
    ≠ Leave Taken
    ≠ Leave Pay
```

Where an applicable accepted jurisdiction authority establishes a statutory or regulatory leave floor, merchant policy MAY enhance a worker's benefit where permitted but MUST NOT be used by Main Street to reduce that applicable mandatory floor.

Main Street SHALL NOT invent one universal global annual-leave number or treat a jurisdiction-specific figure, including a 28-day figure, as a universal semantic rule.

Exact statutory entitlement formulas remain owned by the applicable jurisdiction/regulatory authority and remain outside this amendment.

## 12.3 Scheduling conflict boundary

A Leave approval communication does not resolve an unresolved conflict with an existing Scheduled Work Commitment. Composite MS-PROT-081's leave/schedule conflict rules remain authoritative.

---

# 13. Contract — `scheduled-work-reminder@1`

## 13.1 Purpose

`scheduled-work-reminder@1` is a bounded reminder about one still-current future Scheduled Work Commitment.

It is not a generic Timekeeping reminder and does not mean:

```text
clock in now
break now
approve time now
resolve attendance anomaly now
```

## 13.2 Reminder timing ownership

Notifications MUST NOT choose Workforce Scheduling reminder timing.

Canonical:

```text
Workforce Scheduling source policy / accepted merchant scheduling choice
    determines whether and when a Scheduled Work reminder is due

MS-PROT-065
    wakes due work

MS-PROT-075
    delivers the already-due communication
```

There is NO universal Main Street rule such as:

```text
all workforce reminders = 24 hours before
```

and NO universal mandatory reminder cadence.

Where no source-owned accepted reminder timing applies, this contract does not manufacture a reminder merely because a future commitment exists.

## 13.3 Source currentness

A reminder may externalise only while the referenced Scheduled Work Commitment/revision remains current enough for the reminder's meaning.

A reminder MUST NOT be sent from a stale source state when, before externalisation, the commitment has been:

```text
released/cancelled
materially revised so the reminder payload is stale
otherwise rendered non-applicable by authoritative source state
```

A material revision that remains reminder-eligible is evaluated against the current revision and current applicable reminder timing; the previous reminder obligation MUST NOT blindly survive merely because it had already been scheduled in background infrastructure.

## 13.4 Reminder identity and duplicate prevention

Each logical reminder occurrence MUST have stable source-qualified identity sufficient to prevent duplicate event delivery, duplicate scheduler wake-up or retry from multiplying the same logical reminder responsibility.

A retry of the same logical reminder is not a new business reminder occurrence.

A separately source-authorised later reminder occurrence, where the accepted source policy permits more than one, is a distinct logical responsibility.

---

# 14. Currentness, Race and Concurrency Rules

Source-owned applicability is evaluated against authoritative workforce state; Notification delivery cannot freeze stale business state into continued applicability.

At minimum:

1. a scheduled-work establishment/revision/release responsibility MUST bind the exact committed source revision;
2. an offer responsibility MUST bind the exact offer revision/disposition;
3. a Leave decision responsibility MUST bind the exact recorded Leave Decision;
4. a reminder MUST revalidate its source commitment/revision before externalisation according to the accepted orchestration boundary;
5. source mutation and logical communication-intent establishment MUST preserve enough transaction/event provenance that retries cannot create contradictory logical responsibilities for the same source transition;
6. concurrent source changes MUST resolve through the source capability's authoritative concurrency rules, not through "last notification sent".

A provider-side success cannot override a later authoritative source fact.

---

# 15. Idempotency and Retry

For each admitted contract:

```text
one logical source communication responsibility
        ↓
one logical NotificationIntent
```

Repeated source events, process retries, background wake-ups or delivery retries MUST NOT create a second logical NotificationIntent for the same exact responsibility.

Notification transport retry remains governed by composite MS-PROT-075 and MS-PROT-065.

The workforce owner MUST NOT respond to a Notification provider retry by re-performing the underlying business mutation.

---

# 16. Failure and Uncertainty

Notification failure MUST NOT roll back or rewrite an already-committed workforce fact merely because communication could not be delivered.

Canonical separation:

```text
source business mutation committed
        +
notification delivery later fails
        ↓
source business fact remains authoritative
notification failure remains delivery/operational evidence
```

If a source transition and required communication responsibility must be recorded atomically enough to avoid losing the responsibility, implementation SHALL use the accepted cross-capability/event/outbox consistency mechanisms. This amendment does not prescribe one persistence technology.

Provider uncertainty is handled by composite MS-PROT-075/MS-PROT-069. It MUST NOT cause duplicate Scheduled Work, Shift Offer or Leave effects.

---

# 17. Notification Read/Open Is Not Workforce Acknowledgement

The following are distinct:

```text
message dispatched
message provider-accepted
message provider-reported delivered
message read/opened where supported
worker accepted Shift Offer
worker accepted a material schedule change where required
worker performed work
worker acknowledged a legal notice where separately required
```

No admitted contract establishes mandatory acknowledgement semantics.

A future requirement for legally significant acknowledgement requires separate accepted authority.

---

# 18. Exposure, Data Minimisation and Business Language

Notification rendering MUST expose only information permitted for the exact recipient/context under applicable Exposure, Data Protection and workforce-access authority.

Ordinary worker-facing wording SHOULD use business language such as:

```text
Your shift has been scheduled.
Your shift time has changed.
Your shift has been cancelled.
You have a shift offer.
That shift offer is no longer available.
Your leave request was approved/rejected.
You are scheduled to work soon.
```

Ordinary workers MUST NOT need to understand:

```text
WorkforceSchedulingArrangement
ScheduledWorkCommitment
NotificationContract
NotificationIntent
semantic owner
source revision affinity
provider evidence profile
```

Those are internal implementation/design semantics.

---

# 19. AI Boundary

AI MAY assist with:

```text
interpreting merchant intent for a candidate reminder preference/policy
explaining a workforce communication in business language
assisting deterministic content drafting before the governing approval boundary
```

AI MUST NOT:

```text
invent a reminder cadence
create a Leave entitlement rule
infer statutory leave rights
fabricate a Shift Offer or Scheduled Work Commitment
convert delivery evidence into worker acceptance
bypass source currentness
silently create new workforce Notification Contracts
```

Any merchant policy that becomes authoritative follows the applicable merchant/manual approval and deterministic validation boundary.

---

# 20. Timekeeping Notification Deferral

This amendment deliberately does not close the Timekeeping portion of `MS-PROT-081-DQ-015`.

Exact Timekeeping communication semantics depend on unresolved questions including:

```text
MS-PROT-081-DQ-009
    anti-time-theft / fraud evidence policy

MS-PROT-081-DQ-010
    time-rounding rules

MS-PROT-081-DQ-011
    manager time-approval / self-attestation policy

MS-PROT-081-DQ-012
    unscheduled-work handling
```

Until those matters are accepted, Main Street MUST NOT invent generic notifications such as:

```text
"You forgot to clock in"
"Your break is too long"
"Approve this timesheet"
"Unscheduled work detected"
```

as authoritative generic workforce behaviour.

Therefore:

```text
MS-PROT-081-DQ-015
    = PARTIALLY RESOLVED
```

The Scheduling/Leave/Scheduled-Work-reminder portfolio defined here is resolved; the Timekeeping notification/reminder remainder stays deferred.

---

# 21. Relationship to MS-PROT-081 v1.2

MS-PROT-081 v1.2 remains unchanged and fully authoritative within its scope.

In particular this amendment does NOT alter:

```text
ArrangementOverlapPolicy
CrossArrangementOverlapOverride
MinimumInterCommitmentBuffer
same/different Arrangement overlap rules
sequential commitment-buffer semantics
merchant ownership of scheduling buffer values
```

Notification of a Scheduled Work fact never changes whether the underlying commitment was conformingly allocated under v1.2.

`MS-PROT-081-DQ-021` remains resolved by v1.2.

---

# 22. Invariants

**INV-081-N01 — Source Authority**  
Every admitted workforce Notification responsibility derives from an authoritative source-owned workforce fact or source-owned reminder-due condition.

**INV-081-N02 — Delivery Non-Authority**  
Notification delivery state MUST NOT become Workforce Scheduling, Leave or Timekeeping truth.

**INV-081-N03 — Exact Portfolio**  
The initial admitted portfolio is exactly the seven contract identifiers in Section 2; no generic catch-all contract is implied.

**INV-081-N04 — Exact Worker Affinity**  
Worker-directed scheduling/Leave communications remain bound to the exact Merchant Membership and Workforce Scheduling Arrangement where arrangement affinity is material.

**INV-081-N05 — Shift/Commitment Separation**  
An unassigned rota Shift does not itself create `scheduled-work-*` communication responsibility.

**INV-081-N06 — Establishment After Commitment**  
`scheduled-work-established@1` derives from a committed Scheduled Work Commitment, not an attempted assignment or draft Shift.

**INV-081-N07 — Material Revision Only**  
`scheduled-work-materially-revised@1` requires an authoritative material Scheduled Work revision; presentation-only changes do not qualify.

**INV-081-N08 — Release Is Explicit**  
`scheduled-work-released@1` requires an authoritative release/cancellation fact and preserves historical commitment evidence.

**INV-081-N09 — Targeted Offer Only**  
`targeted-shift-offer-issued@1` does not broadcast an open/unassigned Shift to every eligible worker.

**INV-081-N10 — Offer Non-Actionability Is Source-Owned**  
`targeted-shift-offer-no-longer-actionable@1` communicates only an authoritative owner-defined non-actionable state.

**INV-081-N11 — Leave Decision Affinity**  
`leave-decision-recorded@1` binds the exact Leave Request and authoritative Leave Decision.

**INV-081-N12 — Leave Notification Is Not Entitlement Authority**  
A Leave notification cannot determine statutory entitlement, leave-pay amount or jurisdictional legality.

**INV-081-N13 — No Universal Leave Number**  
Main Street MUST NOT encode one jurisdiction-specific annual-leave quantity as a universal workforce rule.

**INV-081-N14 — Mandatory Floor Preservation**  
Where accepted jurisdiction authority establishes a mandatory leave floor, merchant policy cannot be used by Main Street to reduce that floor.

**INV-081-N15 — Reminder Timing Is Source-Owned**  
MS-PROT-075 does not choose Scheduled Work reminder timing.

**INV-081-N16 — No Universal Reminder Cadence**  
No Main Street-wide reminder lead time or cadence is implied by `scheduled-work-reminder@1`.

**INV-081-N17 — Reminder Currentness**  
A reminder MUST NOT externalise materially stale or released Scheduled Work meaning.

**INV-081-N18 — Logical Intent Idempotency**  
Duplicate source/event/background/delivery retries MUST NOT multiply one logical workforce Notification responsibility.

**INV-081-N19 — Read Is Not Acceptance**  
Notification read/open/delivery evidence does not establish Shift Offer acceptance, schedule-change acceptance or work occurrence.

**INV-081-N20 — Failure Isolation**  
Notification delivery failure does not rewrite a committed workforce fact.

**INV-081-N21 — Provider Neutrality**  
Provider identity or provider status MUST NOT define workforce business meaning.

**INV-081-N22 — Timekeeping Remainder Deferred**  
Exact Timekeeping notification/reminder semantics remain unresolved until their governing Timekeeping policies are accepted.

**INV-081-N23 — AI Non-Authority**  
AI MUST NOT invent reminder timing, Leave rights or authoritative workforce communication policy.

**INV-081-N24 — Business-Language Surface**  
Ordinary workforce recipients need not learn internal Main Street semantic/Notification terminology.

**INV-081-N25 — v1.2 Preservation**  
This amendment does not weaken or reopen accepted cross-Arrangement overlap or merchant scheduling-buffer authority from v1.2.

---

# 23. Falsification

## F-01 — Direct assignment establishes work

An authorised merchant actor conformingly creates a worker-specific Scheduled Work Commitment.

Expected: one `scheduled-work-established@1` logical responsibility for the exact commitment/revision. **PASS**

## F-02 — Duplicate establishment event

The same committed establishment event is delivered twice.

Expected: one logical NotificationIntent, not two. **PASS**

## F-03 — Draft Shift only

A rota Shift exists but nobody is allocated.

Expected: no `scheduled-work-established@1` for any worker. **PASS**

## F-04 — Accepted targeted offer

A targeted Shift Offer is accepted and conformingly establishes one Scheduled Work Commitment.

Expected: offer acceptance does not multiply the commitment; the established-work communication binds the exact resulting commitment. **PASS**

## F-05 — Non-material presentation change

Only display metadata changes.

Expected: no `scheduled-work-materially-revised@1`. **PASS**

## F-06 — Material time change

A Scheduled Work Commitment changes from 09:00–17:00 to 10:00–18:00 through an authoritative material revision.

Expected: one materially-revised responsibility bound to old/new material revisions. **PASS**

## F-07 — Stale revision rendered

A second material revision commits before the first revision-change message externalises.

Expected: stale content MUST NOT be presented as current Scheduled Work meaning. **PASS**

## F-08 — Cancellation commits

A future Scheduled Work Commitment is explicitly cancelled.

Expected: one `scheduled-work-released@1`; original commitment/history preserved. **PASS**

## F-09 — Cancellation attempt fails

A cancellation command is rejected and no source release fact commits.

Expected: no released-work responsibility. **PASS**

## F-10 — Open Shift published

One open rota Shift becomes available to a pool of ten eligible workers.

Expected: this contract portfolio does not create ten `targeted-shift-offer-issued@1` responsibilities merely from open publication. **PASS**

## F-11 — Targeted offer issued

One exact Shift Offer becomes actionable for one exact Arrangement.

Expected: one targeted-offer-issued responsibility. **PASS**

## F-12 — Duplicate offer publication

The same exact offer/revision is republished through retry.

Expected: one logical issued-offer responsibility. **PASS**

## F-13 — Offer withdrawn

A targeted offer becomes authoritatively non-actionable through withdrawal.

Expected: one `targeted-shift-offer-no-longer-actionable@1`. **PASS**

## F-14 — Old offer gone, new offer exists

Offer A becomes non-actionable and later Offer B is issued for the same worker/Shift context.

Expected: A's non-actionable communication does not invalidate B; identities remain distinct. **PASS**

## F-15 — Leave request submitted only

A worker submits Leave but no decision exists.

Expected: no `leave-decision-recorded@1`. **PASS**

## F-16 — Leave approved

An authorised APPROVE decision commits for the exact request/Arrangement.

Expected: one leave-decision-recorded responsibility communicating APPROVE. **PASS**

## F-17 — Leave rejected

An authorised REJECT decision commits.

Expected: one leave-decision-recorded responsibility communicating REJECT. **PASS**

## F-18 — Notification tries to calculate leave entitlement

Rendering logic attempts to infer a statutory entitlement number from a generic platform default.

Expected: rejected; entitlement remains jurisdiction/Leave authority. **PASS**

## F-19 — Universal 28-day assumption

A global rule attempts to assert 28 days for every worker/jurisdiction.

Expected: rejected. **PASS**

## F-20 — Merchant policy below accepted mandatory floor

Applicable accepted jurisdiction authority establishes a mandatory minimum and merchant configuration attempts to reduce it.

Expected: the merchant reduction cannot become conforming Main Street leave authority. **PASS**

## F-21 — Future Scheduled Work with no source reminder rule

A commitment exists but no accepted source-owned reminder timing applies.

Expected: no reminder invented by Notifications. **PASS**

## F-22 — Reminder due

A current Scheduled Work Commitment reaches an accepted source-owned reminder due condition.

Expected: one `scheduled-work-reminder@1` logical responsibility; MS-PROT-065 wakes it and MS-PROT-075 delivers it. **PASS**

## F-23 — Commitment cancelled before reminder externalisation

Reminder work is already queued; the commitment is then released before dispatch.

Expected: source applicability revalidation suppresses the stale reminder. **PASS**

## F-24 — Commitment materially revised before reminder externalisation

Queued reminder payload reflects an old time; a material revision commits first.

Expected: stale payload is not externalised as current truth; current source policy/revision determines any new applicable reminder. **PASS**

## F-25 — Duplicate scheduler wake-up

The same reminder occurrence wakes twice.

Expected: one logical NotificationIntent for that occurrence. **PASS**

## F-26 — Provider failure

EMAIL delivery fails after the workforce source fact commits.

Expected: source fact remains authoritative; delivery evidence/fallback/retry follows Notifications authority. **PASS**

## F-27 — Provider says delivered

Provider reports successful delivery of a material-change message.

Expected: no worker acceptance is manufactured. **PASS**

## F-28 — Timekeeping "forgot to clock in" proposal

No accepted unscheduled-work/timekeeping reminder policy exists.

Expected: not admitted by this v1.3 portfolio; DQ-015 Timekeeping remainder remains deferred. **PASS**

## F-29 — AI guesses reminder lead time

AI proposes two hours because it observes merchant history, without merchant/source authority.

Expected: no authoritative reminder timing is created. **PASS**

## F-30 — v1.2 overlap/buffer conflict

A proposed Scheduled Work allocation violates an applicable v1.2 overlap or MinimumInterCommitmentBuffer rule but its notification could be delivered.

Expected: notification capability cannot make the invalid allocation valid; v1.2 scheduling authority governs first. **PASS**

---

# 24. Rejected Alternatives

The following alternatives are rejected within this amendment scope:

1. **Notification-owned workforce policy** — rejected because delivery infrastructure would become business authority.
2. **One generic workforce catch-all Notification Contract** — rejected because it obscures source ownership and allows accidental feature expansion.
3. **Universal 24-hour Scheduled Work reminder** — rejected because reminder timing is a business-source decision, not platform delivery policy.
4. **Universal mandatory acknowledgement** — rejected because delivery/read evidence does not prove business acceptance and legal acknowledgement needs separate authority.
5. **Notify every eligible worker whenever an open Shift exists** — rejected as an unbounded administrative/noise policy not established by this portfolio.
6. **Role-name or pay-type recipient inference** — rejected because exact Membership/Arrangement affinity already exists.
7. **Notification delivery changes workforce truth** — rejected because failure/success must not rewrite source facts.
8. **Provider status defines business outcome** — rejected under provider neutrality.
9. **Global fixed statutory Leave entitlement** — rejected because jurisdiction-specific rules are not universal Main Street semantics.
10. **Timekeeping notifications before Timekeeping policy closure** — rejected because unresolved DQ-009 through DQ-012 would otherwise be silently decided by notification wording.
11. **AI-inferred authoritative reminder policy** — rejected because AI is assistive, not semantic authority.
12. **Reopening v1.2 overlap/buffer policy through notification behaviour** — rejected because v1.3 is additive within its bounded communication scope.

---

# 25. Trade-offs

The design deliberately accepts:

- a small explicit contract portfolio instead of a universal event-to-message mechanism;
- source-currentness checks before reminder/material-change externalisation;
- partial rather than artificial complete closure of DQ-015;
- jurisdictional Leave dependency instead of one globally simple but incorrect Leave rule;
- internal source-affinity/idempotency complexity to keep merchant and staff interaction simple.

The cost is additional internal orchestration and traceability. The benefit is correct ownership, low user administration, provider replaceability and prevention of stale/duplicated workforce communications becoming business truth.

---

# 26. Fundamental Vision Conformance

**Outcome:** `VISION-CONFORMING`

### Business-to-Software Translation

PASS — Main Street translates authoritative workforce facts into appropriate worker communications without forcing merchants to configure delivery infrastructure.

### Administrative Compression

PASS — ordinary schedule/offer/leave changes can produce their corresponding communication responsibilities automatically; no enterprise Notification administration is introduced.

### Ordinary-Staff Training

PASS — workers receive business-language communication about their own work/leave rather than internal Main Street concepts.

### Role-Native Operation

PASS — the exact affected worker is the natural recipient; unrelated workforce information is not exposed.

### Target-Market Proportionality

PASS — seven bounded contracts provide materially useful small-business coordination without enterprise HR/communications scope.

### Capability Depth

PASS — the amendment stops at source communication requirements and reuses mature generic Notification infrastructure rather than duplicating it.

### Ownership Versus Integration

PASS — Main Street owns workforce facts it already requires; delivery remains provider-neutral infrastructure under MS-PROT-075/MS-PROT-048.

### Cross-Capability Value

PASS — Workforce Scheduling/Leave, Notifications and durable background work coordinate without ownership transfer.

### Exception-Driven Operation

PASS — merchants are not required to manually relay ordinary scheduling/Leave changes through a separate messaging workflow.

### Business Evolution

PASS — reminder behaviour and future communications can be introduced through accepted source-owned policy/feature admission rather than universal onboarding configuration.

Feature Admission:

```text
Representation: PASS
Coordination: PASS
Administrative Compression: PASS
```

---

# 27. Amendment Effect

Upon accepted repository formalisation:

1. composite MS-PROT-081 becomes v1.0 + v1.1 + v1.2 + v1.3 within scope-aware composition;
2. the seven contracts in Section 2 become accepted Workforce Scheduling/Leave source-owned Notification Contract portfolio authority;
3. `MS-PROT-081-DQ-015` becomes **PARTIALLY RESOLVED**;
4. exact Timekeeping notification/reminder semantics remain deferred pending the applicable Timekeeping decisions, including DQ-009 through DQ-012;
5. `MS-PROT-081-DQ-021` remains **RESOLVED** by v1.2 and is not reopened;
6. MS-PROT-091 remains owner of rota `Shift`; composite MS-PROT-081 remains owner of worker-specific Scheduled Work/Leave semantics;
7. composite MS-PROT-075 remains owner of Notification delivery infrastructure and generic Notification policy;
8. MS-PROT-065 remains owner of durable wake-up/scheduled background execution;
9. jurisdiction/regulatory authority remains owner of statutory Leave entitlement/formula semantics;
10. no other MS-PROT-081 DQ is resolved by this amendment;
11. implementation activation remains NONE.

---

# 28. Implementation Boundary

This authority is semantic/design authority only.

It does NOT select:

```text
Java type names
persistence schema
table layout
API endpoints
message broker
scheduler technology
provider vendor
exact UI component
lock/transaction implementation
```

A future production implementation MUST follow accepted `designs/IMPLEMENTATION-RULES.md`, tests first, exact design-to-code traceability and any then-current implementation gates.

No production implementation is activated by this acceptance.

---

# 29. Final Decision

Main Street SHALL treat worker-specific schedule/offer/Leave communication as source-qualified business responsibilities rather than generic Notification business policy.

The initial admitted portfolio is:

```text
scheduled-work-established@1
scheduled-work-materially-revised@1
scheduled-work-released@1
targeted-shift-offer-issued@1
targeted-shift-offer-no-longer-actionable@1
leave-decision-recorded@1
scheduled-work-reminder@1
```

Workforce Scheduling/Leave owns why these communications exist and the source business meaning. MS-PROT-075 owns delivery. Scheduled Work reminder timing remains source-owned and has no universal platform cadence. Leave notifications communicate authoritative decisions but do not invent jurisdictional entitlement. Exact Timekeeping notification/reminder semantics remain deferred until the governing Timekeeping policies are accepted.

`MS-PROT-081-DQ-015` is therefore **PARTIALLY RESOLVED**.

```text
VISION CONFORMANCE: VISION-CONFORMING
RECOMMENDATION: ACCEPT
MANUAL APPROVAL: GRANTED — 9 September 2026
STATUS: ACCEPTED
IMPLEMENTATION ACTIVATION: NONE
```
