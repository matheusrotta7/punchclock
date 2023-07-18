package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.repository.CustomManagerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class CustomManagerRepositoryImpl implements CustomManagerRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Manager> getAllManagers() {
        List<Manager> result = new ArrayList<>();

        String queryString = "Select m.id, m.name from Manager m where 1=1";

        Query query = entityManager.createQuery(queryString);

        List<Object[]> rows = query.getResultList();

        for (Object[] row: rows) {
            Manager curManager = new Manager();

            curManager.setId(parseLong(row[0].toString()));
            curManager.setName(row[1].toString());

            result.add(curManager);
        }

        return result;
    }
}
