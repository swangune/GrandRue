# MS-PROT-040 v1.7 — Non-Initial Configuration Activation Authority Amendment

**Document ID:** MS-PROT-040
**Version:** 1.7
**Status:** ACCEPTED
**Approved:** Explicit manual confirmation and lifecycle-status reconciliation on 14 September 2026; original approval date is not asserted by this correction
**Authority type:** Merchant Configuration activation-authority amendment
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`
**Amends:** composite MS-PROT-040 through approved v1.6 within ordinary non-initial activation actor authority only
**Depends on:** MS-PROT-040 v1.0–v1.6; MS-PROT-031; MS-PROT-062; MS-PROT-071; MS-PROT-076; ADR-014
**Preserves:** first-configuration activation authority under MS-PROT-040 v1.2; exact approval affinity under v1.6; replacement/reinstatement validation, impact, concurrency, compatibility and serving-admission requirements under existing composite authority
**Purpose:** Establish the concrete current actor and trusted execution boundary for ordinary replacement and reinstatement Configuration activation without inventing staff delegation, accepting caller-asserted identity, weakening Merchant Account lifecycle authority or making every routine configuration change a universal authentication step-up event.

---

## 1. Governing Decision

For the current Main Street MVP authority model, ordinary activation of a non-initial Configuration Revision SHALL be initiated only by the **current active Merchant Controller** for the affected Merchant Scope through a valid trusted authenticated execution context.

Canonical:

```text
trusted authenticated Controller context
        +
current OPEN Merchant Account
        +
no blocking Merchant Account Suspension
        +
exact validated Configuration Revision
        +
exact completed impact review
        +
exact currently applicable approval
        +
exact serving admission
        +
current-base concurrency
        ↓
atomic Configuration activation
```

Therefore:

```text
principal identifier supplied by caller
        ≠
activation authority
```

and:

```text
historical approval
        ≠
current activation authority
```

---

## 2. Scope

This amendment governs the initiating actor and current authority predicates for:

```text
ordinary replacement Configuration activation
ordinary reinstatement Configuration activation
```

It does not redefine:

```text
Configuration candidate generation
validation
impact analysis
approval semantics
Resolved Configuration Package ownership
serving-deployment admission
semantic compatibility
base-revision concurrency
Merchant Controller transfer
Merchant Account suspension semantics
authentication architecture
Workforce role semantics
```

First activation remains governed by the stronger first-configuration rules of MS-PROT-040 v1.2.

---

## 3. Ordinary Activation Authority

Until another accepted authority establishes delegation, an ordinary non-initial activation may be initiated only by:

```text
current ACTIVE Merchant Controller
```

for the exact Merchant Scope.

The following SHALL NOT independently authorise activation:

```text
historical Controller status
Merchant Membership
staff role
administrator role label
booking privilege
ordering privilege
inventory privilege
payment privilege
approval existence
change-set authorship
AI/inference origin
system-generated candidate
caller-supplied principal identifier
```

---

## 4. Trusted Execution Context

The activation boundary SHALL consume a trusted execution context established by Main Street.

For a human Controller activation, that context must establish at least:

```text
exact Merchant Scope
exact Execution Principal
human authentication provenance
```

The activation request's initiating principal must equal the trusted context principal.

The trusted context Merchant Scope must equal the Merchant Scope of the candidate Configuration Revision.

A client request containing:

```text
merchantIdentifier
principalIdentifier
```

without trusted context establishment is not sufficient activation authority.

---

## 5. Authentication Is Not Business Authority

Authentication establishes trusted identity continuity.

It does not establish Merchant Controller authority.

Canonical:

```text
valid authenticated Identity
        +
trusted Merchant Scope
        +
current Controller Relationship
        ↓
ordinary activation actor may be authorised
```

A still-valid session belonging to a former Controller must fail current Controller authority after control transfer.

---

## 6. Current Controller Relationship

The activating principal must be the Identity of the exact current ACTIVE Merchant Controller Relationship for the Merchant Scope at the activation authority boundary.

Conceptually:

```text
TrustedExecutionContext.principal
        ==
currentControllerRelationship.identity
```

The system MUST NOT infer this from historical approval or from session contents.

Current Controller relationship truth remains owned by Merchant Account authority.

---

## 7. Merchant Account Lifecycle

Ordinary non-initial activation requires the Merchant Account to remain in a lifecycle state that permits ordinary privileged merchant mutation.

For the currently accepted ordinary path:

```text
Merchant Account = OPEN
```

is required.

A `CLOSING` or `CLOSED` Merchant Account SHALL NOT perform ordinary non-initial Configuration activation unless a future explicit lifecycle authority establishes a narrowly different permitted purpose.

---

## 8. Merchant Account Suspension

Where an applicable Merchant Account Suspension currently denies ordinary privileged merchant mutation:

```text
Configuration activation
    → denied
