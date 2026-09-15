# MS-PROT-048 — Capability Fulfilment Provider Contract

**Document ID:** MS-PROT-048  
**Version:** 1.0  
**Status:** **ACCEPTED after cross-provider falsification and manual approval**  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.4, MS-PROT-023, MS-PROT-024 v1.1, MS-PROT-029 v1.1, MS-PROT-038 v1.0, MS-PROT-039 v1.1, MS-PROT-041 v1.0, MS-PROT-047 v1.0, ADR-006  
**Purpose:** Define how Main Street may fulfil active capability responsibilities through internal implementations or external providers without allowing provider choice, provider APIs, provider state or provider outages to redefine Main Street semantics.

---

## 1. Governing decision

> **Capabilities define what Main Street means. Fulfilment roles define what technical responsibility must be discharged. A fulfiller or provider defines how that responsibility is performed. No provider may redefine the capability semantics it helps fulfil.**

Canonical separation:

```text
Registered Main Street capability semantics
        ↓
Applicable capability configuration
        ↓
Required fulfilment roles
        ↓
Fulfilment binding
   ├── Main Street internal fulfiller
   └── supported external provider
        ↓
Provider adapter / integration boundary
        ↓
Provider execution or provider evidence
        ↓
Main Street validation / reconciliation
        ↓
Capability-owned business semantics
```

Hard rule:

> **Provider selection changes execution plumbing, not semantic meaning.**

---

## 2. Relationship to MS-PROT-047

MS-PROT-047 governs bounded configuration of an already-active capability.

MS-PROT-048 governs a different question:

> **How is a required technical fulfilment responsibility satisfied once Main Street already knows what the active semantics are?**

Therefore:

```text
Capability Configuration Contract
        ≠
Capability Fulfilment Provider Contract
```

A provider binding is not a policy value, not a capability toggle and not a merchant-authored semantic decision.

Examples:

```text
booking / confirmation-mode = MERCHANT_CONFIRMATION
    → capability configuration

scheduling fulfilment role = external provider Calendly
    → fulfilment binding
```

---

## 3. Capability and provider are different kinds of things

This distinction is mandatory.

```text
Capability
    answers:
    WHAT can the merchant/platform do?

Provider
    answers:
    HOW is a technical responsibility discharged?
```

Rejected:

```text
CalendlyCapability
StripeCapability
GoogleCalendarCapability
ResendCapability
```

Accepted pattern:

```text
Scheduling capability
    ↓
appointment-scheduling fulfilment role
    ↓
Main Street internal fulfiller
OR
Calendly external provider
```

Likewise:

```text
Payment capability
    ↓
payment-execution fulfilment role
    ↓
supported payment provider
```

The provider does not become a semantic node merely because it participates in fulfilment.

---

## 4. Providers bind to fulfilment roles, not whole capabilities

A capability may require several materially different technical responsibilities.

Therefore Main Street shall not assume:

```text
one capability = one provider
```

Example scheduling stack:

```text
Scheduling semantics
    ├── availability calculation
    ├── appointment commitment
    ├── schedule-information storage
    ├── meeting-link creation
    └── customer notification
```

These may be fulfilled by different implementations.

Example:

```text
Availability / appointment authority
    → Main Street

Calendar representation
    → Google Calendar

Meeting link
    → external meeting provider

Notification delivery
    → external messaging provider
```

Therefore the canonical binding unit is a **Fulfilment Role**.

---

## 5. Fulfilment Role

A Fulfilment Role is a Main Street-defined technical responsibility required by already-defined semantics or platform infrastructure.

Conceptually:

```text
FulfilmentRoleDefinition
{
    ownerContext
    roleIdentifier
    applicability
    obligations
    authorityBoundary
    evidenceContract
    failureContract
}
```

This structure is conceptual. MS-PROT-048 does not mandate a Java representation.

### 5.1 ownerContext

The role belongs either to:

