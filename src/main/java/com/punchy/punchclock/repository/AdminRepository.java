package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Admin;
import com.punchy.punchclock.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, CustomAdminRepository {

}
