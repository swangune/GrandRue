# MAIN STREET

# Technical Architecture Specification (TAS)

## Production Backup, Restore, Corruption Recovery & Disaster Recovery Architecture

**Document ID:** MS-TAS-RECOVERY-001  
**Version:** 1.0  
**Status:** ACCEPTED after governed production-architecture review, falsification and manual approval  
**Authority class:** Production Architecture / TAS  
**Depends on:** MS-PROT-027, composite MS-PROT-053, MS-PROT-034, MS-PROT-040, MS-PROT-054, MS-PROT-064, MS-PROT-065, composite MS-PROT-066, MS-PROT-067, MS-PROT-068, MS-PROT-069, MS-PROT-070, MS-PROT-072, MS-PROT-074, MS-PROT-076, `docs/development/TAS/high-level-architecture.md`  
**Purpose:** Define Main Street's initial production architecture for high availability, backup, restore, corruption recovery and regional disaster recovery without allowing recovery infrastructure, backup copies, derived projections or external-provider state to become independent business authority.

---

# 1. Governing Principle

> **Recover authoritative state from independently protected recovery copies, rebuild what is derived, reconcile what lives outside Main Street, and never make a restored copy authoritative until integrity, security, privacy and execution-uncertainty checks have passed.**

This TAS implements accepted Main Street semantic authorities. It does not redefine capability ownership, business lifecycle, identity, commercial authority, Exposure, provider authority or data-protection semantics.

Canonical separation:

```text
High Availability
    ≠ Backup
    ≠ Point-in-Time Recovery
    ≠ Disaster Recovery
    ≠ Business Retention
    ≠ Business Authority
```

---

# 2. Initial Deployment Recovery Model

Main Street SHALL initially use an **active-primary / pilot-light regional disaster-recovery model**.

```text
PRIMARY REGION
    ├── stateless/recreatable application instances
    ├── multi-zone PostgreSQL high availability
    ├── authoritative PostgreSQL cluster
    ├── canonical media object storage
    ├── secret / credential-management infrastructure
    └── derived projections / caches

                 │
                 │ protected recovery copies / replication
                 ▼

SECONDARY RECOVERY REGION
    ├── cross-region PostgreSQL recovery data
    ├── protected canonical-media copies
    ├── recoverable security/KMS prerequisites
    ├── immutable application/release artefacts
    └── infrastructure required to recreate service

NO SECOND ACTIVE AUTHORITATIVE WRITER
```

The secondary region is recovery capability, not a concurrently authoritative Main Street deployment.

---

# 3. Recovery Data Classes

Main Street SHALL classify recoverable state by authority and reconstruction semantics.

| Recovery class | Examples | Initial recovery treatment |
|---|---|---|
| **Authoritative transactional state** | Merchant Account, Configuration, Orders, Bookings, Inventory, Commercial state, Identity relationships, Staff authority, AuditRecords, durable work/outbox/process state | PostgreSQL HA + cluster-level PITR |
| **Canonical binary state** | original merchant images, videos and documents | independently protected object storage / versioned recovery copies |
| **Security state** | secrets, signing keys, provider credentials | secret/KMS recovery path; not generic DB restore |
| **Derived state** | search, caches, analytics projections, media renditions | rebuild from recovered authority |
| **External state** | provider payments, refunds, Google/provider state, delivery/provider evidence | reconcile with external authority/evidence |
| **Release state** | application artefact, Flyway migrations, semantic registry release, deployment specification | immutable Recovery Release Bundle |

One backup mechanism SHALL NOT be assumed sufficient for all classes.

---

# 4. Authoritative PostgreSQL Recovery Boundary

PostgreSQL remains Main Street's default authoritative transactional store under MS-PROT-034.

For the initial modular-monolith deployment, the authoritative recovery unit SHALL be the **PostgreSQL cluster / mutually consistent transactional recovery point**, not independently selected capability tables.

Rejected:

```text
Booking tables restored to T1
Inventory tables restored to T2
Commercial tables restored to T3
```

where T1, T2 and T3 could represent a state that never existed atomically.

Required:

```text
one trusted PostgreSQL recovery point T
        ↓
capability-owned transactional state reconstructed
with the same database transaction boundaries that
were authoritative at T
```

