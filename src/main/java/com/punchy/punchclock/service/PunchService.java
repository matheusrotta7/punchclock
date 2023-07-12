package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.filter.PunchFilter;
import com.punchy.punchclock.repository.PunchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PunchService {

    @Autowired
    private PunchRepository punchRepository;


    public List<Punch> getPunchListGivenFilter(PunchFilter punchFilter) {
        return punchRepository.getPunchListGivenFilter(punchFilter);
    }

    public Punch getPunchById(Long id) {
        return punchRepository.findById(id).orElse(null);
    }

    public Punch createPunch(Punch punchBody) {
        return punchRepository.save(punchBody);
    }
}
