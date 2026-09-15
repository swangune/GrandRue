# BO-08 — Service Catalogue Management

> **Workflow ID:** BO-08
> **Module:** Business Operations
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow manages the business's service catalogue throughout its lifecycle.

The Service Catalogue represents the services that a business makes available to customers, regardless of whether those services require bookings, immediate fulfilment or other operational arrangements.

The workflow provides a consistent representation of business services while supporting diverse operational models across different industries.

---

# 2. Objective

Provide a unified service catalogue that:

- Represents business services
- Supports service configuration
- Supports multiple fulfilment models
- Integrates with operational capabilities
- Enables consistent customer experiences
- Maintains service history

---

# 3. Actors

## Primary Actor

- Merchant

## Supporting Actors

- Booking Management
- Business Order Management
- Website Generation
- Payment Operations
- AI Services

---

# 4. Preconditions

- Merchant has completed onboarding.
- Service Catalogue capability is enabled.

---

# 5. Trigger

The workflow begins whenever:

- A new service is introduced.
- Service information changes.
- Service availability changes.
- Service pricing changes.
- A service is retired.

---

# 6. Business Principles

- Services represent business offerings.
- Services are independent of bookings.
- Bookings are optional and determined by business configuration.
- Services shall maintain a single source of truth.
- Services may be offered through multiple customer channels.
- Service history shall remain auditable.

---

# 7. Workflow

## Step 1

The merchant creates or updates a service.

The platform records the service definition together with its business characteristics.

---

## Step 2

The platform validates the service information.

Where appropriate, AI Services may recommend improvements including:

- Service descriptions
- Categories
- Customer-facing content
- Search optimisation

Merchant approval is required before recommendations are applied.

---

## Step 3

The service is published to the Service Catalogue.

Availability is determined according to merchant configuration and applicable business rules.

---

## Step 4

The service becomes available to authorised platform capabilities including:

- Website Generation
- Business Orders
- Booking Management
- Reporting
- Customer-facing channels

---

## Step 5

The merchant may update the service throughout its lifecycle.

Historical information remains available for reporting and audit purposes.

---

# 8. Alternative Flows

## Draft Service

The service is saved without becoming available to customers.

---

## Temporarily Unavailable

The service remains within the catalogue but cannot currently be requested.

---

## Service Archived

The service is retired from active operations.

Historical Business Orders and Bookings continue referencing the archived service.

---

# 9. Business Rules

- Service Catalogue capability is optional.
- Services may require bookings.
- Services may support walk-in fulfilment.
- Services may require operational capacity.
- Services shall not be permanently deleted where historical records exist.
- All service changes shall be auditable.

---

# 10. Platform Responsibilities

The platform shall:

- Maintain the Service Catalogue.
- Validate service information.
- Publish services to authorised capabilities.
- Preserve historical references.
- Maintain audit history.

---

# 11. Success Criteria

The workflow is successful when:

- Service information has been recorded successfully.
- Service availability has been updated.
- Authorised platform capabilities can access the service.
- Historical integrity has been preserved.

---

# 12. Postconditions

Service information becomes available to authorised business capabilities including:

- Booking Management
- Business Order Management
- Website Generation
- Payment Operations
- Reporting & Analytics
- AI Services

---

# 13. Related Workflows

Previous:

- BO-07 Product Catalogue Management

Next:

- BO-09 Inventory Management

Platform Services:

- AI Services

---

# 14. Notes

The Service Catalogue is the authoritative source of service information within Main Street.

The workflow manages business services independently of booking and fulfilment mechanisms.

Operational capabilities such as Booking Management determine how services are delivered rather than defining the services themselves.