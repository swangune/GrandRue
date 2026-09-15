package mainstreet.prototype;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Prototype-only Booking subject/configuration facts. */
final class PrototypeBookingSubjectConfiguration {

    private static final Map<String, List<Subject>> SUBJECTS_BY_MERCHANT = Map.of(
            "prototype-motel",
            List.of(new Subject(
                    "standard-room",
                    "Standard room",
                    "standard-room",
                    "prototype-standard-room-capacity-1"
            )),
            "prototype-daycare",
            List.of(new Subject(
                    "full-day-care-session",
                    "Full-day care session",
                    "daycare-session",
                    "prototype-daycare-capacity-1"
            ))
    );

    private PrototypeBookingSubjectConfiguration() {
    }

    static List<Subject> subjectsFor(String merchantIdentifier) {
        return SUBJECTS_BY_MERCHANT.getOrDefault(merchantIdentifier, List.of());
    }

    static Set<String> merchants() {
        return SUBJECTS_BY_MERCHANT.keySet();
    }

    static Optional<Subject> byBookedSubject(
            String merchantIdentifier,
            String bookedSubjectReference
    ) {
        return subjectsFor(merchantIdentifier).stream()
                .filter(subject -> subject.bookedSubjectReference().equals(
                        bookedSubjectReference
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
            String bookedSubjectReference,
            String allocationSubjectReference
    ) {
        Subject {
            requireValue(publicSubjectReference, "Public subject reference");
            requireValue(label, "Label");
            requireValue(bookedSubjectReference, "Booked subject reference");
            requireValue(allocationSubjectReference, "Allocation subject reference");
        }
    }

    private static void requireValue(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
