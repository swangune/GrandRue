# MS-PROT-048 v1.4 — Provider Connection, Operational Readiness & Runtime Fulfilment Admission Amendment

**Document ID:** MS-PROT-048  
**Version:** 1.4  
**Status:** **ACCEPTED by manual approval on 27 August 2026**  
**Amends:** MS-PROT-048 v1.0–v1.3 within ProviderConnection, operational readiness and runtime fulfilment admission  
**Depends on:** MS-PROT-023; MS-PROT-024; MS-PROT-025; MS-PROT-028 v1.3; MS-PROT-048 v1.0–v1.3; MS-PROT-049 v1.1; MS-PROT-062; MS-PROT-063; MS-PROT-065; MS-PROT-067; MS-PROT-068; MS-PROT-069; MS-PROT-070; MS-PROT-079  
**Closes:** MS-PROT-079 Target 9 — Provider readiness  
**Approved:** Manual approval on 27 August 2026 after in-chat authority trace, implementation-evidence review, falsification, corpus-conformance review and recommendation  
**Purpose:** Define ProviderConnection identity and the production runtime authority that determines whether an already-selected fulfiller can currently satisfy the exact applicable fulfilment responsibility without allowing provider state, credentials, health, callbacks, outages or AI inference to redefine Main Street business semantics.

---

# 1. Governing decision

Main Street SHALL distinguish four provider-related questions:

```text
FULFILMENT REQUIREMENT
    What technical responsibility is required?

FULFILMENT BINDING
    Who is selected to discharge it?

PROVIDER CONNECTION
    Which authorised external account/context
    may that selected provider use?

PROVIDER READINESS
    Can that exact selected fulfilment path
    currently satisfy the required obligation?
```

Therefore:

```text
Requirement
    ≠ Binding
    ≠ ProviderConnection
    ≠ Provider Readiness
```

MS-PROT-048 already establishes the first two distinctions.

Target 9 establishes the production runtime contract for the latter two.

---

# 2. Provider Readiness definition

**Provider Readiness** means:

> **A current, scope-qualified runtime determination of whether an already-selected fulfiller has sufficient current technical conditions to attempt the exact applicable fulfilment responsibility safely.**

Canonical:

```text
Resolved Fulfilment Binding
+
exact applicable obligations
+
current ProviderConnection where required
+
current credential/security usability
+
current provider/dependency evidence
+
current governing resilience restrictions
        ↓
PROVIDER READINESS RESOLUTION
```

Readiness SHALL NOT determine whether the capability or Fulfilment Role should exist.

---

# 3. Initial readiness algebra

The generic runtime readiness result SHALL be:

```text
READY
DEGRADED
NOT_READY
UNKNOWN
```

These are operational fulfilment conditions.

They are not business lifecycle states.

### READY

Current evidence establishes that the exact fulfilment path may normally be attempted.

### DEGRADED

The path remains usable, but current accepted constraints indicate reduced quality, reduced functionality or another explicit degraded execution condition.

### NOT_READY

Current evidence establishes that the path must not currently be used for the relevant execution.

### UNKNOWN

Main Street cannot establish enough current evidence to claim readiness.

The distinction between `NOT_READY` and `UNKNOWN` is operationally important.

For execution admission:

```text
READY
    → may proceed to remaining runtime checks

DEGRADED
    → may proceed only where the owning contract permits degraded execution

NOT_READY
    → do not initiate dependent execution

UNKNOWN
    → do not initiate dependent execution
```

Thus runtime execution is fail-closed.

---

# 4. Readiness is obligation-qualified

Readiness MUST NOT be resolved merely as:

```text
Stripe = READY
Google = READY
Calendly = READY
```

It is qualified by the responsibility being attempted.

Canonical key:

```text
Merchant Scope
+
Resolved Fulfilment Binding
+
Fulfilment Role
+
semantic context
+
required obligation subset
```

A provider may be ready for one obligation and not another.

Example:

```text
notification provider
    SEND_EMAIL          READY
    SEND_SMS            NOT_READY
```

