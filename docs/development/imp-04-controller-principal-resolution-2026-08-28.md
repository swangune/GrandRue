# IMP-04 Current Merchant Controller Principal Resolution — Implementation Evidence

**Programme:** MS-IMP-001  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** Current Merchant Controller relationship → scoped attributable execution principal  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

## Governing boundary

MS-PROT-063 and ADR-014 require an authenticated Identity to be re-evaluated against the current merchant relationship before a Merchant-scoped Trusted Execution Context is established. Controller relationship must remain distinct from Identity, Session and operation authorisation.

This child preserves:

```text
authenticated Identity
        ↓
already-resolved Merchant Scope
        ↓
current active Controller relationship
        ↓
Execution Principal (attribution only)

Execution Principal
    ≠ Controller privilege
    ≠ role claim
    ≠ entitlement
    ≠ frozen Merchant authority
```

## Test-first evidence

```text
2a97fdba3fd063b9e1f0f4cc7bac9b24d2d855fd
test: specify current Controller principal resolution
```

Tests prove:

- an authenticated identity establishes a principal only when it exactly matches the current active Controller relationship for the Merchant Scope;
- another identity cannot inherit the Controller relationship;
- absence of a current Controller relationship fails principal establishment rather than manufacturing authority;
- Controller transfer/current-relationship replacement is observed on the next resolution, so the former Controller identity stops establishing the contextual Controller principal without requiring its unrelated Identity Session to be destroyed.

## Production implementation

```text
17b63aa1e2c0a576f6263547c6911704f712e36c
feat: expose current Controller relationship authority

f119a7c1c073c8decf8a4d6ac3751cfe29e73d32
refactor: expose lifecycle store through Controller read authority

69d5695540089e1ef110844f1ae59d4d6849f878
feat: resolve current Merchant Controller execution principal
```

Production types/boundaries:

- `MerchantControllerRelationshipAuthority` — narrow current relationship read authority;
- `MerchantAccountLifecycleStore` — continues to own lifecycle persistence/consistency while exposing its existing `activeController(scope)` read through the narrow authority;
- `MerchantControllerScopedExecutionPrincipalResolver` — performs current relationship revalidation and returns only an attributable `ExecutionPrincipal`.

No Controller role or privilege is copied into the principal.

## Verification

Pull request:

```text
#30 — IMP-04: resolve current Merchant Controller principals
head: 69d5695540089e1ef110844f1ae59d4d6849f878
```

Pre-merge verification:

```text
GitHub Actions run 33136584489
Maven Tests #1125
SUCCESS
```

Merge:

```text
c3af6a1670b849809e734ca1e48bd9bf82dedff2
```

Post-merge verification:

```text
GitHub Actions run 33136705060
Maven Tests #1126
head: c3af6a1670b849809e734ca1e48bd9bf82dedff2
SUCCESS
```

## Explicit non-claims

This child does not resolve an untrusted merchant identifier from HTTP/transport input, does not implement WebAuthn/passkey authentication, does not treat Merchant Account suspension or commercial eligibility as authentication state, and does not grant operation authorisation.

> **Current Merchant Controller relationship → attributable scoped principal resolution is CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
