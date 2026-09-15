# MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model

**Document ID:** MS-PROT-040
**Version:** 1.0
**Status:** **Accepted**
**Depends on:** MS-PROT-020, MS-PROT-021, MS-PROT-022, MS-PROT-023, MS-PROT-027, MS-PROT-036, MS-PROT-037, MS-PROT-038, MS-PROT-039
**Purpose:** Define the strict lifecycle through which a candidate merchant configuration is validated, explained, approved, activated, modified, superseded, and—where appropriate—reinstated without exposing Main Street's semantic graph, permitting inference to activate behaviour autonomously, producing partially active configurations, or silently changing existing business commitments.

---

# 1. Governing principle

> **A merchant configuration change does not become operational merely because it has been inferred, edited, saved, or compiled successfully. It becomes operational only after the applicable validation, merchant-authority, impact-review and activation requirements have been satisfied.**

Canonical lifecycle:

```text
Current Active Revision
        │
        │ proposed change
        ▼
Configuration Change Set
        │
        ▼
Candidate Revision
        │
        ▼
Compiler Validation
        │
        ▼
Impact Analysis
        │
        ▼
Merchant Review / Approval
        │
        ▼
Atomic Activation
        │
        ├── previous revision → SUPERSEDED
        │
        └── new revision      → ACTIVE
```

No intermediate state may become partially authoritative.

---

# 2. Architectural objectives

MS-PROT-040 shall guarantee:

1. deterministic configuration activation;
2. explicit ownership of merchant choices;
3. complete configuration revision traceability;
4. atomic change between active revisions;
5. preservation of existing commitments;
6. no AI/inference self-activation;
7. no silent semantic expansion;
8. meaningful merchant-facing change review;
9. safe capability deactivation;
10. safe configuration concurrency;
11. coherent storefront/dashboard behaviour;
12. ability to understand which configuration governed a historical operation.

---

# 3. Scope

This document governs:

```text
configuration revision
configuration change set
candidate configuration
validation
impact analysis
merchant review
merchant approval
activation
supersession
configuration provenance
configuration conflict
configuration rollback/reinstatement
capability deactivation
existing commitment treatment
configuration concurrency
```

It does not define:

```text
database technology
transaction implementation
frontend framework
API transport
event broker
audit product
configuration UI styling
Git workflow
payment-provider reconciliation
legal approval workflow
```

---

# 4. Fundamental concepts

The following concepts shall remain distinct.

```text
CONFIGURATION DEFINITION
    supported structure of merchant configuration

CONFIGURATION REVISION
    one coherent resolved merchant configuration version

CHANGE SET
    merchant/inference/platform proposal to modify a revision

CANDIDATE REVISION
    proposed resulting configuration

VALIDATION
    determination that candidate structure is semantically valid

IMPACT ANALYSIS
    determination of consequential effects of activation

APPROVAL
    authorised acceptance of applicable merchant-owned effects

ACTIVATION
    making one validated revision authoritative for new operations

SUPERSESSION
    replacement of a previously active revision

REINSTATEMENT
    making a previous revision active again after current validation
```

Hard invariant:

> **Validation ≠ Approval ≠ Activation.**

---

# 5. Configuration revision

A configuration revision represents one coherent merchant operating configuration.

Conceptually:

```text
ConfigurationRevision
{
    revision_identity
    merchant_scope
    base_revision?
    semantic_registry_context
    configuration
    provenance
    lifecycle_status
}
```

A revision is not runtime business state.

It does not contain:

```text
current orders
current bookings
inventory movements
customer messages
payment transactions
live allocations
```

Those remain capability-owned operational state.

---

# 6. Revision identity

Every materialised configuration revision shall have a stable identity.

Conceptually:

```text
Merchant M123

Revision C17
Revision C18
Revision C19
```

The identity shall permit Main Street to answer:

> Which merchant configuration governed this operation?

This is essential for:

* historical explanation;
* existing commitments;
* support;
* debugging;
* audit/provenance;
* safe configuration evolution.

---

# 7. Lifecycle states

Minimum lifecycle:

```text
CANDIDATE
    ↓
VALIDATED
    ↓
APPROVED
    ↓
ACTIVE
    ↓
SUPERSEDED
```

Additional failure classifications may exist without becoming lifecycle states:

```text
VALIDATION_FAILED
APPROVAL_REQUIRED
ACTIVATION_CONFLICT
```

`APPROVED` may be implicit in an appropriately explicit merchant action as defined later.

---

# 8. CANDIDATE

A candidate revision:

* is not authoritative;
* may not govern new operations;
* may not change customer behaviour;
* may not alter dashboard authority;
* may not affect live scheduling;
* may not modify existing commitments merely by existing.

Inference may create a candidate.

Merchant editing may create a candidate.

Neither activates it.

---

# 9. VALIDATED

A candidate becomes validated only after the compiler confirms that it satisfies all structural and semantic requirements.

Validation shall include, where applicable:

```text
semantic references exist
semantic ownership is valid
dependencies resolve
policy values are permitted
conflicts are absent
requirements are structurally valid
capability relationships are registered
platform invariants remain satisfied
configuration scope is correct
```

Validation answers:

> **Could Main Street execute this configuration safely as a supported configuration?**

It does not answer:

> Has the merchant agreed to operate this way?

---

# 10. APPROVED

Approval means that the authorised merchant controller or appropriately privileged merchant actor has accepted the consequential merchant-owned configuration effects.

Approval does not permit the merchant to approve:

```text
invalid semantics
platform-invariant violations
unsupported policy values
unauthorised capability relationships
security bypasses
```

Approval is authority over legitimate merchant choices only.

---

# 11. ACTIVE

An active revision is the configuration revision authoritative for applicable **new business activity** within its scope.

At the merchant level:

```text
Merchant
    ↓
Active Configuration Revision = C19
```

There shall not normally be two competing active revisions for the same new-operation configuration scope.

Existing commitments may still retain affinity to older revisions.

That is not equivalent to having multiple active configurations for new activity.

---

# 12. SUPERSEDED

A superseded revision is no longer authoritative for initiating new business operations.

It remains historically meaningful.

It must not be treated as deleted merely because another revision replaced it.

Main Street may need it to:

* interpret existing commitments;
* reconstruct historical decisions;
* support customer service;
* explain prior configuration;
* potentially evaluate reinstatement.

---

# 13. Change set

A change set describes the intended delta from a known base revision.

Conceptually:

```text
ChangeSet
{
    merchant_scope
    base_revision
    additions
    removals
    modifications
    provenance
}
```

Examples:

```text
enable customer booking

change scheduling mode:
MERCHANT_PROPOSES_OR_CONFIRMS
→ CUSTOMER_SELECTS_AVAILABLE_TIME

change working hours:
09:00–17:00
→ 10:00–18:00

disable delivery

enable consultation

change payment policy

disable customer cancellation
```

---

# 14. Change-set provenance

Every change shall identify its origin.

Minimum origins:

```text
MERCHANT_INITIATED
INFERENCE_PROPOSED
REGISTERED_DERIVATION
PLATFORM_REQUIRED
```

These origins have different authority semantics.

---

# 15. Merchant-initiated changes

A merchant may directly request a supported configuration change.

Example:

```text
Merchant changes:

Tuesday working hours
09:00–17:00

to

10:00–18:00
```

The merchant action expresses intent.

It does not bypass:

```text
compiler validation
impact analysis
existing-commitment protection
authority validation
```

---

# 16. Inference-proposed changes

Inference may propose:

```text
enable Consultation
enable Scheduling
enable Booking
```

It may not activate them.

Canonical flow:

```text
Inference
    ↓
Proposed Change Set
    ↓
Validation
    ↓
Merchant-facing explanation
    ↓
Merchant approves/rejects/corrects
```

