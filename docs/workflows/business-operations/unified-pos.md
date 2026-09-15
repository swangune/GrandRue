# Business Operations — BO-02 Unified POS

> **Workflow ID:** BO-02
> **Module:** Business Operations
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

The Unified POS provides merchants with a single operational environment for managing customer interactions, regardless of how those interactions begin.

It unifies in-store and digital business activities into one consistent operational workflow.

The Unified POS eliminates the distinction between physical and digital transactions, allowing merchants to operate their business through one operational experience.

---

# 2. Objective

Provide a single operational workflow for managing customer interactions from initiation through completion.

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
* Merchant has entered the Operational Workspace.
* Business is operational.

---

# 5. Trigger

The workflow begins whenever a customer interaction requires operational processing.

Examples include:

* Walk-in customer.
* Website order.
* Online booking.
* Customer conversation.
* Telephone enquiry.
* Merchant-initiated transaction.

---

# 6. Workflow

### Step 1

A customer interaction is initiated.

---

### Step 2

The Unified POS creates or resumes the operational session associated with the interaction.

---

### Step 3

The merchant performs the required business activity.

Depending on the interaction, this may include one or more operational tasks.

---

### Step 4

The merchant completes the customer interaction.

---

### Step 5

The Unified POS finalises the operational session.

---

### Step 6

The merchant returns to the Operational Workspace.

---

# 7. Alternative Flows

## Existing Operational Session

If an active operational session already exists for the customer, the Unified POS resumes the existing session rather than creating a new one.

---

## Interrupted Session

If an operational session cannot be completed immediately, it may remain active until resumed or closed.

---

## Cancelled Interaction

If the customer interaction is cancelled before completion, the operational session is closed according to applicable business rules.

---

# 8. Postconditions

Upon successful completion:

* The customer interaction is completed.
* The operational session is closed or updated.
* Related operational records are updated.
* The merchant returns to the Operational Workspace.

---

# 9. Related Documents

Business Rules

* Unified POS Rules

Platform Services

* Unified POS Service

AI Agents

* Unified POS Agent

UI/UX

* Unified POS Interface

Data

* Operational Session Model

Previous Workflow

* BO-01 Operational Workspace

Next Workflows

* Order Management
* Booking Management
* Customer Communication

---

# 10. Notes

The Unified POS is not limited to processing sales.

It serves as the operational entry point for customer interactions across the Main Street platform.

Individual operational behaviour is defined by the relevant business rules and platform services rather than this workflow.
