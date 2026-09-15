# **27. Merchant Dashboard**

> **Authority note — 8 September 2026:** This PRD is lower-authority product evidence. Merchant-facing Business Health and Business Recommendations are governed by accepted `MS-PROT-083 v1.0`; Merchant Attention remains governed independently by `MS-PROT-043`. MS-PROT-083 does not establish a universal Business Health score. Where wording below is less precise or conflicts, the accepted authorities govern.

## **27.1 Purpose**

The Merchant Dashboard is the operational control centre of Main Street.

It provides merchants with a clear, real-time overview of their business, enabling them to manage daily operations from a single interface.

Rather than navigating multiple applications for websites, bookings, products, marketing and analytics, merchants access every essential business function through one unified dashboard.

The dashboard should answer one question every time a merchant logs in:

> **"What do I need to know and what should I do today?"**

---

# **27.2 Design Philosophy**

The Merchant Dashboard is based on one principle:

> **Surface what matters. Hide what doesn't.**

Local business owners are busy.

The dashboard should prioritise important information and minimise distractions.

Complex business software often overwhelms users with menus, reports and settings.

Main Street should do the opposite.

---

# **27.3 Objectives**

The Merchant Dashboard shall:

* Provide a business overview.
* Highlight important actions through the governed Merchant Attention model where applicable.
* Simplify business management.
* Surface governed Business Recommendations where applicable.
* Reduce administrative effort.
* Support mobile-first operation.
* Deliver governed current insights.

---

# **27.4 Dashboard Layout**

The dashboard should be divided into logical sections.

## Business Summary

Displays today's business snapshot using currently supportable business and analytical evidence.

Examples include:

* Today's bookings.
* Today's orders.
* Pending enquiries.
* Defined monetary observations where authoritative semantics exist.
* New customers where the metric is governed.
* Announcement performance where defined.
* Applicable Business Health indicators/assessments.

---

## Quick Actions

Frequently used actions should always be accessible.

Examples include:

* Create Announcement.
* Add Product.
* Add Service.
* View Orders.
* View Bookings.
* Contact Customers.
* Update Opening Hours.
* Upload Photos.

Quick Actions should require no more than one tap where doing so remains compatible with the applicable authority and safety requirements.

---

## Business Activity

Displays recent events.

Examples include:

* New booking.
* New order.
* New enquiry.
* Customer registration.
* Review received.
* Announcement published.
* Payment evidence received.

Activity should appear chronologically where that presentation is appropriate.

---

## Business Insights & Recommendations

Governed Business Insights and Business Recommendations may appear prominently where their evidence, coverage and current premises support presentation.

Examples:

> "Friday afternoons have been your busiest comparable period."

> "Consider publishing an announcement about your new product."

> "Your Google Business Profile is awaiting verification."

Recommendations should be practical, evidence-grounded and advisory. They do not become Merchant Intent or executable authority until the merchant creates a new instruction through the applicable operation.

---

# **27.5 Notifications and Merchant Attention**

The dashboard should display important notifications and applicable Merchant Attention items under their owning authorities.

Examples include:

* Booking cancellations.
* Failed payments.
* Synchronisation issues.
* Low inventory where Inventory evidence supports the condition.
* Website generation completed.
* Subscription renewal.
* Staff requests.

Presentation should prioritise material items without allowing Business Health to re-own Merchant Attention semantics.

---

# **27.6 Business Health Profile**

Where accepted Business Health Indicator Definitions and sufficient governed evidence exist, the dashboard may present a Business Health Profile consisting of applicable Business Health Assessments such as:

```text
NO_MATERIAL_CONCERN
MONITOR
MATERIAL_CONCERN
UNKNOWN
NOT_APPLICABLE
```

Potential future dimensions may include demand, capacity, customer continuity, inventory and workforce coverage where their analytical definitions are accepted.

Main Street does **not** currently use a canonical universal weighted Business Health score. The profile should help merchants identify material conditions without false precision or competitive ranking.

