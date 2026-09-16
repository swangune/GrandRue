# MS-PROT-067 — Credential, Secret & External Connection Security Model

**Document ID:** MS-PROT-067  
**Version:** 1.0  
**Status:** Accepted  
**Depends on:** MS-PROT-024, MS-PROT-028, MS-PROT-031, MS-PROT-048, MS-PROT-057, MS-PROT-062, MS-PROT-063, MS-PROT-064, MS-PROT-065  
**Purpose:** Define the backend security contract for protected credentials, credential binding, rotation, revocation, compromise handling, provider/integration connection security and bounded credential use without allowing secret material to become business configuration, provider semantics or application-domain authority.

---

## 1. Governing Principle

> **Credentials prove or enable technical access; they do not define business meaning, capability applicability, merchant configuration, entitlement, fulfilment semantics or provider connection identity.**

Main Street SHALL keep protected credential material behind an explicit security boundary and SHALL expose only the minimum credential capability required by the infrastructure component performing the technical interaction.

---

## 2. Canonical Separation

Main Street SHALL preserve the following distinctions:

```text
Capability / Semantic Configuration
    owns business meaning and applicability

Fulfilment Binding
    owns which supported fulfiller discharges a role

Provider / External Connection
    owns which external account/context is connected
    and its operational authorisation/health

Credential Binding
    identifies the protected credential context used
    by that connection or platform responsibility

Credential Security Boundary
    owns credential generations, usability,
    rotation, revocation and compromise handling

Protected Credential Store
    protects physical secret material
```

None of these concepts are interchangeable.

---

## 3. Credential Is Not Configuration

Raw credential material MUST NOT be embedded in merchant semantic configuration, capability configuration, semantic registry entries or Resolved Configuration Packages.

Examples of prohibited semantic configuration fields include:

```text
stripeApiKey
calendarRefreshToken
courierPassword
webhookSigningSecret
aiProviderApiKey
```

Credential rotation MUST NOT require a merchant semantic configuration revision merely because protected material changed.

---

## 4. Credential Is Not Fulfilment Binding

A fulfilment binding determines which supported fulfiller is selected for a Main Street-defined role.

Credential material merely enables an already-selected and already-authorised external connection to perform technical interaction.

Therefore:

```text
credential exists
    ≠ provider selected

provider selected
    ≠ credential valid

credential valid
    ≠ capability applicable
```

Credential presence MUST NOT activate capability semantics.

---

## 5. Credential Is Not Provider Connection

A ProviderConnection may contain non-secret operational metadata such as:

```text
merchant or platform scope
provider account identity
connection/authorisation state
granted provider scopes
external account identifiers
connection health
webhook subscription state
sync cursor
last reconciliation evidence
credential binding identity
```

Protected access tokens, refresh tokens, passwords, private keys and signing secrets MUST remain behind the credential-security boundary.

A stored credential may exist while the provider connection is invalid or requires reauthorisation.

---

## 6. Credential Binding

A Credential Binding is a stable Main Street security reference associating a connection or platform responsibility with one or more protected credential generations.

Conceptually:

```text
CredentialBinding
{
    bindingIdentity
    owning scope/responsibility
    external/provider context
    permitted technical purpose
    current usable generation(s)
}
```

This structure is conceptual and does not mandate a Java class.

Credential Binding identity MUST NOT be the secret-store vendor locator itself.

---

## 7. Credential Generations

Rotation may require more than one physical credential generation to coexist temporarily.

Main Street MUST therefore NOT assume:

```text
one CredentialBinding = exactly one physical credential value
```

A binding MAY temporarily have an active generation and one or more bounded retiring/verification generations where the provider protocol requires overlap.

The semantic distinction is behavioural, not a required universal enum. The backend must be able to determine, as applicable, whether a generation:

```text
may be used for new execution
may be used for authorised existing-obligation completion
may be used only for verification
is retiring
is expired
is revoked
is compromised
```

---

## 8. Rotation

Credential rotation is an operational security process, not a merchant semantic reconfiguration.

