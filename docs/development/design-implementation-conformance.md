# Main Street Design–Implementation Conformance Map

> **Snapshot date:** 25 August 2026  
> **Status:** HISTORICAL SNAPSHOT — retired as current implementation navigation  
> **Authority:** none independently — accepted authority remains `designs/AUTHORITY-INDEX.md` and the referenced MS-PROT/TAS documents.  
> **Snapshot verification baseline:** `6cf6658d4e2ae8adc05337c9a9792184ab514238` / GitHub Actions run `32883856417` — BUILD SUCCESS
>
> Current programme navigation is `docs/development/implementation-status.md`; current IMP-06 evidence is `docs/development/imp-06-conformance-baseline-2026-08-30.md`. This snapshot must not be used to determine READY nodes or current macro completion.

## 1. Purpose

This map prevents two failure modes:

```text
accepted design ≠ implemented merely because code exists

not-yet-implemented design ≠ permission to invent a different implementation
```

At its snapshot date, it recorded where `development` provided executable evidence and where accepted scope remained to be implemented. Git history preserves this authority-level baseline; later programme eligibility is governed by the current navigation records named above.

## 2. Status vocabulary

| Status | Meaning |
|---|---|
| **VERIFIED FOUNDATION** | Foundational accepted invariants are exercised by executable evidence. It does not mean every optional/future clause is implemented. |
| **PARTIAL** | Material accepted behaviour exists, but meaningful accepted implementation scope remains. |
| **NOT YET IMPLEMENTED** | Accepted authority exists but there is no material production implementation of the listed surface yet. |
| **ON-DEMAND** | Accepted authority requires machinery only when a concrete transition/case demonstrates the need; absence of speculative universal machinery is conformant. |

## 3. Historical measured verification

```text
commit                              6cf6658d4e2ae8adc05337c9a9792184ab514238
GitHub Actions run                  32883856417
command                             mvn --batch-mode clean verify -Ppostgres-it
production Java sources             415
test Java sources                   146
unit tests                          431 PASS
PostgreSQL integration tests        143 PASS
total tests                         574 PASS
failures / errors / skipped         0 / 0 / 0
Flyway migrations                   25
PostgreSQL                          18.6
result                              BUILD SUCCESS
```

## 4. Historical accepted-design conformance map

