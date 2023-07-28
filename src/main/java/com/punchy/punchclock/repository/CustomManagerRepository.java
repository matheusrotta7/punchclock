package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Manager;
import com.punchy.punchclock.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomManagerRepository {

    public List<Manager> getAllManagers();

}
