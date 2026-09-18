package grandrue.surface;

/**
 * Opaque identity proving that trusted observation values were established
 * inside the same bounded request.
 *
 * <p>The binding carries no reusable credential or authority and is
 * deliberately non-serializable.</p>
 */
public sealed interface ObservationRequestBinding
        permits DefaultObservationRequestBinding {
}
