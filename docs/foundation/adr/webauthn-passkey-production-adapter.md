# ADR-015 — WebAuthn / Passkey Production Adapter

**Document ID:** ADR-015  
**Status:** Accepted  
**Accepted:** 28 August 2026  
**Resolves:** ADR-014-DQ-001  
**Depends on:** MS-PROT-028, MS-PROT-031, MS-PROT-062, MS-PROT-063, MS-PROT-064, ADR-014  
**Scope:** Production Controller WebAuthn/passkey ceremony and cryptographic-verification adapter only

---

## 1. Context

ADR-014 accepted passkey/WebAuthn as the high-assurance authentication direction but deliberately deferred the exact production library/provider choice as `ADR-014-DQ-001` until Controller authentication implementation.

IMP-04 has now established and verified Main Street-owned foundations for:

- durable `SessionRecord` authority;
- opaque bearer credential verification;
- bearer-to-trusted-identity continuity;
- durable logout/session revocation;
- fresh post-authentication session establishment and session-fixation protection;
- current Merchant Controller relationship revalidation for one already-resolved Merchant Scope.

The remaining production Controller authentication path requires a concrete WebAuthn implementation without transferring Main Street semantic authority into a framework session, browser claim, provider SDK or credential library.

---

## 2. Problem Statement

Main Street requires a maintained WebAuthn implementation that can perform standards-conformant registration and authentication ceremonies while preserving these existing boundaries:

```text
WebAuthn ceremony / cryptographic verification
        ↓
authenticated Identity evidence
        ↓
Main Street HumanSessionEstablishmentService
        ↓
fresh opaque Main Street Session Credential
        ↓
durable SessionRecord authority
        ↓
current Merchant Scope / relationship / authorisation revalidation
```

The WebAuthn implementation MUST NOT become:

- the Main Street Identity Session authority;
- the source of Merchant Scope;
- the source of Controller or staff authority;
- a browser-carried role/privilege authority;
- the source of business authorisation;
- the source of Audit semantics.

---

## 3. Options Considered

### Option A — Spring Security `spring-security-webauthn`

Use Spring Security's native WebAuthn/passkey support as the ceremony and cryptographic-verification adapter.

Advantages:

- aligns with Main Street's existing Spring Boot/Spring stack;
- version is managed through the Spring Boot dependency-management baseline;
- supports WebAuthn registration and authentication;
- exposes replaceable credential/user repositories and relying-party operations;
- avoids introducing a parallel authentication framework;
- allows Main Street to adapt successful authentication into its own session authority.

Risks:

- Spring Security has its own security-context/session conventions that MUST NOT become Main Street session authority;
- default ceremony-state repositories may use `HttpSession` and therefore require explicit boundary treatment;
- relying-party configuration, allowed origins and user-verification policy must be explicit;
- framework credential persistence must not become the semantic owner of Main Street Identity/authority relationships.

### Option B — Yubico `java-webauthn-server`

Use Yubico's WebAuthn server library directly.

Advantages:

- mature dedicated WebAuthn library;
- strong standards focus;
- lower coupling to Spring Security authentication conventions.

Disadvantages:

- introduces an additional authentication integration stack beside Spring;
- requires more custom ceremony, HTTP and lifecycle integration;
- increases maintenance surface without providing semantic authority Main Street needs.

### Option C — custom WebAuthn implementation

Rejected. WebAuthn ceremony and cryptographic verification are security-sensitive standards work and are not an appropriate bespoke implementation surface for Main Street.

---

## 4. Decision

Main Street SHALL use:

```text
org.springframework.security:spring-security-webauthn
```

as the initial production WebAuthn/passkey ceremony and cryptographic-verification adapter.

The dependency SHALL use the version managed by Main Street's accepted Spring Boot dependency baseline unless a separately approved security/compatibility decision requires an override.

This decision resolves `ADR-014-DQ-001`.

---

## 5. Mandatory Boundary Rules

### 5.1 WebAuthn authenticates Identity; it does not establish business authority

Successful WebAuthn verification may establish authenticated Identity evidence only.

It MUST NOT directly establish:

- Merchant Scope;
- current Controller relationship;
- staff Membership;
- Role/Privilege;
- entitlement;
- capability permission;
- operational eligibility.

Those remain current authoritative checks under MS-PROT-031, MS-PROT-062, MS-PROT-063 and the owning domain authorities.

### 5.2 Main Street remains Session authority

A successful WebAuthn authentication SHALL feed the Main Street post-authentication session-establishment boundary.

Main Street SHALL create a fresh independent opaque Session Credential and durable `SessionRecord`.

Spring Security `HttpSession`, `SecurityContext`, remember-me state or equivalent framework continuity MUST NOT replace the accepted Main Street Session Record authority.

