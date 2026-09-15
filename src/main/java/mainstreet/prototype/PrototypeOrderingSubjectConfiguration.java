package mainstreet.prototype;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Prototype-only Ordering proposition/configuration facts. */
final class PrototypeOrderingSubjectConfiguration {

    private static final Map<String, List<Subject>> SUBJECTS_BY_MERCHANT = Map.of(
            "prototype-retailer",
            List.of(new Subject(
                    "milk-2l",
                    "Milk 2L",
                    "sku-1",
                    "sku-1",
                    "EACH",
                    2500L,
                    "prototype-offering:sku-1@1",
                    10L
            ))
    );

    private PrototypeOrderingSubjectConfiguration() {
    }

    static List<Subject> subjectsFor(String merchantIdentifier) {
        return SUBJECTS_BY_MERCHANT.getOrDefault(merchantIdentifier, List.of());
    }

    static Set<String> merchants() {
        return SUBJECTS_BY_MERCHANT.keySet();
    }

    static Optional<Subject> byOrderableSubject(
            String merchantIdentifier,
            String orderableSubjectReference
    ) {
        return subjectsFor(merchantIdentifier).stream()
                .filter(subject -> subject.orderableSubjectReference().equals(
                        orderableSubjectReference
                ))
                .findFirst();
    }

    static Optional<Subject> byPublicReference(
            String merchantIdentifier,
            String publicSubjectReference
    ) {
        return subjectsFor(merchantIdentifier).stream()
                .filter(subject -> subject.publicSubjectReference().equals(
                        publicSubjectReference
                ))
                .findFirst();
    }

    record Subject(
            String publicSubjectReference,
            String label,
            String orderableSubjectReference,
            String stockSubjectReference,
            String orderQuantityUnitIdentifier,
            long priceMinorUnits,
            String commitmentProvenance,
            long initialStockOnHand
    ) {
        Subject {
            requireValue(publicSubjectReference, "Public subject reference");
            requireValue(label, "Label");
            requireValue(orderableSubjectReference, "Orderable subject reference");
            requireValue(stockSubjectReference, "Stock subject reference");
            requireValue(orderQuantityUnitIdentifier, "Order quantity unit identifier");
            requireValue(commitmentProvenance, "Commitment provenance");
            if (priceMinorUnits < 0) {
                throw new IllegalArgumentException("Price minor units must not be negative");
            }
            if (initialStockOnHand < 0) {
                throw new IllegalArgumentException("Initial stock must not be negative");
            }
        }
    }

    private static void requireValue(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
