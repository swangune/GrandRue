package grandrue.workforce;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Authoritative merchant-scoped workforce relationship for one Main Street
 * identity. Membership alone grants no capability privilege.
 */
public final class MerchantMembership {

    private final String membershipIdentifier;
    private final MerchantScope merchantScope;
    private final String identityReference;
    private final Instant establishedAt;
    private MerchantMembershipLifecycle lifecycle;
    private Instant endedAt;

    private MerchantMembership(
            String membershipIdentifier,
            MerchantScope merchantScope,
            String identityReference,
            Instant establishedAt
    ) {
        this.membershipIdentifier = requireIdentifier(
                membershipIdentifier,
                "Membership identifier"
        );
        this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        this.identityReference = requireIdentifier(
                identityReference,
                "Identity reference"
        );
        this.establishedAt = Objects.requireNonNull(establishedAt, "establishedAt");
        this.lifecycle = MerchantMembershipLifecycle.ACTIVE;
    }

    public static MerchantMembership establish(
            String membershipIdentifier,
            MerchantScope merchantScope,
            String identityReference,
            Instant establishedAt
    ) {
        return new MerchantMembership(
                membershipIdentifier,
                merchantScope,
                identityReference,
                establishedAt
        );
    }

    public String membershipIdentifier() {
        return membershipIdentifier;
    }

    public MerchantScope merchantScope() {
        return merchantScope;
    }

    public String identityReference() {
        return identityReference;
    }

    public Instant establishedAt() {
        return establishedAt;
    }

    public MerchantMembershipLifecycle lifecycle() {
        return lifecycle;
    }

    public Optional<Instant> endedAt() {
        return Optional.ofNullable(endedAt);
    }

    public boolean participatesInCurrentAuthority() {
        return lifecycle == MerchantMembershipLifecycle.ACTIVE;
    }

    public void suspend() {
        requireLifecycle(MerchantMembershipLifecycle.ACTIVE, "suspend");
        lifecycle = MerchantMembershipLifecycle.SUSPENDED;
    }

    public void resume() {
        requireLifecycle(MerchantMembershipLifecycle.SUSPENDED, "resume");
        lifecycle = MerchantMembershipLifecycle.ACTIVE;
    }

    public void end(Instant endedAt) {
        Objects.requireNonNull(endedAt, "endedAt");
        if (lifecycle == MerchantMembershipLifecycle.ENDED) {
            throw new IllegalStateException("Ended membership is terminal");
        }
        if (endedAt.isBefore(establishedAt)) {
            throw new IllegalArgumentException(
                    "Membership end cannot precede establishment"
            );
        }
        this.endedAt = endedAt;
        lifecycle = MerchantMembershipLifecycle.ENDED;
    }

    private void requireLifecycle(
            MerchantMembershipLifecycle required,
            String transition
    ) {
        if (lifecycle != required) {
            throw new IllegalStateException(
                    "Cannot " + transition + " membership from " + lifecycle
            );
        }
    }

    private static String requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }
}
