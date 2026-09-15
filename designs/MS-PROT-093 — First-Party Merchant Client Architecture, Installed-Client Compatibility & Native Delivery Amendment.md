# MS-PROT-093 — First-Party Merchant Client Architecture, Installed-Client Compatibility & Native Delivery Amendment

**Document ID:** MS-PROT-093  
**Version:** 1.0  
**Status:** **ACCEPTED**  
**Approved:** Explicit manual approval on 13 September 2026 after Fundamental Vision Conformance, architecture review, falsification, trade-off review, complete pre-approval presentation and structural/UI visualisation  
**Authority type:** Material delivery-surface / client-architecture amendment  
**Governed by:** `MS-DESIGN-RULES-001`; `DOCUMENT-GOVERNANCE.md`  
**Fundamental authority:** `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-035 within independently installed first-party client compatibility and client-SDK scope; MS-PROT-037 within merchant delivery-surface portfolio scope; composite MS-PROT-063 within installed-native session-transport scope  
**Preserves:** ADR-014 as the browser authentication/session profile; MS-PROT-036 customer/storefront architecture; capability-owned business semantics; Projection/Exposure separation; Actor Authorisation; Merchant Scope; Merchant Operational Device Context; business-type neutrality; mobile-first ordinary operation  
**Depends on:** MS-FUNDAMENTAL-VISION-001; MS-PROT-027; MS-PROT-029; composite MS-PROT-035; MS-PROT-036; MS-PROT-037; composite MS-PROT-057; composite MS-PROT-059; composite MS-PROT-062; composite MS-PROT-063; MS-PROT-067; MS-PROT-068; MS-PROT-069; MS-PROT-070; composite MS-PROT-074; ADR-014  
**Closes:** the deferred native merchant-client delivery-surface decision; ADR-014-DQ-014 within native merchant-client authentication/session architecture; the former MS-PROT-035 client-SDK-generation deferral within first-party merchant clients  
**Purpose:** Establish Main Street's first-party merchant-client architecture across merchant web and admitted installed native platform families, including independently deployed installed-client compatibility, contract-driven native implementation, deterministic generated tooling, cross-platform conformance, platform-local reuse, bounded compile-time declarative generation, native session transport, offline boundaries and anti-semantic-gravity constraints.

---

# 0. Fundamental Vision Conformance

## 0.1 Product-purpose test

Main Street exists so that micro and small businesses can operate with sophisticated digital infrastructure without acquiring corresponding software-administration complexity.

The merchant-client architecture therefore optimises for:

```text
business concepts
rather than
software architecture

role-native work
rather than
module navigation

platform-appropriate operation
rather than
framework uniformity

shared business correctness
rather than
maximum shared UI source code
```

The architecture introduces substantial internal engineering complexity:

```text
multiple native client implementations
contract generation
compatibility management
cross-platform conformance
multiple release pipelines
platform-specific testing
```

That complexity remains inside Main Street.

It MUST NOT create additional:

```text
merchant configuration
staff training
software terminology
merchant integration responsibility
business-rule duplication
```

## 0.2 Feature-admission test

The proposal satisfies primarily **Administrative Compression**.

Installed native clients can absorb platform/device concerns such as secure credentials, background continuity, notifications, camera/device interaction, local state, desktop input and operational-device integration rather than requiring merchants to manage those mechanisms.

It also supports **Representation**: some merchant operating contexts materially benefit from platform-native interaction, especially constrained mobile operation and richer desktop/device-oriented workflows.

It also supports **Coordination**: all client surfaces converge on the same capability-owned application and projection authority instead of evolving separate business interpretations.

## 0.3 Complexity justification

The architecture does not introduce native applications merely because native development is technically attractive.

Native clients are justified only because Main Street is an operational product expected to function in materially different device contexts while preserving:

```text
low cognitive burden
mobile-first operation
role-native interaction
platform accessibility
operational reliability
device integration
```

The architecture deliberately rejects a universal cross-platform runtime where that runtime would become a second source of complexity or constrain platform-native UX.

## 0.4 Vision result

```text
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY
```

The additional internal complexity is justified by role-native operation, platform/device integration, client reliability and reduction of semantic drift.

The complexity does not become merchant-visible architecture.

---

# 1. Problem

Main Street has one authoritative capability-driven operational backend but potentially several first-party merchant interaction environments.

The relevant environments include:

```text
merchant web
Android
iOS
Windows
macOS
```

A naive implementation produces one of two failure modes.

## Failure mode A — independent client semantics

```text
backend contract
    ↓
Android interprets it one way
iOS interprets it another
Windows another
web another
```

This causes:

```text
business-rule duplication
error-handling drift
permission drift
inconsistent retry behaviour
inconsistent stale-state handling
inconsistent role behaviour
```

## Failure mode B — universal client runtime

```text
backend
    ↓
universal interaction IR
    ↓
universal renderer/runtime
    ↓
all platforms
```

This introduces:

```text
lowest-common-denominator interaction
generic UI DSL pressure
runtime compatibility machinery
presentation semantic gravity
platform abstraction leakage
custom-surface escape complexity
```

Main Street requires a third model.

---

# 2. Governing Decision

Main Street SHALL adopt a **Contract-Driven First-Party Client Architecture**.

Installed native merchant clients SHALL use a **Contract-Driven Native** profile.

The architecture shares:

```text
authoritative business semantics
application contracts
projection/exposure contracts
transport representations
problem/outcome vocabularies
design language
conformance obligations
generated contract tooling
```

The architecture does NOT require sharing:

```text
native view hierarchies
navigation implementation
layout geometry
platform controls
window organisation
device interaction
platform-local abstractions
```

Canonical:

```text
              MAIN STREET AUTHORITATIVE CORE
                         │
               capability-owned semantics
                         │
                application authority
                         │
             projection / Exposure authority
                         │
              registered API contracts
                         │
        language-neutral client contract bundle
                         │
        ┌────────────────┼─────────────────┐
        │                │                 │
 generated SDKs    design language    conformance
        │                │                 │
        └────────────────┼─────────────────┘
                         ↓
                 FIRST-PARTY CLIENT
                         │
       ┌─────────────────┼──────────────────┐
       │                 │                  │
 purpose-built      platform-local      bounded
 native UX          reusable code       generation
       │                 │                  │
       └─────────────────┼──────────────────┘
                         ↓
                 platform experience
                         ↓
                   user intent
                         ↓
             capability-owned operation
                         ↓
            authoritative revalidation
