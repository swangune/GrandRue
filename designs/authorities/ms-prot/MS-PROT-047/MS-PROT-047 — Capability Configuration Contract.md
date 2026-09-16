# MS-PROT-047 — Capability Configuration Contract

**Document ID:** MS-PROT-047  
**Version:** 1.0  
**Status:** **ACCEPTED after cross-domain falsification and manual approval**  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.4, MS-PROT-023, MS-PROT-038 v1.0, MS-PROT-039 v1.1, MS-PROT-045 v1.1  
**Purpose:** Define how an active Main Street capability exposes bounded merchant-configurable decisions without allowing merchant configuration, onboarding, presentation or inference to create executable semantics.

---

## 1. Governing decision

> **A Capability Configuration Contract declares the bounded choices and stable operational parameters through which an already-active capability may be configured. A configuration choice may select, narrow or parameterise Main Street-defined semantics; it may not create new semantic meaning.**

Canonical boundary:

```text
Platform-owned capability semantics
        ↓
Capability Configuration Contract
        ↓
Merchant-owned bounded selections / parameters
        ↓
Configuration Compiler
        ↓
Resolved executable merchant model
```

The contract exists between registered capability semantics and merchant configuration. It is not a questionnaire, dashboard descriptor, provider integration contract or business-category template.

---

## 2. Relationship to MS-PROT-021

MS-PROT-021 remains the broad authority for Merchant Configuration.

MS-PROT-047 refines one specific question left implicit there:

> **What may an active capability legitimately ask the merchant to configure, and how is that configurable surface bounded?**

MS-PROT-047 does not replace capability activation, policy resolution, offerings, configured resources, schedules, exposure, notification or configuration lifecycle semantics already governed elsewhere.

---

## 3. Capability selection and capability configuration are distinct

This distinction is mandatory.

```text
Capability selection
    → determines what semantic capability is active

Capability configuration
    → determines how an already-active capability behaves
       within Main Street-defined bounds
```

Rejected:

```text
accept-bookings = true
        ↓
compiler silently activates Booking
```

Accepted:

```text
Booking selected / required through capability resolution
        ↓
Booking active
        ↓
booking.confirmation-mode = MERCHANT_CONFIRMATION
```

A configuration value must never become a hidden capability-activation mechanism.

Capability activation remains governed by MS-PROT-022. A policy or configuration decision cannot activate its owner or an unrelated capability.

---

## 4. Semantic ownership

Each configuration decision belongs to one registered capability.

Canonical identity is capability-scoped:

```text
CapabilityConfigurationDecisionIdentity
{
    ownerCapabilityIdentifier
    decisionIdentifier
}
```

A bare decision identifier is not globally authoritative.

This follows the same ownership discipline used elsewhere in the accepted semantic model: semantic meaning belongs to the capability that defines it.

Example:

```text
booking / confirmation-mode
scheduling / cancellation-window
inventory / low-stock-threshold
```

The merchant may select or supply a permitted value. The merchant does not own the meaning of the decision.

---

## 5. Configuration definition is not configuration presentation

Hard separation:

```text
Configuration definition
        ≠
Configuration presentation
```

A capability may define:

```text
owner: booking
identifier: confirmation-mode
value domain: ENUM
allowed values:
    AUTOMATIC
    MERCHANT_CONFIRMATION
default:
    AUTOMATIC
```

Onboarding may present:

> When someone books with you, should it be confirmed immediately or wait for your approval?

The merchant dashboard may present:

```text
Booking approval
Automatic >
```

These are different presentations of the same underlying registered decision.

Therefore a Capability Configuration Contract shall not contain merchant-facing question wording, screen layout, navigation structure, help copy, AI prompts or dashboard component definitions.

---

## 6. Configuration decision model

Conceptually, a registered configuration decision contains:

```text
CapabilityConfigurationDecisionDefinition
{
    identity
    semanticTarget
    valueDomain
    resolutionRequirement
    optionalDefault
    optionalApplicabilityConditionIdentifier
    scopeContract
}
```

This structure is conceptual. MS-PROT-047 does not mandate a Java representation.

### 6.1 identity

Capability-scoped stable identity.

### 6.2 semanticTarget

The Main Street-defined semantic/configuration construct affected by the decision.

A decision may, for example:

- select one registered policy value;
- supply one bounded stable configuration parameter;
- bind one permitted reference to an already-defined merchant/configuration object.

A decision cannot target arbitrary Java fields, JSON paths, database columns, workflow nodes or user-authored expressions.

### 6.3 valueDomain

Defines exactly what values are permitted.

### 6.4 resolutionRequirement

States whether an applicable decision must resolve before configuration can be accepted.

