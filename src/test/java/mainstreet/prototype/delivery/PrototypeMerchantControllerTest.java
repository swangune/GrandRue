package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypeMerchantRuntime;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PrototypeMerchantControllerTest {

    @Test
    void active_merchant_projection_is_available_over_http() throws Exception {
        MockMvc mvc = mvc();

        mvc.perform(get("/prototype/merchants/prototype-retailer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.merchantIdentifier")
                        .value("prototype-retailer"))
                .andExpect(jsonPath("$.configurationIdentifier")
                        .value("prototype-retailer-config-1"))
                .andExpect(jsonPath("$.capabilityIdentifiers", containsInAnyOrder(
                        "ordering",
                        "inventory",
                        "payment",
                        "order-fulfilment"
                )))
                .andExpect(jsonPath("$.operationIdentifiers", containsInAnyOrder(
                        "ordering.commit"
                )));
    }

    @Test
    void requested_reference_businesses_are_projected_from_capabilities()
            throws Exception {
        MockMvc mvc = mvc();

        mvc.perform(get("/prototype/merchants/prototype-motel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capabilityIdentifiers", containsInAnyOrder(
                        "booking",
                        "customer",
                        "payment"
                )))
                .andExpect(jsonPath("$.operationIdentifiers", containsInAnyOrder(
                        "booking.confirm"
                )));

        mvc.perform(get("/prototype/merchants/prototype-daycare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capabilityIdentifiers", containsInAnyOrder(
                        "booking",
                        "customer",
                        "payment"
                )))
                .andExpect(jsonPath("$.operationIdentifiers", containsInAnyOrder(
                        "booking.confirm"
                )));

        mvc.perform(get("/prototype/merchants/prototype-gardener"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capabilityIdentifiers", containsInAnyOrder(
                        "appointment",
                        "scheduling",
                        "customer",
                        "payment"
                )))
                .andExpect(jsonPath("$.operationIdentifiers", containsInAnyOrder(
                        "appointment.confirm"
                )));
    }

    @Test
    void same_trade_merchants_project_different_active_capability_graphs()
            throws Exception {
        MockMvc mvc = mvc();

        mvc.perform(get("/prototype/merchants/prototype-gardener-showcase"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capabilityIdentifiers", containsInAnyOrder(
                        "publication",
                        "enquiry"
                )))
                .andExpect(jsonPath("$.operationIdentifiers").isEmpty());

        mvc.perform(get("/prototype/merchants/prototype-gardener-bookable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capabilityIdentifiers", containsInAnyOrder(
                        "publication",
                        "enquiry",
                        "appointment",
                        "scheduling",
                        "customer"
                )))
                .andExpect(jsonPath("$.operationIdentifiers", containsInAnyOrder(
                        "appointment.confirm"
                )));
    }

    @Test
    void publisher_projection_contains_no_synthetic_commerce_capabilities()
            throws Exception {
        MockMvc mvc = mvc();

        mvc.perform(get("/prototype/merchants/prototype-publisher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capabilityIdentifiers", containsInAnyOrder(
                        "publication",
                        "enquiry"
                )))
                .andExpect(jsonPath("$.operationIdentifiers").isEmpty());
    }

    private static MockMvc mvc() {
        return MockMvcBuilders.standaloneSetup(
                new PrototypeMerchantController(
                        PrototypeMerchantRuntime.standard()
                )
        ).build();
    }
}
