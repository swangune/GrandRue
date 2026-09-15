# Main Street Document Governance

**Document ID:** MS-DOCUMENT-GOVERNANCE-001  
**Version:** 2.3  
**Status:** Accepted  
**Applies from:** 5 September 2026  
**Purpose:** Define how Main Street fundamental product-purpose, design, architecture and implementation authorities are created, navigated, changed, validated and kept unambiguous while maintaining one canonical current governance/rule document per responsibility.

---

## 1. Governing Principle

> **Current governance must be readable from one canonical document per responsibility; Git history preserves governance revision provenance, while semantic design amendment chains remain separate where their scope-aware provenance forms part of current semantic authority.**

Governance makes accepted decisions discoverable and enforceable. It does not become a substitute for the fundamental product-purpose authority, semantic authority or architectural authority that owns a subject.

---

## 2. Authority Hierarchy

Main Street uses the following authority direction:

```text
MS-FUNDAMENTAL-VISION-001
Fundamental Vision / Mission / Product Constitution
        ↓
Product philosophy / accepted product requirements and intent
        ↓
DESIGN-RULES.md
        ↓
Accepted MS-PROT semantic/design authority
        ↓
Accepted TAS / ADR implementation architecture
        ↓
IMPLEMENTATION-RULES.md conformance gates
        ↓
Tests / production code / implementation evidence
```

`MS-FUNDAMENTAL-VISION-001` governs why Main Street exists and the non-negotiable product-purpose constraints within its scope. It does not create capability-specific business semantics.

`DESIGN-RULES.md` governs how material design is reasoned, checked against the Fundamental Vision, specified, falsified and made implementation-ready; it does not replace the substantive semantic authority that owns a business subject.

`IMPLEMENTATION-RULES.md` governs how accepted design is implemented; it is not a semantic/domain authority.

A lower layer MUST NOT silently redefine an accepted higher-layer decision within that decision's scope.

Where accepted authorities appear to conflict, implementation MUST pause for the affected behaviour and the ambiguity MUST enter the governed design-decision lifecycle.

---

## 3. Canonical Current Governance Set

The live governance/navigation/rule responsibilities are owned by exactly these canonical files:

```text
designs/DESIGN-RULES.md
    → mandatory Fundamental Vision Conformance, design reasoning, writing,
      ambiguity elimination, review, falsification and implementation-readiness rules

designs/DOCUMENT-GOVERNANCE.md
    → how authority and governance work

designs/AUTHORITY-INDEX.md
    → which accepted fundamental product-purpose, semantic/design and downstream authorities currently govern

designs/CANONICAL-SEMANTIC-LEXICON.md
    → canonical qualification of high-risk shared terminology

designs/DEFERRED-DECISION-REGISTER.md
    → unresolved, deferred and deliberately promoted design work

designs/DESIGN-CORPUS-CONFORMANCE.md
    → mechanically checkable structural governance rules

designs/IMPLEMENTATION-RULES.md
    → mandatory implementation process and architectural conformance
```

`docs/foundation/Fundamental-Vision-Mission-and-Product-Constitution.md` is the accepted upstream substantive product-purpose authority, not a competing governance/rule responsibility.

No companion `CURRENT`, `vX amendment`, `closure amendment`, or similarly competing live governance/rule document SHALL be created for the responsibilities listed above.

---

## 4. Single-Current-Document Rule

For governance, navigation and operational rule authorities:

```text
approved change
    ↓
merge into canonical file
    ↓
increment integrated version
    ↓
commit
    ↓
Git history preserves prior revision
```

Future changes MUST update the canonical file in place rather than create another live amendment file.

This rule applies to the current governance/rule set listed in Section 3 and to any future governance/rule authority explicitly designated as a single-current-document authority.

### 4.1 Semantic-design exception

This rule does **not** require flattening MS-PROT semantic/design amendment chains where an amendment:

- supersedes only part of an earlier authority;
- preserves earlier rules outside its scope;
- adds narrower semantic meaning;
- records migration/compatibility consequences; or
- otherwise requires scope-aware provenance to determine current meaning.

For those authorities, the accepted semantic graph remains navigated through `AUTHORITY-INDEX.md`.

Git history alone MUST NOT replace semantic amendment provenance where that provenance affects current meaning.

---

## 5. Document Lifecycle

Material design documents use:

```text
RESEARCH / PROTOTYPE
        ↓
PROPOSED
        ↓ review + falsification
ACCEPTED
        ↓ explicit replacement where applicable
SUPERSEDED
```

Research/prototype preserves exploration/evidence and is not implementation authority. Proposed work is not implementation authority. Accepted work governs within scope. Superseded work remains traceability evidence but no longer governs superseded scope.

Material semantic or product-purpose change requires the accepted lifecycle; discussion or implementation alone does not confer authority.

---

## 6. Current Semantic-Series Navigation

`DOCUMENT-GOVERNANCE.md` MUST NOT contain a manually maintained current MS-PROT numeric range or per-authority catalogue.

Current accepted semantic authorities, versions, amendments, supersession relationships and amendment scopes are determined through:

```text
designs/AUTHORITY-INDEX.md
```

Acceptance of later MS-PROT identifiers therefore does not require a Document Governance revision merely to extend a number range.

---

## 7. Scope-Aware Semantic Amendment Graph

Main Street MUST NOT assume that the numerically highest semantic document version automatically replaces every earlier statement.

An accepted semantic amendment may fully supersede, narrowly replace, add a more-specific rule, or preserve unrelated earlier rules.

Current semantic meaning is the surviving accepted base/amendment graph recorded by `AUTHORITY-INDEX.md`.

---

## 8. Material Design Decision Lifecycle

Every material semantic or architectural decision follows:

```text
PROPOSE
   ↓
FUNDAMENTAL VISION CONFORMANCE
   ↓
REVIEW
   ↓
FALSIFICATION
   ↓
RECOMMENDATION
   ↓
SEEK MANUAL APPROVAL
   ↓
FORMALISE ACCEPTED AUTHORITY
   ↓
UPDATE AUTHORITY INDEX
   ↓
UPDATE DDR / LEXICON where applicable
   ↓
REVIEW IMPLEMENTATION-RULES impact
   ↓
RUN CORPUS CONFORMANCE
   ↓
COMMIT
```

The detailed Fundamental Vision Conformance, reasoning, ambiguity and implementation-readiness gates are governed by `DESIGN-RULES.md`.

No material decision becomes accepted merely because it was discussed, coded or written. Approval authorises formalisation of the approved recommendation only.

---

## 9. Governance Completion Invariant

A material accepted design change is governance-complete only when all applicable items are satisfied:

```text
accepted authority committed
+
Fundamental Vision Conformance satisfied through DESIGN-RULES.md
+
DESIGN-RULES.md satisfied
+
AUTHORITY-INDEX.md current
+
DEFERRED-DECISION-REGISTER.md current when decision status changes
+
CANONICAL-SEMANTIC-LEXICON.md current when terminology authority changes
+
IMPLEMENTATION-RULES.md impact reviewed
+
DESIGN-CORPUS-CONFORMANCE.md passes
```

A navigation defect does not retroactively erase an accepted semantic decision, but the defect MUST be repaired before further dependent formalisation where practical.

---

## 10. Authority Index Role

`AUTHORITY-INDEX.md` is normative for current authority navigation, including the accepted fundamental product-purpose authority, lifecycle status of accepted semantic/design authorities, amendment/supersession relationships, scope-aware composite authority identification, dependency navigation and deferred-decision closure links.

```text
AUTHORITY INDEX
    → WHICH authority governs

ACCEPTED AUTHORITY
    → WHAT the substantive meaning is
```

If an index summary conflicts with substantive accepted authority text, the substantive authority governs its scope and the index defect MUST be repaired.

---

## 11. Design Rules Role

`DESIGN-RULES.md` governs:

- mandatory Fundamental Vision Conformance before ordinary material-design review;
- mandatory proposal/review/falsification/approval sequence;
- normative design writing;
- ambiguity elimination;
- semantic ownership specification;
- operation/lifecycle/contract precision;
- trade-off and falsification recording;
- implementation-readiness gates; and
- escalation when a material design rule is missing or ambiguous.

It MUST NOT itself become the semantic owner of Product, Order, Booking, Inventory, Customer, Payment or another business subject.

---

## 12. Canonical Semantic Lexicon Role

`CANONICAL-SEMANTIC-LEXICON.md` governs qualification of overloaded terms, semantic category/name distinctions, prohibited ambiguous shorthand and navigation to accepted semantic owners.