### 6.5 optionalDefault

A platform-owned default may resolve a decision without pretending that the merchant explicitly selected it.

### 6.6 optionalApplicabilityConditionIdentifier

Applicability may depend on a platform-owned condition identity. The condition is referenced, not authored by the merchant.

### 6.7 scopeContract

Defines the semantic/configuration context in which the value applies. A decision must not be assumed merchant-wide where accepted semantics permit different values by offering, operation, resource or another supported context.

---

## 7. Minimal value-domain algebra

The initial contract shall use a deliberately small bounded value vocabulary.

### 7.1 BOOLEAN

A genuine two-state merchant choice whose semantic meaning is already registered.

Example:

```text
customer-cancellation-allowed = true | false
```

A BOOLEAN is not valid when `true` would actually mean “activate another capability”.

### 7.2 ENUM

Exactly one value from a registered finite set.

Example:

```text
confirmation-mode =
    AUTOMATIC
    MERCHANT_CONFIRMATION
```

The existing registered Policy model is a current concrete example of bounded enumerated variability.

### 7.3 BOUNDED_SCALAR

A scalar constrained by a registered unit/domain and permitted bounds.

Examples:

```text
cancellation-window = 24 HOURS
preparation-buffer = 15 MINUTES
low-stock-threshold = 5 UNITS
```

A bounded scalar shall define enough type information to prevent arbitrary untyped numeric interpretation.

### 7.4 REFERENCE

A reference to an existing object/configuration identity of an allowed registered kind and scope.

Example:

```text
default-service-resource → configured consultant/resource
```

A reference grants no mutation authority over the target and cannot create the referenced object.

### 7.5 STRUCTURED_BOUNDED_VALUE

A value with platform-owned structure and validation rather than an arbitrary map/document.

Examples may include:

```text
operating-hours configuration
recurring break configuration
bounded schedule constraints
```

The structure is Main Street-defined. Merchant-supplied arbitrary JSON is not a semantic configuration primitive.

---

## 8. TEXT is not an executable configuration primitive

MS-PROT-039 permits `TEXT` as an onboarding answer form because natural language can provide evidence or merchant content.

MS-PROT-047 deliberately does not treat arbitrary text as an executable configuration value.

```text
TEXT evidence/content
        ↓
inference or merchant content handling
        ↓
registered bounded decision / registered schema field
```

Rejected:

```text
confirmation-rule =
"Automatically confirm unless the customer seems risky"
```

unless Main Street has separately registered semantics representing that behaviour.

This preserves the invariant:

> **Natural language may describe intent; it does not become executable authority.**

---

## 9. No merchant-authored rule language

A Capability Configuration Contract shall not expose a generic executable rule language.

Rejected:

```text
IF customer.type = VIP
AND order.value > 200
THEN auto-confirm
ELSE request approval
```

Also rejected:

```text
merchant JavaScript
merchant expressions
merchant SQL
merchant-authored condition graphs
arbitrary workflow DSL
```

If a behaviour is important enough for Main Street to execute, it must be represented through registered semantics, policies, operations, requirements or another accepted platform-owned contract.

---

## 10. Applicability

A decision may be potentially registered yet not applicable to a particular merchant configuration.

Canonical process:

```text
resolve active capabilities
        ↓
resolve applicable semantic/configuration context
        ↓
resolve applicable configuration decisions
        ↓
apply explicit selection / default / required resolution
```

A decision cannot make its own applicability true by being selected.

An applicability condition must be platform-owned. Merchant configuration may satisfy or select values used by that condition, but cannot author the condition itself.

This mirrors the separation already established for conditional requirements.

---

## 11. Resolution states and provenance

Where applicable, resolved configuration shall preserve at least the following distinctions:

```text
NOT_APPLICABLE
DEFAULTED
EXPLICITLY_SELECTED
```

Onboarding may additionally preserve `INFERRED`, `MERCHANT_SELECTED`, `DERIVED` and `UNRESOLVED` candidate-decision provenance under MS-PROT-039.

Those onboarding states do not all need to survive as executable-model states.

Critical distinction:

```text
proposal provenance
        ≠
accepted configuration provenance
        ≠
resolved executable value
```

The compiler must not treat an inferred value as privileged merely because inference produced it.

---

## 12. Required and optional decisions

An applicable decision may be:

- required and resolvable by a registered default;
- required and require an explicit/derived supported value;
- optional.

If a required applicable decision has neither a valid accepted value nor a valid registered default, compilation/publication must fail rather than invent behaviour.

The result is a configuration gap, not an inference opportunity for the compiler.

---

## 13. Context-scoped configuration

MS-PROT-039 establishes that merchant behaviour may differ by offering or operation.

