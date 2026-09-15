# IMP-04 Opaque Session Credential → Trusted Execution Binding — Implementation Evidence

**Programme:** MS-IMP-001  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-014 opaque bearer credential → current authenticated session → Trusted Execution Context  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

## Governing boundary

ADR-014 distinguishes the non-secret Session Identity from the bearer Session Credential and requires current authoritative session resolution before Merchant Scope / relationship / device / principal establishment.

This child preserves:

```text
Session Credential
    ≠ Session Identity
    ≠ Merchant Scope
    ≠ merchant relationship
    ≠ actor authorisation
```

A presented opaque bearer is resolved through `SessionCredentialResolver` before scoped principal establishment. Merchant membership, Merchant Operational Device Authorisation and downstream actor authorisation remain current independent authorities.

## Production implementation

Verified implementation head:

```text
a4e94387ddda356054581346c86723d544a28dae
refactor: remove identifier-based session authority seam
```

Production changes:

- `SessionTrustedExecutionContextEstablisher` now consumes a presented opaque session credential through `SessionCredentialResolver` before resolving the current scoped execution principal;
- `StaffOperationalTrustedExecutionContextEstablisher` uses the same bearer-resolution path before current Merchant Membership and Merchant Operational Device Authorisation checks;
- the superseded identifier-based `AuthenticationSessionAuthority` seam is removed rather than retained as a parallel authentication path;
- Session Identity remains non-secret provenance/audit evidence only.

## Test evidence

Tests prove:

- a valid bearer establishes authenticated continuity before scoped-principal resolution;
- revocation removes continuity immediately;
- the literal non-secret Session Identity cannot replace the bearer credential;
- staff operation still requires current Merchant Membership and current Merchant Operational Device Authorisation after session resolution;
- device authority for another Merchant Scope cannot establish staff execution context.

## Verification

Pull request:

```text
#27 — IMP-04: bind trusted execution to opaque session credentials
head: a4e94387ddda356054581346c86723d544a28dae
```

Pre-merge verification:

```text
GitHub Actions run 33135589490
Maven Tests #1116
SUCCESS
```

Merge:

```text
ee3369cc6313e8abbf2869c4917eb96658ecbbda
```

Post-merge verification:

```text
GitHub Actions run 33135696256
Maven Tests #1117
head: ee3369cc6313e8abbf2869c4917eb96658ecbbda
SUCCESS
```

## Explicit non-claims

This child does not implement WebAuthn/passkey ceremony or select a WebAuthn dependency/provider. It does not implement host-cookie transport, CSRF, session idle progression, security-generation invalidation, step-up authentication, account recovery or browser hardening. Those remain separate IMP-04 children subject to their governing authorities and deferred-decision boundaries.

> **Opaque credential → current session → Trusted Execution Context binding is CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
