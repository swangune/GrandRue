# IMP-06 S3 Design-Gate Resolution & Corpus Conformance — 3 September 2026

**Status:** ACCEPTED-DESIGN FORMALISATION / CORPUS-CONFORMANCE EVIDENCE  
**Gate:** `IMP-06-S3-DG-001`  
**Resolving authority:** `MS-PROT-049 v1.4 — Capability-Owned Public Interaction Participation Source & Generic Binding Projection Amendment`  
**Manual approval:** 3 September 2026  
**Prior verified governance head:** `development@f8e924efedbd73b5115508007795efee91bc8b98`  
**Latest verified code-bearing baseline:** `development@fdd859a2a68e62407bff8cc6fe50f52ff4c54b45`  
**Code-bearing verification:** run `33805294267` / job `100814217917` — SUCCESS

This record is governance/conformance evidence only. It does not itself create semantic authority or claim S3 production implementation.

---

## 1. Trigger

After T4b closed, the IMP-06 macro applicability review found S3 to be the remaining unresolved generic Surface foundation while T2d/T3c were already classified ON_DEMAND and T4c remained conditional on a concrete query adapter.

The existing `MS-WATCH-002` question was therefore traversed.

---

## 2. Hard-cycle proof

The accepted corpus establishes all edges required to prove Criterion A:

```text
MS-PROT-049 v1.2
    Public Interaction Binding requires independently authoritative
    subject-interaction participation

MS-PROT-046 v1.2
    first concrete production participation definition:
    Opportunity → enquiry / send-enquiry

MS-IMP-001
    IMP-07 is first Publication → Enquiry vertical slice
    IMP-06 HARD → IMP-07
```

If generic S3 completion requires that concrete positive production source before IMP-06 can complete:

```text
IMP-06
    waits for Opportunity → Enquiry source
        ↓
source belongs to IMP-07
        ↓
IMP-07 waits HARD for IMP-06
        ↓
cycle
```

This satisfies `MS-WATCH-002` Criterion A — actual hard dependency cycle.

Repository review found no already-conforming production participation source that could legitimately satisfy the prerequisite without pulling a later capability implementation forward.

---

## 3. Smallest-repair falsification

### Alternative: infer participation from Surface/Exposure

**REJECTED.** Violates MS-PROT-049 single-owner participation and would convert observation/composition into semantic authority.

### Alternative: implement Opportunity → Enquiry inside IMP-06

**REJECTED.** Pulls Publication/Enquiry business semantics into generic Surface infrastructure solely to satisfy sequencing.

### Alternative: change `MS-IMP-001` so IMP-07 may begin before IMP-06

**REJECTED.** Larger programme change is unnecessary because the generic S3 boundary can be implemented independently and fail closed.

### Alternative: remove S3 from IMP-06 completion without implementing a generic source/projector boundary

**REJECTED.** Would discard an accepted generic Surface responsibility rather than satisfy it.

### Alternative: universal interaction rules engine

**REJECTED.** Introduces unnecessary semantic gravity and shared interpretation authority.

### Accepted smallest repair

```text
capability-owned participation facts
        ↓
owner-qualified participation-source contract
        ↓
generic binding projector
```

with:

```text
zero registered production sources
        ↓
valid fail-closed state
        ↓
zero positive bindings
```

Concrete positive production sources remain with their owning capability vertical slices.

---

## 4. Approval trace

The complete proposed `MS-PROT-049 v1.4` authority was presented in ChatGPT after review, falsification and recommendation.

The user explicitly approved the complete proposal on 3 September 2026.

Repository formalisation therefore became permitted under `DESIGN-RULES.md`.

The repository copy changes only lifecycle metadata from proposed/pending to accepted/manual-approval wording; the approved normative meaning is preserved.

---

## 5. DESIGN-CORPUS-CONFORMANCE review

### Metadata and lifecycle

**PASS**

The accepted amendment carries:

- stable Document ID `MS-PROT-049`;
- Version `1.4`;
- accepted/manual-approval status;
- explicit amendment scope;
- dependencies;
- governance references;
- purpose; and
- approval trace.

### Amendment target exists

**PASS**

The accepted authority explicitly amends surviving MS-PROT-049 v1.0-v1.3 within the production Public Interaction participation-source and binding-projection scope.

