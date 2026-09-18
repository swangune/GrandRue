package grandrue.scheduling;

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

public final class InMemoryAppointmentUnitOfWork implements AppointmentUnitOfWork {

    private State state = State.empty();

    @Override
    public synchronized AppointmentConfirmation execute(
            ConfirmAppointmentCommand command,
            Function<AppointmentTransaction, AppointmentConfirmation> work
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(work, "work");
        ScopedIdentifier key = new ScopedIdentifier(
                command.merchantScope(),
                command.identifier()
        );
        HandledCommand handled = state.handledCommands().get(key);
        if (handled != null) {
            if (!handled.command().equals(command)) {
                throw new AppointmentCommandIdentityConflictException(command.identifier());
            }
            return handled.confirmation();
        }

        StagedTransaction transaction =
                new StagedTransaction(state, command.merchantScope());
        AppointmentConfirmation result = Objects.requireNonNull(
                work.apply(transaction),
                "Appointment work returned no confirmation"
        );
        state = transaction.commit(command, result);
        return result;
    }

    public synchronized Optional<Appointment> appointment(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(state.appointments().get(
                new ScopedIdentifier(merchantScope, identifier)
        ));
    }

    public synchronized Optional<AllocationClaim> conflictingClaim(
            MerchantScope merchantScope,
            AllocationScope scope
    ) {
        return findConflict(state.claims(), merchantScope, scope);
    }

    public synchronized Optional<DomainEvent> pendingEvent(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(state.pendingEvents().get(
                new ScopedIdentifier(merchantScope, identifier)
        ));
    }

    private static Optional<AllocationClaim> findConflict(
            List<ScopedAllocationClaim> claims,
            MerchantScope merchantScope,
            AllocationScope scope
    ) {
        TimeWindowAllocationScope candidate = requireTimeWindow(scope);
        return claims.stream()
                .filter(existing -> existing.merchantScope().equals(merchantScope))
                .map(ScopedAllocationClaim::claim)
                .filter(existing -> requireTimeWindow(existing.scope())
                        .overlaps(candidate))
                .findFirst();
    }

    private static TimeWindowAllocationScope requireTimeWindow(
            AllocationScope scope
    ) {
        Objects.requireNonNull(scope, "scope");
        if (!(scope instanceof TimeWindowAllocationScope timeWindow)) {
            throw new IllegalArgumentException(
                    "Appointment unit of work requires a time-window allocation scope"
            );
        }
        return timeWindow;
    }

    private record State(
            Map<ScopedIdentifier, Appointment> appointments,
            List<ScopedAllocationClaim> claims,
            Map<ScopedIdentifier, DomainEvent> pendingEvents,
            Map<ScopedIdentifier, HandledCommand> handledCommands
    ) {
        private static State empty() {
            return new State(Map.of(), List.of(), Map.of(), Map.of());
        }
    }

    private record HandledCommand(
            ConfirmAppointmentCommand command,
            AppointmentConfirmation confirmation
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
    }

    private static final class StagedTransaction
            implements AppointmentTransaction {
        private final MerchantScope merchantScope;
        private final Map<ScopedIdentifier, Appointment> appointments;
        private final List<ScopedAllocationClaim> claims;
        private final Map<ScopedIdentifier, DomainEvent> pendingEvents;
        private final Map<ScopedIdentifier, HandledCommand> handledCommands;

        private StagedTransaction(State state, MerchantScope merchantScope) {
            this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
            appointments = new LinkedHashMap<>(state.appointments());
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
            TimeWindowAllocationScope timeWindow = requireTimeWindow(scope);
            Optional<AllocationClaim> conflict = findConflict(
                    claims,
                    merchantScope,
                    timeWindow
            );
            if (conflict.isPresent()) {
                throw new AllocationConflictException(conflict.orElseThrow());
            }
            AllocationClaim claim = new AllocationClaim(
                    identifier,
                    timeWindow,
                    useIdentifier,
                    claimedAt
            );
            boolean duplicate = claims.stream()
                    .filter(existing -> existing.merchantScope().equals(merchantScope))
                    .anyMatch(existing -> existing.claim().identifier().equals(identifier));
            if (duplicate) {
                throw new IllegalArgumentException(
                        "Allocation claim identifier already used: " + identifier
                );
            }
            claims.add(new ScopedAllocationClaim(merchantScope, claim));
            return claim;
        }

        @Override
        public void recordAppointment(Appointment appointment) {
            Objects.requireNonNull(appointment, "appointment");
            if (!appointment.merchantScope().equals(merchantScope)) {
                throw new IllegalArgumentException(
                        "Appointment belongs to another merchant scope"
                );
            }
            if (appointments.putIfAbsent(
                    new ScopedIdentifier(merchantScope, appointment.identifier()),
                    appointment
            ) != null) {
                throw new IllegalArgumentException(
                        "Appointment identifier already used: " + appointment.identifier()
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
                ConfirmAppointmentCommand command,
                AppointmentConfirmation confirmation
        ) {
            Appointment appointment = confirmation.appointment();
            if (!command.merchantScope().equals(merchantScope)
                    || !appointment.merchantScope().equals(merchantScope)
                    || !command.appointmentIdentifier().equals(appointment.identifier())
                    || !command.customerContextIdentifier().equals(
                    appointment.customerContextIdentifier()
            ) || !command.scheduledOperationIdentifier().equals(
                    appointment.scheduledOperationIdentifier()
            ) || !command.scheduledInterval().equals(
                    appointment.scheduledInterval()
            )) {
                throw new IllegalArgumentException(
                        "Confirmation does not match appointment command"
                );
            }
            if (!appointment.equals(appointments.get(
                    new ScopedIdentifier(merchantScope, appointment.identifier())
            ))) {
                throw new IllegalStateException("Appointment fact was not staged");
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
                        "Appointment confirmation consequences were not staged"
                );
            }
            handledCommands.put(
                    new ScopedIdentifier(merchantScope, command.identifier()),
                    new HandledCommand(command, confirmation)
            );
            return new State(
                    Map.copyOf(appointments),
                    List.copyOf(claims),
                    Map.copyOf(pendingEvents),
                    Map.copyOf(handledCommands)
            );
        }
    }
}
