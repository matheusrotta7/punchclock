package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomEmployeeRepository {

    public List<Employee> getAllEmployees();

}
