# MS-PROT-069 — Network Failure, Execution Uncertainty & Acknowledgement Model

**Document ID:** MS-PROT-069  
**Version:** 1.0  
**Status:** Accepted  
**Depends on:** MS-PROT-023, MS-PROT-024, MS-PROT-025, MS-PROT-027, MS-PROT-048, MS-PROT-059, MS-PROT-062, MS-PROT-063, MS-PROT-065, MS-PROT-068  
**Purpose:** Define what Main Street may and may not conclude when communication fails before, during or after authoritative execution, including client/server failure, database commit uncertainty, provider timeout, retry, acknowledgement loss, offline intent and reconnection.

---

## 1. Governing Principle

> **Transport failure is evidence about communication, not proof of business-operation failure.**

Main Street MUST distinguish whether a logical command was durably accepted, whether its authoritative business effect executed, and whether the caller received acknowledgement.

Client-visible failure MUST NOT be treated as authoritative evidence that an operation did not execute.

---

## 2. Canonical Certainty Dimensions

For a logical command, Main Street distinguishes:

```text
Acceptance certainty
    Was the command durably accepted by the authoritative side?

Execution certainty
    Did the authoritative operation produce its business effect?

Acknowledgement certainty
    Did the initiating caller reliably receive the known outcome?
```

These dimensions MUST NOT be collapsed into a single `SUCCESS/FAILED` transport flag where doing so can misrepresent business reality.

---

## 3. Receipt Is Not Durable Acceptance

Receiving bytes, parsing an HTTP request, or beginning application handling does not establish durable acceptance.

> **Durable Acceptance means Main Street has persisted enough authoritative command/work evidence that later execution or outcome resolution does not depend on the originating network connection remaining alive.**

Depending on the use case, durable acceptance may be represented by the committed business transaction itself or durable accepted-work state.

`received ≠ durably accepted`.

---

## 4. Behavioural Outcome Classifications

MS-PROT-069 defines behavioural execution/communication classifications, not capability business lifecycle states:

```text
KNOWN_NOT_ACCEPTED
    authoritative side can establish that no durable acceptance occurred

ACCEPTED_EXECUTION_PENDING
    command/work durably accepted but final business outcome is pending

KNOWN_EXECUTED
    authoritative business effect is known to have occurred

KNOWN_NOT_EXECUTED
    authoritative side knows the effect did not occur

EXECUTION_UNCERTAIN
    authoritative side cannot safely determine whether effect occurred

ACKNOWLEDGEMENT_LOST
    known outcome exists but initiating caller did not receive it

CLIENT_OFFLINE
    intent cannot currently reach authoritative execution
```

Implementations MAY use different names provided these distinctions remain representable.

---

## 5. Logical Command Identity

Transport attempts and business intents are distinct.

```text
one logical business intent
        ↓
one logical command identity
        ↓
one or more transport attempts
```

Repeated transport delivery MUST NOT multiply one logical business intent.

Payload equality MUST NOT be used as the sole definition of duplicate business intent. Two intentionally identical purchases remain two distinct commands if they are separate business intentions.

---

## 6. Idempotent Retry

Where retry of one logical command is supported, the stable logical command/idempotency identity MUST be reused across transport attempts.

If the same retry/idempotency identity is presented with materially different command intent, Main Street MUST reject the mismatch rather than reinterpret it as a valid retry.

A lost acknowledgement MUST NOT cause duplicate authoritative business execution.

Exactly-once network/message delivery MUST NOT be assumed.

Safe business effect is achieved through logical command identity, idempotency, authoritative revalidation and reconciliation where outcome is uncertain.

---

## 7. Known Executed but Unacknowledged

If execution committed but the response was lost:

```text
Execution = KNOWN_EXECUTED
Acknowledgement = LOST
```

A retry/query using the same logical identity MUST resolve the previously accepted result where the governing operation supports result recovery. It MUST NOT create a second business effect.

The implementation MUST preserve sufficient durable identity/result evidence where idempotent recovery requires it.

---

## 8. Known Not Executed

Where Main Street can prove that authoritative execution did not commit or otherwise take effect, the outcome may be `KNOWN_NOT_EXECUTED`.

A subsequent attempt is a new execution decision and MUST re-establish applicable current runtime authority and invariants.

---

## 9. Internal Commit Uncertainty

A database or other authoritative-store connection failure during commit does not necessarily prove rollback.

Examples include connection loss after the store accepted commit but before the application received acknowledgement, failover during commit, or ambiguous transaction outcome.

Where commit outcome cannot be safely established:

```text
EXECUTION_UNCERTAIN
```

MUST be preserved until authoritative reconciliation resolves it.

Main Street MUST NOT blindly repeat a potentially side-effecting authoritative mutation merely because the application observed a connection error.

---

## 10. External Provider Uncertainty

For side-effecting provider interactions:

```text
request sent
    ↓
timeout / reset / response loss
```

MUST NOT automatically mean provider operation failure.

Where the provider may have acted but Main Street lacks reliable evidence:

```text
EXECUTION_UNCERTAIN
    ↓
RECONCILIATION_REQUIRED
```

Blind retry is prohibited unless the provider contract independently guarantees retry safety through idempotency or equivalent semantics.

Provider/network uncertainty classifications MUST NOT become capability business lifecycle states.

---

## 11. Read Failure vs Side-Effect Uncertainty

A failed observation/read and a side-effecting uncertain operation are materially different.

Read retries MAY be handled by resilience policy when safe because no external/business mutation is expected.

Side-effecting uncertainty requires proof of safe retry or reconciliation before repetition.

MS-PROT-069 therefore MUST NOT define one generic retry rule for all network failures.

---

## 12. Client Offline Intent

Client-side local persistence MAY preserve drafts or queued intent where a use case permits it.

Examples may include draft content, draft Product information, draft Announcement or unsent configuration proposal.

Local persistence MUST NOT imply authoritative business commitment.

```text
offline queued intent
    ≠
Order / Booking / Inventory / Payment commitment
```

Operations requiring current authoritative state MUST re-enter normal trusted execution and authoritative revalidation after reconnection.

---

## 13. True Offline Authoritative Mutation Is Separately Governed

MS-PROT-069 does NOT establish general offline-first mutation authority.

True offline authoritative mutation would require separately accepted semantics for local authority, conflict resolution, multi-device divergence, inventory/resource partitioning, payment evidence, sequencing and reconciliation.

Client-side queuing alone MUST NOT be interpreted as permission to claim that authoritative business execution occurred offline.

---

## 14. Multi-Device Conflict

Multiple disconnected merchant devices MAY each retain local intent based on stale state.

On reconnection, each intent MUST revalidate against current authoritative state.

Example:

```text
Inventory = 1 before disconnect
Device A intends sale of final unit
Device B intends sale of final unit
```

Neither local intent may independently become authoritative stock mutation. Current server-side invariant evaluation decides which, if either, succeeds.

The same rule applies to scheduling/resource conflicts.

---

## 15. Authoritative Store Unavailable

Where Main Street cannot establish current authoritative state, authoritative mutation MUST NOT proceed from stale cached state unless a separately accepted authority explicitly permits it.

Default rule:

```text
authoritative state unavailable
    ↓
no authoritative write
```

Read degradation is governed separately by the read model's explicit staleness contract.

---

## 16. Cached and Stale Reads

MS-PROT-069 does not globally authorise cached reads during authority-store/network failure.

Whether a read projection may be served while disconnected from current authority depends on the read model's staleness/exposure contract under MS-PROT-027 and later resilience policy.

Public profile/catalogue content may tolerate different staleness from live inventory, appointment availability or payment status.

---

## 17. Reconnection

After reconnection, any NEW authoritative execution MUST re-establish current Trusted Execution Context under MS-PROT-063 and pass current runtime decision composition under MS-PROT-062.

A retry identity does not preserve expired authentication, authorisation, entitlement, trust satisfaction or unrelated runtime authority.

Resolving the outcome of a previously accepted command remains distinct from authorising new execution.

---

## 18. Previously Accepted Command vs New Execution

This distinction is mandatory:

```text
Resolve previously accepted command
    determine/retrieve existing authoritative outcome

New execution
    attempt business mutation now under current authority/state
```

Later changes to session, entitlement, merchant configuration, provider binding or runtime conditions MUST NOT retroactively rewrite the historical meaning/provenance of an already accepted/committed operation.

If no durable acceptance occurred and execution is attempted anew, current semantics and runtime authority apply.

---

## 19. Configuration and Binding Provenance

Where an accepted operation depends on a configuration/binding release, sufficient provenance MUST be retained so later configuration/provider changes do not silently reinterpret the already accepted operation.

Outcome resolution for an accepted command MUST use the provenance governing that accepted execution where required by existing authorities.

---

## 20. Asynchronous Accepted Work

Durable acceptance does not require immediate terminal execution.

Example:

```text
media upload accepted
    ↓
background processing scheduled
    ↓
ACCEPTED_EXECUTION_PENDING
```

The originating HTTP connection may disappear without invalidating accepted work.

The caller may later resolve status/result using stable identifiers and authorised access.

---

## 21. Background Work and Network Failure

Durable background work under MS-PROT-065 MUST NOT embed raw credentials or rely on the original client connection.

Repeated work-delivery attempts MUST preserve logical work identity and MUST NOT multiply one intended business effect.

Network flapping MAY cause repeated transport delivery but MUST NOT create repeated business intent.

Retry timing/backoff remains a resilience/implementation concern unless semantic consequences require explicit authority.

---

## 22. Late Responses

