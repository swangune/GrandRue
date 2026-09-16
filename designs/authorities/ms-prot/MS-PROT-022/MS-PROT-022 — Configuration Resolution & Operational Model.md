# MS-PROT-022 — Configuration Resolution & Operational Model

**Version:** 1.3  
**Status:** Accepted after review and validation  
**Depends on:** MS-PROT-020, MS-PROT-021  
**Purpose:** Define how a validated merchant configuration and Main Street’s registered semantics are deterministically resolved into the merchant-scoped operational model consumed by runtime.

The central question is:

> **Once Main Street knows which registered semantics a merchant uses and how those semantics are configured, what exactly does runtime execute against?**

The answer must avoid both extremes:

```text
Runtime repeatedly interprets
raw MerchantConfiguration
        ✗
```

and:

```text
Compiler generates a bespoke
merchant application
        ✗
```

The proposed middle layer is the **Resolved Operational Model**.

---

# 1. Governing principle

> **The Resolved Operational Model is an immutable, merchant-scoped, derived representation of already-registered Main Street semantics and validated merchant configuration. It contains no newly invented semantic meaning.**

The pipeline becomes:

```text
Semantic Registry
        +
Validated Merchant Configuration
        │
        ▼
Configuration Resolution
        │
        ▼
Resolved Operational Model
        │
        ▼
Generic Runtime
```

This maintains the authority hierarchy accepted in MS-PROT-020:

```text
Platform semantics
        ↓
Merchant configuration
        ↓
Deterministic resolution
        ↓
Runtime
```

---

# 2. Why a resolved operational model is necessary

We should first prove that this layer deserves to exist.

Without it, runtime could theoretically perform:

```text
receive Command
      ↓
load MerchantConfiguration
      ↓
look up Capability
      ↓
resolve Policy
      ↓
expand dependencies
      ↓
derive Requirements
      ↓
resolve Operation
      ↓
check relationships
      ↓
execute
```

for every command.

That creates three problems.

### Repeated interpretation

Runtime repeatedly performs work that should already have been validated.

### Configuration ambiguity

If configuration changes between two steps, runtime could observe a partially different model.

### Authority leakage

Runtime gradually becomes responsible for interpreting configuration semantics.

That would blur:

```text
compiler/resolver
        ≠
runtime
```

The resolved model removes this problem.

---

# 3. What resolution means

Resolution is:

> **The deterministic process of turning valid semantic references and permitted configuration selections into a coherent merchant-specific operational view.**

It may perform:

```text
reference resolution
dependency closure
policy resolution
requirement applicability resolution
relationship validation
operation binding
resource/offering association
schedule parameter resolution
effective configuration calculation
index construction
```

It may not perform:

```text
semantic invention
business-category inference
AI reasoning
arbitrary workflow generation
runtime execution
customer-data collection
resource allocation
availability decisions
```

---

# 4. Configuration compilation versus resolution

Previous documents have used the word **compiler**.

MS-PROT-022 tightens the terminology.

Conceptually:

```text
CONFIGURATION COMPILATION
        =
validation
+
resolution
+
materialisation
```

The component may still eventually be called:

```text
ConfigurationCompiler
```

in implementation.

But architecture distinguishes its responsibilities.

### Validation

> Is the candidate configuration legal?

### Resolution

> What registered semantics and effective configuration does it imply?

### Materialisation

> Produce the runtime-consumable operational model.

Therefore:

```text
CandidateConfiguration
        ↓
VALIDATE
        ↓
ValidatedConfiguration
        ↓
RESOLVE
        ↓
ResolvedOperationalModel
```

No runtime effects occur during this pipeline.

---

# 5. Resolved model is derived state

This is fundamental.

The sources of truth remain:

```text
Registered semantic definitions
        +
Merchant configuration
```

The resolved model is derived.

Therefore:

```text
ResolvedOperationalModel
        ≠
Semantic Registry
```

and:

```text
ResolvedOperationalModel
        ≠
MerchantConfiguration
```

and:

```text
ResolvedOperationalModel
        ≠
Runtime business state
```

It may be regenerated deterministically from its authoritative inputs.

---

# 6. The resolved model must not duplicate semantic authority

A dangerous implementation would copy semantic definitions into merchant-specific mutable versions.

For example:

```text
Global Scheduling definition
        ↓
copy
        ↓
Merchant A Scheduling definition
        ↓
modified
```

Reject this.

Instead:

```text
Registered Scheduling semantics
          ↑
          │ references
Resolved Merchant Model
          │
          └── effective configuration
```

The operational model may hold resolved references, identifiers, immutable handles or equivalent structures.

It does not redefine the semantic source.

---

# 7. Minimum conceptual model

