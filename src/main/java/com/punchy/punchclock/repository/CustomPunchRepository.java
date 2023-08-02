package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.entity.PunchStatus;
import com.punchy.punchclock.filter.PunchFilter;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomPunchRepository {

    List<Punch> getPunchListGivenFilter(PunchFilter punchFilter);

    @Transactional
    void updateStatus(Long punchId, PunchStatus punchStatus);
}
