# MS-PROT-064 — Audit Evidence, Operational Intervention & Administrative Action Model

**Document ID:** MS-PROT-064  
**Version:** 1.0  
**Status:** Accepted  
**Depends on:** MS-PROT-023, MS-PROT-024, MS-PROT-025, MS-PROT-026, MS-PROT-028, MS-PROT-031, MS-PROT-032, MS-PROT-062, MS-PROT-063  
**Purpose:** Define the backend semantic contract for audit evidence, authorised operational intervention, platform-administrative actions, exceptional/break-glass activity, and the boundary preventing operational support from bypassing capability-owned invariants.

---

## 1. Problem

Main Street must preserve trustworthy evidence of materially important actions and decisions while allowing authorised humans and system operators to intervene when normal automated processing cannot complete safely.

Without a strict model, several dangerous ambiguities arise:

- Domain Events may be mistaken for audit evidence;
- audit storage may become a competing source of business truth;
- platform administrators may acquire implicit universal merchant authority;
- manual intervention may become a generic invariant bypass;
- direct database mutation may become an undocumented business-operation interface;
- rejected or security-sensitive attempts may disappear because no domain mutation occurred;
- audit collection may retain excessive sensitive data;
- operational support may mutate process state without invoking the capability that owns the affected business truth.

This document closes those ambiguities.

---

## 2. Governing Principle

> **Audit records preserve evidence about actions, decisions and interventions; they do not own the business facts those actions concern. Manual intervention is an authorised execution path, not an invariant bypass.**

Every authoritative mutation MUST remain attributable to an accepted execution path and its semantic owner.

---

## 3. Canonical Separation

Main Street SHALL distinguish at least the following concepts:

```text
Business Fact
    authoritative domain/configuration/process truth

Domain Event
    committed domain fact published for reaction

Audit Record
    integrity-protected evidence about an action,
    decision, access or intervention

Operational Intervention
    authorised human/system action used to resolve
    an exceptional operational condition

Administrative Action
    explicitly registered platform or merchant
    administration operation

Break-Glass Action
    exceptional, tightly controlled operational
    activity used when ordinary supported paths
    cannot safely resolve an incident
```

None of these terms are interchangeable.

---

## 4. AuditRecord Is Not DomainEvent

A `DomainEvent` represents a committed business fact relevant to domain reaction.

Examples:

```text
AppointmentRescheduled
OrderCancelled
PaymentRecorded
ShipmentDispatched
```

An `AuditRecord` represents evidence about execution or access.

Examples include:

```text
principal attempted operation X
operation X was rejected for reason category Y
configuration revision R2 was approved by principal P
platform administrator revoked session S
manual reconciliation operation was executed
break-glass intervention was authorised and performed
```

A rejected command MAY require an AuditRecord even though it MUST NOT create a DomainEvent for a business fact that did not occur.

---

## 5. Audit Ownership

The Audit subsystem OWNS durable audit evidence.

It MUST NOT own:

- Order state;
- Appointment state;
- Inventory state;
- Payment state;
- Shipment state;
- merchant configuration;
- entitlement state;
- identity relationships;
- process progression owned by a coordinator;
- capability-specific business invariants.

Audit evidence MAY reference authoritative subjects by stable identifiers.

An AuditRecord MUST NOT be treated as the source from which ordinary business state is reconstructed unless a future explicitly accepted design assigns that responsibility.

---

## 6. Auditable Action Classes

The backend SHALL support explicit audit classification. Initial semantic categories are:

```text
EXECUTION_ACCEPTED
EXECUTION_REJECTED
AUTHENTICATION_SECURITY
AUTHORISATION_SECURITY
CONFIGURATION_CHANGE
ADMINISTRATIVE_ACTION
INTEGRATION_RECONCILIATION
BREAK_GLASS_ACTION
DATA_ACCESS
DATA_EXPORT
```

These are evidence classifications. They MUST NOT become business lifecycle states.

A capability MAY define additional audit classifications only where materially distinct evidentiary meaning exists and the addition follows the governing design rules.

---

## 7. What MUST Be Auditable

