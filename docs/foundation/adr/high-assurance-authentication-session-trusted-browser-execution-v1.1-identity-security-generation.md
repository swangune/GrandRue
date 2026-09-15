# ADR-014 v1.1 — Identity Security Generation Ownership, Rotation & Session Invalidation Amendment

**Document ID:** ADR-014 v1.1  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 28 August 2026 — explicit manual approval  
**Authority type:** Authentication/session implementation architecture amendment  
**Governed by:** `designs/DESIGN-RULES.md`  
**Depends on:** MS-PROT-028 through v1.3; MS-PROT-063 through v1.1; MS-PROT-064; MS-PROT-076; ADR-014 v1.0; ADR-015 through v1.1  
**Amends:** ADR-014 — High-Assurance Authentication, Session & Trusted Browser Execution Architecture  
**Supersedes:** No ADR-014 rule outside the Identity security-generation scope defined here  
**Purpose:** Assign durable Identity security-generation ownership and define initialization, optimistic rotation, atomic Session invalidation and non-secret Audit evidence without transferring merchant or credential authority into Sessions.

---

## 1. Accepted Decision

Main Street SHALL maintain exactly one durable current Identity security-generation state for every Identity whose authentication security state has been established.

The **Identity Security context** owns that state.

```text
Identity authentication security established
        ↓
current Identity security generation
        ↓ snapshot at Session establishment
Session Record
        ↓ compare on each request
current Identity security generation
```

A Session whose captured generation no longer equals the current generation MUST NOT establish authenticated continuity.

---

## 2. Problem and Governed Scope

ADR-014 requires security reset, recovery and compromise handling to invalidate affected Sessions. The implementation already captures a generation reference in each Session and re-reads a current-generation authority on every request, but ADR-014 v1.0 did not assign the durable owner or mutation contract for that authority.

This amendment governs only:

- ownership of current Identity security-generation state;
- idempotent initial establishment;
- rotation under optimistic concurrency;
- atomic invalidation of the Identity's still-current Sessions;
- non-secret authentication-security Audit evidence; and
- the read contract consumed by Session establishment and request validation.

---

## 3. Explicit Non-Goals

This amendment does not define:

- passkey, password or recovery-factor proof mechanics;
- the evidence sufficient to approve account recovery;
- Merchant Controller transfer or Merchant Control Recovery;
- Merchant Account suspension;
- WebAuthn credential registration/replacement ceremonies;
- browser endpoints, CSRF or CSP mechanics;
- Merchant Scope, role, privilege, entitlement or capability authority;
- a secret-store model; or
- customer-account-specific authentication policy.

Those concerns remain governed or deferred by their existing authorities.

---

## 4. Canonical Terms

### 4.1 Identity Security State

The durable security state owned for exactly one Identity. It contains the current generation reference and concurrency/version evidence required to serialize mutation.

### 4.2 Identity Security Generation Reference

A non-secret opaque reference identifying one generation of an Identity's authentication security state.

It is:

- immutable for that generation;
- replaced on successful rotation;
- safe for non-secret diagnostic/Audit correlation; and
- not a credential, authenticator, Session Credential, Identity identifier, Merchant Scope or business-authority claim.

### 4.3 Rotation

The atomic security mutation that replaces the current generation, invalidates the Identity's still-current Sessions and records non-secret Audit evidence.

---

## 5. Semantic Ownership and Negative Boundaries

The Identity Security context exclusively owns current Identity security-generation state.

The following MUST NOT own or independently mutate it:

- Merchant Account;
- Merchant Controller relationship;
- WebAuthn credential records;
- external/provider Credential Binding;
- Session Record;
- Merchant Scope;
- workforce Membership, Role or Device Authorisation;
- Commercial state; and
- Audit.

Session Records retain only the generation snapshot observed at Session establishment. Audit records evidence a committed security action but do not become current security state.

---

## 6. Durable State and Identity

There is exactly zero or one current Identity Security State per Identity reference before authentication security establishment, and exactly one afterward.

Conceptually:

```text
IdentitySecurityState
{
    identityReference
    currentGenerationReference
    concurrencyVersion
    establishedAt
    lastRotatedAt?
}
```

The exact relational names and Java representation remain implementation details. Identity equality is determined by the authoritative Identity reference, not by generation reference or attribute similarity.

---

## 7. Initial Establishment

When authentication security state is first established for an Identity, Main Street SHALL create its initial security generation durably.

Repeated initialization for the same Identity is idempotent:

```text
state absent
    → create exactly one initial generation

state already present
    → return the existing current state unchanged
```

Concurrent initialization MUST produce one committed current state. It MUST NOT create multiple generation owners or silently replace a generation already used by Sessions.

Initialization does not authenticate a request and does not create merchant authority.

---

## 8. Session Establishment and Request Validation

Successful Session establishment SHALL snapshot the current generation reference read from the Identity Security authority.

Every otherwise-current Session request SHALL re-read the current generation.

