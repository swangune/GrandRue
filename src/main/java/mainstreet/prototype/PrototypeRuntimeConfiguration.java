package mainstreet.prototype;

import grandrue.infrastructure.persistence.appointment.JooqAppointmentUnitOfWork;
import mainstreet.infrastructure.persistence.booking.JooqBookingUnitOfWork;
import mainstreet.infrastructure.persistence.ordering.JooqOrderingUnitOfWork;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;

/** Spring composition shell for the explicitly opt-in prototype profile. */
@Configuration
@Profile("prototype")
public class PrototypeRuntimeConfiguration {

    @Bean
    PrototypeMerchantRuntime prototypeMerchantRuntime() {
        return PrototypeMerchantRuntime.standard();
    }

    @Bean
    Clock prototypeClock() {
        return Clock.systemUTC();
    }

    @Bean
    PrototypeOrderUseCase prototypeOrderUseCase(
            PrototypeMerchantRuntime merchantRuntime,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Clock prototypeClock
    ) {
        JooqOrderingUnitOfWork unitOfWork = new JooqOrderingUnitOfWork(
                dsl,
                transactionManager
        );
        return new PrototypeJooqOrderingUseCase(
                merchantRuntime,
                unitOfWork,
                unitOfWork::order,
                prototypeClock
        );
    }

    @Bean
    PrototypeBookingUseCase prototypeBookingUseCase(
            PrototypeMerchantRuntime merchantRuntime,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Clock prototypeClock
    ) {
        JooqBookingUnitOfWork unitOfWork = new JooqBookingUnitOfWork(
                dsl,
                transactionManager,
                prototypeClock
        );
        return new PrototypeJooqBookingUseCase(
                merchantRuntime,
                unitOfWork,
                unitOfWork::booking,
                prototypeClock
        );
    }

    @Bean
    PrototypeAppointmentUseCase prototypeAppointmentUseCase(
            PrototypeMerchantRuntime merchantRuntime,
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            Clock prototypeClock
    ) {
        JooqAppointmentUnitOfWork unitOfWork = new JooqAppointmentUnitOfWork(
                dsl,
                transactionManager
        );
        return new PrototypeJooqAppointmentUseCase(
                merchantRuntime,
                unitOfWork,
                unitOfWork::appointment,
                prototypeClock
        );
    }

    @Bean
    PrototypePublicOrderUseCase prototypePublicOrderUseCase(
            PrototypeOrderUseCase orders
    ) {
        return new PrototypePublicOrderUseCase(orders);
    }

    @Bean
    PrototypePublicBookingUseCase prototypePublicBookingUseCase(
            PrototypeBookingUseCase bookings
    ) {
        return new PrototypePublicBookingUseCase(bookings);
    }

    @Bean
    PrototypePublicAppointmentUseCase prototypePublicAppointmentUseCase(
            PrototypeAppointmentUseCase appointments
    ) {
        return new PrototypePublicAppointmentUseCase(appointments);
    }

    @Bean
    PrototypePublicationProjection prototypePublicationProjection(
            PrototypeMerchantRuntime merchantRuntime
    ) {
        return new PrototypePublicationProjection(merchantRuntime);
    }

    @Bean
    PrototypeStorefrontSurfaceProjection prototypeStorefrontSurfaceProjection(
            PrototypeMerchantRuntime merchantRuntime
    ) {
        return PrototypeStorefrontSurfaceProjection.standard(merchantRuntime);
    }

    /**
     * Deterministic fixture data only. This is not an Inventory adjustment
     * operation and must not be reused as a production mutation path.
     */
    @Bean
    ApplicationRunner prototypeInventoryFixture(DSLContext dsl) {
        return arguments -> {
            for (String merchantIdentifier :
                    PrototypeOrderingSubjectConfiguration.merchants()) {
                for (PrototypeOrderingSubjectConfiguration.Subject subject :
                        PrototypeOrderingSubjectConfiguration.subjectsFor(
                                merchantIdentifier
                        )) {
                    dsl.insertInto(
                                    DSL.table(DSL.name("inventory_stock_position")),
                                    DSL.field(
                                            DSL.name("merchant_identifier"),
                                            String.class
                                    ),
                                    DSL.field(
                                            DSL.name("subject_identifier"),
                                            String.class
                                    ),
                                    DSL.field(
                                            DSL.name("stock_on_hand"),
                                            Long.class
                                    )
                            )
                            .values(
                                    merchantIdentifier,
                                    subject.stockSubjectReference(),
                                    subject.initialStockOnHand()
                            )
                            .onConflict(
                                    DSL.field(DSL.name("merchant_identifier")),
                                    DSL.field(DSL.name("subject_identifier"))
                            )
                            .doNothing()
                            .execute();
                }
            }
        };
    }
}