Audit evidence MUST be produced where accountability, security, compliance, reconciliation or exceptional operational recovery requires durable evidence.

At minimum, the design SHALL support audit evidence for:

1. security-relevant authentication activity;
2. security-relevant authorisation denials;
3. merchant configuration revisions and approvals;
4. platform-administrative actions;
5. exceptional operational interventions;
6. break-glass actions;
7. material provider/integration reconciliation decisions;
8. sensitive data access or export where governing privacy/security authority requires evidence;
9. other high-risk operations explicitly designated by their owning capability.

Not every successful read or routine operation MUST generate durable audit evidence. Audit volume MUST follow an explicit evidentiary requirement rather than indiscriminate logging.

---

## 8. Rejected Operations

A rejected operation MUST NOT create a domain fact representing successful execution.

A rejected operation MAY create audit evidence when the attempt has security, accountability, abuse-detection, compliance or diagnostic value.

Example:

```text
Staff refund attempt
        ↓
Actor Authorisation = false
        ↓
Operation rejected
        ├── no refund mutation
        ├── no RefundCompleted domain event
        └── authorisation-security AuditRecord where required
```

Audit recording MUST NOT convert rejection into business execution.

---

## 9. Minimum Audit Evidence Contract

Where a field is applicable, an AuditRecord SHOULD preserve:

```text
AuditRecord
    auditId
    occurredAt
    principal/origin reference
    execution scope
    merchant scope where applicable
    action / audit category
    subject type/reference where applicable
    outcome
    reason category where applicable
    correlation identifier
    causation identifier where applicable
    channel/origin metadata where relevant
    evidence metadata required by the governing action
```

The exact persistence representation is downstream implementation detail.

Absence of a non-applicable field MUST NOT require fabricated placeholder values.

---

## 10. Data Minimisation

Audit evidence MUST contain only information required for its evidentiary purpose.

Audit records MUST NOT store, merely for convenience:

- passwords;
- raw authentication secrets;
- session tokens;
- private keys;
- full payment credentials;
- provider signing secrets;
- unnecessary full request payloads;
- unnecessary full database snapshots;
- sensitive personal information unrelated to the evidentiary purpose.

Where evidence can be represented by identifier, category, hash/reference, redacted value or bounded metadata, the narrower representation SHOULD be preferred.

Auditability MUST NOT be used to justify uncontrolled data retention.

---

## 11. Audit Integrity

Durable audit evidence MUST be integrity-protected against ordinary mutation.

The semantic default is append-oriented history.

An ordinary merchant, staff member, customer, capability or support workflow MUST NOT rewrite historical audit evidence.

Where audit evidence requires correction, the default model is:

```text
original evidence remains
        +
new correcting/superseding evidence
```

rather than destructive historical replacement.

The exact integrity mechanism is an implementation decision subject to these invariants.

---

## 12. Audit Consistency Boundary

There is no universal rule that every AuditRecord must be committed in the same transaction as every business mutation.

The required consistency follows the evidentiary requirement.

Two semantic classes exist:

### 12.1 Atomic Audit Evidence

Where the action MUST NOT be considered successfully executed unless its audit evidence is durably established, the audit effect MUST participate in the appropriate atomic consistency boundary or an equivalent guarantee that prevents successful execution without required evidence.

Typical candidates include explicitly designated high-risk administrative or break-glass operations.

### 12.2 Durable Post-Commit Audit Evidence

Where audit evidence may safely follow a committed authoritative fact, durable post-commit propagation MAY be used, provided loss is not silently tolerated and delivery follows the accepted persistence/event guarantees.

The owning design MUST state which class applies when the distinction is material.

---

## 13. Manual Intervention

Manual intervention is an authorised execution path.

It MUST NOT mean:

```text
skip validation
force state
ignore ownership
write arbitrary table
bypass entitlement
bypass actor authority
```

The canonical path is:

```text
Human / authorised operator
        ↓
MS-PROT-063 Trusted Execution Context
        ↓
registered intervention/admin operation
        ↓
MS-PROT-062 runtime decision composition
        ↓
capability/application contract
        ↓
normal invariant evaluation
        ↓
authoritative mutation
        ↓
audit evidence
```

