package grandrue.semantic.configuration;

/**
 * Initial bounded value-domain algebra accepted by MS-PROT-047.
 *
 * <p>Presence here does not imply that every domain already has a production
 * resolver. The current foundation materialises registered Policy values as
 * ENUM decisions only.</p>
 */
public enum CapabilityConfigurationValueDomain {
    BOOLEAN,
    ENUM,
    BOUNDED_SCALAR,
    REFERENCE,
    STRUCTURED_BOUNDED_VALUE
}
