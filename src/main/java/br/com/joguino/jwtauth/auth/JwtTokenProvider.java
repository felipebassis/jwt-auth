package br.com.joguino.jwtauth.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Log4j2
@Component
public class JwtTokenProvider {

    private static final SecretKey JWT_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateJwtToken() {

        return Jwts.builder()
                .setSubject("Test")
                .setIssuedAt(Date.from(LocalDateTime.now()
                        .atZone(ZoneId.systemDefault())
                        .toInstant())
                ).setExpiration(Date.from(LocalDateTime.now()
                        .plusDays(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant())
                ).signWith(JWT_KEY)
                .compact();
    }

    public boolean isTokenValid(String authToken) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(JWT_KEY)
                .build();
        try {
            parser.parse(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired Token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported Token");
        } catch (MalformedJwtException e) {
            log.error("Invalid Token");
        } catch (SignatureException e) {
            log.error("Invalid Signature");
        } catch (IllegalArgumentException e) {
            log.error("Jwt is Empty");
        }
        return false;
    }
}
