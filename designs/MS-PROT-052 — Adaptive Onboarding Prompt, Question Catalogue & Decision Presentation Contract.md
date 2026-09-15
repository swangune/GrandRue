# MS-PROT-052 — Adaptive Onboarding Prompt, Question Catalogue & Decision Presentation Contract

**Document ID:** MS-PROT-052  
**Version:** 1.0  
**Status:** **ACCEPTED after onboarding architecture review and manual approval**  
**Depends on:** MS-PROT-021 v1.3, MS-PROT-022 v1.5, MS-PROT-027 v1.1, MS-PROT-028 v1.3, MS-PROT-038 v1.0, MS-PROT-039 v1.2, MS-PROT-040, MS-PROT-043 v1.2, MS-PROT-046 v1.1, MS-PROT-047 v1.0, MS-PROT-048 v1.1, MS-PROT-049 v1.0, MS-PROT-050 v1.2, MS-PROT-051 v1.0  
**Purpose:** Define the reusable onboarding prompt classes, stable question/option identities, adaptive eligibility and sequencing rules, discovery mappings, contextual decision presentation, answer provenance and initial supported question catalogue through which Main Street learns merchant intent without turning questionnaire wording, AI inference or business category into semantic authority.

---

# 1. Governing decision

Main Street onboarding shall remain adaptive, structured and business-facing.

The governing decision is:

> **Onboarding prompts present discovery needs, registered configuration decisions, structured merchant data requirements and final configuration review. They do not define executable semantics. Semantic meaning remains owned by the registered Main Street authorities that the prompts reference.**

Canonical boundary:

```text
Registered Main Street semantics
        +
Registered configuration decisions
        +
Merchant-profile / data contracts
        +
Current candidate configuration
        ↓
Unresolved onboarding needs
        ↓
Onboarding Presentation Registry
        ├── DISCOVERY QUESTION
        ├── CONFIGURATION DECISION QUESTION
        ├── DATA CAPTURE PROMPT
        └── REVIEW / CONFIRMATION
        ↓
Adaptive Prompt Selection
        ↓
Merchant-facing structured interaction
        ↓
Evidence / selection / data / approval
        ↓
Configuration Proposal
        ↓
Deterministic validation under MS-PROT-022
```

Hard rule:

> **A question may present a semantic decision; question wording and answer labels shall never become the source of that decision's semantic meaning.**

---

# 2. Why this contract is required

MS-PROT-038 established that onboarding is an adaptive configuration-discovery process and deliberately deferred the exact question catalogue.

MS-PROT-039 established reusable question families, structured answer forms, provenance, branch pruning and scheduling decision presentation.

MS-PROT-047 established that capability configuration definitions are distinct from merchant-facing presentation.

A remaining risk was to implement onboarding as one large questionnaire catalogue in which every merchant interaction was treated as the same kind of question.

That would collapse materially different concerns:

```text
"What should customers be able to do?"
        → discovery

"Should customers choose available times or should you arrange them?"
        → configuration decision

"What are your Monday opening hours?"
        → structured data capture

"Is this how your business should work?"
        → configuration review / approval
```

These are not the same kind of authority.

This contract separates them explicitly.

---

# 3. Scope

MS-PROT-052 governs:

```text
onboarding prompt classes
question identity
question-definition version
option identity
answer-form presentation
discovery mappings
prompt applicability
adaptive prompt sequencing
branch pruning
context-scoped questioning
VARIES behaviour
NOT_SURE behaviour
OTHER / unsupported-intent behaviour
natural-language preselection
question and answer provenance
localisable presentation wording
review / confirmation semantics
initial discovery question catalogue
accepted Scheduling prompt presentation
accepted Publication prompt presentation
accepted Merchant Profile / location / Business Hours prompts
accepted external-calendar prompt presentation
```

MS-PROT-052 does not own:

```text
capability definitions
capability dependencies
operation semantics
configuration value meaning
Offering schemas
Business Hours temporal semantics
Merchant Location semantics
trust requirements
provider compatibility
runtime availability
compiler validity
AI model/provider
frontend component library
copywriting/localisation content
```

Those retain their existing accepted authorities.

---

# 4. Four onboarding prompt classes

Main Street shall distinguish four prompt classes.

```text
1. DISCOVERY QUESTION
2. CONFIGURATION DECISION QUESTION
3. DATA CAPTURE PROMPT
4. REVIEW / CONFIRMATION
```

A fifth generic class shall not be added merely because one screen looks different.

The class follows the authority and purpose of the interaction, not the visual widget used to render it.

---

# 5. Discovery Question

A **Discovery Question** asks the merchant about business intent in order to identify which already-supported semantic areas may be relevant.

It answers questions such as:

```text
What should customers be able to do?
What kind of information do you publish?
Do customers arrange appointments?
```

A discovery answer is evidence from which Main Street may propose registered semantic seeds.

It is not itself executable configuration.

Canonical flow:

```text
Discovery answer
        ↓
registered DiscoveryMapping
        ↓
candidate semantic seed(s)
        ↓
registered dependency resolution
        ↓
unresolved configuration decisions
```

Hard invariant:

> **Discovery option identity is not capability identity.**

For example:

```text
ARRANGE_APPOINTMENT
```

is an onboarding discovery option.

It must not be treated as a capability named `ARRANGE_APPOINTMENT`.

Its mapping may propose existing Scheduling / Appointment / Booking-related semantics only where accepted registry contracts support them.

---

