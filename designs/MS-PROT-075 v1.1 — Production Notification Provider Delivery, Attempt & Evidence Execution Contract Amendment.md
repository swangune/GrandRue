# MS-PROT-075 v1.1 — Production Notification Provider Delivery, Attempt & Evidence Execution Contract Amendment

**Document ID:** MS-PROT-075  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-075 v1.0 only within concrete Provider Fulfilment participation, delivery-attempt execution, path affinity, Provider Readiness, provider evidence, retry/fallback and production-read classification  
**Depends on:** MS-PROT-023; MS-PROT-025; composite MS-PROT-027; composite MS-PROT-048 through v1.5; MS-PROT-053; MS-PROT-059; MS-PROT-062; MS-PROT-063; MS-PROT-065; MS-PROT-067; MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-073; MS-PROT-075 v1.0; MS-PROT-079  
**Closes:** MS-PROT-079 Target 16 — Notification providers and the Notification-specific scope of `MS-PROT-048-V14-DQ-014`  
**Purpose:** Make provider-backed EMAIL/SMS/PUSH/other registered Notification delivery production-executable while preserving Notification Intent, Dispatch, physical Delivery Attempt, provider evidence, recipient preference, business authority, Provider Readiness and acknowledgement as distinct facts.

---

## 1. Governing Decision

The accepted v1.0 communication hierarchy survives:

```text
business fact / process requirement
        ↓
NotificationIntent
        ↓
NotificationDispatch
        ↓
DeliveryAttempt
        ↓
DeliveryEvidence
```

Provider execution adds:

```text
NotificationDispatch
        ↓
exact message-delivery fulfilment path
        ↓
Provider Readiness
        ↓
durable DeliveryAttempt
        ↓
external side effect
        ↓
authenticated/correlated evidence
        ↓
DeliveryEvidence
```

Hard separation:

```text
business fact
    ≠ NotificationIntent

NotificationIntent
    ≠ NotificationDispatch

NotificationDispatch
    ≠ DeliveryAttempt

DeliveryAttempt
    ≠ provider acceptance

provider acceptance
    ≠ provider-reported delivery

provider-reported delivery
    ≠ recipient read

recipient read
    ≠ business acknowledgement
```

---

## 2. Provider Fulfilment Role

Target 16 canonically registers the already-established role identity:

```text
notification / message-delivery
```

The role is owned by Notifications as technical communication infrastructure.

It does not own the business reason why a message exists.

---

## 3. Requirement Ownership

The exact communication requirement remains owned by the originating capability/process/platform authority.

Examples:

```text
booking
    → SEND_CONFIRMATION

appointment
    → SEND_REMINDER

ordering
    → SEND_CANCELLATION

security authority
    → applicable security communication obligation
```

These are examples of owner-qualified responsibility.

Target 16 does not define a universal business notification catalogue.

---

## 4. Channel-Qualified Fulfilment Context

A Notification Channel is part of the technical execution context for `notification / message-delivery`.

Initial registered channel families remain:

```text
EMAIL
SMS
PUSH
IN_APP
```

where supported.

For static fulfilment purposes, each provider/internal-delivery channel permitted by an applicable Notification Contract MUST have a registered compatible message-delivery fulfilment path for that exact channel context.

Conceptually:

```text
notification / message-delivery
    context = EMAIL
        → email fulfiller

notification / message-delivery
    context = SMS
        → SMS fulfiller

notification / message-delivery
    context = PUSH
        → push fulfiller

notification / message-delivery
    context = IN_APP
        → internal fulfiller
```

---

## 5. Channel Context Does Not Own Communication Meaning

The channel qualifier answers:

> How can this already-authorised Notification Dispatch be technically delivered?

It does not answer:

> Why does this notification exist?

Therefore:

```text
SEND_CONFIRMATION
    ≠ EMAIL

SEND_CONFIRMATION
    ≠ SMS
```

A confirmation may legitimately use different independently permitted channels without moving confirmation semantics into provider routing.

---

## 6. Static Coverage

A Notification Contract MUST NOT declare a provider/internal channel as an executable delivery option where the exact release cannot establish a compatible fulfilment path for that channel.

Registry/release validation must therefore preserve consistency among:

```text
Notification Contract

permitted channel

message-delivery Fulfilment Role

exact channel context

applicable obligation set

compatible binding/provider/internal fulfiller
```

