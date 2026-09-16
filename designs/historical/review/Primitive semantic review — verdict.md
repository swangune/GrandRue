## Primitive semantic review — verdict

The primitive layer needs a **material revision before we use it to judge MS-PROT-043**.

The original primitive work was valuable for discovering Main Street's vocabulary, but several things originally labelled “primitives” have since acquired much more precise meanings. MS-PROT-020 already moved us in that direction by defining a Primitive as the smallest reusable semantic contract or deterministic behaviour, while explicitly separating Capability, Resource, DataConcept, Requirement, Policy, EventDefinition, and owner-scoped Operation/State/Transition. 

The code now also contains evidence of two generations of the model: the newer registry architecture supports capability-owned resources, operations with multiple bounded effects, requirements and policies, while the older prototype still assumes one `Operation = Resource + Transition + Privilege` and still contains the now-rejected trigger→consequence composition machinery.  

My semantic verdict is therefore:

> **Do not expand the primitive vocabulary. Shrink it. Most business nouns and verbs should cease to be primitives. Primitives should describe only the smallest capability-neutral mechanisms from which capability-owned semantics are constructed.**

---

# 1. The stricter primitive test

A construct should qualify as a Main Street Primitive only if all of these are true:

| Test | Requirement |
|---|---|
| Capability neutrality | Its meaning does not change between realtor, motel, mechanic, consultant, publisher, retail, etc. |
| Semantic indivisibility | Splitting it further would no longer produce independently useful business semantics |
| Typed contract | Its admissible inputs, outputs and effects can be stated precisely |
| Fixed meaning | A capability may use it but cannot redefine it |
| No merchant meaning | Merchants never configure raw primitives |
| No UX dependency | Its meaning is independent of buttons, forms, pages or terminology |
| No provider dependency | Google, PayPal, Calendly, etc. do not determine its semantics |
| Determinism | Given the relevant authoritative context, its semantic consequence is known |
| Ownership compatibility | It acquires concrete business authority only through a Capability/Operation |

This is consistent with MS-PROT-020's accepted construction hierarchy:

```text
Primitive
    ↓
Abstraction
    ↓
Framework
    ↓
Capability
    ↓
merchant-facing policy/configuration
```

and, importantly, merchants do **not** compose raw primitives themselves. 

---

# 2. The original primitive taxonomy does not survive intact

MS-PROT-001 originally proposed nouns such as `Offering`, `Resource`, `Capacity`, `Schedule`, `Location`, `Identity` and `Participant`, along with verbs such as `Request`, `Select`, `Reserve`, `Allocate`, `Schedule`, `Confirm`, `Cancel`, `Pay`, `Notify`, `Require`, `Allow`, `Validate` and `Verify`.  

That was a good discovery vocabulary. It is no longer a safe **primitive vocabulary**.

### Nature concepts

| Original | Verdict | Correct semantic level |
|---|---|---|
| `Offering` | **REMOVE from primitives** | `OfferingSchema` + merchant-owned Offering data |
| `Resource` | **REDEFINE** | Semantic/domain construct; see major ambiguity below |
| `Capacity` | **RECLASSIFY** | Bounded operational constraint/claim context |
| `Schedule` | **REMOVE as one primitive** | Split into configuration, availability, schedule intent, Appointment semantics |
| `Location` | **RECLASSIFY** | `DataConcept` / capability-owned structured data |
| `Identity` | **RECLASSIFY** | Identity/access bounded context |
| `Participant` | **RECLASSIFY** | Runtime actor/relationship context |

The most obvious example is `Schedule`. We now know:

```text
working hours/breaks
        ≠
merchant UNAVAILABLE intent
        ≠
Appointment
        ≠
Booking
        ≠
calendar representation
```

So a generic `SCHEDULE` primitive cannot carry all those meanings without becoming dangerously overloaded.

---

# 3. Most original action primitives should also be removed

