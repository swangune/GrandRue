# MS-PROT-048 v1.5 — Platform-Scoped Fulfilment Requirement, Routing & Serving-Affinity Contract Amendment

**Document ID:** MS-PROT-048  
**Version:** 1.5  
**Status:** ACCEPTED  
**Approved:** 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** composite MS-PROT-048 through v1.4 only within platform-originated Fulfilment Requirement ownership, platform-scoped fulfilment routing and serving affinity  
**Preserves:** all merchant-specific Fulfilment Binding Set, capability-owned requirement, ProviderConnection, Provider Readiness, provider evidence, open-first and no-substitution authority outside this amendment  
**Depends on:** MS-PROT-022; composite MS-PROT-040; MS-PROT-048 v1.0–v1.4; MS-PROT-054; MS-PROT-062; MS-PROT-067; MS-PROT-069; MS-PROT-070; MS-PROT-072; MS-PROT-075; MS-PROT-079; ADR-012; ADR-013  
**Closes:** the platform-originated Fulfilment Requirement gap explicitly retained by MS-PROT-048 v1.3 and the platform-routing dependency required by MS-PROT-079 Target 16  
**Purpose:** Allow accepted platform/security authorities to require shared provider fulfilment and allow Main Street-owned platform infrastructure to use exact release-affined provider routing without manufacturing a business capability, fake Merchant Scope, merchant-specific routing choice or mutable global provider pointer.

---

## 1. Governing Decision

Main Street SHALL distinguish:

```text
MERCHANT-SPECIFIC FULFILMENT ROUTING

    routing choice is materially specific
    to one Merchant Configuration / merchant provider context

        ↓

    FulfilmentBindingSetRevision
    under MS-PROT-048 v1.2
```

from:

```text
PLATFORM-SCOPED FULFILMENT ROUTING

    shared Main Street infrastructure
    discharges a registered platform responsibility

        ↓

    PlatformFulfilmentBindingSetRevision
```

Neither routing source may activate business semantics.

Canonical:

```text
semantic requirement
        ↓
applicable Fulfilment Role
        ↓
routing scope
    ├── MERCHANT
    └── PLATFORM
        ↓
exact binding
        ↓
current readiness
        ↓
execution
```

---

## 2. Why Platform Scope Is Required

Composite MS-PROT-048 already permits:

```text
ProviderConnection.ownerScope
    = Merchant Scope
    OR
    = platform responsibility
```

and explicitly gives external email delivery as a platform-scoped provider example.

But MS-PROT-048 v1.3 intentionally limited initial Fulfilment Requirement ownership to registered capabilities.

That is insufficient for accepted authorities such as:

```text
security authority
account authority
notification infrastructure
other explicitly registered platform authority
```

that may legitimately require technical fulfilment without inventing a merchant business capability.

Target 16 closes that narrow gap.

---

## 3. Fulfilment Requirement Owner

A registered Fulfilment Requirement may now be owned by exactly one:

```text
REGISTERED_CAPABILITY
```

or:

```text
REGISTERED_PLATFORM_AUTHORITY
```

The owner identity MUST be release-affined and registered.

Rejected:

```text
arbitrary string owner
provider-owned requirement
frontend-owned requirement
AI-created requirement
implementation class as semantic owner
```

The requirement owner answers:

> Why is this technical responsibility legitimately required?

---

## 4. Platform Requirement Does Not Create Business Activity

A platform-originated requirement establishes availability of technical infrastructure.

It does not itself create:

```text
NotificationIntent
Payment
Order
Appointment
security incident
customer relationship
merchant policy
```

Example:

```text
platform-security authority
    requires ability to deliver
    an authorised security communication

        ≠

security communication automatically exists
```

The owning business/security operation still determines whether an actual communication responsibility exists.

---

## 5. Initial Platform Requirement Applicability

The initial platform-originated requirement model SHALL remain deliberately bounded.

A platform requirement may use:

```text
ALWAYS_FOR_EXECUTABLE_PLATFORM_AUTHORITY
```

