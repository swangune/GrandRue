# GrandRue Canonical Authority Index

**Version:** 4.38
**Status:** ACCEPTED governance navigation authority
**Governed by:** `DOCUMENT-GOVERNANCE.md`
**Purpose:** Identify which accepted GrandRue fundamental product-purpose, semantic/design, implementation-architecture and implementation-programme/governance authorities currently govern, including scope-aware amendment composition and key supersession/closure links.

---

## 1. Navigation Rule

Authority is determined by:

```text
stable document identifier
+
accepted lifecycle status
+
accepted amendment/supersession graph
+
amendment scope
```

The index identifies **which** authority governs. The accepted authority owns **what** the substantive meaning is. Long semantic summaries are intentionally not duplicated here.

**Product identity resolution:** **GrandRue** is the current product and repository identity. Within accepted authorities not yet name-migrated, legacy product-name references to `Main Street` refer to GrandRue unless the context is explicitly historical. Stable `MS-*` authority identifiers, protected `mainstreet*` external/persisted compatibility identities and historical provenance remain unchanged; current implementation/package identifiers use `grandrue.*`.

Accepted product/design/architecture/implementation authorities remain subordinate to `MS-FUNDAMENTAL-VISION-001` within its product-purpose scope. Accepted TAS/ADR and implementation-programme/governance authority remain subordinate to accepted MS-PROT semantic/design authority within overlapping semantic scope. Review/falsification evidence under `docs/development/` is not substantive authority unless an accepted authority explicitly incorporates it.

### 1.1 Fundamental Product-Purpose Authority

| Authority | Current accepted composition / location |
|---|---|
| MS-FUNDAMENTAL-VISION-001 | accepted v1.0 — `docs/foundation/Fundamental-Vision-Mission-and-Product-Constitution.md` |

`MS-FUNDAMENTAL-VISION-001` governs why GrandRue exists (under the historical Main Street name in that authority) and the non-negotiable product-purpose constraints against which downstream product, design, architecture, UX and implementation decisions are evaluated. It does not own capability-specific business semantics.

`designs/DESIGN-RULES.md` v2.5 operationalises that authority through the mandatory Fundamental Vision Conformance Gate and governs design review, including comparative justification against credible alternatives under §2.3.

The older `docs/foundation/Vision.md` remains historical foundation/product evidence and MUST NOT override `MS-FUNDAMENTAL-VISION-001` within overlapping product-purpose scope.

---

## 2. Design-Series Strata

```text
MS-PROT-001..019
    research / prototype / proposed evidence unless explicitly incorporated

MS-PROT-020..096
    accepted design series where indexed below, subject to scope-aware version composition
    MS-PROT-078 is non-current historical readiness evidence and is not required current authority
```

The absence of MS-PROT-017 is intentional historical sequencing. The numeric upper bound is informational and may be extended by later accepted authority.

---

## 3. Current Accepted Semantic / Design Authority Map

| Authority | Current accepted composition |
|---|---|
| MS-PROT-020 | base/revisions through v1.5 |
| MS-PROT-021 | accepted document + incorporated revisions |
| MS-PROT-022 | through v1.5 |
| MS-PROT-023 | accepted document + accepted amendments |
| MS-PROT-024 | v1.0 |
| MS-PROT-025 | accepted document + accepted amendments |
| MS-PROT-026 | v1.0 + v1.1 |
| MS-PROT-027 | v1.1 + v1.2 + v1.3 + v1.4 + v1.5 + v1.6 + v1.7 + v1.8 + v1.9 + v1.10 + v1.11 + v1.12 + v1.13 + v1.14 |
| MS-PROT-028 | through v1.3 |
| MS-PROT-029 | accepted document + accepted amendments |
| MS-PROT-030 | accepted document |
| MS-PROT-031 | accepted document + accepted amendments |
| MS-PROT-032 | accepted document + accepted amendments |
| MS-PROT-033 | accepted document + accepted amendments |
| MS-PROT-034 | accepted document + accepted amendments |
| MS-PROT-035 | v1.0 + v1.1 + MS-PROT-093 within installed first-party client compatibility, representation-versioning and client-SDK scope |
| MS-PROT-036 | v1.2 + v1.3 within hostname-independent website-delivery commercial access + v1.4 within composition-publication lifecycle, access and business-evolution handoff + MS-PROT-094 within storefront presentation/composition, business-data non-ownership and centrally evolvable delivery scope |
| MS-PROT-037 | accepted document + MS-PROT-093 within merchant delivery-surface portfolio scope + MS-PROT-094 within merchant presentation consistency scope |
| MS-PROT-038 | accepted document |
| MS-PROT-039 | through v1.2 |
| MS-PROT-040 | v1.0 + v1.1 + v1.2 + v1.3 + v1.4 + v1.5 + v1.6 + v1.7 + v1.8 |
| MS-PROT-041 | through v1.1 |
| MS-PROT-042 | v1.2 + v1.3 + v1.4 + v1.5 + v1.6 + v1.7 + v1.8 + v1.9 + v1.10 + v1.11 + v1.12 + v1.13 + v1.14 + v1.15 + v1.16 |
| MS-PROT-043 | v1.2 + v1.3 + v1.4 + v1.5 + v1.6 |
| MS-PROT-044 | base + v1.1 + v1.2 within Offering/Product/ProductVariant owner-qualified merchant-definition access scope |
| MS-PROT-045 | through v1.1 |
| MS-PROT-046 | v1.1 + v1.2 + v1.3 + v1.4 + v1.5 |
| MS-PROT-047 | v1.0 |
| MS-PROT-048 | v1.0 + v1.1 + v1.2 + v1.3 + v1.4 + v1.5 + v1.6 |
| MS-PROT-049 | v1.0 + v1.1 + v1.2 + v1.3 + v1.4 + MS-PROT-094 within storefront presentation-profile and website-presentation recommendation scope |
| MS-PROT-050 | v1.2 + v1.3 + v1.4 + v1.5 + v1.6 |
| MS-PROT-051 | v1.0 + v1.1 + v1.2 + v1.3 + v1.4 + v1.5 + v1.6 |
| MS-PROT-052 | v1.0 + v1.1 + v1.2 |
| MS-PROT-053 | v1.0 + v1.1 + v1.2 + v1.3 |
| MS-PROT-054 | v1.0 |
| MS-PROT-055 | v1.0 + v1.1 + v1.2 |
| MS-PROT-056 | v1.0 through v1.9, scope-aware |
| MS-PROT-057 | v1.0 + v1.1 + v1.2 + MS-PROT-094 within website presentation-assistance scope |
| MS-PROT-058 | v1.0 + v1.1 + v1.2 + v1.3 |
| MS-PROT-059 | v1.0 |
| MS-PROT-060 | v1.0 + v1.1 + v1.2 |
| MS-PROT-061 | v1.0 + v1.1 + v1.2 |
| MS-PROT-062 | v1.0 + v1.1 |
| MS-PROT-063 | v1.0 + v1.1 + v1.2 + MS-PROT-093 within installed-native session-transport scope |
| MS-PROT-064 | v1.0 |
| MS-PROT-065 | v1.0 + v1.1 |
| MS-PROT-066 | v1.0 + incorporated v1.1 + v1.2 |
| MS-PROT-067 | v1.0 |
| MS-PROT-068 | v1.0 + v1.1 |
| MS-PROT-069 | v1.0 + v1.1 |
| MS-PROT-070 | v1.0 |
| MS-PROT-071 | v1.0 + v1.1 + v1.2 |
| MS-PROT-072 | v1.0 |
| MS-PROT-073 | v1.0 |
| MS-PROT-074 | v1.0 + v1.1 + v1.2 |
| MS-PROT-075 | v1.0 + v1.1 + v1.2 |
| MS-PROT-076 | v1.0 |
| MS-PROT-077 | v1.0 + v1.1 + v1.2 |
| MS-PROT-079 | v1.0 + v1.1 |
| MS-PROT-080 | v1.1 + v1.2 + v1.3 + v1.4 |
| MS-PROT-081 | v1.0 + v1.1 + v1.2 + v1.3 + v1.4 |
| MS-PROT-082 | v1.0 + v1.1 |
| MS-PROT-083 | v1.0 + v1.1 + v1.2 + v1.3 + v1.4 + v1.5 + v1.6 + v1.7 + v1.8 |
| MS-PROT-084 | v1.1 complete composition + v1.2 Financial Operations commercial-access classification; earlier standalone targeted amendment retained as historical evidence |
| MS-PROT-085 | v1.0 + v1.1 + v1.2 + v1.3 + v1.4 |
| MS-PROT-086 | v1.0 + v1.1 + v1.2 + v1.3 + v1.4 |
| MS-PROT-087 | v1.0 + v1.1 + v1.2 + v1.3 + v1.4 |
| MS-PROT-088 | v1.0 + v1.1 within website namespace-use commercial access + v1.2 within website-binding selection, disconnection and supporting commercial requirements + v1.3 within website-qualified platform namespace allocation, naming and retirement + v1.4 within Website Connection Request and bounded deferred local completion + v1.5 within website DNS/certificate execution, maintenance and recovery |
| MS-PROT-089 | v1.0 |
| MS-PROT-090 | v1.0 |
| MS-PROT-091 | v1.0 + v1.1 |
| MS-PROT-092 | v1.0 + v1.1 |
| MS-PROT-093 | v1.0 |
| MS-PROT-094 | v1.0 |
| MS-PROT-095 | v1.0 |
| MS-PROT-096 | v1.0 |

