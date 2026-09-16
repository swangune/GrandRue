# MS-PROT-052 v1.1 — Baseline Public Presence & Discovery-Axis Amendment

**Document ID:** MS-PROT-052  
**Version:** 1.1  
**Status:** **ACCEPTED after continuous-improvement review and manual approval**  
**Amends:** MS-PROT-052 v1.0 — Adaptive Onboarding Prompt, Question Catalogue & Decision Presentation Contract  
**Depends on:** MS-PROT-027 v1.1, MS-PROT-036 v1.1, MS-PROT-038 v1.0, MS-PROT-039 v1.2, MS-PROT-047 v1.0, MS-PROT-050 v1.2, MS-PROT-051 v1.0, MS-PROT-052 v1.0  
**Purpose:** Remove basic merchant public presence from capability-style discovery, establish Merchant Profile/storefront information as a valid baseline public experience, and refine the initial customer-interaction discovery question so it discovers only additional customer interaction semantics beyond that baseline.

---

# 1. Governing amendment

MS-PROT-052 v1.0 remains accepted except where this amendment changes the initial discovery model and its related examples/invariants.

The governing decision is:

> **Basic merchant public presence is not a capability-style discovery outcome. A merchant may validly publish audience-safe Merchant Profile information through the Main Street storefront without selecting any additional customer-interaction semantic. Initial onboarding discovery therefore asks what visitors should be able to do beyond viewing the public business information the merchant chooses to expose.**

Canonical separation:

```text
MERCHANT PUBLIC PRESENCE BASELINE
    Merchant public descriptor
    public contact points where chosen
    public Merchant Locations where chosen
    public service-area information where chosen
    Public Business Hours where configured
    public external-presence links where chosen
        ↓
    audience-safe storefront projection

        ≠

ADDITIONAL CUSTOMER INTERACTION SEMANTICS
    Publication
    Enquiry
    Appointment/Scheduling-related interaction
    Booking/reservation
    Ordering
    Subscription
    other supported semantics
```

Hard invariant:

> **A merchant does not need a synthetic `VIEW_BUSINESS_INFORMATION` capability, discovery seed or runtime semantic merely to have a Main Street public presence.**

---

# 2. Why v1.1 is required

MS-PROT-052 v1.0 correctly chose customer interaction as the preferred high-information discovery axis.

However, its initial option set included:

```text
VIEW_BUSINESS_INFORMATION
    "Learn about my business and how to contact or visit me"
```

That option conflated two different architectural concerns:

```text
basic public Merchant Profile projection
        ≠
additional customer interaction capability discovery
```

MS-PROT-051 already establishes Merchant Profile/public presence as a structured, exposure-aware source of public merchant information.

MS-PROT-036 and MS-PROT-027 already establish storefront/public projection as a composed read experience rather than a business capability whose existence must be discovered through an interaction option.

If `VIEW_BUSINESS_INFORMATION` remained inside the discovery catalogue, implementations could be tempted to introduce:

```text
VIEW_BUSINESS_INFORMATION capability
```

or:

```text
public-presence semantic seed
```

merely to satisfy an onboarding answer.

That would add architecture where none is required.

This amendment removes that pressure.

---

# 3. Baseline public presence

A valid Main Street merchant may operate with only its public presence baseline and no additional customer interaction semantics.

Examples of baseline public information may include, where configured and exposed:

```text
display name
approved business description
public telephone
public email
public Merchant Location
public service area
Public Business Hours
public external-profile links
branding/presentation
```

The exact set is contextual.

No merchant is required to publish every possible profile field.

Canonical:

```text
Merchant Profile authorities
        +
Exposure
        +
Presentation configuration
        ↓
Public merchant storefront
```

No additional capability is necessary solely to render this information.

---

# 4. Baseline presence is not a hidden capability

Rejected:

```text
Merchant wants a simple brochure website
        ↓
activate VIEW_BUSINESS_INFORMATION
        ↓
public website allowed
```

Accepted:

```text
Merchant has valid public profile facts
        +
public exposure
        ↓
storefront projects those facts
```

The storefront itself remains a platform/public projection concern under MS-PROT-036.

Hard rule:

> **Projection eligibility shall not be manufactured as a merchant-selected business capability where the accepted architecture already provides a baseline public projection.**

---

# 5. Refined initial discovery question

MS-PROT-052 v1.0 section 30 is amended.

The preferred initial high-information discovery question becomes conceptually:

> **Besides learning about your business and seeing the public information you choose to show, what else should visitors be able to do through your Main Street website? Select all that apply.**

Stable conceptual option identities:

```text
PUBLISH_INFORMATION
SEND_ENQUIRY
ARRANGE_APPOINTMENT
RESERVE_SUBJECT
PLACE_ORDER
SUBSCRIBE_UPDATES
NOTHING_ELSE_FOR_NOW
OTHER
```

Possible presentation:

```text
[ ] Read information, updates or opportunities I publish
[ ] Send me an enquiry
[ ] Arrange an appointment or agreed service time
[ ] Reserve something for a date or period
[ ] Place an order or request to buy something
[ ] Subscribe for future updates
[ ] Nothing else for now
[ ] Something else
```

`VIEW_BUSINESS_INFORMATION` is removed from the initial discovery option set.

---

# 6. NOTHING_ELSE_FOR_NOW

`NOTHING_ELSE_FOR_NOW` is an onboarding-control outcome.

It means:

> **The merchant does not currently request any additional customer-interaction semantic from this discovery question.**

It does not mean:

```text
merchant has no website
merchant has no public presence
merchant is inactive
merchant is incomplete
merchant cannot later add capabilities
```

It also does not become:

```text
capability = NOTHING_ELSE_FOR_NOW
configurationValue = NOTHING_ELSE_FOR_NOW
runtimeState = NOTHING_ELSE_FOR_NOW
```

Hard invariant:

> **`NOTHING_ELSE_FOR_NOW` resolves the discovery branch without creating executable semantics.**

---

# 7. Mutual-exclusion rule

Within the initial discovery prompt, `NOTHING_ELSE_FOR_NOW` is mutually exclusive with positive additional-interaction selections.

Invalid answer set:

```text
NOTHING_ELSE_FOR_NOW
+
SEND_ENQUIRY
```

because the meanings contradict each other in the same question context.

The presentation layer shall prevent or resolve such a contradictory selection.

`OTHER` may be selected separately where the merchant is describing an additional desired interaction not represented by the listed options.

If `OTHER` expresses material intent, `NOTHING_ELSE_FOR_NOW` no longer applies.

---

# 8. Discovery mapping refinement

MS-PROT-052 v1.0 sections 5 and 14 are refined.

A Discovery Question does not necessarily produce a capability seed for every valid answer.

The canonical flow is now:

```text
Discovery answer
        ↓
registered DiscoveryMapping / discovery outcome handling
        ↓
one of:
    candidate semantic seed(s)
    no additional semantic seed
    further bounded clarification
    CONFIGURATION GAP
        ↓ where seeds exist
registered dependency resolution
```

Examples:

```text
SEND_ENQUIRY
    → candidate Enquiry semantics

PUBLISH_INFORMATION
    → candidate Publication semantics

NOTHING_ELSE_FOR_NOW
    → no additional semantic seed

OTHER
    → clarification / existing supported mapping / CONFIGURATION GAP
```

Hard rule:

> **A valid discovery outcome may legitimately produce no new semantic seed.**

---

# 9. Discovery mapping is broader than capability mapping

A registered discovery mapping exists to interpret onboarding evidence consistently.

It must not be defined as:

> every option maps to one or more capabilities.

A mapping may instead produce a bounded discovery consequence such as:

```text
NO_ADDITIONAL_INTERACTION_SEMANTICS
```

or trigger contextual clarification.

This consequence remains onboarding/configuration-proposal state, not runtime semantics.

The distinction prevents the discovery registry from becoming an artificial capability taxonomy.

---

# 10. Merchant public-presence data remains separate from Q1

Removing `VIEW_BUSINESS_INFORMATION` from Q1 does not remove Merchant Profile onboarding.

Profile data may still be collected progressively through Data Capture Prompts and profile decisions under MS-PROT-051 and MS-PROT-050.

Examples:

```text
What should we call your business publicly?
    → profile data capture

Would you like to show customers a business location?
    → profile-presence decision

Do you want customers to see when your business is normally open?
    → Public Business Hours applicability/profile decision
```

These do not need to masquerade as customer-interaction discovery options.

---

# 11. Brochure-style merchant is first-class

Example:

```text
Merchant:
local tradesperson

Public profile:
    display name
    description
    telephone
    service area
    Business Hours

Additional customer interactions:
    NONE
```

Initial discovery answer:

```text
NOTHING_ELSE_FOR_NOW
```

Result:

```text
public storefront present
public profile information visible according to exposure
no Main Street Enquiry form
no Scheduling
no Booking
no Ordering
no Subscription
```

This is a valid Main Street merchant configuration.

**PASS**

---

# 12. Information-only merchant

Example:

```text
Merchant:
organisation presenting its own public information

Public profile:
    display name
    description
    contact point

No separately managed articles/opportunities/announcements
No Enquiry capability
```

Discovery:

```text
NOTHING_ELSE_FOR_NOW
```

Result:

- storefront remains valid;
- no fake Publication capability is created merely because the website contains merchant profile copy;
- public business description remains Merchant Profile content under MS-PROT-051.

**PASS**

---

# 13. Scholarship publisher

Example:

```text
Public profile baseline:
    display name
    description

Additional interactions:
    PUBLISH_INFORMATION
    SEND_ENQUIRY
    SUBSCRIBE_UPDATES
```

Result:

- profile baseline does not need its own discovery option;
- Publication/Enquiry/Subscription-related semantic needs remain discoverable;
- the merchant's own business description is not confused with published scholarship Opportunity content.

**PASS**

---

# 14. Online consultant

Example:

```text
Public profile baseline:
    display name
    description
    optional public contact point

Additional interactions:
    SEND_ENQUIRY
    ARRANGE_APPOINTMENT
```

Result:

- no `VIEW_BUSINESS_INFORMATION` semantic seed is required;
- Enquiry and Scheduling-related discovery remains unchanged;
- physical location remains optional.

**PASS**

---

# 15. Retailer

Example:

```text
Public profile baseline:
    merchant descriptor
    public shop location
    Public Business Hours

Additional interactions:
    PLACE_ORDER
    SEND_ENQUIRY
```

Result:

- profile/location/hours remain profile/presence authorities;
- Ordering/Enquiry remain separate discovered semantic areas;
- presence data does not activate commerce.

**PASS**

---

# 16. Motel

Example:

```text
Public profile baseline:
    descriptor
    location
    contact

Additional interactions:
    RESERVE_SUBJECT
    SEND_ENQUIRY
```

Result:

- ordinary public motel information is baseline projection;
- `RESERVE_SUBJECT` still discovers reservation/Booking-related intent without forcing Appointment semantics;
- no duplicate public-presence option is required.

**PASS**

---

# 17. Merchant evolution

A merchant that initially selects:

```text
NOTHING_ELSE_FOR_NOW
```

may later add:

```text
SEND_ENQUIRY
```

or:

```text
ARRANGE_APPOINTMENT
```

through the same discovery/configuration-evolution machinery.

The merchant does not migrate to another Main Street product or recreate its profile.

Canonical:

```text
Baseline public presence
        ↓ later
Additional semantic discovery
        ↓
configuration proposal
        ↓
validation / activation
```

---

# 18. Natural-language preselection under v1.1

Natural-language inference may continue to preselect the initial discovery options.

Example:

Merchant says:

> I just need a simple site showing what I do, my phone number and opening times.

Inference should not invent:

```text
VIEW_BUSINESS_INFORMATION
```

Instead it may propose profile data requirements and preselect:

```text
NOTHING_ELSE_FOR_NOW
```

subject to merchant review.

Example:

> I advertise scholarships and people can contact me with questions.

Inference may preselect:

```text
PUBLISH_INFORMATION
SEND_ENQUIRY
```

The same AI boundary from v1.0 remains unchanged.

---

# 19. Review / confirmation under v1.1

The final review should distinguish baseline public presence from additional customer interactions.

Example:

```text
Your public business information:
✓ business description
✓ public telephone
✓ service area
✓ opening hours

Customers can also:
– no additional Main Street interactions enabled
```

or:

```text
Your public business information:
✓ business description
✓ public location
✓ opening hours

Customers can also:
✓ send enquiries
✓ arrange appointments
```

This communicates the distinction without exposing internal capability vocabulary.

---

# 20. Revised initial option identities

The authoritative initial discovery option set is now:

```text
PUBLISH_INFORMATION
SEND_ENQUIRY
ARRANGE_APPOINTMENT
RESERVE_SUBJECT
PLACE_ORDER
SUBSCRIBE_UPDATES
NOTHING_ELSE_FOR_NOW
OTHER
```

`VIEW_BUSINESS_INFORMATION` remains historical v1.0 evidence only and shall not be used by new onboarding definitions under v1.1.

Historical answers recorded under v1.0 remain traceable through their question-definition version.

They shall not be silently rewritten or reinterpreted as though the merchant had answered v1.1.

