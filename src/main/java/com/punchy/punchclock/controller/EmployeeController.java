package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.exception.PunchException;
import com.punchy.punchclock.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@CrossOrigin
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") Long id) {
        Employee employee = employeeService.getEmployeeWithId(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employeeBody) {
        try {
            Employee employee = employeeService.createEmployee(employeeBody);
            return ResponseEntity.ok(employee);
        } catch (PunchException punchException) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employeeList = employeeService.getAllEmployees();
        if (employeeList != null) {
            return ResponseEntity.ok(employeeList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/manager/{id}")
    public ResponseEntity<List<Employee>> getAllEmployeesOfManager(@PathVariable("id") Long managerId) {
        List<Employee> employeeList = employeeService.getAllEmployeesOfManager(managerId);
        if (employeeList != null) {
            return ResponseEntity.ok(employeeList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