- a capability; or
- an explicitly defined platform service/infrastructure boundary.

A role does not belong to a provider.

### 5.2 roleIdentifier

Stable Main Street identity for the technical responsibility.

Examples:

```text
scheduling / appointment-execution
payment / payment-execution
notification / message-delivery
calendar-service / schedule-information-storage
publication / social-distribution
```

### 5.3 applicability

A role is applicable only when the resolved Main Street model/configuration requires it.

A provider binding cannot make an otherwise-inactive capability or role applicable.

### 5.4 obligations

The exact Main Street-defined responsibilities that a fulfiller must satisfy.

Examples may include:

```text
create appointment
cancel appointment
return authenticated payment result
store calendar representation
publish outbound message
return delivery acknowledgement
```

Provider-specific API methods are not the obligation vocabulary.

### 5.5 authorityBoundary

Defines which facts remain authoritative in Main Street and which facts may be authoritative at the external provider.

### 5.6 evidenceContract

Defines what authenticated/correlated evidence must be returned or retained before Main Street may treat provider work as complete.

### 5.7 failureContract

Defines what happens when fulfilment is unavailable, uncertain, rejected or delayed.

---

## 6. Fulfilment Binding

A Fulfilment Binding selects how one applicable fulfilment role is discharged for a merchant/configuration context.

Conceptually:

```text
FulfilmentBinding
{
    merchantScope
    roleIdentity
    semanticContext
    fulfillerKind
    fulfillerIdentity
    optionalProviderConnectionIdentity
    bindingProvenance
}
```

Where:

```text
fulfillerKind =
    INTERNAL
    EXTERNAL_PROVIDER
```

The binding does not contain executable semantics.

---

## 7. Internal fulfilment and external fulfilment use the same Main Street contract

Main Street may implement a fulfilment role itself or delegate it to a supported external provider.

Example:

```text
Scheduling
    ↓
appointment-execution role
    ├── INTERNAL → Main Street scheduler/booking implementation
    └── EXTERNAL_PROVIDER → Calendly adapter
```

The external option is valid only if it satisfies the same applicable Main Street obligations required by that merchant's resolved model.

Internal implementation is not semantically privileged merely because Main Street owns the code.

External implementation is not semantically privileged merely because the provider owns the external system.

Both are judged against the Main Street fulfilment contract.

---

## 8. Provider Definition

An external provider must be registered by Main Street before it may be bound.

Conceptually:

```text
ProviderDefinition
{
    providerIdentifier
    supportedFulfilmentRoles
    supportedObligations
    interactionContract
    evidenceCapabilities
    connectionRequirements
    securityRequirements
}
```

Provider registration is platform-owned.

The merchant cannot create an arbitrary provider by entering a URL, prompt, script or webhook target and thereby make it semantically executable.

---

## 9. Provider capability claims are not semantic capability definitions

A provider may claim technical support such as:

```text
can create appointments
can cancel appointments
can expose availability
can return payment status
can accept idempotency key
can emit webhook evidence
```

These are fulfilment-support claims.

They are not Main Street capabilities.

Therefore:

```text
provider.supports("cancel appointment")
```

must never be interpreted as:

```text
Cancellation capability exists
```

The semantic capability/operation must already exist in Main Street.

---

## 10. Applicable obligation set

Provider compatibility shall be evaluated against the obligations actually required by the merchant's resolved semantics.

Canonical process:

```text
Active capabilities
        +
resolved capability configuration
        +
applicable operations/requirements
        ↓
Applicable fulfilment obligations
        ↓
Candidate provider support
        ↓
Compatible?
```

This permits a provider to be valid for one merchant configuration but invalid for another without creating business-category branches.

Example:

```text
Merchant A scheduling
    requires create + cancel

Provider X
    supports create + cancel
    → valid

Merchant B scheduling
    requires create + reschedule + resource-aware availability

Provider X
    lacks resource-aware availability
    → invalid for this binding
```

