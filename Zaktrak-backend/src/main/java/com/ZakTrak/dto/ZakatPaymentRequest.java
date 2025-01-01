package com.ZakTrak.dto;

import java.math.BigDecimal;

public record ZakatPaymentRequest(BigDecimal amount, String description) {
}
