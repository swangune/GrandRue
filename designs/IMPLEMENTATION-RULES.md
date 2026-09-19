# GrandRue Implementation Rules

**Document ID:** MS-IMPLEMENTATION-RULES-001  
**Version:** 2.1  
**Status:** Accepted  
**Approved:** 19 September 2026 — explicit manual approval to formalise the complete v2.1 source-rooted defect-correction and import-clarity proposal  
**Applies from:** 19 September 2026  
**Last amended:** 19 September 2026  
**Purpose:** Define the mandatory rules for implementing accepted GrandRue design in production code and tests, including MS-IMP-001 integration, dependency-complete behavioural-slice execution, test integrity, proportional checkpoint verification, full node-completion verification, composite architecture/programming-paradigm conformance, evidence integrity, exact design-to-code traceability, the canonical `/IMPLEMENTATION.md` live controller, explicit branch-creation authorisation, DESIGN-RULES-governed escalation, terminal post-migration handoff, source-rooted defect correction, import clarity, and context-efficient authority loading.

---

## 1. Governing Principle

Implementation realises accepted GrandRue design. It does not silently redefine it.

```text
Accepted design authority
        ↓
Implementation target
        ↓
Executable tests
        ↓
Minimum production code
        ↓
Verification
        ↓
Behaviour-preserving refactoring
        ↓
Full conformance
        ↓
Traceable commit
```

Production code, tests, tooling and refactoring MUST remain subordinate to accepted semantic and architectural authority.

Where implementation reveals missing, contradictory or improvable semantics, implementation MUST stop at the applicable approval boundary rather than inventing a design decision.

---

## 2. Automated Implementation Lifecycle

Implementation SHOULD proceed automatically whenever all required decisions are already governed by accepted authority.

MS-IMP-001 or another accepted implementation programme determines eligible macro work and the fine-grained graph determines READY nodes. Within one READY or IN_PROGRESS node, execution proceeds through one or more dependency-complete behavioural slices.

```text
READ ACCEPTED AUTHORITY
        ↓
RESOLVE ELIGIBLE MACRO / READY NODE
        ↓
PREPARE NODE BOUNDARY ONCE
        ↓
SELECT DEPENDENCY-COMPLETE BEHAVIOURAL SLICE
        ↓
DERIVE / UPDATE FAILING TESTS
        ↓
RUN TARGETED RED WHERE APPLICABLE
        ↓
IMPLEMENT MINIMUM CONFORMING SLICE
        ↓
RUN TARGETED / RELEVANT CONTRACT / INTEGRATION / CONFORMANCE CHECKS
        ↓
FALSIFY SLICE
        ↓
COMMIT DURABLE SLICE CHECKPOINT
        ↓
MORE SLICE WORK REQUIRED FOR NODE?
        ├── YES → prepare/reuse next closed slice
        └── NO
              ↓
          RUN FULL APPLICABLE NODE-COMPLETION GATE
              ↓
          RECORD MATERIAL COMPLETION EVIDENCE
              ↓
          REFRESH GRAPH / MACRO STATE WHERE CHANGED
              ↓
          SYNCHRONISE /IMPLEMENTATION.md
              ↓
          VERIFY CONTROLLER / GRAPH / EVIDENCE CONSISTENCY
              ↓
          COMMIT CYCLE-CLOSING STATE
              ↓
          NEXT ELIGIBLE WORK
```

Fine-grained traceability does not require file-by-file reasoning or commit cycles.

A full repository verification gate is required for node completion where applicable, but is not automatically required after every internal behavioural-slice checkpoint. Earlier full-gate execution remains mandatory where the blast radius cannot be bounded reliably.

This lifecycle MAY continue without manual approval while every change remains within already accepted semantics and the auto-change boundaries defined here.

### 2.1 Branch-Creation Authorisation Rule

Implementation automation MUST use the currently active/instructed repository branch unless the user explicitly instructs branch creation in the active chat.

The implementation agent, engineer or automated tool MUST NOT autonomously create any Git branch, including a feature branch, temporary branch, scratch branch, experiment branch, recovery branch or other alternate ref intended to function as a branch.

Branch creation is authorised only by a chat instruction whose scope explicitly includes creating a branch. General instructions such as `proceed`, `continue`, `implement`, `proceed automatically`, permission to commit, automatic READY-node execution, or authority to perform AUTO_* work MUST NOT be interpreted as branch-creation permission.

```text
NO EXPLICIT CHAT INSTRUCTION TO CREATE A BRANCH
        ↓
DO NOT CREATE A BRANCH
        ↓
continue governed work on the current instructed branch
```

If implementation cannot safely proceed without creating a branch, the agent MUST leave the branch uncreated and report the constraint rather than creating a convenience, temporary or protective branch on its own initiative.

This is an operational repository-control rule. It does not alter semantic/design approval requirements and does not prevent read-only inspection of existing branches.

---

## 3. Design-Governed Manual-Approval Stop Condition

Automation MUST stop the affected implementation work when implementation
encounters or proposes a material decision not already governed by accepted
authority.

The affected decision MUST enter the complete lifecycle required by the
current accepted `designs/DESIGN-RULES.md`. IMPLEMENTATION-RULES MUST NOT
compress, replace or bypass that lifecycle.

```text
Implementation evidence or proposed improvement
        ↓
Material semantic / architectural / contract implication?
        │
        ├── NO → continue the governed implementation loop
        │
        └── YES
              ↓
          STOP AFFECTED IMPLEMENTATION
              ↓
          classify the design gap
              ↓
          apply current DESIGN-RULES.md
              ↓
          DESIGN / PROPOSE IN CHATGPT
              ↓
          REVIEW
              ↓
          FALSIFICATION
              ↓
          AMBIGUITY REVIEW
              ↓
          RECOMMEND exactly one:
              ACCEPT | REVISE | REJECT | DEFER
              ↓
          if REVISE / REJECT / DEFER
              → do not request approval
              → revise, reject or defer the proposal
              ↓
          if ACCEPT
              → present the complete final proposed authority in ChatGPT
              → present the manual-approval recommendation packet
              → request explicit manual approval
              ↓
          explicit approval attributable to the complete authority?
              │
              ├── NO / UNCLEAR
              │     → remain chat-only
              │     → do not formalise
              │
              └── YES
                    → formalise the approved authority
                    → update required governance navigation
                    → perform corpus conformance
                    → resume implementation when READY
```

Only the affected work stops. Independent implementation nodes governed by
accepted authority may continue.

Implementation agents, engineers and automated tools MUST NOT cross this
boundary merely because a proposed change appears objectively better.

### 3.1 Manual-Approval Recommendation Packet

Before requesting manual approval, the implementation agent MUST present one
self-contained recommendation packet containing:

1. the exact implementation-discovered decision or gap;
2. the affected implementation node and blocked dependencies;
3. the accepted authorities and current `DESIGN-RULES.md` provisions applied;
4. the complete final proposed authority intended for repository
   formalisation;
5. the design-review and corpus-conformance findings;
6. the falsification cases, outcomes and unresolved counterexamples;
7. the mandatory ambiguity-review result;
8. the alternatives, trade-offs, accepted consequences and residual risks;
9. exactly one recommendation:
   `ACCEPT`, `REVISE`, `REJECT` or `DEFER`;
10. the evidence supporting that recommendation;
11. the exact authority and navigation files that approval would permit the
    implementation agent to change; and
12. the explicit approval scope.

The packet MUST state:

```text
RECOMMENDATION: ACCEPT
        ≠
MANUAL APPROVAL: GRANTED
        ≠
STATUS: ACCEPTED
```

Manual approval may be requested only when the recommendation is `ACCEPT` and
every applicable `DESIGN-RULES.md` gate has passed.

A recommendation of `REVISE`, `REJECT` or `DEFER` blocks an approval
request.

### 3.2 Approval-Scope Validation

Manual approval MUST be attributable to the complete final proposed authority
presented in the recommendation packet.

Approval of any of the following MUST NOT be interpreted as approval of the
complete authority:

- a mitigation;
- an alternative;
- a trade-off;
- an individual principle;
- a falsification response;
- a recommendation;
- a subset of the proposed rules; or
- permission merely to continue analysis.

Where multiple objects or approval scopes are active, words such as
`accepted`, `agreed`, `proceed`, `continue` or equivalent MUST NOT be
treated as complete-authority approval unless their scope is explicit and
unambiguous.

If approval scope is uncertain, the proposal remains unapproved and repository
formalisation is prohibited.

The agent MUST identify what was approved and what remains unapproved before
taking any repository action.

---

## 4. Changes Allowed Without Manual Approval

The following MAY be performed automatically when they do not alter accepted semantics, contracts or externally observable behaviour:

- fixing compilation errors caused by implementation defects;
- implementing missing behaviour explicitly required by accepted design;
- correcting production-code defects exposed by valid tests;
- adding tests for already accepted invariants;
- strengthening assertions where the accepted contract is already explicit;
- removing code duplication without changing behaviour or ownership;
- renaming local/private implementation details where semantic meaning is preserved;
- extracting helpers that preserve module/capability boundaries;
- simplifying control flow without changing outcomes;
- replacing accidental mutable implementation detail with equivalent immutable representation;
- improving null/error handling already required by accepted contract;
- fixing deterministic formatting, linting and static-analysis failures;
- correcting dependency violations where accepted architecture determines direction;
- improving test setup/fixtures/builders without weakening meaning;
- optimising implementation details while semantics and consistency remain unchanged;
- adding safe diagnostic logging/metrics that do not alter control flow.

These automatic change permissions do not authorise branch creation. Section 2.1 governs branch creation independently.

---

## 5. Changes Requiring Manual Approval

Manual approval is required unless current accepted authority already explicitly requires the exact change for:

