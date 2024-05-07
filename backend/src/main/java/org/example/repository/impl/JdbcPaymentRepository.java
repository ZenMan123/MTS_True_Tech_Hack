package org.example.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.PaymentDto;
import org.example.model.Payment;
import org.example.repository.PaymentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcPaymentRepository implements PaymentRepository {
    private static final String FIND_LAST_PAYMENTS = "SELECT * from payment_history WHERE user_id = ? ORDER BY date LIMIT ?";
    private static final String INSERT_PAYMENT = "INSERT INTO payment_history (user_id, amount, date, type) VALUES (?, ?, ?,?) RETURNING id";
    private final JdbcTemplate jdbcTemplate;
    private final PaymentMapper MAPPER = new PaymentMapper();

    @Override
    public List<Payment> findLastPaymentsById(long user_id, int batchSize) {
        return jdbcTemplate
                .query(FIND_LAST_PAYMENTS, MAPPER, user_id, batchSize);
    }

    @Override
    public void insertPayment(PaymentDto payment) {
        jdbcTemplate.
                queryForObject(INSERT_PAYMENT, Long.class,
                        payment.user_id(),
                        payment.amount(),
                        OffsetDateTime.now(),
                        payment.type()
                );
    }
}
