# IMP-05-R4B — Enquiry new-activity impact increment

**Rules:** IMPLEMENTATION-RULES v1.7. **Node state:** IN_PROGRESS. **Entry baseline:** `development@02ccee0725b6a6966c62884fabf9f82295117401`, confirmed current before this increment. This record is a bounded R4B progress checkpoint; it does not complete R4B, R4, R4C or IMP-05.

## Exact authority

- `designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md`, v1.0 §§20–22 impact analysis/classification and §26 capability deactivation.
- `designs/MS-PROT-043 v1.4 — Production Enquiry Submission, Provenance & Merchant Observation Amendment.md`, §§2–6 canonical submission, merchant-general/subject-specific Enquiry, untrusted subject references and current-participation revalidation.
- `designs/MS-PROT-049 v1.2 — Public Interaction Subject Participation & Binding Amendment.md`, §4 interaction contribution versus subject binding and §§17–18 execution revalidation/stale-binding behaviour.
- `designs/IMPLEMENTATION-RULES.md`, v1.7 §§30, 46–48 and §§52.14–52.19.

## Implemented responsibility and ownership

`src/main/java/mainstreet/enquiry/EnquiryAvailabilityImpactAssessment.java`, `assess`: compare whether the Enquiry capability is active in the exact historical base compiled under its pinned semantic-registry release and in the candidate's already resolved executable model. Direct selection names are not treated as capability truth because registered dependency closure may differ between releases.

A membership change produces one bounded CONSEQUENTIAL business effect for **new Enquiry activity**. Enabling says that new customer enquiries can be accepted through Main Street only where an applicable Enquiry interaction is available. Disabling says that new customer enquiries will no longer be accepted through Main Street. The assessment does not infer subject participation, manufacture a Public Interaction Binding, claim current availability, mutate Enquiry data, or state that durable Enquiry history is deleted by deactivation.

Paradigm fit: Configuration supplies immutable exact-result context; the compiler owns static dependency closure; Enquiry owns the business meaning of Enquiry capability membership. Existing Enquiry submission/binding authorities remain independent. No policy meaning, database schema, provider integration or library is introduced.

## Tests-first and falsification scope

`src/test/java/mainstreet/enquiry/EnquiryAvailabilityImpactAssessmentTest.java` covers:

1. initial Enquiry enablement without invented subject binding/availability;
2. deactivation without claiming existing Enquiry history is deleted;
3. unchanged active membership without invented effects;
4. both directions of release-dependent dependency closure while direct merchant selections remain the same; and
5. failure when the historical semantic-registry release is unavailable rather than treating the base as inactive.

Tests-first RED commit `56aa7f1432dfbb2b0233837f31f0d462168b2b08`, GitHub Actions run `34033343480`: the configured Maven/PostgreSQL verification failed at the tests-only stage before the production assessment existed. Minimum implementation commit `789838a3433eb53ce16b207fff7b3f52b66868fa`, run `34033354486`: SUCCESS. During traceability review, the Enquiry authority anchor was found to point to a non-existent §33 in the v1.4 amendment; corrected code/test anchors now point to §§2–6 at commit `26fae71341b64fe469dae55c77df329e7eab9483`.

Corrected-head Java 25/PostgreSQL 18 verification: commit `26fae71341b64fe469dae55c77df329e7eab9483`, GitHub Actions run `34033603657`, job `101487558600`: **SUCCESS**. The workflow executed the configured `mvn --batch-mode clean verify -Ppostgres-it` gate and its unit/PostgreSQL integration-test step completed successfully. This verifies the bounded Enquiry increment with corrected trace anchors; it does not certify remaining R4B coverage.

## Counterevidence and remaining R4B gap

- Comparing selected capability identifiers would miss dependency-induced Enquiry activation/deactivation; the release-dependent regression covers this counterexample.
- Enquiry capability membership is not subject-participation authority. The assessment therefore does not infer `subject → send-enquiry` from Enquiry activation.
- A Public Interaction Binding is not execution authority or current availability. No binding/availability claim is produced by this assessment.
- Disabling new Enquiry activity does not erase already durable Enquiries or their submission provenance.
- This increment covers Enquiry new-activity membership only. Production policy-owner interpretations, other capability effects, binding/routing effects and applicable owner-backed commitment conflicts remain unfinished R4B work.
- R4C durable review integration remains blocked on complete R4B coverage. No R4B, R4 or IMP-05 completion is claimed.
