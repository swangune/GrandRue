package grandrue.commercial;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InitialFullExperienceTrialEstablisherTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant START = Instant.parse("2026-08-23T04:00:00Z");

    @Test
    void establishes_candidate_when_configuration_confirms_first_activation() {
        AtomicInitialTrialStore store = new AtomicInitialTrialStore();
        InitialFullExperienceTrialEstablisher establisher =
                new InitialFullExperienceTrialEstablisher(
                        (merchant, revision, activation, activatedAt) -> true,
                        store
                );
        InitialFullExperienceTrial candidate = trial("trial-1");

        InitialFullExperienceTrial established = establisher.establish(candidate);

        assertSame(candidate, established);
        assertEquals(1, store.size());
    }

    @Test
    void duplicate_delivery_resolves_existing_trial_without_extension() {
        AtomicInitialTrialStore store = new AtomicInitialTrialStore();
        InitialFullExperienceTrialEstablisher establisher =
                new InitialFullExperienceTrialEstablisher(
                        (merchant, revision, activation, activatedAt) -> true,
                        store
                );
        InitialFullExperienceTrial first = trial("trial-1");
        InitialFullExperienceTrial duplicate = trial("trial-duplicate-attempt");

        InitialFullExperienceTrial establishedFirst = establisher.establish(first);
        InitialFullExperienceTrial establishedAgain = establisher.establish(duplicate);

        assertSame(establishedFirst, establishedAgain);
        assertEquals("trial-1", establishedAgain.trialIdentity());
        assertEquals(START, establishedAgain.startsAt());
        assertEquals(1, store.size());
    }

    @Test
    void rejects_candidate_that_is_not_from_first_committed_activation() {
        AtomicInteger storeCalls = new AtomicInteger();
        InitialFullExperienceTrialEstablisher establisher =
                new InitialFullExperienceTrialEstablisher(
                        (merchant, revision, activation, activatedAt) -> false,
                        candidate -> {
                            storeCalls.incrementAndGet();
                            return candidate;
                        }
                );

        assertThrows(
                InvalidInitialFullExperienceTrialOriginException.class,
                () -> establisher.establish(trial("trial-1"))
        );
        assertEquals(0, storeCalls.get());
    }

    @Test
    void forwards_exact_activation_provenance_to_configuration_authority() {
        AtomicInteger matches = new AtomicInteger();
        InitialFullExperienceTrial candidate = trial("trial-1");
        InitialFullExperienceTrialEstablisher establisher =
                new InitialFullExperienceTrialEstablisher(
                        (merchant, revision, activation, activatedAt) -> {
                            if (merchant.equals(MERCHANT)
                                    && revision.equals("config-1")
                                    && activation.equals("activation-1")
                                    && activatedAt.equals(START)) {
                                matches.incrementAndGet();
                                return true;
                            }
                            return false;
                        },
                        value -> value
                );

        establisher.establish(candidate);

        assertEquals(1, matches.get());
    }

    private static InitialFullExperienceTrial trial(String identity) {
        return new InitialFullExperienceTrial(
                identity,
                MERCHANT,
                "config-1",
                "activation-1",
                START
        );
    }

    private static final class AtomicInitialTrialStore
            implements InitialFullExperienceTrialStore {

        private final Map<MerchantScope, InitialFullExperienceTrial> trials =
                new ConcurrentHashMap<>();

        @Override
        public InitialFullExperienceTrial establishIfAbsent(
                InitialFullExperienceTrial candidate
        ) {
            return trials.computeIfAbsent(
                    candidate.merchantScope(),
                    ignored -> candidate
            );
        }

        int size() {
            return trials.size();
        }
    }
}
