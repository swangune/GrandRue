# IMP-06 BR6 Same-Read Fragment Selection Conformance Record

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Node:** `BR6 — same-read E4-positive fragment selection`  
> **Result:** **CONFORMING_COMPLETE**

This record closes BR6 under accepted `MS-PROT-027 v1.13` §§33–37 and the established BR1–BR5/P2/E4 boundaries. It is implementation evidence only and does not create semantic authority.

---

## 1. Governing invariant

BR6 must bind E4-positive membership back to projection material retained by the same immutable bounded read without reopening data acquisition, P2 evaluation or Exposure evaluation.

Canonical chain:

```text
BoundedProjectionRead B1
        ↓ exact binding
ProjectionServiceabilityResult P2(B1)
        ↓ eligible material only
ApiExposureResolution E4(B1 request / merchant / release)
        ↓ exact positive membership correspondence
ProjectionMaterialFragment selection from B1
```

Required properties:

- P2 evidence belongs to the exact bounded read being represented;
- E4 resolution preserves the same Observation Request, Merchant Scope, Semantic Registry Release and established invocation affinity;
- every positive E4 membership corresponds to exactly one P2-eligible fragment retained by that same bounded read;
- selected fragments preserve bounded-read order rather than E4-result order;
- zero, duplicate, omitted, forged or cross-read correspondence invalidates the entire representation attempt; and
- selection does not rerun P2/E4, read repositories, acquire new business values, infer capability participation or perform Surface/transport rendering.

---

## 2. Tests-only RED evidence

Tests-only RED commit:

- `2c0df6fdebf42a2a50c4696303151aa476e7fae9` — `test(imp-06): add BR6 same-read fragment-selection RED`

GitHub Actions:

- run `33771293389` — **FAILED as intended**

The RED was specific to missing BR6 production behavior: production sources compiled, while test compilation failed on the absent `BoundedProjectionFragmentSelector` / structural-failure seam. This established that the accepted same-read selection behavior did not already exist.

---

## 3. Production implementation

Minimum production implementation:

- `1de11fe5805a0727f19a32c30af4ae19c79cd52b` — BR6 Surface-local selector implementation

Initial full GREEN verification:

- GitHub Actions run `33771455217` — **SUCCESS**

The implementation is contained in:

`src/main/java/mainstreet/surface/BoundedProjectionFragmentSelector.java`

It consumes only already-materialised immutable Surface/P2/E4 results and introduces no repository, E4-evaluator, capability-authority or transport dependency.

---

## 4. Adversarial falsification

Falsification commit:

- `c1d3cb85072a33d185884e467148b4420d6089bb` — `test(imp-06): falsify BR6 same-read selection boundary`

The hardened suite attacks structural false positives including:

1. P2 result from another bounded read;
2. E4 resolution from another Observation Request;
3. positive membership with no same-read fragment;
4. positive membership whose source is P2-omitted;
5. forged/mismatched P2 provenance despite an otherwise plausible binding;
6. `NOT_SERVICEABLE` P2 input;
7. `NOT_APPLICABLE` or source-progress-incoherent material;
8. forged/mismatched E4 Semantic Registry Release provenance;
9. dependency creep beyond the Surface-local selection boundary; and
10. E4 membership order differing from bounded-read order.

The positive controls prove that valid same-read/P2-eligible/E4-positive material is returned immutably in bounded-read order. Empty positive membership yields an empty immutable selection rather than widening membership.

---

## 5. Scope / architecture review

Comparison:

`3f91f7540a89d5c9f0933b1dfe63ec042cd5e19d..c1d3cb85072a33d185884e467148b4420d6089bb`

The complete BR6 delta is exactly three files:

- `src/main/java/mainstreet/surface/BoundedProjectionFragmentSelector.java` — added;
- `src/test/java/mainstreet/surface/BoundedProjectionFragmentSelectorTest.java` — added;
- `src/test/java/mainstreet/surface/BoundedProjectionFragmentSelectorFalsificationTest.java` — added.

No E4 evaluator/resolver, repository, S2 assembly, T1b4 transport, Profile authority or capability-owned source was modified.

Therefore BR6 preserves the accepted separation:

```text
business/source acquisition
        ↓ already bounded
P2 serviceability
        ↓ already resolved
E4 membership
        ↓ already resolved
BR6 exact same-read selection
        ↓
S2 representation assembly (later node)
```

---

## 6. Final GREEN verification

Final hardened code-bearing baseline:

`development@c1d3cb85072a33d185884e467148b4420d6089bb`

GitHub Actions:

- workflow: `Maven Tests`
- run `33771843413` — **SUCCESS**
- job/check `100703770328` — **SUCCESS**
- command: `mvn --batch-mode clean verify -Ppostgres-it`
- result: `BUILD SUCCESS`

Observed test totals:

- unit/conformance (Surefire): **914** tests, 0 failures, 0 errors, 0 skipped;
- PostgreSQL integration (Failsafe): **307** tests, 0 failures, 0 errors, 0 skipped;
- complete gate: **1,221 PASS**, 0 failures, 0 errors, 0 skipped.

BR6-specific hardened classes include:

- `BoundedProjectionFragmentSelectorTest` — 6 PASS;
- `BoundedProjectionFragmentSelectorFalsificationTest` — 6 PASS.

---

## 7. Dependency consequence

BR6 is **CONFORMING_COMPLETE**.

Its completion satisfies S2's BR6 dependency and promotes:

```text
S2 — final PUBLIC/CUSTOMER Surface assembly
    → READY — CURRENT SMALLEST EXECUTABLE NODE
```

T1b4 remains blocked until S2 conforms. S3 remains independently `BLOCKED_DEPENDENCY` on authoritative capability-owned subject-interaction participation sources and remains monitored by `MS-WATCH-002`.

IMP-06 therefore remains **PARTIALLY_CONFORMING**.