Hard invariant:

> **Inference shall never constitute merchant approval.**

---

# 17. Registered derivation

Some configuration effects follow automatically from semantics already selected.

Example:

```text
Mobile Service selected
        ↓
registered semantics
        ↓
Service Location requirement applicable
```

The derived requirement is not an independent merchant choice.

The merchant need not approve it as though it were optional.

However, consequential derived effects should be explained where necessary for informed review.

---

# 18. Platform-required behaviour

Platform invariants are not merchant-selectable configuration.

A merchant cannot reject:

```text
tenant isolation
authorisation invariants
capacity integrity
configuration type safety
compiler rules
security invariants
```

Main Street shall not misrepresent these as optional configuration.

A future platform change that makes an existing configuration unsupported requires an explicit compatibility/migration mechanism.

It must not silently rewrite merchant-owned choices under the guise of validation.

Detailed platform migration policy remains outside this document.

---

# 19. Candidate generation

A change set is applied conceptually to its base revision:

```text
Active C18
   +
Change Set Δ19
   ↓
Candidate C19
```

The resulting candidate must represent a complete coherent configuration, not merely isolated changed fields.

This allows the compiler to validate the resulting operating model as a whole.

---

# 20. Configuration diff

Main Street shall retain a meaningful difference between:

```text
base revision
candidate revision
```

The diff should distinguish at least:

```text
capability activation/deactivation
policy changes
scheduling changes
requirement changes
access changes
customer-interaction changes
public-exposure changes
resource/capacity configuration changes
integration-related changes
```

This diff is the basis for impact analysis and review.

---

# 21. Impact analysis

A structurally valid configuration can still have significant operational consequences.

Therefore compiler validation alone is insufficient.

Impact analysis asks:

> **What changes if this revision becomes active?**

Potential impact categories include:

```text
customer behaviour
merchant workflow
scheduling availability
public/storefront capability
dashboard surface
required customer information
existing commitments
staff authority
payment behaviour
notifications
resource/capacity participation
```

---

# 22. Impact classifications

Impact findings should be classified conceptually as:

```text
BLOCKING
CONSEQUENTIAL
INFORMATIONAL
EXISTING_COMMITMENT_CONFLICT
```

### BLOCKING

Activation cannot proceed.

Examples:

```text
invalid policy
unresolved dependency
unsupported semantic combination
missing mandatory configuration
```

### CONSEQUENTIAL

Activation is valid but materially changes merchant/customer behaviour.

Examples:

```text
enable customer self-booking
disable delivery
require payment before confirmation
disable customer cancellation
change working hours
```

### INFORMATIONAL

Valid change with limited operational consequence.

### EXISTING_COMMITMENT_CONFLICT

New configuration differs from conditions under which existing commitments were established.

This conflict does not automatically invalidate those commitments.

---

# 23. Existing commitments are protected

Hard invariant:

> **Activating a new configuration revision shall not silently reinterpret, cancel, move, invalidate or rewrite an existing business commitment.**

Example:

```text
Existing appointment
Tuesday 16:00–17:00

New configuration
Tuesday closes at 16:00
```

Required behaviour:

```text
new bookings after activation:
    cannot use 16:00–17:00

existing appointment:
    remains valid business commitment

merchant:
    informed of conflict
```

Main Street may require the merchant to resolve the existing commitment separately.

---

# 24. Existing commitments retain semantic affinity

Where an existing commitment depends materially on the configuration under which it was created, Main Street must retain enough configuration provenance to operate it correctly.

Conceptually:

```text
Booking B123
    created_under = Configuration C18
```

New merchant configuration:

```text
Active = C19
```

B123 is not automatically reinterpreted under C19.

This applies particularly to:

```text
cancellation policy
payment timing
scheduling semantics
fulfilment mode
allocation policy
customer commitments
```

---

# 25. Current platform invariants remain authoritative

Configuration affinity does not freeze obsolete platform safety rules.

