package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfileExposureChoiceReadPortAdapterTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant NOW = Instant.parse("2026-09-02T12:00:00Z");

    @Test
    void contact_adapter_returns_only_current_active_exposure() {
        MerchantContactPointAuthority authority = contactAuthority(Map.of(
                "C1", contact("C1", MerchantContactPointLifecycle.ACTIVE, MerchantContactPointExposure.PUBLIC),
                "C2", contact("C2", MerchantContactPointLifecycle.ACTIVE, MerchantContactPointExposure.PRIVATE_INTERNAL),
                "C3", contact("C3", MerchantContactPointLifecycle.RETIRED, MerchantContactPointExposure.PUBLIC)
        ));
        MerchantContactPointExposureChoiceReadPort port =
                new AuthorityBackedMerchantContactPointExposureChoiceReadPort(authority);

        assertEquals(Optional.of(MerchantContactPointExposure.PUBLIC),
                port.currentActiveExposure(MERCHANT, "C1"));
        assertEquals(Optional.of(MerchantContactPointExposure.PRIVATE_INTERNAL),
                port.currentActiveExposure(MERCHANT, "C2"));
        assertTrue(port.currentActiveExposure(MERCHANT, "C3").isEmpty());
        assertTrue(port.currentActiveExposure(MERCHANT, "missing").isEmpty());
    }

    @Test
    void contact_progress_affinity_requires_exact_current_revision_identity() {
        MerchantContactPointAuthority authority = contactAuthority(Map.of(
                "C1", contact(
                        "C1",
                        MerchantContactPointLifecycle.ACTIVE,
                        MerchantContactPointExposure.PUBLIC
                )
        ));
        MerchantContactPointExposureChoiceReadPort port =
                new AuthorityBackedMerchantContactPointExposureChoiceReadPort(authority);

        assertEquals(
                Optional.of(MerchantContactPointExposure.PUBLIC),
                port.currentActiveExposureAtProgress(MERCHANT, "C1", "cp-rev-C1")
        );
        assertTrue(port.currentActiveExposureAtProgress(
                MERCHANT,
                "C1",
                "cp-rev-stale"
        ).isEmpty());
        assertTrue(port.currentActiveExposureAtProgress(
                MERCHANT,
                "missing",
                "cp-rev-C1"
        ).isEmpty());
    }

    @Test
    void location_adapter_requires_current_active_location_and_current_explicit_choice() {
        MerchantLocationAuthority locations = locationAuthority(Map.of(
                "L1", location("L1", MerchantLocationLifecycle.ACTIVE),
                "L2", location("L2", MerchantLocationLifecycle.ACTIVE),
                "L3", location("L3", MerchantLocationLifecycle.RETIRED)
        ));
        MerchantLocationExposureChoiceAuthority choices = choiceAuthority(Map.of(
                "L1", choice("L1", MerchantLocationExposure.PUBLIC),
                "L3", choice("L3", MerchantLocationExposure.PUBLIC),
                "L4", choice("L4", MerchantLocationExposure.PUBLIC)
        ));
        MerchantLocationExposureChoiceReadPort port =
                new AuthorityBackedMerchantLocationExposureChoiceReadPort(
                        locations,
                        choices
                );

        assertEquals(Optional.of(MerchantLocationExposure.PUBLIC),
                port.currentActiveExposure(MERCHANT, "L1"));
        assertTrue(port.currentActiveExposure(MERCHANT, "L2").isEmpty());
        assertTrue(port.currentActiveExposure(MERCHANT, "L3").isEmpty());
        assertTrue(port.currentActiveExposure(MERCHANT, "L4").isEmpty());
        assertTrue(port.currentActiveExposure(MERCHANT, "missing").isEmpty());
    }

    @Test
    void location_progress_must_match_active_source_before_choice_authority_is_consulted() {
        MerchantLocationAuthority locations = locationAuthority(Map.of(
                "L1", location("L1", MerchantLocationLifecycle.ACTIVE)
        ));
        AtomicInteger choiceReads = new AtomicInteger();
        MerchantLocationExposureChoiceAuthority choices =
                new MerchantLocationExposureChoiceAuthority() {
                    @Override
                    public MerchantLocationExposureChoiceRevision set(
                            SetMerchantLocationExposureChoiceCommand command,
                            TrustedExecutionContext trustedContext
                    ) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Optional<MerchantLocationExposureChoiceRevision> current(
                            MerchantScope merchantScope,
                            String locationIdentity
                    ) {
                        assertEquals(MERCHANT, merchantScope);
                        assertEquals("L1", locationIdentity);
                        choiceReads.incrementAndGet();
                        return Optional.of(choice(
                                "L1",
                                MerchantLocationExposure.PUBLIC
                        ));
                    }

                    @Override
                    public Optional<MerchantLocationExposureChoiceRevision> revision(
                            String revisionIdentity
                    ) {
                        return Optional.empty();
                    }
                };
        MerchantLocationExposureChoiceReadPort port =
                new AuthorityBackedMerchantLocationExposureChoiceReadPort(
                        locations,
                        choices
                );

        assertTrue(port.currentActiveExposureAtProgress(
                MERCHANT,
                "L1",
                "loc-rev-stale"
        ).isEmpty());
        assertEquals(0, choiceReads.get());

        assertEquals(
                Optional.of(MerchantLocationExposure.PUBLIC),
                port.currentActiveExposureAtProgress(
                        MERCHANT,
                        "L1",
                        "loc-rev-L1"
                )
        );
        assertEquals(1, choiceReads.get());
    }

    @Test
    void adapter_does_not_convert_unexpected_authority_failure_to_absence() {
        MerchantContactPointAuthority authority = new MerchantContactPointAuthority() {
            @Override
            public MerchantContactPointRevision create(CreateMerchantContactPointCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public MerchantContactPointRevision update(UpdateMerchantContactPointCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public MerchantContactPointRevision retire(RetireMerchantContactPointCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<MerchantContactPointRevision> current(MerchantScope merchantScope, String contactPointIdentity) {
                throw new IllegalStateException("authority unavailable");
            }

            @Override
            public Optional<MerchantContactPointRevision> revision(String revisionIdentity) {
                return Optional.empty();
            }
        };
        MerchantContactPointExposureChoiceReadPort port =
                new AuthorityBackedMerchantContactPointExposureChoiceReadPort(authority);

        IllegalStateException failure = assertThrows(
                IllegalStateException.class,
                () -> port.currentActiveExposure(MERCHANT, "C1")
        );
        assertEquals("authority unavailable", failure.getMessage());
    }

    private static MerchantContactPointRevision contact(
            String identity,
            MerchantContactPointLifecycle lifecycle,
            MerchantContactPointExposure exposure
    ) {
        return new MerchantContactPointRevision(
                "cp-rev-" + identity,
                MerchantContactPointScope.merchant(MERCHANT),
                identity,
                1,
                Optional.empty(),
                lifecycle,
                MerchantContactPointKind.EMAIL,
                identity.toLowerCase() + "@example.com",
                exposure,
                Optional.empty(),
                Optional.empty(),
                "req-cp-" + identity,
                "prov",
                "actor",
                "controller",
                NOW
        );
    }

    private static MerchantLocationRevision location(
            String identity,
            MerchantLocationLifecycle lifecycle
    ) {
        return new MerchantLocationRevision(
                "loc-rev-" + identity,
                MERCHANT,
                identity,
                1,
                Optional.empty(),
                lifecycle,
                Optional.empty(),
                PostalAddressEvidence.accept(new PostalAddressInput(
                        "GB",
                        List.of("1 High Street"),
                        Optional.empty(),
                        Optional.of("London"),
                        Optional.empty(),
                        Optional.of("SW1A 1AA"),
                        Optional.empty()
                )),
                Optional.empty(),
                "req-loc-" + identity,
                "prov",
                "actor",
                "controller",
                NOW
        );
    }

    private static MerchantLocationExposureChoiceRevision choice(
            String identity,
            MerchantLocationExposure exposure
    ) {
        return new MerchantLocationExposureChoiceRevision(
                "choice-rev-" + identity,
                MERCHANT,
                identity,
                1,
                Optional.empty(),
                exposure,
                "req-choice-" + identity,
                "prov",
                "actor",
                "controller",
                NOW
        );
    }

    private static MerchantContactPointAuthority contactAuthority(
            Map<String, MerchantContactPointRevision> current
    ) {
        return new MerchantContactPointAuthority() {
            @Override
            public MerchantContactPointRevision create(CreateMerchantContactPointCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public MerchantContactPointRevision update(UpdateMerchantContactPointCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public MerchantContactPointRevision retire(RetireMerchantContactPointCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<MerchantContactPointRevision> current(MerchantScope merchantScope, String contactPointIdentity) {
                assertEquals(MERCHANT, merchantScope);
                return Optional.ofNullable(current.get(contactPointIdentity));
            }

            @Override
            public Optional<MerchantContactPointRevision> revision(String revisionIdentity) {
                return Optional.empty();
            }
        };
    }

    private static MerchantLocationAuthority locationAuthority(
            Map<String, MerchantLocationRevision> current
    ) {
        return new MerchantLocationAuthority() {
            @Override
            public MerchantLocationRevision create(CreateMerchantLocationCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public MerchantLocationRevision correct(CorrectMerchantLocationCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public MerchantLocationRevision retire(RetireMerchantLocationCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<MerchantLocationRevision> current(MerchantScope merchantScope, String locationIdentity) {
                assertEquals(MERCHANT, merchantScope);
                return Optional.ofNullable(current.get(locationIdentity));
            }

            @Override
            public Optional<MerchantLocationRevision> revision(String revisionIdentity) {
                return Optional.empty();
            }
        };
    }

    private static MerchantLocationExposureChoiceAuthority choiceAuthority(
            Map<String, MerchantLocationExposureChoiceRevision> current
    ) {
        return new MerchantLocationExposureChoiceAuthority() {
            @Override
            public MerchantLocationExposureChoiceRevision set(SetMerchantLocationExposureChoiceCommand command, TrustedExecutionContext trustedContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<MerchantLocationExposureChoiceRevision> current(MerchantScope merchantScope, String locationIdentity) {
                assertEquals(MERCHANT, merchantScope);
                return Optional.ofNullable(current.get(locationIdentity));
            }

            @Override
            public Optional<MerchantLocationExposureChoiceRevision> revision(String revisionIdentity) {
                return Optional.empty();
            }
        };
    }
}