Main Street must not weaken Merchant B's semantic contract merely to accommodate Provider X.

---

## 11. Provider binding cannot activate semantics

Hard invariant:

> **A provider binding cannot activate a capability, operation, policy, relationship or requirement.**

Rejected:

```text
merchant connects Calendly
        ↓
Scheduling silently becomes active
```

Accepted:

```text
Scheduling already selected/inferred and approved
        ↓
appointment-execution role becomes applicable
        ↓
merchant chooses/retains supported Calendly fulfilment
```

Onboarding may use evidence of an existing Calendly account to propose Scheduling, but inference remains advisory and capability activation remains governed by the semantic configuration flow.

---

## 12. Interaction classes

External fulfilment does not have one universal transaction pattern. Main Street shall distinguish at least the following interaction classes conceptually.

### 12.1 Main Street authoritative, provider performs external side effect

Examples:

```text
notification delivery
calendar representation
social publication
```

Pattern:

```text
Main Street authoritative fact commits
        ↓
durable post-commit delivery
        ↓
provider attempts side effect
        ↓
acknowledgement / retry / reconciliation
```

ADR-006 governs this boundary.

Provider failure must not erase the committed Main Street business fact unless accepted business semantics define a later compensating action.

### 12.2 Provider authoritative for an external fact

Examples:

```text
payment provider transaction result
Google verification status
courier acceptance
```

Pattern:

```text
provider owns external fact
        ↓
authenticated provider evidence
        ↓
Main Street validates/correlates
        ↓
capability/process evaluates consequence
        ↓
Main Street business state changes through owning semantics
```

Main Street shall not fabricate the external fact.

### 12.3 Delegated capability execution

Example:

```text
external scheduling provider creates/cancels an appointment-like external record
```

Pattern:

```text
Main Street semantic operation / approved external interaction
        ↓
provider performs delegated action
        ↓
provider returns correlated evidence
        ↓
Main Street validates the evidence against the captured semantic contract
        ↓
Main Street records/reconciles the corresponding business consequence
```

The provider may be authoritative for the provider-side record.

Main Street remains authoritative for what that record means inside Main Street.

### 12.4 Read-only/enrichment provider

Examples:

```text
map/distance information
analytics import
external profile information
```

Provider data may inform projections or decisions where accepted semantics permit it.

It does not gain mutation authority merely because Main Street can read it.

---

## 13. Provider states are not Main Street states

External providers often expose provider-specific state machines.

Example:

```text
Provider payment states:
PENDING
AUTHORIZED
CAPTURED
FAILED
REFUNDED
```

or provider-specific variants.

Main Street shall not automatically copy those strings into capability lifecycle states.

Required pattern:

```text
Provider state/evidence
        ↓
registered provider interpretation/mapping
        ↓
Main Street fact/decision
        ↓
capability-owned operation/state consequence
```

No generic string-mapping convention may silently create business semantics.

---

## 14. Uncertain external outcomes remain uncertain

External calls can fail after the provider acts but before Main Street receives confirmation.

Therefore:

```text
timeout
network error
lost callback
connection reset
```

must not automatically mean:

```text
provider operation failed
```

Where provider outcome is uncertain:

```text
UNKNOWN / RECONCILIATION_REQUIRED
```

or another accepted process-specific uncertainty outcome must be preserved until reconciled.

MS-PROT-024 remains authoritative: uncertain external outcomes must not be silently converted into success or failure.

---

## 15. Provider connection is operational state, not semantic configuration

A provider binding and a provider connection are different.

```text
FulfilmentBinding
    → which supported fulfiller is selected for a role

ProviderConnection
    → whether/how this merchant is currently authorised and connected
```

Conceptually a ProviderConnection may contain or reference:

```text
merchant scope
provider account identity
authorisation state
credential/secret references
external account identifiers
connection health
webhook subscription state
sync cursor
last reconciliation time
```

