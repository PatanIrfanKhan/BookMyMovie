package com.example.BookMyMovie.Service;

import com.example.BookMyMovie.Entity.Ticket;
import com.example.BookMyMovie.Entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class KafkaService {

   @Autowired
   private EmailService emailService;

   @Autowired
   private StringRedisTemplate stringRedisTemplate;

   @Autowired
   private UserService userService;

   @KafkaListener(topics = "User-Login-OTP",groupId = "User-Login-Service")
    public void loginWithOTP(User user)
    {
        int OTP=(int)(Math.random()*1000000);
        String subject="Login via OTP";
        String message="Hi! "+user.getUsername()+"\n Your Login OTP is " +OTP;
        emailService.sendMail(user.getEmail(),subject,message);
        stringRedisTemplate.opsForValue().set(user.getEmail(),String.valueOf(OTP),180, TimeUnit.SECONDS);
        log.info("OTP(Kakfa Service) is : {}",OTP);
    }
    @KafkaListener(topics = "User-Welcome-Message",groupId = "User-Login-Service")
    public void sendWelcomeMessage(User user)
    {
        String subject="Welcome to BookMyMovie";
        String message=" Thank you for choosing BookMyMovie! \n\n Your user id is : "+user.getId()+" \n Your username is " +user.getUsername()+" \n\nBook Your Tickets on BookMyMovie for your favourite movie and have fun!";
        emailService.sendMail(user.getEmail(),subject,message);
    }

    @KafkaListener(topics = "Ticket-Booking-Details",groupId = "Booking-Details")
    public void sendBookingDetails(Ticket ticket)
    {
        String subject="Movie Ticket Booking Confirmation";
        String message="Thank you for choosing and booking the movie Tickets on BookMyMovie here are your ticket detials.\n\n" +
                "Your Ticket id : "+ticket.getTicketid()+"\n"+
                "Movie Name : "+ticket.getMoviename()+"\n"+
                "Theatre Name : "+ticket.getTheatrename()+"\n"+
                "Location and Address : "+ticket.getLocationname()+", "+ticket.getAddress()+"\n"+
                "ShowTime : "+ticket.getShowtime()+"\n"+
                "Seats : "+ticket.getSeats()+"\n"+
                "\nPlease reach out to the theatre before 15 Minutes of Show Time."+"\n"+
                "\nThank You Once Again for choosing BookMyMovieApp and Enjoy the Movie!!!";
        String useremail=userService.getUserById(ticket.getUserid()).getEmail();
        emailService.sendMail(useremail,subject,message);
    }
}
