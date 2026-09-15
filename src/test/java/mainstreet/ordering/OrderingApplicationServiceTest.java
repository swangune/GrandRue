package mainstreet.ordering;

import mainstreet.application.MerchantScope;
import mainstreet.customer.CustomerContext;
import mainstreet.customer.InMemoryCustomerContextAuthority;
import mainstreet.inventory.InsufficientQuantityException;
import mainstreet.money.CurrencyIdentity;
import mainstreet.money.MonetaryAmount;
import mainstreet.runtime.OperationExecutionGuard;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.QuantityAllocationScope;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderingApplicationServiceTest {

    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");
    private static final Instant COMMITTED_AT =
            Instant.parse("2026-08-26T18:30:00Z");

    @Test
    void commit_atomically_records_order_required_inventory_claim_and_event() {
        InMemoryOrderingUnitOfWork unitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 1L));
        OrderingApplicationService service = service(unitOfWork);

        OrderConfirmation confirmation = service.commit(
                command("command-1", "order-1", "sku-1", Optional.of("customer-1")),
                "semantic-release-8"
        );

        Order order = confirmation.order();
        assertSame(
                order,
                unitOfWork.order(MERCHANT, "order-1").orElseThrow()
        );
        assertEquals("semantic-release-8", order.governingReleaseIdentifier());
        assertEquals(COMMITTED_AT, order.committedAt());
        assertEquals("customer-1", order.customerContextIdentifier().orElseThrow());
        assertEquals(1, order.commitmentPortions().size());

        OrderCommitmentPortion portion = order.commitmentPortions().getFirst();
        assertEquals("portion-1", portion.identifier());
        assertEquals("sku-1", portion.committedSubjectReference());
        assertEquals(new CommittedQuantity(new BigDecimal("1"), "EACH"), portion.quantity());
        assertEquals(money(2500), portion.committedUnitAmount());
        assertEquals("offering:sku-1@revision-3", portion.commercialTermsProvenanceReference());

        assertEquals(1, confirmation.inventoryClaims().size());
        AllocationClaim claim = confirmation.inventoryClaims().getFirst();
        assertSame(
                claim,
                unitOfWork.inventoryClaim(claim.identifier()).orElseThrow()
        );
        assertEquals(0L, unitOfWork.availableToPromise("sku-1"));

        DomainEvent event = confirmation.pendingEvent();
        assertEquals("order.committed", event.factIdentifier());
        assertSame(
                event,
                unitOfWork.pendingEvent(MERCHANT, event.identifier()).orElseThrow()
        );
    }

    @Test
    void insufficient_required_inventory_commits_no_order_or_event() {
        InMemoryOrderingUnitOfWork unitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 1L));
        OrderingApplicationService service = service(unitOfWork);

        service.commit(
                command("command-1", "order-1", "sku-1", Optional.empty()),
                "semantic-release-8"
        );

        CommitOrderCommand second = command(
                "command-2",
                "order-2",
                "sku-1",
                Optional.empty()
        );

        assertThrows(
                InsufficientQuantityException.class,
                () -> service.commit(second, "semantic-release-8")
        );

        assertTrue(unitOfWork.order(MERCHANT, "order-2").isEmpty());
        assertTrue(unitOfWork.pendingEvent(
                MERCHANT,
                "command-2:order-committed"
        ).isEmpty());
        assertEquals(0L, unitOfWork.availableToPromise("sku-1"));
    }

    @Test
    void failed_local_transaction_discards_staged_inventory_order_and_event() {
        InMemoryOrderingUnitOfWork unitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 1L));
        CommitOrderCommand command = command(
                "command-1",
                "order-1",
                "sku-1",
                Optional.empty()
        );
        Order order = new Order(
                MERCHANT,
                "order-1",
                Optional.empty(),
                List.of(resolvedPortion("sku-1")),
                "semantic-release-8",
                COMMITTED_AT
        );

        assertThrows(
                RuntimeException.class,
                () -> unitOfWork.execute(command, transaction -> {
                    AllocationClaim claim = transaction.claim(
                            "claim-1",
                            new QuantityAllocationScope("sku-1", 1L),
                            "portion-1",
                            COMMITTED_AT
                    );
                    transaction.recordOrder(order);
                    transaction.appendPendingEvent(new DomainEvent(
                            "command-1:order-committed",
                            "order.committed",
                            order.identifier(),
                            command.identifier(),
                            COMMITTED_AT
                    ));
                    throw new RuntimeException(claim.identifier());
                })
        );

        assertTrue(unitOfWork.order(MERCHANT, "order-1").isEmpty());
        assertTrue(unitOfWork.inventoryClaim("claim-1").isEmpty());
        assertTrue(unitOfWork.pendingEvent(
                MERCHANT,
                "command-1:order-committed"
        ).isEmpty());
        assertEquals(1L, unitOfWork.availableToPromise("sku-1"));
    }

    @Test
    void duplicate_logical_command_replays_original_confirmation() {
        InMemoryOrderingUnitOfWork unitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 2L));
        OrderingApplicationService service = service(unitOfWork);
        CommitOrderCommand command = command(
                "command-1",
                "order-1",
                "sku-1",
                Optional.empty()
        );

        OrderConfirmation first = service.commit(command, "semantic-release-8");
        OrderConfirmation duplicate = service.commit(command, "semantic-release-8");

        assertSame(first, duplicate);
        assertEquals(1L, unitOfWork.availableToPromise("sku-1"));
    }

    @Test
    void command_identifier_cannot_be_reused_for_different_order_intent() {
        InMemoryOrderingUnitOfWork unitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 2L));
        OrderingApplicationService service = service(unitOfWork);

        service.commit(
                command("command-1", "order-1", "sku-1", Optional.empty()),
                "semantic-release-8"
        );

        CommitOrderCommand reused = command(
                "command-1",
                "order-2",
                "sku-1",
                Optional.empty()
        );

        assertThrows(
                OrderCommandIdentityConflictException.class,
                () -> service.commit(reused, "semantic-release-8")
        );
        assertTrue(unitOfWork.order(MERCHANT, "order-2").isEmpty());
        assertEquals(1L, unitOfWork.availableToPromise("sku-1"));
    }

    @Test
    void identical_order_and_command_identifiers_are_isolated_by_merchant() {
        InMemoryOrderingUnitOfWork firstUnitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 1L));
        InMemoryOrderingUnitOfWork secondUnitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 1L));
        OrderingApplicationService firstService = service(firstUnitOfWork);
        OrderingApplicationService secondService = service(secondUnitOfWork);
        MerchantScope secondMerchant = new MerchantScope("merchant-b");

        OrderConfirmation first = firstService.commit(
                command("command-1", "order-1", "sku-1", Optional.empty()),
                "semantic-release-8"
        );
        OrderConfirmation second = secondService.commit(
                new CommitOrderCommand(
                        secondMerchant,
                        "command-1",
                        "order-1",
                        Optional.empty(),
                        List.of(new RequestedOrderPortion(
                                "portion-1",
                                "sku-1",
                                new CommittedQuantity(new BigDecimal("1"), "EACH")
                        ))
                ),
                "semantic-release-8"
        );

        assertEquals(MERCHANT, first.order().merchantScope());
        assertEquals(secondMerchant, second.order().merchantScope());
        assertSame(
                first.order(),
                firstUnitOfWork.order(MERCHANT, "order-1").orElseThrow()
        );
        assertSame(
                second.order(),
                secondUnitOfWork.order(secondMerchant, "order-1").orElseThrow()
        );
    }

    @Test
    void non_stock_order_does_not_require_fake_inventory() {
        InMemoryOrderingUnitOfWork unitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of());
        OrderingApplicationService service = new OrderingApplicationService(
                unitOfWork,
                customerContexts(),
                (merchantScope, requested) -> new ResolvedOrderCommitment(
                        new OrderCommitmentPortion(
                                requested.identifier(),
                                requested.subjectReference(),
                                requested.quantity(),
                                money(5000),
                                "offering:made-to-order@revision-2"
                        ),
                        List.of()
                ),
                OperationExecutionGuard.allowAll(),
                Clock.fixed(COMMITTED_AT, ZoneOffset.UTC)
        );

        OrderConfirmation confirmation = service.commit(
                command(
                        "command-1",
                        "order-1",
                        "made-to-order",
                        Optional.empty()
                ),
                "semantic-release-8"
        );

        assertTrue(confirmation.inventoryClaims().isEmpty());
        assertEquals(1, confirmation.order().commitmentPortions().size());
    }

    @Test
    void caller_does_not_supply_the_committed_price_or_commit_timestamp() {
        InMemoryOrderingUnitOfWork unitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 1L));
        OrderingApplicationService service = service(unitOfWork);

        OrderConfirmation confirmation = service.commit(
                command("command-1", "order-1", "sku-1", Optional.empty()),
                "semantic-release-8"
        );

        assertEquals(
                money(2500),
                confirmation.order().commitmentPortions().getFirst()
                        .committedUnitAmount()
        );
        assertEquals(COMMITTED_AT, confirmation.order().committedAt());
    }

    private static OrderingApplicationService service(
            InMemoryOrderingUnitOfWork unitOfWork
    ) {
        return new OrderingApplicationService(
                unitOfWork,
                customerContexts(),
                OrderingApplicationServiceTest::resolve,
                OperationExecutionGuard.allowAll(),
                Clock.fixed(COMMITTED_AT, ZoneOffset.UTC)
        );
    }

    private static InMemoryCustomerContextAuthority customerContexts() {
        InMemoryCustomerContextAuthority customerContexts =
                new InMemoryCustomerContextAuthority();
        customerContexts.register(new CustomerContext(
                MERCHANT,
                "customer-1",
                COMMITTED_AT.minusSeconds(60)
        ));
        customerContexts.register(new CustomerContext(
                new MerchantScope("merchant-b"),
                "customer-1",
                COMMITTED_AT.minusSeconds(60)
        ));
        return customerContexts;
    }

    private static ResolvedOrderCommitment resolve(
            MerchantScope merchantScope,
            RequestedOrderPortion requested
    ) {
        return new ResolvedOrderCommitment(
                new OrderCommitmentPortion(
                        requested.identifier(),
                        requested.subjectReference(),
                        requested.quantity(),
                        money(2500),
                        "offering:" + requested.subjectReference() + "@revision-3"
                ),
                List.of(new OrderInventoryClaimRequest(
                        "claim:" + requested.identifier(),
                        requested.identifier(),
                        new QuantityAllocationScope(
                                requested.subjectReference(),
                                requested.quantity().magnitude().longValueExact()
                        )
                ))
        );
    }

    private static CommitOrderCommand command(
            String commandIdentifier,
            String orderIdentifier,
            String subjectIdentifier,
            Optional<String> customerContextIdentifier
    ) {
        return new CommitOrderCommand(
                MERCHANT,
                commandIdentifier,
                orderIdentifier,
                customerContextIdentifier,
                List.of(new RequestedOrderPortion(
                        "portion-1",
                        subjectIdentifier,
                        new CommittedQuantity(new BigDecimal("1.0"), "EACH")
                ))
        );
    }

    private static OrderCommitmentPortion resolvedPortion(String subject) {
        return new OrderCommitmentPortion(
                "portion-1",
                subject,
                new CommittedQuantity(BigDecimal.ONE, "EACH"),
                money(2500),
                "offering:" + subject + "@revision-3"
        );
    }

    private static MonetaryAmount money(long minorUnits) {
        return new MonetaryAmount(
                new CurrencyIdentity("GBP"),
                BigInteger.valueOf(minorUnits)
        );
    }
}