Complete Financial Health remains separately governed future scope and must not be inferred from nearby Payment or operational data.

---

# **27.7 Daily Summary**

Each day, merchants should receive a concise summary where the relevant evidence exists.

Examples:

Today's Bookings

Today's Orders

Defined monetary observations where supported

Outstanding Tasks

Pending Customer Messages

Upcoming Appointments

Material Business Health conditions or current Recommendations where applicable

The summary should help merchants prepare for the day ahead while distinguishing authoritative business facts from derived analytical interpretation.

---

# **27.8 Search & Navigation**

The dashboard shall provide universal search where supported.

Merchants should quickly locate:

* Customers.
* Products.
* Services.
* Orders.
* Bookings.
* Announcements.
* Staff.
* Reports.

Search should remain fast regardless of business size.

---

# **27.9 Mobile-First Design**

The Merchant Dashboard is primarily designed for smartphones.

Every major business function should be operable using one hand where operationally safe.

The interface should favour:

* Large touch targets.
* Simple navigation.
* Minimal typing.
* Clear visual hierarchy.

Desktop layouts should extend, not redefine, the mobile experience.

---

# **27.10 Personalisation**

Merchants may customise limited aspects of the dashboard where supported.

Examples include:

* Favourite quick actions.
* Presentation preferences.
* Notification preferences.
* Default landing page.
* Theme (Light/Dark/System).

Personalisation must not create new business semantics or allow presentation configuration to redefine Merchant Attention, Business Health, Exposure or operation authority.

The overall structure remains consistent across businesses while applicable capabilities determine what is relevant.

---

# **27.11 Real-Time Updates**

Where supported, dashboard information should update automatically.

Examples include:

* New orders.
* New bookings.
* Payment confirmations/evidence under Payment semantics.
* Customer registrations.
* Staff actions.

Merchants should not need to refresh the page manually for supported current information.

Derived analytical information remains subject to its own freshness/serviceability/currentness semantics.

---

# **27.12 Multi-Business Support**

Merchants managing multiple businesses should be able to switch between businesses from a single account where the applicable tenancy/account model supports it.

Each business remains isolated under its Merchant Scope.

Examples:

* Separate customer contexts.
* Separate analytics.
* Separate products.
* Separate staff/workforce relationships.
* Separate subscriptions.

Switching businesses should not collapse scope or authority boundaries.

---

# **27.13 Accessibility**

The dashboard shall comply with accessibility best practices.

Features include:

* Keyboard navigation.
* Screen reader support.
* High contrast compatibility.
* Responsive layouts.
* Accessible forms.
* Readable typography.

Accessibility benefits all merchants.

---

# **27.14 Performance**

Dashboard loading should prioritise speed without weakening currentness or authority semantics.

The interface should:

* Load progressively.
* Cache only where compatible with governing freshness/currentness contracts.
* Minimise unnecessary network requests.
* Optimise media assets.
* Prioritise visible content.

Performance should remain consistent as transaction history grows.

---

# **27.15 Merchant Experience**

The dashboard should feel like a daily business assistant rather than administration software.

Merchants should spend less time navigating menus and more time running their businesses.

Every screen should help answer questions such as:

* What is happening?
* What needs my attention?
* What should I consider doing next?
* What is known, inferred or unresolved?

---

# **27.16 Dashboard Statement**

The Merchant Dashboard serves as a unified mobile-first merchant workspace over Main Street's independently owned capabilities, Merchant Attention and governed analytical decision support.

It should present the most useful current business information without turning the dashboard into a new semantic owner or forcing merchants to navigate specialist software concepts.

When a merchant can open the dashboard and understand what is happening, what genuinely needs attention and what supported actions are available—without losing the distinction between fact, analysis and recommendation—the Merchant Dashboard has fulfilled its purpose.

---

### End of Section 27 – Merchant Dashboard