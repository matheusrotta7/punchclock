package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Admin;
import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.vo.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoginService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AdminService adminService;

    public LoginResponse login(Person loginBody) {
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

        if (targetPerson != null) {
            if (isPasswordCorrect(loginBody, targetPerson)) {
                loginResponse.setId(targetPerson.getId());
                loginResponse.setName(targetPerson.getName());
                loginResponse.setRole(targetPerson.getClass().getSimpleName());
            }
        } else {
            loginResponse = null;
        }

        return loginResponse;
    }

    private boolean isPasswordCorrect(Person loginBody, Person targetPerson) {
        return targetPerson.getPassword().equals(loginBody.getPassword());
    }

}
