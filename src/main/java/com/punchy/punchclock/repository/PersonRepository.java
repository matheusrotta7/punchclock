package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Person;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository {

    @Transactional
    public void changePersonPassword(String passwordHash, Person targetPerson);
}
