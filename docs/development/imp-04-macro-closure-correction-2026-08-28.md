# IMP-04 Macro Closure Correction — Required Identity Security-Generation Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-04 — Merchant Account, Scope, Identity & Trust  
**Date:** 28 August 2026  
**Status:** **RESOLVED — REQUIRED CHILD CONFORMING**

---

## 1. Governing authority

This correction applies only:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`; and
- the accepted authentication/session authorities they route to.

It does not amend accepted design. It corrects implementation classification and programme navigation.

---

## 2. Defect

The earlier closure introduced an unsupported qualifier—“complete for the IMP-04 foundation”—for authentication and Sessions, then treated a missing required child as future optional work.

MS-IMP-001 gives the credential/security item a `foundation` qualifier, but it does not reduce the whole authentication/session scope to reader ports or architectural seams. Its completion gate requires every required child to have accepted authority, minimum implementation, executable proof, verification and evidence.

The current production path consumes `IdentitySecurityGenerationAuthority` in two security-critical places:

```text
successful WebAuthn proof
    → read current Identity security generation
    → establish Session snapshot

presented Session Credential
    → re-read current Identity security generation
    → compare with Session snapshot
```

The repository has no accepted owner/writer contract or durable implementation for initial generation creation and security-event rotation. A fail-closed reader port proves safe rejection; it does not make the production authority exist.

---

## 3. Corrected dependency classification

```text
IMP-04 — Merchant Account, Scope, Identity & Trust = PARTIALLY_CONFORMING

required child:
    Identity security-generation ownership / creation / rotation
        = BLOCKED_DESIGN

IMP-05 — Merchant Definition, Configuration & Activation = BLOCKED
    HARD dependency IMP-04 is incomplete
```

The earlier file `imp-04-merchant-account-scope-identity-trust-closure-2026-08-28.md` is retained as historical evidence but is superseded and cannot unlock IMP-05.

---

## 4. Why the required child is blocked

IMPLEMENTATION-RULES prohibits implementation from inventing a consequential authentication/security mutation contract.

Accepted authority currently establishes that:

- Session establishment and request validation need current security-generation evidence;
- security resets, account recovery and suspected compromise can invalidate Sessions; and
- authentication identity, Session continuity and merchant authority remain separate.

Accepted authority does not yet assign:

- the aggregate/module that owns Identity security-generation state;
- the initial generation-creation event and consistency boundary;
- the exact mutation command for factor reset, recovery or compromise;
- idempotency/concurrency behavior for rotation;
- transaction ordering between rotation and Session revocation; or
- the durable adapter/wiring that supplies the reader consumed by production authentication.

MS-PROT-067 cannot fill that gap because it explicitly excludes identity-provider/login credential semantics from its responsibilities.

Those missing decisions materially affect authentication and security policy. The required child is therefore `BLOCKED_DESIGN`, not READY and not optional.

---

## 5. Preserved implementation evidence

All verified IMP-04 child implementations remain valid evidence for their bounded scopes, including WebAuthn policy and credential persistence, authentication-subject binding, opaque Session establishment, durable Session Records, request-time validation, logout/revocation, cookie representation, request extraction and current scoped-principal resolution.

The already-implemented IMP-05 onboarding children are also preserved. They are reusable early evidence, but MS-IMP-001 does not permit them to advance the active programme while IMP-04 remains incomplete.

No production code is reverted by this correction.

---

## 6. Test-first correction evidence

Regression test commit:

```text
0257fff2b8af250db7048c03a18383339f3c0448
test(governance): require truthful IMP-04 dependency gate
```

RED verification:

```text
Maven Tests #1268
GitHub Actions run 33206354335
FAILURE as intended
632 tests run
1 failure
0 errors
0 skipped
```

The sole failure was `ImplementationProgrammeGateConformanceTest`, because this superseding correction did not yet exist. Existing tests remained green.

---

## 7. Continuation decision

The implementation graph is refreshed as follows:

```text
active macro          IMP-04
macro state           PARTIALLY_CONFORMING
next required child   Identity security-generation owner/writer
child state           BLOCKED_DESIGN
IMP-05                BLOCKED
```

Per IMPLEMENTATION-RULES, implementation must stop on that affected child until accepted design assigns the missing owner and mutation semantics. No further IMP-05 work may be selected in the meantime.

Independent IMP-04 children may proceed only if their accepted authority and dependencies are complete; this correction does not manufacture such authority.


---

## 8. GREEN verification

Correction implementation head:

```text
87e423cdbffdccb0d94188d298bc6c8b4bec7b6f
fix(governance): restore IMP-04 dependency gate
```

Exact-head verification:

```text
Maven Tests #1269
GitHub Actions run 33206734177
SUCCESS
```

Verified suite:

```text
production Java sources              577
test Java sources                    217
unit tests                           632 PASS
PostgreSQL integration tests         183 PASS
total Maven tests                    815 PASS
failures / errors / skipped          0 / 0 / 0
Flyway migrations                    31
PostgreSQL                           18.6
BUILD SUCCESS
```

The new `ImplementationProgrammeGateConformanceTest` passed and the full PostgreSQL-backed suite remained green.


---

## 9. Resolution of the corrected blocker

The correction remains authoritative historical evidence that the first IMP-04 closure was invalid. Its active blocker classification is now resolved, not erased.

Resolution authority and implementation evidence:

```text
ADR-014 v1.1
    → accepted Identity Security ownership and rotation contract

imp-04-identity-security-generation-authority-2026-08-28.md
    → CONFORMING_COMPLETE required child

imp-04-merchant-account-scope-identity-trust-closure-v2-2026-08-28.md
    → fresh macro review and replacement closure
```

The earlier `PARTIALLY_CONFORMING` / `BLOCKED` statements in this correction describe the state that correctly applied before the accepted amendment and conforming implementation. They are intentionally retained for provenance. Current navigation is governed by the replacement closure and `implementation-status.md`.
