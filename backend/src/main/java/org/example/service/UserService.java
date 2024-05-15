package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.PaymentDto;
import org.example.dto.TransferMoneyRequest;
import org.example.exception.InvalidRequestException;
import org.example.model.Payment;
import org.example.model.User;
import org.example.repository.PaymentRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String TRANSFER = "Перевод";
    private static final String RECEIPT = "Поступление";
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void transferMoney(TransferMoneyRequest request) {
        long fromId = request.fromId();
        long toId = userRepository.findByPhoneNumber(request.toPhoneNumber())
                .orElseThrow(() -> new InvalidRequestException("Cannot find user with number: " + request.toPhoneNumber()))
                .getId();
        double fromBalance = userRepository.getBalanceById(fromId)
                .orElseThrow(() -> new InvalidRequestException("Cannot find balance for user " + toId));

        double amount = request.value();
        validateTransferOperation(fromBalance, amount);
        userRepository.updateBalanceById(fromId, fromBalance - amount);
        double toBalance = userRepository.getBalanceById(toId)
                .orElseThrow(() -> new InvalidRequestException("Cannot find balance for user " + toId));
        userRepository.updateBalanceById(toId, toBalance + amount);

        paymentRepository.insertPayment(new PaymentDto(
                fromId, amount, TRANSFER, ""
        ));
        paymentRepository.insertPayment(new PaymentDto(
                toId, amount, RECEIPT, ""
        ));
    }

    private void validateTransferOperation(double fromBalance, double value) {
        if (value < 0 || fromBalance < value) {
            throw new InvalidRequestException("The balance is less than the transfer amount");
        }
    }

    public double getBalance(long id) {
        return userRepository.getBalanceById(id).orElseThrow(InvalidRequestException::new);
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<Payment> getPayments(Long id) {
        return paymentRepository.findLastPaymentsById(id, 10);
    }
}