### 3.1 Key scope-aware links

The following cross-authority links are retained explicitly because they materially affect current navigation:

- MS-PROT-096 v1.0 — `MS-PROT-096 — Invoice, Issuance & Payment Relationship Model.md` — establishes native outbound customer Invoicing, resolves `MS-PROT-084-DQ-006`, keeps Payment as owner of Payment Obligation/Amount Due/payment truth, prevents duplicate Financial Operations Receivables, establishes exact CustomerContext linkage where a durable customer relationship exists, admits `invoicing/invoice-issuance-access@1` / `ISSUE_CUSTOMER_INVOICE` for BUSINESS + GROWTH, classifies bounded preparation/observation/resolution without independent Invoicing entitlement, advances `MS-PROT-056-V17-DQ-004`, narrows `MS-PROT-056-V17-DQ-001`, and activates no implementation.

- MS-PROT-095 v1.0 — `MS-PROT-095 — Quotation, Revision & Acceptance Model.md` — establishes Quotation as the owner of merchant-issued quoted commercial offers, immutable issued revisions and recipient responses while preserving Enquiry ownership of customer requests. It admits the existing BUSINESS Quotation reservation through `quotation/commercial-offer-issuance-access@1` / `ISSUE_QUOTATION_COMMERCIAL_OFFER` for BUSINESS + GROWTH, classifies bounded preparation/observation/response/resolution without independent Commercial Entitlement, partially resolves `MS-PROT-056-V17-DQ-004`, narrows `MS-PROT-056-V17-DQ-001`, preserves `MS-PROT-084-DQ-006` Invoice authority as deferred, and activates no implementation.

- MS-PROT-044 v1.2 — `MS-PROT-044 v1.2 — Merchant Offering & Product Definition Access Contract Amendment.md` — supplies exact owner-qualified merchant definition observation and authoring access contracts for Offering and Product/ProductVariant source truth. It preserves Product optionality, keeps Inventory and downstream transaction authority independent, explicitly excludes Listing and public-source access, makes no commercial classification, leaves `MS-PROT-056-V17-DQ-001` OPEN and activates no implementation.

- MS-PROT-083 v1.7 — `MS-PROT-083 v1.7 — Business Intelligence Commercial Access Classification Amendment.md` — classifies `business-intelligence/business-analytics-evaluation-access@1` / `USE_BUSINESS_ANALYTICS` for BUSINESS + GROWTH, `business-intelligence/campaign-analytics-evaluation-access@1` / `USE_CAMPAIGN_ANALYTICS` for GROWTH, and `business-intelligence/merchant-analytics-presentation-access@1` with no independent Commercial Entitlement. It preserves source-capability ownership, analytical applicability/coverage, retained-history and downgrade boundaries, keeps Campaign execution and source mutation independently governed, and leaves report/export commercial classification plus the final concrete DQ-001 catalogue manifest open.

- MS-PROT-083 v1.8 — `MS-PROT-083 v1.8 — Analytical Reports & Exports Commercial Access Classification Amendment.md` — classifies the accepted v1.6 `merchant-analytical-report@1` and `measure-observation-export@1` delivery paths through exact `merchant-analytical-report-access@1` / `DELIVER_MERCHANT_ANALYTICAL_REPORT` and `measure-observation-export-access@1` / `EXPORT_MEASURE_OBSERVATIONS` access contracts, both with no independent Commercial Entitlement. It preserves v1.7 analytical-evaluation protection, current Actor Authorisation, Exposure, data-use, Audit and Resource Protection at externalisation, and completes the current Business Intelligence owner/supporting classification contribution without closing `MS-PROT-056-V17-DQ-001`.

- MS-PROT-092 v1.1 — `MS-PROT-092 v1.1 — Document Evidence Coordination Commercial Access Classification Amendment.md` — classifies five exact Document Evidence Coordination supporting access contracts for purpose-bound intake establishment, Extraction Candidate processing, evidence validation, owner-qualified evidence handoff and bounded coordination observation. All five are `PLATFORM_SERVICE_ACCESS` contracts with `NO INDEPENDENT COMMERCIAL ENTITLEMENT`. It preserves consuming-owner commercial permission and business-truth ownership, Media ownership, Actor Authorisation, Data Protection, Exposure, security admission, Provider Readiness, Resource Protection and AI non-authority; contributes zero new standard entitlement identities; and partially resolves `MS-PROT-056-V17-DQ-001` without closing it or activating implementation.

- MS-PROT-084 v1.2 — `MS-PROT-084 v1.2 — Financial Operations & Evidence Commercial Access Classification Amendment.md` — classifies `financial-operations/record-establishment-access@1` / `ESTABLISH_FINANCIAL_OPERATIONS_RECORD` for BUSINESS + GROWTH while explicitly classifying bounded new-record preparation, existing-record observation and existing-record resolution without an independent Commercial Entitlement. It preserves Financial Operations residual ownership, source-capability ownership, MS-PROT-092 evidence-preparation boundaries, MS-PROT-083 analytical ownership, provider/payment/accounting boundaries, and the downgrade distinction between materially new financial truth and bounded resolution of existing truth. It partially resolves `MS-PROT-056-V17-DQ-001`; final entitlement identities and the complete catalogue remain open.

- MS-PROT-061 v1.2 — `MS-PROT-061 v1.2 — Returns Commercial Access Classification Amendment.md` — supplies the current Returns owner-qualified commercial classifications: applicable-policy observation, existing-Order return-resolution support, return-label preparation and existing return-label observation/reconciliation require no independent Commercial Entitlement. It preserves Merchant Configuration ownership of Returns applicability and the independent Ordering, Payment, Inventory, Fulfilment/Shipment and provider boundaries; it contributes zero new standard entitlement identities and partially resolves `MS-PROT-056-V17-DQ-001`, which remains OPEN pending the complete concrete manifest.

- MS-PROT-051 v1.6 — `MS-PROT-051 v1.6 — Merchant Profile Commercial Access Amendment.md` — supplies exact commercial classifications for existing Profile public source observation, merchant preparation, observation, presence authoring, retirement and privacy-only restriction. §§1–9 allocate `MAINTAIN_MERCHANT_PRESENCE` to FREE with explicit BUSINESS/GROWTH inclusion and exempt the five bounded non-authoring contracts. §§3, 10–12 preserve the initial PUBLIC portfolio exclusions, source ownership, current authority, lifecycle, concurrency, historical recovery and neighbouring service boundaries. Exact entitlement identities and the complete catalogue remain unresolved under `MS-PROT-056-V17-DQ-001`; existing Profile deferred decisions and C3 status remain unchanged.

- MS-PROT-043 v1.6 §§1–12 establishes `enquiry/public-interaction-preparation-access@1` with no independent Commercial Entitlement for bounded public entry and non-committing preparation of general and Opportunity-linked Enquiries. Submission retains its v1.5 origination requirements; source access and delivery remain independently governed. `MS-PROT-056-V17-DQ-001` remains OPEN; no implementation activation is authorised.

- MS-PROT-046 v1.4 §§1–12 classifies the exact `publication/public-source-observation-access@1` and `publication/opportunity-enquiry-participation-access@1` contracts without independent Publication entitlements. Public source observation, participation and Enquiry execution remain distinct; delivery, administration and target-operation grants remain separately governed. The complete catalogue remains open under `MS-PROT-056-V17-DQ-001`; no implementation activation is authorised.
- MS-PROT-046 v1.5 — `MS-PROT-046 v1.5 — Merchant Publication Authoring & Commercial Access Amendment.md` — supplies merchant preparation, observation, authoring and withdrawal commercial classifications for the accepted PublishedContent, Opportunity and Announcement portfolio. §§1–7 bind the exact authoring access contract to `AUTHOR_AND_PUBLISH_INFORMATION` in FREE with explicit paid-tier inclusion, while classifying bounded preparation, observation and withdrawal without independent paid requirements. §§8–12 preserve lifecycle, current/published revision separation, receipt recovery, Exposure, delivery and source ownership. Exact entitlement identities and the complete catalogue remain unresolved under DQ-001; existing Publication deferred decisions and C3 status remain unchanged.

- MS-PROT-085 v1.3 §§1–12 resolves commercial classification only for `enquiry/initial-submission-review@1`: qualified post-commit progression requires no Commercial Entitlement; merchant observation, acknowledgement and recording review require `OBSERVE_ENQUIRY` through MS-PROT-043 v1.5 without an independent Attention entitlement. Controller/source-access requirements and other Attention families remain unchanged. `MS-PROT-056-V17-DQ-001` remains OPEN; no implementation activation is authorised.

