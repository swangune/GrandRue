# IMP-04 Privileged Browser Session Request Resolution — Implementation Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-014 privileged browser Session Credential request resolution  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains subject to macro closure review**

---

## 1. Governing authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- ADR-014 — High-Assurance Authentication, Session & Trusted Browser Execution Architecture.

ADR-014 establishes that the privileged human browser credential is an opaque Session Credential transported in the host-bound `__Host-MS-SESSION` cookie and that possession of that bearer does not itself establish Merchant Scope or business authority.

The server-side Session Record remains authoritative. If authoritative session state cannot be established, privileged authenticated execution fails closed.

---

## 2. Fine-grained dependency decision

After completion of the privileged Session cookie representation, the IMP-04 residual graph was refreshed.

The request-resolution adapter was selected as the smallest READY child because:

```text
privileged browser cookie representation exists
        +
server-authoritative SessionCredentialResolver exists
        +
request cookie extraction is infrastructure adaptation
        +
no Merchant Scope / role / privilege semantics are required
        ↓
request bearer can be handed to existing Main Street session authority
without choosing a complete endpoint/filter-chain architecture
```

This child deliberately does not introduce a `SecurityFilterChain`, endpoint routing, CSRF mechanism or merchant/business authority.

---

## 3. Test-first evidence

Test-first commit:

```text
1d177cd2e89b5013e43dcdb5b93199a201163737
```

Commit message:

```text
test: require privileged session request resolution
```

RED workflow:

```text
GitHub Actions run 33177757536
Maven Tests #1203
FAILURE as intended
```

The build reached test compilation after successfully compiling existing production sources and then failed with four `cannot find symbol` errors because `PrivilegedSessionRequestResolver` did not yet exist. No unrelated production failure caused the RED result.

The falsifying contract requires:

1. the exact `__Host-MS-SESSION` cookie to be delegated to `SessionCredentialResolver`;
2. unrelated cookies to have no authority;
3. a missing privileged cookie to fail closed before session-authority lookup;
4. an HTTP `Authorization` header or request parameter not to substitute for the privileged browser cookie at this boundary;
5. a blank privileged cookie to fail closed before session-authority lookup;
6. duplicate privileged cookies to fail closed as ambiguous credential evidence.

---

## 4. Minimum production implementation

Implementation commit:

```text
f9de8d6e5ba0df79b9c5cb1c547b4b15184188a7
```

Commit message:

```text
feat: resolve privileged browser session from host cookie
```

Added:

```text
src/main/java/mainstreet/infrastructure/security/session/PrivilegedSessionRequestResolver.java
```

The adapter:

```text
HttpServletRequest
        ↓
find exactly one __Host-MS-SESSION cookie
        ↓
reject missing / blank / duplicate evidence
        ↓
pass raw presented bearer to SessionCredentialResolver
        ↓
existing server-authoritative Session Record validation
        ↓
AuthenticationSession continuity evidence
```

It does not inspect or trust HTTP authorization headers, request parameters or Spring authority state as an alternative privileged browser Session Credential source.

It does not establish:

```text
Merchant Scope
Controller relationship
Merchant Membership
role
privilege
Commercial Entitlement
device authority
capability authority
```

---

## 5. Exact-head GREEN verification

Verified implementation head:

```text
f9de8d6e5ba0df79b9c5cb1c547b4b15184188a7
```

Exact-head GREEN workflow:

```text
GitHub Actions run 33177857528
Maven Tests #1204
SUCCESS
```

Verified suite:

```text
Temurin Java                         25.0.4+1
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

Relevant focused tests included:

```text
PrivilegedSessionRequestResolverTest     4 PASS
PrivilegedSessionCookieFactoryTest       1 PASS
SpringWebAuthnSessionBridgeTest           4 PASS
SessionCredentialResolverTest            8 PASS
```

Architecture/governance conformance tests also remained green.

---

## 6. Invariants proven by this child

```text
browser privileged Session Credential source
    = exact __Host-MS-SESSION cookie at this adapter

missing credential evidence
    → fail closed

blank credential evidence
    → fail closed

duplicate credential evidence
    → fail closed

Authorization header / request parameter
    ≠ substitute privileged browser Session Credential

cookie extraction
    → SessionCredentialResolver
    → current durable Session authority

cookie possession
    ≠ Merchant Scope
    ≠ merchant relationship
    ≠ role
    ≠ privilege
    ≠ entitlement
    ≠ capability authority
```

This preserves ADR-014's distinction between browser bearer transport, authenticated Identity continuity and downstream current business authority.

---

## 7. Explicit non-claims and residual dependencies

This child does **not** claim to implement or complete:

- a complete Spring Security `SecurityFilterChain`;
- HTTP endpoint selection or routing;
- successful-login response cookie issuance wiring;
- logout response cookie clearing;
- privileged-host/custom-domain routing enforcement beyond the host-cookie representation itself;
- CSRF token/proof mechanics;
- Origin/equivalent request-origin validation;
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
- IMP-04 macro completion.

Several of those concerns have separate accepted/deferred design boundaries and must not be implemented merely to broaden this child.

> **Privileged browser Session request resolution is CONFORMING_COMPLETE for this child scope. The next action is a formal IMP-04 macro conformance/closure review, not automatic expansion into an adjacent security feature.**
