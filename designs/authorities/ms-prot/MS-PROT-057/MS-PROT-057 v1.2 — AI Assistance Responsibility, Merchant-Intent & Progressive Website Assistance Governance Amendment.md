# MS-PROT-057 v1.2 — AI Assistance Responsibility, Merchant-Intent & Progressive Website Assistance Governance Amendment

**Document ID:** MS-PROT-057  
**Version:** 1.2  
**Status:** **ACCEPTED after proposal, review, falsification, trade-off resolution and manual approval**  
**Approved:** 27 August 2026  
**Amends:** MS-PROT-057 v1.0 and v1.1  
**Related authorities:** MS-PROT-036, MS-PROT-040, MS-PROT-049, MS-PROT-053, MS-PROT-057, MS-PROT-059, MS-PROT-062, MS-PROT-063, MS-PROT-066, MS-PROT-079  
**Purpose:** Define Main Street's merchant-side AI assistance architecture, responsibility portfolio, merchant-intent confirmation boundary, progressive website-population behaviour, prioritisation, orchestration and frugal model-selection policy.

> **Current-authority navigation notice:** This accepted amendment remains part of the current composite MS-PROT-057 authority. Within customer-facing website presentation-assistance scope, read it together with **MS-PROT-094 v1.0**. MS-PROT-094 supersedes references below to `registered presentation options` or presentation-profile selection as the governing storefront mechanism. Website AI may propose merchant-specific composition, but it MUST NOT select templates, themes, named reusable styles, domain layouts, Presentation Profiles or hidden finite site archetypes. Historical wording below is retained as design provenance and MUST NOT be implemented where MS-PROT-094 governs.

---

## 1. Governing principle

Main Street SHALL use AI to simplify interaction with deterministic Main Street capabilities.

AI SHALL NOT become:

- the primary operating path;
- semantic authority;
- merchant-intent authority;
- business-state authority;
- deterministic validation authority;
- a mandatory dependency for ordinary operations; or
- an autonomous operator of the merchant's business.

```text
                         MERCHANT
                            │
              ┌─────────────┴─────────────┐
              │                           │
              ▼                           ▼
        DIRECT / MANUAL              AI-ASSISTED
          INTERACTION                 INTERACTION
              │                           │
              │                  interpret / clarify
              │                  explain / draft
              │                  suggest / propose
              │                           │
              └─────────────┬─────────────┘
                            ▼
                  SAME MAIN STREET
                 APPLICATION CONTRACT
                            │
                            ▼
                    AUTHORISATION
                            │
                            ▼
               DETERMINISTIC VALIDATION
                            │
                            ▼
                CAPABILITY-OWNED EXECUTION
                            │
                            ▼
                  AUTHORITATIVE STATE
```

> **AI simplifies Main Street; deterministic Main Street operates the business.**

This remains consistent with the accepted production AI boundary under which model output remains untrusted until validated against bounded contracts and registered semantics.

---

# 2. Contract-first architecture

Main Street SHALL NOT architect AI as a fixed fleet such as:

```text
exactly 10 agents
exactly 16 agents
one capability = one agent
one responsibility = one model
```

The governing abstraction is:

```text
Merchant Job
    ↓
AI Assistance Responsibility
    ↓
Versioned AI Inference Contract
    ↓
Purpose-Built Context
    ↓
Qualified Runtime Mechanism
```

Therefore:

```text
Responsibility
    ≠ model
    ≠ process
    ≠ microservice
    ≠ capability
    ≠ semantic owner
```

The accepted `AIInferenceContract` remains the production inference boundary.

---

# 3. Main Street Assistant

Main Street MAY expose one coherent merchant-facing conversational assistant.

The **Main Street Assistant** is an interaction and orchestration shell.

It MAY provide:

```text
conversational continuity
context-aware routing
ambiguity detection
bounded decomposition
result consolidation
business-language explanation
```

It SHALL NOT own any business semantics.

