package mainstreet.semantic.configuration;

import mainstreet.application.MerchantScope;

import java.util.Optional;

/** Durable authority for exact successful validation/package evidence. */
public interface ConfigurationValidationEvidenceAuthority {

    ConfigurationValidationEvidence recordSuccessfulValidation(
            RecordConfigurationValidationEvidenceCommand command
    );

    Optional<ConfigurationValidationEvidence> evidence(
            MerchantScope merchantScope,
            String validationEvidenceIdentifier
    );
}