---

# 21. Question-definition versioning consequence

Because the initial option set and interpretation materially change, the affected question definition must receive a new definition version.

Conceptually:

```text
questionIdentity:
    onboarding.discovery.customer-interactions

v1:
    included VIEW_BUSINESS_INFORMATION

v2:
    removes VIEW_BUSINESS_INFORMATION
    adds NOTHING_ELSE_FOR_NOW
    reframes prompt around additional interactions
```

This is precisely the type of change for which MS-PROT-052 v1.0 established question-definition versioning.

Hard invariant:

> **The stable question identity may remain the same while the material definition version changes.**

---

# 22. Historical answer handling

Suppose a historical v1 answer contains:

```text
VIEW_BUSINESS_INFORMATION
SEND_ENQUIRY
```

Main Street shall preserve that answer as historical onboarding evidence under the v1 question definition.

Current runtime/configuration authority remains the approved configuration produced from that earlier onboarding flow.

No runtime decision shall depend on remapping the old raw answer.

If the merchant later re-enters onboarding/evolution, current unresolved needs are evaluated using the current accepted prompt definitions.

---

# 23. Branch-pruning consequence

If a merchant selects only:

```text
NOTHING_ELSE_FOR_NOW
```

all prompts that exist solely for additional semantic branches shall be pruned.

Examples:

```text
Scheduling authority mode
Publication type
Subscription
External calendar
```

may become ineligible.

Profile enrichment/data prompts may remain eligible independently because Merchant Profile/public presence is not part of that additional-interaction branch.

This distinction is essential.

---

# 24. Prompt priority consequence

The adaptive priority model from v1.0 remains unchanged.

However, basic public-presence data capture shall not be forced to wait for a positive capability-discovery answer.

The engine may interleave profile setup and semantic discovery based on eligibility, requiredness and user experience.

For example:

```text
merchant display name
        → required profile data

additional customer interactions
        → high-branch discovery
```

Exact screen ordering remains presentation work.

---

# 25. Merchant category remains non-authoritative

This amendment does not restore merchant category as the onboarding root.

The discovery axis remains customer interaction, but now only **additional customer interaction beyond baseline public presence**.

Therefore:

```text
category = PLUMBER
```

still cannot imply:

```text
Enquiry
Scheduling
Payment
```

and:

```text
NOTHING_ELSE_FOR_NOW
```

still does not mean the merchant is category-less or invalid.

---

# 26. Storefront boundary

MS-PROT-036 remains authoritative for storefront composition.

Baseline public presence may produce a storefront containing only merchant-profile projections and presentation elements.

Example:

```text
/
    business name
    description
    service area
    Business Hours
    telephone
```

No capability-derived customer interaction component is required.

If Enquiry later becomes active, the storefront may add the corresponding public interaction contribution through normal composition.

---

# 27. Projection boundary

MS-PROT-027 remains authoritative:

```text
authoritative fact
        +
exposure
        ↓
public projection
```

A public profile baseline therefore does not mean all Merchant Profile facts are automatically visible.

The merchant may have private profile information while exposing only selected public facts.

This amendment changes discovery, not exposure policy.

---

# 28. Relationship to MS-PROT-051

MS-PROT-051 remains authoritative for Merchant Profile/public business information.

The baseline public-presence rule means:

```text
Merchant Profile public facts
        ↓
public projection
```

is sufficient to produce an informational storefront.

MS-PROT-052 discovers additional business interactions beyond that baseline.

Neither document acquires the other's authority.

---

# 29. Relationship to MS-PROT-049

Capability surface contributions remain relevant only where corresponding capabilities are active.

Baseline Merchant Profile projection is a platform/storefront composition input and shall not require manufacturing a capability surface contribution solely to make business descriptor/contact/location information visible.

Future accepted design may define dedicated non-capability platform-surface contribution machinery if needed, but v1.1 does not require it.

---

# 30. Relationship to Enquiry

A merchant with:

```text
public phone
```

may select:

```text
NOTHING_ELSE_FOR_NOW
```

and remain contactable outside Main Street through the published telephone.

A merchant that selects:

```text
SEND_ENQUIRY
```

requests Main Street Enquiry semantics in addition to any direct public contact points.

Therefore:

```text
public contact point
        ≠
Enquiry
```

remains preserved.

---

# 31. Relationship to Publication

Merchant Profile content such as:

```text
business description
business tagline
location information
opening hours
```

is not Publication capability content merely because it is public.

