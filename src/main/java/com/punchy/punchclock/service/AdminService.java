package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Admin;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.AdminRepository;
import com.punchy.punchclock.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AdminService {

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private AdminRepository adminRepository;

    public Admin createAdmin(Admin adminBody) {
        return adminRepository.save(adminBody);
    }


    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public void saveToken(String token, Date tokenExpiryDate, Person targetPerson) {
        adminRepository.saveToken(token, tokenExpiryDate, targetPerson);
    }

    public void savePasswordToken(String token, Person targetPerson) {
        adminRepository.savePasswordToken(token, targetPerson);
    }
}
