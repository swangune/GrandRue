# IMP-04 Privileged Browser Session Cookie — Implementation Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-014 privileged browser Session Credential cookie contract  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

---

## 1. Governing authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- ADR-014 — High-Assurance Authentication, Session & Trusted Browser Execution Architecture.

ADR-014 fixes the default privileged first-party browser Session Credential transport contract as a hardened host-bound cookie:

```text
name        __Host-MS-SESSION
Secure      true
HttpOnly    true
SameSite    Strict
Path        /
Domain      absent
```

The `__Host-` model and absence of a `Domain` attribute preserve host-bound credential isolation. The raw bearer is transport material only; it does not establish Merchant Scope, role, privilege, entitlement, device authority or capability authority.

ADR-014 separately defers the exact CSRF implementation/library and exact CSP/Trusted Types hardening profile. This child therefore does not invent either policy.

---

## 2. Fine-grained dependency decision

After completion of the Identity-wide Session revocation primitive, the IMP-04 residual graph was refreshed.

The privileged Session cookie contract was selected as the smallest READY child because:

```text
accepted cookie contract is explicit
        +
OpaqueSessionCredential already exists
        +
Spring ResponseCookie is already available
        +
no new persistence or business authority is required
        ↓
minimum transport representation can be implemented without new semantics
```

This child does not assert that the complete Spring Security/browser filter chain is implemented. It materialises only the accepted cookie representation required by that later delivery boundary.

---

## 3. Test-first evidence

Test-first commit:

```text
784c776754673233380a25a497768703c5474638
```

Commit message:

```text
test: require hardened privileged session cookie contract
```

RED workflow:

```text
GitHub Actions run 33176979759
Maven Tests #1200
FAILURE as intended
```

The build failed during test compilation because `PrivilegedSessionCookieFactory` did not yet exist. This is the exact missing production abstraction the falsifying test was designed to expose.

The test requires that an issued privileged Session cookie:

1. is named `__Host-MS-SESSION`;
2. carries the already-issued opaque Session Credential value;
3. uses `Path=/`;
4. has no `Domain` attribute;
5. is `Secure`;
6. is `HttpOnly`;
7. uses `SameSite=Strict`.

No production code existed for this abstraction before the RED proof.

---

## 4. Minimum production implementation

Implementation commit:

```text
c4150ca2d374d8c3a65efdce35c8300c53ddbb31
```

Commit message:

```text
feat: implement hardened privileged session cookie contract
```

Added:

```text
src/main/java/mainstreet/infrastructure/security/session/PrivilegedSessionCookieFactory.java
```

The factory accepts an already-issued `OpaqueSessionCredential` and produces a Spring `ResponseCookie` with only the accepted ADR-014 transport attributes.

It does not:

```text
authenticate a human
create Session authority
resolve Merchant Scope
resolve role / privilege / entitlement
define CSRF mechanics
define browser routing
define session expiry
rotate credentials
write persistence
```

No Flyway migration or new semantic concept was introduced.

---

## 5. Exact-head GREEN verification

Verified implementation head:

```text
c4150ca2d374d8c3a65efdce35c8300c53ddbb31
```

Exact-head GREEN workflow:

```text
GitHub Actions run 33177153610
Maven Tests #1201
SUCCESS
```

Verified suite:

```text
Temurin Java                         25.0.4+1
production Java sources              556
test Java sources                    198
unit tests                           591 PASS
PostgreSQL integration tests         183 PASS
total Maven tests                    774 PASS
failures / errors / skipped          0 / 0 / 0
Flyway migrations                    31
PostgreSQL                           18.6
BUILD SUCCESS
```

`PrivilegedSessionCookieFactoryTest` ran 1 test with zero failures/errors/skips.

---

## 6. Invariants proven by this child

```text
privileged browser Session Credential
    transported as __Host-MS-SESSION

privileged Session cookie
    is Secure
    is HttpOnly
    is SameSite=Strict
    has Path=/
    has no Domain

raw opaque Session Credential
    may enter the browser cookie transport
    but is not persisted by this factory
    and is not business authority

cookie possession
    != Merchant Scope
    != merchant relationship
    != role
    != privilege
    != entitlement
    != capability authority
```

The implementation therefore preserves ADR-014's required separation between Session Credential and merchant/business authority.

---

## 7. Explicit non-claims and residual dependencies

This child does **not** claim to implement or complete:

- actual HTTP response/filter-chain cookie issuance;
- request-time cookie extraction;
- logout cookie clearing;
- CSRF token/proof mechanics;
- Origin/equivalent request-origin validation;
- privileged-host/custom-domain routing enforcement beyond the cookie representation itself;
- TLS/HSTS edge configuration;
- CSP/Trusted Types browser hardening;
- idle-timeout progression;
- step-up authentication orchestration;
- authentication/session Audit wiring;
- durable `IdentitySecurityGenerationAuthority` persistence ownership;
- security-generation creation/rotation/recovery orchestration;
- WebAuthn recovery/rebinding/replacement/rotation;
- hardware-attestation policy;
- staff device-key implementation specifics;
- complete IMP-04 closure.

The durable Identity security-generation writer/rotation boundary remains a **DESIGN_ESCALATION / not-READY dependency** until accepted authority determines ownership and mutation semantics sufficiently for implementation.

> **The ADR-014 privileged browser Session cookie representation is CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
