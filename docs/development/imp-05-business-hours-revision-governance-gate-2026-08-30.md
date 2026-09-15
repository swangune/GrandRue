# IMP-05 — Business Hours Revision Governance Gate

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Affected node:** B2 — Durable Business Hours revision authority
**Gate identity:** `IMP-05-B2-DG-001`
**Date:** 30 August 2026
**Status:** **RESOLVED by accepted MS-PROT-050 v1.4**

## 1. Accepted authority that is sufficient

Composite `MS-PROT-050` through v1.3 already governs:

- Public Business Hours as an independently authoritative merchant-owned fact;
- the explicit `MERCHANT | MERCHANT_LOCATION` Business Hours Scope algebra;
- scope-affined IANA time zones;
- stable weekly intervals, including cross-midnight meaning;
- no implicit scope inheritance or precedence; and
- a separate effective-dated override lifecycle.

The existing B1 value model and tests conform to those rules.

## 2. Missing authority

The accepted corpus does not state the production mutation contract for stable
weekly Business Hours. In particular, it does not decide:

- whether history is immutable revisions or mutable rows;
- the durable currentness representation per Business Hours Scope;
- create/update/withdraw expected-revision semantics;
- lost-acknowledgement retry identity and changed-intent behavior;
- the actor authorised to commit ordinary changes; or
- the exact retained actor, request and commit provenance.

These are substantive authority, concurrency and idempotency decisions rather
than table-layout choices. `MS-PROT-051 v1.1` cannot silently supply them: it
explicitly excludes Public Business Hours from Profile/Presence ownership and
retains Business Hours as a separate authority.

No B2 production code was started before approval.

## 3. Recommended governing contract

The approved `MS-PROT-050 v1.4 — Stable Business Hours Mutation, Revision &
Currentness Amendment` formalises this minimum contract:

1. The Business Hours authority owns stable weekly-hours mutation separately
   from Profile, Scheduling, dated overrides, projections and Exposure.
2. Every accepted configure, replace or withdraw command appends one immutable
   revision for exactly one Business Hours Scope.
3. One durable current pointer per exact scope identifies the effective latest
   revision; a `WITHDRAWN` revision represents intentional absence without
   erasing history.
4. Initial configuration requires no prior revision. Replace and withdraw
   require the exact expected current revision; stale expectation fails closed.
5. Every externally retryable command carries one merchant/scope-affined
   logical request identity. Exact replay returns the original committed
   revision; reuse with materially different intent conflicts.
6. The initial mutation surface requires an authenticated current Merchant
   Controller at commit. Delegated staff mutation remains unavailable until an
   exact registered privilege contract is accepted.
7. Each revision retains scope, monotonic scope-local revision number,
   predecessor identity, disposition, normalized weekly value when configured,
   actor identity, logical request identity and committed time.
8. Merchant-location scope additionally requires a current authoritative
   Merchant Location in the same Merchant Scope. It cannot be implemented
   before A4 supplies that authority.
9. A stable-hours commit does not mutate Scheduling, dated overrides,
   Configuration Revision, Exposure, projection state or storefront state.

## 4. Fine-grained graph consequence

On approval, refine B2 without changing macro meaning:

```text
B2a — Merchant-scope stable weekly-hours revision authority
      READY after MS-PROT-050 v1.4 acceptance

B2b — Merchant-location stable weekly-hours revision integration
      BLOCKED_DEPENDENCY on A4 Merchant Location authority
```

B2a is now the smallest coherent READY implementation node because it can prove the
revision, currentness, concurrency, idempotency and withdrawal contract without
inventing or bypassing Merchant Location existence.
