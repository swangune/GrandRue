package grandrue.surface;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExposureContractCandidateAffinityTest {

    @Test
    void exposure_contract_rejects_cross_owner_element_reference() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExposureElementContract(
                        new ExposureElementContractIdentity(
                                "profile",
                                "public-display-name"
                        ),
                        new ExposableElementReference(
                                "business-hours",
                                "public-display-name"
                        ),
                        SurfaceAudience.PUBLIC,
                        ExposureMemberIdentitySpecification.singleton(),
                        ExposureDecision.EXPOSE,
                        Optional.empty(),
                        Set.of()
                )
        );
    }

    @Test
    void resolved_positive_member_is_constructed_from_the_exact_contract()
            throws Exception {
        Constructor<ResolvedExposedElement> constructor =
                ResolvedExposedElement.class.getDeclaredConstructor(
                        ExposableElementReference.class,
                        Optional.class,
                        ExposureElementContract.class
                );
        constructor.setAccessible(true);

        ExposableElementReference reference =
                new ExposableElementReference("profile", "display-name");
        ExposureElementContract contract = contract(reference);

        ResolvedExposedElement member = constructor.newInstance(
                reference,
                Optional.empty(),
                contract
        );
        assertEquals(reference, member.membership().elementReference());
        assertEquals(contract.identity(), member.contractIdentity());

        InvocationTargetException failure = assertThrows(
                InvocationTargetException.class,
                () -> constructor.newInstance(
                        new ExposableElementReference("profile", "tagline"),
                        Optional.empty(),
                        contract
                )
        );
        assertTrue(failure.getCause() instanceof IllegalArgumentException);
    }

    private static ExposureElementContract contract(
            ExposableElementReference reference
    ) {
        return new ExposureElementContract(
                new ExposureElementContractIdentity(
                        reference.ownerIdentifier(),
                        "public-display-name"
                ),
                reference,
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
    }
}
