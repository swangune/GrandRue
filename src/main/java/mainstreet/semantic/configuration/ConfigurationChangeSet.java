package mainstreet.semantic.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.fulfilment.FulfilmentBindingSetRevisionReference;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Desired selections relative to one exact base. Retaining the complete desired
 * selections makes removals explicit and prevents field-level automatic merging.
 * This value carries proposed intent; the owner must resolve its stored base,
 * authorize its source and supply the applicable pinned release separately.
 *
 * Authority: MS-PROT-040 v1.0,
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * §13 — Change set; §19 — Candidate generation; §20 — Configuration diff;
 * §44 — Base-revision concurrency invariant; §45 — Automatic merge is not assumed.
 * Immutable candidate identity: MS-PROT-040 v1.1,
 * designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md,
 * §3 — Configuration Revision identity and immutability.
 */
public record ConfigurationChangeSet(
        MerchantScope merchantScope,
        String changeSetIdentifier,
        String baseConfigurationRevisionIdentifier,
        Set<String> capabilityIdentifiers,
        Set<PolicySelection> policySelections,
        Optional<FulfilmentBindingSetRevisionReference> fulfilmentBindingSetRevisionReference,
        Provenance provenance
) {
    public ConfigurationChangeSet {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(changeSetIdentifier, "Change-set identifier");
        requireIdentifier(baseConfigurationRevisionIdentifier, "Base revision identifier");
        capabilityIdentifiers = Set.copyOf(capabilityIdentifiers);
        policySelections = Set.copyOf(policySelections);
        Objects.requireNonNull(fulfilmentBindingSetRevisionReference, "fulfilmentBindingSetRevisionReference");
        Objects.requireNonNull(provenance, "provenance");
    }

    /** Produces compiler input; it does not establish validation or activation. */
    public MerchantConfiguration candidateFrom(
            MerchantConfiguration base,
            String candidateIdentifier,
            long version,
            String pinnedSemanticRegistryRelease
    ) {
        requireBase(base);
        return new MerchantConfiguration(merchantScope.merchantIdentifier(), candidateIdentifier,
                version, pinnedSemanticRegistryRelease, capabilityIdentifiers, policySelections,
                Optional.of(baseConfigurationRevisionIdentifier), fulfilmentBindingSetRevisionReference);
    }

    /** A changed policy value appears as a deselection and a selection of the same policy. */
    public Difference differenceFrom(MerchantConfiguration base) {
        requireBase(base);
        return new Difference(
                difference(capabilityIdentifiers, base.capabilityIdentifiers()),
                difference(base.capabilityIdentifiers(), capabilityIdentifiers),
                difference(policySelections, base.policySelections()),
                difference(base.policySelections(), policySelections),
                base.fulfilmentBindingSetRevisionReference(), fulfilmentBindingSetRevisionReference);
    }

    private void requireBase(MerchantConfiguration base) {
        Objects.requireNonNull(base, "base");
        if (!merchantScope.merchantIdentifier().equals(base.merchantIdentifier())
                || !baseConfigurationRevisionIdentifier.equals(base.configurationIdentifier())) {
            throw new IllegalArgumentException("Change set requires its exact merchant and base revision");
        }
    }

    private static <T> Set<T> difference(Set<T> selected, Set<T> previous) {
        var result = new HashSet<>(selected);
        result.removeAll(previous);
        return Set.copyOf(result);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    /**
     * Authority: MS-PROT-040 v1.0,
     * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
     * §14 — Change-set provenance.
     */
    public enum Origin {
        MERCHANT_INITIATED,
        INFERENCE_PROPOSED,
        REGISTERED_DERIVATION,
        PLATFORM_REQUIRED
    }

    /** Source attribution is retained for review; attribution does not confer actor authority. */
    public record Provenance(
            Origin origin,
            String sourceIdentifier,
            String proposingPrincipalIdentifier,
            Instant proposedAt
    ) {
        public Provenance {
            Objects.requireNonNull(origin, "origin");
            requireIdentifier(sourceIdentifier, "Change source identifier");
            requireIdentifier(proposingPrincipalIdentifier, "Proposing principal identifier");
            Objects.requireNonNull(proposedAt, "proposedAt");
        }
    }

    /** Structural diff of represented selections, not a business impact assessment. */
    public record Difference(
            Set<String> enabledCapabilities,
            Set<String> disabledCapabilities,
            Set<PolicySelection> selectedPolicies,
            Set<PolicySelection> deselectedPolicies,
            Optional<FulfilmentBindingSetRevisionReference> previousBinding,
            Optional<FulfilmentBindingSetRevisionReference> proposedBinding
    ) {
        public Difference {
            enabledCapabilities = Set.copyOf(enabledCapabilities);
            disabledCapabilities = Set.copyOf(disabledCapabilities);
            selectedPolicies = Set.copyOf(selectedPolicies);
            deselectedPolicies = Set.copyOf(deselectedPolicies);
            Objects.requireNonNull(previousBinding, "previousBinding");
            Objects.requireNonNull(proposedBinding, "proposedBinding");
        }
    }
}
