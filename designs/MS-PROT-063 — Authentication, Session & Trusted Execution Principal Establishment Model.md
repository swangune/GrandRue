# MS-PROT-063 — Authentication, Session & Trusted Execution Principal Establishment Model

**Document ID:** MS-PROT-063  
**Version:** 1.0  
**Status:** ACCEPTED  
**Depends on:** MS-PROT-023, MS-PROT-028, MS-PROT-031, MS-PROT-043, MS-PROT-057, MS-PROT-059, MS-PROT-062  
**Purpose:** Define how Main Street converts incoming human, guest, system, scheduled and integration interactions into a trusted execution context without allowing authentication technology, sessions, identifiers or AI mediation to become business authority.

---

## 1. Governing principle

> **Authentication establishes trusted identity or origin evidence; scope resolution establishes where the interaction applies; execution-principal establishment records who or what is acting in that context; authorisation and business eligibility remain separate downstream decisions.**

Main Street MUST NOT collapse identity, session, tenant scope, business relationship and authority into a single `User` abstraction.

Canonical flow:

```text
Untrusted / external interaction
        ↓
Authentication boundary
        ↓
Identity / origin evidence
        ↓
Scope resolution
        ↓
Relationship/context resolution
        ↓
Trusted Execution Context
        ↓
MS-PROT-062 decision composition
        ↓
Capability-owned execution
```

---

## 2. Scope

This authority governs:

- authentication proof intake;
- identity/origin establishment;
- session continuity;
- merchant/platform scope resolution;
- execution-principal construction;
- guest contextual-access proof;
- customer-account authentication context;
- staff authentication context;
- merchant-controller authentication context;
- integration-principal establishment;
- scheduled/system-principal establishment;
- AI-assisted attribution/delegation context;
- session expiry/revocation implications;
- trusted-context propagation into application/runtime boundaries.

It does NOT own:

- capability permissions;
- merchant staff policy;
- CustomerContext business relationships;
- subscription entitlement;
- trust-policy requirements;
- business invariants;
- merchant configuration semantics;
- provider business semantics.

---

## 3. Canonical concepts

### 3.1 Identity

A durable or otherwise trusted representation of a human, system or integration identity where identification is required.

Identity answers:

> Who or what has been authenticated or otherwise established?

Identity MUST NOT by itself determine merchant scope or operation authority.

### 3.2 Execution Principal

The attributable actor/origin for one execution context.

Examples:

```text
merchant controller
staff member
registered customer
guest contextual principal
system process
scheduled process
integration principal
AI-mediated delegated principal
```

Execution Principal is contextual. The same Identity MAY produce different principals in different merchant relationships or scopes.

### 3.3 Session

Security/infrastructure state preserving authenticated continuity across interactions.

A Session MAY carry or reference:

```text
identity reference
authentication time
authentication assurance
expiry
revocation state
security metadata
```

A Session MUST NOT own business authority.

### 3.4 Trusted Execution Context

A trusted application-boundary representation of the context required for downstream runtime evaluation.

Conceptually:

```text
TrustedExecutionContext
{
    scope
    principal
    authenticatedIdentity?
    authenticationEvidence / assurance
    contextualAccess?
    relationshipContext?
    correlation / request context
}
```

This is a semantic contract, not a mandated Java record/class shape.

### 3.5 Contextual Access Proof

A proof that grants narrowly scoped access to a specific merchant/resource/interaction without establishing a global customer account or broad identity authority.

Example:

```text
secure guest order link
    ↓
Merchant M
Order O123
permitted projection/operation scope
```

---

## 4. Authentication is not authorisation

Hard invariant:

```text
Authentication
    ≠
Authorisation
```

A successful login MUST NOT imply permission to perform every operation available in the merchant or platform.

Examples:

```text
customer authenticated
    ≠ may cancel another customer's appointment

staff authenticated
    ≠ may issue refunds

merchant controller authenticated
    ≠ automatically acting in every merchant they control
```

Authorisation remains governed by MS-PROT-028, MS-PROT-062 and applicable capability policy.