| Authority | Status | Current implementation evidence / remaining frontier |
|---|---|---|
| **MS-PROT-020 — Operational Object identity** | **VERIFIED FOUNDATION** | Capability-scoped Operational Object identity, owner-qualified executable lookup, schema affinity and cross-capability relationship identity are implemented. Appointment conforms to the accepted v1.5 canonical `appointment / appointment` identity; executable evidence proves the legacy `booking / appointment` identity is absent. |
| **MS-PROT-021 — Merchant Configuration** | **VERIFIED FOUNDATION** | Declarative capability/policy configuration and exact subordinate `FulfilmentBindingSetRevisionReference` are bounded inputs. Merchant Configuration cannot define schemas, object namespaces, workflows or executable semantics. |
| **MS-PROT-022 — Configuration Resolution / RCP** | **VERIFIED FOUNDATION** | Immutable RCP, provenance, resolved capability configuration, static surfaces and exact binding-set-derived Fulfilment Plan path exist. Contextual actor authority, Provider Readiness, residual obligations and interaction availability remain outside static compilation; only the explicit static surface dependency reference may enter the static catalogue. |
| **MS-PROT-023 — Runtime Operation Execution** | **VERIFIED FOUNDATION** | Executable operations/effects, runtime dispatch, requirements and scoped authority foundations are exercised. Surface interaction availability never grants execution authority. |
| **MS-PROT-024 — Business Process / long-running coordination** | **PARTIAL** | Cross-capability process/orchestration primitives exist; concrete long-running processes remain capability/use-case driven rather than a universal saga engine. |
| **MS-PROT-025 — Persistence / transaction consistency** | **PARTIAL** | PostgreSQL/jOOQ transaction and consistency evidence exists across substantial contexts; the Booking/Appointment local atomic path remains green after introducing the Appointment-owned mutation port. Not every accepted future capability has persistence yet. |
| **MS-PROT-026 — Domain Events** | **PARTIAL** | Domain-event and durable post-commit foundations exist; complete event propagation for all future capabilities is not implemented. |
| **MS-PROT-027 — Projection / Exposure** | **PARTIAL** | Projection/exposure distinctions are represented in architecture; complete read-model/exposure delivery is not yet built. MS-PROT-049 contextual surface resolution consumes current authority/readiness evidence without making surfaces or interaction availability business truth. |
| **MS-PROT-028 — Trust / verification** | **PARTIAL** | Trust-boundary foundations exist; complete merchant/public verification delivery remains future work. |
| **MS-PROT-029 — Integration boundary** | **PARTIAL** | Provider-neutral adapter/security/correlation architecture is reflected in implemented subsystems; concrete adapters remain incomplete. |
| **MS-PROT-030 — Backend & Web technology** | **PARTIAL** | Java/Spring/PostgreSQL backend baseline exists; accepted merchant/storefront web applications are not yet built. |
| **MS-PROT-031 — Merchant Scope** | **VERIFIED FOUNDATION** | MerchantScope establishment and scoped runtime/persistence paths have executable evidence. Contextual merchant surface resolution also fails closed when request scope does not match the executable merchant model. |
| **MS-PROT-032 — Backend bounded contexts/modules** | **PARTIAL** | Modular-monolith package/context boundaries are substantial; physical bounded-context placement is not treated as capability ownership. Complete delivery modules remain future work. |
| **MS-PROT-033 — Relational aggregate/repository architecture** | **PARTIAL** | jOOQ/PostgreSQL repository patterns and aggregate persistence exist across many contexts; not every future aggregate is implemented. |
| **MS-PROT-034 — Database technology** | **VERIFIED FOUNDATION** | PostgreSQL 18.6, Flyway and jOOQ are exercised in the full integration gate. |
| **MS-PROT-035 — API contract/transport** | **NOT YET IMPLEMENTED** | No complete accepted HTTP/API delivery surface yet. |
| **MS-PROT-036 — Storefront composition** | **NOT YET IMPLEMENTED** | Semantic/surface foundations precede storefront implementation. Public/customer contextual surface resolution remains downstream before storefront delivery. |
| **MS-PROT-037 — Merchant dashboard** | **NOT YET IMPLEMENTED** | Static composition plus contextual MERCHANT eligibility and provider-readiness interaction-availability seams now exist; dashboard routes/components/navigation remain intentionally deferred. |
| **MS-PROT-038 — Onboarding/inference** | **PARTIAL** | Configuration/inference boundaries are established; complete adaptive onboarding is not built. |
| **MS-PROT-039 — Scheduling/calendar onboarding** | **PARTIAL** | Provider-neutral scheduling semantics exist; complete onboarding/integration delivery remains future. |
| **MS-PROT-040 — Configuration lifecycle** | **VERIFIED FOUNDATION** | Immutable revisions/releases, validation/publication/approval/activation, exact active-release affinity, concurrency/idempotency and RCP affinity are exercised. Residual surface recovery and Provider Readiness are contextual and do not mutate or broaden the active RCP. |
| **MS-PROT-041 — Calendar** | **PARTIAL** | Main Street scheduling intent/appointment timing foundations exist; complete calendar projection/provider integration remains future. |
| **MS-PROT-042 — Booking & Appointment** | **PARTIAL** | Booking/Appointment vertical slice and concurrency/persistence evidence exist. Appointment uses `appointment / appointment`, and Booking coordinates Appointment mutation only through Appointment-owned contracts while preserving the shared local transaction. Broader feature coverage remains use-case driven. |
| **MS-PROT-043 — CustomerContext / Enquiry** | **PARTIAL** | CustomerContext authority exists; full Enquiry/Conversation/customer-access delivery and customer-context surface filtering remain incomplete. |
| **MS-PROT-044 — Offering/Product/Listing** | **PARTIAL** | Semantic boundaries and Inventory composition foundations exist; complete offering/listing/product delivery is not built. |
| **MS-PROT-045 — Capability-owned schema/mutation** | **VERIFIED FOUNDATION** | Owner-scoped schemas/fields and field-targeted mutation conformance are exercised. |
| **MS-PROT-046 — Publication/Opportunity/Announcement** | **NOT YET IMPLEMENTED** | Accepted semantics exist; production subsystem remains future. |
| **MS-PROT-047 — Capability Configuration Contract** | **VERIFIED FOUNDATION** | Generic decision identity/status/value-domain view exists over the proven Policy/ENUM subset. Other accepted bounded domains remain on-demand when concrete capabilities require them. |
| **MS-PROT-048 — Provider Fulfilment** | **VERIFIED FOUNDATION** | v1.2 exact immutable `FulfilmentBindingSetRevision` affinity and v1.3 requirement-driven applicability are executable. Exact obligation subsets, binding affinity and provider compatibility are proven. MS-PROT-049 v1.1 can consume a bounded role-readiness projection for surface interaction availability, but live ProviderConnection/readiness authority, concrete adapters/reconciliation, independent platform-originated requirements and additional bounded applicability domains remain downstream. |
| **MS-PROT-049 — Surface Contributions** | **PARTIAL** | Registry-driven static catalogue, static eligibility evidence, operation validation and deterministic audience+composition-target aggregation are verified. Contextual MERCHANT resolution supports bounded `ACTIVE_ONLY` / `ACTIVE_OR_RESIDUAL` eligibility, optional current actor privilege and residual-management recovery. Accepted v1.1 now also provides a presentation-neutral `AVAILABLE` / `DEGRADED` / `UNAVAILABLE` state for otherwise included MERCHANT ACTION contributions with an explicit static `FulfilmentRoleIdentity` dependency. Missing readiness fails closed to `UNAVAILABLE`; provider degradation does not remove the workspace or affect unrelated independent actions; readiness cannot grant actor/backend authority; no ATTENTION contribution is synthesized. Live readiness remains outside the RCP. CUSTOMER context, PUBLIC Exposure, projection availability, concrete residual/readiness adapters, cross-registry assembly validation and final presentation remain. |
| **MS-PROT-050 — Business Hours** | **VERIFIED FOUNDATION** | Explicit MERCHANT / MERCHANT_LOCATION scope and cross-midnight behaviour are exercised. Merchant Profile/location delivery and broader current-status presentation remain future. |
| **MS-PROT-051 — Merchant Profile** | **NOT YET IMPLEMENTED** | Accepted profile/location semantics exist; production profile surface is not built. |
| **MS-PROT-052 — Onboarding prompts/questions** | **NOT YET IMPLEMENTED** | Accepted question/option semantics exist; adaptive prompt graph is not implemented. |
| **MS-PROT-053 — Data protection lifecycle** | **PARTIAL** | Purpose-bound personal-data-use foundations/persistence exist; full retention/erasure/anonymisation/redaction administration remains. |
| **MS-PROT-054 — Semantic compatibility/migration** | **VERIFIED FOUNDATION / ON-DEMAND** | Durable compatibility evidence and fail-closed production activation are verified. Universal migration machinery is intentionally not invented; registered migration is implemented when a concrete semantic transition requires it. |
| **MS-PROT-055 — Money / payment evidence** | **PARTIAL** | MonetaryAmount and durable payment-authority foundations exist; complete provider-backed payment workflows remain future. |
| **MS-PROT-056 — Plans/trial/entitlement** | **VERIFIED FOUNDATION** | Trial, Standing Free, Commercial Agreement, entitlement definitions/grants and access decisions have unit/PostgreSQL evidence. |
| **MS-PROT-057 — AI Concierge** | **NOT YET IMPLEMENTED** | Accepted AI authority boundary exists; production Concierge/specialists are not built. |
| **MS-PROT-058 — Inventory** | **PARTIAL** | Quantity authority/allocation foundations are exercised; complete catalogue/commerce inventory delivery remains future. |
| **MS-PROT-059 — Unified interaction/idempotency** | **PARTIAL** | Idempotency/retry/channel-independent authority foundations exist; not every interaction channel has a production adapter. |
| **MS-PROT-060 — Physical Order Fulfilment** | **NOT YET IMPLEMENTED** | Accepted authority exists; production shipment/order-fulfilment subsystem remains future. |
| **MS-PROT-061 — Returns/courier labels** | **NOT YET IMPLEMENTED** | Accepted boundary exists; production return/label paths remain future. |
| **MS-PROT-062 — Runtime eligibility/access composition** | **PARTIAL** | Entitlement, actor authority, protection and operational decision dimensions exist. Surface actor filtering reuses current authorization authority, and surface interaction availability consumes only contextual operability evidence; neither becomes backend authority. Complete contextual provider/trust composition remains future. |
| **MS-PROT-063 — Authentication/session/trusted principal** | **PARTIAL** | Trusted execution-principal and staff operational-device contexts exist; complete external authentication/session establishment is not built. Surface resolution keeps privileges out of the principal and queries current authority. |
| **MS-PROT-064 — Audit** | **VERIFIED FOUNDATION** | Append-only audit evidence model and PostgreSQL persistence are exercised. |
| **MS-PROT-065 — Durable background work** | **VERIFIED FOUNDATION** | Durable work instructions/store and execution boundary are exercised. |
| **MS-PROT-066 — Media** | **VERIFIED FOUNDATION** | Canonical media/rendition semantic and persistence foundations are exercised; production delivery/transcoding products remain downstream. |
| **MS-PROT-067 — Credential/external connection security** | **VERIFIED FOUNDATION** | Credential-generation/security metadata persistence and separation from semantic/provider-binding authority are exercised. |
| **MS-PROT-068 — Observability** | **VERIFIED FOUNDATION** | Operational observability model preserves telemetry/business-authority distinctions. Surface readiness consumption does not make health evidence semantic truth; concrete readiness projection remains an owning-adapter responsibility. |
| **MS-PROT-069 — Execution uncertainty** | **VERIFIED FOUNDATION** | Transport failure/execution-uncertainty distinctions and retry safety model are exercised. |
| **MS-PROT-070 — Resilience/degradation** | **VERIFIED FOUNDATION** | Controlled degradation, retry/fallback boundary and resilience invariants have executable evidence. The initial surface consequence is now wired through MS-PROT-049 v1.1: an explicitly provider-dependent ACTION may be `DEGRADED` or `UNAVAILABLE` without removing the semantic workspace or activating fallback/rebinding. Concrete readiness/resilience adapters remain downstream. |
| **MS-PROT-071 — Merchant Account establishment** | **VERIFIED FOUNDATION** | Bootstrap, controller establishment and temporal evidence have unit/PostgreSQL coverage. |
| **MS-PROT-072 — Cross-capability orchestration** | **VERIFIED FOUNDATION** | Application outcome/orchestration primitives and post-commit patterns are implemented. The Booking/Appointment path proves an application coordinator may preserve one local atomic boundary while entering participant mutation through an owning-capability contract. |
| **MS-PROT-073 — Resource protection** | **VERIFIED FOUNDATION** | Protection targets/policies/admission/state have domain and PostgreSQL evidence. |
| **MS-PROT-074 — Workforce** | **VERIFIED FOUNDATION** | Membership, groups, roles and Merchant Operational Device Authorisation have unit/PostgreSQL evidence. Current workforce/actor authorization authority can be consumed by presentation filtering without becoming frontend authorization. |
| **MS-PROT-075 — Notifications** | **VERIFIED FOUNDATION** | Intent/dispatch/delivery-attempt/evidence foundations and PostgreSQL delivery coordination are exercised. Concrete provider adapters remain downstream fulfilment. |
| **MS-PROT-076 — Merchant Account lifecycle** | **VERIFIED FOUNDATION** | OPEN/CLOSING/CLOSED, suspension/controller-transfer foundations and durable persistence are exercised. |
| **MS-TAS-RECOVERY-001 — Backup/restore/DR** | **NOT YET IMPLEMENTED** | Accepted production architecture exists; concrete cloud/IaC/runbook implementation remains future. |

