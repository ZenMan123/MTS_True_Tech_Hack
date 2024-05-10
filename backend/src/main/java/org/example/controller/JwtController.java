package org.example.controller;

import jakarta.validation.Valid;
import org.example.exception.ValidationException;
import org.example.form.UserCredentials;
import org.example.form.validator.UserCredentialsEnterValidator;
import org.example.service.JwtService;
import org.example.service.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.example.model.User;

@RestController
@RequestMapping("/api/")
public class JwtController {
    private final UserCredentialsEnterValidator userCredentialsEnterValidator;
    private final UserService userService;
    private final JwtService jwtService;

    public JwtController(UserCredentialsEnterValidator userCredentialsEnterValidator, UserService userService, JwtService jwtService) {
        this.userCredentialsEnterValidator = userCredentialsEnterValidator;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @InitBinder("userCredentials")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(userCredentialsEnterValidator);
    }

    @PostMapping("jwts")
    public String create(@RequestBody @Valid UserCredentials userCredentials, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        User user = userService.findByPhoneNumber(userCredentials.getPhoneNumber());
        return jwtService.create(user);
    }
}
