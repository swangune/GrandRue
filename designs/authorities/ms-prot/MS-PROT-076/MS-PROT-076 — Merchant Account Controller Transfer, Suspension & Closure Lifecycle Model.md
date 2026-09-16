# MS-PROT-076 — Merchant Account Controller Transfer, Suspension & Closure Lifecycle Model

**Document ID:** MS-PROT-076  
**Version:** 1.0  
**Status:** ACCEPTED  
**Depends on:** MS-PROT-028 v1.2, MS-PROT-028 v1.3, MS-PROT-031, MS-PROT-053, MS-PROT-056 v1.5, MS-PROT-062, MS-PROT-063, MS-PROT-064, MS-PROT-067, MS-PROT-071 v1.0, MS-PROT-071 v1.1, MS-PROT-072, MS-PROT-073, MS-PROT-074, MS-PROT-075  
**Closes:** Promoted boundary review — Merchant/controller ownership transfer, suspension, closure and terminal account lifecycle  
**Purpose:** Define Merchant Account post-establishment lifecycle, Merchant Controller cardinality and transfer/recovery, account-wide suspension, terminal closure, residual-operation preservation and controller-credential-reset consequences without collapsing Merchant Account identity, staff authority, Commercial state, Resource Protection, Provider state, trust state or data-retention semantics.

---

## 1. Governing Principle

> **A Merchant Account remains the same durable Main Street tenancy throughout controller transfer, suspension and closure. Controller authority may change, merchant-wide execution may be restricted, and the account may enter a terminal closed lifecycle without rewriting its immutable tenancy identity, configuration history, commercial history, existing business commitments or durable evidence.**

Canonical separation:

```text
Merchant Account existence
    ≠ Merchant Account lifecycle
    ≠ Merchant Account suspension
    ≠ Merchant Controller relationship
    ≠ Merchant Controller credential/session
    ≠ Merchant Membership / Staff authority
    ≠ Commercial Agreement / Entitlement
    ≠ Resource Protection restriction
    ≠ Trust state
    ≠ Provider state
    ≠ Merchant Configuration
    ≠ Publication / Exposure
    ≠ business commitment lifecycle
    ≠ data retention / deletion
```

No universal merchant `status` may collapse these independent authorities.

---

## 2. Architectural Ownership

The Merchant Account lifecycle authority owns:

```text
MerchantAccountLifecycle
MerchantAccountSuspension
normal Merchant Controller transfer semantics
exceptional Merchant Control Recovery transition semantics
post-establishment controller cardinality invariant
terminal Merchant Account closure transition
```

It does not own:

```text
Merchant Account establishment / immutable merchantIdentifier
Identity or credential material
Session security
Staff Membership or Role Assignment
Commercial Agreement / payment / entitlement
Merchant Configuration
capability-owned Orders, Bookings, Appointments, Payments or Inventory
ProviderConnection / Provider Readiness
TrustClaim truth
AuditRecord
Data-retention disposition
```

MS-PROT-071 remains authoritative for Merchant Account establishment and immutable tenancy identity. MS-PROT-028/MS-PROT-063 remain authoritative for authentication/trust context. MS-PROT-074 remains authoritative for workforce access. MS-PROT-064 owns audit evidence.

---

## 3. Merchant Account Lifecycle

The accepted Merchant Account post-establishment lifecycle is exactly:

```text
OPEN
   ↓
CLOSING
   ↓
CLOSED
```

`CLOSED` is terminal.

`SUSPENDED` is intentionally not a Merchant Account lifecycle state. Suspension is an orthogonal account-wide restriction and may coexist with `OPEN` or `CLOSING` where independently justified.

`OPEN` means only that the tenancy remains open. It does not imply that the merchant is configured, published, commercially entitled, verified, provider-ready or operationally eligible.

---

## 4. Merchant Controller Cardinality

For v1.0:

