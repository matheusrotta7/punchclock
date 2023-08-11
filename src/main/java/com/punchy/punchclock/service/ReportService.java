package com.punchy.punchclock.service;

import com.itextpdf.io.font.CFFFont;
import com.itextpdf.io.font.FontCache;
import com.itextpdf.io.font.FontNames;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextChunkLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextChunk;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.filter.PunchFilter;
import com.punchy.punchclock.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
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

    private byte[] createPDFReport(List<Punch> punchList, String employeeName, Integer month)  {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try {
            PdfWriter pdfWriter = new PdfWriter(byteStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            addText(document, 16, "Monthly punch report");
            document.add( new Paragraph("\n") );


            addText(document, 12, "Employee: " + employeeName);
            document.add( new Paragraph("\n") );

            addText(document, 12, "Month: " + dateUtils.getMonthString(month));
            document.add( new Paragraph("\n") );


            if (punchList != null && !punchList.isEmpty()) {
                Table table = new Table(8);
                addTableHeader(table);
                addRows(table, punchList);
                document.add(table);
            } else {
                addText(document, 12, "Employee had no punches in this period");
                document.add( new Paragraph("\n") );
            }

            byte[] byteArray = byteStream.toByteArray();
            return byteArray;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

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
            if (punchList.size() < 4) {
                for (int i = 0; i < 4 - punchList.size(); i++) {
                    table.addCell("Missing punch");
                }
            }

            //add worked hours
            String workedHoursString = calculateWorkedHours(dailyPunches);
            table.addCell(workedHoursString);

            //add day balance (for now it can be the difference with 8 worked hours
            table.addCell(calculateDayBalance(workedHoursString));

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
            if (punchListByDay.get(day) != null) {
                punchListByDay.get(day).add(p);
            } else {
                punchListByDay.put(day, List.of(p));
            }
        }
        return punchListByDay;
    }


    private void addText(Document document, int fontSize, String text)  {
        document.add(new Paragraph(text));
    }

}
