package mainstreet.infrastructure.persistence.ordering;

import mainstreet.application.MerchantScope;
import mainstreet.infrastructure.persistence.inventory.JooqQuantityAllocationAuthority;
import mainstreet.ordering.CommitOrderCommand;
import mainstreet.ordering.Order;
import mainstreet.ordering.OrderConfirmation;
import mainstreet.ordering.OrderCommitmentPortion;
import mainstreet.ordering.OrderingPersistenceException;
import mainstreet.ordering.OrderingTransaction;
import mainstreet.ordering.RequestedOrderPortion;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.AllocationScope;
import mainstreet.semantic.DomainEvent;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/** PostgreSQL/jOOQ mutation adapter for one local Ordering consistency boundary. */
public final class JooqOrderingTransaction implements OrderingTransaction {

    private static final Table<?> ORDER = DSL.table(DSL.name("ordering_order"));
    private static final Table<?> PORTION = DSL.table(DSL.name("ordering_order_portion"));
    private static final Table<?> OUTBOX = DSL.table(DSL.name("ordering_outbox"));
    private static final Table<?> HANDLED = DSL.table(DSL.name("ordering_handled_command"));
    private static final Table<?> HANDLED_REQUEST =
            DSL.table(DSL.name("ordering_handled_requested_portion"));
    private static final Table<?> HANDLED_CLAIM =
            DSL.table(DSL.name("ordering_handled_claim"));

    private static final Field<String> MERCHANT_IDENTIFIER = field("merchant_identifier", String.class);
    private static final Field<String> ORDER_IDENTIFIER = field("order_identifier", String.class);
    private static final Field<String> CUSTOMER_CONTEXT_IDENTIFIER = field("customer_context_identifier", String.class);
    private static final Field<String> GOVERNING_RELEASE_IDENTIFIER = field("governing_release_identifier", String.class);
    private static final Field<Instant> COMMITTED_AT = field("committed_at", Instant.class);
    private static final Field<String> PORTION_IDENTIFIER = field("portion_identifier", String.class);
    private static final Field<Integer> PORTION_ORDINAL = field("portion_ordinal", Integer.class);
    private static final Field<String> COMMITTED_SUBJECT_REFERENCE = field("committed_subject_reference", String.class);
    private static final Field<BigDecimal> QUANTITY_MAGNITUDE = field("quantity_magnitude", BigDecimal.class);
    private static final Field<String> QUANTITY_UNIT_IDENTIFIER = field("quantity_unit_identifier", String.class);
    private static final Field<String> CURRENCY_IDENTIFIER = field("currency_identifier", String.class);
    private static final Field<BigDecimal> COMMITTED_UNIT_MINOR_AMOUNT = field("committed_unit_minor_amount", BigDecimal.class);
    private static final Field<String> COMMERCIAL_TERMS_PROVENANCE_REFERENCE = field("commercial_terms_provenance_reference", String.class);
    private static final Field<String> EVENT_IDENTIFIER = field("event_identifier", String.class);
    private static final Field<String> FACT_IDENTIFIER = field("fact_identifier", String.class);
    private static final Field<String> SUBJECT_IDENTIFIER = field("subject_identifier", String.class);
    private static final Field<String> CAUSATION_IDENTIFIER = field("causation_identifier", String.class);
    private static final Field<Instant> OCCURRED_AT = field("occurred_at", Instant.class);
    private static final Field<Instant> ACKNOWLEDGED_AT = field("acknowledged_at", Instant.class);
    private static final Field<String> COMMAND_IDENTIFIER = field("command_identifier", String.class);
    private static final Field<String> SUBJECT_REFERENCE = field("subject_reference", String.class);
    private static final Field<String> CLAIM_IDENTIFIER = field("claim_identifier", String.class);
    private static final Field<Integer> CLAIM_ORDINAL = field("claim_ordinal", Integer.class);

    private final DSLContext dsl;
    private final MerchantScope merchantScope;
    private final JooqQuantityAllocationAuthority inventory;

    public JooqOrderingTransaction(
            DSLContext dsl,
            MerchantScope merchantScope
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        this.inventory = new JooqQuantityAllocationAuthority(dsl, merchantScope);
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
        return inventory.claim(identifier, scope, useIdentifier, claimedAt);
    }

