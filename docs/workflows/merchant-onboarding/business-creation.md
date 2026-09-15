# Merchant Onboarding — 02. Business Creation

> **Workflow ID:** MO-02
> **Module:** Merchant Onboarding
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow creates a new business under an existing merchant account.

It establishes the merchant's business identity within Main Street and becomes the foundation for all subsequent onboarding activities.

A merchant account may exist without a business, but a business cannot exist without a merchant account.

---

# 2. Objective

Enable merchants to create their business with minimal effort while collecting only the information required to identify and initialise the business.

The workflow should prepare the platform for business configuration without overwhelming the merchant.

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
* Merchant is authenticated.
* Merchant has not already created the business being registered.
* Platform services are available.

Email verification is not required at this stage.

---

# 5. Trigger

The workflow begins immediately after account registration or whenever a merchant without a business selects **Create Business**.

---

# 6. Inputs

Required:

* Business Name
* Business Category (Business Niche)
* Country
* Business Address

Optional:

* Business Phone Number

The business address is required because Main Street only supports legitimate local businesses with a physical presence.

---

# 7. Workflow

### Step 1

Merchant selects **Create Business**.

---

### Step 2

Platform displays the Business Creation form.

---

### Step 3

Merchant enters the required business information.

---

### Step 4

Platform validates:

* Required fields
* Business name
* Address completeness
* Supported country

---

### Step 5

Business record is created.

The platform assigns:

* Business ID
* Initial onboarding status
* Default business settings

---

### Step 6

The AI Assistant analyses the selected business category to prepare for later onboarding steps.

No decisions are made at this stage.

---

### Step 7

Merchant proceeds to Business Profile Setup.

---

# 8. Alternative Flows

## Validation Failure

Invalid fields are highlighted.

Previously entered information remains on screen.

---

## Duplicate Business Detection

If an existing business appears to match the submitted information, the platform flags it for review.

The merchant may continue, but Business Verification will determine whether the business is legitimate.

---

# 9. Business Rules

* Every business belongs to exactly one merchant owner.
* A business must have a physical operating address.
* Business creation does not publish the business.
* Business creation does not verify the business.
* Business creation does not create a Google Business Profile.

---

# 10. AI Responsibilities

The AI Assistant may:

* Explain each field.
* Clarify business category selection.
* Help merchants understand the next onboarding steps.

The AI Assistant shall not:

* Invent business information.
* Automatically classify the business.
* Skip required validation.

---

# 11. Platform Responsibilities

The platform shall:

* Validate submitted information.
* Create the business record.
* Assign a Business ID.
* Initialise onboarding progress.
* Record audit events.

---

# 12. Security Considerations

* Only authenticated merchants may create businesses.
* All submitted information shall be validated.
* Audit logs shall record business creation.

---

# 13. Success Criteria

The workflow is successful when:

* A business record has been created.
* The business is linked to the merchant account.
* Onboarding progress advances to Business Profile Setup.

The business remains private and unavailable to the public.

---

# 14. Postconditions

The platform now contains:

* Merchant account
* Business record
* Initial onboarding state

No website, Google Business Profile, public listing or customer access exists yet.

---

# 15. Related Workflows

Previous:

* MO-01 Account Registration

Next:

* MO-03 Business Profile Setup

Future Dependencies:

* Commerce Profile Determination
* Business Verification
* Website Generation
* Go Live

---

# 16. Notes

Business creation establishes the legal and operational identity of the merchant's business.

It intentionally avoids collecting detailed operational information, which will be completed during the following onboarding workflows.
