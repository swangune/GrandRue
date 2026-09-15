# MS-PROT-040 v1.6 — Replacement & Reinstatement Configuration Approval Authority Amendment

**Document ID:** MS-PROT-040
**Version:** 1.6
**Status:** ACCEPTED
**Approved:** Explicit manual confirmation and lifecycle-status reconciliation on 14 September 2026; original approval date is not asserted by this correction
**Authority type:** Merchant Configuration lifecycle / approval authority amendment
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `designs/DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`
**Amends:** composite MS-PROT-040 through v1.5 within non-initial Configuration Revision approval establishment and applicability only
**Preserves:** ordinary first-configuration approval under MS-PROT-040 v1.3; replacement/reinstatement validation, impact, activation, concurrency and historical-affinity rules under existing composite MS-PROT-040
**Purpose:** Establish the minimum durable and currently applicable approval authority for non-initial Configuration Revisions without inventing delegated staff authority, weakening exact evidence affinity or allowing historical approval to bypass current Merchant Controller authority.

---

## 1. Governing Decision

For the currently accepted Main Street authority model, an ordinary non-initial Configuration Revision may be approved only by the **current active Merchant Controller** for the affected Merchant Scope.

Canonical flow:

```text
immutable non-initial Configuration Revision
        ↓
exact successful validation evidence
        ↓
exact completed impact-review evidence
        ↓
current Merchant Controller reviews exact effects
        ↓
immutable Configuration Revision Approval
        ↓
separate activation
```

Therefore:

```text
validated
    ≠
impact reviewed
    ≠
approved
    ≠
active
```

No caller assertion, earlier approval, role name, ordinary workforce privilege, inference result or activation request may substitute for current Controller approval.

This amendment does not reject the MS-PROT-040 v1.0 concept of a future appropriately privileged merchant actor. It establishes that such delegated Configuration approval is **not yet an activated authority path**.

---

## 2. Scope

This amendment governs approval establishment and approval applicability for:

```text
replacement Configuration Revisions
later merchant Configuration changes
reinstatement decisions requiring current approval
```

It does not alter:

```text
ordinary first-configuration approval
Configuration candidate generation
compiler validation
impact-analysis ownership
existing-commitment semantics
semantic-release compatibility
activation authorization
serving-deployment admission
Merchant Controller transfer semantics
Workforce delegation semantics
```

---

## 3. Approval Authority Boundary

Configuration owns the immutable approval fact and determines whether that fact is applicable to an exact Configuration Revision.

Merchant Account authority remains the source of current Merchant Controller truth.

Therefore Configuration MUST query current Merchant Controller authority. It MUST NOT derive Controller authority from:

```text
approving principal identity alone
historical approval
Merchant Membership
Merchant Role Assignment
role display name
Commercial Entitlement
caller assertion
session claim alone
```

Merchant Controller relationship and Configuration approval remain distinct authoritative facts.

---

## 4. Non-Initial Configuration Revision Approval

A **Non-Initial Configuration Revision Approval** is an immutable Configuration-owned fact that the then-current Merchant Controller explicitly accepted one exact immutable non-initial Configuration Revision against one exact successful validation result and one exact completed business-facing impact review.

The existing approval affinity model is retained conceptually:

```text
ConfigurationRevisionApproval
{
    logicalApprovalRequestIdentifier
    merchantIdentifier
    configurationRevisionIdentifier
    semanticRegistryReleaseIdentifier
    validationEvidenceIdentifier
    impactReviewEvidenceIdentifier
    resolvedPackageEvidenceIdentifier
    approvingPrincipalIdentifier
    controllerRelationshipIdentifier
    approvedAt
}
```

The representation is conceptual and does not mandate a new table or Java type.

The existing durable `configuration_revision_approval` authority may represent both initial and non-initial approval facts provided their different admission predicates remain explicit.

---

## 5. Exact Controller Relationship Affinity

Approval SHALL bind both:

```text
approvingPrincipalIdentifier
+
controllerRelationshipIdentifier
```

The Controller Relationship must be the exact active relationship that established approval authority at commit.

Principal identity alone is insufficient.

Consequently, a historical approval SHALL NOT silently regain applicability merely because the same Identity later becomes Controller through a different Controller Relationship.

---

## 6. Approval Preconditions

Approval of a non-initial Configuration Revision may commit only when all of the following are true within the authority-sensitive commit boundary.

The Merchant Account and target Configuration Revision exist in the requested Merchant Scope.

The target revision is immutable and is a non-initial revision governed by the accepted replacement/reinstatement lifecycle.

The identified successful Configuration Validation Evidence exists and has exact merchant, revision, semantic-release and Resolved Configuration Package affinity.

The identified Configuration Impact Review Evidence exists and has exact merchant, revision, semantic-release, validation-evidence and package affinity.

The impact evidence contains no `BLOCKING` finding.

The approving principal is established through a trusted authenticated execution context for the same Merchant Scope.

The approving principal is the Identity of the current ACTIVE Merchant Controller Relationship at commit.

The exact Controller Relationship identity used for that authority is retained in the approval fact.

The logical approval request identity has not previously been committed for different intent.

No unspecified boolean such as:

```text
validated
reviewed
approved
authorised
ready
```

may replace these predicates.

---

## 7. Base-Revision Currentness Is Not an Approval Predicate

A non-initial candidate may be reviewed and approved even if concurrency later makes its base stale.

Approval SHALL NOT silently rebase, merge or activate the revision.

The mandatory stale-base/current-active conflict remains an activation concern under existing MS-PROT-040 authority.

Therefore:

```text
approval succeeds
+
base later/currently stale
        ↓
activation may reject
```

A successful approval is not a guarantee of eventual activation.

---

## 8. Business-Facing Impact Visibility

The approving Controller must accept the exact business-facing impact evidence identified by the approval.

Approval SHALL NOT be based only on:

```text
semantic graph changes
capability identifiers
compiler internals
policy identifiers
technical diffs
```

where merchant/customer consequences are required for informed review.

`CONSEQUENTIAL`, `INFORMATIONAL` and `EXISTING_COMMITMENT_CONFLICT` findings may be presented and approved where activation is otherwise valid.

A `BLOCKING` finding prevents approval.

Approval of an `EXISTING_COMMITMENT_CONFLICT` does not cancel, rewrite, move or discharge the affected commitment.

---

## 9. Direct Merchant Action

The existing MS-PROT-040 rule that explicit merchant action may constitute approval survives.

Where the current Controller directly initiates a supported Configuration change through an authoritative interface, is shown the exact completed impact review, and explicitly commits that exact result, Main Street MAY treat that final Controller action as the approval intent.

The system must still establish the same immutable approval fact and satisfy every predicate in this amendment.

Therefore user-experience compression does not collapse:

```text
validation
impact evidence
approval evidence
activation
```

into one semantic operation.

---

## 10. Inference-Proposed Changes

Inference, AI or registered derivation SHALL NOT constitute non-initial approval.

An inference-proposed candidate requires an explicit current-Controller acceptance after the exact impact evidence is available.

AI MAY explain the impact evidence.

AI MUST NOT:

```text
assert Controller authority
approve the revision
convert inference into approval
waive BLOCKING findings
change approval affinity
```

---

## 11. Approval Commit and Controller Concurrency

Current Controller authority MUST be revalidated within the transaction or equivalent authority-sensitive consistency boundary that decides whether approval commits.

If Controller transfer, suspension, termination or another Controller-authority transition commits before the approval has crossed its required current-authority decision boundary, the stale Controller approval MUST NOT subsequently commit.

The exact locking, fencing, compare-and-set or transaction mechanism is implementation architecture.

The observable authority rule is mandatory.

---

## 12. Approval Applicability

An approval fact is historical immediately after commit.

