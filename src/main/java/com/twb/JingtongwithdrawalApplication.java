package com.twb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@ComponentScan
public class JingtongwithdrawalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JingtongwithdrawalApplication.class, args);
	}
}
