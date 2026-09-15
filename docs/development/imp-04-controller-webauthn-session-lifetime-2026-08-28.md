# IMP-04 Controller WebAuthn Session Absolute-Lifetime Bound — Implementation Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-014 initial Controller WebAuthn Session absolute-lifetime ceiling  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child; IMP-04 remains PARTIALLY_CONFORMING**

---

## 1. Governing authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- ADR-014 — High-Assurance Authentication, Session & Trusted Browser Execution Architecture; and
- ADR-015 — WebAuthn/Passkey Production Adapter Decision.

ADR-014 fixes the initial Merchant Controller absolute Session lifetime at 12 hours, permits security policy to shorten it and permits lengthening only under a future approved security policy. ADR-015 identifies the initial adapter path as passkey-first Controller authentication.

No unresolved owner, persistence or recovery decision is needed to enforce that already-accepted configuration ceiling.

---

## 2. Fine-grained dependency decision

After correcting the IMP-04 macro graph, this was the smallest independent READY child:

```text
initial Controller WebAuthn adapter exists
        +
absolute lifetime is already supplied by policy
        +
bridge already derives absoluteExpiry from that policy
        +
ADR-014 fixes the maximum at 12 hours
        ↓
reject non-conforming policy before Session establishment
```

This child is independent of the blocked Identity security-generation writer because it validates configuration before any Session Record mutation.

---

## 3. Test-first evidence

RED commit:

```text
9329d0289a3885bf34c86833a52cedbab9fac40d
test(imp-04): bound Controller WebAuthn session lifetime
```

RED workflow:

```text
Maven Tests #1272
GitHub Actions run 33207850033
FAILURE as intended
634 tests run
1 failure
0 errors
0 skipped
```

The sole failure was `WebAuthnSessionEstablishmentPolicyTest.controller_session_absolute_lifetime_cannot_exceed_twelve_hours`: the existing policy accepted 12 hours plus one second.

The test also proves that exactly 12 hours and a shorter eight-hour lifetime remain valid.

---

## 4. Minimum production implementation

Implementation commit:

```text
58e5ef426908d335ce5e08f0abf435b130098d8a
fix(imp-04): enforce Controller session lifetime ceiling
```

`WebAuthnSessionEstablishmentPolicy` now rejects any configured absolute lifetime greater than `PT12H`.

The existing positive-duration validation remains intact. The bridge continues to calculate `absoluteExpiry` from the accepted bounded lifetime, and no authority, Merchant Scope, role, privilege or entitlement is added to the Session.

---

## 5. Exact-head GREEN verification

```text
Maven Tests #1273
GitHub Actions run 33207958933
SUCCESS
```

Verified suite:

```text
production Java sources              577
test Java sources                    218
unit tests                           634 PASS
PostgreSQL integration tests         183 PASS
total Maven tests                    817 PASS
failures / errors / skipped          0 / 0 / 0
Flyway migrations                    31
PostgreSQL                           18.6
BUILD SUCCESS
```

---

## 6. Invariants proven

```text
Controller WebAuthn Session absolute lifetime <= 12 hours
shorter accepted security policy remains valid
future unapproved lengthening fails before Session establishment
Session lifetime policy != Merchant Scope
Session lifetime policy != merchant authority
```

---

## 7. Explicit non-claims and residual graph

This child does not implement:

- idle-timeout activity progression;
- staff operational-context idle locking;
- step-up authentication;
- CSRF mechanics;
- recovery or authenticator replacement;
- Identity security-generation creation/rotation; or
- IMP-04 macro closure.

The required Identity security-generation owner/writer remains `BLOCKED_DESIGN` under `imp-04-macro-closure-correction-2026-08-28.md`. IMP-05 therefore remains blocked.
