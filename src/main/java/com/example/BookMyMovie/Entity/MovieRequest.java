package com.example.BookMyMovie.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    private String moviename;
    private String genre;
    private String language;
    private LocalDate releaseDate;
    private String description;

    // Show times for a specific theatre
    private List<ShowTime> showTimes;

//    @DBRef
//    private Location location;
}
