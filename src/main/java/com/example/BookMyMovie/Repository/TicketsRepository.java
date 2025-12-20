package com.example.BookMyMovie.Repository;

import com.example.BookMyMovie.Entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketsRepository extends MongoRepository<Ticket,String>
{

}
