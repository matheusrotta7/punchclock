package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee getEmployeeWithId(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.orElse(null);
    }

    public Employee createEmployee(Employee employeeBody) {
        return employeeRepository.save(employeeBody);
    }

}