It is **currently applicable** to activation only while all exact revision/evidence affinity predicates continue to hold and the approval's exact Controller Relationship remains the current ACTIVE Merchant Controller Relationship for the Merchant Scope.

Conceptually:

```text
historical approval exists
        ≠
currently applicable approval
```

An applicability check SHALL verify:

```text
merchant
configuration revision
validation evidence
impact-review evidence
resolved package
approving principal
controller relationship
current ACTIVE Controller relationship
```

No approval may become applicable through principal identity alone.

---

## 13. Controller Transfer

If Controller Relationship `CR-A` approves revision `C2` and control later transfers to `CR-B`:

```text
CR-A approval remains immutable historical evidence
        +
CR-A approval becomes inapplicable
        +
current Controller under CR-B may approve C2
```

If the original person later becomes Controller again through a new relationship `CR-C`, the old `CR-A` approval SHALL NOT silently revive.

A new approval is required.

This prevents historical Controller authority from becoming future mutation authority.

---

## 14. Reinstatement

Reinstatement remains a new current decision.

A historical approval associated with an earlier activation SHALL NOT by itself authorise reinstatement.

Reinstatement must satisfy the currently applicable validation and impact-review requirements and must receive a current Controller approval against the exact evidence governing the reinstatement decision.

Where reinstatement produces new validation or impact evidence for an otherwise historically known revision, the new approval must bind that current evidence.

Rollback terminology SHALL NOT be used to bypass current review or approval.

---

## 15. Append-Only History

Approval facts are append-only.

Loss of applicability SHALL NOT:

```text
delete approval
rewrite approval
replace approving principal
replace Controller Relationship
replace reviewed evidence
```

A later approval creates another immutable approval fact.

Historical approval remains available for audit, support, explanation and reconstruction.

---

## 16. Logical Retry and Lost Acknowledgement

Approval establishment SHALL use a stable logical approval request identity.

If approval committed but acknowledgement was lost, retry with the same logical request identity and identical intent SHALL return the committed result rather than create another logical approval.

Reuse of that logical request identity with different:

```text
merchant
revision
validation evidence
impact evidence
approving principal
approval instant where represented as request intent
```

must be rejected as request-identity conflict.

Technical uncertainty about commit outcome must be reconciled before a new approval is attempted.

---

## 17. Duplicate and Concurrent Approval

Concurrent establishment of the same exact logical approval must result in at most one committed logical approval fact.

A later approval by a different current Controller Relationship is not a duplicate of a historical approval associated with an earlier Controller Relationship.

Multiple historical approvals may therefore exist for one immutable revision.

Activation requires at least one exact **currently applicable** approval, not merely one historical row.

---

## 18. Approval Has No Activation Side Effect

Successful approval MUST NOT itself:

```text
activate the Configuration Revision
move the current active pointer
publish customer-facing behaviour
rewrite existing commitments
promote semantic releases
change serving-deployment state
```

Approval and activation remain separate authority boundaries.

The principal who approves need not be the principal who later initiates activation; activation retains its own current authorization predicates.

---

## 19. No Workforce-Privilege Inference

Until a separate accepted authority establishes a legitimate registered Configuration approval Operation/privilege and its delegation/applicability semantics, none of the following grants non-initial Configuration approval authority:

```text
Merchant Membership
Manager role name
Administrator role name
booking privileges
ordering privileges
inventory privileges
payment privileges
group membership
combined workforce privileges
```

In particular:

```text
all workforce privileges combined
    ≠
Merchant Controller
    ≠
Configuration approval authority under this amendment
```

This amendment does not create `configuration.approve`.

---

## 20. Future Delegated Configuration Approval

MS-PROT-040 v1.0's concept of an appropriately privileged merchant actor remains valid future design scope.

Introducing such authority requires a separate accepted amendment that determines at least:

```text
the authoritative Configuration approval Operation or privilege
its semantic registration ownership
delegation ceiling
merchant scope
trusted staff execution requirements
mixed-change authority
current applicability after membership/role change
approval provenance
```

