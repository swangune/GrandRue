# MS-PROT-028 v1.2 — Merchant Control, Trust Claims & Conditional Verification Amendment

**Document ID:** MS-PROT-028  
**Version:** 1.2  
**Status:** **ACCEPTED after trust-boundary review and manual approval**  
**Amends:** MS-PROT-028 v1.1 — Identity, Actor, Customer & Access Boundary Model  
**Depends on:** MS-PROT-021, MS-PROT-022 v1.5, MS-PROT-023, MS-PROT-028 v1.1, MS-PROT-031, MS-PROT-038, MS-PROT-040, MS-PROT-043 v1.2, MS-PROT-048 v1.1  
**Purpose:** Remove universal real-world identity verification of merchant controllers as a Main Street platform prerequisite, distinguish authentication and merchant control from identity/business verification, and define conditional trust claims that arise only from concrete security, legal, regulatory, capability or fulfilment obligations.

---

## 1. Governing amendment

MS-PROT-028 v1.1 remains accepted except where this amendment changes the default merchant-controller verification obligation.

The governing decision is:

> **Main Street requires authenticated and authorised control of every merchant account. Real-world identity verification, business verification and external trust claims are required only when a concrete platform-security, legal, regulatory, capability or fulfilment obligation justifies that specific claim.**

Canonical separation:

```text
ACCOUNT AUTHENTICATION
    "Can this principal prove control of accepted credentials?"
        ≠
MERCHANT CONTROL RELATIONSHIP
    "Is this principal authorised to control this merchant?"
        ≠
REAL-WORLD IDENTITY VERIFICATION
    "Has an authorised verifier established the person's real-world identity?"
        ≠
BUSINESS VERIFICATION
    "Has an authorised verifier established a defined claim about the business?"
```

Hard rule:

> **No merchant shall be required to prove a real-world personal identity merely because they create, configure or publicly operate an ordinary Main Street merchant account.**

---

## 2. Why the previous default is superseded

MS-PROT-028 v1.1 stated that Main Street's default trust obligation was to establish that a real individual controls the merchant account.

That default is superseded.

It was broader than Main Street's actual role.

Main Street is operating infrastructure, not a marketplace, business-licensing authority or general-purpose merchant gatekeeper.

Representative falsifier:

```text
Information publisher
    ↓
publishes scholarship information
receives enquiries
manages subscribers
```

Ordinary operation of this merchant does not inherently create a requirement for Main Street to collect or verify:

```text
government identity document
date of birth
legal personal name
biometric identity evidence
residential address
```

Likewise, a consultant using:

```text
website
Enquiry
Scheduling
external meeting link
```

does not inherently require Main Street to establish their government identity merely because they use Main Street infrastructure.

Therefore the platform shall not create a universal identity-proofing bottleneck without an obligation that requires it.

---

## 3. Universal merchant-account requirements

Every merchant account shall have, at minimum:

```text
authenticated principal
        +
merchant-scoped control relationship
        +
authorisation for attempted operation
        +
security/audit context appropriate to the operation
```

Conceptually:

```text
Identity / Credential Subject
        ↓ authentication
Principal
        ↓ merchant relationship
Merchant Controller
        ↓ authorisation
Permitted merchant operation
```

This proves platform account control.

It does not automatically prove real-world identity.

---

## 4. Authentication does not mean identity verification

Authentication may use supported mechanisms such as:

```text
password
passkey
one-time code
federated login
hardware-backed credential
multi-factor authentication
```

Authentication establishes continuity/control of an accepted credential or authentication factor.

It shall not be described internally or externally as proof that Main Street independently established the person's civil identity unless a separate identity-verification claim exists.

Therefore:

```text
AUTHENTICATED
    ≠
IDENTITY_VERIFIED
```

Hard rule:

> **Credential assurance and real-world identity assurance are separate trust properties.**

---

## 5. Merchant control does not mean legal ownership