# 6. Configuration Decision Question

A **Configuration Decision Question** presents a merchant-owned bounded decision whose semantic definition already exists under an accepted authority.

Example registered decision:

```text
scheduling / authority-mode

allowed semantic values:
    CUSTOMER_SELECTS_AVAILABLE_TIME
    MERCHANT_PROPOSES_OR_CONFIRMS
```

Possible presentation:

> When a customer needs an appointment, how should choosing the time work?

```text
[ They choose from times Main Street says are available ]
[ I arrange or confirm the time ]
[ Different services work differently ]
```

The first two options resolve registered semantic values.

The third is a presentation/meta outcome that triggers context partitioning; it is not a runtime scheduling value.

MS-PROT-047 remains authoritative for the underlying capability configuration decision.

Hard rule:

> **Onboarding shall not create a new configuration value merely because the user experience would benefit from another option.**

If the desired option is not supported by the registered semantic/configuration contract, onboarding must expose a configuration gap rather than invent meaning.

---

# 7. Data Capture Prompt

A **Data Capture Prompt** obtains structured merchant-owned data required by an already-established authority.

Examples:

```text
business display name
public telephone
Merchant Location address
weekly Business Hours
Offering duration
merchant-approved description
```

It does not ask the merchant to choose a semantic behaviour unless the owning contract explicitly makes that value a configuration decision.

Example:

```text
Monday opening hours
    09:00–17:00
```

is data capture under MS-PROT-050.

The prompt must not be interpreted as a capability configuration definition owned by onboarding.

Hard rule:

> **A structured editor is not automatically a configuration-decision engine.**

---

# 8. Review / Confirmation

A **Review / Confirmation** interaction presents the merchant with the material consequences of the current proposal in business-facing language and allows approval or correction.

Example:

```text
Customers can:
✓ browse your published opportunities
✓ send enquiries
✓ subscribe for updates

Appointments:
– not enabled

Online ordering:
– not enabled
```

Approval confirms merchant-owned choices represented by the reviewed proposal.

It does not bypass deterministic validation.

Canonical flow:

```text
Candidate configuration
        ↓
business-facing review projection
        ↓
merchant approves / edits
        ↓
validated configuration revision
        ↓
MS-PROT-040 lifecycle
```

Hard invariant:

> **Merchant approval confirms proposed merchant intent; it does not legalise unsupported semantics or override compiler invariants.**

---

# 9. Material choice does not require one standalone screen

A merchant-owned material decision must be authoritatively resolved before activation where its contract requires resolution.

That does not mean every decision must be presented as a separate screen.

Valid:

```text
Natural-language evidence
        ↓
Inference preselects bounded option
        ↓
Final review displays the consequence
        ↓
merchant approves / corrects
```

where the review interaction makes the decision sufficiently explicit and correctable.

Invalid:

```text
Inference silently chooses material merchant-owned value
        ↓
merchant never sees or approves it
        ↓
configuration activated
```

The objective is **minimum interaction**, not maximum questionnaire count.

---

# 10. Question identity

Every reusable onboarding question shall have a stable identity independent of its merchant-facing wording.

Conceptually:

```text
OnboardingQuestionIdentity
{
    namespace
    identifier
}
```

Example:

```text
onboarding.discovery / customer-interactions
```

or conceptually:

```text
onboarding.discovery.customer-interactions
```

Exact serialization remains an implementation decision.

Hard rule:

> **Question identity shall not be the English sentence shown to the merchant.**

Wording may evolve, be localised or be shortened for mobile presentation without changing semantic mapping.

---

# 11. Question-definition version

A material question definition shall be versioned where a change could alter:

```text
option set
semantic/discovery mapping
applicability contract
answer interpretation
context meaning
```

Conceptually:

```text
QuestionDefinition
{
    questionIdentity
    version
    promptClass
    targetNeed
    applicability
    answerForm
    optionDefinitions
    mappingContract
    presentationKey
}
```

Changing punctuation or explanatory copy need not create a semantic definition version unless interpretation changes materially.

Hard invariant:

> **An old stored answer shall not silently acquire new semantic meaning because a later question definition changed.**

---

# 12. Option identity

Structured answer options shall have stable identities separate from display labels.

Example:

```text
question:
    onboarding.discovery.customer-interactions

option identities:
    VIEW_BUSINESS_INFORMATION
    PUBLISH_INFORMATION
    SEND_ENQUIRY
    ARRANGE_APPOINTMENT
    RESERVE_SUBJECT
    PLACE_ORDER
    SUBSCRIBE_UPDATES
    OTHER
```

Merchant-facing labels may be:

```text
Learn about my business
Read information I publish
Send me an enquiry
Arrange an appointment
Reserve something
Place an order
Subscribe for updates
Something else
```

The persisted semantic answer must not depend on those exact phrases.

---

# 13. Answer forms

MS-PROT-039's bounded answer-form approach remains accepted.

Initial presentation forms may include:

```text
SINGLE_SELECT
MULTI_SELECT
BOOLEAN
QUANTITY
DURATION
TEXT
STRUCTURED_VALUE
```

This vocabulary describes merchant interaction shape.

It does not create new semantic value types.

For example:

```text
MULTI_SELECT
```

does not mean the target capability configuration accepts an arbitrary set unless its owning contract permits that set.

---

# 14. Discovery mappings

Discovery mappings are platform-owned, registered mappings from onboarding evidence to candidate semantic seeds.

Conceptually:

