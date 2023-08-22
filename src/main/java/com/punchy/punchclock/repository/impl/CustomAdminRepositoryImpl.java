package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Company;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.CustomAdminRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.Date;
import java.util.List;

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

    @Override
    public Company getAdminsCompany(Long adminId) {
        String queryString = "from Company c " +
                "where c.id = (" +
                "select adm.company.id  from Admin adm where adm.id = :adminId)";

        Query query = entityManager.createQuery(queryString);
        query.setParameter("adminId", adminId);
        List<Company> companyList = query.getResultList();
        if (!companyList.isEmpty()) {
            return companyList.get(0);
        } else {
            return null;
        }
    }



}
