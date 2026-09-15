# IMP-04 — Merchant Account, Scope, Identity & Trust — Closure Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Date:** 28 August 2026  
**Status:** **SUPERSEDED — MACRO CLOSURE INVALIDATED**  
**Verified pre-closure development head:** `d35547319d6ebc2e099bd98d6224e638b8cf36cd`

> **Supersession notice:** This historical closure decision is invalidated by `imp-04-macro-closure-correction-2026-08-28.md`. It MUST NOT be used to unlock IMP-05. The text below is retained to preserve the original reasoning and evidence trail; the superseding classification is `IMP-04 = PARTIALLY_CONFORMING`.

---

## 1. Governing completion target

MS-IMP-001 defines IMP-04 as the foundation for:

```text
Merchant Account
Controller establishment
Merchant Scope
authentication
WebAuthn/passkey implementation
sessions
Trusted Execution Context
trusted principal
credential/security foundation
baseline Audit
```

and requires proof that:

```text
identity
    != authority

identifier
    != Merchant Scope

session
    != immutable merchant authority
```

IMP-04 is a foundational macro target. It is not the final production-operability, full browser-hardening, recovery or system-wide security target. Later programme stages remain responsible for the broader delivery/operability concerns assigned to them.

---

## 2. Scope-to-implementation closure map

### 2.1 Merchant Account — CONFORMING_COMPLETE

The merchant-account model and persistence establish a durable merchant tenancy/account identity without treating existence as activation, configuration readiness or actor authority.

Relevant implementation includes the `mainstreet.merchantaccount` model and durable PostgreSQL merchant-account persistence.

### 2.2 Controller establishment — CONFORMING_COMPLETE

`MerchantAccountEstablisher` and `MerchantAccountBootstrapStore` establish the Merchant Account, initial Controller relationship and establishment evidence within the accepted bootstrap consistency boundary.

The Controller relationship remains a distinct authority relationship rather than an attribute implied by Identity or account identifier.

### 2.3 Merchant Scope — CONFORMING_COMPLETE

`mainstreet.application.MerchantScope` is an explicitly trusted merchant tenancy boundary. Ordinary untrusted identifiers do not become Merchant Scope merely because they name a merchant.

Scope construction is reserved for trusted delivery/background boundaries and does not imply Controller relationship, Membership, role, privilege, entitlement or capability authority.

### 2.4 Authentication — CONFORMING_COMPLETE for IMP-04 foundation

The implementation provides:

- WebAuthn authentication policy;
- durable WebAuthn credential persistence;
- durable WebAuthn authentication-subject binding;
- authenticated Identity reference authority;
- WebAuthn-to-Main-Street Session establishment bridge;
- opaque Session Credential issuance;
- durable authoritative Session Record lookup;
- request-time Session validation;
- host-bound privileged browser cookie representation;
- privileged browser cookie-to-Session authority request adapter.

Authentication establishes Identity continuity only. It does not establish merchant business authority.

### 2.5 WebAuthn/passkey implementation — CONFORMING_COMPLETE for IMP-04 foundation

Relevant evidence includes:

- `imp-04-webauthn-rp-policy-2026-08-28.md`;
- `imp-04-webauthn-credential-record-persistence-2026-08-28.md`;
- `imp-04-webauthn-authentication-subject-binding-conformance-2026-08-28.md`;
- `imp-04-webauthn-session-bridge-2026-08-28.md`;
- the manually approved ADR-015 authority and its governance evidence.

The implementation preserves the separation between opaque WebAuthn authentication-subject handles and Main Street semantic Identity identifiers.

### 2.6 Sessions — CONFORMING_COMPLETE for IMP-04 foundation

Relevant evidence includes:

- `imp-04-human-session-establishment-2026-08-28.md`;
- `imp-04-durable-session-record-foundation-2026-08-28.md`;
- `imp-04-session-credential-resolution-2026-08-28.md`;
- `imp-04-session-security-generation-request-validation-2026-08-28.md`;
- `imp-04-session-logout-2026-08-28.md`;
- `imp-04-identity-wide-session-revocation-2026-08-28.md`;
- `imp-04-privileged-session-cookie-2026-08-28.md`;
- `imp-04-privileged-session-request-resolution-2026-08-28.md`.

The Session Record preserves authentication continuity and security metadata but does not persist mutable merchant authority such as Controller status, role, privilege, Membership, entitlement, Merchant Scope or capability permission.

### 2.7 Trusted Execution Context — CONFORMING_COMPLETE

`SessionTrustedExecutionContextEstablisher` composes an authenticated Identity Session with an already-trusted Merchant Scope and a freshly resolved current scoped principal.

Relevant evidence:

- `imp-04-trusted-execution-session-binding-2026-08-28.md`.

### 2.8 Trusted principal — CONFORMING_COMPLETE

`MerchantControllerScopedExecutionPrincipalResolver` re-reads the current Controller relationship for the trusted Merchant Scope and requires the authenticated Identity to match that current authority before producing the scoped execution principal.

Relevant evidence:

- `imp-04-controller-principal-resolution-2026-08-28.md`.

### 2.9 Credential/security foundation — CONFORMING_COMPLETE

The foundation includes:

```text
WebAuthn/passkey authentication policy
opaque high-entropy Session Credential
one-way persistent Session verifier
durable Session Record
absolute-expiry validation
explicit revocation
Identity-wide revocation primitive
current security-generation validation port
fail-closed missing security-generation behaviour
privileged host-cookie representation
privileged cookie request extraction
credential/security metadata persistence foundation
```

