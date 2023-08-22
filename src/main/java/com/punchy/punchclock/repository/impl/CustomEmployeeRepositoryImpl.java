package com.punchy.punchclock.repository.impl;

import com.punchy.punchclock.entity.Company;
import com.punchy.punchclock.entity.Employee;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.CustomEmployeeRepository;
import com.punchy.punchclock.utils.DateUtils;
import com.punchy.punchclock.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;

public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

    @Autowired
    private DateUtils dateUtils;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> result = new ArrayList<>();

        String queryString = "Select e.id, e.name, e.username, e.password, e.token, e.tokenExpiryDate from Employee e where 1=1";

        Query query = entityManager.createQuery(queryString);

        List<Object[]> rows = query.getResultList();

        for (Object[] row: rows) {
            Employee curEmployee = new Employee();

            curEmployee.setId(parseLong(row[0].toString()));
            curEmployee.setName(StringUtils.nullSafeToString(row[1]));
            curEmployee.setUsername(StringUtils.nullSafeToString(row[2]));
            curEmployee.setPassword(StringUtils.nullSafeToString(row[3]));
            curEmployee.setToken(StringUtils.nullSafeToString(row[4]));
            curEmployee.setTokenExpiryDate(row[5] != null ? dateUtils.stringToDate(StringUtils.nullSafeToString(row[5])) : null);

            result.add(curEmployee);
        }

        return result;
    }

    @Override
    public void saveToken(String token, Date tokenExpiryDate, Person targetPerson) {
        String queryString = "Update Employee e set e.token=:token, e.tokenExpiryDate=:tokenExpiryDate where e.id=:employeeId";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("token", token);
        query.setParameter("tokenExpiryDate", tokenExpiryDate);
        query.setParameter("employeeId", targetPerson.getId());

        query.executeUpdate();
    }

    @Override
    public List<Employee> getAllEmployeesOfManager(Long managerId) {

        String queryString = "from Employee e where e.manager.id = :managerId";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("managerId", managerId);

        return query.getResultList();
    }

    @Override
    public Company getEmployeesCompany(Long employeeId) {
        String queryString = "from Company c " +
                "where c.id = (" +
                "select adm.company.id  from Admin adm where adm.id = (" +
                "select m.admin.id  from Manager m where m.id = (" +
                "select e.manager.id from Employee e where e.id = :employeeId)))";

        Query query = entityManager.createQuery(queryString);
        query.setParameter("employeeId", employeeId);
        List<Company> companyList = query.getResultList();
        if (!companyList.isEmpty()) {
            return companyList.get(0);
        } else {
            return null;
        }
    }


    }
