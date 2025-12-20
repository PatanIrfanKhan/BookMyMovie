package com.example.BookMyMovie.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "locationDB")
@Data
@AllArgsConstructor
@NoArgsConstructor

// ✅ Location.java
public class Location {

    @Id
    private String locationid;

    @NonNull
    @Indexed(unique = true)
    private String locationname;

    // Parent side → Location → Theatre
//    @DBRef
    @DBRef(lazy = true)
    @JsonIgnore
    private List<Theatre> theatreList = new ArrayList<>();
}