Secrets, tokens and mutable connection health shall not be embedded in the semantic registry or capability configuration contract.

---

## 16. Binding provenance must be captured

Provider choice can materially change where execution is routed.

Therefore execution must be able to determine which binding governed the action.

Hard invariant:

> **A runtime action that depends on provider fulfilment must retain enough immutable provenance to identify the governing fulfilment binding/provider contract version used for that action.**

The implementation may realise this through a fulfilment-binding snapshot associated with the active configuration release or another equally strong immutable release reference.

The representation is implementation-specific; the provenance requirement is not.

A later provider switch must not rewrite the historical meaning of earlier provider-backed actions.

---

## 17. Binding changes are controlled configuration/integration changes

Changing:

```text
Main Street scheduling → Calendly
Calendly → Main Street scheduling
Provider A → Provider B
```

is not a semantic capability change if the same fulfilment role contract remains satisfied.

However it is a material execution-routing change and must be reviewed/activated through controlled merchant configuration/integration lifecycle rather than changing silently at runtime.

Provider binding changes must not rewrite historical bindings.

---

## 18. Connection health must not mutate the capability graph

A provider outage, expired token or disconnected account does not mean the merchant's capability ceased to exist.

Rejected:

```text
Calendly unavailable
        ↓
Scheduling capability removed
```

Accepted:

```text
Scheduling capability remains active
        ↓
selected fulfilment binding unavailable/degraded
        ↓
operation availability or integration health reflects failure
        ↓
retry/reconnect/rebind/fallback according to accepted contract
```

Capability semantics and provider health are separate state dimensions.

---

## 19. No silent fallback

Main Street shall not silently switch an authoritative or materially consequential fulfilment role to another provider merely because the selected provider is unavailable.

Automatic fallback is permitted only where all of the following are true:

1. Main Street has explicitly registered the fallback behaviour;
2. both fulfillers satisfy the same applicable fulfilment obligations;
3. the fallback does not change merchant/customer meaning or authority assumptions;
4. the merchant/platform policy explicitly permits that fallback where merchant choice is material; and
5. correlation/idempotency prevents duplicate external action.

Otherwise Main Street must surface degradation, retry, reconciliation or merchant intervention.

---

## 20. External callbacks enter through the integration boundary

MS-PROT-029 remains authoritative that provider callbacks/webhooks use a distinct authenticated integration surface.

Required flow:

```text
External callback
        ↓
authenticate provider/source
        ↓
validate message integrity
        ↓
resolve merchant + provider connection + correlation
        ↓
interpret provider evidence
        ↓
issue/trigger authorised capability/process action
        ↓
capability-owned validation and mutation
```

Rejected:

```text
webhook JSON
    ↓
direct database update
```

External transport does not bypass semantic ownership, merchant scope or runtime authority.

---

## 21. Idempotency and correlation

Provider fulfilment must assume retries, duplicate callbacks and delayed delivery.

Where a provider supports idempotency keys, Main Street should supply a stable Main Street correlation/idempotency identity appropriate to the operation.

Where it does not, duplicate external effects remain a known risk requiring reconciliation.

At minimum, provider-backed activity must be correlatable to:

```text
merchant scope
governing fulfilment binding
Main Street command/process/business identity
provider-side operation/transaction identity where available
```

Exactly-once delivery shall not be assumed.

---

## 22. Data minimisation and security

Each provider contract shall declare the minimum information required for fulfilment.

Main Street shall not grant a provider broader merchant/customer data access merely because the provider is connected.

Requirements include:

- least-privilege provider permissions;
- merchant-scoped connection identity;
- secret isolation;
- authenticated callbacks;
- auditability of connection/binding changes;
- explicit correlation between provider facts and Main Street scope;
- no trust based solely on user-supplied external identifiers.

Provider connection does not itself grant capability execution authority.

---

## 23. Merchant-facing model

Merchants should interact with providers in business/integration language rather than architecture vocabulary.

