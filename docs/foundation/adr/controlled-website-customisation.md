# ADR-003 — Controlled Website Customisation

> **ADR ID:** ADR-003
> **Status:** Accepted
> **Date:** TBD
> **Owner:** Architecture Team

---

# 1. Context

Traditional website builders provide merchants with extensive control over website design, including layouts, colours, typography, navigation, widgets and page structures.

While this flexibility benefits experienced users, it also introduces significant complexity for the majority of small business owners.

Research and operational experience indicate that many local businesses:

- lack design expertise
- have limited technical knowledge
- rarely update their websites
- produce inconsistent customer experiences
- inadvertently reduce usability and SEO performance through excessive customisation

Main Street's objective is to simplify digital operations rather than provide unlimited design freedom.

---

# 2. Problem Statement

How can Main Street provide professionally designed websites while minimising merchant effort and maintaining:

- Brand identity
- Accessibility
- Performance
- Consistency
- SEO quality
- Long-term maintainability

---

# 3. Options Considered

## Option A

Fully Customisable Website Builder

Examples include traditional drag-and-drop website builders.

### Advantages

- Maximum flexibility
- Unlimited creativity
- Familiar market approach

### Disadvantages

- High learning curve
- Inconsistent user experience
- Poor design quality
- Increased support costs
- Greater maintenance complexity
- Reduced automation opportunities

---

## Option B

Fixed Website Template

Every merchant receives the same website.

### Advantages

- Extremely simple
- Consistent quality
- Easy maintenance

### Disadvantages

- Poor business differentiation
- Limited branding
- Not suitable for diverse industries

---

## Option C

Controlled Website Customisation

The platform automatically generates websites using predefined design systems while allowing merchants to control approved branding elements.

### Advantages

- Professional appearance
- Consistent usability
- Strong SEO
- Lower support requirements
- Enables AI-driven optimisation
- Faster onboarding
- Better mobile performance

### Disadvantages

- Reduced design freedom
- Advanced designers may require external solutions

---

# 4. Decision

Main Street adopts **Controlled Website Customisation**.

Website generation shall be automated using platform-approved design systems.

Merchants customise their digital presence through approved branding options rather than unrestricted layout editing.

---

# 5. Design Principles

The platform owns:

- Layout
- Navigation
- Component structure
- Responsive behaviour
- Accessibility
- Performance optimisation
- SEO implementation

The merchant owns:

- Business information
- Logo
- Brand colours
- Images
- Content
- Products
- Services
- Announcements
- Business identity

---

# 6. AI Responsibilities

AI shall:

- Recommend the most appropriate website layout.
- Adapt presentation according to business category.
- Optimise page structure.
- Improve SEO.
- Improve accessibility.
- Optimise content presentation.

AI shall not generate arbitrary layouts that violate platform standards.

---

# 7. Business Benefits

Controlled customisation enables:

- Faster onboarding
- Better customer experience
- Improved search engine visibility
- Lower operational support costs
- Consistent platform quality
- Simplified future upgrades

---

# 8. Platform Responsibilities

The platform shall ensure:

- Responsive design
- Accessibility compliance
- Mobile optimisation
- Consistent navigation
- Secure implementation
- Performance optimisation

Future improvements shall benefit all merchants without requiring website redesign.

---

# 9. Merchant Responsibilities

Merchants remain responsible for:

- Business information accuracy
- Brand assets
- Product information
- Service descriptions
- Pricing
- Business policies
- Customer-facing content

---

# 10. Consequences

## Positive

- Professional websites by default
- Reduced merchant effort
- Stronger SEO
- Easier platform evolution
- Consistent customer experience
- Lower support overhead

---

## Negative

- Limited creative freedom
- Not intended for highly customised enterprise websites

---

# 11. Relationship to ADR-001

Controlled Website Customisation supports the Business-Driven Modular Architecture by allowing the Website Generation component to evolve independently while maintaining consistent user experience.

---

# 12. Relationship to ADR-002

The decision supports the Mobile-First Thin Client Architecture by ensuring websites remain lightweight, responsive and performant across a wide range of devices.

---

# 13. Relationship to Platform Vision

Main Street exists to remove digital complexity from local businesses.

Website design should be an automated capability rather than a technical task performed by merchants.

Technology should enable businesses to focus on serving customers rather than designing websites.

---

# 14. Status

Accepted.