- MS-PROT-085 v1.4 — `MS-PROT-085 v1.4 — Customer Communication Human Response Commercial Access Amendment.md` — classifies exactly `customer-communication/human-response-required@1` as a supporting Merchant Attention path with no independent Commercial Entitlement. Merchant observation and response remain gated by the exact independently valid Customer Communication access and actor requirements; the classification does not make Customer Communication FREE, mint a new entitlement identity or permit tier-name runtime checks. It completes the commercial classification of the currently accepted two-family Merchant Attention portfolio while leaving Customer Communication exact bindings and the complete `MS-PROT-056-V17-DQ-001` catalogue manifest OPEN; no implementation activation is authorised.

- MS-PROT-086 v1.4 — `MS-PROT-086 v1.4 — Customer Communication Commercial Access Classification Amendment.md` — classifies exactly two protected Customer Communication purposes: `customer-communication/message-participation-access@1` / `CONDUCT_CUSTOMER_COMMUNICATION` and `customer-communication/automated-response-access@1` / `AUTOMATE_ROUTINE_CUSTOMER_SERVICE`, both allocated to BUSINESS + GROWTH as `PLATFORM_SERVICE_ACCESS`. It explicitly classifies bounded non-committing human preparation, existing-Conversation observation and committed-communication progression with no independent Commercial Entitlement; preserves FREE Enquiry separation, exact participant/actor/access and Projection/Exposure requirements, Notification and Merchant Attention ownership, provider/AI non-authority, historical Message truth and the no-free-continuation boundary; leaves `MS-PROT-086-DQ-004` and the final `MS-PROT-056-V17-DQ-001` catalogue manifest open; and activates no implementation.

- MS-PROT-087 v1.4 — `MS-PROT-087 v1.4 — Merchant Marketing Commercial Access Classification Amendment.md` — classifies exactly `marketing/campaign-service-access@1` / `CONDUCT_MARKETING_CAMPAIGNS` for GROWTH as `PLATFORM_SERVICE_ACCESS`, while bounded non-committing preparation, existing Marketing-state observation and Campaign restriction require no independent Commercial Entitlement. It preserves current commercial revalidation before protected new use and pre-externalisation, retained-history/downgrade boundaries, Publication/Notification/source/actor/provider ownership and the no-generic-committed-progression boundary; it leaves `MS-PROT-087-DQ-006` unchanged, leaves the final `MS-PROT-056-V17-DQ-001` catalogue manifest open, mints no final entitlement identity and activates no implementation.

- MS-PROT-043 v1.5 §§1–12 establishes three exact Enquiry-owned commercial access contracts for general submission, Opportunity-linked submission and merchant observation. It preserves MS-PROT-056 v1.7 FREE placement and v1.9 binding authority; exact entitlement definitions, supporting-contract closure and the complete catalogue remain open under `MS-PROT-056-V17-DQ-001`. No production activation or implementation-node completion is authorised.

