package com.example.BookMyMovie.Service;

import com.example.BookMyMovie.Entity.Theatre;
import com.example.BookMyMovie.Repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    public Theatre saveTheatre(Theatre theatre)
    {
       return theatreRepository.save(theatre);
    }

    public Theatre getTheatreById(String theatreid)
    {
        Theatre theatre=theatreRepository.findById(theatreid).orElse(null);
        return theatre;
    }

    public void saveAllTheatres(List<Theatre> theatres)
    {
        theatreRepository.saveAll(theatres);
    }

    public void deleteTheatreById(String id)
    {
        theatreRepository.deleteById(id);
    }
    public List<Theatre> getAllTheatres()
    {
        List<Theatre> theatreList=theatreRepository.findAll();
        return theatreList;
    }
}
