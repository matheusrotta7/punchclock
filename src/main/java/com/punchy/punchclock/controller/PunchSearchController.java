package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.filter.PunchFilter;
import com.punchy.punchclock.service.PunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/punchSearch")
public class PunchSearchController {

    @Autowired
    private PunchService punchService;

    @PostMapping
    public List<Punch> getPunchListGivenFilter(@RequestBody PunchFilter punchFilter) {
        return punchService.getPunchListGivenFilter(punchFilter);
    }
}
