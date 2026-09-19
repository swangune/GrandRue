package grandrue.api;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiMediaTransferContractDefinitionTest {

    @Test
    void retains_bounded_upload_authority_and_completion_separation() {
        ApiMediaTransferContractDefinition.UploadAuthority upload =
                new ApiMediaTransferContractDefinition.UploadAuthority(
                        "trusted-merchant-actor-scope",
                        "permitted-media-role-and-context",
                        "registered-size-type-profile-constraints",
                        "bounded-upload-transfer-identity",
                        "bounded-upload-expiry",
                        "authorised-storage-destination",
                        "source-confirmation-and-validation",
                        "upload-is-not-validation-attachment-or-exposure"
                );
        ApiMediaTransferContractDefinition definition =
                new ApiMediaTransferContractDefinition(registration(), upload);

        ApiMediaTransferContractDefinition.UploadAuthority retained = assertInstanceOf(
                ApiMediaTransferContractDefinition.UploadAuthority.class,
                definition.authority()
        );
        assertEquals(
                "bounded-upload-transfer-identity",
                retained.boundedTransferIdentityReference()
        );
        assertEquals(
                "upload-is-not-validation-attachment-or-exposure",
                retained.completionSeparationRuleReference()
        );
    }

    @Test
    void retains_protected_download_authority_and_optional_delivery_reference() {
        ApiMediaTransferContractDefinition.BoundedDeliveryReferenceAuthority delivery =
                new ApiMediaTransferContractDefinition.BoundedDeliveryReferenceAuthority(
                        "bounded-delivery-authority",
                        Optional.of("short-delivery-lifetime"),
                        "applicable-exposure-compatible",
                        "delivery-reference-is-not-media-asset-identity"
                );
        ApiMediaTransferContractDefinition.DownloadAuthority download =
                new ApiMediaTransferContractDefinition.DownloadAuthority(
                        "current-principal-and-context",
                        "trusted-merchant-scope",
                        Optional.of("current-customer-media-relationship"),
                        "media-exposure-and-access-authority",
                        Optional.of(delivery)
                );
        ApiMediaTransferContractDefinition definition =
                new ApiMediaTransferContractDefinition(registration(), download);

        ApiMediaTransferContractDefinition.DownloadAuthority retained = assertInstanceOf(
                ApiMediaTransferContractDefinition.DownloadAuthority.class,
                definition.authority()
        );
        assertEquals(
                "current-customer-media-relationship",
                retained.businessRelationshipRuleReference().orElseThrow()
        );
        assertEquals(delivery, retained.boundedDeliveryReferenceAuthority().orElseThrow());
    }

    @Test
    void permits_download_without_relationship_or_temporary_delivery_reference() {
        ApiMediaTransferContractDefinition.DownloadAuthority download =
                new ApiMediaTransferContractDefinition.DownloadAuthority(
                        "current-principal-and-context",
                        "trusted-merchant-scope",
                        Optional.empty(),
                        "media-exposure-and-access-authority",
                        Optional.empty()
                );

        assertTrue(download.businessRelationshipRuleReference().isEmpty());
        assertTrue(download.boundedDeliveryReferenceAuthority().isEmpty());
    }

    @Test
    void requires_media_transfer_registration_and_non_blank_authority_references() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiMediaTransferContractDefinition(
                        registration(ApiContractKind.COMMAND),
                        uploadAuthority()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiMediaTransferContractDefinition.UploadAuthority(
                        "scope",
                        "role-context",
                        "constraints",
                        "identity",
                        "expiry",
                        " ",
                        "validation",
                        "separation"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiMediaTransferContractDefinition.DownloadAuthority(
                        "principal",
                        "scope",
                        Optional.of(" "),
                        "exposure",
                        Optional.empty()
                )
        );
    }

    private static ApiMediaTransferContractDefinition.UploadAuthority uploadAuthority() {
        return new ApiMediaTransferContractDefinition.UploadAuthority(
                "scope",
                "role-context",
                "constraints",
                "identity",
                "expiry",
                "destination",
                "validation",
                "separation"
        );
    }

    private static ApiContractRegistration registration() {
        return registration(ApiContractKind.MEDIA_TRANSFER);
    }

    private static ApiContractRegistration registration(ApiContractKind kind) {
        return new ApiContractRegistration(
                new ApiContractIdentity("media", "merchant-media-transfer"),
                ApiSurfaceClass.MERCHANT_OPERATIONAL,
                kind,
                new ApiOwnerContractReference("media", "bounded-media-transfer"),
                "trusted-merchant-media-scope"
        );
    }
}
