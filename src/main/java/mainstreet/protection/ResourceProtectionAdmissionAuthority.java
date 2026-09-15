package mainstreet.protection;

/**
 * Platform Resource Protection authority for one attempted protected
 * consumption. The caller supplies a stable consumption identity appropriate
 * to the policy's measurement basis; Resource Protection does not infer
 * business idempotency from transport retries.
 */
public interface ResourceProtectionAdmissionAuthority {

    ProtectionAdmissionDecision admit(
            ProtectionPolicy policy,
            ProtectionSubject subject,
            String consumptionIdentity,
            long units,
            boolean owningExecutionSupportsDeferral
    );
}
