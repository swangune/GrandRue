# IMP-04 Identity-Wide Session Revocation — Implementation Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-014 durable exact-Identity current-session revocation primitive  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

---

## 1. Governing authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- ADR-014 — High-Assurance Authentication, Session & Trusted Browser Execution Architecture;
- the existing ADR-014 `SessionRecord` / `SessionRecordStore` authority boundary.

ADR-014 requires Main Street to support security actions capable of invalidating one Session or all applicable Sessions for an Identity/security event. The current implementation already supported exact single-session revocation but lacked an exact-Identity durable revocation primitive.

This child deliberately implements only that already-required persistence primitive. It does not define which higher-level security event initiates the operation, account-recovery policy, Identity security-generation mutation, browser transport policy or any business-authority consequence.

---

## 2. Fine-grained dependency decision

The refreshed IMP-04 dependency graph first considered durable `IdentitySecurityGenerationAuthority` ownership/rotation. That work is **not READY** because accepted authority establishes request-time generation comparison but does not currently assign the durable generation writer, creation/rotation lifecycle or recovery orchestration strongly enough to implement those semantics without invention.

The independent Identity-wide Session revocation primitive was therefore selected as the smaller READY child because:

```text
accepted ADR-014 requirement exists
        +
Session Record already carries exact Identity reference
        +
V29 already persists and indexes identity_reference
        +
existing revocation evidence semantics are defined
        ↓
no new semantic or schema decision required
```

---

## 3. Test-first evidence

Test-first commit:

```text
2f26aa3c8d59bddbd99661d8ac1294146fdc91bf
```

Commit message:

```text
test: require identity-wide session revocation
```

RED workflow:

```text
GitHub Actions run 33175889324
Maven Tests #1193
FAILURE as intended
```

The falsifying PostgreSQL integration test requires that:

1. every still-current Session Record for the exact target Identity is revoked;
2. another Identity's Session Record remains untouched;
3. already-revoked Session Records retain their original revocation timestamp and reason;
4. a later repeated Identity-wide revocation cannot rewrite historical revocation evidence.

The test uses the production-representative PostgreSQL path rather than an in-memory substitute because the behaviour is a durable multi-row persistence invariant.

---

## 4. Minimum production implementation

### Session persistence port

`SessionRecordStore` now exposes:

```java
void revokeAllForIdentity(
        String identityReference,
        Instant revokedAt,
        String reason
);
```

The operation is intentionally scoped to Session persistence. It establishes no Merchant Scope, role, privilege, entitlement, capability authority or device authorisation.

### PostgreSQL/jOOQ implementation

`JooqSessionRecordStore` performs one bounded update:

```text
exact identity_reference
        +
revoked_at IS NULL
        ↓
set revoked_at + revocation_reason
```

Rows carrying prior revocation evidence are excluded from mutation. Another Identity cannot match the exact-Identity predicate.

No new migration was introduced. Existing V29 already persists and indexes `identity_reference`, so the implementation reuses the current schema rather than adding redundant state.

### Deterministic test infrastructure

`TestSessionRecordStore` implements the same semantic boundary for runtime tests. Test-local `SessionRecordStore` doubles used by unrelated WebAuthn/session-resolution tests were updated only to satisfy the expanded persistence port; they explicitly reject the unsupported operation because those tests do not exercise Identity-wide revocation.

---

## 5. GREEN correction trace

The first implementation attempt reached production compilation but failed test compilation because two test-local `SessionRecordStore` doubles had not yet implemented the new port method.

This was classified as an implementation/test-fixture compatibility defect under the automated failure-handling rule. The RED behavioural contract was not weakened or rewritten.

The correction added only the missing fixture methods and retained their existing narrow test responsibility.

---

## 6. Final exact-head verification

Final verified implementation head:

```text
f1f285bee8d58b4666b7d8713cf423c0281f89d6
```

Exact-head GREEN workflow:

```text
GitHub Actions run 33176448989
Maven Tests #1198
SUCCESS
```

Verified suite:

```text
Temurin Java                         25.0.4+1
production Java sources              555
Test Java sources                    197
unit tests                           590 PASS
PostgreSQL integration tests         183 PASS
total Maven tests                    773 PASS
failures / errors / skipped          0 / 0 / 0
Flyway migrations                    31
PostgreSQL                           18.6
BUILD SUCCESS
```

`JooqSessionRecordStoreIT` ran 6 tests with zero failures/errors/skips, including the new Identity-wide revocation contract.

---

## 7. Invariants proven by this child

```text
Identity reference
    ≠ Session Identity

Identity-wide revocation
    targets only the exact Identity

current Session Record
    may be revoked by the Identity-wide primitive

already-revoked Session Record
    retains prior revocation evidence

later retry / security action
    cannot rewrite prior revocation evidence

other Identity
    remains unaffected

Session persistence
    ≠ Merchant Scope
    ≠ role
    ≠ privilege
    ≠ entitlement
    ≠ capability authority
    ≠ device authorisation
```

The implementation stores no raw bearer credential and introduces no new authority snapshot into the Session Record.

---

## 8. Explicit non-claims and residual dependencies

This child does **not** claim to implement or complete:

- the higher-level global-security-event/account-recovery workflow;
- a concurrency barrier between Identity-wide revocation and concurrently established new Sessions;
- durable `IdentitySecurityGenerationAuthority` persistence ownership;
- security-generation creation, rotation or increment orchestration;
- account-recovery policy;
- idle-timeout progression;
- step-up authentication orchestration;
- authentication-security Audit wiring;
- Spring Security browser/filter-chain delivery;
- cookie or CSRF implementation policy;
- high-risk hardware-attestation policy;
- staff operational device-key specifics;
- complete IMP-04 closure.

The durable Identity security-generation writer/rotation boundary remains a **DESIGN_ESCALATION / not-READY dependency** until accepted authority determines ownership and mutation semantics sufficiently for implementation.

> **Identity-wide current-session revocation is CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
