# GrandRue Migration Prepared Work Protocol

**Migration:** `MAIN_STREET_TO_GRANDRUE`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Status:** `ACTIVE`  
**Authority class:** non-semantic operational execution aid  
**Canonical migration ledger:** `GRANDRUE-MIGRATION.md`

This file reduces repeated migration discovery by separating decision preparation from bounded execution. It does not create semantic, architecture, programme, implementation, acceptance, verification, or approval authority. `AGENTS.md`, accepted repository authority, and `GRANDRUE-MIGRATION.md` continue to govern where applicable.

The governing execution rule is:

> Prepare the decisions once. Execute exact instructions. Verify preservation. Stop on any mismatch.

The coverage rule is:

> Every discovered migration unit must receive an explicit disposition before the migration can pass its final residual/coverage gate.

---

## 1. Responsibility split

### Preparation

Preparation resolves the questions that would otherwise require the executor to rediscover migration intent:

- exact coherent migration unit;
- live owner and dependency boundary;
- exact owner files and affected production consumers;
- source and destination paths;
- exact permitted edits and expected occurrence counts;
- protected identities and expected residuals;
- branch, expected parent commit, and source blob identities;
- structural verification commands/checks;
- checkpoint/receipt requirements; and
- any widening or design-escalation question raised by `AGENTS.md` or accepted authority.

Preparation may inspect a wider repository boundary than execution. It may prepare more than one independent packet when the evidence is fresh and the packet boundaries are independently closed.

### Execution

Execution applies one `READY` packet. It does not select the migration leaf, discover additional scope, reinterpret protected identities, broaden the write set, or resolve unexpected semantic/compatibility questions.

The executor must stop when the packet ceases to describe the repository exactly.

---

## 2. Packet state machine

Only the following active states are valid:

```text
PREPARATION_REQUIRED
        |
        v
     PREPARED
        |
        | freshness/preflight succeeds
        v
       READY
        |
        | execution begins
        v
   IN_PROGRESS
        |
        | exact transformation + declared verification succeed
        v
ledger checkpoint / receipt
```

`PREPARATION_REQUIRED` means material questions remain unresolved or the dependency boundary has not been enumerated.

`PREPARED` means the decisions are resolved, but execution freshness has not yet been established against the current branch state.

`READY` means the expected branch/parent, declared input blob identities, scope, protected identities, transformations, residual expectations and verification are all explicit and fresh. Only `READY` may be executed.

`IN_PROGRESS` is transient. If any stop condition appears, execution stops and the packet returns to preparation rather than broadening itself.

Completion history is recorded canonically in `GRANDRUE-MIGRATION.md`; this work file must not become a second historical ledger.

---

## 3. Smallest coherent unit

Smaller means the smallest dependency-bounded change whose required live dependencies are fully accounted for. It does not mean the fewest files.

If a package move requires owner files plus production consumers, the owner and all required production consumers form one coherent transformation. Preparation, preflight, mutation and verification are separate stages; they are not automatically separate commits.

A packet must not intentionally leave broken production references between checkpoints.

---

## 4. READY packet contract

A packet is not `READY` if it contains instructions such as `inspect and decide`, `find the remaining consumers`, `rename as appropriate`, unresolved paths, unsupported assumptions, or an open authority question.

Human rationale may explain why the packet is safe. Machine execution data must have one canonical structured record. Do not maintain separate prose and YAML copies of the same executable state.

Minimum record:

```yaml
packet: <migration-leaf-id>
state: READY

repository: swangune/GrandRue
branch: development
expected_parent: <full-commit-sha>

scope:
  owners:
    - <path>
  production_consumers:
    - <path>
  permitted_changed_paths:
    - <path>

inputs:
  - path: <path>
    blob_sha: <git-blob-sha>

moves:
  - from: <source-path>
    to: <destination-path>

replacements:
  - path: <path>
    from: <exact-text>
    to: <exact-text>
    expected_count: <integer>

protected:
  exact_strings:
    - <protected-value>
  paths:
    - <path-or-pattern>

expected_residuals:
  - path: <path>
    match: <legacy-or-protected-text>
    reason: <why-it-must-remain>

verification:
  changed_path_set: exact
  moved_owner_presence: required
  old_owner_absence: required
  replacement_counts: exact
  protected_identity_check: required
  residual_check: required
  diff_check: required
  maven_tests: prohibited_unless_separately_authorised
  github_actions: prohibited_unless_separately_authorised

checkpoint:
  code_commit_required: true
  ledger_receipt_required: true
```

The record may contain additional bounded fields when necessary, but additional fields must reduce ambiguity rather than reintroduce open-ended discovery.

---

## 5. Freshness and preflight

Immediately before execution:

1. confirm the active branch is exactly the packet branch;
2. confirm branch HEAD is exactly `expected_parent`;
3. confirm every declared input path has the declared blob SHA;
4. confirm every declared source path exists and every declared destination has the expected precondition;
5. confirm the packet has no unresolved placeholder, assumption, or decision;
6. confirm the authorised write set is complete for the declared coherent unit; and
7. confirm the packet remains compatible with the migration ledger and applicable `AGENTS.md` widening/stop conditions.

Any mismatch invalidates `READY`. Do not repair the packet during execution. Return it to preparation.

A packet prepared against an older repository state may remain logically useful, but it is not executable until refreshed and revalidated.

---

## 6. Execution algorithm

For one `READY` packet:

1. perform the freshness/preflight checks;
2. set packet state to `IN_PROGRESS` for the execution context;
3. apply only the declared moves and exact replacements;
4. require every expected replacement count to match exactly;
5. inspect the complete changed-path set;
6. reject every path outside `permitted_changed_paths`;
7. verify moved owners exist at their destinations and authorised old owner paths are absent;
8. verify protected identities and unrelated content remain unchanged;
9. run only the declared structural/residual checks;
10. run `git diff --check` or equivalent diff-integrity verification;
11. if every declared check succeeds, create the code commit;
12. checkpoint the completed leaf in `GRANDRUE-MIGRATION.md`; and
13. publish the ledger receipt as a descendant of the code commit when the existing checkpoint format requires a separate receipt commit.

Execution must not widen because an additional file appears convenient or likely relevant.

---

## 7. Mandatory stop / return-to-preparation conditions

Stop the packet when any of these occurs:

- branch HEAD differs from `expected_parent`;
- a declared input blob SHA differs;
- a source or destination precondition differs;
- an exact replacement count differs;
- a changed path appears outside the permitted changed-path set;
- an undeclared live production dependency is discovered;
- a protected identity would need to change;
- a persistence, schema, protocol, stable external identity, historical evidence, runtime-data, compatibility, or semantic behaviour question appears;
- repository evidence contradicts the prepared dependency boundary;
- an `AGENTS.md` mandatory-widening trigger appears;
- accepted authority is ambiguous, contradictory or underspecified for the discovered consequence;
- declared verification fails in a way that suggests the packet scope/assumption is wrong; or
- execution would require judgement not already resolved by the packet.

Use `DESIGN_ESCALATION` where `AGENTS.md` / applicable accepted authority requires it. Otherwise return the affected packet to migration preparation. Independent `READY` packets may continue only when their boundaries and authority remain unaffected.

---

## 8. Preservation requirements

### Document preservation

Do not reconstruct `GRANDRUE-MIGRATION.md` from summaries, truncated responses or remembered content. Build a ledger change from the exact current repository file and verify that existing historical records remain present. A checkpoint must not erase earlier checkpoints, commit references, protected identities, repair notes or programme state.

### Source preservation

Every removed owner path must have its declared destination. The complete code diff must contain only declared moves and edits. Unrelated content, file modes and protected strings must remain unchanged unless the packet explicitly and validly authorises otherwise.

### History preservation