A Merchant Controller is an authenticated principal with authority to control the merchant account within Main Street.

This does not itself assert:

```text
legal ownership of a company
directorship
beneficial ownership
employment status
professional registration
physical-premises ownership
commercial legitimacy
```

Therefore:

```text
principal controls Merchant M123 in Main Street
```

must not silently become:

```text
principal legally owns the underlying real-world business
```

where Main Street has not established that claim.

---

## 6. No universal `VERIFIED_MERCHANT` state

Main Street shall not collapse materially different trust facts into a generic boolean such as:

```text
merchantVerified = true
```

or:

```text
controllerVerified = true
```

without qualifying the claim.

Those states become ambiguous because they do not answer:

```text
verified what?
verified by whom?
for which subject?
using which authority/evidence?
for which purpose?
valid until when?
```

Instead Main Street shall model trust as specific claims where a claim is required.

---

## 7. Trust Claim model

A Trust Claim represents one specific assertion whose evidence and authority are relevant to a Main Street operation, capability, integration or risk boundary.

Conceptually:

```text
TrustClaim
{
    claimIdentity
    claimType
    subjectReference
    issuerAuthority
    evidenceReference / provenance
    status
    establishedAt
    optionalExpiryOrReviewRequirement
    applicablePurposeOrScope
}
```

This structure is conceptual. The amendment does not mandate one Java class or database table.

Examples:

```text
CONTROLLER_IDENTITY_VERIFIED
    issuer = supported identity-verification authority

PAYMENT_ACCOUNT_ELIGIBLE
    issuer = payment provider

GOOGLE_BUSINESS_PROFILE_VERIFIED
    issuer = Google

PROFESSIONAL_REGISTRATION_CONFIRMED
    issuer = applicable regulator
```

Claims retain the authority that actually established them.

---

## 8. Trust requirements are obligation-driven

The correct question is:

> **What specific trust claim, if any, is necessary to perform this operation safely and lawfully?**

Canonical process:

```text
Selected capability / operation / integration
        +
platform-security requirement
        +
legal/regulatory requirement
        +
fulfilment-provider requirement
        ↓
Applicable trust requirements
        ↓
Required claims already satisfied?
    ├── YES → continue
    └── NO  → obtain the specific required claim
```

Rejected process:

```text
merchant exists
    ↓
verify person
    ↓
verify business
    ↓
verify premises
    ↓
allow operation
```

---

## 9. Publication does not inherently require identity proofing

Public website activation shall not inherently depend on a universal real-world controller-identity claim.

The minimum platform rule is:

```text
valid merchant account
        +
authenticated authorised controller
        +
valid active configuration
        +
all trust requirements actually applicable to selected operations
        ↓
public activation eligible
```

Example:

```text
Publication + Enquiry merchant

Applicable trust requirements:
    authenticated merchant control

Result:
    identity proofing not inherently required
```

A different merchant may have additional claims only because its selected operations or integrations genuinely require them.

---

## 10. Payments do not create automatic duplicate Main Street verification

Where payment execution is delegated to an external provider, the provider may own regulated onboarding/KYC/KYB or payment-account eligibility requirements.

Main Street shall not automatically duplicate the provider's verification process merely because payment is enabled.

Required pattern:

```text
Payment capability/fulfilment applicable
        ↓
Payment provider establishes provider-owned eligibility facts
        ↓
authenticated/correlated provider evidence
        ↓
Main Street records only the trust consequence required for its operation
```

A provider's KYC/KYB process does not automatically become a generic Main Street business-verification claim.

MS-PROT-048 remains authoritative for provider ownership and external facts.

---

## 11. Google verification remains Google's claim

Where a merchant elects to use Google Business Profile or another Google-owned local-discovery feature:

```text
Google-owned profile/verification fact
        ↓
Google authority
```

Main Street may consume authenticated evidence where supported.

Main Street shall not require universal duplicate:

```text
GPS verification
walk-in video
premises evidence
business-address validation
```

