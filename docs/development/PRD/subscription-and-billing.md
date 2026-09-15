# **30. Subscription & Billing**

## **30.1 Purpose**

The Subscription & Billing module manages merchant subscriptions, feature entitlements, payments and billing while maintaining Main Street's Software-as-a-Service (SaaS) business model.

The module should make upgrading effortless, billing transparent and subscription management self-service.

Merchants should clearly understand what they are paying for, what features are available on their current plan and how upgrading benefits their business.

---

# **30.2 Design Philosophy**

The Subscription & Billing module is built upon one principle:

> **Businesses should upgrade because they have grown, not because they have reached an artificial limitation.**

The Free Plan should provide genuine value.

Paid plans should unlock growth rather than remove unnecessary restrictions.

---

# **30.3 Objectives**

The Subscription & Billing module shall:

* Manage subscription plans.
* Process recurring payments.
* Enable seamless upgrades and downgrades.
* Control feature availability.
* Provide transparent billing.
* Minimise subscription administration.
* Integrate with platform services.

---

# **30.4 Subscription Structure**

Main Street offers four subscription tiers.

## Free

Designed to establish a business online.

Includes:

* Business Profile.
* AI-generated website.
* Main Street subdomain.
* Announcement feed.
* Basic SEO.
* Product **or** service catalogue.
* Basic analytics.
* Contact forms.
* Powered by Main Street branding.

The Free plan is intended to create online visibility rather than provide a complete digital operating system.

---

## Tier 1 – Business

Designed for businesses ready to transact online.

Additional capabilities include:

* Custom domain.
* Online bookings.
* Online ordering.
* Stripe Connect integration.
* CRM.
* Staff management.
* Customer registration.
* Google Business Profile integration.
* Enhanced SEO.
* Removal of advertisements.

---

## Tier 2 – Growth

Designed for businesses seeking growth through automation.

Includes everything in Business, plus:

* AI marketing campaigns.
* Customer segmentation.
* Marketing automation.
* Smart discounts.
* Unified AI Inbox.
* Social media syndication.
* Advanced analytics.
* Business intelligence.

---

## Tier 3 – Enterprise

Designed for high-volume local businesses.

Includes everything in Growth, plus:

* Unlimited marketing campaigns.
* Autonomous SEO content generation.
* Priority infrastructure resources.
* Premium AI capabilities.
* Advanced automation.
* Future enterprise integrations.

---

# **30.5 Feature Flags**

Every platform capability shall be controlled using Feature Flags.

Examples include:

* Booking Engine.
* Ordering Engine.
* Marketing Automation.
* AI Assistant.
* Staff Management.
* Analytics.
* Customer Accounts.
* Custom Domains.

Feature availability is determined dynamically by the merchant's subscription.

No code changes should be required to enable or disable features.

---

# **30.6 Subscription Lifecycle**

A merchant subscription progresses through several states.

These include:

* Trial (if offered).
* Active.
* Payment Pending.
* Grace Period.
* Suspended.
* Cancelled.
* Expired.

The platform shall respond automatically to subscription state changes.

---

# **30.7 Upgrades**

Merchants may upgrade at any time.

Upgrading should:

* Preserve all existing data.
* Immediately unlock new features where technically possible.
* Prorate charges where supported by the payment provider.
* Require minimal interruption.

Businesses should never need to recreate their website after upgrading.

---

# **30.8 Downgrades**

Merchants may downgrade their subscription.

Downgrading shall never delete business data automatically.

Instead:

* Premium features become inaccessible.
* Existing data is preserved.
* The merchant may regain access by upgrading again.

This protects business continuity and reduces the risk of accidental data loss.

---

# **30.9 Payment Processing**

Subscription payments shall be processed through Stripe.

Main Street shall not store payment card information.

Supported payment methods depend on the payment provider and merchant location.

---

# **30.10 Billing History**

Merchants shall have access to a complete billing history.

Records include:

* Subscription invoices.
* Payment receipts.
* Failed payments.
* Refunds (where applicable).
* Plan changes.
* Renewal history.

Billing records should remain available for the lifetime of the account.

---

# **30.11 Failed Payments**

If a subscription payment fails:

* The merchant is notified.
* Automatic retry attempts are made where supported.
* A grace period may be applied.
* Subscription status is updated accordingly.

Business data should remain protected during temporary payment issues.

---

# **30.12 Free Plan Advertising**

The Free Plan may display platform advertising.

Advertising shall:

* Promote Main Street.
* Never interfere with merchant content.
* Never advertise competing businesses.
* Remain clearly distinguishable from merchant content.

Upgrading removes these advertisements.

---

# **30.13 Powered by Main Street**

All Free Plan websites shall display a discreet footer message:

> **Powered by Main Street**

This serves as platform attribution and organic marketing.

Paid plans may remove this branding, depending on subscription level.

---

# **30.14 Subscription Notifications**

Merchants shall receive timely notifications regarding:

* Upcoming renewals.
* Successful payments.
* Failed payments.
* Subscription changes.
* Feature availability.
* Trial expiration.

Notifications should be clear and actionable.

---

# **30.15 Self-Service Billing**

Merchants should be able to:

* Upgrade plans.
* Downgrade plans.
* Cancel subscriptions.
* Update payment methods.
* Download invoices.
* View billing history.

These actions should not require contacting Main Street support.

---

# **30.16 Business Continuity**

Subscription changes shall never compromise business continuity.

Examples:

* Website remains online during billing grace periods where appropriate.
* Customer data remains protected.
* Orders and bookings remain accessible.
* Historical analytics are preserved.

The platform should prioritise stability and merchant trust.

---

# **30.17 Future Commercial Models**

The architecture should support future pricing models, including:

* Annual subscriptions.
* Multi-location pricing.
* Enterprise contracts.
* Add-on services.
* API usage plans.
* Marketplace integrations (if introduced).

The subscription engine should remain flexible without requiring architectural redesign.

---

# **30.18 Subscription & Billing Statement**

The Subscription & Billing module provides the commercial foundation of Main Street while remaining transparent, predictable and easy to manage.

By combining flexible subscription plans, dynamic feature management, secure recurring payments and self-service billing, the platform enables businesses to scale naturally as they grow.

The goal is not simply to charge for software, but to ensure that every subscription level delivers meaningful business value and supports the long-term success of local businesses.

---

### End of Section 30 – Subscription & Billing
