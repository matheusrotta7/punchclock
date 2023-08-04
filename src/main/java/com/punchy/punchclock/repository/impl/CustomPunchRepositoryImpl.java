package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.entity.PunchStatus;
import com.punchy.punchclock.filter.PunchFilter;
import com.punchy.punchclock.repository.CustomPunchRepository;
import com.punchy.punchclock.utils.DateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

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

        Date monthBegin = null;
        Date monthEnd = null;

        if (punchFilter.getMonth() != null) {
            monthBegin = dateUtils.getFirstMomentOfMonth(punchFilter.getMonth(), punchFilter.getYear());
            monthEnd = dateUtils.getLastMomentOfMonth(punchFilter.getMonth(), punchFilter.getYear());
            queryString += " and p.timestamp < :monthEnd";
            queryString += " and p.timestamp > :monthBegin";
        }

        PunchStatus punchStatus = null;
        if (punchFilter.getPunchStatus() != null) {
            if (punchFilter.getPunchStatus().equals("ALL_PENDING")) { //wants to fetch all pending actions
                queryString += " and p.punchStatus like 'PENDING%'";
            } else { //wants to fetch specific pending action
                punchStatus = PunchStatus.valueOf(punchFilter.getPunchStatus());
                queryString += " and p.punchStatus = :punchStatus";
            }
        }

        Query query = entityManager.createQuery(queryString);

        if (punchFilter.getMonth() != null) {
            query.setParameter("monthBegin", monthBegin);
            query.setParameter("monthEnd", monthEnd);
        }
        if (punchFilter.getPunchStatus() != null && !punchFilter.getPunchStatus().equals("ALL_PENDING")) {
            query.setParameter("punchStatus", punchStatus.name());
        }

        List<Punch> punchList = (List<Punch>) query.getResultList();

        return punchList;
    }

    @Override
    public void updateStatus(Long punchId, PunchStatus punchStatus) {
        String queryString = "Update Punch p set p.punchStatus = :newPunchStatus where p.id = :punchId";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("newPunchStatus", punchStatus.name());
        query.setParameter("punchId", punchId);
        query.executeUpdate();
    }
}