The smallest useful contract is:

```text
ResolvedOperationalModel
{
    merchant
    source_configuration

    active_capabilities
    resolved_policies

    applicable_operations
    applicable_requirements

    configured_offerings
    configured_resources
    effective_scheduling

    interaction_modes
    exposure_bindings
    notification_bindings
}
```

This is conceptual only.

It is **not** a required Java object layout or database schema.

---

# 8. Merchant scope

Every operational model belongs to exactly one merchant scope.

Conceptually:

```text
ResolvedOperationalModel
        │
        └── merchant = Merchant A
```

It cannot accidentally contain:

```text
Merchant B resource instance
Merchant C offering
Merchant D policy selection
```

Merchant isolation is therefore an invariant of resolution.

The detailed tenancy architecture remains separate, but semantic resolution must never weaken merchant boundaries.

---

# 9. Source configuration traceability

Every resolved model must identify the configuration from which it was produced.

Conceptually:

```text
ResolvedOperationalModel
    source_configuration = configuration-X
```

This does **not** reintroduce semantic-definition versioning rejected in MS-PROT-020.

It provides configuration provenance.

We need to be able to answer:

> Why was this operational behaviour active?

with:

```text
because configuration X
selected registered semantics Y
and policy Z
```

rather than:

> The compiler somehow produced it.

---

# 10. Capability resolution

Suppose the merchant activates:

```text
Scheduling
CustomerAccess
Notification
```

Resolution determines:

```text
directly selected capabilities
        +
required semantic dependencies
        ↓
effective active capability set
```

But dependency closure is allowed only over **registered relationships**.

For example:

```text
Capability A
    REQUIRES
Capability B
```

causes B to participate whenever A is effectively active. `REQUIRES` is a
directed, transitive compiler relationship. The resolver repeatedly expands
registered `REQUIRES` edges until it reaches a fixed point:

```text
merchant-selected seed capabilities
        ↓
transitive REQUIRES closure
        ↓
effective active capability set
```

The first dependency slice assigns precise compiler meaning to the registered
relationship kinds:

| Relationship | Compiler meaning |
| --- | --- |
| `REQUIRES` | Transitively activates the target capability. |
| `CONFLICTS_WITH` | Symmetrically rejects a resolved model containing both endpoints. |
| `SUPPORTS` | Describes compatibility only; it does not activate either endpoint. |
| `DEPENDS_ON` | Has no executable compiler meaning until its distinction from `REQUIRES` is specified. |

A cyclic `REQUIRES` graph is invalid registry semantics. Mutually inseparable
meaning should be represented by an appropriate shared capability/framework
or a future explicitly defined co-requisite relationship, not by circular
activation.

Conflict validation occurs after dependency closure so a derived capability
cannot silently introduce a forbidden combination.

But the resolver cannot decide:

> Businesses like this probably need Capability C.

That belongs to configuration inference before validation.

---

# 11. Explicit and derived activation

We should preserve why a capability is active.

For example:

```text
SCHEDULING
    activation = MERCHANT_SELECTED

TIME_MODEL
    activation reason = REQUIRED_BY(SCHEDULING)
```

The resolved model retains an immutable set of activation reasons for each
effective capability:

```text
MERCHANT_SELECTED
REQUIRED_BY(source capability)
```

Multiple reasons are preserved. If Booking and Consultation both require
Scheduling, removing Booking does not make Scheduling optional while
Consultation remains active.

`REQUIRED_BY_PLATFORM` remains deferred until a concrete platform-profile
authority exists; the compiler must not invent platform-required seeds.

This means resolution can explain:

```text
why is this capability active?
```

without treating all active semantics as though the merchant explicitly selected them.

---

# 12. Policy resolution

Policy resolution combines:

```text
registered PolicyDefinition
        +
applicability
        +
merchant selection or default
        ↓
effective Policy value
```

For example:

```text
ViewingConfirmationMode
allowed:
    AUTOMATIC
    MERCHANT_CONFIRMATION

Merchant selection:
    MERCHANT_CONFIRMATION
```

Resolved model:

```text
effective ViewingConfirmationMode
    = MERCHANT_CONFIRMATION
```

But:

```text
Policy definition
```

continues to belong to the registry.

---

# 13. Absence versus default

Resolution must preserve the distinction accepted in MS-PROT-021:

```text
NOT_APPLICABLE
DEFAULTED
EXPLICITLY_SELECTED
```

These must not collapse into:

```text
null
false
zero
```

unless the semantic policy explicitly defines such a value.

For example:

```text
PaymentTiming
```

for a merchant with no Payment capability is:

```text
NOT_APPLICABLE
```

not:

```text
PaymentTiming = NONE
```

