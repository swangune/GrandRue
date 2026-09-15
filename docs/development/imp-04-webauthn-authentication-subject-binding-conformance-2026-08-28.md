# IMP-04 WebAuthn Authentication Subject Binding Conformance

Date: 2026-08-28
Status: CONFORMING_COMPLETE
Macro target: IMP-04 — Merchant Account, Scope, Identity & Trust

## Authority

This child realises the manually approved ADR-015 v1.1 WebAuthn Authentication Subject model.

The approved model is the hybrid representation:

- Main Street generates a distinct opaque WebAuthn Authentication Subject Handle;
- the handle is not the semantic Main Street Identity identifier;
- one active handle maps one-to-one to one Main Street Identity in the initial model;
- one Identity has at most one active handle in the initial model;
- multiple passkeys for the Identity may share that handle;
- the handle contains no PII, Merchant Scope, Controller authority, role, privilege, entitlement or capability permission;
- Spring `PublicKeyCredentialUserEntity` attributes remain authentication-infrastructure data only;
- verified handle → Identity resolution feeds the existing Main Street post-authentication/session boundary;
- Merchant Scope and all business authority remain current downstream resolution concerns.

The user reconfirmed this representation as the approved hybrid on 2026-08-28. That clarification does not change the merged model; it confirms the ADR-015 v1.1 implementation shape.

## Implementation

Merged implementation: PR #40 — `IMP-04: bind WebAuthn subjects to Main Street Identity`.

Verified PR head:

`078d17d4e664668edf1f7f13a2ddb014e807ff2b`

Merge commit on `development`:

`ff20a30eddc265db0391dd486cc886bd94d7fc5b`

Implemented evidence includes:

- Flyway V31 `webauthn_authentication_subject` persistence;
- Main Street-generated opaque 32-byte / 256-bit authentication-subject handles;
- durable exact active handle ↔ Identity binding;
- Spring `PublicKeyCredentialUserEntityRepository` persistence over the approved binding;
- concrete `WebAuthnIdentityReferenceAuthority` resolving verified handle to Main Street Identity evidence;
- fail-closed behavior preventing an unknown framework save from manufacturing a new Identity binding;
- username/display-name data excluded from Identity authority;
- no Merchant Scope, Controller role, privilege, entitlement or capability-authority state in the binding.

A repository-wide determinism violation discovered during GREEN implementation was corrected by removing an unnecessary wall-clock timestamp from V31/adapter state. No test was weakened.

## Verification

Test-first RED commit:

`f22c0f604948f21db59323b868cd63bc113bdb1d`

Final exact-head PR CI:

- Maven Tests #1179 / run `33164812158`;
- 587 unit tests;
- 182 PostgreSQL integration tests;
- 0 failures;
- 0 errors;
- 0 skipped;
- 31 Flyway migrations;
- BUILD SUCCESS.

Independent post-merge `development` CI:

- Maven Tests #1180 / run `33164976044`;
- merge commit `ff20a30eddc265db0391dd486cc886bd94d7fc5b`;
- 587 unit tests;
- 182 PostgreSQL integration tests;
- 0 failures;
- 0 errors;
- 0 skipped;
- 31 Flyway migrations;
- BUILD SUCCESS.

## Superseded Parallel Draft

PR #39 was a parallel test-first draft for the same child. It has been closed without merge and explicitly marked superseded by PR #40. It is not implementation authority.

## Conformance Result

This child is CONFORMING_COMPLETE.

The implementation preserves the governing separation:

`WebAuthn authentication subject != Main Street Identity identifier != Merchant Scope != business authority`.

## Explicit Non-Claims / Residual IMP-04 Work

This child does not claim completion of:

- authentication-subject recovery, rebinding, replacement or rotation;
- durable Identity security-generation ownership/rotation and global invalidation orchestration;
- idle-timeout activity progression;
- step-up authentication;
- authentication-security Audit wiring;
- browser/filter-chain delivery, cookie policy, CSRF or CSP/Trusted Types;
- high-risk hardware-attestation policy;
- staff device-key policy.

IMP-04 therefore remains PARTIALLY_CONFORMING pending refresh of the residual implementation graph.