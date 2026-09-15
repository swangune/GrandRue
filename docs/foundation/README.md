# Main Street Documentation

> **Version:** 1.1
> **Status:** Active
> **Owner:** Product Team
> **Fundamental product-purpose authority:** `MS-FUNDAMENTAL-VISION-001` — `docs/foundation/Fundamental-Vision-Mission-and-Product-Constitution.md`

---

# 1. Purpose

This repository contains the complete product design and implementation specifications for Main Street.

The documentation serves as the single source of truth for product decisions, business rules, workflows, user experience, system behaviour, APIs, data models and implementation guidance, subject to the accepted authority hierarchy defined by `designs/DOCUMENT-GOVERNANCE.md` and `designs/AUTHORITY-INDEX.md`.

The objective is to ensure every feature is fully understood before implementation begins, reducing ambiguity, rework and inconsistent behaviour across the platform.

---

# 2. What is Main Street?

Main Street is a mobile-first digital operating-infrastructure platform for micro and small businesses with real operational needs but limited software-administration capacity.

Main Street translates how a business operates into supported digital capabilities, configures and coordinates those capabilities on the business's behalf, and absorbs the underlying software complexity so merchants and ordinary staff can interact with their business rather than with a conventional software stack.

The current implementation programme is the **Main Street MVP**. The MVP proves the foundational capability architecture and essential supported operations; it is not yet the complete Digital Business Operating System described by the long-term Fundamental Vision.

The platform is designed around business-model-first configuration, simplicity, administrative compression, mobile-first operation, deterministic capability ownership, progressive activation and AI-assisted interpretation without making AI authoritative over business truth.

---

# 3. Documentation Philosophy

This documentation is intended to guide implementation rather than merely describe the product.

Every document should answer:

* Why does this exist?
* Who uses it?
* How does it work?
* What business rules apply?
* What happens when things go wrong?
* What data is required?
* What APIs are required?
* How should this be implemented?

If an engineer cannot implement a feature without making business decisions, the documentation is incomplete.

---

# 4. Product Design Philosophy

Main Street is designed through meticulous collaboration between humans and AI.

Human responsibilities include:

* Vision
* Business strategy
* Product decisions
* User experience
* Business rules
* Prioritisation

AI responsibilities include:

* Challenging assumptions
* Identifying edge cases
* Drafting documentation
* Producing diagrams
* Assisting with UX
* Generating implementation
* Generating tests
* Improving consistency

Product decisions are always human-approved before implementation.

---

# 5. Development Methodology

Every feature follows the same lifecycle.

Business Idea

↓

Business Decision

↓

Markdown Specification

↓

Workflow Diagrams

↓

Wireframes

↓

UI Design

↓

API Design

↓

Data Design

↓

Implementation

↓

Testing

↓

Release

---

# 6. No Coding Policy

Main Street follows a design-first development approach.

No production code shall be written until all Version 1 workflows have been:

* Designed
* Reviewed
* Diagrammed
* Approved

Implementation begins only after the product design is considered stable.

---

# 7. Documentation Principles

Every document shall have a single responsibility.

Information should exist in one authoritative location only.

Other documents should reference that source instead of duplicating content.

This prevents conflicting documentation.

---

# 8. Documentation Structure

The documentation is organised into the following major areas:

* Product Principles
* Actors
* Business Concepts
* Workflows
* Platform Services
* APIs
* Data Models
* User Interface
* Diagrams
* Architectural Decisions

Each area is maintained independently while remaining connected through references.

---

# 9. Workflow-Driven Development

Workflows are the foundation of Main Street.

Every feature must originate from an approved workflow.

Each workflow defines:

* Business objective
* Actors
* Entry points
* User journey
* Business rules
* AI responsibilities
* Background automation
* Exception handling
* Completion criteria

All diagrams, UI designs, APIs and data models are derived from the workflow.

---

# 10. Diagram-Driven Design

Every major workflow shall include visual diagrams.

These diagrams communicate product behaviour more effectively than text alone and reduce ambiguity during implementation.

Where appropriate, workflows should include:

* User Flow Diagram
* Activity Diagram
* Sequence Diagram
* State Diagram
* Architecture Diagram

---

# 11. AI Philosophy

AI exists to amplify merchants, not replace them.

AI may:

* Improve writing
* Explain information
* Automate repetitive tasks
* Assist customer service
* Support marketing
* Generate implementation artefacts

AI shall not:

* Invent business facts
* Make commercial decisions
* Publish merchant content without approval
* Remove merchant control from business operations

---

# 12. Living Documentation

This documentation is intended to evolve alongside the product.

When a product decision changes:

1. Update the relevant Markdown document.
2. Update affected diagrams.
3. Update related workflows.
4. Update implementation guidance if necessary.

Documentation should always reflect the current product behaviour.

---

# 13. Success Criteria

This documentation is successful when:

* Product decisions are unambiguous.
* Engineers do not make business decisions during implementation.
* Designers understand every workflow.
* AI implementation follows approved business rules.
* New contributors can understand the product without relying on chat history.

---

# 14. Guiding Principle

Main Street is built on one simple belief:

**Merchants understand their business. Main Street understands technology.**

The platform exists to bridge that gap by translating business reality into supported capabilities, absorbing avoidable software complexity and presenting each person only with the decisions and actions relevant to them.

Current product, design, architecture, UX and implementation work must conform to `MS-FUNDAMENTAL-VISION-001`.