MS-PROT-047 therefore prohibits accidental merchant-wide configuration when semantics require narrower scope.

Example:

```text
Initial Consultation
    scheduling mode = CUSTOMER_SELECTS_AVAILABLE_TIME

Complex Project Review
    scheduling mode = MERCHANT_PROPOSES_OR_CONFIRMS
```

The configuration contract must retain enough scope identity for those values to coexist without creating separate consultant/business templates.

Scope must be explicit and validated. A narrower scoped value cannot silently override an unrelated context.

---

## 14. Policy compatibility

The existing `OwnedPolicyDefinition` / `PolicySelection` contract remains valid.

A registered policy is one concrete form of capability configuration:

```text
registered finite allowed values
        ↓
optional merchant selection
        ↓
DEFAULTED / EXPLICITLY_SELECTED / NOT_APPLICABLE
```

MS-PROT-047 does not redefine policy semantics.

Instead it establishes the broader rule that future bounded parameters and references must obey the same ownership, applicability, validation and no-hidden-activation discipline.

---

## 15. Merchant-owned operational data is not automatically configuration

The contract shall distinguish stable configuration from live operational truth.

Examples:

```text
working hours                   configuration
appointment currently occupied runtime state

low-stock threshold             configuration
current stock quantity          runtime state

cancellation window             configuration
specific cancellation event     operational fact
```

Likewise merchant content such as names, descriptions and media does not become a semantic configuration decision merely because onboarding collects it.

---

## 16. Provider selection is outside this contract

Capability meaning and capability fulfilment remain separate.

```text
WHAT the merchant can do
        ↓
Capability semantics + configuration

HOW the capability is fulfilled
        ↓
Provider contract / provider binding
```

Example:

```text
Scheduling semantics
        ↓
configured scheduling behaviour
        ↓
fulfilled by Main Street or a supported external provider
```

`Calendly`, `Google Calendar`, a payment provider or another integration shall not become an ENUM value merely to avoid designing the provider contract.

Provider fulfilment requires a separate accepted contract.

---

## 17. Projection metadata is outside this contract

A Capability Configuration Contract does not define dashboard pages, storefront sections, navigation items or UI components.

Those are projection concerns.

```text
Capability semantics
        +
resolved configuration
        ↓
Projection contract
        ↓
website / dashboard / POS / other surface
```

This prevents semantic definitions from becoming a giant descriptor containing runtime meaning, onboarding questions, provider adapters and presentation metadata.

---

## 18. Inference boundary

Inference may propose configuration values only from registered contracts.

```text
Merchant evidence
        ↓
Inference
        ↓
Configuration proposal
        ↓
Merchant correction / approval
        ↓
Merchant configuration
        ↓
Compiler
```

Inference may:

- propose an allowed ENUM value;
- propose a BOOLEAN;
- extract a candidate bounded scalar;
- identify a likely reference;
- populate a candidate structured bounded value.

Inference may not:

- add an unregistered option;
- change bounds;
- invent a new decision;
- author applicability logic;
- activate a capability by setting one of its configuration values;
- bypass compiler validation.

---

# 19. Cross-domain falsification

The contract was tested against materially different merchant operations.

## 19.1 Salon

Relevant active semantics may include Scheduling, Booking, CustomerContext and capacity-bearing staff/resources.

Configuration needs can be represented as:

```text
confirmation mode           ENUM
customer cancellation       BOOLEAN
cancellation window         BOUNDED_SCALAR
service/resource binding    REFERENCE
working hours / breaks      STRUCTURED_BOUNDED_VALUE
```

No salon-specific configuration primitive is required.

**PASS**

## 19.2 Motel

A motel may use date-range Booking and Resource Allocation without being forced into appointment scheduling.

Representative stable decisions remain expressible through bounded values/references and registered booking/resource semantics.

The model does not require `MotelConfiguration` or an appointment-calendar assumption.

**PASS**

## 19.3 Solicitor / professional service

Consultations may differ by offering:

```text
initial consultation → customer-selectable time
complex matter review → merchant proposes/confirms
```

Context-scoped ENUM values plus bounded duration/schedule values represent the difference without a solicitor template.

Client facts/intake data remain schema-governed data rather than free-form configuration semantics.

**PASS**

## 19.4 Online consultant using Calendly

Scheduling capability/configuration remains semantic.

Calendly is not represented as a capability or as a configuration value in this contract. It is a future fulfilment-provider binding.

This case falsifies any design that mixes capability configuration and provider implementation.

**PASS**

## 19.5 Scholarship-information publisher

Publication/Opportunity/Enquiry may be active while Booking, Inventory and Commerce remain absent.