A generic provider-wide `healthy=true` cannot override this.

---

# 5. Provider-wide health is evidence, not connection readiness

MS-PROT-068 already distinguishes:

```text
Provider Health
≠
ProviderConnection Readiness
```

Target 9 preserves this.

Example:

```text
Google Calendar platform health = READY

Merchant M1 OAuth grant = expired
    → M1 external-calendar readiness = NOT_READY

Merchant M2 connection valid
    → M2 external-calendar readiness may remain READY
```

Conversely:

```text
merchant credentials valid
+
provider-wide outage
    → path may be NOT_READY
```

No evidence source owns the final result by itself.

---

# 6. ProviderConnection

A `ProviderConnection` is:

> **A stable Main Street operational identity representing one authorised relationship between an applicable Main Street scope/responsibility and an external provider account/context.**

Conceptually:

```text
ProviderConnection
{
    connectionIdentity

    providerIdentifier

    ownerScope
        merchant scope
        or platform responsibility

    externalAccountReference?

    authorisationEvidence

    grantedScopeEvidence

    credentialBindingReference?

    callback/integration registration evidence?

    lifecycle / connection condition

    operational provenance
}
```

This is conceptual and does not mandate one Java aggregate.

---

# 7. ProviderConnection identity is stable

The identity SHALL NOT be:

```text
OAuth access token
refresh token
API key
provider username
secret-store path
webhook signing secret
```

Credential rotation therefore does not create a new ProviderConnection merely because security material changed.

MS-PROT-067 already requires this distinction.

---

# 8. Merchant-scoped and platform-scoped connections

Main Street SHALL support both:

```text
MERCHANT-SCOPED CONNECTION

Merchant M
    → Google Calendar account
```

and:

```text
PLATFORM-SCOPED PROVIDER RESPONSIBILITY

Main Street
    → email delivery provider
```

A platform provider SHALL NOT require creation of a fake Merchant Scope.

This composes directly with MS-PROT-067's platform-credential rule.

---

# 9. Connection existence is not readiness

The following inference is prohibited:

```text
ProviderConnection exists
    → READY
```

A connection may exist while:

```text
reauthorisation required
credential revoked
required provider scope removed
external account disabled
callback registration broken
secret store unavailable
provider unavailable
```

Therefore connection identity survives many readiness failures.

---

# 10. Connection lifecycle and readiness are separate

Main Street SHALL NOT use one overloaded status such as:

```text
CONNECTED
DISCONNECTED
```

to answer every operational question.

The connection lifecycle may establish whether the relationship still exists, while readiness answers whether it is usable for the current responsibility.

For example:

```text
ProviderConnection exists
+
reauthorisation required
        ↓
connection retained
readiness = NOT_READY
```

The exact persisted lifecycle vocabulary is deferred.

---

# 11. Required connection evidence

For an external-provider binding that requires a merchant/provider connection, readiness SHALL establish the current connection evidence required by the registered provider contract.

This may include:

```text
connection exists

connection belongs to expected Merchant Scope

connection provider matches selected ProviderDefinition

external account/context remains authorised

required provider scopes remain granted

required credential capability is currently usable

required callback/integration registration exists where operationally necessary
```

Not every provider requires every item.

The provider contract determines applicability.

---

# 12. No generic mandatory OAuth model

Main Street SHALL NOT assume all providers use:

```text
OAuth
webhooks
API keys
long-lived connections
```

A provider may instead use:

```text
platform credential
merchant OAuth
merchant API credential
ephemeral delegated authorisation
provider-generated connection context
no merchant-specific connection at all
```

The readiness architecture must support these without provider-specific branches in domain semantics.

---

# 13. Credential usability feeds readiness

Credential security may establish operational evidence such as:

```text
credential usable
expired
revoked
compromised
unavailable
insufficient scope
secret store unavailable
```

MS-PROT-067 already defines these as security/operational classifications rather than business states.

Readiness MAY consume them.

Credential Security does not own the final readiness verdict.

---

# 14. Credential presence is insufficient

Rejected:

```text
token exists
    → READY
```

Accepted:

```text
current authorised credential generation
+
connection still authorised
+
required scope established
+
provider/dependency conditions acceptable
        ↓
readiness may be READY
```

---

# 15. Health evidence freshness

Provider Readiness is a current runtime decision.

Evidence used to establish it SHALL have an explicit validity interpretation.

Main Street SHALL NOT assume:

```text
health observed once
    → READY forever
```

Nor shall Target 9 impose one universal TTL.

Different evidence may have different freshness semantics.

Examples:

```text
OAuth grant status
provider-wide incident state
credential usability
connection validation result
callback-registration state
recent provider response
```

Exact timings are implementation/provider-contract decisions.

---

# 16. UNKNOWN is not READY

If Main Street cannot establish the required evidence:

```text
readiness = UNKNOWN
```

For new provider-dependent execution:

```text
UNKNOWN
    → admission denied
```

This is deliberately conservative.

It prevents:

```text
telemetry unavailable
    → assume provider healthy
```

---

# 17. Observability is not the readiness owner

Logs, metrics, traces and health observations may contribute evidence.

But:

```text
HealthSignal
≠ Provider Readiness
```

MS-PROT-068 already prohibits operational telemetry from becoming mutation/execution authority.

Canonical:

```text
observations
+
connection/security evidence
+
registered provider contract
+
resilience state
        ↓
Provider Readiness Resolver
```

---

# 18. Circuit breaker is not readiness authority

Circuit state MAY contribute execution restriction evidence.

But MS-PROT-070 already states:

```text
CircuitState
≠ ProviderConnection state
≠ Fulfilment Binding state
≠ capability state
```

Target 9 extends the same separation:

```text
OPEN circuit
    may make current path NOT_READY

but

circuit OPEN
    does not rewrite ProviderConnection
```

---

# 19. Readiness is not execution success prediction

`READY` means:

> sufficient evidence exists to attempt the path under current rules.

It does **not** mean:

```text
provider call will succeed
provider will accept request
business operation will complete
network will remain available
```

Execution outcome remains independently determined.

---

# 20. Readiness is revalidated at execution

A surface may have displayed:

```text
AVAILABLE
```

based on a previous readiness projection.

Before actual provider-dependent execution, runtime SHALL resolve/revalidate applicable current readiness again.

Therefore:

```text
surface availability evidence
≠ execution admission authority
```

This preserves MS-PROT-049 v1.1.

---

# 21. Surface mapping

MS-PROT-049 currently uses:

```text
AVAILABLE
DEGRADED
UNAVAILABLE
```

for interaction presentation.

Target 9 does not replace that algebra.

Conceptually:

```text
Provider readiness READY
    → dependent interaction may be AVAILABLE

Provider readiness DEGRADED
    → dependent interaction may be DEGRADED

Provider readiness NOT_READY
    → dependent interaction UNAVAILABLE

Provider readiness UNKNOWN
    → dependent interaction UNAVAILABLE
```

subject to all other surface eligibility/authority rules.

The mapping is one-way evidence consumption.

Surface does not own Provider Readiness.

---

# 22. FulfilmentPlan remains static

Current `FulfilmentPlanResolver` correctly excludes:

```text
provider health
credentials
live ProviderConnection state
```

from static plan resolution.

That remains correct.

Canonical:

```text
Resolved Configuration Package
        ↓
static FulfilmentPlan
        ↓
runtime Provider Readiness
        ↓
provider-dependent execution
```

Provider readiness changes do not create Configuration Revisions or new Fulfilment Binding Set Revisions.

---

# 23. ProviderConnection must match binding

Where a binding carries:

```text
providerConnectionIdentity
```

runtime SHALL establish that the referenced current ProviderConnection:

```text
exists
belongs to the expected scope
targets the selected provider
is valid for the binding's intended provider/account context
```

A connection belonging to another merchant or provider SHALL fail closed.

The current static implementation already preserves the connection identity in `ResolvedFulfilmentBinding`.

---

# 24. No connection substitution

If:

```text
binding → Connection C1
```

Main Street SHALL NOT silently use:

```text
Connection C2
```

merely because C1 is unavailable.

Using another connection may change:

```text
external account
merchant consent
data destination
financial destination
calendar ownership
provider-side authority
```

Therefore it is a material routing decision.

Fallback must be independently authorised.

---

# 25. No provider substitution

Likewise:

```text
Provider A NOT_READY
```

does not permit:

```text
use Provider B
```

unless an accepted fallback binding/path already exists.

This preserves MS-PROT-048 and MS-PROT-070.

---

# 26. Existing obligations versus new activity

Provider readiness for **new activity** may differ from the ability to perform bounded reconciliation/completion for an existing obligation.

Example:

```text
merchant disconnects provider

new provider operations
    → NOT_READY

authorised reconciliation of already-uncertain payment
    → may remain permitted under explicit residual contract
```

Credential Security already permits this distinction when expressly authorised.

Therefore readiness must be evaluated for the **specific intended responsibility**, not as a universal account switch.

---

# 27. Callback reception is separate

Incoming provider callbacks do not require the same readiness condition as initiating a new outbound provider operation.

Example:

```text
merchant's outbound connection requires reauthorisation
```

does not necessarily justify rejecting an authenticated callback concerning a previously accepted transaction.

Callback processing SHALL instead establish:

```text
authentic provider source
+
correct ProviderConnection / provider context
+
correlation
+
applicable existing obligation/process authority
```

then invoke capability-owned processing.

---

# 28. Callback authentication does not imply business truth

A valid webhook signature proves trusted origin/integrity within its contract.

It does not prove:

```text
payment settled
appointment valid
shipment delivered
refund complete
```

Provider evidence must still be interpreted under its registered provider/evidence contract.

MS-PROT-048 already governs this.

---

# 29. Provider evidence does not directly mutate business state

Canonical:

```text
provider evidence
    ↓
verify origin/integrity
    ↓
resolve provider + connection + correlation
    ↓
registered provider interpretation
    ↓
capability/process validation
    ↓
capability-owned mutation
```

Never:

```text
provider JSON
    ↓
database update
```

---

# 30. Execution uncertainty is separate from readiness

Suppose:

```text
Provider Readiness = READY
provider request sent
timeout occurs
```

The readiness decision does not retroactively change to:

```text
NOT_READY = operation failed
```

Instead MS-PROT-069 governs:

```text
EXECUTION_UNCERTAIN
```

where the provider may already have acted.

Readiness governs whether an attempt may begin.

Execution certainty governs what can be concluded after the attempt.

---

# 31. Readiness degradation after acceptance

A provider becoming NOT_READY after Main Street has durably accepted work SHALL NOT erase the accepted obligation.

Depending on the owning process:

```text
accepted work
    → pending
    → retry later if safe
    → reconcile
    → require intervention
```

The provider outage does not rewrite business history.

---

# 32. Readiness resolution responsibility

Main Street SHALL have one logical **Provider Readiness Resolution responsibility**.

It owns:

```text
runtime readiness evaluation
connection/provider scope validation
required-evidence composition
operational readiness verdict
```

It SHALL NOT own:

```text
capability semantics
Fulfilment Requirement
Fulfilment Binding
ProviderDefinition
credential lifecycle
provider health observations
retry policy
resilience mechanism
business execution outcome
```

This logical responsibility does not imply a microservice.

---

# 33. Provider-specific evidence adapters

Provider-specific APIs inevitably differ.

The adapter boundary MAY produce bounded provider-neutral readiness evidence such as:

```text
authorisation valid
required scope present
account reachable
required operation supported now
reauthorisation required
external account disabled
provider rejected access
```

Provider-specific raw states SHALL NOT escape as universal business semantics.

---

# 34. Do not over-normalise provider failures

Main Street must still preserve enough diagnostic classification to distinguish recovery paths.

For example:

```text
REAUTHORIZATION_REQUIRED
INSUFFICIENT_PROVIDER_SCOPE
CREDENTIAL_UNAVAILABLE
PROVIDER_UNAVAILABLE
EXTERNAL_ACCOUNT_DISABLED
CONNECTION_INVALID
READINESS_UNKNOWN
```

