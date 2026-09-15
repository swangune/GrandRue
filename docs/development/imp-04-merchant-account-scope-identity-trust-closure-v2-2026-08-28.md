# IMP-04 — Merchant Account, Scope, Identity & Trust — Replacement Closure Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Date:** 28 August 2026  
**Status:** **CURRENT — CONFORMING_COMPLETE**  
**Verified production head:** `abe4a30c17e89485f8c8c864c81b76e43878eacd`  

This is the fresh closure review required after the invalid earlier closure and its correction. It does not revive the reasoning that treated a missing writer as optional.

## 1. Governing completion gate

MS-IMP-001 requires Merchant Account, Controller establishment, Merchant Scope, authentication, WebAuthn/passkeys, Sessions, Trusted Execution Context, trusted principal, credential/security foundation and baseline Audit, with executable proof that:

```text
identity != authority
identifier != Merchant Scope
session != immutable merchant authority
```

Every required child must have accepted authority, minimum conforming implementation, executable evidence, full verification and traceable commits.

## 2. Fresh required-child review

| Required IMP-04 child | Classification | Current evidence |
|---|---|---|
| Merchant Account | CONFORMING_COMPLETE | Durable merchant-account model/persistence |
| Controller establishment | CONFORMING_COMPLETE | Atomic account/bootstrap Controller relationship evidence |
| Merchant Scope | CONFORMING_COMPLETE | Trusted `MerchantScope` boundary |
| Authentication | CONFORMING_COMPLETE | WebAuthn proof policy, subject binding and Identity continuity |
| WebAuthn/passkey foundation | CONFORMING_COMPLETE | RP policy, durable credential/subject persistence, Session bridge |
| Sessions | CONFORMING_COMPLETE | Opaque credentials, durable records, expiry, logout/revocation and request-time validation |
| Trusted Execution Context | CONFORMING_COMPLETE | Session/Scope/current-principal composition |
| Trusted principal | CONFORMING_COMPLETE | Current Controller relationship re-read |
| Credential/security foundation | CONFORMING_COMPLETE | 12-hour maximum, durable Identity security generation, optimistic rotation and fail-closed request comparison |
| Baseline Audit | CONFORMING_COMPLETE | Durable append store plus committed authentication-security rotation producer |

The corrected Identity security-generation child is proven by `imp-04-identity-security-generation-authority-2026-08-28.md`.

## 3. Mandatory separation proofs

- **Identity is not authority:** authentication yields Identity continuity; current merchant-scoped Controller authority is independently re-read before an execution principal is established.
- **Identifier is not Merchant Scope:** untrusted merchant identifiers do not construct the trusted `MerchantScope` boundary.
- **Session is not immutable merchant authority:** Session state carries Identity/security continuity only; current scoped authority is resolved downstream on each protected execution.
- **Credential/session state does not own security generation:** the Identity Security context owns the current generation; Sessions retain snapshots only.
- **Rotation does not mutate merchant authority:** Identity-wide invalidation revokes the Identity's Sessions without changing Merchant Account or Controller relationships.

## 4. Residual classification

The earlier closure's explicit non-claims were re-examined. Final Spring Security filter-chain/HTTP composition, exact CSRF library/mechanism, Origin enforcement, TLS/HSTS edge configuration, final CSP/Trusted Types, staff device-key strategy, later recovery ceremonies, disaster recovery and complete production operability remain assigned to narrower accepted authorities or later IMP-08B/IMP-16/IMP-17/IMP-18/IMP-19 work.

They remain real deferred work. They are not required predecessors of IMP-05 under the accepted MS-IMP-001 IMP-04 foundation scope.

The former required blocker is different: Identity security-generation ownership and mutation were consumed by the IMP-04 Session path itself. ADR-014 v1.1 and its conforming implementation now close that child.

## 5. Verification

Production behavior at `abe4a30c17e89485f8c8c864c81b76e43878eacd` passed Maven Tests #1279 / run `33209674797`:

```text
586 production Java sources
222 test Java sources
638 unit tests PASS
191 PostgreSQL integration tests PASS
829 total Maven tests PASS
32 Flyway migrations
0 failures / 0 errors / 0 skipped
BUILD SUCCESS
```

The governance transition is independently protected by `ImplementationProgrammeGateConformanceTest`, which requires the correction, child conformance evidence, this replacement closure and current programme navigation to agree.

## 6. Macro decision

The fresh IMP-04 graph has **no unresolved required child**. All required children satisfy the accepted completion gate and all three mandatory separation proofs remain executable.

```text
IMP-04 — Merchant Account, Scope, Identity & Trust
    = CONFORMING_COMPLETE
```

The invalid first closure remains superseded. The correction remains preserved as evidence of the prior defect and blocker.

## 7. Dependency consequence

MS-IMP-001 defines IMP-05 as HARD-dependent on IMP-03 and IMP-04. Both are now `CONFORMING_COMPLETE`.

```text
IMP-05 — Merchant Definition, Configuration & Activation
    = READY
```

READY does not mean complete. The next implementation action is to inspect existing code against the exact IMP-05 scope, derive its fine-grained dependency graph and select the smallest residual READY child.