```

---

# 3. Merchant Delivery-Surface Portfolio

This authority resolves the native-app decision deferred by MS-PROT-037.

## 3.1 Merchant web

`merchant-web` remains a first-class Main Street merchant surface.

It SHALL remain:

```text
broadly accessible
mobile-capable
capability-driven
role-sensitive
business-facing
```

It remains especially valuable for:

```text
zero-install access
onboarding
administration
recovery access
unsupported-device access
fallback during native-client update requirements
```

The introduction of native clients does not demote or replace `merchant-web`.

## 3.2 Native mobile surface class

Main Street SHALL admit installed native merchant applications as first-class operational surfaces.

The target mobile platform families are:

```text
Android
iOS
```

Native mobile applications are justified by Main Street's mobile-first operating requirement and MAY provide stronger platform integration for:

```text
notifications
camera/media capture
secure credential storage
biometric/passkey interaction
background continuity
local state
device-aware workflows
```

## 3.3 Native desktop surface class

Main Street SHALL also admit installed native desktop merchant applications.

The target desktop platform families are:

```text
Windows
macOS
```

Desktop clients MAY use the larger workspace and native desktop interaction model for:

```text
dense operational views
keyboard interaction
pointer interaction
multi-window operation
peripheral-oriented workflows
high-volume operational work
```

## 3.4 Platform admission does not require simultaneous implementation

The accepted surface portfolio does NOT require:

```text
Android launch
+
iOS launch
+
Windows launch
+
macOS launch
```

to occur simultaneously.

Implementation sequencing is a programme decision.

A platform may enter production only when its required:

```text
security
compatibility
quality
accessibility
conformance
operational support
```

are proven.

## 3.5 No platform-specific business model

A merchant MUST NOT become a different Main Street business because they use:

```text
web
Android
iOS
Windows
macOS
```

Platform differences remain delivery differences.

Canonical:

```text
same merchant
same configuration
same capability authority
same business commitments
different client expression
```

---

# 4. Amendment to MS-PROT-037 Direction

The existing dashboard-composition principle is preserved.

The following remains authoritative:

```text
Merchant Configuration
+
Enabled Capabilities
+
Actor Authority
+
Operational Projections
        ↓
Merchant Experience Composition
```

The previous `merchant-web`-only delivery assumption is widened to:

```text
Merchant Operational Model
        ↓
first-party merchant client surfaces
        ├── merchant-web
        ├── Android
        ├── iOS
        ├── Windows
        └── macOS
```

The following existing MS-PROT-037 principles remain preserved:

```text
business-facing terminology
capability-driven composition
actor-specific projection
operations before administration
mobile-first ordinary operation
business-type neutrality
backend-enforced authority
progressive recomposition
low merchant-visible complexity
```

No accepted semantic invariant is removed.

---

# 5. Contract-Driven Boundary

All first-party merchant clients SHALL consume registered Main Street application/API contracts.

Clients SHALL NOT redefine business operations locally.

Canonical:

```text
native/web interaction
        ↓
registered client/API contract
        ↓
trusted context
        ↓
application operation/query
        ↓
capability-owned semantics
```

Client source code MAY contain:

```text
presentation orchestration
navigation
local UI state
draft input
formatting
platform integration
non-authoritative caching
```

Client source code MUST NOT become owner of:

```text
booking eligibility
cancellation policy
inventory authority
allocation authority
payment truth
merchant entitlement
staff authority
provider readiness
business lifecycle rules
```

---

# 6. Language-Neutral Client Contract Bundle

Main Street SHALL produce a deterministic **Client Contract Bundle** from accepted registered API contracts.

The Client Contract Bundle is DERIVED engineering material.

It is not semantic authority.

It SHALL contain the transport information required to generate and test clients, including where applicable:

```text
ApiContractIdentity
transport representation version
request representation
response representation
safe problem/outcome representation
idempotency requirement
concurrency precondition representation
pagination representation
time representation
money representation
media-transfer coordination representation
```

The bundle MUST NOT add:

```text
new business rule
new permission
new policy
new workflow
new capability applicability
```

If the generated bundle conflicts with accepted API authority:

```text
accepted API authority wins
+
generation/build fails
```

---

# 7. Installed-Client Contract Representation Versioning

The existing stable `ApiContractIdentity` remains the logical contract identity.

Installed clients require an additional delivery concern: **Client Contract Representation Version**.

Conceptually:

```text
ApiContractIdentity
+
ClientContractRepresentationVersion
```

identifies one client-consumable transport representation.

Representation versioning MUST NOT change semantic ownership.

For example:

```text
ordering / order-create
representation 1
representation 2
```

MAY coexist if both faithfully invoke the same accepted Order operation.

A materially different business operation MUST NOT be hidden behind a transport-version increment.

---

# 8. Compatibility Model for Installed Clients

MS-PROT-035 permits coordinated first-party evolution where deployment coordination is guaranteed.

Installed applications invalidate that assumption.

Therefore:

> Independently installed first-party clients SHALL be treated as independently deployed consumers for compatibility purposes.

## 8.1 Backward compatibility

A backend release MUST continue supporting every Client Contract Representation Version required by every currently supported installed-client generation.

## 8.2 Breaking transport evolution

A breaking client representation change requires:

```text
new representation version
        +
parallel server support
        +
updated generated client
        +
client release
```

Old representation support may be removed only after no supported production client generation requires it.

## 8.3 No silent semantic change

Existing representation meaning MUST NOT silently change.

Additive evolution SHOULD be preferred.

## 8.4 Unsupported installed client

When Main Street can no longer safely support a client generation or requested contract representation:

```text
CLIENT_UPDATE_REQUIRED
```

or an equivalent explicitly registered transport outcome SHALL be returned.

The client MUST NOT continue by guessing newer contract meaning.

## 8.5 Update requirement is not business authority

`CLIENT_UPDATE_REQUIRED` means:

```text
this software client is no longer compatible
```

It does NOT mean:

```text
merchant suspended
subscription lost
actor unauthorised
business operation invalid
```

## 8.6 Emergency retirement

A client generation MAY be retired immediately where continuing service would violate an accepted:

```text
security
privacy
legal
integrity
```

invariant.

Such retirement does not change merchant business state.

Where serviceable, `merchant-web` remains an alternative first-party surface.

---

# 9. Client Build Information

Native clients MAY send non-authoritative build metadata such as:

```text
platform family
application generation
application version
supported contract representations
```

This metadata MAY support:

```text
compatibility diagnostics
update messaging
telemetry
support
```

It MUST NOT establish:

```text
identity
Merchant Scope
Actor Authorisation
Commercial Entitlement
business capability
```

A malicious client lying about its version MUST NOT gain additional business authority.

---

# 10. Generated Client SDKs

Client SDK generation is promoted from an optional deferred implementation possibility to an accepted implementation mechanism.

The architecture SHALL support deterministic generated clients for relevant first-party languages.

Generated SDK responsibilities MAY include:

```text
request/response types
contract identifiers
transport adapters
safe problem/outcome types
pagination primitives
Money transport representation
time transport representation
retry-identity transport
concurrency-precondition transport
media-transfer coordination helpers
test fixtures
```

Generated SDKs MUST NOT contain duplicated domain policy.

The server remains authoritative.

---

# 11. Two Classes of Code Generation

Generated code SHALL have an explicit lifecycle class.

## 11.1 Deterministic Generated Artifact

A **Deterministic Generated Artifact** is regenerated from authoritative/derived contract input.

Examples:

```text
API types
transport clients
problem-code types
design-token output
contract test fixtures
```

Rules:

```text
MUST be reproducible
MUST record generator/input provenance
MUST NOT be manually edited
MUST be replaceable through regeneration
```

Generated output is not independent authority.

## 11.2 One-Time Native Scaffold

A **One-Time Native Scaffold** is developer productivity output.

Example:

```text
initial ProductEditor.swift
initial ProductEditor.kt
initial Windows ProductEditor implementation
```

Once generated:

```text
scaffold generation ends
        ↓
