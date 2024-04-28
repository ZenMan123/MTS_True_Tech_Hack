package org.example.repository;

import org.example.dto.UserDto;
import org.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findById(long id);

    Optional<Double> getBalanceById(long id);

    void updateBalanceById(long id, double balance);
    void registerUser(UserDto user);
}
