# MS-PROT-021 — Merchant Configuration Model

**Version:** 1.3  
**Status:** **Accepted**  
**Depends on:** MS-PROT-020 — Canonical Semantic Schema & Compiler Boundary  
**Purpose:** Define how merchants select and parameterise Main Street's registered semantics without creating new semantics, executable rules or niche-specific runtime behaviour.

## 1. Governing principle

> **Merchant configuration expresses merchant intent within Main Street's permitted semantic space.**

```text
Registered semantics
      ↓
Inference proposes likely configuration
      ↓
Merchant reviews/corrects supported selections
      ↓
Compiler validates composition
      ↓
Resolved operational model
```

Inference is an onboarding accelerator, not a gatekeeper or semantic authority.

## 2. What merchant configuration may control

Configuration may:

- activate supported capabilities;
- select permitted policy values;
- provide merchant offerings and configured resources that conform to registered schemas/definitions;
- provide schedule parameters;
- select supported interaction/access modes;
- select supported exposure and notification preferences;
- supply merchant-owned content and operational data.

It may not:

- define capabilities, operations, states, transitions, requirements or semantic relationships;
- define arbitrary workflow graphs or executable code;
- create niche-specific compiler/runtime branches;
- issue runtime orchestration instructions;
- redefine platform invariants or capability contracts.

## 3. Controlled customisation

```text
PLATFORM CONTROLLED
security, invariants, semantic meaning,
capability contracts, compiler rules

MERCHANT CONTROLLED WITHIN BOUNDS
supported capabilities, policy values,
schedules, resources, offerings,
interaction/exposure/notification preferences

MERCHANT CONTENT
names, descriptions, prices, media,
public copy and contextual business data
```

A lower level cannot redefine a higher level.

## 4. Capability activation

Activation origins are conceptually:

```text
REQUIRED_BY_PLATFORM
REQUIRED_BY_SELECTED_SEMANTICS
MERCHANT_SELECTED
```

AI is not an activation authority class. It may propose candidate selections.

Capability activation is not necessarily a merchant-facing toggle; required/internal capabilities may remain hidden.

Business category may assist inference, onboarding and presentation but cannot determine runtime/compiler behaviour.

## 5. Merchant correction and choice

A merchant may correct an inferred configuration and add or remove supported semantics where permitted.

Example:

```text
Inference proposes:
Service + Enquiry

Merchant states:
"We also accept appointments."

Candidate becomes:
Service + Enquiry + Scheduling + Booking
```

The compiler—not inference—decides whether the final composition is valid.

Hard rule:

> **Inference may propose what is likely; it cannot determine what the merchant is allowed to be within Main Street's supported semantic space.**

## 6. Adaptive onboarding

Onboarding should refine a candidate configuration graph rather than run one universal questionnaire.

```text
Merchant answer
      ↓
Infer/refine seed nodes
      ↓
Resolve registered dependencies
      ↓
Identify unresolved merchant choices
      ↓
Ask only relevant next questions
      ↓
Refine candidate configuration
```

Examples:

- Information publisher → ask about classification, deadlines, external links, enquiries and subscriptions; do not ask about inventory/payment unless later relevant.
- Mechanic → infer Service; if appointments are supported, add Scheduling/Booking; if appointments depend on technicians/bays, capacity/allocation becomes relevant; if mobile service is selected, registered location requirements become relevant.

Inference selects semantics; registered semantics own relationships.

## 7. Policy selection

A Policy configures variability inside semantics that already exist.

```text
PolicySelection
{
    policy
    owner_context
    selected_value
    selection_origin
}
```

Where relevant, preserve:

```text
NOT_APPLICABLE
DEFAULTED
EXPLICITLY_SELECTED
```

Policy cannot activate/create its owning capability.

## 8. Requirements

Merchants do not arbitrarily attach Requirements.

Requirements arise from active semantics, selected policies and platform-owned applicability relationships. Merchants/customer interactions provide the data or conditions that satisfy applicable Requirements.

Requirement applicability remains contextual; no universal merchant/customer form is created.

## 9. Offerings, resources and schedules

Main Street owns the semantic structure; merchants own their actual operational data.

### 9.1 Offering contract

```text
Registered OfferingSchema
        ↓ constrains
MerchantOffering
        ↓ references
capability bindings + resource requirements
        + schedule parameters + policy selections
        ↓ validated and resolved
ResolvedOffering
```

- `OfferingSchema` is platform-owned semantics. It defines permitted structure, parameter types and applicable binding points.
- `MerchantOffering` is merchant-owned configuration. It supplies stable merchant identity, name, description, commercial values and permitted operational parameters.
- `ResolvedOffering` is derived configuration that contains validated references and effective bindings. It is neither a new semantic authority nor live operational state.

