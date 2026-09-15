# MS-PROT-048 v1.1 — Open-First Fulfilment Selection Amendment

**Document ID:** MS-PROT-048  
**Version:** 1.1  
**Status:** **ACCEPTED after manual refinement**  
**Amends:** MS-PROT-048 v1.0 — Capability Fulfilment Provider Contract  
**Purpose:** Establish the mandatory selection order for fulfilment implementations so Main Street prefers modifiable open libraries and open software before introducing paid external services.

---

## 1. Governing decision

> **For every fulfilment responsibility, Main Street shall first prefer a modifiable open library that can be incorporated into and adapted by Main Street. If that is not suitable, sustainable or safe, Main Street shall next consider open software that can be operated or self-hosted. Paid external services shall be considered only when the open-library and open-software options are not suitable, sustainable or safe for the required responsibility.**

Canonical selection order:

```text
Required fulfilment responsibility
        ↓
1. MODIFIABLE OPEN LIBRARY
        │
        ├── suitable + sustainable + safe
        │       → use/adapt internally
        │
        └── not suitable / sustainable / safe
                ↓
2. OPEN SOFTWARE
        │
        ├── suitable + sustainable + safe
        │       → operate/self-host/integrate
        │
        └── not suitable / sustainable / safe
                ↓
3. PAID EXTERNAL SERVICE
        ↓
Only after the first two classes fail the required evaluation
```

This order is normative.

---

## 2. Why libraries come before open software

A modifiable open library gives Main Street the strongest architectural control.

It can generally be:

- incorporated directly into Main Street-owned implementation;
- adapted to Main Street's semantic and operational contracts;
- tested with the platform's own test suite;
- replaced incrementally;
- maintained without depending on an external runtime service;
- kept behind Main Street-owned interfaces.

Therefore the first question for a new fulfilment responsibility is not:

> Which SaaS provider should we integrate?

It is:

> Can Main Street fulfil this responsibility safely and sustainably with a modifiable open library?

---

## 3. Open software is the second preference

Where a library alone is insufficient, Main Street should evaluate mature open software that can be operated, self-hosted or otherwise controlled by Main Street.

Examples may include self-hosted infrastructure for:

- search;
- analytics;
- object storage;
- queues;
- document generation;
- observability;
- selected communication infrastructure;
- other non-regulated technical responsibilities.

Open software is preferred over a paid external service only where operating it is itself suitable, sustainable and safe.

---

## 4. Paid services are the third option, not the default

A paid external service may be selected only where the required responsibility cannot reasonably be discharged through either:

1. a suitable, sustainable and safe modifiable open library; or
2. suitable, sustainable and safe open software.

A paid service is not justified merely because it:

- is popular;
- has a convenient SDK;
- reduces initial coding effort;
- is commonly used by competitors;
- has an attractive free tier;
- is easier to demonstrate in a prototype.

The burden of justification lies with the paid dependency.

---

## 5. Evaluation gates

Each candidate in the selection order shall be evaluated against three mandatory gates.

### 5.1 Suitability

The candidate must satisfy the actual Main Street fulfilment obligations and architectural boundary.

Questions include:

```text
Does it satisfy the required role?
Does it preserve Main Street semantic authority?
Can it meet the required performance and reliability characteristics?
Can it be integrated behind a stable Main Street-owned contract?
Does it work for the required merchant scale and operating contexts?
```

A technically interesting project that cannot satisfy the required obligation is not suitable.

### 5.2 Sustainability

The candidate must be realistically supportable over the expected lifetime of Main Street.

Questions include:

```text
Is the project actively maintained?
Is the licence compatible with Main Street's intended use and modification model?
Can Main Street understand and maintain the relevant code if necessary?
Is the operational burden proportionate?
Does the project have a credible upgrade/security-patch path?
Would adoption create an effectively unmaintainable fork?
Does running it require specialist operations disproportionate to the benefit?
```

Open source is not automatically sustainable merely because its licence permits modification.

### 5.3 Safety

The candidate must not create unacceptable security, privacy, compliance, availability or data-integrity risk.

Questions include:

```text
Is the software sufficiently maintained against known vulnerabilities?
Can secrets and credentials be handled safely?
Can merchant isolation be preserved?
Can authoritative business data remain protected?
Can failure/recovery behaviour satisfy Main Street's contracts?
Does operating it ourselves create unacceptable security or compliance exposure?
```

If Main Street cannot operate an open option safely, choosing it merely to avoid a paid service is prohibited.

---

## 6. The hierarchy is about control, not ideology

The policy does not say:

```text
open = always correct
paid = always wrong
```

It establishes a preference for control and architectural independence while recognising operational reality.

The decision process is:

```text
control where practical
        +
open implementation where sustainable
        +
externalisation only where justified
```

A paid service may therefore be the correct choice where external network ownership, regulated infrastructure, specialised operational capability or disproportionate self-hosting risk makes internal/open fulfilment unsuitable.

---

## 7. Examples

### 7.1 Scheduling calculation

```text
Need:
availability / slot calculation

First:
modifiable open libraries / Main Street-owned implementation

Expected outcome:
INTERNAL fulfilment
```

A SaaS scheduling provider is not the default merely because one exists.

### 7.2 PDF/document generation

```text
Need:
document rendering

First:
modifiable open library

If suitable/sustainable/safe:
use internally
```

