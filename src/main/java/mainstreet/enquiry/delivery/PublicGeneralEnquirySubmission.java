package mainstreet.enquiry.delivery;

import grandrue.api.*;
import grandrue.application.ApplicationRequestIdentity;
import grandrue.application.MerchantScope;
import grandrue.enquiry.delivery.PublicEnquirySubmissionAdmissionAuthority;
import grandrue.enquiry.delivery.PublicGeneralEnquiryRequirements;
import mainstreet.enquiry.*;
import mainstreet.publication.OpportunityPublicationStateAuthority;
import mainstreet.publication.OpportunityPublicationSubmissionLock;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import java.time.Clock;
import java.util.*;

/** General public submission composition; E2 owns transaction/replay and E3 owns fresh applicability. */
public final class PublicGeneralEnquirySubmission {
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
    private final PublicGeneralEnquiryRequirements requirements;
    private final Clock clock;

    public PublicGeneralEnquirySubmission(PublicEnquiryRouteScopeAuthority routes,
            PublicEnquirySubmissionAdmissionAuthority admission, EnquirySubmissionApplicationService application,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot registry,
            OpportunityPublicationStateAuthority publication, OpportunityPublicationSubmissionLock publicationLock,
            PublicGeneralEnquiryRequirements requirements, Clock clock) {
        this.contracts = new ApiContractRegistrySnapshot(Set.of(PublicGeneralEnquiryContract.definition().registration()));
        this.scopes = new ApiTransportScopeAuthorityRegistrySnapshot(List.of(Objects.requireNonNull(routes)));
        this.admission = Objects.requireNonNull(admission);
        this.application = Objects.requireNonNull(application);
        this.activation = Objects.requireNonNull(activation);
        this.registry = Objects.requireNonNull(registry);
        this.publication = Objects.requireNonNull(publication);
        this.publicationLock = Objects.requireNonNull(publicationLock);
        this.requirements = Objects.requireNonNull(requirements);
        this.clock = Objects.requireNonNull(clock);
    }

    public Acknowledgement submit(String locator, String retryKey, PublicGeneralEnquiryRequest input) {
        Objects.requireNonNull(input, "input");
        if (retryKey == null || retryKey.isBlank() || retryKey.length() > 200) {
            throw new Rejection(ApiProblemCategory.INVALID_REQUEST);
        }
        var registration = contracts.contract(PublicGeneralEnquiryContract.definition().registration().identity()).orElseThrow();
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

        var intent = new EnquirySubmissionIntent(scope, input.question(),
                new EnquirySubmittedContact(Optional.ofNullable(input.name()), Optional.ofNullable(input.email()),
                        Optional.ofNullable(input.telephone())), Optional.empty(), Optional.empty());
        var preparation = new OpportunityEnquirySubmissionPreparation(scope, activation, registry,
                Optional.empty(), publication, publicationLock, requirements, clock);
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
        return new ApplicationRequestIdentity("enquiry-public-general|" + scope.merchantIdentifier().length()
                + ":" + scope.merchantIdentifier() + retryKey);
    }
}
