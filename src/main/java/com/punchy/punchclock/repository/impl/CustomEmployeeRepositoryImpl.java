package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.repository.CustomEmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;

public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> result = new ArrayList<>();

        String queryString = "Select e.id, e.name from Employee e where 1=1";

        Query query = entityManager.createQuery(queryString);

        List<Object[]> rows = query.getResultList();

        for (Object[] row: rows) {
            Employee curEmployee = new Employee();

            curEmployee.setId(parseLong(row[0].toString()));
            curEmployee.setName(row[1].toString());

            result.add(curEmployee);
        }

        return result;
    }
}
