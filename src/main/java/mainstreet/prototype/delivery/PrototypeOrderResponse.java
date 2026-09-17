package mainstreet.prototype.delivery;

import grandrue.ordering.Order;
import grandrue.ordering.OrderCommitmentPortion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Read/command response DTO; domain Order is never serialized directly. */
public record PrototypeOrderResponse(
        String merchantIdentifier,
        String orderIdentifier,
        Optional<String> customerContextIdentifier,
        String governingReleaseIdentifier,
        Instant committedAt,
        List<Portion> portions
) {

    public static PrototypeOrderResponse from(Order order) {
        return new PrototypeOrderResponse(
                order.merchantScope().merchantIdentifier(),
                order.identifier(),
                order.customerContextIdentifier(),
                order.governingReleaseIdentifier(),
                order.committedAt(),
                order.commitmentPortions().stream()
                        .map(Portion::from)
                        .toList()
        );
    }

    public record Portion(
            String portionIdentifier,
            String subjectReference,
            BigDecimal quantity,
            String unitIdentifier,
            String currencyIdentifier,
            BigInteger unitMinorAmount,
            String commercialTermsProvenanceReference
    ) {
        private static Portion from(OrderCommitmentPortion portion) {
            return new Portion(
                    portion.identifier(),
                    portion.committedSubjectReference(),
                    portion.quantity().magnitude(),
                    portion.quantity().unitIdentifier(),
                    portion.committedUnitAmount().currencyIdentity().identifier(),
                    portion.committedUnitAmount().minorUnitAmount(),
                    portion.commercialTermsProvenanceReference()
            );
        }
    }
}