A frontend route or provider availability cannot repair missing static fulfilment support.

---

## 7. Provider Compatibility vs Provider Readiness

Two questions remain distinct.

```text
STATIC COMPATIBILITY

Can the selected fulfiller satisfy
this message-delivery role/context/obligation?
```

versus:

```text
CURRENT PROVIDER READINESS

Can that exact already-selected path
safely be attempted now?
```

A provider that fundamentally does not support SMS is not merely:

```text
SMS = NOT_READY
```

It is statically incompatible with the SMS path.

Readiness applies only after static compatibility exists.

---

## 8. Channel Selection

Runtime Channel Selection may choose only a channel for which all existing v1.0 conditions remain satisfied:

```text
Notification Contract permits channel

recipient has legitimate endpoint

recipient/current relationship permits communication

Exposure/data-handling rules permit payload

preference permits/ranks channel where applicable

consent/policy permits it where required

commercial entitlement permits channel where applicable

staff device/security restrictions are satisfied

static message-delivery path exists

current technical execution remains admissible
```

Channel availability does not create communication permission.

---

## 9. DeliveryAttempt Semantic Definition

A **DeliveryAttempt** is:

> A durable Notification-owned identity allocating one logical physical delivery effect against one exact Notification Dispatch and one exact fulfilment path.

Its durable existence is established **before** the external provider side effect.

Creation of the attempt proves:

```text
Main Street accepted this exact provider-path attempt
```

It does not prove:

```text
provider request was transmitted
provider accepted message
provider delivered message
recipient observed message
```

---

## 10. DeliveryAttempt Identity

Each DeliveryAttempt has stable identity.

Conceptually:

```text
DeliveryAttempt
{
    attemptIdentity
    dispatchIdentity

    ownerScope

    Notification Channel

    endpoint/content provenance

    fulfilmentRole
    semanticContext

    routingScope
    exact binding revision provenance

    fulfiller identity
    provider identity where external
    ProviderConnection identity where external

    providerEffectIdentity /
    idempotency-correlation identity

    acceptedAt
}
```

The exact Java/storage representation is implementation-specific.

---

## 11. Immutable Attempt Provenance

After an attempt may have externalised data, the following MUST NOT silently change:

```text
Dispatch
channel
recipient endpoint used by the attempt
content/request provenance
binding revision
provider/fulfiller
ProviderConnection
provider effect identity
```

A materially different provider effect requires a new DeliveryAttempt.

---

## 12. Delivery Payload Provenance

The attempt MUST preserve enough evidence to reproduce or identify the exact provider request semantics safely.

This may be satisfied through:

```text
exact immutable rendered payload snapshot
```

or:

```text
exact content-projection/rendering revision
+
immutable endpoint/sender/metadata provenance
```

provided retries cannot silently render materially different content under the same provider effect identity.

Notification payload provenance remains a technical communication snapshot, not a second source of business truth.

---

## 13. `notification.delivery.attempt`

Canonical internal application operation:

```text
notification.delivery.attempt
```

**Owner:** Notifications.

**Principal:** Trusted Main Street execution principal authorised to progress an already-established Notification Dispatch.

The operation does not create a new business communication requirement.

---

## 14. Attempt Inputs

Inputs include:

```text
Notification Dispatch identity

Delivery Attempt identity

expected channel

logical execution/correlation identity
```

The exact fulfilment path is resolved server-side from accepted authority.

Clients/workers MUST NOT supply arbitrary provider identity as business authority.

---

## 15. Attempt Authoritative Reads

Before commit, the operation resolves/revalidates as applicable:

```text
NotificationIntent

NotificationDispatch

NotificationContract

current recipient eligibility

current endpoint legitimacy where required

Exposure/data-handling constraints

current Notification Preference where applicable

applicable consent/policy result

Commercial Entitlement where applicable

Resource Protection admission

exact message-delivery Fulfilment Plan

static channel/provider compatibility

current ProviderConnection where external

current Provider Readiness
```

No single dimension replaces the others.

---

## 16. Attempt Mutation Boundary

Before external invocation, Main Street MUST durably commit exactly one DeliveryAttempt for the logical effect.

Canonical:

```text
validate
+
resolve exact path
+
create DeliveryAttempt
        ↓
LOCAL COMMIT
        ↓
external provider invocation
```