The accepted prohibition against turning the unified assistant into a semantic monolith remains unchanged.

---

# 4. Deterministic routing first

A model SHALL NOT be invoked merely to route a request when application context already identifies the applicable responsibility.

Example:

```text
workspace = Website
selected content = Service Description

Merchant:
"Make this clearer."
```

may route directly to a Website Population inference contract.

Rejected:

```text
Concierge model
    ↓
Website model
```

where the first call contributes no useful reasoning.

> **One assistant experience does not require one Concierge inference call per interaction.**

---

# 5. Ordinary operations remain usable without AI

Every ordinary supported merchant business operation SHALL have a deterministic non-AI path.

Examples include:

```text
add Service
add Product
change price
change Business Hours
manage staff
manage configuration
manage website information
manage Booking
manage Order
change Profile information
```

AI is an alternative assistance path to the same underlying application authority.

AI failure SHALL NOT deactivate the capability being assisted.

---

# 6. Optional AI-native enhancements

An optional intrinsically generative enhancement need not have an equivalent non-AI transformation mechanism.

Example:

```text
Upload original image              ✓
Use original image                 ✓
Publish authorised original        ✓

AI Image Amplification             optional
```

Therefore:

> **The underlying business operation must remain AI-independent; an optional AI-native enhancement need not.**

---

# 7. Interpretation and merchant intent

AI MAY create non-authoritative hypotheses needed to understand natural language.

It SHALL NOT promote materially inferred business meaning into merchant intent merely because the interpretation appears probable.

```text
Merchant Input
      ↓
Non-Authoritative Interpretation
      ↓
Relevant meaning explicit?
   ┌───────┴───────┐
  YES              NO
   │                │
   │       Material inference?
   │                │
   │                ▼
   │       Merchant confirmation
   │                │
   └────────┬───────┘
            ▼
      Confirmed Intent
            ↓
 Owner-Qualified Candidate
```

> **AI may hypothesise. The merchant establishes merchant intent.**

---

# 8. Material semantic promotion

Merchant confirmation is required before an inferred hypothesis is promoted into an owner-qualified candidate where the inference introduces or materially changes concepts such as:

```text
Product
Service / Offering
Location
Service Area
Capability
business operating rule
configuration policy
Booking behaviour
Payment behaviour
Staff authority
Publication meaning
Exposure intention
business relationship
```

Example:

> “Put on the website that we now do boiler servicing.”

The assistant MAY identify:

```text
possible new Service
```

but SHALL determine whether the merchant means:

```text
website information only

or

a new Service actually offered
```

before constructing a Service candidate.

---

# 9. Explicit instructions do not require redundant confirmation

The rule above SHALL NOT make Main Street unnecessarily conversational.

Merchant:

> “Add Boiler Servicing as a service for £95.”

has explicitly established the key meaning.

No separate question asking whether it should be added as a Service is required.

Therefore:

```text
explicit meaning
    → normal interpretation

material meaning inferred by AI
    → confirmation before semantic promotion
```

---

# 10. Interpretation confirmation and change approval

These remain conceptually distinct:

```text
INTERPRETATION CONFIRMATION
"Yes, that is what I meant."

        ≠

CHANGE APPROVAL
"Yes, apply the resulting change."
```

They MAY collapse into one merchant action when the complete consequence is already explicit and the governing capability permits it.

Material configuration remains subject to deterministic validation, impact review and applicable merchant approval under MS-PROT-040.

---

# 11. Merchant-side responsibility portfolio

The currently justified merchant-assistance responsibilities are:

1. **Onboarding & Configuration Discovery**
2. **Reconfiguration**
3. **Website Population & Management**
4. **Scheduling & Reservation**
5. **Commerce**
6. **Merchant Presence**
7. **Workforce & Access**
8. **Merchant Administration**
9. **Customer Engagement**
10. **Business Intelligence**

This catalogue does not require ten separately deployed agents.

---

# 12. Product importance

The initial value order is:

```text
P1  Onboarding & Configuration Discovery
P2  Reconfiguration
P3  Website Population & Management
P4  Scheduling & Reservation
P5  Commerce
P6  Merchant Presence
P7  Workforce & Access
P8  Merchant Administration
P9  Customer Engagement
P10 Business Intelligence
```

The ordering expresses expected merchant value.

It SHALL NOT determine:

```text
backend dependency order
model size
model cost
semantic authority
```

MS-PROT-079 remains authoritative for backend design sequencing.

---

# 13. Onboarding & Configuration Discovery

Purpose:

> Help Main Street understand how a new merchant actually operates.

It MAY:

```text
interpret business descriptions
interpret OTHER responses
map evidence to registered candidate semantics
identify unresolved decisions
clarify ambiguity
explain current understanding
prepare bounded initial configuration intent
```

It SHALL NOT:

```text
invent capabilities
infer behaviour purely from category
activate configuration
invent semantic relationships
treat model confidence as merchant approval
```

---

# 14. Reconfiguration

Purpose:

> Help an existing merchant evolve how the business operates.

It SHALL begin from current authoritative configuration.

```text
Current Configuration
        +
Confirmed New Intent
        ↓
Candidate Change Set
```

It SHALL distinguish configuration from operational activity.

```text
"Close next Monday."
    → specific Schedule Intent

"We don't open Mondays anymore."
    → recurring configuration
```

It MAY coordinate several bounded owners but SHALL NOT become a universal configuration authority.

Existing commitments remain protected under MS-PROT-040.

---

# 15. Website Population & Management

Purpose:

> **Enable non-technical merchants to create, populate, maintain and improve their Main Street website using ordinary business language rather than website technology.**

The merchant should not need to understand:

```text
CMS schema
blocks
components
routes
React
Next.js
frontend implementation
```

The responsibility MAY understand relevant website concepts including:

```text
business introduction
About information
services/products displayed
contact/location presentation
Publications
media
homepage emphasis
registered presentation options
customer interaction entry points
```

Storefront generation continues to derive from configuration, projections and registered presentation rather than arbitrary AI-generated applications.

---

# 16. Website Population has reactive and progressive modes

Website assistance SHALL support two distinct behaviours.

### Reactive

The merchant requests an operation.

Example:

> “Add these pictures to our wedding service.”

```text
Merchant Request
    ↓
Website Population
    ↓
bounded assistance
```

### Progressive

Main Street identifies a meaningful unresolved website gap.

```text
Current Storefront Composition
        ↓
Applicable Website Gap Analysis
        ↓
Useful unresolved improvement?
        ↓
Website suggestion
```

Example:

> “Your services are listed, but none currently has an image. Would you like to add some?”

Both modes remain advisory.

---

# 17. Website completeness is contextual

Main Street SHALL NOT define “fully populated” as:

```text
every possible field completed
every possible page present
every available capability represented
```

A merchant website is contextually complete when its applicable storefront composition contains sufficient truthful information for the merchant's intended public model.

Therefore:

```text
Website Completeness
    =
applicable merchant semantics
+
applicable public surfaces
+
merchant-approved content
+
required presentation/content dependencies
```

not:

```text
percentage of global Main Street fields completed
```

Examples:

- a consultant does not need Products;
- an information publisher does not need Booking;
- a plumber need not have a Gallery;
- an enquiry-only organisation need not have Payment.

---

# 18. Website gap classes

Website Population MAY classify unresolved website needs conceptually as:

```text
REQUIRED_FOR_SURFACE

RECOMMENDED

OPTIONAL_ENHANCEMENT
```

### REQUIRED_FOR_SURFACE

Information genuinely required for a particular intended website/public interaction to operate coherently.

### RECOMMENDED

Information likely to materially improve an already-applicable surface.

### OPTIONAL_ENHANCEMENT

Presentation improvement that is not necessary to make the site useful or truthful.

These classifications describe website assistance priority.

They SHALL NOT create business semantics or platform invariants.

