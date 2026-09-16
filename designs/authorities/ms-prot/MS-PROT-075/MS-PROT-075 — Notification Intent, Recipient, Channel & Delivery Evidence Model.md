# MS-PROT-075 — Notification Intent, Recipient, Channel & Delivery Evidence Model

**Document ID:** MS-PROT-075  
**Version:** 1.0  
**Status:** ACCEPTED  
**Depends on:** MS-PROT-026, MS-PROT-027, MS-PROT-031, MS-PROT-043, MS-PROT-053, MS-PROT-059, MS-PROT-062, MS-PROT-063 v1.1, MS-PROT-064, MS-PROT-065, MS-PROT-069, MS-PROT-070, MS-PROT-073, MS-PROT-074  
**Closes:** Promoted design-review item — Notification intent, recipient preference, channel selection, delivery attempt and delivery evidence  
**Purpose:** Define the cross-cutting notification authority for durable communication intent, recipient/channel resolution, content projection, preference application, dispatch, delivery attempts and delivery evidence without allowing notification infrastructure to own business facts, marketing consent, capability policy, staff access, provider truth or business acknowledgement.

---

## 1. Governing Principle

> **The owning capability or process determines why a communication is required and what authoritative facts it may communicate. Notifications owns the durable communication intent, recipient/channel resolution, notification presentation and delivery lifecycle. Delivery infrastructure does not become business authority, and evidence that a message was transported does not prove that the recipient understood or acknowledged the underlying business fact.**

Canonical separation:

```text
Domain Event / authoritative business fact
    ≠ Notification Intent

Notification Intent
    ≠ Notification Dispatch

Notification Preference
    ≠ consent / lawful communication authority
    ≠ data Exposure authority

Delivery Attempt
    ≠ Delivery Evidence

provider accepted
    ≠ recipient received

provider reported delivered
    ≠ recipient read

notification read/opened
    ≠ business acknowledgement
```

Notifications is a cross-cutting communication authority. It is not a universal workflow engine, Marketing authority, customer-conversation authority, capability business owner or provider-specific messaging subsystem.

---

## 2. Architectural Placement and Ownership

Notifications owns:

```text
NotificationContract
NotificationIntent
NotificationRecipient
NotificationPreference
NotificationDispatch
DeliveryAttempt
DeliveryEvidence
notification-specific content projection/rendering contract
notification read/unread presentation state where applicable
```

Notifications does NOT own:

```text
Order / Booking / Appointment / Payment / Inventory business truth
Domain Event ownership
business reminder policy or deadline meaning
Campaign / segmentation / marketing-consent semantics
Identity / CustomerContext / Merchant Membership truth
Merchant Controller / staff Role authority
Merchant Operational Device Authorisation
Data Protection / Exposure authority
provider account/connection identity
background scheduler authority
AuditRecord authority
business acknowledgement / acceptance semantics
```

The owning capability/process remains authoritative for why the communication exists, when it is applicable and what business consequence follows if communication cannot be completed.

---

## 3. Canonical Responsibility Graph

```text
Authoritative business fact / process condition
                │
                │ capability-defined communication requirement
                ▼
        Notification Contract
                │
                ▼
        Notification Intent
                │
        current applicability
                │
                ▼
       Recipient Resolution
                │
                ▼
    Exposure / communication eligibility
                │
                ▼
      Preference Resolution
                │
                ▼
       Channel Selection
                │
                ▼
     Content Projection/Rendering
                │
                ▼
      Notification Dispatch
                │
        ┌───────┴────────┐
        ▼                ▼
 Delivery Attempt    later valid retry
        │
        ▼
 Delivery Evidence
```

The diagram is conceptual. Implementations MAY optimise ordering where security/privacy is preserved, but no optimisation may use a later delivery-stage success to manufacture an earlier semantic permission.

---

## 4. Notification Contract

A **Notification Contract** is the registered communication contract contributed by the capability, process or platform authority that understands the underlying communication requirement.

Conceptually:

```text
NotificationContract
{
    notificationType
    semanticOwner
    sourceFactOrProcess
    recipientResolutionContract
    contentProjectionContract
    permittedChannels
    preferenceApplicability
    requiredDeliveryGuarantee
    exposureAndDataHandlingConstraints
    senderPresentation
}
```

The representation is conceptual and does not mandate one Java type, table or configuration format.

The Notification Contract MUST NOT invent capability semantics.

Examples:

```text
Appointment confirmation requirement
    owned by Scheduling / Appointment semantics

Order cancellation communication requirement
    owned by Ordering

PIN-changed security notification requirement
    owned by the applicable security authority
```

