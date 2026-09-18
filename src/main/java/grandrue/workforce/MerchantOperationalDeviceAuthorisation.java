package grandrue.workforce;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Merchant-scoped trust fact authorising one verified device/application
 * binding to establish staff operational contexts.
 *
 * <p>This fact is not authentication, Merchant Membership, Role Assignment or
 * Merchant Controller authority. Governed by MS-PROT-074.</p>
 */
public final class MerchantOperationalDeviceAuthorisation {

    private final String authorisationIdentifier;
    private final MerchantScope merchantScope;
    private final String bindingReference;
    private final String authorisedByIdentityReference;
    private final Instant authorisedAt;
    private Instant revokedAt;

    private MerchantOperationalDeviceAuthorisation(
            String authorisationIdentifier,
            MerchantScope merchantScope,
            String bindingReference,
            String authorisedByIdentityReference,
            Instant authorisedAt,
            Instant revokedAt
    ) {
        this.authorisationIdentifier = requireIdentifier(
                authorisationIdentifier,
                "Operational device authorisation identifier"
        );
        this.merchantScope = Objects.requireNonNull(
                merchantScope,
                "merchantScope"
        );
        this.bindingReference = requireIdentifier(
                bindingReference,
                "Device/application binding reference"
        );
        this.authorisedByIdentityReference = requireIdentifier(
                authorisedByIdentityReference,
                "Authorising Controller identity reference"
        );
        this.authorisedAt = Objects.requireNonNull(authorisedAt, "authorisedAt");
        if (revokedAt != null && revokedAt.isBefore(authorisedAt)) {
            throw new IllegalArgumentException(
                    "Device authorisation revocation cannot precede authorisation"
            );
        }
        this.revokedAt = revokedAt;
    }

    public static MerchantOperationalDeviceAuthorisation authorised(
            String authorisationIdentifier,
            MerchantScope merchantScope,
            String bindingReference,
            String authorisedByIdentityReference,
            Instant authorisedAt
    ) {
        return new MerchantOperationalDeviceAuthorisation(
                authorisationIdentifier,
                merchantScope,
                bindingReference,
                authorisedByIdentityReference,
                authorisedAt,
                null
        );
    }

    /** Reconstitutes an already committed historical authorisation fact. */
    public static MerchantOperationalDeviceAuthorisation reconstitute(
            String authorisationIdentifier,
            MerchantScope merchantScope,
            String bindingReference,
            String authorisedByIdentityReference,
            Instant authorisedAt,
            Optional<Instant> revokedAt
    ) {
        return new MerchantOperationalDeviceAuthorisation(
                authorisationIdentifier,
                merchantScope,
                bindingReference,
                authorisedByIdentityReference,
                authorisedAt,
                Objects.requireNonNull(revokedAt, "revokedAt").orElse(null)
        );
    }

    public String authorisationIdentifier() {
        return authorisationIdentifier;
    }

    public MerchantScope merchantScope() {
        return merchantScope;
    }

    public String bindingReference() {
        return bindingReference;
    }

    public String authorisedByIdentityReference() {
        return authorisedByIdentityReference;
    }

    public Instant authorisedAt() {
        return authorisedAt;
    }

    public Optional<Instant> revokedAt() {
        return Optional.ofNullable(revokedAt);
    }

    public MerchantOperationalDeviceAuthorisationLifecycle lifecycle() {
        return revokedAt == null
                ? MerchantOperationalDeviceAuthorisationLifecycle.ACTIVE
                : MerchantOperationalDeviceAuthorisationLifecycle.REVOKED;
    }

    public boolean isActive() {
        return lifecycle() == MerchantOperationalDeviceAuthorisationLifecycle.ACTIVE;
    }

    public void revoke(Instant revokedAt) {
        Objects.requireNonNull(revokedAt, "revokedAt");
        if (this.revokedAt != null) {
            throw new IllegalStateException(
                    "Revoked operational device authorisation is terminal"
            );
        }
        if (revokedAt.isBefore(authorisedAt)) {
            throw new IllegalArgumentException(
                    "Device authorisation revocation cannot precede authorisation"
            );
        }
        this.revokedAt = revokedAt;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MerchantOperationalDeviceAuthorisation that)) {
            return false;
        }
        return authorisationIdentifier.equals(that.authorisationIdentifier)
                && merchantScope.equals(that.merchantScope)
                && bindingReference.equals(that.bindingReference)
                && authorisedByIdentityReference.equals(that.authorisedByIdentityReference)
                && authorisedAt.equals(that.authorisedAt)
                && Objects.equals(revokedAt, that.revokedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                authorisationIdentifier,
                merchantScope,
                bindingReference,
                authorisedByIdentityReference,
                authorisedAt,
                revokedAt
        );
    }

    private static String requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }
}
