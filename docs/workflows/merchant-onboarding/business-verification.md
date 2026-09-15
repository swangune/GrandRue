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

Business Verification is the only verification required for a business to become operational on Main Street.

---

# 2. Objective

Determine whether the business satisfies Main Street's eligibility requirements for operating on the platform.

Upon successful verification:

* the business becomes trusted by Main Street,
* the website becomes eligible for generation,
* the merchant may continue towards publication.

Google Business Profile verification is not required for onboarding completion.

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

Main Street independently verifies businesses before publication.

Where a merchant already manages a verified Google Business Profile, the Verification Engine may inherit Google's trust to reduce verification effort.

Google verification enhances discoverability but does not determine whether a merchant may operate on Main Street.

---

# 7. Verification Paths

The Verification Engine automatically selects one of two verification paths.

## Path A — Google Trust Inheritance

Applicable when the merchant already manages a verified Google Business Profile.

The Verification Engine validates:

* Google ownership.
* Google verification status.
* Business identity consistency.
* Physical presence through hardware GPS validation.

If validation succeeds:

* Main Street inherits Google's trust.
* Walk-in video verification is waived.
* The business proceeds directly to Main Street verification approval.

---

## Path B — Main Street Verification

Applicable when no verified Google Business Profile exists.

The Verification Engine performs independent verification using:

* Walk-in verification video.
* Hardware GPS validation.
* Business information validation.
* Human review supported by AI.

Upon successful verification:

* The business becomes trusted by Main Street.
* Website generation may proceed immediately.

Google Business Profile creation and verification become background platform activities after publication.

---

# 8. Workflow

### Step 1

Platform submits the business to the Verification Engine.

---

### Step 2

Verification Engine selects the appropriate verification path.

---

### Step 3

Verification evidence is collected.

Evidence may include:

* Google ownership confirmation.
* Hardware GPS location.
* Walk-in verification video.
* Business identity validation.
* Supporting metadata.

---

### Step 4

Verification Engine analyses submitted evidence.

Specialised AI Verification Agents perform supporting analysis.

Where required, evidence is reviewed by authorised human reviewers.

---

### Step 5

Verification Engine produces one of the following outcomes:

* Verified
* Rejected
* Additional Information Required

---

### Step 6

Platform updates the business verification status.

---

### Step 7

Verified businesses continue directly to Website Generation.

Rejected businesses remain unpublished until verification is successfully completed.

---

# 9. Alternative Flows

## Additional Information Required

The Verification Engine requests additional evidence.

The merchant provides the requested information.

Verification resumes without restarting onboarding.

---

## Verification Rejected

The business remains unpublished.

The merchant receives:

* reason for rejection,
* required corrective actions,
* resubmission guidance.

---

## Manual Review Required

Where automated verification cannot confidently determine an outcome, the business enters the Verification Queue.

Human reviewers complete the verification.

The merchant remains informed of progress.

---

# 10. Business Rules

* Every business must be verified by Main Street before publication.
* Main Street verification is independent of Google verification.
* Existing verified Google Business Profiles may reduce verification effort through Google Trust Inheritance.
* Businesses failing Main Street verification shall not become publicly accessible.
* Google Business Profile verification shall never delay Main Street website generation after successful Main Street verification.
* All verification decisions shall be auditable.

---

# 11. AI Responsibilities

The AI Verification Agents may:

* analyse submitted evidence,
* detect inconsistencies,
* validate GPS information,
* analyse verification videos,
* identify fraud indicators,
* generate confidence scores,
* recommend verification outcomes.

During the initial operational phases, AI recommendations require authorised human approval.

---

# 12. Platform Responsibilities

The platform shall:

* invoke the Verification Engine,
* track verification progress,
* notify merchants of status changes,
* securely store verification evidence,
* maintain immutable audit records,
* initiate downstream workflows upon successful verification.

---

# 13. Success Criteria

The workflow is successful when:

* the business has been verified by Main Street,
* verification status has been updated,
* the business becomes eligible for Website Generation.

Google Business Profile integration is not part of the success criteria for this workflow.

---

# 14. Postconditions

Verified businesses proceed to:

* Website Generation.
* Business Readiness Assessment.
* Publication.

Google Business Profile integration continues independently as a background platform workflow.

Verification remains valid unless revoked through future Trust & Safety processes.

---

# 15. Related Workflows

Previous:

* MO-04 Commerce Profile Determination

Next:

* MO-06 Website Generation

Platform Services:

* Verification Engine

Related Platform Workflows:

* Google Business Profile Synchronisation

---

# 16. Notes

Business Verification is the trust gateway of Main Street.

The workflow itself contains minimal business logic, delegating all verification activities to the Verification Engine.

Main Street's verification establishes the platform's trust in a business and determines publication eligibility.

Google Business Profile integration is an independent, asynchronous platform capability that improves external discoverability without affecting a merchant's ability to operate on Main Street.
