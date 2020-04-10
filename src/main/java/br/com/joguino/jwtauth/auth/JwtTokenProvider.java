package br.com.joguino.jwtauth.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

@Log4j2
public class JwtTokenProvider {

    private JwtTokenProvider() {
        throw new UnsupportedOperationException();
    }

    private static final SecretKey JWT_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    static void generateJwtToken(HttpServletResponse response, String username) {
        String jwt = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(LocalDateTime.now()
                        .atZone(ZoneId.systemDefault())
                        .toInstant())
                ).setExpiration(Date.from(LocalDateTime.now()
                        .plusDays(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant())
                ).signWith(JWT_KEY)
                .compact();
        response.addHeader("Authorization", "Bearer" + " " + jwt);
    }

    static Authentication getAuthentication(HttpServletRequest response) {
        String token = response.getHeader("Authorization");

        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey(JWT_KEY)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            }
        }

        return null;
    }
}