output becomes ordinary native source
```

The generator has no continuing claim that the source remains synchronised.

## 11.3 Prohibition on mixed ownership

A file MUST NOT simultaneously be:

```text
generator-owned
and
developer-owned
```

A deliberate transition from deterministic generation to ordinary source requires explicit reclassification.

---

# 12. Contract Generation Is Not UI Generation

API schema does not mechanically determine appropriate UX.

Invalid:

```text
Money
    ↓
MoneyTextField

Boolean
    ↓
Toggle

Resource
    ↓
DetailScreen
```

as universal architecture.

Correct:

```text
accepted operation/projection meaning
        +
merchant task
        +
role context
        +
platform context
        ↓
client UX decision
```

Contract generation supplies correct data/operation bindings.

It does not choose the complete merchant experience.

---

# 13. Main Street Design Language

Main Street SHALL maintain a shared merchant-client design language.

The design language MAY establish:

```text
semantic colour roles
typographic hierarchy
spacing system
geometry principles
iconography
motion principles
feedback principles
destructive-action treatment
loading/error principles
accessibility principles
business-language principles
```

The design language does not require pixel identity.

Canonical:

```text
shared design intent
        +
platform conventions
        ↓
native platform expression
```

---

# 14. Platform-Local Design Systems

Each native platform MAY maintain platform-local reusable components and patterns.

Examples:

```text
Android-specific components
Apple-specific components
Windows-specific components
```

Platform-local abstractions do NOT automatically become global Main Street abstractions.

A useful SwiftUI abstraction need not be reproduced in Kotlin or C# merely for symmetry.

Global abstraction requires global evidence.

---

# 15. Apple-Family Reuse

iOS and macOS MAY share:

```text
generated contracts
Swift models
networking
formatting
design tokens
non-UI utilities
selected SwiftUI components
```

where genuine compatibility exists.

They MUST remain free to diverge where:

```text
windowing
navigation
keyboard
menu
pointer
layout density
platform convention
```

materially differ.

Shared Apple code is an optimisation, not semantic authority.

---

# 16. Cross-Language Sharing Rule

Main Street SHALL prefer:

```text
shared contracts
+
generated source
+
shared tests
```

over forcing all native clients to consume one cross-platform binary runtime.

A cross-platform shared library MAY be introduced for a bounded problem where:

```text
the abstraction is genuinely platform-neutral
the integration cost is lower than generated/native alternatives
platform-native operation is not degraded
semantic authority remains server-side
```

No cross-platform client runtime is mandatory.

---

# 17. Bounded Declarative Native

**Bounded Declarative Native is retained.**

It is classified as an optional **compile-time implementation optimisation** inside Contract-Driven Native.

It is NOT the governing architecture.

Canonical:

```text
bounded presentation descriptor
        ↓
compile/build-time generator
        ↓
native source / native component composition
        ↓
ordinary native application
```

Not:

```text
server
        ↓
runtime UI description
        ↓
generic interpreter
        ↓
screen
```

---

# 18. Bounded Presentation Descriptor

A bounded declarative descriptor MAY express non-authoritative presentation intent such as:

```text
present a collection
present a summary
collect a value
present an already-established choice
request confirmation
present an already-established consequence
display owner-supplied validation feedback
```

It MUST NOT contain:

```text
business eligibility
business policy
Actor Authorisation
Commercial Entitlement
Provider Readiness
allocation logic
availability authority
stock authority
pricing authority
arbitrary executable expressions
workflow graphs
```

---

# 19. Promotion Criteria for Declarative Generation

A presentation pattern MAY be promoted into bounded declarative generation only when all applicable conditions hold:

```text
1. repeated need has been observed in materially different workflows;

2. the repeated meaning is presentation-level rather than
   capability-specific business meaning;

3. at least two platform implementations can express the
   abstraction without material UX degradation;

4. the abstraction does not require merchant-type branching;

5. the abstraction removes meaningful repetitive engineering
   or defect risk;

6. purpose-built native implementation is not materially
   simpler or superior;

7. accessibility remains platform-correct;

8. no new semantic authority moves into the generator.
```

There is no target percentage for declarative coverage.

---

# 20. Demotion / Abandonment Criteria

A shared generated abstraction SHALL be narrowed, demoted or abandoned when evidence shows:

```text
capability-specific modifiers proliferating
platform exception flags proliferating
accessibility becoming weaker
layout becoming lowest-common-denominator
business rules entering presentation descriptors
one-off workflows expanding the generic vocabulary
purpose-built native implementation becoming simpler
```

Removing a bad client abstraction is preferable to preserving abstraction purity.

---

# 21. Purpose-Built Native Surfaces

Purpose-built native implementation is a first-class mechanism.

It is not an escape-hatch failure.

Appropriate examples may include:

```text
advanced scheduling
POS operation
scanner workflows
camera/video verification
payment-terminal integration
receipt printing
desktop drag/drop
multi-window workflows
dense operational boards
platform-specific system integration
```

The exact list remains evidence-driven.

A purpose-built surface MUST still consume the same authoritative contracts.

---

# 22. No Universal Client UI Runtime

Main Street SHALL NOT introduce by default:

```text
universal Interaction IR
universal Screen DSL
universal Workflow DSL
server-driven arbitrary UI
remote executable presentation rules
cross-platform component interpreter
```

A future proposal for any such mechanism is a new material architectural decision.

It MUST independently prove that repeated real requirements cannot be represented efficiently through:

```text
contract-driven native code
generated tooling
platform-local reuse
bounded compile-time generation
```

---

# 23. Native Authentication and Session Transport

ADR-014 remains authoritative for trusted browser execution.

Installed native clients require a sibling transport profile.

## 23.1 Identity semantics remain unchanged

Native clients MUST use the same accepted:

```text
Identity
authentication authority
Merchant Membership
Actor Authorisation
Merchant Scope
Session authority
```

as other Main Street surfaces.

Native clients do not create a separate identity system.

## 23.2 Passkey-first direction remains

Where passkey authentication is applicable to the actor under accepted authentication policy, installed clients SHOULD use platform-native passkey/authenticator facilities against the same server-side authentication authority.

## 23.3 Native session credential

After successful authentication, a native client MAY receive a fresh high-entropy opaque server-authoritative session credential suitable for installed-client transport.

The credential:

```text
MUST be revocable
MUST NOT encode independent business authority
MUST NOT freeze mutable merchant relationships
MUST NOT be upgraded from attacker-influenced pre-auth state
MUST be protected using platform secure credential storage
MUST be transmitted only through protected transport
```

## 23.4 Staff operational-device requirement remains

For staff merchant-operational access:

```text
valid session
        +