- adding/redefining semantic concepts or invariants;
- changing capability ownership or operation meaning;
- introducing a new capability or semantic cross-capability relationship;
- materially changing internal/public contracts;
- changing transaction, concurrency or idempotency semantics;
- changing event meaning, ordering guarantees or ownership;
- changing persistence semantics affecting history, consistency, lifecycle or recovery;
- introducing/changing provider responsibility or provider dependency;
- changing provider-neutral semantics to match one provider;
- changing merchant/customer-visible behaviour not already authorised;
- changing entitlement, configuration/compiler or AI authority;
- changing authentication, authorisation, trust or exposure policy;
- introducing/replacing an architectural paradigm or deployment boundary;
- splitting the modular monolith into distributed services;
- introducing queues/brokers/asynchronous workflows without accepted need;
- materially changing public API, legal/privacy/retention behaviour;
- weakening tests or deleting invariants for implementation convenience;
- adopting dependencies that materially alter architecture, operations, licensing, security, provider strategy or replacement cost;
- any improvement whose benefit depends on changing accepted design.

---

## 6. Design Ambiguity Escalation Rule

If implementation requires a decision the accepted corpus does not determine, implementation MUST NOT select the most convenient or conventional interpretation.

```text
AMBIGUOUS_AUTHORITY
CONTRADICTORY_AUTHORITY
UNDERSPECIFIED_SEMANTICS
MISSING_INVARIANT
MISSING_CONTRACT
MISSING_FAILURE_BEHAVIOUR
MISSING_OWNERSHIP
MISSING_CONSISTENCY_RULE
MISSING_ARCHITECTURAL_FIT
```

Affected work pauses and enters the governed design-decision lifecycle. Independent work MAY continue.

Every escalation under this section MUST follow Sections 3 through 3.2 and the
current accepted `DESIGN-RULES.md`. Discovery of a design gap does not itself
authorise a recommendation, approval request or repository amendment.

---

## 7. Test-First Rule

For new behaviour or defect correction, tests SHOULD be written/adjusted before production code wherever behaviour can be specified from accepted authority.

```text
accepted invariant
      ↓
failing executable test
      ↓
minimum implementation
      ↓
passing test
      ↓
refactor
      ↓
full suite
```

A test is evidence of an accepted contract, not semantic authority by itself.

---

## 8. Test Integrity Rule

A failing test MUST NOT be changed merely to make the build green.

A test MAY change automatically only when it demonstrably misrepresents current authority, contains an implementation/fixture mistake, asserts an accidental implementation detail, is superseded by stronger accepted coverage, or requires behaviour-preserving structural refactoring.

Tests MUST NOT be weakened through vague assertions, deletion of difficult failure cases, unauthorised input broadening, accepting multiple outcomes where determinism is required, sleeps/retries that hide races, or mocks that remove the behaviour under test.

---

## 9. Test Classification

Tests SHOULD be organised by responsibility:

- **Semantic / Unit:** capability invariants, values, policies, deterministic calculations and transitions.
- **Application:** use-case orchestration, actor/context handling and capability interaction without redefining capability semantics.
- **Contract:** internal capability, API and provider-adapter contracts including failure/correlation/idempotency semantics.
- **Persistence / Integration:** mapping, transactions, constraints, concurrency and repository semantics.
- **End-to-End:** whole-path validation only where materially required; not a substitute for deterministic lower-level tests.
- **Architecture / Conformance:** module/package boundaries, forbidden dependencies, governance artefacts and mechanically testable architectural constraints.

---

## 10. Minimum Invariant Test Set

Where applicable, material invariants SHOULD cover:

```text
VALID / HAPPY PATH
BOUNDARY CASE
INVALID INPUT / REJECTION
STATE CONFLICT
AUTHORITY / PERMISSION FAILURE
DUPLICATE / RETRY CASE
CONCURRENCY CASE
HISTORICAL-AFFINITY CASE
PROVIDER FAILURE CASE
ENTITLEMENT / ACCESS CASE
ARCHITECTURAL BOUNDARY CASE
```

Irrelevant categories MUST NOT be manufactured for numerical completeness.

---

## 11. Production-Code Responsibility Rules

```text
Domain / Capability code
    → business meaning and invariants
Application code
    → orchestration and use cases
Infrastructure / adapters
    → external systems, persistence and providers
Transport / controllers
    → protocol translation and I/O mapping
Projection code
    → derived read representation
Configuration/compiler
    → deterministic semantic resolution
```

A layer MUST NOT absorb another layer's semantic responsibility merely because doing so reduces local code.

---

## 12. Domain-Code Rule

Capability/domain code MUST own authoritative business invariants assigned to that capability and SHOULD avoid direct dependency on transport types, database details, provider SDK objects, UI models, plan identifiers where entitlement suffices, AI responses and transport retry semantics.

Domain objects MUST NOT depend on a concrete provider to define business meaning.

---

## 13. Application-Orchestration Rule

Application services MAY coordinate capabilities but MUST NOT become a second owner of their invariants.

```text
establish principal/context
        ↓
resolve applicability / entitlement / authority
        ↓
collect/validate required inputs
        ↓
invoke capability-owned operations
        ↓
coordinate transaction boundary where required
        ↓
commit
        ↓
trigger post-commit reactions
```

Cross-capability use cases MUST use accepted internal contracts rather than directly mutate another capability's persistence representation.

---

## 14. Infrastructure and Adapter Rule

Infrastructure implements ports and technical fulfilment. Adapters MUST translate provider-specific representations into GrandRue contracts and vice versa. Provider SDK types SHOULD NOT leak into capability-domain APIs.

Provider callbacks MUST be validated, correlated and idempotently reconciled before authoritative consequences. Provider failure MUST follow accepted failure semantics.

---

## 15. Persistence Rule

Persistence stores authoritative state; it does not invent business policy. It MUST preserve capability ownership, identity constraints, historical truth where required, transactional consistency, concurrency semantics, data protection and migration compatibility.

Database constraints MAY reinforce domain invariants but MUST NOT be their only undocumented source.

---

## 16. Transaction Rule

Operations requiring atomic invariants MUST execute within an accepted consistency boundary. Required atomicity MUST NOT be replaced with asynchronous events for convenience. Transaction boundaries must be no broader than necessary and no narrower than correctness permits.

---

## 17. Idempotency and Duplicate-Action Rule

Repeated requests/callbacks/commands MUST follow accepted idempotency semantics. Technical retries MUST NOT create duplicate authoritative effects. Logical retry identity and human duplicate intent remain distinct.

---

## 18. Concurrency Rule

Shared authoritative state MUST consider concurrent mutation. Tests MUST exercise materially relevant races such as competing bookings, stock claims, lifecycle transitions, provider callbacks and configuration revisions. The mechanism may vary but MUST preserve accepted invariants.

---

## 19. Event Rule

Events represent committed facts or other explicitly accepted event semantics. They SHOULD follow secured authoritative mutation. Handlers MUST NOT own originating capability state and SHOULD be idempotent where redelivery is possible. Failed non-authoritative reactions MUST NOT silently roll back committed facts absent an accepted compensation process.

---

## 20. Error and Failure Rule

Materially different failures MUST remain distinguishable where callers require different handling. Exact implementation types are downstream unless governed by a contract. Errors MUST NOT expose secrets or sensitive internal/provider details.

---

## 21. Security and Authority Rule

Accepted actor authority MUST be enforced at the appropriate runtime boundary. UI hiding is insufficient. Merchant scope MUST be resolved before merchant-scoped mutation. Material operations require negative-authority tests. Broadening read/mutation authority requires approval unless already authorised.

---

## 22. Data Protection Rule

Production/test code MUST follow accepted classification, retention, minimisation and exposure rules. Tests SHOULD use synthetic data. Real secrets/credentials/personal data MUST NOT be required or committed. Logging MUST minimise protected data.

---

## 23. AI Implementation Rule

AI may interpret, propose and assist only within accepted authority. Deterministic authority boundaries MUST remain. AI output MUST NOT directly mutate authoritative configuration/state where approval or deterministic validation is required. Probabilistic tests MUST NOT weaken deterministic core tests.

---

## 24. Subscription / Entitlement Implementation Rule

Commercial entitlement MUST remain distinct from capability semantics and merchant configuration. Configuration MUST NOT be removed merely because entitlement is lost. Existing commitments/residual access follow accepted authority. Plan identifiers MUST NOT substitute for semantic applicability.

---

## 25. Projection and Exposure Implementation Rule

Read projections MUST derive from authoritative truth and accepted Exposure rules. Caches/read models MUST NOT become mutation authority. Stale projection state MUST NOT override authoritative execution checks.

---

## 26. Dependency Adoption Rule

Dependencies must be classified as local implementation detail or architectural/provider decision. Local libraries MAY be adopted automatically only when they do not materially alter ownership, deployment, security, provider strategy, licensing, cost or semantics. Material adoption requires manual approval.

---

## 27. Refactoring Rule

Automatic refactoring must be behaviour-preserving and MUST NOT move semantic ownership, materially alter contracts, remove validation, alter authority, transaction outcomes, event meaning, historical interpretation or merchant/customer behaviour.

---

## 28. Minimum-Change Rule

Prefer the smallest implementation satisfying accepted contracts while preserving capability boundaries, testability, maintainability and known accepted requirements. Do not build speculative abstractions.

`Smallest implementation` means the minimum complete implementation required by the selected dependency-complete behavioural slice. It MUST NOT be interpreted as requiring the smallest possible file, class or method edit.

---

## 29. Naming Rule

Production/test identifiers SHOULD use canonical semantic vocabulary and qualifiers where bare terms are ambiguous. Public/persisted renames with migration/contract impact require the applicable approval process.

Production and test source code SHOULD use explicit imports by default because explicit imports normally provide clearer symbol provenance, dependency ownership, reviewability and refactoring safety.

Wildcard imports (`*`), including static wildcard imports, MAY be used only where they expose one cohesive namespace or vocabulary, materially improve readability or maintainability over the equivalent explicit-import set, and do not obscure dependency provenance, semantic ownership or symbol identity.

Wildcard imports MUST NOT be introduced merely to reduce import count, satisfy an IDE/tool threshold, conceal an unresolved or missing dependency, avoid identifying the correct source type, or shortcut a defect correction. If an actual or reasonably foreseeable ambiguity exists, or provenance becomes materially less clear, explicit imports MUST be used.

Static wildcard imports are subject to the same rule and SHOULD be used more conservatively where unqualified members could obscure their declaring owner. Tooling and IDE configuration SHOULD preserve explicit imports as the default and MUST NOT collapse explicit imports into wildcards solely because an arbitrary numeric import threshold is reached.

