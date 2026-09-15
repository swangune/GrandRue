# IMP-04 — ADR-015 Governance Conformance Evidence

**Date:** 28 August 2026  
**Status:** GOVERNANCE FORMALISATION EVIDENCE  
**Scope:** ADR-014-DQ-001 resolution and ADR-014-DQ-006 reconciliation within IMP-04  
**Authority:** Evidence only; does not create semantic or architectural authority

## 1. Manual decision

The exact WebAuthn/passkey implementation/provider/library question `ADR-014-DQ-001` was manually approved on 28 August 2026 after recommendation, trade-off and security-boundary review.

The approved decision is formalised by the existing accepted ADR-015 — WebAuthn / Passkey Production Adapter at `docs/foundation/adr/webauthn-passkey-production-adapter.md`.

## 2. Approved implementation boundary

ADR-015 selects Spring Security `spring-security-webauthn` as the initial replaceable WebAuthn ceremony and cryptographic-verification adapter while preserving these existing Main Street authorities:

- Main Street opaque Session Credential and durable Session Record remain authenticated-continuity authority;
- successful WebAuthn authentication feeds the Main Street post-authentication session-establishment boundary;
- WebAuthn credential persistence contains no mutable Merchant Scope, Controller role, staff Role Assignment, entitlement or capability privilege;
- current Merchant Scope, Controller relationship and business authority remain revalidated from their owning Main Street authorities;
- RP ID and privileged origins are explicit;
- passwordless Controller authentication requires user verification;
- ordinary Controller passkeys do not universally require authenticator attestation;
- high-risk hardware/security-key policy remains deferred under `ADR-014-DQ-002`;
- Spring/WebAuthn types remain isolated at the security adapter boundary.

## 3. Existing implementation compatibility

The current `development` baseline already contains a Spring WebAuthn dependency and a verified post-WebAuthn session bridge. That bridge delegates successful authentication into `HumanSessionEstablishmentService` and does not make Spring `HttpSession`, `SecurityContext`, GrantedAuthority or Merchant Scope the authoritative Main Street session/business-authority model.

This governance formalisation therefore preserves existing compatible implementation rather than creating a duplicate bridge.

## 4. Deferred-decision reconciliation

The canonical Deferred Decision Register is updated in place:

- `ADR-014-DQ-001` → **RESOLVED** by ADR-015;
- `ADR-014-DQ-006` → **RESOLVED** by the already-verified V29 durable `session_record` / `JooqSessionRecordStore` implementation evidence.

All other ADR-014 deferred questions retain their prior status.

## 5. Governance navigation

The Canonical Authority Index is updated to v3.39 and now indexes ADR-015 beneath accepted MS-PROT semantic authority and ADR-014.

The canonical Deferred Decision Register is updated to v4.22.

## 6. Verification gate

This evidence is conforming only after the exact governance head passes the repository's normal pull-request verification gate and the resulting `development` merge commit independently passes the push-triggered verification gate.
