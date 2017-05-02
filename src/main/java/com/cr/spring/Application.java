package com.cr.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by harisa on 5/1/17.
 */
@SpringBootApplication
@ComponentScan("com.cr")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
