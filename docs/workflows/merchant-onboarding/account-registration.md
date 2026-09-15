# Merchant Onboarding — 01. Account Registration

> **Workflow ID:** MO-01
> **Module:** Merchant Onboarding
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow defines how a new merchant creates a Main Street account.

It establishes the merchant's identity within the platform and creates the initial account from which all subsequent onboarding activities are performed.

Account registration creates a user account only. It does not create a business.

Business creation occurs in a subsequent workflow.

---

# 2. Objective

Enable prospective merchants to register quickly and securely while minimising friction.

The registration process should require only the information necessary to establish an account.

Additional business information will be collected later during onboarding.

---

# 3. Actors

### Primary Actor

* Merchant

### Supporting Actors

* Main Street Platform

### Supporting Services

* Authentication Service
* Notification Service
* AI Assistant (optional)
* Email Provider

---

# 4. Preconditions

* Merchant does not already have a Main Street account.
* Platform is available.
* Registration is enabled.

---

# 5. Trigger

The workflow begins when a visitor selects **Create Account** from the Main Street landing page.

---

# 6. Inputs

Required:

* First Name
* Last Name
* Email Address
* Password

Optional:

* Referral Code
* Marketing Consent

Business information is intentionally excluded from this workflow.

---

# 7. Workflow

### Step 1

Merchant selects **Create Account**.

---

### Step 2

Platform displays the registration form.

---

### Step 3

Merchant enters the required information.

---

### Step 4

Platform validates:

* Required fields
* Email format
* Password policy
* Duplicate account

---

### Step 5

If validation succeeds:

* Merchant account is created.
* Account status is set to **Pending Verification**.
* Email verification token is generated.

---

### Step 6

Verification email is sent.

---

### Step 7

Merchant is informed that email verification is required before continuing.

---

### Step 8

Workflow ends.

Merchant proceeds to:

**MO-02 Identity Verification**

---

# 8. Alternative Flows

## Duplicate Email

If the email already exists:

* Registration is rejected.
* Merchant is informed that an account already exists.
* Option to sign in is presented.
* Option to reset password is presented.

---

## Invalid Input

If validation fails:

* Invalid fields are highlighted.
* Merchant remains on the registration page.
* Previously entered valid data is preserved.

---

## Email Delivery Failure

If the verification email cannot be delivered:

* Account remains in **Pending Verification**.
* Merchant may resend the verification email.

---

# 9. Business Rules

* Email addresses must be unique.
* Passwords must satisfy platform security requirements.
* Accounts remain inactive until email verification is completed.
* Business information shall not be requested during account registration.
* Registration shall not automatically create a merchant business.

---

# 10. AI Responsibilities

The AI Assistant may:

* Explain registration fields.
* Clarify password requirements.
* Answer onboarding questions.

The AI Assistant shall not:

* Register accounts on behalf of users.
* Modify registration data.
* Bypass validation or verification.

---

# 11. Platform Responsibilities

The platform shall:

* Validate registration data.
* Create the user account.
* Generate verification tokens.
* Send verification emails.
* Record registration timestamps.
* Log security events.

---

# 12. Security Considerations

* Passwords shall never be stored in plaintext.
* Email verification tokens shall expire.
* Duplicate account enumeration should be minimised where appropriate.
* Registration endpoints should be protected against automated abuse.

---

# 13. Success Criteria

The workflow is successful when:

* A user account has been created.
* The account is in **Pending Verification** status.
* A verification email has been issued.
* The merchant is redirected to the identity verification stage.

---

# 14. Postconditions

The platform now has:

* A registered user account.
* A unique user identifier.
* A pending email verification.

No business has been created at this stage.

---

# 15. Related Workflows

Previous:

* None

Next:

* MO-02 Identity Verification

Future Dependencies:

* Business Creation
* Merchant Dashboard
* Authentication

---

# 16. Notes

This workflow is intentionally lightweight.

The objective is to establish a secure merchant identity before collecting business information.

Keeping registration simple reduces onboarding abandonment and allows subsequent workflows to focus exclusively on business setup.
