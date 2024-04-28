package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.GetBalanceRequest;
import org.example.dto.TransferMoneyRequest;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/operations")
@RequiredArgsConstructor
public class OperationsController {
    private final UserService userService;

    @PostMapping("/transfer")
    public void transfer(@RequestBody @Valid TransferMoneyRequest request) {
        userService.transferMoney(request);
    }

    @GetMapping("/getBalance")
    public double getBalance(@RequestBody GetBalanceRequest request) {
        return userService.getBalance(request.id());
    }
}
