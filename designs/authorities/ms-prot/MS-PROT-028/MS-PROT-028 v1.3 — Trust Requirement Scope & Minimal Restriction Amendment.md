# MS-PROT-028 v1.3 — Trust Requirement Scope & Minimal Restriction Amendment

**Document ID:** MS-PROT-028  
**Version:** 1.3  
**Status:** **ACCEPTED after continuous-improvement review and manual approval**  
**Amends:** MS-PROT-028 v1.2 — Merchant Control, Trust Claims & Conditional Verification Amendment  
**Depends on:** MS-PROT-021, MS-PROT-022 v1.5, MS-PROT-023, MS-PROT-027, MS-PROT-028 v1.2, MS-PROT-031, MS-PROT-036, MS-PROT-037, MS-PROT-040, MS-PROT-043 v1.2, MS-PROT-048 v1.1, MS-PROT-049  
**Purpose:** Define how unsatisfied trust requirements constrain Main Street operation so a missing claim restricts only the smallest semantic, operational or surface scope that actually requires it, rather than becoming a universal merchant or publication gate.

---

## 1. Governing amendment

MS-PROT-028 v1.2 remains accepted except where this amendment clarifies the scope and consequences of conditional trust requirements.

The governing decision is:

> **An unsatisfied trust requirement shall constrain the smallest operation, capability, fulfilment role, integration, surface contribution or merchant-control boundary whose accepted contract actually requires that claim. It shall not become a merchant-wide or publication-wide restriction unless the underlying legal, security, platform or account-control obligation genuinely applies at merchant-wide scope.**

Canonical rule:

```text
Specific obligation
        ↓
Specific trust requirement
        ↓
Specific affected scope
        ↓
Restriction applies there
```

Rejected rule:

```text
Any missing trust claim
        ↓
merchant unverified
        ↓
entire merchant unpublished / disabled
```

Hard invariant:

> **Trust requirement scope determines restriction scope.**

---

## 2. Why v1.3 is required

MS-PROT-028 v1.2 correctly removed universal real-world identity verification and universal business verification.

However, a remaining risk was that implementation could still collapse conditional trust requirements into one merchant-wide publication gate.

Example:

```text
Merchant capabilities:
    Publication
    Enquiry
    Catalogue
    Payment

Payment provider onboarding incomplete
```

Incorrect consequence:

```text
entire merchant unavailable
```

Correct default consequence:

```text
Publication      AVAILABLE
Enquiry          AVAILABLE
Catalogue        AVAILABLE
Payment          UNAVAILABLE / SETUP REQUIRED
```

The missing payment trust evidence belongs to the payment-related obligation unless an independent rule says otherwise.

This amendment prevents conditional verification from recreating universal gatekeeping indirectly.

---

## 3. Trust requirements are scoped obligations

A trust requirement shall be attributable to an accepted obligation and an affected semantic or operational scope.

Conceptually:

```text
TrustRequirement
{
    requirementIdentity
    requiredClaimType
    subjectScope
    affectedScope
    authoritySource
    satisfactionCondition
    failureConsequence
}
```

This structure is conceptual and does not mandate one Java type or table.

### 3.1 requirementIdentity

Stable Main Street identity for the requirement.

### 3.2 requiredClaimType

The precise claim needed, for example:

```text
PAYMENT_ACCOUNT_READY
PROFESSIONAL_REGISTRATION_CONFIRMED
AGE_RESTRICTED_OPERATION_AUTHORISED
GOOGLE_BUSINESS_PROFILE_VERIFIED
REAL_WORLD_IDENTITY_VERIFIED
ACCOUNT_CONTROL_REAUTHENTICATED
```

A generic `VERIFIED` claim is insufficient.

### 3.3 subjectScope

The person, merchant, provider account, professional registration, external profile or other subject to which the claim applies.

### 3.4 affectedScope

The operation, capability, fulfilment role, integration, surface contribution or merchant-wide security boundary whose availability depends on the requirement.

