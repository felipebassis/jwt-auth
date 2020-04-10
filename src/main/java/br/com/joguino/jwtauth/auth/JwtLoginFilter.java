package br.com.joguino.jwtauth.auth;

import br.com.joguino.jwtauth.domain.User;
import br.com.joguino.jwtauth.domain.UserDTO;
import br.com.joguino.jwtauth.domain.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Esta classe é responsavel por autenticar o usuário, apenas convertendo o {@link UserDTO} e delegando para o {@link org.springframework.security.authentication.AuthenticationProvider}
 * que foi declarado em {@link SecurityConfig#authenticationProvider(PasswordEncoder, UserService)},
 */

@Log4j2
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    protected JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    /**
     * Delega ao {@link org.springframework.security.authentication.AuthenticationProvider} a autenticação
     * @param httpServletRequest os detalhes da request
     * @param httpServletResponse os detalhes da resposta
     * @return O objeto de validação da autenticaçaõ
     * @throws AuthenticationException Caso não seja possivel autenticar
     * @throws IOException Caso o Body da request não seja valido
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        UserDTO userDTO = new ObjectMapper()
                .readValue(httpServletRequest.getInputStream(), UserDTO.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword(),
                        Collections.emptyList()
                )
        );
    }

    /**
     * Método responsável por tratar a autenticação com sucesso, neste caso esse método esta adicionando o JWT no header.
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        JwtTokenProvider.generateJwtToken(response, authResult.getName());
    }
}