Scoped logical repair after recovery remains permitted where the owning authority supports it; this rule prohibits constructing inconsistent historical authority by arbitrary per-table restore points.

---

# 5. Initial Recovery Objectives

These are internal engineering objectives for the initial production architecture. They are not customer-facing SLA promises unless separately adopted commercially.

## 5.1 Ordinary database instance / availability-zone failure

```text
PostgreSQL RPO target:
    0 committed transactions

PostgreSQL RTO target:
    ≤ 15 minutes
```

The selected managed PostgreSQL/high-availability topology SHALL provide synchronous or equivalently durable in-region replication/failover capable of supporting this objective.

## 5.2 Full primary-region disaster

```text
Authoritative PostgreSQL RPO target:
    ≤ 5 minutes

Core-service RTO target:
    ≤ 4 hours
```

Core service means sufficient trusted authority and essential projections to resume safe Main Street operation; it does not require every analytical or low-priority derived artefact to be rebuilt before service resumes.

## 5.3 Canonical media

```text
regional-loss canonical-media RPO target:
    ≤ 15 minutes

priority referenced-media recovery target:
    ≤ 4 hours

complete bulk rendition/media restoration target:
    ≤ 24 hours
```

## 5.4 Derived state

Regenerable derived state does not require a conventional business-data RPO when it can be reconstructed safely from recovered authority.

Targets:

```text
essential operational/public projections:
    rebuild ≤ 4 hours

non-critical analytics / bulk derivations:
    rebuild ≤ 24 hours
```

---

# 6. PostgreSQL Backup and PITR Strategy

The initial production PostgreSQL recovery contract SHALL support:

```text
CONTINUOUS
    WAL / equivalent point-in-time recovery data archival

DAILY
    incremental/base-chain backup or equivalent managed recovery capture

WEEKLY
    full base backup or equivalent independently verifiable full recovery point

RETENTION
    minimum 30-day point-in-time recovery window
```

A managed PostgreSQL service MAY implement these mechanics differently if it satisfies or exceeds the same recovery behaviour, isolation, integrity and testing requirements.

This TAS specifies recovery outcomes rather than mandating one backup utility or cloud product.

---

# 7. Replication Is Not Backup

A high-availability replica MAY reproduce accidental deletion, bad migration, malicious mutation or logical corruption.

Therefore:

```text
Replica
    ≠ Backup
```

Main Street SHALL maintain recovery history that is independently protected from ordinary replication and application mutation paths.

---

# 8. Backup Is Not Business Retention

Backup retention serves infrastructure recovery.

Business/evidence retention is governed by MS-PROT-053 and the owning business/audit authorities.

```text
Business Retention
    preserves required authoritative/evidentiary facts

Backup Retention
    preserves recoverability of infrastructure state
```

Backups SHALL NOT be retained indefinitely merely to serve as a substitute business archive.

The initial PITR window is 30 days unless a later approved production/legal requirement changes it.

---

# 9. Recovery-Store Isolation

Production application credentials SHALL NOT have authority to destroy Main Street's recovery history.

Required control boundary:

```text
Application Principal
        ─X→ delete/disable protected recovery repository

Backup / Recovery Administrative Principal
        → independently protected recovery system
```

Recovery data SHALL use appropriate:

- encryption;
- independent administrative credentials;
- deletion protection / immutability where available;
- separate failure-domain or region placement;
- restricted restore/promotion authority; and
- auditable recovery administration where required by MS-PROT-064.

A compromise of an ordinary application credential MUST NOT automatically compromise both live data and recovery copies.

---

# 10. Recovery Manifest

Each recoverable PostgreSQL backup chain/recovery point SHALL preserve enough metadata to determine compatibility and provenance.

Conceptually:

```text
RecoveryManifest
{
    backupIdentity
    sourceClusterIdentity
    captureStart / captureEnd
    recoverableTimeRange / WAL position metadata
    PostgreSQLVersion
    schema / FlywayVersion
    applicationRelease
    semanticRegistryRelease
    encryptionKeyReference
    verificationResult
    canonicalMediaRecoveryReference where applicable
}
```

`RecoveryManifest` is recovery metadata. It is not a source of business truth.

