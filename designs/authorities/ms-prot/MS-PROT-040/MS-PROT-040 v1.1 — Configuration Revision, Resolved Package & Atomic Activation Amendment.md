# MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment

**Document ID:** MS-PROT-040  
**Version:** 1.1  
**Status:** **ACCEPTED after governed review, falsification and manual approval**  
**Amends:** MS-PROT-040 v1.0  
**Depends on:** MS-PROT-021, MS-PROT-022 v1.5, MS-PROT-040 v1.0, MS-PROT-054, MS-PROT-059, MS-PROT-062, MS-PROT-069, MS-PROT-071  
**Purpose:** Remove ambiguity at the boundary among authoritative Merchant Configuration revisions, deterministic Resolved Configuration Packages and atomic activation, including exact approval affinity, semantic-registry pinning, first-activation concurrency, retry/idempotency and runtime package binding.

---

## 1. Governing decision

The following MUST remain distinct:

```text
Merchant Configuration Revision
    AUTHORITATIVE merchant configuration

Resolved Configuration Package
    DERIVED deterministic executable representation

Configuration Activation
    AUTHORITATIVE fact selecting which revision governs applicable new operations
```

Therefore:

```text
revision exists
    ≠ revision validated
    ≠ revision approved
    ≠ resolved package produced
    ≠ revision active
```

A Resolved Configuration Package MUST NOT become a competing Merchant Configuration authority.

---

## 2. Ownership

The Merchant Configuration context OWNS:

- Configuration Revision identity;
- revision lineage/base revision;
- merchant approval provenance;
- which revision is active;
- supersession/reinstatement decisions.

The compiler/resolution subsystem deterministically derives Resolved Configuration Packages from authoritative inputs. It MUST NOT approve or activate Merchant Configuration.

---

## 3. Configuration Revision identity and immutability

Every materialised Configuration Revision MUST have a stable identity scoped to exactly one Merchant Account.

Two revisions are the same revision only when both `merchantIdentifier` and `configurationRevisionIdentifier` are equal.

Once a revision is submitted for deterministic validation, the semantic contents identified by that revision identity MUST NOT mutate.

Any material change after validation creates a different revision identity and requires the applicable validation and approval lifecycle again.

---

## 4. Semantic-registry affinity

Every validated Configuration Revision MUST identify the exact semantic-registry release against which it was validated.

The compiler MUST NOT silently substitute a newer registry release during approval, package production or activation.

If a different registry release must govern the revision, compatibility/migration MUST follow MS-PROT-054. A materially changed merchant configuration outcome requires a new valid activation candidate.

---

## 5. Validation and Resolved Configuration Package production

A Configuration Revision is VALIDATED for activation only when deterministic static resolution using the exact Configuration Revision and exact pinned semantic-registry release succeeds.

The resulting Resolved Configuration Package MUST identify at least:

```text
merchantIdentifier
sourceConfigurationRevisionIdentifier
semanticRegistryReleaseIdentifier
```

and preserve the provenance required by MS-PROT-022 v1.5.

An implementation MAY combine validation and package materialisation in one deterministic pass. Equivalent authoritative inputs MUST produce equivalent resolved semantics.

---

## 6. Approval affinity

Merchant approval MUST bind to one exact immutable Configuration Revision.

Approval provenance MUST establish at least:

```text
merchantIdentifier
configurationRevisionIdentifier
authorised approving principal
approval instant
```

Approval of one revision MUST NOT transfer automatically to a materially different revision.

Inference, compilation, package materialisation or platform defaults MUST NOT constitute merchant approval where merchant approval is required.

---

## 7. Activation preconditions

`ActivateConfigurationRevision` may succeed only when all of the following are true:

1. the revision belongs to the target Merchant Account;
2. the revision is immutable for the activation attempt;
3. deterministic validation succeeded;
4. all required merchant approval exists for that exact revision;
5. a valid Resolved Configuration Package exists for the exact revision and pinned registry release;
6. that package identifies the revision as its source;
7. no applicable blocking impact finding remains;
8. the base-revision concurrency predicate is satisfied;
9. the initiating principal is authorised to activate the revision.

No undefined `ready`, `valid`, `active enough` or equivalent predicate may replace these conditions.

---

## 8. First activation concurrency

For the first Merchant Configuration activation:

```text
candidate.baseRevision = ABSENT
AND
currentActiveRevision = ABSENT
```

MUST both be true.

For every replacement activation:

```text
candidate.baseRevision = currentActiveRevision
```

MUST be true.

If the predicate is false, activation MUST return `ACTIVATION_CONFLICT` and MUST NOT silently overwrite the current active revision.

---

## 9. Authoritative activation fact

A successful activation establishes the authoritative fact:

> For the identified Merchant Account, the identified Configuration Revision became authoritative for applicable new activity at the committed activation instant.

Conceptually:

```text
ConfigurationActivation
{
    merchantIdentifier
    configurationRevisionIdentifier
    resolvedPackageProvenance
    activatedAt
    replacedRevisionIdentifier?
}
```

This structure is conceptual and does not mandate one Java type or table.

The Configuration Revision remains the authoritative merchant configuration; the Resolved Configuration Package is derived execution material.

---

## 10. Atomic activation boundary

For replacement activation, the following change MUST commit atomically:

```text
previous revision no longer governs new activity
+
new revision governs new activity
```

For first activation, the following MUST commit atomically:

```text
no current active revision
+
first revision becomes active
```

There MUST NOT be an observable committed state with two competing active revisions for the same new-operation configuration scope, nor a transient committed gap caused only by a half-completed replacement.

---

## 11. Package production is outside the activation transaction

Deterministic package materialisation SHOULD occur before the short activation transaction.