An old booking does not gain permission to violate a current non-configurable invariant.

Therefore:

```text
historical merchant policy
        ≠
historical platform safety authority
```

Existing commitments preserve relevant merchant semantics, while current platform invariants remain enforced.

---

# 26. Capability deactivation

Disabling a capability for future use does not necessarily eliminate all operational responsibility associated with it.

Example:

```text
Merchant disables Booking
```

After activation:

```text
new customer bookings
    prohibited

existing bookings
    remain manageable
```

Therefore:

> **Capability deactivation stops applicable new activity; it does not abandon outstanding obligations.**

---

# 27. Residual management surface

If a deactivated capability still has outstanding commitments, the merchant dashboard may retain the minimum operational surface required to manage those commitments.

Example:

```text
Booking capability:
    disabled for new bookings

Existing future bookings:
    7
```

Dashboard may continue showing:

```text
Bookings
```

for management of those seven commitments.

It must not necessarily continue exposing:

```text
Create new booking
Enable customer booking
new-booking availability
```

This extends the MS-PROT-037 minimum-surface principle.

---

# 28. Capability retirement completion

A capability may become fully absent from the merchant operating surface once:

```text
no applicable outstanding commitments remain
no operational obligations remain
no required historical management surface remains
```

Historical records may remain accessible through appropriate history/audit surfaces.

---

# 29. Scheduling configuration change

MS-PROT-039 establishes:

```text
working hours
recurring breaks
```

as non-bypassable scheduling configuration.

Changing them creates a configuration change.

Example:

```text
Old:
Monday–Friday 09:00–17:00

New:
Monday–Friday 08:00–16:00
```

Impact analysis shall examine future customer commitments potentially outside the new valid scheduling surface.

---

# 30. Scheduling-change activation

Suppose:

```text
Existing appointment:
Friday 16:00–17:00

Candidate configuration:
Friday working hours end 16:00
```

The candidate may remain valid for new scheduling.

Impact report:

```text
EXISTING_COMMITMENT_CONFLICT

1 existing appointment falls outside
the proposed working hours.
```

The merchant must be informed before activation.

Activation shall not automatically move or cancel that appointment.

---

# 31. Merchant-created unavailability is not a configuration revision

A merchant creating:

```text
UNAVAILABLE
"Dentist appointment"
Tuesday 14:00–15:30
```

is manipulating operational schedule information.

It is not changing:

```text
working hours
regular breaks
scheduling mode
```

Therefore it does not require a new merchant configuration revision.

This distinction is strict:

```text
recurring scheduling rules
    → configuration

specific schedule intent
    → operational calendar data
```

---

# 32. Customer appointment manipulation is not configuration

Similarly:

```text
reschedule Customer A
14:00 → 15:00
```

is an operational command.

It is not a configuration change.

It remains subject to the active scheduling configuration and runtime invariants.

---

# 33. Configuration approval presentation

Merchants shall review business effects, not semantic internals.

Example:

```text
Proposed change

Customer appointments
─────────────────────

Customers will be able to choose available
appointment times themselves.

Working hours:
Mon–Fri 09:00–17:00

Regular break:
12:30–13:30

Main Street will automatically prevent bookings:
• outside working hours
• during your regular break
• during blocked calendar periods
• where required capacity is unavailable
```

Do not expose:

```text
CapabilityNode:SCHEDULING
PolicyValue:AUTO_ACCEPT_VALID_SELECTION
RequirementGraph
ConfigurationIR
```

---

# 34. Consequential change review

A change is consequential when it materially alters how customers, merchant staff or Main Street may operate.

Examples:

```text
activate/deactivate capability
change customer booking authority
change payment requirement
change cancellation policy
change delivery availability
change working hours
change access policy
change public exposure
```

Consequential changes require an intelligible effect summary before activation.

---

# 35. Merchant direct action may constitute approval

Main Street shall avoid unnecessary double confirmation.

When the merchant explicitly changes a configuration setting through an authoritative configuration interface and is shown the applicable impact before committing the change, the final:

```text
Apply
Save & Activate
Confirm Change
```

action may constitute approval.

A second ceremonial approval step is not required merely to satisfy the lifecycle model.

Therefore:

```text
merchant intent
+
impact visibility
+
authorised commit action
=
approval
```

where appropriate.

---

# 36. Inference requires separate approval

This shortcut does **not** apply to inference-generated configuration.

Example:

```text
Inference proposes:
"Enable paid consultations?"
```

Inference cannot press:

```text
Apply
```

on the merchant's behalf.

The merchant must explicitly accept the proposal.

---

# 37. Approval authority

Configuration approval requires an actor with sufficient authority for the affected merchant scope.

A staff member permitted only to:

```text
manage bookings
```

shall not automatically gain permission to:

```text
enable payment
disable customer booking
change merchant working hours
change staff authority
```

Configuration authority must remain contextual.

---

# 38. Activation is atomic

A validated and approved configuration revision shall become authoritative atomically.

Forbidden state:

```text
Booking = C19
Scheduling = C18
Payment = C19
Dashboard = half C18 / half C19
```

Canonical activation:

```text
ACTIVE = C18
        ↓
atomic revision switch
        ↓
ACTIVE = C19
SUPERSEDED = C18
```

The implementation mechanism is deferred.

The semantic requirement is not.

---

# 39. Commands see one coherent configuration

A runtime command must execute against one coherent operational model revision.

Forbidden:

```text
command begins under C18
        ↓
configuration activates C19
        ↓
half command evaluated using C19
```

Instead:

```text
Command
    ↓
bind to coherent operational model revision
    ↓
evaluate/execute
```

This preserves MS-PROT-022.

---

# 40. New commands after activation

Once C19 becomes active:

```text
new operation
    ↓
C19
```

Commands already bound to C18 may complete under the appropriate coherent execution boundary.

Existing long-running commitments retain applicable provenance as described above.

---

# 41. Storefront recomposition

Configuration activation may change the public storefront.

Examples:

```text
enable Booking
    → Book action appears

disable Delivery
    → Delivery option disappears

enable Publication
    → publication surfaces appear
```

Storefront projections may be asynchronously rebuilt.

However, stale presentation must never grant behaviour prohibited by the active backend configuration.

Example:

```text
old cached page still displays "Book"
        ↓
customer clicks
        ↓
backend evaluates active C19
        ↓
Booking disabled
        ↓
operation rejected safely
```

Presentation is not semantic authority.

---

# 42. Dashboard recomposition

The dashboard shall recompute from active configuration and operational obligations.

Example:

```text
Publication + Enquiry
        ↓
merchant enables Consultation + Booking
        ↓
dashboard adds only required new surfaces
```

If a capability is disabled but still has outstanding commitments, residual management surfaces may remain as defined earlier.

---

# 43. Concurrent configuration edits

Main Street must prevent lost configuration updates.

Example:

```text
Owner A opens C18
Owner B opens C18

Owner A activates C19

Owner B attempts to activate
candidate based on C18
```

Main Street shall detect that B's base revision is no longer active.

It must not silently overwrite C19.

---

# 44. Base-revision concurrency invariant

Every configuration change set shall identify the revision against which it was prepared.

Before activation:

```text
candidate.base_revision
        ==
current active revision
```

must be evaluated.

If not:

```text
ACTIVATION_CONFLICT
```

Main Street shall require the changes to be reconciled/rebased/revalidated against the current active configuration.

Exact concurrency mechanism is implementation-specific.

---

# 45. Automatic merge is not assumed

Main Street shall not automatically merge concurrent configuration changes merely because they modify apparently different fields.

Semantic dependencies can make independent-looking changes interact.

Example:

```text
A enables Customer Booking

B disables Scheduling
```

A field-level merge could produce an invalid configuration.

Therefore the resulting combined configuration must always pass complete semantic validation.

---

