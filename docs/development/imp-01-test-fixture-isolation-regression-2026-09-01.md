# IMP-01 Test-Fixture Isolation Regression Correction

**Date:** 1 September 2026  
**Macro target:** IMP-01 — Engineering & Conformance Foundation  
**Node:** IMP-01-R1 — PostgreSQL integration-test fixture isolation correction  
**Classification:** AUTO_FIX / FAILED_IMPLEMENTATION → COMPLETE  
**Governing authority:** `designs/MS-IMP-001.md`; `designs/IMPLEMENTATION-RULES.md`  
**Corrective commit:** `69d0b1d4eb79964f86bdf4af17a7f506e9308b51`  
**Verification:** GitHub Maven Tests #1306 / run `33475164682` — SUCCESS

This record is implementation evidence only. It does not create semantic,
architectural or programme authority.

## 1. Regression

The full Maven/PostgreSQL gate at development head
`7ddde36a7afff9108a6b135b91513968a741edab` failed before operational-device
authorisation behaviour was exercised.

All seven errors occurred while
`JooqMerchantOperationalDeviceAuthorisationIT` prepared its fixtures. Earlier
Business Hours integration tests left the globally unique
`controller_relationship_identifier` value `controller-rel-a` owned by
`merchant-acme`. The device-authorisation test attempted to reuse that
identifier for `merchant-device-a`, producing a PostgreSQL primary-key
violation.

The defect was cross-test mutable fixture contamination. It did not establish a
production-domain or persistence-semantics defect.

## 2. Governing classification

The correction is authorised automatically by:

- MS-IMP-001 IMP-01, which requires production-representative PostgreSQL test
  fixture standards and a known green full-suite baseline;
- MS-IMP-001 sections 30 and 33, which require a known baseline and full Maven
  success before completion/readiness;
- Implementation Rules sections 4, 8, 31, 34, 44 and 48, which permit fixture
  correction without changing accepted meaning and prohibit hiding flakiness
  with retries or weakened assertions.

The existing failing full integration suite supplied the RED evidence. No
production behaviour, capability ownership, contract, transaction,
authorisation or externally observable outcome changed.

## 3. Minimum correction

`JooqMerchantOperationalDeviceAuthorisationIT` now:

- uses class-specific controller-relationship identifiers;
- deletes device-authorisation request/fact rows only for its two synthetic
  merchants;
- removes only its own controller relationships and merchant accounts;
- performs the same scoped cleanup before and after each test; and
- preserves every operational-device authorisation assertion, including
  authority rejection, merchant scoping, idempotency, revocation and concurrent
  replay.

## 4. Canonical verification

```text
workflow                                Maven Tests
run number                              1306
run id                                  33475164682
head                                    69d0b1d4eb79964f86bdf4af17a7f506e9308b51
command                                 mvn --batch-mode clean verify -Ppostgres-it
production Java sources compiled        830
test Java sources compiled              262
unit / conformance tests                720 PASS
PostgreSQL integration tests            298 PASS
total Maven tests                       1018 PASS
Flyway migrations                       51 validated / V51 current
failures / errors / skipped             0 / 0 / 0
result                                  BUILD SUCCESS
```

Verification run:
https://github.com/swangune/MainStreet/actions/runs/33475164682

## 5. Graph refresh

The IMP-01 regression node is COMPLETE and the repository again has a known
green baseline. Historical IMP-00 through IMP-05 completion remains supported.

The programme frontier does not advance:

```text
IMP-06 PARTIALLY_CONFORMING
E3 BLOCKED_DESIGN — exact server-established Audience Observation Context
E4 / T1B4 / T4B BLOCKED_DEPENDENCY
IMP-07 and later BLOCKED_DEPENDENCY
```

No independent implementation node is READY. Implementation stops at the E3
manual design-approval boundary.