meaning:

> The exact registered platform authority requires this fulfilment infrastructure to be available whenever that authority is executable in the applicable serving release.

No generic expression language is introduced.

Conditional platform requirement DSLs, arbitrary feature flags and runtime predicates remain outside this amendment.

---

## 6. Capability-Owned Requirements Survive Unchanged

Existing capability-owned requirements continue to use MS-PROT-048 v1.3.

Example:

```text
booking
    owns:
    booking-confirmation-delivery

        ↓

notification / message-delivery
```

Target 16 does not move the reason for booking confirmations into Notifications or the provider layer.

---

## 7. Routing Scope

Every resolved Provider Fulfilment binding SHALL have exactly one routing authority:

```text
MERCHANT_ROUTED
```

or:

```text
PLATFORM_ROUTED
```

The routing scope must be determinable from registered fulfilment contracts.

A runtime caller, provider or AI system MUST NOT choose the routing authority ad hoc.

---

## 8. MERCHANT_ROUTED

`MERCHANT_ROUTED` preserves MS-PROT-048 v1.2 unchanged.

It applies where provider routing materially belongs to the merchant operating configuration, including where:

```text
merchant provider account matters
merchant-specific ProviderConnection matters
merchant explicitly chooses provider routing
routing materially changes merchant external authority
```

The exact `FulfilmentBindingSetRevision` remains pinned through the governing Merchant Configuration Revision.

---

## 9. PLATFORM_ROUTED

`PLATFORM_ROUTED` applies where Main Street supplies shared technical infrastructure and the provider choice is not itself merchant business semantics.

Examples may include:

```text
shared transactional email transport
shared SMS transport
shared push infrastructure
```

where Main Street is supplying the communication infrastructure.

This does not prevent a later product from supporting merchant-specific provider routing.

If merchant-specific provider routing is introduced, that path must use `MERCHANT_ROUTED`.

---

## 10. PlatformFulfilmentBindingSetRevision

A **PlatformFulfilmentBindingSetRevision** is an immutable platform-scoped routing definition.

Conceptually:

```text
PlatformFulfilmentBindingSetRevision
{
    bindingSetIdentity
    revision

    semanticRegistryRelease

    bindings[]
}
```

A binding contains the stable routing information needed to resolve:

```text
Fulfilment Role
semantic context
fulfiller kind
fulfiller identity
ProviderConnection identity where external
```

It contains no raw credential material or current readiness state.

---

## 11. No Fake Merchant Scope

Platform routing MUST NOT manufacture:

```text
merchant = MAIN_STREET
fake MerchantAccount
fake merchant configuration
```

merely to reuse merchant binding machinery.

The platform binding is explicitly platform-scoped.

---

## 12. Serving Affinity

Initial production SHALL NOT introduce a separately mutable global:

```text
currentProvider
```

pointer.

Instead, one exact compatible PlatformFulfilmentBindingSetRevision SHALL be affined to the serving deployment/generation that executes new platform-routed work.

Canonical:

```text
Serving Deployment
    +
Semantic Registry Release
    +
exact PlatformFulfilmentBindingSetRevision
        ↓
admitted platform fulfilment support
```

This preserves deterministic serving support while avoiding a second merchant configuration authority.

---

## 13. Platform Binding Is Not Merchant Configuration

Changing shared Main Street email infrastructure:

```text
Provider A → Provider B
```

does not change:

```text
merchant business semantics
merchant return policy
booking policy
Notification Intent meaning
```

and therefore MUST NOT require rewriting every merchant's business configuration merely because Main Street changed its shared infrastructure.

---

## 14. Platform Binding Change

A material platform routing change such as:

```text
INTERNAL → EXTERNAL_PROVIDER
Provider A → Provider B
platform ProviderConnection C1 → C2
registered fallback topology change
```

requires a new immutable PlatformFulfilmentBindingSetRevision.

Initial production activates that revision through serving/deployment admission rather than an independent hot-swappable routing control plane.

---

