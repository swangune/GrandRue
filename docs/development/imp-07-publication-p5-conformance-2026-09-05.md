# IMP-07 Publication P5 Conformance Evidence — Request-Scoped Public Opportunity Representation

**Date:** 5 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Implementation node:** IMP-07-P5 — Request-scoped public Opportunity representation  
**Branch:** `development`  
**Status:** **CONFORMING_COMPLETE effective when the cycle-closing head containing this evidence, the P5 closure graph, implementation-status.md and the programme-gate update passes full verification**

This record is implementation/conformance evidence only. It creates no semantic, architectural or product authority.

---

## 1. Authority and boundary

P5 implements the accepted public Opportunity representation boundary from:

- composite `MS-PROT-046 v1.2 + v1.3` — bounded PUBLIC representation, Category-B synchronous request-scoped reads, exact published-revision material and the separation of visibility from actionability;
- composite `MS-PROT-027 v1.13 + v1.14` — bounded-read acquisition, same-observation material affinity and fail-closed currentness mechanics; and
- `MS-PROT-049 v1.4` as a negative boundary — Exposure does not establish Public Interaction participation and Publication → Enquiry participation remains a later owner-qualified node.

P5 is limited to request-scoped PUBLIC representation. It does not own Opportunity actionability, Public Interaction participation, Enquiry persistence, concrete transport, caching or indexing.

---

## 2. Historical RED → GREEN trace

```text
RED
    development@3f7958fca49874ffd606f6f360d049e053d31420
    GitHub Actions Maven Tests run 33958778665
    FAILURE — expected historical RED

initial GREEN
    development@bbfe026e535cfd468816c79b7f2ee1b8b7cf6ef4
    GitHub Actions Maven Tests run 33958986905
    FAILURE — implementation-cycle verification defect

repaired GREEN
    development@3d2775ec2add3aadbdba4b2736cf716f581bad46
    GitHub Actions Maven Tests run 33959091152
    SUCCESS

post-GREEN adversarial hardening
    development@2fcc452f0b8df452a2eba42cc10824d24ff0039e
    GitHub Actions Maven Tests run 33959274014
    SUCCESS
```

The initial GREEN failure was repaired inside the implementation cycle. The repair corrected a proof-fixture accessor mismatch and removed an unintended extra restriction on optional public text; it did not require new design authority.

---

## 3. Conformance assertions

Executable proof establishes:

```text
exact published revision rather than the unpublished current revision supplies public material                 PASS
stable owner-qualified Opportunity instance remains the Exposure candidate identity                            PASS
revision affinity remains owner material affinity rather than PUBLIC identity                                  PASS
bounded PUBLIC value excludes revision identifiers                                                             PASS
bounded PUBLIC value excludes internal provenance and administration metadata                                  PASS
Publication material affinity is request-bound and Merchant-Scope-bound                                         PASS
material-affinity contribution implementation remains capability-private                                        PASS
published-binding change during bounded acquisition produces no public material fragment                        PASS
published-binding change / republish after acquisition but before E4 resolves WITHHOLD fail closed             PASS
request-binding rebinding is rejected                                                                           PASS
Merchant-Scope rebinding is rejected                                                                             PASS
generic Projection / Exposure infrastructure does not reacquire Publication business values after selection    PASS
P6 Opportunity actionability remains separate                                                                    PASS
Participation / Enquiry / transport are not introduced                                                          PASS
cache / index / Redis / static publication read model is not introduced                                         PASS
```

The representation carries the accepted audience-safe Opportunity material required by the initial PUBLIC representation while deliberately excluding Publication/revision mechanics from the public value.

---

## 4. Race and currentness falsification

The critical P5 race is:

```text
acquire published revision A
        ↓
Publication changes to revision B
        ↓
E4 Exposure evaluates
```

P5 does not emit A using a decision about B and does not substitute B for the already-acquired A. The bounded read carries Publication-owned material affinity for A, and the Publication-owned Exposure evaluator requires that exact affinity still to match current authoritative publication evidence. If it does not, Exposure fails closed.

The complementary acquisition race is also covered: if the published binding changes while coherent material acquisition is in progress, no fragment is established for the request.

---

## 5. Architectural non-absorption

The implementation does not collapse these distinct concepts:

```text
eligible for PUBLIC Exposure
    != request-scoped public representation
    != Opportunity actionability
    != Opportunity → Enquiry participation
    != interaction execution authority
```

No design amendment is required by the P5 implementation or adversarial inspection.

---

## 6. Closure result

The P5 code-bearing GREEN head and post-GREEN adversarial head have both passed the repository full `Maven Tests` gate, including the PostgreSQL integration profile. The cycle-closing synchronization commit still must prove that this evidence, the P5 closure graph, `implementation-status.md` and the executable programme-gate assertion agree.

Therefore:

```text
P5 = CONFORMING_COMPLETE
    effective when the cycle-closing synchronization head passes full verification

I1 = READY
    on that same successful synchronization CI
```

Do not begin I1 before that synchronization gate succeeds. P6 Opportunity actionability remains separate.