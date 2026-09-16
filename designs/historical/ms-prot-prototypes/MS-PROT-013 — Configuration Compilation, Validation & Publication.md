> **Governance note (21 August 2026):** This proposed document is retained as research evidence. MS-PROT-022 v1.2 is authoritative for the resolved model, immutable configuration release, activation and continuity contracts. Terms below such as `ConfigurationArtifact` must be read as historical precursors rather than a competing runtime authority.

MS-PROT-013 — Configuration Compilation, Validation & Publication

Status: Proposed
Purpose: Define the deterministic pipeline that converts Main Street's AI-inferred semantic configuration into an approved, versioned, executable configuration.

This specification establishes a critical principle:

> AI proposes. The configuration system proves. The merchant approves. The runtime executes.



AI is therefore never the final authority over merchant operations.


---

1. The complete pipeline

Merchant Business Understanding
            │
            ▼
      AI Inference
            │
            ▼
    Candidate Configuration
            │
            ▼
       NORMALISATION
            │
            ▼
      SEMANTIC RESOLUTION
            │
            ▼
        TYPE CHECK
            │
            ▼
    DEPENDENCY VALIDATION
            │
            ▼
      CONFLICT VALIDATION
            │
            ▼
      INVARIANT VALIDATION
            │
            ▼
    OPERATIONAL SIMULATION
            │
            ▼
      CONFIGURATION ARTIFACT
            │
            ▼
       MERCHANT REVIEW
            │
            ▼
          APPROVAL
            │
            ▼
         PUBLICATION
            │
            ▼
        VERSIONED RUNTIME

Each stage has a defined responsibility.


---

2. Stage 1 — Business Understanding

This begins with the onboarding information we have already agreed on.

The merchant answers structured questions.

Main Street derives a business description.

The merchant approves it.

For example:

Merchant:
Independent mobile vehicle servicing business

Approved description:
Provides scheduled mobile vehicle maintenance
at customer-selected locations.

This description becomes an input to configuration inference.


---

3. AI does not directly configure runtime

The AI may infer:

SERVICE
MOBILE_SERVICE
SCHEDULING
VEHICLE_CONTEXT
LOCATION_CONTEXT
PAYMENT

But it cannot produce:

arbitrary executable code

or directly modify production configuration.


---

4. Stage 2 — Candidate Configuration

AI produces a candidate semantic configuration.

Conceptually:

CandidateConfiguration
{
    framework: SERVICE_BOOKING

    capabilities:
        SCHEDULING
        MOBILE_SERVICE
        PAYMENT

    requirements:
        VEHICLE_REGISTRATION
        SERVICE_LOCATION

    policies:
        PAYMENT_REQUIRED
}

This is an assertion:

> "These constructs appear to describe this merchant."



It is not yet valid.


---

5. Stage 3 — Normalisation

Different AI agents might express the same concept differently.

For example:

"mobile servicing"
mobile_service
MOBILE_SERVICE

The normaliser maps these to the canonical semantic identifier:

MOBILE_SERVICE

The system therefore operates on a controlled vocabulary.


---

6. Normalisation must be deterministic

The AI may be probabilistic.

The normaliser cannot be.

Once the candidate configuration enters the compiler:

same input
    ↓
same normalised representation

This is essential for reproducibility.


---

7. Stage 4 — Semantic Resolution

The compiler resolves references.

For example:

SERVICE_BOOKING

may resolve to:

Framework ID
Required states
Allowed transitions
Supported policies
Required capabilities
Applicable requirements
Applicable events

The AI doesn't need to know every underlying dependency.

The framework registry does.


---

8. Framework registry

Main Street therefore needs a controlled registry containing definitions such as:

Framework
Capability
Primitive
Policy
Rule
Requirement
Event
State
Invariant
Version

Conceptually:

Semantic Registry
       │
       ├── Frameworks
       ├── Capabilities
       ├── Policies
       ├── Rules
       ├── Data Concepts
       ├── State Models
       └── Invariants

This registry is part of Main Street's internal intellectual property.


---

9. Stage 5 — Type checking

Every semantic construct has a type.

For example:

SERVICE_BOOKING

is a:

FRAMEWORK

while:

PAYMENT_REQUIRED

is a:

POLICY

and:

VEHICLE_REGISTRATION

is a:

DATA_CONCEPT

The compiler rejects invalid compositions.

Example:

SERVICE_BOOKING
+
VEHICLE_REGISTRATION

may be valid.

But something like:

VEHICLE_REGISTRATION
+
CANCELLATION_RULE

without a framework context should not automatically become meaningful.


---

10. Stage 6 — Dependency validation

The compiler expands the dependency graph.

Example:

MOBILE_SERVICE

may require:

SERVICE_LOCATION

and:

SERVICE_BOOKING

may require:

SCHEDULING

The compiler resolves these dependencies.


---

11. Explicit versus inferred dependencies

This distinction matters.

