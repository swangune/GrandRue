package mainstreet.semantic.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.TrustedExecutionContext;
import java.util.Optional;

/**
 * Configuration-owned durable change/revision boundary; it cannot approve or activate.
 * Authority: MS-PROT-040 v1.1,
 * designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md,
 * §2 — Ownership; §3 — Configuration Revision identity and immutability.
 */
public interface ConfigurationChangeAuthority {
    ChangedConfigurationRevision materialise(MaterialiseConfigurationChangeCommand command,
                                             TrustedExecutionContext context);
    Optional<ChangedConfigurationRevision> revisionForChange(MerchantScope scope, String changeSetIdentifier);
}
