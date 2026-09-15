package mainstreet.businesshours;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Stable merchant-owned weekly public Business Hours for one explicit
 * Business Hours scope. This is merchant-profile configuration, not Scheduling
 * capability configuration.
 */
public record StandardBusinessHours(
        BusinessHoursScope scope,
        ZoneId timeZone,
        Set<WeeklyOperatingInterval> weeklyOperatingIntervals
) {
    private static final int MINUTES_PER_DAY = 24 * 60;
    private static final int MINUTES_PER_WEEK = 7 * MINUTES_PER_DAY;

    public StandardBusinessHours {
        Objects.requireNonNull(scope, "scope");
        Objects.requireNonNull(timeZone, "Time zone must not be null");
        weeklyOperatingIntervals = Set.copyOf(
                Objects.requireNonNull(
                        weeklyOperatingIntervals,
                        "Weekly operating intervals must not be null"
                )
        );
        rejectOverlaps(weeklyOperatingIntervals);
    }

    public StandardBusinessHours(
            BusinessHoursScope scope,
            String timeZoneIdentifier,
            Set<WeeklyOperatingInterval> weeklyOperatingIntervals
    ) {
        this(
                scope,
                parseIanaTimeZone(timeZoneIdentifier),
                weeklyOperatingIntervals
        );
    }

    private static ZoneId parseIanaTimeZone(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Time zone identifier must not be blank"
            );
        }
        if (!ZoneId.getAvailableZoneIds().contains(identifier)) {
            throw new IllegalArgumentException(
                    "Unknown IANA time zone: " + identifier
            );
        }
        try {
            return ZoneId.of(identifier);
        } catch (DateTimeException exception) {
            throw new IllegalArgumentException(
                    "Unknown IANA time zone: " + identifier,
                    exception
            );
        }
    }

    private static void rejectOverlaps(
            Set<WeeklyOperatingInterval> intervals
    ) {
        List<WeekSegment> segments = new ArrayList<>();
        for (WeeklyOperatingInterval interval : intervals) {
            addSegments(interval, segments);
        }
        segments.sort(Comparator.comparingInt(WeekSegment::startMinute));
        for (int index = 1; index < segments.size(); index++) {
            WeekSegment previous = segments.get(index - 1);
            WeekSegment current = segments.get(index);
            if (current.startMinute() < previous.endMinute()) {
                throw new IllegalArgumentException(
                        "Weekly operating intervals must not overlap"
                );
            }
        }
    }

    private static void addSegments(
            WeeklyOperatingInterval interval,
            List<WeekSegment> target
    ) {
        int dayStart = (interval.startDay().getValue() - 1)
                * MINUTES_PER_DAY;
        int start = dayStart + minuteOfDay(interval.startTime());
        int end = dayStart + minuteOfDay(interval.endTime());
        if (interval.crossesMidnight()) {
            end += MINUTES_PER_DAY;
        }

        if (end <= MINUTES_PER_WEEK) {
            target.add(new WeekSegment(start, end));
            return;
        }

        target.add(new WeekSegment(start, MINUTES_PER_WEEK));
        int wrappedEnd = end - MINUTES_PER_WEEK;
        if (wrappedEnd > 0) {
            target.add(new WeekSegment(0, wrappedEnd));
        }
    }

    private static int minuteOfDay(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    private record WeekSegment(int startMinute, int endMinute) {
    }
}
