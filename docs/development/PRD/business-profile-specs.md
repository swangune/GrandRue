# **16. Business Profile & Public Presence Specification**

**Revision:** 13 September 2026  
**Status:** Current product intent  
**Design authority:** composite MS-PROT-027, composite MS-PROT-036, composite MS-PROT-049, composite MS-PROT-050, composite MS-PROT-051, composite MS-PROT-052, MS-PROT-066, MS-PROT-088, MS-PROT-094 and applicable capability authorities

## **16.1 Purpose**

The Business Profile is a bounded part of Main Street's merchant/public-presence model.

It is **not** a universal single source of truth for the business and is **not** the operational core of Main Street.

The Business Profile owns only the merchant/profile/public-information facts assigned to it by accepted authority. Products, services, inventory, availability, bookings, orders, payments, staff authority, scheduling and other operational facts remain owned by their applicable capabilities.

Main Street composes customer-facing websites and other authorised surfaces from those separately owned facts through Projection/Exposure and application contracts.

Canonical:

```text
Profile-owned public facts
+
capability-owned operational facts
+
merchant configuration
+
merchant content/media
        ↓
authorised projections / Exposure
        ↓
customer-facing surfaces
```

The merchant manages the business and its public information. Main Street coordinates the resulting digital presentation without duplicating business truth into website-specific data.

---

## **16.2 Design Philosophy**

The Business Profile should answer bounded public-presence questions such as:

> **Who is this merchant, where/how can customers find or contact it, and what merchant-owned public information should be presented?**

It should not attempt to answer every operational question about the business.

Merchants should not need to edit webpages or understand presentation infrastructure.

Customer-facing websites are composed from authorised business/public material rather than maintained as separate data stores or manually edited page structures.

---

## **16.3 Core Principles**

### Bounded ownership

Each business fact has one accepted semantic owner.

The Business Profile does not duplicate facts already owned by Product, Service/Offering, Scheduling, Booking, Order, Payment, Inventory, Workforce or other capabilities.

### Structured public information

Profile-owned information should remain structured where accepted authority requires structured representation so that projections, search representations, AI assistance and delivery surfaces can consume it safely.

### Projection-driven synchronisation

When an authoritative fact changes, affected customer-facing representations should refresh from the owning authority/projection rather than require duplicate manual edits.

### Mobile-first merchant administration

Ordinary public-profile maintenance should remain operable from a smartphone.

### Progressive completion

Merchants should provide only information relevant to their current business and intended public presence.

Optional public content gaps should produce useful guidance rather than unrelated business-operation blockers.

---

## **16.4 Public-Presence Inputs**

The exact authoritative fields remain governed by their owning authorities.

Profile/public-presence inputs may include, where applicable and accepted:

- merchant display identity;
- public descriptions and merchant-authored copy;
- merchant classifications used for description/discovery rather than executable business behaviour;
- contact points;
- merchant locations and service-area presentation;
- public business-hours projections;
- merchant brand inputs;
- media references;
- public trust/verification representations where separately authorised;
- website/domain identity; and
- other public/profile facts explicitly assigned to Profile/Presence authority.

This list does not transfer ownership from another capability merely because a fact appears publicly.

---

## **16.5 Merchant Identity**

Merchant identity/presence may include business-facing information such as the public business name and merchant-approved descriptive information where governed by the accepted Profile/Presence authority.

Business classification may assist description, discovery or onboarding context.

Business classification must not create business-domain runtime branches, dashboard applications, website templates, themes or styles.

---

## **16.6 Brand Inputs**

Brand inputs allow the merchant's public identity to influence customer-facing composition without turning Main Street into a manual website builder.

Supported inputs may include, where governed and available:

### Logo

Merchant-approved logo/media reference.

### Brand colour or other brand characteristics

Supported merchant brand characteristics may inform composition subject to accessibility requirements.

### Imagery and media

Merchant-owned/approved media may be used throughout the customer-facing composition through the accepted media lifecycle.

### Typography preferences

Supported typography preferences may be accepted where the presentation system supports them.

These are **composition inputs**, not a template customisation layer.

Main Street does not require the merchant to choose a website template, theme, named style, domain layout, component tree or responsive breakpoint system.