current Merchant Membership
        +
current Merchant Operational Device Context
        +
current Actor Authorisation
```

remain independently required where governed.

A native application install is NOT itself Merchant Operational Device Authorisation.

## 23.5 Native session transport is not browser-cookie transport

Browser host-bound cookie and CSRF rules remain browser-specific.

Native clients MAY use a native-appropriate opaque credential transport.

Exact:

```text
header syntax
credential encoding
secure-storage API
rotation interval
proof-of-possession mechanism
platform attestation
```

remain downstream security implementation choices unless threat-model evidence promotes them to a material architecture decision.

## 23.6 Provider/browser handoff

Where a native application must use a system browser or external provider authorisation flow, callback correlation MUST preserve accepted source/authentication protections.

A callback URL or deep link is not business authority.

---

# 24. Offline Read Behaviour

Installed clients MAY retain local read projections for operational continuity.

Cached local projections remain:

```text
derived
non-authoritative
potentially stale
```

A client MUST NOT convert cached:

```text
availability
inventory
permission
provider readiness
entitlement
```

into mutation authority.

Where material to correct user interpretation, stale/offline state SHALL be indicated.

---

# 25. Offline Mutation Behaviour

Offline mutation requires explicit operation-level support.

Default:

```text
consequential operation
        ↓
ONLINE REQUIRED
```

A command MAY support deferred submission only when its owning operation/API contract establishes that delayed submission is safe under:

```text
logical retry identity
idempotency
conflict behaviour
authority revalidation
business-time consequences
```

Canonical deferred submission:

```text
user intent captured locally
        ↓
logical command identity persisted
        ↓
connectivity restored
        ↓
current authentication/context established
        ↓
server receives same logical intent
        ↓
authoritative revalidation
        ↓
accepted / rejected / conflict
        ↓
client reconciles projection
```

The client MUST NOT tell the merchant that authoritative completion occurred before the server establishes it.

---

# 26. Background and Real-Time Operation

Native clients MAY use platform mechanisms such as:

```text
push notification
background refresh
local notification
WebSocket
SSE
polling
```

where appropriate.

These mechanisms transport information.

They do not own business truth.

A background notification indicating:

```text
Order changed
```

does not itself authorise later mutation.

The client reacquires or uses a serviceable current projection and invokes the applicable operation.

---

# 27. Merchant Attention and Exception-Driven UX

Native and web merchant clients SHOULD exploit Main Street's accepted exception-driven product direction.

Operational home surfaces should emphasise:

```text
what needs attention
what decision is required
what conflict exists
what work is due
```

rather than reproducing every backend capability as navigation.

The client architecture MUST NOT produce:

```text
one module tab per capability
```

merely because a contract exists.

---

# 28. Platform-Native Expression

Shared behavioural meaning does not require shared control selection.

The same operation MAY be represented differently by each platform.

Example:

```text
Reschedule Appointment
```

may use:

```text
Android:
touch-oriented native flow

iOS:
native sheet / temporal controls

Windows:
dense calendar + command interaction

macOS:
sidebar/detail + native menu/toolbar
```

All invoke the same authoritative scheduling operation.

---

# 29. Mobile-First Preservation

Ordinary daily merchant operation MUST remain comfortably possible from a phone.

Native desktop clients MAY increase:

```text
density
parallel visibility
keyboard efficiency
windowing
peripheral integration
```

but MUST NOT create new business semantics merely because a desktop interface exists.

A specialised device-assisted operation MAY have a superior desktop or device-native interaction without changing the underlying business operation.

---

# 30. Accessibility

Accessibility is a platform responsibility constrained by shared design principles.

The shared architecture MAY express platform-neutral meaning such as:

```text
label
semantic grouping
importance
error relationship
action consequence
```

The native platform remains responsible for correct:

```text
screen-reader semantics
keyboard traversal
focus
pointer/touch target behaviour
dynamic text
contrast
native accessibility services
```

A shared abstraction MUST be rejected if it prevents conforming platform accessibility.

---

# 31. Design Excellence

A generated or shared implementation is not successful merely because it functions.

The architecture SHALL consider a reusable client abstraction invalid where it materially prevents:

```text
clarity
platform-native interaction
excellent accessibility
appropriate information density
fast task completion
strong feedback
recoverability
visual quality
```

Design quality is therefore a falsification criterion for code reuse.

---

# 32. Performance and Resource Responsibility

Native clients own platform-specific execution concerns such as:

```text
memory
local storage
battery
lifecycle
background limits
rendering efficiency
window lifecycle
network scheduling
```

These concerns SHALL NOT be forced into a universal cross-platform runtime merely for consistency.

The backend remains responsible for bounded APIs, pagination, projections and avoiding unnecessary client data volume.

---

# 33. Design Tokens and Generated Design Assets

Canonical design tokens MAY be transformed deterministically into platform-specific assets.

Examples:

```text
canonical semantic token
        ↓
Compose token
Swift token
WinUI token
web token
```

Platform-specific accessibility or native convention MAY override a raw visual value while preserving the semantic design role.

The token generator does not determine business meaning.

---

# 34. Localisation and Business Language

Client localisation MAY be centralised through generated/localised resources.

Labels MUST NOT become semantic identifiers.

Canonical business terminology should derive from accepted product/domain vocabulary.

Different platforms MAY phrase instructions differently where grammar, space or interaction context requires it, provided business meaning is preserved.

---

# 35. Cross-Platform Conformance

Conformance—not identical source code—is the primary mechanism preventing behavioural drift.

Main Street SHALL maintain cross-platform conformance evidence for material client behaviour.

Conformance MAY cover:

```text
correct contract invocation
correct semantic outcome handling
correct conflict behaviour
correct unauthorised behaviour
correct update-required behaviour
correct stale/offline presentation
correct retry behaviour
required role visibility
required accessibility behaviour
```

---

# 36. Authority Provenance of Conformance

Conformance tests/specifications are evidence.

They are NOT independent semantic authority.

Required dependency:

```text
accepted authority
        ↓
testable client obligation
        ↓
conformance scenario
        ↓
platform evidence
```

Prohibited:

```text
test says X
        ↓
therefore X becomes business truth
```

If a conformance scenario requires materially new behaviour absent from accepted authority:

```text
STOP
        ↓
