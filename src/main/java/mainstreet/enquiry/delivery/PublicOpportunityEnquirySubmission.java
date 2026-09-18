package mainstreet.enquiry.delivery;

import grandrue.api.*;
import grandrue.application.ApplicationRequestIdentity;
import grandrue.application.MerchantScope;
import grandrue.enquiry.delivery.PublicEnquirySubmissionAdmissionAuthority;
import grandrue.enquiry.delivery.PublicOpportunityEnquiryRequirements;
import mainstreet.enquiry.*;
import mainstreet.publication.OpportunityPublicationStateAuthority;
import mainstreet.publication.OpportunityPublicationSubmissionLock;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import java.time.Clock;
import java.util.*;

/** Opportunity-bound public submission composition; E2 owns transaction/replay and E3 owns fresh applicability. */
public final class PublicOpportunityEnquirySubmission {
    public record Acknowledgement(ApiCommandOutcomeClass outcome) { }
    /** Only this boundary can create a known pre-application rejection. */
    public static final class Rejection extends RuntimeException {
        private final ApiProblemCategory category;
        private Rejection(ApiProblemCategory category) { super(category.name()); this.category = category; }
        public ApiProblemCategory category() { return category; }
    }
    private final ApiContractRegistrySnapshot contracts;
    private final ApiTransportScopeAuthorityRegistrySnapshot scopes;
    private final PublicEnquirySubmissionAdmissionAuthority admission;
    private final EnquirySubmissionApplicationService application;
    private final ConfigurationReleaseActivation activation;
    private final SemanticRegistrySnapshot registry;
    private final OpportunityPublicationStateAuthority publication;
    private final OpportunityPublicationSubmissionLock publicationLock;
    private final PublicOpportunityEnquiryRequirements requirements;
    private final Clock clock;
    private final OpportunityEnquiryBindingCodec codec;

    public PublicOpportunityEnquirySubmission(PublicEnquiryRouteScopeAuthority routes,
            PublicEnquirySubmissionAdmissionAuthority admission, EnquirySubmissionApplicationService application,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot registry,
            OpportunityPublicationStateAuthority publication, OpportunityPublicationSubmissionLock publicationLock,
            PublicOpportunityEnquiryRequirements requirements, Clock clock, OpportunityEnquiryBindingCodec codec) {
        this.contracts = new ApiContractRegistrySnapshot(Set.of(PublicOpportunityEnquiryContract.command().registration()));
        this.scopes = new ApiTransportScopeAuthorityRegistrySnapshot(List.of(Objects.requireNonNull(routes)));
        this.admission = Objects.requireNonNull(admission);
        this.application = Objects.requireNonNull(application);
        this.activation = Objects.requireNonNull(activation);
        this.registry = Objects.requireNonNull(registry);
        this.publication = Objects.requireNonNull(publication);
        this.publicationLock = Objects.requireNonNull(publicationLock);
        this.requirements = Objects.requireNonNull(requirements);
        this.clock = Objects.requireNonNull(clock);
        this.codec = Objects.requireNonNull(codec);
    }

    public Acknowledgement submit(String locator, String retryKey, PublicOpportunityEnquiryRequest input) {
        Objects.requireNonNull(input, "input");
        if (retryKey == null || retryKey.isBlank() || retryKey.length() > 200) {
            throw new Rejection(ApiProblemCategory.INVALID_REQUEST);
        }
        var registration = contracts.contract(PublicOpportunityEnquiryContract.command().registration().identity()).orElseThrow();
        MerchantScope scope;
        try {
            scope = ((MerchantApiTransportScope) scopes.establish(registration,
                    new PublicEnquiryRouteScopeAuthority.Route(locator))).merchantScope();
        } catch (RuntimeException invalidRoute) {
            throw new Rejection(ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE);
        }
        boolean admitted;
        try { admitted = admission.admits(scope); }
        catch (RuntimeException unavailable) {
            throw new Rejection(ApiProblemCategory.TEMPORARILY_UNAVAILABLE);
        }
        if (!admitted) throw new Rejection(ApiProblemCategory.NOT_AUTHORISED);

        EnquiryRevisionProvenance revision;
        try { revision = codec.decode(scope, input.binding()); }
        catch (IllegalArgumentException invalid) { throw new Rejection(ApiProblemCategory.INVALID_REQUEST); }
        var intent = new EnquirySubmissionIntent(scope, input.question(),
                new EnquirySubmittedContact(Optional.ofNullable(input.name()), Optional.ofNullable(input.email()),
                        Optional.ofNullable(input.telephone())), Optional.of(revision), Optional.empty());
        var preparation = new OpportunityEnquirySubmissionPreparation(scope, activation, registry,
                Optional.of(mainstreet.publication.OpportunityEnquiryParticipationDefinition.forRelease(registry)),
                publication, publicationLock, requirements, clock);
        var result = application.submit(requestIdentity(scope, retryKey), intent, preparation);
        if (!intent.equals(EnquirySubmissionIntent.from(result))) {
            throw new IllegalStateException("Enquiry result intent mismatch");
        }
        // No resource ID, submitted values or provenance crosses this public retry boundary.
        return new Acknowledgement(ApiCommandOutcomeClass.COMPLETED);
    }

    public static ApplicationRequestIdentity requestIdentity(MerchantScope scope, String retryKey) {
        Objects.requireNonNull(scope);
        if (retryKey == null || retryKey.isBlank() || retryKey.length() > 200) {
            throw new IllegalArgumentException("Invalid retry key");
        }
        return new ApplicationRequestIdentity("enquiry-public-opportunity|" + scope.merchantIdentifier().length()
                + ":" + scope.merchantIdentifier() + retryKey);
    }
}
