# MS-PROT-050 v1.4 — Stable Business Hours Mutation, Revision & Currentness Amendment

**Document ID:** MS-PROT-050
**Version:** 1.4
**Status:** **ACCEPTED after implementation-discovered governance review, falsification and manual approval**
**Approved:** 30 August 2026
**Amends:** composite MS-PROT-050 through v1.3 within stable weekly Public Business Hours mutation, revision, currentness, concurrency, idempotency and actor-authority scope
**Closes:** `IMP-05-B2-DG-001`
**Depends on:** MS-PROT-031; MS-PROT-050 v1.2; MS-PROT-050 v1.3; MS-PROT-051 v1.1; MS-PROT-059; MS-PROT-063; MS-PROT-069; MS-PROT-071; MS-PROT-076
**Purpose:** Establish the production authority for changing stable weekly Public Business Hours without transferring ownership to Profile, Scheduling, projections or transport, losing historical meaning, permitting stale writes, or multiplying one logical mutation under retry.

---

## 1. Governing decision

Stable weekly Public Business Hours SHALL be retained as immutable exact
revisions under a separate Business Hours authority.

For every exact Business Hours Scope:

```text
append-only Standard Business Hours revisions
        +
one durable current pointer
        ↓
current authoritative stable weekly hours or intentional withdrawal
```

The current pointer is currentness evidence. It does not erase or mutate the
revision history it selects.

---

## 2. Ownership remains separate

The Business Hours authority owns:

```text
stable weekly Public Business Hours mutation
exact immutable revision history
scope-local currentness
ordinary mutation concurrency and retry identity
```

It does not own:

```text
Merchant Profile or Merchant Location
Scheduling configuration
effective-dated Business Operating Overrides
CurrentBusinessOperatingStatus
Exposure or projections
storefront publication
Merchant Configuration Revision
```

`MS-PROT-051 v1.1` remains authoritative for Profile/Location and explicitly
does not absorb Public Business Hours. Shared revision techniques do not merge
semantic ownership.

---

## 3. Standard Business Hours Revision

One immutable revision conceptually retains:

```text
StandardBusinessHoursRevision
{
    revisionIdentity
    businessHoursScope
    scopeLocalRevisionNumber
    predecessorRevisionIdentity?
    disposition
    standardBusinessHours?       // required only when CONFIGURED
    actorIdentity
    logicalRequestIdentity
    committedAt
}
```

Initial dispositions are:

```text
CONFIGURED
WITHDRAWN
```

Revision identity is durable and globally unambiguous. Revision number is
strictly monotonic within one exact Business Hours Scope and begins at 1.

---

## 4. Configured closed week is not withdrawal

A configured weekly value may legitimately contain no open intervals:

```text
CONFIGURED
weekly intervals = []
```

That means authoritative Public Business Hours exist and the ordinary weekly
pattern is closed throughout the week.

It is distinct from:

```text
WITHDRAWN
standard value = absent
```

which means the merchant intentionally has no current authoritative stable
weekly Public Business Hours for that scope. Consumers must not collapse these
states.

---

## 5. One current pointer per exact scope

One durable pointer exists for each exact Business Hours Scope that has ever
received a committed revision:

```text
CurrentStandardBusinessHours
{
    businessHoursScope
    revisionIdentity
    scopeLocalRevisionNumber
}
```

The pointer advances atomically with the appended revision. It is never
advanced to an uncommitted or differently scoped revision.

Merchant and Merchant Location scopes remain independent. No pointer fallback,
inheritance or precedence is introduced.

---

## 6. Configure or replace operation

The owner operation conceptually accepts:

```text
ConfigureStandardBusinessHours
{
    authenticatedActor
    businessHoursScope
    expectedCurrentRevisionIdentity?
    logicalRequestIdentity
    standardBusinessHours
}
```

For first configuration:

```text
current pointer absent
expected current revision absent
        ↓
append CONFIGURED revision 1
```

For replacement:

```text
current pointer = Rn
expected current revision = Rn
        ↓
append CONFIGURED revision n+1
advance pointer
```

Supplying an expected revision where no current pointer exists, omitting it
where one exists, or supplying a stale/different revision fails closed.

---

## 7. Withdrawal operation

Withdrawal conceptually accepts:

```text
WithdrawStandardBusinessHours
{
    authenticatedActor
    businessHoursScope
    expectedCurrentRevisionIdentity
    logicalRequestIdentity
}
```

It appends a `WITHDRAWN` revision and advances the scope's current pointer.

It does not delete history, remove the Business Hours Scope, retire a Merchant
Location or create an empty configured week.

Repeated withdrawal after a different committed request is a new mutation and
must satisfy the current expected-revision contract.

---

## 8. Actor authority

The initial ordinary mutation surface requires an authenticated current
Merchant Controller for the target Merchant Scope at commit.

```text
authenticated actor
        +
current Controller authority
        +
merchant account permits ordinary mutation
        ↓
Business Hours mutation may commit
```

A caller-supplied `isController` boolean, remembered earlier authorization or
merchant identifier alone is not authority.

Controller transfer before commit invalidates the former Controller's
authority. Delegated staff mutation is unavailable until an accepted exact
registered privilege contract governs it.

---

## 9. Same-scope optimistic concurrency

The current pointer is the optimistic concurrency boundary for one exact
Business Hours Scope.

Two commands expecting the same current revision may not both append distinct
successor revisions.

Unrelated scopes do not share a universal Business Hours version:

```text
MERCHANT(M1) edit
        ≠ artificial conflict with
MERCHANT_LOCATION(M1, L1) edit
```

The persistence implementation must serialize pointer creation and advancement
correctly even when no current row exists yet.

---

## 10. Logical retry identity

Every externally retryable mutation carries one logical request identity
affined to the Merchant Scope and Business Hours Scope.

Exact replay after commit returns the original committed revision, even if the
scope's current pointer has since advanced.

Reuse of the same logical request identity with materially different intent
fails with an idempotency conflict.

Material intent includes at least:

```text
operation kind
Business Hours Scope
expected revision
disposition
configured time zone and normalized weekly intervals where applicable
```

Technical transport attempt identity remains separate.

---

## 11. Atomic transaction boundary

One successful mutation atomically commits:

```text
authority revalidation
expected-current proof
immutable revision
normalized configured intervals where applicable
logical retry evidence
current pointer advancement
```

No event, projection refresh or storefront update is required inside this
transaction merely to make the Business Hours fact authoritative.

Failure before commit produces no partial revision or pointer change. An
uncertain acknowledgement is recovered using the same logical request identity.

---

## 12. Retained provenance

Every revision retains the exact authenticated actor identity, logical request
identity and committed time.

The stable value retains:

```text
Business Hours Scope
scope-qualified IANA time zone
normalized weekly operating intervals
```

Provenance is historical evidence. It is not continuing actor authority.

---

## 13. Merchant Location dependency

A `MERCHANT_LOCATION` Business Hours Scope must reference a current
authoritative Merchant Location in the same Merchant Scope at commit.

An arbitrary string that merely resembles a location identity is not enough.

Therefore production location-scoped mutation remains blocked until the
Merchant Location authority exists. This dependency does not block the
merchant-scoped revision authority.

Location retirement and historical retention remain governed by
`MS-PROT-051`; a Business Hours implementation must not invent a competing
location lifecycle.

---

## 14. Dated overrides remain separate

This amendment does not merge stable weekly revisions with effective-dated
Business Operating Overrides.

```text
stable weekly revision currentness
        ≠
effective-dated override authority
```

A temporary closure or special opening does not require a stable weekly
revision. A stable weekly change does not erase dated override history.

---

## 15. Projection and configuration consequences

A stable-hours commit does not itself:

- change Exposure;
- publish a storefront;
- mutate a read projection in the authority transaction;
- mutate Scheduling configuration;
- create a Merchant Configuration Revision;
- compile an RCP; or
- rewrite existing commitments.

Downstream consumers use the current authoritative revision through their
governed projection or resolution boundary.

---

## 16. Distinguishable outcomes

The application boundary must preserve distinguishable outcomes where recovery
differs, including:

```text
SUCCESS
ALREADY_APPLIED
STANDARD_BUSINESS_HOURS_NOT_CONFIGURED
STANDARD_BUSINESS_HOURS_REVISION_CONFLICT
BUSINESS_HOURS_IDEMPOTENCY_CONFLICT
MERCHANT_LOCATION_NOT_FOUND
MERCHANT_LOCATION_RETIRED
AUTHENTICATION_REQUIRED
CURRENT_CONTROLLER_REQUIRED
MERCHANT_ACCOUNT_OPERATION_RESTRICTED
VALIDATION_REJECTION
TECHNICAL_FAILURE_BEFORE_COMMIT
EXECUTION_UNCERTAIN
```

