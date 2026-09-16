# MS-PROT-027 v1.12 — Opaque Evaluator Submission Affinity Amendment

**Document ID:** MS-PROT-027  
**Version:** 1.12  
**Status:** **ACCEPTED by explicit manual approval on 2 September 2026**  
**Approved:** 2 September 2026  
**Authority type:** Production Exposure evaluator/result-affinity architecture amendment  
**Governed by:** MS-DESIGN-RULES-001; MS-IMPLEMENTATION-RULES-001; MS-IMP-001  
**Amends:** composite MS-PROT-027 through v1.11 only within evaluator candidate-submission identity, evaluator batch-result affinity and mechanical cross-request/cross-invocation rejection  
**Depends on:** MS-PROT-027 v1.11 and its dependencies  
**Closes:** `IMP-06-E4-DG-002 — Evaluator Result Submission-Affinity Gap`  
**Leaves unchanged:** S2/T1b4 bounded-read representation-affinity gate  
**Purpose:** Make the v1.11 exact batch-result and cross-request/cross-invocation structural-failure rules mechanically enforceable without contaminating stable candidate identity, exposing private observation bindings to owner evaluators, or expanding E4 into business-state authority.

---

## 1. Governing decision

Production E4 SHALL introduce an ephemeral opaque candidate-evaluation binding for every candidate submitted to every logical evaluator batch invocation.

Canonical:

```text
structurally valid candidate
        +
exact evaluator semantic reference
        +
current bounded E4 resolution
        ↓
fresh E4-owned candidate-evaluation binding
        ↓
opaque candidate evaluation submission
        ↓
owner evaluator
        ↓
binding + evaluator decision
        ↓
E4 exact submission-affinity validation
        ↓
candidate decision
```

The binding exists only to establish exact evaluator-result affinity.

It SHALL NOT become business identity, semantic identity, authorisation, business evidence, result membership or data-acquisition authority.

---

## 2. Problem resolved

MS-PROT-027 v1.11 requires structural rejection of:

```text
cross-request evaluator result
cross-invocation evaluator result
```

Stable candidate identity alone cannot establish that provenance because the same legitimate candidate may participate in multiple bounded observation requests or evaluator invocations.

Therefore:

```text
ExposureCandidateObservation equality
```

SHALL NOT be treated as proof that a returned evaluator decision belongs to the current logical evaluator invocation.

---

## 3. Identity-role extension

The existing v1.11 identity-role invariant is extended with one additional execution-only role:

```text
ExposureCandidateEvaluationBinding
    → opaque identity of one exact candidate submission
      to one exact logical evaluator batch invocation
```

It is distinct from:

```text
ExposableElementReference
ExposureCandidateInstanceReference
domain revision identity
ExposureElementContractIdentity
merchant-choice revision identity
ObservationRequest binding
Audience Observation invocation binding
ExposedElementMembership
```

In particular:

```text
candidate evaluation binding
    ≠ stable candidate identity

candidate evaluation binding
    ≠ observation request identity

candidate evaluation binding
    ≠ audience-admission invocation identity

candidate evaluation binding
    ≠ result membership
```

---

## 4. Candidate evaluation binding

Conceptually:

```text
ExposureCandidateEvaluationBinding
```

is an opaque E4-owned runtime identity.

A valid binding SHALL be:

1. created by E4;
2. fresh for one exact candidate submission;
3. valid for exactly one logical evaluator batch invocation;
4. non-reused across other candidate submissions;
5. non-reused across evaluator semantic references;
6. non-reused between requirement and merchant-choice evaluator calls;
7. non-reused across bounded E4 resolutions;
8. non-reused across Observation Requests;
9. non-reused across Audience Observation admission invocations; and
10. non-reused across Semantic Registry Releases.

The exact runtime mechanism used to establish freshness is implementation detail.

No particular UUID, counter, database sequence, cryptographic token or object-identity mechanism is prescribed.

---

## 5. Private affinity maintained by E4

For one current logical evaluator batch invocation, E4 SHALL privately associate each issued binding with the exact submitted candidate and the current execution boundary.

