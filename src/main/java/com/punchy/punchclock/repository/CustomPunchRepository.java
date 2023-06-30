package com.punchy.punchclock.repository;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.filter.PunchFilter;

import java.util.List;

public interface CustomPunchRepository {

    List<Punch> getPunchListGivenFilter(PunchFilter punchFilter);
}
