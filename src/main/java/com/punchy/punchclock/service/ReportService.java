package com.punchy.punchclock.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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

    public byte[] generatePunchReport(PunchFilter punchFilter) throws DocumentException {
        List<Punch> punchList = punchService.getPunchListGivenFilter(punchFilter);
        String employeeName = getEmployeeName(punchFilter.getEmployeeId());

        return createPDFReport(punchList, employeeName, punchFilter.getMonth());
    }

    private byte[] createPDFReport(List<Punch> punchList, String employeeName, Integer month) throws DocumentException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            document.open();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, byteStream);

            addText(document, 16, "Monthly punch report");
            document.add( new Paragraph("\n") );


            addText(document, 12, "Employee: " + employeeName);
            document.add( new Paragraph("\n") );

            addText(document, 12, "Month: " + dateUtils.getMonthString(month));
            document.add( new Paragraph("\n") );

            PdfPTable table = new PdfPTable(8);
            addTableHeader(table);
            addRows(table, punchList);

            document.add(table);

            byte[] byteArray = byteStream.toByteArray();
            document.close();
            return byteArray;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    private String getEmployeeName(Long employeeId) {
        return employeeService.getEmployeeWithId(employeeId).getName();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Day", "Punch #1", "Punch #2", "Punch #3", "Punch #4", "Worked Hours", "Day Balance", "Special Observations")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<Punch> punchList) {
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


    private void addText(Document document, int fontSize, String text) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, fontSize, BaseColor.BLACK);
        Chunk titleChunk = new Chunk(text, font);

        document.add(titleChunk);
    }

}