Example after Scheduling is already relevant:

> How would you like appointments to be managed?

Possible supported presentation:

```text
Use Main Street
Keep using Calendly
```

The internal model remains:

```text
Scheduling active
        ↓
appointment-execution fulfilment role
        ↓
FulfilmentBinding
```

The merchant shall not be asked to understand provider adapters, webhook schemas, authority classes or role identifiers.

---

## 24. Inference boundary

Inference may:

- detect evidence that a merchant currently uses a known provider;
- propose a compatible provider binding;
- ask whether the merchant wants to retain or replace that provider;
- identify missing provider connection steps.

Inference may not:

- register a new provider contract;
- change provider obligations;
- declare an incompatible provider sufficient;
- invent provider-to-semantic mappings;
- activate a capability solely because a provider account exists;
- bypass compiler/integration validation.

---

## 25. Online consultant using Calendly

Merchant evidence:

> "I am an accountant. Clients book consultations using Calendly."

Correct interpretation:

```text
Consultation / Scheduling / Booking semantics
        ↓
Scheduling capability active or proposed
        ↓
appointment-execution role applicable
        ↓
Calendly proposed as external fulfiller
```

Main Street does not create:

```text
CalendlyCapability
```

If the applicable merchant model requires:

```text
create appointment
cancel appointment
receive appointment evidence
maintain customer correlation
```

then the Calendly provider contract must support all required obligations for that context.

If it cannot, Main Street must either:

- reject that binding for the requested operating model; or
- offer a smaller explicitly supported operating model if that smaller model is independently valid Main Street semantics chosen by the merchant.

Main Street must not pretend unsupported provider behaviour exists.

---

## 26. Google Calendar example

MS-PROT-041 remains authoritative.

Google Calendar may satisfy a role such as:

```text
calendar-service / schedule-information-storage
```

It does not thereby own:

```text
Scheduling capability
Appointment semantics
Booking semantics
Customer relationship
working-hours semantics
availability authority
```

Therefore:

```text
Google Calendar connected
```

must never mean:

```text
Scheduling semantics activated or delegated wholesale to Google
```

Calendar records remain representations/correlated schedule information under Main Street's scheduling contract.

---

## 27. Payment example

The payment workflow already establishes:

```text
Main Street
    coordinates payment requirement

Payment provider
    processes the financial transaction
    owns provider-side financial facts

Main Street
    records provider evidence
    updates business semantics through accepted capability/process rules
```

This maps to:

```text
Payment capability
        ↓
payment-execution fulfilment role
        ↓
external provider binding
        ↓
authenticated provider transaction evidence
        ↓
Main Street payment/business consequence
```

Provider-specific payment products remain provider-controlled.

Main Street remains provider-independent at the semantic level.

MS-PROT-048 does not select or replace a concrete payment provider named elsewhere in Product Requirements. It defines the architectural contract any selected provider must satisfy.

---

## 28. Notification example

Current booking implementation already demonstrates a narrow post-commit gateway seam:

```text
committed booking fact
        ↓
outbox
        ↓
BookingNotificationGateway
        ↓
external delivery
```

This is consistent with the fulfilment-role model.

However Main Street shall not prematurely generalise this narrow adapter into a universal provider framework until more than one real role requires the abstraction.

The design contract may precede generic implementation.

---

## 29. Social distribution example

Announcement semantics remain Main Street-owned.

A connected social provider may fulfil:

```text
publication / social-distribution
```

The provider may publish an external post.

It does not become the owner of the Announcement or its Main Street lifecycle.

External publication failure does not erase the Main Street Announcement fact.

---

## 30. Google Business Profile / verification example

Google may remain authoritative for its own verification status.

Main Street may consume authenticated Google evidence.

That evidence may affect Google-specific integration behaviour where accepted product rules require it.

It must not silently create a universal Main Street business-verification requirement or physical-premises eligibility rule.

Provider authority is bounded to the provider's own fact.