Canonical flow:

```text
immutable candidate revision
    ↓
deterministic validation/static resolution
    ↓
immutable Resolved Configuration Package
    ↓
approval/preconditions
    ↓
short atomic activation transaction
```

The activation transaction MUST revalidate package/revision/registry affinity and the concurrency predicate before commit.

Package materialisation MUST NOT itself confer operational authority.

---

## 12. Compilation succeeds but activation fails

A Resolved Configuration Package MAY exist for a revision that never becomes active.

Such a package has no independent runtime authority and MAY be retained, reused only for identical still-applicable inputs, or later discarded according to implementation/storage policy.

Activation failure MUST leave the current active revision unchanged unless the activation commit actually succeeded.

---

## 13. Activation idempotency and lost acknowledgement

Every retriable activation request MUST have a logical activation-request identity sufficient to recognise the same logical intent.

If an activation commit succeeds but acknowledgement is lost, retry of the same logical activation request MUST resolve to the already-committed historical outcome and MUST NOT create another activation effect.

If a later revision has since become active, retrying the earlier successful activation request MUST NOT reinstate the earlier revision.

Retry of an old activation request and reinstatement are distinct operations.

---

## 14. Reinstatement

MS-PROT-040 v1.0 remains authoritative: reinstatement is a new activation decision, not time travel.

A superseded revision MUST NOT become active merely because an earlier activation request is replayed.

A reinstatement request MUST satisfy current validation, compatibility, authority, approval and activation rules.

---

## 15. Runtime package binding

A runtime operation that depends on Merchant Configuration MUST bind to one exact Resolved Configuration Package before evaluating configuration-dependent semantics.

Canonical sequence:

```text
resolve applicable Configuration Revision
    ↓
resolve package derived from that revision
    ↓
bind invocation to that package
    ↓
evaluate and execute
```

An in-flight invocation MUST NOT switch to a different package merely because a newer revision activates after binding.

Historical commitment affinity remains governed by MS-PROT-040 v1.0 and the owning capability authority.

---

## 16. Historical reproducibility

Where an existing commitment depends materially on historical Merchant Configuration, Main Street MUST retain either:

- the exact historical Resolved Configuration Package; or
- all immutable authoritative inputs required to reconstruct an equivalent package deterministically.

Current active Merchant Configuration MUST NOT silently reinterpret such historical commitments.

---

## 17. Failure and rejection classes

The activation contract MUST distinguish, where applicable:

```text
VALIDATION_REJECTION
APPROVAL_REQUIRED
AUTHORISATION_REJECTION
ACTIVATION_CONFLICT
PACKAGE_MISMATCH
TECHNICAL_FAILURE_BEFORE_ACTIVATION_COMMIT
EXECUTION_UNCERTAIN
SUCCESS
```

`EXECUTION_UNCERTAIN` MUST be reconciled under MS-PROT-069. Transport failure MUST NOT be treated as proof that activation did not commit.

---

## 18. Post-commit event

After successful activation Main Street MAY emit `ConfigurationRevisionActivated`.

The event means that the identified Configuration Revision has already become authoritative for applicable new activity.

The event SHOULD identify:

```text
merchantIdentifier
activatedConfigurationRevisionIdentifier
replacedConfigurationRevisionIdentifier if one existed
activation/provenance reference sufficient for consumers
```

The event MUST be post-commit. Consumer failure MUST NOT roll back activation.

Projection refresh, storefront recomposition, dashboard recomposition, notifications and analytics MAY react after commit but MUST NOT share Merchant Configuration ownership.

---

## 19. Explicit non-responsibilities

This amendment does NOT define:

- initial 30-day trial start;
- subscription entitlement;
- controller verification policy;
- provider health/readiness;
- runtime Operational Eligibility;
- storefront Exposure policy;
- capability-specific business operation semantics;
- merchant suspension, closure or ownership transfer.

Configuration activation is defined here so later authorities MAY reference it precisely. This amendment itself MUST NOT assign commercial meaning to activation.

---

## 20. Falsification evidence

The model was tested against:

- two concurrent first-activation candidates;
- two later candidates sharing the same base revision;
- approval followed by candidate mutation;
- semantic-registry release change before activation;
- successful package production without merchant approval;
- package production followed by activation conflict;
- committed activation with lost HTTP acknowledgement;
- replay of an old successful activation after a newer revision becomes active;
- stale storefront projection after activation;
- existing commitments retaining historical revision affinity;
- compiler failure before package completion;
- uncertain activation commit outcome.

No tested case requires the Resolved Configuration Package to become merchant authority or requires compilation to execute inside the activation transaction.

---

## 21. Trade-off decision

Rejected:

```text
mutable active configuration object
```

because mutation destroys approval affinity, deterministic provenance and historical reproducibility.

Rejected:

```text
Resolved Configuration Package as merchant authority
```

because compiler output would become a competing authority.

Accepted:

```text
immutable authoritative Configuration Revision
+
immutable derived Resolved Configuration Package
+
atomic activation selecting the authoritative revision
```

The accepted consequence is explicit revision/package provenance and retention/reconstruction requirements for historical affinity.

---

## 22. Scope of amendment

This amendment is scope-specific.

MS-PROT-040 v1.0 remains authoritative for all lifecycle, impact-analysis, existing-commitment, capability-deactivation, merchant-review, reinstatement and concurrency rules not explicitly refined here.

---

## 23. Acceptance statement

Main Street now has an implementation-deterministic boundary between merchant configuration authority, deterministic compiled representation and activation authority.

> **Approve one immutable revision, derive one exact package from pinned semantics, and atomically select the revision that governs new activity; never let compiler output become a second merchant authority.**
