package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.CustomEmployeeRepository;
import com.punchy.punchclock.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> result = new ArrayList<>();

        String queryString = "Select e.id, e.name, e.username, e.password from Employee e where 1=1";

        Query query = entityManager.createQuery(queryString);

        List<Object[]> rows = query.getResultList();

        for (Object[] row: rows) {
            Employee curEmployee = new Employee();

            curEmployee.setId(parseLong(row[0].toString()));
            curEmployee.setName(StringUtils.nullSafeToString(row[1]));
            curEmployee.setUsername(StringUtils.nullSafeToString(row[2]));
            curEmployee.setPassword(StringUtils.nullSafeToString(row[3]));

            result.add(curEmployee);
        }

        return result;
    }

}
