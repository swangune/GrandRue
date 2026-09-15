# Merchant Onboarding — 08. Go Live

> **Workflow ID:** MO-08
> **Module:** Merchant Onboarding
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow publishes the merchant's business to the Main Street platform and officially transitions the business from onboarding to live operations.

Go Live is the final step of merchant onboarding.

Upon successful completion, the merchant's website becomes publicly accessible, customers can interact with the business, and operational services are activated.

---

# 2. Objective

Safely transition a verified, fully configured business into an active production state.

Publication shall occur as a coordinated platform event to ensure all customer-facing services become available simultaneously.

---

# 3. Actors

## Primary Actor

* Main Street Platform

## Supporting Actors

* Publication Service
* Website Service
* Unified POS Service
* Notification Service
* Search Service
* Analytics Service

---

# 4. Preconditions

The following must already exist:

* Business verified by Main Street.
* Website marked **Publication Ready**.
* Business Readiness Assessment completed successfully.
* Commerce profile assigned.
* Unified POS configured.

Google Business Profile integration is not required.

---

# 5. Trigger

The workflow begins automatically when the business achieves **Ready for Publication** status.

Where platform policy requires, the merchant may review and confirm publication before activation.

---

# 6. Publication Activities

The Publication Service performs a coordinated activation of the business.

Activities include:

* Publishing the merchant website.
* Activating customer access.
* Enabling Unified POS.
* Enabling online ordering where applicable.
* Enabling appointment booking where applicable.
* Activating customer messaging.
* Registering the business with Main Street Search.
* Enabling analytics collection.
* Initialising operational monitoring.
* Recording the publication event.

All activities should complete as a single logical publication process.

---

# 7. Workflow

### Step 1

Platform confirms that all publication prerequisites remain satisfied.

---

### Step 2

Publication Service locks the publication transaction to prevent partial activation.

---

### Step 3

The website becomes publicly accessible.

---

### Step 4

Customer-facing commerce capabilities are enabled according to the assigned commerce profile.

Examples:

**Product Business**

* Online ordering enabled.

**Service Business**

* Appointment scheduling enabled.

**Hybrid Business**

* Ordering and scheduling enabled.

---

### Step 5

The business is indexed within Main Street's internal search and discovery services.

---

### Step 6

Analytics, monitoring and operational services are activated.

---

### Step 7

A publication audit record is created.

---

### Step 8

The merchant receives a confirmation that the business is now live.

Where Google Business Profile integration has not yet completed, the merchant is informed that this process will continue automatically in the background.

---

# 8. Business Rules

* Publication shall occur only after successful Main Street verification.
* Google Business Profile integration shall not block publication.
* Publication shall activate all required operational services together.
* Failed publication shall automatically roll back any partial activation where technically possible.
* Every publication event shall be auditable.

---

# 9. Platform Responsibilities

The platform shall:

* Publish the website.
* Activate customer-facing services.
* Enable operational services.
* Record publication history.
* Notify the merchant.
* Begin post-publication monitoring.

---

# 10. Success Criteria

The workflow is successful when:

* The website is publicly accessible.
* Customers can interact with the business.
* Commerce capabilities are operational.
* Operational services are active.
* The business status becomes **Live**.

---

# 11. Postconditions

The merchant transitions from onboarding into normal business operations.

Background platform services continue independently, including:

* Google Business Profile synchronisation.
* SEO improvements.
* Analytics processing.
* Notification delivery.
* Continuous monitoring.

---

# 12. Related Workflows

Previous:

* MO-07 Business Readiness Assessment

Next:

* Merchant Operations

Platform Services:

* Publication Service
* Unified POS Service
* Notification Service
* Search Service
* Analytics Service

---

# 13. Notes

Go Live represents the operational launch of a merchant on Main Street.

Main Street assumes responsibility for publishing and activating the merchant's digital presence while continuing to improve external integrations and platform services in the background.

The merchant begins serving customers immediately, regardless of the completion status of non-essential external integrations such as Google Business Profile.
