package com.example.BookMyMovie.SecurityConfig;

import com.example.BookMyMovie.SecurityConfig.UserDetailServiceImplementation;
import com.example.BookMyMovie.Service.UserService;
import com.example.BookMyMovie.Utilities.JWTUtilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtilities jwtUtilities;

    @Autowired
    private UserDetailServiceImplementation userdetailservice;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationheader=request.getHeader("Authorization");
        String username=null;
        String token=null;

        log.info("authorizationheader : {}",authorizationheader);

        if(authorizationheader!=null && authorizationheader.startsWith("Bearer"))
        {
             token=authorizationheader.substring(7);
            log.info("Token: {}",token);
             username=jwtUtilities.extractUserName(token);
        }

        if(username!=null)
        {
            log.info("Token: {}",token);
            log.info("Username: {}",username);
            UserDetails userDetails=userdetailservice.loadUserByUsername(username);

            if(!(jwtUtilities.isTokenExpired(token)))
            {
                UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request,response);
    }
}
