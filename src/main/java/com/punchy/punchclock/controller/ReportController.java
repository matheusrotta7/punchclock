package com.punchy.punchclock.controller;

import com.punchy.punchclock.filter.PunchFilter;
import com.punchy.punchclock.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@CrossOrigin
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<byte[]> getPunchReport(@RequestBody PunchFilter punchFilter) {
        try {
            byte[] pdfFileBytes = reportService.generatePunchReport(punchFilter);
            return ResponseEntity.ok(pdfFileBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
