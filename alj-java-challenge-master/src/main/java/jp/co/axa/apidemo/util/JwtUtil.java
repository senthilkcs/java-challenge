package jp.co.axa.apidemo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for handling JWT tokens.
 */
@Component
public class JwtUtil {

    /**
     * The secret key used to sign JWT tokens.
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * The validity period (in seconds) of JWT tokens.
     */
    @Value("${jwt.token.validity}")
    private long jwtTokenValidity;

    /**
     * Extracts the username from a JWT token.
     *
     * @return The username extracted from the token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @return The expiration date extracted from the token.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extracts a claim from a JWT token.
     *
     * @return The claim extracted from the token.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses a JWT token and returns all claims.
     *
     * @return All claims extracted from the token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    /**
     * Determines if a JWT token is expired.
     *
     * @return True if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Generates a JWT token for a user.
     *
     * @return The JWT token generated for the user.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Generates a JWT token with the specified claims and subject.
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    /**
     * Validates a JWT token for a user.
     *
     * @return True if the token is valid for the user, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