That boundary is already governed by:

```text
exact Observation Request
exact Audience Observation admission invocation
exact Semantic Registry Release
exact evaluator kind
exact owner-qualified evaluator semantic reference
exact submitted candidate observation
```

These affinity facts SHALL remain E4-private integrity information.

The public opaque binding does not need to expose or serialise those facts.

---

## 6. Binding opacity

Owner evaluators MAY receive and echo `ExposureCandidateEvaluationBinding`.

They SHALL NOT receive an API that reveals from that binding:

```text
raw ObservationRequestBinding
raw AudienceObservationInvocationBinding
release internals
session/token/credential
request-world-state
authorisation state
business payload
```

Valid binding construction SHALL remain controlled by E4.

An owner evaluator SHALL NOT mint a valid binding for a submission that E4 did not issue.

---

## 7. Candidate evaluation submission

Evaluator input SHALL use an immutable conceptual submission:

```text
ExposureCandidateEvaluationSubmission
{
    ExposureCandidateEvaluationBinding evaluationBinding
    ExposureCandidateObservation candidate
}
```

The submission wrapper does not change candidate membership semantics.

The candidate remains the exact v1.11 identity-only candidate observation.

The evaluation binding is execution provenance only.

---

## 8. Binding scope

A new binding SHALL be issued for every:

```text
candidate
×
logical evaluator batch invocation
```

Therefore, if candidate `C1` participates in:

```text
requirement R1
requirement R2
merchant choice M1
```

then it receives three distinct evaluation bindings.

A binding from R1 is invalid in R2 or M1 even though the stable candidate is identical.

---

## 9. Requirement evaluator contract

The requirement evaluator remains batch-capable and read-only.

Its conceptual boundary becomes:

```text
evaluateBatch(
    exact ExposureRequirementReference,
    OwnerExposureEvaluationContext,
    immutable List<ExposureCandidateEvaluationSubmission>
)
```

This change SHALL NOT alter:

```text
SATISFIED
UNSATISFIED
UNRESOLVED
```

semantics.

---

## 10. Merchant-choice evaluator contract

The merchant-choice evaluator remains batch-capable and read-only.

Its conceptual boundary becomes:

```text
evaluateBatch(
    exact MerchantExposureChoiceSourceReference,
    OwnerExposureEvaluationContext,
    immutable List<ExposureCandidateEvaluationSubmission>
)
```

This change SHALL NOT alter:

```text
EXPOSE
WITHHOLD
UNRESOLVED
```

semantics.

---

## 11. Requirement result row

A requirement result row SHALL conceptually become:

```text
ExposureRequirementCandidateEvaluation
{
    ExposureCandidateEvaluationBinding evaluationBinding
    ExposureRequirementEvaluationDecision decision
}
```

The result SHALL NOT independently repeat `ExposureCandidateObservation`.

E4 resolves the binding back to the exact current submitted candidate.

This prevents two independent result-row identities from disagreeing.

---

## 12. Merchant-choice result row

A merchant-choice result row SHALL conceptually become:

```text
MerchantExposureChoiceCandidateEvaluation
{
    ExposureCandidateEvaluationBinding evaluationBinding
    MerchantExposureChoiceEvaluationDecision decision
}
```

The result SHALL NOT independently repeat `ExposureCandidateObservation`.

E4 resolves the binding back to the exact current submitted candidate.

---

## 13. Batch envelopes

The existing exact semantic-reference binding on each batch response remains required.

Conceptually:

```text
ExposureRequirementBatchEvaluation
{
    exact requirement reference
    immutable result rows
}

MerchantExposureChoiceBatchEvaluation
{
    exact merchant-choice source reference
    immutable result rows
}
```

A separate batch-level execution token is not required.

Per-submission bindings provide the stronger candidate-row affinity.

---

## 14. Exact result coverage

For each logical evaluator batch invocation:

```text
issued submission bindings
```

and:

```text
returned result bindings
```

SHALL match exactly.

