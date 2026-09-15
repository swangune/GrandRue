Document type: Implementation conformance / traceability evidence
Programme target: IMP-05 — Merchant Definition, Configuration & Activation
Branch: development
Session starting HEAD: fcead087423a70b27c6458d5e177e0142bc54919
Execution mode: User-applied local working-tree changes; repository writes were intentionally deferred by a session-only chat override
Authority formalised in this cycle: accepted composite MS-PROT-040 through v1.7
Full gate: mvn --batch-mode clean verify -Ppostgres-it
Final observed verification: 12 September 2026 04:33:55 +01:00 — BUILD SUCCESS
Observed suite: 420 tests, 0 failures, 0 errors, 0 skipped
PostgreSQL: 18.6
Flyway schema: V63; 63 migrations successfully validated; schema current

1. Purpose

This evidence closes the implementation/traceability work completed in the session after the 8 September IMP-05 impact-analysis frontier and records exactly what is and is not conforming.

It does not make IMP-05 conforming complete.

The verified session result is:

IMP-05-R4B    CONFORMING_COMPLETE
IMP-05-R4C    CONFORMING_COMPLETE
IMP-05-R4     CONFORMING_COMPLETE

IMP-05-R2     CONFORMING_COMPLETE

IMP-05-R3A    CONFORMING_COMPLETE
IMP-05-R3     PARTIALLY_CONFORMING

IMP-05-R3B    next implementation frontier
IMP-05        PARTIALLY_CONFORMING
2. R4 fine-graph correction

The earlier R4B frontier had become over-constrained by treating later macro owners as prerequisites of IMP-05.

Accepted MS-IMP-001 places, among other later-domain work:

Scheduling / Booking deeper availability semantics -> IMP-11
Offering / Product / Inventory expansion            -> IMP-12
Payment expansion                                   -> IMP-13

IMP-05 remains the Merchant Definition, Configuration & Activation foundation.

Therefore R4B is judged against the executable IMP-05 Configuration impact portfolio, not against production semantics that accepted macro ordering deliberately places later.

The correction does not weaken fail-closed behaviour.

Unknown changed capability or effective-policy ownership remains a failure rather than an empty business impact.

3. R4B verified owner portfolio

The production membership portfolio includes the currently implemented IMP-05 impact owners for:

Booking
Enquiry
Fulfilment
Inventory
Ordering
Payment
Publication
Scheduling

and preserves the generic:

CapabilityMembershipImpactAssessment
ResolvedPolicyImpactAssessment

coverage boundaries.

Membership analysis preflights changed capability ownership before owner execution and still invokes registered unchanged owners where residual-management assessment is required.

Changed effective policies without a concrete accepted owner interpretation fail closed rather than being converted into no effect.

No later macro-specific capability semantics are fabricated.

R4B conclusion

IMP-05-R4B = CONFORMING_COMPLETE for the current executable IMP-05 portfolio.

This does not claim that later IMP-11/12/13 owner semantics are already implemented.

4. R4C concrete composition

The session replaced the earlier arbitrary-assessment-list composition with a concrete cross-capability application composition.

The production path now establishes:

exact stored Configuration Revision
        ↓
exact Resolved Configuration Package
        ↓
exact successful validation evidence
        ↓
concrete complete current impact-assessment portfolio
        ↓
ConfigurationImpactAnalyzer
        ↓
exact durable business-facing impact-review evidence

The production service owns coordination only.

Participating capability owners retain business meaning.

Missing assessment ownership remains fail-closed.

The implementation no longer requires callers to fabricate an arbitrary List<ConfigurationImpactAssessment> for the real review path.

R4C conclusion

IMP-05-R4C = CONFORMING_COMPLETE.

Because R4A was already conforming and R4B is closed under the corrected current-portfolio interpretation:

IMP-05-R4 = CONFORMING_COMPLETE.

5. MS-PROT-040 v1.6 design escalation and R2

Implementation discovered that accepted MS-PROT-040 defined a concrete durable Controller approval only for the ordinary first Configuration.

The session entered the full DESIGN-RULES lifecycle.

The accepted v1.6 authority establishes the current MVP rule:

ordinary non-initial approval
    -> current active Merchant Controller
    -> exact trusted authenticated principal
    -> exact validation evidence
    -> exact impact-review evidence
    -> exact Resolved Configuration Package
    -> exact Controller Relationship affinity

Historical approval remains append-only but becomes inapplicable when the exact Controller Relationship is no longer current.

The same human later regaining control through another Controller Relationship does not revive the old approval.

Production proof

The session implemented a durable non-initial approval writer and current-applicability read boundary.

V63 changes exact approval uniqueness so a new Controller Relationship may create a new approval even where the Identity is the same person:

V63__configuration__bind_approval_uniqueness_to_controller_relationship.sql

Verified behaviour includes:

current authenticated Controller approval;
exact revision/validation/impact/package affinity;
rejection of initial-revision use through the non-initial writer;
rejection of unauthenticated or non-Controller principals;
blocking-impact rejection;
exact retry identity;
request-identity conflict;
historical approval non-revival after Controller Relationship replacement;
later valid approval by the same Identity under a new relationship; and
concurrent duplicate delivery producing one logical approval fact.
R2 conclusion