- MS-PROT-094 v1.0 establishes the presentation asymmetry between first-party merchant software and merchant-branded customer-facing websites: merchant clients converge around a coherent GrandRue operational identity while remaining environment-optimised under MS-PROT-093; customer-facing websites are merchant-specific compositions over shared centrally evolvable storefront engines, own no authoritative business data, and MUST NOT be selected from templates, themes, reusable styles, domain layouts, presentation profiles or hidden finite site archetypes. It amends composite MS-PROT-036 within storefront composition/data-non-ownership/central evolution scope, composite MS-PROT-037 within merchant presentation-consistency scope, composite MS-PROT-049 within storefront presentation-profile/recommendation scope and composite MS-PROT-057 within website presentation-assistance scope; MS-PROT-093 remains unchanged.
- MS-PROT-093 v1.0 widens MS-PROT-037's former `merchant-web`-only delivery assumption into a first-party merchant-client portfolio of `merchant-web` plus admitted Android, iOS, Windows and macOS native client classes without requiring simultaneous implementation; it composes with MS-PROT-035 by treating independently installed first-party clients as independently deployed compatibility consumers, introducing Client Contract Representation Versioning, `CLIENT_UPDATE_REQUIRED`, a deterministic Client Contract Bundle and generated first-party SDKs; and it composes with MS-PROT-063/ADR-014 by adding a native opaque server-authoritative session-transport profile while preserving Identity, Session, Merchant Scope, Membership, Actor Authorisation and Merchant Operational Device Context authority. It rejects a universal client UI runtime, retains bounded declarative generation only as compile-time optimisation, requires conformance evidence to derive from accepted authority, preserves `merchant-web` as a first-class/fallback merchant surface, and leaves MS-PROT-036 storefront/customer architecture unchanged within MS-PROT-093's own scope.
- MS-PROT-026 v1.1 composes with MS-PROT-065 v1.1 for production event/background progression.
- MS-PROT-027 v1.4 resolves `MS-PROT-051-V11-DQ-005`; MS-PROT-027 v1.5 resolves `MS-PROT-051-V11-DQ-006`; MS-PROT-027 v1.6 resolves `MS-PROT-027-V15-DQ-001` with immutable typed owner-qualified Exposure Element Contracts and an exact-release registry snapshot; MS-PROT-027 v1.7 resolves `MS-PROT-027-V14-DQ-001` with immutable typed owner-qualified Projection Contracts, closed applicability/materialisation vocabularies, exact-release registration and the initial Merchant Presence/Calendar registrations; MS-PROT-027 v1.8 closes `IMP-06-P2-DG-001` with exact-release typed policy-evaluator bindings, server-established exact source evidence, deterministic fail-closed Projection Serviceability and retained decision provenance while leaving checkpoints, persistence, transport and Exposure resolution deferred; MS-PROT-027 v1.9 closes `IMP-06-E3-DG-001` with a trusted exact-Active-Release request-scoped Audience Observation Context, closed observation subjects, typed owner extensions, current audience admission and opaque API-surface-bound Exposure outputs while leaving E4, raw contextual credentials, continuation, HTTP mapping and semantic-bundle encoding outside E3; MS-PROT-027 v1.10 closes `IMP-06-E3-DG-002` with immutable positive owner-qualified API Exposure membership, private request/invocation/release/contract provenance, trustworthy empty-result semantics distinct from structural fail-closed resolution, and an explicit anti-semantic-gravity boundary; MS-PROT-027 v1.11 closes `IMP-06-E4-DG-001` with instance-aware membership, deterministic audience-variant contract resolution, exact-release owner-filtered read-only batch evaluators and value-blind E4 while leaving bounded-read representation affinity to S2/T1b4; MS-PROT-027 v1.12 closes `IMP-06-E4-DG-002` with fresh opaque per-candidate submission affinity for each logical evaluator call, exact returned-binding coverage and mechanical cross-request/cross-invocation rejection without adding request/invocation identity to stable candidates or owner evaluation context; MS-PROT-027 v1.13 closes `IMP-06-S2-T1B4-DG-001` with request-bound typed Projection material, exact P2/read/source affinity, current owner material-progress checks where E4 policy is revision-affined, and same-read positive representation selection without generic payloads or post-E4 value reacquisition; MS-PROT-027 v1.14 closes `IMP-06-BR4-DG-001` with a narrow trusted E3 construction boundary that supplies the exact request binding and Merchant Scope to capability-owned contribution constructors without exposing a general request-unwrapping API or transferring contribution semantics to Surface.
- MS-PROT-046 v1.3 resolves the IMP-07-P2B Opportunity material-schema/history design gap with immutable schema-affined Opportunity Material Revisions, the bounded `publication / opportunity@1` schema, typed temporal boundaries, role-qualified external links and append-only `PUBLISH`/`WITHDRAW`/`REPUBLISH` Publication History while leaving P3 idempotency/orchestration, Exposure, Enquiry, transport, generic semantic-value runtime and numeric retention downstream or deferred.
- MS-PROT-049 v1.4 closes `IMP-06-S3-DG-001` by defining owner-qualified capability-owned Public Interaction participation sources, deterministic single-owner routing and a valid fail-closed zero-production-source state for the generic S3 binding projector, while preserving concrete capability sources for their owning vertical slices and leaving the IMP-06 → IMP-07 HARD macro edge unchanged.
- MS-PROT-051 v1.5 establishes independently revisioned Profile-owned Merchant Location public Exposure choice keyed to stable Merchant Location identity, preserves absence as unresolved rather than fabricated private intent, and resolves the design/persistence answer for `MS-PROT-027-V15-DQ-003` subject to conforming implementation evidence.
- ADR-016 resolves `MS-PROT-027-V15-DQ-002` by adding a dedicated integrity-covered Exposure-definition constituent to semantic bundle v2 and `SemanticReleaseAssembly`, preserving exact-release all-or-nothing materialisation while leaving Exposure semantics, runtime evaluation and Projection packaging with their owning authorities.
- CUSTOMER Exposure requirement scope under MS-PROT-027 v1.5 is resolved for Booking/Appointment by MS-PROT-042 v1.6, Ordering by MS-PROT-077 v1.1, Payment by MS-PROT-055 v1.1, Order Fulfilment/Shipment by MS-PROT-060 v1.1, and Returns by MS-PROT-061 v1.1 through the owning relationship authorities.
- MS-PROT-040 v1.2 consumes ADR-012 executable-support and ADR-013 materialisation evidence for activation admission without transferring semantic ownership; MS-PROT-040 v1.3 requires exact durable validation/package and business-facing impact-review evidence before ordinary first-configuration approval persistence; MS-PROT-040 v1.4 resolves `MS-PROT-040-V12-DQ-002` through `DQ-005` with immutable normalized serving-generation evidence, deterministic RCP-affined requirement-set identity and a durable two-phase ordinary-cohort generation fence; MS-PROT-040 v1.5 resolves `MS-PROT-040-V12-DQ-001` with append-only exact release-purpose decisions, current per-release/purpose pointers, immutable ordinary-reference revisions and pinned-release activation provenance; MS-PROT-040 v1.6 establishes replacement/reinstatement Configuration approval authority under the current ACTIVE Merchant Controller, trusted authenticated principal, exact validation/impact-review/package evidence and exact current Controller Relationship affinity, without creating Workforce approval authority; MS-PROT-040 v1.7 establishes ordinary non-initial Configuration activation authority under trusted authenticated execution context, OPEN Merchant Account, no effective account-wide Suspension, current ACTIVE Merchant Controller, exact current approval, compatibility/admission and atomic activation, without creating Workforce, system, AI or scheduled ordinary activation authority.; MS-PROT-040 v1.8 binds every reinstatement decision to the exact current Reinstatement Basis Activation, requires basis-affined fresh validation/impact/approval evidence, permits superseded initial-revision reinstatement through a bounded reinstatement authority and prevents historical approval or same-revision cycle aliasing from becoming current reinstatement authority.
- MS-PROT-050 v1.4 closes `IMP-05-B2-DG-001` with immutable scope-affined stable-hours revisions, one durable current pointer per exact Business Hours Scope, explicit withdrawal, exact expected revision, logical retry identity and current-Controller authority; merchant-location mutation remains dependent on authoritative Merchant Location.
- MS-PROT-051 v1.2 resolves `MS-PROT-051-V11-DQ-002` with the version-affined provider-neutral `PostalAddressV1` representation, retained original/normalized structured evidence and optional accepted-coordinate provenance without making address or provider evidence Merchant Location identity.
- MS-PROT-051 v1.3 resolves `IMP-05-A3B-DG-001` with the versioned `ServiceAreaGeographyV1` tagged union, exact Location-revision affinity for radius values and immutable Service Area revision evidence while prohibiting descriptive geography from becoming executable eligibility.
- MS-PROT-051 v1.4 resolves `IMP-05-A3D-DG-001` with independently revisioned `MerchantClassificationEntryV1` facts, immutable kind, merchant-approved normalized free-text labels and explicit source exposure while preserving classification as non-executable metadata; `MS-PROT-051-V11-DQ-012` remains deferred and inactive.
- MS-PROT-042 v1.6 resolves the Scheduling-specific scope of `MS-PROT-048-V14-DQ-014`; MS-PROT-042 v1.7 permits explicit owner-qualified Workforce availability evidence from MS-PROT-081 to constrain new Appointment scheduling without transferring Appointment commitment ownership; MS-PROT-042 v1.8 requires that workforce-evidence interoperability be bound to an explicit Workforce Scheduling Arrangement ↔ schedulable Resource link rather than Merchant Membership or Identity similarity alone. MS-PROT-042 v1.9 establishes Appointment-owned occurrence outcome and correction semantics, prohibits clock/payment/AI/provider inference of occurrence, preserves broader service fulfilment as separate truth, and groups the retained MS-PROT-042 semantic work queue without activating implementation. MS-PROT-042 v1.10 resolves `MS-PROT-042-GRP-02` with deterministic TimeProposal acceptance deadlines, explicit UNPROTECTED/PROTECTED modes, proposal-specific Appointment Proposal Hold semantics, automatic deadline-aligned release, expired-Hold exclusion and an atomic Hold-to-Appointment-allocation transition while preserving final Appointment revalidation. MS-PROT-042 v1.11 resolves `MS-PROT-042-GRP-05` by establishing customer-decision authority for merchant-initiated Appointment rescheduling, exact-revision-affined reschedule proposals, policy-qualified direct merchant changes, proposal target-capacity protection and Appointment-owned check-in evidence/correction while preserving occurrence, Scheduling, Workforce, Notification and Customer Messaging ownership boundaries. MS-PROT-042 v1.12 resolves `MS-PROT-042-GRP-04` by separating Appointment-owned support requirements from Resource/Allocation-owned current supporting capacity, distinguishing within-requirement substitution from material support-requirement change, preserving exact Workforce Scheduling Arrangement affinity, authoritative target revalidation and invariant-preserving replacement, and preventing staff/resource reassignment from silently rewriting Appointment, Booking, Check-In or occurrence truth. MS-PROT-042 v1.13 resolves `MS-PROT-042-GRP-03` with Appointment-owned recurring Series authority, one-Appointment-per-Series-Occurrence commitment admission, local-civil-time recurrence, independently authoritative shared Appointment participations, self-scoped participant access/withdrawal and participant attendance outcomes while preserving Scheduling, Resource/Allocation, Workforce, Payment, Notification and CustomerContext ownership boundaries. MS-PROT-042 v1.14 resolves `MS-PROT-042-GRP-01` by preserving narrow `IN_FORCE`/`RELEASED` Booking commitment truth, adding registered Booking Natural Discharge Contracts, independently evidenced Booking Utilisation Outcomes, immutable Booking Commitment Revisions and capacity-safe booked-subject/reservation-scope amendment semantics while preserving Resource/Allocation, CustomerContext, Payment, Notification, policy, audit and AI ownership boundaries. MS-PROT-042 v1.15 resolves `MS-PROT-042-GRP-06` by admitting bounded capacity waitlist coordination as a separately owned future capability, explicitly rejecting Booking/Appointment ownership of waitlist membership, rejecting overbooking for the initial portfolio, preserving ordinary capacity/concurrency invariants and promoting the admitted waitlist scope to the unapproved next design node `MS-PROT-089` without implementation activation.
- MS-PROT-055 v1.1 resolves the Payment-specific scope of `MS-PROT-048-V14-DQ-014`.
- MS-PROT-060 v1.1 resolves the Shipment-specific scope of `MS-PROT-048-V14-DQ-014`.
- MS-PROT-061 v1.1 resolves the return-label-specific scope of `MS-PROT-048-V14-DQ-014`.
- MS-PROT-075 v1.1 resolves the Notification-specific scope of `MS-PROT-048-V14-DQ-014`; MS-PROT-075 v1.2 closes the retained Notification provider-selection/status-mapping, retry/backoff, rendering, preference, marketing-contact-policy ownership, reminder-timing ownership, batching/digest, quiet-hours and open/click-tracking tail while preserving provider-neutral fulfilment. Separately owned Marketing contact-policy semantics remain governed by `MS-PROT-087-DQ-003` until explicitly approved.
- MS-PROT-053 v1.2 resolves the semantic data-protection lifecycle/convergence scope of `MS-PROT-027-V15-DQ-007`.
- MS-PROT-053 v1.3 resolves `MS-PROT-043-V14-DQ-007` with concrete Enquiry/Customer Communication retention qualification and baseline periods: 12 calendar months for Transitory Enquiry content, 24 calendar months per ordinary ConversationMessage, shorter bounded provider/security/tombstone periods, owner-qualified longer business-evidence retention where applicable, plan-neutral lifecycle semantics and recovery-owned backup retention. It preserves Enquiry/Conversation/source-capability ownership and does not activate implementation.
- MS-PROT-077 narrowly supersedes ADR-005's universal Business Order proposition; Booking, Appointment, Enquiry and Publication remain independent.
- MS-PROT-058 v1.1 narrowly supersedes ADR-007 only where ADR-007 assumed whole-claim resolution.
- MS-PROT-079 v1.1 preserves the original Targets 1–21 as the historical backend-design completion baseline, qualifies Target 11 as Customer Booking / Appointment Scheduling, and registers composite MS-PROT-081 as the Workforce Scheduling/Timekeeping/Leave post-baseline extension and composite MS-PROT-080 as the Workforce Compensation/Payroll post-baseline extension without retroactively reopening earlier targets.
- MS-PROT-080 v1.1 completely supersedes v1.0 and establishes Workforce Compensation as the generic merchant-facing compensation boundary; MS-PROT-080 v1.2 composes authoritative worked-time/leave evidence from MS-PROT-081; MS-PROT-080 v1.3 requires exact Workforce Scheduling Arrangement and Compensation Relationship affinity, independently governs related-Payee Compensation self-service, removes `Paid Break Entitlement` as a canonical generic concept, and records `MS-PROT-080-V11-DQ-005` resolved while narrowing DQ-013 to generic/former/legal-entity Payee access mechanics.
- MS-PROT-081 v1.0 establishes Workforce Scheduling, Timekeeping, Scheduled Break and Leave authority; MS-PROT-081 v1.1 introduces the narrow Workforce Scheduling Arrangement, arrangement-scopes Workforce Time Terms, shifts, time evidence and leave, preserves optional exact Compensation Relationship affinity, prevents cross-arrangement evidence reuse, and adds DQ-021/DQ-022 for overlap and retrospective attribution; MS-PROT-081 v1.2 resolves DQ-021 with an exact symmetric Arrangement-pair policy (`OVERLAP_ALLOWED` / `OVERLAP_PROHIBITED`, absence unresolved), exact one-off `CrossArrangementOverlapOverride`, and optional merchant-owned Membership-wide `MinimumInterCommitmentBuffer` for sequential non-overlapping `ScheduledWorkCommitment`s. v1.2 preserves MS-PROT-091 `Shift` as work-requirement truth, MS-PROT-081 `ScheduledWorkCommitment` as worker-scheduling truth, defines no universal buffer duration or unassigned-Shift spacing rule, leaves DQ-022 unresolved, and authorises no implementation activation. MS-PROT-081 v1.3 partially resolves DQ-015 by registering exactly seven source-owned Workforce Scheduling/Leave Notification Contracts — `scheduled-work-established@1`, `scheduled-work-materially-revised@1`, `scheduled-work-released@1`, `targeted-shift-offer-issued@1`, `targeted-shift-offer-no-longer-actionable@1`, `leave-decision-recorded@1` and `scheduled-work-reminder@1` — while preserving Notification delivery under composite MS-PROT-075, durable wake-up under MS-PROT-065, jurisdiction-qualified Leave entitlement outside Notification, no universal reminder cadence, and the exact Timekeeping notification/reminder remainder as deferred pending DQ-009 through DQ-012. v1.3 authorises no implementation activation.
- MS-PROT-091 v1.0 refines composite MS-PROT-081 for rota composition without creating a competing worker-scheduling commitment: it owns RotaPeriod, rota Shift requirements/cardinality, OPEN_SELECTION ShiftClaim coordination, operational ScheduleExclusion distinct from Leave and conditionally admitted WorkSite semantics; every worker-specific operation is exact-Workforce-Scheduling-Arrangement-affined and accepted open claims converge on MS-PROT-081 ScheduledWorkCommitment. It preserves MS-PROT-081-DQ-013; its original preservation of DQ-015 is superseded in current navigation by MS-PROT-081 v1.3, which partially resolves the Scheduling/Leave/Scheduled-Work-reminder slice while leaving the Timekeeping notification/reminder remainder deferred. The former DQ-021 is resolved by MS-PROT-081 v1.2 and remains MS-PROT-081-owned rather than MS-PROT-091-owned. MS-PROT-091 prohibits direct rota-to-customer-bookability inference and authorises no implementation activation.
- MS-PROT-092 v1.0 establishes the generic purpose-bound Document Intake → non-authoritative Extraction Candidate → owner-qualified validation/evidence-handoff boundary. It preserves composite MS-PROT-053 data lifecycle, composite MS-PROT-057 AI/security, composite MS-PROT-066 canonical media/file lifecycle, composite MS-PROT-028 claim-specific verification/trust, and every consuming capability's business-truth ownership. It does not resolve `MS-PROT-084-DQ-013` or `MS-PROT-057-V11-DQ-010`, does not make duplicate observation equivalent to authenticity/fraud adjudication, and authorises no implementation activation.
- MS-PROT-092 v1.1 adds only commercial-access classification for the accepted Document Evidence Coordination substrate: five exact purpose-bound `PLATFORM_SERVICE_ACCESS` contracts cover intake establishment, Extraction Candidate processing, evidence validation, owner-qualified Evidence Handoff and bounded coordination observation, all with no independent Commercial Entitlement. It preserves Media/file lifecycle ownership, consuming-owner business truth and commercial permission, Actor Authorisation, Data Protection, Exposure, security admission, Provider Readiness, Resource Protection and AI non-authority. It contributes no standard entitlement identity and leaves `MS-PROT-056-V17-DQ-001` open for the complete concrete Commercial Catalogue Manifest and any remaining owner/supporting classifications.
- MS-PROT-082 v1.0 establishes the Jurisdiction & Regulatory Administration context: purpose-qualified Jurisdiction Context, source-backed/effective-dated Regulatory Rule Set Releases, Jurisdiction Pack Releases, purpose-specific support declarations, immutable Regulatory Determinations and qualified Regulatory Administrative Requirements. It preserves capability ownership, composes provider fulfilment through MS-PROT-048, does not resolve jurisdiction-specific Payroll/Workforce implementation details, and explicitly defers Business Health/Financial Intelligence semantics to separate authority. MS-PROT-082 v1.1 resolves `MS-PROT-082-DQ-001` and `MS-PROT-082-DQ-002` with exactly England and Wales as the initial independently addressable `CommercialJurisdiction` portfolio, exactly three initial Regulatory Purposes — Commercial Transaction Regulatory Administration, Workforce Regulatory Administration and Business Authorisation Regulatory Administration — and exactly four initial `FilingScopeReference` subtypes — Merchant Filing Scope Reference, Operating Location Filing Scope Reference, Workforce Filing Scope Reference and Commercial Activity Filing Scope Reference. Vocabulary admission does not activate a Jurisdiction Pack or imply public support; DQ-003 through DQ-007 remain unchanged and implementation activation remains NONE.
- MS-PROT-083 v1.0 establishes the derived cross-capability analytical and operational-decision-support substrate: versioned Analytical Input Bindings and Measure Definitions, exact evaluation affinity and evidence coverage, Business Health indicators/assessments, epistemically classified Analytical Claims, qualified probabilistic methods, Forecasts, Scenario Evaluations, Impact Estimates and non-authoritative Business Recommendations. It preserves source-capability ownership, Merchant Attention, Operational Health/Alert, Reconciliation, Projection/Exposure and JRA boundaries, prohibits autonomous BI mutation, and deliberately leaves complete Financial Health/Financial Intelligence semantics unresolved. MS-PROT-083 v1.1 resolves the campaign-specific slice of `MS-PROT-083-DQ-001` and resolves `MS-PROT-087-DQ-005` by registering five exact Campaign Analytical Measure families plus deterministic `DIRECT_EXECUTION_TRACE_V1`, preserving provider/source evidence qualification, evidence coverage and as-of evaluation while explicitly rejecting open/click/conversion/revenue/retention/ROI and downstream commercial-causation claims. MS-PROT-083 v1.2 further partially resolves `MS-PROT-083-DQ-001` for the customer-return slice with exact fulfilled-Order, occurred/partially-occurred Appointment and utilised/partially-utilised Booking activity contracts, four customer-return Measure Definition families, coverage-honest RETURNING classification and a merchant-initiated handoff into existing Marketing re-engagement authority. It rejects a standalone Retention/CRM capability, universal retention rate, durable customer lifecycle states, churn/propensity scoring, automatic re-engagement triggers and Campaign-to-return causation. MS-PROT-083 v1.3 resolves the remaining initial general `MS-PROT-083-DQ-001` portfolio with exactly seven general Measure Definition families — Order commitment count, Appointment occurrence-outcome count, Booking utilisation-outcome count, outstanding Payment Obligation count/value, Inventory position constraint count and scheduled-commitment person-duration — plus the bounded `Commitment Interaction Origin` analytical dimension (`ONLINE`, `WALK_IN`, `TELEPHONE`, `OTHER_REPRESENTED`, `UNRESOLVED`) derived from MS-PROT-059/source provenance. It preserves source ownership, missing/non-applicable versus zero, Currency separation, no generic KPI engine, no channel-specific business objects, no Payment/Fulfilment/Marketing-attribution conflation and no Business Health inference. `MS-PROT-083-DQ-001` is RESOLVED. MS-PROT-083 v1.4 resolves `MS-PROT-083-DQ-009` by establishing one bounded Business Intelligence Analytics `WORKSPACE` contribution, the request-scoped `business-intelligence/merchant-analytics` Projection Contract, analytical presentation-fidelity and progressive-disclosure requirements, a Mandatory Honesty Envelope, natural-language/chart truthfulness rules and cross-platform conformance while preserving MS-PROT-037/049/085/093/094 ownership and rejecting a universal UI DSL. MS-PROT-083 v1.5 resolves `MS-PROT-083-DQ-002` with exactly one directly MS-PROT-083-owned non-financial Business Health Indicator family, `business-health/inventory-claim-integrity@1`, driven only by the accepted `ACTIVE_CLAIM_SHORTFALL` analytical dimension under complete current evidence; it composes the five accepted MS-PROT-084 Financial Health indicator assessments by reference, rejects a universal Business Health score/status and refuses unsupported KPI-health thresholds for demand, customer return, appointment/booking outcomes, workforce coverage, campaign performance or generic stock health. MS-PROT-083 v1.6 resolves `MS-PROT-083-DQ-010` with exactly one initial merchant analytical report contract, `business-intelligence / merchant-analytical-report@1`, limited to `ON_DEMAND_SNAPSHOT` generation with `CSV` and `PDF` encodings. Reports bind request-scoped UTC as-of time and exact measure/indicator scope; preserve source-owner identities, measure/indicator identities, Currency boundaries, evidence windows, missing/non-applicable/unknown/unsupported/zero distinctions, coverage and reconstructible provenance; and render only from the exact frozen analytical snapshot without reacquiring current source state. Scheduled/digest/professional/statutory exports remain outside the initial portfolio, `MS-PROT-083-DQ-006` and the remaining unresolved BI tail stay deferred, and implementation activation remains NONE. MS-PROT-083 v1.7 adds Business Intelligence commercial-access classification: BUSINESS + GROWTH protect new evaluation/re-evaluation of the accepted business-analytics portfolio through `USE_BUSINESS_ANALYTICS`; GROWTH separately protects the accepted Campaign analytical portfolio through `USE_CAMPAIGN_ANALYTICS`; merchant analytical presentation has no independent Commercial Entitlement. It preserves source ownership, exact analytical applicability/coverage, historical-artifact observation boundaries and Marketing separation. MS-PROT-083 v1.8 completes the current Business Intelligence owner/supporting commercial classification by classifying the v1.6 PDF report and bounded measure-observation export delivery access paths with no independent Commercial Entitlement while preserving externalisation authority, Audit, Resource Protection and v1.7 evaluation permissions. DQ-001 remains open for the final concrete Commercial catalogue manifest and any other remaining owner/supporting classifications.
- MS-PROT-084 v1.1 establishes bounded residual Financial Operations ownership, initial Operating Cost and Financial Evidence portfolios, Financial Account purpose/evidence-scope applicability, MS-PROT-092 financial document-evidence consumption, economic-exposure non-duplication and five initial Financial Health indicator families through MS-PROT-083. It resolves `MS-PROT-083-DQ-015` and, composed with MS-PROT-083, resolves `MS-PROT-082-DQ-007`; it does not activate implementation or establish general-ledger/statutory accounting. The earlier targeted amendment is retained as historical evidence. MS-PROT-084 v1.2 adds only the Financial Operations commercial-access classification: protected establishment of new/materially expanded Financial Operations truth belongs to BUSINESS + GROWTH, while bounded preparation, existing-record observation and pure existing-record resolution require no independent Commercial Entitlement. DQ-001 remains open for the final catalogue.
- MS-PROT-085 v1.0 establishes the cross-capability Merchant Attention coordination substrate: immutable versioned Attention Contracts, source-owner-qualified contributions, stable recurrence-aware Merchant Attention Occurrences, immutable handling facts, coverage-qualified observation and Candidate Action handoff through a new authorised instruction. It preserves source truth, Notification, Business Intelligence, Operational Health/Alert, Reconciliation, Projection/Exposure and runtime-authority ownership; resolves `MS-PROT-043-V14-DQ-005` and `MS-PROT-083-DQ-008`; and originally deferred the initial production Attention Contract portfolio under `MS-PROT-085-DQ-001`. MS-PROT-085 v1.1 resolves that decision by selecting exactly `enquiry / initial-submission-review@1`: Controller-only explicit initial review, without assignment, snooze, response claims or historical backfill. MS-PROT-085 v1.2 adds exactly `customer-communication / human-response-required@1`, whose source is an exact inbound ConversationMessage plus CustomerServiceResponseAssessment requiring human handling; it permits durable human-response or explicit outside-Main-Street handling evidence without creating source-business operation authority, response SLA, assignment or snooze semantics. Merchant activation and implementation remain separately governed.
- MS-PROT-086 v1.0 establishes the provider-neutral Customer Communication substrate: contract-governed Conversation creation/reuse, participant/guest/channel bindings, immutable idempotent Messages, relationship-bound source access, delivery separation, contract-governed customer-service response assessment and truthful Merchant Attention handoff. It resolves `MS-PROT-043-V14-DQ-006` and originally deferred channel, automated-response, guest-access and attachment portfolios under `MS-PROT-086-DQ-001` through `MS-PROT-086-DQ-004`. MS-PROT-086 v1.1 resolves `MS-PROT-086-DQ-001` by selecting exactly Merchant Website Messaging plus Conversation-Bound Email, with `WEBSITE_MESSAGE_CREATE_V1` and `EMAIL_REPLY_CONTINUE_V1`, authenticated CustomerContext participant binding for registered signed-in customers, unverified guest reply endpoints, deterministic Conversation-bound email correlation and no generic inbound mailbox semantics. MS-PROT-086 v1.2 resolves `MS-PROT-086-DQ-003` by defining registered-customer participation-backed browser access and a distinct one-Conversation Guest Conversation Access Grant/Proof path, with `CONVERSATION_BROWSER_VIEW_V1`, `CONVERSATION_BROWSER_CONTINUE_V1` and `REGISTERED_CONVERSATION_DISCOVERY_V1`; guest grants permit exactly `VIEW` and `APPEND_TEXT_MESSAGE`, use a 90-day idle expiry and 12-calendar-month hard absolute lifetime, do not derive authority from contact equality, remain independent from Conversation-Bound Email routing and Message retention, and do not grant source-object authority. MS-PROT-086 v1.3 resolves `MS-PROT-086-DQ-002` by selecting exactly seven fact-first Customer-Service Response Contract families for public merchant information, public offering information, published policy information, current Scheduling availability, related Booking/Appointment information, related Order/Fulfilment/Shipment information and related Payment/Refund information; automatic substantive response requires complete request coverage, exact owner-qualified evidence/access, deterministic validation and the paired MS-PROT-085 v1.2 human-response Attention path. AI confidence, generic FAQ/RAG material and Conversation access do not create response or source-object authority. MS-PROT-086 v1.4 adds the current Customer Communication commercial-access classification: `CONDUCT_CUSTOMER_COMMUNICATION` and `AUTOMATE_ROUTINE_CUSTOMER_SERVICE` are protected for BUSINESS + GROWTH through exact `PLATFORM_SERVICE_ACCESS` contracts, while bounded human preparation, existing-Conversation observation and committed progression have no independent Commercial Entitlement. It preserves FREE Enquiry separation, current participant/actor/source access, no free existing-Conversation continuation, historical Message truth, Notification/Attention/source ownership and provider/AI non-authority. DQ-004 remains unresolved, final Commercial-owned entitlement identities/bindings remain under `MS-PROT-056-V17-DQ-001`, and no merchant activation or implementation is authorised.
- MS-PROT-087 v1.0 establishes bounded merchant Marketing Campaign authority: immutable Campaign Revisions, registered Audience Definitions, current campaign-specific Recipient Eligibility Assessments, exact merchant approval, bounded Automated Campaign Contracts, stable Campaign Occurrences, suppression/deduplication and provider-qualified outcome evidence. It preserves CustomerContext, Publication, Notification, Conversation, source-business, data-protection, analytics, provider, Merchant Attention and AI ownership. MS-PROT-087 v1.1 resolves `MS-PROT-087-DQ-001` by selecting exactly four purpose families — `marketing/merchant-news-awareness@1`, `marketing/offering-awareness@1`, `marketing/customer-appreciation@1` and `marketing/customer-reengagement@1` — and exactly two outreach families — `WEBSITE_ANNOUNCEMENT_V1` and `DIRECT_EMAIL_MARKETING_V1`. Standalone website Announcements remain directly MS-PROT-046-owned; initial direct marketing is relationship-based rather than prospecting-based; direct email remains MS-PROT-075 Notification rather than MS-PROT-086 Conversation-Bound Email; personalisation remains shallow. MS-PROT-087 v1.2 resolves `MS-PROT-087-DQ-002` by selecting exactly three Audience Definition families — `marketing-audience/existing-customer@1`, `marketing-audience/recent-customer-relationship@1` and `marketing-audience/previous-customer-reengagement@1` — using only CustomerContext plus owner-qualified Order, Booking and Appointment commitment evidence for the bounded recency families. It rejects CRM lifecycle labels, generic customer activity, product-history segmentation, spend/value scoring, sensitive/proxy targeting and AI-selected audiences; insufficient negative-evidence coverage yields `UNRESOLVED`. MS-PROT-087 v1.3 resolves `MS-PROT-087-DQ-003` and `MS-PROT-087-DQ-004` together as `MKT-GRP-01 — Direct Outreach Safety & Automation`, establishing jurisdiction-qualified direct-email contact-policy determinations, durable endpoint/CustomerContext suppression, immediate unsubscribe effect, bounded cross-campaign contact pressure, exact Campaign-Revision-affined Automated Campaign Contracts, scheduled-single and periodic-audience trigger families, bounded weekly/monthly recurrence, no catch-up/replay, and mandatory per-occurrence/pre-externalisation revalidation. MS-PROT-083 v1.1 then resolves `MS-PROT-087-DQ-005` / `MKT-GRP-02` with deterministic direct-execution Campaign measurement and a hard no-commercial-causation boundary. MS-PROT-087 v1.4 adds the Marketing commercial-access classification: exactly `marketing/campaign-service-access@1` / `CONDUCT_MARKETING_CAMPAIGNS` is protected for GROWTH, while bounded non-committing preparation, existing Marketing-state observation and Campaign restriction require no independent Commercial Entitlement. It preserves current-entitlement revalidation before protected new use/externalisation, retained-history/downgrade boundaries, owner separation and the no-generic-committed-progression boundary. `MS-PROT-087-DQ-006` remains the sole retained Marketing production gate, the final `MS-PROT-056-V17-DQ-001` catalogue manifest remains open, and no implementation or merchant activation is authorised.
- MS-PROT-088 v1.0 establishes Merchant Brand Infrastructure as a provider-neutral platform service owning Merchant Brand Namespace, domain-control coordination, Website Hostname Binding and Business Email Identity. It keeps domains distinct from Merchant Scope, preserves existing website/email infrastructure by default, makes registrar transfer optional, requires merchant domain portability, keeps website and business-email bindings independently activatable, prohibits generic mailbox semantics and preserves Storefront, Notification, Customer Messaging, provider and credential ownership boundaries. It introduces no unresolved semantic DQ and authorises no implementation activation.
- MS-PROT-088 v1.1 — `MS-PROT-088 v1.1 — Website Namespace Use Commercial Access Contracts Amendment.md` — governs exact platform-namespace and merchant-controlled-domain commercial-use contracts. Paired MS-PROT-036 v1.3 — `MS-PROT-036 v1.3 — Hostname-Independent Website Delivery Commercial Access Amendment.md` — governs their common website-delivery contract. MS-PROT-094 retains presentation/composition and shared-rendering authority. Neither amendment establishes provisioning, publication, cutover, executable grants or implementation activation.
- MS-PROT-036 v1.4 — `MS-PROT-036 v1.4 — Storefront Composition Publication Lifecycle & Commercial Access Amendment.md` — supplies exact preparation, merchant-publication, platform-maintenance and withdrawal contracts, immutable Storefront Publication Selection, and the business-evolution presentation handoff. It preserves Configuration activation, dashboard ownership and MS-PROT-094 composition/shared-rendering authority. DQ-001 remains OPEN; no production activation or C3 completion is authorised.
- MS-PROT-088 v1.2 — `MS-PROT-088 v1.2 — Website Binding Selection, Disconnection & Commercial Access Amendment.md` — governs exact inspection, selection and disconnection contracts, immutable Website Binding Selection, independent namespace-family selection boundaries and supporting commercial requirements. §§2, 10, 12, 14 retain allocation, external execution, retention and catalogue gates. MS-PROT-094 remains unchanged; DQ-001 remains OPEN and no production activation or C3 completion is authorised.
- MS-PROT-088 v1.3 — `MS-PROT-088 v1.3 — Platform Website Namespace Allocation & Retirement Amendment.md` — supplies the website-qualified platform allocation, naming and retirement scope retained by v1.1 §8 and v1.2 §§2, 14. §§3–12 govern assignment identity, non-reused discriminators, current Controller authority, commercial composition, terminal retirement and binding-safe concurrency. §§13–15 retain production lifecycle/recovery, parent-domain, external provisioning and catalogue gates. DQ-001 remains OPEN; no production activation or C3 completion is authorised.
- MS-PROT-088 v1.4 — `MS-PROT-088 v1.4 — Website Connection Request & Bounded Completion Amendment.md` — supplies the bounded deferred local-binding authorisation scope retained by v1.2 §10. §§3–12 govern immutable approved request content, one pending request per scope/family, exact commercial composition, Controller approval, separately registered completion authority, cancellation/supersession and atomic completion/recovery. §7 qualifies v1.0 §26's intended-binding preparation authority without authorising external effects. §§13–16 retain external execution, data-lifecycle and catalogue gates. DQ-001 remains OPEN; no production activation, C3 completion or programme promotion is authorised.
- MS-PROT-088 v1.5 — `MS-PROT-088 v1.5 — Website DNS, Certificate Execution & Recovery Amendment.md` — supplies bounded website external-execution composition retained by v1.2 §10, v1.3 §§13–15 and v1.4 §§7, 11, 16. §§3–16 govern Website Infrastructure Plan, exact application/fulfilment contracts, conditional DNS changes, HTTPS-before-cutover ordering, maintenance, dispatch/cancellation ordering, safe cleanup, historical-effect reconciliation and durable progression. §§2, 17–18 retain initial proof mechanisms, provider/security/lifecycle qualification, executable support and production verification. DQ-001 remains OPEN; no vendor selection, production activation, catalogue publication or C3 completion is authorised.
- MS-PROT-089 v1.0 establishes bounded Capacity Waitlist authority for exact Appointment-interval, shared-Appointment-participation and Booking-reservation unmet demand: CustomerContext-bound Waitlist Entries, immutable FIFO Queue Precedence, current source-owner-qualified admission/opportunity/candidate assessments, one active Promotion per exact target, explicit UNPROTECTED/SOURCE_PROTECTED source-capacity boundaries, versioned Promotion Response Profiles, operational Notification handoff, purpose-bound customer ACCEPT/DECLINE authority and source-owned commitment revalidation. It prohibits generic queues, VIP/AI/manual priority, batch first-to-click offers, Conversation-as-command, Waitlist-owned Holds/Allocation, hidden overbooking and product-restock expansion; it retains no semantic DQ for the initial portfolio and authorises no implementation activation.
- MS-PROT-090 v1.0 establishes the bounded external Review/Reputation coordination model: source-qualified Appointment/Booking review-experience eligibility, neutral `ALL_ELIGIBLE_ONCE` solicitation, provider-owned external review truth, sentiment-neutral Merchant Attention integration, exact merchant approval for provider-bound review replies, and explicit anti-review-marketplace/anti-reputation-score boundaries; it has no retained semantic DQ for its initial portfolio and implementation activation remains NONE.
- MS-PROT-074 v1.2 establishes membership-relative own-subject Personal Workforce Self-Service Actor Authorisation without creating a default merchant Role and preserves merchant-operational Role/Device requirements for cross-subject administration.
- ADR-014 supplies production authentication/session architecture beneath MS-PROT-028, MS-PROT-063, MS-PROT-071, MS-PROT-074 and MS-PROT-076 without redefining their semantic ownership. `ADR-014-DQ-011` remains separately owned security architecture and is active before production Guest Conversation browser access after accepted MS-PROT-086 v1.2; exact physical credential representation remains security-architecture scope and is not resolved by MS-PROT-086. `ADR-014-DQ-014` is resolved by MS-PROT-093 v1.0 for the installed native merchant-client architecture: native clients use the same accepted identity/session authority with a revocable opaque server-authoritative native session transport while browser cookie/CSRF rules remain browser-specific.

