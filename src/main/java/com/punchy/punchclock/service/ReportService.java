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

    public byte[] generatePunchReport(PunchFilter punchFilter) {
        List<Punch> punchList = punchService.getPunchListGivenFilter(punchFilter);
        String employeeName = getEmployeeName(punchFilter.getEmployeeId());

        return createPDFReport(punchList, employeeName, punchFilter.getMonth());
    }

    private byte[] createPDFReport(List<Punch> punchList, String employeeName, Integer month) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            document.open();
            PdfWriter.getInstance(document, byteStream);

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


            document.close();
        } catch (Exception e) {
            e.printStackTrace();
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
            for (Punch p : punchList) {
                table.addCell(dateUtils.timestampToHours(p.getTimestamp()));
            }

            //check if padding necessary....
            if (punchList.size() < 4) {
                for (int i = 0; i < 4 - punchList.size(); i++) {
                    table.addCell("Missing punch");
                }
            }

            //add worked hours
            //add day balance
            //add special observations
        }
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
