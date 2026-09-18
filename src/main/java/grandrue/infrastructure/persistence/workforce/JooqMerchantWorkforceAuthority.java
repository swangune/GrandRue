package grandrue.infrastructure.persistence.workforce;

import grandrue.application.MerchantScope;
import mainstreet.semantic.Privilege;
import grandrue.workforce.MerchantMembershipAuthority;
import grandrue.workforce.MerchantWorkforceAuthority;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.Objects;

/**
 * PostgreSQL current-state reader for merchant workforce authority.
 *
 * <p>Current membership is resolved independently from privilege. Actor
 * Authorisation is then derived only from current ACTIVE Merchant Membership
 * plus an effective direct Role Assignment or an effective Role Assignment
 * reached through an ACTIVE Merchant Group Membership. Every query is
 * explicitly tenant scoped and reads current authoritative rows.</p>
 */
public final class JooqMerchantWorkforceAuthority
        implements MerchantWorkforceAuthority, MerchantMembershipAuthority {

    private static final Table<?> MEMBERSHIP =
            DSL.table(DSL.name("workforce_merchant_membership")).as("m");
    private static final Table<?> GROUP_MEMBERSHIP =
            DSL.table(DSL.name("workforce_group_membership")).as("gm");
    private static final Table<?> ROLE_ASSIGNMENT =
            DSL.table(DSL.name("workforce_role_assignment")).as("ra");
    private static final Table<?> ROLE_PRIVILEGE =
            DSL.table(DSL.name("workforce_role_privilege")).as("rp");

    private static final Field<String> M_MEMBERSHIP_IDENTIFIER =
            DSL.field(DSL.name("m", "membership_identifier"), String.class);
    private static final Field<String> M_MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("m", "merchant_identifier"), String.class);
    private static final Field<String> M_IDENTITY_REFERENCE =
            DSL.field(DSL.name("m", "identity_reference"), String.class);
    private static final Field<String> M_LIFECYCLE =
            DSL.field(DSL.name("m", "lifecycle"), String.class);

    private static final Field<String> GM_MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("gm", "merchant_identifier"), String.class);
    private static final Field<String> GM_MEMBERSHIP_IDENTIFIER =
            DSL.field(DSL.name("gm", "membership_identifier"), String.class);
    private static final Field<String> GM_GROUP_IDENTIFIER =
            DSL.field(DSL.name("gm", "group_identifier"), String.class);
    private static final Field<String> GM_LIFECYCLE =
            DSL.field(DSL.name("gm", "lifecycle"), String.class);

    private static final Field<String> RA_MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("ra", "merchant_identifier"), String.class);
    private static final Field<String> RA_MEMBERSHIP_IDENTIFIER =
            DSL.field(DSL.name("ra", "membership_identifier"), String.class);
    private static final Field<String> RA_GROUP_IDENTIFIER =
            DSL.field(DSL.name("ra", "group_identifier"), String.class);
    private static final Field<String> RA_ROLE_IDENTIFIER =
            DSL.field(DSL.name("ra", "role_identifier"), String.class);
    private static final Field<Instant> RA_EFFECTIVE_FROM =
            DSL.field(DSL.name("ra", "effective_from"), Instant.class);
    private static final Field<Instant> RA_EFFECTIVE_UNTIL_EXCLUSIVE =
            DSL.field(DSL.name("ra", "effective_until_exclusive"), Instant.class);
    private static final Field<Instant> RA_REVOKED_AT =
            DSL.field(DSL.name("ra", "revoked_at"), Instant.class);

    private static final Field<String> RP_MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("rp", "merchant_identifier"), String.class);
    private static final Field<String> RP_ROLE_IDENTIFIER =
            DSL.field(DSL.name("rp", "role_identifier"), String.class);
    private static final Field<String> RP_PRIVILEGE_IDENTIFIER =
            DSL.field(DSL.name("rp", "privilege_identifier"), String.class);

    private final DSLContext dsl;

    public JooqMerchantWorkforceAuthority(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
    }

    @Override
    public boolean isActive(
            MerchantScope merchantScope,
            String identityReference
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identityReference, "Identity reference");
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(MEMBERSHIP)
                        .where(M_MERCHANT_IDENTIFIER.eq(
                                merchantScope.merchantIdentifier()
                        ))
                        .and(M_IDENTITY_REFERENCE.eq(identityReference))
                        .and(M_LIFECYCLE.eq("ACTIVE"))
        );
    }

    @Override
    public boolean hasPrivilege(
            MerchantScope merchantScope,
            String identityReference,
            Privilege privilege,
            Instant instant
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identityReference, "Identity reference");
        Objects.requireNonNull(privilege, "privilege");
        Objects.requireNonNull(instant, "instant");

        String merchantIdentifier = merchantScope.merchantIdentifier();
        Condition assignmentEffective = RA_EFFECTIVE_FROM.le(instant)
                .and(RA_EFFECTIVE_UNTIL_EXCLUSIVE.isNull()
                        .or(RA_EFFECTIVE_UNTIL_EXCLUSIVE.gt(instant)))
                .and(RA_REVOKED_AT.isNull().or(RA_REVOKED_AT.gt(instant)));

        if (hasDirectPrivilege(
                merchantIdentifier,
                identityReference,
                privilege.identifier(),
                assignmentEffective
        )) {
            return true;
        }

        return hasGroupDerivedPrivilege(
                merchantIdentifier,
                identityReference,
                privilege.identifier(),
                assignmentEffective
        );
    }

    private boolean hasDirectPrivilege(
            String merchantIdentifier,
            String identityReference,
            String privilegeIdentifier,
            Condition assignmentEffective
    ) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(MEMBERSHIP)
                        .join(ROLE_ASSIGNMENT)
                        .on(RA_MERCHANT_IDENTIFIER.eq(M_MERCHANT_IDENTIFIER))
                        .and(RA_MEMBERSHIP_IDENTIFIER.eq(M_MEMBERSHIP_IDENTIFIER))
                        .join(ROLE_PRIVILEGE)
                        .on(RP_MERCHANT_IDENTIFIER.eq(RA_MERCHANT_IDENTIFIER))
                        .and(RP_ROLE_IDENTIFIER.eq(RA_ROLE_IDENTIFIER))
                        .where(M_MERCHANT_IDENTIFIER.eq(merchantIdentifier))
                        .and(M_IDENTITY_REFERENCE.eq(identityReference))
                        .and(M_LIFECYCLE.eq("ACTIVE"))
                        .and(RA_MEMBERSHIP_IDENTIFIER.isNotNull())
                        .and(RA_GROUP_IDENTIFIER.isNull())
                        .and(assignmentEffective)
                        .and(RP_PRIVILEGE_IDENTIFIER.eq(privilegeIdentifier))
        );
    }

    private boolean hasGroupDerivedPrivilege(
            String merchantIdentifier,
            String identityReference,
            String privilegeIdentifier,
            Condition assignmentEffective
    ) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(MEMBERSHIP)
                        .join(GROUP_MEMBERSHIP)
                        .on(GM_MERCHANT_IDENTIFIER.eq(M_MERCHANT_IDENTIFIER))
                        .and(GM_MEMBERSHIP_IDENTIFIER.eq(M_MEMBERSHIP_IDENTIFIER))
                        .join(ROLE_ASSIGNMENT)
                        .on(RA_MERCHANT_IDENTIFIER.eq(GM_MERCHANT_IDENTIFIER))
                        .and(RA_GROUP_IDENTIFIER.eq(GM_GROUP_IDENTIFIER))
                        .join(ROLE_PRIVILEGE)
                        .on(RP_MERCHANT_IDENTIFIER.eq(RA_MERCHANT_IDENTIFIER))
                        .and(RP_ROLE_IDENTIFIER.eq(RA_ROLE_IDENTIFIER))
                        .where(M_MERCHANT_IDENTIFIER.eq(merchantIdentifier))
                        .and(M_IDENTITY_REFERENCE.eq(identityReference))
                        .and(M_LIFECYCLE.eq("ACTIVE"))
                        .and(GM_LIFECYCLE.eq("ACTIVE"))
                        .and(RA_MEMBERSHIP_IDENTIFIER.isNull())
                        .and(RA_GROUP_IDENTIFIER.isNotNull())
                        .and(assignmentEffective)
                        .and(RP_PRIVILEGE_IDENTIFIER.eq(privilegeIdentifier))
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
