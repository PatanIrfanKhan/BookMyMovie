package com.example.BookMyMovie.Controller;

import com.example.BookMyMovie.Entity.Movie;
import com.example.BookMyMovie.Entity.Theatre;
import com.example.BookMyMovie.Entity.Ticket;
import com.example.BookMyMovie.Entity.User;
import com.example.BookMyMovie.Service.LocationService;
import com.example.BookMyMovie.Service.UserService;
import com.example.BookMyMovie.Utilities.JWTUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private JWTUtilities jwtUtilities;

//    @PostMapping
//    public ResponseEntity<?> createNewUser(@RequestBody User user)
//    {
//        try
//        {
//            userService.createUser(user);
//            return new ResponseEntity<>(user,HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//    }


    @GetMapping()
    public ResponseEntity<?> getUserById(@RequestHeader("Authorization") String header)
    {
        String jwttoken=header.substring(7);
        String username=jwtUtilities.extractUserName(jwttoken);
        User user=userService.getUserByName(username);
//     User user=userService.getUserById(id);
        if(user!=null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/Get-all-users")
    public ResponseEntity<?> getAllUSers()
    {
        List<User> users=userService.getAllUsers();
        if(users!=null)
            return new ResponseEntity<>(users,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //-------------------- Not Needed because we are getting user details from authentication-------------------//

//    @GetMapping("/id/{id}")
//    public ResponseEntity<?> getUserById(@PathVariable String id)
//    {
//     User user=userService.getUserById(id);
//     if(user!=null)
//         return new ResponseEntity<>(user,HttpStatus.OK);
//     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//
//
//    @GetMapping("/name/{name}")
//    public ResponseEntity<?> getUserByName(@PathVariable String name)
//    {
//        User user=userService.getUserByName(name);
//        if(user!=null)
//            return new ResponseEntity<>(user,HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    //----------------------------------------------------------------------//

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String header,@RequestBody User user)
    {
        String jwttoken=header.substring(7);
        String username=jwtUtilities.extractUserName(jwttoken);
        User olduser=userService.getUserByName(username);
//        User olduser=userService.getUserById(user.getId());

        if(olduser!=null)
        {
            String email=(user.getEmail()!=null)?(user.getEmail()):(olduser.getEmail());
            olduser.setEmail(email);

            String mobile=(user.getMobilenumber()!=null)?(user.getMobilenumber()):(olduser.getMobilenumber());
            olduser.setMobilenumber(mobile);

            userService.modifyUser(olduser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUserById(@RequestHeader("Authorization") String header,@PathVariable String id)
    {
        String jwttoken=header.substring(7);
        String username=jwtUtilities.extractUserName(jwttoken);
        User user=userService.getUserByName(username);
//        User user=userService.getUserById(id);
        if(user!=null)
        {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //----------------------------Not Needed because we are getting user details from authentication-----------------//

//    @DeleteMapping("/id/{id}")
//    public ResponseEntity<?> deleteUserById(@PathVariable String id)
//    {
//        User user=userService.getUserById(id);
//        if(user!=null)
//        {
//            userService.deleteUserById(id);
//            return new ResponseEntity<>(HttpStatus.OK);
//            }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @DeleteMapping("/name/{name}")
//    public ResponseEntity<?> deleteUserByName(@PathVariable String name)
//    {
//        User user=userService.getUserByName(name);
//        if(user!=null) {
//            userService.deleteUserByName(name);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    //--------------------------------------------------------------------------------------------------//

    /// Location->Movie->Theatres

    @GetMapping("/locationname/{locationname}/movies")
    public ResponseEntity<?> getAllMoviesForLocation(@PathVariable String locationname)
    {
        List<Movie> movieList=locationService.getAllMoviesByLocationByName(locationname);
        if(movieList!=null) {
//            log.info("Movielist:", movieList);
            return new ResponseEntity<>(movieList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("locationname/{locationname}/movie/{moviename}/theatres")
    public ResponseEntity<?> getAllTheatresForMovieWithShowTimesForLocationByNames(@PathVariable String locationname,@PathVariable String moviename)
    {
        List<Theatre> theatres=locationService.getAllTheatresForMovieWithShowTimesForLocationByNames(locationname,moviename);

        return new ResponseEntity<>(theatres,HttpStatus.OK);
    }


    @GetMapping("locationid/{locationid}/movie/{movieid}/theatres")
    public ResponseEntity<?> getAllTheatresForMovieWithShowTimesForLocationByIds(@PathVariable String locationid,@PathVariable String movieid)
    {
        List<Theatre> theatres=locationService.getAllTheatresForMovieWithShowTimesForLocationByIds(locationid,movieid);

        return new ResponseEntity<>(theatres,HttpStatus.OK);
    }
    @GetMapping("/locations")
    public ResponseEntity<?> getAllLocations()
    {
        List<String> locationlist=locationService.getAllLocations();
        return new ResponseEntity<>(locationlist,HttpStatus.OK);













































































    }

    @GetMapping("/MyTickets")
    public ResponseEntity<?> getTicketsForUser(@RequestHeader("Authorization") String header)
    {
        String jwttoken=header.substring(7);
        String username=jwtUtilities.extractUserName(jwttoken);
        User user=userService.getUserByName(username);

        List<Ticket> tickets=user.getMyTickets();

        if(tickets!=null)
            return new ResponseEntity<>(tickets,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
