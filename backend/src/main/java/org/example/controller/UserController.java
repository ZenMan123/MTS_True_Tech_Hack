package org.example.controller;


import org.example.dto.UserDto;
import org.example.model.User;
import org.example.service.JwtService;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;

    public UserController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("users/auth")
    @CrossOrigin
    public UserDto findUserByJwt(@RequestParam String jwt) {
        return jwtService.find(jwt);
    }
}