`PUBLISH_INFORMATION` continues to represent intent to manage separate published information such as supported resources, opportunities, announcements or other accepted Publication semantics.

This prevents a brochure website from accidentally activating Publication.

---

# 32. Rejected alternatives

## A. Keep VIEW_BUSINESS_INFORMATION as a harmless discovery checkbox

Rejected because it creates pressure for a semantic/capability mapping where no additional semantic is required.

## B. Map VIEW_BUSINESS_INFORMATION to Storefront

Rejected because Storefront is a projection/composition product, not the merchant business capability being discovered.

## C. Map VIEW_BUSINESS_INFORMATION to Merchant Profile

Rejected because Merchant Profile is a platform/merchant-information authority and management surface, not an optional customer-interaction capability.

## D. Require at least one positive interaction selection

Rejected because brochure-style and direct-contact merchants are valid.

## E. Treat NOTHING_ELSE_FOR_NOW as a capability

Rejected because it represents absence of additional discovery seeds.

## F. Treat basic merchant description as Publication

Rejected because profile description and separately managed Publication content have distinct ownership and lifecycle.

---

# 33. Accepted invariants

MS-PROT-052 v1.0 invariants remain accepted except where amended below.

1. Basic Merchant Profile/public storefront presence is a valid baseline and does not require an additional customer-interaction capability.
2. `VIEW_BUSINESS_INFORMATION` is removed from the current initial discovery option set.
3. The initial discovery question asks what visitors should be able to do **beyond** viewing the public business information the merchant chooses to expose.
4. `NOTHING_ELSE_FOR_NOW` means no additional customer-interaction semantic is requested from the current discovery branch.
5. `NOTHING_ELSE_FOR_NOW` is onboarding state/outcome, not runtime semantics or configuration value.
6. `NOTHING_ELSE_FOR_NOW` is mutually exclusive with positive additional-interaction selections in the same question context.
7. A valid discovery outcome may produce no semantic seed.
8. Discovery mappings are not required to map every answer to a capability.
9. Merchant Profile data capture remains independently eligible where applicable even if no additional interaction semantic is selected.
10. Profile public presence, capability discovery and exposure remain distinct authorities.
11. Merchant Profile description/public facts do not automatically activate Publication.
12. Public contact points do not automatically activate Enquiry.
13. Historical v1 `VIEW_BUSINESS_INFORMATION` answers remain versioned evidence and are not silently reinterpreted.
14. The affected stable question identity may be retained while its question-definition version increments.
15. Merchant category remains contextual metadata and does not replace the additional-interaction discovery axis.
16. The same baseline supports brochure-style, physical, online, informational, mobile-service and hybrid merchants.

---

# 34. Deferred decisions

This amendment does not define:

```text
exact storefront minimum required profile fields
exact visual presentation of an information-only storefront
whether a merchant must supply a public contact method
exact question wording/localisation
exact interaction between profile setup screens and discovery screens
future non-capability platform-surface contribution abstractions
```

Those decisions remain governed by their appropriate product/presentation/security authorities.

No deferred decision may reintroduce a synthetic `VIEW_BUSINESS_INFORMATION` business capability merely for baseline public presence.

---

# 35. Implementation consequence

During the active design-first phase, no implementation is required.

When implementation resumes:

```text
Merchant Profile/public projection
        ≠
Capability seed
```

must be preserved.

The initial discovery question definition should be versioned rather than mutating historical answer interpretation in place.

No runtime logic should depend on `NOTHING_ELSE_FOR_NOW` after configuration proposal resolution.

---

# 36. Continuous-improvement checkpoint

The material improvement introduced by v1.1 is simplification:

```text
public business information
    → profile/projection baseline

additional customer behaviour
    → semantic discovery
```

This removes an unnecessary pseudo-capability and makes the onboarding question more precise.

It also improves falsification against brochure-style merchants, direct-contact businesses and merchants that initially want only a professional public presence.

No further material correction was identified within this bounded amendment after testing the revised model against a brochure-style tradesperson, information-only organisation, scholarship publisher, online consultant, retailer and motel.

---

# Governance verdict

**ACCEPTED.**

Canonical decision:

> **Main Street may provide a valid public merchant storefront from audience-safe Merchant Profile information alone. The initial onboarding discovery question therefore asks what additional customer interactions the merchant wants beyond that baseline. Discovery outcomes may legitimately produce no new semantic seed, and the absence of additional interactions is not itself a capability or runtime state.**
