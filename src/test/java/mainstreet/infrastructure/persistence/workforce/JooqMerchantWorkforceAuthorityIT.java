package mainstreet.infrastructure.persistence.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.Privilege;
import mainstreet.semantic.registry.OwnedOperationDefinition;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.workforce.MerchantAccessGroup;
import mainstreet.workforce.MerchantGroupMembership;
import mainstreet.workforce.MerchantMembership;
import mainstreet.workforce.MerchantRoleAssignment;
import mainstreet.workforce.MerchantRoleDefinition;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantWorkforceAuthorityIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final Instant T0 = Instant.parse("2026-08-24T00:00:00Z");
    private static final Instant T1 = Instant.parse("2026-08-25T00:00:00Z");
    private static final Instant T2 = Instant.parse("2026-08-26T00:00:00Z");
    private static final Privilege BOOKING_CREATE = new Privilege("booking.create");

    private DataSource dataSource;
    private DSLContext dsl;
    private JooqMerchantWorkforceStore store;
    private JooqMerchantWorkforceAuthority authority;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );

        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        TransactionAwareDataSourceProxy transactionAware =
                new TransactionAwareDataSourceProxy(dataSource);
        dsl = DSL.using(transactionAware, SQLDialect.POSTGRES);
        store = new JooqMerchantWorkforceStore(
                dsl,
                new DataSourceTransactionManager(dataSource)
        );
        authority = new JooqMerchantWorkforceAuthority(dsl);

        clearWorkforceTables();
        dsl.insertInto(
                        DSL.table(DSL.name("merchant_account")),
                        DSL.field(DSL.name("merchant_identifier"))
                )
                .values(MERCHANT_A.merchantIdentifier())
                .values(MERCHANT_B.merchantIdentifier())
                .onConflictDoNothing()
                .execute();
    }

    @AfterEach
    void tearDown() {
        clearWorkforceTables();
    }

    @Test
    void active_membership_and_direct_effective_role_assignment_grant_registered_privilege() {
        MerchantMembership membership = membership("membership-a", MERCHANT_A, "identity-1");
        MerchantRoleDefinition role = role("role-a", MERCHANT_A);
        store.establishMembership(membership);
        store.defineRole(role);
        store.assignRole(MerchantRoleAssignment.forMembership(
                "assignment-a",
                membership,
                role,
                T0,
                Optional.empty()
        ));

        assertTrue(authority.hasPrivilege(
                MERCHANT_A,
                "identity-1",
                BOOKING_CREATE,
                T1
        ));
        assertFalse(authority.hasPrivilege(
                MERCHANT_A,
                "identity-1",
                new Privilege("booking.cancel"),
                T1
        ));
    }

    @Test
    void active_group_membership_and_group_role_assignment_grant_privilege() {
        MerchantMembership membership = membership("membership-a", MERCHANT_A, "identity-1");
        MerchantAccessGroup group = new MerchantAccessGroup("group-a", MERCHANT_A, "Front Desk");
        MerchantRoleDefinition role = role("role-a", MERCHANT_A);
        store.establishMembership(membership);
        store.createAccessGroup(group);
        store.establishGroupMembership(MerchantGroupMembership.establish(
                "group-membership-a",
                membership,
                group
        ));
        store.defineRole(role);
        store.assignRole(MerchantRoleAssignment.forGroup(
                "assignment-a",
                group,
                role,
                T0,
                Optional.empty()
        ));

        assertTrue(authority.hasPrivilege(
                MERCHANT_A,
                "identity-1",
                BOOKING_CREATE,
                T1
        ));

        assertTrue(store.endGroupMembership("group-membership-a"));

        assertFalse(authority.hasPrivilege(
                MERCHANT_A,
                "identity-1",
                BOOKING_CREATE,
                T1
        ));
    }

    @Test
    void suspended_or_ended_membership_contributes_no_current_authority() {
        MerchantMembership membership = membership("membership-a", MERCHANT_A, "identity-1");
        MerchantRoleDefinition role = role("role-a", MERCHANT_A);
        store.establishMembership(membership);
        store.defineRole(role);
        store.assignRole(MerchantRoleAssignment.forMembership(
                "assignment-a",
                membership,
                role,
                T0,
                Optional.empty()
        ));

        assertTrue(authority.hasPrivilege(MERCHANT_A, "identity-1", BOOKING_CREATE, T1));
        assertTrue(store.suspendMembership("membership-a"));
        assertFalse(authority.hasPrivilege(MERCHANT_A, "identity-1", BOOKING_CREATE, T1));
        assertTrue(store.resumeMembership("membership-a"));
        assertTrue(authority.hasPrivilege(MERCHANT_A, "identity-1", BOOKING_CREATE, T1));
        assertTrue(store.endMembership("membership-a", T1));
        assertFalse(authority.hasPrivilege(MERCHANT_A, "identity-1", BOOKING_CREATE, T2));
    }

    @Test
    void role_assignment_effectiveness_is_current_half_open_and_revocation_sensitive() {
        MerchantMembership membership = membership("membership-a", MERCHANT_A, "identity-1");
        MerchantRoleDefinition role = role("role-a", MERCHANT_A);
        store.establishMembership(membership);
        store.defineRole(role);
        store.assignRole(MerchantRoleAssignment.forMembership(
                "assignment-a",
                membership,
                role,
                T0,
                Optional.of(T2)
        ));

        assertTrue(authority.hasPrivilege(MERCHANT_A, "identity-1", BOOKING_CREATE, T1));
        assertFalse(authority.hasPrivilege(MERCHANT_A, "identity-1", BOOKING_CREATE, T2));

        MerchantRoleAssignment openEnded = MerchantRoleAssignment.forMembership(
                "assignment-b",
                membership,
                role,
                T0,
                Optional.empty()
        );
        store.assignRole(openEnded);
        assertTrue(authority.hasPrivilege(MERCHANT_A, "identity-1", BOOKING_CREATE, T1));
        assertTrue(store.revokeRoleAssignment("assignment-b", T1));
        assertFalse(authority.hasPrivilege(
                MERCHANT_A,
                "identity-1",
                BOOKING_CREATE,
                T2
        ));
    }

    @Test
    void authority_is_strictly_merchant_scoped() {
        MerchantMembership membership = membership("membership-a", MERCHANT_A, "identity-1");
        MerchantRoleDefinition role = role("role-a", MERCHANT_A);
        store.establishMembership(membership);
        store.defineRole(role);
        store.assignRole(MerchantRoleAssignment.forMembership(
                "assignment-a",
                membership,
                role,
                T0,
                Optional.empty()
        ));

        assertTrue(authority.hasPrivilege(MERCHANT_A, "identity-1", BOOKING_CREATE, T1));
        assertFalse(authority.hasPrivilege(MERCHANT_B, "identity-1", BOOKING_CREATE, T1));
    }

    @Test
    void later_rejoin_under_new_membership_does_not_revive_old_direct_role_assignment() {
        MerchantMembership oldMembership = membership("membership-old", MERCHANT_A, "identity-1");
        MerchantRoleDefinition role = role("role-a", MERCHANT_A);
        store.establishMembership(oldMembership);
        store.defineRole(role);
        store.assignRole(MerchantRoleAssignment.forMembership(
                "assignment-old",
                oldMembership,
                role,
                T0,
                Optional.empty()
        ));
        assertTrue(store.endMembership("membership-old", T1));

        MerchantMembership newMembership = MerchantMembership.establish(
                "membership-new",
                MERCHANT_A,
                "identity-1",
                T2
        );
        store.establishMembership(newMembership);

        assertFalse(authority.hasPrivilege(
                MERCHANT_A,
                "identity-1",
                BOOKING_CREATE,
                T2.plusSeconds(1)
        ));
    }

    private MerchantMembership membership(
            String membershipIdentifier,
            MerchantScope merchantScope,
            String identityReference
    ) {
        return MerchantMembership.establish(
                membershipIdentifier,
                merchantScope,
                identityReference,
                T0
        );
    }

    private MerchantRoleDefinition role(String roleIdentifier, MerchantScope merchantScope) {
        return MerchantRoleDefinition.define(
                roleIdentifier,
                merchantScope,
                "Booking Manager",
                Set.of(BOOKING_CREATE),
                registry()
        );
    }

    private static SemanticRegistrySnapshot registry() {
        OwnedOperationalObjectDefinition booking = new OwnedOperationalObjectDefinition(
                "booking",
                Set.of("requested"),
                "requested"
        );
        OwnedOperationDefinition create = OwnedOperationDefinition.creation(
                "booking.create",
                "booking",
                "requested",
                "booking.created",
                "booking.create"
        );
        return new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new RegisteredCapability(
                        "booking",
                        List.of(booking),
                        List.of(create)
                ))
        );
    }

    private void clearWorkforceTables() {
        dsl.deleteFrom(DSL.table(DSL.name("workforce_operational_device_authorisation_request"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_operational_device_authorisation"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_role_assignment"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_role_privilege"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_role_definition"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_group_membership"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_access_group"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_merchant_membership"))).execute();
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: " + name
            );
        }
        return value;
    }
}
