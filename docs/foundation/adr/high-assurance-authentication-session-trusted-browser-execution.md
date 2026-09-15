# ADR-014 — High-Assurance Authentication, Session & Trusted Browser Execution Architecture

> **ADR ID:** ADR-014  
> **Version:** 1.0  
> **Status:** Accepted  
> **Date:** 26 August 2026  
> **Owner:** Architecture Team  
> **Approved:** Manual approval on 26 August 2026 after complete hardened authority presentation  
> **Authority type:** Production / security implementation architecture  
> **Governed by:** `DESIGN-RULES.md` v2.1 and `DOCUMENT-GOVERNANCE.md`  
> **Depends on:** MS-PROT-028; MS-PROT-030; MS-PROT-031; MS-PROT-062; MS-PROT-063 v1.0 + v1.1; MS-PROT-067; MS-PROT-071; MS-PROT-074; MS-PROT-076  
> **Amends / Supersedes:** None  
> **Closes:** MS-PROT-079 Target 2 — Authentication/session establishment  
> **Purpose:** Establish Main Street's initial high-assurance production authentication and session architecture for Merchant Controllers, staff, customers and browser-based access while preserving immediate revocation, phishing resistance, merchant isolation, current relationship/device authority and strong protection against session theft, CSRF and client-side authority leakage.

---

## 1. Governing Decision

Main Street SHALL use a **high-assurance, server-authoritative authentication architecture** built around:

```text
phishing-resistant human authentication
        ↓
server-authoritative opaque session
        ↓
host-bound protected browser cookie
        ↓
current authoritative session resolution
        ↓
current Merchant Scope resolution
        ↓
current relationship / device context
        ↓
step-up authentication where required
        ↓
MS-PROT-062 runtime authority composition
        ↓
capability-owned execution
```

For human browser sessions:

```text
opaque session credential
+
server-side Session Record
```

is the governing model.

Main Street MUST NOT use self-contained browser tokens containing mutable merchant roles, privileges, entitlement, device-authorisation state or other business authority as its primary authentication authority.

---

## 2. Security Posture

The initial Main Street authentication architecture SHALL target a security posture closer to a financial/enterprise application than to a conventional small-business website.

The objective is:

```text
strong security
+
low-friction normal operation
+
fast revocation
+
merchant isolation
+
staff walk-away protection
+
phishing resistance
```

Security MUST NOT depend on users understanding Main Street's internal capability or authority model.

---

## 3. Explicit Non-Goals

ADR-014 does not redefine Identity, Merchant Scope, Merchant Membership, Merchant Controller, CustomerContext, Role Assignment, Merchant Operational Device Authorisation, Actor Authorisation, Commercial Entitlement, Operational Eligibility or capability-owned business state.

It does not define frontend screen design, recovery UI, exact passkey vendor, exact hardware-security-key vendor, exact cryptographic libraries, exact database schema, exact reverse-proxy product or production implementation.

It also does not require browser sessions for scheduled, system or integration principals.

---

## 4. Canonical Security Separations

The following distinctions are mandatory:

```text
Identity
    ≠ Session

Session
    ≠ Merchant Scope

Merchant Scope
    ≠ Merchant relationship

Authentication
    ≠ Actor Authorisation

Device Authorisation
    ≠ staff authentication

staff authentication
    ≠ capability privilege

Session Credential
    ≠ Session Identity

Session Credential
    ≠ business authority
```

A browser cookie proves possession of a session credential.

It does not prove that an operation is authorised.

---

## 5. Merchant Controller Authentication

Merchant Controller authentication SHALL prefer **WebAuthn/passkeys as the primary authentication mechanism**.

Canonical path:

```text
Controller
        ↓
WebAuthn challenge
        ↓
registered passkey/authenticator
        ↓
cryptographic proof
        ↓
Identity authenticated
        ↓
server-side session created
```

Passwords MAY exist for bounded compatibility or recovery purposes but MUST NOT be treated as the preferred high-assurance authentication mechanism.

