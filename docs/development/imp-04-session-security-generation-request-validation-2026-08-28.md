# IMP-04 Session Security-Generation Request Validation — Implementation Evidence

**Programme:** MS-IMP-001  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** Request-time revalidation of the Session Record security-generation snapshot against the current Identity security-generation authority  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

## Governing authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- ADR-014 — high-assurance authentication/session/trusted-browser execution;
- the existing `IdentitySecurityGenerationAuthority` contract already consumed during WebAuthn post-authentication session establishment.

ADR-014 requires a Session Record to snapshot the Identity security-generation reference established at authentication time, and requires otherwise-current authenticated requests to fail when that snapshot no longer matches the current Identity security generation.

This child deliberately does not define how the durable current generation is created, rotated or globally invalidated. It only closes the already-governed request-time comparison boundary.

## Test-first evidence

Test-first commit:

```text
79c354d2e7b6ecc1415b2011dc5fe91e8ff289d7
```

RED workflow:

```text
GitHub Actions run 33166189151
Maven Tests #1182
FAILURE as intended
```

RED characteristics:

- 555 production source files compiled successfully;
- test compilation failed only because `AuthenticationFailureCategory.SESSION_SECURITY_INVALID` and the authority-aware `SessionCredentialResolver` constructor did not yet exist;
- no existing runtime invariant had to be weakened.

The test-first contract requires:

1. an otherwise-current bearer resolves only when the Session Record security generation exactly matches the current Identity security generation;
2. the current generation is re-read on every request;
3. a changed generation invalidates an otherwise-current session;
4. a missing/blank current generation fails closed;
5. unknown bearer failure occurs before generation lookup;
6. revoked-session failure occurs before generation lookup;
7. absolute-expiry failure occurs before generation lookup;
8. the non-secret Session Identity cannot replace the bearer credential.

## Production implementation

### Failure classification

`AuthenticationFailureCategory` now includes:

```text
SESSION_SECURITY_INVALID
```

This keeps a security-generation invalidation distinguishable from an unknown bearer, ordinary absolute expiry and selected-session revocation.

### Request-time resolver

`SessionCredentialResolver` now requires:

```text
SessionRecordStore
IdentitySecurityGenerationAuthority
Clock
```

The former authority-free construction path is not retained.

For each presented bearer credential, resolution proceeds:

```text
presented opaque bearer
        ↓
one-way verifier
        ↓
current durable Session Record
        ↓
revocation check
        ↓
absolute-expiry check
        ↓
current IdentitySecurityGenerationAuthority lookup
        ↓
exact generation comparison
        ↓
AuthenticationSession continuity evidence
```

A null, blank or changed current security-generation reference fails closed as `SESSION_SECURITY_INVALID`.

The resolver does not establish Merchant Scope, Controller relationship, staff relationship, role, privilege, entitlement, capability authority or device authorisation.

## Final exact-head verification

Final implementation head:

```text
c8f9f971a7465cec0ce5133d616232758db5cc04
```

Pull request:

```text
#41 — IMP-04: revalidate session security generation on every request
```

Exact-head GREEN workflow:

```text
GitHub Actions run 33166371001
Maven Tests #1187
SUCCESS
```

Verified exact-head suite:

```text
590 unit tests
182 PostgreSQL integration tests
0 failures
0 errors
0 skipped
31 Flyway migrations
BUILD SUCCESS
```

All 8 `SessionCredentialResolverTest` tests passed.

## Merge and independent post-merge verification

PR #41 was promoted only after exact-head GREEN verification and merged with the verified head SHA pinned.

Merge commit:

```text
4df49fa388555d3d910a3d4bd470a8fecd239ffe
```

Independent `development` push workflow:

```text
GitHub Actions run 33166595420
Maven Tests #1188
event: push
head: 4df49fa388555d3d910a3d4bd470a8fecd239ffe
SUCCESS
```

The post-merge run independently reconfirmed:

```text
590 unit tests
182 PostgreSQL integration tests
0 failures
0 errors
0 skipped
31 Flyway migrations
BUILD SUCCESS
```

Post-merge `SessionCredentialResolverTest` again ran 8 tests with zero failures/errors/skips.

## Invariants proven by this child

```text
Session Record security-generation snapshot ≠ current security-generation authority
current Identity security generation is re-read for every otherwise-current request
changed generation invalidates existing session continuity
missing/blank current generation fails closed
revoked and absolutely expired sessions reject before generation lookup
Session Identity ≠ bearer credential
session continuity ≠ Merchant Scope or business authority
```

## Explicit non-claims

This child does not claim to implement or complete:

- durable Identity security-generation persistence ownership;
- security-generation rotation/increment orchestration;
- account-recovery/global-session invalidation workflow;
- idle-timeout activity progression;
- step-up authentication orchestration;
- authentication-security Audit wiring;
- browser/security-filter-chain delivery;
- CSRF/trusted-browser cookie policy;
- high-risk hardware-attestation policy;
- staff operational device-key specifics;
- complete IMP-04 closure.

Those remain subject to the refreshed IMP-04 dependency graph and applicable accepted authority.

> **Request-time Session security-generation revalidation is CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
