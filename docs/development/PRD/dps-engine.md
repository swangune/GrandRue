# **18. Digital Presence Synchronisation Engine**

## **18.1 Purpose**

The Digital Presence Synchronisation Engine (DPSE) is responsible for keeping a merchant's digital presence consistent across every connected channel.

It ensures that business information is entered once, stored once and synchronised intelligently wherever appropriate.

This eliminates one of the biggest problems faced by small businesses: maintaining the same information across multiple websites, platforms and services.

The synchronisation engine is a defining capability of Main Street.

---

# **18.2 Design Philosophy**

Main Street adopts the principle:

> **Update once. Publish everywhere.**

Merchants should never have to manually repeat the same change across multiple platforms.

The Business Profile remains the authoritative source of truth.

Every connected channel consumes information from this profile according to its own capabilities and policies.

---

# **18.3 Objectives**

The Synchronisation Engine shall:

* Eliminate duplicate data entry.
* Reduce inconsistent business information.
* Keep customer-facing channels aligned.
* Respect the limitations of third-party platforms.
* Provide clear synchronisation status.
* Recover gracefully from failures.
* Maintain a complete audit trail.

---

# **18.4 Single Source of Truth**

The Business Profile is the only editable source of business information.

Information should never be edited independently on connected systems through Main Street.

When a merchant updates:

* Opening hours
* Phone number
* Business description
* Products
* Services
* Branding
* Announcements

The Synchronisation Engine determines which destinations can receive those updates.

---

# **18.5 Synchronisation Model**

Every change follows a standard lifecycle:

1. Merchant updates the Business Profile.
2. Changes are validated.
3. The Business Profile is updated.
4. A synchronisation event is created.
5. Connected destinations are identified.
6. Each destination receives the update using its supported interface.
7. Successes and failures are recorded.
8. The merchant is notified if manual action is required.

This process should occur automatically without user intervention whenever possible.

---

# **18.6 Supported Destinations**

The Synchronisation Engine may publish to:

## Main Street Website

Always synchronised.

This is the primary destination.

---

## Google Business Profile

Where supported by Google's APIs and policies, Main Street may synchronise eligible business information.

Examples include:

* Opening hours
* Contact information
* Business description
* Photos

Main Street must never attempt to bypass Google's verification or approval processes.

Changes subject to Google's review may not appear immediately.

The platform should clearly communicate this to merchants.

---

## Social Media Platforms

Announcements may be syndicated to connected business accounts.

Supported platforms may include:

* Facebook
* Instagram
* LinkedIn
* X
* Threads

Each announcement should be adapted to the destination platform's capabilities while preserving the merchant's intended message.

---

## Search Engine Assets

Whenever relevant content changes, Main Street should automatically update:

* XML sitemap
* Structured data
* Internal links
* RSS or content feeds (if supported)

These updates help search engines discover changes more efficiently.

---

## Customer Communications

Business information used in:

* Booking confirmations
* Order confirmations
* Email templates
* Customer notifications

should always reflect the latest Business Profile.

---

# **18.7 Synchronisation Rules**

Not every field is appropriate for every destination.

The Synchronisation Engine shall determine:

* Which information is eligible.
* Which platforms support it.
* Whether merchant approval is required.
* Whether manual intervention is necessary.

This rules-based approach prevents unsupported or conflicting updates.

---

# **18.8 Announcement Syndication**

Announcements are one of the primary outputs of the Synchronisation Engine.

When a merchant publishes an announcement:

1. It appears immediately on the merchant's website.
2. A dedicated announcement page is generated.
3. Eligible social media posts are created.
4. SEO metadata is generated.
5. Search engine discovery assets are updated.

The merchant writes the announcement once.

Main Street distributes it intelligently.

---

# **18.9 Synchronisation Status**

Merchants should always understand the state of their connected services.

Each destination should display a status such as:

* Connected
* Synchronised
* Pending
* Requires Attention
* Disconnected
* Permission Required
* Synchronisation Failed

Statuses should include clear explanations and recommended actions.

---

# **18.10 Failure Handling**

Synchronisation failures should never interrupt the merchant's ability to operate.

If an update cannot be delivered:

* The website remains current.
* Successful destinations remain updated.
* Failed destinations are retried where appropriate.
* Permanent failures are surfaced to the merchant with guidance.

Failures should be isolated rather than cascading across the system.

---

# **18.11 Audit Trail**

Every synchronisation event should be recorded.

The audit log should include:

* Timestamp
* Merchant
* Updated fields
* Destination
* Synchronisation outcome
* Retry attempts
* Final status

This supports troubleshooting, transparency and compliance.

---

# **18.12 Performance Considerations**

Synchronisation should occur asynchronously wherever possible.

Merchants should not wait for every external platform to acknowledge an update before continuing their work.

Updates should be queued, processed reliably and monitored until completion.

---

# **18.13 Security**

Only authorised merchants may initiate synchronisation for their businesses.

Third-party credentials should be stored securely using industry best practices.

Access tokens should be encrypted and refreshed automatically where supported.

The Synchronisation Engine should request only the permissions necessary to perform its functions.

---

# **18.14 Merchant Transparency**

Main Street should clearly communicate what it can and cannot synchronise.

The platform must not create unrealistic expectations.

Where external platforms impose limitations, review processes or API restrictions, these should be explained in plain language.

Merchants should always understand when a delay or limitation originates from a third-party service rather than Main Street.

---

# **18.15 Synchronisation Statement**

The Digital Presence Synchronisation Engine transforms fragmented business management into a unified workflow.

Merchants no longer manage multiple platforms independently.

Instead, they maintain a single Business Profile while Main Street coordinates the distribution of information across connected digital channels responsibly, transparently and within the capabilities of each external platform.

This "update once, publish everywhere" approach is central to Main Street's vision of becoming the Digital Presence Operating System for local businesses.

---

### End of Section 18 – Digital Presence Synchronisation Engine
