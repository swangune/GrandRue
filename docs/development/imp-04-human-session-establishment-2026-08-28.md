# IMP-04 Human Session Establishment & Fixation Protection — Implementation Evidence

**Programme:** MS-IMP-001  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-014 post-authentication human Session establishment / fixation protection foundation  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

## Governing boundary

ADR-014 requires successful human authentication to establish a fresh independent opaque Session Credential and forbids upgrading an attacker-influenced pre-authentication session identifier into authenticated continuity.

This child begins only after an upstream authentication mechanism has produced trusted identity/security evidence. It does not verify WebAuthn or select an authentication provider/library.

Canonical child boundary:

```text
trusted post-authentication identity/security evidence
        ↓
service-owned fresh Session Identity
        +
service-generated fresh 256-bit opaque bearer
        ↓
credential verifier only persisted in durable Session Record
        ↓
raw bearer returned once to the transport/security adapter
```

## Test-first evidence

```text
23844f9cdcbeea991fb991b4e6ae026ddcd0477c
test: specify fresh human session establishment
```

Tests prove:

- repeated successful authentication evidence produces distinct Session Identities;
- each establishment produces an independent opaque bearer;
- persisted Session Records retain identity, authentication time, assurance/method, absolute expiry, initial activity timestamp and security-generation reference;
- persistent lookup uses the credential verifier, not the raw bearer;
- the establishment result never renders the raw bearer in `toString()`;
- invalid security/session evidence fails before a Session Record is committed.

Tests inject deterministic `SecureRandom` and deterministic Session Identity generation while production uses platform cryptographic randomness and UUID Session Identity generation.

## Production implementation

```text
0eb00a7130dabd9e5036fee585a66e148701504f
feat: add redacted established human session result

15be1244a2f0f3733259eff9740a5a773918bfd1
feat: establish fresh server-authoritative human sessions
```

Production types:

```text
mainstreet.runtime.EstablishedHumanSession
mainstreet.runtime.HumanSessionEstablishmentService
```

`HumanSessionEstablishmentService` does not accept a caller-supplied/pre-auth Session Identity. It creates the new Session Identity and bearer itself, persists only the bearer verifier, and returns a redacted result containing the raw bearer for the subsequent hardened transport boundary.

## Verification

Pull request:

```text
#29 — IMP-04: establish fresh server-authoritative human sessions
head: 15be1244a2f0f3733259eff9740a5a773918bfd1
```

Pre-merge verification:

```text
GitHub Actions run 33136238967
Maven Tests #1122
SUCCESS
```

Merge:

```text
6ff192cbbd105dbd5e42a2c15c632ce8056e1ff2
```

Post-merge verification:

```text
GitHub Actions run 33136355337
Maven Tests #1123
head: 6ff192cbbd105dbd5e42a2c15c632ce8056e1ff2
SUCCESS
```

## Explicit non-claims

This child does not implement or select WebAuthn/passkey verification. It does not define browser cookies, CSRF, Controller/staff idle-timeout enforcement, activity progression, current security-generation invalidation, global Identity-wide revocation, step-up/credential rotation, account recovery or merchant/business authority.

> **Post-authentication human Session establishment and fixation protection are CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
