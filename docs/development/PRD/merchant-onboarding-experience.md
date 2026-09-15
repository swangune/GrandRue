# **15. Merchant Onboarding Experience**

**Revision:** 13 September 2026  
**Status:** Current product intent  
**Design authority:** MS-PROT-038, MS-PROT-039, MS-PROT-040, composite MS-PROT-036, MS-PROT-052, composite MS-PROT-057, MS-PROT-094 and applicable identity/trust authorities

## **15.1 Purpose**

Onboarding transforms merchant business intent into a supported Main Street operating configuration and an initial set of useful digital surfaces.

It should feel less like configuring software and more like explaining how the business works.

The primary outcome is **not merely a generated website**. The primary outcome is a valid, merchant-approved initial operational model from which the website, dashboard and applicable customer interactions can be composed.

The experience should be:

- guided;
- mobile-first;
- fast;
- adaptive;
- progressive;
- explainable; and
- safe to correct.

---

## **15.2 Onboarding Objectives**

The onboarding experience has seven primary objectives:

1. establish the merchant account and controller context;
2. capture the merchant's business identity and bounded profile facts;
3. learn enough about how customers and staff interact with the business;
4. propose supported registered capabilities and configuration;
5. resolve material ambiguity through bounded questions and merchant confirmation;
6. validate and approve the initial operational configuration; and
7. compose the initial merchant/customer delivery surfaces, including a merchant-specific customer-facing website where applicable.

Every step should move the merchant closer to a **usable business operating state**, not simply a more complete page.

---

## **15.3 Governing Onboarding Principle**

> **Onboarding gathers business intent; inference proposes supported semantics; registered relationships determine consequences; the merchant corrects or approves; deterministic compilation/validation determines validity.**

```text
merchant evidence
      ↓
structured questions + optional natural language
      ↓
inference / candidate semantics
      ↓
registered relationships
      ↓
adaptive clarification
      ↓
merchant review
      ↓
deterministic validation
      ↓
approved configuration
      ↓
delivery-surface composition
```

Business category may help onboarding language, discovery, classification or inference context. It must not create niche application logic, determine capability activation by itself, or select a customer-facing website template, theme, named style, domain layout or Presentation Profile.

---

## **15.4 Onboarding Principles**

### Progressive Disclosure

Ask only questions whose answers can materially affect the candidate configuration or a required merchant decision.

### Structured Decisions, Natural-Language Assistance

Bounded structured choices are preferred for material business decisions.

Natural language may reduce questioning and identify likely candidate semantics, but must not silently create material business meaning.

### Merchant Correction

Inference is advisory. The merchant must be able to correct a proposal and express supported optional behaviour without being constrained by AI confidence or a category label.

### Immediate Useful Progress

As soon as enough authoritative information exists, Main Street may show provisional business-facing summaries and surface previews. A preview must be clearly non-authoritative where configuration is not yet approved.

### Mobile First

The complete ordinary onboarding flow must be achievable from a smartphone.

### Save and Resume

Progress should be durable and resumable according to the accepted onboarding-case lifecycle. A partially completed case must not be confused with an active merchant configuration.

### No Artificial Completeness Gate

Website-content completeness must not block otherwise valid merchant operations. Only genuine deterministic requirements of the selected operation/configuration may block activation or use.

### No Website-Builder Administration

Onboarding must not require the merchant to administer:

- website templates or themes;
- named website styles;
- component trees;
- layout graphs;
- responsive breakpoints; or
- storefront engine versions.

Main Street absorbs those presentation-system concerns.

---

## **15.5 AI-Assisted Business Understanding**

AI may help interpret a statement such as:

> "I run a barber shop with four barbers. Customers normally book but we accept walk-ins, and we sell a few hair products."

It may propose candidate concepts such as:

```text
Service
Scheduling / Booking
Staff / Workforce context
walk-in interaction
Product / Ordering context
```

It may not invent the semantic relationships among them or activate them merely because they seem likely.

Where a material choice is ambiguous, Main Street asks a business-facing question.

Example:

> **Can customers buy those products online, only in person, or both?**

The merchant answers the business question; Main Street resolves the supported semantic consequence.

Business category is evidence/context only. It must not become the key for a hidden website-template/style classifier.

---

## **15.6 Recommended Onboarding Flow**

The UI may change over time, but the product flow should preserve the following logical progression.

### Stage 1 — Welcome and Account Establishment

Explain that Main Street will learn how the business operates and set up the relevant digital infrastructure.

Avoid website-only language such as "Let's build your website" as the primary promise.

Preferred framing:

> **Tell us how your business works. Main Street will set up the supported digital experience around it.**

### Stage 2 — Business Identity

Collect the minimum authoritative profile/identity information required at this point, such as business name and relevant contact/profile facts.

Business category may be collected for discovery, classification or inference purposes, but it is not executable configuration or website-design authority.