unless `NONE` is a registered supported value.

---

# 14. Requirement applicability resolution

Merchant configuration does not attach Requirements arbitrarily.

Instead:

```text
active semantics
        +
effective policies
        +
registered consequences
        ↓
applicable requirements
```

Example:

```text
Mobile service
        ↓
ServiceLocation Requirement applicable
```

The resolved model can therefore expose:

```text
Operation:
    mobile_service.request

Applicable requirements:
    CustomerContact
    VehicleRegistration
    ServiceLocation
```

Runtime does not have to rediscover those semantic relationships.

---

# 15. Requirement applicability is not satisfaction

Important distinction:

```text
Resolved Model:
    ServiceLocation is required
```

does not mean:

```text
Runtime:
    customer supplied ServiceLocation
```

Therefore:

```text
Requirement applicability
        ≠
Requirement satisfaction
```

The first is resolved semantics.

The second is runtime/interaction data.

This prevents customer data from leaking into configuration compilation.

---

# 16. Operation resolution

Runtime needs to know which Operations are applicable to the merchant's resolved model.

For example:

```text
Scheduling
        ↓
create appointment
cancel appointment
reschedule appointment
```

subject to registered semantics and effective policies.

However, resolution cannot manufacture:

```text
operation:
    gardener.auto_delay_for_rain
```

because no registered semantic definition exists.

The resolved operation set is a subset/materialisation of registered semantics.

---

# 17. Operation availability versus operational eligibility

We need another important distinction.

A resolved model may establish:

```text
Operation "cancel_booking"
is part of this merchant's model
```

That does **not** mean:

```text
this particular booking
can be cancelled right now
```

The first is **model applicability**.

The second depends on runtime state, authority, policy and context.

Therefore:

```text
Resolved operation exists
        ≠
Command is currently executable
```

Runtime retains responsibility for actual execution guards.

---

# 18. Offering resolution

Offering resolution preserves three distinct concepts:

```text
OfferingSchema             registered platform semantics
        ↓ constrains
MerchantOffering           merchant identity/content/commercial values
        ↓ resolves with registered bindings
ResolvedOffering           derived validated operational configuration
```

A resolved offering combines the merchant values with applicable capability bindings, resource requirements, schedule parameters and policy selections. It retains the identities of both the schema and merchant offering so provenance and ownership are not flattened.

Example:

```text
Offering:
    60-minute driving lesson

Configured:
    duration = 60 min
    price = £X

Uses:
    Scheduling
    Payment
```

The resolved model may establish these bindings only where the registered schema and capability contracts permit them.

It does not create the Offering's actual runtime bookings or purchases.

---

# 19. Resource resolution

Resource resolution also preserves three distinct concepts:

```text
ResourceDefinition:
    platform semantic meaning such as instructor
        ↓ constrains
ConfiguredResource:
    stable merchant configuration such as Instructor A
        ↓ participates in
ResourceInstanceState:
    live occupancy/allocation/quantity/health
```

One ResourceDefinition may permit multiple configured resources:

```text
Instructor A
Instructor B
```

The resolved model establishes the valid relationship between each ConfiguredResource and its ResourceDefinition.

But their live operational state belongs to runtime/persistence.

For example:

```text
Instructor A
currently busy
```

must not be embedded into the static operational model.

---

# 20. Static versus dynamic information

This distinction is critical.

### Resolved model

Contains relatively stable merchant semantics/configuration:

```text
active capabilities
effective policies
resource definitions/configuration
offerings
schedule rules
operation applicability
requirement applicability
```

### Runtime state

Contains changing operational truth:

```text
current booking
resource state
current allocations
customer inputs
payment status
live availability
order status
```

Therefore:

```text
ResolvedOperationalModel
        ≠
Merchant runtime database
```

---

# 21. Scheduling resolution

Scheduling resolution may produce effective scheduling constraints such as:

```text
merchant operating hours
offering duration
resource schedule relationships
booking policy
buffers
```

But it must not produce live availability.

For example:

```text
Resolved model:
    Lesson duration = 60 minutes
    Instructor availability participates
```

Runtime:

```text
Is 14:00 Tuesday available?
```

That question belongs to Availability evaluation.

Therefore:

```text
Schedule configuration
        ≠
AvailabilityDecision
```

---

# 22. Interaction mode resolution

A resolved model may establish that a merchant supports:

```text
guest enquiry
authenticated customer portal
secure contextual link
merchant-only operation
```

where those modes are registered and configured.

But it does not authenticate a specific user.

Therefore:

```text
Customer access capability enabled
        ≠
Customer authenticated
```

Identity/authentication remains runtime/platform infrastructure.

---

# 23. Exposure resolution

