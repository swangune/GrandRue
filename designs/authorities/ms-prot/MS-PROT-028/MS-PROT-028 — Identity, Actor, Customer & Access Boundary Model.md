# MS-PROT-028 — Identity, Actor, Customer & Access Boundary Model

**Version:** 1.1  
**Status:** **Accepted**  
**Depends on:** MS-PROT-020 → MS-PROT-027  
**Purpose:** Define how Main Street identifies participants, represents actors, grants authority, supports guest/persistent customer relationships, and distinguishes account-controller identity verification from business verification.

## 1. Governing principle

> **Identity establishes who or what is interacting; authority determines what that identity may do; customer relationship determines what business information may be associated with that customer; verification claims must state exactly what has been verified.**

These concerns must not collapse into one object.

```text
Identity
   ↓
Actor / Principal
   ↓
Authority in context

Merchant controller identity
   ≠
Merchant business
   ≠
Business verification claim
```

## 2. Core concepts

### Identity

Represents an identifiable human, system or integration identity where identification is required.

### Actor / execution principal

The identity/origin participating in or initiating an operation in a specific context.

Possible principals include:

```text
merchant controller
staff
customer
guest/anonymous interaction
system
integration
scheduled process
AI-assisted process under delegated authority
```

### Merchant

The merchant is the operating/tenant boundary, not the same object as the person who controls it.

### Merchant controller identity

A verified/authenticated human identity authorised to control the merchant account.

### Customer relationship

Merchant-scoped association between a customer/context and merchant business activity. It does not automatically imply a global Main Street account.

## 3. Identity is not authority

Authentication answers:

> Who/what is this?

Authorisation answers:

> May this principal perform this operation against this target in this context?

A logged-in customer cannot automatically cancel another customer's booking. A valid staff login does not automatically grant refund authority.

Authority may depend on:

```text
principal
merchant scope
operation
target/execution scope
relationships
role/privilege
effective policy
```

## 4. Business participant is not execution principal

A client, guest, recipient or customer may be part of a business record without authenticating as the actor who executes an operation.

Example:

```text
Solicitor staff confirms appointment
Client receives email/ICS
```

The client is a participant/contact; staff is the execution principal.

## 5. Guest and anonymous interaction

Main Street must support operations explicitly designed for anonymous/guest initiation, such as:

```text
submit enquiry
request contact
browse publication
request viewing
begin booking
```

Anonymous interaction must not be converted into a fake persistent customer account.

Technical/request context may still be retained for security, anti-abuse, correlation and audit where lawful/appropriate.

## 6. Customer identity and merchant-scoped relationship

Customer information may be reused where appropriate, but Main Street should avoid assuming one universal customer profile contains every niche data field.

Contextual business information—vehicle, project, legal matter, delivery address, consultation topic, etc.—remains governed by the relevant semantic/data requirements.

Persistent customer accounts/portals are optional product capabilities.

## 7. Merchant controller verification boundary

Main Street's default trust obligation is to establish that a real individual controls the merchant account.

Conceptually:

```text
Person
   ↓ identity verification/authentication
Verified merchant-controller identity
   ↓ authorised relationship
Merchant account
```

Purposes include:

- account integrity;
- impersonation resistance;
- accountability;
- recovery/security.

The identity-verification provider/mechanism and completion timing are deliberately not selected here.

## 8. Controller verification is not business verification

This is a hard semantic distinction:

```text
Controller identity verified
        ≠
Business verified by Main Street
```

Unless Main Street deliberately introduces and substantiates a separate verification capability/claim, controller verification does **not** assert that Main Street has verified:

```text
business legitimacy
physical premises
professional status/licence
merchant commercial claims
merchant-provided content
Google/local listing eligibility
```

Main Street must not present controller identity verification using language that implies broader business certification.

## 9. Main Street is not a default business-admission authority

Main Street provides operating infrastructure and normally validates configurations, account control and runtime safety—not whether a business "deserves" to operate.

Additional verification may be required only where a concrete legal, regulatory, risk, payment, platform or capability requirement creates that responsibility.

No universal business-verification gate is accepted.

## 10. Physical premises and external verification

Physical premises are optional merchant context, not an identity/access prerequisite.

If a merchant wants Google's local-discovery benefits, Google Business Profile and Google's verification rules remain Google's responsibility.

```text
Merchant wants Google/local discoverability
        ↓
Google Business Profile
        ↓
Google verification/claims
```

Main Street should not duplicate universal GPS, walk-in video or premises-evidence verification merely because a merchant has a physical location.

External trust claims must retain their source/authority. Examples may include Google, payment providers or professional regulators.

## 11. Staff identity and access

Staff authority is merchant-scoped and must not leak between employers/merchants.

A staff identity/access mechanism may be lightweight, but runtime authority still derives from explicit merchant relationships/permissions rather than possession of an identifier alone.

The exact staff registration/login product design remains downstream.

## 12. System, integration and AI principals

`SYSTEM` or `INTEGRATION` is not synonymous with unrestricted authority.

Background processes, integrations and AI-assisted actions must operate within registered/delegated authority boundaries.

AI may recommend or initiate permitted actions under delegated authority but cannot become independent semantic authority or bypass runtime checks.

## 13. Trusted execution context

Authoritative execution metadata such as merchant scope, authenticated principal and execution time must be established by trusted application boundaries rather than blindly accepted from client input.

Possession of a resource identifier does not establish access to that resource.

## 14. Falsification findings

Rejected assumptions:

| Failed assumption | Why it fails |
|---|---|
| Identity and Actor are the same concept | One identity may act in different contexts/roles |
| Authentication implies authorisation | Target/relationship/policy constraints still matter |
| Customer relationship requires a global account | Guest and contextual interactions are legitimate |
| Merchant and owner/controller are the same object | Tenant/business and human identity have different lifecycles |
| Controller identity verification means business verified | Claims have different evidence and liability |
| Physical premises must be Main Street verified | Online/publisher merchants disprove the requirement and external platforms own their claims |
| SYSTEM is omnipotent | Background authority must remain bounded |
| AI may inherit merchant authority generally | Creates uncontrolled authority leakage |

## 15. Accepted invariants

1. Identity, Actor/Principal, authority and customer relationship remain distinct.
2. Authentication does not imply authorisation.
3. Business participants need not be execution principals.
4. Guest/anonymous operations are allowed only where semantics permit them and do not require fake accounts.
5. Merchant is the tenant/operating boundary; controller identity is separate.
6. Main Street's default verification claim concerns merchant-account controller identity/control.
7. Controller identity verification does not imply business, premises, professional or commercial-claim verification.
8. No universal Main Street business-verification gate is accepted.
9. Physical premises are optional context, not eligibility.
10. External verification claims retain external ownership/attribution.
11. System/integration/AI principals remain explicitly authorised, not omnipotent.
12. Trusted execution context must establish merchant/principal scope.

## 16. Deferred decisions

Identity-verification vendor, verification timing, staff-login UX, account recovery implementation, regulator integrations, Google Business Profile integration and legal/regulatory exceptional verification requirements remain open.

## Governance verdict

**ACCEPTED.** The 20 August 2026 handover materially clarifies the trust boundary while preserving the existing identity/actor/access architecture.
