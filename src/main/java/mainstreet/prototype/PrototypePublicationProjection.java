package mainstreet.prototype;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Deterministic publication projection used to exercise information-capable
 * merchants before Publication mutation execution is formalised.
 *
 * <p>Fixture content is merchant-scoped data. Merchant identity does not
 * select different projection behaviour.</p>
 */
public final class PrototypePublicationProjection {

    private static final PrototypePublicationView SCHOLARSHIP =
            new PrototypePublicationView(
                    "publication-1",
                    "Engineering Scholarship 2026",
                    "A seeded information-publication example for the pre-UI prototype.",
                    "PUBLISHED",
                    1,
                    Optional.of(Instant.parse("2026-08-26T00:00:00Z")),
                    Optional.empty()
            );

    private static final PrototypePublicationView GARDEN_PROJECT =
            new PrototypePublicationView(
                    "garden-project-1",
                    "Courtyard Garden Transformation",
                    "A seeded completed-project showcase for the merchant-choice prototype.",
                    "PUBLISHED",
                    1,
                    Optional.of(Instant.parse("2026-08-26T00:00:00Z")),
                    Optional.empty()
            );

    private static final Map<String, List<PrototypePublicationView>>
            SEEDED_PUBLICATIONS = Map.of(
                    "prototype-publisher", List.of(SCHOLARSHIP),
                    "prototype-gardener-showcase", List.of(GARDEN_PROJECT),
                    "prototype-gardener-bookable", List.of(GARDEN_PROJECT),
                    "prototype-gardener-evolving", List.of(GARDEN_PROJECT)
            );

    private final PrototypeMerchantRuntime merchantRuntime;

    public PrototypePublicationProjection(
            PrototypeMerchantRuntime merchantRuntime
    ) {
        this.merchantRuntime = Objects.requireNonNull(
                merchantRuntime,
                "merchantRuntime"
        );
    }

    public List<PrototypePublicationView> publications(
            String merchantIdentifier
    ) {
        PrototypeMerchantView merchant = merchantRuntime.merchant(
                merchantIdentifier
        );
        if (!merchant.capabilityIdentifiers().contains("publication")) {
            throw new IllegalArgumentException(
                    "Publication is not active for this prototype merchant"
            );
        }
        return SEEDED_PUBLICATIONS.getOrDefault(
                merchantIdentifier,
                List.of()
        );
    }
}
