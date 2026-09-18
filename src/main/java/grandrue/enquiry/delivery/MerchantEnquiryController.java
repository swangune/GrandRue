package grandrue.enquiry.delivery;

import grandrue.api.ApiProblem;
import grandrue.api.ApiProblemCategory;
import grandrue.api.ApiQueryUnavailableException;
import mainstreet.enquiry.delivery.MerchantEnquiryQuery;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@Profile("enquiry-merchant-api")
public final class MerchantEnquiryController {

    private final MerchantEnquiryQuery query;

    public MerchantEnquiryController(MerchantEnquiryQuery query) {
        this.query = Objects.requireNonNull(query);
    }

    @GetMapping("/api/merchant/workspaces/{locator}/enquiries/{identity}")
    public ResponseEntity<?> get(
            @PathVariable String locator,
            @PathVariable String identity,
            @RequestHeader HttpHeaders headers,
            @RequestParam Map<String, String> parameters
    ) {
        if (!parameters.isEmpty()) {
            return problem(400, ApiProblemCategory.INVALID_REQUEST);
        }

        var authorization = headers.get(HttpHeaders.AUTHORIZATION);
        if (authorization == null
                || authorization.size() != 1
                || !authorization.getFirst().regionMatches(
                        true,
                        0,
                        "Bearer ",
                        0,
                        7
                )) {
            return problem(401, ApiProblemCategory.UNAUTHENTICATED);
        }

        try {
            var result = query.get(
                    locator,
                    identity,
                    authorization.getFirst().substring(7)
            );

            if (result.isEmpty()) {
                return problem(
                        404,
                        ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE
                );
            }

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.noStore())
                    .body(result.orElseThrow());
        } catch (ApiQueryUnavailableException known) {
            return problem(
                    switch (known.category()) {
                        case INVALID_REQUEST -> 400;
                        case UNAUTHENTICATED -> 401;
                        case NOT_AUTHORISED -> 403;
                        case NOT_FOUND_OR_NOT_ACCESSIBLE -> 404;
                        default -> 503;
                    },
                    known.category()
            );
        } catch (RuntimeException unavailable) {
            return problem(
                    503,
                    ApiProblemCategory.REPRESENTATION_UNAVAILABLE
            );
        }
    }

    private static ResponseEntity<ApiProblem> problem(
            int status,
            ApiProblemCategory category
    ) {
        var response = ResponseEntity.status(status)
                .cacheControl(CacheControl.noStore());

        if (status == 401) {
            response.header(
                    HttpHeaders.WWW_AUTHENTICATE,
                    "Bearer"
            );
        }

        return response.body(
                new ApiProblem(
                        category,
                        Optional.empty()
                )
        );
    }
}