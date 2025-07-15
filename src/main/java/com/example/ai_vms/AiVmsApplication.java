package com.example.ai_vms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiVmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiVmsApplication.class, args);
	}

}