# 46. Configuration reinstatement

A previously superseded revision may be considered for reinstatement.

However:

> **Reinstatement is a new activation decision, not time travel.**

C17 cannot simply become active because it was once valid.

It must be evaluated against:

```text
current semantic registry
current platform invariants
current merchant scope
current dependency requirements
current compatibility constraints
```

---

# 47. Reinstatement does not undo business history

Suppose:

```text
C18 enabled Delivery
orders were delivered

C19 disabled Delivery
```

Reinstating C18 does not:

```text
undo C19
reverse orders
recreate deleted operational state
reissue notifications
reverse payments
```

It merely makes an equivalent valid configuration authoritative for applicable future operations.

---

# 48. Configuration rollback terminology

The term `rollback` shall be used carefully.

Database-style rollback implies that effects disappear.

That is not true for merchant business history.

Prefer:

```text
REINSTATE PRIOR CONFIGURATION
```

rather than suggesting:

```text
UNDO EVERYTHING SINCE REVISION
```

---

# 49. Configuration history

Main Street shall preserve enough configuration history to determine:

```text
which revision existed
when it became active
which revision it replaced
who/what proposed the change
who approved it where required
what impact was identified
why activation succeeded/failed
```

This is configuration provenance.

It does not replace the platform's separate audit model.

---

# 50. Configuration event versus business event

A configuration activation may produce configuration/audit evidence.

It shall not be confused with business domain events such as:

```text
BookingConfirmed
OrderAccepted
AppointmentCancelled
PaymentReceived
```

Configuration change is platform/merchant operating-model history.

Domain events describe business facts within capabilities.

---

# 51. Initial onboarding activation

Initial onboarding follows the same lifecycle.

```text
Onboarding answers
        ↓
Inference
        ↓
Candidate C1
        ↓
Compiler validation
        ↓
Impact/business summary
        ↓
Merchant review
        ↓
Merchant approval
        ↓
C1 ACTIVE
```

There is no special path by which onboarding inference bypasses normal configuration authority.

---

# 52. Information publisher validation

Initial configuration:

```text
Publication
Classification/Search
Enquiry
Subscription
```

Later merchant enables:

```text
Consultation
Scheduling
Booking
Payment
```

Process:

```text
C4
 +
change set
 ↓
C5 candidate
 ↓
validate
 ↓
show new customer/merchant behaviour
 ↓
merchant approves
 ↓
C5 active
```

Existing publications remain unaffected.

Dashboard and storefront expand minimally.

**PASS**

---

# 53. Consultant scheduling-mode change validation

Initial:

```text
Consultation
Scheduling mode =
MERCHANT_PROPOSES_OR_CONFIRMS
```

Merchant changes to:

```text
CUSTOMER_SELECTS_AVAILABLE_TIME
```

Impact report must explain:

```text
Customers will now see valid appointment availability
and may create appointments without individual
merchant confirmation.
```

This is consequential.

Working hours/break configuration must already be valid.

Existing appointments remain unchanged.

**PASS**

---

# 54. Working-hours validation

Current:

```text
09:00–17:00
```

Future appointments include:

```text
Tuesday 16:30–17:00
Friday 16:00–17:00
```

Candidate:

```text
09:00–16:00
```

Impact:

```text
EXISTING_COMMITMENT_CONFLICT

2 appointments fall outside
the proposed future scheduling surface.
```

Activation may proceed after proper review if structurally valid.

Existing commitments remain.

New scheduling uses 09:00–16:00.

**PASS**

---

# 55. Grocery delivery deactivation validation

Current:

```text
Ordering
Delivery
Collection
```

Merchant disables:

```text
Delivery
```

Existing delivery orders:

```text
Order D101
Order D102
```

After activation:

```text
new orders:
    delivery unavailable

existing D101/D102:
    delivery obligation remains

dashboard:
    retains required fulfilment surfaces
```

Delivery capability cannot simply disappear operationally while commitments remain.

**PASS**

