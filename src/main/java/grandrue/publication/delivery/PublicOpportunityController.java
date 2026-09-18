package grandrue.publication.delivery;

import grandrue.api.ApiProblem;
import grandrue.api.ApiProblemCategory;
import grandrue.api.ApiQueryUnavailableException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Opt-in production PUBLIC delivery. Registry configuration and owner authorities are required. */
@RestController
@Profile("publication-public-api")
public final class PublicOpportunityController {

    private final PublicOpportunityQuery query;

    public PublicOpportunityController(PublicOpportunityQuery query) {
        this.query = Objects.requireNonNull(query);
    }

    @GetMapping("/api/public/storefronts/{locator}/opportunities/{opportunity}")
    public ResponseEntity<?> get(
            @PathVariable String locator,
            @PathVariable String opportunity,
            @RequestParam Map<String, String> parameters
    ) {
        if (!parameters.isEmpty()) {
            return problem(ApiProblemCategory.INVALID_REQUEST);
        }

        try {
            var response = query.get(locator, opportunity);

            return response.<ResponseEntity<?>>map(value ->
                            ResponseEntity.ok()
                                    .cacheControl(CacheControl.noStore())
                                    .body(value))
                    .orElseGet(() ->
                            problem(ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE));
        } catch (ApiQueryUnavailableException failure) {
            return problem(failure.category());
        } catch (RuntimeException failure) {
            return problem(ApiProblemCategory.REPRESENTATION_UNAVAILABLE);
        }
    }

    private static ResponseEntity<ApiProblem> problem(
            ApiProblemCategory category
    ) {
        int status = switch (category) {
            case INVALID_REQUEST -> 400;
            case NOT_FOUND_OR_NOT_ACCESSIBLE -> 404;
            case NOT_AUTHORISED -> 403;
            default -> 503;
        };

        return ResponseEntity.status(status)
                .cacheControl(CacheControl.noStore())
                .body(new ApiProblem(
                        category,
                        Optional.empty()
                ));
    }
}