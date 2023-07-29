package com.assemble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class AssembleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssembleApplication.class, args);
	}

}
