package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Admin;
import com.punchy.punchclock.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin createAdmin(Admin adminBody) {
        return adminRepository.save(adminBody);
    }


    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
}