A merchant configuration may select bounded exposure behaviour.

Resolution may establish:

```text
Order.preparing
    may be projected to CUSTOMER
```

or:

```text
Job.inspection_required
    merchant/internal only
```

But the resolved model does not turn those policies into actual delivered UI.

Therefore:

```text
Exposure binding
        ≠
Presentation
```

and:

```text
Exposure binding
        ≠
Notification
```

---

# 24. Notification resolution

The resolved model may establish:

```text
AppointmentConfirmed
        ↓
customer notification enabled
        ↓
permitted delivery:
    email
    ICS
```

But runtime must still produce the actual event.

Notification delivery cannot occur merely because resolution knows such a binding exists.

Therefore:

```text
Resolved notification binding
        ≠
Notification event
        ≠
Delivery attempt
```

---

# 24A. Canonical configuration and release objects

The pipeline uses four distinct objects and authorities:

```text
MerchantConfiguration
        ↓ deterministic validation and compilation
ExecutableMerchantModel
        ↓ bound together with source and provenance
ConfigurationRelease
        ↓ selected atomically per merchant by
ActiveRelease
```

- `MerchantConfiguration` is an immutable source snapshot of merchant selections and permitted values. It is not executable.
- `ExecutableMerchantModel` is the deterministic compiler output. It is the implementation-facing form of the `ResolvedOperationalModel` defined throughout this document; these are two names for one concept, not competing model layers.
- `ConfigurationRelease` is an immutable provenance envelope that binds the exact MerchantConfiguration, exact ExecutableMerchantModel, registry/version inputs, compiler identity and release identity used to produce it.
- `ActiveRelease` is the merchant-scoped atomic pointer to the ConfigurationRelease used to begin new runtime work. It contains no copied or independently editable semantics.

MS-PROT-040's `ConfigurationRevision` is the lifecycle view of this coherent source/model/release unit. It is not a fifth independently editable semantic representation. MS-PROT-040 governs review, approval, activation, supersession and reinstatement.

The terms `ConfigurationArtifact`, `ResolvedOperationalModel` and `ExecutableMerchantModel` must not become three separately authoritative compiled representations. Historical `ConfigurationArtifact` wording maps to the immutable ConfigurationRelease envelope; executable semantics exist only in the bound ExecutableMerchantModel.

---

# 25. Operational model is immutable after materialisation

Once a model is activated for runtime use, it must not be mutated in place by ad-hoc configuration changes.

Reject:

```text
ACTIVE MODEL
    policy A changes
    resource semantics change
    operation disappears
    while commands are executing
```

Instead:

```text
Active Model A
        │
merchant change
        ▼
Candidate Config B
        ↓
validate
        ↓
resolve
        ↓
Model B
        ↓
activate
```

Model A remains coherent.

---

# 26. Configuration change creates a new release

The conceptual lifecycle is:

```text
Configuration A → Executable Model A → Release A → ACTIVE

Merchant changes configuration
      ↓
Candidate Configuration B
      ↓
validate + compile/resolve
      ↓
Executable Model B
      ↓
immutable Release B
      ↓
stage + ACTIVATE
```

The end-to-end lifecycle is:

```text
Candidate configuration
    CANDIDATE → VALIDATED → APPROVED (where required)
                               ↓ compile and bind
Configuration release
                       STAGED → ACTIVE → SUPERSEDED
```

Creation or persistence of a candidate, compiled model or staged release does not alter the active release. Validation, compilation, release persistence or activation failure leaves the previous ActiveRelease unchanged.

This avoids partial mutation and prevents an unvalidated source object from becoming runtime authority.

---

# 27. Activation must be atomic from runtime's perspective

When a new model replaces an old one, runtime should observe:

```text
Model A
```

or:

```text
Model B
```

not a partially resolved mixture.

The specific implementation may eventually use:

```text
transaction
atomic reference
database version
configuration snapshot
```

or another mechanism.

MS-PROT-022 defines only the invariant:

> **A command executes against one coherent resolved operational model.**

Runtime captures the applicable ConfigurationRelease once at the command/continuation boundary. It must not reread the ActiveRelease between evaluation and fulfilment. Activation atomically replaces only the merchant-scoped pointer after the complete immutable release is available.

Configuration reinstatement follows the same atomicity rule but is not direct rollback or time travel. Under MS-PROT-040, a superseded revision must be revalidated against current platform semantics and impact requirements and materialised as a new release/activation decision. Reinstatement may reuse an earlier source configuration where still valid, but it does not reactivate the superseded release identity, mutate release history or reverse intervening business facts.

---

# 28. Existing operations during configuration change

This reveals an important boundary.

Suppose a driving instructor disables customer cancellation while future lessons already exist.

