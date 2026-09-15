# **17. Website Delivery Surface & Generation Engine**

**Revision:** 13 September 2026  
**Status:** Current product intent  
**Design authority:** MS-PROT-027, MS-PROT-029, composite MS-PROT-036, composite MS-PROT-049, composite MS-PROT-057, MS-PROT-094 and applicable capability authorities

## **17.1 Purpose**

The Website Generation Engine is the delivery mechanism that composes a merchant's public website/storefront from approved configuration, authorised projections, interaction contributions, merchant content/media, merchant brand inputs and merchant-specific presentation composition.

It is **not** the Main Street product core.

It must be treated as one replaceable delivery surface over the merchant operational model.

```text
authoritative capability state
        +
approved merchant configuration
        +
public/customer Exposure
        +
merchant content/media references
        +
merchant brand inputs
        +
website constraints
        ↓
merchant-specific composition
        ↓
Storefront Composition Revision
        ↓
shared storefront rendering runtime
```

The website is generated from business truth. It does not create business truth.

---

## **17.2 Design Philosophy**

The governing principle is:

> **Businesses manage business information and operations; Main Street composes and renders the appropriate website from the authorised operational model.**

The former shorthand "The Business Profile is the website" is no longer sufficient and must not be used as an architectural rule.

The Business Profile contributes bounded merchant/profile facts. Products, services, publications, availability, bookings, ordering, payment and other operational concepts remain owned by their accepted capability authorities.

The website's visual character is a presentation outcome. Main Street does not select a reusable website template, theme, named style, domain layout or Presentation Profile as the governing generation mechanism.

---

## **17.3 Product Boundary**

Website generation is valuable because it removes design and technical work from merchants, but website generation alone is not treated as a defensible product moat.

Main Street's durable value sits beneath the website in:

- current authoritative merchant context;
- validated configuration;
- capability-owned operations;
- Exposure and projection rules;
- customer and staff relationships;
- transaction and audit evidence;
- provider integration; and
- operational continuity.

Accordingly, future improvements to website generation must not pull semantic or transaction authority into the frontend, composition engine or composition revision.

Customer-facing websites own no authoritative business data. Any browser state, generated HTML, CDN/static material, search representation or client cache remains derived and non-authoritative.

---

## **17.4 Core Objectives**

The Website Generation Engine shall:

- compose professional public/customer surfaces automatically;
- expose only applicable and authorised information/interactions;
- produce merchant-specific compositions rather than select templates/themes/styles/profiles;
- remain responsive, accessible, performant and secure;
- update displayed business information as relevant authoritative state/projections change without requiring recomposition solely because a business value changed;
- support controlled merchant branding/presentation inputs;
- support SEO/local discovery where applicable;
- remain replaceable without changing merchant business semantics; and
- allow shared storefront-engine improvements to benefit large numbers of existing merchant websites without merchant-specific engineering maintenance.

It shall not:

- define merchant capabilities;
- own operational state;
- decide concurrency-sensitive availability or commitments from cached data;
- duplicate capability-owned business rules;
- create a merchant-specific application/codebase per website;
- select a hidden finite website template/style family;
- generate arbitrary executable business logic per merchant; or
- make website completeness a universal merchant-operability gate.

---

## **17.5 Composition Workflow**

Conceptually, storefront composition follows this sequence:

### Step 1 — Resolve Active Merchant Context

Use the approved merchant configuration and the active serving/release context required by the accepted architecture.

### Step 2 — Resolve Applicable Public/Customer Contributions

Determine which registered capability-owned public information and interaction entry points are applicable.

### Step 3 — Obtain Authorised Projections

Read the relevant authorised projections/material through accepted Projection/Exposure contracts.

Do not reacquire or infer hidden authoritative state in the frontend.

### Step 4 — Compose Merchant-Specific Presentation

Use merchant content/media references, merchant brand inputs, available customer actions, bounded presentation capabilities and website structural/accessibility constraints to produce a merchant-specific Storefront Composition Revision.

