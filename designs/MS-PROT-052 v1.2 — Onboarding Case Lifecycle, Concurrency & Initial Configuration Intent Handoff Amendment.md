# MS-PROT-052 v1.2 — Onboarding Case Lifecycle, Concurrency & Initial Configuration Intent Handoff Amendment

**Document ID:** MS-PROT-052  
**Version:** 1.2  
**Status:** **ACCEPTED after governed review, falsification, ambiguity review and manual approval**  
**Approved:** Manual approval on 27 August 2026  
**Amends:** MS-PROT-052 v1.0 and MS-PROT-052 v1.1 within onboarding-engine lifecycle, durable progress, correction, concurrency and initial-configuration handoff scope  
**Depends on:** MS-PROT-021, MS-PROT-022, MS-PROT-038, MS-PROT-040 v1.2, MS-PROT-052 v1.0, MS-PROT-052 v1.1, MS-PROT-057, MS-PROT-063, MS-PROT-071 v1.1/v1.2, MS-PROT-076, MS-PROT-079  
**Closes:** MS-PROT-079 Target 4 — Onboarding Engine  
**Purpose:** Complete the production onboarding-engine lifecycle by defining durable merchant-scoped onboarding progress, correction and concurrency semantics, exact final-review affinity, retry-safe submission and the immutable Initial Configuration Intent handoff to MS-PROT-040 without making onboarding, AI or partial progress authoritative Merchant Configuration.

---

## 1. Governing Decision

Main Street SHALL represent ordinary initial merchant onboarding as a durable, merchant-scoped **Onboarding Case**.

The Onboarding Case owns only onboarding progress, merchant-supplied/inferred evidence, answer provenance, correction history, unresolved onboarding needs, review state and submission provenance.

It does not own executable business semantics or authoritative Merchant Configuration.

Canonical flow:

```text
OPEN Merchant Account
+
current authorised Merchant Controller
        ↓
Onboarding Case
        ↓
merchant evidence / structured answers
        ↓
immutable case revisions
        ↓
deterministic candidate recomputation
        ↓
eligible unresolved prompt frontier
        ↓
merchant correction / refinement
        ↓
complete supported intent
        ↓
exact final review
        ↓
immutable Initial Configuration Intent submission
        ↓
MS-PROT-040
        ↓
Configuration Revision
        ↓
deterministic validation / RCP / approval / activation
```

The hard boundary is:

```text
Onboarding Case
    = durable discovery / evidence / proposal authority

Initial Configuration Intent
    = immutable non-executable handoff

Configuration Revision
    = Merchant Configuration authority

Resolved Configuration Package
    = resolved executable configuration authority
```

Onboarding SHALL NOT create active configuration.

---

## 2. Why an Onboarding Case is required

A stateless questionnaire is insufficient because accepted onboarding behaviour already permits interruption, resume, adaptive branch recomputation and merchant correction.

Without a durable owner, two competent implementations could materially disagree about what happens when:

```text
merchant answers A
merchant later changes A
dependent answers already exist
browser closes
another browser edits concurrently
question definition changes
AI becomes unavailable
controller changes
submission response is lost
```

Those behaviours affect authoritative merchant intent and therefore cannot be left to controller/UI implementation convention.

The canonical term SHALL be **Onboarding Case**, not `Onboarding Session`, because `Session` already belongs to authentication/security authority under MS-PROT-063.

---

## 3. Scope and non-goals

This amendment governs ordinary **initial configuration onboarding**.

It does not redefine the prompt catalogue, capability semantics, configuration values, Merchant Profile, Merchant Location, AI authority, authentication sessions, commercial entitlement, configuration activation or frontend experience.

It also does not prescribe:

```text
database technology
table structure
ORM
event broker
HTTP endpoint shape
screen count
progress indicator
animation
copywriting
AI provider
```

Target 5 remains responsible for production Merchant Profile/Location completion. Target 6 remains responsible for the production AI inference boundary. Target 18 remains responsible for concrete background-process architecture.

---

## 4. Canonical Onboarding Case

Conceptually:

```text
OnboardingCase
{
    onboardingCaseIdentity
    merchantIdentifier
    purpose = INITIAL_CONFIGURATION
    lifecycle
    currentRevision
}
```

The physical persistence representation is not prescribed.

The Onboarding Case belongs to the Merchant Account, not to the human identity that happened to begin it.

Therefore:

```text
Controller A starts onboarding
        ↓
Controller transfer commits
        ↓
same Merchant Account
same Onboarding Case
        ↓
Controller B may continue if currently authorised
```

