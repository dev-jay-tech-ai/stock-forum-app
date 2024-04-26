package dev.be.servicenewsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ServiceNewsFeedApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceNewsFeedApplication.class, args);
	}
}
