# IMP-04 — Identity Security Generation Authority — Conformance Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Node:** Identity security-generation owner/writer  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE**  

```text
Identity security-generation owner/writer = CONFORMING_COMPLETE
```

## 1. Governing authority

This node implements ADR-014 v1.1 under `designs/MS-IMP-001.md` and `designs/IMPLEMENTATION-RULES.md`.

ADR-014 v1.1 assigns exactly one durable current generation to the Identity Security context, requires idempotent initialization, and requires optimistic rotation, Identity-wide Session revocation and non-secret Audit append to commit atomically.

## 2. Implemented boundary

The production implementation adds:

- the `mainstreet.identitysecurity` owner model and management contract;
- PostgreSQL/Flyway migration `V32__identity_security__create_generation_authority.sql`;
- `JooqIdentitySecurityGenerationManagement` as both durable writer and runtime reader;
- advisory-lock and compare/update concurrency protection;
- atomic generation replacement, Session revocation and `AUTHENTICATION_SECURITY` Audit append;
- idempotent WebAuthn-time initialization through `IdentitySecurityGenerationService`; and
- WebAuthn Session establishment against the same durable current-generation authority.

Generation state contains Identity reference, opaque generation reference, concurrency version and timestamps. It contains no Merchant Scope, Controller relationship, role, privilege, entitlement, credential material or Session Credential.

## 3. Conformance matrix

| ADR-014 v1.1 condition | Executable evidence |
|---|---|
| One durable current generation per established Identity | Primary-keyed PostgreSQL state; idempotent initialization IT; WebAuthn bootstrap IT |
| Idempotent, concurrency-safe initialization | `IdentitySecurityGenerationTest`; PostgreSQL advisory-lock initialization and repeated initialization IT |
| Session reader compatibility | Management implements `IdentitySecurityGenerationAuthority`; WebAuthn bridge IT snapshots the durable value |
| Stale expected generation conflicts without mutation | PostgreSQL IT verifies unchanged generation, current Session and absent success Audit |
| At most one concurrent rotation commits | Two-thread PostgreSQL race test verifies one success and one `CONFLICT` |
| Successful rotation is atomic | IT verifies replacement, all Identity Sessions revoked, unrelated Session unchanged and one Audit record |
| Required-write failure rolls back | Simulated Audit failure IT verifies generation and Session revocation both roll back |
| No merchant/business authority in generation state | Domain value and V32 schema contain only Identity security state fields; package-boundary conformance protects ownership |
| Durable production persistence | Fresh management adapter reads the same PostgreSQL row; full PostgreSQL 18.6 integration profile passes |

Validation rejection uses constructor/argument validation, missing state is classified `NOT_ESTABLISHED`, stale state is `CONFLICT`, success returns committed state, and technical persistence/Audit failure propagates only after transaction rollback.

## 4. Test-first trace

```text
849666e20d8ff53d72c75cf88cb7ae1e2e087020
docs(adr): accept Identity security generation authority

763521f1a56778778982824998ec7b467c9a9f0b
test(imp-04): require durable Identity security generation
Maven Tests #1276 — FAILURE as intended (missing production types)

1327f0c29e53ffb3c2bbdaab774eefc190345b3d
feat(imp-04): add durable Identity security generation authority
Maven Tests #1277 — SUCCESS

3182f7b5efc2f18b122962d25762c4d6a3ea5e72
test(imp-04): require WebAuthn Identity security bootstrap
Maven Tests #1278 — FAILURE as intended (missing bootstrap service)

abe4a30c17e89485f8c8c864c81b76e43878eacd
feat(imp-04): bootstrap Identity security for WebAuthn
Maven Tests #1279 — SUCCESS
```

## 5. Verified baseline

```text
workflow                                  Maven Tests #1279
run id                                    33209674797
head                                      abe4a30c17e89485f8c8c864c81b76e43878eacd
Java                                      Temurin 25 / release 25
PostgreSQL                                18.6
Flyway migrations                        32
production Java sources                  586
test Java sources                        222
unit tests                               638 PASS
PostgreSQL integration tests             191 PASS
total tests                               829 PASS
failures / errors / skipped              0 / 0 / 0
result                                    BUILD SUCCESS
```

## 6. Limits and graph consequence

This node does not implement credential proof ceremonies, recovery approval evidence, passkey replacement endpoints, Controller transfer, Merchant suspension, CSRF/CSP mechanics or customer authentication policy. Those are explicit ADR-014 v1.1 non-goals.

The required Identity security-generation blocker identified by the IMP-04 macro correction is closed. Macro completion still requires a fresh review of every IMP-04 child; this evidence alone does not manufacture that decision.
