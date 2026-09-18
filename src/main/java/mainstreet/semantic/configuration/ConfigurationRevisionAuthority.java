package mainstreet.semantic.configuration;

import grandrue.application.MerchantScope;
import grandrue.onboarding.InitialConfigurationIntentIdentity;

import java.util.Optional;

/** Authoritative immutable Configuration Revision materialisation and lookup. */
public interface ConfigurationRevisionAuthority {

    MerchantConfigurationRevision materialiseInitial(
            MaterialiseInitialConfigurationRevisionCommand command
    );

    /** Initial-revision lookup retaining its onboarding-specific provenance. */
    Optional<MerchantConfigurationRevision> revision(
            MerchantScope merchantScope,
            String configurationRevisionIdentifier
    );

    Optional<MerchantConfigurationRevision> revisionForInitialIntent(
            InitialConfigurationIntentIdentity intentIdentity
    );

    /**
     * Common immutable contents; replacements do not manufacture initial provenance.
     * Authority: MS-PROT-040 v1.1,
     * designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md,
     * §2 — Ownership; §3 — Configuration Revision identity and immutability.
     */
    default Optional<MerchantConfiguration> configuration(MerchantScope scope, String revisionIdentifier) {
        return revision(scope, revisionIdentifier).map(MerchantConfigurationRevision::configuration);
    }
}
