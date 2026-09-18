package grandrue.commercial;

import grandrue.commercial.MerchantCommercialAgreement;
import grandrue.commercial.StandardPlanLevel;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * One idempotent authoritative paid Commercial Agreement transition command.
 *
 * <p>The expected current identity is the latest committed agreement transition
 * head, not a subscription plan name and not a semantic configuration revision.
 * A scheduled future plan-change intent remains a separate commercial fact until
 * its accepted transition boundary is actually committed.</p>
 */
public record MerchantCommercialAgreementTransition(
        String logicalRequestIdentity,
        Optional<String> expectedCurrentAgreementIdentity,
        MerchantCommercialAgreement candidateAgreement
) {

    public MerchantCommercialAgreementTransition {
        if (logicalRequestIdentity == null || logicalRequestIdentity.isBlank()) {
            throw new IllegalArgumentException(
                    "Commercial transition logical request identity must not be blank"
            );
        }
        expectedCurrentAgreementIdentity = Objects.requireNonNull(
                expectedCurrentAgreementIdentity,
                "expectedCurrentAgreementIdentity"
        );
        expectedCurrentAgreementIdentity.ifPresent(value -> {
            if (value.isBlank()) {
                throw new IllegalArgumentException(
                        "Expected current Commercial Agreement identity must not be blank"
                );
            }
        });
        Objects.requireNonNull(candidateAgreement, "candidateAgreement");
        if (candidateAgreement.planRevision().level() == StandardPlanLevel.FREE) {
            throw new IllegalArgumentException(
                    "Standing FREE commercial baseline must not be represented as a paid Commercial Agreement"
            );
        }
    }

    public MerchantScope merchantScope() {
        return candidateAgreement.merchantScope();
    }
}
