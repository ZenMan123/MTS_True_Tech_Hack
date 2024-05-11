package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.PaymentDto;
import org.example.dto.TransferMoneyRequest;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController("/api/operations")
@RequiredArgsConstructor
@CrossOrigin
public class OperationsController {
    private final UserService userService;

    @PostMapping("/transfer")
    @CrossOrigin
    public void transfer(@RequestBody @Valid TransferMoneyRequest request) {
        userService.transferMoney(request);
    }

    @GetMapping("/getBalance")
    @CrossOrigin
    public double getBalance(@RequestParam long userId) {
        return userService.getBalance(userId);
    }

    @GetMapping("/getHistory")
    @CrossOrigin
    public List<PaymentDto> getHistory(@RequestParam long userId) {
        return userService.getPayments(userId)
                .stream()
                .map(payment -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
                    String date = payment.getDate().format(formatter);
                    return new PaymentDto(payment.getUser_id(), payment.getAmount(), payment.getType(), date);
                })
                .toList();
    }
}