The canonical rotation flow is:

```text
obtain/issue replacement
        ↓
validate replacement
        ↓
register new generation
        ↓
make new generation usable
        ↓
bounded overlap where provider protocol requires it
        ↓
prevent unauthorised new use of retiring generation
        ↓
complete explicitly authorised outstanding use if allowed
        ↓
retire/revoke/destroy old material according to policy
```

Rotation MUST preserve ProviderConnection identity unless the external account/connection itself changes.

---

## 9. Revocation and Compromise

A compromised or revoked credential MUST be preventable from future unauthorised use without waiting for semantic recompilation.

Credential compromise/revocation MAY affect Provider Readiness or connection health, but MUST NOT erase or rewrite existing business facts.

Examples of business facts that remain intact include Orders, Appointments, Inventory facts, Payment evidence, Shipments and Merchant Configuration revisions.

Security-relevant revocation/compromise actions SHOULD generate audit evidence under MS-PROT-064 where required.

---

## 10. Connection Reauthorisation

Credential existence is not proof that the provider still authorises Main Street.

A provider may revoke granted scopes or the external account may invalidate access while credential material remains stored.

The ProviderConnection MUST therefore be able to represent operational outcomes such as reauthorisation/reconnection required without redefining credential material as connection authority.

Connection reauthorisation MUST NOT silently disable the underlying semantic capability.

---

## 11. Merchant-Scoped Connection Credentials

Where a merchant connects an external provider account, credential resolution MUST be scoped to the specific ProviderConnection or equivalent external-connection identity.

Rejected:

```text
getCredential("stripe")
```

Required principle:

```text
resolve credential for this exact authorised connection/scope
```

Two merchants using the same provider MUST NOT share credential context merely because provider type is equal.

---

## 12. Platform Credentials

Main Street MAY use platform-owned credentials for infrastructure providers such as notification, AI, media or other platform services.

Platform credentials MUST NOT require creation of a fake merchant scope.

The credential-security boundary SHALL support explicitly non-merchant platform responsibility.

Platform credentials remain subject to least privilege, rotation, revocation, audit and secret-exposure restrictions.

---

## 13. Integration/Webhook Verification Material

Incoming provider callbacks MAY require signing keys, secrets, certificates or equivalent verification material.

Credential rotation MAY require verification against current and bounded retiring generations during a transition window.

Successful signature/credential verification establishes trusted integration origin only. It MUST NOT imply unrestricted authority to mutate arbitrary Main Street business state.

Verified callbacks MUST continue through MS-PROT-063 and MS-PROT-062-compatible bounded execution paths.

---

## 14. Background Work

Durable work instructions under MS-PROT-065 MUST NOT persist raw credentials.

Canonical pattern:

```text
DurableWorkInstruction
    references provider/external connection or platform responsibility
        ↓
execution begins
        ↓
resolve current authorised credential generation
        ↓
provider/infrastructure adapter
```

A scheduled operation created before credential rotation SHOULD ordinarily use the current usable credential at execution time rather than embedding the historical secret.

Any protocol requiring credential generation capture MUST explicitly justify that requirement and MUST NOT expose the raw credential in work payloads.

---

## 15. Existing Obligations After Disconnection

Merchant/provider disconnection does not permit the Credential Security boundary to invent business policy.

The owning capability/process/connection contract determines whether credentials may remain usable for bounded completion or reconciliation of existing obligations.

Where authorised:

```text
credential may be disabled for new activity
while remaining usable for explicitly bounded
existing-obligation reconciliation
```

Where not authorised, new or continued use MUST stop.

The Credential Security boundary enforces the authorised use classification; it does not decide why the business obligation exists.

---

## 16. Least Privilege

Raw credential material SHOULD be available only to the minimum infrastructure component that must perform the external technical interaction.

Examples:

```text
Payment adapter
    may obtain payment-provider credential capability

Publication domain
    must not

Merchant dashboard
    must not retrieve raw credential

AI specialist
    must not retrieve courier/payment secrets
```

