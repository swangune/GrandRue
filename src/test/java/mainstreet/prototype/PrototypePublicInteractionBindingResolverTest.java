package mainstreet.prototype;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrototypePublicInteractionBindingResolverTest {

    private final PrototypePublicInteractionBindingResolver resolver =
            PrototypePublicInteractionBindingResolver.standard();

    @Test
    void resolves_only_explicitly_registered_subject_participation() {
        assertEquals(
                List.of(new PrototypePublicInteractionBinding(
                        "garden-maintenance",
                        "Garden maintenance visit"
                )),
                resolver.resolve(
                        "prototype-gardener-bookable",
                        "appointment",
                        "arrange-appointment"
                )
        );
    }

    @Test
    void fails_closed_when_no_subject_participation_owner_is_registered() {
        assertEquals(
                List.of(),
                resolver.resolve(
                        "prototype-gardener-showcase",
                        "appointment",
                        "arrange-appointment"
                )
        );
        assertEquals(
                List.of(),
                resolver.resolve(
                        "prototype-gardener-bookable",
                        "publication",
                        "browse-published-content"
                )
        );
        assertEquals(
                List.of(),
                resolver.resolve(
                        "prototype-gardener-bookable",
                        "future-capability",
                        "future-interaction"
                )
        );
    }
}