IMP-05-R2 = CONFORMING_COMPLETE.

6. Replacement activation / R3A

R3 replacement activation now carries the full currently applicable non-initial approval into activation.

This prevents the real serving-admission path from discarding exact approved package affinity.

Canonical replacement path:

exact published candidate
        ↓
trusted application context
        ↓
current account / Controller authority
        ↓
current active/base concurrency
        ↓
current applicable non-initial approval
        ↓
exact approved package
        ↓
semantic compatibility where required
        ↓
serving admission
        ↓
atomic activation

A reduced legacy approval that lacks exact package affinity cannot authorize a real replacement serving-admission decision.

7. Trusted activation actor authority

Implementation discovered that ConfigurationActivationAuthorizationAuthority previously accepted only asserted strings and had no real production actor composition.

The session entered the DESIGN-RULES lifecycle again.

Accepted MS-PROT-040 v1.7 establishes ordinary non-initial activation as a current-Controller operation under a trusted authenticated execution context.

Production composition now separates:

TrustedExecutionContext
    -> authentication / principal / Merchant Scope binding

MerchantAccountLifecycleStore
    -> OPEN/CLOSING/CLOSED truth
    -> effective Merchant Account Suspension truth
    -> current ACTIVE Controller Relationship truth

Configuration activation
    -> exact candidate/approval/concurrency/admission/commit truth

The current-Controller activation authorizer therefore consumes Merchant Account-owned read authority instead of duplicating Merchant Account persistence semantics.

8. Shared current-authority fence

Merchant Controller transfer, Merchant Account suspension/closure transitions, non-initial approval currentness and Configuration activation use the merchant-scoped advisory authority fence with seed 76.

The activation writer evaluates current actor/account authority only after acquiring:

activation-request fence
merchant activation fence
merchant current-authority fence (seed 76)

This prevents a stale Controller or newly suspended/closing Merchant Account from crossing the current-authority decision boundary underneath an activation commit.

The mechanism remains implementation architecture; the accepted observable current-authority semantics are MS-PROT-040 v1.6/v1.7 plus Merchant Account authority.

9. Lost acknowledgement

A previously committed activation request remains historical reconciliation.

Exact replay of the same logical request returns the committed activation rather than becoming a new mutation.

The trusted application boundary still requires the replaying context to match the original initiating principal and Merchant Scope.

Current Controller authority is not reinterpreted as a prerequisite for returning already committed historical evidence.

A request that did not previously commit must pass all current predicates on retry.

10. Final verification evidence

Observed final command:

mvn --batch-mode clean verify -Ppostgres-it

Observed environment/result:

PostgreSQL 18.6
Flyway schema version 63
63 migrations validated successfully
schema up to date

Tests run: 420
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
Total time: 03:52 min
Finished at: 2026-09-12T04:33:55+01:00

This verifies the local implementation working tree before the session-end documentation synchronization.

After applying the documentation synchronization, the full gate must be run once more before the cycle-closing commit because governance/conformance tests may inspect repository documentation.

11. R3 remains partial

The session deliberately does not claim complete R3 conformance.

The unclosed path is reinstatement.

Accepted authority requires reinstatement to be a new current decision with:

current validation
current impact review
current applicable approval
current Controller activation authority
current semantic compatibility
current serving admission
current concurrency

Historical approval or historical activation replay is insufficient.

The existing replacement proof does not yet establish the complete superseded-revision reinstatement path.

Fine-graph refinement

Use:

IMP-05-R3A
    real replacement activation
    -> CONFORMING_COMPLETE

IMP-05-R3B
    current superseded-revision reinstatement
    -> IN_PROGRESS / next governed frontier

Parent:

IMP-05-R3 = PARTIALLY_CONFORMING.

12. Explicit non-claims

This evidence does not claim:

complete reinstatement implementation;
IMP-05 macro completion;
IMP-06 eligibility;
delegated Workforce approval;
delegated Workforce activation;
a configuration.approve Workforce privilege;
a configuration.activate Workforce privilege;
system/AI/scheduled ordinary activation authority;
universal passkey step-up for every replacement activation;
implementation of later IMP-11, IMP-12 or IMP-13 semantic-owner portfolios;
promotion/deployment; or
a GitHub commit performed by ChatGPT during this session.
13. Next governed action

IMP-05-R3B — Reinstatement composition

The next implementation session must first inspect the post-synchronization local repository state and exact cycle-closing commit.

It must then prove that a previously superseded Configuration Revision can become active again only through a fresh current decision and cannot be reinstated through historical activation replay or historical approval.

Potential implementation detail requiring particular care:

a superseded initial Configuration Revision is not admitted by the deliberately non-initial R2 approval writer. The next session must determine the smallest v1.6-conforming approval implementation for that reinstatement case without weakening first-configuration or non-initial approval predicates.

If accepted authority proves insufficient for that representation, enter DESIGN_ESCALATION; do not infer a shortcut