## 15. Changes That Do Not Require a New Platform Binding Revision

The following do not change routing identity by themselves:

```text
credential rotation
token refresh
secret generation change
temporary provider outage
Provider Readiness change
circuit state
provider latency
callback receipt
retry counter
health observation
```

Those remain live operational facts.

---

## 16. Static Compatibility

Before a platform binding participates in an admitted serving generation, Main Street MUST validate:

```text
exact semantic release

registered Fulfilment Role

semantic context where applicable

exact required obligation set

registered fulfiller

registered external provider where applicable

provider support for required obligations

provider interaction/evidence contract compatibility

ProviderConnection identity shape where required

executable adapter support
```

A platform binding cannot weaken an obligation to fit a provider.

---

## 17. Configuration Activation and Platform-Routed Requirements

Where a Merchant Configuration produces an applicable requirement whose route is `PLATFORM_ROUTED`, the RCP does not acquire merchant authority over the platform provider selection.

Instead it preserves:

```text
required Fulfilment Role
semantic context
exact applicable obligations
routing scope = PLATFORM_ROUTED
```

Serving admission must establish executable platform fulfilment support.

Thus:

```text
Merchant Configuration
    says WHAT technical responsibility is required

Platform serving infrastructure
    says HOW shared infrastructure currently fulfils it
```

---

## 18. No Active-but-Unexecutable Platform Route

A serving deployment SHALL NOT be admitted if an applicable required platform-routed fulfilment contract lacks compatible executable support.

Likewise, platform provider routing MUST NOT be promoted in a way that knowingly removes the last safe executable path for still-required platform fulfilment responsibilities.

This composes with ADR-012 and MS-PROT-040 v1.2's no-orphan principle.

---

## 19. ProviderConnection

An external `PLATFORM_ROUTED` binding may reference a platform-scoped ProviderConnection.

Canonical:

```text
platform fulfilment binding
        ↓
ProviderConnection
    ownerScope = platform responsibility
```

Credential rotation does not change connection identity merely because secret material changes.

---

## 20. Provider Readiness

Static binding does not imply runtime readiness.

For each provider-dependent execution:

```text
exact platform binding
+
exact role/context/obligations
+
ProviderConnection
+
credential/security evidence
+
provider/dependency evidence
+
resilience restrictions
        ↓
Provider Readiness
```

The generic MS-PROT-048 v1.4 algebra survives:

```text
READY
DEGRADED
NOT_READY
UNKNOWN
```

---

## 21. Historical Provider Affinity

Every durable provider-side-effect progression that materially depends on a platform binding MUST preserve sufficient provenance to reconstruct:

```text
exact PlatformFulfilmentBindingSetRevision

exact Fulfilment Role/context

fulfiller/provider identity

ProviderConnection identity where applicable

provider contract / semantic release
```

A later provider switch MUST NOT reinterpret an earlier attempt as though it used the new provider.

---

## 22. Existing Work During Provider Change

Provider routing for new work and completion/reconciliation of existing work are distinct.

After:

```text
Provider A → Provider B
```

new work may use Provider B under the new admitted binding.

But:

```text
callback
reconciliation
uncertain execution
historical provider evidence
```

for an attempt already bound to Provider A continues against the historical A provenance where authorised.

---

## 23. No Provider Substitution Inside Existing Attempt

A durable operation bound to:

```text
Provider A
Connection C1
Binding Revision P7
```

MUST NOT silently execute against:

```text
Provider B
Connection C2
Binding Revision P8
```

because current infrastructure changed.

A separately authorised new logical provider attempt is required.

---

## 24. Callback Handling

Inbound evidence for an existing platform-routed attempt does not require the current platform binding still to select that provider for new work.

It requires:

```text
authenticated source
+
historical provider/binding/connection correlation
+
existing operation/evidence authority
```

before capability-owned interpretation.

---

## 25. Platform Routing Does Not Become Business Authority

The following remain prohibited:

```text
provider available
    → NotificationIntent created

email provider configured
    → merchant must send emails

platform SMS available
    → customer may be contacted

provider fallback
    → business communication requirement changed
```