The modular monolith deployment model does not weaken this least-privilege requirement.

---

## 17. Adapter Boundary

Domain and ordinary application code MUST remain independent of concrete credential-store products.

Preferred conceptual direction:

```text
Application / Provider operation
        ↓
Provider adapter
        ↓
Credential resolver/security port
        ↓
Protected credential-store adapter
```

Domain code MUST NOT depend on provider-specific secret manager clients, vault paths, environment-variable names or secret-store APIs.

Where feasible, ordinary application code SHOULD pass connection/responsibility identity rather than receive raw `String apiKey` values.

---

## 18. Raw Secret Exposure Prohibition

Raw credential material MUST NOT be emitted into normal:

```text
application logs
traces
metrics labels
audit payloads
domain events
integration events
API responses
error responses
exception messages
AI prompts or agent context
configuration dumps
```

Where diagnostics require identification, Main Street SHOULD use non-secret references such as CredentialBinding identity, credential generation identifier, ProviderConnection identity or provider transaction/correlation identifiers.

---

## 19. Source Control and Test Safety

Production secret material MUST NOT be committed to source control, production code constants, fixtures or ordinary test resources.

Tests SHOULD use non-production dummy credentials, ephemeral test secrets, fake credential resolvers or mock/stub provider adapters according to test scope.

A test MUST NOT require real production credentials merely to validate domain/application behaviour.

---

## 20. AI Provider Credentials

AI specialists MUST NOT receive or manipulate raw provider credentials.

Canonical path:

```text
AI Specialist
    ↓
AI Provider Port
    ↓
AI Infrastructure Adapter
    ↓
bounded credential resolution
    ↓
AI provider
```

Credential availability MUST NOT allow an AI specialist to invent or select unregistered semantics.

Credential material MUST NOT be copied into prompt/context memory.

---

## 21. Secret Store Unavailability

If protected credential material cannot be resolved because the credential store is unavailable:

```text
semantic applicability remains unchanged
commercial entitlement remains unchanged
actor authority remains unchanged
provider binding remains unchanged
provider/external connection identity remains unchanged
```

The resulting operational condition MAY make Provider Readiness false or produce another explicit infrastructure failure classification.

Secret-store outage MUST NOT rewrite semantic configuration.

---

## 22. Credential Failure Classifications

The backend SHOULD distinguish security/connection conditions sufficiently to avoid ambiguous recovery. Examples include:

```text
CREDENTIAL_UNAVAILABLE
CREDENTIAL_EXPIRED
CREDENTIAL_REVOKED
CREDENTIAL_COMPROMISED
REAUTHENTICATION_REQUIRED
INSUFFICIENT_PROVIDER_SCOPE
SECRET_STORE_UNAVAILABLE
CONNECTION_INVALID
```

These are operational/security classifications, not universal business lifecycle states.

The exact representation MAY differ by implementation/provider contract.

---

## 23. Historical Business Provenance

Provider-backed business actions MUST preserve the binding/provider provenance required by MS-PROT-048.

They SHOULD NOT normally persist raw credentials or make credential generation part of business identity.

Credential-generation evidence MAY be retained in protected security/audit metadata where required for forensic or compliance purposes, but MUST remain distinct from business semantic identity.

---

## 24. Merchant UX Boundary

Where merchant-connected providers are supported, merchant surfaces MAY expose bounded connection controls such as:

```text
connect
reconnect
disconnect
connection status
required granted scopes
provider account identity where safe
```

Merchant surfaces MUST NOT routinely redisplay stored raw secrets after acceptance.

Direct raw secret entry MAY be supported only where the selected provider protocol requires it and MUST flow immediately into the credential-security boundary rather than merchant semantic configuration.

---

## 25. Audit

Credential creation, rotation, revocation, compromise response, privileged read/access, reconnection and break-glass operations MAY require AuditRecord evidence under MS-PROT-064.

Audit evidence MUST reference non-secret identity/provenance rather than raw credential material.