SMS MUST NOT be Main Street's strongest authentication factor.

---

## 6. Higher-Assurance Authenticators

Ordinary Merchant Controllers MAY use appropriately secured platform or synchronised passkeys where production security review accepts them.

For higher-risk contexts such as platform administration or unusually sensitive merchant-control requirements, Main Street MAY require:

```text
device-bound authenticator
or
hardware security key
```

whose private key is non-exportable to the extent supported by the selected authentication architecture.

This stronger path does not become mandatory for every ordinary small merchant merely because it provides greater theoretical assurance.

---

## 7. Server-Authoritative Opaque Sessions

After authentication, Main Street SHALL issue a cryptographically unpredictable opaque Session Credential.

The credential:

```text
contains no user profile
contains no merchant role
contains no permission list
contains no entitlement
contains no Merchant Configuration
contains no device-authorisation claim
```

Its only security purpose is to locate/prove possession of a server-authoritative Session Record.

---

## 8. Session Credential Entropy

Session credentials SHALL use a cryptographically secure random generator.

The Main Street implementation baseline SHALL use **256 bits of random entropy** unless a later security authority establishes an equivalent or stronger representation.

The credential MUST NOT be derived predictably from:

```text
Identity ID
merchant ID
email
timestamp
staff reference
session database identifier
```

---

## 9. Session Identity and Secret Separation

Main Street SHALL distinguish:

```text
Session Identity
    non-secret durable/reference identifier

Session Credential
    bearer secret
```

The Session Identity may appear in trusted audit and diagnostics.

The raw Session Credential MUST NOT.

---

## 10. Server-Side Credential Protection

Where practical, persistent session storage SHOULD retain a cryptographically suitable verifier/fingerprint of the Session Credential rather than requiring storage of the raw bearer value.

Conceptually:

```text
Browser
    raw session credential

Server
    ↓
credential verification

Persistence
    session identity
    credential verifier
    identity reference
    security metadata
```

Compromise of ordinary session persistence SHOULD NOT automatically yield immediately replayable browser bearer credentials where the selected verifier architecture can prevent that.

---

## 11. Session Record

A server-authoritative Session Record conceptually contains:

```text
sessionIdentity
identityReference
credentialVerifier
establishedAt
authenticationAssurance
authenticationMethod
absoluteExpiry
idle/security timestamps where applicable
securityGeneration/reference
revokedAt?
revocationReason?
```

The exact relational representation remains implementation detail.

A Session Record MUST NOT own merchant business authority.

---

## 12. Browser Cookie Contract

For first-party high-authority Main Street browser sessions, the session credential SHALL be carried in a hardened host-bound cookie.

The target form is conceptually:

```http
Set-Cookie:
__Host-MS-SESSION=<opaque-secret>;
Secure;
HttpOnly;
SameSite=Strict;
Path=/
```

No `Domain` attribute is permitted for the high-authority session cookie.

Where a specific legitimate browser interaction demonstrably cannot operate with `SameSite=Strict`, a narrowly justified `Lax` policy MAY be selected for that specific surface/session class.

Broad weakening of all session cookies is prohibited.

---

## 13. `__Host-` Isolation

Main Street SHALL use the host-cookie security model for privileged browser sessions wherever browser/platform support permits.

The architecture must ensure:

```text
merchant administration host
    receives merchant session

merchant storefront custom domain
    does not

unrelated Main Street subdomain
    does not merely because it shares a parent domain
```

This is a hard security requirement.

---

## 14. Custom-Domain Isolation

Merchant custom domains MUST NOT receive Merchant Controller or staff operational session cookies.

For example:

```text
merchant.mainstreet.app
    privileged merchant session

bellasalon.co.uk
    no merchant-dashboard credential

anothermerchant.co.uk
    no merchant-dashboard credential
```

A storefront custom domain therefore cannot become a privileged authentication origin merely because Main Street renders the storefront.

