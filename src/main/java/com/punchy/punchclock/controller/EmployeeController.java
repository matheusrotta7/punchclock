package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable("id") Long id) {
        return employeeService.getEmployeeWithId(id);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employeeBody) {
        return employeeService.createEmployee(employeeBody);
    }


}
