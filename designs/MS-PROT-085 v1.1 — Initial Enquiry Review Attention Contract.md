# MS-PROT-085 v1.1 — Initial Enquiry Review Attention Contract

**Document ID:** MS-PROT-085  
**Version:** 1.1  
**Status:** ACCEPTED  
**Approved:** 8 September 2026 by explicit manual approval  
**Authority type:** Attention portfolio and contract amendment  
**Governed by:** `designs/DESIGN-RULES.md` v2.2; MS-FUNDAMENTAL-VISION-001  
**Depends on:** MS-PROT-085 v1.0; composite MS-PROT-043 through v1.4; composite MS-PROT-027, MS-PROT-053, MS-PROT-062, MS-PROT-063 and MS-PROT-065  
**Amends:** MS-PROT-085 Section 39 within initial portfolio selection only  
**Resolves:** `MS-PROT-085-DQ-001`, through selection of the bounded portfolio below  
**Purpose:** Give merchants a durable indication that a newly accepted Enquiry needs review without claiming that review answers the customer or resolves the underlying request.

## 1. Governing decision

The initial Attention portfolio SHALL contain exactly one contract:

`enquiry / initial-submission-review@1`

The contract concerns **review of a newly accepted Enquiry**, not mandatory customer response or request resolution.

All other source families remain outside this initial portfolio. Acceptance SHALL NOT activate Conversation, Reconciliation, Business Recommendation, regulatory, provider-degradation or commitment-exception contributions.

Every MS-PROT-085 v1.0 rule remains unchanged except its statement that no initial portfolio has been selected.

## 2. Problem and scope

Accepted Enquiry authority preserves what a customer asked, but deliberately does not own merchant work state.

A merchant needs to distinguish:

- an Enquiry awaiting initial review;
- an Enquiry whose initial review has been recorded; and
- Enquiry evidence that cannot currently be evaluated or shown.

Neither Notification delivery nor merely opening an Enquiry reliably establishes that initial review has occurred.

This amendment governs that distinction only.

It does not introduce response deadlines, service-level agreements, customer satisfaction, sales conversion, spam classification, team routing or a ticket lifecycle.

## 3. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY.**

This slice satisfies the Coordination and Administrative-Compression tests. A sole trader can see “New enquiry” and explicitly record “Reviewed” without configuring queues, routing or workflow states.

Internal occurrence identity, durable reaction and concurrency checks are justified by retry safety and truthful history.

Provider integration cannot own this fact: an email provider can report delivery, but cannot establish the merchant’s review of an Enquiry.

## 4. Contract definition

| Contract field | Required meaning |
|---|---|
| Family and version | `enquiry / initial-submission-review@1` |
| Source owner | Enquiry |
| Source subject | One accepted Enquiry in one exact Merchant Scope |
| Activation | Initial acceptance of that Enquiry |
| Activation identity | The Enquiry’s durable identity, qualified as initial-submission review |
| Recurrence | Exactly one initial-review episode per Enquiry |
| Required handling | Explicit merchant recording that initial review occurred |
| Permitted handling | Acknowledge; record initial review |
| Assignment | Not permitted by this contract |
| Snooze | Not permitted by this contract |
| Source-operation Candidate Actions | None |
| External-execution Candidate Actions | None |
| Notification policy | This contract creates no Notification responsibility |
| Deadline and urgency | None established |

A source link used to observe the Enquiry is navigation, not an executable Candidate Action.

A later independent Enquiry creates its own episode. Follow-up Messages do not reopen this episode or create another occurrence under this contract.

## 5. Applicability and activation boundary

The contract SHALL participate only when:

1. the serving semantic release includes this exact supported contract;
2. Enquiry is applicable under accepted Merchant Configuration;
3. the initial-review family is activated through accepted configuration and activation authority; and
4. the Enquiry was accepted while that activation was effective.

The effective activation boundary SHALL be established by authoritative activation ordering, not by comparing independently reported client timestamps.

This contract does not infer consent to activation from an existing Enquiry capability. Activation remains subject to the existing configuration approval process.

The initial portfolio SHALL NOT backfill Enquiries accepted before its activation. Observation SHALL communicate that coverage boundary.

