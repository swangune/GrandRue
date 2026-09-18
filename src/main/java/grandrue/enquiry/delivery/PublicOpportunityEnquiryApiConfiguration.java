package grandrue.enquiry.delivery;

import mainstreet.enquiry.EnquirySubmissionApplicationService;
import mainstreet.publication.*;
import mainstreet.publication.delivery.PublicOpportunityRouteScopeAuthority;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.*;
import org.springframework.context.annotation.*;
import java.time.Clock;

/** Explicit opt-in; no default binding keys, admission or remaining-requirement provider. */
@Configuration
@Profile("enquiry-opportunity-public-api")
public class PublicOpportunityEnquiryApiConfiguration {
    @Bean
    PublicOpportunityEnquiryBindingQuery publicOpportunityEnquiryBindingQuery(PublicOpportunityRouteScopeAuthority routes,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot registry,
            OpportunityPublicationStateAuthority owner, AudienceObservationContextEstablisher contexts,
            AudienceObservationAdmissionEvaluator admission, Clock clock, OpportunityEnquiryBindingCodec codec) {
        return PublicOpportunityEnquiryBindingQuery.create(routes, activation, registry, owner, contexts, admission, clock, codec);
    }

    @Bean
    PublicOpportunityEnquirySubmission publicOpportunityEnquirySubmission(PublicEnquiryRouteScopeAuthority routes,
            PublicEnquirySubmissionAdmissionAuthority admission, EnquirySubmissionApplicationService application,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot registry,
            OpportunityPublicationStateAuthority publication, OpportunityPublicationSubmissionLock lock,
            PublicOpportunityEnquiryRequirements requirements, Clock clock, OpportunityEnquiryBindingCodec codec) {
        return new PublicOpportunityEnquirySubmission(routes, admission, application, activation, registry,
                publication, lock, requirements, clock, codec);
    }
}