DESIGN-RULES lifecycle
```

---

# 37. Conformance Layers

The client verification model SHOULD distinguish:

## Contract conformance

Does the client correctly consume the registered transport contract?

## Behavioural conformance

Does the client correctly represent and invoke the accepted business interaction?

## Platform conformance

Does the implementation satisfy platform-specific accessibility, security, lifecycle and interaction requirements?

## Product-design conformance

Does the experience remain business-facing, low-burden and role-native?

These layers MUST NOT be collapsed into one generic pass/fail test.

---

# 38. Generated Fixture Strategy

The contract toolchain MAY generate:

```text
valid response fixtures
safe rejection fixtures
conflict fixtures
stale/read fixtures
pagination fixtures
Money/time fixtures
```

for use across platform test suites.

Generated fixtures MUST derive from registered representations.

They MUST NOT fabricate new business semantics.

---

# 39. AI-Assisted Client Engineering

AI MAY be used aggressively to improve engineering throughput.

AI MAY:

```text
generate initial native implementations
translate an approved interaction to another platform
generate tests from accepted contract fixtures
suggest platform-specific refactoring
generate documentation
identify contract drift
```

AI-generated client code has no special authority.

Every output remains subject to:

```text
accepted contracts
native build/compiler
static checks
security checks
conformance
accessibility review
platform review
human approval where required
```

AI SHOULD increase implementation output.

It MUST NOT motivate a weaker runtime architecture merely because shared code would otherwise be convenient.

---

# 40. Reuse Hierarchy

Main Street SHALL maximise reuse in the following order:

```text
1. authoritative semantics

2. registered transport contracts

3. generated client contracts/SDKs

4. shared conformance fixtures

5. shared design language/tokens

6. platform-family shared utilities/components

7. platform-local reusable components

8. bounded declarative compile-time generation

9. AI-assisted generation

10. purpose-built native implementation
```

This hierarchy does NOT mean lower entries are inferior.

It means global abstraction should be introduced only where the more fundamental forms of reuse do not already solve the problem.

---

# 41. Reuse Is Not Measured by Shared-Code Percentage

Main Street SHALL NOT optimise for:

```text
80% shared UI
90% shared code
minimum lines per platform
```

as an architectural objective.

The optimisation target is:

```text
maximum semantic consistency
+
maximum product/design quality
+
maximum engineering leverage
+
minimum speculative runtime abstraction
```

---

# 42. Correlated Generator Failure

Code generation introduces correlated failure risk.

A defective generator can reproduce the same defect across multiple platforms.

Therefore deterministic generators SHALL have:

```text
unit tests
contract fixtures
versioned generator identity
reproducible output
per-platform generated-code compilation
```

A generator passing its own tests does not remove platform-specific conformance requirements.

---

# 43. Generator Versioning

Generated output SHALL retain sufficient provenance to determine:

```text
generator version
input contract/bundle version
generation timestamp/build identity where useful
```

A generator change that alters material client behaviour MUST undergo the same review level applicable to that behaviour.

Formatting-only or equivalent source changes remain implementation details.

---

# 44. Observability

Client requests SHOULD provide safe operational metadata sufficient to diagnose:

```text
platform family
client build
contract identity
representation version
request correlation
```

where useful.

This metadata MUST NOT become:

```text
identity authority
Merchant Scope
business operation identity
```

Client diagnostics SHOULD allow tracing:

```text
user interaction
        ↓
client contract invocation
        ↓
backend API contract
        ↓
application operation/query
        ↓
safe result
```

without exposing sensitive information.

---

# 45. Client Telemetry

Product/UX telemetry MAY be collected where permitted by data-protection authority.

Telemetry is analytical evidence.

It is not:

```text
business state
customer commitment
staff authority
merchant policy
```

A telemetry event such as:

```text
reschedule_screen_opened
```

MUST NOT become an Appointment business fact.

---

# 46. Release Independence

Native clients and backend releases MAY proceed independently subject to compatibility rules.

A backend release MUST NOT require simultaneous app-store deployment to preserve an already-supported client operation.

A native release MUST NOT require backend business-semantic duplication to operate against an older compatible representation.

The contract-compatibility layer absorbs legitimate deployment skew.

---

# 47. Native Release Integrity

Each native production artifact SHALL use the applicable platform signing/distribution mechanism.

Exact:

```text
store
package format
notarisation mechanism
CI vendor
release service
```

are implementation/operations decisions.

Production clients MUST be distinguishable from untrusted arbitrary clients through accepted distribution/security controls where this is required for security decisions.

Distribution identity alone does not grant merchant authority.

---

# 48. App-Store or Distribution Delay

A delayed native release MUST NOT force an already-supported client generation to stop functioning merely because a newer server exists.

The backend maintains compatible representations until the applicable client generation is formally retired.

This isolates:

```text
server deployment
from
store approval/release timing
```

---

# 49. Web Fallback and Resilience

Where serviceable, `merchant-web` SHOULD remain available when:

```text
native client requires update
merchant uses unsupported device
native release is delayed
native platform-specific integration fails
```

The web fallback does not bypass security or authority requirements.

A native-client outage does not redefine merchant business truth.

---

# 50. Customer-Facing Storefront Boundary

This authority does NOT replace or weaken MS-PROT-036.

Customer-facing merchant websites remain:

```text
shared storefront-web runtime
+
capability-owned projections/interactions
+
merchant content
+
bounded presentation composition
```

No Main Street customer native application is created by this authority.

The merchant-client and storefront architectures may share:

```text
API infrastructure
design-quality principles
contract tooling
```

but they remain separate delivery surfaces for different actors.

---

# 51. Business-Type Neutrality

No first-party client SHALL choose business behaviour from:

```text
SALON
MOTEL
SHOP
SOLICITOR
RESTAURANT
```

when the difference is already expressed by accepted capability/configuration authority.

Invalid:

```text
if merchantType == MOTEL
    enable booking logic
```

Required direction:

```text
accepted configuration
+
capability/application contracts
+
actor authority
        ↓
