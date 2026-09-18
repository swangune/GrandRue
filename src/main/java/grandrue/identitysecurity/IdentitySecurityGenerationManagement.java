package grandrue.identitysecurity;

import grandrue.runtime.IdentitySecurityGenerationAuthority;

import java.time.Instant;
import java.util.Optional;

/**
 * Identity Security-owned durable generation management boundary.
 */
public interface IdentitySecurityGenerationManagement
        extends IdentitySecurityGenerationAuthority {

    IdentitySecurityGeneration initialize(
            String identityReference,
            String initialGenerationReference,
            Instant establishedAt
    );

    Optional<IdentitySecurityGeneration> state(String identityReference);

    IdentitySecurityGeneration rotate(IdentitySecurityRotationCommand command);

    @Override
    default String currentSecurityGenerationReference(String identityReference) {
        return state(identityReference)
                .map(IdentitySecurityGeneration::generationReference)
                .orElse(null);
    }
}
