package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.filter.PunchFilter;
import com.punchy.punchclock.repository.CustomPunchRepository;
import com.punchy.punchclock.utils.DateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Month;
import java.util.Date;
import java.util.List;

public class CustomPunchRepositoryImpl implements CustomPunchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DateUtils dateUtils;

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
            Date monthBegin = dateUtils.getFirstMomentOfMonth(punchFilter.getMonth(), punchFilter.getYear());
            Date monthEnd = dateUtils.getLastMomentOfMonth(punchFilter.getMonth(), punchFilter.getYear());
            queryString += " and p.timestamp < " + monthEnd;
            queryString += " and p.timestamp > " + monthBegin;
        }

        Query query = entityManager.createQuery(queryString);
        List<Punch> punchList = (List<Punch>) query.getResultList();

        return punchList;
    }
}
