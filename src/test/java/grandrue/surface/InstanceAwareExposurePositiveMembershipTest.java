package grandrue.surface;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstanceAwareExposurePositiveMembershipTest {

    private static final ExposableElementReference CONTACT =
            new ExposableElementReference("profile", "public-contact-point");
    private static final ExposureCandidateInstanceKindReference CONTACT_KIND =
            new ExposureCandidateInstanceKindReference("profile", "contact-point");

    @Test
    void stable_candidate_instance_identity_is_owner_and_kind_qualified() {
        ExposureCandidateInstanceReference profile =
                new ExposureCandidateInstanceReference(
                        "profile",
                        "contact-point",
                        "C1"
                );
        ExposureCandidateInstanceReference booking =
                new ExposureCandidateInstanceReference(
                        "booking",
                        "contact-point",
                        "C1"
                );

        assertNotEquals(profile, booking);
        assertEquals("profile", profile.ownerIdentifier());
        assertEquals("contact-point", profile.instanceKindIdentifier());
        assertEquals("C1", profile.instanceIdentifier());
    }

    @Test
    void instance_qualified_positive_member_contains_exact_stable_instance() {
        ExposureCandidateInstanceReference c1 = contact("C1");
        ResolvedExposedElement resolved = new ResolvedExposedElement(
                CONTACT,
                Optional.of(c1),
                instanceContract()
        );

        assertEquals(
                new ExposedElementMembership(CONTACT, Optional.of(c1)),
                resolved.membership()
        );
        assertEquals(instanceContract().identity(), resolved.contractIdentity());
    }

    @Test
    void two_distinct_instances_of_one_semantic_element_are_distinct_members() {
        ExposedElementMembership c1 = new ExposedElementMembership(
                CONTACT,
                Optional.of(contact("C1"))
        );
        ExposedElementMembership c2 = new ExposedElementMembership(
                CONTACT,
                Optional.of(contact("C2"))
        );

        assertEquals(2, Set.of(c1, c2).size());
    }

    @Test
    void instance_qualified_member_requires_exact_owner_and_kind() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ResolvedExposedElement(
                        CONTACT,
                        Optional.empty(),
                        instanceContract()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ResolvedExposedElement(
                        CONTACT,
                        Optional.of(new ExposureCandidateInstanceReference(
                                "profile",
                                "merchant-location",
                                "L1"
                        )),
                        instanceContract()
                )
        );
    }

    @Test
    void singleton_optional_evaluation_instance_never_becomes_member_identity() {
        ExposableElementReference displayName =
                new ExposableElementReference("profile", "public-display-name");
        ExposureElementContract singleton = new ExposureElementContract(
                new ExposureElementContractIdentity(
                        "profile",
                        "public-display-name"
                ),
                displayName,
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );

        ResolvedExposedElement resolved = new ResolvedExposedElement(
                displayName,
                Optional.of(contact("C1")),
                singleton
        );

        assertEquals(
                new ExposedElementMembership(displayName, Optional.empty()),
                resolved.membership()
        );
    }

    @Test
    void public_api_positive_set_exposes_membership_not_bare_elements() {
        assertEquals(
                Set.class,
                ApiExposedElementSet.class.getDeclaredMethods()[0].getReturnType()
        );
        assertTrue(java.util.Arrays.stream(ApiExposedElementSet.class.getMethods())
                .filter(method -> method.getDeclaringClass()
                        == ApiExposedElementSet.class)
                .anyMatch(method -> method.getName().equals("members")));
        assertTrue(java.util.Arrays.stream(ApiExposedElementSet.class.getMethods())
                .filter(method -> method.getDeclaringClass()
                        == ApiExposedElementSet.class)
                .noneMatch(method -> method.getName().equals("elements")));
    }

    private static ExposureCandidateInstanceReference contact(String identifier) {
        return new ExposureCandidateInstanceReference(
                CONTACT_KIND.ownerIdentifier(),
                CONTACT_KIND.instanceKindIdentifier(),
                identifier
        );
    }

    private static ExposureElementContract instanceContract() {
        return new ExposureElementContract(
                new ExposureElementContractIdentity(
                        "profile",
                        "public-contact-point"
                ),
                CONTACT,
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(CONTACT_KIND),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
    }
}