---

## 30. Comment, Documentation & Design-Traceability Rule

Production code MUST remain self-explanatory for ordinary structure and behaviour. Comments and implementation documentation exist to preserve non-obvious design intent and traceability; they MUST NOT become a substitute for clear naming, strong typing, cohesive decomposition or correct ownership.

Where understanding a material implementation responsibility depends on accepted design semantics that are not evident from ordinary typed code, tests and naming, at least one appropriate trace anchor MUST identify the exact governing accepted provision.

Comments SHOULD explain non-obvious:

- semantic invariants;
- capability ownership boundaries;
- transaction/concurrency/idempotency reasoning;
- historical-affinity requirements;
- security/authority constraints;
- provider quirks or replacement constraints;
- deliberately rejected simpler implementations; and
- other architectural constraints whose WHY is not evident from clean code.

Comments MUST NOT merely narrate obvious control flow or restate method/variable names. Comments MUST NOT contain an undocumented alternative semantic contract. If a comment is required to explain material business behaviour for which no governing accepted authority can be found, treat the behaviour as a possible design gap and escalate under Section 6 if unresolved.

### 30.1 Exact Authority Pointer Contract

A material design pointer MUST be deterministic enough for another engineer to reach the exact governing rule without relying on conversation history, inference or broad document search.

A conforming pointer MUST identify:

```text
stable authority identifier
+
accepted version/composition where applicable
+
exact repository path to the governing constituent document
+
exact section/clause identifier and heading
+
stable clause identifier where the accepted authority already defines one
```

For example, the required shape is:

```text
MS-PROT-XYZ vN
`designs/<exact accepted constituent filename>.md`
§X.Y — <exact governing heading>
<stable clause identifier, when one exists>
```

Line numbers MUST NOT be the primary authority pointer because ordinary document editing makes them unstable.

The following are insufficient terminal pointers for material traceability:

```text
see design docs
see architecture
see MS-PROT-XYZ
see Authority Index
as previously agreed
according to the spec
see section above
```

`designs/AUTHORITY-INDEX.md` MAY be used to resolve the current accepted composition, but it is normally navigation rather than the terminal pointer. Where a composite authority governs, the trace anchor MUST identify the exact accepted constituent provision that supplies the implemented rule.

ChatGPT conversation references, remembered descriptions, handover notes and implementation summaries MUST NOT substitute for accepted repository authority.

### 30.2 Trace-Anchor Placement Rule

GrandRue MUST NOT achieve traceability by saturating source files with authority comments.

A material semantic responsibility SHOULD have the smallest useful stable trace anchor at the boundary that owns or exposes that responsibility, for example:

```text
aggregate / domain policy / principal operation
application orchestration service
repository or migration boundary
provider adapter
package/module documentation
test class or nested contract-test group
implementation/conformance evidence
```

If several methods implement one class-owned invariant, one precise class/invariant-level trace anchor is preferable to repeated per-method citations.

An authority comment on every class, method, field, branch, repository call or test is prohibited when a smaller set of ownership-boundary anchors provides unambiguous traceability.

### 30.3 Clean-Code-First Rule

Before adding explanatory comments, implementation MUST determine whether the ambiguity can reasonably be removed by:

```text
better naming
stronger types
smaller cohesive responsibilities
clearer ownership
simpler control flow
better decomposition
```

If so, behaviour-preserving refactoring is preferred over adding a comment.

Use the following distinction:

```text
WHAT / ordinary HOW
    → code should normally explain it

WHY this non-obvious accepted constraint exists
    → concise comment + exact authority pointer where material
```

Documentation MUST NOT compensate for unnecessarily obscure implementation.

### 30.4 Comment-Minimisation Rule

A design-trace comment SHOULD normally contain only:

```text
concise local WHY
+
exact authority pointer
```

It SHOULD point to accepted authority rather than copy substantial design prose into source code.

Comments MUST NOT create a second semantic corpus inside Java, SQL, configuration or tests.

### 30.5 Tests-to-Authority Traceability

Material invariant and contract tests SHOULD be traceable to the exact accepted provision they prove.

This does not require a citation on every test method. Prefer a test-class, nested test-group or evidence-level trace anchor when multiple tests prove the same provision, while descriptive test names identify individual cases.

For material behaviour, an engineer SHOULD be able to navigate:

```text
exact accepted provision
        ↓
invariant / contract
        ↓
executable test
        ↓
production implementation
```

A passing test without a determinable governing contract is insufficient completion evidence where the behaviour is semantically material.

### 30.6 Bidirectional Traceability

Traceability SHOULD support both directions:

```text
implementation
    ↓
exact governing accepted provision
```

and, through implementation/conformance evidence:

```text
accepted provision
    ↓
implementation responsibility
    ↓
tests
    ↓
production code
```

Accepted design documents SHOULD NOT be polluted with volatile source-code locations merely to provide reverse navigation. Reverse implementation navigation belongs primarily in implementation/conformance evidence because implementation changes more frequently than semantic authority.

### 30.7 Implementation Documentation Boundary

Material implementation documentation MUST leave enough navigation for an engineer to reconstruct the design-to-code relationship without consulting ChatGPT history.

Where an implementation-evidence document is warranted, it SHOULD identify:

```text
implementation target
governing exact authority pointer(s)
implemented responsibilities
important code locations
executable tests
verification commands/results
known limitations / deferred concerns
commit SHA
```

Implementation documentation remains evidence/navigation. It MUST NOT redefine semantic behaviour or become a parallel design specification.

### 30.8 Authority-Change Impact Rule

When an accepted authority referenced by implementation changes materially, the affected trace anchors, code, tests and implementation documentation MUST be reviewed for continued conformance.

References MUST NOT be mechanically advanced to a newer authority version/composition without checking that the implementation still conforms to the amended semantics.

### 30.9 Stale-Reference Rule

A materially misleading authority reference is an implementation defect.

Trace anchors MUST be corrected or removed when the referenced provision is superseded, responsibility moves, the documented invariant no longer applies, or implementation changes make the explanation materially false.

Removing a stale reference MUST NOT remove necessary traceability where the underlying material responsibility remains; the trace anchor must move to the correct stable boundary.

### 30.10 Documentation Non-Duplication Rule

Comments and implementation documents SHOULD use:

```text
short local explanation
+
exact accepted authority pointer
```

rather than reproduce the governing design rule.

The objective is maximum useful traceability with minimum documentation noise and stale-reference surface.

---

## 31. Test Determinism Rule

Deterministic production behaviour SHOULD have deterministic tests. Tests MUST NOT depend on arbitrary sleeps, uncontrolled wall-clock time/randomness, live providers for ordinary builds, hidden environment state or shared mutable fixtures. Flakiness is a defect, not a reason for repeated reruns.

---

## 32. Time Rule

Business-time logic SHOULD use controllable explicit clocks. Time zones, DST and historical time-affinity follow governing authority. System-local time MUST NOT silently define merchant/customer semantics where explicit timezone is required.

---

## 33. Provider Test Rule

External providers SHOULD be tested through deterministic adapter tests, provider contract/sandbox tests where appropriate, narrow integration tests and production observability. Core builds SHOULD NOT depend on live provider availability.

---

## 34. Database Test Rule

Where transactions, constraints or SQL behaviour matter, tests SHOULD use a persistence mode representative enough of production. In-memory substitutes MUST NOT be trusted for behaviours they cannot model.

---

## 35. Migration Rule

Schema/data migrations MUST preserve historical and semantic guarantees and SHOULD be forward-safe/testable. Semantic migration and physical migration remain distinct. Material semantic migration requires the accepted semantic migration process.

---

## 36. Observability Rule

Production code SHOULD expose sufficient operational evidence without making telemetry business truth. Sensitive information MUST be minimised. Behaviour-preserving observability additions MAY be automated.

---

## 37. Performance Rule

Performance optimisation MUST be evidence-driven where it adds complexity. Do not change architecture/semantics for hypothetical scale. Caching authoritative state, relaxing consistency, introducing asynchronous processing, partitioning ownership or changing transaction boundaries requires approval.

---

## 38. Resource-Conscious Implementation Rule

Merchant-facing implementation MUST account for accepted mobile/resource constraints and avoid unnecessary payload, client storage, polling, background work and memory-heavy behaviour.

---

## 39. Code Duplication Rule

Remove duplication only when it represents the same semantic responsibility. Similar-looking code from different bounded contexts MUST NOT be collapsed merely for textual reuse. Cross-capability abstractions require ownership scrutiny.

---

## 40. No Business-Type Branching Rule

Business-category conditionals MUST NOT substitute for generic semantic composition where design does not authorise them. Behaviour should derive from configured capabilities, policies and context.

---

## 41. No Plan-Type Branching Rule

Plan names SHOULD NOT decide business semantics. Plans govern entitlement where applicable; semantic applicability remains with configuration/capability authority.

---

## 42. No Provider-Type Branching in Domain Rule

Capability-domain code MUST NOT branch on concrete provider identity where provider-neutral semantics suffice. Provider-specific behaviour belongs at adapter/provider-selection boundaries unless explicit design scrutiny establishes otherwise.

---

## 43. Automated Improvement Classification

```text
AUTO_FIX       implementation defect; semantics unchanged
AUTO_TEST      coverage of accepted behaviour
AUTO_REFACTOR  structure improved; behaviour unchanged
AUTO_HARDEN    defensive implementation of accepted constraints
MANUAL_APPROVAL changes semantics/architecture/contract/authority/observable behaviour
DESIGN_ESCALATION authorities insufficient/ambiguous/contradictory
```

Only the first four classes may continue automatically.

---

## 44. Automated Sequence Failure Handling

If accepted behaviour is clear, identify and fix the implementation/test defect and rerun verification. If it is not clear, stop affected work as `DESIGN_ESCALATION`. Automation MUST NOT repeatedly mutate tests/code without establishing authority.

### 44.1 Source-Rooted Defect Correction Rule

When a defect is discovered after a migration, verification programme, implementation node or other bounded execution has closed, implementation MUST correct the current repository state from the earliest authoritative source that owns the incorrect state, behaviour, dependency or representation.