Deactivation prevents qualification of new Enquiries. It does not delete existing occurrences or handling evidence. Existing occurrences retain historical contract affinity and remain subject to current access and residual-management authority.

## 6. Contribution and establishment

After Enquiry acceptance commits, a durable Enquiry-owned reaction SHALL request Attention evaluation.

The contribution SHALL contain:

- exact Merchant Scope;
- durable Enquiry identity;
- authoritative Enquiry acceptance evidence;
- evidence that the contract activation covered that acceptance;
- source provenance; and
- contract family/version.

Attention SHALL validate the contribution before establishment.

The occurrence’s logical identity SHALL be:

`Merchant Scope + contract family + Enquiry owner + Enquiry identity + initial-submission activation`

Occurrence establishment SHALL atomically enforce uniqueness of that identity.

Replayed contributions SHALL return or converge on the existing occurrence, including when it is already reviewed. Replay MUST NOT erase review or recreate outstanding work.

Attention failure SHALL NOT roll back Enquiry acceptance. Coverage remains degraded until the durable reaction converges.

## 7. Review evidence

This contract introduces one closed Attention disposition:

`INITIAL_REVIEW_RECORDED`

Its meaning is:

> An eligible actor explicitly recorded that they reviewed this Enquiry’s initial submission.

It does not establish:

- a response was sent;
- the customer received a response;
- the request was accepted;
- a booking, order or payment exists;
- the Enquiry was resolved; or
- the merchant has no remaining work concerning the customer.

Review SHALL require an explicit actor instruction. Opening a page, reading a Notification, navigating to the Enquiry or AI summarisation MUST NOT create this disposition.

## 8. Actor and observation authority

Initial handlers SHALL be current Merchant Controllers who also satisfy the applicable Enquiry observation, data-use and Exposure requirements.

This portfolio does not add staff privileges or infer them from Enquiry read access. Staff handling and assignment require later accepted portfolio extension.

Observation and handling SHALL revalidate:

- trusted exact Merchant Scope;
- current Controller authority;
- applicable Actor Authorisation;
- applicable Commercial Entitlement;
- source-data use authority; and
- serviceability and Exposure of the necessary submission content.

None of those checks substitutes for another.

If required content cannot be lawfully shown, the system MUST NOT offer an actionable “Reviewed” control.

## 9. Handling operation contract

`RecordInitialEnquiryReview` is an Attention-owned operation.

**Inputs:** Exact occurrence identity, expected handling revision and logical operation identity.

**Reads:** Occurrence, captured contract, current handling revision, current actor authority and required source serviceability/access evidence.

**Writes:** One immutable `INITIAL_REVIEW_RECORDED` Attention Handling Fact and the corresponding handling revision.

**Atomicity:** Expected-revision validation, handling-fact append and revision advancement SHALL be atomic.

**Success:** Returns the committed handling result.

**Retry:** The same logical operation identity with the same intent returns the established result. Reuse for different intent conflicts.

**Concurrency:** A competing write against an outdated revision returns conflict; it MUST NOT silently overwrite history.

**Prohibited effect:** The operation MUST NOT mutate Enquiry or create a Message, Notification, customer relationship or business commitment.

Technical uncertainty after submission SHALL be reconciled using the same operation identity. It MUST NOT prompt a new logical operation automatically.

Acknowledgement follows MS-PROT-085 Section 14 and does not satisfy initial review.

## 10. Derived handling outcome

The current outcome SHALL be determined as follows:

| Evidence | Outcome |
|---|---|
| Qualifying occurrence; no review disposition; required evaluation serviceable | `REQUIRES_HANDLING` |
| Committed initial-review disposition | `HANDLING_SATISFIED` |
| Required current evaluation unavailable or contradictory | `UNRESOLVED` |

Access denial SHALL withhold protected representation; it MUST NOT be presented as source resolution.

This contract establishes no automatic `NO_LONGER_APPLICABLE` transition. Publication withdrawal, lack of customer contact information and Notification failure do not cancel an accepted Enquiry’s initial-review episode.

An established review fact remains historical truth if source detail subsequently becomes unavailable. Its continued representation remains subject to data-protection authority.

## 11. Coverage and presentation

Attention coverage SHALL identify:

- the initial-Enquiry-review source family;
- its activation boundary;
- whether qualifying acceptance reactions have converged; and
- source and projection serviceability.

