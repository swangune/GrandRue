# MS-PROT-050 v1.5 — Dated Business Operating Override Mutation, Revision & Currentness Amendment

**Document ID:** MS-PROT-050  
**Version:** 1.5  
**Status:** ACCEPTED  
**Approved:** 15 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Business Hours owner-operation and currentness amendment  
**Design node:** prerequisite design gap discovered during the `MS-PROT-056-V17-DQ-001` catalogue-completeness audit; no prior MS-PROT-050 deferred-decision identifier exists for dated-override mutation  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** composite MS-PROT-050 through v1.4 within dated `BusinessOperatingOverride` mutation, immutable revision history, currentness, concurrency, idempotency, actor authority and the minimum stable-hours lifecycle interlock required to keep dated overrides temporally unambiguous  
**Depends on:** composite MS-PROT-031; composite MS-PROT-050 through v1.4; composite MS-PROT-051; composite MS-PROT-053; composite MS-PROT-059; composite MS-PROT-063; composite MS-PROT-069; composite MS-PROT-071; composite MS-PROT-076  
**Preserves:** MS-PROT-050 v1.2 Business Hours Scope; MS-PROT-050 v1.3 origin-date/carry-over/entered-date override semantics; MS-PROT-050 v1.4 stable weekly-hours revision authority; Profile/Location, Scheduling, Exposure, Storefront, Enquiry and Configuration ownership boundaries  
**Does not resolve:** `MS-PROT-056-V17-DQ-001` or any Commercial Entitlement identity/binding  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

**Product identity note:** inherited `Main Street` references identify the product currently named **GrandRue**. Stable `MS-*` and `mainstreet.*` identifiers remain unchanged pending separately governed migration.

---

# 1. Governing Decision

GrandRue SHALL make the already-accepted dated `BusinessOperatingOverride` model authoritatively mutable without collapsing it into stable weekly Business Hours.

For each exact:

```text
BusinessHoursScope
+
localDate
```

Business Hours SHALL retain:

```text
append-only dated-override revisions
        +
one durable current pointer
        ↓
current configured override
or
intentional absence after withdrawal
```

The resulting current override continues to participate in the existing MS-PROT-050 v1.3 resolution model:

```text
origin-date authority
+
entered-date override authority
        ↓
Effective Business Hours
```

This amendment creates no new kind of opening-hours rule.

It completes the mutation/currentness contract for a semantic object already admitted by MS-PROT-050 v1.1–v1.3.

---

# 2. Why This Amendment Is Required

Composite MS-PROT-050 already establishes that merchants may represent:

```text
holiday closure
special opening
shortened day
emergency closure
special cross-midnight opening
```

through `BusinessOperatingOverride`.

MS-PROT-050 v1.4 deliberately excludes those facts from stable weekly-hours mutation.

Therefore the accepted corpus currently determines:

```text
what a dated override means
```

but does not completely determine:

```text
how one is authoritatively created
how replacement preserves history
which revision is current
how concurrent edits conflict
how retries converge
who may commit the change
how withdrawal differs from a closed date
```

Those choices MUST NOT be invented by repository implementation or by the Commercial catalogue.

---

# 3. No New Feature Admission

This amendment does not admit a new merchant feature.

The feature already exists semantically.

The amendment supplies the missing owner-operation contract necessary to make that feature executable and commercially classifiable later.

## 3.1 Representation Test

**PASS**

A merchant must be able to distinguish:

```text
normal Monday hours
        ≠
Christmas Monday closed

normal Friday hours
        ≠
one Friday closes at 13:00

normal Saturday hours
        ≠
one Saturday opens specially
```

## 3.2 Coordination Test

**PASS**

The operation must coordinate:

```text
Business Hours Scope
Merchant Location where applicable
stable Business Hours currentness
Controller authority
time-zone basis
dated override history
Effective Business Hours resolution
```

without transferring ownership.

## 3.3 Administrative-Compression Test

**PASS**

The merchant should perform a business-native action such as:

```text
Closed on Christmas Day
```

or:

```text
Open 09:00–13:00 on 24 December
```

rather than understand configuration revisions, interval provenance or precedence.

## 3.4 ERP-Drift Test

**PASS**

The amendment does not create:

```text
generic calendar exception engine
rules DSL
holiday-management ERP
shift calendar
event scheduling framework
arbitrary temporal precedence language
```

---

# 4. Ownership Boundary

Business Hours owns:

```text
BusinessOperatingOverride
dated-override immutable revision history
dated-override currentness
scope/date-local mutation concurrency
dated-override retry identity
Effective Business Hours resolution
```

Existing authorities retain:

```text
Profile / Merchant Location
    profile and location truth

Scheduling
    scheduling evaluation and constraints

Appointment / Booking
    customer commitments

Storefront / Exposure
    audience presentation and exposure

Enquiry
    enquiry semantics

Commercial
    entitlement identity, binding and grant truth

Merchant Account / Controller
    account and controlling authority

Authentication / Session
    trusted execution context
```

Neither a dated override nor permission to mutate it creates authority in any neighbouring capability.

---

# 5. Business Operating Override Revision

One immutable revision conceptually contains:

```text
BusinessOperatingOverrideRevision
{
    revisionIdentity

    businessHoursScope
    localDate

    scopeDateRevisionNumber
    predecessorRevisionIdentity?

    disposition
        CONFIGURED
        WITHDRAWN

    effectiveOpenIntervals?    // CONFIGURED only
    optionalReason?            // descriptive only

    standardBusinessHoursRevisionAffinity?

    actorIdentity
    logicalRequestIdentity
    committedAt
}
```

The exact Java or relational representation remains implementation scope.

`standardBusinessHoursRevisionAffinity` preserves the stable-hours basis that existed when a configured override was committed.

It is historical provenance.

It does not cause an old stable schedule to override current Business Hours resolution.

---

# 6. Override Identity and Revision Sequence

Revision currentness is scoped by exactly:

```text
BusinessHoursScope
×
localDate
```

For each such pair:

```text
scopeDateRevisionNumber
```

begins at `1` and increases monotonically.

Revisions for:

```text
MERCHANT(M1), 2026-12-25
```

do not share a revision sequence with:

```text
MERCHANT(M1), 2026-12-24
```

or:

```text
MERCHANT_LOCATION(M1, L1), 2026-12-25
```

No merchant-global override version is introduced.

---

# 7. One Current Pointer per Scope and Date

For each scope/date that has ever received a committed override revision:

```text
CurrentBusinessOperatingOverride
{
    businessHoursScope
    localDate
    revisionIdentity
    scopeDateRevisionNumber
}
```

selects the current revision.

The pointer advances atomically with the revision it selects.

It never points at:

```text
uncommitted revision
different scope
different local date
```

Historical revisions remain immutable.

---

# 8. Configured Closed Date Is Not Withdrawal

This distinction is mandatory.

```text
CONFIGURED
effectiveOpenIntervals = []
```

means:

> an authoritative dated override exists and the represented operating date is closed.

By contrast:

```text
WITHDRAWN
```

means:

> no current dated override applies for that scope/date.

A withdrawn override therefore returns resolution to the ordinary MS-PROT-050 v1.3 rule:

```text
dated override absent
        ↓
ordinary weekly/origin-carry-over interpretation applies
```

These two outcomes MUST NOT be collapsed.

---

# 9. Configure or Replace Dated Override

The canonical owner operation is:

```text
business-hours/dated-override.configure@1
```

Conceptually:

```text
ConfigureBusinessOperatingOverride
{
    authenticatedActor
    businessHoursScope
    localDate

    expectedCurrentOverrideRevisionIdentity?

    logicalRequestIdentity

    effectiveOpenIntervals
    optionalReason?
}
```

## 9.1 First configuration

Where no current pointer exists:

```text
expected current override = absent
        ↓
append CONFIGURED revision 1
advance pointer
```