---

## 5. Identity is not merchant scope

A human identity MAY have relationships with multiple merchants.

Example:

```text
Identity P
   ├── controller of Merchant A
   ├── controller of Merchant B
   └── staff of Merchant C
```

Authentication establishes `P`.

Merchant Scope MUST be separately resolved and validated against trusted routing/session/application context and current relationships.

Switching merchants MAY reuse the same authenticated identity/session while producing a new Trusted Execution Context.

The old merchant context MUST NOT leak into the new one.

---

## 6. Identity is not global role

Roles/privileges are contextual relationships, not intrinsic properties of Identity.

Invalid model:

```text
Identity P
    role = OWNER
```

Preferred semantic model:

```text
Identity P
   │
   ├── Merchant A relationship = CONTROLLER
   └── Merchant B relationship = STAFF + privileges
```

Role/privilege resolution belongs to the applicable merchant relationship and authority model.

---

## 7. Staff authentication

A staff identifier, staff number, email address, username or similar identifier MUST NOT by itself establish authority.

A valid staff execution context requires, as applicable:

```text
staff identity / identifier
+
valid authentication proof
+
merchant relationship
+
current access state
+
resolved merchant scope
```

Termination, revocation or removal of the merchant relationship MUST be able to prevent future authorised execution even if an old identifier remains known.

Exact staff-login UX remains downstream.

---

## 8. Merchant-controller authentication

Merchant-controller authentication establishes the human identity controlling one or more merchant accounts.

It does not automatically select a Merchant Scope.

Canonical flow:

```text
authenticate controller identity
        ↓
resolve requested merchant context
        ↓
verify current controller relationship
        ↓
establish Merchant Scope
        ↓
Trusted Execution Context
```

Controller identity verification does not imply business verification, consistent with MS-PROT-028.

---

## 9. CustomerAccount authentication

CustomerAccount authentication establishes authenticated customer access where a merchant has enabled that capability.

It MUST NOT redefine CustomerContext.

Canonical distinction:

```text
CustomerAccount
    = authenticated access mechanism

CustomerContext
    = merchant-scoped business relationship
```

Authenticated customer access to an Order, Appointment or other object still requires relationship/contextual authorisation.

Matching email, phone number or name MUST NOT silently establish access to unrelated business records.

---

## 10. Guest / anonymous interaction

Main Street MUST support guest/anonymous execution where the governing operation semantics permit it.

Guest interaction MUST NOT cause creation of a fake persistent account merely to satisfy the authentication model.

A guest execution may still establish:

```text
merchant scope
interaction identity/correlation
security/anti-abuse context
transaction-specific authority
```

where required.

Guest access is therefore not equivalent to `no context`.

---

## 11. Transaction-specific contextual access

Secure transaction-specific proofs MAY establish authority for a narrowly bounded context without creating a CustomerAccount.

Example:

```text
secure order link
    ↓
validate proof
    ↓
Merchant M
Order O123
allowed customer-safe projection/actions
```

Hard invariant:

> **Guest contextual access MUST NOT be promoted into global customer identity or broad merchant access merely because the proof is valid.**

---

## 12. System and scheduled principals

Authoritative mutation does not require a human session, but it requires an attributable principal/origin.

Examples:

```text
SYSTEM
SCHEDULED_PROCESS
```

These categories are not omnipotent.

A scheduled process may only invoke operations for which it has registered system authority and must still pass applicable runtime evaluation.

No browser session or human account is required for legitimate system execution.

---

## 13. Integration principals

External callbacks and API/integration requests require authenticated or validated origin evidence appropriate to the integration.

Canonical flow:

```text
provider callback / API interaction
        ↓
validate signature / credential / equivalent proof
        ↓
establish integration identity/origin
        ↓
resolve registered provider/integration binding
        ↓
resolve merchant/platform/process context
        ↓
Integration Principal
```

A valid callback MUST NOT grant arbitrary authority.

It authorises only the registered interaction scope and still composes with MS-PROT-062.

---

## 14. AI-assisted execution attribution

AI is not an independent unrestricted business principal.