```text
Session generation == current Identity generation
    → generation check passes

Session generation != current Identity generation
    → authenticated continuity fails closed

current Identity generation unavailable
    → authenticated continuity fails closed
```

Generation equality is security-continuity evidence only. Current Merchant Scope and business authority remain independently resolved.

---

## 9. Rotation Command

A rotation request SHALL identify:

- the Identity reference;
- the expected current generation reference;
- a newly generated replacement generation reference;
- the security reason category;
- the effective time;
- the attributable acting principal/origin required by the caller's authority;
- a correlation reference; and
- the Audit identity/evidence required for retry-safe append.

Only an accepted authentication/security operation may invoke rotation. This amendment does not grant callers permission to reset credentials or perform recovery.

---

## 10. Optimistic Concurrency and Retry

Rotation MUST compare the supplied expected generation with the current durable generation inside the mutation transaction.

```text
expected == current
    → rotation may commit

expected != current
    → CONFLICT
    → no generation change
    → no Session revocation
    → no success Audit record
```

At most one concurrent rotation from the same expected generation may commit.

A repeated request carrying the superseded expected generation cannot rotate again; it receives the same conflict class and MUST re-read current state before a distinct later security action. Retry transport therefore cannot multiply rotation effect.

---

## 11. Atomic Rotation, Session Invalidation and Audit

A successful rotation SHALL commit one transaction containing:

1. replacement of the current Identity generation;
2. revocation of every still-current Session Record for that Identity; and
3. append of one non-secret `AUTHENTICATION_SECURITY` Audit record for the rotation.

If any required write fails, the transaction MUST roll back all three effects.

A successful rotation becomes authoritative at commit. No post-commit event is required to make invalidation effective.

The Session revocation reason and Audit reason category SHALL derive from the governed security reason supplied by the accepted caller. Raw credentials, authenticator material and Session Credentials MUST NOT enter Identity Security State or Audit.

---

## 12. Failure Classification

The implementation SHALL distinguish:

- `VALIDATION_REJECTION` for missing/invalid command evidence;
- `NOT_ESTABLISHED` when no Identity Security State exists;
- `CONFLICT` when the expected generation is stale;
- `SUCCESS` when generation replacement, Session revocation and Audit append commit; and
- technical failure when the atomic transaction cannot commit.

A technical failure MUST NOT expose a partially rotated generation.

---

## 13. Falsification Evidence

### 13.1 Concurrent resets

Two resets read the same generation and rotate concurrently. Optimistic comparison permits at most one commit; the loser conflicts without revoking or rotating again. **PASS**

### 13.2 Commit acknowledgement lost

The committed request is retried with its old expected generation. It conflicts and cannot create another generation. The committed generation and revocations remain authoritative. **PASS**

### 13.3 Audit persistence failure

Generation replacement and Session revocation are attempted but Audit append fails. The shared transaction rolls back all effects. **PASS**

### 13.4 Multi-merchant Controller Identity

One Identity controls several Merchants. Identity-level rotation invalidates that Identity's Sessions across those contexts but does not suspend or mutate any Merchant Account or Controller relationship. **PASS**

### 13.5 Session-store race

A request races rotation. Before commit, ordinary current-state rules apply; after commit, the changed generation fails the request-time comparison even if revocation evidence has not yet been observed by a separate read. The atomic durable transaction prevents a committed mixed state. **PASS**

### 13.6 Credential or Session attempts ownership

A credential record or Session tries to become the generation owner. Rejected by the exclusive Identity Security ownership rule. **PASS**

---

## 14. Trade-Offs and Rejected Alternatives

Main Street accepts one authoritative database read during Session validation because immediate security invalidation is more important than eliminating that lookup.

Rejected:

- **Session-owned generation** — cannot invalidate all affected Sessions from one Identity security action.
- **credential-owned generation** — one Identity may have multiple credentials and recovery can replace them.
- **Merchant Account-owned generation** — incorrectly couples Identity security to one merchant context.
- **asynchronous revocation after rotation** — permits ambiguous partial completion and weakens attributable security evidence.
- **last-write-wins rotation** — allows concurrent security actions to overwrite one another without detection.

---

## 15. Conformance Conditions

Implementation conforms to this amendment only when executable evidence proves:

1. one durable current generation per established Identity;
2. idempotent and concurrency-safe initialization;
3. request-time reader compatibility with ADR-014 Session validation;
4. stale expected generation conflicts without mutation;
5. at most one concurrent rotation from one expected generation commits;
6. successful rotation atomically replaces generation, revokes all current Identity Sessions and appends non-secret Audit evidence;
7. failure of any required write rolls back the whole rotation;
8. generation state contains no Merchant Scope or business authority; and
9. production persistence survives process restart.

---

## 16. Amendment Effect

ADR-014 v1.0 remains unchanged outside this amendment's Identity security-generation scope.

This amendment closes the Identity security-generation owner/writer design blocker recorded by `docs/development/imp-04-macro-closure-correction-2026-08-28.md`.

It does not by itself complete IMP-04; implementation conformance remains required.
