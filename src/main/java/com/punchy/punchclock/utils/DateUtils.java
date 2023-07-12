package com.punchy.punchclock.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtils {

    public Date getFirstMomentOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, 1, 0, 0, 0);

        return calendar.getTime();
    }

    public Date getLastMomentOfMonth(Integer month, Integer year) {
        Calendar calendar = Calendar.getInstance();

        //set calendar first so that getActualMaximum can fetch the correct max for this month
        calendar.set(year, month, 1, 0, 0);

        int dateMax = calendar.getActualMaximum(Calendar.DATE);
        int hourMax = calendar.getActualMaximum(Calendar.HOUR);
        int minuteMax = calendar.getActualMaximum(Calendar.MINUTE);
        int secondMax = calendar.getActualMaximum(Calendar.SECOND);

        calendar.set(year, month, dateMax, hourMax, minuteMax, secondMax);

        return calendar.getTime();
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        int actualMaximum = calendar.getActualMaximum(Calendar.DATE);
        System.out.println("Date maximum: " + actualMaximum);
        calendar.set(2020, Calendar.FEBRUARY, 1, 0, 0);
        actualMaximum = calendar.getActualMaximum(Calendar.DATE);
        System.out.println("Date maximum for february: " + actualMaximum);

        System.out.println("*******************************");

        DateUtils du = new DateUtils();
        Date lastMomentOfFebruaryOnLeapYear = du.getLastMomentOfMonth(Calendar.FEBRUARY, 2020);
        Date firstMomentOfDecember = du.getFirstMomentOfMonth(Calendar.DECEMBER, 2023);
        System.out.println("lastMomentOfFebruaryOnLeapYear: " + lastMomentOfFebruaryOnLeapYear);
        System.out.println("firstMomentOfDecember: " + firstMomentOfDecember);

        System.out.println("***********************************");
        calendar.set(2023, 3, 23, 15, 44, 12);
        Date testDateString = calendar.getTime();
        System.out.println("test date format: " + testDateString);

    }
}
