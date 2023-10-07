package com.desiato.puresynth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PureSynthApplication {
	private static final Logger logger = LoggerFactory.getLogger(PureSynthApplication.class);


	static {
		logger.info("Attempting to load libpdnative using loadLibrary");
		try {
			// Where does Java look for the library?
			logger.info("java.library.path: " + System.getProperty("java.library.path"));

			System.loadLibrary("pdnative");
			logger.info("Loaded libpdnative using loadLibrary");
		} catch (UnsatisfiedLinkError e) {
			logger.error("Failed to load libpdnative using loadLibrary");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(PureSynthApplication.class, args);

		System.out.println("java.library.path: " + System.getProperty("java.library.path"));


	}
}
