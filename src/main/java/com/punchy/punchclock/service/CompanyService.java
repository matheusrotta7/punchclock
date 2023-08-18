package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Company;
import com.punchy.punchclock.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;


    public Company createCompany(Company companyBody) {
        return companyRepository.save(companyBody);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
}
