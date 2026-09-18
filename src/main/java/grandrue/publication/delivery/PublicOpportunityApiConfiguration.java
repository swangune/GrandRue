package grandrue.publication.delivery;

import grandrue.publication.OpportunityPublicationStateAuthority;
import grandrue.semantic.configuration.ConfigurationReleaseActivation;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.AudienceObservationAdmissionEvaluator;
import mainstreet.surface.AudienceObservationContextEstablisher;
import org.springframework.context.annotation.*;
import java.time.Clock;

/**
 * Opt-in production composition. Deployment must supply the registered route map, active release,
 * serving registry, durable owner and current audience authorities; no permissive defaults exist.
 */
@Configuration
@Profile("publication-public-api")
public class PublicOpportunityApiConfiguration {
    @Bean
    PublicOpportunityQuery publicOpportunityQuery(PublicOpportunityRouteScopeAuthority routes,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot semantic,
            OpportunityPublicationStateAuthority owner, AudienceObservationContextEstablisher contexts,
            AudienceObservationAdmissionEvaluator admission, Clock clock) {
        return PublicOpportunityQuery.create(routes, activation, semantic, owner, contexts, admission, clock);
    }
}
