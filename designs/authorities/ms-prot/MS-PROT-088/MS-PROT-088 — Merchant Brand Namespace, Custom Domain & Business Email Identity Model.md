# MS-PROT-088 — Merchant Brand Namespace, Custom Domain & Business Email Identity Model

**Document ID:** MS-PROT-088  
**Version:** 1.0  
**Status:** **ACCEPTED by explicit manual approval on 9 September 2026**  
**Approved:** Explicit manual approval on 9 September 2026 after governed review, falsification and complete proposal presentation in ChatGPT  
**Authority type:** Merchant-brand infrastructure / platform-service authority  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; `MS-FUNDAMENTAL-VISION-001`  
**Depends on:** composite MS-PROT-031, MS-PROT-036, MS-PROT-048, MS-PROT-051, MS-PROT-053, MS-PROT-056, MS-PROT-063, MS-PROT-064, MS-PROT-065, MS-PROT-067, MS-PROT-071, composite MS-PROT-075 and composite MS-PROT-086  
**Implementation activation:** NONE

---

# 1. Governing Decision

Main Street SHALL treat a merchant's customer-facing Internet identity as one coherent **Merchant Brand Infrastructure** concern comprising:

```text
merchant brand namespace
        +
public website hostname
        +
business email sender identity
        +
supporting domain-control infrastructure
```

The merchant-facing experience SHALL be coherent.

The underlying authorities SHALL remain separated.

Canonical:

```text
Merchant
    "I want my business to use bellasalon.co.uk."
                    ↓
          Merchant Brand Namespace
                    ↓
        ┌───────────┴───────────┐
        ↓                       ↓
Website Hostname          Business Email Identity
bellasalon.co.uk          bookings@bellasalon.co.uk
        ↓                       ↓
Storefront                 Notification /
delivery surface           Customer Communication
```

The domain SHALL NOT become the merchant's tenant identity, business model, website content owner, Notification owner, Conversation owner, mailbox or delivery provider.

---

# 2. Why Main Street Should Own This Coordination

A micro or small merchant should not need to understand:

```text
registrars
nameservers
DNS zones
A / AAAA / CNAME records
MX records
SPF
DKIM
DMARC
TLS certificates
MAIL FROM
bounce domains
email receiving infrastructure
registrar transfer locks
authorisation codes
provider webhooks
certificate renewal
```

merely to operate publicly as:

```text
bellasalon.co.uk

and

bookings@bellasalon.co.uk
```

Main Street therefore owns the **coordination and administrative compression** required to establish and maintain that business-facing identity.

Main Street SHALL NOT accomplish this by becoming a generic registrar console, DNS-management product, mailbox provider or website-hosting control panel.

---

# 3. Feature Admission

The node satisfies the **Administrative-Compression Test**.

Without it, the merchant must coordinate domain registration, DNS, website routing, certificates and email authentication manually or through several external products.

It also satisfies the **Coordination Test**.

A merchant-facing Internet identity must compose correctly with:

```text
Merchant Scope
Storefront
Notifications
Customer Messaging
Commercial Entitlement
Provider fulfilment
Credential Security
Exposure
Background Work
Audit
```

No conventional ERP, CRM, office-suite or generic IT-administration capability is introduced.

---

# 4. Authority Classification

Merchant Brand Infrastructure is a **Main Street platform service**.

It is not a merchant business-semantic capability merely because it is commercially valuable.

Canonical:

```text
Merchant business model
        ≠
Merchant Brand Infrastructure

Commercial entitlement
        may permit access to
Merchant Brand Infrastructure

but

Commercial entitlement
        does not create domain control
        does not verify a domain
        does not create sender authority
```

MS-PROT-056 remains authoritative for plan packaging, pricing and commercial entitlement.

---

# 5. Canonical Separation

The following distinctions are mandatory:

```text
Merchant Scope
    ≠ domain

Merchant public name
    ≠ domain

Merchant legal identity
    ≠ domain

Domain-control evidence
    ≠ legal business verification

Merchant Brand Namespace
    ≠ DNS provider

Merchant Brand Namespace
    ≠ registrar

Business Email Identity
    ≠ mailbox

Business Email Identity
    ≠ Notification

Business Email Identity
    ≠ Conversation

Business Email Identity
    ≠ email-delivery provider

Website Hostname Binding
    ≠ Storefront business state

Domain verification
    ≠ authentication of merchant staff

Domain connection
    ≠ registrar transfer

Registrar transfer
    ≠ transfer of Merchant Scope

Subscription termination
    ≠ loss of merchant domain rights
```

---

# 6. Merchant Brand Namespace

A **Merchant Brand Namespace** identifies a domain namespace that Main Street is authorised to use for one Merchant Scope.

Initial namespace families are:

```text
PLATFORM_DELEGATED_NAMESPACE

MERCHANT_CONTROLLED_DOMAIN
```

## 6.1 Platform-delegated namespace

Main Street MAY provide a merchant-qualified namespace below a Main Street-controlled parent domain.

Conceptually:

```text
bella.mainstreet.app
```

and, where commercially offered:

```text
hello@bella.mainstreet.app
```

Main Street owns the parent namespace.

The merchant receives a bounded right to use its assigned child namespace while the applicable service remains available.

