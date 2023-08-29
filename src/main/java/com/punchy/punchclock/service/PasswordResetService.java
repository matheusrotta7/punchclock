package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Admin;
import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.exception.PunchException;
import com.punchy.punchclock.vo.EmailBody;
import com.punchy.punchclock.vo.PasswordBody;
import com.punchy.punchclock.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class PasswordResetService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

    public void startPasswordResetProcess(EmailBody emailBody) throws PunchException {
        //recover user from login methods that search person by email
        //if not found, just return 404 and tell them no one with that email was found
        Person targetPerson = loginService.getTargetPersonByUsername(emailBody.getDestinyEmailAddress());
        if (targetPerson == null) {
            throw new PunchException("Person not found");
        }

        //if found, set new password reset token for that person in database
        String passwordResetToken = UUID.randomUUID().toString();
        savePasswordTokenInDatabase(passwordResetToken, targetPerson);

        //create temporary page link string based on current frontend dynamic route format
        String temporaryPageLink = "https://punchy.app/passwordresetscreen/" + passwordResetToken;
        //send password reset email with that link to user's  email
        emailService.sendPasswordResetEmail(emailBody.getDestinyEmailAddress(), temporaryPageLink);

        //person will access the link and token will already be set in frontend variable
        //then person will add two passwords and frontend will send us a payload with a token and a passwordHash



    }

    public void finishPasswordResetProcess(PasswordBody passwordBody) {
        //search for token in database
        //if not found, return error
        //if found, then change person's password with passwordHash that was given

    }

    private void savePasswordTokenInDatabase(String passwordToken, Person targetPerson) throws PunchException {
        String personClassStr = targetPerson.getClass().getSimpleName().toUpperCase();
        Role personRole = Role.valueOf(personClassStr);
        switch (personRole) {
            case ADMIN -> adminService.savePasswordToken(passwordToken, targetPerson);
            case MANAGER -> managerService.savePasswordToken(passwordToken, targetPerson);
            case EMPLOYEE -> employeeService.savePasswordToken(passwordToken, targetPerson);
            default -> throw new PunchException("Wrong Role while saving passwordToken to database");
        }
    }
}