No paid document API is justified without evidence that the open-library route fails.

### 7.3 Search

```text
Need:
search/indexing

First:
modifiable open library where sufficient

Then:
open search software if a standalone engine is justified

Only then:
paid hosted search service
```

### 7.4 Email delivery

Main Street should separate message semantics from delivery infrastructure.

```text
notification semantics / templates / recipient selection / audit
    → Main Street

message construction and protocol/client behaviour
    → open library first

mail transport infrastructure
    → open/self-hosted software second where sustainable and safe

managed delivery service
    → only if deliverability, security or operational burden makes the open route unsuitable
```

### 7.5 SMS

Carrier-network access may make an external service unavoidable even if protocol/client libraries are open.

The paid service is justified by the external network responsibility, not by convenience.

### 7.6 Payments

Regulated payment execution remains external where Main Street does not intend to become the payment processor or financial institution.

Open libraries may implement client/protocol integration, but they do not replace the regulated external payment network/provider.

### 7.7 Google Business Profile

Where a merchant chooses Google's ecosystem, Google remains the external authority for Google-owned verification/profile facts. Open software cannot substitute for Google's authority.

---

## 8. Libraries and software must remain behind Main Street-owned boundaries

Using open code does not permit external implementation details to leak into semantic architecture.

Required pattern:

```text
Main Street fulfilment role
        ↓
Main Street-owned interface / adapter boundary
        ↓
open library OR open software OR paid provider adapter
```

Therefore a library's classes, data structures or state machine shall not become Main Street semantic definitions merely because Main Street uses that library internally.

---

## 9. Forking and modification

The ability to modify a library is valuable, but modification shall be disciplined.

A fork is justified only where:

- the change is necessary to satisfy Main Street's accepted contract;
- upstream extension/configuration is insufficient;
- the maintenance cost is understood;
- security updates can continue to be incorporated;
- Main Street does not create an unnecessarily divergent private platform dependency.

Where practical, Main Street should contribute generally useful fixes upstream rather than maintain permanent divergence.

---

## 10. Provider-selection evidence

Any proposal to introduce a paid external service for a fulfilment role shall record at least:

```text
fulfilment responsibility
required obligations
open libraries evaluated
why each is unsuitable / unsustainable / unsafe
open software evaluated
why each is unsuitable / unsustainable / unsafe
paid service selected
why the paid service satisfies the unmet requirement
exit/replacement strategy
```

The evaluation need not become bureaucratic for trivial tooling, but material runtime dependencies require traceable evidence.

---

## 11. Cost is part of sustainability, not the only criterion

A nominally free open implementation may have substantial costs in:

- infrastructure;
- maintenance;
- security patching;
- monitoring;
- backups;
- specialist operations;
- incident response;
- deliverability/reputation management;
- compliance.

Likewise, a cheap paid service may create expensive lock-in later.

Therefore sustainability considers total lifecycle burden rather than licence price alone.

---

## 12. No SaaS-shaped architecture

Main Street must not evolve into:

```text
Scheduling → SaaS
Search → SaaS
Email → SaaS
Analytics → SaaS
Documents → SaaS
Storage → SaaS
Automation → SaaS
Maps → SaaS
```

merely because those services are available.

The intended architecture is:

```text
                 MAIN STREET
                      │
           Main Street-owned contracts
                      │
          ┌───────────┼────────────┐
          ▼           ▼            ▼
   open library   open software   paid service
      preferred      second        exception
```

This protects cost structure, portability and architectural independence.

---

## 13. Relationship to provider abstraction

MS-PROT-048 v1.0 remains authoritative for fulfilment roles, bindings, provider compatibility, authority boundaries, callbacks, uncertainty, provenance and degraded fulfilment.

MS-PROT-048 v1.1 changes the **selection priority**, not those provider semantics.

A fulfilment role may still ultimately bind to an external paid provider where justified. The new requirement is that Main Street evaluates the open options first.

---

## 14. Accepted invariants

1. Modifiable open libraries are the first implementation preference for fulfilment responsibilities.
2. Open/self-hostable software is considered second when a library is insufficient.
3. Paid external services are considered only after both preceding classes fail suitability, sustainability or safety evaluation.
4. Convenience alone does not justify a paid runtime dependency.
5. Open-source status alone does not establish suitability, sustainability or safety.
6. Total lifecycle burden is part of sustainability.
7. Regulated/external-authority/network-owned responsibilities may legitimately require external providers.
8. Main Street-owned interfaces isolate all three implementation classes from semantic architecture.
9. Modification/forking must remain maintainable and security-updatable.
10. Material paid-service adoption requires traceable justification and a replacement/exit strategy.
11. Provider choice never changes Main Street semantic meaning.
12. Main Street shall avoid SaaS-shaped architecture and unnecessary provider lock-in.

---

## Governance verdict

**ACCEPTED.** Manual refinement established the hierarchy:

```text
modifiable open library
        ↓ if unsuitable / unsustainable / unsafe
open software
        ↓ if unsuitable / unsustainable / unsafe
paid external service
```

### Canonical decision

> **Main Street shall prefer fulfilment implementations it can understand, modify and control. It shall first evaluate modifiable open libraries, then open software, and shall use paid external services only where the preceding options cannot satisfy the responsibility suitably, sustainably and safely.**
