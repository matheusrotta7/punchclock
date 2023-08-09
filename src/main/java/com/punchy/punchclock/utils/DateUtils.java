package com.punchy.punchclock.utils;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtils {

    public Date nowPlusHours(int numHours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, numHours);

        return cal.getTime();
    }

    public int getDayFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

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

    public Date stringToDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String dateToStringWithoutHours(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" +  cal.get(Calendar.DAY_OF_MONTH);
    }

    public String getMonthString(Integer month) {
        if (month == null) {
            return null;
        }

        return Month.of(month).name();
    }

    public String timestampToHours(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE);
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