Notifications supplies the cross-cutting contract and lifecycle used to realise that communication responsibility.

---

## 5. Notification Contract Does Not Own Business Policy

Notifications MUST NOT decide independently that:

```text
all appointments receive a 24-hour reminder
all Orders require SMS confirmation
all payment failures alert the merchant
all low-stock conditions email Managers
```

Those requirements must originate from the owning capability/process/platform-security authority.

Therefore:

```text
Capability / Process
    owns why, when and whether communication is required

Notifications
    owns how an accepted communication responsibility is represented and delivered
```

This prevents a generic Notification subsystem from becoming a hidden business-policy engine.

---

## 6. Notification Intent

A **Notification Intent** is one logical durable communication responsibility created under a Notification Contract.

Conceptually:

```text
NotificationIntent
{
    intentIdentity
    notificationContract
    ownerScope
    sourceFactOrProcessReference
    recipientResolutionBasis
    correlation / causation
    createdAt
    lifecycle / current disposition
}
```

A Notification Intent is not a Domain Event and does not replace the authoritative business fact that caused it.

One Domain Event MAY produce:

```text
zero Notification Intents
one Notification Intent
multiple Notification Intents
```

according to accepted communication requirements.

Example:

```text
OrderConfirmed
    ↓
customer confirmation intent
    +
merchant operational alert intent
```

The two intents are independent because they may have different recipients, Exposure rules, channels and delivery guarantees.

---

## 7. Logical Intent Identity and Idempotency

One logical communication responsibility MUST have stable identity sufficient to prevent duplicate source-event/background delivery from multiplying the same notification responsibility.

Canonical relationship:

```text
one logical communication responsibility
        ↓
one NotificationIntent
        ↓
one or more NotificationDispatches
        ↓
one or more physical DeliveryAttempts
```

Duplicate event delivery, duplicate background wake-up or process retry MUST NOT automatically create a second Notification Intent for the same logical responsibility.

Payload equality MUST NOT be the sole duplicate criterion because two deliberately separate identical notifications may be valid independent intents.

---

## 8. Notification Recipient

A **Notification Recipient** represents the semantic audience/recipient relationship for one Notification Intent.

Examples MAY include:

```text
CustomerContext
commitment-bound guest/contact
Merchant Controller
Merchant Membership
current responsibility-bearing workforce audience
platform/security Identity
```

The recipient is not a delivery endpoint.

Hard distinction:

```text
CustomerContext
    ≠ email address

Merchant Membership
    ≠ push token

Identity
    ≠ telephone number
```

Delivery endpoints are channel-specific contact/technical facts resolved for the recipient under applicable authority.

---

## 9. Recipient Resolution Basis

Notification Contracts MUST distinguish the basis on which recipients are resolved.

At minimum, Main Street MUST be able to represent the behavioural distinction between:

### 9.1 Commitment-bound recipient

The communication is directed to the participant/contact already associated with an authoritative commitment or accepted interaction.

Example:

```text
Order O42
    ↓
customer/contact bound to O42
```

Where the owning semantics preserve a specific contact for the commitment, current unrelated workforce/customer-directory changes do not silently substitute a different recipient.

### 9.2 Current relationship/responsibility recipient

The communication is intended for whoever currently holds an applicable relationship, role or responsibility.

Example:

```text
current Inventory Managers
current assigned Staff Membership
current Merchant Controllers
```

These recipients MUST be re-resolved/revalidated before externalising operational information.

A person who lost the relevant current relationship/authority MUST NOT receive operational details merely because they were eligible when the Notification Intent was created.

---

## 10. Guest Recipients Are Supported

A Notification Recipient does not require a global Main Street customer account.

Guest/customer-context interactions may legitimately bind a communication endpoint required for the accepted interaction.

Examples:

```text
guest booking confirmation
order collection-ready notice
appointment cancellation notice
secure interaction follow-up
```

The existence of a valid guest/contact recipient MUST NOT be interpreted as creation of a global CustomerAccount.

MS-PROT-043 remains authoritative for CustomerContext/CustomerAccount distinctions.

---

## 11. Notification Preference

A **Notification Preference** is a recipient/context preference that constrains eligible notification delivery choices where the Notification Contract permits preference control.

Conceptually:

```text
NotificationPreference
{
    preferenceIdentity
    subject / recipient context
    notification class / scope
    preferred or disabled channels
    effective interval
    provenance
}
```

Exact preference representation is downstream.

A preference MUST NOT become:

```text
marketing consent
lawful-basis determination
Exposure authority
Merchant Role authority
business-operation authority
```

Canonical rule:

> **Notification Preference may narrow or rank already-permitted communication choices; it MUST NOT create permission to communicate information that another governing authority forbids.**

