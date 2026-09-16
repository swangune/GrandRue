> **Governance note (21 August 2026):** Retained as early technology-selection evidence. Later accepted architecture and the current Maven repository are authoritative where choices below differ; in particular, Gradle is not the current build contract.

Now we move from the semantic design into the physical implementation architecture. We should make the choices practical enough to start coding, while preserving the abstractions we've already established.

1. Prototype 1 technology choices

Given the architecture we've established, I recommend:

Layer	Choice	Reason

Backend	Java + Spring Boot	Strong typing, mature transaction/security ecosystem, good fit for the semantic runtime
Database	PostgreSQL	Transactions, JSONB, relational integrity, indexing
Cache	Redis — only if needed	Don't introduce it until profiling demonstrates a need
Customer web	TypeScript + React	Component model suits generated experiences
Merchant web	TypeScript + React	Same UI technology, different application
Mobile	React Native initially	Reuse experience/client concepts
Desktop	Tauri initially	Reuse web application while retaining desktop capabilities
API	HTTP/JSON	Simple, inspectable, universally supported
Live updates	SSE	Server → client observation without unnecessary complexity
Build	Gradle	Strong Java multi-module support
Testing	JUnit + integration tests	Essential for semantic/runtime correctness
Containers	Docker	Reproducible development environment


The important point is that the technology does not define Main Street's semantics.

It implements them.


---

2. Backend remains a modular monolith

The backend should be physically one deployable application initially.

But internally:

main-street/
│
└── backend/
    │
    ├── identity/
    ├── configuration/
    ├── compiler/
    ├── semantic/
    ├── runtime/
    ├── persistence/
    ├── experience/
    ├── events/
    ├── notification/
    └── api/

These are architectural modules, not separately deployed applications.

This gives us the structural simplicity we wanted without destroying boundaries.


---

3. Dependency direction

The dependency graph matters more than the folder names.

API
                     │
                     ▼
                Application
                     │
          ┌──────────┼──────────┐
          ▼          ▼          ▼
       Runtime   Experience   Identity
          │
          ▼
       Semantic
          │
     ┌────┴────┐
     ▼         ▼
Configuration  Events
     │
     ▼
   Compiler
     │
     ▼
 Persistence

But we should refine this.

The domain/semantic layer must not depend on Spring, PostgreSQL or React.

Frameworks sit around the core.


---

4. A cleaner hexagonal/composite structure

For the backend, I recommend combining:

modular monolith;

domain-driven boundaries;

hexagonal architecture where it provides value;

declarative semantic runtime;

conventional imperative code for infrastructure.


So:

External Interfaces
                    │
             ┌──────┴──────┐
             ▼             ▼
           API          Admin/API
             │
             ▼
       Application Layer
             │
             ▼
       Semantic Runtime
             │
       ┌─────┴─────┐
       ▼           ▼
   Domain Model   Ports
                     │
             ┌───────┼────────┐
             ▼       ▼        ▼
         PostgreSQL Events  Identity

We don't need to force one programming paradigm onto everything.


---

5. Java package/module structure

At implementation level:

ms-backend/
│
├── ms-api/
│
├── ms-application/
│
├── ms-semantic/
│
├── ms-configuration/
│
├── ms-compiler/
│
├── ms-runtime/
│
├── ms-experience/
│
├── ms-identity/
│
├── ms-events/
│
├── ms-persistence/
│
└── ms-bootstrap/

The names are provisional implementation names, but the boundaries are deliberate.


---

6. Semantic module

This is the most protected module.

It contains things such as:

ResourceDefinition
AttributeDefinition
RelationshipDefinition
OperationDefinition
StateDefinition
TransitionDefinition
ConstraintDefinition
EventDefinition
CapabilityDefinition
ExperienceDefinition

It should know nothing about:

HTTP
PostgreSQL
Spring
React
Redis
JWT

That makes the semantic core testable independently.


---

7. Compiler module

The compiler consumes configuration and produces ESM.

ConfigurationCompiler

compile(configuration)
        ↓
CompilationResult

Internally:

SchemaValidator
SemanticValidator
ReferenceResolver
DependencyAnalyzer
Normalizer
EsmGenerator
EsmValidator

These are ordinary Java components.

We do not need a compiler framework.


---

8. Runtime module

The runtime is responsible for execution.

Conceptually:

OperationExecutor
        │
        ├── AuthorizationService
        ├── InputValidator
        ├── ConstraintEvaluator
        ├── StateTransitionEngine
        ├── TransactionCoordinator
        └── EventRecorder