---

## 15. TLS

Production session credentials SHALL be transmitted only over authenticated TLS.

HTTP transport MUST NOT carry authenticated Main Street session credentials.

The production web edge SHOULD use HSTS and other appropriate TLS-hardening controls.

---

## 16. HttpOnly

The session cookie SHALL be `HttpOnly`.

Ordinary React/Next.js application JavaScript MUST NOT need to read the raw session credential.

Therefore:

```text
frontend may know:
    authenticated presentation state

frontend must not know:
    reusable session bearer secret
```

---

## 17. Session Fixation Protection

A successful authentication MUST establish a new authenticated Session Credential.

Main Street MUST NOT upgrade an attacker-influenced pre-authentication session identifier into an authenticated session.

Canonical behaviour:

```text
pre-auth context
        ↓
authentication succeeds
        ↓
old credential discarded
        ↓
new independent credential generated
```

---

## 18. Credential Rotation

Session credentials SHALL rotate after security-sensitive transitions where appropriate, including:

```text
initial authentication
step-up authentication
credential/security reset
account recovery
suspected session compromise
other accepted security-policy triggers
```

Merchant navigation or switching between valid Merchant Scopes does not by itself require session rotation.

---

## 19. CSRF Protection

Because privileged browser authentication uses cookies, state-changing requests MUST use explicit CSRF defence.

The high-assurance baseline SHALL combine:

```text
SameSite policy
+
anti-CSRF proof
+
Origin / equivalent request-origin validation where applicable
```

SameSite alone MUST NOT be treated as sufficient universal CSRF protection.

Exact Spring/security-library mechanics remain implementation detail.

---

## 20. Browser Script Security

`HttpOnly` prevents JavaScript from reading the cookie but does not make XSS harmless.

Privileged web applications SHALL therefore adopt a strong browser-content security posture including an appropriately restrictive Content Security Policy.

The implementation SHOULD avoid unsafe script execution modes where practical and MAY adopt Trusted Types or equivalent browser controls where they materially improve XSS resistance.

Session security review MUST treat XSS as an authenticated-session threat.

---

## 21. Identity Session

Normal human authentication establishes an **Identity Session**, not permanent authority over one Merchant Scope.

One Identity may participate in several merchant relationships:

```text
Identity P
   ├── Controller → Merchant A
   ├── Controller → Merchant B
   └── Staff → Merchant C
```

One valid Identity Session may support separately established execution contexts for each.

---

## 22. Merchant Scope Resolution

For every merchant-scoped use:

```text
valid Identity Session
        ↓
trusted target Merchant Scope resolution
        ↓
current applicable relationship
        ↓
Trusted Execution Context
```

Client-supplied merchant identifiers are references only until independently resolved and validated.

MS-PROT-031's trusted tenant boundary remains authoritative.

---

## 23. Mutable Authority Must Not Be Frozen Into Sessions

A Session Record MUST NOT establish durable authoritative claims such as:

```text
controllerOfMerchantA = true
role = ADMIN
permissions = [...]
membershipActive = true
deviceTrusted = true
subscription = PREMIUM
```

in a form that overrides current authoritative sources.

Convenience/presentation hints MAY be cached, but security use requires current authoritative validation consistent with applicable freshness/invalidation guarantees.

---

## 24. Controller Transfer

After Controller transfer commits, an old session may remain a technically valid Identity Session.

It MUST immediately cease to establish the former Controller relationship.

Therefore:

```text
old session
+
former Controller
        ↓
current Controller relationship check fails
        ↓
controller operation denied
```

Relevant sessions SHOULD additionally be revoked as defence in depth.

The current relationship remains the authoritative protection boundary.

---

## 25. Merchant Suspension and Closure

Merchant suspension or closure does not automatically destroy the human Identity.

If one person controls multiple merchants:

```text
Merchant A suspended/closed
        ≠
Identity loses Merchant B access
```

