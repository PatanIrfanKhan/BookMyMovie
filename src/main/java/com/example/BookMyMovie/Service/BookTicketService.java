package com.example.BookMyMovie.Service;

import com.example.BookMyMovie.Entity.Ticket;
import com.example.BookMyMovie.Entity.User;
import com.example.BookMyMovie.Repository.TicketsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class BookTicketService {

    Logger logger= LoggerFactory.getLogger(BookTicketService.class);

    @Autowired
    private TicketsRepository ticketsRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TheatreService theatreService;

    @Autowired
    private UserService userService;

    public Ticket getTicketDetailsById(String ticketid)
    {
        Ticket ticket=ticketsRepository.findById(ticketid).orElse(null);
        return ticket;
    }

//    public Ticket createTicket(String userid,String locationid, String movieid, String thatreid, List<String> seats, LocalDateTime showtime)
//    {
//        Ticket ticket=new Ticket();
//        Location location=locationService.getLocation(locationid);
//        ticket.setBookedtimeanddate(LocalDateTime.now());
//        ticket.setLocationname(location.getLocationname());
//        ticket.setMoviename(movieService.getMovieById(movieid).getMoviename());
//        ticket.setTheatrename(theatreService.getTheatreById(thatreid).getTheatrename());
//        ticket.setSeats(seats);
//        ticket.setShowtime(showtime);
//        ticketsRepository.save(ticket);
//
//        User user=userService.getUserById(userid);
//        user.getMyTickets().add(ticket);
//        userService.saveUser(user);
//        return ticket;
//    }
public Ticket createTicket(String userid,Ticket ticket)
{
    logger.info("Ticket Generation started in service");
    User user=userService.getUserById(userid);
    logger.info("User in service: {}",user);
    ticket.setUserid(userid);
//    ticket.setSeats(Arrays.asList("Diamond - A23","Diamond - A24"));
    ticketsRepository.save(ticket);
    logger.info("Ticket created in service: {}",ticket);
    user.getMyTickets().add(ticket);
    userService.saveUser(user);
    logger.info("Ticket added to user  : {} and ticket is {}",user,ticket);
    return ticket;
}
}
