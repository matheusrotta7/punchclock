package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Punch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PunchRepository extends JpaRepository<Punch, Long>, CustomPunchRepository {


}
