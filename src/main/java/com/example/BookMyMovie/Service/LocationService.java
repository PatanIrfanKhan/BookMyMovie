package com.example.BookMyMovie.Service;

import com.example.BookMyMovie.Entity.Location;
import com.example.BookMyMovie.Entity.Movie;
import com.example.BookMyMovie.Entity.MovieRequest;
import com.example.BookMyMovie.Entity.Theatre;
import com.example.BookMyMovie.Repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TheatreService theatreService;

    public Location addLocation(Location location)
    {
     return locationRepository.save(location);
    }

    public Location getLocation(String id)
    {
        Location location=locationRepository.findById(id).orElse(null);
        return location;
    }

    public List<String> getAllLocations()
    {
        List<Location> locationList=locationRepository.findAll();
        List<String> locationnmes=locationList.stream().distinct().map(x->x.getLocationname()).collect(Collectors.toList());
        return locationnmes;

    }


//    public void updateLocation(Location location)
//    {
//        locationRepository.save(location);
////        List<Movie> movieList=movieService.getAllMovies();
//        List<Movie> movieList=new ArrayList<>();
//        movieList.addAll(location.getMovieList());
//        movieService.saveAllMovie(movieList);
//        List<Theatre> theatres=new ArrayList<>();
//        for(Movie movie:movieList)
//        {
//            Theatre theatre=movie.getTheatre();
//            theatres.add(theatre);
//        }
//        theatreService.saveAllTheatres(theatres);
//    }

//    public void

    public void deleteLocation(Location location)
    {
        locationRepository.deleteById(location.getLocationid());
    }



    public void addTheatresToLocation(Location location)
    {
        locationRepository.save(location);

    }

    public Movie addMovieToTheatre(String locationId, String theatreId, MovieRequest movieRequest) {

        // 1️⃣ Fetch Location & Theatre
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        Theatre theatre = theatreService.getTheatreById(theatreId);
        if (theatre == null) {
            throw new RuntimeException("Theatre not found");
        }

        // 2️⃣ Create and Save the Movie First (to get movieId)
        Movie newMovie = new Movie();
        newMovie.setDescription(movieRequest.getDescription());
        newMovie.setLanguage(movieRequest.getLanguage());
        newMovie.setGenre(movieRequest.getGenre());
        newMovie.setMoviename(movieRequest.getMoviename());
        newMovie.setReleaseDate(movieRequest.getReleaseDate());
        newMovie.setLocation(location);

        Movie savedMovie = movieService.saveMovie(newMovie); // ✅ Save first so it gets a valid ID

        // 3️⃣ Update each showtime with movieId
        movieRequest.getShowTimes().forEach(st -> st.setMovieid(savedMovie.getMovieid()));

        // 4️⃣ Add movie & showtimes to theatre
        theatre.getMovieList().add(savedMovie);
        theatre.getShowTimes().addAll(movieRequest.getShowTimes());
        theatreService.saveTheatre(theatre);

        // 5️⃣ Link theatre to movie
        savedMovie.getTheatreList().add(theatre);
        movieService.saveMovie(savedMovie);

//        // 6️⃣ (Optional) Link movie to location if required
//        if (!location.getMovieList().contains(savedMovie)) {
//            location.getMovieList().add(savedMovie);
//            locationRepository.save(location);
//        }

        return savedMovie;
    }


    public List<Movie> getAllMoviesByLocationByName(String locationName) {

        Location location = locationRepository.findBylocationname(locationName);
        List<Theatre> theatreList=location.getTheatreList();

//        LocationResponse location = locationRepository.findBylocationname(locationName);
//        List<TheatreResponse> theatreList=location.getTheatres();


        List<Movie> movieList=new ArrayList<>();

        for(Theatre theatre:theatreList)
        {
            movieList.addAll(theatre.getMovieList());
        }

        return movieList;
    }
    public List<Movie> getAllMoviesByLocationById(String locationid) {

        Location location = locationRepository.findById(locationid).orElse(null);
        List<Theatre> theatreList=new ArrayList<>();
        if(location!=null)
       {
        theatreList=location.getTheatreList();
        }

//        LocationResponse location = locationRepository.findBylocationname(locationName);
//        List<TheatreResponse> theatreList=location.getTheatres();


        List<Movie> movieList=new ArrayList<>();

        for(Theatre theatre:theatreList)
        {
            movieList.addAll(theatre.getMovieList());
        }

        return movieList;
    }

    public List<Theatre> getAllTheatresForMovieWithShowTimesForLocationByNames(String locationname,String moviename)
    {
        Location location=locationRepository.findBylocationname(locationname);
        List<Movie> movieslist=this.getAllMoviesByLocationByName(locationname);

        List<Theatre> theatres=new ArrayList<>();

        for(Movie movie:movieslist)
        {
            if(movie.getMoviename().equals(moviename) && movie.getLocation().getLocationname().equals(locationname))
            {
                theatres.addAll(movie.getTheatreList());
            }
        }

        return theatres;

    }
    public List<Theatre> getAllTheatresForMovieWithShowTimesForLocationByIds(String locationid,String movieid)
    {
        Location location=locationRepository.findById(locationid).orElse(null);
        List<Movie> movieslist=this.getAllMoviesByLocationById(locationid);

        List<Theatre> theatres=new ArrayList<>();

        for(Movie movie:movieslist)
        {
            if(movie.getMovieid().equals(movieid) && movie.getLocation().getLocationid().equals(locationid))
            {
                theatres.addAll(movie.getTheatreList());
            }
        }

        return theatres;

    }
}
