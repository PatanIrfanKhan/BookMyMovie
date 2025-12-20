package com.example.BookMyMovie.Controller;

import com.example.BookMyMovie.Entity.Ticket;
import com.example.BookMyMovie.Entity.User;
import com.example.BookMyMovie.Service.BookTicketService;
import com.example.BookMyMovie.Service.KafkaService;
import com.example.BookMyMovie.Service.UserService;
import com.example.BookMyMovie.Utilities.JWTUtilities;
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
        String jwttoken=header.substring(7);
        String username=jwtUtilities.extractUserName(jwttoken);
        User user=userService.getUserByName(username);
        if(user!=null)
        {
            Ticket newticket=bookTicketService.createTicket(user.getId(),ticket);
            kafkaTemplate.send("Ticket-Booking-Details",user.getEmail(),newticket);
            return new ResponseEntity<>(newticket, HttpStatus.OK);
        }

        return new ResponseEntity<>("User is Not Found",HttpStatus.NOT_FOUND);

    }
}