Where AI assists a merchant or staff member:

```text
human trusted context
        ↓
AI interaction
        ↓
intent inference
        ↓
candidate operation/configuration
        ↓
required approval/validation
        ↓
execution with preserved attribution
```

The resulting execution evidence SHOULD retain both:

```text
human/delegating principal
AI-mediated origin/context
```

where relevant for auditability.

AI MUST NOT acquire general merchant authority merely because it receives natural-language instructions.

---

## 15. Session continuity

Session continuity MAY be implemented with opaque sessions, tokens or another mechanism, but the semantic rule is independent of technology.

A Session is evidence for continuity; it is not permanent authority.

A session that is:

```text
expired
revoked
security-invalidated
associated with disabled access
```

MUST NOT continue to establish a valid trusted principal where current security semantics invalidate it.

Where current relationship state matters, stale session claims MUST NOT override authoritative relationship/access state.

---

## 16. Authentication assurance

Trusted Execution Context MAY preserve authentication assurance/evidence required by operation-specific trust checks.

Examples:

```text
ordinary authenticated session
recent re-authentication
stronger authentication evidence
verified integration signature
```

MS-PROT-063 does not impose universal MFA or re-authentication requirements.

Applicable trust requirements remain owned by MS-PROT-028 / MS-PROT-062 and specific platform/capability policies.

---

## 17. Client input is not trusted context

The following MUST NOT become trusted merely because a client supplied them:

```text
merchantId
userId
staffId
customerId
role
privilege
resourceId
scope
system=true
```

They may be input references, but trusted application boundaries must independently resolve and validate their meaning.

Possession of a globally unique identifier does not establish access.

---

## 18. Merchant Scope establishment

Merchant Scope is governed by MS-PROT-031 and is incorporated into Trusted Execution Context only after trusted resolution.

Possible trusted sources include:

```text
validated merchant session context
resolved storefront/domain mapping
validated merchant route
trusted API credential binding
registered integration binding
platform administration context
```

The exact mechanism varies by delivery surface.

---

## 19. Platform scope

Not every execution is merchant-scoped.

Platform administration or platform-owned operations MAY establish:

```text
scope = PLATFORM
```

rather than fabricating a synthetic merchant.

Cross-merchant operations require explicit platform semantics and authority.

---

## 20. Context propagation boundary

Transport-specific security details MUST NOT leak into capability domain logic.

Preferred flow:

```text
HTTP / API / callback / scheduler
        ↓
security / authentication adapter
        ↓
Trusted Execution Context
        ↓
application layer
        ↓
MS-PROT-062 composition
        ↓
capability execution
```

Capability code SHOULD NOT inspect cookies, JWT claims, raw API keys or provider signatures directly.

---

## 21. Provider neutrality

Authentication/provider technology remains replaceable infrastructure unless a semantic requirement explicitly depends on it.

Identity-provider availability is not itself a universal business-eligibility predicate.

For example, an outage preventing new login MAY differ from the validity of already-established sessions depending on the selected security architecture.

Domain modules MUST NOT depend directly on provider-specific identity SDK concepts.

---

## 22. Failure categories

Authentication/principal establishment SHOULD distinguish at least the following categories where useful:

```text
AUTHENTICATION_FAILED
SESSION_EXPIRED
SESSION_REVOKED
SCOPE_RESOLUTION_FAILED
RELATIONSHIP_NOT_FOUND
CONTEXTUAL_ACCESS_INVALID
INTEGRATION_PROOF_INVALID
PRINCIPAL_ESTABLISHMENT_FAILED
```

These are diagnostic/security categories, not capability lifecycle states.

Downstream authorisation failures remain distinct.

---

## 23. Auditability

Every authoritative mutation MUST be attributable to an execution principal/origin sufficient for audit and diagnostics.

Evidence MAY include:

```text
principal identity/type
merchant/platform scope
authentication/session reference
integration/provider reference
AI-mediated origin
correlation/causation
execution time
```

Only information justified by audit/security requirements should be retained.

---

## 24. Security revocation and relationship change

