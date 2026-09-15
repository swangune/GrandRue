package mainstreet.surface;

/**
 * Opaque request-local identity for one logical bounded Projection read.
 *
 * <p>The binding carries no business meaning, credential, repository-query
 * permission or reusable authority. It exists only to preserve same-read
 * affinity across the trusted read spine.</p>
 */
public sealed interface BoundedProjectionReadBinding
        permits RuntimeBoundedProjectionReadBinding {
}