applicable merchant experience
```

Business category MAY influence bounded presentation defaults without becoming operational authority.

---

# 52. Feature Availability and Client Capability

A client may not yet implement every possible Main Street surface.

Absence of client implementation does NOT mean:

```text
capability absent
merchant not entitled
operation invalid
```

The client SHOULD provide an appropriate alternative where required by product policy, including:

```text
merchant-web handoff
upgrade path
safe unavailable message
```

No client may fabricate a weaker substitute operation.

---

# 53. Platform-Specific Enhancements

A platform MAY provide an enhancement unavailable elsewhere where the enhancement:

```text
does not alter authoritative business meaning
does not create new platform-specific business truth
does not make platform UI state mutation authority
```

Examples include:

```text
keyboard shortcut
native share sheet
window arrangement
system notification action
platform search integration
```

---

# 54. Security Boundary

Native clients are untrusted application callers with trusted authentication/security evidence established by the server.

A native binary MUST NOT be trusted merely because Main Street produced it.

The backend continues to enforce:

```text
authentication
Merchant Scope
Actor Authorisation
Commercial Entitlement
Operational Eligibility
Provider Readiness
authoritative concurrency
```

Client-side hiding/disabling remains UX only.

---

# 55. No Client Business Secret Assumption

Main Street MUST assume installed client code and local storage may be inspected.

Security MUST NOT depend on:

```text
hidden client algorithm
embedded provider secret
obfuscated business rule
secret endpoint behaviour
```

Long-lived provider credentials and semantic authority remain server-side.

---

# 56. Failure Handling

Clients SHALL preserve stable API problem distinctions required for correct recovery.

A client MUST NOT collapse:

```text
CONFLICT
NOT_AUTHORISED
NOT_ENTITLED
PROVIDER_UNAVAILABLE
OUTCOME_UNCERTAIN
CLIENT_UPDATE_REQUIRED
```

into one generic retry path.

Client recovery behaviour follows the applicable accepted API/owner contract.

---

# 57. Falsification — Low-Software-Capacity Merchant

Scenario:

A sole trader opens Main Street to see today's work.

Failure condition:

```text
merchant must choose:
Booking module
Scheduling module
Projection module
```

Result:

```text
FAIL
```

Required architecture:

```text
Today
    09:00 Sarah
    11:30 David

1 item needs attention
```

The contract/client architecture remains invisible.

**PASS under this authority.**

---

# 58. Falsification — Ordinary Staff

Scenario:

A receptionist must mark an arriving customer present.

Required:

```text
Sarah Jones
10:30

[Check In]
```

The staff member does not learn:

```text
OperationRuntime
Appointment transition
Actor Authorisation model
```

**PASS.**

---

# 59. Falsification — Old Client, New Backend

Scenario:

iOS application requires representation `appointment/reschedule@2`.

Backend deploys a newer representation.

Required:

```text
@2 remains supported
```

until the client generation using it leaves support.

No silent reinterpretation.

**PASS.**

---

# 60. Falsification — Removed Compatibility

Scenario:

An obsolete client requests a representation no longer safely supported.

Required:

```text
CLIENT_UPDATE_REQUIRED
```

No generic server error.

No business suspension.

**PASS.**

---

# 61. Falsification — Generated SDK Drift

Scenario:

Generated Swift request type differs from accepted contract bundle.

Required:

```text
generation/conformance build fails
```

No manual patch to generator-owned file.

**PASS.**

---

# 62. Falsification — Scaffold Customisation

Scenario:

A generated Product Editor becomes highly customised.

Required:

```text
one-time scaffold
        ↓
ordinary Swift/Kotlin/C# source
```

No false continuing generator ownership.

**PASS.**

---

# 63. Falsification — Conformance Becomes Authority

Scenario:

A shared test says cancellation requires a new business restriction absent from accepted owner authority.

Required:

```text
STOP
→ design lifecycle
```

The test cannot establish policy.

**PASS.**

---

# 64. Falsification — Complex Scheduler

Scenario:

A Windows scheduler requires:

```text
drag/drop
multi-resource overlays
keyboard commands
dense timeline
```

Expanding a shared declarative vocabulary would create a UI language.

Required:

```text
purpose-built native scheduler
```

using accepted scheduling contracts.

**PASS.**

---

# 65. Falsification — POS / Peripheral Workflow

Scenario:

A merchant uses scanner, printer and payment terminal.

Required:

```text
platform/device integration
        ↓
same Ordering / Payment / Inventory operations
```

Peripheral orchestration does not become Order or Payment authority.

**PASS.**

---

# 66. Falsification — Offline Stale Availability

Scenario:

Client cached a 10:00 appointment slot.

Merchant later books it elsewhere.

Offline client reconnects and submits.

Required:

```text
authoritative revalidation
        ↓
CONFLICT
```

No double booking.

**PASS.**

---

# 67. Falsification — Queued Unsafe Operation

Scenario:

Client attempts to queue a consequential operation offline whose owner does not permit deferred submission.

Required:

```text
ONLINE REQUIRED
```

No local assumption of completion.

**PASS.**

---

# 68. Falsification — Platform-Specific UI

Scenario:

iOS uses a sheet while Windows uses an inline scheduling panel.

Both invoke the same operation and satisfy the same product obligations.

Required result:

```text
both conform
```

Visual identity is not required.

**PASS.**

---

# 69. Falsification — Business-Type Branching

Scenario:

Renderer contains:

```text
if salon
if motel
if restaurant
```

to determine business operations.

Required:

```text
FAIL
```

The difference must come from accepted capabilities/configuration.

---

# 70. Falsification — Primitive Proliferation

Scenario:

A reusable generator acquires:

```text
17 capability-specific flags
12 platform exceptions
arbitrary conditions
```

Required:

```text
demote/narrow/remove abstraction
```

Do not preserve generic machinery.

**PASS boundary established.**

---

# 71. Falsification — App-Store Delay

Scenario:

Backend deployment completes while mobile release is still awaiting distribution.

Required:

```text
existing supported app remains compatible
```

No forced lockout merely due release ordering.

**PASS.**

---

# 72. Falsification — Security Emergency

Scenario:

A client generation contains a material exploitable security defect.

Required:

```text
generation retired
        ↓
CLIENT_UPDATE_REQUIRED
```

Compatibility does not override security.

**PASS.**

---

# 73. Falsification — Revoked Operational Device

Scenario:

Staff has:

```text
valid cached session
+
revoked Merchant Operational Device Authorisation
```

Required:

```text
future staff operational context fails
```

Native client continuity does not freeze device trust.

**PASS.**

---

# 74. Falsification — Generator Bug

Scenario:

A generator creates an incorrect Money decoder on three platforms.

Required controls:

```text
generator tests
+
shared Money fixtures
+
per-platform compilation/conformance
```

The architecture recognises correlated-generation risk.

**PASS with controls.**

---

# 75. Falsification — AI-Generated Native Screen

Scenario:

AI produces attractive SwiftUI code that locally decides whether an appointment may be cancelled.

Required:

```text
FAIL
```

AI implementation must consume owner-established operation results.

---

# 76. Falsification — Platform Feature Missing

Scenario:

macOS client has not implemented a new workflow yet.

Required:

```text
business capability still exists
```

The client may direct the merchant to another supported first-party surface if product policy requires access.

No weaker substitute semantics are invented.

**PASS.**

---

# 77. Falsification — Client Lies About Version

Scenario:

A modified client claims to be a newer supported build.

Required:

```text
no authority increase
```

Every API request remains subject to current server-side semantics/security.

Build metadata is not authority.

**PASS.**

---

# 78. Architecture Watch Conditions

The following require architecture review:

```text
presentation generator begins evaluating business policy

