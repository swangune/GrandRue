MS-PROT-015 — Semantic Graph & Composition Algebra

Status: Proposed
Purpose: Define how Main Street's typed semantic constructs combine, specialise, depend on, constrain, and produce one another.

This specification answers the question that has been underneath our entire design:

> If Main Street is built from semantic building blocks, what are the exact rules for putting those blocks together?



The answer must be deliberately constrained. We do not want to create a general-purpose programming language disguised as a configuration system.


---

1. Core decision

Main Street's semantic model will use a typed directed graph.

NODE = semantic construct

EDGE = permitted semantic relationship

For example:

SERVICE_BOOKING
      │
      ├── requires ──→ SCHEDULING
      │
      ├── supports ──→ PAYMENT_POLICY
      │
      ├── uses ──────→ RESOURCE_ALLOCATION
      │
      └── produces ──→ BOOKING_CONFIRMED

The graph is not arbitrary.

Only registered edge types are permitted.


---

2. The graph has two distinct purposes

We must distinguish:

Definition graph

Describes how Main Street's semantic constructs are designed.

SERVICE_BOOKING
    requires
SCHEDULING

Configuration graph

Describes how a particular merchant uses those constructs.

Merchant A
    enables
SERVICE_BOOKING

Merchant A
    configures
PAYMENT_REQUIRED

This distinction is extremely important.


---

3. The registry graph

The registry contains reusable definitions:

FRAMEWORK
   │
   ├── contains → STATES
   ├── contains → TRANSITIONS
   ├── supports → POLICIES
   ├── requires → CAPABILITIES
   ├── uses → RESOURCES
   └── produces → EVENTS

This graph is platform-owned.


---

4. The merchant configuration graph

A merchant configuration references registry nodes:

MERCHANT
   │
   ├── activates → FRAMEWORK
   ├── enables → CAPABILITY
   ├── selects → POLICY
   └── provides → DATA

The merchant does not create the underlying nodes.


---

5. The fundamental edge types

The initial composition algebra should contain only a small number of relationships:

CONTAINS
REQUIRES
DEPENDS_ON
SUPPORTS
USES
ACTIVATES
CONFIGURES
REFINES
SPECIALISES
PRODUCES
CONSUMES
TRANSITIONS_TO
CONFLICTS_WITH
CONSTRAINED_BY

We should not create an edge merely because it sounds semantically useful.

Every new edge increases the complexity of the graph.


---

6. CONTAINS

Means:

> A is structurally composed of B.



Example:

SERVICE_BOOKING
    CONTAINS
        BookingStateModel

It does not necessarily mean runtime ownership.


---

7. REQUIRES

Means:

> A cannot operate correctly without B.



Example:

SERVICE_BOOKING
    REQUIRES
        SCHEDULING

If the requirement cannot be resolved, compilation fails.


---

8. DEPENDS_ON

This is slightly weaker.

It means:

> A's implementation or interpretation relies on B.



For example:

PAYMENT_POLICY
    DEPENDS_ON
        PAYMENT_CAPABILITY

A dependency can be transitive.


---

9. SUPPORTS

Means:

> A framework permits B to be used within its semantic boundaries.



For example:

SERVICE_BOOKING
    SUPPORTS
        PAYMENT_POLICY

This does not mean payment is automatically enabled.

That distinction matters.


---

10. ACTIVATES

This is primarily a configuration relationship.

Merchant
    ACTIVATES
        SERVICE_BOOKING

The framework becomes available to that merchant.


---

11. CONFIGURES

Used when a merchant selects a permitted policy value.

Merchant
    CONFIGURES
        PAYMENT_POLICY = BEFORE_COMMITMENT

This is different from ACTIVATES.


---

12. USES

Represents operational use.

SERVICE_BOOKING
    USES
        RESOURCE_ALLOCATION

It says that the framework relies on the abstraction.


---

13. PRODUCES

Represents an output.

CONFIRM_BOOKING
    PRODUCES
        BOOKING_CONFIRMED

The output may be an event, state change or derived artefact depending on the type constraints.


---

14. CONSUMES

The reverse relationship.

CONFIRM_BOOKING
    CONSUMES
        PAYMENT_CONFIRMED

Again, the compiler ensures the source and target types are compatible.


---

15. TRANSITIONS_TO

Only State/Transition structures can use this relationship.

REQUESTED
    TRANSITIONS_TO
CONFIRMED

The transition itself should remain a first-class object because it carries preconditions and effects.


---

16. CONFLICTS_WITH

Represents an explicit incompatibility.

PAYMENT_REQUIRED
    CONFLICTS_WITH