### 3.5 authoritySource

The authority able to substantiate the claim where external evidence is required.

Examples:

```text
payment provider
professional regulator
Google
identity verification provider
Main Street account-security authority
```

### 3.6 satisfactionCondition

The condition under which the requirement is presently satisfied.

### 3.7 failureConsequence

The bounded operational consequence when the requirement is not satisfied.

The consequence shall not be broader than the obligation justifies.

---

## 4. Minimum-restriction principle

When a trust requirement becomes unsatisfied, Main Street shall determine the narrowest safe restriction.

Canonical evaluation:

```text
Unsatisfied trust requirement
        ↓
Which accepted obligation requires it?
        ↓
Which semantic/operational scope depends on that obligation?
        ↓
Can unrelated scopes continue safely and lawfully?
        ├── YES → restrict only dependent scope
        └── NO  → widen restriction only as justified
```

This is not a usability preference.

It is an authority rule.

Main Street shall not infer broader risk merely because a trust claim exists elsewhere in the merchant graph.

---

## 5. Operation-scoped restriction

Where a claim is required only by one operation, that operation alone shall normally be restricted.

Example:

```text
Capability:
Payment

Operation:
CAPTURE_PAYMENT

Required claim:
PAYMENT_ACCOUNT_READY
```

If the claim is unsatisfied:

```text
CAPTURE_PAYMENT
    → UNAVAILABLE
```

Other operations such as:

```text
browse catalogue
submit enquiry
view business information
```

remain unaffected unless independently constrained.

---

## 6. Capability-scoped restriction

Where a trust requirement applies to all meaningful operations of one capability, the capability may remain semantically active while its executable/customer-visible operations are unavailable or degraded according to the applicable contract.

Example:

```text
RegulatedService capability
    requires
PROFESSIONAL_REGISTRATION_CONFIRMED
```

If registration evidence expires:

```text
RegulatedService operations
    → unavailable

Unrelated Publication / Enquiry
    → remain available
```

The capability graph need not be rewritten merely because a contextual trust requirement is currently unsatisfied.

This follows MS-PROT-022 v1.5's separation of stable configuration from contextual runtime facts.

---

## 7. Fulfilment-role restriction

A trust requirement may belong to a fulfilment role rather than the semantic capability itself.

Example:

```text
Payment semantics active
        ↓
payment-execution fulfilment role
        ↓
provider requires onboarding/KYC/KYB
```

If provider onboarding is incomplete:

```text
Payment semantics remain active
Payment fulfilment = DEGRADED / UNAVAILABLE
Checkout action = unavailable where execution is mandatory
```

This shall not imply:

```text
merchant invalid
catalogue invalid
enquiry invalid
website invalid
```

MS-PROT-048 remains authoritative for provider state and degraded fulfilment.

---

## 8. Integration-scoped restriction

External-platform verification normally constrains only that integration.

Example:

```text
Google Business Profile not verified
```

Default consequence:

```text
Google local-profile integration
    → unavailable / incomplete
```

Not:

```text
Main Street storefront unpublished
merchant unable to receive enquiries
merchant account suspended
```

Google owns Google's verification claims.

Main Street does not convert an external ecosystem prerequisite into universal Main Street eligibility.

---

## 9. Surface consequence

Surface visibility shall reflect the restricted operational scope without inventing new semantics.

Example:

```text
Checkout requires payment-execution trust requirement
        ↓
requirement unsatisfied
        ↓
Checkout ACTION contribution unavailable
        ↓
Catalogue WORKSPACE/PUBLIC_INTERACTION remains visible
```

MS-PROT-049 remains authoritative for surface contribution ownership.

The trust requirement may affect contextual eligibility of a surface contribution.

It does not transfer semantic ownership to the trust subsystem.

---

## 10. Publication is not a universal trust gate

Main Street shall not use a generic rule such as:

```text
all required trust claims satisfied
        ↓
merchant website may exist
```

