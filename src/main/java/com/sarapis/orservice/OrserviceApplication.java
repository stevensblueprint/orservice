package com.sarapis.orservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class OrserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrserviceApplication.class, args);
	}
}
