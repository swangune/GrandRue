# MS-PROT-048 v1.3 — Fulfilment Requirement Applicability & Obligation Subset Amendment

**Document ID:** MS-PROT-048  
**Version:** 1.3  
**Status:** **ACCEPTED after manual approval**  
**Amends:** MS-PROT-048 v1.2 — Merchant Fulfilment Binding Set Revision & Activation Affinity Amendment  
**Preserves:** MS-PROT-048 v1.0-v1.2 role, provider, open-first selection, binding-set revision and single-activation-authority rules outside this amendment's scope  
**Depends on:** MS-PROT-021, MS-PROT-022 v1.5, MS-PROT-040 v1.1, MS-PROT-047 v1.0, MS-PROT-048 v1.0-v1.2, MS-PROT-054 v1.0  
**Purpose:** Make Fulfilment Role applicability and provider compatibility derive deterministically from the exact fulfilment obligations required by the resolved merchant configuration, including when the target role is owned by a platform-service/infrastructure context.

---

## 1. Governing decision

> **A Fulfilment Role is applicable to a resolved merchant configuration if, and only if, one or more registered Fulfilment Requirements resolve to a non-empty set of applicable obligations for that role and semantic context. Provider compatibility SHALL be evaluated against that exact applicable obligation set, not automatically against every obligation the role can express.**

Canonical resolution:

```text
Registered capability semantics
        +
Resolved capability configuration
        ↓
Registered Fulfilment Requirements
        ↓
configuration-specific applicable obligations
        ↓
Group by Fulfilment Role + semantic context
        ↓
Applicable Fulfilment Role/context
        +
Merchant Fulfilment Binding
        ↓
Provider compatibility against exact required subset
        ↓
FulfilmentPlan
```

This amendment removes the need for a second independent role-applicability framework.

---

## 2. Role, Requirement and Binding are distinct

The following concepts SHALL remain separate:

```text
Fulfilment Role
    What technical responsibility can be discharged?

Fulfilment Requirement
    Which part of that responsibility is required by this exact resolved configuration?

Fulfilment Binding
    Who is selected to discharge the applicable responsibility?

ProviderConnection / Provider Readiness
    Can the selected provider currently be contacted/executed successfully?
```

Therefore:

```text
Role ≠ Requirement
Requirement ≠ Binding
Binding ≠ ProviderConnection
ProviderConnection ≠ Provider Readiness
```

A provider binding cannot create a requirement, make an otherwise inapplicable role applicable, or weaken the applicable obligation set to fit provider capabilities.

---

## 3. Fulfilment Requirement Definition

A `FulfilmentRequirementDefinition` is a registered, platform-controlled declaration that an active capability may require one or more obligations of a registered Fulfilment Role.

Conceptually:

```text
FulfilmentRequirementDefinition
{
    ownerCapabilityIdentifier
    requirementIdentifier
    roleIdentity
    semanticContext?
    obligationIdentifiers[]
    applicability
}
```

The exact Java representation is implementation-specific.

### 3.1 Requirement ownership

For this accepted v1.3 foundation, each Fulfilment Requirement is owned by one registered capability.

The owner answers **why** the technical responsibility is required. The referenced Fulfilment Role may be owned by the same capability or by a different explicitly registered platform-service/infrastructure context.

Example:

```text
booking capability
    owns requirement: booking-confirmation-delivery
        ↓ references
notification / message-delivery role
        ↓ obligation
SEND_CONFIRMATION
```

This allows platform-service/infrastructure-owned roles to become applicable without treating the platform's mere existence as applicability evidence.

A future platform-originated requirement model, if needed independently of an active capability, requires separate accepted ownership semantics and SHALL NOT be inferred by this amendment.

---

## 4. Requirement identity

A requirement identity SHALL be stable within its owner capability.

Conceptually:

```text
FulfilmentRequirementIdentity
{
    ownerCapabilityIdentifier
    requirementIdentifier
}
```

Two capabilities MAY use the same local requirement identifier because the owner capability qualifies the identity.

---

## 5. Referenced role and obligation registration

Every requirement SHALL reference:

1. one registered `FulfilmentRoleIdentity`; and
2. one or more obligations declared by that role.

Registry publication MUST fail when a requirement references:

- an unregistered role;
- an obligation absent from the referenced role; or
- an invalid/blank semantic identity.

A capability therefore cannot invent provider-specific or ad-hoc obligations during merchant resolution.

---

## 6. Requirement applicability

Requirement applicability SHALL be derived only from deterministic static evidence available during configuration resolution.

For the v1.3 executable foundation, the accepted applicability forms are deliberately bounded:

```text
ALWAYS

WHEN_ENUM_CONFIGURATION_DECISION_VALUE_IN
    decisionIdentity
    acceptedValues[]
```

No general-purpose expression language is accepted.

Rejected as part of this amendment:

```text
arbitrary scripts
SpEL / JavaScript expressions
generic boolean expression trees
runtime provider health predicates
current actor authority predicates
current inventory/availability predicates
unregistered JSON rule fragments
```

