# IMP-04 Privileged WebAuthn Relying-Party Policy — Implementation Evidence

**Programme:** MS-IMP-001  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-015 privileged WebAuthn relying-party policy and coherent Spring security platform baseline  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

## Governing authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- accepted ADR-015 privileged WebAuthn relying-party policy;
- ADR-014 boundaries that keep authentication/session continuity distinct from Merchant Scope and business authority.

The implementation preserves the security boundary:

```text
WebAuthn ceremony proof
    ≠ Spring HttpSession authority
    ≠ Spring SecurityContext business authority
    ≠ Merchant Scope
    ≠ Controller/staff authorisation
```

## Implemented policy

Production WebAuthn relying-party policy now requires:

- an explicit relying-party identifier;
- an explicit relying-party display name;
- an explicit privileged-authentication origin allow-list;
- HTTPS-only configured production origins;
- no implicit admission of merchant storefront or custom-domain origins;
- `UserVerificationRequirement.REQUIRED` for passwordless Controller authentication request options;
- `UserVerificationRequirement.REQUIRED` in registration authenticator selection;
- construction of Spring `Webauthn4JRelyingPartyOperations` from the explicit policy without treating Spring session/security-context state as Main Street business authority.

Production types:

```text
mainstreet.infrastructure.security.webauthn.PrivilegedWebAuthnRelyingPartyPolicy
mainstreet.infrastructure.security.webauthn.SpringWebAuthnRelyingPartyOperationsFactory
```

## Approved Spring platform security baseline

The initial implementation exposed a dependency-security compatibility decision.

Spring Boot 4.1.0 managed Spring Security 7.1.0. A manual security approval first authorised Spring Security 7.1.1. Verification then showed that a Security-only 7.1.1 override expected Spring Framework 7.0.9+ while Boot 4.1.0 managed Framework 7.0.8.

A second manual approval on 28 August 2026 authorised the coherent platform correction:

```text
Spring Boot      4.1.1
Spring Framework 7.0.9
Spring Security  7.1.1
```

The temporary `spring-security.version` override was removed. Spring Boot 4.1.1 again owns dependency management for the Spring platform.

Executable regression proof now requires both:

```text
SpringVersion.getVersion() == 7.0.9
SpringSecurityCoreVersion.getVersion() == 7.1.1
```

This prevents silent regression to the previously rejected dependency combination.

## Test-first evidence

Initial RED workflow:

```text
GitHub Actions run 33157149498
FAILED during test compilation because the production RP policy/factory did not yet exist
```

The final policy tests prove:

- insecure/non-HTTPS origins are rejected;
- the privileged origin set must be explicit and non-empty;
- merchant/custom-domain origins are not implicitly admitted;
- configured origins are defensively copied and immutable after policy construction;
- user verification is REQUIRED;
- Spring relying-party operations receive the exact RP identifier and verification policy;
- the approved Spring Framework/Security dependency baseline is active.

## Pull-request verification

Pull request:

```text
#35 — IMP-04: enforce explicit privileged WebAuthn RP policy
head: 9b26dd5248a0aae2332e690ccbcc921134d6b83c
```

Final exact-head pre-merge verification:

```text
GitHub Actions run 33158660729
Maven Tests #1159
SUCCESS
```

Verified suite:

```text
587 unit tests
173 PostgreSQL integration tests
0 failures
0 errors
0 skipped
29 Flyway migrations
BUILD SUCCESS
```

The run confirmed Spring Security Core 7.1.1 and Spring Boot 4.1.1; the prior Spring Framework compatibility warning was absent.

## Merge and post-merge verification

PR #35 was merged only after exact-head GREEN verification.

Merge commit:

```text
3a8e8609287dfddbd955717a7d855eb6cb49dd82
```

Post-merge `development` verification:

```text
GitHub Actions run 33158912342
Maven Tests #1160
event: push
head: 3a8e8609287dfddbd955717a7d855eb6cb49dd82
SUCCESS
```

Post-merge suite independently confirmed:

```text
587 unit tests
173 PostgreSQL integration tests
0 failures
0 errors
0 skipped
29 Flyway migrations
Spring Boot 4.1.1
Spring Security Core 7.1.1
BUILD SUCCESS
```

## Invariants proven by this child

```text
privileged WebAuthn origin admission is explicit
merchant storefront domain ≠ privileged authentication origin
passwordless Controller authentication requires user verification
WebAuthn relying-party configuration ≠ Merchant Scope
Spring authentication/session machinery ≠ Main Street business authority
security patch selection remains dependency-compatible at the approved platform baseline
```

## Explicit non-claims

This child does not claim to complete:

- durable WebAuthn credential persistence;
- HTTP filter-chain/browser delivery integration;
- trusted-browser cookie transport policy;
- CSRF implementation;
- Merchant Scope resolution;
- Controller relationship/authority establishment;
- recovery;
- step-up orchestration;
- high-risk hardware-attestation policy;
- complete IMP-04 closure.

Those remain subject to the refreshed IMP-04 dependency graph and applicable accepted authority.

> **Privileged WebAuthn relying-party policy and its coherent Spring security platform baseline are CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
