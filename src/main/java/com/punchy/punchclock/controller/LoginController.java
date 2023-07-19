package com.punchy.punchclock.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    //post method with body username password
    //returns token that allows user to do what it should do
    @PostMapping
    public Object login() {
        return null;
    }

}