## 5. Questions resolved at the snapshot frontier

### Merchant fulfilment-binding authority and applicability — RESOLVED

The implementation-discovered question formerly tracked as GitHub issue #19 is resolved by accepted MS-PROT-048 v1.2:

```text
MerchantConfigurationRevision
        │ exact immutable reference
        ▼
FulfilmentBindingSetRevision
        │ no independent ACTIVE authority
        ▼
Resolved Fulfilment Plan
```

MS-PROT-048 v1.3 resolves the later bounded applicability frontiers through registered Fulfilment Requirements and configuration-specific applicable obligations. The measured baseline verifies revision/merchant/release affinity, private-vs-platform-service role ownership, bounded `ALWAYS`/ENUM applicability, exact obligation union, missing/stale binding rejection and exact-subset provider compatibility.

### Appointment owner capability identity — RESOLVED AND ALIGNED

Accepted MS-PROT-020 v1.5 gives:

```text
appointment / appointment
```

Production conforms to it. Booking enters Appointment mutation through `AppointmentConfirmationAuthority` / `AppointmentMutation` rather than declaring Appointment persistence authority. The shared local transaction remains atomic and the PostgreSQL booking integration suite remains green. GitHub issue #18 is closed as completed.

### Contextual merchant surface eligibility and interaction availability — VERIFIED FOUNDATION