The affected merchant context fails its own current lifecycle/access checks.

---

## 26. Staff Security Model

Staff operational access SHALL require two independent dimensions:

```text
authorised operational device/application context
        +
individual staff authentication
```

Neither is sufficient alone.

This preserves the accepted MS-PROT-063 v1.1 / MS-PROT-074 boundary.

---

## 27. Staff Device Cryptographic Context

Main Street SHOULD use asymmetric cryptographic device/application-instance proof for Merchant Operational Device Context where supported by the deployment/client platform.

Conceptually:

```text
authorised device
        ↓
private device key retained locally
        ↓
server challenge
        ↓
device signs
        ↓
server verifies registered public key
```

The private key SHOULD use secure platform storage and hardware protection where practical.

The server stores the corresponding non-secret verification material and authorisation relationship.

---

## 28. Device Credential Separation

The architecture SHALL preserve:

```text
Device Credential
    proves authorised device/application context

Staff Credential
    authenticates individual staff member

Session Credential
    preserves authenticated continuity
```

These MUST NOT be collapsed into one reusable shared credential.

---

## 29. Staff Personal Security Context

A staff member may use personal authentication for bounded security administration such as invitation acceptance, credential management and PIN reset.

That personal context MUST NOT expose ordinary merchant operational information solely because the person is a Merchant Member.

---

## 30. Staff Operational Context

Operational staff access requires:

```text
ACTIVE Merchant Operational Device Context
+
staff authentication proof
+
current Merchant Membership
+
resolved Merchant Scope
        ↓
Staff Execution Principal
```

Role/privilege evaluation remains downstream.

---

## 31. Staff Fast Authentication

Main Street MAY support merchant-scoped Staff Reference + PIN for rapid authentication on an already authorised operational device.

The model is:

```text
authorised operational device
        +
merchant-scoped staff reference
        +
individual PIN
```

The staff reference is not secret.

The PIN MUST be protected against brute force, stored using an appropriate credential-verification mechanism and independently revocable/resettable.

A valid PIN on an unauthorised device MUST NOT establish operational access.

---

## 32. Stronger Staff Authentication

Where security requirements justify it, a staff member MAY use WebAuthn/passkey authentication instead of or in addition to a PIN.

High-risk staff roles SHOULD be capable of stronger authentication without changing the underlying Merchant Membership or Role model.

---

## 33. Device Revocation

After Merchant Operational Device Authorisation is revoked:

```text
old staff session
+
correct staff reference
+
correct PIN
```

MUST NOT independently preserve merchant operational access.

Unknown device state fails closed.

---

## 34. Merchant Controller Session Lifetime

The initial high-assurance security baseline SHALL target:

```text
Merchant Controller
    idle timeout:       15 minutes
    absolute lifetime:  12 hours
```

Security policy MAY shorten these periods.

A future approved security policy MAY lengthen them only where the resulting assurance remains acceptable and the change does not weaken an accepted operation-specific requirement.

---

## 35. Staff Operational Session Lifetime

The initial high-assurance baseline SHALL target:

```text
Staff Operational Context
    idle lock:          10 minutes
    absolute lifetime:   8 hours
```

The staff experience SHOULD favour rapid individual reauthentication after idle lock rather than requiring a full account-registration/login flow.

For environments where 10 minutes materially prevents legitimate operation, security policy MAY permit a bounded increase up to 15 minutes after explicit risk review.

---

## 36. Idle Lock vs Identity Destruction

For staff:

```text
idle timeout
        ↓
operational context locks
```

does not necessarily require deletion of all personal Identity continuity.

Quick reauthentication may re-establish the staff context only after:

```text
person proof
+
current Membership
+
current Device Authorisation
```

are validated.

---

## 37. Sensitive Controller Operations

A valid long-running Controller session is insufficient by itself for high-impact security/control operations.

Operations including:

```text
Merchant Controller transfer
Merchant Account closure
new staff-device authorisation
authentication-factor change
control-recovery change
high-risk credential/security administration
other designated privileged actions
```

