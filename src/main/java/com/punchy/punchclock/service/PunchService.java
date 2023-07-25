package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.entity.PunchStatus;
import com.punchy.punchclock.exception.PunchException;
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
        punchBody.setPunchStatus(PunchStatus.NORMAL.toString());
        return punchRepository.save(punchBody);
    }

    /**
     * This method updates the punch on the database, the idea here is that
     * the employee can call Put changing the status of the punch to pending deletion
     * or to pending addition depending on whether he wants to create a new punch or
     * delete an old punch. If the user wants to update a current punch, he can delete it
     * and then create a new one, updating directly would create more complexity in the database
     *
     * @param punchBody with new Punch Information
     * @return updated punch
     */
    public Punch updatePunch(Punch punchBody) throws PunchException {
        if (punchRepository.existsById(punchBody.getId())) {
            return punchRepository.save(punchBody);
        } else {
            throw new PunchException("Punch that was to be updated didn't exist in the first place");
        }
    }
}