---

## 12. Preference Is Not Consent or Lawful Communication Authority

Transactional/operational communication and marketing communication may use the same technical channels while remaining semantically distinct.

Example:

```text
EMAIL channel
    may carry
        appointment confirmation
        order cancellation
        security notice
        marketing campaign
```

Shared channel infrastructure does not imply shared consent semantics.

Therefore:

```text
customer prefers EMAIL
    ≠ marketing email consent

customer disables marketing
    ≠ suppress every transactional/security communication
```

Marketing campaign eligibility, segmentation, promotional consent and applicable legal/policy authority remain outside MS-PROT-075.

Notifications MUST consume the result of those authorities where marketing generates Notification Intents.

---

## 13. Required Communication and Configurable Preference

A Notification Contract MUST define how recipient preferences participate in the communication requirement.

Main Street MUST NOT use a universal rule such as:

```text
recipient opted out
    ↓
suppress every communication
```

Nor may a Notification Contract label a communication `required` merely to bypass an independently applicable privacy/legal/security restriction.

Canonical composition:

```text
source communication requirement
        +
recipient/context Exposure eligibility
        +
applicable consent/legal/policy authority
        +
preference contract
        ↓
permitted delivery choices
```

Security-critical, contractual, transactional, reminder and marketing communications MAY have different preference applicability because their owning obligations differ.

---

## 14. Notification Dispatch

A **Notification Dispatch** is one logical recipient-and-channel delivery obligation derived from a Notification Intent after current recipient/channel eligibility has been established.

Conceptually:

```text
NotificationDispatch
{
    dispatchIdentity
    notificationIntent
    resolvedRecipient
    channel
    endpointReference
    contentProjectionReference / rendering provenance
    createdAt
    current delivery disposition
}
```

One Notification Intent MAY create multiple Dispatches where the Notification Contract legitimately requires/performs multiple channels or recipients.

A Dispatch is not a physical provider request.

---

## 15. Channel Selection

Channel selection MUST use only channels already permitted by the Notification Contract and applicable recipient/context authority.

Conceptually:

```text
contract permits channel?
        +
recipient has usable endpoint?
        +
recipient/context may receive this content?
        +
preference permits/ranks channel?
        +
applicable consent/policy permits channel?
        +
staff device restriction satisfied where applicable?
        +
channel/provider operationally usable?
        ↓
select dispatch channel(s)
```

Channel selection MUST NOT create new communication authority.

Provider failure MAY select another channel only when that channel was already independently permitted.

---

## 16. Channel Semantics Are Provider-Neutral

Canonical notification channels may include concepts such as:

```text
IN_APP
EMAIL
PUSH
SMS
```

where supported.

Notification semantics MUST NOT be expressed primarily as provider-specific identities such as:

```text
SendGridNotification
TwilioNotification
FirebaseNotification
```

Provider-specific delivery is an adapter/infrastructure concern.

Conceptual composition:

```text
Notification Dispatch
       ↓
Channel Port
       ↓
Provider Adapter
```

Changing a provider MUST NOT rewrite the business fact or Notification Intent merely because transport infrastructure changes.

---

## 17. Notification Content Is a Projection

Notification content is a recipient/channel-specific projection of authoritative facts; it is not another authoritative copy of the business object.

Example:

```text
Appointment
Merchant Profile
Customer / commitment context
Exposure / data-handling rules
        ↓
Notification content projection
        ↓
EMAIL / PUSH / IN_APP representation
```

Content rendering MUST preserve the owning semantic meaning while exposing only the fields permitted and necessary for the notification purpose.

Templates/profile technology is downstream implementation detail.

---

## 18. No Business-Type Notification Branching

Generic Notification infrastructure MUST NOT require business-type branching such as:

```text
if merchantType == SALON
if merchantType == MOTEL
if merchantType == SOLICITOR
```

Capabilities/processes provide registered Notification Contracts and semantic content parameters.

Presentation/rendering infrastructure applies the accepted contract without inventing niche business semantics.

---

## 19. Customer Communications and Merchant Presentation

Where the owning product/capability contract requires merchant-branded customer communication, Main Street infrastructure MUST preserve that sender presentation without becoming the apparent business counterparty.

Example:

```text
"Bella Salon — your appointment is confirmed"
```

rather than implying that Main Street is the merchant providing the service.

Sender presentation MUST NOT alter the actual provider/infrastructure provenance needed for operational/security evidence.

---

## 20. Staff Private-Device Exposure Rule

MS-PROT-074 and MS-PROT-063 v1.1 require Merchant Operational Device Authorisation before staff may receive Merchant Operational Projections or perform merchant operations.

