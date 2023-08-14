package com.punchy.punchclock.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.filter.PunchFilter;
import com.punchy.punchclock.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ReportService {

    @Autowired
    private PunchService punchService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired DateUtils dateUtils;

    public byte[] generatePunchReport(PunchFilter punchFilter)  {
        List<Punch> punchList = punchService.getPunchListGivenFilter(punchFilter);
        String employeeName = getEmployeeName(punchFilter.getEmployeeId());

        return createPDFReport(punchList, employeeName, punchFilter.getMonth());
    }

    private byte[] createPDFReport(List<Punch> punchList, String employeeName, Integer month) {
        File file = new File("pdfresults/report.pdf");
        file.getParentFile().mkdirs();

        PdfWriter pdfWriter = null;
        try {
            pdfWriter = new PdfWriter("pdfresults/report.pdf");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);


        PdfFont font;

        try {
            font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        document.add(new Paragraph("Monthly punch report").setFont(font));
        document.add(new Paragraph("\n").setFont(font));


        document.add(new Paragraph("Employee: " + employeeName).setFont(font));
        document.add(new Paragraph("\n").setFont(font));

        document.add(new Paragraph("Month: " + dateUtils.getMonthString(month)).setFont(font));
        document.add(new Paragraph("\n").setFont(font));


        if (punchList != null && !punchList.isEmpty()) {
            Table table = new Table(8);
            addTableHeader(table);
            addRows(table, punchList);
            document.add(table);
        } else {
            document.add(new Paragraph("Employee had no punches in this period").setFont(font));
            document.add(new Paragraph("\n").setFont(font));
        }

        document.close();

//            byte[] byteArray = byteStream.toByteArray();
        return null;

    }

    private String getEmployeeName(Long employeeId) {
        return employeeService.getEmployeeWithId(employeeId).getName();
    }

    private void addTableHeader(Table table) {
        Stream.of("Day", "Punch #1", "Punch #2", "Punch #3", "Punch #4", "Worked Hours", "Day Balance", "Special Observations")
                .forEach(columnTitle -> {
                    Cell headerCell = new Cell();
                    headerCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                    headerCell.setWidth(2);
                    headerCell.add(new Paragraph(columnTitle));
                    table.addCell(headerCell);
                });
    }

    private void addRows(Table table, List<Punch> punchList) {
        Map<Integer, List<Punch>> punchListByDay = groupByDate(punchList);
        for (Integer day : punchListByDay.keySet()) {
            List<Punch> dailyPunches = punchListByDay.get(day);
            table.addCell(dateUtils.dateToStringWithoutHours(dailyPunches.get(0).getTimestamp()));
            for (Punch p : dailyPunches) {
                table.addCell(dateUtils.timestampToHours(p.getTimestamp()));
            }

            //check if padding necessary....
            if (dailyPunches.size() < 4) {
                for (int i = 0; i < 4 - dailyPunches.size(); i++) {
                    table.addCell("Missing punch");
                }
            }

            //add worked hours
            String workedHoursString = calculateWorkedHours(dailyPunches);
            table.addCell(new Paragraph(workedHoursString));

            //add day balance (for now it can be the difference with 8 worked hours
            if (workedHoursString.equals("Odd number of Punches")) {
                table.addCell("n/a");
            } else {
                table.addCell(new Paragraph(calculateDayBalance(workedHoursString)).setFontColor(workedHoursString.charAt(0) == '+' ? ColorConstants.GREEN : ColorConstants.RED));
            }

            //add special observations
            table.addCell("Not Implemented yet"); //todo
        }
    }

    private String calculateDayBalance(String workedHoursString) {
        return dateUtils.calculateOffsetBetweenTwoHourStrings("08:00", workedHoursString); //08:00 hardcoded for now
    }

    private String calculateWorkedHours(List<Punch> dailyPunches) {
        if (dailyPunches == null || dailyPunches.size() == 0) {
            return "00:00";
        } else if (dailyPunches.size() % 2 == 1) {
            return "Odd number of Punches";
        }

        boolean openingPunch = true;
        Punch prevPunch = null;

        Long offsetTimeSum = 0L;

        for (Punch curPunch : dailyPunches) {
            if (openingPunch) {
                prevPunch = curPunch;
                openingPunch = false;
            } else {
                Long offsetTime = dateUtils.calculateOffsetTime(curPunch.getTimestamp(), prevPunch.getTimestamp());
                offsetTimeSum += offsetTime;
                openingPunch = true;
            }
        }

        Date offsetDate = new Date(offsetTimeSum);
        String hoursString = dateUtils.timestampToHours(offsetDate);

        return hoursString;
    }

    private Map<Integer, List<Punch>> groupByDate(List<Punch> punchList) {
        Map<Integer, List<Punch>> punchListByDay = new HashMap<>();
        for (Punch p : punchList) {
            Date punchDate = p.getTimestamp();
            int day = dateUtils.getDayFromDate(punchDate);
            List<Punch> thisDayPunchList = punchListByDay.get(day);
            if (thisDayPunchList != null) {
                thisDayPunchList.add(p);
            } else {
                List<Punch> newList = new ArrayList<>();
                newList.add(p);
                punchListByDay.put(day, newList);
            }
        }
        return punchListByDay;
    }


}