merely because the merchant operates a physical location.

Google verification is optional context unless a selected Google integration specifically requires it.

---

## 12. Professional and regulated claims are specific claims

If a concrete Main Street capability or legally supported operation requires evidence that a merchant/person possesses a regulated status, the requirement shall identify the exact claim and appropriate authority.

Example:

```text
Operation requires professional registration
        ↓
PROFESSIONAL_REGISTRATION_CONFIRMED
        ↓
issuer = relevant authoritative register/regulator
```

This does not imply that every merchant must undergo professional/business verification.

The requirement must remain capability/operation/jurisdiction specific.

---

## 13. Staff identity boundary

Staff require authenticated, merchant-scoped access and explicit authority.

They do not inherit a universal real-world identity-proofing requirement merely because the merchant controller may satisfy one for some separate obligation.

Default:

```text
Staff principal
    ↓
authentication
    ↓
merchant-established staff relationship
    ↓
scoped privilege/authority
```

A stronger identity or trust requirement may be applied only where a concrete operation, security risk or regulatory obligation justifies it.

Hard rule:

> **Staff access assurance shall be proportional to the authority being exercised, not to a universal government-ID requirement.**

---

## 14. Risk-based verification is escalation, not admission gating

Main Street may require additional trust evidence where credible elevated security or abuse risk arises.

Examples may include:

```text
suspected account takeover
high-impact ownership-transfer request
credible fraud indicators
sensitive privilege escalation
repeated recovery anomalies
legally required investigation
```

This is a security/risk response.

It must not silently become a universal onboarding rule.

Conceptually:

```text
normal authenticated merchant operation
        ↓
credible elevated risk condition
        ↓
additional assurance requirement
        ↓
proportionate verification / restriction / recovery flow
```

---

## 15. Abuse prevention does not require universal identity proofing

Main Street may use non-identity controls such as:

```text
email/credential assurance
rate limiting
session/device security
abuse reporting
content/moderation controls where applicable
account reputation
provider evidence
security monitoring
suspension/restriction mechanisms
audit trails
```

These controls may often address platform abuse more proportionately than collecting identity documents from every merchant.

Identity verification remains available only where justified.

---

## 16. Account recovery is distinct from identity proofing

Secure account recovery does not require Main Street to identity-proof every controller during onboarding.

Recovery mechanisms may include, where accepted by the security design:

```text
passkey recovery
verified email
multi-factor recovery
recovery codes
trusted-device/session evidence
administrative exceptional recovery
```

Therefore:

```text
secure account recovery
    ≠
universal onboarding identity verification
```

Where normal recovery evidence is insufficient for a high-risk case, Main Street may request additional assurance as an escalation.

---

## 17. Merchant-control recovery and credential recovery are different

The following shall remain distinct:

```text
credential recovery
        ≠
principal identity recovery
        ≠
merchant-control recovery
        ≠
merchant-control transfer
```

Recovering access to an email/password credential must not automatically authorise a transfer of merchant control where a stronger trust boundary applies.

Exact recovery implementation is deferred, but the semantic distinction is mandatory.

---

## 18. Merchant-control transfer

A normal merchant-control transfer should require strong authentication and explicit transfer semantics.

Conceptually:

```text
current authorised controller
        +
strong authentication / current authority
        +
receiving authenticated principal
        +
explicit transfer acceptance
        +
audit provenance
        ↓
merchant-control relationship changes
```

Real-world identity verification of the receiving controller is required only if a separate applicable trust obligation demands it or if the transfer enters an exceptional/high-risk recovery path.

This avoids making ordinary transfer depend on identity proofing while preserving room for stronger assurance when justified.

---

## 19. Trust claim lifecycle

A trust claim may have a lifecycle different from the merchant account or capability configuration.

Possible conceptual states include:

```text
UNSATISFIED
PENDING
SATISFIED
EXPIRED
REVOKED
UNAVAILABLE
```