    @Override
    public void recordOrder(Order order) {
        Objects.requireNonNull(order, "order");
        requireMerchant(order.merchantScope(), "Order");
        try {
            dsl.insertInto(ORDER)
                    .columns(
                            MERCHANT_IDENTIFIER,
                            ORDER_IDENTIFIER,
                            CUSTOMER_CONTEXT_IDENTIFIER,
                            GOVERNING_RELEASE_IDENTIFIER,
                            COMMITTED_AT
                    )
                    .values(
                            merchantScope.merchantIdentifier(),
                            order.identifier(),
                            order.customerContextIdentifier().orElse(null),
                            order.governingReleaseIdentifier(),
                            order.committedAt()
                    )
                    .execute();

            int ordinal = 0;
            for (OrderCommitmentPortion portion : order.commitmentPortions()) {
                dsl.insertInto(PORTION)
                        .columns(
                                MERCHANT_IDENTIFIER,
                                ORDER_IDENTIFIER,
                                PORTION_IDENTIFIER,
                                PORTION_ORDINAL,
                                COMMITTED_SUBJECT_REFERENCE,
                                QUANTITY_MAGNITUDE,
                                QUANTITY_UNIT_IDENTIFIER,
                                CURRENCY_IDENTIFIER,
                                COMMITTED_UNIT_MINOR_AMOUNT,
                                COMMERCIAL_TERMS_PROVENANCE_REFERENCE
                        )
                        .values(
                                merchantScope.merchantIdentifier(),
                                order.identifier(),
                                portion.identifier(),
                                ordinal++,
                                portion.committedSubjectReference(),
                                portion.quantity().magnitude(),
                                portion.quantity().unitIdentifier(),
                                portion.committedUnitAmount().currencyIdentity().identifier(),
                                new BigDecimal(portion.committedUnitAmount().minorUnitAmount()),
                                portion.commercialTermsProvenanceReference()
                        )
                        .execute();
            }
        } catch (RuntimeException failure) {
            throw persistenceFailure("Could not record Order commitment", failure);
        }
    }

    @Override
    public void appendPendingEvent(DomainEvent event) {
        Objects.requireNonNull(event, "event");
        try {
            dsl.insertInto(OUTBOX)
                    .columns(
                            MERCHANT_IDENTIFIER,
                            EVENT_IDENTIFIER,
                            FACT_IDENTIFIER,
                            SUBJECT_IDENTIFIER,
                            CAUSATION_IDENTIFIER,
                            OCCURRED_AT,
                            ACKNOWLEDGED_AT
                    )
                    .values(
                            merchantScope.merchantIdentifier(),
                            event.identifier(),
                            event.factIdentifier(),
                            event.subjectIdentifier(),
                            event.causationIdentifier(),
                            event.occurredAt(),
                            null
                    )
                    .execute();
        } catch (RuntimeException failure) {
            throw persistenceFailure("Could not append Ordering event", failure);
        }
    }

    public void recordHandled(
            CommitOrderCommand command,
            OrderConfirmation confirmation
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(confirmation, "confirmation");
        requireMerchant(command.merchantScope(), "Order command");
        requireMerchant(confirmation.order().merchantScope(), "Order confirmation");
        if (!command.orderIdentifier().equals(confirmation.order().identifier())) {
            throw new IllegalArgumentException(
                    "Order confirmation does not match CommitOrder identity"
            );
        }

        try {
            dsl.insertInto(HANDLED)
                    .columns(
                            MERCHANT_IDENTIFIER,
                            COMMAND_IDENTIFIER,
                            ORDER_IDENTIFIER,
                            CUSTOMER_CONTEXT_IDENTIFIER,
                            EVENT_IDENTIFIER
                    )
                    .values(
                            merchantScope.merchantIdentifier(),
                            command.identifier(),
                            command.orderIdentifier(),
                            command.customerContextIdentifier().orElse(null),
                            confirmation.pendingEvent().identifier()
                    )
                    .execute();

            int portionOrdinal = 0;
            for (RequestedOrderPortion requested : command.requestedPortions()) {
                dsl.insertInto(HANDLED_REQUEST)
                        .columns(
                                MERCHANT_IDENTIFIER,
                                COMMAND_IDENTIFIER,
                                PORTION_IDENTIFIER,
                                PORTION_ORDINAL,
                                SUBJECT_REFERENCE,
                                QUANTITY_MAGNITUDE,
                                QUANTITY_UNIT_IDENTIFIER
                        )
                        .values(
                                merchantScope.merchantIdentifier(),
                                command.identifier(),
                                requested.identifier(),
                                portionOrdinal++,
                                requested.subjectReference(),
                                requested.quantity().magnitude(),
                                requested.quantity().unitIdentifier()
                        )
                        .execute();
            }

            int claimOrdinal = 0;
            for (AllocationClaim claim : confirmation.inventoryClaims()) {
                dsl.insertInto(HANDLED_CLAIM)
                        .columns(
                                MERCHANT_IDENTIFIER,
                                COMMAND_IDENTIFIER,
                                CLAIM_IDENTIFIER,
                                CLAIM_ORDINAL
                        )
                        .values(
                                merchantScope.merchantIdentifier(),
                                command.identifier(),
                                claim.identifier(),
                                claimOrdinal++
                        )
                        .execute();
            }
        } catch (RuntimeException failure) {
            throw persistenceFailure(
                    "Could not record handled CommitOrder command",
                    failure
            );
        }
    }

    private void requireMerchant(MerchantScope actual, String label) {
        if (!merchantScope.equals(actual)) {
            throw new IllegalArgumentException(
                    label + " belongs to another merchant scope"
            );
        }
    }

    private static <T> Field<T> field(String name, Class<T> type) {
        return DSL.field(DSL.name(name), type);
    }

    private static OrderingPersistenceException persistenceFailure(
            String message,
            RuntimeException failure
    ) {
        return new OrderingPersistenceException(message, failure);
    }
}