A platform-delegated namespace:

```text
is not merchant-owned
is not externally transferable
does not prove domain ownership
does not become Merchant Scope identity
```

## 6.2 Merchant-controlled domain

A merchant-controlled domain is a domain or delegated subdomain over which adequate current control has been established for the intended Main Street use.

Example:

```text
bellasalon.co.uk
```

or an explicitly delegated subdomain where appropriate.

The domain remains distinguishable from the Main Street provider relationships used to administer it.

---

# 7. Initial Custom-Domain Portfolio

The initial portfolio SHALL support:

```text
one primary merchant-controlled custom domain
+
one bounded canonical website alias where required,
such as www versus apex
+
technical service subdomains required internally
```

Examples of internal technical subdomains may include:

```text
reply.bellasalon.co.uk
bounce.bellasalon.co.uk
```

where required by accepted fulfilment contracts.

Technical service subdomains do not count as additional merchant-facing custom domains.

Arbitrary portfolios of many unrelated public domains are not part of the initial service.

Wildcard merchant-controlled domain claims are not part of the initial portfolio.

---

# 8. Domain Acquisition Paths

The initial merchant experience SHALL provide three distinct paths:

```text
1. CONNECT_EXISTING_DOMAIN
2. REGISTER_NEW_DOMAIN
3. TRANSFER_DOMAIN_MANAGEMENT
```

Their priority is intentional.

Canonical product ordering:

```text
Connect existing domain
        ↓ preferred where merchant already owns one

Register new domain
        ↓ preferred where merchant has none

Transfer registrar management
        ↓ optional convenience, never required merely to connect
```

---

# 9. Connect Existing Domain

A merchant who already controls a domain SHOULD normally retain its existing registrar.

Main Street SHALL prefer:

```text
merchant keeps registrar
        +
Main Street receives only the control required
for authorised website/email infrastructure
```

over:

```text
mandatory transfer to Main Street
```

The ordinary merchant interaction SHOULD be equivalent to:

```text
Do you already have a domain?

bellasalon.co.uk

[Connect domain]
```

Main Street SHALL then attempt the least administratively burdensome safe connection method available.

Preferred order:

```text
supported provider authorisation
        ↓
bounded automated configuration

otherwise

deterministic domain verification
+
minimum required manual DNS action
```

The merchant SHALL NOT be required to transfer their registrar relationship merely because automated DNS management is unavailable.

---

# 10. Existing Infrastructure Must Be Preserved

Connecting a domain is additive by default.

Main Street MUST inspect or otherwise account for existing relevant infrastructure before making a material cutover.

A domain may already support:

```text
existing website
Google Workspace
Microsoft 365
Zoho Mail
other email hosting
existing verification records
other subdomains
existing security policy
```

Main Street SHALL NOT silently replace these.

Hard rule:

> **Domain connection does not grant Main Street blanket authority to rewrite the merchant's DNS zone.**

Where Main Street manages DNS records, it SHALL manage only records required by accepted Main Street responsibilities unless the merchant separately chooses a broader managed-DNS service under an accepted authority.

---

# 11. Website Cutover Is Explicit

If:

```text
bellasalon.co.uk
```

already serves another website, connecting the domain to Main Street SHALL NOT automatically replace that website.

The merchant must explicitly approve website binding/cutover.

The domain may therefore initially exist in Main Street as:

```text
verified for email
website binding absent
```

or:

```text
verified for website
business-email binding absent
```

or both.

Coherent Merchant Brand Infrastructure does not mean all endpoint bindings are activated together.

---

# 12. Register New Domain Through Main Street

A merchant without a domain MAY request a new domain through Main Street.

The merchant interaction SHOULD use business language.

Conceptually:

```text
Business:
Bella Salon

Suggested:
bellasalon.co.uk
bellasalonswansea.co.uk

Merchant chooses:
bellasalon.co.uk
```

AI or deterministic naming assistance MAY suggest candidate names.

A suggestion is not evidence that the domain is available.

Current availability MUST come from the applicable registrar/registry fulfilment path.

Before registration, Main Street SHALL show the merchant the material commercial terms, including applicable purchase/renewal cost and renewal arrangement.

Registration requires explicit merchant approval.

---

# 13. Domain Registration External-Effect Safety

Domain registration is an external side effect.

Main Street SHALL establish durable intent before requesting the external registration effect.

Canonical:

```text
approved registration intent
        ↓
durable external-operation identity
        ↓
registrar execution
        ↓
provider/registry evidence
        ↓
Main Street registration relationship
```

If the provider response is lost after a possible registration effect:

```text
possible external effect
        ↓
UNRESOLVED / reconciliation required
        ↓
no blind second registration attempt
```

Main Street SHALL NOT assume failure merely because an acknowledgement was not received.

---

# 14. Merchant Domain Right

Where Main Street registers or manages a merchant's custom domain, the commercial/technical arrangement MUST preserve the merchant's durable right to take control of or transfer that domain, subject only to genuine registry, legal, security or provider restrictions.

Main Street SHALL NOT make ownership of the merchant's public business domain a platform asset merely because Main Street paid, resold or technically administered the registration.

