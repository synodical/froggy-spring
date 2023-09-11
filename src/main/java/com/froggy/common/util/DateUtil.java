package com.froggy.common.util;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class DateUtil {
    private static Clock clock = Clock.systemDefaultZone();
    public static void setClock(Clock clock) {
        DateUtil.clock = clock;
    }
    public static LocalDateTime now(){
        return LocalDateTime.now(clock);
    }

    public static LocalDate today(){
        return LocalDate.now(clock);
    }

    public static LocalDate getStartOfMonth(LocalDate localDate) {
        LocalDate startOfMonth = localDate.withDayOfMonth(1);
        return startOfMonth;
    }

    public static LocalDate getEndOfMonth(LocalDate localDate) {
        LocalDate endOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return endOfMonth;
    }

    public static LocalDate getStartOfWeek(LocalDate localDate) {
        LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return startOfWeek;
    }

    public static LocalDate getEndOfWeek(LocalDate localDate) {
        LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return endOfWeek;
    }
}
