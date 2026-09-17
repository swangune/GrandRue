# GrandRue Naming Migration Ledger

**Migration:** `MAIN_STREET_TO_GRANDRUE`  
**Repository:** `swangune/GrandRue`  
**Branch:** `development`  
**Baseline:** `c4153441d8340b229a29884967d796280d949a7d`  
**Status:** `IN_PROGRESS`  
**Authority class:** non-semantic operational migration ledger

This is the canonical resumable execution registry for the Main Street → GrandRue naming migration. It records operational scope and evidence; it does not redefine accepted semantic authority. Detailed file-level evidence is retained in the referenced commits rather than duplicated here.

---

## 1. Migration Contract

This is a **minimum executable migration plus active repository controls and canonical governance**.

A path is a migration target only when:

1. leaving legacy naming unchanged would break or invalidate current GrandRue build/test/package/start/configure/serve coherence;
2. it is an active repository-control/navigation file: `AGENTS.md`, `SEQUENCE.md`, or this ledger; or
3. it is one of the seven canonical current governance files in root `designs/`.

Everything else is reference-only unless concrete dependency evidence proves otherwise.

### Essential executable surfaces

- `src/main/**`
- `src/test/**`
- `pom.xml`
- `.github/workflows/maven-tests.yml`
- `.github/workflows/storefront-web-tests.yml` — essential wiring, no current legacy naming action
- `compose.prototype.yml`
- executable `storefront-web/**`

### Standing control/governance exceptions

- `AGENTS.md`
- `SEQUENCE.md`
- `GRANDRUE-MIGRATION.md`
- `designs/DESIGN-RULES.md`
- `designs/DOCUMENT-GOVERNANCE.md`
- `designs/AUTHORITY-INDEX.md`
- `designs/CANONICAL-SEMANTIC-LEXICON.md`
- `designs/DEFERRED-DECISION-REGISTER.md`
- `designs/DESIGN-CORPUS-CONFORMANCE.md`
- `designs/IMPLEMENTATION-RULES.md`

---

## 2. Execution Constraints

- work only on `development`;
- no branch creation unless explicitly authorised;
- **do not run Maven tests unless explicitly authorised**;
- **do not run GitHub Actions unless explicitly authorised**;
- smallest conforming dependency-bounded change only;
- no semantic redesign or unrelated cleanup;
- production namespace leaves include all production cross-package consumers atomically;
- test namespace/runtime-coupled changes remain deferred to `GR-REN-03`;
- current-product prose/comments in moved production sources remain deferred to `GR-REN-02-02`;
- structural verification must not be represented as Maven/integration/runtime verification.

Mutation is authorised only within the frozen `GR-REN-01F` action map.

---

## 3. Frozen Pre-Mutation Action Map

`GR-REN-01A` through `GR-REN-01F` are complete.

Final action-map evidence: `08dcfda8a8b23bc442c3d63a4754c2ed6b74ab52`.

### Authorised current-identity changes

- production/test Java package roots `mainstreet` → `grandrue`, including package/import/path-sensitive test changes in their later governed group;
- Maven/build/CI/prototype/storefront current product naming → GrandRue equivalents;
- preferred `GRANDRUE_PROTOTYPE_POSTGRES_*` and `GRANDRUE_BACKEND_URL`, retaining frozen legacy external-env fallbacks where required;
- current wording in `AGENTS.md`, `SEQUENCE.md`, and the seven canonical governance files → GrandRue.

### Protected identities — do not mechanically rename

- applied Flyway migration contents;
- `mainstreet_correlation_identifier` / `mainStreetCorrelationIdentity`;
- historical notification failure FQCN evidence;
- Opportunity binding v1 AAD `mainstreet/enquiry/opportunity-binding/v1/`;
- `mainstreet-semantic-bundle-v1/v2`;
- `mainstreet-exposure-definitions-v1`;
- `calendar/current-main-street-commitments`;
- stable `MS-*`, accepted DQ/rule/contract identifiers, and historical evidence.

---

## 4. Mutation Programme

### `GR-REN-02` — production Java namespace migration

State: `OPEN`

`GR-REN-02-01` is a parent group. Each completed leaf below is `COMPLETE_PENDING_FINAL_VERIFICATION`. The referenced code commits are the authoritative file-level evidence.

