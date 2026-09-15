# **22. AI Platform & Automation Engine**

**Revision:** 4 September 2026  
**Status:** Compatibility navigation — detailed current product requirements are owned by `docs/development/PRD/ai-platform.md`.

## **22.1 Purpose**

This file is retained because earlier PRD navigation and traceability refer to Section 22 and to this filename.

To prevent two overlapping AI product specifications from drifting, the current detailed AI requirements are consolidated in:

```text
docs/development/PRD/ai-platform.md
```

The following product boundary remains binding here and in that document:

> **AI simplifies Main Street; deterministic Main Street operates the business.**

## **22.2 Current Product Boundary**

AI may interpret merchant language, clarify ambiguity, draft or transform content, explain business state, recommend supported actions and prepare bounded proposals.

AI is not:

- semantic authority;
- merchant-intent authority;
- authoritative business state;
- deterministic validation authority;
- a mandatory dependency for ordinary operations; or
- a mechanism for generating independent executable business logic per merchant.

Direct and AI-assisted interactions converge on the same Main Street application contracts and capability-owned execution.

## **22.3 Business Context**

The former statement that the Business Profile is AI's universal source of context is superseded.

AI context may draw, when authorised and relevant, from the coherent merchant operational model: merchant/profile facts, active configuration, capability-owned projections, operational state and registered semantic options.

Context remains purpose-bound and does not become a new source of truth.

## **22.4 Website Boundary**

AI-assisted website generation and content improvement remain supported product capabilities, but the website is a delivery surface rather than the product core.

Website assistance must not invent or activate new business behaviour merely to improve presentation.

## **22.5 Canonical Reference**

For current AI product requirements covering onboarding, reconfiguration, website assistance, customer/operational assistance, context, model architecture, failure independence and AI-era strategy, use:

`docs/development/PRD/ai-platform.md`

For accepted semantic/design authority, use the current MS-PROT-057 composition identified by `designs/AUTHORITY-INDEX.md`.

---

### End of Section 22 – AI Platform & Automation Engine compatibility navigation