This preserves deterministic configuration compilation and prevents Fulfilment applicability from becoming a second programming language.

---

## 7. ALWAYS applicability

`ALWAYS` means:

> when the requirement-owning capability is active in the resolved executable model, this requirement contributes its declared obligations.

It does **not** mean that the target Fulfilment Role applies to every merchant merely because the role is registered or platform-owned.

Canonical rule:

```text
owner capability active
        +
requirement applicability = ALWAYS
        ↓
requirement obligations applicable
```

If the owner capability is inactive, the requirement contributes nothing.

---

## 8. Configuration-decision applicability

A requirement MAY depend on an already-resolved capability configuration decision.

For the v1.3 executable foundation the bounded condition is:

```text
WHEN_ENUM_CONFIGURATION_DECISION_VALUE_IN
{
    decisionIdentity
    acceptedValues[]
}
```

The referenced decision identity SHALL use the capability-scoped identity governed by MS-PROT-047.

A condition is satisfied only when:

1. the requirement-owning capability is active;
2. the referenced resolved decision exists;
3. the decision is applicable (`DEFAULTED` or `EXPLICITLY_SELECTED`);
4. the decision's value domain is `ENUM`; and
5. the resolved ENUM value is a member of the registered accepted-value set.

`NOT_APPLICABLE`, a missing decision, a non-ENUM value domain, or a non-matching value means the requirement contributes no obligations.

The requirement condition consumes resolved configuration evidence. It does not redefine the configuration decision.

---

## 9. Role applicability is derived from obligations

There SHALL NOT be a second independent boolean role-applicability authority in this model.

For each `(FulfilmentRoleIdentity, semanticContext)` group:

```text
applicableObligations = union(
    obligations of every satisfied registered requirement
)

roleApplicable ⇔ applicableObligations is not empty
```

This prevents contradictory states such as:

```text
roleApplicable = false
requiredObligations = { SEND_CONFIRMATION }
```

The applicable obligation set is the sole static applicability evidence for the role/context.

---

## 10. Configuration-specific obligation subset

A role definition describes the complete obligation vocabulary that role can express. It does not mean every merchant configuration requires every obligation.

Example:

```text
Role: notification / message-delivery
registered obligations:
    SEND_CONFIRMATION
    SEND_REMINDER
    SEND_CANCELLATION

Merchant resolved configuration:
    confirmations = enabled
    reminders = disabled
    cancellations = enabled

Applicable obligation set:
    SEND_CONFIRMATION
    SEND_CANCELLATION
```

The resolver SHALL use that applicable subset for binding and compatibility validation.

---

## 11. Provider compatibility rule

For an external provider binding:

```text
requiredObligations ⊆ providerSupportedObligations
```

MUST hold for the exact applicable role/context obligation set.

The provider does not need to support registered role obligations that are not applicable to this resolved configuration.

Conversely, a provider's inability to satisfy any applicable obligation causes static resolution to fail closed.

Rejected:

```text
provider lacks SEND_REMINDER
        ↓
resolver silently drops SEND_REMINDER
```

Accepted:

```text
SEND_REMINDER is not applicable under resolved configuration
        ↓
provider is not required to support it for this configuration
```

---

## 12. Binding completeness

For every applicable `(roleIdentity, semanticContext)` group, the exact pinned `FulfilmentBindingSetRevision` SHALL contain one matching binding selection.

Static resolution SHALL fail closed when:

- an applicable role/context has no binding;
- a binding references an unregistered role;
- an external provider is unregistered;
- the provider cannot satisfy the exact applicable obligation subset; or
- any existing MS-PROT-048 v1.2 affinity invariant fails.

A binding for a role/context with no applicable obligations SHALL NOT create plan applicability.

The implementation MAY reject such extraneous bindings or omit them deterministically; the selected behaviour SHALL be stable and tested. The v1.3 executable foundation chooses fail-closed rejection to prevent stale routing configuration from silently surviving semantic/configuration changes.

---

## 13. Platform-service/infrastructure-owned Fulfilment Roles

A role owned by an explicitly registered platform service or infrastructure context is not automatically applicable.

Example:

```text
notification / message-delivery
```

may be referenced by requirements owned by:

```text
booking
appointment
publication
customer-enquiry
```

If none of the active capabilities produces an applicable requirement for the role, the role is absent from the Fulfilment Plan.

Therefore:

```text
platform service exists
    ≠
role applicable
```

and:

```text
one or more satisfied registered capability requirements
    ↓
non-empty obligation set for platform-owned role
    ↓
role applicable
```

---

## 14. Static/live boundary remains unchanged

Requirement applicability SHALL NOT depend on live facts such as:

```text
provider health
credential validity
network availability
current actor authority
current capacity
current inventory
current operational-object state
```

Those remain contextual/runtime concerns under the applicable authorities.

A provider outage therefore does not remove the static requirement or rewrite the binding.

---

## 15. Resolution ordering

The deterministic static order is:

```text
1. compile active capabilities and executable semantics
2. resolve bounded capability configuration
3. evaluate registered Fulfilment Requirements
4. derive obligation sets by role/context
5. derive applicable roles from non-empty obligation sets
6. resolve exact binding selections from the pinned binding-set revision
7. validate internal/external fulfiller contracts
8. materialise FulfilmentPlan with exact required-obligation evidence
```

Provider selection cannot feed back into steps 1-5.

---

## 16. Fulfilment Plan evidence

The resolved `FulfilmentPlan` SHOULD retain enough immutable static evidence to establish, for each resolved binding:

```text
role identity
semantic context
exact applicable obligation set
selected fulfiller
provider compatibility evidence where external
binding-set revision provenance
fulfilment-contract registry provenance
```

This evidence supports historical reconstruction and prevents later role/provider changes from obscuring why a provider was considered compatible.

---

## 17. Registry-release affinity

Fulfilment Role definitions, Fulfilment Requirement definitions and Provider definitions used for one deterministic resolution SHALL belong to the same registered fulfilment-contract snapshot affined to the pinned semantic registry release.

A requirement or provider contract from another semantic release SHALL NOT be mixed into the Resolved Configuration Package.

---

## 18. Failure behaviour

Static resolution fails closed when required applicability/compatibility cannot be proven.

It SHALL NOT guess that:

- a missing configuration decision means `false` or `true`;
- an unregistered obligation is equivalent to another provider-specific operation;
- a provider supports an obligation because it supports the same role generally;
- a missing binding can fall back to another provider unless a separately accepted fallback contract exists.

---

## 19. Falsification cases

### 19.1 Platform-owned role is not globally applicable

```text
role: notification / message-delivery
active capability: publication
no satisfied publication requirement for message-delivery
```

Expected:

```text
message-delivery absent from applicable role set
```

**PASS**

### 19.2 Active capability with ALWAYS requirement

```text
booking active
booking-confirmation requirement = ALWAYS
obligation = SEND_CONFIRMATION
```

Expected:

```text
notification/message-delivery applicable
required obligations = { SEND_CONFIRMATION }
```

**PASS**

### 19.3 Configuration-specific subset

```text
role obligations:
    SEND_CONFIRMATION
    SEND_REMINDER

requirements:
    confirmation = ALWAYS
    reminder = WHEN reminder-policy IN { DAY_BEFORE }

resolved reminder-policy = DISABLED
```

Expected:

```text
required obligations = { SEND_CONFIRMATION }
```

Provider supporting only `SEND_CONFIRMATION` is compatible.

**PASS**

### 19.4 Matching conditional obligation

```text
resolved reminder-policy = DAY_BEFORE
```

Expected:

```text
required obligations = {
    SEND_CONFIRMATION,
    SEND_REMINDER
}
```

Provider lacking `SEND_REMINDER` is rejected.

**PASS**

### 19.5 Binding cannot create applicability

```text
binding set contains notification/message-delivery
no applicable requirements produce obligations
```

Expected: static resolution rejects the extraneous binding.

**PASS**

### 19.6 Inactive capability requirement

```text
booking requirement registered
booking capability inactive
```

Expected: requirement contributes no obligations regardless of its `ALWAYS` marker.

**PASS**

---

## 20. Rejected alternatives

The following are rejected:

1. Treating every obligation declared by a role as required for every merchant configuration.
2. Treating platform-service role registration as applicability evidence.
3. Provider-specific conditionals inside `FulfilmentPlanResolver`.
4. Allowing bindings/providers to choose which semantic obligations remain required.
5. A general-purpose scripting/expression language for fulfilment applicability.
6. Runtime provider health or credentials as static requirement applicability inputs.
7. Duplicate independent role-applicability and obligation-applicability authorities.
8. Capability-specific `if` branches embedded in the generic resolver.

---

## 21. Implementation boundary

The first executable v1.3 foundation SHALL support:

- capability-owned registered Fulfilment Requirements;
- requirements targeting roles owned by any registered fulfilment context, including platform-service/infrastructure contexts;
- `ALWAYS` applicability;
- ENUM configuration-decision value applicability;
- deterministic union of applicable obligations by role/context;
- applicability derived from a non-empty obligation set;
- rejection of extraneous bindings with no applicable obligations;
- rejection of missing bindings for applicable role/contexts;
- external-provider compatibility against the exact applicable obligation subset;
- retention of exact applicable obligations in resolved binding evidence.

This foundation SHALL NOT introduce arbitrary expressions, runtime applicability inputs or a platform-originated requirement ownership model.

---

## 22. Final decision

Main Street therefore adopts:

```text
ROLE
    complete registered technical responsibility vocabulary

REQUIREMENT
    configuration-specific required responsibility

BINDING
    selected fulfiller

CONNECTION / READINESS
    live technical execution condition
```

and the invariant:

> **A role/context is applicable exactly when its resolved applicable obligation set is non-empty. Provider compatibility is measured against that exact set.**
