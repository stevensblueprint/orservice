package com.sarapis.orservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sarapis.orservice.entity"})
public class OrserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrserviceApplication.class, args);
	}

}
