package grandrue.infrastructure.persistence.workforce;

import grandrue.semantic.Privilege;
import grandrue.workforce.MerchantAccessGroup;
import grandrue.workforce.MerchantGroupMembership;
import grandrue.workforce.MerchantGroupMembershipLifecycle;
import grandrue.workforce.MerchantMembership;
import grandrue.workforce.MerchantMembershipLifecycle;
import grandrue.workforce.MerchantRoleAssignment;
import grandrue.workforce.MerchantRoleAssignmentLifecycle;
import grandrue.workforce.MerchantRoleAssignmentSubjectType;
import grandrue.workforce.MerchantRoleDefinition;
import grandrue.workforce.MerchantWorkforceStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/** PostgreSQL/jOOQ adapter for authoritative MS-PROT-074 workforce facts. */
public final class JooqMerchantWorkforceStore implements MerchantWorkforceStore {

    private static final Table<?> MEMBERSHIP =
            DSL.table(DSL.name("workforce_merchant_membership"));
    private static final Table<?> ACCESS_GROUP =
            DSL.table(DSL.name("workforce_access_group"));
    private static final Table<?> GROUP_MEMBERSHIP =
            DSL.table(DSL.name("workforce_group_membership"));
    private static final Table<?> ROLE_DEFINITION =
            DSL.table(DSL.name("workforce_role_definition"));
    private static final Table<?> ROLE_PRIVILEGE =
            DSL.table(DSL.name("workforce_role_privilege"));
    private static final Table<?> ROLE_ASSIGNMENT =
            DSL.table(DSL.name("workforce_role_assignment"));

    private static final Field<String> MEMBERSHIP_IDENTIFIER =
            DSL.field(DSL.name("membership_identifier"), String.class);
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> IDENTITY_REFERENCE =
            DSL.field(DSL.name("identity_reference"), String.class);
    private static final Field<String> LIFECYCLE =
            DSL.field(DSL.name("lifecycle"), String.class);
    private static final Field<Instant> ESTABLISHED_AT =
            DSL.field(DSL.name("established_at"), Instant.class);
    private static final Field<Instant> ENDED_AT =
            DSL.field(DSL.name("ended_at"), Instant.class);

    private static final Field<String> GROUP_IDENTIFIER =
            DSL.field(DSL.name("group_identifier"), String.class);
    private static final Field<String> GROUP_MEMBERSHIP_IDENTIFIER =
            DSL.field(DSL.name("group_membership_identifier"), String.class);
    private static final Field<String> DISPLAY_NAME =
            DSL.field(DSL.name("display_name"), String.class);

    private static final Field<String> ROLE_IDENTIFIER =
            DSL.field(DSL.name("role_identifier"), String.class);
    private static final Field<String> PRIVILEGE_IDENTIFIER =
            DSL.field(DSL.name("privilege_identifier"), String.class);

