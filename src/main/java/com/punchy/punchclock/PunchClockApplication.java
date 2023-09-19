package com.punchy.punchclock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PunchClockApplication {

	public static void main(String[] args) {
		SpringApplication.run(PunchClockApplication.class, args);
	}

}
