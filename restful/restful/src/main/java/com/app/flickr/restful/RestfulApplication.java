package com.app.flickr.restful;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RestfulApplication implements CommandLineRunner {

	
	public static void main(String[] args) {
		SpringApplication.run(RestfulApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("============================= connection success >>>>>>>>>>>>>>>>>>>>>>>>>>");
		
	}

}
