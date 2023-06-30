package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.filter.PunchFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PunchRepository extends JpaRepository<Punch, Long>, CustomPunchRepository {


}
