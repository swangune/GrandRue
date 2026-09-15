# MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment

**Document ID:** MS-PROT-040  
**Version:** 1.3  
**Status:** **ACCEPTED by manual approval on 29 August 2026**  
**Approved:** Manual approval on 29 August 2026  
**Authority type:** Merchant Configuration lifecycle / evidence design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-040 v1.0, v1.1 and v1.2 within durable validation/package evidence, business-facing impact-review evidence and ordinary first-configuration approval persistence scope  
**Supersedes:** No accepted authority outside the amended scope  
**Depends on:** MS-PROT-021 v1.3; MS-PROT-022 v1.5; MS-PROT-040 v1.0 + v1.1 + v1.2; MS-PROT-052 v1.2; MS-PROT-069 v1.1; MS-PROT-071 v1.1; MS-PROT-076  
**Closes:** No deferred-decision identifier; closes the implementation-readiness gap between deterministic package production and exact current-Controller approval persistence  
**Purpose:** Require immutable revision/release/package-affined validation evidence and immutable business-facing impact-review evidence before ordinary first-configuration approval can commit, while preserving exact approval affinity, current-Controller authority, append-only history and retry/concurrency safety.

---

## 1. Governing Decision

The ordinary first-configuration path SHALL distinguish:

```text
deterministic compiler and Resolved Configuration Package model
        ↓
immutable exact validation/package evidence
        ↓
immutable exact business-facing impact-review evidence
        ↓
current Merchant Controller approval of that exact evidence
        ↓
separate later activation admission
```

Therefore:

```text
compiler/RCP model exists
    ≠ exact validation/package evidence durably exists
    ≠ exact impact-review evidence durably exists
    ≠ current Controller approved
    ≠ revision active
```

An approval command MUST NOT replace either evidence dependency with a caller-supplied boolean, assertion or unverified reference.

---

## 2. Problem and Governed Scope

MS-PROT-040 v1.1 establishes exact Configuration Revision, semantic-release and Resolved Configuration Package affinity. MS-PROT-040 v1.2 requires deterministic validation, impact analysis and exact current-Controller approval before ordinary first activation.

Those rules do not by themselves define the minimum durable evidence that an approval transaction must consume. Without this amendment, an implementation could incorrectly accept `validated = true`, `impactReviewed = true` or `approved = true` from a caller without proving the exact revision, semantic release, package, impact content or current approving authority.

This amendment governs the missing evidence and approval-persistence boundary for the ordinary first Merchant Configuration. It also clarifies which historical approval remains applicable after Controller transfer.

---

## 3. Explicit Non-Goals

This amendment does not define:

```text
impact-review screen layout
transport or public API representation
database table or serialization format
cryptographic digest algorithm
semantic compiler implementation
Resolved Configuration Package internal representation
activation or serving-deployment admission implementation
later delegated configuration-approval authority
approval-age or revalidation policy
```

Those matters remain governed by existing accepted authority or by implementation architecture where no new business rule is required.

---

## 4. Canonical Terminology

### 4.1 Configuration Validation Evidence

**Configuration Validation Evidence** is an immutable durable fact that a trusted deterministic compiler/resolution path successfully validated one exact immutable Configuration Revision against one exact semantic-registry release and produced one exact Resolved Configuration Package result.

It is evidence about derived semantics. It is not Merchant Configuration authority, merchant approval or activation authority.

### 4.2 Configuration Impact Review Evidence

**Configuration Impact Review Evidence** is an immutable durable representation of the business-facing effects and classified impact findings produced for one exact validation/package result and made available for merchant review.

It is not semantic validation, merchant approval or activation authority.

### 4.3 Configuration Revision Approval

A **Configuration Revision Approval** is an immutable Merchant Configuration-owned fact that one authorised principal explicitly accepted one exact immutable Configuration Revision using one exact applicable validation-evidence record and one exact applicable impact-review-evidence record.

For the ordinary first configuration governed here, the authorised approving principal is the current Merchant Controller at approval commit.

### 4.4 Applicable Approval

An approval is **applicable** to an ordinary first-configuration activation only when its revision, evidence and approving principal continue to satisfy the exact predicates in Sections 12 and 16. Historical existence alone does not make an approval currently applicable.

