package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.service.EmployeeService;
import com.punchy.punchclock.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
@CrossOrigin
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManager(@PathVariable("id") Long id) {
        Manager manager = managerService.getManagerWithId(id);
        if (manager != null) {
            return ResponseEntity.ok(manager);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Manager createManager(@RequestBody Manager managerBody) {
        return managerService.createManager(managerBody);
    }

}
