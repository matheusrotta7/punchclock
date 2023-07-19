package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.service.EmployeeService;
import com.punchy.punchclock.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Manager>> getAllManagers() {
        List<Manager> managerList = managerService.getAllManagers();
        if (managerList != null) {
            return ResponseEntity.ok(managerList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Manager createManager(@RequestBody Manager managerBody) {
        return managerService.createManager(managerBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable("id") Long id) {
        try {
            managerService.deleteManager(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
