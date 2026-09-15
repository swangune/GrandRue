# **7. Guiding Principles**

**Revision:** 13 September 2026  
**Status:** Current product intent

## **7.1 Purpose**

These principles govern product, design and engineering decisions across Main Street. They are intentionally broader than individual features and should remain valid as interfaces, AI capabilities and delivery technologies evolve.

If a proposal conflicts with these principles, it should be redesigned or explicitly escalated for product review.

---

## **7.2 Principle 1 – Business Before Technology**

Technology exists to support merchants, not become another merchant responsibility.

Main Street should speak in business concepts and outcomes. Merchants should not need to understand hosting, DNS, framework choices, databases, API topology, semantic registries or AI model mechanics.

Technical complexity should be hidden; material business consequences should not be hidden.

---

## **7.3 Principle 2 – The Operational Model Is the Product Core**

Main Street's durable product core is the coherent model of the merchant's supported operations and the governed runtime that executes them.

The product must therefore prioritise:

- authoritative state;
- explicit semantic ownership;
- validated configuration;
- reliable operations;
- permissions and merchant control;
- transaction and audit evidence;
- integration continuity; and
- consistent behaviour across channels.

A generated interface is useful only to the extent that it accurately exposes or invokes this core.

---

## **7.4 Principle 3 – One Coherent Model, Many Surfaces**

Main Street may provide multiple surfaces, including:

- merchant dashboard;
- public website/storefront;
- customer interaction flows;
- staff/POS experiences;
- integrations/APIs; and
- AI-assisted interaction.

These are separate presentation, trust or interaction boundaries over shared capability-owned business authority.

They must not become separate business engines.

---

## **7.5 Principle 4 – Authoritative Ownership Before “Single Source of Truth”**

Main Street should avoid the misleading idea that one universal Business Profile owns every business fact.

The Business Profile owns the profile/public-information facts assigned to it. Other capabilities own their bounded operational facts.

One coherent merchant operational model emerges from those authorities.

Derived websites, dashboards, caches, search indexes and AI context remain non-authoritative unless an accepted authority explicitly provides otherwise.

---

## **7.6 Principle 5 – Technology Should Be Invisible**

Merchants should experience outcomes rather than implementation machinery.

Main Street should automate security, performance, accessibility, rendering, infrastructure and other technical concerns where practical.

Invisible technology must remain observable, testable and recoverable by the platform.

---

## **7.7 Principle 6 – Automation Before Configuration**

Technical choices should be automated wherever Main Street can make them safely.

Merchant choices should be requested when they materially determine how the business operates or what the merchant intends.

Automation must not convert an inferred preference into business authority merely because an algorithm is confident.

---

## **7.8 Principle 7 – Operational Convergence, Storefront Composition Freedom**

Main Street merchant software should remain a coherent, recognisable Main Street product while adapting to the strengths and conventions of its execution environment.

Merchant business category or identity does not require a unique dashboard design. Applicable capabilities, actor role/authority, current work, operational state, accessibility requirements and environment should drive operational variation.

Customer-facing merchant websites follow the opposite presentation strategy. They should be merchant-specific compositions over shared Main Street storefront infrastructure rather than instances of templates, themes, named reusable styles, business-domain layouts or presentation profiles.

Reusable engineering mechanics are encouraged where they reduce maintenance and defects without prescribing complete website designs or acquiring business authority.

---

## **7.9 Principle 8 – Professional by Default**

Every applicable customer-facing surface should provide professional design, accessibility, security, mobile behaviour, performance and search-friendly structure without requiring merchant design expertise.

Professional presentation is a platform property, not the product's source of business truth.

Main Street should be able to improve many existing customer-facing websites by improving shared rendering and composition infrastructure rather than manually maintaining each merchant website.

---

## **7.10 Principle 9 – Local Businesses First**

Main Street exists to strengthen direct relationships between local businesses and their customers.

The platform is infrastructure rather than a gatekeeping marketplace. Supported online, service-area and information-oriented businesses need not be forced into a physical-storefront model merely because Main Street is local-first.

---

## **7.11 Principle 10 – Trust Through Operational Accuracy**

Trust requires more than attractive presentation.

Main Street should prioritise accurate information, current availability, valid commitments, explicit permissions, secure transactions, traceable actions and predictable failure handling.

