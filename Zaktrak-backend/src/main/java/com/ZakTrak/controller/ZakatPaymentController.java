package com.ZakTrak.controller;

import com.ZakTrak.model.ZakatPayment;
import com.ZakTrak.service.ZakatPaymentService;
import com.ZakTrak.dto.UpdatePaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/zakat-payments")
@RequiredArgsConstructor
public class ZakatPaymentController {

    private final ZakatPaymentService zakatPaymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZakatPayment createPayment(@RequestBody ZakatPaymentRequest request) {
        return zakatPaymentService.createPayment(request.amount(), request.description());
    }

    @GetMapping
    public List<ZakatPayment> getAllPayments() {
        return zakatPaymentService.getAllPayments();
    }

    @GetMapping("/remaining")
    public BigDecimal getRemainingZakatToPay(@RequestParam BigDecimal totalDue) {
        return zakatPaymentService.getRemainingZakatToPay(totalDue);
    }

    @GetMapping("/total")
    public BigDecimal getTotalPaymentsForCurrentYear() {
        return zakatPaymentService.getTotalPaymentsForCurrentYear();
    }

    @DeleteMapping("/payments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePayment(@PathVariable String id) {
        zakatPaymentService.deletePayment(id);
    }

    @PutMapping("/payments/{id}")
    public ZakatPayment updatePayment(@PathVariable String id, @RequestBody UpdatePaymentRequest request) {
        return zakatPaymentService.updatePayment(id, request);
    }


    record ZakatPaymentRequest(BigDecimal amount, String description) {}
}