The former Controller's stale session or browser state cannot confer continuing mutation authority.

---

## 5. Case lifecycle

The initial production lifecycle SHALL distinguish:

```text
IN_PROGRESS
SUBMITTED
COMPLETED
ABANDONED
```

`IN_PROGRESS` means onboarding evidence may still be changed.

`SUBMITTED` means one immutable Initial Configuration Intent has been produced from an exact reviewed case revision and control of configuration creation is being handed to MS-PROT-040.

`COMPLETED` means the applicable configuration-bootstrap authority has accepted responsibility for the submitted intent such that further correction belongs to the configuration lifecycle rather than mutation of historical onboarding evidence.

`ABANDONED` means this case will not produce an Initial Configuration Intent.

`COMPLETED` and `ABANDONED` are terminal for that Onboarding Case.

A submitted historical intent SHALL NOT be edited in place.

Where correction is legitimately required before configuration lifecycle ownership has been established, Main Street may establish a successor Onboarding Case referencing the prior case rather than rewriting the historical submission.

---

## 6. One current ordinary initial case

For one Merchant Account, Main Street SHALL NOT permit competing current ordinary initial-onboarding cases.

Conceptually:

```text
merchant M
    → at most one current
      IN_PROGRESS or unresolved SUBMITTED
      INITIAL_CONFIGURATION case
```

Repeated attempts to “start onboarding” while an eligible current case exists SHALL resolve to that case rather than silently creating another independent questionnaire universe.

Once an active initial Merchant Configuration exists, a fresh **initial** Onboarding Case is no longer the applicable path.

Later merchant evolution may reuse MS-PROT-052's prompt machinery, but configuration evolution remains governed by the established configuration lifecycle rather than pretending the merchant is being established for the first time.

---

## 7. Case revisions

Every successful material onboarding mutation SHALL advance an exact logical **Onboarding Case Revision**.

Conceptually:

```text
Case C
Revision 7
    ↓ answer/correction
Revision 8
```

A case revision identifies the exact onboarding-owned evidence state against which the current candidate and unresolved frontier were derived.

A revision does not become executable configuration.

The implementation may reconstruct derived candidate/frontier information rather than physically snapshot every derived value, provided behaviour remains deterministic and the exact effective evidence/provenance of the revision can be recovered.

---

## 8. Answer correction is supersession, not history rewriting

When a merchant changes an earlier answer, Main Street SHALL preserve the prior answer as historical evidence and establish the replacement as the current effective answer.

Rejected:

```text
UPDATE old_answer
SET value = new_value
```

if this destroys the provenance required to explain what changed.

Conceptually:

```text
Q1 v2
answer A
        ↓ merchant correction
answer A becomes superseded
answer B becomes effective
```

The exact storage strategy is implementation detail.

The semantic requirement is preservation of historical provenance.

---

## 9. Correction forces deterministic recomputation

After every material evidence/answer mutation, Main Street SHALL recompute the affected candidate configuration and eligible unresolved prompt frontier.

Therefore:

```text
answer X
    activates branch B
    ↓
merchant changes X
    branch B no longer applicable
    ↓
B removed from current candidate/frontier
```

Answers previously collected exclusively inside branch B SHALL NOT continue influencing the current proposal merely because they remain historically persisted.

They remain evidence history.

They cease to be effective candidate input unless another currently valid dependency independently makes their scope applicable.

This is the production consequence of MS-PROT-052's branch-pruning rule.

---

## 10. `VARIES`, `NOT_SURE` and `OTHER`

Existing MS-PROT-052 semantics survive unchanged.

`VARIES` partitions a decision and does not satisfy it.

`NOT_SURE` leaves a material decision unresolved.

`OTHER` may expose unsupported intent but cannot create semantics.

Therefore an Initial Configuration Intent SHALL NOT claim completeness while an applicable mandatory supported decision remains unresolved through `VARIES`, `NOT_SURE` or equivalent unresolved state.

Unsupported intent may remain recorded as unsupported. It SHALL NOT be silently converted to the nearest supported semantic.

Where Main Street can validly represent only a supported subset of the merchant's wider intent, that limitation must be made explicit during review rather than hidden by inference.

---

## 11. Question-definition evolution

Historical answers SHALL retain the exact question identity and material question-definition version under which they were supplied, as already required by MS-PROT-052.

When a newer definition exists on resume, the engine SHALL NOT silently reinterpret an old answer.

The old answer may remain usable only when the registered contracts establish that its previous interpretation remains valid for the current need.

