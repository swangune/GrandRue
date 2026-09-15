package mainstreet.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.Privilege;
import mainstreet.semantic.registry.OwnedOperationDefinition;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchantWorkforceModelTest {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final Instant T0 = Instant.parse("2026-08-24T00:00:00Z");
    private static final Instant T1 = Instant.parse("2026-08-25T00:00:00Z");
    private static final Instant T2 = Instant.parse("2026-08-26T00:00:00Z");

    @Test
    void membership_lifecycle_is_active_suspendable_resumable_and_terminal_when_ended() {
        MerchantMembership membership = MerchantMembership.establish(
                "membership-1",
                MERCHANT_A,
                "identity-1",
                T0
        );

        assertEquals(MerchantMembershipLifecycle.ACTIVE, membership.lifecycle());
        assertTrue(membership.participatesInCurrentAuthority());

        membership.suspend();
        assertEquals(MerchantMembershipLifecycle.SUSPENDED, membership.lifecycle());
        assertFalse(membership.participatesInCurrentAuthority());

        membership.resume();
        assertEquals(MerchantMembershipLifecycle.ACTIVE, membership.lifecycle());

        membership.end(T1);
        assertEquals(MerchantMembershipLifecycle.ENDED, membership.lifecycle());
        assertEquals(Optional.of(T1), membership.endedAt());
        assertFalse(membership.participatesInCurrentAuthority());
        assertThrows(IllegalStateException.class, membership::resume);
        assertThrows(IllegalStateException.class, membership::suspend);
    }

    @Test
    void group_membership_requires_same_merchant_and_active_membership_and_is_terminal_when_ended() {
        MerchantMembership member = MerchantMembership.establish(
                "membership-1",
                MERCHANT_A,
                "identity-1",
                T0
        );
        MerchantAccessGroup group = new MerchantAccessGroup(
                "group-1",
                MERCHANT_A,
                "Front Desk"
        );
        MerchantGroupMembership groupMembership = MerchantGroupMembership.establish(
                "group-membership-1",
                member,
                group
        );

        assertEquals(MerchantGroupMembershipLifecycle.ACTIVE, groupMembership.lifecycle());
        groupMembership.end();
        assertEquals(MerchantGroupMembershipLifecycle.ENDED, groupMembership.lifecycle());
        assertThrows(IllegalStateException.class, groupMembership::end);

        MerchantAccessGroup foreignGroup = new MerchantAccessGroup(
                "group-2",
                MERCHANT_B,
                "Front Desk"
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> MerchantGroupMembership.establish(
                        "group-membership-2",
                        member,
                        foreignGroup
                )
        );

        member.suspend();
        assertThrows(
                IllegalStateException.class,
                () -> MerchantGroupMembership.establish(
                        "group-membership-3",
                        member,
                        group
                )
        );
    }

    @Test
    void role_definition_accepts_only_privileges_registered_by_main_street_semantics() {
        SemanticRegistrySnapshot registry = registryWithBookingCreatePrivilege();

        MerchantRoleDefinition role = MerchantRoleDefinition.define(
                "role-1",
                MERCHANT_A,
                "Booking Manager",
                Set.of(new Privilege("booking.create")),
                registry
        );

        assertEquals(Set.of(new Privilege("booking.create")), role.privileges());
        assertThrows(
                IllegalArgumentException.class,
                () -> MerchantRoleDefinition.define(
                        "role-2",
                        MERCHANT_A,
                        "Invented Role",
                        Set.of(new Privilege("booking.unregistered")),
                        registry
                )
        );
    }

    @Test
    void role_assignment_is_merchant_scoped_half_open_and_revocation_is_permanent() {
        SemanticRegistrySnapshot registry = registryWithBookingCreatePrivilege();
        MerchantMembership membership = MerchantMembership.establish(
                "membership-1",
                MERCHANT_A,
                "identity-1",
                T0
        );
        MerchantRoleDefinition role = MerchantRoleDefinition.define(
                "role-1",
                MERCHANT_A,
                "Booking Manager",
                Set.of(new Privilege("booking.create")),
                registry
        );

        MerchantRoleAssignment assignment = MerchantRoleAssignment.forMembership(
                "assignment-1",
                membership,
                role,
                T1,
                Optional.of(T2)
        );

        assertFalse(assignment.isEffectiveAt(T0));
        assertTrue(assignment.isEffectiveAt(T1));
        assertTrue(assignment.isEffectiveAt(T2.minusNanos(1)));
        assertFalse(assignment.isEffectiveAt(T2));
        assertEquals(MerchantRoleAssignmentLifecycle.EXPIRED, assignment.lifecycleAt(T2));

        MerchantRoleAssignment active = MerchantRoleAssignment.forMembership(
                "assignment-2",
                membership,
                role,
                T0,
                Optional.empty()
        );
        active.revoke(T1);
        assertEquals(MerchantRoleAssignmentLifecycle.REVOKED, active.lifecycleAt(T2));
        assertFalse(active.isEffectiveAt(T2));
        assertThrows(IllegalStateException.class, () -> active.revoke(T2));
    }

    @Test
    void role_assignment_subject_and_role_must_share_one_merchant_scope() {
        SemanticRegistrySnapshot registry = registryWithBookingCreatePrivilege();
        MerchantMembership membership = MerchantMembership.establish(
                "membership-1",
                MERCHANT_A,
                "identity-1",
                T0
        );
        MerchantAccessGroup group = new MerchantAccessGroup(
                "group-1",
                MERCHANT_A,
                "Managers"
        );
        MerchantRoleDefinition roleA = MerchantRoleDefinition.define(
                "role-a",
                MERCHANT_A,
                "Booking Manager",
                Set.of(new Privilege("booking.create")),
                registry
        );
        MerchantRoleDefinition roleB = MerchantRoleDefinition.define(
                "role-b",
                MERCHANT_B,
                "Booking Manager",
                Set.of(new Privilege("booking.create")),
                registry
        );

        MerchantRoleAssignment groupAssignment = MerchantRoleAssignment.forGroup(
                "assignment-group",
                group,
                roleA,
                T0,
                Optional.empty()
        );
        assertEquals(MerchantRoleAssignmentSubjectType.ACCESS_GROUP, groupAssignment.subjectType());
        assertEquals("group-1", groupAssignment.subjectIdentifier());

        assertThrows(
                IllegalArgumentException.class,
                () -> MerchantRoleAssignment.forMembership(
                        "assignment-foreign",
                        membership,
                        roleB,
                        T0,
                        Optional.empty()
                )
        );
    }

    private static SemanticRegistrySnapshot registryWithBookingCreatePrivilege() {
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
        RegisteredCapability capability = new RegisteredCapability(
                "booking",
                List.of(booking),
                List.of(create)
        );
        return new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(capability)
        );
    }
}
