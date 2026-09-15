# IMP-07 Publication Lifecycle / Currentness Conformance

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Evidence basis:** `development@b0ff9a27ee0b04a675408e8c1eb0219b831d875b`  
**Scoped result:** **CONFORMING_COMPLETE — Publication domain lifecycle/currentness foundation only**

This record is implementation evidence only. It creates no semantic, architectural or programme authority.

---

## 1. Governing authority

The implemented scope is constrained by:

- composite `MS-PROT-046 v1.1 + v1.2` Publication authority;
- `MS-PROT-059 v1.0` logical-operation/idempotency and channel-convergence authority where applicable;
- the already-complete IMP-06 Projection / Exposure / Surface / API spine;
- `MS-IMP-001`; and
- `IMPLEMENTATION-RULES.md`.

The accepted Publication rules relevant to this evidence require stable merchant-scoped Publication identity, distinct current and published revision identity, expected-current conflict on material mutation, the exact `DRAFT | PUBLISHED | WITHDRAWN` lifecycle, explicit republish, preservation of publication evidence, and separation of Publication lifecycle from Exposure.

---

## 2. Implementation evidence

Production implementation:

```text
src/main/java/mainstreet/publication/OpportunityPublicationState.java
src/main/java/mainstreet/publication/PublicationLifecycle.java
src/main/java/mainstreet/publication/PublicationRevisionConflictException.java
```

Verification suites:

```text
src/test/java/mainstreet/publication/OpportunityPublicationStateTest.java
src/test/java/mainstreet/publication/OpportunityPublicationStateAdversarialTest.java
```

The implementation proves within this domain-state boundary that:

1. the lifecycle vocabulary is exactly `DRAFT`, `PUBLISHED`, `WITHDRAWN`;
2. Publication state is merchant-scoped and has stable Opportunity identity distinct from revision identity;
3. publish binds the exact current revision;
4. a material revise advances current revision identity and rejects stale expected-current evidence;
5. revising a PUBLISHED Opportunity does not silently withdraw it and does not silently publish the new revision;
6. withdrawal requires the current revision even when the last published revision is older;
7. withdrawal preserves the last published revision as evidence;
8. only explicit republish restores a WITHDRAWN Opportunity to PUBLISHED;
9. stale expected-current evidence fails for revise, publish, withdraw and republish;
10. invalid lifecycle entry points fail closed;
11. structurally impossible lifecycle/evidence combinations are rejected; and
12. the domain state remains isolated from Repository, Provider, Exposure, participation, Enquiry, Authorisation and Availability concerns.

---

## 3. Adversarial / falsification result

The domain foundation survives the currently applicable falsification cases:

```text
stale revise based on R1 when current is R2                 REJECTED
stale publish based on R1 when current is R2                REJECTED
PUBLISHED R1 → revise R2 → withdraw expecting R1            REJECTED
PUBLISHED R1 → revise R2 → withdraw expecting R2            ACCEPTED; published evidence remains R1
DRAFT → withdraw                                             REJECTED
DRAFT → republish                                            REJECTED
PUBLISHED → republish                                        REJECTED
WITHDRAWN → ordinary publish                                 REJECTED
same revision identity used as a material revision advance  REJECTED
DRAFT carrying published-revision evidence                  REJECTED
PUBLISHED/WITHDRAWN without published-revision evidence     REJECTED
```

No test or implementation result permits silent last-write-wins semantics at this in-memory state boundary.

---

## 4. Verification evidence

Publication currentness code last changed at:

`34ceb30a88fa7363283249a90a6f131dfb303175` — `fix(imp-07): enforce withdrawal revision currentness`

GitHub Actions:

```text
run 33822343422
head 34ceb30a88fa7363283249a90a6f131dfb303175
workflow Maven Tests
result SUCCESS
```

The current development head subsequently passed the same workflow without changing Publication code:

```text
run 33823654486
head b0ff9a27ee0b04a675408e8c1eb0219b831d875b
workflow Maven Tests
result SUCCESS
```

---

## 5. Explicit non-claims

This evidence does **not** prove:

```text
immutable durable material revision history
historical representation reconstructibility
PostgreSQL current-pointer persistence
transactional compare-and-set / optimistic concurrency
concurrent-writer race safety
logical-operation idempotency persistence
create/revise/publish/withdraw/republish application orchestration
publication/withdrawal transition history persistence
publishFrom / publishUntil persistence or evaluation
Publication-owned Exposure contract registration/evaluation
request-scoped public Publication projection
Opportunity actionability
Opportunity → enquiry/send-enquiry participation source
Enquiry submission or subject provenance
merchant Enquiry observation
public or merchant concrete transport adapters
end-to-end IMP-07 vertical-slice completion
```

In particular, in-memory expected-current checks are not treated as proof of production concurrency control.

---

## 6. Conformance conclusion

```text
IMP-07-P1 — Opportunity Publication lifecycle/currentness domain foundation
    CONFORMING_COMPLETE

IMP-07 macro target
    IN_PROGRESS
```

The next node must be selected from a refreshed fine-grained dependency graph. No downstream persistence, Exposure or Enquiry work becomes complete merely because P1 is complete.
