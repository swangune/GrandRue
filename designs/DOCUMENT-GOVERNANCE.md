# Main Street Document Governance

**Document ID:** MS-DOCUMENT-GOVERNANCE-001  
**Version:** 2.4  
**Status:** Accepted  
**Applies from:** 16 September 2026  
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

---

## 23. Canonical Design-Corpus Layout

GrandRue SHALL organise current design material by governance responsibility and stable authority identity.

The canonical shape SHALL be:

```text
designs/
│
├── DESIGN-RULES.md
├── DOCUMENT-GOVERNANCE.md
├── AUTHORITY-INDEX.md
├── DEFERRED-DECISION-REGISTER.md
├── CANONICAL-SEMANTIC-LEXICON.md
├── DESIGN-CORPUS-CONFORMANCE.md
├── IMPLEMENTATION-RULES.md
│
├── authorities/
│   ├── ms-prot/
│   │   ├── MS-PROT-020/
│   │   ├── MS-PROT-021/
│   │   ├── ...
│   │   └── MS-PROT-094/
│   │
│   └── programme/
│       └── MS-IMP-001/
│
└── historical/
    └── <classified non-current design evidence>
```

The seven single-current governance files remain at `designs/` root.

This avoids unnecessary path churn for the most frequently referenced governance authorities.

---

## 24. Stable Authority Directory Rule

Every accepted MS-PROT authority chain SHALL have exactly one canonical directory derived from its stable Document ID.

Example:

```text
designs/authorities/ms-prot/MS-PROT-044/
```

shall contain the accepted files constituting the MS-PROT-044 chain, for example:

```text
MS-PROT-044 — Offering, Listing & Published Subject Model.md
MS-PROT-044 v1.1 — Product, Offering & Variant Boundary Amendment.md
MS-PROT-044 v1.2 — Merchant Offering & Product Definition Access Contract Amendment.md
```

All versions of one stable authority are therefore physically colocated.

A later v1.3 belongs in the same directory.

---

## 25. Directory Does Not Determine Authority

Physical location improves discovery.

It does not replace the authority graph.

Canonical authority remains determined by:

```text
stable Document ID
+
Version
+
lifecycle status
+
accepted amendment/supersession scope
+
AUTHORITY-INDEX navigation
```

Moving a byte-identical authority into its canonical directory is not a semantic amendment.

---

## 26. No Business-Domain Authority Folders

Primary authority storage MUST NOT be organised by mutable or overlapping business labels such as:

```text
commerce/
workforce/
marketing/
website/
finance/
customer/
```

as the authoritative home.

Reason: one authority may legitimately coordinate several domains, and its scope may evolve.

Domain classifications MAY exist as derived navigation views.

They MUST NOT become competing authority ownership.

Stable Document ID is the authoritative physical grouping key.

---

## 27. No Per-Authority Current README Requirement

The corpus SHALL NOT require a separate:

```text
README
CURRENT
LATEST
manifest
summary
```

file inside every authority directory.

Such files would duplicate `AUTHORITY-INDEX.md` and create another stale-navigation surface.

The directory itself groups provenance.

The Authority Index identifies current composition.

---

## 28. Canonical Navigation Remains Singular

`AUTHORITY-INDEX.md` remains the one current accepted-authority navigation source.

`DEFERRED-DECISION-REGISTER.md` remains the one current deferred/work-queue source.

They MUST NOT be split into independently current domain registers merely to reduce file size.

For example, the following are prohibited:

```text
COMMERCIAL-AUTHORITY-INDEX.md
WORKFORCE-AUTHORITY-INDEX.md
MARKETING-DDR.md
BOOKING-DDR.md
```

as independent current governance sources.

This prevents cross-register drift.

---

## 29. Navigation Compression Rule

Canonical navigation documents SHOULD contain only the information necessary to identify current authority and current decision state.

They SHOULD NOT become duplicate semantic specifications.

`AUTHORITY-INDEX.md` SHOULD favour:

```text
stable authority
current accepted composition
material scope-aware cross-authority link
supersession/closure information where necessary
```

over long restatements already owned by the source authority.

`DEFERRED-DECISION-REGISTER.md` SHOULD favour:

```text
stable decision identity
current status
resolving authority/current owner
revisit condition
```

