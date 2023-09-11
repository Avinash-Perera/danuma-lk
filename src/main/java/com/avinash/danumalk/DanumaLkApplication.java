package com.avinash.danumalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.avinash.danumalk.model")
public class DanumaLkApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanumaLkApplication.class, args);
	}

}