- MS-PROT-056 v1.7 amends v1.1 only within service-to-tier allocation scope and preserves all other applicable provisions through v1.6. Its §§1–12 govern standard FREE/BUSINESS/GROWTH allocation, distinguish defined-scope allocations from future reservations and leave executable grants, catalogue publication, prices and reserved-service admission downstream under `MS-PROT-056-V17-DQ-001` through `MS-PROT-056-V17-DQ-004`. It authorises no implementation activation or C3 readiness change and does not resolve the MS-PROT-082 v1.1 lifecycle-status finding.

- MS-PROT-056 v1.8 amends v1.7 only within analytical portfolio allocation and supporting presentation access: §§3–4 allocate the exact initial customer-return measures to BUSINESS and campaign measures to GROWTH; §§6–8 preserve portfolio-specific permission, presentation fidelity, Marketing authority and historical boundaries. It leaves all four `MS-PROT-056-V17-DQ-*` records open and authorises no executable grants, catalogue publication or implementation activation.

- MS-PROT-056 v1.9 amends v1.0 §§4–7, v1.1 §§6–8 and v1.5 §§12–13 within catalogue binding/publication scope and supplies v1.6 §3's historical catalogue-resolution policy. Its §§3–13 establish exact bindings, immutable manifest publication, publication-time effectiveness, historical resolution and recovery; §17 resolves `MS-PROT-056-V17-DQ-002` only for catalogue-publication and temporal-selection policy. DQ-001, DQ-003 and DQ-004 remain open; no concrete production manifest, implementation activation or C3 completion is authorised.