A delayed original response may arrive after a retry has already resolved the logical operation.

Clients SHOULD reconcile using logical command/result identity rather than assuming the latest-arriving transport response defines business truth.

Multiple responses for one logical command MUST NOT represent multiple business executions.

---

## 23. AI and Other Channels

AI, website, merchant dashboard, POS, telephone-assisted, integration and background channels all obey the same uncertainty semantics.

An AI specialist MUST NOT infer that a timed-out operation failed and automatically repeat it without resolving the logical command outcome or establishing safe retry semantics.

Channel origin MUST NOT create a weaker uncertainty model.

---

## 24. Observability

MS-PROT-068 MAY observe timeout, retry, acknowledgement loss, commit uncertainty, provider uncertainty and reconnection behaviour.

Telemetry MUST NOT itself resolve business uncertainty or become mutation authority.

Loss of observability MUST NOT erase authoritative command/outcome evidence.

---

## 25. Audit

Security-sensitive or administrative resolution/reconciliation MAY require audit evidence under MS-PROT-064.

Audit evidence does not itself establish that a business effect occurred unless the owning authority independently establishes that fact.

---

## 26. Canonical Graph

```text
Logical Business Intent
        ↓
Logical Command Identity
        ↓
Transport Attempt(s)
        ↓
┌──────────────────────────────┐
│  Acceptance certainty        │
│  Execution certainty         │
│  Acknowledgement certainty   │
└──────────────────────────────┘
        ↓
Outcome known?
   │            │
  YES          NO
   │            │
resolve        preserve uncertainty
existing       and reconcile/query
result         authority
   │            │
   └──────┬─────┘
          ↓
NO DUPLICATE BUSINESS EFFECT
```

---

## 27. Hard Invariants

1. Transport failure MUST NOT be interpreted as proof of business-operation failure.
2. Client-visible failure MUST NOT be authoritative evidence that a command did not execute.
3. Receipt MUST remain distinct from durable acceptance.
4. Acceptance, execution and acknowledgement certainty MUST remain distinguishable.
5. Lost acknowledgement MUST NOT cause duplicate business execution.
6. Retries of one logical intent MUST preserve stable logical command identity where idempotent retry is supported.
7. Reuse of one retry/idempotency identity with materially different intent MUST be rejected.
8. Transport attempts MUST NOT be equated with business intents.
9. Exactly-once network/message delivery MUST NOT be assumed.
10. External side-effect uncertainty MUST trigger reconciliation rather than blind retry unless retry safety is independently guaranteed.
11. Internal commit uncertainty MUST likewise be reconciled before potentially duplicating mutation.
12. Offline queued intent MUST NOT become authoritative business truth merely because it is stored locally.
13. True offline authoritative mutation requires a separate accepted design.
14. Authoritative writes MUST NOT proceed from stale cached state when current authority cannot be established, absent separate explicit authority.
15. Read degradation/caching MUST follow the read model's explicit staleness contract.
16. Retry identity MUST NOT preserve expired authentication, authorisation, entitlement or unrelated runtime authority.
17. Resolving a previously accepted command MUST remain distinct from executing a new command.
18. Configuration/binding provenance applicable to an accepted operation MUST NOT be silently rewritten by later changes.
19. Provider/network uncertainty classifications MUST NOT become capability business states.
20. AI, POS, website, dashboard, integration and background channels MUST obey the same uncertainty semantics.
21. Reconnection MUST re-establish current trusted runtime context before any new authoritative execution.
22. Payload equality MUST NOT be used as the sole definition of duplicate business intent.
23. A delayed/duplicate transport response MUST NOT create additional business execution.
24. Telemetry MUST NOT become the authority that resolves execution uncertainty.

---

## 28. Explicit Non-Responsibilities

MS-PROT-069 does NOT select network library, HTTP client, retry/backoff algorithm, timeout values, circuit-breaker library, database transaction implementation, message broker, mobile offline storage technology, conflict-resolution algorithm for true offline authority, or provider-specific reconciliation API.

Those remain downstream or belong to later resilience/offline authorities.

---

## 29. Falsification Summary

The model passed layered review and adversarial falsification across request-never-sent, edge/application failure, commit-before-response-loss, rollback, crash-after-commit, idempotency-key misuse, asynchronous accepted work, provider timeout, read timeout, offline client intent, multi-device contention, appointment collision, delayed original response, repeated button presses, authoritative-store outage, database commit ambiguity/failover, authentication expiry, entitlement/configuration changes during uncertainty, AI-assisted commands, network flapping and observability interaction.

---

## 30. Acceptance Statement

Main Street now has a canonical network-failure and execution-uncertainty model that prevents communication symptoms from being mistaken for business outcomes.

> **First determine what is known about acceptance and execution; preserve uncertainty when proof is unavailable; resolve or reconcile before repeating side effects; and never let lost acknowledgement multiply business intent.**
