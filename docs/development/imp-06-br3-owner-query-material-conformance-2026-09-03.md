# IMP-06 BR3 Owner/Query Material Acquisition Conformance Evidence

**Date:** 3 September 2026  
**Programme:** IMP-06 — Read, Exposure & Transport Spine  
**Node:** BR3 — owner/query material acquisition + source-affinity proof  
**Authority:** MS-PROT-027 v1.13, especially §§3–4, 10–18  
**Record type:** implementation evidence only  
**Status:** **CONFORMING_COMPLETE**  
**Verified code-bearing baseline:** `development@8955ea907766069fe4e65120b63ff1d687ce8752`  
**Full verification:** GitHub Actions run `33703576374`, job `100487849019` — SUCCESS (`mvn --batch-mode clean verify -Ppostgres-it`; 887 unit tests + 307 PostgreSQL integration tests = 1,194 tests; 0 failures, 0 errors)

## 1. Implemented BR3 boundary

The first concrete owner/query acquisition seam is Profile-owned:

- `MerchantPublicDescriptorProjectionReadPort`
- `AuthorityBackedMerchantPublicDescriptorProjectionReadPort`
- `MerchantPublicDescriptorProjectionObservation`
- `MerchantPublicDescriptorProjectionMaterial`
- `MerchantPublicDescriptorProjectionFragment`

The adapter obtains one current authoritative `MerchantPublicDescriptorRevision` from `MerchantPublicDescriptorAuthority` and derives typed Profile projection material plus the corresponding `ProjectionSourceEvidence` from that same coherent owner observation.

## 2. Accepted invariants established

The implementation establishes the BR3 obligations already accepted by MS-PROT-027 v1.13:

1. **Capability ownership remains intact.** Merchant Public Descriptor facts and construction of the typed Profile observation remain in `mainstreet.merchantprofile`; generic Surface/Projection infrastructure does not become the owner of Profile business values.
2. **One coherent owner observation establishes material and evidence.** The authority is read once for a successful observation. The typed material progress identifier and P2 evidence progress identifier are derived from the same exact descriptor revision.
3. **Exact source dependency is retained.** Every Profile descriptor fragment carries the Profile-owned `merchant-public-descriptor` source dependency and exact observed progress affinity.
4. **Progress meaning remains owner-defined.** Generic infrastructure receives opaque progress identifiers and does not interpret revision ordering or Profile business semantics.
5. **Missing owner material fails closed as evidence.** Absence yields no material and explicit unavailable/missing P2 source evidence rather than fabricated values or generic repair.
6. **Merchant-scope mismatch is structural failure.** Authority material for another merchant cannot be accepted for the requested scope.
7. **Equal business values do not collapse revision affinity.** Equal Profile values at different owner revisions produce distinct progress affinity.
8. **No generic business payload envelope was introduced.** The Profile representation remains typed and owner/query-specific.
9. **No BR4/BR5/BR6 behaviour was smuggled into BR3.** The read port does not register Material Affinity Observation Contributions, compare current owner progress in E4, select E4-positive fragments, assemble S2 Surface output, or implement T1b4 transport.

## 3. Falsification and architecture evidence

The BR3 tests cover:

- exact typed material and source-affinity establishment;
- a changing authority proving that material and P2 evidence come from a single owner read rather than two time-separated reads;
- equal-value/new-revision affinity separation;
- missing-source fail-closed behaviour;
- cross-merchant authority mismatch rejection;
- rejection of public/external construction paths that could forge the Profile-owned observation; and
- module dependency conformance preventing `surface` from depending on `merchantprofile`.

This closes the principal laundering counterexamples:

```text
same values at R7 and R8
    ≠ same material progress

merchant A request + merchant B owner material
    → structural failure

generic Surface code
    ✕ cannot become Profile observation owner
```

## 4. Scope review

No new semantic concept, ownership rule, consistency rule, API contract or exposure policy was required to complete BR3. The implementation is a direct realisation of accepted MS-PROT-027 v1.13 and therefore remains within MS-IMPLEMENTATION-RULES-001 automatic-change authority.

## 5. Dependency consequence

BR3 is **CONFORMING_COMPLETE**.

The direct successor may therefore be promoted:

```text
BR3 owner/query material acquisition + source affinity
    CONFORMING_COMPLETE
    ↓
BR4 Material Affinity Observation Contribution registration/runtime binding
    READY
```

BR5, BR6, S2 and T1b4 remain blocked by their existing dependency chain.