```text
DiscoveryMapping
{
    sourceQuestionIdentity
    sourceQuestionVersion
    sourceOptionIdentity
    proposedSemanticSeeds
    optionalEvidenceMetadata
}
```

A mapping may propose:

```text
SEND_ENQUIRY
    → candidate Enquiry semantics
```

or:

```text
PUBLISH_INFORMATION
    → candidate Publication semantics
```

The mapping may not invent:

```text
new capability
new dependency
new relationship
new operation
new effect
new trust requirement
```

Registered semantic relationships remain authoritative after seed proposal.

---

# 15. Discovery mapping is not deterministic activation

A discovery answer may be sufficiently direct to produce a strong candidate seed, but activation still follows accepted configuration resolution.

Canonical:

```text
merchant selects SEND_ENQUIRY
        ↓
registered discovery mapping proposes Enquiry
        ↓
configuration proposal contains candidate Enquiry selection
        ↓
registered dependencies/conflicts resolved
        ↓
merchant review where applicable
        ↓
compiler validation
```

The onboarding engine does not directly mutate the active semantic registry or runtime.

---

# 16. Natural-language evidence

Main Street may invite optional natural-language description before or during structured onboarding.

Example:

> In one sentence, tell us what your business does.

Merchant:

> I publish scholarship opportunities and people contact us with questions.

Inference may use this as evidence to preselect:

```text
PUBLISH_INFORMATION
SEND_ENQUIRY
```

The structured interaction can then show:

> Here's what I understood. What should visitors be able to do?

with those options preselected.

Hard rule:

> **Natural language may preselect, rank or explain registered options; it may not create an option, change a mapping contract or make an otherwise ineligible semantic decision executable.**

---

# 17. AI boundary

AI may:

```text
interpret merchant description
extract likely business facts
rank eligible prompts
preselect eligible registered options
suggest concise explanations
identify evidence conflict
translate business language into candidate registered semantics
```

AI may not:

```text
invent questions with new semantic consequences
invent answer options
change question applicability
change registered discovery mappings
create capabilities
create configuration values
change capability dependencies
resolve compiler conflicts by creativity
make a required prompt ineligible
make an ineligible prompt eligible
silently approve merchant choices
```

A deterministic non-AI onboarding path must remain possible.

---

# 18. Deterministic prompt eligibility

A prompt is eligible only when its registered applicability condition is satisfied by current authoritative/candidate state.

Conceptually:

```text
Current candidate configuration
        +
resolved registered consequences
        +
known merchant evidence/data
        ↓
Prompt applicability evaluation
        ↓
ELIGIBLE / INELIGIBLE
```

AI may not override this boundary.

Examples:

```text
External calendar prompt
    INELIGIBLE before Scheduling is relevant

Location Business Hours editor
    INELIGIBLE before target Merchant Location exists

Scheduling authority-mode prompt
    INELIGIBLE where appointment-style Scheduling is not applicable
```

---

# 19. Adaptive sequencing

Main Street shall select the next eligible unresolved prompt from the current frontier rather than follow one universal questionnaire sequence.

Priority classes are:

```text
1. HIGH-BRANCH DISCOVERY
   decisions that can eliminate large semantic branches

2. MANDATORY CONFIGURATION DECISIONS
   applicable decisions required for a valid configuration

3. REQUIRED STRUCTURED DATA
   data without which an already-selected operation/profile cannot be completed

4. OPTIONAL INTEGRATIONS
   provider or external-system connections relevant to selected semantics

5. OPTIONAL PROFILE ENRICHMENT
   useful but non-blocking descriptive/public information
```

Within one class, prefer the prompt whose valid answers can resolve or remove the largest amount of currently unresolved downstream work.

This is an information-gain heuristic over registered dependencies/applicability, not permission for AI to invent architecture.

---

# 20. Highest-information next question

The preferred next question should normally be one whose answer has the greatest material effect on the unresolved graph.

Example:

```text
Do customers arrange appointments?
```

should normally precede:

```text
What preparation buffer do you need?
```

because a `NO` answer to the first may remove the second entirely.

Likewise:

```text
Do you want customers to see a business location?
```

should precede location-specific address/hours prompts.

---

# 21. Branch pruning

When an answer makes a branch inapplicable, all prompts that are relevant only to that branch shall be removed from the active onboarding frontier.

Example:

```text
ARRANGE_APPOINTMENT not selected
```

may make irrelevant:

```text
scheduling authority mode
appointment duration
appointment buffers
external calendar connection
appointment-specific cancellation options
```

unless another independently selected semantic requires them.

Hard rule:

> **The existence of a prompt in the global registry does not make it eligible for every merchant.**

---

# 22. Context-scoped decisions

A prompt shall carry the scope of the decision/data it is resolving where that scope is not merchant-wide.

Examples:

```text
scheduling authority mode
    scope = Offering A

scheduling authority mode
    scope = Offering B

Business Hours
    scope = Merchant Location L1

Business Hours
    scope = Merchant Location L2
```

The merchant-facing presentation may hide technical scope identifiers, but the answer must retain exact applicable context.

This prevents one answer from accidentally becoming merchant-wide.

---

# 23. VARIES is a presentation/meta outcome

Main Street may present a merchant-facing answer such as:

```text
Different services work differently
```

or conceptually:

```text
VARIES
```

This shall not become an executable configuration value unless the underlying registered contract independently defines such a value.

Default interpretation:

```text
merchant-wide answer insufficient
        ↓
identify supported subcontexts
        ↓
ask/resolve actual registered value per context
```

