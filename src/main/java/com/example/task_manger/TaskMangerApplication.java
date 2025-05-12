package com.example.task_manger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class TaskMangerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskMangerApplication.class, args);
	}

}
