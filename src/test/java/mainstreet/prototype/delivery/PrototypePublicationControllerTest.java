package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypeMerchantRuntime;
import mainstreet.prototype.PrototypePublicationProjection;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PrototypePublicationControllerTest {

    @Test
    void information_only_merchant_exposes_seeded_published_content_without_commerce()
            throws Exception {
        MockMvc mvc = mvc();

        mvc.perform(get(
                        "/prototype/merchants/prototype-publisher/publications"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].publicationIdentifier")
                        .value("publication-1"))
                .andExpect(jsonPath("$[0].publicationState")
                        .value("PUBLISHED"))
                .andExpect(jsonPath("$[0].revision").value(1));
    }

    @Test
    void showcase_gardener_uses_the_same_publication_projection_with_different_content()
            throws Exception {
        MockMvc mvc = mvc();

        mvc.perform(get(
                        "/prototype/merchants/prototype-gardener-showcase/publications"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].publicationIdentifier")
                        .value("garden-project-1"))
                .andExpect(jsonPath("$[0].title")
                        .value("Courtyard Garden Transformation"))
                .andExpect(jsonPath("$[0].publicationState")
                        .value("PUBLISHED"));
    }

    private static MockMvc mvc() {
        return MockMvcBuilders.standaloneSetup(
                new PrototypePublicationController(
                        new PrototypePublicationProjection(
                                PrototypeMerchantRuntime.standard()
                        )
                )
        ).build();
    }
}
