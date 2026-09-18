package grandrue.semantic.configuration;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Process-local atomic configuration-activation adapter implementing the
 * accepted base-revision CAS, approval affinity, authorisation, semantic
 * compatibility and committed request-idempotency semantics.
 *
 * <p>Historical committed-request reconciliation is valid only while the
 * exact activation still owns the current pointer.</p>
 *
 * <p>Authority: MS-PROT-040 v1.8,
 * {@code designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision
 * Affinity Amendment.md}, §14 — Retry and Idempotency.</p>
 */
public final class InMemoryConfigurationReleaseActivation
        implements ConfigurationReleaseActivation {

    private final ConfigurationPublication publication;
    private final ConfigurationRevisionApprovalAuthority approvalAuthority;
    private final ConfigurationActivationAuthorizationAuthority
            authorizationAuthority;
    private final SemanticCompatibilityAuthority compatibilityAuthority;
    private final Clock clock;

    private State state = State.empty();

    /**
     * Backward-compatible construction is fail-closed for semantic registry
     * transitions. Callers that need to activate across registry versions must
     * supply an explicit trusted compatibility authority.
     */
    public InMemoryConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            ConfigurationActivationAuthorizationAuthority authorizationAuthority,
            Clock clock
    ) {
        this(
                publication,
                approvalAuthority,
                authorizationAuthority,
                (merchant, source, target) -> Optional.empty(),
                clock
        );
    }

    public InMemoryConfigurationReleaseActivation(
            ConfigurationPublication publication,
            ConfigurationRevisionApprovalAuthority approvalAuthority,
            ConfigurationActivationAuthorizationAuthority authorizationAuthority,
            SemanticCompatibilityAuthority compatibilityAuthority,
            Clock clock
    ) {
        this.publication = Objects.requireNonNull(publication, "publication");
        this.approvalAuthority = Objects.requireNonNull(
                approvalAuthority,
                "approvalAuthority"
        );
        this.authorizationAuthority = Objects.requireNonNull(
                authorizationAuthority,
                "authorizationAuthority"
        );
        this.compatibilityAuthority = Objects.requireNonNull(
                compatibilityAuthority,
                "compatibilityAuthority"
        );
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    @Override
    public synchronized ConfigurationActivationResult activate(
            ConfigurationActivationRequest request
    ) {
        Objects.requireNonNull(request, "request");

        CommittedRequest committed = state.committedByRequestIdentifier().get(
                request.activationRequestIdentifier()
        );
        if (committed != null) {
            if (!committed.request().equals(request)) {
                throw new IllegalArgumentException(
                        "Activation request identity is already bound to different intent: "
                                + request.activationRequestIdentifier()
                );
            }

            ConfigurationActivation currentActivation =
                    state.currentActivationByMerchant().get(
                            committed.activation().merchantIdentifier()
                    );

            if (currentActivation == null
                    || !currentActivation
                    .activationRequestIdentifier()
                    .equals(
                            committed.activation()
                                    .activationRequestIdentifier()
                    )) {
                return ConfigurationActivationResult.rejected(
                        ConfigurationActivationStatus.ACTIVATION_CONFLICT
                );
            }

            return ConfigurationActivationResult.success(
                    committed.activation()
            );
        }

        Optional<ConfigurationRelease> releaseCandidate =
                publication.release(request.releaseIdentifier());
        if (releaseCandidate.isEmpty()) {
            return ConfigurationActivationResult.rejected(
                    ConfigurationActivationStatus.VALIDATION_REJECTION
            );
        }
        ConfigurationRelease release = releaseCandidate.orElseThrow();

        Optional<ConfigurationRevisionApproval> approval =
                approvalAuthority.approvalFor(
                        release.merchantIdentifier(),
                        release.configurationIdentifier()
                );
        if (approval.isEmpty()
                || !approvalMatchesRelease(approval.orElseThrow(), release)) {
            return ConfigurationActivationResult.rejected(
                    ConfigurationActivationStatus.APPROVAL_REQUIRED
            );
        }

        if (!authorizationAuthority.isAuthorized(
                request.initiatingPrincipalIdentifier(),
                release.merchantIdentifier(),
                release.configurationIdentifier()
        )) {
            return ConfigurationActivationResult.rejected(
                    ConfigurationActivationStatus.AUTHORISATION_REJECTION
            );
        }

        ActiveRelease current = state.activeByMerchant().get(
                release.merchantIdentifier()
        );
        Optional<String> currentRevisionIdentifier = current == null
                ? Optional.empty()
                : Optional.of(current.release().configurationIdentifier());

        if (!request.expectedCurrentConfigurationIdentifier().equals(
                currentRevisionIdentifier
        )) {
            return ConfigurationActivationResult.rejected(
                    ConfigurationActivationStatus.ACTIVATION_CONFLICT
            );
        }

        if (!release.configuration().baseConfigurationIdentifier().equals(
                currentRevisionIdentifier
        )) {
            return ConfigurationActivationResult.rejected(
                    ConfigurationActivationStatus.ACTIVATION_CONFLICT
            );
        }

        if (current != null
                && !current.release().semanticRegistryVersion().equals(
                        release.semanticRegistryVersion()
                )
                && !compatibilityPermits(current.release(), release)) {
            return ConfigurationActivationResult.rejected(
                    ConfigurationActivationStatus.VALIDATION_REJECTION
            );
        }

        ConfigurationActivation activation = new ConfigurationActivation(
                request.activationRequestIdentifier(),
                release.merchantIdentifier(),
                release.configurationIdentifier(),
                release.releaseIdentifier(),
                clock.instant(),
                currentRevisionIdentifier
        );

        Map<String, ActiveRelease> updatedActive = new HashMap<>(
                state.activeByMerchant()
        );
        updatedActive.put(
                release.merchantIdentifier(),
                new ActiveRelease(release)
        );

        Map<String, CommittedRequest> updatedCommitted = new HashMap<>(
                state.committedByRequestIdentifier()
        );
        updatedCommitted.put(
                request.activationRequestIdentifier(),
                new CommittedRequest(request, activation)
        );

        Map<String, ConfigurationActivation> updatedCurrentActivations =
                new HashMap<>(
                        state.currentActivationByMerchant()
                );
        updatedCurrentActivations.put(
                release.merchantIdentifier(),
                activation
        );

        state = new State(
                Map.copyOf(updatedActive),
                Map.copyOf(updatedCommitted),
                Map.copyOf(updatedCurrentActivations)
        );
        return ConfigurationActivationResult.success(activation);
    }

    @Override
    public synchronized Optional<ActiveRelease> current(
            String merchantIdentifier
    ) {
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        return Optional.ofNullable(
                state.activeByMerchant().get(merchantIdentifier)
        );
    }

    @Override
    public synchronized Optional<ConfigurationActivation> committedActivation(
            String activationRequestIdentifier
    ) {
        requireIdentifier(
                activationRequestIdentifier,
                "Activation request identifier"
        );
        return Optional.ofNullable(
                state.committedByRequestIdentifier().get(
                        activationRequestIdentifier
                )
        ).map(CommittedRequest::activation);
    }

    private boolean compatibilityPermits(
            ConfigurationRelease source,
            ConfigurationRelease target
    ) {
        return compatibilityAuthority.evidenceFor(
                source.merchantIdentifier(),
                source.releaseIdentifier(),
                target.releaseIdentifier()
        ).filter(evidence -> evidence.matches(source, target))
                .filter(SemanticCompatibilityEvidence::permitsActivation)
                .isPresent();
    }

    private static boolean approvalMatchesRelease(
            ConfigurationRevisionApproval approval,
            ConfigurationRelease release
    ) {
        return approval.merchantIdentifier().equals(release.merchantIdentifier())
                && approval.configurationRevisionIdentifier().equals(
                        release.configurationIdentifier()
                );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private record CommittedRequest(
            ConfigurationActivationRequest request,
            ConfigurationActivation activation
    ) {
    }

    private record State(
            Map<String, ActiveRelease> activeByMerchant,
            Map<String, CommittedRequest> committedByRequestIdentifier,
            Map<String, ConfigurationActivation> currentActivationByMerchant
    ) {

        private static State empty() {
            return new State(Map.of(), Map.of(), Map.of());
        }
    }
}
