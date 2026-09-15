package mainstreet.prototype;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Prototype-only Appointment proposition/configuration facts.
 *
 * <p>The public subject reference is deliberately distinct from the internal
 * scheduled-operation and capacity references. This fixture stands in for the
 * accepted Offering/configuration authorities; it is not a production
 * catalogue model.</p>
 */
final class PrototypeAppointmentSubjectConfiguration {

    private static final Map<String, List<Subject>> SUBJECTS_BY_MERCHANT = Map.of(
            "prototype-consultant",
            List.of(new Subject(
                    "structural-consultation",
                    "Structural consultation",
                    "consultation.perform",
                    "prototype-consultant-capacity-1"
            )),
            "prototype-gardener",
            List.of(new Subject(
                    "garden-maintenance",
                    "Garden maintenance visit",
                    "gardening.perform",
                    "prototype-gardener-capacity-1"
            )),
            "prototype-gardener-bookable",
            List.of(new Subject(
                    "garden-maintenance",
                    "Garden maintenance visit",
                    "gardening.perform",
                    "prototype-gardener-bookable-capacity-1"
            )),
            "prototype-gardener-evolving",
            List.of(new Subject(
                    "garden-maintenance",
                    "Garden maintenance visit",
                    "gardening.perform",
                    "prototype-gardener-evolving-capacity-1"
            ))
    );

    private PrototypeAppointmentSubjectConfiguration() {
    }

    static List<Subject> subjectsFor(String merchantIdentifier) {
        return SUBJECTS_BY_MERCHANT.getOrDefault(merchantIdentifier, List.of());
    }

    static Set<String> merchants() {
        return SUBJECTS_BY_MERCHANT.keySet();
    }

    static Optional<Subject> byScheduledOperation(
            String merchantIdentifier,
            String scheduledOperationIdentifier
    ) {
        return subjectsFor(merchantIdentifier).stream()
                .filter(subject -> subject.scheduledOperationIdentifier().equals(
                        scheduledOperationIdentifier
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
            String scheduledOperationIdentifier,
            String capacitySubjectReference
    ) {
        Subject {
            requireValue(publicSubjectReference, "Public subject reference");
            requireValue(label, "Label");
            requireValue(scheduledOperationIdentifier, "Scheduled operation identifier");
            requireValue(capacitySubjectReference, "Capacity subject reference");
        }
    }

    private static void requireValue(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
