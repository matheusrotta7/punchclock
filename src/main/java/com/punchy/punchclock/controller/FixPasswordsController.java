package com.punchy.punchclock.controller;

import com.punchy.punchclock.service.FixPasswordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fixpasswords")
@CrossOrigin
public class FixPasswordsController {

    @Autowired
    private FixPasswordsService fixPasswordsService;

    @GetMapping
    public void fixPasswords() {
        fixPasswordsService.fixPasswords();
    }
}