Example:

```text
Initial Consultation
    CUSTOMER_SELECTS_AVAILABLE_TIME

Complex Project Review
    MERCHANT_PROPOSES_OR_CONFIRMS
```

There is no runtime:

```text
schedulingMode = VARIES
```

Hard invariant:

> **`VARIES` partitions a decision; it does not satisfy the decision.**

---

# 24. NOT_SURE is not a semantic value

For usability, a merchant-facing prompt may offer:

```text
I'm not sure
```

where appropriate.

This means:

```text
material decision remains UNRESOLVED
```

Main Street may respond by:

```text
explaining the options
showing domain-neutral examples
asking a simpler discriminator
allowing the merchant to return later where non-blocking
```

It may not compile:

```text
configurationValue = NOT_SURE
```

Hard invariant:

> **Uncertainty is onboarding state, not business semantics.**

---

# 25. OTHER and unsupported merchant intent

A discovery prompt may offer:

```text
Something else
```

This is necessary to avoid forcing merchant intent into the nearest known option.

Selecting `OTHER` may trigger a bounded text explanation.

Inference may then determine whether the explanation maps to already-supported registered semantics.

Possible outcomes:

```text
existing supported semantics identified
        → return to bounded options / proposal

material intent unsupported
        → CONFIGURATION GAP
```

Rejected:

```text
OTHER
    ↓
AI invents capability
```

Hard rule:

> **Unsupported intent shall remain unsupported until Main Street deliberately introduces the necessary semantics through the design/registry process.**

---

# 26. Repetition rule

Main Street shall not ask for information already known authoritatively unless:

```text
prior evidence conflicts
scope/context differs
merchant changed the answer
source is no longer sufficiently reliable
previous value became invalid
material confirmation is required before activation
```

Example:

If a Merchant Location address already exists in MS-PROT-051 authority, a later integration flow should reference it rather than ask the merchant to retype it solely because the integration also needs an address.

If an external provider requires its own independently authoritative address submission, the UI may explain the provider-specific requirement rather than pretending Main Street lacks the existing value.

---

# 27. Answer provenance

Material answers shall retain enough provenance to explain what was asked and what the merchant/evidence supplied.

Conceptually:

```text
OnboardingAnswer
{
    questionIdentity
    questionDefinitionVersion
    answerOptionIdentities / structuredValueReference
    contextScope
    answerOrigin
    answeredBy
    answeredAt
    semanticRegistryRelease
}
```

Possible answer origins include conceptually:

```text
MERCHANT_SELECTED
MERCHANT_APPROVED_IN_REVIEW
INFERRED_PROPOSAL
DERIVED
DEFAULTED
IMPORTED_EVIDENCE
```

These do not replace the configuration provenance vocabulary in MS-PROT-047; onboarding provenance explains how evidence/selection entered the proposal.

---

# 28. Raw onboarding answers are not runtime authority

After configuration approval and resolution:

```text
Onboarding answers
        → evidence / provenance

Approved configuration revision
        → configuration authority

Resolved Configuration Package
        → static executable/resolved authority

Runtime state/context
        → contextual execution authority
```

Hard rule:

> **Runtime shall not reinterpret historical questionnaire answers to decide current business semantics.**

If a merchant changes an onboarding-originated decision later, Main Street creates/updates the applicable configuration through the normal configuration lifecycle.

---

# 29. Localisation and wording

Presentation text is localisable and may evolve independently of semantic identity.

Conceptually:

```text
Question identity
        ↓
presentationTextKey
        ↓
locale-specific wording
```

Option labels follow the same model.

A translation must preserve the registered decision/mapping meaning.

Hard rule:

> **Localisation shall not create locale-specific semantic behaviour.**

---

# 30. Initial discovery catalogue — customer interaction

The initial high-information discovery question is:

> **What should visitors be able to do through your Main Street website? Select all that apply.**

Stable conceptual option identities:

```text
VIEW_BUSINESS_INFORMATION
PUBLISH_INFORMATION
SEND_ENQUIRY
ARRANGE_APPOINTMENT
RESERVE_SUBJECT
PLACE_ORDER
SUBSCRIBE_UPDATES
OTHER
```

Merchant-facing wording may be:

```text
[ ] Learn about my business and how to contact or visit me
[ ] Read information, updates or opportunities I publish
[ ] Send me an enquiry
[ ] Arrange an appointment or agreed service time
[ ] Reserve something for a date or period
[ ] Place an order or request to buy something
[ ] Subscribe for future updates
[ ] Something else
```

This is a `MULTI_SELECT` discovery question.

The displayed wording may be shortened where device constraints require it, provided meaning is preserved.

---

# 31. Why customer interaction is the preferred first discovery axis

Customer interaction is a better configuration discriminator than merchant category because it describes actual desired behaviour.

Representative mappings:

```text
Scholarship publisher
    PUBLISH_INFORMATION
    SEND_ENQUIRY
    SUBSCRIBE_UPDATES

Online consultant
    VIEW_BUSINESS_INFORMATION
    SEND_ENQUIRY
    ARRANGE_APPOINTMENT

Salon
    VIEW_BUSINESS_INFORMATION
    SEND_ENQUIRY
    ARRANGE_APPOINTMENT

Motel
    VIEW_BUSINESS_INFORMATION
    SEND_ENQUIRY
    RESERVE_SUBJECT

Retailer
    VIEW_BUSINESS_INFORMATION
    SEND_ENQUIRY
    PLACE_ORDER

Information-only organisation
    VIEW_BUSINESS_INFORMATION
    PUBLISH_INFORMATION
```

