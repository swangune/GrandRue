package mainstreet.businesshours;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardBusinessHoursTest {

    private static final MerchantScope MERCHANT_SCOPE =
            new MerchantScope("merchant-a");
    private static final BusinessHoursScope BUSINESS_HOURS_SCOPE =
            BusinessHoursScope.merchant(MERCHANT_SCOPE);

    @Test
    void preserves_explicit_merchant_scope() {
        StandardBusinessHours hours = new StandardBusinessHours(
                BUSINESS_HOURS_SCOPE,
                "Europe/London",
                Set.of()
        );

        assertEquals(BUSINESS_HOURS_SCOPE, hours.scope());
        assertEquals(BusinessHoursScopeKind.MERCHANT, hours.scope().kind());
        assertEquals(MERCHANT_SCOPE, hours.scope().merchantScope());
        assertTrue(hours.scope().merchantLocationIdentity().isEmpty());
    }

    @Test
    void preserves_explicit_merchant_location_scope() {
        BusinessHoursScope locationScope = BusinessHoursScope.merchantLocation(
                MERCHANT_SCOPE,
                "location-swansea"
        );

        StandardBusinessHours hours = new StandardBusinessHours(
                locationScope,
                "Europe/London",
                Set.of()
        );

        assertEquals(BusinessHoursScopeKind.MERCHANT_LOCATION, hours.scope().kind());
        assertEquals(MERCHANT_SCOPE, hours.scope().merchantScope());
        assertEquals(
                "location-swansea",
                hours.scope().merchantLocationIdentity().orElseThrow()
        );
    }

    @Test
    void rejects_blank_merchant_location_identity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> BusinessHoursScope.merchantLocation(
                        MERCHANT_SCOPE,
                        " "
                )
        );
    }

    @Test
    void preserves_a_valid_same_day_operating_interval() {
        WeeklyOperatingInterval monday = new WeeklyOperatingInterval(
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        );

        StandardBusinessHours hours = new StandardBusinessHours(
                BUSINESS_HOURS_SCOPE,
                "Europe/London",
                Set.of(monday)
        );

        assertEquals(ZoneId.of("Europe/London"), hours.timeZone());
        assertEquals(Set.of(monday), hours.weeklyOperatingIntervals());
        assertFalse(monday.crossesMidnight());
        assertEquals(DayOfWeek.MONDAY, monday.endDay());
    }

    @Test
    void rejects_an_unknown_time_zone() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardBusinessHours(
                        BUSINESS_HOURS_SCOPE,
                        "Not/AZone",
                        Set.of()
                )
        );
    }

    @Test
    void preserves_cross_midnight_business_meaning() {
        WeeklyOperatingInterval fridayNight = new WeeklyOperatingInterval(
                DayOfWeek.FRIDAY,
                LocalTime.of(20, 0),
                LocalTime.of(2, 0)
        );

        StandardBusinessHours hours = new StandardBusinessHours(
                BUSINESS_HOURS_SCOPE,
                "Europe/London",
                Set.of(fridayNight)
        );

        assertTrue(fridayNight.crossesMidnight());
        assertEquals(DayOfWeek.SATURDAY, fridayNight.endDay());
        assertEquals(Set.of(fridayNight), hours.weeklyOperatingIntervals());
    }

    @Test
    void rejects_a_zero_length_operating_interval() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyOperatingInterval(
                        DayOfWeek.MONDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(9, 0)
                )
        );
    }

    @Test
    void rejects_overlapping_same_day_intervals() {
        WeeklyOperatingInterval morning = new WeeklyOperatingInterval(
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(13, 0)
        );
        WeeklyOperatingInterval overlap = new WeeklyOperatingInterval(
                DayOfWeek.MONDAY,
                LocalTime.of(12, 30),
                LocalTime.of(17, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardBusinessHours(
                        BUSINESS_HOURS_SCOPE,
                        "Europe/London",
                        Set.of(morning, overlap)
                )
        );
    }

    @Test
    void rejects_overlap_created_by_a_cross_midnight_interval() {
        WeeklyOperatingInterval fridayNight = new WeeklyOperatingInterval(
                DayOfWeek.FRIDAY,
                LocalTime.of(20, 0),
                LocalTime.of(2, 0)
        );
        WeeklyOperatingInterval saturdayEarly = new WeeklyOperatingInterval(
                DayOfWeek.SATURDAY,
                LocalTime.of(1, 0),
                LocalTime.of(3, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardBusinessHours(
                        BUSINESS_HOURS_SCOPE,
                        "Europe/London",
                        Set.of(fridayNight, saturdayEarly)
                )
        );
    }

    @Test
    void permits_adjacent_non_overlapping_intervals() {
        WeeklyOperatingInterval morning = new WeeklyOperatingInterval(
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(12, 0)
        );
        WeeklyOperatingInterval afternoon = new WeeklyOperatingInterval(
                DayOfWeek.MONDAY,
                LocalTime.of(12, 0),
                LocalTime.of(17, 0)
        );

        StandardBusinessHours hours = new StandardBusinessHours(
                BUSINESS_HOURS_SCOPE,
                "Europe/London",
                Set.of(morning, afternoon)
        );

        assertEquals(2, hours.weeklyOperatingIntervals().size());
    }
}
