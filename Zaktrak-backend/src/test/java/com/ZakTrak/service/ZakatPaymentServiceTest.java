package com.ZakTrak.service;

import com.ZakTrak.dto.UpdatePaymentRequest;
import com.ZakTrak.model.ZakatPayment;
import com.ZakTrak.repository.ZakatPaymentRepository;
import com.ZakTrak.model.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ZakatPaymentServiceTest {

    @Mock
    private ZakatPaymentRepository zakatPaymentRepository;

    @Mock
    private UserService userService;

    @Mock
    private User mockUser;

    @InjectMocks
    private ZakatPaymentService zakatPaymentService;

    @Test
    @DisplayName("Should create new zakat payment")
    void shouldCreateNewZakatPayment() {
        // Arrange
        String userId = "user123";
        when(mockUser.getId()).thenReturn(userId);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        BigDecimal amount = new BigDecimal("100.00");
        String description = "Test payment";

        ZakatPayment payment = new ZakatPayment(
                userId,
                amount,
                LocalDate.now(),
                description
        );

        when(zakatPaymentRepository.save(any(ZakatPayment.class))).thenReturn(payment);

        // Act
        ZakatPayment result = zakatPaymentService.createPayment(amount, description);

        // Assert
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should get total payments for current year")
    void shouldGetTotalPaymentsForCurrentYear() {
        // Arrange
        String userId = "user123";
        when(mockUser.getId()).thenReturn(userId);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        LocalDate now = LocalDate.now();
        when(zakatPaymentRepository.sumPaymentsByUserIdAndPaymentDateBetween(
                eq(userId),
                any(LocalDate.class),
                any(LocalDate.class)
        )).thenReturn(250.00);

        // Act
        BigDecimal total = zakatPaymentService.getTotalPaymentsForCurrentYear();

        // Assert
        assertThat(total).isEqualTo(new BigDecimal("250.0"));

    }

    @Test
    @DisplayName("Should get all payments for user")
    void shouldGetAllPaymentsForUser() {
        // Arrange
        String userId = "user123";
        when(mockUser.getId()).thenReturn(userId);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        List<ZakatPayment> payments = Arrays.asList(
                new ZakatPayment(userId, new BigDecimal("100.00"), LocalDate.now(), "Payment 1"),
                new ZakatPayment(userId, new BigDecimal("150.00"), LocalDate.now(), "Payment 2")
        );

        when(zakatPaymentRepository.findByUserId(userId)).thenReturn(payments);

        // Act
        List<ZakatPayment> result = zakatPaymentService.getAllPayments();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Should calculate remaining zakat to pay")
    void shouldCalculateRemainingZakatToPay() {
        // Arrange
        String userId = "user123";
        when(mockUser.getId()).thenReturn(userId);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        // Mock total payments for current year
        LocalDate now = LocalDate.now();
        when(zakatPaymentRepository.sumPaymentsByUserIdAndPaymentDateBetween(
                eq(userId),
                any(LocalDate.class),
                any(LocalDate.class)
        )).thenReturn(150.00);

        BigDecimal totalZakatDue = new BigDecimal("250.00");

        // Act
        BigDecimal remaining = zakatPaymentService.getRemainingZakatToPay(totalZakatDue);

        // Assert
        assertThat(remaining).isEqualByComparingTo(new BigDecimal("100.00")); // 250 - 150
    }

    @Test
    @DisplayName("Should return zero when payments exceed total zakat due")
    void shouldReturnZeroWhenPaymentsExceedDue() {
        // Arrange
        String userId = "user123";
        when(mockUser.getId()).thenReturn(userId);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        // Mock total payments exceeding zakat due
        when(zakatPaymentRepository.sumPaymentsByUserIdAndPaymentDateBetween(
                eq(userId),
                any(LocalDate.class),
                any(LocalDate.class)
        )).thenReturn(300.00);

        BigDecimal totalZakatDue = new BigDecimal("250.00");

        // Act
        BigDecimal remaining = zakatPaymentService.getRemainingZakatToPay(totalZakatDue);

        // Assert
        assertThat(remaining).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldDeletePayment() {
        // Arrange
        String paymentId = "123";
        String userId = "user123";
        User mockUser = mock(User.class);
        ZakatPayment payment = new ZakatPayment(userId, new BigDecimal("100.00"),
                LocalDate.now(), "test");

        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(userId);
        when(zakatPaymentRepository.findByIdAndUserId(paymentId, userId))
                .thenReturn(Optional.of(payment));

        // Act
        zakatPaymentService.deletePayment(paymentId);

        // Assert
        verify(zakatPaymentRepository).delete(payment);
    }

    @Test
    void shouldUpdatePayment() {
        // Arrange
        String paymentId = "123";
        String userId = "user123";
        User mockUser = mock(User.class);
        ZakatPayment payment = new ZakatPayment(userId, new BigDecimal("100.00"),
                LocalDate.now(), "test");
        BigDecimal newAmount = new BigDecimal("150.00");
        String newDescription = "updated";

        UpdatePaymentRequest request = new UpdatePaymentRequest(newAmount, newDescription);

        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(userId);
        when(zakatPaymentRepository.findByIdAndUserId(paymentId, userId))
                .thenReturn(Optional.of(payment));
        when(zakatPaymentRepository.save(any(ZakatPayment.class))).thenReturn(payment);

        // Act
        ZakatPayment updatedPayment = zakatPaymentService.updatePayment(paymentId, request);

        // Assert
        assertThat(updatedPayment.getAmount()).isEqualTo(request.amount());
        assertThat(updatedPayment.getDescription()).isEqualTo(request.description());
        verify(zakatPaymentRepository).save(payment);
    }
}