If no legitimate intervention operation exists for the required action, implementation MUST NOT invent a bypass. The missing operation is a design gap and MUST be escalated through the Main Street design lifecycle.

---

## 14. Process Intervention

A long-running process coordinator MAY expose explicit intervention operations such as retry, reconcile, cancel, acknowledge or resume only where those operations are semantically valid.

The coordinator MUST NOT directly rewrite business state owned by participating capabilities.

Example:

```text
Process requires manual intervention
        ↓
authorised operator selects ReconcileProviderOutcome
        ↓
coordinator/application orchestration
        ↓
owning capability operation(s)
        ↓
process progression updated from authoritative results
```

A process state MUST NOT be changed to `COMPLETED` merely because an operator wants the process to disappear from an exception queue.

---

## 15. Platform Administration

Platform administration is a distinct authority context.

A platform administrator MAY be authorised for explicitly registered platform operations such as:

- session revocation;
- credential/account security actions;
- merchant-controller access recovery where governed;
- platform configuration administration;
- audit inspection where authorised;
- provider/integration operational management;
- incident-response operations.

Platform-administrator status MUST NOT imply unrestricted authority to mutate merchant business facts.

When a platform administrator must affect merchant-owned business state, the action MUST use an explicit operation governed by the owning capability and applicable runtime rules.

The system MUST NOT represent platform administration by inventing a fake merchant such as `MAIN_STREET` merely to satisfy merchant-scoped code.

---

## 16. Merchant Administration

Merchant administrative authority remains merchant-scoped.

Merchant controller or staff privileges MAY permit administrative operations within that merchant, but those operations remain subject to:

- trusted Merchant Scope;
- current relationship/privilege state;
- commercial entitlement where applicable;
- trust requirements where applicable;
- capability-owned invariants;
- authoritative revalidation where required.

Merchant administrator MUST NOT be interpreted as global platform administrator.

---

## 17. No Universal Override

Main Street MUST NOT introduce a universal semantic mechanism equivalent to:

```text
force = true
bypassValidation = true
ignoreEntitlement = true
adminOverrideEverything = true
```

An exceptional capability-specific action MUST be explicitly modelled with its own authority, invariants and consequences.

The inconvenience of an invariant is not evidence that an override is required.

---

## 18. Entitlement and Intervention

Administrative intervention MUST NOT be used to bypass the accepted Subscription/Entitlement model.

Where an expired, downgraded or cancelled merchant requires residual access to honour an existing commitment, that access MUST come from the accepted residual-management semantics rather than an administrator pretending the merchant is entitled.

If the entitlement model does not permit a legitimately required operation, that is a design issue.

---

## 19. Direct Database Mutation

Direct database mutation is NOT an accepted routine business-operation interface.

Production support, merchant support, AI agents and ordinary administrative tooling MUST NOT perform business operations by directly editing capability-owned persistence.

Where a severe incident requires break-glass data repair outside ordinary supported operations, it MUST be treated as exceptional operational activity subject to Section 20.

A recurring need for direct database repair is evidence of a missing operational capability or design defect and MUST be escalated.

---

## 20. Break-Glass Activity

Break-glass activity is exceptional and MUST NOT become a convenient alternate application interface.

Where break-glass activity is permitted, the operational mechanism MUST support, as applicable:

```text
explicit incident/reference
explicit operator identity
strong/recent authentication where required
narrow authorised scope
reason/justification
bounded duration where applicable
recorded action
integrity/reconciliation verification
post-action review
append-oriented audit evidence
```

Break-glass authority MUST be narrower than unrestricted infrastructure ownership wherever technically possible.

The exact tooling is downstream implementation design.

---

## 21. Configuration Change Evidence

Authoritative merchant configuration remains owned by the configuration lifecycle.

Audit MUST preserve evidence about material configuration change where required, including as applicable:

```text
merchant scope
principal approving the change
previous revision reference
new revision reference
approval time
origin/channel
correlation
```

Audit evidence MUST NOT replace the authoritative configuration revision.

