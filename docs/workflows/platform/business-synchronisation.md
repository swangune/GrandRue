# Merchant Onboarding — 05. Business Verification

> **Workflow ID:** MO-05
> **Module:** Merchant Onboarding
> **Status:** Draft
> **Owner:** Product Team

---

# 1. Purpose

This workflow verifies that the registered business is a legitimate local business operating from a genuine physical location.

Business Verification establishes trust between the merchant, Main Street and future customers.

The workflow delegates all verification activities to the Verification Engine.

---

# 2. Objective

Determine whether the business satisfies Main Street's eligibility requirements for operating on the platform.

Upon successful verification, the business becomes eligible for website generation and publication.

---

# 3. Actors

## Primary Actor

* Merchant

## Supporting Actors

* Main Street Platform
* Verification Engine
* AI Verification Agents
* Human Verification Team
* Google Business Profile (where applicable)

---

# 4. Preconditions

* Merchant account exists.
* Business Registration has been completed.
* Business Profile Setup has been completed.
* Commerce Profile has been determined.

---

# 5. Trigger

The workflow begins immediately after Commerce Profile Determination.

---

# 6. Verification Principles

Main Street verifies businesses using one of two verification paths.

### Path A

Google Trust Inheritance

For businesses that already manage a verified Google Business Profile.

---

### Path B

Main Street Verification

For businesses that do not have a verified Google Business Profile.

---

The Verification Engine automatically determines the appropriate path.

---

# 7. Workflow

### Step 1

Platform submits the business to the Verification Engine.

---

### Step 2

Verification Engine determines the appropriate verification path.

---

### Step 3

Verification evidence is collected according to the selected path.

Examples include:

* Google ownership confirmation.
* GPS validation.
* Walk-in verification video.
* Business information validation.

---

### Step 4

Verification Engine analyses the submitted evidence.

AI agents assist where appropriate.

Human reviewers perform the final verification decision during the initial operational phases.

---

### Step 5

Verification Engine returns one of the following outcomes:

* Verified
* Rejected
* More Information Required

---

### Step 6

Platform updates the business verification status.

---

### Step 7

Verified businesses proceed to Website Generation.

Rejected businesses remain private until verification is successfully completed.

---

# 8. Alternative Flows

## Additional Information Required

The Verification Engine requests additional evidence.

The merchant uploads the requested information.

Verification resumes without restarting the onboarding process.

---

## Verification Rejected

The business remains unpublished.

The merchant receives the reason for rejection together with guidance on how to resubmit.

---

## Verification Delayed

If manual review is required, the business enters the Verification Queue.

The merchant may continue reviewing completed onboarding information but cannot publish the business.

---

# 9. Business Rules

* Every business must be verified before publication.
* Main Street verification is independent of Google verification.
* Existing verified Google Business Profiles may inherit Google's trust subject to ownership and location validation.
* Businesses failing verification shall not become publicly accessible.
* All verification decisions shall be auditable.

---

# 10. AI Responsibilities

The AI Verification Agents may:

* Analyse submitted evidence.
* Detect inconsistencies.
* Produce confidence scores.
* Recommend verification outcomes.

The AI shall not independently approve or reject businesses during the initial operational phases.

---

# 11. Platform Responsibilities

The platform shall:

* Invoke the Verification Engine.
* Track verification progress.
* Notify merchants of status changes.
* Store verification outcomes.
* Maintain complete audit history.

---

# 12. Success Criteria

The workflow is successful when:

* The business has been verified by Main Street.
* Verification status has been updated.
* The business becomes eligible for website generation.

---

# 13. Postconditions

Verified businesses may proceed to:

* Website Generation.
* Google Business Profile Integration.
* Business Readiness Assessment.

Verification remains valid unless revoked through future Trust & Safety processes.

---

# 14. Related Workflows

Previous:

* MO-04 Commerce Profile Determination

Next:

* MO-06 Website Generation

Platform Services:

* Verification Engine

---

# 15. Notes

Business Verification is the trust gateway of Main Street.

The workflow itself contains minimal business logic. All verification activities are delegated to the Verification Engine, ensuring a single, consistent verification process across the platform while allowing the engine to evolve independently of onboarding.