---

# 19. Website suggestions SHALL be non-blocking

Website Population MAY proactively suggest improvements.

Suggestions SHALL NOT prevent normal merchant operation.

Rejected:

```text
"Website is 70% complete.
Complete it before continuing."
```

Rejected:

```text
cannot manage Orders
because About section is empty
```

Rejected:

```text
cannot manage staff
because Product images are missing
```

Required:

```text
merchant continues normal business operations

Website suggestion:
"Your About section is still empty.
I can help you complete it when you're ready."
```

> **Website incompleteness is not business-operability incompleteness.**

---

# 20. Genuine operation requirements remain authoritative

The non-blocking Website rule does not override legitimate deterministic requirements of a specific operation.

Example:

If publication of a particular Product requires an authoritative value under its owning contract, that operation may legitimately be rejected.

That is:

```text
capability/application invariant
```

not:

```text
Website AI blocking merchant progress
```

A Website suggestion SHALL NOT manufacture new mandatory requirements.

---

# 21. Suggestions SHALL be spaced

Main Street SHALL NOT repeatedly interrupt the merchant with website recommendations.

Website suggestion policy SHALL support:

```text
prioritisation
spacing
deduplication
cooldown
dismissal memory
explicit rejection memory
```

Rejected pattern:

```text
login
→ website suggestion

open Orders
→ another website suggestion

dismiss
→ same suggestion later that day
```

The exact time intervals remain implementation decisions.

The architectural invariant is:

> **Website assistance SHALL optimise usefulness rather than suggestion frequency.**

---

# 22. Dismissal semantics

A temporary response such as:

> “Not now.”

SHOULD suppress the suggestion for an appropriate interval.

An explicit durable preference such as:

> “I don't want an About section.”

SHOULD stop the same recommendation from being treated as an unresolved gap unless materially relevant circumstances change.

Therefore:

```text
temporary dismissal
    ≠
permanent merchant preference
```

Both SHALL be distinguishable where necessary.

---

# 23. Suggestion priority

Website assistance SHOULD present the highest-value unresolved improvement rather than an indiscriminate checklist.

Conceptually:

```text
cannot contact merchant
        ↑
critical service information missing
        ↑
important item lacks useful media
        ↑
About content incomplete
        ↑
additional gallery polish
```

Suggestion prioritisation MAY consider:

```text
customer usefulness
merchant semantics
surface importance
truth/completeness
merchant effort
previous dismissal
suggestion history
recent business change
```

It SHALL NOT optimise primarily for engagement with the AI.

---

# 24. Suggestions SHALL NOT invent facts

The Website responsibility may say:

> “Your website does not currently show opening hours. Would you like to add them?”

It SHALL NOT infer:

```text
Monday–Friday
09:00–17:00
```

from merchant category or similar businesses.

Likewise:

> “Your services do not show prices. Would you like to add prices?”

does not imply prices are mandatory.

---

# 25. Suggested business expansion requires handoff

Website assistance MAY detect that the merchant might benefit from another Main Street business capability.

Example:

> “Would you like customers to be able to book these services online?”

This is a suggestion, not configuration.

If the merchant expresses interest:

```text
Website suggestion
        ↓
Merchant:
"Yes, I want that."
        ↓
Reconfiguration responsibility
        ↓
required business decisions
        ↓
candidate configuration
```

Website Population SHALL NOT directly activate Booking, Ordering, Payment or other business behaviour merely because it would improve the website.

---

# 26. Website Population SHALL NOT own business facts

Merchant:

> “Add home delivery.”

The responsibility must establish whether:

```text
delivery already exists
and is merely absent from website

or

merchant wants to introduce
delivery as new operating behaviour
```

If new behaviour is intended, the request moves to Reconfiguration/appropriate owner.

---

# 27. Website Population SHALL NOT own Exposure

Rejected:

```text
fact exists
    ↓
Website assistant sees it
    ↓
publish
```

Required:

```text
authoritative fact/content
        +
Exposure authority
        +
storefront composition
        ↓
public representation
```

Website Population MAY help express merchant publication intention.

It SHALL NOT treat information existence as public authority.

---

# 28. Website Population context

The Website responsibility SHALL receive purpose-built context rather than unrestricted merchant state.

Conceptually:

```text
WebsiteAssistanceContext
{
    currentCompositionSummary
    selectedSectionOrContent

    applicablePublicContentReferences
    relevantBusinessFactReferences

    registeredPresentationChoices
    supportedContentOperations

    semanticOwnerReferences
    ExposureSafeObservations

    suggestionHistory
    dismissalState
}
```

It SHALL normally exclude unrelated:

```text
payment history
staff audit logs
all historic bookings
credential data
private customer information
```

---

# 29. Website population lifecycle

The Website responsibility SHALL recognise a lifecycle broadly equivalent to:

```text
INITIAL WEBSITE
      ↓
PROGRESSIVE POPULATION
      ↓
CONTEXTUALLY COMPLETE
      ↓
MAINTENANCE
      ↓
MATERIAL BUSINESS CHANGE
      ↓
RE-EVALUATE AFFECTED AREAS
```

### Initial / Progressive Population

The assistant may proactively guide the merchant through meaningful gaps.

### Contextually Complete

No material applicable gap remains.

### Maintenance

The assistant stops inventing setup work and responds primarily to merchant requests or relevant business changes.

### Re-evaluation

A material change such as:

```text
new Service
new Product
new Location
new Publication
new capability/configuration
```

MAY cause only the affected website composition to be re-evaluated.

---

# 30. Completion SHALL reduce proactive assistance

Once contextual completeness has been reached, Website assistance SHALL transition from:

> “Here is the next important thing to add.”

toward:

> “Your website is in good shape.”

It SHALL NOT continuously produce increasingly trivial suggestions merely because the model can generate them.

> **The purpose of progressive population is to reach useful completeness, not perpetual optimisation.**

---

# 31. Scheduling & Reservation

This responsibility MAY interpret:

```text
Business Hours
Scheduling
Appointment
Booking
Calendar
availability-related policy
Schedule Intent
```

without merging those concepts.

Material ambiguity SHALL be clarified.

Example:

> “Don't take appointments next Monday.”

could mean several materially different operations and therefore may require confirmation.

---

# 32. Commerce

Commerce MAY assist with:

```text
Offering
Product
Variant
pricing intent
Ordering
Inventory interpretation
```

while preserving:

```text
Product ≠ Offering
Product ≠ Inventory
Order ≠ Inventory
```

Example:

> “We're not selling the blue one anymore.”

shall not automatically mean deletion, retirement, zero stock or cancellation.

---

# 33. Merchant Presence

Presence MAY assist with:

```text
business descriptor
contact facts
Merchant Location
Service Area
external-presence references
```

It SHALL preserve:

```text
address correction ≠ relocation
business fact ≠ Exposure
```

Example:

```text
"We moved."
    → Presence

"Move our address higher on the website."
    → Website Population
```

---

# 34. Workforce & Access

This responsibility MAY interpret staff/access intent.

It SHALL NOT grant authority.

Example:

> “James doesn't need access anymore.”

may require clarification between privilege removal, suspension, membership termination or complete access removal.

Deterministic authority remains mandatory.

> **Model sophistication SHALL NOT replace authorisation.**

---

# 35. Merchant Administration

Administration concerns Main Street/platform administration, including potentially:

```text
Merchant Account
commercial subscription
domain administration
platform preferences
administrative setup
security-flow guidance
integration-management entry points
```

There SHALL NOT be a universal semantic owner called `Settings`.

Domain configuration remains domain-owned.

---

# 36. Customer Engagement

May assist with:

```text
Publication content
announcements
customer communication
notification wording
enquiry-related communication
```

It SHALL NOT determine:

```text
consent
recipient authority
Exposure
subscription validity
notification eligibility
```

