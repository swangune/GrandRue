# MS-PROT-094 — Merchant Presentation Convergence, Storefront Composition & Centrally Evolvable Website Delivery Amendment

**Document ID:** MS-PROT-094  
**Version:** 1.0  
**Status:** **ACCEPTED**  
**Approved:** Explicit manual approval on 13 September 2026 after Fundamental Vision Conformance, full design review, falsification, maintenance-cost review and complete pre-approval presentation  
**Authority type:** Material presentation and delivery-surface architecture amendment  
**Governed by:** `MS-DESIGN-RULES-001`; `DOCUMENT-GOVERNANCE.md`  
**Fundamental authority:** `MS-FUNDAMENTAL-VISION-001`  
**Amends:** composite MS-PROT-036 within storefront presentation/composition, data-non-ownership and centrally evolvable delivery scope; composite MS-PROT-037 within merchant presentation consistency scope; composite MS-PROT-049 within registered-presentation-profile and website-presentation recommendation scope; composite MS-PROT-057 within website presentation-assistance scope  
**Preserves:** MS-PROT-093 v1.0; capability-owned semantics; Projection/Exposure authority; public/customer application contracts; merchant authority; business-type neutrality; provider neutrality; existing customer/storefront delivery-surface independence  
**Depends on:** MS-FUNDAMENTAL-VISION-001; composite MS-PROT-027; MS-PROT-029; composite MS-PROT-035; composite MS-PROT-036; composite MS-PROT-037; composite MS-PROT-049; composite MS-PROT-057; MS-PROT-066; MS-PROT-093; applicable customer/public capability authorities  
**Implementation activation:** NONE  
**Purpose:** Establish the deliberate presentation asymmetry between Main Street merchant software and merchant-branded customer-facing websites; replace registered storefront presentation profiles with merchant-specific composition; establish storefront business-data non-ownership; and ensure that arbitrarily many differentiated merchant websites remain centrally maintainable, evolvable, testable and roll-backable through shared Main Street website engines.

---

# 0. Fundamental Vision Conformance

## 0.1 Business-to-software translation

Main Street SHALL absorb presentation-system complexity rather than transfer that complexity to merchants.

Merchants SHALL NOT be required to understand or administer:

```text
website templates
themes
layout systems
component trees
responsive breakpoints
composition graphs
presentation-profile identifiers
frontend frameworks
storefront engine versions
```

Merchants MAY provide ordinary business-facing inputs including:

```text
content
media
logo
brand information
supported presentation preferences
desired emphasis expressed in business language
```

Main Street owns translation of those inputs into the supported customer-facing website presentation.

## 0.2 Administrative compression

The proposal reduces recurring merchant administration.

Merchant software remains a coherent Main Street product rather than a separately customised application per business.

Customer-facing websites remain centrally operated Main Street presentation surfaces rather than independently maintained merchant applications.

Adding merchants SHALL NOT create proportional:

```text
merchant software variants
website codebases
manual website maintenance
merchant integration work
presentation administration
```

## 0.3 Ordinary staff

The merchant-client consistency model reduces training rather than increasing it.

An ordinary staff member SHOULD be able to transfer substantial interaction familiarity between businesses using Main Street on the same execution environment.

## 0.4 Internal complexity

The architecture introduces internal complexity for:

```text
composition revisioning
shared rendering evolution
composition-engine evolution
compatibility
validation
staged rollout
rollback
cross-estate presentation maintenance
```

This complexity remains inside Main Street.

It MUST NOT require corresponding merchant configuration or staff product training.

## 0.5 Vision result

```text
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
```

The internal complexity is justified because it enables Main Street to absorb website design and maintenance administration while preserving platform-managed reliability and merchant-specific public presentation.

---

# 1. Governing Decision

Main Street SHALL use deliberately different presentation strategies for merchant operational software and merchant-branded customer-facing websites.

Canonical:

```text
MERCHANT SOFTWARE                    CUSTOMER-FACING WEBSITE

Main Street is the product           Merchant is the presented identity

consistency is valuable              compositional freedom is valuable

environment-optimised                merchant-specific composition

capability/role/state variation      broad presentation variation

Main Street design identity          no required Main Street visual identity

central product experience           central engine + merchant composition
```

Governing principle:

> **Operational surfaces converge around Main Street. Customer-facing websites compose around the merchant.**

This distinction does not alter business-semantic ownership.

---

# 2. Scope

This authority governs:

```text
merchant presentation consistency
environment-specific merchant presentation adaptation
customer-facing website composition freedom
storefront structural presentation constraints
storefront business-data non-ownership
storefront composition revisions
shared storefront composition/rendering responsibilities
central website evolution
presentation rollout and rollback boundaries
AI composition boundaries
template-gravity controls
maintenance scalability
```