SHALL require recent elevated authentication assurance.

---

## 38. Step-Up Authentication

Where existing session assurance is insufficient:

```text
valid session
        ↓
privileged operation
        ↓
recent assurance insufficient
        ↓
WebAuthn/passkey challenge
        ↓
successful proof
        ↓
rotate/elevate session security state
        ↓
revalidate current authority
        ↓
operation may continue
```

Step-up authentication MUST NOT bypass Actor Authorisation or current business/security state.

---

## 39. Freshness Window for Critical Operations

The high-assurance baseline SHOULD require strong authentication within approximately **10 minutes** for designated critical Merchant Controller operations.

The exact operation catalogue may be refined by each owning security/account authority.

An old valid session is not equivalent to fresh possession of the Controller authenticator.

---

## 40. CustomerAccount Sessions

Optional CustomerAccount authentication MAY use the same fundamental opaque server-session architecture.

Customer authentication remains distinct from CustomerContext.

A logged-in customer MUST still satisfy resource/relationship authority for the Order, Booking, Appointment or other merchant relationship being accessed.

---

## 41. Guest Access

Guest operations MUST remain possible where owning semantics permit them.

Anonymous interaction does not require creation of a fake Identity Session.

---

## 42. Contextual Access Credentials

Secure guest tracking/action links SHALL use narrowly bounded contextual access credentials.

A contextual credential SHALL be limited to:

```text
one merchant/context
+
one resource/relationship
+
defined permitted interaction
+
bounded validity
```

It MUST NOT become a global CustomerAccount or Merchant Session.

---

## 43. System, Scheduled and Integration Principals

System work, timers, scheduled processing and external integrations MUST NOT manufacture browser sessions.

They establish separately bounded trusted principals under the accepted MS-PROT-063 model.

---

## 44. Session Persistence

Authoritative human Session Records SHALL reside in durable server-controlled persistence compatible with Main Street's production relational architecture.

Therefore session continuity survives ordinary:

```text
process restart
rolling application deployment
individual instance replacement
```

without making process memory the authority.

---

## 45. Session Cache

A session cache MAY be introduced only as a performance optimisation.

```text
cache
    ≠ Session authority
```

Any cache must preserve acceptable revocation behaviour.

A design that permits revoked sessions to remain valid because of stale cache state requires explicit security justification and must not be introduced as an incidental optimisation.

---

## 46. Session Store Failure

If Main Street cannot establish authoritative session state:

```text
session cannot be verified
        ↓
privileged authenticated execution fails closed
```

The browser cookie itself is not sufficient evidence.

Unauthenticated public functionality may remain operational independently where safe.

---

## 47. Logout

Logout revokes the selected Session Record.

Once revocation commits, the corresponding credential MUST no longer establish trusted continuity.

Loss of logout acknowledgement does not reactivate the session.

---

## 48. Global Session Revocation

Main Street SHALL support security actions that invalidate:

```text
one session
or
all applicable sessions for an Identity/security event
```

depending on the governing security operation.

Examples include suspected compromise, explicit logout-all-devices and sensitive account recovery.

---

## 49. Password/Authenticator Reset

A normal authentication-factor reset affects credential/session security state.

It does not automatically alter:

```text
Merchant Account
Merchant Controller relationship
Merchant Membership
Merchant Configuration
business commitments
```

Affected sessions SHALL be invalidated where required by the selected credential-security policy.

MS-PROT-076's separation remains authoritative.

---

## 50. Recovery Security

Recovery MUST NOT be easier to abuse than normal authentication.

Merchant-control recovery is a high-authority security process and MUST NOT consist of support personnel directly rewriting controller identifiers based on weak evidence.

Successful high-assurance recovery SHOULD invalidate relevant existing:

```text
authenticators
sessions
recovery credentials
```

according to the accepted recovery/security policy.

---

## 51. Session Theft Response

