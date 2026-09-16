# MS-PROT-045 v1.1 — Field-Targeted Mutation & Schema Identity Amendment

**Document ID:** MS-PROT-045  
**Version:** 1.1  
**Status:** **ACCEPTED after targeted falsification and manual approval**  
**Amends:** MS-PROT-045 v1.0  
**Depends on:** MS-PROT-020 v1.4, MS-PROT-021, MS-PROT-022, MS-PROT-023, MS-PROT-031, MS-PROT-044 v1.0, MS-PROT-045 v1.0, MS-PROT-046 v1.0  
**Purpose:** Resolve the implementation-blocking ambiguity around authoritative `MUTATE_DATA`, schema identity, field identity and DataConcept scope without turning Main Street into a merchant-template system, arbitrary object patch engine or vertical-specific runtime.

---

# 1. Governing decision

Main Street shall make authoritative data mutation **field-targeted**.

> **A `MUTATE_DATA` effect authorises mutation of a registered `FieldDefinition` belonging to the schema governing a target Operational Object. It does not authorise mutation of a Java property, JSON path, database column, arbitrary key/value entry or DataConcept directly.**

Canonical flow:

```text
Capability
    ↓ owns
OperationalObjectDefinition
    ↓ governed by
SchemaDefinition @ immutable version
    ↓ contains
FieldDefinition
    ↓ may bind to
DataConcept / capability-local concept / field-specific semantics

OperationDefinition
    ↓ declares
MUTATE_DATA
    ↓ targets
Operational Object + registered field
```

The capability-owned handler performs the authoritative mutation. The generic runtime/compiler validates conformance to the registered semantic contract; it does not become a universal object mutation engine.

---

# 2. Why DataConcept is not the mutation target

A reusable concept may participate in several distinct business fields.

Example:

```text
MONETARY_AMOUNT
    ↓
Listing.askingPrice
Listing.depositAmount
Offering.price
```

Therefore:

```text
MUTATE_DATA → MONETARY_AMOUNT
```

is ambiguous and rejected.

The operation must own authority over the contextual field:

```text
ChangeListingPrice
    ↓
MUTATE_DATA
    targetObject = Listing
    targetField  = asking-price
```

Hard invariant:

> **DataConcept supplies reusable semantic meaning; FieldDefinition supplies contextual business meaning; mutation authority attaches to the FieldDefinition.**

---

# 3. Canonical schema identity

Every executable schema is identified by:

```text
SchemaIdentity
    ownerCapabilityIdentifier
    schemaIdentifier
    schemaVersion
```

Example:

```text
ownerCapabilityIdentifier = listing
schemaIdentifier          = listing
schemaVersion             = 1
```

A schema version is immutable once published in a semantic registry release.

A material schema change creates a new schema version rather than mutating the meaning of an already-published version.

The schema version model exists to preserve:

```text
historical interpretability
compiler determinism
configuration provenance
operational-object affinity
safe future evolution
```

This decision does **not** require the complete schema-migration subsystem to be implemented in the current slice. It defines the identity contract that later migration machinery must respect.

---

# 4. Operational Object schema affinity

An `OperationalObjectDefinition` may reference one governing schema version.

Conceptually:

```text
OperationalObjectDefinition
{
    identifier
    schemaReference?
    lifecycle?
    other capability-owned roles
}
```

An Operational Object that owns no structured mutable business data may omit a schema reference.

However:

> **Any Operational Object targeted by `MUTATE_DATA` must resolve to a governing registered schema version.**

The object definition is the single source of schema affinity for operation validation.

The mutation effect shall not independently carry a second competing schema version.

This prevents drift such as:

```text
Operational Object → ListingSchema v2
Operation effect   → ListingSchema v1
```

---

# 5. Canonical field identity

A `FieldDefinition` has a stable semantic identifier unique within its governing schema version.

Example:

```text
schema: listing@1
field: asking-price
```

Field identity is independent of implementation names.

Therefore the following are implementation details and are not semantic identities:

```text
Java:        askingPrice
SQL:         asking_price
JSON:        askingPrice
UI label:    Asking price
```

The semantic identity remains:

```text
listing@1 / asking-price
```

Hard invariant:

> **Storage and presentation names shall not define Main Street field identity.**

---

# 6. Mutation target contract

A registered mutation effect shall identify:

```text
MUTATE_DATA
{
    targetOperationalObjectIdentifier
    targetFieldIdentifier
}
```

The schema identifier/version is resolved through the target Operational Object definition.

The compiler must prove:

```text
1. target Operational Object exists;
2. target object is owned by the operation's capability;
3. target object has a governing schema;
4. governing schema/version exists;
5. target FieldDefinition exists in that schema;
6. the effect therefore references a registered semantic field.
```

If any condition fails, the semantic definition or compilation candidate is invalid.

The resolved executable effect may retain the fully resolved field reference for provenance:

```text
owner capability
schema identifier
schema version
field identifier
```

but the registered operation does not duplicate schema affinity.

---

# 7. Cross-capability mutation is prohibited by default

A capability may not directly mutate another capability's authoritative field merely because that field is visible in a schema or read model.

Rejected:

```text
Booking capability
    ↓ direct MUTATE_DATA
CustomerContext.email
```

Instead:

```text
Booking capability
    ↓ requests/invokes supported semantic behaviour
Customer capability operation
    ↓ owns mutation
CustomerContext.email
```

Hard invariant:

> **Read visibility does not grant mutation authority. Cross-capability authoritative data changes must pass through semantics owned by the capability that owns the target object/field, unless an explicit accepted semantic contract grants otherwise.**

This preserves capability encapsulation and prevents schemas becoming a back door around ownership.

---

# 8. DataConcept scope is now explicit

MS-PROT-045 v1.0 recognised three useful scopes. v1.1 resolves how they participate in schema definition.

## 8.1 Global DataConcept

A reusable concept whose meaning is stable across capabilities.

Examples:

```text
EMAIL_ADDRESS
PHONE_NUMBER
TIME_INTERVAL
MONETARY_AMOUNT
```

Global DataConcept definitions belong to the semantic registry release.

## 8.2 Capability DataConcept

A reusable concept whose meaning is intentionally local to one capability.

Examples may include:

```text
PropertyBedroomCount
OpportunityFundingType
```

Capability DataConcept definitions belong to their `RegisteredCapability`.

## 8.3 Field-specific semantics

When no separate reusable DataConcept is justified, the FieldDefinition itself is the semantic authority for that field.

It does not require creation of a synthetic global or capability DataConcept.

Hard invariant:

> **A field must not be promoted to a reusable DataConcept merely to satisfy implementation structure. Reuse must justify registration.**

---

# 9. Field semantic binding

Each field has exactly one semantic basis:

```text
FieldDefinition
    ├── GlobalDataConceptReference
    ├── CapabilityDataConceptReference
    └── FieldSpecificSemantics
```

The compiler/registry validator must reject unresolved DataConcept references.

A capability-scoped reference may resolve only within the owning capability unless another accepted contract explicitly permits cross-capability semantic reuse.

This eliminates untyped concept-name strings while avoiding a universal ontology for every field.

---

# 10. Nested structured data

Nested data shall not be addressed through arbitrary string paths.

Rejected semantic target:

```text
"address.postcode"
```

Instead, nested fields are registered semantic field nodes.

A schema may model structure such as:

```text
address
    ├── building-number
    ├── street
    ├── locality
    ├── postcode
    └── country
```

Each independently addressable nested field has its own stable field identifier within the schema.

A nested field may retain a parent-field relationship for structure, but mutation targets the registered field identity rather than interpreting a dynamic path.

Where the capability treats a structured value atomically, only the parent structured field needs to be mutation-addressable.

Therefore:

```text
ChangePropertyAddress
    may own MUTATE_DATA → address
```

while another capability contract could deliberately expose:

```text
CorrectPropertyPostcode
    may own MUTATE_DATA → postcode
```

if independent mutation is semantically valid.

---

# 11. Capability-owned handler remains authoritative

`MUTATE_DATA` is a declarative effect contract.

It does not instruct the generic runtime how to patch an object.

Canonical execution remains:

```text
resolved operation
        ↓
execution guards / authority checks
        ↓
capability-owned handler
        ↓
authoritative domain mutation
        ↓
reported fulfilment/effects
        ↓
generic conformance validation
```

Rejected:

```text
Generic runtime
    ↓ reads field name
    ↓ reflects over Java object / applies JSON patch
    ↓ mutates arbitrary state
```

This preserves MS-PROT-023.

---

# 12. One effect authorises one semantic field

If one operation legitimately changes several fields, it declares several bounded mutation effects.

Example:

```text
UpdateListingMarketing
    MUTATE_DATA → Listing.headline
    MUTATE_DATA → Listing.description
    MUTATE_DATA → Listing.marketing-media
```

Rejected:

```text
MUTATE_DATA → Listing.*
```

or:

```text
MUTATE_DATA → arbitrary payload
```