Existing commitments retain the release that governed their establishment unless an explicit, validated migration changes that association.

```text
Commitment created under Release A
        ↓ stores governing release/sufficient immutable snapshot
Release B becomes active
        ├── new independent work begins under Release B
        └── continuation of commitment resolves Release A
```

The commitment must retain either the governing release identity or a sufficient immutable semantic snapshot where retention policy prevents full release storage. If the governing semantics cannot be resolved, runtime must reject or quarantine the continuation for explicit repair; it must not silently substitute the current ActiveRelease.

A future migration mechanism may deliberately move eligible commitments after validating compatibility, authority and business obligations. The migration mechanism is deferred; the no-silent-reinterpretation default is decided here.

---

# 29. The resolved model may be transient or persisted

MS-PROT-022 does not require:

```text
store resolved model in database
```

or:

```text
recompute on startup
```

Both may be valid.

The architectural requirement is:

```text
deterministically derivable
coherent
traceable
immutable while active
```

Persistence choice remains downstream.

---

# 30. Local development independence

The resolution pipeline must work from the local Main Street working copy.

For development/test scenarios:

```text
local semantic definitions
        +
local merchant test configuration
        ↓
local resolver/compiler
        ↓
Resolved Operational Model
```

GitHub must not be needed to resolve or execute ordinary local test configurations.

This preserves the accepted local-development requirement.

---

# 31. Determinism

Given the same:

```text
semantic definitions
merchant configuration
resolver/compiler version/behaviour
```

resolution must produce semantically equivalent output.

AI must not participate inside deterministic resolution.

Therefore:

```text
same validated inputs
        ↓
same effective operational semantics
```

This is necessary for reproducibility and debugging.

---

# 32. Resolution errors

Resolution may fail for reasons such as:

```text
unknown semantic reference
missing required dependency
cyclic required dependency
unsupported policy selection
policy not applicable
conflicting capability selections
invalid owner relationship
unresolved resource requirement
invalid offering binding
semantic invariant violation
```

Failure must not cause the compiler to improvise.

Result:

```text
RESOLUTION FAILED
```

rather than:

```text
best effort operational model
```

---

# 33. Explanation/provenance

The resolver should conceptually be able to explain why something appears in the effective model.

Example:

```text
Why is ServiceLocation required?

Because:
MobileService is active
    ↓
registered semantic relationship
    ↓
ServiceLocation becomes applicable
```

Similarly:

```text
Why is Scheduling active?

Because:
CustomerBooking requires Scheduling
```

This is valuable for:

```text
debugging
merchant explanation
AI diagnosis
architecture validation
future migration tooling
```

It does not require verbose provenance to be stored everywhere, but the relationships must remain traceable.

---

# 34. The resolver must not flatten away ownership

Suppose:

```text
Booking
Payment
Scheduling
Notification
```

are composed.

The resulting model should not become:

```text
one giant merchant workflow object
```

where capability ownership disappears.

Instead:

```text
ResolvedOperationalModel
       │
       ├── Booking semantics
       ├── Payment semantics
       ├── Scheduling semantics
       └── Notification semantics
              +
       validated relationships
```

This preserves modular ownership.

---

# 35. Runtime collaboration remains capability-owned

Resolution may establish:

```text
Booking
    requires
Scheduling
```

or:

```text
Notification
    consumes
BookingConfirmed
```

But it does not generically decide Java execution order.

Where immediate consistency is required:

```text
application/domain coordination
```

handles it.

Where independent reaction applies:

```text
DomainEvent
```

handles it.

MS-PROT-022 therefore preserves the rejection of generic `OperationComposer`.

---

# FORMAL REVIEW

Now challenge the proposal.

## Review 1 — Do we actually need a ResolvedOperationalModel?

Could runtime simply use semantic registry + validated MerchantConfiguration?

Technically yes.

But that would make runtime repeatedly responsible for:

```text
dependency interpretation
policy/default resolution
requirement applicability
semantic binding
```

Those responsibilities belong upstream.

A logical resolved model therefore provides a valid boundary.

It need not become a giant physical object.

**Verdict: PASS.**

---

# 36. Review 2 — Is this just duplicated configuration?

Potentially.

If the operational model simply copies:

```text
MerchantConfiguration
```

then it adds no value.

Required constraint:

> The resolved model contains **effective semantics**, not merely raw selections.

For example:

```text
Configuration:
    CustomerBooking active
```

Resolved:

```text
CustomerBooking active
Scheduling active because required
applicable policy defaults resolved
associated operations available
registered Requirements known
```

The layer earns its existence through resolution.

**PASS WITH CONSTRAINT.**

---

# 37. Review 3 — Could effective semantics become a second registry?