Rejected:

```text
call provider
        ↓
then try to remember what was sent
```

---

## 17. No External Call in Local Transaction

The provider call MUST NOT occur inside the local database transaction that creates the attempt.

The architecture instead relies on:

```text
durable attempt identity
+
safe provider-effect correlation
+
MS-PROT-065 durable progression
+
MS-PROT-069 uncertainty
+
reconciliation
```

---

## 18. Provider Effect Identity

Provider idempotency/correlation is scoped to the **exact logical provider effect**, not universally to the Notification Dispatch.

This refines MS-PROT-075 v1.0's earlier recommendation to propagate Dispatch identity into provider deduplication.

Canonical:

```text
Dispatch
    one logical recipient+channel obligation

DeliveryAttempt A1
    one logical physical provider effect

DeliveryAttempt A2
    another separately authorised provider effect
```

Therefore a provider idempotency key MUST NOT prevent a legitimate later A2 merely because A1 and A2 belong to the same Dispatch.

---

## 19. Retry of the Same Attempt

Where the provider contract offers safe idempotent re-invocation:

```text
same DeliveryAttempt
    → same provider effect identity
    → same provider/path/request semantics
```

A network retry of that exact attempt does not create another DeliveryAttempt.

---

## 20. New Delivery Attempt

A new attempt may be created only when accepted authority establishes that another logical physical effect is legitimate.

Examples include:

```text
known provider rejection before effect

known technical failure before provider effect

separately authorised retry after definitive non-delivery

authorised provider fallback after safe prior resolution
```

A different attempt receives a different attempt identity and provider-effect identity.

---

## 21. Provider Readiness Admission

For a new external provider attempt:

```text
READY
    → may proceed

DEGRADED
    → may proceed only under §22

NOT_READY
    → do not initiate provider effect

UNKNOWN
    → do not initiate provider effect
```

`UNKNOWN` is never optimistic availability.

---

## 22. Notifications-Specific DEGRADED Rule

`DEGRADED` may permit a new Notification Delivery Attempt only if the exact registered provider contract establishes that the required message-delivery obligation remains safely satisfiable without weakening any hard requirement of the Notification Contract.

Hard requirements include, where applicable:

```text
required channel

recipient/endpoint security

payload sensitivity/data handling

sender identity/presentation contract

required delivery-evidence capability

required timing characteristic where semantically material

required provider operation semantics
```

If degradation means any required hard obligation cannot be proven:

```text
treat execution as not admissible
```

This resolves the Notifications-specific scope of `MS-PROT-048-V14-DQ-014`.

---

## 23. DEGRADED Does Not Permit Semantic Downgrade

Rejected:

```text
email provider degraded
    → send SMS instead
```

unless SMS is independently permitted and the applicable fallback rules authorise a different Dispatch.

Rejected:

```text
delivery evidence unavailable
    → proceed even though contract requires it
```

Rejected:

```text
secure sender capability unavailable
    → send from arbitrary address
```

Degradation cannot silently weaken the accepted Notification Contract.

---

## 24. Platform-Scoped Provider

Where Main Street supplies shared delivery infrastructure:

```text
routingScope = PLATFORM_ROUTED
```

The DeliveryAttempt binds to the exact platform fulfilment binding and platform-scoped ProviderConnection where external.

No fake merchant provider account is created.

Merchant Scope still remains attached to merchant-scoped notification data.

---

## 25. Merchant-Specific Provider

Where accepted product/configuration semantics allow merchant-specific provider routing:

```text
routingScope = MERCHANT_ROUTED
```

the governing exact merchant Fulfilment Binding Set remains authoritative.

The platform provider model does not override merchant-specific routing.

---

## 26. Internal Fulfilment

An internal fulfiller may satisfy:

```text
notification / message-delivery
```

for applicable contexts such as `IN_APP`.

It does not require a fake external ProviderConnection.

Internal execution still requires current technical/runtime eligibility where applicable.

---

## 27. Provider Selection Is Not a Worker Argument

A background worker may identify:

```text
Dispatch
Attempt
```

but MUST NOT independently choose arbitrary:

```text
SendGrid
Twilio
Firebase
another ProviderConnection
```

Provider selection comes from the exact accepted fulfilment path.

---

## 28. No Silent Provider or Connection Substitution

If an attempt records:

```text
Provider A
Connection C1
Binding B7
```

the same attempt MUST NOT execute using:

```text
Provider B
Connection C2
Binding B8
```

because the current configuration changed.

A new attempt is required when separately authorised.

---

## 29. Provider Fallback Within the Same Channel

A fallback provider for the same Notification Channel may be used only when:

```text
an accepted fallback path exists
+
the previous attempt is known safe to supersede
or separately accepted duplicate-safe semantics apply
+
current recipient/communication eligibility remains valid
+
the fallback provider/path is statically compatible
+
current Provider Readiness admits it
```

Fallback is not inferred from provider availability alone.

---

## 30. Cross-Channel Fallback

Changing channel changes the logical Dispatch.

Therefore:

```text
EMAIL Dispatch
    → EMAIL DeliveryAttempts
```

and:

```text
fallback to SMS
    → new SMS NotificationDispatch
```

under the same NotificationIntent where independently authorised.

A DeliveryAttempt does not mutate its Dispatch from EMAIL to SMS.

---

## 31. Uncertain Attempt Blocks Blind Fallback

If Provider A may already have accepted/effected the notification:

```text
EXECUTION_UNCERTAIN
```

Main Street MUST NOT automatically create:

```text
another provider attempt
or
another channel Dispatch
```

merely because another path exists.

It must first:

```text
reconcile
```

or establish an independently accepted duplicate-tolerant/safe-retry contract.

This prevents:

```text
email may have been sent
+
SMS automatically sent
+
second email automatically sent
```

from becoming an accidental consequence of a timeout.

---

## 32. Immediate Provider Result

A synchronous provider response is evidence about the DeliveryAttempt.

It may establish bounded classifications such as:

```text
known accepted by provider

known rejected

known failed before provider effect

execution uncertain
```

The exact implementation enum is not semantic authority.

Provider acceptance remains weaker than delivery.

---

## 33. DeliveryEvidence

MS-PROT-075 v1.0 `DeliveryEvidence` remains the canonical Notification-owned transport/delivery evidence fact.

Provider evidence MAY include:

```text
provider accepted
provider rejected
provider reported delivered
provider reported bounced
provider reported opened
provider-generated message reference
provider-generated failure classification
```

with exact provenance preserved.

---

## 34. `notification.delivery-evidence.record`

Canonical operation:

```text
notification.delivery-evidence.record
```

**Owner:** Notifications.

It accepts authenticated/correlated provider evidence concerning an existing Notification Dispatch/DeliveryAttempt.

---

## 35. Provider Evidence Admission

Before provider evidence may establish Notification-owned DeliveryEvidence, Main Street MUST establish:

```text
source authenticity/integrity

expected provider identity

expected historical/current ProviderConnection context

exact DeliveryAttempt or Dispatch correlation

Merchant Scope where applicable

provider-contract / semantic-release affinity

registered evidence interpretation
```

Uncorrelated raw provider JSON is not Notification truth.

---

## 36. Synchronous and Asynchronous Evidence Use the Same Boundary

Whether evidence arrives through:

```text
immediate API response
webhook
delivery-status callback
poll/reconciliation result
```

does not change semantic ownership.

All provider-native evidence must be interpreted through the registered provider/evidence contract before becoming Notification DeliveryEvidence.

---

## 37. Duplicate Provider Evidence

Repeated delivery of the same provider evidence MUST NOT create duplicate semantic evidence/consequences.

Evidence identity/correlation must support idempotent recording.

Payload equality alone is insufficient where a provider can legitimately emit multiple distinct events with identical content.

---

## 38. Callback After Provider Change or Disconnect

A callback concerning an earlier DeliveryAttempt may remain valid even when:

```text
provider no longer selected for new work
ProviderConnection no longer READY for outbound execution
merchant/provider routing changed
```

The callback is evaluated against historical attempt affinity.

New-work Provider Readiness is not callback authority.

---

## 39. Provider Evidence Does Not Mutate Business State Directly

Canonical:

```text
provider evidence
        ↓
authenticate/correlate
        ↓
registered Notification interpretation
        ↓
DeliveryEvidence
```

Never:

```text
provider webhook
    → Order updated

provider webhook
    → Appointment acknowledged

provider webhook
    → customer consent created
```

Any business consequence requires its owning capability operation.

---

## 40. Current Eligibility Revalidation

