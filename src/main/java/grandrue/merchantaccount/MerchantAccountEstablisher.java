package grandrue.merchantaccount;

import mainstreet.application.TrustedPlatformHumanPrincipal;

import java.util.Objects;

/**
 * Application service for ordinary PLATFORM-scoped Merchant Account
 * self-establishment.
 *
 * <p>Governed by the composite MS-PROT-071 v1.0 + v1.1 authority.</p>
 */
public final class MerchantAccountEstablisher {

    private final MerchantAccountEstablishmentAuthorizer authorizer;
    private final MerchantAccountBootstrapStore bootstrapStore;

    public MerchantAccountEstablisher(
            MerchantAccountEstablishmentAuthorizer authorizer,
            MerchantAccountBootstrapStore bootstrapStore) {
        this.authorizer = Objects.requireNonNull(authorizer, "authorizer");
        this.bootstrapStore = Objects.requireNonNull(bootstrapStore, "bootstrapStore");
    }

    public Result establish(
            String logicalEstablishmentRequestIdentity,
            TrustedPlatformHumanPrincipal principal) {
        Objects.requireNonNull(principal, "principal");
        if (logicalEstablishmentRequestIdentity == null
                || logicalEstablishmentRequestIdentity.isBlank()) {
            return Result.validationRejection();
        }

        if (!authorizer.mayEstablishOwnMerchantAccount(principal)) {
            return Result.authorisationRejection();
        }

        MerchantAccountBootstrapStore.BootstrapOutcome outcome =
                bootstrapStore.establishIfAbsent(logicalEstablishmentRequestIdentity, principal);

        return outcome.alreadyEstablished()
                ? Result.alreadyEstablished(outcome.merchantAccount())
                : Result.success(outcome.merchantAccount());
    }

    public enum Status {
        VALIDATION_REJECTION,
        AUTHORISATION_REJECTION,
        SUCCESS,
        ALREADY_ESTABLISHED
    }

    public record Result(Status status, MerchantAccount merchantAccount) {
        public Result {
            Objects.requireNonNull(status, "status");
            if ((status == Status.SUCCESS || status == Status.ALREADY_ESTABLISHED)
                    && merchantAccount == null) {
                throw new IllegalArgumentException("successful result requires merchantAccount");
            }
            if ((status == Status.VALIDATION_REJECTION
                    || status == Status.AUTHORISATION_REJECTION)
                    && merchantAccount != null) {
                throw new IllegalArgumentException("rejection must not expose merchantAccount");
            }
        }

        public static Result validationRejection() {
            return new Result(Status.VALIDATION_REJECTION, null);
        }

        public static Result authorisationRejection() {
            return new Result(Status.AUTHORISATION_REJECTION, null);
        }

        public static Result success(MerchantAccount merchantAccount) {
            return new Result(Status.SUCCESS, merchantAccount);
        }

        public static Result alreadyEstablished(MerchantAccount merchantAccount) {
            return new Result(Status.ALREADY_ESTABLISHED, merchantAccount);
        }
    }
}
