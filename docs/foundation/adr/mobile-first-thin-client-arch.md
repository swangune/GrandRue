# ADR-002 — Mobile-First Thin Client Architecture

> **ADR ID:** ADR-002
> **Status:** Accepted
> **Date:** TBD
> **Owner:** Architecture Team

---

# 1. Context

Main Street is designed primarily for local businesses whose primary computing device is a smartphone.

Merchants may operate on:

- Entry-level Android devices
- Mid-range Android devices
- Premium Android devices
- iPhones
- Tablets
- Desktop computers

The platform must provide a consistent operational experience regardless of device capability.

As the platform evolves, new features, AI capabilities and business services must not degrade the mobile experience.

---

# 2. Problem Statement

How should Main Street deliver increasingly sophisticated platform capabilities while maintaining:

- Fast application performance
- Low memory consumption
- Small application footprint
- Efficient battery usage
- Reliable operation on lower-powered devices
- Minimal network usage

---

# 3. Options Considered

## Option A

Fat Client Architecture

The mobile application performs substantial business processing and stores significant operational logic.

### Advantages

- Rich offline capabilities
- Reduced server dependency

### Disadvantages

- Large application size
- Higher memory usage
- Increased battery consumption
- More complex updates
- Greater device compatibility challenges

---

## Option B

Thin Client Architecture

The mobile application acts primarily as the presentation and interaction layer while platform services perform business processing.

### Advantages

- Lightweight application
- Small installation size
- Lower device requirements
- Simplified updates
- Centralised business logic
- Easier maintenance

### Disadvantages

- Greater dependency on backend services
- Requires intelligent offline strategies

---

# 4. Decision

Main Street adopts a **Mobile-First Thin Client Architecture**.

The mobile application shall primarily:

- Present information
- Capture user input
- Perform lightweight validation
- Cache essential operational data
- Support limited offline operation

Business processing shall primarily occur within platform services.

---

# 5. Design Principles

The mobile application shall remain lightweight regardless of future platform growth.

New platform capabilities shall increase server-side intelligence rather than client-side complexity.

The application shall prioritise responsiveness over feature density.

---

# 6. Client Responsibilities

The mobile client is responsible for:

- User interface rendering
- User interaction
- Local session management
- Temporary caching
- Offline data synchronisation
- Secure authentication
- Device capability integration
  - Camera
  - GPS
  - Biometrics
  - Notifications

The client shall avoid implementing business logic beyond lightweight validation.

---

# 7. Platform Responsibilities

Platform services are responsible for:

- Business rules
- Workflow execution
- AI orchestration
- Verification processing
- Website generation
- Payment processing
- Reporting
- Analytics
- SEO automation
- Notification orchestration

---

# 8. Performance Objectives

Platform architecture shall optimise for:

- Fast application launch
- Responsive navigation
- Low memory consumption
- Efficient battery usage
- Reduced network traffic
- Minimal storage usage

Where trade-offs exist, merchant experience shall take precedence over architectural elegance.

---

# 9. Offline Operation

The platform shall support offline operation where practical.

Offline capabilities may include:

- Viewing cached operational data
- Creating draft Business Orders
- Recording bookings
- Capturing verification evidence

Synchronisation shall occur automatically when connectivity is restored.

Offline functionality shall never compromise data integrity.

---

# 10. AI Considerations

AI processing shall primarily execute on platform infrastructure.

The mobile application shall interact with AI services through well-defined interfaces.

AI capabilities shall not require large on-device models unless a future business requirement justifies such deployment.

---

# 11. Consequences

## Positive

- Lightweight mobile application
- Lower hardware requirements
- Improved battery performance
- Easier deployment
- Centralised business logic
- Better long-term maintainability

---

## Negative

- Greater dependence on platform availability
- Increased backend processing requirements
- More sophisticated synchronisation strategies

---

# 12. Relationship to ADR-001

This decision implements the Business-Driven Modular Architecture by ensuring that architectural complexity remains within platform components rather than the merchant's device.

The mobile application remains an operational interface rather than the primary processing environment.

---

# 13. Relationship to Platform Vision

Main Street exists to simplify digital operations for local businesses.

Merchants should experience a fast, responsive and reliable application regardless of device capability.

Platform intelligence shall increase without increasing client complexity.

---

# 14. Status

Accepted.