Where registrar/provider structure permits, the merchant or applicable merchant-controlled registrant shall be reflected appropriately in the registration relationship.

Where provider or registry mechanics require another arrangement, Main Street MUST preserve equivalent merchant portability and disclose any material limitation before registration.

Hard invariant:

```text
Main Street manages infrastructure
        ≠
Main Street acquires the merchant's business identity
```

---

# 15. Optional Registrar Transfer

A registrar transfer into a Main Street-managed arrangement is optional.

It SHALL NOT be required merely to:

```text
host the Main Street storefront
authenticate business email
use Conversation-bound email
```

Transfer may be offered where it materially reduces administration.

The merchant interaction SHOULD be expressed as:

```text
Let Main Street manage this domain for you
```

rather than exposing registrar implementation vocabulary unnecessarily.

Transfer requires explicit merchant approval.

Main Street SHALL absorb transfer codes, locks and registry procedure where technically possible.

Where merchant action is unavoidable, only the required action SHALL be surfaced.

---

# 16. Transfer Must Not Cause Premature Cutover

A pending registrar transfer SHALL NOT itself authorise:

```text
nameserver replacement
website cutover
MX replacement
sender activation
```

Existing domain operation should remain stable while the transfer is pending.

Website/email bindings change only through their own accepted readiness and activation rules.

---

# 17. Transfer Out and Portability

A merchant SHALL be able to stop using Main Street without forfeiting a merchant-controlled domain.

Canonical:

```text
Main Street subscription ends
        ≠
domain ownership/control ends
```

Where Main Street manages registration, the merchant SHALL have a governed path to:

```text
transfer registrar management away
or
take direct registrar control
or
change DNS away from Main Street
```

Main Street SHALL NOT create an artificial transfer lock merely to retain the merchant.

Genuine registry or provider transfer restrictions remain valid external constraints.

---

# 18. Renewal

Domain renewal is an external commercial/registry responsibility distinct from Main Street subscription entitlement.

Main Street MAY coordinate renewal where it manages registration.

Automatic renewal requires an accepted commercial arrangement that makes the merchant's renewal commitment clear.

Main Street SHALL monitor material renewal state where provider evidence permits.

The merchant SHOULD be interrupted only where action is required.

Examples:

```text
renewal payment requires action
registrar authorisation expired
domain entered provider-defined grace state
transfer prevents normal renewal
```

Provider-specific expiry, grace and redemption semantics SHALL remain provider/registry evidence and SHALL NOT be invented by Main Street.

---

# 19. Renewal Failure Does Not Rewrite History

If a domain expires, is suspended or otherwise ceases to be usable:

```text
historical domain relationship remains historical fact

but

new website/email externalisation through that domain
must fail closed where current control/readiness is absent
```

Domain failure SHALL NOT:

```text
delete Merchant Scope
delete Orders
delete Bookings
delete Conversations
rewrite previous Notifications
erase historical sender evidence
```

---

# 20. Domain Control Evidence

A merchant-controlled domain SHALL require deterministic control evidence before Main Street treats it as available for protected infrastructure use.

Evidence may arise through:

```text
supported registrar/DNS authorisation
DNS challenge
provider-supported equivalent proof
```

The exact physical proof mechanism is infrastructure/configuration detail.

Domain-control evidence establishes only:

> Main Street currently has adequate evidence that this Merchant Scope is authorised to use the domain for the accepted purpose.

It SHALL NOT establish:

```text
legal entity identity
trading-name verification
regulatory status
controller identity
ownership of every subdomain
ownership of every existing mailbox
```

---

# 21. Domain Control Is Purpose-Bounded

Control over:

```text
bellasalon.co.uk
```

does not automatically grant unrestricted authority over every existing service beneath it.

A domain connection SHALL maintain purpose-qualified administration rights.

Examples:

```text
website binding
email sender authentication
technical reply routing
certificate provisioning
```

Main Street MUST NOT infer authority for unrelated infrastructure.

---

# 22. Domain Uniqueness and Merchant Scope

An exact active public hostname SHALL resolve to at most one Merchant Scope at a time.

The initial custom-domain portfolio SHALL also prevent one merchant-controlled primary namespace from being simultaneously claimed as the independent primary brand namespace of multiple Merchant Scopes.

A second merchant claiming the same domain SHALL NOT succeed merely because:

```text
business names are similar
controller identity overlaps
DNS still points to Main Street
```

A separately designed shared-domain model may be introduced later if target-business evidence justifies it.

---

# 23. Domain Is Not Tenant Identity

A custom hostname resolves Merchant Scope at a trusted routing boundary.

It does not define the tenant.

Canonical:

```text
bellasalon.co.uk
        ↓
trusted active hostname binding
        ↓
Merchant Scope = M123
```

Rejected:

```text
tenant identity = "bellasalon.co.uk"
```

The merchant can change domains without becoming a different Merchant Scope.

---

# 24. Website Hostname Binding

A **Website Hostname Binding** associates an authorised hostname with the merchant's Storefront delivery surface.

Conceptually:

```text
WebsiteHostnameBinding
{
    merchantScope
    merchantBrandNamespace
    hostname
    canonical/alias role
    exposure/use purpose
    control-evidence affinity
    binding lifecycle
    provenance
}
```

