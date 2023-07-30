package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.*;
import com.punchy.punchclock.exception.PunchException;
import com.punchy.punchclock.repository.AdminRepository;
import com.punchy.punchclock.repository.EmployeeRepository;
import com.punchy.punchclock.repository.ManagerRepository;
import com.punchy.punchclock.utils.DateUtils;
import com.punchy.punchclock.vo.LoginResponse;
import com.punchy.punchclock.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class LoginService {

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AdminService adminService;

    public LoginResponse login(Person loginBody) throws PunchException {
        LoginResponse loginResponse = new LoginResponse();

        List<Person> personList = getAllPeople();


        Person targetPerson = personList.stream()
                .filter(p -> loginBody.getUsername().equals(p.getUsername()))
                .findFirst()
                .orElse(null);

        if (targetPerson != null && isPasswordCorrect(loginBody, targetPerson)) {
            loginResponse.setId(targetPerson.getId());
            loginResponse.setName(targetPerson.getName());
            loginResponse.setRole(targetPerson.getClass().getSimpleName());
            String token = UUID.randomUUID().toString();
            saveTokenInDatabase(token, targetPerson);
            loginResponse.setToken(token);
        } else {
            throw new PunchException("Person not found");
        }

        return loginResponse;
    }

    public LoginResponse recoverUserViaToken(String token) {
        List<Person> personList = getAllPeople();

        Person targetPerson = personList.stream()
                .filter(p -> token.equals(p.getToken()))
                .findFirst()
                .orElse(null);

        if (targetPerson != null) {
            LoginResponse lr = new LoginResponse();
            lr.setRole(targetPerson.getClass().getSimpleName());
            lr.setName(targetPerson.getName());
            lr.setId(targetPerson.getId());
            return lr;
        } else {
            return null;
        }
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

    private void saveTokenInDatabase(String token, Person targetPerson) throws PunchException {
        String personClassStr = targetPerson.getClass().getSimpleName().toUpperCase();
        Role personRole = Role.valueOf(personClassStr);
        Date tokenExpiryDate = dateUtils.nowPlusHours(9);
        switch (personRole) {
            case ADMIN -> adminService.saveToken(token, tokenExpiryDate, targetPerson);
            case MANAGER -> managerService.saveToken(token, tokenExpiryDate, targetPerson);
            case EMPLOYEE -> employeeService.saveToken(token, tokenExpiryDate, targetPerson);
            default -> throw new PunchException("Wrong Role while saving token to database");
        }
    }

    private boolean isPasswordCorrect(Person loginBody, Person targetPerson) {
        return targetPerson.getPassword().equals(loginBody.getPassword());
    }


}