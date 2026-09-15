# MAIN STREET

# Technical Architecture Specification (TAS)

## Part I — Architecture Foundation

### Section 2. System Context

---

# 2.1 Purpose

The System Context defines the operational boundaries of Main Street.

It identifies every external actor, external system and major interaction that exists outside the platform.

Before designing internal architecture, it is important to understand **where Main Street begins and where it ends**.

This section establishes those boundaries.

---

# 2.2 System Boundary

Main Street is the digital operating system used by local businesses to establish and manage their online presence.

The platform is responsible for:

* Merchant onboarding.
* Website generation.
* Business management.
* Product management.
* Service management.
* Customer management.
* Orders.
* Bookings.
* AI assistance.
* Announcements.
* Analytics.
* Subscription management.

The platform is **not** responsible for:

* Delivering products.
* Performing services.
* Resolving customer disputes.
* Holding customer funds.
* Verifying businesses independently.
* Managing taxes.
* Running merchant businesses.

Those responsibilities remain with the merchant or trusted third-party providers.

---

# 2.3 Primary Actors

The architecture recognises five primary actors.

---

## Merchant

The merchant owns and operates the business.

The merchant interacts with Main Street to:

* Register a business.
* Manage products.
* Manage services.
* Publish announcements.
* View analytics.
* Process orders.
* Manage bookings.
* Configure business settings.

The merchant is the primary platform user.

---

## Customer

Customers interact only with the merchant's website.

Customers can:

* Browse products.
* Browse services.
* Place orders.
* Book appointments.
* Contact the merchant.
* Register with the merchant.
* Continue as guests.

Customers do not interact directly with the Main Street administration platform.

---

## Platform Administrator

Platform administrators manage Main Street itself.

Responsibilities include:

* Merchant moderation.
* Platform configuration.
* Subscription management.
* System monitoring.
* Support operations.
* Business verification oversight.
* Platform maintenance.

Administrators never become involved in merchant business operations.

---

## Artificial Intelligence

AI acts as an internal platform service.

It assists both merchants and platform operations.

Examples include:

* Website generation.
* Content generation.
* Image optimisation.
* FAQ generation.
* Marketing assistance.
* Analytics insights.

AI is not considered an independent user.

---

## Third-Party Services

External providers supply specialised capabilities.

Examples include:

* Google Business Profile
* Stripe Connect
* Resend
* AI model providers
* Domain providers

These services extend the platform without becoming part of the core application.

---

# 2.4 External Systems

Main Street communicates with several external systems.

These integrations provide specialised functionality while allowing Main Street to remain focused on its core mission.

Examples include:

## Google

Responsible for:

* Business Profiles.
* Maps.
* Search visibility.
* Business verification.

---

## Stripe

Responsible for:

* Merchant onboarding.
* Payment processing.
* Financial compliance.
* Merchant payouts.

---

## Email Provider

Responsible for:

* Transactional emails.
* Marketing emails.
* Review requests.
* Business notifications.

---

## AI Provider

Responsible for:

* Natural language generation.
* Image enhancement.
* Intelligent automation.

---

## Domain Provider

Responsible for:

* Domain registration.
* DNS management.
* SSL provisioning.

---

# 2.5 Information Flow

At the highest level, information flows as follows:

Merchant → Main Street

Main Street → Merchant Website

Customer → Merchant Website

Merchant Website → Main Street

Main Street → Third-Party Services

Third-Party Services → Main Street

Main Street → Merchant

Each interaction should have clearly defined ownership and responsibility.

---

# 2.6 Ownership Boundaries

Main Street owns:

* Platform software.
* Website generation.
* Merchant dashboard.
* Platform infrastructure.
* AI automation.
* Business management tools.

Merchants own:

* Business information.
* Products.
* Services.
* Customers.
* Orders.
* Bookings.
* Policies.
* Brand identity.

Third-party providers own:

* Payments.
* Google verification.
* Email delivery.
* External authentication services.

Ownership boundaries must remain clear throughout the architecture.

---

# 2.7 Trust Boundaries

Every interaction crossing into or out of Main Street represents a trust boundary.

Examples include:

* Merchant login.
* Customer checkout.
* Payment authorisation.
* Google authentication.
* AI requests.
* Email delivery.

Trust boundaries will guide later decisions regarding:

* Authentication.
* Authorisation.
* Encryption.
* API security.
* Data validation.

---

# 2.8 Architectural Scope

The TAS covers every component inside the Main Street platform.

External providers are treated as independent systems accessed through secure interfaces.

Future architectural decisions shall preserve this separation to minimise coupling and simplify future provider replacement where practical.

---

## End of Section 2 – System Context
