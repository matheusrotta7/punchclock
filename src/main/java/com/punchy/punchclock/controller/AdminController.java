package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Admin;
import com.punchy.punchclock.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public Admin createAdmin(@RequestBody Admin adminBody) {
        return adminService.createAdmin(adminBody);
    }

    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        try {
            List<Admin> adminList = adminService.getAllAdmins();
            return ResponseEntity.ok(adminList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
