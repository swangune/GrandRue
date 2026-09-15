# BO-10 — Payment Orchestration

> **Workflow ID:** BO-10
> **Module:** Business Operations
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow facilitates payments between customers and merchants through authorised third-party payment providers.

Main Street does not process, hold, settle or finance customer funds.

It coordinates the payment workflow and records transaction information returned by the selected provider as business evidence.

---

# 2. Objective

Provide merchants with a unified payment experience that:

- Makes supported payment methods available to customers
- Connects merchants with authorised payment providers
- Supports provider-specific payment options
- Receives payment status information
- Associates transactions with business activities
- Maintains payment evidence
- Keeps payment processing outside Main Street

---

# 3. Actors

## Primary Actors

- Merchant
- Customer

## External Actors

- Payment Provider

Examples may include:

- Card payment providers
- Digital wallet providers
- Buy-now-pay-later providers
- Bank payment providers
- Payment terminal providers

---

# 4. Preconditions

- Merchant has configured at least one supported payment provider.
- The relevant provider integration is operational.
- A business activity has created a payment requirement.

---

# 5. Trigger

The workflow begins when a payment is required for a business activity.

Examples include:

- Business Order
- Booking
- Invoice
- Deposit
- Subscription
- Other supported commercial activity

---

# 6. Business Principles

- Main Street does not process customer funds.
- Main Street does not hold customer funds.
- Main Street does not provide financing.
- Payment execution remains the responsibility of the selected provider.
- Provider-specific financial products remain under provider control.
- Main Street records payment information returned by providers.
- Payment information is evidence of a business transaction, not a replacement for the provider's financial records.
- Merchants may use multiple payment providers where supported.
- Payment capability remains modular and provider-independent.

---

# 7. Workflow

## Step 1 — Payment Requirement

A business activity creates a payment requirement.

Main Street determines:

- Amount due
- Currency
- Related business activity
- Customer
- Payment context

---

## Step 2 — Provider Selection

Main Street identifies the payment providers configured and available for the merchant.

Applicable payment methods are presented according to:

- Merchant configuration
- Customer eligibility
- Provider availability
- Business rules

---

## Step 3 — Payment Initiation

Main Street initiates the payment through the selected provider.

The customer is transferred to, presented with, or connected to the provider's payment experience.

Main Street does not collect or process the customer's financial credentials.

---

## Step 4 — Provider Execution

The payment provider executes the transaction according to its own:

- Payment processing rules
- Security controls
- Fraud controls
- Financing rules
- Settlement processes
- Regulatory obligations

Main Street has no authority over these processes.

---

## Step 5 — Provider Confirmation

The provider returns a transaction result to Main Street.

The result may indicate:

- Successful
- Pending
- Failed
- Cancelled
- Refunded
- Partially refunded
- Other provider-defined states

---

## Step 6 — Evidence Recording

Main Street records the provider response required to support business operations.

Recorded information may include:

- Provider identity
- Provider transaction reference
- Related business activity
- Amount
- Currency
- Payment method
- Transaction status
- Timestamp
- Provider evidence or receipt reference

Sensitive payment credentials shall not be stored.

---

## Step 7 — Business State Update

Main Street updates the relevant business workflow based on the confirmed provider event.

For example:

```text
Payment Confirmed

↓

Business Order

↓

Paid / Partially Paid

↓

Fulfilment