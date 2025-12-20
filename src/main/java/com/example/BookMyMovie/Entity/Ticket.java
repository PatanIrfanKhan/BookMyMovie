package com.example.BookMyMovie.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(def="{'ticketid':1,userid:1}",unique = true)
public class Ticket {

    @Id
    private String ticketid;

    @Indexed
    private String userid;

    private LocalDateTime bookedtimeanddate=LocalDateTime.now();

    private String locationname;
    private String address;

    private String moviename;

    private String theatrename;

    private LocalDateTime showtime;
    private String seats;

    private double amount;

    private String ticketstatus;

}