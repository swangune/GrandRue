# Storefront Customer Boundary Conformance — 26 August 2026

> **Status:** Implementation evidence only — not semantic/design authority  
> **Branch:** `development`  
> **Governing authority:** MS-PROT-036 v1.1 — Storefront Composition & Website Generation Model  
> **Implementation governance:** `designs/IMPLEMENTATION-RULES.md`

## 1. Implemented boundary

The merchant storefront route now keeps semantic/configuration diagnostics out of the customer-facing page while continuing to derive content and interactions from the backend-projected Surface composition.

Removed from rendered customer content:

- semantic release identifiers;
- raw merchant identifiers in the prototype navigation bar;
- Surface Contribution keys;
- unsupported-presentation developer diagnostics; and
- fallback copy referring to active semantics, contribution types or storefront-surface internals.

Unsupported Surface Contributions continue to fail closed in `composeStorefront`; the UI does not guess a component or operation for them.

## 2. Preserved authority boundaries

This correction does not change:

- merchant configuration or the Resolved Configuration Package;
- Surface registration, eligibility, Projection Serviceability or Exposure;
- public interaction bindings;
- Booking, Appointment or Ordering command contracts;
- server-side revalidation, idempotency or runtime authorisation; or
- business-type-independent storefront composition.

The change is a customer-presentation correction under the accepted MS-PROT-036 white-label boundary, not a new semantic decision.

## 3. Test-first evidence

```text
993cb093eea5  test(storefront): hide semantic internals from customers
008c7f1d7678  fix(storefront): keep semantic diagnostics internal
67e4bb76440a  test(storefront): reject semantic jargon in customer copy
10db2eb37b70  fix(storefront): use merchant-facing fallback copy
```

The second RED commit failed the Storefront workflow as expected before the production correction:

```text
GitHub Actions run 32993869183
Storefront Web Tests
result: FAILURE
```

The final implementation head passed both required gates:

```text
commit: 10db2eb37b70e2a67e966e14dca07a10361ec1e7

GitHub Actions run 32993837164
Storefront Web Tests
Vitest + Next.js production build
result: SUCCESS

GitHub Actions run 32993837196
Maven Tests
mvn --batch-mode clean verify -Ppostgres-it
result: SUCCESS
```

## 4. Remaining scope

Merchant Profile data, bounded brand/presentation profiles, final production copy, customer authentication/context delivery and complete storefront routes remain separate authority-driven implementation nodes. This correction does not pre-implement or infer them.
