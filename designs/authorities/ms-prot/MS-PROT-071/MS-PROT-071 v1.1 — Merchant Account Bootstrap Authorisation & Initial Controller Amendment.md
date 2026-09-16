# MS-PROT-071 v1.1 — Merchant Account Bootstrap Authorisation & Initial Controller Amendment

**Document ID:** MS-PROT-071  
**Version:** 1.1  
**Status:** ACCEPTED after governed review, falsification and manual approval  
**Amends:** MS-PROT-071 v1.0 — Merchant Account Establishment & Tenancy Identity Model  
**Depends on:** MS-PROT-028 v1.2, MS-PROT-028 v1.3, MS-PROT-063, MS-PROT-071 v1.0, MS-PROT-072  
**Purpose:** Resolve the bootstrap authorisation predicate for ordinary Merchant Account self-establishment and define the minimum cross-capability atomic invariant required to prevent an established Merchant Account from being created without its initial authorised controller.

---

## 1. Governing amendment

MS-PROT-071 v1.0 remains accepted except where this amendment defines the authorisation and initial-control semantics of ordinary Merchant Account self-establishment.

The governing decision is:

> **A new Merchant Account may be self-established only by a currently authenticated human execution principal acting in PLATFORM scope who is authorised for the narrow platform operation `ESTABLISH_OWN_MERCHANT_ACCOUNT`. Successful self-establishment atomically establishes the Merchant Account and the initiating authenticated identity as its initial Merchant Controller.**

Canonical bootstrap:

```text
authenticated human identity
        ↓
Trusted Execution Principal
scope = PLATFORM
        ↓
ESTABLISH_OWN_MERCHANT_ACCOUNT authorised
        ↓
atomic bootstrap
    Merchant Account existence
        +
    initial Merchant Controller relationship
        ↓
merchant-scoped control exists
```

This rule exists only for ordinary self-establishment. Administrative, migration, partner or bulk provisioning are not implicitly authorised by this amendment.

---

## 2. Why PLATFORM scope is required

No Merchant Account exists before successful establishment. Therefore no valid Merchant Scope or merchant-scoped controller relationship can be a prerequisite for the bootstrap request.

Rejected circular model:

```text
Merchant Scope required to establish Merchant Account
        +
Merchant Account required to resolve Merchant Scope
```

`EstablishMerchantAccount` is therefore a platform-level bootstrap operation executed in trusted `PLATFORM` scope.

After the bootstrap commits, later merchant-scoped operations MUST resolve the merchant context and current controller relationship through the governing identity/access authorities.

---

## 3. Ordinary self-establishment principal

Ordinary self-establishment requires all of the following:

```text
principal category = HUMAN
current authentication = valid
trusted execution context = established
execution scope = PLATFORM
operation authority = ESTABLISH_OWN_MERCHANT_ACCOUNT
```

Authentication alone is insufficient.

```text
AUTHENTICATED
    ≠
AUTHORISED TO ESTABLISH A MERCHANT ACCOUNT
```

The authorisation predicate is operation-specific and MUST NOT be represented as proof of civil identity, business legitimacy or global identity role.

---

## 4. No universal identity or business verification prerequisite

Ordinary Merchant Account self-establishment MUST NOT require, solely because a Merchant Account is being created:

```text
real-world identity verification
business verification
premises verification
Google Business Profile verification
payment-provider readiness
professional verification
Commercial Entitlement
Merchant Configuration
```

A separately accepted legal, security, regulatory or platform restriction MAY constrain establishment only within its accepted scope.

This amendment does not create or imply a universal `verifiedMerchant` or `verifiedController` state.

---

## 5. `ESTABLISH_OWN_MERCHANT_ACCOUNT` authority

`ESTABLISH_OWN_MERCHANT_ACCOUNT` means only:

> The authenticated human principal may establish a new Main Street Merchant Account whose initial Main Street control relationship belongs to that same authenticated identity.

It MUST NOT be interpreted as authority to:

```text
establish a merchant for an arbitrary other identity
assign an arbitrary initial controller
verify a real-world business
publish a storefront
activate Merchant Configuration
establish Commercial Entitlement
bypass account-control policy
perform administrative provisioning
```

`OWN` refers to Main Street account control for the bootstrap operation. It does not assert legal ownership of the underlying real-world business.

---

## 6. Initial-controller derivation

The initial Merchant Controller identity MUST be derived from the trusted execution context of the initiating principal.

Client-supplied fields such as:

```text
ownerUserId
controllerIdentity
role = OWNER
merchantScope
```

MUST NOT independently establish the initial controller relationship.

A transport contract MAY carry correlation or registration data, but it MUST NOT override the authenticated identity established by the trusted application boundary.

---

## 7. Strengthened bootstrap atomicity invariant

MS-PROT-071 v1.0 defined the original establishment atomicity as:

```text
allocate authoritative merchantIdentifier
+
commit Merchant Account existence
```

Implementation review demonstrated an additional accepted invariant from the merchant-control authority: ordinary self-establishment MUST NOT commit an authoritative Merchant Account with no initial controller relationship.

