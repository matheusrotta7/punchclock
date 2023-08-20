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
            Table table = new Table(7);
            addTableHeader(table);
            addRows(table, punchList);
            document.add(table);
        } else {
            document.add(new Paragraph("Employee had no punches in this period").setFont(font));
            document.add(new Paragraph("\n").setFont(font));
        }

        document.close();

//            byte[] byteArray = byteStream.toByteArray();
        FileInputStream fl = null;
        try {
            fl = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Now creating byte array of same length as file
        byte[] arr = new byte[(int)file.length()];

        // Reading file content to byte array
        // using standard read() method
        try {
            fl.read(arr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // lastly closing an instance of file input stream
        // to avoid memory leakage
        try {
            fl.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Returning above byte array
        return arr;

    }

    private String getEmployeeName(Long employeeId) {
        return employeeService.getEmployeeWithId(employeeId).getName();
    }

    private void addTableHeader(Table table) {
        Stream.of("Day", "Punch #1", "Punch #2", "Punch #3", "Punch #4", "Worked Hours", "Day Balance")
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
            table.addCell(new Paragraph(stripPlusIfNecessary(workedHoursString)));

            //add day balance (for now it can be the difference with 8 worked hours
            if (workedHoursString.equals("Odd number of Punches")) {
                table.addCell("n/a");
            } else {
                String dayBalance = calculateDayBalance(workedHoursString);
                Color fontColor = dayBalance.charAt(0) == '+' ? ColorConstants.BLUE : ColorConstants.RED;
                table.addCell(new Paragraph(dayBalance).setFontColor(fontColor));
            }

            //add special observations
//            table.addCell("Not Implemented yet"); //todo
        }
    }

    private String stripPlusIfNecessary(String workedHoursString) {
        if (workedHoursString == null || workedHoursString.length() == 0) {
            return null;
        }

        return workedHoursString.charAt(0) == '+' ? workedHoursString.substring(1) : workedHoursString;
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

        String offsetTimeSumString = "00:00";

        for (Punch curPunch : dailyPunches) {
            if (openingPunch) {
                prevPunch = curPunch;
                openingPunch = false;
            } else {
                offsetTimeSumString = dateUtils.sumHourStrings(offsetTimeSumString, dateUtils.calculateOffsetBetweenTwoHourStrings(dateUtils.timestampToHours(prevPunch.getTimestamp()), dateUtils.timestampToHours(curPunch.getTimestamp())));
                openingPunch = true;
            }
        }


        return offsetTimeSumString;
    }

    private Map<Integer, List<Punch>> groupByDate(List<Punch> punchList) {
        Map<Integer, List<Punch>> punchListByDay = new TreeMap<>();
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