---

## **16.7 Customer-Facing Website Relationship**

The Business Profile does not equal the website.

Customer-facing website composition may consume:

```text
Profile/public-presence projections
+
Offering/Product/Service projections
+
Publication projections
+
current authorised operational projections
+
customer interaction entry points
+
merchant content/media
+
merchant brand inputs
+
Storefront Composition Revision
```

The website owns none of those business facts.

The Storefront Composition Revision owns presentation arrangement only.

The shared storefront engines may improve or recompose merchant websites centrally without migrating or duplicating the merchant's operational data.

---

## **16.8 Products, Services and Other Offerings**

Products, Services/Offerings and their prices, availability, variants or other operational properties remain owned by their accepted capability authorities.

The Business Profile or website may present authorised projections of those facts but must not become a duplicate editable source.

A merchant changing a capability-owned value should not have to repeat the same change in a separate website/profile copy.

---

## **16.9 Contact, Location and Service Area**

Merchant contact points, locations and service areas must follow their accepted authoritative representations and exposure decisions.

Presentation may format those facts appropriately for customers.

Presentation must not create new location identity, service eligibility or business rules from display text or map treatment.

---

## **16.10 Business Hours**

Business Hours remain governed by their accepted time/operating-hours authority.

Customer-facing surfaces consume the applicable authorised public projection.

A website display of hours does not become an independently editable source of operating-time truth.

---

## **16.11 Staff and People Presentation**

Where merchant staff/person information is legitimately exposed to customers, the presentation consumes authorised projections from the applicable owning authorities.

The Business Profile does not acquire workforce membership, role, scheduling or staff-authorisation ownership merely because selected information is shown publicly.

---

## **16.12 Publications and Announcements**

Publications/Announcements remain owned by their accepted publication authority.

The customer-facing website may compose authorised publication content and interaction entry points without duplicating publication semantics into the Profile.

---

## **16.13 Customer Operations**

Capabilities such as:

- ordering;
- booking/appointments;
- enquiry/messaging;
- payment;
- delivery/collection; and
- other supported customer operations

remain governed by their owning application/capability contracts.

The website exposes only applicable and authorised customer interactions.

A visible button or form does not create authority to execute the underlying operation.

---

## **16.14 Search and Discovery**

Main Street may derive search-oriented representations such as metadata, structured data, canonical information, sitemaps and local-discovery material from authorised public facts.

Search representations are derived.

External search/discovery provider state does not become Main Street business authority.

---

## **16.15 AI Assistance**

AI may help merchants:

- draft or improve public copy;
- organise merchant-owned content;
- identify useful public-information gaps;
- improve supported media;
- explain public-presence requirements; and
- propose merchant-specific website composition.

AI must not invent merchant facts, convert business category into executable behaviour, select a hidden finite website-template/style family, or mutate another capability's authoritative state merely to improve public presentation.

Material inferred operating intent must follow the applicable reconfiguration/capability process.

---

## **16.16 Public-Presence Completeness**

Main Street should not represent public-presence completeness as completion of every global platform field.

Guidance should be contextual to the merchant's active business model and intended public/customer surfaces.

Useful guidance may identify missing merchant content or public facts without blocking unrelated merchant operations.

A genuine capability-owned requirement may still block the specific operation it governs.

---

## **16.17 Synchronisation and Maintenance**

Main Street should minimise duplicate entry and reconciliation.

When an authoritative source changes:

```text
owner commits current truth
        ↓
projection / Exposure changes
        ↓
shared delivery surfaces present current authorised material
```

Ordinary authoritative-data changes should not require website recomposition solely because a displayed value changed.

Presentation arrangement changes may create a new Storefront Composition Revision under MS-PROT-094.

Main Street should be able to improve shared website rendering/composition infrastructure across many merchants without requiring merchant-specific code changes or manual website maintenance.

---

## **16.18 Business Profile Statement**

> **The Business Profile is a bounded authority for merchant/public-presence facts, not the business's universal operational record. Main Street combines Profile-owned public information with separately owned capability facts through authorised projections and interactions, then composes merchant-specific customer-facing websites without duplicating business truth into the website.**

---

### End of Section 16 – Business Profile & Public Presence Specification
