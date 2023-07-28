package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.*;
import com.punchy.punchclock.exception.PunchException;
import com.punchy.punchclock.repository.AdminRepository;
import com.punchy.punchclock.repository.EmployeeRepository;
import com.punchy.punchclock.repository.ManagerRepository;
import com.punchy.punchclock.vo.LoginResponse;
import com.punchy.punchclock.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class LoginService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AdminService adminService;

    public LoginResponse login(Person loginBody) throws PunchException {
        LoginResponse loginResponse = new LoginResponse();

        List<Employee> employeeList = employeeService.getAllEmployees();
        List<Manager> managerList = managerService.getAllManagers();
        List<Admin> adminList = adminService.getAllAdmins();

        List<Person> personList = new ArrayList<>(employeeList);
        personList.addAll(managerList);
        personList.addAll(adminList);


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
            loginResponse = null;
        }

        return loginResponse;
    }

    private void saveTokenInDatabase(String token, Person targetPerson) throws PunchException {
        String personClassStr = targetPerson.getClass().getSimpleName().toUpperCase();
        Role personRole = Role.valueOf(personClassStr);
        switch (personRole) {
            case ADMIN -> adminService.saveToken(token, targetPerson);
            case MANAGER -> managerService.saveToken(token, targetPerson);
            case EMPLOYEE -> employeeService.saveToken(token, targetPerson);
            default -> throw new PunchException("Wrong Role while saving token to database");
        }
    }

    private boolean isPasswordCorrect(Person loginBody, Person targetPerson) {
        return targetPerson.getPassword().equals(loginBody.getPassword());
    }


}
