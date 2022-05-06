package com.cacttus.rita.advanced.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class AdvancedWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvancedWebApplication.class, args);
	}

}
