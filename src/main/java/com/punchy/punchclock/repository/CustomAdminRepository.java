package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Person;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CustomAdminRepository {

    @Transactional
    void saveToken(String token, Date tokenExpiryDate, Person targetPerson);
}
