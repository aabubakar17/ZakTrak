package com.ZakTrak.dto;
import java.math.BigDecimal;

public record UpdatePaymentRequest( BigDecimal amount, String description) {
}