## 9.2 Replacement

Where current pointer is `Rn`:

```text
expected current override = Rn
        ↓
append CONFIGURED revision n+1
advance pointer
```

This applies whether `Rn` is currently:

```text
CONFIGURED
```

or:

```text
WITHDRAWN
```

A stale, omitted or incorrect expected revision fails closed.

---

# 10. Withdraw Dated Override

The canonical owner operation is:

```text
business-hours/dated-override.withdraw@1
```

Conceptually:

```text
WithdrawBusinessOperatingOverride
{
    authenticatedActor
    businessHoursScope
    localDate

    expectedCurrentOverrideRevisionIdentity

    logicalRequestIdentity
}
```

Withdrawal is valid only when the expected current revision exists and has:

```text
disposition = CONFIGURED
```

Success:

```text
append WITHDRAWN revision n+1
advance current pointer
```

It does not delete history.

It does not create:

```text
effectiveOpenIntervals = []
```

and therefore does not represent a closed date.

A new logical withdrawal against an already-current `WITHDRAWN` revision is rejected as no configured override being present.

Exact retry of the previously committed withdrawal remains recoverable under its logical request identity.

---

# 11. Stable Weekly Hours Are a Required Basis

A new or replacement dated override requires current authoritative:

```text
StandardBusinessHours
disposition = CONFIGURED
```

for the same exact `BusinessHoursScope`.

Therefore:

```text
no configured stable Business Hours
        ↓
no new dated override
```

This amendment does not create an independent one-off-hours system for a scope whose stable Business Hours are absent.

A future product need for one-off public operation without a stable Business Hours basis would require separate admission.

---

# 12. Time-Zone Basis

The dated override uses the IANA time zone of the current configured stable Business Hours for the exact scope at commit.

The committed override revision retains affinity to that stable revision for historical interpretation.

It does not define an independent mutable time-zone authority.

Canonical:

```text
current configured Standard Business Hours
        ↓
scope IANA time zone
        ↓
dated override localDate
and local-civil-time intervals
```

---

# 13. Stable-Hours Replacement with the Same Time Zone

Replacing stable weekly Business Hours while preserving the same IANA time zone does not invalidate current dated overrides.

Current configured dated overrides continue to replace the exact dates they govern.

Their stored stable-revision affinity remains provenance rather than precedence.

Therefore:

```text
change Monday weekly closing time
        ≠
automatically erase Christmas override
```

---

# 14. Time-Zone Change Requires Override Reconciliation

A stable weekly-hours replacement that changes the exact scope's IANA time zone MUST NOT silently reinterpret current or future dated overrides.

Before such a time-zone change may commit, no current `CONFIGURED` dated override may exist on or after the conservative reconciliation boundary date.

Define:

```text
oldCurrentDate
    = current date under current scope zone

newCurrentDate
    = current date under proposed scope zone

reconciliationBoundaryDate
    = earlier(oldCurrentDate, newCurrentDate)
```

If any current configured override exists where:

```text
override.localDate >= reconciliationBoundaryDate
```

the time-zone-changing stable-hours mutation fails with:

```text
BUSINESS_HOURS_OVERRIDE_RECONCILIATION_REQUIRED
```

or an exactly equivalent semantic outcome.

The merchant may then explicitly:

```text
withdraw
or
replace/re-establish
```

the affected dated overrides under the intended time basis.

GrandRue SHALL NOT silently move local dates or clock times across time zones.

---

# 15. Stable-Hours Withdrawal Requires Current/Future Override Reconciliation

`WithdrawStandardBusinessHours` MUST NOT make current/future dated overrides semantically orphaned.

Before stable weekly hours may be withdrawn, there must be no current `CONFIGURED` dated override whose:

```text
localDate >= current date
```

under the current Business Hours Scope time zone.

Otherwise:

```text
BUSINESS_HOURS_OVERRIDE_RECONCILIATION_REQUIRED
```

is returned.

Past override revisions remain immutable historical evidence and do not block withdrawal.

