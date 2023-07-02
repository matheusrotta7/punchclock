package com.punchy.punchclock.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtils {

    public Date getFirstMomentOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, 1, 0, 0);

        return calendar.getTime();
    }

    public Date getLastMomentOfMonth(Integer month, Integer year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, 1, 0, 0);

        return calendar.getTime();
    }
}
