package org.example.repository.impl;

import org.example.model.Payment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class PaymentMapper implements RowMapper<Payment> {
    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Payment.builder()
                .id(rs.getLong("id"))
                .user_id(rs.getLong("user_id"))
                .amount(rs.getDouble("amount"))
                .date(rs.getObject("date", OffsetDateTime.class))
                .type(rs.getString("type"))
                .build();
    }
}
