# IMP-04 — WebAuthn Authentication Subject Binding Conformance

**Date:** 28 August 2026  
**Status:** CONFORMING_COMPLETE implementation-child evidence  
**Implementation target:** IMP-04 Merchant Account, Scope, Identity & Trust  
**Governing authority:** MS-PROT-028, MS-PROT-063, ADR-014, ADR-015 and accepted WebAuthn Authentication Subject binding amendment  
**Implementation rules:** `designs/IMPLEMENTATION-RULES.md`, `designs/MS-IMP-001.md`

---

## 1. Decision evidence

Manual approval was received on 28 August 2026 for the refined Option B / lightweight hybrid:

> Main Street owns a distinct opaque WebAuthn authentication-subject handle, one-to-one with one Main Street Identity for the initial implementation. Multiple passkeys for that Identity share the handle. The handle is authentication infrastructure evidence only and carries no Merchant Scope, Controller authority, role, privilege, entitlement or other business authority.

The accepted authority was incorporated before implementation through PR #38. The verified `development` authority baseline used by this child was:

```text
1066905fe522654732d12c676b1e871823b74c6a
```

---

## 2. Implementation child

PR:

```text
#40 — IMP-04: bind WebAuthn subjects to Main Street Identity
```

Final exact PR head:

```text
078d17d4e664668edf1f7f13a2ddb014e807ff2b
```

Merged `development` commit:

```text
ff20a30eddc265db0391dd486cc886bd94d7fc5b
```

Implemented production artifacts:

```text
src/main/resources/db/migration/
    V31__runtime__create_webauthn_authentication_subject.sql

src/main/java/mainstreet/infrastructure/persistence/webauthn/
    JooqWebAuthnAuthenticationSubjectRepository.java

src/main/java/mainstreet/infrastructure/security/webauthn/
    WebAuthnAuthenticationSubjectService.java
```

Executable integration evidence:

```text
src/test/java/mainstreet/infrastructure/persistence/webauthn/
    JooqWebAuthnAuthenticationSubjectRepositoryIT.java
```

---

## 3. RED → GREEN evidence

### RED

Test-first commit:

```text
f22c0f604948f21db59323b868cd63bc113bdb1d
```

GitHub Actions:

```text
run 33164473984 / Maven Tests #1174
```

The run failed at test compilation because the required production repository/service types did not yet exist. Existing production sources compiled. The failure therefore proved the intended missing implementation contract rather than an unrelated regression.

### First implementation attempt — rejected by repository conformance

An initial implementation used `Instant.now()` for non-authoritative authentication-subject establishment metadata.

GitHub Actions:

```text
run 33164609635 / Maven Tests #1177
```

Compilation succeeded, but the existing `TimeDeterminismConformanceTest` rejected hidden wall-clock access in production code.

No test was weakened. The unnecessary timestamp field and hidden wall-clock read were removed because the accepted WebAuthn authentication-subject binding does not require establishment time as authoritative state.

### Corrected exact-head GREEN

Final exact PR head:

```text
078d17d4e664668edf1f7f13a2ddb014e807ff2b
```

GitHub Actions:

```text
run 33164812158 / Maven Tests #1179
```

Result:

```text
unit tests                         587 PASS
PostgreSQL integration tests       182 PASS
failures / errors / skipped        0 / 0 / 0
Flyway migrations                  31 validated/applied
PostgreSQL                         18.6
Spring Boot                        4.1.1
Spring Security Core               7.1.1
result                             BUILD SUCCESS
```

`JooqWebAuthnAuthenticationSubjectRepositoryIT` contributed five passing PostgreSQL tests.

### Independent post-merge verification

Merge commit:

```text
ff20a30eddc265db0391dd486cc886bd94d7fc5b
```

Push-triggered GitHub Actions:

```text
run 33164976044 / Maven Tests #1180
```

Result:

```text
SUCCESS
```

The exact merged `development` commit therefore passed the repository gate independently of the pull-request merge ref.

---

## 4. Proven invariants

Executable evidence now proves:

1. Main Street generates the WebAuthn authentication-subject handle rather than accepting a caller-selected account handle.
2. The initial generated handle uses 32 cryptographically random bytes and remains within WebAuthn's 1..64 byte user-handle boundary.
3. The opaque handle is distinct from the Main Street semantic Identity reference and does not encode the Identity reference as plaintext.
4. One active WebAuthn authentication-subject handle maps to exactly one Main Street Identity.
5. One Main Street Identity maps to at most one active WebAuthn authentication-subject handle in the initial implementation.
6. Re-establishing the same Identity reuses its existing handle rather than manufacturing another authentication subject.
7. Different Identities receive independent opaque handles.
8. Existing handle ↔ Identity bindings cannot be silently rebound through the repository.
9. Spring `PublicKeyCredentialUserEntityRepository.save` may update human-readable username/display-name metadata only for an already-known Main Street handle; it cannot create an unknown caller-selected handle.
10. Human-readable username/display name are not Identity authority. Verified WebAuthn authentication resolves Identity through the opaque handle.
11. `JooqWebAuthnAuthenticationSubjectRepository` implements the existing `WebAuthnIdentityReferenceAuthority` seam used by the post-verification WebAuthn → Main Street Session bridge.
12. Deleting the technical authentication subject does not delete or redefine semantic Main Street Identity authority; the removed handle simply ceases to resolve authentication Identity evidence.
13. Merchant Scope, Merchant Controller relationship, role, privilege, entitlement and capability/business authority are absent from the authentication-subject binding and remain current downstream checks.
14. The implementation preserves repository-wide deterministic-time conformance; no hidden wall-clock dependency remains in the binding implementation.

Existing V30 WebAuthn credential persistence remains separate. Multiple WebAuthn credentials may reference the same authentication-subject user handle without making credential identity equivalent to semantic Identity.

---

## 5. Explicit non-claims / remaining boundaries

This child does **not** claim completion of:

- credential/account recovery;
- authentication-subject rebinding or handle rotation;
- recovery-driven Identity/control changes;
- browser SecurityFilterChain or WebAuthn HTTP endpoints;
- privileged browser cookie delivery;
- CSRF implementation;
- CSP / Trusted Types hardening;
- step-up authentication execution/rotation;
- high-risk hardware-attestation/security-key policy (`ADR-014-DQ-002`);
- staff device-key policy (`ADR-014-DQ-008` / later workforce scope);
- complete authentication-security Audit event wiring;
- session idle-activity progression;
- any Merchant Scope, Controller, role, privilege or business-authorisation shortcut.

Those remain separately governed IMP-04 or downstream implementation nodes.

---

## 6. Conformance conclusion

The approved WebAuthn Authentication Subject binding child is **CONFORMING_COMPLETE**.

Main Street now has the production authentication chain foundation:

```text
Main Street Identity
        │
        │ one-to-one technical binding
        ▼
opaque Main Street WebAuthn authentication-subject handle
        │
        ├── WebAuthn credential A
        ├── WebAuthn credential B
        └── WebAuthn credential ...
        │
        ▼
Spring WebAuthn cryptographic verification
        │
        ▼
handle → Main Street Identity evidence
        │
        ▼
HumanSessionEstablishmentService
        │
        ▼
fresh opaque Main Street Session Credential + durable SessionRecord
        │
        ▼
current Merchant Scope / relationship / authorisation checks
```

IMP-04 remains **PARTIALLY_CONFORMING** until its remaining independent security/session/audit boundaries are closed or explicitly deferred to their governed downstream targets.
