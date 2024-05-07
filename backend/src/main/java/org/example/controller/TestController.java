package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.model.Payment;
import org.example.model.User;
import org.example.repository.PaymentRepository;
import org.example.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

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


    @GetMapping("/getPayment/{user_id}")
    public List<Payment> getPayment(@PathVariable("user_id") long user_id) {
        return paymentRepository.findLastPaymentsById(user_id, 2);
    }
}