Exact transport mappings remain downstream.

---

## 17. Persistence consequence

The accepted relational architecture should retain normalized immutable
revision/value rows and one scope-keyed current pointer.

Exact PostgreSQL table/index names are implementation details provided the
implementation proves:

- immutable history;
- unique scope-local revision numbers;
- one current pointer per exact scope;
- exact retry reconstruction;
- same-scope concurrency safety; and
- scope and location referential integrity where applicable.

---

## 18. Falsification record

### First configuration race

Two current Controllers concurrently submit different first schedules for the
same scope. At most one commits as revision 1; the other receives a revision
conflict. **PASS**

### Lost acknowledgement

A replace commits and the response is lost. Exact retry returns the original
revision even after a later distinct replacement. **PASS**

### Changed retry intent

The same request identity is reused with a different time zone or interval.
The command conflicts rather than returning or creating a misleading result.
**PASS**

### Closed week versus absence

A configured empty interval set remains authoritative closed-week evidence.
Withdrawal produces absence. Consumers can distinguish them. **PASS**

### Controller transfer race

The former Controller begins an edit and transfer commits first. The edit
cannot commit using stale authority. **PASS**

### Independent scopes

Merchant-scope and location-scope edits do not conflict through one global
version and do not inherit from one another. **PASS**

### Invented location

A location-scoped command supplies an identifier with no current authoritative
Merchant Location. The mutation fails. **PASS**

### Temporary closure

A holiday closure does not append a stable weekly revision merely to express
an effective-dated exception. **PASS**

---

## 19. Rejected alternatives

- mutable current rows with no exact historical revisions;
- one merchant-global Business Hours version across all scopes;
- delete-as-withdrawal;
- treating an empty weekly interval set as absence;
- blind last-write-wins;
- retry deduplication by value alone;
- caller booleans as Controller authority;
- delegated mutation without an exact accepted privilege;
- accepting arbitrary location identifiers before Location authority exists;
- merging dated overrides into stable revision lifecycle;
- synchronously publishing storefront state inside the owner transaction; and
- turning Business Hours changes into Merchant Configuration activation.

---

## 20. Hard invariants

1. Stable weekly Public Business Hours remain a separate authority.
2. Every accepted mutation appends one immutable exact scope-affined revision.
3. One current pointer per exact Business Hours Scope determines currentness.
4. `CONFIGURED` and `WITHDRAWN` are distinct.
5. A configured empty week is not withdrawal.
6. Replacement and withdrawal require the exact current revision.
7. Same-scope stale writes fail closed.
8. Unrelated scopes do not share a universal version.
9. Exact logical retry returns the original committed revision.
10. Changed intent under the same request identity conflicts.
11. Current authenticated Controller authority is revalidated at commit.
12. Historical actor identity is not continuing authority.
13. Merchant Location scope requires same-merchant current Location authority.
14. Stable weekly revisions remain separate from dated overrides.
15. Mutation does not imply Exposure, projection publication, Scheduling change,
    Configuration Revision or activation.

---

## 21. Implementation graph consequence

The approved fine-grained split is:

```text
B2a — Merchant-scope stable weekly-hours revision authority
      READY

B2b — Merchant-location stable weekly-hours revision integration
      BLOCKED_DEPENDENCY on A4 Merchant Location authority
```

B2a may implement only the minimum contract above. It must not weaken or mock
the B2b location-existence requirement.

---

## 22. Acceptance statement

> **Stable weekly Public Business Hours change through immutable exact
> scope-affined revisions, one durable current pointer, current-Controller
> authority, optimistic concurrency and exact logical retry identity.
> Withdrawal preserves history and remains distinct from an authoritative
> closed week. Merchant-location mutation waits for real Merchant Location
> authority, while dated overrides, Scheduling, Exposure and projections remain
> separate owners.**

**Review:** PASS
**Falsification:** PASS
**Ambiguity review:** PASS
**Manual approval:** GRANTED — 30 August 2026
**Governance verdict:** **ACCEPTED**
