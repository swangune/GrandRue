# Customer Context Surface Eligibility — Implementation & Conformance Evidence

**Date:** 26 August 2026  
**Authority:** MS-PROT-049 v1.3 — Customer Context & Relationship Surface Eligibility Amendment  
**Status:** Non-authoritative implementation/conformance evidence

## 1. Governed design trace

MS-PROT-049 v1.3 was manually approved after the DESIGN-RULES lifecycle was completed in chat: business/domain problem, proposal, authority review, cross-domain falsification, second-order falsification, alternatives/trade-offs and recommendation.

The accepted authority is indexed by AUTHORITY-INDEX.md v3.13.

No Deferred Decision Register item was opened or closed by this amendment; the current DDR has no matching unresolved row. IMPLEMENTATION-RULES requires no semantic/process amendment for this slice.

## 2. Implemented semantic boundary

The Surface implementation supports a release-affined registered, owner-qualified `CustomerSurfaceEligibilityRequirementIdentity` for CUSTOMER contributions and preserves the requirement reference through static Surface materialisation.

`CustomerContextualSurfaceResolver` resolves one merchant-scoped CUSTOMER Surface from:

```text
registered CUSTOMER Surface contribution
+
registered owner-qualified customer eligibility requirement
+
current TrustedExecutionContext / MerchantScope
+
owner-routed CustomerSurfaceEligibilityAuthority decision
+
Projection Serviceability
+
Customer Surface Exposure
        ↓
contextual CUSTOMER Surface composition
```

The resolver deliberately keeps these dimensions separate:

```text
Customer Surface Eligibility
    != CustomerAccount authentication
    != CustomerContext existence
    != Projection Serviceability
    != Exposure
    != execution authority
```

Missing requirement references, unregistered requirement definitions, missing owner evaluators, merchant-scope mismatch, null/unknown owner decisions and negative eligibility fail closed.

## 3. Implemented contracts

Implemented Surface contracts include:

- `CustomerSurfaceEligibilityRequirementIdentity`
- `CustomerSurfaceEligibilityRequirementDefinition`
- `CustomerSurfaceEligibilityAuthority`
- `CustomerSurfaceEligibilityRequirementEvaluator`
- `CustomerSurfaceEligibilityRequirementBinding`
- `CompositeCustomerSurfaceEligibilityAuthority`
- `CustomerSurfaceExposureAuthority`
- `CustomerSurfaceResolutionContext`
- `SurfaceContributionDefinition.customerEligibilityRequirement`
- `StaticSurfaceContribution.customerEligibilityRequirement`
- `SurfaceContributionRegistrySnapshot.customerEligibilityRequirementDefinitions`
- registry validation that every referenced CUSTOMER eligibility requirement is registered in the same release-affined Surface registry snapshot
- registry materialisation of the requirement reference
- `CustomerRelationshipSurfaceEligibilityEvidence`
- `CustomerContextualSurfaceResolver`

The owner-routing composite owns no customer relationship semantics. It routes an owner-qualified requirement to that owner's evaluator. A missing evaluator fails closed for that requirement without blocking an unrelated owner's eligibility decision.

No production Booking, Appointment or Ordering customer-relationship requirement identifier has been introduced by this infrastructure slice. Identifiers such as `booking / related-customer-booking` remain synthetic test examples because MS-PROT-049 v1.3 explicitly requires the applicable owning relationship authority before such concrete production registration.

## 4. RED / conformance tests

`CustomerContextualSurfaceResolverTest` specifies at least the following behaviours:

- only CUSTOMER candidates with a satisfied registered owner-qualified requirement are included;
- a CUSTOMER contribution without its required relationship contract fails closed;
- an authenticated principal does not bypass missing/unestablished relationship evidence;
- Projection Serviceability and Exposure can restrict but cannot create CUSTOMER Surface membership;
- contextual relationship evidence retains the satisfied owner-qualified requirement identity;
- non-CUSTOMER candidates are ignored;
- cross-merchant resolution is rejected;
- semantic-registry release mismatch is rejected;
- empty/unknown current eligibility is treated explicitly as fail-closed.

`SurfaceContributionRegistryCustomerEligibilityTest` specifies:

- a CUSTOMER contribution referencing an unregistered requirement is rejected at registry construction;
- the same contribution is accepted when the owner-qualified requirement definition is registered in the release-affined Surface registry.

`CompositeCustomerSurfaceEligibilityAuthorityTest` specifies:

- owner-qualified requirements route only to the matching owner evaluator;
- merchant scope, trusted execution context and bounded request context are preserved;
- a missing owner evaluator fails closed without invoking or blocking an unrelated owner;
- merchant-scope mismatch fails closed;
- a null evaluator decision is normalised to fail-closed unknown;
- duplicate owner bindings are rejected.

## 5. Public/contextual companion foundation

The repository also contains `PublicContextualSurfaceResolver`, which independently applies Projection Serviceability and PUBLIC Exposure to already-registered PUBLIC candidates. Projection/Exposure may restrict membership but cannot create a contribution or grant execution authority.

This corrects older implementation-status text that described PUBLIC Exposure and projection filtering as wholly unimplemented.

## 6. Governance repair

During post-approval governance verification, an attempted CANONICAL-SEMANTIC-LEXICON v1.9 update was found to have mechanically truncated unrelated accepted tail sections. The corrupted update was not retained as current governance. The complete prior accepted lexicon v1.8 was restored without rewriting history.

MS-PROT-049 v1.3 remains self-sufficient substantive authority, and AUTHORITY-INDEX.md v3.13 remains the navigation authority for the amendment.

## 7. Verification evidence

The first CUSTOMER contextual resolver/governance slice was repository-wide green at commit `6427524c01a8c11e1d1ab1a3b3fbfc5d61f1878a`, GitHub Actions Maven Tests run `32984980381` (run number 954), executing:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Result: `BUILD SUCCESS`.

The later static-registration and owner-routing hardening commits are descendants of that green baseline. Their final repository-wide verification must be taken from a Maven run on the hardening head or a descendant; intermediate GitHub Actions `startup_failure`/queue states before a job is allocated are infrastructure scheduling outcomes and are not treated as code-test failures or successes.

## 8. Remaining frontier

The generic CUSTOMER Surface eligibility infrastructure is now implemented, but MS-PROT-049 remains partial overall. Remaining work includes:

- production capability/context-owned customer relationship evaluators where separately accepted relationship authority permits concrete requirement semantics;
- production registration of CUSTOMER contributions only after those owner contracts exist;
- guest/customer authentication or secure contextual-access delivery adapters;
- complete customer-facing HTTP/frontend delivery and presentation;
- broader capability-specific projection contracts/read models where required;
- other Surface/provider adapters outside this slice.

A broad `Proceed` instruction does not authorise inventing a concrete Booking/Appointment/Ordering customer relationship contract where accepted authority has not already defined it.
