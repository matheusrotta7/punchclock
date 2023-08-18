package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Company;
import com.punchy.punchclock.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@CrossOrigin
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company companyBody) {
        try {
            Company company = companyService.createCompany(companyBody);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
