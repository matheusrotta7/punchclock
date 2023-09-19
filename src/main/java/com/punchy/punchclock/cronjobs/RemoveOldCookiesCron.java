package com.punchy.punchclock.cronjobs;

import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.PersonRepository;
import com.punchy.punchclock.service.LoginService;
import com.punchy.punchclock.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RemoveOldCookiesCron {

    private static final Logger logger = LoggerFactory.getLogger(RemoveOldCookiesCron.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void removeOldCookies() {
        // remove old cookies
        // this cron job will have to scan all Person extending tables and check if the token has expired,
        // i.e.: if curDate > tokenExpiryDate, then if this is true it must set the token to null
        logger.info("Starting remove old cookies job...");
        List<Person> personList = loginService.getAllPeople();

        for (Person person : personList) {
            removeOldCookiesFromPerson(person);
        }
    }

    private void removeOldCookiesFromPerson(Person person) {
        logger.info("Checking if we need to remove any old cookies from person " + person.getName());
        personService.removeOldCookiesFromPerson(person);
    }

}
