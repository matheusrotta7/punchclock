package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.CustomAdminRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.Date;

public class CustomAdminRepositoryImpl implements CustomAdminRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveToken(String token, Date tokenExpiryDate, Person targetPerson) {
        String queryString = "Update Admin a set a.token=:token, a.tokenExpiryDate=:tokenExpiryDate where a.id=:adminId";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("token", token);
        query.setParameter("tokenExpiryDate", tokenExpiryDate);
        query.setParameter("adminId", targetPerson.getId());

        query.executeUpdate();
    }

}