PAYMENT_NOT_REQUIRED

The compiler can reject such combinations immediately.


---

17. CONSTRAINED_BY

Represents a restriction imposed by a higher-level construct.

BOOKING
    CONSTRAINED_BY
CAPACITY_INVARIANT

This is particularly useful for resource allocation.


---

18. REFINES

This is where abstraction becomes powerful.

Suppose:

BOOKING

is a general abstraction.

A framework may refine it:

SERVICE_BOOKING
    REFINES
BOOKING

The specialised framework adds domain-specific semantics while retaining the general contract.


---

19. SPECIALISES

We should distinguish SPECIALISES from REFINES.

Refines

Adds precision to an existing semantic contract.

Specialises

Creates a narrower semantic variant.

Example:

RESOURCE_BOOKING
      ↑
      │ specialises
CAR_WASH_BOOKING

The specialised construct cannot violate the parent contract.


---

20. No arbitrary inheritance

This is a major architectural constraint.

We should not allow:

A extends B

simply because it is convenient.

Inheritance creates hidden coupling.

Instead, specialisation must declare:

what it inherits
what it adds
what it constrains
what remains compatible


---

21. Substitution rule

If:

CAR_WASH_BOOKING

specialises:

SERVICE_BOOKING

then wherever the parent contract is expected, the specialised construct must remain semantically valid.

This gives us a form of substitutability.


---

22. Composition operators

We can now define conceptual operators.

Sequence

A → B

A produces a condition consumed by B.

Example:

PAYMENT_CONFIRMED
    →
CONFIRM_BOOKING

Composition

A + B

Both capabilities coexist.

Requirement

A requires B

Refinement

A ⊑ B

A is a refinement of B.

Conflict

A ⊥ B

A and B cannot coexist under the same applicable context.

These are conceptual algebraic relationships, not necessarily syntax exposed to agents.


---

23. Composition must be typed

We cannot simply allow:

A + B

for every pair.

The compiler must ask:

Is A composable with B?

using the registry's declared compatibility rules.


---

24. Example: valid composition

SERVICE_BOOKING
+
SCHEDULING
+
PAYMENT
+
RESOURCE_ALLOCATION

Potentially valid because the framework declares those relationships.


---

25. Example: invalid composition

PROPERTY_VIEWING
+
INVENTORY_DECREMENT

There may be no semantic relationship between them.

The compiler rejects the composition unless an approved framework explicitly defines one.


---

26. Composition is contextual

This is important.

A construct may be valid in one context and invalid in another.

For example:

PAYMENT

is broadly useful.

But:

PAYMENT
+
PROPERTY_VIEWING

doesn't by itself define what payment means.

The applicable framework determines:

when payment occurs;

what it represents;

what happens after payment.



---

27. Framework as semantic context

This leads to a critical design principle:

> Capabilities provide reusable semantic building blocks; frameworks provide the context that gives those building blocks operational meaning.



Therefore:

Capability ≠ Framework


---

28. Why this solves the grocery/spare-parts problem

Earlier we established that:

> Grocery stores and spare-parts dealers are both traders, but their operational semantics differ.



Our graph handles this naturally.

TRADING
   │
   ├── specialises → GROCERY_COMMERCE
   │
   └── specialises → SPARE_PARTS_COMMERCE

Both share:

ORDERING
PAYMENT
INVENTORY

but each framework adds its own domain requirements.


---

29. Consultation example

Similarly:

CONSULTATION
   │
   ├── specialises → STRUCTURAL_ENGINEERING_CONSULTATION
   │
   └── specialises → LEGAL_CONSULTATION

Both may use:

SCHEDULING
DOCUMENT_COLLECTION
PAYMENT

but their requirements differ.

For example:

STRUCTURAL_ENGINEERING
    → PROJECT_INFORMATION
    → SITE_INFORMATION

while:

LEGAL_CONSULTATION
    → CLIENT_INFORMATION
    → MATTER_INFORMATION

The common semantic substrate is reused without pretending that the businesses are identical.


---

30. This is the abstraction mechanism we wanted

We can therefore represent business semantics as:

GENERIC
   ↓
DOMAIN
   ↓
NICHE
   ↓
MERCHANT

For example:

TRADING
   ↓
COMMERCE
   ↓
GROCERY
   ↓
Merchant-specific configuration

or:

CONSULTATION
   ↓
PROFESSIONAL_CONSULTATION
   ↓
STRUCTURAL_ENGINEERING
   ↓
Merchant-specific configuration


---

31. Avoiding combinatorial explosion

This is where we must be careful.