> **Every non-closed Merchant Account MUST have exactly one ACTIVE Merchant Controller relationship. A CLOSED Merchant Account MUST have zero ACTIVE Merchant Controller relationships.**

Therefore:

```text
OPEN
    → exactly 1 ACTIVE Merchant Controller

CLOSING
    → exactly 1 ACTIVE Merchant Controller

CLOSED
    → 0 ACTIVE Merchant Controllers
```

This Main Street control relationship does not assert legal ownership, directorship, beneficial ownership or real-world business title.

Multiple Merchant Memberships, Administrators, Managers and other delegated workforce relationships remain supported independently through MS-PROT-074.

Multiple equal Merchant Controllers are not accepted in v1.0. A future co-controller/quorum model requires separately governed evidence and semantics.

---

## 5. Merchant Controller Relationship Lifecycle

A Merchant Controller relationship uses:

```text
ACTIVE
ENDED
```

`ENDED` is terminal for that relationship identity.

If the same Identity later becomes Controller again, a new relationship identity is established. Historical controller provenance remains intact.

---

## 6. Normal Controller Transfer

Normal transfer requires:

```text
current ACTIVE Merchant Controller
+
current/strong authentication assurance required by the operation
+
receiving authenticated Identity
+
explicit recipient acceptance
+
current authoritative relationship/lifecycle revalidation
+
required audit evidence
```

Canonical flow:

```text
Controller A initiates transfer to Identity B
        ↓
transfer remains pending / non-authoritative
        ↓
B authenticates and explicitly accepts
        ↓
current Controller + lifecycle state revalidated
        ↓
ATOMIC COMMIT
    Controller A relationship ACTIVE → ENDED
    Controller B relationship → ACTIVE
```

The transfer MUST NOT commit an intermediate state with zero or two active Controllers.

A transfer invitation or acceptance token is not Merchant Controller authority before the transfer commits.

---

## 7. Transfer Concurrency and Idempotency

Controller transfer MUST be conditional on the expected current authoritative Controller/lifecycle state.

If concurrent transfers race from the same current state, at most one may commit. Later acceptance based on stale Controller state MUST fail with a transfer conflict and re-evaluate current authority.

A logical transfer request MUST have stable request identity sufficient to prevent retry or lost acknowledgement from applying the same transfer more than once.

If transfer committed but acknowledgement was lost, retry MUST resolve to the already-committed result rather than create a new Controller transition.

---

## 8. Transfer Does Not Replace the Merchant Account

Controller transfer MUST preserve:

```text
merchantIdentifier
Merchant Account existence
Merchant Configuration history and active revision
business commitments
CustomerContext/business history
Inventory state
Commercial history and trial history
ProviderConnection identity
Staff Memberships / Roles / Groups
Merchant Operational Device Authorisations
Audit history
```

unless an independently authorised operation changes those facts.

Controller transfer MUST NOT restart the initial full-experience trial or silently create a new Merchant Account.

---

## 9. Stale Former-Controller Sessions

After transfer commits, the former Controller MUST immediately cease to satisfy current Merchant Controller authorisation.

Old sessions, cached UI state, tokens or old credentials MUST NOT restore Merchant Controller authority.

Relevant former-Controller sessions SHOULD be invalidated, but current relationship revalidation is the authoritative protection boundary.

---

## 10. Credential Reset Is Not Control Transfer

A Merchant Account does not own a password. Authentication credentials belong to the Controller Identity/security context.

Therefore:

```text
Controller password reset
    ≠ Merchant Account suspension
    ≠ Merchant Controller transfer
    ≠ Merchant Controller relationship termination
    ≠ Staff Membership termination
    ≠ Merchant Operational Device deauthorisation
```

Ordinary password reset, credential rotation, forgotten-password recovery or Controller session invalidation MUST NOT by themselves interrupt merchant business execution.

Canonical normal-reset consequence:

```text
Controller Identity credential
    old credential → unusable
    replacement credential → established

Controller sessions dependent on superseded credential/security state
    → invalidated as required

Merchant Account
    → unchanged

Merchant Controller relationship
    → ACTIVE

Staff Memberships / Staff sessions
    → unchanged

Merchant Operational Device Authorisations
    → unchanged

storefront / Orders / Bookings / POS / normal business operation
    → continue according to their independent authorities
```

If the same Identity controls multiple Merchant Accounts, Identity-level password reset may invalidate that Identity's relevant authentication sessions across those contexts, but MUST NOT suspend each Merchant Account or affect independent staff execution merely because the Controller is temporarily unauthenticated.

---

## 11. Credential Reset Under Suspected Compromise

A broader restriction MAY occur only when an independently established account-control/security condition justifies it.

Canonical distinction:

```text
password reset alone
    → credential/session consequence only

password reset
+
credible account-compromise condition
    → separate security evaluation
    → possible Merchant Account Suspension where merchant-wide restriction is justified
```

The suspension derives from the security/account-control authority, not from the password-reset action.

---

## 12. Merchant Control Recovery

Main Street MUST distinguish:

```text
credential recovery
    ≠ identity recovery
    ≠ Merchant Control Recovery
    ≠ normal Merchant Controller transfer
```

Credential recovery restores authentication continuity for the same Identity where the accepted security evidence is sufficient.

Merchant Control Recovery is an exceptional path used when normal current-Controller transfer cannot establish the replacement Controller. It requires separately authorised high-assurance evidence/intervention and MUST NOT be implemented as customer support directly editing a controller identifier.

If recovery authoritatively replaces the Controller, the same atomic cardinality invariant applies:

```text
old ACTIVE Controller relationship → ENDED
new Controller relationship → ACTIVE
```

No intermediate committed ownerless or multi-controller tenancy is permitted.

---

## 13. Merchant Account Suspension

A Merchant Account Suspension is an explicit merchant-wide restriction justified by an accepted security, legal, account-control or administrative authority whose scope genuinely applies to the Merchant Account as a whole.

Conceptually:

```text
MerchantAccountSuspension
{
    suspensionIdentity
    merchantIdentifier
    sourceAuthority
    reasonClass
    establishedAt
    establishedBy
    releaseAuthority / condition
    provenance
}
```

The representation is conceptual and does not prescribe one Java type/table.

A Merchant Account MAY have multiple independently effective suspension facts.

---

## 14. Suspension Is Exceptional and Scope-Bounded

Merchant-wide suspension MUST NOT be used when a narrower accepted authority can safely govern the problem.

Therefore:

```text
Payment failure
    ≠ Merchant Account Suspension

Provider failure
    ≠ Merchant Account Suspension

Temporary Protective Restriction / rate limit
    ≠ Merchant Account Suspension

Staff Membership suspension
    ≠ Merchant Account Suspension

narrow TrustRequirement failure
    ≠ Merchant Account Suspension
```

A merchant-wide suspension requires merchant-wide justification.

Evidence from another authority MAY trigger review or a separately authorised suspension operation; it MUST NOT mutate Merchant Account suspension automatically.

---

## 15. Suspension Composition

Where multiple active Merchant Account Suspensions exist, they compose independently.

Releasing one suspension MUST NOT restore ordinary account execution while another independently effective suspension remains.

A single mutable `suspended=true/false` fact is insufficient where it would erase concurrent source authority and provenance.

---

## 16. Suspension Effect

While any applicable Merchant Account Suspension is effective, ordinary new merchant operational activity and ordinary privileged merchant mutation MUST be denied at the account-wide boundary.

The suspension contract MAY still allow explicitly authorised suspension-safe purposes including, where applicable:

```text
account recovery
security remediation
required audit/evidence review
registered administrative intervention
purpose-bound resolution of pre-existing business commitments
```

Suspension MUST NOT erase existing Orders, Bookings, Appointments, payment evidence, Configuration or Commercial history.

The owning capabilities remain authoritative for any required commitment-resolution operation.

---

