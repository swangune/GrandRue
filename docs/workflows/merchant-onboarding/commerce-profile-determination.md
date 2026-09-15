# Merchant Onboarding — 04. Commerce Profile Determination

> **Workflow ID:** MO-04
> **Module:** Merchant Onboarding
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow determines the operational commerce model of a business.

Using the merchant's selected business niche and a minimal set of contextual questions, Main Street automatically classifies the business into the appropriate commerce profile.

The commerce profile becomes the foundation for website generation, Unified POS configuration and customer interactions.

The merchant is never required to manually choose a commerce profile.

---

# 2. Objective

Automatically determine whether the business operates as:

* Product Business
* Service Business
* Hybrid Business

while keeping the onboarding experience simple and natural.

---

# 3. Actors

## Primary Actor

* Merchant

## Supporting Actors

* Main Street Platform
* AI Assistant
* Commerce Profile Engine

---

# 4. Preconditions

* Business Registration has been completed.
* Business Profile Setup has been completed.
* Business category (business niche) has been selected.

---

# 5. Trigger

The workflow begins immediately after Business Profile Setup.

---

# 6. Commerce Profiles

## Product Business

Businesses that primarily sell physical products.

Examples:

* Grocery Store
* Bakery
* Electronics Shop
* Florist
* Clothing Store

Unified POS Mode:

**Order**

---

## Service Business

Businesses that primarily provide services.

Examples:

* Hair Salon
* Auto Mechanic
* Dentist
* Solicitor
* Accountant

Unified POS Mode:

**Schedule**

---

## Hybrid Business

Businesses that provide services while also selling products.

Examples:

* Hair Salon selling beauty products
* Auto Mechanic selling spare parts
* Veterinary Clinic selling pet supplies
* Spa selling skincare products

Unified POS Mode:

**Hybrid**

---

# 7. Workflow

### Step 1

Merchant selects the business niche.

Example:

Hair Salon

---

### Step 2

Commerce Profile Engine loads the decision rules for the selected niche.

---

### Step 3

Platform asks only the additional questions required for that niche.

Example:

Hair Salon

Question:

> Do you sell beauty products to customers?

Merchant:

Yes

---

### Step 4

Commerce Profile Engine evaluates the responses.

---

### Step 5

Commerce profile is assigned automatically.

Example:

Hair Salon

*

Beauty Products

↓

Hybrid Business

---

### Step 6

Platform stores the commerce profile.

---

### Step 7

Merchant proceeds to Business Verification.

---

# 8. Example Decision Trees

## Hair Salon

```text id="n0t1hf"
Hair Salon

↓

Sell beauty products?

Yes

↓

Hybrid

No

↓

Service
```

---

## Auto Mechanic

```text id="g4cqtx"
Auto Mechanic

↓

Sell spare parts?

Yes

↓

Hybrid

No

↓

Service
```

---

## Bakery

```text id="h7wypn"
Bakery

↓

Offer table reservations?

Yes

↓

Hybrid

No

↓

Product
```

---

## Photographer

```text id="lcn4jq"
Photographer

↓

Sell printed albums or frames?

Yes

↓

Hybrid

No

↓

Service
```

---

# 9. Business Rules

* Merchants never manually select Product, Service or Hybrid.
* Commerce profiles are determined solely by the Commerce Profile Engine.
* Questions presented shall be specific to the selected business niche.
* Only the minimum number of questions necessary shall be asked.
* Commerce profiles may be recalculated if the business model changes.

---

# 10. AI Responsibilities

The AI Assistant may:

* Explain questions.
* Clarify business terminology.
* Help merchants understand the implications of their answers.

The AI Assistant shall not override the Commerce Profile Engine.

---

# 11. Commerce Profile Engine Responsibilities

The Commerce Profile Engine shall:

* Maintain decision rules for supported business niches.
* Present contextual questions.
* Evaluate merchant responses.
* Assign the correct commerce profile.
* Configure downstream platform services.

---

# 12. Platform Responsibilities

Following profile determination, the platform shall automatically prepare:

* Unified POS mode.
* Website structure.
* Navigation.
* Customer ordering capability.
* Appointment scheduling capability.
* Inventory requirements.
* Operational workflows.

No manual configuration is required from the merchant.

---

# 13. Success Criteria

The workflow is successful when:

* The business has been assigned a commerce profile.
* The profile has been stored.
* Platform configuration can continue automatically.

---

# 14. Postconditions

The platform now understands how the business operates.

Subsequent services—including Website Generation, Unified POS, customer journeys and operational workflows—will be configured using this commerce profile.

---

# 15. Related Workflows

Previous:

* MO-03 Business Profile Setup

Next:

* MO-05 Business Verification

Future Dependencies:

* Website Generation
* Unified POS
* Customer Journey
* Inventory
* Scheduling
* Orders

---

# 16. Notes

Commerce Profile Determination is an internal platform capability.

Merchants are never exposed to technical implementation concepts such as Product, Service or Hybrid modes.

Instead, Main Street learns how the business operates through natural business questions and configures the platform automatically, reducing onboarding complexity while ensuring an accurate operational model.
