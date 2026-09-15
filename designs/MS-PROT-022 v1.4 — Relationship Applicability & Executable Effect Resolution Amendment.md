# MS-PROT-022 v1.4 — Relationship Applicability & Executable Effect Resolution Amendment

**Document ID:** MS-PROT-022  
**Version:** 1.4  
**Status:** **ACCEPTED after targeted falsification and manual approval**  
**Amends:** MS-PROT-022 v1.3  
**Depends on:** MS-PROT-020 v1.5, MS-PROT-022 v1.3, MS-PROT-023, MS-PROT-043 v1.2  
**Purpose:** Define when a registered typed relationship participates in one merchant's resolved operational model without turning relationship existence into hidden capability activation.

---

## 1. Governing decision

A registered relationship is a **potential semantic contract**.

> **A registered typed relationship becomes applicable in one merchant's executable model only when both the relationship source capability and the target Operational Object owner capability are active in that resolved merchant model. The relationship itself never activates either capability.**

Capability activation remains governed by MS-PROT-022 v1.3:

```text
merchant-selected capabilities
        +
transitive REQUIRES closure
        ↓
effective active capability set
```

Relationship applicability is evaluated only after that active set has been resolved.

---

## 2. Why relationship existence must not activate capabilities

MS-PROT-043 permits Enquiry to define optional SUBJECT relationships to several supported semantic targets:

```text
Enquiry → Listing
Enquiry → Product
Enquiry → Opportunity
Enquiry → Appointment
Enquiry → Booking
```

An Enquiry may also have no SUBJECT.

If the mere presence of those registered relationship definitions activated their target capabilities, enabling Enquiry would transitively activate every possible subject capability.

Rejected:

```text
Enquiry active
    ↓
registered SUBJECT relationships exist
    ↓
Listing active
Product active
Opportunity active
Appointment active
Booking active
```

That would create hidden dependency expansion and eventually capability explosion.

Canonical rule:

> **Structural reference possibility is not capability dependency.**

---

## 3. Registry validity versus merchant applicability

Two questions are distinct.

### Registry validity

> Does the relationship reference a real registered target semantic type?

Example:

```text
Enquiry / enquiry
    SUBJECT → publication / opportunity
```

The semantic registry must reject the definition if:

```text
publication / opportunity
```

is not a registered Operational Object type in that registry release.

### Merchant applicability

> Are both semantic owners active in this merchant's resolved model?

Example:

```text
Merchant active capabilities:
    enquiry
    publication
```

Then:

```text
Enquiry → Opportunity
```

may be materialised.

If Publication is inactive, the registered relationship remains valid registry semantics but is not part of that merchant's executable model.

---

## 4. Resolution order

Canonical compilation order:

```text
1. validate registry references
2. resolve merchant-selected capabilities
3. expand transitive REQUIRES closure
4. reject active conflicts
5. establish effective active capability set
6. materialise active Operational Object types
7. determine applicable registered relationships
8. materialise applicable relationship effects
9. materialise the remaining executable operation contract
```

Relationship applicability must not feed back into step 2 or step 3.

This prevents circular semantic authority:

```text
relationship exists
    ↓
activates target
    ↓
relationship becomes applicable
```

---

## 5. Relationship applicability rule

For a registered relationship:

```text
source owner = S
target owner = T
```

it is applicable to a resolved merchant model iff:

```text
S ∈ active capabilities
AND
T ∈ active capabilities
```

The source owner will normally already be active because relationships are materialised only from active capability definitions.

The target-owner check is nevertheless explicit and authoritative.

---

## 6. Example — Enquiry and Listing

Registered semantics:

```text
Enquiry capability
    Operational Object: enquiry
    Relationship: SUBJECT → listing / listing

Listing capability
    Operational Object: listing
```

Merchant A:

```text
active:
    enquiry
    listing
```

Executable model:

```text
Enquiry SUBJECT → Listing
```

Merchant B:

```text
active:
    enquiry
```

Executable model:

```text
no Enquiry → Listing relationship
```

The registry definition remains unchanged in both cases.

---

## 7. Relationship effects are applicability-sensitive

A registered operation may include relationship effects that refer to registered relationship definitions.

Example:

```text
CreateEnquiry
    CREATE_OBJECT enquiry
    ESTABLISH_RELATIONSHIP enquiry.subject-listing
    ESTABLISH_RELATIONSHIP enquiry.subject-opportunity
```

Suppose the merchant has:

```text
enquiry active
listing active
publication inactive
```

Then the executable operation may contain:

```text
CREATE_OBJECT enquiry
ESTABLISH_RELATIONSHIP enquiry.subject-listing
```

and omit:

```text
ESTABLISH_RELATIONSHIP enquiry.subject-opportunity
```

because the referenced relationship is not applicable to that merchant release.

The same rule applies to:

```text
REMOVE_RELATIONSHIP
```

---

## 8. Effect filtering is bounded

This amendment permits applicability filtering only for relationship effects whose referenced registered relationship is not applicable after capability resolution.

It does not authorise the compiler to remove arbitrary authoritative effects.

The compiler shall not conditionally discard:

```text
CREATE_OBJECT
TRANSITION_STATE
MUTATE_DATA
CLAIM_ALLOCATION
RELEASE_ALLOCATION
```

