package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Company;
import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.CustomManagerRepository;
import com.punchy.punchclock.utils.DateUtils;
import com.punchy.punchclock.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;

public class CustomManagerRepositoryImpl implements CustomManagerRepository {

    @Autowired
    private DateUtils dateUtils;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Manager> getAllManagers(Long adminId) {
        List<Manager> result = new ArrayList<>();

        String queryString = "Select m.id, m.name, m.username, m.password, m.token, m.tokenExpiryDate, m.passwordResetToken from Manager m where 1=1 ";

        if (adminId != null) {
            queryString += " and m.admin.id = :adminId";
        }

        Query query = entityManager.createQuery(queryString);

        if (adminId != null) {
            query.setParameter("adminId", adminId);
        }

        List<Object[]> rows = query.getResultList();

        for (Object[] row: rows) {
            Manager curManager = new Manager();

            curManager.setId(parseLong(row[0].toString()));
            curManager.setName(StringUtils.nullSafeToString(row[1].toString()));
            curManager.setUsername(StringUtils.nullSafeToString(row[2]));
            curManager.setPassword(StringUtils.nullSafeToString(row[3]));
            curManager.setToken(StringUtils.nullSafeToString(row[4]));
            curManager.setTokenExpiryDate(row[5] != null ? dateUtils.stringToDate(StringUtils.nullSafeToString(row[5])) : null);
            curManager.setPasswordResetToken(StringUtils.nullSafeToString(row[6]));
            result.add(curManager);
        }

        return result;
    }

    @Override
    public void saveToken(String token, Date tokenExpiryDate, Person targetPerson) {
        String queryString = "Update Manager m set m.token=:token, m.tokenExpiryDate=:tokenExpiryDate where m.id=:managerId";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("token", token);
        query.setParameter("tokenExpiryDate", tokenExpiryDate);
        query.setParameter("managerId", targetPerson.getId());

        query.executeUpdate();
    }

    @Override
    public Company getManagersCompany(Long managerId) {
        String queryString = "from Company c " +
                "where c.id = (" +
                "select adm.company.id  from Admin adm where adm.id = (" +
                "select m.admin.id  from Manager m where m.id = :managerId))";

        Query query = entityManager.createQuery(queryString);
        query.setParameter("managerId", managerId);
        List<Company> companyList = query.getResultList();
        if (!companyList.isEmpty()) {
            return companyList.get(0);
        } else {
            return null;
        }

    }

    @Override
    public void savePasswordToken(String passwordToken, Person targetPerson) {
        String queryString = "Update Manager m set m.passwordResetToken=:passwordToken where m.id=:managerId";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("passwordToken", passwordToken);
        query.setParameter("managerId", targetPerson.getId());

        query.executeUpdate();
    }

}
