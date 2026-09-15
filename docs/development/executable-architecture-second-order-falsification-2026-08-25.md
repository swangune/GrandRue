# Main Street Executable Architecture Second-Order Falsification — 25 August 2026

**Status:** SECOND-ORDER FALSIFICATION COMPLETE — FINAL REVISED RECOMMENDATIONS AWAIT MANUAL APPROVAL — **NOT IMPLEMENTATION AUTHORITY**  
**Authority class:** Design-review / falsification evidence  
**Governed by:** `designs/DESIGN-RULES.md`, `designs/DOCUMENT-GOVERNANCE.md`  
**Builds on:** `docs/development/executable-architecture-recommendation-falsification-2026-08-25.md`  
**Purpose:** Attack the first revised EAC-001/EAC-002/EAC-003 recommendations for residual overreach or ambiguity before manual approval.

This document does not accept or formalise any design. It narrows the candidate recommendations where the first falsification still left an over-broad or engineer-interpretable clause.

---

# 1. Why a second-order falsification was necessary

The first dedicated falsification correctly revised all three original recommendations. A further adversarial pass found that three phrases could still allow materially different interpretations:

```text
EAC-001
"support activity needs the release"
    could be read as requiring complete release material for every residual/historical path

EAC-002
"deployment admission prevents unsupported contexts"
    could be read as requiring one homogeneous deployment pool to support every historical context

EAC-003
"material projection"
    could allow each engineer to decide ad hoc whether a freshness/serviceability contract exists
```

`DESIGN-RULES.md` does not permit those phrases to become normative implementation authority without tighter predicates.

---

# 2. EAC-001 second-order falsification

## 2.1 Counterexample — historical execution without full release resolution

Assume an outstanding Booking B100 has:

```text
exact retained RCP(C18,R8)
+
accepted compatible executable support
+
all authoritative operational state required by the residual operation
```

The execution path does not need to reconstruct `SemanticReleaseAssembly(R8)` from the published source definition set.

Therefore this must remain invalid:

```text
any use of R8 semantics
    => full R8 release-definition material must be loaded
```

The release definition set is required only for a path that actually needs to resolve/reconstruct/validate registered source definitions from that release, or where accepted recovery/interpretation authority explicitly requires the source definition evidence.

## 2.2 Counterexample — active runtime may operate entirely from retained RCP

An ordinary restart may recover:

```text
active Configuration identity
+
exact immutable retained RCP
```

and bind new activity to that RCP without recompiling configuration merely because the process restarted.

EAC-001 therefore must not turn application startup into compulsory recompilation of every active merchant.

## 2.3 Final EAC-001 candidate rule

> **Every published Semantic Registry Release has one coherent immutable published definition set/evidence from which its registered static definitions can be reproduced or verified when a compiler/runtime/recovery path requires source-release resolution. Exact retained RCPs and other accepted immutable execution evidence may satisfy paths that do not require reconstruction of the full source release. A process never fabricates an old release by relabelling current definitions.**

Required consequences:

1. Publication is coherent: a partially published constituent set is not a valid published release.
2. Publication evidence provides sufficient integrity/provenance to reject substitution/corruption.
3. The definition set contains registered static definitions only, not Merchant Configuration, credentials, Provider Readiness or mutable operational state.
4. Source-release resolution is exact by release identity; `latest` substitution is prohibited.
5. Resolution may be eager or lazy.
6. Ordinary restart does not require recompilation of every active merchant where an exact retained RCP already supplies the accepted runtime package.
7. Retention of complete source definitions is required only for support paths/recovery obligations that actually need them; MS-PROT-054 support dimensions remain distinct.
8. Physical storage/distribution remains implementation-deferred.

**Second-order verdict: SURVIVES.**

---

# 3. EAC-002 second-order falsification

## 3.1 Counterexample — global deployment blocking is unnecessarily broad

Assume:

```text
10,000 merchants use R9
1 merchant has one residual R8 obligation
new instance supports R9 but not the exceptional R8 path
```

A universal rule that the entire deployment or every process must fail readiness because one residual execution context is unsupported would sacrifice unrelated safe availability and conflict with MS-PROT-070's scoped degradation principle.

The semantic invariant is not:

```text
every instance supports every historical semantic context
```

The semantic invariant is:

```text
an invocation may execute only on a path compatible with its exact semantic contract
```

and the operational invariant is:

```text
Main Street must not knowingly remove the last safe execution path
for a still-required active/residual contract without an accepted
migration/restriction/remediation outcome.
```

## 3.2 Define execution-support orphaning

For this design review, a required execution contract is **orphaned** when all are true:

```text
1. accepted current authority still requires an operation to remain executable
   for new activity or an existing commitment;
2. the exact semantic/RCP context is known;
3. no currently available/recoverable executable implementation path can
   conform to that required contract; and
4. no accepted safety restriction, migration, compensation/remediation or
   manual-intervention rule intentionally supersedes execution.
```

A deployment plan must not knowingly create this state.

If an unexpected support gap nevertheless occurs, the affected operation fails closed / becomes explicitly unavailable or intervention-required; unrelated operations remain eligible according to their own authorities.

## 3.3 Counterexample — semantic-aware routing is not required initially

A valid initial implementation may use:

```text
one homogeneous application pool that supports all contexts the pool receives
```

A future implementation may use:

```text
compatibility-aware routing to a narrower historical execution adapter/pool
```

Neither routing topology is part of the semantic contract. The hard requirement is that an unsupported process must not execute the invocation and that required support is not silently orphaned.

## 3.4 Final EAC-002 candidate rule

