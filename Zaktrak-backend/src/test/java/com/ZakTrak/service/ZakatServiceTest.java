package com.ZakTrak.service;

import com.ZakTrak.model.Asset;
import com.ZakTrak.model.Nisab;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZakatServiceTest {

    @Mock
    private AssetService assetService;

    @Mock
    private NisabService nisabService;

    @InjectMocks
    private ZakatService zakatService;

    @Test
    void shouldCalculateZakatWhenAboveNisab() {
        when(assetService.calculateTotalZakatableValue())
                .thenReturn(new BigDecimal("10000.00"));

        when(nisabService.getLatestNisabThreshold())
                .thenReturn(new Nisab(
                        new BigDecimal("85.00"),
                        new BigDecimal("0.95"),
                        LocalDate.now()
                ));

        BigDecimal zakatDue = zakatService.calculateZakat("gold");

        assertThat(zakatDue).isEqualByComparingTo(new BigDecimal("250.00")); // 2.5% of 10000
    }

    @Test
    void shouldReturnZeroWhenBelowNisab() {
        when(assetService.calculateTotalZakatableValue())
                .thenReturn(new BigDecimal("5000.00"));

        when(nisabService.getLatestNisabThreshold())
                .thenReturn(new Nisab(
                        new BigDecimal("800.00"),
                        new BigDecimal("15.00"),
                        LocalDate.now()
                ));

        BigDecimal zakatDue = zakatService.calculateZakat("gold");

        assertThat(zakatDue).isEqualByComparingTo(BigDecimal.ZERO);
    }
}