The merchant may require very little executable configuration; merchant content and opportunity data remain content/data, not forced configuration settings.

This case falsifies any design that assumes every merchant requires the same configuration surface.

**PASS**

## 19.6 Product retailer

Inventory and Commerce may use bounded configuration such as:

```text
fulfilment behaviour        ENUM
low-stock threshold         BOUNDED_SCALAR
default configured location REFERENCE
store operating hours       STRUCTURED_BOUNDED_VALUE
```

Current stock, individual orders and inventory movements remain operational state/facts.

No retailer-specific primitive is required.

**PASS**

---

## 20. Failed alternatives

| Rejected approach | Why it fails |
| --- | --- |
| Business-category configuration objects | Creates `RestaurantConfiguration`, `SalonConfiguration`, etc. and turns categories into architecture |
| One giant `CapabilityDescriptor` | Mixes semantics, configuration, providers, onboarding and projection concerns |
| Arbitrary key/value settings | Loses semantic ownership, typing, bounds and compiler authority |
| Free-text executable settings | Makes natural language an executable-rule surface |
| Generic merchant rule DSL | Reintroduces merchant-authored executable semantics |
| Configuration values that activate capabilities | Hides capability graph changes inside settings |
| Provider names as capability configuration | Conflates semantic behaviour with fulfilment mechanism |
| Merchant-wide values for every decision | Breaks mixed offerings and context-dependent operation |
| Treat all merchant data as configuration | Collapses stable configuration and runtime/business data |
| Put exact questions into capability definitions | Couples semantic contract to one onboarding presentation |

No material falsifier remains after these separations.

---

## 21. Accepted invariants

1. A Capability Configuration Contract configures an already-active capability.
2. Configuration cannot activate a capability unless activation is represented separately through the accepted capability-resolution model.
3. Every configuration decision has capability-scoped semantic ownership.
4. A merchant may select/narrow/parameterise only Main Street-defined semantics.
5. Configuration definition and merchant-facing presentation remain separate.
6. The initial value-domain algebra is BOOLEAN, ENUM, BOUNDED_SCALAR, REFERENCE and STRUCTURED_BOUNDED_VALUE.
7. Arbitrary TEXT is not an executable configuration primitive.
8. No merchant-authored executable rule language is permitted.
9. Applicability logic is platform-owned and referenced rather than merchant-authored.
10. Required applicable decisions must resolve through a supported value/default or compilation must fail.
11. Context-scoped configuration is preserved where operation differs by offering/operation/resource context.
12. Registered Policy remains a valid specialised form of capability configuration.
13. Stable configuration and live operational state remain distinct.
14. Merchant content/data does not automatically become semantic configuration.
15. Provider fulfilment and provider binding are separate from capability configuration.
16. Projection metadata is separate from capability configuration.
17. Inference may propose only registered values and has no privileged compiler path.
18. The contract must scale by reusable capability semantics, not merchant-category templates.

---

## 22. Implementation consequences

MS-PROT-047 intentionally does not mandate immediate implementation of all five value domains.

The current code already proves one bounded subset through registered policies and `PolicySelection`.

Future implementation shall proceed tests-first and may introduce additional configuration-definition/value types only when a representative capability requires them.

The next implementation slice must not broaden `MerchantConfiguration` speculatively merely to mirror this entire conceptual vocabulary.

Hard implementation rule:

> **Introduce a configuration primitive only when an accepted capability contract and failing conformance test require it.**

---

## 23. Deferred decisions

MS-PROT-047 does not yet define:

- Java type names or package layout for configuration decisions;
- persistence representation;
- exact catalogue of capability configuration decisions;
- provider fulfilment/provider binding contract;
- merchant-facing projection contract;
- exact onboarding questions or wording;
- inference provider/model;
- visual settings screens;
- pricing-tier entitlement effects;
- arbitrary workflow automation.

These concerns must consume this contract rather than redefine it.

---

## 24. Governance verdict

```text
PROPOSED BOUNDARY            ✓
MANUAL APPROVAL              ✓
CROSS-DOMAIN FALSIFICATION   ✓
CATEGORY-TEMPLATE PRESSURE   REJECTED
PROVIDER LEAKAGE             REJECTED
RULE-LANGUAGE LEAKAGE        REJECTED
ONBOARDING/PRESENTATION LEAK REJECTED
ACCEPT                       ✓
```

### Canonical decision

> **Main Street capabilities expose a small, typed, capability-owned configuration surface. Merchants may choose among or parameterise registered behaviour, while capability activation, semantic meaning, applicability rules and compiler invariants remain Main Street-controlled. Configuration is declarative and bounded; presentation, inference and provider fulfilment are separate layers.**