Drafting is not delivery authority.

---

# 37. Business Intelligence

Business Intelligence SHALL sit above deterministic analytical semantics.

```text
authoritative data
    ↓
defined metric/query semantics
    ↓
deterministic observation
    ↓
AI explanation / interpretation
```

It SHALL distinguish:

```text
OBSERVED
DERIVED
INFERRED
UNKNOWN
```

It SHALL NOT fabricate undefined metrics.

---

# 38. Business Intelligence SHALL NOT self-act

AI may recommend.

The merchant decides.

```text
AI observation
    ↓
merchant judgement
    ↓
new merchant instruction
    ↓
appropriate operational/reconfiguration path
```

Autonomous optimisation loops are prohibited.

---

# 39. Customer-facing AI remains separate

Customer Service AI is outside this merchant-side authority.

A future customer AI architecture must independently resolve:

```text
customer identity
anonymous customer context
privacy
customer relationship
customer operations
merchant policy
customer data access
```

---

# 40. Dependency-bound responsibilities

The following remain deferred to their owning backend targets:

```text
Provider & Integration assistance
Payment assistance
Fulfilment assistance
Returns assistance
Customer-facing AI
```

This amendment SHALL NOT pre-empt their boundaries.

---

# 41. Image Optimisation

Ordinary Image Optimisation is deterministic-first.

```text
known role
+
known rendition profile
    ↓
deterministic media processor
```

No AI is required for ordinary:

```text
resizing
rendition creation
lossless encoding
technical optimisation
```

MS-PROT-066 already requires known-profile processing without AI.

---

# 42. Video Optimisation

Video Optimisation is likewise deterministic-first.

Registered transcoding, poster generation and rendition production SHALL normally be deterministic.

AI may be used only for bounded interpretation where useful.

---

# 43. Image Amplification

Image Amplification is an optional cross-cutting generative capability rather than a mandatory standalone agent.

```text
merchant requests enhancement
        ↓
bounded amplification contract
        ↓
generative image mechanism
        ↓
candidate enhanced asset
        ↓
merchant review
```

It SHALL NOT silently fabricate materially relevant business reality.

The canonical source remains preserved, consistent with MS-PROT-066.

---

# 44. Inference classes

Main Street SHALL use model-independent classes:

```text
D0 — Deterministic

M1 — Economy inference

M2 — Higher reasoning inference

M3 — Generative media
```

A current deployment MAY map:

```text
M1 → Luna-class
M2 → Sol-class
```

but these mappings remain infrastructure decisions.

---

# 45. Model selection is per contract/request

Rejected:

```text
Reconfiguration always uses Sol
Website always uses Luna
```

Accepted:

```text
simple explicit request
    → D0/M1

materially complex bounded reasoning
    → M2
```

Model strength follows actual reasoning requirements rather than responsibility identity.

---

# 46. Frugal decision policy

Preferred order:

```text
Can D0 safely satisfy the contract?
        ↓ yes
       D0

        ↓ no

Can concise merchant clarification
resolve the material uncertainty?
        ↓ yes
       ASK

        ↓ no

Is M1 qualified?
        ↓ yes
       M1

        ↓ no

Does M2 materially improve
successful resolution?
        ↓ yes
       M2

        ↓ no
UNSUPPORTED / UNRESOLVED
```

M3 is used for explicit supported generative-media tasks.

---

# 47. Frugality means successful-outcome cost

Main Street SHALL NOT optimise only for cheapest model call.

The objective is:

> **Minimise expected cost per successful contract-valid outcome, subject to required quality, safety and acceptable latency.**

Relevant cost includes:

```text
model calls
context size
retries
validation failures
escalations
latency
merchant clarification burden
```

---

# 48. Purpose-built context

Every inference contract SHALL specify allowed context.

The model SHALL NOT receive all merchant/customer/platform information merely because that information exists.

Context minimisation reduces:

```text
cost
latency
privacy exposure
model distraction
hallucination surface
```