A hybrid merchant may select several.

No business category directly chooses architecture.

---

# 32. Merchant category is profile/discovery metadata, not the configuration root

Main Street may still collect merchant category for:

```text
search
SEO
merchant classification
onboarding evidence
presentation recommendation
merchant-facing terminology
analytics
```

Under MS-PROT-051 it remains contextual metadata.

It shall not be treated as the authoritative first decision from which capabilities are mechanically activated.

Rejected:

```text
category = SALON
    → activate Booking
    → activate Scheduling
    → use Salon dashboard
```

Accepted:

```text
category = SALON
    → useful inference evidence / public classification

customer-interaction selections
    + registered semantic mappings
    → candidate configuration
```

---

# 33. Initial Publication follow-up

If `PUBLISH_INFORMATION` is selected or otherwise applicable, Main Street may ask:

> **What kind of information do you publish? Select all that apply.**

Initial conceptual options justified by MS-PROT-046:

```text
PUBLISHED_INFORMATION_OR_RESOURCE
OPPORTUNITY
ANNOUNCEMENT_OR_UPDATE
```

Possible presentation:

```text
[ ] Articles, resources or general information
[ ] Opportunities with details such as deadlines, eligibility or external links
[ ] Short announcements or business updates
```

The prompt shall use multi-select rather than adding a meaningless `Several of these` option.

Future publication types require accepted semantics before becoming semantic options.

---

# 34. Subscription follow-up

Where Publication/Notification/Subscription semantics are applicable and supported, Main Street may ask:

> **Should visitors be able to subscribe for future updates?**

Conceptual answers:

```text
YES
NO
```

This prompt shall not appear merely because a merchant has a public website.

Its eligibility must derive from applicable supported semantics.

---

# 35. Enquiry prompt discipline

If `SEND_ENQUIRY` is selected and Enquiry semantics are active/proposed, onboarding shall not ask merchants to configure behaviour already established as a platform invariant.

For example, MS-PROT-043's context-carry-forward model means that an enquiry initiated from a known subject retains that subject context.

Rejected question:

> Should enquiries include the product/listing/opportunity the customer was looking at?

That is not a legitimate merchant choice where the accepted semantic contract already requires context carry-forward.

Hard rule:

> **Onboarding shall not ask the merchant to choose whether a registered invariant should apply.**

---

# 36. Initial Scheduling applicability follow-up

Where `ARRANGE_APPOINTMENT` is selected or Scheduling is otherwise potentially applicable, MS-PROT-039 remains authoritative for appointment-style scheduling applicability.

Business-facing first discriminator:

> **Does this service require an appointment or agreed service time?**

Conceptual outcomes remain:

```text
NO_APPOINTMENT_SCHEDULING
APPOINTMENT_SCHEDULING
CONTEXT_DEPENDENT
```

Where the discovery selection itself already makes appointment intent explicit and no ambiguity remains, Main Street may avoid repeating an equivalent question and proceed to the next unresolved scheduling decision.

---

# 37. Scheduling authority-mode question

Where appointment-style scheduling applies, present the accepted scheduling authority choice as:

> **When a customer needs an appointment, how should choosing the time work?**

Conceptual merchant-facing options:

```text
CUSTOMER_SELECTS
    "They choose from times Main Street says are available"

MERCHANT_ARRANGES_OR_CONFIRMS
    "I arrange or confirm the time"

VARIES_BY_CONTEXT
    "Different services work differently"
```

Semantic mapping:

```text
CUSTOMER_SELECTS
    → CUSTOMER_SELECTS_AVAILABLE_TIME

MERCHANT_ARRANGES_OR_CONFIRMS
    → MERCHANT_PROPOSES_OR_CONFIRMS

VARIES_BY_CONTEXT
    → partition contexts; does not resolve semantic value
```

---

# 38. Context-dependent scheduling

If the merchant chooses `VARIES_BY_CONTEXT`, Main Street shall identify the smallest supported contexts that require independent answers.

Example:

```text
30-minute consultation
    → CUSTOMER_SELECTS_AVAILABLE_TIME

Complex assessment
    → MERCHANT_PROPOSES_OR_CONFIRMS

Product sale
    → scheduling not applicable
```

The same presentation may be reused with each context clearly identified in business language.

This reuse does not violate the repetition rule because the semantic scope differs.

---

# 39. Public location prompt

Physical location is optional under MS-PROT-051.

Where public presence setup is relevant, Main Street may ask:

> **Do you want customers to see a business location?**

Conceptual options:

```text
NO_PUBLIC_LOCATION
ONE_PUBLIC_LOCATION
MULTIPLE_PUBLIC_LOCATIONS
```

These answers govern public profile-presence setup.

They do not establish:

```text
merchant eligibility
business legitimacy
premises verification
bookability
resource capacity
```

If one or more public locations are selected, Main Street proceeds to Merchant Location data capture under MS-PROT-051.

---

# 40. Service-area prompt

Where a merchant may want to describe an area served without exposing premises, Main Street may ask:

> **Would you like to show customers the area you serve?**

Conceptual answers:

```text
YES
NO
```

If `YES`, Main Street collects the supported service-area descriptor under MS-PROT-051.

Hard rule:

> **A public service-area description shall not silently become an executable service/delivery eligibility rule.**

