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
			System.loadLibrary("libpdnative");
			logger.info("Loaded libpdnative using loadLibrary");
		} catch (UnsatisfiedLinkError e) {
			logger.error("Failed to load libpdnative using loadLibrary");
			logger.info("Attempting to load libpdnative using System.load");
			// Fallback to hardcoded path or other strategies if necessary.
			System.load("/Users/giuseppedesiato/Documents/tuning-systems/libs/libpdnative.dylib");
			logger.info("Loaded libpdnative using System.load");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(PureSynthApplication.class, args);
	}
}
