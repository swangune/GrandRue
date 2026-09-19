package grandrue.application;

import grandrue.booking.BookingResidualObligationAuthority;
import grandrue.fulfilment.FulfilmentBindingSetRevisionAuthority;
import grandrue.fulfilment.FulfilmentBindingSetRevisionReference;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.*;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import grandrue.semantic.release.SemanticReleaseAssemblyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConfigurationImpactReviewApplicationServiceTest {

    private static final MerchantScope SCOPE =
            new MerchantScope("merchant-a");

    private static final Instant PACKAGE_AT =
            Instant.parse("2026-09-11T15:00:00Z");

    private static final Instant VALIDATION_AT =
            Instant.parse("2026-09-11T15:01:00Z");

    private static final Instant IMPACT_AT =
            Instant.parse("2026-09-11T15:02:00Z");

    @Test
    void concrete_portfolio_produces_exact_review_for_known_enquiry_change() {
        Fixture f = new Fixture();

        MerchantConfiguration base = configuration(
                "c1",
                1,
                Set.of(),
                Optional.empty()
        );

        MerchantConfiguration candidate = configuration(
                "c2",
                2,
                Set.of("enquiry"),
                Optional.of("c1")
        );

        when(f.revisions.configuration(SCOPE, "c2"))
                .thenReturn(Optional.of(candidate));
        when(f.revisions.configuration(SCOPE, "c1"))
                .thenReturn(Optional.of(base));

        ConfigurationImpactReviewEvidence result =
                f.service().produce(command("c2"));

        assertEquals("impact-1", result.impactReviewEvidenceIdentifier());
        assertEquals("merchant-a", result.merchantIdentifier());
        assertEquals("c2", result.configurationRevisionIdentifier());
        assertEquals("release-1", result.semanticRegistryReleaseIdentifier());
        assertEquals("validation-1", result.validationEvidenceIdentifier());
        assertEquals("package-1", result.resolvedPackageEvidenceIdentifier());

        assertEquals(
                List.of(
                        "New customer enquiries can be accepted through GrandRue "
                                + "where an applicable enquiry interaction is available."
                ),
                result.businessFacingEffects()
        );

        assertFalse(result.findings().isEmpty());

        ArgumentCaptor<RecordConfigurationValidationEvidenceCommand> validation =
                ArgumentCaptor.forClass(
                        RecordConfigurationValidationEvidenceCommand.class
                );

        verify(f.validations)
                .recordSuccessfulValidation(validation.capture());

        assertEquals(
                "c2",
                validation.getValue()
                        .resolvedPackage()
                        .sourceConfigurationRevisionIdentifier()
        );
    }

    @Test
    void reinstatement_review_preserves_exact_basis_across_evidence_chain() {
        Fixture f = new Fixture();

        MerchantConfiguration base = configuration(
                "c1",
                1,
                Set.of(),
                Optional.empty()
        );

        MerchantConfiguration candidate = configuration(
                "c2",
                2,
                Set.of("enquiry"),
                Optional.of("c1")
        );

        when(f.revisions.configuration(SCOPE, "c2"))
                .thenReturn(Optional.of(candidate));
        when(f.revisions.configuration(SCOPE, "c1"))
                .thenReturn(Optional.of(base));

        ConfigurationImpactReviewEvidence result =
                f.service().produce(
                        command(
                                "c2",
                                Optional.of("activate-basis-1")
                        )
                );

        assertEquals(
                Optional.of("activate-basis-1"),
                result.reinstatementBasisActivationRequestIdentifier()
        );

        ArgumentCaptor<RecordConfigurationValidationEvidenceCommand>
                validation = ArgumentCaptor.forClass(
                        RecordConfigurationValidationEvidenceCommand.class
                );

        verify(f.validations)
                .recordSuccessfulValidation(validation.capture());

        assertEquals(
                Optional.of("activate-basis-1"),
                validation.getValue()
                        .reinstatementBasisActivationRequestIdentifier()
        );

        ArgumentCaptor<RecordConfigurationImpactReviewEvidenceCommand> review =
                ArgumentCaptor.forClass(
                        RecordConfigurationImpactReviewEvidenceCommand.class
                );

        verify(f.reviews)
                .recordCompletedReview(review.capture());

        assertEquals(
                Optional.of("activate-basis-1"),
                review.getValue()
                        .reinstatementBasisActivationRequestIdentifier()
        );
    }

    @Test
    void changed_capability_without_concrete_owner_fails_closed() {
        Fixture f = new Fixture();

        MerchantConfiguration base = configuration(
                "c1",
                1,
                Set.of(),
                Optional.empty()
        );

        MerchantConfiguration candidate = configuration(
                "c2",
                2,
                Set.of("future-capability"),
                Optional.of("c1")
        );

        when(f.revisions.configuration(SCOPE, "c2"))
                .thenReturn(Optional.of(candidate));
        when(f.revisions.configuration(SCOPE, "c1"))
                .thenReturn(Optional.of(base));

        IllegalStateException failure = assertThrows(
                IllegalStateException.class,
                () -> f.service().produce(command("c2"))
        );

        assertEquals(
                "Missing owner impact assessment for capability: future-capability",
                failure.getMessage()
        );

        verify(f.validations).recordSuccessfulValidation(any());
        verify(f.reviews, never()).recordCompletedReview(any());
    }

    @Test
    void missing_exact_fulfilment_revision_fails_before_validation() {
        Fixture f = new Fixture();

        FulfilmentBindingSetRevisionReference reference =
                new FulfilmentBindingSetRevisionReference(
                        "binding-set-1",
                        3L
                );

        MerchantConfiguration candidate =
                configuration(
                        "c1",
                        1,
                        Set.of(),
                        Optional.empty()
                ).withFulfilmentBindingSetRevisionReference(reference);

        when(f.revisions.configuration(SCOPE, "c1"))
                .thenReturn(Optional.of(candidate));

        when(f.bindingSets.revision(SCOPE, reference))
                .thenReturn(Optional.empty());

        IllegalArgumentException failure = assertThrows(
                IllegalArgumentException.class,
                () -> f.service().produce(command("c1"))
        );

        assertEquals(
                "Exact Fulfilment Binding Set Revision does not exist: "
                        + reference.provenanceIdentifier(),
                failure.getMessage()
        );

        verifyNoInteractions(f.validations, f.reviews);
    }

    private static ProduceConfigurationImpactReviewCommand command(
            String revision
    ) {
        return command(revision, Optional.empty());
    }

    private static ProduceConfigurationImpactReviewCommand command(
            String revision,
            Optional<String> reinstatementBasisActivationRequestIdentifier
    ) {
        return new ProduceConfigurationImpactReviewCommand(
                SCOPE,
                revision,
                "compiler-1",
                PACKAGE_AT,
                "validation-1",
                "package-1",
                VALIDATION_AT,
                "impact-1",
                IMPACT_AT,
                reinstatementBasisActivationRequestIdentifier
        );
    }

    private static MerchantConfiguration configuration(
            String identifier,
            long version,
            Set<String> capabilities,
            Optional<String> base
    ) {
        return new MerchantConfiguration(
                "merchant-a",
                identifier,
                version,
                "release-1",
                capabilities,
                Set.of(),
                base,
                Optional.empty()
        );
    }

    private static final class Fixture {

        private final ConfigurationRevisionAuthority revisions =
                mock(ConfigurationRevisionAuthority.class);

        private final ConfigurationValidationEvidenceAuthority validations =
                mock(ConfigurationValidationEvidenceAuthority.class);

        private final ConfigurationImpactReviewEvidenceAuthority reviews =
                mock(ConfigurationImpactReviewEvidenceAuthority.class);

        private final FulfilmentBindingSetRevisionAuthority bindingSets =
                mock(FulfilmentBindingSetRevisionAuthority.class);

        private final BookingResidualObligationAuthority bookingObligations =
                mock(BookingResidualObligationAuthority.class);

        private final SemanticReleaseAssemblyRepository releases =
                mock(SemanticReleaseAssemblyRepository.class);

        private final AtomicReference<ConfigurationValidationEvidence>
                storedValidation =
                new AtomicReference<>();

        private final ConfigurationCompiler compiler;

        private Fixture() {
            SemanticRegistrySnapshot snapshot =
                    new SemanticRegistrySnapshot(
                            "release-1",
                            Set.of(
                                    new RegisteredCapability(
                                            "enquiry",
                                            List.of(),
                                            List.of()
                                    ),
                                    new RegisteredCapability(
                                            "future-capability",
                                            List.of(),
                                            List.of()
                                    )
                            )
                    );

            compiler = new ConfigurationCompiler(version ->
                    "release-1".equals(version)
                            ? Optional.of(snapshot)
                            : Optional.empty()
            );

            when(validations.recordSuccessfulValidation(any()))
                    .thenAnswer(invocation -> {
                        RecordConfigurationValidationEvidenceCommand command =
                                invocation.getArgument(0);

                        ResolvedConfigurationPackage resolved =
                                command.resolvedPackage();

                        ConfigurationValidationEvidence evidence =
                                new ConfigurationValidationEvidence(
                                        command.validationEvidenceIdentifier(),
                                        resolved.merchantIdentifier(),
                                        resolved.sourceConfigurationRevisionIdentifier(),
                                        resolved.semanticRegistryReleaseIdentifier(),
                                        command.resolvedPackageEvidenceIdentifier(),
                                        ConfigurationValidationOutcome.SUCCEEDED,
                                        resolved.provenance().compilerIdentifier(),
                                        resolved.provenance().generatedAt(),
                                        command.evidenceProducedAt(),
                                        command
                                                .reinstatementBasisActivationRequestIdentifier()
                                );

                        storedValidation.set(evidence);
                        return evidence;
                    });

            when(validations.evidence(any(), any()))
                    .thenAnswer(invocation -> {
                        MerchantScope scope = invocation.getArgument(0);
                        String identifier = invocation.getArgument(1);

                        ConfigurationValidationEvidence evidence =
                                storedValidation.get();

                        if (evidence == null
                                || !evidence.merchantIdentifier().equals(
                                        scope.merchantIdentifier()
                                )
                                || !evidence.validationEvidenceIdentifier()
                                        .equals(identifier)) {
                            return Optional.empty();
                        }

                        return Optional.of(evidence);
                    });

            when(reviews.recordCompletedReview(any()))
                    .thenAnswer(invocation -> {
                        RecordConfigurationImpactReviewEvidenceCommand command =
                                invocation.getArgument(0);

                        ConfigurationImpactAnalysisResult result =
                                command.impactAnalysisResult();

                        return new ConfigurationImpactReviewEvidence(
                                command.impactReviewEvidenceIdentifier(),
                                result.merchantIdentifier(),
                                result.configurationRevisionIdentifier(),
                                result.semanticRegistryReleaseIdentifier(),
                                result.validationEvidenceIdentifier(),
                                result.resolvedPackageEvidenceIdentifier(),
                                result.businessFacingEffects(),
                                result.findings(),
                                result.completedAt(),
                                command
                                        .reinstatementBasisActivationRequestIdentifier()
                        );
                    });
        }

        private ConfigurationImpactReviewApplicationService service() {
            return new ConfigurationImpactReviewApplicationService(
                    revisions,
                    new ConfigurationPackageResolver(compiler),
                    bindingSets,
                    validations,
                    compiler,
                    bookingObligations,
                    releases,
                    reviews
            );
        }
    }
}