```

Historical validation, impact evidence, approval and previously committed activation history remain intact.

Suspension does not delete or rewrite Configuration history.

Removal or expiry of the suspension does not itself activate a pending revision.

A fresh activation attempt must re-evaluate all current predicates.

---

## 9. Approval and Activation Remain Separate

The current Controller's exact approval does not itself activate the revision.

Likewise, Controller activation authority does not create approval.

Canonical:

```text
approve C2
        ≠
activate C2
```

Both boundaries may be presented to the merchant through one coherent final action where existing MS-PROT-040 rules permit UI compression.

Internally they remain distinct authoritative decisions.

---

## 10. Current MVP Consequence

Because approved MS-PROT-040 v1.6 currently restricts ordinary non-initial approval to the current Controller, and this amendment restricts ordinary non-initial activation to the current Controller, the same current Controller Identity will normally perform both decisions.

This is an intentional bounded MVP result.

It does not merge the concepts.

Future accepted authority may allow:

```text
Controller approves
        +
authorised delegated actor activates
```

or another legitimate composition.

No such path is established here.

---

## 11. Direct `Save & Activate`

Main Street MAY present an authoritative Controller workflow such as:

```text
Review changes
        ↓
Save & Activate
```

without forcing two ceremonial confirmation screens.

The one user action may trigger:

```text
approval establishment
        +
activation attempt
```

provided every independent predicate succeeds.

Failure after approval but before activation leaves:

```text
approval = committed historical fact where applicable
activation = not committed
current active revision = unchanged
```

No partial activation is permitted.

---

## 12. Inference-Proposed Configuration

AI or inference may create or propose a candidate under its accepted source authority.

It SHALL NOT become the activating principal for the ordinary merchant path.

Canonical:

```text
AI proposes
        ↓
merchant reviews
        ↓
current Controller approves
        ↓
current Controller authorises activation
```

Automation of technical processing between these steps does not transfer merchant authority to AI.

---

## 13. Platform-Origin Candidates

A `PLATFORM_REQUIRED` or otherwise platform-originated Configuration candidate does not automatically gain ordinary activation authority.

Where a platform migration must become active without ordinary current-Controller activation, that requires a separate accepted migration/administrative authority.

This amendment does not create such a bypass.

---

## 14. Reinstatement

Reinstatement remains a new current activation decision.

Therefore a previously superseded revision may become active again only through:

```text
current validation
current compatibility
current impact review
current applicable approval
current Controller activation authority
current serving admission
current concurrency predicates
```

An old activation request SHALL NOT be repurposed as reinstatement.

An old Controller's historical activation authority SHALL NOT revive.

---

## 15. Lost Acknowledgement and Replay

If an activation already committed and the acknowledgement was lost, replay of the same exact logical activation request returns the historical committed result.

That replay does not require present-day Controller authority because it creates no new activation effect.

Canonical:

```text
activation committed at T1
        ↓
response lost
        ↓
same request replayed at T2
        ↓
return T1 committed evidence
```

The current active pointer MUST NOT move merely because the historical request was replayed.

If no activation previously committed, retry is a new attempt against current authority and must re-evaluate the current Controller, Merchant Account and Suspension predicates.

---

## 16. Controller Transfer Before Activation

Suppose:

```text
Controller A approves C2
        ↓
control transfers to B
        ↓
A attempts activation
```

Result:

```text
reject
```

A's authenticated Identity Session may remain technically valid, but it no longer supplies current Controller authority.

Under v1.6 the old approval is also no longer currently applicable.

Controller B must establish the currently required approval and activation authority before C2 may become active.

---

## 17. Controller Transfer Racing Activation

Controller transfer and Configuration activation must be serialised or otherwise fenced such that their observable authority result is deterministic.

If the Controller transfer crosses the current-authority decision boundary first:

```text
former Controller activation
    → reject
```

If a valid activation commits first:

```text
activation remains historical fact
subsequent Controller transfer does not undo it
```

Exact advisory-lock, transaction or compare-and-set implementation remains implementation architecture.

---

## 18. Suspension Racing Activation

Merchant Account suspension and ordinary Configuration activation must likewise preserve the accepted account-wide mutation boundary.

If an effective suspension becomes authoritative before activation crosses its required current account-authority boundary:

```text
ordinary activation must not commit
```

A committed activation is not retrospectively erased by a later suspension.

The suspension governs subsequent applicable activity according to Merchant Account authority.

---

## 19. Base-Revision Concurrency Remains Independent

Current Controller authority does not waive stale-base concurrency.

A fully authorised Controller attempting to activate:

```text
candidate.baseRevision != currentActiveRevision
```

must still receive:

```text
ACTIVATION_CONFLICT
```

No current Controller may authorise silent overwrite or automatic semantic merge.

---

## 20. Serving Admission Remains Independent

Current Controller authority cannot waive:

```text
semantic release admission
Resolved Configuration Package affinity
new-activity execution requirement coverage
current serving generation
deployment support
```

A correctly authorised activation may still fail serving admission.

Merchant authority does not become deployment authority.

---

## 21. Authentication Assurance

This amendment does **not** designate every ordinary replacement Configuration activation as a universal ADR-014 step-up-authentication event.

Rationale:

```text
small configuration adjustment
        ≠ automatically
