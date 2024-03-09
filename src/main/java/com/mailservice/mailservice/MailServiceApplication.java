package com.mailservice.mailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MailServiceApplication {

	public static void main(String[] args) {
        String userHome = System.getProperty("user.home");

        // Print the user's home directory
        System.out.println("User's Home Directory: " + userHome);
		SpringApplication.run(MailServiceApplication.class, args);
	}

}