Notifications MUST NOT circumvent that boundary.

Hard invariant:

> **Notification delivery MUST NOT expose merchant operational information to a Staff Identity through a channel/device context that would not be permitted to expose the same information directly.**

Examples on a private/non-operational staff device:

```text
"Your Bella Salon PIN was changed."
    → permitted security/membership information

"You have business activity requiring attention. Open Main Street on an authorised device."
    → MAY be permitted where content contains no prohibited operational payload

"John Smith booked haircut at 14:30; phone 07...; payment £95."
    → prohibited without authorised Merchant Operational Device Context
```

Staff preference for PUSH/EMAIL/SMS MUST NOT override this Exposure/security rule.

---

## 21. Operational Device-Bound Push

Detailed merchant-operational push delivery to staff is permitted only where the target push/application endpoint is bound to a currently valid Merchant Operational Device Context for the same Merchant Scope and current recipient relationship/authority remains valid.

A push token or application identifier alone MUST NOT establish Merchant Operational Device Authorisation.

Device/security ownership remains MS-PROT-074 / MS-PROT-063 v1.1 / MS-PROT-067.

---

## 22. Reminder Semantics

A generic `Reminder Engine` MUST NOT own business reminder meaning.

The owning capability/process defines:

```text
why reminder exists
when reminder becomes due
whether reminder remains applicable
what consequence follows if it cannot be delivered
```

MS-PROT-065 may persist/wake the future work.

Canonical flow:

```text
Appointment confirmed
        ↓
Scheduling/process establishes reminder responsibility
        ↓
DurableWorkInstruction
        ↓
due-time wake-up
        ↓
revalidate current Appointment/process state
        │
        ├── still applicable → NotificationIntent / Dispatch
        └── no longer applicable → suppress / NO_LONGER_APPLICABLE
```

A scheduled reminder MUST NOT reserve future authority to communicate stale business information.

---

## 23. Current Applicability Revalidation

Before externalising a notification whose eligibility can change over time, Main Street MUST re-evaluate applicable current authority/relationship/exposure facts.

Examples include:

```text
recipient preference changed
Staff Membership ended
Role/Group responsibility changed
Merchant Operational Device Authorisation revoked
customer contact/consent state changed where current-state semantics apply
source business condition no longer applicable
```

The existence of a prior Notification Intent does not freeze mutable current authority unless the owning Notification Contract explicitly binds a historical commitment recipient/contact for the communication.

---

## 24. Delivery Attempt

A **Delivery Attempt** is one physical attempt to execute a Notification Dispatch through a selected channel/provider path.

Conceptually:

```text
DeliveryAttempt
{
    attemptIdentity
    dispatchIdentity
    attemptedAt
    channel / provider context
    request/idempotency reference where supported
    immediate outcome / certainty
    failure reference where applicable
}
```

Multiple attempts MAY belong to one Dispatch.

A retry MUST NOT become a new Notification Intent.

---

## 25. Delivery Is Not Exactly Once

Main Street MUST NOT assume exactly-once external notification delivery.

Notification delivery may experience:

```text
duplicate provider request
retry
late delivery
provider timeout after acceptance
worker crash after provider side effect
out-of-order status evidence
lost acknowledgement
```

Where supported, stable Dispatch identity SHOULD be propagated into provider idempotency/deduplication mechanisms.

Correctness MUST rely on stable logical identities, safe retry contracts, current revalidation and reconciliation rather than a fictitious exactly-once transport guarantee.

---

## 26. Delivery Evidence

**Delivery Evidence** is notification-owned evidence describing what Main Street can substantiate about a Dispatch/Attempt.

Conceptually:

```text
DeliveryEvidence
{
    evidenceIdentity
    dispatchIdentity
    attemptIdentity?
    channel / provider context
    evidenceType
    observedAt
    providerReference?
    provenance
}
```

Possible evidence MAY include:

```text
provider accepted
provider rejected
provider reported delivered
provider reported bounced
provider reported opened
in-app notification opened
execution uncertain
```

Evidence names MUST preserve provenance/meaning. Provider-reported evidence MUST NOT be silently promoted into stronger human-observation facts.

---

## 27. Delivery Evidence Is Not Recipient Awareness

Hard distinctions:

```text
provider accepted
    ≠ provider delivered

provider delivered
    ≠ recipient read

provider reported opened
    ≠ recipient understood

notification opened/read
    ≠ business acknowledgement
```

If a capability requires explicit acknowledgement, acceptance, confirmation or response, it MUST expose an owning capability Operation/Interaction for that business fact.

Notification read/open state MUST NOT manufacture business acknowledgement.

---