A closed historical programme MUST NOT be reopened merely because it introduced, failed to detect or previously passed over the defect.

The correction process MUST:

1. establish reproducible current evidence of the defect;
2. resolve the relevant accepted authority through `AUTHORITY-INDEX.md`;
3. identify the current semantic, architectural or implementation owner;
4. trace the causal dependency chain to the authoritative source of the incorrect state;
5. correct that source and only the necessary dependent surfaces;
6. verify the correction according to its current blast radius; and
7. enter `DESIGN_ESCALATION` if accepted authority does not determine the correct state or owner.

Historical migration, verification, checkpoint and completion evidence remains immutable provenance of what was executed and checked. It is evidence, not repair authority.

Where the missed defect exposes a reusable weakness in an applicable test, conformance rule or checker, that verification weakness SHOULD be corrected independently and regression-tested or negative-controlled where proportionate. Strengthening the detector does not reopen the historical programme and does not rewrite or retroactively invalidate its historical receipts.

A downstream workaround MUST NOT substitute for a safe source correction merely because the workaround is smaller or produces a faster green build.

---

## 45. Improvement Proposal Rule

A material improvement outside current authority MUST be proposed rather than implemented. Proposal evidence SHOULD include constraint, evidence, proposed improvement, affected authorities/modules/contracts, alternatives, trade-offs, migration/compatibility impact, test impact and recommendation, followed by manual approval.

---

## 46. Commit Discipline

Implementation commits SHOULD preserve traceability between accepted design, tests and code. Production code and relevant tests SHOULD normally commit together. Required failing tests block completion unless an approved staged state is explicitly recorded.

The normal durable implementation unit is a coherent passing behavioural slice. Intermediate RED, GREEN, verification, evidence or recovery commits MAY occur where they improve traceability or resumability.

A slice commit does not automatically complete its enclosing graph node. The next slice inside the same node MAY proceed when the current slice is durably checkpointed and its prerequisites remain satisfied.

Before selecting a different READY node after a terminal node transition, the required graph/evidence/controller synchronisation under Section 52.14 MUST be committed to `development`.

Checkpoint administration SHOULD be materially cheaper than the implementation checkpoint it protects except where the risk of the change requires stronger evidence.

---

## 47. Implementation Evidence

Material milestones MUST record sufficient evidence to establish the scope of their completion claim.

For an ordinary bounded slice, sufficient evidence MAY consist of:

```text
exact authority pointer(s)
+
tests
+
verification result
+
Git diff / commit
+
/IMPLEMENTATION.md checkpoint
```

Dedicated evidence documents are not mandatory merely because production code changed.

Dedicated evidence remains required where needed to support a material claim involving node completion, macro completion, architecture, security/trust, persistence migration, concurrency, recovery, provider execution, historical affinity, material falsification or another responsibility whose proof cannot be reconstructed reliably from tests, Git and the live controller.

Where applicable, material evidence SHOULD identify:

```text
implementation target / node / slice
macro target
exact governing authority pointer(s)
implementation responsibilities
material code/test locations
verification commands/results
completion-falsification findings
limitations / deferred concerns
evidence scope / freshness
commit SHA
```

Implementation evidence is non-authoritative navigation/proof and MUST NOT redefine semantic authority or duplicate long history already retained in Git.

---

## 48. Full Verification Gate

Verification SHALL be proportional to the claim being made.

A behavioural-slice checkpoint MUST run the cheapest verification sufficient to falsify the slice claim, including as applicable targeted semantic/unit tests, relevant contract tests, bounded integration tests, affected architecture/conformance/static checks, migration checks and explicit negative/adversarial cases.

A node MUST NOT become `COMPLETE` or `CONFORMING_COMPLETE` until its complete applicable verification gate passes. Where applicable that gate includes compilation, semantic/unit tests, application tests, contract/integration tests, architecture/conformance tests, the full Maven/PostgreSQL suite and configured static checks.

A still-fresh full verification result MAY be reused only where no intervening change can materially invalidate it. Targeted slice verification MUST NOT be promoted into a node-completion claim.

Known failing required checks block the completion claim.

## 49. Production Readiness Boundary

Passing tests alone does not establish production readiness. Applicable readiness also requires secrets/configuration, migrations, observability, rollback/recovery, provider/environment configuration, security, data protection, resource/performance validation and failure-mode behaviour.

---

## 50. Implementation Review Checklist

Before completing a material implementation item verify:

1. Which accepted authority governs it?
2. What invariant/contract does each new test represent?
3. Did code introduce a new semantic concept?
4. Is each mutation performed by its owning capability?
5. Is application orchestration still orchestration rather than ownership?
6. Are configuration and runtime facts separated?
7. Are entitlement and applicability distinct?
8. Are provider concepts isolated?
9. Are transaction boundaries correct?
10. Are retry/idempotency semantics explicit where necessary?
11. Are concurrency conflicts protected?
12. Are material failures distinguishable?
13. Is authority enforced server/runtime-side rather than only UI-side?
14. Are projections prevented from becoming mutation authority?
15. Are tests deterministic?
16. Were tests weakened to fit implementation? If yes, STOP.
17. Did a dependency materially change architecture/risk? If yes, STOP.
18. Did refactoring alter behaviour/ownership? If yes, STOP.
19. Did implementation require guessing semantics? If yes, STOP.
20. Which constituent composite paradigm applies and why?
21. Has a paradigm been forced where ordinary typed code is simpler and sufficient?
22. Has a framework/library become architectural authority?
23. For a slice checkpoint, did every proportional required check pass; for a node-completion claim, does the full applicable verification gate pass?
24. Has `/IMPLEMENTATION.md` been synchronised with the current graph/evidence state required by this checkpoint or terminal node transition?
25. Was any branch created without an explicit branch-creation instruction in the active chat? If yes, STOP and record the governance violation.
26. Can the exact governing accepted provision be reached deterministically from each required material trace anchor?
27. Does every material authority pointer identify the accepted authority, exact constituent repository path and exact governing section/clause rather than a vague document-level reference?
28. Are non-obvious semantic or architectural decisions traceable without ChatGPT history?
29. Are comments explaining WHY rather than narrating WHAT the code already states?
30. Could clearer naming, typing, decomposition or ownership remove any explanatory comment? If yes, refactor where behaviour-preserving.
31. Does implementation documentation point to accepted authority rather than duplicate it?
32. Are any design/authority references stale, misleading or mechanically advanced without semantic review?
33. Has completion been actively falsified rather than inferred from the new happy-path tests?
34. Are implementation-state claims supported by fresh repository evidence, with observations, deductions and uncertainty kept distinct?

---

## 51. Relationship to Design and Programme Governance

This document governs implementation behaviour only and MUST NOT bypass GrandRue Design Rules, accepted MS-PROT authorities, accepted TAS/ADR authority, DOCUMENT-GOVERNANCE, Authority Index, Canonical Semantic Lexicon, Current DDR or an accepted implementation-programme authority such as MS-IMP-001.

Where an accepted implementation-programme authority exists, implementation MUST additionally conform to it.

```text
Accepted product/design intent
        ↓
Accepted semantic/architectural authority
        ↓
Accepted implementation-programme governance
        ↓
Implementation Rules
        ↓
Tests and production code
        ↓
Implementation evidence
```

An implementation-programme authority may order implementation work but cannot override substantive semantic/design authority.

Conflicts MUST be escalated rather than silently resolved in code.

At an implementation-discovered manual design gate, the current accepted
`DESIGN-RULES.md` controls the proposal, review, falsification, ambiguity
review, recommendation, complete pre-approval presentation, approval-scope
validation and repository-isolation process.

---

## 52. Canonical Automation Loop

```text
READ AUTHORITY
      ↓
RESOLVE ELIGIBLE MACRO TARGET
      ↓
BUILD / REFRESH FINE-GRAINED IMPLEMENTATION GRAPH
      ↓
SELECT SMALLEST MEANINGFUL READY NODE
      ↓
PREPARE NODE AUTHORITY / DEPENDENCY BOUNDARY ONCE
      ↓
SELECT DEPENDENCY-COMPLETE BEHAVIOURAL SLICE
      ↓
TEST FIRST
      ↓
MINIMUM CONFORMING SLICE IMPLEMENTATION
      ↓
PROPORTIONAL SLICE VERIFICATION / FALSIFICATION
      ↓
COMMIT SLICE CHECKPOINT
      ↓
MORE WORK IN NODE?
      ├── YES → NEXT CLOSED SLICE
      └── NO
            ↓
        FULL APPLICABLE NODE-COMPLETION GATE
            ↓
        CLASSIFY DISCOVERED IMPROVEMENT
            │
            ├── AUTO_* → apply/retest
            ├── MANUAL_APPROVAL → block affected path and propose
            └── DESIGN_ESCALATION → block affected path and reopen design
            ↓
        RECORD MATERIAL COMPLETION EVIDENCE
            ↓
        MARK NODE TERMINAL STATE
            ↓
        REFRESH GRAPH / MACRO STATE WHERE CHANGED
            ↓
        SYNCHRONISE /IMPLEMENTATION.md
            ↓
        VERIFY CONTROLLER / GRAPH / EVIDENCE CONSISTENCY
            ↓
        COMMIT CYCLE-CLOSING STATE
            ↓
        NEXT READY NODE
```

### 52.1 Graph-driven implementation protocol

The automated implementation lifecycle SHALL operate over an explicit dependency graph whenever more than one material implementation target exists or target ordering affects correctness.

Where an accepted macro implementation-programme authority exists, the implementation dependency graph has two levels:

```text
MACRO GRAPH
    governed by the accepted implementation-programme authority

FINE-GRAINED GRAPH
    dynamically maintained under Implementation Rules
```

A fine-grained execution node MUST belong to or explicitly satisfy one eligible macro target.

An implementation node represents the smallest coherent implementation responsibility whose governing authority, prerequisites, invariants, tests and completion evidence can be evaluated independently.

A node is not a source-file granularity rule. One node MAY require multiple dependency-complete behavioural slices, and one slice MAY modify multiple files/classes/adapters where they jointly realise one already-governed behaviour.

