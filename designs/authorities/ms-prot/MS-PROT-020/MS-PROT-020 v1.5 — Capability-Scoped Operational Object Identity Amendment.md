# MS-PROT-020 v1.5 — Capability-Scoped Operational Object Identity Amendment

**Document ID:** MS-PROT-020  
**Version:** 1.5  
**Status:** **ACCEPTED after targeted review and manual approval**  
**Amends:** MS-PROT-020 v1.4  
**Depends on:** MS-PROT-020 v1.4, MS-PROT-022, MS-PROT-023, MS-PROT-045 v1.1  
**Purpose:** Resolve the implementation-blocking ambiguity over Operational Object definition identity without introducing a global object-name namespace, merchant templates or cross-capability authority leakage.

---

## 1. Governing decision

Operational Object definitions are capability-owned and therefore use **capability-scoped semantic identity**.

Canonical identity:

```text
OperationalObjectTypeIdentity
{
    ownerCapabilityIdentifier
    objectIdentifier
}
```

Examples:

```text
booking / booking
appointment / appointment
customer / customer-context
publication / opportunity
```

Hard invariant:

> **A capability-owned Operational Object definition is identified by its owning capability plus its local object identifier. A bare object identifier is not globally authoritative.**

---

## 2. Why global object-name uniqueness is rejected

Two unrelated capabilities may legitimately use the same local semantic noun.

Example:

```text
support / case
legal / case
```

Requiring global uniqueness would force otherwise independent capabilities to coordinate internal naming and would turn the semantic registry into a growing global namespace.

Rejected:

```text
case
case
→ collision
```

Accepted:

```text
support / case
legal / case
→ distinct semantic identities
```

This preserves independent capability evolution and Main Street's composition model.

---

## 3. Registration boundary

Within one `RegisteredCapability`:

- local Operational Object identifiers must remain unique;
- object lifecycle, schema affinity and other owned semantics remain capability-owned;
- the owning capability supplies the namespace.

Therefore an `OwnedOperationalObjectDefinition` may retain a local `objectIdentifier` without duplicating the owner identifier inside every owned definition.

The fully qualified identity is established by registration and retained by the compiler in the executable model.

---

## 4. Executable identity

The compiler shall materialise the fully qualified identity into the executable model.

Conceptually:

```text
ExecutableOperationalObjectDefinition
{
    ownerCapabilityIdentifier
    objectIdentifier
    lifecycle?
    schemaReference?
}
```

Runtime lookup shall not rely on a globally unique bare object identifier.

Canonical lookup:

```text
operationalObject(ownerCapabilityIdentifier, objectIdentifier)
```

A convenience lookup by bare identifier is permitted only where ambiguity has already been eliminated by an enclosing capability-owned context. It must not become the authoritative global lookup contract.

---

## 5. Operation-effect ownership

Operations are capability-owned. Within a registered operation definition, a local target object identifier is interpreted in the namespace of the operation's owning capability.

Example:

```text
booking capability
    operation: ChangeReservationScope
    MUTATE_DATA
        object = booking
        field  = reservation-scope
```

The compiler resolves this to:

```text
booking / booking
    ↓ governing schema
booking / booking-schema@1
    ↓ field
reservation-scope
```

An operation cannot use an unqualified local identifier to acquire authority over an object owned by another capability.

---

## 6. Cross-capability mutation remains prohibited

This amendment preserves MS-PROT-045 v1.1.

Rejected:

```text
booking capability
    MUTATE_DATA → customer / customer-context.email
```

unless a separate accepted semantic contract explicitly grants that authority.

The existence or visibility of another capability's Operational Object does not transfer mutation authority.

---

## 7. Typed relationship endpoints

A relationship source remains owned by the source capability. Cross-capability target endpoints must resolve to explicit semantic type identity rather than a globally assumed bare object string.

Conceptually:

```text
RelationshipDefinition
{
    source = booking / booking
    role = CUSTOMER
    target = customer / customer-context
}
```

Hard invariant:

> **Cross-capability relationship endpoints must retain enough identity to resolve the target semantic owner unambiguously.**

Reference authority remains distinct from mutation authority.

---