If an operation needs geographic eligibility semantics, that capability must define them separately.

---

# 41. Public Business Hours applicability prompt

Main Street shall not force every merchant to configure opening hours.

Where Public Business Hours may be useful, Main Street may ask:

> **Do you want customers to see when your business is normally open?**

Conceptual answers:

```text
YES
NO
```

If `YES`, the applicable Business Hours scope is resolved under MS-PROT-050 v1.2.

Possible scopes include:

```text
MERCHANT
MERCHANT_LOCATION(locationIdentity)
```

The weekly-hours editor that follows is a `DATA CAPTURE PROMPT`, not another semantic question.

---

# 42. Multi-location Business Hours capture

For multiple Merchant Locations, Main Street shall present hours in the exact location context.

Example:

```text
Swansea
    [weekly hours]

Cardiff
    [weekly hours]
```

The UX may provide:

```text
Copy Swansea hours to Cardiff
```

but such convenience shall create explicit Cardiff-scope values rather than an implicit inheritance rule.

MS-PROT-050 v1.2 remains authoritative.

---

# 43. External calendar prompt comes after Scheduling

External calendar integration is not a Scheduling-discovery mechanism.

Only after Scheduling is established and an external calendar integration is relevant may Main Street ask:

> **Would you like to connect an external calendar?**

Conceptual options:

```text
NO_EXTERNAL_CALENDAR
OUTBOUND_MAIN_STREET_APPOINTMENTS
INBOUND_EXTERNAL_BUSY_TIME
BOTH_DIRECTIONS
```

Possible presentation:

```text
[ ] No
[ ] Show Main Street appointments in my external calendar
[ ] Use my external busy times when Main Street calculates availability
[ ] Both
```

This preserves MS-PROT-041 v1.1 / MS-PROT-039 v1.2 separation between outbound projection and inbound busy-time constraint import.

Hard rule:

> **Connecting an external calendar shall not activate Scheduling, Appointment or Booking semantics.**

---

# 44. Integration questions are downstream of business semantics

The general rule for integrations is:

```text
business semantic need established
        ↓
fulfilment / integration role applicable
        ↓
provider/integration question may become eligible
```

Rejected:

```text
Do you use Stripe?
        ↓
therefore Payment active
```

or:

```text
Do you use Calendly?
        ↓
therefore Scheduling active
```

A provider is an implementation/fulfilment choice, not the definition of the merchant's business behaviour.

---

# 45. Capability-owned question registration

A future capability may contribute onboarding presentation only after its semantic/configuration contract exists.

Conceptually:

```text
Accepted capability semantics
        ↓
Accepted Capability Configuration Decision
        ↓
Registered onboarding presentation
```

Onboarding presentation may specify:

```text
business-facing wording key
answer form
option label keys
explanatory copy/examples
applicability reference
```

It may not redefine:

```text
value domain
semantic target
default
resolution requirement
scope contract
```

Those remain owned by MS-PROT-047/underlying capability authority.

---

# 46. Do not pre-design unsupported capability choices

MS-PROT-052 intentionally does not define exact configuration questions/options for every possible Main Street capability.

In particular, where accepted contracts do not yet define precise choices for:

```text
Ordering
Delivery
Collection
Payment timing
advanced Booking policy
Inventory-specific configuration
other future capabilities
```

this document shall not invent those choices merely to make the catalogue look complete.

Discovery may establish that the semantic area is relevant.

The detailed question becomes registrable only when the underlying accepted decision exists.

Hard invariant:

> **Onboarding presents architecture; it does not design architecture.**

---

# 47. Profile data prompts are not semantic activation

The Business Profile may ask merchants to add:

```text
display name
description
public contact point
location
service area
Business Hours
external presence link
```

under MS-PROT-051/050.

Supplying those values does not automatically activate unrelated capabilities.

Examples:

```text
public address
    ≠ Collection enabled

public phone
    ≠ Enquiry enabled

Business Hours
    ≠ Scheduling enabled

Instagram link
    ≠ social syndication integration connected
```

---

# 48. Required semantics are not fake questions

If a registered dependency is mandatory, Main Street shall not ask the merchant whether they want that dependency as though it were optional.

Example conceptual pattern:

```text
Merchant selects semantic A
        ↓
registry says A REQUIRES B
        ↓
B becomes required
```

Do not ask:

> Would you also like B?

Instead, where useful, explain the consequence in business-facing language.

Questions represent genuine merchant choices or required data, not implementation ceremony.

---

# 49. Registered defaults reduce questioning

If an applicable configuration decision has an accepted registered default and merchant choice need not be explicit before use, onboarding may avoid a standalone question.

The default remains provenance-marked as `DEFAULTED` under the configuration authority.

The final review may expose the value where material and allow the merchant to change it.

This allows onboarding to remain short without pretending the merchant explicitly selected the default.

---

# 50. Explainability

Main Street shall be able to explain internally why a prompt was asked.

Conceptually:

```text
PromptDecision
{
    promptIdentity
    targetUnresolvedNeed
    applicableContext
    triggeringEvidence
    eligibilityBasis
}
```

Valid explanation:

```text
External calendar question is shown because:
Scheduling is active and an optional calendar integration role is unresolved.
```

Invalid explanation:

```text
Most consultants use Google Calendar.
```

Statistical patterns may rank or preselect; they do not establish eligibility.

---

# 51. Customer-facing language

Merchant-facing onboarding shall use business vocabulary rather than semantic-engine terminology.

Prefer:

```text
Customers choose an available time
```

not:

```text
Set scheduling.authority-mode = CUSTOMER_SELECTS_AVAILABLE_TIME
```

Prefer:

```text
Show customers your Swansea location
```

not:

```text
Expose MerchantLocationIdentity L123
```

The internal mapping remains traceable.

---

# 52. Mobile-first interaction

MS-PROT-052 does not define frontend layout, but the prompt model must be compatible with complete mobile onboarding.

Therefore prompts should favour:

```text
short bounded selections
progressive disclosure
contextual structured editors
review summaries
save/resume
```

rather than long universal forms.

Mobile presentation requirements shall not change semantic meaning.

---

# 53. Save, resume and partial completion

Onboarding may be interrupted and resumed.

Stored prompt progress may contain:

```text
answered discovery questions
unresolved decisions
partial profile data
inferred proposals
review state
```

Partial onboarding state is not active configuration.

On resume, Main Street recalculates current eligible prompts from authoritative candidate state and retained evidence, rather than blindly continuing a stale numeric step index.

Hard rule:

> **The current unresolved graph, not a fixed wizard step number, determines where onboarding resumes.**

---

# 54. Configuration evolution uses the same prompt machinery

The prompt architecture applies after initial onboarding as merchants evolve.

Example:

```text
Existing merchant:
Publication + Enquiry

Merchant chooses:
"I want to start offering paid consultations."
```

Main Street may reuse discovery/configuration presentation to resolve only the newly relevant branch.

It shall not replay the entire initial onboarding questionnaire.

---

# 55. Cross-domain trace — scholarship publisher

Merchant description:

> I publish scholarship opportunities and people can contact us with questions.

Possible flow:

```text
Optional natural-language description
        ↓
Inference preselects:
    PUBLISH_INFORMATION
    SEND_ENQUIRY
        ↓
merchant confirms customer interactions
        ↓
Publication follow-up:
    OPPORTUNITY
        ↓
optional Subscription decision
        ↓
Opportunity/profile structured data prompts
        ↓
optional public contact/location/hours enrichment
        ↓
review
```

No Payment, Inventory, Scheduling or physical-location requirement appears unless independently relevant.

**PASS**

---

# 56. Cross-domain trace — online consultant

Possible flow:

```text
Customer interactions:
    SEND_ENQUIRY
    ARRANGE_APPOINTMENT
        ↓
Scheduling authority:
    CUSTOMER_SELECTS_AVAILABLE_TIME
        ↓
Consultation Offering data
        ↓
applicable Business Hours / Scheduling data
        ↓
optional external calendar
        ↓
review
```

No premises verification or mandatory external calendar is introduced.

**PASS**

---

# 57. Cross-domain trace — salon with mixed scheduling

Possible flow:

```text
Customer interactions:
    ARRANGE_APPOINTMENT
        ↓
Scheduling authority:
    VARIES_BY_CONTEXT
        ↓
Haircut:
    CUSTOMER_SELECTS_AVAILABLE_TIME

Colour correction consultation:
    MERCHANT_PROPOSES_OR_CONFIRMS
        ↓
location/hours/resource prompts as applicable
        ↓
review
```

There is no executable `VARIES` value.

**PASS**

---

# 58. Cross-domain trace — motel

Possible discovery:

```text
VIEW_BUSINESS_INFORMATION
SEND_ENQUIRY
RESERVE_SUBJECT
```

`RESERVE_SUBJECT` does not automatically force the Appointment scheduling model.

Registered Booking/resource/date-range semantics determine downstream decisions.

Until those exact configuration choices are accepted, MS-PROT-052 shall not invent appointment-style prompts for the motel.

**PASS**

---

# 59. Cross-domain trace — retailer

Possible discovery:

```text
VIEW_BUSINESS_INFORMATION
PLACE_ORDER
SEND_ENQUIRY
```

Main Street may proceed to supported Offering/product data capture and whatever Ordering configuration contracts are already accepted.

MS-PROT-052 does not assume:

```text
Payment required
Inventory required
Delivery required
physical location required
```

Those arise only from registered semantics/merchant choices.

**PASS**

---

# 60. Cross-domain trace — multi-location merchant

Merchant chooses:

```text
MULTIPLE_PUBLIC_LOCATIONS
```

Main Street captures:

```text
Swansea location
Cardiff location
```

Then, where Public Business Hours are desired:

```text
Swansea hours
Cardiff hours
```

The prompt layer does not create cross-location inheritance or a synthetic global OPEN state.

**PASS**

---

# 61. Cross-domain trace — mobile tradesperson

Possible profile choices:

```text
NO_PUBLIC_LOCATION
SHOW_SERVICE_AREA = YES
SEND_ENQUIRY
```

The service-area prompt does not create a geographic runtime eligibility rule.

A future mobile-service/delivery capability may explicitly bind to structured geographic constraints if accepted.

**PASS**

---

# 62. Rejected alternatives

## A. One universal questionnaire

Rejected because irrelevant questions scale with platform breadth and reintroduce business-template thinking.

## B. Business category as the root configuration decision

Rejected because hybrid/evolving merchants and accepted capability architecture disprove category-driven execution.

## C. Natural-language-only onboarding

Rejected because material ambiguity requires bounded, reviewable decisions.

## D. Questionnaire wording as semantic authority

Rejected because wording/localisation changes would change business meaning accidentally.

## E. One standalone screen per decision

Rejected because review/confirmation can safely resolve or confirm several explicit correctable choices and unnecessary screens increase friction.