Suppose AI selects:

MOBILE_SERVICE

and the framework defines:

MOBILE_SERVICE
→ SERVICE_LOCATION

The compiler automatically includes the requirement.

This is framework inference, not AI invention.

That keeps the behaviour deterministic.


---

12. Missing dependencies

There are three possible situations.

A. Automatically resolvable

MOBILE_SERVICE
→ SERVICE_LOCATION

Compiler resolves it.

B. Merchant decision required

DELIVERY
→ delivery method?

If several valid methods exist, ask the merchant.

C. Unsupported

If no valid composition exists:

Configuration rejected.

The system must not improvise.


---

13. Stage 7 — Conflict validation

The compiler checks for contradictions.

Example:

PAYMENT_REQUIRED
PAYMENT_NOT_REQUIRED

Result:

CONFLICT

Another example:

CUSTOMER_SELECTS_RESOURCE

with:

RESOURCE_SELECTION_DISABLED

Result:

CONFLICT


---

14. Conflict classes

We should standardise these.

DIRECT_CONFLICT
DEPENDENCY_CONFLICT
TYPE_CONFLICT
STATE_CONFLICT
CAPABILITY_CONFLICT
POLICY_CONFLICT
RESOURCE_CONFLICT
SECURITY_CONFLICT
LIFECYCLE_CONFLICT

This allows meaningful diagnostics.


---

15. Stage 8 — Invariant validation

The compiler then checks fundamental Main Street invariants.

Examples:

A commitment cannot reference an invalid offering.

An operation cannot transition into an undefined state.

An allocation cannot exceed available capacity.

An unauthorised actor cannot execute a protected operation.

An unpublished configuration cannot drive production.

Operational semantics cannot be modified by UI configuration.

These are not merchant preferences.

They are system constraints.


---

16. Stage 9 — Operational simulation

This is the most important validation stage after compilation.

We should create a semantic simulator.

It takes:

Compiled Configuration

and executes representative scenarios.

For a booking framework:

create request
→ collect requirements
→ check availability
→ allocate
→ confirm
→ modify
→ cancel
→ release allocation


---

17. Why simulation matters

A configuration can be logically valid but operationally broken.

For example:

Booking enabled
Capacity enabled
Scheduling enabled

may individually be valid.

But their composition might produce:

booking succeeds
↓
capacity isn't released after cancellation

The simulator can expose this before publication.


---

18. Scenario library

Each framework should have mandatory scenarios.

For example:

SERVICE_BOOKING

successful booking
unavailable slot
customer abandonment
payment failure
booking cancellation
booking modification
capacity exhaustion
concurrent booking

COMMERCE_ORDER

successful order
inventory exhaustion
payment failure
partial availability
cancellation
fulfilment

ACCOMMODATION_STAY

successful booking
room unavailable
date modification
cancellation
payment failure
concurrent booking


---

19. Concurrency must be part of simulation

This directly addresses the race-condition concern we've been discussing.

For example:

Customer A → requests 14:00
Customer B → requests 14:00

The simulator should test concurrent execution.

Required invariant:

available capacity = 1

successful commitments ≤ 1

The system must not rely on UI behaviour to enforce this.


---

20. Simulation is not production execution

The simulator must use:

deterministic test resources

not live merchant resources.

It proves:

> "This configuration is internally coherent."



It does not make a real booking.


---

21. Stage 10 — Configuration Artifact

Once validation passes, the compiler creates a versioned artifact.

Conceptually:

ConfigurationArtifact
{
    merchant_id
    configuration_id
    version
    semantic_version
    frameworks
    capabilities
    policies
    requirements
    state_models
    rules
    invariants
    dependencies
    generated_at
}

The runtime consumes this artifact.


---

22. Immutable artifacts

Once published:

Configuration v17

must be immutable.

Changing it produces:

Configuration v18

This gives us:

auditability;

rollback;

reproducibility;

historical interpretation;

safer deployment.



---

23. Merchant review

The merchant should not review the internal semantic graph.

That would violate our original objective of simplicity.

Instead, Main Street translates the configuration into merchant-readable decisions.

For example:

> How should customers book your services?



● Customers choose an available appointment time

> Do customers need to pay before their booking is confirmed?



● Yes

> What information is needed?



✓ Vehicle registration
✓ Service location


---

24. Merchant review is semantic approval

The merchant is approving the meaning, not the implementation.

They are not seeing:

Capability ID: CAP-087
Rule graph: ...
Transition T14...

They see:

> "Customers must provide their vehicle registration when booking."




---

25. AI-generated description remains separate

We must preserve our previous decision.

AI can generate:

Business description

for merchant verification.

It can also infer:

configuration

but configuration inference is not treated as prose generation.

Therefore:

AI description
→ merchant approval

AI configuration inference
→ compiler
→ validation
→ merchant-readable configuration
→ merchant approval


---

26. Why two approvals?

Because they answer different questions.