A website must never present a stale projection as authority for a concurrency-sensitive operation.

Customer-facing websites do not own authoritative business data; they present authorised Main Street projections and send customer intent back through the owning application operations.

---

## **7.12 Principle 11 – Merchant Ownership**

Merchants remain owners and operators of their businesses.

Main Street provides infrastructure and governed execution. Merchants retain authority over material matters such as offerings, pricing, business policies, operating choices, staff delegation and customer relationships, subject to applicable law and platform safety/security requirements.

---

## **7.13 Principle 12 – AI Assists; Deterministic Main Street Operates**

AI may:

- interpret natural language;
- clarify ambiguity;
- draft and transform content;
- propose registered semantics or changes;
- explain authoritative business state;
- detect useful gaps;
- propose merchant-specific website composition; and
- help invoke supported application operations.

AI must not:

- invent capabilities or semantic relationships;
- become merchant-intent authority;
- become authoritative business state;
- bypass authorisation or deterministic validation;
- independently alter material business behaviour without the required merchant decision;
- select a hidden finite template/theme/style family as a substitute for merchant-specific composition; or
- generate uncontrolled per-merchant application logic as the operating architecture.

AI and manual paths should converge on the same application authority.

---

## **7.14 Principle 13 – AI Should Resolve Ambiguity, Not Manufacture Certainty**

Natural language can reduce onboarding and operational friction, but ambiguous material intent must be clarified or confirmed before semantic promotion.

Unsupported merchant intent should be represented as a capability/configuration gap rather than hidden behind plausible generated output.

---

## **7.15 Principle 14 – Surfaces Are Replaceable; Business Authority Is Durable**

Website frameworks, UI patterns, AI models and device experiences will change.

The operational model must outlive them.

Main Street should be able to replace, improve, recompose or add a delivery surface without migrating the merchant into a new business model or moving authoritative business data into that surface.

---

## **7.16 Principle 15 – Progressive Growth**

Businesses should be able to activate additional supported capabilities as they evolve without being reclassified into separate Main Street products or receiving niche-specific code forks.

Complexity should increase only when the merchant's business requires it.

---

## **7.17 Principle 16 – Security, Privacy and Evidence by Design**

Security, privacy, resilience, auditability and data lifecycle requirements are foundational. AI convenience, frontend generation or integration shortcuts must not bypass them.

---

## **7.18 Principle 17 – Simplicity Is a Competitive Advantage**

Main Street should resist feature accumulation that merely copies isolated SaaS categories.

The stronger solution is usually the one that lets multiple surfaces and interactions reuse the same well-owned capability semantics.

Presentation reuse should remain bounded: sharing mechanics is useful; forcing merchant websites into shared visual outcomes is not.

---

## **7.19 AI-Substitution Decision Test**

Before making a feature strategically central, ask:

> **If a general-purpose AI system could generate this visible feature tomorrow, what value would still require Main Street?**

Strong answers include authoritative context, merchant history, reliable execution, current state, permissions, integrations, reconciliation, audit evidence and operational continuity.

This test helps prioritise defensible platform value. It does not prohibit AI-generated presentation or content.

---

## **7.20 Decision Framework**

A feature should satisfy the following questions:

1. Does it make the merchant's life simpler?
2. Does it strengthen or correctly expose the merchant operational model?
3. Does it preserve capability ownership rather than create parallel logic?
4. Can technical configuration be automated safely?
5. Does it preserve merchant control over material business choices?
6. Does it improve operational accuracy or customer trust?
7. Does it remain useful across materially different supported merchants?
8. Can its AI path remain subordinate to deterministic authority?
9. Would Main Street still contribute durable value if the visible interface were commoditised?
10. Can the capability evolve without making a delivery surface the source of truth?
11. Can customer-facing presentation remain centrally maintainable without collapsing into templates or merchant-specific codebases?

---

## **7.21 Guiding Principle Statement**

> **Main Street makes digital business operation effortless by keeping authoritative business semantics and state in a governed operational core, exposing that core through replaceable professional surfaces, standardising the operational software merchants use, composing customer-facing websites around each merchant, and using AI to reduce ambiguity and work without surrendering merchant control or deterministic execution.**

---

### End of Section 7 – Guiding Principles