The ordinary bootstrap atomicity invariant is therefore amended to:

```text
allocate authoritative merchantIdentifier
+
commit Merchant Account existence
+
establish initial Merchant Controller relationship
```

All three effects MUST commit as one atomic bootstrap outcome for ordinary self-establishment.

Forbidden committed states:

```text
Merchant Account exists = YES
initial controller relationship = NONE
```

and:

```text
initial controller relationship = COMMITTED
Merchant Account existence = NO
```

This cross-capability atomicity is justified by a specific invariant and does not transfer semantic ownership between contexts.

---

## 8. Semantic ownership remains separate

Atomic consistency does not create shared semantic ownership.

```text
Merchant Account context
    OWNS Merchant Account existence and merchantIdentifier

Identity / merchant-control authority
    OWNS Merchant Controller relationship

Application orchestration / transactional coordination
    coordinates the accepted bootstrap invariant
```

After bootstrap, Merchant Account existence and controller lifecycle remain independently governed facts.

Revocation or later change of controller authority MUST NOT by itself erase Merchant Account existence.

---

## 9. Principal categories excluded from ordinary self-establishment

The following principal categories MUST NOT receive `ESTABLISH_OWN_MERCHANT_ACCOUNT` merely because they are trusted execution principals:

```text
SYSTEM
SCHEDULED_PROCESS
INTEGRATION
AI-mediated independent origin
GUEST / ANONYMOUS
```

AI MAY assist the human interaction but MUST preserve the authenticated human as the authoritative initiating principal and MUST NOT autonomously establish a Merchant Account.

Any future administrative, migration, partner or machine provisioning path requires its own explicit accepted operation and authority.

---

## 10. Authentication assurance

A current valid authentication context is sufficient for ordinary self-establishment unless a separately accepted security policy requires stronger assurance for this operation.

This amendment does not impose universal:

```text
MFA
recent re-authentication
real-world identity proofing
biometric verification
```

Unknown or invalid authentication MUST NOT be treated as satisfied.

---

## 11. Authorisation decision contract

Ordinary self-establishment is permitted if and only if all of the following are true at execution time:

```text
1. A Trusted Execution Context exists.
2. The principal is an authenticated human identity.
3. The authentication/session remains currently valid.
4. Execution scope is PLATFORM.
5. The principal is currently authorised for ESTABLISH_OWN_MERCHANT_ACCOUNT.
6. No separately accepted platform-wide restriction denies this establishment request.
```

Condition 6 MUST NOT become a hidden policy escape hatch. Only separately accepted restrictions may participate.

The operation MUST NOT infer merchant scope, controller authority, business legitimacy or verification from client-supplied attributes.

---

## 12. Retry and idempotency

`logicalEstablishmentRequestIdentity` identifies the complete ordinary bootstrap intent.

Repeated execution of the same logical request MUST resolve to the same committed bootstrap outcome:

```text
same Merchant Account
+
same initial-controller relationship
```

A retry after acknowledgement loss MUST NOT:

```text
create another Merchant Account
create another initial-controller assignment
move the Merchant Account to another controller
restart downstream onboarding or commercial effects as part of the bootstrap transaction
```

The idempotency boundary therefore covers the complete bootstrap effect rather than identifier allocation alone.

---

## 13. Multiple Merchant Accounts and cardinality

This amendment does not establish either of the following rules:

```text
one human identity may control only one Merchant Account
one human identity may create unlimited Merchant Accounts
```

A distinct logical establishment request from the same authenticated principal is not a duplicate solely because the controller identity is the same.

Controller-to-Merchant-Account cardinality, rate limiting, abuse prevention and account-count restrictions remain separately governed.

---

## 14. Failure and outcome semantics

Where caller recovery differs, ordinary Merchant Account self-establishment MUST preserve at least the following distinctions:

```text
AUTHENTICATION_REQUIRED
    no valid authenticated human principal is established

AUTHORISATION_REJECTION
    authenticated principal lacks ESTABLISH_OWN_MERCHANT_ACCOUNT

VALIDATION_REJECTION
    the logical establishment request violates the accepted contract

CONFLICT
    a separately governed uniqueness/cardinality invariant rejects the request

TECHNICAL_FAILURE_BEFORE_COMMIT
    no bootstrap fact became authoritative

EXECUTION_UNCERTAIN
    commit outcome cannot yet be established safely

SUCCESS
    Merchant Account and initial controller relationship committed

ALREADY_ESTABLISHED
    the same logical request previously committed and the original bootstrap result is returned
```

`ALREADY_ESTABLISHED` is an idempotent success outcome, not a conflict.

A transport acknowledgement failure after commit MUST NOT redefine the committed bootstrap as failed.

---

## 15. Post-bootstrap separation

Successful Merchant Account bootstrap MUST NOT itself imply:

```text
onboarding started or completed
Merchant Configuration exists
Merchant Configuration activated
initial trial started
Commercial Agreement exists
Commercial Entitlement exists
business verified
storefront published
Provider Readiness satisfied
merchant operationally eligible
```

