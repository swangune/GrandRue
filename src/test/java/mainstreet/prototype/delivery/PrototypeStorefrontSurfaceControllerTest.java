package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypeMerchantRuntime;
import mainstreet.prototype.PrototypeStorefrontSurfaceProjection;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PrototypeStorefrontSurfaceControllerTest {

    @Test
    void showcase_and_bookable_gardeners_expose_different_composed_public_surfaces()
            throws Exception {
        MockMvc mvc = mvc();

        mvc.perform(get(
                        "/prototype/merchants/prototype-gardener-showcase/storefront-surface"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.merchantIdentifier")
                        .value("prototype-gardener-showcase"))
                .andExpect(jsonPath("$.groups.length()").value(1))
                .andExpect(jsonPath("$.groups[0].compositionTargetReference")
                        .value("public/content"))
                .andExpect(jsonPath(
                        "$.groups[0].contributions[0].contributionIdentifier"
                ).value("browse-published-content"))
                .andExpect(jsonPath(
                        "$.groups[0].contributions[0].bindings.length()"
                ).value(0));

        mvc.perform(get(
                        "/prototype/merchants/prototype-gardener-bookable/storefront-surface"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groups.length()").value(2))
                .andExpect(jsonPath(
                        "$.groups[*].compositionTargetReference",
                        containsInAnyOrder(
                                "public/content",
                                "public/primary-actions"
                        )
                ))
                .andExpect(jsonPath(
                        "$.groups[?(@.compositionTargetReference == 'public/primary-actions')].contributions[0].bindings[0].subjectReference"
                ).value("garden-maintenance"))
                .andExpect(jsonPath(
                        "$.groups[?(@.compositionTargetReference == 'public/primary-actions')].contributions[0].bindings[0].label"
                ).value("Garden maintenance visit"))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("gardening.perform")
                )));
    }

    @Test
    void motel_binding_exposes_booked_subject_not_internal_allocation_subject()
            throws Exception {
        MockMvc mvc = mvc();

        mvc.perform(get(
                        "/prototype/merchants/prototype-motel/storefront-surface"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.groups[0].contributions[0].supportedOperationReferences[0]"
                ).value("booking.confirm"))
                .andExpect(jsonPath(
                        "$.groups[0].contributions[0].bindings[0].subjectReference"
                ).value("standard-room"))
                .andExpect(jsonPath(
                        "$.groups[0].contributions[0].bindings[0].label"
                ).value("Standard room"))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString(
                                "prototype-standard-room-capacity-1"
                        )
                )));
    }

    @Test
    void retailer_binding_exposes_public_proposition_reference_not_internal_sku()
            throws Exception {
        MockMvc mvc = mvc();

        mvc.perform(get(
                        "/prototype/merchants/prototype-retailer/storefront-surface"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.groups[0].contributions[0].supportedOperationReferences[0]"
                ).value("ordering.commit"))
                .andExpect(jsonPath(
                        "$.groups[0].contributions[0].bindings[0].subjectReference"
                ).value("milk-2l"))
                .andExpect(jsonPath(
                        "$.groups[0].contributions[0].bindings[0].label"
                ).value("Milk 2L"))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("sku-1")
                )));
    }

    private static MockMvc mvc() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();
        return MockMvcBuilders.standaloneSetup(
                new PrototypeStorefrontSurfaceController(
                        PrototypeStorefrontSurfaceProjection.standard(runtime)
                )
        ).build();
    }
}