---

# 11. Backup Verification

A successful backup-upload job SHALL NOT be treated as proof that Main Street is recoverable.

```text
backup job succeeded
    ≠ backup is restorable
```

The production recovery process SHALL include:

```text
EVERY BACKUP CHAIN / RECOVERY SET
    structural/integrity verification appropriate to the selected technology

MONTHLY
    isolated restore exercise

QUARTERLY
    end-to-end disaster-recovery exercise
```

For PostgreSQL mechanisms that support backup manifests/verification utilities, those facilities SHOULD be used. Managed-provider equivalents are acceptable when they establish comparable evidence.

---

# 12. Restore Exercise Requirements

A restore drill MUST do more than verify archive presence.

At minimum, an applicable isolated restore exercise SHALL:

1. restore PostgreSQL to the selected recovery point;
2. start the database cleanly;
3. verify backup/storage integrity;
4. run applicable database consistency checks;
5. verify Flyway/schema compatibility;
6. verify semantic-registry/application-release compatibility;
7. start a compatible Main Street application build;
8. run representative authority and tenant-isolation checks;
9. verify durable work/outbox references;
10. verify canonical-media references where applicable; and
11. record actual recovery duration and failures.

An untested recovery procedure SHALL NOT be described operationally as proven.

---

# 13. Database Corruption Detection

PostgreSQL data checksums SHALL remain enabled for governed production data files.

Main Street SHALL additionally use scheduled database consistency/integrity inspection such as `pg_amcheck` or an equivalent managed mechanism where appropriate.

These mechanisms SHALL NOT be interpreted as proof that every possible logical corruption is absent.

Recovery confidence therefore requires:

```text
storage/page integrity evidence
+
database structural integrity evidence
+
Main Street business-invariant checks
```

---

# 14. Corruption Classes

Incident response SHALL distinguish at least:

```text
PHYSICAL CORRUPTION
    page/index/storage damage

LOGICAL CORRUPTION
    database writes are technically valid but business data is wrong

ACCIDENTAL DESTRUCTIVE OPERATION
    unintended delete/update/operator action

BAD MIGRATION
    schema/data migration damages authoritative meaning

CANONICAL MEDIA CORRUPTION
    canonical source missing/damaged

DERIVED DATA CORRUPTION
    cache/index/rendition/projection damaged
```

The smallest safe recovery scope SHALL be preferred.

A corrupt search index SHALL normally be rebuilt rather than triggering authoritative-database rollback. A reconstructable database index defect SHALL not automatically require full-cluster PITR.

---

# 15. Unknown Authoritative Corruption

If the scope or integrity of current authoritative data cannot be safely established, Main Street SHALL prefer temporary authoritative-write unavailability over continuing to create business mutations against potentially corrupted truth.

This follows MS-PROT-070's preservation order:

```text
Security / Privacy
    ↓
Authoritative Business Invariants
    ↓
Existing Commitments / Evidence
    ↓
Safe Availability
```

---

# 16. Logical Corruption Recovery

Whole-cluster rollback is not the default answer to every logical corruption.

Where valid post-corruption business activity would otherwise be lost, Main Street SHOULD prefer:

```text
detect corruption
        ↓
stop the corrupting execution path
        ↓
preserve current incident evidence
        ↓
restore clean historical candidate in isolation
        ↓
identify affected authoritative facts
        ↓
reconstruct / compare
        ↓
perform governed forward repair through
accepted owning authority / intervention path
        ↓
verify
```

Full PITR promotion MAY be used where the current authoritative state cannot safely be repaired or where corruption effectively compromises the entire authoritative boundary.

Recovery SHALL NOT rewrite business history merely for technical convenience.

---

# 17. Recovery Candidate Lifecycle

A backup/restored copy SHALL NOT become production authority merely because it can start.

Canonical recovery stages:

```text
BACKUP ARTIFACT
        ↓
RESTORED CANDIDATE
        ↓
VALIDATED RECOVERY CANDIDATE
        ↓
PROMOTED AUTHORITATIVE STORE
```

Before promotion:

```text
RESTORED CANDIDATE
    has no production mutation authority
```

Promotion requires the validation and fencing gates in this TAS.