may be useful operational evidence.

They need not become one mandatory universal enum.

Safe merchant presentation may collapse several classifications where appropriate.

---

# 35. Connection mutations are operational/integration changes

Operations such as:

```text
connect
reauthorise
disconnect
replace external account
refresh provider grants
repair callback registration
```

do not change capability semantics by themselves.

But they are authoritative changes to integration state and SHALL use explicit application contracts.

No provider adapter may change connection identity/state merely as an undocumented side effect of an unrelated domain operation.

---

# 36. Disconnect does not deactivate capability

Canonical:

```text
merchant disconnects external calendar
        ↓
ProviderConnection no longer usable
        ↓
external-calendar readiness NOT_READY
        ↓
Scheduling capability remains active
```

What happens next depends on accepted binding/fallback semantics.

Disconnecting a provider is not equivalent to:

```text
disable Scheduling
```

---

# 37. Provider registration versus runtime connection

A `ProviderDefinition` is release-affined registered platform knowledge.

A `ProviderConnection` is live operational state.

Therefore:

```text
ProviderDefinition
    immutable registered contract

ProviderConnection
    mutable operational integration identity/state
```

A connection SHALL reference a provider definition identity.

It SHALL NOT contain a private redefinition of the provider contract.

---

# 38. Release affinity

Provider compatibility remains established against the exact provider/fulfilment contracts affined to the governing semantic release.

Runtime connection/readiness evidence does not silently replace those definitions with today's latest provider contract.

Where an already-accepted execution needs historical interpretation, the governing binding/provider-contract provenance must remain resolvable.

---

# 39. Provider contract evolution

Provider API evolution may alter:

```text
adapter implementation
connection repair needs
credential requirements
operational readiness
```

without automatically changing Main Street semantic meaning.

But if the provider no longer satisfies a registered obligation required by the current binding:

```text
binding compatibility is no longer supportable
```

Main Street SHALL NOT pretend readiness merely because the API remains reachable.

---

# 40. Static compatibility versus runtime readiness

These are distinct.

```text
STATIC COMPATIBILITY

Provider X contract supports:
    CREATE_APPOINTMENT
    CANCEL_APPOINTMENT

Merchant requires:
    CREATE_APPOINTMENT

→ statically compatible
```

But runtime:

```text
merchant token expired
→ NOT_READY
```

Conversely:

```text
token valid
```

cannot rescue a provider that is statically incompatible with the required obligation.

---

# 41. Provider readiness does not belong in RCP

The immutable Resolved Configuration Package MAY retain:

```text
provider definition identity
Fulfilment Binding
ProviderConnection identity reference
required obligations
provider-contract provenance
```

where appropriate.

It SHALL NOT retain as current truth:

```text
provider currently healthy
credential currently valid
connection currently reachable
circuit currently closed
provider currently ready
```

---

# 42. Readiness caching

Target 9 SHALL NOT require Redis, a Provider Readiness database or durable precomputed readiness projection.

Initial implementation MAY resolve readiness from current bounded evidence.

A cache is justified only if later performance/operational evidence requires it.

Cached readiness must never outrank security revocation or current connection invalidation.

---

# 43. No mandatory polling framework

Providers differ materially.

Readiness evidence may arise from:

```text
outbound validation
provider API response
credential state
OAuth/grant state
provider status evidence
callback evidence
periodic health check
recent execution evidence
```

Target 9 SHALL NOT require all providers to support polling.

---

# 44. No destructive health checks

Readiness checks SHALL NOT create real business side effects merely to determine provider readiness.

This follows MS-PROT-068's non-destructive health-check rule.

Rejected:

```text
create £1 payment to test Stripe
create appointment to test Calendly
send customer SMS to test notification provider
```

---

# 45. Data minimisation

Readiness evaluation SHALL consume the minimum data required.

Generic Provider Readiness code SHALL NOT receive raw credential material where a credential capability/reference is sufficient.

