package grandrue.workforce;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Merchant-scoped assignment of one role definition to an initial supported
 * subject: a Merchant Membership or Merchant Access Group.
 */
public final class MerchantRoleAssignment {

    private final String assignmentIdentifier;
    private final MerchantScope merchantScope;
    private final MerchantRoleAssignmentSubjectType subjectType;
    private final String subjectIdentifier;
    private final MerchantRoleDefinition roleDefinition;
    private final Instant effectiveFrom;
    private final Optional<Instant> effectiveUntilExclusive;
    private Instant revokedAt;

    private MerchantRoleAssignment(
            String assignmentIdentifier,
            MerchantScope merchantScope,
            MerchantRoleAssignmentSubjectType subjectType,
            String subjectIdentifier,
            MerchantRoleDefinition roleDefinition,
            Instant effectiveFrom,
            Optional<Instant> effectiveUntilExclusive
    ) {
        if (assignmentIdentifier == null || assignmentIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Role assignment identifier must not be blank"
            );
        }
        this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        this.subjectType = Objects.requireNonNull(subjectType, "subjectType");
        if (subjectIdentifier == null || subjectIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Role assignment subject identifier must not be blank"
            );
        }
        this.roleDefinition = Objects.requireNonNull(roleDefinition, "roleDefinition");
        if (!merchantScope.equals(roleDefinition.merchantScope())) {
            throw new IllegalArgumentException(
                    "Role assignment cannot cross Merchant Scope"
            );
        }
        this.effectiveFrom = Objects.requireNonNull(effectiveFrom, "effectiveFrom");
        this.effectiveUntilExclusive = Objects.requireNonNull(
                effectiveUntilExclusive,
                "effectiveUntilExclusive"
        );
        effectiveUntilExclusive.ifPresent(end -> {
            if (!end.isAfter(effectiveFrom)) {
                throw new IllegalArgumentException(
                        "Role assignment end must be after its start"
                );
            }
        });
        this.assignmentIdentifier = assignmentIdentifier;
        this.subjectIdentifier = subjectIdentifier;
    }

    public static MerchantRoleAssignment forMembership(
            String assignmentIdentifier,
            MerchantMembership membership,
            MerchantRoleDefinition roleDefinition,
            Instant effectiveFrom,
            Optional<Instant> effectiveUntilExclusive
    ) {
        Objects.requireNonNull(membership, "membership");
        return new MerchantRoleAssignment(
                assignmentIdentifier,
                membership.merchantScope(),
                MerchantRoleAssignmentSubjectType.MEMBERSHIP,
                membership.membershipIdentifier(),
                roleDefinition,
                effectiveFrom,
                effectiveUntilExclusive
        );
    }

    public static MerchantRoleAssignment forGroup(
            String assignmentIdentifier,
            MerchantAccessGroup group,
            MerchantRoleDefinition roleDefinition,
            Instant effectiveFrom,
            Optional<Instant> effectiveUntilExclusive
    ) {
        Objects.requireNonNull(group, "group");
        return new MerchantRoleAssignment(
                assignmentIdentifier,
                group.merchantScope(),
                MerchantRoleAssignmentSubjectType.ACCESS_GROUP,
                group.groupIdentifier(),
                roleDefinition,
                effectiveFrom,
                effectiveUntilExclusive
        );
    }

    public String assignmentIdentifier() {
        return assignmentIdentifier;
    }

    public MerchantScope merchantScope() {
        return merchantScope;
    }

    public MerchantRoleAssignmentSubjectType subjectType() {
        return subjectType;
    }

    public String subjectIdentifier() {
        return subjectIdentifier;
    }

    public MerchantRoleDefinition roleDefinition() {
        return roleDefinition;
    }

    public Instant effectiveFrom() {
        return effectiveFrom;
    }

    public Optional<Instant> effectiveUntilExclusive() {
        return effectiveUntilExclusive;
    }

    public Optional<Instant> revokedAt() {
        return Optional.ofNullable(revokedAt);
    }

    public boolean isEffectiveAt(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        if (instant.isBefore(effectiveFrom)) {
            return false;
        }
        if (effectiveUntilExclusive.map(end -> !instant.isBefore(end)).orElse(false)) {
            return false;
        }
        return revokedAt == null || instant.isBefore(revokedAt);
    }

    public MerchantRoleAssignmentLifecycle lifecycleAt(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        if (revokedAt != null && !instant.isBefore(revokedAt)) {
            return MerchantRoleAssignmentLifecycle.REVOKED;
        }
        if (effectiveUntilExclusive.map(end -> !instant.isBefore(end)).orElse(false)) {
            return MerchantRoleAssignmentLifecycle.EXPIRED;
        }
        return MerchantRoleAssignmentLifecycle.ACTIVE;
    }

    public void revoke(Instant revokedAt) {
        Objects.requireNonNull(revokedAt, "revokedAt");
        if (this.revokedAt != null) {
            throw new IllegalStateException("Revoked role assignment is terminal");
        }
        if (effectiveUntilExclusive
                .map(end -> !revokedAt.isBefore(end))
                .orElse(false)) {
            throw new IllegalStateException("Expired role assignment is terminal");
        }
        this.revokedAt = revokedAt;
    }
}
