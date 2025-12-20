package com.example.BookMyMovie.Service;

import com.example.BookMyMovie.Entity.Movie;
import com.example.BookMyMovie.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies()
    {
        List<Movie> movieList=movieRepository.findAll();
        return movieList;
    }

    public Movie getMovieById(String id)
    {
        Movie movie=movieRepository.findById(id).orElse(null);
        return movie;
    }

    public Movie saveMovie(Movie movie)
    {
     return movieRepository.save(movie);
    }

    public void saveAllMovie(List<Movie> movieList)
    {
        movieRepository.saveAll(movieList);
    }

    public void deleteMovieById(String id)
    {
        movieRepository.deleteById(id);
    }

}