Yes, if definitions are copied and modified.

Therefore:

> Resolved structures reference registered semantics and attach merchant-scoped effective configuration; they do not redefine semantic meaning.

**PASS AFTER CONSTRAINT.**

---

# 38. Review 4 — Does immutability create excessive complexity?

No.

The conceptual invariant:

```text
do not mutate active semantic model in place
```

is important.

Implementation can initially be extremely simple.

A new model replacing an old object is enough.

No distributed configuration system is implied.

**PASS.**

---

# 39. Review 5 — Do we need universal model versioning?

No.

We need source-configuration traceability, but MS-PROT-022 should not design general semantic version compatibility.

Required distinction:

```text
configuration/model identity
        ≠
semantic-definition versioning
```

**PASS.**

---

# 40. Review 6 — Could Requirement applicability be too dynamic for resolution?

Yes, some Requirements depend on runtime choices.

Example:

```text
Grocery order:
customer eventually chooses DELIVERY
```

Before that choice:

```text
DeliveryAddress
```

is not yet required.

Therefore requirement resolution cannot always produce a flat final list at merchant-model compilation time.

### Material revision required

The model must distinguish:

```text
statically applicable Requirement
```

from:

```text
contextually resolvable Requirement
```

The resolved operational model should retain the **registered applicability semantics** necessary for deterministic runtime requirement resolution.

It must not prematurely evaluate runtime-dependent conditions.

This is an important correction.

---

# 41. Revised requirement model

Therefore:

```text
Resolved Operational Model
        │
        ├── unconditional requirements
        │
        └── registered conditional applicability
```

At runtime:

```text
Operation context
        +
Resolved requirement semantics
        ↓
Requirement applicability decision
```

Example:

```text
fulfilment = DELIVERY
        ↓
DeliveryAddress applies
```

This keeps runtime dynamic where necessary without allowing runtime to invent relationships.

**Review defect corrected.**

---

# 42. Review 7 — Same issue with policies?

Some policy applicability may also depend on context.

Yes.

Therefore resolved model should contain:

```text
effective merchant policy selections
```

plus applicable registered semantic scope.

Runtime uses them where relevant.

It should not flatten every policy into every operation.

**PASS AFTER CLARIFICATION.**

---

# CROSS-DOMAIN VALIDATION

## Gardener

Configuration:

```text
Service
Enquiry
Quotation
Scheduling
Notification
Portfolio
```

Resolution might establish:

```text
applicable service operations
registered requirements:
    CustomerContact
    ServiceLocation where applicable

effective scheduling policy
notification bindings
```

Live job status remains runtime.

No gardener-specific resolved object required.

**PASS.**

---

# 43. Driving instructor

Configuration:

```text
Lesson offering
Scheduling
CustomerAccess
Payment
PurchaseHistory
```

Resolved model establishes:

```text
lesson duration
scheduling participation
instructor resources
customer schedule access
purchase capability
```

A specific lesson:

```text
Tuesday 14:00
```

is runtime data, not part of the resolved model.

**PASS.**

---

# 44. Solicitor

Configuration:

```text
Consultation
Scheduling
Notification
ICS delivery
```

Resolved model:

```text
merchant scheduling active
customer portal absent
notification binding active
ICS delivery enabled
```

A specific client appointment remains runtime.

No Solicitor-specific scheduling semantics needed.

**PASS.**

---

# 45. Restaurant

Configuration:

```text
Ordering
Payment
CustomerTracking
Notification
possibly Inventory
```

Resolved model may establish:

```text
order operations
effective payment policies
tracking exposure bindings
notification reactions
inventory dependency
```

Specific Order #123 remains runtime.

**PASS.**

---

# 46. Restaurant consistency test

Suppose:

```text
ConfirmOrder
```

requires authoritative inventory allocation.

Can resolver encode:

```text
Operation A triggers Operation B
```

as generic execution sequence?

No.

It may resolve the semantic relationship indicating that order confirmation participates in an inventory consistency requirement.

Actual synchronous orchestration remains runtime/application design.

**PASS.**

---

# 47. Grocery dynamic requirement test

Merchant supports:

```text
COLLECTION
DELIVERY
```

At compile/resolution time, customer fulfilment choice is unknown.

Resolved model therefore cannot simply say:

```text
DeliveryAddress always required
```

Instead it preserves:

```text
IF operational fulfilment context = DELIVERY
    DeliveryAddress applies
```

as registered bounded applicability semantics.

Runtime evaluates it against the current operation context.

No arbitrary merchant rule is created.

**PASS after Review 6 correction.**

---

# 48. Motel

Resolved model establishes:

```text
Accommodation capability
room-category semantics
allocation mode
payment configuration
scheduling/interval semantics
```