AI-proposed configuration changes remain attributable to the merchant approval and the AI-assisted origin where relevant.

---

## 22. AI-Assisted Administrative Activity

AI MUST NOT acquire unrestricted administrative authority.

Where AI assists an authorised principal:

```text
principal interaction
        ↓
AI intent inference
        ↓
candidate administrative/intervention operation
        ↓
required approval/validation
        ↓
operation executes under attributable delegated context
```

Audit evidence SHOULD preserve AI-assisted origin where materially relevant.

The accountable execution principal MUST remain identifiable.

---

## 23. Provider and Integration Reconciliation

Provider reconciliation MAY require operational intervention where Main Street evidence and provider evidence disagree or delivery state is uncertain.

The reconciliation operation MUST:

1. authenticate/authorise the operator or integration principal;
2. preserve provider evidence separately from Main Street semantic authority;
3. invoke the capability/application operation that owns the resulting Main Street fact;
4. preserve audit evidence of the reconciliation decision;
5. preserve correlation with the relevant process/provider interaction.

Provider dashboards MUST NOT become an undocumented source from which operators directly rewrite Main Street state.

---

## 24. Audit Query and Exposure

Audit evidence is not automatically visible to every actor associated with the merchant.

Audit query/exposure MUST be governed by explicit authorisation and data-exposure rules.

Different audiences MAY receive different projections of audit evidence.

For example:

```text
merchant controller
    may view merchant-relevant operational history

staff
    may view only authorised subset

platform security operator
    may view security-relevant evidence

customer
    does not receive internal audit evidence merely
    because it concerns the customer's transaction
```

Projection/exposure MUST NOT change the underlying AuditRecord.

---

## 25. Retention

Audit retention MUST follow applicable legal, security, operational and privacy requirements.

This document does not establish one universal retention period.

Retention policy MUST distinguish the evidentiary purpose and sensitivity of audit categories where required.

Deletion/expiry required by governing policy MUST occur through controlled lifecycle mechanisms and MUST NOT permit ordinary users to selectively erase accountability evidence.

---

## 26. Correlation and Causation

Audit evidence SHOULD preserve correlation with the execution/process that produced it where materially useful.

Where an AuditRecord concerns a specific command, event, provider interaction or administrative intervention, correlation and causation identifiers SHOULD allow reconstruction of the relevant operational chain without requiring raw sensitive payload retention.

Audit identifiers MUST remain distinct from domain entity identifiers.

---

## 27. Failure Behaviour

Failure to produce required audit evidence MUST NOT be silently ignored.

Behaviour depends on the audit consistency class:

```text
Atomic evidence required
    → authoritative operation must not report success
      without required durable evidence.

Durable post-commit evidence
    → business fact remains committed;
      audit delivery failure becomes an explicit
      recoverable operational condition.
```

Retry MUST preserve idempotency and MUST NOT create misleading duplicate evidence for one logical action.

Where repeated delivery creates multiple physical records, they MUST remain recognisable as evidence of one logical audited action or delivery sequence.

---

## 28. Idempotency

Administrative, reconciliation and intervention operations MUST define idempotency/retry behaviour where duplicate submission is possible.

An operator refreshing a page, retrying after timeout or receiving duplicate provider evidence MUST NOT accidentally apply the same authoritative correction twice.

Audit evidence MUST preserve sufficient identifiers to diagnose retries without turning duplicate transport attempts into false claims of multiple independent business actions.

---

## 29. Concurrency

Manual or administrative intervention MUST NOT disable ordinary concurrency protection.

If authoritative state changes between inspection and intervention execution, the operation MUST revalidate concurrency-sensitive predicates inside the accepted consistency boundary.

An operator's earlier observation does not reserve the right to mutate stale state.

---

## 30. Observability Is Not Audit

Operational logs, metrics and traces are not automatically authoritative AuditRecords.

```text
Logs / metrics / traces
    optimise diagnosis and operation

AuditRecord
    preserves governed accountability evidence
```

The same action MAY generate both observability telemetry and audit evidence, but the retention, integrity, exposure and semantic guarantees MAY differ.

