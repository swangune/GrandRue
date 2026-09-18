package grandrue.booking;

import grandrue.application.MerchantScope;
import grandrue.semantic.AllocationClaim;
import grandrue.semantic.AllocationConflictException;
import grandrue.semantic.AllocationScope;
import grandrue.semantic.DomainEvent;
import grandrue.semantic.TimeWindowAllocationScope;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class InMemoryBookingUnitOfWork
        implements BookingUnitOfWork, BookingOutbox {

    private State state = State.empty();

    @Override
    public synchronized BookingConfirmation execute(
            ConfirmBookingCommand command,
            Function<BookingTransaction, BookingConfirmation> work
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(work, "work");

        ScopedIdentifier commandKey = new ScopedIdentifier(
                command.merchantScope(),
                command.identifier()
        );
        HandledCommand handled = state.handledCommands().get(commandKey);
        if (handled != null) {
            if (!handled.command().equals(command)) {
                throw new CommandIdentityConflictException(command.identifier());
            }
            return handled.confirmation();
        }

        StagedTransaction transaction =
                new StagedTransaction(state, command.merchantScope());
        BookingConfirmation result = Objects.requireNonNull(
                work.apply(transaction),
                "Booking work returned no confirmation"
        );
        state = transaction.commit(command, result);
        return result;
    }

    public synchronized Optional<Booking> booking(
            MerchantScope merchantScope,
            String identifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        return Optional.ofNullable(state.bookings().get(
                new ScopedIdentifier(merchantScope, identifier)
        ));
    }

    public synchronized Optional<AllocationClaim> conflictingClaim(
            MerchantScope merchantScope,
            AllocationScope scope
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(scope, "scope");
        return findConflict(state.claims(), merchantScope, scope);
    }

    public synchronized Optional<DomainEvent> pendingEvent(
            MerchantScope merchantScope,
            String identifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        return Optional.ofNullable(state.pendingEvents().get(
                new ScopedIdentifier(merchantScope, identifier)
        ));
    }

    @Override
    public synchronized List<DomainEvent> pendingEvents(MerchantScope merchantScope) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        return state.pendingEvents().entrySet().stream()
                .filter(entry -> entry.getKey().merchantScope().equals(merchantScope))
                .map(Map.Entry::getValue)
                .toList();
    }

    @Override
    public synchronized void recordPublished(
            MerchantScope merchantScope,
            String eventIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(eventIdentifier, "eventIdentifier");
        ScopedIdentifier key = new ScopedIdentifier(merchantScope, eventIdentifier);
        if (!state.pendingEvents().containsKey(key)) {
            return;
        }
        Map<ScopedIdentifier, DomainEvent> remaining =
                new LinkedHashMap<>(state.pendingEvents());
        remaining.remove(key);
        state = new State(
                state.bookings(),
                state.claims(),
                Map.copyOf(remaining),
                state.handledCommands()
        );
    }

    private static Optional<AllocationClaim> findConflict(
            List<ScopedAllocationClaim> claims,
            MerchantScope merchantScope,
            AllocationScope scope
    ) {
        TimeWindowAllocationScope candidate = requireTimeWindowScope(scope);
        return claims.stream()
                .filter(existing -> existing.merchantScope().equals(merchantScope))
                .map(ScopedAllocationClaim::claim)
                .filter(existing -> requireTimeWindowScope(existing.scope())
                        .overlaps(candidate))
                .findFirst();
    }

    private static TimeWindowAllocationScope requireTimeWindowScope(
            AllocationScope scope
    ) {
        Objects.requireNonNull(scope, "scope");
        if (!(scope instanceof TimeWindowAllocationScope timeWindow)) {
            throw new IllegalArgumentException(
                    "Booking unit of work requires a time-window allocation scope"
            );
        }
        return timeWindow;
    }

    private record State(
            Map<ScopedIdentifier, Booking> bookings,
            List<ScopedAllocationClaim> claims,
            Map<ScopedIdentifier, DomainEvent> pendingEvents,
            Map<ScopedIdentifier, HandledCommand> handledCommands
    ) {
        private static State empty() {
            return new State(Map.of(), List.of(), Map.of(), Map.of());
        }
    }

    private record HandledCommand(
            ConfirmBookingCommand command,
            BookingConfirmation confirmation
    ) {
    }

    private record ScopedIdentifier(
            MerchantScope merchantScope,
            String identifier
    ) {
        private ScopedIdentifier {
            Objects.requireNonNull(merchantScope, "merchantScope");
            Objects.requireNonNull(identifier, "identifier");
        }
    }

    private record ScopedAllocationClaim(
            MerchantScope merchantScope,
            AllocationClaim claim
    ) {
        private ScopedAllocationClaim {
            Objects.requireNonNull(merchantScope, "merchantScope");
            Objects.requireNonNull(claim, "claim");
        }
    }

    private static final class StagedTransaction implements BookingTransaction {
        private final MerchantScope merchantScope;
        private final Map<ScopedIdentifier, Booking> bookings;
        private final List<ScopedAllocationClaim> claims;
        private final Map<ScopedIdentifier, DomainEvent> pendingEvents;
        private final Map<ScopedIdentifier, HandledCommand> handledCommands;

        private StagedTransaction(State state, MerchantScope merchantScope) {
            this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
            bookings = new LinkedHashMap<>(state.bookings());
            claims = new ArrayList<>(state.claims());
            pendingEvents = new LinkedHashMap<>(state.pendingEvents());
            handledCommands = new LinkedHashMap<>(state.handledCommands());
        }

        @Override
        public MerchantScope merchantScope() {
            return merchantScope;
        }

        @Override
        public AllocationClaim claim(
                String identifier,
                AllocationScope scope,
                String useIdentifier,
                Instant claimedAt
        ) {
            TimeWindowAllocationScope timeWindow = requireTimeWindowScope(scope);
            Optional<AllocationClaim> conflict = findConflict(
                    claims,
                    merchantScope,
                    timeWindow
            );
            if (conflict.isPresent()) {
                throw new AllocationConflictException(conflict.orElseThrow());
            }
            boolean duplicateIdentifier = claims.stream()
                    .filter(existing -> existing.merchantScope().equals(merchantScope))
                    .map(ScopedAllocationClaim::claim)
                    .anyMatch(existing -> existing.identifier().equals(identifier));
            if (duplicateIdentifier) {
                throw new IllegalArgumentException(
                        "Allocation claim identifier already used: " + identifier
                );
            }
            AllocationClaim claim = new AllocationClaim(
                    identifier,
                    timeWindow,
                    useIdentifier,
                    claimedAt
            );
            claims.add(new ScopedAllocationClaim(merchantScope, claim));
            return claim;
        }

        @Override
        public void recordBooking(Booking booking) {
            Objects.requireNonNull(booking, "booking");
            if (!booking.merchantScope().equals(merchantScope)) {
                throw new IllegalArgumentException(
                        "Booking belongs to another merchant scope"
                );
            }
            if (bookings.putIfAbsent(
                    new ScopedIdentifier(merchantScope, booking.identifier()),
                    booking
            ) != null) {
                throw new IllegalArgumentException(
                        "Booking identifier already used: " + booking.identifier()
                );
            }
        }

        @Override
        public void appendPendingEvent(DomainEvent pendingEvent) {
            Objects.requireNonNull(pendingEvent, "pendingEvent");
            if (pendingEvents.putIfAbsent(
                    new ScopedIdentifier(merchantScope, pendingEvent.identifier()),
                    pendingEvent
            ) != null) {
                throw new IllegalArgumentException(
                        "Pending event identifier already used: "
                                + pendingEvent.identifier()
                );
            }
        }

        private State commit(
                ConfirmBookingCommand command,
                BookingConfirmation confirmation
        ) {
            if (!command.merchantScope().equals(merchantScope)
                    || !confirmation.booking().merchantScope().equals(merchantScope)) {
                throw new IllegalArgumentException(
                        "Handled booking result belongs to another merchant scope"
                );
            }
            validateAuthoritativeFacts(command, confirmation);
            handledCommands.put(
                    new ScopedIdentifier(merchantScope, command.identifier()),
                    new HandledCommand(command, confirmation)
            );
            return new State(
                    Map.copyOf(bookings),
                    List.copyOf(claims),
                    Map.copyOf(pendingEvents),
                    Map.copyOf(handledCommands)
            );
        }

        private void validateAuthoritativeFacts(
                ConfirmBookingCommand command,
                BookingConfirmation confirmation
        ) {
            Booking booking = confirmation.booking();
            if (!command.bookingIdentifier().equals(booking.identifier())
                    || !command.customerContextIdentifier().equals(
                    booking.customerContextIdentifier()
            ) || !command.bookedSubjectReference().equals(
                    booking.bookedSubjectReference()
            ) || !command.reservationWindow().equals(
                    booking.reservationWindow()
            )) {
                throw new IllegalArgumentException(
                        "Confirmation does not match booking command"
                );
            }
            if (!booking.equals(bookings.get(
                    new ScopedIdentifier(merchantScope, booking.identifier())
            ))) {
                throw new IllegalStateException("Booking fact was not staged");
            }
            boolean claimStaged = claims.stream().anyMatch(scoped ->
                    scoped.merchantScope().equals(merchantScope)
                            && scoped.claim().equals(confirmation.allocationClaim())
            );
            DomainEvent event = confirmation.pendingEvent();
            boolean eventStaged = event.equals(pendingEvents.get(
                    new ScopedIdentifier(merchantScope, event.identifier())
            ));
            if (!claimStaged || !eventStaged) {
                throw new IllegalStateException(
                        "Booking confirmation consequences were not staged"
                );
            }
        }
    }
}
