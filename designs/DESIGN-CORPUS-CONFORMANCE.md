# Main Street Design Corpus Conformance Contract

**Document ID:** MS-DESIGN-CORPUS-CONFORMANCE-001  
**Version:** 2.6  
**Status:** Accepted governance conformance authority  
**Governed by:** `DOCUMENT-GOVERNANCE.md`  
**Purpose:** Define mechanically checkable structural rules that prevent fundamental-product-purpose authority drift, authority-navigation drift, competing current governance documents and selected high-confidence terminology defects without pretending automation can prove semantic or product-purpose correctness of arbitrary design prose.

---

## 1. Governing Principle

> **Automation checks structure; humans review semantics and product-purpose conformity.**

The conformance system detects repository-governance defects that can be established reliably from metadata, references, canonical filenames and narrowly defined patterns.

It MUST NOT claim that arbitrary design documents are semantically consistent or aligned with the Fundamental Vision merely because structural checks pass.

---

## 2. Required Canonical Governance Artefacts

Exactly one live canonical file SHALL exist for each current governance/rule responsibility:

```text
designs/DESIGN-RULES.md
designs/DOCUMENT-GOVERNANCE.md
designs/AUTHORITY-INDEX.md
designs/CANONICAL-SEMANTIC-LEXICON.md
designs/DEFERRED-DECISION-REGISTER.md
designs/DESIGN-CORPUS-CONFORMANCE.md
designs/IMPLEMENTATION-RULES.md
```

The accepted upstream product-purpose authority MUST also exist at its indexed canonical location:

```text
docs/foundation/Fundamental-Vision-Mission-and-Product-Constitution.md
    → MS-FUNDAMENTAL-VISION-001
```

Absence of a required canonical governance/rule file or of the indexed Fundamental Vision authority is an `ERROR`.

A competing live governance/rule companion such as:

```text
DESIGN-RULES vX — ... Amendment.md
DOCUMENT-GOVERNANCE vX — ... Amendment.md
DEFERRED-DECISION-REGISTER-CURRENT.md
DEFERRED-DECISION-REGISTER vX — ... Amendment.md
IMPLEMENTATION-RULES vX — ... Amendment.md
```

is an `ERROR` once its accepted content has been integrated into the canonical file.

Git history is the revision provenance for these single-current-document authorities.

---

## 3. Semantic Amendment Exception

The single-current-document rule does NOT flatten accepted MS-PROT semantic/design amendment chains where scope-aware provenance affects current meaning.

Conformance MUST therefore distinguish:

```text
governance/rule revision
    → merge into canonical current file

semantic/design amendment
    → preserve separately where scope composition matters
```

The Authority Index resolves the latter.

`MS-FUNDAMENTAL-VISION-001` is a substantive upstream product-purpose authority and is not a competing governance/rule companion.

---

## 4. Design-Rules Conformance

`DESIGN-RULES.md` is the sole current authority for Fundamental Vision Conformance, design-decision method, normative precision, ambiguity elimination, falsification and implementation-readiness gates.

Conformance SHOULD verify, where mechanically possible, that:

```text
DESIGN-RULES.md exists
+
accepted lifecycle/status is unambiguous
+
MS-FUNDAMENTAL-VISION-001 is referenced as the fundamental product-purpose authority
+
Fundamental Vision Conformance is present before ordinary material-design review
+
VISION-CONFORMING / VISION-CONFORMING WITH JUSTIFIED COMPLEXITY /
VISION-NON-CONFORMING / VISION-UNRESOLVED outcomes remain defined
+
single-current-document rule remains present
+
no competing live design-rules amendment exists
```

Structural checks MUST NOT substitute for the human Fundamental Vision review and ambiguity review required by `DESIGN-RULES.md`.

---

## 5. Fundamental Vision Authority Conformance

`MS-FUNDAMENTAL-VISION-001` is the accepted upstream authority for Main Street's fundamental product purpose.

Conformance SHALL verify mechanically that:

```text
docs/foundation/Fundamental-Vision-Mission-and-Product-Constitution.md exists
+
Document ID = MS-FUNDAMENTAL-VISION-001
+
Status = ACCEPTED
+
AUTHORITY-INDEX.md identifies it as current fundamental product-purpose authority
+
DOCUMENT-GOVERNANCE.md places it upstream of DESIGN-RULES.md
+
DESIGN-RULES.md references it
```

