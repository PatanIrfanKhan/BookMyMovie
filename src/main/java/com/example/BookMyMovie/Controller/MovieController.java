package com.example.BookMyMovie.Controller;

import com.example.BookMyMovie.Entity.Movie;
import com.example.BookMyMovie.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<?> AddMovieToLocation()
    {
        List<Movie> movieList=movieService.getAllMovies();
        if(movieList!=null)
            return new ResponseEntity<>(movieList, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @DeleteMapping
//    public ResponseEntity<?> deleteMovie
}