Unexpected branch movement is a stop condition. It does not authorise force-pushing, overwriting intervening work, manufacturing sibling checkpoint/code commits, or improvised lineage reconciliation.

Where a separate ledger receipt is required, preserve linear lineage:

```text
verified parent -> code commit -> ledger receipt commit
```

### Runtime/data preservation

A namespace/name transformation does not authorise database reset, volume deletion, data migration, schema alteration, environment retargeting, or changing the database/application instance used at runtime. Such effects require their own explicit authority and packet scope.

---

## 9. Verification claims

Structural verification may prove only the properties it actually checks: for example exact changed paths, path moves, import/package replacements, protected-string preservation and scoped residual results.

Unless separately authorised and actually executed, it must not be represented as proving:

- Maven compilation/test success;
- integration-test success;
- GitHub Actions success;
- runtime compatibility;
- database compatibility; or
- final migration completion.

Existing ledger states such as `COMPLETE_PENDING_FINAL_VERIFICATION` remain unchanged until their later governed verification gate is satisfied.

---

## 10. Coverage accounting

Coverage exists to prevent a discovered migration unit from disappearing between preparation and final verification. It is not an independent authority catalogue.

Each discovered unit must have exactly one current disposition:

```text
COMPLETE_PENDING_FINAL_VERIFICATION
PREPARATION_REQUIRED
PREPARED
READY
DEFERRED_TO_NAMED_GROUP
EXCLUDED_WITH_REASON
```

The active coverage manifest may be compact, but it must expose whether enumeration is complete. Never report `UNCLASSIFIED = 0` while remaining migration scope has not yet been enumerated.

Final coverage/residual verification requires, at minimum:

```text
enumeration_complete = true
unclassified = 0
prepared = 0
ready = 0
in_progress = 0
unresolved_blocked = 0
```

plus the later migration gates required by `GRANDRUE-MIGRATION.md`.

Current known position at protocol adoption:

```yaml
coverage:
  completed_leaf_records_through: GR-REN-02-01X7
  completed_leaf_count: 30
  remaining_expansion_node: GR-REN-02-01X+
  remaining_enumeration: INCOMPLETE
  unclassified: UNKNOWN_UNTIL_ENUMERATION
```

This intentionally does not guess the number or identity of remaining production leaves.

---

## 11. Active queue

At protocol adoption no new production leaf has been selected or declared `READY`.

| Work item | State | Required next action |
|---|---|---|
| `GR-REN-02-01X+` | `PREPARATION_REQUIRED` | Inspect current live production dependency evidence, enumerate remaining migration units, update coverage, and prepare the first exact bounded packet. |

Protocol-adoption parent: `d3e20efdfa3acda54ed511e8c2f2374d3ae2d2ce`.

The first substantive migration action after adoption is preparation. Production mutation must not begin until an exact packet has passed freshness/preflight and is `READY`.

Until separately authorised:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

---

## 12. Current migration execution state

This section is the current operational pointer. It supersedes the adoption snapshot in sections 10–11 for resume purposes without turning this file into a second historical ledger. Completed-leaf history remains canonical in `GRANDRUE-MIGRATION.md`.

```yaml
coverage:
  completed_leaf_records_through: GR-REN-02-01X48
  completed_leaf_count: 71
  remaining_expansion_node: GR-REN-02-01X+
  remaining_enumeration: INCOMPLETE
  unclassified: UNKNOWN_UNTIL_ENUMERATION

active_packet: null
active_state: PREPARATION_REQUIRED
last_completed_leaf: GR-REN-02-01X48
last_code_commit: 46ca6c5fa54c39ae1de9c2a07e495e34bd886130
next_action: Prepare the next exact bounded production namespace packet from current live dependency evidence, establish freshness, and execute only after it is READY.
```

Test namespace/runtime-coupled changes remain deferred to `GR-REN-03`. Maven tests and GitHub Actions remain prohibited unless separately authorised.
