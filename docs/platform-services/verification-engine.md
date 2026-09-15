# Verification Engine

> **Module:** Platform Services
> **Location:** `docs/04-platform-services/verification-engine.md`
> **Status:** Draft
> **Owner:** Platform Engineering

---

# 1. Purpose

The Verification Engine is responsible for establishing trust between Main Street, merchants, customers and external platforms.

Its purpose is to verify that businesses operating on Main Street are genuine, locally operating brick-and-mortar businesses before they become publicly available.

The Verification Engine provides a single, reusable verification capability for the entire platform.

All verification-related workflows shall pass through this service.

---

# 2. Objectives

The Verification Engine shall:

* Verify merchant identity.
* Verify business legitimacy.
* Prevent fraudulent registrations.
* Detect fake or duplicate businesses.
* Protect customers from illegitimate businesses.
* Support Google Business Profile integration.
* Provide a scalable foundation for automated verification.

---

# 3. Principles

The Verification Engine follows these principles:

* Trust is earned through verification.
* Verification should minimise friction for legitimate businesses.
* Existing trusted evidence should be reused where appropriate.
* Human reviewers establish verification standards.
* AI assists verification but does not initially replace human judgement.
* Verification decisions must be auditable and explainable.

---

# 4. Responsibilities

The Verification Engine is responsible for:

* Verification orchestration.
* Verification evidence collection.
* Google trust validation.
* GPS validation.
* Video verification.
* Fraud detection.
* Human verification workflow.
* AI-assisted verification.
* Verification audit logging.
* Verification status management.

---

# 5. Verification Architecture

```text
Merchant
      │
      ▼
Verification Engine
      │
      ├── Verification Orchestrator
      ├── Evidence Collection
      ├── Verification Queue
      ├── Human Review
      ├── AI Assistance
      ├── Decision Engine
      └── Audit Repository
```

The Verification Engine provides a single entry point for all verification requests.

---

# 6. Verification Paths

The engine supports two verification paths.

## Path A – Existing Verified Google Business Profile

This path applies when the merchant already manages a verified Google Business Profile.

The engine performs:

* Google ownership validation.
* Google Business Profile status validation.
* Business information comparison.
* Hardware GPS validation.

If successful:

* Main Street inherits Google's trust.
* Video verification is waived.
* The merchant proceeds directly to activation.

---

## Path B – Main Street Verification

This path applies when no verified Google Business Profile exists.

The engine requires:

* Continuous walk-in verification video.
* Hardware GPS location.
* Business address validation.
* Manual review.

Once approved:

* The merchant is verified by Main Street.
* The website becomes active.
* Google verification begins asynchronously.

---

# 7. Verification States

Businesses move through defined verification states.

```text
Pending

↓

Evidence Submitted

↓

Under Review

↓

Verified

or

Rejected

or

More Information Required
```

State transitions shall be fully audited.

---

# 8. Verification Evidence

Depending on the verification path, evidence may include:

* Google ownership confirmation.
* Google verification status.
* Business address.
* Hardware GPS coordinates.
* Walk-in verification video.
* Verification timestamps.
* Supporting metadata.

All evidence shall be securely stored and linked to the verification record.

---

# 9. Human Verification

During the initial phases of Main Street, all verification decisions are made by trained human reviewers.

Human reviewers shall:

* Review submitted evidence.
* Approve or reject verification requests.
* Request additional evidence where necessary.
* Record the reason for every decision.

Human decisions form the authoritative verification standard for the platform.

---

# 10. AI-Assisted Verification

AI supports reviewers by analysing submitted evidence and producing recommendations.

Examples include:

* GPS consistency checks.
* Address matching.
* Signage detection.
* Business entrance detection.
* Fraud indicators.
* Verification confidence scoring.

The AI provides recommendations only.

Final approval remains the responsibility of authorised human reviewers.

---

# 11. Learning Strategy

Human verification decisions are continuously collected to improve future automation.

Every completed verification contributes to:

* Training datasets.
* Model evaluation.
* Agent refinement.
* Fraud detection improvements.

Automation shall increase only after sufficient high-quality verification data has been collected.

---

# 12. Verification Orchestrator

The Verification Orchestrator coordinates all verification activities.

Responsibilities include:

* Selecting the appropriate verification path.
* Invoking specialised verification agents.
* Aggregating verification results.
* Preparing human review packages.
* Recording final outcomes.

The orchestrator does not independently make verification decisions.

---

# 13. Verification Agents

The Verification Engine supports specialised agents.

Initial agents include:

* Google Trust Validation Agent
* GPS Validation Agent
* Video Analysis Agent
* Business Information Validation Agent
* Fraud Detection Agent
* Decision Support Agent

Additional agents may be introduced as the platform evolves.

---

# 14. Verification Decision Engine

The Decision Engine combines:

* Verification evidence.
* Human review.
* AI recommendations.
* Business rules.

It produces one of the following outcomes:

* Verified
* Rejected
* More Information Required

---

# 15. Audit and Traceability

Every verification action shall be recorded.

Audit records include:

* Evidence submitted.
* Reviewer identity.
* AI recommendations.
* Final decision.
* Decision rationale.
* Timestamps.

Verification history shall be immutable.

---

# 16. Security

The Verification Engine shall:

* Protect submitted evidence.
* Encrypt sensitive verification data.
* Restrict reviewer access.
* Prevent unauthorised verification changes.
* Record all administrative actions.

---

# 17. Future Evolution

As Main Street grows, the Verification Engine will gradually transition from:

Human Verification

↓

AI-Assisted Verification

↓

Confidence-Based Automation

Human oversight will remain available for exceptional cases, quality assurance and continuous improvement.

---

# 18. Platform Statement

The Verification Engine is the trust foundation of Main Street.

By combining human expertise, structured verification workflows and progressively trained AI, the platform ensures that legitimate local businesses are onboarded efficiently while maintaining a high standard of integrity, transparency and customer trust.
