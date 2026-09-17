package grandrue.infrastructure.persistence.commercial;

import mainstreet.application.MerchantScope;
import mainstreet.commercial.CommercialEntitlementGrant;
import mainstreet.commercial.CommercialEntitlementGrantAuthority;
import mainstreet.commercial.CommercialEntitlementIdentity;
import mainstreet.commercial.InitialFullExperienceTrial;
import mainstreet.commercial.InitialFullExperienceTrialGrantSource;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Read adapter that resolves the durable initial trial fact into effective
 * full-experience Commercial entitlement grants.
 *
 * <p>The explicit entitlement set is supplied by Commercial product authority;
 * this adapter does not inspect Merchant Configuration or infer plan semantics.</p>
 */
public final class JooqInitialFullExperienceTrialGrantAuthority
        implements CommercialEntitlementGrantAuthority {

    private static final Table<?> TRIAL =
            DSL.table(DSL.name("initial_full_experience_trial"));
    private static final Field<String> MERCHANT_ID =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> TRIAL_ID =
            DSL.field(DSL.name("trial_identity"), String.class);
    private static final Field<String> ORIGIN_REVISION_ID = DSL.field(
            DSL.name("origin_configuration_revision_identifier"),
            String.class
    );
    private static final Field<String> ORIGIN_ACTIVATION_ID = DSL.field(
            DSL.name("originating_first_activation_identity"),
            String.class
    );
    private static final Field<Instant> STARTS_AT =
            DSL.field(DSL.name("starts_at"), Instant.class);

    private final DSLContext dsl;
    private final Set<CommercialEntitlementIdentity> fullExperienceEntitlements;
    private final InitialFullExperienceTrialGrantSource grantSource =
            new InitialFullExperienceTrialGrantSource();

    public JooqInitialFullExperienceTrialGrantAuthority(
            DSLContext dsl,
            Set<CommercialEntitlementIdentity> fullExperienceEntitlements
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.fullExperienceEntitlements = Set.copyOf(Objects.requireNonNull(
                fullExperienceEntitlements,
                "fullExperienceEntitlements"
        ));
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
        if (!fullExperienceEntitlements.contains(entitlementIdentity)) {
            return List.of();
        }

        Record record = dsl.select(
                        MERCHANT_ID,
                        TRIAL_ID,
                        ORIGIN_REVISION_ID,
                        ORIGIN_ACTIVATION_ID,
                        STARTS_AT
                )
                .from(TRIAL)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .fetchOne();
        if (record == null) {
            return List.of();
        }

        InitialFullExperienceTrial trial = new InitialFullExperienceTrial(
                record.get(TRIAL_ID),
                new MerchantScope(record.get(MERCHANT_ID)),
                record.get(ORIGIN_REVISION_ID),
                record.get(ORIGIN_ACTIVATION_ID),
                record.get(STARTS_AT)
        );
        if (!trial.isEffectiveAt(instant)) {
            return List.of();
        }
        return grantSource.grantsFor(trial, Set.of(entitlementIdentity));
    }
}
