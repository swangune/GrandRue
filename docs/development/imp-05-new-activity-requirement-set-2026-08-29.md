# IMP-05 — Configuration New-Activity Requirement-Set Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** D5c1 — Deterministic RCP-affined requirement-set evidence
**Date:** 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements the requirement derivation, canonical identity and
durable evidence rules accepted by:

- `designs/MS-PROT-040 v1.2 — Initial Merchant Configuration Bootstrap & Serving-Deployment Admission Amendment.md`;
- `designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md`;
- `designs/MS-PROT-040 v1.4 — Serving Deployment Admission Evidence & Generation Fence Amendment.md`;
- `docs/foundation/adr/runtime-semantic-execution-compatibility.md` (ADR-012);
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md`.

The accepted boundary requires the Configuration owner to derive every
statically applicable new-activity execution requirement from one exact RCP
and release-affined Semantic Registry mappings. Callers cannot supply a
resolved set or a completeness boolean.

## 2. Implemented production boundary

The D5c1 implementation provides:

- a release-affined operation-to-execution-requirement mapping contract;
- a deterministic resolver that consumes every executable operation in the
  exact RCP and fails closed when any mapping is absent or ambiguous;
- the version-1 unsigned-UTF-8, length-prefixed canonical encoding and exact
  `ms-reqset-v1:sha256:<lowercase-hex>` identity;
- an immutable evidence model retaining merchant, Configuration Revision,
  Semantic Registry Release, resolved-package evidence, canonicalization
  version, digest, grouped exact requirements and production time;
- a PostgreSQL/jOOQ authority that accepts the exact RCP rather than
  caller-supplied requirements, proves its D2b validation/package affinity,
  derives the set and records it atomically; and
- normalized header, affected-requirement and participant/effect-contract
  tables whose foreign keys preserve exact package, release and grouping
  affinity.

The resolved-package evidence identifier is the evidence object's immutable
package affinity. The requirement-set identifier is deterministic content
identity; the same content may therefore remain reconstructable for distinct
validated packages without collapsing their historical evidence.

## 3. RED → GREEN evidence

RED first established that no canonical identity authority existed. The
focused compile failed on the missing
`ConfigurationNewActivityRequirementSetIdentity` symbol.

GREEN added the versioned canonical primitive, fixed test vector, exact
release-affined resolver, immutable evidence contracts, Flyway migration and
PostgreSQL adapter. Focused verification then proved:

- identity independence from requirement and participant input order;
- identity change for a material contract change;
- complete exact-RCP operation mapping and fail-closed missing mapping;
- cross-release mapping rejection;
- normalized durable reconstruction of affected and participant/effect
  contracts;
- exact retry returning the original evidence;
- rejection without exact D2b validation/package evidence; and
- rejection when one package evidence identity is reused for changed
  immutable intent.

The first PostgreSQL RED exposed a truncated constraint-name collision and the
first canonical RED exposed one older non-cascading shared-schema cleanup.
Both were corrected explicitly, and focused regression verification passed
before the canonical rerun.

## 4. Canonical verification

The isolated canonical gate ran only after confirming no competing MainStreet
Maven process was using the shared target directory or PostgreSQL schema.
Editor Java autobuild was disabled for the run and restored afterward.

```text
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           41
production Java sources compiled:      670
test Java sources compiled:            237
unit / conformance tests:              667 PASS
PostgreSQL integration tests:          245 PASS
total Maven tests:                     912 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

## 5. Explicit non-claims and next node

D5c1 does not claim that a serving generation exists, that a release and
bundle digest are materialised, or that any implementation path covers the
recorded requirements. Those are D5c2 responsibilities.

D5c2 immutable serving-generation snapshot/support evidence is the smallest
critical **READY** node. D5c3 follows it. D5c4 activation-side integration
remains blocked by active decision `MS-PROT-040-V12-DQ-001`, which must govern
the durable current release-purpose admission authority before activation may
consume that predicate.
