package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.*;
import com.punchy.punchclock.exception.PunchException;
import com.punchy.punchclock.repository.EmployeeRepository;
import com.punchy.punchclock.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    
    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerService managerService;

    public Employee getEmployeeWithId(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.orElse(null);
    }

    public Employee createEmployee(Employee employeeBody) throws PunchException {
        Company company = employeeRepository.getEmployeesCompany(employeeBody.getManager().getId());
        int curNumberOfEmployees = numberOfEmployees(company);
        int maxNumberOfEmployees = company.getMaxNumberOfEmployees();
        if (curNumberOfEmployees < maxNumberOfEmployees) {
            logger.info("number of employees used: " + curNumberOfEmployees + "/" + maxNumberOfEmployees);
            return employeeRepository.save(employeeBody);
        } else {
            logger.warn("Maximum number of employees reached " + "(" + maxNumberOfEmployees + ")");
            throw new PunchException("This company has already reached its maximum employee capacity inside punchy");
        }
    }

    private int numberOfEmployees(Company company) {

        List<Employee> employeeList = company.getAdminList().stream().map(Admin::getManagerList).flatMap(List::stream).map(Manager::getEmployeeList).flatMap(List::stream).toList();
        return employeeList.size();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    public void saveToken(String token, Date tokenExpiryDate, Person targetPerson) {
        employeeRepository.saveToken(token, tokenExpiryDate, targetPerson);
    }

    public List<Employee> getAllEmployeesOfManager(Long managerId) {
        return employeeRepository.getAllEmployeesOfManager(managerId);
    }
}