This preserves the accepted MS-PROT-057 v1.1 context boundary.

---

# 49. Sparse activation

Only the minimum necessary responsibility set SHALL be invoked.

Rejected:

```text
broadcast to all specialists
agent voting
recursive debates
open-ended autonomous delegation
```

Compound merchant intent MAY invoke several bounded responsibilities where genuinely necessary.

The accepted prohibition on uncontrolled agent swarms remains authoritative.

---

# 50. Traceability

For materially inferred business meaning Main Street SHALL be able to reconstruct:

```text
merchant input
    ↓
non-authoritative hypothesis
    ↓
merchant confirmation
    ↓
owner-qualified candidate
    ↓
applicable approval
    ↓
deterministic validation
    ↓
authoritative operation
```

AI inference itself is not merchant-intent authority.

---

# 51. Final falsification of progressive Website assistance

### Information publisher

Site legitimately contains:

```text
Publications
Enquiry
Subscription
```

No Product/Booking gaps are generated merely because those capabilities exist elsewhere in Main Street.

**PASS**

### Plumber with no gallery

Gallery is optional.

Website can become contextually complete without one.

**PASS**

### Consultant with no Products

No Product suggestion generated because Products are not part of the applicable operating model.

**PASS**

### Merchant says “not now”

Suggestion is suppressed rather than immediately repeated.

Merchant continues operating normally.

**PASS**

### Merchant says “I don't want an About section”

The preference is retained and the assistant does not repeatedly classify the absence as unresolved.

**PASS**

### Missing optional image

Merchant continues receiving bookings/orders and managing the business.

**PASS**

### Missing information genuinely required for one public operation

Only that applicable website/public operation may be rejected by its deterministic contract.

Unrelated operations continue.

**PASS**

### New Service added later

Website maintenance re-evaluates relevant Service surfaces and may suggest content/image additions.

It does not re-open unrelated website setup.

**PASS**

### Contextually complete website

Assistant stops inventing increasingly trivial improvements.

**PASS**

### Suggestion identifies possible Booking opportunity

Website assistant may suggest the option.

Merchant interest routes to Reconfiguration rather than silently enabling Booking.

**PASS**

### Merchant never engages with suggestions

Suggestions remain advisory and spaced.

Main Street business operation remains unaffected.

**PASS**

---

# 52. Rejected designs

The following are rejected:

1. AI-only ordinary merchant operations.
2. Fixed agent count as semantic architecture.
3. One model permanently assigned to one responsibility.
4. AI promotion of unconfirmed semantic assumptions.
5. Redundant confirmation of explicit merchant instructions.
6. Universal strongest-model usage.
7. Cheapest-model-first dogma without outcome economics.
8. Concierge-model invocation on every message.
9. Unrestricted merchant-account context.
10. Agent swarms.
11. Website AI creating business semantics merely to satisfy copy.
12. Website AI treating fact existence as Exposure.
13. Website completion as universal field completion.
14. Website incompleteness blocking unrelated business activity.
15. Repetitive or engagement-maximising website nudges.
16. Re-presenting explicitly rejected website suggestions without material change.
17. Perpetual website optimisation after contextual completeness.
18. AI-driven business-intelligence self-reconfiguration.
19. AI for known-profile image/video optimisation.
20. Image amplification that materially fabricates business reality.

---

# 53. Hard invariants

