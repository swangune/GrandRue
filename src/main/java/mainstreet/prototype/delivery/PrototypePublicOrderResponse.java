package mainstreet.prototype.delivery;

import mainstreet.ordering.Order;
import mainstreet.ordering.OrderCommitmentPortion;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Public Order receipt containing only public proposition references and committed quantity. */
public record PrototypePublicOrderResponse(
        String merchantIdentifier,
        String orderIdentifier,
        Instant committedAt,
        List<Portion> portions
) {
    public static PrototypePublicOrderResponse from(
            Order order,
            PrototypePublicOrderRequest request
    ) {
        Map<String, PrototypePublicOrderRequest.Portion> publicPortions =
                request.portions().stream().collect(Collectors.toUnmodifiableMap(
                        PrototypePublicOrderRequest.Portion::portionIdentifier,
                        Function.identity()
                ));

        return new PrototypePublicOrderResponse(
                order.merchantScope().merchantIdentifier(),
                order.identifier(),
                order.committedAt(),
                order.commitmentPortions().stream()
                        .map(portion -> publicPortion(portion, publicPortions))
                        .toList()
        );
    }

    private static Portion publicPortion(
            OrderCommitmentPortion committed,
            Map<String, PrototypePublicOrderRequest.Portion> publicPortions
    ) {
        PrototypePublicOrderRequest.Portion publicPortion = publicPortions.get(
                committed.identifier()
        );
        if (publicPortion == null) {
            throw new IllegalStateException(
                    "Committed Order portion has no originating public intent portion"
            );
        }
        return new Portion(
                committed.identifier(),
                publicPortion.subjectReference(),
                committed.quantity().magnitude()
        );
    }

    public record Portion(
            String portionIdentifier,
            String subjectReference,
            BigDecimal quantity
    ) {
    }
}
