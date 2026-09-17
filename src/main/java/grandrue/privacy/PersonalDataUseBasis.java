package grandrue.privacy;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Bounded authority/evidence record for one personal-data subject, data scope,
 * purpose and intended audience.
 *
 * <p>This is not a universal consent record and does not establish Exposure or
 * guardian/legal-relationship truth.</p>
 */
public record PersonalDataUseBasis(
        String basisIdentity,
        DataSubjectReference subject,
        String dataScopeIdentifier,
        DataUsePurpose purpose,
        String audienceScopeIdentifier,
        String authoritySource,
        String evidenceReference,
        Instant effectiveFrom,
        Optional<Instant> effectiveUntilExclusive
) {
    public PersonalDataUseBasis {
        requireIdentifier(basisIdentity, "basisIdentity");
        Objects.requireNonNull(subject, "subject");
        requireIdentifier(dataScopeIdentifier, "dataScopeIdentifier");
        Objects.requireNonNull(purpose, "purpose");
        requireIdentifier(audienceScopeIdentifier, "audienceScopeIdentifier");
        requireIdentifier(authoritySource, "authoritySource");
        requireIdentifier(evidenceReference, "evidenceReference");
        Objects.requireNonNull(effectiveFrom, "effectiveFrom");
        effectiveUntilExclusive = Objects.requireNonNull(
                effectiveUntilExclusive,
                "effectiveUntilExclusive"
        );
        effectiveUntilExclusive.ifPresent(end -> {
            if (!end.isAfter(effectiveFrom)) {
                throw new IllegalArgumentException(
                        "Personal-data use basis end must be after its start"
                );
            }
        });
    }

    public boolean isEffectiveAt(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        if (instant.isBefore(effectiveFrom)) {
            return false;
        }
        return effectiveUntilExclusive.map(instant::isBefore).orElse(true);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