### 5.3 Pre-authentication ceremony state is not authenticated continuity

WebAuthn challenge/creation/request option state is bounded ceremony state.

A framework `HttpSession` MAY be used internally for short-lived pre-authentication ceremony correlation only where required by the chosen adapter, but:

- it is not an authenticated Main Street Session Credential;
- it is not promoted into authenticated continuity;
- successful authentication results in a fresh Main Street credential;
- attacker-influenced pre-authentication identifiers are not upgraded into authenticated authority.

This preserves ADR-014 session-fixation protection.

### 5.4 Relying Party configuration is explicit

Production configuration MUST explicitly define:

- Relying Party identifier;
- allowed privileged-authentication origins;
- expected HTTPS production origin policy;
- user-verification requirement appropriate to passwordless Controller authentication.

Merchant storefront/custom domains MUST NOT silently become privileged authentication origins merely because they route to Main Street infrastructure.

### 5.5 Credential persistence is adapted behind Main Street boundaries

Spring Security credential repositories MAY be used as technical adapters, but they MUST NOT become semantic ownership of:

- Identity relationships;
- Merchant Controller authority;
- Merchant Scope;
- staff Membership;
- application authorisation.

Persisted WebAuthn credential material is authentication evidence/credential state only.

### 5.6 No universal hardware-attestation requirement

Ordinary Merchant Controller authentication does not require universal enterprise authenticator attestation.

Strict hardware/security-key or authenticator-attestation requirements for platform/high-risk roles remain governed by `ADR-014-DQ-002` and are not resolved by this ADR.

### 5.7 Audit remains separate

Security-relevant registration, authentication, credential-management and recovery activity SHALL emit governed `AUTHENTICATION_SECURITY` evidence where required by MS-PROT-064.

Raw WebAuthn assertions, private keys, session bearers or unnecessary credential payloads MUST NOT be written to Audit merely for convenience.

---

## 6. Initial Implementation Shape

The intended adapter boundary is:

```text
Spring Security WebAuthn
        │
        │ successful verified authentication
        ▼
MainStreetWebAuthnAuthenticationAdapter
        │
        │ authenticated Identity + bounded method/assurance evidence
        ▼
HumanSessionEstablishmentService
        │
        ├── new Session Identity
        ├── new 256-bit opaque bearer
        ├── persisted verifier only
        └── durable SessionRecord
```

A later browser/HTTP delivery slice may set the resulting Main Street bearer in the accepted secure transport representation. That transport choice does not alter session ownership.

---

## 7. Dependency Adoption Classification

`spring-security-webauthn` is a material security dependency under `IMPLEMENTATION-RULES.md` because it affects authentication/security architecture.

Manual approval was received on 28 August 2026.

The dependency is therefore approved only within the boundaries in this ADR. Broader adoption of Spring Security session/authorisation semantics is not implied.

---

## 8. Consequences

Positive:

- production WebAuthn implementation can proceed without bespoke cryptographic protocol code;
- the adapter remains aligned with the existing Spring stack;
- Main Street session and merchant-authority semantics remain independent;
- credential/authentication technology remains replaceable behind a narrow adapter.

Costs:

- integration tests must prove Spring authentication cannot substitute its own session authority;
- explicit RP/origin/user-verification configuration is required;
- WebAuthn credential persistence and migration become a maintained security implementation concern;
- framework upgrades require security regression testing.

---

## 9. Falsification / Acceptance Tests

The implementation is non-conforming if any of the following can occur:

1. successful WebAuthn authentication directly grants Merchant Scope or Controller privilege;
2. a Spring `HttpSession` identifier becomes the Main Street authenticated bearer;
3. a caller-supplied/pre-auth session identifier is upgraded into the authenticated Main Street session;
4. changing the current Merchant Controller relationship fails to remove former Controller authority on the next scoped resolution;
5. merchant storefront origins are implicitly accepted as privileged WebAuthn origins;
6. raw session credentials or private credential material are persisted in Audit;
7. a framework-provided role or authority bypasses MS-PROT-062 current authorisation checks;
8. WebAuthn registration/authentication succeeds outside explicitly configured RP/origin constraints.

---

## 10. Deferred Scope Preserved

This ADR does not resolve:

- `ADR-014-DQ-002` — exact hardware-security-key requirement for high-risk/platform roles;
- `ADR-014-DQ-003` — password/KDF fallback implementation;
- `ADR-014-DQ-004` — account/control recovery mechanism;
- `ADR-014-DQ-005` — exact CSRF implementation/library;
- `ADR-014-DQ-007` — session cache technology/invalidation strategy;
- `ADR-014-DQ-008` — device-key storage/hardware binding;
- `ADR-014-DQ-017` — exact CSP/Trusted Types privileged-browser hardening profile.

Those remain separately governed and must not be inferred from this decision.
