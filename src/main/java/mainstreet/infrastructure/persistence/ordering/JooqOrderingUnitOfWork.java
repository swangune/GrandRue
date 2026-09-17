package mainstreet.infrastructure.persistence.ordering;

import grandrue.ordering.OrderingTransaction;
import mainstreet.application.MerchantScope;
import mainstreet.infrastructure.persistence.inventory.JooqQuantityAllocationAuthority;
import grandrue.money.CurrencyIdentity;
import grandrue.money.MonetaryAmount;
import grandrue.ordering.CommitOrderCommand;
import grandrue.ordering.CommittedQuantity;
import grandrue.ordering.Order;
import grandrue.ordering.OrderCommandIdentityConflictException;
import grandrue.ordering.OrderConfirmation;
import grandrue.ordering.OrderCommitmentPortion;
import grandrue.ordering.OrderingUnitOfWork;
import grandrue.ordering.RequestedOrderPortion;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.QuantityAllocationScope;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/** Durable PostgreSQL Ordering unit-of-work and idempotency boundary. */
public final class JooqOrderingUnitOfWork implements OrderingUnitOfWork {

    private static final Table<?> ORDER = DSL.table(DSL.name("ordering_order"));
    private static final Table<?> PORTION = DSL.table(DSL.name("ordering_order_portion"));
    private static final Table<?> OUTBOX = DSL.table(DSL.name("ordering_outbox"));
    private static final Table<?> HANDLED = DSL.table(DSL.name("ordering_handled_command"));
    private static final Table<?> HANDLED_REQUEST =
            DSL.table(DSL.name("ordering_handled_requested_portion"));
    private static final Table<?> HANDLED_CLAIM =
            DSL.table(DSL.name("ordering_handled_claim"));
    private static final Table<?> INVENTORY_CLAIM =
            DSL.table(DSL.name("inventory_quantity_claim"));

    private static final Field<String> MERCHANT_IDENTIFIER = field("merchant_identifier", String.class);
    private static final Field<String> COMMAND_IDENTIFIER = field("command_identifier", String.class);
    private static final Field<String> ORDER_IDENTIFIER = field("order_identifier", String.class);
    private static final Field<String> CUSTOMER_CONTEXT_IDENTIFIER = field("customer_context_identifier", String.class);
    private static final Field<String> GOVERNING_RELEASE_IDENTIFIER = field("governing_release_identifier", String.class);
    private static final Field<Instant> COMMITTED_AT = field("committed_at", Instant.class);
    private static final Field<String> PORTION_IDENTIFIER = field("portion_identifier", String.class);
    private static final Field<Integer> PORTION_ORDINAL = field("portion_ordinal", Integer.class);
    private static final Field<String> COMMITTED_SUBJECT_REFERENCE = field("committed_subject_reference", String.class);
    private static final Field<String> SUBJECT_REFERENCE = field("subject_reference", String.class);
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
    private static final Field<String> CLAIM_IDENTIFIER = field("claim_identifier", String.class);
    private static final Field<Integer> CLAIM_ORDINAL = field("claim_ordinal", Integer.class);
    private static final Field<Long> QUANTITY = field("quantity", Long.class);
    private static final Field<String> USE_IDENTIFIER = field("use_identifier", String.class);
    private static final Field<Instant> CLAIMED_AT = field("claimed_at", Instant.class);

