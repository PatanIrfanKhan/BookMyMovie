package com.example.BookMyMovie;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookMyMovieApplication {

	Logger logger= LoggerFactory.getLogger(BookMyMovieApplication.class);

	public static void main(String[] args)
	{
		SpringApplication.run(BookMyMovieApplication.class, args);
	}

//	private static final Logger log = LogManager.getLogger(BookMyMovieApplication.class);

	@PostConstruct
	public void testLog() {
		logger.info("BOOKMYMOVIE APPLICATION LOG TEST");
	}

}
