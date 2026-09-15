package mainstreet.merchantprofile;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** MS-PROT-051 v1.2 structured international postal-address contracts. */
class PostalAddressV1Test {

    @Test
    void normalizes_only_structure_and_retains_exact_original_input() {
        PostalAddressInput input = new PostalAddressInput(
                " gb ",
                List.of("  10 High Street  ", "Sŵn  House"),
                Optional.empty(),
                Optional.of("  Swansea  "),
                Optional.of("Wales"),
                Optional.of("  sa1 1aa "),
                Optional.empty()
        );

        PostalAddressEvidence evidence = PostalAddressEvidence.accept(input);

        assertEquals(input, evidence.originalInput());
        assertEquals("GB", evidence.normalizedAddress().countryCode());
        assertEquals(
                List.of("10 High Street", "Sŵn  House"),
                evidence.normalizedAddress().addressLines()
        );
        assertEquals(Optional.of("Swansea"), evidence.normalizedAddress().locality());
        assertEquals(Optional.of("sa1 1aa"), evidence.normalizedAddress().postalCode());
        assertEquals("POSTAL_ADDRESS_V1", evidence.normalizedAddress().schemaIdentity());
        assertEquals(
                "MS_POSTAL_ADDRESS_NORMALIZATION_V1",
                evidence.normalizedAddress().normalizationProfileIdentity()
        );
        assertEquals(
                "ISO_3166_1_ALPHA_2_2026_08_30",
                evidence.normalizedAddress().countryRegistryIdentity()
        );
    }

    @Test
    void accepts_international_shape_without_universal_postal_or_region_fields() {
        PostalAddressV1 address = PostalAddressEvidence.accept(new PostalAddressInput(
                "AE",
                List.of("Burj Khalifa", "Downtown Dubai"),
                Optional.empty(),
                Optional.of("Dubai"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        )).normalizedAddress();

        assertEquals(Optional.empty(), address.postalCode());
        assertEquals(Optional.empty(), address.administrativeArea());
    }

    @Test
    void rejects_unknown_country_blank_lines_blank_optional_and_controls() {
        assertInvalid(input("ZZ", List.of("10 High Street")));
        assertInvalid(input("GB", List.of()));
        assertInvalid(input("GB", List.of("   ")));
        assertInvalid(new PostalAddressInput(
                "GB",
                List.of("10 High Street"),
                Optional.empty(),
                Optional.of("  "),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));
        assertInvalid(input("GB", List.of("10\u0000 High Street")));
    }

    @Test
    void validates_optional_accepted_coordinates_and_provenance() {
        AcceptedLocationCoordinates coordinates = new AcceptedLocationCoordinates(
                new BigDecimal("51.621440"),
                new BigDecimal("-3.943646"),
                LocationCoordinateSourceKind.MERCHANT_SELECTED,
                Optional.empty(),
                "controller-a",
                Instant.parse("2026-08-30T06:30:00Z")
        );

        assertEquals(new BigDecimal("51.621440"), coordinates.latitude());
        assertThrows(IllegalArgumentException.class, () ->
                new AcceptedLocationCoordinates(
                        new BigDecimal("90.000001"),
                        BigDecimal.ZERO,
                        LocationCoordinateSourceKind.GEOCODER_EVIDENCE,
                        Optional.of("candidate-1"),
                        "controller-a",
                        Instant.parse("2026-08-30T06:30:00Z")
                )
        );
    }

    private static PostalAddressInput input(String country, List<String> lines) {
        return new PostalAddressInput(
                country,
                lines,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
    }

    private static void assertInvalid(PostalAddressInput input) {
        assertThrows(
                PostalAddressValidationException.class,
                () -> PostalAddressEvidence.accept(input)
        );
    }
}
