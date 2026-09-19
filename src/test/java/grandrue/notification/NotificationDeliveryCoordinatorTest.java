package grandrue.notification;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationDeliveryCoordinatorTest {

    private static final Instant NOW = Instant.parse("2026-08-24T07:30:00Z");
    private static final NotificationOwnerScope MERCHANT =
            NotificationOwnerScope.merchant("merchant-1");

    @Test
    void current_ineligibility_prevents_attempt_and_provider_call() {
        MemoryStore store = preparedStore();
        AtomicInteger calls = new AtomicInteger();
        NotificationDeliveryCoordinator coordinator = new NotificationDeliveryCoordinator(
                store,
                dispatch -> false,
                (dispatch, idempotencyReference) -> {
                    calls.incrementAndGet();
                    return accepted();
                },
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        assertThrows(
                NotificationDispatchNotEligibleException.class,
                () -> coordinator.deliver("dispatch-1", "attempt-1", "provider-context")
        );
        assertEquals(0, calls.get());
        assertEquals(Optional.empty(), store.findAttempt("attempt-1"));
    }

    @Test
    void provider_receives_stable_dispatch_identity_as_idempotency_reference() {
        MemoryStore store = preparedStore();
        AtomicReference<String> providerIdempotency = new AtomicReference<>();
        NotificationDeliveryCoordinator coordinator = new NotificationDeliveryCoordinator(
                store,
                dispatch -> true,
                (dispatch, idempotencyReference) -> {
                    providerIdempotency.set(idempotencyReference);
                    return accepted();
                },
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        DeliveryAttempt completed = coordinator.deliver(
                "dispatch-1",
                "attempt-1",
                "provider-context"
        );

        assertEquals("dispatch-1", providerIdempotency.get());
        assertEquals(DeliveryAttemptOutcome.KNOWN_ACCEPTED, completed.outcome());
    }

    @Test
    void provider_exception_is_preserved_as_execution_uncertain_not_known_failure() {
        MemoryStore store = preparedStore();
        NotificationDeliveryCoordinator coordinator = new NotificationDeliveryCoordinator(
                store,
                dispatch -> true,
                (dispatch, idempotencyReference) -> {
                    throw new IllegalStateException("response lost");
                },
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        DeliveryAttempt completed = coordinator.deliver(
                "dispatch-1",
                "attempt-1",
                "provider-context"
        );

        assertEquals(DeliveryAttemptOutcome.EXECUTION_UNCERTAIN, completed.outcome());
    }

    @Test
    void existing_started_attempt_is_not_blindly_sent_again() {
        MemoryStore store = preparedStore();
        store.startAttempt(started("attempt-1"));
        AtomicInteger calls = new AtomicInteger();
        NotificationDeliveryCoordinator coordinator = new NotificationDeliveryCoordinator(
                store,
                dispatch -> true,
                (dispatch, idempotencyReference) -> {
                    calls.incrementAndGet();
                    return accepted();
                },
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        DeliveryAttempt existing = coordinator.deliver(
                "dispatch-1",
                "attempt-1",
                "provider-context"
        );

        assertEquals(DeliveryAttemptOutcome.STARTED, existing.outcome());
        assertEquals(0, calls.get());
    }

    @Test
    void existing_uncertain_attempt_is_not_blindly_sent_again() {
        MemoryStore store = preparedStore();
        store.startAttempt(started("attempt-1"));
        store.completeAttempt(new DeliveryAttempt(
                "attempt-1",
                "dispatch-1",
                MERCHANT,
                NOW,
                "provider-context",
                "dispatch-1",
                DeliveryAttemptOutcome.EXECUTION_UNCERTAIN,
                Optional.of("provider-timeout")
        ));
        AtomicInteger calls = new AtomicInteger();
        NotificationDeliveryCoordinator coordinator = new NotificationDeliveryCoordinator(
                store,
                dispatch -> true,
                (dispatch, idempotencyReference) -> {
                    calls.incrementAndGet();
                    return accepted();
                },
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        DeliveryAttempt existing = coordinator.deliver(
                "dispatch-1",
                "attempt-1",
                "provider-context"
        );

        assertEquals(DeliveryAttemptOutcome.EXECUTION_UNCERTAIN, existing.outcome());
        assertEquals(0, calls.get());
    }

    private static MemoryStore preparedStore() {
        MemoryStore store = new MemoryStore();
        store.establishIntent(new NotificationIntent(
                "intent-1",
                "appointment.confirmed",
                MERCHANT,
                "appointment-1",
                "commitment-contact",
                Optional.empty(),
                Optional.empty(),
                NOW
        ));
        store.establishDispatch(new NotificationDispatch(
                "dispatch-1",
                "intent-1",
                MERCHANT,
                new NotificationRecipient(
                        "recipient-1",
                        "CustomerContext",
                        "customer-1",
                        NotificationRecipientResolutionBasis.COMMITMENT_BOUND
                ),
                NotificationChannel.EMAIL,
                "endpoint-ref",
                "content-ref",
                NOW
        ));
        return store;
    }

    private static DeliveryAttempt started(String identity) {
        return new DeliveryAttempt(
                identity,
                "dispatch-1",
                MERCHANT,
                NOW,
                "provider-context",
                "dispatch-1",
                DeliveryAttemptOutcome.STARTED,
                Optional.empty()
        );
    }

    private static NotificationDeliveryProviderResult accepted() {
        return new NotificationDeliveryProviderResult(
                DeliveryAttemptOutcome.KNOWN_ACCEPTED,
                Optional.empty()
        );
    }

    private static final class MemoryStore implements NotificationStore {
        private final Map<String, NotificationIntent> intents = new LinkedHashMap<>();
        private final Map<String, NotificationDispatch> dispatches = new LinkedHashMap<>();
        private final Map<String, DeliveryAttempt> attempts = new LinkedHashMap<>();
        private final List<DeliveryEvidence> evidence = new ArrayList<>();

        @Override
        public NotificationIntent establishIntent(NotificationIntent intent) {
            intents.putIfAbsent(intent.intentIdentity(), intent);
            return intents.get(intent.intentIdentity());
        }

        @Override
        public NotificationDispatch establishDispatch(NotificationDispatch dispatch) {
            dispatches.putIfAbsent(dispatch.dispatchIdentity(), dispatch);
            return dispatches.get(dispatch.dispatchIdentity());
        }

        @Override
        public DeliveryAttempt startAttempt(DeliveryAttempt attempt) {
            attempts.putIfAbsent(attempt.attemptIdentity(), attempt);
            return attempts.get(attempt.attemptIdentity());
        }

        @Override
        public DeliveryAttempt completeAttempt(DeliveryAttempt attempt) {
            attempts.put(attempt.attemptIdentity(), attempt);
            return attempt;
        }

        @Override
        public DeliveryEvidence recordEvidence(DeliveryEvidence value) {
            evidence.add(value);
            return value;
        }

        @Override
        public Optional<NotificationIntent> findIntent(String identity) {
            return Optional.ofNullable(intents.get(identity));
        }

        @Override
        public Optional<NotificationDispatch> findDispatch(String identity) {
            return Optional.ofNullable(dispatches.get(identity));
        }

        @Override
        public Optional<DeliveryAttempt> findAttempt(String identity) {
            return Optional.ofNullable(attempts.get(identity));
        }

        @Override
        public List<DeliveryAttempt> attemptsForDispatch(String dispatchIdentity) {
            return attempts.values().stream()
                    .filter(attempt -> attempt.dispatchIdentity().equals(dispatchIdentity))
                    .toList();
        }
    }
}