Suppose we have:

10 capabilities
10 policies
10 resources
10 requirements

Naively combining them could produce thousands of possible configurations.

We must not pre-create every combination.

Instead:

Registry primitives
        ↓
Framework constraints
        ↓
Compiler
        ↓
Valid composition

Only valid combinations need to exist.


---

32. Framework constraints act as boundaries

For example:

GROCERY_COMMERCE

may support:

ORDERING
INVENTORY
PAYMENT
DELIVERY

but not:

ROOM_ALLOCATION

Therefore the compiler doesn't need to understand every possible combination.

The framework establishes the legal region of the graph.


---

33. Semantic graph as a constrained search space

AI is effectively solving:

Find configuration C

such that:

C satisfies business understanding
AND
C uses registered constructs
AND
C satisfies framework constraints
AND
C satisfies dependencies
AND
C satisfies invariants
AND
C passes simulation

This is a much safer problem than:

> "AI, design the merchant's software."




---

34. AI therefore becomes a semantic search engine

This is perhaps the cleanest description of the AI role.

Business description
       ↓
Semantic retrieval
       ↓
Candidate graph
       ↓
Composition
       ↓
Compiler validation

AI is searching/composing within a known semantic space.


---

35. The graph should be acyclic where appropriate

Some relationships must not form cycles.

For example:

A REQUIRES B
B REQUIRES A

should be rejected unless the relationship type explicitly supports recursive structures.

Otherwise dependency resolution becomes problematic.


---

36. Cycles are not universally forbidden

We should not impose a blanket DAG requirement.

Some legitimate relationships can be cyclic.

For example:

Event → process → event

may occur at runtime.

Therefore:

> Acyclicity is a property of specific edge types, not necessarily the entire semantic graph.



This is a more precise rule.


---

37. Dependency graph

For REQUIRES and DEPENDS_ON, we should require acyclicity.

A
 ↓ requires
B
 ↓ requires
C

but not:

A → B → C → A

unless explicitly marked as a supported recursive dependency.


---

38. Runtime graph versus definition graph

Another important separation:

Definition Graph

describes what Main Street knows.

Runtime State Graph

describes what is happening now.

For example:

Booking 8342
    REQUESTED
       ↓
    CONFIRMED

should not be confused with:

BOOKING_FRAMEWORK
    supports
REQUESTED → CONFIRMED

The first is data.

The second is semantic definition.


---

39. Merchant configuration graph

There is a third graph:

Merchant
   ↓
activated framework
   ↓
selected policies
   ↓
configured offerings

So Main Street really has three related layers:

1. Semantic Definition Graph
2. Merchant Configuration Graph
3. Runtime State Graph

Keeping them separate is essential.


---

40. The three graphs

SEMANTIC REGISTRY
       │
       │ defines
       ▼
MERCHANT CONFIGURATION
       │
       │ governs
       ▼
RUNTIME STATE

Information flows downward.

Runtime events must not mutate semantic definitions.


---

41. Configuration graph immutability

Once published:

Merchant Configuration v8

is immutable.

A new merchant decision produces:

Merchant Configuration v9

The runtime then operates against v9 for applicable new operations.


---

42. Semantic graph immutability

Registry definitions should also be versioned and effectively immutable.

If we change:

SERVICE_BOOKING@1

we produce:

SERVICE_BOOKING@2

rather than silently changing version 1.


---

43. This gives us reproducibility

We can answer:

> "Why did Main Street behave this way for this transaction?"



By traversing:

Transaction
 ↓
Configuration version
 ↓
Framework version
 ↓
Policy values
 ↓
Rules
 ↓
Semantic registry versions

That is a powerful property for a business-serving platform.


---

44. Merchant customisation remains constrained

The merchant can customise:

POLICY VALUES
OFFERINGS
BUSINESS DATA
SUPPORTED OPTIONS

But cannot arbitrarily modify:

PRIMITIVES
FRAMEWORK RULES
INVARIANTS
STATE MACHINES

unless Main Street explicitly exposes such a capability.


---

45. The merchant still gets enormous flexibility

This might initially seem restrictive.

It isn't.

Because the platform's semantic vocabulary can be expressive.

The merchant may be able to configure:

what is offered
who can request it
when it is available
what information is required
whether payment is needed
how resources are allocated
what cancellations are permitted
how fulfilment works

without ever seeing the machinery beneath it.


---

46. The "95%" principle

Your earlier idea now becomes technically defensible:

> AI performs approximately 95% of configuration inference, while the remaining merchant-controlled decisions are limited to meaningful business choices that cannot safely be inferred with sufficient confidence.