An offering cannot contain arbitrary executable workflow or create a capability, operation, requirement, policy, resource meaning or schedule meaning. Product labels such as `Product`, `Service` and `Hybrid` may assist presentation or discovery, but must not cause compiler or runtime branching. Behaviour comes from registered capability bindings.

### 9.2 Resource contract

```text
ResourceDefinition       platform-owned semantic meaning
        ↓ constrains
ConfiguredResource       stable merchant configuration
        ↓ participates in
ResourceInstanceState    live runtime/persistence truth
```

Configured resources may represent physical assets, staff, quantities or logical contributors where the registered ResourceDefinition permits them. A configured resource supplies merchant-owned identity and allowed parameters. Current occupancy, quantity, allocation, health or availability is runtime state and must not be embedded in merchant configuration.

MS-PROT-023 uses `ResourceInstance` for an authoritative runtime target/entity. Its changing operational component is `ResourceInstanceState`; the runtime term does not collapse that entity into the `ConfiguredResource` source record.

### 9.3 Schedule contract

`ScheduleConfiguration` contains stable merchant parameters such as hours, durations, buffers, booking constraints and offering/resource relationships. `AvailabilityDecision` is a contextual runtime result produced from schedule configuration plus current operational state.

```text
ScheduleConfiguration + current state + request context
                         ↓
              AvailabilityDecision
                         ↓ revalidate at authority boundary
             Allocation / Hold / Commitment
```

Scheduling semantics remain separate from calendar UI, portals, email and ICS delivery. Availability remains advisory and cannot itself create an authoritative claim.

## 10. Customer interaction, exposure and notification

Configuration may support modes such as:

```text
guest enquiry
contextual/secure guest access
authenticated customer portal
```

Customer accounts are optional.

Exposure determines what information may be projected to an audience. Notification determines communication/delivery. They remain distinct.

## 11. Information and non-transactional merchants

Main Street must support the smallest valid capability set.

Example information publisher:

```text
Publication
Search/Discovery
Classification
Contact/Enquiry
optional Notification/Subscription
```

It should not receive Booking, Payment, Inventory, Ordering, Allocation or Scheduling unless its actual operation requires them.

Online consultant example:

```text
Consultation
Scheduling
Booking
Enquiry
Notification
optional Payment
```

Physical premises are not an eligibility prerequisite.

## 12. Configuration lifecycle

Conceptual lifecycle:

```text
MerchantConfiguration:
CANDIDATE → VALIDATED → APPROVED (where required)

ConfigurationRelease:
PUBLISHED/STAGED → ACTIVE → SUPERSEDED
```

Changes produce a new candidate/change set and are validated before a release may be published/staged. `PUBLISHED/STAGED` is a persistence/publication condition, not an additional merchant configuration revision lifecycle state. Raw configuration is not activated directly, and an active release is not mutated piecemeal during execution. MS-PROT-022 owns the release envelope; MS-PROT-040 owns review, approval, activation, supersession and reinstatement.

## 13. Accepted invariants

1. Configuration is declarative, not executable programming.
2. Merchant choice is bounded by supported semantics, not by business-category inference.
3. AI/inference proposes; merchant may correct; compiler determines validity.
4. Adaptive onboarding asks only unresolved/relevant questions.
5. Inference cannot invent semantic relationships.
6. Capability activation and policy selection remain distinct.
7. Requirements arise from registered semantics, not merchant-authored rules.
8. Business category and physical premises are contextual, not architectural eligibility rules.
9. Customer accounts, exposure and notifications remain optional/configurable concerns.
10. Merchant configurations are evolvable without moving to another product architecture.
11. OfferingSchema, MerchantOffering and ResolvedOffering remain distinct authorities and lifecycle stages.
12. ResourceDefinition, ConfiguredResource and ResourceInstanceState remain distinct.
13. ScheduleConfiguration is stable configuration; AvailabilityDecision is contextual runtime truth.
14. Product/service/hybrid labels never select compiler or runtime behaviour.

## 14. Deferred decisions

Identity verification, inference algorithms, onboarding UX, external scheduler integration, publication capability details, pricing and dashboard layout remain downstream/product decisions unless specified elsewhere.

## Governance verdict

**ACCEPTED.** The 20 August 2026 handover broadens the supported merchant universe and clarifies inference/merchant autonomy without changing the underlying configuration architecture.

**Revision 1.2:** Defined the offering, resource and schedule configuration contracts and aligned the configuration lifecycle with immutable releases governed by MS-PROT-022.

**Revision 1.3:** Aligned lifecycle terminology with MS-PROT-040 and clarified that staging is a publication condition rather than a configuration revision authority state.