Implementation MUST NOT rely on transient application logs as the sole evidence for an action that requires durable auditability.

---

## 31. Internal Contract

The audit boundary SHALL expose an explicit application-facing contract rather than allowing arbitrary domain modules to write audit tables directly.

Conceptually:

```text
AuditEvidencePort
    record(required evidence)
    query(authorised criteria) where applicable
```

The exact API/ADT is implementation detail.

Capability/application code SHOULD provide semantic evidence metadata; the Audit subsystem owns durable audit representation and integrity behaviour.

---

## 32. Transaction Interaction

Where audit evidence participates atomically with an authoritative mutation, module/persistence design MUST preserve capability ownership and the accepted transaction boundary.

This requirement MUST NOT justify shared mutable domain ownership.

Where cross-module atomic persistence is used in the modular monolith, it remains an implementation mechanism serving an explicit invariant, not evidence that the Audit subsystem owns the business transaction.

---

## 33. Security

Audit and administrative interfaces are security-sensitive.

Implementations MUST enforce:

- least privilege;
- trusted execution context;
- explicit scope;
- secure exposure;
- sensitive-value redaction;
- integrity protection;
- protection against ordinary deletion/mutation;
- attributable administrative activity.

Security controls MUST NOT be deferred merely because the platform is initially deployed as a modular monolith.

---

## 34. Privacy

Auditability does not override privacy principles.

Design and implementation MUST minimise personal data, restrict access, apply appropriate retention and avoid collecting data unrelated to the audit purpose.

Where identity evidence is unnecessary, a stable principal/reference MAY be preferable to duplicated personal attributes.

---

## 35. Administrative UI/API Boundary

An administrative UI or API is a transport surface, not an authority owner.

```text
Admin UI/API
        ↓
Trusted Execution Context
        ↓
Application operation
        ↓
MS-PROT-062 evaluation
        ↓
Capability/platform-admin semantic owner
```

Administrative transport MUST NOT call repositories directly to perform business mutation.

---

## 36. Intervention Discovery

When a process or operation requires human intervention, the system MAY project an intervention task/queue.

That projection MUST identify the supported actions rather than expose arbitrary state editing.

Example:

```text
Payment reconciliation required
    Supported actions:
        RecheckProviderEvidence
        ConfirmKnownProviderOutcome
        EscalateForInvestigation

not:
        Edit payment status
```

The projection is not authoritative process state.

---

## 37. Missing Intervention Operation

If an operator legitimately needs an action that no accepted capability/application contract supports:

```text
operational need
        ↓
no legitimate operation exists
        ↓
DESIGN_ESCALATION
```

Implementation MUST NOT respond by creating a hidden repository mutation, generic override or undocumented script as the permanent solution.

Emergency break-glass repair MAY resolve an immediate incident, but the recurring semantic need MUST still enter the design lifecycle.

---

## 38. State Correction

Correction of erroneous business state MUST preserve the owning capability's semantics.

Where the capability supports a compensating/corrective operation, that operation SHOULD be used.

Historical business facts SHOULD NOT be rewritten merely to make the current state look as though the error never occurred unless the governing semantic authority explicitly requires correction-in-place.

Audit evidence SHOULD preserve the correction chain where material.

---

## 39. Audit Before/After Evidence

Before/after evidence MAY be required for particular configuration or administrative actions.

Where used, it MUST be bounded to the fields necessary to establish the material change.

The system MUST NOT default to serialising entire aggregates into audit records.

Stable revision identifiers or explicit changed-field evidence SHOULD be preferred where sufficient.

---

## 40. Failure Isolation

Audit subsystem degradation MUST be observable and handled according to the required consistency class.

The platform MUST distinguish:

```text
business operation failed

audit propagation delayed

audit persistence unavailable

administrative operation prohibited because
required atomic evidence cannot be guaranteed
```

These outcomes MUST NOT be collapsed into a generic success/failure state that hides evidentiary risk.

---

## 41. Canonical Backend Graph