---

## 3.2 MS-PROT-084 complete Financial Operations authority

MS-PROT-084 v1.1 remains the complete current authority for its Financial Operations / Financial Evidence source scope and Financial Health source composition. MS-PROT-084 v1.2 composes with it only within commercial-access classification scope. The earlier `designs/historical/ms-prot-084/MS-PROT-084 - Final Targeted Amendment Patch.md` remains historical approval/falsification evidence and no longer constitutes a current incomplete-base blocker.

The v1.1 composition closes `MS-PROT-083-DQ-015` and compositionally closes `MS-PROT-082-DQ-007` with MS-PROT-083. It records the exact DQ-001..016 disposition in the canonical DDR. v1.2 adds the exact Financial Operations owner classifications needed by `MS-PROT-056-V17-DQ-001` while preserving those v1.1 semantic and deferred-decision boundaries. Neither authority activates production implementation or requires an `IMPLEMENTATION-RULES.md` amendment.

---

## 4. Accepted TAS / ADR Implementation-Architecture Authority

| Authority | Current accepted composition / location |
|---|---|
| ADR-009 | accepted — `docs/foundation/adr/capability-owned-residual-authority-composition.md` |
| ADR-010 | accepted — `docs/foundation/adr/semantic-release-assembly.md` |
| ADR-011 | accepted — `docs/foundation/adr/semantic-release-publication-retention-bootstrap.md` |
| ADR-012 | accepted — `docs/foundation/adr/runtime-semantic-execution-compatibility.md` |
| ADR-013 | accepted — `docs/foundation/adr/packaged-semantic-definition-materialisation-production-bootstrap.md` |
| ADR-014 | accepted v1.0 `docs/foundation/adr/high-assurance-authentication-session-trusted-browser-execution.md` + accepted v1.1 `docs/foundation/adr/high-assurance-authentication-session-trusted-browser-execution-v1.1-identity-security-generation.md`; native merchant-client session-transport scope additionally constrained by MS-PROT-093 v1.0 |
| ADR-015 | accepted v1.0 `docs/foundation/adr/webauthn-passkey-production-adapter.md` + accepted v1.1 `docs/foundation/adr/webauthn-passkey-production-adapter-v1.1-authentication-subject-binding.md` |
| ADR-016 | accepted v1.0 — `docs/foundation/adr/exposure-definition-packaging-semantic-release-materialisation.md` |
| MS-TAS-RECOVERY-001 | accepted v1.0 `docs/development/TAS/backup-restore-disaster-recovery.md` + accepted v1.1 `docs/development/TAS/backup-restore-disaster-recovery-v1.1-amendment.md` |