This is conceptual and does not mandate one Java aggregate.

The hostname binding SHALL NOT own:

```text
website content
Offering truth
Booking
Ordering
Availability
Publication
merchant profile facts
```

Storefront remains the projection/interactions authority.

---

# 25. Primary Hostname and Redirect Alias

The initial service supports one merchant custom-domain primary website hostname.

Where appropriate, one equivalent canonical alias may redirect to it.

Example:

```text
bellasalon.co.uk
        primary

www.bellasalon.co.uk
        canonical redirect
```

The exact choice of apex versus `www` is infrastructure/presentation policy provided that:

```text
one canonical public identity is preserved
no redirect crosses Merchant Scope
HTTPS remains valid
```

---

# 26. TLS and HTTPS

Main Street SHALL provide HTTPS for a Main Street-managed website binding where the fulfilment path supports it.

Certificate provisioning/renewal is infrastructure.

Merchants SHALL NOT be expected to administer certificates.

Certificate possession does not create domain authority; the domain-control/binding authority must already exist.

---

# 27. No Dangling-Domain Cross-Tenant Routing

If DNS continues to point at Main Street after a merchant's hostname binding has been removed:

```text
Main Street MUST NOT
route the hostname to another merchant
or expose another merchant's storefront.
```

An unbound hostname SHALL fail safely.

A later merchant may use that hostname only after new valid control evidence and binding authority are established.

---

# 28. Merchant Authentication Origin Boundary

A customer-facing custom domain SHALL NOT automatically become an authorised merchant/staff authentication origin.

Canonical:

```text
bellasalon.co.uk
    may host customer storefront

but

does not automatically host
privileged Main Street merchant authentication
```

Authentication-origin policy remains governed by the applicable Identity/Security authority.

Domain control cannot silently enlarge staff/controller authentication trust.

---

# 29. Business Email Identity

A **Business Email Identity** is a merchant-branded sender identity used by an accepted Main Street communication responsibility.

Example:

```text
bookings@bellasalon.co.uk
```

Conceptually:

```text
BusinessEmailIdentity
{
    merchantScope
    brandNamespace
    semantic communication role
    public sender address
    display-name projection
    sender-authentication affinity
    lifecycle
    provenance
}
```

Business Email Identity owns the public sender identity relationship.

It does not own the communication itself.

---

# 30. Business Email Identity Is Not a Mailbox

Hard invariant:

```text
bookings@bellasalon.co.uk
        ≠
generic mailbox
```

MS-PROT-088 does not create:

```text
Inbox
Sent
Drafts
Spam
folders
IMAP
POP
generic webmail
mail search
calendar
contacts
shared mailbox administration
```

Main Street is not becoming Google Workspace or Microsoft 365.

---

# 31. Business Email Identity Roles

Business Email Identity SHALL be role-oriented rather than mailbox-oriented.

Initial semantic roles may include:

```text
GENERAL_BUSINESS
BOOKING_APPOINTMENT
ORDERING
CUSTOMER_SERVICE
```

The exact supported public local-part vocabulary is release/configuration authority.

Examples might render as:

```text
hello@
bookings@
orders@
support@
```

but the literal word is not the semantic role.

Main Street MAY recommend an appropriate supported address automatically from the merchant's active operating model.

The merchant SHALL NOT need to design an email-account hierarchy.

---

# 32. Capability-Derived Email Identity Availability

A role-specific business email identity SHOULD be offered only where the corresponding business/customer communication responsibility exists.

Conceptually:

```text
general merchant communication
    → GENERAL_BUSINESS identity

Booking / Appointment communication
    → BOOKING_APPOINTMENT identity

Ordering communication
    → ORDERING identity

Customer Messaging / service communication
    → CUSTOMER_SERVICE identity
```

Creating an email address SHALL NOT activate the underlying capability.

---

# 33. Platform-Delegated Business Email

Where commercially offered, a merchant without a custom domain MAY receive business email sender identities beneath a Main Street-controlled namespace.

Example:

```text
bookings@bella.mainstreet.app
```

Such an address:

```text
is merchant-branded
is platform-managed
is provider-independent
is not merchant-owned
is not externally portable as a domain
```

A later migration to:

```text
bookings@bellasalon.co.uk
```

SHALL NOT require changing Booking, Notification or Conversation semantics.

---

# 34. Merchant-Owned Business Email

For a verified merchant-controlled domain, Main Street MAY establish branded sender identities such as:

```text
hello@bellasalon.co.uk
bookings@bellasalon.co.uk
orders@bellasalon.co.uk
support@bellasalon.co.uk
```

subject to:

```text
current domain authority
current email-authentication readiness
commercial entitlement
source communication authority
provider fulfilment readiness
security/protection requirements
```

Possession of an email-like string is insufficient.

---

# 35. Existing Email Hosting Must Coexist

If the merchant already uses:

```text
Google Workspace
Microsoft 365
Zoho
another mailbox provider
```

Main Street SHALL preserve that infrastructure by default.

Specifically:

> **Main Street SHALL NOT replace the merchant's root-domain MX routing merely to support branded Main Street sending.**

Existing addresses such as:

```text
owner@bellasalon.co.uk
accounts@bellasalon.co.uk
```

