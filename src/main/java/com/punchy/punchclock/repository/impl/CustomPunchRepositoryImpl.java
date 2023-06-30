package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.filter.PunchFilter;
import com.punchy.punchclock.repository.CustomPunchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.Month;
import java.util.Date;
import java.util.List;

public class CustomPunchRepositoryImpl implements CustomPunchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public CustomPunchRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Punch> getPunchListGivenFilter(PunchFilter punchFilter) {
        String queryString = "Select p from Punch p where 1=1";
        if (punchFilter.getEmployeeId() != null) {
            queryString += " and p.employee.id = " + punchFilter.getEmployeeId();

        }
        if (punchFilter.getMonth() != null) {
            int month = punchFilter.getMonth();
            Date monthBegin = Date.from(Month.from(Tem))
            queryString += " and p.timestamp < "
        }
    }
}