## 28. Provider Timeout and Execution Uncertainty

A provider/network timeout after a side-effecting notification request MUST NOT automatically be interpreted as known non-delivery.

Canonical flow:

```text
request sent to provider
        ↓
timeout / reset / response lost
        ↓
can provider effect be established?
        ├── YES → record supported evidence
        └── NO  → preserve DELIVERY EXECUTION UNCERTAIN
```

Blind retry is prohibited where duplicate external delivery may occur unless provider idempotency/retry safety is independently established.

MS-PROT-069 remains authoritative for communication/execution uncertainty.

---

## 29. Delivery Failure Usually Does Not Roll Back Business State

For ordinary post-commit notifications:

```text
Order = CONFIRMED
NotificationIntent = outstanding / delivery failed
```

is valid.

Notification failure MUST NOT normally cause:

```text
Order rollback
Appointment cancellation
Payment reversal
business fact deletion
```

The originating capability/process determines whether one particular communication is:

```text
best-effort enhancement
durable post-commit obligation
completion-critical communication
```

These phrases describe behavioural classes; v1.0 does not mandate a universal enum.

Where a communication is completion-critical, that requirement MUST be explicitly established by its owning semantic authority rather than inferred by Notifications.

---

## 30. In-App Read State

Notifications MAY own presentation/read state such as:

```text
UNREAD
READ
```

for an in-app notification.

This state belongs to the notification projection and MUST NOT become a business lifecycle state or acknowledgement unless a separate capability operation explicitly establishes such a fact.

---

## 31. Delivery Evidence Is Not AuditRecord

Delivery Evidence and AuditRecord are distinct evidence classes.

```text
DeliveryEvidence
    evidence about notification transport/delivery

AuditRecord
    integrity/accountability evidence governed by MS-PROT-064
```

Ordinary notification delivery SHOULD NOT create durable AuditRecords merely because delivery evidence exists.

Audit evidence MAY independently be required for materially sensitive actions such as:

```text
notification-security policy administration
high-risk notification suppression/override
security-notification preference changes where permitted
```

according to MS-PROT-064.

---

## 32. Data Protection and Minimisation

Notification content and delivery metadata MUST respect MS-PROT-053 purpose limitation, handling classification, Exposure and retention requirements.

Notifications MUST send only information necessary for the communication purpose.

Examples:

```text
"Your order is ready for collection."
```

is preferred over unrelated full Order/customer history where those details are not required.

`SECRET` credential material MUST NOT be placed in ordinary notification content, Delivery Evidence or provider metadata.

Security codes/links requiring protected credential semantics remain governed by applicable security/credential authority and MUST NOT turn notification history into a general secret store.

---

## 33. Preference Changes Before Delivery

Where a Notification Contract allows preference-controlled delivery, the current applicable preference SHOULD be re-evaluated before Dispatch/Attempt where the preference can change after Intent creation.

Example:

```text
Monday: optional reminder scheduled
Tuesday: recipient disables optional reminders
Wednesday: reminder due
        ↓
current preference evaluated
        ↓
suppress if contract permits preference suppression
```

The historical existence of the Notification Intent is not a reservation of future preference permission.

---

## 34. Workforce Changes Before Delivery

Where a staff notification targets a current workforce responsibility, current Merchant Membership, Group/Role responsibility, Merchant Scope and operational-device Exposure MUST be re-evaluated before operational data leaves Main Street.

Example:

```text
09:00 NotificationIntent created for current Managers
09:01 Sarah removed from Managers
09:02 worker resolves recipients
        ↓
Sarah does not receive operational details
```

Once information has legitimately left Main Street through email/SMS/push, later revocation cannot retract the external copy. Therefore current authority must be evaluated before externalisation.

---

## 35. Marketing Boundary

A Marketing Campaign MAY generate Notification Intents after the Marketing/customer/privacy authorities have established eligible recipients, content and consent/policy conditions.

Conceptually:

```text
Campaign
    ↓
segment / consent / promotion authority
    ↓
eligible communication responsibilities
    ↓
Notification Intents
    ↓
shared EMAIL / SMS / PUSH delivery infrastructure
```

Notifications MUST NOT own:

```text
customer segmentation
marketing-consent determination
campaign eligibility
promotion/discount semantics
campaign business scheduling meaning
```

Shared transport does not transfer semantic ownership.

---

## 36. Resource Protection and Delivery Priority

Notification delivery is subject to MS-PROT-073 Platform Resource Protection.

Bulk notification work MAY be admitted, deferred or rejected according to accepted operational protection policy.

Resource Protection MUST NOT determine notification business criticality merely from channel/provider identity.

