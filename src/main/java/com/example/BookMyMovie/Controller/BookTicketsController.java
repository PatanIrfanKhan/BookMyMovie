package com.example.BookMyMovie.Controller;

import com.example.BookMyMovie.Entity.Ticket;
import com.example.BookMyMovie.Entity.User;
import com.example.BookMyMovie.Service.BookTicketService;
import com.example.BookMyMovie.Service.KafkaService;
import com.example.BookMyMovie.Service.UserService;
import com.example.BookMyMovie.Utilities.JWTUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/BookTickets")
public class BookTicketsController {

    private Logger logger= LoggerFactory.getLogger(BookTicketsController.class);

    @Autowired
    private BookTicketService bookTicketService;

    @Autowired
    private JWTUtilities jwtUtilities;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private KafkaTemplate<String,Ticket> kafkaTemplate;

    @PostMapping()
    public ResponseEntity<?> createTicket(@RequestHeader("Authorization") String header, @RequestBody Ticket ticket)
    {
       logger.info("Ticket Creation Started");
        String jwttoken=header.substring(7);
        String username=jwtUtilities.extractUserName(jwttoken);
        User user=userService.getUserByName(username);
        logger.info("user : {}",user);
        if(user!=null)
        {
            Ticket newticket=bookTicketService.createTicket(user.getId(),ticket);
            logger.info("Ticket sending to email started");
            kafkaTemplate.send("Ticket-Booking-Details",user.getEmail(),newticket);
            logger.info("Ticket sending to email completed");
            logger.info("Ticket creation is completed");
            return new ResponseEntity<>(newticket, HttpStatus.OK);
        }

        logger.info("User: {} Not Found",user);

        return new ResponseEntity<>("User is Not Found",HttpStatus.NOT_FOUND);

    }
}

