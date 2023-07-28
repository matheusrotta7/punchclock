package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.EmployeeRepository;
import com.punchy.punchclock.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmployeeService {
    
    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee getEmployeeWithId(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.orElse(null);
    }

    public Employee createEmployee(Employee employeeBody) {
        return employeeRepository.save(employeeBody);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    public void saveToken(String token, Person targetPerson) {
        Employee employee = new Employee();

        employee.setId(targetPerson.getId());
        employee.setName(targetPerson.getName());
        employee.setPassword(targetPerson.getPassword());

        employee.setToken(token);
        employee.setTokenExpiryDate(dateUtils.nowPlusHours(9));

        employeeRepository.save(employee);
    }
}
