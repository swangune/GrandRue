package grandrue.identitysecurity;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Generates the non-secret initial generation reference and delegates durable,
 * idempotent establishment to the Identity Security owner.
 */
public final class IdentitySecurityGenerationService
        implements IdentitySecurityGenerationInitializer {

    private final IdentitySecurityGenerationManagement management;
    private final Clock clock;
    private final Supplier<String> generationReferenceSupplier;

    public IdentitySecurityGenerationService(
            IdentitySecurityGenerationManagement management
    ) {
        this(management, Clock.systemUTC(), () -> UUID.randomUUID().toString());
    }

    public IdentitySecurityGenerationService(
            IdentitySecurityGenerationManagement management,
            Clock clock,
            Supplier<String> generationReferenceSupplier
    ) {
        this.management = Objects.requireNonNull(management, "management");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.generationReferenceSupplier = Objects.requireNonNull(
                generationReferenceSupplier,
                "generationReferenceSupplier"
        );
    }

    @Override
    public void initialize(String identityReference) {
        String generationReference = generationReferenceSupplier.get();
        if (generationReference == null || generationReference.isBlank()) {
            throw new IllegalStateException(
                    "Identity security generation supplier returned a blank reference"
            );
        }
        Instant establishedAt = clock.instant();
        management.initialize(
                identityReference,
                generationReference,
                establishedAt
        );
    }
}
