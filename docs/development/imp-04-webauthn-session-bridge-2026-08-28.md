# IMP-04 WebAuthn → Main Street Session Bridge Evidence — 2026-08-28

**Programme node:** MS-IMP-001 / IMP-04 — Merchant Account, Scope, Identity & Trust  
**Child scope:** ADR-015 post-verification WebAuthn → Main Street Session bridge  
**Status:** CONFORMING_COMPLETE for this child scope; IMP-04 remains PARTIALLY_CONFORMING

## Accepted authority

- MS-PROT-028 — Identity, Actor, Customer & Access Boundary Model
- MS-PROT-063 — Authentication, Session & Trusted Execution Principal Establishment Model
- ADR-014 — High-Assurance Authentication, Session & Trusted Browser Execution Architecture
- ADR-015 — WebAuthn / Passkey Production Adapter
- designs/IMPLEMENTATION-RULES.md

## Implemented evidence

Production:

- Boot-managed `org.springframework.security:spring-security-webauthn` dependency;
- `WebAuthnIdentityReferenceAuthority` — explicit mapping from already-verified Spring WebAuthn user entity to Main Street Identity reference;
- `IdentitySecurityGenerationAuthority` — current Identity security-generation evidence port;
- `WebAuthnSessionEstablishmentPolicy` — explicit assurance/method/lifetime evidence supplied by the configured authentication path;
- `SpringWebAuthnSessionBridge` — successful Spring WebAuthn authentication to existing Main Street `HumanSessionEstablishmentService`;
- `UnverifiedWebAuthnAuthenticationException` — fail-closed post-verification boundary.

Tests prove:

1. a successful Spring `WebAuthnAuthentication` establishes a fresh Main Street opaque Session;
2. Spring's human-readable WebAuthn username/display identity is not silently treated as Main Street semantic Identity;
3. current security-generation evidence is obtained from a Main Street authority;
4. assurance/method/absolute-lifetime evidence is injected by policy rather than inferred from Spring roles;
5. arbitrary Spring `GrantedAuthority` does not enter Main Street Session authority;
6. repeated successful WebAuthn proofs create independent Session Identities and bearer/verifier values;
7. an unauthenticated WebAuthn token is rejected before Identity/security/session authorities are touched;
8. blank authoritative identity mapping fails closed before Session creation.

## Explicit non-scope

This child does not implement:

- WebAuthn credential registration or durable credential repository;
- relying-party ID / allowed-origin / user-verification HTTP configuration;
- Spring Security filter-chain activation;
- Spring `HttpSession` / `SecurityContext` as Main Street continuity;
- privileged session-cookie transport;
- CSRF implementation;
- Merchant Scope resolution or Controller privilege;
- idle-timeout/security-generation validation on bearer reuse;
- step-up authentication;
- authentication-security Audit wiring.

## Verification

Implementation PR: #32 — `IMP-04: bridge verified WebAuthn into Main Street sessions`

Verified PR head:

`ec1a0175f1a35c7b4ce7739d2e19bf43eae027e3`

PR CI:

- GitHub Actions run `33151167841`
- `mvn --batch-mode clean verify -Ppostgres-it`
- result: SUCCESS

Merged development commit:

`a3fc60da59d59842a6f223b0a57149b2d4dc8be1`

Post-merge development CI:

- GitHub Actions run `33151335525`
- `mvn --batch-mode clean verify -Ppostgres-it`
- result: SUCCESS

## Residual IMP-04 boundary

IMP-04 remains PARTIALLY_CONFORMING. The next automatic security work may close already-accepted session invalidation/lifetime mechanics. WebAuthn credential persistence and privileged browser transport must remain separate from semantic Identity, Main Street Session authority, Merchant Scope and downstream authorisation.