## 17. Suspension and Public Exposure

Suspension does not universally imply storefront deletion or public unpublication.

The source authority and affected scope determine the required Exposure consequence.

Examples:

```text
account-control compromise
    → privileged merchant execution may be suspended
    → public read-only information may remain where safe

legal takedown applying merchant-wide
    → public Exposure may also be withdrawn
```

Presentation MUST follow the actual suspension/exposure contract rather than a universal `suspended website` rule.

---

## 18. Suspension Does Not Remove the Controller

Suspension constrains account-level execution; it does not by itself terminate the Merchant Controller relationship.

```text
Merchant Controller relationship
    identifies who controls the Merchant Account

Merchant Account Suspension
    constrains what account-wide execution is currently permitted
```

This preserves control provenance and supports secure remediation/recovery.

---

## 19. Suspension Release

A suspension may be released only by the authority or recovery path authorised to release that specific suspension.

Merchant Controller status alone MUST NOT permit clearing a security/legal suspension where the source authority does not delegate release to the Controller.

Release MUST preserve all still-effective independent suspensions.

---

## 20. Begin Merchant Account Closure

Ordinary voluntary closure MUST NOT jump directly from `OPEN` to `CLOSED`.

Canonical transition:

```text
OPEN
    -- BeginMerchantAccountClosure -->
CLOSING
```

`CLOSING` means:

> The Merchant Account is in terminal progression, may no longer originate ordinary new business activity, and remains available only for authorised closure work and purpose-bound resolution of pre-existing obligations.

For ordinary merchant-requested closure, the initiating principal MUST be the current Merchant Controller with the current/recent authentication assurance required by this high-impact operation.

An effective account-wide security suspension SHOULD prevent ordinary self-service closure unless the suspension's governing policy explicitly permits it. Administrative/legal closure may use a separate authorised path.

---

## 21. CLOSING Is Not Configuration or Data Deletion

Entering `CLOSING` MUST NOT delete or rewrite Merchant Configuration, business history, Staff Memberships, CustomerContext, provider history or commitments.

Instead, Merchant Account lifecycle contributes a restriction against new ordinary business activity while preserving the facts required to resolve outstanding commitments.

---

## 22. Existing Commitments During CLOSING

Existing valid business commitments MUST survive closure initiation.

Examples include:

```text
future Appointments
unfulfilled Orders
pending refunds
provider reconciliation obligations
required customer communication
```

The owning capability determines which operations remain semantically valid to fulfil, cancel, refund, reconcile, communicate about or otherwise resolve those commitments.

Account closure MUST NOT fabricate cancellation or completion merely to reach `CLOSED`.

---

## 23. Staff Access During CLOSING

Entering `CLOSING` MUST NOT automatically terminate Staff Memberships, Role Assignments or Merchant Operational Device Authorisations.

Authorised staff MAY continue only those operations that remain permitted for purpose-bound resolution of existing obligations.

New ordinary activity remains prohibited by Merchant Account lifecycle even if Staff Actor Authorisation would otherwise permit it.

MS-PROT-074 continues to govern which Staff Principal may act and from which authorised operational device.

---

## 24. Closure Orchestration

Merchant Account closure is a cross-capability progression and SHOULD use MS-PROT-072 application/process orchestration rather than a universal cross-domain transaction.

Conceptually:

```text
Begin closure
    ↓
Merchant Account = CLOSING
    ↓
withdraw new operational entry points
    ↓
resolve pre-existing commitments
    ↓
request Commercial termination where applicable
    ↓
perform provider/security cleanup
    ↓
perform required data/export/disposition preparation
    ↓
evaluate final closure eligibility
    ↓
CLOSED
```

Each participating authority remains owner of its own facts. The closure process owns progression only.

---

## 25. Commercial, Provider and Configuration Separation During Closure

The following distinctions remain mandatory:

```text
Merchant Account CLOSING
    ≠ Commercial Agreement cancelled

Commercial Agreement cancelled
    ≠ Merchant Account CLOSED

Merchant Account CLOSED
    ≠ Provider cleanup completed

Merchant Account CLOSED
    ≠ Merchant Configuration deleted
```

Commercial termination, provider disconnection/credential revocation and configuration history remain owned by their accepted authorities.

---

## 26. Final Closure Eligibility

`CLOSING → CLOSED` may commit only when the closure authority can establish that:

```text
no currently known business commitment still requires
ordinary Merchant Account operational authority

+
no unresolved execution uncertainty could still materially
establish such an obligation without reconciliation

+
required account-control/closure evidence is durable

+
remaining obligations are completed or represented through
explicitly valid post-closure residual/remediation paths
```

This does not require every historical record, invoice, AuditRecord or retained evidence item to disappear.

---

## 27. Final Closure Atomicity

Final closure MUST atomically preserve this invariant:

```text
MerchantAccountLifecycle
    CLOSING → CLOSED

+
current Merchant Controller relationship
    ACTIVE → ENDED
```

A successful final closure MUST NOT leave a CLOSED Merchant Account with an ACTIVE Merchant Controller relationship.

Likewise the system MUST NOT end the sole active Controller relationship as part of final closure while leaving the Merchant Account authoritatively `CLOSING` because of a partial commit.

---

## 28. CLOSED Semantics

`CLOSED` means:

> The Merchant Account remains a historically real Main Street tenancy but no longer supports ordinary merchant operation or creation of new business activity.

Therefore:

```text
Merchant Account existence = YES
merchantIdentifier = unchanged
lifecycle = CLOSED
active Merchant Controller = NONE
```

Ordinary new Orders, Bookings, Merchant Configuration activation, Publication, Staff operational activity and ordinary Controller operations are unavailable.

Only explicitly accepted post-closure residual, evidence, legal, reconciliation or remediation operations may remain possible.

---

## 29. CLOSED Is Terminal and Not Reopened

`CLOSED` MUST NOT transition back to `OPEN`.

If the same real-world business later returns to Main Street, it establishes a new Merchant Account with a new `merchantIdentifier`.

This prevents stale Staff authority, old device authorisations, old Commercial grants, Provider credentials, trial history or configuration/runtime assumptions from silently reactivating.

A future reopening model would require a separately accepted amendment with explicit authority-migration semantics.

---

## 30. Closure Is Not Data Deletion

`CLOSED` MUST NOT mean that all data has been physically deleted.

Post-closure disposition remains governed by MS-PROT-053 and applicable evidence/legal/business requirements.

Permitted later dispositions may include:

```text
DELETE
ANONYMISE
REDACT
RESTRICT_USE
RETAIN_MINIMAL_EVIDENCE
RETAIN_WITH_EXPLICIT_JUSTIFICATION
```

No Merchant Account lifecycle state named `DELETED` is established by this authority.

---

## 31. Administrative Closure

A platform/legal/security authority MAY initiate terminal closure only through an explicitly registered and authorised administrative/intervention operation.

Main Street MUST NOT support routine direct database mutation such as:

```text
merchant.lifecycle = CLOSED
```

Administrative closure MAY first establish Merchant Account Suspension where immediate merchant-wide restriction is justified, then progress through the same authoritative `CLOSING → CLOSED` semantics with any necessary policy-specific differences explicitly governed.

---

## 32. Audit Evidence

The following are high-impact security/account-control operations and require durable attributable audit evidence; the accepted implementation SHOULD treat them as Atomic Audit Evidence where success without audit evidence would be unacceptable:

```text
Merchant Controller transfer
exceptional Merchant Control Recovery
Merchant Account Suspension
Merchant Account Suspension release
Begin Merchant Account Closure
final Merchant Account closure
```

Evidence SHOULD preserve the acting principal/origin, Merchant Scope, prior and resulting state/reference, source/reason category, time and correlation/provenance required by MS-PROT-064.

