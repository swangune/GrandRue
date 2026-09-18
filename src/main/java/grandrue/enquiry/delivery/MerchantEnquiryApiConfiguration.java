package grandrue.enquiry.delivery;

import grandrue.enquiry.delivery.MerchantEnquiryObservationPrivileges;
import grandrue.enquiry.delivery.MerchantEnquiryRouteScopeAuthority;
import grandrue.enquiry.EnquirySubmissionStore;
import mainstreet.runtime.*;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.*;
import org.springframework.context.annotation.*;
import java.time.Clock;

/** Opt-in composition; session, current authority and all family privileges are mandatory providers. */
@Configuration
@Profile("enquiry-merchant-api")
public class MerchantEnquiryApiConfiguration {
    @Bean
    MerchantEnquiryQuery merchantEnquiryQuery(MerchantEnquiryRouteScopeAuthority routes,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot semantic, EnquirySubmissionStore submissions,
            SessionTrustedExecutionContextEstablisher authentication, AudienceObservationContextEstablisher contexts,
            AudienceObservationAdmissionEvaluator admission, ActorAuthorisationAuthority actors,
            MerchantEnquiryObservationPrivileges privileges, Clock clock) {
        return MerchantEnquiryQuery.create(routes, activation, semantic, submissions, authentication, contexts,
                admission, actors, privileges, clock);
    }
}