A dependency edge means that the downstream node MUST NOT begin until the upstream prerequisite is complete or otherwise explicitly satisfied by existing conforming implementation.

The graph is implementation governance/planning. It MUST NOT create semantic authority, capability ownership, business lifecycle state or product requirements.

### 52.2 Ready-node rule

A node is `READY` only when:

1. its owning macro target is READY where a macro programme governs;
2. all required semantic/architectural authorities are accepted;
3. all declared implementation prerequisites are satisfied;
4. no unresolved design escalation blocks the node;
5. its invariants/contracts can be stated without guessing;
6. the applicable test and conformance obligations can be identified.

Automation MAY select any ready node consistent with dependency order, but SHOULD prefer the smallest node that unlocks useful downstream work and provides early architectural evidence.

Automation MUST NOT select a blocked downstream node merely because it is easier or more visible.

### 52.3 Node and Slice Execution State

Implementation nodes MAY be tracked using states equivalent to:

```text
PENDING
READY
IN_PROGRESS
BLOCKED_DESIGN
BLOCKED_DEPENDENCY
FAILED_IMPLEMENTATION
COMPLETE
```

Within one READY/IN_PROGRESS node, behavioural slices MAY use operational states equivalent to:

```text
PREPARATION_REQUIRED
READY
IN_PROGRESS
VERIFIED
COMMITTED
```

Multiple committed slices MAY exist inside one `IN_PROGRESS` node.

Node and slice states describe implementation workflow only. They MUST NOT be projected into merchant/domain semantics or treated as authoritative business state.

### 52.4 Blocked-node isolation

If one node encounters `MANUAL_APPROVAL` or `DESIGN_ESCALATION`:

```text
block affected node
      ↓
block only downstream dependants that require it
      ↓
record reason / governing ambiguity
      ↓
continue any independent READY nodes
```

Automation MUST NOT stop the entire implementation programme solely because one branch of the implementation graph is blocked.

Conversely, automation MUST NOT bypass a blocked prerequisite by duplicating responsibility, weakening tests, inventing a temporary semantic rule or creating an alternate implementation path that contradicts the accepted architecture.

### 52.5 Graph refresh rule

The implementation graph MUST be refreshed when any of the following materially changes executable prerequisites:

- an implementation node completes;
- a macro target completes;
- a design escalation is resolved;
- accepted authority is amended;
- tests reveal a previously unknown dependency;
- repository inspection reveals already-implemented prerequisite behaviour;
- a node is split because its responsibilities are not independently testable;
- a node is merged only where they represent one inseparable accepted invariant.

Graph refinement is automatic only when it does not change semantic ownership, accepted contracts, transaction boundaries, provider responsibility, externally observable behaviour or an accepted macro HARD dependency/PROGRAMME_GATE. Otherwise it requires the applicable approval path.

The graph MUST NOT be rewritten ceremonially after every internal slice. Refresh it when dependency/readiness/completion facts materially change.

### 52.6 Agent authority boundary

An implementation agent MAY autonomously:

```text
inspect repository state
construct/refine the fine-grained implementation dependency graph
select READY nodes inside READY macro targets
prepare and execute dependency-complete behavioural slices
write tests for accepted invariants
implement minimum conforming slices
run proportional slice and full node-completion verification as applicable
perform AUTO_* improvements
record evidence
synchronise /IMPLEMENTATION.md from current graph/evidence
commit conforming slice checkpoints and terminal cycle-closing state
continue to newly READY nodes
```

An implementation agent MUST NOT autonomously:

```text
invent missing semantics
change capability ownership
change transaction/concurrency/idempotency meaning
create new provider or commercial policy
weaken accepted tests/invariants
reinterpret blocked prerequisites
change an accepted macro target or programme gate
approve its own material design proposal
create any Git branch without an explicit branch-creation instruction in the active chat
```

Graph-driven automation expands execution autonomy only inside already accepted design and implementation-programme authority. It does not expand AI semantic authority or branch-creation authority.

### 52.7 Graph Evidence and Traceability

For each completed material node, implementation evidence SHOULD identify:

```text
node identity / target
macro target where applicable
exact governing authority pointer(s)
prerequisite nodes or already-satisfied prerequisites
implemented invariants/contracts
material production-code locations
tests and verification evidence
completion-falsification / counterevidence findings
evidence scope and freshness
blocked/deferred dependants unlocked by completion
commit SHA
```

The fine-grained implementation graph SHOULD be reproducible from accepted authority plus repository state. Macro programme state is governed by the accepted implementation-programme authority.

### 52.8 Canonical macro programme source

When MS-IMP-001 is accepted and current, it is the binding macro implementation dependency graph for the production implementation programme.

The Implementation Rules execute work inside that graph; they do not replace its HARD dependencies, PROGRAMME_GATE ordering or target completion criteria.

### 52.9 Existing implementation satisfaction

Repository inspection may establish that a prerequisite already exists.

It counts as satisfied only where:

```text
accepted authority
+
implementation
+
tests
+
conformance
```

prove equivalent completion.

Existing code without this evidence remains unverified implementation and MUST NOT silently unlock downstream macro targets.

### 52.10 Macro target decomposition and completion propagation

A macro target MAY be split automatically into independently testable fine-grained child nodes where substantive authority and macro gates remain unchanged.

When all required child nodes satisfy the macro target's accepted completion gate:

```text
macro target
    → COMPLETE
```

The implementation graph is then refreshed and only newly eligible downstream macro targets become READY.

### 52.11 Automatic implementation authorisation

Where MS-IMP-001 or another accepted implementation-programme authority explicitly authorises automatic READY-node execution, no additional manual approval is required for each implementation node provided:

```text
accepted substantive authority is explicit
macro programme permits the node
change stays inside Implementation Rules auto-change boundaries
no MANUAL_APPROVAL condition is encountered
no DESIGN_ESCALATION is encountered
```

This is implementation authorisation only. It is not authority to alter accepted design and does not authorise branch creation; Section 2.1 still requires an explicit branch-creation instruction in the active chat.

### 52.12 Programme-graph change boundary

The implementation agent may refine the fine-grained graph automatically.

It may NOT autonomously change an accepted macro:

```text
target meaning
HARD dependency
PROGRAMME_GATE
programme completion criterion
```

where doing so changes the accepted implementation strategy.

Such a change requires proposal, review and manual approval.

### 52.13 Initial MS-IMP-001 state

Upon formal acceptance of MS-IMP-001:

```text
CURRENT IMPLEMENTATION TARGET:
    IMP-00 — Current Implementation Baseline

READY:
    IMP-00

BLOCKED_DEPENDENCY / PENDING:
    IMP-01..IMP-20
```

The canonical automation loop begins by inspecting and classifying the existing repository rather than writing speculative new production code.

### 52.14 Mandatory `/IMPLEMENTATION.md` Synchronisation

Root `/IMPLEMENTATION.md` is the single live non-authoritative implementation execution controller.

At each durable behavioural-slice checkpoint it SHALL contain sufficient current information to restart the active node without conversation memory. At a terminal node transition (`COMPLETE`, `BLOCKED_DESIGN`, `BLOCKED_DEPENDENCY`, `FAILED_IMPLEMENTATION`) it SHALL be synchronised after any required graph/evidence refresh and before a different READY node is selected.

The controller MUST expose, at minimum where applicable:

1. current macro target and state;
2. current fine-grained node and state;
3. current slice identity/state or `null`;
4. exact authority references needed for the active work;
5. current blockers/escalations;
6. canonical graph/evidence pointer;
7. relevant last checkpoint/verification evidence;
8. immediate next governed action.

Required terminal ordering:

```text
accepted authority + repository state
        ↓
implementation / verification evidence
        ↓
refresh graph / macro state where materially changed
        ↓
synchronise /IMPLEMENTATION.md
        ↓
verify controller / graph / evidence consistency
        ↓
commit cycle-closing state
        ↓
select next READY node
```

`/IMPLEMENTATION.md` does not create implementation permission, semantic meaning, graph edges or macro completion. The canonical fine-grained dependency/readiness graph remains `docs/development/implementation-programme-state.json`.

If the controller conflicts with the current graph or fresh conformance evidence, the graph/current evidence governs and the controller is stale. Correct it before implementation continues.

`docs/development/implementation-status.md` is historical/compatibility evidence after v2.0 and MUST NOT be used to select current implementation work.

Controller synchronisation requires no separate manual approval when it only records facts already determined by accepted authority and repository evidence.

If reconciliation exposes a genuine contradiction that cannot be resolved from accepted authority and repository evidence, stop the affected selection path and follow the applicable programme/design escalation rule.

### 52.15 Repository-Evidence and Freshness Rule

Current repository state is the primary source of implementation fact.

Before asserting materially that behaviour is `IMPLEMENTED`, `MISSING`, `PARTIAL`, `CONFORMING`, `NON_CONFORMING`, `COMPLETE`, `READY` or `BLOCKED`, the implementation agent MUST inspect sufficient current repository and verification evidence to support that classification.

Prior conversations, remembered implementation state, handovers, status summaries, previous test results, commit messages and previous implementation evidence MAY guide navigation but MUST NOT substitute for current repository evidence when the state may have changed.

Repository observations MUST be refreshed when intervening commits, authority changes, migrations, test-fixture changes or substantial implementation work could materially invalidate them. Unchanged evidence MUST NOT be re-inspected ritualistically without a freshness reason.

### 52.16 Observation, Deduction and Uncertainty Rule

Implementation reasoning MUST preserve the distinction among:

```text
OBSERVED
    directly established by repository/verification evidence

DEDUCED
    supported inference from observed evidence

UNCERTAIN
    evidence is insufficient to establish the claim

RECOMMENDED
    proposed action or interpretation
```

A deduction MUST NOT silently become an observation during later reasoning. Material uncertainty MUST NOT silently become an implementation or design assumption.

Where uncertainty can be resolved through accepted authority, repository inspection or deterministic testing, it MUST be investigated before proceeding. If accepted authority remains insufficient, the affected work follows `DESIGN_ESCALATION`.

### 52.17 No Inference Across Evidence Gaps

Required behaviour MUST NOT be inferred merely because:

- adjacent infrastructure exists;
- similarly named classes/tables/migrations exist;
- another capability implements similar behaviour;
- a design document requires the behaviour;
- neighbouring tests pass;
- an interface exists without a proven implementation path; or
- another implementation node is complete.

The following distinctions are mandatory:

```text
DESIGNED ≠ IMPLEMENTED
IMPLEMENTED ≠ VERIFIED
VERIFIED COMPONENT ≠ VERIFIED DEPENDANT
```

### 52.18 Counterevidence Before Completion

Before a material node is classified `COMPLETE`, the implementation agent MUST actively seek reasonable evidence that would falsify completion where applicable, including:

- missing or alternate execution paths;
- bypasses around the invariant;
- concurrency or rollback paths;
- idempotency gaps;
- stale fixtures or migrations;
- architectural dependency violations;
- authority-bypass paths;
- tests proving only a happy path;
- stale implementation evidence; and
- stale or misleading design references.

Completion means the claim survived reasonable falsification. A new happy-path test passing is not sufficient by itself.

### 52.19 Evidence Locality Rule

Evidence establishes only what it actually proves.

A passing service/integration test MUST NOT automatically be promoted into proof that all callers, all transaction boundaries, all adapters, all dependants or the containing macro target conform unless additional evidence establishes those claims.

Implementation evidence MUST state or preserve its material scope where over-promotion would be plausible.

### 52.20 AI Evidence-Hierarchy Rule

An AI implementation agent MUST rank current repository inspection, accepted authority and executable verification above conversation memory, plausibility, common software convention, prior summaries and its own previous conclusions when determining implementation fact.

Where those sources conflict, the agent MUST investigate the current authoritative evidence rather than select the more plausible narrative.

The agent MUST NOT fill a material implementation or design gap merely because one answer is conventional, elegant or statistically likely.

---

## 53. Composite Architecture & Programming Paradigm Conformance

GrandRue uses a deliberately composite architecture. Production code, test code and implementation proposals MUST conform to the accepted composite architecture rather than allowing implementation convenience, framework convention or local code simplicity to silently replace an accepted responsibility with another paradigm.

The accepted implementation composition is:

```text
Business-driven architecture
        +
Capability-oriented bounded contexts / modules
        +
Modular monolith as the initial deployment architecture
        +
Declarative registered semantics and deterministic configuration compilation
        +
Transactional execution where atomic invariants require it
        +
Selective post-commit event-driven reaction
        +
Explicit lifecycle/state modelling where business state materially matters
        +
Ports and adapters for persistence/external providers/infrastructure
        +
Explicit application orchestration for cross-capability use cases
        +
Ordinary typed imperative/functional code where no stronger paradigm is warranted
```

### 53.1 Applicability rule

Composite does **not** mean every constituent paradigm must appear in every implementation target.

The applicable paradigm MUST follow the responsibility being implemented:

```text
Business invariant
    → capability/domain ownership

Cross-capability use case
    → explicit application orchestration

Atomic invariant
    → transactional consistency

Post-commit independent reaction
    → event-driven handling where justified

External provider / persistence technology
    → port and adapter

Merchant-supported semantic variation
    → registered declarative semantics / deterministic compiler

Meaningful business lifecycle
    → explicit state/lifecycle model

Simple deterministic value transformation
    → ordinary typed code
```

A pattern MUST NOT be introduced merely because it exists elsewhere in GrandRue.

### 53.2 Paradigm-fit gate

Before production code is accepted, implementation MUST establish where applicable:

1. the semantic owner;
2. the applicable constituent paradigm and why it fits;
3. module/bounded-context integrity;
4. mutation authority;
5. required consistency boundary;
6. infrastructure isolation through ports/adapters where applicable;
7. that events are reactions rather than substitutes for required atomicity;
8. that merchant semantic variation remains declarative where governed as configuration;
9. that cross-capability workflow is explicitly orchestrated;
10. that no framework/library has become architectural authority;
11. that no unnecessary pattern has been imposed on simple code.

Failure of this gate blocks implementation completion.

### 53.3 Architecture-test rule

Architecture/conformance tests SHOULD prove mechanically enforceable aspects of the composite architecture, including where applicable:

- forbidden cross-capability dependencies;
- direct cross-capability persistence mutation;
- transaction rollback/atomicity requirements;
- compiler determinism;
- event-after-commit behaviour;
- adapter substitutability/contracts;
- orchestration boundary integrity;
- provider SDK leakage into domain APIs;
- prohibited business-type/plan-type/provider-type branching.

Tests MUST NOT pretend to mechanically prove architectural judgement that requires human review.

### 53.4 Anti-dogma rule

If an accepted constituent paradigm is demonstrably unsuitable for a new responsibility, implementation MUST NOT force it merely to satisfy stylistic uniformity.

Instead:

```text
identify mismatch
    ↓
stop affected implementation
    ↓
propose alternative
    ↓
review + falsification
    ↓
manual approval
    ↓
update accepted authority if required
    ↓
implement
```

Composite architecture is a governed method for selecting appropriate responsibility patterns, not a universal-pattern mandate.

### 53.5 Deployment rule

The modular monolith remains the initial deployment architecture. Internal modular boundaries MUST be real enough that a later distribution decision could be evaluated deliberately, but code MUST NOT introduce distributed-systems complexity merely to simulate hypothetical future services.

A move to distributed deployment requires explicit architectural approval.

---

## 54. Single-Document Authority Rule

`designs/IMPLEMENTATION-RULES.md` is the **single canonical implementation-governance document**.

Accepted implementation-rule improvements MUST be merged into this document as a new integrated version rather than maintained as parallel amendment files.

Historical Git commits preserve provenance. Separate implementation-rule amendment documents MUST NOT become competing current authorities.

When this document changes materially:

```text
review + falsification where required
        ↓
manual approval where required
        ↓
merge into IMPLEMENTATION-RULES.md
        ↓
increment integrated version
        ↓
update governance/conformance references where applicable
        ↓
commit
```

If any separate implementation-rule amendment file is later discovered, its accepted content MUST be reconciled into this canonical document and the duplicate current authority retired or removed after provenance is verified.

---

## 55. Agent Context-Efficient Authority Loading

### 55.1 Governing Decision

GrandRue implementation agents SHALL use:

```text
SMALL PERSISTENT GOVERNANCE KERNEL
        +
TASK-SCOPED CURRENT AUTHORITY
        +
FAIL-SAFE CONTEXT WIDENING
```

rather than unconditional full-governance-corpus loading before every material implementation task.

Context efficiency MUST NOT weaken accepted authority precedence, semantic ownership, design-gap escalation, implementation test integrity, authority freshness, repository evidence requirements or manual approval boundaries.

The optimisation rule is:

> Load the smallest context whose sufficiency can presently be established; whenever sufficiency becomes uncertain, widen context rather than infer missing meaning.

Context optimisation MUST fail toward additional current authority or `DESIGN_ESCALATION`, never toward invented semantics.

### 55.2 Authority Boundary

This context-loading mechanism creates no semantic, architecture or product authority.

Authority remains:

```text
AUTHORITY-INDEX.md
    → determines WHICH accepted authority currently governs

accepted substantive authority
    → determines WHAT the governing meaning is

IMPLEMENTATION-RULES.md
    → determines HOW accepted authority is implemented
```

No agent instruction file, context cache, previous conversation, implementation graph, evidence document, source code, test or model judgement may replace those responsibilities.

### 55.3 Repository Agent Adapter

The repository root SHALL contain one `AGENTS.md` operating adapter for repository coding agents.

`AGENTS.md` is `NON-AUTHORITATIVE`.

It SHALL provide the minimum persistent implementation-governance kernel, point to canonical current authorities, define the authority-loading procedure, define widening and invalidation triggers, identify standard verification commands and preserve explicit stop conditions.

It MUST NOT define business semantics, become a capability authority, reproduce MS-PROT/TAS/ADR substantive rules, maintain an independent current-authority catalogue, decide which semantic authority supersedes another, resolve deferred questions, create implementation permission beyond accepted authority, replace `DESIGN-RULES.md`, replace `IMPLEMENTATION-RULES.md`, or become a second governance corpus.

If `AGENTS.md` conflicts with accepted authority:

```text
accepted authority governs
+
AGENTS.md is defective
+
the adapter must be corrected
```

### 55.4 Single Adapter Rule

Initial implementation SHALL use exactly one repository-scoped agent adapter:

```text
/AGENTS.md
```

No repository `AGENTS.override.md`, nested `AGENTS.md`, or module-specific `AGENTS.md` shall be introduced without separate review establishing a repeated module-specific need that cannot be handled adequately through task-scoped authority retrieval.

The objective is to avoid creating another hierarchical rule system.

### 55.5 Adapter Size Boundary

The root `AGENTS.md` MUST remain a concise navigation and execution map.

Initial limit:

```text
maximum UTF-8 size: 12 KiB
```

Its target should remain substantially below that maximum.

A need to exceed the limit is evidence that substantive authority, documentation or module-specific guidance is being duplicated into the adapter and MUST be reviewed before expansion.

### 55.6 Persistent Governance Kernel

`AGENTS.md` SHALL preserve these universal implementation rules directly and concisely:

1. accepted repository authority outranks agent instructions, memory, code, tests and implementation precedent;
2. `AUTHORITY-INDEX.md` determines which authority currently governs;
3. substantive accepted authority determines the governing semantic meaning;
4. missing, contradictory or materially ambiguous semantics MUST NOT be inferred;
5. such gaps cause the affected path to enter `DESIGN_ESCALATION`;
6. capability ownership and accepted architectural boundaries MUST be preserved;
7. test-first/test-integrity rules remain applicable;
8. minimum conforming implementation is preferred over speculative machinery;
9. current repository evidence outranks remembered repository state;
10. no Git branch may be created without explicit active-chat branch-creation authorisation;
11. required verification and completion falsification remain mandatory; and
12. uncertainty about context sufficiency causes widening rather than guessing.

These are operational restatements only. Canonical accepted authority remains controlling.

### 55.7 Task Classification

Before material repository work, the implementation agent SHALL classify the work as one of:

```text
A — LOCAL READ-ONLY / EXPLANATORY WORK
B — IMPLEMENTATION OF ALREADY ACCEPTED AUTHORITY
C — CROSS-CAPABILITY / CROSS-BOUNDARY IMPLEMENTATION
D — MATERIAL DESIGN / ARCHITECTURE / GOVERNANCE WORK
```

Classification is an implementation-navigation decision only.

If classification itself is materially uncertain, select the broader applicable class.

The agent MUST NOT choose a narrower class merely to reduce context.

### 55.8 Class A — Local Read-Only Work

For bounded inspection or explanation:

```text
identify affected repository scope
        ↓
inspect current relevant code/tests/evidence
        ↓
resolve governing authority where a semantic claim is made
        ↓
widen only if discovered scope requires it
```

Full governance-corpus loading is not mandatory merely because repository inspection is material.

Any semantic/design recommendation discovered during the inspection remains non-authoritative and follows the applicable design process.

### 55.9 Class B — Accepted-Authority Implementation

For implementation already determined by accepted authority:

```text
capture current branch + HEAD
        ↓
identify implementation target/node
        ↓
inspect current implementation programme state as needed
        ↓
resolve current governing authority through AUTHORITY-INDEX
        ↓
read exact applicable accepted constituent provisions
        ↓
read relevant implementation evidence
        ↓
inspect affected code and tests
        ↓
derive executable invariant
        ↓
follow IMPLEMENTATION-RULES lifecycle
```

The implementation agent NEED NOT reread the complete canonical governance corpus merely because another implementation task has begun.

### 55.10 Class C — Cross-Capability Work

Where the implementation crosses capability or architectural boundaries, context SHALL include:

```text
all participating semantic owners
+
accepted cross-capability contracts
+
applicable TAS/ADR architecture
+
relevant implementation evidence
+
affected code/tests
```

The implementation agent SHALL traverse discovered ownership and contract relationships until the material boundary is closed.

A primary capability alone MUST NOT be treated as sufficient context where another capability's authoritative fact or contract participates.

### 55.11 Class D — Material Design, Architecture or Governance

Material semantic design, architecture change, governance change, authority ambiguity resolution, systemic architectural falsification, corpus-wide conformance reasoning, new semantic ownership, or new cross-capability relationship is NOT eligible for narrow implementation context.

The agent SHALL load the broad applicable governance/design corpus required by `DESIGN-RULES.md`, including current authority navigation, relevant terminology/deferred-decision state and all materially applicable substantive authority.

Context efficiency MUST NOT truncate the governed design lifecycle.

### 55.12 Task-Scoped Authority Retrieval

Task-specific authority SHOULD be retrieved at the smallest stable granularity sufficient to establish the governing rule.

Preferred:

```text
exact authority
+
exact accepted constituent
+
exact section/clause
```

rather than automatically loading the entire document where the relevant accepted provision and its dependencies are determinable.

Where meaning depends on surrounding sections, amendment composition or dependencies, the retrieval scope MUST expand accordingly.

Line numbers MUST NOT be treated as stable authority identity.

### 55.13 Authority Composition

Scope-aware semantic authority MUST be resolved through the current `AUTHORITY-INDEX.md`.

The agent MUST NOT infer that highest version, latest filename, latest date, nearest code or current test automatically supplies current semantic meaning.

If current base/amendment/supersession composition cannot be determined reliably, `WIDEN`.

If it remains unresolved, `DESIGN_ESCALATION`.

### 55.14 Deferred-Decision Retrieval

The entire `DEFERRED-DECISION-REGISTER.md` need not be loaded for every implementation task.

The agent SHALL search/inspect the current DDR when:

- the applicable authority references a deferred decision;
- the implementation node/evidence identifies a design gap;
- affected terminology/concepts plausibly match an unresolved or promoted question; or
- implementation requires behaviour not explicitly determined by accepted authority.

A matching OPEN/PROMOTED/materially unresolved decision MUST NOT be silently implemented.

### 55.15 Lexicon Retrieval

The complete Canonical Semantic Lexicon need not be loaded for every implementation task.

The agent SHALL inspect the relevant current Lexicon entry when:

- introducing or changing semantic names;
- using an accepted high-risk overloaded term;
- ownership depends on terminology qualification;
- materially different concepts could share ordinary-language wording; or
- terminology ambiguity appears during implementation/review.

Terminology retrieval does not transfer semantic ownership to the Lexicon.

### 55.16 Mandatory Context-Widening Triggers

Current context MUST widen when any of the following is discovered:

```text
unknown semantic owner
multiple plausible semantic owners
cross-capability command/query/event contract
new affected capability
task expands beyond initially inspected boundary
unresolved authority reference
base/amendment/supersession uncertainty
open/promoted DDR relevance
material Lexicon ambiguity
transaction-boundary consequence
concurrency consequence
idempotency/retry consequence
historical-affinity consequence
security/authorisation/trust consequence
Exposure/projection consequence
provider-responsibility consequence
persistence consequence affecting business truth/history
accepted-authority versus implementation conflict
accepted-authority versus test conflict
repository evidence contradicting assumed task scope
behaviour required that current authority does not explicitly determine
```

Widening SHALL retrieve additional current authority/evidence.

It SHALL NOT select a plausible answer.

### 55.17 Stop Conditions

If widening reveals:

```text
AMBIGUOUS_AUTHORITY
CONTRADICTORY_AUTHORITY
UNDERSPECIFIED_SEMANTICS
MISSING_INVARIANT
MISSING_CONTRACT
MISSING_FAILURE_BEHAVIOUR
MISSING_OWNERSHIP
MISSING_CONSISTENCY_RULE
MISSING_ARCHITECTURAL_FIT
```

the affected implementation path SHALL stop under the existing `DESIGN_ESCALATION` rules.

Independent READY work may continue where its authority remains sufficient.

### 55.18 Context Reuse

Current authority/evidence already inspected during the same continuous agent run MAY be reused without ritualistic re-reading when:

```text
source remains unchanged
+
task remains within established scope
+
no widening trigger has appeared
+
the agent can still identify the exact governing provision
```

Repeated reading solely because a new implementation subtask starts is unnecessary.

Context reuse is optimisation only. It never changes authority.

### 55.19 Freshness and Invalidation

Previously inspected context becomes invalid for affected reasoning when:

- repository branch changes;
- a governing authority changes;
- `AUTHORITY-INDEX.md` changes materially to the applicable authority graph;
- implementation programme state changes in a way affecting eligibility;
- intervening repository work changes the relevant evidence;
- task scope expands materially;
- a previous assumption is contradicted;
- an exact governing provision can no longer be determined confidently; or
- an authority/evidence freshness reason under existing Implementation Rules arises.

Invalidation SHALL trigger rereading of the affected current sources.

It does not automatically require rereading unrelated governance.

### 55.20 Context Loss / Compaction Safety

The mechanism MUST NOT depend on detecting a particular model implementation's context-compaction event.

Whenever an agent can no longer establish the exact governing provision required for a material decision, reread that current provision.

If it cannot determine which provision is required, widen authority retrieval.

If authority remains uncertain, `DESIGN_ESCALATION`.

Thus context loss fails toward retrieval rather than semantic reconstruction from memory.

### 55.21 Pre-Commit Freshness Gate

Before completing a material implementation cycle, the agent SHALL verify that:

1. it remains on the instructed branch;
2. applicable accepted authority has not materially changed since it was relied upon;
3. applicable current implementation evidence has not been invalidated;
4. widening triggers discovered during implementation were resolved;
5. no unresolved design gap was crossed;
6. exact authority traceability remains valid; and
7. required targeted/full verification has passed.

A freshness defect blocks completion until resolved.

### 55.22 No Independent Authority Resolver

No separate repository artefact SHALL be authoritative for deciding which semantic authority governs.

Machine-readable implementation/navigation aids MAY assist retrieval, but:

```text
AUTHORITY-INDEX.md remains authoritative for WHICH authority governs
```

and:

```text
accepted substantive authority remains authoritative for WHAT it means
```

A future retrieval index MUST therefore be a non-authoritative optimisation and requires separate review if introduced.

No such additional resolver is introduced by v1.8.

### 55.23 Existing Implementation Graph

`docs/development/implementation-programme-state.json` remains implementation programme/navigation evidence according to its existing authority boundary.

It MAY assist with current node, dependencies, evidence locations and readiness navigation, but MUST NOT determine semantic meaning or supersession.

No duplicate agent-specific implementation graph shall be introduced.

### 55.24 Operational Compatibility File

The existing root `operational-rules.md` SHALL cease to contain a second long-form operational restatement.

It SHALL be reduced to a short non-authoritative compatibility pointer stating that canonical authority remains under `designs/`, implementation process is governed by `designs/IMPLEMENTATION-RULES.md`, repository coding-agent instructions are in `/AGENTS.md`, and accepted authority prevails over either operational file.

It MUST contain no independent substantive implementation or semantic rules.

### 55.25 Mechanical Adapter Conformance

Repository conformance testing SHALL mechanically verify at least:

```text
/AGENTS.md exists
/AGENTS.md declares itself non-authoritative
/AGENTS.md remains within the 12 KiB limit
no repository AGENTS.override.md exists
no nested repository AGENTS.md exists
canonical governance paths referenced by AGENTS.md exist
operational-rules.md is only a compatibility pointer
```

Mechanical testing MAY verify adapter structure and bounded size.

It MUST NOT claim to prove semantic sufficiency or arbitrary correctness of natural-language authority selection.

Human/agent authority reasoning remains required where meaning is material.

### 55.26 Governance-Change Impact

Whenever a canonical governance/implementation authority changes materially, formalisation SHALL explicitly review whether `AGENTS.md`, the `operational-rules.md` compatibility pointer and agent-instruction conformance tests remain faithful to accepted authority.

No automatic semantic interpretation of the changed governance document is permitted merely to update the adapter.

### 55.27 No Fixed Token-Saving Claim

This authority establishes a context-efficiency architecture.

It does NOT assert a fixed percentage token or monetary saving.

Efficiency SHALL be evaluated empirically through comparable implementation work where usage evidence is available.

Failure to achieve expected savings MAY justify later optimisation, but MUST NOT justify weakening authority-loading safety.

### 55.28 Explicit Non-Goals

