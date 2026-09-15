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
import static org.junit.jupiter.api.Assertions.assertThrows;

class MerchantRoleAssignmentLifecycleTest {

    @Test
    void expired_assignment_cannot_be_rewritten_as_revoked() {
        MerchantScope scope = new MerchantScope("merchant-a");
        MerchantMembership membership = MerchantMembership.establish(
                "membership-1",
                scope,
                "identity-1",
                Instant.parse("2026-08-24T00:00:00Z")
        );
        MerchantRoleDefinition role = MerchantRoleDefinition.define(
                "role-1",
                scope,
                "Booking Manager",
                Set.of(new Privilege("booking.create")),
                registry()
        );
        Instant expiresAt = Instant.parse("2026-08-25T00:00:00Z");
        MerchantRoleAssignment assignment = MerchantRoleAssignment.forMembership(
                "assignment-1",
                membership,
                role,
                Instant.parse("2026-08-24T00:00:00Z"),
                Optional.of(expiresAt)
        );

        assertEquals(
                MerchantRoleAssignmentLifecycle.EXPIRED,
                assignment.lifecycleAt(expiresAt)
        );
        assertThrows(
                IllegalStateException.class,
                () -> assignment.revoke(expiresAt)
        );
        assertEquals(
                MerchantRoleAssignmentLifecycle.EXPIRED,
                assignment.lifecycleAt(expiresAt)
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
}
