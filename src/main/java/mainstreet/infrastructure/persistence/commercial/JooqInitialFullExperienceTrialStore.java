package mainstreet.infrastructure.persistence.commercial;

import mainstreet.application.MerchantScope;
import mainstreet.commercial.InitialFullExperienceTrial;
import mainstreet.commercial.InitialFullExperienceTrialStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;

/**
 * PostgreSQL/jOOQ persistence adapter for the accepted one-automatic-trial
 * invariant owned by Commercial.
 *
 * <p>The adapter stores the authoritative Initial Full-Experience Trial fact
 * exactly once per Merchant Account. Duplicate or concurrent delivery returns
 * the already committed trial and cannot restart or extend it.</p>
 */
public final class JooqInitialFullExperienceTrialStore
        implements InitialFullExperienceTrialStore {

    private static final Table<?> TRIAL =
            DSL.table(DSL.name("initial_full_experience_trial"));

    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> TRIAL_IDENTITY =
            DSL.field(DSL.name("trial_identity"), String.class);
    private static final Field<String> ORIGIN_CONFIGURATION_REVISION_IDENTIFIER =
            DSL.field(
                    DSL.name("origin_configuration_revision_identifier"),
                    String.class
            );
    private static final Field<String> ORIGINATING_FIRST_ACTIVATION_IDENTITY =
            DSL.field(
                    DSL.name("originating_first_activation_identity"),
                    String.class
            );
    private static final Field<Instant> STARTS_AT =
            DSL.field(DSL.name("starts_at"), Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqInitialFullExperienceTrialStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public InitialFullExperienceTrial establishIfAbsent(
            InitialFullExperienceTrial candidate
    ) {
        Objects.requireNonNull(candidate, "candidate");
        String merchantIdentifier = candidate.merchantScope().merchantIdentifier();

        InitialFullExperienceTrial result = transactionTemplate.execute(status -> {
            lockMerchantTrial(merchantIdentifier);

            InitialFullExperienceTrial existing = existingTrial(merchantIdentifier);
            if (existing != null) {
                return existing;
            }

            dsl.insertInto(TRIAL)
                    .columns(
                            MERCHANT_IDENTIFIER,
                            TRIAL_IDENTITY,
                            ORIGIN_CONFIGURATION_REVISION_IDENTIFIER,
                            ORIGINATING_FIRST_ACTIVATION_IDENTITY,
                            STARTS_AT
                    )
                    .values(
                            merchantIdentifier,
                            candidate.trialIdentity(),
                            candidate.originConfigurationRevisionIdentifier(),
                            candidate.originatingFirstActivationIdentity(),
                            candidate.startsAt()
                    )
                    .execute();

            return candidate;
        });

        return Objects.requireNonNull(
                result,
                "Initial Full-Experience Trial transaction returned no result"
        );
    }

    private InitialFullExperienceTrial existingTrial(String merchantIdentifier) {
        Record record = dsl
                .select(
                        MERCHANT_IDENTIFIER,
                        TRIAL_IDENTITY,
                        ORIGIN_CONFIGURATION_REVISION_IDENTIFIER,
                        ORIGINATING_FIRST_ACTIVATION_IDENTITY,
                        STARTS_AT
                )
                .from(TRIAL)
                .where(MERCHANT_IDENTIFIER.eq(merchantIdentifier))
                .fetchOne();

        if (record == null) {
            return null;
        }

        return new InitialFullExperienceTrial(
                record.get(TRIAL_IDENTITY),
                new MerchantScope(record.get(MERCHANT_IDENTIFIER)),
                record.get(ORIGIN_CONFIGURATION_REVISION_IDENTIFIER),
                record.get(ORIGINATING_FIRST_ACTIVATION_IDENTITY),
                record.get(STARTS_AT)
        );
    }

    private void lockMerchantTrial(String merchantIdentifier) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 56))",
                merchantIdentifier
        );
    }
}
