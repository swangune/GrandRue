# IMP-01 — Engineering & Conformance Foundation Closure Evidence

**Programme:** MS-IMP-001 v1.0  
**Execution rules:** IMPLEMENTATION-RULES.md v1.3  
**Date:** 28 August 2026  
**Status:** CONFORMING_COMPLETE  
**Validated implementation head:** `5170dce3dd9f30a12780a276c12c2164637fa8ac`  
**GitHub Actions run:** 1074 (`33129447751`)  

---

## 1. Scope

MS-IMP-001 requires IMP-01 to establish the reusable engineering and conformance foundation for subsequent production implementation work:

- reproducible build;
- architecture/corpus conformance checks;
- module dependency checks;
- deterministic Clock/test support;
- production-representative PostgreSQL integration harness;
- migration verification harness;
- test-fixture discipline;
- authority-to-test traceability; and
- a clean full-suite baseline.

This evidence records the implementation state that satisfies that target. It does not create new semantic or architectural authority.

---

## 2. Verified Build Baseline

The exact validated head was executed by GitHub Actions using:

```text
Java Temurin 25.0.4+1
PostgreSQL 18.6
mvn --batch-mode clean verify -Ppostgres-it
```

The verified build compiled:

```text
516 production Java sources
178 test Java sources
```

Final results:

```text
Unit tests:        522
Integration tests: 168
Total:             690
Failures:            0
Errors:              0
Skipped:             0
BUILD SUCCESS
```

The CI workflow therefore remains the canonical production-representative implementation gate for the current modular-monolith build.

---

## 3. Architecture and Corpus Conformance

Existing reusable governance protection remains active:

- `DesignCorpusConformanceTest`
- `ImplementationBoundaryConformanceTest`

`ImplementationBoundaryConformanceTest` continues to protect, among other things:

- the canonical production `ConfigurationCompiler` path;
- quarantine of superseded semantic prototype paths into test scope;
- prohibition on a Booking-owned parallel notification delivery stack; and
- repository-navigation material from reasserting obsolete implementation claims.

No new architecture-testing framework was introduced. The accepted MS-PROT-032 rules remain mechanically protected using the existing JUnit/filesystem style, avoiding an unnecessary ArchUnit, Spring Modulith or JPMS dependency.

---

## 4. Module Dependency Conformance

IMP-01 added:

```text
src/test/java/mainstreet/governance/ModuleDependencyConformanceTest.java
```

It mechanically protects the accepted MS-PROT-032 dependency subset that is derivable from the current package architecture:

1. non-infrastructure production code may not depend directly on infrastructure implementations;
2. database implementation technology remains inside infrastructure, except for explicit Spring composition roots that wire concrete adapters; and
3. one semantic-ownership module may not directly import another ownership module's persistence Store/Repository/Jdbc/Jooq implementation contract.

The initial failing test correctly exposed three prototype application/use-case classes that directly constructed JOOQ persistence implementations.

The minimum behaviour-preserving correction inverted those dependencies through already-existing capability contracts:

```text
PrototypeJooqOrderingUseCase
    -> OrderingUnitOfWork + read function

PrototypeJooqBookingUseCase
    -> BookingUnitOfWork + read function

PrototypeJooqAppointmentUseCase
    -> AppointmentUnitOfWork + read function
```

Concrete JOOQ construction moved to `PrototypeRuntimeConfiguration`, the explicit Spring composition root. This preserves the accepted dependency direction:

```text
composition root / infrastructure wiring
        ↓
application/use-case contract
        ↓
capability-owned domain behaviour
```

No semantic ownership, transaction meaning or business behaviour changed.

---

## 5. Deterministic Time Conformance

IMP-01 added:

```text
src/test/java/mainstreet/governance/TimeDeterminismConformanceTest.java
```

The test prevents hidden production wall-clock reads through:

- `Instant.now(...)`;
- `LocalDate.now(...)`;
- `LocalDateTime.now(...)`;
- `OffsetDateTime.now(...)`;
- `ZonedDateTime.now(...)`; and
- `System.currentTimeMillis(...)`.

Production business-time logic must continue to receive an explicit controllable `Clock` where time affects deterministic behaviour.

The validated repository contained no violation of this rule.

---

## 6. Production-Representative PostgreSQL Harness

The existing `postgres-it` Maven profile remains the production-representative persistence test gate:

```text
PostgreSQL 18.6 service container
Flyway production migrations
jOOQ production SQL dialect
Spring transaction support
Failsafe integration tests
```

This satisfies the IMPLEMENTATION-RULES requirement that SQL, constraint, transaction, concurrency and persistence behaviour be tested against the production database engine rather than inferred from an in-memory substitute.

---

## 7. Migration Verification Harness

IMP-01 added:

```text
src/test/java/mainstreet/infrastructure/persistence/FlywayMigrationChainIT.java
```

The test creates an isolated PostgreSQL schema, applies the complete production Flyway migration chain, validates it, re-runs migration, validates again and proves that no duplicate migration history is created.

At the validated head:

```text
28 migrations validated
28 migrations applied to a clean schema
second migrate: no migration necessary
second validate: successful
```

This converts full-chain migration safety from incidental coverage into an explicit reusable implementation gate.

---

## 8. Test Fixture Discipline

The repository already contains deterministic semantic/configuration fixture support through:

```text
src/test/java/mainstreet/testing/TestConfigurationReleases.java
```

and related test builders/fixtures. Current tests use explicit identifiers and fixed/injected clocks where time is material.

IMP-01 does not introduce a second fixture framework. Future implementation should extend these deterministic fixture patterns rather than create test data by uncontrolled current time, random global state or production bootstrap shortcuts.

---

## 9. Authority-to-Test Traceability

The reusable IMP-01 gate is intentionally traceable to accepted authority:

| Authority / rule | Executable evidence |
|---|---|
| MS-PROT-032 module ownership/dependency direction | `ModuleDependencyConformanceTest` |
| IMPLEMENTATION-RULES deterministic-time requirements | `TimeDeterminismConformanceTest` |
| IMPLEMENTATION-RULES production-representative DB testing | `postgres-it` CI profile and PostgreSQL integration suite |
| IMPLEMENTATION-RULES migration discipline | `FlywayMigrationChainIT` |
| Design/document corpus governance | `DesignCorpusConformanceTest` |
| Existing implementation-boundary decisions | `ImplementationBoundaryConformanceTest` |

Every later macro target remains responsible for adding its own authority-specific tests rather than treating this matrix as a substitute for target-level conformance evidence.

---

## 10. Dependency and Architecture Review

IMP-01 introduced no new runtime or test dependency.

Specifically, it did not add:

- ArchUnit;
- Spring Modulith;
- JPMS modules;
- an alternative database;
- a second migration framework;
- a new test-fixture framework; or
- a new deployment architecture.

The current package tree remains an implementation arrangement constrained by MS-PROT-032, not a new source of architecture authority.

---

## 11. Closure Verdict

```text
IMP-01
Engineering & Conformance Foundation

reproducible build                         PASS
architecture/corpus conformance            PASS
module dependency conformance              PASS
deterministic time                         PASS
PostgreSQL integration harness             PASS
migration verification                     PASS
fixture discipline                         PASS
authority-to-test traceability             PASS
full production-representative suite       PASS

VERDICT: CONFORMING_COMPLETE
```

No unresolved material design or architecture question was encountered by IMP-01.

Under MS-IMP-001 dependency governance:

```text
IMP-00 COMPLETE
    ↓
IMP-01 COMPLETE
    ↓
IMP-02 READY
```

The next implementation target is **IMP-02 — Persistence & Semantic Release Foundation**.
