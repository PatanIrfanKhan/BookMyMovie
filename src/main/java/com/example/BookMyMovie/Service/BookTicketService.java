package com.example.BookMyMovie.Service;

import com.example.BookMyMovie.Entity.Ticket;
import com.example.BookMyMovie.Entity.User;
import com.example.BookMyMovie.Repository.TicketsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class BookTicketService {

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
    User user=userService.getUserById(userid);
    ticket.setUserid(userid);
//    ticket.setSeats(Arrays.asList("Diamond - A23","Diamond - A24"));
    ticketsRepository.save(ticket);
    user.getMyTickets().add(ticket);
    userService.saveUser(user);
    return ticket;
}
}