The 95% is not an architectural guarantee or hard-coded target.

It is a product objective.

The compiler remains the safety boundary.


---

47. Ambiguity handling

If AI cannot distinguish between:

PAYMENT_BEFORE_CONFIRMATION

and:

PAYMENT_AFTER_CONFIRMATION

it should not guess.

It generates an ambiguity:

AMBIGUITY:
payment timing unresolved

The merchant receives one simple question.


---

48. This creates a useful optimisation

The merchant does not configure:

100 settings

They answer:

3–10 meaningful questions

because the framework resolves the consequences.

This is precisely the simplification Main Street is trying to achieve.


---

49. Composition invariant

I recommend formally adopting:

> Every composed configuration must be reducible to a finite graph of registered semantic constructs whose relationships are individually type-valid and whose combined graph satisfies all applicable framework constraints and invariants.



This is an important architectural acceptance criterion.


---

50. Another invariant

> No merchant configuration may introduce a semantic relationship that is absent from the registry or explicitly permitted by the applicable framework.



This prevents configuration drift.


---

51. Another invariant

> Specialisation may narrow or extend a semantic contract only where substitutability and declared compatibility are preserved.



This prevents inheritance from becoming a loophole.


---

52. Prototype representation

For the prototype, we don't need a sophisticated graph database.

We can initially represent the registry relationally:

semantic_types
semantic_nodes
semantic_edges
semantic_versions
constraints

Then:

semantic_edges
--------------------------
source_id
edge_type
target_id
version
constraints

This is sufficient to prototype the model.


---

53. Don't prematurely choose a graph database

The semantic model is a graph.

That does not mean:

> "We must use Neo4j."



A relational database can represent this graph perfectly well for the prototype.

The runtime access patterns should determine the storage technology later.


---

54. Prototype compiler

The first compiler can therefore operate approximately as:

Candidate
   ↓
resolve nodes
   ↓
resolve edges
   ↓
validate types
   ↓
resolve dependencies
   ↓
check conflicts
   ↓
check invariants
   ↓
produce compiled graph

This is enough to test the architecture.


---

55. What we have now achieved

The architecture is no longer merely:

> "Let's use AI to configure businesses."



It is now:

BUSINESS
                       │
                       ▼
               AI INTERPRETATION
                       │
                       ▼
              SEMANTIC COMPOSITION
                       │
                       ▼
               TYPED GRAPH
                       │
                       ▼
              DETERMINISTIC COMPILER
                       │
                       ▼
             VALIDATED CONFIGURATION
                       │
                       ▼
                    RUNTIME

That is a real engineering architecture.


---

56. MS-PROT-015 decisions

Decision	Status

Semantic model represented as typed directed graph	Accepted
Registry graph and merchant configuration graph separated	Accepted
Runtime state graph separated from both	Accepted
Small controlled set of relationship types	Accepted
Composition is type-checked	Accepted
Framework defines legal composition context	Accepted
Arbitrary composition prohibited	Accepted
Dependency cycles prohibited for dependency edges	Accepted
Entire graph need not be acyclic	Accepted
Specialisation must preserve compatibility	Accepted
No arbitrary inheritance	Accepted
AI acts as semantic search/composition mechanism	Accepted
AI cannot invent runtime semantic types	Accepted
Merchant customisation constrained to approved configuration surface	Accepted
Configuration graph versioned	Accepted
Registry graph versioned	Accepted
Relational representation acceptable for prototype	Accepted
Graph database not mandated	Accepted



---

We have reached an important boundary

At this point, I would stop adding more abstract semantic layers.

We have now established:

WHAT exists
     ↓
HOW it is typed
     ↓
HOW it relates
     ↓
HOW it composes
     ↓
HOW AI proposes it
     ↓
HOW Main Street validates it
     ↓
HOW merchants approve it

Continuing to invent abstractions now risks exactly the complexity we have been trying to avoid.

Next: MS-PROT-016 — Concrete Prototype Domain Model

This should be different from the previous specifications.

We should now take real Main Street businesses and instantiate the model.

For example:

GROCERY STORE
SPARE-PARTS DEALER
CAR WASH
STRUCTURAL ENGINEER
SOLICITOR
REAL ESTATE REALTOR
MOTEL

For each one we will map:

Business
 → Framework
 → Capabilities
 → Resources
 → Requirements
 → Policies
 → States
 → Transitions
 → Events
 → Customer operations
 → Merchant operations

Then we test whether one semantic model genuinely handles all of them without special-case logic.

That is the next validation gate before we freeze the prototype domain model and begin the detailed architectural drawings.