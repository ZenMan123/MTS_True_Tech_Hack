package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController("api/test")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;

    @GetMapping("hello")
    public String test() {
        return "Hello World";
    }

    @GetMapping("userPN/{phoneNumber}")
    public User findUserByPhoneUser(@PathVariable("phoneNumber") String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    @GetMapping("userID/{id}")
    public User findUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping("userBalance/{id}")
    public double getUserBalanceById(@PathVariable("id") Long id) {
        return userRepository.getBalanceById(id).orElse(-1.0);
    }

    @PostMapping("userUpd/{id}")
    public void updateUserBalance(@PathVariable("id") Long id, @RequestBody double value) {
        userRepository.updateBalanceById(id, value);
    }

    @PostMapping("userRegister")
    public void registerUser(@RequestBody UserDto user) {
        userRepository.registerUser(user);
    }
}