### ADR-014 / ADR-015 authentication composition

ADR-014 establishes the high-assurance server-authoritative session/browser architecture. `ADR-014-DQ-001` is resolved by ADR-015; `ADR-014-DQ-006` is resolved by verified IMP-04 durable Session Record evidence. `ADR-014-DQ-011` is active before production Guest Conversation browser access after accepted MS-PROT-086 v1.2; exact physical credential representation remains security-architecture scope and is not resolved by MS-PROT-086. `ADR-014-DQ-014` is resolved by MS-PROT-093 v1.0 for installed native merchant clients while preserving ADR-014's identity/session authority and browser-specific cookie/CSRF profile.

ADR-014 v1.1 assigns the Identity Security context as the durable owner of current Identity security-generation state and requires optimistic rotation, Identity-wide Session revocation and non-secret authentication-security Audit evidence to commit atomically.

ADR-015 v1.0 selects Spring Security WebAuthn as the replaceable ceremony/cryptographic-verification adapter while GrandRue retains Identity, opaque Session, Merchant Scope/relationship revalidation and business authority.

ADR-015 v1.1 adds the approved WebAuthn Authentication Subject binding:

```text
GrandRue Identity
        1
        |
        0..1 active
opaque WebAuthn Authentication Subject Handle
        1
        |
        0..N WebAuthn credentials
```
The handle is GrandRue-generated, opaque and non-PII; it is not the semantic Identity identifier and carries no Merchant Scope, Controller, role, privilege or entitlement semantics. Spring user-entity persistence and handle-to-Identity resolution remain authentication infrastructure only. High-risk hardware/attestation policy remains deferred under `ADR-014-DQ-002`.

