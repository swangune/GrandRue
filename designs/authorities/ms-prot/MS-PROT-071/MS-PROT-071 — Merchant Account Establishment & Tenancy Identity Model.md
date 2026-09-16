# MS-PROT-071 — Merchant Account Establishment & Tenancy Identity Model

**Document ID:** MS-PROT-071  
**Version:** 1.0  
**Status:** ACCEPTED after governed review, falsification and manual approval  
**Depends on:** MS-PROT-028, MS-PROT-031, MS-PROT-059, MS-PROT-063, MS-PROT-069  
**Purpose:** Define the authoritative fact by which a Main Street Merchant Account exists, its durable tenancy identity, establishment operation, atomicity, retry/idempotency behaviour and strict non-ownership boundaries without collapsing onboarding, configuration, trust, commercial, publication or operational lifecycles into one merchant status.

---

## 1. Governing decision

A **Merchant Account** is the authoritative Main Street tenancy under which one merchant's Main Street-owned configuration, commercial, operational and administrative facts are scoped.

Merchant Account establishment means only that the merchant tenancy exists. It MUST NOT imply that the merchant is configured, verified, published, subscribed, commercially entitled, operationally eligible or provider-ready.

Canonical separation:

```text
Merchant Account existence
    ≠ Merchant Configuration
    ≠ controller trust / verification
    ≠ Commercial Agreement
    ≠ Commercial Entitlement
    ≠ publication / Exposure
    ≠ Operational Eligibility
    ≠ Provider Readiness
```

No universal merchant `status` may collapse these independent authorities.

---

## 2. Semantic owner

The **Merchant Account context** OWNS Merchant Account existence and Merchant Account identity.

The following neighbouring concerns MUST NOT own or independently recreate Merchant Account existence:

- onboarding;
- Merchant Configuration;
- commercial/subscription;
- authentication/session;
- trust/verification;
- publication/storefront;
- provider integration;
- AI inference.

Other contexts MAY reference the durable `merchantIdentifier`, request Merchant Account operations, react to committed Merchant Account facts or derive projections. They MUST NOT maintain a competing authoritative account-existence fact.

---

## 3. Authoritative source of truth

The authoritative source of Merchant Account existence is the durably committed Merchant Account fact owned by the Merchant Account context.

A UI session, onboarding draft, authenticated principal, Merchant Configuration, website record, provider account or Commercial Agreement MUST NOT by itself establish Merchant Account existence.

Formally:

```text
committed MerchantAccount(merchantIdentifier)
    => Merchant Account exists
```

No other representation is authoritative for that fact.

---

## 4. Identity and equality

Every Merchant Account MUST have one immutable Main Street-issued `merchantIdentifier`.

Two Merchant Accounts are the same Merchant Account if and only if their durable `merchantIdentifier` is equal.

The following MUST NOT establish Merchant Account identity or equality:

```text
business name
address
controller identity
email address
business category
provider account identity
```

Similarity of merchant attributes MAY be relevant to separate uniqueness, abuse, trust or reconciliation authorities, but MUST NOT silently redefine Merchant Account identity.

---

## 5. Scope

`merchantIdentifier` supplies merchant tenancy scope to facts owned by other Main Street contexts.

Examples include Merchant Configuration revisions, commercial relationships, merchant actor relationships, operational objects, provider connections and merchant projections.

Supplying scope MUST NOT transfer semantic ownership of those facts to the Merchant Account context.

---

## 6. EstablishMerchantAccount operation

### 6.1 Operation name

`EstablishMerchantAccount`

### 6.2 Owning context

Merchant Account context.

### 6.3 Required semantic inputs

The operation MUST receive:

```text
logicalEstablishmentRequestIdentity
authorised initiating principal
```

Additional registration data MAY be supplied by separately governed contracts. MS-PROT-071 does not make such additional data authoritative merely because it participates in transport or onboarding.

### 6.4 Actor/principal requirement

The initiating principal MUST satisfy the applicable authentication/authorisation requirement established by the governing identity/access authorities.

MS-PROT-071 does not define the authentication mechanism or controller-verification policy.

### 6.5 Semantic applicability

`EstablishMerchantAccount` is a platform-level tenancy-establishment operation. It MUST NOT depend on Merchant Configuration capability applicability because no Merchant Configuration is required to exist before the Merchant Account is established.