Do not select a reusable template, theme, named style, business-domain layout, Presentation Profile or hidden finite site archetype.

Business category may provide descriptive context but must not act as presentation authority.

### Step 5 — Resolve Website Structure

Create only routes/sections justified by the active merchant context and the merchant-specific composition.

Examples may include:

- home/business summary;
- products;
- services;
- publications/opportunities;
- portfolio/gallery;
- contact/enquiry;
- booking interaction;
- ordering interaction;
- subscription; and
- location/opening-hours information.

The examples are possible customer-facing responsibilities, not a page-template catalogue.

### Step 6 — Apply Media, Branding and Presentation Relationships

Apply authorised logo/media/content references and supported merchant brand/presentation inputs through the Storefront Composition Revision.

Visual character emerges from the composition rather than from selection of a reusable named style.

### Step 7 — Optimise the Delivery Surface

Apply responsive rendering, accessibility, performance, security and search-engine requirements through shared storefront infrastructure.

### Step 8 — Publish/Refresh the Surface

Publish the validated composition revision and/or refresh affected derived surface material as required.

This activity remains presentation/read-model work. It does not commit business transactions.

---

## **17.6 Website Structure**

There is no requirement that every merchant have the same pages or visual structure.

The website structure derives from active merchant configuration, applicable public/customer contributions and merchant-specific composition.

A service merchant may expose services, enquiry and booking.

A product merchant may expose catalogue and ordering.

An information publisher may expose opportunities/publications, categories, search, enquiry and subscription without commerce.

An online consultant may expose consultation information, enquiry and booking without physical-premises content.

These examples do not create domain layouts or reusable website templates.

Main Street must not generate empty or irrelevant sections merely to satisfy a global website shape.

Business-domain or capability similarity must not impose website-design similarity, although similar merchant-specific evidence may naturally lead to similar composition.

---

## **17.7 Interaction Components**

Customer-facing actions such as `Book`, `Order`, `Enquire`, `Subscribe` or equivalent must invoke supported Main Street application use cases.

```text
website component
      ↓
public/customer application contract
      ↓
authorisation / eligibility / validation
      ↓
capability-owned execution
      ↓
authoritative state
```

The frontend must not reimplement the business rule simply because AI or a component generator can produce the code.

The website collects and presents customer intent; the owning Main Street operation authoritatively executes or rejects that intent.

---

## **17.8 Dynamic Operational Information**

Values such as availability, booking opportunity, stock indication, current price or other operational information must be sourced through the accepted capability/projection contracts.

Cached or generated website material may improve performance but cannot authorise concurrency-sensitive commitments.

When a customer attempts a commitment, authoritative state is revalidated by the owning operation.

A change in an authoritative business value must not require website recomposition solely because the value changed. The rendering path should present the current authorised projection through the existing valid composition where the presentation relationship itself has not changed.

---

## **17.9 Merchant Presentation Inputs**

Supported merchant presentation inputs may include:

- logo;
- brand colour or other supported brand characteristics;
- imagery/media;
- merchant-authored or merchant-approved content;
- supported typography preferences where applicable; and
- desired public emphasis expressed through supported merchant-facing controls or ordinary business language.

These inputs feed merchant-specific composition. They do not customise a preselected template/theme/profile.

Layout mechanics, navigation behaviour, accessibility mechanisms, security controls and executable business logic remain governed by Main Street.

Merchants should not be asked to administer component trees, responsive breakpoints, composition graphs or rendering-engine versions.

---

## **17.10 AI Website Assistance**

AI may assist with:

- drafting or improving merchant-approved public content;
- identifying missing useful content;
- explaining what information is required for a surface;
- proposing merchant-specific website composition;
- media enhancement; and
- helping merchants express changes in ordinary business language.

AI may reason about presentation hierarchy, grouping, relative prominence, media emphasis, visual rhythm, responsive composition and customer-action prominence.

AI may not:

- invent merchant facts;
- infer and activate new operating behaviour merely to improve a website;
- create arbitrary frontend scripts or domain logic;
- treat a likely business capability as active without the required merchant/configuration process;
- publish material contrary to the applicable merchant-approval rule; or
- substitute template/theme/profile classification for merchant-specific composition.

If website assistance discovers possible new operating intent, it hands that intent to the appropriate reconfiguration/capability process.

An already valid published website must remain serviceable without live AI.

---

## **17.11 Website Completeness**

There is no universal percentage representing whether a merchant is "ready".

Website completeness is contextual to the applicable merchant semantics and intended public model.

Missing optional About copy, gallery images or another presentation enhancement must not block Orders, Staff operations, Bookings or other unrelated capabilities.

A genuine capability/application requirement may still block the specific operation it governs.

---

## **17.12 Responsive Design and Accessibility**

Every supported storefront should be mobile-first, responsive and accessible by default.

Merchants should not be asked to configure responsive breakpoints, semantic HTML, keyboard behaviour, accessibility mechanics or equivalent implementation details.

Stable website responsibilities such as site identity/navigation, principal content, applicable primary actions and terminal/footer information may have materially different visual treatments across merchant compositions while preserving logical structure, discoverability and accessibility.

---

## **17.13 Performance and Security**

The surface should use appropriate image/media optimisation, caching/CDN behaviour, code delivery optimisation, secure transport and platform security controls.

Performance optimisation must not weaken freshness, authority or security requirements.

Shared rendering-engine improvements may be applied centrally where compatible with published composition intent.

---

## **17.14 Central Evolution and Recomposition**

Main Street maintains shared storefront infrastructure rather than independently maintained merchant website applications.

A rendering-engine improvement may improve compatible existing websites without changing their Storefront Composition Revision where presentation intent remains valid.

A change that materially changes merchant-specific presentation arrangement must produce a new Storefront Composition Revision.

Material recomposition must be versioned and validated before replacing the current published composition. Failed candidate recomposition must leave the current valid composition in place.

Material estate-wide storefront changes should support representative-corpus testing, controlled rollout and rollback according to MS-PROT-094.

An engine or composition improvement must not require manual editing of each merchant website.

---

## **17.15 Search and Local Discovery**

Main Street may generate applicable metadata, structured data, sitemaps, canonical information and other search-oriented representations from authorised business/public material.

Search representations remain derived. Search-engine ranking or external platform state does not become Main Street business authority.

Physical-location/local-discovery information appears only where applicable and authorised.

---

## **17.16 Delivery-Surface Independence**

Main Street should be able to replace the website renderer, add another authorised customer channel or change frontend technology without changing the meaning of merchant operations.

The target dependency direction is:

```text
operational authority
      ↓
Projection / Exposure / application contracts
      ↓
Storefront Composition Revision
      ↓
shared rendering technology
```

Never:

```text
website implementation
      ↓
defines merchant semantics
```

---

## **17.17 Degradation Boundary**

A website rendering, composition-engine or CDN failure must not redefine merchant business state.

Where the backend and merchant operational surfaces remain healthy, merchants may continue supported operations even if a public website surface is degraded. Customer-facing behaviour during degradation follows the accepted resilience architecture.

AI failure must not deactivate an already valid published website.

---

## **17.18 Powered by Main Street**

Eligible plans may display a discreet `Powered by Main Street` attribution according to product/commercial policy.

The customer experience remains merchant-facing; attribution does not make Main Street a marketplace identity or commercial counterparty.

---

## **17.19 Website Delivery Surface Statement**

> **Main Street customer-facing websites are merchant-specific presentation-and-interaction compositions over capability-owned business authority. They own no authoritative business data, are delivered through shared centrally evolving storefront infrastructure, and can be improved or recomposed at scale without becoming templates, merchant-specific application codebases or sources of business truth.**

---

### End of Section 17 – Website Delivery Surface & Generation Engine