    private static final Field<String> HANDLED_CLAIM_MERCHANT_IDENTIFIER =
            qualifiedField("ordering_handled_claim", "merchant_identifier", String.class);
    private static final Field<String> HANDLED_CLAIM_COMMAND_IDENTIFIER =
            qualifiedField("ordering_handled_claim", "command_identifier", String.class);
    private static final Field<String> HANDLED_CLAIM_CLAIM_IDENTIFIER =
            qualifiedField("ordering_handled_claim", "claim_identifier", String.class);
    private static final Field<Integer> HANDLED_CLAIM_ORDINAL =
            qualifiedField("ordering_handled_claim", "claim_ordinal", Integer.class);
    private static final Field<String> INVENTORY_CLAIM_MERCHANT_IDENTIFIER =
            qualifiedField("inventory_quantity_claim", "merchant_identifier", String.class);
    private static final Field<String> INVENTORY_CLAIM_CLAIM_IDENTIFIER =
            qualifiedField("inventory_quantity_claim", "claim_identifier", String.class);
    private static final Field<String> INVENTORY_CLAIM_SUBJECT_IDENTIFIER =
            qualifiedField("inventory_quantity_claim", "subject_identifier", String.class);
    private static final Field<Long> INVENTORY_CLAIM_QUANTITY =
            qualifiedField("inventory_quantity_claim", "quantity", Long.class);
    private static final Field<String> INVENTORY_CLAIM_USE_IDENTIFIER =
            qualifiedField("inventory_quantity_claim", "use_identifier", String.class);
    private static final Field<Instant> INVENTORY_CLAIM_CLAIMED_AT =
            qualifiedField("inventory_quantity_claim", "claimed_at", Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqOrderingUnitOfWork(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public OrderConfirmation execute(
            CommitOrderCommand command,
           Function<OrderingTransaction, OrderConfirmation> work
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(work, "work");
        return Objects.requireNonNull(
                transactionTemplate.execute(status -> {
                    lockLogicalCommand(
                            command.merchantScope(),
                            command.identifier()
                    );
                    Optional<HandledCommand> handled = handledCommand(
                            command.merchantScope(),
                            command.identifier()
                    );
                    if (handled.isPresent()) {
                        HandledCommand existing = handled.orElseThrow();
                        requireSameCommand(existing.command(), command);
                        return loadConfirmation(existing);
                    }

                    JooqOrderingTransaction transaction =
                            new JooqOrderingTransaction(
                                    dsl,
                                    command.merchantScope()
                            );
                    OrderConfirmation produced = Objects.requireNonNull(
                            work.apply(transaction),
                            "Ordering work returned no confirmation"
                    );
                    transaction.recordHandled(command, produced);
                    return produced;
                }),
                "Ordering transaction returned no confirmation"
        );
    }

    public Optional<Order> order(
            MerchantScope merchantScope,
            String identifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identifier, "Order identifier");
        Record row = dsl.select(
                        CUSTOMER_CONTEXT_IDENTIFIER,
                        GOVERNING_RELEASE_IDENTIFIER,
                        COMMITTED_AT
                )
                .from(ORDER)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(ORDER_IDENTIFIER.eq(identifier))
                .fetchOne();
        if (row == null) {
            return Optional.empty();
        }
        List<OrderCommitmentPortion> portions = dsl.select(
                        PORTION_IDENTIFIER,
                        COMMITTED_SUBJECT_REFERENCE,
                        QUANTITY_MAGNITUDE,
                        QUANTITY_UNIT_IDENTIFIER,
                        CURRENCY_IDENTIFIER,
                        COMMITTED_UNIT_MINOR_AMOUNT,
                        COMMERCIAL_TERMS_PROVENANCE_REFERENCE
                )
                .from(PORTION)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(ORDER_IDENTIFIER.eq(identifier))
                .orderBy(PORTION_ORDINAL)
                .fetch(portionRow -> new OrderCommitmentPortion(
                        portionRow.get(PORTION_IDENTIFIER),
                        portionRow.get(COMMITTED_SUBJECT_REFERENCE),
                        new CommittedQuantity(
                                portionRow.get(QUANTITY_MAGNITUDE),
                                portionRow.get(QUANTITY_UNIT_IDENTIFIER)
                        ),
                        new MonetaryAmount(
                                new CurrencyIdentity(
                                        portionRow.get(CURRENCY_IDENTIFIER)
                                ),
                                portionRow.get(COMMITTED_UNIT_MINOR_AMOUNT)
                                        .toBigIntegerExact()
                        ),
                        portionRow.get(COMMERCIAL_TERMS_PROVENANCE_REFERENCE)
                ));
        return Optional.of(new Order(
                merchantScope,
                identifier,
                Optional.ofNullable(row.get(CUSTOMER_CONTEXT_IDENTIFIER)),
                portions,
                row.get(GOVERNING_RELEASE_IDENTIFIER),
                row.get(COMMITTED_AT)
        ));
    }

    public List<AllocationClaim> inventoryClaims(
            MerchantScope merchantScope,
            String orderIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(orderIdentifier, "Order identifier");
        Record handled = dsl.select(COMMAND_IDENTIFIER)
                .from(HANDLED)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(ORDER_IDENTIFIER.eq(orderIdentifier))
                .fetchOne();
        if (handled == null) {
            return List.of();
        }
        String commandIdentifier = handled.get(COMMAND_IDENTIFIER);
        return dsl.select(
                        INVENTORY_CLAIM_CLAIM_IDENTIFIER,
                        INVENTORY_CLAIM_SUBJECT_IDENTIFIER,
                        INVENTORY_CLAIM_QUANTITY,
                        INVENTORY_CLAIM_USE_IDENTIFIER,
                        INVENTORY_CLAIM_CLAIMED_AT
                )
                .from(HANDLED_CLAIM)
                .join(INVENTORY_CLAIM)
                .on(HANDLED_CLAIM_MERCHANT_IDENTIFIER
                        .eq(INVENTORY_CLAIM_MERCHANT_IDENTIFIER))
                .and(HANDLED_CLAIM_CLAIM_IDENTIFIER
                        .eq(INVENTORY_CLAIM_CLAIM_IDENTIFIER))
                .where(HANDLED_CLAIM_MERCHANT_IDENTIFIER
                        .eq(merchantScope.merchantIdentifier()))
                .and(HANDLED_CLAIM_COMMAND_IDENTIFIER
                        .eq(commandIdentifier))
                .orderBy(HANDLED_CLAIM_ORDINAL)
                .fetch(row -> new AllocationClaim(
                        row.get(INVENTORY_CLAIM_CLAIM_IDENTIFIER),
                        new QuantityAllocationScope(
                                row.get(INVENTORY_CLAIM_SUBJECT_IDENTIFIER),
                                row.get(INVENTORY_CLAIM_QUANTITY)
                        ),
                        row.get(INVENTORY_CLAIM_USE_IDENTIFIER),
                        row.get(INVENTORY_CLAIM_CLAIMED_AT)
                ));
    }

    public List<DomainEvent> pendingEvents(MerchantScope merchantScope) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        return dsl.select(
                        EVENT_IDENTIFIER,
                        FACT_IDENTIFIER,
                        SUBJECT_IDENTIFIER,
                        CAUSATION_IDENTIFIER,
                        OCCURRED_AT
                )
                .from(OUTBOX)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(ACKNOWLEDGED_AT.isNull())
                .orderBy(OCCURRED_AT, EVENT_IDENTIFIER)
                .fetch(this::event);
    }