### 6.6 Commercial entitlement

`EstablishMerchantAccount` MUST NOT require a merchant subscription Commercial Entitlement.

Commercial state follows Merchant Account existence and is governed separately.

---

## 7. Successful authoritative effect

A successful `EstablishMerchantAccount` operation MUST establish exactly one durable Merchant Account for the logical establishment request.

The minimum semantic fact is:

```text
MerchantAccount
{
    merchantIdentifier
}
```

Persistence metadata that has no independent business semantics MAY exist as implementation detail.

The success result MUST identify the established `merchantIdentifier`.

---

## 8. Atomicity boundary

The following invariant MUST be protected atomically:

```text
allocate authoritative merchantIdentifier
+
commit Merchant Account existence
```

Main Street MUST NOT expose an authoritative `merchantIdentifier` as an established Merchant Account when the Merchant Account existence fact did not commit.

Once the establishment transaction commits, loss of transport acknowledgement MUST NOT make the Merchant Account cease to exist.

Onboarding creation, Merchant Configuration creation, Commercial Agreement creation, trial establishment, publication and welcome/notification delivery MUST NOT be forced into this transaction merely because they may occur later in the merchant journey.

---

## 9. Retry and duplicate semantics

The same logical account-establishment intent may arrive more than once through retriable transport, UI repetition or lost acknowledgement.

`logicalEstablishmentRequestIdentity` identifies the logical establishment intent for idempotency purposes within its authorised establishment context.

Repeated submission of the same logical establishment request MUST NOT create more than one Merchant Account.

If the first attempt committed but its acknowledgement was lost, a retry of the same logical establishment request MUST resolve to the already-established Merchant Account rather than establish another Merchant Account.

Different logical establishment requests MUST NOT be classified as the same Merchant Account solely because merchant attributes are similar.

The storage mechanism used to enforce this invariant is an implementation detail provided that the observable semantics above are preserved.

---

## 10. Concurrency

Concurrent processing of the same `logicalEstablishmentRequestIdentity` MUST serialize or otherwise enforce the invariant that at most one Merchant Account is established for that logical request.

MS-PROT-071 does not prescribe the database or locking mechanism.

Concurrent distinct logical establishment requests are not duplicates under MS-PROT-071 merely because business attributes overlap. Any independent uniqueness restriction requires its own accepted authority.

---

## 11. Failure and rejection semantics

`EstablishMerchantAccount` MUST distinguish outcomes where caller recovery differs:

```text
VALIDATION_REJECTION
    establishment input violates the accepted operation contract

AUTHORISATION_REJECTION
    initiating principal lacks required authority

CONFLICT
    an independently governed uniqueness/invariant condition is violated

TECHNICAL_FAILURE_BEFORE_COMMIT
    no new Merchant Account was established

SUCCESS
    Merchant Account establishment committed

ACKNOWLEDGEMENT_LOSS_AFTER_COMMIT
    Merchant Account remains established;
    retry follows the idempotency rule
```

A technical or transport failure after commit MUST NOT retroactively redefine the committed Merchant Account as nonexistent.

Generic `failed` MUST NOT erase these distinctions where caller behaviour differs.

---

## 12. Lifecycle scope

Within MS-PROT-071, the only governed establishment transition is:

```text
NONEXISTENT
    -- EstablishMerchantAccount -->
ESTABLISHED
```

`NONEXISTENT` is not a persisted Merchant Account status. It denotes the absence of a committed Merchant Account fact for the identity being considered.

MS-PROT-071 does NOT define `SUSPENDED`, `CLOSED`, `DELETED`, `TRANSFERRED`, `RESTORED` or equivalent terminal/administrative account lifecycle semantics.

Merchant/controller ownership transfer, suspension, closure and terminal account lifecycle remain a separately promoted design boundary in `DEFERRED-DECISION-REGISTER.md`.

---

## 13. Onboarding relationship

A Merchant Account MAY exist without onboarding having started or completed.

```text
Merchant Account established
    ≠ onboarding started
    ≠ onboarding completed
```

Onboarding MAY use `merchantIdentifier` as merchant scope and MAY react to `MerchantAccountEstablished` after commit.

Abandoning onboarding MUST NOT by itself erase or negate the committed Merchant Account existence fact.

---

