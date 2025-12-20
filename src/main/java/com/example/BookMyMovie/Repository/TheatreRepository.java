package com.example.BookMyMovie.Repository;

import com.example.BookMyMovie.Entity.Theatre;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreRepository extends MongoRepository<Theatre,String> {
}
