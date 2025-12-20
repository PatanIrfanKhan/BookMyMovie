package com.example.BookMyMovie.DTO;


import lombok.Data;

import java.util.List;

@Data
public class TheatreResponse {

    private String theatreName;
    private String address;
    private List<MovieResponse> movies;


}