v1.8 does NOT introduce:

```text
nested AGENTS files
AGENTS.override.md
LLM authority classifier
vector semantic retrieval
new authority database
new semantic dependency graph
autonomous authority resolver
machine-generated semantic summaries
new capability semantics
new business rules
new merchant behaviour
new agent semantic authority
```

These require separate justification if later proposed.

### 55.29 Trade-Off

Accepted trade-off:

```text
less unconditional context
        +
greater reliance on explicit widening/freshness discipline
```

in exchange for:

```text
lower initial context
lower repeated context retrieval
higher signal-to-noise
better multi-task efficiency
preserved fail-safe authority behaviour
```

Safety is preserved by requiring uncertainty to expand retrieval or stop implementation.

The system deliberately does NOT optimise the irreducibly broad context required for systemic design/governance review.

### 55.30 Falsification Cases

The design MUST preserve these outcomes:

- Local change hides a cross-capability dependency → dependency discovery requires mandatory widening and additional owner authority loading.
- Semantic authority changes during work → affected context is invalidated and current authority reread.
- Agent loses earlier detailed context → exact clause uncertainty requires rereading the exact current clause.
- `AGENTS.md` disagrees with canonical authority → canonical authority wins and `AGENTS.md` is defective.
- A material design problem appears during implementation → `DESIGN_ESCALATION` and the full governed design lifecycle apply.
- Systemic architecture review is requested → Class D and broad applicable corpus apply.
- Multiple implementation tasks occur in one run → stable context may be reused while task-specific authority changes as required.
- Authority completeness cannot be established → widen; if still unresolved, stop.

### 55.31 Acceptance Boundary

This authority establishes only the implementation-agent context-loading and repository-agent-adapter mechanism.

It does not alter any capability semantics, architectural ownership, programme HARD dependency, merchant behaviour, public contract or existing manual design-approval boundary.

### 55.32 Formalisation Effect

Formalisation of v1.8 consists of integrating this authority into the canonical `designs/IMPLEMENTATION-RULES.md`, updating the `MS-IMPLEMENTATION-RULES-001` entry in `designs/AUTHORITY-INDEX.md`, creating the concise root `/AGENTS.md`, reducing `/operational-rules.md` to the compatibility pointer defined above, adding a narrow repository conformance test for mechanically checkable adapter invariants, reviewing Document Governance/DDR/Lexicon impact without changing them absent an actual applicable consequence, and running the applicable repository verification gate.

No amendment to `MS-IMP-001` is created by v1.8. `MS-IMP-001` continues to own the binding macro implementation programme, HARD dependencies, PROGRAMME_GATE ordering and programme completion criteria within its scope.

---

## 56. v2.0 Efficient Dependency-Complete Execution and Live Controller

### 56.1 Behavioural-Slice Rule

Within a READY or IN_PROGRESS node, implementation SHALL use the largest coherent dependency-complete behavioural slice that can be independently prepared, implemented, falsified and checkpointed without unresolved semantic judgement.

A valid slice has explicit accepted behaviour/invariant, a closed material dependency boundary, identified tests, a finite affected implementation surface, identified widening/escalation triggers and independent checkpoint value.

A slice is not defined by file, class, method, package or arbitrary numeric count. Unrelated behaviours MUST NOT be combined merely for throughput.

### 56.2 Prepare-Once Rule

Before executing a slice, resolve once the exact accepted authority/invariant, semantic owners, dependency closure, affected code/test surface, applicable transaction/concurrency/recovery/security/persistence/provider/Exposure implications, verification obligations, non-goals and stop/escalation conditions.

Reuse that preparation while authority, repository inputs and the dependency boundary remain fresh. Do not repeatedly rediscover the same resolved boundary per file.

### 56.3 Exception-Driven Widening

Unexpected semantic owner, contract, persistence, security/trust, transaction/concurrency/idempotency, provider, historical-affinity, Exposure or other material consequences leave ordinary slice execution. If current accepted authority resolves them, rebuild the slice; otherwise the affected path enters `DESIGN_ESCALATION`.

An exception MUST NOT force unrelated deterministic implementation back into file-level cycles.

### 56.4 Resource-Efficiency Invariant

Implementation SHALL minimise repeated reasoning boundaries while retaining exact correctness boundaries:

```text
reason once for one closed behaviour
execute mechanically determined implementation within that boundary
verify the strongest bounded claim once
retain granular evidence mechanically
escalate only exceptional uncertainty
```

Efficiency MUST NOT be achieved by skipping authority, weakening tests, hiding dependencies, combining unrelated behaviour, bypassing `DESIGN_ESCALATION`, omitting required completion gates or downgrading security/persistence/concurrency scrutiny.

### 56.5 Live Controller Contract

`/IMPLEMENTATION.md` SHALL remain a small live execution controller, not a historical diary. History belongs in Git/bounded evidence; dependency/readiness truth belongs in `implementation-programme-state.json`; accepted meaning belongs in accepted authority.

The controller SHOULD remain at or below approximately 12 KiB and MUST NOT exceed 20 KiB without reviewing/removing duplicated or historical material.

### 56.6 Historical `implementation-status.md`

`docs/development/implementation-status.md` ceases to be the mandatory live cycle-closing surface under v2.0 and becomes historical/compatibility implementation-navigation evidence. Its earlier contents and references remain valid historical evidence within their original scope and MUST NOT be mass-rewritten.

Future live navigation uses `/IMPLEMENTATION.md` plus `docs/development/implementation-programme-state.json`.

### 56.7 MS-IMP-001 Relationship

v2.0 changes no MS-IMP-001 macro target meaning, HARD dependency, PROGRAMME_GATE ordering, macro eligibility or completion criterion. MS-IMP-001 continues to govern macro work; these rules govern fine-grained execution.

`smallest coherent READY node` means the smallest independently meaningful implementation responsibility, not the smallest possible source-code edit.

### 56.8 Post-Migration Verification → Implementation Handoff

Implementation remains paused while the GrandRue naming migration or its required post-migration verification/closure is incomplete.

Automatic implementation re-entry occurs only when:

```text
migration execution complete
+
required post-migration verification complete
+
GR-REN-08..11 closure requirements satisfied
+
no unresolved migration defect requiring repair at handoff
+
no unresolved DESIGN_ESCALATION preventing implementation
```

Then:

```text
verified final repository head
        ↓
reconcile implementation-programme-state.json against current tree
        ↓
preserve MS-IMP-001 programme meaning unless evidence requires factual state correction
        ↓
refresh /IMPLEMENTATION.md
        ↓
select eligible READY node
        ↓
prepare dependency-complete behavioural slice
        ↓
resume implementation automatically
```

No separate `continue implementation` prompt is required where MS-IMP-001 and these rules already authorise the READY work.

At successful handoff, the naming migration and its post-migration verification are closed for implementation purposes. `IMPLEMENTATION-RULES.md` MUST NOT reopen them.

A defect discovered after handoff, including one introduced by migration or missed by post-migration verification, is governed by Section 44.1 using current evidence, current accepted authority and the current authoritative source. No new migration leaf, tranche, repair cycle, checkpoint or migration re-entry is created merely because the defect has migration provenance.

Historical migration and post-migration verification artefacts MAY be inspected to establish what happened and where a defect originated, but they do not become repair authority and MUST NOT be rewritten to imply that a later-discovered defect was detected earlier.

Where the later defect exposes a reusable weakness in a current test, conformance rule or checker, that detector MAY be strengthened independently under Section 44.1. This does not rerun or reopen the historical post-migration verification programme.

If post-migration verification returns `FAIL` or a relevant `BLOCKED` outcome before handoff, there is NO implementation handoff. Resolve the migration finding under the then-current migration authority before handoff.

Naming migration changes MUST NOT be interpreted as semantic implementation completion or as authority to change graph meaning.

### 56.9 Historical Compatibility

Implementation evidence produced under earlier accepted IMPLEMENTATION-RULES versions remains valid for the claims it originally established. Historical references to earlier rule versions and the former `implementation-status.md` lifecycle remain historical evidence.

### 56.10 v2.0 Formalisation Effect

v2.0 is integrated into this canonical file. `AUTHORITY-INDEX.md` identifies integrated accepted v2.0. `/IMPLEMENTATION.md` becomes the live controller; `implementation-status.md` becomes historical/compatibility; repository navigation points to the controller plus canonical graph; applicable mechanical conformance checks verify the controller; DDR/Lexicon require no change because no semantic/deferred-decision meaning changes.

No MS-IMP-001 amendment is created.

### 56.11 v2.0 Falsification Preservation

The implementation process MUST reject these failure modes:

- large slice hides undeclared dependency → widen/rebuild boundary;
- targeted slice checks are used as node completion → reject until full applicable gate;
- stale controller conflicts with graph/current evidence → graph/evidence governs and controller is corrected;
- status retirement loses history → retain Git and bounded evidence;
- slice becomes arbitrary huge batch → partition by behavioural/dependency closure and falsifiability;
- migration rename becomes implementation progress → reconcile factual paths only; preserve programme meaning;
- unresolved migration defect known before handoff → block implementation handoff; later-discovered defect after handoff → apply Section 44.1 from source without reopening migration;
- security/persistence/concurrency/provider/design uncertainty is treated as routine → widen/escalate;
- stale full-suite result is reused after invalidating changes → rerun applicable gate.

### 56.12 Final Governing Rule

> **Implement by the smallest meaningful READY responsibility, but execute that responsibility through dependency-complete behavioural slices rather than file-level cycles. Prepare resolved decisions once, use tests first, apply the minimum complete implementation needed for the slice, verify proportionally at checkpoints, require the full applicable gate before node completion, keep the canonical graph machine-readable and `/IMPLEMENTATION.md` small and restartable, and preserve historical evidence without duplicating it. After successful migration handoff, migration remains closed: later defects are corrected from their authoritative source using current evidence and accepted authority, with reusable detector gaps hardened independently where justified. Explicit imports remain the default; wildcard imports are permitted only when they expose a cohesive vocabulary, materially improve clarity and preserve unambiguous dependency provenance. Efficiency never authorises semantic inference, hidden dependency, weakened verification or bypass of a design gate.**