---

# 56. Booking capability deactivation validation

Merchant has:

```text
12 future customer bookings
```

Merchant disables new Booking.

Correct:

```text
new customer booking = prohibited

12 existing bookings = retained

merchant can:
    view
    manage
    cancel/reschedule where permitted
    complete
```

Incorrect:

```text
Booking page disappears completely
and commitments become inaccessible
```

**PASS**

---

# 57. Concurrent administrator validation

Owner and manager both begin editing C10.

Owner activates C11.

Manager attempts candidate C12 based on C10.

Required result:

```text
ACTIVATION_CONFLICT
```

Manager must reconcile against C11.

No lost update.

**PASS**

---

# 58. Inference autonomy validation

Inference detects:

> "Merchant appears to offer consultations."

It proposes Consultation capability.

Forbidden:

```text
Inference
    ↓
activate Consultation automatically
```

Required:

```text
Inference
    ↓
candidate change
    ↓
merchant review
    ↓
merchant approval
```

**PASS**

---

# 59. Reinstatement validation

Merchant:

```text
C20 = Booking enabled

C21 = Booking disabled
```

Later merchant wants prior behaviour.

Main Street may construct/revalidate:

```text
candidate equivalent to C20
```

If valid under current platform semantics:

```text
activate as new effective revision
```

Existing history remains unchanged.

**PASS**

---

# 60. Falsification review

The model was challenged against the following assumptions.

| Failed assumption                                                       | Reason rejected                                                    |
| ----------------------------------------------------------------------- | ------------------------------------------------------------------ |
| Validated means active                                                  | Validation establishes correctness, not authority                  |
| AI may activate high-confidence inference                               | Violates merchant authority                                        |
| Merchant save can bypass compiler                                       | Allows invalid configuration                                       |
| Every change requires a separate approval ceremony                      | Creates unnecessary friction                                       |
| A merchant direct authorised commit can never constitute approval       | False; explicit reviewed action may be approval                    |
| New configuration governs all historical operations immediately         | Silently changes existing commitments                              |
| Disabling capability deletes existing obligations                       | Violates business commitments                                      |
| Disabled capability should disappear completely from dashboard          | Existing obligations may still require management                  |
| Changing working hours invalidates old appointments                     | Configuration governs new availability, not historical commitments |
| Calendar unavailability is a configuration revision                     | It is operational schedule intent                                  |
| Rescheduling customer appointment is configuration                      | It is operational state manipulation                               |
| Activation can be field-by-field                                        | Produces incoherent runtime models                                 |
| Concurrent edits can use last-write-wins                                | Can silently discard merchant decisions                            |
| Different-field edits are always safe to merge                          | Semantic dependencies may interact                                 |
| Old configuration can be restored without validation                    | Semantic registry/platform may have changed                        |
| Rollback reverses business history                                      | Configuration reinstatement cannot undo domain effects             |
| UI freshness determines configuration authority                         | Backend active revision remains authoritative                      |
| A deactivated capability need not remain executable for old commitments | Outstanding obligations disprove it                                |

No remaining falsifier requires revision of the proposed boundary.

---

# 61. Accepted invariants

