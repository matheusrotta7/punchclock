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

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public LoginResponse login(Person loginBody) throws PunchException {
        LoginResponse loginResponse = new LoginResponse();

        Person targetPerson = getTargetPersonByUsername(loginBody.getUsername());

        if (targetPerson == null) {
            throw new PunchException("Person not found");
        }
        if (!isPasswordCorrect(loginBody, targetPerson)) {
            throw new PunchException("A person was found but the password was incorrect");
        }

        loginResponse.setId(targetPerson.getId());
        loginResponse.setName(targetPerson.getName());
        String roleString = targetPerson.getClass().getSimpleName();
        loginResponse.setRole(roleString);

        setIfRoot(loginResponse, targetPerson, roleString);

        if (!isCompanyPaying(targetPerson) && !loginResponse.getRoot()) { // root doesn't have to pay :)
            throw new PunchException("Credentials are correct but company is no longer paying");
        }

        String token = UUID.randomUUID().toString();
        saveTokenInDatabase(token, targetPerson);
        loginResponse.setToken(token);


        return loginResponse;
    }

    public Person getTargetPersonByUsername(String username) {
        List<Person> personList = getAllPeople();


        Person targetPerson = personList.stream()
                .filter(p -> username.equals(p.getUsername()))
                .findFirst()
                .orElse(null);
        return targetPerson;
    }

    private boolean isCompanyPaying(Person targetPerson) throws PunchException {
        String personClassStr = targetPerson.getClass().getSimpleName().toUpperCase();
        Role personRole = Role.valueOf(personClassStr);
        Company company = null;
        switch (personRole) {
            case ADMIN -> company = adminRepository.getAdminsCompany(targetPerson.getId());
            case MANAGER -> company = managerRepository.getManagersCompany(targetPerson.getId());
            case EMPLOYEE -> company = employeeRepository.getEmployeesCompany(targetPerson.getId());
        }
        if (company == null) {
            return false; // if you can't find the employee's company, assume the company is not paying (could just be root)
        }

        return company.getPaying();
    }

    private void setIfRoot(LoginResponse loginResponse, Person targetPerson, String roleString) {
        loginResponse.setRoot(false);
        if (roleString.equals(Admin.class.getSimpleName())) {
            Admin admin = (Admin) targetPerson;
            if (admin.getRoot() != null && admin.getRoot()) {
                loginResponse.setRoot(true);
            }
        }
    }

    public LoginResponse recoverUserViaToken(String token) throws PunchException {
        List<Person> personList = getAllPeople();

        Person targetPerson = personList.stream()
                .filter(p -> token.equals(p.getToken()))
                .findFirst()
                .orElse(null);

        if (targetPerson != null) {
            LoginResponse lr = new LoginResponse();
            String roleString = targetPerson.getClass().getSimpleName();
            lr.setRole(roleString);

            setIfRoot(lr, targetPerson, roleString);

            lr.setName(targetPerson.getName());
            lr.setId(targetPerson.getId());
            return lr;
        } else {
            throw new PunchException("A person was not found with this cookie");
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
