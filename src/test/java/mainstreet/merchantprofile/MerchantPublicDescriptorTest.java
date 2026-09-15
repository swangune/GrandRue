package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MS-PROT-051 merchant public descriptor contract.
 */
class MerchantPublicDescriptorTest {

    @Test
    void preserves_merchant_scoped_public_description_without_business_architecture() {
        MerchantScope scope = new MerchantScope("merchant-acme");

        MerchantPublicDescriptor descriptor = new MerchantPublicDescriptor(
                scope,
                "Acme Structural Consulting",
                Optional.of("Practical structural advice"),
                Optional.of("Independent structural consultancy"),
                Optional.of("We help clients understand and resolve structural problems.")
        );

        assertEquals(scope, descriptor.merchantScope());
        assertEquals("Acme Structural Consulting", descriptor.displayName());
        assertEquals(Optional.of("Practical structural advice"), descriptor.tagline());
        assertEquals(Optional.of("Independent structural consultancy"), descriptor.shortSummary());
        assertEquals(
                Optional.of("We help clients understand and resolve structural problems."),
                descriptor.approvedDescription()
        );
    }

    @Test
    void optional_public_copy_may_be_absent() {
        MerchantPublicDescriptor descriptor = new MerchantPublicDescriptor(
                new MerchantScope("merchant-publisher"),
                "Scholarship Bulletin",
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        assertEquals(Optional.empty(), descriptor.tagline());
        assertEquals(Optional.empty(), descriptor.shortSummary());
        assertEquals(Optional.empty(), descriptor.approvedDescription());
    }

    @Test
    void merchant_scope_and_display_name_are_required() {
        MerchantScope scope = new MerchantScope("merchant-acme");

        assertThrows(
                NullPointerException.class,
                () -> new MerchantPublicDescriptor(
                        null,
                        "Acme",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantPublicDescriptor(
                        scope,
                        "   ",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
        );
    }
}
