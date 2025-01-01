package com.ZakTrak.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "zakat_payments")
@Getter
public class ZakatPayment {
    @Id
    private String id;

    @Indexed
    private String userId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String description;
    private LocalDateTime createdAt;

    public ZakatPayment(String userId, BigDecimal amount, LocalDate paymentDate, String description) {
        validatePayment(amount, paymentDate);
        this.userId = userId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    private void validatePayment(BigDecimal amount, LocalDate paymentDate) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        if (paymentDate == null) {
            throw new IllegalArgumentException("Payment date is required");
        }
        if (paymentDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Payment date cannot be in the future");
        }
    }
}