package com.example.BookMyMovie.Controller;

import com.example.BookMyMovie.Entity.Location;
import com.example.BookMyMovie.Entity.Movie;
import com.example.BookMyMovie.Entity.MovieRequest;
import com.example.BookMyMovie.Entity.Theatre;
import com.example.BookMyMovie.Service.LocationService;
import com.example.BookMyMovie.Service.MovieService;
import com.example.BookMyMovie.Service.TheatreService;
import com.example.BookMyMovie.Service.UserService;
import com.example.BookMyMovie.Utilities.JWTUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@Slf4j
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TheatreService theatreService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtilities jwtUtilities;

    @PostMapping
    public ResponseEntity<?> AddLocation(@RequestBody Location location)
    {
        try {
            Location savedlocation= locationService.addLocation(location);
            return new ResponseEntity<>(savedlocation, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Unable to add location due to "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{LocationId}/theatre")
    public ResponseEntity<?> addThetresToLocation(@PathVariable String LocationId, @RequestBody Theatre theatre)
    {
//        Location location=locationService.getLocation(LocationId);
//        List<Theatre> theatreList=location.getTheatreList();

        Location location=locationService.getLocation(LocationId);
        List<Theatre> theatreList=location.getTheatreList();

        theatre.setLocation(location);

        theatreList.add(theatre);

        location.setTheatreList(theatreList);

        Theatre savedtheatre= theatreService.saveTheatre(theatre);
        locationService.addTheatresToLocation(location);

        return new ResponseEntity<>(savedtheatre,HttpStatus.OK);
    }

    @PostMapping("/{locationId}/theatre/{theatreId}/Movie")
    public ResponseEntity<?> addMoviestoTheteresForLocation(@PathVariable String locationId,@PathVariable String theatreId,@RequestBody MovieRequest movie)
    {

        Movie savedmovie=locationService.addMovieToTheatre(locationId,theatreId,movie);
        Location location=locationService.getLocation(locationId);
//        savedmovie.set

        if(movie!=null)
            return new ResponseEntity<>(savedmovie,HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/name/{locationname}/movies")
    public ResponseEntity<?> getAllMoviesForLocation(@PathVariable String locationname)
    {
        List<Movie> movieList=locationService.getAllMoviesByLocationByName(locationname);
        if(movieList!=null) {
//            log.info("Movielist:", movieList);
            return new ResponseEntity<>(movieList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("name/{locationname}/movie/{moviename}/theatres")
    public ResponseEntity<?> getAllTheatresForMovieWithShowTimesForLocationByNames(@PathVariable String locationname,@PathVariable String moviename)
    {
        List<Theatre> theatres=locationService.getAllTheatresForMovieWithShowTimesForLocationByNames(locationname,moviename);

        return new ResponseEntity<>(theatres,HttpStatus.OK);
    }
    @GetMapping("id/{locationid}/movie/{movieid}/theatres")
    public ResponseEntity<?> getAllTheatresForMovieWithShowTimesForLocationByIds(@PathVariable String locationid,@PathVariable String movieid)
    {
        List<Theatre> theatres=locationService.getAllTheatresForMovieWithShowTimesForLocationByIds(locationid,movieid);

        return new ResponseEntity<>(theatres,HttpStatus.OK);
    }

    @GetMapping("/user/name/locations")
    public ResponseEntity<?> getAllLocations()
    {
        List<String> locationlist=locationService.getAllLocations();
        return new ResponseEntity<>(locationlist,HttpStatus.OK);
    }
}
