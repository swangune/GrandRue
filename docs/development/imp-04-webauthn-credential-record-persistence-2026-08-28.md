# IMP-04 WebAuthn Credential Record Persistence — Implementation Evidence

**Programme:** MS-IMP-001  
**Macro-node:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** Durable Spring Security WebAuthn `CredentialRecord` persistence through a Main Street-owned PostgreSQL/jOOQ `UserCredentialRepository` adapter  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING**

## Governing authority

This child is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- ADR-014 high-assurance authentication/session authority;
- accepted ADR-015 WebAuthn/passkey production adapter authority.

ADR-015 permits Spring Security WebAuthn to own ceremony and cryptographic-verification adapter concerns while Main Street remains authoritative for semantic Identity, merchant relationships, Merchant Scope, sessions and business authorisation.

The implementation therefore preserves:

```text
WebAuthn CredentialRecord persistence
    = technical authentication credential state
    ≠ Main Street Identity authority
    ≠ Merchant Scope
    ≠ Controller/staff authority
    ≠ role or privilege
```

The generic credential/secret model in MS-PROT-067 is not expanded to absorb passkey login semantics. The WebAuthn credential table is a technical authentication persistence adapter only.

## Test-first evidence

Test-first commit:

```text
d94203a463a4ef90ea2a1c9b41f963e84e32c060
test: specify durable WebAuthn credential record persistence
```

Initial RED verification:

```text
GitHub Actions run 33160219781
Maven Tests #1162
FAILED as expected during test compilation because the production repository adapter did not yet exist
```

The integration contract specifies that:

1. a registered WebAuthn credential survives repository recreation;
2. Spring may persist authenticator runtime-state progression without rebinding immutable registration identity;
3. an existing credential identifier cannot be rebound to another WebAuthn user handle or public key;
4. lookup by user handle is exact;
5. deletion removes only the selected credential.

The tests exercise Spring Security WebAuthn `CredentialRecord` state including credential type, credential identifier, WebAuthn user handle, public key, signature count, user-verification initialization, authenticator transports, backup eligibility/state, attestation evidence, creation time, last-used time and label.

## Production implementation

### Flyway V30

Production migration:

```text
src/main/resources/db/migration/V30__runtime__create_webauthn_credential_record.sql
```

V30 creates `webauthn_credential_record` with durable storage for Spring WebAuthn credential state and an index supporting exact WebAuthn user-handle lookup.

The table intentionally contains no:

- Main Street semantic Identity authority;
- Merchant Scope;
- Merchant Controller relationship;
- staff relationship;
- role;
- privilege;
- commercial entitlement;
- frozen business-authority claim.

Database constraints require non-empty credential identifiers/public keys, WebAuthn user handles within the permitted byte range and non-negative signature counters.

### Main Street-owned Spring adapter

Production type:

```text
mainstreet.infrastructure.persistence.webauthn.JooqWebAuthnUserCredentialRepository
```

The adapter implements Spring Security WebAuthn `UserCredentialRepository` using the existing Main Street PostgreSQL/jOOQ persistence foundation.

Supported operations:

```text
save(CredentialRecord)
findByCredentialId(Bytes)
findByUserId(Bytes)
delete(Bytes)
```

The save path uses a PostgreSQL conflict update constrained by the existing stored WebAuthn user handle, public key and registration creation evidence. Authenticator runtime state may progress, but the credential identifier cannot silently move to another registered user handle or public key.

## Diagnostic implementation corrections

The tests were not weakened or rewritten in response to implementation failures.

### Spring generated-builder API correction

Workflow:

```text
GitHub Actions run 33160438240
Maven Tests #1164
FAILED during production compilation
```

The implementation had assumed a nested type named `ImmutableCredentialRecord.Builder`. Spring exposes `ImmutableCredentialRecord.builder()` but does not expose the generated builder under that source-level type name.

Correction:

```text
38c9ef62d0b86adc122dacc6c0235734bb8eb58a
fix: use inferred Spring WebAuthn credential builder type
```

The tests remained unchanged.

### PostgreSQL conflict-predicate qualification correction

Workflow:

```text
GitHub Actions run 33160555122
Maven Tests #1165
```

This run proved:

- production compilation succeeded;
- test compilation succeeded;
- all 587 unit tests passed;
- V30 migrated successfully;
- 30 Flyway migrations validated.

Only the four new WebAuthn repository integration tests failed. PostgreSQL reported an ambiguous target-column reference inside the `ON CONFLICT DO UPDATE ... WHERE` predicate.

Correction:

```text
15be219b6e929f2f4ff8fce6b40475cbd651c538
fix: qualify WebAuthn upsert identity predicate
```

The target-row fields were explicitly qualified with `webauthn_credential_record`. The tests and persistence contract remained unchanged.

## Final exact-head pre-merge verification

Pull request:

```text
#36 — IMP-04: persist WebAuthn credential records
head: 15be219b6e929f2f4ff8fce6b40475cbd651c538
```

Final exact-head workflow:

```text
GitHub Actions run 33160865480
Maven Tests #1166
SUCCESS
```

Verified suite:

```text
587 unit tests
177 PostgreSQL integration tests
0 failures
0 errors
0 skipped
30 Flyway migrations
BUILD SUCCESS
```

The four new `JooqWebAuthnUserCredentialRepositoryIT` tests all passed against PostgreSQL 18.6.

## Merge and post-merge verification

PR #36 was promoted from draft only after exact-head GREEN verification and was merged with the verified head SHA pinned.

Merge commit:

```text
3b142ac4ab74cc39e5a6999dfe10b6cff88b7daa
```

Independent post-merge `development` verification:

```text
GitHub Actions run 33161073998
Maven Tests #1167
event: push
head: 3b142ac4ab74cc39e5a6999dfe10b6cff88b7daa
SUCCESS
```

## Invariants proven by this child

```text
WebAuthn credential state is durably recoverable
credential-id lookup is exact
WebAuthn user-handle lookup is exact
selected credential deletion does not remove sibling credentials
runtime authenticator state may progress after registration
credential identifier cannot be rebound to another registered user handle
credential identifier cannot be rebound to another registered public key
technical WebAuthn credential persistence does not own Merchant Scope or business authority
```

## Explicit non-claims

This child does not claim to complete:

- authoritative Main Street Identity ↔ WebAuthn user-handle binding;
- a concrete production `PublicKeyCredentialUserEntityRepository`;
- a concrete production `WebAuthnIdentityReferenceAuthority`;
- registration/bootstrap policy for binding WebAuthn user handles to Main Street Identity;
- HTTP security filter-chain/browser delivery integration;
- trusted-browser cookie policy;
- CSRF implementation;
- Merchant Scope resolution;
- Controller relationship/authority establishment;
- recovery;
- step-up authentication orchestration;
- high-risk hardware-attestation policy;
- idle-timeout progression;
- Identity-wide security-generation invalidation;
- complete IMP-04 closure.

Those remain subject to the refreshed IMP-04 dependency graph and applicable accepted authority.

> **Durable WebAuthn `CredentialRecord` persistence is CONFORMING_COMPLETE for this child scope. IMP-04 remains PARTIALLY_CONFORMING.**
