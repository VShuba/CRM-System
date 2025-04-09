package ua.shpp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ua.shpp.entity.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//Ticket Scrum-33
@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    /**
     * Отримання імені користувача з токена
     *
     * @param token токен
     * @return ім'я користувача
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Генерація токена
     *
     * @param userDetails дані користувача
     * @return токен
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("role", customUserDetails.getRole());
        }
        return generateToken(claims, userDetails);
    }

    /**
     * Перевірка токена на валідність
     *
     * @param token       токен
     * @param userDetails дані користувача
     * @return true, якщо токен валідний
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Отримання даних з токена
     *
     * @param token           токен
     * @param claimsResolvers функція отримання даних
     * @param <T>             тип даних
     * @return дані
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерація токена
     *
     * @param extraClaims додаткові дані
     * @param userDetails дані користувача
     * @return токен
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Перевірка терміну дії токена
     *
     * @param token токен
     * @return true, якщо термін сплив
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Отримання терміну дії
     *
     * @param token токен
     * @return дата закінчення дії
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Отримання всіх даних з токена
     *
     * @param token токен
     * @return дані
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Отримання ключа та підпису
     *
     * @return ключ
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
