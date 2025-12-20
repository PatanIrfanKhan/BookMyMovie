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

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Theatres")
@CompoundIndex(def = "{'theatrename': 1, 'location.$id': 1}", unique = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theatre {

    @Id
    private String theatreid;

    @Indexed
    private String theatrename;

    @NonNull
    private String address;

    @DBRef
    private Location location; // ✅ Reference to location

    @DBRef(lazy = true)
    @JsonIgnore
    private List<Movie> movieList = new ArrayList<>();

    private List<ShowTime> showTimes = new ArrayList<>();
}
