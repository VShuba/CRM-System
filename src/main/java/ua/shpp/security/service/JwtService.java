package ua.shpp.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;
    @Value("${token.expirationInMillis}")
    private int tokenExpirationInMillis;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractAuthority(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String generateToken(UserDetails userDetails) {
        log.debug("Generating a JWT token for the user: {} ", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    public boolean isTokenValid(String token) {
        boolean valid = isTokenNotExpired(token);
        log.debug("Token validation for user with id: {}, valid: {}", extractUserName(token), valid);
        return valid;
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpirationInMillis))
                .signWith(getSigningKey()).compact();
    }

    private boolean isTokenNotExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}