That future scope is not required to implement or close the Controller-approved non-initial Configuration path established here.

No future delegation may be inferred from this amendment.

---

## 21. Failure Semantics

The implementation must distinguish at least:

```text
revision absent or wrong merchant
revision is not an applicable non-initial revision
validation evidence absent or mismatched
package evidence absent or mismatched
impact evidence absent or mismatched
BLOCKING impact remains
trusted authenticated principal absent
current Controller absent
approving principal is not current Controller
Controller Relationship changed/stale
logical request identity conflict
approval concurrency conflict
```

Failure to approve creates no approval fact and performs no activation.

Previously committed validation and impact evidence remain historical evidence.

---

## 22. Fundamental Vision Conformance

This authority keeps the ordinary merchant path simple:

```text
review what changes
        ↓
approve
```

The merchant is not required to understand semantic registry structure, approval evidence internals, capability graphs or concurrency mechanics.

Small merchants require no new role administration.

Internal complexity is retained by Main Street to guarantee exact authority, historical explainability and safe concurrency.

Future delegation may be introduced only when it can reduce real merchant administration without exposing additional semantic or permission-system complexity.

---

## 23. Falsification Outcomes

A Booking Manager without Controller authority attempts to disable Booking: reject.

A caller supplies `approved=true`: reject.

A current Controller approves exact evidence: commit immutable approval.

Controller transfer races approval: stale Controller must not commit after transfer wins the authority boundary.

Controller transfers after approval but before activation: approval remains historical but is not applicable.

The same human later becomes Controller through a new Controller Relationship: old approval does not revive.

Candidate base becomes stale after approval: approval remains historical; activation rejects under current-base concurrency rules.

Inference creates the candidate: inference cannot approve.

A BLOCKING impact exists: approval cannot commit.

An existing-commitment conflict is reviewed and approved: approval does not mutate the commitment.

Approval acknowledgement is lost: exact logical retry returns the committed outcome.

A superseded revision is selected for reinstatement: historical approval is insufficient; current evidence and new current-Controller approval are required.

A sole trader with no staff configuration: no extra permission setup is introduced.

---

## 24. Rejected Alternatives

**Copy a delegated `configuration.approve` privilege into Workforce now:** rejected because Workforce roles accept only privileges already grounded in registered Main Street semantic Operations. No accepted Configuration approval Operation currently supplies that privilege.

**Infer approval from Administrator/Manager role labels:** rejected because role names are not authority.

**Map existing capability privileges to configuration approval:** rejected because operational authority such as Booking or Inventory does not imply authority to change the merchant operating model.

**Create per-capability or per-impact approval privileges now:** rejected as unnecessary permission-matrix complexity and unsupported semantic scope.

**Let a historical Controller approval remain applicable after Controller transfer:** rejected because stale authority would become future activation authority.

**Require the candidate base still to be active at approval time:** rejected because accepted MS-PROT-040 places stale-base conflict at activation; approval does not perform automatic merge/rebase.

---

## 25. Amendment Effect

This accepted amendment establishes a complete implementation-ready authority for durable non-initial Configuration Revision approval by the current Merchant Controller.

It preserves all existing first-configuration approval semantics and all separately governed replacement/reinstatement activation requirements.

It does not itself make IMP-05 complete.

Implementation must still prove the replacement approval writer, current applicability, retry/concurrency behaviour and subsequent R3 activation/reinstatement composition.

### Lifecycle-status reconciliation — 14 September 2026

Following explicit manual confirmation, the stale proposed-status metadata and conditional acceptance wording were reconciled with the accepted composition already identified by AUTHORITY-INDEX.md, the resolved records in DEFERRED-DECISION-REGISTER.md and accepted MS-PROT-040 v1.8. This correction follows DESIGN-RULES v2.3 and DOCUMENT-GOVERNANCE v2.3; it does not change substantive rules, amendment scope, historical governance references, implementation evidence or programme readiness. It does not assert a previously undocumented original approval date.
