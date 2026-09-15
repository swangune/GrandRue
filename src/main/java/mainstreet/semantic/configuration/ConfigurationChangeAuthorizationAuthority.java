package mainstreet.semantic.configuration;

import mainstreet.runtime.TrustedExecutionContext;

/**
 * Current origin-qualified authority to materialise proposed intent, not approval.
 * Implementations must validate the source and current principal within the owning
 * transaction, using locks/fences for mutable authority. There is no default grant.
 * Authority: MS-PROT-040 v1.0,
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * §14 — Change-set provenance; §15 — Merchant-initiated changes; §16 — Inference-proposed changes.
 */
@FunctionalInterface
public interface ConfigurationChangeAuthorizationAuthority {
    boolean mayMaterialise(ConfigurationChangeSet changeSet, TrustedExecutionContext context);
}