Where a Notification Contract establishes a durable communication obligation, temporary resource deferral MUST preserve that obligation according to applicable background-work/resilience semantics rather than silently deleting it.

---

## 37. Commercial Entitlement and Channel Availability

Commercial Entitlement MAY affect access to a notification channel or platform feature where an accepted Commercial Entitlement Definition establishes that relationship.

However:

```text
channel entitlement loss
    ≠ underlying business fact removed
    ≠ Notification Intent erased
```

If another channel is independently permitted by the Notification Contract, recipient authority, preferences and applicable consent/policy, that channel MAY satisfy the communication responsibility.

Notifications MUST NOT infer fallback permission solely from plan identity.

---

## 38. Merchant Scope Isolation

Every merchant-scoped Notification Contract, Intent, Recipient, Dispatch, Attempt and Delivery Evidence MUST preserve sufficient Merchant Scope to prevent cross-merchant data exposure.

One Identity participating in multiple merchants MUST NOT cause Notification state or delivery endpoints to merge merchant operational information across scopes.

Where the same endpoint is used in multiple Merchant Scopes, each communication remains independently scoped and exposed according to its own authority.

---

## 39. Failure Semantics

Notification processing MUST preserve at least the following behavioural distinctions where applicable:

```text
NOTIFICATION_NOT_APPLICABLE
    source/current conditions no longer require delivery

RECIPIENT_NOT_ELIGIBLE
    recipient/current relationship or Exposure no longer permits delivery

NO_PERMITTED_CHANNEL
    no channel currently satisfies contract + endpoint + preference + policy constraints

DISPATCH_PENDING
    logical Dispatch exists but no completed provider attempt yet

DELIVERY_ATTEMPT_FAILED_KNOWN
    provider/transport known not to have accepted/effected the attempt where this can be established

DELIVERY_EXECUTION_UNCERTAIN
    provider may have acted but Main Street cannot safely determine the effect

DELIVERY_EVIDENCE_RECEIVED
    supported evidence exists; strength follows evidence type/provenance
```

These are notification-processing classifications, not business lifecycle states.

Implementations MAY use different names provided the distinctions remain representable.

---

## 40. Retry and Reconciliation

Retry/reconciliation behaviour MUST follow the combination of:

```text
Notification Dispatch identity
provider/channel retry guarantees
MS-PROT-065 background-work semantics
MS-PROT-069 execution certainty
MS-PROT-070 resilience policy
current recipient/exposure/preference applicability
```

A retry library, HTTP status or provider exception MUST NOT itself determine whether resending is safe.

Where provider outcome remains uncertain and no idempotent/safe retry contract exists, Main Street MUST preserve uncertainty and use reconciliation/manual policy as applicable rather than blindly duplicate delivery.

---

## 41. Transaction Boundary

The owning capability business mutation and Notification delivery SHOULD normally remain separate consistency boundaries unless a separately accepted requirement makes notification completion part of the capability's atomic business invariant.

Where communication is a required post-commit consequence:

```text
authoritative business mutation
        +
durable notification/reaction intent
        ↓
COMMIT
        ↓
notification delivery later
```

The architecture MUST preserve enough durable intent/recovery semantics that a successful business commit does not silently lose a required post-commit communication responsibility because a transient worker/provider failure occurred.

Exact outbox/queue/storage technology is implementation-specific.

---

## 42. Concurrency

Concurrent/duplicate generation or processing MUST preserve logical intent/dispatch identity.

Examples:

```text
two consumers observe duplicate OrderConfirmed delivery
    → one logical confirmation NotificationIntent where contract identifies same responsibility

two workers claim same Dispatch
    → infrastructure/attempt semantics must not create uncontrolled duplicate provider effects
```

The required atomic/deduplication boundary applies at the smallest scope necessary to preserve the accepted logical communication invariant.

MS-PROT-075 does not prescribe database locks, queues, compare-and-set, Redis or provider-specific mechanisms.

---

## 43. Sender and Recipient Address Changes

A channel endpoint is not the semantic recipient.

Where the Notification Contract binds to a historical commitment contact, the accepted commitment semantics determine whether the historical/bound contact or current profile contact is authoritative for that communication.

Where the contract uses current-recipient resolution, current endpoint authority applies.

Notifications MUST NOT guess between historical and current contact semantics.

The owning Notification Contract must make the basis explicit.

---

## 44. Retention and Evidence Lifecycle

Notification Intents, Dispatches and Delivery Evidence MUST follow purpose/retention rules under MS-PROT-053.

The need to diagnose delivery MUST NOT justify indefinite retention of complete notification bodies or personal contact data.

