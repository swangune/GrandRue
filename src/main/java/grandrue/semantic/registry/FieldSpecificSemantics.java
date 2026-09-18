package grandrue.semantic.registry;

/**
 * Marker stating that the FieldDefinition itself owns the field's semantic
 * meaning and does not require a separately reusable DataConcept.
 */
public record FieldSpecificSemantics() implements FieldSemanticBasis {
}
