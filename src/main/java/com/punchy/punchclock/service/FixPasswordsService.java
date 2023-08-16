package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Admin;
import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.exception.PunchException;
import com.punchy.punchclock.vo.Role;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FixPasswordsService {

    private static final Logger logger = LoggerFactory.getLogger(FixPasswordsService.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AdminService adminService;

    public void fixPasswords() {

        List<Person> personList = getAllPeople();

        for (Person p : personList) {
            updatePassword(p);
        }

    }

    private void updatePassword(Person person) {
        // person class already has the password, just need to calculate the hash
        // and save to correct table (admin, manager or employee)
        // could be a switch case like in login service

        String curPassword = person.getPassword();
        if (curPassword == null || curPassword.length() == 0) {
            logger.info("Skipping this person because he didn't have a password in the first place");
            return;
        }
        String passwordHash = calculateHash(curPassword);

        try {
            updatePassword(passwordHash, person);
        } catch (PunchException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePassword(String newPassword, Person targetPerson) throws PunchException {
        if (targetPerson.getUsername().equals("zeno@zoldyck.fast")) {
            logger.info("Skipping Zeno because his password is already hashed");
            return;
        }

        logger.info("Updating password for person " + targetPerson.getName());
        String personClassStr = targetPerson.getClass().getSimpleName().toUpperCase();
        Role personRole = Role.valueOf(personClassStr);

        switch (personRole) {
            case ADMIN -> adminService.updatePassword(newPassword, targetPerson);
            case MANAGER -> managerService.updatePassword(newPassword, targetPerson);
            case EMPLOYEE -> employeeService.updatePassword(newPassword, targetPerson);
            default -> throw new PunchException("Wrong Role while saving newPassword to database");
        }
    }

    private String calculateHash(String password) {
        return DigestUtils.sha256Hex(password);
    }

    private List<Person> getAllPeople() {
        List<Employee> employeeList = employeeService.getAllEmployees();
        List<Manager> managerList = managerService.getAllManagers();
        List<Admin> adminList = adminService.getAllAdmins();

        List<Person> personList = new ArrayList<>(employeeList);
        personList.addAll(managerList);
        personList.addAll(adminList);
        return personList;
    }

}