The model MUST permit business/security access to change independently of Identity existence.

Examples:

```text
staff leaves merchant
controller removed
customer account disabled
integration disconnected
API credential revoked
```

Such changes MUST be able to prevent subsequent principal establishment or authorisation without deleting unrelated historical identity/business evidence.

---

## 25. No global `User` authority object

Main Street MUST NOT introduce a universal object whose fields simultaneously attempt to own:

```text
identity
tenant
role
permissions
customer relationship
subscription
verification
session
```

This would collapse independently owned semantics and create authority leakage.

A convenience projection named `User` MAY exist at a delivery surface only if it does not become semantic authority.

---

## 26. Relationship with MS-PROT-062

MS-PROT-063 produces trusted inputs for runtime decision composition.

Conceptually:

```text
MS-PROT-063
    establishes:
        principal
        scope
        authentication/context evidence

MS-PROT-062
    evaluates:
        semantic applicability
        commercial entitlement
        actor authorisation
        trust satisfaction
        operational eligibility
        provider readiness
        authoritative revalidation
```

MS-PROT-063 MUST NOT duplicate the MS-PROT-062 decision engine.

---

## 27. Relationship with CustomerContext

Authentication does not own customer business history.

CustomerContext remains merchant-scoped relationship authority.

A logged-in account MAY resolve to one or more authorised CustomerContexts, but authentication MUST NOT create or merge CustomerContexts solely from weak identifier similarity.

Guest interactions MAY create or associate business context according to the governing customer/enquiry/order semantics without requiring authentication.

---

## 28. Relationship with merchant staff policy

Merchant staff roles, privileges and access choices remain merchant/business policy where registered semantics permit them.

Authentication proves the staff identity/access proof.

Authorisation evaluates the current role/privilege relationship.

Therefore:

```text
valid staff login
    ≠
valid privilege for requested operation
```

---

## 29. Relationship with entitlement

Authentication and subscription entitlement are independent.

A successfully authenticated merchant may still lack commercial entitlement for a protected operation.

An entitlement failure MUST NOT be misreported as authentication failure.

Likewise, entitlement MUST NOT automatically establish identity or merchant scope.

---

## 30. Relationship with trust requirements

Some operations MAY require additional trust evidence beyond ordinary authentication.

The authentication subsystem may provide such evidence/assurance, but trust-policy applicability remains owned elsewhere.

This prevents the authentication subsystem from becoming a universal policy engine.

---

## 31. Relationship with providers

Provider callback authentication verifies origin/evidence.

Provider business facts remain owned by the provider/capability integration semantics.

Example:

```text
valid payment webhook signature
    ≠
payment succeeded
```

The signature proves source integrity; the payload/business fact still requires provider/capability reconciliation.

---

## 32. Concurrency / stale context

A Trusted Execution Context MAY remain valid across more than one interaction, but it MUST NOT freeze mutable business truth.

Examples of information that may require current re-evaluation:

```text
staff relationship active?
controller relationship active?
account disabled?
merchant access revoked?
contextual link expired?
```

Likewise, execution-time business invariants remain subject to MS-PROT-023/MS-PROT-062 authoritative revalidation.

---

## 33. Falsification findings

The following assumptions were rejected:

| Failed assumption | Why it fails |
|---|---|
| Identity = role | One identity may have different roles in different merchants |
| Identity = Merchant Scope | One identity may control/work for multiple merchants |
| Session = authority | Session only proves continuity; business authority remains contextual |
| Logged in customer = owner of every matching record | Relationship/context checks still apply |
| Guest interaction requires fake account | Legitimate anonymous/contextual operations exist |
| Secure transaction link = global customer identity | Proof is intentionally narrow |
| Staff number alone grants access | Identifier possession is not authentication/authority |
| SYSTEM is omnipotent | System operations remain registered and bounded |
| Integration callback can mutate anything | Valid origin does not imply arbitrary authority |
| AI inherits merchant authority generally | Creates uncontrolled delegation |
| Client `merchantId` defines tenant context | Untrusted input cannot establish Merchant Scope |
| Every operation is merchant-scoped | Platform operations exist |
| Authentication provider semantics belong in domain code | Violates provider neutrality and ports/adapters |
| Stale session claims override current relationship state | Revocation/termination would be ineffective |