Where historical evidence is required, Main Street SHOULD retain the minimum sufficient provenance/outcome evidence rather than unnecessary full payloads, subject to the governing retention contract.

---

## 45. Falsification Scenarios

### 45.1 Guest booking confirmation

A guest has no global Main Street account but a valid commitment-bound contact.

Result:

```text
Booking/Appointment owns confirmation requirement
→ NotificationIntent created
→ merchant-branded confirmation dispatched to permitted endpoint
```

**PASS**

### 45.2 Appointment cancelled before scheduled reminder

Reminder future work becomes due after Appointment cancellation.

Result:

```text
current Appointment/process state revalidated
→ reminder no longer applicable
→ no stale delivery
```

**PASS**

### 45.3 Staff private phone prefers push

Staff Identity has active Membership but private app context lacks Merchant Operational Device Authorisation.

Result:

```text
operational payload push = prohibited
generic security-safe prompt = possible only if no prohibited operational data
```

**PASS**

### 45.4 Staff removed before queued notification resolves

Current workforce relationship no longer qualifies.

Result:

```text
recipient/current Exposure revalidation suppresses operational delivery
```

**PASS**

### 45.5 Customer opts out of marketing

Marketing email becomes ineligible while an independent Order confirmation remains required/permitted by its own contract.

Result:

```text
marketing and transactional eligibility remain distinct
```

**PASS**

### 45.6 Provider timeout after possible acceptance

Provider may have accepted the email but Main Street lost acknowledgement.

Result:

```text
DELIVERY_EXECUTION_UNCERTAIN
→ no blind resend unless safe retry/idempotency proven
```

**PASS**

### 45.7 Duplicate OrderConfirmed event delivery

Same logical customer confirmation responsibility observed twice.

Result:

```text
stable logical intent identity prevents duplicate NotificationIntent
```

**PASS**

### 45.8 Email provider outage with SMS available

SMS is selected only if contract + endpoint + preference/consent + Exposure already permit it.

**PASS**

### 45.9 Push provider reports delivered

Result remains provider delivery evidence; no customer business acknowledgement is manufactured.

**PASS**

### 45.10 Identity works for multiple merchants

Recipient/device resolution preserves Merchant Scope.

Result:

```text
Merchant A communication cannot expose Merchant B operational data
```

**PASS**

### 45.11 Bulk marketing saturation

Resource Protection defers low-priority/bulk work according to accepted operational policy without deleting independently durable notification obligations.

**PASS**

### 45.12 Paid push entitlement lost

Push channel may become unavailable while underlying business fact/Notification Intent remains intact; another independently permitted channel may be selected.

**PASS**

### 45.13 Source object contains sensitive data

Content projection emits only purpose-required fields.

**PASS**

### 45.14 Recipient opens notification

Read/open evidence does not become capability acknowledgement.

**PASS**

---

## 46. Rejected Alternatives

### A. `DomainEvent → sendEmail()` as the architecture

Rejected because it collapses logical communication identity, recipient eligibility, preferences, multiple channels, retries, provider uncertainty and delivery evidence.

### B. One universal `NotificationSent = true`

Rejected because provider acceptance, provider-reported delivery, recipient read/open and business acknowledgement are materially different facts.

### C. Notification system owns reminder timings

Rejected because reminder meaning/deadlines belong to the owning capability/process.

### D. Preference implies consent

Rejected because channel preference cannot establish marketing/privacy/legal authority.

### E. Channel failure automatically falls back to another channel

Rejected unless the fallback channel is independently authorised by contract, recipient/context policy and available endpoint.

### F. Staff role/membership at intent creation freezes recipient eligibility

Rejected because current workforce/device Exposure must be revalidated before operational information leaves Main Street.

### G. Push/email/SMS bypass staff device restriction

Rejected because Notifications cannot become an alternate data-exfiltration path around MS-PROT-074.

### H. Delivery evidence is an AuditRecord

Rejected because notification delivery evidence and governance/accountability audit evidence have different ownership and retention purposes.

### I. Notification read state equals business acknowledgement

Rejected because presentation interaction cannot manufacture capability-owned business acceptance/acknowledgement.

### J. Exactly-once human communication guarantee

Rejected because external provider/network uncertainty makes the claim unjustified; Main Street instead preserves stable logical identity, safe retry/reconciliation and truthful evidence.

### K. Notifications owns Marketing segmentation/consent

Rejected because shared delivery infrastructure does not transfer Marketing/customer/privacy authority.

---

## 47. Hard Invariants