must continue functioning unless the merchant independently chooses a future Main Street mailbox-hosting service under a separately accepted authority.

---

# 36. Sender Authentication

Main Street SHALL establish the technical authentication required for a Business Email Identity through the appropriate infrastructure/provider fulfilment paths.

Relevant infrastructure may include:

```text
DKIM
SPF / MAIL FROM
DMARC compatibility
provider identity verification
bounce routing
```

These technical mechanisms SHALL NOT be exposed as ordinary merchant configuration.

Main Street SHALL NOT silently weaken or replace an existing domain email-security policy merely to make its provider easier to configure.

Where an existing policy conflicts with the proposed sending path, Main Street SHALL either configure a compatible path or surface the minimum merchant action required.

---

# 37. Envelope Infrastructure Is Not Public Identity

Technical addresses such as:

```text
bounce.bellasalon.co.uk
```

or provider-specific envelope identities are delivery infrastructure.

They SHALL NOT become the merchant's canonical public business email identity.

Canonical:

```text
Public From:
bookings@bellasalon.co.uk

Technical fulfilment:
provider-specific MAIL FROM / bounce identity
```

Changing delivery provider SHALL NOT require changing the merchant's public Business Email Identity where the replacement fulfiller can satisfy the accepted contract.

---

# 38. Conversation-Bound Replies

MS-PROT-086 remains authoritative for Conversation-bound email continuation.

Where a Conversation email is sent:

```text
From:
bookings@bellasalon.co.uk

Reply-To:
opaque Conversation-bound route
```

MAY be used where accepted Customer Messaging infrastructure requires it.

The reply route may use a dedicated technical subdomain such as:

```text
reply.bellasalon.co.uk
```

without replacing root-domain MX.

Business Email Identity remains the branded sender.

The reply route remains Customer Messaging correlation infrastructure.

---

# 39. No Generic Inbound Mail From This Authority

MS-PROT-088 SHALL NOT permit:

```text
customer sends arbitrary new email
to support@bellasalon.co.uk
        ↓
automatically create Conversation
```

unless a separately accepted Customer Messaging authority later permits that operation.

Therefore:

```text
Business Email Identity
    ≠ generic inbound-email capability
```

Main Street MUST NOT advertise an address as a monitored general-purpose inbound mailbox unless an accepted authority actually provides that behaviour.

---

# 40. Reply Behaviour Must Not Mislead

When an outbound Business Email Identity does not support ordinary inbound replies, Main Street SHALL ensure the communication surface does not falsely imply that replies are monitored.

Where Conversation-bound reply is supported, the accepted reply route SHALL be used.

Where no reply authority exists, the communication SHALL use an appropriate non-reply treatment or another accepted customer-contact path.

---

# 41. Address Conflicts

Main Street SHALL NOT assume exclusive ownership of a local part merely because it can authenticate the domain.

If:

```text
bookings@bellasalon.co.uk
```

already participates in the merchant's existing email system, Main Street must preserve the existing mailbox/routing semantics unless the merchant explicitly authorises a compatible use.

A conflict SHALL NOT cause Main Street to delete or redirect existing mail infrastructure automatically.

---

# 42. Current-State Revalidation Before Email Externalisation

Before sending through a merchant-controlled Business Email Identity, the applicable execution path SHALL revalidate at least:

```text
Merchant Scope
active Business Email Identity binding
current domain/control eligibility
current sender-authentication readiness
applicable commercial entitlement
originating communication authority
Notification/contact policy
provider fulfilment/readiness
applicable protection constraints
```

A historically verified domain does not guarantee current authority forever.

---

# 43. Loss of Domain or Sender Readiness

If current domain/sender readiness becomes unavailable:

```text
new branded externalisation through that identity
    → fail closed
```

Main Street SHALL NOT fabricate successful delivery.

Main Street also SHALL NOT silently substitute a different public sender identity where that substitution would materially alter merchant branding or communication expectation.

An independently authorised fallback identity MAY be used only under the applicable Notification/source contract.

---

# 44. Provider Neutrality

Merchant Brand Infrastructure SHALL define provider-neutral fulfilment responsibilities.

Relevant responsibilities may include:

```text
domain-registration
domain-management
DNS-management
certificate-management
email-sender-authentication
```

A single provider may fulfil several responsibilities.

No provider is required to do so.

Canonical:

```text
Merchant Brand Infrastructure authority
        ↓
required infrastructure responsibility
        ↓
exact fulfilment binding
        ↓
provider/internal fulfiller
```

Registrar, DNS, certificate and email-delivery vendors SHALL NOT become semantic owners of the merchant's brand namespace.

---

# 45. Provider Credentials

Provider credentials and authorisation tokens remain governed by MS-PROT-067.

Domain-provider access:

```text
proves/enables bounded technical access
```

but does not create:

```text
Merchant Scope
merchant legal identity
domain semantic ownership
commercial entitlement
website publication authority
email communication authority
```

Credential loss may make a management path unavailable without rewriting historical domain facts.

---

# 46. Commercial Entitlement

Merchant Brand Infrastructure may be commercially restricted through MS-PROT-056.

Examples:

```text
custom-domain service
merchant-branded email service
managed registration service
```

may each be commercially packaged.