Instead public activation is evaluated by applicable public surfaces and their actual requirements.

Conceptually:

```text
Public business information
    requirements satisfied
        → may publish

Enquiry
    requirements satisfied
        → may publish

Payment checkout
    payment trust unmet
        → do not expose executable checkout
```

A merchant website may therefore remain publicly available while one operational interaction is unavailable.

---

## 11. Merchant-wide restriction is exceptional but legitimate

Some trust failures genuinely affect the whole merchant-control boundary.

Examples may include:

```text
credible account takeover
controller credentials revoked
merchant-control relationship invalidated
court/legal order applying to the whole merchant
platform-wide safety/security suspension justified by evidence
```

In such cases:

```text
merchant-wide restriction
```

may be correct because the underlying obligation applies merchant-wide.

The rule is not:

```text
merchant-wide restriction is forbidden
```

The rule is:

> **Merchant-wide restriction requires a merchant-wide justification.**

---

## 12. Account-control compromise is categorically different

If Main Street cannot safely establish that the current principal is authorised to control the merchant, the restriction may legitimately apply to all privileged merchant operations.

Example:

```text
account takeover suspected
        ↓
controller authority uncertain
        ↓
privileged merchant operations suspended
```

This is not business verification.

It is preservation of the merchant-control boundary itself.

Public read-only surfaces may or may not remain available depending on the accepted incident/security policy.

MS-PROT-028 v1.3 does not prescribe one universal incident-response presentation.

---

## 13. Trust requirement and capability activation remain separate

An unsatisfied trust requirement shall not normally deactivate the capability that owns or uses the affected operation.

Rejected:

```text
Payment provider KYC incomplete
        ↓
Payment capability removed from configuration
```

Accepted:

```text
Payment capability remains configured
        ↓
required payment-execution trust/fulfilment condition unsatisfied
        ↓
contextual operation availability reflects restriction
```

This preserves historical provenance and avoids recompilation for mutable trust/provider facts unless the configuration itself changes.

---

## 14. Trust state is contextual runtime state unless configuration changes

Where satisfaction of a trust requirement can change without changing merchant configuration, the current satisfaction state is contextual runtime/integration state.

Examples:

```text
provider onboarding completed
professional registration expired
external verification revoked
security reauthentication required
```

These changes shall not automatically require rebuilding the immutable Resolved Configuration Package.

The static package may contain the fact that an operation has a trust requirement.

Contextual resolution determines whether the requirement is currently satisfied.

Canonical separation:

```text
Static configuration/package:
    operation X requires claim Y

Contextual state:
    claim Y currently SATISFIED / UNSATISFIED / UNKNOWN
```

---

## 15. Unknown trust state must not be silently treated as satisfied

Where a required claim is not known to be valid and the operation depends on it, Main Street shall preserve uncertainty.

```text
UNKNOWN
    ≠
SATISFIED
```

The affected operation shall follow its accepted failure/uncertainty contract.

Main Street shall not broaden that uncertainty to unrelated operations without evidence.

---

## 16. Trust claims must retain provenance

A claim used to satisfy a requirement shall retain enough provenance to explain:

```text
what claim was relied upon
which subject it concerned
who/what issued or established it
when it became valid
whether it expires or can be revoked
which requirement consumed it
```

Conceptually:

```text
TrustClaimEvidence
{
    claimType
    subject
    issuer
    status
    establishedAt
    optionalExpiresAt
    evidenceReference
}
```

The exact persistence representation is downstream.

---

## 17. No generic merchant-verification boolean

This amendment reinforces MS-PROT-028 v1.2.

Forbidden authority model:

```text
merchantVerified = true / false
```

when that field is expected to answer multiple unrelated trust questions.

A merchant may simultaneously be:

```text
authenticated for account control
payment provider onboarding incomplete
Google profile verified
professional-registration claim not applicable
```

No single boolean can express this safely.

---

## 18. Customer-facing behaviour

