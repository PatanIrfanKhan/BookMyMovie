package com.example.BookMyMovie.Controller;

import com.example.BookMyMovie.Entity.User;
import com.example.BookMyMovie.SecurityConfig.UserDetailServiceImplementation;
import com.example.BookMyMovie.Service.UserService;
import com.example.BookMyMovie.Utilities.JWTUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/GoogleAuth")
@Slf4j
public class GoogleAuthController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDetailServiceImplementation userDetailServiceImplementation;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtilities jwtUtilities;

    @Value("${spring.security.auth2.client.registration.google.client-id}")
    private String client_id;

    @Value("${spring.security.auth2.client.registration.google.client-secret}")
    private String client_secret;


    @GetMapping("/callback")
    ResponseEntity<?> getAuthController(@RequestParam String code)
    {
        try
        {
            String googleurllink="https://oauth2.googleapis.com/token";
            MultiValueMap<String,String> params=new LinkedMultiValueMap<>();
            params.add("code",code);
            params.add("client_id",client_id);
            params.add("client_secret",client_secret);
            params.add("redirect_uri","https://developers.google.com/oauthplayground");
            params.add("grant_type","authorization_code");

            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String,String>> request=new HttpEntity<>(params,headers);
            log.info("request:{}",request);

            ResponseEntity<Map> tokenresponse=restTemplate.postForEntity(googleurllink,request,Map.class);

            String idtoken=(String)(tokenresponse).getBody().get("id_token");

            String userinfourl="https://oauth2.googleapis.com/tokeninfo?id_token=" + idtoken;

            ResponseEntity<Map> userinforesponse=restTemplate.getForEntity(userinfourl,Map.class);

            if(userinforesponse.getStatusCode()== HttpStatus.OK)
            {
                Map<String,Object> userInfo=userinforesponse.getBody();
                String email=(String) userInfo.get("email");

                UserDetails userdetails=null;
                try
                {
                    userdetails=userDetailServiceImplementation.loadUserByUsername(email);
                } catch (Exception e) {
                    User newuser=new User();
                    newuser.setEmail(email);
                    newuser.setUsername(email);
                    newuser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    newuser.setRoles(Arrays.asList("USER"));
                    userService.saveUser(newuser);
                    userdetails=userDetailServiceImplementation.loadUserByUsername(email);
                }

                User user=userService.getUserByName(email);
                String jwttoken=jwtUtilities.createToken(user.getUsername());
//                userdetails=userdetailsimpl.loadUserByUsername(email);

                return new ResponseEntity<>(Collections.singletonMap("token",jwttoken),HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);



        } catch (Exception e) {

            log.info("Login failed due to {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
