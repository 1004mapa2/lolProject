package com.lol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LolApplication {

	public static void main(String[] args) {
		SpringApplication.run(LolApplication.class, args);
	}

}
