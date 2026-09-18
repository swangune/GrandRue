package grandrue.application;

import grandrue.runtime.AuthenticationProvenance;
import grandrue.runtime.TrustedExecutionContext;
import mainstreet.semantic.configuration.ConfigurationActivation;
import mainstreet.semantic.configuration.ConfigurationActivationRequest;
import mainstreet.semantic.configuration.ConfigurationActivationResult;
import mainstreet.semantic.configuration.ConfigurationActivationStatus;
import mainstreet.semantic.configuration.ConfigurationPublication;
import mainstreet.semantic.configuration.ConfigurationRelease;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;

import java.util.Objects;
import java.util.Optional;

/**
 * Trusted application boundary for human Merchant Configuration activation.
 *
 * <p>This service binds caller intent to a Main Street-established
 * TrustedExecutionContext before a new activation attempt may reach the
 * authoritative activation boundary.</p>
 *
 * <p>Mutable Merchant Controller, account lifecycle and suspension authority
 * are deliberately not frozen into this context. They are re-evaluated inside
 * the underlying serialized activation transaction by the configured
 * ConfigurationActivationAuthorizationAuthority.</p>
 *
 * Authority: approved MS-PROT-040 v1.7.
 */
public final class ConfigurationActivationApplicationService {

    private final ConfigurationPublication publication;
    private final ConfigurationReleaseActivation activation;

    public ConfigurationActivationApplicationService(
            ConfigurationPublication publication,
            ConfigurationReleaseActivation activation
    ) {
        this.publication =
                Objects.requireNonNull(
                        publication,
                        "publication"
                );

        this.activation =
                Objects.requireNonNull(
                        activation,
                        "activation"
                );
    }

    public ConfigurationActivationResult activate(
            ConfigurationActivationRequest request,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(
                request,
                "request"
        );

        /*
         * Lost-acknowledgement replay is historical reconciliation rather
         * than a new business mutation. We still require a trusted context
         * matching the original initiating principal and Merchant Scope, but
         * current Controller authority is intentionally not re-evaluated here.
         *
         * JooqConfigurationReleaseActivation subsequently verifies that the
         * reused logical request carries exactly the same release, expected
         * current revision and initiating principal as the committed fact.
         */
        Optional<ConfigurationActivation> committed =
                activation.committedActivation(
                        request.activationRequestIdentifier()
                );

        if (committed.isPresent()) {
            ConfigurationActivation historical =
                    committed.orElseThrow();

            if (!matchesTrustedContext(
                    request,
                    trustedContext,
                    historical.merchantIdentifier()
            )) {
                return ConfigurationActivationResult.rejected(
                        ConfigurationActivationStatus
                                .AUTHORISATION_REJECTION
                );
            }

            return activation.activate(request);
        }

        Optional<ConfigurationRelease> release =
                publication.release(
                        request.releaseIdentifier()
                );

        /*
         * There is no candidate against which a Merchant Scope can be bound.
         * Delegate to the underlying authority so its existing stable
         * VALIDATION_REJECTION behaviour remains canonical. No mutation can
         * occur because the release candidate is absent.
         */
        if (release.isEmpty()) {
            return activation.activate(request);
        }

        ConfigurationRelease candidate =
                release.orElseThrow();

        if (!matchesTrustedContext(
                request,
                trustedContext,
                candidate.merchantIdentifier()
        )) {
            return ConfigurationActivationResult.rejected(
                    ConfigurationActivationStatus
                            .AUTHORISATION_REJECTION
            );
        }

        return activation.activate(request);
    }

    private static boolean matchesTrustedContext(
            ConfigurationActivationRequest request,
            TrustedExecutionContext context,
            String expectedMerchantIdentifier
    ) {
        if (context == null
                || context.authentication().isEmpty()) {
            return false;
        }

        if (!context
                .merchantScope()
                .merchantIdentifier()
                .equals(
                        expectedMerchantIdentifier
                )) {
            return false;
        }

        String trustedPrincipalIdentifier =
                context.principal().identifier();

        if (!trustedPrincipalIdentifier.equals(
                request.initiatingPrincipalIdentifier()
        )) {
            return false;
        }

        AuthenticationProvenance authentication =
                context.authentication()
                        .orElseThrow();

        return authentication
                .identityIdentifier()
                .equals(
                        trustedPrincipalIdentifier
                );
    }
}