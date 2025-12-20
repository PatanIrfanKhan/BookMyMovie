package com.example.BookMyMovie.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "movies")
@CompoundIndex(def = "{'moviename': 1, 'location.$id': 1}", unique = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
// ✅ Movie.java
public class Movie {

    @Id
    private String movieid;

    @NonNull
    @Indexed
    private String moviename;

    private String genre;
    private String language;
    private String description;
    private LocalDate releaseDate;

    @DBRef
    private Location location;

    // Avoid circular nesting → ignore Theatre reference during JSON conversion
    @DBRef(lazy = true)
    @JsonIgnore
    private List<Theatre> theatreList = new ArrayList<>();
}

