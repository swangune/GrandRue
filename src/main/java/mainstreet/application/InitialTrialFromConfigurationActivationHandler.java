package mainstreet.application;

import mainstreet.commercial.InitialFullExperienceTrial;
import mainstreet.commercial.InitialFullExperienceTrialEstablisher;
import mainstreet.semantic.configuration.ConfigurationActivation;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Post-commit application reaction from a committed configuration activation
 * to Commercial's one-time initial full-experience trial establishment.
 *
 * <p>This orchestrator owns neither Configuration nor Commercial truth. It
 * translates the authoritative activation fact into a Commercial command
 * candidate only for the merchant's first activation.</p>
 */
public final class InitialTrialFromConfigurationActivationHandler {

    private final InitialFullExperienceTrialEstablisher trialEstablisher;
    private final Function<ConfigurationActivation, String> trialIdentityFactory;

    public InitialTrialFromConfigurationActivationHandler(
            InitialFullExperienceTrialEstablisher trialEstablisher,
            Function<ConfigurationActivation, String> trialIdentityFactory
    ) {
        this.trialEstablisher = Objects.requireNonNull(
                trialEstablisher,
                "trialEstablisher"
        );
        this.trialIdentityFactory = Objects.requireNonNull(
                trialIdentityFactory,
                "trialIdentityFactory"
        );
    }

    public Optional<InitialFullExperienceTrial> handle(
            ConfigurationActivation activation
    ) {
        Objects.requireNonNull(activation, "activation");

        if (activation.replacedConfigurationRevisionIdentifier().isPresent()) {
            return Optional.empty();
        }

        InitialFullExperienceTrial candidate = new InitialFullExperienceTrial(
                requireTrialIdentity(trialIdentityFactory.apply(activation)),
                new MerchantScope(activation.merchantIdentifier()),
                activation.configurationRevisionIdentifier(),
                activation.activationRequestIdentifier(),
                activation.activatedAt()
        );
        return Optional.of(trialEstablisher.establish(candidate));
    }

    private static String requireTrialIdentity(String identity) {
        if (identity == null || identity.isBlank()) {
            throw new IllegalArgumentException(
                    "Trial identity factory must return a non-blank identity"
            );
        }
        return identity;
    }
}
