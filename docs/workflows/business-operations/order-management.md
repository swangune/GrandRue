# Business Operations — BO-04 Order Management

> **Workflow ID:** BO-04
> **Module:** Business Operations
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

The Order Management workflow manages the lifecycle of Business Orders within the Main Street platform.

A Business Order represents work that a business has agreed to perform for a customer.

Business Orders provide a unified operational model for products, services and hybrid businesses.

---

# 2. Objective

Provide a consistent operational workflow for creating, managing, fulfilling and completing Business Orders.

---

# 3. Actors

## Primary Actor

* Merchant

## Supporting Actors

* Customer
* Main Street Platform

---

# 4. Preconditions

* Merchant is authenticated.
* Business is operational.
* A customer interaction has resulted in work requiring fulfilment.

---

# 5. Trigger

The workflow begins whenever work must be performed for a customer.

Examples include:

* Walk-in purchase.
* Online purchase.
* Appointment begins.
* Merchant-created order.
* Customer request requiring fulfilment.

---

# 6. Workflow

### Step 1

A Business Order is created.

---

### Step 2

The Business Order is associated with the relevant customer interaction.

Where applicable, the order may reference:

* Customer
* Booking
* Conversation
* Operational Session

---

### Step 3

The merchant adds one or more line items to the Business Order.

Line items may represent:

* Products
* Services
* Other fulfilment items supported by platform policy

---

### Step 4

The merchant reviews the Business Order.

---

### Step 5

The Business Order proceeds through fulfilment.

---

### Step 6

Upon completion of fulfilment, the Business Order proceeds to payment where applicable.

---

### Step 7

The Business Order is completed.

---

### Step 8

The merchant returns to the Operational Workspace.

---

# 7. Alternative Flows

## Existing Business Order

If work is already associated with an existing Business Order, the existing order is updated rather than creating a duplicate.

---

## Booking-Originated Order

A booking may initiate creation of a Business Order when fulfilment begins.

---

## Cancelled Order

A cancelled Business Order follows the cancellation policies defined by the applicable business rules.

---

## Suspended Fulfilment

Business Orders may remain active while fulfilment is temporarily suspended.

---

# 8. Postconditions

Upon successful completion:

* Business Order is updated.
* Operational records are updated.
* Payment workflow may continue.
* Customer history is updated.
* Merchant returns to the Operational Workspace.

---

# 9. Related Documents

Business Rules

* Order Management Rules

Platform Services

* Order Service

AI Agents

* Order Management Agent

Data

* Business Order Model

Previous Workflow

* BO-03 Customer Communication

Next Workflow

* BO-05 Booking Management

---

# 10. Notes

A Business Order represents customer work rather than a specific sales transaction.

Products, services and hybrid fulfilment are represented through Business Order line items.

Order Management defines the operational workflow only.

Business behaviour, pricing, inventory, taxation, payment and fulfilment policies are defined by their respective documentation.
