package com.example.BookMyMovie;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookMyMovieApplication {

	@Value("${spring.security.auth2.client.registration.google.client-id}")
	private String client_id;

	@Value("${spring.security.auth2.client.registration.google.client-secret}")
	private String client_secret;

	Logger logger= LoggerFactory.getLogger(BookMyMovieApplication.class);

	public static void main(String[] args)
	{
		SpringApplication.run(BookMyMovieApplication.class, args);

	}

//	private static final Logger log = LogManager.getLogger(BookMyMovieApplication.class);

	@PostConstruct
	public void testLog() {
		logger.info("cleinrt id: {} and clent-secret: {}",client_id,client_secret);
		logger.info("BOOKMYMOVIE APPLICATION LOG TEST");
	}

}