A lower-level accepted authority or current governance document that explicitly claims to override or bypass `MS-FUNDAMENTAL-VISION-001` without an accepted reconsideration/supersession authority is an `ERROR` where the contradiction is mechanically determinable.

Human conformance review MUST separately evaluate material accepted product/design/architecture authorities for substantive contradiction with the Fundamental Vision.

Conformance SHOULD flag for review high-confidence current/non-historical statements that appear to narrow Main Street's fundamental purpose to only a website builder, online-presence tool, commerce app, marketplace or software-module suite when those statements are presented as current global product purpose rather than bounded capability scope.

Historical evidence MAY retain superseded product-purpose language when its historical/non-authoritative status is clear.

---

## 6. Accepted Authority Metadata

New or materially amended accepted semantic/design or fundamental product-purpose authorities SHOULD expose parseable metadata sufficient to establish, where applicable:

```text
Document ID
Version
Status
Approved
Authority type
Governed by
Amends
Supersedes
Depends on
Closes
Purpose
```

Historical semantic/design documents predating this convention MAY remain without modern metadata where `AUTHORITY-INDEX.md` resolves their status.

---

## 7. Duplicate Semantic Authority Checks

The checker SHALL report an error when two semantic/design documents claim the same:

```text
Document ID
+
Version
+
ACCEPTED status
```

without an explicit governance explanation that one is a storage duplicate or non-competing representation.

The checker SHALL likewise report an error if more than one document claims to be the current accepted `MS-FUNDAMENTAL-VISION-001` authority without explicit supersession/navigation resolution.

A research/proposed document sharing an identifier with a later accepted authority is not automatically an error but MAY produce a navigation warning.

---

## 8. Dependency and Governance-Reference Checks

Every normative `Depends on` reference in a new/materially amended accepted semantic/design authority SHALL resolve to:

- an existing authority identifier; or
- an accepted decision explicitly incorporated into the depending authority.

Missing normative dependencies are errors.

Historical narrative references to retired experiments remain permitted when clearly non-normative.

### 8.1 `Governed by` Checks

For a current canonical governance/rule document, a normative `Governed by` reference SHALL resolve to an existing accepted authority valid for that governance relationship.

For a new or materially amended accepted semantic/design authority, each `Governed by` reference SHALL resolve to an existing accepted authority valid for the stated governance/review role at the time of approval.

An unresolved `Governed by` reference in new or materially amended authority is an `ERROR`.

Already accepted semantic/design authority MAY retain historical `Governed by` metadata in accordance with `DOCUMENT-GOVERNANCE.md`.

Where `DOCUMENT-GOVERNANCE.md` explicitly classifies a historical governance reference as non-operative, that historical reference SHALL NOT be treated as a current substantive dependency.

The known legacy `MS-AVS-001` references in already accepted authority therefore produce historical/non-blocking treatment rather than retroactive invalidation.

A new or materially amended authority that introduces or reasserts `MS-AVS-001` as current normative authority is an `ERROR`.

---

## 9. Semantic Amendment Checks

Where an accepted semantic/design authority declares:

```text
Amends: X
Supersedes: X
```

`X` MUST resolve to an existing authority/version.

An amendment MUST identify enough scope to determine whether it fully supersedes, narrowly replaces specified rules, or remains additive/more specific.

Scope ambiguity is a human-review warning and MUST NOT be reduced automatically to `max(version)`.

---

## 10. Authority Index Completeness

`AUTHORITY-INDEX.md` SHALL represent:

- `MS-FUNDAMENTAL-VISION-001` as the current accepted fundamental product-purpose authority; and
- every accepted MS-PROT authority in the current accepted design series.

Errors include:

```text
accepted fundamental product-purpose authority absent from index
accepted MS-PROT authority absent from index
index points to nonexistent identifier
index marks superseded/proposed evidence as sole current authority
```

The index MUST NOT require Document Governance to maintain a duplicate numeric current-series range.

---

## 11. Deferred Decision Register Consistency

`DEFERRED-DECISION-REGISTER.md` is the only current DDR conformance target.

Where an accepted authority closes a deferred-decision identifier, the current DDR MUST NOT continue to mark it open without an explicit later reopening authority.

A deliberately promoted new gap MAY be listed without a DDR identifier and MUST NOT be interpreted as an accepted semantic answer.

Historical DDR state is recovered from Git history, not competing current files.

---

## 12. Lifecycle Status Vocabulary

Recognised semantic/design lifecycle classes include:

```text
RESEARCH / PROTOTYPE
PROPOSED
ACCEPTED
SUPERSEDED
```

Unknown or contradictory lifecycle metadata in new/materially amended normative authorities SHALL be flagged.

---

## 13. High-Risk Terminology Checks

Automated terminology checks SHALL remain limited to high-confidence contexts such as semantic type headings, code/API identifiers in normative examples, authority tables, explicit canonical definitions and cross-domain diagrams.

Initial high-risk vocabulary is governed by `CANONICAL-SEMANTIC-LEXICON.md`.

`Administrative Compression` and other Fundamental Vision concepts are product-purpose principles, not automatically capability-owned semantic domain types merely because they appear in product/governance prose.

---

## 14. Fulfilment Terminology Rule

In cross-domain normative identifiers/definitions, prefer `Provider Fulfilment / Capability Fulfilment` and `Order Fulfilment` rather than bare `Fulfilment` where both meanings are plausible.

Ordinary English use is not automatically an error.

---

## 15. Availability Terminology Rule

Where multiple availability authorities participate in the same specification or diagram, qualify the term, for example:

```text
Booking Availability
Appointment Availability
Inventory Availability
Provider Readiness
```

The ordinary word `available` is not globally prohibited.

---

## 16. Customer Terminology Rule

Where identity/relationship authority matters, normative identifiers SHOULD distinguish `Visitor`, `CustomerContext` and `CustomerAccount`.

---

## 17. Exposure Terminology Rule

Where audience observation is meant, `Exposure` is the canonical cross-domain term and SHALL mean observation of an already-legitimate candidate semantic element. Presentation-only visibility may use ordinary UI terminology where it cannot be mistaken for semantic Exposure authority.

`Exposure` SHALL NOT be used as a synonym for Surface membership, Projection Serviceability, operation invokability or execution authority. New cross-domain Exposure definitions SHOULD use the owner-qualified `Exposure Element Contract` and server-established `Audience Observation Context` terminology where those MS-PROT-027 v1.5 concepts are intended.

---

## 18. Runtime Decision Vocabulary

Conformance SHOULD preserve distinction among:

```text
Semantic Applicability
Commercial Entitlement
Actor Authorisation
Resource Protection Admission
Operational Eligibility
Provider Readiness
Projection Serviceability
Surface Eligibility / Membership
Exposure
```

`Exposure` governs audience observation only; it MUST NOT be treated as operation invokability or execution authority.

`Resource Protection Admission` is governed by the composite MS-PROT-062 authority and MS-PROT-073 and MUST NOT be collapsed into Commercial Entitlement, Actor Authorisation, Operational Eligibility or Provider Readiness.

A new central identifier that appears to collapse these dimensions SHOULD trigger architectural review rather than automatic semantic rejection based solely on its name.

---

## 19. Implementation-Governance Conformance

`IMPLEMENTATION-RULES.md` is a required single-current-document authority.

Conformance SHOULD verify, where mechanically possible, that:

```text
IMPLEMENTATION-RULES.md exists
+
accepted lifecycle/status is unambiguous
+
composite-architecture/programming-paradigm rules remain present
+
no competing live implementation-rules amendment file exists
```

Implementation Rules MUST remain distinct from semantic ownership.

Implementation conformance MUST treat accepted upstream Fundamental Vision/product-purpose constraints as binding through the authority hierarchy; this does not require IMPLEMENTATION-RULES to duplicate the full Vision Conformance Gate.

---

## 20. Canonical-Reference Checks

Current governance/rule files MUST reference canonical filenames rather than removed versioned companions.

Current governance files that depend on Fundamental Vision Conformance MUST reference `MS-FUNDAMENTAL-VISION-001` and/or its canonical indexed path rather than an obsolete or ambiguous generic `Vision.md` reference.

Current governance/rule documents MUST NOT treat MS-AVS-001 as current normative authority.

A mechanically detectable current normative `MS-AVS-001` dependency is an `ERROR`.

Historical semantic/design metadata is governed by the compatibility rule in `DOCUMENT-GOVERNANCE.md`.

Examples of invalid current references include versioned Design Rules, Document Governance, DDR or Implementation Rules amendment filenames and `DEFERRED-DECISION-REGISTER-CURRENT.md`.

Historical semantic/design prose may mention obsolete governance filenames only as clearly historical evidence where necessary.

---

## 21. Prohibited False-Semantic Checks