---

## 31. Multi-provider composition is valid

One merchant may legitimately use:

```text
Scheduling appointment execution → Main Street
Calendar storage → Google Calendar
Payment execution → Provider A
Notification delivery → Provider B
Social distribution → Provider C
```

Another merchant may use a different composition while having the same business capabilities.

Therefore provider composition must not become a hidden business-category template.

---

## 32. Multiple providers for one role may be valid where semantics permit

Some roles may support multiple simultaneous providers.

Example:

```text
Payment execution
    ├── Provider A
    └── Provider B
```

This is valid only where provider-selection semantics are explicitly supported.

The mere existence of several connected providers does not define how a customer transaction chooses among them.

Selection/eligibility rules must remain platform-owned and bounded.

---

## 33. Falsification review

The following proposals were tested and rejected.

| Failed assumption | Why it fails |
|---|---|
| Provider should be represented as a Capability | Conflates business meaning with technical implementation |
| One provider should fulfil an entire capability | Capabilities often require several independent technical roles |
| Connecting a provider should activate a capability | Makes integrations a hidden semantic authority |
| Provider API states can become Main Street lifecycle states directly | Imports third-party semantics into the core model |
| Provider outage should deactivate the capability | Confuses integration health with business capability |
| Runtime may silently switch providers | Can duplicate effects or change authority/merchant intent |
| Provider callbacks may update business tables directly | Bypasses merchant scope, ownership and invariants |
| External timeout means external failure | Provider may have completed the action |
| Provider secrets belong in MerchantConfiguration | Mixes mutable credentials with semantic configuration |
| Provider selection should be a capability policy value | Provider choice is execution routing, not capability semantic variability |
| One generic provider adapter should be built immediately | Premature abstraction before multiple concrete roles demonstrate the common contract |
| Business category should determine provider stack | Recreates merchant templates and prevents hybrid/evolving configurations |

No material falsifier remains after these corrections.

---

## 34. Cross-provider validation

### A. Main Street scheduling

```text
Scheduling active
appointment-execution role
INTERNAL fulfiller
```

No external provider required.

**PASS**

### B. Online consultant retaining Calendly

```text
Scheduling active
appointment-execution role
EXTERNAL_PROVIDER = Calendly
```

Valid only to the extent the registered provider contract satisfies the applicable obligations.

**PASS**

### C. Google Calendar-backed Main Street Calendar

```text
Scheduling semantics remain Main Street-owned
calendar storage role → Google Calendar
```

Storage provider does not become scheduling authority.

**PASS**

### D. Third-party payment

```text
Payment semantics active
payment execution role → external provider
provider owns financial transaction fact
Main Street records/correlates evidence
```

**PASS**

### E. External notification delivery

```text
Main Street commits business fact
notification delivery occurs post-commit
provider outage leaves business fact intact
```

**PASS**

### F. Provider disconnection

```text
Capability remains active
binding becomes unavailable/degraded
reconnect/rebind/reconcile required
```

No semantic graph mutation.

**PASS**

### G. Hybrid merchant

A merchant can combine different providers across commerce, scheduling, notifications and publication without requiring a business-category integration template.

**PASS**

---

## 35. Accepted invariants