It does not contain:

```text
Room 104 currently available
```

That is live operational truth.

Availability is evaluated at runtime.

**PASS.**

---

# 49. Mechanic

Configuration:

```text
Workshop Service
Mobile Service
Scheduling
```

Resolved model may establish both service modes.

Runtime operation choice determines:

```text
Workshop:
    VehicleRegistration

Mobile:
    VehicleRegistration
    ServiceLocation
```

Again, contextual Requirement applicability is preserved.

**PASS.**

---

# 50. Realtor

General enquiry requires no scheduling model.

Viewing request participates in scheduling.

Resolved model supports both operations without assigning fake:

```text
capacity = infinity
```

to general enquiry.

**PASS.**

---

# ADVERSARIAL VALIDATION

## Unknown capability reference

Configuration:

```text
capability = MAGIC_AUTOMATION
```

Resolver:

```text
FAIL
```

Not:

```text
create placeholder capability
```

**PASS.**

---

# 51. Missing dependency

Configuration activates:

```text
CustomerBooking
```

Registered semantics require:

```text
Scheduling
```

If dependency is registered and resolvable:

```text
resolve Scheduling
```

If required semantics are unavailable:

```text
FAIL
```

**PASS.**

---

# 52. Invalid policy

Configuration:

```text
PaymentTiming = SOMETIMES
```

if not registered:

```text
FAIL
```

**PASS.**

---

# 53. Configuration mutation during execution

Model A active.

Merchant modifies scheduling.

Can existing Model A mutate halfway through a Command?

No.

Model B must be separately resolved and activated.

**PASS.**

---

# 54. Live availability leakage

Could resolver calculate:

```text
Tuesday 14:00 available
```

and store it as part of the operational model?

No.

Availability depends on changing runtime facts.

**PASS.**

---

# 55. Customer data leakage

Could resolver store:

```text
CustomerAddress = 20 High Street
```

because DeliveryAddress is required?

No.

Requirement applicability and actual customer data remain separate.

**PASS.**

---

# 56. Business-category leakage

Merchant metadata says:

```text
business_category = SOLICITOR
```

Can resolver automatically generate:

```text
LegalAppointment workflow
```

No.

Only configured registered semantics participate.

**PASS.**

---

# 57. AI inside resolution

Could AI decide how to resolve an ambiguous capability relationship?

No.

Ambiguity must cause:

```text
configuration clarification
or
resolution failure
```

The deterministic resolver cannot delegate semantic authority to AI.

**PASS.**

---

# 58. GitHub dependency test

Local machine contains:

```text
MainStreet source
semantic definitions
merchant test config
```

Can the resolver construct a model without fetching GitHub?

Yes.

That remains an implementation requirement.

**PASS.**

---

# 59. Final accepted conceptual structure

```text
                 SEMANTIC REGISTRY
                        │
                        │
        IMMUTABLE MERCHANT CONFIGURATION
                        │
              ┌─────────┴─────────┐
              ▼                   ▼
        Semantic refs       Merchant values
              │                   │
              └─────────┬─────────┘
                        ▼
             CONFIGURATION RESOLVER
                        │
        ┌───────────────┼────────────────┐
        ▼               ▼                ▼
   Dependencies      Policies       Applicability
        │               │                │
        └───────────────┼────────────────┘
                        ▼
              RESOLVED OPERATIONAL MODEL
             / EXECUTABLE MERCHANT MODEL
                        │
        ┌───────────────┼────────────────┐
        ▼               ▼                ▼
   Capability view   Operation view   Config view
        │               │                │
        └───────────────┼────────────────┘
                        ▼
              CONFIGURATION RELEASE
              (source + model + provenance)
                        │
             MERCHANT ACTIVE RELEASE
                        │ atomic capture
                        ▼
                  GENERIC RUNTIME
                        │
              dynamic operational truth
```

---

# 60. Hard invariants

MS-PROT-022 establishes:

1. The Resolved Operational Model is **derived**, not an independent semantic source of truth.
2. Resolution is deterministic.
3. AI does not participate in deterministic resolution.
4. The resolver cannot invent semantic definitions.
5. Registered semantic ownership survives resolution.
6. Merchant scope cannot be crossed during resolution.
7. Runtime executes against one coherent resolved model.
8. An active model is not mutated piecemeal.
9. Configuration changes produce a new resolved model.
10. Model activation appears atomic to command execution.
11. The resolved model contains effective semantics, not merely a copy of raw configuration.
12. The resolved model does not contain live operational state.
13. Availability is not resolved as static truth.
14. Allocation is not performed during configuration resolution.
15. Requirement applicability and Requirement satisfaction remain separate.
16. Runtime-dependent Requirement applicability is preserved for deterministic contextual evaluation rather than prematurely flattened.
17. Policy definition and effective policy selection remain separate.
18. Operations present in the model are not necessarily executable in every runtime context.
19. Exposure binding does not equal presentation.
20. Notification binding does not equal notification delivery.
21. Capability relationships do not define generic runtime operation sequencing.
22. Configuration/model provenance must remain traceable.
23. Existing commitments must not be silently reinterpreted by configuration replacement.
24. Local model resolution cannot depend on fetching Main Street artefacts from GitHub.
25. Only registered `REQUIRES` relationships transitively activate capabilities in the first dependency slice.
26. Capability activation retains every merchant-selection and requiring-capability reason.
27. Cyclic `REQUIRES` relationships are invalid registry semantics.
28. `CONFLICTS_WITH` is symmetric and is validated after dependency closure.
29. `SUPPORTS` does not activate capabilities, and `DEPENDS_ON` has no executable meaning until separately specified.
30. MerchantConfiguration, ExecutableMerchantModel, ConfigurationRelease and ActiveRelease are distinct objects with distinct authorities.
31. ResolvedOperationalModel and ExecutableMerchantModel name one derived executable concept, not two independently authoritative models.
32. A ConfigurationRelease immutably binds its exact source, executable model and compilation provenance.
33. Validation, compilation, persistence or activation failure leaves the previous ActiveRelease unchanged.
34. Activation atomically replaces only the merchant-scoped active-release pointer with a complete immutable release.
35. Runtime captures one applicable release for an execution boundary and does not mix models during that execution.
36. Existing commitments retain their governing release or sufficient immutable semantic snapshot unless an explicit validated migration changes it.
37. A missing historical governing model is an explicit error; runtime must not silently substitute the current active release.
38. OfferingSchema, MerchantOffering and ResolvedOffering remain distinct.
39. ResourceDefinition, ConfiguredResource and ResourceInstanceState remain distinct.
40. ScheduleConfiguration is stable configuration; AvailabilityDecision remains contextual runtime truth.
41. A superseded release identity cannot be directly reactivated; reinstatement requires current validation, impact review and a new release/activation decision under MS-PROT-040.
42. Configuration reinstatement does not reverse domain facts or business history.

---

# 61. Items explicitly deferred

MS-PROT-022 deliberately does **not** decide:

```text
Java representation
JSON/YAML format
database schema
configuration storage
cache implementation
semantic version compatibility
explicit configuration compatibility/migration mechanism
explicit existing-commitment migration mechanism
distributed configuration propagation
API contracts
UI generation
persistence strategy
event broker
```

Those remain downstream.

---

# 62. Governance verdict

The first proposal exposed one material defect during review: **Requirement applicability cannot always be fully resolved statically**, because some applicability depends on runtime operation context such as Delivery versus Collection.

The proposal was revised to distinguish:

```text
static semantic resolution
        +
registered contextual applicability
        ↓
runtime deterministic evaluation
```

After that correction, the model survives Gardener, Driving Instructor, Solicitor, Restaurant, Grocery, Motel, Mechanic and Realtor validation without a niche-specific resolver branch.

Therefore:

```text
MS-PROT-022
Configuration Resolution & Operational Model

PROPOSE      ✓
REVIEW       ✓
REVISE       ✓
VALIDATE     ✓
ACCEPT       ✓
```

## Status: **ACCEPTED**

The central accepted rule is:

> **Configuration resolution produces an immutable merchant-scoped operational view of existing Main Street semantics. It resolves what can be known from configuration while preserving genuinely runtime-dependent questions for deterministic runtime evaluation. It never becomes either a second semantic registry or a repository of live business state.**

**Revision 1.2:** Defined the canonical source/model/release/active-pointer contract, atomic failure and rollback behaviour, default continuity for existing commitments, and the aligned offering/resource/schedule resolution boundaries.

**Revision 1.3:** Incorporated MS-PROT-040 as the lifecycle authority, mapped ConfigurationRevision to the existing source/model/release unit and replaced direct rollback with validated reinstatement as a new release.

---

## Next design dependency

The next unresolved boundary is now quite clear.

We have:

```text
Registered semantics
        ↓
Merchant configuration
        ↓
Resolved operational model
        ↓
???
        ↓
Runtime operations
```

The next design needs to define **how commands are evaluated and executed against that resolved model**, including authority, requirements, runtime guards, state validity, contextual policy application and production of execution evidence/events.

That should be:

> **MS-PROT-023 — Command Evaluation & Runtime Execution Contract**

It should consolidate the earlier `OperationRequest`/Command work without redesigning persistence or API transport.
