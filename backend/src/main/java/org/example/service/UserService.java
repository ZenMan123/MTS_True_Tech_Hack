package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransferMoneyRequest;
import org.example.exception.InvalidRequestException;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void transferMoney(TransferMoneyRequest request) {
        long fromId = request.fromId();
        long toId = userRepository.findByPhoneNumber(request.toPhoneNumber())
                .orElseThrow(InvalidRequestException::new).getId();
        double fromBalance = userRepository.getBalanceById(fromId)
                .orElseThrow(InvalidRequestException::new);
        double toBalance = userRepository.getBalanceById(toId)
                .orElseThrow(InvalidRequestException::new);

        validateTransferOperation(fromBalance, request.value());
        userRepository.updateBalanceById(fromId, fromBalance - request.value());
        userRepository.updateBalanceById(toId, toBalance + request.value());
    }

    private void validateTransferOperation(double fromBalance, double value) {
        if (fromBalance < value) {
            throw new InvalidRequestException("Баланс меньше чем сумма перевода");
        }
    }

    public double getBalance(long id) {
        return userRepository.getBalanceById(id).orElseThrow(InvalidRequestException::new);
    }
}