| Original verb | Semantic verdict |
|---|---|
| `REQUEST` | Too ambiguous: Command, Enquiry, SchedulingRequest and business request are different |
| `OFFER` | Capability/business operation, not fundamental mechanism |
| `SELECT` | Participant/UI decision, not authoritative business primitive |
| `RESERVE` | **Reject**: conflates Booking, Hold and Allocation |
| `ALLOCATE` | Retain underlying allocation semantics, but tighten definition |
| `SCHEDULE` | Reject as generic primitive |
| `CONFIRM` | Capability-owned lifecycle operation |
| `CANCEL` | Capability-owned lifecycle operation |
| `COMPLETE` | Capability-owned lifecycle operation |
| `PAY` | Payment capability operation |
| `REFUND` | Payment capability operation |
| `FULFIL` | Capability-specific abstraction/operation |
| `DELIVER` | Delivery capability operation |
| `COLLECT` | Fulfilment operation in one context |
| `NOTIFY` | Notification capability operation |

There is also direct evidence that the old taxonomy was too loose: **`COLLECT` was used twice with two different meanings**—customer collection of goods and collection of information. 

A genuine primitive vocabulary cannot tolerate that kind of semantic overloading.

---

# 4. The control verbs are not primitives either

The old:

```text
REQUIRE
ALLOW
DENY
ENABLE
DISABLE
LIMIT
CONSTRAIN
```

should not remain primitives.

We now have stronger concepts:

```text
REQUIRE
    → Requirement

ALLOW / DENY
    → Policy / Authorisation result

ENABLE / DISABLE
    → Merchant configuration lifecycle

LIMIT
    → bounded Policy / Capacity constraint

CONSTRAIN
    → Requirement / Policy / Invariant
```

Likewise:

```text
IDENTIFY
COLLECT
VALIDATE
VERIFY
```

are not one common primitive family.

`VALIDATE` belongs heavily to compiler/runtime decision machinery. `VERIFY` has materially different trust semantics. `IDENTIFY` belongs to identity/relationship modelling. `COLLECT` is an interaction mechanism.

---

# 5. What should remain at the true primitive level?

The newer registry implementation gives us a useful clue.

An `OwnedOperationDefinition` no longer assumes one transition. It owns a collection of bounded effects. 

And the current canonical effect algebra starts with:

```text
CREATE RESOURCE INSTANCE
TRANSITION RESOURCE STATE
```

through `OwnedResourceCreationEffect` and `OwnedStateTransitionEffect`.  

That is much closer to the right level.

I would divide the true primitive layer into two families.

### Authoritative effect primitives

```text
CREATE_OPERATIONAL_OBJECT

TRANSITION_LIFECYCLE_STATE

MUTATE_OWNED_DATA          ← missing today

ESTABLISH_TYPED_REFERENCE  ← candidate, needs proof

REMOVE_TYPED_REFERENCE     ← candidate, needs proof

CLAIM_ALLOCATION

RELEASE_ALLOCATION
```

`MUTATE_OWNED_DATA` is important. Our current effect model can create a resource and transition its state, but real accepted operations also need to change authoritative values while preserving identity:

```text
Appointment
10:00 → 15:00

Listing
£1,200 → £1,300

CustomerContext
old email → new email

Booking
Friday–Sunday → Saturday–Monday
```

None of those is naturally just “create another object” or “change lifecycle state”.

We therefore have a genuine semantic gap in the current effect algebra.

### Decision/evaluation primitives

```text
EVALUATE_REQUIREMENT

RESOLVE_POLICY

EVALUATE_AVAILABILITY
```

These produce decisions but no authoritative business mutation.

This respects the accepted distinctions between Requirement, Policy and Availability.

---

# 6. Allocation deserves special treatment

`Allocation` is one of the few original ideas that still behaves like a genuinely reusable low-level semantic mechanism.

Current code models an `AllocationClaim` over a typed `AllocationScope`, including quantity and time-window scopes.   

Its meaning can remain stable across:

```text
hotel capacity
equipment
staff time
service bays
inventory quantity
other constrained resources
```

But the correct semantics are:

> **Allocation is an authoritative scoped claim against capacity.**