The exact vocabulary may vary by claim type.

A claim's lifecycle shall not silently mutate the merchant's capability graph unless accepted semantics explicitly define that consequence.

Example:

```text
payment-provider eligibility revoked
        ↓
payment fulfilment unavailable/degraded
        ≠
merchant account deleted
```

---

## 20. Trust claim provenance

Where Main Street relies on a trust claim, it must retain enough provenance to answer:

```text
what claim was relied on?
which subject did it concern?
which authority established it?
what evidence/reference was used?
when was it established?
was it still valid for this operation?
```

Historical activity must not be retrospectively rewritten merely because a later claim expires or is revoked.

---

## 21. Verification UI must name the actual requirement

Merchant-facing language shall avoid generic statements such as:

```text
Verify your business
Become a verified merchant
Main Street approved
```

unless Main Street has explicitly defined and substantiated that exact claim.

Preferred language identifies the requirement:

```text
Confirm your payment account with [provider]
Connect your verified Google Business Profile
Confirm the professional registration required for this service
Additional account security verification required
```

This avoids implying endorsement beyond the actual evidence.

---

## 22. No universal public verification badge

Main Street shall not create a general public `Verified by Main Street` badge from authentication, account age or one unrelated trust claim.

A public trust indicator may exist only where:

1. the underlying claim is explicitly defined;
2. Main Street is authorised to expose it;
3. the source/meaning is not misleading;
4. current validity can be established; and
5. its presentation does not imply broader endorsement than the claim supports.

---

## 23. Relationship to onboarding inference

Inference may detect evidence that suggests an external trust requirement may later apply.

Example:

```text
merchant says they accept card payments
        ↓
inference may propose Payment capability
        ↓
Payment fulfilment may later require provider onboarding
```

Inference shall not:

```text
identity-verify the merchant
invent verification requirements
infer a business is trustworthy
silently gate merchant eligibility
```

Applicable trust requirements must come from registered platform/capability/provider/legal contracts.

---

## 24. Relationship to configuration compilation

Trust claims are not semantic capability configuration values merely because they affect whether an operation can currently proceed.

A changing verification/provider claim should normally be treated as contextual/operational authority or fulfilment state according to the owning contract.

Therefore:

```text
Trust claim state
    ≠
capability activation
    ≠
merchant-authored semantic configuration
```

Where a stable configuration explicitly selects an operation that has an external trust prerequisite, the resolved configuration may retain the requirement, while current satisfaction is evaluated contextually.

This aligns with MS-PROT-022 v1.5.

---

## 25. Legal/regulatory exceptions must be explicit

This amendment does not claim that identity/business verification is never required.

It establishes that such requirements must have a concrete authority and scope.

A future requirement must identify at least:

```text
triggering capability/operation/integration
applicable jurisdiction or policy source where relevant
specific trust claim required
subject of the claim
authoritative issuer/evidence source
failure/recovery consequence
reverification/expiry behaviour where applicable
```

Generic statements such as:

```text
"for compliance"
```

are insufficient design justification on their own.

---

## 26. Cross-domain falsification

### Information publisher

```text
Publication + Enquiry
```

Requires authenticated merchant control.

No inherent real-world identity verification.

**PASS**

### Online consultant

```text
Publication + Enquiry + Scheduling + Appointment
```

No inherent premises or identity-proofing requirement.

External meeting/calendar providers remain optional integrations.

**PASS**

### Retailer with external payment provider

```text
Catalogue + Order + Payment
```

Payment provider may establish provider-owned onboarding/eligibility claims.

Main Street does not automatically duplicate KYC/KYB.

**PASS**

### Physical local merchant using Google Business Profile

Google owns Google Business Profile verification.

Main Street may consume the resulting external claim where useful.

No universal GPS/video duplication.

**PASS**

### Regulated professional operation

Specific regulator claim may be required if accepted product/legal semantics demand it.

