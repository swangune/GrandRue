# IMP-06 BR5 Owner-Progress Affinity Conformance Record

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Node:** `BR5 — owner evaluator current-progress comparison`  
> **Result:** **CONFORMING_COMPLETE**

This record closes BR5 under accepted `MS-PROT-027 v1.13` §§28–30 and the existing E4 owner-evaluator boundaries. It is implementation evidence only and does not create semantic authority.

---

## 1. Governing invariant

BR5 must prevent a current owner Exposure choice from governing bounded material produced under a different owner-progress revision.

Canonical invariant:

```text
A current R9 choice cannot authorise bounded R8 material.
```

Required decision sequence:

```text
bounded owner-qualified expected progress
        ↓ compare
coherent current owner progress
        ↓ exact match only
current owner Exposure choice
        ↓
EXPOSE / WITHHOLD
```

A progress mismatch, unavailable current authority, missing/mismatched affinity or cross-capability affinity must fail closed to `UNRESOLVED` rather than permitting a newer current choice to govern stale bounded material.

---

## 2. Tests-only RED evidence

Tests-only RED commit:

- `091929cafd1762964348626379540ce7764d6210` — `test(imp-06): add BR5 profile owner-progress RED`

RED trigger commit:

- `4fe89271fe660b7a239cd0e043a507b86fcd0750` — `test(imp-06): trigger BR5 profile owner-progress RED`

GitHub Actions run:

- run `33715993663` — **FAILED as intended**

The intended failure was semantic, not incidental: the two stale-affinity scenarios expected `UNRESOLVED`, while the pre-BR5 evaluator returned `EXPOSE`. This demonstrated that the existing implementation allowed a current Exposure choice to govern bounded material without first proving exact owner-progress affinity.

---

## 3. Production implementation and hardening

BR5 was implemented and hardened through the following code-bearing sequence:

- `4631d698170542764821bbed5a5659643997db28` — `feat(imp-06): implement BR5 profile owner-progress affinity`
- `770d19a2a63cfbb2a4a3631bbb5163b54332cf80` — `test(imp-06): harden BR5 evaluator mismatch isolation`
- `1fd4dfd2e591ff3acc2cddc8ab247b8cbe0ad67e` — `refactor(imp-06): move BR5 current progress onto read ports`
- `04119ded1fb43b40fd33beb049381baa325dab2b` — `refactor(imp-06): expose BR5 current progress as value`
- `a6b1d17b288af70356018251746338049767ecc3` — `test(imp-06): extract BR5 typed affinity policy`
- `a09c5ae633236860e46125fc3dea28b7569f3ca6` — `test(imp-06): align BR5 public scenario names`
- `b5708a9cdc66900e925346425869ca2e8a7893fe` — `test(imp-06): name BR5 unresolved fallback scenario`
- `b4ddc4e250ea98d7012555e15e0f4a25d9a8f6a6` — `test(imp-06): correct BR5 reverse-affinity fixture`

The final BR5 semantic/code-bearing baseline is:

`development@b4ddc4e250ea98d7012555e15e0f4a25d9a8f6a6`

---

## 4. Verified behaviour

The BR5 implementation proves the required owner-progress gate:

1. bounded R8 + coherent current R8 + current PUBLIC choice → `EXPOSE`;
2. bounded R8 + coherent current R8 + current PRIVATE_INTERNAL choice → `WITHHOLD`;
3. bounded R8 + current R9 mismatch → `UNRESOLVED`;
4. unavailable/unresolvable current owner authority → `UNRESOLVED`;
5. missing or mismatched owner-qualified material-affinity evidence fails closed;
6. Contact candidates cannot consume Location affinity and Location candidates cannot consume Contact affinity; and
7. Merchant Location compares current active Location source progress before its separate current Location Exposure Choice may govern.

Architecture remains capability-owned:

```text
Generic E4
    | owner-filtered contribution evidence
    v
Profile evaluator
    | Profile revision/progress semantics
    v
Profile authority
```

Generic E4 remains revision-agnostic and does not learn Profile revision ordering, rendered business values or current-choice semantics.

---

## 5. GREEN verification

Final GitHub Actions verification:

- workflow: `Maven Tests`
- run `33722015716` — **SUCCESS**
- job/check `100542935385` — **SUCCESS**
- command: `mvn --batch-mode clean verify -Ppostgres-it`
- result: full PostgreSQL verification profile green

No test-count total is restated here because BR5 closure relies on the observed final workflow result rather than reusing the historical BR4 count.

---

## 6. Dependency consequence

BR5 is therefore **CONFORMING_COMPLETE**.

Its completion satisfies BR6's BR5 dependency and promotes:

```text
BR6 — same-read E4-positive fragment selection
    → READY — CURRENT SMALLEST EXECUTABLE NODE
```

This does not complete IMP-06. S2 remains blocked on BR6; T1b4 remains blocked on BR6 + S2; S3 remains independently blocked on authoritative capability-owned participation sources and remains covered by `MS-WATCH-002`.
