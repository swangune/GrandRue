# MS-PROT-061 v1.1 — Optional Merchant Returns Policy Capability & Production Return-Action Execution Contract Amendment

**Document ID:** MS-PROT-061  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 27 August 2026 — explicit manual approval of the complete revised Target-15 package  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-061 v1.0  
**Depends on:** MS-PROT-027, MS-PROT-043, MS-PROT-048, composite MS-PROT-055, composite MS-PROT-058, MS-PROT-059, composite MS-PROT-060, MS-PROT-061 v1.0, MS-PROT-062, MS-PROT-063, MS-PROT-064, MS-PROT-069, MS-PROT-072, composite MS-PROT-077, MS-PROT-079  
**Closes with MS-PROT-058 v1.2:** Target 15 — Returns

---

## 1. Governing Decision

Returns is an **optional merchant-policy capability**.

Its presence gives the merchant structured Main Street infrastructure for recurring return policy and return operations.

It is **not** the source of the merchant's authority to decide what happens in an individual customer situation.

Canonical:

```text
MERCHANT BUSINESS AUTHORITY
        │
        ├── exists whether Returns is enabled or disabled
        │
        ▼
merchant decides business response
        │
        ▼
independently applicable Main Street capabilities
execute that response
```

Therefore:

```text
Returns disabled
    ≠ returns prohibited

Returns enabled
    ≠ a return is approved

Customer asks for return
    ≠ Returns becomes enabled

Ordering enabled
    ≠ Returns enabled

Inventory enabled
    ≠ Returns enabled

Shipment enabled
    ≠ Returns enabled

Payment enabled
    ≠ Returns enabled
```

Main Street provides infrastructure. The merchant remains the business decision-maker, subject to applicable law, security, contractual commitments and platform-wide non-overridable constraints.

---

## 2. Returns Capability Applicability

Current Returns applicability is established only through accepted Merchant Configuration.

It MUST NOT be inferred from:

```text
business category
Product presence
Ordering
Inventory
Shipment
Payment
merchant website layout
customer request
AI inference
industry convention
```

AI/onboarding MAY propose that Returns be enabled.

Only the accepted merchant configuration process may make that proposal authoritative.

---

## 3. Capability Disabled

When Returns is not applicable for the merchant's current configuration, Main Street MUST NOT manufacture:

```text
structured Returns policy surface
return eligibility workflow
return-specific customer mutation flow
return-specific merchant dashboard workflow
automated return adjudication
return-label preparation operation
return-specific automation
```

But Main Street MUST NOT interpret absence as a merchant business prohibition.

The customer may still:

```text
contact merchant
```

through applicable Conversation/Enquiry infrastructure.

The merchant may still decide to:

```text
refund
replace
accept goods back
decline
make an exception
give instructions
use another legitimate remediation
```

and may execute each consequence through independently applicable Main Street capabilities.

---

## 4. Capability Enabled

When Returns is applicable, the merchant may configure registered recurring return-policy semantics such as:

```text
return window
condition requirements
contact requirement
return carriage responsibility
instructions
supported recurring remedies
```

Returns then streamlines:

```text
policy configuration
policy publication
historical-policy association
merchant decision support
provider-backed return-label preparation
structured return-related operational assistance
```

It still does not automatically adjudicate an individual case.

---

## 5. Merchant Policy vs Merchant Authority

Two concepts are distinct:

```text
RETURNS POLICY
    recurring configured merchant rules

MERCHANT RETURN DECISION
    merchant's authoritative decision
    for a specific business situation
```

A merchant may legitimately make an exception to ordinary policy where permitted.

Therefore:

```text
policy says no discretionary returns
    ≠ Main Street prevents merchant goodwill Refund

policy window expired
    ≠ merchant technically forbidden from making exception
```

Main Street may inform the merchant of policy.

It must not replace the merchant's judgement.

---

## 6. No Return Workflow Aggregate

Target 15 continues to reject:

```text
ReturnRequest
ReturnCase
ReturnApproval
ReturnWorkflow
ReturnStatus
ReturnOrder
ReverseOrder
```

Customer communication remains communication.

The merchant's chosen consequences remain facts owned by their respective capabilities.

---

## 7. Customer Contact When Returns Is Disabled

Canonical:

```text
Customer
    ↓
Conversation / Enquiry
    ↓
exact Order context where available
    ↓
Merchant
    ↓
merchant decides
```

