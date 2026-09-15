# IMP-04 Durable Session Logout — Implementation Evidence

**Programme:** MS-IMP-001  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-014 durable logout / selected Session Record revocation  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

## Governing boundary

ADR-014 Section 47 requires logout to revoke the selected Session Record. Once revocation commits, the corresponding bearer credential must no longer establish trusted continuity. Loss of the logout acknowledgement must not reactivate the session.

This child preserves:

```text
logout
    → selected Session Record revocation

logout
    ≠ Identity deletion
    ≠ Merchant Scope change
    ≠ merchant relationship change
    ≠ role / privilege mutation
```

## Test-first evidence

```text
2aed7760c598c7479065f5ae08516678df751b00
test: specify durable session logout semantics
```

Tests prove:

- the presented bearer selects exactly one Session Record through its verifier;
- logout durably revokes that selected session;
- the existing `SessionCredentialResolver` rejects the credential after revocation;
- a second session for the same Identity remains independent;
- retry after acknowledgement loss preserves the original revocation timestamp/reason rather than rewriting evidence;
- an unknown bearer is classified as `AUTHENTICATION_FAILED` without mutating another Session Record.

Existing `JooqSessionRecordStoreIT` already proves that persisted logout revocation survives store recreation and cannot be silently rewritten with different revocation evidence.

## Production implementation

```text
6f89be3e35dcc68f024be915f76e67213faa99d2
feat: add durable opaque-session logout
```

Production type:

```text
mainstreet.runtime.SessionLogoutService
```

The service derives the verifier from the presented bearer, resolves the durable Session Record, returns an already-revoked record unchanged on retry, or commits `logout` revocation for the selected current record.

## Verification

Pull request:

```text
#28 — IMP-04: add durable opaque-session logout
head: 6f89be3e35dcc68f024be915f76e67213faa99d2
```

Pre-merge verification:

```text
GitHub Actions run 33135946153
Maven Tests #1119
SUCCESS
```

Merge:

```text
1db6a5a7b6ca9a45be6cbfcae468c87051786c64
```

Post-merge verification:

```text
GitHub Actions run 33136061900
Maven Tests #1120
head: 1db6a5a7b6ca9a45be6cbfcae468c87051786c64
SUCCESS
```

## Explicit non-claims

This child does not implement browser cookie clearing/transport, global Identity-wide revocation, WebAuthn/passkey ceremony, CSRF, idle timeout/activity progression, security-generation invalidation, credential rotation, step-up authentication or recovery.

> **Durable selected-session logout is CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
