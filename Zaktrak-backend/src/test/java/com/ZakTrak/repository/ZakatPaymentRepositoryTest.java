package com.ZakTrak.repository;

import com.ZakTrak.model.ZakatPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ZakatPaymentRepositoryTest {

    @Autowired
    private ZakatPaymentRepository zakatPaymentRepository;

    @BeforeEach
    void setUp() {
        zakatPaymentRepository.deleteAll();
    }

    @Test
    @DisplayName("Should find payments by user ID")
    void shouldFindPaymentsByUserId() {
        // Arrange
        String userId = "testUser";
        ZakatPayment payment = new ZakatPayment(
                userId,
                new BigDecimal("100.00"),
                LocalDate.now(),
                "Test payment"
        );
        zakatPaymentRepository.save(payment);

        // Act
        List<ZakatPayment> foundPayments = zakatPaymentRepository.findByUserId(userId);

        // Assert
        assertThat(foundPayments)
                .hasSize(1)
                .first()
                .satisfies(p -> {
                    assertThat(p.getUserId()).isEqualTo(userId);
                    assertThat(p.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
                });
    }

    @Test
    @DisplayName("Should find payments by user ID and year")
    void shouldFindPaymentsByUserIdAndYear() {
        // Arrange
        String userId = "testUser";
        LocalDate paymentDate = LocalDate.of(2024, 1, 15); // Specific date in 2024

        ZakatPayment payment = new ZakatPayment(
                userId,
                new BigDecimal("100.00"),
                paymentDate,
                "Test payment"
        );
        zakatPaymentRepository.save(payment);

        // Test finding payments for the entire year of 2024
        LocalDate startDate = LocalDate.of(2024, 1, 1);    // Start of 2024
        LocalDate endDate = LocalDate.of(2024, 12, 31);    // End of 2024

        // Act
        List<ZakatPayment> foundPayments = zakatPaymentRepository
                .findByUserIdAndPaymentDateBetween(userId, startDate, endDate);

        // Assert
        assertThat(foundPayments)
                .hasSize(1)
                .first()
                .satisfies(p -> {
                    assertThat(p.getUserId()).isEqualTo(userId);
                    assertThat(p.getPaymentDate()).isEqualTo(paymentDate);
                });
    }

    @Test
    @DisplayName("Should calculate total payments for year")
    void shouldCalculateTotalPaymentsForYear() {
        // Arrange
        String userId = "testUser";
        LocalDate firstPaymentDate = LocalDate.of(2024, 1, 15);

        ZakatPayment payment1 = new ZakatPayment(
                userId,
                new BigDecimal("100.00"),
                firstPaymentDate,
                "Payment 1"
        );

        ZakatPayment payment2 = new ZakatPayment(
                userId,
                new BigDecimal("150.00"),
                firstPaymentDate.plusDays(1),
                "Payment 2"
        );

        zakatPaymentRepository.saveAll(List.of(payment1, payment2));

        // Test total for the entire year of 2024
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        // Print debug information
        System.out.println("Saved payments: " + zakatPaymentRepository.findAll().size());

        // Act
        Double totalPayments = zakatPaymentRepository
                .sumPaymentsByUserIdAndPaymentDateBetween(userId, startDate, endDate);

        // Assert
        assertThat(totalPayments).isNotNull();
        assertThat(totalPayments).isEqualTo(250.00);
    }
}