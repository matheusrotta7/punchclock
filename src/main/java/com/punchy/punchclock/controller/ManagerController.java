package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.service.EmployeeService;
import com.punchy.punchclock.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/{id}")
    public Manager getManager(@PathVariable("id") Long id) {
        return managerService.getManagerWithId(id);
    }

    @PostMapping
    public Manager createManager(@RequestBody Manager managerBody) {
        return managerService.createManager(managerBody);
    }

}