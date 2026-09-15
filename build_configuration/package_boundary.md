# Main Street Production Package Boundaries

> **Status:** Current implementation navigation; not independent semantic authority.  
> **Authority:** `designs/AUTHORITY-INDEX.md`, accepted MS-PROT/TAS authorities and `designs/IMPLEMENTATION-RULES.md`.

## 1. Source-set boundary

```text
src/main
    production implementation only

src/test
    executable tests, falsification evidence and explicitly quarantined
    historical prototype fixtures
```

A class retained in `src/test` does not constitute a supported production extension point.

## 2. Dependency direction

The accepted backend dependency direction remains:

```text
delivery / external adapters
        ↓
application coordination
        ↓
domain / capability authorities
        ↓
domain contracts

infrastructure ──implements──► domain/application contracts
```

The domain must not depend on Spring controllers, SQL/jOOQ records, Flyway, HTTP transport, provider SDKs or other delivery/infrastructure mechanics merely to express business meaning.

## 3. Production package responsibilities

| Package | Responsibility |
|---|---|
| `mainstreet.semantic` | fundamental semantic primitives shared by the accepted semantic kernel |
| `mainstreet.semantic.registry` | immutable registered semantic definitions and registry snapshots |
| `mainstreet.semantic.configuration` | merchant configuration/release/publication/approval/activation contracts and compatibility evidence contracts |
| `mainstreet.semantic.compiler` | **canonical production compiler** from pinned merchant configuration + registered semantics |
| `mainstreet.semantic.executable` | immutable executable merchant model and resolved operation definitions |
| `mainstreet.runtime` | scoped dispatch, requirements and independent execution guards |
| `mainstreet.identitysecurity` | Identity-owned authentication security-generation state and mutation contracts |
| `mainstreet.application` | cross-capability progression/coordination without owning participating business truth |
| `mainstreet.booking` | Booking-owned business/application semantics; not generic notification delivery |
| `mainstreet.scheduling` | scheduling intent and appointment semantics |
| `mainstreet.inventory` | inventory/allocation authority |
| `mainstreet.customer` | CustomerContext authority |
| `mainstreet.businesshours` | merchant business-hours/time resolution |
| `mainstreet.merchantaccount` | Merchant Account establishment and lifecycle |
| `mainstreet.commercial` | commercial agreement, plans, trial and entitlement authority |
| `mainstreet.workforce` | merchant membership, groups, roles and operational-device authority |
| `mainstreet.notification` | generic Notification Intent/Dispatch/Delivery semantics |
| `mainstreet.money` | monetary/payment authority foundations |
| `mainstreet.privacy` | purpose-bound personal-data-use/retention foundations |
| `mainstreet.audit` | attributable append-only audit evidence contracts |
| `mainstreet.background` | durable background-work semantics |
| `mainstreet.media` | media asset/rendition semantics |
| `mainstreet.credential` | credential/security metadata contracts |
| `mainstreet.protection` | platform resource-protection admission |
| `mainstreet.resilience` | execution uncertainty, retry safety and resilience semantics |
| `mainstreet.observability` | operational health/diagnostic evidence model |
| `mainstreet.infrastructure.persistence.*` | PostgreSQL/jOOQ implementations of durable contracts; never semantic authority |

## 4. Canonical compilation boundary

Production configuration resolution is:

```text
MerchantConfiguration
    + exact SemanticRegistrySnapshot
    ↓
mainstreet.semantic.compiler.ConfigurationCompiler
    ↓
ExecutableMerchantModel
```

The historical mutable `mainstreet.semantic.capability` compiler/composition model is quarantined in test scope. In particular, these must not exist under `src/main`:

```text
mainstreet.semantic.capability.CapabilityCompiler
mainstreet.semantic.capability.OperationComposition
mainstreet.semantic.capability.CapabilityCompositionDefinition
```

The historical policy/capability fixture family that exists only to execute those prototype tests is likewise test-scope evidence.

## 5. Notification boundary

MS-PROT-075 owns generic notification delivery semantics. Production capabilities may establish why a notification exists and the facts it may communicate; they must not create independent provider-delivery stacks.

Therefore the historical Booking-specific classes are test-scope only:

```text
BookingNotificationGateway
BookingNotificationDelivery
NotificationDeliveryException
```

Production delivery coordination belongs under `mainstreet.notification` and preserves owner scope, current eligibility and delivery evidence.

## 6. Persistence boundary

Production persistence uses:

```text
PostgreSQL
+ jOOQ
+ Flyway
+ Spring transaction management
```

Rules:

- schema creation/evolution belongs to versioned Flyway migrations;
- domain classes do not perform runtime DDL;
- persistence adapters implement authoritative contracts rather than redefining them;
- transaction boundaries must preserve accepted multi-write invariants;
- durable identity/provenance must survive process restart;
- exact semantic/configuration affinity must not be replaced by latest-version lookup.

## 7. Mechanical enforcement

`mainstreet.governance.ImplementationBoundaryConformanceTest` fails if rejected prototype compiler/composition or Booking-specific notification-delivery classes return to `src/main`.

When a new package materially changes these boundaries, update this document only after checking the applicable accepted design authority. This document describes the implementation boundary; it does not create a new one.