Before any new external provider effect, current mutable delivery eligibility MUST be revalidated where the Notification Contract requires current state.

This includes as applicable:

```text
recipient relationship

current endpoint legitimacy

preference

consent/policy authority

Exposure

staff device authority

source communication applicability

channel entitlement
```

A previously created Intent or Dispatch does not permanently reserve current delivery permission.

---

## 41. Existing Uncertainty and Later Ineligibility

If an attempt may already have externalised data and the recipient later becomes ineligible:

```text
preserve historical uncertainty/evidence
```

Main Street cannot retroactively retract the possible external effect.

But later ineligibility prevents a new outbound effect unless a separately governing obligation permits it.

---

## 42. Endpoint Changes

An attempt is bound to the endpoint provenance that it externalises.

If current-recipient semantics require a materially different endpoint before any physical attempt:

```text
the old endpoint MUST NOT be silently substituted inside an existing provider attempt
```

A new/re-derived Dispatch or equivalent preserved delivery-obligation provenance must represent the new endpoint according to the Notification Contract.

Historical attempt endpoint provenance remains reconstructible.

---

## 43. Content Changes

The same provider effect identity MUST NOT silently send materially different rendered content on retry.

If current business revalidation means materially different communication is now required:

```text
re-evaluate Notification Contract / Dispatch semantics
```

rather than mutating an already-affined physical attempt.

---

## 44. Provider Failure Does Not Rewrite Business Truth

Provider delivery failure normally leaves:

```text
business fact = valid

NotificationIntent = valid/outstanding as applicable

delivery path = failed / uncertain
```

The owning semantic authority determines any exceptional business consequence of failed communication.

Notifications does not invent one.

---

## 45. Required Post-Commit Communication

Where the owning authority says communication is a durable post-commit obligation:

```text
business mutation
+
durable communication responsibility
        ↓
commit/recoverable consequence
```

must remain recoverable.

Target 16 defines the provider execution semantics.

Target 18 will define the concrete generic background-work/event mechanism.

---

## 46. Read Architecture

Initial production reads use Notification owner queries and request-scoped derivation for:

```text
NotificationIntent

NotificationDispatch

DeliveryAttempt

DeliveryEvidence
```

These are authoritative Notification facts, not projections merely because they are read.

No dedicated Notification read database/cache/search index is initially required.

---

## 47. Notification Read State

`READ` / `UNREAD` remains presentation/projection state under v1.0.

It does not become DeliveryEvidence strength or business acknowledgement.

---

## 48. Data Protection Boundary

Provider execution receives only data necessary for the exact communication purpose.

Target 16 preserves:

```text
purpose limitation
data minimisation
secret exclusion
channel sensitivity rules
merchant isolation
```

Exact:

```text
retention periods
jurisdiction-specific transfer basis
marketing-consent law
provider data-residency policy
```

remain Target 17 / applicable policy scope.

---

## 49. Credentials

Raw:

```text
API keys
OAuth tokens
signing secrets
provider credentials
```

MUST NOT enter:

```text
NotificationIntent
NotificationDispatch
DeliveryAttempt ordinary payload
DeliveryEvidence
Domain Events
AI context
ordinary logs
```

ProviderConnection/credential references are used instead.

MS-PROT-067 remains credential authority.

---

## 50. Observability

Provider metrics, latency, errors and health may contribute diagnostic/readiness evidence.

They do not become:

```text
DeliveryEvidence
Provider Readiness
Notification business state
```

merely because they are observed.

Exact operational reconciliation/telemetry is Target 19.

---

## 51. AI Boundary

AI MAY:

```text
explain provider delivery evidence
summarise notification failure
help prepare safe diagnostic information
suggest a permitted operational response
```

AI MUST NOT:

```text
create communication permission
select unregistered provider
override current preference/consent
declare Provider Readiness
declare provider delivery without evidence
turn read/open into business acknowledgement
decide blind retry is safe
```

---

## 52. Failure Semantics

Target 16 preserves at least:

```text
NOTIFICATION_NOT_APPLICABLE

RECIPIENT_NOT_ELIGIBLE

NO_PERMITTED_CHANNEL

FULFILMENT_PATH_NOT_AVAILABLE

PROVIDER_NOT_READY

ATTEMPT_IDENTITY_CONFLICT

PROVIDER_REJECTED

KNOWN_FAILURE_BEFORE_PROVIDER_EFFECT

DELIVERY_EXECUTION_UNCERTAIN

PROVIDER_EVIDENCE_INVALID

PROVIDER_EVIDENCE_UNCORRELATED

TECHNICAL_FAILURE
```