Conformance automation SHALL NOT claim to determine automatically whether arbitrary paragraphs express the same invariant, whether an architectural trade-off is correct, whether a capability boundary is valid, whether merchant policy is respected in every prose example, whether a lifecycle is semantically complete, whether two semantic concepts should be merged, or whether arbitrary prose substantively conforms to the Fundamental Vision.

These remain Fundamental Vision review, design-review and falsification responsibilities.

---

## 22. Severity Classes

```text
ERROR
    structurally provable governance defect

WARNING
    likely navigation/terminology/product-purpose ambiguity requiring review

INFO
    historical/non-blocking observation
```

Examples:

```text
ERROR required canonical governance file absent
ERROR MS-FUNDAMENTAL-VISION-001 canonical authority absent
ERROR DESIGN-RULES lacks required Fundamental Vision authority reference
ERROR competing live governance amendment file present
ERROR accepted MS-PROT missing from Authority Index
ERROR nonexistent normative dependency
ERROR new/materially amended authority contains an unresolved Governed by reference
ERROR current governance/rule document treats MS-AVS-001 as normative authority
WARNING current global product statement appears to narrow Main Street to online presence only
WARNING proposed and accepted semantic files share unclear identity
WARNING bare Fulfilment used as cross-domain semantic heading
INFO historical semantic/product file lacks modern metadata
INFO already accepted authority retains historical MS-AVS-001 Governed by metadata under DOCUMENT-GOVERNANCE.md
```

---

## 23. Governance Completion Gate

For a newly accepted material design change:

```text
MS-FUNDAMENTAL-VISION-001 applicable conformance established
    ✓
DESIGN-RULES.md satisfied
    ✓
accepted authority
    ✓
AUTHORITY-INDEX.md
    ✓
DEFERRED-DECISION-REGISTER.md where applicable
    ✓
CANONICAL-SEMANTIC-LEXICON.md where applicable
    ✓
IMPLEMENTATION-RULES.md impact reviewed
    ✓
canonical governance references
    ✓
structural conformance
    PASS
```

The implementation mechanism for automated conformance remains downstream.

---

## 24. Historical Preservation

Conformance SHALL NOT require deletion or mass rewriting of historical product, research, prototype, ADR or semantic-design evidence merely to make the repository aesthetically uniform.

Historical product-purpose statements that conflict with `MS-FUNDAMENTAL-VISION-001` MAY remain when clearly classified as historical/non-authoritative evidence.

Obsolete standalone governance/rule revision files SHOULD be removed after integration into their canonical file because Git history supplies that revision provenance.

The objective is:

```text
preserve semantic/product reasoning
+
one current fundamental product-purpose authority
+
one current governance source per responsibility
+
make current authority obvious
+
prevent future drift
```

---

## 25. Acceptance Statement

Main Street corpus conformance enforces the accepted Fundamental Vision authority, the single-current-document governance model and scope-aware semantic amendment chains while preserving the distinction between mechanically provable structural defects and human review of substantive product/semantic truth.

> **One current fundamental product purpose; one current governance source per responsibility; structural automation for navigation integrity; human review for product-purpose and semantic truth.**

---

## 26. Recursive Corpus Scan

Structural conformance MUST scan the complete applicable design corpus recursively.

A checker MUST NOT assume:

```text
designs/*.md
```

is the complete authority corpus.

After the approved migration completes, accepted authority scanning MUST include:

```text
designs/authorities/**
```

and the canonical governance files.

Historical/non-authoritative areas SHALL be classified separately rather than silently treated as current authority.

During the bounded migration transition, the checker MAY recognise accepted authority in both the legacy flat location and the target stable-ID location, but it MUST still reject duplicate competing authority identities.

---

## 27. Authority Identity Structural Errors

The following SHALL be `ERROR`:

```text
duplicate accepted Document ID + Version

same Document ID + Version represented by conflicting files

accepted MS-PROT outside its canonical authority directory
    after migration completion

authority-directory ID != document metadata ID

accepted authority absent from AUTHORITY-INDEX

AUTHORITY-INDEX version/composition referring to a missing authority

two canonical directories for one stable Document ID

unapproved/proposed authority persisted in the accepted authority store

single-current governance authority duplicated by a companion CURRENT/LATEST file
```

The checker SHOULD also warn where a non-accepted file occupies a candidate stable version in a way likely to cause future authoring collision.

---

## 28. Preflight Mode

The deterministic checker SHALL support an authority-preflight operation conceptually equivalent to:

```text
preflight
    Document ID
    candidate Version
```

Its output MUST identify at least:

```text
existing versions
their repository locations
their lifecycle statuses
current indexed composition
candidate collision result
canonical destination
```

A candidate version already occupied by any repository document claiming that same stable Document ID + Version SHALL produce a non-success collision result, regardless of whether the occupant is ACCEPTED, SUPERSEDED, HISTORICAL, PROPOSED, DRAFT or another lifecycle state.

---

## 29. Full-Conformance Mode

The checker SHALL support a full-corpus mode validating at least:

```text
required canonical governance files

stable-ID directory placement

metadata/path affinity

duplicate ID/version detection

accepted-index coverage

index-to-file existence

canonical DDR presence

prohibited duplicate current governance files

broken repository-relative Markdown authority links
    where mechanically detectable
```

Semantic contradiction detection remains a human design-review responsibility.

Mechanical success MUST NOT be represented as semantic proof.

---

## 30. Local Deterministic Tool

The initial checker SHOULD be implemented as:

```text
tools/DesignCorpusCheck.java
```

using only the Java standard library where practical.

The repository currently targets Java 25.

The checker SHOULD therefore be runnable without Maven, Spring, PostgreSQL or GitHub Actions, conceptually:

```text
java tools/DesignCorpusCheck.java --full

java tools/DesignCorpusCheck.java \
    --preflight MS-PROT-044 \
    --candidate-version 1.3
```

Exact command syntax MAY be refined during implementation provided the governance behaviours remain unchanged.

The checker is governance tooling.

It is not semantic authority.

---

## 31. ChatGPT / GitHub-Connector Equivalence

When design work is performed remotely through ChatGPT rather than a checked-out repository, the same preflight obligations apply.

The executor MAY satisfy them using repository reads/searches rather than the local Java tool.

The evidence standard remains:

```text
exact branch
exact HEAD
current authority composition
repository-resident versions
candidate collision result
```

The local checker therefore strengthens the workflow without making local-machine access mandatory.

---

## 32. Migration Link Integrity

Before a path-migration commit is complete, structural conformance MUST identify repository-local Markdown references to moved paths where reasonably mechanically detectable.

A migration that leaves authoritative navigation pointing to nonexistent files is non-conforming.

Stable-ID prose references that do not depend on physical paths need no artificial rewrite.

---

## 33. Pilot-Migration Conformance

Before any accepted MS-PROT bulk migration, the checker MUST validate the MS-PROT-044 pilot under both identity and reference-integrity rules.

The pilot passes only when:

```text
all three accepted MS-PROT-044 constituents exist in the canonical stable-ID directory
+
no competing legacy copies remain after the move completes
+
Document ID / Version / Status metadata remains unchanged
+
AUTHORITY-INDEX resolves the moved chain
+
repository-local path references to the old locations have been repaired where required
+
no semantic-content modification occurred during the move
```

Bulk authority movement remains blocked while any pilot condition fails.

---

## 34. Updated Severity Examples

The Section 22 severity classes remain unchanged and are extended by the following examples:

```text
ERROR candidate preflight version already occupied
ERROR two files claim the same Document ID + Version with conflicting content
ERROR accepted authority stored under the wrong stable-ID directory after migration completion
ERROR stable-ID directory name disagrees with authority metadata
ERROR accepted Authority Index constituent missing from repository
ERROR pilot migration leaves both old and new accepted copies
WARNING non-accepted document occupies a version likely to collide with future authoring
INFO historical evidence remains outside the accepted authority store
```

---

## 35. Revised Governance Completion Gate

For a newly accepted material design change, governance completion additionally requires:

```text
pre-design identity preflight performed
    ✓
post-approval latest-head identity revalidation performed
    ✓
accepted authority formalised without collision
    ✓
recursive structural conformance
    PASS
```

Where the authority is formalised during a multi-commit governance cycle, intermediate commits do not count as governance completion and MUST NOT be used as the basis for dependent formalisation until the cycle is complete.

---

## 36. Revised Acceptance Statement

Main Street corpus conformance enforces the accepted Fundamental Vision authority, single-current governance, scope-aware semantic amendment chains, stable authority identity, recursive authority discovery and deterministic collision prevention while preserving the distinction between mechanically provable structural defects and human review of substantive product/semantic truth.

> **One current product purpose; one current governance source per responsibility; one stable authority identity per version; recursive structural checks for navigation integrity; human review for semantic truth.**