Controller password reset itself remains governed by authentication/security audit requirements; it is not a Merchant Account lifecycle mutation.

---

## 33. Notification Relationship

Controller transfer, suspension, suspension release, closure initiation and final closure MAY establish required Notification Intents under MS-PROT-075.

Notification delivery failure MUST NOT undo a committed account/control transition unless the specific operation independently defines communication as completion-critical.

Notification Delivery Evidence is not Merchant Account lifecycle truth.

---

## 34. Domain Events

Where cross-capability reaction is required, post-commit events may conceptually include:

```text
MerchantControllerTransferred
MerchantAccountSuspended
MerchantAccountSuspensionReleased
MerchantAccountClosingStarted
MerchantAccountClosed
```

Events report already-committed facts and MUST NOT independently transfer control, suspend, release or close the Merchant Account.

---

## 35. Failure / Behavioural Outcome Distinctions

The implementation contract MUST be able to preserve materially distinct outcomes including, as applicable:

```text
CONTROLLER_AUTHENTICATION_REQUIRED
CONTROLLER_AUTHORISATION_REJECTED
TRANSFER_RECIPIENT_NOT_ACCEPTED
TRANSFER_CONFLICT
ACCOUNT_SUSPENDED
ACCOUNT_CLOSING
ACCOUNT_CLOSED
CLOSURE_NOT_READY
CONTROL_RECOVERY_REQUIRED
TECHNICAL_FAILURE_BEFORE_COMMIT
EXECUTION_UNCERTAIN
ALREADY_APPLIED
SUCCESS
```

This list defines behavioural distinctions, not one mandatory universal Java enum.

---

## 36. Falsification Evidence

### 36.1 Controller ordinary password reset

Controller changes or resets their Identity credential. Controller-specific affected sessions are invalidated as required. Staff sessions, authorised merchant devices, storefront, Orders, Bookings and ordinary merchant operations continue.

**PASS**

### 36.2 Controller reset after credible compromise signal

Password reset occurs while account-control compromise is independently established. Security authority may establish Merchant Account Suspension. The suspension follows the security determination rather than the credential reset itself.

**PASS**

### 36.3 Multi-merchant Controller resets Identity password

One Identity controls several Merchants. Identity-level session continuity may be invalidated across those contexts. The Merchant Accounts and independent staff operations remain unaffected.

**PASS**

### 36.4 Business sold with active Appointments

Controller A transfers Main Street control to B. Merchant Account identity and Appointments remain unchanged.

**PASS**

### 36.5 Concurrent transfers

Two recipients attempt transfer from the same Controller revision. At most one commits; the stale acceptance conflicts.

**PASS**

### 36.6 Stale former-Controller session

A formerly valid Controller session is used after transfer. Current relationship revalidation rejects Controller authority.

**PASS**

### 36.7 Rate-limit exhaustion

Resource Protection denies/defer work. No Merchant Account Suspension is created automatically.

**PASS**

### 36.8 Payment failure

Commercial authority handles payment/commercial consequences. No Merchant Account Suspension is inferred.

**PASS**

### 36.9 Account takeover

A genuinely merchant-wide account-control security condition may establish Merchant Account Suspension while preserving Merchant Account existence and historical commitments.

**PASS**

### 36.10 Two independent suspension reasons

Releasing one suspension leaves the other effective; ordinary operation does not resume prematurely.

**PASS**

### 36.11 Voluntary closure with outstanding Orders/Appointments

Account enters CLOSING, accepts no new ordinary activity, and retains purpose-bound staff/controller operations required to resolve existing commitments before final closure.

**PASS**

### 36.12 Provider cleanup delayed

Provider disconnection/cleanup remains independently owned. Final closure may proceed only where remaining provider work is represented by a valid post-closure remediation path and no ordinary business commitment still requires the account to remain operational.

**PASS**

### 36.13 Closed account with retained evidence

Merchant Account is CLOSED while financial, audit and minimum historical evidence remain according to retention requirements.