---

# 18. Recovery Candidate Validation Gate

Before a restored PostgreSQL candidate becomes authoritative, operators/automation SHALL verify all applicable controls, including:

- backup/recovery-set integrity;
- PostgreSQL startup and structural integrity;
- Flyway schema compatibility;
- compatible application and semantic registry release;
- Merchant Scope / tenant isolation;
- critical relational/database invariants;
- durable work/outbox/process consistency;
- required AuditRecord availability;
- Merchant Account/controller invariants;
- Configuration Revision / activation affinity;
- Commercial/trial provenance and consistency;
- Inventory/allocation constraints where applicable;
- media-reference reconciliation;
- security/privacy reconciliation;
- provider/external-effect reconciliation plan; and
- absence of known unresolved corruption that would make promotion unsafe.

Failed required validation means:

```text
DO NOT PROMOTE
```

---

# 19. Recovery Release Bundle

A database recovery point may require an earlier compatible application/schema/semantic release.

For at least the operational recovery window, production releases SHALL preserve a recoverable bundle containing enough material to reproduce the accepted runtime around a restored database, conceptually:

```text
RecoveryReleaseBundle
{
    application artefact / image
    Flyway migration chain
    semantic registry release
    required configuration schema
    deployment / infrastructure specification
}
```

Recovery SHALL NOT pair an old database blindly with the latest application binary.

Canonical progression:

```text
restore database at compatible schema/release
        ↓
validate with compatible release
        ↓
apply accepted Flyway migrations as required
        ↓
advance through compatible deployment path
```

---

# 20. Canonical Media Recovery

Under the composite MS-PROT-066 authority:

```text
Canonical Media Source
    authoritative canonical bytes
        ↓
MUST be recoverable

Media Rendition
    deterministic derived representation
        ↓
normally rebuildable
```

Canonical media SHALL use independent/versioned/cross-failure-domain protection sufficient to meet the targets in Section 5.

Derived thumbnails, responsive images, video renditions, posters and other reproducible technical artefacts SHOULD normally be regenerated rather than treated as authoritative backup state.

---

# 21. Database and Object-Store Reconciliation

A PostgreSQL recovery point SHALL NOT require blindly rolling the complete object store to exactly the same wall-clock time.

Required pattern:

```text
recovered PostgreSQL MediaAsset / attachment authority
        ↓
canonical media storage / versions
        ↓
reconcile required objects
```

Possible states:

```text
object exists but recovered authority no longer references it
    → unreferenced recovery candidate; not automatically exposed

recovered authority references canonical object but object is missing
    → recovery defect; restore object/version or intervene
```

The existence of bytes in object storage SHALL NOT itself create a business attachment or public Exposure.

---

# 22. Post-Target Privacy and Security Reconciliation

Restoring an earlier historical point MUST NOT silently resurrect a privacy/security permission that was valid at the recovery point but revoked later.

Examples include:

- PersonalDataUseBasis withdrawal;
- personal-data restriction/erasure disposition;
- public-media Exposure withdrawal;
- credential/security revocation;
- Merchant Account suspension;
- other accepted security/privacy restriction whose later effect remains applicable.

Before affected protected/public surfaces reopen, recovery SHALL establish and reconcile applicable post-target high-risk restrictions through their owning authorities.

Conceptually:

```text
restore point T
        ↓
identify applicable security/privacy
changes after T that must survive recovery
        ↓
reconcile/reapply through owning authority
        ↓
only then expose affected surface
```

If the required post-target security/privacy state cannot be established safely:

```text
UNKNOWN
    ↓
FAIL CLOSED for the affected protected surface
```

This rule does not require every ordinary post-target business mutation to be reapplied; it protects current revocation/privacy requirements from being defeated by recovery.

---

# 23. Authentication Session Recovery

A production recovery involving point-in-time rollback or regional disaster promotion SHALL invalidate pre-recovery authenticated Session continuity.

Users and staff reauthenticate against recovered/current authority.

This does not terminate:

- Merchant Controller relationships;
- Merchant Memberships;
- Role Assignments; or
- Merchant Operational Device Authorisations

unless their owning authoritative state separately requires it.

```text
Session invalidation
    ≠ business relationship termination
```

