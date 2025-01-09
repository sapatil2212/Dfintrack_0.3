package zenith_expense_tracker_nov_v1.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.jwt.CustomUserDetails;
import zenith_expense_tracker_nov_v1.repository.UserRepository;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
@Component
public class JwtHelper {

    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public SecretKey getSigningKey() {
        return getKey();
    }
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    @Autowired
    private UserRepository userRepository;

    public String generateToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", userDetails.getId())
                .claim("name", userDetails.getName())
                .claim("accountType", userDetails.getAccountType().toString())
                .claim("securityKey", userDetails.getSecurityKey())
                .claim("propertyId", userDetails.getPropertyId())
                .claim("propertyName", userDetails.getPropertyName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    public String generateTokenFromUsername(String username) {
        // Fetch user details from repository
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found while refreshing token"));

        // Create CustomUserDetails with all necessary information
        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                user.getAccountType(),
                user.getSecurityKey(),
                new ArrayList<>(),
                user.getProperty() != null ? user.getProperty().getId() : null,
                user.getProperty() != null ? user.getProperty().getName() : null
        );

        // Generate token with all claims
        return generateToken(userDetails);
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Invalid signature: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }
    // Extract userId from the JWT token
    public Long extractUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().get("id").toString());
    }
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Extract propertyId and propertyName from JWT token
    public Long extractPropertyId(String token) {
        return Long.parseLong(Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().get("propertyId").toString());
    }

    public String extractPropertyName(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().get("propertyName").toString();
    }
}
