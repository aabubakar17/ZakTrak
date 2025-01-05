package com.ZakTrak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.ZakTrak.service.ZakatService;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ZakatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ZakatService zakatService;

    @Test
    @WithMockUser
    void shouldCalculateZakat() throws Exception {
        when(zakatService.calculateZakat())
                .thenReturn(new BigDecimal("250.00"));

        mockMvc.perform(get("/api/zakat/calculate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("250.0"));
    }





}
