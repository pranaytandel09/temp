package com.purplebits.emrd2.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil implements Serializable {
	private final String className = JwtTokenUtil.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(JwtTokenUtil.class);

    private static final long serialVersionUID = -2550185165626007488L;

    // Token validity in seconds (15 min)
    @Value("#{${jwt.token.validity} * 60}")
    private long JWT_TOKEN_VALIDITY;
    @Value("#{${refresh.token.validity} * 60}")
    private long REFRESH_TOKEN_VALIDITY;

    // The "Bearer " prefix for Authorization headers
    public static final String JWT_AUTH_HEADER = "Bearer ";

    // The secret key used for signing and validating JWTs
    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String secret;
    

    // Initialize the secret key from the injected secret
    @PostConstruct
    public void init() {
        // Create a SecretKey from the secret string
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Retrieve username from JWT token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve expiration date from JWT token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Retrieve a specific claim from JWT token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Retrieve all claims from JWT token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Using the correct secret key
                .build()
                .parseClaimsJws(token) // Parse the JWT claims
                .getBody();
    }

 // Generate a JWT token with claims
    public String generateToken(UserDetails userDetails,String userType) {
        Map<String, Object> claims = new HashMap<>();
        // Add the username and userType claims
        claims.put("username", userDetails.getUsername());
        claims.put("userType", userType);
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return JWT_AUTH_HEADER + doGenerateToken(claims, userDetails.getUsername(), secretKey);
    }
    
    
    public String generateRefreshToken(UserDetails userDetails,String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("userType", userType);
        return JWT_AUTH_HEADER +doGenerateFromRefershToken(claims, userDetails.getUsername(), secretKey, REFRESH_TOKEN_VALIDITY * 1000);
    }

    
 // Modify doGenerateToken to accept a custom expiration time
    private String doGenerateFromRefershToken(Map<String, Object> claims, String subject, SecretKey secretKey, long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    
    
    // Generate a JWT token with a custom secret key
    private String doGenerateToken(Map<String, Object> claims, String subject, SecretKey secretKey) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date()) // Current time
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) // Token validity
                .signWith(secretKey, SignatureAlgorithm.HS512) // Consistent algorithm
                .compact();
    }

    // Validate JWT token and check if it's expired
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername())
				&& !isTokenExpired(token) /* && !jwtBlacklistService.isTokenBlacklisted(token) */);
    }

    // Check if the JWT token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Get the expiration time in milliseconds from JWT token
    public long getExpirationTime(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.getTime();
    }

    // Get the user type from JWT token
    public String getUserTypeFromToken(String token, String userTypeParam) {
        try {
            Claims claims = getAllClaimsFromToken(token);

            // Retrieve the "userType" claim
            String userType = claims.get(userTypeParam, String.class);

            if (userType == null) {
                throw new IllegalArgumentException("User type claim is missing in the JWT");
            }

            return userType;

        } catch (ExpiredJwtException e) {
        	logger.error(className + " getUserTypeFromToken() invoked for: "+e.getMessage());
            throw new IllegalArgumentException("JWT token has expired"+ e);
        } catch (UnsupportedJwtException | MalformedJwtException e) {
        	logger.error(className + " getUserTypeFromToken() invoked for: "+e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token", e);
        } catch (Exception e) {
        	logger.error(className + " getUserTypeFromToken() invoked for: "+e);
            throw new RuntimeException("Error parsing JWT token", e);
        }
    }
    
 // Method to get user type from JWT token
 	public String getUserTypeFromToken(String token) {
 		try {
 			Claims claims = getAllClaimsFromToken(token); // Get all claims from the token

 			// Get the "userType" claim from the claims
 			String userType = claims.get("userType", String.class); // Extract userType claim

 			if (userType == null) {
 				throw new IllegalArgumentException("UserType claim is missing in the JWT");
 			}

 			return userType; // Return the extracted userType claim

 		} catch (ExpiredJwtException e) {
 			logger.error(className + " getUserTypeFromToken() invoked for: "+e.getMessage());
 			throw new IllegalArgumentException("JWT token has expired", e);
 		} catch (UnsupportedJwtException | MalformedJwtException e) {
 			logger.error(className + " getUserTypeFromToken() invoked for: "+e.getMessage());
 			throw new IllegalArgumentException("Invalid JWT token", e);
 		} catch (Exception e) {
 			logger.error(className + " getUserTypeFromToken() invoked for: "+e);
 			throw new RuntimeException("Error while parsing JWT token", e);
 		}
 	}
}
