package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransferMoneyRequest;
import org.example.exception.InvalidRequestException;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void transferMoney(TransferMoneyRequest request) {
        long fromId = request.fromId();
        long toId = userRepository.findByPhoneNumber(request.toPhoneNumber())
                .orElseThrow(() -> new InvalidRequestException("Cannot find user with number: " + request.toPhoneNumber()))
                .getId();
        double fromBalance = userRepository.getBalanceById(fromId)
                .orElseThrow(() -> new InvalidRequestException("Cannot find balance for user " + toId));
        double toBalance = userRepository.getBalanceById(toId)
                .orElseThrow(() -> new InvalidRequestException("Cannot find balance for user " + toId));

        validateTransferOperation(fromBalance, request.value());
        userRepository.updateBalanceById(fromId, fromBalance - request.value());
        userRepository.updateBalanceById(toId, toBalance + request.value());
    }

    private void validateTransferOperation(double fromBalance, double value) {
        if (fromBalance < value) {
            throw new InvalidRequestException("The balance is less than the transfer amount");
        }
    }

    public double getBalance(long id) {
        return userRepository.getBalanceById(id).orElseThrow(InvalidRequestException::new);
    }
}