---

# 24. Secrets and Credentials

Raw secrets SHALL NOT be restored from generic PostgreSQL backups.

PostgreSQL may recover references such as:

```text
ProviderConnection
CredentialReference
credential generation reference
```

but the active secret/security system remains authoritative for whether the referenced credential generation currently exists and remains usable.

A revoked credential SHALL NOT be resurrected merely because an older database backup references it.

Where a recovered reference cannot resolve to valid current security material, the affected provider/security operation SHALL remain unavailable/recovery-required according to its owning authority.

---

# 25. External Provider State

Main Street backups cannot restore authoritative state owned by external providers.

For side-effecting provider operations around the incident/recovery window, recovery SHALL use provider reconciliation/evidence according to MS-PROT-048/MS-PROT-069 rather than blindly repeating the operation.

Examples include, where applicable:

```text
payments
refunds
courier requests
domain/provider operations
external scheduling integrations
social/publication provider operations
```

Canonical rule:

```text
restored Main Street state
+
external provider evidence
        ↓
reconciliation

NOT
        ↓
blind repeat of side effect
```

---

# 26. Durable Background Work and Outbox Recovery

Physical queue/broker contents SHALL NOT be the sole recovery authority for durable Main Street responsibilities.

Under MS-PROT-065/MS-PROT-072:

```text
DurableWorkInstruction
Outbox-equivalent delivery intent
Durable process progression state
        ↓
recover from authoritative durable state
        ↓
re-evaluate current authority/state
        ↓
resume safely
```

A queue/broker MAY be rebuilt/repopulated from durable responsibility where the accepted implementation supports it.

Main Street SHALL NOT attempt to restore database authority to one historical point and independently restore queue contents to an incompatible point that could manufacture duplicate or impossible business effects.

Exactly-once delivery SHALL NOT be assumed during recovery.

---

# 27. Event and Side-Effect Replay

Recovery SHALL NOT blindly replay every historical event or side-effecting instruction.

Preferred recovery modes:

```text
Derived projection
    → rebuild from recovered/current authoritative state where valid

Externally side-effecting work
    → reconcile before retry/re-execution

Current durable work
    → revalidate current authoritative conditions before execution
```

Stored coordinator/event context remains provenance/progression evidence rather than automatic mutation authority.

---

# 28. Derived-State Recovery Order

After authoritative state is validated/promoted, derived state SHOULD be restored progressively in approximately this priority:

```text
1. authentication/runtime-critical projections
2. merchant operational projections
3. public storefront projections
4. active referenced media renditions
5. search/discovery projections
6. analytics
7. lower-priority historical aggregates
```

The exact ordering MAY vary when an owning feature has a stronger accepted dependency, but performance/convenience MUST NOT outrank security/privacy/invariant correctness.

---

# 29. Primary-Region High Availability

The initial production topology SHALL support:

```text
Application
    multiple recreatable/stateless instances
    across at least two provider failure zones where available

PostgreSQL
    managed primary
    + synchronous/equivalently durable HA standby

Canonical Media
    durable managed object storage

Secrets / encryption keys
    managed security/KMS infrastructure
```

Application-instance failure SHALL not require database restore.

Database-instance/availability-zone failure SHOULD use high-availability failover before backup restoration.

---

# 30. Regional Disaster Recovery

Initial regional DR SHALL be **pilot-light / restore-and-promote**, not active-active authoritative mutation.

Rejected for initial architecture:

```text
Region A writable authority
+
Region B independently writable authority
```

because Main Street has no accepted distributed-authority model for concurrently mutable Inventory, Appointment allocation, Merchant control, Configuration activation or Commercial state.

Temporary regional write unavailability is preferable to split-brain authority.

---

# 31. Regional Promotion Authority

In-region managed HA failover MAY be automatic where the provider mechanism preserves the accepted single-authority invariant.

Full regional disaster promotion SHALL initially require explicit authorised operational approval.

The promotion decision MUST consider at least:

- whether the prior primary is fenced;
- the latest trusted recovery point;
- whether corruption caused the incident;
- whether side-effecting provider operations may exist after the recovery point;
- whether security/privacy post-target restrictions are reconciled; and
- whether the recovery candidate validation gate passed.

