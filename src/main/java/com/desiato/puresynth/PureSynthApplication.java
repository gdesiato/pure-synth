package com.desiato.puresynth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PureSynthApplication {

	public static void main(String[] args) {

		System.load("/Users/giuseppedesiato/Documents/tuning-systems/libs/libpdnative.jnilib");
		//System.loadLibrary("libs/libpdnative");
		SpringApplication.run(PureSynthApplication.class, args);
	}

}