1. AI remains optional for ordinary merchant business operations.
2. Ordinary operations retain deterministic non-AI paths.
3. Optional AI-native enhancements may remain AI-dependent.
4. AI architecture is inference-contract-first.
5. Deployed agent count is not an architectural invariant.
6. Responsibility does not imply model identity.
7. Main Street Assistant does not own business semantics.
8. Deterministic routing is preferred where sufficient.
9. AI may form non-authoritative hypotheses.
10. Materially inferred business meaning requires merchant confirmation before semantic promotion.
11. Explicit merchant intent shall not trigger redundant clarification.
12. Interpretation confirmation and consequential approval remain conceptually distinct.
13. AI shall not invent business facts or executable semantics.
14. AI responsibilities shall not acquire capability authority.
15. Reconfiguration shall not silently rewrite existing commitments.
16. Website Population & Management may operate reactively and progressively.
17. Website completeness is contextual, not universal.
18. Website suggestions remain advisory and non-blocking.
19. Website suggestions shall be prioritised, deduplicated and spaced.
20. Temporary dismissal and explicit rejection shall be respected.
21. Website assistance shall not invent facts to increase completeness.
22. Website assistance shall not own Exposure.
23. Website assistance shall not activate new business behaviour from a suggestion.
24. Contextually complete websites shall transition to maintenance mode.
25. Only materially affected areas should be re-evaluated after later business changes.
26. Security enforcement remains deterministic.
27. Business Intelligence requires deterministic metric/query semantics.
28. Business Intelligence shall not autonomously mutate business state.
29. Image Optimisation is deterministic-first.
30. Video Optimisation is deterministic-first.
31. Image Amplification is optional and shall not silently fabricate business reality.
32. Customer-facing AI requires separate authority.
33. Provider, Payment, Fulfilment and Returns assistance remain dependency-bound.
34. Product priority and inference complexity remain separate axes.
35. Model names remain replaceable infrastructure.
36. Inference class is selected per bounded contract/request.
37. D0 is preferred where sufficient.
38. Clarification is preferred over probabilistic guessing where it efficiently resolves material uncertainty.
39. Frugality is measured by expected successful-outcome cost.
40. Context must remain purpose-built and minimised.
41. Specialist activation remains sparse.
42. Open-ended autonomous agent delegation remains prohibited.
43. Material AI-assisted outcomes remain traceable.
44. New assistance responsibilities require evidence of materially distinct merchant jobs and inference boundaries.

---

# 54. Deferred decisions

This authority deliberately leaves unresolved:

```text
number of deployed agent processes
number of model deployments
exact Luna/Sol mappings
future model names/providers
token and monetary ceilings
exact suggestion cooldown duration
exact website-gap scoring algorithm
exact suggestion presentation UX
exact dismissal storage representation
exact contextual-completeness algorithm
Business Intelligence metric catalogue
Customer-side AI architecture
Provider assistance architecture
Payment assistance architecture
Fulfilment assistance architecture
Returns assistance architecture
full Image Amplification transformation catalogue
concrete Java implementation
```

These are downstream decisions requiring implementation evidence or their owning design targets.

---

# 55. Programme effect

This amendment refines the already-closed AI inference boundary.

It does not:

```text
reopen Target 6
close Target 8
activate Target 9
authorise implementation
authorise code
authorise migrations
authorise frontend/prototype work
```

MS-PROT-079 remains authoritative for backend sequencing.

---

# 56. Final governing principle

> **Main Street shall provide one coherent AI-assisted merchant experience through bounded assistance responsibilities and versioned inference contracts rather than a fixed fleet of autonomous agents. AI may interpret merchant language, identify opportunities and form non-authoritative hypotheses, but materially inferred business meaning shall not become merchant intent until confirmed. Website assistance may progressively guide merchants toward a contextually complete website, but its suggestions must remain truthful, prioritised, spaced, dismissible and non-blocking. Ordinary business operation remains AI-independent. Deterministic mechanisms are preferred whenever sufficient, and inference expenditure is governed by measured cost per successful contract-valid outcome rather than agent identity, model prestige or cheapest-call dogma.**

---

## Governance assessment

**DESIGN / PROPOSE:** COMPLETE  
**REVIEW:** COMPLETE  
**FALSIFICATION:** **PASS**, including progressive Website Population/Management cases  
**CORPUS CONFORMANCE:** PASS within the governed scope  
**RECOMMENDATION:** **ACCEPT**  
**MANUAL APPROVAL:** **GRANTED on 27 August 2026**  
**STATUS:** **ACCEPTED**