A generic health alert SHALL NOT itself authorise regional promotion.

---

# 32. Fencing

Before a secondary/recovered environment accepts authoritative writes, the prior writer SHALL be fenced sufficiently to prevent competing authoritative mutation.

```text
SECONDARY / RECOVERED PROMOTION
        requires
PRIOR WRITE AUTHORITY FENCED
```

This applies even if the prior region appears unavailable. A later network recovery MUST NOT produce two independent primaries.

---

# 33. End-to-End Regional Recovery Sequence

The canonical recovery progression is:

```text
INCIDENT DECLARED
        ↓
stop / fence authoritative writes
        ↓
classify failure / corruption scope
        ↓
select latest trusted recovery point
        ↓
restore PostgreSQL to isolated candidate
        ↓
verify backup/database integrity
        ↓
verify application/schema/semantic compatibility
        ↓
reconcile post-target security/privacy state
        ↓
reconcile canonical media
        ↓
reconcile external side-effect providers
        ↓
invalidate stale Sessions
        ↓
rebuild essential projections
        ↓
validate Main Street business invariants
        ↓
PROMOTE recovered PostgreSQL authority
        ↓
enable constrained core operations
        ↓
resume/re-evaluate durable work
        ↓
progressively rebuild secondary features
        ↓
FULL SERVICE
```

Where evidence remains uncertain, MS-PROT-069 uncertainty/reconciliation semantics continue to apply.

---

# 34. Recovery Observability

Production operations SHOULD expose bounded recovery evidence including:

```text
last successful backup
last verified backup
oldest recoverable PITR point
WAL/recovery archive lag
backup verification failures
last successful restore drill
actual tested RPO
actual tested RTO
canonical-media replication/recovery lag
recovery-candidate validation result
provider reconciliation backlog
projection rebuild progress
```

These are operational observations under MS-PROT-068. They do not become business truth.

---

# 35. Business-Invariant Recovery Tests

A restored environment SHALL test representative Main Street invariants, not merely SQL readability.

The test catalogue SHALL include applicable checks such as:

```text
Merchant Scope isolation

exactly one ACTIVE Merchant Controller
for each OPEN/CLOSING Merchant Account

zero ACTIVE Merchant Controllers
for CLOSED Merchant Accounts

Configuration Revision / activation affinity

Commercial entitlement / grant provenance consistency

Booking / Appointment lifecycle consistency

Inventory / allocation consistency

durable work/outbox references resolve

MediaAsset canonical references resolve

protected/public Exposure does not bypass
current security/privacy restrictions
```

The specific executable test implementation remains downstream, but the recovery acceptance gate MUST verify semantically meaningful integrity.

---

# 36. Disaster-Recovery Exercise Cadence

Initial cadence:

```text
MONTHLY
    isolated restore drill

QUARTERLY
    regional disaster-recovery simulation

AFTER MATERIAL PERSISTENCE CHANGE
    targeted recovery validation

AFTER MAJOR SCHEMA / MIGRATION CHANGE
    restore + migration compatibility exercise
```

Each exercise SHALL record, at minimum:

- achieved RPO;
- achieved RTO;
- failed/manual recovery steps;
- missing artefacts/documentation;
- provider-reconciliation issues;
- security/privacy reconciliation issues; and
- corrective actions.

Documented objectives that have not been exercised SHALL not be represented as proven operational capability.

---

# 37. Trade-off Decision

The following initial alternatives were evaluated.

## 37.1 Backup only

Lower infrastructure cost but weak availability and slower recovery.

**Rejected.**

## 37.2 Multi-zone HA + pilot-light regional DR

Provides strong ordinary-failure continuity and recoverable regional disaster handling without introducing concurrent regional authority.

**ACCEPTED.**

## 37.3 Active-active multi-region

Could reduce theoretical regional recovery time, but requires distributed write authority, conflict/consistency semantics, split-brain control and substantially greater operational complexity.

**Rejected for the initial Main Street architecture.**

A future active-active design would require separate architectural/semantic evidence and approval.

---

# 38. Provider-Neutral Implementation Boundary

This TAS does NOT select:

```text
cloud provider
managed PostgreSQL product
object-storage provider
backup product
secret/KMS product
infrastructure-as-code tool
monitoring/alerting product
CDN
queue/broker product
```

Any selected implementation SHALL satisfy this TAS and the accepted semantic authorities.

Concrete product selection MAY be governed by later TAS/ADR decisions where material.

---

# 39. Hard Architecture Invariants

1. Replication SHALL NOT be treated as Backup.
2. Backup SHALL NOT be treated as Business Retention.
3. Backup Artifact SHALL NOT become business authority merely because it is restorable.
4. PostgreSQL cluster-level PITR is the initial authoritative transactional recovery boundary.
5. Capability tables SHALL NOT be independently restored to incompatible historical recovery points.
6. Primary-region PostgreSQL HA SHALL target RPO 0 committed transactions and RTO ≤ 15 minutes.
7. Regional authoritative PostgreSQL DR SHALL target RPO ≤ 5 minutes and core-service RTO ≤ 4 hours.
8. A minimum 30-day point-in-time recovery window SHALL be maintained.
9. Recovery copies SHALL reside behind an administrative/failure boundary independent of ordinary application credentials.
10. Backup integrity and restore capability SHALL be verified rather than inferred.
11. PostgreSQL checksums SHALL remain enabled; database integrity inspection SHALL be combined with business-invariant validation.
12. Logical corruption SHOULD prefer scoped governed forward repair where whole-platform rollback would destroy valid later business history.
13. Canonical Media Sources SHALL be recoverable.
14. Reproducible Media Renditions and other derived projections need not be backed up as independent authority.
15. Raw secrets SHALL NOT be recovered from generic PostgreSQL backups.
16. Credential revocation SHALL NOT be undone by restoring an older database reference.
17. External provider effects SHALL be reconciled rather than blindly recreated.
18. Applicable post-target privacy/security restrictions SHALL be reconciled before protected/public exposure resumes from an older restore.
19. Unknown required security/privacy recovery state SHALL fail closed for the affected protected surface.
20. Pre-recovery Sessions SHALL NOT remain trusted after PITR/regional recovery.
21. A regional secondary/recovered environment SHALL NOT accept authoritative writes while the previous writer remains unfenced.
22. Initial regional DR SHALL be active-primary/pilot-light, not active-active.
23. Full regional promotion SHALL initially require explicit authorised operational approval.
24. Derived state SHALL be rebuilt from recovered authority according to priority and accepted exposure/security rules.
25. Durable background responsibility SHALL recover from durable authoritative responsibility rather than physical queue contents alone.
26. Recovery candidates SHALL pass integrity, compatibility, security/privacy and semantic validation before promotion.
27. Recovery objectives SHALL be measured through recurring restore and DR exercises.
28. Recovery infrastructure SHALL remain subordinate to accepted business, security, privacy and provider authorities.

---

# 40. Falsification Summary

The architecture was falsified against:

- primary database-instance failure;
- availability-zone loss;
- full-region loss;
- previous primary returning after secondary promotion;
- accidental bulk Product deletion;
- bad migration affecting multiple capability tables;
- isolated search/projection corruption;
- lost media renditions with intact canonical sources;
- canonical media loss;
- personal-media permission withdrawal after the selected restore point;
- external payment success after the selected database recovery point;
- physical queue/broker loss;
- old database backup with older schema/application release;
- apparently successful but unusable backup artefact;
- compromised application credential;
- failed post-restore business-invariant check; and
- delayed reconstruction of non-critical analytics.

No tested scenario requires active-active regional authority, independent per-capability recovery timestamps, queue-as-authority recovery, secret resurrection or backup copies to become independent business truth.

---

# 41. Acceptance Statement

Main Street's initial production recovery model combines **multi-zone high availability**, **independently protected point-in-time recovery**, **canonical-media protection**, **security/provider reconciliation**, **validated recovery-candidate promotion**, and **pilot-light regional disaster recovery**.

> **Protect authority independently, restore one coherent transactional truth, rebuild derivatives, reconcile external and post-target security state, fence competing writers, validate before promotion, and prove recovery through exercises.**

---

## End — Production Backup, Restore, Corruption Recovery & Disaster Recovery Architecture
