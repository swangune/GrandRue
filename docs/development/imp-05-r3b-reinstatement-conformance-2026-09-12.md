# IMP-05-R3B Configuration Reinstatement Conformance — 12 September 2026

## Classification

- Document type: implementation conformance and graph-refresh evidence.
- Programme target: `IMP-05 — Merchant Definition, Configuration & Activation`.
- Completed node: `IMP-05-R3B — Reinstatement composition`.
- Completion propagation: `IMP-05-R3`, `IMP-05-D5`, `IMP-05-D` and `IMP-05` become `CONFORMING_COMPLETE`.
- Instructed branch: `development`.
- Code-bearing starting head: `bce80962165796edb9f95a343c2eb006f5555a36`.
- Substantive authority: composite `MS-PROT-040` through accepted v1.8, specifically `designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision Affinity Amendment.md` §§5–16.
- Programme/execution authority: `designs/MS-IMP-001.md` plus the accepted v1.1 amendment, and `designs/IMPLEMENTATION-RULES.md` §52.

This record is navigation/evidence, not semantic authority. Its completion and graph states are effective only when the synchronized cycle-closing tree passes the exact full gate below.

## Implemented decision path

A Configuration revision that was active previously, is not current, and is selected while another activation owns the current pointer is classified as a reinstatement before historical base affinity is considered. Reinstatement is a new current decision. It requires:

1. the exact activation request currently owning the merchant's Configuration pointer as the Reinstatement Basis Activation;
2. fresh successful validation and exact requirement-package evidence affined to that basis;
3. a fresh complete impact review over that same validation, package and basis;
4. a new approval by the current ACTIVE Controller Relationship under a trusted authenticated principal;
5. exact current applicability of that approval at activation time;
6. the ordinary shared compatibility and serving-admission checks;
7. the existing merchant activation lock and current-pointer compare-and-set ordering; and
8. a new immutable activation/publication-intent fact followed by atomic pointer movement.

Historical approval remains historical. A committed historical activation request is replayable only while it still owns the current pointer; it cannot be replayed to reinstate a revision after a later activation.

## Material implementation

- `src/main/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationReleaseActivation.java` — exact classification, basis capture, bounded approval selection and the shared admission/concurrency transaction path. Stable trace anchor: MS-PROT-040 v1.8 §§10–12 and 14.
- `src/main/java/mainstreet/infrastructure/persistence/configuration/configuration/JooqConfigurationReinstatementApprovalAuthority.java` — fresh basis-affined approval writing and exact current-applicability query. Stable trace anchor: MS-PROT-040 v1.8 §§7, 9 and 14–16.
- `src/main/java/mainstreet/semantic/configuration/ConfigurationReinstatementApprovalApplicabilityAuthority.java` — bounded activation-time read contract. Stable trace anchor: MS-PROT-040 v1.8 §11.
- Configuration validation and impact-review commands, evidence values, application composition and JOOQ authorities — optional exact Reinstatement Basis Activation affinity is carried end to end while ordinary initial/replacement evidence retains absent affinity.
- `src/main/java/mainstreet/semantic/configuration/InMemoryConfigurationReleaseActivation.java` — committed-request replay now remains current-pointer bounded, matching the durable authority.
- `src/main/resources/db/migration/V64__configuration__bind_reinstatement_decision_to_activation_basis.sql` — optional nonblank same-merchant foreign-key affinity from validation, impact review and reinstatement approval to an immutable Configuration activation request, without historical backfill.
- The test-only JOOQ activation shadow was removed so integration tests exercise the production activation authority.

## Verification and falsification

Primary proof is in:

- `src/test/java/mainstreet/infrastructure/persistence/configuration/JooqReplacementConfigurationReleaseActivationIT.java`;
- `src/test/java/mainstreet/infrastructure/persistence/configuration/configuration/JooqConfigurationReinstatementApprovalAuthorityIT.java`;
- `src/test/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationValidationEvidenceAuthorityIT.java`;
- `src/test/java/mainstreet/infrastructure/persistence/configuration/JooqConfigurationImpactReviewEvidenceAuthorityIT.java`;
- `src/test/java/mainstreet/semantic/configuration/ConfigurationReinstatementEvidenceAffinityTest.java`; and
- the existing lifecycle/application unit suites updated for the accepted affinity contract.

The v1.8 falsification set was applied as follows:

| Counterexample | Result |
| --- | --- |
| F1 historical initial approval authorizes reinstatement | Rejected; reinstatement uses only the bounded reinstatement-approval authority. |
| F2 superseded initial revision can never be reinstated | Falsified; fresh basis-affined evidence and approval successfully reinstate it. |
| F3 an old same-revision approval revives in a later cycle | Rejected by exact Reinstatement Basis Activation affinity. |
| F4 an old forward-replacement approval is sufficient | Rejected by classification-specific approval selection. |
| F5 Controller transfer leaves an old approval applicable | Rejected by exact current Controller Relationship applicability. |
| F6 approval and activation can observe different current bases | Prevented by the shared merchant activation lock then current-authority lock order. |
| F7 pointer movement after approval is harmless | Rejected; exact current basis/pointer affinity is revalidated at activation. |
| F8 validation, impact or package from another basis can be mixed | Rejected by exact stored affinity and approval-time equality checks. |
| F9 any non-current revision is a reinstatement | Rejected; a never-active non-current target follows forward-base rules or conflicts. |
| F10 internal approval/basis terms leak into public contracts | No public transport or Exposure contract was widened. |
| F11 historical activation replay performs reinstatement | Rejected unless the exact committed request still owns the current pointer. |
| F12 reinstatement bypasses ordinary compatibility/admission | Rejected; reinstatement joins the unconditional shared compatibility and serving-admission path. |

The first complete PostgreSQL 18 run exposed seven fixture-order errors: the reinstatement requirement set was inserted before its newly required validation-evidence foreign key existed. The other 412 integration tests passed. Moving only that fixture insert behind production validation recording corrected the setup; the focused seven-test activation suite then passed. This was a fixture defect, not a relaxation of the database invariant.

The repaired pre-synchronization full gate ran against PostgreSQL 18.6 and Flyway V64:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Observed result: **BUILD SUCCESS** — 1,226 unit/governance tests and 419 PostgreSQL integration tests; zero failures, zero errors and zero skipped. The same exact gate is required again after this evidence, graph, status and governance-test synchronization; the cycle must not commit if it fails.

## Scope and non-claims

This closes the missing general Configuration reinstatement composition and therefore the corrected IMP-05 gap. It does not implement rollback, mutate historical activations, revive historical approval, broaden initial/non-initial approval authority, delegate Controller authority, create a public contract, deploy, promote, push or create a branch.

IMP-06 and IMP-07 regain `CONFORMING_COMPLETE` macro eligibility from their retained, previously verified closure evidence once IMP-05 is complete; no new IMP-06/07 implementation is claimed. Their restored programme-gate chain makes IMP-08A and IMP-08B `READY` and restores IMP-08C to `IN_PROGRESS`. `IMP-07-P6` remains separately blocked on authoritative Opportunity actionability and does not reopen the bounded IMP-07 vertical-slice closure.

The smallest previously decomposed useful frontier is restored as `IMP-08C-C4D2B — trusted reaction composition`, with its exact owner/source/principal/current-authority and effect/acknowledgement-recovery obligations unchanged. No universal dependency on DurableWorkInstruction or WorkAttempt is invented; MS-PROT-065 v1.1 §38 remains controlling.
