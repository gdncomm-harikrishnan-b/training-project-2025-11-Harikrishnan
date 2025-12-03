package com.blibi.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main application class for the Product Service microservice.
 * 
 * This class serves as the entry point for the Spring Boot application and enables
 * key features including MongoDB repositories and caching support.
 * 
 * @EnableMongoRepositories - Enables Spring Data MongoDB repositories, allowing
 *                          automatic implementation of repository interfaces
 * @SpringBootApplication - Combines @Configuration, @EnableAutoConfiguration, and
 *                        @ComponentScan annotations to bootstrap the application
 * @EnableCaching - Enables Spring's caching abstraction, allowing methods to be
 *                 annotated with caching annotations (uses Redis in this application)
 * 
 * @author HKB
 *
 */
@EnableMongoRepositories
@SpringBootApplication
@EnableCaching
public class ProductApplication {

	/**
	 * Main method that starts the Spring Boot application.
	 * 
	 * This method initializes the Spring ApplicationContext, starts the embedded
	 * Tomcat server, and loads all configured beans and components.
	 * 
	 * @param args Command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

}
