package mainstreet.surface;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SurfaceContributionRegistryCustomerEligibilityTest {

    @Test
    void rejects_customer_contribution_that_references_unregistered_eligibility_requirement() {
        CustomerSurfaceEligibilityRequirementIdentity requirement = requirement();
        SurfaceContributionDefinition customerContribution = customerContribution(requirement);

        assertThrows(
                IllegalArgumentException.class,
                () -> new SurfaceContributionRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(customerContribution)
                )
        );
    }

    @Test
    void accepts_customer_contribution_when_eligibility_requirement_is_registered() {
        CustomerSurfaceEligibilityRequirementIdentity requirement = requirement();
        SurfaceContributionDefinition customerContribution = customerContribution(requirement);

        assertDoesNotThrow(() -> new SurfaceContributionRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(customerContribution),
                Set.of(new CustomerSurfaceEligibilityRequirementDefinition(requirement))
        ));
    }

    private static CustomerSurfaceEligibilityRequirementIdentity requirement() {
        return new CustomerSurfaceEligibilityRequirementIdentity(
                "booking",
                "related-customer-booking"
        );
    }

    private static SurfaceContributionDefinition customerContribution(
            CustomerSurfaceEligibilityRequirementIdentity requirement
    ) {
        return new SurfaceContributionDefinition(
                new SurfaceContributionIdentity(
                        "booking",
                        "customer-booking"
                ),
                SurfaceAudience.CUSTOMER,
                SurfaceContributionKind.WORKSPACE,
                Optional.of("customer/bookings"),
                Set.of("booking-customer-summary"),
                Set.of(),
                SurfaceEligibilityContract.activeOnly(),
                SurfaceInteractionAvailabilityContract.independent(),
                Optional.of(requirement)
        );
    }
}