1. Capability semantics and provider fulfilment are separate architectural concerns.
2. Providers fulfil Main Street-defined roles; they do not define Main Street capabilities.
3. Fulfilment roles, not whole capabilities, are the canonical provider-binding unit.
4. One capability may use zero, one or several fulfilment roles/providers.
5. One provider may support several roles without gaining semantic ownership.
6. Provider bindings cannot activate capabilities or other semantics.
7. Applicable provider obligations are derived only after semantic/configuration resolution.
8. A provider binding is valid only if it satisfies every applicable obligation for its context.
9. Main Street must not weaken semantics to fit an insufficient provider.
10. Provider-specific states require explicit interpretation before they may affect Main Street business state.
11. Provider-authoritative facts remain bounded to the provider's own authority.
12. External callbacks enter through an authenticated integration boundary and cannot mutate capability state directly.
13. External uncertainty remains uncertainty until reconciled.
14. Exactly-once external delivery is not assumed.
15. Provider connection state and secrets remain operational integration state, not semantic configuration.
16. Provider binding provenance must be retained for provider-backed execution.
17. Provider changes are controlled execution-routing changes and do not rewrite history.
18. Provider health does not mutate the capability graph.
19. Automatic fallback is forbidden unless explicitly registered, semantically equivalent and safely idempotent.
20. Main Street and external fulfillers are judged against the same applicable fulfilment obligations.
21. Google Calendar/storage providers do not become scheduling semantic authority.
22. Payment providers own provider-side financial execution/facts while Main Street retains business-semantic interpretation.
23. Merchant-facing provider choices use business/integration language, not provider-contract internals.
24. Inference may propose provider bindings but cannot register providers, alter obligations or bypass validation.
25. Provider composition must remain business-category independent.

---

## 36. Implementation discipline

MS-PROT-048 establishes an architectural contract; it does not justify immediate creation of a broad provider framework.

Current implementation already contains narrow integration seams such as `BookingNotificationGateway` and post-commit delivery behaviour.

The implementation rule is:

> **Do not generalise provider infrastructure until a concrete second/third fulfilment role creates a tested need for the shared abstraction.**

When implementation begins, use the existing Main Street workflow:

```text
inspect accepted design
        ↓
inspect current tests
        ↓
inspect current implementation
        ↓
RED test for one concrete provider role
        ↓
smallest implementation
        ↓
full regression
        ↓
conformance review
```

Candidate future RED cases include:

- provider binding cannot activate its role owner capability;
- binding to unsupported obligations is rejected;
- provider connection loss does not remove capability activation;
- external callback cannot bypass merchant scope/capability ownership;
- provider evidence is correlated to the governing binding and business identity;
- handler/process cannot treat an uncertain provider outcome as success/failure without accepted evidence;
- provider switch does not rewrite prior action provenance;
- automatic fallback is rejected unless explicitly authorised;
- several roles of one capability may bind to different fulfillers;
- the same provider may fulfil roles for multiple capabilities without merging their semantics.

No production-code change is required merely to accept this document.

---

## 37. Deferred implementation details

The following remain downstream implementation decisions rather than semantic ambiguities:

- Java class/interface names;
- persistence schema for provider definitions/connections/bindings;
- OAuth libraries;
- webhook framework;
- retry/backoff library;
- provider SDK choice;
- connection-health polling mechanism;
- secret store technology;
- provider registry persistence;
- exact provider certification process;
- API endpoint paths;
- whether fulfilment-binding provenance is embedded in `ConfigurationRelease` or referenced through an immutable companion snapshot, provided the provenance invariant is preserved.

These details may vary without changing the accepted architectural boundary.

---

## Governance verdict

```text
PROPOSE                     ✓
CROSS-DESIGN RECONCILIATION ✓
PROVIDER FALSIFICATION      ✓
CROSS-PROVIDER VALIDATION   ✓
MANUAL APPROVAL             ✓
ACCEPT                      ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street capabilities remain provider-independent semantic contracts. Applicable semantics and configuration determine a set of Main Street-owned fulfilment roles. Each role may be fulfilled internally or through a registered external provider whose supported obligations, authority boundary, evidence, security, failure and correlation contracts satisfy that resolved merchant context. Provider bindings cannot activate or redefine semantics; provider-specific state must be interpreted through Main Street contracts; external uncertainty must be reconciled; provider health remains separate from capability activation; and provider-backed execution retains immutable binding provenance.**

The next design slice should define the **Merchant Surface Projection Contract**: how the resolved executable model, capability configuration and fulfilment availability contribute merchant/public/dashboard surfaces without business-category templates or provider-specific UI branches.
