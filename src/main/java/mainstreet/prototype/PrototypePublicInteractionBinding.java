package mainstreet.prototype;

/**
 * Prototype transport-facing projection value for one publicly selectable
 * subject participating in a registered public interaction.
 *
 * <p>This is not an Operational Object and does not grant execution authority.</p>
 */
public record PrototypePublicInteractionBinding(
        String subjectReference,
        String label
) {
    public PrototypePublicInteractionBinding {
        requireValue(subjectReference, "Subject reference");
        requireValue(label, "Label");
    }

    private static void requireValue(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
