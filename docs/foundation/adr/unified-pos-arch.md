# ADR-004 — Unified POS Architecture

> **ADR ID:** ADR-004
> **Status:** Accepted
> **Date:** TBD
> **Owner:** Architecture Team

---

# 1. Context

Local businesses operate in many different ways.

Examples include:

- Retail stores
- Restaurants
- Hair salons
- Pharmacies
- Florists
- Bakeries
- Mechanics
- Consultants
- Tutors
- Healthcare providers

Traditional business software often provides separate operational systems for different industries.

This results in:

- Duplicate functionality
- Inconsistent user experience
- Increased training requirements
- Higher maintenance costs
- Platform fragmentation

Main Street aims to provide a unified operational experience across all supported business categories.

---

# 2. Problem Statement

How can Main Street support diverse business operations while maintaining a simple, consistent and intuitive operational experience?

---

# 3. Options Considered

## Option A

Industry-Specific POS Systems

Each business category receives its own dedicated operational interface.

### Advantages

- Highly specialised
- Familiar industry workflows

### Disadvantages

- Significant duplication
- Difficult maintenance
- Inconsistent user experience
- Increased development effort
- Difficult cross-category support

---

## Option B

Generic Retail POS

Treat every business as a retail store.

### Advantages

- Simple implementation
- Consistent interface

### Disadvantages

- Poor support for service businesses
- Weak appointment integration
- Limited operational flexibility

---

## Option C

Unified POS Architecture

Provide a single operational workspace centred on Business Orders, with functionality adapting to business capabilities rather than business categories.

### Advantages

- Single operational experience
- Reduced duplication
- Easier training
- Greater maintainability
- Supports hybrid businesses
- Simplifies AI integration

### Disadvantages

- Requires careful domain modelling
- Greater upfront architectural effort

---

# 4. Decision

Main Street adopts a **Unified POS Architecture**.

The Unified POS shall provide a single operational workspace for all supported business types.

Business capabilities shall be enabled or disabled according to merchant configuration rather than through separate POS implementations.

---

# 5. Core Principles

The Unified POS represents the operational centre of the business.

The merchant shall not switch between different operational systems based on business type.

Instead, the operational experience adapts according to enabled capabilities.

---

# 6. Operational Model

The Unified POS shall support:

- Walk-in sales
- Online orders
- Service fulfilment
- Appointment fulfilment
- Hybrid product and service transactions
- Returns and refunds
- Payment collection
- Receipt generation

All operations occur within a consistent workflow.

---

# 7. Business Capability Model

Capabilities may include:

- Products
- Services
- Inventory
- Bookings
- Staff assignment
- Customer communication
- Payment processing

Capabilities are modular.

Merchants only see the capabilities relevant to their business.

---

# 8. User Experience Principles

The Unified POS shall:

- Minimise navigation
- Reduce cognitive load
- Prioritise speed
- Support touch-first interaction
- Remain usable on mobile devices
- Present a consistent operational workflow

Operational simplicity shall take precedence over exposing every possible feature simultaneously.

---

# 9. AI Responsibilities

AI may assist with:

- Order preparation
- Workflow recommendations
- Customer suggestions
- Inventory alerts
- Operational insights

AI shall augment merchant operations without replacing merchant control.

---

# 10. Consequences

## Positive

- Consistent merchant experience
- Lower training requirements
- Reduced platform duplication
- Easier maintenance
- Strong support for hybrid businesses
- Simplified future expansion

---

## Negative

- Greater domain modelling complexity
- Higher initial design effort

---

# 11. Relationship to ADR-001

The Unified POS is implemented as a business capability within the Business-Driven Modular Architecture.

Its internal implementation may evolve independently while maintaining a consistent operational interface.

---

# 12. Relationship to ADR-002

The Unified POS supports the Mobile-First Thin Client Architecture by maintaining a lightweight operational interface while delegating business processing to platform services.

---

# 13. Relationship to Platform Vision

Local businesses should not need to learn different operational systems as they grow.

The Unified POS enables merchants to manage products, services and hybrid business models through one consistent operational experience.

Operational complexity belongs inside the platform.

Operational simplicity belongs to the merchant.

---

# 14. Status

Accepted.