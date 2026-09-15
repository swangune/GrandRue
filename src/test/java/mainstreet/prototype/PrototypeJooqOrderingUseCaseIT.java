package mainstreet.prototype;

import mainstreet.infrastructure.persistence.ordering.JooqOrderingUnitOfWork;
import mainstreet.ordering.CommittedQuantity;
import mainstreet.ordering.RequestedOrderPortion;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrototypeJooqOrderingUseCaseIT {

    private DataSource dataSource;
    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure().dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        transactionManager = new DataSourceTransactionManager(dataSource);
        dsl.execute("truncate table ordering_handled_claim, ordering_handled_requested_portion, ordering_handled_command, ordering_outbox, ordering_order_portion, ordering_order, inventory_quantity_claim, inventory_stock_position cascade");
        seedStock();
    }

    @Test
    void committed_order_survives_use_case_recreation_and_idempotent_retry() {
        PrototypeOrderUseCase first = useCase();
        List<RequestedOrderPortion> portions = List.of(new RequestedOrderPortion(
                "portion-1",
                "sku-1",
                new CommittedQuantity(BigDecimal.ONE, "EACH")
        ));

        var committed = first.commit(
                "prototype-retailer",
                "request-1",
                "order-1",
                portions
        );

        PrototypeOrderUseCase restarted = useCase();
        var recovered = restarted.order(
                "prototype-retailer",
                "order-1"
        ).orElseThrow();
        var replayed = restarted.commit(
                "prototype-retailer",
                "request-1",
                "order-1",
                portions
        );

        assertEquals(committed, recovered);
        assertEquals(committed, replayed);
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("ordering_order"))));
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("inventory_quantity_claim"))));
        assertEquals(9L, new JooqOrderingUnitOfWork(
                dsl,
                transactionManager
        ).availableToPromise(
                new mainstreet.application.MerchantScope("prototype-retailer"),
                "sku-1"
        ));
    }

    @Test
    void publisher_cannot_use_retail_ordering_path() {
        PrototypeOrderUseCase useCase = useCase();

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.commit(
                        "prototype-publisher",
                        "request-1",
                        "order-1",
                        List.of(new RequestedOrderPortion(
                                "portion-1",
                                "sku-1",
                                new CommittedQuantity(BigDecimal.ONE, "EACH")
                        ))
                )
        );
        assertTrue(dsl.fetchCount(DSL.table(DSL.name("ordering_order"))) == 0);
    }

    private PrototypeOrderUseCase useCase() {
        JooqOrderingUnitOfWork unitOfWork = new JooqOrderingUnitOfWork(
                dsl,
                transactionManager
        );
        return new PrototypeJooqOrderingUseCase(
                PrototypeMerchantRuntime.standard(),
                unitOfWork,
                unitOfWork::order,
                Clock.fixed(
                        Instant.parse("2026-08-26T03:00:00Z"),
                        ZoneOffset.UTC
                )
        );
    }

    private void seedStock() {
        dsl.insertInto(
                        DSL.table(DSL.name("inventory_stock_position")),
                        DSL.field(DSL.name("merchant_identifier"), String.class),
                        DSL.field(DSL.name("subject_identifier"), String.class),
                        DSL.field(DSL.name("stock_on_hand"), Long.class)
                )
                .values("prototype-retailer", "sku-1", 10L)
                .execute();
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: "
                            + name
            );
        }
        return value;
    }
}
