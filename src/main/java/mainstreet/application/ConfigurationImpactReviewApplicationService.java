package mainstreet.application;

import grandrue.booking.BookingAvailabilityImpactAssessment;
import grandrue.booking.BookingResidualObligationAuthority;
import mainstreet.enquiry.EnquiryAvailabilityImpactAssessment;
import grandrue.fulfilment.FulfilmentBindingSetRevision;
import grandrue.fulfilment.FulfilmentBindingSetRevisionAuthority;
import grandrue.fulfilment.FulfilmentRoutingImpactAssessment;
import grandrue.inventory.InventoryAvailabilityImpactAssessment;
import grandrue.money.PaymentAvailabilityImpactAssessment;
import grandrue.ordering.OrderingAvailabilityImpactAssessment;
import mainstreet.publication.PublicationAvailabilityImpactAssessment;
import grandrue.scheduling.SchedulingAvailabilityImpactAssessment;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.CapabilityMembershipImpactAssessment;
import mainstreet.semantic.configuration.ConfigurationImpactAnalysisResult;
import mainstreet.semantic.configuration.ConfigurationImpactAnalyzer;
import mainstreet.semantic.configuration.ConfigurationImpactReviewEvidence;
import mainstreet.semantic.configuration.ConfigurationImpactReviewEvidenceAuthority;
import mainstreet.semantic.configuration.ConfigurationPackageResolver;
import mainstreet.semantic.configuration.ConfigurationRevisionAuthority;
import mainstreet.semantic.configuration.ConfigurationValidationEvidence;
import mainstreet.semantic.configuration.ConfigurationValidationEvidenceAuthority;
import mainstreet.semantic.configuration.ConfigurationValidationOutcome;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.configuration.ProduceConfigurationImpactReviewCommand;
import mainstreet.semantic.configuration.RecordConfigurationImpactReviewEvidenceCommand;
import mainstreet.semantic.configuration.RecordConfigurationValidationEvidenceCommand;
import mainstreet.semantic.configuration.ResolvedConfigurationPackage;
import mainstreet.semantic.configuration.ResolvedPolicyImpactAssessment;
import mainstreet.semantic.release.SemanticReleaseAssemblyRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Concrete cross-capability Configuration impact-review composition.
 *
 * <p>This class coordinates existing semantic owners. It does not redefine
 * their business truth. Every presently implemented capability-impact owner is
 * explicitly registered. Unknown changed capabilities and changed policies
 * without an accepted owner interpretation fail closed.</p>
 *
 * Authority:
 * MS-PROT-040 v1.0 §§20-26;
 * MS-PROT-040 v1.3 §§9-11;
 * MS-PROT-040 v1.8 §§5-6.
 */
public final class ConfigurationImpactReviewApplicationService {

    private final ConfigurationRevisionAuthority revisions;
    private final ConfigurationPackageResolver packages;
    private final FulfilmentBindingSetRevisionAuthority bindingSets;
    private final ConfigurationValidationEvidenceAuthority validations;
    private final ConfigurationImpactAnalyzer analyzer;
    private final ConfigurationImpactReviewEvidenceAuthority reviews;

    public ConfigurationImpactReviewApplicationService(
            ConfigurationRevisionAuthority revisions,
            ConfigurationPackageResolver packages,
            FulfilmentBindingSetRevisionAuthority bindingSets,
            ConfigurationValidationEvidenceAuthority validations,
            ConfigurationCompiler compiler,
            BookingResidualObligationAuthority bookingObligations,
            SemanticReleaseAssemblyRepository releases,
            ConfigurationImpactReviewEvidenceAuthority reviews
    ) {
        this.revisions = Objects.requireNonNull(revisions, "revisions");
        this.packages = Objects.requireNonNull(packages, "packages");
        this.bindingSets = Objects.requireNonNull(bindingSets, "bindingSets");
        this.validations = Objects.requireNonNull(validations, "validations");
        this.reviews = Objects.requireNonNull(reviews, "reviews");

        Objects.requireNonNull(compiler, "compiler");
        Objects.requireNonNull(bookingObligations, "bookingObligations");
        Objects.requireNonNull(releases, "releases");

        FulfilmentRoutingImpactAssessment fulfilment =
                new FulfilmentRoutingImpactAssessment(
                        compiler,
                        releases,
                        bindingSets
                );

        CapabilityMembershipImpactAssessment membership =
                new CapabilityMembershipImpactAssessment(
                        compiler,
                        Map.of(
                                "booking",
                                new BookingAvailabilityImpactAssessment(
                                        compiler,
                                        bookingObligations
                                ),
                                "enquiry",
                                new EnquiryAvailabilityImpactAssessment(
                                        compiler
                                ),
                                "fulfilment",
                                fulfilment,
                                "inventory",
                                new InventoryAvailabilityImpactAssessment(
                                        compiler
                                ),
                                "ordering",
                                new OrderingAvailabilityImpactAssessment(
                                        compiler
                                ),
                                "payment",
                                new PaymentAvailabilityImpactAssessment(
                                        compiler
                                ),
                                "publication",
                                new PublicationAvailabilityImpactAssessment(
                                        compiler
                                ),
                                "scheduling",
                                new SchedulingAvailabilityImpactAssessment(
                                        compiler
                                )
                        )
                );

        /*
         * No concrete production policy-owner interpretations are currently
         * registered. Empty registration is deliberate fail-closed behaviour:
         * a changed effective policy cannot silently pass impact review.
         */
        ResolvedPolicyImpactAssessment policies =
                new ResolvedPolicyImpactAssessment(
                        compiler,
                        Map.of()
                );

        this.analyzer = new ConfigurationImpactAnalyzer(
                revisions,
                validations,
                List.of(
                        membership,
                        policies
                )
        );
    }

