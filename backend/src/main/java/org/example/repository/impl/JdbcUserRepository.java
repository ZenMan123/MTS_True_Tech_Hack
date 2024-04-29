package org.example.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private static final UserMapper MAPPER = new UserMapper();

    private static final String FIND_BY_PHONE_NUMBER = "SELECT * FROM users WHERE phone_number = ?";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_BALANCE_BY_ID = "SELECT balance FROM users WHERE user_id = ?";
    private static final String UPDATE_BALANCE_BY_ID = "UPDATE users SET balance = ? WHERE user_id = ?";
    private static final String REGISTER_USER = "INSERT INTO users (name, surname, phone_number, balance) VALUES (?, ?, ?, ?) RETURNING user_id";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return jdbcTemplate.queryForStream(FIND_BY_PHONE_NUMBER, MAPPER, phoneNumber)
                .findAny();

    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.queryForStream(FIND_BY_ID, MAPPER, id)
                .findAny();
    }

    @Override
    public Optional<Double> getBalanceById(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BALANCE_BY_ID, Double.class, id));
    }

    @Override
    public void updateBalanceById(long id, double balance) {
        jdbcTemplate.update(
                UPDATE_BALANCE_BY_ID,
                balance,
                id
        );
    }

    @Override
    public void registerUser(UserDto user) {
        jdbcTemplate.queryForObject(
                REGISTER_USER,
                Long.class, user.name(), user.surname(), user.phoneNumber(), user.balance()
        );
    }
}