## 14. Merchant Configuration relationship

A Merchant Account MAY exist with no authoritative Merchant Configuration.

```text
Merchant Account exists     YES
Merchant Configuration      NONE
```

Merchant Configuration validity, revision, approval, compilation, activation and supersession remain governed by MS-PROT-021, MS-PROT-022 and MS-PROT-040 and any later accepted amendments.

Merchant Account establishment MUST NOT activate semantic capabilities.

---

## 15. Initial trial relationship

MS-PROT-071 does not define the start event or eligibility rule for the initial full-experience trial.

Therefore:

```text
Merchant Account establishment
    DOES NOT IMPLY
initial trial establishment
```

The trial anchor MUST be resolved by a separately governed decision using an already-defined authoritative event or predicate. Implementation MUST NOT infer the trial anchor from account creation merely because an account-creation timestamp exists.

---

## 16. Trust and verification relationship

Merchant Account establishment and controller/business trust claims are separate facts.

```text
Merchant Account established
    ≠ controller verified
    ≠ business verified
```

MS-PROT-071 does not determine which trust claim, if any, is required before account establishment or later merchant operations. Applicable trust requirements remain governed by MS-PROT-028, MS-PROT-062, MS-PROT-063 and more specific accepted authorities.

Possession of an established Merchant Account MUST NOT itself satisfy a trust predicate.

---

## 17. Commercial relationship

Merchant Account existence MUST NOT imply a plan selection, Commercial Agreement, Commercial Entitlement, payment obligation or payment evidence.

Commercial authorities MAY reference `merchantIdentifier` after account establishment.

Commercial packaging MUST NOT become the authority for whether the Merchant Account exists.

---

## 18. Publication and Exposure relationship

Merchant Account establishment MUST NOT publish or expose a merchant storefront or other merchant surface.

```text
Merchant Account established
    ≠ publication committed
    ≠ Exposure permitted
```

Publication and Exposure remain governed by their owning authorities.

---

## 19. Provider relationship

No external business provider owns or determines Merchant Account existence.

An identity provider MAY participate in establishing the initiating principal under the authentication boundary, but provider account identity MUST NOT become Merchant Account identity.

Provider unavailability after a successful Merchant Account commit MUST NOT rewrite the Merchant Account existence fact.

---

## 20. AI relationship

AI MUST NOT autonomously establish Merchant Accounts, assign Merchant Account identity, infer that Merchant Account existence is authoritative, merge Merchant Accounts, or redefine identity based on attribute similarity.

AI MAY assist non-authoritative onboarding/registration interaction only within separately accepted AI boundaries.

---

## 21. MerchantAccountEstablished event

Where Main Street exposes a cross-capability event for successful account establishment, the canonical event is `MerchantAccountEstablished`.

### Source owner

Merchant Account context.

### Fact already true

The referenced Merchant Account has successfully committed and exists authoritatively.

### Commit timing

The event MUST represent a post-commit fact. It MUST NOT be published as proof of establishment before the establishment transaction commits.

### Minimum semantic reference

```text
merchantIdentifier
```

Additional event metadata MAY be included when separately governed and MUST NOT create new business authority.

### Delivery/replay semantics

Consumers MUST tolerate duplicate delivery/replay where the event transport can redeliver. Consumer reactions MUST protect their own duplicate-sensitive effects.

Failure of an event consumer MUST NOT undo or redefine the already-committed Merchant Account establishment.

### Consumer ownership

Consumers MAY begin onboarding support, analytics, projection or other post-commit work. Consumers MUST NOT become co-owners of Merchant Account existence.

---

## 22. Projection relationship

Merchant-facing or administrative surfaces MAY project account existence and related independently owned facts.

A projection label such as `setup incomplete`, `ready`, `active` or `trial` MUST NOT become Merchant Account lifecycle authority unless a separate accepted authority defines that term and predicate.

---

## 23. Explicit non-responsibilities

MS-PROT-071 does NOT define:

- business-name uniqueness;
- controller-to-Merchant-Account cardinality;
- repeat-trial eligibility or abuse prevention;
- trial start;
- subscription selection or Commercial Agreement lifecycle;
- Merchant Configuration approval or activation;
- identity-verification completion;
- business legitimacy verification;
- provider connection lifecycle;
- storefront publication;
- staff/team membership;
- merchant/controller ownership transfer;
- merchant suspension or closure;
- terminal account deletion/retention behaviour;
- persistence technology;
- identifier generation algorithm;
- database locking/idempotency-table mechanism;
- transport/API representation.