> **Every configuration-dependent invocation binds its exact semantic/RCP context to an executable implementation path proven to support the affected semantic execution contract and required participant/effect contracts. An unsupported process must not execute or reinterpret the invocation. Main Street must not knowingly orphan a still-required execution contract during deployment; unexpected support gaps degrade only the affected scope unless a higher accepted authority requires broader restriction.**

Required consequences:

1. Support is reference-/contract-scoped, not a release-global boolean.
2. One implementation may serve multiple semantic contexts where exact bound data plus conformance evidence establishes the required behaviour.
3. Cross-capability participants/effects are included in support validation.
4. Class, bean, package and deployment names do not prove support.
5. Durable work recovers the semantic/configuration affinity required by its owning operation before support resolution.
6. Missing/unproven support fails closed for the affected execution path.
7. Deployment may remain partially available if unaffected scopes are safe; whole-platform readiness failure is not a universal consequence.
8. A deployment must not knowingly remove the last safe execution path for a contract still required by accepted active/residual authority unless an accepted migration/restriction/remediation path has taken effect.
9. Historical support never overrides current platform/security/legal/integrity restrictions.
10. Homogeneous pools, compatibility-aware routing, historical adapters or exceptional old binaries are implementation strategies rather than semantic identity.

**Second-order verdict: SURVIVES AFTER NARROWING.**

---

# 4. EAC-003 second-order falsification

## 4.1 Counterexample — "material projection" is not a determinable predicate

Two engineers could reasonably disagree whether a simple synchronous Booking summary is "material" and therefore whether it requires a Projection Contract.

A formal recommendation needs explicit applicability triggers.

## 4.2 Projection-contract applicability triggers

A dedicated freshness/serviceability Projection Contract is required when at least one of the following is true:

```text
A. the read representation is persisted/cached beyond the authoritative query transaction/request;

B. the read representation is updated asynchronously from authoritative state;

C. another registered contract (for example a Surface Contribution) references
   the projection/read identity or depends on its availability;

D. the system may serve the representation when one or more authoritative
   source dependencies are unavailable;

E. the system may intentionally serve data known not to reflect the latest
   authoritative observation;

F. the representation combines multiple authoritative sources whose freshness
   or serviceability can diverge materially;

G. privacy/security/Exposure revocation timing can make continued serving of
   an older representation unsafe.
```

A synchronous request-scoped projection computed through accepted query/application boundaries directly from currently established authoritative sources may use its ordinary query contract without acquiring a separate cache/staleness contract when none of A–G applies.

This does not permit transport/UI code to query capability tables directly.

## 4.3 Counterexample — freshness and serviceability must not collapse

A projection may be:

```text
fresh source version known
but presentation withheld because Exposure revoked
```

or:

```text
stale within permitted informational tolerance
and still serviceable
```

Therefore a future formalisation should distinguish the evidence used to establish **projection freshness/currentness** from the final **may this projection be served to this audience/context now?** decision. It need not freeze a universal enum to do so.

## 4.4 Final EAC-003 candidate rule

> **Where any explicit projection-contract applicability trigger applies, one declared projection owner defines the evidence required to determine currentness/staleness, the conditions under which the projection may still be served, the bounded consequence of missing evidence, and any rebuild/provenance guarantee. Source capabilities retain business-fact authority; Exposure/security/privacy and mandatory Surface inclusion remain independent higher-order constraints.**

Required consequences:

1. No global TTL defines projection correctness.
2. Age is only one possible contract input where sufficient.
3. Composite projections preserve independently material source/subprojection evidence.
4. A mandatory residual workspace is not erased solely because its projection is unavailable.
5. Privacy/security/Exposure revocation can withhold data regardless of ordinary staleness tolerance.
6. Projection storage/materialisation technology remains replaceable.
7. Synchronous request-scoped projections are valid and need not be forced into a materialised-projection framework.
8. Commands revalidate authoritative state and command responses do not become general projection authority.
9. Rebuildability is asserted only where retained authoritative/provenance evidence is sufficient.
10. No universal projection availability enum or DSL is implied.

**Second-order verdict: SURVIVES AFTER APPLICABILITY PRECISION.**

---

# 5. Authority-layer recheck

Second-order falsification does not change the recommended authority layers:

```text
EAC-001
    implementation-architecture ADR/TAS beneath MS-PROT-054 + ADR-010

EAC-002
    runtime/deployment implementation-architecture ADR beneath
    MS-PROT-054 + MS-PROT-040 + MS-PROT-023

EAC-003
    narrow MS-PROT-027 amendment composed with MS-PROT-049 + MS-PROT-070
```

Reason:

- EAC-001 selects how accepted immutable release semantics become exactly materialisable without changing semantic identity.
- EAC-002 selects how executable implementation conformance is resolved without redefining the semantic contract.
- EAC-003 introduces normative read/projection serviceability behaviour visible across API/Surface/Exposure, so it belongs at the projection semantic/design layer.

---

# 6. Final recommendation after two falsification passes

The final candidate recommendations have now survived:

```text
restart
retained-RCP execution
release reconstruction
partial/corrupt publication
release withdrawal from new use
rolling deployment
partial deployment capability
cross-capability execution
background work after deployment
provider degradation
security-driven historical restriction
projection lag
mixed-freshness dashboard
privacy/Exposure revocation
residual workspace projection failure
synchronous projection
command/projection convergence delay
publisher / consultant / motel / retailer / hybrid domains
```

The falsification result remains:

```text
EAC-001  ACCEPT revised direction for manual-approval review
EAC-002  ACCEPT revised direction for manual-approval review
EAC-003  ACCEPT revised direction for manual-approval review
```

None is accepted authority yet.

> **Manual approval, if given, should apply to the final narrowed recommendations in this document together with the first falsification evidence—not to the broader initial completeness-review wording.**