    public ConfigurationImpactReviewEvidence produce(
            ProduceConfigurationImpactReviewCommand command
    ) {
        Objects.requireNonNull(command, "command");

        MerchantConfiguration configuration =
                revisions.configuration(
                                command.merchantScope(),
                                command.configurationRevisionIdentifier()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Exact Configuration Revision does not exist: "
                                                + command
                                                .configurationRevisionIdentifier()
                                )
                        );

        requireExactRevision(command, configuration);

        ResolvedConfigurationPackage resolvedPackage =
                resolvePackage(command, configuration);

        ConfigurationValidationEvidence validation =
                validations.recordSuccessfulValidation(
                        new RecordConfigurationValidationEvidenceCommand(
                                command.validationEvidenceIdentifier(),
                                command.resolvedPackageEvidenceIdentifier(),
                                resolvedPackage,
                                command.validationEvidenceProducedAt(),
                                command
                                        .reinstatementBasisActivationRequestIdentifier()
                        )
                );

        requireExactValidation(
                command,
                resolvedPackage,
                validation
        );

        ConfigurationImpactAnalysisResult analysis =
                analyzer.analyze(
                        command.merchantScope(),
                        command.validationEvidenceIdentifier(),
                        command.resolvedPackageEvidenceIdentifier(),
                        resolvedPackage,
                        command.impactAnalysisCompletedAt()
                );

        ConfigurationImpactReviewEvidence recorded =
                reviews.recordCompletedReview(
                        new RecordConfigurationImpactReviewEvidenceCommand(
                                command.impactReviewEvidenceIdentifier(),
                                analysis,
                                command
                                        .reinstatementBasisActivationRequestIdentifier()
                        )
                );

        ConfigurationImpactReviewEvidence expected =
                expectedReview(command, analysis);

        if (!expected.equals(recorded)) {
            throw new IllegalStateException(
                    "Impact-review authority returned evidence "
                            + "outside the exact completed analysis"
            );
        }

        return recorded;
    }

    private ResolvedConfigurationPackage resolvePackage(
            ProduceConfigurationImpactReviewCommand command,
            MerchantConfiguration configuration
    ) {
        if (configuration
                .fulfilmentBindingSetRevisionReference()
                .isEmpty()) {
            return packages.resolve(
                    configuration,
                    command.compilerIdentifier(),
                    command.packageGeneratedAt()
            );
        }

        var reference = configuration
                .fulfilmentBindingSetRevisionReference()
                .orElseThrow();

        FulfilmentBindingSetRevision bindingSet =
                bindingSets
                        .revision(command.merchantScope(), reference)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Exact Fulfilment Binding Set Revision "
                                                + "does not exist: "
                                                + reference
                                                .provenanceIdentifier()
                                )
                        );

        return packages.resolve(
                configuration,
                bindingSet,
                command.compilerIdentifier(),
                command.packageGeneratedAt()
        );
    }

    private static void requireExactRevision(
            ProduceConfigurationImpactReviewCommand command,
            MerchantConfiguration configuration
    ) {
        boolean exactMerchant =
                command.merchantScope()
                        .merchantIdentifier()
                        .equals(configuration.merchantIdentifier());

        boolean exactRevision =
                command.configurationRevisionIdentifier()
                        .equals(configuration.configurationIdentifier());

        if (!exactMerchant || !exactRevision) {
            throw new IllegalStateException(
                    "Configuration Revision authority returned "
                            + "non-affined configuration contents"
            );
        }
    }

    private static void requireExactValidation(
            ProduceConfigurationImpactReviewCommand command,
            ResolvedConfigurationPackage resolvedPackage,
            ConfigurationValidationEvidence actual
    ) {
        ConfigurationValidationEvidence expected =
                new ConfigurationValidationEvidence(
                        command.validationEvidenceIdentifier(),
                        resolvedPackage.merchantIdentifier(),
                        resolvedPackage
                                .sourceConfigurationRevisionIdentifier(),
                        resolvedPackage
                                .semanticRegistryReleaseIdentifier(),
                        command.resolvedPackageEvidenceIdentifier(),
                        ConfigurationValidationOutcome.SUCCEEDED,
                        resolvedPackage.provenance().compilerIdentifier(),
                        resolvedPackage.provenance().generatedAt(),
                        command.validationEvidenceProducedAt(),
                        command
                                .reinstatementBasisActivationRequestIdentifier()
                );

        if (!expected.equals(actual)) {
            throw new IllegalStateException(
                    "Validation authority returned evidence "
                            + "outside the exact resolved package"
            );
        }
    }

    private static ConfigurationImpactReviewEvidence expectedReview(
            ProduceConfigurationImpactReviewCommand command,
            ConfigurationImpactAnalysisResult analysis
    ) {
        return new ConfigurationImpactReviewEvidence(
                command.impactReviewEvidenceIdentifier(),
                analysis.merchantIdentifier(),
                analysis.configurationRevisionIdentifier(),
                analysis.semanticRegistryReleaseIdentifier(),
                analysis.validationEvidenceIdentifier(),
                analysis.resolvedPackageEvidenceIdentifier(),
                analysis.businessFacingEffects(),
                analysis.findings(),
                analysis.completedAt(),
                command
                        .reinstatementBasisActivationRequestIdentifier()
        );
    }
}