AI SHALL NOT receive provider credentials or unrestricted provider connection data.

---

# 46. Merchant-facing status

Merchants may receive safe actionable projections such as:

```text
Connected

Needs reconnecting

Temporarily unavailable

Some functions are limited
```

rather than:

```text
HTTP 401
OAuth refresh_token invalid_grant
CircuitBreaker OPEN
secret store timeout
```

Presentation does not become the authoritative connection/readiness state.

---

# 47. Provider Assistance AI boundary

If a future Provider & Integration Assistant is introduced, it MAY:

```text
explain connection state
identify likely repair action
guide reconnect
interpret safe diagnostics
propose connection operation
```

It SHALL NOT:

```text
invent provider readiness
change Fulfilment Binding
access raw credentials
claim provider operation succeeded without evidence
perform unapproved material connection changes
```

This remains optional assistance under MS-PROT-057.

---

# 48. Initial runtime execution composition

For a provider-dependent operation:

```text
current trusted execution context
+
semantic applicability
+
commercial entitlement where applicable
+
actor authorisation
+
operational eligibility
+
resource protection
+
exact Fulfilment Plan
+
Provider Readiness
        ↓
capability/provider execution
```

No universal `EligibilityEngine` is implied.

The applicable dimensions remain independently owned.

---

# 49. Internal fulfillers

Internal fulfiller readiness also requires current technical conditions where failure is possible.

But Target 9 SHALL NOT manufacture `ProviderConnection` objects for internal Main Street implementations.

Conceptually:

```text
INTERNAL fulfiller
    → internal dependency/readiness evidence where applicable

EXTERNAL_PROVIDER fulfiller
    → ProviderConnection + external provider readiness evidence where applicable
```

Both satisfy the same Fulfilment Role obligations.

---

# 50. Falsification — expired OAuth

```text
Scheduling active
binding = Google Calendar
ProviderConnection exists
OAuth credential expired
```

Expected:

```text
Scheduling remains active
binding unchanged
connection identity retained
external-calendar readiness = NOT_READY
repair/reconnect may be offered
```

**PASS**

---

# 51. Falsification — provider-wide outage

```text
ProviderConnection valid
credentials valid
Google Calendar outage
```

Expected:

```text
readiness = NOT_READY or DEGRADED
depending on registered operational evidence

other Main Street Scheduling work continues
```

**PASS**

---

# 52. Falsification — telemetry backend failure

```text
provider actually usable
observability exporter unavailable
```

Generic telemetry failure alone does not rewrite the provider connection.

If required readiness evidence remains independently available:

```text
READY may still be established
```

If required evidence cannot be established:

```text
UNKNOWN
```

Not:

```text
assume READY
```

**PASS**

---

# 53. Falsification — one merchant OAuth failure

```text
Merchant A token revoked
Merchant B token valid
```

Expected:

```text
A readiness NOT_READY
B unaffected
provider-wide health unchanged
```

**PASS**

---

# 54. Falsification — valid token, wrong scope

```text
credential valid
required calendar write scope absent
```

Expected:

```text
readiness for WRITE_CALENDAR = NOT_READY
```

A valid credential does not imply sufficient authority.

**PASS**

---

# 55. Falsification — read works, write does not

```text
provider account permits reads
write grant revoked
```

Expected:

```text
READ obligation  READY
WRITE obligation NOT_READY
```

A provider-global boolean is insufficient.

**PASS**

---

# 56. Falsification — connection substitution

```text
binding references merchant Google account A
A needs reconnecting

merchant also has account B connected
```

Expected:

```text
do not silently use B
```

**PASS**

---

# 57. Falsification — fallback provider exists

```text
Provider A unavailable
Provider B statically compatible
```

but no accepted fallback path exists.

Expected:

```text
no automatic switch
```

**PASS**

---

# 58. Falsification — side-effect timeout

```text
readiness READY
payment request sent
network timeout
```

Expected:

```text
execution certainty handled by MS-PROT-069
possibly EXECUTION_UNCERTAIN

not:
readiness NOT_READY therefore payment failed
```

**PASS**

---