MS-PROT-088 does not set prices or plan names.

Hard rule:

```text
commercial entitlement
    permits service use

but does not
    register a domain
    verify control
    alter DNS
    create sender authority
```

---

# 47. Entitlement Loss and Merchant Domain Rights

Loss of a Main Street commercial entitlement may make new Main Street custom-domain/email service unavailable according to commercial policy.

It SHALL NOT:

```text
transfer merchant domain rights to Main Street
delete registrar history
prevent legitimate transfer out
cancel a domain merely because a Main Street feature became unavailable
```

Where renewal or registration is separately paid/contracted, those commercial obligations must be treated according to their own accepted terms.

---

# 48. Disconnect

A merchant may request that a custom domain stop serving one or more Main Street functions.

The service MUST distinguish:

```text
disconnect website binding

disconnect branded email sending

disconnect Main Street DNS management

transfer registrar management out

release entire Main Street domain relationship
```

These are not equivalent operations.

A merchant may, for example, stop using Main Street's website while continuing to use Main Street business email if the applicable commercial and technical authorities allow it.

---

# 49. Safe DNS Cleanup

Where Main Street created identifiable DNS records for its own infrastructure, disconnection MAY remove or retire those exact records where safe and authorised.

Main Street SHALL NOT perform blanket zone deletion.

Existing unrelated merchant records SHALL be preserved.

Where removal could break an external service unexpectedly, the operation must surface the consequence before execution.

---

# 50. Domain Relationship Termination

Full termination of a merchant-controlled domain relationship requires:

```text
no active Main Street public hostname binding
no active Main Street branded sender binding
no remaining required Main Street technical subdomain
no unresolved transfer/registration effect
no external obligation requiring bounded reconciliation
```

Historical evidence may remain according to MS-PROT-053 and applicable audit/security authority.

Termination of the Main Street relationship does not mean the domain ceases to exist.

---

# 51. Protected Registrant Information

Domain registration may require personal or legally identifying registrant data.

Main Street SHALL collect, retain and expose only what is required for:

```text
registration
provider/registry compliance
renewal
transfer
security
accountability
```

Registrant data SHALL NOT become general Merchant Profile or Marketing data merely because Main Street possesses it.

Provider privacy/proxy services may be used where lawful and compatible.

---

# 52. Merchant-Facing Administration

The ordinary Merchant Brand Infrastructure surface SHOULD communicate outcomes such as:

```text
bellasalon.co.uk
Connected

Website
Ready

Business email
Ready
```

or:

```text
Your domain needs attention.

Reconnect your domain provider.
```

It SHOULD NOT ordinarily expose:

```text
zone files
DNS record tables
DKIM selectors
MX priority
certificate chains
TTL
provider API scopes
webhook identifiers
```

Technical diagnostics may exist for authorised support/engineering purposes without becoming ordinary merchant administration.

---

# 53. Exception-Driven Operation

Routine infrastructure maintenance SHALL be automated where safe.

Merchants should be interrupted primarily for:

```text
approval
purchase
material cutover
external reauthorisation
renewal failure
ownership/control conflict
security issue
transfer action
unresolvable provider condition
```

Background renewal checks, certificate renewal, provider status reconciliation and compatible DNS maintenance SHOULD remain invisible when successful.

---

# 54. Actor Authority

Domain purchase, registrar transfer, domain release, material DNS cutover and equivalent high-impact operations SHALL require appropriately authorised merchant/controller authority.

Ordinary staff SHALL NOT gain domain-management authority merely because they can:

```text
manage bookings
fulfil orders
answer customer messages
```

Domain administration is not an ordinary frontline-staff operation.

---

# 55. AI Boundary

AI MAY:

```text
suggest available-looking domain names
explain domain choices in business language
recommend a supported sender role
summarise an infrastructure problem
guide the merchant through an exception
```

AI SHALL NOT:

```text
declare a domain available without provider evidence
declare control verified without deterministic proof
register a domain without required approval
transfer registrar management without required approval
modify DNS merely from inferred intent
invent successful certificate/email authentication
override a conflict
claim domain legal ownership
```

AI may reduce administrative burden.

It does not become domain authority.

---

# 56. No Hidden Web/Email Coupling

The following must remain valid:

```text
custom website active
+
existing Microsoft 365 email retained
```

and:

```text
existing third-party website retained
+
Main Street branded email active
```

and:

```text
Main Street website
+
Main Street branded email
```

The brand infrastructure is coherent because Main Street coordinates it through one merchant experience.

It is not monolithic.

---

# 57. Failure Semantics

Where recovery behaviour differs, Main Street SHALL preserve distinctions such as:

```text
DOMAIN_CONTROL_NOT_VERIFIED

DOMAIN_ALREADY_BOUND

DNS_CHANGE_REQUIRED

DNS_CONFLICT

REGISTRATION_PROVIDER_NOT_READY

REGISTRATION_EFFECT_UNRESOLVED

TRANSFER_PENDING

TRANSFER_REQUIRES_MERCHANT_ACTION

WEBSITE_BINDING_NOT_READY

SENDER_IDENTITY_NOT_READY

COMMERCIAL_ACCESS_UNAVAILABLE
```

The exact API representation is downstream.

