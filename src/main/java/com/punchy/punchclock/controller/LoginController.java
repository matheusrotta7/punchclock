package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.exception.PunchException;
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

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody Person loginBody) {
        try {
            LoginResponse loginResponse = loginService.login(loginBody);
            return ResponseEntity.ok(loginResponse);
        } catch (PunchException pe) {
            pe.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<LoginResponse> recoverUserThroughToken(@RequestHeader("token") String token) {
        try {
            LoginResponse loginResponse = loginService.recoverUserViaToken(token);
            return ResponseEntity.ok(loginResponse);
        } catch (PunchException pe) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


}
