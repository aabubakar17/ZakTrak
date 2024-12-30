package com.ZakTrak.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NisabTest {

    @Test
    void shouldCreateValidNisab() {
        Nisab nisab = new Nisab(
                new BigDecimal("800.00"),  // Gold price per gram
                new BigDecimal("15.00"),   // Silver price per gram
                LocalDate.now()
        );

        assertThat(nisab.getGoldNisab()).isEqualByComparingTo(new BigDecimal("68000.00")); // 85g * 800
        assertThat(nisab.getSilverNisab()).isEqualByComparingTo(new BigDecimal("9000.00")); // 600g * 15
    }

    @Test
    void shouldNotAllowNegativePrices() {
        assertThrows(IllegalArgumentException.class, () ->
                new Nisab(new BigDecimal("-1"), new BigDecimal("15.00"), LocalDate.now())
        );
    }
}