Bearer-session theft cannot be made mathematically impossible while retaining normal browser sessions.

The architecture therefore uses defence in depth:

```text
TLS
Secure
HttpOnly
SameSite
host-only cookie
CSRF protection
CSP/XSS controls
high entropy
short idle window
absolute timeout
rotation
server revocation
current relationship checks
device checks
step-up authentication
```

A stolen session therefore still cannot manufacture current merchant authority that no longer exists.

---

## 52. Authentication Assurance

The Session Record SHALL preserve trusted evidence sufficient to distinguish applicable assurance classes such as:

```text
password/fallback authentication
passkey/WebAuthn authentication
recent WebAuthn step-up
staff PIN on authorised device
other approved strong authentication
```

Authentication owns the evidence.

The owning operation/security authority decides what assurance is required.

---

## 53. Risk-Based Security Signals

Main Street MAY later consider signals such as:

```text
new device/browser
major geography/network change
credential-stuffing patterns
repeated authentication failure
unusual session concurrency
suspicious privileged activity
```

as security evidence.

Risk scoring MUST NOT become merchant business authority.

It may trigger bounded outcomes such as step-up authentication, security restriction, alerting or session revocation under accepted security policy.

---

## 54. Authentication Information Leakage

Public authentication responses SHOULD avoid unnecessary disclosure of whether:

```text
Identity exists
staff reference exists
specific credential was correct
merchant relationship exists
```

Precise internal security diagnostics may be retained where justified.

---

## 55. Rate Limiting and Credential Attack Protection

Login, recovery, PIN and other credential-verification endpoints MUST compose with Main Street's Platform Resource Protection architecture.

Rate limiting does not establish whether a credential is semantically valid.

It is a security/resource-protection control.

---

## 56. Spring Security Boundary

Spring Security MAY provide:

```text
HTTP security filters
WebAuthn/password adapter integration
cookie extraction
CSRF support
security middleware
session credential extraction
```

but remains infrastructure.

Canonical direction:

```text
HTTP
    ↓
Spring Security / security adapter
    ↓
Main Street authentication/session authority
    ↓
Trusted Execution Context
    ↓
application
    ↓
MS-PROT-062
    ↓
capability
```

Capability code MUST NOT depend directly upon browser cookies, Spring `SecurityContext`, JWTs, CSRF tokens or password hashes.

---

## 57. Next.js Boundary

Next.js may render authenticated pages, forward requests and perform frontend aggregation.

It MUST NOT establish independent merchant authority.

The Java backend remains the authoritative authentication/session security boundary.

---

## 58. Audit

Authoritative mutation evidence SHOULD retain non-secret context sufficient to establish:

```text
Identity
Execution Principal
Merchant Scope
Session Identity
authentication assurance
device context where material
AI-mediated origin where material
request/correlation identity
```

Raw session credentials, passwords, PINs and private-key material MUST NOT enter audit records.

---

## 59. Data Protection

Session and authentication records are security/personal data.

They SHALL be retained only for their justified security/audit purposes.

Expired bearer credentials MUST NOT be retained indefinitely merely because unrelated business history remains durable.

---

## 60. Recovery / Disaster Recovery

Backup/restore MUST NOT accidentally reactivate session state that the recovery process cannot safely prove remains valid.

A production recovery may invalidate human sessions globally when necessary to guarantee restored authentication security.

Business state remains independent of such session invalidation.

---

## 61. Failure Categories

The architecture distinguishes at least:

```text
AUTHENTICATION_FAILED
SESSION_NOT_FOUND
SESSION_EXPIRED
SESSION_REVOKED
SESSION_SECURITY_INVALID
SCOPE_RESOLUTION_FAILED
RELATIONSHIP_NOT_CURRENT
DEVICE_CONTEXT_REQUIRED
DEVICE_CONTEXT_INVALID
AUTHENTICATION_ASSURANCE_INSUFFICIENT
```

External responses MAY deliberately normalise some categories to reduce attacker information.