A generic `domain failed` state SHALL NOT erase materially different recovery paths.

---

# 58. No Universal Domain Status Boolean

Rejected:

```text
domainActive = true|false
```

as the complete model.

A merchant domain may simultaneously have:

```text
control verified              YES
registrar managed by MS       NO
website binding               YES
email sender binding          NO
commercial website access     YES
email provider readiness      NO
renewal action required       YES
```

These facts retain separate authority.

---

# 59. Falsification — Existing Google Workspace Merchant

Scenario:

```text
bellasalon.co.uk

existing:
owner@bellasalon.co.uk
accounts@bellasalon.co.uk
Google Workspace MX
```

Merchant connects Main Street website and branded booking emails.

Expected:

```text
existing MX preserved
existing mailboxes preserved
website binding explicitly authorised
sender authentication added compatibly
Conversation reply infrastructure uses bounded route
```

**PASS**

---

# 60. Falsification — Existing Website, Email Only

Scenario:

```text
merchant already has WordPress at bellasalon.co.uk
wants Main Street business email only
```

Expected:

```text
domain verified
WordPress routing preserved
no website cutover
business-email identity may activate independently
```

**PASS**

---

# 61. Falsification — Website Only

Scenario:

```text
merchant wants Main Street storefront
but keeps Microsoft 365 for all email
```

Expected:

```text
website hostname may bind
root email routing preserved
no requirement to activate Main Street business email
```

**PASS**

---

# 62. Falsification — New Domain Registration Acknowledgement Lost

Scenario:

```text
registrar may have successfully registered domain
network response lost
```

Expected:

```text
registration outcome unresolved
Main Street reconciles provider/registry evidence
no blind second registration
```

**PASS**

---

# 63. Falsification — Duplicate Merchant Claim

Scenario:

```text
Merchant A actively uses bellasalon.co.uk

Merchant B attempts to claim same domain
```

Expected:

```text
Merchant B does not receive routing or sender authority
new proof/control resolution required
no cross-tenant exposure
```

**PASS**

---

# 64. Falsification — Dangling DNS

Scenario:

```text
Merchant A disconnects
DNS still points at Main Street
```

Expected:

```text
hostname does not route to Merchant B
hostname does not expose Merchant A after binding removal
safe unbound response
```

**PASS**

---

# 65. Falsification — Subscription Cancellation

Scenario:

```text
Main Street paid entitlement expires
merchant domain was registered through Main Street
```

Expected:

```text
Main Street custom-domain service may become commercially unavailable
merchant retains transfer/take-control path
domain is not confiscated
historical business state remains
```

**PASS**

---

# 66. Falsification — Registrar Access Revoked

Scenario:

```text
merchant revokes Main Street registrar/DNS provider authorisation
```

Expected:

```text
management readiness degrades
historical domain relationship remains
website/email continue only where existing infrastructure remains independently valid
merchant receives action request only where needed
```

**PASS**

---

# 67. Falsification — Branded Sender Authentication Breaks

Scenario:

```text
DKIM/provider authentication no longer valid
```

Expected:

```text
new send through branded identity blocked
no fabricated readiness
Notification remains semantically valid
another separately authorised sender may be considered only under owning communication policy
```

**PASS**

---

# 68. Falsification — Arbitrary Inbound Email

Scenario:

```text
customer independently emails support@bellasalon.co.uk
without an existing Conversation
```

Expected under initial authority:

```text
MS-PROT-088 does not manufacture Conversation creation
```

**PASS**

---

# 69. Falsification — Ordinary Employee

Scenario:

A salon receptionist can manage appointments but is asked to transfer the domain.

Expected:

```text
domain-transfer operation unavailable
no DNS/registrar concepts exposed
authorised merchant/controller action required
```

**PASS**

---

# 70. Falsification — Low-Software-Capacity Merchant

Scenario:

Merchant says:

```text
"I already own bellasalon.co.uk.
I want Main Street to use it."
```

Expected merchant journey:

```text
enter domain
        ↓
authenticate provider or perform one bounded verification step
        ↓
review any material website/email change
        ↓
approve
        ↓
Main Street configures infrastructure
        ↓
merchant sees Ready / Action required
```

Merchant is not required to become a DNS administrator.

**PASS**

---

# 71. Falsification — AI Suggests Unavailable Domain

Scenario:

AI suggests:

```text
bellasalon.co.uk
```

but it is unavailable.

Expected:

```text
provider/registry evidence rejects availability
AI suggestion has no registration authority
alternative suggestions presented
```

**PASS**

---

# 72. ERP/Product-Suite Drift Test

The proposal does not add:

```text
generic email hosting
office suite
DNS control panel
domain-reseller marketplace
website builder administration
CRM
contact centre
generic IT-management console
```

It adds only the infrastructure depth necessary to provide a coherent merchant public identity across Main Street's existing customer-facing surfaces.

**PASS**

---

# 73. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

Domain registration, DNS, email authentication, certificate management, renewal and provider reconciliation contain significant intrinsic technical complexity.

That complexity is justified because it is required to operate a reliable public business identity.

The proposal remains vision-conforming because Main Street absorbs that complexity rather than transferring it to the merchant.

Merchant experience:

```text
I already have a domain.
        or
I need a domain.

        ↓

Main Street

        ↓

Website ready
Business email ready
```

instead of:

```text
Merchant becomes
registrar administrator
DNS engineer
certificate administrator
email deliverability engineer
```

---

# 74. Hard Invariants

```text
INV-088-001
A domain never defines Merchant Scope.

INV-088-002
Connecting a domain does not require registrar transfer.

INV-088-003
Domain verification does not establish legal-business or controller identity.

INV-088-004
Domain connection is additive by default and MUST NOT silently destroy existing website/email infrastructure.

INV-088-005
Website binding and business-email binding are independently activatable.

INV-088-006
A merchant-controlled domain remains portable away from Main Street.

INV-088-007
Main Street subscription loss does not transfer merchant domain rights to Main Street.

INV-088-008
An exact active public hostname maps to at most one Merchant Scope.

INV-088-009
Dangling DNS cannot route to another merchant.

INV-088-010
A Business Email Identity is not a mailbox.

INV-088-011
Existing root-domain MX SHALL NOT be replaced merely to enable Main Street branded sending.

INV-088-012
Provider identity does not define Merchant Brand Namespace meaning.

INV-088-013
Changing email-delivery provider need not change the public Business Email Identity.

INV-088-014
Conversation-bound reply routing remains Customer Messaging infrastructure.

INV-088-015
Generic inbound email is not authorised by this contract.

INV-088-016
Current sender/domain readiness is revalidated before branded externalisation.

INV-088-017
Custom storefront domains do not become privileged merchant authentication origins merely through routing.

INV-088-018
Domain registration uncertainty requires reconciliation rather than blind repetition.

INV-088-019
Main Street SHALL NOT expose ordinary merchants to routine DNS/registrar administration.

INV-088-020
AI cannot create domain, sender, registrar or DNS authority.
```

---

# 75. Rejected Alternatives

Rejected:

```text
domain = merchant identity

mandatory registrar transfer

custom domain as a field on BusinessProfile

domain connection automatically replaces all DNS

one "domainActive" Boolean

root MX replacement merely for branded sending

Main Street generic mailbox hosting in v1.0

email provider owns public sender identity

provider thread owns Conversation

custom domain automatically trusted for merchant authentication

subscription cancellation confiscates domain

AI decides domain availability/control

merchant manually administers DNS during routine operation

one all-or-nothing website+email domain activation
```

---

# 76. Initial Scope Deliberately Excluded

The following do not belong to MS-PROT-088 v1.0:

```text
generic mailbox hosting
IMAP/POP/webmail
generic arbitrary inbound-email Conversation creation
multi-merchant shared-domain architecture
large arbitrary multi-domain portfolios
wildcard merchant-controlled public domains
enterprise DNS administration
domain brokerage/resale marketplace
SEO semantics
website-content semantics
Marketing permission
Notification semantics
Conversation semantics
pricing/plan definitions
specific registrar vendor
specific DNS vendor
specific email-delivery vendor
specific certificate vendor
```

A future need in these areas requires the applicable owner or ordinary feature admission process.

---

# 77. Release and Implementation Decisions That Are Not Semantic DQs

The following may be selected under their existing owning authorities without reopening MS-PROT-088:

```text
initial registrar provider
initial supported TLD registration catalogue
DNS provider implementation
certificate provider
email-delivery provider
exact DKIM/SPF record representation
exact DNS automation mechanism
provider OAuth scopes
exact domain verification token representation
bounded public email local-part vocabulary
exact retry/backoff implementation
exact REST/API representation
database schema
worker/queue implementation
commercial price
plan packaging
```

The semantic contract determines what those choices must preserve.

---

# 78. Deferred-Decision Outcome

MS-PROT-088 v1.0 requires no unresolved semantic DQ before implementation of the defined initial portfolio.

Future features such as:

```text
generic mailbox hosting
arbitrary inbound custom-domain email
multi-domain portfolios
shared corporate domains across Merchant Scopes
```

are not hidden deferred implementation work.

They are new feature boundaries requiring independent admission if promoted.

---

# 79. Implementation-Rules Impact

Acceptance of MS-PROT-088 SHALL NOT itself activate production functionality.

Implementation remains separately governed.

Any implementation must preserve at minimum:

```text
tenant/domain separation
safe domain uniqueness
durable external-effect identities
provider uncertainty reconciliation
credential isolation
non-destructive DNS mutation
sender/domain currentness checks
portability
auditability
no generic mailbox semantics
```

No amendment to `designs/IMPLEMENTATION-RULES.md` is required by this authority because these are authority-specific implementation obligations rather than a new cross-programme implementation-governance principle.

---

# 80. Governance Verdict

**ACCEPTED**

MS-PROT-088 establishes the smallest coherent Merchant Brand Infrastructure model that:

```text
gives merchants a professional public identity
unifies custom domain and business email management
preserves existing external infrastructure
keeps provider implementations replaceable
prevents domain/email concepts from stealing other semantic authority
preserves merchant portability
and hides infrastructure administration from micro/small merchants
```

The design passed Feature Admission, Fundamental Vision Conformance, ownership review, anti-ERP review and the stated falsification scenarios, and was explicitly manually approved on 9 September 2026.