Do not require merchants to select universal `Product / Service / Hybrid` architecture classes. Supported behaviour is discovered compositionally.

### Stage 3 — What the Business Provides

Use high-information questions and optional natural-language description to discover likely offerings/content and seed semantics.

Examples:

- Do you provide services?
- Do you sell products?
- Do you publish information or opportunities?
- Do customers contact you about specific offerings?

The question set adapts to prior evidence.

### Stage 4 — How Customers Interact

Discover relevant interactions such as:

- browse/read;
- enquire/contact;
- book;
- order;
- subscribe;
- visit/walk in; or
- several of these.

Only applicable follow-up questions should appear.

### Stage 5 — Operational Constraints and Choices

Where relevant, resolve business choices concerning schedules, availability, capacity, collection/delivery, payment participation, staff/resource involvement and other supported semantics.

The merchant should see business language, not internal capability or policy identifiers.

### Stage 6 — Merchant Profile, Presence and Brand Inputs

Collect additional applicable profile/public information, merchant-authored/approved content, branding and media.

Supported brand/presentation inputs may include logo, brand characteristics, imagery/media and supported typography preferences where applicable.

These inputs feed merchant-specific customer-facing website composition. They do not select or customise a pre-authored website template, theme, named style, business-domain layout or Presentation Profile.

The merchant should express public identity and desired emphasis in ordinary business-facing terms rather than administer presentation-system internals.

### Stage 7 — External Connections

Offer external providers only when relevant to the merchant's selected semantics or product choices.

Google Business Profile is **conditional**, not a universal onboarding gate. It is relevant when the merchant chooses or requires supported Google/local-discovery participation.

Payment, calendar/scheduling, domain, social or other providers likewise remain implementation/integration choices behind the relevant Main Street semantics.

### Stage 8 — Business-Facing Configuration Review

Present the current understanding in business language.

Example:

```text
Customers can:
✓ browse your services
✓ send enquiries
✓ book appointments

Your business can:
✓ manage service availability
✓ assign relevant staff

Not enabled:
– online product ordering
– delivery
```

Do not expose raw semantic graph internals as the normal review experience.

### Stage 9 — Validation and Approval

Main Street validates the candidate configuration through deterministic rules.

If merchant intent cannot be represented by supported semantics, surface a configuration/product gap. Do not ask AI to invent a workaround that changes business meaning.

Material configuration approval follows the accepted configuration review/activation authority.

### Stage 10 — Surface Preview

Once enough approved/provisional information exists, show the merchant how applicable surfaces will appear.

A customer-facing website preview may present a merchant-specific candidate composition over applicable public information, products/services, publications and valid interaction entry points.

The preview is a presentation of the candidate/approved operational model, not a separate website business-data or semantic authority.

The merchant is not choosing from a catalogue of website templates/themes/styles. Where composition assistance is AI-backed, AI proposes merchant-specific presentation subject to MS-PROT-094 and the accepted AI authority.

### Stage 11 — Activate and Publish

Activation establishes the approved merchant configuration according to the accepted lifecycle.

Publication then exposes the authorised public/customer surfaces that follow from that configuration and the validated published presentation composition.

The final action should not imply that a merchant becomes operational only by publishing a website. Some capabilities may remain useful to the merchant even when a public website surface is incomplete or intentionally minimal.

---

## **15.7 Post-Onboarding Guidance**

Post-onboarding recommendations should be contextual, non-blocking and driven by applicable merchant semantics.

Examples may include:

- complete relevant public information;
- add useful offering media;
- connect an applicable external provider;
- invite staff where Workforce capability is relevant;
- enable a supported customer interaction the merchant has expressed interest in; or
- improve the public storefront composition/content.

Do not use a global "website completeness" score as a proxy for business readiness.

Do not make the merchant responsible for routine storefront-engine upgrades or presentation migrations that Main Street can safely manage centrally.

---

## **15.8 Success Criteria**

Onboarding is successful when:

- the merchant has a valid approved initial operating configuration;
- authoritative profile and required capability facts exist at the appropriate owners;
- the merchant understands the supported customer interactions that are enabled;
- applicable delivery surfaces are usable or can be completed progressively;
- where a customer-facing website is applicable, Main Street can produce a professional merchant-specific composition without requiring template/theme/layout administration;
- unsupported intent has not been disguised as supported behaviour;
- the merchant can continue operating without understanding Main Street's technical architecture; and
- the process remains practical on a smartphone.

A published website is an important possible outcome, but it is no longer the defining success criterion by itself.

---

## **15.9 Onboarding Statement**

> **Main Street onboarding discovers and validates how the merchant intends to operate, then composes the right digital surfaces from that approved operational model. AI reduces questioning and ambiguity and may assist merchant-specific website composition; registered semantics and deterministic validation preserve correctness.**

---

### End of Section 15 – Merchant Onboarding Experience
