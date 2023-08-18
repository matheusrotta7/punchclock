package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.ManagerRepository;
import com.punchy.punchclock.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ManagerService {

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private ManagerRepository managerRepository;

    public Manager getManagerWithId(Long id) {
        return managerRepository.findById(id).orElse(null);
    }

    public Manager createManager(Manager managerBody) {
        return managerRepository.save(managerBody);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.getAllManagers(null);
    }

    public void deleteManager(Long id) {
        managerRepository.deleteById(id);
    }

    public void saveToken(String token, Date tokenExpiryDate, Person targetPerson) {
        managerRepository.saveToken(token, tokenExpiryDate, targetPerson);
    }

    public List<Manager> getAllManagersFromAdmin(Long adminId) {
        return managerRepository.getAllManagers(adminId);
    }
}
