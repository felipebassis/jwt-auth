package br.com.joguino.jwtauth.controller;

import br.com.joguino.jwtauth.domain.UserDTO;
import br.com.joguino.jwtauth.domain.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping(path = "signUp")
    public ResponseEntity<Void> signUp(@RequestBody UserDTO userDto) {
        userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