Where later implementation requires any unresolved material semantic item above, the item MUST return to governed design unless another accepted authority already resolves it.

---

## 24. Falsification evidence

The accepted model was tested against the following cases.

### Abandoned onboarding

A merchant establishes an account and abandons onboarding. The Merchant Account remains established while Merchant Configuration may remain absent. No competing lifecycle fact is required.

**PASS**

### Multiple merchant contexts under one controller

A controller submits distinct logical establishment requests. MS-PROT-071 does not silently prohibit multiple Merchant Accounts based on controller similarity. Any cardinality restriction requires separate authority.

**PASS**

### Lost network acknowledgement

Establishment commits and the response is lost. Retrying the same logical request resolves to the already-established Merchant Account rather than multiplying authoritative effect.

**PASS**

### Concurrent duplicate submission

Two executions race for the same logical request identity. At most one Merchant Account may be established for that logical request.

**PASS**

### AI detects similar businesses

AI similarity evidence cannot merge Merchant Accounts or redefine their durable identities.

**PASS**

### Identity/provider outage after commit

Loss of provider availability does not erase the committed Merchant Account existence fact.

**PASS**

### Configuration compilation rejection

A Merchant Configuration may later fail compilation without negating Merchant Account existence.

**PASS**

### No subscription

A Merchant Account may exist without a subscription or Commercial Entitlement because commercial packaging does not own tenancy existence.

**PASS**

No tested scenario requires onboarding, configuration, subscription, provider or AI authority to co-own Merchant Account existence.

---

## 25. Trade-off decision

Two principal models were reviewed:

```text
A. Thin Merchant Account tenancy authority

B. Giant merchant lifecycle aggregate containing onboarding,
   verification, configuration, subscription and publication
```

Model A is accepted.

Model A preserves single semantic ownership and the composite architecture. Its accepted cost is that application orchestration and projections must compose independently owned facts when presenting merchant progress.

Model B is rejected because it would couple independent lifecycles, duplicate neighbouring authority and create a broad merchant status that obscures configuration/runtime/commercial/trust distinctions.

Revisit this decision only if implementation or domain evidence demonstrates an atomic invariant that genuinely requires Merchant Account existence and a neighbouring authoritative lifecycle fact to share one owner and transaction boundary.

---

## 26. Hard invariants

1. Merchant Account existence has exactly one semantic owner: the Merchant Account context.
2. Every Merchant Account has one immutable Main Street-issued `merchantIdentifier`.
3. Merchant Account equality is determined by `merchantIdentifier`, not attribute similarity.
4. `EstablishMerchantAccount` MUST NOT depend on Merchant Configuration or subscription entitlement.
5. Identifier establishment and Merchant Account existence commit form one atomic invariant.
6. The same logical establishment request MUST NOT create more than one Merchant Account.
7. Lost acknowledgement after commit MUST NOT multiply Merchant Account establishment.
8. Onboarding, configuration, trust, commercial state, publication, provider readiness and operational eligibility MUST remain distinct from Merchant Account existence.
9. Merchant Account establishment MUST NOT activate semantic capabilities.
10. Merchant Account establishment MUST NOT start the initial trial unless a later accepted authority explicitly selects that event as the trial anchor.
11. Merchant Account establishment MUST NOT itself satisfy controller/business verification.
12. Merchant Account establishment MUST NOT publish a storefront or grant Exposure.
13. Provider failure MUST NOT rewrite an already-committed Merchant Account existence fact.
14. AI MUST NOT own Merchant Account establishment or identity.
15. `MerchantAccountEstablished`, when emitted, MUST describe a fact already committed.
16. Event-consumer failure MUST NOT undo Merchant Account establishment.
17. Merchant suspension, closure, transfer and terminal lifecycle remain outside MS-PROT-071.

---

## 27. Acceptance statement

Main Street now has a bounded tenancy-establishment authority in which Merchant Account existence is durable, merchant-scoped, idempotent and independent of onboarding, configuration, trust, commercial packaging, publication and provider state.

> **Establish the merchant tenancy once; let each neighbouring authority own its own truth.**