---

## 5. Ownership

The deterministic compiler/resolution subsystem owns production of the resolved result and its compiler provenance under composite MS-PROT-021 and MS-PROT-022 authority.

The Merchant Configuration context owns:

```text
durable lifecycle evidence that the exact result was validated
durable business-facing impact-review evidence
approval provenance
whether an approval is applicable to the exact revision
```

The Merchant Account/Controller authority remains the source of current Controller truth. The Merchant Configuration context MUST query that authority; it MUST NOT infer Controller authority from an earlier approval, onboarding actor or caller claim.

No evidence record created by this amendment becomes a second source of Merchant Configuration content, semantic-registry meaning, Controller identity or active-revision truth.

---

## 6. Validation-Evidence Identity and Minimum Affinity

Every Configuration Validation Evidence record MUST have a stable identity and MUST immutably establish:

```text
validationEvidenceIdentifier
merchantIdentifier
configurationRevisionIdentifier
semanticRegistryReleaseIdentifier
exact Resolved Configuration Package evidence reference
successful deterministic validation outcome
evidence production instant
trusted compiler/resolution provenance
```

The Resolved Configuration Package evidence reference MUST resolve to the exact retained immutable package result or exact retained immutable package provenance required for historical reproduction. This amendment does not require a new universal package identifier or digest representation.

Two validation-evidence records are the same record only when their `validationEvidenceIdentifier` values are equal within the owning merchant scope.

---

## 7. Validation-Evidence Production Predicate

Validation evidence MAY be recorded only when all of the following are true:

1. the Configuration Revision exists and belongs to the target Merchant Account;
2. the revision is immutable under MS-PROT-040 v1.1;
3. the revision identifies the exact semantic-registry release used by compilation;
4. deterministic validation succeeded using that exact revision and release;
5. the exact Resolved Configuration Package result identifies that revision and release as its source affinity;
6. required MS-PROT-022 provenance is available through the retained package result or evidence reference.

Failure of any predicate MUST NOT create successful validation evidence.

---

## 8. No Caller-Boolean Validation

The approval operation MUST NOT accept any of the following as validation evidence:

```text
caller boolean
UI state
request claim
unverified package identifier
successful onboarding submission
successful candidate persistence
compiler/RCP type existence
```

The approving path SHALL resolve an immutable validation-evidence identity through a trusted internal authority and verify its exact affinity.

---

## 9. Impact-Review-Evidence Identity and Minimum Affinity

Every Configuration Impact Review Evidence record MUST have a stable identity and MUST immutably establish:

```text
impactReviewEvidenceIdentifier
merchantIdentifier
configurationRevisionIdentifier
semanticRegistryReleaseIdentifier
validationEvidenceIdentifier
exact Resolved Configuration Package evidence reference
business-facing effect content reviewed
zero or more classified impact findings
impact-analysis completion instant
```

Each impact finding MUST have exactly one applicable MS-PROT-040 classification:

```text
BLOCKING
CONSEQUENTIAL
INFORMATIONAL
EXISTING_COMMITMENT_CONFLICT
```

The evidence MUST preserve the exact business-facing effect content and findings to which later approval refers. An implementation MAY choose its durable encoding, ordering and rendering mechanics provided that later reconstruction cannot silently substitute different effect content or findings under the same evidence identity.

---

## 10. Business-Facing Review Boundary

Impact-review evidence SHALL describe merchant/customer operational effects in intelligible business terms as required by MS-PROT-040 v1.0.

The evidence MUST NOT rely only on semantic-internal representations such as compiler graph nodes, intermediate representation or internal requirement identifiers.

Semantic-internal provenance MAY accompany the business-facing evidence for diagnosis. It MUST NOT replace the business-facing effect content required for approval.

---

## 11. Impact-Evidence Production Predicate

Impact-review evidence MAY be recorded only when:

1. the referenced validation evidence exists;
2. its merchant, revision, release and exact package affinity match that validation evidence;
3. impact analysis completed for that exact result;
4. each material finding is classified under Section 9;
5. the business-facing effect content is durably retained under the evidence identity.