The first contextual slice remains deliberately bounded:

```text
active static contribution
    + current actor privilege
        → contextual inclusion

inactive capability
    + explicit ACTIVE_OR_RESIDUAL contract
    + outstanding residual obligation
    + current actor privilege where required
        → residual management inclusion
```

MS-PROT-049 v1.1 adds the distinct interaction-availability step for an otherwise included MERCHANT ACTION:

```text
included ACTION
    + explicit static FulfilmentRoleIdentity dependency
    + current role-readiness projection
        → AVAILABLE / DEGRADED / UNAVAILABLE
```

The same pinned `SurfaceContributionRegistrySnapshot` is used to recover a residual-eligible definition after capability deactivation because the active static catalogue correctly excludes inactive capabilities and live residual state must remain outside the immutable RCP. The static catalogue may carry the explicit role dependency reference, but current Provider Readiness remains contextual.

Executable evidence proves that provider outage/degradation does not remove the workspace, does not affect an unrelated action, does not bypass actor filtering, does not create ATTENTION semantics and does not grant backend operation authority. Missing readiness evidence for a dependent ACTION fails closed to `UNAVAILABLE`.

This does not create a universal eligibility/availability DSL and does not make the surface resolver an authority over commitments, ProviderConnection state, resilience policy or command execution.