E4 SHALL validate this before accepting any evaluator decision.

Structural failure includes:

```text
missing submitted binding
foreign/unissued binding
duplicate returned binding
binding issued for another evaluator semantic reference
binding issued for another evaluator kind
binding issued for another bounded E4 resolution
binding issued for another Observation Request
binding issued for another Audience Observation admission invocation
binding issued for another Semantic Registry Release
wrong batch semantic evaluator reference
```

Result ordering carries no semantic meaning.

---

## 15. Cross-request rejection

If the same stable candidate participates in request R1 and request R2:

```text
R1/C1 → binding B1
R2/C1 → binding B2
```

then:

```text
B1 returned while evaluating R2
    → structural failure
```

even though both submissions refer to the same stable candidate identity.

Stable candidate equality SHALL NOT make B1 valid in R2.

---

## 16. Cross-invocation rejection

If two Audience Observation admission invocations occur for the same Observation Request:

```text
I1/C1 → binding B1
I2/C1 → binding B2
```

then:

```text
B1 returned while evaluating I2
    → structural failure
```

The same rule applies between separate logical evaluator calls inside one bounded E4 resolution.

---

## 17. Requirement and choice separation

A binding issued for requirement evaluation SHALL NOT be valid for merchant-choice evaluation.

A binding issued for merchant-choice evaluation SHALL NOT be valid for requirement evaluation.

A binding issued for one exact requirement or choice semantic reference SHALL NOT be valid for another semantic reference.

---

## 18. Existing decision precedence remains unchanged

This amendment does not change v1.11 deterministic precedence:

```text
request/release/admission integrity
        ↓
element definition
        ↓
membership identity
        ↓
current audience variant
        ↓
requirements
        ↓
merchant choice when declared
        ↓
otherwise baseline
```

The new binding validates evaluator-result provenance only.

It does not create another policy layer.

---

## 19. Missing evaluator semantics remain unchanged

Where a structurally coherent exact-release runtime binding lacks the required evaluator:

```text
candidate → WITHHOLD
```

as governed by v1.11.

E4 need not create a candidate-evaluation binding for an evaluator that will not be invoked.

---

## 20. Expected unresolved semantics remain unchanged

Expected inability of an owner evaluator to establish current evidence remains:

```text
UNRESOLVED
    → candidate WITHHOLD
```

A current valid evaluation binding does not convert unresolved evidence into structural failure.

---

## 21. Unexpected evaluator failure remains structural

Unexpected evaluator exceptions, malformed batch envelopes and invalid submission-affinity results remain whole-resolution structural failures.

They SHALL NOT become candidate-level `WITHHOLD`.

---

## 22. OwnerExposureEvaluationContext remains unchanged

This amendment SHALL NOT add:

```text
ObservationRequestBinding
AudienceObservationInvocationBinding
candidate-evaluation binding collection
raw session
credential
release internals
```

to `OwnerExposureEvaluationContext`.

Candidate evaluation bindings travel only with candidate submissions/results.

This prevents execution-affinity machinery from becoming shared owner context.

---

## 23. Candidate identity remains unchanged

`ExposureCandidateObservation` remains the v1.11 identity-only candidate.

It SHALL NOT acquire:

```text
request identity
invocation identity
evaluation-binding identity as stable state
release identity
decision
payload
```

as a consequence of this amendment.

---

## 24. Positive membership remains unchanged

`ExposedElementMembership` remains:

```text
ExposableElementReference
+
optional stable member instance reference
```

Candidate evaluation bindings SHALL NOT enter successful Exposure membership.

---

## 25. No authority grant

Possession of an `ExposureCandidateEvaluationBinding` grants no authority to:

```text
read owner state
reacquire payload
authorise an actor
bypass Projection Serviceability
bypass Exposure requirements
change merchant choice
perform mutation
invoke a provider
```

It is correlation/integrity evidence only.

---

## 26. No business-evidence attestation

The binding proves:

> the result row is associated with a current E4-issued candidate submission.

It does not prove:

> the evaluator's substantive decision is factually correct.

