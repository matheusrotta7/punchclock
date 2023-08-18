package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