It MUST NOT independently redefine capability invariants, lifecycle, mutation authority, policy, configuration or runtime behaviour.

> **Vocabulary qualification resolves language ambiguity; it does not collapse bounded semantics.**

---

## 13. Deferred Decision Register Role

`DEFERRED-DECISION-REGISTER.md` is the one current work-queue/navigation authority for unresolved questions, deliberately deferred questions, resolved identifiers/resolving authorities, retained future scope and newly discovered gaps deliberately promoted for review.

Promotion means only that a question requires governed review. It does not pre-accept an answer or document number.

Historical DDR state remains recoverable from Git history and accepted resolving authorities.

---

## 14. Design Corpus Conformance Role

`DESIGN-CORPUS-CONFORMANCE.md` defines mechanically checkable structural rules.

Conformance MAY establish missing canonical files, competing live governance/rule files, duplicate accepted identifiers/versions, invalid dependencies, accepted authorities absent from the Authority Index, stale DDR state, invalid lifecycle metadata, high-confidence terminology defects and mechanically detectable Fundamental Vision navigation/conformance defects.

Automation MUST NOT claim to prove arbitrary semantic or product-purpose coherence. Human Fundamental Vision review, design review and falsification remain authoritative for substantive meaning.

---

## 15. Implementation Governance Role

`IMPLEMENTATION-RULES.md` governs how accepted architecture is translated into tests and production code.

The accepted composite architecture remains binding at implementation time, including responsibility-appropriate use of capability boundaries, modular-monolith deployment, declarative semantic compilation, transactional consistency, selective post-commit events, lifecycle modelling, ports/adapters and explicit application orchestration.

Governance MUST NOT allow framework convention, generated code, AI suggestion or local convenience to silently replace accepted architectural responsibility or the upstream Fundamental Vision.

Implementation likewise MUST NOT mechanically force a pattern where it is unsuitable; the implementation rules govern escalation to a better approach.

---

## 16. Implementation Ambiguity Stop Rule

Implementation MUST stop at the affected boundary when accepted semantic authority is clear but the proposed implementation would materially alter or violate architectural responsibility or accepted upstream product-purpose authority.

Examples include bypassing capability ownership, replacing atomicity with eventual reaction, direct cross-capability persistence mutation, infrastructure becoming semantic owner, framework annotations becoming the sole invariant definition, business-type branching replacing semantic composition, universal engines collapsing bounded responsibilities, mechanically forcing an unsuitable pattern, or transferring avoidable software-administration burden to target merchants contrary to accepted design.

---

## 17. Historical Preservation

Historical product, research, prototype, ADR, MS-PROT and implementation evidence SHOULD remain available where they preserve material reasoning or traceability.

The single-current-document rule changes how governance/rule revisions are maintained; it does not authorise mass deletion of historical semantic evidence.

For canonical governance/rule files, Git history is the required revision provenance. Separate obsolete governance/rule amendment files SHOULD be removed once their accepted content is integrated.

Historical product-purpose evidence does not override later accepted `MS-FUNDAMENTAL-VISION-001` within overlapping scope.

---

## 18. Filename and Identity

For semantic/design documents, filenames are navigation aids; authority follows stable identifier, version/status, accepted amendment/supersession graph and Authority Index.

For single-current governance/rule authorities, the canonical filename in Section 3 is part of repository navigation and MUST remain stable unless a separately approved migration changes it.

The accepted Fundamental Vision is identified by `MS-FUNDAMENTAL-VISION-001`; its canonical repository location is indexed by `AUTHORITY-INDEX.md`.

---

## 19. Reference Rule

Normative dependencies MUST resolve to existing accepted authority or explicitly incorporated decisions.

Governance/rule documents MUST reference canonical filenames from Section 3 rather than obsolete versioned companion files.

Semantic/design documents SHOULD reference stable identifiers/titles; filenames remain secondary.

Where product-purpose conformity is material, downstream authorities SHOULD reference `MS-FUNDAMENTAL-VISION-001` rather than relying on vague references to vision, strategy or product philosophy.

### 19.1 `Governed by` Metadata

`Governed by` and `Depends on` have different meanings.

```text
Governed by
    → governance/review authority applicable to the document revision

Depends on
    → substantive authority dependency required to determine the governed design
```

`Governed by` MUST NOT by itself transfer semantic ownership or create a substantive semantic dependency.

