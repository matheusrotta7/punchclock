package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.CustomManagerRepository;
import com.punchy.punchclock.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class CustomManagerRepositoryImpl implements CustomManagerRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Manager> getAllManagers() {
        List<Manager> result = new ArrayList<>();

        String queryString = "Select m.id, m.name, m.username, m.password from Manager m where 1=1";

        Query query = entityManager.createQuery(queryString);

        List<Object[]> rows = query.getResultList();

        for (Object[] row: rows) {
            Manager curManager = new Manager();

            curManager.setId(parseLong(row[0].toString()));
            curManager.setName(StringUtils.nullSafeToString(row[1].toString()));
            curManager.setUsername(StringUtils.nullSafeToString(row[2]));
            curManager.setPassword(StringUtils.nullSafeToString(row[3]));

            result.add(curManager);
        }

        return result;
    }

}