This authority does NOT establish:

```text
new merchant business semantics
new customer business semantics
new capability ownership
new payment authority
new booking authority
new availability authority
new Exposure authority
new Actor Authorisation
new merchant policy
a universal UI framework
a universal business/presentation DSL
```

---

# 3. Canonical Terminology

## 3.1 Merchant client

A **Merchant Client** is a first-party Main Street operational client governed by MS-PROT-093.

Current admitted client classes are:

```text
merchant-web
Android
iOS
Windows
macOS
```

`Apple` may be used informally to group applicable Apple platforms but is not a replacement for the explicit platform classes above.

## 3.2 Customer-Facing Website

A **Customer-Facing Website**, also referred to as the storefront within MS-PROT-036 scope, is the merchant-branded website delivered through Main Street's storefront architecture for customers/public visitors.

It is not:

```text
merchant software
a future customer native app
a marketplace listing
a generic public API
every possible customer interaction channel
```

## 3.3 Storefront Composition Revision

A **Storefront Composition Revision** is an immutable, merchant-storefront-scoped presentation artifact describing how authorised presentation inputs are arranged for rendering.

A Storefront Composition Revision MAY describe:

```text
presentation hierarchy
grouping
relative prominence
layout relationships
presentation primitive references
responsive presentation relationships
typographic relationships
spacing relationships
media treatment
navigation treatment
customer-action prominence
other presentation-only relationships
```

A Storefront Composition Revision MUST NOT own or duplicate authoritative business truth.

It MUST NOT establish:

```text
price
availability
capacity
stock
opening hours
booking eligibility
payment state
merchant policy
customer authority
staff authority
business capability participation
```

A Storefront Composition Revision is authoritative only for the selected presentation arrangement within its presentation scope.

It is NOT business-semantic authority.

Each published storefront SHALL resolve to one current published Storefront Composition Revision at a time.

A new material recomposition SHALL create a new revision rather than mutating the previously published revision in place.

The exact persistence technology and serialization representation remain implementation details provided these invariants are preserved.

## 3.4 Storefront Composition Engine

The **Storefront Composition Engine** is the Main Street presentation mechanism that produces candidate Storefront Composition Revisions from permitted presentation inputs.

It is a purpose-specific storefront presentation mechanism.

It is not a business-semantic owner.

## 3.5 Storefront Rendering Engine

The **Storefront Rendering Engine** is the shared Main Street runtime responsible for rendering a published Storefront Composition Revision together with current authorised presentation material.

It does not create business truth.

## 3.6 Presentation Primitive

A **Presentation Primitive** is a bounded reusable presentation or interaction mechanism.

Examples MAY include:

```text
accessible navigation behaviour
media presentation
structured text presentation
collection rendering
customer-action presentation
location/contact presentation
form mechanics
focus management
responsive layout mechanics
```

A Presentation Primitive is not:

```text
a complete website
a domain template
a reusable site style
a preassembled page archetype
a business-semantic object
```

## 3.7 Template / Presentation Profile

For this authority, a **Template / Presentation Profile** is a reusable pre-authored multi-region, page-level or site-level arrangement selected and populated to produce a merchant website.

The governing storefront architecture SHALL NOT use such objects.

Renaming an equivalent mechanism does not make it conforming.

---

# 4. Merchant Presentation Convergence

First-party merchant clients SHALL deliberately favour a coherent Main Street operational experience.

Merchants with materially equivalent operational needs using the same client environment MAY receive substantially identical presentation.

Merchant identity or business domain does not require aesthetic differentiation of Main Street operational software.

Variation SHOULD arise from:

```text
enabled capabilities
Actor Authorisation
role
current operational state
current work
attention/exception state
accessibility requirements
execution environment
demonstrated operational need
```

Variation SHOULD NOT arise merely from:

```text
merchant business category
merchant desire for unique software appearance
merchant public branding
desire to demonstrate technical customisation
```

Merchant identity information such as business name or logo MAY appear as identity content.

Such identity content does not require the surrounding Main Street application to become merchant-themed.

Canonical:

> **The merchant interface adapts operationally and environmentally, not decoratively merely because merchants are different businesses.**

---

# 5. Merchant Environment Optimisation

MS-PROT-093 remains authoritative and is NOT amended by this authority.

Merchant clients SHALL continue to optimise for their execution environment.

Examples of environment-local presentation concerns include:

```text
touch interaction
pointer interaction
keyboard interaction
windowing
multi-window use
information density
system menus
native controls
notifications
camera/media use
device integration
peripheral integration
```