This makes mutation authority statically inspectable and supports future audit, compatibility analysis, permissions and AI restrictions without changing the semantic model.

---

# 13. Generic PATCH remains rejected

Transport technology may use HTTP `PATCH`, but the application/domain boundary must resolve the request to a registered capability-owned operation.

Rejected semantic operations include generic forms such as:

```text
UpdateObject
SetProperty
ModifyAnyField
SaveArbitraryChanges
```

when their meaning is unrestricted mutation.

Accepted semantics are intent-specific operations such as:

```text
ChangeListingPrice
ChangeCustomerEmail
RescheduleAppointment
ChangeBookingPartySize
```

provided each operation declares its permitted effects.

---

# 14. No-template scalability invariant

The schema model shall not become a disguised merchant-template system.

Main Street must continue to support arbitrarily many merchants by compiling reusable platform/capability semantics with merchant configuration and merchant-owned values.

Canonical scaling model:

```text
Platform semantic primitives
        ↓
Reusable capability-owned definitions
        ↓
Operational Objects + Schemas + Fields + Operations + Policies
        ↓
Merchant configuration
        ↓
Compiler
        ↓
Merchant-specific executable model
```

Rejected scaling model:

```text
RestaurantTemplate
SalonTemplate
SolicitorTemplate
ScholarshipTemplate
ConsultantTemplate
MechanicTemplate
...
```

Hard invariants:

1. **Merchant count shall not drive schema-definition count.**
2. **Business category shall not be treated as schema identity by default.**
3. **A schema shall describe a reusable semantic context or object, not merely reproduce a merchant vertical or UI layout.**
4. **A new vertical-specific schema is justified only when it represents genuinely distinct capability-owned semantics that cannot be represented correctly through existing composition.**
5. **Merchant configuration selects supported capabilities, policies and values; it does not generate new executable schemas.**
6. **Storefront and dashboard composition remain projections of the active configuration graph, not templates selected by business category.**
7. **Adding the millionth merchant should normally create more configuration and operational data, not more application code or merchant-specific schema classes.**

This does not prohibit genuinely domain-specific semantics such as `Property`, `Opportunity` or another materially distinct Operational Object. It prohibits using domain labels as a substitute for semantic composition.

---

# 15. Falsification

## 15.1 Listing price

Required:

```text
ChangeListingPrice
    MUTATE_DATA → Listing.asking-price
```

`asking-price` may use a reusable Money/Monetary DataConcept but retains contextual Listing meaning.

No generic `price` mutation and no Java property path are required.

**PASS**

## 15.2 Appointment rescheduling

Required:

```text
RescheduleAppointment
    MUTATE_DATA → Appointment.scheduled-interval
```

The field may bind to `TIME_INTERVAL` while the operation remains Appointment-owned.

Allocation/scheduling consistency may require additional effects or authority checks; field targeting does not collapse those semantics.

**PASS**

## 15.3 CustomerContext email

Required:

```text
ChangeCustomerEmail
    MUTATE_DATA → CustomerContext.email
```

`email` may bind to global `EMAIL_ADDRESS`.

A Booking capability cannot directly mutate this field merely because it can read CustomerContext.

**PASS**

## 15.4 Booking reservation scope

A Booking may expose one capability-owned reservation-scope field or several registered constituent fields depending on the accepted Booking schema.

An operation may mutate only the registered field boundaries it owns.

No arbitrary payload or reflection is needed.

**PASS**

## 15.5 Structured Property address

Address may be atomic for one operation or expose independently registered nested fields where domain semantics justify it.

No dynamic `"address.postcode"` path is required.

**PASS**

## 15.6 Information publisher

An Opportunity or PublishedContent object can use the same schema/field machinery:

```text
Opportunity.deadline
Opportunity.provider
PublishedContent.body
```

No `ScholarshipWebsiteTemplate` or publication-specific runtime branch is required.

**PASS**

## 15.7 Consultant

Consultant merchants can compose:

```text
Offering
Appointment
CustomerContext
Enquiry
Publication
```

using reusable semantic definitions.

No consultant template is required.

**PASS**

## 15.8 Million-merchant test

Assume one million merchants across overlapping business models.

The design requires:

```text
one million merchant configurations / operational datasets
```

but does not require:

```text
one million schemas
one million templates
one million code branches
```

Schema growth occurs only when the platform accepts genuinely new reusable semantics.

**PASS**

---

# 16. Rejected alternatives

The following are rejected:

1. `MUTATE_DATA` targeting DataConcept directly.
2. `MUTATE_DATA` targeting Java property names.
3. `MUTATE_DATA` targeting SQL column names.
4. `MUTATE_DATA` targeting JSON/property paths.
5. Generic arbitrary domain patch operations.
6. Runtime reflection as semantic mutation machinery.
7. One operation receiving unrestricted whole-object mutation authority.
8. Cross-capability mutation merely because a field is readable.
9. Every field promoted into a global DataConcept.
10. Merchant-created executable schemas.
11. Business-category templates masquerading as schemas.
12. Storefront template identity determining domain semantics.
13. Schema version silently changing after publication.
14. Mutation effect carrying an independent schema version that can diverge from its target Operational Object.

---

# 17. Accepted invariants added by v1.1

1. Authoritative data mutation is field-targeted.
2. Mutation authority attaches to contextual FieldDefinition, not DataConcept.
3. Schema identity includes owner capability, schema identifier and immutable version.
4. Operational Object schema affinity is the source of schema resolution for mutation effects.
5. A `MUTATE_DATA` target identifies Operational Object plus registered field.
6. Compiler validation resolves and verifies the governing schema and field.
7. Storage names and presentation labels do not define semantic field identity.
8. Global DataConcept definitions belong to the semantic registry release.
9. Capability DataConcept definitions belong to their capability.
10. Field-specific semantics require no artificial reusable DataConcept.
11. Nested mutation uses registered field identities, not arbitrary property paths.
12. Capability-owned handlers perform authoritative mutation.
13. Generic runtime does not become a universal patch engine.
14. One mutation effect authorises one semantic field boundary.
15. Cross-capability direct mutation is prohibited by default.
16. Generic domain PATCH semantics remain rejected.
17. Merchant count does not drive schema/template count.
18. Business category is not schema identity by default.
19. Merchant-specific executable schema generation is rejected.
20. Storefront/dashboard composition remains configuration-graph projection.
21. New schemas require genuinely distinct reusable semantics, not merely a new merchant niche.

---

# 18. Implementation consequence

The current implementation's broad resource model and two-effect algebra are not sufficient for this accepted contract.

The coherent migration must establish, in order:

```text
OperationalObjectDefinition
SchemaDefinition + immutable schema version
FieldDefinition + typed semantic binding
registered relationship definitions
full authoritative effect algebra
    CREATE_OBJECT
    TRANSITION_STATE
    MUTATE_DATA
    ESTABLISH_RELATIONSHIP
    REMOVE_RELATIONSHIP
    CLAIM_ALLOCATION
    RELEASE_ALLOCATION
        ↓
compiler validation/materialisation
        ↓
executable model
        ↓
capability-owned handler conformance
```

The implementation must not preserve the old broad `Resource` abstraction as the canonical object model merely to minimise code churn.

The migration must remain tests-first and must not introduce merchant-template or business-category branching.

---

# 19. Governance verdict

## PROPOSE

Field-targeted mutation with schema-governed Operational Objects was proposed.

## FALSIFY

The proposal was tested against:

```text
Listing price
Appointment rescheduling
CustomerContext email
Booking reservation scope
structured Property address
information publishing
consulting
million-merchant scaling
```

No case requires arbitrary object patching, merchant-generated executable schema or business-category templates.

**PASS**

## MANUAL APPROVAL

The direction was manually approved on 21 August 2026 with the explicit constraint that it must preserve Main Street's core ability to support millions of businesses without writing a template for each business or niche.

**PASS**

## ACCEPT

**MS-PROT-045 v1.1 is ACCEPTED.**

---

# 20. Canonical decision

> **Main Street shall authorise structured business-data mutation through registered field identities governed by immutable capability-owned schema versions. `MUTATE_DATA` identifies a target Operational Object and one registered FieldDefinition; the compiler resolves the object's governing schema, verifies the field and materialises the resolved semantic reference. DataConcepts provide reusable meaning but are not mutation targets. Global DataConcepts belong to the semantic registry release, capability-scoped DataConcepts belong to their capability, and field-specific semantics require no artificial global concept. Nested values are addressed through registered semantic field nodes rather than arbitrary property paths. Capability-owned handlers remain authoritative for mutation, and cross-capability direct field mutation is prohibited unless an accepted owning contract explicitly grants it. This schema machinery is reusable semantic infrastructure, not a merchant-template system: business category does not define schema identity, merchant configuration cannot generate executable schemas, storefront/dashboard composition remains a projection of the active capability graph, and growth from one merchant to millions should primarily create additional configurations and operational data rather than templates, code branches or merchant-specific schema classes.**