Customers should not be exposed to internal trust architecture unnecessarily.

Example:

If payment execution is unavailable because provider onboarding is incomplete, the customer-facing storefront may say:

```text
Online payment is not currently available.
Contact the business to complete your order.
```

where that alternative is semantically supported.

It should not expose:

```text
KYC verification pending
Provider claim invalid
Trust requirement PAYMENT_004 unsatisfied
```

unless a legal/product obligation requires such disclosure.

Presentation remains separate from machine reason.

---

## 19. Merchant-facing behaviour

The merchant should receive an actionable explanation scoped to the affected feature.

Example:

```text
Online checkout is unavailable.
Complete payment-provider setup to enable customer payments.
```

Not:

```text
Your business is unverified.
```

unless the precise accepted claim actually concerns a business-wide verification obligation.

This distinction reduces ambiguity and prevents Main Street from implying endorsement or rejection beyond its evidence.

---

## 20. Staff and delegated actors

A trust requirement associated with one actor shall not automatically constrain all merchant actors.

Example:

```text
Staff member lacks strong reauthentication required for refund
        ↓
refund operation unavailable to that staff actor
```

This does not necessarily make refund unavailable to an authorised controller who satisfies the requirement.

Therefore affected scope may include actor context.

---

## 21. Historical commitments

A newly unsatisfied trust requirement shall not automatically erase or reinterpret existing commitments.

Example:

```text
Payment provider connection becomes restricted
        ↓
existing confirmed Order remains an Order
existing Booking remains a Booking
```

Any required follow-up, cancellation, refund, fulfilment or remediation must occur through the owning capability/process semantics.

Trust degradation is not retroactive semantic deletion.

---

## 22. Residual obligations

A restricted capability may still require merchant access to historical or residual management surfaces.

Example:

```text
regulated service can no longer accept new customers
        +
existing appointments remain
```

Then:

```text
new regulated-service action
    → unavailable

residual appointment-management surface
    → remains available where lawful/required
```

MS-PROT-040 and MS-PROT-049 remain authoritative for residual obligations and surfaces.

---

## 23. Cross-domain falsification

### 23.1 Information publisher

```text
Publication + Enquiry
No special trust requirement
```

Result:

```text
ordinary public operation proceeds with authenticated merchant control
```

**PASS**

### 23.2 Retailer with incomplete payment onboarding

```text
Catalogue + Enquiry + Payment
Payment trust unsatisfied
```

Result:

```text
Catalogue = available
Enquiry = available
Checkout = unavailable
```

**PASS**

### 23.3 Consultant with optional Google integration

```text
Scheduling + Enquiry
Google profile unverified
```

Result:

```text
Scheduling = available
Enquiry = available
Google-profile integration = unavailable
```

**PASS**

### 23.4 Regulated professional

```text
General Publication
+ RegulatedService
```

Professional registration expires.

Result:

```text
RegulatedService new operations = unavailable
General Publication = available unless the governing regulation requires broader restriction
```

**PASS**

### 23.5 Account takeover

Merchant-control authority cannot be trusted.

Result:

```text
privileged merchant operations may be suspended merchant-wide
```

because the underlying security problem is merchant-wide.

**PASS**

### 23.6 Existing commitments after trust degradation

Trust requirement for new operation becomes unsatisfied while prior commitments exist.

Result:

```text
new action constrained
historical commitments retained
residual management preserved where required
```

**PASS**

---

## 24. Rejected alternatives

### A. Universal publication gate

Rejected because one narrow trust dependency could disable unrelated valid merchant operation.

### B. Generic merchantVerified flag

Rejected because it conflates unrelated claims, authorities and scopes.

### C. Capability removal when trust is unsatisfied

Rejected because contextual trust state is not capability semantics or configuration selection.

### D. Trust subsystem owns customer-facing surfaces

Rejected because trust state may constrain contextual eligibility but does not own business semantics or presentation composition.

### E. Always keep website live regardless of trust failure