Cross-platform pixel parity is NOT a design objective.

The required relationship is:

```text
same Main Street business meaning
+
same operational concepts
+
same business-language intent
+
environment-appropriate interaction
```

Android, iOS, Windows, macOS and web MAY therefore differ materially in layout and interaction while remaining manifestations of the same Main Street operational product.

---

# 6. Customer-Facing Website Composition

Every Main Street Customer-Facing Website SHALL be composed for the merchant.

Main Street SHALL NOT use as the governing website-generation mechanism:

```text
templates
themes
named reusable styles
style packs
business-domain layouts
presentation profiles
website archetypes
preassembled page recipes
finite hidden site families
```

Rejected:

```text
SALON
  ↓
Salon Template
  ↓
merchant content
  ↓
website
```

Rejected:

```text
merchant evidence
  ↓
AI chooses hidden layout #7
  ↓
website
```

Required:

```text
authorised public/customer-facing material
+
merchant content references
+
merchant media references
+
merchant brand inputs
+
available customer actions
+
presentation capabilities
+
web experience constraints
        ↓
merchant-specific composition
        ↓
Storefront Composition Revision
        ↓
shared Storefront Rendering Engine
```

---

# 7. Presentation Diversity Boundary

Business-domain similarity MUST NOT impose customer-facing website similarity.

Capability-set similarity MUST NOT impose customer-facing website similarity.

The following implication is prohibited:

```text
same business domain
        ⇒
same website design
```

The following remains valid:

```text
similar merchant-specific evidence
        ↓
similar composition may emerge
```

Main Street SHALL NOT artificially force uniqueness merely to make two websites different.

The requirement is compositional freedom, not random differentiation.

Canonical:

> **Business similarity does not prescribe presentation similarity; presentation similarity may nevertheless emerge where merchant-specific evidence justifies it.**

A large population of Main Street websites SHOULD be capable of exhibiting the broad design diversity expected from many independently designed professional websites.

This quality objective SHALL NOT be implemented through an ever-growing catalogue of styles or templates.

---

# 8. Visual Character Is an Outcome

Concrete customer-facing website presentation MAY include different:

```text
typography
colour use
spacing
density
visual hierarchy
image treatment
media prominence
section sequencing
alignment
symmetry/asymmetry
background treatment
navigation treatment
commerce presentation
service presentation
call-to-action treatment
motion
responsive transformation
```

Main Street SHALL NOT require these properties to be selected through reusable named style objects.

Canonical:

```text
merchant-specific inputs
+
composition decisions
        ↓
visual character
```

not:

```text
style identity
        ↓
website composition
```

---

# 9. Stable Website Structural Responsibilities

Composition freedom does not remove fundamental web structure and usability responsibilities.

A normal customer-facing page SHALL preserve a logical structure in which:

```text
site identity / entry / navigation responsibility
        precedes
principal page content
        precedes
terminal site/footer responsibility
```

Visual implementation MAY differ materially.

The site-entry/navigation region MAY:

```text
overlay imagery
use centred identity
use compact navigation
use multiple navigation rows
use minimal navigation treatment
use another accessible presentation
```

provided logical structure, discoverability and accessibility remain conforming.

The terminal/footer region MAY differ materially in layout, density and visual treatment while remaining logically after the principal page content.

Canonical:

> **Stable structural responsibility does not imply stable visual appearance.**

Primary applicable customer actions SHALL remain discoverable.

Merchant identity SHALL remain understandable.

Responsive interaction SHALL remain usable.

Material meaning SHALL NOT depend on colour alone.

---

# 10. Customer-Facing Website Business-Data Non-Ownership

A Customer-Facing Website SHALL NOT own authoritative business data.

Authoritative state remains with accepted capability owners.

This includes, without limitation:

```text
merchant operational facts
products/services
prices
inventory
availability
capacity
business hours
bookings
orders
payments
customer relationships
staff availability
merchant operating policies
```

The website consumes authorised projections, Exposure results and application contracts.

Canonical:

```text
authoritative capability state
        ↓
authorised projection / Exposure
        ↓
Storefront Rendering Engine
        ↓
customer presentation
```

The following MUST NOT become independent authoritative copies:

```text
browser state
CDN materialisation
generated HTML
static generation output
search representation
client cache
LLM-generated content
Storefront Composition Revision
```

Derived caching or materialisation MAY be used where permitted by existing projection, caching, security and resilience authority.

Possession or display of a value does not establish authority to commit against that value.

---

# 11. Composition Is Presentation State, Not Business State

A Storefront Composition Revision MAY be durable because Main Street requires:

```text
stable publication
versioning
controlled migration
rollback
presentation provenance
```

That durability does not make the website a business-data owner.

The revision SHOULD reference authoritative content/material rather than duplicate mutable capability-owned values.

For example:

```text
composition:
"primary service collection receives high prominence"

not:

"Haircut costs £45"
```

A change to an authoritative business value such as price SHALL NOT require website recomposition solely because the value changed.

The current projection supplies the changed value during rendering.

A new composition is required only where presentation arrangement itself changes.

---

# 12. Customer Intent and Authoritative Execution

Customer-facing interactions SHALL continue to invoke accepted public/customer application contracts.

Canonical:

```text
customer intent
        ↓
website interaction
        ↓
public/customer application contract
        ↓
authorisation / eligibility / deterministic validation
        ↓
capability-owned execution
        ↓
authoritative state
        ↓
updated projection
        ↓
website presentation
```

The website does not itself create:

```text
Booking
Order
Payment
Reservation
Inventory mutation
customer authority
```

Displayed current state SHALL be revalidated by the owning operation where the operation requires current authoritative eligibility.

---

# 13. Shared Storefront Engine Architecture

Main Street SHALL maintain shared storefront infrastructure rather than merchant-specific website applications.

Canonical:

```text
                   MAIN STREET PLATFORM
                           │
              authoritative public material
                           │
             ┌─────────────┴─────────────┐
             │                           │
             ▼                           ▼
  Storefront Composition Engine   authorised projections
             │                           │
             ▼                           │
 Storefront Composition Revision         │
             │                           │
             └─────────────┬─────────────┘
                           ▼
                Storefront Rendering Engine
                           │
                           ▼
                Customer-Facing Website
```

Adding a merchant SHALL NOT require:

```text
a merchant-specific frontend repository
a merchant-specific deployed application
merchant-specific routine engineering maintenance
merchant-specific business-rule code
```

The shared engine MAY gain new presentation capabilities over time.

Such capabilities MUST remain presentation mechanisms rather than merchant-domain semantics.

---

# 14. Central Evolution

Main Street SHALL be able to improve existing customer-facing websites through shared storefront infrastructure.

Two classes of evolution are distinguished.

## 14.1 Rendering Engine Evolution

A Rendering Engine improvement MAY improve compatible existing websites without creating a new Storefront Composition Revision where the composition's presentation intent remains valid.

Examples MAY include:

```text
accessibility implementation
responsive behaviour
image delivery
browser compatibility
performance
typographic rendering
focus behaviour
navigation mechanics
motion implementation
```

## 14.2 Recomposition

A change that materially changes the merchant-specific presentation arrangement SHALL produce a new Storefront Composition Revision.

Recomposition MAY be initiated because of:

```text
merchant presentation changes
material public-surface change
composition-engine improvement
presentation-capability improvement
platform-wide design-quality improvement
```

A recomposition operation MUST NOT mutate business truth.

---

# 15. Automatic Platform-Managed Presentation Improvement

Main Street MAY automatically apply centrally managed presentation improvements without requiring merchant approval for every engine release or recomposition when all of the following remain preserved:

```text
merchant-authored/approved public content
authoritative business facts
supported merchant brand inputs
explicit supported presentation preferences
available customer-operation meaning
customer authority
merchant policy
legal/security/accessibility constraints
```

A platform-managed presentation improvement MUST NOT silently rewrite merchant-authored content or merchant business policy.

Where a change requires alteration of merchant-owned content, explicit merchant presentation choice or business meaning, the governing owner/approval process remains required.

This rule exists to preserve Administrative Compression.

Merchants SHALL NOT become routine website-release administrators.

---

# 16. Versioning, Validation and Rollback

The centrally evolvable model creates large blast-radius potential.

Material storefront-engine changes SHALL therefore support:

```text
versioned release
validation before publication
representative-corpus testing
controlled rollout
rollback
```

Exact rollout percentages, cohort sizes and deployment technology are implementation details.

A material recomposition SHALL NOT replace the current published composition until the candidate composition has passed applicable deterministic structural and accessibility/conformance validation.

If candidate recomposition fails validation:

```text
candidate is not published
current valid composition remains current
```

If a material rollout reveals unacceptable presentation behaviour:

```text
rollback restores a previously valid compatible presentation path
```

An estate-wide improvement MUST NOT require manual editing of each merchant website.

---

# 17. AI-Assisted Composition

AI MAY assist Storefront Composition.

Permitted input scope MAY include:

```text
authorised public/customer-facing facts
merchant-authored/approved content
merchant media references
merchant brand inputs
available customer actions
presentation capabilities
web experience constraints
```