Controller transfer / account closure class operation
```

Requiring passkey step-up for every ordinary configuration activation would materially increase merchant software burden without an accepted risk classification proving that such friction is proportionate.

The ordinary baseline therefore requires:

```text
valid trusted authenticated Controller context
+
current Controller authority
```

Any stronger authentication requirement established by another accepted authority for a specific high-impact operation remains mandatory.

MS-PROT-040 v1.2's stronger authentication requirement for ordinary **first activation** remains unchanged.

---

## 22. Future Risk-Sensitive Step-Up

A future accepted security/configuration authority MAY classify specific configuration effects as requiring elevated authentication.

Examples might include, if later justified:

```text
control/security changes
certain payment-control changes
certain high-risk access changes
other expressly designated configuration effects
```

Such a rule must identify the authoritative classification and required authentication assurance.

This amendment SHALL NOT infer high-impact status merely from text labels, capability names or UI wording.

---

## 23. No New Workforce Privilege

This amendment does not create:

```text
configuration.activate
```

as a Merchant Workforce privilege.

It also does not grant activation through:

```text
ADMIN
MANAGER
all privileges
merchant staff membership
```

Future delegated activation requires separate accepted authority defining the operation, privilege ownership, delegation ceiling, trusted execution context and revocation/currentness semantics.

---

## 24. System and Scheduled Principals

The ordinary non-initial merchant activation path does not accept:

```text
system principal
scheduled principal
AI principal
integration principal
```

as a substitute for current Controller authority.

A future automated activation workflow must possess explicit accepted authority defining:

```text
who authorised the automation
what exact configuration it may activate
when that authority expires
how current approval is preserved
how suspension/control-transfer changes affect execution
how retry/reconciliation works
```

No such authority is implied here.

---

## 25. Application Boundary

The production activation application boundary must receive the trusted execution context directly or receive an equivalent non-forgeable application-established authority context.

The implementation SHALL NOT reduce the application boundary to:

```text
isAuthorized(
    principalIdentifier,
    merchantIdentifier,
    revisionIdentifier
)
```

where all three values can originate solely as caller assertions.

Those identifiers may remain useful references, but they must be checked against the trusted context and current authority.

---

## 26. Current-Authority Revalidation

Current Controller, Merchant Account lifecycle and applicable Suspension state must be evaluated with the freshness/concurrency guarantees appropriate to the activation commit.

A cached role or old relationship snapshot is insufficient where it could outlive a committed authority change.

No browser token or session claim may override current authoritative state.

---

## 27. Activation Request Affinity

The logical activation request retains its existing immutable intent affinity.

At minimum the implementation must continue binding retry identity to:

```text
release
expected current Configuration Revision
initiating principal
```

Where trusted-context provenance is recorded separately, retry reconciliation must not reinterpret a committed request as a different activation.

Reuse of one logical activation request identifier for materially different intent remains prohibited.

---

## 28. Historical Activation Provenance

Successful activation evidence shall continue to identify the initiating principal.

Where required for support/security reconstruction, trusted authentication/session provenance may be retained through appropriate audit/security evidence without making the session credential part of Configuration authority.

Raw browser session credentials MUST NOT be persisted into Configuration activation evidence.

---

## 29. Failure Semantics

The implementation must be able to reject at least the following conditions without activation side effects:

```text
trusted execution context absent
trusted Merchant Scope mismatch
trusted principal mismatch
human authentication provenance absent
Merchant Account not permitted for ordinary mutation
effective Suspension blocks ordinary mutation
current Controller absent
initiating principal is not current Controller
current approval absent/inapplicable
validation/package affinity failure
serving admission failure
semantic compatibility failure
stale base/current pointer
logical activation request conflict
```

These conditions may map to existing stable activation status categories where those categories remain truthful.

This amendment does not require a new public status for every internal denial reason.

---

## 30. Authority Ordering

The ordinary replacement/reinstatement activation decision conceptually evaluates:

```text
committed-request replay?
        ↓ no
trusted execution context
        ↓
current account mutation permission
        ↓
current Controller authority
        ↓
current active/base concurrency
        ↓
current applicable approval
        ↓
semantic compatibility
        ↓
serving admission
        ↓
atomic activation
```

Implementation may reorder independent reads for safe transactional efficiency only where observable semantics remain identical and no fail-open behaviour is introduced.

---

## 31. Fundamental Vision Conformance

This model introduces no new merchant-visible permission system.

For the current target merchant:

```text
Merchant reviews change
        ↓
