package com.example.BookMyMovie.Controller;

import com.example.BookMyMovie.Entity.Theatre;
import com.example.BookMyMovie.Service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Theatre")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @GetMapping
    public ResponseEntity<?> getAllTheaters()
    {
        List<Theatre> theatreList=theatreService.getAllTheatres();
        if(theatreList!=null)
            return new ResponseEntity<>(theatreList, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}