package mainstreet.commercial;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommercialEntitlementGrantProvenanceTest {

    @Test
    void retains_authoritative_commercial_source_identity() {
        CommercialEntitlementGrantProvenance provenance =
                new CommercialEntitlementGrantProvenance(
                        "merchant-commercial-agreement",
                        "agreement-17"
                );

        assertEquals(
                "merchant-commercial-agreement",
                provenance.sourceClassIdentifier()
        );
        assertEquals("agreement-17", provenance.sourceIdentifier());
    }

    @Test
    void rejects_missing_source_identity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommercialEntitlementGrantProvenance(null, "source-1")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommercialEntitlementGrantProvenance(" ", "source-1")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommercialEntitlementGrantProvenance("trial", null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommercialEntitlementGrantProvenance("trial", " ")
        );
    }
}