---

## 34. Cross-domain validation

### Multi-merchant controller

One authenticated person switches between two controlled merchants. Identity remains stable while Merchant Scope and execution context change. **PASS**

### Staff member with another merchant relationship

One person is controller in one merchant and staff in another. Roles remain relationship-scoped. **PASS**

### Guest physical-product order

Guest creates and tracks an Order through contextual proof without forced account creation. **PASS**

### Registered customer

CustomerAccount authentication enables portal access but individual Orders still require relationship authority. **PASS**

### Payment webhook

Provider callback establishes Integration Principal after proof validation but business fact still reconciles through Payment capability. **PASS**

### Scheduled reminder

Scheduled Process Principal executes without human login and remains bounded by registered authority. **PASS**

### AI concierge

AI preserves the delegating merchant/staff principal and cannot become independent business authority. **PASS**

### Platform administration

Platform-scoped execution is representable without synthetic merchant identity. **PASS**

---

## 35. Hard invariants

1. Authentication is not authorisation.
2. Identity is not Execution Principal context.
3. Identity is not Merchant Scope.
4. Role/privilege is contextual, not globally intrinsic to Identity.
5. Session is security continuity evidence, not business authority.
6. Merchant Scope must be established at a trusted boundary.
7. Client-supplied tenant/role identifiers do not establish trust.
8. Possession of a resource identifier does not establish access.
9. CustomerAccount and CustomerContext remain distinct.
10. Guest contextual access does not create global customer authentication.
11. Staff identifiers alone do not establish authority.
12. System, scheduled, integration and AI principals are not omnipotent.
13. Integration-origin validity does not imply arbitrary mutation authority.
14. AI-mediated execution preserves attributable delegated context.
15. Current revocation/relationship state may invalidate stale session continuity.
16. Platform scope remains distinct from Merchant Scope.
17. Transport/provider security details do not leak into capability domain code.
18. Every authoritative mutation must be attributable to a legitimate execution principal/origin.
19. MS-PROT-063 establishes trusted context; MS-PROT-062 composes runtime eligibility/authority decisions.
20. Authentication technology must remain replaceable infrastructure unless explicitly promoted by semantic requirement.

---

## 36. Explicit exclusions

MS-PROT-063 does not select:

```text
identity provider
password hashing implementation
session store
JWT vs opaque session
cookie strategy
MFA vendor/mechanism
staff-login UX
account recovery flow
OAuth/OIDC implementation details
API-key technology
webhook signature technology
service-mesh identity
browser token storage
session duration values
```

These are downstream implementation/provider decisions unless future evidence shows a semantic consequence requiring design authority.

---

## 37. Canonical backend graph

```text
Human / Guest / Integration / Scheduled / System interaction
                         │
                         ▼
               Authentication Boundary
                         │
                         ▼
              Identity / Origin Evidence
                         │
                         ▼
                  Scope Resolution
                         │
                         ▼
             Relationship / Access Context
                         │
                         ▼
              Trusted Execution Context
                  ┌──────┼───────┐
                  │      │       │
               Scope  Principal  Assurance /
                               Contextual Proof
                  │
                  ▼
                 MS-PROT-062
       Runtime Decision Composition
                  │
                  ▼
       Capability-Owned Execution
```

---

## 38. Acceptance statement

MS-PROT-063 is accepted as the canonical backend authority for authentication/session continuity and trusted execution-principal establishment.

It preserves Main Street's composite architecture by keeping identity, merchant scope, customer relationship, commercial entitlement, authorisation, business invariants and provider semantics independently owned while providing one trusted application-boundary context for downstream runtime evaluation.

Canonical rule:

> **Authenticate the identity or origin, resolve the trusted scope and contextual relationship, establish an attributable execution principal, then let the owning runtime/capabilities decide what that principal may actually do.**