Merchant applies change
```

Main Street internally resolves:

```text
trusted identity
current Controller
account lifecycle
suspension
approval
validation
deployment support
concurrency
```

The merchant need not understand any of those internal authorities.

This therefore conforms to:

```text
Administrative Compression
low merchant-visible complexity
business-language interaction
deterministic authority
```

while maintaining business control.

---

## 32. Falsification Outcomes

### Former Controller with valid session

```text
authentication valid
current Controller relationship absent
```

Result: reject.

### Staff administrator attempts activation

```text
ACTIVE membership
ADMIN role
not Controller
```

Result: reject.

### Current Controller with trusted authenticated context

All other activation predicates valid.

Result: activation may proceed.

### Caller supplies another Controller's identifier

Trusted context principal differs.

Result: reject.

### AI-created candidate

Current Controller later reviews and activates.

Result: valid.

AI itself attempts activation.

Result: reject.

### Suspended Merchant Account

Current Controller remains authenticated.

Result: ordinary activation rejected.

### Suspension lifted

Pending candidate remains non-active.

Current Controller retries.

Result: all current predicates evaluated again.

### Controller transfer after approval

Historical approval persists but is inapplicable.

Former Controller activation: reject.

New Controller without new approval: reject.

New Controller with exact current approval: may proceed.

### Lost acknowledgement

Previously committed activation is replayed after Controller transfer.

Result: return historical committed outcome; do not reactivate anything.

### Stale candidate

Current Controller authorised but candidate base is no longer current.

Result: `ACTIVATION_CONFLICT`.

### Reinstatement

Old successful activation request replayed.

Result: historical replay only.

Fresh reinstatement request with current validation, approval and Controller authority.

Result: may proceed.

---

## 33. Rejected Alternatives

### Trust `initiatingPrincipalIdentifier`

Rejected because a caller-supplied string is attribution, not trusted actor authority.

### Reuse approval as activation authorisation

Rejected because Approval and Activation are intentionally separate authority boundaries.

### Permit all Merchant Administrators

Rejected because no accepted Workforce operation/privilege currently establishes Configuration activation authority.

### Add `configuration.activate` immediately

Rejected because that creates a new delegated permission system and semantic operation beyond what R3 requires.

### Permit automated system activation after approval

Rejected for the ordinary path because no accepted automation authority currently defines its scope, expiry and revocation/currentness behaviour.

### Require universal passkey step-up for every replacement

Rejected as disproportionate to potentially routine configuration changes and contrary to Administrative Compression without a specific accepted risk classification.

### Remove authentication because approval already exists

Rejected because activation remains a current privileged mutation and must have a trusted initiator.

---

## 34. Future Delegated Activation

Future delegated Configuration activation remains permitted design space.

A later authority must define at least:

```text
Configuration activation Operation identity
privilege identity
semantic owner
delegation ceiling
who may grant it
trusted execution requirements
current Membership/Role applicability
Controller override/revocation
Merchant Account Suspension interaction
approval relationship
reinstatement authority
authentication assurance
```

Until then:

```text
current Controller only
```

is the ordinary non-initial activation rule.

---

## 35. Amendment Effect

This accepted amendment resolves the previously unspecified ordinary non-initial Configuration activation actor for IMP-05-R3.

It establishes:

```text
real trusted caller context
+
current Controller authority
+
current account mutation authority
```

as mandatory activation predicates.

It does not itself close IMP-05-R3.

Implementation must still prove:

```text
real production activation authorizer
trusted-context binding
Controller-transfer race behaviour
Suspension/current-account behaviour
replacement composition
reinstatement composition
retry/history preservation
full PostgreSQL verification
```

---

## 36. Acceptance Statement

Main Street shall not make a Configuration Revision authoritative merely because a request names an authorised-looking principal.

> **Ordinary replacement and reinstatement activation require a real trusted authenticated execution context, current Merchant Controller authority, current Merchant Account permission to mutate, exact approval and all existing activation/admission predicates.**

For the current MVP, Main Street keeps that authority simple:

> **The current Controller changes how the business operates; Main Street absorbs the security and concurrency machinery required to make that decision safe.**

### Lifecycle-status reconciliation — 14 September 2026

Following explicit manual confirmation, the stale proposed-status metadata and conditional acceptance wording were reconciled with the accepted composition already identified by AUTHORITY-INDEX.md, the resolved records in DEFERRED-DECISION-REGISTER.md and accepted MS-PROT-040 v1.8. This correction follows DESIGN-RULES v2.3 and DOCUMENT-GOVERNANCE v2.3; it does not change substantive rules, amendment scope, historical governance references, implementation evidence or programme readiness. It does not assert a previously undocumented original approval date.
