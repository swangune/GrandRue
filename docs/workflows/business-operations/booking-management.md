# BO-05 — Booking Management

> **Workflow ID:** BO-05
> **Module:** Business Operations
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow manages customer bookings throughout their lifecycle.

A booking represents a commitment by the business to reserve future operational capacity for a customer.

The workflow enables merchants to efficiently manage appointments, reservations and scheduled services while ensuring that booking decisions are based on the business's actual ability to fulfil the commitment.

---

# 2. Objective

Provide a consistent booking experience that:

- Reserves business capacity
- Prevents operational conflicts
- Supports merchant flexibility
- Maximises fulfilment reliability
- Integrates seamlessly with Business Orders

---

# 3. Actors

## Primary Actors

- Merchant
- Customer

## Supporting Actors

- Booking Engine
- Customer Communication Service
- Notification Service
- Business Order Management

---

# 4. Preconditions

- Merchant has completed onboarding.
- Booking capability is enabled for the business.
- Services requiring bookings have been configured.
- Business operating hours have been established.

---

# 5. Trigger

The workflow begins when:

- A customer requests a booking.
- A merchant creates a booking.
- A booking is modified.
- A booking requires operational action.

---

# 6. Business Principles

- A booking represents a business commitment.
- A booking reserves future business capacity.
- A booking is not a Business Order.
- Business Orders are created when fulfilment begins.
- Availability shall be determined by the Booking Engine.
- Merchants retain final authority over booking decisions.

---

# 7. Workflow

## Step 1

A booking request is received.

Requests may originate from:

- Merchant
- Customer website
- Customer portal
- AI assistant
- External integrations

---

## Step 2

The platform submits the request to the Booking Engine.

The Booking Engine determines whether the business can fulfil the requested booking based on the merchant's enabled business capabilities and operational constraints.

---

## Step 3

If suitable availability exists, the booking is created.

Where multiple suitable options exist, the platform may recommend the most appropriate option.

---

## Step 4

The booking is recorded.

The booking becomes part of the merchant's operational schedule.

Where appropriate, customer confirmations and notifications are issued.

---

## Step 5

The booking remains manageable throughout its lifecycle.

Merchants may:

- Confirm
- Reschedule
- Cancel
- Check in
- Add notes
- Update customer information

---

## Step 6

When fulfilment begins, the booking may be converted into a Business Order.

The Business Order becomes the operational object used by the Unified POS.

---

## Step 7

Upon completion, the booking is closed.

Historical booking information remains available for reporting and audit purposes.

---

# 8. Alternative Flows

## Availability Not Found

The Booking Engine determines that the requested booking cannot be fulfilled.

The platform may recommend alternative availability.

---

## Merchant Override

Where permitted by business policy, the merchant may manually accept or modify a booking despite platform recommendations.

Merchant overrides shall be recorded within the audit history.

---

## Customer Cancellation

The booking is cancelled.

Reserved business capacity is released.

---

## Merchant Cancellation

The booking is cancelled by the merchant.

The customer is notified.

Reserved business capacity is released.

---

## Customer No-show

The booking is recorded as a no-show.

Subsequent actions are determined by merchant policy.

---

# 9. Business Rules

- Booking capability is optional.
- Businesses without booking capability shall not access this workflow.
- A booking reserves business capacity rather than specific implementation resources.
- Availability shall always be determined by the Booking Engine.
- Merchants remain responsible for final booking decisions.
- All booking events shall be auditable.

---

# 10. Platform Responsibilities

The platform shall:

- Manage booking lifecycle
- Invoke the Booking Engine
- Record booking history
- Notify customers where appropriate
- Maintain booking audit records
- Convert bookings into Business Orders when fulfilment begins

---

# 11. Success Criteria

The workflow is successful when:

- The booking has been successfully recorded or updated.
- Business capacity has been managed appropriately.
- Customer notifications have been issued where required.
- The booking is available for operational fulfilment.

---

# 12. Postconditions

Completed bookings may result in:

- Business Order creation
- Payment processing
- Customer history updates
- Business reporting
- Analytics

---

# 13. Related Workflows

Previous:

- BO-04 Order Management

Next:

- BO-06 Customer Management

Platform Services:

- Booking Engine
- Customer Communication Service
- Notification Service

---

# 14. Notes

Booking Management coordinates the operational lifecycle of bookings.

The workflow intentionally delegates availability determination and operational scheduling to the Booking Engine, ensuring that booking intelligence remains centralised and reusable across the platform.

Bookings represent commitments to customers.

Business Orders represent the execution of those commitments.