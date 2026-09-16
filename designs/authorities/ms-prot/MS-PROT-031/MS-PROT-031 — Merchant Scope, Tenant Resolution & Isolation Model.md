# MS-PROT-031 — Merchant Scope, Tenant Resolution & Isolation Model

**Version:** 1.1  
**Status:** **Accepted**  
**Depends on:** MS-PROT-028, MS-PROT-029, MS-PROT-030  
**Purpose:** Define how one Main Street platform safely hosts many independent merchants and organisations, determines which merchant a request belongs to, and prevents data, authority, configuration and execution from crossing merchant boundaries.

## 1. Governing principle

> **Every merchant-owned operation, resource, relationship, configuration and projection executes within an explicit Merchant Scope. Tenant context must be established at a trusted application boundary and must never be inferred from an untrusted business-object identifier alone.**

```text
Incoming request
      ↓
Resolve Merchant Scope
      ↓
Establish Actor/Authority
      ↓
Execute merchant-scoped use case
      ↓
Merchant-scoped persistence/projection
```

## 2. Merchant is the primary tenant

The primary tenant boundary is the `Merchant` operating entity.

It is not:

```text
business category
website/domain
physical premises
branch/location
merchant owner/controller identity
customer
```

A merchant may be a physical retailer, online consultant, information publisher, professional practice, service operator or hybrid organisation. Location/physical presence does not determine tenancy.

## 3. Why Merchant Scope exists

Main Street may host many independently operated merchants and organisations on shared application infrastructure.

Merchant Scope prevents:

```text
Merchant A reading Merchant B data
Merchant A allocating Merchant B resources
Merchant A using Merchant B configuration
Merchant A staff acting in Merchant B context
Merchant A public/customer identifiers resolving into Merchant B operations
```

Isolation is a semantic/application invariant, not merely a database convention.

## 4. Trusted tenant resolution

Merchant Scope may be established from trusted routing/authentication/application context such as:

```text
verified merchant session
resolved storefront/domain mapping
trusted API credential
validated public merchant route
validated integration configuration
```

The exact mechanism varies by surface.

Invalid approach:

```text
request says merchant_id = X
therefore scope = X
```

unless that value has been resolved/validated against trusted context.

## 5. Merchant controller and tenant are distinct

A verified merchant-controller identity may control one or more merchants according to explicit relationships.

```text
Controller Identity
        ↓ authorised relationship
Merchant Scope
```

Controller identity verification does not establish a new tenant boundary and does not imply that the merchant business itself has been verified.

## 6. Staff and customer relationships

Staff access is merchant-scoped. A staff member's authority for Merchant A must not automatically apply to Merchant B.

Customer identity/relationship is also evaluated in merchant/business context; a customer reference alone does not grant access to every transaction belonging to that merchant or platform.

## 7. Platform-scoped state

Not every object is merchant-owned.

Main Street may also have platform-scoped state such as platform configuration, semantic definitions or integration metadata.

Therefore:

```text
merchant-scoped
platform-scoped
other explicitly defined scope
```

must remain distinguishable.

## 8. Persistence isolation

The initial architecture may use shared physical persistence while preserving logical tenant ownership.

Shared database does not mean shared write authority.

Every merchant-scoped repository/query/mutation must include or derive the correct Merchant Scope, and persistence constraints should reinforce isolation where practical.

Exact database isolation strategy is specified downstream.

## 9. Runtime isolation

A command cannot cross merchant scope merely because resource identifiers are globally unique or guessable.

Execution must verify that:

```text
operation is permitted in merchant model
target belongs/is valid in merchant scope
principal has authority in merchant scope
participating state/resources are correctly scoped
```

Cross-merchant operations require an explicitly designed platform capability; they are never the default.

## 10. Projection and storefront isolation

Public/customer/merchant projections must resolve from the correct Merchant Scope.

A storefront route or custom domain maps to a merchant; it does not make the domain/website itself the tenant.

The dashboard likewise derives from the current merchant scope rather than the signed-in person's global identity alone.

## 11. Configuration isolation

Each merchant has its own active configuration/resolved operational model. Activation or policy changes for one merchant must not alter another merchant's runtime semantics.

Registered platform semantics remain shared; merchant configuration remains isolated.

## 12. External integrations

Integration credentials/configuration must be associated with the correct semantic scope.

Examples:

```text
Google Business Profile link
payment account
external scheduler/calendar
social account
```

A provider relationship does not redefine Merchant Scope. External providers retain authority for their own claims/facts.

## 13. Falsification findings

Rejected assumptions:

| Failed assumption | Why it fails |
|---|---|
| Owner/controller identity is the tenant | One person may control multiple merchants and identity has separate lifecycle |
| Website/domain is the tenant | Domains can change; multiple domains/surfaces may map to one merchant |
| Physical branch is the tenant | Online/non-physical merchants and multi-location merchants disprove it |
| Business category determines tenant boundary | Category is contextual, not ownership |
| Globally unique IDs make scope checks unnecessary | IDs do not grant authority |
| Shared database means shared module/tenant access | Physical co-location is not semantic ownership |

## 14. Accepted invariants

1. Merchant is the primary tenant/operating scope.
2. Merchant Scope is explicit and established at trusted boundaries.
3. Business category, website, controller identity and physical location do not define the tenant.
4. Merchant-scoped state/configuration/authority cannot cross scopes accidentally.
5. Staff/customer authority remains contextual to Merchant Scope.
6. Platform-scoped state remains distinguishable from merchant-scoped state.
7. Shared physical infrastructure does not weaken semantic isolation.
8. Public/storefront routing resolves Merchant Scope but does not become the tenant definition.
9. External integrations belong to a scope but do not redefine it.
10. The tenancy model supports physical, online, informational and hybrid merchants equally.

## 15. Deferred decisions

Tenant-key schema, row-level security, schema/database-per-tenant alternatives, custom-domain resolution implementation and cross-merchant administrative operations remain downstream decisions.

## Governance verdict

**ACCEPTED.** The 20 August 2026 handover requires only merchant-neutral wording; the Merchant Scope architecture remains valid.
