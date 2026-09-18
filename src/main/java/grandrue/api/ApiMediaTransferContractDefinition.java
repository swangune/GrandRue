package grandrue.api;

import java.util.Objects;
import java.util.Optional;

/**
 * Static bounded media-transfer responsibilities governed by composite
 * MS-PROT-035 and MS-PROT-066. The contract coordinates authority for bytes;
 * it does not make upload completion into validation, business attachment or
 * Exposure, and it does not make a delivery reference into MediaAsset identity.
 */
public record ApiMediaTransferContractDefinition(
        ApiContractRegistration registration,
        Authority authority
) {
    public ApiMediaTransferContractDefinition {
        Objects.requireNonNull(registration, "registration");
        if (registration.kind() != ApiContractKind.MEDIA_TRANSFER) {
            throw new IllegalArgumentException(
                    "API media-transfer definition requires a MEDIA_TRANSFER registration"
            );
        }
        Objects.requireNonNull(authority, "authority");
    }

    /** Closed initial media-transfer responsibility classes. */
    public sealed interface Authority permits UploadAuthority, DownloadAuthority {
    }

    /** Bounded GrandRue authority required before media bytes are uploaded. */
    public record UploadAuthority(
            String merchantActorScopeAuthorityReference,
            String permittedMediaRoleContextReference,
            String sizeTypeProfileConstraintsReference,
            String boundedTransferIdentityReference,
            String expiryLifetimeReference,
            String storageDestinationAuthorityReference,
            String sourceConfirmationValidationRuleReference,
            String completionSeparationRuleReference
    ) implements Authority {
        public UploadAuthority {
            requireReference(
                    merchantActorScopeAuthorityReference,
                    "Merchant/actor scope authority reference"
            );
            requireReference(
                    permittedMediaRoleContextReference,
                    "Permitted media role/context reference"
            );
            requireReference(
                    sizeTypeProfileConstraintsReference,
                    "Size/type/profile constraints reference"
            );
            requireReference(
                    boundedTransferIdentityReference,
                    "Bounded transfer identity reference"
            );
            requireReference(expiryLifetimeReference, "Expiry/lifetime reference");
            requireReference(
                    storageDestinationAuthorityReference,
                    "Storage destination authority reference"
            );
            requireReference(
                    sourceConfirmationValidationRuleReference,
                    "Source confirmation/validation rule reference"
            );
            requireReference(
                    completionSeparationRuleReference,
                    "Completion separation rule reference"
            );
        }
    }

    /** Current authority required before protected media bytes are delivered. */
    public record DownloadAuthority(
            String currentPrincipalContextAuthorityReference,
            String merchantScopeAuthorityReference,
            Optional<String> businessRelationshipRuleReference,
            String exposureAccessAuthorityReference,
            Optional<BoundedDeliveryReferenceAuthority> boundedDeliveryReferenceAuthority
    ) implements Authority {
        public DownloadAuthority {
            requireReference(
                    currentPrincipalContextAuthorityReference,
                    "Current principal/context authority reference"
            );
            requireReference(
                    merchantScopeAuthorityReference,
                    "Merchant Scope authority reference"
            );
            businessRelationshipRuleReference = requireOptionalReference(
                    businessRelationshipRuleReference,
                    "Business relationship rule reference"
            );
            requireReference(
                    exposureAccessAuthorityReference,
                    "Exposure/access authority reference"
            );
            boundedDeliveryReferenceAuthority = Objects.requireNonNull(
                    boundedDeliveryReferenceAuthority,
                    "boundedDeliveryReferenceAuthority"
            );
        }
    }

    /** Optional signed/temporary reference policy; never MediaAsset identity. */
    public record BoundedDeliveryReferenceAuthority(
            String authorityBoundaryRuleReference,
            Optional<String> lifetimeRuleReference,
            String exposureCompatibilityRuleReference,
            String mediaAssetIdentitySeparationRuleReference
    ) {
        public BoundedDeliveryReferenceAuthority {
            requireReference(
                    authorityBoundaryRuleReference,
                    "Delivery authority-boundary rule reference"
            );
            lifetimeRuleReference = requireOptionalReference(
                    lifetimeRuleReference,
                    "Delivery lifetime rule reference"
            );
            requireReference(
                    exposureCompatibilityRuleReference,
                    "Exposure compatibility rule reference"
            );
            requireReference(
                    mediaAssetIdentitySeparationRuleReference,
                    "MediaAsset identity-separation rule reference"
            );
        }
    }

    private static Optional<String> requireOptionalReference(
            Optional<String> value,
            String label
    ) {
        Objects.requireNonNull(value, label);
        value.ifPresent(reference -> requireReference(reference, label));
        return value;
    }

    private static void requireReference(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