After stable-hours withdrawal:

```text
new dated override configuration
```

is unavailable until stable Business Hours are configured again.

---

# 16. Interval Semantics

A configured dated override's `effectiveOpenIntervals` MUST satisfy the already-accepted MS-PROT-050 interval rules.

This includes:

```text
valid local civil time
no invalid same-slice overlaps
closed date represented as []
legitimate cross-midnight interval support
```

No new interval type is created.

---

# 17. Cross-Midnight Resolution Remains v1.3 Authority

This amendment does not change MS-PROT-050 v1.3.

For example:

```text
Friday configured override:
21:00 → Saturday 04:00

Saturday override:
CLOSED
```

continues to resolve as:

```text
Friday portion
    follows Friday-origin override

Saturday entered-date portion
    is suppressed by Saturday override
```

The mutation contract only determines which dated override revision is current.

It does not redefine temporal precedence.

---

# 18. Override Reason Is Non-Controlling

`optionalReason` MAY retain merchant-facing descriptive context such as:

```text
Christmas Day
Bank holiday
Emergency closure
Private event
```

The reason MUST NOT control:

```text
open/closed calculation
Scheduling authority
legal consequence
priority
precedence
commercial permission
```

Changing a materially retained reason is a new revision, not an in-place edit.

No free-text reason becomes a rule language.

---

# 19. Actor Authority

Initial dated-override mutation requires:

```text
trusted authenticated execution context
+
exact Merchant Scope
+
current ACTIVE Merchant Controller
+
Merchant Account = OPEN
+
no effective account-wide Suspension
```

at commit.

Historical approval is not current authority.

A former Controller cannot commit after transfer.

A caller-supplied:

```text
isController = true
```

is not authority.

No delegated Workforce/staff privilege is introduced.

---

# 20. Merchant Location Scope

For:

```text
MERCHANT_LOCATION
```

scope, mutation additionally requires:

```text
current authoritative Merchant Location
+
same Merchant Scope
+
location not retired
```

An arbitrary location identifier is insufficient.

Location retirement remains MS-PROT-051 authority.

This amendment does not create or restore a Merchant Location.

---

# 21. Same-Scope/Date Concurrency

The current override pointer is the optimistic concurrency boundary for one exact:

```text
BusinessHoursScope
×
localDate
```

Two commands expecting the same current override revision cannot both commit different successors.

Different dates do not create artificial conflicts.

Different Business Hours Scopes do not create artificial conflicts.

The persistence mechanism may use:

```text
optimistic versioning
locking
compare-and-set
unique constraints
```

or another conforming mechanism.

Technology is implementation scope.

---

# 22. Logical Retry Identity

Every externally retryable mutation carries one logical request identity affined to:

```text
Merchant Scope
Business Hours Scope
localDate
```

Exact retry after commit returns the original committed revision even if a later revision is now current.

Reuse of the same logical request identity with materially different intent fails with:

```text
BUSINESS_HOURS_OVERRIDE_IDEMPOTENCY_CONFLICT
```

or equivalent.

Material intent includes at least:

```text
operation kind
Business Hours Scope
localDate
expected current override revision
effective intervals where configured
retained reason where supplied
```

Transport-attempt identity remains separate.

---

# 23. Atomic Commit Boundary

One successful override mutation atomically establishes:

```text
current actor/account revalidation
+
scope/location revalidation
+
current stable-hours basis
+
expected override-currentness proof
+
immutable override revision
+
logical retry evidence
+
current override pointer advancement
```

No downstream projection refresh, Storefront publication, Scheduling recalculation or Notification delivery is required inside the owner transaction.

Failure before commit leaves no partial override revision or pointer change.

---

# 24. Distinguishable Outcomes

The owner/application boundary MUST preserve materially different results including:

```text
SUCCESS
ALREADY_APPLIED

STANDARD_BUSINESS_HOURS_NOT_CONFIGURED

DATED_OVERRIDE_NOT_CONFIGURED
DATED_OVERRIDE_REVISION_CONFLICT
BUSINESS_HOURS_OVERRIDE_IDEMPOTENCY_CONFLICT
BUSINESS_HOURS_OVERRIDE_RECONCILIATION_REQUIRED

MERCHANT_LOCATION_NOT_FOUND
MERCHANT_LOCATION_RETIRED

AUTHENTICATION_REQUIRED
CURRENT_CONTROLLER_REQUIRED
MERCHANT_ACCOUNT_OPERATION_RESTRICTED

VALIDATION_REJECTION

TECHNICAL_FAILURE_BEFORE_COMMIT
EXECUTION_UNCERTAIN
```

Exact HTTP mapping remains downstream.

---

# 25. Projection and Exposure Boundary

A committed dated override:

```text
does not publish a Storefront
does not itself expose data publicly
does not mutate a projection
does not create a website
```

Current public Business Hours and `CurrentBusinessOperatingStatus` remain projected through accepted Projection/Exposure authority.

Canonical:

```text
current Business Hours source truth
        ↓
Effective Business Hours
        ↓
owner-qualified projection
        ↓
Exposure
        ↓
customer representation
```

Public observation does not acquire mutation authority.

---

# 26. Scheduling Boundary

A committed override may change the Business Hours evidence consumed by Scheduling.

It does not:

```text
create an Appointment
cancel an Appointment
cancel a Booking
release capacity
create ScheduleIntent
rewrite existing commitment history
```

Scheduling continues to consume the resolved Effective Business Hours result.

Existing customer commitments remain owned by their source capability.

---

# 27. Enquiry Boundary

Enquiry remains digitally independent from Business Hours.

Changing a dated override may alter truthful acknowledgement context such as:

```text
currently closed
next open tomorrow
```

It does not disable an otherwise eligible Enquiry interaction.

---

# 28. Historical Interpretation

Earlier override revisions remain immutable.

A later correction does not rewrite the fact that an earlier revision once governed.

Where a historical consumer needs to reconstruct the authoritative interpretation that applied at an earlier time, it MUST use the applicable retained revision/provenance rather than applying today's current override retrospectively without a governing correction rule.

This amendment does not create a general time-travel query framework.

---

# 29. Data Lifecycle

This amendment creates no universal retention period.

Override revision/provenance retention and eventual disposition remain subject to composite MS-PROT-053 and any independently accepted evidence obligations.

Retention does not transfer Business Hours ownership.

---

# 30. Commercial Boundary

This amendment defines owner operations only.

It does **not** determine:

```text
FREE entitlement identity
BUSINESS entitlement identity
GROWTH entitlement identity
protected Commercial purpose
Commercial Access Binding
plan grant set
```

Acceptance therefore MUST NOT be interpreted as:

```text
dated override operation
    automatically commercially granted
```

The subsequent MS-PROT-050 commercial-access classification must explicitly classify both:

```text
stable weekly Business Hours access
dated override access
```

before the complete FREE Public Business Hours allocation can participate in `MS-PROT-056-V17-DQ-001`.

---

# 31. AI Boundary

AI MAY assist the merchant by translating ordinary language such as:

```text
We're closed on Christmas Day.

Next Friday we close at one.

We're open 10 till 4 this bank holiday.
```

into a candidate dated override.

AI MUST NOT:

```text
commit the override
invent the date
invent opening times
choose a Business Hours Scope silently
bypass Controller authority
bypass expected-revision conflict
infer a time zone
invent precedence
silently reinterpret a time-zone change
```

The authoritative operation remains deterministic.

AI unavailability MUST NOT make ordinary deterministic Business Hours editing impossible.

---

# 32. Low-Software-Capacity Merchant Test

The merchant-facing model remains:

```text
Normal hours
+
special date
```

not:

```text
revision graph
current pointer
optimistic concurrency token
origin-date carry-over
entered-date precedence
idempotency identity
IANA reconciliation
```

GrandRue absorbs that complexity internally.

**Result: PASS**

---

# 33. Alternatives Considered

