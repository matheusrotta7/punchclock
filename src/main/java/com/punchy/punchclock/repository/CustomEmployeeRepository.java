package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomEmployeeRepository {
    public List<Employee> getAllEmployees();
}
