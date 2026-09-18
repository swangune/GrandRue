package grandrue.identitysecurity;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdentitySecurityGenerationServiceTest {

    private static final Instant NOW =
            Instant.parse("2026-08-28T21:00:00Z");

    @Test
    void authentication_security_establishment_initializes_a_generated_generation() {
        AtomicReference<IdentitySecurityGeneration> initialized =
                new AtomicReference<>();
        IdentitySecurityGenerationManagement management =
                new RecordingManagement(initialized);
        Supplier<String> generationReferences = () -> "generation-generated-1";
        IdentitySecurityGenerationService service =
                new IdentitySecurityGenerationService(
                        management,
                        Clock.fixed(NOW, ZoneOffset.UTC),
                        generationReferences
                );

        service.initialize("identity-42");

        IdentitySecurityGeneration candidate = initialized.get();
        assertEquals("identity-42", candidate.identityReference());
        assertEquals("generation-generated-1", candidate.generationReference());
        assertEquals(NOW, candidate.establishedAt());
    }

    private static final class RecordingManagement
            implements IdentitySecurityGenerationManagement {

        private final AtomicReference<IdentitySecurityGeneration> initialized;

        private RecordingManagement(
                AtomicReference<IdentitySecurityGeneration> initialized
        ) {
            this.initialized = initialized;
        }

        @Override
        public IdentitySecurityGeneration initialize(
                String identityReference,
                String initialGenerationReference,
                Instant establishedAt
        ) {
            IdentitySecurityGeneration generation =
                    new IdentitySecurityGeneration(
                            identityReference,
                            initialGenerationReference,
                            1,
                            establishedAt,
                            Optional.empty()
                    );
            initialized.set(generation);
            return generation;
        }

        @Override
        public Optional<IdentitySecurityGeneration> state(
                String identityReference
        ) {
            return Optional.ofNullable(initialized.get());
        }

        @Override
        public IdentitySecurityGeneration rotate(
                IdentitySecurityRotationCommand command
        ) {
            throw new UnsupportedOperationException("Not required");
        }
    }
}
