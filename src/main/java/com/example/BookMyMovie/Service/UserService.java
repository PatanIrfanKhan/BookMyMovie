package com.example.BookMyMovie.Service;

import com.example.BookMyMovie.Entity.User;
import com.example.BookMyMovie.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    Logger logger=LoggerFactory.getLogger(UserService.class);

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public void createUser(User user)
    {
       logger.info("creating user in service and setting the roles");
        user.setRoles(Arrays.asList("ADMIN","USER"));
//        user.setRoles(Arrays.asList("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("User created:{}",user);
    }

    public User saveUser(User user)
    {
        logger.info("Saving user without roles");
        return userRepository.save(user);
    }

    public List<User> getAllUsers()
    {
        List<User> allusers=userRepository.findAll();
        return allusers;
    }

    //Get user by id
    public User getUserById(String id)
    {
        User user=userRepository.findById(id).orElse(null);
        return user;
    }
    //Get user by name
    public User getUserByName(String name)
    {
        User user=userRepository.findByusername(name);
        return user;
    }
    //modify user
    public void modifyUser(User user)
    {
        userRepository.save(user);
    }

    //delete user by id
    public void deleteUserById(String id)
    {
        logger.info("Deleting user by id");
        userRepository.deleteById(id);
    }

    //delete user by name
    public void deleteUserByName(String name)
    {
        logger.info("Deleting user by name");
        userRepository.deleteByusername(name);
    }
}