An empty result MUST NOT mean “No customers are waiting” or “Everything is handled”.

Permitted business-language presentation includes:

- “New enquiry”;
- “Reviewed”; and
- “Some new enquiries may not be shown while updates are unavailable.”

No response deadline, urgency or customer identity verification may be inferred from the submitted text.

## 12. Failure and recovery

Failures SHALL preserve the classifications in MS-PROT-085 Section 32.

In particular:

- missing activation or source evidence prevents establishment;
- unavailable source evidence produces uncertainty, not absence;
- stale Controller authority rejects handling;
- stale expected revision conflicts;
- Notification failure has no effect on occurrence existence;
- duplicate source delivery converges; and
- deactivation does not destroy durable review history.

No provider call is required by this contract.

## 13. Hard invariants

1. One accepted qualifying Enquiry produces at most one initial-review occurrence.
2. Independent submissions remain independent even when their text or contact values match.
3. Opening, acknowledgement and Notification read do not record review.
4. Review is an explicit Attention-owned fact.
5. Review does not answer or resolve an Enquiry.
6. Follow-up Messages do not reopen initial review.
7. Historical Enquiries are not silently backfilled.
8. Source failure cannot become a reassuring empty state.
9. Current Controller and source-access authority are required for handling.
10. Replay cannot undo review.
11. Concurrent handling cannot silently overwrite evidence.
12. No other Attention family is activated by this amendment.

## 14. Alternatives and trade-offs

**Rejected: every Enquiry requires a response.** Some are informational, lack a usable response path or have already been dealt with outside Main Street. That rule would manufacture an obligation.

**Rejected: opening means reviewed.** Navigation, interrupted sessions and accidental opens are insufficient evidence.

**Rejected: launch all Attention families together.** Each requires distinct source activation, recurrence and satisfaction semantics.

**Chosen:** A Controller-only, explicit initial-review slice.

The trade-off is deliberately limited functionality: no delegation, snooze or response tracking. It establishes a useful, testable first portfolio without requiring messaging-channel selection. A demonstrated need for team delegation or response obligations justifies a later scoped amendment.

## 15. Falsification and ambiguity review

| Counterexample | Result under the proposed contract |
|---|---|
| Enquiry commits; Attention reaction fails | Enquiry survives; coverage degrades; reaction retries |
| Same acceptance is delivered repeatedly | One occurrence |
| Customer independently asks the same question twice | Two occurrences |
| Merchant opens the enquiry accidentally | Review remains outstanding |
| Merchant records review without responding | Review satisfied; no response claim |
| Customer sends a later Message | Separate communication scope; no reopening |
| Two devices record review concurrently | Atomic revision check prevents silent overwrite |
| Controller authority ends before submission | Handling rejected |
| Source content becomes inaccessible | No actionable review control; protected detail withheld |
| Notification provider is offline | In-product occurrence remains |
| Contract is activated for an existing merchant | No silent historical backlog |
| Sole trader has little software capacity | “New enquiry” and “Reviewed”; no workflow setup |

The initial hypothesis—“all inbound enquiries need a reply; opening clears them”—fails the response-obligation and accidental-opening cases. The proposed contract removes both assumptions.

**Review conclusion:** No new source lifecycle, provider authority, AI authority or cross-capability mutation is required.

**Ambiguity-review conclusion:** Activation boundary, occurrence identity, review meaning, handler class, concurrency and coverage are explicitly bounded. Other source portfolios are excluded rather than implicitly specified.

## 16. Governance and implementation consequence

This approved amendment:

- resolves `MS-PROT-085-DQ-001` as selection of the initial one-family portfolio;
- replaces Section 39’s “no portfolio selected” statement with this exact selection;
- preserves other families as unselected future extensions;
- requires updates to the Authority Index, DDR and relevant terminology;
- requires an update to `SEQUENCE.md`’s status overlay; and
- preserves the existing implementation sequence.

Acceptance does not activate the contract for a merchant, implement it, deploy it or authorise another source family.

**Fundamental Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY  
**Recommendation:** ACCEPT  
**Manual approval:** GRANTED — 8 September 2026  
**Repository formalisation:** AUTHORISED  
**Implementation promotion:** NONE