| Node | State | Production scope | Evidence |
|---|---|---|---|
| `GR-REN-02-01A` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `identitysecurity/**` + bounded consumers | code `6396afba5fdf3e76e2099536d8a3451184650056`; lineage reconciliation `b7149659134714b0baf15899f59ad3f44d1c7014` |
| `GR-REN-02-01B` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `audit/**` + bounded consumers | `fbabeed82d767d2454923f7767f3655ca690642f` |
| `GR-REN-02-01C` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `businesshours/**` + bounded consumer | `8b23ee7ca70e84159d4f9dfbe5bf4e2b06b63516` |
| `GR-REN-02-01D` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `credential/**` + bounded consumer | `bd7df15233f61cc7968447e49bfa1dedb0b5c8fb` |
| `GR-REN-02-01E` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `media/**` + bounded consumer | `d1d9059e071ef6362aefabba0423a6f5298b5cd4` |
| `GR-REN-02-01F` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `protection/**` + bounded consumers | `f7a729aebd9c93a9ecf5f9af7c39bac05331ef5c` |
| `GR-REN-02-01G` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `resilience/**`; no external production consumers | `87a51a1679d13b04d0cc9fde02edb0d24cb91e79` |
| `GR-REN-02-01H` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `observability/**`; no external production consumers | `833fc7428d9e61e03a612d7b99e358112fd660b0` |
| `GR-REN-02-01I` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `privacy/**` + bounded consumer | `f00d5e57ad24b8be80e7cdaf1a3894d614b08903` |
| `GR-REN-02-01J` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `customer/**` + six bounded consumers | code `de95f490823ab080f832c07b9cf8f933b409df84`; boundary correction `a79384f0018e2bd09b5cd28dafe60cc157607017` |
| `GR-REN-02-01K` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `inventory/**` + five bounded consumers | `6b641099a6694647d947339031b8185c6a2cd0a8` |
| `GR-REN-02-01L` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `money/**` + six bounded consumers | `50cc3f9462363b120de8f7e2d47fad60a180487e` |
| `GR-REN-02-01M` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `background/**` + eight bounded consumers | `6a610d767a5d8decfdc839d621127d9a73901490` |
| `GR-REN-02-01N` | `COMPLETE_PENDING_FINAL_VERIFICATION` | root production `GrandRueApplication.java`; no production consumers | `6ba9245b16dc70aa5a9da58d739ce4b422a1e2bb` |
| `GR-REN-02-01O` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `booking/**` + nine bounded production consumers | code `9c36c0720ac5a2b48e22f4f04fc621a8298632c7`; lineage reconciliation `07acfe2a54ac62c856e644146dbadb9aa117b72c` |
| `GR-REN-02-01P` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `ordering/**` + nine bounded production consumers | `390c0713ecdab445b8558a6ed6dec6f65bdb8677` |
| `GR-REN-02-01Q` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `notification/**` + one bounded production consumer | `64ccb3b83fdeca775c226f34aa045f4200517ec1` |
| `GR-REN-02-01R` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `deployment/**` + three bounded production consumers | `f5a5d71ec17a99ed52aae9a76fdc80e54d1c12aa` |
| `GR-REN-02-01S` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `workforce/**` + five bounded production consumers | `cddb54d8c8e76ef8db7208f21dc38f2c2e300c57` |
| `GR-REN-02-01T` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `scheduling/**` + eight bounded production consumers | `11f7b8e3e7706703cadbaa27d257df1ab2752cbb` |
| `GR-REN-02-01U` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `fulfilment/**` + twelve bounded production consumers | `cf0e6497ca9e2e20a7479659458e7441072b1a63` |
| `GR-REN-02-01V` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `merchantaccount/**` + twelve bounded production consumers | `2072066cb8cbb0437725edf9f9747bb77109e9bb` |
| `GR-REN-02-01W` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `api/**` + thirty-two bounded production consumers | `73785a07a4d84cb2cf6c8b9edfce8a2762cf2276` |
| `GR-REN-02-01X1` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/security/session/**`; no external production consumers | `8daa834bfbefb76b500e6b2b9cb1d30b9586f59a` |
| `GR-REN-02-01X2` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/audit/**`; no external production consumers | `3b5a9bb14499698c08cf34d7612fe5c95420b448` |
| `GR-REN-02-01X3` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/media/**`; no external production consumers | `4f1e5b757e035359b6415e636c75a8fc29ce7ef2` |
| `GR-REN-02-01X4` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/event/**`; no external production consumers | `5d9c2efb20a2e63a5390bb0c413e8e6de51c5ff5` |
| `GR-REN-02-01X5` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/privacy/**`; no external production consumers | `d575446357be3bec681206dc7f466844e2692683` |
| `GR-REN-02-01X6` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/enquiry/**`; no external production consumers | code `e55ba415f6ab5132ab257f149ac4c96ab746b0d0`; lineage reconciliation `56b2effe282dbc801924807b52fe47e28f966c3e`; integrity repair `075fe5ae53a0e513960c6965634a5720cd1a23eb` |
| `GR-REN-02-01X7` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/appointment/**` + one bounded production consumer | `71f61cf6809effdae2f0f91e0a396ff5a6853e6f` |
| `GR-REN-02-01X8` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/release/**`; no external production consumers | `1c17d087fc6868ee9fa103592247a10300ee4371` |
| `GR-REN-02-01X9` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/webauthn/**` + one bounded production consumer | `47bc3bd278132ee2405118bbf236fd2c699eda2b` |
| `GR-REN-02-01X10` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/runtime/**`; no external production consumers | `84fc449d47ca886a502573f43224522290cb1e4f` |
| `GR-REN-02-01X11` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/credential/**`; no external production consumers | `f5203adf597f7c43a2f106e58a7c388a7924084f` |
| `GR-REN-02-01X12` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/businesshours/**`; no external production consumers | `3ca2a6b3cb393eddca33c2350275e8abb37e6873` |
| `GR-REN-02-01X13` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/identitysecurity/**`; no external production consumers | `764603c0bddc42663905c866f586494e84d34255` |
| `GR-REN-02-01X14` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/money/**`; no external production consumers | `3167f786b1cd9184d783ddc597a820fb50199505` |
| `GR-REN-02-01X15` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/notification/**`; no external production consumers | `e3337683eb9f18cb32300cc44f5e69ca17f452ea` |
| `GR-REN-02-01X16` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/protection/**`; no external production consumers | `71cf01cbcc01650ca724f75e839b2dbf4fbc2dc6` |
| `GR-REN-02-01X17` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/deployment/**`; no external production consumers | `86ccade5b972d9ff0fc2076d7d4cd58d09159e14` |
| `GR-REN-02-01X18` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/background/**`; no external production consumers | `ae2184f5484108e49b2ef62a2410f920cbdecfca` |
| `GR-REN-02-01X19` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/onboarding/**`; no external production consumers | `d2cd0936f308fbabaef1495f994cbb8fe7e77b00` |
| `GR-REN-02-01X20` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/publication/**`; no external production consumers | `1cd5fec356273b4ea26be2d6625620c89069ef1d` |
| `GR-REN-02-01X21` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/ordering/**` + one bounded production consumer | `cdea37fe0b98d0903547f9d950bf5e3f3f800de0` |
| `GR-REN-02-01X22` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/inventory/**` + two bounded production consumers | `d745fcec0dba3855ee37ada13cf4bd88f75c3a62` |
| `GR-REN-02-01X23` | `COMPLETE_PENDING_FINAL_VERIFICATION` | `infrastructure/persistence/workforce/**`; no external production consumers | `dc052c737676d2b50efc4eb6beade8cdc29d62ce` |
| `GR-REN-02-01X+` | `EXPANSION_REQUIRED` | remaining production package roots; prepare next leaf from live bounded dependency evidence | pending |
| `GR-REN-02-02` | `NOT_STARTED` | current-product comments/wording only in touched production source files | pending |
| `GR-REN-02-03` | `NOT_STARTED` | production namespace structural consistency/residual gate | pending |

### Important lineage and boundary notes

- `GR-REN-02-01X6`: two Enquiry persistence owner files moved from `mainstreet.infrastructure.persistence.enquiry` to `grandrue.infrastructure.persistence.enquiry`. No external production consumers were found; only tests reference the package outside those owner files. Existing application/enquiry/semantic-registry dependencies, SQL/table/column names, request-lock identity, comments, transaction semantics and behaviour were unchanged. Test namespace consumers remain deferred to `GR-REN-03`. Code commit `e55ba415f6ab5132ab257f149ac4c96ab746b0d0` contains exactly two owner renames, each with only a one-line package declaration replacement. Ledger-only commits created during checkpointing were reconciled with the code lineage by merge commit `56b2effe282dbc801924807b52fe47e28f966c3e`; no force update was used. A later lineage reconciliation merge, `e29eec52c6ed3cf79ffa4fd4f85bf7e437bca87d`, retained its first-parent tree and therefore dropped the Enquiry filesystem rename while keeping the migration ancestry. This false-complete state was detected at branch head `2f2ab05dec9590b4c5e7022c63f407fcff9aa90c`. Integrity repair commit `075fe5ae53a0e513960c6965634a5720cd1a23eb` restored exactly the same two owner renames against the live tree; aggregate comparison from `2f2ab05dec9590b4c5e7022c63f407fcff9aa90c` contains only those two renames, and structural verification confirmed the GrandRue paths present and the legacy Enquiry path absent. No Maven tests or GitHub Actions were run.
- `GR-REN-02-01X7`: two Appointment persistence owner files moved from `mainstreet.infrastructure.persistence.appointment` to `grandrue.infrastructure.persistence.appointment`. The sole bounded production consumer, `mainstreet.prototype.PrototypeRuntimeConfiguration`, received only the corresponding import replacement. Existing scheduling/application/semantic dependencies, SQL/table/column names, advisory-lock key construction, comments, transaction semantics and behaviour were unchanged. Test namespace consumers remain deferred to `GR-REN-03`. Code commit `71f61cf6809effdae2f0f91e0a396ff5a6853e6f` contains the two owner renames, their one-line package declaration replacements, and the one production import replacement.
- `GR-REN-02-01X8`: `JooqSemanticReleaseAdmissionAuthority.java` moved from `mainstreet.infrastructure.persistence.release` to `grandrue.infrastructure.persistence.release`. No external production consumers were found; current test consumers remain deferred to `GR-REN-03`. The prepared parent was `fae8822deecf60b8840ef881c20dd43312196235`, the owner input blob was `276b5c64283398bf9a1725ab8448d985b1f0077b`, and the GrandRue destination was absent at preflight. Existing semantic-release imports, SQL/table/column names, `MS-PROT-040` identity/comment, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `1c17d087fc6868ee9fa103592247a10300ee4371` contains exactly one owner rename with only the package declaration replacement.
- `GR-REN-02-01X9`: two WebAuthn persistence owner files moved from `mainstreet.infrastructure.persistence.webauthn` to `grandrue.infrastructure.persistence.webauthn`. The sole bounded production consumer, `mainstreet.infrastructure.security.webauthn.WebAuthnAuthenticationSubjectService`, received only the corresponding `JooqWebAuthnAuthenticationSubjectRepository` import replacement. Current test consumers remain deferred to `GR-REN-03`. The prepared parent was `90da9cce7fb19f610ab4281d11d4173640f96553`; owner input blobs were `b4ad1cfa6f508d583ead3a4f7d8227154ecf79c1` and `dccea9daf84b5720a2d8fa71a94f178a2675fa88`; the consumer input blob was `0e6117910c61376312bfff49ee14b0d00deabdc5`; and the GrandRue destination was absent at preflight. Existing `WebAuthnIdentityReferenceAuthority` usage, WebAuthn table/column identifiers, Main Street prose/comments, Identity/security semantics and persistence behaviour were unchanged. Code commit `47bc3bd278132ee2405118bbf236fd2c699eda2b` contains exactly two owner renames with one package-declaration replacement each and one production import replacement.
- `GR-REN-02-01X10`: `JooqSessionRecordStore.java` moved from `mainstreet.infrastructure.persistence.runtime` to `grandrue.infrastructure.persistence.runtime`. No external production consumers were found; current test consumers remain deferred to `GR-REN-03`. The prepared parent was `6546c10827c8a20ca41b35c95febab0addc5f6ab`, the owner input blob was `9dc6e3f119464b0e6e1599ddb520270206221dfb`, and the GrandRue destination was absent at preflight. Existing runtime interface imports, ADR-014 prose, SQL/table/column identities, session-security semantics and persistence behaviour were unchanged. Code commit `84fc449d47ca886a502573f43224522290cb1e4f` contains exactly one owner rename with only the package declaration replacement.
- `GR-REN-02-01X11`: `JooqCredentialSecurityStore.java` moved from `mainstreet.infrastructure.persistence.credential` to `grandrue.infrastructure.persistence.credential`. No external production consumers were found; the current integration-test consumer remains deferred to `GR-REN-03`. The prepared parent was `2d02d6a4b8393594cecf445352aed5d11e7fad24`, the owner input blob was `b938d47fe00b2280d036ae6a84b47e9c7f588366`, and the GrandRue destination was absent at preflight. Existing `MerchantScope` and credential-domain imports, credential table/column identifiers, advisory-lock key construction, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `f5203adf597f7c43a2f106e58a7c388a7924084f` contains exactly one owner rename with only the package declaration replacement.
- `GR-REN-02-01X12`: `JooqStandardBusinessHoursAuthority.java` moved from `mainstreet.infrastructure.persistence.businesshours` to `grandrue.infrastructure.persistence.businesshours`. No external production consumers were found; current integration-test consumers remain deferred to `GR-REN-03`. The prepared parent was `2f15fc049392d6169577a497b620f035000b9697`, the owner input blob was `219b5f42d3dabc6455f1ec803c1dbf95326ac90a`, and the GrandRue destination was absent at preflight. Existing `MerchantScope`, `TrustedExecutionContext` and Business Hours domain imports, SQL/table/column identities, advisory-lock keys, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `3ca2a6b3cb393eddca33c2350275e8abb37e6873` contains exactly one owner rename with only the package declaration replacement.
- `GR-REN-02-01X13`: `JooqIdentitySecurityGenerationManagement.java` moved from `mainstreet.infrastructure.persistence.identitysecurity` to `grandrue.infrastructure.persistence.identitysecurity`. No external production consumers were found; current integration-test consumers remain deferred to `GR-REN-03`. The prepared parent was `f1cc76a0abdd9bf758401a12696c8251ccf12080`, the owner input blob was `1c56a5bffcc7c49befa3d7bfcc87a5d3926e6099`, and the GrandRue destination was absent at preflight. Existing Audit and Identity Security imports, `SessionRecordStore`, ADR-014 prose, SQL/table/column identities, action and advisory-lock strings, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `764603c0bddc42663905c866f586494e84d34255` contains exactly one owner rename with only the package declaration replacement.
- `GR-REN-02-01X14`: `JooqPaymentAuthorityStore.java` moved from `mainstreet.infrastructure.persistence.money` to `grandrue.infrastructure.persistence.money`. No external production consumers were found; the current integration-test consumer remains deferred to `GR-REN-03`. The prepared parent was `65fac53daa5b7a1aaf395812e9142c839fde99db`, the owner input blob was `2af95437ae66968f36d64c9573d69fdc3c8b7fd1`, and the GrandRue destination was absent at preflight. Existing `MerchantScope` and Money domain imports, payment SQL/table/column identities, protected `mainstreet_correlation_identifier` / `mainStreetCorrelationIdentity`, advisory-lock key construction, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `3167f786b1cd9184d783ddc597a820fb50199505` contains exactly one owner rename with only the package declaration replacement.
- `GR-REN-02-01X15`: `JooqNotificationStore.java` moved from `mainstreet.infrastructure.persistence.notification` to `grandrue.infrastructure.persistence.notification`. No external production consumers were found; the current integration-test consumer remains deferred to `GR-REN-03`. The prepared parent was `452f51f5619015f26116f4bdfebbe59fe8741db8`, the owner input blob was `c02ad96a08b3494c85c97ebce0ba4d7edec230c7`, and the GrandRue destination was absent at preflight. Existing Notification domain imports, notification table/column identities, advisory-lock key construction, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `e3337683eb9f18cb32300cc44f5e69ca17f452ea` contains exactly one owner rename with only the package declaration replacement.
- `GR-REN-02-01X16`: `JooqResourceProtectionAuthority.java` moved from `mainstreet.infrastructure.persistence.protection` to `grandrue.infrastructure.persistence.protection`. No external production consumers were found; the current integration-test consumer remains deferred to `GR-REN-03`. The prepared parent was `f176f13c07671e66354c28151b271c2bfe446879`, the owner input blob was `bc7cb4106617b0de2ed9b235ce404a988666e6a8`, and the GrandRue destination was absent at preflight. Existing Resource Protection domain imports, SQL/table/column identities, advisory-lock key construction, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `71cf01cbcc01650ca724f75e839b2dbf4fbc2dc6` contains exactly one owner rename with only the package declaration replacement.
- `GR-REN-02-01X17`: `JooqOrdinaryServingGenerationPromotionAuthority.java` and `JooqServingDeploymentAdmissionSnapshotAuthority.java` moved from `mainstreet.infrastructure.persistence.deployment` to `grandrue.infrastructure.persistence.deployment`. No external production consumers were found; current integration-test and runtime-fixture consumers remain deferred to `GR-REN-03`. The prepared parent was `0a3cdf141506bf1a67edb5c5638ccec9a41413bb`; owner input blobs were `471963dfd7e68cfd22d1e7b45b7f632aa5f79e74` and `d126ed737d826ee413683f4beb4b98590457e2e4`; both GrandRue destinations were absent at preflight. Existing application, semantic configuration/execution and Deployment domain imports, SQL/table/column identities, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `86ccade5b972d9ff0fc2076d7d4cd58d09159e14` contains exactly two owner renames with only the package declaration replacement in each file.
- `GR-REN-02-01X18`: `JooqDurableWorkStore.java` and `JooqRegisteredDurableWorkClaimer.java` moved from `mainstreet.infrastructure.persistence.background` to `grandrue.infrastructure.persistence.background`. No external production consumers were found; current integration-test consumers remain deferred to `GR-REN-03`. The prepared parent was `5ca111cead78dd1ee3d77fc550185d0b6f713e3b`; owner input blobs were `2f76be924b4cecd7fd5ec06424aab8361a287593` and `39c711f03922245a2275c6cc8430388eaf730b48`; both GrandRue destinations were absent at preflight. Existing `MerchantScope` and Background domain imports, SQL/table/column identities, `background|` advisory-lock key construction, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `ae2184f5484108e49b2ef62a2410f920cbdecfca` contains exactly two owner renames with only the package declaration replacement in each file.
- `GR-REN-02-01X19`: `JooqOnboardingCaseEvidenceStore.java` moved from `mainstreet.infrastructure.persistence.onboarding` to `grandrue.infrastructure.persistence.onboarding`. No external production consumers were found; current integration-test and runtime-fixture consumers remain deferred to `GR-REN-03`. The prepared parent was `e44732213434d26ef6eae30dbd14f211b9132af4`; the owner input blob was `8ea6fdc13c9eb985e1d3e51f6552ced2bc0f2571`, and the GrandRue destination was absent at preflight. Existing `MerchantScope` and Onboarding domain imports, SQL/table/column identities, onboarding advisory-lock strings and seeds, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `d2cd0936f308fbabaef1495f994cbb8fe7e77b00` contains exactly one owner rename with only the package declaration replacement.
- `GR-REN-02-01X20`: `JooqOpportunityPublicationApplicationService.java` and `JooqOpportunityPublicationStateAuthority.java` moved from `mainstreet.infrastructure.persistence.publication` to `grandrue.infrastructure.persistence.publication`. No external production consumers were found; current test consumers remain deferred to `GR-REN-03`. The prepared parent was `33a508bdcf609c94ec43116fb1edb6546dc911e4`; owner input blobs were `2645df38cdb8c263a2d15393cb64d57bce03c1ee` and `484d124b22a8404031ab07ca253e31b6198ec03f`; both GrandRue destinations were absent at preflight. Existing application and Publication domain imports, SQL/table/column identities, `opportunity-publication-application|` advisory-lock key construction, material schema/field identifiers and encodings, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `1cd5fec356273b4ea26be2d6625620c89069ef1d` contains exactly two owner renames with only the package declaration replacement in each file.
- `GR-REN-02-01X21`: `JooqOrderingTransaction.java` and `JooqOrderingUnitOfWork.java` moved from `mainstreet.infrastructure.persistence.ordering` to `grandrue.infrastructure.persistence.ordering`. The sole bounded production consumer, `mainstreet.prototype.PrototypeRuntimeConfiguration`, received only the corresponding `JooqOrderingUnitOfWork` import replacement. Current test consumers remain deferred to `GR-REN-03`. The prepared parent was `9c59bbf05aa6548b2c7e76f4d6573d879009db69`; owner input blobs were `ea99de35adbc77c7657a4bb23a2c295c090ec9f0` and `ef1f99c0f165920dac5e7bd226181b3d72b38f2e`; the consumer input blob was `d94f4fb4aed7cdaa27ea2a0eeb540fa089f0823e`; both GrandRue destinations were absent at preflight. Existing `JooqQuantityAllocationAuthority` dependency, Ordering domain imports, SQL/table/column identities, `|ordering-command|` advisory-lock key construction, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `cdea37fe0b98d0903547f9d950bf5e3f3f800de0` contains exactly two owner renames with one package-declaration replacement each and one production import replacement.
- `GR-REN-02-01X22`: `JooqQuantityAllocationAuthority.java` moved from `mainstreet.infrastructure.persistence.inventory` to `grandrue.infrastructure.persistence.inventory`. The two bounded production consumers, `grandrue.infrastructure.persistence.ordering.JooqOrderingTransaction` and `grandrue.infrastructure.persistence.ordering.JooqOrderingUnitOfWork`, received only the corresponding `JooqQuantityAllocationAuthority` import replacement. Current test consumers remain deferred to `GR-REN-03`. The prepared parent was `7bca3728a08f0ff760238bf8219a959fa4d7ac27`; the owner input blob was `9b7601994ecedc53e7af7571732c54ef01d9a119`; consumer input blobs were `709075944a925cb71a4229602c2efc24ce438779` and `b26c85c9d3cda1fd3ce259723add5ebe060996a5`; the GrandRue destination was absent at preflight. Existing `MerchantScope`, Inventory and allocation-domain imports, SQL/table/column identities, row-locking and stock arithmetic, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `d745fcec0dba3855ee37ada13cf4bd88f75c3a62` contains exactly one owner rename with one package-declaration replacement and two production import replacements.
- `GR-REN-02-01X23`: `JooqMerchantOperationalDeviceAuthorisationStore.java`, `JooqMerchantWorkforceAuthority.java` and `JooqMerchantWorkforceStore.java` moved from `mainstreet.infrastructure.persistence.workforce` to `grandrue.infrastructure.persistence.workforce`. No external production consumers were found; current integration-test consumers remain deferred to `GR-REN-03`. The prepared parent was `a96bd80aad217253b54d85e12de79fd717f216eb`; owner input blobs were `197b5c1f698cacb25331eab1d50f1559b697b584`, `8a7a4a1e6416ca1c867f95444e9b1b0ded0e9b8e` and `f2d6fd1008e691cb28507e3749a13d638c8747ac`; the GrandRue destination directory was absent at preflight. Existing `MerchantScope`, trusted-device, runtime/security and Workforce domain imports, SQL/table/column identities, controller-authority revalidation, advisory-lock construction, membership/role lifecycle logic, comments, transaction/concurrency behaviour and persistence semantics were unchanged. Code commit `dc052c737676d2b50efc4eb6beade8cdc29d62ce` contains exactly three owner renames with only the package declaration replacement in each file.

### Ledger integrity repair

Checkpoint commit `ee7b9c659b8038cd8ef14af9c80ac29102322516` correctly recorded the `GR-REN-02-01N` table entry but accidentally truncated later portions of this non-semantic ledger during file replacement. No production source was affected. Repair commit `abda517cd8f42a197c95af8adec55d1843576ea1` reconstructed the canonical operational record from the last intact checkpoint and retained commit evidence, while compacting repeated file-level detail into commit references.

During `GR-REN-02-01U` staging, accidental connector commit `749af81b99b0a788c09653ff390138a51fe89901` created an empty root `__nonexistent__` placeholder. Repair commit `f254be429c14701e16249a8de5ce7360be46466e` immediately removed it. Comparison from Scheduling checkpoint `e20f47166f836723014fc405f73c50cdebafc6a9` to the repair commit has zero changed files. The verified Fulfilment code tree was then re-parented onto the repaired HEAD without force-updating the branch.

During `GR-REN-02-01X4` preparation, an unintended connector call created branch `__invalid_should_not_create` at checkpoint `4308f6aeaa440e30a163dd660821ee6f5aa11f9c`. The branch contained no unique migration work and did not alter `development`. The user explicitly deferred cleanup and authorised continued migration on `development`; this branch is therefore a recorded non-blocking cleanup item and must not be used for migration work.

### Remaining programme

- `GR-REN-03` — test namespace/runtime-coupled fixtures: `NOT_STARTED`
- `GR-REN-04` — essential build/CI naming: `NOT_STARTED`
- `GR-REN-05` — essential runtime/config naming: `NOT_STARTED`
- `GR-REN-06` — compatibility aliases/preserved identities: `NOT_STARTED`
- `GR-REN-07` — active controls/canonical governance wording: `NOT_STARTED`
- `GR-REN-08..11` — residual audit, falsification, structural/final verification: `NOT_STARTED`

---

## 5. Checkpoint

```yaml
migration: MAIN_STREET_TO_GRANDRUE
repository: swangune/GrandRue
branch: development
baseline: c4153441d8340b229a29884967d796280d949a7d
status: IN_PROGRESS
mutation_authorised: true
active_group: GR-REN-02
selected_execution_leaf: GR-REN-02-01X23
last_completed_task: GR-REN-02-01X23
last_task_commit: dc052c737676d2b50efc4eb6beade8cdc29d62ce
last_integrity_repair: GR-REN-02-01X6
last_integrity_repair_commit: 075fe5ae53a0e513960c6965634a5720cd1a23eb
last_verified_head: 075fe5ae53a0e513960c6965634a5720cd1a23eb
next_action: Prepare the next exact bounded production namespace packet under GR-REN-02-01X+ from live dependency evidence, establish freshness, and execute only after it is READY. Keep test namespace changes for GR-REN-03. The stray branch cleanup is deferred and non-blocking. Do not run Maven tests or GitHub Actions.
```

---

## 6. Restart and Verification

1. inspect `AGENTS.md`, this ledger and `docs/development/grandrue-migration-work.md`;
2. inspect current `development` HEAD;
3. reconcile branch HEAD against the checkpoint and any ledger-only checkpoint commit;
4. prepare the smallest conforming production namespace packet from live dependency evidence;
5. execute only a fresh `READY` packet, moving the owner package and all declared production cross-package consumers atomically;
6. structurally verify aggregate diff, new-path presence, old-path absence, declared consumer imports, protected identities and expected residuals;
7. checkpoint the completed leaf here;
8. preserve the frozen action map and protected identities;
9. keep `__invalid_should_not_create` isolated until manually removed.

Until separately authorised:

```text
DO NOT run Maven tests
DO NOT run GitHub Actions
```

Structural verification must not be represented as passed Maven/integration/runtime verification.

---

## 7. Prepared Work Protocol

For migration work after adoption of this section, `docs/development/grandrue-migration-work.md` is the subordinate operational preparation/execution aid for this ledger.

This addition does not alter the historical meaning of sections 1–6, completed leaf records, commit evidence, protected identities, repair notes, or the frozen action map. It changes how the open-ended `GR-REN-02-01X+` continuation is prepared and executed so that repeated discovery is removed from bounded execution.

### Governing relationship

- this ledger remains the canonical resumable migration registry and completion/checkpoint history;
- `docs/development/grandrue-migration-work.md` holds preparation procedure, coverage accounting, the small active queue and the exact current work packet;
- the work file is not semantic/design authority and must not become a second historical ledger;
- completed packet history is checkpointed here, with detailed file evidence retained in commits;
- `AGENTS.md` mandatory widening, stop conditions and `DESIGN_ESCALATION` remain fully applicable.

### Procedural interpretation of the existing checkpoint

For future work, `next_action: Select and execute ...` in section 5 and selection in section 6 are decomposed into two distinct stages:

1. **prepare** — inspect live dependency evidence, close the coherent boundary, resolve protected identities and exact verification, account for discovered coverage, and write a complete work packet; then
2. **execute** — only after freshness/preflight establishes packet state `READY`, apply the exact packet without rediscovering or broadening its scope.

A bounded executor must never perform leaf selection or resolve an unresolved migration/design decision during execution.

### Packet states

```text
PREPARATION_REQUIRED -> PREPARED -> READY -> IN_PROGRESS -> ledger checkpoint
```

Only `READY` is executable. Any stale input, unexpected dependency, additional changed path, unmatched exact edit, protected-identity consequence, authority gap, repository contradiction or other undeclared judgement invalidates execution and returns the packet to preparation or the applicable `DESIGN_ESCALATION` path.

### Coverage invariant

Every discovered migration unit must receive exactly one disposition. Final coverage/residual verification may not treat the migration as fully enumerated until the work file records enumeration complete and no discovered item remains unclassified, prepared, ready, in progress or unresolved/blocked.

At protocol adoption, no new production leaf is selected. `GR-REN-02-01X+` remains `PREPARATION_REQUIRED`; its remaining dependency graph and coverage must be enumerated from current repository evidence rather than guessed.

### Preservation and lineage

Prepared execution must preserve document history, source content outside declared transformations, protected identities, runtime/data boundaries and Git lineage. If a separate ledger receipt is required, preserve:

```text
verified parent -> code commit -> ledger receipt commit
```

Unexpected branch movement is a stop condition and never authorises force-push, overwrite or improvised reconciliation.

### Current procedural checkpoint

```yaml
prepared_work_protocol: docs/development/grandrue-migration-work.md
prepared_work_state: PREPARATION_REQUIRED
prepared_work_node: GR-REN-02-01X+
ready_packet: null
last_prepared_execution_leaf: GR-REN-02-01X23
last_prepared_execution_commit: dc052c737676d2b50efc4eb6beade8cdc29d62ce
post_adoption_integrity_repair_leaf: GR-REN-02-01X6
post_adoption_integrity_repair_commit: 075fe5ae53a0e513960c6965634a5720cd1a23eb
protocol_adoption_parent: d3e20efdfa3acda54ed511e8c2f2374d3ae2d2ce
next_procedural_action: Prepare the next exact bounded packet from current live production dependency evidence, update coverage, and establish freshness before marking it READY. Do not mutate production code before READY. Keep test namespace changes for GR-REN-03. Do not run Maven tests or GitHub Actions unless separately authorised.
```
