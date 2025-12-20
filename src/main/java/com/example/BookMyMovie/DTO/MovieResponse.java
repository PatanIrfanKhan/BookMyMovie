package com.example.BookMyMovie.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieResponse {
    private String movieName;
    private String genre;
    private String language;
    private List<LocalDateTime> showTimes;
}
