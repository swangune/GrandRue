# BO-07 — Product Catalogue Management

> **Workflow ID:** BO-07
> **Module:** Business Operations
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow manages the business's product catalogue throughout its lifecycle.

The Product Catalogue represents the products that a business makes available for customers to purchase, independent of inventory levels, sales channels or operational state.

The workflow enables merchants to create, organise and maintain accurate product information while ensuring products remain consistent across all platform capabilities.

---

# 2. Objective

Provide a unified product catalogue that:

- Represents the business's products
- Supports consistent product information
- Enables multiple sales channels
- Supports product variants
- Integrates with operational capabilities
- Maintains product history

---

# 3. Actors

## Primary Actor

- Merchant

## Supporting Actors

- Website Generation
- Business Order Management
- Inventory Management
- Payment Operations
- AI Services

---

# 4. Preconditions

- Merchant has completed onboarding.
- Product Catalogue capability is enabled.

---

# 5. Trigger

The workflow begins whenever:

- A new product is introduced.
- Product information changes.
- Product availability changes.
- Product variants are modified.
- A product is retired.

---

# 6. Business Principles

- Products represent business offerings.
- Products exist independently of inventory.
- Products may exist without stock.
- Product information shall have a single source of truth.
- Products may be presented through multiple sales channels.
- Product history shall remain auditable.

---

# 7. Workflow

## Step 1

The merchant creates or updates a product.

The platform captures the product's business information.

---

## Step 2

The platform validates the product information.

Where appropriate, AI Services may recommend improvements such as descriptions, categorisation and search optimisation.

Merchant approval is required before recommendations are applied.

---

## Step 3

The product is published to the Product Catalogue.

Availability is determined according to merchant configuration and applicable business rules.

---

## Step 4

The product becomes available to authorised platform capabilities, including:

- Website Generation
- Business Orders
- Inventory
- Reporting
- Customer-facing channels

---

## Step 5

The merchant may update the product throughout its lifecycle.

Historical information remains available for reporting and audit purposes.

---

# 8. Alternative Flows

## Draft Product

The product is saved without becoming available to customers.

---

## Product Temporarily Unavailable

The product remains in the catalogue but is unavailable for purchase.

Historical references remain unaffected.

---

## Product Archived

The product is retired from active operations.

Historical Business Orders continue referencing the archived product.

---

# 9. Business Rules

- Product Catalogue capability is optional.
- Products shall not be permanently deleted if referenced by historical business records.
- Product variants belong to their parent product.
- Inventory management shall not own product information.
- Product availability may be independent of inventory availability.
- All product changes shall be auditable.

---

# 10. Platform Responsibilities

The platform shall:

- Maintain the Product Catalogue.
- Validate product information.
- Support product variants.
- Publish product information to authorised capabilities.
- Preserve historical product references.
- Maintain audit history.

---

# 11. Success Criteria

The workflow is successful when:

- Product information has been recorded successfully.
- Product availability has been updated where applicable.
- Authorised platform capabilities have access to the latest product information.
- Product history remains intact.

---

# 12. Postconditions

Product information becomes available to authorised business capabilities, including:

- Website Generation
- Business Order Management
- Inventory Management
- Payment Operations
- Reporting & Analytics
- AI Services

---

# 13. Related Workflows

Previous:

- BO-06 Customer Relationship Management

Next:

- BO-08 Service Catalogue Management

Platform Services:

- AI Services

---

# 14. Notes

The Product Catalogue is the authoritative source of product information within Main Street.

It manages business offerings rather than stock. Inventory Management is responsible only for stock-related operations.

The Product Catalogue supports multiple sales channels while maintaining a single, consistent representation of each product.