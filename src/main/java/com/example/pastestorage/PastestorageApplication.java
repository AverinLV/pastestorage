package com.example.pastestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PastestorageApplication {
	public static void main(String[] args) {
		SpringApplication.run(PastestorageApplication.class, args);
	}

}