    private static final Field<String> ASSIGNMENT_IDENTIFIER =
            DSL.field(DSL.name("assignment_identifier"), String.class);
    private static final Field<Instant> EFFECTIVE_FROM =
            DSL.field(DSL.name("effective_from"), Instant.class);
    private static final Field<Instant> EFFECTIVE_UNTIL_EXCLUSIVE =
            DSL.field(DSL.name("effective_until_exclusive"), Instant.class);
    private static final Field<Instant> REVOKED_AT =
            DSL.field(DSL.name("revoked_at"), Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantWorkforceStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public void establishMembership(MerchantMembership membership) {
        Objects.requireNonNull(membership, "membership");
        dsl.insertInto(MEMBERSHIP)
                .columns(
                        MEMBERSHIP_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        IDENTITY_REFERENCE,
                        LIFECYCLE,
                        ESTABLISHED_AT,
                        ENDED_AT
                )
                .values(
                        membership.membershipIdentifier(),
                        membership.merchantScope().merchantIdentifier(),
                        membership.identityReference(),
                        membership.lifecycle().name(),
                        membership.establishedAt(),
                        membership.endedAt().orElse(null)
                )
                .execute();
    }

    @Override
    public Optional<MerchantMembershipLifecycle> membershipLifecycle(
            String membershipIdentifier
    ) {
        requireIdentifier(membershipIdentifier, "Membership identifier");
        String lifecycle = dsl.select(LIFECYCLE)
                .from(MEMBERSHIP)
                .where(MEMBERSHIP_IDENTIFIER.eq(membershipIdentifier))
                .fetchOne(LIFECYCLE);
        return Optional.ofNullable(lifecycle)
                .map(MerchantMembershipLifecycle::valueOf);
    }

    @Override
    public boolean suspendMembership(String membershipIdentifier) {
        requireIdentifier(membershipIdentifier, "Membership identifier");
        return dsl.update(MEMBERSHIP)
                .set(LIFECYCLE, MerchantMembershipLifecycle.SUSPENDED.name())
                .where(MEMBERSHIP_IDENTIFIER.eq(membershipIdentifier))
                .and(LIFECYCLE.eq(MerchantMembershipLifecycle.ACTIVE.name()))
                .execute() == 1;
    }

    @Override
    public boolean resumeMembership(String membershipIdentifier) {
        requireIdentifier(membershipIdentifier, "Membership identifier");
        return dsl.update(MEMBERSHIP)
                .set(LIFECYCLE, MerchantMembershipLifecycle.ACTIVE.name())
                .where(MEMBERSHIP_IDENTIFIER.eq(membershipIdentifier))
                .and(LIFECYCLE.eq(MerchantMembershipLifecycle.SUSPENDED.name()))
                .execute() == 1;
    }

    @Override
    public boolean endMembership(
            String membershipIdentifier,
            Instant endedAt
    ) {
        requireIdentifier(membershipIdentifier, "Membership identifier");
        Objects.requireNonNull(endedAt, "endedAt");
        return dsl.update(MEMBERSHIP)
                .set(LIFECYCLE, MerchantMembershipLifecycle.ENDED.name())
                .set(ENDED_AT, endedAt)
                .where(MEMBERSHIP_IDENTIFIER.eq(membershipIdentifier))
                .and(LIFECYCLE.in(
                        MerchantMembershipLifecycle.ACTIVE.name(),
                        MerchantMembershipLifecycle.SUSPENDED.name()
                ))
                .and(ESTABLISHED_AT.le(endedAt))
                .execute() == 1;
    }

    @Override
    public void createAccessGroup(MerchantAccessGroup group) {
        Objects.requireNonNull(group, "group");
        dsl.insertInto(ACCESS_GROUP)
                .columns(GROUP_IDENTIFIER, MERCHANT_IDENTIFIER, DISPLAY_NAME)
                .values(
                        group.groupIdentifier(),
                        group.merchantScope().merchantIdentifier(),
                        group.displayName()
                )
                .execute();
    }

    @Override
    public void establishGroupMembership(
            MerchantGroupMembership groupMembership
    ) {
        Objects.requireNonNull(groupMembership, "groupMembership");
        transactions.executeWithoutResult(status -> {
            String currentMembershipLifecycle = dsl.select(LIFECYCLE)
                    .from(MEMBERSHIP)
                    .where(MEMBERSHIP_IDENTIFIER.eq(
                            groupMembership.membership().membershipIdentifier()
                    ))
                    .and(MERCHANT_IDENTIFIER.eq(
                            groupMembership.membership()
                                    .merchantScope()
                                    .merchantIdentifier()
                    ))
                    .forUpdate()
                    .fetchOne(LIFECYCLE);

            if (!MerchantMembershipLifecycle.ACTIVE.name().equals(
                    currentMembershipLifecycle
            )) {
                throw new IllegalStateException(
                        "Group membership requires current ACTIVE Merchant Membership"
                );
            }

            dsl.insertInto(GROUP_MEMBERSHIP)
                    .columns(
                            GROUP_MEMBERSHIP_IDENTIFIER,
                            MERCHANT_IDENTIFIER,
                            MEMBERSHIP_IDENTIFIER,
                            GROUP_IDENTIFIER,
                            LIFECYCLE
                    )
                    .values(
                            groupMembership.groupMembershipIdentifier(),
                            groupMembership.membership()
                                    .merchantScope()
                                    .merchantIdentifier(),
                            groupMembership.membership().membershipIdentifier(),
                            groupMembership.group().groupIdentifier(),
                            groupMembership.lifecycle().name()
                    )
                    .execute();
        });
    }

    @Override
    public Optional<MerchantGroupMembershipLifecycle> groupMembershipLifecycle(
            String groupMembershipIdentifier
    ) {
        requireIdentifier(groupMembershipIdentifier, "Group membership identifier");
        String lifecycle = dsl.select(LIFECYCLE)
                .from(GROUP_MEMBERSHIP)
                .where(GROUP_MEMBERSHIP_IDENTIFIER.eq(groupMembershipIdentifier))
                .fetchOne(LIFECYCLE);
        return Optional.ofNullable(lifecycle)
                .map(MerchantGroupMembershipLifecycle::valueOf);
    }

    @Override
    public boolean endGroupMembership(String groupMembershipIdentifier) {
        requireIdentifier(groupMembershipIdentifier, "Group membership identifier");
        return dsl.update(GROUP_MEMBERSHIP)
                .set(LIFECYCLE, MerchantGroupMembershipLifecycle.ENDED.name())
                .where(GROUP_MEMBERSHIP_IDENTIFIER.eq(groupMembershipIdentifier))
                .and(LIFECYCLE.eq(MerchantGroupMembershipLifecycle.ACTIVE.name()))
                .execute() == 1;
    }

    @Override
    public void defineRole(MerchantRoleDefinition roleDefinition) {
        Objects.requireNonNull(roleDefinition, "roleDefinition");
        transactions.executeWithoutResult(status -> {
            dsl.insertInto(ROLE_DEFINITION)
                    .columns(ROLE_IDENTIFIER, MERCHANT_IDENTIFIER, DISPLAY_NAME)
                    .values(
                            roleDefinition.roleIdentifier(),
                            roleDefinition.merchantScope().merchantIdentifier(),
                            roleDefinition.displayName()
                    )
                    .execute();
            for (Privilege privilege : roleDefinition.privileges()) {
                dsl.insertInto(ROLE_PRIVILEGE)
                        .columns(
                                ROLE_IDENTIFIER,
                                MERCHANT_IDENTIFIER,
                                PRIVILEGE_IDENTIFIER
                        )
                        .values(
                                roleDefinition.roleIdentifier(),
                                roleDefinition.merchantScope().merchantIdentifier(),
                                privilege.identifier()
                        )
                        .execute();
            }
        });
    }

    @Override
    public Set<Privilege> rolePrivileges(String roleIdentifier) {
        requireIdentifier(roleIdentifier, "Role identifier");
        return dsl.select(PRIVILEGE_IDENTIFIER)
                .from(ROLE_PRIVILEGE)
                .where(ROLE_IDENTIFIER.eq(roleIdentifier))
                .fetch(PRIVILEGE_IDENTIFIER)
                .stream()
                .map(Privilege::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void assignRole(MerchantRoleAssignment assignment) {
        Objects.requireNonNull(assignment, "assignment");
        String membershipIdentifier = assignment.subjectType()
                == MerchantRoleAssignmentSubjectType.MEMBERSHIP
                ? assignment.subjectIdentifier()
                : null;
        String groupIdentifier = assignment.subjectType()
                == MerchantRoleAssignmentSubjectType.ACCESS_GROUP
                ? assignment.subjectIdentifier()
                : null;

        dsl.insertInto(ROLE_ASSIGNMENT)
                .columns(
                        ASSIGNMENT_IDENTIFIER,
                        MERCHANT_IDENTIFIER,
                        MEMBERSHIP_IDENTIFIER,
                        GROUP_IDENTIFIER,
                        ROLE_IDENTIFIER,
                        EFFECTIVE_FROM,
                        EFFECTIVE_UNTIL_EXCLUSIVE,
                        REVOKED_AT
                )
                .values(
                        assignment.assignmentIdentifier(),
                        assignment.merchantScope().merchantIdentifier(),
                        membershipIdentifier,
                        groupIdentifier,
                        assignment.roleDefinition().roleIdentifier(),
                        assignment.effectiveFrom(),
                        assignment.effectiveUntilExclusive().orElse(null),
                        assignment.revokedAt().orElse(null)
                )
                .execute();
    }

    @Override
    public boolean roleAssignmentEffectiveAt(
            String assignmentIdentifier,
            Instant instant
    ) {
        requireIdentifier(assignmentIdentifier, "Role assignment identifier");
        Objects.requireNonNull(instant, "instant");
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(ROLE_ASSIGNMENT)
                        .where(ASSIGNMENT_IDENTIFIER.eq(assignmentIdentifier))
                        .and(EFFECTIVE_FROM.le(instant))
                        .and(EFFECTIVE_UNTIL_EXCLUSIVE.isNull()
                                .or(EFFECTIVE_UNTIL_EXCLUSIVE.gt(instant)))
                        .and(REVOKED_AT.isNull().or(REVOKED_AT.gt(instant)))
        );
    }

    @Override
    public Optional<MerchantRoleAssignmentLifecycle> roleAssignmentLifecycleAt(
            String assignmentIdentifier,
            Instant instant
    ) {
        requireIdentifier(assignmentIdentifier, "Role assignment identifier");
        Objects.requireNonNull(instant, "instant");
        Record record = dsl.select(EFFECTIVE_UNTIL_EXCLUSIVE, REVOKED_AT)
                .from(ROLE_ASSIGNMENT)
                .where(ASSIGNMENT_IDENTIFIER.eq(assignmentIdentifier))
                .fetchOne();
        if (record == null) {
            return Optional.empty();
        }
        Instant revokedAt = record.get(REVOKED_AT);
        if (revokedAt != null && !instant.isBefore(revokedAt)) {
            return Optional.of(MerchantRoleAssignmentLifecycle.REVOKED);
        }
        Instant effectiveUntil = record.get(EFFECTIVE_UNTIL_EXCLUSIVE);
        if (effectiveUntil != null && !instant.isBefore(effectiveUntil)) {
            return Optional.of(MerchantRoleAssignmentLifecycle.EXPIRED);
        }
        return Optional.of(MerchantRoleAssignmentLifecycle.ACTIVE);
    }

    @Override
    public boolean revokeRoleAssignment(
            String assignmentIdentifier,
            Instant revokedAt
    ) {
        requireIdentifier(assignmentIdentifier, "Role assignment identifier");
        Objects.requireNonNull(revokedAt, "revokedAt");
        return dsl.update(ROLE_ASSIGNMENT)
                .set(REVOKED_AT, revokedAt)
                .where(ASSIGNMENT_IDENTIFIER.eq(assignmentIdentifier))
                .and(REVOKED_AT.isNull())
                .and(EFFECTIVE_UNTIL_EXCLUSIVE.isNull()
                        .or(EFFECTIVE_UNTIL_EXCLUSIVE.gt(revokedAt)))
                .execute() == 1;
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