This is sufficient for the IMP-04 security foundation without asserting that every later credential lifecycle, browser-hardening or production-operability policy is already implemented.

### 2.10 Baseline Audit — CONFORMING_COMPLETE

The Audit subsystem already provides durable append-oriented evidence with explicit security classifications including `AUTHENTICATION_SECURITY` and `AUTHORISATION_SECURITY`, together with PostgreSQL persistence and integration coverage.

IMP-04 requires the baseline Audit foundation, not the final complete audit portfolio or every security event producer. Detailed cross-system Audit integration remains eligible at the owning later vertical/system-hardening nodes.

---

## 3. Mandatory completion proofs

### 3.1 Identity != authority — PROVEN

```text
AuthenticationSession.identityReference
        ↓
current Merchant Scope already independently trusted
        ↓
MerchantControllerRelationshipAuthority re-read
        ↓
only current matching Controller relationship
        ↓
ExecutionPrincipal
```

A successfully authenticated Identity therefore does not become merchant authority merely by existing or possessing a valid Session.

### 3.2 Identifier != Merchant Scope — PROVEN

`MerchantScope` is a trusted execution boundary constructed only after independent trusted resolution. A client or other untrusted caller may supply an identifier/reference, but that value is not itself Merchant Scope.

### 3.3 Session != immutable merchant authority — PROVEN

The Session Record contains Identity continuity/security evidence, not durable merchant business claims. Merchant-scoped execution re-evaluates current relationship authority downstream of the Session.

A technically current Session therefore cannot preserve a former Controller relationship, role, privilege or merchant authority after the authoritative relationship changes.

---

## 4. Security-generation closure classification

`IdentitySecurityGenerationAuthority` is intentionally an authority port consumed on every Session request. The current resolver fails closed when current generation state is unavailable or differs from the Session snapshot.

The repository does **not** yet define a concrete durable owner/writer for generation creation, rotation, account recovery or security-event orchestration.

That absence does not invalidate IMP-04 completion because the macro target requires the credential/security **foundation**, and the accepted programme assigns later system-wide hardening, production wiring/operability and recovery to later macro targets. The current implementation already establishes the required architectural boundary:

```text
Session snapshot
    != current security authority

request validation
    must re-read current security authority

missing current authority
    -> fail closed
```

A future implementation node that needs to create or rotate Identity security generations MUST NOT invent that writer/ownership policy. At that point the node becomes `BLOCKED_DESIGN` unless an accepted authority has resolved it.

Accordingly this deferred lifecycle concern is retained as a future conditional design dependency, not an unresolved required IMP-04 child.

---

## 5. Explicit non-claims

IMP-04 closure does **not** claim completion of:

- final production Spring Security filter-chain composition;
- complete login/logout HTTP endpoint wiring;
- exact CSRF implementation/library;
- Origin/equivalent request-origin enforcement;
- TLS/HSTS production edge configuration;
- final CSP/Trusted Types profile;
- Controller/staff idle-timeout progression;
- step-up authentication orchestration;
- detailed authentication/security Audit event-production portfolio;
- authenticator recovery/rebinding/replacement/rotation;
- durable Identity security-generation writer/rotation/recovery policy;
- hardware-attestation policy;
- final staff device-key implementation strategy;
- customer-account-specific authentication policy;
- disaster-recovery session invalidation procedures;
- complete production operability.

These are governed by narrower ADR deferred questions, later capability/workforce work, system-wide hardening, production operability or recovery programme stages as applicable. They must not be pulled into IMP-04 merely because they are security-related.

---

## 6. Verification baseline

The latest production-behaviour implementation before this closure review is:

```text
f9de8d6e5ba0df79b9c5cb1c547b4b15184188a7
```

Maven Tests #1204 / GitHub Actions run `33177857528` verified:

```text
production Java sources              557
test Java sources                    199
unit tests                           595 PASS
PostgreSQL integration tests         183 PASS
total Maven tests                    778 PASS
failures / errors / skipped          0 / 0 / 0
Flyway migrations                    31
PostgreSQL                           18.6
BUILD SUCCESS
```

The subsequent request-resolution evidence commit:

```text
d35547319d6ebc2e099bd98d6224e638b8cf36cd
```

was independently verified by Maven Tests #1205 with SUCCESS before this closure classification was recorded.

---

## 7. Macro completion decision

The refreshed IMP-04 child graph contains no unresolved **required** child whose absence prevents the MS-IMP-001 IMP-04 scope or mandatory separation proofs from being satisfied.

Therefore:

```text
IMP-04 — Merchant Account, Scope, Identity & Trust
    = CONFORMING_COMPLETE
```

This classification does not erase deferred security work. It establishes that those narrower concerns are not required predecessors of IMP-05 under the current accepted programme.

---

## 8. Newly unlocked macro target

MS-IMP-001 defines:

```text
IMP-05
    HARD depends on IMP-03
    HARD depends on IMP-04
```

With IMP-03 already `CONFORMING_COMPLETE` and IMP-04 now `CONFORMING_COMPLETE`:

```text
IMP-05 — Merchant Definition, Configuration & Activation
    becomes READY for fine-grained dependency-graph derivation.
```

No IMP-05 production code should be selected by document order. The next step is to inspect current implementation against the exact IMP-05 scope, classify existing code, derive the smallest dependency-complete READY residual node, then resume the IMPLEMENTATION-RULES RED → GREEN loop.
