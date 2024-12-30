package com.ZakTrak.controller;

import com.ZakTrak.model.Nisab;
import com.ZakTrak.service.NisabService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NisabControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NisabService nisabService;

    private final String BASE_URL = "/api/nisab";

    @Test
    @WithMockUser
    void shouldUpdateNisabThreshold() throws Exception {
        Nisab nisab = new Nisab(
                new BigDecimal("800.00"),
                new BigDecimal("15.00"),
                LocalDate.now()
        );
        when(nisabService.updateNisabThreshold(any(), any())).thenReturn(nisab);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"goldPrice\":800.00,\"silverPrice\":15.00}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.goldPricePerGram").value("800.0"))
                .andExpect(jsonPath("$.silverPricePerGram").value("15.0"));
    }

    @Test
    @WithMockUser
    void shouldGetLatestNisab() throws Exception {
        Nisab nisab = new Nisab(
                new BigDecimal("800.00"),
                new BigDecimal("15.00"),
                LocalDate.now()
        );
        when(nisabService.getLatestNisabThreshold()).thenReturn(nisab);

        mockMvc.perform(get(BASE_URL + "/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goldPricePerGram").value("800.0"));
    }
}