over long historical narrative where Git history and accepted source authorities already preserve that provenance.

Removing redundant navigation prose MUST NOT remove substantive authority or an unresolved decision.

---

## 30. Historical Evidence Classification

Historical, prototype, review and non-authoritative design material MUST NOT be moved into:

```text
designs/authorities/
```

merely because its filename resembles an MS-PROT authority.

Before migration it must be classified as:

```text
accepted authority constituent
or
historical/non-authoritative evidence
```

Only the first class belongs in the accepted authority store.

This is especially important for the earlier MS-PROT-001..019 stratum and other prototype/review documents.

---

## 31. Mechanical Move Rule

A corpus reorganisation MAY move accepted authority files without a new semantic lifecycle when all of the following are true:

```text
file bytes unchanged
Document ID unchanged
Version unchanged
Status unchanged
authority composition unchanged
supersession/amendment scope unchanged
```

The migration MUST update repository-local path references where required.

Git history remains migration provenance.

A path-only move MUST NOT be described as a semantic amendment.

---

## 32. No Mixed Semantic-and-Path Migration

A mechanical authority relocation commit MUST NOT simultaneously introduce substantive semantic changes to the relocated authority.

If semantic change is required:

```text
semantic lifecycle first
or
separate governed semantic change
```

The filesystem migration remains independently reviewable.

---

## 33. Corpus-Migration Sequence

The approved one-time corpus migration SHALL proceed in this order:

1. repair known current navigation defects before changing physical layout;
2. integrate the approved governance changes into the canonical governance files;
3. establish a deterministic recursive corpus checker capable of understanding both the current flat layout and the target stable-ID layout during transition;
4. migrate exactly one authority chain as a pilot;
5. verify content identity, navigation resolution, reference integrity and recursive conformance for that pilot;
6. only after the pilot passes, mechanically relocate the remaining accepted MS-PROT authority chains;
7. relocate implementation-programme authority under its stable programme identifier;
8. classify historical/prototype/review material separately; and
9. only after physical migration succeeds, perform navigation-only compaction of the Authority Index and DDR where separately safe.

Bulk migration MUST NOT begin before the pilot proves the migration mechanics.

---

## 34. Pilot Authority

The initial migration pilot SHALL be:

```text
MS-PROT-044
```

Its currently accepted constituents are:

```text
MS-PROT-044 — Offering, Listing & Published Subject Model.md
MS-PROT-044 v1.1 — Product, Offering & Variant Boundary Amendment.md
MS-PROT-044 v1.2 — Merchant Offering & Product Definition Access Contract Amendment.md
```

The pilot SHALL move them to:

```text
designs/authorities/ms-prot/MS-PROT-044/
```

with byte-identical authority content.

The pilot MUST prove:

```text
content identity
index resolution
repository-reference integrity
recursive checker success
no semantic diff
```

before any bulk movement.

---

## 35. Updated Hard Governance Invariants

The Section 21 invariants remain authoritative and are extended by the following:

15. Accepted MS-PROT authority chains MUST converge on one stable-Document-ID directory after the approved migration completes.
16. Physical directory location MUST NOT become semantic authority.
17. Business-domain folders MUST NOT become primary authority ownership or require duplicate authority copies.
18. `AUTHORITY-INDEX.md` and `DEFERRED-DECISION-REGISTER.md` MUST remain singular current navigation sources.
19. Mechanical path migration MUST preserve authority bytes and MUST NOT be combined with semantic amendment.
20. Historical/non-authoritative evidence MUST be classified before relocation and MUST NOT masquerade as accepted authority merely because of its filename.
21. Bulk accepted-authority migration MUST remain blocked until the MS-PROT-044 pilot passes the approved migration checks.

---

## 36. Revised Acceptance Statement

Main Street governance uses `MS-FUNDAMENTAL-VISION-001` as the upstream product-purpose authority, a single-current-document model for governance/navigation responsibilities, scope-aware semantic amendment chains where provenance affects current meaning, and stable-Document-ID physical colocation for accepted authority chains.

> **One fundamental product purpose; one live governance document per responsibility; one stable physical home per accepted authority chain; one Authority Index; one DDR; Git history for governance revisions; explicit semantic provenance where meaning depends on it.**