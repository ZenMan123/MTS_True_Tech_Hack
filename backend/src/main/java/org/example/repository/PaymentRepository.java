package org.example.repository;

import org.example.dto.PaymentDto;
import org.example.model.Payment;

import java.util.List;

public interface PaymentRepository {
    List<Payment> findLastPaymentsById(long user_id, int batchSize);

    void insertPayment(PaymentDto payment);
}