Description approval

> "Does Main Street understand my business correctly?"



Configuration approval

> "Does Main Street intend to operate my business this way?"



Both are necessary.


---

27. Configuration changes later

Suppose merchant changes:

Payment required = YES

to:

Payment required = NO

The same pipeline applies:

Draft
→ compile
→ validate
→ simulate
→ merchant approval
→ publish

There is no special bypass merely because the merchant is already onboarded.


---

28. Safe configuration deployment

Publication should be atomic.

Conceptually:

Current:
v17

New:
v18

At publication:

runtime
   ↓
switches
   ↓
v18

There must never be a half-published configuration.


---

29. Existing operations

A configuration change must not corrupt existing operations.

Therefore:

New operations
→ v18

Existing commitments
→ remain governed by appropriate historical configuration

The exact migration rules will be defined later.


---

30. Rollback

If a published configuration causes a problem:

v18
 ↓
rollback
 ↓
v17

Because v17 is immutable, rollback is deterministic.


---

31. Configuration provenance

Every artifact should record:

source:
    onboarding
    merchant change
    AI inference

agent:
    agent identifier/version

semantic registry:
    registry version

compiler:
    compiler version

approved by:
    merchant

published at:
    timestamp

This gives us an audit trail.


---

32. Specialist agents

This architecture now makes specialist agents much safer.

For example:

Business Understanding Agent
        ↓
Business Model

Operations Agent
        ↓
Operational Candidate Configuration

Policy Agent
        ↓
Policy Candidate

UI Agent
        ↓
UI Candidate

Each agent has a narrow semantic contract.


---

33. Agents cannot bypass the compiler

Even if an agent is extremely capable:

Agent
 ↓
Candidate
 ↓
Compiler

always.

There is no:

Agent → production database

path.

This should be a hard architectural boundary.


---

34. Compiler versus AI

This gives Main Street an excellent division of labour:

AI	Compiler

Understands business language	Validates semantics
Infers intent	Resolves dependencies
Selects constructs	Detects conflicts
Handles ambiguity	Enforces invariants
Suggests configuration	Compiles artifact
Explains configuration	Guarantees structure


AI handles interpretation.

The compiler handles truth.


---

35. The "Scratch" analogy is now complete

Your original idea can now be expressed technically:

PRIMITIVES
                        │
                        ▼
                 ABSTRACTIONS
                        │
                        ▼
                  CAPABILITIES
                        │
                        ▼
                   FRAMEWORKS
                        │
                        ▼
                    POLICIES
                        │
                        ▼
                 CONFIGURATION
                        │
                        ▼
                     RUNTIME

The merchant doesn't manipulate the blocks directly.

AI assembles them.

The compiler checks that the blocks form a valid structure.

The merchant approves the resulting behaviour.


---

36. What we should NOT build

This specification rules out several tempting architectures.

❌ AI-generated executable workflows

Too unpredictable.

❌ Merchant-authored rules

Too complex.

❌ One giant universal rules engine

Too difficult to reason about.

❌ One configuration schema for every conceivable business

Creates unnecessary complexity.

❌ Separate hard-coded application for every niche

Destroys the purpose of Main Street.

❌ UI-driven business logic

Creates coupling between presentation and operations.


---

37. What we ARE building

Finite semantic vocabulary
        +
Composable frameworks
        +
Controlled merchant policies
        +
Deterministic compiler
        +
Framework-specific simulation
        +
Versioned configuration
        +
Specialist AI inference
        +
Simple merchant approval

This is the architecture I would accept as the foundation for the prototype.


---

38. MS-PROT-013 decisions

Decision	Status

AI produces candidate configuration only	Accepted
Configuration compiler is authoritative	Accepted
Normalisation is deterministic	Accepted
Semantic registry required	Accepted
Dependency resolution required	Accepted
Conflict validation required	Accepted
Invariant validation required	Accepted
Operational simulation required	Accepted
Concurrency scenarios included in simulation	Accepted
Published configuration is immutable	Accepted
Configuration is versioned	Accepted
Merchant approves semantic behaviour, not implementation	Accepted
Configuration changes go through same validation pipeline	Accepted
Publication is atomic	Accepted
Rollback supported	Accepted
Configuration provenance required	Accepted
AI agents cannot bypass compiler	Accepted
UI configuration remains separate	Accepted



---

Next: MS-PROT-014 — Semantic Registry & Type System

This is the next level down, and it is important because we have repeatedly used terms such as:

Primitive
Abstract Primitive
Capability
Framework
Policy
Rule
Requirement
Data Concept
State
Event
Invariant

We now need to define exactly what each one is, what fields it contains, what it can reference, what it cannot reference, and how the registry represents relationships between them.

In other words, we are moving from:

> "These are the architectural concepts."



to:

> "This is the actual type system and ontology from which the prototype can be drawn."



That is the appropriate next step before we start producing the lowest-level detailed drawings.
