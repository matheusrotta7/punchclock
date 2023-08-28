package com.punchy.punchclock.service;

import com.punchy.punchclock.exception.PunchException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final String sendgridApiKey = "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";

    public void sendPasswordResetEmail(String destinyEmailAddress, String temporaryPageLink) throws PunchException {
        Email from = new Email("support@punchy.app"); //should always be from support
        String subject = "Password Reset Request";
        Email to = new Email(destinyEmailAddress);
        Mail mail = new Mail();

        mail.addContent(new Content("text/plain", "You have requested a password reset, here's the link to do it: \n" + temporaryPageLink));
        mail.setFrom(from);
        mail.setSubject(subject);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info("response status code: " + response.getStatusCode());
            logger.info("response body: " + response.getBody());
            logger.info("response headers: " + response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new PunchException("There was an error when trying to send password reset email");
        }
    }

}