Otherwise:

```text
historical answer retained
        +
current decision becomes unresolved
        ↓
merchant review / re-answer required
```

Changing wording or localisation alone does not invalidate an answer where semantic interpretation is unchanged.

---

## 12. Semantic Registry Release boundary

The Onboarding Case SHALL NOT choose or pin the Semantic Registry Release of the first Configuration Revision.

MS-PROT-040 v1.2 already owns that boundary: the exact release becomes immutable when the first Configuration Revision is materialised for deterministic validation.

Onboarding may retain semantic-release information as answer/inference **provenance** where required by MS-PROT-052.

But:

```text
answer provenance release
    ≠
Configuration Revision release affinity
```

Onboarding therefore hands MS-PROT-040 stable registered business/configuration intent, not authority saying:

```text
"create this merchant on release R21"
```

unless a future separately accepted configuration-bootstrap authority explicitly establishes such a path.

---

## 13. Deterministic non-AI operation

The Onboarding Case lifecycle SHALL remain fully operable without AI.

AI may interpret text, preselect registered options, rank eligible prompts and explain choices, but cannot directly mutate authoritative state or invent semantics. This is consistent with both MS-PROT-052 and MS-PROT-057.

Therefore:

```text
AI unavailable
        ↓
structured deterministic onboarding continues
```

is a required valid mode.

AI failure is not equivalent to onboarding failure.

---

## 14. Final review identity

Final review SHALL be bound to one exact Onboarding Case Revision.

Conceptually:

```text
Review
{
    onboardingCaseIdentity
    reviewedRevision
}
```

If the case changes after the review was generated:

```text
reviewed revision = 14
current revision = 15
```

the previous review cannot authorise submission of revision 15.

A new review of the new effective state is required.

This prevents stale-tab approval from authorising merchant intent the merchant did not actually review.

---

## 15. Submission completeness

`SubmitInitialConfigurationIntent` may succeed only when the exact reviewed revision establishes all currently applicable conditions required to express the merchant's complete **supported** configuration intent.

At minimum:

```text
all applicable mandatory configuration decisions resolved

all required supported configuration data present

all context-partitioned decisions resolved per applicable context

no unresolved mandatory NOT_SURE state

no unresolved executable VARIES state

no unsupported intent silently represented as supported

review corresponds to current case revision

current account/controller authority remains valid
```

This does not require optional profile enrichment, optional integrations or unsupported future semantics merely to make onboarding look “100% complete.”

MS-PROT-040's rule that the first configuration represents the merchant's complete supported operating model rather than a commercial-tier placeholder remains unchanged.

---

## 16. Initial Configuration Intent submission

Successful submission creates one immutable, stably identifiable Initial Configuration Intent representation derived from the exact reviewed case revision.

Conceptually:

```text
InitialConfigurationIntent
{
    intentIdentity
    merchantIdentifier
    sourceOnboardingCaseIdentity
    sourceOnboardingCaseRevision
    candidateRegisteredIntent
    provenanceReferences
    merchantReviewEvidence
}
```

This is a logical contract, not a prescribed Java class or database table.

The intent SHALL NOT itself:

```text
activate capabilities
create runtime authority
establish entitlement
publish the merchant
choose provider success
become a Configuration Revision
```

Those boundaries remain with their existing owners.

---

## 17. Submission atomicity

Within the onboarding authority, successful submission SHALL atomically establish:

```text
immutable submitted intent
+
exact source case revision affinity
+
merchant review/approval evidence
+
case lifecycle transition
    IN_PROGRESS → SUBMITTED
```

A committed state where the case says `SUBMITTED` but no exact submitted intent can be recovered is forbidden.

Likewise, a submitted intent must not exist without an identifiable source case/revision and review evidence.

Crossing into Merchant Configuration does not require Onboarding and Merchant Configuration to share semantic ownership.

---

## 18. Handoff to MS-PROT-040

After submission, the exact intent is passed to the existing MS-PROT-040 initial-configuration bootstrap operation.

MS-PROT-040 remains solely authoritative for:

```text
Configuration Revision creation
semantic-release affinity
compiler validation
Resolved Configuration Package
impact analysis
exact configuration approval
serving-deployment admission
first activation
```

Onboarding must not duplicate those decisions.

The handoff must tolerate retry and acknowledgement loss.

It must not rely on an assumption of exactly-once network or worker delivery.

The concrete synchronous/background mechanism may be completed under later infrastructure design, but the semantic contract is:

```text
same intentIdentity retried
        ↓
same logical configuration-bootstrap request
        ↓
no duplicated business effect
```

