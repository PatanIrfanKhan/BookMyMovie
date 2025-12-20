package com.example.BookMyMovie.Service;

import com.example.BookMyMovie.Entity.User;
import com.example.BookMyMovie.Repository.UserRepository;
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

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public void createUser(User user)
    {
        user.setRoles(Arrays.asList("ADMIN","USER"));
//        user.setRoles(Arrays.asList("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    public User saveUser(User user)
    {
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
        userRepository.deleteById(id);
    }

    //delete user by name
    public void deleteUserByName(String name)
    {
        userRepository.deleteByusername(name);
    }
}
