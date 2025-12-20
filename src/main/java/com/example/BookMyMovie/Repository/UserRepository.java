package com.example.BookMyMovie.Repository;

import com.example.BookMyMovie.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    public User findByusername(String name);
    public User deleteByusername(String name);
}
