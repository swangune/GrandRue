package grandrue.enquiry.delivery;

import grandrue.enquiry.EnquiryIdentityConflictException;

import grandrue.enquiry.EnquiryApplicationRequestConflictException;
import grandrue.enquiry.EnquirySubmissionRevalidationException;

import grandrue.api.*;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@Profile("enquiry-opportunity-public-api")
public final class PublicOpportunityEnquiryController {
    public record ProblemResponse(ApiCommandOutcomeClass outcome, ApiProblem problem) { }
    private final PublicOpportunityEnquirySubmission submission;
    private final PublicOpportunityEnquiryBindingQuery query;
    public PublicOpportunityEnquiryController(PublicOpportunityEnquiryBindingQuery query, PublicOpportunityEnquirySubmission submission) {
        this.submission = Objects.requireNonNull(submission);
        this.query = Objects.requireNonNull(query);
    }

    @GetMapping("/api/public/storefronts/{locator}/opportunities/{opportunity}/enquiry-binding")
    public ResponseEntity<?> binding(@PathVariable String locator, @PathVariable String opportunity,
            @RequestParam Map<String, String> parameters) {
        if (!parameters.isEmpty()) return queryProblem(400, ApiProblemCategory.INVALID_REQUEST);
        try {
            var response = query.get(locator, opportunity);
            if (response.isEmpty()) return queryProblem(404, ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE);
            return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(response.orElseThrow());
        } catch (ApiQueryUnavailableException known) {
            var category = known.category();
            return queryProblem(switch (category) {
                case INVALID_REQUEST -> 400;
                case NOT_FOUND_OR_NOT_ACCESSIBLE -> 404;
                case NOT_AUTHORISED -> 403;
                default -> 503;
            }, category);
        } catch (RuntimeException unavailable) {
            return queryProblem(503, ApiProblemCategory.REPRESENTATION_UNAVAILABLE);
        }
    }

    private static ResponseEntity<ApiProblem> queryProblem(int status, ApiProblemCategory category) {
        return ResponseEntity.status(status).cacheControl(CacheControl.noStore())
                .body(new ApiProblem(category, Optional.empty()));
    }

    @PostMapping("/api/public/storefronts/{locator}/enquiries/opportunity")
    public ResponseEntity<?> submit(@PathVariable String locator,
            @RequestHeader("Idempotency-Key") String retryKey,
            @RequestBody PublicOpportunityEnquiryRequest input, @RequestParam Map<String, String> parameters) {
        if (!parameters.isEmpty()) return rejected(400, ApiProblemCategory.INVALID_REQUEST);
        try {
            return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(submission.submit(locator, retryKey, input));
        } catch (PublicOpportunityEnquirySubmission.Rejection knownPreExecutionFailure) {
            var category = knownPreExecutionFailure.category();
            int status = switch (category) {
                case INVALID_REQUEST -> 400;
                case NOT_FOUND_OR_NOT_ACCESSIBLE -> 404;
                case NOT_AUTHORISED -> 403;
                default -> 503;
            };
            return rejected(status, category);
        } catch (EnquiryApplicationRequestConflictException conflict) {
            return rejected(409, ApiProblemCategory.IDEMPOTENCY_CONFLICT);
        } catch (EnquirySubmissionRevalidationException | PublicEnquiryRequirementsUnsatisfiedException invalid) {
            return rejected(422, ApiProblemCategory.NOT_APPLICABLE);
        } catch (EnquiryIdentityConflictException conflict) {
            return rejected(503, ApiProblemCategory.TEMPORARILY_UNAVAILABLE);
        } catch (RuntimeException uncertain) {
            return ResponseEntity.status(503).cacheControl(CacheControl.noStore()).body(new ProblemResponse(
                    ApiCommandOutcomeClass.OUTCOME_UNCERTAIN, new ApiProblem(ApiProblemCategory.OUTCOME_UNCERTAIN,
                    Optional.empty())));
        }
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingRequestHeaderException.class})
    public ResponseEntity<ProblemResponse> invalidTransport(Exception ignored) {
        return rejected(400, ApiProblemCategory.INVALID_REQUEST);
    }

    private static ResponseEntity<ProblemResponse> rejected(int status, ApiProblemCategory category) {
        return ResponseEntity.status(status).cacheControl(CacheControl.noStore()).body(new ProblemResponse(
                ApiCommandOutcomeClass.REJECTED, new ApiProblem(category, Optional.empty())));
    }
}