merely because of unrelated capability composition.

Those effects remain governed by their own accepted semantic contracts.

---

## 9. Operation authority remains capability-owned

Filtering an inapplicable relationship effect does not transfer operation authority.

The owning capability still owns the operation.

The compiler only narrows the operation to relationship semantics that are valid in the resolved merchant model.

Runtime may execute only the captured executable effect set.

A capability handler may not reintroduce an omitted relationship effect at runtime.

---

## 10. REQUIRES remains the activation mechanism

If Capability A cannot operate coherently without Capability B, the semantic registry must express:

```text
A REQUIRES B
```

A mandatory relationship definition is not a substitute for a capability dependency.

Example:

```text
Booking requires CustomerContext semantics
```

If Booking cannot be validly resolved without Customer capability, that dependency belongs in the registered capability relationship graph.

Therefore:

```text
relationship cardinality
    ≠
capability activation rule
```

and:

```text
REQUIRED_ONE relationship
    ≠ automatically REQUIRES target capability
```

The two concepts serve different semantic purposes.

---

## 11. SUPPORTS and DEPENDS_ON remain non-activating

This amendment does not redefine the capability relationship meanings accepted by MS-PROT-022 v1.3.

In particular:

```text
SUPPORTS
```

continues to describe compatibility without activation.

```text
DEPENDS_ON
```

continues to have no executable activation meaning until separately specified.

A typed Operational Object relationship must not be used to smuggle activation behaviour into either concept.

---

## 12. Empty executable operation boundary

Current executable semantics require every executable operation to contain at least one authoritative effect.

If relationship-applicability filtering would remove every effect from a registered operation, this amendment does **not** invent a new zero-effect operation contract.

The compiler shall fail closed rather than manufacture behaviour.

A future requirement to retain or omit an operation whose complete authoritative effect set is inapplicable requires separate design evidence.

This preserves the current invariant without broadening operation semantics silently.

---

## 13. Falsification

### 13.1 Inactive optional target

Registry:

```text
Enquiry SUBJECT → Listing
```

Merchant activates only Enquiry.

Expected:

```text
Listing is not activated
relationship is absent from executable model
relationship effect is absent from executable operation
```

**PASS**

### 13.2 Explicitly active target

Merchant activates:

```text
Enquiry
Listing
```

Expected:

```text
relationship is materialised
relationship effect is materialised
```

**PASS**

### 13.3 Required target

Registry:

```text
Booking REQUIRES Customer
Booking relationship → CustomerContext
```

Merchant selects only Booking.

Expected:

```text
Customer activated by REQUIRES
relationship becomes applicable after closure
relationship effect is materialised
```

**PASS**

### 13.4 SUPPORTS target

Registry:

```text
Enquiry SUPPORTS Publication
Enquiry SUBJECT → Opportunity
```

Merchant selects only Enquiry.

Expected:

```text
Publication remains inactive
relationship remains inapplicable
```

**PASS**

### 13.5 Missing registered target

Registry relationship references:

```text
publication / opportunity
```

but no such registered Operational Object type exists.

Expected:

```text
registry publication rejected
```

**PASS**

---

## 14. No-template scalability consequence

This rule preserves independent reusable capability composition.

Adding a new supported Enquiry subject does not create:

```text
new merchant template
new business category
new Enquiry subclass
new runtime branch
```

It adds a registered potential relationship.

Each merchant's resolved model contains only the relationship subset supported by that merchant's active capability graph.

Merchant count therefore continues to drive configuration and operational data rather than semantic-definition proliferation.

---

## 15. Accepted invariants added by v1.4

1. A registered relationship is a potential semantic contract.
2. Relationship existence never activates a capability.
3. `REQUIRES` remains the only currently accepted transitive activation relationship.
4. Relationship applicability is evaluated after capability closure.
5. An executable relationship requires both source and target capability owners to be active.
6. Registry reference validity is distinct from merchant applicability.
7. Inapplicable relationship definitions are omitted from the merchant executable model.
8. `ESTABLISH_RELATIONSHIP` and `REMOVE_RELATIONSHIP` effects are materialised only when their referenced relationship is applicable.
9. Relationship-effect filtering cannot be generalised to arbitrary authoritative effects.
10. A capability handler cannot reintroduce an effect omitted from the captured executable operation.
11. Relationship cardinality does not imply capability activation.
12. If filtering would yield a zero-effect executable operation, compilation fails closed pending separate accepted semantics.

---

## 16. Implementation consequence

The ConfigurationCompiler shall:

```text
resolve active capabilities first
        ↓
materialise Operational Object types for active owners
        ↓
filter registered relationships by active target owner
        ↓
materialise only applicable relationships
        ↓
filter relationship effects against that applicable relationship set
        ↓
materialise executable operations
```

Tests must prove:

```text
inactive relationship target does not activate
inactive relationship is absent
inactive relationship effect is absent
active relationship target includes both relationship and effect
REQUIRES activation includes both relationship and effect
SUPPORTS does not activate
```

Only after those tests and the existing MS-PROT-020 v1.5 / MS-PROT-045 conformance suite pass may the migration be considered mergeable.