---

## 5. Accepted Implementation Programme & Implementation Governance Authority

| Authority | Current accepted composition |
|---|---|
| MS-IMP-001 | accepted v1.0 `designs/authorities/programme/MS-IMP-001/MS-IMP-001.md` + accepted v1.1 `designs/authorities/programme/MS-IMP-001/MS-IMP-001 v1.1 — Post-Baseline Workforce Extension Alignment Amendment.md` |
| MS-DESIGN-RULES-001 | integrated accepted v2.5 — `designs/DESIGN-RULES.md` |
| MS-DOCUMENT-GOVERNANCE-001 | integrated accepted v2.4 — `designs/DOCUMENT-GOVERNANCE.md` |
| MS-DESIGN-CORPUS-CONFORMANCE-001 | integrated accepted v2.6 — `designs/DESIGN-CORPUS-CONFORMANCE.md` |
| MS-IMPLEMENTATION-RULES-001 | integrated accepted v2.0 — `designs/IMPLEMENTATION-RULES.md` |

MS-IMP-001 governs the macro production-implementation graph. DESIGN-RULES governs the design lifecycle and Fundamental Vision Conformance before acceptance. DOCUMENT-GOVERNANCE governs canonical authority placement and navigation. DESIGN-CORPUS-CONFORMANCE governs deterministic structural validation. IMPLEMENTATION-RULES governs READY-node execution through dependency-complete behavioural slices, tests-first implementation, proportional slice verification, full node-completion verification, `/IMPLEMENTATION.md` live-controller synchronisation, exact design-to-code traceability, evidence integrity, context-efficient authority loading and mandatory escalation for material unresolved decisions. None of these creates capability-specific business semantics.

---

## 6. Known Amendment Chains Requiring Scope-Aware Reading

```text
MS-PROT-020
MS-PROT-022
MS-PROT-026
MS-PROT-027
MS-PROT-028
MS-PROT-035
MS-PROT-037
MS-PROT-039
MS-PROT-040
MS-PROT-041
MS-PROT-042
MS-PROT-043
MS-PROT-044
MS-PROT-045
MS-PROT-046
MS-PROT-048
MS-PROT-049
MS-PROT-050
MS-PROT-051
MS-PROT-052
MS-PROT-053
MS-PROT-055
MS-PROT-056
MS-PROT-057
MS-PROT-058
MS-PROT-060
MS-PROT-061
MS-PROT-062
MS-PROT-063
MS-PROT-065
MS-PROT-066
MS-PROT-068
MS-PROT-069
MS-PROT-071
MS-PROT-074
MS-PROT-075
MS-PROT-077
MS-PROT-079
MS-PROT-080
MS-PROT-081
MS-PROT-082
MS-PROT-083
MS-PROT-084
ADR-015
MS-TAS-RECOVERY-001
MS-PROT-085
MS-PROT-086
MS-PROT-087
MS-PROT-092
MS-PROT-093
MS-PROT-094
```

---

## 7. Legacy and Historical Evidence

Prototype, PRD, workflow, unindexed foundation ADR and design-review documents remain evidence but do not override `MS-FUNDAMENTAL-VISION-001`, later accepted semantic/design authority, or indexed TAS/ADR/implementation-governance authority within their respective scopes.

The MS-PROT-001..019 prototype/research stratum is retained under `designs/historical/ms-prot-prototypes/`; it is not current accepted semantic/design authority unless a later accepted authority explicitly incorporates a provision.

MS-PROT-078 readiness material is retained only as historical governance/readiness evidence under `docs/development/`. No current MS-PROT-078 authority artifact is required or indexed.

The earlier proposed MS-PROT-043 v1.1 representation is retained under `designs/historical/proposed/`; composite MS-PROT-043 beginning with accepted v1.2 governs current Enquiry/CustomerContext scope.

The earlier MS-PROT-084 targeted amendment patch is retained under `designs/historical/ms-prot-084/`; MS-PROT-084 v1.1 complete composition plus v1.2 govern current scope.

`docs/foundation/Vision.md` remains historical foundation/product evidence and is superseded within overlapping fundamental product-purpose scope by `MS-FUNDAMENTAL-VISION-001`.

`docs/foundation/adr/business-order-domain-model.md` (ADR-005) remains preserved historical/unindexed evidence subject to MS-PROT-077's narrow supersession described above.

`docs/foundation/adr/quantity-allocation-reservation.md` (ADR-007) remains preserved historical architecture evidence subject to MS-PROT-058 v1.1's narrow supersession described above.

The earlier Draft Conversation/Customer Communication documents under `docs/platform-services/conversation-engine.md`, `docs/data/conversation-lifecycle-model.md`, `docs/business-rules/customer-communication-rules.md`, `docs/workflows/business-operations/customer-communication.md` and `docs/development/PRD/customer-support-and-communication.md` remain historical product evidence. Composite MS-PROT-043, MS-PROT-059, composite MS-PROT-075, composite MS-PROT-085 through v1.4, composite MS-PROT-086 through v1.4 and composite MS-PROT-053 through v1.3 govern their overlapping semantic scope.

The earlier `docs/development/PRD/marketing-and-campaign-engine.md` remains historical product-intent evidence. Composite MS-PROT-087 through v1.4 governs Campaign purpose/revision, the initial Campaign-purpose/outreach portfolio, the initial Audience Definition/attribute portfolio, jurisdiction-qualified direct-email permission/contact policy, durable Marketing suppression, contact pressure, bounded scheduled/periodic automation, campaign-specific recipient eligibility, approval, occurrence, suppression and outcome evidence, plus Marketing commercial-access classification; composite MS-PROT-043, MS-PROT-046, MS-PROT-053, MS-PROT-057, MS-PROT-075, composite MS-PROT-083 through v1.8 and composite MS-PROT-086 retain their independently owned semantics.

The earlier custom-domain and domain-integration intent under `docs/development/PRD/platform-integration.md`, `docs/development/PRD/merchant-journey.md` and `docs/development/PRD/subscription-and-billing.md` remains historical product evidence. MS-PROT-088 v1.0 governs overlapping Merchant Brand Namespace, custom-domain, website-hostname and Business Email Identity semantics while composite MS-PROT-036, MS-PROT-056, MS-PROT-075 and MS-PROT-086 retain their independently owned scopes.

The earlier merchant-web-only delivery assumption in MS-PROT-037 remains historical within its original scope. MS-PROT-093 v1.0 now governs the widened first-party merchant-client delivery portfolio and installed-client architecture while preserving `merchant-web` as first-class. MS-PROT-094 v1.0 later governs merchant presentation consistency and amends MS-PROT-036 within storefront presentation/composition, business-data non-ownership and centrally evolvable delivery scope.

---

## 8. Index Maintenance Invariant

Whenever a new accepted fundamental product-purpose, semantic/design, TAS/ADR, implementation-programme or implementation-governance authority or amendment is formalised, this index MUST be updated in the same governance completion cycle. An accepted authority absent from this index is a governance defect.