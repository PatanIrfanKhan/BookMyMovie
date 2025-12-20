package com.example.BookMyMovie.Utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtilities {

    private String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String createToken(String username)
    {
        Map<String,Object> claims=new HashMap<>();
        return generateToken(username,claims);
    }

    public String generateToken(String subject,Map<String,Object> claims)
    {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public boolean isTokenExpired(String token)
    {
        Date tokenexpirationdate=extractAllClaims(token).getExpiration();

        if(tokenexpirationdate.before(new Date()))
            return true;
        return false;

    }

    public String extractUserName(String token)
    {
        return extractAllClaims(token).getSubject();
    }



}
