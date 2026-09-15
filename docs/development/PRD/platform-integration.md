# **37. Platform Integrations**

**Revision:** 4 September 2026  
**Status:** Current product intent

## **37.1 Purpose**

The Platform Integrations module defines how Main Street securely connects with third-party services to extend platform capabilities while preserving the platform's core philosophy of Intelligent Simplicity.

Integrations should eliminate manual work for merchants rather than introduce additional complexity.

Whenever possible, integrations should be configured automatically during onboarding or through guided workflows.

---

## **37.2 Design Philosophy**

The Platform Integrations module is founded on one principle:

> **Integrations should feel native, not connected.**

Merchants should not need to understand APIs, authentication protocols or technical configuration.

Connecting external services should require as little effort as possible.

---

## **37.3 Objectives**

The Platform Integrations module shall:

- reduce manual work;
- improve operational efficiency;
- expand platform capabilities;
- maintain security;
- protect merchant and personal data;
- support future platform growth; and
- minimise configuration effort.

---

## **37.4 Integration Principles**

Every integration shall follow these principles.

### Merchant First

Integrations exist to improve merchant workflows.

Technology should never become the merchant's responsibility.

### Optional by Default

Merchants should connect only the services relevant to their business.

No unnecessary integration should be required unless a selected capability cannot be fulfilled safely without it.

### Secure

All integrations shall use secure authentication and encrypted communication.

### Reliable

Integration failures should not silently corrupt Main Street business state.

Where safe, the platform shall retry failed synchronisations automatically. Where an external action may already have happened, Main Street must reconcile uncertainty rather than repeat the action blindly.

### Replaceable

No single third-party provider should permanently define the platform architecture.

Providers should bind to bounded fulfilment responsibilities and remain replaceable where practical.

---

## **37.5 Google Business Profile**

Google Business Profile may be relevant to merchants using Google's local-discovery ecosystem.

Main Street may assist merchants in linking an existing profile, creating one where appropriate, monitoring supported status and synchronising supported information.

Google remains responsible for its own verification and provider facts.

Google Business Profile is not a universal eligibility requirement for Main Street.

---

## **37.6 Payment Integration**

Supported payment providers may provide payment-execution infrastructure.

Responsibilities may include:

- provider onboarding;
- identity/compliance processes owned by the provider;
- payment execution;
- payout/settlement services; and
- provider transaction evidence.

The merchant remains the Merchant of Record where applicable.

Provider execution does not replace Main Street's accepted business-commitment and Payment semantics.

---

# **37.7 Payroll Provider Integration**

Main Street shall support integration with qualified payroll providers as part of the optional Payroll capability.

Initial payroll support should prefer external providers for jurisdiction-sensitive responsibilities that would otherwise force Main Street to maintain country-specific statutory engines.

Possible provider fulfilment responsibilities include:

- gross-to-net payroll calculation;
- statutory tax/deduction calculation;
- employer contribution calculation;
- statutory submission/filing;
- generation of jurisdiction-specific payroll documents;
- payroll payment instruction or funds movement; and
- provider compliance updates.

The provider boundary must remain explicit:

```text
Main Street Payroll semantics
        ↓
required payroll fulfilment role
        ↓
supported provider binding
        ↓
provider calculation / filing / execution
        ↓
provider evidence
        ↓
Main Street validation / reconciliation / merchant workflow
```

A provider must not redefine:

- who Main Street considers a Payroll Participant;
- merchant approval authority;
- Main Street payroll/run identity;
- Main Street access-control semantics; or
- the distinction between prepared, approved, submitted and settled payroll states.

One provider need not fulfil every payroll responsibility. Calculation, statutory filing, document generation and funds movement may be separate fulfilment roles.

Payroll integrations are post-MVP product scope unless deliberately rescheduled.

---

## **37.8 Email Integration**

The platform shall integrate with supported email providers for transactional and authorised marketing delivery.

Examples include:

- Order confirmations;
- Booking confirmations;
- Appointment reminders;
- review requests;
- business announcements; and
- security/payroll-document availability notifications where appropriate.

Sensitive payroll amounts should not be placed in ordinary notification payloads unless explicitly justified and protected.

---

## **37.9 Social Media Integration**

Merchants may connect supported social media accounts.

The primary objective is content distribution, not social networking.

When a merchant publishes an authorised Announcement, the platform may publish corresponding content to connected social providers.

---

## **37.10 Domain Integration**

Supported subscriptions may permit custom domains.

The platform should automate domain verification, DNS guidance, certificate provisioning, HTTPS configuration and renewal monitoring where possible.

---

## **37.11 Maps & Location Services**

Location services may support local discovery, directions, geographic service areas and distance calculations.

Location is relevant only where the merchant's operating model requires it.

---

## **37.12 Analytics Integrations**

Future integrations may include external analytics platforms where appropriate.

Main Street should present provider-derived information through governed projections rather than forcing merchants to operate multiple reporting tools.

External analytics results do not become authoritative business facts merely because they are displayed in Main Street.

---

## **37.13 Calendar Integrations**

Service businesses may synchronise appropriate scheduling information with external calendar providers.

Calendar representation must not become the authoritative Booking/Appointment owner.

---

## **37.14 Future Operational Integrations**

The architecture should support future integrations with areas such as:

- delivery providers;
- inventory systems;
- accounting software;
- Point-of-Sale systems;
- customer loyalty platforms;
- tax calculation services;
- payroll providers; and
- workforce/timekeeping providers.

These integrations should remain modular and capability-bound.

---

## **37.15 Integration Management**

Merchants shall have a coherent location for managing integrations.

The Integration Centre should expose only information useful to the merchant, such as:

- connected services;
- connection status;
- required permissions;
- last successful interaction/synchronisation where meaningful;
- action-required state; and
- connection health.

Merchants should be able to disconnect optional services, subject to clear explanation where doing so makes an active capability temporarily unserviceable.

---

## **37.16 Failure and Uncertainty Handling**

Integration failures shall be managed according to the nature of the external operation.

The platform shall:

- detect failures;
- retry only when retry is safe;
- reconcile uncertain external outcomes;
- avoid duplicate payments, payroll runs, filings or notifications;
- notify merchants only when action is required; and
- retain sufficient evidence for diagnostics and audit.

Operational continuity should be prioritised without fabricating successful provider outcomes.

---

## **37.17 Security and Privacy**

Every integration shall:

- use secure authentication;
- request only necessary permissions;
- protect credentials/secrets;
- encrypt transmitted data appropriately;
- enforce merchant scope;
- respect purpose limitation and privacy requirements; and
- maintain required audit evidence.

Payroll and workforce integrations require particularly strict treatment because they may process compensation, tax, identity and bank-related information.

---

## **37.18 Future Integration Platform**

As Main Street evolves, the integration framework may support public APIs, webhooks, partner applications and developer tooling.

Any future ecosystem must preserve capability ownership and prevent third-party provider conventions from becoming Main Street business semantics.

---

## **37.19 Platform Integrations Statement**

Main Street integrations extend the operating platform without fragmenting it.

> **Main Street owns the supported business meaning and merchant workflow; providers fulfil bounded external responsibilities and return evidence.**

This rule applies equally to payments, calendars, communications, payroll and future provider-backed capabilities.

---

### End of Section 37 – Platform Integrations