These are Notification/provider execution classifications.

They are not business lifecycle states.

---

## 53. Atomicity

The following local invariant is required:

```text
authoritative acceptance of exact DeliveryAttempt
        ↓
COMMIT
        ↓
provider side effect may begin
```

Provider execution and the local attempt creation are not one distributed ACID transaction.

Provider evidence is recorded in later local transactions.

No universal transaction spans:

```text
business capability
+
Notification
+
external provider
```

---

## 54. Concurrency

Concurrent workers attempting the same logical DeliveryAttempt must converge on one attempt identity.

They MUST NOT independently produce uncontrolled external provider effects.

Exact locking/CAS/queue leasing is implementation detail provided the semantic invariant is preserved.

---

## 55. Falsification — Provider Readiness UNKNOWN

```text
Dispatch exists
provider health cannot be established
```

Expected:

```text
no new provider side effect
Dispatch remains
```

**PASS**

---

## 56. Falsification — DEGRADED Provider, Required Evidence Unavailable

```text
provider can send
but cannot supply evidence required by Notification Contract
```

Expected:

```text
do not treat DEGRADED as admissible
```

**PASS**

---

## 57. Falsification — Platform Email Provider

```text
merchant has no email-provider account
Main Street supplies transactional email
```

Expected:

```text
platform-routed fulfilment
platform-scoped ProviderConnection
merchant remains business sender/counterparty presentation
```

**PASS**

---

## 58. Falsification — Different Providers Per Channel

```text
EMAIL → Provider A
SMS   → Provider B
PUSH  → Provider C
```

Expected:

```text
all fulfil same notification/message-delivery role
under distinct channel contexts
no provider-specific Notification semantics
```

**PASS**

---

## 59. Falsification — Provider Does Not Support SMS

Expected:

```text
SMS path statically incompatible
not merely runtime NOT_READY
```

**PASS**

---

## 60. Falsification — Timeout After Possible Acceptance

```text
Attempt A1 committed
provider request transmitted
response lost
```

Expected:

```text
A1 = execution uncertain
no automatic A2
no automatic SMS fallback
```

**PASS**

---

## 61. Falsification — Safe Provider Idempotency Replay

```text
A1 uncertain
provider guarantees same idempotency key
produces same logical effect
```

Expected:

```text
replay A1 with same provider effect identity
not new A2
```

subject to current delivery eligibility.

**PASS**

---

## 62. Falsification — Known Pre-Effect Failure

```text
A1 definitively failed before provider effect
```

Expected:

```text
new A2 may be authorised
```

**PASS**

---

## 63. Falsification — Current Implementation Uses Dispatch ID as Idempotency Key

```text
Dispatch D1
Attempt A1
Attempt A2
```

Expected design:

```text
D1 remains logical Dispatch identity

A1 and A2 remain distinct provider-effect identities
```

A universal provider key equal to D1 MUST NOT collapse A1/A2 when both are legitimate distinct effects.

**PASS**

---

## 64. Falsification — Provider A Uncertain, Provider B Available

Expected:

```text
do not silently switch to B
```

until A is reconciled or a separately accepted duplicate-safe rule permits another effect.

**PASS**

---

## 65. Falsification — EMAIL Known Failed, SMS Fallback Permitted

Expected:

```text
EMAIL Dispatch preserved

new SMS Dispatch
    under same NotificationIntent

new SMS DeliveryAttempt
```

not mutation of the EMAIL attempt.

**PASS**

---

## 66. Falsification — Duplicate Webhook

Expected:

```text
one semantic DeliveryEvidence fact/effect
```

according to provider evidence identity.

**PASS**

---

## 67. Falsification — Provider Reports Delivered

Expected:

```text
provider-reported delivery evidence
```

not:

```text
recipient read
business acknowledged
```

**PASS**

---

## 68. Falsification — Provider Switched After Send

```text
A1 used Provider A
new infrastructure uses Provider B
callback from A arrives
```

Expected:

```text
callback correlated to A1 historical path
```

**PASS**

---

## 69. Falsification — Staff Loses Authority Before Attempt

