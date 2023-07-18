package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public Manager getManagerWithId(Long id) {
        return managerRepository.findById(id).orElse(null);
    }

    public Manager createManager(Manager managerBody) {
        return managerRepository.save(managerBody);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.getAllManagers();
    }
}