Not:

> “Reserve something.”

`Reserve` should therefore disappear from the primitive vocabulary. A Booking may cause an Allocation. A Hold may temporarily cause an Allocation-like claim. Neither makes `RESERVE` the fundamental semantic operation.

---

# 7. `Resource` currently has a serious semantic ambiguity

This is the largest issue I found.

MS-PROT-001 originally defined Resource narrowly:

> something whose availability or allocation can constrain an operation. 

Examples were:

```text
Room
Mechanic
Service Bay
Consultant
Vehicle
```

But current `OwnedResourceDefinition` means essentially:

> an owner-scoped lifecycle-bearing thing with states. 

And `RegisteredCapability` makes operations affect those resources. 

Those meanings are not equivalent.

If we eventually model:

```text
Appointment
Booking
Enquiry
CustomerContext
Listing
```

through `OwnedResourceDefinition`, then we would be saying all of those are **Resources**.

That is semantically wrong.

A Booking is not a resource.

An Enquiry is not a resource.

A CustomerContext is not a resource.

### Recommended correction

Introduce a neutral lifecycle-owning semantic term:

```text
OperationalObjectDefinition
```

or:

```text
EntityDefinition
```

Then:

```text
OperationalObjectDefinition
    ├── Appointment
    ├── Booking
    ├── Enquiry
    ├── Allocation
    └── etc.
```

while `Resource` retains its narrower meaning:

> **something whose capability/capacity may constrain an operation.**

This distinction will prevent major confusion later.

I would prefer **OperationalObject** over `Entity`, because `Entity` is an overloaded DDD/database term.

---

# 8. State and Transition survive the review

These are among the cleanest semantics we have.

Current `State` belongs to an exact owner, and `Transition` cannot cross owners.  

The canonical registry model strengthens that by keeping resource lifecycle states owner-scoped. 

So:

```text
State
Transition
```

should remain **owner-scoped semantic constructs**, not universal primitives merchants compose.

That is correct.

---

# 9. Operation is not a primitive

An Operation is the semantic point where primitives acquire concrete business meaning.

For example:

```text
CancelAppointment
ModifyBooking
SubmitEnquiry
ChangeListingPrice
CreateUnavailableIntent
```

An Operation may:

```text
evaluate requirements
check authority
evaluate policies
change several authoritative objects
establish/release claims
produce domain facts
```

The newer `OwnedOperationDefinition` correctly allows multiple effects and requirements. 

The old `Operation` class still requires exactly one `Resource`, one `Transition` and one `Privilege`, so it should now be considered **legacy prototype semantics**, not the canonical Operation definition. 

---

# 10. Availability also needs tightening

The current `AvailabilityDecision` is:

```text
requestIdentifier
subjectIdentifier
boolean available
evaluatedAt
```



The core idea remains correct: Availability is a contextual decision, not persisted truth.

But `boolean available` is now insufficient for the information our scheduling model needs.

For example, merchants must be able to distinguish:

```text
outside working hours
regular break
merchant unavailable
existing Appointment
capacity exhausted
required resource unavailable
```

I would **not** turn those into one enormous universal status enum.

Instead:

```text
AvailabilityDecision
{
    subject
    context
    outcome
    reasons[]
    evaluatedAt
}
```

where:

```text
outcome = AVAILABLE | UNAVAILABLE
```

and domain-specific/scheduling-specific reason definitions carry the explanation.

The merchant calendar can then project:

```text
INVALID
UNAVAILABLE
OCCUPIED
```

without forcing those UI classifications into the universal Availability primitive.

---

# 11. The new Enquiry semantics do not require new primitives

This is important before reviewing MS-PROT-043.

We should **not** introduce:

```text
ENQUIRY_SUBJECT primitive
PROPERTY primitive
LISTING primitive
CONTEXT_CARRY_FORWARD primitive
```

The realtor case is representable more cleanly.

Conceptually:

```text
Listing
   │
   │ typed relationship
   ▼
Enquiry

Enquiry
   │
   └── customer-supplied data
```