AI MAY reason about:

```text
hierarchy
grouping
relative prominence
content relationships
media emphasis
visual rhythm
responsive composition
customer-action prominence
```

AI MUST NOT establish or invent:

```text
business facts
prices
availability
capacity
stock
merchant policy
opening hours
customer authority
staff authority
business capability participation
payment state
booking eligibility
```

AI output is a presentation candidate.

It becomes a published Storefront Composition Revision only after the applicable validation/publication path.

---

# 18. No Runtime AI Dependency

A valid published Customer-Facing Website SHALL remain renderable when AI is unavailable.

AI MAY be invoked for:

```text
initial composition
requested recomposition
platform-managed recomposition
optional presentation assistance
```

AI SHALL NOT be required merely to serve an already published ordinary website request.

Canonical:

```text
valid published composition
+
authorised projections
+
Rendering Engine
        ↓
website remains serviceable
```

---

# 19. Hidden Templates Are Also Prohibited

The template prohibition applies to internal architecture as well as merchant-visible UX.

The following remains non-conforming even when hidden from merchants:

```text
merchant classification
        ↓
AI/rule classifier
        ↓
finite site archetype
        ↓
content population
```

Concepts such as the following trigger review when they determine substantial website arrangement:

```text
SalonLayout
HospitalityStyle
LuxuryTheme
CompositionPreset
WebsiteRecipe
VisualArchetype
PresentationProfile
TemplateId
StyleId
```

The conformance question is:

> **Was this merchant's presentation composed, or was a substantially predefined site selected and populated?**

The latter is non-conforming.

---

# 20. Presentation Primitive Admission

A reusable Presentation Primitive SHOULD be promoted into shared storefront infrastructure only where it represents a genuinely reusable presentation or interaction responsibility.

A primitive SHOULD NOT be introduced merely because one merchant composition requires a new visual appearance.

Where existing smaller primitives can express a presentation correctly without unacceptable complexity, accessibility loss or performance cost, unnecessary new global primitives SHOULD be avoided.

A shared primitive MUST NOT accumulate:

```text
business-domain branching
merchant-type policy
business eligibility
business authority
arbitrary executable expressions
full page structure
complete website structure
```

Canonical:

```text
primitive
≠
section template
≠
page template
≠
website template
```

---

# 21. No Universal Presentation DSL

This authority does NOT approve a universal Main Street UI language, universal page DSL or arbitrary visual-programming runtime.

The Storefront Composition Engine is purpose-bounded to customer-facing website presentation.

Its representation MUST NOT acquire:

```text
business policy
capability semantics
authorisation
availability calculation
provider readiness interpretation
arbitrary scripting
general workflow execution
```

If implementing new website requirements repeatedly pressures the composition representation toward those responsibilities, the boundary MUST return to governed design review.

---

# 22. Semantic and Template Gravity

Storefront composition is subject to the existing persistent Semantic Gravity watch.

Warning signs include:

```text
merchant-type branching in the composition engine
business vocabulary inside generic layout machinery
composition objects acquiring business policy
presentation primitives acquiring operation authority
one composition context accumulating arbitrary business facts
generic expression bags
large preassembled page structures
finite reusable site families
```

The default corrective response is:

```text
decompose
localise
remove unnecessary abstraction
increase merchant-specific composition freedom
return business meaning to its owner
```

The default response is NOT:

```text
create another template
broaden the universal composition engine
```

---

# 23. Maintenance Scalability

Merchant count SHALL NOT imply proportional engineering-maintenance growth.

The target relationship is:

```text
more merchants
        ↓
more Storefront Composition Revisions
```

not:

```text
more merchants
        ↓
more website codebases
more templates
more bespoke maintenance
```

Adding an ordinary conforming merchant SHOULD require approximately no merchant-specific engineering work after the platform supports the merchant's required capabilities and presentation needs.

The architecture optimises for:

> **high presentation diversity with central maintenance leverage.**

---

# 24. Merchant-Client Scalability

Merchant-client scalability follows the opposite presentation strategy.

Canonical:

```text
more merchants
+
more business domains
        ↓
same coherent Main Street client families
+
different applicable capabilities/roles/state
```

Merchant count SHOULD NOT cause proportional growth in dashboard designs.

Presentation consistency across merchants is a positive property where operational requirements are materially equivalent.

---

# 25. Storefront Scalability

Customer-facing website scalability is:

```text
more merchants
        ↓
more merchant-specific compositions
```

not:

```text
more merchant domains
        ↓
more templates/styles
```

Main Street SHALL NOT require creation of a new domain template/style merely because a new merchant type enters the platform.

