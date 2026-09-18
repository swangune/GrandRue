package mainstreet.enquiry.delivery;

import grandrue.enquiry.EnquiryApplicationRequestConflictException;

import grandrue.api.*;
import mainstreet.enquiry.*;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@Profile("enquiry-public-api")
public final class PublicGeneralEnquiryController {
    public record ProblemResponse(ApiCommandOutcomeClass outcome, ApiProblem problem) { }
    private final PublicGeneralEnquirySubmission submission;
    public PublicGeneralEnquiryController(PublicGeneralEnquirySubmission submission) {
        this.submission = Objects.requireNonNull(submission);
    }

    @PostMapping("/api/public/storefronts/{locator}/enquiries/general")
    public ResponseEntity<?> submit(@PathVariable String locator,
            @RequestHeader("Idempotency-Key") String retryKey,
            @RequestBody PublicGeneralEnquiryRequest input, @RequestParam Map<String, String> parameters) {
        if (!parameters.isEmpty()) return rejected(400, ApiProblemCategory.INVALID_REQUEST);
        try {
            return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(submission.submit(locator, retryKey, input));
        } catch (PublicGeneralEnquirySubmission.Rejection knownPreExecutionFailure) {
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