## A. Mutable one-row-per-date override

**REJECTED**

It loses authoritative history and makes lost-acknowledgement recovery ambiguous.

## B. Put exceptions inside stable weekly revision

**REJECTED**

It contradicts the accepted stable-versus-effective-dated lifecycle split and creates configuration churn for temporary facts.

## C. Use Calendar or ScheduleIntent as the override owner

**REJECTED**

Calendar is projection and ScheduleIntent is scheduling-operational intent; neither owns public operating-hours truth.

## D. Generic temporal-rule DSL

**REJECTED**

It creates unnecessary abstraction and merchant-facing software burden.

## E. Append-only scope/date override revisions with one current pointer

**SELECTED**

It is the smallest model that preserves currentness, history, conflict safety, retry convergence and existing temporal semantics.

---

# 34. Falsification

## 34.1 Christmas closure

```text
normal Wednesday open
25 December override = []
```

Result:

```text
current dated override CONFIGURED
date resolves CLOSED
```

**PASS**

## 34.2 Withdrawal of mistaken closure

Current override:

```text
CONFIGURED []
```

merchant withdraws it.

Result:

```text
WITHDRAWN revision
ordinary weekly/carry-over authority resumes
```

No fake empty-open schedule is created.

**PASS**

## 34.3 Reconfigure after withdrawal

Current revision is `WITHDRAWN`.

Merchant deliberately configures a new exception using the exact expected withdrawn revision.

A new `CONFIGURED` successor is appended.

**PASS**

## 34.4 Same-date concurrent edits

Two current Controllers submit different replacements against the same expected override revision.

At most one commits.

The other receives revision conflict.

**PASS**

## 34.5 Different dates

A Christmas edit and Boxing Day edit do not conflict merely because they belong to the same Merchant Scope.

**PASS**

## 34.6 Different locations

Swansea and Cardiff overrides remain scope-independent.

**PASS**

## 34.7 Controller transfer race

Former Controller begins an edit.

Controller transfer commits first.

Former Controller cannot commit the override.

**PASS**

## 34.8 Lost acknowledgement

Override commit succeeds but response is lost.

Exact logical retry returns the original revision and does not create another override.

**PASS**

## 34.9 Changed retry intent

Same logical request identity is reused with different intervals.

Identity reuse is rejected.

**PASS**

## 34.10 Cross-midnight special opening

Friday override:

```text
21:00 → Saturday 04:00
```

Saturday has no override.

v1.3 carry-over semantics remain valid.

**PASS**

## 34.11 Entered-date closure

Friday special override crosses into Saturday.

Saturday override is configured closed.

Saturday override suppresses entered-date carry-over exactly as v1.3 requires.

**PASS**

## 34.12 Stable schedule changes, same time zone

Merchant changes ordinary Friday weekly hours but retains `Europe/London`.

Existing Christmas override remains current.

**PASS**

## 34.13 Time-zone change with future override

Current zone:

```text
Europe/London
```

future dated override exists.

Merchant attempts to replace stable hours with:

```text
America/New_York
```

Result:

```text
BUSINESS_HOURS_OVERRIDE_RECONCILIATION_REQUIRED
```

No silent reinterpretation.

**PASS**

## 34.14 Stable-hours withdrawal with future override

Future configured exception exists.

Merchant attempts stable-hours withdrawal.

Result:

```text
reconciliation required
```

Merchant resolves future overrides first.

**PASS**

## 34.15 Past override after stable withdrawal

Old override revisions remain historical evidence and do not block stable-hours withdrawal once no current/future configured exception remains.

**PASS**

## 34.16 AI outage

Merchant can still deterministically create, replace and withdraw supported exceptions.

**PASS**

---

# 35. Hard Invariants