Audit MUST NOT become the source of credential material or ProviderConnection state.

---

## 26. Canonical Execution Graph

```text
Capability / Process needs provider fulfilment
        ↓
Fulfilment Binding
        ↓
Provider / External Connection
        ↓
Provider Adapter
        ↓
Credential Binding
        ↓
Credential Security Boundary
        ↓
current authorised credential generation
        ↓
Protected Credential Store
        ↓
External Provider
```

Incoming callback direction:

```text
External Provider
        ↓
verification material
        ↓
Credential Security / Integration verification
        ↓
Trusted Integration Principal
        ↓
MS-PROT-062
        ↓
capability-owned processing
```

---

## 27. Hard Invariants

1. Credential material MUST NOT be business configuration.
2. Credential material MUST NOT be fulfilment binding authority.
3. Credential material MUST NOT be ProviderConnection identity.
4. Credential existence MUST NOT imply ProviderConnection validity.
5. Credential existence MUST NOT activate semantic capability applicability.
6. Credential rotation MUST NOT require semantic configuration revision merely because protected material changed.
7. Credential generation MUST NOT become business entity identity.
8. Raw credentials MUST NOT enter normal domain/application data structures where avoidable.
9. Raw credentials MUST NOT enter ordinary logs, events, audit payloads, API responses, error payloads, traces, metrics or AI context.
10. Production secret material MUST NOT be committed to source control.
11. Durable background work MUST NOT persist raw credentials.
12. Credential resolution MUST be connection/scope bounded.
13. Platform credentials MUST NOT require fake merchant scope.
14. Provider disconnection MUST NOT disable underlying semantic capabilities merely because credentials are unavailable.
15. Credential failure affects connection/provider readiness, not semantic applicability.
16. Rotation MUST support bounded credential overlap where provider protocol requires it.
17. Revocation/compromise MUST prevent unauthorised future credential use.
18. Existing-obligation credential use MUST be explicitly authorised by the owning process/connection contract.
19. Provider/integration callback verification MUST NOT imply arbitrary mutation authority.
20. Domain/application semantics MUST remain independent of the concrete credential-store vendor.
21. AI specialists MUST NOT receive raw provider credentials.
22. Merchant surfaces MUST NOT routinely redisplay stored raw secrets.
23. Security/audit provenance MUST use non-secret references wherever possible.

---

## 28. Explicit Non-Responsibilities

MS-PROT-067 does NOT define:

- merchant business policies;
- capability applicability;
- subscription entitlement;
- fulfilment-role semantics;
- ProviderConnection lifecycle beyond the security interactions defined here;
- identity-provider/login credential semantics already governed by identity/authentication authorities;
- secret-store vendor;
- KMS/HSM vendor;
- encryption algorithm/implementation;
- OAuth library;
- token refresh library;
- secret caching strategy;
- concrete Java classes/interfaces;
- deployment/runtime configuration generally.

These remain governed elsewhere or downstream unless later evidence demonstrates semantic consequences.

---

## 29. Falsification Summary

The model has been tested against:

- merchant OAuth reconnection;
- provider disconnection without semantic capability removal;
- credential present but provider scope revoked;
- secret-store outage;
- credential rotation during an in-flight provider operation;
- overlapping credential generations;
- webhook signing-key rotation;
- scheduled work created before credential rotation;
- multiple merchants using one provider type;
- platform-owned credentials;
- AI-provider credentials;
- accidental logging of raw secrets;
- credential compromise/revocation;
- provider disconnection while outstanding reconciliation obligations remain.

No tested scenario requires credentials to own business semantics, ProviderConnection identity or fulfilment selection.

---

## 30. Acceptance Statement

Main Street now has a bounded credential-security model in which external technical access can be rotated, revoked, reconciled and protected without contaminating merchant semantics or capability ownership.

The governing rule is:

> **Bind credentials to the exact technical connection or platform responsibility that needs them; expose only bounded credential capability; rotate security material without rewriting business meaning.**
