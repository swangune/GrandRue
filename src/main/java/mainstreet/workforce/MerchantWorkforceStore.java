package mainstreet.workforce;

import mainstreet.semantic.Privilege;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

/**
 * Capability-owned persistence port for authoritative workforce facts.
 * Infrastructure adapters must preserve merchant scope and lifecycle CAS
 * invariants rather than treating these facts as generic CRUD rows.
 */
public interface MerchantWorkforceStore {

    void establishMembership(MerchantMembership membership);

    Optional<MerchantMembershipLifecycle> membershipLifecycle(
            String membershipIdentifier
    );

    boolean suspendMembership(String membershipIdentifier);

    boolean resumeMembership(String membershipIdentifier);

    boolean endMembership(String membershipIdentifier, Instant endedAt);

    void createAccessGroup(MerchantAccessGroup group);

    void establishGroupMembership(MerchantGroupMembership groupMembership);

    Optional<MerchantGroupMembershipLifecycle> groupMembershipLifecycle(
            String groupMembershipIdentifier
    );

    boolean endGroupMembership(String groupMembershipIdentifier);

    void defineRole(MerchantRoleDefinition roleDefinition);

    Set<Privilege> rolePrivileges(String roleIdentifier);

    void assignRole(MerchantRoleAssignment assignment);

    boolean roleAssignmentEffectiveAt(
            String assignmentIdentifier,
            Instant instant
    );

    Optional<MerchantRoleAssignmentLifecycle> roleAssignmentLifecycleAt(
            String assignmentIdentifier,
            Instant instant
    );

    boolean revokeRoleAssignment(
            String assignmentIdentifier,
            Instant revokedAt
    );
}