## 8. Schema affinity remains owner-scoped

MS-PROT-045 v1.1 already establishes schema identity as:

```text
ownerCapabilityIdentifier
schemaIdentifier
schemaVersion
```

Operational Object identity now follows the same ownership principle:

```text
Capability
    ├── Operational Object local identity
    ├── Schema local identity @ version
    ├── Operations
    └── other owned semantics
```

This alignment avoids mixing capability-scoped schemas with globally-scoped object names.

---

## 9. Merchant configuration remains unchanged

Merchant configuration does not gain a requirement to supply fully qualified object identities.

Canonical flow remains:

```text
merchant selects supported capabilities/policies/values
        ↓
compiler resolves capability-owned definitions
        ↓
fully qualified executable semantic identities
```

This amendment therefore adds no merchant-facing complexity and does not introduce merchant-authored semantics.

---

## 10. No-template scalability invariant

This amendment strengthens, rather than weakens, Main Street's no-template architecture.

A million merchants may compile from reusable capability-owned definitions without producing a million object types, schemas or code branches.

Likewise, adding a new capability does not require renaming its local semantics merely because another capability already uses the same local noun.

Hard invariants:

1. Merchant count does not drive Operational Object definition count.
2. Business category does not define Operational Object identity.
3. Capability ownership provides semantic namespace.
4. Local object names may repeat across unrelated capabilities.
5. The compiler, not the merchant, materialises fully qualified executable identities.
6. No business-specific runtime branch is introduced by this identity model.

---

## 11. Falsification

### 11.1 Same local name in two capabilities

```text
support / case
legal / case
```

Both may be active without collision.

**PASS**

### 11.2 Booking and Customer relationship

```text
booking / booking
    CUSTOMER → customer / customer-context
```

The target owner remains explicit and Booking does not gain Customer mutation authority.

**PASS**

### 11.3 Field-targeted mutation

```text
booking / booking
    schema → booking / booking@1
    field  → reservation-scope
```

`MUTATE_DATA` remains field-targeted and capability-owned.

**PASS**

### 11.4 Million-merchant composition

Merchant instances create configuration and operational data, not merchant-specific object namespaces.

**PASS**

### 11.5 Information publisher

A publication capability may define `publication / item` while another capability independently defines its own `item` without forcing vertical naming conventions.

**PASS**

---

## 12. Rejected alternatives

The following are rejected:

1. Global uniqueness of bare Operational Object identifiers.
2. Merchant-defined Operational Object namespaces.
3. Business-category prefixes used as a substitute for capability ownership.
4. Cross-capability target resolution by ambiguous bare strings.
5. Inferring mutation authority from object-name equality.
6. Introducing templates to avoid semantic-name collisions.

---

## 13. Accepted invariants added by v1.5

1. Operational Object definitions are capability-scoped semantic types.
2. Canonical object type identity is `(ownerCapabilityIdentifier, objectIdentifier)`.
3. Local object identifiers are unique only within their owning capability.
4. The compiler materialises fully qualified object identity into the executable model.
5. Runtime global lookup by bare object identifier is not authoritative.
6. Operation-local target identifiers resolve within the owning capability namespace.
7. Cross-capability relationship targets must resolve explicit semantic ownership.
8. Cross-capability mutation remains prohibited unless separately authorised.
9. Merchant configuration remains free of object-identity authoring.
10. Capability-scoped identity must not become a merchant-template mechanism.

---

## 14. Implementation consequence

The MS-PROT-020 v1.4 migration shall not merge while `ExecutableMerchantModel` treats Operational Object identifiers as globally unique bare strings.

The implementation must:

```text
1. retain local identifiers in capability-owned registered definitions;
2. introduce/retain explicit owner-qualified executable object identity;
3. validate uniqueness by owner + local object identifier;
4. resolve operation targets in the operation owner's capability namespace;
5. make cross-capability relationship targets owner-qualified;
6. add tests proving two active capabilities may own the same local object identifier;
7. preserve MS-PROT-045 v1.1 field-targeted mutation authority.
```

Only after those conditions and the existing v1.4/v1.1 conformance tests pass may the coherent migration be considered mergeable into `development`.
