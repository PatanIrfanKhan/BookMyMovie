package com.example.BookMyMovie.Controller;

import com.example.BookMyMovie.Entity.User;
import com.example.BookMyMovie.SecurityConfig.UserDetailServiceImplementation;
import com.example.BookMyMovie.Service.UserService;
import com.example.BookMyMovie.Utilities.JWTUtilities;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    private Logger logger= LoggerFactory.getLogger(PublicController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtilities jwtUtilities;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImplementation userDetailServiceImplementation;

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/SignupViaOTP")
    public ResponseEntity<?> createNewUserViaOTP(@RequestBody User user)
    {
        try
        {
            kafkaTemplate.send("User-Login-OTP",user.getEmail(),user);
//            userService.createUser(user);
            return new ResponseEntity<>("OTP Sent Successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verifyotp")
    public ResponseEntity<?> verifyOTP(@RequestParam String otp, @RequestBody User user)
    {

            String redisotp=stringRedisTemplate.opsForValue().get(user.getEmail());
            log.info("OTP(Controller) is : {} and request param otp : {}",redisotp,otp);
            if(redisotp.equals(otp)) {
                userService.createUser(user);
               String jwttoken= jwtUtilities.createToken(user.getUsername());
               kafkaTemplate.send("User-Welcome-Message",user.getEmail(),user);
                return new ResponseEntity<>(jwttoken, HttpStatus.CREATED);
            }
            return new ResponseEntity<>("OTP is Incorrect",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/Signup")
    public ResponseEntity<?> createNewUser(@RequestBody User user)
    {
        try
        {
            logger.info("User Creation Started");
            userService.createUser(user);
            logger.info("User created : {}",user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            String str="Write operation error on server localhost:27017. Write error: WriteError{code=11000, message='E11000 duplicate key error collection: BookMyMovie.users index: username dup key:";
            if(e.getMessage().toString().indexOf(str)>-1) {
                logger.info("User already exists");
                return new ResponseEntity<>("User Already Exists !", HttpStatus.BAD_REQUEST);
            }
            log.info("uanble to signup due to {}",e.getMessage());
            return new ResponseEntity<>("Enter Valid Credentials !",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody User user)
    {
        try
        {
            logger.info("Authenticating user");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
            UserDetails userDetails=userDetailServiceImplementation.loadUserByUsername(user.getUsername());
            logger.info("Authentication success and creating a jwt token");
            String JwtToken= jwtUtilities.createToken(user.getUsername());
            logger.info("Token Created : {}",JwtToken);
            return new ResponseEntity<>(Map.of("token",JwtToken),HttpStatus.OK);
        } catch (Exception e) {
            logger.info("User with userid: {} not found!",user.getId());
            return new ResponseEntity<>("User Not Found !",HttpStatus.NOT_FOUND);
        }
    }
}
