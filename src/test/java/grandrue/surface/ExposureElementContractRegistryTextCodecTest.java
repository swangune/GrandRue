package grandrue.surface;

import grandrue.semantic.release.SemanticDefinitionSectionDecoder;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExposureElementContractRegistryTextCodecTest {

    private static final String RELEASE = "semantic-registry-19";

    @Test
    void round_trips_exact_release_and_explicit_membership_semantics() {
        ExposureElementContract singleton = contract(
                "profile",
                "public-display-name",
                "public-display-name",
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
        ExposureElementContract instanceQualified = contract(
                "profile",
                "public-contact-point",
                "public-contact-point",
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference(
                                "profile",
                                "contact-point"
                        )
                ),
                ExposureDecision.WITHHOLD,
                Optional.of(new MerchantExposureChoiceSourceReference(
                        "profile",
                        "contact-point-public-exposure"
                )),
                Set.of(new ExposureRequirementReference(
                        "data-protection",
                        "purpose-permits-public-observation"
                ))
        );
        ExposureElementContractRegistrySnapshot original =
                new ExposureElementContractRegistrySnapshot(
                        RELEASE,
                        Set.of(singleton, instanceQualified)
                );

        ExposureElementContractRegistryTextCodec codec =
                new ExposureElementContractRegistryTextCodec();
        ExposureElementContractRegistrySnapshot decoded =
                codec.decode(codec.encode(original));

        assertEquals(RELEASE, decoded.semanticRegistryReleaseIdentifier());
        assertEquals(original.contracts(), decoded.contracts());
        assertEquals(
                ExposureMemberIdentitySpecification.singleton(),
                decoded.contract(
                        RELEASE,
                        singleton.identity()
                ).orElseThrow().memberIdentitySpecification()
        );
        assertEquals(
                instanceQualified.memberIdentitySpecification(),
                decoded.contract(
                        RELEASE,
                        instanceQualified.identity()
                ).orElseThrow().memberIdentitySpecification()
        );
    }

    @Test
    void encoding_is_deterministic_independent_of_input_set_iteration_order() {
        ExposureElementContract first = contract(
                "profile",
                "public-display-name",
                "public-display-name",
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
        ExposureElementContract second = contract(
                "business-hours",
                "public-business-hours",
                "public-business-hours",
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference(
                                "business-hours",
                                "business-hours-scope"
                        )
                ),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
        ExposureElementContractRegistryTextCodec codec =
                new ExposureElementContractRegistryTextCodec();

        byte[] one = codec.encode(new ExposureElementContractRegistrySnapshot(
                RELEASE,
                Set.of(first, second)
        ));
        byte[] two = codec.encode(new ExposureElementContractRegistrySnapshot(
                RELEASE,
                Set.of(second, first)
        ));

        assertArrayEquals(one, two);
    }

    @Test
    void codec_is_a_registry_owned_semantic_definition_section_decoder() {
        assertTrue(SemanticDefinitionSectionDecoder.class.isAssignableFrom(
                ExposureElementContractRegistryTextCodec.class
        ));
    }

    @Test
    void malformed_or_legacy_definition_without_explicit_membership_semantics_is_rejected() {
        ExposureElementContractRegistryTextCodec codec =
                new ExposureElementContractRegistryTextCodec();
        byte[] legacyLike = (
                "format=mainstreet-exposure-definitions-v1\n"
                        + "release=c2VtYW50aWMtcmVnaXN0cnktMTk=\n"
                        + "contract=cHJvZmlsZQ==|cHVibGljLWRpc3BsYXktbmFtZQ==|"
                        + "cHJvZmlsZQ==|cHVibGljLWRpc3BsYXktbmFtZQ==|PUBLIC|EXPOSE|-|-\n"
        ).getBytes(StandardCharsets.UTF_8);

        assertThrows(IllegalArgumentException.class, () -> codec.decode(legacyLike));
    }

    @Test
    void decoded_release_is_taken_from_exact_encoded_evidence_not_from_current_portfolio() {
        ExposureElementContractRegistryTextCodec codec =
                new ExposureElementContractRegistryTextCodec();
        ExposureElementContractRegistrySnapshot historical =
                new ExposureElementContractRegistrySnapshot(
                        "semantic-registry-historical",
                        Set.of()
                );

        ExposureElementContractRegistrySnapshot decoded =
                codec.decode(codec.encode(historical));

        assertEquals(
                "semantic-registry-historical",
                decoded.semanticRegistryReleaseIdentifier()
        );
        assertTrue(decoded.contracts().isEmpty());
    }

    private static ExposureElementContract contract(
            String owner,
            String contractIdentifier,
            String elementIdentifier,
            SurfaceAudience audience,
            ExposureMemberIdentitySpecification memberIdentitySpecification,
            ExposureDecision baselineDecision,
            Optional<MerchantExposureChoiceSourceReference> merchantChoiceSource,
            Set<ExposureRequirementReference> requirements
    ) {
        return new ExposureElementContract(
                new ExposureElementContractIdentity(owner, contractIdentifier),
                new ExposableElementReference(owner, elementIdentifier),
                audience,
                memberIdentitySpecification,
                baselineDecision,
                merchantChoiceSource,
                requirements
        );
    }
}