Rejected because some merchant-wide legal/security obligations genuinely justify broader restriction.

---

## 25. Accepted invariants

1. Trust requirement scope determines restriction scope.
2. Missing trust evidence shall constrain the narrowest safe and lawful affected scope.
3. A missing operation-specific trust claim shall not disable unrelated operations.
4. A missing capability-specific trust claim shall not disable unrelated capabilities.
5. Provider/fulfilment trust requirements constrain the affected fulfilment path rather than redefining merchant legitimacy.
6. External ecosystem verification normally constrains only that integration.
7. Public website publication is not a universal trust-aggregation gate.
8. Merchant-wide restriction requires merchant-wide justification.
9. Account-control compromise may legitimately create merchant-wide privileged-operation restriction.
10. Unsatisfied trust state does not normally deactivate capability configuration.
11. Mutable trust satisfaction is contextual state unless configuration itself changes.
12. `UNKNOWN` trust state shall not be treated as satisfied where the requirement is mandatory.
13. Trust claims retain specific type, subject, issuer and provenance.
14. Generic `merchantVerified` or `controllerVerified` booleans are not sufficient trust authority.
15. Customer-facing presentation shall not expose unnecessary verification internals.
16. Merchant-facing messaging shall identify the affected feature/requirement rather than imply broader rejection.
17. Actor-specific trust requirements may constrain one actor without constraining all actors.
18. Trust degradation does not retroactively erase existing business commitments.
19. Residual management access may survive restriction where existing obligations require it.
20. Trust subsystems constrain eligibility; they do not acquire semantic ownership of capabilities or surfaces.

---

## 26. Relationship to MS-PROT-022 v1.5

The static/dynamic boundary is:

```text
Resolved Configuration Package
    knows:
        applicable trust requirement contracts

Contextual Resolution
    knows:
        current trust-claim satisfaction
        current actor context
        current provider/integration state
```

Therefore:

> **A changing trust-claim status shall not cause recompilation merely because the claim changes, unless the authoritative merchant configuration or registered semantic contract itself changed.**

---

## 27. Relationship to MS-PROT-048

Where a trust requirement exists because an external provider requires onboarding, authorisation or compliance evidence:

```text
provider trust state
        ≠
Main Street merchant legitimacy
```

The effect is evaluated through the applicable fulfilment role and affected operation.

Provider degradation/restriction shall follow MS-PROT-048's authority and failure contracts.

---

## 28. Relationship to MS-PROT-049

A trust requirement may influence whether an ACTION or PUBLIC_INTERACTION contribution is contextually available.

It shall not redefine the contribution's owner or create a trust-owned screen taxonomy.

Example:

```text
Payment ACTION contribution
    configured and statically applicable
        +
PAYMENT_ACCOUNT_READY unsatisfied
        ↓
contextually unavailable
```

The static contribution remains part of the resolved catalogue where otherwise applicable.

---

## 29. Continuous-improvement checkpoint

This amendment was produced because the v1.2 trust model still risked one broad implementation shortcut:

```text
all trust claims → one publish gate
```

The improved model is:

```text
trust requirement
        ↓
explicit affected scope
        ↓
minimal justified restriction
```

Cross-domain falsification does not reveal a need for a broader universal trust gate.

The amendment therefore closes the identified scope ambiguity without introducing a generic policy engine, verification marketplace, or business-admission authority.

---

## Governance verdict

```text
PROPOSE                    ✓
CONTINUOUS-IMPROVEMENT     ✓
MANUAL APPROVAL             ✓
CROSS-DOMAIN FALSIFICATION ✓
ACCEPT                      ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street does not aggregate conditional trust claims into a universal merchant-verification or publication gate. Each trust requirement constrains the smallest semantic, operational, fulfilment, integration, actor or surface scope that actually depends on it. Merchant-wide restriction is permitted only when the underlying obligation genuinely applies to merchant-wide account control, law, security or platform safety.**
