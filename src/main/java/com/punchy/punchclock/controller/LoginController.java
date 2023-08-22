package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.exception.PunchException;
import com.punchy.punchclock.service.LoginService;
import com.punchy.punchclock.vo.ErrorMessage;
import com.punchy.punchclock.vo.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Person loginBody) {
        try {
            logger.info("Received a login request");
            LoginResponse loginResponse = loginService.login(loginBody);
            return ResponseEntity.ok(loginResponse);
        } catch (PunchException pe) {
            pe.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new ErrorMessage(pe.getLocalizedMessage()));
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
