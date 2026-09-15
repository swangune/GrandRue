# MS-PROT-034 — Database, Persistence Framework & Migration Technology Selection

**Version:** 1.0  
**Status:** **Accepted**  
**Depends on:** MS-PROT-025, MS-PROT-031, MS-PROT-033  
**Purpose:** Select the initial relational database, Java persistence approach, and schema-migration mechanism that best satisfy Main Street’s accepted transaction, tenancy, modularity, scheduling, local-development, and maintainability requirements.

Current verification: PostgreSQL 18 is the current stable major release; PostgreSQL 19 is still beta as of July 2026. Spring Boot 4.1 has first-class jOOQ support, and Spring Data JPA 4.1 is also current. Flyway 13.1.0 is current as of July 30, 2026. 

## 1. Proposed baseline

```text
Database
    PostgreSQL 18

Authoritative persistence
    jOOQ + Spring transaction management

Schema migrations
    Flyway

Spring Data JPA
    not the default authoritative persistence mechanism
```

The important decision is not simply "PostgreSQL instead of MongoDB." It is:

> **Use PostgreSQL as Main Street’s authoritative transactional store and use SQL-explicit persistence rather than making an ORM-managed object graph the centre of the domain architecture.**

---

## 2. Why PostgreSQL

PostgreSQL directly supports the characteristics Main Street repeatedly needs:

```text
transactions
relational constraints
merchant-scoped ownership
concurrent allocation
structured joins
time-based scheduling
historical records
JSON where bounded flexibility is useful
```

PostgreSQL 18 is currently supported and stable; PostgreSQL 19 remains a development release and should not be the production baseline. 

The decision is therefore:

```text
Production baseline:
    PostgreSQL 18.x
```

not:

```text
PostgreSQL 19 beta
```

---

## 3. Why not make JPA/Hibernate the default

Spring Data JPA is mature and current, and Spring Boot 4.1 supports JPA/Hibernate directly. 

Its advantages are real:

```text
low CRUD boilerplate
repository abstraction
entity persistence
transaction integration
broad ecosystem
```

But Main Street’s difficult persistence problems are not ordinary CRUD.

They include:

```text
interval scheduling conflicts
quantity allocation
tenant-qualified constraints
explicit transactional coordination
cross-capability references
projection queries
database-enforced invariants
careful aggregate loading
```

JPA can implement these, but the abstraction can become counterproductive when the database itself is an important part of invariant enforcement.

The risk is architecture such as:

```text
Java entity graph
      ↓
Hibernate mappings
      ↓
database
```

where persistence convenience begins determining:

```text
aggregate shape
relationships
loading
mutability
transaction behaviour
```

MS-PROT-033 explicitly rejected that direction.

---

## 4. Why jOOQ

jOOQ generates Java representations of the relational schema and provides type-safe SQL construction. Spring Boot 4.1 provides dedicated jOOQ auto-configuration and testing support. 

This matches Main Street well because we want:

```text
Domain model
      ↓
Repository implementation
      ↓
explicit relational operations
      ↓
PostgreSQL
```

rather than treating persisted rows as the domain model.

jOOQ gives us direct access to:

```text
joins
CTEs
locking
PostgreSQL features
explicit inserts/updates
conditional updates
projection queries
transaction-aware SQL
```

without dropping to stringly typed JDBC everywhere.

---

## 5. jOOQ does not become the domain model

This boundary is mandatory.

Generated jOOQ records belong in:

```text
infrastructure/persistence
```

not:

```text
domain
```

Reject:

```java
public OrderRecord completeOrder(OrderRecord order)
```

as the domain contract.

Prefer conceptually:

```text
Domain Order
      ↓
OrderRepository
      ↓
JooqOrderRepository
      ↓
generated database records
```

Thus:

```text
jOOQ schema types
    ≠
domain types
```

---

## 6. Why not plain JDBC everywhere

Plain JDBC gives maximum SQL control.

But we would repeatedly implement:

```text
parameter binding
result mapping
query construction
schema-name refactoring
type conversion
boilerplate
```

jOOQ preserves SQL transparency while providing compile-time assistance.

Therefore:

```text
plain JDBC
```

remains available for exceptional cases, but is not the default persistence style.

---

## 7. Why not Spring Data JDBC as the default

Spring Data Relational 4.1 is current and gives a simpler aggregate-oriented persistence model than JPA. 

It is a serious alternative.

However, Main Street is likely to require sophisticated SQL relatively early for:

```text
availability
interval overlap
capacity
cross-projection queries
tenant filtering
database constraints
```