# 59. Falsification — disconnect after historical payment

Merchant disconnects provider after an already-recorded payment.

Expected:

```text
historical Payment evidence unchanged
ProviderConnection readiness for new work NOT_READY
```

**PASS**

---

# 60. Falsification — callback after disconnect

A valid callback arrives for an earlier payment after merchant disconnects.

Expected:

```text
callback authentication/correlation evaluated
existing obligation/process may reconcile if authorised
```

Not automatic rejection solely because new outbound work is disabled.

**PASS**

---

# 61. Falsification — secret-store outage

```text
provider healthy
connection exists
credential cannot be retrieved
```

Expected:

```text
affected external execution NOT_READY or UNKNOWN
underlying capability unchanged
binding unchanged
```

**PASS**

---

# 62. Falsification — internal fulfiller

```text
notification role
fulfiller = INTERNAL
```

Expected:

```text
no fake ProviderConnection required
internal readiness evaluated through applicable technical evidence
```

**PASS**

---

# 63. Falsification — provider removes feature

Provider remains reachable but stops supporting a registered obligation.

Expected:

```text
reachability ≠ readiness
```

Current binding cannot be treated as executable for that obligation merely because login/API health succeeds.

**PASS**

---

# 64. Rejected designs

Target 9 rejects:

1. one global `providerHealthy` boolean;
2. one global `ProviderStatus`;
3. Provider Readiness inside Merchant Configuration;
4. Provider Readiness inside the immutable RCP as current truth;
5. ProviderConnection identity equal to credential identity;
6. credential presence implying readiness;
7. OAuth as the universal connection architecture;
8. provider-wide health automatically overriding every merchant connection;
9. merchant-specific OAuth failure marking provider globally down;
10. connection existence implying operational readiness;
11. readiness implying execution success;
12. readiness failure implying previous provider execution failed;
13. automatic provider substitution;
14. automatic connection substitution;
15. circuit-breaker state becoming ProviderConnection state;
16. raw provider states becoming business lifecycle states;
17. raw telemetry becoming merchant-facing status;
18. destructive provider health checks;
19. mandatory polling for every provider;
20. a speculative distributed Provider Readiness microservice;
21. Redis/readiness caching without demonstrated need;
22. provider-specific branches in capability semantics;
23. callback signature validity becoming arbitrary mutation authority;
24. AI deciding runtime readiness.

---

# 65. Hard invariants

1. Provider Readiness is live runtime evidence.
2. Fulfilment Requirement, Binding, ProviderConnection and Readiness remain distinct.
3. Provider Readiness cannot activate semantics.
4. ProviderConnection cannot activate semantics.
5. Provider-wide Health and connection-specific Readiness remain distinct.
6. Readiness is role/context/obligation qualified.
7. `UNKNOWN` cannot be treated as `READY`.
8. Runtime provider-dependent execution fails closed when readiness cannot be established.
9. A surface readiness projection is not backend execution authority.
10. Readiness must be revalidated where execution depends on current provider conditions.
11. Credential existence is not readiness.
12. Credential validity is not sufficient provider authority.
13. ProviderConnection identity is not credential identity.
14. Credential rotation does not by itself replace ProviderConnection identity.
15. ProviderConnection must match the bound merchant/provider context.
16. Provider or connection substitution requires separately authorised binding/fallback semantics.
17. Circuit state is not ProviderConnection state.
18. Provider reachability is not sufficient readiness.
19. Provider-specific states do not become Main Street business states.
20. Provider evidence is interpreted before capability mutation.
21. Callback authentication does not imply business consequence.
22. Readiness does not resolve post-call execution uncertainty.
23. Existing accepted obligations survive later provider unavailability according to their owning semantics.
24. Connection disconnect does not deactivate the underlying capability.
25. Provider readiness changes do not create Configuration Revisions.
26. Provider readiness changes do not create Fulfilment Binding Set Revisions.
27. Initial production requires no dedicated readiness database/cache/service.
28. Provider health checks must not produce destructive business side effects.
29. Internal fulfilment does not require fake external ProviderConnections.
30. AI is not Provider Readiness authority.

