# Merchant Onboarding — 03. Business Profile Setup

> **Workflow ID:** MO-03
> **Module:** Merchant Onboarding
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow captures the core identity of a merchant's business.

It establishes the information required for Main Street to understand the business, generate its digital presence, determine its commerce model and prepare subsequent onboarding workflows.

Business Profile Setup describes the business.

It does not configure how the business operates.

---

# 2. Objective

Enable merchants to provide accurate and complete information about their business while keeping the onboarding experience simple and intuitive.

The collected information becomes the authoritative business profile used throughout the platform.

---

# 3. Actors

## Primary Actor

* Merchant

## Supporting Actors

* Main Street Platform
* AI Assistant

---

# 4. Preconditions

* Merchant account exists.
* Business has been registered.
* Merchant is authenticated.

---

# 5. Trigger

The workflow begins immediately after successful Business Registration.

---

# 6. Information Collected

## Business Identity

* Business Name
* Trading Name (optional)
* Business Category (Business Niche)

---

## Business Description

Merchant provides a short description of the business.

Example:

> "We provide hair styling and colouring services."

The AI Assistant enhances this description into a professional business profile while preserving the merchant's intended meaning.

The merchant always reviews and approves the final version.

---

## Contact Information

* Primary Telephone Number
* Business Email Address
* Website Contact Email (optional)

---

## Location

* Business Address
* Postcode
* Country

Location information is used for verification, customer discovery and Google Business Profile integration.

---

## Branding

* Business Logo
* Cover Image (optional)

Brand colours, typography and layout are selected automatically by the Website Generation Engine.

Merchants are not required to design their websites.

---

# 7. Workflow

### Step 1

Platform displays the Business Profile form.

---

### Step 2

Merchant enters the required information.

---

### Step 3

AI Assistant analyses the submitted description.

---

### Step 4

AI generates an enhanced professional description.

The generated description:

* maintains factual accuracy,
* improves clarity,
* improves readability,
* improves professionalism.

The AI shall not invent services, products or claims.

---

### Step 5

Merchant reviews the enhanced description.

The merchant may:

* Accept
* Edit
* Replace

The merchant has final editorial control.

---

### Step 6

Platform validates all submitted information.

---

### Step 7

Business profile is saved.

---

### Step 8

Merchant proceeds to Commerce Profile Determination.

---

# 8. Alternative Flows

## AI Unavailable

If AI services are unavailable:

* Merchant's original description is stored.
* The enhancement step may be completed later.

---

## Merchant Rejects AI Suggestion

Merchant edits or replaces the generated description.

The merchant's version becomes the authoritative business description.

---

## Validation Failure

Invalid fields are highlighted.

Previously entered information remains available.

---

# 9. Business Rules

* Every business shall have exactly one primary business profile.
* Business descriptions must accurately represent the business.
* AI-generated content requires merchant approval before publication.
* Main Street shall not publish unapproved AI-generated content.
* Branding shall follow the platform's zero-customisation principles.

---

# 10. AI Responsibilities

The AI Assistant may:

* Improve grammar.
* Improve readability.
* Improve professionalism.
* Expand concise descriptions.
* Generate SEO-friendly wording.

The AI Assistant shall not:

* Invent business offerings.
* Invent qualifications.
* Invent awards.
* Invent certifications.
* Change the intended meaning.
* Publish content without merchant approval.

---

# 11. Platform Responsibilities

The platform shall:

* Validate profile information.
* Store approved content.
* Preserve version history where applicable.
* Prepare profile data for downstream services.

---

# 12. Success Criteria

The workflow is successful when:

* Business profile information has been completed.
* The business description has been approved.
* The profile is stored successfully.
* The platform can proceed to Commerce Profile Determination.

---

# 13. Postconditions

The platform now has sufficient business information to:

* Generate a professional website.
* Prepare SEO content.
* Configure Google Business Profile integration.
* Determine the appropriate commerce model.
* Continue onboarding.

---

# 14. Related Workflows

Previous:

* MO-02 Business Registration

Next:

* MO-04 Commerce Profile Determination

Future Dependencies:

* Website Generation
* SEO Service
* Verification Engine
* Google Business Integration
* Unified POS

---

# 15. Notes

Business Profile Setup defines the public identity of the merchant's business.

It is the primary source of business information for all customer-facing experiences and downstream platform services.

The merchant retains complete editorial ownership of all published business information.
