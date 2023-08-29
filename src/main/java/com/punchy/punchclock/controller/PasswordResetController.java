package com.punchy.punchclock.controller;

import com.punchy.punchclock.exception.PunchException;
import com.punchy.punchclock.service.EmailService;
import com.punchy.punchclock.service.PasswordResetService;
import com.punchy.punchclock.vo.EmailBody;
import com.punchy.punchclock.vo.ErrorMessage;
import com.punchy.punchclock.vo.PasswordBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/passwordreset")
@CrossOrigin
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/start")
    public ResponseEntity<?> startPasswordResetProcess(@RequestBody EmailBody emailBody) {
        try {
            passwordResetService.startPasswordResetProcess(emailBody);
            return ResponseEntity.ok().build();
        } catch (PunchException punchException) {
            punchException.printStackTrace();
            return ResponseEntity.internalServerError().body(new ErrorMessage(punchException.getLocalizedMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/finish")
    public ResponseEntity<?> finishPasswordResetProcess(@RequestBody PasswordBody passwordBody) {
        try {
            passwordResetService.finishPasswordResetProcess(passwordBody);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
