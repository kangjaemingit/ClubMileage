package com.example.clubmileage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ClubmileageApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClubmileageApplication.class, args);
	}

}
