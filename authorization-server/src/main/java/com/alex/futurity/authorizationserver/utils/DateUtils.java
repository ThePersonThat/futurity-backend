package com.alex.futurity.authorizationserver.utils;

import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@UtilityClass
public class DateUtils {
    private static final ZoneId UTC_TIME_ZONE = ZoneId.of("UTC");

    public ZonedDateTime now() {
        return ZonedDateTime.now(UTC_TIME_ZONE);
    }

    public boolean isInPast(ZonedDateTime dateTime) {
        return now().isAfter(dateTime);
    }

    public boolean isNotInPast(ZonedDateTime dateTime) {
        return !isInPast(dateTime);
    }

    public Date toDate(ZonedDateTime dateTime) {
        return Date.from(dateTime.toInstant());
    }
}
