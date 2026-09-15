package mainstreet.prototype.delivery;

import mainstreet.application.MerchantScope;
import mainstreet.ordering.CommittedQuantity;
import mainstreet.ordering.Order;
import mainstreet.ordering.OrderCommitmentPortion;
import mainstreet.money.CurrencyIdentity;
import mainstreet.money.MonetaryAmount;
import mainstreet.prototype.PrototypeOrderUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PrototypeOrderControllerTest {

    @Test
    void post_maps_idempotency_key_and_path_scope_to_order_use_case() throws Exception {
        RecordingOrderUseCase useCase = new RecordingOrderUseCase();
        MockMvc mvc = MockMvcBuilders.standaloneSetup(
                new PrototypeOrderController(useCase)
        ).build();

        mvc.perform(post("/prototype/merchants/prototype-retailer/orders")
                        .header("Idempotency-Key", "request-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "orderIdentifier":"order-001",
                                  "portions":[{
                                    "portionIdentifier":"portion-001",
                                    "subjectReference":"sku-1",
                                    "quantity":1,
                                    "unitIdentifier":"EACH"
                                  }]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.merchantIdentifier")
                        .value("prototype-retailer"))
                .andExpect(jsonPath("$.orderIdentifier").value("order-001"))
                .andExpect(jsonPath("$.governingReleaseIdentifier")
                        .value("prototype-retailer-release-1"))
                .andExpect(jsonPath("$.portions[0].currencyIdentifier")
                        .value("GBP"))
                .andExpect(jsonPath("$.portions[0].unitMinorAmount")
                        .value(2500));

        org.junit.jupiter.api.Assertions.assertEquals(
                "prototype-retailer",
                useCase.merchantIdentifier
        );
        org.junit.jupiter.api.Assertions.assertEquals(
                "request-001",
                useCase.commandIdentifier
        );
    }

    @Test
    void get_returns_a_read_representation_without_serialising_domain_directly()
            throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(
                new PrototypeOrderController(new RecordingOrderUseCase())
        ).build();

        mvc.perform(get(
                        "/prototype/merchants/prototype-retailer/orders/order-001"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderIdentifier").value("order-001"));
    }

    private static final class RecordingOrderUseCase
            implements PrototypeOrderUseCase {
        private String merchantIdentifier;
        private String commandIdentifier;

        @Override
        public Order commit(
                String merchantIdentifier,
                String commandIdentifier,
                String orderIdentifier,
                List<mainstreet.ordering.RequestedOrderPortion> requestedPortions
        ) {
            this.merchantIdentifier = merchantIdentifier;
            this.commandIdentifier = commandIdentifier;
            return order(merchantIdentifier, orderIdentifier).orElseThrow();
        }

        @Override
        public Optional<Order> order(
                String merchantIdentifier,
                String orderIdentifier
        ) {
            return Optional.of(new Order(
                    new MerchantScope(merchantIdentifier),
                    orderIdentifier,
                    Optional.empty(),
                    List.of(new OrderCommitmentPortion(
                            "portion-001",
                            "sku-1",
                            new CommittedQuantity(BigDecimal.ONE, "EACH"),
                            new MonetaryAmount(
                                    new CurrencyIdentity("GBP"),
                                    BigInteger.valueOf(2500)
                            ),
                            "prototype-offering:sku-1@1"
                    )),
                    "prototype-retailer-release-1",
                    Instant.parse("2026-08-26T03:00:00Z")
            ));
        }
    }
}
