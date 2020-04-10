package br.com.joguino.jwtauth.domain;

import lombok.Data;

@Data
public class UserDTO {

    private String username;

    private String password;
}