A later change to revision contents, release affinity, package result, effect content or findings requires a different applicable evidence identity. Existing evidence remains immutable historical fact.

---

## 12. Ordinary First-Configuration Approval Preconditions

`ApproveInitialConfigurationRevision` may commit only when all of the following are true inside the owning transaction:

1. the target Merchant Account exists;
2. the target Configuration Revision exists and belongs to that Merchant Account;
3. the revision is the immutable ordinary first-configuration revision being approved;
4. the identified Configuration Validation Evidence exists;
5. validation-evidence merchant, revision and release affinity exactly match the target revision;
6. the exact package evidence reference resolves consistently with that validation evidence;
7. the identified Configuration Impact Review Evidence exists;
8. impact-evidence merchant, revision, release, validation-evidence and package affinity exactly match;
9. the impact-review evidence contains no `BLOCKING` finding;
10. the approving principal is an authenticated trusted principal;
11. that principal is the current Merchant Controller at commit;
12. the logical approval request identity has not previously been used for different intent.

No undefined `validated`, `reviewed`, `authorised`, `ready` or equivalent boolean may replace these predicates.

---

## 13. Approval Intent and Evidence Affinity

The approval command MUST identify at least:

```text
logicalApprovalRequestIdentifier
merchantIdentifier
configurationRevisionIdentifier
validationEvidenceIdentifier
impactReviewEvidenceIdentifier
approvingPrincipalIdentifier
```

Successful approval records MUST preserve those values plus the approval instant.

The approving action means:

> The current Merchant Controller explicitly accepts this exact immutable revision with the exact business-facing impact-review evidence identified by the approval.

Approval of one evidence combination MUST NOT silently transfer to different revision, release, package, validation evidence or impact-review evidence.

---

## 14. Onboarding Review Is Not Approval Evidence

MS-PROT-052 v1.2 `OnboardingFinalReview` precedes creation and exact validation of the first Configuration Revision. Therefore it cannot by itself establish:

```text
exact Configuration Revision approval
exact semantic-release affinity
exact Resolved Configuration Package affinity
exact post-compilation impact-review evidence
current Controller authority at approval commit
```

An onboarding review or Initial Configuration Intent MAY contribute merchant-intent provenance. Neither is a substitute for the validation evidence, impact-review evidence or approval fact governed here.

---

## 15. Approval Transaction and Authority Revalidation

The approval operation SHALL:

```text
resolve exact revision and evidence
        +
resolve current Merchant Controller authority
        +
validate request identity and duplicate state
        ↓
commit one immutable approval fact atomically
```

Controller authority MUST be revalidated within the transaction boundary that decides whether approval commits, using a concurrency mechanism that prevents an approval from committing on stale Controller authority.

The exact locking, compare-and-set, fencing or database constraint mechanism is implementation architecture. The observable invariant is mandatory.

---

## 16. Controller Transfer and Approval Applicability

If Controller A approves an ordinary first Configuration Revision and Controller authority later transfers to B:

```text
A approval remains immutable historical evidence
        +
A approval is not the current applicable first-activation approval
        +
B may approve the same unchanged revision and exact evidence
        +
B approval becomes applicable if all current predicates hold
```

Controller transfer alone MUST NOT mutate or delete A's approval and need not create a new Configuration Revision.

At first-activation admission, Main Street MUST resolve an applicable approval whose approving principal is the then-current Merchant Controller and whose exact revision and evidence affinity still match.

---

## 17. Append-Only Approval History

Configuration Revision Approval facts are append-only.

A later Controller approval, activation failure, Controller transfer or loss of current applicability MUST NOT overwrite or delete an earlier approval fact.

Historical approvals MAY coexist for one unchanged revision when different principals held current Controller authority at their respective approval commits.

---

## 18. Retry and Lost Acknowledgement

If an approval commit succeeds but acknowledgement is lost, retry with the same logical approval request identifier and identical intent MUST return the already-committed approval outcome and MUST NOT create another logical approval.

Reuse of that request identifier with any different merchant, revision, evidence or approving principal MUST be rejected as a request-identity conflict.

