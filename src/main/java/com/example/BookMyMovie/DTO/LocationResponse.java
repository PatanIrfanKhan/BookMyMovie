package com.example.BookMyMovie.DTO;

import com.example.BookMyMovie.DTO.TheatreResponse;
import lombok.Data;

import java.util.List;

@Data
public class LocationResponse {

    private String locationName;
    private List<TheatreResponse> theatres;
}
