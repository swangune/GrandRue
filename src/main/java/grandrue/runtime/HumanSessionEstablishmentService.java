package grandrue.runtime;

import mainstreet.runtime.SessionRecord;
import mainstreet.runtime.SessionRecordStore;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Establishes the durable ADR-014 human Session after an upstream
 * authentication mechanism has already produced trusted identity/security
 * evidence.
 *
 * <p>This service does not authenticate a human and does not interpret
 * WebAuthn/provider-specific proof. Its responsibility begins after successful
 * authentication and is limited to fixation-safe Session establishment.</p>
 */
public final class HumanSessionEstablishmentService {

    private final SessionRecordStore sessionRecordStore;
    private final SecureRandom secureRandom;
    private final Supplier<String> sessionIdentitySupplier;

    public HumanSessionEstablishmentService(SessionRecordStore sessionRecordStore) {
        this(
                sessionRecordStore,
                new SecureRandom(),
                () -> UUID.randomUUID().toString()
        );
    }

    HumanSessionEstablishmentService(
            SessionRecordStore sessionRecordStore,
            SecureRandom secureRandom,
            Supplier<String> sessionIdentitySupplier
    ) {
        this.sessionRecordStore = Objects.requireNonNull(
                sessionRecordStore,
                "sessionRecordStore"
        );
        this.secureRandom = Objects.requireNonNull(secureRandom, "secureRandom");
        this.sessionIdentitySupplier = Objects.requireNonNull(
                sessionIdentitySupplier,
                "sessionIdentitySupplier"
        );
    }

    public EstablishedHumanSession establish(
            String identityReference,
            Instant authenticatedAt,
            String authenticationAssuranceReference,
            String authenticationMethodReference,
            Instant absoluteExpiry,
            String securityGenerationReference
    ) {
        OpaqueSessionCredential credential =
                OpaqueSessionCredential.generate(secureRandom);
        String sessionIdentity = sessionIdentitySupplier.get();
        if (sessionIdentity == null || sessionIdentity.isBlank()) {
            throw new IllegalStateException(
                    "Session identity supplier returned a blank identity"
            );
        }

        SessionRecord candidate = new SessionRecord(
                sessionIdentity,
                identityReference,
                credential.verifier(),
                authenticatedAt,
                authenticationAssuranceReference,
                authenticationMethodReference,
                absoluteExpiry,
                authenticatedAt,
                securityGenerationReference,
                java.util.Optional.empty(),
                java.util.Optional.empty()
        );
        SessionRecord committed = sessionRecordStore.create(candidate);

        if (!candidate.equals(committed)) {
            throw new IllegalStateException(
                    "Session persistence returned different establishment evidence"
            );
        }

        return new EstablishedHumanSession(
                committed.sessionIdentity(),
                credential
        );
    }
}
