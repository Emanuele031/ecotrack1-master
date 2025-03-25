package it.epicode.ecotrack.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret:LaTuaChiaveSegretaMoltoLungaPerSicurezza1234}")
    private String secret;

    @Value("${jwt.expiration:3600000}") // default: 1 ora in millisecondi
    private long jwtExpirationInMs;

    // Estrae il subject (username) dal token
    public String getUsernameFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            log.error("Token scaduto: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.error("Errore nella decodifica del token: {}", e.getMessage());
            throw e;
        }
    }

    // Estrae la data di scadenza dal token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Estrae un claim specifico dal token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Recupera tutti i claims dal token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // Verifica se il token è scaduto
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        boolean expired = expiration.before(new Date());
        if (expired) {
            log.debug("Il token è scaduto. Expiration: {}, Now: {}", expiration, new Date());
        }
        return expired;
    }

    // Genera un token JWT dato il subject (in questo caso l'username)
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        log.debug("Generato token per subject {}: {}", subject, token);
        return token;
    }

    // Valida il token confrontandolo con i dettagli dell'utente
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String usernameFromToken = getUsernameFromToken(token);
            boolean expired = isTokenExpired(token);
            boolean valid = usernameFromToken.equals(userDetails.getUsername()) && !expired;
            log.debug("Validazione token: usernameFromToken = {}, userDetails.getUsername() = {}, isExpired = {}, valid = {}",
                    usernameFromToken, userDetails.getUsername(), expired, valid);
            return valid;
        } catch (JwtException e) {
            log.error("Errore nella validazione del token: {}", e.getMessage());
            return false;
        }
    }
}
