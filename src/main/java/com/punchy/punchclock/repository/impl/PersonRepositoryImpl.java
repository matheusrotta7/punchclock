package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void changePersonPassword(String passwordHash, Person targetPerson) {
        String className = targetPerson.getClass().getSimpleName();
        String queryString = "Update " + className + " p set p.password=:passwordHash where p.id=:personId";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("passwordHash", passwordHash);
        query.setParameter("personId", targetPerson.getId());

        query.executeUpdate();
    }

    @Override
    @Transactional
    public void deletePasswordResetToken(Person targetPerson) {
        String className = targetPerson.getClass().getSimpleName();
        String queryString = "Update " + className + " p set p.passwordResetToken=:passwordResetToken where p.id=:personId";
        Query query = entityManager.createQuery(queryString);
        //set password reset token to null so that no one else can use this token for password reset
        //todo: check later if malicious user can use null to change someone's password
        query.setParameter("passwordResetToken", null);
        query.setParameter("personId", targetPerson.getId());

        query.executeUpdate();
    }
}