client contract bundle begins containing domain rules

one UI abstraction accumulates capability-specific switches

client SDK becomes source of semantic truth

shared client runtime becomes necessary for correctness

server begins sending arbitrary executable UI behaviour

native client version begins affecting Merchant Entitlement

platform-specific business state appears

generated code cannot be reproduced deterministically

supported-client compatibility causes uncontrolled backend branching
```

Repeated evidence may justify a new design.

It does not justify silent architectural expansion.

---

# 79. Alternatives Reviewed

## Web-only merchant client

Advantages:

```text
single deployment
single frontend codebase
simple compatibility
```

Trade-off:

```text
less native platform integration
less platform-specific UX freedom
potentially weaker device/peripheral/background experience
```

Rejected as the exclusive long-term merchant-client architecture.

`merchant-web` remains first-class.

## Four independent native clients without shared contracts/tooling

Advantages:

```text
maximum local freedom
simple platform-local reasoning
```

Trade-off:

```text
contract interpretation drift
duplicated transport work
duplicated fixtures
higher behavioural divergence risk
```

Rejected as governing architecture.

## Universal cross-platform UI runtime

Advantages:

```text
maximum immediate shared UI implementation
```

Trade-off:

```text
runtime abstraction
framework dependency
desktop compromise risk
platform integration escape paths
lowest-common-denominator pressure
```

Not selected as the governing architecture.

## Universal Declarative Native runtime

Advantages:

```text
native rendering with shared interaction description
```

Trade-off:

```text
new Interaction IR
runtime renderer compatibility
generic UI-language pressure
semantic gravity
```

Rejected as governing architecture.

## Contract-Driven Native + bounded compile-time generation

Advantages:

```text
native design freedom
shared semantics
generated repetitive transport work
bounded shared presentation leverage
no universal runtime
strong conformance
```

Trade-off:

```text
multiple codebases
multiple release pipelines
compatibility management
generator/conformance tooling
```

Selected.

---

# 80. Principal Trade-off

The selected architecture intentionally pays more in:

```text
native source code
platform engineering
release management
cross-platform QA
```

to avoid paying more in:

```text
runtime UI abstraction
framework coupling
semantic duplication
lowest-common-denominator UX
generic presentation machinery
```

Generated tooling, AI assistance, design tokens, platform-local reuse and bounded compile-time generation are used to reduce the repetitive portion of that cost.

---

# 81. Simplicity Test

This authority introduces more engineering machinery than one web application.

However it avoids introducing a new universal runtime.

The complexity is concentrated in:

```text
contract generation
compatibility
tooling
testing
```

which are technical responsibilities Main Street can absorb centrally.

The merchant sees:

```text
their business
their work
their decisions
```

not the machinery.

This satisfies the Main Street Complexity Burden Rule.

---

# 82. Deferred Implementation Decisions

The following remain safely downstream because materially observable accepted business semantics can remain equivalent:

```text
exact Android UI toolkit/version
exact Apple UI toolkit/version
exact Windows UI toolkit/version
exact client HTTP library
exact contract-bundle encoding
exact OpenAPI/JSON Schema/codegen implementation
exact SDK-generator implementation language
exact native credential header syntax
exact secure-storage API
exact proof-of-possession mechanism unless required by threat model
exact app packaging/store pipeline
exact CI vendor
exact screenshot testing framework
exact accessibility-test tooling
exact telemetry SDK
exact numeric performance budgets
exact client support-window duration
exact native platform implementation sequence
```

Before production launch, operational policy MUST establish the support window and release criteria.

These implementation decisions MUST conform to this authority.

---

# 83. Decisions Explicitly Resolved by This Authority

This authority resolves:

```text
native merchant-client class
    ACCEPTED

merchant-web replacement by native
    REJECTED — web remains first-class

Android/iOS native target families
    ADMITTED

Windows/macOS native target families
    ADMITTED

simultaneous four-platform launch
    NOT REQUIRED

native client business authority
    REJECTED

contract-driven first-party clients
    ACCEPTED

installed-client independent compatibility
    REQUIRED

client contract representation versioning
    REQUIRED

client SDK generation
    ACCEPTED

generated-code ownership ambiguity
    RESOLVED into deterministic vs scaffold classes

universal client UI runtime
    REJECTED

runtime server-driven arbitrary UI
    REJECTED initially

bounded declarative generation
    ACCEPTED as compile-time optimisation only

cross-platform conformance
    REQUIRED as evidence, not authority

native session transport profile
    ACCEPTED as sibling to browser profile

offline authoritative mutation
    REJECTED unless owner explicitly permits deferred submission