A retry MUST re-establish whether the recorded outcome exists before attempting a new commit. It MUST NOT manufacture success solely from the caller's assertion that an earlier attempt succeeded.

---

## 19. Duplicate and Concurrency Rule

The logical approval fact is unique for the exact combination of:

```text
merchantIdentifier
configurationRevisionIdentifier
validationEvidenceIdentifier
impactReviewEvidenceIdentifier
approvingPrincipalIdentifier
```

Concurrent attempts to establish that same logical approval MUST result in at most one committed logical approval fact. Successful duplicate resolution MAY return the existing fact.

An approval from a later current Controller is not a duplicate of a historical approval from an earlier Controller.

---

## 20. Failure and Rejection Semantics

The implementation MUST distinguish at least the following observable rejection causes:

```text
revision absent or wrong merchant
validation evidence absent or affinity mismatch
package evidence absent or affinity mismatch
impact-review evidence absent or affinity mismatch
blocking impact remains
current Controller absent
approving principal is not current Controller
logical request identity reused for different intent
approval concurrency conflict
```

These are business/admission rejections rather than proof that an approval committed.

A technical failure with unknown commit outcome MUST be reconciled by logical request identity before a new approval fact is attempted, consistent with MS-PROT-069 v1.1.

---

## 21. No Partial Authority on Failure

If approval does not commit:

```text
no approval fact exists for that attempt
no active Configuration changes
no publication changes
no serving-deployment state changes
```

Already committed validation evidence and impact-review evidence remain historical evidence and MAY be reused only while all exact affinity and current-governance predicates continue to hold.

---

## 22. Approval Has No Activation Side Effect

A successful approval commit MUST NOT itself:

```text
compile or rematerialise the package
activate the revision
publish merchant experience
promote a semantic release
alter serving-deployment admission
start customer operations under the revision
```

Activation remains the distinct operation governed by composite MS-PROT-040 v1.1 and v1.2.

---

## 23. Historical Reproducibility

For every approval, Main Street MUST be able to establish later:

```text
which merchant and revision were approved
which semantic-registry release governed validation
which exact package result/provenance was validated
which business-facing effects and findings were reviewed
which principal approved
whether that principal was current Controller at commit
when approval committed
which logical request established it
```

Historical reconstruction MUST NOT depend on current mutable UI state, current compiler defaults, current semantic-release selection or current Controller identity.

---

## 24. AI Boundary

AI MAY explain business-facing impact evidence or assist a merchant in navigating it.

AI MUST NOT:

```text
create successful validation evidence without deterministic compiler success
replace the exact impact-analysis result
assert Controller authority
approve on behalf of the Controller
change evidence affinity
convert onboarding inference into approval
```

---

## 25. Falsification Evidence

The amendment was tested against the following cases:

| Case | Required result |
|---|---|
| Caller submits `validated=true` | Reject; trusted immutable validation evidence is required. |
| Validation evidence belongs to another release | Reject; revision, release and package affinity must all match. |
| Onboarding final review is supplied as approval | Reject; it predates exact validation and impact evidence. |
| Impact summary changes after review | Require a different evidence identity and approval. |
| A blocking finding remains | Reject; a caller cannot waive the finding with a boolean. |
| Controller changes during approval | Stale Controller authority cannot commit approval. |
| Controller changes after approval | Preserve history; require the new current Controller before first activation. |
| Approval acknowledgement is lost | Exact retry returns the committed approval without duplication. |
| Concurrent duplicate approvals race | At most one logical approval fact commits. |
| Activation later fails | Approval remains historical and may remain applicable if exact predicates still hold. |

---

## 26. Rejected Alternatives

| Alternative | Rejection reason |
|---|---|
| Caller booleans for validation or review | They do not prove exact affinity or trusted evidence origin. |
| Treat onboarding final review as approval | It occurs before the exact revision, compilation result and impact evidence exist. |
| Store only revision and approving principal | This loses the exact evidence accepted by the principal. |
| Overwrite approval when Controller changes | This destroys historical authority provenance. |
| Make approval activate immediately | This collapses separate approval and activation lifecycles. |
| Require a universal package digest here | Existing authority requires exact provenance; one encoding would unnecessarily govern implementation architecture. |

