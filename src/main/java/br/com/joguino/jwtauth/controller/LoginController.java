package br.com.joguino.jwtauth.controller;

import br.com.joguino.jwtauth.auth.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<String> logIn() {
        return ResponseEntity.ok().body(jwtTokenProvider.generateJwtToken());
    }

}