```

---

# 84. Preservation Matrix — No Silent Removal

Every material element from the preceding proposals is preserved or explicitly reclassified.

| Previous element | Revised disposition |
|---|---|
| Contract-Driven Native | **Retained; native profile of governing first-party client architecture** |
| Generated tooling | **Retained and strengthened** |
| Generated SDKs | **Retained; deterministic derived artifacts** |
| Shared behavioural conformance | **Retained; explicitly evidence, not authority** |
| Shared design language | **Retained** |
| Design tokens | **Retained with deterministic platform generation** |
| Bounded Declarative Native | **Retained; compile-time implementation optimisation** |
| Runtime Interaction IR | **Explicitly rejected as governing mechanism** |
| Native renderer freedom | **Retained** |
| Purpose-built native surfaces | **Retained as first-class** |
| Platform-local reusable components | **Retained** |
| Apple-family selective sharing | **Retained** |
| No shared-pixel requirement | **Retained** |
| Compile-time generation | **Retained and strengthened** |
| Disposable one-time generation | **Retained as One-Time Native Scaffold** |
| Generator-owned source | **Retained separately as Deterministic Generated Artifact** |
| AI implementation leverage | **Retained and constrained by conformance** |
| Offline caching | **Retained with explicit authority boundary** |
| Pending offline commands | **Retained only where owner explicitly permits deferred submission** |
| Platform-native accessibility | **Retained** |
| Platform-native performance/resource control | **Retained** |
| Native device integration | **Retained** |
| Excellent design everywhere | **Retained as abstraction falsification criterion** |
| No declarative coverage target | **Retained** |
| Business-type neutrality | **Retained** |
| Server-authoritative semantics | **Retained** |
| Merchant-web | **Preserved and amplified as first-class/fallback surface** |
| Customer storefront | **Explicitly preserved unchanged under MS-PROT-036** |
| Four native platform families | **Admitted, but simultaneous release not required** |
| Installed-client compatibility | **Resolved** |
| Native authentication/session transport | **Resolved at architecture level** |
| Contract representation versioning | **Added** |
| App-store/release skew | **Resolved** |
| Generator correlated-failure risk | **Addressed** |
| Client build metadata | **Bounded as non-authoritative** |
| Compatibility update-required outcome | **Established** |
| Web fallback during native incompatibility | **Established** |

Nothing material from the approved architecture is silently dropped.

---

# 85. Accepted Invariants

1. Main Street retains one capability-owned authoritative operational core.
2. Merchant clients are delivery surfaces, never separate business engines.
3. `merchant-web` remains first-class.
4. Installed native merchant clients are an accepted delivery-surface class.
5. Android and iOS are admitted native mobile target families.
6. Windows and macOS are admitted native desktop target families.
7. Simultaneous implementation of all native clients is not required.
8. Business semantics do not vary by client platform.
9. API/application contracts remain the client semantic boundary.
10. Independently installed clients require independent compatibility handling.
11. `ApiContractIdentity` remains distinct from client transport representation version.
12. Supported installed clients MUST NOT be silently broken by backend deployment.
13. Unsupported clients fail explicitly through client-compatibility outcome, not business-state mutation.
14. Client build/version claims do not grant authority.
15. Generated SDKs are derived from accepted contracts.
16. Deterministic generated artifacts and one-time scaffolds are distinct.
17. Generator-owned files are not manually edited.
18. API schemas do not mechanically determine UX.
19. Conformance evidence derives from authority and cannot create authority.
20. Design language is shared; platform expression may differ.
21. Platform-local abstractions do not require global promotion.
22. Bounded declarative generation is compile-time only unless a later accepted design establishes otherwise.
23. No universal Interaction IR or UI runtime is accepted.
24. Purpose-built native UX is first-class.
25. Native clients use existing Identity/business authority.
26. Native session transport remains server-authoritative and revocable.
27. Staff operational device authority remains independent from native install/session.
28. Offline reads remain non-authoritative.
29. Offline mutation is online-required unless explicitly permitted by owner authority.
30. Background/push transport does not create business truth.
31. Ordinary daily merchant operation remains mobile-first.
32. Accessibility remains platform-native.
33. Shared abstractions must be abandoned when they reduce design quality.
34. Business category does not drive operational client branching.
35. Client security never depends on hidden source code or embedded business secrets.
36. AI-generated client code has no semantic authority.
37. Reuse is measured by semantic consistency and engineering leverage, not shared-code percentage.
38. The customer storefront architecture remains separately governed by MS-PROT-036.
39. A client outage or incompatibility does not redefine merchant business state.
40. Material expansion of client-runtime abstraction returns to DESIGN-RULES.

---

# 86. Corpus Impact

Repository formalisation SHALL update the affected authority graph rather than silently replace existing documents.

Required navigation/conformance consequences are:

```text
MS-PROT-037 composition
    existing authority + MS-PROT-093 within merchant delivery-surface portfolio

MS-PROT-035 composition
    existing authority + MS-PROT-093 within installed first-party compatibility,
    representation-versioning and client-SDK scope

MS-PROT-063 composition
    existing authority + MS-PROT-093 within installed-native session transport

AUTHORITY-INDEX
    navigation update

DEFERRED-DECISION-REGISTER
    native-app/native-session decision resolution
    client SDK generation resolution where applicable

IMPLEMENTATION-RULES review
    generated-artifact provenance
    client conformance gates
```

ADR-014 remains the browser profile and is not silently superseded.

MS-PROT-036 remains the customer/storefront authority and is not amended by this package.

---

# 87. Watch-List Relationship

The architecture remains subject to semantic-gravity surveillance.

Promotion back to governed design is required if evidence shows:

```text
client contract layer acquiring business policy
presentation generator acquiring semantic ownership
shared client context becoming a world-state container
universal UI runtime emerging through incremental extension
compatibility layer becoming business entitlement
platform clients developing independent business states
```

The smallest repair MUST be falsified first.

---

# 88. Final Architecture

Canonical:

```text
                           MAIN STREET
                              CORE
                               │
                  capability-owned semantics
                               │
                    application contracts
                               │
                projection / Exposure contracts
                               │
                    registered API contracts
                               │
           client representation compatibility
                               │
           language-neutral contract bundle
                               │
        ┌──────────────────────┼───────────────────────┐
        │                      │                       │
 generated SDKs         design language        conformance
        │                      │                       │
        └──────────────────────┼───────────────────────┘
                               │
                  FIRST-PARTY MERCHANT CLIENTS
                               │
    ┌────────────┬─────────────┼────────────┬────────────┐
    ↓            ↓             ↓            ↓            ↓
merchant-web   Android        iOS        Windows       macOS
                  │             │            │            │
                  └─────────────┼────────────┴────────────┘
                                │
                      native implementation
                                │
         ┌──────────────────────┼──────────────────────┐
         ↓                      ↓                      ↓
 platform-local           bounded compile-       purpose-built
    reuse                  time generation          native UX
         │                      │                      │
         └──────────────────────┼──────────────────────┘
                                ↓
                       excellent role-native UX
                                ↓
                           user intent
                                ↓
                  authoritative Main Street API
                                ↓
                   capability-owned revalidation
```

---

# 89. Canonical Decision

> **Main Street shall share business meaning, client contracts, design intent and conformance globally while allowing each first-party client to express those obligations natively. Installed native clients shall be independently compatible with the authoritative backend rather than relying on coordinated deployment. Repetitive client work may be automated through deterministic SDK generation, platform-local reuse, AI assistance and bounded compile-time declarative generation, but no such mechanism becomes business authority or a universal runtime.**

> **The architecture optimises semantic consistency, native design quality and engineering leverage—not shared-code percentage.**

> **Main Street absorbs the complexity of multiple excellent clients so merchants and ordinary staff do not have to absorb the complexity of Main Street.**

---

# 90. Approval and Acceptance Record

```text
FUNDAMENTAL VISION:
VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

BUSINESS-TYPE NEUTRALITY:
PASS

CAPABILITY OWNERSHIP:
PASS

PROJECTION / PRESENTATION SEPARATION:
PASS

MOBILE-FIRST:
PASS

LOW-SOFTWARE-CAPACITY MERCHANT:
PASS

ORDINARY STAFF:
PASS

SEMANTIC-GRAVITY FALSIFICATION:
PASS WITH WATCH CONDITIONS

INSTALLED-CLIENT COMPATIBILITY:
RESOLVED

GENERATED-ARTIFACT OWNERSHIP:
RESOLVED

CONFORMANCE-AUTHORITY PROVENANCE:
RESOLVED

NATIVE-SESSION ARCHITECTURE:
RESOLVED AT REQUIRED ARCHITECTURAL LEVEL

STORE/FRAMEWORK/TOOL IMPLEMENTATION:
SAFELY DEFERRED

DESIGN REVIEW RECOMMENDATION:
ACCEPT

MANUAL APPROVAL:
GRANTED 13 SEPTEMBER 2026

STATUS:
ACCEPTED
```
