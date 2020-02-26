package br.com.joguino.jwtauth.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {

        String jwt = getJwtFromRequest(httpServletRequest);

        if (!StringUtils.isEmpty(jwt) && jwtTokenProvider.isTokenValid(jwt)) {
            try {


                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("Test", 123L, null);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {
                log.error("Error {}", jwt);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .map(authorization -> authorization.replace("Bearer ", ""))
                .orElse("");
    }
}