1. The owning capability/process/platform authority determines why a notification exists and whether it remains applicable.
2. `Domain Event ≠ Notification Intent`.
3. One logical communication responsibility has stable Notification Intent identity.
4. `Notification Recipient ≠ delivery endpoint`.
5. Recipient-resolution basis MUST distinguish historical/commitment-bound recipients from current relationship/responsibility recipients where the distinction matters.
6. Notification Preference may constrain already-permitted delivery but MUST NOT create consent, lawful authority, Exposure or business permission.
7. Marketing eligibility/consent remains separate from transactional/operational notification eligibility.
8. Channel selection may choose only independently permitted channels.
9. Notification channels remain provider-neutral semantic concepts.
10. Notification content is a purpose-limited projection, not a second business source of truth.
11. Generic Notification infrastructure MUST NOT branch on merchant business type where registered semantic contracts suffice.
12. Notification delivery MUST NOT bypass Merchant Operational Device Authorisation or other Exposure/security restrictions.
13. Staff operational payloads may be delivered only through contexts independently authorised to expose that information.
14. Reminder meaning/timing remains owned by the applicable capability/process; background infrastructure only wakes/retries work.
15. Mutable recipient/exposure/preference applicability MUST be revalidated before externalisation where the Notification Contract depends on current state.
16. `Notification Dispatch ≠ Delivery Attempt`.
17. Retries do not create new Notification Intents.
18. Exactly-once external delivery MUST NOT be assumed.
19. `Delivery Attempt ≠ Delivery Evidence`.
20. Provider-reported evidence MUST preserve its actual evidence strength/provenance.
21. Provider acceptance/delivery/open evidence MUST NOT become human understanding or business acknowledgement.
22. Notification read/unread state MUST NOT become capability lifecycle/acknowledgement state.
23. Provider timeout may produce delivery execution uncertainty; blind retry is prohibited unless independently safe.
24. Notification delivery failure MUST NOT normally roll back already-valid business facts.
25. Communication criticality/consequence belongs to the originating semantic authority, not Notifications.
26. Delivery Evidence is not AuditRecord.
27. Notification content/evidence MUST obey MS-PROT-053 data minimisation and handling rules.
28. Merchant Scope MUST be preserved through Intent, recipient resolution, Dispatch, Attempt and evidence.
29. Commercial channel entitlement MUST NOT redefine underlying business truth or Notification Intent.
30. Resource Protection may constrain delivery execution but MUST NOT erase durable communication obligations or invent semantic priority.
31. Required post-commit notification responsibilities need durable recovery semantics.
32. Notification retry/reconciliation MUST compose with MS-PROT-065, MS-PROT-069 and MS-PROT-070 rather than inventing separate transport certainty rules.

---

## 48. Deferred / Downstream Decisions

The following remain downstream or separately governed:

```text
specific email/SMS/push providers
push platform technology
template/rendering engine technology
exact retry counts/backoff intervals
provider-specific delivery-status mappings
provider webhook implementation
exact notification preference UI/defaults
exact reminder timings
marketing-consent law/policy details
campaign segmentation/personalisation semantics
exact sender-domain/address infrastructure
notification analytics dashboards
open/click tracking policy and privacy controls
exact local/in-app notification storage
batching/digest rules
quiet-hours policy
escalation chains
cross-channel deduplication UX
```

These MUST NOT be inferred as accepted merely because MS-PROT-075 establishes the notification semantic boundary.

---

## 49. Implementation Fitness Contracts

When implementation resumes, representative architecture/fitness tests SHOULD include:

```text
duplicateSourceEventDoesNotMultiplyLogicalNotificationIntent()
notificationPreferenceCannotGrantMarketingConsent()
staffPrivateDeviceCannotReceiveMerchantOperationalPayload()
removedStaffIsRevalidatedBeforeOperationalDelivery()
providerTimeoutPreservesDeliveryUncertainty()
providerDeliveredDoesNotCreateBusinessAcknowledgement()
notificationReadStateDoesNotMutateCapabilityState()
reminderRevalidatesOwningBusinessStateBeforeSend()
channelFallbackRequiresIndependentPermission()
retryReusesDispatchIdentity()
deliveryEvidenceIsNotAuditRecord()
merchantScopeDoesNotLeakAcrossRecipientResolution()
commercialChannelLossDoesNotEraseNotificationIntent()
notificationContentProjectionMinimisesSensitiveData()
```

These are architectural contracts, not prescriptions for a specific queue, provider SDK or persistence product.

---

## 50. Acceptance Statement

Main Street accepts Notifications as a bounded cross-cutting communication authority that preserves business ownership, audience Exposure, staff device security, channel neutrality and truthful delivery evidence.

> **Business owners define why communication matters; Notifications represents and delivers that responsibility; providers report transport evidence; no delivery signal silently becomes business truth.**
