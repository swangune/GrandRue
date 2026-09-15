package mainstreet.infrastructure.persistence.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.Privilege;
import mainstreet.semantic.registry.OwnedOperationDefinition;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.workforce.MerchantAccessGroup;
import mainstreet.workforce.MerchantGroupMembership;
import mainstreet.workforce.MerchantGroupMembershipLifecycle;
import mainstreet.workforce.MerchantMembership;
import mainstreet.workforce.MerchantMembershipLifecycle;
import mainstreet.workforce.MerchantRoleAssignment;
import mainstreet.workforce.MerchantRoleAssignmentLifecycle;
import mainstreet.workforce.MerchantRoleDefinition;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantWorkforceStoreIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final Instant T0 = Instant.parse("2026-08-24T00:00:00Z");
    private static final Instant T1 = Instant.parse("2026-08-25T00:00:00Z");
    private static final Instant T2 = Instant.parse("2026-08-26T00:00:00Z");

    private DataSource dataSource;
    private DSLContext dsl;
    private JooqMerchantWorkforceStore store;

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
    void workforce_facts_persist_and_survive_store_recreation() {
        MerchantMembership membership = membershipA();
        MerchantAccessGroup group = groupA();
        MerchantGroupMembership groupMembership = MerchantGroupMembership.establish(
                "group-membership-1",
                membership,
                group
        );
        MerchantRoleDefinition role = roleA();
        MerchantRoleAssignment assignment = MerchantRoleAssignment.forGroup(
                "assignment-1",
                group,
                role,
                T0,
                Optional.of(T2)
        );

        store.establishMembership(membership);
        store.createAccessGroup(group);
        store.establishGroupMembership(groupMembership);
        store.defineRole(role);
        store.assignRole(assignment);

        JooqMerchantWorkforceStore restarted = new JooqMerchantWorkforceStore(
                DSL.using(
                        new TransactionAwareDataSourceProxy(dataSource),
                        SQLDialect.POSTGRES
                ),
                new DataSourceTransactionManager(dataSource)
        );

        assertEquals(
                Optional.of(MerchantMembershipLifecycle.ACTIVE),
                restarted.membershipLifecycle("membership-1")
        );
        assertEquals(
                Optional.of(MerchantGroupMembershipLifecycle.ACTIVE),
                restarted.groupMembershipLifecycle("group-membership-1")
        );
        assertEquals(
                Set.of(new Privilege("booking.create")),
                restarted.rolePrivileges("role-1")
        );
        assertTrue(restarted.roleAssignmentEffectiveAt("assignment-1", T1));
        assertFalse(restarted.roleAssignmentEffectiveAt("assignment-1", T2));
    }

    @Test
    void ended_membership_is_terminal_in_postgresql_transition_contract() {
        store.establishMembership(membershipA());

        assertTrue(store.suspendMembership("membership-1"));
        assertTrue(store.endMembership("membership-1", T1));
        assertFalse(store.resumeMembership("membership-1"));
        assertFalse(store.suspendMembership("membership-1"));
        assertEquals(
                Optional.of(MerchantMembershipLifecycle.ENDED),
                store.membershipLifecycle("membership-1")
        );
    }

    @Test
    void ended_group_membership_is_terminal() {
        MerchantMembership membership = membershipA();
        MerchantAccessGroup group = groupA();
        store.establishMembership(membership);
        store.createAccessGroup(group);
        store.establishGroupMembership(MerchantGroupMembership.establish(
                "group-membership-1",
                membership,
                group
        ));

        assertTrue(store.endGroupMembership("group-membership-1"));
        assertFalse(store.endGroupMembership("group-membership-1"));
        assertEquals(
                Optional.of(MerchantGroupMembershipLifecycle.ENDED),
                store.groupMembershipLifecycle("group-membership-1")
        );
    }

    @Test
    void database_rejects_cross_merchant_group_membership_even_when_java_is_bypassed() {
        MerchantMembership membership = membershipA();
        MerchantAccessGroup groupB = new MerchantAccessGroup(
                "group-b",
                MERCHANT_B,
                "Foreign Group"
        );
        store.establishMembership(membership);
        store.createAccessGroup(groupB);

        assertThrows(
                DataAccessException.class,
                () -> dsl.insertInto(
                                DSL.table(DSL.name("workforce_group_membership")),
                                DSL.field(DSL.name("group_membership_identifier")),
                                DSL.field(DSL.name("merchant_identifier")),
                                DSL.field(DSL.name("membership_identifier")),
                                DSL.field(DSL.name("group_identifier")),
                                DSL.field(DSL.name("lifecycle"))
                        )
                        .values(
                                "illegal-cross-scope",
                                MERCHANT_A.merchantIdentifier(),
                                membership.membershipIdentifier(),
                                groupB.groupIdentifier(),
                                MerchantGroupMembershipLifecycle.ACTIVE.name()
                        )
                        .execute()
        );
    }

    @Test
    void database_rejects_cross_merchant_role_assignment_even_when_java_is_bypassed() {
        MerchantMembership membership = membershipA();
        MerchantRoleDefinition roleB = MerchantRoleDefinition.define(
                "role-b",
                MERCHANT_B,
                "Foreign Booking Manager",
                Set.of(new Privilege("booking.create")),
                registry()
        );
        store.establishMembership(membership);
        store.defineRole(roleB);

        assertThrows(
                DataAccessException.class,
                () -> dsl.insertInto(
                                DSL.table(DSL.name("workforce_role_assignment")),
                                DSL.field(DSL.name("assignment_identifier")),
                                DSL.field(DSL.name("merchant_identifier")),
                                DSL.field(DSL.name("membership_identifier")),
                                DSL.field(DSL.name("group_identifier")),
                                DSL.field(DSL.name("role_identifier")),
                                DSL.field(DSL.name("effective_from")),
                                DSL.field(DSL.name("effective_until_exclusive")),
                                DSL.field(DSL.name("revoked_at"))
                        )
                        .values(
                                "illegal-cross-scope",
                                MERCHANT_A.merchantIdentifier(),
                                membership.membershipIdentifier(),
                                null,
                                roleB.roleIdentifier(),
                                T0,
                                null,
                                null
                        )
                        .execute()
        );
    }

    @Test
    void role_assignment_revocation_is_persistent_and_permanent() {
        MerchantMembership membership = membershipA();
        MerchantRoleDefinition role = roleA();
        store.establishMembership(membership);
        store.defineRole(role);
        store.assignRole(MerchantRoleAssignment.forMembership(
                "assignment-1",
                membership,
                role,
                T0,
                Optional.empty()
        ));

        assertTrue(store.roleAssignmentEffectiveAt("assignment-1", T1));
        assertTrue(store.revokeRoleAssignment("assignment-1", T1));
        assertFalse(store.revokeRoleAssignment("assignment-1", T2));
        assertFalse(store.roleAssignmentEffectiveAt("assignment-1", T2));
        assertEquals(
                Optional.of(MerchantRoleAssignmentLifecycle.REVOKED),
                store.roleAssignmentLifecycleAt("assignment-1", T2)
        );
    }

    private MerchantMembership membershipA() {
        return MerchantMembership.establish(
                "membership-1",
                MERCHANT_A,
                "identity-1",
                T0
        );
    }

    private MerchantAccessGroup groupA() {
        return new MerchantAccessGroup("group-1", MERCHANT_A, "Managers");
    }

    private MerchantRoleDefinition roleA() {
        return MerchantRoleDefinition.define(
                "role-1",
                MERCHANT_A,
                "Booking Manager",
                Set.of(new Privilege("booking.create")),
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
