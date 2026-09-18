package grandrue.infrastructure.persistence.commercial;

import grandrue.application.MerchantScope;
import grandrue.commercial.BillingCadence;
import grandrue.commercial.CommercialAcceptanceProvenance;
import mainstreet.commercial.CommercialEntitlementGrant;
import mainstreet.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.CommercialTransitionConflictException;
import grandrue.commercial.MerchantCommercialAgreement;
import grandrue.commercial.MerchantCommercialAgreementStore;
import grandrue.commercial.MerchantCommercialAgreementTransition;
import grandrue.commercial.StandardPlanLevel;
import grandrue.commercial.StandardPlanRevision;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PostgreSQL/jOOQ adapter for authoritative paid Merchant Commercial Agreement
 * transitions and agreement-derived entitlement grants.
 *
 * <p>The adapter persists exact plan-revision identity plus the revision's
 * explicit entitlement snapshot. It never resolves a merchant through a plan
 * name or "latest" revision. Transition-head locking and expected-current
 * comparison provide the accepted optimistic-concurrency/CAS semantics.</p>
 */
public final class JooqMerchantCommercialAgreementStore
        implements MerchantCommercialAgreementStore {

    private static final Table<?> AGREEMENT =
            DSL.table(DSL.name("commercial_agreement"));
    private static final Table<?> AGREEMENT_ENTITLEMENT =
            DSL.table(DSL.name("commercial_agreement_entitlement"));
    private static final Table<?> TRANSITION_HEAD =
            DSL.table(DSL.name("commercial_agreement_transition_head"));
    private static final Table<?> TRANSITION_REQUEST =
            DSL.table(DSL.name("commercial_agreement_transition_request"));

    private static final Field<String> AGREEMENT_ID =
            DSL.field(DSL.name("commercial_agreement_identity"), String.class);
    private static final Field<String> MERCHANT_ID =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> PLAN_LEVEL =
            DSL.field(DSL.name("plan_level"), String.class);
    private static final Field<String> PLAN_REVISION_ID =
            DSL.field(DSL.name("plan_revision_identifier"), String.class);
    private static final Field<String> BILLING_CADENCE =
            DSL.field(DSL.name("billing_cadence"), String.class);
    private static final Field<Instant> EFFECTIVE_FROM =
            DSL.field(DSL.name("effective_from"), Instant.class);
    private static final Field<Instant> EFFECTIVE_UNTIL =
            DSL.field(DSL.name("effective_until_exclusive"), Instant.class);
    private static final Field<String> ACCEPTANCE_ID =
            DSL.field(DSL.name("acceptance_provenance_identity"), String.class);
    private static final Field<String> ENTITLEMENT_ID =
            DSL.field(DSL.name("entitlement_identity"), String.class);
    private static final Field<String> LOGICAL_REQUEST_ID =
            DSL.field(DSL.name("logical_request_identity"), String.class);
    private static final Field<String> EXPECTED_CURRENT_AGREEMENT_ID =
            DSL.field(DSL.name("expected_current_agreement_identity"), String.class);
    private static final Field<String> INTENT_FINGERPRINT =
            DSL.field(DSL.name("intent_fingerprint"), String.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantCommercialAgreementStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantCommercialAgreement apply(
            MerchantCommercialAgreementTransition transition
    ) {
        Objects.requireNonNull(transition, "transition");
        MerchantCommercialAgreement committed = transactions.execute(status -> {
            lockLogicalRequest(transition.logicalRequestIdentity());

            Record replay = transitionRequestRecord(transition.logicalRequestIdentity());
            if (replay != null) {
                requireSameIntent(replay, transition);
                return requireAgreement(replay.get(AGREEMENT_ID));
            }

            MerchantScope merchantScope = transition.merchantScope();
            lockMerchantTransitionHead(merchantScope.merchantIdentifier());

            String actualHead = currentTransitionHead(merchantScope.merchantIdentifier());
            String expectedHead = transition.expectedCurrentAgreementIdentity().orElse(null);
            if (!Objects.equals(expectedHead, actualHead)) {
                throw new CommercialTransitionConflictException(
                        merchantScope.merchantIdentifier(),
                        expectedHead,
                        actualHead
                );
            }

            MerchantCommercialAgreement candidate = transition.candidateAgreement();
            if (agreementRecord(candidate.commercialAgreementIdentity()) != null) {
                throw new IllegalArgumentException(
                        "Commercial Agreement identity already exists: "
                                + candidate.commercialAgreementIdentity()
                );
            }

            if (actualHead != null) {
                closePredecessorAtTransitionBoundary(
                        actualHead,
                        candidate.effectiveFrom()
                );
            }

            insertAgreement(candidate);
            replaceTransitionHead(candidate);
            insertTransitionRequest(transition);
            return candidate;
        });

        return Objects.requireNonNull(
                committed,
                "Commercial Agreement transaction returned no result"
        );
    }

    @Override
    public Optional<MerchantCommercialAgreement> committedAgreement(
            String logicalRequestIdentity
    ) {
        requireIdentifier(logicalRequestIdentity, "Logical request identity");
        Record request = transitionRequestRecord(logicalRequestIdentity);
        if (request == null) {
            return Optional.empty();
        }
        return Optional.of(requireAgreement(request.get(AGREEMENT_ID)));
    }

    @Override
    public Optional<MerchantCommercialAgreement> effectiveAgreement(
            MerchantScope merchantScope,
            Instant instant
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(instant, "instant");

        var records = dsl.select(
                        AGREEMENT_ID,
                        MERCHANT_ID,
                        PLAN_LEVEL,
                        PLAN_REVISION_ID,
                        BILLING_CADENCE,
                        EFFECTIVE_FROM,
                        EFFECTIVE_UNTIL,
                        ACCEPTANCE_ID
                )
                .from(AGREEMENT)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .and(EFFECTIVE_FROM.le(instant))
                .and(EFFECTIVE_UNTIL.isNull().or(EFFECTIVE_UNTIL.gt(instant)))
                .orderBy(EFFECTIVE_FROM.desc(), AGREEMENT_ID.desc())
                .limit(2)
                .fetch();

        if (records.size() > 1) {
            throw new IllegalStateException(
                    "Overlapping effective Commercial Agreements detected for merchant: "
                            + merchantScope.merchantIdentifier()
            );
        }
        if (records.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toAgreement(records.getFirst()));
    }

    @Override
    public List<CommercialEntitlementGrant> effectiveGrants(
            MerchantScope merchantScope,
            CommercialEntitlementIdentity entitlementIdentity,
            Instant instant
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(entitlementIdentity, "entitlementIdentity");
        Objects.requireNonNull(instant, "instant");

        Optional<MerchantCommercialAgreement> effective = effectiveAgreement(
                merchantScope,
                instant
        );
        if (effective.isEmpty()) {
            return List.of();
        }
        MerchantCommercialAgreement agreement = effective.get();
        if (!agreement.planRevision().entitlements().contains(entitlementIdentity)) {
            return List.of();
        }
        return List.of(new CommercialEntitlementGrant(
                merchantScope,
                entitlementIdentity,
                agreement.grantProvenance(),
                agreement.effectiveFrom(),
                agreement.effectiveUntilExclusive()
        ));
    }

    private void closePredecessorAtTransitionBoundary(
            String predecessorAgreementIdentity,
            Instant transitionBoundary
    ) {
        Record predecessor = dsl.select(EFFECTIVE_FROM, EFFECTIVE_UNTIL)
                .from(AGREEMENT)
                .where(AGREEMENT_ID.eq(predecessorAgreementIdentity))
                .forUpdate()
                .fetchOne();
        if (predecessor == null) {
            throw new IllegalStateException(
                    "Commercial transition head references missing agreement: "
                            + predecessorAgreementIdentity
            );
        }

        Instant predecessorStart = predecessor.get(EFFECTIVE_FROM);
        if (!transitionBoundary.isAfter(predecessorStart)) {
            throw new IllegalArgumentException(
                    "Successor Commercial Agreement must start after its predecessor"
            );
        }

        Instant predecessorEnd = predecessor.get(EFFECTIVE_UNTIL);
        if (predecessorEnd == null || transitionBoundary.isBefore(predecessorEnd)) {
            dsl.update(AGREEMENT)
                    .set(EFFECTIVE_UNTIL, transitionBoundary)
                    .where(AGREEMENT_ID.eq(predecessorAgreementIdentity))
                    .execute();
        }
    }

    private void insertAgreement(MerchantCommercialAgreement agreement) {
        dsl.insertInto(AGREEMENT)
                .columns(
                        AGREEMENT_ID,
                        MERCHANT_ID,
                        PLAN_LEVEL,
                        PLAN_REVISION_ID,
                        BILLING_CADENCE,
                        EFFECTIVE_FROM,
                        EFFECTIVE_UNTIL,
                        ACCEPTANCE_ID
                )
                .values(
                        agreement.commercialAgreementIdentity(),
                        agreement.merchantScope().merchantIdentifier(),
                        agreement.planRevision().level().name(),
                        agreement.planRevision().revisionIdentifier(),
                        agreement.billingCadence().name(),
                        agreement.effectiveFrom(),
                        agreement.effectiveUntilExclusive().orElse(null),
                        agreement.acceptanceProvenance().identifier()
                )
                .execute();

        agreement.planRevision().entitlements().stream()
                .sorted(Comparator.comparing(CommercialEntitlementIdentity::identifier))
                .forEach(entitlement -> dsl.insertInto(AGREEMENT_ENTITLEMENT)
                        .columns(AGREEMENT_ID, MERCHANT_ID, ENTITLEMENT_ID)
                        .values(
                                agreement.commercialAgreementIdentity(),
                                agreement.merchantScope().merchantIdentifier(),
                                entitlement.identifier()
                        )
                        .execute());
    }

    private void replaceTransitionHead(MerchantCommercialAgreement agreement) {
        dsl.insertInto(TRANSITION_HEAD)
                .columns(MERCHANT_ID, AGREEMENT_ID)
                .values(
                        agreement.merchantScope().merchantIdentifier(),
                        agreement.commercialAgreementIdentity()
                )
                .onConflict(MERCHANT_ID)
                .doUpdate()
                .set(AGREEMENT_ID, agreement.commercialAgreementIdentity())
                .execute();
    }

    private void insertTransitionRequest(
            MerchantCommercialAgreementTransition transition
    ) {
        dsl.insertInto(TRANSITION_REQUEST)
                .columns(
                        LOGICAL_REQUEST_ID,
                        MERCHANT_ID,
                        EXPECTED_CURRENT_AGREEMENT_ID,
                        AGREEMENT_ID,
                        INTENT_FINGERPRINT
                )
                .values(
                        transition.logicalRequestIdentity(),
                        transition.merchantScope().merchantIdentifier(),
                        transition.expectedCurrentAgreementIdentity().orElse(null),
                        transition.candidateAgreement().commercialAgreementIdentity(),
                        intentFingerprint(transition)
                )
                .execute();
    }

    private Record transitionRequestRecord(String logicalRequestIdentity) {
        return dsl.select(
                        MERCHANT_ID,
                        EXPECTED_CURRENT_AGREEMENT_ID,
                        AGREEMENT_ID,
                        INTENT_FINGERPRINT
                )
                .from(TRANSITION_REQUEST)
                .where(LOGICAL_REQUEST_ID.eq(logicalRequestIdentity))
                .fetchOne();
    }

    private void requireSameIntent(
            Record committedRequest,
            MerchantCommercialAgreementTransition transition
    ) {
        String committedFingerprint = committedRequest.get(INTENT_FINGERPRINT);
        String requestedFingerprint = intentFingerprint(transition);
        if (!requestedFingerprint.equals(committedFingerprint)) {
            throw new IllegalArgumentException(
                    "Committed Commercial transition request identity cannot be reused "
                            + "for different intent"
            );
        }
    }

    private String currentTransitionHead(String merchantIdentifier) {
        return dsl.select(AGREEMENT_ID)
                .from(TRANSITION_HEAD)
                .where(MERCHANT_ID.eq(merchantIdentifier))
                .fetchOne(AGREEMENT_ID);
    }

    private MerchantCommercialAgreement requireAgreement(String agreementIdentity) {
        Record record = agreementRecord(agreementIdentity);
        if (record == null) {
            throw new IllegalStateException(
                    "Commercial agreement evidence is missing: " + agreementIdentity
            );
        }
        return toAgreement(record);
    }

    private Record agreementRecord(String agreementIdentity) {
        return dsl.select(
                        AGREEMENT_ID,
                        MERCHANT_ID,
                        PLAN_LEVEL,
                        PLAN_REVISION_ID,
                        BILLING_CADENCE,
                        EFFECTIVE_FROM,
                        EFFECTIVE_UNTIL,
                        ACCEPTANCE_ID
                )
                .from(AGREEMENT)
                .where(AGREEMENT_ID.eq(agreementIdentity))
                .fetchOne();
    }

    private MerchantCommercialAgreement toAgreement(Record record) {
        String agreementIdentity = record.get(AGREEMENT_ID);
        String merchantIdentifier = record.get(MERCHANT_ID);
        Set<CommercialEntitlementIdentity> entitlements = dsl.select(ENTITLEMENT_ID)
                .from(AGREEMENT_ENTITLEMENT)
                .where(AGREEMENT_ID.eq(agreementIdentity))
                .and(MERCHANT_ID.eq(merchantIdentifier))
                .fetch(ENTITLEMENT_ID)
                .stream()
                .map(CommercialEntitlementIdentity::new)
                .collect(Collectors.toUnmodifiableSet());

        return new MerchantCommercialAgreement(
                agreementIdentity,
                new MerchantScope(merchantIdentifier),
                new StandardPlanRevision(
                        StandardPlanLevel.valueOf(record.get(PLAN_LEVEL)),
                        record.get(PLAN_REVISION_ID),
                        entitlements
                ),
                BillingCadence.valueOf(record.get(BILLING_CADENCE)),
                record.get(EFFECTIVE_FROM),
                Optional.ofNullable(record.get(EFFECTIVE_UNTIL)),
                new CommercialAcceptanceProvenance(record.get(ACCEPTANCE_ID))
        );
    }

    private void lockLogicalRequest(String logicalRequestIdentity) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 560))",
                logicalRequestIdentity
        );
    }

    private void lockMerchantTransitionHead(String merchantIdentifier) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 561))",
                merchantIdentifier
        );
    }

    private static String intentFingerprint(
            MerchantCommercialAgreementTransition transition
    ) {
        MerchantCommercialAgreement candidate = transition.candidateAgreement();
        StringBuilder canonical = new StringBuilder();
        append(canonical, transition.merchantScope().merchantIdentifier());
        append(canonical, transition.expectedCurrentAgreementIdentity().orElse(""));
        append(canonical, candidate.commercialAgreementIdentity());
        append(canonical, candidate.planRevision().level().name());
        append(canonical, candidate.planRevision().revisionIdentifier());
        candidate.planRevision().entitlements().stream()
                .map(CommercialEntitlementIdentity::identifier)
                .sorted()
                .forEach(value -> append(canonical, value));
        append(canonical, candidate.billingCadence().name());
        append(canonical, candidate.effectiveFrom().toString());
        append(canonical, candidate.effectiveUntilExclusive()
                .map(Instant::toString)
                .orElse(""));
        append(canonical, candidate.acceptanceProvenance().identifier());

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(
                    canonical.toString().getBytes(StandardCharsets.UTF_8)
            ));
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 is unavailable", impossible);
        }
    }

    private static void append(StringBuilder target, String value) {
        target.append(value.length()).append(':').append(value).append('|');
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