These are authentication/security outcomes, not capability lifecycle states.

---

## 62. Alternatives

### Server-authoritative opaque session

**Selected.**

It provides immediate revocation, current relationship evaluation, simple multi-merchant identity handling and clean device-state integration.

### Self-contained human JWT carrying authority

**Rejected as primary model.**

It creates stale-claim and revocation complexity and encourages identity/session state to absorb merchant authority.

### Java-process-local session

**Rejected for production.**

It creates process affinity and restart/session-loss problems.

### External IdP as complete Main Street authority

**Rejected.**

An IdP may prove Identity, but it does not own Main Street Merchant Scope, workforce relationships, device authority or capability permission.

### Merchant-specific authentication session per merchant

**Rejected as universal model.**

Identity continuity and Merchant Scope remain separate.

---

## 63. Falsification

The architecture survives the following cases:

```text
one Identity controls several merchants
former Controller retains an open browser after transfer
staff Membership is suspended
staff operational device is revoked
staff PIN is stolen but device is unauthorised
authorised device is stolen but staff proof is unavailable
merchant closes while Identity controls another merchant
application process restarts
session database becomes unavailable
customer knows another customer's resource identifier
guest accesses a bounded tracking link
provider webhook executes without human session
background timer executes without human session
frontend displays stale privilege state
critical Controller action is attempted from an old session
merchant storefront custom domain attempts to receive dashboard credential
```

In each case, the architecture preserves current authoritative relationships rather than trusting stale client state.

---

## 64. Trade-Offs

Main Street accepts a server-side security lookup because immediate revocation and current authority are more important than eliminating a persistence access.

Main Street accepts stronger authentication and step-up requirements for high-authority operations because Controller compromise has materially greater impact than ordinary website-session inconvenience.

Main Street accepts short staff idle locks because shared/open operational environments create significant walk-away risk, but combines them with fast reauthentication to preserve usability.

Main Street accepts separate device/person/session credentials because collapsing them would turn theft of one credential into compromise of all trust dimensions.

---

## 65. Deferred Question Catalogue

The following narrower questions remain deferred and are catalogued in the canonical DDR with these stable identifiers:

| ID | Question | Classification / owner | Revisit trigger |
|---|---|---|---|
| ADR-014-DQ-001 | Exact WebAuthn/passkey implementation/provider/library | Security implementation architecture | Before production Controller authentication implementation |
| ADR-014-DQ-002 | Exact hardware-security-key requirement for platform/high-risk roles | Security policy | Before high-privilege platform administration is production-enabled |
| ADR-014-DQ-003 | Exact password/KDF implementation where fallback passwords exist | Security implementation detail | Before fallback password credentials reach production |
| ADR-014-DQ-004 | Exact recovery evidence/mechanism for lost authenticators | Security architecture | Before production account/control recovery |
| ADR-014-DQ-005 | Exact CSRF implementation/library | Web-security implementation detail | During privileged browser implementation |
| ADR-014-DQ-006 | Exact persistent Session Record schema/index strategy | Persistence implementation detail | During authentication/session implementation |
| ADR-014-DQ-007 | Exact session cache technology/invalidation strategy | Performance/security architecture | Only if authoritative persistence lookup becomes materially expensive |
| ADR-014-DQ-008 | Exact device-key storage/hardware binding strategy by client platform | Device-security implementation architecture | Before operational-device implementation |
| ADR-014-DQ-009 | Federated/OIDC/social authentication | Provider architecture candidate | When product requirements justify external identity federation |
| ADR-014-DQ-010 | CustomerAccount-specific authentication policy | Product/security design | Before optional customer accounts become production-active |
| ADR-014-DQ-011 | Exact guest contextual-access credential representation | Security architecture | Before secure order/booking/customer tracking links |
| ADR-014-DQ-012 | Delegated staff-device enrolment by non-Controller administrators | Semantic/design candidate | When merchant evidence demonstrates Controller-only enrolment is inadequate |
| ADR-014-DQ-013 | Offline staff operational authority | Security/offline architecture | If offline merchant operations become required |
| ADR-014-DQ-014 | Native mobile authentication/session representation | Delivery/security architecture | Before a native mobile client is introduced |
| ADR-014-DQ-015 | Dedicated distributed session service | Platform architecture | Only if future topology makes relational session authority unsuitable |
| ADR-014-DQ-016 | Risk-scoring model and automated security response | Security architecture | When sufficient telemetry/threat evidence justifies adaptive security |
| ADR-014-DQ-017 | Exact CSP/Trusted Types browser hardening profile | Frontend security implementation | Before privileged web production hardening |