---

# 66. Deferred questions

| ID | Status | Deferred question | Future owner |
|---|---|---|---|
| `MS-PROT-048-V14-DQ-001` | DEFERRED — INACTIVE | Exact Java representation of ProviderConnection | Provider implementation |
| `MS-PROT-048-V14-DQ-002` | DEFERRED — INACTIVE | Exact persisted ProviderConnection lifecycle vocabulary | Provider persistence implementation |
| `MS-PROT-048-V14-DQ-003` | DEFERRED — INACTIVE | Exact Java Provider Readiness resolver/result representation | Runtime implementation |
| `MS-PROT-048-V14-DQ-004` | DEFERRED — INACTIVE | Exact provider-contract declaration of readiness evidence requirements | Provider adapter implementation |
| `MS-PROT-048-V14-DQ-005` | DEFERRED — INACTIVE | Exact health-evidence freshness/expiry rules per provider | Provider implementation |
| `MS-PROT-048-V14-DQ-006` | DEFERRED — INACTIVE | OAuth/API-key/etc. provider-specific connection mechanics | Individual provider adapters |
| `MS-PROT-048-V14-DQ-007` | DEFERRED — INACTIVE | Connection reconciliation/background validation mechanism | Target 18 |
| `MS-PROT-048-V14-DQ-008` | DEFERRED — INACTIVE | Provider readiness telemetry and operational dashboards | Target 19 |
| `MS-PROT-048-V14-DQ-009` | DEFERRED — INACTIVE | Public/API representation of connection/readiness failures | Target 20 |
| `MS-PROT-048-V14-DQ-010` | DEFERRED — INACTIVE | Exact persistent storage/index strategy for ProviderConnections | Persistence implementation |
| `MS-PROT-048-V14-DQ-011` | DEFERRED — INACTIVE | Whether readiness caching is ever justified | Only after measured need |
| `MS-PROT-048-V14-DQ-012` | DEFERRED — INACTIVE | Exact provider-wide status ingestion mechanism | Operations/provider implementation |
| `MS-PROT-048-V14-DQ-013` | DEFERRED — INACTIVE | Provider & Integration AI Assistant concrete contracts | Future AI/provider assistance |
| `MS-PROT-048-V14-DQ-014` | DEFERRED — INACTIVE | Capability-specific degraded-operation rules | Owning later capability targets |

---

# 67. Implementation evidence

The current `FulfilmentPlanResolver` explicitly states that provider health, credentials and live ProviderConnection state remain outside its static resolution boundary. It preserves `providerConnectionIdentity` in the resolved binding but performs no runtime connection/readiness evaluation.

Repository search found no current production `ProviderConnection`, readiness-result or readiness-resolver implementation.

That means:

```text
static fulfilment architecture
    exists

live Provider Readiness architecture
    does not
```

The implementation evidence therefore confirms—not creates—the Target-9 design gap.

---

# 68. Corpus assessment

The authority preserves existing ownership:

- MS-PROT-048 remains provider/fulfilment semantic authority.
- MS-PROT-049 remains interaction-availability projection authority.
- MS-PROT-067 remains credential-security authority.
- MS-PROT-068 remains operational-health evidence authority.
- MS-PROT-069 remains execution-uncertainty authority.
- MS-PROT-070 remains resilience/fallback/degradation authority.

The design introduces no second provider semantics, no generic availability engine and no provider-specific business branching.

---

# 69. Governing trade-off

The accepted architecture deliberately avoids both insufficient and excessive provider-readiness machinery:

```text
too weak:
    ProviderConnection exists → READY

too elaborate:
    generic policy engine
    persistent readiness service
    universal health DSL
    provider state machine abstraction

accepted:
    obligation-qualified runtime resolver
    over current connection/security/health evidence
    fail closed
    request/current-state scoped initially
```

> **Provider Readiness is the smallest current-state runtime decision that proves an already-selected fulfilment path may be attempted now; it must never become capability meaning, provider selection, connection identity, execution outcome or business truth.**
