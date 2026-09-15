package mainstreet.infrastructure.persistence.booking;

import mainstreet.application.MerchantScope;
import mainstreet.booking.BookingResidualObligationAuthority;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.util.Objects;

/**
 * PostgreSQL projection of Booking-owned reservation-commitment truth for the
 * residual-management decision. It deliberately does not derive discharge
 * from reservation timestamps or foreign-capability state.
 */
public final class JooqBookingResidualObligationAuthority
        implements BookingResidualObligationAuthority {

    private static final Table<?> BOOKING =
            DSL.table(DSL.name("booking_booking"));
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> RESERVATION_COMMITMENT_STATE =
            DSL.field(DSL.name("reservation_commitment_state"), String.class);

    private final DSLContext dsl;

    public JooqBookingResidualObligationAuthority(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
    }

    @Override
    public boolean hasOutstandingBookingObligation(MerchantScope merchantScope) {
        Objects.requireNonNull(merchantScope, "merchantScope");

        return dsl.fetchExists(
                dsl.selectOne()
                        .from(BOOKING)
                        .where(MERCHANT_IDENTIFIER.eq(
                                merchantScope.merchantIdentifier()
                        ))
                        .and(RESERVATION_COMMITMENT_STATE.eq("IN_FORCE"))
        );
    }
}
