package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.filter.PunchFilter;
import com.punchy.punchclock.service.PunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punchSearch")
@CrossOrigin
public class PunchSearchController {

    @Autowired
    private PunchService punchService;

    @PostMapping
    public List<Punch> getPunchListGivenFilter(@RequestBody PunchFilter punchFilter) {
        return punchService.getPunchListGivenFilter(punchFilter);
    }
}
