package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.service.LoginService;
import com.punchy.punchclock.vo.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginService loginService;

    //post method with body username password
    //returns token that allows user to do what it should do
    //for now it will return a string, but this will be changed later
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody Person loginBody) {
        try {
            LoginResponse loginResponse = loginService.login(loginBody);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
