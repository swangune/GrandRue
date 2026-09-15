package mainstreet.workforce;

import java.util.Objects;

/** Associates one active Merchant Membership with one group in the same scope. */
public final class MerchantGroupMembership {

    private final String groupMembershipIdentifier;
    private final MerchantMembership membership;
    private final MerchantAccessGroup group;
    private MerchantGroupMembershipLifecycle lifecycle;

    private MerchantGroupMembership(
            String groupMembershipIdentifier,
            MerchantMembership membership,
            MerchantAccessGroup group
    ) {
        if (groupMembershipIdentifier == null || groupMembershipIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Group membership identifier must not be blank"
            );
        }
        this.membership = Objects.requireNonNull(membership, "membership");
        this.group = Objects.requireNonNull(group, "group");
        if (!membership.merchantScope().equals(group.merchantScope())) {
            throw new IllegalArgumentException(
                    "Group membership cannot cross Merchant Scope"
            );
        }
        if (!membership.participatesInCurrentAuthority()) {
            throw new IllegalStateException(
                    "Group membership requires an ACTIVE Merchant Membership"
            );
        }
        this.groupMembershipIdentifier = groupMembershipIdentifier;
        this.lifecycle = MerchantGroupMembershipLifecycle.ACTIVE;
    }

    public static MerchantGroupMembership establish(
            String groupMembershipIdentifier,
            MerchantMembership membership,
            MerchantAccessGroup group
    ) {
        return new MerchantGroupMembership(
                groupMembershipIdentifier,
                membership,
                group
        );
    }

    public String groupMembershipIdentifier() {
        return groupMembershipIdentifier;
    }

    public MerchantMembership membership() {
        return membership;
    }

    public MerchantAccessGroup group() {
        return group;
    }

    public MerchantGroupMembershipLifecycle lifecycle() {
        return lifecycle;
    }

    public void end() {
        if (lifecycle == MerchantGroupMembershipLifecycle.ENDED) {
            throw new IllegalStateException("Ended group membership is terminal");
        }
        lifecycle = MerchantGroupMembershipLifecycle.ENDED;
    }
}
