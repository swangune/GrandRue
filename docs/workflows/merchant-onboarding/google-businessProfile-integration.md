# Merchant Onboarding — 07. Google Business Profile Integration

> **Workflow ID:** MO-07
> **Module:** Merchant Onboarding
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow synchronises the merchant's verified business with Google Business Profile.

Its purpose is to strengthen the merchant's online visibility by linking or creating a Google Business Profile while ensuring consistency between Main Street and Google.

Google Business Profile Integration is an external platform integration.

It is not required for Main Street to generate or operate a merchant website.

---

# 2. Objectives

The workflow shall:

* Connect existing verified Google Business Profiles.
* Support creation of new Google Business Profiles where applicable.
* Synchronise approved business information.
* Update the business website URL.
* Improve discoverability on Google Search and Google Maps.

---

# 3. Actors

## Primary Actor

* Main Street Platform

## Supporting Actors

* Google Business Integration Service
* Google Business Profile
* Verification Engine
* Merchant

---

# 4. Preconditions

The following shall exist:

* Verified Main Street business.
* Generated website.
* Approved business profile.
* Valid business address.
* Merchant authorisation where required.

---

# 5. Trigger

The workflow begins immediately after Website Generation.

---

# 6. Integration Paths

The Google Business Integration Service automatically selects one of two integration paths.

## Path A

Existing Google Business Profile

The merchant already owns or manages a verified Google Business Profile.

---

## Path B

New Google Business Profile

The merchant does not currently have a Google Business Profile.

---

# 7. Workflow

## Path A – Existing Google Business Profile

### Step 1

Merchant authorises Main Street to access their Google Business Profile.

---

### Step 2

Google ownership is validated.

---

### Step 3

Business identity is compared.

The integration service validates consistency between:

* Business name.
* Address.
* Category.
* Geographic location.

---

### Step 4

The generated Main Street website URL is synchronised with Google Business Profile.

---

### Step 5

Business information is synchronised where appropriate.

Examples include:

* Opening hours.
* Telephone number.
* Website URL.
* Business category.

---

### Step 6

Integration status is updated.

---

## Path B – New Google Business Profile

### Step 1

Merchant grants the required authorisation.

---

### Step 2

The Google Business Integration Service prepares the required business information.

---

### Step 3

Verification evidence previously approved by Main Street is prepared for submission where supported.

---

### Step 4

Google Business Profile creation is initiated.

---

### Step 5

Google verification proceeds asynchronously.

The merchant's Main Street website remains operational regardless of Google's review timeline.

---

### Step 6

Once Google verification is complete, synchronisation continues automatically.

---

# 8. Synchronised Information

Depending on Google's capabilities and merchant permissions, synchronised information may include:

* Business name.
* Business description.
* Business category.
* Address.
* Telephone number.
* Opening hours.
* Website URL.
* Logo.
* Cover image.

Main Street remains the authoritative source for synchronised business information unless the merchant explicitly edits their Google Business Profile independently.

---

# 9. Synchronisation Principles

* Main Street and Google remain independent systems.
* Main Street does not rely on Google for website operation.
* Google does not determine Main Street publication status.
* Synchronisation is incremental.
* Failed synchronisation shall not interrupt merchant operations.

---

# 10. Platform Responsibilities

The platform shall:

* Authenticate Google access.
* Synchronise approved information.
* Detect synchronisation failures.
* Retry where appropriate.
* Record integration history.
* Notify merchants of significant events.

---

# 11. Business Rules

* Google Business Profile integration is optional where permitted by platform policy.
* Merchant consent is required before accessing Google accounts.
* Only verified businesses may initiate Google Business Profile integration.
* Synchronisation shall never overwrite merchant information without authorisation.
* Integration failures shall not disable the Main Street website.

---

# 12. Success Criteria

The workflow is successful when:

For existing Google Business Profiles:

* Business successfully linked.
* Website URL synchronised.
* Business information updated.

For new Google Business Profiles:

* Profile creation initiated.
* Verification submitted where supported.
* Future synchronisation scheduled.

---

# 13. Postconditions

The business has:

* A synchronised Google Business Profile or an active integration process.
* An operational Main Street website.
* Improved potential discoverability across Google services.

---

# 14. Related Workflows

Previous:

* MO-06 Website Generation

Next:

* MO-08 Business Readiness Assessment

Platform Services:

* Google Business Integration Service
* Verification Engine
* Website Generation Service

---

# 15. Notes

Google Business Profile Integration extends the merchant's digital presence beyond Main Street.

It is designed as a resilient, asynchronous integration that enhances visibility without introducing operational dependencies.

The merchant's Main Street website remains the primary digital storefront, while Google Business Profile acts as an external discovery channel that directs customers to the business.