For a new or materially amended authority, `Governed by` references MUST identify accepted authority that is valid for the stated governance/review role at the time of approval.

An already accepted semantic/design authority MAY retain the exact `Governed by` metadata that formed part of its approved revision.

A later revision of `DESIGN-RULES.md`, `IMPLEMENTATION-RULES.md`, `DOCUMENT-GOVERNANCE.md` or another governance authority does not require mass rewriting of earlier accepted semantic authorities merely to modernise their historical approval-context metadata.

Current governance for new work is determined through the current canonical governance set and Authority Index.

Historical `Governed by` metadata MUST NOT be interpreted as freezing the repository permanently to the governance revision that existed when the semantic authority was approved.

### 19.2 `MS-AVS-001` Legacy Resolution

The recovered:

```text
MS-AVS-001
Main Street Architecture & Domain Validation Standard
Version 1.0
Status: Proposed
```

never became an accepted repository authority.

Accordingly, legacy references to `MS-AVS-001` in already accepted semantic/design authorities record historical review context only.

They:

```text
do not create an independent current semantic dependency;
do not create current governance authority;
do not invalidate an otherwise explicitly accepted semantic authority;
and do not require mass rewriting of the accepted semantic corpus.
```

No current governance/rule document and no new or materially amended semantic/design authority MAY introduce or reassert `MS-AVS-001` as current normative authority.

Current design-validation method is governed by `DESIGN-RULES.md`.

Capability-specific semantic validation remains governed by the applicable accepted semantic authorities.

Implementation-level vertical, integration and cross-merchant proof remains governed by applicable accepted implementation/programme authority.

Only rules explicitly incorporated into current accepted authority have current normative force.

No repository copy of the historical Proposed `MS-AVS-001` is created by this resolution.

---

## 20. Ambiguity Handling

When ambiguity is discovered:

```text
DISCOVER
   ↓
TRACE CURRENT AUTHORITIES
   ↓
CLASSIFY
   ↓
GRAPH / LOOP FALSIFICATION
   ↓
RECOMMENDATION
   ↓
MANUAL APPROVAL
   ↓
NARROW REPAIR
```

Detailed ambiguity classes, Fundamental Vision Conformance and readiness checks are governed by `DESIGN-RULES.md`.

No ambiguity audit may silently reinterpret accepted semantics or weaken the Fundamental Vision.

---

## 21. Hard Governance Invariants

1. Each current governance/rule responsibility MUST have one canonical live file.
2. Future governance/rule revisions MUST be merged into that canonical file rather than emitted as competing amendment files.
3. Git history MUST preserve governance/rule revision provenance.
4. Semantic MS-PROT amendment chains MUST remain scope-aware where provenance affects current meaning.
5. `AUTHORITY-INDEX.md` alone owns current accepted authority navigation.
6. `DEFERRED-DECISION-REGISTER.md` alone owns the current deferred/promoted work queue.
7. `DESIGN-RULES.md` governs design method/precision and Fundamental Vision Conformance but MUST NOT become business-semantic ownership authority.
8. `CANONICAL-SEMANTIC-LEXICON.md` MUST NOT become semantic ownership authority.
9. `IMPLEMENTATION-RULES.md` MUST NOT redefine accepted business semantics or the Fundamental Vision.
10. Structural conformance MUST NOT claim to prove semantic or product-purpose correctness.
11. A lower authority layer MUST NOT silently redefine a higher accepted authority.
12. Material semantic/architectural changes require Fundamental Vision Conformance, review, falsification, recommendation and manual approval.
13. Governance consolidation MUST NOT erase necessary semantic traceability.
14. `MS-FUNDAMENTAL-VISION-001` MUST remain upstream of downstream product, design, architecture, UX and implementation decisions within its scope unless it is itself explicitly reconsidered and approved through the governed lifecycle.

---

## 22. Acceptance Statement

Main Street governance uses `MS-FUNDAMENTAL-VISION-001` as the upstream product-purpose authority, a single-current-document model for design rules, document governance, navigation, terminology, deferred decisions, corpus conformance and implementation rules, and scope-aware semantic amendment chains where those chains are necessary to determine meaning.

> **One fundamental product purpose; one live governance document per responsibility; one navigable semantic authority graph; Git history for governance revisions; explicit semantic provenance where meaning depends on it.**
