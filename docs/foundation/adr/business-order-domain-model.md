# ADR-005 — Business Order Domain Model

> **ADR ID:** ADR-005
> **Status:** Accepted
> **Date:** TBD
> **Owner:** Architecture Team

---

# 1. Context

Main Street supports diverse business types including:

- Retail
- Restaurants
- Hair salons
- Pharmacies
- Bakeries
- Florists
- Mechanics
- Healthcare providers
- Consultants
- Tutors
- Hybrid businesses

These businesses fulfil customer requests differently.

Traditional business software often models these requests using separate concepts such as:

- Sales Orders
- Service Orders
- Work Orders
- Repair Jobs
- Job Cards
- Tickets
- Appointments
- Product Orders

Although these names differ, they all represent work that the business has agreed to perform.

Maintaining separate operational models introduces duplication and unnecessary complexity.

---

# 2. Problem Statement

How should Main Street represent customer work in a manner that supports all business types while remaining simple, extensible and operationally consistent?

---

# 3. Options Considered

## Option A

Separate Domain Models

Create independent operational models for:

- Product Orders
- Service Orders
- Repair Orders
- Job Tickets
- Work Orders

### Advantages

- Familiar terminology
- Industry-specific modelling

### Disadvantages

- Significant duplication
- Inconsistent workflows
- Increased maintenance
- Difficult support for hybrid businesses

---

## Option B

Retail-Centred Order Model

Treat every business activity as a traditional sales order.

### Advantages

- Simple implementation

### Disadvantages

- Poor representation of service businesses
- Weak support for appointments
- Limited flexibility

---

## Option C

Business Order Domain Model

Represent all customer work through a single Business Order abstraction.

Products, services and future fulfilment types become line items within the Business Order.

### Advantages

- Unified operational model
- Supports every business category
- Simplifies workflows
- Simplifies Unified POS
- Supports hybrid businesses
- Extensible for future capabilities

### Disadvantages

- Requires careful domain modelling
- Introduces a new business concept

---

# 4. Decision

Main Street adopts the **Business Order Domain Model**.

A Business Order represents work that a business has agreed to perform for a customer.

The Business Order becomes the primary operational object within the Unified POS.

---

# 5. Business Order Definition

A Business Order may contain one or more fulfilment items.

Examples include:

- Products
- Services
- Future platform-supported fulfilment types

The Business Order represents the operational responsibility accepted by the business.

It does not prescribe how fulfilment occurs.

---

# 6. Relationship to Bookings

Bookings and Business Orders are independent business objects.

A Booking reserves future business capacity.

A Business Order represents actual business work.

A Booking may create a Business Order when fulfilment begins.

A Booking may also:

- Be cancelled
- Expire
- Result in a no-show

without creating a Business Order.

---

# 7. Relationship to Conversations

Customer conversations may lead to:

- General enquiries
- Bookings
- Business Orders
- Support requests

Not every conversation creates a Business Order.

---

# 8. Relationship to Payments

Payments settle Business Orders.

A Business Order may:

- Require full payment
- Require partial payment
- Require no payment
- Be paid before fulfilment
- Be paid during fulfilment
- Be paid after fulfilment

Payment policy is determined by business rules.

---

# 9. Relationship to Inventory

Inventory supports fulfilment.

Inventory is not the Business Order.

Business Orders consume inventory where applicable.

Service-only businesses may have no inventory consumption.

---

# 10. Operational Principles

Every Business Order shall:

- Have a lifecycle
- Maintain an audit history
- Support customer association
- Support staff assignment where applicable
- Support payment integration
- Support fulfilment tracking

Business Orders remain independent of business category.

---

# 11. Consequences

## Positive

- Unified operational language
- Reduced duplication
- Easier support for hybrid businesses
- Simplified AI reasoning
- Simplified reporting
- Simplified Unified POS

---

## Negative

- Requires merchants and developers to adopt the Business Order terminology internally

Customer-facing terminology may remain business-specific.

---

# 12. Relationship to ADR-004

The Unified POS operates on Business Orders.

Business Orders are the primary operational object presented within the operational workspace.

---

# 13. Relationship to ADR-001

The Business Order Domain Model supports the Business-Driven Modular Architecture by providing a common operational abstraction shared across business capabilities.

---

# 14. Relationship to Platform Vision

Main Street aims to simplify business operations rather than mirror industry-specific software.

A unified operational model reduces complexity while allowing businesses to operate according to their own workflows.

Merchants continue to think in terms familiar to their industry, while the platform maintains a consistent internal model.

---

# 15. Status

Accepted.