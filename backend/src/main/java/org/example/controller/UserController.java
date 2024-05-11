package org.example.controller;


import org.example.model.User;
import org.example.service.JwtService;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final JwtService jwtService;

    public UserController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/users/jwt")
    public User findUserByJwt(@RequestParam(name = "jwt") String jwt) {
        return jwtService.find(jwt);
    }
}