A new presentation mechanism is justified by a materially new presentation responsibility, not by business-category naming.

---

# 26. Failure Boundaries

## 26.1 Rendering failure

Rendering failure MUST NOT mutate or redefine authoritative business state.

## 26.2 Composition failure

A failed candidate composition or recomposition MUST NOT replace the current valid published composition.

## 26.3 AI failure

AI failure MUST NOT deactivate an already valid website.

## 26.4 Projection staleness

Stale or cached display material MUST NOT become transaction authority.

## 26.5 Engine migration failure

A failed engine/composition migration MUST preserve or restore a previously valid compatible presentation path where technically possible.

## 26.6 Merchant operations

Customer-facing website failure MUST NOT redefine unrelated merchant operational state.

---

# 27. Trade-Off Decision

The selected architecture intentionally rejects the minimum-maintenance template model.

## Alternative A — Template / Theme Catalogue

Advantages:

```text
simple QA
cheap implementation
predictable layout count
```

Rejected because:

```text
constrains presentation diversity
creates style/domain classification pressure
scales by adding templates
conflicts with merchant-specific composition requirement
```

## Alternative B — Bespoke Merchant Website Codebases

Advantages:

```text
maximum presentation freedom
```

Rejected because:

```text
maintenance grows with merchant count
security/update drift
per-merchant deployment
per-merchant regression burden
poor central evolution
```

## Alternative C — Runtime AI Website Generation

Advantages:

```text
high apparent flexibility
```

Rejected because:

```text
live AI dependency
runtime cost
nondeterministic presentation
latency
failure coupling
hidden semantic risk
```

## Alternative D — Independently Generated Static Applications

Advantages:

```text
AI/generation flexibility
runtime simplicity
```

Rejected as the governing model because:

```text
merchant-specific application artifacts acquire upgrade burden
central engine improvement becomes migration across generated applications
presentation drift becomes harder to control
```

Static/CDN materialisation MAY still be used as derived delivery optimisation where existing authority permits it.

## Selected — Shared Engines + Merchant-Specific Composition Revisions

Advantages:

```text
high presentation freedom
central maintenance
shared security/accessibility improvement
no per-merchant codebase
AI-independent ordinary serving
rollback
business-data non-ownership
```

Accepted costs:

```text
more sophisticated composition machinery
larger presentation QA space
versioning
estate-level rollout discipline
visual-quality evaluation
```

Those costs are borne by Main Street rather than merchants.

---

# 28. Falsification

The following cases are normative evidence shaping this boundary.

| Case | Required result |
|---|---|
| 500 salons use substantially identical Main Street iPhone client presentation | PASS |
| Those salons expose different operations because capabilities/roles differ | PASS |
| Windows merchant client uses denser keyboard/pointer interaction than iPhone | PASS |
| Same-domain merchants receive materially different customer-facing website compositions | PASS |
| Same-domain merchants naturally arrive at similar websites from similar evidence | PASS |
| `SALON` selects a predefined visual layout | FAIL |
| AI chooses one of a hidden finite set of site designs | FAIL |
| Website price changes require editing the composition revision | FAIL |
| Authoritative price changes and the website presents the new projection | PASS |
| Customer commits against cached availability without backend revalidation | FAIL |
| Existing websites continue when AI is unavailable | PASS |
| Shared accessibility-engine improvement benefits thousands of websites | PASS |
| Material engine improvement requires individual developer edits to thousands of websites | FAIL |
| Platform-managed recomposition produces different new revisions for different merchants | PASS |
| Failed recomposition replaces current valid website | FAIL |
| Merchant must understand breakpoints/components/layout graphs | FAIL |
| Reusable primitive evolves into nearly complete pre-authored website | FAIL |
| Composition engine begins interpreting Booking or Inventory policy | FAIL |
| CDN/static material becomes authoritative business state | FAIL |
| Website can be redesigned/recomposed without migrating merchant operational data | PASS |
| Merchant count increases while template catalogue remains absent | PASS |

---

# 29. Amendment — MS-PROT-036

This authority materially amends composite MS-PROT-036 only within the scopes below.

All unlisted MS-PROT-036 rules remain unchanged.

## 29.1 Governing composition input

Where MS-PROT-036 currently includes a `Presentation Profile` as a storefront-composition input, that concept is superseded by:

```text
merchant-specific Storefront Composition Revision
```

produced under this authority.

## 29.2 Section 3 presentation input

The concept:

```text
registered presentation components/profiles
```

is superseded for customer-facing website composition by:

```text
bounded Presentation Primitives
merchant brand inputs
website structural/accessibility constraints
merchant-specific composition
```

