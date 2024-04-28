package org.example.repository.impl;

import org.example.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .phoneNumber(rs.getString("phone_number"))
                .balance(rs.getDouble("balance"))
                .build();
    }
}