## F. AI-generated questions/options

Rejected because AI would become a semantic author.

## G. Store answer labels as configuration values

Rejected because labels are presentation text and may change/localise.

## H. `VARIES` as executable configuration

Rejected because it hides unresolved context-specific decisions.

## I. `NOT_SURE` as executable configuration

Rejected because uncertainty is onboarding state, not business meaning.

## J. Provider-first questioning

Rejected because providers fulfil/integrate semantics rather than define them.

## K. Complete every future capability's questionnaire now

Rejected because onboarding would invent configuration decisions before capability contracts justify them.

---

# 63. Accepted invariants

1. Onboarding prompts present business intent and registered choices; they do not own executable semantics.
2. Discovery Question, Configuration Decision Question, Data Capture Prompt and Review/Confirmation remain distinct prompt classes.
3. Discovery option identity is not capability identity.
4. Configuration Decision Questions may present only already-registered bounded decisions.
5. Data Capture Prompts collect values under their owning authority and do not automatically create semantic choices.
6. Review/Confirmation may explicitly approve several merchant-owned choices without requiring one screen per choice.
7. Question identities are stable and independent of merchant-facing wording.
8. Material question interpretations are versioned.
9. Answer-option identities are stable and independent of labels/localisation.
10. Stored old answers shall not be silently reinterpreted by later question versions.
11. Discovery mappings are platform-owned and may propose only registered semantics.
12. Discovery answers do not directly activate runtime semantics.
13. Natural language may preselect/rank registered options but cannot invent semantics/options/mappings.
14. AI cannot alter deterministic prompt eligibility.
15. A deterministic non-AI onboarding path remains possible.
16. Prompt selection is adaptive over the unresolved configuration/evidence frontier.
17. High-branch questions normally precede leaf-level detail.
18. Inapplicable branches are pruned.
19. Scoped decisions retain explicit semantic context.
20. `VARIES` partitions a decision and is not a runtime semantic value unless explicitly defined elsewhere.
21. `NOT_SURE` leaves a decision unresolved and is not a runtime semantic value.
22. `OTHER` may expose unsupported intent but cannot author new capability semantics.
23. Known information is not repeatedly requested without a material reason.
24. Raw onboarding answers are evidence/provenance, not runtime authority.
25. Localisation cannot create locale-specific semantic meaning.
26. Customer interaction is the preferred initial high-information discovery axis; merchant category remains contextual metadata.
27. Public location is optional and is not merchant eligibility or verification.
28. Business Hours prompting respects merchant/location scope under MS-PROT-050 v1.2.
29. External calendar questioning occurs only after relevant Scheduling semantics and does not activate them.
30. Integration/provider prompts remain downstream of semantic need.
31. Required semantic dependencies are not presented as fake optional merchant choices.
32. Registered defaults may reduce questioning while retaining default provenance.
33. Prompt eligibility and question reason remain explainable.
34. Resume/evolution recomputes the unresolved graph rather than relying on a fixed wizard step.
35. Onboarding may not pre-design unsupported future capability choices.

---

# 64. Deferred decisions

This contract deliberately leaves downstream:

```text
exact frontend component library
exact screen layout / transition animation
exact copy/localisation text
AI/inference provider and model
prompt-ranking implementation details beyond accepted priority rules
question analytics / experimentation framework
save/resume persistence technology
exact Ordering configuration questions not yet semantically specified
exact Payment questions not yet semantically specified
exact Delivery/Collection questions not yet semantically specified
advanced Booking policy questions not yet semantically specified
other future capability-specific question presentations
```

These are not blockers to the prompt architecture.

Future capability-specific onboarding questions must be introduced only after the applicable semantic/configuration contract exists.

---

# 65. Implementation consequence

During the active design-first phase, this document does not mandate immediate implementation.

A future implementation should conceptually preserve:

```text
Prompt registry
        ≠
Semantic registry

Question presentation
        ≠
Capability configuration definition

Answer/provenance
        ≠
Active configuration
```

No giant hard-coded wizard or business-category-specific questionnaire should be introduced.

Implementation should begin from conformance tests only after design closure and an ordered TDD plan.

---

# 66. Continuous-improvement checkpoint

The review that produced this contract found several improvements over the earlier onboarding concept:

1. separate discovery, decision, data and review interactions instead of treating all as questions;
2. use a small reusable discovery catalogue rather than category questionnaires;
3. use customer interaction as the highest-information initial discriminator rather than business category;
4. allow natural language to preselect bounded options instead of replacing them;
5. let final review confirm several explicit choices and reduce screen count;
6. make `VARIES`, `NOT_SURE` and `OTHER` onboarding-control outcomes rather than accidental runtime semantics;
7. version question interpretations so historical answers cannot be reinterpreted;
8. refuse to design future capability semantics inside the questionnaire catalogue.

No further material correction was found within this bounded prompt/catalogue scope after cross-domain review.

---

# Governance verdict

**ACCEPTED.**

Canonical decision:

> **Main Street onboarding is a composed, adaptive presentation over registered semantics, configuration decisions and merchant data contracts. It begins with a small set of high-information business-intent discovery questions, uses deterministic applicability and branch pruning to ask only unresolved relevant prompts, presents bounded configuration choices in business language, collects structured data under its real authority, permits AI only to interpret/preselect/rank eligible registered choices, and culminates in merchant review before deterministic validation. Questionnaires never become a parallel semantic model.**
