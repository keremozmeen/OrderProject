package com.company.ordersystem.user.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    String generateToken(String userName);
    String generateJwtToken(Authentication authentication);
    String extractUsername(String token);
    Date extractExpiration(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    boolean validateJwtToken(String authToken);
    Boolean validateToken(String token, UserDetails userDetails);
}
