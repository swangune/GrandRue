# Merchant Onboarding — 06. Website Generation

> **Workflow ID:** MO-06
> **Module:** Merchant Onboarding
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow automatically generates a complete, production-ready website for the merchant using verified business information collected during onboarding.

The merchant does not build the website manually.

Instead, Main Street assembles a professional website using its Website Generation Service, Design System and Controlled Customisation Architecture.

The generated website becomes the merchant's primary digital storefront.

---

# 2. Objective

Generate a complete website that:

* accurately represents the merchant,
* reflects the merchant's commerce model,
* follows Main Street's design standards,
* requires no web design knowledge,
* is immediately ready for publication.

---

# 3. Actors

## Primary Actor

* Main Street Platform

## Supporting Actors

* Website Generation Service
* Theme & Personalisation Engine
* Commerce Profile Engine
* Branding Engine
* SEO Service
* AI Operations Layer

---

# 4. Preconditions

The following must exist:

* Main Street verification completed.
* Business profile approved.
* Commerce profile assigned.
* Branding assets available.
* Business description approved.
* Contact information completed.

Google Business Profile integration is not required.

---

# 5. Trigger

The workflow begins immediately after successful Main Street verification.

---

# 6. Inputs

The Website Generation Service consumes:

* Business profile.
* Commerce profile.
* Business description.
* Contact information.
* Business category.
* Branding assets.
* Business location.
* Platform configuration.

No further merchant input is required.

---

# 7. Controlled Customisation

Main Street follows a Controlled Customisation Architecture.

The merchant customises approved business information.

The platform controls website architecture.

---

## Merchant Controlled

Examples include:

* Business name.
* Logo.
* Cover image.
* Business description.
* Products.
* Services.
* Announcements.
* Opening hours.
* Contact information.
* Social media links.

---

## Platform Controlled

The platform automatically determines:

* Page layouts.
* Navigation.
* Typography.
* Component placement.
* Responsive behaviour.
* Accessibility.
* Performance optimisation.
* SEO implementation.
* Unified POS integration.

---

# 8. Workflow

### Step 1

Platform invokes the Website Generation Service.

---

### Step 2

Business information is retrieved.

---

### Step 3

Commerce Profile Engine determines website behaviour.

Examples:

Product Business

↓

Order-first website

---

Service Business

↓

Booking-first website

---

Hybrid Business

↓

Integrated commerce experience

---

### Step 4

Theme & Personalisation Engine selects the most appropriate presentation.

Selection considers:

* Business niche.
* Branding.
* Commerce profile.
* Content structure.
* Platform design rules.

No manual template selection is required.

---

### Step 5

Branding Engine applies visual identity.

Examples include:

* Colour palette.
* Logo.
* Cover image.
* Brand accents.
* Component styling.

---

### Step 6

AI Operations Layer generates supporting content where appropriate.

Examples:

* Welcome message.
* Homepage introduction.
* Section summaries.
* Calls to action.
* SEO-friendly headings.

The AI shall never invent factual business information.

---

### Step 7

SEO Service prepares:

* Page titles.
* Meta descriptions.
* Structured data.
* Open Graph metadata.
* XML sitemap.
* Canonical URLs.

---

### Step 8

Unified POS components are automatically embedded.

Examples:

* Ordering.
* Booking.
* Hybrid commerce.
* Customer messaging.

Only the required components are included.

---

### Step 9

Website Generation Service assembles the final website.

---

### Step 10

The completed website is stored and marked:

**Publication Ready**

The website remains private until the publication workflow.

---

# 9. Website Composition

Every website is assembled from reusable platform components.

Typical components include:

* Hero section.
* Business overview.
* Products.
* Services.
* Booking.
* Ordering.
* Announcements.
* Contact.
* Business information.
* Customer support.
* Footer.

Components are selected automatically according to the commerce profile.

---

# 10. Business Rules

* Websites shall be generated automatically.
* Controlled Customisation shall be enforced.
* Website architecture shall remain platform-controlled.
* Commerce profile determines functional components.
* Every generated website shall conform to the Main Street Design System.

---

# 11. AI Responsibilities

AI may:

* improve readability,
* generate supporting marketing copy,
* improve customer engagement,
* optimise headings,
* prepare SEO-friendly wording.

AI shall not:

* invent business information,
* change approved facts,
* publish content without merchant approval where approval is required.

---

# 12. Platform Responsibilities

The platform shall:

* assemble website components,
* configure navigation,
* integrate Unified POS,
* prepare SEO,
* optimise performance,
* store the generated website,
* mark the website as Publication Ready.

---

# 13. Success Criteria

The workflow is successful when:

* a complete website has been generated,
* branding has been applied,
* commerce functionality has been configured,
* SEO assets have been prepared,
* the website is marked Publication Ready.

---

# 14. Postconditions

The platform now contains a complete website ready for publication.

The website becomes the merchant's primary digital presence.

Publication occurs during a later onboarding workflow.

---

# 15. Related Workflows

Previous:

* MO-05 Business Verification

Next:

* MO-07 Business Readiness Assessment

Platform Services:

* Website Generation Service
* Theme & Personalisation Engine
* Branding Engine
* Commerce Profile Engine
* SEO Service

---

# 16. Notes

Website Generation is a fully automated platform capability.

Rather than providing a website builder, Main Street generates professionally designed websites using reusable components, controlled customisation and intelligent service orchestration.

This approach enables every merchant to receive a high-quality digital storefront while allowing the platform to evolve centrally without requiring merchants to redesign or rebuild their websites.