Every identifier remains stable. Resolution must record its resolving authority or implementation evidence. Deferred items do not weaken the accepted architecture.

---

## 66. Implementation Constraints

Any later implementation must preserve:

```text
WebAuthn/passkey-first Controller authentication

server-authoritative opaque sessions

256-bit CSPRNG session credential baseline

Session Identity ≠ bearer credential

Secure
HttpOnly
host-only __Host- cookie

SameSite=Strict by default for privileged session

explicit CSRF defence

strong CSP/XSS mitigation

Session ≠ business authority

Identity Session ≠ Merchant Scope

current relationships revalidated

staff device proof
    ≠ staff proof
    ≠ session credential

short bounded session lifetimes

step-up authentication for critical control operations

immediate server-side revocation

session persistence independent of application process

raw session credentials excluded from logs/events/audit/AI

frontend and Spring framework remain adapters
```

---

## 67. Target-2 Conformance Gate

Target 2 is Design-Closed because this accepted architecture guarantees:

```text
authentication ≠ authorisation
Identity ≠ Merchant Scope
Session ≠ business authority

phishing-resistant Controller authentication is supported as primary
opaque server-authoritative human sessions are selected
session credentials contain no mutable authority
session credential entropy baseline is explicit
privileged cookies are host-bound, Secure and HttpOnly
custom merchant domains cannot receive admin/staff credentials
explicit CSRF defence exists
XSS/browser-script security is recognised as session security
session fixation is prevented
session credentials rotate on material security events
server-side revocation is immediate
relationships/device state are current
multi-merchant Identity is supported
Controller transfer cannot be defeated by stale session
staff operational access requires authorised device + person
staff device proof cannot be created by staff credential alone
staff walk-away timeout is bounded
critical control actions require recent strong authentication
guest/contextual/system/integration paths remain independent
session-store uncertainty fails closed
recovery cannot silently resurrect unsafe sessions
deferred questions have stable traceability identifiers
```

Implementation conformance remains future work under `IMPLEMENTATION-RULES.md`.

---

## 68. Amendment / Supersession Effect

ADR-014 SHALL:

```text
select WebAuthn/passkeys as the primary Merchant Controller authentication direction

select opaque server-authoritative human sessions

select durable server-controlled session authority

select 256-bit random session credentials as the baseline

require host-bound __Host- protected cookies for privileged browser sessions

require CSRF protection beyond SameSite alone

require browser/XSS hardening as part of session defence

require current server-side relationship/device resolution

establish high-assurance Controller and staff session lifetime baselines

require step-up authentication for critical Controller operations

require independent cryptographic operational-device context plus staff identity proof

prohibit mutable merchant authority in browser session claims

preserve customer/guest/system/integration distinctions

catalogue ADR-014-DQ-001 through ADR-014-DQ-017

close MS-PROT-079 Target 2

activate Target 3 — Initial merchant configuration bootstrap
```

---

## 69. Acceptance Statement

Main Street shall treat authentication as the establishment of trusted identity/security continuity, never as a substitute for current business authority.

> **Phishing-resistant identity proof, opaque server-authoritative sessions, tightly scoped browser credentials, current merchant/device authority, immediate revocation and fresh authentication for high-impact control operations.**
