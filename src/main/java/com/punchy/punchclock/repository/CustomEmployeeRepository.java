package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Person;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomEmployeeRepository {

    public List<Employee> getAllEmployees();

    @Transactional
    void saveToken(String token, Date tokenExpiryDate, Person targetPerson);

    @Transactional
    void updatePassword(String password, Person targetPerson);

    List<Employee> getAllEmployeesOfManager(Long managerId);
}
