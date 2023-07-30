package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.entity.Person;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomManagerRepository {

    public List<Manager> getAllManagers();

    @Transactional
    public void saveToken(String token, Date tokenExpiryDate, Person targetPerson);

}
