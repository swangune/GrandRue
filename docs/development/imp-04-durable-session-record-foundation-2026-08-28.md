# IMP-04 Durable Session Record Foundation — Implementation Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-014 durable server-authoritative human Session Record foundation  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

---

## 1. Governing Authority

This child is governed by:

- `designs/IMPLEMENTATION-RULES.md`;
- `designs/MS-IMP-001.md`;
- ADR-014 — High-Assurance Authentication, Session & Trusted Browser Execution Architecture;
- accepted Merchant Scope / Identity / Runtime authority that keeps authentication distinct from actor authorisation and merchant business authority.

ADR-014 requires human browser continuity to use:

```text
opaque session credential
+
server-authoritative Session Record
```

with durable server-controlled persistence. It fixes 256-bit session-credential entropy and permits persistence of a cryptographic verifier rather than the raw bearer credential. The exact relational schema/index strategy is an ADR-014 implementation detail.

No new authentication product policy, WebAuthn provider choice, public API contract or merchant business authority is created by this evidence record.

---

## 2. Test-First Trace

The child began with executable specification before production implementation:

```text
620b5bf5e45e6f4d04d2350655097ac772678775
test: specify ADR-014 session record foundation

152bf762065b15f801f845693e47eddcdffa0c06
test: specify durable ADR-014 session persistence
```

The tests require:

- exactly 256 bits / 32 bytes of random session-credential entropy;
- bearer-secret redaction from ordinary string rendering;
- one-way credential-verifier persistence rather than raw bearer persistence;
- durable Session Record recreation after process/store recreation;
- exact session-identity replay idempotency;
- conflicting identity/verifier reuse rejection;
- durable logout/session revocation evidence;
- paired revocation time/reason semantics;
- no Merchant Scope, role, privilege, entitlement or device-authorisation fields in the Session Record or persistence schema.

---

## 3. Production Implementation

Production implementation commits:

```text
84851f41dae9bb09e69a1faf64fb9957d463a0dd
feat: add server-authoritative session record

edfd23377eae127019ada0fe441104998f94667f
feat: add 256-bit opaque session credential

b93b896393b218ddb25d7393790348e6a594dcf2
feat: add session record persistence port

357545022aa2e6b2318f5f3b7383d82a2bb8bba2
feat: add durable authentication session schema

f6696b1242226f88691f0061f3c8adef1227a094
feat: persist server-authoritative session records
```

Implemented production types:

```text
mainstreet.runtime.SessionRecord
mainstreet.runtime.OpaqueSessionCredential
mainstreet.runtime.SessionRecordStore
mainstreet.infrastructure.persistence.runtime.JooqSessionRecordStore
```

Flyway migration:

```text
V29__runtime__create_authentication_session_record.sql
```

---

## 4. Security Boundary Preserved

`SessionRecord` contains trusted continuity/security evidence only:

```text
sessionIdentity
identityReference
credentialVerifier
establishedAt
authenticationAssuranceReference
authenticationMethodReference
absoluteExpiry
lastActivityAt
securityGenerationReference
revokedAt?
revocationReason?
```

It deliberately contains no:

```text
Merchant Scope
merchant relationship
role
privilege
commercial entitlement
device authorisation
Merchant Configuration
capability authority
```

The raw opaque bearer value is not persisted by the jOOQ store. The persistent lookup key is the one-way SHA-256 verifier.

This preserves ADR-014's separation:

```text
Session Credential
    ≠ Session Identity
    ≠ business authority
```

---

## 5. Persistence Behaviour

`JooqSessionRecordStore` provides durable PostgreSQL persistence with:

- unique Session Identity;
- unique credential verifier;
- exact-record replay idempotency;
- rejection of conflicting identity/verifier reuse;
- lookup by Session Identity;
- lookup by credential verifier;
- durable revocation;
- exact revocation replay idempotency;
- rejection of conflicting revocation rewrite.

Database checks reinforce, but do not originate, the Java-domain invariants for time ordering and paired revocation evidence.

---

## 6. Verification Evidence

Pull request:

```text
#25 — IMP-04: durable server-authoritative session record foundation
head: f6696b1242226f88691f0061f3c8adef1227a094
```

Pre-merge full verification:

```text
GitHub Actions run: 33134504099
Maven Tests #1110
result: SUCCESS
```

Verified merge into `development`:

```text
213dc3a5b6bb38d96e3fadb845d9f39f11958a8a
IMP-04: durable server-authoritative session record foundation
```

Post-merge full verification:

```text
GitHub Actions run: 33134710385
Maven Tests #1111
head: 213dc3a5b6bb38d96e3fadb845d9f39f11958a8a
result: SUCCESS
```

Both gates execute:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

on JDK 25 with the production-representative PostgreSQL integration harness.

---

## 7. Explicit Non-Claims / Remaining IMP-04 Work

This child does **not** claim IMP-04 completion and does not implement or select:

- the exact WebAuthn/passkey provider/library;
- WebAuthn registration/authentication ceremonies;
- Controller authentication transport/endpoints;
- privileged browser cookie emission/validation;
- exact CSRF implementation;
- browser CSP/XSS delivery configuration;
- credential rotation/session fixation orchestration;
- idle-timeout/current-session resolution from the bearer credential;
- current security-generation invalidation;
- step-up authentication;
- account/control recovery;
- customer-account authentication;
- staff PIN/passkey/device authentication completion.

Those remain separate governed children. In particular, ADR-014-DQ-001 leaves the exact WebAuthn implementation/provider/library as a security implementation-architecture decision and must not be crossed by implementation convenience.

---

## 8. Child Verdict

> **The ADR-014 durable server-authoritative Session Record foundation is CONFORMING_COMPLETE for its defined child scope. IMP-04 remains PARTIALLY_CONFORMING.**

The next READY child may connect opaque credential verification and durable current-session resolution to trusted execution-context establishment, provided it preserves current Merchant Scope/relationship authority and does not select an unresolved WebAuthn/security architecture.
