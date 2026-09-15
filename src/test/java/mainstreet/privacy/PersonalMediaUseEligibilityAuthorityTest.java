package mainstreet.privacy;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonalMediaUseEligibilityAuthorityTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant T0 = Instant.parse("2026-08-24T10:00:00Z");

    @Test
    void unchanged_multi_subject_media_requires_every_required_subject_basis() {
        DataSubjectReference subjectA = new DataSubjectReference(MERCHANT, "subject-a");
        DataSubjectReference subjectB = new DataSubjectReference(MERCHANT, "subject-b");

        PersonalMediaUseEligibilityAuthority authority =
                new PersonalMediaUseEligibilityAuthority(
                        (subject, dataScope, purpose, audience, instant) ->
                                subject.equals(subjectA)
                );

        assertFalse(authority.isEligible(
                MERCHANT,
                "media-1",
                List.of(subjectA, subjectB),
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "PUBLIC",
                T0
        ));
    }

    @Test
    void all_required_subjects_may_satisfy_exact_purpose_and_audience() {
        DataSubjectReference subjectA = new DataSubjectReference(MERCHANT, "subject-a");
        DataSubjectReference subjectB = new DataSubjectReference(MERCHANT, "subject-b");
        Set<DataSubjectReference> authorised = Set.of(subjectA, subjectB);

        PersonalMediaUseEligibilityAuthority authority =
                new PersonalMediaUseEligibilityAuthority(
                        (subject, dataScope, purpose, audience, instant) ->
                                authorised.contains(subject)
                                        && dataScope.equals("media-1")
                                        && purpose == DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE
                                        && audience.equals("PUBLIC")
                );

        assertTrue(authority.isEligible(
                MERCHANT,
                "media-1",
                List.of(subjectA, subjectB),
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "PUBLIC",
                T0
        ));
        assertFalse(authority.isEligible(
                MERCHANT,
                "media-1",
                List.of(subjectA, subjectB),
                DataUsePurpose.OPERATIONAL_NEED,
                "PUBLIC",
                T0
        ));
    }

    @Test
    void non_personal_media_path_does_not_require_fictitious_subject_basis() {
        PersonalMediaUseEligibilityAuthority authority =
                new PersonalMediaUseEligibilityAuthority(
                        (subject, dataScope, purpose, audience, instant) -> false
                );

        assertTrue(authority.isEligible(
                MERCHANT,
                "media-empty-room",
                List.of(),
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "PUBLIC",
                T0
        ));
    }

    @Test
    void subject_reference_cannot_cross_merchant_scope() {
        PersonalMediaUseEligibilityAuthority authority =
                new PersonalMediaUseEligibilityAuthority(
                        (subject, dataScope, purpose, audience, instant) -> true
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> authority.isEligible(
                        MERCHANT,
                        "media-1",
                        List.of(new DataSubjectReference(
                                new MerchantScope("merchant-b"),
                                "subject-b"
                        )),
                        DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                        "PUBLIC",
                        T0
                )
        );
    }

    @Test
    void personal_data_use_basis_has_half_open_effective_interval() {
        PersonalDataUseBasis basis = new PersonalDataUseBasis(
                "basis-1",
                new DataSubjectReference(MERCHANT, "subject-a"),
                "media-1",
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "PUBLIC",
                "merchant-recorded-authority",
                "evidence-1",
                T0,
                java.util.Optional.of(T0.plusSeconds(60))
        );

        assertTrue(basis.isEffectiveAt(T0));
        assertTrue(basis.isEffectiveAt(T0.plusSeconds(59)));
        assertFalse(basis.isEffectiveAt(T0.plusSeconds(60)));
    }
}