1. A merchant has one coherent active configuration revision for applicable new operations within a configuration scope.
2. Candidate configuration is not operational authority.
3. Validation, approval and activation are separate concepts.
4. Inference cannot approve or activate merchant-owned configuration.
5. Merchant-initiated configuration changes remain compiler-validated.
6. Semantic derivations are not presented as optional choices.
7. Every revision has stable identity and merchant scope.
8. Every change identifies its base revision.
9. Configuration activation is atomic.
10. A command executes against one coherent operational model revision.
11. Configuration changes cannot silently reinterpret existing commitments.
12. Existing commitments retain sufficient configuration provenance.
13. Current platform invariants remain authoritative.
14. Disabling a capability prevents applicable new activity but does not abandon existing obligations.
15. Residual management surfaces may remain while obligations exist.
16. Working-hours changes affect new scheduling but do not silently cancel existing appointments.
17. Merchant-created unavailability is operational calendar data, not configuration.
18. Customer appointment changes are operational actions, not configuration changes.
19. Consequential changes require intelligible impact review.
20. Merchant-facing review uses business language rather than semantic graph terminology.
21. Explicit authorised merchant commit may constitute approval where appropriate.
22. Inference-proposed changes require explicit merchant acceptance.
23. Approval authority is contextual and privilege-bound.
24. Configuration concurrency cannot use silent last-write-wins.
25. Stale-base activation must be detected.
26. Combined concurrent changes require full semantic revalidation.
27. Storefront presentation cannot override active backend configuration.
28. Dashboard composition derives from active configuration plus outstanding operational obligations.
29. Superseded revisions remain historically meaningful.
30. Reinstating a previous configuration requires current validation.
31. Configuration reinstatement does not reverse business history.
32. Configuration history/provenance is distinct from business domain events.
33. Initial onboarding uses the same validation and activation boundary as later changes.
34. No activation path may bypass the compiler merely because the merchant or platform initiated it.
35. Configuration gaps or incompatibilities must remain explicit rather than being repaired by invented semantics.

---

# 62. Deferred decisions

The following remain intentionally unresolved:

```text
exact configuration revision persistence model
exact activation transaction mechanism
exact configuration-diff format
exact impact-analysis implementation
impact severity UI design
scheduled future activation
multi-level approval workflows
regulatory approval workflows
automatic compatibility migration
configuration branch/merge UI
configuration history retention duration
configuration export/import
multi-location configuration inheritance
bulk merchant configuration
configuration rollback UI
exact dashboard residual-surface behaviour
configuration notification policy
platform semantic-version migration mechanism
```

These decisions may not violate the invariants established in this document.

---

# 63. Governance review

## PROPOSE

A configuration lifecycle separating candidate creation, validation, impact analysis, approval, activation and supersession was defined.

**PASS**

## REVIEW / FALSIFICATION

The proposal was challenged against:

```text
AI activation
merchant direct editing
working-hours changes
existing appointments
delivery shutdown with live orders
booking shutdown with future commitments
concurrent administrators
stale configuration edits
dashboard/storefront lag
configuration reinstatement
historical semantics
```

Material weaknesses were corrected.

**PASS**

## VALIDATE

The model was validated against:

```text
information publisher expansion
consultant scheduling change
mechanic/calendar configuration
grocery fulfilment deactivation
customer booking retirement
multi-actor configuration concurrency
```

No business-specific architecture was required.

**PASS**

## ACCEPT

The model is consistent with the accepted configuration/compiler/runtime boundaries.

**ACCEPTED**

---

# 64. Canonical decision

> **Main Street merchant configuration is revisioned, validated and atomically activated. Inference may propose a configuration but cannot activate it. Merchants approve consequential merchant-owned behaviour through business-facing review, while deterministic semantic consequences remain platform-owned. Each change is evaluated against a known base revision, compiled as a coherent candidate, subjected to impact analysis, and activated only after applicable authority requirements are satisfied. New configurations govern applicable new operations but do not silently reinterpret existing commitments. Capabilities disabled for new use remain operationally manageable while outstanding obligations exist. Working-hours and other recurring scheduling-rule changes are configuration changes, whereas specific merchant unavailability and customer appointment manipulation remain operational calendar actions. Concurrent configuration edits cannot silently overwrite one another, and reinstating a previous configuration is a new validated activation—not a reversal of business history.**

**MS-PROT-040 is ACCEPTED.**

The next consequential boundary is **MS-PROT-041 — Main Street Calendar, Scheduling Storage & Appointment Consistency Model**. That document should now specify, rigorously, how Main Street's scheduling engine, Main Street database, Google Calendar-backed schedule storage, merchant unavailability intents and customer appointments remain consistent without allowing Google Calendar to become semantic authority.