Expected:

```text
current eligibility fails
no operational payload externalised
```

**PASS**

---

## 70. Falsification — Merchant Changes Customer Email

Where contract uses current-recipient contact and no attempt has yet externalised:

```text
re-resolve under current semantics
preserve old Dispatch/endpoint provenance
do not silently rewrite historical attempt
```

**PASS**

---

## 71. Falsification — Marketing Consent Missing

```text
EMAIL technically available
provider READY
recipient prefers EMAIL
```

but applicable marketing authority does not permit communication.

Expected:

```text
no provider attempt
```

**PASS**

---

## 72. Falsification — IN_APP Internal Delivery

Expected:

```text
INTERNAL fulfiller
no fake ProviderConnection
same Notification ownership boundaries
```

**PASS**

---

## 73. Rejected Alternatives

Target 16 rejects:

```text
SendGridNotification / TwilioNotification domain types

provider selected by worker parameter

provider selected by frontend

provider readiness stored in NotificationIntent

provider availability activating a notification

one global providerHealthy boolean

ProviderConnection existence = READY

Provider A timeout = known failure

automatic provider substitution

automatic cross-channel fallback after uncertainty

Dispatch identity as universal physical-attempt idempotency key

provider callback = business mutation

provider delivered = recipient read

recipient read = business acknowledgement

provider status = universal Notification status

external send before durable attempt identity

raw credentials in attempt/evidence

Notification provider outage rolling back Order/Booking/etc.

speculative Notification CQRS/read platform
```

---

## 74. Deferred Scope

Deferred:

```text
specific email provider

specific SMS provider

specific push provider

exact provider SDK/client

provider-specific status-to-evidence mapping tables

webhook framework implementation

exact retry/backoff counts — Target 18

worker/queue/broker technology — Target 18

scheduled reminder worker machinery — Target 18

retention periods — Target 17

jurisdiction-specific consent/data-transfer law — Target 17

notification/provider operational dashboards — Target 19

reconciliation operator UI — Target 19

exact API DTO/routes — Target 20

preference-management API/UI — Target 20

sender-domain/DKIM/SPF infrastructure implementation

template/rendering technology

batch/digest/quiet-hours semantics

marketing campaign architecture

open/click analytics policy
```

---

## 75. Conformance

A conforming implementation MUST prove:

```text
[ ] notification / message-delivery is the provider-neutral role

[ ] communication reason remains with originating owner

[ ] provider/internal execution is channel-qualified

[ ] static provider compatibility precedes runtime readiness

[ ] provider Readiness is revalidated before new external effects

[ ] DEGRADED cannot weaken hard Notification Contract obligations

[ ] DeliveryAttempt exists durably before provider side effect

[ ] DeliveryAttempt path/provenance is immutable

[ ] same-attempt retry preserves exact provider effect identity

[ ] distinct physical attempts may have distinct provider identities
    under one Dispatch

[ ] Dispatch identity is not blindly used as universal provider
    request identity

[ ] provider/connection cannot silently substitute within an attempt

[ ] provider fallback and channel fallback remain distinct

[ ] channel fallback creates a separate Dispatch

[ ] uncertain provider effect blocks blind duplicate/fallback

[ ] synchronous and asynchronous provider evidence pass
    authentication/correlation/interpretation

[ ] duplicate callbacks are idempotent

[ ] old-provider evidence remains processable after provider switch

[ ] DeliveryEvidence does not become recipient awareness

[ ] recipient awareness does not become business acknowledgement

[ ] current recipient/exposure/preference authority is revalidated
    where required before externalisation

[ ] external provider failure does not rewrite business truth

[ ] raw provider credentials do not enter Notification domain records

[ ] Notification source reads remain owner-query/request-scoped initially

[ ] no speculative provider-specific Notification domain is created
```

---

## 76. Governing Principle

> **A Notification provider performs one already-authorised technical delivery responsibility; it does not decide why communication exists, who may receive it, which business consequence follows, or whether the recipient acknowledged anything. Every external effect begins from an exact durable DeliveryAttempt bound to an accepted fulfilment path and current Provider Readiness. Provider evidence is authenticated, correlated and interpreted before becoming Notification DeliveryEvidence; uncertain effects are never converted into convenient failure, and provider or channel fallback never manufactures a second communication without independent authority.**