    public long availableToPromise(
            MerchantScope merchantScope,
            String subjectIdentifier
    ) {
        return new JooqQuantityAllocationAuthority(dsl, merchantScope)
                .availableToPromise(subjectIdentifier);
    }

    private Optional<HandledCommand> handledCommand(
            MerchantScope merchantScope,
            String commandIdentifier
    ) {
        Record row = dsl.select(
                        ORDER_IDENTIFIER,
                        CUSTOMER_CONTEXT_IDENTIFIER,
                        EVENT_IDENTIFIER
                )
                .from(HANDLED)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(COMMAND_IDENTIFIER.eq(commandIdentifier))
                .fetchOne();
        if (row == null) {
            return Optional.empty();
        }
        List<RequestedOrderPortion> requested = dsl.select(
                        PORTION_IDENTIFIER,
                        SUBJECT_REFERENCE,
                        QUANTITY_MAGNITUDE,
                        QUANTITY_UNIT_IDENTIFIER
                )
                .from(HANDLED_REQUEST)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(COMMAND_IDENTIFIER.eq(commandIdentifier))
                .orderBy(PORTION_ORDINAL)
                .fetch(portionRow -> new RequestedOrderPortion(
                        portionRow.get(PORTION_IDENTIFIER),
                        portionRow.get(SUBJECT_REFERENCE),
                        new CommittedQuantity(
                                portionRow.get(QUANTITY_MAGNITUDE),
                                portionRow.get(QUANTITY_UNIT_IDENTIFIER)
                        )
                ));
        return Optional.of(new HandledCommand(
                new CommitOrderCommand(
                        merchantScope,
                        commandIdentifier,
                        row.get(ORDER_IDENTIFIER),
                        Optional.ofNullable(row.get(CUSTOMER_CONTEXT_IDENTIFIER)),
                        requested
                ),
                row.get(EVENT_IDENTIFIER)
        ));
    }

    private OrderConfirmation loadConfirmation(HandledCommand handled) {
        Order order = order(
                handled.command().merchantScope(),
                handled.command().orderIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Handled CommitOrder has no Order"
        ));
        List<AllocationClaim> claims = inventoryClaims(
                handled.command().merchantScope(),
                handled.command().orderIdentifier()
        );
        DomainEvent event = outboxEvent(
                handled.command().merchantScope(),
                handled.eventIdentifier()
        ).orElseThrow(() -> new IllegalStateException(
                "Handled CommitOrder has no outbox event"
        ));
        return new OrderConfirmation(order, claims, event);
    }

    private Optional<DomainEvent> outboxEvent(
            MerchantScope merchantScope,
            String identifier
    ) {
        Record row = dsl.select(
                        EVENT_IDENTIFIER,
                        FACT_IDENTIFIER,
                        SUBJECT_IDENTIFIER,
                        CAUSATION_IDENTIFIER,
                        OCCURRED_AT
                )
                .from(OUTBOX)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(EVENT_IDENTIFIER.eq(identifier))
                .fetchOne();
        return Optional.ofNullable(row).map(this::event);
    }

    private DomainEvent event(Record row) {
        return new DomainEvent(
                row.get(EVENT_IDENTIFIER),
                row.get(FACT_IDENTIFIER),
                row.get(SUBJECT_IDENTIFIER),
                row.get(CAUSATION_IDENTIFIER),
                row.get(OCCURRED_AT)
        );
    }

    private void lockLogicalCommand(
            MerchantScope merchantScope,
            String commandIdentifier
    ) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                merchantScope.merchantIdentifier()
                        + "|ordering-command|"
                        + commandIdentifier
        );
    }

    private static void requireSameCommand(
            CommitOrderCommand committed,
            CommitOrderCommand candidate
    ) {
        if (!committed.equals(candidate)) {
            throw new OrderCommandIdentityConflictException(
                    candidate.identifier()
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private static <T> Field<T> field(String name, Class<T> type) {
        return DSL.field(DSL.name(name), type);
    }

    private static <T> Field<T> qualifiedField(
            String tableName,
            String fieldName,
            Class<T> type
    ) {
        return DSL.field(DSL.name(tableName, fieldName), type);
    }

    private record HandledCommand(
            CommitOrderCommand command,
            String eventIdentifier
    ) { }
}
