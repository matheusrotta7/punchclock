package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee getEmployeeWithId(Long id) {
        return employeeRepository.getReferenceById(id);
    }
}
