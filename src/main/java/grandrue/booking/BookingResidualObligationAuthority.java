package grandrue.booking;

import grandrue.application.MerchantScope;

/**
 * Booking-owned authority for whether the merchant still has at least one
 * outstanding Booking reservation obligation requiring operational management.
 *
 * <p>Time passage, payment/refund state, fulfilment outcome and presentation
 * state are not independent discharge authority.</p>
 */
@FunctionalInterface
public interface BookingResidualObligationAuthority {

    boolean hasOutstandingBookingObligation(MerchantScope merchantScope);
}