## 29.3 Section 4

The existing prohibition against niche-specific applications is strengthened.

Business category SHALL additionally NOT select:

```text
website template
theme
named style
domain layout
presentation profile
site archetype
```

## 29.4 Section 5

MS-PROT-036 Section 5 `Presentation profile` is completely superseded within storefront scope.

The replacement rule is Sections 6–22 of MS-PROT-094.

## 29.5 Section 6

Merchant branding/content inputs remain valid.

Those inputs SHALL feed merchant-specific composition rather than customise a preselected template/profile.

## 29.6 Sections 12–13

The existing dynamic-state and caching rules are preserved and strengthened by the Customer-Facing Website Business-Data Non-Ownership rule in this authority.

Derived material MAY be cached/materialised.

It MUST NOT become authoritative business data.

## 29.7 Section 15

The AI permission to recommend registered presentation options is superseded.

AI MAY instead propose merchant-specific composition under MS-PROT-094 and applicable MS-PROT-057 constraints.

## 29.8 Accepted invariant 4

The former invariant referring to bounded presentation profiles is superseded.

Storefront composition instead derives from applicable semantics, authorised projections/Exposure, capability contributions, merchant content/media references, brand inputs, web constraints and merchant-specific composition.

## 29.9 Accepted invariant 11

The former AI permission to recommend registered presentation is superseded by bounded merchant-specific composition assistance.

## 29.10 Deferred scope

Any reference to future theme/profile selection SHALL NOT be interpreted as permitting reusable storefront themes/templates/profiles.

Exact primitive implementation, typography mechanics, colour mechanics, SEO technology, route names, CDN/cache technology and composition serialization remain downstream where they do not violate this authority.

---

# 30. Amendment — MS-PROT-037

This authority adds the following merchant-presentation invariant to composite MS-PROT-037.

> **Merchant Presentation Consistency Invariant:** Merchant identity and business category do not require aesthetic dashboard differentiation. First-party merchant presentation SHOULD deliberately favour a coherent Main Street operational experience. Presentation variation SHOULD arise from applicable capability, actor role/authority, operational state, current work, accessibility requirements, execution environment or demonstrated operational need.

MS-PROT-037's existing capability-driven composition, role-native filtering, mobile-first operation and business-language rules remain unchanged.

MS-PROT-093 remains authoritative for the expanded first-party merchant-client portfolio and platform-native implementation freedom.

---

# 31. Amendment — Composite MS-PROT-049

This authority amends composite MS-PROT-049 only within storefront presentation-profile scope.

All contribution ownership, contribution identity, eligibility, Projection authority, operation authority and composition-target semantics remain unchanged.

## 31.1 Relationship to MS-PROT-036

References to MS-PROT-036 as authority for `bounded presentation profiles` SHALL be read as authority for merchant-specific storefront composition under composite MS-PROT-036 + MS-PROT-094.

## 31.2 Business-category presentation

The Section 22 pattern:

```text
resolved semantics
+
applicable surface contributions
+
registered presentation profile
```

is superseded for customer-facing websites by:

```text
resolved semantics
+
applicable public/customer contributions
+
authorised projections/Exposure
+
merchant presentation inputs
+
merchant-specific Storefront Composition Revision
```

No business-category presentation profile is permitted.

## 31.3 Presentation descriptor boundary

A `presentationDescriptorReference`, where retained for a capability contribution, MUST remain a bounded presentation hint/mechanism reference.

It MUST NOT reference or select:

```text
complete storefront template
theme
named reusable site style
multi-section site recipe
domain layout
Storefront Composition Revision
```

Capability contribution definitions do not own website arrangement.

## 31.4 AI presentation recommendation

The former rule that AI may recommend registered presentation/composition options is superseded for customer-facing websites.

AI MAY propose merchant-specific composition under MS-PROT-094 and MS-PROT-057.

AI MUST NOT author executable surface semantics.

---

# 32. Amendment — Composite MS-PROT-057

This authority amends MS-PROT-057 v1.2 only within Website Population & Management presentation scope.

All AI merchant-intent, semantic-promotion, confirmation, deterministic validation and authoritative-operation boundaries remain unchanged.

## 32.1 Website concepts

The Website Population & Management responsibility MAY reason about:

```text
business introduction
About information
customer-facing services/products
contact/location presentation
publications
media
homepage emphasis
customer interaction entry points
merchant brand inputs
merchant-specific composition needs
```

It SHALL NOT require the merchant to select or understand:

```text
templates
themes
styles
presentation profiles
component trees
layout systems
```

## 32.2 Registered presentation wording

