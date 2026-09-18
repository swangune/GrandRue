package grandrue.runtime;


/**
 * Capability-owned application contract for fulfilling one typed command
 * against one applicable executable operation.
 *
 * <p>The implementation owns authoritative target resolution, final guard and
 * invariant validation, transaction control, and mutation through its
 * capability contracts. This port does not turn executable effects into
 * generic mutation instructions.</p>
 *
 * @param <C> capability-specific command representation
 */
@FunctionalInterface
public interface CapabilityOperationHandler<C> {

    /**
     * Fulfils the command and reports outcome evidence constrained by the
     * captured executable operation.
     */
    OperationFulfilment fulfill(
            C command,
            OperationExecutionContext context
    );
}