Requirement is scoped rather than universal.

**PASS**

### Staff member operating POS

Authenticated staff + merchant relationship + privilege.

No universal government-ID proofing.

**PASS**

---

## 27. Rejected alternatives

### A. Verify every merchant controller before publication

Rejected because ordinary infrastructure use does not inherently create a real-world identity-verification obligation and the rule creates unnecessary admission friction.

### B. Keep one `merchantVerified` boolean

Rejected because materially different claims, authorities, evidence and lifecycles cannot be represented safely by one state.

### C. Treat payment-provider KYC as Main Street business verification

Rejected because provider-owned regulated facts retain provider authority and do not prove unrelated merchant claims.

### D. Treat Google verification as Main Street verification

Rejected because Google owns Google Business Profile verification and its scope.

### E. Never perform identity verification under any circumstance

Rejected because specific security, recovery, legal, regulatory or capability obligations may justify stronger assurance.

### F. Identity-proof all staff because they operate merchant systems

Rejected because assurance must be proportional to actual authority/risk.

---

## 28. Accepted invariants

1. Authentication and real-world identity verification are distinct.
2. Merchant control and legal business ownership are distinct.
3. Every merchant account requires authenticated, authorised control.
4. Real-world controller identity verification is not a universal Main Street prerequisite.
5. Business verification is not a universal Main Street prerequisite.
6. Public activation requires only the trust claims actually applicable to the merchant's selected operations/integrations.
7. Main Street shall not use an ambiguous universal `merchantVerified` state.
8. Trust claims must identify their subject, type and authoritative issuer/provenance.
9. External trust claims retain external authority.
10. Payment-provider verification is not automatically duplicated by Main Street.
11. Google Business Profile verification remains Google's claim.
12. Professional/regulatory claims are scoped to concrete requirements.
13. Staff do not inherit universal identity-proofing requirements.
14. Risk-based additional verification is an escalation mechanism, not a default admission gate.
15. Account recovery does not justify universal onboarding identity verification.
16. Credential recovery, merchant-control recovery and merchant-control transfer remain distinct.
17. Trust-claim state does not itself activate/deactivate capabilities unless explicit semantics define a consequence.
18. Verification UI must state the actual claim being established and avoid misleading endorsement language.
19. No general public `Verified by Main Street` badge is created from unrelated or narrow claims.
20. Legal/regulatory exceptions require explicit authority, scope, claim and consequence.

---

## 29. Deferred decisions

This amendment intentionally does not yet select:

```text
authentication provider
passkey/password implementation
MFA policy thresholds
risk-scoring implementation
identity-verification vendor for exceptional cases
exact recovery UX
exact merchant-control transfer UX
trust-claim persistence schema
jurisdiction-specific regulatory catalogue
public trust-indicator catalogue
```

Those decisions may be designed later without reintroducing universal identity/business verification.

---

## 30. Legacy verification documents

The following earlier draft materials contain assumptions superseded by MS-PROT-028 v1.2 where they conflict:

```text
docs/platform-services/verification-engine.md
docs/workflows/merchant-onboarding/business-verification.md
```

In particular, the following are no longer authoritative Main Street requirements:

```text
brick-and-mortar-only merchant eligibility
universal business legitimacy verification
universal physical-premises verification
universal GPS validation
universal walk-in video verification
universal human business review
universal Main Street verification before publication
```

The documents remain useful historical product-design evidence only.

---

## Governance verdict

**ACCEPTED.** The previous assumption that every merchant-controller must have a real-world identity verified by Main Street is removed.

### Canonical decision

> **Main Street requires authenticated and authorised merchant control, not universal identity proofing. Identity verification, business verification and other trust evidence are specific claims required only when a concrete security, legal, regulatory, capability or fulfilment obligation makes that claim necessary. Trust requirements shall remain narrowly scoped, attributable to the authority that established them, and shall never silently turn Main Street into a general merchant-admission or endorsement authority.**