```text
                    Trusted Execution Context
                              │
                              ▼
                    Application Operation
                              │
                       MS-PROT-062
                              │
                 ┌────────────┴────────────┐
                 │                         │
              REJECT                   PROCEED
                 │                         │
                 ▼                         ▼
       Audit evidence where       Capability / Admin
             required              semantic owner
                                           │
                                           ▼
                                  authoritative result
                                           │
                             ┌─────────────┼─────────────┐
                             ▼             ▼             ▼
                         DomainEvent   AuditRecord   Projection/
                         if relevant   if required   process reaction
```

For manual intervention:

```text
Operational exception
        ↓
Intervention projection/task
        ↓
Authorised human/system principal
        ↓
Registered intervention operation
        ↓
normal runtime decision + invariants
        ↓
authoritative owner
        ↓
audit evidence
```

---

## 42. Explicit Non-Responsibilities

MS-PROT-064 does NOT define:

- business lifecycle rules for individual capabilities;
- merchant staff privileges;
- subscription entitlement semantics;
- authentication mechanisms;
- identity-provider selection;
- provider-specific reconciliation APIs;
- legal retention periods;
- SIEM product selection;
- log/metrics/tracing technology;
- audit database technology;
- WORM/hash-chain technology;
- break-glass tooling;
- concrete Java interfaces/classes;
- administrative UX.

Those are governed elsewhere or remain downstream choices.

---

## 43. Deferred Technology Choices

The following remain implementation/provider decisions unless later evidence demonstrates semantic consequences:

```text
audit persistence engine
append-only storage mechanism
hash chaining / WORM mechanism
SIEM
log aggregation
metrics platform
trace platform
break-glass tooling
administrative console technology
retention automation mechanism
```

Technology MUST satisfy this document's semantic and security constraints.

---

## 44. Hard Invariants

The following invariants are mandatory:

1. `AuditRecord` MUST NOT be treated as `DomainEvent`.
2. Audit evidence MUST NOT become business authority.
3. A rejected operation MUST NOT create a successful business fact.
4. A rejected operation MAY be auditable without a DomainEvent.
5. Administrative status MUST NOT imply unrestricted merchant-domain authority.
6. Manual intervention MUST NOT bypass capability invariants.
7. A generic universal override MUST NOT exist.
8. Direct database mutation MUST NOT be a routine business-operation path.
9. Break-glass activity MUST be exceptional and attributable.
10. Audit evidence MUST be integrity-protected against ordinary mutation.
11. Audit collection MUST obey data minimisation.
12. Required audit failure MUST NOT be silently ignored.
13. Audit consistency MUST follow the evidentiary requirement.
14. Platform administration and merchant administration MUST remain distinct authority contexts.
15. AI-assisted administrative activity MUST remain attributable to an authorised principal and accepted operation.
16. Provider reconciliation MUST NOT make provider evidence the owner of Main Street business semantics.
17. Intervention operations MUST preserve idempotency and concurrency rules where applicable.
18. Observability telemetry MUST NOT be assumed to satisfy durable audit requirements.
19. Audit exposure MUST be explicitly authorised.
20. Missing legitimate intervention semantics MUST trigger design escalation rather than hidden bypass implementation.

---

## 45. Falsification Summary

The model has been tested against:

- unauthorised staff refund attempts;
- cross-customer resource access attempts;
- merchant configuration changes;
- provider-stalled long-running processes;
- platform support/account-security intervention;
- direct production data repair;
- audit persistence failure;
- excessive audit payload collection;
- attempted audit-history rewriting;
- entitlement/residual-management scenarios;
- AI-assisted administration;
- provider reconciliation;
- retry and duplicate intervention;
- stale-state/concurrent intervention.

No tested scenario requires audit to own business state or administrators to bypass capability ownership.

---

## 46. Acceptance Statement

MS-PROT-064 establishes Audit as a dedicated evidence responsibility and operational intervention as a governed execution path.

Main Street therefore supports accountable administration and exceptional recovery without introducing a shadow architecture around its capability model.

The canonical rule is:

> **Observe and preserve evidence centrally where appropriate; mutate business truth only through the semantic owner.**
