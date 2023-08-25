package com.punchy.punchclock.controller;

import com.punchy.punchclock.exception.PunchException;
import com.punchy.punchclock.service.EmailService;
import com.punchy.punchclock.vo.EmailBody;
import com.punchy.punchclock.vo.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@CrossOrigin
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> sendEmail(@RequestBody EmailBody emailBody) {
        try {
            emailService.sendPasswordResetEmail(emailBody.getDestinyEmailAddress(), emailBody.getTemporaryPageLink());
            return ResponseEntity.ok().build();
        } catch (PunchException punchException) {
            punchException.printStackTrace();
            return ResponseEntity.internalServerError().body(new ErrorMessage(punchException.getLocalizedMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