Provider infrastructure fulfils already-authorised responsibility only.

---

## 26. Open-First Rule Survives

MS-PROT-048 v1.1 remains fully authoritative.

Selection of shared notification infrastructure must still evaluate:

```text
modifiable open library
    ↓
open/self-hostable software
    ↓
paid external service
```

in that order subject to suitability, sustainability and safety.

External network ownership, deliverability and operational burden may legitimately justify an external provider.

---

## 27. Failure Semantics

At minimum, platform routing must distinguish:

```text
PLATFORM_FULFILMENT_BINDING_MISSING

PLATFORM_FULFILMENT_BINDING_INCOMPATIBLE

PLATFORM_PROVIDER_NOT_READY

PLATFORM_PROVIDER_EXECUTION_UNCERTAIN

HISTORICAL_BINDING_UNRESOLVABLE

TECHNICAL_FAILURE
```

These are technical/provider classifications.

They do not become business lifecycle states.

---

## 28. Falsification

### Merchant has no provider account

```text
transactional email
uses Main Street shared provider
```

Expected:

```text
platform-scoped ProviderConnection
no fake merchant provider account
```

**PASS**

### Security authority requires delivery infrastructure

Expected:

```text
platform-authority-owned Fulfilment Requirement valid
no fake business capability
```

**PASS**

### Platform provider changes

Expected:

```text
new platform binding revision
merchant business configuration unchanged
historical attempts retain old provider provenance
```

**PASS**

### Credential rotation

Expected:

```text
same platform binding
same ProviderConnection identity
new credential generation
```

**PASS**

### Provider A becomes unavailable

Expected:

```text
binding unchanged
readiness changes
business semantics unchanged
```

**PASS**

### Callback from old provider after deployment switched to B

Expected:

```text
authenticate/correlate against historical A attempt
do not reject solely because B is current for new work
```

**PASS**

### Arbitrary provider entered by merchant

Expected:

```text
not executable unless registered through accepted fulfilment/provider authority
```

**PASS**

---

## 29. Rejected Alternatives

Rejected:

```text
fake Main Street merchant

platform requirement disguised as merchant capability

shared provider copied into every merchant's business policy

mutable global currentProvider pointer

provider selected by frontend

provider selected by AI

current provider switch rewriting historical attempts

credential identity = binding identity

provider readiness = binding state

provider configuration activates communication semantics
```

---

## 30. Deferred Scope

Deferred:

```text
hot-swappable platform provider routing without serving promotion

weighted provider traffic splitting

active-active provider routing

automatic provider cost optimisation

automatic cross-provider failover policy

provider marketplace

exact platform binding persistence format

exact deployment manifest encoding

exact administrative API

exact operational UI

generic background reconciliation — Target 18/19
```

---

## 31. Conformance

A conforming implementation MUST prove:

```text
[ ] platform authorities may own registered fulfilment requirements

[ ] platform requirements do not manufacture business activity

[ ] MERCHANT_ROUTED and PLATFORM_ROUTED remain distinct

[ ] platform routing creates no fake Merchant Scope

[ ] platform provider routing is immutable and exact-revision identifiable

[ ] initial platform routing is serving-affined

[ ] merchant configuration does not become platform-provider authority

[ ] provider/connection changes do not rewrite historical attempts

[ ] credential rotation does not imply binding replacement

[ ] runtime Provider Readiness remains independent

[ ] old-provider callbacks may reconcile existing work

[ ] no provider substitution occurs inside an existing attempt

[ ] open-first provider-selection governance survives
```

---

## 32. Governing Principle

> **Shared Main Street infrastructure needs its own explicit provider-routing authority rather than pretending every technical provider choice belongs to a merchant capability. Platform authorities may require fulfilment without manufacturing business semantics; platform routing remains serving-affined technical infrastructure, while merchant-specific routing remains pinned to Merchant Configuration. In both cases providers fulfil already-defined responsibility and never become its semantic owner.**
