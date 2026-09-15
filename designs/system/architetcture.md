Main Street — Overall Workflow Architecture Map

Status: Draft
Level: System / Level 1
Purpose: Establish the relationship between approved business workflows and the system's architectural capabilities and components.

1. Purpose

This document does not define how individual components are implemented.

Its purpose is to answer:

What does Main Street need to be able to do, based on the established workflows, and which architectural capability is responsible for each behaviour?

It therefore sits between the workflow documents and the component architecture.

Workflow Documents
        │
        ▼
Overall Workflow Map
        │
        ▼
System Capabilities
        │
        ▼
Component Boundaries
        │
        ▼
Component Architecture
2. Architectural Source of Truth

The following precedence applies:

Requirements
     │
     ▼
Workflow Documents
     │
     ▼
Overall Architecture
     │
     ▼
Component Architecture
     │
     ▼
Implementation

A lower-level design must not silently contradict a higher-level established decision.

If a component design exposes a contradiction, the correct response is to revisit the higher-level document rather than work around it informally.

3. Workflow-to-Capability Principle

A workflow describes behaviour.

A component describes responsibility.

These are not the same thing.

For example:

Workflow
"Merchant publishes an announcement"
              │
              ▼
       Required behaviours
              │
       ┌──────┼──────┐
       ▼      ▼      ▼
   Create   Publish  Distribute
              │
              ▼
       Architectural
       responsibilities

Only after this analysis should we decide which component owns each responsibility.

This prevents us from designing components simply because a feature has a convenient name.

4. Current System Capability Domains

Based on the established Main Street architecture, the current capability domains are:

Merchant
Customer
Catalogue
Resource
Staff
Availability
Website
Announcements
Notifications
Unified POS
Audit

Important: these are the current architectural areas we have established. They should not be treated as proof that every item must become an independently deployed service.

5. Workflow Classification

Every workflow should eventually be classified into one or more of these categories:

Business establishment

Establishing and maintaining the merchant's business presence.

Business configuration

Configuring how the merchant operates.

Catalogue operations

Defining what the merchant provides.

Resource operations

Defining and managing things required to provide those offerings.

Availability operations

Determining when offerings/resources can be used.

Customer operations

Managing customer interactions and relationships.

Staff operations

Registering, assigning and controlling merchant staff.

Website operations

Creating and maintaining the merchant's customer-facing website.

Announcement operations

Creating and publishing merchant announcements and distributing them to supported channels.

Notification operations

Communicating relevant events or information to customers, staff or merchants.

POS operations

Executing operational customer-facing activities.

Audit operations

Recording significant actions and maintaining traceability.

6. Capability Ownership Matrix

This is the working matrix, not yet the final dependency model.

Capability	Primary owner	Consumers / participants
Business identity	Merchant	All merchant-scoped components
Business profile	Merchant	Website, Customer-facing experiences
Staff relationship	Staff	Merchant, POS, Audit
Staff access	Staff / Identity & Access	Merchant, POS, other protected operations
Offerings	Catalogue	Website, POS, Availability
Resources	Resource	Catalogue, Availability, POS
Availability	Availability	Website, POS, Catalogue
Customer	Customer	Website, POS, Notifications
Website	Website	Customer
Announcements	Announcements	Website, connected external channels
Notifications	Notifications	Merchant, Staff, Customer
POS operations	Unified POS	Staff, Catalogue, Customer, Audit
Audit trail	Audit	All significant operations

Where the table says "owner", it means domain responsibility, not necessarily implementation ownership.

7. Announcement Workflow Implication

This is where our previous omission becomes important.

Announcements must be explicitly represented in the architecture because the established requirement is broader than simply displaying text on a website.

The conceptual flow is:

                    MERCHANT
                       │
                       ▼
                 ANNOUNCEMENT
                       │
              ┌────────┴────────┐
              │                 │
              ▼                 ▼
           WEBSITE       SOCIAL CHANNELS
              │                 │
              ▼                 ▼
          CUSTOMER        EXTERNAL AUDIENCE

But we should not yet decide whether social-media publishing belongs internally to Announcements, Notifications, an Integration capability, or another architectural arrangement.

That decision belongs in the Announcement component design after the workflow has been mapped completely.

8. Workflow Traceability

Every workflow should ultimately have a traceability chain:

WF-XXX
  │
  ├── Actors
  │
  ├── Preconditions
  │
  ├── Business rules
  │
  ├── Main flow
  │
  ├── Alternative flows
  │
  └── Outcomes
          │
          ▼
       Capability
          │
          ▼
       Component
          │
          ├── Domain model
          ├── Data
          ├── API
          ├── Communication
          ├── Security
          └── Experience

This becomes one of the most important architectural controls in the project.

9. Example Trace

Suppose an established workflow says:

Merchant creates an announcement
        ↓
Merchant reviews it
        ↓
Merchant publishes it
        ↓
Announcement appears on website
        ↓
Merchant may distribute it through connected social channels

The architectural derivation would be:

Workflow
   │
   ▼
Announcement capability
   │
   ▼
Announcement component
   │
   ├── Announcement domain
   ├── Publication state
   ├── Website contract
   ├── Social-channel integration contract
   └── Audit interaction

Then the component design can determine the appropriate internal architecture.

We do not jump directly from the workflow to a database schema or API.

10. Architectural Dependency Rule

A dependency should only be documented when it can be justified by:

an established workflow;
an established business rule;
an established data ownership requirement; or
a deliberate architectural decision.

Therefore, this:

Catalogue → Website

may be valid because the Website needs merchant offerings.

But we should record why it exists rather than simply drawing an arrow.

Likewise:

Announcements → Website

has a clear behavioural basis because published announcements are intended to appear on the merchant website.

11. Overall Workflow Map

The system-level picture should therefore become:

                              MAIN STREET
                                   │
                                   ▼
                         BUSINESS WORKFLOWS
                                   │
       ┌───────────────┬───────────┼────────────┬───────────────┐
       │               │           │            │               │
       ▼               ▼           ▼            ▼               ▼
   Merchant         Staff      Catalogue     Customer       Announcements
       │               │           │            │               │
       │               │           ▼            │               │
       │               │        Resource        │               │
       │               │           │            │               │
       │               │           ▼            │               │
       │               │      Availability      │               │
       │               │                        │               │
       └───────────────┴────────────┬───────────┘               │
                                    ▼                           ▼
                              Unified POS                    Website
                                    │                           │
                                    └──────────┬────────────────┘
                                               ▼
                                         Notifications
                                               │
                                               ▼
                                             Audit

Again, this is a workflow/capability map, not yet a final component dependency diagram.

12. What We Do Next

We now have three distinct architectural levels:

LEVEL 1
Overall Architecture
        │
        ▼
LEVEL 1.5
Overall Workflow → Capability Map
        │
        ▼
LEVEL 2
Component Architecture
        │
        ▼
LEVEL 3
Component APIs / Data / Contracts / UX
        │
        ▼
LEVEL 4
Prototype

The next step is therefore not to invent another architecture diagram.

It is to take the existing workflow documents one by one, extract their actors, operations, rules, state transitions and outcomes, and build the actual traceability matrix.