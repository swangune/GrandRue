package grandrue.businesshours;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

/**
 * One merchant-facing weekly operating period. An end time earlier than the
 * start time means the interval crosses midnight into the following day.
 */
public record WeeklyOperatingInterval(
        DayOfWeek startDay,
        LocalTime startTime,
        LocalTime endTime
) {

    public WeeklyOperatingInterval {
        Objects.requireNonNull(startDay, "Start day must not be null");
        Objects.requireNonNull(startTime, "Start time must not be null");
        Objects.requireNonNull(endTime, "End time must not be null");
        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException(
                    "Operating interval must have non-zero duration"
            );
        }
    }

    public boolean crossesMidnight() {
        return endTime.isBefore(startTime);
    }

    public DayOfWeek endDay() {
        return crossesMidnight() ? startDay.plus(1) : startDay;
    }
}