1. `BusinessOperatingOverride` remains Business Hours-owned.
2. Stable weekly hours and dated overrides remain separate lifecycle classes.
3. Override currentness is keyed by exact `BusinessHoursScope × localDate`.
4. Every mutation appends an immutable revision.
5. One durable current pointer selects the current revision for a scope/date.
6. `CONFIGURED []` means authoritative closed date.
7. `WITHDRAWN` means no current override.
8. Withdrawal never deletes history.
9. First configuration and replacement use exact expected-current semantics.
10. Same-scope/date stale writes fail closed.
11. Different dates do not share one artificial version.
12. Different scopes do not share one artificial version.
13. Exact logical retry converges on the original result.
14. Changed intent under one request identity conflicts.
15. Current authenticated Controller authority is revalidated at commit.
16. Merchant Location scope requires a current same-merchant Location.
17. New/replacement override requires current configured stable Business Hours.
18. A configured override retains its stable-hours revision affinity for historical provenance.
19. Same-zone stable-hours replacement does not erase dated overrides.
20. Time-zone-changing stable-hours replacement cannot silently reinterpret current/future overrides.
21. Stable-hours withdrawal cannot orphan current/future configured overrides.
22. Cross-midnight precedence remains governed solely by v1.3.
23. Override reason text never controls semantics.
24. Override mutation does not itself publish or expose a Storefront.
25. Override mutation does not mutate Scheduling or customer commitments.
26. Enquiry remains independent of open/closed state.
27. AI assistance is optional and non-authoritative.
28. Acceptance creates no Commercial Entitlement or catalogue grant.
29. Implementation activation remains NONE.

---

# 36. Catalogue Consequence

This amendment removes one prerequisite discovered by the `MS-PROT-056-V17-DQ-001` completeness audit:

```text
dated Public Business Hours
semantic meaning
        +
exact owner mutation/currentness
        = complete owner-side service mechanics
```

It does **not** close the FREE Business Hours catalogue blocker by itself.

The next Business Hours design node becomes:

```text
MS-PROT-050 v1.6
Public Business Hours Commercial Access Contracts
```

That amendment may then classify the exact stable-hours and dated-override access points without inventing undefined operations.

`MS-PROT-056-V17-DQ-001` remains OPEN.

---

# 37. Implementation-Rules Impact

**Expected impact: NONE.**

This authority changes substantive Business Hours semantics but does not require a new generic implementation rule.

Any later implementation remains subject to the existing implementation rules, including explicit imports and tests-first behaviour when implementation activation is separately authorised.

No implementation is activated by acceptance of this design.

---

# 38. Corpus Conformance

The amendment preserves:

```text
MS-PROT-050 v1.2
    scope separation and no implicit inheritance

MS-PROT-050 v1.3
    origin-date and entered-date override authority

MS-PROT-050 v1.4
    immutable stable weekly revisions

MS-PROT-051
    Profile / Merchant Location ownership

MS-PROT-042
    Appointment / Booking ownership

MS-PROT-043
    Enquiry independence

MS-PROT-027 / 036 / 049 / 094
    projection, exposure and presentation ownership

MS-PROT-056
    Commercial ownership and DQ-001 catalogue discipline
```

No contradictory semantic owner is introduced.

---

# 39. Governance Outcome

**Fundamental Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

The internal revision/currentness machinery is more complex than a mutable holiday-hours row, but that complexity protects the merchant from:

```text
stale overwrites
duplicate retries
hidden precedence
time-zone reinterpretation
lost history
cross-location leakage
```

while the merchant continues to express ordinary business facts in ordinary language.

**Review:** PASS  
**Falsification:** PASS  
**Ambiguity review:** PASS  
**Corpus-conformance review:** PASS within the accepted scope  
**Recommendation:** ACCEPT

---

# 40. Acceptance Statement

> **GrandRue shall represent dated public operating exceptions as immutable scope-and-date-affined Business Hours revisions with one current pointer, exact Controller authority, optimistic concurrency and retry-safe mutation. A configured closed date remains distinct from withdrawal; time-zone changes and stable-hours withdrawal may not silently orphan or reinterpret current or future exceptions. Dated overrides remain separate from Scheduling, Storefront, Enquiry and Commercial authority.**
