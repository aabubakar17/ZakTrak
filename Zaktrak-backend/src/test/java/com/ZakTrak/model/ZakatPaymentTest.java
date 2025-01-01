package com.ZakTrak.model;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

public class ZakatPaymentTest {

    @Test
    void shouldCreateValidZakatPayment() {
        ZakatPayment payment = new ZakatPayment(
                "userId123",
                new BigDecimal("100.00"),
                LocalDate.now(),
                "Monthly payment"
        );

        assertThat(payment.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(payment.getUserId()).isEqualTo("userId123");
    }
}