Those remain independently governed authorities and may react post-commit where accepted.

---

## 16. Falsification evidence

### 16.1 New authenticated merchant controller

A human authenticates in platform scope and is authorised for ordinary self-establishment.

Result: one Merchant Account and one initial controller relationship commit atomically.

**PASS**

### 16.2 Anonymous establishment attempt

No authenticated human principal exists.

Result: `AUTHENTICATION_REQUIRED`; no Merchant Account is established.

**PASS**

### 16.3 Authenticated but unauthorised principal

Authentication succeeds but `ESTABLISH_OWN_MERCHANT_ACCOUNT` is not authorised.

Result: `AUTHORISATION_REJECTION`.

**PASS**

### 16.4 Lost acknowledgement

Bootstrap commits and the response is lost. The same logical request is retried.

Result: original Merchant Account and controller relationship are returned; no duplicate effect occurs.

**PASS**

### 16.5 Client nominates another controller

The client supplies another identity as `ownerUserId`.

Result: the supplied identity does not become authority; initial controller derives from trusted execution context.

**PASS**

### 16.6 No real-world identity verification

A properly authenticated human has no separate civil-identity verification claim.

Result: ordinary self-establishment remains permitted unless a separately accepted obligation applies.

**PASS**

### 16.7 Payment or Google setup incomplete

External provider setup is absent.

Result: Merchant Account bootstrap is unaffected because provider readiness does not own tenancy establishment.

**PASS**

### 16.8 System or AI tries ordinary self-establishment

The principal is SYSTEM, INTEGRATION, SCHEDULED_PROCESS or autonomous AI origin.

Result: ordinary self-establishment authority is unavailable.

**PASS**

### 16.9 Partial bootstrap commit

Merchant Account commits but initial controller relationship does not, or vice versa.

Result: forbidden by the strengthened atomic invariant.

**PASS**

### 16.10 Later controller revocation

The initial controller is later removed or disabled under a future accepted controller-lifecycle authority.

Result: Merchant Account existence is not retroactively erased.

**PASS**

No falsifier requires a pre-existing Merchant Scope, universal identity verification, provider authority or shared semantic ownership.

---

## 17. Rejected alternatives

### A. Require pre-existing merchant-scoped controller relationship

Rejected because it creates an impossible bootstrap cycle.

### B. Authentication automatically authorises account creation

Rejected because authentication and authorisation are separate authorities.

### C. Establish Merchant Account first and attach controller asynchronously

Rejected because it permits an authoritative ownerless tenancy during ordinary self-establishment.

### D. Require real-world identity verification before account creation

Rejected because ordinary Main Street merchant operation does not justify a universal civil-identity gate.

### E. Trust a client-supplied initial-controller identifier

Rejected because client input is not trusted execution context.

### F. Allow SYSTEM/AI/integration principals to use the ordinary self-establishment permission

Rejected because trusted origin does not imply this platform authority and would create uncontrolled tenancy creation.

---

## 18. Hard invariants

1. Ordinary Merchant Account self-establishment is a `PLATFORM`-scoped operation.
2. The ordinary self-establishment principal MUST be a currently authenticated human execution principal.
3. Authentication alone MUST NOT confer Merchant Account establishment authority.
4. `ESTABLISH_OWN_MERCHANT_ACCOUNT` is the narrow authority for ordinary self-establishment.
5. No pre-existing Merchant Scope or merchant-scoped controller relationship is required before bootstrap.
6. Successful ordinary self-establishment MUST atomically commit Merchant Account existence and the initiating identity's initial Merchant Controller relationship.
7. The initial controller MUST derive from trusted execution context, not arbitrary client input.
8. Ordinary establishment MUST NOT require universal real-world identity verification, business verification, Merchant Configuration, Commercial Entitlement or Provider Readiness.
9. SYSTEM, SCHEDULED_PROCESS, INTEGRATION, guest and autonomous AI principals MUST NOT receive ordinary self-establishment authority by default.
10. Replay of the same logical establishment request MUST return the same complete bootstrap effect.
11. Controller-to-Merchant-Account cardinality, rate limiting and abuse restrictions remain separate decisions.
12. Cross-capability bootstrap atomicity MUST NOT transfer semantic ownership between Merchant Account and merchant-control authorities.
13. Later controller revocation or change MUST NOT by itself erase Merchant Account existence.
14. Successful bootstrap MUST NOT start onboarding, configuration activation, trial, commercial entitlement, publication or provider activation inside the establishment transaction.

---

## 19. Acceptance statement

Main Street now has a non-circular, minimally restrictive and explicit bootstrap authorisation policy for ordinary Merchant Account creation. A trusted authenticated human acts in platform scope, receives only the narrow establishment authority, and becomes the initial merchant controller in the same atomic bootstrap that establishes the tenancy.

> **Bootstrap account control atomically; do not make authentication, verification or merchant scope pretend the tenancy already exists.**