The statement that storefront generation derives from `registered presentation` is superseded.

Storefront generation derives from:

```text
authoritative configuration/projections/Exposure
+
bounded presentation capabilities
+
merchant-specific composition
```

## 32.3 AI presentation assistance

Website AI MAY assist merchant-specific composition.

It SHALL NOT classify the merchant into a finite presentation profile/theme/template family.

Existing rules concerning merchant intent, business-fact invention, configuration changes and non-blocking website suggestions remain unchanged.

---

# 33. MS-PROT-093 Impact

No amendment to MS-PROT-093 is required.

MS-PROT-093 already establishes:

```text
shared business correctness
shared design intent
platform-local implementation freedom
platform-appropriate operation
no cross-platform pixel parity requirement
```

MS-PROT-094 relies on and preserves that authority.

Creating a duplicate environment-optimisation amendment would add unnecessary overlapping authority.

---

# 34. Non-Authoritative Corpus Consequences

After explicit approval and repository formalisation, corpus conformance is expected to identify non-authoritative product/implementation documentation that still refers to:

```text
presentation profiles
registered presentation options
theme/profile selection
```

Such documents SHALL be updated only as downstream conformance consequences of the approved authority.

They SHALL NOT define or alter the approved semantic/presentation boundary.

Known likely examples include website-generation and guiding-principle PRD material.

---

# 35. Deferred Implementation Details

The following are intentionally downstream because current semantic/presentation correctness does not depend on selecting them now:

```text
exact Storefront Composition Revision serialization
database/storage technology
exact presentation primitive library
CSS/rendering framework
exact responsive-layout algorithms
exact AI model
exact composition prompting strategy
exact visual-regression tooling
exact quantitative diversity metric
exact canary percentage
exact rollout cohort size
exact historical composition retention period beyond required rollback needs
```

These decisions MUST conform to this authority.

They MUST return to governed design if implementation evidence shows that choosing them would require:

```text
business authority transfer
template/profile architecture
merchant-specific application codebases
a universal semantic UI DSL
live AI as ordinary serving dependency
```

No unresolved material question blocks the architecture established here.

---

# 36. Conformance Conditions

An implementation conforms only if all applicable conditions hold.

1. Merchant clients remain recognisably Main Street while adapting to their execution environments.
2. Merchant business category does not create aesthetic client forks.
3. Customer-facing websites are not selected from templates/themes/styles/profiles.
4. Same-domain merchants can receive materially different compositions without domain-specific presentation code.
5. A Customer-Facing Website owns no authoritative business state.
6. Mutable business values are obtained through accepted projections/contracts rather than copied into composition authority.
7. Customer commitments return to owning application operations.
8. One shared storefront runtime can serve materially different merchant compositions.
9. Adding a merchant does not require a merchant-specific codebase.
10. Existing valid websites remain serviceable without AI.
11. Material recomposition is revisioned and does not overwrite the current valid composition before validation.
12. Material engine evolution supports controlled rollout and rollback.
13. Engine improvements can benefit many compatible websites without individual manual edits.
14. AI cannot conceal a finite template/profile selection mechanism.
15. Presentation primitives remain presentation mechanisms rather than business-semantic owners.
16. The composition mechanism does not become a universal business/UI DSL.
17. Business-domain growth does not require proportional template/style growth.
18. Website redesign/recomposition does not require migration of merchant operational data.

---

# 37. Acceptance Effect

This accepted authority establishes:

> **Main Street standardises and environment-optimises the operational software merchants use, while customer-facing merchant websites remain merchant-specific compositions over one centrally evolving website platform.**

> **Customer-facing websites own no authoritative business data. Their compositions describe presentation only; current business truth remains with accepted Main Street capability owners.**

> **Main Street may improve or materially rearrange large populations of websites through shared rendering and composition-engine evolution without creating merchant-specific codebases or requiring routine merchant website administration.**

> **That central evolution must remain versioned, validated and roll-backable, and it must never collapse into templates, reusable site styles, domain layouts, hidden presentation profiles or semantic authority in the presentation layer.**

Canonical summary:

```text
MERCHANT SOFTWARE
    converge
    +
    optimise for environment

CUSTOMER-FACING WEBSITES
    compose
    +
    diverge visually
    +
    own no business truth

SHARED STOREFRONT ENGINES
    evolve centrally
    +
    improve many sites
    +
    avoid per-site maintenance
```

**VISION CONFORMANCE:** `VISION-CONFORMING WITH JUSTIFIED COMPLEXITY`

**RECOMMENDATION:** `ACCEPT`

## Status

**ACCEPTED after explicit manual approval on 13 September 2026.**