---

## 19. Mutation concurrency

Every onboarding mutation SHALL be conditional on the caller's expected current case revision or an equivalent concurrency predicate.

Example:

```text
browser A reads revision 12
browser B reads revision 12

A writes
    12 → 13

B tries to write against 12
    → CASE_REVISION_CONFLICT
```

Main Street SHALL NOT silently merge two material merchant decisions merely because both requests are technically valid JSON.

The losing client must recover the current state and allow the merchant to reconcile the intended change.

This is a semantic protection against lost merchant intent, not merely a database optimisation.

---

## 20. Idempotency

Every externally retryable material onboarding mutation SHALL have sufficient logical request identity to distinguish retry from a new merchant action.

The same logical request after a lost acknowledgement must resolve to the same committed outcome.

For submission:

```text
submit intent I
commit succeeds
response lost
same submission retried
        ↓
return I
```

It must not produce `I2`.

Reuse of one logical request identity with materially different payload SHALL be rejected rather than interpreted as a retry.

---

## 21. Authentication, authorisation and account lifecycle

Ordinary initial onboarding requires a currently valid trusted execution context and currently authorised Merchant Controller authority for the target Merchant Account.

Merchant Account establishment itself does not start or complete onboarding; MS-PROT-071 explicitly keeps Merchant Account existence separate from onboarding completion and configuration.

Controller transfer preserves the case but changes who may act.

Merchant-wide suspension preserves the case and its evidence but SHALL prevent ordinary onboarding mutation/submission while that suspension denies ordinary privileged merchant mutation.

`CLOSING` or `CLOSED` Merchant Accounts SHALL NOT submit a new ordinary initial operating configuration.

A stale former Controller session cannot bypass these current predicates.

---

## 22. Commercial independence

Onboarding completeness is not a pricing-tier decision.

The engine SHALL discover the merchant's complete supported operating intent without deliberately suppressing semantics because:

```text
merchant is Free
trial has not begun
merchant has not paid
feature may later require entitlement
```

Commercial Entitlement remains a separate runtime/application predicate.

---

## 23. Profile and Location boundary

Onboarding may collect Merchant Profile, Merchant Location, service-area or Business Hours candidate information through the accepted prompt model.

The Onboarding Engine itself does not thereby become owner of those facts.

Until Target 5 closes their production application contract, such captured material is onboarding evidence/candidate data unless and until the owning authority accepts it through its own authoritative operation.

Therefore:

```text
onboarding address answer
    ≠ automatically authoritative Merchant Location
```

unless the accepted Merchant Profile/Location operation explicitly makes it so.

This prevents Target 4 from pre-empting Target 5.

---

## 24. Data protection and audit

Onboarding evidence, free text, contact/profile candidate data and provenance remain subject to accepted data-protection authority from the moment they are stored.

Target 17 may complete remaining lifecycle architecture later; its later sequence position does not suspend already accepted privacy obligations.

Material submission, correction and authority-sensitive changes must remain traceable under existing audit semantics where applicable.

Audit evidence does not become onboarding truth.

---

## 25. Failure semantics

Where caller recovery differs, the application contract must preserve distinctions equivalent to:

```text
AUTHENTICATION_REQUIRED

AUTHORISATION_REJECTION

ACCOUNT_OPERATION_RESTRICTED

INITIAL_ONBOARDING_NOT_APPLICABLE

PROMPT_NOT_CURRENTLY_ELIGIBLE

ANSWER_INVALID_FOR_QUESTION_DEFINITION

CASE_REVISION_CONFLICT

STALE_REVIEW

INCOMPLETE_SUPPORTED_INTENT

DEFINITION_REVIEW_REQUIRED

UNSUPPORTED_INTENT

TECHNICAL_FAILURE_BEFORE_COMMIT

EXECUTION_UNCERTAIN

SUCCESS

ALREADY_APPLIED
```

Exact transport names remain Target-20/API detail.

`EXECUTION_UNCERTAIN` must never be converted into a fresh mutation without reconciliation.

---

## 26. Falsification

The proposed model survives the principal failure cases.

**Browser/process restart:** durable case resumes from current evidence and recalculates the frontier rather than remembering a wizard step. Pass.

**Two tabs answer the same question:** expected-revision concurrency allows at most one mutation from the old revision; the other conflicts. Pass.

**Merchant reverses an earlier high-branch answer:** branch-dependent answers remain historical but stop contributing to the current candidate. Pass.

