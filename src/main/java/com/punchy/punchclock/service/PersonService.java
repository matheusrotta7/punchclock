package com.punchy.punchclock.service;

import com.punchy.punchclock.entity.Person;
import com.punchy.punchclock.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PersonService {

    private static Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    public void removeOldCookiesFromPerson(Person person) {
        Date curDate = new Date();
        Date tokenExpiryDate = person.getTokenExpiryDate();
        if (tokenExpiryDate != null && curDate.after(tokenExpiryDate)) {
            logger.info("Removing old login token from person " + person.getName());
            personRepository.deleteLoginToken(person);
        }

    }
}