Owner evaluators remain trusted read/evaluation authorities responsible for correct interpretation of their current owner evidence.

An evaluator that deliberately associates a current valid binding with an incorrect decision violates its evaluator contract.

This amendment does not introduce cryptographic attestation of owner business truth.

---

## 27. Runtime-only lifetime

Candidate evaluation bindings SHALL be ephemeral.

They SHALL NOT become:

```text
authoritative persistence
audit business identity
semantic bundle definition
Semantic Registry Release definition
merchant configuration
external API identifier
continuation token
public URL identifier
domain event identity
```

Normal transient diagnostic correlation MAY use a non-sensitive representation where existing observability rules permit it, but such representation is not business authority.

---

## 28. Batch behaviour remains generic

The amendment SHALL NOT require one evaluator invocation per candidate.

Candidates sharing one exact evaluator binding continue to be processed in one logical batch.

The wrapper/binding mechanism only makes each returned row mechanically attributable to its exact submission.

---

## 29. Owner extensibility remains generic

Adding a new evaluator owner SHALL require:

```text
owner evaluator
+
exact-release binding
+
owner tests
```

It SHALL NOT require an owner-specific branch in the generic E4 submission-affinity algorithm.

If a new owner requires changes to generic affinity semantics merely because of its business type, architecture review MUST reopen this authority.

---

## 30. Implementation mechanism boundary

A conforming modular-monolith implementation MAY use a Surface-owned sealed opaque runtime type whose concrete construction is unavailable to owner evaluators.

The normative requirements are:

```text
freshness
exact per-submission identity
non-reuse
opacity
E4-owned issuance
exact current-call validation
```

The normative design does not prescribe UUIDs, hashes, database rows, cryptographic signatures or distributed token protocols.

---

## 31. Future distributed evaluator boundary

This amendment does not introduce a distributed evaluator protocol.

If owner evaluators later cross a process/service boundary, the representation and validation of submission affinity MUST be reviewed under the then-current architecture.

The current in-process opaque mechanism SHALL NOT be prematurely expanded into distributed infrastructure.

---

## 32. Required executable falsification

Conforming implementation SHALL cover at least:

```text
same stable candidate across two Observation Requests
    → stale first-request binding rejected

same request across two admission invocations
    → stale first-invocation binding rejected

same candidate across two requirement references
    → cross-reference binding rejected

requirement binding returned to merchant-choice evaluation
    → rejected

missing returned binding
    → structural failure

foreign binding
    → structural failure

duplicate binding
    → structural failure

reordered complete current binding set
    → accepted

multiple instances of one semantic element
    → independently mapped

singleton optional evaluator instance
    → does not enter membership

missing evaluator
    → candidate WITHHOLD

UNRESOLVED
    → candidate WITHHOLD

unexpected evaluator exception
    → structural failure

no current audience variant
    → WITHHOLD without evaluator invocation

exact-release mismatch
    → structural failure
```

Architecture/conformance tests SHALL additionally establish that:

```text
OwnerExposureEvaluationContext does not gain raw request/invocation affinity
candidate evaluation binding does not enter stable candidate identity
candidate evaluation binding does not enter positive membership
generic E4 gains no owner/business-specific dependencies
binding is not semantic-bundle or persistence authority
```

---

## 33. Existing v1.11 structural semantics remain authoritative

Except for the mechanical result-affinity contract introduced here, MS-PROT-027 v1.11 remains authoritative.

This amendment does not weaken or broaden:

```text
candidate legitimacy
audience admission
member identity
exact-release registry resolution
requirement semantics
merchant-choice semantics
baseline semantics
owner context
read-only evaluator invariant
whole-resolution structural failure
positive membership
E4-to-representation separation
```

---

## 34. Downstream boundary remains unchanged

S2/T1b4 bounded-read representation affinity remains a separate downstream gate.

A valid candidate evaluation binding SHALL NOT become authority to reacquire owner state after Exposure.

This amendment therefore closes only evaluator-result submission affinity.

It does not solve or redefine downstream representation-value affinity.
