package com.dollarsbank.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dollarsbank.exception.BadAuthenticationHeaderException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

	private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private final int EXPIRATION_TIME = 1000 * 60 * 60;
	private final int TOKEN_START_INDEX = 7;
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public String generateTokens(UserDetails userDetails) {
		Map <String, Object> claims = new HashMap<>();
		
		return createToken(claims, userDetails.getUsername());
	}
	
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date (System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SECRET_KEY, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public String extractToken(String authenticationHeader) throws BadAuthenticationHeaderException{
		if (!authenticationHeader.startsWith("Bearer ")) {
			throw new BadAuthenticationHeaderException();
		}
		return authenticationHeader.substring(TOKEN_START_INDEX);
	}
}
