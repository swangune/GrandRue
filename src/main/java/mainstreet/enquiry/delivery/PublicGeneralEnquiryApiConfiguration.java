package mainstreet.enquiry.delivery;

import mainstreet.enquiry.EnquirySubmissionApplicationService;
import mainstreet.publication.*;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.springframework.context.annotation.*;
import java.time.Clock;

/** Opt-in delivery wiring; current admission, owner requirements and durable E2 providers are mandatory. */
@Configuration
@Profile("enquiry-public-api")
public class PublicGeneralEnquiryApiConfiguration {
    @Bean
    PublicGeneralEnquirySubmission publicGeneralEnquirySubmission(PublicEnquiryRouteScopeAuthority routes,
            PublicEnquirySubmissionAdmissionAuthority admission, EnquirySubmissionApplicationService application,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot registry,
            OpportunityPublicationStateAuthority publication, OpportunityPublicationSubmissionLock lock,
            PublicGeneralEnquiryRequirements requirements, Clock clock) {
        return new PublicGeneralEnquirySubmission(routes, admission, application, activation, registry,
                publication, lock, requirements, clock);
    }
}
