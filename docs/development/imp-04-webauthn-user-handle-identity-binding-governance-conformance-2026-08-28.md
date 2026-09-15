# IMP-04 — ADR-015 v1.1 WebAuthn Authentication-Subject Binding Governance Conformance

**Date:** 28 August 2026  
**Status:** GOVERNANCE_CONFORMANCE_EVIDENCE  
**Implementation target:** IMP-04 Merchant Account, Scope, Identity & Trust

## Decision resolved

The design escalation `docs/development/imp-04-webauthn-user-handle-identity-binding-design-escalation-2026-08-28.md` identified a material unresolved security/identity question: the relationship between the WebAuthn `user.id` / user handle and Main Street semantic Identity.

Manual approval was received on 28 August 2026 for the recommended lightweight hybrid:

> use a Main Street-generated opaque authentication-subject handle, one-to-one with Identity; multiple passkeys for the Identity share the handle; the handle has no independent business semantics and does not require a heavyweight WebAuthn User domain object.

## Formalised authority

The approval is formalised as:

- `ADR-015 v1.1 — WebAuthn Authentication-Subject Binding Amendment`
- path: `docs/foundation/adr/webauthn-passkey-production-adapter-v1.1-authentication-subject-binding.md`

The amendment is additive/narrower to ADR-015 v1.0. It does not replace ADR-015's Spring Security WebAuthn adapter, RP/origin/user-verification, Main Street Session authority or downstream merchant-authority boundaries.

## Canonical navigation

`designs/AUTHORITY-INDEX.md` is updated to v3.40 and now composes ADR-015 v1.0 + v1.1.

The index was also compacted without semantic change so it remains a navigation authority rather than duplicating long substantive design summaries. All accepted MS-PROT identifiers 020..079, accepted ADR/TAS authority, implementation-programme authority and known amendment-chain navigation remain represented.

## DDR impact review

No canonical Deferred Decision Register row changed status. The escalation was deliberately promoted development evidence without a DDR identifier, which `DESIGN-CORPUS-CONFORMANCE.md` explicitly permits. ADR-015 already exists in the DDR's accepted implementation/production-architecture catalogue, so ADR-015 v1.1 does not introduce a new architecture identifier requiring a new DDR catalogue entry.

Therefore no `DEFERRED-DECISION-REGISTER.md` mutation is required for this governance completion cycle.

## Implementation-Rules impact

No Implementation Rules change is required. The existing rules already require:

- implementation from accepted authority;
- tests first;
- smallest READY child;
- no silent semantic/security invention;
- verification/evidence/commit discipline.

The approved authority removes the previous manual stop for the narrow WebAuthn authentication-subject binding child.

## Newly authorised implementation boundary

The smallest READY child may now implement:

```text
Main Street-owned PublicKeyCredentialUserEntityRepository
+
opaque Authentication Subject Handle persistence
+
unique active handle <-> Identity binding
+
Spring-required id/name/displayName lookup
+
WebAuthnIdentityReferenceAuthority backed by the binding
```

It may not infer or add Merchant Scope, Controller, Role, Privilege, entitlement, browser/filter-chain, CSRF, recovery, step-up, high-risk hardware-attestation or authentication-subject replacement/rotation semantics.