Using Spring Data JDBC plus increasingly large amounts of custom SQL risks ending with two competing persistence styles.

jOOQ gives a more consistent SQL-first implementation model from the beginning.

---

## 8. Why Flyway

Database structure is part of Main Street’s persisted contract.

Schema changes must therefore be:

```text
versioned
reviewable
repeatable
locally reproducible
deployable
```

Flyway provides ordered schema migrations and integrates naturally with Java/Spring-based systems. Flyway 13.1.0 is the current release line as of July 2026. 

The rule becomes:

> **Production schema changes occur through version-controlled migrations, not runtime ORM schema generation.**

---

## 9. Hibernate schema generation is rejected

Do not use:

```text
ddl-auto = update
```

or equivalent production behaviour as schema governance.

Why?

Because the database contains authoritative:

```text
constraints
indexes
tenant keys
allocation rules
migration history
```

and those changes need explicit engineering review.

Application startup must not silently redesign production persistence.

---

## 10. PostgreSQL-specific features are allowed

A common portability rule would say:

> Never use database-specific features.

We reject that as a universal constraint.

If PostgreSQL provides a feature that safely enforces an important invariant, using it is legitimate.

Examples may eventually include:

```text
range types
exclusion constraints
partial indexes
JSONB
transaction/advisory locking
advanced constraints
```

But use a PostgreSQL-specific feature only where it materially improves correctness or simplicity.

We are not designing for hypothetical migration to every SQL database.

---

# FALSIFICATION REVIEW

## 11. Could PostgreSQL become a single point of failure?

Yes.

But replacing it with MongoDB or another primary database does not remove the need for database availability.

High availability, backup and recovery are deployment concerns.

This does not falsify PostgreSQL as the data model choice.

**Survives.**

---

## 12. Could PostgreSQL struggle with merchant website traffic?

Public website reads should primarily use:

```text
projections
caching
pre-rendering
CDN delivery later
```

rather than hammer authoritative transactional queries.

MS-PROT-029 already separated those concerns.

**Survives.**

---

## 13. Could PostgreSQL become inappropriate for media

Yes.

Images and videos should not necessarily be persisted as large relational blobs.

That was never part of the claim.

PostgreSQL remains the authoritative transactional default, not the universal storage engine.

**Survives.**

---

## 14. Could jOOQ couple us too strongly to the database schema?

Yes.

Generated types deliberately know the relational schema.

If they leak across module/domain boundaries, schema changes will couple everything.

Therefore generated jOOQ types must remain inside persistence adapters.

With that constraint:

**Survives.**

---

## 15. Could jOOQ create too much manual mapping code

Yes.

Compared with JPA:

```text
database row
↔
domain object
```

mapping becomes more explicit.

This is an actual cost.

But Main Street deliberately values:

```text
explicit ownership
predictable loading
explicit SQL
domain independence
```

over avoiding mapping code.

Mapping should be localised in repository adapters rather than scattered.

**Survives.**

---

## 16. Could JPA handle everything anyway?

Technically, yes.

This is not a capability question.

The question is which abstraction makes architecture easier to preserve.

Main Street repeatedly requires explicit control over:

```text
transaction boundaries
queries
constraints
loading
tenant scope
database-specific concurrency mechanisms
```

JPA's conveniences do not eliminate these requirements.

So JPA is not rejected as incapable; it is rejected as the **default architectural centre**.

---

## 17. Could some capability legitimately benefit from JPA

Possibly.

Should we prohibit it absolutely?

No.

That would be another unjustified universal rule.

Revised decision:

> **jOOQ is the default authoritative persistence technology. A module may use another persistence adapter only if a reviewed requirement demonstrates material benefit without violating module ownership or transaction semantics.**

We avoid mixed persistence casually, but we do not make it impossible.

---

## 18. Could Flyway migrations become coupled across modules

Yes.

One global unordered migration directory could make every capability contend for schema ownership.

Therefore migration organisation should preserve module ownership while producing one valid deployment sequence.

Exact layout is deferred, but migrations must identify their owning module/context.

**Survives with constraint.**

---

## 19. Can developer tests use H2 instead of PostgreSQL

This looks convenient but can create false confidence.

Main Street is likely to depend on PostgreSQL behaviour for:

```text
constraints
types
locking
SQL syntax
interval semantics
```

Therefore:

> **Persistence integration tests that validate PostgreSQL-specific invariants should run against real PostgreSQL, not an approximate in-memory replacement.**

Pure domain/unit tests still need no database.

This is an important falsification finding.

---

## 20. Does real PostgreSQL break local-development independence

No.

The user should still be able to run PostgreSQL locally, for example via:

```text
installed PostgreSQL
or
local container
```

without accessing GitHub.

Source-controlled Flyway migrations create the local schema.

The eventual developer workflow should support:

```text
local source
+
local PostgreSQL
+
mvn test
```

GitHub remains irrelevant to execution.

---

# 21. Cross-domain validation

**Restaurant:** transactions, order/inventory consistency and rich query needs fit PostgreSQL+jOOQ. **PASS**

**Grocery:** quantity allocation requires explicit concurrency-safe SQL and constraints. **PASS**

**Motel:** interval allocations make PostgreSQL's advanced relational/time capabilities attractive. **PASS**

**Driving instructor:** overlapping instructor schedules require explicit interval consistency. **PASS**

**Solicitor:** simple appointments do not suffer from using the same persistence stack. **PASS**

**Gardener:** ordinary enquiries, quotes and jobs remain straightforward. **PASS**

**Mechanic:** job/resource/scheduling queries fit naturally. **PASS**

**Realtor:** simple enquiries and richer viewing schedules coexist without new persistence technology. **PASS**

---

# 22. Accepted baseline

| Concern | Decision |
|---|---|
| Authoritative transactional database | **PostgreSQL 18.x** |
| Persistence style | **SQL-explicit** |
| Default Java persistence library | **jOOQ** |
| Transaction integration | **Spring transaction management** |
| Schema migration | **Flyway** |
| Default ORM | **None** |
| JPA/Hibernate | **Not default; exception only with validated reason** |
| Production schema generation | **Explicit migrations only** |
| PostgreSQL-specific features | **Allowed when they materially improve correctness/simplicity** |
| Database integration tests | **Real PostgreSQL where DB semantics matter** |
| Domain/unit tests | **Database-free where possible** |
| GitHub dependency | **None** |

Spring Boot 4.1 officially supports jOOQ auto-configuration, and jOOQ's current maintained 3.21 line supports Java 21+; therefore it is compatible with the accepted Java 25 baseline. 

---

# 23. Accepted invariants

1. PostgreSQL is Main Street's default authoritative transactional store.
2. PostgreSQL is not required for every data type.
3. jOOQ is the default persistence adapter technology.
4. SQL remains explicit at the infrastructure/repository boundary.
5. jOOQ-generated types do not enter the domain model.
6. Domain repositories remain capability-owned.
7. Persistence mapping remains local to infrastructure.
8. Spring transaction management coordinates local transactions.
9. Flyway owns production schema evolution.
10. Runtime ORM schema generation is prohibited for governed environments.
11. Database-specific features may be used where justified by accepted invariants.
12. Database portability is not prioritised over correctness without evidence.
13. JPA/Hibernate is not the default persistence architecture.
14. Alternative persistence approaches require explicit justification rather than convenience.
15. Persistence integration tests use actual PostgreSQL where database behaviour matters.
16. Unit/domain tests remain database-independent wherever possible.
17. Migration organisation preserves module ownership.
18. Local database creation/testing must be possible without GitHub.

---

# 24. Deferred decisions

MS-PROT-034 does not yet choose:

```text
physical table layout
SQL schema-per-module
row-level security
UUID vs ULID
PostgreSQL range/exclusion strategy
transaction isolation levels
connection pooling
Testcontainers
Flyway directory structure
outbox schema
backup strategy
production hosting/database provider
```

These now have a clear technology baseline.

---

# 25. Governance verdict

The main alternatives tested were:

```text
PostgreSQL
vs document/other relational storage

jOOQ
vs JPA/Hibernate
vs Spring Data JDBC
vs plain JDBC

Flyway
vs automatic ORM schema evolution
```

The difficult falsification case was not PostgreSQL; it was whether jOOQ's explicit mapping cost outweighed JPA convenience.

Given Main Street's emphasis on database-backed invariants, controlled aggregates, explicit queries, modular persistence ownership and likely PostgreSQL-specific scheduling/capacity constraints, it does not.

```text
MS-PROT-034
Database, Persistence Framework & Migration Technology Selection

PROPOSE                     ✓
FALSIFICATION REVIEW        ✓
REVISED                     ✓
CROSS-DOMAIN VALIDATION     ✓
ACCEPT                      ✓
```

## **Status: ACCEPTED**

### Canonical decision

> **Main Street will initially use PostgreSQL 18 as its authoritative transactional database, jOOQ as the default Java persistence technology, Spring-managed local transactions, and Flyway for explicit schema evolution. Relational persistence remains the authoritative operational default rather than a universal storage mandate, and the domain model remains independent of both jOOQ-generated schema types and database implementation details.**