**Question definition changes:** historical answer retains its old interpretation and is either explicitly reusable or becomes unresolved. No silent reinterpretation. Pass.

**AI provider unavailable:** structured deterministic onboarding remains operational. Pass.

**Controller transfers halfway through onboarding:** case remains attached to the Merchant Account; new Controller may continue, former Controller loses authority. Pass.

**Account becomes suspended:** progress is preserved; prohibited ordinary mutation stops. Pass.

**Submission acknowledgement is lost:** retry returns the same submitted intent. Pass.

**Active configuration appears concurrently:** submission revalidates that ordinary initial onboarding remains applicable; it cannot create a second “initial” authority. Pass.

**Unsupported merchant intent:** remains unsupported or explicitly excluded from represented supported scope; AI cannot fabricate it. Pass.

**Semantic release advances while onboarding:** onboarding does not create competing release affinity; MS-PROT-040 remains authoritative at Configuration Revision materialisation. Pass.

---

## 27. Rejected alternatives

A purely stateless questionnaire is rejected because it cannot authoritatively resolve resume, correction and concurrent-edit behaviour.

A single mutable answer document is rejected because corrections would erase provenance and make downstream branch invalidation ambiguous.

Last-write-wins is rejected because concurrent merchant decisions could silently destroy one another.

Calling the object `OnboardingSession` is rejected because `Session` already has security semantics.

Making AI the Onboarding Case owner is rejected because AI is probabilistic interpretation, not authoritative business state.

Pinning the Configuration Revision's Semantic Registry Release inside onboarding is rejected because MS-PROT-040 already owns that authority.

Creating Configuration Revision records directly from each questionnaire answer is rejected because partial onboarding is explicitly non-authoritative configuration.

Rewriting a submitted intent is rejected because it destroys the historical merchant-intent boundary needed for retry and audit.

---

## 28. Accepted invariants

```text
One current ordinary initial Onboarding Case per Merchant Account.

The case belongs to the Merchant Account, not the initiating Controller.

Partial onboarding state is durable but non-executable.

Every material mutation advances an exact logical case revision.

Concurrent stale mutation is rejected rather than silently merged.

Corrections preserve historical provenance.

Branch pruning removes stale answers from effective candidate influence,
not from history.

Historical answers are never silently reinterpreted under newer material
question definitions.

AI is optional to engine correctness.

Final review binds to one exact case revision.

Any mutation after review invalidates that review for submission.

Submission creates one immutable Initial Configuration Intent.

Onboarding does not pin Configuration Revision semantic-release affinity.

Onboarding does not create Configuration Revision or active configuration.

Submission/handoff is retry-safe and acknowledgement-loss-safe.

Controller transfer preserves case continuity but immediately changes
current execution authority.

Suspension/closure restrictions are revalidated at mutation/submission.

Commercial tier never defines the merchant's supported business model.

Profile/Location candidate collection does not transfer fact ownership.

Raw onboarding history never becomes runtime business authority.
```

---

## 29. Deferred implementation choices

The following remain genuinely downstream:

```text
PostgreSQL table layout
aggregate persistence style
snapshot versus reconstruction optimisation
jOOQ record layout
REST endpoint naming
background worker implementation
message broker
frontend route structure
progress display
autosave timing
localisation implementation
analytics / experimentation
AI model/provider
```

Those choices may vary without changing the authority above.

The canonical Deferred Decision Register assigns stable governance traceability identifiers `MS-PROT-052-V12-DQ-001` through `MS-PROT-052-V12-DQ-013` to these retained choices. The identifiers do not change their implementation/downstream classification.

---

## 30. Acceptance Statement

The governing production principle is:

> **Onboarding is a durable merchant-scoped discovery process, not a wizard and not a partial configuration. Main Street stores exact, versioned onboarding evidence in a revision-controlled Onboarding Case, deterministically recomputes the unresolved graph after every material change, requires explicit review of the exact final case state, and hands one immutable Initial Configuration Intent to the Configuration lifecycle. Corrections preserve history; retries do not duplicate intent; AI assists but never owns the result.**

Canonical boundary:

```text
Onboarding Case
        ↓
merchant evidence
        ↓
adaptive deterministic resolution
        ↓
reviewed candidate intent
        ↓
immutable Initial Configuration Intent
        ↓
MS-PROT-040
        ↓
Configuration Revision
        ↓
validation / approval / deployment admission / activation
```

**Review:** PASS  
**Falsification:** PASS  
**Ambiguity review:** PASS  
**Manual approval:** GRANTED on 27 August 2026  
**Status:** **ACCEPTED**
