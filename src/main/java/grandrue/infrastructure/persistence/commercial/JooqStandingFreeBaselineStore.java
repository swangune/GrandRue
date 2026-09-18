package grandrue.infrastructure.persistence.commercial;

import grandrue.application.MerchantScope;
import mainstreet.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.StandingFreeBaseline;
import grandrue.commercial.StandingFreeBaselineStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * PostgreSQL/jOOQ adapter for Commercial's one Standing Free baseline per
 * Merchant Account invariant.
 */
public final class JooqStandingFreeBaselineStore
        implements StandingFreeBaselineStore {

    private static final Table<?> BASELINE =
            DSL.table(DSL.name("standing_free_baseline"));
    private static final Table<?> ENTITLEMENT =
            DSL.table(DSL.name("standing_free_baseline_entitlement"));

    private static final Field<String> BASELINE_IDENTIFIER =
            DSL.field(DSL.name("baseline_identifier"), String.class);
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> ORIGINATING_ESTABLISHMENT_IDENTITY =
            DSL.field(DSL.name("originating_establishment_identity"), String.class);
    private static final Field<Instant> EFFECTIVE_FROM =
            DSL.field(DSL.name("effective_from"), Instant.class);
    private static final Field<String> FREE_PLAN_REVISION_IDENTIFIER =
            DSL.field(DSL.name("free_plan_revision_identifier"), String.class);
    private static final Field<String> ENTITLEMENT_IDENTIFIER =
            DSL.field(DSL.name("entitlement_identifier"), String.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqStandingFreeBaselineStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public StandingFreeBaseline establishIfAbsent(StandingFreeBaseline candidate) {
        Objects.requireNonNull(candidate, "candidate");

        StandingFreeBaseline result = transactionTemplate.execute(status -> {
            lockMerchant(candidate.merchantScope());

            Optional<StandingFreeBaseline> existing = baselineForInternal(
                    candidate.merchantScope()
            );
            if (existing.isPresent()) {
                StandingFreeBaseline committed = existing.orElseThrow();
                requireSameOriginIntent(committed, candidate);
                return committed;
            }

            dsl.insertInto(BASELINE)
                    .columns(
                            BASELINE_IDENTIFIER,
                            MERCHANT_IDENTIFIER,
                            ORIGINATING_ESTABLISHMENT_IDENTITY,
                            EFFECTIVE_FROM,
                            FREE_PLAN_REVISION_IDENTIFIER
                    )
                    .values(
                            candidate.baselineIdentity(),
                            candidate.merchantScope().merchantIdentifier(),
                            candidate.originatingMerchantAccountEstablishmentIdentity(),
                            candidate.effectiveFrom(),
                            candidate.freePlanRevisionIdentity()
                    )
                    .execute();

            for (CommercialEntitlementIdentity entitlement
                    : candidate.entitlementSnapshot()) {
                dsl.insertInto(ENTITLEMENT)
                        .columns(
                                BASELINE_IDENTIFIER,
                                MERCHANT_IDENTIFIER,
                                ENTITLEMENT_IDENTIFIER
                        )
                        .values(
                                candidate.baselineIdentity(),
                                candidate.merchantScope().merchantIdentifier(),
                                entitlement.identifier()
                        )
                        .execute();
            }

            return candidate;
        });

        return Objects.requireNonNull(
                result,
                "Standing Free baseline transaction returned no result"
        );
    }

    @Override
    public Optional<StandingFreeBaseline> baselineFor(MerchantScope merchantScope) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        return baselineForInternal(merchantScope);
    }

    private Optional<StandingFreeBaseline> baselineForInternal(
            MerchantScope merchantScope) {
        Record record = dsl.select(
                        BASELINE_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        ORIGINATING_ESTABLISHMENT_IDENTITY,
                        EFFECTIVE_FROM,
                        FREE_PLAN_REVISION_IDENTIFIER
                )
                .from(BASELINE)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .fetchOne();

        if (record == null) {
            return Optional.empty();
        }

        String baselineIdentifier = record.get(BASELINE_IDENTIFIER);
        Set<CommercialEntitlementIdentity> entitlements = new LinkedHashSet<>();
        var entitlementRows = dsl.select(ENTITLEMENT_IDENTIFIER)
                .from(ENTITLEMENT)
                .where(BASELINE_IDENTIFIER.eq(baselineIdentifier))
                .and(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .orderBy(ENTITLEMENT_IDENTIFIER)
                .fetch();
        entitlementRows.forEach(row -> entitlements.add(
                new CommercialEntitlementIdentity(row.get(ENTITLEMENT_IDENTIFIER))
        ));

        return Optional.of(new StandingFreeBaseline(
                baselineIdentifier,
                merchantScope,
                record.get(ORIGINATING_ESTABLISHMENT_IDENTITY),
                record.get(EFFECTIVE_FROM),
                record.get(FREE_PLAN_REVISION_IDENTIFIER),
                entitlements
        ));
    }

    private void lockMerchant(MerchantScope merchantScope) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 81))",
                merchantScope.merchantIdentifier()
        );
    }

    private static void requireSameOriginIntent(
            StandingFreeBaseline committed,
            StandingFreeBaseline candidate) {
        if (!committed.merchantScope().equals(candidate.merchantScope())
                || !committed.originatingMerchantAccountEstablishmentIdentity().equals(
                        candidate.originatingMerchantAccountEstablishmentIdentity())
                || !committed.effectiveFrom().equals(candidate.effectiveFrom())
                || !committed.freePlanRevisionIdentity().equals(
                        candidate.freePlanRevisionIdentity())
                || !committed.entitlementSnapshot().equals(
                        candidate.entitlementSnapshot())) {
            throw new IllegalStateException(
                    "Standing Free baseline already exists with different authoritative origin"
            );
        }
    }
}