**PASS**

### 36.14 Former merchant returns later

A new Merchant Account is established. Old Staff/device/Commercial authority does not silently revive.

**PASS**

No falsifier requires a giant Merchant status aggregate, shared Merchant Account/business-capability ownership or universal legal/business ownership verification.

---

## 37. Rejected Alternatives

Rejected:

- one universal Merchant status combining trust, Commercial, provider, configuration and lifecycle state;
- multiple equal active Merchant Controllers in v1.0 without proven quorum/governance requirements;
- `SUSPENDED` as a Merchant Account lifecycle state;
- automatic Merchant Account suspension from rate limit, payment failure, provider failure or narrow trust failure;
- controller transfer implemented as mutation of `ownerUserId` without relationship history and recipient acceptance;
- ordinary Controller password reset interrupting staff/business operations;
- password reset automatically revoking merchant operational devices;
- direct `OPEN → CLOSED` for ordinary voluntary closure with unresolved commitments;
- closure as physical deletion;
- terminal account reopening without a separately accepted authority-migration model;
- support/admin direct database edit as Merchant Control Recovery or closure.

---

## 38. Hard Invariants

1. Merchant Account identity/existence survives Controller transfer, suspension and closure.
2. Merchant Account lifecycle is exactly `OPEN → CLOSING → CLOSED` in v1.0.
3. `CLOSED` is terminal.
4. Merchant Account Suspension is orthogonal to lifecycle.
5. Every `OPEN` or `CLOSING` Merchant Account has exactly one ACTIVE Merchant Controller relationship.
6. A `CLOSED` Merchant Account has zero ACTIVE Merchant Controller relationships.
7. Merchant Controller authority is Main Street account control, not proof of legal business ownership.
8. Normal transfer atomically replaces the sole active Merchant Controller and requires current Controller authority, required authentication assurance and recipient acceptance.
9. Transfer retry/concurrency MUST NOT create zero or multiple active Controllers.
10. Merchant Control Recovery is distinct from credential recovery and normal transfer.
11. Ordinary Controller credential/password reset MUST NOT suspend the Merchant Account, terminate the Controller relationship, revoke staff authority, deauthorise merchant operational devices or interrupt merchant business execution.
12. Controller sessions invalidated by password reset are authentication continuity only; independent Staff sessions remain valid unless separately affected.
13. Broader restriction during credential reset requires an independently established merchant-wide security/account-control justification.
14. Merchant Account Suspension requires explicit merchant-wide authority and justification.
15. Multiple effective suspensions compose independently and releasing one does not clear another.
16. Resource Protection, Commercial failure, Provider failure and Staff Membership state do not automatically become Merchant Account Suspension.
17. Suspension does not erase Controller relationship, Merchant Account identity, Configuration or business commitments.
18. `CLOSING` prohibits new ordinary business activity while preserving purpose-bound resolution of pre-existing commitments.
19. `CLOSING` does not automatically terminate Staff Memberships or device authorisations.
20. Final closure atomically commits `CLOSING → CLOSED` and ends the sole ACTIVE Merchant Controller relationship.
21. `CLOSED` does not mean physically deleted.
22. Post-closure retention/disposition remains governed by MS-PROT-053.
23. Returning after terminal closure requires a new Merchant Account unless a future accepted amendment explicitly introduces reopening/migration semantics.
24. High-impact transfer, recovery, suspension and closure mutations require durable attributable audit evidence.

---

## 39. Acceptance Statement

Main Street now has a bounded post-establishment Merchant Account lifecycle and ultimate-control model. Credential maintenance does not become merchant downtime; workforce delegation remains independent; merchant-wide suspension requires merchant-wide justification; transfer preserves one deterministic control holder; and terminal closure resolves obligations without falsifying historical tenancy or business truth.

> **Change control without replacing the tenancy; restrict only with explicit authority; close through obligation resolution; never confuse credential maintenance with merchant lifecycle.**