## 6. Historical next frontiers — superseded

The next implementation sequence should remain authority-driven:

1. extend MS-PROT-049 contextual resolution one bounded dimension at a time: PUBLIC Exposure and CUSTOMER context, then projection availability where existing authorities make the rules mechanically specifiable;
2. wire concrete `ResidualSurfaceObligationAuthority` adapters from capability-owned commitment authorities rather than storing residual counts in the surface layer;
3. wire `FulfilmentRoleInteractionAvailabilityAuthority` to the owning ProviderConnection/readiness/resilience projection without moving health state or provider logic into the surface package;
4. add cross-registry assembly validation for static surface `FulfilmentRoleIdentity` dependencies when the composition/package assembly boundary has both registries available;
5. keep HTTP/storefront/dashboard/onboarding delivery behind the generic composition/resolution seam and keep live ProviderConnection readiness/health outside static configuration and the RCP.

No frontend/API implementation should bypass these authority boundaries for convenience.

## 7. Current routing

This file is deliberately frozen as a dated conformance snapshot. It is not amended with later source counts or used to infer current completion.

Current implementation state:

```text
IMP-00..IMP-05 CONFORMING_COMPLETE
IMP-06 PARTIALLY_CONFORMING
E3 BLOCKED_DESIGN — manual approval required
IMP-07..IMP-20 BLOCKED_DEPENDENCY
```

Use `implementation-status.md` for the current measured baseline and macro graph, and `imp-06-conformance-baseline-2026-08-30.md` for the active fine-grained frontier.