### Dependency existence / authority compatibility

**PASS**

Dependencies on composite MS-PROT-027, MS-PROT-043 v1.4, MS-PROT-046 v1.2, MS-PROT-049 v1.0-v1.3, MS-PROT-059 and MS-PROT-062 are already accepted authorities.

No dependency direction transfers Publication, Enquiry, Booking, Appointment or Ordering authority into generic Surface.

### Authority Index

**PASS**

`designs/AUTHORITY-INDEX.md` is updated from v3.56 to v3.57 and records MS-PROT-049 through v1.4 plus the `IMP-06-S3-DG-001` resolution consequence.

### Deferred Decision Register

**PASS subject to this formalisation commit**

The canonical `designs/DEFERRED-DECISION-REGISTER.md` is updated in place to v4.37. It records `IMP-06-S3-DG-001` as RESOLVED by MS-PROT-049 v1.4 and updates the current promoted-work/navigation state without creating a companion register.

### Watch list

**PASS**

`MS-WATCH-002` remains active. The watch is not retired merely because the current deadlock is resolved. Its current evidence is updated to record the proved Criterion-A cycle and the v1.4 mitigation.

### Canonical Semantic Lexicon impact

**REVIEWED — NO UPDATE REQUIRED**

The accepted meaning of `Public Interaction Binding` is unchanged. MS-PROT-049 v1.4 defines the new participation-source/definition terms sufficiently within the owning authority and no conflicting canonical term was found that requires a Lexicon disambiguation to implement S3 correctly.

If later cross-domain use makes either term ambiguous, the Lexicon can be updated through ordinary governance without changing v1.4 semantics.

### Implementation Rules impact

**REVIEWED — NO RULE AMENDMENT REQUIRED**

Existing `IMPLEMENTATION-RULES.md` already governs:

- selection of the smallest READY node;
- tests-first execution;
- minimum conforming implementation;
- adversarial review/falsification;
- design escalation for material gaps;
- evidence/graph/status closure; and
- branch creation only with explicit active-chat permission.

MS-PROT-049 v1.4 changes S3 dependency eligibility, not the implementation process.

### MS-IMP-001 impact

**REVIEWED — NO AMENDMENT REQUIRED**

The accepted v1.4 authority explicitly preserves:

```text
IMP-06 HARD → IMP-07
```

The hidden cycle is removed by making generic S3 structurally completeable with a fail-closed zero-production-source registry, not by weakening the macro edge.

### Duplicate/current authority check

**PASS**

Before formalisation, the current Authority Index listed MS-PROT-049 only through v1.3 and repository search found no accepted MS-PROT-049 v1.4 authority.

### Structural/normative review

**PASS**

The authority retains:

- one semantic owner for each positive participation fact;
- no Surface/Exposure inference;
- exact subject/scope/release/context affinity requirements;
- many-to-many participation;
- participation/availability/execution separation;
- fail-closed zero-source behavior; and
- later concrete-source ownership.

No universal interaction rules engine, business-type branching, generic payload bag, repository discovery authority or execution permission is introduced.

---

## 6. Resulting graph consequence

```text
IMP-06-S3-DG-001
    RESOLVED by MS-PROT-049 v1.4
        ↓
S3 generic participation-source/binding projection
    READY after this formalisation head verifies
```

The refreshed graph is recorded in:

`docs/development/imp-06-graph-refresh-2026-09-03-s3-design-resolution.md`

---

## 7. Explicit nonclaims

This design-gate closure does **not** claim:

- S3 production code exists;
- any concrete production participation source is registered;
- `Opportunity → enquiry/send-enquiry` is implemented;
- Publication or Enquiry production implementation has begun;
- IMP-06 is complete;
- IMP-07 is READY;
- T4c is READY;
- a concrete query adapter exists; or
- a test fixture is production semantic authority.

---

## 8. Implementation handoff

Once the formalisation commit verifies green, S3 may enter the ordinary implementation loop.

The first RED must target the accepted generic contract rather than a concrete Publication/Enquiry source.

The implementation must preserve the central v1.4 distinction:

```text
generic infrastructure can be complete with zero production sources
        ≠
positive production participation exists
```

`MS-WATCH-002` remains active throughout S3 and later capability-source implementation.
