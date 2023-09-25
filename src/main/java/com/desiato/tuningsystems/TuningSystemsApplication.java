package com.desiato.tuningsystems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TuningSystemsApplication {

	public static void main(String[] args) {

		System.loadLibrary("libs/libpdnative");
		SpringApplication.run(TuningSystemsApplication.class, args);
	}

}
