package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
