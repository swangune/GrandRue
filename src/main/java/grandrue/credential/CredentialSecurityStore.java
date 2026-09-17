package grandrue.credential;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Security-boundary persistence for non-secret credential binding/generation
 * metadata. It does not store or return raw credential material.
 */
public interface CredentialSecurityStore {

    CredentialBinding registerBinding(CredentialBinding binding);

    CredentialGeneration registerGeneration(
            CredentialGeneration generation,
            CredentialGenerationPolicy initialPolicy
    );

    CredentialGenerationPolicy transitionGeneration(
            String generationIdentity,
            CredentialGenerationState expectedCurrentState,
            CredentialGenerationPolicy nextPolicy
    );

    Optional<CredentialBinding> binding(String bindingIdentity);

    Optional<CredentialGenerationPolicy> currentPolicy(String generationIdentity);

    List<CredentialGeneration> eligibleGenerations(
            String bindingIdentity,
            CredentialTechnicalUse technicalUse,
            Instant instant
    );
}