The executor orchestrates.

The individual components remain small.


---

9. Constraint evaluator

This deserves special treatment.

Don't use Java reflection or arbitrary expression evaluation for Prototype 1.

Instead define a controlled AST:

Expression
├── Equals
├── NotEquals
├── GreaterThan
├── LessThan
├── Exists
├── And
├── Or
└── Not

Then:

ConstraintEvaluator.evaluate(
    expression,
    executionContext
)

This gives us predictable semantics and avoids turning configuration into executable code.


---

10. Persistence module

This module owns PostgreSQL interaction.

Initial logical tables:

merchant
merchant_configuration
executable_model
user
merchant_membership
resource_instance
resource_relationship
operation_record
event
outbox_event
audit_record

We should resist creating tables such as:

restaurant
hotel
law_firm
table
room
case

Those belong to merchant configuration/resource definitions.


---

11. JSONB has a specific role

PostgreSQL JSONB is appropriate for:

merchant configuration
ESM representation
flexible resource attributes
event payload

But we still use relational columns for things that need:

identity;

uniqueness;

indexing;

joins;

transactional integrity.


For example:

resource_instance
-----------------
id
merchant_id
resource_type
state
attributes_json
created_at
updated_at

The semantic resource type remains data-driven.


---

12. API module

The API should be thin.

HTTP Request
     ↓
Controller
     ↓
Application Command/Query
     ↓
Runtime

Controllers should not contain business logic.

Bad:

@PostMapping
if (...) {
   updateDatabase();
   sendNotification();
}

Good:

@PostMapping
executeOperation(command)

The runtime owns execution.


---

13. Read and write paths

We should distinguish them.

Write

HTTP
 ↓
Command
 ↓
Operation Runtime
 ↓
Transaction
 ↓
State
 ↓
Event

Read

HTTP
 ↓
Query
 ↓
Read service
 ↓
Persistence
 ↓
Experience/resource representation

We don't need a full CQRS implementation.

We simply maintain a clean conceptual separation.


---

14. Customer website architecture

The customer application should be a generic renderer.

Customer Web
│
├── Experience Loader
├── Router
├── Resource Renderer
├── Form Renderer
├── Operation Executor
├── State Store
└── Event Client

The UI components should consume semantic descriptions rather than domain-specific screens wherever practical.

For example:

Semantic field:
    type = datetime

Renderer:
    DateTimeInput

rather than:

RestaurantBookingDatePicker


---

15. Merchant application architecture

The merchant application has more capabilities:

Merchant App
│
├── Experience Loader
├── Navigation
├── Resource Management
├── Operation UI
├── Configuration UI
├── State Store
├── Event Client
└── Notification Center

The configuration UI is special because it modifies the configuration model rather than operational resources.


---

16. One frontend codebase or three?

For Prototype 1:

One TypeScript/React application family, with separate shells for customer and merchant experiences.

Conceptually:

frontend/
│
├── core/
├── semantic-ui/
├── customer/
├── merchant/
└── shared/

Then later:

Customer Web
Merchant Web
Merchant Mobile
Merchant Desktop

can reuse the semantic UI/client libraries.

We don't need to build all four applications before validating the backend.


---

17. Build order

This is where we need discipline.

Phase A — Backend kernel

Semantic model
Configuration schema
Compiler
ESM
Runtime
Persistence

Phase B — API

Configuration API
Experience API
Operation API
Resource API
Event stream

Phase C — Customer

Experience loader
Resource browsing
Operation forms
Operation execution
Live updates

Phase D — Merchant

Authentication
Dashboard shell
Resource management
Operation management
Configuration management
Live updates

Then integrate the complete vertical slice.


---

18. The first code should NOT be the UI

Even though we now understand the UI boundary, the first executable artifact should be:

Configuration
       ↓
Compiler
       ↓
ESM
       ↓
Runtime
       ↓
Persistence

with automated tests.

Then expose it through HTTP.

Then build the UI.

That gives us a known-good backend contract for the frontend team/code.


---

19. The immediate implementation target

The first implementation milestone should therefore be:

CONFIGURATION → COMPILER → ESM → RUNTIME

with one operation:

booking.create

and one transition:

none → requested

Then:

booking.confirm

with:

requested → confirmed

If that works entirely from declarative configuration, we have experimentally validated the central Main Street thesis.

If it becomes awkward, brittle or requires domain-specific code, we have discovered it early—before building the UI and the rest of the platform.

That is exactly the right next move.