`EnquirySubject` is therefore best understood as a **role/typed relationship**, not a fundamental semantic object.

Likewise:

```text
street
building number
city
postcode
coordinates
```

are structured `DataConcept`s.

The map is presentation/integration.

The Listing/Property capability owns their business meaning.

This is precisely why the primitive review should happen before accepting MS-PROT-043.

---

# 12. One missing semantic mechanism exposed by Enquiry

The realtor scenario does reveal something the existing canonical model does not yet express strongly enough:

> **typed relationships between operational objects owned by different semantic contexts.**

Examples now include:

```text
Enquiry → Listing

Appointment → CustomerContext

Appointment → Offering

Booking → CustomerContext

Enquiry → Appointment

Enquiry → Booking
```

We should not solve that using arbitrary IDs or free-form strings.

But I would **not yet declare `REFERENCE` a Primitive**.

First we should specify a bounded relationship contract and falsify it. Otherwise we risk building a generic graph language—the exact kind of semantic over-expansion we have repeatedly avoided.

---

# 13. Current repository semantic debt

There are four concrete discrepancies we should record.

The first is that `SemanticDefinitionKind` includes `PRIMITIVE`, `ABSTRACT_PRIMITIVE` and `FRAMEWORK`, while MS-PROT-020 says those are construction/reuse mechanisms that do not necessarily need to become globally registered runtime definitions. It also currently omits the now-accepted `OfferingSchema` kind.  

Second, the repository still contains the old mutable `Capability`, `Resource`, `Operation`, and compiler generation while the new registry architecture has `RegisteredCapability`, `OwnedResourceDefinition`, `OwnedOperationDefinition`, etc.

Third, `OperationComposition` and `CapabilityCompositionDefinition` still encode trigger→consequence composition.  

That directly conflicts with MS-PROT-020:

> **A universal trigger → consequence operation-composition mechanism is rejected.** 

And it is not merely dead source: the old `CapabilityCompiler` still accepts `OperationComposition`, and tests still reference it.  

So eventually that old semantic path must be removed or explicitly quarantined as obsolete prototype evidence.

---

# Semantic verdict

The primitive architecture itself remains sound, but the **original primitive vocabulary does not**.

The corrected construction should look more like:

```text
PLATFORM INVARIANTS
        ↓
LOW-LEVEL SEMANTIC MECHANISMS
        │
        ├── authoritative effects
        ├── evaluations
        └── capacity claims
        ↓
ABSTRACTIONS / REUSABLE MACHINERY
        ↓
CAPABILITY-OWNED
OPERATIONS + OBJECTS + RELATIONSHIPS
        ↓
POLICIES / REQUIREMENTS
        ↓
MERCHANT CONFIGURATION
```

And the most consequential decisions from this review are:

```text
Resource must be disambiguated.

Operation remains capability-owned, not primitive.

State/Transition remain owner-scoped.

Booking, Appointment and Enquiry are not primitives.

Offering is no longer a primitive.

Schedule is no longer a primitive.

Request/Reserve/Confirm/Cancel/etc.
are not universal primitive verbs.

Allocation retains a low-level,
cross-domain semantic contract.

The effect algebra needs typed data mutation.

Cross-object typed relationships require
their own semantic review before we accept
the Enquiry subject model.
```

### Governance verdict

**Primitive semantics: REVISE.**

This is not a rejection of the architecture. It is a necessary pruning of the exploratory vocabulary now that the domain has become much clearer.

Before we accept **MS-PROT-043**, I recommend we resolve exactly **three semantic questions** in order:

1. **OperationalObject vs Resource** — what owns lifecycle, and what specifically counts as an allocatable Resource?
2. **Authoritative effect algebra** — creation, transition, data mutation, allocation/release, and whether relationship mutation belongs here.
3. **Typed relationship model** — particularly `Enquiry → Listing`, `Appointment → CustomerContext`, and similar cross-capability references.

Once those three survive falsification, we can review MS-PROT-043 against a considerably cleaner semantic foundation.