No Returns capability is required merely to discuss a return.

A customer statement such as:

> I want to return this item.

creates no platform return approval or workflow.

---

## 8. Return-Related Merchant Action

A **Merchant Return Action** is an operation-time business instruction explaining why an independently owned consequence is being requested.

It is not an Operational Object.

It may authorise a specific consequence such as:

```text
Refund £X
send remedial replacement
release still-unfulfilled quantity
generate return label
provide instructions
```

The resulting business effect remains owned by Payment, Ordering, Shipment/Fulfilment, Inventory or the applicable provider boundary.

---

## 9. Return Action Context

A structured Returns action, when Returns is applicable, must resolve as required:

```text
MerchantScope
trusted merchant principal
Actor Authorisation
exact Order
relevant committed/supplied quantities
historical return-policy provenance
customer/order context
requested consequence
logical command identity
merchant instruction provenance
```

Identifiers locate facts; they do not create authority.

---

## 10. Historical Policy Affinity

A later merchant configuration change MUST NOT rewrite historical commercial terms.

Example:

```text
Order O1 committed
under Returns policy R1

merchant later changes/disables Returns
```

O1 retains the historical policy provenance that actually applied.

Therefore:

```text
current Returns configuration
    governs future/current structured capability availability

historical Order return terms
    preserve historical commercial truth
```

---

## 11. Residual Historical Authority

Disabling Returns for future commerce MUST NOT make Main Street incapable of resolving obligations already created under historical merchant terms.

Where an existing Order has a historical return-related obligation whose resolution legitimately requires previously accepted Returns infrastructure, bounded residual operation may remain available for that exact historical commitment.

Residual authority:

```text
does not enable Returns for new Orders
does not create new merchant policy
does not expose unrelated return tooling
does not bypass merchant/security authority
```

This follows the same principle used elsewhere for resolving existing commitments after configuration changes.

---

# Independent Capability Consequences

## 12. Refund Does Not Require Returns Capability

Payment Refund authority MUST NOT depend universally on Returns applicability.

Canonical:

```text
merchant-authorised business consequence
        ↓
payment.refund.request
```

The business reason may be:

```text
return
goodwill
service failure
pricing correction
merchant exception
other accepted remediation
```

Payment owns monetary/provider validation.

Returns does not.

---

## 13. Return-Sourced Refund Permission

Where structured Returns is applicable, an explicit merchant Return action may provide the business permission consumed by `payment.refund.request`.

Where Returns is disabled, another authenticated merchant business instruction may provide equivalent legitimate Refund permission without pretending that Returns became enabled.

Payment must validate its own requirements either way.

---

## 14. Refund Independence

Physical goods need not return before Refund.

Valid:

```text
merchant:
"Keep it; I will refund you."
```

Likewise:

```text
goods physically returned
    ≠ Refund automatically due
```

---

## 15. Replacement Does Not Require Returns Capability

A merchant may authorise remedial replacement even when structured Returns is disabled.

Where the remediation is within the existing commercial commitment:

```text
merchant instruction
    ↓
shipment.redispatch
    ↓
new Shipment attempt
    +
additional Inventory consumption where required
```

Order Fulfilment quantity is not increased again.

---

## 16. Commercial Exchange Boundary

A materially different exchange involving new commercial terms, price differences or a genuinely new purchase commitment MUST NOT be hidden inside remedial replacement.

That remains deferred.

---

## 17. Returned Inventory Does Not Require Returns Capability

A merchant may physically receive goods back regardless of whether structured Returns was enabled.

Therefore:

```text
inventory.return.receive
```

MUST NOT have `Returns capability applicable` as a universal precondition.

Inventory owns physical receipt truth.

---

# Return Labels

## 18. Structured Return-Label Infrastructure

Main Street-managed provider-backed return-label preparation is a **Returns capability service**.

For ordinary new return-label requests:

```text
Returns applicable
+
merchant authorisation
+
applicable carriage policy/exception
+
provider fulfilment binding
+
Provider Readiness
```

are required.

If Returns is disabled, Main Street does not pretend the structured return-label facility is active.

The merchant remains free to handle carriage independently outside that capability.

Historical residual authority under §11 may still apply to an existing commitment where warranted.

---

## 19. ReturnLabelPreparationRequest

A durable Returns-owned progression identity is established before provider side effect:

```text
ReturnLabelPreparationRequest
{
    requestIdentity
    MerchantScope

    exact Order / supplied scope

    return origin
    merchant return destination

    carriage authorisation
    policy or exception provenance

    Fulfilment Binding
    ProviderConnection

    correlation/idempotency identity
    logical command identity
    acceptedAt
}
```

It is not:

```text
Return approval
ReturnCase
Refund
Shipment
Inventory receipt
```

---

## 20. Provider Role

Canonical Provider Fulfilment role:

```text
returns / return-label-preparation
```

It owns technical obligations such as:

```text
prepare exact label request
preserve correlation
preserve idempotency
return authenticated label/reference evidence
support provider reconciliation where available
```

It does not decide whether the merchant should accept the customer's return.

---

## 21. `returns.return-label.prepare`

Owner: Returns.

For a new structured request, it must validate:

```text
Returns applicability
or exact residual historical authority

trusted merchant principal
Actor Authorisation

exact Order/supply context

merchant carriage authorisation

historical/current policy as appropriate

Provider Fulfilment Binding
ProviderConnection
Provider Readiness
```

The durable request commits before the provider call.

No provider side effect occurs inside the local database transaction.

---

## 22. Carriage Control

A merchant-funded label may be generated only where:

```text
merchant's applicable configured policy
authorises merchant-funded carriage
```

or:

```text
merchant explicitly authorises the cost
for this exact case
```

Customer preference alone is insufficient.

---

## 23. Provider Readiness

For new provider work:

```text
READY
    → may proceed

DEGRADED
    → only if exact registered provider contract
      permits the operation safely

NOT_READY
    → do not initiate

UNKNOWN
    → do not initiate
```

---

## 24. Provider Uncertainty

If a provider may have generated/charged for a label but Main Street loses the response:

```text
EXECUTION_UNCERTAIN
```

applies.

No blind duplicate provider request.

Reconciliation follows MS-PROT-069.

---

## 25. ProviderReturnLabelEvidence

Provider evidence is immutable external evidence.

It may retain:

```text
provider identity
request identity
provider reference
label artifact reference
tracking reference
provider result evidence
observedAt
binding/connection provenance
```

It does not approve a return.

---

## 26. Return Movement Evidence

Courier tracking associated with a return remains provider transport evidence.

It MUST NOT automatically establish:

```text
merchant physically received goods
Inventory stock increased
goods are sellable
Refund
replacement
Order release
```

---

## 27. Customer-Arranged Returns

A customer may return goods using another arrangement where the merchant permits it.

Therefore:

```text
Main Street return label
```

is never a universal prerequisite for Inventory receipt.

---

# Customer Surface

## 28. Customer Relationship

No new:

```text
returns / related-customer-return
```

is introduced.

There is no Return Operational Object requiring a second relationship authority.

Return-related Order access reuses:

```text
ordering / related-customer-order
```

---

## 29. Returns Disabled Customer Surface

When structured Returns is disabled for current commerce:

```text
no structured return-policy capability contribution
```

is produced merely because the merchant sells goods.

Generic:

```text
Contact merchant
```

may still be available through the appropriate communication capability.

---

## 30. Returns Enabled Customer Surface

When applicable, candidate CUSTOMER elements may include:

```text
returns / applicable-order-return-policy
returns / merchant-return-instructions
returns / merchant-issued-return-label-reference
returns / provider-return-movement-summary
```

Each still requires:

```text
exact Order relationship
Projection Serviceability
Exposure
security/data-protection checks
```

---

## 31. Historical Policy Observation

For an existing Order, an applicable historical return-policy representation may remain observable where required even after Returns is disabled for new commerce.

The representation must clearly derive from:

```text
historical Order policy provenance
```

rather than current merchant configuration.

---

## 32. No Customer Mutation Authority

Seeing a policy or return label does not authorise:

```text
Refund
replacement
restock
return approval
new label
```

Customer self-service return execution remains future scope.

---

# Reads

## 33. Initial Read Architecture

Initial Returns reads use request-scoped composition of current authoritative owners.

No initial:

```text
ReturnCase table
ReturnStatus projection
Returns workflow database
Redis Returns cache
```

is required.

A later materialised projection must satisfy MS-PROT-027 triggers.

---

# AI

## 34. AI Boundary

AI may:

```text
suggest enabling Returns during onboarding
interpret merchant policy intent
summarise historical policy
identify likely Order context
prepare candidate merchant actions
```

AI may not:

```text
enable Returns independently
decide customer entitlement
force merchant policy
approve Refund
decide carriage responsibility
declare physical return received
declare goods sellable
```

---

# Failure and Retry

## 35. Failure Classes

Returns-specific operations preserve at least:

```text
VALIDATION_REJECTION
AUTHORISATION_REJECTION
RETURNS_NOT_APPLICABLE
RETURN_CONTEXT_INVALID
RETURN_LABEL_NOT_APPLICABLE
PROVIDER_NOT_READY
PROVIDER_BUSINESS_REJECTION
AUTHORITATIVE_CONFLICT
TECHNICAL_FAILURE
EXECUTION_UNCERTAIN
```

`RETURNS_NOT_APPLICABLE` applies only to Returns-owned structured operations.

It MUST NOT be used to prohibit independent Payment, Shipment, Inventory or communication operations.

---

## 36. No Global Return Transaction

There is no universal atomic transaction spanning:

```text
Refund
replacement
label
Inventory receipt
Inventory disposition
```

These are independently owned facts and may occur at different times.

MS-PROT-072 may coordinate technical progression without becoming Return business authority.

---

# Falsification

## 37. Falsification

| Scenario | Required outcome |
|---|---|
| Merchant sells physical goods but never enabled Returns | no structured Returns capability |
| Customer asks merchant for return anyway | customer may contact merchant |
| Merchant with Returns disabled gives Refund | valid Payment path if otherwise authorised |
| Merchant with Returns disabled sends replacement | valid Shipment/Inventory path |
| Goods arrive back to merchant with Returns disabled | Inventory may record physical receipt |
| Merchant has Returns enabled | structured policy/tooling available |
| Returns enabled but merchant rejects individual request | no automatic approval |
| Returns policy normally rejects case but merchant makes exception | merchant may authorise independent consequence |
| Returns later disabled after old Order promised return terms | historical terms survive |
| Customer request tries to activate Returns | reject |
| AI infers retailer normally accepts returns | no activation |
| Customer-funded Returns | no merchant-funded label automatically |
| Label provider timeout | uncertain; no blind duplicate |
| Courier says parcel returned | no Inventory mutation |
| Refund completed without physical return | valid |
| Physical return without Refund | valid |
| Replacement sent | no duplicate Fulfilment quantity |
| Materially different commercial exchange | not disguised as replacement |

---

## 38. Rejected Alternatives

Rejected:

```text
physical retailer → Returns automatically enabled

Returns disabled → returns prohibited

Returns enabled → customer return automatically accepted

Returns capability as merchant business-authority owner

Refund requires Returns

Replacement requires Returns

Inventory receipt requires Returns

customer request activates Returns

AI activates Returns

ReturnCase / ReturnStatus / ReturnWorkflow

platform return adjudication

provider movement = returned stock

current config rewriting historical Order policy
```

---

## 39. Deferred Scope

Deferred:

```text
customer self-service return workflow
automatic return adjudication
automatic Refund from policy
advanced commercial exchange
store credit
repair/reconditioning workflow
dedicated reverse-logistics aggregate
package-level returns
marketplace disputes
exact courier adapter
exact APIs
return UI
generic background machinery
generic operator/reconciliation tooling
```

---

## 40. Conformance

A conforming implementation must prove:

```text
[ ] Returns applicability comes from Merchant Configuration

[ ] commerce capabilities do not imply Returns

[ ] Returns disabled does not prohibit merchant business discretion

[ ] generic customer contact remains possible when applicable

[ ] independent Refund does not require Returns

[ ] independent replacement does not require Returns

[ ] physical Inventory receipt does not require Returns

[ ] structured return-label preparation does require
    Returns applicability or valid historical residual authority

[ ] historical return terms survive later configuration changes

[ ] merchant remains return decision-maker

[ ] AI cannot activate Returns or adjudicate returns

[ ] no Return workflow aggregate is introduced

[ ] customer relationship reuses Ordering

[ ] no speculative Returns projection is introduced
```

---

## 41. Governing Principle

> **Returns is optional infrastructure, not business sovereignty. A merchant may enable the Returns capability to systematise recurring return policy and streamline return operations, but Main Street does not acquire authority to decide whether the merchant may accept, refuse or exceptionally resolve an individual return. When Returns is disabled, customers may still contact the merchant and the merchant may use any independently applicable Main Street capability to execute its chosen response. Current configuration governs structured capability availability; historical commercial terms remain historical truth.**
