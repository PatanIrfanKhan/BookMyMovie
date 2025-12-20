package com.example.BookMyMovie.Repository;


import com.example.BookMyMovie.Entity.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends MongoRepository<Location,String> {

    public Location findBylocationname(String locationname);
//    public  findBylocationname(String locationname);
}