---

## 27. Trade-Offs and Consequences

This amendment accepts additional durable evidence and referential constraints in exchange for auditable exact approval, no trust in caller assertions, safe Controller transfer, retry-safe persistence and historical reconstruction.

It deliberately leaves storage schema, package-reference encoding and impact rendering to conformant implementation because those mechanisms do not alter the approved business predicates.

---

## 28. Deferred and Future Scope

Existing MS-PROT-040 v1.2 deferred questions remain unchanged, including approval-age policy, transport representation and future delegated first-configuration authority.

No new material semantic question is deferred by this amendment. If implementation discovers that exact evidence cannot be retained without selecting a new business identity or authority rule, design MUST reopen before that choice is implemented.

---

## 29. Implementation Constraints

A conformant implementation SHALL provide separate durable authorities for:

```text
exact validation/package evidence
exact business-facing impact-review evidence
append-only approval facts
```

Physical co-location in one database or transaction service is permitted. Logical separation of identities, predicates and ownership is mandatory.

Implementation MAY add technical metadata, indexes, constraints and internal references provided they do not weaken or replace the minimum evidence and approval contract.

---

## 30. Conformance Criteria

Implementation conforms only if tests establish at least:

```text
[ ] successful validation evidence is immutable and exact-affined
[ ] caller booleans cannot establish validation or review
[ ] impact-review evidence retains exact business-facing content and classifications
[ ] mismatched merchant/revision/release/package/evidence is rejected
[ ] blocking impact rejects approval
[ ] current Controller is revalidated at approval commit
[ ] onboarding final review cannot substitute for approval evidence
[ ] approval retains both exact evidence identities
[ ] approval history is append-only across Controller transfer
[ ] a later current Controller can approve the unchanged exact revision
[ ] exact retry returns the original approval
[ ] request-identity reuse with different intent conflicts
[ ] concurrent duplicate approval creates at most one logical fact
[ ] approval does not activate, publish or promote anything
[ ] canonical PostgreSQL-backed verification proves durable constraints
```

---

## 31. Amendment Effect

This amendment adds the minimum durable evidence and approval-persistence contract required between compilation and ordinary first activation. It narrows any implementation interpretation that permitted caller booleans, pre-revision onboarding review or mutable review content to stand in for exact evidence.

MS-PROT-040 v1.0 remains authoritative for the configuration lifecycle, impact classifications, business-facing review and general contextual approval rules.

MS-PROT-040 v1.1 remains authoritative for revision immutability, semantic-release affinity, Resolved Configuration Package semantics, activation atomicity and historical package binding.

MS-PROT-040 v1.2 remains authoritative for ordinary first-configuration bootstrap, current-Controller approval/activation authority, serving-deployment admission and activation fencing.

All rules outside the explicitly amended evidence and ordinary first-approval-persistence scope remain unchanged.

---

## 32. Acceptance Statement

MS-PROT-040 v1.3 is accepted when Main Street treats exact durable validation/package evidence and exact durable business-facing impact-review evidence as prerequisites to current-Controller approval persistence, preserves evidence affinity and append-only authority history, and keeps approval separate from activation.

> **A Controller approves an exact immutable Configuration Revision through exact immutable validation/package and business-facing impact-review evidence. Caller assertions and pre-revision onboarding review cannot create that authority; Controller transfer preserves history but requires the current Controller's own applicable approval before ordinary first activation.**

---

## Governance Assessment

```text
DESIGN / PROPOSE: COMPLETE
AUTHORITY TRACE: COMPLETE
OWNERSHIP REVIEW: PASS
EVIDENCE-AFFINITY REVIEW: PASS
FAILURE / RETRY / CONCURRENCY REVIEW: PASS
CONTROLLER-TRANSFER FALSIFICATION: PASS
AMBIGUITY REVIEW: PASS within the approved amendment scope
IMPLEMENTATION READINESS: READY for exact evidence persistence followed by approval persistence
MANUAL APPROVAL: GRANTED on 29 August 2026
STATUS: ACCEPTED
```
