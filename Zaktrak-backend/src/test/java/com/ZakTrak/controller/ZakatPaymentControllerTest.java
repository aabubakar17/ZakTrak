package com.ZakTrak.controller;

import com.ZakTrak.model.ZakatPayment;
import com.ZakTrak.service.ZakatPaymentService;
import com.ZakTrak.dto.ZakatPaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ZakatPaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ZakatPaymentService zakatPaymentService;

    private final String BASE_URL = "/api/zakat-payments";

    @Test
    @DisplayName("Should create new payment")
    @WithMockUser
    void shouldCreateNewPayment() throws Exception {
        // Arrange
        ZakatPaymentRequest request = new ZakatPaymentRequest(
                new BigDecimal("100.00"),
                "Test payment"
        );

        ZakatPayment payment = new ZakatPayment(
                "user123",
                new BigDecimal("100.00"),
                LocalDate.now(),
                "Test payment"
        );

        when(zakatPaymentService.createPayment(any(), any())).thenReturn(payment);

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value("100.0"))
                .andExpect(jsonPath("$.description").value("Test payment"));
    }

    @Test
    @DisplayName("Should get all payments")
    @WithMockUser
    void shouldGetAllPayments() throws Exception {
        // Arrange
        when(zakatPaymentService.getAllPayments()).thenReturn(
                Arrays.asList(
                        new ZakatPayment("user123", new BigDecimal("100.00"), LocalDate.now(), "Payment 1"),
                        new ZakatPayment("user123", new BigDecimal("150.00"), LocalDate.now(), "Payment 2")
                )
        );

        // Act & Assert
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value("100.0"))
                .andExpect(jsonPath("$[1].amount").value("150.0"));
    }

    @Test
    @DisplayName("Should get remaining zakat to pay")
    @WithMockUser
    void shouldGetRemainingZakatToPay() throws Exception {
        // Arrange
        BigDecimal totalDue = new BigDecimal("250.00");
        when(zakatPaymentService.getRemainingZakatToPay(totalDue))
                .thenReturn(new BigDecimal("100.00"));

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/remaining")
                        .param("totalDue", "250.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("100.0"));
    }

    @Test
    @DisplayName("Should get total payments for current year")
    @WithMockUser
    void shouldGetTotalPaymentsForCurrentYear() throws Exception {
        // Arrange
        when(zakatPaymentService.getTotalPaymentsForCurrentYear())
                .thenReturn(new BigDecimal("150.00"));

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("150.0"));
    }
}

