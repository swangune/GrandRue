# IMP-04 Opaque Session Credential Resolution — Implementation Evidence

**Programme:** MS-IMP-001  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-014 opaque bearer credential → current server-authoritative session resolution  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

## Governing boundary

ADR-014 distinguishes the non-secret Session Identity from the bearer Session Credential. Possession of the Session Identity is not authentication proof. The browser presents the raw opaque credential; server-side verification derives the stored verifier and resolves the durable Session Record.

This child preserves:

```text
Session Credential
    ≠ Session Identity
    ≠ Merchant Scope
    ≠ business authority
```

## Test-first evidence

```text
e3f63d48e42170d53bd8e5d65a338e9a50257708
test: specify opaque credential session resolution
```

Tests prove:

- a presented bearer resolves only through its one-way verifier;
- Session Identity is rejected when presented as a bearer secret;
- unknown credentials fail closed;
- revoked Session Records fail closed;
- absolute expiry fails closed;
- successful resolution returns only session/identity continuity.

## Production implementation

```text
9238f83c98c9e0bc91b539caaa824eb3758bd9b4
feat: derive verifier from presented session credential

f2205283a21286ddae46e411bd3de7e3dc66d387
feat: resolve current session from opaque bearer credential
```

Production type:

```text
mainstreet.runtime.SessionCredentialResolver
```

`OpaqueSessionCredential.verifierForPresentedCredential(...)` derives the same SHA-256 verifier used by persistent Session Records. Derivation alone grants nothing; the verifier must resolve to a current authoritative Session Record.

## Verification

Pull request:

```text
#26 — IMP-04: resolve opaque bearer credentials to current sessions
head: f2205283a21286ddae46e411bd3de7e3dc66d387
```

Pre-merge verification:

```text
GitHub Actions run 33134950826
Maven Tests #1113
SUCCESS
```

Merge:

```text
6b209f79b50e28019615ce746803c29d428d0263
```

Post-merge verification:

```text
GitHub Actions run 33135070306
Maven Tests #1114
head: 6b209f79b50e28019615ce746803c29d428d0263
SUCCESS
```

## Explicit non-claims

This child does not yet replace the older identifier-based internal trusted-context seam. It does not implement idle activity progression, current security-generation invalidation, cookies, CSRF, WebAuthn ceremonies/library selection, step-up authentication, recovery or merchant relationship/device authority.

> **Opaque credential → current durable session resolution is CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
