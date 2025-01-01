package com.ZakTrak.service;

import com.ZakTrak.model.ZakatPayment;
import com.ZakTrak.repository.ZakatPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZakatPaymentService {

    private final ZakatPaymentRepository zakatPaymentRepository;
    private final UserService userService;


    public ZakatPayment createPayment(BigDecimal amount, String description) {
        String userId = userService.getCurrentUser().getId();

        ZakatPayment payment = new ZakatPayment(
                userId,
                amount,
                LocalDate.now(),
                description
        );

        return zakatPaymentRepository.save(payment);
    }


    public BigDecimal getTotalPaymentsForCurrentYear() {
        String userId = userService.getCurrentUser().getId();
        LocalDate now = LocalDate.now();
        LocalDate startOfYear = now.withDayOfYear(1);
        LocalDate endOfYear = now.withDayOfYear(now.lengthOfYear());

        Double total = zakatPaymentRepository.sumPaymentsByUserIdAndPaymentDateBetween(
                userId,
                startOfYear,
                endOfYear
        );

        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }


    public List<ZakatPayment> getAllPayments() {
        String userId = userService.getCurrentUser().getId();
        return zakatPaymentRepository.findByUserId(userId);
    }


    public BigDecimal getRemainingZakatToPay(BigDecimal totalZakatDue) {
        BigDecimal totalPaid = getTotalPaymentsForCurrentYear();
        BigDecimal remaining = totalZakatDue.subtract(totalPaid);
        return remaining.compareTo(BigDecimal.ZERO) > 0 ? remaining : BigDecimal.ZERO;
    }
}